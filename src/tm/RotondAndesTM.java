
package tm;


/*
A simple 2 phase XA demo. Both the branches talk to different RMS
Need 2 java enabled 8.1.6 databases to run this demo.
  -> start-1
  -> start-2
  -> Do some DML on 1
  -> Do some DML on 2
  -> end 1
  -> end 2
  -> prepare-1
  -> prepare-2
  -> commit-1
  -> commit-2
Please change the URL2 before running this.
*/

//You need to import the java.sql package to use JDBC
import java.sql.*;
import javax.sql.*;
import oracle.jdbc.driver.*;
import oracle.jdbc.pool.*;
import oracle.jdbc.xa.OracleXid;
import oracle.jdbc.xa.OracleXAException;
import oracle.jdbc.xa.client.*;
import javax.transaction.xa.*;

import java.io.File;




import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import dao.*;
import dtm.RotondAndesDistributed;
import jms.NonReplyException;
import rfc.ContenedoraClienteProductos;
import rfc.ContenedoraInformacion;
import rfc.ContenedoraRestauranteInfoFinanciera;
import rfc.ContenedoraZonaCategoriaProducto;
import rfc.Criterio;
import rfc.CriterioAgregacion;
import rfc.CriterioOrden;
import rfc.CriterioVerdad;
import rfc.CriterioVerdadHaving;
import rfc.LimiteFechas;
import rfc.ListaObjetos;
import rfc.PendientesOrden;
import rfc.UsuarioCompleto;
import test.PruebasJavaContraSQL;
import vos.UsuarioMinimum;
import vos.Video;
import vos.CondicionTecnica;
import vos.Cuenta;
import vos.CuentaMinimum;
import vos.InfoIngRest;
import vos.InfoProdRest;
import vos.Ingrediente;
import vos.ListaVideos;
import vos.Menu;
import vos.MenuMinimum;
import vos.Mesa;
import vos.PedidoMenu;
import vos.PedidoMenuConSustituciones;
import vos.PedidoProd;
import vos.PedidoProdConSustituciones;
import vos.Preferencia;
import vos.Producto;
import vos.Producto.TiposDePlato;
import vos.Reserva;
import vos.Restaurante;
import vos.RestauranteMinimum;
import vos.SustitucionIngrediente;
import vos.SustitucionIngredienteEnProducto;
import vos.SustitucionProducto;
import vos.Usuario;
import vos.UsuarioMinimum.Rol;
import vos.Zona;
import vos.ZonaMinimum;
import vos.Categoria;

/**
 * Transaction Manager de la aplicacion (TM)
 * Fachada en patron singleton de la aplicacion
 * @author s.guzmanm-js.diaz
 */
public class RotondAndesTM {
	/**
	 * Espaciado de los servicios rest
	 */
	public static final String SPACE="%20";
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
	
	private RotondAndesDistributed dtm;



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
		System.out.println("Instancing DTM...");
		dtm = RotondAndesDistributed.getInstance(this);
		System.out.println("Done!");
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
			conn.setAutoCommit(false);
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.deleteCategoria(c);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.deleteCondicionTecnica(c);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.insertarPorZona(nombreZona, condiciones);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.insertarPorCondicion(nombre, zonas);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.insertarPorCondicionYZona(nombreCondicion, nombreZona);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.eliminarCondicion(nombreZona, nombreCondicion);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);

			dao.setConn(conn);
			dao.eliminarCondicionesPorZona(nombreZona);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.eliminarCondicionesPorCondicion(condicionTecnica);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
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
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.addCuenta(c);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.updateCuenta(c);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	 * @param c Cuenta<br>
	 * @param revisarPedidos Si se debe o no revisar los pedidos enviados.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void cuentaCancelarPedido (Cuenta c, boolean revisarPedidos) throws Exception
	{
		DAOTablaCuenta dao = new DAOTablaCuenta();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.deleteCuenta(c,revisarPedidos);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.borrarHistorialCliente(id);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	//RF10
	/**
	 * Paga la cuenta con el número dado.<br>
	 * @param numeroCuenta Número de la cuenta a pagar<bR>
	 * @throws Exception Si existe algún tipo de error
	 */
	public PendientesOrden cuentaPagarCuenta(String numeroCuenta) throws Exception
	{
		DAOTablaCuenta dao = new DAOTablaCuenta();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			return dao.pagarCuenta(numeroCuenta);
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
	//Mesa
	/**
	 * Borra las cuentas de una mesa<br>
	 * @param Mesa cuyas cuentas se van a borrar.<br>
	 * @param Nombre del restaurante cuyas cuentas se van a borrar.<br>
	 * @throws Exception Si existe algún error
	 */
	public List<CuentaMinimum> mesaBorrarCuentasActivasPorMesa(Mesa mesa, String nombreRestaurante) throws Exception
	{	List<CuentaMinimum> list=new ArrayList<>();
	DAOTablaMesa dao = new DAOTablaMesa();
	try
	{
		this.conn=darConexion();
		conn.setAutoCommit(false);
		dao.setConn(conn);
		list=dao.borrarCuentasActivasPorRestaurante(mesa, nombreRestaurante);
		conn.commit();
	}
	catch (SQLException e) {
		System.err.println("SQLException:" + e.getMessage());
		e.printStackTrace();
		conn.rollback();
		throw e;
	} 
	catch (Exception e) {
		System.err.println("GeneralException:" + e.getMessage());
		e.printStackTrace();
		conn.rollback();
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
	 * Retorna una lista de mesas en el sistema.<br>
	 * @return Lista de mesas.<bR>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Mesa> mesaDarMesas() throws Exception
	{
		ArrayList<Mesa> list = null;
		DAOTablaMesa dao = new DAOTablaMesa();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darMesas();
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
	 * Busca un mesa por id.<br>
	 * @param id Id del mesa.<br>
	 * @return Mesa.<bR>
	 * @throws Exception Si existe algún tipo de error
	 */
	public Mesa mesaBuscarMesaPorId(Long id) throws Exception
	{
		Mesa i = null;
		DAOTablaMesa dao = new DAOTablaMesa();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			i=dao.buscarMesasPorId(id);
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
	 * Agrega una mesa al sistema.<br>
	 * @param mesa Mesa.<bR>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void mesaAddMesa(Mesa mesa) throws Exception
	{
		DAOTablaMesa dao = new DAOTablaMesa();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.addMesa(mesa);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	 * Actualiza un mesa<br>
	 * @param mesa Mesa.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void mesaUpdateMesa(Mesa mesa) throws Exception
	{
		DAOTablaMesa dao = new DAOTablaMesa();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.updateMesa(mesa);
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	 * Borra un mesa <br>
	 * @param mesa Mesa.<bR>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void mesaDeleteMesa(Mesa mesa) throws Exception
	{
		DAOTablaMesa dao = new DAOTablaMesa();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.deleteMesa(mesa);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	 * M�todo que ordena los pedidos de producto pedidos en la mesa dada.
	 * @param pedidos Pedidos a ordenar
	 * @param mesa Mesa que hace los pedidos
	 * @throws Exception Si algo falla en la ejecucci�n. El tal caso no se hace ning�n cambio a la BD.
	 */
	public void mesaRegistrarPedidosProducto(List<PedidoProd> pedidos, Mesa mesa) throws Exception {
		DAOTablaPedidoProducto dao = new DAOTablaPedidoProducto();
		try
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			for(PedidoProd pedido : pedidos) {
				if(!mesa.getCuentas().contains(pedido.getCuenta())) {
					conn.rollback();
					throw new Exception("no se puede pedir una cuenta que no venga de esta mesa.");
				}
				dao.addPedidoProd(pedido);
			}
			conn.commit();
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
	 * M�todo que ordena los pedidos de menu pedidos en la mesa dada.
	 * @param pedidos Pedidos a ordenar
	 * @param mesa Mesa que hace los pedidos
	 * @throws Exception Si algo falla en la ejecucci�n. El tal caso no se hace ning�n cambio a la BD.
	 */	
	public void mesaRegistrarPedidosMenu(List<PedidoMenu> pedidos, Mesa mesa) throws Exception{
		DAOTablaPedidoMenu dao = new DAOTablaPedidoMenu();
		try
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			for(PedidoMenu pedido : pedidos) {
				if(!mesa.getCuentas().contains(pedido.getCuenta())) {
					conn.rollback();
					throw new Exception("no se puede pedir una cuenta que no venga de esta mesa.");
				}
				dao.addPedidoMenu(pedido);
			}
			conn.commit();
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
	 * M�todo que registra el servicio de los pedidos de cierta mesa.
	 * @param mesa mesa de la cual se registran los pedidos.
	 * @throws Exception Si hay alg�n error en la operaci�n. El tal caso no se hace ning�n cambio a la BD.
	 */
	public void mesaRegistrarServicio(Mesa mesa) throws Exception{
		DAOTablaCuenta dao = new DAOTablaCuenta();
		try
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			for(CuentaMinimum cuenta : mesa.getCuentas()) {
				dao.pagarCuenta(cuenta.getNumeroCuenta());
			}
			conn.commit();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.deleteIngrediente(ingrediente);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.addPreferencia(idUsuario, p);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.actualizarPreferenciasDePrecioDeUsuario(idUsuario, p);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.deletePreferencia(idUsuario, p);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.insertarPreferenciasCategoria(idUsuario, categorias);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.borrarPorId(idUsuario);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.borrarPorCategoria(id);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.borrarMulticriterio(idUsuario, id);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.insertarPreferenciasZona(idUsuario, zonas);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.borrarPorId(idUsuario);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.modificarPorZonaEliminada(id);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.borrarMulticriterio(idUsuario, id);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.addProducto(producto);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.updateProducto(producto);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.deleteProducto(producto);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.borrarProductosPorTipo(nombreTipo);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
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

	/**
	 * Da los productos mas ofrecidos del sistema.<br>
	 * @return Productos en lista.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Producto> productoDarProductosMasOfrecidos() throws Exception
	{
		List<Producto> list =null;
		DAOTablaProducto dao = new DAOTablaProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darProductosMasOfrecidos();
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
	 * Da los productos mas vendidos del sistema.<br>
	 * @return Productos en lista.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Producto> productoDarProductosMasVendidos() throws Exception
	{
		List<Producto> list =null;
		DAOTablaProducto dao = new DAOTablaProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darProductosMasVendidos();
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
	 * Da los productos mas vendidos en una zona particular.<br>
	 * @param nombreZona nombre de la zona a revisar.
	 * @return Productos en lista.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Producto> productoDarProductosMasVendidosPorZona(String nombreZona) throws Exception
	{
		List<Producto> list =null;
		DAOTablaProducto dao = new DAOTablaProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darProductosMasVendidosPorZona(nombreZona);
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
	 * Da los productos mas vendidos en un dia particular.<br>
	 * @param dia nombre del dia a revisar.
	 * @return Productos en lista.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Producto> productoDarProductosMasYMenosVendidosPorDiaDeLaSemana(String dia) throws Exception
	{
		while(dia.length() < 9)
			dia += " ";
		List<Producto> list =null;
		DAOTablaProducto dao = new DAOTablaProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);			
			list=dao.darProductosMasYMenosVendidosPorDiaDeLaSemana(dia);
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.deleteRol(DAOTablaRol.buscarRol(rol));
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.deleteTipos_De_Plato(DAOTablaTiposProducto.convertirAPlato(tipo));
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	 * Busca al usuario minimum por id.<br>
	 * @param id Id del usuario.<br>
	 * @return Usuario buscado.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public UsuarioMinimum usuarioBuscarUsuarioMinimumPorId(Long usuarioId) throws SQLException, Exception{
		UsuarioMinimum u =null;
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			u=dao.buscarUsuarioMinimumPorId(usuarioId);
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
	 * Retorna una lista con la información contendia de los productos consumidos por el cliente.<br>
	 * @param id Id del cliente.<br>
	 * @return Lista con información.<br>
	 * @throws Exception SI hay errores.
	 */
	public List<ContenedoraClienteProductos> usuarioProuctosConsumidos(Long id) throws Exception
	{
		List<ContenedoraClienteProductos> list=null;
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.informacionProductos(id);
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
	 * Agrega un usuario al sistema.<br>
	 * @param usuario Usuario a agregar.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void usuarioAddUsuario(Usuario usuario) throws Exception
	{
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.addUsuario(usuario);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.updateUsuario(usuario);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.deleteUsuario(usuario);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.borrarPorRol(nombreRol);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	 * Da los buenos clientes del sistema.<br>
	 * @return Listado de usuarios.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Usuario> usuarioDarBuenosClientes() throws Exception
	{
		List<Usuario> list =null;
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darBuenosClientes();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.addZona(zona);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.updateZona(zona);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.deleteZona(zona);
			conn.commit();

		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	 * Obtiene una lista de contenedora de zona categoría y producto.<br>
	 * @param fechaInicial Fecha inicial de consulta.<br>
	 * @param fechaFinal Fecha final de consulta.<br>
	 * @param nombreRestaurante Nombre del restaurante.<br>
	 * @return Listado de contenedoras.<br>
	 * @throws Exception Si se ocasiona alguna excepción
	 */
	public List<ContenedoraZonaCategoriaProducto>zonaDarProductosTotalesPorZonaYCategoria(Date fechaInicial, Date fechaFinal, String nombreRestaurante) throws Exception
	{
		List<ContenedoraZonaCategoriaProducto> list =null;
		DAOTablaZona dao = new DAOTablaZona();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darProductosTotalesPorZonaYCategoria(fechaInicial, fechaFinal, nombreRestaurante);
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
	//CRITERIOS
	/**
	 * Metodo que modela la transaccion que retorna todos los videos de la base de datos.
	 * @return ListaVideos - objeto que modela  un arreglo de videos. este arreglo contiene el resultado de la busqueda
	 * @throws Exception Si existe algún tipo de error -  cualquier error que se genere durante la transaccion
	 */
	public List<ContenedoraInformacion> criteriosOrganizarPorZonasComoSeQuiera(String nombreZona,
			List<CriterioOrden> criteriosOrganizacion, List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregaciones,CriterioVerdad where, CriterioVerdadHaving having,boolean esProd) throws Exception {
		List<ContenedoraInformacion> informacion=null;
		DAOTablaCriterio dao=null;
		try 
		{
			dao=new DAOTablaCriterio();
			dao.setConn(darConexion());
			informacion = dao.generarListaFiltradaZonas(nombreZona,criteriosOrganizacion,criteriosAgrupamiento, agregaciones, where, having,esProd);

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
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return informacion;
	}

	/**
	 * Metodo que modela la transaccion que retorna productos organizados por ciertos criterios.
	 * @throws Exception Si existe algún tipo de error -  cualquier error que se genere durante la transaccion
	 */
	public List<ContenedoraInformacion> criteriosOrganizarPorProductoUniversal(String nombreProducto,
			List<CriterioOrden> criteriosOrganizacion, List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregaciones,CriterioVerdad where, CriterioVerdadHaving having) throws Exception {
		List<ContenedoraInformacion> informacion=null;
		DAOTablaCriterio dao=null;
		try 
		{
			//////transaccion
			dao=new DAOTablaCriterio();
			dao.setConn(darConexion());
			informacion = dao.generarListaFiltradaProductoEspecifica(nombreProducto,criteriosOrganizacion,criteriosAgrupamiento, agregaciones, where, having);

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
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return informacion;
	}

	/**
	 * Metodo que modela la transaccion que retorna productos organizados por ciertos criterios.
	 * @throws Exception Si existe algún tipo de error -  cualquier error que se genere durante la transaccion
	 */
	public List<ContenedoraInformacion> criteriosOrganizarPorProductosComoSeQuiera(
			List<CriterioOrden> criteriosOrganizacion, List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregaciones,CriterioVerdad where, CriterioVerdadHaving having) throws Exception {
		List<ContenedoraInformacion> informacion=null;
		DAOTablaCriterio dao=null;
		try 
		{
			dao=new DAOTablaCriterio();
			dao.setConn(darConexion());
			informacion = dao.generarListaFiltradaProductos(criteriosOrganizacion,criteriosAgrupamiento, agregaciones, where, having);

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
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return informacion;
	}

	/**
	 * Metodo que modela la transaccion que retorna productos organizados por ciertos criterios.
	 * @throws Exception Si existe algún tipo de error -  cualquier error que se genere durante la transaccion
	 */
	public List<Object> criteriosFiltrarClientesProductos(
			List<CriterioOrden> criteriosOrganizacion, List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregaciones,CriterioVerdad where, CriterioVerdadHaving having,LimiteFechas limite, String nombreRestaurante, Boolean es) throws Exception {
		List<Object> informacion=null;
		DAOTablaCriterio dao=null;
		try 
		{
			dao=new DAOTablaCriterio();
			dao.setConn(darConexion());
			informacion = dao.generarListaFiltradaUsuarioProductos(criteriosOrganizacion, criteriosAgrupamiento, agregaciones, where, having, nombreRestaurante, es, limite);

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
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return informacion;
	}

	/**
	 * Metodo que modela la transaccion que retorna productos organizados por ciertos criterios.
	 * @throws Exception Si existe algún tipo de error -  cualquier error que se genere durante la transaccion
	 */
	public List<Object> criteriosFiltrarClientesMenus(
			List<CriterioOrden> criteriosOrganizacion, List<Criterio> criteriosAgrupamiento,
			List<CriterioAgregacion> agregaciones,CriterioVerdad where, CriterioVerdadHaving having,LimiteFechas limite, String nombreRestaurante, Boolean es) throws Exception {
		List<Object> informacion=null;
		DAOTablaCriterio dao=null;
		try 
		{
			dao=new DAOTablaCriterio();
			dao.setConn(darConexion());
			informacion = dao.generarListaFiltradaUsuarioMenus(criteriosOrganizacion, criteriosAgrupamiento, agregaciones, where, having, nombreRestaurante, es, limite);

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
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return informacion;
	}

	//Restaurante
	/**
	 * Retorna las restaurantes del sistema.<br>
	 * @return Restaurantes.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Restaurante> restauranteDarRestaurantes() throws Exception
	{
		List<Restaurante> list =null;
		DAOTablaRestaurante dao = new DAOTablaRestaurante();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darRestaurantes();
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
	 * Busca una restaurante por nombre.<br>
	 * @param name Nombre de restaurante.<br>
	 * @return La restaurante.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public Restaurante restauranteBuscarRestaurantePorNombre(String name) throws Exception
	{
		Restaurante z =null;
		DAOTablaRestaurante dao = new DAOTablaRestaurante();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			z=dao.darRestaurantePorNombre(name);
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
	 * Agrega una restaurante.<br>
	 * @param restaurante Restaurante.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void restauranteAddRestaurante(Restaurante restaurante, Usuario usuario) throws Exception
	{
		DAOTablaRestaurante dao = new DAOTablaRestaurante();
		DAOTablaUsuario daoU= new DAOTablaUsuario();
		for(Categoria c: restaurante.getCategorias())
		{
			if(categoriaBuscarCategoriasPorName(c.getNombre())==null) 
				throw new Exception("Hay categorias del restaurante que no existen en el sistema con "+c.getNombre());
		}
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);

			usuario.setRestaurante(new RestauranteMinimum(restaurante.getNombre(), restaurante.getPagWeb(),true));
			daoU.setConn(conn);
			usuario.setRol(Rol.LOCAL);
			daoU.addUsuario(usuario);

			dao.setConn(conn);
			UsuarioMinimum rep=usuario;
			restaurante.setRepresentante(rep);
			dao.addRestaurante(restaurante);
			conn.commit();



		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			try
			{
				daoU.cerrarRecursos();
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
	 * Actualiza una restaurante.<br>
	 * @param restaurante Restaurante.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void restauranteUpdateRestaurante(Restaurante restaurante) throws Exception
	{
		DAOTablaRestaurante dao = new DAOTablaRestaurante();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.updateRestaurante(restaurante);
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
	 * Borra una restaurante.<br>
	 * @param restaurante Restaurante.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void restauranteDeleteRestaurante(String restaurante) throws Exception
	{
		DAOTablaRestaurante dao = new DAOTablaRestaurante();
		DAOTablaUsuario daoU= new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			Restaurante r=dao.darRestaurantePorNombre(restaurante);			
			daoU.setConn(conn);
			daoU.deleteUsuario(new Usuario(r.getRepresentante().getNombre(), r.getRepresentante().getId(), r.getRepresentante().getCorreo(), r.getRepresentante().getRol(), null, null, null));

			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				daoU.cerrarRecursos();
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
	 * Registra que un restaurante desea surtir sus productos.<br>
	 * @param nombre Nombre del restaurante.<br>
	 * @throws Exception Si sucede algún error.
	 */
	public void restauranteSurtirProductos(String nombre) throws Exception {
		DAOTablaRestaurante dao= new DAOTablaRestaurante();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.surtirRestaurante(nombre);
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
	 * Retorna una lista de contenedora de información para el restaurante a nivel financiero.<br>
	 * @param restaurante Nombre del restaurante.<br>
	 * @param esProd Si es producto o no.<br>
	 * @return Lista de contenedoras de información.<br>
	 * @throws Exception Si hay errores.
	 */
	public List<ContenedoraRestauranteInfoFinanciera> restauranteDarInformacionFinanciera(String restaurante, boolean esProd) throws Exception
	{
		DAOTablaRestaurante dao= new DAOTablaRestaurante();
		List<ContenedoraRestauranteInfoFinanciera> list=new ArrayList<>();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.balanceFinanciero(restaurante, esProd);
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
	 * Da los restaurantes mas visitados en un dia particular.<br>
	 * @param dia nombre del dia a revisar.
	 * @return Restaurantes en lista.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Restaurante> restauranteDarRestaurantesMasYMenosVisitadosPorDiaDeLaSemana(String dia) throws Exception
	{
		while(dia.length() < 9)
			dia += " ";
		List<Restaurante> list =null;
		DAOTablaRestaurante dao = new DAOTablaRestaurante();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darRestaurantesMasYMenosFrecuentadosPorDiaDeLaSemana(dia);
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
	
	//Menu
	/**
	 * Retorna las menus del sistema.<br>
	 * @return Menus.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Menu> menuDarMenusPorRestaurante(String restaurante) throws Exception
	{
		List<Menu> list =null;
		DAOTablaMenu dao = new DAOTablaMenu();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darMenusPorRestaurante(restaurante);
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
	 * Busca una menu por nombre.<br>
	 * @param name Nombre de menu.<br>
	 * @param restaurante nombre del restaurante al cual pertenece.
	 * @return La menu.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public Menu menuBuscarMenusPorNombreYRestaurante(String name, String restaurante) throws Exception
	{
		Menu z =null;
		DAOTablaMenu dao = new DAOTablaMenu();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			z=dao.buscarMenusPorNombreYRestaurante(name, restaurante);
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
	 * Agrega una menu.<br>
	 * @param menu Menu.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void menuAddMenu(Menu menu) throws Exception
	{
		DAOTablaMenu dao = new DAOTablaMenu();
		for(Categoria c: menu.getCategorias())
		{
			if(categoriaBuscarCategoriasPorName(c.getNombre())==null) 
				throw new Exception("Hay categorias del menu que no existen en el sistema con "+c.getNombre());
		}
		menuVerficarTiposDeProducto(menu);
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addMenu(menu);
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

	private void menuVerficarTiposDeProducto(Menu menu) throws Exception {
		for(InfoProdRest plato : menu.getPlatos())
			for(InfoProdRest otroPlato : menu.getPlatos())
				if(plato != otroPlato && plato.getProducto().getTipo() == otroPlato.getProducto().getTipo())
					throw new Exception("El menu tiene dos platos del mismo tipo");
	}
	/**
	 * Actualiza una menu.<br>
	 * @param menu Menu.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void menuUpdateMenu(Menu menu) throws Exception
	{
		menuVerficarTiposDeProducto(menu);
		DAOTablaMenu dao = new DAOTablaMenu();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.updateMenu(menu);
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
	 * Borra una menu.<br>
	 * @param menu Menu.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void menuDeleteMenu(String nombre, String nombreRestaurante) throws Exception
	{
		DAOTablaMenu dao = new DAOTablaMenu();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteMenu(dao.buscarMenusPorNombreYRestaurante(nombre, nombreRestaurante));
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

	//InfoProdRest
	/**
	 * Retorna las infoProdRests del sistema.<br>
	 * @return InfoProdRests.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<InfoProdRest> infoProdRestDarInfoProdRestsPorRestaurante(String restaurante) throws Exception
	{
		List<InfoProdRest> list =null;
		DAOTablaInfoProdRest dao = new DAOTablaInfoProdRest();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darInfoProdRestsPorRestaurante(restaurante);
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
	 * Busca una infoProdRest por nombre.<br>
	 * @param id id del producto.<br>
	 * @param restaurante Nombre del restaurante.<br>
	 * @return La infoProdRest.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public InfoProdRest infoProdRestBuscarInfoProdRestsPorIdYRestaurante(Long id, String restaurante) throws Exception
	{
		InfoProdRest z =null;
		DAOTablaInfoProdRest dao = new DAOTablaInfoProdRest();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			z=dao.buscarInfoProdRestsPorIdYRestaurante(id, restaurante);
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
	 * Agrega una infoProdRest.<br>
	 * @param infoProdRest InfoProdRest.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void infoProdRestAddInfoProdRest(InfoProdRest infoProdRest) throws Exception
	{
		DAOTablaInfoProdRest dao = new DAOTablaInfoProdRest();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addInfoProdRest(infoProdRest);
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
	 * Actualiza una infoProdRest.<br>
	 * @param infoProdRest InfoProdRest.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void infoProdRestUpdateInfoProdRest(InfoProdRest infoProdRest) throws Exception
	{
		DAOTablaInfoProdRest dao = new DAOTablaInfoProdRest();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.updateInfoProdRest(infoProdRest);
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
	 * Borra una infoProdRest.<br>
	 * @param infoProdRest InfoProdRest.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void infoProdRestDeleteInfoProdRest(Long idProducto, String nombreRestaurante) throws Exception
	{
		DAOTablaInfoProdRest dao = new DAOTablaInfoProdRest();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteInfoProdRest(dao.buscarInfoProdRestsPorIdYRestaurante(idProducto, nombreRestaurante));
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

	//InfoIngRest	
	/**
	 * Retorna las infoIngRests del sistema.<br>
	 * @return InfoIngRests.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<InfoIngRest> infoIngRestDarInfoIngRestsPorRestaurante(String restaurante) throws Exception
	{
		List<InfoIngRest> list =null;
		DAOTablaInfoIngRest dao = new DAOTablaInfoIngRest();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darInfoIngRestsPorRestaurante(restaurante);
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
	 * Busca una infoIngRest por nombre.<br>
	 * @param id Id del ingrediente.<br>
	 * @param restaurante Nombre del restaurante.<br>
	 * @return La infoIngRest.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public InfoIngRest infoIngRestBuscarInfoIngRestsPorIdYRestaurante(Long id, String restaurante) throws Exception
	{
		InfoIngRest z =null;
		DAOTablaInfoIngRest dao = new DAOTablaInfoIngRest();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			z=dao.buscarInfoIngRestsPorIdYRestaurante(id, restaurante);
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
	 * Agrega una infoIngRest.<br>
	 * @param infoIngRest InfoIngRest.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void infoIngRestAddInfoIngRest(InfoIngRest infoIngRest) throws Exception
	{
		DAOTablaInfoIngRest dao = new DAOTablaInfoIngRest();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addInfoIngRest(infoIngRest);
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
	 * Actualiza una infoIngRest.<br>
	 * @param infoIngRest InfoIngRest.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void infoIngRestUpdateInfoIngRest(InfoIngRest infoIngRest) throws Exception
	{
		DAOTablaInfoIngRest dao = new DAOTablaInfoIngRest();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.updateInfoIngRest(infoIngRest);
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
	 * Borra una infoIngRest.<br>
	 * @param infoIngRest InfoIngRest.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void infoIngRestDeleteInfoIngRest(Long idIngucto, String nombreRestaurante) throws Exception
	{
		DAOTablaInfoIngRest dao = new DAOTablaInfoIngRest();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteInfoIngRest(dao.buscarInfoIngRestsPorIdYRestaurante(idIngucto, nombreRestaurante));
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

	///PerteneceAMenu
	/**
	 * Retorna un listado de menus para un producto de infoProdRest.<br>
	 * @param producto Id del producto.<br>
	 * @param restaurante Restaurante al cual pertenece.<br>
	 * @return Lista de menus.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<MenuMinimum> perteneceAMenuBuscarMenusPorProducto(Long producto, String restaurante) throws Exception
	{
		DAOTablaPerteneceAMenu dao = new DAOTablaPerteneceAMenu();
		List<MenuMinimum> list=null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list = dao.consultarPorProducto(producto, restaurante);
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
	 * Retorna infoProdRests por una menu dada.<br>
	 * @param menu Nombre del menu.<br>
	 * @param restaurante Nombre del restaurante.<br>
	 * @return Listado de infoProdRests.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<InfoProdRest> perteneceAMenuBuscarProductosPorMenu(String menu, String restaurante) throws Exception
	{
		DAOTablaPerteneceAMenu dao = new DAOTablaPerteneceAMenu();
		List<InfoProdRest> list =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list = dao.consultarPorMenu(menu, restaurante);
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
	 * Borra infoProdRests de menu por id.<br>
	 * @param producto id del producto.<br>
	 * @param restaurante nombre del restaurante. <br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void perteneceAMenuBorrarPorProducto(Long producto, String restaurante) throws Exception
	{
		DAOTablaPerteneceAMenu dao = new DAOTablaPerteneceAMenu();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.eliminarPorProducto(producto, restaurante);
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
	 * Modifica las infoProdRests si se elimina una menu.<br>
	 * @param nombreMenu nombre del menu.<br>
	 * @param nombreRestaurante nombre del restaurante.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void perteneceAMenuBorrarPorMenu(String nombreMenu, String nombreRestaurante) throws Exception
	{
		DAOTablaPerteneceAMenu dao = new DAOTablaPerteneceAMenu();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.eliminarPorMenu(nombreMenu, nombreRestaurante);;
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
	 * @param idProducto id del producto.<br>
	 * @param nombreMenu nombre del menu.<br>
	 * @param nombreRestaurante nombre del restaurante.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void perteneceAMenuBorrarMulticriterio(Long idProducto, String nombreMenu, String nombreRestaurante) throws Exception
	{
		DAOTablaPerteneceAMenu dao = new DAOTablaPerteneceAMenu();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.desasociarProductoYMenu(nombreMenu, nombreRestaurante, idProducto);;
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

	///PerteneceAPlato
	/**
	 * Retorna un listado de ingredientes para un producto de producto.<br>
	 * @param producto Id del producto.<br>
	 * @return Lista de ingredientes.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Ingrediente> perteneceAPlatoBuscarIngredientesPorProducto(Long producto) throws Exception
	{
		DAOTablaPerteneceAProducto dao = new DAOTablaPerteneceAProducto();
		List<Ingrediente> list=null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list = dao.consultarPorProducto(producto);
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
	 * Retorna productos por una ingrediente dada.<br>
	 * @param ingrediente Id del ingrediente.<br>
	 * @return Listado de productos.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Producto> perteneceAPlatoBuscarProductosPorIngrediente(Long ingrediente) throws Exception
	{
		DAOTablaPerteneceAProducto dao = new DAOTablaPerteneceAProducto();
		List<Producto> list =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list = dao.consultarPorIngrediente(ingrediente);
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
	 * Borra productos de ingrediente por id.<br>
	 * @param producto id del producto.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void perteneceAPlatoBorrarPorProducto(Long producto) throws Exception
	{
		DAOTablaPerteneceAProducto dao = new DAOTablaPerteneceAProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.eliminarPorProducto(producto);
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
	 * Modifica las productos si se elimina una ingrediente.<br>
	 * @param ingrediente id del ingrediente.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void perteneceAPlatoBorrarPorIngrediente(Long ingrediente) throws Exception
	{
		DAOTablaPerteneceAProducto dao = new DAOTablaPerteneceAProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.eliminarPorIngrediente(ingrediente);
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
	 * @param idProducto id del producto.<br>
	 * @param idIngrediente id del ingrediente.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void perteneceAPlatoBorrarMulticriterio(Long idProducto, Long idIngrediente) throws Exception
	{
		DAOTablaPerteneceAProducto dao = new DAOTablaPerteneceAProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.desasociarIngredienteYProducto(idIngrediente, idProducto);
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

	///CategoriaProducto
	/**
	 * Retorna un listado de categorias para un producto de producto.<br>
	 * @param producto Id del producto.<br>
	 * @return Lista de categorias.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Categoria> categoriaProductoBuscarCategoriasPorProducto(Long producto) throws Exception
	{
		DAOTablaCategoriaProducto dao = new DAOTablaCategoriaProducto();
		List<Categoria> list=null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list = dao.consultarPorProducto(producto);
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
	 * Retorna productos por una categoria dada.<br>
	 * @param categoria Nombre de la categoria.<br>
	 * @return Listado de productos.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Producto> categoriaProductoBuscarProductosPorCategoria(String categoria) throws Exception
	{
		DAOTablaCategoriaProducto dao = new DAOTablaCategoriaProducto();
		List<Producto> list =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list = dao.consultarPorCategoria(categoria);
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
	 * Borra productos de categoria por id.<br>
	 * @param producto id del producto.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void categoriaProductoBorrarPorProducto(Long producto) throws Exception
	{
		DAOTablaCategoriaProducto dao = new DAOTablaCategoriaProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.eliminarPorProducto(producto);
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
	 * Modifica las productos si se elimina una categoria.<br>
	 * @param categoria nombre del categoria.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void categoriaProductoBorrarPorCategoria(String categoria) throws Exception
	{
		DAOTablaCategoriaProducto dao = new DAOTablaCategoriaProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.eliminarPorCategoria(categoria);
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
	 * @param producto id del producto.<br>
	 * @param categoria nombre de la categoria.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void categoriaProductoBorrarMulticriterio(Long producto, String categoria) throws Exception
	{
		DAOTablaCategoriaProducto dao = new DAOTablaCategoriaProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.desasociarCategoriaYProducto(producto, categoria);
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

	///CategoriaRestaurante
	/**
	 * Retorna un listado de categorias para un restaurante de restaurante.<br>
	 * @param restaurante nombre del restaurante.<br>
	 * @return Lista de categorias.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Categoria> categoriaRestauranteBuscarCategoriasPorRestaurante(String restaurante) throws Exception
	{
		DAOTablaCategoriaRestaurante dao = new DAOTablaCategoriaRestaurante();
		List<Categoria> list=null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list = dao.consultarPorRestaurante(restaurante);
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
	 * Retorna restaurantes por una categoria dada.<br>
	 * @param categoria nombre de la categoria.<br>
	 * @return Listado de restaurantes.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Restaurante> categoriaRestauranteBuscarRestaurantesPorCategoria(String categoria) throws Exception
	{
		DAOTablaCategoriaRestaurante dao = new DAOTablaCategoriaRestaurante();
		List<Restaurante> list =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list = dao.consultarPorCategoria(categoria);
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
	 * Borra restaurantes de categoria por id.<br>
	 * @param restaurante nombre del restaurante.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void categoriaRestauranteBorrarPorRestaurante(String restaurante) throws Exception
	{
		DAOTablaCategoriaRestaurante dao = new DAOTablaCategoriaRestaurante();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.eliminarPorRestaurante(restaurante);
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
	 * Modifica las restaurantes si se elimina una categoria.<br>
	 * @param categoria nombre de la categoria.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void categoriaRestauranteBorrarPorCategoria(String categoria) throws Exception
	{
		DAOTablaCategoriaRestaurante dao = new DAOTablaCategoriaRestaurante();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.eliminarPorCategoria(categoria);
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
	 * @param restaurante nombre del restaurante.<br>
	 * @param categoria nombre del categoria.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void categoriaRestauranteBorrarMulticriterio(String restaurante, String categoria) throws Exception
	{
		DAOTablaCategoriaRestaurante dao = new DAOTablaCategoriaRestaurante();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.desasociarCategoriaYRestaurante(categoria, restaurante);
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

	///CategoriaMenu
	/**
	 * Retorna un listado de menus para un categoria de categoria.<br>
	 * @param categoria nombre del categoria.<br>
	 * @return Lista de menus.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<MenuMinimum> categoriaMenuBuscarMenusPorCategoria(String categoria) throws Exception
	{
		DAOTablaCategoriaMenu dao = new DAOTablaCategoriaMenu();
		List<MenuMinimum> list=null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list = dao.consultarPorCategoria(categoria);
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
	 * Retorna categorias por una menu dada.<br>
	 * @param menu Nombre del menu.<br>
	 * @param restaurante Nombre del restaurante.<br>
	 * @return Listado de categorias.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Categoria> categoriaMenuBuscarCategoriasPorMenu(String menu, String restaurante) throws Exception
	{
		DAOTablaCategoriaMenu dao = new DAOTablaCategoriaMenu();
		List<Categoria> list =null;
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list = dao.consultarPorMenu(menu, restaurante);
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
	 * Borra categorias de menu por id.<br>
	 * @param categoria nombre del categoria.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void categoriaMenuBorrarPorCategoria(String categoria) throws Exception
	{
		DAOTablaCategoriaMenu dao = new DAOTablaCategoriaMenu();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.eliminarPorCategoria(categoria);
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
	 * Modifica las categorias si se elimina una menu.<br>
	 * @param nombreMenu nombre del menu.<br>
	 * @param nombreRestaurante nombre del restaurante.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void categoriaMenuBorrarPorMenu(String nombreMenu, String nombreRestaurante) throws Exception
	{
		DAOTablaCategoriaMenu dao = new DAOTablaCategoriaMenu();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.eliminarPorMenu(nombreMenu, nombreRestaurante);;
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
	 * @param idCategoria id del categoria.<br>
	 * @param nombreMenu nombre del menu.<br>
	 * @param nombreRestaurante nombre del restaurante.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void categoriaMenuBorrarMulticriterio(String categoria, String nombreMenu, String nombreRestaurante) throws Exception
	{
		DAOTablaCategoriaMenu dao = new DAOTablaCategoriaMenu();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.desasociarCategoriaYMenu(categoria, nombreMenu, nombreRestaurante);
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

	//Reserva
	/**
	 * Retorna las reservas del sistema.<br>
	 * @return Reservas.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<Reserva> reservaDarReservasPorUsuario(Long usuario) throws Exception
	{
		List<Reserva> list =null;
		DAOTablaReserva dao = new DAOTablaReserva();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darReservasPorUsuario(usuario);
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
	 * Busca una reserva por usuario y fecha.<br>
	 * @param fecha fecha de la reserva.<br>
	 * @param reservador Id del usuario que hace la reserva.<br>
	 * @return La reserva.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public Reserva reservaBuscarReservasPorFechaYUsuario(Date fecha, Long reservador) throws Exception
	{
		Reserva z =null;
		DAOTablaReserva dao = new DAOTablaReserva();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			z = dao.buscarReservasPorFechaYUsuario(fecha, reservador);
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
	 * Agrega una reserva.<br>
	 * @param reserva Reserva.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void reservaAddReserva(Reserva reserva) throws Exception
	{
		DAOTablaReserva dao = new DAOTablaReserva();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.addReserva(reserva);
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
	 * Actualiza una reserva.<br>
	 * @param reserva Reserva.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void reservaUpdateReserva(Reserva reserva) throws Exception
	{
		DAOTablaReserva dao = new DAOTablaReserva();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.updateReserva(reserva);
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
	 * Borra una reserva.<br>
	 * @param reserva Reserva.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void reservaDeleteReserva(Date fecha, Long idUsuario) throws Exception
	{
		DAOTablaReserva dao = new DAOTablaReserva();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			dao.deleteReserva(dao.buscarReservasPorFechaYUsuario(fecha, idUsuario));
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

	//PedidoProd
	/**
	 * Retorna las pedidoProds del sistema.<br>
	 * @return PedidoProds.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<PedidoProd> pedidoProdDarPedidoProdsPorCuenta(String cuenta) throws Exception
	{
		List<PedidoProd> list =null;
		DAOTablaPedidoProducto dao = new DAOTablaPedidoProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darPedidoProdsPorCuenta(cuenta);
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
	 * Busca una pedidoProd por nombre.<br>
	 * @param id id del infoProdRest.<br>
	 * @param cuenta Nombre del cuenta.<br>
	 * @param restaurante Nombre del restaurante.<br>
	 * @return La pedidoProd.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public PedidoProd pedidoProdBuscarPedidoProdsPorIdYCuenta(Long id, String cuenta, String restaurante) throws Exception
	{
		PedidoProd z =null;
		DAOTablaPedidoProducto dao = new DAOTablaPedidoProducto();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			z=dao.buscarPedidoProdsPorIdYCuenta(id, restaurante, cuenta);
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
	 * Agrega una pedidoProd.<br>
	 * @param pedidoProd PedidoProd.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void pedidoProdAddPedidoProd(PedidoProd pedidoProd) throws Exception
	{
		DAOTablaPedidoProducto dao = new DAOTablaPedidoProducto();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.addPedidoProd(pedidoProd);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	 * Agrega una pedidoProd con equivalencias.<br>
	 * @param pedidoProd PedidoProd.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void pedidoProdAddPedidoProdConEquivalencias(PedidoProdConSustituciones pedidoProd) throws Exception
	{
		DAOTablaPedidoProducto dao = new DAOTablaPedidoProducto();
		DAOTablaSustitucionIngrediente daoSustIng = new DAOTablaSustitucionIngrediente();
		DAOTablaInfoIngRest daoIng = new DAOTablaInfoIngRest();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.addPedidoProd(pedidoProd);
			PedidoProd infoPedido = dao.buscarPedidoProdsPorIdYCuenta(pedidoProd.getPlato().getProducto().getId(), pedidoProd.getPlato().getRestaurante().getNombre(), pedidoProd.getCuenta().getNumeroCuenta());
			daoSustIng.setConn(conn);
			daoIng.setConn(conn);
			for(SustitucionIngrediente s : pedidoProd.getSustituciones()) {
				InfoIngRest original = daoIng.buscarInfoIngRestsPorIdYRestaurante(s.getOriginal().getId(), pedidoProd.getPlato().getRestaurante().getNombre());
				if(!infoPedido.getPlato().getProducto().getIngredientes().contains(s.getOriginal()) || !original.getSustitutos().contains(s.getSustituto())) {
					conn.rollback();
					throw new Exception("La sustituci�n pedida no es v�lida.");
				}
				daoSustIng.addSustitucionIngrediente(s, pedidoProd);
			}
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				daoSustIng.cerrarRecursos();
				daoIng.cerrarRecursos();
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
	 * Actualiza una pedidoProd.<br>
	 * @param pedidoProd PedidoProd.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void pedidoProdUpdatePedidoProd(PedidoProd pedidoProd) throws Exception
	{
		DAOTablaPedidoProducto dao = new DAOTablaPedidoProducto();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.updatePedidoProd(pedidoProd);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	 * Borra una pedidoProd.<br>
	 * @param pedidoProd PedidoProd.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void pedidoProdCancelarPedidoProd(String numeroCuenta, Long id, String restaurante) throws Exception
	{
		DAOTablaPedidoProducto dao = new DAOTablaPedidoProducto();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.deletePedidoProd(dao.buscarPedidoProdsPorIdYCuenta(id, restaurante, numeroCuenta));
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	/**
	 * Retorna las pedidoMenus del sistema.<br>
	 * @return PedidoMenus.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public List<PedidoMenu> pedidoMenuDarPedidoMenusPorCuenta(String cuenta) throws Exception
	{
		List<PedidoMenu> list =null;
		DAOTablaPedidoMenu dao = new DAOTablaPedidoMenu();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.darPedidoMenusPorCuenta(cuenta);
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
	 * Busca una pedidoMenu por nombre.<br>
	 * @param menu Nombre del menu.<br>
	 * @param cuenta Nombre del cuenta.<br>
	 * @param restaurante Nombre del restaurante.<br>
	 * @return La pedidoMenu.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public PedidoMenu pedidoMenuBuscarPedidoMenusPorIdYCuenta(String menu, String cuenta, String restaurante) throws Exception
	{
		PedidoMenu z =null;
		DAOTablaPedidoMenu dao = new DAOTablaPedidoMenu();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			z=dao.buscarPedidoMenusPorNombreYCuenta(menu, restaurante, cuenta);
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
	 * Agrega una pedidoMenu.<br>
	 * @param pedidoMenu PedidoMenu.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void pedidoMenuAddPedidoMenu(PedidoMenu pedidoMenu) throws Exception
	{
		DAOTablaPedidoMenu dao = new DAOTablaPedidoMenu();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.addPedidoMenu(pedidoMenu);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	 * Agrega una pedidoMenu con equivalencias.<br>
	 * @param pedidoMenu PedidoMenu.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void pedidoMenuAddPedidoMenuConEquivalencias(PedidoMenuConSustituciones pedidoMenu) throws Exception
	{
		DAOTablaPedidoMenu dao = new DAOTablaPedidoMenu();
		DAOTablaMenu daoMenu = new DAOTablaMenu();
		DAOTablaSustitucionProducto daoSusProducto = new DAOTablaSustitucionProducto();
		DAOTablaSustitucionIngredienteEnProducto daoSusIngrediente = new DAOTablaSustitucionIngredienteEnProducto();
		DAOTablaInfoProdRest daoProducto = new DAOTablaInfoProdRest();
		DAOTablaInfoIngRest daoIngrediente = new DAOTablaInfoIngRest();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.addPedidoMenu(pedidoMenu);

			daoMenu.setConn(conn);
			Menu menu = daoMenu.buscarMenusPorNombreYRestaurante(pedidoMenu.getMenu().getNombre(), pedidoMenu.getMenu().getRestaurante().getNombre());

			daoSusProducto.setConn(conn);
			daoProducto.setConn(conn);
			for(SustitucionProducto s : pedidoMenu.getSustitucionesProducto()) {
				InfoProdRest original = daoProducto.buscarInfoProdRestsPorIdYRestaurante(s.getOriginal().getId(), menu.getRestaurante().getNombre());
				if(!menu.getPlatos().contains(original) || !original.getSustitutos().contains(s.getSustituto())) {
					conn.rollback();
					throw new Exception("La sustituci�n pedida no es v�lida.");
				}
				daoSusProducto.addSustitucionProducto(s, pedidoMenu);
			}

			daoSusIngrediente.setConn(conn);
			daoIngrediente.setConn(conn);
			for(SustitucionIngredienteEnProducto s : pedidoMenu.getSustitucionesIngrediente()) {
				InfoProdRest producto = daoProducto.buscarInfoProdRestsPorIdYRestaurante(s.getProducto().getId(), menu.getRestaurante().getNombre());
				InfoIngRest original = daoIngrediente.buscarInfoIngRestsPorIdYRestaurante(s.getOriginal().getId(), pedidoMenu.getMenu().getRestaurante().getNombre());
				if(!menu.getPlatos().contains(producto) || !producto.getProducto().getIngredientes().contains(s.getOriginal()) || !original.getSustitutos().contains(s.getSustituto()))
					throw new Exception("La sustituci�n pedida no es v�lida.");
				daoSusIngrediente.addSustitucionIngredienteEnProducto(s, pedidoMenu);

			}

			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
		finally
		{
			try
			{
				dao.cerrarRecursos();
				daoMenu.cerrarRecursos();
				daoProducto.cerrarRecursos();
				daoIngrediente.cerrarRecursos();
				daoSusProducto.cerrarRecursos();
				daoSusIngrediente.cerrarRecursos();
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
	 * Actualiza una pedidoMenu.<br>
	 * @param pedidoMenu PedidoMenu.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void pedidoMenuUpdatePedidoMenu(PedidoMenu pedidoMenu) throws Exception
	{
		DAOTablaPedidoMenu dao = new DAOTablaPedidoMenu();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.updatePedidoMenu(pedidoMenu);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	 * Borra una pedidoMenu.<br>
	 * @param pedidoMenu PedidoMenu.<br>
	 * @throws Exception Si existe algún tipo de error
	 */
	public void cancelarPedidoMenu(String numeroCuenta, String nombre, String restaurante) throws Exception
	{
		DAOTablaPedidoMenu dao = new DAOTablaPedidoMenu();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.cancelarPedidoMenu(dao.buscarPedidoMenusPorNombreYCuenta(nombre, restaurante, numeroCuenta));
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	//Ejemplo de métodos distribuidos
	//---------------------------
	//ITERACIÓN 5
	//---------------------------
	//RF18
	//Llama al método de pedir por nombre usando mesa
	public void registrarPedidoProdMesa(List<String> mensaje,String mesa, String correo) throws Exception{
		//Llama al método local
		List<String> list=pedidoProdMesaNombre(mensaje,mesa,correo);
		//Llama al método global
		String msj = correo + ";" + mesa + ";" + list.size();
		for(String s:list)
		{
			msj+= ";" + s;
		}
		try
		{
			dtm.pedidoProdMesaGlobal(msj);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void registrarPedidoMenuMesa(List<String> mensaje, String mesa,String correo) throws Exception{
		//Llama al método local
		List<String> list=pedidoMenuMesaNombre(mensaje,mesa,correo);
		String msj = correo + ";" + mesa + ";" + list.size();
		for(String s:list)
		{
			msj += ";" + s;
		}
		try
		{
			dtm.pedidoMenuMesaGlobal(msj);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//RF19
	//Llama al método de retirar restaurante de la rotonda
	public void retirarRestaurante(String nombreRestaurante) throws Exception{
		try
		{
			//Restaurante a retirar de forma local
			retirarRestauranteLocal(nombreRestaurante);
			//Restaurante a retirar de forma global
			dtm.retirarRestauranteGlobal(nombreRestaurante);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	//RFC13
	//Llama al método RFC1
	public List<Object> consultarProductos(String inicial, String terminal, String nombreRestaurante, String catProd, String precioMin, String precioMax) throws Exception
	{
		ArrayList<CriterioOrden> criteriosOrganizacion=new ArrayList<>();
		ArrayList<Criterio> criteriosAgrupamiento=new ArrayList<Criterio>();
		CriterioVerdad temp=null;
		CriterioVerdad where=new CriterioVerdad(new Criterio("PRECIO"),0+"",null,">",true,null,null,null,null);
		if(inicial!=null)
		{
			if (inicial.length()==0)
			{
				criteriosOrganizacion.add(new CriterioOrden(null, "FECHA_INICIO", true));
			}
			else 
			{
				criteriosOrganizacion=new ArrayList<>();
				criteriosAgrupamiento.add(new Criterio("FECHA_INICIO"));
				temp = new CriterioVerdad(new Criterio("FECHA_INICIO"),dateFormat(inicial),null,">=",true,null,null,null,null);
				where=new CriterioVerdad(null,null,null,null,true,null,where,temp,true);
			}
		}
		if(terminal!=null)
		{
			if (terminal.length()==0)
			{
				criteriosOrganizacion.add(new CriterioOrden(null, "FECHA_FIN", true));
			}
			else 
			{
				criteriosOrganizacion=new ArrayList<>();
				criteriosAgrupamiento.add(new Criterio("FECHA_FIN"));
				temp = new CriterioVerdad(new Criterio("FECHA_FIN"),dateFormat(terminal),null,"<=",true,null,null,null,null);
				where=new CriterioVerdad(null,null,null,null,true,null,where,temp,true);
			}
		}
		if(nombreRestaurante!=null)
		{
			if (nombreRestaurante.length()==0)
			{
				criteriosOrganizacion.add(new CriterioOrden(null, "NOMBRE_RESTAURANTE", true));
			}
			else 
			{
				criteriosOrganizacion=new ArrayList<>();
				criteriosAgrupamiento.add(new Criterio("NOMBRE_RESTAURANTE"));
				temp = new CriterioVerdad(new Criterio("NOMBRE_RESTAURANTE"),"'"+nombreRestaurante+"'",null,"=",true,null,null,null,null);
				where=new CriterioVerdad(null,null,null,null,true,null,where,temp,true);
			}
		}
		if(catProd!=null)
		{
			if (catProd.length()==0)
			{
				criteriosOrganizacion.add(new CriterioOrden(null, "TIPO", true));
			}
			else 
			{
				criteriosOrganizacion=new ArrayList<>();
				criteriosAgrupamiento.add(new Criterio("TIPO"));
				temp = new CriterioVerdad(new Criterio("TIPO"),"'"+catProd+"'",null,"=",true,null,null,null,null);
				where=new CriterioVerdad(null,null,null,null,true,null,where,temp,true);
			}
		}
		if(precioMin!=null)
		{
			if (precioMin.length()==0)
			{
				criteriosOrganizacion.add(new CriterioOrden(null, "PRECIO", true));
			}
			else
			{
				criteriosAgrupamiento.add(new Criterio("PRECIO"));
				temp = new CriterioVerdad(new Criterio("PRECIO"),precioMin+"",null,">=",true,null,null,null,null);
				where=new CriterioVerdad(null,null,null,null,true,null,where,temp,true);
			}
		}
		if(precioMax!=null)
		{
			if (precioMax.length()==0)
			{
				criteriosOrganizacion.add(new CriterioOrden(null, "PRECIO", true));
			}
			else
			{
				criteriosOrganizacion=new ArrayList<>();
				criteriosAgrupamiento.add(new Criterio("PRECIO"));
				temp = new CriterioVerdad(new Criterio("PRECIO"),precioMax+"",null,"<=",true,null,null,null,null);
				where=new CriterioVerdad(null,null,null,null,true,null,where,temp,true);
			}
		}
		//Método local. Falta parsearlo a como lo tienen ramos y mauricio
		List<Object> list=new ArrayList<>();
		list.add(criteriosOrganizarPorProductosComoSeQuiera(criteriosOrganizacion, criteriosAgrupamiento, null, where, null));
		//Método global que mandaría el json de mauricio y ramos
		try
		{
			if(catProd!=null && catProd.length()>0)
			{
				String s=catProd.substring(1, catProd.length());
				catProd=catProd.charAt(0)+s.toLowerCase();
			}
			List<Object>other=dtm.consultarProductos(inicial+";"+terminal+
					";"+nombreRestaurante+";"+catProd+";"+precioMin+";"+precioMax);
			//Agregar los archivos a la lista local
			list.addAll(other);
		}
		catch(Exception e)
		{
			
		}
		list.add(new UsuarioMinimum("A",1l,"A",Rol.CLIENTE));
		list.add(TiposDePlato.ACOMPANAMIENTO);
		return list;
	}
	
	public String dateFormat(String fecha) throws Exception
	{
		SimpleDateFormat o = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		o.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
		
		SimpleDateFormat x = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		x.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
		return "TO_DATE('"+ x.format(o.parse(fecha)) + "', 'yyyy-MM-dd hh24:mi:ss')";

	}
	
	//RFC14
	//Llama al método RFC5
	public List<Object> consultarRentabilidadZona(String fechaInicial, String fechaFinal, String nombreRestaurante) throws Exception
	{
		SimpleDateFormat x = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		List<Object> rta=new ArrayList<>();
		//Método local. Falta parsealo a como lo tienen ramos y mauricio
		System.out.println(fechaInicial +" "+fechaFinal);
		rta.add(zonaDarProductosTotalesPorZonaYCategoria(x.parse(fechaInicial), x.parse(fechaFinal), nombreRestaurante));
		//Método global que mandaría el json de mauricio y ramos
		try
		{
			//Falta parsearlo con el json de ramos y mauricio
			List<Object>other=dtm.consultarRentabilidadZonaGlobal(fechaInicial+";"+fechaFinal+";"+nombreRestaurante);
			rta.addAll(other);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return rta;
	}

	public void retirarRestauranteLocal(String nombreRestaurante)throws Exception {
		DAOTablaRestaurante dao = new DAOTablaRestaurante();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.sacarRestaurante(nombreRestaurante);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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

	public void reingresarRestaurante(String name) throws Exception {
		DAOTablaRestaurante dao = new DAOTablaRestaurante();
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			dao.restituirRestaurante(name);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
	
	public List<String> pedidoProdMesaNombre(List<String> mensaje,String mesa,String correo) throws Exception
	{
		DAOTablaCuenta c=new DAOTablaCuenta();
		List<String>list = new ArrayList<>();
		DAOTablaPedidoProducto dao = new DAOTablaPedidoProducto();
		try
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			c.setConn(conn);
			String num=c.addCuentaPorCorreo(mesa, correo);
			System.out.println("LA CUENTA ES "+num);
			c.cerrarRecursos();
			dao.setConn(conn);
			String[] datos=null;
			
			for(String s:mensaje)
			{
				datos=s.split(",");
				try
				{
					dao.addPedidoProdPorNombre(num, datos[0], datos[1]);
				}
				catch(Exception e)
				{
					list.add(s);
				}
				
			}
			conn.commit();
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
	
	public List<String> pedidoMenuMesaNombre(List<String> mensaje, String mesa, String correo) throws Exception
	{
		DAOTablaCuenta c= new DAOTablaCuenta();
		DAOTablaPedidoMenu dao = new DAOTablaPedidoMenu();
		List<String> list = new ArrayList<>();
		try
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			c.setConn(conn);
			String num=c.addCuentaPorCorreo(mesa, correo);
			System.out.println("NUMERO DE CUENTA "+num);
			c.cerrarRecursos();
			dao.setConn(conn);
			String[] datos=null;
			for(String s:mensaje)
			{
				try
				{
					datos=s.split(",");
					dao.addPedidoMenuPorNombre( datos[0], datos[1],num);
				}
				catch(Exception e)
				{
					list.add(s);
				}
			}
			conn.commit();
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

	public String cuentaCrearCuentaUsuario(String mesa,String correo) throws Exception {
		DAOTablaCuenta dao = new DAOTablaCuenta();
		String rta=null;
		try
		{
			this.conn=darConexion();
			conn.setAutoCommit(false);
			dao.setConn(conn);
			rta=dao.addCuentaPorCorreo(mesa,correo);
			conn.commit();
		}
		catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} 
		catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
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
		return rta;
	}

	public UsuarioMinimum usuarioBuscarUsuarioMinimumPorCorreo(String correo) throws Exception {
		UsuarioMinimum list =null;
		DAOTablaUsuario dao = new DAOTablaUsuario();
		try
		{
			this.conn=darConexion();
			dao.setConn(conn);
			list=dao.buscarUsuarioMinimumPorCorreo(correo);
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
	
	public Object dtmConsultarProductos(String mensaje) throws Exception
	{
		Object list = new ArrayList<>();
		try
		{
			list=dtm.consultarProductosLocal(mensaje);
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
	
	public Object dtmConsultarRentabilidad(String mensaje) throws Exception
	{
		Object list = new ArrayList<>();
		try
		{
			list=dtm.consultarRentabilidadZonaLocal(mensaje);
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
	
	public String dtmRetirarRestaurante(String mensaje) throws Exception
	{
		String list="";
		try
		{
			list=dtm.retirarRestauranteLocal(mensaje.split(";")[0]);
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
	
	public List<String> dtmRegistrarPedidoProd(String mensaje) throws Exception
	{
		List<String> list = new ArrayList<>();
		try
		{
			list=dtm.pedidoProdMesaLocal(mensaje);
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
	
	public List<String> dtmRegistrarPedidoMenu(String mensaje) throws Exception
	{
		List<String> list = new ArrayList<>();
		try
		{
			list=dtm.pedidoMenuMesaLocal(mensaje);
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
	
	

	public void twoPhaseCommit(String name) throws Exception {
		
		try
	    {
			File arch = new File(this.connectionDataPath);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(arch);
			prop.load(in);
			in.close();
			String user2 = prop.getProperty("usuario2");
			String password2 = prop.getProperty("clave2");
			String user3=prop.getProperty("usuario3");
			String password3=prop.getProperty("clave3");
			String sql1=prop.getProperty("sql1").replaceAll("nombreRestaurante", name);
			String sql2=prop.getProperty("sql2").replaceAll("nombreRestaurante", name);
			String sql3=prop.getProperty("sql3").replaceAll("nombreRestaurante", name);

	       	        // Create a XADataSource instance
	        OracleXADataSource oxds1 = new OracleXADataSource();
	        oxds1.setURL(url);
	        oxds1.setUser(user);
	        oxds1.setPassword(password);
	        
	        System.out.println(oxds1.getURL()+";;;"+oxds1.getUser());


	        OracleXADataSource oxds2 = new OracleXADataSource();

	        oxds2.setURL(url);
	        oxds2.setUser(user2);
	        oxds2.setPassword(password2);
	        
	        System.out.println(oxds2.getURL()+";;;"+oxds2.getUser());
	        //Hasta tener la tercera conexión
	        
	        OracleXADataSource oxds3 = new OracleXADataSource();

	        oxds3.setURL(url);
	        oxds3.setUser(user3);
	        oxds3.setPassword(password3);
	        
	        System.out.println(oxds3.getURL()+";;;"+oxds3.getUser());
	        
	    
	        // Get a XA connection to the underlying data source
	        XAConnection pc1  = oxds1.getXAConnection();

	        // We can use the same data source 
	        XAConnection pc2  = oxds2.getXAConnection();
	        

	        // Get the Physical Connections
	        Connection conn1 = pc1.getConnection();
	        Connection conn2 = pc2.getConnection();

	        // Get the XA Resources
	        XAResource oxar1 = pc1.getXAResource();
	        XAResource oxar2 = pc2.getXAResource();
	        
	        // Create the Xids With the Same Global Ids
	        Xid xid1 = createXid(1);
	        Xid xid2 = createXid(2);
	        // Start the Resources
	        oxar1.start (xid1, XAResource.TMNOFLAGS);
	        oxar2.start (xid2, XAResource.TMNOFLAGS);
	        //RESOURCE 3
	        XAConnection pc3=oxds3.getXAConnection();
	        Connection conn3=pc3.getConnection();
	        XAResource oxar3=pc3.getXAResource();
	        Xid xid3=createXid(3);
	        oxar3.start (xid3, XAResource.TMNOFLAGS);

	        // Do  something with conn1 and conn2
	        System.out.println(1);
	        doSomeWork (conn1,sql1);
	        System.out.println(2);
	        doSomeWork (conn2,sql2);
	        System.out.println(3);
	        doSomeWork(conn3,sql3);

	        // END both the branches -- THIS IS MUST
	        oxar1.end(xid1, XAResource.TMSUCCESS);
	        oxar2.end(xid2, XAResource.TMSUCCESS);
	        oxar3.end(xid3,XAResource.TMSUCCESS);
	        // Prepare the RMs
	        int prp1 =  oxar1.prepare (xid1);
	        int prp2 =  oxar2.prepare (xid2);
	        int prp3=oxar3.prepare(xid3);
	        System.out.println("Return value of prepare 1 is " + prp1);
	        System.out.println("Return value of prepare 2 is " + prp2);
	        System.out.println("Return value of prepare 3 is " + prp3);

	        boolean do_commit = true;

	        if (!((prp1 == XAResource.XA_OK) || (prp1 == XAResource.XA_RDONLY)))
	           do_commit = false;

	        if (!((prp2 == XAResource.XA_OK) || (prp2 == XAResource.XA_RDONLY)))
	           do_commit = false;
	        
	        if (!((prp3 == XAResource.XA_OK) || (prp3 == XAResource.XA_RDONLY)))
		           do_commit = false;

	       System.out.println("do_commit is " + do_commit);
	        System.out.println("Is oxar1 same as oxar2 ? " + oxar1.isSameRM(oxar2));
	        System.out.println("Is oxar1 same as oxar3 ? " + oxar1.isSameRM(oxar3));
	        System.out.println("Is oxar2 same as oxar3 ? " + oxar2.isSameRM(oxar3));

	        if (prp1 == XAResource.XA_OK)
	          if (do_commit)
	             oxar1.commit (xid1, false);
	          else
	             oxar1.rollback (xid1);

	        if (prp2 == XAResource.XA_OK)
	          if (do_commit)
	             oxar2.commit (xid2, false);
	          else
	             oxar2.rollback (xid2);
	       
	        if (prp3 == XAResource.XA_OK)
		          if (do_commit)
		             oxar3.commit (xid3, false);
		          else
		             oxar3.rollback (xid3);
		             
	         // Close connections
	        conn1.close();
	        conn1 = null;
	        conn2.close();
	        conn2 = null;
	        
	        conn3.close();
	        conn3 = null;
	        
	        pc1.close();
	        pc1 = null;
	        pc2.close();
	        pc2 = null;
	        pc3.close();
	        pc3 = null;

	  
	    } catch (SQLException sqe)
	    {
	      sqe.printStackTrace();
	    } catch (XAException xae)
	    {
	      if (xae instanceof OracleXAException) {
	        System.out.println("XA Error is " +
	                      ((OracleXAException)xae).getXAError());
	        System.out.println("SQL Error is " +
	                      ((OracleXAException)xae).getOracleError());
	      }
	    }
	}
	
	   Xid createXid(int bids)
			    throws XAException
			  {
			    byte[] gid = new byte[1]; gid[0]= (byte) 9;
			    byte[] bid = new byte[1]; bid[0]= (byte) bids;
			    byte[] gtrid = new byte[64];
			    byte[] bqual = new byte[64];
			    System.arraycopy (gid, 0, gtrid, 0, 1);
			    System.arraycopy (bid, 0, bqual, 0, 1);
			    Xid xid = new OracleXid(0x1234, gtrid, bqual);
			    return xid;
			  }

			  private  void doSomeWork (Connection conn, String sql)
			   throws SQLException
			  {
				  System.out.println(sql);
			    // Create a Statement
			    PreparedStatement stmt = conn.prepareStatement(sql);

			    ResultSet rs=stmt.executeQuery();
			    System.out.println(sql);
			    stmt.close();
			    stmt = null;
			  }

			  private  void doSomeWork2 (Connection conn, String sql)
			    throws SQLException
			  {
			    // Create a Statement
				    Statement stmt = conn.createStatement ();

				    ResultSet rs=stmt.executeQuery ("select * from restaurantes where nombre='El Corral'");

				    if(rs.next())
				    System.out.println(rs.getString("NOMBRE"));
				    else
				    	System.out.println("El restaurante no existe");
				    stmt.close();
				    stmt = null;
			  }

}
