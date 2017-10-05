
package rest;


import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import rfc.PendientesOrden;
import tm.RotondAndesTM;
import vos.Cuenta;
import vos.PedidoMenu;
import vos.PedidoProd;
import vos.Usuario;
import vos.UsuarioMinimum;
import vos.UsuarioMinimum.Rol;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/CuentaAndes/rest/cuentas/...
 * @author Monitores 2017-20
 */
@Path("cuentas")
public class CuentaServices {

	/**
	 * Atributo que usa la anotacion @Context para tener el ServletContext de la conexion actual.
	 */
	@Context
	private ServletContext context;

	/**
	 * Metodo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}
	
	
	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	

	/**
	 * Metodo que expone servicio REST usando GET que da todos los cuentas de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/CuentaAndes/rest/cuentas
	 * @param usuarioId Id del usuario que realiza la solicitud.
	 * @return Json con todos los cuentas de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCuentas(@HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Cuenta> cuentas;
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!u.getRol().equals(Rol.OPERADOR))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			cuentas = tm.cuentaDarCuentas();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(cuentas).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca las cuentas de un cliente con id dado.
     * <b>URL: </b> http://"ip o nombre de host":8080/CuentaAndes/rest/cuentas/<<id>>" para la busqueda"
     * @param name - Nombre del cuenta a buscar que entra en la URL como parametro 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el/los cuentas encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	/*@GET
	@Path("id")
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getHistorialCliente( @QueryParam( "id" ) Long id, @HeaderParam("usuarioId") Long usuarioId )
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.CLIENTE) && u.getId()!=id))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			List<CuentaMinimum> v = tm.cuentaBuscarCuentasPorId( id );
			return Response.status( 200 ).entity( v ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}*/
	 /**
     * Metodo que expone servicio REST usando GET que busca el cuenta con el número de cuenta que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/CuentaAndes/rest/cuentas/<<id>>" para la busqueda"
     * @param name - Nombre del cuenta a buscar que entra en la URL como parametro 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el/los cuentas encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{numCuenta: [0-9]+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getCuenta(@PathParam("numCuenta")String numCuenta, @HeaderParam("usuarioId") Long usuarioId)
	{
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try
		{
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!u.getRol().equals(Rol.OPERADOR))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			if(numCuenta==null || numCuenta.length()==0)
				throw new Exception("El número de cuenta es inválido");
			Cuenta c = tm.cuentaBuscarCuentasPorNumeroDeCuenta(numCuenta);
			if(u.getRol().equals(Rol.CLIENTE) && u.getId()!=c.getCliente().getId())
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			if(c==null)return Response.status( 404 ).entity(c).build( );			
			return Response.status(200).entity(c).build();
		}
		catch (Exception e)
		{
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	

    /**
     * Metodo que expone servicio REST usando POST que agrega el cuenta que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CuentaAndes/rest/cuentas/cuenta
     * @param cuenta - cuenta a agregar
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el cuenta que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCuenta(Cuenta cuenta, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!u.getRol().equals(Rol.OPERADOR) )
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			if(cuenta.getCliente()!=null && !cuenta.getCliente().getRol().equals(Rol.CLIENTE))
				throw new Exception("Una cuenta solo puede pertenecer a un cliente");
			tm.cuentaAddCuenta(cuenta);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(cuenta).build();
	}
	
    /**
     * Metodo que expone servicio REST usando PUT que actualiza el cuenta que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CuentaAndes/rest/cuentas
     * @param cuenta - cuenta a actualizar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el cuenta que actualizo o Json con el error que se produjo
     */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCuenta(Cuenta cuenta, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.CLIENTE) && u.getId()!=cuenta.getCliente().getId()))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.cuentaUpdateCuenta(cuenta);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(cuenta).build();
	}
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina la cuenta que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CuentaAndes/rest/cuentas
     * @param cuenta - cuenta a aliminar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el cuenta que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCuenta(Cuenta cuenta, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.CLIENTE) && u.getId()!=cuenta.getCliente().getId()))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.cuentaDeleteCuenta(cuenta);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(cuenta).build();
	}
	/**
     * Metodo que expone servicio REST usando DELETE que elimina el historial recibido del cliente.
     * <b>URL: </b> http://"ip o nombre de host":8080/CuentaAndes/rest/cuentas
     * @param id a eliminar.
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el cuenta que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id : \\d+}")
	public Response borrarHistorialCliente(@QueryParam("id")Long id, @HeaderParam("usuarioId") Long usuarioId)
	{
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try
		{
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!u.getRol().equals(Rol.OPERADOR))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.cuentaBorrarHistorialCliente(id);
		}
		catch (Exception e)
		{
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).build();
	}
	
	/**
     * Metodo que expone servicio REST usando POST que agrega el pedidoProd que recibe en Json
     * @param numeroCuenta nombre del cuenta al cual agregarlo.
     * @param pedidoProd - pedidoProd a agregar.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el pedidoProd que agrego o Json con el error que se produjo
     */
	@PUT
	@Path( "{numeroCuenta: \\d+}" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pagarCuenta(@PathParam("numeroCuenta") String numeroCuenta, @HeaderParam("usuarioId") Long id ) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		PendientesOrden ped=null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.CLIENTE) && id != tm.cuentaBuscarCuentasPorNumeroDeCuenta(numeroCuenta).getCliente().getId()))) 
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			ped=tm.cuentaPagarCuenta(numeroCuenta);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(ped).build();
	}
	//Subrecurso pedidoProd
	/** Metodo que expone servicio REST usando GET que da todos los pedidoProds de la base de datos para un cuenta particular.
	 * @param numeroCuenta nombre del cuenta.
	 * @return Json con todos los pedidoProds de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Path( "{numeroCuenta: \\d+}/productos" )
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPedidoProds(@PathParam("numeroCuenta") String numeroCuenta) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<PedidoProd> pedidoProds;
		try {
			pedidoProds = tm.pedidoProdDarPedidoProdsPorCuenta(numeroCuenta);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedidoProds).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el pedidoProd con el id que entra como parametro.
     * @param numeroCuenta nombre del cuenta al cual pertenece el pedidoProd.
     * @param name Nombre del pedidoProd a buscar que entra en la URL como parametro.
     * @return Json con el/los pedidoProds encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{numeroCuenta: \\d+}/productos/{restaurante: [a-zA-Z]+}/{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getPedidoProd( @PathParam( "id" ) Long id, @PathParam("restaurante") String restaurante, @PathParam("numeroCuenta") String numeroCuenta)
	{
		restaurante=restaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			PedidoProd v = tm.pedidoProdBuscarPedidoProdsPorIdYCuenta(id, numeroCuenta, restaurante);
			if(v == null) 
				return Response.status( 404 ).entity( v ).build( );
			return Response.status( 200 ).entity( v ).build( );
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


    /**
     * Metodo que expone servicio REST usando POST que agrega el pedidoProd que recibe en Json
     * @param numeroCuenta nombre del cuenta al cual agregarlo.
     * @param pedidoProd - pedidoProd a agregar.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el pedidoProd que agrego o Json con el error que se produjo
     */
	@POST
	@Path( "{numeroCuenta: \\d+}/productos" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPedidoProd(@PathParam("numeroCuenta") String numeroCuenta, PedidoProd pedidoProd, @HeaderParam("usuarioId") Long id) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			Cuenta c=tm.cuentaBuscarCuentasPorNumeroDeCuenta(numeroCuenta);
			if(!(u.getRol().equals(Rol.OPERADOR) || id == tm.cuentaBuscarCuentasPorNumeroDeCuenta(numeroCuenta).getCliente().getId())) 
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			pedidoProd.setCuenta(c);
			tm.pedidoProdAddPedidoProd(pedidoProd);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedidoProd).build();
	}
	
    /**
     * Metodo que expone servicio REST usando PUT que modifica un cuenta.
     * @param idProducto nombre del pedidoProd a modificar.
     * @param numeroCuenta nombre del cuenta que lo contiene.
     * @param pedidoProd informaci�n del pedidoProd modificado.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el cuenta que elimino o Json con el error que se produjo
     */
	@PUT
	@Path( "{numeroCuenta: \\d+}/productos/{restaurante: [a-zA-Z]+}/{id: \\d+}" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePedidoProd(PedidoProd pedidoProd, @HeaderParam("usuarioId") Long id, @PathParam("id") Long idProducto, @PathParam("numeroCuenta") String numeroCuenta, @PathParam("restaurante") String restaurante) {
		restaurante=restaurante.replaceAll(RotondAndesTM.SPACE, " ");
		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			Cuenta c=tm.cuentaBuscarCuentasPorNumeroDeCuenta(numeroCuenta);
			if(!(u.getRol().equals(Rol.OPERADOR) || u.getRol().equals(Rol.LOCAL) || (u.getRol().equals(Rol.CLIENTE) && c.getCliente()!=null && id != c.getCliente().getId())))
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			if(u.getRol().equals(Rol.LOCAL)) 
			{
				if(u.getRestaurante()==null || !u.getRestaurante().getNombre().equals(restaurante)) throw new Exception("Esta cuenta no es de ese restaurante");
				pedidoProd.setEntregado(true);
			}
			if(pedidoProd.getEntregado() && !u.getRol().equals(Rol.LOCAL)) throw new Exception("No se puede modificar un pedido que ya fue entregado");
			Cuenta cu=tm.cuentaBuscarCuentasPorNumeroDeCuenta(numeroCuenta);
			pedidoProd.setPlato(tm.infoProdRestBuscarInfoProdRestsPorIdYRestaurante(idProducto, restaurante));
			pedidoProd.setCuenta(cu);
			tm.pedidoProdUpdatePedidoProd(pedidoProd);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedidoProd).build();
	}

	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina un pedidoProd de un cuenta dado.
     * @param idProducto nombre del pedidoProd a eliminar.
     * @param numeroCuenta nombre del cuenta que lo contiene.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el pedidoProd que elimino o Json con el error que se produjo
     */
	@DELETE
	@Path( "{numeroCuenta: \\d+}/productos/{restaurante: [a-zA-Z]+}/{id: \\d+}" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePedidoProd(@HeaderParam("usuarioId") Long id, @PathParam("numeroCuenta") String numeroCuenta, @PathParam("id") Long idProducto, @PathParam("restaurante") String restaurante) {
		restaurante=restaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || id == tm.cuentaBuscarCuentasPorNumeroDeCuenta(numeroCuenta).getCliente().getId()))
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			tm.pedidoProdDeletePedidoProd(numeroCuenta, idProducto, restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity("pedidoProd eliminado correctamente").build();
	}
	
	//Subrecurso pedidoMenu
	/** Metodo que expone servicio REST usando GET que da todos los pedidoMenus de la base de datos para un cuenta particular.
	 * @param numeroCuenta nombre del cuenta.
	 * @return Json con todos los pedidoMenus de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Path( "{numeroCuenta: \\d+}/menus" )
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPedidoMenus(@PathParam("numeroCuenta") String numeroCuenta) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<PedidoMenu> pedidoMenus;
		try {
			pedidoMenus = tm.pedidoMenuDarPedidoMenusPorCuenta(numeroCuenta);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedidoMenus).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el pedidoMenu con el id que entra como parametro.
     * @param numeroCuenta nombre del cuenta al cual pertenece el pedidoMenu.
     * @param name Nombre del pedidoMenu a buscar que entra en la URL como parametro.
     * @return Json con el/los pedidoMenus encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{numeroCuenta: \\d+}/menus/{restaurante: [a-zA-Z]+}/{nombre: [a-zA-Z]+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getPedidoMenu( @PathParam( "nombre" ) String nombre, @PathParam("restaurante") String restaurante, @PathParam("numeroCuenta") String numeroCuenta)
	{
		restaurante=restaurante.replaceAll(RotondAndesTM.SPACE, " ");
		nombre=nombre.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			PedidoMenu v = tm.pedidoMenuBuscarPedidoMenusPorIdYCuenta(nombre, numeroCuenta, restaurante);
			if(v == null) 
				return Response.status( 404 ).entity( v ).build( );
			return Response.status( 200 ).entity( v ).build( );
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


    /**
     * Metodo que expone servicio REST usando POST que agrega el pedidoMenu que recibe en Json
     * @param numeroCuenta nombre del cuenta al cual agregarlo.
     * @param pedidoMenu - pedidoMenu a agregar.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el pedidoMenu que agrego o Json con el error que se produjo
     */
	@POST
	@Path( "{numeroCuenta: \\d+}/menus" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPedidoMenu(@PathParam("numeroCuenta") String numeroCuenta, PedidoMenu pedidoMenu, @HeaderParam("usuarioId") Long id) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			Cuenta c=tm.cuentaBuscarCuentasPorNumeroDeCuenta(numeroCuenta);
			if(!(u.getRol().equals(Rol.OPERADOR) || id == tm.cuentaBuscarCuentasPorNumeroDeCuenta(numeroCuenta).getCliente().getId())) 
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			pedidoMenu.setCuenta(c);
			tm.pedidoMenuAddPedidoMenu(pedidoMenu);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedidoMenu).build();
	}
	
    /**
     * Metodo que expone servicio REST usando PUT que modifica un cuenta.
     * @param nombre nombre del pedidoMenu a modificar.
     * @param numeroCuenta nombre del cuenta que lo contiene.
     * @param pedidoMenu informaci�n del pedidoMenu modificado.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el cuenta que elimino o Json con el error que se produjo
     */
	@PUT
	@Path( "{numeroCuenta: \\d+}/menus/{restaurante: [a-zA-Z]+}/{nombre: [a-zA-Z]+}" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePedidoMenu(PedidoMenu pedidoMenu, @HeaderParam("usuarioId") Long id, @PathParam("nombre") String nombre, @PathParam("numeroCuenta") String numeroCuenta, @PathParam("restaurante") String restaurante) {
		restaurante=restaurante.replaceAll(RotondAndesTM.SPACE, " ");
		nombre=nombre.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			Cuenta c=tm.cuentaBuscarCuentasPorNumeroDeCuenta(numeroCuenta);
			if(!(u.getRol().equals(Rol.OPERADOR) || u.getRol().equals(Rol.LOCAL) || (u.getRol().equals(Rol.CLIENTE) && c.getCliente()!=null && id != c.getCliente().getId())))
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			if(u.getRol().equals(Rol.LOCAL)) 
			{
				if(u.getRestaurante()==null || !u.getRestaurante().getNombre().equals(restaurante)) throw new Exception("Esta cuenta no es de ese restaurante");
				pedidoMenu.setEntregado(true);
			}
			if(pedidoMenu.getEntregado() && !u.getRol().equals(Rol.LOCAL)) throw new Exception("No se puede modificar un pedido que ya fue entregado");
			Cuenta cu=tm.cuentaBuscarCuentasPorNumeroDeCuenta(numeroCuenta);
			pedidoMenu.setMenu(tm.menuBuscarMenusPorNombreYRestaurante(nombre, restaurante));
			pedidoMenu.setCuenta(cu);
			tm.pedidoMenuUpdatePedidoMenu(pedidoMenu);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedidoMenu).build();
	}

	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina un pedidoMenu de un cuenta dado.
     * @param nombre nombre del pedidoMenu a eliminar.
     * @param numeroCuenta nombre del cuenta que lo contiene.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el pedidoMenu que elimino o Json con el error que se produjo
     */
	@DELETE
	@Path( "{numeroCuenta: \\d+}/menus/{restaurante: [a-zA-Z]+}/{nombre: [a-zA-Z]+}" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePedidoMenu(@HeaderParam("usuarioId") Long id, @PathParam("numeroCuenta") String numeroCuenta, @PathParam("nombre") String nombre, @PathParam("restaurante") String restaurante) {
		restaurante=restaurante.replaceAll(RotondAndesTM.SPACE, " ");
		nombre=nombre.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || id == tm.cuentaBuscarCuentasPorNumeroDeCuenta(numeroCuenta).getCliente().getId()))
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			tm.pedidoMenuDeletePedidoMenu(numeroCuenta, nombre, restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity("pedidoMenu eliminado correctamente").build();
	}
}
