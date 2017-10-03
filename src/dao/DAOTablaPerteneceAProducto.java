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
public class DAOTablaPerteneceAProducto {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private List<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOTablaIngredienteProducto
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaPerteneceAProducto() {
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
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los ingredientes de la base de datos para un producto determinado
	 * @return lista con las ingredientes de la base de datos para un producto.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<Ingrediente> consultarPorProducto(Producto producto) throws SQLException, Exception {
		DAOTablaIngrediente daoIngrediente = new DAOTablaIngrediente();
		daoIngrediente.setConn(conn);
		List<Ingrediente> ingredientes = new ArrayList<>();

		String sql = "SELECT * FROM PERTENECE_A_PLATO WHERE ID_PLATO = " + producto.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			Long id = rs.getLong("ID_INGREDIENTE");
			ingredientes.add(daoIngrediente.buscarIngredientePorId(id));

		}
		daoIngrediente.cerrarRecursos();
		return ingredientes;
	}

	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los productos de la base de datos para un ingrediente determinado
	 * @return lista con las ingredientes de la base de datos para un producto.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos.
	 */
	public List<Producto> consultarPorIngrediente(Ingrediente ingrediente) throws SQLException, Exception {
		List<Producto> productos = new ArrayList<>();

		String sql = "SELECT * FROM PERTENECE_A_PLATO WHERE ID_INGREDIENTE = " + ingrediente.getId();

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
	 * M�todo que asocia un ingrediente y un producto en la BD.
	 * @param ingrediente Ingrediente a asociar.
	 * @param producto Producto a asociar.
	 * @throws SQLException Excepci�n generada por problemas de base de datos.<br>
	 * @throws Exception Todo otro problema.
	 */
	public void asociarIngredienteYProducto(Ingrediente ingrediente, Producto producto) throws SQLException, Exception {
		String sql = "INSERT INTO PERTENECE_A_PLATO VALUES (";
		sql +=  producto.getId() + ", ";
		sql += ingrediente.getId() + ")";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina una relaci�n entre un ingrediente y producto espec�ficos.<br>
	 * @throws SQLException En caso de que se presente alg�n otro error.<br>
	 * @throws Exception Cualquier otro error de la base de datos.
	 */
	public void desasociarIngredienteYProducto(Producto producto, Ingrediente ingrediente) throws SQLException, Exception {

		String sql = "DELETE FROM PERTENECE_A_PLATO WHERE ID_INGREDIENTE = " + ingrediente.getId();
		sql += " AND ID_PRODUCTO = "+ producto.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}


	/**
	 * Elimina los ingredientes que tenga producto dado por par�metro.<br>
	 * @param producto al cual eliminar los ingredientes.<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorProducto(Producto producto) throws SQLException, Exception {
		String sql = "DELETE FROM PERTENECE_A_PLATO WHERE ID_PLATO = " + producto.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Elimina los productos del ingrediente dada en par�metro.<br>
	 * @param ingrediente ingrediente al cual eliminar los productos<br>
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarPorIngrediente (Ingrediente ingrediente) throws SQLException, Exception{
		String sql = "DELETE FROM PERTENECE_A_PLATO WHERE ID_INGREDIENTE = " + ingrediente.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
