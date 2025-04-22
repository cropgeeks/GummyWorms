package jhi.gummyworms;

import java.io.InputStream;
import java.util.Scanner;

import org.glassfish.jersey.media.multipart.FormDataParam;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/submit")
public class Submit {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response submit(@FormDataParam("file") InputStream fileContents) {
        
        try(Scanner scanner = new Scanner(fileContents)) {
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line); // Process the line as needed
            }
            return Response.ok().build();
        }

    }
}
