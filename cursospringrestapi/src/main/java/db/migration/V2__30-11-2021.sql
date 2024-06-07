
ALTER DATABASE restspring OWNER TO postgres;

-- Function: public.notify_client_update()

-- DROP FUNCTION public.notify_client_update();

CREATE OR REPLACE FUNCTION public.notify_client_update()
  RETURNS trigger AS
$BODY$
BEGIN
  IF NEW.statusservico <> OLD.statusservico THEN
    PERFORM pg_notify('client_update', json_build_object(
      'descricao_servico', NEW.descricao_servico,
      'garantia', NEW.garantia,
      'nome', NEW.nome,
      'telefone', NEW.telefone,
      'datadacompra', NEW.datadacompra,
      'numerodaos', NEW.numerodaos,
      'numeronfcompra', NEW.numeronfcompra
    )::text);
  END IF;
  RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.notify_client_update()
  OWNER TO postgres;
