

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import exceptions.InvalidGenreDataException;
import exceptions.InvalidOrderDataException;
import exceptions.InvalidProductDataException;
import exceptions.InvalidUserDataException;
import webSite.WebSite;

/**
 * Application Lifecycle Listener implementation class LoadupContextListener
 *
 */
@WebListener
public class LoadupContextListener implements ServletContextListener {

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent servletContextEvent)  { 
    	//Initialize the website main method on startup --> load all collections when starting up
		try {
			WebSite.main(null);
		}
		catch (SQLException | InvalidGenreDataException | InvalidProductDataException | InvalidUserDataException | InvalidOrderDataException e) {
			e.printStackTrace();
		}
    }
	
}
