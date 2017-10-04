ESPECIFICACIONES PARA CORRER EL PROYECTO
1. Realizar “clean” antes de correr la aplicación web.
2. Seleccionar “Run As”-> “Run On Server”.
3. Esperar al despliegue de la aplicación.
4. Utilizar postman.

Si se desea hacer consultas con cadenas de caracteres que tienen espacios, deben estar separadas por la combinación ‘%20’. Es decir si quiero consultar “LA ZONA” en el URL debe aparecer “LA%20ZONA”.

A su vez, por algún motivo ajeno a nosotros, cuando se intentan obtener todos los usuarios y todas las cuentas, se obtiene un error recursivo. No sabemos la causa de esto, y la verdad no sabríamos cómo reducirlo. A pesar de que utilizamos representaciones minimum, se presentan esto solo cuando se pide un flujo de información muy grande. Pero el recurso sirve perfectamente cuando se piden pequeños pedazos de la información (buscar por id o por número de cuenta).

REQUERIMIENTOS FUNCIONALES:
Para los requerimientos y la privacidad de las acciones se tomó la determinación de pasar un HeaderParam llamado usuarioId, que tiene el id del usuario que va a realizar la acción, con el fin de garantizar al privacidad de la información.

RF1:	Registra a un usuario, dependiendo del tipo se inicial izan sus demás atributos de 	la forma adecuada. Por ejemplo si es un cliente su historial se iniciativa en algo 	vacío.
	-URL: [POST]localhost:8080/VideoAndes/rest/usuarios
	-Json:{
        "nombre": "USUARIO",
        "correo": "fYYgBEXCii",
        "rol": "CLIENTE",
        "preferencia": null,
        "historial": [],
        "restaurante": null
    	}
RF2:	Mismo que el requerimiento funcional 1.
RF3:
RF4:	Registra un producto en el sistema. De forma automática se registran sus 		ingredientes y se le asigna un id, debido a que es solo único que lo diferencia en 	el sistema.
	-URL: [POST] localhost:8080/VideoAndes/rest/productos
	-Json:{
	"personalizable": true,
        "nombre": "Nuevo plato",
        "precio": 200,
        "tipo": "PLATO_FUERTE",
        "descripcion": "nOqKROYhql",
        "traduccion": "CdNWDkklSE",
        "tiempo": 4,
         "categorias":[],
        "costoProduccion": 100,
        "ingredientes": [{"nombre": "aeJBdjjhFK",
        "descripcion": "SjVqIAiRRJ",
        "traduccion": "rLWTNjQeKH",
        "id": 1}]
	}
RF5:	Registra un ingrediente en el sistema.
	-URL: [POST] localhost:8080/VideoAndes/rest/ingredientes
	-Json: {
        "nombre":"Nuevo",
        "descripcion":"Un ingrediente",
        "traduccion":"An ingredient"
	}
RF6:	
RF7:	Registra una zona en el sistema.
	URL: [POST] localhost:8080/VideoAndes/rest/zonas
	Json:
	{
	"capacidad": 49,
        "ingresoEspecial": true,
        "abiertaActualmente": true,
        "capacidadOcupada": 39,
        "nombre": "Nueva",
        "condiciones": [
            {
                "nombre": "Organized"
            }
        ]
	}
RF8: Para este requerimiento se manejo que un usuario solo puede tener una única preferencia, la cual se puede ir modificando utilizando acciones de actualización en los diferentes subrecursos propuestos. Si se quiere agregar una preferencia se puede poner el Json directamente con toda la información disponible.
	URL: [POST] localhost:8080/VideoAndes/rest/preferencias/{idUsuario}
	Json:
{
     "precioInicial": 40.36,
    "precioFinal": 70.36,
    "zonas": [
        {
            "capacidad": 55,
            "ingresoEspecial": false,
            "abiertaActualmente": true,
            "capacidadOcupada": 46,
            "nombre": "Kids"
        }
     ],
	"categorias": [
        {
            "nombre": "Taiwanese"
        },
        {
            "nombre": "Ute"
        }
    ]
}
A su vez se puede agregar parcialmente solo colocando las preferencias de precio como sigue:
	URL:[POST]localhost:8080/VideoAndes/rest/preferencias/{idUsuario}
	Json:{
	"precioInicial": 80,
        "precioFinal": 180
	}
Agregando preferencias de zona de la siguiente forma: Con un HeaderParam indicando la preferencia del usuario.
	URL:[POST]localhost:8080/VideoAndes/rest/preferencias/zonas
	Json: [
	{
		"capacidad": 87,
        "ingresoEspecial": true,
        "abiertaActualmente": false,
        "capacidadOcupada": 38,
        "nombre": "Sports"
	},
	{
		"capacidad": 57,
        "ingresoEspecial": true,
        "abiertaActualmente": false,
        "capacidadOcupada": 17,
        "nombre": "Computers"
	}
	]
Agregando preferencias de categoría de la siguiente forma: Con un HeaderParam indicando la preferencia del usuario.

	URL:[POST] localhost:8080/VideoAndes/rest/preferencias/categorias
	JSON:
	[
	{
		"nombre":"Filipino"
	},
	{
		"nombre":"Ute"
	},
	{
		"nombre":"Cuban"
	}
	]	
Por último, y si se desea, se pueden realizar acciones de actualización sobre los precios con el siguiente formato.
	URL:[PUT]localhost:8080/VideoAndes/rest/preferencias/{idUsuario}
	Json:{
	"precioInicial": 60,
        "precioFinal": 140
	}
Si se quiere actualizar únicamente las preferencias de zona o de categoría, el usuario tiene la opción de agregar sus preferencias y quitar las que no le gustan directamente. No se permitió hacer una actualización completa.
RF9:
RF10:
Para este requerimiento se simuló el procedimiento de compra de usuario intentando actualizar la compra que tiene un número dado. El formato es el siguiente:
	-URL:[PUT]localhost:8080/VideoAndes/rest/cuentas
	-Json: 
RFC1:
RFC2: Por si acaso, las anotaciones utilizadas para el Json se encuentran en las clases 	involucradas en el procedimiento (Revisar el archivo ITERACION2.pdf). A su vez, se 	imprime el mensaje SQL en la consola para revisar que interpreta el sistema. 
	 Pide la contenedora que tiene la información dada por parámetro.
	-URL:[POST] localhost:8080/VideoAndes/rest/zonas/completo
	-Json:
	TIPO1: Agrupamiento y ordenamiento
	{
	"orden":[
		{
			"nombre":"CAPACIDAD",
			"ascendente":false
		}
		],
	"agrupacion":[
		{
			"nombre":"CAPACIDAD"
		},
		{
			"nombre":"NOMBRE_MENU"
		}
		],
		"agregacion":[
		{
			"distinto":false
		},
		{
			"valorAgrupacion":"CAPACIDADOCUPADA",
			"agregacion":"MIN",
			"distinto":true
		},
		{
			"valorAgrupacion":"ABIERTAACTUALMENTE",
			"agregacion":"AVG",
			"distinto":false
		}
		]
	}
	TIPO2: Comparando con valores fijos. Si no se envía una operación en la sección 	del where, se asume que se utiliza un LIKE.
	{
	"orden":[
		{
			"nombre":"CAPACIDAD",
			"ascendente":false
		}
		],
	"where":{
		"valorAnterior1":{
			"nombre":"NUMERO_CUENTA"
		},
		"valorComparacion1":"1%",
		"afirmativo1":true
	}
	}
	TIPO3: Comparación de criterios contra criterios
	{
	"orden":[
		{
			"nombre":"CAPACIDAD",
			"ascendente":false
		}
		],
	"agrupacion":[
		{
			"nombre":"CAPACIDAD"
		},
		{
			"nombre":"INGRESOESPECIAL"
		}
		],
	"agregacion":[
		{
			"valorAgrupacion":"CAPACIDAD",
			"agregacion":"MAX",
			"distinto":false
		},
		{
			"valorAgrupacion":"CAPACIDADOCUPADA",
			"agregacion":"MIN",
			"distinto":true
		},
		{
			"valorAgrupacion":"ABIERTAACTUALMENTE",
			"agregacion":"AVG",
			"distinto":false
		}
		],
		"where":{
			"valorAnterior1":
			{
				"nombre":"CAPACIDAD"
			},
			"valorComparacion2":
			{
				"nombre":"CAPACIDADOCUPADA"
			},
			"operacion1":"=",
			"afirmativo1":false
		},
		"having":{
			"valorAnterior1":
			{
				"valorAgrupacion":"CAPACIDAD",
				"agregacion":"MAX",
				"distinto":false
			},
			"valorComparacion2":
			{
				"valorAgrupacion":"CAPACIDADOCUPADA",
				"agregacion":"MIN",
				"distinto":false
			},
			"operacion1":">",
			"afirmativo1":true
		}
	}
	TIPO4: Conjunción y disyunción
	{
	"orden":[
		{
			"nombre":"CAPACIDAD",
			"ascendente":false
		}
		],
	"agrupacion":[
		{
			"nombre":"CAPACIDAD"
		},
		{
			"nombre":"INGRESOESPECIAL"
		}
		],
	"agregacion":[
		{
			"valorAgrupacion":"CAPACIDAD",
			"agregacion":"MAX",
			"distinto":false
		},
		{
			"valorAgrupacion":"CAPACIDADOCUPADA",
			"agregacion":"MIN",
			"distinto":true
		},
		{
			"valorAgrupacion":"ABIERTAACTUALMENTE",
			"agregacion":"AVG",
			"distinto":false
		}
		],
		"where":{
			"c1":{
				"valorAnterior1":
				{
					"nombre":"CAPACIDAD"
				},
				"valorComparacion2":
				{
					"nombre":"CAPACIDADOCUPADA"
				},
				"operacion1":"=",
				"afirmativo1":false
			},
			"c2":{
				"valorAnterior1":
				{
					"nombre":"CAPACIDAD"
				},
				"valorComparacion2":
				{
					"nombre":"CAPACIDADOCUPADA"
				},
				"operacion1":"=",
				"afirmativo1":false
			},
			"conjuncion":true,
			"afirmativo1":true
			
		},
		"having":{
			"c1":{
				"valorAnterior1":
				{
					"valorAgrupacion":"ABIERTAACTUALMENTE",
					"agregacion":"AVG",
					"distinto":false
				},
				"valorComparacion2":
				{
					"valorAgrupacion":"ABIERTAACTUALMENTE",
					"agregacion":"AVG",
					"distinto":false
				},
				"operacion1":"=",
				"afirmativo1":true
			},
			"c2":{
				"valorAnterior1":
				{
					"valorAgrupacion":"ABIERTAACTUALMENTE",
					"agregacion":"MAX",
					"distinto":false
				},
				"valorComparacion2":
				{
					"valorAgrupacion":"ABIERTAACTUALMENTE",
					"agregacion":"MIN",
					"distinto":false
				},
				"operacion1":"=",
				"afirmativo1":false
			},
			"conjuncion":false,
			"afirmativo1":true
		}
	}
Si se desea hacer para solo una zona, se utiliza el siguiente URL: 
	[POST] localhost:8080/VideoAndes/rest/zonas/completo/{nombreDeLaZona}
Si se quieren más ejemplos se recomienda que se revisen las pruebas al respecto.


RFC3: Pide la representación completa de un usuario.
	-URL:[GET] localhost:8080/VideoAndes/rest/usuarios/completo/106
	-Json: No aplica
RFC4:
RFC5: Con el usuarioId del HeaderParam se establece cómo se fragmenta la información.
	URL:[POST] localhost:8080/VideoAndes/rest/zonas/rentabilidad
	Json:{
	"fechaInicial":"2015-10-31",
	"fechaFinal":"2019-10-31"
	}
RFC6:
