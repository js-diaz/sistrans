
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
public class DAOTablaCondiciones {


	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOCondicionTecnica
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaCondiciones() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos los condicions de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM CONDICIONTECNICAS;
	 * @return Arraylist con los condicions de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<CondicionTecnica> darCondicionTecnicas() throws SQLException, Exception {
		ArrayList<CondicionTecnica> condicions = new ArrayList<CondicionTecnica>();

		String sql = "SELECT * FROM CONDICIONTECNICA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			condicions.add(new CondicionTecnica( name));
		}
		return condicions;
	}


	/**
	 * Metodo que busca el/los condicions con el nombre que entra como parámetro.
	 * @param name - Nombre de el/los condicions a buscar
	 * @return ArrayList con los condicions encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public CondicionTecnica buscarCondicionTecnicasPorName(String name) throws SQLException, Exception {

		String sql = "SELECT * FROM CONDICIONTECNICA WHERE NOMBRE LIKE'" + name + "'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		CondicionTecnica c= null;
		if (rs.next()) {
			String name2 = rs.getString("NOMBRE");
			c= new CondicionTecnica(name2);
		}
		return c;
	}

	/**
	 * Metodo que agrega la condición que entra como parámetro a la base de datos.
	 * @param condicion - la condición a agregar. condicion !=  null
	 * <b> post: </b> se ha agregado la condición a la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que la condición baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la condición a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addCondicionTecnica(CondicionTecnica condicion) throws SQLException, Exception {

		String sql = "INSERT INTO CONDICIONTECNICA VALUES ('";
		sql += condicion.getNombre() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	
	/**
	 * Metodo que elimina la condición que entra como parámetro en la base de datos.
	 * @param condicion - la condición a borrar. condicion !=  null
	 * <b> post: </b> se ha borrado la condición en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la condición.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteCondicionTecnica(CondicionTecnica condicion) throws SQLException, Exception {

		borrarCondicionesZona(condicion.getNombre());
		String sql = "DELETE FROM CONDICIONTECNICA";
		sql += " WHERE NOMBRE LIKE '" + condicion.getNombre()+"'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Borra las condiciones en la tabla COndicionZona.<br>
	 * @param nombre Nombre de la condición a borrar.<br>
	 */
	private void borrarCondicionesZona(String nombre) throws SQLException, Exception {
		DAOTablaCondicionesZona cond = new DAOTablaCondicionesZona();
		cond.setConn(this.conn);
		cond.eliminarCondicionesPorCondicion(nombre);
		cond.cerrarRecursos();
	}

}
