package newfarma.venta;

import jakarta.persistence.EntityNotFoundException;
import newfarma.model.Persona;
import newfarma.model.Producto;
import newfarma.model.Venta;
import newfarma.productos.ProductoRepositoryJPA;
import newfarma.venta.dto.DashboardKpiResponse;
import newfarma.venta.dto.VentaListRequest;
import newfarma.venta.dto.VentaListResponse;
import newfarma.venta.dto.VentaRequestDto;
import newfarma.utils.BaseResponse;
import newfarma.utils.PageableUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class VentaService implements VentaServiceI {
    private final ventaRepository repository;
    private final VentaRepositoryJPA ventaRepositoryJPA;
    private final ProductoRepositoryJPA productoRepositoryJPA;

    public VentaService(VentaRepositoryJPA ventaRepositoryJPA, ventaRepository repository, ProductoRepositoryJPA productoRepositoryJPA){
        this.ventaRepositoryJPA = ventaRepositoryJPA;
        this.repository = repository;
        this.productoRepositoryJPA = productoRepositoryJPA;
    }

    @Override
    public VentaListResponse list(VentaListRequest params) {
        Pageable pageable = PageableUtils.createPageable(params.getPage(), params.getXpage());
        params.setPage(pageable.getPageNumber() + 1);
        params.setXpage(pageable.getPageSize());
        params.setOffset((int) pageable.getOffset());

        List<Venta> l = (List<Venta>) repository.list(params, "L");
        Long total = (Long) repository.list(params, "T");
        return VentaListResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .total(total)
                .xpage(pageable.getPageSize())
                .list(l)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse save(VentaRequestDto ventaDto) {
        Venta venta = new Venta();
        venta.setIdventa(ventaDto.getIdVenta());
        venta.setSerie(ventaDto.getSerie());
        venta.setCorrelativo(ventaDto.getCorrelativo());
        venta.setFechaventa(ventaDto.getFechaVenta());
        venta.setIgv(ventaDto.getIgv());
        venta.setSubtotal(ventaDto.getSubtotal());
        venta.setCostoventa(ventaDto.getCostoVenta());
        venta.setCliente(ventaDto.getCliente());
        venta.setEmpleado(ventaDto.getEmpleado());
        venta.setTipoComprobante(ventaDto.getTipoComprobante());
        return save(venta);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse save(Venta venta){
        BaseResponse response;
        Venta venta1;
        if(venta.getIdventa() != null) // actualiza un objeto existente
        {
            venta1 = ventaRepositoryJPA.findById(venta.getIdventa()).get();
            venta1.setCorrelativo(venta.getCorrelativo());
            venta1.setSerie(venta.getSerie());
            venta1.setFechaventa(venta.getFechaventa());
            venta1.setIgv(venta.getIgv());
            venta1.setSubtotal(venta.getSubtotal());
            venta1.setCostoventa(venta.getCostoventa());
            venta1.setCliente(venta.getCliente());
            venta1.setEmpleado(venta.getEmpleado());
            venta1.setTipoComprobante(venta.getTipoComprobante());
            ventaRepositoryJPA.save(venta1);
            response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("UPDATE SUCESSFULLY").build();
        } else // crea un nuevo objeto
        {
            boolean existName = ventaRepositoryJPA.existsVentaByCorrelativo(venta.getCorrelativo());
            if (!existName){
               Long idventa = ventaRepositoryJPA.save(venta).getIdventa();

                response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("SAVED SUCESSFULLY")
                        .idVenta(idventa)
                        .build();
            } else {
                response = BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS BY CORRELATIVO EXISTE IN DB").build();
            }
        }

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse eliminar(Long id) {
        BaseResponse response;
        Venta venta = ventaRepositoryJPA.findById(id).orElseThrow(EntityNotFoundException::new);
        if (venta.getIdventa() != null)
        {
            ventaRepositoryJPA.delete(venta);
            response = BaseResponse.builder().status(200).code(String.valueOf(HttpStatus.OK)).message("DELETE SUCESSFULLY").build();
        } else {
            response = BaseResponse.builder().status(500).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).message("NOT SUCESS").build();
        }
        return response;
    }

    @Override
    public DashboardKpiResponse getDashboardKpis() {
        // 1. Resumen Ventas Hoy
        Double ventasHoyMonto = 0.0;
        Integer ventasHoyCantidad = 0;
        List<Object[]> hoyRows = ventaRepositoryJPA.getVentasHoySummary();
        if (hoyRows != null && !hoyRows.isEmpty() && hoyRows.get(0) != null) {
            Object[] r = hoyRows.get(0);
            ventasHoyMonto = r[0] != null ? ((Number) r[0]).doubleValue() : 0.0;
            ventasHoyCantidad = r[1] != null ? ((Number) r[1]).intValue() : 0;
        }
        Double ticketPromedio = ventasHoyCantidad > 0 ? (ventasHoyMonto / ventasHoyCantidad) : 0.0;

        // 2. Resumen Ventas Mes
        Double ventasMesMonto = 0.0;
        Integer ventasMesCantidad = 0;
        List<Object[]> mesRows = ventaRepositoryJPA.getVentasMesSummary();
        if (mesRows != null && !mesRows.isEmpty() && mesRows.get(0) != null) {
            Object[] r = mesRows.get(0);
            ventasMesMonto = r[0] != null ? ((Number) r[0]).doubleValue() : 0.0;
            ventasMesCantidad = r[1] != null ? ((Number) r[1]).intValue() : 0;
        }

        // 3. Resumen Stock Productos
        Integer stockCritico = 0;
        Integer stockAgotado = 0;
        Integer totalProductos = 0;
        if (productoRepositoryJPA != null) {
            List<Object[]> prodRows = productoRepositoryJPA.getStockAlertSummary();
            if (prodRows != null && !prodRows.isEmpty() && prodRows.get(0) != null) {
                Object[] r = prodRows.get(0);
                stockCritico = r[0] != null ? ((Number) r[0]).intValue() : 0;
                stockAgotado = r[1] != null ? ((Number) r[1]).intValue() : 0;
                totalProductos = r[2] != null ? ((Number) r[2]).intValue() : 0;
            }
        }

        // 4. Ventas por hora hoy
        List<DashboardKpiResponse.VentasHoraDto> porHora = new ArrayList<>();
        List<Object[]> horaRows = ventaRepositoryJPA.getVentasPorHoraHoy();
        if (horaRows != null) {
            for (Object[] hr : horaRows) {
                porHora.add(DashboardKpiResponse.VentasHoraDto.builder()
                        .hora(hr[0] != null ? ((Number) hr[0]).intValue() : 0)
                        .cantidad(hr[1] != null ? ((Number) hr[1]).intValue() : 0)
                        .total(hr[2] != null ? ((Number) hr[2]).doubleValue() : 0.0)
                        .build());
            }
        }

        // 5. Ventas por comprobante mes
        List<DashboardKpiResponse.VentasComprobanteDto> porComp = new ArrayList<>();
        List<Object[]> compRows = ventaRepositoryJPA.getVentasPorComprobanteMes();
        if (compRows != null) {
            for (Object[] cr : compRows) {
                porComp.add(DashboardKpiResponse.VentasComprobanteDto.builder()
                        .tipoComprobante(cr[0] != null ? cr[0].toString() : "TICKET")
                        .cantidad(cr[1] != null ? ((Number) cr[1]).intValue() : 0)
                        .total(cr[2] != null ? ((Number) cr[2]).doubleValue() : 0.0)
                        .build());
            }
        }

        // 6. Top Vendedores mes
        List<DashboardKpiResponse.VendedorRendimientoDto> topVendedores = new ArrayList<>();
        List<Object[]> vendRows = ventaRepositoryJPA.getTopVendedoresMes();
        if (vendRows != null) {
            for (Object[] vr : vendRows) {
                topVendedores.add(DashboardKpiResponse.VendedorRendimientoDto.builder()
                        .idEmpleado(vr[0] != null ? ((Number) vr[0]).longValue() : null)
                        .vendedor(vr[1] != null && !vr[1].toString().isBlank() ? vr[1].toString() : "No registrado")
                        .cantidadVentas(vr[2] != null ? ((Number) vr[2]).intValue() : 0)
                        .totalMonto(vr[3] != null ? ((Number) vr[3]).doubleValue() : 0.0)
                        .build());
            }
        }

        return DashboardKpiResponse.builder()
                .ventasHoyMonto(ventasHoyMonto)
                .ventasHoyCantidad(ventasHoyCantidad)
                .ticketPromedioHoy(ticketPromedio)
                .ventasMesMonto(ventasMesMonto)
                .ventasMesCantidad(ventasMesCantidad)
                .productosStockCritico(stockCritico)
                .productosAgotados(stockAgotado)
                .totalProductos(totalProductos)
                .ventasPorHoraHoy(porHora)
                .ventasPorComprobanteMes(porComp)
                .topVendedoresMes(topVendedores)
                .build();
    }
}
