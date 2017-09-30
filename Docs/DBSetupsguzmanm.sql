CREATE TABLE ROL (
 NOMBRE VARCHAR(20),
 CONSTRAINT PK_ROL PRIMARY KEY (NOMBRE),
 CONSTRAINT CK_ROL CHECK (NOMBRE IN ('CLIENTE','OPERADOR','ORGANIZADORES','LOCAL','PROVEEDOR'))
 );
 
 CREATE TABLE USUARIO (
 ID INTEGER,
 CORREO VARCHAR(20),
 NOMBRE VARCHAR (20) NOT NULL,
 ROL VARCHAR (20) NOT NULL,
 CONSTRAINT FK_ROL FOREIGN KEY (ROL) REFERENCES ROL(NOMBRE),
 CONSTRAINT PK_USUARIO PRIMARY KEY(ID),
 ADD CONSTRAINT CK_ID CHECK (ID>=0),
 CHECK (ID>=0));
 
 
 CREATE TABLE ZONA (
  NOMBRE VARCHAR(20),
 CAPACIDAD INTEGER NOT NULL,
 INGRESOESPECIAL CHAR(1) NOT NULL,
 ABIERTAACTUALMENTE CHAR(1) NOT NULL,
 CAPACIDADOCUPADA INTEGER NOT NULL,
 CONSTRAINT PK_ZONA PRIMARY KEY (NOMBRE),
 CONSTRAINT CK_CAPACIDAD CHECK (CAPACIDAD>=0),
 CONSTRAINT CK_OCUPADA CHECK (CAPACIDADOCUPADA>=0),
 CONSTRAINT CK_CAPACIDADES CHECK (CAPACIDADOCUPADA<=CAPACIDAD),
 CONSTRAINT CK_ABIERTA CHECK (ABIERTAACTUALMENTE IN ('0','1')),
 CONSTRAINT CK_INGRESOESPECIAL CHECK (INGRESOESPECIAL IN ('0','1'))
);

 
 CREATE TABLE CONDICIONTECNICA (
 NOMBRE VARCHAR(20),
 CONSTRAINT PK_CONDICIONTECNICA PRIMARY KEY (NOMBRE));
 
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
 

 
 CREATE TABLE PREFERENCIAZONA(
 IDUSUARIO INTEGER,
 NOMBREZONA VARCHAR(20),
 CONSTRAINT FK_USUARIOPREF FOREIGN KEY (IDUSUARIO) REFERENCES PREFERENCIA(IDUSUARIO),
 CONSTRAINT FK_ZONAPREF FOREIGN KEY (NOMBREZONA) REFERENCES ZONA(NOMBRE),
 CONSTRAINT PK_PREFERENCIAZONA PRIMARY KEY (IDUSUARIO,NOMBREZONA)
 );
  
 CREATE TABLE CATEGORIA(
 NOMBRE VARCHAR(20),
 CONSTRAINT PK_CATEGORIA PRIMARY KEY (NOMBRE)
 );
 
 CREATE TABLE PREFERENCIACATEGORIA(
 IDUSUARIO INTEGER,
 NOMBRECATEGORIA VARCHAR(20),
 CONSTRAINT FK_IDPREFERENCIA FOREIGN KEY (IDUSUARIO) REFERENCES PREFERENCIA(IDUSUARIO),
 CONSTRAINT FK_CATEGORIAPREFERENCIA FOREIGN KEY (NOMBRECATEGORIA) REFERENCES CATEGORIA(NOMBRE),
 CONSTRAINT PK_PREFERENCIACATEGORIA PRIMARY KEY (IDUSUARIO, NOMBRECATEGORIA));
 
CREATE TABLE CUENTA(
VALOR NUMBER NOT NULL,
NUMEROCUENTA VARCHAR (20) NOT NULL,
FECHA DATE NOT NULL,
IDUSUARIO INTEGER ,
CONSTRAINT FK_IDUSUARIOCUENTA FOREIGN KEY (IDUSUARIO) REFERENCES USUARIO(ID),
CONSTRAINT PK_CUENTA PRIMARY KEY (NUMEROCUENTA),
CONSTRAINT CK_NUMCUENTA CHECK (REGEXP_LIKE(NUMEROCUENTA, '^[[:digit:]]+$')));

CREATE TABLE TIPOSDEPLATO(
NOMBRE VARCHAR(20),
CONSTRAINT PK_TIPOSP PRIMARY KEY (NOMBRE),
CONSTRAINT CK_TIPOSP CHECK (NOMBRE IN ('ENTRADA','PLATO FUERTE','POSTRE','BEBIDA','ACOMPAÑAMIENTO'))
);

CREATE TABLE PRODUCTO(
ID INTEGER,
NOMBRE VARCHAR(20) NOT NULL,
TIPO VARCHAR(20) NOT NULL,
PERSONALIZABLE CHAR(1) NOT NULL,
PRECIO NUMBER NOT NULL,
TRADUCCION VARCHAR(500),
DESCRIPCION VARCHAR(500),
COSTOPRODUCCION NUMBER NOT NULL,
TIEMPO NUMBER,
CONSTRAINT PK_PRODUCTO PRIMARY KEY (ID),
CONSTRAINT FK_TIPO FOREIGN KEY (TIPO) REFERENCES TIPOS_DE_PLATO (NOMBRE),
CONSTRAINT CK_NUMEROS CHECK (PRECIO >=0 AND COSTOPRODUCCION >=0 AND TIEMPO >=0),
CONSTRAINT CK_DESCRIPCION CHECK ((DESCRIPCION IS NULL AND TRADUCCION IS NULL) OR (DESCRIPCION IS NOT NULL AND TRADUCCION IS NOT NULL)),
CONSTRAINT CK_PERSONALIZABLE CHECK (PERSONALIZABLE IN ('0','1')),
CONSTRAINT CK_DESCRIPCION2 CHECK ((DESCRIPCION LIKE 'null' AND TRADUCCION LIKE 'null') OR (DESCRIPCION  NOT LIKE 'null' AND TRADUCCION  NOT LIKE 'null')),
CONSTRAINT CK_DESCRIPCION3 CHECK (((DESCRIPCION LIKE '' AND TRADUCCION LIKE '') OR (DESCRIPCION  NOT LIKE '' AND TRADUCCION  NOT LIKE '')))

);

CREATE TABLE INGREDIENTE(
ID INTEGER,
NOMBRE VARCHAR(20) NOT NULL,
DESCRIPCION VARCHAR (20),
TRADUCCION VARCHAR (20),
CONSTRAINT PK_ING PRIMARY KEY (ID),
CONSTRAINT CK_DESCING CHECK ((DESCRIPCION IS NULL AND TRADUCCION IS NULL) OR (DESCRIPCION IS NOT NULL AND TRADUCCION IS NOT NULL)),
CONSTRAINT CK_DESCING2 CHECK ((DESCRIPCION LIKE 'null' AND TRADUCCION LIKE 'null') OR (DESCRIPCION  NOT LIKE 'null' AND TRADUCCION  NOT LIKE 'null')),
CONSTRAINT CK_DESCING3 CHECK (((DESCRIPCION LIKE '' AND TRADUCCION LIKE '') OR (DESCRIPCION  NOT LIKE '' AND TRADUCCION  NOT LIKE '')))
);

ALTER TABLE PRODUCTO
DROP CONSTRAINT CK_DESCING3;

ALTER TABLE INGREDIENTE
ADD CONSTRAINT CK_DESCING3 CHECK (((DESCRIPCION LIKE '' AND TRADUCCION LIKE '') OR (DESCRIPCION  NOT LIKE '' AND TRADUCCION  NOT LIKE '')));

CREATE SEQUENCE IDUSUARIO 
    MINVALUE 0
    INCREMENT BY 1;
    
CREATE SEQUENCE IDPRODUCTO
    MINVALUE 0
    INCREMENT BY 1;

CREATE SEQUENCE IDINGREDIENTE
    MINVALUE 0
    INCREMENT BY 1;
    
CREATE SEQUENCE NUMCUENTA
    MINVALUE 0
    INCREMENT BY 1;


CREATE TABLE CRITERIOORGZONA (
    NOMBRE VARCHAR(30),
    CONSTRAINT PK_COZ PRIMARY KEY (NOMBRE)
);


CREATE TABLE CRITERIOGRUZONA (
    NOMBRE VARCHAR(30),
    CONSTRAINT PK_CGZ PRIMARY KEY (NOMBRE)
);


CREATE TABLE CRITERIOORGPROD (
    NOMBRE VARCHAR(30),
    CONSTRAINT PK_COP PRIMARY KEY (NOMBRE)
);


CREATE TABLE CRITERIOGRUPPROD (
    NOMBRE VARCHAR(30),
    CONSTRAINT PK_CGP PRIMARY KEY (NOMBRE)
);

