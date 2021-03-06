--------------------------------------------------------
--  Constraints for Table SUSTITUTOS_PRODUCTO
--------------------------------------------------------

  ALTER TABLE "ISIS2304A061720"."SUSTITUTOS_PRODUCTO" ADD CONSTRAINT "PK_SUSTITUTOS_PRODUCTO" PRIMARY KEY ("ID_PRODUCTO", "ID_SUSTITUTO", "NOMBRE_RESTAURANTE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD"  ENABLE;
  ALTER TABLE "ISIS2304A061720"."SUSTITUTOS_PRODUCTO" ADD CONSTRAINT "PROD_SUSTITUIR_DIFERENTE" CHECK (NOT ID_PRODUCTO = ID_SUSTITUTO) ENABLE;
  ALTER TABLE "ISIS2304A061720"."SUSTITUTOS_PRODUCTO" MODIFY ("NOMBRE_RESTAURANTE" CONSTRAINT "SUSTPROD_REST_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."SUSTITUTOS_PRODUCTO" MODIFY ("ID_SUSTITUTO" CONSTRAINT "SUSTPROD_SUSTITUTO_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."SUSTITUTOS_PRODUCTO" MODIFY ("ID_PRODUCTO" CONSTRAINT "SUSTPROD_PRODUCTO_EXISTE" NOT NULL ENABLE);
