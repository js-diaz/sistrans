
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación
 * @author Monitores 2017-20
 */
public class DAOTablaIngrediente {


	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOIngrediente
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaIngrediente() {
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
	 * Metodo que inicializa la connection del DAO a la base de datos con la conexión que entra como parámetro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection con){
		this.conn = con;
	}


	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los ingredientes de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM INGREDIENTES;
	 * @return Arraylist con los ingredientes de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Ingrediente> darIngredientes() throws SQLException, Exception {
		ArrayList<Ingrediente> ingredientes = new ArrayList<Ingrediente>();

		String sql = "SELECT * FROM INGREDIENTE";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			String descripcion = rs.getString("DESCRIPCION");
			String traduccion= rs.getString("TRADUCCION");
			ingredientes.add(new Ingrediente(name, descripcion, traduccion, id));
		}
		return ingredientes;
	}


	
	/**
	 * Metodo que busca el ingrediente con el id que entra como parámetro.
	 * @param name - Id de el ingrediente a buscar
	 * @return Ingrediente encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Ingrediente buscarIngredientePorId(Long id) throws SQLException, Exception 
	{
		Ingrediente ingrediente = null;

		String sql = "SELECT * FROM INGREDIENTE WHERE ID =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			String name = rs.getString("NOMBRE");
			Long id2 = rs.getLong("ID");
			String descripcion = rs.getString("DESCRIPCION");
			String traduccion= rs.getString("TRADUCCION");
			ingrediente=new Ingrediente(name, descripcion, traduccion, id2);
		}

		return ingrediente;
	}

	/**
	 * Metodo que agrega el ingrediente que entra como parámetro a la base de datos.
	 * @param ingrediente - el ingrediente a agregar. ingrediente !=  null
	 * <b> post: </b> se ha agregado el ingrediente a la base de datos en la transaction actual. pendiente que el ingrediente master
	 * haga commit para que el ingrediente baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el ingrediente a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addIngrediente(Ingrediente ingrediente) throws SQLException, Exception {

		String sql = "INSERT INTO INGREDIENTE VALUES (";
		sql +=   "IDINGREDIENTE.nextval,'";
		sql += ingrediente.getNombre() + "','";
		sql+=ingrediente.getDescripcion()+"','";
		sql += ingrediente.getTraduccion() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Metodo que actualiza el ingrediente que entra como parámetro en la base de datos.
	 * @param ingrediente - el ingrediente a actualizar. ingrediente !=  null
	 * <b> post: </b> se ha actualizado el ingrediente en la base de datos en la transaction actual. pendiente que el ingrediente master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el ingrediente.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateIngrediente(Ingrediente ingrediente) throws SQLException, Exception {

		String sql = "UPDATE INGREDIENTE SET ";
		sql += "NOMBRE='" + ingrediente.getNombre() + "',";
		sql += "DESCRIPCION='" + ingrediente.getDescripcion()+"',";
		sql+="TRADUCCION='"+ingrediente.getTraduccion()+"'";
		sql += " WHERE ID = " + ingrediente.getId();


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina el ingrediente que entra como parámetro en la base de datos.
	 * @param ingrediente - el ingrediente a borrar. ingrediente !=  null
	 * <b> post: </b> se ha borrado el ingrediente en la base de datos en la transaction actual. pendiente que el ingrediente master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el ingrediente.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteIngrediente(Ingrediente ingrediente) throws SQLException, Exception {

		borrarIngredienteDeProducto(ingrediente.getId());
		
		String sql = "DELETE FROM INGREDIENTE";
		sql += " WHERE ID = " + ingrediente.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Borra todos los ingredientes de la tabla de productos con el id dado.<br>
	 * @param id Id del ingrediente a borrar.
	 */
	private void borrarIngredienteDeProducto(Long id) {
		DAOTablaPerteneceAProducto prod= new DAOTablaPerteneceAProducto();
		prod.setConn(this.conn);
		prod.borrarIngredienteDeLista(id);
		prod.cerrarRecursos();
	}

}
