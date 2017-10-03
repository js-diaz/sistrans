package rfc;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase para el requerimiento 5 que contiene la zona
 * @author jc161
 *
 */
public class ContenedoraZonaCategoriaProducto {

	/**
	 * Nombre de la zona
	 */
	@JsonProperty(value="nombreZona")
	private String nombreZona;
	/**
	 * Listado de contenedoras categoría producto
	 */
	@JsonProperty(value="categoriaProductos")
	private List<CategoriaProducto> categoriaProductos;
	/**
	 * Construye una contenedora vacía.
	 */
	public ContenedoraZonaCategoriaProducto()
	{
		categoriaProductos= new ArrayList<>();
	}
	/**
	 * Construye una nueva contenedora de zona con la información dada.<br>
	 * @param nombreZona Nombre de la zona.<br>
	 * @param categoriaProductos Listado de categorías de productos.
	 */
	public ContenedoraZonaCategoriaProducto(@JsonProperty(value="nombreZona")String nombreZona, 
			@JsonProperty(value="categoriaProductos")List<CategoriaProducto> categoriaProductos) {
		super();
		this.nombreZona = nombreZona;
		this.categoriaProductos = categoriaProductos;
	}
	/**
	 * Retorna el nombre de la zona.<br>
	 * @return nombreZona
	 */
	public String getNombreZona() {
		return nombreZona;
	}
	/**
	 * Modifica el nombre de la zona al valor dado por parámetro.<br>
	 * @param nombreZona
	 */
	public void setNombreZona(String nombreZona) {
		this.nombreZona = nombreZona;
	}
	/**
	 * Obtiene un listado de objetos categoría producto.<r>
	 * @return categoriasProductos
	 */
	public List<CategoriaProducto> getCategoriaProductos() {
		return categoriaProductos;
	}
	/**
	 * Modifica el valor de categoriaProductos a aquel dado por parámetro.<br>
	 * @param categoriaProductos
	 */
	public void setCategoriaProductos(List<CategoriaProducto> categoriaProductos) {
		this.categoriaProductos = categoriaProductos;
	}
	
	
	
	
}
