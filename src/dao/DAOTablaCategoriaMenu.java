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
public class DAOTablaCategoriaMenu {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private List<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
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
	 * Metodo que inicializa la connection del DAO a la base de datos con la conexi�n que entra como par�metro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection con){
		this.conn = con;
	}


	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todas las categorias de la base de datos para un menu determinado
	 * @return lista con las categorias de la base de datos para un menu.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<Categoria> consultarPorMenu(MenuMinimum menu) throws SQLException, Exception {
		DAOTablaCategoria daoCategoria = new DAOTablaCategoria();
		daoCategoria.setConn(conn);
		List<Categoria> categorias = new ArrayList<>();

		String sql = "SELECT * FROM CATEGORIA_MENU WHERE NOMBRE_MENU LIKE '" + menu.getNombre();
		sql += "' AND NOMBRE_RESTAURANTE LIKE '" + menu.getRestaurante().getNombre() + "'";

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
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los menus de la base de datos para una categoria determinada
	 * @return lista con las categorias de la base de datos para un menu.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<Menu> consultarPorCategoria(Categoria categoria) throws SQLException, Exception {
		List<Menu> menus = new ArrayList<>();

		String sql = "SELECT * FROM CATEGORIA_MENU WHERE NOMBRE_CATEGORIA LIKE '" + categoria.getNombre() + "'";

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
	 * M�todo que asocia una categoria y un menu en la BD.
	 * @param categoria Categoria a asociar.
	 * @param menu Menu a asociar.
	 * @throws SQLException Excepci�n generada por problemas de base de datos.<br>
	 * @throws Exception Todo otro problema.
	 */
	public void asociarCategoriaYMenu(Categoria categoria, MenuMinimum menu) throws SQLException, Exception {
		String sql = "INSERT INTO CATEGORIA_MENU VALUES (";
		sql += "'" + categoria.getNombre() + "', ";
		sql += "'" + menu.getNombre() + "', ";
		sql += "'" + menu.getRestaurante().getNombre() + "')";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina una relaci�n entre una categoria y menus espec�ficas.<br>
	 * @throws SQLException En caso de que se presente alg�n otro error.<br>
	 * @throws Exception Cualquier otro error de la base de datos.
	 */
	public void desasociarCategoriaYMenu(MenuMinimum menu, Categoria categoria) throws SQLException, Exception {

		String sql = "DELETE FROM CATEGORIA_MENU WHERE NOMBRE_CATEGORIA LIKE '" + categoria.getNombre();
		sql += "' AND NOMBRE_MENU LIKE '"+ menu.getNombre();
		sql += "' AND NOMBRE_RESTAURANTE LIKE '" + menu.getRestaurante().getNombre() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	/**
	 * Elimina las categorias que tenga menu dado por par�metro.<br>
	 * @param menu al cual eliminar las categorias.<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorMenu(MenuMinimum menu) throws SQLException, Exception {
		String sql = "DELETE FROM CATEGORIA_MENU WHERE NOMBRE_MENU LIKE '" + menu.getNombre();
		sql += "' AND NOMBRE_RESTAURNANTE LIKE '" + menu.getRestaurante().getNombre() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Elimina los menus de la categoria dada en par�metro.<br>
	 * @param categoria categoria a la cual eliminar los menus<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorCategoria (Categoria categoria) throws SQLException, Exception{
		String sql = "DELETE FROM CATEGORIA_MENU WHERE NOMBRE_CATEGORIA LIKE '" + categoria.getNombre() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
