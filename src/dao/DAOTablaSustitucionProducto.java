package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Producto;
import vos.PedidoMenu;
import vos.SustitucionProducto;

public class DAOTablaSustitucionProducto {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOSustitucionProducto
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaSustitucionProducto() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos los sustitucionProductos de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM SUSTITUCION_PRODUCTOS;
	 * @return Arraylist con los sustitucionProductos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<SustitucionProducto> darSustitucionProductos() throws SQLException, Exception {

		String sql = "SELECT * FROM SUSTITUCION_PRODUCTO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadSustitucionProducto(rs);
	}

	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los sustitucionProductos de la base de datos para un pedido particular.
	 * @param cuenta nombre del PedidoMenu.
	 * @return Arraylist con los sustitucionProductos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<SustitucionProducto> darSustitucionProductosPorPedidoMenu(String cuenta, String menu, String restaurante) throws SQLException, Exception {

		String sql = "SELECT * FROM SUSTITUCION_PRODUCTO WHERE NUMERO_CUENTA LIKE '" + cuenta + "'";
		sql += " AND NOMBRE_MENU LIKE '" + menu + "' AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadSustitucionProducto(rs);
	}

	/**
	 * Metodo que busca la informacion de un producto en una cienta dados por par�metro.
	 * @param id - Id del producto a buscar
	 * @param pedido - Nombre del pedido al que pertenece
	 * @return ArrayList con los sustitucionProductos encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public SustitucionProducto buscarSustitucionProductosPorNombreYPedidoMenu(String cuenta, String menu, String restaurante, Long original, Long sustituto) throws SQLException, Exception {

		String sql = "SELECT * FROM SUSTITUCION_PRODUCTO WHERE NUMERO_CUENTA LIKE '" + cuenta + "'";
		sql += " AND NOMBRE_MENU LIKE '" + menu + "' AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";
		sql += "AND ID_ORIGINAL = " + original + " AND ID_SUSTITUTO = " + sustituto;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<SustitucionProducto> sustitucionProductos = convertirEntidadSustitucionProducto(rs);
		return sustitucionProductos.get(0);
	}

	/**
	 * Metodo que agrega la sustitucionProducto que entra como parametro a la base de datos.
	 * @param sustitucionProducto - la sustitucionProducto a agregar. sustitucionProducto !=  null
	 * <b> post: </b> se ha agregado la sustitucionProducto a la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que la sustitucionProducto baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la sustitucionProducto a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addSustitucionProducto(SustitucionProducto sustitucionProducto, PedidoMenu pedido) throws SQLException, Exception {

		String sql = "INSERT INTO SUSTITUCION_PRODUCTO VALUES (";
		sql += sustitucionProducto.getOriginal().getId() + ", ";
		sql += sustitucionProducto.getSustituto().getId() + ", ";
		sql += "'" + pedido.getCuenta().getNumeroCuenta() + "', ";
		sql += "'" + pedido.getMenu().getNombre() + "', ";
		sql += "'" + pedido.getMenu().getRestaurante().getNombre() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina la sustitucionProducto que entra como parametro en la base de datos.
	 * @param sustitucionProducto - la sustitucionProducto a borrar. sustitucionProducto !=  null
	 * <b> post: </b> se ha borrado la sustitucionProducto en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la sustitucionProducto.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteSustitucionProducto(SustitucionProducto sustitucionProducto, PedidoMenu pedido) throws SQLException, Exception {

		String sql = "DELETE FROM SUSTITUCION_PRODUCTO";
		sql += " WHERE ID_ORIGINAL = " + sustitucionProducto.getOriginal().getId();
		sql += " AND ID_SUSTITUTO = " + sustitucionProducto.getSustituto().getId();
		sql += " AND NOMBRE_MENU LIKE '" + pedido.getMenu().getNombre() + "'";
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + pedido.getMenu().getRestaurante().getNombre() + "'";
		sql += " AND NUMERO_CUENTA LIKE '" + pedido.getCuenta().getNumeroCuenta() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Crea un arreglo de sustitucionProductos con el set de resultados pasado por par�metro.<br>
	 * @param rs Set de resultados.<br>
	 * @return sustitucionProductos Lista de sustitucionProductos convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	private List<SustitucionProducto> convertirEntidadSustitucionProducto(ResultSet rs) throws SQLException, Exception
	{
		DAOTablaProducto daoProducto = new DAOTablaProducto();
		daoProducto.setConn(conn);
		List<SustitucionProducto> sustitucionProductos = new ArrayList<>();
		while (rs.next()) {
			Producto original = daoProducto.buscarProductoPorId(rs.getLong("ID_ORIGNAL"));
			Producto sustituto = daoProducto.buscarProductoPorId(rs.getLong("ID_SUSTITUTO"));
			sustitucionProductos.add(new SustitucionProducto(original, sustituto));
		}
		daoProducto.cerrarRecursos();
		return sustitucionProductos;
	}

	/**
	 * Elimina todos los sustitucionProductos pertenecientes al pedido dado.
	 * @param pedido PedidoMenu al cual eliminarle los sustitucionProductos.
	 * @throws SQLException Alg�n problema de la base de datos.<br>
	 */
	public void eliminarSustitucionProductosPorPedidoMenu(PedidoMenu pedido) throws SQLException {
		String sql = "DELETE FROM SUSTITUCION_PRODUCTO WHERE NUMERO_CUENTA LIKE '" + pedido.getCuenta().getNumeroCuenta() + "'";
		sql += " AND NOMBRE_MENU LIKE '" + pedido.getMenu().getNombre() + "' AND NOMBRE_RESTAURANTE LIKE '" + pedido.getMenu().getRestaurante().getNombre() + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}



}
