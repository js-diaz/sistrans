--------------------------------------------------------
--  Constraints for Table PRODUCTO
--------------------------------------------------------

  ALTER TABLE "ISIS2304A061720"."PRODUCTO" ADD CONSTRAINT "PK_PRODUCTO" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD"  ENABLE;
  ALTER TABLE "ISIS2304A061720"."PRODUCTO" ADD CONSTRAINT "CK_DESCRIPCION3" CHECK (((DESCRIPCION LIKE '' AND TRADUCCION LIKE '') OR (DESCRIPCION  NOT LIKE '' AND TRADUCCION  NOT LIKE ''))) ENABLE;
  ALTER TABLE "ISIS2304A061720"."PRODUCTO" ADD CONSTRAINT "CK_DESCRIPCION2" CHECK ((DESCRIPCION LIKE 'null' AND TRADUCCION LIKE 'null') OR (DESCRIPCION  NOT LIKE 'null' AND TRADUCCION  NOT LIKE 'null')) ENABLE;
  ALTER TABLE "ISIS2304A061720"."PRODUCTO" ADD CONSTRAINT "CK_PERSONALIZABLE" CHECK (PERSONALIZABLE IN ('0','1')) ENABLE;
  ALTER TABLE "ISIS2304A061720"."PRODUCTO" ADD CONSTRAINT "CK_DESCRIPCION" CHECK ((DESCRIPCION IS NULL AND TRADUCCION IS NULL) OR (DESCRIPCION IS NOT NULL AND TRADUCCION IS NOT NULL)) ENABLE;
  ALTER TABLE "ISIS2304A061720"."PRODUCTO" MODIFY ("PERSONALIZABLE" CONSTRAINT "ES_PERSONALIZABLE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."PRODUCTO" MODIFY ("TIPO" CONSTRAINT "TIPO_PRODUCTO_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."PRODUCTO" MODIFY ("NOMBRE" CONSTRAINT "NOMBRE_PRODUCTO_EXISTE" NOT NULL ENABLE);
