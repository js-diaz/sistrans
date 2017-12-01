
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

import rfc.ListaObjetos;
import tm.RotondAndesTM;
import vos.Categoria;
import vos.Usuario;
import vos.UsuarioMinimum;
import vos.UsuarioMinimum.Rol;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/CategoriaAndes/rest/categorias/...
 * @author Monitores 2017-20
 */
@Path("distributed")
public class RotondAndesDistributedServices {

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
	@POST
	@Path("RFC13")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	//Falta acomodarlo al json de ramos
	public Response consultarProductos(Object jsonRamos) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<ListaObjetos> list;
		try {
			list = tm.consultarProductos();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(list).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el video con el id que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/CategoriaAndes/rest/videos/<<id>>" para la busqueda"
     * @param name - Nombre del video a buscar que entra en la URL como parametro 
     * @return Json con el/los videos encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@POST
	@Path("RF19")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response retirarRestaurante( @PathParam( "name" ) String name )
	{
		name=name.replaceAll(RotondAndesTM.SPACE, " ");
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			if(name==null || name.length()==0)
				throw new Exception("El nombre de la categoría es inválido");
			tm.retirarRestaurante(name);
			return Response.status( 200 ).build( );			
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
	@Path("RF18")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response pedidoPorMesa(@HeaderParam("esProducto") boolean esProducto, @HeaderParam("usuarioId") Long id,String mensaje) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		UsuarioMinimum u =null;
		try {
			u=tm.usuarioBuscarUsuarioMinimumPorId(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		try {
			if(!(u.getRol().equals(Rol.OPERADOR))) throw new Exception("El usuario no tiene los permisos para ingresar a esta funcionalidad");
			if(esProducto)
				tm.registrarPedidoProdMesa();
			else
				tm.registrarPedidoMenuMesa();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).build();
	}
	//Falta el json de ramos para usar esta funcionalidad
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el video que recibe en Json
     * @param video - video a aliminar. 
     * @param id_ Id del usuario que realiza la solicitud.
     * @return Json con el video que elimino o Json con el error que se produjo
     */
	@POST
	@Path("RFC14")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarRentabilidad(@HeaderParam("usuarioId") Long id,Object jsonDeRamos) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<ListaObjetos> lista;
		try {
			Usuario u = tm.usuarioBuscarUsuarioPorId( id );
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
			lista=tm.consultarRentabilidadZona();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(lista).build();
	}


}