package jhi.gummyworms;

import java.util.HashMap;

public class Sample {
    public String sampleName;
    public double bacterivore = 0;
    public double fungivore = 0;
    public double herbivore = 0;
    public double omnivore = 0;
    public double omnivorePredator = 0;
    public double unknown = 0;
    public double unknownCount = 0;
    public double bacterivorePC = 0;
    public double fungivorePC = 0;
    public double herbivorePC = 0;
    public double omnivorePC = 0;
    public double omnivorePredatorPC = 0;
    public double unknownPC = 0;
    public double nonNematode = 0;
    public double nonNematodePC = 0;
    public double nonGenus = 0;
    public double nonGenusPC = 0;
    public double unassigned = 0;
    public double unassignedPC = 0;

    public HashMap<String, Double> unknownGenus = new HashMap<String, Double>();

    public Sample(String sampleName) {
        this.sampleName = sampleName;
    }

    public void addTrophicGrouping(String trophicGrouping, double count) {
        switch (trophicGrouping.toLowerCase()) {
            case "bacterivore":
                bacterivore += count;
                break;
            case "fungivore":
                fungivore += count;
                break;
            case "herbivore":
                herbivore += count;
                break;
            case "omnivore":
                omnivore += count;
                break;
            case "omnivore-predator":
                omnivorePredator += count;
                break;
            case "unknown":
                unknown += count;
                break;
            case "non-nematode":
                nonNematode += count;
                break;
            case "not genus level":
                nonGenus += count;
                break;
            case "unassigned":
                unassigned += count;
                break;
        }
    }

    public void addUnknown(String taxonimy, double count) {
        unknown += count;
        unknownCount += count;
        if (unknownGenus.containsKey(taxonimy)) {
            unknownGenus.put(taxonimy, unknownGenus.get(taxonimy) + count);
        } else {
            unknownGenus.put(taxonimy, count);
        }
    }

    public void calcPercentages() {
        double total = bacterivore + fungivore + herbivore + omnivore + omnivorePredator + unknown + nonNematode + nonGenus + unassigned;
        if (total > 0) {
            bacterivorePC = (bacterivore / total) * 100;
            fungivorePC = (fungivore / total) * 100;
            herbivorePC = (herbivore / total) * 100;
            omnivorePC = (omnivore / total) * 100;
            omnivorePredatorPC = (omnivorePredator / total) * 100;
            unknownPC = (unknown / total) * 100;
            nonNematodePC = (nonNematode / total) * 100;
            nonGenusPC = (nonGenus / total) * 100;
            unassignedPC = (unassigned / total) * 100;
        }
        System.out.println("Total for sample " + sampleName + " is " + total);
    }

    public String print() {
        return ("\n" + sampleName + "\t" + bacterivore + "\t" + String.format("%.2f", bacterivorePC) + "%\t" + fungivore+ "\t" + String.format("%.2f", fungivorePC) + "%\t" + herbivore + "\t" + String.format("%.2f", herbivorePC) + "%\t" + omnivore + "\t" + String.format("%.2f", omnivorePC) + "%\t" + omnivorePredator + "\t" + String.format("%.2f", omnivorePredatorPC) + "%\t" + unknown  + "\t" + String.format("%.2f", unknownPC) + "%\t" + nonNematode + "\t" + String.format("%.2f", nonNematodePC) + "%\t" + nonGenus + "\t" + String.format("%.2f", nonGenusPC) + "%\t" + unassigned + "\t" + String.format("%.2f", unassignedPC) + "%");
        
    } 

    public String printUnknowns(String taxonimy) {
        if (unknownGenus.containsKey(taxonimy)) {
           return ("\t" + unknownGenus.get(taxonimy));
        } else {
            return ("\t0");
        }
    }
}
