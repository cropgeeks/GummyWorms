package jhi.gummyworms;

import java.io.File;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/downloadUnknowns")
public class DownloadUnknowns {

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response doGet(@QueryParam("folder") String folder) {
        File file = new File("data", folder + "/unknowns.tsv");

        ResponseBuilder response = Response.ok(file);
        response.header("Content-Disposition", "attachment; filename=\"" + folder + "_unknowns.tsv\"");

        return response.build();
            
        }
    }