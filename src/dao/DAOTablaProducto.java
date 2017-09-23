/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: ProductoAndes
 * Autor: Juan Felipe García - jf.garcia268@uniandes.edu.co
 * -------------------------------------------------------------------
 */
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
 * @author Monitores 2017-20
 */
public class DAOTablaProducto {


	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
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
	 * Metodo que inicializa la connection del DAO a la base de datos con la conexión que entra como parametro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection con){
		this.conn = con;
	}


	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los productos de la base de datos
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
			Double prcio =rs.getDouble("PRECIO");
			Double costoProduccion=rs.getDouble("COSTOPRODUCCION");
			String descripcion  = rs.getString("DESCRIPCION");
			String traduccion = rs.getString("TRADUCCION");
			Double tiempo = rs.getDouble("TIEMPO");
			ArrayList<Ingrediente> ingredientes=buscarIngredientes(id);
			ArrayList<Categoria> categorias=buscarCategorias(id);
			boolean personalizable=convertirABooleano(rs.getString("PERSONALIZABLE"));
			productos.add(new Producto(personalizable, nombre, prcio, tipo, descripcion, traduccion, tiempo, costoProduccion, id, ingredientes, categorias));
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
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			TiposDePlato tipo= tipos.buscarTipos_De_PlatosPorName(rs.getString("TIPO"));
			Double prcio =rs.getDouble("PRECIO");
			Double costoProduccion=rs.getDouble("COSTOPRODUCCION");
			String descripcion  = rs.getString("DESCRIPCION");
			String traduccion = rs.getString("TRADUCCION");
			Double tiempo = rs.getDouble("TIEMPO");
			ArrayList<Ingrediente> ingredientes=buscarIngredientes(id);
			ArrayList<Categoria> categorias=buscarCategorias(id);
			boolean personalizable=convertirABooleano(rs.getString("PERSONALIZABLE"));
			productos.add(new Producto(personalizable, nombre, prcio, tipo, descripcion, traduccion, tiempo, costoProduccion, id, ingredientes, categorias));
		
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
		if(rs.next()) {
			String nombre = rs.getString("NOMBRE");
			Long id2 = rs.getLong("ID");
			TiposDePlato tipo= tipos.buscarTipos_De_PlatosPorName(rs.getString("TIPO"));
			Double prcio =rs.getDouble("PRECIO");
			Double costoProduccion=rs.getDouble("COSTOPRODUCCION");
			String descripcion  = rs.getString("DESCRIPCION");
			String traduccion = rs.getString("TRADUCCION");
			Double tiempo = rs.getDouble("TIEMPO");
			ArrayList<Ingrediente> ingredientes=buscarIngredientes(id);
			ArrayList<Categoria> categorias=buscarCategorias(id);
			boolean personalizable=convertirABooleano(rs.getString("PERSONALIZABLE"));
			producto=(new Producto(personalizable, nombre, prcio, tipo, descripcion, traduccion, tiempo, costoProduccion, id2, ingredientes, categorias));
		
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

		String sql = "INSERT INTO PRODUCTO VALUES (";
		sql += "IDPRODUCTO.NEXTVAL" + ",'";
		sql += producto.getNombre() + "','";
		sql += convertirTipo(producto.getTipo())+"','";
		sql+= convertirBooleano(producto.isPersonalizable())+"',";
		sql+=producto.getPrecio()+",'";
		sql+=producto.getTraduccion()+"','";
		sql+=producto.getDescripcion()+"',";
		sql+=producto.getCostoProduccion()+",";
		sql+=producto.getTiempo()+ ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
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
		sql += "PERZONALIZABLE='" + convertirBooleano(producto.isPersonalizable()) + "',";
		sql += "PRECIO=" + producto.getPrecio() + ",";
		sql += "TRADUCCION='" + producto.getTraduccion() + "',";
		sql += "DESCRIPCION='" + producto.getDescripcion() + "',";
		sql += "COSTOPRODUCCION=" + producto.getCostoProduccion() + ",";
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

		borrarIngredientesDePlato(producto.getId());
		borrarCategoriasDePlato(producto.getId());
		String sql = "DELETE FROM PRODUCTO";
		sql += " WHERE ID = " + producto.getId();
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Borra todos los productos del tipo dado por parámetro.<br>
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
		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			TiposDePlato tipo= tipos.buscarTipos_De_PlatosPorName(rs.getString("TIPO"));
			Double prcio =rs.getDouble("PRECIO");
			Double costoProduccion=rs.getDouble("COSTOPRODUCCION");
			String descripcion  = rs.getString("DESCRIPCION");
			String traduccion = rs.getString("TRADUCCION");
			Double tiempo = rs.getDouble("TIEMPO");
			ArrayList<Ingrediente> ingredientes=buscarIngredientes(id);
			ArrayList<Categoria> categorias=buscarCategorias(id);
			boolean personalizable=convertirABooleano(rs.getString("PERSONALIZABLE"));
			productos.add(new Producto(personalizable, nombre, prcio, tipo, descripcion, traduccion, tiempo, costoProduccion, id, ingredientes, categorias));
		
		}
		tipos.cerrarRecursos();

		return productos;
	}

	/**
	 * Lista de categorías del producto dado.<br>
	 * @param id Id del producto.<br>
	 * @return Lista de categorías.
	 */
	private ArrayList<Categoria> buscarCategorias(Long id) {
		DAOTablaCategoriaProducto prod= new DAOTablaCategoriaProducto();
		prod.setConn(conn);
		ArrayList<Categoria> c =prod.buscarCategoriasPorId(id);
		prod.cerrarRecursos();
		return c;
	}
	/**
	 * Convierte un parámetro asumido como 0 o 1 en un valor booleano.<br>
	 * @param string Parámetro a convertir<br>
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
	private ArrayList<Ingrediente> buscarIngredientes(Long id) {
		DAOTablaPerteneceAProducto ing = new DAOTablaPerteneceAProducto();
		ing.setConn(this.conn);
		ArrayList<Ingrediente> i=ing.buscarIngredientesPorId(id);
		ing.cerrarRecursos();
		return i;
	}

	/**
	 * Agrega las cateogrías a la tabla usando el producto dado por parámetro.<br>
	 * @param producto Producto con categorías.
	 */
	private void agregarCategorias(Producto producto) throws SQLException, Exception {
		DAOTablaCategoriaProducto cat = new DAOTablaCategoriaProducto();
		cat.setConn(this.conn);
		cat.agregarCategoriasPorId(producto.getId(),producto.getCategorias());
	}
	/**
	 * Agrega los ingredientes a la tabla usando el producto dado por parámetro.<br>
	 * @param producto Producto con ingredientes.
	 */
	private void agregarIngredientes(Producto producto) {
		DAOTablaPerteneceAProducto ing = new DAOTablaPerteneceAProducto();
		ing.setConn(conn);
		ing.agregarIngredientesPorId(producto.getId(),producto.getIngredientes());
	}
	/**
	 * Convierte el tipo recibido como parámetro en un String.<br>
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
		case ACOMPANAMIENTO: return "ACOMPAÑAMIENTO";
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
	 * Borra todas las categorías de un plato con id.<br>
	 * @param id Id del plato a borrar categorías.
	 */
	private void borrarCategoriasDePlato(Long id) {
		DAOTablaCategoriaProducto cat = new DAOTablaCategoriaProducto();
		cat.borrarCategoriasPorId(id);
		cat.cerrarRecursos();
	}
	/**
	 * Borra todos los ingredientes de un plato con id.<br>
	 * @param id Id del plato a borrar ingredientes.<br>
	 */
	private void borrarIngredientesDePlato(Long id) {
		DAOTablaPerteneceAProducto p = new DAOTablaPerteneceAProducto();
		p.borrarIngredientesPorIdPlato(id);
		p.cerrarRecursos();
		
	}
	/**
	 * Actualiza las categorías de un producto dado.<br>
	 * @param producto Producto dado
	 */
	private void actualizarCategorias(Producto producto) {
		DAOTablaCategoriaProducto tab = new DAOTablaCategoriaProducto();
		tab.actualizarCategoria(producto.getId(),producto.getCategorias());
		tab.cerrarRecursos();
	}
	/**
	 * Actualiza los ingredientes de un producto dado.<br>
	 * @param producto Producto dado
	 */
	private void actualizarIngredientes(Producto producto) {
		DAOTablaPerteneceAProducto tab = new DAOTablaPerteneceAProducto();
		tab.actualizarIngredientes(producto.getId(),producto.getCategorias());
		tab.cerrarRecursos();
	}
	

}
