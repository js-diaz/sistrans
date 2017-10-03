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
public class DAOTablaCategoriaRestaurante {

	/**
	 * Arraylits de recursos que se usan para la ejecuciÃ³n de sentencias SQL
	 */
	private List<Object> recursos;

	/**
	 * Atributo que genera la conexiÃ³n a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOTablaCategoriaRestaurante
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaCategoriaRestaurante() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todas las categorias de la base de datos para un restaurante determinado
	 * @return lista con las categorias de la base de datos para un restaurante.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<Categoria> consultarPorRestaurante(String restaurante) throws SQLException, Exception {
		DAOTablaCategoria daoCategoria = new DAOTablaCategoria();
		daoCategoria.setConn(conn);
		List<Categoria> categorias = new ArrayList<>();

		String sql = "SELECT * FROM CATEGORIA_RESTAURANTE WHERE NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

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
	 * Metodo que, usando la conexión a la base de datos, saca todos los restaurantes de la base de datos para una categoria determinada
	 * @return lista con las categorias de la base de datos para un restaurante.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<Restaurante> consultarPorCategoria(String categoria) throws SQLException, Exception {
		List<Restaurante> restaurantes = new ArrayList<>();

		String sql = "SELECT * FROM CATEGORIA_RESTAURANTE WHERE NOMBRE_CATEGORIA LIKE '" + categoria + "'";

		DAOTablaRestaurante daoRestaurante= new DAOTablaRestaurante();
		daoRestaurante.setConn(conn);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE_RESTAURANTE");
			restaurantes.add(daoRestaurante.darRestaurantePorNombre(nombre));
		}
		daoRestaurante.cerrarRecursos();
		return restaurantes;
	}

	/**
	 * Método que asocia una categoria y un restaurante en la BD.
	 * @param categoria Categoria a asociar.
	 * @param restaurante Restaurante a asociar.
	 * @throws SQLException Excepción generada por problemas de base de datos.<br>
	 * @throws Exception Todo otro problema.
	 */
	public void asociarCategoriaYRestaurante(String categoria, String restaurante) throws SQLException, Exception {
		String sql = "INSERT INTO CATEGORIA_RESTAURANTE VALUES (";
		sql += "'" + categoria + "', ";
		sql += "'" + restaurante + "')";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * MÃ©todo que elimina una relación entre una categoria y restaurantes específicas.<br>
	 * @throws SQLException En caso de que se presente algún otro error.<br>
	 * @throws Exception Cualquier otro error de la base de datos.
	 */
	public void desasociarCategoriaYRestaurante(String restaurante, String categoria) throws SQLException, Exception {

		String sql = "DELETE FROM CATEGORIA_RESTAURANTE WHERE NOMBRE_CATEGORIA LIKE '" + categoria;
		sql += "' AND NOMBRE_RESTAURANTE LIKE '"+ restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	/**
	 * Elimina las categorias que tenga restaurante dado por parámetro.<br>
	 * @param restaurante al cual eliminar las categorias.<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorRestaurante(String restaurante) throws SQLException, Exception {
		String sql = "DELETE FROM CATEGORIA_RESTAURANTE WHERE NOMBRE_RESTAURANTE LIKE '" + restaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Elimina los restaurantes de la categoria dada en parámetro.<br>
	 * @param categoria categoria a la cual eliminar los restaurantes<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorCategoria (String categoria) throws SQLException, Exception{
		String sql = "DELETE FROM CATEGORIA_RESTAURANTE WHERE NOMBRE_CATEGORIA LIKE '" + categoria + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
