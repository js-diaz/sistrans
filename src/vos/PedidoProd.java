package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Pedido que ha hecho un cliente de un producto a un restaurante. Representación Detail.
 * @author JuanSebastian
 */
public class PedidoProd {
	
	/**
	 * Cantidad de unidades del producto que se van a pedir
	 */
	@JsonProperty(value="cantidad")
	private Integer cantidad;
	
	/**
	 * Cuenta que está ordenado el producto
	 */
	@JsonProperty(value="cuenta")
	private CuentaMinimum cuenta;
	
	/**
	 * Producto que se está ordenando
	 */
	@JsonProperty(value="plato")
	private InfoProdRest plato;

	public PedidoProd(@JsonProperty(value="cantidad") Integer cantidad, @JsonProperty(value="cuenta") CuentaMinimum cuenta,
			@JsonProperty(value="plato") InfoProdRest plato) {
		this.cantidad = cantidad;
		this.cuenta = cuenta;
		this.plato = plato;
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

	public InfoProdRest getPlato() {
		return plato;
	}

	public void setPlato(InfoProdRest plato) {
		this.plato = plato;
	}
	
	
}
