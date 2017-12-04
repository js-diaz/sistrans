package dtm;

import java.io.IOException;



import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;

import jms.*;
import rfc.ContenedoraInformacion;
import rfc.ContenedoraZonaCategoriaProducto;
import rfc.Criterio;
import rfc.CriterioOrden;
import rfc.CriterioVerdad;
import rfc.ListaObjetos;
import tm.RotondAndesTM;
import vos.ListaVideos;

public class RotondAndesDistributed 
{
	private final static String QUEUE_NAME = "java:global/RMQAppQueue";
	private final static String MQ_CONNECTION_NAME = "java:global/RMQClient";
	
	private static final String SPLIT_MESSAGE="...";
	
	private static final String SPLIT_PARAM=";;;";
	
	private static RotondAndesDistributed instance;
	
	private RotondAndesTM tm;
	
	private QueueConnectionFactory queueFactory;
	
	private TopicConnectionFactory factory;
	
	
	private ConsultarProdMDB consultarProdMQ;
	
	private ConsultarRentabilidadMDB consultarRentabilidadMQ;

	private PedidoMenuMDB pedidoMenuMQ;
	
	private PedidoProdMDB pedidoProdMQ;
	
	private RetirarRestauranteMDB retirarRestauranteMQ;
	
	private static String path;


	private RotondAndesDistributed() throws NamingException, JMSException
	{
		InitialContext ctx = new InitialContext();
		factory = (RMQConnectionFactory) ctx.lookup(MQ_CONNECTION_NAME);
		//Inicializicación mqs
		consultarProdMQ=new ConsultarProdMDB(factory,ctx);
		consultarRentabilidadMQ= new ConsultarRentabilidadMDB(factory,ctx);
		pedidoMenuMQ=new PedidoMenuMDB(factory,ctx);
		pedidoProdMQ=new PedidoProdMDB(factory,ctx);
		retirarRestauranteMQ=new RetirarRestauranteMDB(factory,ctx);
		
		
		//Start MQ
		consultarProdMQ.start();
		consultarRentabilidadMQ.start();
		pedidoMenuMQ.start();
		pedidoProdMQ.start();
		retirarRestauranteMQ.start();
		
	}
	
	public void stop() throws JMSException
	{
		consultarProdMQ.close();
		consultarRentabilidadMQ.close();
		pedidoMenuMQ.close();
		pedidoProdMQ.close();
		retirarRestauranteMQ.close();
	}
	
	/**
	 * Método que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	public static void setPath(String p) {
		path = p;
	}
	
	public void setUpTransactionManager(RotondAndesTM tm)
	{
	   this.tm = tm;
	}
	
	private static RotondAndesDistributed getInst()
	{
		return instance;
	}
	
	public static RotondAndesDistributed getInstance(RotondAndesTM tm)
	{
		if(instance == null)
		{
			try {
				instance = new RotondAndesDistributed();
			} catch (NamingException e) {
				e.printStackTrace();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		instance.setUpTransactionManager(tm);
		return instance;
	}
	
	public static RotondAndesDistributed getInstance()
	{
		if(instance == null)
		{
			RotondAndesTM tm = new RotondAndesTM(path);
			return getInstance(tm);
		}
		if(instance.tm != null)
		{
			return instance;
		}
		RotondAndesTM tm = new RotondAndesTM(path);
		return getInstance(tm);
	}
	//Para RFC13 usando RFC1
	public List<Object> consultarProductos(String msg) throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		return consultarProdMQ.consultarProductos(msg);
	}
	
	public List<ContenedoraInformacion> consultarProductosLocal(String s) throws Exception
	{
		String[]datos=s.split(SPLIT_MESSAGE);
		String inicial="";
		String terminal="";
		String nombreRestaurante="";
		String catProd="";
		try
		{
			 inicial=datos[0].split(SPLIT_PARAM)[1];
		}
		catch(Exception e)
		{
			
		}
		try
		{
			 terminal=datos[1].split(SPLIT_PARAM)[1];
		}
		catch(Exception e)
		{
			
		}
		try
		{
			 nombreRestaurante=datos[2].split(SPLIT_PARAM)[1];
		}
		catch(Exception e)
		{
			
		}
		try
		{
			 catProd=datos[3].split(SPLIT_PARAM)[1].toUpperCase();
		}
		catch(Exception e)
		{
			
		}
		Double precioMin=null;
		try
		{
			precioMin=Double.parseDouble(datos[4].split(SPLIT_PARAM)[1]);
		}
		catch(Exception e)
		{
			
		}
		Double precioMax=null;
		try
		{
			precioMax=Double.parseDouble(datos[5].split(SPLIT_PARAM)[1]);
		}
		catch(Exception e)
		{
			
		}
		
		ArrayList<CriterioOrden> criteriosOrganizacion=new ArrayList<>();
		ArrayList<Criterio> criteriosAgrupamiento=new ArrayList<Criterio>();
		CriterioVerdad where = new CriterioVerdad();
		CriterioVerdad temp=null;
		if(!inicial.equals("null"))
		{
			if (inicial.length()==0)
			{
				criteriosOrganizacion.add(new CriterioOrden(null, "FECHA_INICIO", true));
			}
			else 
			{
				criteriosAgrupamiento.add(new Criterio("FECHA_INICIO"));
				temp = new CriterioVerdad(new Criterio("FECHA_INICIO"),inicial,null,"=",true,null,null,null,null);
				where=new CriterioVerdad(null,null,null,null,true,null,where,temp,true);
			}
		}
		if(!terminal.equals("null"))
		{
			if (terminal.length()==0)
			{
				criteriosOrganizacion.add(new CriterioOrden(null, "FECHAFIN", true));
			}
			else 
			{
				criteriosAgrupamiento.add(new Criterio("FECHA_FIN"));
				temp = new CriterioVerdad(new Criterio("FECHA_FIN"),terminal,null,"=",true,null,null,null,null);
				where=new CriterioVerdad(null,null,null,null,true,null,where,temp,true);
			}
		}
		if(!nombreRestaurante.equals("null"))
		{
			if (nombreRestaurante.length()==0)
			{
				criteriosOrganizacion.add(new CriterioOrden(null, "NOMBRE_RESTAURANTE", true));
			}
			else 
			{
				criteriosAgrupamiento.add(new Criterio("NOMBRE_RESTAURANTE"));
				temp = new CriterioVerdad(new Criterio("NOMBRE_RESTAURANTE"),nombreRestaurante,null,"=",true,null,null,null,null);
				where=new CriterioVerdad(null,null,null,null,true,null,where,temp,true);
			}
		}
		if(!catProd.equals("null"))
		{
			if (catProd.length()==0)
			{
				criteriosOrganizacion.add(new CriterioOrden(null, "TIPO", true));
			}
			else 
			{
				criteriosAgrupamiento.add(new Criterio("TIPO"));
				temp = new CriterioVerdad(new Criterio("TIPO"),catProd,null,"=",true,null,null,null,null);
				where=new CriterioVerdad(null,null,null,null,true,null,where,temp,true);
			}
		}
		if(!precioMin.equals("null"))
		{
				criteriosAgrupamiento.add(new Criterio("PRECIO"));
				temp = new CriterioVerdad(new Criterio("PRECIO"),precioMin+"",null,">=",true,null,null,null,null);
				where=new CriterioVerdad(null,null,null,null,true,null,where,temp,true);
		}
		if(!precioMax.equals("null"))
		{
			criteriosAgrupamiento.add(new Criterio("PRECIO"));
			temp = new CriterioVerdad(new Criterio("PRECIO"),precioMax+"",null,"<=",true,null,null,null,null);
			where=new CriterioVerdad(null,null,null,null,true,null,where,temp,true);
		}
		return tm.criteriosOrganizarPorProductosComoSeQuiera(criteriosOrganizacion, criteriosAgrupamiento, null, where, null);
	}
	//Para RFC14 usando RFC5
	public List<Object> consultarRentabilidadZonaGlobal(String msg) throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		return consultarRentabilidadMQ.consultarRentabilidadZona(msg);
	}
	
	
	public List<ContenedoraZonaCategoriaProducto> consultarRentabilidadZonaLocal(String message) throws Exception
	{
		String[]datos=message.split(SPLIT_MESSAGE);
		Date fechaInicio=null;
		Date fechaFin=null;
		String nombreRestaurante=null;
		String[] valores=datos[0].split(SPLIT_PARAM);
		SimpleDateFormat x = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		if(valores[0].equals("fechaInicio"))
		{
			fechaInicio=x.parse(valores[1]);
		}
		valores=datos[1].split(SPLIT_PARAM);
		if(valores[0].equals("fechaFin"))
		{
			fechaFin=x.parse(valores[1]);
		}
		valores=datos[2].split(SPLIT_PARAM);
		if(valores[0].equals("nombreRestaurante"))
		{
			nombreRestaurante=(valores[1]);
		}
		return tm.zonaDarProductosTotalesPorZonaYCategoria(fechaInicio, fechaFin, nombreRestaurante);
	}
	
	//RF18
	public void pedidoProdMesaGlobal(String msj) throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		pedidoProdMQ.pedidoProdMesa(msj);
	}
	
	public String pedidoProdMesaLocal(String mensaje) throws Exception
	{
		String[]datos=mensaje.split(":");
		String mesa=datos[0];
		String correo=datos[1];
		ArrayList<String> list = new ArrayList<String>();
		datos=datos[1].split(";;;");
		for(String s:datos)
		{
			list.add(s);
		}
		try
		{
			tm.pedidoProdMesaNombre(list,mesa,correo);
			return "OK";
		}
		catch(Exception e)
		{
			return "NO OK";
		}
	}
	
	public void pedidoMenuMesaGlobal(String mensaje) throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		pedidoMenuMQ.pedidoMenuMesa(mensaje);
	}
	
	public String pedidoMenuMesaLocal(String mensaje) throws Exception
	{
		//Falta definir las entradas
		String[]datos=mensaje.split(":");
		String mesa=datos[0];
		String correo=datos[1];
		ArrayList<String> list = new ArrayList<String>();
		datos=datos[1].split(";;;");
		for(String s:datos)
		{
			list.add(s);
		}
		try
		{
			tm.pedidoMenuMesaNombre(list,mesa,correo);
			return "OK";
		}
		catch(Exception e)
		{
			return "NO OK";
		}
	}
	
	//RF19
	
	public String retirarRestauranteLocal(String nombreRestaurante) throws Exception
	{
		//Función para retirar el restaurante a nivel local
		try
		{
			tm.retirarRestauranteLocal(nombreRestaurante);
		}
		catch(Exception e)
		{
			return"NO OK";
		}
		return "OK";
	}
	
	public void retirarRestauranteGlobal(String nombreRestaurante) throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		retirarRestauranteMQ.retirarRestaurante(nombreRestaurante);
	}
	

	
	
}
