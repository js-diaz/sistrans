package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación
 * @author JuanSebastian
 */
public class DAOTablaPerteneceAMenu {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private List<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOTablaProductoMenu
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaPerteneceAMenu() {
		recursos = new ArrayList<Object>();
	}

	/**
	 * Metodo que cierra todos los recursos que estan en el arreglo de recursos
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
	 * Metodo que inicializa la connection del DAO a la base de datos con la conexi�n que entra como par�metro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection con){
		this.conn = con;
	}


	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los platos de la base de datos para un menu determinado
	 * @return lista con los platos de la base de datos para un menu.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<InfoProdRest> consultarPorMenu(String nombreMenu, String nombreRestaurante) throws SQLException, Exception {
		DAOTablaInfoProdRest daoProducto = new DAOTablaInfoProdRest();
		daoProducto.setConn(conn);
		List<InfoProdRest> platos = new ArrayList<>();

		String sql = "SELECT * FROM PERTENECE_A_MENU WHERE NOMBRE_MENU LIKE '" + nombreMenu;
		sql += "' AND NOMBRE_RESTAURANTE LIKE '" + nombreRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			long id = rs.getLong("ID_PLATO");
			platos.add(daoProducto.buscarInfoProdRestsPorIdYRestaurante(id, nombreRestaurante));
		}
		daoProducto.cerrarRecursos();
		return platos;
	}

	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los menus de la base de datos para un plato determinada
	 * @return lista con los menus de la base de datos para un plato.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<MenuMinimum> consultarPorProducto(Long idProducto, String nombreRestaurante) throws SQLException, Exception {
		List<MenuMinimum> menus = new ArrayList<>();

		String sql = "SELECT * FROM PERTENECE_A_MENU WHERE ID_PRODUCTO = " + idProducto;
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + nombreRestaurante + "'";

		DAOTablaMenu daoMenu= new DAOTablaMenu();
		daoMenu.setConn(conn);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE_MENU");
			String restaurante = rs.getString("NOMBRE_RESTAURANTE");
			menus.add(daoMenu.buscarMenusPorNombreYRestaurante(nombre, restaurante));
		}
		daoMenu.cerrarRecursos();
		return menus;
	}

	/**
	 * M�todo que asocia un plato y un menu en la BD.
	 * @param plato Producto a asociar.
	 * @param menu Menu a asociar.
	 * @throws SQLException Excepci�n generada por problemas de base de datos.<br>
	 * @throws Exception Todo otro problema.
	 */
	public void asociarProductoYMenu(Long idProducto, String nombreMenu, String nombreRestaurante) throws SQLException, Exception {
		String sql = "INSERT INTO PERTENECE_A_MENU VALUES (";
		sql += "'" + nombreMenu + "', ";
		sql += "" + idProducto + ", ";
		sql += "'" + nombreRestaurante + "')";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina una relaci�n entre un plato y menus espec�ficos.<br>
	 * @throws SQLException En caso de que se presente alg�n otro error.<br>
	 * @throws Exception Cualquier otro error de la base de datos.
	 */
	public void desasociarProductoYMenu(String nombreMenu, String nombreRestaurante, Long idProducto) throws SQLException, Exception {

		String sql = "DELETE FROM PERTENECE_A_MENU WHERE ID_PRODUCTO = " + idProducto;
		sql += " AND NOMBRE_MENU LIKE '"+ nombreMenu;
		sql += "' AND NOMBRE_RESTAURANTE LIKE '" + nombreRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	/**
	 * Elimina los platos que tenga menu dado por par�metro.<br>
	 * @param menu al cual eliminar los platos.<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorMenu(String nombreMenu, String nombreRestaurante) throws SQLException, Exception {
		String sql = "DELETE FROM PERTENECE_A_MENU WHERE NOMBRE_MENU LIKE '" + nombreMenu;
		sql += "' AND NOMBRE_RESTAURANTE LIKE '" + nombreRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Elimina los menus del plato dada en par�metro.<br>
	 * @param plato plato a la cual eliminar los menus<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorProducto (Long idProducto, String nombreRestaurante) throws SQLException, Exception{
		String sql = "DELETE FROM PERTENECE_A_MENU WHERE ID_PRODUCTO = " + idProducto;
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + nombreRestaurante +"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
