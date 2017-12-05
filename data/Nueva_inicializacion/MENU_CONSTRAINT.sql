--------------------------------------------------------
--  Constraints for Table MENU
--------------------------------------------------------

  ALTER TABLE "ISIS2304A061720"."MENU" ADD CONSTRAINT "PK_MENU" PRIMARY KEY ("NOMBRE", "NOMBRE_RESTAURANTE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD"  ENABLE;
  ALTER TABLE "ISIS2304A061720"."MENU" MODIFY ("PRECIO" CONSTRAINT "PRECIO_MENU_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."MENU" MODIFY ("NOMBRE_RESTAURANTE" CONSTRAINT "RESTAURANTE_DE_MENU_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."MENU" MODIFY ("NOMBRE" CONSTRAINT "NOMBRE_MENU_EXISTE" NOT NULL ENABLE);