package vos;
/**
 * Clase que modela una zona del restuarante.
 * @author jc161
 *
 */

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Zona extends ZonaMinimum {
	/**
	 * Condiciones de la zona.
	 */
	@JsonProperty(value="condiciones")
	private List<CondicionTecnica> condiciones;
	/**
	 * RestauranteMinimums de la zona.
	 */
	@JsonProperty(value="restaurantes")
	private List<RestauranteMinimum> restaurantes;
	/**
	 * Construye una nueva zona con los parámetros dados.<br>
	 * @param capacidad<br>
	 * @param ingresoEspecial<br>
	 * @param abiertaActualmente<br>
	 * @param capacidadOcupada<br>
	 * @param nombre<br>
	 * @param condiciones<br>
	 * @param restaurantes<br>
	 */
	public Zona(@JsonProperty(value="capacidad")int capacidad, @JsonProperty(value="ingresoEspecial")boolean ingresoEspecial, @JsonProperty(value="abiertaActualmente")boolean abiertaActualmente, @JsonProperty(value="capacidadOcupada")int capacidadOcupada, @JsonProperty(value="nombre")String nombre,
			@JsonProperty(value="condiciones")List<CondicionTecnica> condiciones, @JsonProperty(value="restaurantes")List<RestauranteMinimum> restaurantes) {
		super(capacidad,ingresoEspecial,abiertaActualmente,capacidadOcupada,nombre);
		this.condiciones = condiciones;
		this.restaurantes = restaurantes;
	}

	/**
	 * Obtiene un listado de las condiciones técnicas.<br>
	 * @return condiciones
	 */
	public List<CondicionTecnica> getCondiciones() {
		return condiciones;
	}
	/**
	 * Modifica el valor de las condiciones al dado por parámetro.<bR>
	 * @param condiciones 
	 */
	public void setCondiciones(List<CondicionTecnica> condiciones) {
		this.condiciones = condiciones;
	}
	/**
	 * Obtiene los restaurantes de la zona.<br>
	 * @return restaurantes
	 */
	public List<RestauranteMinimum> getRestauranteMinimums() {
		return restaurantes;
	}
	/**
	 * Modifica el valor de los restaurantes al dado por parámetro.<br>
	 * @param restaurantes
	 */
	public void setRestauranteMinimums(List<RestauranteMinimum> restaurantes) {
		this.restaurantes = restaurantes;
	}
	

}
