-- ==============================================================================
-- Repeatable Migration: Vistas y Procedimientos Almacenados
-- Flyway ejecutará este script cada vez que su contenido/checksum cambie.
-- ==============================================================================

-- ------------------------------------------------------------------------------
-- 1. VISTAS (VIEWS)
-- ------------------------------------------------------------------------------

CREATE OR REPLACE VIEW `listarproducto` AS 
SELECT 
    `p`.`idproducto` AS `idproducto`,
    `p`.`codigoproducto` AS `codigoproducto`,
    `p`.`codbarra` AS `codbarra`,
    `p`.`nombre` AS `nombre`,
    `p`.`composicion` AS `composicion`,
    `p`.`estado` AS `estado`,
    `p`.`vencimiento` AS `vencimiento`,
    `p`.`ubicacion` AS `ubicacion`,
    `pre`.`nombrepresentacion` AS `nombrepresentacion`,
    `um`.`nombreunidad` AS `nombreunidad`,
    `lab`.`nombrelaboratorio` AS `nombrelaboratorio`,
    `p`.`stock` AS `stock`,
    `p`.`precioventa` AS `precioventa`,
    `p`.`presentacion_idpresentacion` AS `presentacion_idpresentacion`,
    `p`.`unidadmedida_idunidadmedida` AS `unidadmedida_idunidadmedida`,
    `p`.`laboratorio_idlaboratorio` AS `laboratorio_idlaboratorio`,
    `p`.`precioblister` AS `precioblister`,
    `p`.`preciocaja` AS `preciocaja` 
FROM `producto` `p` 
JOIN `presentacion` `pre` ON `p`.`presentacion_idpresentacion` = `pre`.`idpresentacion`
JOIN `laboratorio` `lab` ON `p`.`laboratorio_idlaboratorio` = `lab`.`idlaboratorio`
JOIN `unidadmedida` `um` ON `p`.`unidadmedida_idunidadmedida` = `um`.`idunidadmedida`;

CREATE OR REPLACE VIEW `lproductoventa` AS 
SELECT 
    `p`.`idproducto` AS `ID`,
    `p`.`codigoproducto` AS `CODIGO`,
    `p`.`codbarra` AS `COD_BARRA`,
    `p`.`nombre` AS `NOMBRE`,
    `p`.`composicion` AS `COMPOSICION`,
    MAX(`dc`.`costounidad`) AS `P_COMPRA`,
    `p`.`precioventa` AS `P_VENTA`,
    `p`.`precioblister` AS `P_BLIS`,
    `p`.`preciocaja` AS `P_CAJA`,
    `p`.`stock` AS `STOCK`,
    `p`.`vencimiento` AS `VCTO`,
    `um`.`nombreunidad` AS `U_M`,
    `lab`.`nombrelaboratorio` AS `LABORATORIO`,
    `pre`.`nombrepresentacion` AS `PRESENTACION`,
    `p`.`ubicacion` AS `UBICACION`,
    `p`.`estado` AS `ESTADO`,
    MAX(`dc`.`lote`) AS `LOTE`,
    `p`.`presentacion_idpresentacion` AS `ID_P`,
    `p`.`unidadmedida_idunidadmedida` AS `ID_UM`,
    `p`.`laboratorio_idlaboratorio` AS `ID_LAB` 
FROM `producto` `p` 
JOIN `presentacion` `pre` ON `p`.`presentacion_idpresentacion` = `pre`.`idpresentacion`
JOIN `laboratorio` `lab` ON `p`.`laboratorio_idlaboratorio` = `lab`.`idlaboratorio`
JOIN `unidadmedida` `um` ON `p`.`unidadmedida_idunidadmedida` = `um`.`idunidadmedida`
JOIN `detallecompra` `dc` ON `p`.`idproducto` = `dc`.`producto_idproducto`
GROUP BY `p`.`idproducto`;

CREATE OR REPLACE VIEW `vista_empleado` AS 
SELECT 
    `e`.`idempleado` AS `idempleado`,
    `es`.`estado` AS `Estado_empleado`,
    `p`.`nombre` AS `NOMBRE`,
    `p`.`paterno` AS `PATERNO`,
    `p`.`materno` AS `MATERNO`,
    `p`.`dni` AS `DNI`,
    `p`.`fechanacimiento` AS `F_v`,
    `tp`.`nombre` AS `TIPO_PERSONA`,
    `p`.`sexo` AS `SEXO`,
    `p`.`telefono` AS `TELEFONO`,
    `p`.`correo` AS `CORREO`,
    `p`.`direccion` AS `DIRECCION`,
    `u`.`nombreusuario` AS `USER`,
    `u`.`clave` AS `CLAVE`,
    `c`.`nombrecargo` AS `CARGO`,
    `est`.`estado` AS `Estado_Cargo` 
FROM `empleado` `e` 
JOIN `persona` `p` ON `e`.`persona_idpersona` = `p`.`idpersona`
JOIN `estado` `es` ON `e`.`estado_idestado` = `es`.`idestado`
JOIN `usuario` `u` ON `e`.`usuario_idusuario` = `u`.`idusuario`
JOIN `cargo` `c` ON `u`.`cargo_idcargo` = `c`.`idcargo`
JOIN `estado` `est` ON `c`.`estado_idestado` = `est`.`idestado`
JOIN `tipopersona` `tp` ON `p`.`tipopersona_idtipopersona` = `tp`.`idtipopersona`;

-- ------------------------------------------------------------------------------
-- 2. PROCEDIMIENTOS ALMACENADOS (STORED PROCEDURES)
-- ------------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS `Comprobante`;
DELIMITER //
CREATE PROCEDURE `Comprobante`(
    IN `SERIEVENTA` VARCHAR(50)
)
BEGIN
    SELECT 
        tc.nombrecomprobante AS comprobante,
        v.serie AS serie,
        v.correlativo AS correltivo,
        v.fechaventa AS fechaventa,
        CONCAT(p.nombre, ' ', p.paterno, ' ', p.materno) AS cliente,
        p.dni AS dni,
        p.ruc AS ruc,
        p.direccion AS direccion,
        pp.nombre AS producto,
        d.unidades AS cantidad,
        d.costounidad AS Costounidaddv,
        d.subtotal AS subtotaldv,
        d.descuentounidad AS dcto,
        d.total AS totaldv,
        v.subtotal AS subtotalv,
        v.igv AS igv,
        v.costoventa AS totalventa,
        u.nombreusuario
    FROM detalleventa d 
    INNER JOIN producto pp ON d.producto_idproducto = pp.idproducto 
    INNER JOIN venta v ON d.venta_idventa = v.idventa 
    INNER JOIN cliente cl ON v.cliente_idcliente = cl.idcliente 
    INNER JOIN persona p ON cl.persona_idpersona = p.idpersona 
    INNER JOIN tipocomprobante tc ON v.tipocomprobante_idtipocomprobante = tc.idtipocomprobante 
    INNER JOIN empleado em ON v.empleado_idempleado = em.idempleado 
    INNER JOIN usuario u ON em.usuario_idusuario = u.idusuario 
    WHERE v.serie = SERIEVENTA;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS `pdetallecompra`;
DELIMITER //
CREATE PROCEDURE `pdetallecompra`(
    IN `idcompra` INT
)
BEGIN
    SELECT 
        iddetallecompra AS ID,
        codigodetallecompra AS CODIGO,
        compra_idcompra AS CCOMPRA,
        P.nombre AS PPRODUCTO,
        lote AS LOTE,
        unidades AS UNIDADES,
        costounidad AS COSTO,
        total AS TOTAL
    FROM detallecompra DC 
    JOIN producto P ON DC.producto_idproducto = P.idproducto 
    WHERE codigodetallecompra = idcompra;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS `RepCompdnifecha`;
DELIMITER //
CREATE PROCEDURE `RepCompdnifecha`(
    IN `dni` BIGINT,
    IN `fecha` DATETIME
)
BEGIN
    SELECT 
        d.codigodetalleventa,
        d.venta_idventa,
        pp.nombre,
        d.unidades,
        d.costounidad,
        d.total,
        v.serie,
        p.direccion,
        p.dni,
        CONCAT(p.Nombre, ' ', p.paterno, ' ', p.materno) AS cliente,
        tc.nombrecomprobante,
        u.nombreusuario,
        v.fechaventa,
        v.igv,
        v.subtotal,
        v.costoventa,
        v.correlativo
    FROM detalleventa d 
    INNER JOIN producto pp ON d.producto_idproducto = pp.idproducto 
    INNER JOIN venta v ON d.venta_idventa = v.idventa 
    INNER JOIN cliente cl ON v.cliente_idcliente = cl.idcliente 
    INNER JOIN persona p ON cl.persona_idpersona = p.idpersona 
    INNER JOIN tipocomprobante tc ON v.tipocomprobante_idtipocomprobante = tc.idtipocomprobante 
    INNER JOIN empleado em ON v.empleado_idempleado = em.idempleado 
    INNER JOIN usuario u ON em.usuario_idusuario = u.idusuario 
    WHERE p.dni = dni AND v.fechaventa = fecha;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS `ReporteCompras`;
DELIMITER //
CREATE PROCEDURE `ReporteCompras`(
    IN `idcompra` INT
)
BEGIN
    SELECT 
        dc.codigodetallecompra,
        dc.compra_idcompra,
        p.nombre,
        dc.unidades,
        dc.costounidad,
        dc.total 
    FROM detallecompra dc 
    JOIN producto p ON dc.producto_idproducto = p.idproducto 
    WHERE dc.compra_idcompra = idcompra;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS `Reporteventas`;
DELIMITER //
CREATE PROCEDURE `Reporteventas`(
    IN `dni` VARCHAR(50),
    IN `idventa` VARCHAR(50)
)
BEGIN
    SELECT 
        d.codigodetalleventa,
        d.venta_idventa,
        pp.nombre,
        d.unidades,
        d.costounidad,
        d.total,
        p.direccion,
        p.dni,
        CONCAT(p.Nombre, ' ', p.paterno, ' ', p.materno) AS cliente,
        tc.nombrecomprobante,
        u.nombreusuario,
        v.fechaventa,
        v.igv,
        v.subtotal,
        v.costoventa                  
    FROM detalleventa d 
    INNER JOIN producto pp ON d.producto_idproducto = pp.idproducto 
    INNER JOIN venta v ON d.venta_idventa = v.idventa 
    INNER JOIN cliente cl ON v.cliente_idcliente = cl.idcliente 
    INNER JOIN persona p ON cl.persona_idpersona = p.idpersona 
    INNER JOIN tipocomprobante tc ON v.tipocomprobante_idtipocomprobante = tc.idtipocomprobante 
    INNER JOIN empleado em ON v.empleado_idempleado = em.idempleado 
    INNER JOIN usuario u ON em.usuario_idusuario = u.idusuario 
    WHERE p.dni = dni AND v.idventa = idventa;
END //
DELIMITER ;

