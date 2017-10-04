
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

import org.codehaus.jackson.*;

import rfc.ContenedoraCriterios;
import rfc.ContenedoraInformacion;
import rfc.ContenedoraZonaCategoriaProducto;
import rfc.Criterio;
import rfc.CriterioVerdad;
import rfc.LimiteFechas;
import tm.RotondAndesTM;
import vos.Usuario;
import vos.UsuarioMinimum;
import vos.Zona;
import vos.UsuarioMinimum.Rol;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/ZonaAndes/rest/zonas/...
 * @author s.guzmanm
 */
@Path("zonas")
public class ZonaServices {

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
	 * <b>URL: </b> http://"ip o nombre de host":8080/ZonaAndes/rest/zonas
	 * @return Json con todos los zonas de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getZonas() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Zona> zonas;
		try {
			zonas = tm.zonaDarZonas();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zonas).build();
	}

    /**
     * Metodo que expone servicio REST usando GET que busca la zona con el nombre que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/ZonaAndes/rest/zonas/nombre/nombre?nombre=<<nombre>>" para la busqueda"
     * @param name - Nombre dla zona a buscar que entra en la URL como parametro 
     * @return Json con el/los zonas encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{nombre :[a-zA-Z]+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getZonaName( @PathParam("nombre") String name) {
		name=name.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Zona zonas;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre dla zona no valido");
			zonas = tm.zonaBuscarZonasPorName(name);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		if(zonas==null)return Response.status( 404 ).entity( zonas ).build( );			

		return Response.status(200).entity(zonas).build();
	}


    /**
     * Metodo que expone servicio REST usando POST que agrega la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/ZonaAndes/rest/zonas/zona
     * @param zona - zona a agregar
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con la zona que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addZona(Zona zona, @HeaderParam("usuarioId")Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR) || u.getRol().equals(Rol.ORGANIZADORES)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.zonaAddZona(zona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zona).build();
	}
	
    /**
     * Metodo que expone servicio REST usando PUT que actualiza la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/ZonaAndes/rest/zonas
     * @param zona - zona a actualizar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con la zona que actualizo o Json con el error que se produjo
     */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateZona(Zona zona, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR) ||u.getRol().equals(Rol.ORGANIZADORES)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.zonaUpdateZona(zona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zona).build();
	}
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina la zona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/ZonaAndes/rest/zonas
     * @param zona - zona a aliminar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con la zona que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteZona(Zona zona, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)|| u.getRol().equals(Rol.ORGANIZADORES)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.zonaDeleteZona(zona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zona).build();
	}
	/**
	 * Genera un filtro por nombre de zona con las características que prefiere el usuario.<br>
	 * @param name Nombre de la zona.<br>
	 * @param c Criterios del usuario.<br>
	 * @return Json con la información deseada.
	 */
	@POST
	@Path("completo/{nombre: [a-zA-Z]+}")
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pruebaFiltros( @PathParam("nombre") String name, ContenedoraCriterios c) {
		name=name.replaceAll(RotondAndesTM.SPACE, " ");
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<ContenedoraInformacion> zonas;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre de la zona no valido");
			zonas = tm.criteriosOrganizarPorZonaUniversal(name,c.getOrden(), c.getAgrupacion(), c.getAgregacion(), c.getWhere(), c.getHaving());
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zonas).build();
	}
	/**
	 * Genera un filtro por todas las zonas con las características que prefiere el usuario.<br>
	 * @param c Criterios del usuario.<br>
	 * @return Json con la información deseada.
	 */
	@POST
	@Path("completo")
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pruebaFiltrosMultiples( ContenedoraCriterios c) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<ContenedoraInformacion> zonas;
		try {
			zonas = tm.criteriosOrganizarPorZonasComoSeQuiera(c.getOrden(), c.getAgrupacion(), c.getAgregacion(), c.getWhere(), c.getHaving());
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zonas).build();
	}
	/**
	 * Genera una nueva rentabilidad de la zona. Si se tiene un local se respeta y se maneja la privacidad del mismo.<br>
	 * @param limite Objeto de límite de fechas.<br>
	 * @param usuarioId Id del usuario.<br>
	 * @return Rentabilidad de la zona.
	 */
	@POST
	@Path("rentabilidad")
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response rentabilidadDeZona( LimiteFechas limite,@HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<ContenedoraZonaCategoriaProducto> zonas;
		try {
			Usuario u = tm.usuarioBuscarUsuarioPorId( usuarioId );
			boolean restaurante=false;
			if( u.getRol().equals(Rol.LOCAL))
			{
				if(u.getRestaurante()==null) throw new Exception("EL local no tiene restaurante");
				restaurante=true;
			}
			if(!(u.getRol().equals(Rol.OPERADOR) || restaurante))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			String nombre=null;
			if(restaurante) nombre=u.getRestaurante().getNombre();
			zonas = tm.zonaDarProductosTotalesPorZonaYCategoria(limite.getFechaInicial(), limite.getFechaFinal(), nombre);
			if(zonas.isEmpty()) return Response.status(404).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zonas).build();
	}


}
