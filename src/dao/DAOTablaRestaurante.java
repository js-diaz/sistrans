
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicaci�n
 * @author JuanSebastian
 */
public class DAOTablaRestaurante {


	/**
	 * Arraylits de recursos que se usan para la ejecuci�n de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexi�n a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAORestaurante
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaRestaurante() {
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
	public void setConn(Connection con){
		this.conn = con;
	}


	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los restaurantes de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM RESTAURANTE;
	 * @return Arraylist con los restaurantes de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<Restaurante> darRestaurantes() throws SQLException, Exception {

		String sql = "SELECT * FROM RESTAURANTE";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadRestaurante(rs);
	}


	/**
	 * Metodo que busca el restaurante con el nombre que entra como parametro.
	 * @param name - Nombre de el restaurante a buscar
	 * @return ArrayList con los restaurantes encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Restaurante darRestaurantePorNombre(String name) throws SQLException, Exception {

		String sql = "SELECT * FROM RESTAURANTE WHERE NOMBRE LIKE '" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		return convertirEntidadRestaurante(rs).get(0);
	}
	

	/**
	 * Metodo que busca el/los restaurantes con el nombre que entra como parametro.
	 * @param name - Nombre de el/los restaurantes a buscar
	 * @return ArrayList con los restaurantes encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public RestauranteMinimum buscarRestaurantesMinimumPorName(String name) throws SQLException, Exception {

		String sql = "SELECT * FROM RESTAURANTE WHERE NOMBRE LIKE '" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		return convertirEntidadRestauranteMinimum(rs).get(0);
	}

	/**
	 * Metodo que agrega la restaurante que entra como parametro a la base de datos.
	 * @param restaurante - la restaurante a agregar. restaurante !=  null
	 * <b> post: </b> se ha agregado la restaurante a la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que la restaurante baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la restaurante a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addRestaurante(Restaurante restaurante) throws SQLException, Exception {

		String sql = "INSERT INTO RESTAURANTE VALUES (";
		sql += "'" + restaurante.getNombre() + "', ";
		sql += "'" + restaurante.getPagWeb() + "', ";
		sql += restaurante.getRepresentante().getId() + ", ";
		sql += "'" + restaurante.getZona().getNombre() + ")";
		
		
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	

	/**
	 * Metodo que actualiza la restaurante que entra como parámetro en la base de datos.
	 * @param restaurante - la restaurante a actualizar. restaurante !=  null
	 * <b> post: </b> se ha actualizado la restaurante en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la restaurante.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateRestaurante(RestauranteMinimum restaurante) throws SQLException, Exception {
		
		String sql = "UPDATE RESTAURANTE SET ";
		sql += "PAG_WEB = '" + restaurante.getPagWeb() + "'";
		if(restaurante instanceof Restaurante) {
			Restaurante restauranteDetail = (Restaurante) restaurante;
			sql += ", ID_REPRESENTANTE = " + restauranteDetail.getRepresentante().getId();
			sql += ", NOMBRE_ZONA = '" + restauranteDetail.getZona().getNombre() + "'";
		}
		sql += " WHERE NOMBRE LIKE '" + restaurante.getNombre() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina la restaurante que entra como parametro en la base de datos.
	 * @param restaurante - la restaurante a borrar. restaurante !=  null
	 * <b> post: </b> se ha borrado la restaurante en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la restaurante.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteRestaurante(RestauranteMinimum restaurante) throws SQLException, Exception {

		borrarCategorias(restaurante);
		borrarMenus(restaurante);
		borrarIngredientesRelacionados(restaurante);
		borrarProductosRelacionados(restaurante);
		
		String sql = "DELETE FROM RESTAURANTE";
		sql += " WHERE NOMBRE LIKE " + restaurante.getNombre();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	/**
	 * Crea un arreglo de restaurantes con el set de resultados pasado por parámetro.<br>
	 * @param rs Set de resultados.<br>
	 * @return restaurantes Lista de restaurantes convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	private List<Restaurante> convertirEntidadRestaurante(ResultSet rs) throws SQLException, Exception
	{
		DAOTablaUsuario daoUsuario = new DAOTablaUsuario();
		DAOTablaZona daoZona = new DAOTablaZona();
		daoUsuario.setConn(conn);
		daoZona.setConn(conn);
		List<Restaurante> restaurantes = new ArrayList<>();
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			String pagWeb = rs.getString("PAG_WEB");
			UsuarioMinimum representante = daoUsuario.buscarUsuarioMinimumPorId(rs.getLong("ID_REPRESENTANTE"));
			ZonaMinimum zona = daoZona.buscarZonasMinimumPorName(rs.getString("NOMBRE_ZONA"));
			restaurantes.add(new Restaurante(nombre, pagWeb, zona, new ArrayList<Categoria>(), representante,
					new ArrayList<InfoProdRest>(), new ArrayList<InfoIngRest>(), new ArrayList<MenuMinimum>()));
		}
		daoUsuario.cerrarRecursos();
		daoZona.cerrarRecursos();
		return restaurantes;
	}
	
	/**
	 * Crea un arreglo de restaurantes con el set de resultados pasado por parámetro.<br>
	 * @param rs Set de resultados.<br>
	 * @return restaurantes Lista de restaurantes convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	private ArrayList<RestauranteMinimum> convertirEntidadRestauranteMinimum(ResultSet rs) throws SQLException, Exception
	{
		ArrayList<RestauranteMinimum> restaurantes = new ArrayList<>();
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			String pagWeb = rs.getString("PAG_WEB");
			restaurantes.add(new RestauranteMinimum(nombre, pagWeb));
		}
		return restaurantes;
	}
	
	/**
	 * Borra los menus que pertenecen a un restaurante que se va a borrar.<br>
	 * @param restaurante Restaurante de donde se borran
	 */
	private void borrarMenus(RestauranteMinimum restaurante) throws SQLException, Exception
	{
		DAOTablaMenu daoMenu = new DAOTablaMenu();
		daoMenu.setConn(conn);
		daoMenu.eliminarMenusPorRestaurante(restaurante);
		daoMenu.cerrarRecursos();
	}
	
	/**
	 * Borra la asociacion a las categorias a las cuales pertenece el restaurante.<br>
	 * @param restaurante Restaurante de donde se borran.
	 */
	private void borrarCategorias(RestauranteMinimum restaurante) throws SQLException, Exception
	{
		DAOTablaCategoriaRestaurante daoCatRest = new DAOTablaCategoriaRestaurante();
		daoCatRest.setConn(conn);
		daoCatRest.eliminarPorRestaurante(restaurante.getNombre());
		daoCatRest.cerrarRecursos();
	}
	
	/**
	 * Borra la informaci�n del los productos que son servidos por un restaurante que se va a borrar.<br>
	 * @param restaurante Restaurante de donde se borran
	 */
	private void borrarProductosRelacionados (RestauranteMinimum restaurante) throws SQLException, Exception
	{
		DAOTablaInfoProdRest daoProducto = new DAOTablaInfoProdRest();
		daoProducto.setConn(conn);
		daoProducto.eliminarInfoProdRestsPorRestaurante(restaurante);
		daoProducto.cerrarRecursos();
	}
	
	/**
	 * Borra la informacion de los ingrediantes que son usados por el restaurante que se va a borrar.<br>
	 * @param restaurante Restaurante de donde se borran
	 */
	private void borrarIngredientesRelacionados (RestauranteMinimum restaurante) throws SQLException, Exception
	{
		DAOTablaInfoIngRest daoIngrediente = new DAOTablaInfoIngRest();
		daoIngrediente.setConn(conn);
		daoIngrediente.eliminarInfoIngRestsPorRestaurante(restaurante);
		daoIngrediente.cerrarRecursos();
	}
	
	/**
	 * Consulta los restaurantes de una zona particular.
	 * @param nombreZona nombre de la zona a buscar.
	 * @return lista con los restaurantes buscados.
	 * @throws SQLException - Cualquier error que la base de datos arroje. 
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<RestauranteMinimum> consultarPorZona(String nombreZona) throws SQLException, Exception {
		String sql = "SELECT * FROM Restaurante WHERE NOMBRE_ZONA LIKE '" + nombreZona + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		return convertirEntidadRestauranteMinimum(ps.executeQuery());
	}

	/**
	 * Elimina todos los restaurantes en la lista dada.
	 * @param list Restaurantes a eliminar.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la restaurante.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void eliminarRestaurantes(List<RestauranteMinimum> restaurantes) throws SQLException, Exception {
		if(restaurantes==null) return;
		for(RestauranteMinimum restauranteMinimum: restaurantes) {
			deleteRestaurante(restauranteMinimum);
		}
	}

	/**
	 * Busca un restaurante dado el id de su representante.
	 * @param id Id del representante.
	 * @return restaurante en representaci�n minimum.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la restaurante.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public RestauranteMinimum darRestauranteDeUsuario(Long id) throws SQLException, Exception {
		String sql = "SELECT * FROM Restaurante WHERE ID_REPRESENTANTE = " + id;
		PreparedStatement ps = conn.prepareStatement(sql);
		recursos.add(ps);
		List<Restaurante> rest=convertirEntidadRestaurante(ps.executeQuery());
		if(rest.isEmpty()) return null;
		return rest.get(0);
	}

	/**
	 * Asocia un restaurante a un usuario para que este lo represente.
	 * @param id
	 * @param restaurante
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la restaurante.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void insertarPorIdRepresentante(Long id, RestauranteMinimum restaurante) throws SQLException, Exception {
		String sql = "UPDATE Restaurante SET ID_REPRESENTANTE = " + id; 
		sql += " WHERE NOMBRE LIKE '" + restaurante.getNombre() + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}

	/**
	 * Actualiza el restaurante del usuario dado con la informacion dada.
	 * @param id Id del representante del restaurante a modificar.
	 * @param restaurante Modificaciones a efectuar.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la restaurante.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void actualizarRestauranteDeUsuario(Long id, RestauranteMinimum restaurante) throws SQLException, Exception {
		restaurante.setNombre(darRestauranteDeUsuario(id).getNombre());
		updateRestaurante(restaurante);
	}

	/**
	 * Borra un restaurante dado el id de su representante.
	 * @param id Id del representante del restaurante a borrar.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 */
	public void borrarRestaurantePorIdRepresentante(Long id) throws SQLException {
		String sql = "DELETE FROM Restaurante WHERE ID_REPRESENTANTE = " + id;
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}

	

}
