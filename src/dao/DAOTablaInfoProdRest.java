package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import vos.RestauranteMinimum;
import vos.InfoProdRest;
import vos.Producto;

public class DAOTablaInfoProdRest {

	/**
	 * Arraylits de recursos que se usan para la ejecuci贸n de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexi贸n a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOInfoProdRest
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaInfoProdRest() {
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
	 * Metodo que, usando la conexi贸n a la base de datos, saca todos los infoProdRests de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM INFO_PROD_RESTS;
	 * @return Arraylist con los infoProdRests de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<InfoProdRest> darInfoProdRests() throws SQLException, Exception {

		String sql = "SELECT * FROM INFO_PROD_REST";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadInfoProdRest(rs);
	}


	

	/**
	 * Metodo que busca la informacion de un producto en un restaurnate dados por par醡etro.
	 * @param id - Id del producto a buscar
	 * @param restaurante - Nombre del restaurante al que pertenece
	 * @return ArrayList con los infoProdRests encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public InfoProdRest buscarInfoProdRestsPorNombreYRestaurante(Long id, String restaurante) throws SQLException, Exception {

		String sql = "SELECT * FROM INFO_PROD_REST WHERE ID_PRODUCTO = " + id + " AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		ArrayList<InfoProdRest> infoProdRests = convertirEntidadInfoProdRest(rs);
		return infoProdRests.get(0);
	}

	/**
	 * Metodo que agrega la infoProdRest que entra como parametro a la base de datos.
	 * @param infoProdRest - la infoProdRest a agregar. infoProdRest !=  null
	 * <b> post: </b> se ha agregado la infoProdRest a la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que la infoProdRest baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la infoProdRest a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addInfoProdRest(InfoProdRest infoProdRest) throws SQLException, Exception {

		String sql = "INSERT INTO INFO_PROD_REST VALUES (";
		sql += infoProdRest.getProducto().getId() + ", ";
		sql += "'" + infoProdRest.getRestaurante().getNombre() + "', ";
		sql += infoProdRest.getPrecio() +", ";
		sql += infoProdRest.getCosto() + ", ";
		sql += infoProdRest.getDisponibilidad() + ", ";
		sql += "TO_DATE(" + infoProdRest.getFechaInicio() + "), ";
		sql += "TO_DATE(" + infoProdRest.getFechaFin() + "))";
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	

	/**
	 * Metodo que actualiza la infoProdRest que entra como par谩metro en la base de datos.
	 * @param infoProdRest - la infoProdRest a actualizar. infoProdRest !=  null
	 * <b> post: </b> se ha actualizado la infoProdRest en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la infoProdRest.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateInfoProdRest(InfoProdRest infoProdRest) throws SQLException, Exception {

		String sql = "UPDATE INFO_PROD_REST SET ";
		sql += "PRECIO = " + infoProdRest.getPrecio();
		sql += ", COSTO = "+ infoProdRest.getCosto();
		sql += ", DISPONIBILIDAD = " + infoProdRest.getDisponibilidad();
		sql += ", FECHA_INICIO = TO_DATE(" + infoProdRest.getFechaInicio() + ")";
		sql += ", FECHS_FIN = TO_DATE(" + infoProdRest.getFechaFin() + ")";
		
		sql += " WHERE ID_PRODUCTO = " + infoProdRest.getProducto().getId() + " AND NOMBRE_RESTAURANTE LIKE '" + infoProdRest.getRestaurante().getNombre();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina la infoProdRest que entra como parametro en la base de datos.
	 * @param infoProdRest - la infoProdRest a borrar. infoProdRest !=  null
	 * <b> post: </b> se ha borrado la infoProdRest en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la infoProdRest.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteInfoProdRest(InfoProdRest infoProdRest) throws SQLException, Exception {
		
		String sql = "DELETE FROM INFO_PROD_REST";
		sql += " WHERE ID_PRODUCTO = " + infoProdRest.getProducto().getId(); 
		sql += " AND NOMBRE_RESTAURANTE LIKE '" + infoProdRest.getRestaurante().getNombre() + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	/**
	 * Crea un arreglo de infoProdRests con el set de resultados pasado por par醡etro.<br>
	 * @param rs Set de resultados.<br>
	 * @return infoProdRests Lista de infoProdRests convertidas.<br>
	 * @throws SQLException Alg煤n problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepci贸n.
	 */
	private ArrayList<InfoProdRest> convertirEntidadInfoProdRest(ResultSet rs) throws SQLException, Exception
	{
		DAOTablaRestaurante daoRest = new DAOTablaRestaurante();
		DAOTablaProducto daoProd = new DAOTablaProducto();
		daoRest.setConn(conn);
		daoProd.setConn(conn);
		ArrayList<InfoProdRest> infoProdRests = new ArrayList<>();
		while (rs.next()) {
			double precio = rs.getDouble("PRECIO");
			double costo = rs.getDouble("COSTO");
			int disponibilidad = rs.getInt("DISPONIBILIDAD");
			Date fechaInicio = rs.getDate("FECHA_INICIO");
			Date fechaFin = rs.getDate("FECHA_FIN");
			Producto producto = daoProd.buscarProductoPorId(rs.getLong("ID_PRODUCTO"));
			RestauranteMinimum restaurante = daoRest.darRestaurantePorNombre(rs.getString("NOMBRE_RESTAURANTE"));
			infoProdRests.add(new InfoProdRest(costo, precio, disponibilidad, fechaInicio, fechaFin, producto, restaurante));
		}
		daoRest.cerrarRecursos();
		daoProd.cerrarRecursos();
		return infoProdRests;
	}
	
	/**
	 * Elimina todos los infoProdRests pertenecientes al restaurante dado.
	 * @param restaurante Restaurante al cual eliminarle los infoProdRests.
	 * @throws SQLException Alg鷑 problema de la base de datos.<br>
	 */
	public void eliminarInfoProdRestsPorRestaurante(RestauranteMinimum restaurante) throws SQLException {
		String sql = "DELETE FROM INFO_PROD_REST WHERE NOMBRE_RESTAURANTE LIKE '" + restaurante.getNombre() + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}

}
