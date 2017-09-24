
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
import vos.CondicionTecnica;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/CondicionTecnicaAndes/rest/condiciones/...
 * @author s.guzmanm
 */
@Path("condiciones")
public class CondicionServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los condiciones de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/RotondAndes/rest/condiciones
	 * @return Json con todos los condiciones de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCondicionTecnicas() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<CondicionTecnica> condiciones;
		try {
			condiciones = tm.condicionDarCondicionesTecnicas();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(condiciones).build();
	}

    /**
     * Metodo que expone servicio REST usando GET que busca la zona con el nombre que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionTecnicaAndes/rest/condiciones/nombre/nombre?nombre=<<nombre>>" para la busqueda"
     * @param name - Nombre dla zona a buscar que entra en la URL como parametro 
     * @return Json con el/los condiciones encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{nombre :[a-zA-Z]+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getCondicionTecnicaName( @PathParam("nombre") String name) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		CondicionTecnica condiciones;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre de la condición no valido");
			condiciones = tm.condicionBuscarCondicionTecnicasPorName(name);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(condiciones).build();
	}


    /**
     * Metodo que expone servicio REST usando POST que agrega la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionTecnicaAndes/rest/condiciones/zona
     * @param zona - zona a agregar
     * @return Json con la zona que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCondicionTecnica(CondicionTecnica zona) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.condicionAddCondicionTecnica(zona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zona).build();
	}
	
   
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionTecnicaAndes/rest/condiciones
     * @param zona - zona a aliminar. 
     * @return Json con la zona que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCondicionTecnica(CondicionTecnica zona) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.condicionDeleteCondicionTecnica(zona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zona).build();
	}


}
