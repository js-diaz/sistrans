
package dao;


import java.sql.Connection; 

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;
import vos.Usuario.Rol;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación
 * @author s.guzmanm
 */
public class DAOTablaUsuario {


	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOUsuario
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaUsuario() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos los usuarios de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM USUARIOS;
	 * @return Arraylist con los usuarios de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Usuario> darUsuarios() throws SQLException, Exception {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

		DAOTablaPreferencia pref = new DAOTablaPreferencia();
		DAOTablaCuenta hist = new DAOTablaCuenta();
		DAOTablaRestaurante rest = new DAOTablaRestaurante();
		pref.setConn(this.conn);
		hist.setConn(this.conn);
		rest.setConn(this.conn);
		
		String sql = "SELECT * FROM USUARIO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			String correo = rs.getString("CORREO");
			Rol r = convertirARol(rs.getString("ROL"));
			Preferencia p = pref.buscarPreferenciaPorId(id);
			ArrayList<Cuenta> historial=hist.buscarCuentasPorId(id);
			Restaurante restaurante = rest.darRestauranteDeUsuario(id);
			usuarios.add(new Usuario(name,id,correo,r,p,historial,restaurante));
		}
		rest.cerrarRecursos();
		hist.cerrarRecursos();
		pref.cerrarRecursos();
		return usuarios;
	}

	

	/**
	 * Metodo que busca el/los usuarios con el nombre que entra como parámetro.
	 * @param name - Nombre de el/los usuarios a buscar
	 * @return ArrayList con los usuarios encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Usuario> buscarUsuariosPorName(String nombre) throws SQLException, Exception {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

		DAOTablaPreferencia pref = new DAOTablaPreferencia();
		DAOTablaCuenta hist = new DAOTablaCuenta();
		DAOTablaRestaurante rest = new DAOTablaRestaurante();
		pref.setConn(this.conn);
		hist.setConn(this.conn);
		rest.setConn(this.conn);
		String sql = "SELECT * FROM USUARIO WHERE NOMBRE LIKE'" + nombre + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		
		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			String correo = rs.getString("CORREO");
			Rol r = convertirARol(rs.getString("ROL"));
			Preferencia p = pref.buscarPreferenciaPorId(id);
			ArrayList<Cuenta> historial=hist.buscarCuentasPorId(id);
			Restaurante restaurante = rest.darRestauranteDeUsuario(id);
			usuarios.add(new Usuario(name,id,correo,r,p,historial,restaurante));
		}

		rest.cerrarRecursos();
		hist.cerrarRecursos();
		pref.cerrarRecursos();
		return usuarios;
	}
	
	/**
	 * Metodo que busca el usuario con el id que entra como parámetro.
	 * @param name - Id de el usuario a buscar
	 * @return Usuario encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Usuario buscarUsuarioPorId(Long id) throws SQLException, Exception 
	{
		Usuario usuario = null;

		DAOTablaPreferencia pref = new DAOTablaPreferencia();
		DAOTablaCuenta hist = new DAOTablaCuenta();
		DAOTablaRestaurante rest = new DAOTablaRestaurante();
		pref.setConn(this.conn);
		hist.setConn(this.conn);
		rest.setConn(this.conn);
		
		String sql = "SELECT * FROM USUARIO WHERE ID =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			String name = rs.getString("NOMBRE");
			Long id2 = rs.getLong("ID");
			String correo = rs.getString("CORREO");
			Rol r = convertirARol(rs.getString("ROL"));
			Preferencia p = pref.buscarPreferenciaPorId(id);
			ArrayList<Cuenta> historial=hist.buscarCuentasPorId(id);
			Restaurante restaurante = rest.darRestauranteDeUsuario(id);
			usuario=(new Usuario(name,id2,correo,r,p,historial,restaurante));
		}

		rest.cerrarRecursos();
		hist.cerrarRecursos();
		pref.cerrarRecursos();
		return usuario;
	}
	

	public ArrayList<Usuario> buscarUsuarioPorRol(String nombreRol)throws SQLException, Exception {
		ArrayList<Usuario> usuario = new ArrayList<>();

		DAOTablaPreferencia pref = new DAOTablaPreferencia();
		DAOTablaCuenta hist = new DAOTablaCuenta();
		DAOTablaRestaurante rest = new DAOTablaRestaurante();
		pref.setConn(this.conn);
		hist.setConn(this.conn);
		rest.setConn(this.conn);
		
		String sql = "SELECT * FROM USUARIO WHERE ROL LIKE ='" + nombreRol+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while(rs.next()) {
			String name = rs.getString("NOMBRE");
			Long id2 = rs.getLong("ID");
			String correo = rs.getString("CORREO");
			Rol r = convertirARol(rs.getString("ROL"));
			Preferencia p = pref.buscarPreferenciaPorId(id2);
			ArrayList<Cuenta> historial=hist.buscarCuentasPorId(id2);
			Restaurante restaurante = rest.darRestauranteDeUsuario(id2);
			usuario.add(new Usuario(name,id2,correo,r,p,historial,restaurante));
		}

		rest.cerrarRecursos();
		hist.cerrarRecursos();
		pref.cerrarRecursos();
		return usuario;
	}

	/**
	 * Metodo que agrega el usuario que entra como parámetro a la base de datos.
	 * @param usuario - el usuario a agregar. usuario !=  null
	 * <b> post: </b> se ha agregado el usuario a la base de datos en la transaction actual. pendiente que el usuario master
	 * haga commit para que el usuario baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el usuario a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addUsuario(Usuario usuario) throws SQLException, Exception {

		String sql = "INSERT INTO USUARIO VALUES (";
		sql += usuario.getId() + ",'";
		sql += usuario.getCorreo() + "','";
		sql += usuario.getNombre()+"','";
		sql+=convertirRol(usuario.getRol())+ "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		DAOTablaRestaurante rest = new DAOTablaRestaurante();
		DAOTablaPreferencia pref = new DAOTablaPreferencia();
		DAOTablaCuenta cuenta = new DAOTablaCuenta();
		rest.setConn(this.conn);
		pref.setConn(this.conn);
		cuenta.setConn(this.conn);
		rest.insertarPorIdDuenho(usuario.getId(), usuario.getRestaurante());
		pref.addPreferencia(usuario.getId(), usuario.getPreferencia());
		for(Cuenta c: usuario.getHistorial())
		cuenta.addCuenta(c);
		
		pref.cerrarRecursos();
		rest.cerrarRecursos();
		cuenta.cerrarRecursos();
	}
	/**
	 * Convierte un rol en un String.<br>
	 * @param rol Rol a convertir.<br>
	 * @return Rol convertido en String.
	 */
	private String convertirRol(Rol rol) {
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
			return null;
		}
	}

	/**
	 * Metodo que actualiza el usuario que entra como parámetro en la base de datos.
	 * @param usuario - el usuario a actualizar. usuario !=  null
	 * <b> post: </b> se ha actualizado el usuario en la base de datos en la transaction actual. pendiente que el usuario master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el usuario.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateUsuario(Usuario usuario) throws SQLException, Exception {

		String sql = "UPDATE USUARIO SET ";
		sql += "NOMBRE='" + usuario.getNombre() + "',";
		sql += "CORREO='" + usuario.getCorreo()+"',";
		sql+="ROL='"+convertirRol(usuario.getRol())+"'";
		sql += " WHERE ID = " + usuario.getId();


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		
		DAOTablaRestaurante rest = new DAOTablaRestaurante();
		DAOTablaPreferencia pref = new DAOTablaPreferencia();
		DAOTablaCuenta cuenta = new DAOTablaCuenta();
		rest.setConn(this.conn);
		pref.setConn(this.conn);
		cuenta.setConn(this.conn);
		rest.actualizarRestaurantesDeUsuario(usuario.getId(),usuario.getRestaurante());
		pref.actualizarPreferenciasDeUsuario(usuario.getId(),usuario.getPreferencia());
		cuenta.actualizarCuentas(usuario.getId(),usuario.getHistorial());
		rest.cerrarRecursos();
		pref.cerrarRecursos();
		cuenta.cerrarRecursos();
	}

	/**
	 * Metodo que elimina el usuario que entra como parámetro en la base de datos.
	 * @param usuario - el usuario a borrar. usuario !=  null
	 * <b> post: </b> se ha borrado el usuario en la base de datos en la transaction actual. pendiente que el usuario master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el usuario.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteUsuario(Usuario usuario) throws SQLException, Exception {

		DAOTablaRestaurante rest = new DAOTablaRestaurante();
		DAOTablaPreferencia pref = new DAOTablaPreferencia();
		DAOTablaCuenta cuenta = new DAOTablaCuenta();
		rest.setConn(this.conn);
		pref.setConn(this.conn);
		cuenta.setConn(this.conn);
		
		rest.borrarRestaurantePorId(usuario.getId());
		pref.deletePreferencia(usuario.getId(), usuario.getPreferencia());
		cuenta.borrarHistorialCliente(usuario.getId());
		
		String sql = "DELETE FROM USUARIO";
		sql += " WHERE ID = " + usuario.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		cuenta.cerrarRecursos();
		pref.cerrarRecursos();
		rest.cerrarRecursos();
	}
	/**
	 * Borrar usuario por rol.<br>
	 * @param nombreRol Borra todos los usuarios con el rol dado.<br>
	 * @throws SQLException Excepción de la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public void borrarPorRol(String nombreRol) throws SQLException, Exception {
		ArrayList<Usuario> u=buscarUsuarioPorRol(nombreRol);
		for(Usuario usu: u)
		{
			deleteUsuario(usu);
		}
	}
	
	/**
	 * Convierte el rol pasado como parámetro a un String.<br>
	 * @param nombreRol Nombre del rol.<br>
	 * @return Equivalencia en String
	 */
	private Rol convertirARol(String nombreRol) {
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

}
