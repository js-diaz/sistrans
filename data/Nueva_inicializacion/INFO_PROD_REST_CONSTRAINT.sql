--------------------------------------------------------
--  Constraints for Table INFO_PROD_REST
--------------------------------------------------------

  ALTER TABLE "ISIS2304A061720"."INFO_PROD_REST" ADD CONSTRAINT "FECHA_INICIO_ANTES_DE_FIN" CHECK (FECHA_INICIO < FECHA_FIN) ENABLE;
  ALTER TABLE "ISIS2304A061720"."INFO_PROD_REST" ADD CONSTRAINT "CKCANTPRODREST" CHECK (DISPONIBILIDAD>=0) ENABLE;
  ALTER TABLE "ISIS2304A061720"."INFO_PROD_REST" ADD CONSTRAINT "PK_INFO_PROD_REST" PRIMARY KEY ("ID_PRODUCTO", "NOMBRE_RESTAURANTE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD"  ENABLE;
  ALTER TABLE "ISIS2304A061720"."INFO_PROD_REST" ADD CONSTRAINT "CKMAXDISP" CHECK (CANTIDAD_MAXIMA>=DISPONIBILIDAD) ENABLE;
  ALTER TABLE "ISIS2304A061720"."INFO_PROD_REST" ADD CONSTRAINT "CKCANTMAXPRODREST" CHECK (CANTIDAD_MAXIMA>0) ENABLE;
  ALTER TABLE "ISIS2304A061720"."INFO_PROD_REST" MODIFY ("PRECIO" CONSTRAINT "PRECIO_DE_PRODUCTO_INOF_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."INFO_PROD_REST" MODIFY ("NOMBRE_RESTAURANTE" CONSTRAINT "RESTAURANTE_PROD_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."INFO_PROD_REST" MODIFY ("ID_PRODUCTO" CONSTRAINT "PRODUCTO_REST_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."INFO_PROD_REST" ADD CONSTRAINT "COSTO_DE_PRODUCTO_INOF_EXISTE" CHECK (COSTO is NOT NULL) ENABLE;
