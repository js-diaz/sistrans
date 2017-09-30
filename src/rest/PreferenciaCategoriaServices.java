
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
import vos.Usuario;
import vos.Categoria;
import vos.Preferencia;
import vos.UsuarioMinimum.Rol;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/preferencias/categorias...
 * @author s.guzmanm
 */
@Path("preferencias/categorias")
public class PreferenciaCategoriaServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los zonas de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/zonas
	 * @param cat Categoría a consultar preferencias.
	 * @return Json con todos los zonas de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces( MediaType.APPLICATION_JSON )
	public Response getPreferenciaCategoriaPorCategoria(@HeaderParam("categoria")String cat) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Preferencia> preferencias;
		try {
			if (cat == null || cat.length() == 0)
				throw new Exception("Nombre de la categoría no válido");
			preferencias = tm.preferenciaCategoriaBuscarCategoriaPorCategoria(new Categoria(cat));
			if(preferencias.isEmpty())return Response.status( 404 ).entity( preferencias ).build( );			

		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(preferencias).build();
	}

    /**
     * Metodo que expone servicio REST usando GET que busca la zona con el nombre que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/zonas/nombre/nombre?nombre=<<nombre>>" para la busqueda"
     * @param preferencias - Id de la preferencia a buscar que entra en la URL como parametro 
     * @return Json con el/los zonas encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Produces( MediaType.APPLICATION_JSON  )
	public Response getCondicionesZonaPorCategoria( @HeaderParam("preferencia") Long preferencia) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Categoria> categorias;
		try {
			if (preferencia == null)
				throw new Exception("Id de la preferencia no válido");
			categorias = tm.preferenciaCategoriaBuscarCategoriasPorId(preferencia);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		if(categorias.isEmpty())return Response.status( 404 ).entity( categorias ).build( );			
		return Response.status(200).entity(categorias).build();
	}
    /**
     * Metodo que expone servicio REST usando POST que agrega la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/zonas/zona
     * @param preferencia - Id de preferencia a agregar
     * @param categorias- Listado de categorías a agregar
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con la zona que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPreferenciasCategoriaPorPreferencia( List<Categoria> categorias, @HeaderParam("preferencia") Long preferencia,@HeaderParam("usuarioId")Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Usuario u = tm.usuarioBuscarUsuarioPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)) || !(u.getRol().equals(Rol.ORGANIZADORES)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.preferenciaCategoriaInsertarPreferenciasPorCategoria(preferencia, categorias);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(categorias).build();
	}
    /**
     * Metodo que expone servicio REST usando DELETE que elimina la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/zonas
     * @param cat - categoría a aliminar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con la zona que elimino o Json con el error que se produjo
     */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePreferenciaCategoriaPorCategoria(@HeaderParam("categoria")String categoria, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Usuario u = tm.usuarioBuscarUsuarioPorId( usuarioId );
			if(categoria==null || categoria.length()==0)
			{
				throw new Exception("Nombre de la categoría es inválido");
			}
			if(!(u.getRol().equals(Rol.OPERADOR)) || !(u.getRol().equals(Rol.ORGANIZADORES)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.preferenciaCategoriaBorrarPorCategoria(categoria);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).build();
	}

	 /**
     * Metodo que expone servicio REST usando DELETE que elimina la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/zonas
     * @param preferencia - id de preferencia a eliminar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con la zona que elimino o Json con el error que se produjo
     */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePreferenciasCategoriaPorPreferencia(@HeaderParam("preferencia")Long preferencia, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Usuario u = tm.usuarioBuscarUsuarioPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)) || !(u.getRol().equals(Rol.ORGANIZADORES)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.preferenciaCategoriaBorrarPorId(preferencia);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).build();
	}
	
	 /**
     * Metodo que expone servicio REST usando DELETE que elimina la preferencia zona
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/zonas
     * @param preferencia - preferencia a aliminar. 
     * @param cat - categoría a eliminar.
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con la zona que elimino o Json con el error que se produjo
     */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePreferenciaCategoria(@HeaderParam("preferencia")Long preferencia, @HeaderParam("categoria") String cat,@HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Usuario u = tm.usuarioBuscarUsuarioPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)) || !(u.getRol().equals(Rol.ORGANIZADORES)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.preferenciaCategoriaBorrarMulticriterio(preferencia, cat);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).build();
	}

}
