﻿--BORRA TODO EN LA BD
    --BORRA TABLAS MUCHOS A MUCHOS
DROP TABLE PREFERENCIACATEGORIA;
DROP TABLE PREFERENCIAZONA;
DROP TABLE CONDICIONZONA;
DROP TABLE CATEGORIA_RESTAURANTE;
DROP TABLE INFO_ING_REST;
DROP TABLE CATEGORIA_PRODUCTO;
DROP TABLE CATEGORIA_MENU;
DROP TABLE PERTENECE_A_MENU;
DROP TABLE PERTENECE_A_PLATO;
DROP TABLE PEDIDO_MENU;
DROP TABLE PEDIDO_PROD;
DROP TABLE INFO_PROD_REST;

    --BORRA ENTIDADES INDEPENDIENTES
DROP TABLE CONDICIONTECNICA;
DROP TABLE CATEGORIA;
DROP TABLE CUENTA;
DROP TABLE PREFERENCIA;
DROP TABLE RESERVA;
DROP TABLE MENU;
DROP TABLE RESTAURANTE;
DROP TABLE PRODUCTO;
DROP TABLE INGREDIENTE;
DROP TABLE USUARIO;
DROP TABLE TIPOSDEPLATO;
DROP TABLE ROL;
DROP TABLE MESA;
DROP TABLE ZONA;



CREATE TABLE ROL (
 NOMBRE VARCHAR(20),
 CONSTRAINT PK_ROL PRIMARY KEY (NOMBRE),
 CONSTRAINT CK_ROL CHECK (NOMBRE IN ('CLIENTE','OPERADOR','ORGANIZADORES','LOCAL','PROVEEDOR'))
 );
 
 CREATE TABLE TIPOSDEPLATO(
NOMBRE VARCHAR(20),
CONSTRAINT PK_TIPOSP PRIMARY KEY (NOMBRE),
CONSTRAINT CK_TIPOSP CHECK (NOMBRE IN ('ENTRADA','PLATO FUERTE','POSTRE','BEBIDA','ACOMPANAMIENTO'))
);
 
COMMIT;
 
CREATE TABLE INGREDIENTE(
ID INTEGER,
NOMBRE VARCHAR(50) CONSTRAINT NOMBRE_ING_EXISTE NOT NULL,
DESCRIPCION VARCHAR (500),
TRADUCCION VARCHAR (500),
CONSTRAINT PK_ING PRIMARY KEY (ID),
CONSTRAINT CK_DESCING CHECK ((DESCRIPCION IS NULL AND TRADUCCION IS NULL) OR (DESCRIPCION IS NOT NULL AND TRADUCCION IS NOT NULL)),
CONSTRAINT CK_DESCING2 CHECK ((DESCRIPCION LIKE 'null' AND TRADUCCION LIKE 'null') OR (DESCRIPCION  NOT LIKE 'null' AND TRADUCCION  NOT LIKE 'null')),
CONSTRAINT CK_DESCING3 CHECK (((DESCRIPCION LIKE '' AND TRADUCCION LIKE '') OR (DESCRIPCION  NOT LIKE '' AND TRADUCCION  NOT LIKE '')))
);

 CREATE TABLE CATEGORIA(
 NOMBRE VARCHAR(20),
 CONSTRAINT PK_CATEGORIA PRIMARY KEY (NOMBRE)
 );
 
  
 CREATE TABLE CONDICIONTECNICA (
 NOMBRE VARCHAR(20),
 CONSTRAINT PK_CONDICIONTECNICA PRIMARY KEY (NOMBRE));
 
 CREATE TABLE PRODUCTO(
ID INTEGER,
NOMBRE VARCHAR(50) CONSTRAINT NOMBRE_PRODUCTO_EXISTE NOT NULL,
TIPO VARCHAR(20) CONSTRAINT TIPO_PRODUCTO_EXISTE NOT NULL,
PERSONALIZABLE CHAR(1) CONSTRAINT ES_PERSONALIZABLE NOT NULL,
PRECIO NUMBER CONSTRAINT PRECIO_PRODUCTO_EXISTE NOT NULL,
TRADUCCION VARCHAR(500),
DESCRIPCION VARCHAR(500),
COSTOPRODUCCION NUMBER CONSTRAINT COSTO_PRODUCTO_EXISTE NOT NULL,
TIEMPO NUMBER,
CONSTRAINT PK_PRODUCTO PRIMARY KEY (ID),
CONSTRAINT FK_TIPO FOREIGN KEY (TIPO) REFERENCES TIPOSDEPLATO (NOMBRE),
CONSTRAINT CK_NUMEROS CHECK (PRECIO >=0 AND COSTOPRODUCCION >=0 AND TIEMPO >=0),
CONSTRAINT CK_DESCRIPCION CHECK ((DESCRIPCION IS NULL AND TRADUCCION IS NULL) OR (DESCRIPCION IS NOT NULL AND TRADUCCION IS NOT NULL)),
CONSTRAINT CK_PERSONALIZABLE CHECK (PERSONALIZABLE IN ('0','1')),
CONSTRAINT CK_DESCRIPCION2 CHECK ((DESCRIPCION LIKE 'null' AND TRADUCCION LIKE 'null') OR (DESCRIPCION  NOT LIKE 'null' AND TRADUCCION  NOT LIKE 'null')),
CONSTRAINT CK_DESCRIPCION3 CHECK (((DESCRIPCION LIKE '' AND TRADUCCION LIKE '') OR (DESCRIPCION  NOT LIKE '' AND TRADUCCION  NOT LIKE '')))

);
 
 CREATE TABLE PERTENECE_A_PLATO
(
    ID_PLATO INTEGER CONSTRAINT PLATO_DE_PERTENECE_EXISTE NOT NULL,
    ID_INGREDIENTE INTEGER CONSTRAINT PLATO_DE_INGREDIENTE_EXISTE NOT NULL,
    CONSTRAINT PK_PERTENECE_A_PLATO PRIMARY KEY (ID_PLATO, ID_INGREDIENTE),
    CONSTRAINT FK_PLATO_DE_INGREDIENTE FOREIGN KEY (ID_PLATO) REFERENCES PRODUCTO(ID),
    CONSTRAINT FK_INGREDIENTE_DEL_PLATO FOREIGN KEY (ID_INGREDIENTE) REFERENCES INGREDIENTE(ID)
);


 CREATE TABLE USUARIO (
 ID INTEGER,
 CORREO VARCHAR(100),
 NOMBRE VARCHAR (50) CONSTRAINT NOMBRE_USUARIO_EXISTE NOT NULL,
 ROL VARCHAR (20) CONSTRAINT ROL_USUARIO_EXISTE NOT NULL,
 CONSTRAINT FK_ROL FOREIGN KEY (ROL) REFERENCES ROL(NOMBRE),
 CONSTRAINT PK_USUARIO PRIMARY KEY(ID),
  CONSTRAINT CK_ID CHECK (ID>=0));
 
 
 
 CREATE TABLE ZONA (
  NOMBRE VARCHAR(20),
 CAPACIDAD INTEGER CONSTRAINT CAPACIDAD_ZONA_EXISTE NOT NULL,
 INGRESOESPECIAL CHAR(1) CONSTRAINT INGRESO_ESPECIAL_EXISTE NOT NULL,
 ABIERTAACTUALMENTE CHAR(1) CONSTRAINT ABIERTA_ACTUALMENTE_EXISTE NOT NULL,
 CAPACIDADOCUPADA INTEGER CONSTRAINT CAPACIDADOCUPADA_ZONA_EXISTE  NOT NULL,
 CONSTRAINT PK_ZONA PRIMARY KEY (NOMBRE),
 CONSTRAINT CK_CAPACIDAD CHECK (CAPACIDAD>=0),
 CONSTRAINT CK_OCUPADA CHECK (CAPACIDADOCUPADA>=0),
 CONSTRAINT CK_CAPACIDADES CHECK (CAPACIDADOCUPADA<=CAPACIDAD),
 CONSTRAINT CK_ABIERTA CHECK (ABIERTAACTUALMENTE IN ('0','1')),
 CONSTRAINT CK_INGRESOESPECIAL CHECK (INGRESOESPECIAL IN ('0','1'))
);


 CREATE TABLE MESA(
 ID INTEGER,
 CAPACIDAD INTEGER CONSTRAINT CAPACIDAD_MESA_EXISTE NOT NULL,
 CAPACIDADOCUPADA INTEGER CONSTRAINT CAPACIDADOCUPADA_MESA_EXISTE NOT NULL,
 ZONA VARCHAR(20) CONSTRAINT ZONA_DE_MESA_EXISTE NOT NULL,
 CONSTRAINT PK_MESA PRIMARY KEY(ID),
 CONSTRAINT FK_MESAZONA FOREIGN KEY(ZONA) REFERENCES ZONA(NOMBRE),
 CONSTRAINT CK_CAPACIDADMESA CHECK (CAPACIDAD>=0),
 CONSTRAINT CK_OCUPADAMESA CHECK (CAPACIDADOCUPADA>=0),
 CONSTRAINT CK_CAPACIDADESMESA CHECK (CAPACIDADOCUPADA<=CAPACIDAD));
 
CREATE TABLE RESTAURANTE
(
    NOMBRE VARCHAR(20) CONSTRAINT NOMBRE_RESTAURANTE_EXISTE NOT NULL,
    PAG_WEB VARCHAR(40),
    ID_REPRESENTANTE INTEGER CONSTRAINT ID_REPRESENTANTE_EXISTE NOT NULL,
    NOMBRE_ZONA VARCHAR(20) CONSTRAINT ZONA_DE_RESTAURANTE_EXISTE NOT NULL,
    CONSTRAINT PK_RESTAURANTE PRIMARY KEY (NOMBRE),
    CONSTRAINT FK_REPRESENTANTE FOREIGN KEY (ID_REPRESENTANTE) REFERENCES USUARIO(ID),
    CONSTRAINT FK_ZONA_DEL_RESTAURANTE FOREIGN KEY (NOMBRE_ZONA) REFERENCES ZONA(NOMBRE)
);
 
 CREATE TABLE CONDICIONZONA(
 CONDICIONTECNICANOMBRE VARCHAR(20),
 ZONANOMBRE VARCHAR (20),
 CONSTRAINT FK_ZONA FOREIGN KEY (ZONANOMBRE) REFERENCES ZONA (NOMBRE),
 CONSTRAINT PK_CONDICIONZONA PRIMARY KEY (CONDICIONTECNICANOMBRE,ZONANOMBRE),
 CONSTRAINT FK_CONDICION FOREIGN KEY (CONDICIONTECNICANOMBRE) REFERENCES CONDICIONTECNICA(NOMBRE)
 );

 
 CREATE TABLE PREFERENCIA(
 IDUSUARIO INTEGER,
 PRECIOINICIAL NUMBER,
 PRECIOFINAL NUMBER,
 CONSTRAINT FK_USUARIO FOREIGN KEY (IDUSUARIO) REFERENCES USUARIO(ID),
 CONSTRAINT PK_PREFERENCIA PRIMARY KEY (IDUSUARIO),
 CONSTRAINT CK_INICIALPOSITIVO CHECK (PRECIOINICIAL IS NULL OR PRECIOINICIAL>=0),
 CONSTRAINT CK_FINALPOSITIVO CHECK (PRECIOFINAL IS NULL OR PRECIOFINAL>=0),
 CONSTRAINT CK_RELACIONPRECIOS CHECK ((PRECIOINICIAL IS NULL AND PRECIOFINAL IS NULL)OR(PRECIOINICIAL IS NOT NULL AND PRECIOFINAL IS NOT NULL AND PRECIOINICIAL<=PRECIOFINAL))
 );
 
CREATE TABLE INFO_PROD_REST
(
    ID_PRODUCTO INTEGER CONSTRAINT PRODUCTO_REST_EXISTE NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) CONSTRAINT RESTAURANTE_PROD_EXISTE NOT NULL,
    PRECIO NUMBER CONSTRAINT PRECIO_DE_PRODUCTO_INOF_EXISTE  NOT NULL,
    COSTO NUMBER,
    DISPONIBILIDAD INTEGER,
    FECHA_INICIO DATE,
    FECHA_FIN DATE,
    CANTIDAD_MAXIMA INTEGER ,
    CONSTRAINT PK_INFO_PROD_REST PRIMARY KEY(ID_PRODUCTO, NOMBRE_RESTAURANTE),
    CONSTRAINT FK_PRODUCTO_DEL_RESTAURANTE FOREIGN KEY (ID_PRODUCTO) REFERENCES PRODUCTO(ID),
    CONSTRAINT FK_RESTAURANTE_DEL_PRODUCTO FOREIGN KEY (NOMBRE_RESTAURANTE) REFERENCES RESTAURANTE(NOMBRE),
    CONSTRAINT CKCANTPRODREST CHECK (DISPONIBILIDAD>0),
    CONSTRAINT CKCANTMAXPRODREST CHECK (CANTIDAD_MAXIMA>0),
    CONSTRAINT CKMAXDISP CHECK (CANTIDAD_MAXIMA>=DISPONIBILIDAD)
);
CREATE TABLE INFO_ING_REST
(
    ID_INGREDIENTE INTEGER CONSTRAINT INGREDIENTE_REST_EXISTE NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) CONSTRAINT RESTAURANTE_ING_EXISTE NOT NULL,
    PRECIO_ADICION NUMBER,
    PRECIO_SUSTITUTO NUMBER,
    CONSTRAINT PK_INFOINGREST PRIMARY KEY (ID_INGREDIENTE, NOMBRE_RESTAURANTE),
    CONSTRAINT FK_INGREDIENTE_DE_RESTAURANTE FOREIGN KEY (ID_INGREDIENTE) REFERENCES INGREDIENTE(ID),
    CONSTRAINT FK_RESTAURANTE_DE_INGREDIENTE FOREIGN KEY( NOMBRE_RESTAURANTE) REFERENCES RESTAURANTE(NOMBRE)
);

CREATE TABLE MENU
(
    NOMBRE VARCHAR(20) CONSTRAINT NOMBRE_MENU_EXISTE NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) CONSTRAINT RESTAURANTE_DE_MENU_EXISTE NOT NULL,
    PRECIO NUMBER CONSTRAINT PRECIO_MENU_EXISTE NOT NULL,
    COSTO NUMBER,
    CONSTRAINT PK_MENU PRIMARY KEY(NOMBRE, NOMBRE_RESTAURANTE),
    CONSTRAINT FK_RESTAURANTE_DE_MENU FOREIGN KEY (NOMBRE_RESTAURANTE) REFERENCES RESTAURANTE(NOMBRE)
);

CREATE TABLE PERTENECE_A_MENU
(
    NOMBRE_MENU VARCHAR(20) CONSTRAINT NOMBRE_MENU_PERT_EXISTE NOT NULL,
    ID_PLATO INTEGER CONSTRAINT PLATO_PERTE_A_MENU_EXISTE NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) CONSTRAINT RESTAURANTE_PERT_A_MENU_EXISTE NOT NULL,
    CONSTRAINT PK_PERTENECE_A_MENU PRIMARY KEY (NOMBRE_MENU, ID_PLATO, NOMBRE_RESTAURANTE),
    CONSTRAINT FK_MENU_DE_PLATO FOREIGN KEY (NOMBRE_MENU, NOMBRE_RESTAURANTE) REFERENCES MENU(NOMBRE, NOMBRE_RESTAURANTE),
    CONSTRAINT FK_PLATO_DE_MENU FOREIGN KEY (ID_PLATO, NOMBRE_RESTAURANTE) REFERENCES INFO_PROD_REST(ID_PRODUCTO, NOMBRE_RESTAURANTE)
);

CREATE TABLE CATEGORIA_MENU
(
    NOMBRE_CATEGORIA VARCHAR(50) CONSTRAINT CATEGORIA_DE_MENU_EXISTE NOT NULL,
    NOMBRE_MENU VARCHAR(20) CONSTRAINT NOMBRE_DE_MENU_EXISTE NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) CONSTRAINT NOMBRE_DE_RESTAURANTE_EXISTE NOT NULL,
    CONSTRAINT PK_CATEGORIAMENU PRIMARY KEY (NOMBRE_CATEGORIA, NOMBRE_MENU, NOMBRE_RESTAURANTE),
    CONSTRAINT FK_CATEGORIA_DEL_MENU FOREIGN KEY (NOMBRE_CATEGORIA) REFERENCES CATEGORIA(NOMBRE),
    CONSTRAINT FK_MENU_DE_LA_CATEGORIA FOREIGN KEY (NOMBRE_MENU, NOMBRE_RESTAURANTE) REFERENCES MENU(NOMBRE, NOMBRE_RESTAURANTE)
);

CREATE TABLE CATEGORIA_RESTAURANTE
(
    NOMBRE_CATEGORIA VARCHAR(50) CONSTRAINT CATEGORIA_DE_REST_EXISTE NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) CONSTRAINT REST_DE_CATEGORIA_EXISTE NOT NULL,
    CONSTRAINT PK_CATEGORIA_RESTAURANTE PRIMARY KEY (NOMBRE_CATEGORIA, NOMBRE_RESTAURANTE),
    CONSTRAINT FK_CATEGORIA_DE_RESTAURANTE FOREIGN KEY (NOMBRE_CATEGORIA) REFERENCES CATEGORIA(NOMBRE),
    CONSTRAINT FK_RESTAURANTE_DE_CATEGORIA FOREIGN KEY (NOMBRE_RESTAURANTE) REFERENCES RESTAURANTE(NOMBRE)
);

CREATE TABLE CATEGORIA_PRODUCTO
(
    NOMBRE_CATEGORIA VARCHAR(50) CONSTRAINT CATEGORIA_DE_PRODUCTO_EXISTE NOT NULL,
    ID_PRODUCTO INTEGER CONSTRAINT PRODUCTO_DE_CATEGORIA_EXISTE NOT NULL,
    CONSTRAINT PK_CATEGORIA_PRODUCTO PRIMARY KEY (NOMBRE_CATEGORIA, ID_PRODUCTO),
    CONSTRAINT FK_CATEGORIA_DE_PRODUCTO FOREIGN KEY (NOMBRE_CATEGORIA) REFERENCES CATEGORIA(NOMBRE),
    CONSTRAINT FK_PRODUCTO_DE_CATEGORIA FOREIGN KEY (ID_PRODUCTO) REFERENCES PRODUCTO(ID)
);

CREATE TABLE CUENTA(
VALOR NUMBER CONSTRAINT VALOR_CUENTA_EXISTE NOT NULL,
NUMEROCUENTA VARCHAR (20) CONSTRAINT NUMEROCUENTA_EXISTE NOT NULL,
FECHA DATE CONSTRAINT FECHA_CUENTA_EXISTE NOT NULL,
IDUSUARIO INTEGER ,
MESA INTEGER,
CONSTRAINT FK_IDUSUARIOCUENTA FOREIGN KEY (IDUSUARIO) REFERENCES USUARIO(ID),
CONSTRAINT PK_CUENTA PRIMARY KEY (NUMEROCUENTA),
CONSTRAINT FK_MESACUENTA FOREIGN KEY (MESA) REFERENCES MESA(ID),
CONSTRAINT CK_NUMCUENTA CHECK (REGEXP_LIKE(NUMEROCUENTA, '^[[:digit:]]+$'))
,CONSTRAINT CK_VALOR_CUENTA CHECK (VALOR>=0));

 CREATE TABLE PEDIDO_MENU
(
    NUMERO_CUENTA VARCHAR(20) CONSTRAINT NUMCUENTA_PEDIDO_MENU_EXISTE NOT NULL,
    NOMBRE_MENU VARCHAR(20) CONSTRAINT MENU_PEDIDO_MENU_EXISTE NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) CONSTRAINT RESTAURANTE_PEDIDO_MENU_EXISTE  NOT NULL,
    CANTIDAD INTEGER CONSTRAINT CANTIDAD_PEDIDO_MENU_EXISTE NOT NULL,
    ENTREGADO CHAR(1) ,
    CONSTRAINT PK_PEDIDO_MENU PRIMARY KEY (NUMERO_CUENTA, NOMBRE_MENU, NOMBRE_RESTAURANTE),
    CONSTRAINT FK_NUMEROCUENTA_PEDIDO_MENU FOREIGN KEY (NUMERO_CUENTA) REFERENCES CUENTA(NUMEROCUENTA),
    CONSTRAINT FK_MENU_DEL_PEDIDO FOREIGN KEY (NOMBRE_MENU, NOMBRE_RESTAURANTE) REFERENCES MENU(NOMBRE, NOMBRE_RESTAURANTE),
    CONSTRAINT CK_ENTMENU CHECK (ENTREGADO IN ('0','1'))
);

CREATE TABLE PEDIDO_PROD
(
    NUMERO_CUENTA VARCHAR(20) CONSTRAINT NUMCUENTA_PEDIDO_PROD_EXISTE NOT NULL,
    ID_PRODUCTO   INTEGER CONSTRAINT PRODUCTO_PEDIDO_PROD_EXISTE NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) CONSTRAINT RESTAURANTE_PEDIDO_PROD_EXISTE NOT NULL,
    CANTIDAD INTEGER CONSTRAINT CANTIDAD_PEDIDO_PROD_EXISTE NOT NULL,
    ENTREGADO CHAR(1),
    CONSTRAINT PK_PEDIDO_PROD PRIMARY KEY (NUMERO_CUENTA, ID_PRODUCTO, NOMBRE_RESTAURANTE),
    CONSTRAINT FK_NUMEROCUENTA_PEDIDO_PROD FOREIGN KEY (NUMERO_CUENTA) REFERENCES CUENTA(NUMEROCUENTA),
    CONSTRAINT FK_PRODUCTO_DEL_PEDIDO FOREIGN KEY (ID_PRODUCTO, NOMBRE_RESTAURANTE) REFERENCES INFO_PROD_REST(ID_PRODUCTO, NOMBRE_RESTAURANTE),
    CONSTRAINT CK_ENTPROD CHECK (ENTREGADO IN ('0','1'))

);




CREATE TABLE RESERVA
(
    FECHA DATE ,
    ID_RESERVADOR INTEGER ,
    NUM_PERSONAS INTEGER CONSTRAINT NUM_PERSONAS_RESERVA_EXISTE NOT NULL,
    NOMBRE_ZONA VARCHAR(20),
    NOMBRE_MENU VARCHAR(20),
    NOMBRE_RESTAURANTE VARCHAR(20),
    CONSTRAINT PK_RESERVA PRIMARY KEY (FECHA, ID_RESERVADOR),
    CONSTRAINT FK_IDRESERVADOR FOREIGN KEY (ID_RESERVADOR) REFERENCES USUARIO(ID),
    CONSTRAINT FK_ZONARESERVADA FOREIGN KEY (NOMBRE_ZONA) REFERENCES ZONA(NOMBRE),
    CONSTRAINT FK_MENU_RESERVADO FOREIGN KEY (NOMBRE_MENU, NOMBRE_RESTAURANTE) REFERENCES MENU(NOMBRE, NOMBRE_RESTAURANTE)
);
 
 CREATE TABLE PREFERENCIAZONA(
 IDUSUARIO INTEGER,
 NOMBREZONA VARCHAR(20),
 CONSTRAINT FK_USUARIOPREF FOREIGN KEY (IDUSUARIO) REFERENCES PREFERENCIA(IDUSUARIO),
 CONSTRAINT FK_ZONAPREF FOREIGN KEY (NOMBREZONA) REFERENCES ZONA(NOMBRE),
 CONSTRAINT PK_PREFERENCIAZONA PRIMARY KEY (IDUSUARIO,NOMBREZONA)
 );
  

 
 CREATE TABLE PREFERENCIACATEGORIA(
 IDUSUARIO INTEGER,
 NOMBRECATEGORIA VARCHAR(50),
 CONSTRAINT FK_IDPREFERENCIA FOREIGN KEY (IDUSUARIO) REFERENCES PREFERENCIA(IDUSUARIO),
 CONSTRAINT FK_CATEGORIAPREFERENCIA FOREIGN KEY (NOMBRECATEGORIA) REFERENCES CATEGORIA(NOMBRE),
 CONSTRAINT PK_PREFERENCIACATEGORIA PRIMARY KEY (IDUSUARIO, NOMBRECATEGORIA));

COMMIT;

