
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
import vos.UsuarioMinimum;
import vos.Producto.TiposDePlato;
import vos.UsuarioMinimum.Rol;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/TiposDePlatoAndes/rest/tipos/...
 * @author s.guzmanm
 */
@Path("tipos")
public class TiposProductoServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los tipos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/TiposDePlatoAndes/rest/tipos
	 * @return Json con todos los tipos de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getTiposDePlatos() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<TiposDePlato> tipos;
		try {
			tipos = tm.tiposDarTipos_De_Plato();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(tipos).build();
	}

	
    /**
     * Metodo que expone servicio REST usando GET que busca el tipo con el nombre que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/TiposDePlatoAndes/rest/tipos/nombre/nombre?nombre=<<nombre>>" para la busqueda"
     * @param name - Nombre del tipo a buscar que entra en la URL como parametro 
     * @return Json con el/los tipos encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{nombre}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getTiposDePlatoName( @PathParam("nombre") String name) {
		name=name.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		TiposDePlato tipos;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre del tipo no valido");
			tipos = tm.tiposBuscarTipos_De_PlatosPorName(name);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		if(tipos==null)return Response.status( 404 ).entity( tipos ).build( );			

		return Response.status(200).entity(tipos).build();
	}


    /**
     * Metodo que expone servicio REST usando POST que agrega el tipo que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/TiposDePlatoAndes/rest/tipos/tipo
     * @param tipo - tipo a agregar
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el tipo que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{nombre}")
	public Response addTiposDePlato(@PathParam("nombre")String tipo, @HeaderParam("usuarioId") Long usuarioId) {
		tipo=tipo.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.tiposAddTipos_De_Plato(tipo);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		
		return Response.status(200).entity(tipo).build();
	}
	
    
	
   
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el tipo que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/TiposDePlatoAndes/rest/tipos
     * @param tipo - tipo a aliminar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el tipo que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{nombre}")
	public Response deleteTiposDePlato(@PathParam("nombre") String tipo, @HeaderParam("usuarioId") Long usuarioId) {
		tipo=tipo.replaceAll(RotondAndesTM.SPACE, " ");
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.tiposDeleteTipos_De_Plato(tipo);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(tipo).build();
	}


}
