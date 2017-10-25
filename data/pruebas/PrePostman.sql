
DROP SEQUENCE IDINGREDIENTE;

create sequence IDINGREDIENTE
minvalue 1
maxvalue 999999999999999999999
start with 101
increment by 1;

DROP SEQUENCE IDPRODUCTO;

create sequence IDPRODUCTO
minvalue 1
maxvalue 999999999999999999999
start with 101
increment by 1;

DROP SEQUENCE IDUSUARIO;

create sequence IDUSUARIO
minvalue 1
maxvalue 999999999999999999999
start with 221
increment by 1;

DROP SEQUENCE IDMESA;

create sequence IDMESA
minvalue 1
maxvalue 999999999999999999999
start with 101
increment by 1;

--DEFAULT
DELETE FROM INGREDIENTE WHERE ID>100;
DELETE FROM PERTENECE_A_PLATO WHERE ID_PLATO>100;
DELETE FROM PRODUCTO WHERE ID>100;
DELETE FROM MESA WHERE ID>100;
DELETE FROM USUARIO WHERE ID >220;
DELETE FROM PEDIDO_PROD WHERE NUMERO_CUENTA='1' OR NUMERO_CUENTA='2' OR NUMERO_CUENTA='3' OR NUMERO_CUENTA='4' OR NUMERO_CUENTA='5' OR NUMERO_CUENTA='6' OR NUMERO_CUENTA='10' OR NUMERO_CUENTA='20' OR NUMERO_CUENTA='9' OR NUMERO_CUENTA='15';
DELETE FROM PEDIDO_MENU WHERE NUMERO_CUENTA='1' OR NUMERO_CUENTA='2' OR NUMERO_CUENTA='3' OR NUMERO_CUENTA='4' OR NUMERO_CUENTA='5' OR NUMERO_CUENTA='6' OR NUMERO_CUENTA='10' OR NUMERO_CUENTA='20' OR NUMERO_CUENTA='9' OR NUMERO_CUENTA='15';
DELETE FROM CUENTA WHERE NUMEROCUENTA='1' OR NUMEROCUENTA='2' OR NUMEROCUENTA='3' OR NUMEROCUENTA='4' OR NUMEROCUENTA='5' OR NUMEROCUENTA='6' OR NUMEROCUENTA='10' OR NUMEROCUENTA='20' OR NUMEROCUENTA='9' OR NUMEROCUENTA='15';
COMMIT;

SELECT * FROM CUENTA;
