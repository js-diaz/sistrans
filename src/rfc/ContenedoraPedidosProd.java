package rfc;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

import vos.PedidoProd;

/**
 * Clase creada para contener pedidos de producto por menú.<br>
 * @author jc161
 *
 */
public class ContenedoraPedidosProd {
	/**
	 * Nombre del menú
	 */
	@JsonProperty(value="nombreMenu")
	private String nombreMenu;
	/**
	 * Listado de pedidos
	 */
	@JsonProperty(value="pedidosProd")
	private ArrayList<PedidoProd> pedidosProd;
	
	
	/**
	 * Crea una nueva contenedora.<br>
	 * @param nombreMenu Nombre del menú.<br>
	 * @param pedidosProd Pedidso de producto.
	 */
	public ContenedoraPedidosProd(@JsonProperty("nombreMenu")String nombreMenu, 
			@JsonProperty(value="pedidosProd")ArrayList<PedidoProd> pedidosProd) {
		super();
		this.nombreMenu = nombreMenu;
		this.pedidosProd = pedidosProd;
	}
	/**
	 * Obtiene el nombre del menu.<br>
	 * @return nombreMenu
	 */
	public String getNombreMenu() {
		return nombreMenu;
	}
	/**
	 * Modifica el nombre del menú al dado por parámetro.<br>
	 * @param nombreMenu
	 */
	public void setNombreMenu(String nombreMenu) {
		this.nombreMenu = nombreMenu;
	}
	/**
	 * Obtiene el listado de pedidos de producto.<br>
	 * @return pedidosProd
	 */
	public ArrayList<PedidoProd> getPedidosProd() {
		return pedidosProd;
	}
	/**
	 * Modifica el valor de pedidos de producto al dado por parámetro.<br>
	 * @param pedidosProd
	 */
	public void setPedidosProd(ArrayList<PedidoProd> pedidosProd) {
		this.pedidosProd = pedidosProd;
	}
	
	
}
