package jms;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.rabbitmq.jms.admin.RMQDestination;

public class TerrorVideosMDB implements MessageListener, ExceptionListener 
{
	private final static String TOPIC_NAME = "java:global/RMQTopicTerrorVideos";

	private TopicConnection topicConnection;
	private TopicSession topicSession;

	public TerrorVideosMDB(TopicConnectionFactory factory, InitialContext ctx) throws JMSException, NamingException
	{
		topicConnection = factory.createTopicConnection();
		topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = (RMQDestination) ctx.lookup(TOPIC_NAME);
		TopicSubscriber topicSubscriber =  topicSession.createSubscriber(topic);
		topicSubscriber.setMessageListener(this);
		topicConnection.setExceptionListener(this);
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
	
	@Override
	public void onException(JMSException exception) 
	{
		
		
	}

	@Override
	public void onMessage(Message message) 
	{
		// TODO Auto-generated method stub
		
	}
	

}
