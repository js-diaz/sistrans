package vos;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que modela la cuenta de una persona.
 * @author jc161
 *
 */
public class Cuenta extends CuentaMinimum {
	/**
	 * Lista de pedidos de productos
	 */
	@JsonProperty(value="pedidoProd")
	private List<PedidoProd> pedidoProd;
	/**
	 * Lista de pedido de menús
	 */
	@JsonProperty (value="pedidoMenu")
	private List<PedidoMenu> pedidoMenu;
	
	/**
	 * Cliente al que hace referencia la cuenta
	 */
	@JsonProperty (value="cliente")
	private UsuarioMinimum cliente;
	/**
	 * Mesa a la que pertenece la cuenta dentro de la rotonda si las transacciones son locales.<br>
	 */
	@JsonProperty(value="mesa")
	private MesaMinimum mesa;
	/**
	 * Dirección de la cuenta si la transacción es distribuida
	 * 
	 */
	@JsonProperty(value="direccion")
	private String direccion;
	
	/**
	 * Constructor de la cuenta.<br>
	 * @param pedidoProd Lista de pedidos de productos.<br>
	 * @param pedidoMenu Lista de pedidos de menús.<br>
	 * @param valor Valor de la cuenta.<br>
	 * @param numeroCuenta Número de la cuenta. <br>
	 * @param fecha Fecha de la cuenta.<br>
	 * @param cliente Cliente al que se le factura.
	 */
	public Cuenta(@JsonProperty(value="pedidoProd")List<PedidoProd> pedidoProd, @JsonProperty(value="pedidoMenu")List<PedidoMenu> pedidoMenu, @JsonProperty(value="valor")double valor, @JsonProperty(value="numeroCuenta")String numeroCuenta,
			@JsonProperty(value="fecha")Date fecha, @JsonProperty(value="cliente")UsuarioMinimum cliente, @JsonProperty(value="mesa") MesaMinimum mesa, @JsonProperty(value="pagada") Boolean pagada,@JsonProperty(value="direccion") String direccion) {
		super(valor,numeroCuenta,fecha,pagada);
		this.pedidoProd = pedidoProd;
		this.pedidoMenu = pedidoMenu;
		this.mesa=mesa;
		this.cliente = cliente;
		this.direccion=direccion;
	}
	/**
	 * Obtiene el listado de pedidos de productos.<br>
	 * @return pedidoProd.
	 */
	public List<PedidoProd> getPedidoProd() {
		return pedidoProd;
	}
	/**
	 * Modifica el pedido del producto al dado por parámetro.<br>
	 * @param pedidoProd Modifica el listado respectivo.
	 */
	public void setPedidoProd(List<PedidoProd> pedidoProd) {
		this.pedidoProd = pedidoProd;
	}
	/**
	 * Obtiene el listado de pedidos del menú.<br>
	 * @return pedidoMenu
	 */
	public List<PedidoMenu> getPedidoMenu() {
		return pedidoMenu;
	}
	/**
	 * Modifica el listado de pedidos de menú al dado por parámetro.<br>
	 * @param pedidoMenu 
	 */
	public void setPedidoMenu(List<PedidoMenu> pedidoMenu) {
		this.pedidoMenu = pedidoMenu;
	}
	
	/**
	 * Obtiene el cliente de la cuenta.<br>
	 * @return cliente
	 */
	public UsuarioMinimum getCliente()
	{
		return cliente;
	}
	/**
	 * Modifica el cliente al dado por parámetro.<br>
	 * @param c
	 */
	public void setCliente (UsuarioMinimum c)
	{
		cliente=c;
	}
	/**
	 * Obtiene la mesa de la cuenta.<br>
	 * @return mesa
	 */
	public MesaMinimum getMesa()
	{
		return mesa;
	}
	/**
	 * Modifica la mesa al valor dado por parámetro.<br>
	 * @param mesa
	 */
	public void setMesa(MesaMinimum mesa)
	{
		this.mesa=mesa;
	}
	/**
	 * Retorna el valor de la dirección de la cuenta.<br>
	 * @return direccion
	 */
	public String getDireccion()
	{
		return direccion;
	}
	/**
	 * Modifica la dirección al valor dado por parámetro-<br>
	 * @param direccion
	 */
	public void setDireccion(String direccion)
	{
		this.direccion=direccion;
	}
	
}
