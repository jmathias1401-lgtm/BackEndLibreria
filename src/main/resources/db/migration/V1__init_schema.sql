-- ========================================================
-- V1__init_schema.sql
-- Migración inicial de esquema de base de datos para Librería / NewFarma
-- ========================================================

CREATE TABLE IF NOT EXISTS `estado` (
  `idestado` int NOT NULL AUTO_INCREMENT,
  `estado` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idestado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `cargo` (
  `idcargo` int NOT NULL AUTO_INCREMENT,
  `estado_idestado` int NOT NULL,
  `nombrecargo` varchar(45) DEFAULT NULL,
  `descripcion` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idcargo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `tipopersona` (
  `idtipopersona` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idtipopersona`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `persona` (
  `idpersona` bigint NOT NULL AUTO_INCREMENT,
  `tipopersona_idtipopersona` int NOT NULL,
  `dni` bigint DEFAULT NULL,
  `ruc` bigint DEFAULT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `materno` varchar(45) DEFAULT NULL,
  `paterno` varchar(45) DEFAULT NULL,
  `fechanacimiento` datetime DEFAULT NULL,
  `telefono` varchar(45) DEFAULT NULL,
  `correo` varchar(45) DEFAULT NULL,
  `sexo` char(1) DEFAULT NULL,
  `direccion` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idpersona`),
  KEY `idx_persona_dni` (`dni`),
  KEY `idx_persona_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `cliente` (
  `idcliente` bigint NOT NULL AUTO_INCREMENT,
  `persona_idpersona` int NOT NULL,
  PRIMARY KEY (`idcliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `proveedor` (
  `idproveedor` bigint NOT NULL AUTO_INCREMENT,
  `persona_idpersona` int NOT NULL,
  PRIMARY KEY (`idproveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `laboratorio` (
  `idlaboratorio` bigint NOT NULL AUTO_INCREMENT,
  `nombrelaboratorio` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`idlaboratorio`),
  UNIQUE KEY `uk_nombrelaboratorio` (`nombrelaboratorio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `presentacion` (
  `idpresentacion` bigint NOT NULL AUTO_INCREMENT,
  `nombrepresentacion` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idpresentacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `unidadmedida` (
  `idunidadmedida` int NOT NULL AUTO_INCREMENT,
  `nombreunidad` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idunidadmedida`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `producto` (
  `idproducto` bigint NOT NULL AUTO_INCREMENT,
  `codigoproducto` varchar(45) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `vencimiento` varchar(45) DEFAULT NULL,
  `estado` varchar(45) DEFAULT NULL,
  `composicion` varchar(500) DEFAULT NULL,
  `ubicacion` varchar(50) DEFAULT NULL,
  `presentacion_idpresentacion` bigint DEFAULT NULL,
  `unidadmedida_idunidadmedida` int DEFAULT NULL,
  `laboratorio_idlaboratorio` bigint DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `precioventa` double DEFAULT NULL,
  `precioblister` double DEFAULT NULL,
  `preciocaja` double DEFAULT NULL,
  `codbarra` varchar(50) DEFAULT NULL,
  `imagen_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idproducto`),
  KEY `fk_producto_laboratorio` (`laboratorio_idlaboratorio`),
  KEY `fk_producto_presentacion` (`presentacion_idpresentacion`),
  KEY `fk_producto_unidadmedida` (`unidadmedida_idunidadmedida`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `tipocomprobante` (
  `idtipocomprobante` int NOT NULL AUTO_INCREMENT,
  `nombrecomprobante` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idtipocomprobante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `usuario` (
  `idusuario` int NOT NULL AUTO_INCREMENT,
  `cargo_idcargo` int NOT NULL,
  `nombreusuario` varchar(45) DEFAULT NULL,
  `clave` varchar(255) DEFAULT NULL,
  `fechacreacion` datetime DEFAULT NULL,
  PRIMARY KEY (`idusuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `empleado` (
  `idempleado` int NOT NULL AUTO_INCREMENT,
  `estado_idestado` int NOT NULL,
  `persona_idpersona` int NOT NULL,
  `usuario_idusuario` int NOT NULL,
  PRIMARY KEY (`idempleado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `token` (
  `idtoken` int NOT NULL AUTO_INCREMENT,
  `token` varchar(512) NOT NULL,
  `revoked` tinyint NOT NULL DEFAULT '0',
  `expired` tinyint NOT NULL DEFAULT '0',
  `usuario_idusuario` int DEFAULT NULL,
  `token_tipe` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idtoken`),
  UNIQUE KEY `uk_token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `empresa` (
  `idempresa` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `direccion` varchar(50) DEFAULT NULL,
  `paginaweb` varchar(50) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `correo` varchar(50) DEFAULT NULL,
  `info1` varchar(50) DEFAULT NULL,
  `info2` varchar(50) DEFAULT NULL,
  `info3` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idempresa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `compra` (
  `idcompra` bigint NOT NULL AUTO_INCREMENT,
  `serie` varchar(45) DEFAULT NULL,
  `correlativo` int DEFAULT NULL,
  `proveedor_idproveedor` int NOT NULL,
  `tipocomprobante_idtipocomprobante` int NOT NULL,
  `empleado_idempleado` int NOT NULL,
  `fechacompra` datetime DEFAULT NULL,
  `costocompra` double DEFAULT NULL,
  PRIMARY KEY (`idcompra`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `detallecompra` (
  `iddetallecompra` bigint NOT NULL AUTO_INCREMENT,
  `codigodetallecompra` varchar(45) DEFAULT NULL,
  `compra_idcompra` int NOT NULL,
  `producto_idproducto` int NOT NULL,
  `lote` varchar(11) NOT NULL,
  `unidades` double DEFAULT NULL,
  `costounidad` double DEFAULT NULL,
  `total` double DEFAULT NULL,
  PRIMARY KEY (`iddetallecompra`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `venta` (
  `idventa` bigint NOT NULL AUTO_INCREMENT,
  `serie` varchar(4) NOT NULL,
  `correlativo` int DEFAULT NULL,
  `cliente_idcliente` int NOT NULL,
  `tipocomprobante_idtipocomprobante` int NOT NULL,
  `empleado_idempleado` int NOT NULL,
  `fechaventa` datetime DEFAULT NULL,
  `igv` double DEFAULT NULL,
  `subtotal` double DEFAULT NULL,
  `costoventa` double DEFAULT NULL,
  PRIMARY KEY (`idventa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `detalleventa` (
  `iddetalleventa` bigint NOT NULL AUTO_INCREMENT,
  `venta_idventa` int NOT NULL,
  `producto_idproducto` int NOT NULL,
  `codigodetalleventa` varchar(45) DEFAULT NULL,
  `unidades` double DEFAULT NULL,
  `costounidad` double DEFAULT NULL,
  `subtotal` double DEFAULT NULL,
  `descuentounidad` double DEFAULT NULL,
  `total` double DEFAULT NULL,
  PRIMARY KEY (`iddetalleventa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `pedido` (
  `idpedido` int NOT NULL AUTO_INCREMENT,
  `idproducto` int NOT NULL,
  `stock` int NOT NULL,
  `fechapedido` datetime DEFAULT NULL,
  `idcliente` int DEFAULT NULL,
  `idestado` int DEFAULT NULL,
  `codigopedido` varchar(100) NOT NULL,
  PRIMARY KEY (`idpedido`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

