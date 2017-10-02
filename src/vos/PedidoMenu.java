package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Pedido que ha hecho un cliente de un menú a un restaurante. Representación Detail.
 * @author JuanSebastian
 */
public class PedidoMenu {

	/**
	 * Cantidad de unidades del menu que se van a pedir
	 */
	@JsonProperty(value="cantidad")
	private Integer cantidad;

	/**
	 * Cuenta que está ordenando el menu
	 */
	@JsonProperty(value="cuenta")
	private CuentaMinimum cuenta;
	
	/**
	 * Menu que se está ordenando
	 */
	@JsonProperty(value="menu")
	private MenuMinimum menu;

	public PedidoMenu(@JsonProperty(value="cantidad") Integer cantidad, @JsonProperty(value="cuenta") CuentaMinimum cuenta,
			@JsonProperty(value="menu") MenuMinimum menu) {
		this.cantidad = cantidad;
		this.cuenta = cuenta;
		this.menu = menu;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public CuentaMinimum getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaMinimum cuenta) {
		this.cuenta = cuenta;
	}

	public MenuMinimum getMenu() {
		return menu;
	}

	public void setMenu(MenuMinimum menu) {
		this.menu = menu;
	}
	
}
