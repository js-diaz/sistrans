
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.InfoIngRest;
import vos.InfoProdRest;
import vos.Menu;
import vos.Restaurante;
import vos.Usuario;
import vos.UsuarioMinimum.Rol;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/RestauranteAndes/rest/restaurantes/...
 * @author js.diaz
 */
@Path("restaurantes")
public class RestauranteServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los restaurantes de la base de datos.
	 * @return Json con todos los restaurantes de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRestaurantes() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Restaurante> restaurantes;
		try {
			restaurantes = tm.restauranteDarRestaurantes();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(restaurantes).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el restaurante con el id que entra como parametro
     * @param nombre - Nombre del restaurante a buscar que entra en la URL como parametro 
     * @return Json con el/los restaurantes encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{nombre: [a-zA-Z]+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getRestaurante( @PathParam( "nombre" ) String nombre )
	{
		nombre=nombre.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			Restaurante v = tm.restauranteBuscarRestaurantePorNombre( nombre );
			if(v==null) return Response.status( 404 ).entity( v ).build( );			
			return Response.status( 200 ).entity( v ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


    /**
     * Metodo que expone servicio REST usando POST que agrega el restaurante que recibe en Json
     * @param restaurante - restaurante a agregar
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el restaurante que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRestaurante(Restaurante restaurante, @HeaderParam("usuarioId") Long id) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR))) 
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			tm.restauranteAddRestaurante(restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(restaurante).build();
	}
	
    /**
     * Metodo que expone servicio REST usando PUT que modifica un restaurante.
     * @param nombre nombre del restaurante a modificar.
     * @param restaurante informaci�n del restaurante modificado.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el restaurante que elimino o Json con el error que se produjo
     */
	@PUT
	@Path( "{nombre: [a-zA-Z]+}" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRestaurante(Restaurante restaurante, @HeaderParam("usuarioId") Long id, @PathParam("nombre") String nombre) {
		nombre=nombre.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.LOCAL) && id == tm.restauranteBuscarRestaurantePorNombre(nombre).getRepresentante().getId())))
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			restaurante.setNombre(nombre);
			tm.restauranteUpdateRestaurante(restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(restaurante).build();
	}

	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el restaurante que recibe en Json
     * @param nombre nombre del restaurante a eliminar.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el restaurante que elimino o Json con el error que se produjo
     */
	@DELETE
	@Path( "{nombre: [a-zA-Z]+}" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRestaurante(@HeaderParam("usuarioId") Long id, @PathParam("nombre") String nombre) {
		nombre=nombre.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.LOCAL) && id == tm.restauranteBuscarRestaurantePorNombre(nombre).getRepresentante().getId()))) 
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			tm.restauranteDeleteRestaurante(nombre);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity("Objeto borrado correctamente").build();
	}
	
	
	//Subrecurso menu
	/** Metodo que expone servicio REST usando GET que da todos los menus de la base de datos para un restaurante particular.
	 * @param nombreRestaurante nombre del restaurante.
	 * @return Json con todos los menus de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Path( "{nombreRestaurante: [a-zA-Z]+}/menus" )
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getMenus(@PathParam("nombreRestaurante") String nombreRestaurante) {
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Menu> menus;
		try {
			menus = tm.menuDarMenusPorRestaurante(nombreRestaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(menus).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el menu con el id que entra como parametro.
     * @param nombreRestaurante nombre del restaurante al cual pertenece el menu.
     * @param name Nombre del menu a buscar que entra en la URL como parametro.
     * @return Json con el/los menus encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{nombreRestaurante: [a-zA-Z]+}/menus/{nombre: [a-zA-Z]+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getMenu( @PathParam( "nombre" ) String nombre, @PathParam("nombreRestaurante") String nombreRestaurante )
	{
		nombre=nombre.replaceAll(RotondAndesTM.SPACE, " ");
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			Menu v = tm.menuBuscarMenusPorNombreYRestaurante(nombre, nombreRestaurante);
			if(v == null) 
				return Response.status( 404 ).entity( v ).build( );
			return Response.status( 200 ).entity( v ).build( );
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


    /**
     * Metodo que expone servicio REST usando POST que agrega el menu que recibe en Json
     * @param nombreRestaurante nombre del restaurante al cual agregarlo.
     * @param menu - menu a agregar.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el menu que agrego o Json con el error que se produjo
     */
	@POST
	@Path( "{nombreRestaurante: [a-zA-Z]+}/menus" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMenu(@PathParam("nombreRestaurante") String nombreRestaurante, Menu menu, @HeaderParam("usuarioId") Long id) {
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.LOCAL) && id == tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante).getRepresentante().getId()))) 
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			menu.setRestaurante(tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante));
			tm.menuAddMenu(menu);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(menu).build();
	}
	
    /**
     * Metodo que expone servicio REST usando PUT que modifica un restaurante.
     * @param nombre nombre del menu a modificar.
     * @param nombreRestaurante nombre del restaurante que lo contiene.
     * @param menu informaci�n del menu modificado.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el restaurante que elimino o Json con el error que se produjo
     */
	@PUT
	@Path( "{nombreRestaurante: [a-zA-Z]+}/menus/{nombre: [a-zA-Z]+}" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMenu(Menu menu, @HeaderParam("usuarioId") Long id, @PathParam("nombre") String nombre, @PathParam("nombreRestaurante") String nombreRestaurante) {
		nombre=nombre.replaceAll(RotondAndesTM.SPACE, " ");
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.LOCAL) && id == tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante).getRepresentante().getId())))
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			menu.setNombre(nombre);
			menu.setRestaurante(tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante));
			tm.menuUpdateMenu(menu);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(menu).build();
	}

	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina un menu de un restaurante dado.
     * @param nombre nombre del menu a eliminar.
     * @param nombreRestaurante nombre del restaurante que lo contiene.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el menu que elimino o Json con el error que se produjo
     */
	@DELETE
	@Path( "{nombreRestaurante: [a-zA-Z]+}/menus/{nombre: [a-zA-Z]+}" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMenu(@HeaderParam("usuarioId") Long id, @PathParam("nombreRestaurante") String nombreRestaurante, @PathParam("nombre") String nombre) {
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");
		nombre=nombre.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.LOCAL) && id == tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante).getRepresentante().getId())))
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			tm.menuDeleteMenu(nombre, nombreRestaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity("menu eliminado correctamente").build();
	}
	
	//Subrecurso infoProdRest
	/** Metodo que expone servicio REST usando GET que da todos los productos de la base de datos para un restaurante particular.
	 * @param nombreRestaurante nombre del restaurante.
	 * @return Json con todos los productos de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Path( "{nombreRestaurante: [a-zA-Z]+}/productos" )
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getInfoProdRests(@PathParam("nombreRestaurante") String nombreRestaurante) {
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<InfoProdRest> productos;
		try {
			productos = tm.infoProdRestDarInfoProdRestsPorRestaurante(nombreRestaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el infoProdRest con el id que entra como parametro.
     * @param nombreRestaurante nombre del restaurante al cual pertenece el infoProdRest.
     * @param id Id del producto a buscar que entra en la URL como parametro.
     * @return Json con el/los productos encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{nombreRestaurante: [a-zA-Z]+}/productos/{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getInfoProdRest( @PathParam( "id" ) Long id, @PathParam("nombreRestaurante") String nombreRestaurante )
	{
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			InfoProdRest v = tm.infoProdRestBuscarInfoProdRestsPorIdYRestaurante(id, nombreRestaurante);
			if(v == null) 
				return Response.status( 404 ).entity( v ).build( );
			return Response.status( 200 ).entity( v ).build( );
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


    /**
     * Metodo que expone servicio REST usando POST que agrega el infoProdRest que recibe en Json
     * @param nombreRestaurante nombre del restaurante al cual agregarlo.
     * @param infoProdRest - infoProdRest a agregar.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el infoProdRest que agrego o Json con el error que se produjo
     */
	@POST
	@Path( "{nombreRestaurante: [a-zA-Z]+}/productos" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addInfoProdRest(@PathParam("nombreRestaurante") String nombreRestaurante, InfoProdRest infoProdRest, @HeaderParam("usuarioId") Long id) {
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.LOCAL) && id == tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante).getRepresentante().getId()))) 
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			infoProdRest.setRestaurante(tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante));
			tm.infoProdRestAddInfoProdRest(infoProdRest);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(infoProdRest).build();
	}
	
    /**
     * Metodo que expone servicio REST usando PUT que modifica un restaurante.
     * @param id id del producto a modificar.
     * @param nombreRestaurante nombre del restaurante que lo contiene.
     * @param infoProdRest informaci�n del infoProdRest modificado.
     * @param idUsuario Id del usuario que realiza la solicitud.
     * @return Json con el restaurante que elimino o Json con el error que se produjo
     */
	@PUT
	@Path( "{nombreRestaurante: [a-zA-Z]+}/productos/{id: \\d+}" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateInfoProdRest(InfoProdRest infoProdRest, @HeaderParam("usuarioId") Long idUsuario, @PathParam("id") Long id, @PathParam("nombreRestaurante") String nombreRestaurante) {
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(idUsuario);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.LOCAL) && idUsuario == tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante).getRepresentante().getId())))
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			infoProdRest.setProducto(tm.productoBuscarProductoPorId(id));
			infoProdRest.setRestaurante(tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante));
			tm.infoProdRestUpdateInfoProdRest(infoProdRest);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(infoProdRest).build();
	}

	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina un infoProdRest de un restaurante dado.
     * @param id Id del producto a eliminar.
     * @param nombreRestaurante nombre del restaurante que lo contiene.
     * @param idUsuario Id del usuario que realiza la solicitud.
     * @return Json con el infoProdRest que elimino o Json con el error que se produjo
     */
	@DELETE
	@Path( "{nombreRestaurante: [a-zA-Z]+}/productos/{id: \\d+}" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteInfoProdRest(@HeaderParam("usuarioId") Long idUsuario, @PathParam("nombreRestaurante") String nombreRestaurante, @PathParam("id") Long id) {
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(idUsuario);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.LOCAL) && idUsuario == tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante).getRepresentante().getId())))
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			tm.infoProdRestDeleteInfoProdRest(id, nombreRestaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity("infoProdRest eliminado correctamente").build();
	}
	
	//Subrecurso infoIngRest
	/** Metodo que expone servicio REST usando GET que da todos los ingredientes de la base de datos para un restaurante particular.
	 * @param nombreRestaurante nombre del restaurante.
	 * @return Json con todos los ingredientes de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Path( "{nombreRestaurante: [a-zA-Z]+}/ingredientes" )
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getInfoIngRests(@PathParam("nombreRestaurante") String nombreRestaurante) {
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<InfoIngRest> ingredientes;
		try {
			ingredientes = tm.infoIngRestDarInfoIngRestsPorRestaurante(nombreRestaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(ingredientes).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el infoIngRest con el id que entra como parametro.
     * @param nombreRestaurante nombre del restaurante al cual pertenece el infoIngRest.
     * @param id Id del ingrediente a buscar que entra en la URL como parametro.
     * @return Json con el/los ingredientes encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{nombreRestaurante: [a-zA-Z]+}/ingredientes/{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getInfoIngRest( @PathParam( "id" ) Long id, @PathParam("nombreRestaurante") String nombreRestaurante )
	{
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			InfoIngRest v = tm.infoIngRestBuscarInfoIngRestsPorIdYRestaurante(id, nombreRestaurante);
			if(v == null) 
				return Response.status( 404 ).entity( v ).build( );
			return Response.status( 200 ).entity( v ).build( );
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


    /**
     * Metodo que expone servicio REST usando POST que agrega el infoIngRest que recibe en Json
     * @param nombreRestaurante nombre del restaurante al cual agregarlo.
     * @param infoIngRest - infoIngRest a agregar.
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el infoIngRest que agrego o Json con el error que se produjo
     */
	@POST
	@Path( "{nombreRestaurante: [a-zA-Z]+}/ingredientes" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addInfoIngRest(@PathParam("nombreRestaurante") String nombreRestaurante, InfoIngRest infoIngRest, @HeaderParam("usuarioId") Long id) {
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.LOCAL) && id == tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante).getRepresentante().getId()))) 
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			infoIngRest.setRestaurante(tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante));
			tm.infoIngRestAddInfoIngRest(infoIngRest);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(infoIngRest).build();
	}
	
    /**
     * Metodo que expone servicio REST usando PUT que modifica un restaurante.
     * @param id id del ingrediente a modificar.
     * @param nombreRestaurante nombre del restaurante que lo contiene.
     * @param infoIngRest informaci�n del infoIngRest modificado.
     * @param idUsuario Id del usuario que realiza la solicitud.
     * @return Json con el restaurante que elimino o Json con el error que se produjo
     */
	@PUT
	@Path( "{nombreRestaurante: [a-zA-Z]+}/ingredientes/{id: \\d+}" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateInfoIngRest(InfoIngRest infoIngRest, @HeaderParam("usuarioId") Long idUsuario, @PathParam("id") Long id, @PathParam("nombreRestaurante") String nombreRestaurante) {
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(idUsuario);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.LOCAL) && idUsuario == tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante).getRepresentante().getId())))
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			infoIngRest.setIngrediente(tm.ingredienteBuscarIngredientePorId(id));
			infoIngRest.setRestaurante(tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante));
			tm.infoIngRestUpdateInfoIngRest(infoIngRest);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(infoIngRest).build();
	}

	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina un infoIngRest de un restaurante dado.
     * @param id Id del ingrediente a eliminar.
     * @param nombreRestaurante nombre del restaurante que lo contiene.
     * @param idUsuario Id del usuario que realiza la solicitud.
     * @return Json con el infoIngRest que elimino o Json con el error que se produjo
     */
	@DELETE
	@Path( "{nombreRestaurante: [a-zA-Z]+}/ingredientes/{id: \\d+}" )
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteInfoIngRest(@HeaderParam("usuarioId") Long idUsuario, @PathParam("nombreRestaurante") String nombreRestaurante, @PathParam("id") Long id) {
		nombreRestaurante=nombreRestaurante.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		Usuario u =null;
		try {
			u=tm.usuarioBuscarUsuarioPorId(idUsuario);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR) || (u.getRol().equals(Rol.LOCAL) && idUsuario == tm.restauranteBuscarRestaurantePorNombre(nombreRestaurante).getRepresentante().getId())))
				throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			tm.infoIngRestDeleteInfoIngRest(id, nombreRestaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity("infoIngRest eliminado correctamente").build();
	}

}
