--------------------------------------------------------
--  Constraints for Table MESA
--------------------------------------------------------

  ALTER TABLE "ISIS2304A061720"."MESA" ADD CONSTRAINT "PK_MESA" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD"  ENABLE;
  ALTER TABLE "ISIS2304A061720"."MESA" ADD CONSTRAINT "CK_CAPACIDADESMESA" CHECK (CAPACIDADOCUPADA<=CAPACIDAD) ENABLE;
  ALTER TABLE "ISIS2304A061720"."MESA" ADD CONSTRAINT "CK_OCUPADAMESA" CHECK (CAPACIDADOCUPADA>=0) ENABLE;
  ALTER TABLE "ISIS2304A061720"."MESA" ADD CONSTRAINT "CK_CAPACIDADMESA" CHECK (CAPACIDAD>=0) ENABLE;
  ALTER TABLE "ISIS2304A061720"."MESA" MODIFY ("ZONA" CONSTRAINT "ZONA_DE_MESA_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."MESA" MODIFY ("CAPACIDADOCUPADA" CONSTRAINT "CAPACIDADOCUPADA_MESA_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."MESA" MODIFY ("CAPACIDAD" CONSTRAINT "CAPACIDAD_MESA_EXISTE" NOT NULL ENABLE);
