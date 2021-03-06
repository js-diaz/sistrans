--------------------------------------------------------
--  DDL for Table CUENTA
--------------------------------------------------------

  CREATE TABLE "ISIS2304A061720"."CUENTA" 
   (	"VALOR" NUMBER, 
	"NUMEROCUENTA" VARCHAR2(20 BYTE), 
	"FECHA" DATE, 
	"IDUSUARIO" NUMBER(*,0), 
	"MESA" NUMBER(*,0), 
	"PAGADA" CHAR(1 BYTE), 
	"DIRECCION" VARCHAR2(100 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;
