CREATE TABLE temp (
    ID INT PRIMARY KEY IDENTITY(1,1),
    Nombre VARCHAR(50),
    Edad INT
);

INSERT INTO temp (Nombre, Edad) 
VALUES ('Juan', 25), ('Maria', 30), ('Carlos', 28);

-- Iniciar una transacción
BEGIN TRANSACTION;

-- Supongamos que actualizamos mal la edad de 'Juan'
UPDATE temp 
SET Edad = 9999 
WHERE Nombre = 'Carlos';

-- Vemos los cambios antes de confirmar
SELECT * FROM temp;

-- Verificar si hay una transacción activa antes de hacer ROLLBACK
SELECT @@TRANCOUNT AS TransaccionesActivas;

-- Si nos damos cuenta del error, deshacemos los cambios
ROLLBACK;

-- Confirmar los cambios
COMMIT

-- Verificamos que los datos siguen igual
SELECT * FROM temp;
