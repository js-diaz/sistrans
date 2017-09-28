
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación utilizando todos los criterios para requerimientos de búsqueda.
 * @author s.guzmanm
 */
public class DAOTablaCriterio {


	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOCriterio
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaCriterio() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos los criterios de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM CRITERIO;
	 * @return Arraylist con los criterios de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Criterio> darCriteriosOrganizacionZona() throws SQLException, Exception {
		ArrayList<Criterio> categorias = new ArrayList<>();

		String sql = "SELECT * FROM CRITERIOORGZONA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			categorias.add(new Criterio(name));
		}
		return categorias;
	}


	/**
	 * Metodo que busca el criterios con el nombre que entra como parámetro.
	 * @param name - Nombre a buscar
	 * @return ArrayList con los criterios encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Criterio buscarCriteriosOrgZonaPorName(String name) throws SQLException, Exception {
		String sql = "SELECT * FROM CRITERIOORGZONA WHERE NOMBRE LIKE'" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		Criterio c=null;
		if (rs.next()) {
			String name2 = rs.getString("NOMBRE");
			c=new Criterio(name2);
		}

		return c;
	}
	
	
	/**
	 * Metodo que agrega el criterio que entra como parámetro a la base de datos.
	 * @param categoria - el criterio a agregar. categoria !=  null
	 * <b> post: </b> se ha agregado el objeto a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que el criterio baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addCriterioOrgZona(Criterio c) throws SQLException, Exception {

		String sql = "INSERT INTO CRITERIOORGZONA VALUES ('";
		sql += c.getNombre() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	/**
	 * Metodo que elimina el criterio que entra como parámetro en la base de datos.
	 * @param categoría - el criterio a borrar. categoria !=  null
	 * <b> post: </b> se ha borrado el criterio en la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteCriterioOrgZona(Criterio c) throws SQLException, Exception {

		String sql = "DELETE FROM CRITERIOORGZONA";
		sql += " WHERE NOMBRE LIKE '" + c.getNombre()+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los criterios de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM CRITERIO;
	 * @return Arraylist con los criterios de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Criterio> darCriteriosAgrupamientoZona() throws SQLException, Exception {
		ArrayList<Criterio> categorias = new ArrayList<>();

		String sql = "SELECT * FROM CRITERIOGRUZONA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			categorias.add(new Criterio(name));
		}
		return categorias;
	}


	/**
	 * Metodo que busca el criterios con el nombre que entra como parámetro.
	 * @param name - Nombre a buscar
	 * @return ArrayList con los criterios encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Criterio buscarCriteriosAgrupamientoZonaPorName(String name) throws SQLException, Exception {
		String sql = "SELECT * FROM CRITERIOGRUZONA WHERE NOMBRE LIKE'" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		Criterio c=null;
		if (rs.next()) {
			String name2 = rs.getString("NOMBRE");
			c=new Criterio(name2);
		}

		return c;
	}
	
	
	/**
	 * Metodo que agrega el criterio que entra como parámetro a la base de datos.
	 * @param categoria - el criterio a agregar. categoria !=  null
	 * <b> post: </b> se ha agregado el objeto a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que el criterio baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addCriterioAgrupamientoZona(Criterio c) throws SQLException, Exception {

		String sql = "INSERT INTO CRITERIOGRUZONA VALUES ('";
		sql += c.getNombre() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	/**
	 * Metodo que elimina el criterio que entra como parámetro en la base de datos.
	 * @param categoría - el criterio a borrar. categoria !=  null
	 * <b> post: </b> se ha borrado el criterio en la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteCriterioAgrupamientoZona(Criterio c) throws SQLException, Exception {

		String sql = "DELETE FROM CRITERIOGRUZONA";
		sql += " WHERE NOMBRE LIKE '" + c.getNombre()+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los criterios de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM CRITERIO;
	 * @return Arraylist con los criterios de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Criterio> darCriteriosOrdenProducto() throws SQLException, Exception {
		ArrayList<Criterio> categorias = new ArrayList<>();

		String sql = "SELECT * FROM CRITERIOORGPROD";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			categorias.add(new Criterio(name));
		}
		return categorias;
	}


	/**
	 * Metodo que busca el criterios con el nombre que entra como parámetro.
	 * @param name - Nombre a buscar
	 * @return ArrayList con los criterios encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Criterio buscarCriteriosOrganizacionProductoPorName(String name) throws SQLException, Exception {
		String sql = "SELECT * FROM CRITERIOORGPROD WHERE NOMBRE LIKE'" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		Criterio c=null;
		if (rs.next()) {
			String name2 = rs.getString("NOMBRE");
			c=new Criterio(name2);
		}

		return c;
	}
	
	
	/**
	 * Metodo que agrega el criterio que entra como parámetro a la base de datos.
	 * @param categoria - el criterio a agregar. categoria !=  null
	 * <b> post: </b> se ha agregado el objeto a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que el criterio baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addCriterioOrganizacionProducto(Criterio c) throws SQLException, Exception {

		String sql = "INSERT INTO CRITERIOORGPROD VALUES ('";
		sql += c.getNombre() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	/**
	 * Metodo que elimina el criterio que entra como parámetro en la base de datos.
	 * @param categoría - el criterio a borrar. categoria !=  null
	 * <b> post: </b> se ha borrado el criterio en la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteCriterioOrganizacionProducto(Criterio c) throws SQLException, Exception {

		String sql = "DELETE FROM CRITERIOORGPROD";
		sql += " WHERE NOMBRE LIKE '" + c.getNombre()+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los criterios de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM CRITERIO;
	 * @return Arraylist con los criterios de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Criterio> darCriteriosAgrupamientoProducto() throws SQLException, Exception {
		ArrayList<Criterio> categorias = new ArrayList<>();

		String sql = "SELECT * FROM CRITERIOGRUPPROD";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			categorias.add(new Criterio(name));
		}
		return categorias;
	}


	/**
	 * Metodo que busca el criterios con el nombre que entra como parámetro.
	 * @param name - Nombre a buscar
	 * @return ArrayList con los criterios encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Criterio buscarCriteriosAgrupamiProductoPorName(String name) throws SQLException, Exception {
		String sql = "SELECT * FROM CRITERIOGRUPPROD WHERE NOMBRE LIKE'" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		Criterio c=null;
		if (rs.next()) {
			String name2 = rs.getString("NOMBRE");
			c=new Criterio(name2);
		}

		return c;
	}
	
	
	/**
	 * Metodo que agrega el criterio que entra como parámetro a la base de datos.
	 * @param categoria - el criterio a agregar. categoria !=  null
	 * <b> post: </b> se ha agregado el objeto a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que el criterio baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addCriterioAgrupamientoProd(Criterio c) throws SQLException, Exception {

		String sql = "INSERT INTO CRITERIOGRUPPROD VALUES ('";
		sql += c.getNombre() + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	/**
	 * Metodo que elimina el criterio que entra como parámetro en la base de datos.
	 * @param categoría - el criterio a borrar. categoria !=  null
	 * <b> post: </b> se ha borrado el criterio en la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteCriterioAgrupamientoProd(Criterio c) throws SQLException, Exception {

		String sql = "DELETE FROM CRITERIOGRUPPROD";
		sql += " WHERE NOMBRE LIKE '" + c.getNombre()+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public List<Zona> generarListaFiltradaZonas(List<Criterio> criteriosOrganizacion,
			List<Criterio> criteriosAgrupamiento) throws SQLException, Exception {
		List<Criterio> existentesOrd=new ArrayList<>();
		List<Criterio> existentesAgrup= new ArrayList<>();
		List<Zona> lista= new ArrayList<>();
		for(Criterio c: criteriosOrganizacion)
		{
			if(existentesOrd.indexOf(c)>=0) continue;
			existentesOrd.add(c);
		}
		for(Criterio c: criteriosAgrupamiento)
		{
			if(existentesAgrup.indexOf(c)>=0) continue;
			existentesAgrup.add(c);
		}
		String sql="SELECT * FROM ZONA Z";
		if(existentesAgrup.size()>0)
		{
			sql+="GROUP BY "+existentesAgrup.get(0).getNombre();
			existentesAgrup.remove(0);
			for(Criterio c: existentesAgrup)
				sql+=", "+c.getNombre();
		}
		if(existentesOrd.size()>0)
		{
			sql+="GROUP BY "+existentesOrd.get(0).getNombre();
			existentesOrd.remove(0);
			for(Criterio c: existentesOrd)
				sql+=", "+c.getNombre();
		}
		
		PreparedStatement prep =conn.prepareStatement(sql);
		recursos.add(prep);
		ResultSet r =prep.executeQuery();
		DAOTablaZona z = new DAOTablaZona();
		z.setConn(this.conn);
		lista=z.convertirEntidadZona(r);
		z.cerrarRecursos();
		return lista;
	}
	

}
