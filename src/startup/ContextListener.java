package startup;

import javax.jms.JMSException;



import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.servlet.annotation.WebListener;

import dtm.VideoAndesDistributed;


@WebListener
public class ContextListener implements ServletContextListener {

	private VideoAndesDistributed dtm;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		try {
			dtm.stop();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		final ServletContext context = arg0.getServletContext();
		VideoAndesDistributed.setPath(context.getRealPath("WEB-INF/ConnectionData"));
		dtm = VideoAndesDistributed.getInstance();
	}

}
