package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Cuenta;
import vos.CuentaMinimum;
import vos.InfoProdRest;
import vos.PedidoProd;

public class DAOTablaPedidoProducto {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOPedidoProd
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaPedidoProducto() {
		recursos = new ArrayList<Object>();
	}

	/**f
	 * Metodo que cierra todos los recursos que estan enel arreglo de recursos
	 * <b>post: </b> Todos los recurso del arreglo de recursos han sido cerrados
	 */
	public void cerrarRecursos() {
		for(Object ob : recursos){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	/**
	 * Metodo que inicializa la connection del DAO a la base de datos con la conexión que entra como parametro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection conn){
		this.conn = conn;
	}


	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los pedidoProds de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM PEDIDO_PRODS;
	 * @return Arraylist con los pedidoProds de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<PedidoProd> darPedidoProds() throws SQLException, Exception {

		String sql = "SELECT * FROM PEDIDO_PROD";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadPedidoProd(rs);
	}
	
	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los pedidoProds de la base de datos para un cuenta particular.
	 * @param cuenta nombre del Cuenta.
	 * @return Arraylist con los pedidoProds de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<PedidoProd> darPedidoProdsPorCuenta(String cuenta) throws SQLException, Exception {

		String sql = "SELECT * FROM PEDIDO_PROD WHERE NUMERO_CUENTA LIKE '" + cuenta + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadPedidoProd(rs);
	}

	/**
	 * Metodo que busca la informacion de un producto en una cienta dados por par�metro.
	 * @param id - Id del producto a buscar
	 * @param cuenta - Nombre del cuenta al que pertenece
	 * @return ArrayList con los pedidoProds encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public PedidoProd buscarPedidoProdsPorIdYCuenta(Long id, String restaurante, String cuenta) throws SQLException, Exception {

		String sql = "SELECT * FROM PEDIDO_PROD WHERE ID_PRODUCTO = " + id + " AND NUMERO_CUENTA LIKE '" + cuenta + "'";
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<PedidoProd> pedidoProds = convertirEntidadPedidoProd(rs);
		return pedidoProds.get(0);
	}

	/**
	 * Metodo que agrega la pedidoProd que entra como parametro a la base de datos.
	 * @param pedidoProd - la pedidoProd a agregar. pedidoProd !=  null
	 * <b> post: </b> se ha agregado la pedidoProd a la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que la pedidoProd baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la pedidoProd a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addPedidoProd(PedidoProd pedidoProd) throws SQLException, Exception {

		verificarDisponibilidad(pedidoProd.getPlato(),pedidoProd.getCantidad());
		String sql = "INSERT INTO PEDIDO_PROD VALUES (";
		sql += "'" + pedidoProd.getCuenta().getNumeroCuenta() + "', ";
		sql += pedidoProd.getPlato().getProducto().getId() + ", ";
		sql += "'" + pedidoProd.getPlato().getRestaurante().getNombre() + "', ";
		sql += pedidoProd.getCantidad() + ", ";
		sql += pedidoProd.getEntregado()? "'1')" : "'0')";
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		modificarPrecioCuenta(pedidoProd.getCuenta(),pedidoProd.getCantidad()*pedidoProd.getPlato().getCosto());
	}
	
	/**
	 * Modifica el precio de la cuenta.<br>
	 * @param cuenta Cuenta Minimum.<br>
	 * @param d Precio a agregar.<br>
	 * @throws SQLException Si hay errores en la BD.<br>
	 * @throws Exception Si hay errores.
	 */
	public void modificarPrecioCuenta(CuentaMinimum cuenta, double d) throws SQLException, Exception {
		DAOTablaCuenta dao = new DAOTablaCuenta();
		dao.setConn(conn);
		Cuenta c= dao.buscarCuentasPorNumeroDeCuenta(cuenta.getNumeroCuenta());
		c.setValor(c.getValor()+d);
		dao.updateCuenta(c);
		dao.cerrarRecursos();
	}

	/**
	 * Verifica la disponibilidad del producto antes de agregarlo.<br>
	 * @param plato Plato del restaurante.<br>
	 * @param cantidad Cantidad.
	 */
	public void verificarDisponibilidad(InfoProdRest plato, Integer cantidad) throws SQLException, Exception {
		DAOTablaInfoProdRest info = new DAOTablaInfoProdRest();
		info.setConn(conn);
		InfoProdRest informacion =info.buscarInfoProdRestsPorIdYRestaurante(plato.getProducto().getId(), plato.getRestaurante().getNombre());
		if(informacion.getDisponibilidad()<cantidad) throw new Exception("No se puede agregar al no haber disponibilidad de productos");
		info.cerrarRecursos();
	}

	/**
	 * Metodo que actualiza la pedidoProd que entra como parámetro en la base de datos.
	 * @param pedidoProd - la pedidoProd a actualizar. pedidoProd !=  null
	 * <b> post: </b> se ha actualizado la pedidoProd en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la pedidoProd.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updatePedidoProd(PedidoProd pedidoProd) throws SQLException, Exception {

		boolean mod=false;
		if(pedidoProd.getCuenta().getPagada()) throw new Exception("Su cuenta ya está pagada");
		if(pedidoProd.getEntregado()==true)
			{
				mod=true;
				pagarProducto(pedidoProd);
			}
		PedidoProd p=buscarPedidoProdsPorIdYCuenta(pedidoProd.getPlato().getProducto().getId(), pedidoProd.getPlato().getRestaurante().getNombre(), pedidoProd.getCuenta().getNumeroCuenta());
		if(!mod)modificarPrecioCuenta(p.getCuenta(),p.getCantidad()*p.getPlato().getPrecio()*-1);
		
				String sql = "UPDATE PEDIDO_PROD SET ";
				sql += "CANTIDAD = " + pedidoProd.getCantidad();
				sql += ", ENTREGADO = " + (pedidoProd.getEntregado()? "'1'" : "'0'");
				
				sql += " WHERE ID_PRODUCTO = " + pedidoProd.getPlato().getProducto().getId(); 
				sql += " AND NOMBRE_RESTAURANTE LIKE '" + pedidoProd.getPlato().getRestaurante().getNombre() + "'";
				sql += " AND NUMERO_CUENTA LIKE '" + pedidoProd.getCuenta().getNumeroCuenta() + "'";

				PreparedStatement prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				prepStmt.executeQuery();
		if(!mod)modificarPrecioCuenta(pedidoProd.getCuenta(),pedidoProd.getCantidad()*pedidoProd.getPlato().getPrecio()*-1);
		if(mod)
		{
			DAOTablaCuenta c= new DAOTablaCuenta();
			c.setConn(conn);
			c.verificarPagado(pedidoProd.getCuenta().getNumeroCuenta());
			c.cerrarRecursos();
		}
	}
	/**
	 * Paga el pedido de producto dado por parámetro.<br>
	 * @param p PedidoProducto<br>
	 * @throws SQLException Si hay errores en la BD.<br>
	 * @throws Exception Si hay errores.
	 */
	private void pagarProducto(PedidoProd p) throws SQLException, Exception{
		DAOTablaInfoProdRest productos= new DAOTablaInfoProdRest();
		productos.setConn(conn);
		InfoProdRest info=null;
		info=p.getPlato();
		info.setDisponibilidad(info.getDisponibilidad()-p.getCantidad());
		productos.updateInfoProdRest(info);
		productos.cerrarRecursos();
	}

	/**
	 * Metodo que elimina la pedidoProd que entra como parametro en la base de datos.
	 * @param pedidoProd - la pedidoProd a borrar. pedidoProd !=  null
	 * <b> post: </b> se ha borrado la pedidoProd en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la pedidoProd.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deletePedidoProd(PedidoProd pedidoProd) throws SQLException, Exception {
		
		String sql = "DELETE FROM PEDIDO_PROD";
		sql += " WHERE ID_PRODUCTO = " + pedidoProd.getPlato().getProducto().getId(); 
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + pedidoProd.getPlato().getRestaurante().getNombre() + "'";
		sql += " AND NUMERO_CUENTA LIKE '" + pedidoProd.getCuenta().getNumeroCuenta() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		modificarPrecioCuenta(pedidoProd.getCuenta(),pedidoProd.getCantidad()*pedidoProd.getPlato().getCosto()*-1);
	}
	
	/**
	 * Crea un arreglo de pedidoProds con el set de resultados pasado por par�metro.<br>
	 * @param rs Set de resultados.<br>
	 * @return pedidoProds Lista de pedidoProds convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	private List<PedidoProd> convertirEntidadPedidoProd(ResultSet rs) throws SQLException, Exception
	{
		DAOTablaCuenta daoCuenta = new DAOTablaCuenta();
		DAOTablaInfoProdRest daoProd = new DAOTablaInfoProdRest();
		daoCuenta.setConn(conn);
		daoProd.setConn(conn);
		List<PedidoProd> pedidoProds = new ArrayList<>();
		while (rs.next()) {
			int cantidad = rs.getInt("CANTIDAD");
			InfoProdRest plato = daoProd.buscarInfoProdRestsPorIdYRestaurante(rs.getLong("ID_PRODUCTO"), rs.getString("NOMBRE_RESTAURANTE"));
			CuentaMinimum cuenta = daoCuenta.buscarCuentasMinimumPorNumeroDeCuenta(rs.getString("NUMERO_CUENTA"));
			boolean entregado = rs.getString("ENTREGADO").equals("1");
			pedidoProds.add(new PedidoProd(cantidad, cuenta, plato, entregado));
		}
		daoCuenta.cerrarRecursos();
		daoProd.cerrarRecursos();
		return pedidoProds;
	}
	
	/**
	 * Elimina todos los pedidoProds pertenecientes al cuenta dado.
	 * @param cuenta Cuenta al cual eliminarle los pedidoProds.
	 * @throws SQLException Alg�n problema de la base de datos.<br>
	 */
	public void eliminarPedidoProdsPorCuenta(CuentaMinimum cuenta) throws SQLException {
		String sql = "DELETE FROM PEDIDO_PROD WHERE NUMERO_CUENTA LIKE '" + cuenta.getNumeroCuenta() + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}

	/**
	 * Metodo que da todos los productos de una cuenta dada
	 * @param numeroCuenta numero de la cuenta
	 * @return Lista con los pedidos de esta cuenta
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	public List<PedidoProd> buscarProductosPorNumCuenta(String numeroCuenta) throws SQLException, Exception {
		String sql = "SELECT * FROM PEDIDO_PROD WHERE NUMERO_CUENTA LIKE '" + numeroCuenta + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		return convertirEntidadPedidoProd(ps.executeQuery());
	}
	/**
	 * Verifica si hay pedidos de producto pendientes para la cuenta.<br>
	 * @param numCuenta Número de la cuenta.<br>
	 * @return Si hay o no pedidos pendientes.<br>
	 * @throws SQLException Excepción de sql si hay erroes en la bd.<br>
	 * @throws Exception Si hay errores.
	 */
	public boolean hayPedidoProdPendientesPorCuenta(String numCuenta) throws SQLException, Exception{
		// TODO Auto-generated method stub
		String sql="SELECT * FROM PEDIDO_PROD WHERE NUMERO_CUENTA LIKE '"+numCuenta+"' AND ENTREGADO='0'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		while(rs.next())
		{
			if(rs.getString("ENTREGADO").equals("0")) return true;
		}
		return false;
	}	

}
