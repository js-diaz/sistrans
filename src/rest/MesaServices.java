
package rest;


import java.util.ArrayList;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.Cuenta;
import vos.CuentaMinimum;
import vos.Mesa;
import vos.PedidoMenu;
import vos.PedidoProd;
import vos.Usuario;
import vos.UsuarioMinimum;
import vos.UsuarioMinimum.Rol;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/MesaAndes/rest/mesas/...
 * @author Monitores 2017-20
 */
@Path("mesas")
public class MesaServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los mesas de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/MesaAndes/rest/mesas
	 * @param usuarioId Id del usuario que realiza la solicitud.
	 * @return Json con todos los mesas de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getMesas() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Mesa> mesas;
		try {
			mesas = tm.mesaDarMesas();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(mesas).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el mesa con el id que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/MesaAndes/rest/mesas/<<id>>" para la busqueda"
     * @param name - Nombre del mesa a buscar que entra en la URL como parametro 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el/los mesas encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getMesa( @PathParam( "id" ) Long id )
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			Mesa v = tm.mesaBuscarMesaPorId( id );
			if(v==null)return Response.status( 404 ).entity( v ).build( );			
			return Response.status( 200 ).entity( v ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

    

    /**
     * Metodo que expone servicio REST usando POST que agrega el mesa que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/MesaAndes/rest/mesas/mesa
     * @param mesa - mesa a agregar
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el mesa que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMesa(Mesa mesa, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!u.getRol().equals(Rol.OPERADOR))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.mesaAddMesa(mesa);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(mesa).build();
	}
	
  
	
    /**
     * Metodo que expone servicio REST usando PUT que actualiza el mesa que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/MesaAndes/rest/mesas
     * @param mesa - mesa a actualizar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el mesa que actualizo o Json con el error que se produjo
     */
	//AUTOMATIZACIÓN
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMesa(Mesa mesa,@HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!u.getRol().equals(Rol.OPERADOR))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.mesaUpdateMesa(mesa);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(mesa).build();
	}
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el mesa que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/MesaAndes/rest/mesas
     * @param mesa - mesa a aliminar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el mesa que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMesa(Mesa mesa, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!u.getRol().equals(Rol.OPERADOR))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.mesaDeleteMesa(mesa);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(mesa).build();
	}
	
	/**
     * Metodo que expone servicio REST usando DELETE que elimina el mesa que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/MesaAndes/rest/mesas
     * @param mesa - mesa a aliminar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el mesa que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("canceloCuenta")
	public Response cancelarCuentasPorMesa(Mesa mesa, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<CuentaMinimum> list = new ArrayList<>();
		try {
			Usuario u = tm.usuarioBuscarUsuarioPorId( usuarioId );
			if(!((u!=null && u.getRol().equals(Rol.LOCAL))))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			list=tm.mesaBorrarCuentasActivasPorMesa(mesa,u.getRestaurante().getNombre());
			
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(list).build();
	}
	/**
	 * M�todo que ordena los pedidos de producto pedidos en la mesa dada.
	 * @param numeroMesa N�mero de la mesa que ordena los pedidos.
	 * @param pedidos Pedidos a ordenar.
	 * @param id Id del usuario que hace la modificaci�n.
	 * @return Response con los pedidos a�adidos.
	 */
	@POST
	@Path( "{id: \\d+}/pedidos-producto" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarPedidosProducto(@PathParam("id") Long idMesa, List<PedidoProd> pedidos, @HeaderParam("usuarioId") Long id) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			Mesa mesa = tm.mesaBuscarMesaPorId(idMesa);
			if(!(u.getRol().equals(Rol.OPERADOR) || u.getRol().equals(Rol.CLIENTE))) 
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			tm.mesaRegistrarPedidosProducto(pedidos, mesa);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedidos).build();
	}

	/**
	 * M�todo que ordena los pedidos de menu pedidos en la mesa dada.
	 * @param numeroMesa N�mero de la mesa que ordena los pedidos.
	 * @param pedidos Pedidos a ordenar.
	 * @param id Id del usuario que hace la modificaci�n.
	 * @return Response con los pedidos a�adidos.
	 */
	@POST
	@Path( "{id: \\d+}/pedidos-menu" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarPedidosMenu(@PathParam("id") Long idMesa, List<PedidoMenu> pedidos, @HeaderParam("usuarioId") Long id) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			Mesa mesa = tm.mesaBuscarMesaPorId(idMesa);
			if(!(u.getRol().equals(Rol.OPERADOR) || u.getRol().equals(Rol.CLIENTE))) 
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			tm.mesaRegistrarPedidosMenu(pedidos, mesa);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedidos).build();
	}

	/**
	 * M�todo que registra el servicio de los pedidos de cierta mesa.
	 * @param numeroMesa N�mero de la mesa de la cu�l se quiere registrar el servicio.
	 * @param id Id del usuario que realiza la operaci�n
	 * @return Response seg�n si la operaci�n fue efectuada correctamente o no.
	 */
	@PUT
	@Path( "{id: \\d+}/servicio" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarServicio(@PathParam("id") Long idMesa, @HeaderParam("usuarioId") Long id) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			Mesa mesa = tm.mesaBuscarMesaPorId(idMesa);
			if(!(u.getRol().equals(Rol.OPERADOR) || u.getRol().equals(Rol.LOCAL))) 
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			tm.mesaRegistrarServicio(mesa);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity("El servicio fue registrado correctamente.").build();
	}

}
