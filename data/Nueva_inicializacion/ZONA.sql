--------------------------------------------------------
--  DDL for Table ZONA
--------------------------------------------------------

  CREATE TABLE "ISIS2304A061720"."ZONA" 
   (	"NOMBRE" VARCHAR2(20 BYTE), 
	"CAPACIDAD" NUMBER(*,0), 
	"INGRESOESPECIAL" CHAR(1 BYTE), 
	"ABIERTAACTUALMENTE" CHAR(1 BYTE), 
	"CAPACIDADOCUPADA" NUMBER(*,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;
