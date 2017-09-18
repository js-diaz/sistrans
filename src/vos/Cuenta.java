package vos;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que modela la cuenta de una persona.
 * @author jc161
 *
 */
public class Cuenta {
	/**
	 * Lista de pedidos de productos
	 */
	@JsonProperty(value="pedidoProd")
	private List<PedidoProd> pedidoProd;
	/**
	 * Lista de pedido de menús
	 */
	@JsonProperty (value="PedidoMenu")
	private List<PedidoMenu> pedidoMenu;
	/**
	 * Valor total de la cuenta
	 */
	@JsonProperty(value="valor")
	private double valor;
	/**
	 * Número de la cuenta
	 */
	@JsonProperty (value="numeroCuenta")
	private String numeroCuenta;
	/**
	 * Fecha de creación
	 */
	@JsonProperty (value="fecha")
	private Date fecha;
	/**
	 * Cliente al que hace referencia la cuenta
	 */
	@JsonProperty (value="cliente")
	private Usuario cliente;
	
	
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
			@JsonProperty(value="fecha")Date fecha, @JsonProperty(value="cliente")Usuario cliente) {
		super();
		this.pedidoProd = pedidoProd;
		this.pedidoMenu = pedidoMenu;
		this.valor = valor;
		this.numeroCuenta = numeroCuenta;
		this.fecha = fecha;
		this.cliente = cliente;
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
	 * Obtiene el valor de la cuenta.<br>
	 * @return valor
	 */
	public double getValor() {
		return valor;
	}
	/**
	 * Modifica el valor de la cuenta al dado por parámetro.<br>
	 * @param valor
	 */
	public void setValor(double valor) {
		this.valor = valor;
	}
	/**
	 * Obtiene el número de cuenta.<br>
	 * @return numeroCuenta
	 */
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	/**
	 * Cambia el número de cuenta al dado por parámetro.<br>
	 * @param numeroCuenta
	 */
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	/**
	 * Obtiene la fecha de creación de la cuenta.<br>
	 * @return fecha
	 */
	public Date getFecha() {
		return fecha;
	}
	/**
	 * Cambia la fecha a la dada por parámetro.<br>
	 * @param fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	/**
	 * Obtiene el cliente de la cuenta.<br>
	 * @return cliente
	 */
	public Usuario getCliente()
	{
		return cliente;
	}
	/**
	 * Modifica el cliente al dado por parámetro.<br>
	 * @param c
	 */
	public void setCliente (Usuario c)
	{
		cliente=c;
	}
	/**
	 * Agrega un pedido de menú a la cuenta.<br>
	 * @param p Pedido de menú a agregar.
	 */
	public void agregarPedidoMenu(PedidoMenu p)
	{
		pedidoMenu.add(p);
		valor+=p.darValor();
	}
	/**
	 * Remover pedido de menú<br>
	 * @param p Pedido de menú a remover.
	 */
	public void removerPedidoMenu(PedidoMenu p)
	{
		valor-=p.darValor();
		pedidoMenu.remove(p);
	}
	/**
	 * Agrega un pedido de producto.<br>
	 * @param p Pedido de producto a añadir.
	 */
	public void agregarPedidoProducto(PedidoProd p)
	{
		pedidoProd.add(p);
		valor+=p.darValor();
	}
	/**
	 * Remueve un pedido de producto de la cuenta.<br>
	 * @param p Pedido de producto a remover.
	 */
	public void removerPedidoProducto(PedidoProd p)
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
