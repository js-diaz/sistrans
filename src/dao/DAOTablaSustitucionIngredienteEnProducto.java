package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Ingrediente;
import vos.PedidoMenu;
import vos.Producto;
import vos.SustitucionIngredienteEnProducto;

public class DAOTablaSustitucionIngredienteEnProducto {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOSustitucionIngredienteEnProducto
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaSustitucionIngredienteEnProducto() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos los sustitucionIngredientes de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM SUSN_INGREDIENTE_EN_PRODUCTOS;
	 * @return Arraylist con los sustitucionIngredientes de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<SustitucionIngredienteEnProducto> darSustitucionIngredienteEnProductos() throws SQLException, Exception {

		String sql = "SELECT * FROM SUSN_INGREDIENTE_EN_PRODUCTO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadSustitucionIngredienteEnProducto(rs);
	}

	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los sustitucionIngredientes de la base de datos para un pedido particular.
	 * @param cuenta nombre del PedidoMenu.
	 * @return Arraylist con los sustitucionIngredientes de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<SustitucionIngredienteEnProducto> darSustitucionIngredienteEnProductosPorPedidoMenu(String cuenta, String menu, String restaurante) throws SQLException, Exception {

		String sql = "SELECT * FROM SUSN_INGREDIENTE_EN_PRODUCTO WHERE NUMERO_CUENTA LIKE '" + cuenta + "'";
		sql += " AND NOMBRE_MENU LIKE '" + menu + "' AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadSustitucionIngredienteEnProducto(rs);
	}

	/**
	 * Metodo que busca la informacion de un ingrediente en una cienta dados por par�metro.
	 * @param id - Id del ingrediente a buscar
	 * @param pedido - Nombre del pedido al que pertenece
	 * @return ArrayList con los sustitucionIngredientes encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public SustitucionIngredienteEnProducto buscarSustitucionIngredienteEnProductosPorNombreYPedidoMenu(String cuenta, String menu, String restaurante, Long producto, Long original, Long sustituto) throws SQLException, Exception {

		String sql = "SELECT * FROM SUSN_INGREDIENTE_EN_PRODUCTO WHERE NUMERO_CUENTA LIKE '" + cuenta + "'";
		sql += " AND NOMBRE_MENU LIKE '" + menu + "' AND ID_PRODUCTO = " + producto + "' AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";
		sql += "AND ID_ORIGINAL = " + original + " AND ID_SUSTITUTO = " + sustituto;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<SustitucionIngredienteEnProducto> sustitucionIngredientes = convertirEntidadSustitucionIngredienteEnProducto(rs);
		return sustitucionIngredientes.get(0);
	}

	/**
	 * Metodo que agrega la sustitucionIngrediente que entra como parametro a la base de datos.
	 * @param sustitucionIngrediente - la sustitucionIngrediente a agregar. sustitucionIngrediente !=  null
	 * <b> post: </b> se ha agregado la sustitucionIngrediente a la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que la sustitucionIngrediente baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la sustitucionIngrediente a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addSustitucionIngredienteEnProducto(SustitucionIngredienteEnProducto sustitucionIngrediente, PedidoMenu pedido) throws SQLException, Exception {

		String sql = "INSERT INTO SUSN_INGREDIENTE_EN_PRODUCTO VALUES (";
		sql += sustitucionIngrediente.getOriginal().getId() + ", ";
		sql += sustitucionIngrediente.getSustituto().getId() + ", ";
		sql += sustitucionIngrediente.getProducto().getId() +", ";
		sql += "'" + pedido.getCuenta().getNumeroCuenta() + "', ";
		sql += "'" + pedido.getMenu().getNombre() + "', ";
		sql += "'" + pedido.getMenu().getRestaurante().getNombre() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina la sustitucionIngrediente que entra como parametro en la base de datos.
	 * @param sustitucionIngrediente - la sustitucionIngrediente a borrar. sustitucionIngrediente !=  null
	 * <b> post: </b> se ha borrado la sustitucionIngrediente en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la sustitucionIngrediente.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteSustitucionIngredienteEnProducto(SustitucionIngredienteEnProducto sustitucionIngrediente, PedidoMenu pedido) throws SQLException, Exception {

		String sql = "DELETE FROM SUSN_INGREDIENTE_EN_PRODUCTO";
		sql += " WHERE ID_ORIGINAL = " + sustitucionIngrediente.getOriginal().getId();
		sql += " AND ID_SUSTITUTO = " + sustitucionIngrediente.getSustituto().getId();
		sql += " AND ID_PRODUCTO = " + sustitucionIngrediente.getProducto().getId();
		sql += " AND NOMBRE_MENU LIKE '" + pedido.getMenu().getNombre() + "'";
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + pedido.getMenu().getRestaurante().getNombre() + "'";
		sql += " AND NUMERO_CUENTA LIKE '" + pedido.getCuenta().getNumeroCuenta() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Crea un arreglo de sustitucionIngredientes con el set de resultados pasado por par�metro.<br>
	 * @param rs Set de resultados.<br>
	 * @return sustitucionIngredientes Lista de sustitucionIngredientes convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	private List<SustitucionIngredienteEnProducto> convertirEntidadSustitucionIngredienteEnProducto(ResultSet rs) throws SQLException, Exception
	{
		DAOTablaIngrediente daoIngrediente = new DAOTablaIngrediente();
		DAOTablaProducto daoProducto = new DAOTablaProducto();
		daoIngrediente.setConn(conn);
		daoProducto.setConn(conn);
		List<SustitucionIngredienteEnProducto> sustitucionIngredientes = new ArrayList<>();
		while (rs.next()) {
			Ingrediente original = daoIngrediente.buscarIngredientePorId(rs.getLong("ID_ORIGINAL"));
			Ingrediente sustituto = daoIngrediente.buscarIngredientePorId(rs.getLong("ID_SUSTITUTO"));
			Producto producto = daoProducto.buscarProductoPorId(rs.getLong("ID_PRODUCTO"));
			sustitucionIngredientes.add(new SustitucionIngredienteEnProducto(original, sustituto, producto));
		}
		daoIngrediente.cerrarRecursos();
		daoProducto.cerrarRecursos();
		return sustitucionIngredientes;
	}

	/**
	 * Elimina todos los sustitucionIngredientes pertenecientes al pedido dado.
	 * @param pedido PedidoMenu al cual eliminarle los sustitucionIngredientes.
	 * @throws SQLException Alg�n problema de la base de datos.<br>
	 */
	public void eliminarSustitucionIngredienteEnProductosPorPedidoMenu(PedidoMenu pedido) throws SQLException {
		String sql = "DELETE FROM SUSN_INGREDIENTE_EN_PRODUCTO WHERE NUMERO_CUENTA LIKE '" + pedido.getCuenta().getNumeroCuenta() + "'";
		sql += " AND NOMBRE_MENU LIKE '" + pedido.getMenu().getNombre() + "' AND NOMBRE_RESTAURANTE LIKE '" + pedido.getMenu().getRestaurante().getNombre() + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}



}
