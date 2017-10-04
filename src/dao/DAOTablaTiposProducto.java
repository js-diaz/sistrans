
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;
import vos.Producto.TiposDePlato;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación
 * @author s.guzmanm
 */
public class DAOTablaTiposProducto {


	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOTipos_De_Plato
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaTiposProducto() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos los tipos de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM TIPOSDEPLATOS;
	 * @return Arraylist con los tipos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<TiposDePlato> darTipos_De_Platos() throws SQLException, Exception {
		ArrayList<TiposDePlato> tipos = new ArrayList<TiposDePlato>();

		String sql = "SELECT * FROM TIPOSDEPLATO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			tipos.add(convertirAPlato(name));
		}
		return tipos;
	}


	/**
	 * Metodo que busca el/los tipos con el nombre que entra como parámetro.
	 * @param name - Nombre de el/los tipos a buscar
	 * @return ArrayList con los tipos encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public TiposDePlato buscarTipos_De_PlatosPorName(String name) throws SQLException, Exception {
		ArrayList<TiposDePlato> tipos = new ArrayList<TiposDePlato>();

		String sql = "SELECT * FROM TIPOSDEPLATO WHERE NOMBRE LIKE'" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		TiposDePlato tipo = null;
		if (rs.next()) {
			String name2 = rs.getString("NOMBRE");
			tipo=convertirAPlato(name2);
		}

		return tipo;
	}
	

	/**
	 * Metodo que agrega el tipo que entra como parámetro a la base de datos.
	 * @param tipo - el tipo a agregar. tipo !=  null
	 * <b> post: </b> se ha agregado el tipo a la base de datos en la transaction actual. pendiente que el tipo master
	 * haga commit para que el tipo baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el tipo a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addTipos_De_Plato(TiposDePlato tipo) throws SQLException, Exception {

		String sql = "INSERT INTO TIPOSDEPLATO VALUES (";
		sql += "'"+ convertirPlato(tipo) + "')";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	
	/**
	 * Metodo que elimina el tipo que entra como parámetro en la base de datos.
	 * @param tipo - el tipo a borrar. tipo !=  null
	 * <b> post: </b> se ha borrado el tipo en la base de datos en la transaction actual. pendiente que el tipo master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el tipo.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteTipos_De_Plato(TiposDePlato tipo) throws SQLException, Exception {

		borrarProductosPorTipo(convertirPlato(tipo));
		String sql = "DELETE FROM TIPOSDEPLATO";
		sql += " WHERE NOMBRE LIKE '" + convertirPlato(tipo)+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Borra los productos del tipo dado por parámetro.<br>
	 * @param nombreTipo Nombre del tipo a borrar. 
	 */
	private void borrarProductosPorTipo(String nombreTipo) throws SQLException, Exception {
		DAOTablaProducto producto = new DAOTablaProducto();
		producto.setConn(this.conn);
		producto.borrarProductosPorTipo(nombreTipo);
		producto.cerrarRecursos();
	}

	/**
	 * Convierte un tipo de plato en un String.<br>
	 * @param tipo Tipo de plato.<br>
	 * @return Mensaje string
	 */
	public static String convertirPlato(TiposDePlato tipo)
	{
		if(tipo==null) return "NOEXISTE";
		switch(tipo)
		{
		case ENTRADA: return "ENTRADA";
		case PLATO_FUERTE: return "PLATO FUERTE";
		case POSTRE: return "POSTRE";
		case BEBIDA: return "BEBIDA";
		case ACOMPANAMIENTO: return "ACOMPANAMIENTO";
		default: return "NOEXISTE";
		}
	}
	/**
	 * Convierte un String en un tipo de plato.<br>
	 * @param nombreTipo Nombre del tipo a convertir.<br>
	 * @return Tipo de plato correspondiente.
	 */
	public static TiposDePlato convertirAPlato(String nombreTipo)
	{
		switch (nombreTipo)
		{
		case"ENTRADA": return TiposDePlato.ENTRADA;
		case "POSTRE": return TiposDePlato.POSTRE;
		case "PLATO FUERTE": return TiposDePlato.PLATO_FUERTE;
		case "ACOMPANAMIENTO": return TiposDePlato.ACOMPANAMIENTO;
		case "BEBIDA": return TiposDePlato.BEBIDA;
		default: return null;
		}
	}
}
