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
public class DAOTablaSustitutosProducto {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private List<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOTablaSustituto Producto
	 * 
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaSustitutosProducto() {
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
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los platos de la base de datos para un producto determinado
	 * @return lista con los platos de la base de datos para un producto.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<Producto> consultarPorProducto(Long idProducto, String nombreRestaurante) throws SQLException, Exception {
		DAOTablaProducto daoSustituto = new DAOTablaProducto();
		daoSustituto.setConn(conn);
		List<Producto> sustitutos = new ArrayList<>();

		String sql = "SELECT * FROM SUSTITUTOS_PRODUCTO WHERE ID_PRODUCTO = " + idProducto;
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + nombreRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			Long id = rs.getLong("ID_PLATO");
			sustitutos.add(daoSustituto.buscarProductoPorId(id));
		}
		daoSustituto.cerrarRecursos();
		return sustitutos;
	}

	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los productos de la base de datos para un plato determinada
	 * @return lista con los producto s de la base de datos para un plato.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<InfoProdRest> consultarPorSustituto(Long idSustituto) throws SQLException, Exception {
		List<InfoProdRest> productos = new ArrayList<>();

		String sql = "SELECT * FROM SUSTITUTOS_PRODUCTO WHERE ID_SUSTITUTO = " + idSustituto;

		DAOTablaInfoProdRest daoProducto = new DAOTablaInfoProdRest();
		daoProducto.setConn(conn);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			Long id = rs.getLong("ID_PRODUCTO");
			String restaurante = rs.getString("NOMBRE_RESTAURANTE");
			productos.add(daoProducto.buscarInfoProdRestsPorIdYRestaurante(id, restaurante));
		}
		daoProducto.cerrarRecursos();
		return productos;
	}

	/**
	 * M�todo que asocia un plato y un producto en la BD.
	 * @param plato Sustituto a asociar.
	 * @param producto Producto a asociar.
	 * @throws SQLException Excepci�n generada por problemas de base de datos.<br>
	 * @throws Exception Todo otro problema.
	 */
	public void asociarSustitutoYProducto(Long idSustituto, Long idProducto, String nombreRestaurante) throws SQLException, Exception {
		String sql = "INSERT INTO SUSTITUTOS_PRODUCTO VALUES (";
		sql += idProducto + ", ";
		sql += "" + idSustituto + ", ";
		sql += "'" + nombreRestaurante + "')";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina una relaci�n entre un plato y productos espec�ficos.<br>
	 * @throws SQLException En caso de que se presente alg�n otro error.<br>
	 * @throws Exception Cualquier otro error de la base de datos.
	 */
	public void desasociarSustitutoYProducto(Long idProducto, String nombreRestaurante, Long idSustituto) throws SQLException, Exception {

		String sql = "DELETE FROM SUSTITUTOS_PRODUCTO WHERE ID_SUSTITUTO = " + idSustituto;
		sql += " AND ID_PRODUCTO = "+ idProducto;
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + nombreRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Elimina los platos que tenga producto dado por par�metro.<br>
	 * @param producto al cual eliminar los platos.<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorProducto(Long idProducto, String nombreRestaurante) throws SQLException, Exception {
		String sql = "DELETE FROM SUSTITUTOS_PRODUCTO WHERE ID_PRODUCTO = " + idProducto;
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + nombreRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Elimina los productos del plato dada en par�metro.<br>
	 * @param plato plato a la cual eliminar los producto s<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorSustituto(Long idSustituto) throws SQLException, Exception {
		String sql = "DELETE FROM SUSTITUTOS_PRODUCTO WHERE ID_SUSTITUTO = " + idSustituto;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
