--------------------------------------------------------
--  DDL for Index PK_TIPOSP
--------------------------------------------------------

  CREATE UNIQUE INDEX "ISIS2304A061720"."PK_TIPOSP" ON "ISIS2304A061720"."TIPOSDEPLATO" ("NOMBRE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOLOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;
