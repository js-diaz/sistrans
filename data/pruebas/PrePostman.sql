
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


--DEFAULT
DELETE FROM INGREDIENTE WHERE ID>100;
DELETE FROM PERTENECE_A_PLATO WHERE ID_PLATO>100;
DELETE FROM PRODUCTO WHERE ID>100;
DELETE FROM USUARIO WHERE ID >220;
COMMIT;

SELECT * FROM RESTAURANTE;