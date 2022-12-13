import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.BitSet;

public class Sampling {

    public static void main(String[] args) throws IOException {
        System.out.println("=======================Sampling Using Apriori=======================");
        

        File file = new File("./retail.txt");
        

        int baskets = Methods.getBaskets(file);
        long startTime = System.nanoTime();
        // BEST PARAMETERS SO FAR: Threshold/6 and DataSize = 0.2
        double dataSize = 0.20;
        double threshold = 0.01;
        double Samplethreshold = (double) (threshold / 5);
        int start = 0;
        double end = (baskets * dataSize);
        HashMap<Integer, Integer> itemCount = new HashMap<Integer, Integer>();
        double transactions = (double) (baskets * dataSize);
        // ====================================================================================================================================
        int numOfItems = Methods.firstPass(file, itemCount, start, end);
        double support = (double) (Samplethreshold * baskets);
        BitSet frequentItems = Methods.getFrequentItems(itemCount, support);
        int numOfFreqItems = frequentItems.cardinality();
        long endTimeOfPass1 = System.nanoTime();
        long passOneTime = endTimeOfPass1 - startTime;
        HashMap<Integer, Integer> conv = new HashMap<>();
        HashMap<Integer, Integer> conv2 = new HashMap<>();
        int n = 0;
        for (int i = 0; i < frequentItems.size(); i++) {
            if (frequentItems.get(i) == true) {
                n++;
                conv.put(i, n);
                conv2.put(n, i);
            }
        }
        int[] matrix = new int[(n * (n - 1) / 2) + 1];
        System.out.println("Memory Usage: after pass 1 with the array "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");

        itemCount = null;
        System.gc();

        System.out.println("Memory Usage: after clearing the itemCount table "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");
        // ====================================================================================================================================
        Methods.secondPass(file, frequentItems, matrix, conv, start, end);
        System.out.println("Memory Usage: after Pass 2: "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");
        ArrayList<Pair> frequentPairs = Methods.getFrequentPairs(matrix, conv2, support, n);

        // ====================================================================================================================================

        long passTwoEnd = System.nanoTime();
        long totalTime = passTwoEnd - startTime;
        Methods.getStatistics(file, dataSize, baskets, support, threshold, transactions, numOfItems, numOfFreqItems,
                passOneTime, totalTime, endTimeOfPass1, frequentPairs.size(), null);

        // ====================================================================================================================================

        // True positives are saved in Pairs.txt

        File file2 = new File("./Apriori_Array_" + file.getName() + "_" + threshold + ".txt");
        HashSet<Pair> truePositives = Methods.getPairsFromFile(file2);
        System.out.println("True Frequent Pairs from file: " + truePositives.size());

        int falsePositives = 0;
        int falseNegative = 0;
        for (Pair sampledPair : frequentPairs) {
            if (!truePositives.contains(sampledPair)) {
                falsePositives++;

            }
        }
        System.out.println("False Positives: " + falsePositives);
        HashSet<Pair> frequentSampledPairs = new HashSet<Pair>(frequentPairs);
        for (Pair truePair : truePositives) {
            if (!frequentSampledPairs.contains(truePair)) {
                falseNegative++;

            }
        }
        System.out.println("False Negative: " + falseNegative);

        long ending = System.nanoTime();

        System.out.println("Run Time after checking: " + (System.nanoTime() - ending));

        PrintWriter pairsText =  new PrintWriter(new FileWriter("Sampling_" + file.getName() + "_" + (threshold) + ".txt"));
            for (Pair text : frequentPairs) {
                pairsText.write(text.toString() + "\n");
            }
      pairsText.close();
    }
    // ====================================================================================================================================

}
