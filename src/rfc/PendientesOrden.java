package rfc;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

import vos.PedidoProd;

/**
 * Clase que representa los elementos pendientes de una orden
 * @author jc161
 *
 */
public class PendientesOrden {
	/**
	 * Productos pendientes
	 */
	@JsonProperty(value="productos")
	private ArrayList<PedidoProd> productos;
	/**
	 * Menús pendientes
	 */
	@JsonProperty(value="menus")
	private ArrayList<ContenedoraPedidosProd> menus;
	/**
	 * Construye una nueva orden pendiente.<br>
	 * @param productos Listado de productos pendientes.<br>
	 * @param menus Listado de menus pendientes
	 */
	public PendientesOrden(@JsonProperty(value="productos")ArrayList<PedidoProd> productos, 
			@JsonProperty(value="menus")ArrayList<ContenedoraPedidosProd> menus) {
		super();
		this.productos = productos;
		this.menus = menus;
	}
	/**
	 * Obtiene ellistado de productos pendientes.<br>
	 * @return productos
	 */
	public ArrayList<PedidoProd> getProductos() {
		return productos;
	}
	/**
	 * Modifica el valor de productos al dado por parámetro.<br>
	 * @param productos
	 */
	public void setProductos(ArrayList<PedidoProd> productos) {
		this.productos = productos;
	}
	/**
	 * Obtiene los menús pendientes.<br>
	 * @return menus
	 */
	public ArrayList<ContenedoraPedidosProd> getMenus() {
		return menus;
	}
	/**
	 * Modifica el valor de los menús al dado por parámetro.<br>
	 * @param menus
	 */
	public void setMenus(ArrayList<ContenedoraPedidosProd> menus) {
		this.menus = menus;
	}
	
	
}
