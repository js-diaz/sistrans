SELECT NOMBRETABLA, NOMBRECOL, DATA_TYPE AS TIPODATO, NOMBRECONSTRAINTCOLUMNA, NOMBRETABLAREFFK
FROM 
    (SELECT T.TABLE_NAME AS NOMBRETABLA, T.COLUMN_NAME AS NOMBRECOL, T.CONSTRAINT_NAME AS NOMBRECONSTRAINTCOLUMNA
     FROM ALL_CONS_COLUMNS T
     WHERE T.OWNER LIKE 'ISIS2304A061720')
     NATURAL FULL OUTER JOIN
     (SELECT A.TABLE_NAME AS NOMBRETABLA,C.COLUMN_NAME AS NOMBRECOL,B.TABLE_NAME AS NOMBRETABLAREFFK, 
        A.CONSTRAINT_NAME AS NOMBRECONSTRAINTCOLUMNA
        FROM ALL_CONSTRAINTS A, ALL_CONSTRAINTS B, ALL_CONS_COLUMNS C
        WHERE A.OWNER LIKE 'ISIS2304A061720' AND A.CONSTRAINT_NAME LIKE 'FK%' AND A.R_CONSTRAINT_NAME=B.CONSTRAINT_NAME AND 
        B.CONSTRAINT_NAME LIKE 'PK%' AND C.CONSTRAINT_NAME LIKE A.CONSTRAINT_NAME), ALL_TAB_COLUMNS
WHERE NOMBRECONSTRAINTCOLUMNA  NOT LIKE 'BIN%' AND NOMBRETABLA LIKE TABLE_NAME AND NOMBRECOL LIKE COLUMN_NAME AND NOMBRETABLA NOT IN ('RESERVAS','SILLASRESERVAS')
ORDER BY NOMBRETABLA ASC, NOMBRECOL ASC, NOMBRECONSTRAINTCOLUMNA ASC;

