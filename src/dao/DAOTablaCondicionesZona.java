
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
public class DAOTablaCondicionesZona {


	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOVideo
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaCondicionesZona() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos las condiciones de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM CONDICIONZONAS;
	 * @return Arraylist con los condiciones de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<CondicionTecnica> consultarZona(String nombreZona) throws SQLException, Exception {
		ArrayList<CondicionTecnica> condiciones = new ArrayList<>();

		String sql = "SELECT * FROM CONDICIONZONA WHERE ZONANOMBRE LIKE '"+nombreZona+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			String name = rs.getString("CONDICIONTECNICANOMBRE");
			condiciones.add(new CondicionTecnica(name));
		}
		return condiciones;
	}

	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos las condiciones de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM CONDICIONZONAS;
	 * @return Arraylist con los condiciones de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Zona> consultarPorCondicion(String nombreCondicion) throws SQLException, Exception {
		ArrayList<Zona> condiciones = new ArrayList<>();

		String sql = "SELECT * FROM CONDICIONZONA WHERE CONDICIONTECNICANOMBRE LIKE '"+nombreCondicion+"'";

		DAOTablaZona zona= new DAOTablaZona();
		zona.setConn(conn);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			String name = rs.getString("ZONANOMBRE");
			condiciones.add(zona.buscarZonasPorName(name));
		}
		zona.cerrarRecursos();
		return condiciones;
	}
	/**
	 * Método que inserta todas las condiciones de una zona particular en la base de datos.<br>
	 * @param nombreZona Nombre de la zona no nula.<br>
	 * @param condiciones Lista de condiciones no nulas. <br>
	 * * <b> post: </b> se ha agregado la condición a la base de datos en la transaction actual. pendiente que la  master
	 * haga commit para que la condición baje  a la base de datos.
	 * @throws SQLException Excepción generada por problemas de base de datos.<br>
	 * @throws Exception Todo otro problema.
	 */
	public void insertarPorZona(String nombreZona, List<CondicionTecnica> condiciones) throws SQLException, Exception {
		String sql="";
		PreparedStatement prepStmt = null;
		for (CondicionTecnica c: condiciones)
		{
			sql="INSERT INTO CONDICIONZONA VALUES (";
			sql+="'"+c.getNombre()+"',";
			sql+="'"+nombreZona+"'";
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}
	}
	/**
	 * Método que inserta todas las zoans de una condición particular en la base de datos.<br>
	 * @param nombre Nombre de la condición a insertar.<br>
	 * @param zonas Lista de zonas no nulas. <br>
	 * * <b> post: </b> se ha agregado la condición a la base de datos en la transaction actual. pendiente que la  master
	 * haga commit para que la condición baje  a la base de datos.
	 * @throws SQLException Excepción generada por problemas de base de datos.<br>
	 * @throws Exception Todo otro problema.
	 */
	public void insertarPorCondicion(String nombre, List<Zona> zonas) throws SQLException, Exception {
		String sql="";
		PreparedStatement prepStmt = null;
		for (Zona z: zonas)
		{
			sql="INSERT INTO CONDICIONZONA VALUES (";
			sql+="'"+nombre+"',";
			sql+="'"+z.getNombre()+"'";
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}
	}
	/**
	 * Método que inserta todas las zoans de una condición particular en la base de datos.<br>
	 * @param nombre Nombre de la condición no nulo.<br>
	 * @param nombreZona Nombre de la zona no nulo. <br>
	 * * <b> post: </b> se ha agregado la condición a la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que la condición baje  a la base de datos.
	 * @throws SQLException Excepción generada por problemas de base de datos.<br>
	 * @throws Exception Todo otro problema.
	 */
	public void insertarPorCondicionYZona(String nombreCondicion, String nombreZona ) throws SQLException, Exception {
		String sql="";
		PreparedStatement prepStmt = null;
			sql="INSERT INTO CONDICIONZONA VALUES (";
			sql+="'"+nombreCondicion+"',";
			sql+="'"+nombreZona+"'";
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		
	}
	
	/**
	 * Método que elimina una relación entre una condición y zonas específicas.<br>
	 * @param nombreZona Nombre de la zona diferente de nulo.<br>
	 * @param nombreCondicion Nombre de la condición diferente de nulo. <br>
	 * <b> post: </b> se ha borrado la zona en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException En caso de que se presente algún otro error.<br>
	 * @throws Exception Cualquier otro error de la base de datos.
	 */
	public void eliminarCondicion(String nombreZona, String nombreCondicion) throws SQLException, Exception {

		String sql = "DELETE FROM CONDICIONZONA";
		sql += " WHERE ZONANOMBRE LIKE '" + nombreZona+"' AND ";
		sql+=" CONDICIONZONANOMBRE LIKE '"+ nombreCondicion+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	
	/**
	 * Elimina las condiciones por zona dada en parámetro.<br>
	 * @param nombreZona Nombre de la zona a eliminar no nulo.<br>
	 * <b> post: </b> se ha borrado la zona en la base de datos en la transaction actual. pendiente que la  master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarCondicionesPorZona(String nombreZona) throws SQLException, Exception {
		String sql ="DELETE FROM CONDICIONZONA";
		sql+=" WHERE ZONANOMBRE LIKE '"+nombreZona+"'";
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Elimina las condiciones por nombreDeCondicion dada en parámetro.<br>
	 * @param nombreZona Nombre de la zona a eliminar diferente de nulo.<br>
	 * <b> post: </b> se ha borrado la zona en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException Si se lanza algo relacionado con la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void eliminarCondicionesPorCondicion (String condicionTecnica) throws SQLException, Exception{
		String sql ="DELETE FROM CONDICIONZONA";
		sql+=" WHERE CONDICIONTECNICANOMBRE LIKE '"+condicionTecnica+"'";
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
