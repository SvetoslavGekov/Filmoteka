

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import dbManager.DBManager;
import exceptions.InvalidGenreDataException;
import exceptions.InvalidOrderDataException;
import exceptions.InvalidProductDataException;
import exceptions.InvalidUserDataException;
import util.WebSite;
import util.taskExecutors.CustomTaskExecutor;

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
        
    	try {
    		 //Destroy the DB manager connection
			DBManager.getInstance().getCon().close();
			//Shutdown all scheduled tasks
			for (CustomTaskExecutor taskExecutor : WebSite.getAllTasks()) {
				taskExecutor.stopTask();
			}
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			//If the system crashes when loading initial resours --> nothing will be loaded anyway
			e.printStackTrace();
		}
    }
	
}
