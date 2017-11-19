package test;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import dao.DAOTablaRestaurante;
import tm.RotondAndesTM;
import vos.CuentaMinimum;
import vos.Usuario;
import vos.UsuarioMinimum;
import vos.UsuarioMinimum.Rol;

public class PruebasJavaContraSQL {

	/**
	 * Arraylits de recursos que se usan para la ejecuci�n de sentencias SQL
	 */
	private  ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexi�n a la base de datos
	 */
	private  Connection conn;

	/**
	 * Metodo constructor que crea DAORestaurante
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public PruebasJavaContraSQL() {
		recursos = new ArrayList<Object>();
	}
	/**
	 * Obtiene el listado de Usuarios en representación minimum.<br>
	 * @param rs ResultSet
	 * @return Lista de Usuario Minimum.<br>
	 * @throws Exception Si hay algún error.
	 */
	public  ArrayList<UsuarioMinimum> obtenerListadoUsuarios(ResultSet rs) throws Exception
	{
		ArrayList<UsuarioMinimum> list = new ArrayList<>();
		while(rs.next())
		{
			String name = rs.getString("NOMBRE");
			Long id = rs.getLong("ID");
			String correo = rs.getString("CORREO");
			Rol r = convertirARol(rs.getString("ROL"));
			list.add(new UsuarioMinimum(name, id, correo, r));
		}
		return list;
	}
	
	/**
	 * Convierte el rol pasado como parÃ¡metro a un String.<br>
	 * @param nombreRol Nombre del rol.<br>
	 * @return Equivalencia en String
	 */
	private  Rol convertirARol(String nombreRol) {
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
	 * Modifica la conexión al sistema.<br>
	 * @param darConexion Conexión a modificar.
	 */
	public void setConn(Connection darConexion) {
		this.conn=darConexion;
	}
	/**
	 * Metodo que cierra todos los recursos que estan enel arreglo de recursos
	 * <b>post: </b> Todos los recurso del arreglo de recursos han sido cerrados
	 */
	public  void cerrarRecursos() {
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
	 * Prueba de selección de datos sobre una tabla pequeña(Con 1000 datos) que sería Usuario.<br>
	 * @throws Exception Si hay algún error
	 */
	public  void selectPequenho() throws Exception
	{
		String lineaAImprimir="";
		File f = new File("./data/pruebasSQLVSJava/selectPequenho.csv");
		FileWriter fw= new FileWriter(f);
		fw.write("TIEMPO SQL, TIEMPO JAVA\n");
		String sql="";
		double promSql=0;
		double promJava=0;
		double time=0;
		int i=0;
		while(i<10)
		{
			lineaAImprimir="";
			sql="SELECT * FROM  USUARIO WHERE ROL='CLIENTE' AND CORREO IS NOT NULL";
			PreparedStatement ps = conn.prepareStatement(sql);
			recursos.add(ps);
			
			time=System.currentTimeMillis();
			ResultSet rs=ps.executeQuery();
			ArrayList<UsuarioMinimum> list = obtenerListadoUsuarios(rs);
			
			time=System.currentTimeMillis()-time;
			
			promSql=(promSql*i+time)/(i+1);
			lineaAImprimir+=time+",";
			
			System.out.println(time);
			sql="SELECT * FROM USUARIO";
			ps=conn.prepareStatement(sql);
			recursos.add(ps);
			time=System.currentTimeMillis();
			rs=ps.executeQuery();
			ArrayList<UsuarioMinimum> newList =new ArrayList<>();
			while(rs.next())
			{
				
				String correo = rs.getString("CORREO");
				Rol r = convertirARol(rs.getString("ROL"));
				if(correo!=null && !correo.equals("NULL") && r.equals(Rol.CLIENTE))
				{
					String name = rs.getString("NOMBRE");
					Long id = rs.getLong("ID");
					newList.add(new UsuarioMinimum(name, id, correo, r));
				}
			}
			time=System.currentTimeMillis()-time;
			System.out.println(time);
			lineaAImprimir+=time+"\n";
			promJava=(promJava*i+time)/(i+1);
			
			if(list.size()!=newList.size()) throw new Exception("Las listas son de tamaño diferente "+list.size()+"VS"+newList.size());
			fw.write(lineaAImprimir);
			System.out.println(lineaAImprimir);
			i++;
		}
		fw.write("PROMEDIO SQL, PROMEDIO JAVA\n");
		lineaAImprimir=promSql+","+promJava;
		fw.write(lineaAImprimir);
		fw.close();
		 cerrarRecursos();
	}
	
	
	/**
	 * Prueba de proyección de datos sobre una tabla pequeña(Con 1000 datos) que sería Usuario.<br>
	 * @throws Exception Si hay algún error
	 */
	public void proyeccionPequenho() throws Exception
	{
		String lineaAImprimir="";
		File f = new File("./data/pruebasSQLVSJava/proyeccionPequenho.csv");
		FileWriter fw= new FileWriter(f);
		fw.write("TIEMPO SQL, TIEMPO JAVA\n");
		String sql="";
		double promSql=0;
		double promJava=0;
		double time=0;
		int i=0;
		while(i<10)
		{
			lineaAImprimir="";
			sql="SELECT NOMBRE,ROL FROM  USUARIO";
			PreparedStatement ps = conn.prepareStatement(sql);
			recursos.add(ps);
			
			time=System.currentTimeMillis();
			ResultSet rs=ps.executeQuery();
			ArrayList<UsuarioMinimum> list = new ArrayList<>();
			while(rs.next())
			{
				list.add(new UsuarioMinimum(rs.getString("NOMBRE"),null,null, convertirARol(rs.getString("ROL"))));
			}
			time=System.currentTimeMillis()-time;
			
			promSql=(promSql*i+time)/(i+1);
			lineaAImprimir+=time+",";
			
			System.out.println(time);
			sql="SELECT * FROM USUARIO";
			ps=conn.prepareStatement(sql);
			recursos.add(ps);
			time=System.currentTimeMillis();
			rs=ps.executeQuery();
			ArrayList<UsuarioMinimum> newList =new ArrayList<>();
			while(rs.next())
			{
				newList.add(new UsuarioMinimum(rs.getString("NOMBRE"),null,null, convertirARol(rs.getString("ROL"))));
			}
			time=System.currentTimeMillis()-time;
			System.out.println(time);
			lineaAImprimir+=time+"\n";
			promJava=(promJava*i+time)/(i+1);
			
			if(list.size()!=newList.size()) throw new Exception("Las listas son de tamaño diferente "+list.size()+"VS"+newList.size());
			fw.write(lineaAImprimir);
			System.out.println(lineaAImprimir);
			i++;
		}
		fw.write("PROMEDIO SQL, PROMEDIO JAVA\n");
		lineaAImprimir=promSql+","+promJava;
		fw.write(lineaAImprimir);
		fw.close();
		 cerrarRecursos();
	}
	/**
	 * Prueba de join de datos sobre una tabla pequeña(Con 1000 datos) que sería Usuario.<br>
	 * @throws Exception Si hay algún error
	 */
	public void joinPequenho() throws Exception
	{
		String lineaAImprimir="";
		File f = new File("./data/pruebasSQLVSJava/joinPequenho.csv");
		FileWriter fw= new FileWriter(f);
		fw.write("TIEMPO SQL, TIEMPO JAVA\n");
		String sql="";
		double promSql=0;
		double promJava=0;
		double time=0;
		int i=0;
		while(i<10)
		{
			lineaAImprimir="";
			sql="SELECT * FROM  USUARIO,CUENTA WHERE USUARIO.ID=CUENTA.IDUSUARIO";
			PreparedStatement ps = conn.prepareStatement(sql);
			recursos.add(ps);
			time=System.currentTimeMillis();
			HashMap<Long,Usuario> hash= new HashMap<Long,Usuario>();
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				Long id = rs.getLong("ID");
				if(!hash.containsKey(id))
				{
					String name = rs.getString("NOMBRE");
					String correo = rs.getString("CORREO");
					Rol r = convertirARol(rs.getString("ROL"));
					hash.put(id,new Usuario(name,id,correo,r,null,new ArrayList<CuentaMinimum>(),null));
					String numCuenta = rs.getString("NUMEROCUENTA");
					Double valor = rs.getDouble("VALOR");
					Date fecha = rs.getDate("FECHA");
					boolean pagado=rs.getString("PAGADA").equals("1")?true:false;
					hash.get(id).getHistorial().add(new CuentaMinimum(valor,numCuenta,fecha,pagado));
				}
				else
				{
					String numCuenta = rs.getString("NUMEROCUENTA");
					Double valor = rs.getDouble("VALOR");
					Date fecha = rs.getDate("FECHA");
					boolean pagado=rs.getString("PAGADA").equals("1")?true:false;
					hash.get(id).getHistorial().add(new CuentaMinimum(valor,numCuenta,fecha,pagado));
				}
				
				
			}
			time=System.currentTimeMillis()-time;
			
			promSql=(promSql*i+time)/(i+1);
			lineaAImprimir+=time+",";
			
			System.out.println(time);
			sql="SELECT * FROM USUARIO";
			ps=conn.prepareStatement(sql);
			recursos.add(ps);
			time=System.currentTimeMillis();
			rs=ps.executeQuery();
			HashMap<Long,Usuario> newHash= new HashMap<Long,Usuario>();
			while(rs.next())
			{
				Long id= rs.getLong("ID");
				String name = rs.getString("NOMBRE");
				String correo = rs.getString("CORREO");
				Rol r = convertirARol(rs.getString("ROL"));
				newHash.put(id,new Usuario(name,id,correo,r,null,new ArrayList<CuentaMinimum>(),null));
			}
			sql="SELECT * FROM CUENTA";
			ps=conn.prepareStatement(sql);
			recursos.add(ps);
			rs=ps.executeQuery();
			while(rs.next())
			{
				Long id=rs.getLong("IDUSUARIO");
				if(newHash.containsKey(id))
				{
					String numCuenta = rs.getString("NUMEROCUENTA");
					Double valor = rs.getDouble("VALOR");
					Date fecha = rs.getDate("FECHA");
					boolean pagado=rs.getString("PAGADA").equals("1")?true:false;
					newHash.get(id).getHistorial().add(new CuentaMinimum(valor,numCuenta,fecha,pagado));
				}
			}
			
			time=System.currentTimeMillis()-time;
			System.out.println(time);
			lineaAImprimir+=time+"\n";
			promJava=(promJava*i+time)/(i+1);
			
			fw.write(lineaAImprimir);
			System.out.println(lineaAImprimir);
			i++;
		}
		fw.write("PROMEDIO SQL, PROMEDIO JAVA\n");
		lineaAImprimir=promSql+","+promJava;
		fw.write(lineaAImprimir);
		fw.close();
		 cerrarRecursos();
	}
	
	public void generalPequenho() throws Exception
	{
		String lineaAImprimir="";
		File f = new File("./data/pruebasSQLVSJava/generalPequenho.csv");
		FileWriter fw= new FileWriter(f);
		fw.write("TIEMPO SQL, TIEMPO JAVA\n");
		String sql="";
		double promSql=0;
		double promJava=0;
		double time=0;
		int i=0;
		while(i<10)
		{
			lineaAImprimir="";
			sql="SELECT ID,NOMBRE FROM  USUARIO,CUENTA WHERE USUARIO.ID=CUENTA.IDUSUARIO AND CUENTA.VALOR>1000";
			PreparedStatement ps = conn.prepareStatement(sql);
			recursos.add(ps);
			time=System.currentTimeMillis();
			ArrayList<String> list= new ArrayList<>();
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				list.add(rs.getLong("ID")+"-"+rs.getString("NOMBRE"));
			}
			time=System.currentTimeMillis()-time;
			
			promSql=(promSql*i+time)/(i+1);
			lineaAImprimir+=time+",";
			
			System.out.println(time);
			sql="SELECT * FROM USUARIO";
			ps=conn.prepareStatement(sql);
			recursos.add(ps);
			time=System.currentTimeMillis();
			rs=ps.executeQuery();
			HashMap<Long,String> newHash= new HashMap<Long,String>();
			ArrayList<String> newList=new ArrayList<>();
			while(rs.next())
			{
				Long id= rs.getLong("ID");
				String name = rs.getString("NOMBRE");
				newHash.put(id,id+"-"+name);
			}
			sql="SELECT * FROM CUENTA";
			ps=conn.prepareStatement(sql);
			recursos.add(ps);
			rs=ps.executeQuery();
			while(rs.next())
			{
				Long id=rs.getLong("IDUSUARIO");
				if(newHash.containsKey(id))
				{
					Double valor = rs.getDouble("VALOR");
					if(valor>1000)
					{
						newList.add(newHash.get(id));
					}
				}
			}
			time=System.currentTimeMillis()-time;
			System.out.println(time);
			lineaAImprimir+=time+"\n";
			promJava=(promJava*i+time)/(i+1);
			if(list.size()!=newList.size()) throw new Exception(list.size()+"VS"+newList.size());
			fw.write(lineaAImprimir);
			System.out.println(lineaAImprimir);
			i++;
		}
		fw.write("PROMEDIO SQL, PROMEDIO JAVA\n");
		lineaAImprimir=promSql+","+promJava;
		fw.write(lineaAImprimir);
		fw.close();
		 cerrarRecursos();
	}
	
	/**
	 * Método que llama el TransactionManager para guardar la información en archivos csv.<br>
	 * @throws Exception Si hay alguna excepción.
	 */
	public void pruebas() throws Exception
	{
		
		//Con datos pequeños
		//selectPequenho();
		//proyeccionPequenho();
		//joinPequenho();
		//generalPequenho();
		//Con datos grandes
	}
	
}
