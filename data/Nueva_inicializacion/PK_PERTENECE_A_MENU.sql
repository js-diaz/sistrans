--------------------------------------------------------
--  DDL for Index PK_PERTENECE_A_MENU
--------------------------------------------------------

  CREATE UNIQUE INDEX "ISIS2304A061720"."PK_PERTENECE_A_MENU" ON "ISIS2304A061720"."PERTENECE_A_MENU" ("ID_PLATO", "NOMBRE_MENU", "NOMBRE_RESTAURANTE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;
