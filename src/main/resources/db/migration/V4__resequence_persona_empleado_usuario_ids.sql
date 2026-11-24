-- ==============================================================================
-- Migration V4: Resecuenciación consecutiva de IDs desde 1 en persona, empleado, usuario
-- Conservando el 100% de la integridad referencial histórica en ventas, compras, tokens, etc.
-- ==============================================================================

SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------------------------------------------
-- 1. TABLA PERSONA (59 registros -> IDs 1..59)
-- Actualizar idpersona 17740917 a 59 y sus claves foráneas
-- ------------------------------------------------------------------------------
UPDATE cliente SET persona_idpersona = 59 WHERE persona_idpersona = 17740917;
UPDATE proveedor SET persona_idpersona = 59 WHERE persona_idpersona = 17740917;
UPDATE empleado SET persona_idpersona = 59 WHERE persona_idpersona = 17740917;
UPDATE persona SET idpersona = 59 WHERE idpersona = 17740917;

ALTER TABLE persona AUTO_INCREMENT = 60;

-- ------------------------------------------------------------------------------
-- 2. TABLA USUARIO (7 registros -> IDs 1..7)
-- Mapeo: 5->4, 6->5, 21->6, 22->7
-- ------------------------------------------------------------------------------
-- Claves foráneas en empleado
UPDATE empleado SET usuario_idusuario = 4 WHERE usuario_idusuario = 5;
UPDATE empleado SET usuario_idusuario = 5 WHERE usuario_idusuario = 6;
UPDATE empleado SET usuario_idusuario = 6 WHERE usuario_idusuario = 21;
UPDATE empleado SET usuario_idusuario = 7 WHERE usuario_idusuario = 22;

-- Claves foráneas en token
UPDATE token SET usuario_idusuario = 4 WHERE usuario_idusuario = 5;
UPDATE token SET usuario_idusuario = 5 WHERE usuario_idusuario = 6;
UPDATE token SET usuario_idusuario = 6 WHERE usuario_idusuario = 21;
UPDATE token SET usuario_idusuario = 7 WHERE usuario_idusuario = 22;

-- Claves foráneas en usuario_has_permiso
UPDATE usuario_has_permiso SET usuario_idusuario = 4 WHERE usuario_idusuario = 5;
UPDATE usuario_has_permiso SET usuario_idusuario = 5 WHERE usuario_idusuario = 6;
UPDATE usuario_has_permiso SET usuario_idusuario = 6 WHERE usuario_idusuario = 21;
UPDATE usuario_has_permiso SET usuario_idusuario = 7 WHERE usuario_idusuario = 22;

-- Actualización secuencial en usuario (orden exacto para evitar colisiones de PK)
UPDATE usuario SET idusuario = 4 WHERE idusuario = 5;
UPDATE usuario SET idusuario = 5 WHERE idusuario = 6;
UPDATE usuario SET idusuario = 6 WHERE idusuario = 21;
UPDATE usuario SET idusuario = 7 WHERE idusuario = 22;

ALTER TABLE usuario AUTO_INCREMENT = 8;

-- ------------------------------------------------------------------------------
-- 3. TABLA EMPLEADO (5 registros -> IDs 1..5)
-- Mapeo: 5->4, 6->5
-- ------------------------------------------------------------------------------
-- Claves foráneas en venta
UPDATE venta SET empleado_idempleado = 4 WHERE empleado_idempleado = 5;
UPDATE venta SET empleado_idempleado = 5 WHERE empleado_idempleado = 6;

-- Claves foráneas en compra
UPDATE compra SET empleado_idempleado = 4 WHERE empleado_idempleado = 5;
UPDATE compra SET empleado_idempleado = 5 WHERE empleado_idempleado = 6;

-- Actualización secuencial en empleado
UPDATE empleado SET idempleado = 4 WHERE idempleado = 5;
UPDATE empleado SET idempleado = 5 WHERE idempleado = 6;

ALTER TABLE empleado AUTO_INCREMENT = 6;

SET FOREIGN_KEY_CHECKS = 1;
