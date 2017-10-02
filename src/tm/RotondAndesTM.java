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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import dao.*;
import vos.CondicionTecnica;
import vos.ContenedoraCriterios;
import vos.ContenedoraInformacion;
import vos.Criterio;
import vos.Criterio.Agregaciones;
import vos.CriterioAgregacion;
import vos.CriterioOrden;
import vos.CriterioVerdad;
import vos.CriterioVerdadHaving;
import vos.Cuenta;
import vos.CuentaMinimum;
import vos.Ingrediente;
import vos.Preferencia;
import vos.Producto;
import vos.Producto.TiposDePlato;
import vos.Restaurante;
import vos.Usuario;
import vos.UsuarioCompleto;
import vos.UsuarioMinimum.Rol;
import vos.Video;
import vos.Zona;
import vos.ZonaMinimum;
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
	 * @throws Exception Si existe algún tipo de error Si llega a pasar algo.
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
	/**
	 * Busca la categoría por nombre.<br>
	 * @param name Nombre de categoría.<br>
	 * @return Categoría.<br>
	 * @throws Exception Si existe algún tipo de error Si sucede algún error.
	 */
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
	/**
	 * Añade una categoría.<br>
	 * @param c Categoría a agregar.<br>
	 * @throws Exception Si existe algún tipo de error Si ahay algún error.
	 */
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
	/**
	 * Borra una categoría.<br>
	 * @param c Categoría a borrar.<br>
	 * @throws Exception Si existe algún tipo de error Si hay algún error.
	 */
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
	/**
	 * Retorna todas las condiciones del sistema.<br>
	 * @return Condiciones en lista.<br>
	 * @throws Exception Si existe algún tipo de error Si hay algún error.
	 */
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
	/**
	 * Busca la condición por nombre.<br>
	 * @param name Nombre.<br>
	 * @return La condición con el nombre.<br>
	 * @throws Exception Si existe algún tipo de error Si hay algún error.
	 */
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
	/**
	 * Añade una condición.<br>
	 * @param c COndición
	 * @throws Exception Si existe algún tipo de error Si hay algún error.
	 */
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
	/**
	 * Borra una condición del sistema.<br>
	 * @param c COndición.<br>
	 * @throws Exception Si existe algún tipo de error Si hay algún error.
	 */
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
	/**
	 * Consulta condiciones por zona.<br>
	 * @param nombreZona Nombre de la zona.<br>
	 * @return Lista de condiciones.<br>
	 * @throws Exception Si existe algún tipo de error Si hay algún error.
	 */
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
	/**
	 * Consulta las zonas que cumplen con la condición.<br>
	 * @param nombreCondicion Nombre de la condición.<br>
	 * @return Zonas con la condición.<br>
	 * @throws Exception Si existe algún tipo de error Si hay algún error.
	 */
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
	/**
	 * Inserta condiciones por el nombre de la zona.<br>
	 * @param nombreZona Nombre de la zona.<br>
	 * @param condiciones Listado de condiciones.<br>
	 * @throws Exception Si existe algún tipo de error Si hay algún error.
	 */
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
	/**
	 * Inserta zonas por el nombre de condición.<br>
	 * @param nombre Nombre de la condición.<br>
	 * @param zonas Zonas a insertar.<br>
	 * @throws Exception Si existe algún tipo de error Si hay errores.
	 */
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
	/**
	 * Insertar por condición y zona.<br>
	 * @param nombreCondicion Nombre de la condición.<br>
	 * @param nombreZona Nombre de la zona.<br>
	 * @throws Exception Si existe algún tipo de error Si hay algún error.
	 */
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
	/**
	 * Elimina la relación de una zona con una condición específica.<br>
	 * @param nombreZona Nombre de la zona.<br>
	 * @param nombreCondicion Nombre de la condición.<br>
	 * @throws Exception Si existe algún tipo de error Si hay errores.
	 */
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
	/**
	 * Elimina todas las condiciones por zona.<br>
	 * @param nombreZona Nombre de zona.<br>
	 * @throws Exception Si existe algún tipo de error SI hay errores.
	 */
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
	/**
	 * Elimina condiciones por nombre de la condición técnica.<br>
	 * @param condicionTecnica Nombre de condición.<br>
	 * @throws Exception Si existe algún tipo de error Si hay errores.
	 */
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
	/**
	 * Retorna todas las cuentas del sistema.<br>
	 * @return Cuentas en lista.<br>
	 * @throws Exception Si existe algún tipo de error Si hay algún error
	 */
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
	/**
	 * Busca una cuenta por su número.<br>
	 * @param numeroCuenta Número de cuenta.<br>
	 * @return Cuenta con el número.<br>
	 * @throws Exception Si existe algún tipo de error Si hay algún error
	 */
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
	/**
	 * Busca cuentas por un id de usuario<br>
	 * @param id Id de usuario.<br>
	 * @return Lista de cuentas.<br>
	 * @throws Exception Si existe algún tipo de error Si hay errores
	 */
	public List<CuentaMinimum> cuentaBuscarCuentasPorId(Long id) throws Exception
	{
		DAOTablaCuenta dao = new DAOTablaCuenta();
		List<CuentaMinimum> list =null;
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
	/**
	 * Agrega una cuetna.<br>
	 * @param c Cuenta.<br>
	 * @throws Exception Si existe algún tipo de error Si hay errores
	 */
	public void cuentaAddCuenta(Cuenta c) throws Exception
	{
		DAOTablaCuenta dao = new DAOTablaCuenta();
		//TODO Checho: Se deben verificar los pedidos de menús y productos.
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
	/**
	 * Actualiza una cuenta.<br>
	 * @param c Cuenta
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Borra una cuenta.<br>
	 * @param c Cuenta<bR>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Borra el historial de un cliente.<br>
	 * @param id Id usuario<bR>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Retorna una lista de ingredientes en el sistema.<br>
	 * @return Lista de ingredientes.<bR>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Busca un ingrediente por id.<br>
	 * @param id Id del ingrediente.<br>
	 * @return Ingrediente.<bR>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Agrega una ingrediente al sistema.<br>
	 * @param ingrediente Ingrediente.<bR>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Actualiza un ingrediente<br>
	 * @param ingrediente Ingrediente.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Borra un ingrediente <br>
	 * @param ingrediente Ingrediente.<bR>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Retorna preferencias del sistema.<br>
	 * @return Lista de preferencias.<bR>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Busca preferencias por id de cliente.<br>
	 * @param id Id del cliente.<br>
	 * @return Preferencia de ese cliente.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Busca preferencias en un rango dado.<br>
	 * @param ini Precio inicial.<br>
	 * @param fin Precio final.<br>
	 * @return Preferencias en lista.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Agrega una preferencia a un usuario.<br>
	 * @param idUsuario Id del usuario.<br>
	 * @param p Preferencia.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void preferenciaAddPreferencia(Long idUsuario, Preferencia p) throws Exception
	{
		DAOTablaPreferencia dao = new DAOTablaPreferencia();
		if(p.getCategorias()!=null)
		for(Categoria c: p.getCategorias())
		{
			if(categoriaBuscarCategoriasPorName(c.getNombre())==null)
				throw new Exception("La categoría del usuario no existe "+c.getNombre());
		}
		if(p.getZonaMinimums()!=null)
		for(ZonaMinimum z: p.getZonaMinimums())
		{
			if(zonaBuscarZonasPorName(z.getNombre())==null)
				throw new Exception("La zona del usuario no existe "+z.getNombre());
		}
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
	/**
	 * Actualiza una preferencia de un usuario a nivel de precio.<br>
	 * @param idUsuario Id del usuario.<br>
	 * @param p Preferencia.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Borra la preferencia del usuario.<br>
	 * @param idUsuario Id del usuario.<br>
	 * @param p Preferencia.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Busca las preferencias por categoría de un usuario.<br>
	 * @param id Id del usuario.<br>
	 * @return Lista de categorías que prefiere.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Categoria> preferenciaCategoriaBuscarCategoriasPorId(Long id) throws Exception
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
	/**
	 * Busca los usuarios que prefieren una categoría.<br>
	 * @param cat Categoría.<br>
	 * @return Listado de preferencias correspondientes.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Inserta preferenciasCategoría por idUsuario.<br>
	 * @param idUsuario Id de usuario.<br>
	 * @param categorias Listado de categorías.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Borrar preferenciasCategoría por id.<br>
	 * @param idUsuario Id de usuario.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Borra preferenciasCategoría por id de categorías.<br>
	 * @param id Nombre de categoría.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Borra usando los dos criterios de la tabla.<br>
	 * @param idUsuario Id de usuario.<br>
	 * @param id Id de categoría /nombre.<br>
	 * @throws Exception Si existe algún tipo de error 
	 */
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
	/**
	 * Retorna un listado de zonas para el id de preferencia.<br>
	 * @param id Id del usuario.<br>
	 * @return Lista de zonas.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<ZonaMinimum> preferenciaZonaBuscarZonasPorId(Long id) throws Exception
	{
		DAOTablaPreferenciaZona dao = new DAOTablaPreferenciaZona();
		List<ZonaMinimum> list=null;
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
	/**
	 * Retorna preferencias por una zona dada.<br>
	 * @param cat Nombre de la zona.<br>
	 * @return Listado de preferencias.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Preferencia> preferenciaZonaBuscarZonaPorZona(String cat) throws Exception
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
	/**
	 * Inserta preferencias de zona por id de usuario.<br>
	 * @param idUsuario Id del usuario.<br>
	 * @param zonas Listado de zonas.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void preferenciaZonaInsertarPreferenciasZona(Long idUsuario, List<ZonaMinimum> zonas) throws Exception
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
	/**
	 * Borra preferencias de zona por id.<br>
	 * @param idUsuario Id del usuario.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Modifica las preferencias si se elimina una zona.<br>
	 * @param id Id del usuario.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Borra usando los dos criterios de la tabla.<br>
	 * @param idUsuario Id del usuario.<br>
	 * @param id Id o nombre de la zona.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Da los productos del sistema.<br>
	 * @return Productos en lista.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Busca los productos que posean el nombre dado.<br>
	 * @param name Nombre.<br>
	 * @return Listado de productos.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Busca productos por id dado.<br>
	 * @param id Id del producto.<br>
	 * @return Producto con el id.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Agrega el producto.<br>
	 * @param producto Producto.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void productoAddProducto(Producto producto) throws Exception
	{
		DAOTablaProducto dao = new DAOTablaProducto();
		// TODO C1: Cuando Sebastián añada su parte debo verificar que los ingredientes del prducto existan.
		for(Categoria c: producto.getCategorias())
		{
			if(categoriaBuscarCategoriasPorName(c.getNombre())==null)
				throw new Exception("La categoría buscada no existe "+c.getNombre());
		}
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
	/**
	 * Actualiza el producto.<br>
	 * @param producto Producto.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Borra el producto.<br>
	 * @param producto Producto.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void productoDeleteProducto(Producto producto) throws Exception
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
	/**
	 * Borra productos por tipo.<br>
	 * @param nombreTipo Nombre del tipo.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Da productos por tipo.<br>
	 * @param nombreTipo Nombre del tipo.<br>
	 * @return Listado de productos.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Da los roles en el sistema.<br>
	 * @return Litado de roles.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Busca un rol por nombre.<br>
	 * @param name Nombre
	 * @return Rol por nombre.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Agrega un rol al sistema.<br>
	 * @param rol Rol
	 * @throws Exception Si existe algún tipo de error
	 */
	public void rolAddRol(String rol) throws Exception
	{
		DAOTablaRol dao = new DAOTablaRol();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addRol(DAOTablaRol.buscarRol(rol));
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
	/**
	 * Borra un rol del sistema.<br>
	 * @param rol Rol
	 * @throws Exception Si existe algún tipo de error
	 */
	public void rolDeleteRol(String rol) throws Exception
	{
		DAOTablaRol dao = new DAOTablaRol();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteRol(DAOTablaRol.buscarRol(rol));
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
	/**
	 * Da los tipos de plato en el sistema.<br>
	 * @return Listado de tipos.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Busca el tipo por nombre.<br>
	 * @param name Nombre<br>
	 * @return Tipo dado.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Agrega un nuevo tipo al sistema.<br>
	 * @param tipo TIpo dado.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void tiposAddTipos_De_Plato(String tipo) throws Exception
	{
		DAOTablaTiposProducto dao = new DAOTablaTiposProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addTipos_De_Plato(DAOTablaTiposProducto.convertirAPlato(tipo));
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
	/**
	 * Borra un tipo del sistema.<br>
	 * @param tipo Tipo.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void tiposDeleteTipos_De_Plato(String tipo) throws Exception
	{
		DAOTablaTiposProducto dao = new DAOTablaTiposProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteTipos_De_Plato(DAOTablaTiposProducto.convertirAPlato(tipo));
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
	/**
	 * Da los usuarios del sistema.<br>
	 * @return Listado de usuarios.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Busca usuarios por nombre.<br>
	 * @param nombre Nombre de usuario.<br>
	 * @return Listado de usuarios.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Busca usuarios por rol.<br>
	 * @param nombreRol Nombre del rol.<br>
	 * @return Listado de usuarios.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Busca al usuario por id.<br>
	 * @param id Id del usuario.<br>
	 * @return Usuario buscado.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Busca al usuario por id.<br>
	 * @param id Id del usuario.<br>
	 * @return Usuario buscado.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public UsuarioCompleto usuarioCompletoBuscarUsuarioPorId(Long id) throws Exception
	{
		UsuarioCompleto u =null;
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			u=dao.darTodaLaInfoDeUnCliente(id);
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
	/**
	 * Agrega un usuario al sistema.<br>
	 * @param usuario Usuario a agregar.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void usuarioAddUsuario(Usuario usuario) throws Exception
	{
		DAOTablaUsuario dao = new DAOTablaUsuario();
		Preferencia p = usuario.getPreferencia();
		if(p!=null)
		{
			for(Categoria c: p.getCategorias())
			{
				if(categoriaBuscarCategoriasPorName(c.getNombre())==null)
					throw new Exception("La categoría del usuario no existe "+c.getNombre());
			}
			for(ZonaMinimum z: p.getZonaMinimums())
			{
				if(zonaBuscarZonasPorName(z.getNombre())==null)
					throw new Exception("La zona del usuario no existe "+z.getNombre());
			}
		}
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
	/**
	 * Actualiza al usuario.<br>
	 * @param usuario usuario.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Borra al usuario del sistema.<br>
	 * @param usuario Usuario.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Borra a los usuarios con un rol dado.<br>
	 * @param nombreRol Nombre del rol.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Retorna las zonas del sistema.<br>
	 * @return Zonas.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Busca una zona por nombre.<br>
	 * @param name Nombre de zona.<br>
	 * @return La zona.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Agrega una zona.<br>
	 * @param zona Zona.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void zonaAddZona(Zona zona) throws Exception
	{
		DAOTablaZona dao = new DAOTablaZona();
		for(CondicionTecnica c: zona.getCondiciones())
		{
			if(condicionBuscarCondicionTecnicasPorName(c.getNombre())==null) 
				throw new Exception("Hay condiciones de la zona que no existen en el sistema con "+c.getNombre());
		}
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
	/**
	 * Actualiza una zona.<br>
	 * @param zona Zona.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	/**
	 * Borra una zona.<br>
	 * @param zona Zona.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
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
	//CRITERIOS
	/**
	 * Metodo que modela la transaccion que retorna todos los videos de la base de datos.
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception Si existe algún tipo de error -  cualquier error que se genere durante la transaccion
	 */
	public List<ContenedoraInformacion> criteriosOrganizarPorZonaUniversal(String nombreZona,
			List<CriterioOrden> criteriosOrganizacion, List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregaciones,CriterioVerdad where, CriterioVerdadHaving having) throws Exception {
		List<ContenedoraInformacion> videos=null;
		DAOTablaCriterio daoVideos=null;
		try 
		{
			//////transaccion
			daoVideos=new DAOTablaCriterio();
			daoVideos.setConn(darConexion());
			videos = daoVideos.generarListaFiltradaZonas(nombreZona,criteriosOrganizacion,criteriosAgrupamiento, agregaciones, where, having);

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
		


	//VIDEOANDES-EJEMPLO
	/**
	 * Metodo que modela la transaccion que retorna todos los videos de la base de datos.
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception Si existe algún tipo de error -  cualquier error que se genere durante la transaccion
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
	 * @throws Exception Si existe algún tipo de error -  cualquier error que se genere durante la transaccion
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
	 * @throws Exception Si existe algún tipo de error -  cualquier error que se genere durante la transaccion
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
	 * @throws Exception Si existe algún tipo de error - cualquier error que se genere agregando el video
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
	 * @throws Exception Si existe algún tipo de error - cualquier error que se genera agregando los videos
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
	 * @throws Exception Si existe algún tipo de error - cualquier error que se genera actualizando los videos
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
	 * @throws Exception Si existe algún tipo de error - cualquier error que se genera actualizando los videos
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
		
	}

}
