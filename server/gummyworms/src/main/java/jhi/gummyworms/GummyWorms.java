package jhi.gummyworms;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.glassfish.jersey.media.multipart.*;
import org.glassfish.jersey.server.ResourceConfig;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;


@ApplicationPath("/api/")
@Path("/")
@WebListener
public class GummyWorms extends ResourceConfig implements ServletContextListener {
	private static Properties properties;
	private ScheduledExecutorService scheduler;
	private Logger log = Logger.getLogger(GummyWorms.class.getName());
	
	public GummyWorms() {
		packages("jhi.gummyworms");
		register(MultiPartFeature.class);
	}

	@GET
	public String getInformation(@Context ServletContext context)
		throws Exception {
		return "GummyWorms API - " + new java.util.Date();
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		DatabaseUtils.init(sce.getServletContext());

		scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(
            () -> {

				log.info("Running data deletion task at " + new java.util.Date());
				deleteFolder(new File("data"));
				log.info("Deletion task completed at " + new java.util.Date());
				
			},
            0,
            1,
            TimeUnit.DAYS
        );
	}

	private void deleteFolder(File folder) {
		
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				deleteFolder(file);
			}
			log.info("Deleting file: " + file.getAbsolutePath());
			file.delete();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		DatabaseUtils.close();
		scheduler.shutdownNow();
	}

	public static String getConfigProperty(String id) {
		if (GummyWorms.properties == null) {
			properties = new Properties();

			try {
				FileInputStream is = new FileInputStream(System.getProperty("catalina.base") + "/webapps/ROOT/WEB-INF/classes/gummyworms.properties");
				GummyWorms.properties.load(is);
				is.close();
			}
			catch (IOException e) { e.printStackTrace(); }
		}

		return properties.getProperty(id);
	}

	public static String getDbUrl() {
		if (GummyWorms.properties == null) {
			properties = new Properties();

			try {
				FileInputStream is = new FileInputStream(System.getProperty("catalina.base") + "/webapps/ROOT/WEB-INF/classes/gummyworms.properties");
				GummyWorms.properties.load(is);
				is.close();
			}
			catch (IOException e) { e.printStackTrace(); }
		}

		return properties.getProperty("db.url");
	}

	public static String getDbUsername() {
		if (GummyWorms.properties == null) {
			properties = new Properties();

			try {
				FileInputStream is = new FileInputStream(System.getProperty("catalina.base") + "/webapps/ROOT/WEB-INF/classes/gummyworms.properties");
				GummyWorms.properties.load(is);
				is.close();
			}
			catch (IOException e) { e.printStackTrace(); }
		}

		return properties.getProperty("db.username");
	}

	public static String getDbPassword() {
		if (GummyWorms.properties == null) {
			properties = new Properties();

			try {
				FileInputStream is = new FileInputStream(System.getProperty("catalina.base") + "/webapps/ROOT/WEB-INF/classes/gummyworms.properties");
				GummyWorms.properties.load(is);
				is.close();
			}
			catch (IOException e) { e.printStackTrace(); }
		}

		return properties.getProperty("db.password");
	}
}