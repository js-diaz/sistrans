
package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.*;
import vos.Criterio.Agregaciones;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación utilizando todos los criterios para requerimientos de búsqueda.
 * @author s.guzmanm
 */
public class DAOTablaCriterio {


	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOCriterio
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaCriterio() {
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
	 * Da todos los criterios básicos de una zona (Sus atributos).<br>
	 * @return Criterios básicos de la zona en forma de lista.<br>
	 * @throws SQLException SI hay un error en la BD.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	public List<Criterio> darCriteriosBasicosZona() throws SQLException, Exception
	{
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'ZONA' OR TABLE_NAME "
				+ "LIKE 'RESTAURANTE' OR TABLE_NAME LIKE 'INFO_PROD_REST' OR TABLE_NAME LIKE 'PEDIDO_PROD' OR TABLE_NAME LIKE 'CUENTA'))";
		String sql = "SELECT * FROM "+table;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<Criterio> c=new ArrayList<>();
		while (rs.next()) {
			String name2 = rs.getString("COLUMN_NAME");
			c.add(new Criterio(name2));
		}

		return c;
	}
	/**
	 * Metodo que busca el criterios con el nombre que entra como parámetro.
	 * @param name - Nombre a buscar
	 * @return ArrayList con los criterios encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Criterio buscarCriteriosZona(String name) throws SQLException, Exception {
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'ZONA' OR TABLE_NAME "
				+ "LIKE 'RESTAURANTE' OR TABLE_NAME LIKE 'INFO_PROD_REST' OR TABLE_NAME LIKE 'PEDIDO_PROD' OR TABLE_NAME LIKE 'CUENTA'))";
		String sql = "SELECT * FROM "+table+" WHERE COLUMN_NAME LIKE'" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		Criterio c=null;
		if (rs.next()) {
			String name2 = rs.getString("COLUMN_NAME");
			c=new Criterio(name2);
		}

		return c;
	}
	/**
	 * Retorna el tipo de dato de cada una de las columnas de zona usados en el sistema.<br>
	 * @return Contenedora de información con los tipos de dato.<br>
	 * @throws SQLException Excepción de la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public ContenedoraInformacion tiposDeDatoZona() throws SQLException, Exception
	{
		ContenedoraInformacion lista= new ContenedoraInformacion();
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'ZONA' OR TABLE_NAME "
				+ "LIKE 'RESTAURANTE' OR TABLE_NAME LIKE 'INFO_PROD_REST' OR TABLE_NAME LIKE 'PEDIDO_PROD' OR TABLE_NAME LIKE 'CUENTA'))";
		String sql = "SELECT COLUMN_NAME, DATA_TYPE FROM "+table;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			lista.agregarInformacion(rs.getString("COLUMN_NAME"), rs.getString("DATA_TYPE"));
		}
		return lista;
	}
	
	/**
	 * Metodo que busca el criterios con el nombre que entra como parámetro.
	 * @param name - Nombre a buscar
	 * @return ArrayList con los criterios encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Criterio buscarCriteriosProducto(String name) throws SQLException, Exception {
		String table="SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'ZONA' OR TABLE_NAME "
				+ "LIKE 'RESTAURANTE' OR TABLE_NAME LIKE 'INFO_PROD_REST' OR TABLE_NAME LIKE 'PEDIDO_PROD' OR TABLE_NAME LIKE 'CUENTA')";
		String sql = "SELECT * FROM "+table+" WHERE NOMBRE LIKE'" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		Criterio c=null;
		if (rs.next()) {
			String name2 = rs.getString("NOMBRE");
			c=new Criterio(name2);
		}

		return c;
	}
	
	/**
	 * Método para generar lista filtrada de la información total de las zonas.<br>
	 * @param criteriosOrganizacion Listado de criterios de organización.<br>
	 * @param criteriosAgrupamiento Listado de criterios de agrupamiento.<br>
	 * @param agregacionesSeleccion Listado de selección de agregaciones.<br>
	 * @param operacionesWhere Operaciones con where.<br>
	 * @param operacionesHaving Operaciones con having.<br>
	 * @return Listado de contenedora de información con los datos dados.<br>
	 * @throws SQLException Si hay un error con la base de datos.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	public List<ContenedoraInformacion> generarListaFiltradaZonas(String nombreZona,
			List<CriterioOrden> criteriosOrganizacion,List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregacionesSeleccion, CriterioVerdad operacionesWhere, 
			CriterioVerdadHaving operacionesHaving) throws SQLException, Exception {
		//Verifica que no haya repeticiones de criterios
		List<CriterioOrden> existentesOrd=new ArrayList<>();
		List<Criterio> existentesAgrup= new ArrayList<>();
		List<CriterioAgregacion> agreSelec=new ArrayList<>();
		if(criteriosAgrupamiento!=null)
		for(Criterio c: criteriosAgrupamiento)
		{
			if(existentesAgrup.indexOf(c)>=0) continue;
			existentesAgrup.add(c);
		}
		if(criteriosOrganizacion!=null)
		for(CriterioOrden c: criteriosOrganizacion)
		{
			if(existentesOrd.indexOf(c)>=0) continue;
			if(existentesAgrup.indexOf(c)<0) throw new Exception("Los criterios no hacen parte del agrupamiento establecido");
			existentesOrd.add(c);
		}
		if(agregacionesSeleccion!=null)
		for(CriterioAgregacion a: agregacionesSeleccion)
		{
			if(agreSelec.indexOf(a)>=0) continue;
			agreSelec.add(a);
		}
		//Empieza la creación de los datos del query
		//El from debería ser con ZONA, RESTAURANTE, INFO_PROD_REST, PEDIDO_PROD, MENU, PEDIDO_MENU, CUENTA. Se busca una unión de lo que respecta a producto y menú. En una tabla aparte.
		String from ="FROM ZONA";
		String select="SELECT ";
		String groupBy="";
		String orderBy="";
		String where="WHERE NOMBRE LIKE '"+nombreZona+"'";
		String having="";
		//Verifica agrupaciones
		if(existentesAgrup.size()>0)
		{
			if(buscarCriteriosZona(simplificarAgrupacion(existentesAgrup.get(0).getNombre()))==null) 
				throw new Exception("Uno de los criterios no existe");
			select+=existentesAgrup.get(0).getNombre();
			groupBy+="GROUP BY "+existentesAgrup.get(0).getNombre();
			existentesAgrup.remove(0);
			for(Criterio c: existentesAgrup)
			{
				if(buscarCriteriosZona(simplificarAgrupacion(c.getNombre()))==null) 
					throw new Exception("Uno de los criterios no existe");
				groupBy+=", "+c.getNombre();
				select+=", "+c.getNombre();
			}
			for(CriterioAgregacion a:agreSelec) 
			{
				if(buscarCriteriosZona(simplificarAgrupacion(a.getNombre()))==null) 
					throw new Exception("Uno de los criterios no existe");
				select+=","+a.getNombre();
			}
		}
		else select+="*";
		//Verifica órdenes
		if(existentesOrd.size()>0)
		{
			if(buscarCriteriosZona(simplificarOrden(existentesOrd.get(0).getNombre()))==null) 
				throw new Exception("Uno de los criterios no existe");
			orderBy+="ORDER BY "+existentesOrd.get(0).getNombre();
			existentesOrd.remove(0);
			for(CriterioOrden c: existentesOrd)
			{
				if(buscarCriteriosZona(simplificarOrden(c.getNombre()))==null) 
					throw new Exception("Uno de los criterios no existe");
				orderBy+=", "+c.getNombre();
			}
		}
		String operaciones="";
		//Verifica where
		if(operacionesWhere!=null)
			{
				for(Criterio c:operacionesWhere.getCriterios())
				{
					operaciones=simplificar(c.getNombre());
					if(operaciones.trim().equals("")) continue;
					if(buscarCriteriosZona(operaciones)==null)
						throw new Exception("El criterio no existe en la base");
				}
				where+=" "+CriterioVerdad.PalabrasVerdad.AND+" "+operacionesWhere.getNombre();
			}
		//Verifica having
		if(operacionesHaving!=null)
			{
				for(CriterioAgregacion c:operacionesHaving.getCriterios())
				{
					operaciones=simplificar(c.getNombre());
					if(operaciones.trim().equals("")) continue;
					if(buscarCriteriosZona(operaciones)==null)
						throw new Exception("El criterio no existe en la base");
				}
				having="HAVING "+operacionesHaving.getNombre();
			}
		String sql=select+" "+from+" "+where+" "+groupBy+" "+having+" "+orderBy;
		PreparedStatement prep =conn.prepareStatement(sql);
		recursos.add(prep);
		System.out.println(sql);
		ResultSet r =prep.executeQuery();
		
		List<ContenedoraInformacion> cont=crearContenedora(r,select);
		return cont;
	}

	private void evaluarHavingZona(String nombre) throws SQLException, Exception {
		String temp=nombre.replaceAll("\\(","").replaceAll("\\)","").trim();
		for(CriterioVerdad.PalabrasVerdad p:CriterioVerdad.PalabrasVerdad.values())
		{
			temp=temp.replaceAll(p+"","");
		}
		for(Criterio.Agregaciones a: Criterio.Agregaciones.values())
		{
			temp=temp.replaceAll(a+"", "");
		}
		String[] criterios=temp.split(" ");
		for(String s:criterios)
		{
			if(s.trim().equals("")) continue;
			if(buscarCriteriosZona(s.trim())==null) throw new Exception("Hay un criterio del having que no existe "+s);
		}
	}

	private ArrayList<String> evaluarVerdad(String nombre) throws SQLException, Exception{
		String temp=nombre.replaceAll("\\(","").replaceAll("\\)","").trim();
		for(CriterioVerdad.PalabrasVerdad p:CriterioVerdad.PalabrasVerdad.values())
		{
			temp=temp.replaceAll(p+"","");
		}
		String[] criterios=temp.split(" ");
		ArrayList<String> lista=new ArrayList<>();
		for(String s:criterios)
		{
			if(s.trim().equals("")) continue;
			lista.add(s);
		}
		return lista;
	}

	private String simplificarOrden(String nombre) {
		return nombre.replaceAll("DESC", "").replaceAll("ASC", "").trim();
	}

	private String simplificarAgrupacion(String nombre) {
		String temp=nombre.replaceAll("\\(","").replaceAll("\\)","").trim();
		for(Criterio.Agregaciones a: Criterio.Agregaciones.values())
		{
			if(nombre.contains(a+""))
			{
				temp=temp.replaceAll(a+"", "");
				break;
			}
		}
		for(Criterio.PalabrasEspeciales a: Criterio.PalabrasEspeciales.values())
		{
			if(nombre.contains(a+""))
			{
				temp=temp.replaceAll(a+"", "");
				break;
			}
		}
		temp=temp.replaceAll("\\*","");
		return temp.trim();
	}
	
	private String simplificar(String nombre)
	{
		return simplificarOrden(simplificarAgrupacion(nombre));
	}

	private List<ContenedoraInformacion> crearContenedora(ResultSet r, String select) throws SQLException, Exception {
		select=select.replace("SELECT ", "");
		String[] datos=select.split(",");
		ContenedoraInformacion c=null;
		List<ContenedoraInformacion> lista= new ArrayList<>();
		if(r.next())
		{
			c=new ContenedoraInformacion(datos);
			for(int i=0;i<datos.length;i++)
			{
				c.modificarInformacion(i, r.getString(datos[i].trim()));
			}
			lista.add(c);
		}
		return lista;
	}
	
	
	

}
