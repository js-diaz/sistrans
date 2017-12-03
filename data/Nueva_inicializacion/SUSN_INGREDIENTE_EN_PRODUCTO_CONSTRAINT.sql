--------------------------------------------------------
--  Constraints for Table SUSN_INGREDIENTE_EN_PRODUCTO
--------------------------------------------------------

  ALTER TABLE "ISIS2304A061720"."SUSN_INGREDIENTE_EN_PRODUCTO" ADD CONSTRAINT "PK_SUSN_ING_EN_PRODUCTO" PRIMARY KEY ("ID_ORIGINAL", "ID_SUSTITUTO", "ID_PRODUCTO", "NUMERO_CUENTA", "NOMBRE_MENU", "NOMBRE_RESTAURANTE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 NOLOGGING 
  TABLESPACE "TBSPROD"  ENABLE;
  ALTER TABLE "ISIS2304A061720"."SUSN_INGREDIENTE_EN_PRODUCTO" MODIFY ("NOMBRE_RESTAURANTE" CONSTRAINT "SUSNINGPROD_REST_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."SUSN_INGREDIENTE_EN_PRODUCTO" MODIFY ("NOMBRE_MENU" CONSTRAINT "SUSNINGPROD_MENU_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."SUSN_INGREDIENTE_EN_PRODUCTO" MODIFY ("NUMERO_CUENTA" CONSTRAINT "SUSNINGPROD_CUENTA_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."SUSN_INGREDIENTE_EN_PRODUCTO" MODIFY ("ID_PRODUCTO" CONSTRAINT "SUSNINGPROD_PRODUCTO_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."SUSN_INGREDIENTE_EN_PRODUCTO" MODIFY ("ID_SUSTITUTO" CONSTRAINT "SUSNINGPROD_SUSTITUTO_EXISTE" NOT NULL ENABLE);
  ALTER TABLE "ISIS2304A061720"."SUSN_INGREDIENTE_EN_PRODUCTO" MODIFY ("ID_ORIGINAL" CONSTRAINT "SUSNINGPROD_ORIGINAL_EXISTE" NOT NULL ENABLE);
