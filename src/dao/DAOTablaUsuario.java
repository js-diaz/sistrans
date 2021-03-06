
package dao;


import java.sql.Connection; 


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rfc.ContenedoraClienteProductos;
import rfc.ContenedoraMenuCliente;
import rfc.ContenedoraMesaMenuCliente;
import rfc.ProductoInformativo;
import rfc.UsuarioCompleto;
import vos.*;
import vos.UsuarioMinimum;
import vos.UsuarioMinimum.Rol;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicaciÃ³n
 * @author s.guzmanm
 */
public class DAOTablaUsuario {


	/**
	 * Arraylits de recursos que se usan para la ejecuciÃ³n de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexiÃ³n a la base de datos
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
	 * Metodo que inicializa la connection del DAO a la base de datos con la conexiÃ³n que entra como parÃ¡metro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection conn){
		this.conn = conn;
	}


	/**
	 * Metodo que, usando la conexiÃ³n a la base de datos, saca todos los usuarios de la base de datos
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
			ArrayList<CuentaMinimum> historial=hist.buscarCuentasPorId(id);
			RestauranteMinimum restaurante = rest.darRestauranteDeUsuario(id);
			usuarios.add(new Usuario(name,id,correo,r,p,historial,restaurante));
		}
		rest.cerrarRecursos();
		hist.cerrarRecursos();
		pref.cerrarRecursos();
		return usuarios;
	}

	

	/**
	 * Metodo que busca el/los usuarios con el nombre que entra como parÃ¡metro.
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
			ArrayList<CuentaMinimum> historial=hist.buscarCuentasPorId(id);
			RestauranteMinimum restaurante = rest.darRestauranteDeUsuario(id);
			usuarios.add(new Usuario(name,id,correo,r,p,historial,restaurante));
		}

		rest.cerrarRecursos();
		hist.cerrarRecursos();
		pref.cerrarRecursos();
		return usuarios;
	}
	
	/**
	 * Metodo que busca el usuario con el id que entra como parÃ¡metro.
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
			ArrayList<CuentaMinimum> historial=hist.buscarCuentasPorId(id);
			RestauranteMinimum restaurante = rest.darRestauranteDeUsuario(id);
			usuario=(new Usuario(name,id2,correo,r,p,historial,restaurante));
		}

		rest.cerrarRecursos();
		hist.cerrarRecursos();
		pref.cerrarRecursos();
		return usuario;
	}
	
	/**
	 * Retorna una lista de usuarios que tienen el rol dado por parÃ¡metro.<br>
	 * @param nombreRol Nombre del rol.<br>
	 * @return Lista de usuarios.<br>
	 * @throws SQLException Si algo sale mal en la BD.<br>
	 * @throws Exception Si cualquier otra cosa sale mal.
	 */
	public ArrayList<Usuario> buscarUsuarioPorRol(String nombreRol)throws SQLException, Exception {
		ArrayList<Usuario> usuario = new ArrayList<>();

		DAOTablaPreferencia pref = new DAOTablaPreferencia();
		DAOTablaCuenta hist = new DAOTablaCuenta();
		DAOTablaRestaurante rest = new DAOTablaRestaurante();
		pref.setConn(this.conn);
		hist.setConn(this.conn);
		rest.setConn(this.conn);
		
		String sql = "SELECT * FROM USUARIO WHERE ROL LIKE '" + nombreRol+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while(rs.next()) {
			String name = rs.getString("NOMBRE");
			Long id2 = rs.getLong("ID");
			String correo = rs.getString("CORREO");
			Rol r = convertirARol(rs.getString("ROL"));
			Preferencia p = pref.buscarPreferenciaPorId(id2);
			ArrayList<CuentaMinimum> historial=hist.buscarCuentasPorId(id2);
			RestauranteMinimum restaurante = rest.darRestauranteDeUsuario(id2);
			usuario.add(new Usuario(name,id2,correo,r,p,historial,restaurante));
		}

		rest.cerrarRecursos();
		hist.cerrarRecursos();
		pref.cerrarRecursos();
		return usuario;
	}

	/**
	 * Metodo que agrega el usuario que entra como parÃ¡metro a la base de datos.
	 * @param usuario - el usuario a agregar. usuario !=  null
	 * <b> post: </b> se ha agregado el usuario a la base de datos en la transaction actual. pendiente que el usuario master
	 * haga commit para que el usuario baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el usuario a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addUsuario(Usuario usuario) throws SQLException, Exception {
		if(usuario.getRol()==null) throw new Exception("El usuario debe tener un rol");
		switch(usuario.getRol())
		{
		case CLIENTE: 
			if(usuario.getRestaurante()!=null) throw new Exception("No se le puede asignar un cliente como dueño de un restaurante");
			break;
		case LOCAL: 
			if(usuario.getRestaurante()==null)
			throw new Exception("No se puede asignar un usuario de tipo local sin su restaurante");
			if(usuario.getPreferencia()!=null) throw new Exception("EL usuario no puede tener preferencias al ser dueño de un local");
			break;
		default:
			if(usuario.getPreferencia()!=null || usuario.getRestaurante()!=null) throw new Exception("Un"+usuario.getRol()+" no puede tener asociado un restaurante o una preferencia");
			break;
		}
		String valor="select IDUSUARIO.NEXTVAL as VALOR from dual";
		PreparedStatement prepStmt = conn.prepareStatement(valor);
		recursos.add(prepStmt);
		ResultSet rs=prepStmt.executeQuery();
		if(rs.next())
		{
			usuario.setId(rs.getLong("VALOR"));
		}
		String sql = "INSERT INTO USUARIO VALUES (";
		sql += usuario.getId()+",'";
		sql += usuario.getCorreo() + "','";
		sql += usuario.getNombre()+"','";
		sql+=convertirRol(usuario.getRol())+ "')";

		prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		DAOTablaPreferencia pref = new DAOTablaPreferencia();
		pref.setConn(this.conn);
		if(usuario.getPreferencia()!=null)
		pref.addPreferencia(usuario.getId(), usuario.getPreferencia());
		pref.cerrarRecursos();
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
	 * Metodo que actualiza el usuario que entra como parÃ¡metro en la base de datos.
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
		rest.actualizarRestauranteDeUsuario(usuario.getId(),usuario.getRestaurante());
		rest.cerrarRecursos();
		pref.cerrarRecursos();
		cuenta.cerrarRecursos();
	}

	/**
	 * Metodo que elimina el usuario que entra como parÃ¡metro en la base de datos.
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
		
		rest.borrarRestaurantePorIdRepresentante(usuario.getId());
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
	 * @throws SQLException ExcepciÃ³n de la BD.<br>
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
	 * Convierte el rol pasado como parÃ¡metro a un String.<br>
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
	
	/**
	 * Obtiene el Ã­ndice actual del ingrediente.<br>
	 * @param Ã�ndice actual.<br>
	 */
	public int getCurrentIndex() throws SQLException, Exception
	{
		String sql = "SELECT last_number FROM user_sequences WHERE sequence_name = 'IDUSUARIO'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs=prepStmt.executeQuery();
		return rs.getInt("LAST_NUMBER");
	}

	
	/**
	 * Metodo que busca el usuario con el id que entra como parÃ¡metro.
	 * @param name - Id de el usuario a buscar
	 * @return Usuario encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public UsuarioMinimum buscarUsuarioMinimumPorId(Long id) throws SQLException, Exception 
	{
		UsuarioMinimum usuario = null;

		
		String sql = "SELECT * FROM USUARIO WHERE ID =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			String name = rs.getString("NOMBRE");
			Long id2 = rs.getLong("ID");
			String correo = rs.getString("CORREO");
			Rol r = convertirARol(rs.getString("ROL"));
			
			usuario=(new UsuarioMinimum(name,id2,correo, r));
		}
		return usuario;
	}
	/**
	 * Da toda la información de un usuario. Método necesario para el requerimiento de consulta 3.<br>
	 * @param id Id del usuario.<br>
	 * @return UsuarioCompleto con toda la información solicitada.<br>
	 * @throws Exception Si sucede cualquier error.<br>
	 * @throws SQLException Si hay un error de BD.
	 */
	//¿Cómo calculo el tiempo?
	public UsuarioCompleto darTodaLaInfoDeUnCliente(Long id) throws SQLException, Exception
	{
		
		DAOTablaPreferencia pref = new DAOTablaPreferencia();
		DAOTablaCuenta hist = new DAOTablaCuenta();
		DAOTablaRestaurante rest = new DAOTablaRestaurante();
		pref.setConn(this.conn);
		hist.setConn(this.conn);
		rest.setConn(this.conn);
		String sql = "SELECT * FROM USUARIO WHERE ID =" + id;

		UsuarioCompleto usuario=null;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		double time =System.currentTimeMillis();
		ResultSet rs =prepStmt.executeQuery();
		time=System.currentTimeMillis()-time;
		System.out.println(time);
		if(time>800) throw new Exception("EL tiempo excede a los 0.8 con "+time);		Long idActual=null;
		if (rs.next()) {
			String name = rs.getString("NOMBRE");
			Long id2 = rs.getLong("ID");
			String correo = rs.getString("CORREO");
			Rol r = convertirARol(rs.getString("ROL"));
			Preferencia p = pref.buscarPreferenciaPorId(id);
			ArrayList<CuentaMinimum> historial=hist.buscarCuentasPorId(id);
			RestauranteMinimum restaurante = rest.darRestauranteDeUsuario(id);
			int[] frecuencias=darFrecuencias(id2);
			usuario=(new UsuarioCompleto(name,id2,correo,r,p,historial,restaurante, frecuencias));
		}
		rest.cerrarRecursos();
		hist.cerrarRecursos();
		pref.cerrarRecursos();
		return usuario;
	}
	/**
	 * Retorna un arreglo con las frecuencias del usuario para los siete días de la semana.<br>
	 * @param id Id del usuario.<br>
	 * @return Frecuencias del usuario.<br>
	 * @throws SQLException Si hay algún error de la BD.<br>
	 * @throws Exception Si sucede cualquier otra excepción.
	 */
	private int[]darFrecuencias(Long id) throws SQLException, Exception {
		String sql="SELECT idusuario, to_char(fecha,'D') AS DIA, COUNT(*) AS FRECUENCIA "
				+ "FROM CUENTA  "
				+ "WHERE idusuario="+id
				+ "GROUP BY idusuario, to_char(fecha,'D') "
				+ "ORDER BY idusuario, to_char(fecha,'D') ";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		int[] frecuencias=new int[7];
		int i=1;
		while(rs.next())
		{
			int conteo=rs.getInt("FRECUENCIA");
			int dia=rs.getInt("DIA");
			frecuencias[dia-1]=conteo;
		}
		return frecuencias;
	}
	
	/**
	 * Retorna la información de los productos consumidos por un cliente, distinguiendo entre si pidió o no en una mesa, y si pidió de algún menú o no.<br>
	 * @param id Id del cliente.<br>
	 * @return contenedora de información requerida.<br>
	 * @throws SQLException SI hay errores de la BD.<br>
	 * @throws Exception SI hay algún otro error.
	 */
	public List<ContenedoraClienteProductos> informacionProductos(Long id) throws SQLException,Exception
	{
		
		String cliente="";
		if(id!=null) cliente="AND IDUSUARIO="+id;
		String sql="SELECT CLIENTE,TIENEMESA,NOMBRE_MENU,ID,NOMBRE,DESCRIPCION,TRADUCCION,TIPO,NOMBRE_RESTAURANTE,CANTIDAD FROM ("
				+ "SELECT USUARIO.ID AS CLIENTE,(CASE WHEN  CUENTA.MESA IS NULL THEN 0"
				+ "    ELSE 1 END) AS TIENEMESA ,NULL AS NOMBRE_MENU, PRODUCTO.ID, PRODUCTO.NOMBRE, PRODUCTO.DESCRIPCION, PRODUCTO.TRADUCCION, PRODUCTO.TIPO,PEDIDO_PROD.NOMBRE_RESTAURANTE,SUM(PEDIDO_PROD.CANTIDAD) AS CANTIDAD "
				+ "FROM CUENTA,PEDIDO_PROD, PRODUCTO,USUARIO "
				+ "WHERE IDUSUARIO=USUARIO.ID AND CUENTA.NUMEROCUENTA LIKE PEDIDO_PROD.NUMERO_CUENTA AND CUENTA.PAGADA='1' AND PEDIDO_PROD.ID_PRODUCTO=PRODUCTO.ID "+cliente
				+ "GROUP BY USUARIO.ID, (CASE WHEN CUENTA.MESA IS NULL THEN 0 ELSE 1 END), NULL, PRODUCTO.ID, PRODUCTO.NOMBRE, PRODUCTO.DESCRIPCION, "
				+ "PRODUCTO.TRADUCCION, PRODUCTO.TIPO, PEDIDO_PROD.NOMBRE_RESTAURANTE) "
				+ "UNION("
				+ "SELECT USUARIO.ID AS CLIENTE,(CASE WHEN CUENTA.MESA IS NULL THEN 0"
				+ "    ELSE 1 END) AS TIENEMESA ,MENU.NOMBRE AS NOMBREMENU, PRODUCTO.ID, PRODUCTO.NOMBRE, PRODUCTO.DESCRIPCION, PRODUCTO.TRADUCCION, PRODUCTO.TIPO, MENU.NOMBRE_RESTAURANTE, SUM(PEDIDO_MENU.CANTIDAD) AS CANTIDAD "
				+ "FROM CUENTA,PEDIDO_MENU, PRODUCTO, MENU, PERTENECE_A_MENU,USUARIO "
				+ "WHERE IDUSUARIO=USUARIO.ID AND CUENTA.NUMEROCUENTA LIKE PEDIDO_MENU.NUMERO_CUENTA AND CUENTA.PAGADA='1' AND PEDIDO_MENU.NOMBRE_MENU LIKE MENU.NOMBRE "
				+ "AND PEDIDO_MENU.NOMBRE_RESTAURANTE LIKE MENU.NOMBRE_RESTAURANTE AND PERTENECE_A_MENU.NOMBRE_RESTAURANTE LIKE MENU.NOMBRE_RESTAURANTE "
				+ "AND PERTENECE_A_MENU.NOMBRE_MENU LIKE MENU.NOMBRE AND PRODUCTO.ID = PERTENECE_A_MENU.ID_PLATO "+cliente
				+ " GROUP BY USUARIO.ID, (CASE WHEN CUENTA.MESA IS NULL THEN 0 ELSE 1 END), MENU.NOMBRE, PRODUCTO.ID, PRODUCTO.NOMBRE, "
				+ "PRODUCTO.DESCRIPCION, PRODUCTO.TRADUCCION, PRODUCTO.TIPO, MENU.NOMBRE_RESTAURANTE) "
				+ "ORDER BY CLIENTE,TIENEMESA,NOMBRE_MENU";
		PreparedStatement prep=conn.prepareStatement(sql);
		System.out.println(sql);
		double time=System.currentTimeMillis();
		ResultSet rs=prep.executeQuery();
		time=System.currentTimeMillis()-time;
		System.out.println(time);
		if(time>800) throw new Exception("EL tiempo excede a los 0.8 con "+time);		Long idActual=null;
		String menuActual="";
		Long nuevaId=null;
		String menu="";
		boolean tieneActual=false;
		boolean tiene=false;
		ContenedoraClienteProductos cp=null;
		ContenedoraMesaMenuCliente mmc=null;
		ContenedoraMenuCliente mc=null;
		ProductoInformativo p=null;
		List<ContenedoraClienteProductos> list=new ArrayList<>();
		if(rs.next())
		{
			idActual=rs.getLong("CLIENTE");
			tieneActual=rs.getBoolean("TIENEMESA");
			menuActual=rs.getString("NOMBRE_RESTAURANTE")+";;;"+rs.getString("NOMBRE_MENU");
			cp=new ContenedoraClienteProductos(idActual, new ArrayList<ContenedoraMesaMenuCliente>());
			mmc=new ContenedoraMesaMenuCliente(rs.getBoolean("TIENEMESA"), new ArrayList<ContenedoraMenuCliente>());
			mc=new ContenedoraMenuCliente(rs.getString("NOMBRE_RESTAURANTE"), rs.getString("NOMBRE_MENU"), new ArrayList<ProductoInformativo>());
			p=new ProductoInformativo(rs.getString("NOMBRE"), rs.getString("TIPO"), rs.getString("DESCRIPCION"), rs.getString("TRADUCCION"), rs.getLong("ID"));
			mc.getProducto().add(p);
		}
		while(rs.next())
		{
			nuevaId=rs.getLong("CLIENTE");
			if(nuevaId==144) 
				{
				System.out.println("HOLA");
				}
			tiene=rs.getBoolean("TIENEMESA");
			menu=rs.getString("NOMBRE_RESTAURANTE")+";;;"+rs.getString("NOMBRE_MENU");
			if(!nuevaId.equals(idActual))
			{
				mmc.getInformacionMenu().add(mc);
				cp.getInformacionMesaMenu().add(mmc);
				list.add(cp);
				idActual=nuevaId;
				tieneActual=tiene;
				menuActual=menu;
				String[] datos=menuActual.split(";;;");
				cp=new ContenedoraClienteProductos(idActual, new ArrayList<ContenedoraMesaMenuCliente>());
				mmc=new ContenedoraMesaMenuCliente(tieneActual, new ArrayList<ContenedoraMenuCliente>());
				mc=new ContenedoraMenuCliente(datos[0],datos[1], new ArrayList<ProductoInformativo>());

			}
			else if(tieneActual!=tiene)
			{
				mmc.getInformacionMenu().add(mc);
				cp.getInformacionMesaMenu().add(mmc);
				tieneActual=tiene;
				menuActual=menu;
				String[] datos=menuActual.split(";;;");
				mmc=new ContenedoraMesaMenuCliente(tieneActual, new ArrayList<ContenedoraMenuCliente>());
				mc=new ContenedoraMenuCliente(datos[0],datos[1], new ArrayList<ProductoInformativo>());
			}
			else if(!(menu.equals(menuActual)))
			{
				mmc.getInformacionMenu().add(mc);
				menuActual=menu;
				String[] datos=menuActual.split(";;;");
				mc=new ContenedoraMenuCliente(datos[0],datos[1], new ArrayList<ProductoInformativo>());
			}
			p=new ProductoInformativo(rs.getString("NOMBRE"), rs.getString("TIPO"), rs.getString("DESCRIPCION"), rs.getString("TRADUCCION"), rs.getLong("ID"));
			mc.getProducto().add(p);
		}
		mmc.getInformacionMenu().add(mc);
		cp.getInformacionMesaMenu().add(mmc);
		list.add(cp);
		return list;

	}
	
	/**
	 * Metodo que, usando la conexiÃ³n a la base de datos, consulta los buenos clientes de la base de datos
	 * @return List con los usuarios de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<Usuario> darBuenosClientes() throws SQLException, Exception {
		List<Usuario> usuarios = new ArrayList<>();

		DAOTablaPreferencia pref = new DAOTablaPreferencia();
		DAOTablaCuenta hist = new DAOTablaCuenta();
		DAOTablaRestaurante rest = new DAOTablaRestaurante();
		pref.setConn(this.conn);
		hist.setConn(this.conn);
		rest.setConn(this.conn);
		
		String sql = "SELECT * FROM USUARIO WHERE ID IN (SELECT IDUSUARIO FROM CUENTA MINUS " + 
				"SELECT IDUSUARIO FROM CUENTA WHERE NUMEROCUENTA IN (SELECT NUMERO_CUENTA FROM PEDIDO_MENU INTERSECT " + 
				"SELECT NUMERO_CUENTA FROM PEDIDO_PROD JOIN INFO_PROD_REST USING (ID_PRODUCTO, NOMBRE_RESTAURANTE) WHERE PRECIO < 1106) UNION ALL " + 
				"SELECT IDUSUARIO FROM CUENTA GROUP BY IDUSUARIO HAVING COUNT(DISTINCT TO_CHAR(FECHA, 'WW YYYY')) = 105)";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			String correo = rs.getString("CORREO");
			Rol r = convertirARol(rs.getString("ROL"));
			Preferencia p = pref.buscarPreferenciaPorId(id);
			ArrayList<CuentaMinimum> historial=hist.buscarCuentasPorId(id);
			RestauranteMinimum restaurante = rest.darRestauranteDeUsuario(id);
			usuarios.add(new Usuario(name,id,correo,r,p,historial,restaurante));
		}
		rest.cerrarRecursos();
		hist.cerrarRecursos();
		pref.cerrarRecursos();
		return usuarios;
	}

	public UsuarioMinimum buscarUsuarioMinimumPorCorreo(String correo) throws Exception {
		// TODO Auto-generated method stub
		UsuarioMinimum usuario = null;

		
		String sql = "SELECT * FROM USUARIO WHERE CORREO ='" + correo+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			String name = rs.getString("NOMBRE");
			Long id2 = rs.getLong("ID");
			String correo2 = rs.getString("CORREO");
			Rol r = convertirARol(rs.getString("ROL"));
			
			usuario=(new UsuarioMinimum(name,id2,correo2, r));
		}
		return usuario;
	}

}

