
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rfc.ContenedoraPedidosProd;
import rfc.PendientesOrden;
import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación
 * @author s.guzmanm
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
	 * Metodo que busca la cuenta con el nÃºmero de cuenta que entra como parametro.
	 * @param numeroCuenta - Nombre de el/los cuentas a buscar
	 * @return Cuenta encontrada
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public CuentaMinimum buscarCuentasMinimumPorNumeroDeCuenta(String numeroCuenta) throws SQLException, Exception {

		String sql = "SELECT * FROM CUENTA WHERE NUMEROCUENTA LIKE'" + numeroCuenta + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		CuentaMinimum c=null;

		if (rs.next()) {
			String numCuenta = rs.getString("NUMEROCUENTA");
			Double valor = rs.getDouble("VALOR");
			Date fecha = rs.getDate("FECHA");
			c=(new CuentaMinimum(valor,numCuenta,fecha));
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
		sql += "0,'";
		sql += c.getNumeroCuenta() + "',";
		sql += dateFormat(c.getFecha())+",";
		sql+= c.getCliente().getId()+")";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		//insertarProducto(c);
		//insertarMenu(c);
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
		String sql ="DELETE FROM CUENTA WHERE IDUSUARIO ="+id;
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
		for(PedidoMenu pm : c.getPedidoMenu())
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
		for(PedidoProd pp : c.getPedidoProd())
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
	/**
	 * Busca una cuenta en forma minimum con el id dado.<br>
	 * @param id Id de la cuenta.<br>
	 * @return Cuenta en formato minimum.<br>
	 * @throws SQLException Si hay algún error en la BD.<br>
	 * @throws Exception SI hay otro error en el sistema.
	 */
	public ArrayList<CuentaMinimum> buscarCuentaMinimumsPorId(Long id) throws SQLException, Exception {
		// TODO Auto-generated method stub
		return buscarCuentasPorId(id);
	}
	/**
	 * Se paga la cuenta con el número dado.<br>
	 * @param numeroCuenta Número de la cuenta.<br>
	 * @return Lista de objetos con los que no se ha decidido qué hacer.<br>
	 * @throws SQLException Si hay algún error en la BD.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	public PendientesOrden pagarCuenta(String numeroCuenta) throws SQLException, Exception
	{
		Cuenta c= buscarCuentasPorNumeroDeCuenta(numeroCuenta);
		
		DAOTablaMenu menus= new DAOTablaMenu();
		menus.setConn(conn);
		Menu menu=null;
		ContenedoraPedidosProd cp=null;
		List<ContenedoraPedidosProd> pedidos=new ArrayList<>();
		for(PedidoMenu m: c.getPedidoMenu())
		{
			if(m.getEntregado()==true) continue;
			menu=menus.buscarMenusPorNombreYRestaurante(m.getMenu().getNombre(), m.getMenu().getRestaurante().getNombre());
			cp=new ContenedoraPedidosProd(menu.getNombre(),new ArrayList<PedidoProd>());
			for(InfoProdRest p:menu.getPlatos())
			cp.getPedidosProd().add(new PedidoProd(m.getCantidad(),m.getCuenta(),p,false));
			pedidos.add(cp);
		}
		menus.cerrarRecursos();
		
		//Revisa pedidos de menús en general
		DAOTablaInfoProdRest productos = new DAOTablaInfoProdRest();
		DAOTablaPedidoProducto daoPedido= new DAOTablaPedidoProducto();
		daoPedido.setConn(conn);
		productos.setConn(this.conn);
		InfoProdRest info=null;
		ArrayList<ContenedoraPedidosProd> menusPendientes= new ArrayList<>();
		for(ContenedoraPedidosProd cont:pedidos)
		{
			try
			{
				for(PedidoProd p: cont.getPedidosProd())
				{
					if(p.getPlato().getDisponibilidad()<p.getCantidad())
					{
						throw new Exception("No se puede agregar el menú");
					}
				}
				for(PedidoProd p: cont.getPedidosProd())
				{
					if(p.getPlato().getDisponibilidad()<p.getCantidad())
					{
						info=p.getPlato();
						info.setDisponibilidad(info.getDisponibilidad()-p.getCantidad());
						productos.updateInfoProdRest(info);
						p.setEntregado(true);
						daoPedido.updatePedidoProd(p);
					}
				}
			}
			catch(Exception e)
			{
				menusPendientes.add(cont);
			}
			
		}
		ArrayList<PedidoProd> restantesProductos= new ArrayList<PedidoProd>();
		for(PedidoProd p:c.getPedidoProd())
		{
			try
			{
				if(p.getEntregado()) continue;
				info=p.getPlato();
				info.setDisponibilidad(info.getDisponibilidad()-p.getCantidad());
				productos.updateInfoProdRest(info);
				p.setEntregado(true);
				daoPedido.updatePedidoProd(p);
			}
			catch(Exception e)
			{
				restantesProductos.add(p);
			}
		}
		daoPedido.cerrarRecursos();
		productos.cerrarRecursos();
		//Analiza los productos y menús restantes
		for(PedidoProd p:restantesProductos)
		{
			c.setValor(c.getValor()-(p.getCantidad()+p.getPlato().getCosto()));
		}
		for(ContenedoraPedidosProd cont: menusPendientes)
		{
			for(PedidoProd p:cont.getPedidosProd())
			{
				c.setValor(c.getValor()-(p.getCantidad()+p.getPlato().getCosto()));
			}
		}
		return new PendientesOrden(restantesProductos, menusPendientes);
	}
	
	public void prueba() throws SQLException, Exception {

		String sql = "SELECT * FROM CUENTA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String fecha = rs.getString("FECHA");
			System.out.println(fecha);
		}
	}
	/**
	 * Retorna un listado de cuentas de esa mesa.<br>
	 * @param id Id de la mesa.<br>
	 * @return Listado de cuentas.<br>
	 * @throws SQLException Excepción de la BD.<br>
	 * @throws Exception Error cualquiera.
	 */
	public List<CuentaMinimum> darCuentasDeMesa(Long id) throws SQLException, Exception {
		ArrayList<CuentaMinimum> cuentas = new ArrayList<>();

		String sql = "SELECT * FROM CUENTA WHERE MESA =" + id + "";

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
	

}