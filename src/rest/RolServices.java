
package rest;


import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import vos.Usuario.Rol;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/RolAndes/rest/rols/...
 * @author s.guzmanm
 */
@Path("rols")
public class RolServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los rols de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/RolAndes/rest/rols
	 * @return Json con todos los rols de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRols() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Rol> rols;
		try {
			rols = tm.rolDarRols();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(rols).build();
	}

    /**
     * Metodo que expone servicio REST usando GET que busca el rol con el nombre que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/RolAndes/rest/rols/nombre/nombre?nombre=<<nombre>>" para la busqueda"
     * @param name - Nombre del rol a buscar que entra en la URL como parametro 
     * @return Json con el/los rols encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{nombre:[A-Z]+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getRolName(@PathParam("nombre") String name) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		Rol rols;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre del rol no valido");
			rols = tm.rolBuscarRolsPorName(name);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(rols).build();
	}


    /**
     * Metodo que expone servicio REST usando POST que agrega el rol que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/RolAndes/rest/rols/rol
     * @param rol - rol a agregar
     * @return Json con el rol que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{nombre:[A-Z]+}")
	public Response addRol(@PathParam("nombre")String rol) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.rolAddRol(rol);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(rol).build();
	}
	
    
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el rol que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/RolAndes/rest/rols
     * @param rol - rol a aliminar. 
     * @return Json con el rol que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{nombre:[A-Z]+}")
	public Response deleteRol(@PathParam("nombre")String rol) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.rolDeleteRol(rol);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(rol).build();
	}


}
