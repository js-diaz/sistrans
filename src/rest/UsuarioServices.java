/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: UsuarioAndes
 * Autor: Juan Felipe García - jf.garcia268@uniandes.edu.co
 * -------------------------------------------------------------------
 */
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

import rfc.UsuarioCompleto;
import tm.RotondAndesTM;
import vos.Usuario;
import vos.UsuarioMinimum;
import vos.UsuarioMinimum.Rol;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/UsuarioAndes/rest/usuarios/...
 * @author s.guzmanm
 */
@Path("usuarios")
public class UsuarioServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los usuarios de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/UsuarioAndes/rest/usuarios
	 * @return Json con todos los usuarios de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getUsuarios() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Usuario> usuarios;
		try {
			usuarios = tm.usuarioDarUsuarios();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuarios).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el usuario con el id que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/UsuarioAndes/rest/usuarios/<<id>>" para la busqueda"
     * @param id - Id del usuario a buscar que entra en la URL como parametro 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el/los usuarios encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{id:\\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getUsuario( @PathParam( "id" ) Long id, @HeaderParam("usuarioId") Long usuarioId )
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
			try
			{
				UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
				if(usuarioId!=id && !(u.getRol().equals(Rol.OPERADOR)) || (u.getRol().equals(Rol.CLIENTE) && u.getId()!=id))
				{
					throw new Exception("El usuario no tiene permitido usar el sistema");
				}
				Usuario v = tm.usuarioBuscarUsuarioPorId( id );
				if(v==null)return Response.status( 404 ).entity( v ).build( );			

				return Response.status( 200 ).entity( v ).build( );			
			}
			catch( Exception e )
			{
				return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
			}
		
		
		
	}
	/**
	 * REQUERIMIENTO DE CONSULTA 3
     * Metodo que expone servicio REST usando GET que busca el usuario con el id que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/UsuarioAndes/rest/usuarios/nombre/nombre?nombre=<<nombre>>" para la busqueda"
     * @param name - Nombre del usuario a buscar que entra en la URL como parametro 
     * @return Json con el/los usuarios encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path("completo/{id:\\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsuarioCompleto( @PathParam("id") Long id, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		UsuarioCompleto usuario=null;
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			if (id == null || id<0)
				throw new Exception("Id no válida");
			usuario=tm.usuarioCompletoBuscarUsuarioPorId(id);
			if(!usuario.getRol().equals(Rol.CLIENTE)) throw new Exception("Este historial solo está diseñado para clientes de RotondAndes");
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuario).build();
	}
	/**
     * Metodo que expone servicio REST usando GET que busca el usuario con el nombre que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/UsuarioAndes/rest/usuarios/nombre/nombre?nombre=<<nombre>>" para la busqueda"
     * @param name - Nombre del usuario a buscar que entra en la URL como parametro 
     * @return Json con el/los usuarios encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "rol" )
	@Produces(  MediaType.APPLICATION_JSON  )
	public Response getUsuarioPorRol( @QueryParam("rol") String nombreRol, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Usuario> usuarios;
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			if (nombreRol == null || nombreRol.length() == 0)
				throw new Exception("Nombre del rol no valido");
			usuarios = tm.usuarioBuscarUsuarioPorRol(nombreRol);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuarios).build();
	}


    /**
     * Metodo que expone servicio REST usando POST que agrega el usuario que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/UsuarioAndes/rest/usuarios/usuario
     * @param usuario - usuario a agregar
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el usuario que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUsuario(Usuario usuario, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.usuarioAddUsuario(usuario);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuario).build();
	}
	
	
    /**
     * Metodo que expone servicio REST usando PUT que actualiza el usuario que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/UsuarioAndes/rest/usuarios
     * @param usuario - usuario a actualizar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el usuario que actualizo o Json con el error que se produjo
     */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUsuario(Usuario usuario, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR))  && usuario.getId()!=usuarioId)
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.usuarioUpdateUsuario(usuario);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuario).build();
	}
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el usuario que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/UsuarioAndes/rest/usuarios
     * @param usuario - usuario a aliminar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el usuario que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUsuario(Usuario usuario, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)) && usuario.getId()!=usuarioId)
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.usuarioDeleteUsuario(usuario);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuario).build();
	}
	
	/**
     * Metodo que expone servicio REST usando DELETE que elimina el usuario que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/UsuarioAndes/rest/usuarios
     * @param usuario - usuario a aliminar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el usuario que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{rol:[A-Z]+}")
	public Response deleteUsuarioPorRol(@QueryParam("rol")String nombreRol, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)) )
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			if (nombreRol == null || nombreRol.length() == 0)
				throw new Exception("Nombre del rol no valido");
			tm.usuarioBorrarPorRol(nombreRol);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).build();
	}


}
