package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import vos.RestauranteMinimum;
import vos.InfoIngRest;
import vos.Ingrediente;

public class DAOTablaInfoIngRest {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private List<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOInfoIngRest
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaInfoIngRest() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos los infoIngRests de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM INFO_ING_RESTS;
	 * @return Arraylist con los infoIngRests de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<InfoIngRest> darInfoIngRests() throws SQLException, Exception {

		String sql = "SELECT * FROM INFO_ING_REST";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadInfoIngRest(rs);
	}
	
	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los infoIngRests de la base de datos para un restaurante particular.
	 * @param restaurante nombre del Restaurante.
	 * @return Arraylist con los infoIngRests de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<InfoIngRest> darInfoIngRestsPorRestaurante(String restaurante) throws SQLException, Exception {

		String sql = "SELECT * FROM INFO_ING_REST WHERE NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadInfoIngRest(rs);
	}

	

	/**
	 * Metodo que busca la informacion de un ingrediente en un restaurnate dados por par�metro.
	 * @param id - Id del ingrediente a buscar
	 * @param restaurante - Nombre del restaurante al que pertenece
	 * @return List con los infoIngRests encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public InfoIngRest buscarInfoIngRestsPorIdYRestaurante(Long id, String restaurante) throws SQLException, Exception {

		String sql = "SELECT * FROM INFO_ING_REST WHERE ID_INGREDIENTE = ";
		sql += id + " AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<InfoIngRest> infoIngRests = convertirEntidadInfoIngRest(rs);
		return infoIngRests.get(0);
	}

	/**
	 * Metodo que agrega la infoIngRest que entra como parametro a la base de datos.
	 * @param infoIngRest - la infoIngRest a agregar. infoIngRest !=  null
	 * <b> post: </b> se ha agregado la infoIngRest a la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que la infoIngRest baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la infoIngRest a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addInfoIngRest(InfoIngRest infoIngRest) throws SQLException, Exception {

		String sql = "INSERT INTO INFO_ING_REST VALUES (";
		sql += infoIngRest.getIngrediente().getId() + ", ";
		sql += "'" + infoIngRest.getRestaurante().getNombre() + "', ";
		sql += infoIngRest.getPrecioAdicion() +", ";
		sql += infoIngRest.getPrecioSustituto() + ")";
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		insertarSustitutos(infoIngRest);
	}
	
	

	/**
	 * Metodo que actualiza la infoIngRest que entra como parámetro en la base de datos.
	 * @param infoIngRest - la infoIngRest a actualizar. infoIngRest !=  null
	 * <b> post: </b> se ha actualizado la infoIngRest en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la infoIngRest.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateInfoIngRest(InfoIngRest infoIngRest) throws SQLException, Exception {

		String sql = "UPDATE INFO_ING_REST SET ";
		sql += "PRECIO_ADICION = " + infoIngRest.getPrecioAdicion();
		sql += ", PRECIO_SUSTITUTO = "+ infoIngRest.getPrecioSustituto();
		
		sql += " WHERE ID_INGREDIENTE = " + infoIngRest.getIngrediente().getId() + " AND NOMBRE_RESTAURANTE LIKE '" + infoIngRest.getRestaurante().getNombre() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		insertarSustitutos(infoIngRest);
	}

	private void insertarSustitutos(InfoIngRest infoIngRest) throws SQLException, Exception {
		DAOTablaSustitutosIngrediente daoSust = new DAOTablaSustitutosIngrediente();
		daoSust.setConn(conn);
		daoSust.eliminarPorIngrediente(infoIngRest.getIngrediente().getId(), infoIngRest.getRestaurante().getNombre());
		for(Ingrediente ing : infoIngRest.getSustitutos())
			daoSust.asociarSustitutoYIngrediente(ing.getId(), infoIngRest.getIngrediente().getId(), infoIngRest.getRestaurante().getNombre());
		daoSust.cerrarRecursos();
	}

	/**
	 * Metodo que elimina la infoIngRest que entra como parametro en la base de datos.
	 * @param infoIngRest - la infoIngRest a borrar. infoIngRest !=  null
	 * <b> post: </b> se ha borrado la infoIngRest en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la infoIngRest.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteInfoIngRest(InfoIngRest infoIngRest) throws SQLException, Exception {
		
		String sql = "DELETE FROM INFO_ING_REST";
		sql += " WHERE ID_INGREDIENTE = " + infoIngRest.getIngrediente().getId(); 
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + infoIngRest.getRestaurante().getNombre() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	/**
	 * Crea un arreglo de infoIngRests con el set de resultados pasado por par�metro.<br>
	 * @param rs Set de resultados.<br>
	 * @return infoIngRests Lista de infoIngRests convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	private List<InfoIngRest> convertirEntidadInfoIngRest(ResultSet rs) throws SQLException, Exception
	{
		DAOTablaRestaurante daoRest = new DAOTablaRestaurante();
		DAOTablaIngrediente daoProd = new DAOTablaIngrediente();
		DAOTablaSustitutosIngrediente daoSust = new DAOTablaSustitutosIngrediente();
		daoRest.setConn(conn);
		daoProd.setConn(conn);
		daoSust.setConn(conn);
		List<InfoIngRest> infoIngRests = new ArrayList<>();
		while (rs.next()) {
			double precioAdicion = rs.getDouble("PRECIO_ADICION");
			double precioSustituto = rs.getDouble("PRECIO_SUSTITUTO");
			Ingrediente ingrediente = daoProd.buscarIngredientePorId(rs.getLong("ID_INGREDIENTE"));
			RestauranteMinimum restaurante = daoRest.darRestauranteMinimumPorNombre(rs.getString("NOMBRE_RESTAURANTE"));
			List<Ingrediente> sustitutos = daoSust.consultarPorIngrediente(ingrediente.getId(), restaurante.getNombre());
			infoIngRests.add(new InfoIngRest(precioSustituto, precioAdicion, ingrediente, restaurante, sustitutos));
		}
		daoRest.cerrarRecursos();
		daoProd.cerrarRecursos();
		daoSust.cerrarRecursos();
		return infoIngRests;
	}
	
	/**
	 * Elimina todos los infoIngRests pertenecientes al restaurante dado.
	 * @param restaurante Restaurante al cual eliminarle los infoIngRests.
	 * @throws SQLException Alg�n problema de la base de datos.<br>
	 */
	public void eliminarInfoIngRestsPorRestaurante(RestauranteMinimum restaurante) throws SQLException {
		String sql = "DELETE FROM INFO_ING_REST WHERE NOMBRE_RESTAURANTE LIKE '" + restaurante.getNombre() + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}

}
