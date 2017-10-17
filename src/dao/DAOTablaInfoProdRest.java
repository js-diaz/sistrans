package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import vos.RestauranteMinimum;
import vos.InfoProdRest;
import vos.Producto;

public class DAOTablaInfoProdRest {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private List<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
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
	 * Metodo que inicializa la connection del DAO a la base de datos con la conexión que entra como parametro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection conn){
		this.conn = conn;
	}


	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los infoProdRests de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM INFO_PROD_RESTS;
	 * @return Arraylist con los infoProdRests de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<InfoProdRest> darInfoProdRests() throws SQLException, Exception {

		String sql = "SELECT * FROM INFO_PROD_REST";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadInfoProdRest(rs);
	}


	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los infoProdRests de la base de datos para un restaurante particular.
	 * @param restaurante nombre del Restaurante.
	 * @return Arraylist con los infoProdRests de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<InfoProdRest> darInfoProdRestsPorRestaurante(String restaurante) throws SQLException, Exception {

		String sql = "SELECT * FROM INFO_PROD_REST WHERE NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadInfoProdRest(rs);
	}
	
	/**
	 * Metodo que busca la informacion de un producto en un restaurnate dados por par�metro.
	 * @param id - Id del producto a buscar
	 * @param restaurante - Nombre del restaurante al que pertenece
	 * @return List con los infoProdRests encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public InfoProdRest buscarInfoProdRestsPorIdYRestaurante(Long id, String restaurante) throws SQLException, Exception {

		String sql = "SELECT * FROM INFO_PROD_REST WHERE ID_PRODUCTO = " + id + " AND NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<InfoProdRest> infoProdRests = convertirEntidadInfoProdRest(rs);
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
		sql += dateFormat(infoProdRest.getFechaInicio()) + ", ";
		sql += dateFormat(infoProdRest.getFechaFin());
		sql+=infoProdRest.getCantidadMaxima()+")";
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	

	/**
	 * Metodo que actualiza la infoProdRest que entra como parámetro en la base de datos.
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
		sql += ", FECHA_INICIO = "+ dateFormat(infoProdRest.getFechaInicio());
		sql += ", FECHA_FIN =" + dateFormat(infoProdRest.getFechaFin());
		sql+=", CANTIDAD_MAXIMA="+infoProdRest.getCantidadMaxima();
		
		sql += " WHERE ID_PRODUCTO = " + infoProdRest.getProducto().getId() + " AND NOMBRE_RESTAURANTE LIKE '" + infoProdRest.getRestaurante().getNombre()+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	/**
	 * Formatea el valor de la cuenta al dado en la base de datos.<br>
	 * @param fecha Fecha de la cuenta.<br>
	 * @return Valor a insertar en la base de datos
	 */
	private String dateFormat(Date fecha) {
		SimpleDateFormat x = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return "TO_DATE('"+x.format(fecha)+"','yyyy-MM-dd hh24:mi:ss')";
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
	 * Crea un arreglo de infoProdRests con el set de resultados pasado por par�metro.<br>
	 * @param rs Set de resultados.<br>
	 * @return infoProdRests Lista de infoProdRests convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	private List<InfoProdRest> convertirEntidadInfoProdRest(ResultSet rs) throws SQLException, Exception
	{
		DAOTablaRestaurante daoRest = new DAOTablaRestaurante();
		DAOTablaProducto daoProd = new DAOTablaProducto();
		daoRest.setConn(conn);
		daoProd.setConn(conn);
		List<InfoProdRest> infoProdRests = new ArrayList<>();
		while (rs.next()) {
			double precio = rs.getDouble("PRECIO");
			double costo = rs.getDouble("COSTO");
			int disponibilidad = rs.getInt("DISPONIBILIDAD");
			Date fechaInicio = rs.getDate("FECHA_INICIO");
			Date fechaFin = rs.getDate("FECHA_FIN");
			int maxima=rs.getInt("CANTIDAD_MAXIMA");
			Producto producto = daoProd.buscarProductoPorId(rs.getLong("ID_PRODUCTO"));
			RestauranteMinimum restaurante = daoRest.darRestauranteMinimumPorNombre(rs.getString("NOMBRE_RESTAURANTE"));
			infoProdRests.add(new InfoProdRest(costo, precio, disponibilidad, fechaInicio, fechaFin, producto, restaurante, maxima));
		}
		daoRest.cerrarRecursos();
		daoProd.cerrarRecursos();
		return infoProdRests;
	}
	
	/**
	 * Elimina todos los infoProdRests pertenecientes al restaurante dado.
	 * @param restaurante Restaurante al cual eliminarle los infoProdRests.
	 * @throws SQLException Alg�n problema de la base de datos.<br>
	 */
	public void eliminarInfoProdRestsPorRestaurante(RestauranteMinimum restaurante) throws SQLException {
		String sql = "DELETE FROM INFO_PROD_REST WHERE NOMBRE_RESTAURANTE LIKE '" + restaurante.getNombre() + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}
	/**
	 * Surte cada uno de los productos de un restaurante dado.<br>
	 * @param nombre Nombre del restaurante.<br>
	 * @throws SQLException Excepción de BD.<br>
	 * @throws Exception Excepción cualquiera.
	 */
	public void surtirInfoProdRest(String nombre) throws SQLException, Exception {
		String sql="UPDATE INFO_PROD_REST SET DISPONIBILIDAD=CANTIDAD_MAXIMA WHERE NOMBRE_RESTAURANTE LIKE '"+nombre+"'";
		PreparedStatement ps= conn.prepareStatement(sql);
		ps.executeQuery();
	}

}
