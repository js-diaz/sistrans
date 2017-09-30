
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
import vos.CondicionTecnica;
import vos.Zona;
import vos.ZonaMinimum;
import vos.UsuarioMinimum.Rol;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/condiciones/zonas...
 * @author s.guzmanm
 */
@Path("condiciones/zonas")
public class CondicionZonaServices {

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
	 * @param zona Zona a consultar condiciones.
	 * @return Json con todos los zonas de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCondicionesZonasPorZona(@HeaderParam("zona")String zona) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<CondicionTecnica> zonas;
		try {
			if (zona == null || zona.length() == 0)
				throw new Exception("Nombre de la zona no válido");
			zonas = tm.condicionZonaConsultarZona(zona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		if(zonas.isEmpty())return Response.status( 404 ).entity( zonas ).build( );			

		return Response.status(200).entity(zonas).build();
	}

    /**
     * Metodo que expone servicio REST usando GET que busca la zona con el nombre que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/zonas/nombre/nombre?nombre=<<nombre>>" para la busqueda"
     * @param condición - Nombre de la condición a buscar que entra en la URL como parametro 
     * @return Json con el/los zonas encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getCondicionesZonaPorCategoria( @HeaderParam("categoria") String condicion) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Zona> zonas;
		try {
			if (condicion == null || condicion.length() == 0)
				throw new Exception("Nombre de la categoría no válido");
			zonas = tm.condicionZonaConsultarPorCondicion(condicion);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		if(zonas.isEmpty())return Response.status( 404 ).entity( zonas ).build( );			
		return Response.status(200).entity(zonas).build();
	}
    /**
     * Metodo que expone servicio REST usando POST que agrega la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/zonas/zona
     * @param zona - zona a agregar
     * @param condiciones- Listado de condiciones a agregar
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con la zona que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCondicionesZonaPorZona( List<CondicionTecnica> condiciones, @HeaderParam("zona") String zona,@HeaderParam("usuarioId")Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Usuario u = tm.usuarioBuscarUsuarioPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)) || !(u.getRol().equals(Rol.ORGANIZADORES)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.condicionZonaInsertarPorZona(zona, condiciones);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(condiciones).build();
	}
	
	/**
     * Metodo que expone servicio REST usando POST que agrega la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/zonas/zona
     * @param zonas - zonas a agregar
     * @param condicion - Condición de la zona.
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con la zona que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCondicionesZonaPorCondicion( List<Zona> zonas, @HeaderParam("condicion") String condicion,@HeaderParam("usuarioId")Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Usuario u = tm.usuarioBuscarUsuarioPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)) || !(u.getRol().equals(Rol.ORGANIZADORES)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.condicionZonaInsertarPorCondicion(condicion, zonas);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zonas).build();
	}
	
	/**
     * Metodo que expone servicio REST usando POST que agrega la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/zonas/zona
     * @param zona - zona a agregar
     * @param condicion - condición a agregar
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con la zona que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCondicionesZona( @HeaderParam("condicion") String condicion, @HeaderParam("zona") String zona,@HeaderParam("usuarioId")Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Usuario u = tm.usuarioBuscarUsuarioPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)) || !(u.getRol().equals(Rol.ORGANIZADORES)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.condicionZonaInsertarPorCondicionYZona(condicion, zona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zona).build();
	}
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/zonas
     * @param zona - zona a aliminar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con la zona que elimino o Json con el error que se produjo
     */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCondicionesZonaPorZona(@HeaderParam("zona")String zona, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Usuario u = tm.usuarioBuscarUsuarioPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)) || !(u.getRol().equals(Rol.ORGANIZADORES)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.condicionZonaEliminarCondicionesPorZona(zona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).build();
	}

	 /**
     * Metodo que expone servicio REST usando DELETE que elimina la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/zonas
     * @param condicion - condición a eliminar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con la zona que elimino o Json con el error que se produjo
     */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCondicionesZonaPorCondicion(@HeaderParam("condicion")String condicion, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Usuario u = tm.usuarioBuscarUsuarioPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)) || !(u.getRol().equals(Rol.ORGANIZADORES)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.condicionZonaEliminarCondicionesPorCondicion(condicion);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).build();
	}
	
	 /**
     * Metodo que expone servicio REST usando DELETE que elimina la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/CondicionesZonaAndes/rest/zonas
     * @param zona - zona a aliminar. 
     * @param condicion - condición a eliminar.
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con la zona que elimino o Json con el error que se produjo
     */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCondicionesZona(@HeaderParam("zona")String zona, @HeaderParam("condicion") String condicion,@HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Usuario u = tm.usuarioBuscarUsuarioPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)) || !(u.getRol().equals(Rol.ORGANIZADORES)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.condicionZonaEliminarCondicion(zona, condicion);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).build();
	}

}
