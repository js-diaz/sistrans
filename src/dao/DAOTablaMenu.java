package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Categoria;
import vos.InfoProdRest;
import vos.RestauranteMinimum;
import vos.Menu;
import vos.MenuMinimum;

public class DAOTablaMenu {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private List<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
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
	 * Metodo que inicializa la connection del DAO a la base de datos con la conexión que entra como parametro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection conn){
		this.conn = conn;
	}


	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los menus de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM MENUS;
	 * @return Arraylist con los menus de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<Menu> darMenus() throws SQLException, Exception {

		String sql = "SELECT * FROM MENU";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadMenu(rs);
	}

	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los menus de la base de datos para un restaurante particular.
	 * @param restaurante nombre del Restaurante.
	 * @return Arraylist con los menus de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<Menu> darMenusPorRestaurante(String restaurante) throws SQLException, Exception {

		String sql = "SELECT * FROM MENU WHERE NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadMenu(rs);
	}

	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los menus de la base de datos para un restaurante particular.
	 * @param nombre nombre del Restaurante.
	 * @return Arraylist con los menus de la base de datos en representacion minimum.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<MenuMinimum> darMenusMinimumPorRestaurante(String nombre) throws Exception, SQLException {
		String sql = "SELECT * FROM MENU WHERE NOMBRE_RESTAURANTE LIKE '" + nombre + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadMenuMinimum(rs);
	}

	/**
	 * Metodo que busca el/los menus con el nombre que entra como parametro.
	 * @param name - Nombre de el menu a buscar
	 * @param restaurante - Nombre del restaurante al que pertenece
	 * @return List con los menus encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Menu buscarMenusPorNombreYRestaurante(String name, String restaurante) throws SQLException, Exception {

		String sql = "SELECT * FROM MENU WHERE NOMBRE LIKE '" + name + "' AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<Menu> menus = convertirEntidadMenu(rs);
		return menus.get(0);
	}

	/**
	 * Metodo que busca el/los menus con el nombre que entra como parametro.
	 * @param name - Nombre de el menu a buscar
	 * @param restaurante - Nombre del restaurante al que pertenece
	 * @return List con los menus encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public MenuMinimum buscarMenusMinimumPorNombreYRestaurante(String name, String restaurante) throws SQLException, Exception {

		String sql = "SELECT * FROM MENU WHERE NOMBRE LIKE '" + name + "' AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<MenuMinimum> menus = convertirEntidadMenuMinimum(rs);

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

		if(!menu.getRestaurante().getActivo()) throw new Exception("El restaurante está por fuera del negocio");

		DAOTablaCategoriaMenu daoCategoria = new DAOTablaCategoriaMenu();
		DAOTablaPerteneceAMenu daoProducto = new DAOTablaPerteneceAMenu();
		daoCategoria.setConn(conn);
		daoProducto.setConn(conn);

		String sql = "INSERT INTO MENU VALUES (";
		sql += "'" + menu.getNombre() + "',";
		sql += "'" + menu.getRestaurante().getNombre() + "', ";
		sql += menu.getPrecio() +", ";
		sql += menu.getCosto() + ")";

		for(Categoria c : menu.getCategorias())
			daoCategoria.asociarCategoriaYMenu(c.getNombre(), menu.getNombre(), menu.getRestaurante().getNombre());
		for(InfoProdRest p : menu.getPlatos())
			daoProducto.asociarProductoYMenu(p.getProducto().getId(), menu.getNombre(), menu.getRestaurante().getNombre());
		daoCategoria.cerrarRecursos();
		daoProducto.cerrarRecursos();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}



	/**
	 * Metodo que actualiza la menu que entra como parámetro en la base de datos.
	 * @param menu - la menu a actualizar. menu !=  null
	 * <b> post: </b> se ha actualizado la menu en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la menu.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateMenu(Menu menu) throws SQLException, Exception {

		if(!menu.getRestaurante().getActivo()) throw new Exception("El restaurante está por fuera del negocio");

		DAOTablaCategoriaMenu daoCategoria = new DAOTablaCategoriaMenu();
		DAOTablaPerteneceAMenu daoProducto = new DAOTablaPerteneceAMenu();
		daoCategoria.setConn(conn);
		daoProducto.setConn(conn);

		String sql = "UPDATE MENU SET ";
		sql += "PRECIO = " + menu.getPrecio();
		sql += ", COSTO = "+ menu.getCosto();
		sql += " WHERE NOMBRE LIKE '" + menu.getNombre() + "' AND NOMBRE_RESTAURANTE LIKE '" + menu.getRestaurante().getNombre() + "'";

		if(menu.getCategorias() != null) {
			daoCategoria.eliminarPorMenu(menu.getNombre(), menu.getRestaurante().getNombre());
			for(Categoria c : menu.getCategorias())
				daoCategoria.asociarCategoriaYMenu(c.getNombre(), menu.getNombre(), menu.getRestaurante().getNombre());
		}
		if(menu.getPlatos() != null) {
			daoProducto.eliminarPorMenu(menu.getNombre(), menu.getRestaurante().getNombre());
			for(InfoProdRest p : menu.getPlatos())
				daoProducto.asociarProductoYMenu(p.getProducto().getId(), menu.getNombre(), menu.getRestaurante().getNombre());
		}
		daoCategoria.cerrarRecursos();
		daoProducto.cerrarRecursos();


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
		borrarProductos(menu);

		String sql = "DELETE FROM MENU";
		sql += " WHERE NOMBRE LIKE '" + menu.getNombre() + "' AND NOMBRE_RESTAURANTE LIKE '" + menu.getRestaurante().getNombre() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Crea un arreglo de menus con el set de resultados pasado por par�metro.<br>
	 * @param rs Set de resultados.<br>
	 * @return menus Lista de menus convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	private List<Menu> convertirEntidadMenu(ResultSet rs) throws SQLException, Exception
	{
		DAOTablaRestaurante daoRest = new DAOTablaRestaurante();
		DAOTablaCategoriaMenu daoCat = new DAOTablaCategoriaMenu();
		DAOTablaPerteneceAMenu daoProd = new DAOTablaPerteneceAMenu();
		daoRest.setConn(conn);
		daoCat.setConn(conn);
		daoProd.setConn(conn);
		List<Menu> menus = new ArrayList<>();
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			double precio = rs.getDouble("PRECIO");
			double costo = rs.getDouble("COSTO");
			RestauranteMinimum restaurante = daoRest.darRestauranteMinimumPorNombre(rs.getString("NOMBRE_RESTAURANTE"));
			List<InfoProdRest> productos = daoProd.consultarPorMenu(nombre, restaurante.getNombre());
			List<Categoria> categorias = daoCat.consultarPorMenu(nombre, restaurante.getNombre());
			menus.add(new Menu(nombre, precio, costo, restaurante, productos, categorias));
		}
		daoRest.cerrarRecursos();
		daoCat.cerrarRecursos();
		daoProd.cerrarRecursos();
		return menus;
	}
	/**
	 * Crea un arreglo de menus minimum con el set de resultados pasado por par�metro.<br>
	 * @param rs Set de resultados.<br>
	 * @return menus Lista de menus convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	private List<MenuMinimum> convertirEntidadMenuMinimum(ResultSet rs) throws SQLException, Exception {
		DAOTablaRestaurante daoRest = new DAOTablaRestaurante();
		daoRest.setConn(conn);
		List<MenuMinimum> menus = new ArrayList<>();
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			double precio = rs.getDouble("PRECIO");
			double costo = rs.getDouble("COSTO");
			RestauranteMinimum restaurante = daoRest.darRestauranteMinimumPorNombre(rs.getString("NOMBRE_RESTAURANTE"));
			menus.add(new MenuMinimum(nombre, precio, costo, restaurante));
		}
		daoRest.cerrarRecursos();
		return menus;
	}

	/**
	 * Borra la asociacion a las categorias a las cuales pertenece el menu.<br>
	 * @param menu Menu de donde se borran.
	 * @throws SQLException Alg�n problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepci�n.
	 */
	private void borrarCategorias(Menu menu) throws SQLException, Exception
	{
		DAOTablaCategoriaMenu daoCatMenu = new DAOTablaCategoriaMenu();
		daoCatMenu.setConn(conn);
		daoCatMenu.eliminarPorMenu(menu.getNombre(), menu.getRestaurante().getNombre());
		daoCatMenu.cerrarRecursos();
	}

	/**
	 * Borra la asociacion a los productos que contiene el menu.<br>
	 * @param menu Menu de donde se borran.
	 * @throws SQLException Alg�n problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepci�n.
	 */
	private void borrarProductos(Menu menu) throws SQLException, Exception
	{
		DAOTablaPerteneceAMenu daoProdMenu = new DAOTablaPerteneceAMenu();
		daoProdMenu.setConn(conn);
		daoProdMenu.eliminarPorMenu(menu.getNombre(), menu.getRestaurante().getNombre());
		daoProdMenu.cerrarRecursos();
	}

	/**
	 * Elimina todos los menus pertenecientes al restaurante dado.
	 * @param restaurante Restaurante al cual eliminarle los menus.
	 * @throws SQLException Alg�n problema de la base de datos.<br>
	 */
	public void eliminarMenusPorRestaurante(RestauranteMinimum restaurante) throws SQLException {
		String sql = "DELETE FROM MENU WHERE NOMBRE_RESTAURANTE LIKE '" + restaurante.getNombre() + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}



}
