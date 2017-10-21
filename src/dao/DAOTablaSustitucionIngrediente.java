package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Ingrediente;
import vos.PedidoProd;
import vos.SustitucionIngrediente;

public class DAOTablaSustitucionIngrediente {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOSustitucionIngrediente
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaSustitucionIngrediente() {
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
	 * <b>SQL Statement:</b> SELECT * FROM SUSTITUCION_INGREDIENTES;
	 * @return Arraylist con los sustitucionIngredientes de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<SustitucionIngrediente> darSustitucionIngredientes() throws SQLException, Exception {

		String sql = "SELECT * FROM SUSTITUCION_INGREDIENTE";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadSustitucionIngrediente(rs);
	}

	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los sustitucionIngredientes de la base de datos para un pedido particular.
	 * @param cuenta nombre del PedidoProd.
	 * @return Arraylist con los sustitucionIngredientes de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<SustitucionIngrediente> darSustitucionIngredientesPorPedidoProd(String cuenta, Long producto, String restaurante) throws SQLException, Exception {

		String sql = "SELECT * FROM SUSTITUCION_INGREDIENTE WHERE NUMERO_CUENTA LIKE '" + cuenta + "'";
		sql += " AND ID_PRODUCTO = " + producto + " AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadSustitucionIngrediente(rs);
	}

	/**
	 * Metodo que busca la informacion de un ingrediente en una cienta dados por par�metro.
	 * @param id - Id del ingrediente a buscar
	 * @param pedido - Nombre del pedido al que pertenece
	 * @return ArrayList con los sustitucionIngredientes encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public SustitucionIngrediente buscarSustitucionIngredientesPorNombreYPedidoProd(String cuenta, Long producto, String restaurante, Long original, Long sustituto) throws SQLException, Exception {

		String sql = "SELECT * FROM SUSTITUCION_INGREDIENTE WHERE NUMERO_CUENTA LIKE '" + cuenta + "'";
		sql += " AND ID_PRODUCTO = " + producto + "' AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";
		sql += "AND ID_ORIGINAL = " + original + " AND ID_SUSTITUTO = " + sustituto;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<SustitucionIngrediente> sustitucionIngredientes = convertirEntidadSustitucionIngrediente(rs);
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
	public void addSustitucionIngrediente(SustitucionIngrediente sustitucionIngrediente, PedidoProd pedido) throws SQLException, Exception {

		String sql = "INSERT INTO SUSTITUCION_INGREDIENTE VALUES (";
		sql += sustitucionIngrediente.getOriginal().getId() + ", ";
		sql += sustitucionIngrediente.getSustituto().getId() + ", ";
		sql += "'" + pedido.getCuenta().getNumeroCuenta() + "', ";
		sql += pedido.getPlato().getProducto().getId() + ", ";
		sql += "'" + pedido.getPlato().getRestaurante().getNombre() + "')";

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
	public void deleteSustitucionIngrediente(SustitucionIngrediente sustitucionIngrediente, PedidoProd pedido) throws SQLException, Exception {

		String sql = "DELETE FROM SUSTITUCION_INGREDIENTE";
		sql += " WHERE ID_ORIGINAL = " + sustitucionIngrediente.getOriginal().getId();
		sql += " AND ID_SUSTITUTO = " + sustitucionIngrediente.getSustituto().getId();
		sql += " AND ID_PRODUCTO = " + pedido.getPlato().getProducto().getId();
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + pedido.getPlato().getRestaurante().getNombre() + "'";
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
	private List<SustitucionIngrediente> convertirEntidadSustitucionIngrediente(ResultSet rs) throws SQLException, Exception
	{
		DAOTablaIngrediente daoIngrediente = new DAOTablaIngrediente();
		daoIngrediente.setConn(conn);
		List<SustitucionIngrediente> sustitucionIngredientes = new ArrayList<>();
		while (rs.next()) {
			Ingrediente original = daoIngrediente.buscarIngredientePorId(rs.getLong("ID_ORIGINAL"));
			Ingrediente sustituto = daoIngrediente.buscarIngredientePorId(rs.getLong("ID_SUSTITUTO"));
			sustitucionIngredientes.add(new SustitucionIngrediente(original, sustituto));
		}
		daoIngrediente.cerrarRecursos();
		return sustitucionIngredientes;
	}

	/**
	 * Elimina todos los sustitucionIngredientes pertenecientes al pedido dado.
	 * @param pedido PedidoProd al cual eliminarle los sustitucionIngredientes.
	 * @throws SQLException Alg�n problema de la base de datos.<br>
	 */
	public void eliminarSustitucionIngredientesPorPedidoProd(PedidoProd pedido) throws SQLException {
		String sql = "DELETE FROM SUSTITUCION_INGREDIENTE WHERE NUMERO_CUENTA LIKE '" + pedido.getCuenta().getNumeroCuenta() + "'";
		sql += " AND ID_PRODUCTO = " + pedido.getPlato().getProducto().getId() + " AND NOMBRE_RESTAURANTE LIKE '" + pedido.getPlato().getRestaurante().getNombre() + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}



}
