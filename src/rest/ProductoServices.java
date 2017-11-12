/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: ProductoAndes
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

import rfc.ContenedoraCriterios;
import rfc.ContenedoraInformacion;
import tm.RotondAndesTM;
import vos.Producto;
import vos.UsuarioMinimum;
import vos.UsuarioMinimum.Rol;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/ProductoAndes/rest/productos/...
 * @author s.guzmanm
 */
@Path("productos")
public class ProductoServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los productos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/ProductoAndes/rest/productos
	 * @return Json con todos los productos de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProductos() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Producto> productos;
		try {
			productos = tm.productoDarProductos();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el producto con el id que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/ProductoAndes/rest/productos/<<id>>" para la busqueda"
     * @param name - Nombre del producto a buscar que entra en la URL como parametro 
     * @return Json con el/los productos encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getProducto( @PathParam( "id" ) Long id )
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			Producto v = tm.productoBuscarProductoPorId( id );
			if(v==null)return Response.status( 404 ).entity( v ).build( );			

			return Response.status( 200 ).entity( v ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

    /**
     * Metodo que expone servicio REST usando GET que busca el producto con el nombre que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/ProductoAndes/rest/productos/nombre/nombre?nombre=<<nombre>>" para la busqueda"
     * @param name - Nombre del producto a buscar que entra en la URL como parametro 
     * @return Json con el/los productos encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{nombre}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getProductoName( @QueryParam("nombre") String name) {
		name=name.replaceAll(RotondAndesTM.SPACE, " ");
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Producto> productos;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre del producto no valido");
			productos = tm.productoBuscarProductoPorName(name);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el producto con el tipo que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/ProductoAndes/rest/productos/tipo/tipo?tipo=<<nombre>>" para la busqueda"
     * @param name - Nombre del producto a buscar que entra en la URL como parametro 
     * @return Json con el/los productos encontrados con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{tipo}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getProductoTipo( @QueryParam("tipo") String tipo) {
		tipo=tipo.replaceAll(RotondAndesTM.SPACE, " ");

		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Producto> productos;
		try {
			if (tipo == null || tipo.length() == 0)
				throw new Exception("Nombre del producto no valido");
			productos = tm.productoDarProductosPorTipo(tipo);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}
    /**
     * Metodo que expone servicio REST usando POST que agrega el producto que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/ProductoAndes/rest/productos/producto
     * @param producto - producto a agregar
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el producto que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProducto(Producto producto, @HeaderParam("usuarioId")Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.productoAddProducto(producto);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(producto).build();
	}
	
    /**
     * Metodo que expone servicio REST usando PUT que actualiza el producto que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/ProductoAndes/rest/productos
     * @param producto - producto a actualizar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el producto que actualizo o Json con el error que se produjo
     */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateProducto(Producto producto, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.productoUpdateProducto(producto);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(producto).build();
	}
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el producto que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/ProductoAndes/rest/productos
     * @param producto - producto a aliminar. 
     * @param usuarioId Id del usuario que realiza la solicitud.
     * @return Json con el producto que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteProducto(Producto producto, @HeaderParam("usuarioId") Long usuarioId) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			UsuarioMinimum u = tm.usuarioBuscarUsuarioMinimumPorId( usuarioId );
			if(!(u.getRol().equals(Rol.OPERADOR)))
			{
				throw new Exception("El usuario no tiene permitido usar el sistema");
			}
			tm.productoDeleteProducto(producto);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(producto).build();
	}

	//RFC1
	/**
	 * Genera un filtro por nombre de zona con las características que prefiere el usuario.<br>
	 * @param name Nombre de la zona.<br>
	 * @param c Criterios del usuario.<br>
	 * @return Json con la información deseada.
	 */
	@POST
	@Path("completo/{nombre}")
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pruebaFiltros( @PathParam("nombre") String name, ContenedoraCriterios c) {
		name=name.replaceAll(RotondAndesTM.SPACE, " ");
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<ContenedoraInformacion> zonas;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre de la zona no valido");
			zonas = tm.criteriosOrganizarPorProductoUniversal(name,c.getOrden(), c.getAgrupacion(), c.getAgregacion(), c.getWhere(), c.getHaving());
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zonas).build();
	}
	/**
	 * Genera un filtro por todos los productos con las características que prefiere el usuario.<br>
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
			zonas = tm.criteriosOrganizarPorProductosComoSeQuiera(c.getOrden(), c.getAgrupacion(), c.getAgregacion(), c.getWhere(), c.getHaving());
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zonas).build();
	}
	
	//RFC4
	/**
	 * Metodo que expone servicio REST usando GET que da los productos m�s ofrecidos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/ProductoAndes/rest/productos/mas-ofrecidos
	 * @return Json con todos los productos de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Path("mas-ofrecidos")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProductosMasOfrecidos() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Producto> productos;
		try {
			productos = tm.productoDarProductosMasOfrecidos();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}
	
	//RFC6
	/**
	 * Metodo que expone servicio REST usando GET que da los productos m�s vendidos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/ProductoAndes/rest/productos/mas-vendidos
	 * @return Json con todos los productos de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Path("mas-vendidos")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProductosMasVendidos() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Producto> productos;
		try {
			productos = tm.productoDarProductosMasVendidos();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}
	
	/**
	 * Metodo que expone servicio REST usando GET que da los productos m�s vendidos de una zona particular.
	 * <b>URL: </b> http://"ip o nombre de host":8080/ProductoAndes/rest/productos/mas-vendidos/nombreZona
	 * @return Json con todos los productos de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Path("mas-vendidos/{nombreZona}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProductosMasVendidosPorZona(@PathParam("nombreZona") String nombreZona) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Producto> productos;
		try {
			productos = tm.productoDarProductosMasVendidosPorZona(nombreZona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}
	
	/**
	 * Metodo que expone servicio REST usando GET que da los productos m�s vendidos en un dia particular.
	 * <b>URL: </b> http://"ip o nombre de host":8080/ProductoAndes/rest/productos/mas-vendidos/dia
	 * @return Json con todos los productos de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Path("mas-vendidos-dia/{dia}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProductosMasVendidosPorDia(@PathParam("dia") String dia) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Producto> productos;
		try {
			productos = tm.productoDarProductosMasVendidosPorDiaDeLaSemana(dia);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}
	
	/**
	 * Metodo que expone servicio REST usando GET que da los productos m�s vendidos en un dia particular.
	 * <b>URL: </b> http://"ip o nombre de host":8080/ProductoAndes/rest/productos/menos-vendidos/dia
	 * @return Json con todos los productos de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Path("menos-vendidos-dia/{dia}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProductosMenosVendidosPorDia(@PathParam("dia") String dia) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Producto> productos;
		try {
			productos = tm.productoDarProductosMenosVendidosPorDiaDeLaSemana(dia);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}




}
