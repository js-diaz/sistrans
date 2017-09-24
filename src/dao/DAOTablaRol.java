
package dao;


import java.sql.Connection; 

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;
import vos.UsuarioMinimum.Rol;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación
 * @author Monitores 2017-20
 */
public class DAOTablaRol {


	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAORol
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaRol() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos los rols de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM ROL;
	 * @return Arraylist con los rols de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Rol> darRols() throws SQLException, Exception {
		ArrayList<Rol> rols = new ArrayList<Rol>();

		String sql = "SELECT * FROM ROL";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			
			rols.add(buscarRol(rs.getString("NOMBRE")));
		}
		return rols;
	}

	

	/**
	 * Metodo que busca el/los rols con el nombre que entra como parámetro.
	 * @param name - Nombre de el/los rols a buscar
	 * @return ArrayList con los rols encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Rol buscarRolsPorName(String name) throws SQLException, Exception {

		String sql = "SELECT * FROM ROL WHERE NOMBRE LIKE'" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		Rol rol = null;
		if (rs.next()) {
			String name2 = rs.getString("NOMBRE");
			rol=buscarRol(name2);
		}

		return rol;
	}
	/**
	 * Metodo que agrega el rol que entra como parámetro a la base de datos.
	 * @param rol - el rol a agregar. rol !=  null
	 * <b> post: </b> se ha agregado el rol a la base de datos en la transaction actual. pendiente que el rol master
	 * haga commit para que el rol baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el rol a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addRol(Rol rol) throws SQLException, Exception {

		String sql = "INSERT INTO ROL VALUES (";
		sql += convertirRol(rol) + ")";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina el rol que entra como parámetro en la base de datos.
	 * @param rol - el rol a borrar. rol !=  null
	 * <b> post: </b> se ha borrado el rol en la base de datos en la transaction actual. pendiente que el rol master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el rol.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteRol(Rol rol) throws SQLException, Exception {

		borrarUsuariosConRol(convertirRol(rol));
		String sql = "DELETE FROM ROL";
		sql += " WHERE NOMBRE LIKE '" + convertirRol(rol)+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Borra los usuarios que tienen este rol en el sistema.<br>
	 * @param nombreRol Rol en String
	 */
	private void borrarUsuariosConRol(String nombreRol) throws SQLException, Exception {
		DAOTablaUsuario usuario = new DAOTablaUsuario();
		usuario.setConn(this.conn);
		usuario.borrarPorRol(nombreRol);
		usuario.cerrarRecursos();
	}

	/**
	 * Método que permite transofrmar un parámetro String en un rol.<br>
	 * @param nombreRol Nombre del rol a convertir.<br>
	 * @return Retorna el rol.
	 */
	public static Rol buscarRol(String nombreRol) {
		switch(nombreRol)
		{
		case "LOCAL": return Rol.LOCAL;
		case "CLIENTE": return Rol.CLIENTE;
		case "OPERADOR": return Rol.OPERADOR;
		case "PROVEEDOR": return Rol.PROVEEDOR;
		case "ORGANIZADORES": return Rol.ORGANIZADORES;
		default: return null;
		}
	}
	/**
	 * Método que convierte un rol en un String.<br>
	 * @param rol Rol a convertir.<br>
	 * @return Retorna el rol convertido a String-
	 */
	public static String convertirRol(Rol rol)
	{
		if(rol==null) return "NOEXISTE";
		switch(rol)
		{
		case LOCAL: return "LOCAL";
		case CLIENTE:
			return "CLIENTE";
		case OPERADOR:
			return "OPERADOR";
		case ORGANIZADORES:
			return "ORGANIZADORES";
		case PROVEEDOR:
			return "PROVEEDOR";
		default:
			return "NOEXISTE";
		}
	}
}
