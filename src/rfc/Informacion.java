package rfc;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase privada de la información utilizada.<br>
 * @author jc161 
 *
 */
public class Informacion{
	/**
	 * Nombre de la información.
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	
	/**
	 * Valor de la información.
	 */
	@JsonProperty(value="valor")
	private String valor;
	/**
	 * Construye una nueva clase de información.<br>
	 * @param nombre Nombre de la información.<br>
	 * @param valor Valor de la información.
	 */
	public Informacion(@JsonProperty(value="nombre") String nombre,
			@JsonProperty(value="valor") String valor)
	{
		this.nombre=nombre;
		this.valor=valor;
	}
	/**
	 * Obtiene el nombre de la información.<br>
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * Modifica el nombre al valor dado por parámetro.<br>
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * Obtiene el valor de la información.<br>
	 * @return valor
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * Modifica el valor a la denominación dada por parámetro.<br>
	 * @param valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
}
