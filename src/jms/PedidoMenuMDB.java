package jms;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.jms.DeliveryMode;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.DatatypeConverter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;

import dtm.RotondAndesDistributed;
import vos.ExchangeMsg;
import vos.ListaVideos;
import vos.Video;


public class PedidoMenuMDB implements MessageListener, ExceptionListener 
{
	public final static int TIME_OUT = 5;
	private final static String APP = "A-05";

	private final static String GLOBAL_TOPIC_NAME = "java:global/RMQPedidoMenuGlobal";
	private final static String LOCAL_TOPIC_NAME = "java:global/RMQPedidoMenuLocal";

	private final static String REQUEST = "REQUEST";
	private final static String REQUEST_ANSWER = "REQUEST_ANSWER";

	private TopicConnection topicConnection;
	private TopicSession topicSession;
	private Topic globalTopic;
	private Topic localTopic;

	private List<String> answer;

	public PedidoMenuMDB(TopicConnectionFactory factory, InitialContext ctx) throws JMSException, NamingException 
	{	
		topicConnection = factory.createTopicConnection();
		topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		globalTopic = (RMQDestination) ctx.lookup(GLOBAL_TOPIC_NAME);
		TopicSubscriber topicSubscriber =  topicSession.createSubscriber(globalTopic);
		topicSubscriber.setMessageListener(this);
		localTopic = (RMQDestination) ctx.lookup(LOCAL_TOPIC_NAME);
		topicSubscriber =  topicSession.createSubscriber(localTopic);
		topicSubscriber.setMessageListener(this);
		topicConnection.setExceptionListener(this);
		System.out.println(globalTopic.getTopicName()+":"+localTopic.getTopicName());

	}

	public void start() throws JMSException
	{
		topicConnection.start();
	}

	public void close() throws JMSException
	{
		topicSession.close();
		topicConnection.close();
	}

	public void pedidoMenuMesa(String mensaje) throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException, NoSuchAlgorithmException
	{
		System.out.println(mensaje);
		answer=new ArrayList<>();
		String id = APP + System.currentTimeMillis();
		MessageDigest md = MessageDigest.getInstance("MD5");
		id = DatatypeConverter.printHexBinary(md.digest(id.getBytes())).substring(0, 8);
		//		id = new String(md.digest(id.getBytes()));

		sendMessage(mensaje, REQUEST, globalTopic, id, "");
		boolean waiting = true;

		int count = 0;
		while(TIME_OUT != count){
			TimeUnit.SECONDS.sleep(1);
			count++;
		}
		if(count == TIME_OUT){
			if(this.answer.isEmpty()){
				waiting = false;
				throw new NonReplyException("Time Out - No Reply");
			}
		}
		waiting = false;

		if(answer.isEmpty())
			throw new NonReplyException("Non Response");
		System.out.println(answer);
	}


	private void sendMessage(String payload, String status, Topic dest, String id, String from) throws JMSException, JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(id);
		ExchangeMsg msg = new ExchangeMsg("pedido.menu.general.A-05", from+" "+APP, payload, status, id);
		TopicPublisher topicPublisher = topicSession.createPublisher(dest);
		topicPublisher.setDeliveryMode(DeliveryMode.PERSISTENT);
		TextMessage txtMsg = topicSession.createTextMessage();
		txtMsg.setJMSType("TextMessage");
		String envelope = mapper.writeValueAsString(msg);
		System.out.println(envelope);
		txtMsg.setText(envelope);
		topicPublisher.publish(txtMsg);
	}

	@Override
	public void onMessage(Message message) 
	{
		TextMessage txt = (TextMessage) message;
		try 
		{
			System.out.println("ENTRA C");

			String body = txt.getText();
			System.out.println(body);
			ObjectMapper mapper = new ObjectMapper();
			ExchangeMsg ex = mapper.readValue(body, ExchangeMsg.class);
			String id = ex.getMsgId();
			System.out.println(ex.getSender());
			System.out.println(ex.getStatus());
			if(!ex.getSender().contains(APP))
			{
				if(ex.getStatus().equals(REQUEST))
				{
					String s=ex.getPayload();
					RotondAndesDistributed dtm = RotondAndesDistributed.getInstance();
					List<String> ans=dtm.pedidoMenuMesaLocal(s);
					if(ans == null) {
						Topic t = new RMQDestination("", "pedido.menu.mesa", ex.getRoutingKey(), "", false);
						sendMessage("NO OK", REQUEST_ANSWER, t, id, ex.getSender());
					}
					else if(ans.isEmpty()) {
						Topic t = new RMQDestination("", "pedido.menu.mesa", ex.getRoutingKey(), "", false);
						sendMessage("OK", REQUEST_ANSWER, t, id, ex.getSender());
					}
					else {
						String[] data = s.split(";");
						String msj = data[0] + ";" + data[1];
						for(String st : ans)
						{
							msj+= ";" + st;
						}
						System.out.println(msj);
						sendMessage(msj, REQUEST, globalTopic, id, ex.getSender());
					}

				}
				else if(ex.getStatus().equals(REQUEST_ANSWER))
				{
					System.out.println(ex.getPayload());
					String ans=ex.getPayload();
					answer.add(ans);
					System.out.println("ANSWER "+answer);
				}
			}

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onException(JMSException exception) 
	{
		System.out.println(exception);
	}

}
