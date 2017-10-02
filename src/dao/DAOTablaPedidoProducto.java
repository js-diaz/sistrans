package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.CuentaMinimum;
import vos.InfoProdRest;
import vos.PedidoProd;

public class DAOTablaPedidoProducto {

	/**
	 * Arraylits de recursos que se usan para la ejecuci贸n de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexi贸n a la base de datos
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
	 * Metodo que inicializa la connection del DAO a la base de datos con la conexi贸n que entra como parametro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection conn){
		this.conn = conn;
	}


	/**
	 * Metodo que, usando la conexi贸n a la base de datos, saca todos los pedidoProds de la base de datos
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
	 * Metodo que busca la informacion de un producto en una cienta dados por par醡etro.
	 * @param id - Id del producto a buscar
	 * @param cuenta - Nombre del cuenta al que pertenece
	 * @return ArrayList con los pedidoProds encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public PedidoProd buscarPedidoProdsPorNombreYCuenta(Long id, String restaurante, String cuenta) throws SQLException, Exception {

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

		String sql = "INSERT INTO PEDIDO_PROD VALUES (";
		sql += "'" + pedidoProd.getCuenta().getNumeroCuenta() + "', ";
		sql += pedidoProd.getPlato().getProducto().getId() + ", ";
		sql += "'" + pedidoProd.getPlato().getRestaurante().getNombre() + "', ";
		sql += pedidoProd.getCantidad() + ")";
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	

	/**
	 * Metodo que actualiza la pedidoProd que entra como par谩metro en la base de datos.
	 * @param pedidoProd - la pedidoProd a actualizar. pedidoProd !=  null
	 * <b> post: </b> se ha actualizado la pedidoProd en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la pedidoProd.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updatePedidoProd(PedidoProd pedidoProd) throws SQLException, Exception {

		String sql = "UPDATE PEDIDO_PROD SET ";
		sql += "CANTIDAD = " + pedidoProd.getCantidad();
		
		sql += " WHERE ID_PRODUCTO = " + pedidoProd.getPlato().getProducto().getId(); 
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + pedidoProd.getPlato().getRestaurante().getNombre() + "'";
		sql += " AND NUMERO_CUENTA LIKE '" + pedidoProd.getCuenta().getNumeroCuenta() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
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
	}
	
	/**
	 * Crea un arreglo de pedidoProds con el set de resultados pasado por par醡etro.<br>
	 * @param rs Set de resultados.<br>
	 * @return pedidoProds Lista de pedidoProds convertidas.<br>
	 * @throws SQLException Alg煤n problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepci贸n.
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
			InfoProdRest plato = daoProd.buscarInfoProdRestsPorNombreYRestaurante(rs.getLong("ID_PRODUCTO"), rs.getString("NOMBRE_RESTAURANTE"));
			CuentaMinimum cuenta = daoCuenta.buscarCuentasPorNumeroDeCuenta(rs.getString("NUMERO_CUENTA"));
			pedidoProds.add(new PedidoProd(cantidad, cuenta, plato));
		}
		daoCuenta.cerrarRecursos();
		daoProd.cerrarRecursos();
		return pedidoProds;
	}
	
	/**
	 * Elimina todos los pedidoProds pertenecientes al cuenta dado.
	 * @param cuenta Cuenta al cual eliminarle los pedidoProds.
	 * @throws SQLException Alg鷑 problema de la base de datos.<br>
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
	 * @throws SQLException Alg煤n problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepci贸n.
	 */
	public List<PedidoProd> buscarProductosPorNumCuenta(String numeroCuenta) throws SQLException, Exception {
		String sql = "SELECT * FROM PEDIDO_PROD WHERE NUMERO_CUENTA LIKE '" + numeroCuenta + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		return convertirEntidadPedidoProd(ps.executeQuery());
	}

}
