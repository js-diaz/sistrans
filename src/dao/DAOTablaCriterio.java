
package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import rfc.ContenedoraInformacion;
import rfc.Criterio;
import rfc.Criterio.Agregaciones;
import rfc.CriterioAgregacion;
import rfc.CriterioOrden;
import rfc.CriterioVerdad;
import rfc.CriterioVerdadHaving;
import rfc.LimiteFechas;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicación utilizando todos los criterios para requerimientos de búsqueda.
 * @author s.guzmanm
 */
public class DAOTablaCriterio {
	/**
	 * Enum de tipos de dato.
	 */
	public enum tiposDatos{
		VARCHAR2,DATE, NUMBER
	}
	
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
				+ "LIKE 'RESTAURANTE' OR  TABLE_NAME LIKE 'PEDIDO_PROD' OR TABLE_NAME LIKE 'CUENTA'))";
		String sql = "SELECT DISTINCT COLUMN_NAME FROM "+table;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<Criterio> c=new ArrayList<>();
		while (rs.next()) {
			String name2 = rs.getString("COLUMN_NAME");
			if(name2.equals("NOMBRE_ZONA") || name2.equals("ID_PRODUCTO")
					|| name2.equals("NOMBRE")
					|| name2.equals("NUMEROCUENTA") || name2.equals("VALOR") ||name2.equals("ENTREGADO")) continue;
			c.add(new Criterio(name2));
		}
		c.add(new Criterio("Z.NOMBRE"));
		c.add(new Criterio("P.ID_PRODUCTO"));
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
				+ "LIKE 'PEDIDO_PROD'  OR TABLE_NAME LIKE 'CUENTA' OR TABLE_NAME LIKE 'RESTAURANTE'))";
		String sql = "SELECT DISTINCT COLUMN_NAME FROM "+table+" WHERE COLUMN_NAME LIKE'" + name + "'";
		if(name.equals("Z.NOMBRE") || name.equals("P.ID_PRODUCTO") )
			return new Criterio(name);
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
				+ "LIKE 'RESTAURANTE' OR TABLE_NAME LIKE 'PEDIDO_PROD' OR TABLE_NAME LIKE 'CUENTA' ))";
		String sql = "SELECT COLUMN_NAME, DATA_TYPE FROM "+table;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			lista.agregarInformacion(rs.getString("COLUMN_NAME"), rs.getString("DATA_TYPE"));
		}
		lista.agregarInformacion("Z.NOMBRE","VARCHAR2");
		lista.agregarInformacion("P.ID_PRODUCTO","NUMBER");
		return lista;
	}
	
	/**
	 * Da todos los criterios básicos de una zona (Sus atributos).<br>
	 * @return Criterios básicos de la zona en forma de lista.<br>
	 * @throws SQLException SI hay un error en la BD.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	public List<Criterio> darCriteriosBasicosZonaMenu() throws SQLException, Exception
	{
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'ZONA' OR TABLE_NAME "
				+ "LIKE 'RESTAURANTE'  OR TABLE_NAME LIKE 'CUENTA' OR TABLE_NAME LIKE 'PEDIDO_MENU'))";
		String sql = "SELECT DISTINCT COLUMN_NAME FROM "+table;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<Criterio> c=new ArrayList<>();
		while (rs.next()) {
			String name2 = rs.getString("COLUMN_NAME");
			if(name2.equals("NOMBRE_ZONA") || name2.equals("NOMBRE_MENU")
					|| name2.equals("NOMBRE")
					|| name2.equals("NUMEROCUENTA") || name2.equals("VALOR") ||name2.equals("ENTREGADO")) continue;
			c.add(new Criterio(name2));
		}
		c.add(new Criterio("Z.NOMBRE"));
		c.add(new Criterio("P.NOMBRE_MENU"));
		return c;
	}
	/**
	 * Metodo que busca el criterios con el nombre que entra como parámetro.
	 * @param name - Nombre a buscar
	 * @return ArrayList con los criterios encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Criterio buscarCriteriosZonaMenu(String name) throws SQLException, Exception {
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'ZONA' OR "
				+ " TABLE_NAME LIKE 'PEDIDO_MENU' OR TABLE_NAME LIKE 'CUENTA' OR TABLE_NAME LIKE 'RESTAURANTE'))";
		String sql = "SELECT DISTINCT COLUMN_NAME FROM "+table+" WHERE COLUMN_NAME LIKE'" + name + "'";
		if(name.equals("Z.NOMBRE") || name.equals("P.NOMBRE_MENU"))
			return new Criterio(name);
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
	public ContenedoraInformacion tiposDeDatoZonaMenu() throws SQLException, Exception
	{
		ContenedoraInformacion lista= new ContenedoraInformacion();
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'ZONA' OR TABLE_NAME "
				+ "LIKE 'RESTAURANTE'  OR TABLE_NAME LIKE 'PEDIDO_MENU' OR TABLE_NAME LIKE 'CUENTA' ))";
		String sql = "SELECT COLUMN_NAME, DATA_TYPE FROM "+table;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			lista.agregarInformacion(rs.getString("COLUMN_NAME"), rs.getString("DATA_TYPE"));
		}
		lista.agregarInformacion("Z.NOMBRE","VARCHAR2");
		lista.agregarInformacion("P.NOMBRE_MENU","VARCHAR2");
		return lista;
	}
	
	/**
	 * Método para generar lista filtrada de la información total de las zonas.<br>
	 * @param criteriosOrganizacion Listado de criterios de organización.<br>
	 * @param criteriosAgrupamiento Listado de criterios de agrupamiento.<br>
	 * @param agregacionesSeleccion Listado de selección de agregaciones.<br>
	 * @param operacionesWhere Operaciones con where.<br>
	 * @param operacionesHaving Operaciones con having.<br>
	 * @param esProd Determina si es un producto o no.<br>
	 * @return Listado de contenedora de información con los datos dados.<br>
	 * @throws SQLException Si hay un error con la base de datos.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	public List<ContenedoraInformacion> generarListaFiltradaZonas(String nombreZona,
			List<CriterioOrden> criteriosOrganizacion,List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregacionesSeleccion, CriterioVerdad operacionesWhere, 
			CriterioVerdadHaving operacionesHaving, boolean esProd) throws SQLException, Exception 
	{
		if(esProd) return generarListaFiltradaZonasProducto(nombreZona,criteriosOrganizacion,criteriosAgrupamiento,
				agregacionesSeleccion,operacionesWhere,operacionesHaving);
		else return generarListaFiltradaZonasMenu(nombreZona,criteriosOrganizacion,criteriosAgrupamiento,
				agregacionesSeleccion,operacionesWhere,operacionesHaving);
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
	public List<ContenedoraInformacion> generarListaFiltradaZonasProducto(
			String nombreZona,List<CriterioOrden> criteriosOrganizacion,List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregacionesSeleccion, CriterioVerdad operacionesWhere, 
			CriterioVerdadHaving operacionesHaving) throws SQLException, Exception {
		//Verifica que no haya repeticiones de criterios
		
		List<CriterioOrden> existentesOrd=new ArrayList<>();
		List<Criterio> existentesAgrup= new ArrayList<>();
		List<CriterioAgregacion> agreSelec=new ArrayList<>();
		if(criteriosAgrupamiento!=null)
		{
			if(criteriosAgrupamiento.contains(new Criterio("NOMBRE")))
			{
				criteriosAgrupamiento.remove(new Criterio("NOMBRE"));
				criteriosAgrupamiento.add(new Criterio("Z.NOMBRE"));
			}
			else if (criteriosAgrupamiento.contains(new Criterio("ID_PRODUCTO")))
			{
				criteriosAgrupamiento.remove(new Criterio("ID_PRODUCTO"));
				criteriosAgrupamiento.add(new Criterio("P.ID_PRODUCTO"));
			}
			for(Criterio c: criteriosAgrupamiento)
			{
				
				if(existentesAgrup.indexOf(c)>=0) continue;
				existentesAgrup.add(c);
			}
		}
			
			
			if(criteriosOrganizacion!=null)
			{
				if(criteriosOrganizacion.contains(new Criterio("NOMBRE")))
				{
					criteriosOrganizacion.remove(new Criterio("NOMBRE"));
					criteriosOrganizacion.add(new CriterioOrden(null, "Z.NOMBRE", false));
				}
				else if (criteriosOrganizacion.contains(new Criterio("ID_PRODUCTO")))
				{
					criteriosOrganizacion.remove(new Criterio("ID_PRODUCTO"));
					criteriosOrganizacion.add(new CriterioOrden(null, "P.ID_PRODUCTO", false));
				}
				for(CriterioOrden c: criteriosOrganizacion)
				{
					
					if(existentesOrd.indexOf(c)>=0) continue;
					if(existentesAgrup!=null && existentesAgrup.size()>0&& existentesAgrup.indexOf(c)<0) throw new Exception("Los criterios no hacen parte del agrupamiento establecido");
					existentesOrd.add(c);
				}
			}
			
			if(agregacionesSeleccion!=null)
			{
				if(agregacionesSeleccion.contains(new Criterio("NOMBRE")))
				{
					agregacionesSeleccion.remove(new Criterio("NOMBRE"));
					agregacionesSeleccion.add(new CriterioAgregacion("Z.NOMBRE",null, false));
				}
				else if (agregacionesSeleccion.contains(new Criterio("ID_PRODUCTO")))
				{
					agregacionesSeleccion.remove(new Criterio("ID_PRODUCTO"));
					agregacionesSeleccion.add(new CriterioAgregacion("P.ID_PRODUCTO",null, false));
				}
				for(CriterioAgregacion a: agregacionesSeleccion)
				{
					
					if(agreSelec.indexOf(a)>=0) continue;
					agreSelec.add(a);
				}
			}
			
		//Empieza la creación de los datos del query
		//El from debería ser con ZONA, RESTAURANTE, INFO_PROD_REST, PEDIDO_PROD, MENU, PEDIDO_MENU, CUENTA. Se busca una unión de lo que respecta a producto y menú. En una tabla aparte.
			if(nombreZona!=null)
				nombreZona=" AND Z.NOMBRE LIKE '"+nombreZona+"'";
			else nombreZona="";
			String from=" FROM ZONA Z, RESTAURANTE R, CUENTA, PEDIDO_PROD P  ";
		String select="SELECT ";
		String groupBy="";
		String orderBy="";
		String where=" WHERE R.NOMBRE_ZONA=Z.NOMBRE AND NUMEROCUENTA=P.NUMERO_CUENTA AND R.NOMBRE=P.NOMBRE_RESTAURANTE "+nombreZona+" "; 
		String having="";
		String temp="";
		//Verifica agrupaciones
		if(existentesAgrup.size()>0)
		{
			temp=simplificarAgrupacion(existentesAgrup.get(0).getNombre());
			if(!temp.equals("") && buscarCriteriosZona(temp)==null) 
				throw new Exception("Uno de los criterios no existe "+temp+".");
			select+=existentesAgrup.get(0).getNombre();
			groupBy+="GROUP BY "+existentesAgrup.get(0).getNombre();
			existentesAgrup.remove(0);
			for(Criterio c: existentesAgrup)
			{
				temp=simplificarAgrupacion(c.getNombre());
				if(!temp.equals("") && buscarCriteriosZona(temp)==null) 
					throw new Exception("Uno de los criterios no existe "+temp+".");
				groupBy+=", "+c.getNombre();
				select+=", "+c.getNombre();
			}
			for(CriterioAgregacion a:agreSelec) 
			{
				temp=simplificarAgrupacion(a.getNombre());
				if(!temp.equals("") && buscarCriteriosZona(temp)==null) 
					throw new Exception("Uno de los criterios no existe");
				select+=","+a.getNombre();
			}
		}
		else
		{
			List<Criterio> criterios=darCriteriosBasicosZona();
			select+=criterios.get(0).getNombre();
			criterios.remove(0);
			for(Criterio c: criterios)
			{
				select+=","+c.getNombre();
			}
		}
		//Verifica órdenes
		if(existentesOrd.size()>0)
		{
			temp=simplificarOrden(existentesOrd.get(0).getNombre());
			if(!temp.equals("") && buscarCriteriosZona(temp)==null) 
				throw new Exception("Uno de los criterios no existe");
			orderBy+="ORDER BY "+existentesOrd.get(0).getNombre();
			existentesOrd.remove(0);
			for(CriterioOrden c: existentesOrd)
			{
				temp=simplificarOrden(c.getNombre());
				if(!temp.equals("") && buscarCriteriosZona(simplificarOrden(c.getNombre()))==null) 
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
				evaluarWhere(operacionesWhere,tiposDeDatoZona());
				where+=" AND "+operacionesWhere.getNombre();
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
				evaluarHaving(operacionesHaving,tiposDeDatoZona());
				having="HAVING "+operacionesHaving.getNombre();
			}
		String sql=select+" "+from+" "+where+" "+groupBy+" "+having+" "+orderBy;
		PreparedStatement prep =conn.prepareStatement(sql);
		recursos.add(prep);
		System.out.println(sql);
		double time =System.currentTimeMillis();
		ResultSet r =prep.executeQuery();
		time=System.currentTimeMillis()-time;
		System.out.println(time);
		List<ContenedoraInformacion> cont=crearContenedora(r,select);
		return cont;
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
	public List<ContenedoraInformacion> generarListaFiltradaZonasMenu(String nombreZona,
			List<CriterioOrden> criteriosOrganizacion,List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregacionesSeleccion, CriterioVerdad operacionesWhere, 
			CriterioVerdadHaving operacionesHaving) throws SQLException, Exception {
		//Verifica que no haya repeticiones de criterios
		List<CriterioOrden> existentesOrd=new ArrayList<>();
		List<Criterio> existentesAgrup= new ArrayList<>();
		List<Criterio> existentesSelec= new ArrayList<>();
		List<CriterioAgregacion> agreSelec=new ArrayList<>();
		if(criteriosAgrupamiento!=null)
		{
			if(criteriosAgrupamiento.contains(new Criterio("NOMBRE")))
			{
				criteriosAgrupamiento.remove(new Criterio("NOMBRE"));
				criteriosAgrupamiento.add(new Criterio("Z.NOMBRE"));
			}
			else if (criteriosAgrupamiento.contains(new Criterio("NOMBRE_MENU")))
			{
				criteriosAgrupamiento.remove(new Criterio("NOMBRE_MENU"));
				criteriosAgrupamiento.add(new Criterio("P.NOMBRE_MENU"));
			}
			for(Criterio c: criteriosAgrupamiento)
			{
				
				if(existentesAgrup.indexOf(c)>=0) continue;
				existentesAgrup.add(c);
			}
		}
			
			if(criteriosOrganizacion!=null)
			{
				if(criteriosOrganizacion.contains(new Criterio("NOMBRE")))
				{
					criteriosOrganizacion.remove(new Criterio("NOMBRE"));
					criteriosOrganizacion.add(new CriterioOrden(null, "Z.NOMBRE", false));
				}
				else if (criteriosOrganizacion.contains(new Criterio("NOMBRE_MENU")))
				{
					criteriosOrganizacion.remove(new Criterio("NOMBRE_MENU"));
					criteriosOrganizacion.add(new CriterioOrden(null,"P.NOMBRE_MENU", false));
				}
				for(CriterioOrden c: criteriosOrganizacion)
				{
					
					if(existentesOrd.indexOf(c)>=0) continue;
					if(existentesAgrup!=null && existentesAgrup.size()>0&& existentesAgrup.indexOf(c)<0) throw new Exception("Los criterios no hacen parte del agrupamiento establecido");
					existentesOrd.add(c);
				}
			}
			
			if(agregacionesSeleccion!=null)
			{
				if(agregacionesSeleccion.contains(new Criterio("NOMBRE")))
				{
					agregacionesSeleccion.remove(new Criterio("NOMBRE"));
					agregacionesSeleccion.add(new CriterioAgregacion("Z.NOMBRE",null, false));
				}
				else if (agregacionesSeleccion.contains(new Criterio("NOMBRE_MENU")))
				{
					agregacionesSeleccion.remove(new Criterio("NOMBRE_MENU"));
					agregacionesSeleccion.add(new CriterioAgregacion("P.NOMBRE_MENU",null, false));
				}
				for(CriterioAgregacion a: agregacionesSeleccion)
				{
					
					if(agreSelec.indexOf(a)>=0) continue;
					agreSelec.add(a);
				}
			}
			
		//Empieza la creación de los datos del query
			if(nombreZona!=null)
				nombreZona=" AND Z.NOMBRE LIKE '"+nombreZona+"'";
			else nombreZona="";
		//El from debería ser con ZONA, RESTAURANTE, INFO_PROD_REST, PEDIDO_PROD, MENU, PEDIDO_MENU, CUENTA. Se busca una unión de lo que respecta a producto y menú. En una tabla aparte.
			String from=" FROM ZONA Z, RESTAURANTE R, CUENTA, PEDIDO_MENU P ";
		String select="SELECT ";
		String groupBy="";
		String orderBy="";
		String where=" WHERE R.NOMBRE_ZONA=Z.NOMBRE AND NUMEROCUENTA=P.NUMERO_CUENTA AND R.NOMBRE=P.NOMBRE_RESTAURANTE "+nombreZona+" "; 
		String having="";
		String temp="";
		//Verifica agrupaciones
		if(existentesAgrup.size()>0)
		{
			temp=simplificarAgrupacion(existentesAgrup.get(0).getNombre());
			if(!temp.equals("") && buscarCriteriosZonaMenu(temp)==null) 
				throw new Exception("Uno de los criterios no existe "+temp+".");
			select+=existentesAgrup.get(0).getNombre();
			groupBy+="GROUP BY "+existentesAgrup.get(0).getNombre();
			existentesAgrup.remove(0);
			for(Criterio c: existentesAgrup)
			{
				temp=simplificarAgrupacion(c.getNombre());
				if(!temp.equals("") && buscarCriteriosZonaMenu(temp)==null) 
					throw new Exception("Uno de los criterios no existe "+temp+".");
				groupBy+=", "+c.getNombre();
				select+=", "+c.getNombre();
			}
			for(CriterioAgregacion a:agreSelec) 
			{
				temp=simplificarAgrupacion(a.getNombre());
				if(!temp.equals("") && buscarCriteriosZonaMenu(temp)==null) 
					throw new Exception("Uno de los criterios no existe");
				select+=","+a.getNombre();
			}
		}
		else
		{
			List<Criterio> criterios=darCriteriosBasicosZonaMenu();
			select+=criterios.get(0).getNombre();
			criterios.remove(0);
			for(Criterio c: criterios)
			{
				select+=","+c.getNombre();
			}
		}
		//Verifica órdenes
		if(existentesOrd.size()>0)
		{
			temp=simplificarOrden(existentesOrd.get(0).getNombre());
			if(!temp.equals("") && buscarCriteriosZonaMenu(temp)==null) 
				throw new Exception("Uno de los criterios no existe");
			orderBy+="ORDER BY "+existentesOrd.get(0).getNombre();
			existentesOrd.remove(0);
			for(CriterioOrden c: existentesOrd)
			{
				temp=simplificarOrden(c.getNombre());
				if(!temp.equals("") && buscarCriteriosZonaMenu(simplificarOrden(c.getNombre()))==null) 
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
					if(buscarCriteriosZonaMenu(operaciones)==null)
						throw new Exception("El criterio no existe en la base");
				}
				evaluarWhere(operacionesWhere,tiposDeDatoZonaMenu());
				where+=" "+CriterioVerdad.PalabrasVerdad.AND+" "+operacionesWhere.getNombre();
			}
		//Verifica having
		if(operacionesHaving!=null)
			{
				for(CriterioAgregacion c:operacionesHaving.getCriterios())
				{
					operaciones=simplificar(c.getNombre());
					if(operaciones.trim().equals("")) continue;
					if(buscarCriteriosZonaMenu(operaciones)==null)
						throw new Exception("El criterio no existe en la base");
				}
				evaluarHaving(operacionesHaving,tiposDeDatoZonaMenu());
				having="HAVING "+operacionesHaving.getNombre();
			}
		String sql=select+" "+from+" "+where+" "+groupBy+" "+having+" "+orderBy;
		PreparedStatement prep =conn.prepareStatement(sql);
		recursos.add(prep);
		System.out.println(sql);
		double time =System.currentTimeMillis();
		ResultSet r =prep.executeQuery();
		time=System.currentTimeMillis()-time;
		System.out.println(time);
		
		List<ContenedoraInformacion> cont=crearContenedora(r,select);
		return cont;
	}
	/**
	 * 
	 * @param where
	 * @param tipos
	 * @throws SQLException
	 * @throws Exception
	 */
	private void evaluarWhere(CriterioVerdad where, ContenedoraInformacion tipos) throws SQLException, Exception{
		String tipo=null;
		String ant1="";
		String ant2="";
		if(where.getValorAnterior()!=null)
		{
			for(int i=0;i<tipos.getInformacion().size() && tipo==null;i++)
			{
				if(where.getValorAnterior().getNombre().equals(tipos.darNombre(i)))
					tipo=tipos.darValor(i);
			}
			if(where.getCriterioComparacion()!=null)
			{
				String tipo2=null;
				for(int i=0;i<tipos.getInformacion().size() && tipo2==null;i++)
				{
					if(where.getValorAnterior().getNombre().equals(tipos.darNombre(i)))
						tipo2=tipos.darValor(i);
				}
				if(!tipo.equals(tipo2) && (tipo.equals(tiposDatos.VARCHAR2+"") ||
						tipo2.equals(tiposDatos.VARCHAR2+"") || tipo.equals(tiposDatos.DATE+"")
						|| tipo2.equals(tiposDatos.DATE+""))) throw new Exception("Está intentando comparar dos criterios diferentes");
			}
			else if (where.getComparacion()!=null)
			{
				System.out.println("EL TIPO DE DATO ES "+tipo+" CONTRA "+tiposDatos.VARCHAR2+" PARA "+where.getValorAnterior().getNombre());
				if(tipo.trim().equals(tiposDatos.VARCHAR2+""))
					{
						System.out.println("A");
						where.setNombre(where.getNombre().replaceAll(where.getComparacion(), "'"+where.getComparacion()+"'"));
						System.out.println(where.getNombre());
					}
				else if (tipo.trim().equals(tiposDatos.DATE+"")) 
					{
						System.out.println("B");
						where.setNombre(where.getNombre().replaceAll(where.getComparacion(), "TO_DATE\\('"+where.getComparacion()+"', 'yyyy-MM-dd hh24:mi:ss'\\)"));
					}
				else
				{
					System.out.println("C");
					if(where.getOperacion()==null) throw new Exception("No se puede usar este operador para objetos diferentes a cadenas de caracteres"); 
				}
				System.out.println("D");
			}
		}
		else
		{
			if(where.getC1()!=null)
			{
				ant1=where.getC1().getNombre();
				ant2=where.getC2().getNombre();
				evaluarWhere(where.getC1(),tipos);
				evaluarWhere(where.getC2(),tipos);
				where.setNombre(where.getNombre().replaceAll(ant1, where.getC1().getNombre()));
				where.setNombre(where.getNombre().replaceAll(ant2, where.getC2().getNombre()));
				System.out.println("TOTAL "+where.getNombre());
			}
			
			
		}
	}
	
	private void evaluarHaving(CriterioVerdadHaving having, ContenedoraInformacion tipos) throws SQLException, Exception{
		String tipo=null;
		if(having.getValorAnterior()!=null)
		{
			if(having.getValorAnterior().getInterno()==null) 
				{
					tipo=tiposDatos.NUMBER+"";
				}
			for(int i=0;i<tipos.getInformacion().size() && tipo==null;i++)
			{
				if(having.getValorAnterior().getInterno().equals(tipos.darNombre(i)))
					tipo=tipos.darValor(i);
			}
			if(having.getCriterioComparacion()!=null)
			{
				String tipo2=null;
				if(having.getCriterioComparacion().getInterno()==null) tipo2=tiposDatos.NUMBER+"";
				for(int i=0;i<tipos.getInformacion().size() && tipo2==null;i++)
				{
					if(having.getValorAnterior().getInterno().equals(tipos.darNombre(i)))
						tipo2=tipos.darValor(i);
				}
				if(!tipo.equals(tipo2) && (tipo.equals(tiposDatos.VARCHAR2+"") ||
						tipo2.equals(tiposDatos.VARCHAR2+"") || tipo.equals(tiposDatos.DATE+"")
						|| tipo2.equals(tiposDatos.DATE+""))) throw new Exception("Está intentando comparar dos criterios diferentes");
			}
			else if (having.getComparacion()!=null)
			{
				if(tipo.equals(tiposDatos.VARCHAR2+"") && (having.getValorAnterior().getAgregacion().equals(Agregaciones.MIN))
						|| having.getValorAnterior().getAgregacion().equals(Agregaciones.MAX))
					{
						having.setNombre(having.getNombre().replaceAll(having.getComparacion(), "'"+having.getComparacion()+"'"));
					}
				else if (tipo.equals(tiposDatos.DATE+"") && (having.getValorAnterior().getAgregacion().equals(Agregaciones.MIN))
						|| having.getValorAnterior().getAgregacion().equals(Agregaciones.MAX)) 
					{
						having.setNombre(having.getNombre().replaceAll(having.getComparacion(), "TO_DATE\\('"+having.getComparacion()+"', 'yyyy/mm/dd'\\)"));
					}
				else
				{
					if(having.getOperacion()==null) throw new Exception("No se puede usar este operador para objetos diferentes a cadenas de caracteres"); 
				}
			}
		}
		else
		{
			evaluarHaving(having.getC1(),tipos);
			evaluarHaving(having.getC2(),tipos);
		}
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
			}
		}
		for(Criterio.PalabrasEspeciales a: Criterio.PalabrasEspeciales.values())
		{
			if(nombre.contains(a+""))
			{
				temp=temp.replaceAll(a+"", "");
			}
		}
		temp=temp.replaceAll("\\*","");
		temp=temp.replaceAll(Criterio.PalabrasEspeciales.DISTINCT+"", "");
		return temp.trim();
	}
	
	private String simplificar(String nombre)
	{
		return simplificarOrden(simplificarAgrupacion(nombre));
	}

	private List<ContenedoraInformacion> crearContenedora(ResultSet r, String select) throws SQLException, Exception {
		select=select.replace("SELECT ", "");		
		if(select.trim().length()==0) return new ArrayList<>();
		String[] datos=select.split(",");
		ContenedoraInformacion c=null;
		List<ContenedoraInformacion> lista= new ArrayList<>();
		String temp="";
		while(r.next())
		{
			c=new ContenedoraInformacion(datos);
			for(int i=0;i<datos.length;i++)
			{
				temp=datos[i];
				if(temp.contains("AS")) temp = temp.split("AS")[1];
				if(temp.contains(".")) temp=temp.split("\\.")[1];
				c.modificarInformacion(i, r.getString(temp.trim()));
			}
			lista.add(c);
		}
		return lista;
	}
	
	//RFC1
	
	/**
	 * Da todos los criterios básicos de una producto (Sus atributos).<br>
	 * @return Criterios básicos de la producto en forma de lista.<br>
	 * @throws SQLException SI hay un error en la BD.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	public List<Criterio> darCriteriosBasicosProducto() throws SQLException, Exception
	{
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'PRODUCTO' OR TABLE_NAME "
				+ "LIKE 'INGREDIENTE' OR  TABLE_NAME LIKE 'RESTAURANTE'  OR TABLE_NAME LIKE 'INFO_PROD_REST'))";
		String sql = "SELECT DISTINCT COLUMN_NAME FROM "+table;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<Criterio> c=new ArrayList<>();
		while (rs.next()) {
			String name2 = rs.getString("COLUMN_NAME");
			if(name2.equals("DISPONIBILIDAD") || name2.equals("NOMBRE_RESTAURANTE")|| name2.equals("ID") || name2.equals("NOMBRE") || name2.equals("ID_PRODUCTO")) continue;
			c.add(new Criterio(name2));
		}
		c.add(new Criterio("P.NOMBRE"));
		c.add(new Criterio("I.ID_PRODUCTO"));
		c.add(new Criterio("I.NOMBRE_RESTAURANTE"));
		c.add(new Criterio("I.DISPONIBILIDAD"));

		return c;
	}
	/**
	 * Metodo que busca el criterios con el nombre que entra como parámetro.
	 * @param name - Nombre a buscar
	 * @return ArrayList con los criterios encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Criterio buscarCriteriosProducto(String name) throws SQLException, Exception {
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'PRODUCTO' OR TABLE_NAME "
				+ "LIKE 'INGREDIENTE' OR  TABLE_NAME LIKE 'RESTAURANTE'  OR TABLE_NAME LIKE 'INFO_PROD_REST'))";
		String sql = "SELECT DISTINCT COLUMN_NAME FROM "+table+" WHERE COLUMN_NAME LIKE'" + name + "'";
		if(name.equals("I.NOMBRE_RESTAURANTE") || name.equals("P.NOMBRE")||name.equals("I.ID_PRODUCTO")) return new Criterio(name);
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
	 * Retorna el tipo de dato de cada una de las columnas de producto usados en el sistema.<br>
	 * @return Contenedora de información con los tipos de dato.<br>
	 * @throws SQLException Excepción de la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public ContenedoraInformacion tiposDeDatoProducto() throws SQLException, Exception
	{
		ContenedoraInformacion lista= new ContenedoraInformacion();
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'PRODUCTO' OR TABLE_NAME "
				+ "LIKE 'INGREDIENTE' OR  TABLE_NAME LIKE 'RESTAURANTE'  OR TABLE_NAME LIKE 'INFO_PROD_REST'))";
		String sql = "SELECT COLUMN_NAME, DATA_TYPE FROM "+table;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			String name=rs.getString("COLUMN_NAME");
			lista.agregarInformacion(name, rs.getString("DATA_TYPE"));
		}
		lista.agregarInformacion("I.ID_PRODUCTO", "NUMBER");
		lista.agregarInformacion("P.NOMBRE", "VARCHAR2");
		lista.agregarInformacion("I.NOMBRE_RESTAURANTE", "VARCHAR2");
		lista.agregarInformacion("I.DISPONIBILIDAD", "NUMBER");
		return lista;
	}
	
	/**
	 * Método para generar lista filtrada de la información total de las productos.<br>
	 * @param criteriosOrganizacion Listado de criterios de organización.<br>
	 * @param criteriosAgrupamiento Listado de criterios de agrupamiento.<br>
	 * @param agregacionesSeleccion Listado de selección de agregaciones.<br>
	 * @param operacionesWhere Operaciones con where.<br>
	 * @param operacionesHaving Operaciones con having.<br>
	 * @return Listado de contenedora de información con los datos dados.<br>
	 * @throws SQLException Si hay un error con la base de datos.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	public List<ContenedoraInformacion> generarListaFiltradaProductos(
			List<CriterioOrden> criteriosOrganizacion,List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregacionesSeleccion, CriterioVerdad operacionesWhere, 
			CriterioVerdadHaving operacionesHaving) throws SQLException, Exception {
		//Verifica que no haya repeticiones de criterios
		List<CriterioOrden> existentesOrd=new ArrayList<>();
		List<Criterio> existentesAgrup= new ArrayList<>();
		List<CriterioAgregacion> agreSelec=new ArrayList<>();
		if(criteriosAgrupamiento!=null)
		{
			if(criteriosAgrupamiento.contains(new Criterio("NOMBRE")))
			{
				criteriosAgrupamiento.remove(new Criterio("NOMBRE"));
				criteriosAgrupamiento.add(new Criterio("P.NOMBRE"));
			}
			else if (criteriosAgrupamiento.contains(new Criterio("ID_PRODUCTO")))
			{
				criteriosAgrupamiento.remove(new Criterio("ID_PRODUCTO"));
				criteriosAgrupamiento.add(new Criterio("I.ID_PRODUCTO"));
			}
			else if (criteriosAgrupamiento.contains(new Criterio("NOMBRE_RESTAURANTE")))
			{
				criteriosAgrupamiento.remove(new Criterio("NOMBRE_RESTAURANTE"));
				criteriosAgrupamiento.add(new Criterio("I.NOMBRE_RESTAURANTE"));
			}
			else if (criteriosAgrupamiento.contains(new Criterio("DISPONIBILIDAD")))
			{
				criteriosAgrupamiento.remove(new Criterio("DISPONIBILIDAD"));
				criteriosAgrupamiento.add(new Criterio("I.DISPONIBILIDAD"));
			}
			for(Criterio c: criteriosAgrupamiento)
			{
				
				if(existentesAgrup.indexOf(c)>=0) continue;
				existentesAgrup.add(c);
			}
		}
		
		if(criteriosOrganizacion!=null)
		{
			if(criteriosOrganizacion.contains(new Criterio("NOMBRE")))
			{
				criteriosOrganizacion.remove(new Criterio("NOMBRE"));
				criteriosOrganizacion.add(new CriterioOrden(null, "P.NOMBRE", false));
			}
			else if (criteriosOrganizacion.contains(new Criterio("ID_PRODUCTO")))
			{
				criteriosOrganizacion.remove(new Criterio("ID_PRODUCTO"));
				criteriosOrganizacion.add(new CriterioOrden(null, "I.ID_PRODUCTO", false));
			}
			else if (criteriosOrganizacion.contains(new Criterio("NOMBRE_RESTAURANTE")))
			{
				criteriosOrganizacion.remove(new Criterio("NOMBRE_RESTAURANTE"));
				criteriosOrganizacion.add(new CriterioOrden(null, "I.NOMBRE_RESTAURANTE", false));
			}
			else if (criteriosOrganizacion.contains(new Criterio("DISPONIBILIDAD")))
			{
				criteriosOrganizacion.remove(new Criterio("DISPONIBILIDAD"));
				criteriosOrganizacion.add(new CriterioOrden(null, "I.DISPONIBILIDAD", false));
			}
			for(CriterioOrden c: criteriosOrganizacion)
			{
				
				if(existentesOrd.indexOf(c)>=0) continue;
				if(existentesAgrup!=null && existentesAgrup.size()>0&& existentesAgrup.indexOf(c)<0) throw new Exception("Los criterios no hacen parte del agrupamiento establecido");
				existentesOrd.add(c);
			}
		}
		
		if(agregacionesSeleccion!=null)
		{
			if(agregacionesSeleccion.contains(new Criterio("NOMBRE")))
			{
				agregacionesSeleccion.remove(new Criterio("NOMBRE"));
				agregacionesSeleccion.add(new CriterioAgregacion("P.NOMBRE",null, false));
			}
			else if (agregacionesSeleccion.contains(new Criterio("ID_PRODUCTO")))
			{
				agregacionesSeleccion.remove(new Criterio("ID_PRODUCTO"));
				agregacionesSeleccion.add(new CriterioAgregacion("I.ID_PRODUCTO",null, false));
			}
			else if (agregacionesSeleccion.contains(new Criterio("NOMBRE_RESTAURANTE")))
			{
				agregacionesSeleccion.remove(new Criterio("NOMBRE_RESTAURANTE"));
				agregacionesSeleccion.add(new CriterioAgregacion("I.NOMBRE_RESTAURANTE",null, false));
			}
			else if (agregacionesSeleccion.contains(new Criterio("DISPONIBILIDAD")))
			{
				agregacionesSeleccion.remove(new Criterio("DISPONIBILIDAD"));
				agregacionesSeleccion.add(new CriterioAgregacion("I.DISPONIBILIDAD",null, false));
			}
			for(CriterioAgregacion a: agregacionesSeleccion)
			{
				
				if(agreSelec.indexOf(a)>=0) continue;
				agreSelec.add(a);
			}
		}
		
		//Empieza la creación de los datos del query
		//El from debería ser con ZONA, RESTAURANTE, INFO_PROD_REST, PEDIDO_PROD, MENU, PEDIDO_MENU, CUENTA. Se busca una unión de lo que respecta a producto y menú. En una tabla aparte.
		String from ="FROM PRODUCTO P, RESTAURANTE,  INFO_PROD_REST I   ";
		String select="SELECT ";
		String groupBy="";
		String orderBy="";
		String where="WHERE  "
				+ "P.ID = I.ID_PRODUCTO AND "
				+ " RESTAURANTE.NOMBRE = I.NOMBRE_RESTAURANTE"; 
		String having="";
		String temp="";
		//Verifica agrupaciones
		if(existentesAgrup.size()>0)
		{
			temp=simplificarAgrupacion(existentesAgrup.get(0).getNombre());
			if(!temp.equals("") && buscarCriteriosProducto(temp)==null) 
				throw new Exception("Uno de los criterios no existe "+temp+".");
			select+=existentesAgrup.get(0).getNombre();
			groupBy+="GROUP BY "+existentesAgrup.get(0).getNombre();
			existentesAgrup.remove(0);
			for(Criterio c: existentesAgrup)
			{
				temp=simplificarAgrupacion(c.getNombre());
				if(!temp.equals("") && buscarCriteriosProducto(temp)==null) 
					throw new Exception("Uno de los criterios no existe "+temp+".");
				groupBy+=", "+c.getNombre();
				select+=", "+c.getNombre();
			}
			for(CriterioAgregacion a:agreSelec) 
			{
				temp=simplificarAgrupacion(a.getNombre());
				if(!temp.equals("") && buscarCriteriosProducto(temp)==null) 
					throw new Exception("Uno de los criterios no existe");
				select+=","+a.getNombre();
			}
		}
		else
		{
			List<Criterio> criterios=darCriteriosBasicosProducto();
			select+=criterios.get(0).getNombre();
			criterios.remove(0);
			for(Criterio c: criterios)
			{
				select+=","+c.getNombre();
			}
		}
		//Verifica órdenes
		if(existentesOrd.size()>0)
		{
			temp=simplificarOrden(existentesOrd.get(0).getNombre());
			if(!temp.equals("") && buscarCriteriosProducto(temp)==null) 
				throw new Exception("Uno de los criterios no existe");
			orderBy+="ORDER BY "+existentesOrd.get(0).getNombre();
			existentesOrd.remove(0);
			for(CriterioOrden c: existentesOrd)
			{
				temp=simplificarOrden(c.getNombre());
				if(!temp.equals("") && buscarCriteriosProducto(simplificarOrden(c.getNombre()))==null) 
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
					if(buscarCriteriosProducto(operaciones)==null)
						throw new Exception("El criterio no existe en la base");
				}
				evaluarWhere(operacionesWhere,tiposDeDatoProducto());
				where+=" AND "+operacionesWhere.getNombre();
			}
		//Verifica having
		if(operacionesHaving!=null)
			{
				for(CriterioAgregacion c:operacionesHaving.getCriterios())
				{
					operaciones=simplificar(c.getNombre());
					if(operaciones.trim().equals("")) continue;
					if(buscarCriteriosProducto(operaciones)==null)
						throw new Exception("El criterio no existe en la base");
				}
				evaluarHaving(operacionesHaving,tiposDeDatoProducto());
				having="HAVING "+operacionesHaving.getNombre();
			}
		String sql=select+" "+from+" "+where+" "+groupBy+" "+having+" "+orderBy;
		PreparedStatement prep =conn.prepareStatement(sql);
		recursos.add(prep);
		System.out.println(sql);
		double time =System.currentTimeMillis();
		ResultSet r =prep.executeQuery();
		time=System.currentTimeMillis()-time;
		System.out.println(time);
		List<ContenedoraInformacion> cont=crearContenedora(r,select);
		return cont;
	}
	
	/**
	 * Método para generar lista filtrada de la información total de las productos.<br>
	 * @param criteriosOrganizacion Listado de criterios de organización.<br>
	 * @param criteriosAgrupamiento Listado de criterios de agrupamiento.<br>
	 * @param agregacionesSeleccion Listado de selección de agregaciones.<br>
	 * @param operacionesWhere Operaciones con where.<br>
	 * @param operacionesHaving Operaciones con having.<br>
	 * @return Listado de contenedora de información con los datos dados.<br>
	 * @throws SQLException Si hay un error con la base de datos.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	public List<ContenedoraInformacion> generarListaFiltradaProductoEspecifica(String nombreProducto,
			List<CriterioOrden> criteriosOrganizacion,List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregacionesSeleccion, CriterioVerdad operacionesWhere, 
			CriterioVerdadHaving operacionesHaving) throws SQLException, Exception {
		//Verifica que no haya repeticiones de criterios
		List<CriterioOrden> existentesOrd=new ArrayList<>();
		List<Criterio> existentesAgrup= new ArrayList<>();
		List<Criterio> existentesSelec= new ArrayList<>();
		List<CriterioAgregacion> agreSelec=new ArrayList<>();
		if(criteriosAgrupamiento!=null)
		{
			if(criteriosAgrupamiento.contains(new Criterio("NOMBRE")))
			{
				criteriosAgrupamiento.remove(new Criterio("NOMBRE"));
				criteriosAgrupamiento.add(new Criterio("P.NOMBRE"));
			}
			else if (criteriosAgrupamiento.contains(new Criterio("ID_PRODUCTO")))
			{
				criteriosAgrupamiento.remove(new Criterio("ID_PRODUCTO"));
				criteriosAgrupamiento.add(new Criterio("I.ID_PRODUCTO"));
			}
			for(Criterio c: criteriosAgrupamiento)
			{
				
				if(existentesAgrup.indexOf(c)>=0) continue;
				existentesAgrup.add(c);
			}
		}
		
		if(criteriosOrganizacion!=null)
		{
			if(criteriosOrganizacion.contains(new Criterio("NOMBRE")))
			{
				criteriosOrganizacion.remove(new Criterio("NOMBRE"));
				criteriosOrganizacion.add(new CriterioOrden(null, "P.NOMBRE", false));
			}
			else if (criteriosOrganizacion.contains(new Criterio("ID_PRODUCTO")))
			{
				criteriosOrganizacion.remove(new Criterio("ID_PRODUCTO"));
				criteriosOrganizacion.add(new CriterioOrden(null, "I.ID_PRODUCTO", false));
			}
			for(CriterioOrden c: criteriosOrganizacion)
			{
				
				if(existentesOrd.indexOf(c)>=0) continue;
				if(existentesAgrup!=null && existentesAgrup.size()>0&& existentesAgrup.indexOf(c)<0) throw new Exception("Los criterios no hacen parte del agrupamiento establecido");
				existentesOrd.add(c);
			}
		}
		
		if(agregacionesSeleccion!=null)
		{
			if(agregacionesSeleccion.contains(new Criterio("NOMBRE")))
			{
				agregacionesSeleccion.remove(new Criterio("NOMBRE"));
				agregacionesSeleccion.add(new CriterioAgregacion("P.NOMBRE",null, false));
			}
			else if (agregacionesSeleccion.contains(new Criterio("ID_PRODUCTO")))
			{
				agregacionesSeleccion.remove(new Criterio("ID_PRODUCTO"));
				agregacionesSeleccion.add(new CriterioAgregacion("I.ID_PRODUCTO",null, false));
			}
			for(CriterioAgregacion a: agregacionesSeleccion)
			{
				
				if(agreSelec.indexOf(a)>=0) continue;
				agreSelec.add(a);
			}
		}
		//Empieza la creación de los datos del query
		//El from debería ser con ZONA, RESTAURANTE, INFO_PROD_REST, PEDIDO_PROD, MENU, PEDIDO_MENU, CUENTA. Se busca una unión de lo que respecta a producto y menú. En una tabla aparte.
		String from ="FROM PRODUCTO P, RESTAURANTE,  INFO_PROD_REST I, CATEGORIA_PRODUCTO   ";
		String select="SELECT ";
		String groupBy="";
		String orderBy="";
		String where="WHERE P.ID = CATEGORIA_PRODUCTO.ID_PRODUCTO AND "
				+ "P.ID = I.ID_PRODUCTO AND P.NOMBRE LIKE '"+nombreProducto+"'"
				+ " RESTAURANTE.NOMBRE = I.NOMBRE_RESTAURANTE"; 
		String having="";
		String temp="";	
		//Verifica agrupaciones
		if(existentesAgrup.size()>0)
		{
			temp=simplificarAgrupacion(existentesAgrup.get(0).getNombre());
			if(!temp.equals("") && buscarCriteriosProducto(temp)==null) 
				throw new Exception("Uno de los criterios no existe "+temp+".");
			select+=existentesAgrup.get(0).getNombre();
			groupBy+="GROUP BY "+existentesAgrup.get(0).getNombre();
			existentesAgrup.remove(0);
			for(Criterio c: existentesAgrup)
			{
				temp=simplificarAgrupacion(c.getNombre());
				if(!temp.equals("") && buscarCriteriosProducto(temp)==null) 
					throw new Exception("Uno de los criterios no existe "+temp+".");
				groupBy+=", "+c.getNombre();
				select+=", "+c.getNombre();
			}
			for(CriterioAgregacion a:agreSelec) 
			{
				temp=simplificarAgrupacion(a.getNombre());
				if(!temp.equals("") && buscarCriteriosProducto(temp)==null) 
					throw new Exception("Uno de los criterios no existe");
				select+=","+a.getNombre();
			}
		}
		else
		{
			List<Criterio> criterios=darCriteriosBasicosProducto();
			select+=criterios.get(0).getNombre();
			criterios.remove(0);
			for(Criterio c: criterios)
			{
				select+=","+c.getNombre();
			}
		}
		//Verifica órdenes
		if(existentesOrd.size()>0)
		{
			temp=simplificarOrden(existentesOrd.get(0).getNombre());
			if(!temp.equals("") && buscarCriteriosProducto(temp)==null) 
				throw new Exception("Uno de los criterios no existe");
			orderBy+="ORDER BY "+existentesOrd.get(0).getNombre();
			existentesOrd.remove(0);
			for(CriterioOrden c: existentesOrd)
			{
				temp=simplificarOrden(c.getNombre());
				if(!temp.equals("") && buscarCriteriosProducto(simplificarOrden(c.getNombre()))==null) 
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
					if(buscarCriteriosProducto(operaciones)==null)
						throw new Exception("El criterio no existe en la base");
				}
				evaluarWhere(operacionesWhere,tiposDeDatoProducto());
				where+=" "+CriterioVerdad.PalabrasVerdad.AND+" "+operacionesWhere.getNombre();
			}
		//Verifica having
		if(operacionesHaving!=null)
			{
				for(CriterioAgregacion c:operacionesHaving.getCriterios())
				{
					operaciones=simplificar(c.getNombre());
					if(operaciones.trim().equals("")) continue;
					if(buscarCriteriosProducto(operaciones)==null)
						throw new Exception("El criterio no existe en la base");
				}
				evaluarHaving(operacionesHaving,tiposDeDatoProducto());
				having="HAVING "+operacionesHaving.getNombre();
			}
		String sql=select+" "+from+" "+where+" "+groupBy+" "+having+" "+orderBy;
		PreparedStatement prep =conn.prepareStatement(sql);
		recursos.add(prep);
		System.out.println(sql);
		double time =System.currentTimeMillis();
		ResultSet r =prep.executeQuery();
		time=System.currentTimeMillis()-time;
		System.out.println(time);
		
		List<ContenedoraInformacion> cont=crearContenedora(r,select);
		return cont;
	}
	
	/**
	 * Da todos los criterios básicos de una producto (Sus atributos).<br>
	 * @return Criterios básicos de la producto en forma de lista.<br>
	 * @throws SQLException SI hay un error en la BD.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	public List<Criterio> darCriteriosBasicosUsuarioDeProducto() throws SQLException, Exception
	{
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'PRODUCTO' OR TABLE_NAME "
				+ "LIKE 'PEDIDO_PROD' OR  TABLE_NAME LIKE 'INFO_PROD_REST' OR TABLE_NAME LIKE 'CUENTA')) ";
		String sql = "SELECT DISTINCT COLUMN_NAME FROM "+table;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		List<Criterio> c=new ArrayList<>();
		while (rs.next()) {
			String name2 = rs.getString("COLUMN_NAME");
			if(name2.equals("NOMBRE")||name2.equals("NUMERO_CUENTA")||name2.equals("ID")) continue;
			c.add(new Criterio(name2));
		}
		c.add(new Criterio("U.NOMBRE"));
		c.add(new Criterio("P.NOMBRE AS NOMBRE_PRODUCTO"));

		return c;
	}
	/**
	 * Metodo que busca el criterios con el nombre que entra como parámetro.
	 * @param name - Nombre a buscar
	 * @return ArrayList con los criterios encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Criterio buscarCriteriosUsuarioPorProducto(String name) throws SQLException, Exception {
		if(name.equals("U.NOMBRE")) return new Criterio("U.NOMBRE");
		if(name.equals("P.NOMBRE AS NOMBRE_PRODUCTO")) return new Criterio(name);
		
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'PRODUCTO' OR TABLE_NAME "
				+ "LIKE 'PEDIDO_PROD' OR  TABLE_NAME LIKE 'INFO_PROD_REST' OR TABLE_NAME LIKE 'CUENTA')) ";
		String sql = "SELECT DISTINCT COLUMN_NAME FROM "+table+" WHERE COLUMN_NAME LIKE'" + name + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		Criterio c=null;
		if (rs.next()) {
			String name2 = rs.getString("COLUMN_NAME");
			c=new Criterio(name2);
		}
		if(c==null)
		{
			if(name.equals("NOMBRE_PRODUCTO"))
				c=new Criterio(name);
		}
		return c;
	}
	/**
	 * Retorna el tipo de dato de cada una de las columnas de producto usados en el sistema.<br>
	 * @return Contenedora de información con los tipos de dato.<br>
	 * @throws SQLException Excepción de la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public ContenedoraInformacion tiposDeDatoUsuarioProducto() throws SQLException, Exception
	{
		ContenedoraInformacion lista= new ContenedoraInformacion();
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'PRODUCTO' OR TABLE_NAME "
				+ "LIKE 'PEDIDO_PROD' OR  TABLE_NAME LIKE 'INFO_PROD_REST' OR TABLE_NAME LIKE 'CUENTA'))";
		String sql = "SELECT COLUMN_NAME, DATA_TYPE FROM "+table;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			String name=rs.getString("COLUMN_NAME");
			lista.agregarInformacion(name, rs.getString("DATA_TYPE"));
		}
		lista.agregarInformacion("U.NOMBRE", "VARCHAR2");
		lista.agregarInformacion("NOMBRE_PRODUCTO", "VARCHAR2");
		return lista;
	}
	
	/**
	 * Método para generar lista filtrada de la información total de las productos.<br>
	 * @param criteriosOrganizacion Listado de criterios de organización.<br>
	 * @param criteriosAgrupamiento Listado de criterios de agrupamiento.<br>
	 * @param agregacionesSeleccion Listado de selección de agregaciones.<br>
	 * @param operacionesWhere Operaciones con where.<br>
	 * @param operacionesHaving Operaciones con having.<br>
	 * @return Listado de contenedora de información con los datos dados.<br>
	 * @throws SQLException Si hay un error con la base de datos.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	public List<Object> generarListaFiltradaUsuarioProductos(
			List<CriterioOrden> criteriosOrganizacion,List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregacionesSeleccion, CriterioVerdad operacionesWhere, 
			CriterioVerdadHaving operacionesHaving, String nombreRestaurante, boolean esDelRestaurante,LimiteFechas limite) throws SQLException, Exception {
		//Verifica que no haya repeticiones de criterios
		List<CriterioOrden> existentesOrd=new ArrayList<>();
		List<Criterio> existentesAgrup= new ArrayList<>();
		List<CriterioAgregacion> agreSelec=new ArrayList<>();
		if(criteriosAgrupamiento!=null)
		{
			if(criteriosAgrupamiento.contains(new Criterio("NOMBRE")))
			{
				criteriosAgrupamiento.remove(new Criterio("NOMBRE"));
				criteriosAgrupamiento.add(new CriterioAgregacion("U.NOMBRE", null, false));
			}
			if(criteriosAgrupamiento.contains(new Criterio("NOMBRE_PRODUCTO")))
			{
				criteriosAgrupamiento.remove(new Criterio("NOMBRE_PRODUCTO"));
				criteriosAgrupamiento.add(new CriterioAgregacion("P.NOMBRE AS NOMBRE_PRODUCTO", null, false));
			}
			for(Criterio c: criteriosAgrupamiento)
			{
				if(existentesAgrup.indexOf(c)>=0) continue;
				existentesAgrup.add(c);
			}
		}
		
		if(criteriosOrganizacion!=null)
		{
			if(criteriosOrganizacion.contains(new Criterio("NOMBRE")))
			{
				criteriosOrganizacion.remove(new Criterio("NOMBRE"));
				criteriosOrganizacion.add(new CriterioOrden(null,"U.NOMBRE", false));
			}
			if(criteriosOrganizacion.contains(new Criterio("NOMBRE_PRODUCTO")))
			{
				criteriosOrganizacion.remove(new Criterio("NOMBRE_PRODUCTO"));
				criteriosOrganizacion.add(new CriterioOrden(null,"P.NOMBRE AS NOMBRE_PRODUCTO", false));
			}
			for(CriterioOrden c: criteriosOrganizacion)
			{
				if(existentesOrd.indexOf(c)>=0) continue;
				if(c.getAgregacion()==null)
				{
					if(existentesAgrup!=null && existentesAgrup.size()>0&& existentesAgrup.indexOf(c)<0) throw new Exception("Los criterios no hacen parte del agrupamiento establecido");
				}
				existentesOrd.add(c);
			}
		}
		
		if(agregacionesSeleccion!=null)
		{
			if(agregacionesSeleccion.contains(new Criterio("NOMBRE")))
			{
				agregacionesSeleccion.remove(new Criterio("NOMBRE"));
				agregacionesSeleccion.add(new CriterioAgregacion("U.NOMBRE", null, false));
			}
			if(agregacionesSeleccion.contains(new Criterio("NOMBRE_PRODUCTO")))
			{
				agregacionesSeleccion.remove(new Criterio("NOMBRE_PRODUCTO"));
				agregacionesSeleccion.add(new CriterioAgregacion("P.NOMBRE AS NOMBRE_PRODUCTO", null, false));
			}
			for(CriterioAgregacion a: agregacionesSeleccion)
			{
				if(agreSelec.indexOf(a)>=0) continue;
				agreSelec.add(a);
			}
		}
		String es="";
		if(!esDelRestaurante) es="NOMBRE_RESTAURANTE<'"+nombreRestaurante+"' AND NOMBRE_RESTAURANTE>'"+nombreRestaurante+"' ";
		else es="NOMBRE_RESTAURANTE = '"+nombreRestaurante+"'";
		//Empieza la creación de los datos del query
		String from =" FROM (PEDIDO_PROD NATURAL INNER JOIN INFO_PROD_REST), PRODUCTO P,USUARIO U, "
				+ " CUENTA";
		String select="SELECT ";
		String groupBy="";
		String orderBy="";
		String where="WHERE ID_PRODUCTO=P.ID "
				+ "AND U.ID=IDUSUARIO AND NUMEROCUENTA=NUMERO_CUENTA "
				+ " AND "+es
				+ "AND FECHA<="+toDate(limite.getFechaFinal())+" AND FECHA>="+toDate(limite.getFechaInicial());
		String having="";
		String temp="";
		//Verifica agrupaciones
		if(existentesAgrup.size()>0)
		{
			temp=simplificarAgrupacion(existentesAgrup.get(0).getNombre());
			if(!temp.equals("") && buscarCriteriosUsuarioPorProducto(temp)==null) 
				throw new Exception("Uno de los criterios no existe "+temp+".");
			select+=existentesAgrup.get(0).getNombre();
			groupBy+="GROUP BY "+existentesAgrup.get(0).getNombre();
			existentesAgrup.remove(0);
			for(Criterio c: existentesAgrup)
			{
				temp=simplificarAgrupacion(c.getNombre());
				if(!temp.equals("") && buscarCriteriosUsuarioPorProducto(temp)==null) 
					throw new Exception("Uno de los criterios no existe "+temp+".");
				groupBy+=", "+c.getNombre();
				select+=", "+c.getNombre();
			}
			for(CriterioAgregacion a:agreSelec) 
			{
				temp=simplificarAgrupacion(a.getNombre());
				if(!temp.equals("") && buscarCriteriosUsuarioPorProducto(temp)==null) 
					throw new Exception("Uno de los criterios no existe");
				select+=","+a.getNombre();
			}
		}
		else
		{
			List<Criterio> criterios=darCriteriosBasicosUsuarioDeProducto();
			select+=criterios.get(0).getNombre();
			criterios.remove(0);
			for(Criterio c: criterios)
			{
				select+=","+c.getNombre();
			}
		}
		//Verifica órdenes
		if(existentesOrd.size()>0)
		{
			if(existentesOrd.get(0).getAgregacion()==null)
			{
				temp=simplificarOrden(existentesOrd.get(0).getNombre());
				if(!temp.equals("") && buscarCriteriosUsuarioPorProducto(simplificarOrden(existentesOrd.get(0).getNombre()))==null) 
					throw new Exception("Uno de los criterios no existe");
			}
			else
			{
				temp=simplificarAgrupacion(existentesOrd.get(0).getAgregacion().getNombre());
				if(!temp.equals("") && buscarCriteriosUsuarioPorProducto(temp)==null) 
					throw new Exception("Uno de los criterios no existe "+temp+".");
			}
			orderBy+="ORDER BY "+existentesOrd.get(0).getNombre();
			existentesOrd.remove(0);
			for(CriterioOrden c: existentesOrd)
			{
				if(c.getAgregacion()==null)
				{
					temp=simplificarOrden(c.getNombre());
					if(!temp.equals("") && buscarCriteriosUsuarioPorProducto(simplificarOrden(c.getNombre()))==null) 
						throw new Exception("Uno de los criterios no existe");
				}
				else
				{
					temp=simplificarAgrupacion(c.getAgregacion().getNombre());
					if(!temp.equals("") && buscarCriteriosUsuarioPorProducto(temp)==null) 
						throw new Exception("Uno de los criterios no existe "+temp+".");
				}
				orderBy+=", "+c.getNombre();
			}
		}
		String operaciones="";
		//Verifica where
		if(operacionesWhere!=null)
			{
				where+=" AND ";
				for(Criterio c:operacionesWhere.getCriterios())
				{
					operaciones=simplificar(c.getNombre());
					if(operaciones.trim().equals("")) continue;
					if(buscarCriteriosUsuarioPorProducto(operaciones)==null)
						throw new Exception("El criterio no existe en la base");
				}
				evaluarWhere(operacionesWhere,tiposDeDatoUsuarioProducto());
				where+=" "+operacionesWhere.getNombre();
			}
		//Verifica having
		if(operacionesHaving!=null)
			{
				for(CriterioAgregacion c:operacionesHaving.getCriterios())
				{
					operaciones=simplificar(c.getNombre());
					if(operaciones.trim().equals("")) continue;
					if(buscarCriteriosUsuarioPorProducto(operaciones)==null)
						throw new Exception("El criterio no existe en la base");
				}
				evaluarHaving(operacionesHaving,tiposDeDatoUsuarioProducto());
				having="HAVING "+operacionesHaving.getNombre();
			}
		String sql=select+" "+from+" "+where+" "+groupBy+" "+having+" "+orderBy;
		PreparedStatement prep =conn.prepareStatement(sql);
		recursos.add(prep);
		System.out.println(sql);
		double time =System.currentTimeMillis();
		ResultSet r =prep.executeQuery();
		time=System.currentTimeMillis()-time;
		System.out.println(time);
		List<ContenedoraInformacion> cont=crearContenedora(r,select);
		List<Object> rta= new ArrayList<>();
		rta.add(cont);
		rta.add(time);
		return rta;
	}
	
	/**
	 * Da todos los criterios básicos de una producto (Sus atributos).<br>
	 * @return Criterios básicos de la producto en forma de lista.<br>
	 * @throws SQLException SI hay un error en la BD.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	public List<Criterio> darCriteriosBasicosUsuarioDeMenu() throws SQLException, Exception
	{
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'MENU' OR TABLE_NAME "
				+ "LIKE 'PEDIDO_MENU' OR TABLE_NAME LIKE 'CUENTA'))";
		String sql = "SELECT DISTINCT COLUMN_NAME FROM "+table;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		
		List<Criterio> c=new ArrayList<>();
		while (rs.next()) {
			String name2 = rs.getString("COLUMN_NAME");
			if(name2.equals("NOMBRE") ||name2.equals("NUMERO_CUENTA")||name2.equals("ID")||name2.equals("NOMBRE_RESTAURANTE")) continue;
			c.add(new Criterio(name2));
		}
		c.add(new Criterio("U.NOMBRE"));
		return c;
	}
	/**
	 * Metodo que busca el criterios con el nombre que entra como parámetro.
	 * @param name - Nombre a buscar
	 * @return ArrayList con los criterios encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Criterio buscarCriteriosUsuarioPorMenu(String name) throws SQLException, Exception {
		if(name.equals("U.NOMBRE")) return new Criterio("U.NOMBRE");
		
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'MENU' OR TABLE_NAME "
				+ "LIKE 'PEDIDO_MENU' OR TABLE_NAME LIKE 'CUENTA'))";
		String sql = "SELECT DISTINCT COLUMN_NAME FROM "+table+" WHERE COLUMN_NAME LIKE'" + name + "'";

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
	 * Retorna el tipo de dato de cada una de las columnas de producto usados en el sistema.<br>
	 * @return Contenedora de información con los tipos de dato.<br>
	 * @throws SQLException Excepción de la BD.<br>
	 * @throws Exception Cualquier otro error.
	 */
	public ContenedoraInformacion tiposDeDatoUsuarioMenu() throws SQLException, Exception
	{
		ContenedoraInformacion lista= new ContenedoraInformacion();
		String table="(SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER LIKE 'ISIS2304A061720' AND (TABLE_NAME LIKE 'MENU' OR TABLE_NAME "
				+ "LIKE 'PEDIDO_MENU' OR TABLE_NAME LIKE 'CUENTA'))";
		String sql = "SELECT COLUMN_NAME, DATA_TYPE FROM "+table;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while(rs.next())
		{
			String name=rs.getString("COLUMN_NAME");
			lista.agregarInformacion(name, rs.getString("DATA_TYPE"));
		}
		lista.agregarInformacion("U.NOMBRE", "VARCHAR2");
		return lista;
	}
	
	/**
	 * Método para generar lista filtrada de la información total de las productos.<br>
	 * @param criteriosOrganizacion Listado de criterios de organización.<br>
	 * @param criteriosAgrupamiento Listado de criterios de agrupamiento.<br>
	 * @param agregacionesSeleccion Listado de selección de agregaciones.<br>
	 * @param operacionesWhere Operaciones con where.<br>
	 * @param operacionesHaving Operaciones con having.<br>
	 * @return Listado de contenedora de información con los datos dados.<br>
	 * @throws SQLException Si hay un error con la base de datos.<br>
	 * @throws Exception Si hay cualquier otro error.
	 */
	public List<Object> generarListaFiltradaUsuarioMenus(
			List<CriterioOrden> criteriosOrganizacion,List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregacionesSeleccion, CriterioVerdad operacionesWhere, 
			CriterioVerdadHaving operacionesHaving, String nombreRestaurante, boolean esDelRestaurante,LimiteFechas limite) throws SQLException, Exception {
		//Verifica que no haya repeticiones de criterios
		List<CriterioOrden> existentesOrd=new ArrayList<>();
		List<Criterio> existentesAgrup= new ArrayList<>();
		List<CriterioAgregacion> agreSelec=new ArrayList<>();
		if(criteriosAgrupamiento!=null)
		{
			if(criteriosAgrupamiento.contains(new Criterio("NOMBRE")))
			{
				criteriosAgrupamiento.remove(new Criterio("NOMBRE"));
				criteriosAgrupamiento.add(new CriterioAgregacion("U.NOMBRE", null, false));
			}
			for(Criterio c: criteriosAgrupamiento)
			{
				if(existentesAgrup.indexOf(c)>=0) continue;
				existentesAgrup.add(c);
			}
		}
		if(criteriosOrganizacion!=null)
		{
			if(criteriosOrganizacion.contains(new Criterio("NOMBRE")))
			{
				criteriosOrganizacion.remove(new Criterio("NOMBRE"));
				criteriosOrganizacion.add(new CriterioOrden(null, "U.NOMBRE", false));
			}
			for(CriterioOrden c: criteriosOrganizacion)
			{
				
				if(existentesOrd.indexOf(c)>=0) continue;
				if(c.getAgregacion()==null)
				{
					if(existentesAgrup!=null && existentesAgrup.size()>0&& existentesAgrup.indexOf(c)<0) throw new Exception("Los criterios no hacen parte del agrupamiento establecido");
				}
				existentesOrd.add(c);
			}
		}
		
		if(agregacionesSeleccion!=null)
		{
			if(agregacionesSeleccion.contains(new Criterio("NOMBRE")))
			{
				agregacionesSeleccion.remove(new Criterio("NOMBRE"));
				agregacionesSeleccion.add(new CriterioAgregacion("U.NOMBRE", null, false));
			}
			for(CriterioAgregacion a: agregacionesSeleccion)
			{
				if(agreSelec.indexOf(a)>=0) continue;
				agreSelec.add(a);
			}
		}
		
		String es="";
		if(!esDelRestaurante) es="PM.NOMBRE_RESTAURANTE<'"+nombreRestaurante+"' AND PM.NOMBRE_RESTAURANTE>'"+nombreRestaurante+"' ";
		else es="PM.NOMBRE_RESTAURANTE = '"+nombreRestaurante+"'";
		//Empieza la creación de los datos del query
		String from =" FROM  PEDIDO_MENU PM, MENU M ,USUARIO U, CUENTA ";
		String select="SELECT ";
		String groupBy="";
		String orderBy="";
		String where=" WHERE M.NOMBRE=PM.NOMBRE_MENU AND M.NOMBRE_RESTAURANTE=PM.NOMBRE_RESTAURANTE AND U.ID=IDUSUARIO AND NUMEROCUENTA=NUMERO_CUENTA "
				+ "AND "+es+" AND FECHA<="+toDate(limite.getFechaFinal())+ " AND FECHA>="+toDate(limite.getFechaInicial()); 
		String having="";
		String temp="";
		//Verifica agrupaciones
		if(existentesAgrup.size()>0)
		{
			temp=simplificarAgrupacion(existentesAgrup.get(0).getNombre());
			if(!temp.equals("") && buscarCriteriosUsuarioPorMenu(temp)==null) 
				throw new Exception("Uno de los criterios no existe "+temp+".");
			select+=existentesAgrup.get(0).getNombre();
			groupBy+="GROUP BY "+existentesAgrup.get(0).getNombre();
			existentesAgrup.remove(0);
			for(Criterio c: existentesAgrup)
			{
				temp=simplificarAgrupacion(c.getNombre());
				if(!temp.equals("") && buscarCriteriosUsuarioPorMenu(temp)==null) 
					throw new Exception("Uno de los criterios no existe "+temp+".");
				groupBy+=", "+c.getNombre();
				select+=", "+c.getNombre();
			}
			for(CriterioAgregacion a:agreSelec) 
			{
				temp=simplificarAgrupacion(a.getNombre());
				if(!temp.equals("") && buscarCriteriosUsuarioPorMenu(temp)==null) 
					throw new Exception("Uno de los criterios no existe");
				select+=","+a.getNombre();
			}
		}
		else
		{
			List<Criterio> criterios=darCriteriosBasicosUsuarioDeMenu();
			select+=criterios.get(0).getNombre();
			criterios.remove(0);
			for(Criterio c: criterios)
			{
				select+=","+c.getNombre();
			}
		}
		//Verifica órdenes
		if(existentesOrd.size()>0)
		{
			if(existentesOrd.get(0).getAgregacion()==null)
			{
				temp=simplificarOrden(existentesOrd.get(0).getNombre());
				if(!temp.equals("") && buscarCriteriosUsuarioPorMenu(simplificarOrden(existentesOrd.get(0).getNombre()))==null) 
					throw new Exception("Uno de los criterios no existe "+temp+".");
			}
			else
			{
				temp=simplificarAgrupacion(existentesOrd.get(0).getAgregacion().getNombre());
				if(!temp.equals("") && buscarCriteriosUsuarioPorMenu(temp)==null) 
					throw new Exception("Uno de los criterios no existe "+temp+".");
			}
			orderBy+="ORDER BY "+existentesOrd.get(0).getNombre();
			existentesOrd.remove(0);
			for(CriterioOrden c: existentesOrd)
			{
				if(c.getAgregacion()==null)
				{
					temp=simplificarOrden(c.getNombre());
					if(!temp.equals("") && buscarCriteriosUsuarioPorMenu(simplificarOrden(c.getNombre()))==null) 
						throw new Exception("Uno de los criterios no existe");
				}
				else
				{
					temp=simplificarAgrupacion(c.getAgregacion().getNombre());
					if(!temp.equals("") && buscarCriteriosUsuarioPorMenu(temp)==null) 
						throw new Exception("Uno de los criterios no existe "+temp+".");
				}
				orderBy+=", "+c.getNombre();
			}
		}
		String operaciones="";
		//Verifica where
		if(operacionesWhere!=null)
			{
				where+=" AND";
				for(Criterio c:operacionesWhere.getCriterios())
				{
					operaciones=simplificar(c.getNombre());
					if(operaciones.trim().equals("")) continue;
					if(buscarCriteriosUsuarioPorMenu(operaciones)==null)
						throw new Exception("El criterio no existe en la base");
				}
				evaluarWhere(operacionesWhere,tiposDeDatoUsuarioMenu());
				where+=" "+operacionesWhere.getNombre();
			}
		//Verifica having
		if(operacionesHaving!=null)
			{
				for(CriterioAgregacion c:operacionesHaving.getCriterios())
				{
					operaciones=simplificar(c.getNombre());
					if(operaciones.trim().equals("")) continue;
					if(buscarCriteriosUsuarioPorMenu(operaciones)==null)
						throw new Exception("El criterio no existe en la base");
				}
				evaluarHaving(operacionesHaving,tiposDeDatoUsuarioMenu());
				having="HAVING "+operacionesHaving.getNombre();
			}
		String sql=select+" "+from+" "+where+" "+groupBy+" "+having+" "+orderBy;
		PreparedStatement prep =conn.prepareStatement(sql);
		recursos.add(prep);
		System.out.println(sql);
		double time =System.currentTimeMillis();
		ResultSet r =prep.executeQuery();
		time=System.currentTimeMillis()-time;
		System.out.println(time);
		List<ContenedoraInformacion> cont=crearContenedora(r,select);
		List<Object> rta= new ArrayList<>();
		rta.add(cont);
		rta.add(time);
		return rta;
	}
	
	private String toDate(Date fecha) {
		SimpleDateFormat x = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return "TO_DATE('"+ x.format(fecha) + "', 'yyyy-MM-dd hh24:mi:ss')";
	}

}
