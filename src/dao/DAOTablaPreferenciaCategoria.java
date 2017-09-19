
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
 * @author Monitores 2017-20
 */
public class DAOTablaPreferenciaCategoria {


	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOPreferencia
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaPreferenciaCategoria() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos los preferencias con id dado de la base de datos
	 * @param id Id del usuario
	 * @return Arraylist con los preferencias de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Categoria> buscarCategoriaPorId(Long id) throws SQLException, Exception {
		ArrayList<Categoria> preferencias = new ArrayList<Categoria>();

		String sql = "SELECT * FROM PREFERENCIACATEGORIA WHERE IDUSUARIO ="+id;

		DAOTablaCategoria categoria = new DAOTablaCategoria();
		categoria.setConn(conn);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			String name = rs.getString("NOMBRECATEGORIA");
			preferencias.add(categoria.buscarCategoriasPorName(name) );
		}
		categoria.cerrarRecursos();
		return preferencias;
	}
	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los preferencias con categoría dada de la base de datos
	 * @param cat Categoría
	 * @return Arraylist con los preferencias de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Preferencia> buscarCategoriaPorCategoria(Categoria cat) throws SQLException, Exception {
		ArrayList<Preferencia> preferencias = new ArrayList<>();

		String sql = "SELECT * FROM PREFERENCIACATEGORIA WHERE NOMBRECATEGORIA LIKE '"+cat.getNombre()+"'";

		DAOTablaPreferencia categoria = new DAOTablaPreferencia();
		categoria.setConn(conn);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			Long id = rs.getLong("IDUSUARIO");
			preferencias.add(categoria.buscarPreferenciaPorId(id) );
		}
		categoria.cerrarRecursos();
		return preferencias;
	}
	/**
	 * Inserta preferencias por el id de Usuario, teniendo una lista de categorías.<br>
	 * @param idUsuario Id del usuario.<br>
	 * @param categorias Listado de categorias.<br>
	 * @throws SQLException Si hay un error en la base de datos.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void insertarPreferenciasCategoria(Long idUsuario, List<Categoria> categorias) throws SQLException, Exception {
		String sql ="";
		for(Categoria c: categorias)
		{
			sql = "INSERT INTO PREFERENCIACATEGORIA VALUES (";
			sql += idUsuario+ ",'";
			sql += c.getNombre() + ")";
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}
	}
	//DEBERÍA?
	public void actualizarPreferenciaPorId(Long idUsuario, List<Categoria> categorias) throws SQLException, Exception{
		// TODO Auto-generated method stub
		ArrayList<Categoria> cat =buscarCategoriaPorId(idUsuario);
		boolean encontrado=false;
		for(Categoria c: categorias)
		{
			encontrado=false;
			for(Categoria d: cat)
			{
				if(c.getNombre().equals(d.getNombre())) 
					{
						encontrado=true;
						break;
					}
			}
			if(!encontrado)
			{
				
			}
		}
	}

	/**
	 * Metodo que elimina la preferencia por categoría que entra como parámetro en la base de datos.
	 * @param idUsuario - Id de la preferencia por categoría a borrar. preferencia !=  null
	 * <b> post: </b> se ha borrado la preferencia por categoría en la base de datos en la transaction actual. pendiente que la preferencia por categoría master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la preferencia por categoría.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */	
	public void borrarPorId(Long idUsuario) throws SQLException, Exception {
		
		String sql = "DELETE FROM PREFERENCIACATEGORIA";
		sql += " WHERE IDUSUARIO = " + idUsuario;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();		
	}
	
	/**
	 * Metodo que elimina la preferencia por categoría que entra como parámetro en la base de datos.
	 * @param id - Nombre de la categoría a borrar. preferencia !=  null
	 * <b> post: </b> se ha borrado la preferencia por categoría en la base de datos en la transaction actual. pendiente que la preferencia por categoría master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la preferencia por categoría.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */	
	public void borrarPorCategoria(String id) throws SQLException, Exception {
		
		String sql = "DELETE FROM PREFERENCIACATEGORIA";
		sql += " WHERE NOMBRECATEGORIA LIKE' " + id+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();		
	}
	
	/**
	 * Metodo que elimina la preferencia por categoría que entra como parámetro en la base de datos.
	 * @param idUusario - Id de la preferencia por categoría a borrar. preferencia !=  null
	 * @param id - Nombre de la categoría a borrar. preferencia !=  null
	 * <b> post: </b> se ha borrado la preferencia por categoría en la base de datos en la transaction actual. pendiente que la preferencia por categoría master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la preferencia por categoría.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */	
	public void borrarMulticriterio(Long idUsuario, String id) throws SQLException, Exception {
		
		String sql = "DELETE FROM PREFERENCIACATEGORIA";
		sql += " WHERE IDUSUARIO = " + idUsuario +" AND NOMBRECATEGORIA LIKE '"+id+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();		
	}

	

	

	


}
