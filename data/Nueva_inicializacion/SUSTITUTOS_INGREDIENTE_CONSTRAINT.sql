--------------------------------------------------------
--  Constraints for Table SUSTITUTOS_INGREDIENTE
--------------------------------------------------------

  ALTER TABLE "ISIS2304A061720"."SUSTITUTOS_INGREDIENTE" ADD CONSTRAINT "PK_SUSTITUTOS_INGREDIENTE" PRIMARY KEY ("ID_INGREDIENTE", "ID_SUSTITUTO", "NOMBRE_RESTAURANTE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD"  ENABLE;
  ALTER TABLE "ISIS2304A061720"."SUSTITUTOS_INGREDIENTE" ADD CONSTRAINT "ING_SUSTITUIR_DIFERENTE" CHECK (NOT ID_INGREDIENTE = ID_SUSTITUTO) ENABLE;
  ALTER TABLE "ISIS2304A061720"."SUSTITUTOS_INGREDIENTE" MODIFY ("NOMBRE_RESTAURANTE" CONSTRAINT "SUSTING_RESTAURANTE_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."SUSTITUTOS_INGREDIENTE" MODIFY ("ID_SUSTITUTO" CONSTRAINT "SUSTING_SUSTITUTO_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."SUSTITUTOS_INGREDIENTE" MODIFY ("ID_INGREDIENTE" CONSTRAINT "SUSTING_INGREDIENTE_EXISTE" NOT NULL ENABLE);
