package rfc;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa una contenedora de menú y cliente.<br>
 * @author jc161
 *
 */
public class ContenedoraMenuCliente {

	/**
	 * Nombre del restaurante
	 */
	@JsonProperty(value="nombreRestaurante")
	private String nombreRestaurante;
	/**
	 * Nombre del menú
	 */
	@JsonProperty(value="nombreMenu")
	private String nombreMenu;
	/**
	 * Producto informativo
	 */
	@JsonProperty(value="producto")
	private List<ProductoInformativo> producto;
	
	/**
	 * Crea una nueva contenedora de menú cliente.<br>
	 * @param nombreMenu Nombre del menú.<br>
	 * @param nombreRestaurante Nombre del restaurante.<br>
	 * @param producto Producto informativo
	 */
	public ContenedoraMenuCliente(@JsonProperty(value="nombreRestaurante")String nombreRestaurante,
			@JsonProperty(value="nombreMenu")String nombreMenu,	@JsonProperty(value="producto")List<ProductoInformativo> producto) {
		super();
		this.nombreMenu = nombreMenu;
		this.nombreRestaurante = nombreRestaurante;
		this.producto = producto;
	}
	/**
	 * Obtiene el nombre del menú.<br>
	 * @return nombreMenu
	 */
	public String getNombreMenu() {
		return nombreMenu;
	}
	/**
	 * Modifica el nombre del menú al valor dado por parámetro.<br>
	 * @param nombreMenu
	 */
	public void setNombreMenu(String nombreMenu) {
		this.nombreMenu = nombreMenu;
	}
	/**
	 * Obtiene el restaurante.<br>
	 * @return nombreRestaurante
	 */
	public String getNombreRestaurante() {
		return nombreRestaurante;
	}
	/**
	 * Modifica el valor del restaurante al dado por parámetro.<br>
	 * @param nombreRestaurante
	 */
	public void setNombreRestaurante(String nombreRestaurante) {
		this.nombreRestaurante = nombreRestaurante;
	}
	/**
	 * Obtiene el producto en versión informativa.<br>
	 * @return producto
	 */
	public List<ProductoInformativo> getProducto() {
		return producto;
	}
	/**
	 * Modifica el valor del producto al dado por parámetro.<br>
	 * @param producto
	 */
	public void setProducto(List<ProductoInformativo> producto) {
		this.producto = producto;
	}

	
	

}
