# sistemaABM
Permite subir, modificar, dar de baja y listar el contenido de una base de datos SQL.

## Estructura de la Base 📦

Tabla: PERSONA
Columnas: ID, NOMBRE, EDAD, FECHA_NACIMIENTO

## Menú de opciones del programa 📋

1- Alta |2- Modificacion |3- Baja |4- Listado |5- Buscar por nombre |0- Salir

### Listado de métodos ⌨️

**- alta()**
Método de tipo POST que permite ingresar el nombre y la fecha de nacimiento de una persona, calcula la edad llamando a calcularEdad() y persiste los tres datos en la base generando un id.

**- modificacion()**
Método de tipo PUT que toma como parametro un id, busca a la persona en la base, muestra los datos de la persona, pide que el usuario seleccione el dato que desea modificar, solicita el nuevo dato y luego lo persiste en la base.

**- baja()**
Método de tipo DELETE que toma como parametro un id, busca a la persona en la base, muestra los datos de la persona, pide confirmación del usuario y luego borra los datos de la base.

**- listado()**
Método de tipo GET que muestra un listado de todos los datos presentes en la base.

**- buscarXnombre()**
Método de tipo GET que le solicita al usuario que ingrese un nombre o las primeras letras del mismo y trae un listado de todas las personas de la base con las que encuentra correspondencia.

**- mostrarPorID()**
Método de tipo GET que recibe como parámetro un id, realiza la consulta a la base y devuelve los datos de la persona correspondiente.

**- calcularEdad()**
Método de utilización interna que toma como parámetro una fechadenacimiento y utiliza la fecha actual para realizar el cálculo y devolver la edad de la persona.

**- mostrarMenu()**
Método de utilización interna que muestra por pantalla el menu, solicita al usuario que ingrese una opcion, la guarda en una variable y la devuelve.


