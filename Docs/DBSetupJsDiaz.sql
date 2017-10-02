DROP TABLE CATEGORIA_MENU;
DROP TABLE CATEGORIA_RESTAURANTE;
DROP TABLE CATEGORIA_PRODUCTO;
DROP TABLE PERTENECE_A_MENU;
DROP TABLE PERTENECE_A_PLATO;
DROP TABLE PEDIDO_MENU;
DROP TABLE PEDIDO_PROD;
DROP TABLE INFO_PROD_REST;
DROP TABLE INFO_ING_REST;
DROP TABLE RESERVA;
DROP TABLE MENU;
DROP TABLE RESTAURANTE;

CREATE TABLE RESTAURANTE
(
    NOMBRE VARCHAR(20) NOT NULL,
    PAG_WEB VARCHAR(40),
    ID_REPRESENTANTE INTEGER NOT NULL,
    NOMBRE_ZONA VARCHAR(20) NOT NULL,
    PRIMARY KEY (NOMBRE),
    FOREIGN KEY (ID_REPRESENTANTE) REFERENCES USUARIO(ID),
    FOREIGN KEY (NOMBRE_ZONA) REFERENCES ZONA(NOMBRE)
);

CREATE TABLE MENU
(
    NOMBRE VARCHAR(20) NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) NOT NULL,
    PRECIO NUMBER NOT NULL,
    COSTO NUMBER,
    PRIMARY KEY(NOMBRE, NOMBRE_RESTAURANTE),
    FOREIGN KEY (NOMBRE_RESTAURANTE) REFERENCES RESTAURANTE(NOMBRE)
);

CREATE TABLE INFO_PROD_REST
(
    ID_PRODUCTO INTEGER NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) NOT NULL,
    PRECIO NUMBER NOT NULL,
    COSTO NUMBER,
    DISPONIBILIDAD INTEGER,
    FECHA_INICIO DATE,
    FECHA_FIN DATE,
    PRIMARY KEY(ID_PRODUCTO, NOMBRE_RESTAURANTE),
    FOREIGN KEY (ID_PRODUCTO) REFERENCES PRODUCTO(ID),
    FOREIGN KEY (NOMBRE_RESTAURANTE) REFERENCES RESTAURANTE(NOMBRE)
);

CREATE TABLE INFO_ING_REST
(
    ID_INGREDIENTE INTEGER NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) NOT NULL,
    PRECIO_ADICION NUMBER,
    PRECIO_SUSTITUTO NUMBER,
    PRIMARY KEY (ID_INGREDIENTE, NOMBRE_RESTAURANTE),
    FOREIGN KEY (ID_INGREDIENTE) REFERENCES INGREDIENTE(ID),
    FOREIGN KEY( NOMBRE_RESTAURANTE) REFERENCES RESTAURANTE(NOMBRE)
);

CREATE TABLE PERTENECE_A_MENU
(
    NOMBRE_MENU VARCHAR(20) NOT NULL,
    ID_PLATO INTEGER NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) NOT NULL,
    PRIMARY KEY (NOMBRE_MENU, ID_PLATO, NOMBRE_RESTAURANTE),
    FOREIGN KEY (NOMBRE_MENU, NOMBRE_RESTAURANTE) REFERENCES MENU(NOMBRE, NOMBRE_RESTAURANTE),
    FOREIGN KEY (ID_PLATO, NOMBRE_RESTAURANTE) REFERENCES INFO_PROD_REST(ID_PRODUCTO, NOMBRE_RESTAURANTE)
);

CREATE TABLE PERTENECE_A_PLATO
(
    ID_PLATO INTEGER NOT NULL,
    ID_INGREDIENTE INTEGER NOT NULL,
    PRIMARY KEY (ID_PLATO, ID_INGREDIENTE),
    FOREIGN KEY (ID_PLATO) REFERENCES PRODUCTO(ID),
    FOREIGN KEY (ID_INGREDIENTE) REFERENCES INGREDIENTE(ID)
);

CREATE TABLE CATEGORIA_MENU
(
    NOMBRE_CATEGORIA VARCHAR(20) NOT NULL,
    NOMBRE_MENU VARCHAR(20) NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) NOT NULL,
    PRIMARY KEY (NOMBRE_CATEGORIA, NOMBRE_MENU, NOMBRE_RESTAURANTE),
    FOREIGN KEY (NOMBRE_CATEGORIA) REFERENCES CATEGORIA(NOMBRE),
    FOREIGN KEY (NOMBRE_MENU, NOMBRE_RESTAURANTE) REFERENCES MENU(NOMBRE, NOMBRE_RESTAURANTE)
);

CREATE TABLE CATEGORIA_RESTAURANTE
(
    NOMBRE_CATEGORIA VARCHAR(20) NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) NOT NULL,
    PRIMARY KEY (NOMBRE_CATEGORIA, NOMBRE_RESTAURANTE),
    FOREIGN KEY (NOMBRE_CATEGORIA) REFERENCES CATEGORIA(NOMBRE),
    FOREIGN KEY (NOMBRE_RESTAURANTE) REFERENCES RESTAURANTE(NOMBRE)
);

CREATE TABLE CATEGORIA_PRODUCTO
(
    NOMBRE_CATEGORIA VARCHAR(20) NOT NULL,
    ID_PRODUCTO INTEGER NOT NULL,
    PRIMARY KEY (NOMBRE_CATEGORIA, ID_PRODUCTO),
    FOREIGN KEY (NOMBRE_CATEGORIA) REFERENCES CATEGORIA(NOMBRE),
    FOREIGN KEY (ID_PRODUCTO) REFERENCES PRODUCTO(ID)
);

CREATE TABLE RESERVA
(
    FECHA DATE NOT NULL,
    ID_RESERVADOR INTEGER NOT NULL,
    NUM_PERSONAS INTEGER NOT NULL,
    NOMBRE_ZONA VARCHAR(20),
    NOMBRE_MENU VARCHAR(20),
    NOMBRE_RESTAURANTE VARCHAR(20),
    PRIMARY KEY (FECHA, ID_RESERVADOR),
    FOREIGN KEY (ID_RESERVADOR) REFERENCES USUARIO(ID),
    FOREIGN KEY (NOMBRE_ZONA) REFERENCES ZONA(NOMBRE),
    FOREIGN KEY (NOMBRE_MENU, NOMBRE_RESTAURANTE) REFERENCES MENU(NOMBRE, NOMBRE_RESTAURANTE)
);

CREATE TABLE PEDIDO_MENU
(
    NUMERO_CUENTA VARCHAR(20) NOT NULL,
    NOMBRE_MENU VARCHAR(20) NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) NOT NULL,
    CANTIDAD INTEGER NOT NULL,
    PRIMARY KEY (NUMERO_CUENTA, NOMBRE_MENU, NOMBRE_RESTAURANTE),
    FOREIGN KEY (NUMERO_CUENTA) REFERENCES CUENTA(NUMEROCUENTA),
    FOREIGN KEY (NOMBRE_MENU, NOMBRE_RESTAURANTE) REFERENCES MENU(NOMBRE, NOMBRE_RESTAURANTE)
);

CREATE TABLE PEDIDO_PROD
(
    NUMERO_CUENTA VARCHAR(20) NOT NULL,
    ID_PRODUCTO INTEGER NOT NULL,
    NOMBRE_RESTAURANTE VARCHAR(20) NOT NULL,
    CANTIDAD INTEGER NOT NULL,
    PRIMARY KEY (NUMERO_CUENTA, ID_PRODUCTO, NOMBRE_RESTAURANTE),
    FOREIGN KEY (NUMERO_CUENTA) REFERENCES CUENTA(NUMEROCUENTA),
    FOREIGN KEY (ID_PRODUCTO, NOMBRE_RESTAURANTE) REFERENCES INFO_PROD_REST(ID_PRODUCTO, NOMBRE_RESTAURANTE)
);
