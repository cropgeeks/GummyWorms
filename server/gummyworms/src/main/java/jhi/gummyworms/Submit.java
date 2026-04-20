package jhi.gummyworms;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.glassfish.jersey.media.multipart.FormDataParam;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/submit")
public class Submit {
  ArrayList<String> unclassifiedGenus = new ArrayList<String>();
  String separator = "";
  String genusPrefix = "";
  String taxonimySplit = "";
  String firstColumn = "";
  String endColumn = "";
  String nonNematode = "";

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public String submit(@FormDataParam("file") InputStream fileContents, @FormDataParam("database") String database) {


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
    String folderName = LocalDateTime.now().format(formatter) + "_" + database;
    File parentFolder = new File("data", folderName);
    parentFolder.mkdirs();

    String output = "";
    String unknowns = "";
    
    switch (database.toLowerCase()) {
      case "nemataxa":
        Nemataxa nemataxa = new Nemataxa(fileContents);
        output = nemataxa.processFile();
        unknowns = nemataxa.getUnknowns();
        break;
      case "silva":
        Silva silva = new Silva(fileContents);
        output = silva.processFile();
        unknowns = silva.getUnknowns();
        break;
    }

    File classificationFile = new File(parentFolder, "classification.tsv");
    try (FileWriter writer = new FileWriter(classificationFile)) {
      writer.write(output);
    } catch (Exception e) {
      e.printStackTrace();
    }

    File unknownsFile = new File(parentFolder, "unknowns.tsv");
    try (FileWriter writer = new FileWriter(unknownsFile)) {
      writer.write(unknowns);
    } catch (Exception e) {
      e.printStackTrace();
    }


    return folderName;
  }
}
