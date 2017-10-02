/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (BogotÃ¡ - Colombia)
 * Departamento de IngenierÃ­a de Sistemas y ComputaciÃ³n
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: CuentaAndes
 * Autor: Juan Felipe GarcÃ­a - jf.garcia268@uniandes.edu.co
 * -------------------------------------------------------------------
 */
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
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicaciÃ³n
 * @author Monitores 2017-20
 */
public class DAOTablaCuenta {


	/**
	 * Arraylits de recursos que se usan para la ejecuciÃ³n de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexiÃ³n a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOCuenta
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaCuenta() {
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
	 * Metodo que inicializa la connection del DAO a la base de datos con la conexiÃ³n que entra como parametro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection con){
		this.conn = con;
	}


	/**
	 * Metodo que, usando la conexiÃ³n a la base de datos, saca todos los cuentas de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM CUENTAS;
	 * @return Arraylist con los cuentas de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Cuenta> darCuentas() throws SQLException, Exception {
		ArrayList<Cuenta> cuentas = new ArrayList<Cuenta>();

		String sql = "SELECT * FROM CUENTA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String numeroCuenta = rs.getString("NUMEROCUENTA");
			Double valor = rs.getDouble("VALOR");
			Date fecha = rs.getDate("FECHA");
			List<PedidoMenu> menus= darPedidosMenus(numeroCuenta);
			List<PedidoProd> productos= darPedidosProductos(numeroCuenta);
			UsuarioMinimum u = buscarUsuarioPorId(rs.getLong("IDUSUARIO"));
			
			cuentas.add(new Cuenta(productos,menus,valor,numeroCuenta,fecha,u));
		}
		return cuentas;
	}



	/**
	 * Metodo que busca la cuenta con el nÃºmero de cuenta que entra como parametro.
	 * @param numeroCuenta - Nombre de el/los cuentas a buscar
	 * @return Cuenta encontrada
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Cuenta buscarCuentasPorNumeroDeCuenta(String numeroCuenta) throws SQLException, Exception {

		String sql = "SELECT * FROM CUENTA WHERE NUMEROCUENTA LIKE'" + numeroCuenta + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		Cuenta c=null;

		if (rs.next()) {
			String numCuenta = rs.getString("NUMEROCUENTA");
			Double valor = rs.getDouble("VALOR");
			Date fecha = rs.getDate("FECHA");
			List<PedidoMenu> menus= darPedidosMenus(numeroCuenta);
			List<PedidoProd> productos= darPedidosProductos(numeroCuenta);
			UsuarioMinimum u = buscarUsuarioPorId(rs.getLong("IDUSUARIO"));
			
			c=(new Cuenta(productos,menus,valor,numCuenta,fecha,u));
		}

		return c;
	}
	
	/**
	 * Metodo que busca las cuentas con el id de usuario dado por parÃ¡metro que entra como parametro.
	 * @param id - Id de usuario
	 * @return Cuenta dada
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<CuentaMinimum> buscarCuentasPorId(Long id) throws SQLException, Exception {
		ArrayList<CuentaMinimum> cuentas = new ArrayList<>();

		String sql = "SELECT * FROM CUENTA WHERE IDUSUARIO =" + id + "";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String numCuenta = rs.getString("NUMEROCUENTA");
			Double valor = rs.getDouble("VALOR");
			Date fecha = rs.getDate("FECHA");
			cuentas.add(new CuentaMinimum(valor,numCuenta,fecha));
		}
		return cuentas;
	}

	
	/**
	 * Metodo que agrega la cuenta que entra como parametro a la base de datos.
	 * @param c - la cuenta a agregar. cuenta !=  null
	 * <b> post: </b> se ha agregado la cuenta a la base de datos en la transaction actual. pendiente que la cuenta master
	 * haga commit para que la cuenta baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la cuenta a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addCuenta(Cuenta c) throws SQLException, Exception {

		String sql = "INSERT INTO CUENTA VALUES (";
		sql += c.getValor() + ",'";
		sql += c.getNumeroCuenta() + "',";
		sql += dateFormat(c.getFecha())+",";
		sql+= c.getCliente().getId()+")";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		insertarProducto(c);
		insertarMenu(c);

	}
	
	

	private String dateFormat(Date fecha) {
		SimpleDateFormat x = new SimpleDateFormat("dd/mm/yyyy hh24:mi:ss");
		return "TODATE('"+x.format(fecha)+"','dd/mm/yyyy hh24:mi:ss')";
	}

	/**
	 * Metodo que actualiza la cuenta que entra como parametro en la base de datos.
	 * @param video - la cuenta a actualizar. video !=  null
	 * <b> post: </b> se ha actualizado la cuenta en la base de datos en la transaction actual. pendiente que la cuenta master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la cuenta.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateCuenta(Cuenta c) throws SQLException, Exception {

		String sql = "UPDATE CUENTA SET ";
		sql += "VALOR=" + c.getValor() + ",";
		sql += "FECHA=" + dateFormat(c.getFecha());
		sql += " WHERE NUMEROCUENTA LIKE '" + c.getNumeroCuenta()+"'";


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina la cuenta que entra como parametro en la base de datos.
	 * @param c - la cuenta a borrar. c !=  null
	 * <b> post: </b> se ha borrado la cuenta en la base de datos en la transaction actual. pendiente que la cuenta master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la cuenta.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteCuenta(Cuenta c) throws SQLException, Exception {

		eliminarProductos(c);
		eliminarMenus(c);
		String sql = "DELETE FROM CUENTA";
		sql += " WHERE NUMEROCUENTA = " + c.getNumeroCuenta();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Borra el historial de un cliente por su id.<br>
	 * @param id Id del cliente a borrar su historial.<br>
	 * @throws Exception Si alguna cosa sale mal.<br>
	 * @throws SQLException Si algo sale mal respecto a la base de datos.
	 */
	public void borrarHistorialCliente(Long id) throws SQLException, Exception {
		String sql ="DELETE FROM CUENTA WHERE USUARIOID ="+id;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Busca el usuario por el id dado.<br>
	 * @param int1 Id del usuario.<br>
	 * @return Usuario buscado
	 */
	private UsuarioMinimum buscarUsuarioPorId(Long int1) throws SQLException, Exception {
		DAOTablaUsuario u= new DAOTablaUsuario();
		u.setConn(this.conn);
		UsuarioMinimum usuario=u.buscarUsuarioMinimumPorId(int1);
		u.cerrarRecursos();
		return usuario;
	}
	/**
	 * Busca los pedidos productos con el nÃºmero de cuenta dado.<br>
	 * @param numeroCuenta NÃºmero de cuenta.<br>
	 * @return Lista con pedidos producto.
	 */
	private List<PedidoProd> darPedidosProductos(String numeroCuenta) throws SQLException, Exception {
		DAOTablaPedidoProducto p  = new DAOTablaPedidoProducto();
		p.setConn(this.conn);
		List<PedidoProd> list = p.buscarProductosPorNumCuenta(numeroCuenta);
		p.cerrarRecursos();
		return list;
	}
	/**
	 * Busca los pedidos menÃº con el nÃºmero de cuenta dado.<br>
	 * @param numeroCuenta NÃºmero de cuenta.<br>
	 * @return Lista con pedidos menÃº.
	 */
	private List<PedidoMenu> darPedidosMenus(String numeroCuenta) throws SQLException, Exception {
		DAOTablaPedidoMenu m = new DAOTablaPedidoMenu();
		m.setConn(this.conn);
		List<PedidoMenu> list = m.buscarMenusPorNumCuenta(numeroCuenta);
		m.cerrarRecursos();
		return list;
	}
	/**
	 * Inserta los pedidosMenu con la cuenta dada por parÃ¡metro.<br>
	 * @param c Cuenta a insertar menÃºs.
	 */
	private void insertarMenu(Cuenta c) throws SQLException, Exception {
		DAOTablaPedidoMenu menus = new DAOTablaPedidoMenu();
		menus.setConn(this.conn);
		for(PedidoMenu pm : c.getPedidoMenuMinimum())
			menus.addPedidoMenu(pm);
		menus.cerrarRecursos();
	}
	/**
	 * Inserta los pedidosProducto con la cuenta dada por parÃ¡metro.<br>
	 * @param c Cuenta a insertar productos.
	 */
	private void insertarProducto(Cuenta c) throws SQLException, Exception {
		DAOTablaPedidoProducto productos = new DAOTablaPedidoProducto();
		productos.setConn(this.conn);
		for(PedidoProd pp : c.getPedidoProdMinimum())
			productos.addPedidoProd(pp);
		productos.cerrarRecursos();
		
	}
	/**
	 * Elimina los pedidosMenu que tienen este nÃºmero de cuenta.<br>
	 * @param numeroCuenta NÃºmero de la cuenta a eliminar pedidos menÃº.
	 */
	private void eliminarMenus(CuentaMinimum cuenta) throws SQLException, Exception {
		DAOTablaPedidoMenu menus = new DAOTablaPedidoMenu();
		menus.setConn(this.conn);
		menus.eliminarPedidoMenusPorCuenta(cuenta);
		menus.cerrarRecursos();
		
	}
	/**
	 * Elimina los pedidosProducto que tienen este nÃºmero de cuenta.<br>
	 * @param numeroCuenta NÃºmero de la cuenta a eliminar pedidos producto.
	 */
	private void eliminarProductos(CuentaMinimum cuenta) throws SQLException, Exception {
		DAOTablaPedidoProducto prod= new DAOTablaPedidoProducto();
		prod.setConn(this.conn);
		prod.eliminarPedidoProdsPorCuenta(cuenta);
		prod.cerrarRecursos();
	}
	/**
	 * Obtiene el Ã­ndice actual del ingrediente.<br>
	 * @param Ã�ndice actual.<br>
	 */
	public int getCurrentIndex() throws SQLException, Exception
	{
		String sql = "SELECT last_number FROM user_sequences WHERE sequence_name = 'NUMCUENTA'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs=prepStmt.executeQuery();
		return rs.getInt("LAST_NUMBER");
	}

	public ArrayList<CuentaMinimum> buscarCuentaMinimumsPorId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
