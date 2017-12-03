--------------------------------------------------------
--  Constraints for Table PREFERENCIA
--------------------------------------------------------

  ALTER TABLE "ISIS2304A061720"."PREFERENCIA" ADD CONSTRAINT "PK_PREFERENCIA" PRIMARY KEY ("IDUSUARIO")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD"  ENABLE;
  ALTER TABLE "ISIS2304A061720"."PREFERENCIA" ADD CONSTRAINT "CK_RELACIONPRECIOS" CHECK ((PRECIOINICIAL IS NULL AND PRECIOFINAL IS NULL)OR(PRECIOINICIAL IS NOT NULL AND PRECIOFINAL IS NOT NULL AND PRECIOINICIAL<=PRECIOFINAL)) ENABLE;
  ALTER TABLE "ISIS2304A061720"."PREFERENCIA" ADD CONSTRAINT "CK_FINALPOSITIVO" CHECK (PRECIOFINAL IS NULL OR PRECIOFINAL>=0) ENABLE;
  ALTER TABLE "ISIS2304A061720"."PREFERENCIA" ADD CONSTRAINT "CK_INICIALPOSITIVO" CHECK (PRECIOINICIAL IS NULL OR PRECIOINICIAL>=0) ENABLE;
