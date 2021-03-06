/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (BogotÃ¡ - Colombia)
 * Departamento de IngenierÃ­a de Sistemas y ComputaciÃ³n
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: ProductoAndes
 * Autor: Juan Felipe GarcÃ­a - jf.garcia268@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package dao;


import java.sql.Connection; 

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.*;
import vos.Producto.TiposDePlato;


/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicaciÃ³n
 * @author Monitores 2017-20
 */
public class DAOTablaProducto {


	/**
	 * Arraylits de recursos que se usan para la ejecuciÃ³n de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexiÃ³n a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOProducto
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaProducto() {
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
	 * Metodo que, usando la conexiÃ³n a la base de datos, saca todos los productos de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM PRODUCTOS;
	 * @return Arraylist con los productos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Producto> darProductos() throws SQLException, Exception {
		ArrayList<Producto> productos = new ArrayList<Producto>();

		String sql = "SELECT * FROM PRODUCTO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		DAOTablaTiposProducto tipos = new DAOTablaTiposProducto();
		tipos.setConn(conn);
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			TiposDePlato tipo= tipos.buscarTipos_De_PlatosPorName(rs.getString("TIPO"));
			String descripcion  = rs.getString("DESCRIPCION");
			String traduccion = rs.getString("TRADUCCION");
			Double tiempo = rs.getDouble("TIEMPO");
			List<Ingrediente> ingredientes = buscarIngredientes(new Producto(false, "", null, "", "", 0, id, null, null));
			List<Categoria> categorias = buscarCategorias(new Producto(false, "",  null, "", "", 0,  id, null, null));
			boolean personalizable=convertirABooleano(rs.getString("PERSONALIZABLE"));
			productos.add(new Producto(personalizable, nombre, tipo, descripcion, traduccion, tiempo, id, ingredientes, categorias));
		}
		tipos.cerrarRecursos();
		return productos;
	}


	/**
	 * Metodo que busca el/los productos con el nombre que entra como parametro.
	 * @param name - Nombre de el/los productos a buscar
	 * @return ArrayList con los productos encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Producto> buscarProductosPorName(String name) throws SQLException, Exception {
		ArrayList<Producto> productos = new ArrayList<Producto>();

		String sql = "SELECT * FROM PRODUCTO WHERE NOMBRE LIKE'" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		DAOTablaTiposProducto tipos = new DAOTablaTiposProducto();
		tipos.setConn(conn);
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			TiposDePlato tipo= tipos.buscarTipos_De_PlatosPorName(rs.getString("TIPO"));
			String descripcion  = rs.getString("DESCRIPCION");
			String traduccion = rs.getString("TRADUCCION");
			Double tiempo = rs.getDouble("TIEMPO");
			List<Ingrediente> ingredientes = buscarIngredientes(new Producto(false, "",  null, "", "", 0,  id, null, null));
			List<Categoria> categorias = buscarCategorias(new Producto(false, "",  null, "", "", 0,  id, null, null));
			boolean personalizable=convertirABooleano(rs.getString("PERSONALIZABLE"));
			productos.add(new Producto(personalizable, nombre, tipo, descripcion, traduccion, tiempo, id, ingredientes, categorias));
		
		}
		tipos.cerrarRecursos();

		return productos;
	}
	
	/**
	 * Metodo que busca el producto con el id que entra como parametro.
	 * @param name - Id de el producto a buscar
	 * @return Producto encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Producto buscarProductoPorId(Long id) throws SQLException, Exception 
	{
		Producto producto = null;

		String sql = "SELECT * FROM PRODUCTO WHERE ID =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		DAOTablaTiposProducto tipos = new DAOTablaTiposProducto();
		tipos.setConn(conn);
		if(rs.next()) {
			String nombre = rs.getString("NOMBRE");
			Long id2 = rs.getLong("ID");
			TiposDePlato tipo= tipos.buscarTipos_De_PlatosPorName(rs.getString("TIPO"));
			String descripcion  = rs.getString("DESCRIPCION");
			String traduccion = rs.getString("TRADUCCION");
			Double tiempo = rs.getDouble("TIEMPO");
			List<Ingrediente> ingredientes=buscarIngredientes(new Producto(false, "", null, "", "", 0,  id, null, null));
			List<Categoria> categorias=buscarCategorias(new Producto(false, "",  null, "", "", 0,  id, null, null));
			boolean personalizable=convertirABooleano(rs.getString("PERSONALIZABLE"));
			producto=(new Producto(personalizable, nombre, tipo, descripcion, traduccion, tiempo, id2, ingredientes, categorias));
		
		}
		tipos.cerrarRecursos();

		return producto;
	}

	/**
	 * Metodo que agrega el producto que entra como parametro a la base de datos.
	 * @param producto - el producto a agregar. producto !=  null
	 * <b> post: </b> se ha agregado el producto a la base de datos en la transaction actual. pendiente que el producto master
	 * haga commit para que el producto baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el producto a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addProducto(Producto producto) throws SQLException, Exception {

		String valor="select IDPRODUCTO.NEXTVAL as VALOR from dual";
		PreparedStatement prepStmt = conn.prepareStatement(valor);
		recursos.add(prepStmt);
		ResultSet rs=prepStmt.executeQuery();
		if(rs.next())
		{
			producto.setId(rs.getLong("VALOR"));
		}
		String sql = "INSERT INTO PRODUCTO VALUES (";
		sql += producto.getId()+ ",'";
		sql += producto.getNombre() + "','";
		sql += convertirTipo(producto.getTipo())+"','";
		sql+= convertirBooleano(producto.isPersonalizable())+"','";
		sql+=producto.getTraduccion()+"','";
		sql+=producto.getDescripcion()+"',";
		sql+=producto.getTiempo()+ ")";

		 prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		
		agregarIngredientes(producto);
		agregarCategorias(producto);

	}

	/**
	 * Metodo que actualiza el producto que entra como parametro en la base de datos.
	 * @param producto - el producto a actualizar. producto !=  null
	 * <b> post: </b> se ha actualizado el producto en la base de datos en la transaction actual. pendiente que el producto master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el producto.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateProducto(Producto producto) throws SQLException, Exception {

		String sql = "UPDATE PRODUCTO SET ";
		sql += "NOMBRE='" + producto.getNombre() + "',";
		sql += "TIPO='" + convertirTipo(producto.getTipo()) + "',";
		sql += "PERSONALIZABLE='" + convertirBooleano(producto.isPersonalizable()) + "',";
		sql += "TRADUCCION='" + producto.getTraduccion() + "',";
		sql += "DESCRIPCION='" + producto.getDescripcion() + "',";
		sql += "TIEMPO=" + producto.getTiempo();

		sql += " WHERE ID = " + producto.getId();


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		actualizarIngredientes(producto);
		actualizarCategorias(producto);
	}


	/**
	 * Metodo que elimina el producto que entra como parametro en la base de datos.
	 * @param producto - el producto a borrar. producto !=  null
	 * <b> post: </b> se ha borrado el producto en la base de datos en la transaction actual. pendiente que el producto master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el producto.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteProducto(Producto producto) throws SQLException, Exception {

		borrarIngredientesDePlato(producto);
		borrarCategoriasDePlato(producto);
		String sql = "DELETE FROM PRODUCTO";
		sql += " WHERE ID = " + producto.getId();
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Borra todos los productos del tipo dado por parÃ¡metro.<br>
	 * @param nombreTipo
	 */
	public void borrarProductosPorTipo(String nombreTipo) throws SQLException, Exception {
		ArrayList<Producto> productos = darProductosPorTipo(nombreTipo);
		for(Producto producto: productos)
		{
			deleteProducto(producto);
		}
		
	}
	/**
	 * Busca los platos con el criterio de tipo dado.<br>
	 * @param nombreTipo Nombre del tipo.<br>
	 * @return Lista con paltos del tipo dado.<br>
	 * @throws SQLException Si algo falla con la BD.<br>
	 * @throws Exception Si alguna otra cosa falla.
	 */
	public ArrayList<Producto> darProductosPorTipo(String nombreTipo) throws SQLException, Exception {
		ArrayList<Producto> productos = new ArrayList<Producto>();

		String sql = "SELECT * FROM PRODUCTO WHERE TIPO LIKE'" + nombreTipo + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		DAOTablaTiposProducto tipos = new DAOTablaTiposProducto();
		tipos.setConn(conn);
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			TiposDePlato tipo= tipos.buscarTipos_De_PlatosPorName(rs.getString("TIPO"));
			String descripcion  = rs.getString("DESCRIPCION");
			String traduccion = rs.getString("TRADUCCION");
			Double tiempo = rs.getDouble("TIEMPO");
			List<Ingrediente> ingredientes=buscarIngredientes(new Producto(false, "",  null, "", "", 0,  id, null, null));
			List<Categoria> categorias=buscarCategorias(new Producto(false, "",  null, "", "", 0,  id, null, null));
			boolean personalizable=convertirABooleano(rs.getString("PERSONALIZABLE"));
			productos.add(new Producto(personalizable, nombre, tipo, descripcion, traduccion, tiempo, id, ingredientes, categorias));
		
		}
		tipos.cerrarRecursos();

		return productos;
	}

	/**
	 * Lista de categorÃ­as del producto dado.<br>
	 * @param id Id del producto.<br>
	 * @return Lista de categorÃ­as.
	 * @throws SQLException Si algo falla con la BD.<br>
	 * @throws Exception Si alguna otra cosa falla.
	 */
	private List<Categoria> buscarCategorias(Producto producto) throws SQLException, Exception {
		DAOTablaCategoriaProducto prod= new DAOTablaCategoriaProducto();
		prod.setConn(conn);
		List<Categoria> c =prod.consultarPorProducto(producto.getId());
		prod.cerrarRecursos();
		return c;
	}
	/**
	 * Convierte un parÃ¡metro asumido como 0 o 1 en un valor booleano.<br>
	 * @param string ParÃ¡metro a convertir<br>
	 * @return true o false.
	 */
	private boolean convertirABooleano(String string) {
		if(string.equals("1")) return true;
		return false;
	}
	/**
	 * BUsca los ingredientes del producto por id del mismo.<br>
	 * @param id Id del producto a buscar.<br>
	 * @return Lista de ingredientes del producto.
	 */
	private List<Ingrediente> buscarIngredientes(Producto producto) throws SQLException, Exception {
		DAOTablaPerteneceAProducto ing = new DAOTablaPerteneceAProducto();
		ing.setConn(this.conn);
		List<Ingrediente> i=ing.consultarPorProducto(producto.getId());
		ing.cerrarRecursos();
		return i;
	}

	/**
	 * Agrega las cateogrÃ­as a la tabla usando el producto dado por parÃ¡metro.<br>
	 * @param producto Producto con categorÃ­as.
	 */
	private void agregarCategorias(Producto producto) throws SQLException, Exception {
		DAOTablaCategoriaProducto cat = new DAOTablaCategoriaProducto();
		cat.setConn(this.conn);
		for(Categoria c : producto.getCategorias())
			cat.asociarCategoriaYProducto(c.getNombre(), producto.getId());
		cat.cerrarRecursos();
	}
	/**
	 * Agrega los ingredientes a la tabla usando el producto dado por parÃ¡metro.<br>
	 * @param producto Producto con ingredientes.
	 */
	private void agregarIngredientes(Producto producto) throws SQLException, Exception {
		DAOTablaPerteneceAProducto ing = new DAOTablaPerteneceAProducto();
		ing.setConn(conn);
		for(Ingrediente i : producto.getIngredientes())
			ing.asociarIngredienteYProducto(i.getId(), producto.getId());
		ing.cerrarRecursos();
	}
	/**
	 * Convierte el tipo recibido como parÃ¡metro en un String.<br>
	 * @param tipo Tipo recibido.<br>
	 * @return Tipo convertido en String.
	 */
	private String convertirTipo(TiposDePlato tipo) {
		switch(tipo)
		{
		case ENTRADA: return "ENTRADA";
		case PLATO_FUERTE: return "PLATO FUERTE";
		case POSTRE: return "POSTRE";
		case BEBIDA: return "BEBIDA";
		case ACOMPANAMIENTO: return "ACOMPANAMIENTO";
		default: return null;
		}
	}
	/**
	 * Convierte un booleano en u string.<br>
	 * @param booleano Booleano.<br>
	 * @return 0 o 1.
	 */
	private String convertirBooleano(boolean booleano) {
		if(booleano) return "1";
		return "0";
	}
	/**
	 * Borra todas las categorÃ­as de un plato con id.<br>
	 * @param id Id del plato a borrar categorÃ­as.
	 */
	private void borrarCategoriasDePlato(Producto producto) throws SQLException, Exception {
		DAOTablaCategoriaProducto cat = new DAOTablaCategoriaProducto();
		cat.setConn(conn);
		cat.eliminarPorProducto(producto.getId());
		cat.cerrarRecursos();
	}
	/**
	 * Borra todos los ingredientes de un plato con id.<br>
	 * @param id Id del plato a borrar ingredientes.<br>
	 */
	private void borrarIngredientesDePlato(Producto producto) throws SQLException, Exception {
		DAOTablaPerteneceAProducto p = new DAOTablaPerteneceAProducto();
		p.setConn(this.conn);
		p.eliminarPorProducto(producto.getId());
		p.cerrarRecursos();
		
	}
	/**
	 * Actualiza las categorÃ­as de un producto dado.<br>
	 * @param producto Producto dado
	 */
	private void actualizarCategorias(Producto producto) throws SQLException, Exception {
		DAOTablaCategoriaProducto tab = new DAOTablaCategoriaProducto();
		tab.setConn(conn);
		tab.eliminarPorProducto(producto.getId());
		for(Categoria c : producto.getCategorias())
			tab.asociarCategoriaYProducto(c.getNombre(), producto.getId());
		tab.cerrarRecursos();
	}
	/**
	 * Actualiza los ingredientes de un producto dado.<br>
	 * @param producto Producto dado
	 */
	private void actualizarIngredientes(Producto producto) throws SQLException, Exception {
		DAOTablaPerteneceAProducto tab = new DAOTablaPerteneceAProducto();
		tab.setConn(conn);
		tab.eliminarPorProducto(producto.getId());
		for(Ingrediente i : producto.getIngredientes())
			tab.asociarIngredienteYProducto(i.getId(), producto.getId());
		tab.cerrarRecursos();
	}
	
	//RFC4
	/**
	 * Da el mayor número de menus al cual pertenece un producto.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	private int darMaximoNumeroMenus() throws SQLException, Exception {
		String sql = "SELECT MAX(NUM_MENUS) FROM (SELECT COUNT(*) AS NUM_MENUS FROM PERTENECE_A_MENU GROUP BY ID_PLATO)";
		PreparedStatement ps = conn.prepareStatement(sql);
		recursos.add(ps);
		ResultSet rs=ps.executeQuery();
		if(rs.next())
		return rs.getInt("MAX(NUM_MENUS)");
		else return -1;
	}
	
	/**
	 * Retorna la lista con los productos que pertenescan al mayor número de menús.
	 * @return Arraylist con los productos que cumplen la conidicion dada.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public List<Producto> darProductosMasOfrecidos() throws SQLException, Exception {
		
		List<Producto> productos = new ArrayList<Producto>();

		String sql = "SELECT ID_PLATO FROM PERTENECE_A_MENU GROUP BY ID_PLATO HAVING COUNT(*) = " + darMaximoNumeroMenus();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		
		double time =System.currentTimeMillis();
		ResultSet rs =prepStmt.executeQuery();
		time=System.currentTimeMillis()-time;
		System.out.println(sql);
		System.out.println(time);
		if(time>800) throw new Exception("EL tiempo excede a los 0.8 con "+time);		Long idActual=null;
		while(rs.next()) 
			productos.add(buscarProductoPorId(rs.getLong("ID_PLATO")));
				
		return productos;
	}
	
		/**
		 * Da el mayor número de ventas que ha tenido un producto en la BD.
		 * @throws SQLException - Cualquier error que la base de datos arroje.
		 * @throws Exception - Cualquier error que no corresponda a la base de datos
		 */
		private int darMaximoNumeroVentas() throws SQLException, Exception {
			String sql = "SELECT MAX(NUM_VENTAS_DIRECTAS + NUM_VENTAS_MENU) AS MAX FROM "
					+ "(SELECT ID_PRODUCTO, SUM(CANTIDAD) AS NUM_VENTAS_DIRECTAS FROM PEDIDO_PROD GROUP BY ID_PRODUCTO) "
					+ "JOIN (SELECT ID_PLATO, SUM(CANTIDAD) AS NUM_VENTAS_MENU FROM PEDIDO_MENU JOIN PERTENECE_A_MENU  "
					+ "USING (NOMBRE_MENU, NOMBRE_RESTAURANTE) GROUP BY ID_PLATO) ON (ID_PRODUCTO=ID_PLATO)";
			PreparedStatement ps = conn.prepareStatement(sql);
			recursos.add(ps);
			ResultSet rs= ps.executeQuery();
			if(rs.next())
			return rs.getInt("MAX");
			else return -1;
		}
		//RFC6
		/**
		 * Retorna la lista con los productos que hayan sido vendidos la mayor cantidad de veces.
		 * @return Arraylist con los productos que cumplen la conidicion dada.
		 * @throws SQLException - Cualquier error que la base de datos arroje.
		 * @throws Exception - Cualquier error que no corresponda a la base de datos
		 */
		public List<Producto> darProductosMasVendidos() throws SQLException, Exception {
			
			List<Producto> productos = new ArrayList<Producto>();

			String sql = "SELECT ID_PRODUCTO FROM " + 
					"(SELECT ID_PRODUCTO, SUM(CANTIDAD) AS NUM_VENTAS_DIRECTAS FROM PEDIDO_PROD GROUP BY ID_PRODUCTO) " + 
					"JOIN (SELECT ID_PLATO AS ID_PRODUCTO, SUM(CANTIDAD) AS NUM_VENTAS_MENU FROM PEDIDO_MENU JOIN PERTENECE_A_MENU " + 
					"USING (NOMBRE_MENU, NOMBRE_RESTAURANTE) GROUP BY ID_PLATO) USING (ID_PRODUCTO) " + 
					"WHERE NUM_VENTAS_DIRECTAS + NUM_VENTAS_MENU = " + darMaximoNumeroVentas();

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);

			double time =System.currentTimeMillis();
			ResultSet rs =prepStmt.executeQuery();
			time=System.currentTimeMillis()-time;
			System.out.println(sql);
			System.out.println(time);
			if(time>800) throw new Exception("EL tiempo excede a los 0.8 con "+time);		Long idActual=null;
			while(rs.next()) 
				productos.add(buscarProductoPorId(rs.getLong("ID_PRODUCTO")));
					
			return productos;
		}
		
		/**
		 * Da el mayor número de ventas que ha tenido un producto en la BD.
		 * @throws SQLException - Cualquier error que la base de datos arroje.
		 * @throws Exception - Cualquier error que no corresponda a la base de datos
		 */
		private int darMaximoNumeroVentasPorZona(String nombreZona) throws SQLException, Exception {
			String sql = "SELECT MAX(NUM_VENTAS_DIRECTAS + NUM_VENTAS_MENU) AS MAX FROM " + 
					"(SELECT ID_PRODUCTO, SUM(CANTIDAD) AS NUM_VENTAS_DIRECTAS FROM PEDIDO_PROD JOIN RESTAURANTE ON (NOMBRE LIKE NOMBRE_RESTAURANTE)"
					+ "WHERE NOMBRE_ZONA LIKE '" + nombreZona + "' GROUP BY ID_PRODUCTO) " + 
					"JOIN (SELECT ID_PLATO, SUM(CANTIDAD) AS NUM_VENTAS_MENU FROM (PEDIDO_MENU JOIN PERTENECE_A_MENU " + 
					"USING (NOMBRE_MENU, NOMBRE_RESTAURANTE)) JOIN RESTAURANTE ON (NOMBRE LIKE NOMBRE_RESTAURANTE)"
					+ "WHERE NOMBRE_ZONA LIKE '" + nombreZona + "' GROUP BY ID_PLATO) ON (ID_PRODUCTO=ID_PLATO)";
			PreparedStatement ps = conn.prepareStatement(sql);
			recursos.add(ps);
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			rs.getInt("MAX");
			return -1;
		}
		
		/**
		 * Retorna la lista con los productos que hayan sido vendidos la mayor cantidad de veces.
		 * @return Arraylist con los productos que cumplen la conidicion dada.
		 * @throws SQLException - Cualquier error que la base de datos arroje.
		 * @throws Exception - Cualquier error que no corresponda a la base de datos
		 */
		public List<Producto> darProductosMasVendidosPorZona(String nombreZona) throws SQLException, Exception {
			
			List<Producto> productos = new ArrayList<Producto>();

			String sql = "SELECT ID_PRODUCTO FROM " + 
					"(SELECT ID_PRODUCTO, SUM(CANTIDAD) AS NUM_VENTAS_DIRECTAS FROM PEDIDO_PROD JOIN RESTAURANTE ON (NOMBRE LIKE NOMBRE_RESTAURANTE)"
					+ "WHERE NOMBRE_ZONA LIKE '" + nombreZona + "' GROUP BY ID_PRODUCTO) " + 
					"JOIN (SELECT ID_PLATO AS ID_PRODUCTO, SUM(CANTIDAD) AS NUM_VENTAS_MENU FROM (PEDIDO_MENU JOIN PERTENECE_A_MENU " + 
					"USING (NOMBRE_MENU, NOMBRE_RESTAURANTE)) JOIN RESTAURANTE ON (NOMBRE LIKE NOMBRE_RESTAURANTE)"
					+ "WHERE NOMBRE_ZONA LIKE '" + nombreZona + "' GROUP BY ID_PLATO) USING (ID_PRODUCTO)" + 
					"WHERE NUM_VENTAS_DIRECTAS + NUM_VENTAS_MENU = " + darMaximoNumeroVentasPorZona(nombreZona);

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			
			while(rs.next()) 
				productos.add(buscarProductoPorId(rs.getLong("ID_PRODUCTO")));
					
			return productos;
		}
		
		/**
		 * Retorna la lista con los productos que hayan sido vendidos la mayor cantidad de veces.
		 * @return Arraylist con los productos que cumplen la conidicion dada.
		 * @throws SQLException - Cualquier error que la base de datos arroje.
		 * @throws Exception - Cualquier error que no corresponda a la base de datos
		 */
		public List<Producto> darProductosMasYMenosVendidosPorDiaDeLaSemana(String dia) throws SQLException, Exception {
			
			List<Producto> productos = new ArrayList<Producto>();

			String sql = "SELECT ID, SUM(VENTAS) AS TOTAL FROM (SELECT ID_PLATO AS ID, SUM(CANTIDAD) AS VENTAS " + 
					"FROM (PEDIDO_MENU JOIN PERTENECE_A_MENU USING (NOMBRE_MENU, NOMBRE_RESTAURANTE)) JOIN CUENTA ON (NUMERO_CUENTA = NUMEROCUENTA)" + 
					"WHERE TO_CHAR(FECHA, 'DAY') = '" + dia + "' GROUP BY ID_PLATO UNION ALL " + 
					"SELECT ID_PRODUCTO AS ID, SUM(CANTIDAD) AS VENTAS FROM PEDIDO_PROD JOIN CUENTA ON(NUMERO_CUENTA = NUMEROCUENTA) " + 
					"WHERE TO_CHAR(FECHA, 'DAY') = '" + dia + "' GROUP BY ID_PRODUCTO) " + 
					"GROUP BY ID ORDER BY TOTAL";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			
			if(rs.first())
				productos.add(buscarProductoPorId(rs.getLong("ID")));
			if(rs.last())
				productos.add(buscarProductoPorId(rs.getLong("ID")));

					
			return productos;
		}
		
}
