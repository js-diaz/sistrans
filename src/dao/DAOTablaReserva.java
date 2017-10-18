package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vos.MenuMinimum;
import vos.UsuarioMinimum;
import vos.ZonaMinimum;
import vos.Reserva;

public class DAOTablaReserva {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOReserva
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaReserva() {
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
	public void setConn(Connection conn){
		this.conn = conn;
	}


	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los reservas de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM RESERVAS;
	 * @return Arraylist con los reservas de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Reserva> darReservas() throws SQLException, Exception {

		String sql = "SELECT * FROM RESERVA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadReserva(rs);
	}

	/**
	 * Metodo que, usando la conexi�n a la base de datos, saca todos los reservas de la base de datos para un usuario particular.
	 * @param usuario nombre del Usuario.
	 * @return Arraylist con los reservas de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Reserva> darReservasPorUsuario(Long usuario) throws SQLException, Exception {

		String sql = "SELECT * FROM RESERVA WHERE ID_RESERVADOR = " + usuario;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadReserva(rs);
	}

	/**
	 * Metodo que busca el/los reservas con el nombre que entra como parametro.
	 * @param name - Nombre de el reserva a buscar
	 * @param reservador - Nombre del reservador al que pertenece
	 * @return ArrayList con los reservas encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Reserva buscarReservasPorFechaYUsuario(Date fecha, Long reservador) throws SQLException, Exception {

		String sql = "SELECT * FROM RESERVA WHERE FECHA = " + dateFormat(fecha) + " AND ID_RESERVADOR = " + reservador;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		ArrayList<Reserva> reservas = convertirEntidadReserva(rs);
		return reservas.get(0);
	}

	/**
	 * Metodo que agrega la reserva que entra como parametro a la base de datos.
	 * @param reserva - la reserva a agregar. reserva !=  null
	 * <b> post: </b> se ha agregado la reserva a la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que la reserva baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la reserva a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addReserva(Reserva reserva) throws SQLException, Exception {

		String sql = "INSERT INTO RESERVA VALUES (";
		sql += 	dateFormat(reserva.getFecha()) + ", ";
		sql += reserva.getReservador().getId() + ", ";
		sql += "" + reserva.getPersonas() + ", ";
		sql += "'" + reserva.getZona().getNombre() + "', ";
		sql += reserva.getMenu() == null? "NULL, " : "'" + reserva.getMenu().getNombre() + "', ";
		sql += reserva.getMenu() == null? "NULL)" : "'" + reserva.getMenu().getRestaurante().getNombre() + "')";
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	/**
	 * Formatea el valor de la cuenta al dado en la base de datos.<br>
	 * @param fecha Fecha de la cuenta.<br>
	 * @return Valor a insertar en la base de datos
	 */
	private String dateFormat(Date fecha) {
		SimpleDateFormat x = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return "TO_DATE('"+x.format(fecha)+"','yyyy-MM-dd hh24:mi:ss')";
	}

	/**
	 * Metodo que actualiza la reserva que entra como parámetro en la base de datos.
	 * @param reserva - la reserva a actualizar. reserva !=  null
	 * <b> post: </b> se ha actualizado la reserva en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la reserva.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateReserva(Reserva reserva) throws SQLException, Exception {

		String sql = "UPDATE RESERVA SET ";
		sql += "NUM_PERSONAS = " + reserva.getPersonas() + ", ";
		sql += "NOMBRE_ZONA = '" + reserva.getZona().getNombre() + "', ";
		sql += "NOMBRE_MENU = " + reserva.getMenu() == null? "NULL, " : "'" + reserva.getMenu().getNombre() + "'";
		sql += "NOMBRE_RESTAURANTE = " + reserva.getMenu() == null? "NULL, " : "'" + reserva.getMenu().getRestaurante().getNombre() + "'";
		sql += " WHERE FECHA = TO_DATE(" + dateFormat(reserva.getFecha()) + ") AND ID_RESERVADOR = " + reserva.getReservador().getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina la reserva que entra como parametro en la base de datos.
	 * @param reserva - la reserva a borrar. reserva !=  null
	 * <b> post: </b> se ha borrado la reserva en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la reserva.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteReserva(Reserva reserva) throws SQLException, Exception {

		String sql = "DELETE FROM RESERVA";
		sql += " WHERE FECHA = TO_DATE(" + dateFormat(reserva.getFecha()) + ") AND ID_RESERVADOR = " + reserva.getReservador().getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	/**
	 * Crea un arreglo de reservas con el set de resultados pasado por par�metro.<br>
	 * @param rs Set de resultados.<br>
	 * @return reservas Lista de reservas convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	private ArrayList<Reserva> convertirEntidadReserva(ResultSet rs) throws SQLException, Exception
	{
		DAOTablaUsuario daoUsuario = new DAOTablaUsuario();
		DAOTablaZona daoZona = new DAOTablaZona();
		DAOTablaMenu daoMenu = new DAOTablaMenu();
		daoUsuario.setConn(conn);
		daoZona.setConn(conn);
		daoMenu.setConn(conn);
		ArrayList<Reserva> reservas = new ArrayList<>();
		while (rs.next()) {
			Date fecha = rs.getDate("FECHA");
			int personas = rs.getInt("NUM_PERSONAS");
			UsuarioMinimum reservador = daoUsuario.buscarUsuarioMinimumPorId(rs.getLong("ID_RESERVADOR"));
			ZonaMinimum zona = daoZona.buscarZonasMinimumPorName(rs.getString("NOMBRE_ZONA"));
			MenuMinimum menu = daoMenu.buscarMenusPorNombreYRestaurante(rs.getString("NOMBRE_MENU"), rs.getString("NOMBRE_RESTAURANTE"));
			reservas.add(new Reserva(fecha, personas, reservador, zona, menu));
		}
		daoUsuario.cerrarRecursos();
		daoZona.cerrarRecursos();
		daoMenu.cerrarRecursos();
		return reservas;
	}
	
	/**
	 * Elimina todos los reservas pertenecientes al reservador dado.
	 * @param reservador Usuario al cual eliminarle los reservas.
	 * @throws SQLException Alg�n problema de la base de datos.<br>
	 */
	public void eliminarReservasPorUsuario(UsuarioMinimum reservador) throws SQLException {
		String sql = "DELETE FROM RESERVA WHERE ID_RESERVADOR = " + reservador.getId();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}

	/**
	 * Elimina todas las reservas en la zona dada.
	 * @param nombreZona nombre de la zona al cual eliminarle las reservas.
	 * @throws SQLException Alg�n problema de la base de datos.<br>
	 */
	public void borrarPorZona(String nombreZona) throws SQLException {
		String sql = "DELETE FROM RESERVA WHERE NOMBRE_ZONA LIKE '" + nombreZona + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeQuery();
	}

}
