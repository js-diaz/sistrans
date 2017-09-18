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
	private List<Zona> zonas;
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
	public Preferencia(@JsonProperty(value="precioInicial")Double precioInicial, @JsonProperty(value="precioFinal")Double precioFinal, @JsonProperty(value="zonas")List<Zona> zonas, @JsonProperty(value="categorias")List<Categoria> categorias) {
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
	public List<Zona> getZonas() {
		return zonas;
	}
	/**
	 * Modifica las zonas de preferencia a aquella lista dada por parámetro.<br>
	 * @param zonas
	 */
	public void setZonas(List<Zona> zonas) {
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
	/**
	 * Agrega una categoría a la lista respectiva.<br>
	 * @param c Categoría nueva
	 */
	public void agregarCategoria(Categoria c)
	{
		categorias.add(c);
	}
	/**
	 * Remueve una categoría de lalista respectiva.<br>
	 * @param c Categoría a eliminar.
	 */
	public void removerCategoria(Categoria c)
	{
		categorias.remove(c);
	}
	/**
	 * Agrega una zona a la lista respectiva.<br>
	 * @param z Zona a agregar.
	 */
	public void agregarZona(Zona z)
	{
		zonas.add(z);
	}
	/**
	 * Remueve una zona de la lista respectiva.<br>
	 * @param z Zona a remover.
	 */
	public void removerZona(Zona z)
	{
		zonas.remove(z);
	}
	
	
	
	
}
