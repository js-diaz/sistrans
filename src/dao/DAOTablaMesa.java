package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación
 * @author s.guzmanm
 */
public class DAOTablaMesa {


	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOMesa
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaMesa() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos los mesas de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM MESAS;
	 * @return Arraylist con los mesas de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Mesa> darMesas() throws SQLException, Exception {

		String sql = "SELECT * FROM MESA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadMesa(rs);
	}


	

	/**
	 * Metodo que busca el/los mesas con el id que entra como parametro.
	 * @param id - Id de el/los mesas a buscar
	 * @return ArrayList con los mesas encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Mesa buscarMesasPorId(Long id) throws SQLException, Exception {

		String sql = "SELECT * FROM MESA WHERE ID=" + id + "";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		ArrayList<Mesa> mesas= convertirEntidadMesa(rs);
		if(mesas.isEmpty()) return null;
		return mesas.get(0);
	}
	

	/**
	 * Metodo que busca el/los mesas con el id que entra como parametro.
	 * @param id - Id de el/los mesas a buscar
	 * @return ArrayList con los mesas encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public MesaMinimum buscarMesasMinimumPorName(Long id) throws SQLException, Exception {

		String sql = "SELECT * FROM MESA WHERE ID=" + id + "";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		ArrayList<MesaMinimum> mesas= convertirEntidadMesaMinimum(rs);
		if(mesas.isEmpty()) return null;
		return mesas.get(0);
	}

	/**
	 * Metodo que agrega la mesa que entra como parametro a la base de datos.
	 * @param mesa - la mesa a agregar. mesa !=  null
	 * <b> post: </b> se ha agregado la mesa a la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que la mesa baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la mesa a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addMesa(Mesa mesa) throws SQLException, Exception {

		String valor="select IDMESA.NEXTVAL as VALOR from dual";
		PreparedStatement prepStmt = conn.prepareStatement(valor);
		recursos.add(prepStmt);
		ResultSet rs=prepStmt.executeQuery();
		if(rs.next())
		{
			mesa.setId(rs.getLong("VALOR"));
		}
		
		String sql = "INSERT INTO MESA VALUES (";
		sql += ""+mesa.getId() + ",";
		sql += mesa.getCapacidad() + ",";
		sql+=0+",'";
		sql+=mesa.getZona().getNombre()+"')";
		
		prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
	}
	
	

	/**
	 * Metodo que actualiza la mesa que entra como parámetro en la base de datos.
	 * @param mesa - la mesa a actualizar. mesa !=  null
	 * <b> post: </b> se ha actualizado la mesa en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la mesa.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateMesa(Mesa mesa) throws SQLException, Exception {
		Mesa mesaOriginal=buscarMesasPorId(mesa.getId());
		DAOTablaZona dao = new DAOTablaZona();
		dao.setConn(conn);
		Zona z=dao.buscarZonasPorName(mesa.getZona().getNombre());
		z.setCapacidadOcupada(z.getCapacidadOcupada()-mesaOriginal.getCapacidadOcupada());
		dao.updateZona(z);
		
		String sql = "UPDATE MESA SET ";
		sql+="CAPACIDAD="+mesa.getCapacidad()+",";
		sql+="CAPACIDADOCUPADA="+mesa.getCapacidadOcupada();
		sql += " WHERE ID =" + mesa.getId()+"";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		z=dao.buscarZonasPorName(mesa.getZona().getNombre());
		z.setCapacidadOcupada(z.getCapacidadOcupada()+mesa.getCapacidadOcupada());
		dao.updateZona(z);
		dao.cerrarRecursos();
		
	}

	/**
	 * Metodo que elimina la mesa que entra como parametro en la base de datos.
	 * @param mesa - la mesa a borrar. mesa !=  null
	 * <b> post: </b> se ha borrado la mesa en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la mesa.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteMesa(Mesa mesa) throws SQLException, Exception {

		borrarCuentasMesa(mesa);
		DAOTablaZona dao = new DAOTablaZona();
		dao.setConn(conn);
		Zona z=dao.buscarZonasPorName(mesa.getZona().getNombre());
		z.setCapacidadOcupada(z.getCapacidadOcupada()-mesa.getCapacidadOcupada());
		z.getMesas().remove(mesa);
		dao.updateZona(z);
		dao.cerrarRecursos();
		String sql = "DELETE FROM MESA";
		sql += " WHERE ID =" + mesa.getId()+"";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Borra las cuentas de una mesa.<br>
	 * @param mesa Mesa a borrar.<br>
	 * @throws SQLException Error en la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	private void borrarCuentasMesa(Mesa mesa) throws SQLException, Exception {
		DAOTablaCuenta dao= new DAOTablaCuenta();
		dao.setConn(conn);
		Cuenta cuenta;
		if(mesa.getCuentas()!=null)
		for(CuentaMinimum c:mesa.getCuentas())
		{
			cuenta=dao.buscarCuentasPorNumeroDeCuenta(c.getNumeroCuenta());
			dao.deleteCuenta(cuenta,false);
		}
	}
	
	/**
	 * Crea un arreglo de mesas con el set de resultados pasado por parámetro.<br>
	 * @param rs Set de resultados.<br>
	 * @return mesas Lista de mesas convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	public ArrayList<Mesa> convertirEntidadMesa(ResultSet rs) throws SQLException, Exception
	{
		ArrayList<Mesa> mesas = new ArrayList<>();
		while (rs.next()) {
			int capacidad=rs.getInt("CAPACIDAD");
			int capacidadOcupada=rs.getInt("CAPACIDADOCUPADA");
			Long id=rs.getLong("ID");
			ZonaMinimum zona=buscarZona(rs.getString("ZONA"));
			List<CuentaMinimum> cuentas= accederACuentas(id,false);
			List<CuentaMinimum> pagadas=accederACuentas(id,true);
			mesas.add(new Mesa(id,capacidad, capacidadOcupada, zona,cuentas,pagadas));
		}
		return mesas;
	}
	/**
	 * Retorna las cuentas de una mesa específica.<br>
	 * @param id Id de la mesa.<br>
	 * @param eta Si está o no en la lista.<br>
	 * @return cuentas.<br>
	 * @throws SQLException Error en la BD.<br>
	 * @throws Exception Excepción
	 */
	private List<CuentaMinimum> accederACuentas(Long id, boolean esta) throws SQLException, Exception {
		List<CuentaMinimum> list= new ArrayList<>();
		DAOTablaCuenta c= new DAOTablaCuenta();
		c.setConn(conn);
		if(esta)
			list=c.darCuentasPagadasDeMesa(id);
		else list=c.darCuentasNoPagadasDeMesa(id);
		c.cerrarRecursos();
		return list;
	}
	/**
	 * BUsca la zona en representación minimum con el nombre dado.<br>
	 * @param nombre Nombre de la zona.<br>
	 * @return ZonaMinimum.<br>
	 * @throws SQLException Si hay errores de la BD.<br>
	 * @throws Exception Si hay errores.
	 */
	private ZonaMinimum buscarZona(String nombre) throws SQLException, Exception {
		ZonaMinimum z=null;
		DAOTablaZona zona= new DAOTablaZona();
		zona.setConn(conn);
		z=zona.buscarZonasMinimumPorName(nombre);
		zona.cerrarRecursos();
		return z;
	}

	/**
	 * Crea un arreglo de mesas con el set de resultados pasado por parámetro.<br>
	 * @param rs Set de resultados.<br>
	 * @return mesas Lista de mesas convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	private ArrayList<MesaMinimum> convertirEntidadMesaMinimum(ResultSet rs) throws SQLException, Exception
	{
		ArrayList<MesaMinimum> mesas = new ArrayList<>();
		while (rs.next()) {
			int capacidad=rs.getInt("CAPACIDAD");
			int capacidadOcupada=rs.getInt("CAPACIDADOCUPADA");
			Long id=rs.getLong("ID");
			mesas.add(new MesaMinimum(id,capacidad, capacidadOcupada));
		}
		return mesas;
	}
	/**
	 * Busca las mesas por nombre de zona.<br>
	 * @param nombre Nombre de la zona.<br>
	 * @return Listado de mesas.<br>
	 * @throws SQLException Si hay errores en la BD.<br>
	 * @throws Exception Si hay errores generales.
	 */
	public List<MesaMinimum> buscarMesasPorZona(String nombre) throws SQLException, Exception {
		String sql = "SELECT * FROM MESA WHERE ZONA LIKE '" + nombre + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		ArrayList<MesaMinimum> mesas= convertirEntidadMesaMinimum(rs);
		return mesas;
	}
	/**
	 * Busca las mesas por nombre de zona.<br>
	 * @param nombre Nombre de la zona.<br>
	 * @return Listado de mesas.<br>
	 * @throws SQLException Si hay errores en la BD.<br>
	 * @throws Exception Si hay errores generales.
	 */
	public void borrarMesasPorZona(String nombre) throws SQLException, Exception {
		String sql = "DELETE FROM MESA WHERE ZONA LIKE '" + nombre + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	//TODO OPCIONAL ¿DEBERía DEFINIR LA ACTUALIZACIÓN DE ZONA PARA MANEJAR MESAS?
}

