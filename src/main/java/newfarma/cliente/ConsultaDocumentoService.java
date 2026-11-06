package newfarma.cliente;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import newfarma.cliente.dto.ClienteConsultaDto;
import newfarma.model.Cliente;
import newfarma.model.Persona;
import newfarma.model.TipoPersona;
import newfarma.persona.PersonaRepositoryJPA;
import newfarma.tipopersona.TipoPersonaRepositoryJPA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
public class ConsultaDocumentoService {

    private final PersonaRepositoryJPA personaRepositoryJPA;
    private final ClienteRepositoryJPA clienteRepositoryJPA;
    private final TipoPersonaRepositoryJPA tipoPersonaRepositoryJPA;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Value("${api.consulta.token:}")
    private String apiToken;

    @Value("${api.consulta.dni-url:https://dniruc.apisperu.com/api/v1/dni/%s?token=%s}")
    private String dniUrlTemplate;

    @Value("${api.consulta.ruc-url:https://dniruc.apisperu.com/api/v1/ruc/%s?token=%s}")
    private String rucUrlTemplate;

    public ConsultaDocumentoService(
            PersonaRepositoryJPA personaRepositoryJPA,
            ClienteRepositoryJPA clienteRepositoryJPA,
            TipoPersonaRepositoryJPA tipoPersonaRepositoryJPA,
            ObjectMapper objectMapper) {
        this.personaRepositoryJPA = personaRepositoryJPA;
        this.clienteRepositoryJPA = clienteRepositoryJPA;
        this.tipoPersonaRepositoryJPA = tipoPersonaRepositoryJPA;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(6))
                .build();
    }

    @Transactional
    public ClienteConsultaDto consultarYRegistrar(String numero) {
        if (numero == null) {
            return ClienteConsultaDto.builder()
                    .exito(false)
                    .mensaje("El número de documento no puede ser nulo")
                    .build();
        }

        String doc = numero.trim();
        if (doc.length() == 8) {
            return procesarDni(doc);
        } else if (doc.length() == 11) {
            return procesarRuc(doc);
        } else {
            return ClienteConsultaDto.builder()
                    .exito(false)
                    .numeroDocumento(doc)
                    .mensaje("El documento debe tener 8 dígitos (DNI) o 11 dígitos (RUC)")
                    .build();
        }
    }

    private ClienteConsultaDto procesarDni(String dniStr) {
        Long dni;
        try {
            dni = Long.parseLong(dniStr);
        } catch (NumberFormatException e) {
            return ClienteConsultaDto.builder().exito(false).numeroDocumento(dniStr).mensaje("DNI inválido").build();
        }

        // 1. Buscar en BD local primero
        Optional<Cliente> clienteOpt = clienteRepositoryJPA.findByPersonaDni(dni);
        if (clienteOpt.isPresent()) {
            Cliente c = clienteOpt.get();
            Persona p = c.getPersona();
            String nombreCompleto = construirNombrePersona(p);
            return ClienteConsultaDto.builder()
                    .exito(true)
                    .idCliente(c.getIdcliente())
                    .idPersona(p.getIdpersona())
                    .tipoDocumento("DNI")
                    .numeroDocumento(dniStr)
                    .nombreCompleto(nombreCompleto)
                    .nombres(p.getNombre())
                    .apellidoPaterno(p.getPaterno())
                    .apellidoMaterno(p.getMaterno())
                    .direccion(p.getDireccion())
                    .origen("LOCAL")
                    .mensaje("Cliente obtenido de la base de datos local")
                    .build();
        }

        // Verificar si la persona existe pero aún no es cliente
        Optional<Persona> personaOpt = personaRepositoryJPA.findByDni(dni);
        if (personaOpt.isPresent()) {
            Persona p = personaOpt.get();
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setPersona(p);
            nuevoCliente = clienteRepositoryJPA.save(nuevoCliente);

            return ClienteConsultaDto.builder()
                    .exito(true)
                    .idCliente(nuevoCliente.getIdcliente())
                    .idPersona(p.getIdpersona())
                    .tipoDocumento("DNI")
                    .numeroDocumento(dniStr)
                    .nombreCompleto(construirNombrePersona(p))
                    .nombres(p.getNombre())
                    .apellidoPaterno(p.getPaterno())
                    .apellidoMaterno(p.getMaterno())
                    .direccion(p.getDireccion())
                    .origen("LOCAL")
                    .mensaje("Persona vinculada como cliente exitosamente")
                    .build();
        }

        // 2. No existe en BD local -> Consultar API externa RENIEC
        log.info("DNI {} no encontrado localmente. Consultando API externa...", dniStr);
        ExtPersonaDto ext = consultarApiDni(dniStr);
        if (ext == null || ext.nombres == null || ext.nombres.isBlank()) {
            return ClienteConsultaDto.builder()
                    .exito(false)
                    .tipoDocumento("DNI")
                    .numeroDocumento(dniStr)
                    .mensaje("No se pudo obtener información de RENIEC para el DNI " + dniStr)
                    .build();
        }

        // 3. Guardar en persona y cliente
        TipoPersona tipoNatural = tipoPersonaRepositoryJPA.findById(1L).orElse(null);

        Persona nuevaPersona = new Persona();
        nuevaPersona.setDni(dni);
        nuevaPersona.setNombre(ext.nombres);
        nuevaPersona.setPaterno(ext.apellidoPaterno);
        nuevaPersona.setMaterno(ext.apellidoMaterno);
        nuevaPersona.setTipoPersona(tipoNatural);
        nuevaPersona = personaRepositoryJPA.save(nuevaPersona);

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setPersona(nuevaPersona);
        nuevoCliente = clienteRepositoryJPA.save(nuevoCliente);

        String nombreCompleto = String.format("%s %s %s",
                ext.nombres,
                ext.apellidoPaterno != null ? ext.apellidoPaterno : "",
                ext.apellidoMaterno != null ? ext.apellidoMaterno : "").trim().replaceAll("\\s+", " ");

        log.info("Nuevo cliente registrado exitosamente desde RENIEC: {} (ID: {})", nombreCompleto, nuevoCliente.getIdcliente());

        return ClienteConsultaDto.builder()
                .exito(true)
                .idCliente(nuevoCliente.getIdcliente())
                .idPersona(nuevaPersona.getIdpersona())
                .tipoDocumento("DNI")
                .numeroDocumento(dniStr)
                .nombreCompleto(nombreCompleto)
                .nombres(ext.nombres)
                .apellidoPaterno(ext.apellidoPaterno)
                .apellidoMaterno(ext.apellidoMaterno)
                .origen("RENIEC")
                .mensaje("Cliente registrado automáticamente desde RENIEC")
                .build();
    }

    private ClienteConsultaDto procesarRuc(String rucStr) {
        Long ruc;
        try {
            ruc = Long.parseLong(rucStr);
        } catch (NumberFormatException e) {
            return ClienteConsultaDto.builder().exito(false).numeroDocumento(rucStr).mensaje("RUC inválido").build();
        }

        // 1. Buscar en BD local primero
        Optional<Cliente> clienteOpt = clienteRepositoryJPA.findByPersonaRuc(ruc);
        if (clienteOpt.isPresent()) {
            Cliente c = clienteOpt.get();
            Persona p = c.getPersona();
            return ClienteConsultaDto.builder()
                    .exito(true)
                    .idCliente(c.getIdcliente())
                    .idPersona(p.getIdpersona())
                    .tipoDocumento("RUC")
                    .numeroDocumento(rucStr)
                    .nombreCompleto(p.getNombre())
                    .direccion(p.getDireccion())
                    .origen("LOCAL")
                    .mensaje("Empresa obtenida de la base de datos local")
                    .build();
        }

        // Si existe persona con ese RUC
        Optional<Persona> personaOpt = personaRepositoryJPA.findByRuc(ruc);
        if (personaOpt.isPresent()) {
            Persona p = personaOpt.get();
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setPersona(p);
            nuevoCliente = clienteRepositoryJPA.save(nuevoCliente);

            return ClienteConsultaDto.builder()
                    .exito(true)
                    .idCliente(nuevoCliente.getIdcliente())
                    .idPersona(p.getIdpersona())
                    .tipoDocumento("RUC")
                    .numeroDocumento(rucStr)
                    .nombreCompleto(p.getNombre())
                    .direccion(p.getDireccion())
                    .origen("LOCAL")
                    .mensaje("Empresa vinculada como cliente exitosamente")
                    .build();
        }

        // 2. Consultar API externa SUNAT
        log.info("RUC {} no encontrado localmente. Consultando API externa...", rucStr);
        ExtEmpresaDto ext = consultarApiRuc(rucStr);
        if (ext == null || ext.razonSocial == null || ext.razonSocial.isBlank()) {
            return ClienteConsultaDto.builder()
                    .exito(false)
                    .tipoDocumento("RUC")
                    .numeroDocumento(rucStr)
                    .mensaje("No se pudo obtener información de SUNAT para el RUC " + rucStr)
                    .build();
        }

        TipoPersona tipoJuridica = tipoPersonaRepositoryJPA.findById(2L).orElse(null);

        Persona nuevaPersona = new Persona();
        nuevaPersona.setRuc(ruc);
        nuevaPersona.setNombre(ext.razonSocial);
        nuevaPersona.setDireccion(ext.direccion);
        nuevaPersona.setTipoPersona(tipoJuridica);
        nuevaPersona = personaRepositoryJPA.save(nuevaPersona);

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setPersona(nuevaPersona);
        nuevoCliente = clienteRepositoryJPA.save(nuevoCliente);

        log.info("Nuevo cliente registrado exitosamente desde SUNAT: {} (ID: {})", ext.razonSocial, nuevoCliente.getIdcliente());

        return ClienteConsultaDto.builder()
                .exito(true)
                .idCliente(nuevoCliente.getIdcliente())
                .idPersona(nuevaPersona.getIdpersona())
                .tipoDocumento("RUC")
                .numeroDocumento(rucStr)
                .nombreCompleto(ext.razonSocial)
                .direccion(ext.direccion)
                .estado(ext.estado)
                .condicion(ext.condicion)
                .origen("SUNAT")
                .mensaje("Empresa registrada automáticamente desde SUNAT")
                .build();
    }

    private ExtPersonaDto consultarApiDni(String dni) {
        try {
            String url = String.format(dniUrlTemplate, dni, apiToken != null ? apiToken : "");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(6))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode data = root.has("resultado") ? root.get("resultado") : (root.has("data") ? root.get("data") : root);

                ExtPersonaDto p = new ExtPersonaDto();
                p.nombres = getText(data, "nombres", "nombre");
                p.apellidoPaterno = getText(data, "apellidoPaterno", "apellido_paterno", "paterno");
                p.apellidoMaterno = getText(data, "apellidoMaterno", "apellido_materno", "materno");
                return p;
            } else {
                log.warn("API DNI respondió código HTTP {}", response.statusCode());
            }
        } catch (Exception e) {
            log.error("Error consultando API externa de DNI: {}", e.getMessage());
        }
        return null;
    }

    private ExtEmpresaDto consultarApiRuc(String ruc) {
        try {
            String url = String.format(rucUrlTemplate, ruc, apiToken != null ? apiToken : "");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(6))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode data = root.has("resultado") ? root.get("resultado") : (root.has("data") ? root.get("data") : root);

                ExtEmpresaDto emp = new ExtEmpresaDto();
                emp.razonSocial = getText(data, "razonSocial", "razon_social", "nombre");
                emp.direccion = getText(data, "direccion", "direccion_completa", "domicilioFiscal");
                emp.estado = getText(data, "estado", "estado_contribuyente");
                emp.condicion = getText(data, "condicion", "condicion_domicilio");
                return emp;
            } else {
                log.warn("API RUC respondió código HTTP {}", response.statusCode());
            }
        } catch (Exception e) {
            log.error("Error consultando API externa de RUC: {}", e.getMessage());
        }
        return null;
    }

    private String getText(JsonNode node, String... fieldNames) {
        for (String field : fieldNames) {
            if (node.hasNonNull(field)) {
                String val = node.get(field).asText().trim();
                if (!val.isBlank()) {
                    return val;
                }
            }
        }
        return null;
    }

    private String construirNombrePersona(Persona p) {
        if (p == null) return "";
        StringBuilder sb = new StringBuilder();
        if (p.getNombre() != null) sb.append(p.getNombre()).append(" ");
        if (p.getPaterno() != null) sb.append(p.getPaterno()).append(" ");
        if (p.getMaterno() != null) sb.append(p.getMaterno());
        return sb.toString().trim().replaceAll("\\s+", " ");
    }

    private static class ExtPersonaDto {
        String nombres;
        String apellidoPaterno;
        String apellidoMaterno;
    }

    private static class ExtEmpresaDto {
        String razonSocial;
        String direccion;
        String estado;
        String condicion;
    }
}

