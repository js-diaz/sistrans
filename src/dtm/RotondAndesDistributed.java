package dtm;

import java.io.IOException;



import java.security.NoSuchAlgorithmException;
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
import rfc.ListaObjetos;
import tm.RotondAndesTM;
import vos.ListaVideos;

public class RotondAndesDistributed 
{
	private final static String QUEUE_NAME = "java:global/RMQAppQueue";
	private final static String MQ_CONNECTION_NAME = "java:global/RMQClient";
	
	private static RotondAndesDistributed instance;
	
	private RotondAndesTM tm;
	
	private QueueConnectionFactory queueFactory;
	
	private TopicConnectionFactory factory;
	
	private AllVideosMDB allVideosMQ;
	
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
		allVideosMQ = new AllVideosMDB(factory, ctx);
		consultarProdMQ=new ConsultarProdMDB(factory,ctx);
		consultarRentabilidadMQ= new ConsultarRentabilidadMDB(factory,ctx);
		pedidoMenuMQ=new PedidoMenuMDB(factory,ctx);
		pedidoProdMQ=new PedidoProdMDB(factory,ctx);
		retirarRestauranteMQ=new RetirarRestauranteMDB(factory,ctx);
		
		
		//Start MQ
		allVideosMQ.start();
		consultarProdMQ.start();
		consultarRentabilidadMQ.start();
		pedidoMenuMQ.start();
		pedidoProdMQ.start();
		retirarRestauranteMQ.start();
		
	}
	
	public void stop() throws JMSException
	{
		allVideosMQ.close();
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
	//Ejemplo
	public ListaVideos getLocalVideos() throws Exception
	{
		return tm.darVideosLocal();
	}
	
	public ListaVideos getRemoteVideos() throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		return allVideosMQ.getRemoteVideos();
	}
	//Para RFC13 usando RFC1
	public List<Object> consultarProductos(String restaurante, boolean esProd) throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		return consultarProdMQ.consultarProductos();
	}
	
	public List<ContenedoraInformacion> consultarProductosLocal() throws Exception
	{
		//Aquí se debe hacer el mapping con la info que den ramos y mauricio
		//return tm.criteriosOrganizarPorProductosComoSeQuiera(criteriosOrganizacion, criteriosAgrupamiento, agregaciones, where, having)(restaurante, esProd);
		return null;
	}
	//Para RFC14 usando RFC5
	public List<Object> consultarRentabilidadZonaGlobal() throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		return consultarRentabilidadMQ.consultarRentabilidadZona();
	}
	
	
	public List<ContenedoraZonaCategoriaProducto> consultarRentabilidadZonaLocal() throws Exception
	{
		//Aquñi se debe hacer el mapping con la info de ramos y mauricio
		//return tm.zonaDarProductosTotalesPorZonaYCategoria(null, null, null);
		return null;
	}
	
	//RF18
	public void pedidoProdMesaGlobal() throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		pedidoProdMQ.pedidoProdMesa();
	}
	
	public void pedidoProdMesaLocal() throws Exception
	{
		//Falta definir las entradas
		//tm.mesaRegistrarPedidosProducto(pedidos, mesa);
	}
	
	public void pedidoMenuMesaGlobal() throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		pedidoMenuMQ.pedidoMenuMesa();
	}
	
	public void pedidoMenuMesaLocal() throws Exception
	{
		//Falta definir las entradas
		//tm.mesaRegistrarPedidosMenu(pedidos, mesa);
	}
	
	//RF19
	
	public void retirarRestauranteLocal() throws Exception
	{
		//Función para retirar la mesa a nivel local
	}
	
	public void retirarRestauranteGlobal() throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		retirarRestauranteMQ.retirarRestaurante();
	}
	
}
