package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que modela un criterio cualquiera.<br>
 * @author jc161
 *
 */


public class Criterio {

	/**
	 * Nombre del criterio.
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	/**
	 * Construye un nuevo criterio.<br>
	 * @param nombre Nombre del nuevo criterio.
	 */
	public Criterio(@JsonProperty (value="nombre")String nombre)
	{
		this.nombre=nombre;
	}
	/**
	 * Retorna el nombre.<br>
	 * @return nombre
	 */
	public String getNombre(){ return nombre;}
	/**
	 * Modifica la categoría al parámetro dado.<br>
	 * @param n Nueva categoría
	 */
	public void setNombre(String n){ nombre=n;}
}
