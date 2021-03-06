package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Pedido que ha hecho un cliente de un men� a un restaurante. Representaci�n Detail.
 * @author JuanSebastian
 */
public class PedidoMenu {

	/**
	 * Cantidad de unidades del menu que se van a pedir
	 */
	@JsonProperty(value="cantidad")
	private Integer cantidad;

	/**
	 * Cuenta que est� ordenando el menu
	 */
	@JsonProperty(value="cuenta")
	private CuentaMinimum cuenta;
	
	/**
	 * Menu que se est� ordenando
	 */
	@JsonProperty(value="menu")
	private MenuMinimum menu;
	
	/**
	 * Determina si el pedido ya fue entregado.
	 */
	@JsonProperty(value="entregado")
	private Boolean entregado;
	
	public PedidoMenu(@JsonProperty(value="cantidad") Integer cantidad, @JsonProperty(value="cuenta") CuentaMinimum cuenta,
			@JsonProperty(value="menu") MenuMinimum menu, @JsonProperty(value="entregado") Boolean entregado) {
		this.cantidad = cantidad;
		this.cuenta = cuenta;
		this.menu = menu;
		this.entregado = entregado;
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

	public Boolean getEntregado() {
		return entregado;
	}

	public void setEntregado(Boolean entregado) {
		this.entregado = entregado;
	}
	
}
