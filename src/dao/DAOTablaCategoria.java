
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación
 * @author s.guzmanm
 */
public class DAOTablaCategoria {


	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOCategoria
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaCategoria() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos las categorías de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM CATEGORIA;
	 * @return Arraylist con las categorías de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Categoria> darCategorias() throws SQLException, Exception {
		ArrayList<Categoria> categorias = new ArrayList<>();

		String sql = "SELECT * FROM CATEGORIA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			categorias.add(new Categoria(name));
		}
		return categorias;
	}


	/**
	 * Metodo que busca la categorías con el nombre que entra como parámetro.
	 * @param name - Nombre a buscar
	 * @return ArrayList con las categorías encontradas
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Categoria buscarCategoriasPorName(String name) throws SQLException, Exception {
		String sql = "SELECT * FROM CATEGORIA WHERE NOMBRE LIKE'" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		Categoria c=null;
		if (rs.next()) {
			String name2 = rs.getString("NAME");
			c=new Categoria(name2);
		}

		return c;
	}
	
	
	/**
	 * Metodo que agrega la categoría que entra como parámetro a la base de datos.
	 * @param categoria - la categoría a agregar. categoria !=  null
	 * <b> post: </b> se ha agregado el objeto a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que la categoría baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addCategoria(Categoria c) throws SQLException, Exception {

		String sql = "INSERT INTO CATEGORIA VALUES (";
		sql += c.getNombre() + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	/**
	 * Metodo que elimina la categoría que entra como parámetro en la base de datos.
	 * @param categoría - la categoría a borrar. categoria !=  null
	 * <b> post: </b> se ha borrado la categoría en la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteCategoria(Categoria c) throws SQLException, Exception {

		borrarCategoriasDePlato(c.getNombre());
		borrarCategoriasDeRestaurante(c.getNombre());
		borrarCategoriasDeMenu(c.getNombre());
		String sql = "DELETE FROM CATEGORIA";
		sql += " WHERE NOMBRE LIKE '" + c.getNombre()+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Borra todas las categorías con el nombre dado de la tabla DAOCategoriaMenu.<br>
	 * @param nombre Nombre de la categoría a eliminar.
	 */
	private void borrarCategoriasDeMenu(String nombre) throws SQLException, Exception {
		DAOTablaCategoriaMenu menu = new DAOTablaCategoriaMenu();
		menu.setConn(this.conn);
		menu.borrarCategoriasPorNombreCategoria(nombre);
		menu.cerrarRecursos();
	}
	/**
	 * Borra todas las categorías con el nombre dado de la tabla DAOCategoriaMenu.<br>
	 * @param nombre Nombre de la categoría a eliminar.
	 */
	private void borrarCategoriasDeRestaurante(String nombre) throws SQLException, Exception {
		DAOTablaCategoriaRestaurante restaurante = new DAOTablaCategoriaRestaurante();
		restaurante.setConn(this.conn);
		restaurante.borrarCategoriasPorNombreCategoria(nombre);
		restaurante.cerrarRecursos();
	}
	/**
	 * Borra todas las categorías con el nombre dado de la tabla DAOCategoriaMenu.<br>
	 * @param nombre Nombre de la categoría a eliminar.
	 */
	private void borrarCategoriasDePlato(String nombre) throws SQLException, Exception {
		DAOTablaCategoriaProducto producto = new DAOTablaCategoriaProducto();
		producto.setConn(this.conn);
		producto.borrarCategoriasPorNombreCategoria(nombre);
		producto.cerrarRecursos();
	}

}
