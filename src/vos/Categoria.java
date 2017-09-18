package vos;

import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Categoría utilizada para productso, menús y categorías. Se creó para el manejo relacional.
 * @author jc161
 *
 */
public class Categoria {
	/**
	 * Nombre dela categoría.
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	/**
	 * Construye una nueva categoría.<br>
	 * @param nombre Nombre de la nueva categoría.
	 */
	public Categoria(@JsonProperty (value="nombre")String nombre)
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
