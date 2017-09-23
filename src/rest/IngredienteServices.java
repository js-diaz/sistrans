
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
import vos.Ingrediente;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/IngredienteAndes/rest/ingredientes/...
 * @author Monitores 2017-20
 */
@Path("ingredientes")
public class IngredienteServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los ingredientes de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/IngredienteAndes/rest/ingredientes
	 * @return Json con todos los ingredientes de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getIngredientes() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Ingrediente> ingredientes;
		try {
			ingredientes = tm.ingredienteDarIngredientes();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(ingredientes).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el ingrediente con el id que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/IngredienteAndes/rest/ingredientes/<<id>>" para la busqueda"
     * @param name - Nombre del ingrediente a buscar que entra en la URL como parametro 
     * @return Json con el/los ingredientes encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getIngrediente( @PathParam( "id" ) Long id )
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			Ingrediente v = tm.ingredienteBuscarIngredientePorId( id );
			return Response.status( 200 ).entity( v ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

    

    /**
     * Metodo que expone servicio REST usando POST que agrega el ingrediente que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/IngredienteAndes/rest/ingredientes/ingrediente
     * @param ingrediente - ingrediente a agregar
     * @return Json con el ingrediente que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addIngrediente(Ingrediente ingrediente) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.ingredienteAddIngrediente(ingrediente);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(ingrediente).build();
	}
	
  
	
    /**
     * Metodo que expone servicio REST usando PUT que actualiza el ingrediente que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/IngredienteAndes/rest/ingredientes
     * @param ingrediente - ingrediente a actualizar. 
     * @return Json con el ingrediente que actualizo o Json con el error que se produjo
     */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateIngrediente(Ingrediente ingrediente) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.ingredienteUpdateIngrediente(ingrediente);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(ingrediente).build();
	}
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el ingrediente que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/IngredienteAndes/rest/ingredientes
     * @param ingrediente - ingrediente a aliminar. 
     * @return Json con el ingrediente que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteIngrediente(Ingrediente ingrediente) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.ingredienteDeleteIngrediente(ingrediente);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(ingrediente).build();
	}


}
