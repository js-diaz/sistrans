

INSERT INTO USUARIO (ID,CORREO,NOMBRE,ROL) VALUES (IDUSUARIO.NEXTVAL,'s.guzmanm@uniandes.edu.co','Sergio Guzmán','CLIENTE');
INSERT INTO USUARIO (ID,CORREO,NOMBRE,ROL) VALUES (IDUSUARIO.NEXTVAL,'js.diaz@uniandes.edu.co','Sebastián Díaz','CLIENTE');
INSERT INTO USUARIO (ID,CORREO,NOMBRE,ROL) VALUES (IDUSUARIO.NEXTVAL,'ja.gomez1@uniandes.edu.co','Jorge Gómez','CLIENTE');
INSERT INTO USUARIO (ID,CORREO,NOMBRE,ROL) VALUES (IDUSUARIO.NEXTVAL,'jc.ruiz@uniandes.edu.co','Camilo Ruiz','CLIENTE');
INSERT INTO USUARIO (ID,CORREO,NOMBRE,ROL) VALUES (IDUSUARIO.NEXTVAL,'jf.ramos@uniandes.edu.co','Felipe Ramos','CLIENTE');
INSERT INTO USUARIO (ID,CORREO,NOMBRE,ROL) VALUES (IDUSUARIO.NEXTVAL,'m.neira10@uniandes.edu.co','Mauricio Neira','CLIENTE');


INSERT INTO PRODUCTO (ID,NOMBRE,TIPO,PERSONALIZABLE, TRADUCCION, DESCRIPCION, TIEMPO) 
    VALUES (IDPRODUCTO.NEXTVAL,'Hamburguesa doble','PLATO FUERTE','0','Traduction','Descripción',10);
INSERT INTO PRODUCTO (ID,NOMBRE,TIPO,PERSONALIZABLE, TRADUCCION, DESCRIPCION, TIEMPO) 
    VALUES (IDPRODUCTO.NEXTVAL,'Pizza de Pepperoni','PLATO FUERTE','0','Traduction','Descripción',10);
INSERT INTO PRODUCTO (ID,NOMBRE,TIPO,PERSONALIZABLE, TRADUCCION, DESCRIPCION, TIEMPO) 
    VALUES (IDPRODUCTO.NEXTVAL,'BBC Porter','BEBIDA','0','Traduction','Descripción',10);
INSERT INTO PRODUCTO (ID,NOMBRE,TIPO,PERSONALIZABLE, TRADUCCION, DESCRIPCION, TIEMPO) 
    VALUES (IDPRODUCTO.NEXTVAL,'Papa salada','ACOMPANAMIENTO','0','Traduction','Descripción',10);
INSERT INTO PRODUCTO (ID,NOMBRE,TIPO,PERSONALIZABLE, TRADUCCION, DESCRIPCION, TIEMPO) 
    VALUES (IDPRODUCTO.NEXTVAL,'Papas fritas','ACOMPANAMIENTO','0','Traduction','Descripción',10);
INSERT INTO PRODUCTO (ID,NOMBRE,TIPO,PERSONALIZABLE, TRADUCCION, DESCRIPCION, TIEMPO) 
    VALUES (IDPRODUCTO.NEXTVAL,'Limonada','BEBIDA','0','Traduction','Descripción',10);
INSERT INTO PRODUCTO (ID,NOMBRE,TIPO,PERSONALIZABLE, TRADUCCION, DESCRIPCION, TIEMPO) 
    VALUES (IDPRODUCTO.NEXTVAL,'Ensalada César','ENTRADA','0','Traduction','Descripción',10);
INSERT INTO PRODUCTO (ID,NOMBRE,TIPO,PERSONALIZABLE, TRADUCCION, DESCRIPCION, TIEMPO) 
    VALUES (IDPRODUCTO.NEXTVAL,'Brownie con helafo doble','POSTRE','0','Traduction','Descripción',10);
INSERT INTO PRODUCTO (ID,NOMBRE,TIPO,PERSONALIZABLE, TRADUCCION, DESCRIPCION, TIEMPO) 
    VALUES (IDPRODUCTO.NEXTVAL,'Pizza de quesos','PLATO FUERTE','0','Traduction','Descripción',10);
INSERT INTO PRODUCTO (ID,NOMBRE,TIPO,PERSONALIZABLE, TRADUCCION, DESCRIPCION, TIEMPO) 
    VALUES (IDPRODUCTO.NEXTVAL,'Postre de maracuyá','POSTRE','0','Traduction','Descripción',10);

INSERT INTO RESTAURANTE(NOMBRE,PAG_WEB,ID_REPRESENTANTE,NOMBRE_ZONA,DISPONIBILIDAD) 
    VALUES ('El Corral','www.pagina.com',1,'Sports','1');
INSERT INTO RESTAURANTE(NOMBRE,PAG_WEB,ID_REPRESENTANTE,NOMBRE_ZONA,DISPONIBILIDAD) 
    VALUES ('Domino´s','www.pagina.com',1,'Sports','1');
INSERT INTO RESTAURANTE(NOMBRE,PAG_WEB,ID_REPRESENTANTE,NOMBRE_ZONA,DISPONIBILIDAD) 
    VALUES ('Gauchos','www.pagina.com',1,'Sports','1');
INSERT INTO RESTAURANTE(NOMBRE,PAG_WEB,ID_REPRESENTANTE,NOMBRE_ZONA,DISPONIBILIDAD) 
    VALUES ('La Diva','www.pagina.com',1,'Sports','1');
INSERT INTO RESTAURANTE(NOMBRE,PAG_WEB,ID_REPRESENTANTE,NOMBRE_ZONA,DISPONIBILIDAD) 
    VALUES ('Home Burger','www.pagina.com',1,'Sports','1');
INSERT INTO RESTAURANTE(NOMBRE,PAG_WEB,ID_REPRESENTANTE,NOMBRE_ZONA,DISPONIBILIDAD) 
    VALUES ('Archie´s','www.pagina.com',1,'Sports','1');
INSERT INTO RESTAURANTE(NOMBRE,PAG_WEB,ID_REPRESENTANTE,NOMBRE_ZONA,DISPONIBILIDAD) 
    VALUES ('Burger King','www.pagina.com',1,'Sports','1');
INSERT INTO RESTAURANTE(NOMBRE,PAG_WEB,ID_REPRESENTANTE,NOMBRE_ZONA,DISPONIBILIDAD) 
    VALUES ('Las lasagnas de Juan','www.pagina.com',1,'Sports','1');
INSERT INTO RESTAURANTE(NOMBRE,PAG_WEB,ID_REPRESENTANTE,NOMBRE_ZONA,DISPONIBILIDAD) 
    VALUES ('Pizza Stop Salitre','www.pagina.com',1,'Sports','1');
INSERT INTO RESTAURANTE(NOMBRE,PAG_WEB,ID_REPRESENTANTE,NOMBRE_ZONA,DISPONIBILIDAD) 
    VALUES ('Parrilla Boyacense','www.pagina.com',1,'Sports','1');
    COMMIT;


--PLATOS FUERTES
