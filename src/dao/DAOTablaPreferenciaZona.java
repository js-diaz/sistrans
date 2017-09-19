
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
public class DAOTablaPreferenciaZona {


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
	public DAOTablaPreferenciaZona() {
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
	public ArrayList<Zona> buscarZonaPorId(Long id) throws SQLException, Exception {
		ArrayList<Zona> preferencias = new ArrayList<>();

		String sql = "SELECT * FROM PREFERENCIAZONA WHERE IDUSUARIO ="+id;

		DAOTablaZona zona = new DAOTablaZona();
		zona.setConn(conn);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			String name = rs.getString("NOMBREZONA");
			preferencias.add(zona.buscarZonasPorName(name) );
		}
		zona.cerrarRecursos();
		return preferencias;
	}
	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los preferencias con zona dada de la base de datos
	 * @param cat Categoría
	 * @return Arraylist con los preferencias de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Preferencia> buscarZonaPorZona(Zona cat) throws SQLException, Exception {
		ArrayList<Preferencia> preferencias = new ArrayList<>();

		String sql = "SELECT * FROM PREFERENCIAZONA WHERE NOMBRECATEGORIA LIKE '"+cat.getNombre()+"'";

		DAOTablaPreferencia zona = new DAOTablaPreferencia();
		zona.setConn(conn);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			Long id = rs.getLong("IDUSUARIO");
			preferencias.add(zona.buscarPreferenciaPorId(id) );
		}
		zona.cerrarRecursos();
		return preferencias;
	}
	/**
	 * Inserta preferencias por el id de Usuario, teniendo una lista de zonas.<br>
	 * @param idUsuario Id del usuario.<br>
	 * @param zonas Listado de zonas.<br>
	 * @throws SQLException Si hay un error en la base de datos.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void insertarPreferenciasZona(Long idUsuario, List<Zona> zonas) throws SQLException, Exception {
		String sql ="";
		for(Zona c: zonas)
		{
			sql = "INSERT INTO PREFERENCIAZONA VALUES (";
			sql += idUsuario+ ",'";
			sql += c.getNombre() + ")";
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}
	}
	//DEBERÍA?
	public void actualizarPreferenciaPorId(Long idUsuario, List<Zona> zonas) throws SQLException, Exception{
		// TODO Auto-generated method stub
		ArrayList<Zona> cat =buscarZonaPorId(idUsuario);
		boolean encontrado=false;
		for(Zona c: zonas)
		{
			encontrado=false;
			for(Zona d: cat)
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
	 * Metodo que elimina la preferencia por zona que entra como parámetro en la base de datos.
	 * @param idUsuario - Id de la preferencia por zona a borrar. preferencia !=  null
	 * <b> post: </b> se ha borrado la preferencia por zona en la base de datos en la transaction actual. pendiente que la preferencia por zona master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la preferencia por zona.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */	
	public void borrarPorId(Long idUsuario) throws SQLException, Exception {
		
		String sql = "DELETE FROM PREFERENCIAZONA";
		sql += " WHERE IDUSUARIO = " + idUsuario;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();		
	}
	
	/**
	 * Metodo que elimina la preferencia por zona que entra como parámetro en la base de datos.
	 * @param id - Nombre de la zona a borrar. preferencia !=  null
	 * <b> post: </b> se ha borrado la preferencia por zona en la base de datos en la transaction actual. pendiente que la preferencia por zona master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la preferencia por zona.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */	
	public void modificarPorZonaEliminada(String id) throws SQLException, Exception {
		
		String sql = "DELETE FROM PREFERENCIAZONA";
		sql += " WHERE NOMBREZONA LIKE' " + id+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();		
	}
	
	/**
	 * Metodo que elimina la preferencia por zona que entra como parámetro en la base de datos.
	 * @param idUusario - Id de la preferencia por zona a borrar. preferencia !=  null
	 * @param id - Nombre de la zona a borrar. preferencia !=  null
	 * <b> post: </b> se ha borrado la preferencia por zona en la base de datos en la transaction actual. pendiente que la preferencia por zona master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la preferencia por zona.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */	
	public void borrarMulticriterio(Long idUsuario, String id) throws SQLException, Exception {
		
		String sql = "DELETE FROM PREFERENCIAZONA";
		sql += " WHERE IDUSUARIO = " + idUsuario +" AND NOMBREZONA LIKE '"+id+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();		
	}


}
