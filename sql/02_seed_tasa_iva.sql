-- Necesario para Producto: ControladorProducto busca el id_tasa_iva
-- por porcentaje (15 para "CON IVA", 0 para "SIN IVA" en la vista de Productos).
-- Ejecutar despues de 01_schema.sql y 02_seed.sql.
INSERT INTO tasa_iva (porcentaje, descripcion) VALUES
(15.00, 'IVA 15%'),
(0.00, 'Sin IVA / Tarifa 0%');
