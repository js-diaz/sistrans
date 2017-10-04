package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.CuentaMinimum;
import vos.PedidoMenu;
import vos.Menu;
import vos.MenuMinimum;

public class DAOTablaPedidoMenu {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOPedidoMenu
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaPedidoMenu() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos los pedidoMenus de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM PEDIDO_MENUS;
	 * @return Arraylist con los pedidoMenus de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<PedidoMenu> darPedidoMenus() throws SQLException, Exception {

		String sql = "SELECT * FROM PEDIDO_MENU";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadPedidoMenu(rs);
	}
	
	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los pedidoMenus de la base de datos para un cuenta particular.
	 * @param cuenta nombre del Cuenta.
	 * @return Arraylist con los pedidoMenus de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<PedidoMenu> darPedidoMenusPorCuenta(String cuenta) throws SQLException, Exception {

		String sql = "SELECT * FROM PEDIDO_MENU WHERE NUMERO_CUENTA LIKE '" + cuenta + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadPedidoMenu(rs);
	}

	/**
	 * Metodo que busca la informacion de un menu en una cienta dados por par�metro.
	 * @param id - Id del menu a buscar
	 * @param cuenta - Nombre del cuenta al que pertenece
	 * @return ArrayList con los pedidoMenus encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public PedidoMenu buscarPedidoMenusPorNombreYCuenta(String menu, String restaurante, String cuenta) throws SQLException, Exception {

		String sql = "SELECT * FROM PEDIDO_MENU WHERE NOMBRE_MENU LIKE '" + menu + "' AND NUMERO_CUENTA LIKE '" + cuenta + "'";
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<PedidoMenu> pedidoMenus = convertirEntidadPedidoMenu(rs);
		return pedidoMenus.get(0);
	}

	/**
	 * Metodo que agrega la pedidoMenu que entra como parametro a la base de datos.
	 * @param pedidoMenu - la pedidoMenu a agregar. pedidoMenu !=  null
	 * <b> post: </b> se ha agregado la pedidoMenu a la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que la pedidoMenu baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la pedidoMenu a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addPedidoMenu(PedidoMenu pedidoMenu) throws SQLException, Exception {

		String sql = "INSERT INTO PEDIDO_MENU VALUES (";
		sql += "'" + pedidoMenu.getCuenta().getNumeroCuenta() + "', ";
		sql += pedidoMenu.getMenu().getNombre() + ", ";
		sql += "'" + pedidoMenu.getMenu().getRestaurante().getNombre() + "', ";
		sql += pedidoMenu.getCantidad() + ", ";
		sql += pedidoMenu.getEntregado()? "'0')" : "'1')";
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	

	/**
	 * Metodo que actualiza la pedidoMenu que entra como parámetro en la base de datos.
	 * @param pedidoMenu - la pedidoMenu a actualizar. pedidoMenu !=  null
	 * <b> post: </b> se ha actualizado la pedidoMenu en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la pedidoMenu.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updatePedidoMenu(PedidoMenu pedidoMenu) throws SQLException, Exception {

		String sql = "UPDATE PEDIDO_MENU SET ";
		sql += "CANTIDAD = " + pedidoMenu.getCantidad();
		sql += "ENTREGADO = " + (pedidoMenu.getEntregado()? "'0' " : "'1' ");

		
		sql += " WHERE NOMBRE_MENU LIKE '" + pedidoMenu.getMenu().getNombre() + "'"; 
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + pedidoMenu.getMenu().getRestaurante().getNombre() + "'";
		sql += " AND NUMERO_CUENTA LIKE '" + pedidoMenu.getCuenta().getNumeroCuenta() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina la pedidoMenu que entra como parametro en la base de datos.
	 * @param pedidoMenu - la pedidoMenu a borrar. pedidoMenu !=  null
	 * <b> post: </b> se ha borrado la pedidoMenu en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la pedidoMenu.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deletePedidoMenu(PedidoMenu pedidoMenu) throws SQLException, Exception {
		
		String sql = "DELETE FROM PEDIDO_MENU";
		sql += " WHERE NOMBRE_MENU LIKE '" + pedidoMenu.getMenu().getNombre() + "'"; 
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + pedidoMenu.getMenu().getRestaurante().getNombre() + "'";
		sql += " AND NUMERO_CUENTA LIKE '" + pedidoMenu.getCuenta().getNumeroCuenta() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	/**
	 * Crea un arreglo de pedidoMenus con el set de resultados pasado por par�metro.<br>
	 * @param rs Set de resultados.<br>
	 * @return pedidoMenus Lista de pedidoMenus convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	private List<PedidoMenu> convertirEntidadPedidoMenu(ResultSet rs) throws SQLException, Exception
	{
		DAOTablaCuenta daoCuenta = new DAOTablaCuenta();
		DAOTablaMenu daoMenu = new DAOTablaMenu();
		daoCuenta.setConn(conn);
		daoMenu.setConn(conn);
		List<PedidoMenu> pedidoMenus = new ArrayList<>();
		while (rs.next()) {
			int cantidad = rs.getInt("CANTIDAD");
			MenuMinimum menu = daoMenu.buscarMenusMinimumPorNombreYRestaurante(rs.getString("NOMBRE_MENU"), rs.getString("NOMBRE_RESTAURANTE"));
			CuentaMinimum cuenta = daoCuenta.buscarCuentasMinimumPorNumeroDeCuenta(rs.getString("NUMERO_CUENTA"));
			boolean entregado = rs.getString("ENTREGADO").equals("1");
			pedidoMenus.add(new PedidoMenu(cantidad, cuenta, menu, entregado));
		}
		daoCuenta.cerrarRecursos();
		daoMenu.cerrarRecursos();
		return pedidoMenus;
	}
	
	/**
	 * Elimina todos los pedidoMenus pertenecientes al cuenta dado.
	 * @param cuenta Cuenta al cual eliminarle los pedidoMenus.
	 * @throws SQLException Alg�n problema de la base de datos.<br>
	 */
	public void eliminarPedidoMenusPorCuenta(CuentaMinimum cuenta) throws SQLException {
		String sql = "DELETE FROM PEDIDO_MENU WHERE NUMERO_CUENTA LIKE '" + cuenta.getNumeroCuenta() + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}

	/**
	 * Metodo que da todos los menus de una cuenta dada
	 * @param numeroCuenta numero de la cuenta
	 * @return Lista con los pedidos de esta cuenta
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	public List<PedidoMenu> buscarMenusPorNumCuenta(String numeroCuenta) throws SQLException, Exception {
		String sql = "SELECT * FROM PEDIDO_MENU WHERE NUMERO_CUENTA LIKE '" + numeroCuenta + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		return convertirEntidadPedidoMenu(ps.executeQuery());
	}


}
