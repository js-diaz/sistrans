
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

import org.codehaus.jackson.map.ObjectMapper;

import tm.RotondAndesTM;
import vos.Cuenta;
import vos.CuentaMinimum;
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
	@GET
	@Path( "{id: \\d+}" )
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
	}
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
			if(!u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.CLIENTE) && u.getId()!=cuenta.getCliente().getId()))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
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


}
