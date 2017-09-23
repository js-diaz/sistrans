/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: VideoAndes
 * Autor: Juan Felipe García - jf.garcia268@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package tm;

import java.io.File;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import dao.*;
import vos.CondicionTecnica;
import vos.Cuenta;
import vos.Ingrediente;
import vos.Preferencia;
import vos.Producto;
import vos.Producto.TiposDePlato;
import vos.Restaurante;
import vos.Usuario;
import vos.Usuario.Rol;
import vos.Video;
import vos.Zona;
import vos.Categoria;

/**
 * Transaction Manager de la aplicacion (TM)
 * Fachada en patron singleton de la aplicacion
 * @author Monitores 2017-20
 */
public class RotondAndesTM {

	/**
	 * Atributo estatico que contiene el path relativo del archivo que tiene los datos de la conexion
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estatico que contiene el path absoluto del archivo que tiene los datos de la conexion
	 */
	private  String connectionDataPath;

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de datos.
	 */
	private String driver;
	
	/**
	 * conexion a la base de datos
	 */
	private Connection conn;


	/**
	 * Metodo constructor de la clase VideoAndesMaster, esta clase modela y contiene cada una de las 
	 * transacciones y la logica de negocios que estas conllevan.
	 * <b>post: </b> Se crea el objeto VideoAndesTM, se inicializa el path absoluto del archivo de conexion y se
	 * inicializa los atributos que se usan par la conexion a la base de datos.
	 * @param contextPathP - path absoluto en el servidor del contexto del deploy actual
	 */
	public RotondAndesTM(String contextPathP) {
		connectionDataPath = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
		initConnectionData();
	}

	/**
	 * Metodo que  inicializa los atributos que se usan para la conexion a la base de datos.
	 * <b>post: </b> Se han inicializado los atributos que se usan par la conexion a la base de datos.
	 */
	private void initConnectionData() {
		try {
			File arch = new File(this.connectionDataPath);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(arch);
			prop.load(in);
			in.close();
			this.url = prop.getProperty("url");
			this.user = prop.getProperty("usuario");
			this.password = prop.getProperty("clave");
			this.driver = prop.getProperty("driver");
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que  retorna la conexion a la base de datos
	 * @return Connection - la conexion a la base de datos
	 * @throws SQLException - Cualquier error que se genere durante la conexion a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("Connecting to: " + url + " With user: " + user);
		return DriverManager.getConnection(url, user, password);
	}

	////////////////////////////////////////
	///////Transacciones////////////////////
	////////////////////////////////////////

	//CATEGORIA
	/**
	 * Retorna las categorías del sistema.<br>
	 * @return Lista de categorías.<br>
	 * @throws Exception Si llega a pasar algo.
	 */
	public List<Categoria> categoriaDarCategorias() throws Exception{
		List<Categoria> categorias;
		DAOTablaCategoria dao= new DAOTablaCategoria();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			categorias=dao.darCategorias();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return categorias;
	}
	
	public Categoria categoriaBuscarCategoriasPorName(String name) throws Exception
	{
		DAOTablaCategoria dao= new DAOTablaCategoria();
		Categoria c=null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			c=dao.buscarCategoriasPorName(name);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return c;
	}
	
	public void categoriaAddCategoria(Categoria c) throws Exception
	{
		DAOTablaCategoria dao = new DAOTablaCategoria();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addCategoria(c);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void categoriaDeleteCategoria (Categoria c) throws Exception
	{
		DAOTablaCategoria dao = new DAOTablaCategoria();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteCategoria(c);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	//CategoriaProducto
	
	//CategoriaMenu
	
	//CategoriaRestaurante
	
	//Condiciones
	
	public List<CondicionTecnica> condicionDarCondicionesTecnicas() throws Exception
	{
		DAOTablaCondiciones dao = new DAOTablaCondiciones();
		List<CondicionTecnica> list= null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darCondicionTecnicas();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public CondicionTecnica condicionBuscarCondicionTecnicasPorName(String name) throws Exception
	{
		DAOTablaCondiciones dao= new DAOTablaCondiciones();
		CondicionTecnica c =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			c=dao.buscarCondicionTecnicasPorName(name);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return c;
	}
	
	public void condicionAddCondicionTecnica(CondicionTecnica c) throws Exception 
	{
		DAOTablaCondiciones dao = new DAOTablaCondiciones();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addCondicionTecnica(c);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void condicionDeleteCondicionTecnica(CondicionTecnica c) throws Exception
	{
		DAOTablaCondiciones dao = new DAOTablaCondiciones();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteCondicionTecnica(c);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	//CondicionesZona
	public List<CondicionTecnica> condicionZonaConsultarZona(String nombreZona) throws Exception
	{
		DAOTablaCondicionesZona dao = new DAOTablaCondicionesZona();
		List<CondicionTecnica> list=null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.consultarZona(nombreZona);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
		
	}
	
	public List<Zona> condicionZonaConsultarPorCondicion (String nombreCondicion) throws Exception
	{
		DAOTablaCondicionesZona dao = new DAOTablaCondicionesZona();
		List<Zona> list = null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.consultarPorCondicion(nombreCondicion);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public void condicionZonaInsertarPorZona(String nombreZona, List<CondicionTecnica> condiciones) throws Exception
	{
		DAOTablaCondicionesZona dao = new DAOTablaCondicionesZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.insertarPorZona(nombreZona, condiciones);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void condicionZonaInsertarPorCondicion(String nombre, List<Zona> zonas) throws Exception
	{
		DAOTablaCondicionesZona dao = new DAOTablaCondicionesZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.insertarPorCondicion(nombre, zonas);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void condicionZonaInsertarPorCondicionYZona(String nombreCondicion, String nombreZona) throws Exception
	{
		DAOTablaCondicionesZona dao = new DAOTablaCondicionesZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.insertarPorCondicionYZona(nombreCondicion, nombreZona);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void condicionZonaEliminarCondicion(String nombreZona, String nombreCondicion) throws Exception
	{
		DAOTablaCondicionesZona dao = new DAOTablaCondicionesZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.eliminarCondicion(nombreZona, nombreCondicion);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void condicionZonaEliminarCondicionesPorZona(String nombreZona) throws Exception
	{
		DAOTablaCondicionesZona dao = new DAOTablaCondicionesZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.eliminarCondicionesPorZona(nombreZona);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void condicionZonaEliminarCondicionesPorCondicion(String condicionTecnica) throws Exception
	{
		DAOTablaCondicionesZona dao = new DAOTablaCondicionesZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.eliminarCondicionesPorCondicion(condicionTecnica);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	//Cuenta
	
	public List<Cuenta> cuentaDarCuentas() throws Exception
	{
		DAOTablaCuenta dao = new DAOTablaCuenta();
		List<Cuenta> list=null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darCuentas();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public Cuenta cuentaBuscarCuentasPorNumeroDeCuenta(String numeroCuenta) throws Exception
	{
		DAOTablaCuenta dao = new DAOTablaCuenta();
		Cuenta c =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			c=dao.buscarCuentasPorNumeroDeCuenta(numeroCuenta);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return c;
	}
	
	public List<Cuenta> cuentaBuscarCuentasPorId(Long id) throws Exception
	{
		DAOTablaCuenta dao = new DAOTablaCuenta();
		List<Cuenta> list =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.buscarCuentasPorId(id);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public void cuentaAddCuenta(Cuenta c) throws Exception
	{
		DAOTablaCuenta dao = new DAOTablaCuenta();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addCuenta(c);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void cuentaUpdateCuenta(Cuenta c) throws Exception
	{
		DAOTablaCuenta dao = new DAOTablaCuenta();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.updateCuenta(c);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void cuentaDeleteCuenta (Cuenta c) throws Exception
	{
		DAOTablaCuenta dao = new DAOTablaCuenta();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteCuenta(c);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void cuentaBorrarHistorialCliente(Long id) throws Exception
	{
		DAOTablaCuenta dao = new DAOTablaCuenta();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.borrarHistorialCliente(id);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	//Ingrediente
	public List<Ingrediente> ingredienteDarIngredientes() throws Exception
	{
		ArrayList<Ingrediente> list = null;
		DAOTablaIngrediente dao = new DAOTablaIngrediente();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darIngredientes();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public Ingrediente ingredienteBuscarIngredientePorId(Long id) throws Exception
	{
		Ingrediente i = null;
		DAOTablaIngrediente dao = new DAOTablaIngrediente();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			i=dao.buscarIngredientePorId(id);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return i;
	}
	
	public void ingredienteAddIngrediente(Ingrediente ingrediente) throws Exception
	{
		DAOTablaIngrediente dao = new DAOTablaIngrediente();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addIngrediente(ingrediente);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void ingredienteUpdateIngrediente(Ingrediente ingrediente) throws Exception
	{
		DAOTablaIngrediente dao = new DAOTablaIngrediente();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.updateIngrediente(ingrediente);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void ingredienteDeleteIngrediente(Ingrediente ingrediente) throws Exception
	{
		DAOTablaIngrediente dao = new DAOTablaIngrediente();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteIngrediente(ingrediente);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	//PedidoMenu
	
	//PedidoProducto
	
	//PerteneceAProducto
	
	//Preferencia
	public List<Preferencia> preferenciaDarPreferencias() throws Exception
	{
		DAOTablaPreferencia dao = new DAOTablaPreferencia();
		List<Preferencia> list = null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darPreferencias();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public Preferencia preferenciaBuscarPreferenciaPorId(Long id) throws Exception
	{
		Preferencia p =null;
		DAOTablaPreferencia dao = new DAOTablaPreferencia();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			p=dao.buscarPreferenciaPorId(id);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return p;
	}
	
	public List<Preferencia> preferenciaBuscarPreferenciaPorRango(Double ini, Double fin) throws Exception
	{
		DAOTablaPreferencia dao = new DAOTablaPreferencia();
		List<Preferencia> list =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.buscarPreferenciaPorRango(ini, fin);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public void preferenciaAddPreferencia(Long idUsuario, Preferencia p) throws Exception
	{
		DAOTablaPreferencia dao = new DAOTablaPreferencia();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addPreferencia(idUsuario, p);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void preferenciaActualizarPreferenciasDePrecioDeUsuario(Long idUsuario, Preferencia p) throws Exception
	{
		DAOTablaPreferencia dao = new DAOTablaPreferencia();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.actualizarPreferenciasDePrecioDeUsuario(idUsuario, p);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void preferenciaDeletePreferencia(Long idUsuario, Preferencia p) throws Exception
	{
		DAOTablaPreferencia dao = new DAOTablaPreferencia();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deletePreferencia(idUsuario, p);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	//PreferenciaCategoria
	
	public List<Categoria> buscarCategoriasPorId(Long id) throws Exception
	{
		DAOTablaPreferenciaCategoria dao = new DAOTablaPreferenciaCategoria();
		List<Categoria> list =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.buscarCategoriaPorId(id);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public List<Preferencia> preferenciaCategoriaBuscarCategoriaPorCategoria(Categoria cat) throws Exception
	{
		DAOTablaPreferenciaCategoria dao = new DAOTablaPreferenciaCategoria();
		List<Preferencia> list=null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.buscarCategoriaPorCategoria(cat);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public void preferenciaCategoriaInsertarPreferenciasPorCategoria(Long idUsuario,List<Categoria> categorias) throws Exception
	{
		DAOTablaPreferenciaCategoria dao = new DAOTablaPreferenciaCategoria();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.insertarPreferenciasCategoria(idUsuario, categorias);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void preferenciaCategoriaBorrarPorId(Long idUsuario) throws Exception
	{
		DAOTablaPreferenciaCategoria dao = new DAOTablaPreferenciaCategoria();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.borrarPorId(idUsuario);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void preferenciaCategoriaBorrarPorCategoria(String id) throws Exception
	{
		DAOTablaPreferenciaCategoria dao = new DAOTablaPreferenciaCategoria();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.borrarPorCategoria(id);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void preferenciaCategoriaBorrarMulticriterio(Long idUsuario, String id) throws Exception
	{
		DAOTablaPreferenciaCategoria dao = new DAOTablaPreferenciaCategoria();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.borrarMulticriterio(idUsuario, id);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	///PreferenciaZona
	
	public List<Zona> preferenciaZonaBuscarZonasPorId(Long id) throws Exception
	{
		DAOTablaPreferenciaZona dao = new DAOTablaPreferenciaZona();
		List<Zona> list=null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.buscarZonaPorId(id);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public List<Preferencia> preferenciaZonaBuscarZonaPorZona(Zona cat) throws Exception
	{
		DAOTablaPreferenciaZona dao = new DAOTablaPreferenciaZona();
		List<Preferencia> list =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.buscarZonaPorZona(cat);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public void preferenciaZonaInsertarPreferenciasZona(Long idUsuario, List<Zona> zonas) throws Exception
	{
		DAOTablaPreferenciaZona dao = new DAOTablaPreferenciaZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.insertarPreferenciasZona(idUsuario, zonas);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void preferenciaZonaBorrarPorId(Long idUsuario) throws Exception
	{
		DAOTablaPreferenciaZona dao = new DAOTablaPreferenciaZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.borrarPorId(idUsuario);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void preferenciaZonaModificarPorZonaEliminada(String id) throws Exception
	{
		DAOTablaPreferenciaZona dao = new DAOTablaPreferenciaZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.modificarPorZonaEliminada(id);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void preferenciaZonaBorrarMulticriterio(Long idUsuario,String id) throws Exception
	{
		DAOTablaPreferenciaZona dao = new DAOTablaPreferenciaZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.borrarMulticriterio(idUsuario, id);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	//Producto
	public List<Producto> productoDarProductos() throws Exception
	{
		List<Producto> list =null;
		DAOTablaProducto dao = new DAOTablaProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darProductos();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public List<Producto> productoBuscarProductoPorName(String name) throws Exception
	{
		DAOTablaProducto dao = new DAOTablaProducto();
		List<Producto> list =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.buscarProductosPorName(name);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public Producto productoBuscarProductoPorId(Long id) throws Exception
	{
		DAOTablaProducto dao = new DAOTablaProducto();
		Producto p =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			p=dao.buscarProductoPorId(id);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return p;
	}
	
	public void productoAddProducto(Producto producto) throws Exception
	{
		DAOTablaProducto dao = new DAOTablaProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addProducto(producto);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void productoUpdateProducto(Producto producto) throws Exception
	{
		DAOTablaProducto dao = new DAOTablaProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.updateProducto(producto);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void deleteProducto(Producto producto) throws Exception
	{
		DAOTablaProducto dao = new DAOTablaProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteProducto(producto);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void productoBorrarProductosPorTipo(String nombreTipo) throws Exception
	{
		DAOTablaProducto dao = new DAOTablaProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.borrarProductosPorTipo(nombreTipo);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public List<Producto> productoDarProductosPorTipo(String nombreTipo) throws Exception
	{
		List<Producto> list =null;
		DAOTablaProducto dao = new DAOTablaProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darProductosPorTipo(nombreTipo);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	//Reserva
	
	//Restaurante
	
	//Rol
	public List<Rol> rolDarRols() throws Exception
	{
		DAOTablaRol dao = new DAOTablaRol();
		List<Rol> list=null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darRols();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public Rol rolBuscarRolsPorName(String name) throws Exception
	{
		DAOTablaRol dao = new DAOTablaRol();
		Rol r =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			r=dao.buscarRolsPorName(name);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return r;
	}
	
	public void rolAddRol(Rol rol) throws Exception
	{
		DAOTablaRol dao = new DAOTablaRol();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addRol(rol);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void rolDeleteRol(Rol rol) throws Exception
	{
		DAOTablaRol dao = new DAOTablaRol();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteRol(rol);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	//TiposPlato
	public List<TiposDePlato> tiposDarTipos_De_Plato() throws Exception
	{
		DAOTablaTiposProducto dao = new DAOTablaTiposProducto();
		List<TiposDePlato> list =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darTipos_De_Platos();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public TiposDePlato tiposBuscarTipos_De_PlatosPorName(String name) throws Exception
	{
		DAOTablaTiposProducto dao = new DAOTablaTiposProducto();
		TiposDePlato tipo=null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			tipo=dao.buscarTipos_De_PlatosPorName(name);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return tipo;
		
	}
	
	public void tiposAddTipos_De_Plato(TiposDePlato tipo) throws Exception
	{
		DAOTablaTiposProducto dao = new DAOTablaTiposProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addTipos_De_Plato(tipo);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void tiposDeleteTipos_De_Plato(TiposDePlato tipo) throws Exception
	{
		DAOTablaTiposProducto dao = new DAOTablaTiposProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteTipos_De_Plato(tipo);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	//Usuario
	public List<Usuario> usuarioDarUsuarios() throws Exception
	{
		List<Usuario> list =null;
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darUsuarios();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public List<Usuario> usuarioBuscarUsuariosPorName(String nombre) throws Exception
	{
		List<Usuario> list =null;
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.buscarUsuariosPorName(nombre);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public List<Usuario> usuarioBuscarUsuarioPorRol(String nombreRol) throws Exception
	{
		List<Usuario> list=null;
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.buscarUsuarioPorRol(nombreRol);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public Usuario usuarioBuscarUsuarioPorId(Long id) throws Exception
	{
		Usuario u =null;
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			u=dao.buscarUsuarioPorId(id);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return u;
	}
	
	public void usuarioAddUsuario(Usuario usuario) throws Exception
	{
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addUsuario(usuario);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void usuarioUpdateUsuario(Usuario usuario) throws Exception
	{
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.updateUsuario(usuario);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void usuarioDeleteUsuario(Usuario usuario) throws Exception
	{
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteUsuario(usuario);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void usuarioBorrarPorRol(String nombreRol) throws Exception
	{
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.borrarPorRol(nombreRol);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	//Zona
	
	public List<Zona> zonaDarZonas() throws Exception
	{
		List<Zona> list =null;
		DAOTablaZona dao = new DAOTablaZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darZonas();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return list;
	}
	
	public Zona zonaBuscarZonasPorName(String name) throws Exception
	{
		Zona z =null;
		DAOTablaZona dao = new DAOTablaZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			z=dao.buscarZonasPorName(name);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return z;
	}
	
	public void zonaAddZona(Zona zona) throws Exception
	{
		DAOTablaZona dao = new DAOTablaZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addZona(zona);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void zonaUpdateZona(Zona zona) throws Exception
	{
		DAOTablaZona dao = new DAOTablaZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.updateZona(zona);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void zonaDeleteZona(Zona zona) throws Exception
	{
		DAOTablaZona dao = new DAOTablaZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteZona(zona);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				if(this.conn!=null) this.conn.close();
			}
			catch(SQLException exception)
			{
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	
	//VIDEOANDES-EJEMPLO
	/**
	 * Metodo que modela la transaccion que retorna todos los videos de la base de datos.
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public List<Video> darVideos() throws Exception {
		List<Video> videos;
		DAOTablaVideos daoVideos = new DAOTablaVideos();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoVideos.setConn(conn);
			videos = daoVideos.darVideos();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return videos;
	}

	/**
	 * Metodo que modela la transaccion que busca el/los videos en la base de datos con el nombre entra como parametro.
	 * @param name - Nombre del video a buscar. name != null
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public List<Video> buscarVideosPorName(String name) throws Exception {
		List<Video> videos;
		DAOTablaVideos daoVideos = new DAOTablaVideos();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoVideos.setConn(conn);
			videos = daoVideos.buscarVideosPorName(name);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return videos;
	}
	
	/**
	 * Metodo que modela la transaccion que busca el video en la base de datos con el id que entra como parametro.
	 * @param name - Id del video a buscar. name != null
	 * @return Video - Resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Video buscarVideoPorId(Long id) throws Exception {
		Video video;
		DAOTablaVideos daoVideos = new DAOTablaVideos();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoVideos.setConn(conn);
			video = daoVideos.buscarVideoPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return video;
	}
	
	/**
	 * Metodo que modela la transaccion que agrega un solo video a la base de datos.
	 * <b> post: </b> se ha agregado el video que entra como parametro
	 * @param video - el video a agregar. video != null
	 * @throws Exception - cualquier error que se genere agregando el video
	 */
	public void addVideo(Video video) throws Exception {
		DAOTablaVideos daoVideos = new DAOTablaVideos();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoVideos.setConn(conn);
			daoVideos.addVideo(video);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	/**
	 * Metodo que modela la transaccion que agrega los videos que entran como parametro a la base de datos.
	 * <b> post: </b> se han agregado los videos que entran como parametro
	 * @param videos - objeto que modela una lista de videos y se estos se pretenden agregar. videos != null
	 * @throws Exception - cualquier error que se genera agregando los videos
	 */
	public void addVideos(List<Video> videos) throws Exception {
		DAOTablaVideos daoVideos = new DAOTablaVideos();
		try 
		{
			//////transaccion - ACID Example
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoVideos.setConn(conn);
			Iterator<Video> it = videos.iterator();
			while(it.hasNext())
			{
				daoVideos.addVideo(it.next());
			}
			
			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	/**
	 * Metodo que modela la transaccion que actualiza el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha actualizado el video que entra como parametro
	 * @param video - Video a actualizar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */
	public void updateVideo(Video video) throws Exception {
		DAOTablaVideos daoVideos = new DAOTablaVideos();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoVideos.setConn(conn);
			daoVideos.updateVideo(video);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que elimina el video que entra como parametro a la base de datos.
	 * <b> post: </b> se ha eliminado el video que entra como parametro
	 * @param video - Video a eliminar. video != null
	 * @throws Exception - cualquier error que se genera actualizando los videos
	 */
	public void deleteVideo(Video video) throws Exception {
		DAOTablaVideos daoVideos = new DAOTablaVideos();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoVideos.setConn(conn);
			daoVideos.deleteVideo(video);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public static void main(String[] args) throws SQLException,Exception {
		RotondAndesTM tm = new RotondAndesTM("./WebContent/WEB-INF/ConnectionData");
		CondicionTecnica c1=new CondicionTecnica("E");
		CondicionTecnica c2 = new CondicionTecnica("FG");
		
		
		ArrayList<Ingrediente> ingrediente = new ArrayList<>();
		Ingrediente i1= new Ingrediente("a","b","c",0l);
		Ingrediente i2= new Ingrediente ("c","d","e",1l);
		ingrediente.add(i1);
		ingrediente.add(i2);
		//DAOTablaIngrediente ing = new DAOTablaIngrediente();
		//ing.setConn(tm.darConexion());
		//ing.addIngrediente(i1);
		//ing.addIngrediente(i2);
		//ing.cerrarRecursos();
		ArrayList<Categoria> categorias = new ArrayList<>();
		categorias.add(new Categoria("Mexicano"));
		categorias.add(new Categoria("Picante"));
		
		DAOTablaUsuario u = new DAOTablaUsuario();
		u.setConn(tm.darConexion());
		u.addUsuario(new Usuario("Sergio", 1l, "s.guzmanm", Rol.CLIENTE, new Preferencia(0.0,12.4,new ArrayList<Zona>(),categorias), new ArrayList<Cuenta>(), null));
		/*DAOTablaPreferencia pr= new DAOTablaPreferencia();
		pr.setConn(tm.darConexion());
		pr.addPreferencia(0l, new Preferencia(0.0,12.4,new ArrayList<Zona>(),categorias));
		pr.cerrarRecursos();
		DAOTablaCategoria c = new DAOTablaCategoria();
		c.setConn(tm.darConexion());
		c.addCategoria(new Categoria("Mexicano"));
		c.addCategoria(new Categoria("Picante"));
		c.cerrarRecursos();
		DAOTablaProducto p = new DAOTablaProducto();
		p.setConn(tm.darConexion());
		p.addProducto(new Producto(true, "ss", 12.4, Tipos_De_Plato.PLATO_FUERTE, "D", "e", 12.4, 10.0, new Long(0l), ingrediente, categorias));
		p.cerrarRecursos();
		tm.conn=null;*/		
		
	}

}
