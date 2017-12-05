--------------------------------------------------------
--  DDL for Table RESERVA
--------------------------------------------------------

  CREATE TABLE "ISIS2304A061720"."RESERVA" 
   (	"FECHA" DATE, 
	"ID_RESERVADOR" NUMBER(*,0), 
	"NUM_PERSONAS" NUMBER(*,0), 
	"NOMBRE_ZONA" VARCHAR2(20 BYTE), 
	"NOMBRE_MENU" VARCHAR2(20 BYTE), 
	"NOMBRE_RESTAURANTE" VARCHAR2(20 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;