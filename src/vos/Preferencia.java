package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que modela la preferencia de un usuario.<br>
 * @author jc161
 *
 */
public class Preferencia {

	/**
	 * Precio inicial de un rango.
	 */
	@JsonProperty(value="precioInicial")
	private Double precioInicial;
	/**
	 * Precio final de un rango.
	 */
	@JsonProperty(value="precioFinal")
	private Double precioFinal;
	/**
	 * Listado de zonas de preferencia.
	 */
	@JsonProperty(value="zonas")
	private List<ZonaMinimum> zonas;
	/**
	 * Lista de categorías de preferencia.
	 */
	@JsonProperty (value="categorias")
	private List<Categoria> categorias;
	/**
	 * Constructor de la clase que recibe todos los parámetros dados.<br>
	 * @param precioInicial<br>
	 * @param precioFinal<br>
	 * @param zonas<br>
	 * @param categorias<br>
	 */
	public Preferencia(@JsonProperty(value="precioInicial")Double precioInicial, @JsonProperty(value="precioFinal")Double precioFinal, @JsonProperty(value="zonas")List<ZonaMinimum> zonas, @JsonProperty(value="categorias")List<Categoria> categorias) {
		super();
		this.precioInicial = precioInicial;
		this.precioFinal = precioFinal;
		this.zonas = zonas;
		this.categorias = categorias;
	}
	/**
	 * Obtiene el precio inicial.<br>
	 * @return precioInicial
	 */
	public Double getPrecioInicial() {
		return precioInicial;
	}
	/**
	 * Modifica el precioInicial del producto al dado por parámetro.<br>
	 * @param precioInicial
	 */
	public void setPrecioInicial(Double precioInicial) {
		this.precioInicial = precioInicial;
	}
	/**
	 * Obtiene el precio final del producto.<br>
	 * @return precioFinal
	 */
	public Double getPrecioFinal() {
		return precioFinal;
	}
	/**
	 * Modifica el precio final del producto al dado por parámetro.<br>
	 * @param precioFinal
	 */
	public void setPrecioFinal(Double precioFinal) {
		this.precioFinal = precioFinal;
	}
	/**
	 * Obtiene un listado de zonas de preferencia.<br>
	 * @return zonas
	 */
	public List<ZonaMinimum> getZonaMinimums() {
		return zonas;
	}
	/**
	 * Modifica las zonas de preferencia a aquella lista dada por parámetro.<br>
	 * @param zonas
	 */
	public void setZonaMinimums(List<ZonaMinimum> zonas) {
		this.zonas = zonas;
	}
	/**
	 * Obtiene la lista de categorias de preferencia.<br>
	 * @return categorias
	 */
	public List<Categoria> getCategorias() {
		return categorias;
	}
	/**
	 * Modifica la lista de categorías a la dada por parámetro.<br>
	 * @param categorias
	 */
	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	
}
