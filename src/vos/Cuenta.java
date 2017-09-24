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
	private List<PedidoProdMinimum> pedidoProd;
	/**
	 * Lista de pedido de menús
	 */
	@JsonProperty (value="pedidoMenu")
	private List<PedidoMenuMinimum> pedidoMenu;
	
	/**
	 * Cliente al que hace referencia la cuenta
	 */
	@JsonProperty (value="cliente")
	private UsuarioMinimum cliente;
	
	
	/**
	 * Constructor de la cuenta.<br>
	 * @param pedidoProd Lista de pedidos de productos.<br>
	 * @param pedidoMenu Lista de pedidos de menús.<br>
	 * @param valor Valor de la cuenta.<br>
	 * @param numeroCuenta Número de la cuenta. <br>
	 * @param fecha Fecha de la cuenta.<br>
	 * @param cliente Cliente al que se le factura.
	 */
	public Cuenta(@JsonProperty(value="pedidoProd")List<PedidoProdMinimum> pedidoProd, @JsonProperty(value="pedidoMenu")List<PedidoMenuMinimum> pedidoMenu, @JsonProperty(value="valor")double valor, @JsonProperty(value="numeroCuenta")String numeroCuenta,
			@JsonProperty(value="fecha")Date fecha, @JsonProperty(value="cliente")UsuarioMinimum cliente) {
		super(valor,numeroCuenta,fecha);
		this.pedidoProd = pedidoProd;
		this.pedidoMenu = pedidoMenu;
		
		this.cliente = cliente;
	}
	/**
	 * Obtiene el listado de pedidos de productos.<br>
	 * @return pedidoProd.
	 */
	public List<PedidoProdMinimum> getPedidoProdMinimum() {
		return pedidoProd;
	}
	/**
	 * Modifica el pedido del producto al dado por parámetro.<br>
	 * @param pedidoProd Modifica el listado respectivo.
	 */
	public void setPedidoProdMinimum(List<PedidoProdMinimum> pedidoProd) {
		this.pedidoProd = pedidoProd;
	}
	/**
	 * Obtiene el listado de pedidos del menú.<br>
	 * @return pedidoMenu
	 */
	public List<PedidoMenuMinimum> getPedidoMenuMinimum() {
		return pedidoMenu;
	}
	/**
	 * Modifica el listado de pedidos de menú al dado por parámetro.<br>
	 * @param pedidoMenu 
	 */
	public void setPedidoMenuMinimum(List<PedidoMenuMinimum> pedidoMenu) {
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
	 * Agrega un pedido de menú a la cuenta.<br>
	 * @param p Pedido de menú a agregar.
	 */
	public void agregarPedidoMenuMinimum(PedidoMenuMinimum p)
	{
		pedidoMenu.add(p);
		valor+=p.darValor();
	}
	/**
	 * Remover pedido de menú<br>
	 * @param p Pedido de menú a remover.
	 */
	public void removerPedidoMenuMinimum(PedidoMenuMinimum p)
	{
		valor-=p.darValor();
		pedidoMenu.remove(p);
	}
	/**
	 * Agrega un pedido de producto.<br>
	 * @param p Pedido de producto a añadir.
	 */
	public void agregarPedidoProd(PedidoProdMinimum p)
	{
		pedidoProd.add(p);
		valor+=p.darValor();
	}
	/**
	 * Remueve un pedido de producto de la cuenta.<br>
	 * @param p Pedido de producto a remover.
	 */
	public void removerPedidoProd(PedidoProdMinimum p)
	{
		valor-=p.darValor();
		pedidoProd.remove(p);
	}
	/**
	 * Actualiza un menú de la cuenta.<br>
	 * @param nombreMenu Nombre del menú.<br>
	 * @param menu Menú a actualizar.
	 */
	public void actualizarMenu(String nombreMenu, Menu menu)
	{
		//TODO
	}
	/**
	 * Actualiza un producto de la cuenta.<br>
	 * @param nombreProducto Nombre del producto.<br>
	 * producto Producto a actualizar.
	 */
	public void actualizarProducto(String nombreProducto, Producto producto)
	{
		//TODO
	}
	
	
	
}
