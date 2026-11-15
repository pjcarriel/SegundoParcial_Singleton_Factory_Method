-- Ejecutar con MySQL Workbench o en la consola de mysql.
-- Solo datos ficticios: aplicación educativa.
CREATE DATABASE IF NOT EXISTS soc_alertas
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE soc_alertas;

CREATE TABLE IF NOT EXISTS alertas (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  tipo VARCHAR(30) NOT NULL,
  severidad VARCHAR(20) NOT NULL,
  descripcion VARCHAR(255) NOT NULL,
  creada_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_alertas_tipo (tipo),
  INDEX idx_alertas_fecha (creada_en)
);
