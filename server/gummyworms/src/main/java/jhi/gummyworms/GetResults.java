package jhi.gummyworms;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.*;

@Path("/getResults")
public class GetResults {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getResults(@QueryParam("folder") String folder) {

    List<Map<String, String>> result = new ArrayList<>();

    try {
        File resultsFile = new File("data", folder + "/classification.tsv");
        Scanner scanner = new Scanner(resultsFile);

        // Read the first line (headers)
        String headerLine = scanner.nextLine();
        if (headerLine == null) {
            scanner.close();
            return null;
        }

        String[] headers = headerLine.split("\t");

        // Read data rows
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split("\t");

            Map<String, String> row = new LinkedHashMap<>();

            for (int i = 0; i < headers.length; i++) {
                String key = headers[i];
                String value = i < values.length ? values[i] : ""; // handle missing columns
                row.put(key, value);
            }

            result.add(row);
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