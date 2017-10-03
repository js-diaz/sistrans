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
public class DAOTablaCategoriaProducto {

	/**
	 * Arraylits de recursos que se usan para la ejecuciÃ³n de sentencias SQL
	 */
	private List<Object> recursos;

	/**
	 * Atributo que genera la conexiÃ³n a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOTablaCategoriaProducto
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaCategoriaProducto() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todas las categorias de la base de datos para un producto determinado
	 * @return lista con las categorias de la base de datos para un producto.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<Categoria> consultarPorProducto(Long producto) throws SQLException, Exception {
		DAOTablaCategoria daoCategoria = new DAOTablaCategoria();
		daoCategoria.setConn(conn);
		List<Categoria> categorias = new ArrayList<>();

		String sql = "SELECT * FROM CATEGORIA_PRODUCTO WHERE ID_PRODUCTO = " + producto;

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
	 * Metodo que, usando la conexión a la base de datos, saca todos los productos de la base de datos para una categoria determinada
	 * @return lista con las categorias de la base de datos para un producto.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<Producto> consultarPorCategoria(String categoria) throws SQLException, Exception {
		List<Producto> productos = new ArrayList<>();

		String sql = "SELECT * FROM CATEGORIA_PRODUCTO WHERE NOMBRE_CATEGORIA LIKE '" + categoria + "'";

		DAOTablaProducto daoProducto= new DAOTablaProducto();
		daoProducto.setConn(conn);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			Long id = rs.getLong("ID_PRODUCTO");
			productos.add(daoProducto.buscarProductoPorId(id));
		}
		daoProducto.cerrarRecursos();
		return productos;
	}

	/**
	 * Método que asocia una categoria y un producto en la BD.
	 * @param categoria Categoria a asociar.
	 * @param producto Producto a asociar.
	 * @throws SQLException Excepción generada por problemas de base de datos.<br>
	 * @throws Exception Todo otro problema.
	 */
	public void asociarCategoriaYProducto(String categoria, Long producto) throws SQLException, Exception {
		String sql = "INSERT INTO CATEGORIA_PRODUCTO VALUES (";
		sql += "'" + categoria + "', ";
		sql += producto + ")";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * MÃ©todo que elimina una relación entre una categoria y productos específicas.<br>
	 * @throws SQLException En caso de que se presente algún otro error.<br>
	 * @throws Exception Cualquier otro error de la base de datos.
	 */
	public void desasociarCategoriaYProducto(Long producto, String categoria) throws SQLException, Exception {

		String sql = "DELETE FROM CATEGORIA_PRODUCTO WHERE NOMBRE_CATEGORIA LIKE '" + categoria;
		sql += "' AND ID_PRODUCTO = "+ producto;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	/**
	 * Elimina las categorias que tenga producto dado por parámetro.<br>
	 * @param producto al cual eliminar las categorias.<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorProducto(Long producto) throws SQLException, Exception {
		String sql = "DELETE FROM CATEGORIA_PRODUCTO WHERE ID_PRODUCTO = " + producto;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Elimina los productos de la categoria dada en parámetro.<br>
	 * @param categoria categoria a la cual eliminar los productos<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorCategoria (String categoria) throws SQLException, Exception{
		String sql = "DELETE FROM CATEGORIA_PRODUCTO WHERE NOMBRE_CATEGORIA LIKE '" + categoria + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
