package jhi.gummyworms;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.*;

@Path("/getUnknowns")
public class GetUnknowns {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getResults(@QueryParam("folder") String folder) {

    Map<String, ArrayList<String>> result = new HashMap<String,ArrayList<String>>();
    try {
        File resultsFile = new File("data", folder + "/unknowns.tsv");
        Scanner scanner = new Scanner(resultsFile);
        String group = "";
        // Read data rows
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            switch (line) {
                case "Unknowns":
                    group = "Unknowns";
                    result.put(group, new ArrayList<String>());
                    break;
                case "Non-Nematodes":
                    group = "Non-Nematodes";
                    result.put(group, new ArrayList<String>());
                    break;
                case "Not Genus Level":
                    group = "Not Genus Level";
                    result.put(group, new ArrayList<String>());
                    break;
                case "Unassigned":
                    group = "Unassigned";
                    result.put(group, new ArrayList<String>());
                    break;
            
                default:
                    if(!line.isBlank())
                        result.get(group).add(line);
                    break;
            }
        }
        scanner.close();


        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV", e);
        }
    
    Gson gson = new Gson();
    String jsonOutput = gson.toJson(result);

    return jsonOutput;
    }
}