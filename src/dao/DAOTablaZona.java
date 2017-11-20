package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rfc.ContenedoraZonaCategoriaProducto;
import rfc.ProductoEnTotal;
import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación
 * @author s.guzmanm
 */
public class DAOTablaZona {


	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOZona
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaZona() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos los zonas de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM ZONAS;
	 * @return Arraylist con los zonas de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Zona> darZonas() throws SQLException, Exception {

		String sql = "SELECT * FROM ZONA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		return convertirEntidadZona(rs);
	}


	

	/**
	 * Metodo que busca el/los zonas con el nombre que entra como parametro.
	 * @param name - Nombre de el/los zonas a buscar
	 * @return ArrayList con los zonas encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Zona buscarZonasPorName(String name) throws SQLException, Exception {

		String sql = "SELECT * FROM ZONA WHERE NOMBRE LIKE '" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		ArrayList<Zona> zonas= convertirEntidadZona(rs);
		if(zonas.isEmpty()) return null;
		return zonas.get(0);
	}
	

	/**
	 * Metodo que busca el/los zonas con el nombre que entra como parametro.
	 * @param name - Nombre de el/los zonas a buscar
	 * @return ArrayList con los zonas encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ZonaMinimum buscarZonasMinimumPorName(String name) throws SQLException, Exception {

		String sql = "SELECT * FROM ZONA WHERE NOMBRE LIKE '" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		ArrayList<ZonaMinimum> zonas= convertirEntidadZonaMinimum(rs);
		if(zonas.isEmpty()) return null;
		return zonas.get(0);
	}

	/**
	 * Metodo que agrega la zona que entra como parametro a la base de datos.
	 * @param zona - la zona a agregar. zona !=  null
	 * <b> post: </b> se ha agregado la zona a la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que la zona baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la zona a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addZona(Zona zona) throws SQLException, Exception {

		String sql = "INSERT INTO ZONA VALUES (";
		sql += "'"+zona.getNombre() + "',";
		sql += zona.getCapacidad() + ",";
		sql +="'"+ convertirBooleano(zona.isIngresoEspecial())+"',";
		sql+="'"+convertirBooleano(zona.isAbiertaActualmente())+"',";
		sql+=0+")";
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		insertarCondicionesTecnicas(zona);
	}
	
	

	/**
	 * Metodo que actualiza la zona que entra como parámetro en la base de datos.
	 * @param zona - la zona a actualizar. zona !=  null
	 * <b> post: </b> se ha actualizado la zona en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la zona.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateZona(Zona zona) throws SQLException, Exception {		
		List<MesaMinimum> mesas=zona.getMesas();
		int capacidadTotal=0;
		for(MesaMinimum m:mesas)
			capacidadTotal+=m.getCapacidadOcupada();
		
		if(capacidadTotal>zona.getCapacidadOcupada()) throw new Exception("Hay mesas que están ocupadas actualmente con un valor mayor a la capacidad que quiere asignar.");
		
		String sql = "UPDATE ZONA SET ";
		sql += "CAPACIDAD=" + zona.getCapacidad() + ",";
		sql+="INGRESOESPECIAL='"+convertirBooleano(zona.isIngresoEspecial())+"',";
		sql+="ABIERTAACTUALMENTE='"+convertirBooleano(zona.isAbiertaActualmente())+"',";
		sql+="CAPACIDADOCUPADA="+zona.getCapacidadOcupada();
		sql += " WHERE NOMBRE LIKE '" + zona.getNombre()+"'";


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina la zona que entra como parametro en la base de datos.
	 * @param zona - la zona a borrar. zona !=  null
	 * <b> post: </b> se ha borrado la zona en la base de datos en la transaction actual. pendiente que la master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la zona.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteZona(Zona zona) throws SQLException, Exception {

		borrarRestauranteMinimums(zona);
		borrarCondiciones(zona);
		modificarPreferencias(zona.getNombre());
		borrarReservas(zona.getNombre());
		borrarMesas(zona.getNombre());
		
		String sql = "DELETE FROM ZONA";
		sql += " WHERE NOMBRE LIKE '" + zona.getNombre()+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	/**
	 * Borra las mesas pertenecientes a la zona.<br>
	 * @param nombre Nombre de la zona.
	 * @throws SQLException Si hay errores en la BD.
	 * @throws Exception SI hay otro error.
	 */
	private void borrarMesas(String nombre) throws SQLException, Exception {
		DAOTablaMesa mesa = new DAOTablaMesa();
		mesa.setConn(conn);
		mesa.borrarMesasPorZona(nombre);
		mesa.cerrarRecursos();
		
	}

	/**
	 * Retorna los productos dados por zona y categoría en una fecha inicial y final.<br>
	 * @param fechaInicial Fecha inicial.<br>
	 * @param fechaFinal Fecha final.<br>
	 * @param nombreRestaurante Nombre del restaurante.<br>
	 * @return Listado de contenedoraZonaCategoriaProducto.<br>
	 * @throws SQLException Si hay algún error en la BD.<br>
	 * @throws Exception SI hay cualquier otro error.
	 */
	public List<ContenedoraZonaCategoriaProducto> darProductosTotalesPorZonaYCategoria(Date fechaInicial, Date fechaFinal, String nombreRestaurante) throws SQLException, Exception
	{
		if(nombreRestaurante==null) nombreRestaurante="";
		else nombreRestaurante="AND RESTAURANTE.NOMBRE LIKE '"+nombreRestaurante+"'";
		List<ContenedoraZonaCategoriaProducto> list= new ArrayList<>();
		ContenedoraZonaCategoriaProducto c=null;
		CategoriaProducto cp=null;
		ProductoEnTotal p=null;
		String zonaActual=null;
		String categoriaActual=null;
		String temp="";
		//Tabla de información
		String table="SELECT NOMBRE_ZONA, ID, SUM(CANTIDADTOTAL1+CANTIDADTOTAL2)AS CANTIDAD FROM"
				+ "           ( SELECT NOMBRE_ZONA ,ID,NVL(CANTIDADTOTAL1,0) AS CANTIDADTOTAL1 "
				+ "          FROM"
				+ "				  (SELECT RESTAURANTE.NOMBRE_ZONA AS NOMBRE_ZONA, PRODUCTO.ID AS ID, SUM(PEDIDO_MENU.CANTIDAD) AS CANTIDADTOTAL1"
				+ "					 FROM PRODUCTO, PEDIDO_MENU, PERTENECE_A_MENU, RESTAURANTE, CUENTA"
				+ "					 WHERE PEDIDO_MENU.NOMBRE_MENU LIKE PERTENECE_A_MENU.NOMBRE_MENU "
				+ "					 AND PEDIDO_MENU.NOMBRE_RESTAURANTE LIKE PERTENECE_A_MENU.NOMBRE_RESTAURANTE"
				+ "					 AND RESTAURANTE.NOMBRE LIKE PEDIDO_MENU.NOMBRE_RESTAURANTE AND CUENTA.NUMEROCUENTA LIKE PEDIDO_MENU.NUMERO_CUENTA"
				+ "					AND	CUENTA.FECHA >="+dateFormat(fechaInicial)+" AND CUENTA.FECHA<="+dateFormat(fechaFinal)
				+ "					 AND PRODUCTO.ID = PERTENECE_A_MENU.ID_PLATO " + nombreRestaurante
				+ "					 GROUP BY RESTAURANTE.NOMBRE_ZONA,PRODUCTO.ID) NATURAL FULL OUTER JOIN PRODUCTO  ) "
				+ "NATURAL FULL OUTER JOIN  (SELECT RESTAURANTE.NOMBRE_ZONA AS NOMBRE_ZONA,PEDIDO_PROD.ID_PRODUCTO AS ID2,SUM(PEDIDO_PROD.CANTIDAD) AS CANTIDADTOTAL2 "
				+ "	   FROM PEDIDO_PROD, RESTAURANTE, CUENTA "
				+ "	 WHERE PEDIDO_PROD.NOMBRE_RESTAURANTE LIKE RESTAURANTE.NOMBRE "+ nombreRestaurante +"AND CUENTA.NUMEROCUENTA LIKE PEDIDO_PROD.NUMERO_CUENTA "
						+ "					AND	CUENTA.FECHA >="+dateFormat(fechaInicial)+" AND CUENTA.FECHA<="+dateFormat(fechaFinal)
				+ "  GROUP BY RESTAURANTE.NOMBRE_ZONA,PEDIDO_PROD.ID_PRODUCTO) "
				+ " WHERE NOMBRE_ZONA IS NOT NULL AND ID IS NOT NULL"
				+ " GROUP BY NOMBRE_ZONA,ID";
		//Comando sql usando la tabla anterior
		String sql="SELECT T.NOMBRE_ZONA, CATEGORIA_PRODUCTO.NOMBRE_CATEGORIA, T.ID,  T.CANTIDAD, (INFO_PROD_REST.COSTO*T.CANTIDAD) AS COSTOTOTAL, (INFO_PROD_REST.PRECIO* T.CANTIDAD) AS VALORTOTAL "
				+ "FROM CATEGORIA_PRODUCTO, INFO_PROD_REST,(" + table+")T "+
				"WHERE T.ID = CATEGORIA_PRODUCTO.ID_PRODUCTO AND INFO_PROD_REST.ID_PRODUCTO=T.ID"
				+" GROUP BY T.NOMBRE_ZONA, CATEGORIA_PRODUCTO.NOMBRE_CATEGORIA, T.ID, (INFO_PROD_REST.PRECIO*T.CANTIDAD), T.CANTIDAD, (INFO_PROD_REST.COSTO*T.CANTIDAD)"
				+ " ORDER BY NOMBRE_ZONA, NOMBRE_CATEGORIA, VALORTOTAL DESC ";
		System.out.println(sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		
		double time =System.currentTimeMillis();
		ResultSet rs =prepStmt.executeQuery();
		time=System.currentTimeMillis()-time;
		System.out.println(time);
		if(time>800) throw new Exception("EL tiempo excede a los 0.8 con "+time);		Long idActual=null;
		c= new ContenedoraZonaCategoriaProducto();
		while(rs.next())
		{
			System.out.println(rs.getString("NOMBRE_ZONA")+" "+rs.getString("NOMBRE_CATEGORIA")+" "+rs.getLong("ID")+" "+rs.getInt("CANTIDAD")+" "+rs.getDouble("COSTOTOTAL")+" "+rs.getDouble("VALORTOTAL"));
			p= new ProductoEnTotal(rs.getLong("ID"),rs.getInt("CANTIDAD"), rs.getDouble("VALORTOTAL"),rs.getDouble("COSTOTOTAL"));
			temp=rs.getString("NOMBRE_ZONA");
			if(zonaActual==null ) 
			{
				c= new ContenedoraZonaCategoriaProducto();
				zonaActual=temp;
				c.setNombreZona(zonaActual);
			}
			if(!temp.equals(zonaActual))
			{
				c.getCategoriaProductos().add(cp);
				list.add(c);
				c= new ContenedoraZonaCategoriaProducto();
				zonaActual=temp;
				c.setNombreZona(zonaActual);
				categoriaActual=null;
			}
			temp=rs.getString("NOMBRE_CATEGORIA");
			if(categoriaActual==null)
			{
				cp=new CategoriaProducto();
				categoriaActual=temp;
				cp.setNombreCategoria(categoriaActual);
			}
			if(!temp.equals(categoriaActual))
			{
				c.getCategoriaProductos().add(cp);
				cp=new CategoriaProducto();
				categoriaActual=temp;
				cp.setNombreCategoria(categoriaActual);
			}
			cp.getProductos().add(p);
		}
		if(p!=null)
		{
			c.getCategoriaProductos().add(cp);
			list.add(c);
		}
		return list;
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
	 * Crea un arreglo de zonas con el set de resultados pasado por parámetro.<br>
	 * @param rs Set de resultados.<br>
	 * @return zonas Lista de zonas convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	public ArrayList<Zona> convertirEntidadZona(ResultSet rs) throws SQLException, Exception
	{
		ArrayList<Zona> zonas = new ArrayList<>();
		while (rs.next()) {
			int capacidad=rs.getInt("CAPACIDAD");
			boolean ingresoEspecial=false;
			if(rs.getString("INGRESOESPECIAL").equals("1")) ingresoEspecial=true;
			boolean abiertaActualmente=false;
			if(rs.getString("ABIERTAACTUALMENTE").equals("1")) abiertaActualmente=true;
			int capacidadOcupada=rs.getInt("CAPACIDADOCUPADA");
			String nombre=rs.getString("NOMBRE");
			List<CondicionTecnica> condiciones= accederACondicionesZona(nombre);
			List<RestauranteMinimum> restaurantes= accederARestauranteMinimums(nombre);
			List<MesaMinimum> mesas= accederAMesasMinimums(nombre);
			zonas.add(new Zona(capacidad, ingresoEspecial, abiertaActualmente, capacidadOcupada, nombre, condiciones, restaurantes,mesas));
		}
		return zonas;
	}
	/**
	 * Retorna una lista de mesas a nivel minimum.<br>
	 * @param nombre Nombre de la zona.<br>
	 * @return Mesas de la zona.<br>
	 * @throws SQLException Si hay alguna excepción en la base de datos.<br>
	 * @throws Exception SI hay algún otro error.
	 */
	private List<MesaMinimum> accederAMesasMinimums(String nombre) throws SQLException, Exception {
		DAOTablaMesa dao = new DAOTablaMesa();
		List<MesaMinimum> list= new ArrayList<>();
		dao.setConn(conn);
		list=dao.buscarMesasPorZona(nombre);
		dao.cerrarRecursos();
		return list;
	}

	/**
	 * Crea un arreglo de zonas con el set de resultados pasado por parámetro.<br>
	 * @param rs Set de resultados.<br>
	 * @return zonas Lista de zonas convertidas.<br>
	 * @throws SQLException Algún problema de la base de datos.<br>
	 * @throws Exception Cualquier otra excepción.
	 */
	private ArrayList<ZonaMinimum> convertirEntidadZonaMinimum(ResultSet rs) throws SQLException, Exception
	{
		ArrayList<ZonaMinimum> zonas = new ArrayList<>();
		while (rs.next()) {
			int capacidad=rs.getInt("CAPACIDAD");
			boolean ingresoEspecial=false;
			if(rs.getString("INGRESOESPECIAL").equals("1")) ingresoEspecial=true;
			boolean abiertaActualmente=false;
			if(rs.getString("ABIERTAACTUALMENTE").equals("1")) abiertaActualmente=true;
			int capacidadOcupada=rs.getInt("CAPACIDADOCUPADA");
			String nombre=rs.getString("NOMBRE");
			zonas.add(new ZonaMinimum(capacidad, ingresoEspecial, abiertaActualmente, capacidadOcupada, nombre));
		}
		return zonas;
	}
	/**
	 * Retorna las condiciones técnicas del nombre de la zona dada por parámetro.<br>
	 * @param nombre Nombre de la zona.<br>
	 * @return Lista de condiciones técnicas.
	 */
	private ArrayList<CondicionTecnica> accederACondicionesZona(String nombre) throws SQLException, Exception {
		DAOTablaCondicionesZona condiciones= new DAOTablaCondicionesZona();
		condiciones.setConn(this.conn);
		return condiciones.consultarZona(nombre);
	}
	/**
	 * Retorna una lista de los restuarantes presentes en esta zona.<br>
	 * @param nombreZona Nombre de la zona a buscar.<br>
	 * @return Lista de los restaurantes presentes en la zona.
	 */
	private List<RestauranteMinimum> accederARestauranteMinimums(String nombreZona) throws SQLException, Exception{
		DAOTablaRestaurante restaurante = new DAOTablaRestaurante();
		restaurante.setConn(this.conn);
		return restaurante.consultarPorZona(nombreZona);
	}
	/**
	 * Inserta las condiciones técnicas en la tabla DAO de CondicionesZona.<br>
	 * @param zona Zona a insertar sus condiciones.<br>
	 * @throws SQLException Si hay algún error en la base de datos<br>
	 * @throws Exception Si hay algún otro error en general.
	 */
	private void insertarCondicionesTecnicas(Zona zona) throws SQLException, Exception {
		DAOTablaCondicionesZona condiciones= new DAOTablaCondicionesZona();
		condiciones.setConn(this.conn);
		condiciones.insertarPorZona(zona.getNombre(),zona.getCondiciones());
		condiciones.cerrarRecursos();
	}
	
	/**
	 * Convierte un booleano en un caracter 0(false) o 1 (true)
	 * @param booleano
	 * @return 0 o 1
	 */
	private String convertirBooleano(boolean booleano) {
		if(booleano) return "1";
		return "0";
	}
	/**
	 * Borra los restaurantes relacionados a dicha zona.<br>
	 * @param zona Zona de donde se borran
	 */
	private void borrarRestauranteMinimums (Zona zona) throws SQLException, Exception
	{
		DAOTablaRestaurante restaurante= new DAOTablaRestaurante();
		restaurante.setConn(this.conn);
		restaurante.eliminarRestaurantes(zona.getRestaurantes());
		restaurante.cerrarRecursos();
	}
	/**
	 * Borra las condiciones técnicas relacionadas a dicha zona.<br>
	 * @param zona Zona de donde se borran.
	 * 
	 */
	private void borrarCondiciones(Zona zona) throws SQLException, Exception
	{
		DAOTablaCondicionesZona condiciones = new DAOTablaCondicionesZona();
		condiciones.setConn(this.conn);
		condiciones.eliminarCondicionesPorZona(zona.getNombre());
		condiciones.cerrarRecursos();
	}
	/**
	 * Modifica el valor de las preferencias de usuario a nulo cuando se borra la zona.<br>
	 * @param nombreZona Nombre de la zona para analizar preferencias.
	 */
	private void modificarPreferencias(String nombreZona) throws SQLException, Exception
	{
		DAOTablaPreferenciaZona preferencia = new DAOTablaPreferenciaZona();
		preferencia.setConn(this.conn);
		preferencia.modificarPorZonaEliminada(nombreZona);
		preferencia.cerrarRecursos();
	}
	/**
	 * Elimina reservas hechas a esta zona.<br>
	 * @param nombreZona Nombre de la zona para analizar reservas.
	 * @throws SQLException Alg�n problema de la base de datos.<br>
	 */
	private void borrarReservas(String nombreZona) throws SQLException
	{
		DAOTablaReserva reserva= new DAOTablaReserva();
		reserva.setConn(this.conn);
		reserva.borrarPorZona(nombreZona);
		reserva.cerrarRecursos();
	}
	
}

