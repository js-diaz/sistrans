package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicaciÃ³n
 * @author JuanSebastian
 */
public class DAOTablaCategoriaMenu {

	/**
	 * Arraylits de recursos que se usan para la ejecuciÃ³n de sentencias SQL
	 */
	private List<Object> recursos;

	/**
	 * Atributo que genera la conexiÃ³n a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOTablaCategoriaMenu
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaCategoriaMenu() {
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
	 * Metodo que inicializa la connection del DAO a la base de datos con la conexión que entra como parámetro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection con){
		this.conn = con;
	}


	/**
	 * Metodo que, usando la conexión a la base de datos, saca todas las categorias de la base de datos para un menu determinado
	 * @return lista con las categorias de la base de datos para un menu.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<Categoria> consultarPorMenu(String nombreMenu, String nombreRestaurante) throws SQLException, Exception {
		DAOTablaCategoria daoCategoria = new DAOTablaCategoria();
		daoCategoria.setConn(conn);
		List<Categoria> categorias = new ArrayList<>();

		String sql = "SELECT * FROM CATEGORIA_MENU WHERE NOMBRE_MENU LIKE '" + nombreMenu;
		sql += "' AND NOMBRE_RESTAURANTE LIKE '" + nombreRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			String name = rs.getString("NOMBRE_CATEGORIA");
			categorias.add(daoCategoria.buscarCategoriasPorName(name));

		}
		daoCategoria.cerrarRecursos();
		return categorias;
	}

	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los menus de la base de datos para una categoria determinada
	 * @return lista con las categorias de la base de datos para un menu.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<MenuMinimum> consultarPorCategoria(String nombreCategoria) throws SQLException, Exception {
		List<MenuMinimum> menus = new ArrayList<>();

		String sql = "SELECT * FROM CATEGORIA_MENU WHERE NOMBRE_CATEGORIA LIKE '" + nombreCategoria + "'";

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
	 * Método que asocia una categoria y un menu en la BD.
	 * @param categoria Categoria a asociar.
	 * @param menu Menu a asociar.
	 * @throws SQLException Excepción generada por problemas de base de datos.<br>
	 * @throws Exception Todo otro problema.
	 */
	public void asociarCategoriaYMenu(String nombreCategoria, String nombreMenu, String nombreRestaurante) throws SQLException, Exception {
		String sql = "INSERT INTO CATEGORIA_MENU VALUES (";
		sql += "'" + nombreCategoria + "', ";
		sql += "'" + nombreMenu + "', ";
		sql += "'" + nombreRestaurante + "')";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * MÃ©todo que elimina una relación entre una categoria y menus específicas.<br>
	 * @throws SQLException En caso de que se presente algún otro error.<br>
	 * @throws Exception Cualquier otro error de la base de datos.
	 */
	public void desasociarCategoriaYMenu(String nombreCategoria, String nombreMenu, String nombreRestaurante) throws SQLException, Exception {

		String sql = "DELETE FROM CATEGORIA_MENU WHERE NOMBRE_CATEGORIA LIKE '" + nombreCategoria;
		sql += "' AND NOMBRE_MENU LIKE '"+ nombreMenu;
		sql += "' AND NOMBRE_RESTAURANTE LIKE '" + nombreRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	/**
	 * Elimina las categorias que tenga menu dado por parámetro.<br>
	 * @param menu al cual eliminar las categorias.<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorMenu(String nombreMenu, String nombreRestaurante) throws SQLException, Exception {
		String sql = "DELETE FROM CATEGORIA_MENU WHERE NOMBRE_MENU LIKE '" + nombreMenu;
		sql += "' AND NOMBRE_RESTAURNANTE LIKE '" + nombreRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Elimina los menus de la categoria dada en parámetro.<br>
	 * @param categoria categoria a la cual eliminar los menus<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorCategoria (String nombreCategoria) throws SQLException, Exception{
		String sql = "DELETE FROM CATEGORIA_MENU WHERE NOMBRE_CATEGORIA LIKE '" + nombreCategoria + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
