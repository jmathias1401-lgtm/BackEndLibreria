-- ==============================================================================
-- V3: Creación de la tabla kardex para movimientos y trazabilidad de inventario
-- ==============================================================================

CREATE TABLE IF NOT EXISTS `kardex` (
  `idkardex` BIGINT NOT NULL AUTO_INCREMENT,
  `producto_idproducto` BIGINT NOT NULL,
  `fecha` DATETIME(6) NOT NULL,
  `tipo_operacion` VARCHAR(50) NOT NULL,
  `motivo` VARCHAR(255) NOT NULL,
  `cantidad` INT NOT NULL,
  `stock_anterior` INT NOT NULL,
  `stock_nuevo` INT NOT NULL,
  `usuario` VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`idkardex`),
  KEY `fk_kardex_producto_idx` (`producto_idproducto`),
  CONSTRAINT `fk_kardex_producto` FOREIGN KEY (`producto_idproducto`) REFERENCES `producto` (`idproducto`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

