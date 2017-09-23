
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
public class DAOTablaPreferencia {


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
	public DAOTablaPreferencia() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos las preferencias de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM PREFERENCIAS;
	 * @return Arraylist con las preferencias de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Preferencia> darPreferencias() throws SQLException, Exception {
		ArrayList<Preferencia> preferencias = new ArrayList<Preferencia>();

		String sql = "SELECT * FROM PREFERENCIA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id= rs.getLong("IDUSUARIO");
			ArrayList<Zona> zonas = obtenerZonasPorId(id);
			ArrayList<Categoria> categorias = obtenerCategoriasPorId(id);
			Double inicial = rs.getDouble("PRECIOINICIAL");
			Double finalP = rs.getDouble("PRECIOFINAL");
			preferencias.add(new Preferencia(inicial, finalP, zonas,categorias));
		}
		return preferencias;
	}
	
	/**
	 * Metodo que busca la preferencia con el id que entra como parámetro.
	 * @param id - Id de la preferencia a buscar
	 * @return Preferencia encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Preferencia buscarPreferenciaPorId(Long id) throws SQLException, Exception 
	{
		Preferencia p = null;

		String sql = "SELECT * FROM PREFERENCIA WHERE IDUSUARIO =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			ArrayList<Zona> zonas = obtenerZonasPorId(id);
			ArrayList<Categoria> categorias = obtenerCategoriasPorId(id);
			Double inicial = rs.getDouble("PRECIOINICIAL");
			Double finalP = rs.getDouble("PRECIOFINAL");
			p=(new Preferencia(inicial, finalP, zonas,categorias));
		}
		return p;
	}
	/**
	 * Metodo que busca la preferencia con el rango que entra como parámetro.
	 * @param ini - Precio inicial
	 * @param fin - Precio final
	 * @return Preferencia encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Preferencia> buscarPreferenciaPorRango(Double ini, Double fin) throws SQLException, Exception 
	{
		ArrayList<Preferencia> p = new ArrayList<>();

		String sql = "SELECT * FROM PREFERENCIA WHERE PRECIOINICIAL >=" + ini+" AND PRECIOFINAL <= "+fin;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		while(rs.next()) {
			Long id =rs.getLong("IDUSUARIO");
			ArrayList<Zona> zonas = obtenerZonasPorId(id);
			ArrayList<Categoria> categorias = obtenerCategoriasPorId(id);
			p.add(new Preferencia(ini, fin, zonas,categorias));
		}
		return p;
	}
	/**
	 * Metodo que agrega la preferencia que entra como parámetro a la base de datos.
	 * @param idUsuario- Identificación del usuario.
	 * @param p - la preferencia a agregar. p !=  null
	 * <b> post: </b> se ha agregado la preferencia a la base de datos en la transaction actual. pendiente que la preferencia master
	 * haga commit para que la preferencia baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la preferencia a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addPreferencia(Long idUsuario,Preferencia p) throws SQLException, Exception {

		verificarusuario(idUsuario);
		String sql = "INSERT INTO PREFERENCIA VALUES (";
		sql += idUsuario + ",";
		sql += p.getPrecioInicial() + ",";
		sql += p.getPrecioFinal() + ")";

		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		insertarPreferenciaCategorias(idUsuario,p);
		insertarPreferenciaZonas(idUsuario,p);
		

	}
	

	/**
	 * Metodo que actualiza la preferencia que entra como parámetro en la base de datos.
	 * @param idUsuario- Id del usuario al que hace referencia.
	 * @param video - la preferencia a actualizar. p !=  null
	 * <b> post: </b> se ha actualizado la preferencia en la base de datos en la transaction actual. pendiente que la preferencia master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la preferencia.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void actualizarPreferenciasDePrecioDeUsuario(Long idUsuario,Preferencia p) throws SQLException, Exception {

		verificarusuario(idUsuario);
		String sql = "UPDATE PREFERENCIA SET ";
		sql += "PRECIOINICIAL=" +p.getPrecioInicial()+  ",";
		sql += "PRECIOFINAL=" + p.getPrecioFinal() ;
		sql += " WHERE IDUSUARIO = " + idUsuario;

		

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
	}

	

	/**
	 * Metodo que elimina la preferencia que entra como parámetro en la base de datos.
	 * @param p - la preferencia a borrar. p !=  null
	 * @param idUsuario. Id del usuario a borrar
	 * <b> post: </b> se ha borrado la preferencia en la base de datos en la transaction actual. pendiente que la preferencia master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la preferencia.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deletePreferencia(Long idUsuario, Preferencia p) throws SQLException, Exception {

		verificarusuario(idUsuario);
		borrarPreferenciasCategoriaPorId(idUsuario);
		borrarPreferenciasZonaPorId(idUsuario);
		String sql = "DELETE FROM PREFERENCIA";
		sql += " WHERE IDUSUARIO = " + idUsuario;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Borrar las preferencias por zona con el id indicado.<br>
	 * @param idUsuario Id indicado<br>
	 * @throws SQLException En caso de que falle la base de datos.<br>
	 * @throws Exception Si algo mñas falla.
	 */
	private void borrarPreferenciasZonaPorId(Long idUsuario) throws SQLException, Exception{
		DAOTablaPreferenciaZona zonas = new DAOTablaPreferenciaZona();
		zonas.setConn(this.conn);
		zonas.borrarPorId(idUsuario);
		zonas.cerrarRecursos();
	}
	/**
	 * Borrar las preferencias por categoría con el id indicado.<br>
	 * @param idUsuario Id indicado<br>
	 * @throws SQLException En caso de que falle la base de datos.<br>
	 * @throws Exception Si algo mñas falla.
	 */
	private void borrarPreferenciasCategoriaPorId(Long idUsuario) throws SQLException, Exception{
		DAOTablaPreferenciaCategoria zonas = new DAOTablaPreferenciaCategoria();
		zonas.setConn(this.conn);
		zonas.borrarPorId(idUsuario);
		zonas.cerrarRecursos();
	}

	/**
	 * Retorna las categorías que tienen el id de preferencia indicado.<br>
	 * @param id Id de la preferencia.<br>
	 * @return Las categorías de la preferencia.
	 */
	private ArrayList<Categoria> obtenerCategoriasPorId(Long id) throws SQLException, Exception {
		DAOTablaPreferenciaCategoria pref = new DAOTablaPreferenciaCategoria();
		pref.setConn(this.conn);
		ArrayList<Categoria> c =pref.buscarCategoriaPorId(id);
		pref.cerrarRecursos();
		return c;
	}
	/**
	 * Retorna las zonas que tienen el id de preferencia indicado.<br>
	 * @param id Id de la preferencia.<br>
	 * @return Las zonas de la preferencia
	 */
	private ArrayList<Zona> obtenerZonasPorId(Long id) throws SQLException, Exception {
		DAOTablaPreferenciaZona pref = new DAOTablaPreferenciaZona();
		pref.setConn(this.conn);
		ArrayList<Zona> z = pref.buscarZonaPorId(id);
		pref.cerrarRecursos();
		return z;
	}
	
	
	/**
	 * Inserta preferencias por zona con la preferencia dada.<br>
	 * <b>pre:</b> p!=null<br>
	 * @param p Preferencia dada.<br>
	 * @throws SQLException Si hay un error en la BD.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	private void insertarPreferenciaZonas(Long idUsuario, Preferencia p) throws SQLException, Exception {

		DAOTablaPreferenciaZona zona = new DAOTablaPreferenciaZona();
		zona.setConn(conn);
		zona.insertarPreferenciasZona(idUsuario,p.getZonas());
		zona.cerrarRecursos();
	}
	/**
	 * Inserta preferencias por categoría con la preferencia dada.<br>
	 * <b>pre:</b> p!=null<br>
	 * @param idUsuario 
	 * @param p Preferencia dada.<br>
	 * @throws SQLException Si hay un error en la BD.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	private void insertarPreferenciaCategorias(Long idUsuario, Preferencia p) throws SQLException, Exception{
		DAOTablaPreferenciaCategoria zona = new DAOTablaPreferenciaCategoria();
		zona.setConn(conn);
		zona.insertarPreferenciasCategoria(idUsuario,p.getCategorias());
		zona.cerrarRecursos();		
	}
	/**
	 * Verifica que el usuario exista en la base de datos.<br>
	 * @param idUsuario Id del usuario.<br>
	 * @throws Exception Si llega a fallar cualquier cosa
	 */
	private void verificarusuario(Long idUsuario) throws Exception {
		DAOTablaUsuario usuario = new DAOTablaUsuario();
		usuario.setConn(this.conn);
		Usuario u = usuario.buscarUsuarioPorId(idUsuario);
		usuario.cerrarRecursos();
		if (u==null) throw new Exception ("No existe el usuario");
	}
}
