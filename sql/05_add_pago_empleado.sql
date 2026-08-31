-- Tabla nueva para el modulo de RH (nomina de empleados)
-- 100% aditivo: no modifica ni borra nada existente.

CREATE SEQUENCE public.seq_pago_empleado
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.seq_pago_empleado OWNER TO postgres;

CREATE TABLE public.pago_empleado (
    id_pago character varying(10) NOT NULL PRIMARY KEY,
    id_empleado character varying(10) NOT NULL REFERENCES public.empleado(id_empleado),
    fecha_pago date DEFAULT CURRENT_DATE,
    periodo character varying(50) NOT NULL,
    monto numeric(10,2) NOT NULL,
    observaciones character varying(255)
);

ALTER TABLE public.pago_empleado OWNER TO postgres;

CREATE FUNCTION public.generar_id_pago_empleado() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.id_pago IS NULL THEN
        NEW.id_pago := 'PE' || LPAD(nextval('seq_pago_empleado')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$;

ALTER FUNCTION public.generar_id_pago_empleado() OWNER TO postgres;

CREATE TRIGGER trg_pago_empleado BEFORE INSERT ON public.pago_empleado
    FOR EACH ROW EXECUTE FUNCTION public.generar_id_pago_empleado();
