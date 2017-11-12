
package rest;


import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.Categoria;
import vos.UsuarioMinimum;
import vos.UsuarioMinimum.Rol;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/CategoriaAndes/rest/categorias/...
 * @author Monitores 2017-20
 */
@Path("categorias")
public class CategoriaServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los videos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/CategoriaAndes/rest/videos
	 * @return Json con todos los videos de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCategorias() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Categoria> categorias;
		try {
			categorias = tm.categoriaDarCategorias();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(categorias).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el video con el id que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/CategoriaAndes/rest/videos/<<id>>" para la busqueda"
     * @param name - Nombre del video a buscar que entra en la URL como parametro 
     * @return Json con el/los videos encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{name: [a-zA-Z]+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getCategoria( @PathParam( "name" ) String name )
	{
		name=name.replaceAll(RotondAndesTM.SPACE, " ");
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			if(name==null || name.length()==0)
				throw new Exception("El nombre de la categoría es inválido");
			Categoria v = tm.categoriaBuscarCategoriasPorName( name );
			if(v==null) return Response.status( 404 ).entity( v ).build( );			
			return Response.status( 200 ).entity( v ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


    /**
     * Metodo que expone servicio REST usando POST que agrega el video que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CategoriaAndes/rest/videos/video
     * @param video - video a agregar
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el video que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCategoria(Categoria video, @HeaderParam("usuarioId") Long id) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		UsuarioMinimum u =null;
		try {
			u=tm.usuarioBuscarUsuarioMinimumPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR))) throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			tm.categoriaAddCategoria(video);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(video).build();
	}
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el video que recibe en Json
     * @param video - video a aliminar. 
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el video que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCategoria(Categoria c, @HeaderParam("usuarioId") Long id) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		UsuarioMinimum u =null;
		try {
			u=tm.usuarioBuscarUsuarioMinimumPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR))) throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			tm.categoriaDeleteCategoria(c);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(c).build();
	}


}
