package vos;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import rfc.ProductoEnTotal;

/**
 * Clase para embasar los productos de una categoría en el req de consulta 5
 * @author jc161
 *
 */
public class CategoriaProducto {

	/**
	 * Nombre de la categoría
	 */
	@JsonProperty(value="nombreCategoria")
	private String nombreCategoria;
	/**
	 * Listado de productos con valores totales.
	 */
	@JsonProperty(value="productos")
	private List<ProductoEnTotal> productos;
	
	/**
	 * Construye una nueva contenedora categoría producto.<br>
	 * @param nombreCategoria Nombre de la categoría.<br>
	 * @param productos Listado de productos con valores totales.
	 */
	public CategoriaProducto(@JsonProperty(value="nombreCategoria")String nombreCategoria,
			@JsonProperty(value="productos")List<ProductoEnTotal> productos) {
		super();
		this.nombreCategoria = nombreCategoria;
		this.productos = productos;
	}
	/**
	 * Inicializa una categoría producto vacía.
	 */
	public CategoriaProducto() {
		productos= new ArrayList<>();
	}
	/**
	 * Obtiene el nombre de la categoría.<br>
	 * @return nombreCategoria
	 */
	public String getNombreCategoria() {
		return nombreCategoria;
	}
	/**
	 * Modifica la categoría al valor dado por parámetro.<br>
	 * @param nombreCategoria 
	 */
	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}
	/**
	 * Obtiene el listado de productos.<br>
	 * @return productos
	 */
	public List<ProductoEnTotal> getProductos() {
		return productos;
	}
	/**
	 * Modifica los productos al valor dado por parámetro.<br>
	 * @param productos 
	 */
	public void setProductos(List<ProductoEnTotal> productos) {
		this.productos = productos;
	}
	
	
	
}
