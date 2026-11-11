package newfarma.cliente;

import com.fasterxml.jackson.databind.ObjectMapper;
import newfarma.cliente.dto.ClienteConsultaDto;
import newfarma.model.Cliente;
import newfarma.model.Persona;
import newfarma.model.TipoPersona;
import newfarma.persona.PersonaRepositoryJPA;
import newfarma.tipopersona.TipoPersonaRepositoryJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - ConsultaDocumentoService")
class ConsultaDocumentoServiceTest {

    @Mock
    private PersonaRepositoryJPA personaRepositoryJPA;

    @Mock
    private ClienteRepositoryJPA clienteRepositoryJPA;

    @Mock
    private TipoPersonaRepositoryJPA tipoPersonaRepositoryJPA;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private ConsultaDocumentoService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "apiToken", "dummy-token");
        ReflectionTestUtils.setField(service, "dniUrlTemplate", "https://dniruc.apisperu.com/api/v1/dni/%s?token=%s");
        ReflectionTestUtils.setField(service, "rucUrlTemplate", "https://dniruc.apisperu.com/api/v1/ruc/%s?token=%s");
    }

    @Nested
    @DisplayName("Validación básica de argumentos")
    class ValidacionArgumentosTests {

        @Test
        @DisplayName("Debe fallar cuando el número es nulo")
        void whenNumeroIsNull_ShouldReturnError() {
            ClienteConsultaDto res = service.consultarYRegistrar(null);
            assertFalse(res.isExito());
            assertEquals("El número de documento no puede ser nulo", res.getMensaje());
        }

        @Test
        @DisplayName("Debe fallar cuando la longitud no es de 8 ni 11 dígitos")
        void whenInvalidLength_ShouldReturnError() {
            ClienteConsultaDto res = service.consultarYRegistrar("12345");
            assertFalse(res.isExito());
            assertTrue(res.getMensaje().contains("8 dígitos (DNI) o 11 dígitos (RUC)"));
        }

        @Test
        @DisplayName("Debe fallar cuando el DNI contiene caracteres no numéricos")
        void whenDniNotNumeric_ShouldReturnError() {
            ClienteConsultaDto res = service.consultarYRegistrar("1234ABCD");
            assertFalse(res.isExito());
            assertEquals("DNI inválido", res.getMensaje());
        }

        @Test
        @DisplayName("Debe fallar cuando el RUC contiene caracteres no numéricos")
        void whenRucNotNumeric_ShouldReturnError() {
            ClienteConsultaDto res = service.consultarYRegistrar("2012345678A");
            assertFalse(res.isExito());
            assertEquals("RUC inválido", res.getMensaje());
        }
    }

    @Nested
    @DisplayName("Procesamiento de DNI")
    class ProcesarDniTests {

        @Test
        @DisplayName("Debe retornar cliente local si ya está registrado en tabla cliente")
        void whenDniExistsInCliente_ShouldReturnLocal() {
            String dniStr = "43705826";
            Long dni = 43705826L;

            Persona persona = new Persona();
            persona.setIdpersona(10L);
            persona.setDni(dni);
            persona.setNombre("JUAN");
            persona.setPaterno("PEREZ");
            persona.setMaterno("LOPEZ");

            Cliente cliente = new Cliente();
            cliente.setIdcliente(1L);
            cliente.setPersona(persona);

            when(clienteRepositoryJPA.findByPersonaDni(dni)).thenReturn(Optional.of(cliente));

            ClienteConsultaDto res = service.consultarYRegistrar(dniStr);

            assertTrue(res.isExito());
            assertEquals("LOCAL", res.getOrigen());
            assertEquals(1L, res.getIdCliente());
            assertEquals("JUAN PEREZ LOPEZ", res.getNombreCompleto());
            verify(clienteRepositoryJPA, times(1)).findByPersonaDni(dni);
            verify(personaRepositoryJPA, never()).findByDni(any());
        }

        @Test
        @DisplayName("Debe crear cliente si la persona existe pero aún no es cliente")
        void whenDniExistsInPersonaOnly_ShouldLinkClienteAndReturn() {
            String dniStr = "43705826";
            Long dni = 43705826L;

            Persona persona = new Persona();
            persona.setIdpersona(10L);
            persona.setDni(dni);
            persona.setNombre("MARIA");
            persona.setPaterno("GARCIA");

            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setIdcliente(5L);
            nuevoCliente.setPersona(persona);

            when(clienteRepositoryJPA.findByPersonaDni(dni)).thenReturn(Optional.empty());
            when(personaRepositoryJPA.findByDni(dni)).thenReturn(Optional.of(persona));
            when(clienteRepositoryJPA.save(any(Cliente.class))).thenReturn(nuevoCliente);

            ClienteConsultaDto res = service.consultarYRegistrar(dniStr);

            assertTrue(res.isExito());
            assertEquals("LOCAL", res.getOrigen());
            assertEquals(5L, res.getIdCliente());
            assertEquals("MARIA GARCIA", res.getNombreCompleto());
            verify(clienteRepositoryJPA, times(1)).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Debe registrar nuevo cliente desde API externa RENIEC exitosamente")
        void whenDniApiSuccess_ShouldRegisterNewCliente() throws Exception {
            String dniStr = "72345678";
            Long dni = 72345678L;

            java.net.http.HttpClient mockHttpClient = mock(java.net.http.HttpClient.class);
            java.net.http.HttpResponse<String> mockResponse = mock(java.net.http.HttpResponse.class);
            ReflectionTestUtils.setField(service, "httpClient", mockHttpClient);

            when(clienteRepositoryJPA.findByPersonaDni(dni)).thenReturn(Optional.empty());
            when(personaRepositoryJPA.findByDni(dni)).thenReturn(Optional.empty());
            when(mockResponse.statusCode()).thenReturn(200);
            when(mockResponse.body()).thenReturn("{\"nombres\":\"CARLOS\",\"apellidoPaterno\":\"QUISPE\",\"apellidoMaterno\":\"RAMOS\"}");
            doReturn(mockResponse).when(mockHttpClient).send(any(), any());

            TipoPersona tipoNatural = new TipoPersona(1L, "NATURAL");
            when(tipoPersonaRepositoryJPA.findById(1L)).thenReturn(Optional.of(tipoNatural));

            Persona personaGuardada = new Persona();
            personaGuardada.setIdpersona(100L);
            personaGuardada.setDni(dni);
            personaGuardada.setNombre("CARLOS");
            personaGuardada.setPaterno("QUISPE");
            personaGuardada.setMaterno("RAMOS");
            when(personaRepositoryJPA.save(any(Persona.class))).thenReturn(personaGuardada);

            Cliente clienteGuardado = new Cliente();
            clienteGuardado.setIdcliente(50L);
            clienteGuardado.setPersona(personaGuardada);
            when(clienteRepositoryJPA.save(any(Cliente.class))).thenReturn(clienteGuardado);

            ClienteConsultaDto res = service.consultarYRegistrar(dniStr);

            assertTrue(res.isExito());
            assertEquals("RENIEC", res.getOrigen());
            assertEquals(50L, res.getIdCliente());
            assertEquals("CARLOS QUISPE RAMOS", res.getNombreCompleto());
            verify(personaRepositoryJPA, times(1)).save(any(Persona.class));
            verify(clienteRepositoryJPA, times(1)).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Debe retornar error si no existe localmente ni en la API externa")
        void whenDniNotFoundInApi_ShouldReturnError() {
            String dniStr = "00000000";
            Long dni = 0L;

            when(clienteRepositoryJPA.findByPersonaDni(dni)).thenReturn(Optional.empty());
            when(personaRepositoryJPA.findByDni(dni)).thenReturn(Optional.empty());

            ClienteConsultaDto res = service.consultarYRegistrar(dniStr);

            assertFalse(res.isExito());
            assertEquals("DNI", res.getTipoDocumento());
            assertTrue(res.getMensaje().contains("No se pudo obtener información"));
        }
    }

    @Nested
    @DisplayName("Procesamiento de RUC")
    class ProcesarRucTests {

        @Test
        @DisplayName("Debe retornar cliente local si el RUC ya está registrado en tabla cliente")
        void whenRucExistsInCliente_ShouldReturnLocal() {
            String rucStr = "20601234567";
            Long ruc = 20601234567L;

            Persona persona = new Persona();
            persona.setIdpersona(20L);
            persona.setRuc(ruc);
            persona.setNombre("DISTRIBUIDORA NORTE S.A.C.");
            persona.setDireccion("AV. ESPAÑA 123");

            Cliente cliente = new Cliente();
            cliente.setIdcliente(2L);
            cliente.setPersona(persona);

            when(clienteRepositoryJPA.findByPersonaRuc(ruc)).thenReturn(Optional.of(cliente));

            ClienteConsultaDto res = service.consultarYRegistrar(rucStr);

            assertTrue(res.isExito());
            assertEquals("LOCAL", res.getOrigen());
            assertEquals(2L, res.getIdCliente());
            assertEquals("DISTRIBUIDORA NORTE S.A.C.", res.getNombreCompleto());
            verify(clienteRepositoryJPA, times(1)).findByPersonaRuc(ruc);
            verify(personaRepositoryJPA, never()).findByRuc(any());
        }

        @Test
        @DisplayName("Debe crear cliente si el RUC existe en persona pero aún no es cliente")
        void whenRucExistsInPersonaOnly_ShouldLinkClienteAndReturn() {
            String rucStr = "20601234567";
            Long ruc = 20601234567L;

            Persona persona = new Persona();
            persona.setIdpersona(25L);
            persona.setRuc(ruc);
            persona.setNombre("COMERCIAL SAN CARLOS S.R.L.");

            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setIdcliente(8L);
            nuevoCliente.setPersona(persona);

            when(clienteRepositoryJPA.findByPersonaRuc(ruc)).thenReturn(Optional.empty());
            when(personaRepositoryJPA.findByRuc(ruc)).thenReturn(Optional.of(persona));
            when(clienteRepositoryJPA.save(any(Cliente.class))).thenReturn(nuevoCliente);

            ClienteConsultaDto res = service.consultarYRegistrar(rucStr);

            assertTrue(res.isExito());
            assertEquals("LOCAL", res.getOrigen());
            assertEquals(8L, res.getIdCliente());
            assertEquals("COMERCIAL SAN CARLOS S.R.L.", res.getNombreCompleto());
            verify(clienteRepositoryJPA, times(1)).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Debe registrar nueva empresa desde API externa SUNAT exitosamente")
        void whenRucApiSuccess_ShouldRegisterNewEmpresa() throws Exception {
            String rucStr = "20556677889";
            Long ruc = 20556677889L;

            java.net.http.HttpClient mockHttpClient = mock(java.net.http.HttpClient.class);
            java.net.http.HttpResponse<String> mockResponse = mock(java.net.http.HttpResponse.class);
            ReflectionTestUtils.setField(service, "httpClient", mockHttpClient);

            when(clienteRepositoryJPA.findByPersonaRuc(ruc)).thenReturn(Optional.empty());
            when(personaRepositoryJPA.findByRuc(ruc)).thenReturn(Optional.empty());
            when(mockResponse.statusCode()).thenReturn(200);
            when(mockResponse.body()).thenReturn("{\"razonSocial\":\"LIBRERIA CENTRAL S.A.C.\",\"direccion\":\"CALLE REAL 456\",\"estado\":\"ACTIVO\",\"condicion\":\"HABIDO\"}");
            doReturn(mockResponse).when(mockHttpClient).send(any(), any());

            TipoPersona tipoJuridica = new TipoPersona(2L, "JURIDICA");
            when(tipoPersonaRepositoryJPA.findById(2L)).thenReturn(Optional.of(tipoJuridica));

            Persona personaGuardada = new Persona();
            personaGuardada.setIdpersona(101L);
            personaGuardada.setRuc(ruc);
            personaGuardada.setNombre("LIBRERIA CENTRAL S.A.C.");
            personaGuardada.setDireccion("CALLE REAL 456");
            when(personaRepositoryJPA.save(any(Persona.class))).thenReturn(personaGuardada);

            Cliente clienteGuardado = new Cliente();
            clienteGuardado.setIdcliente(51L);
            clienteGuardado.setPersona(personaGuardada);
            when(clienteRepositoryJPA.save(any(Cliente.class))).thenReturn(clienteGuardado);

            ClienteConsultaDto res = service.consultarYRegistrar(rucStr);

            assertTrue(res.isExito());
            assertEquals("SUNAT", res.getOrigen());
            assertEquals(51L, res.getIdCliente());
            assertEquals("LIBRERIA CENTRAL S.A.C.", res.getNombreCompleto());
            assertEquals("ACTIVO", res.getEstado());
            assertEquals("HABIDO", res.getCondicion());
            verify(personaRepositoryJPA, times(1)).save(any(Persona.class));
            verify(clienteRepositoryJPA, times(1)).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Debe retornar error si el RUC no existe localmente ni en la API externa")
        void whenRucNotFoundInApi_ShouldReturnError() {
            String rucStr = "20000000001";
            Long ruc = 20000000001L;

            when(clienteRepositoryJPA.findByPersonaRuc(ruc)).thenReturn(Optional.empty());
            when(personaRepositoryJPA.findByRuc(ruc)).thenReturn(Optional.empty());

            ClienteConsultaDto res = service.consultarYRegistrar(rucStr);

            assertFalse(res.isExito());
            assertEquals("RUC", res.getTipoDocumento());
            assertTrue(res.getMensaje().contains("No se pudo obtener información"));
        }
    }
}

