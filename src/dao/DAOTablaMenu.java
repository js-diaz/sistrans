package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Categoria;
import vos.InfoProdRest;
import vos.RestauranteMinimum;
import vos.Menu;

public class DAOTablaMenu {

	/**
	 * Arraylits de recursos que se usan para la ejecuci贸n de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexi贸n a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOMenu
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaMenu() {
		recursos = new ArrayList<Object>();
	}

	/**
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
	 * Metodo que, usando la conexi贸n a la base de datos, saca todos los menus de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM MENUS;
	 * @return Arraylist con los menus de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Menu> darMenus() throws SQLException, Exception {

		String sql = "SELECT * FROM MENU";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadMenu(rs);
	}


	

	/**
	 * Metodo que busca el/los menus con el nombre que entra como parametro.
	 * @param name - Nombre de el menu a buscar
	 * @param restaurante - Nombre del restaurante al que pertenece
	 * @return ArrayList con los menus encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Menu buscarMenusPorNombreYRestaurante(String name, String restaurante) throws SQLException, Exception {

		String sql = "SELECT * FROM MENU WHERE NOMBRE LIKE '" + name + "' AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		ArrayList<Menu> menus = convertirEntidadMenu(rs);
		return menus.get(0);
	}

	/**
	 * Metodo que agrega la menu que entra como parametro a la base de datos.
	 * @param menu - la menu a agregar. menu !=  null
	 * <b> post: </b> se ha agregado la menu a la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que la menu baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la menu a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addMenu(Menu menu) throws SQLException, Exception {

		String sql = "INSERT INTO MENU VALUES (";
		sql += "'" + menu.getNombre() + "',";
		sql += "'" + menu.getRestaurante().getNombre() + "', ";
		sql += menu.getPrecio() +", ";
		sql += menu.getCosto() + ")";
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	

	/**
	 * Metodo que actualiza la menu que entra como par谩metro en la base de datos.
	 * @param menu - la menu a actualizar. menu !=  null
	 * <b> post: </b> se ha actualizado la menu en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la menu.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateMenu(Menu menu) throws SQLException, Exception {

		String sql = "UPDATE MENU SET ";
		sql += "PRECIO = " + menu.getPrecio();
		sql += ", COSTO = "+ menu.getCosto();
		sql += " WHERE NOMBRE LIKE '" + menu.getNombre() + "' AND NOMBRE_RESTAURANTE LIKE '" + menu.getRestaurante().getNombre();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina la menu que entra como parametro en la base de datos.
	 * @param menu - la menu a borrar. menu !=  null
	 * <b> post: </b> se ha borrado la menu en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la menu.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteMenu(Menu menu) throws SQLException, Exception {

		borrarCategorias(menu);
		
		String sql = "DELETE FROM MENU";
		sql += " WHERE NOMBRE LIKE '" + menu.getNombre() + "' AND NOMBRE_RESTAURANTE LIKE '" + menu.getRestaurante().getNombre() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	/**
	 * Crea un arreglo de menus con el set de resultados pasado por par醡etro.<br>
	 * @param rs Set de resultados.<br>
	 * @return menus Lista de menus convertidas.<br>
	 * @throws SQLException Alg煤n problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepci贸n.
	 */
	private ArrayList<Menu> convertirEntidadMenu(ResultSet rs) throws SQLException, Exception
	{
		DAOTablaRestaurante daoRest = new DAOTablaRestaurante();
		daoRest.setConn(conn);
		ArrayList<Menu> menus = new ArrayList<>();
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			double precio = rs.getDouble("PRECIO");
			double costo = rs.getDouble("COSTO");
			RestauranteMinimum restaurante = daoRest.darRestaurantePorNombre(rs.getString("NOMBRE_RESTAURANTE"));
			menus.add(new Menu(nombre, precio, costo, restaurante, new ArrayList<InfoProdRest>(), new ArrayList<Categoria>()));
		}
		daoRest.cerrarRecursos();
		return menus;
	}
	
	/**
	 * Borra la asociacion a las categorias a las cuales pertenece el menu.<br>
	 * @param menu Menu de donde se borran.
	 * @throws SQLException Alg鷑 problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepci髇.
	 */
	private void borrarCategorias(Menu menu) throws SQLException, Exception
	{
		DAOTablaCategoriaMenu daoCatMenu = new DAOTablaCategoriaMenu();
		daoCatMenu.setConn(conn);
		daoCatMenu.eliminarPorMenu(menu);
		daoCatMenu.cerrarRecursos();
	}

	/**
	 * Elimina todos los menus pertenecientes al restaurante dado.
	 * @param restaurante Restaurante al cual eliminarle los menus.
	 * @throws SQLException Alg鷑 problema de la base de datos.<br>
	 */
	public void eliminarMenusPorRestaurante(RestauranteMinimum restaurante) throws SQLException {
		String sql = "DELETE FROM MENU WHERE NOMBRE_RESTAURANTE LIKE '" + restaurante.getNombre() + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}

}
