package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los
 * requerimientos de la aplicación
 * 
 * @author JuanSebastian
 */
public class DAOTablaSustitutosIngrediente {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private List<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOTablaSustituto Ingrediente
	 * 
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaSustitutosIngrediente() {
		recursos = new ArrayList<Object>();
	}

	/**
	 * Metodo que cierra todos los recursos que estan en el arreglo de recursos
	 * <b>post: </b> Todos los recurso del arreglo de recursos han sido cerrados
	 */
	public void cerrarRecursos() {
		for (Object ob : recursos) {
			if (ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	/**
	 * Metodo que inicializa la connection del DAO a la base de datos con la
	 * conexi�n que entra como par�metro.
	 * 
	 * @param con - connection a la base de datos
	 */
	public void setConn(Connection con) {
		this.conn = con;
	}

	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los platos de la base de datos para un ingrediente determinado
	 * @return lista con los platos de la base de datos para un ingrediente.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<Ingrediente> consultarPorIngrediente(Long idIngrediente, String nombreRestaurante) throws SQLException, Exception {
		DAOTablaIngrediente daoSustituto = new DAOTablaIngrediente();
		daoSustituto.setConn(conn);
		List<Ingrediente> sustitutos = new ArrayList<>();

		String sql = "SELECT * FROM SUSTITUTOS_INGREDIENTE WHERE ID_INGREDIENTE = " + idIngrediente;
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + nombreRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			Long id = rs.getLong("ID_PLATO");
			sustitutos.add(daoSustituto.buscarIngredientePorId(id));
		}
		daoSustituto.cerrarRecursos();
		return sustitutos;
	}

	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los ingredientes de la base de datos para un plato determinada
	 * @return lista con los ingrediente s de la base de datos para un plato.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<InfoIngRest> consultarPorSustituto(Long idSustituto) throws SQLException, Exception {
		List<InfoIngRest> ingredientes = new ArrayList<>();

		String sql = "SELECT * FROM SUSTITUTOS_INGREDIENTE WHERE ID_SUSTITUTO = " + idSustituto;

		DAOTablaInfoIngRest daoIngrediente = new DAOTablaInfoIngRest();
		daoIngrediente.setConn(conn);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			Long id = rs.getLong("ID_INGREDIENTE");
			String restaurante = rs.getString("NOMBRE_RESTAURANTE");
			ingredientes.add(daoIngrediente.buscarInfoIngRestsPorIdYRestaurante(id, restaurante));
		}
		daoIngrediente.cerrarRecursos();
		return ingredientes;
	}

	/**
	 * M�todo que asocia un plato y un ingrediente en la BD.
	 * @param plato Sustituto a asociar.
	 * @param ingrediente Ingrediente a asociar.
	 * @throws SQLException Excepci�n generada por problemas de base de datos.<br>
	 * @throws Exception Todo otro problema.
	 */
	public void asociarSustitutoYIngrediente(Long idSustituto, Long idIngrediente, String nombreRestaurante) throws SQLException, Exception {
		String sql = "INSERT INTO SUSTITUTOS_INGREDIENTE VALUES (";
		sql += idIngrediente + ", ";
		sql += "" + idSustituto + ", ";
		sql += "'" + nombreRestaurante + "')";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina una relaci�n entre un plato y ingredientes espec�ficos.<br>
	 * @throws SQLException En caso de que se presente alg�n otro error.<br>
	 * @throws Exception Cualquier otro error de la base de datos.
	 */
	public void desasociarSustitutoYIngrediente(Long idIngrediente, String nombreRestaurante, Long idSustituto) throws SQLException, Exception {

		String sql = "DELETE FROM SUSTITUTOS_INGREDIENTE WHERE ID_SUSTITUTO = " + idSustituto;
		sql += " AND ID_INGREDIENTE = "+ idIngrediente;
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + nombreRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Elimina los platos que tenga ingrediente dado por par�metro.<br>
	 * @param ingrediente al cual eliminar los platos.<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorIngrediente(Long idIngrediente, String nombreRestaurante) throws SQLException, Exception {
		String sql = "DELETE FROM SUSTITUTOS_INGREDIENTE WHERE ID_INGREDIENTE = " + idIngrediente;
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + nombreRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Elimina los ingredientes del plato dada en par�metro.<br>
	 * @param plato plato a la cual eliminar los ingrediente s<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorSustituto(Long idSustituto) throws SQLException, Exception {
		String sql = "DELETE FROM SUSTITUTOS_INGREDIENTE WHERE ID_SUSTITUTO = " + idSustituto;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
