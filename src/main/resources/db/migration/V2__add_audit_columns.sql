-- ==============================================================================
-- V2: Agregar columnas de auditoría a las tablas principales
-- ==============================================================================

ALTER TABLE `producto`
    ADD COLUMN `created_at` DATETIME NULL,
    ADD COLUMN `updated_at` DATETIME NULL,
    ADD COLUMN `created_by` VARCHAR(50) NULL,
    ADD COLUMN `last_modified_by` VARCHAR(50) NULL;

ALTER TABLE `venta`
    ADD COLUMN `created_at` DATETIME NULL,
    ADD COLUMN `updated_at` DATETIME NULL,
    ADD COLUMN `created_by` VARCHAR(50) NULL,
    ADD COLUMN `last_modified_by` VARCHAR(50) NULL;

ALTER TABLE `compra`
    ADD COLUMN `created_at` DATETIME NULL,
    ADD COLUMN `updated_at` DATETIME NULL,
    ADD COLUMN `created_by` VARCHAR(50) NULL,
    ADD COLUMN `last_modified_by` VARCHAR(50) NULL;

ALTER TABLE `pedido`
    ADD COLUMN `created_at` DATETIME NULL,
    ADD COLUMN `updated_at` DATETIME NULL,
    ADD COLUMN `created_by` VARCHAR(50) NULL,
    ADD COLUMN `last_modified_by` VARCHAR(50) NULL;

