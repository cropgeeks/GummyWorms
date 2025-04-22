package jhi.gummyworms;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.*;
import javax.servlet.annotation.*;

import org.glassfish.jersey.media.multipart.*;
import org.glassfish.jersey.server.ResourceConfig;

import jakarta.ws.rs.*;

@ApplicationPath("/api")
@Path("/")
@WebListener
public class GummyWorms extends ResourceConfig implements ServletContextListener {
	private static Properties properties;
	public GummyWorms() {
		packages("jhi.gummyworms");
		register(MultiPartFeature.class);
	}

	public static String getConfigProperty(String id) {
		if (GummyWorms.properties == null) {
			properties = new Properties();

			try {
				FileInputStream is = new FileInputStream(System.getProperty("catalina.base") + "/webapps/ROOT/WEB-INF/classes/params.properties");
				GummyWorms.properties.load(is);
				is.close();
			}
			catch (IOException e) { e.printStackTrace(); }
		}

		return properties.getProperty(id);
	}
}