package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que modela las condiciones técnicas de una zona. Se creó para el manejo relacional.
 * @author jc161
 *
 */
public class CondicionTecnica {
	/**
	 * Nombre de la condición técnica.
	 */
	@JsonProperty (value="nombre")
	private String nombre;
	/**
	 * Construye una nueva condición técnica.<br>
	 * @param nombre Nombre de la nueva condición técnica.
	 */
	public CondicionTecnica(@JsonProperty(value="nombre")String nombre)
	{
		this.nombre=nombre;
	}
	/**
	 * Retorna el nombre de la condición.<br>
	 * @return nombre
	 */
	public String getNombre(){
		return nombre;
	}
	/**
	 * Modifica el nombre de la condición a aquella dada por parámetro.<br>
	 * @param n Nuevo nombre.
	 */
	public void setNombre(String n)
	{
		nombre=n;
	}
}
