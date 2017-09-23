/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: PreferenciaAndes
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

import tm.RotondAndesTM;
import vos.Preferencia;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/PreferenciaAndes/rest/preferencias/...
 * @author Monitores 2017-20
 */
@Path("preferencias")
public class PreferenciaServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los preferencias de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/PreferenciaAndes/rest/preferencias
	 * @return Json con todos los preferencias de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPreferencias() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Preferencia> preferencias;
		try {
			preferencias = tm.preferenciaDarPreferencias();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(preferencias).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca la preferencia con el id que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/PreferenciaAndes/rest/preferencias/<<id>>" para la busqueda"
     * @param name - Nombre dla preferencia a buscar que entra en la URL como parametro 
     * @return Json con el/los preferencias encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getPreferencia( @PathParam( "id" ) Long id )
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			Preferencia v = tm.preferenciaBuscarPreferenciaPorId( id );
			return Response.status( 200 ).entity( v ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	
	 /**
     * Metodo que expone servicio REST usando GET que busca la preferencia con el id que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/PreferenciaAndes/rest/preferencias/<<id>>" para la busqueda"
     * @param name - Nombre dla preferencia a buscar que entra en la URL como parametro 
     * @return Json con el/los preferencias encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getPreferenciaPorRango( @PathParam( "id" ) Long id , @HeaderParam("inicial") Double ini, @HeaderParam("final") Double fin)
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			List<Preferencia> v = tm.preferenciaBuscarPreferenciaPorRango(ini, fin);
			return Response.status( 200 ).entity( v ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
    /**
     * Metodo que expone servicio REST usando POST que agrega la preferencia que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/PreferenciaAndes/rest/preferencias/preferencia
     * @param preferencia - preferencia a agregar
     * @return Json con la preferencia que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id: \\d+}")
	public Response addPreferencia(@PathParam("id")Long id,Preferencia preferencia) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.preferenciaAddPreferencia(id,preferencia);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(preferencia).build();
	}

	
    /**
     * Metodo que expone servicio REST usando PUT que actualiza la preferencia que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/PreferenciaAndes/rest/preferencias
     * @param preferencia - preferencia a actualizar. 
     * @return Json con la preferencia que actualizo o Json con el error que se produjo
     */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id: \\d+}")
	public Response updatePreferencia(@PathParam("id") Long id,Preferencia preferencia) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.preferenciaActualizarPreferenciasDePrecioDeUsuario(id,preferencia);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(preferencia).build();
	}
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina la preferencia que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/PreferenciaAndes/rest/preferencias
     * @param preferencia - preferencia a aliminar. 
     * @return Json con la preferencia que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id: \\d+}")
	public Response deletePreferencia(@PathParam("id") Long id,Preferencia preferencia) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.preferenciaDeletePreferencia(id,preferencia);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(preferencia).build();
	}


}
