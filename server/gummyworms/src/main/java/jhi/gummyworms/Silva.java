package jhi.gummyworms;

import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import jhi.gummyworms.codegen.tables.pojos.*;
import static jhi.gummyworms.codegen.tables.GenusToTrophicGrouping.GENUS_TO_TROPHIC_GROUPING;

public class Silva {
    String delimiter = "";
    String genusPrefix = "g__";
    String taxonimySplit = ";";
    String notKnown = "__";
    InputStream fileContents;
    ArrayList<Sample> samples = new ArrayList<Sample>();
    ArrayList<String> headers = new ArrayList<String>();
    HashMap<String, String> classificationMap = new HashMap<String, String>();
    int genEnd = -1;
    String domain = "";
    String phylum = "";
    String classis = "";
    String order = "";
    String family = "";
    String genus = "";
    String species = "";
    boolean headerProcessed = false;

    public Silva(InputStream fileContents) {
        this.fileContents = fileContents;
    }

    public String processFile() {
        
        Scanner scanner = new Scanner(fileContents);
       
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Sample sample = null;
            int endIndex = -1;

            if (!line.isEmpty() && !headerProcessed) {
                headerProcessed = true;

                if (line.split("\t").length > 0) {
                    delimiter = "\t";
                }
                else if (line.split(",").length > 0) {
                    delimiter = ",";
                }
                else {
                    break;
                }

                String[] headings = line.split(delimiter);
                int index = 0;

                while (endIndex == -1 && index < headings.length) {
                    String heading = headings[index];
                    endIndex = checkEndHeader(heading, index);

                    if(endIndex != -1) {
                        // We have found the end column already
                        break;
                    }
                    if (index == 0) {
                        headers.add(heading);
                    }
                    else {
                        headers.add(heading);
                        splitTaxonomy(heading);
                        setClassification(heading.toLowerCase());
                    }
                    index++;
                }
            }
            else {
                String[] parts = line.split(delimiter);
                
                sample = new Sample(parts[0]);
                for (int i = 1; i < headers.size(); i++) {
                    String taxonimy = headers.get(i);
                    String classification = classificationMap.get(taxonimy.toLowerCase());
                    System.out.println("Classification for " + taxonimy + " is " + classification);
                    double count = Double.parseDouble(parts[i]);
                    sample.addTrophicGrouping(classification, count);
                    System.out.println("Added " + count + " to " + classification);
                }
                samples.add(sample);
            }
           
        }
        
        String out = ("Sample\tBacterivore\tBacterivore%\tFungivore\tFungivore%\tHerbivore\tHerbivore%\tOmnivore\tOmnivore%\tOmnivore-Predator\tOmnivore-Predator%\tUnknown\tUnknown%\tNon-Nematode\tNon-Nematode%\tNot Genus Level\tNot Genus Level%\tUnassigned\tUnassigned%");
       
        for (Sample sample : samples) {
            sample.calcPercentages();
            out += sample.print();
        }

        scanner.close();
        return out;
    }

    public String getUnknowns() {
        String unknown = "Unknowns\n";
        String nonNemaString = "Non-Nematodes\n";
        String notGenusString = "Not Genus Level\n";
        String unassignedString = "Unassigned\n";
        for (String taxonomy : classificationMap.keySet()) {
            switch (classificationMap.get(taxonomy).toLowerCase()) {
                case "unknown":
                    unknown += taxonomy + "\n";
                    break;
                case "non-nematode":
                    nonNemaString += taxonomy + "\n";
                    break;
                case "not genus level":
                    notGenusString += taxonomy + "\n";
                    break;
                case "unassigned":
                    unassignedString += taxonomy + "\n";
                    break;
            }
        }
        return (unknown + "\n" + nonNemaString + "\n" + notGenusString + "\n" + unassignedString);
    }

    public int checkEndHeader(String header, int index) {
        int genEnd = -1;
        if(!header.trim().startsWith("d__") && index > 0 && !header.trim().startsWith("Unassigned")) {
            genEnd = index;
        }
        return genEnd;
    }

    public void splitTaxonomy(String taxonimy) {
        domain = "";
        phylum = "";
        classis = "";
        order = "";
        family = "";
        genus = "";
        species = "";

        taxonimy = taxonimy.toLowerCase();
        String [] taxonimyParts = taxonimy.split(taxonimySplit);
        if(!taxonimyParts[0].equals(notKnown)) {
            domain = taxonimyParts[0].replace("d__", "").trim();
        }
        if(taxonimyParts.length > 1 && !taxonimyParts[1].equals(notKnown)) {
            phylum = taxonimyParts[1].replace("p__", "").trim();
        }
        if(taxonimyParts.length > 2 && !taxonimyParts[2].equals(notKnown)) {
            classis = taxonimyParts[2].replace("c__", "").trim();
        }
        if(taxonimyParts.length > 3 && !taxonimyParts[3].equals(notKnown)) {
            order = taxonimyParts[3].replace("o__", "").trim();
        }
        if(taxonimyParts.length > 4 && !taxonimyParts[4].equals(notKnown)) {
            family = taxonimyParts[4].replace("f__", "").trim();
        }
        if(taxonimyParts.length > 5 && !taxonimyParts[5].equals(notKnown)) {
            genus = taxonimyParts[5].replace("g__", "").trim();
        }
        if(taxonimyParts.length > 6 && !taxonimyParts[6].equals(notKnown)) {
            species = taxonimyParts[6].replace("s__", "").trim();
        }
        if (family.equals(genus) && species.length() > 0 && species.contains("_")) {
            genus = species.substring(0, species.indexOf('_'));
        }
    }

    public void setClassification(String taxonimy) {
        String classification = "";
        if (taxonimy.startsWith("unassigned")) {
            classification = "Unassigned";
        } else if (!phylum.equals("nematozoa")) {
            classification = "Non-Nematode";
        } else if (genus.length() == 0 || genus.equals(family)) {
            classification = "Not Genus Level";
        } else {
            try (Connection conn = DatabaseUtils.getConnection()) {
                
                DSLContext context = DSL.using(conn, SQLDialect.MYSQL);

                GenusToTrophicGrouping result = context.selectFrom(GENUS_TO_TROPHIC_GROUPING)
                .where(GENUS_TO_TROPHIC_GROUPING.GENUSNAME.equalIgnoreCase(genus))
                .fetchOneInto(GenusToTrophicGrouping.class);
          
                if (result != null) {
                    classification = result.getTrophicgrouping();
                }
                else {
                    classification = "unknown";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        classificationMap.put(taxonimy, classification);
    } 
}
