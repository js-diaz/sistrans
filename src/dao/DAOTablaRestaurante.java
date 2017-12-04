
package dao;


import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rfc.ContenedoraRestauranteInfoFinanciera;
import rfc.InformacionFinanciera;
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
		List<Restaurante> rta=convertirEntidadRestaurante(rs);
		if(rta.isEmpty()) return null;
		return rta.get(0);
	}


	/**
	 * Metodo que busca el/los restaurantes con el nombre que entra como parametro.
	 * @param name - Nombre de el/los restaurantes a buscar
	 * @return ArrayList con los restaurantes encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public RestauranteMinimum darRestauranteMinimumPorNombre(String name) throws SQLException, Exception {

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

		DAOTablaCategoriaRestaurante daoCat = new DAOTablaCategoriaRestaurante();
		DAOTablaInfoIngRest daoIng = new DAOTablaInfoIngRest();
		DAOTablaMenu daoMenu = new DAOTablaMenu();
		DAOTablaInfoProdRest daoProd = new DAOTablaInfoProdRest();
		DAOTablaUsuario daoUsuario= new DAOTablaUsuario();
		
		daoCat.setConn(conn);
		daoIng.setConn(conn);
		daoMenu.setConn(conn);
		daoProd.setConn(conn);
		daoUsuario.setConn(conn);
		
		String sql = "INSERT INTO RESTAURANTE VALUES (";
		sql += "'" + restaurante.getNombre() + "', ";
		sql += "'" + restaurante.getPagWeb() + "', ";
		sql += restaurante.getRepresentante().getId() + ", ";
		sql += "'" + restaurante.getZona().getNombre() + "')";
		sql += restaurante.getActivo()? "'1', " : "'0', ";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		for(Categoria c : restaurante.getCategorias())
			daoCat.asociarCategoriaYRestaurante(c.getNombre(), restaurante.getNombre());
		for(InfoIngRest i : restaurante.getInfoIngredientes())
			daoIng.addInfoIngRest(i);
		for(MenuMinimum m : restaurante.getMenus())
			daoMenu.addMenu(daoMenu.buscarMenusPorNombreYRestaurante(m.getNombre(), m.getRestaurante().getNombre()));
		for(InfoProdRest p : restaurante.getInfoProductos())
			daoProd.addInfoProdRest(p);
		daoCat.cerrarRecursos();
		daoIng.cerrarRecursos();
		daoMenu.cerrarRecursos();
		daoProd.cerrarRecursos();
	}



	/**
	 * Metodo que actualiza la restaurante que entra como parámetro en la base de datos.
	 * @param restaurante - la restaurante a actualizar. restaurante !=  null
	 * <b> post: </b> se ha actualizado la restaurante en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la restaurante.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateRestaurante(Restaurante restaurante) throws SQLException, Exception {

		DAOTablaCategoriaRestaurante daoCat = new DAOTablaCategoriaRestaurante();
		DAOTablaInfoIngRest daoIng = new DAOTablaInfoIngRest();
		DAOTablaMenu daoMenu = new DAOTablaMenu();
		DAOTablaInfoProdRest daoProd = new DAOTablaInfoProdRest();
		daoCat.setConn(conn);
		daoIng.setConn(conn);
		daoMenu.setConn(conn);
		daoProd.setConn(conn);

		String sql = "UPDATE RESTAURANTE SET ";
		sql += "PAG_WEB = '" + restaurante.getPagWeb() + "'";


		if(restaurante.getRepresentante() != null)
			sql += ", ID_REPRESENTANTE = " + restaurante.getRepresentante().getId();
		if(restaurante.getZona() != null)
			sql += ", NOMBRE_ZONA = '" + restaurante.getZona().getNombre() + "'";

		sql += " WHERE NOMBRE LIKE '" + restaurante.getNombre() + "'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

		if(restaurante.getCategorias() != null) {
			daoCat.eliminarPorRestaurante(restaurante.getNombre());
			for(Categoria c : restaurante.getCategorias())
				daoCat.asociarCategoriaYRestaurante(c.getNombre(), restaurante.getNombre());
		}
		if(restaurante.getInfoIngredientes() != null) {
			daoIng.eliminarInfoIngRestsPorRestaurante(restaurante);
			for(InfoIngRest i : restaurante.getInfoIngredientes())
				daoIng.addInfoIngRest(i);
		}
		if(restaurante.getMenus() != null) {
			daoMenu.eliminarMenusPorRestaurante(restaurante);
			for(MenuMinimum m : restaurante.getMenus())
				daoMenu.addMenu(daoMenu.buscarMenusPorNombreYRestaurante(m.getNombre(), m.getRestaurante().getNombre()));
		}
		if(restaurante.getInfoProductos() != null) {
			daoProd.eliminarInfoProdRestsPorRestaurante(restaurante);
			for(InfoProdRest p : restaurante.getInfoProductos())
				daoProd.addInfoProdRest(p);
		}
		daoCat.cerrarRecursos();
		daoIng.cerrarRecursos();
		daoMenu.cerrarRecursos();
		daoProd.cerrarRecursos();

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
		sql += " WHERE NOMBRE LIKE '" + restaurante.getNombre() +"'";

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
		DAOTablaCategoriaRestaurante daoCat = new DAOTablaCategoriaRestaurante();
		DAOTablaInfoIngRest daoIng = new DAOTablaInfoIngRest();
		DAOTablaMenu daoMenu = new DAOTablaMenu();
		DAOTablaInfoProdRest daoProd = new DAOTablaInfoProdRest();
		daoCat.setConn(conn);
		daoIng.setConn(conn);
		daoMenu.setConn(conn);
		daoProd.setConn(conn);

		DAOTablaUsuario daoUsuario = new DAOTablaUsuario();
		DAOTablaZona daoZona = new DAOTablaZona();
		daoUsuario.setConn(conn);
		daoZona.setConn(conn);
		List<Restaurante> restaurantes = new ArrayList<>();
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			String pagWeb = rs.getString("PAG_WEB");
			Boolean activo = rs.getString("DISPONIBILIDAD").equals("1");
			UsuarioMinimum representante = daoUsuario.buscarUsuarioMinimumPorId(rs.getLong("ID_REPRESENTANTE"));
			ZonaMinimum zona = daoZona.buscarZonasMinimumPorName(rs.getString("NOMBRE_ZONA"));
			restaurantes.add(new Restaurante(nombre, pagWeb, activo, zona, daoCat.consultarPorRestaurante(nombre), representante,
					daoProd.darInfoProdRestsPorRestaurante(nombre), daoIng.darInfoIngRestsPorRestaurante(nombre), daoMenu.darMenusMinimumPorRestaurante(nombre)));
		}
		daoUsuario.cerrarRecursos();
		daoZona.cerrarRecursos();
		daoCat.cerrarRecursos();
		daoIng.cerrarRecursos();
		daoMenu.cerrarRecursos();
		daoProd.cerrarRecursos();
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
			Boolean activo = rs.getString("DISPONIBILIDAD").equals("1");
			restaurantes.add(new RestauranteMinimum(nombre, pagWeb, activo));
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
		List<RestauranteMinimum> rest=convertirEntidadRestauranteMinimum(ps.executeQuery());
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
		String sql = "UPDATE RESTAURANTE SET ";
		sql += "PAG_WEB = '" + restaurante.getPagWeb() + "'";
		if(restaurante instanceof Restaurante) {
			Restaurante restauranteDetail = (Restaurante) restaurante;
			sql += ", ID_REPRESENTANTE = " + restauranteDetail.getRepresentante().getId();
			sql += ", NOMBRE_ZONA = '" + restauranteDetail.getZona().getNombre() + "'";
		}
		sql += " WHERE NOMBRE LIKE '" + darRestauranteDeUsuario(id).getNombre() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Borra un restaurante dado el id de su representante.
	 * @param id Id del representante del restaurante a borrar.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 */
	public void borrarRestaurantePorIdRepresentante(Long id) throws SQLException,Exception {
		RestauranteMinimum restaurante=darRestauranteDeUsuario(id);
		if(restaurante==null) return;
		borrarCategorias(restaurante);
		borrarMenus(restaurante);
		borrarIngredientesRelacionados(restaurante);
		borrarProductosRelacionados(restaurante);
		
		String sql = "DELETE FROM Restaurante WHERE ID_REPRESENTANTE = " + id;
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}
	/**
	 * Surte todos los productos que vende el restaurante con la capacidad máxima de cada uno.<br>
	 * @param nombre Nombre del restaurante.<br>
	 * @throws SQLException Si hay errores con la base de datos.<br>
	 * @throws Exception si hay algún otro error.
	 */
	public void surtirRestaurante(String nombre) throws SQLException, Exception
	{
		DAOTablaInfoProdRest info = new DAOTablaInfoProdRest();
		info.setConn(conn);
		info.surtirInfoProdRest(nombre);
		info.cerrarRecursos();
	}
	/**
	 * Obtiene la información financiera del restaurante dado por parámetro.<br>
	 * @param nombre Nombre del restaurante.<br>
	 * @param esProd La petición es sobre los menús o sobre los productos.<br>
	 * @throws SQLException Si hay errores en la BD.<br>
	 * @throws Exception Si hay otros errores.
	 */
	public List<ContenedoraRestauranteInfoFinanciera> balanceFinanciero(String nombre,boolean esProd) throws SQLException, Exception
	{
		String sql=obtenerPeticionLargaInformacionFinanciera(esProd,nombre);
		PreparedStatement prep=conn.prepareStatement(sql);
		System.out.println(sql);
		double time=System.currentTimeMillis();
		ResultSet rs=prep.executeQuery();
		time=System.currentTimeMillis()-time;
		System.out.println(time);
		List<ContenedoraRestauranteInfoFinanciera> list=new ArrayList<>();
		ContenedoraRestauranteInfoFinanciera crf=null;
		String restaurante=null;
		String restauranteActual=null;
		if(rs.next())
		{
			restauranteActual=rs.getString("NOMBRE_RESTAURANTE");
			crf=new ContenedoraRestauranteInfoFinanciera(restauranteActual, new ArrayList<InformacionFinanciera>());
			crf.getInformacionFinanciera().add(new InformacionFinanciera(rs.getString("PRODUCTO"), rs.getInt("CANTIDADASIGNADA"), 
					rs.getInt("CANTIDADNOASIGNADA"), rs.getDouble("TOTALASIGNADO"), rs.getDouble("TOTALNOASIGNADO")));
			
		}
		while(rs.next())
		{
			restaurante=rs.getString("NOMBRE_RESTAURANTE");
			if(!restaurante.equals(restauranteActual))
			{
				list.add(crf);
				restauranteActual=restaurante;
				crf=new ContenedoraRestauranteInfoFinanciera(restauranteActual, new ArrayList<InformacionFinanciera>());
			}
			crf.getInformacionFinanciera().add(new InformacionFinanciera(rs.getString("PRODUCTO"), rs.getInt("CANTIDADASIGNADA"), 
					rs.getInt("CANTIDADNOASIGNADA"), rs.getDouble("TOTALASIGNADO"), rs.getDouble("TOTALNOASIGNADO")));
		}
		list.add(crf);
		return list;
		
	}
	/**
	 * Obtiene la peiticón de información financiera basándose en si es para un producto o para un menú.<br>
	 * @param esProd Determina si es un producto o menú.<br>
	 * @param nombre Nombre del restaurante.<br>
	 * @return Petición sql correspondiente.
	 */
	private String obtenerPeticionLargaInformacionFinanciera(boolean esProd, String nombre)
	{
		String sql="";
		String temp="";
		if(nombre!=null)
			temp="AND NOMBRE_RESTAURANTE LIKE '"+nombre+"' ";
		if(esProd)
		{
			sql="SELECT NOMBRE_RESTAURANTE, ID_PRODUCTO AS PRODUCTO, NVL(SUM(PRECIO)*SUM(CASE WHEN C.IDUSUARIO IS NULL THEN 0 ELSE B.CANTIDAD END),0) "
					+ "AS TOTALASIGNADO, NVL(SUM(CASE WHEN C.IDUSUARIO IS NULL THEN 0 ELSE B.CANTIDAD END),0) AS CANTIDADASIGNADA,"
					+ "    NVL(SUM(PRECIO)*SUM(CASE WHEN C.IDUSUARIO IS NOT NULL THEN 0 ELSE B.CANTIDAD END),0) AS TOTALNOASIGNADO, "
					+ "NVL(SUM(CASE WHEN C.IDUSUARIO IS NOT NULL THEN 0 ELSE B.CANTIDAD END),0) AS CANTIDADNOASIGNADA"
					+ " FROM INFO_PROD_REST A NATURAL LEFT OUTER JOIN PEDIDO_PROD B LEFT OUTER JOIN CUENTA C ON B.NUMERO_CUENTA=C.NUMEROCUENTA"
					+ "    ,RESTAURANTE D "
					+ "WHERE PAGADA='1' AND D.NOMBRE LIKE NOMBRE_RESTAURANTE "+temp
					+ "GROUP BY NOMBRE_RESTAURANTE, ID_PRODUCTO "
					+ "ORDER BY NOMBRE_RESTAURANTE, ID_PRODUCTO";
		}
		else
			sql="SELECT B.NOMBRE_RESTAURANTE, B.NOMBRE_MENU AS PRODUCTO, NVL(SUM(PRECIO)*SUM(CASE WHEN C.IDUSUARIO IS NULL THEN 0 ELSE B.CANTIDAD END),0) "
					+ "AS TOTALASIGNADO, NVL(SUM(CASE WHEN C.IDUSUARIO IS NULL THEN 0 ELSE B.CANTIDAD END),0) AS CANTIDADASIGNADA,   "
					+ "NVL(SUM(PRECIO)*SUM(CASE WHEN C.IDUSUARIO IS NOT NULL THEN 0 ELSE B.CANTIDAD END),0) AS TOTALNOASIGNADO, "
					+ "NVL(SUM(CASE WHEN C.IDUSUARIO IS NOT NULL THEN 0 ELSE B.CANTIDAD END),0) AS CANTIDADNOASIGNADA  "
					+ "			FROM (MENU A LEFT OUTER JOIN PEDIDO_MENU B ON A.NOMBRE=B.NOMBRE_MENU AND A.NOMBRE_RESTAURANTE=B.NOMBRE_RESTAURANTE) "
					+ "LEFT OUTER JOIN CUENTA C ON B.NUMERO_CUENTA=C.NUMEROCUENTA, RESTAURANTE D"
					+ "			WHERE PAGADA='1' AND D.NOMBRE LIKE B.NOMBRE_RESTAURANTE "+temp
					+ " group by B.NOMBRE_RESTAURANTE, NOMBRE_MENU ORDER BY B.NOMBRE_RESTAURANTE, B.NOMBRE_MENU ";
		return sql;
	}
	
	public  void prueba() throws Exception {
		String sql="SELECT U.NOMBRE AS NOMBRE_USUARIO  FROM  PEDIDO_MENU NATURAL INNER JOIN MENU NATURAL INNER JOIN CATEGORIA_MENU,USUARIO U, CUENTA NATURAL LEFT OUTER JOIN( PREFERENCIA NATURAL FULL OUTER JOIN PREFERENCIACATEGORIA NATURAL FULL OUTER JOIN PREFERENCIAZONA)  WHERE U.ID=IDUSUARIO AND NUMEROCUENTA=NUMERO_CUENTA AND NOMBRE_RESTAURANTE  LIKE 'Nlounge' AND FECHA<=TO_DATE('2017-08-30 07:00:00', 'yyyy-MM-dd hh24:mi:ss') AND FECHA>=TO_DATE('2017-08-07 07:00:00', 'yyyy-MM-dd hh24:mi:ss')   ORDER BY NOMBRECATEGORIA DESC";
		PreparedStatement ps = conn.prepareStatement(sql);
		recursos.add(ps);
		ResultSet rs=ps.executeQuery();
		 
		 
		 ResultSetMetaData rsmd = rs.getMetaData();
		 System.out.println(rsmd.getColumnName(1));
		 if(rs.next())
		 System.out.println(rs.getString("NOMBRE_USUARIO"));
		 System.out.println(rs.getString("U.NOMBRE"));

	}
	
	public void sacarRestaurante(String nombreRestaurante) throws SQLException,Exception
	{
		String sql = "UPDATE RESTAURANTE SET ";
		sql += "DISPONIBILIDAD='0'";
		sql += " WHERE NOMBRE LIKE '" + nombreRestaurante + "'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
  
	public void restituirRestaurante(String nombreRestaurante) throws SQLException,Exception
	{
		String sql = "UPDATE RESTAURANTE SET ";
		sql += "DISPONIBILIDAD='1'";
		sql += " WHERE NOMBRE LIKE '" + nombreRestaurante + "'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	/**
	 * Retorna la lista con los restaurantes que hayan tenida la mayor cantidad de ventas.
	 * @return Arraylist con los restaurantes que cumplen la conidicion dada.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<Restaurante> darRestaurantesMasYMenosFrecuentadosPorDiaDeLaSemana(String dia) throws SQLException, Exception {
		
		List<Restaurante> restaurantes = new ArrayList<>();

		String sql = "SELECT NOMBRE_RESTAURANTE, COUNT(DISTINCT NUMERO_CUENTA) AS VISITAS FROM " + 
				"(SELECT NOMBRE_RESTAURANTE, NUMERO_CUENTA FROM PEDIDO_PROD, CUENTA " + 
				"WHERE NUMERO_CUENTA = NUMEROCUENTA AND TO_CHAR(FECHA, 'DAY') = '" + dia + "' " + 
				"UNION ALL SELECT NOMBRE_RESTAURANTE, NUMERO_CUENTA FROM PEDIDO_MENU, CUENTA " + 
				"WHERE NUMERO_CUENTA = NUMEROCUENTA AND TO_CHAR(FECHA, 'DAY') = '" + dia + "' " +
				"GROUP BY NOMBRE_RESTAURANTE ORDER BY VISITAS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		if(rs.first()) 
			restaurantes.add(darRestaurantePorNombre(rs.getString("NOMBRE_RESTAURANTE")));
		if(rs.last())
			restaurantes.add(darRestaurantePorNombre(rs.getString("NOMBRE_RESTAURANTE")));
		return restaurantes;
	}
	
	

}
