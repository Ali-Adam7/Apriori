import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.BitSet;
import java.util.Collections;

public class Apriori_Array {

    public static void main(String[] args) throws IOException {
        System.out.println("=======================Apriori Using Triangular Array =======================");
        

        File file = new File("./retail.txt"); // File object to access the baskets


        int baskets = Methods.getBaskets(file); // Scan the file to get baske count
        long startTime = System.nanoTime();
        double threshold = 0.01;
        double dataSize = 1; // Percentage of the file to proccess
        double end = (baskets * dataSize); // Last basket to process
        HashMap<Integer, Integer> itemCount = new HashMap<Integer, Integer>(); // Hashmap to keep count of itemset of size 1
        double transactions = baskets * dataSize; // number of baskets to proccess
        double support = transactions * threshold;

        // ====================================================================================================================================
        int numOfItems = Methods.firstPass(file, itemCount, (double) 0, end); // get item count into the hashmap and return number of total items
        BitSet frequentItems = Methods.getFrequentItems(itemCount, support); // Summarize frequent items into a bit map to save space
        int numOfFreqItems = frequentItems.cardinality(); // get how many items that pass the threshold
        long endTimeOfPass1 = System.nanoTime();
        long passOneTime = endTimeOfPass1 - startTime;
        HashMap<Integer, Integer> mapItems = new HashMap<>(); // Hashmap to map items from 1 to maxFrequentItem to be used in the triangualr array
        HashMap<Integer, Integer> mapItemsBack = new HashMap<>();// Hashmap to map back to the real item number
        // Map items to sequential numbers
        int n = Methods.mapItems(frequentItems, mapItems, mapItemsBack);
        int[] triangularArray = new int[(n * (n - 1) / 2) + 1]; // From the Mining of Massive Dataset book:

        System.out.println("Memory Usage: after pass 1 with the array "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");

        itemCount = null; // clear item count hashmap to save space
        System.gc(); // Garbage collection
        System.out.println("Memory Usage: after clearing the itemCount table "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");
        // ====================================================================================================================================
        Methods.secondPass(file, frequentItems, triangularArray, mapItems, 0, end); // Run second pass of Apriori to count pairs.
        System.out.println("Memory Usage: after Pass 2: "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");
        ArrayList<Pair> frequentPairs = Methods.getFrequentPairs(triangularArray, mapItemsBack, support, n); // Create a                                                                                       // threshold
        long passTwoEnd = System.nanoTime();
        long totalTime = passTwoEnd - startTime;
        Methods.getStatistics(file, dataSize, baskets, support, threshold, transactions, numOfItems, numOfFreqItems,
                passOneTime, totalTime, endTimeOfPass1, frequentPairs.size(), null);

        // ====================================================================================================================================
        // Write pairs into file
        Collections.sort(frequentPairs);
        PrintWriter pairsText = new PrintWriter(new FileWriter("Apriori_Array_" + file.getName() + "_" + threshold+".txt"));
            for (Pair text : frequentPairs) {
                pairsText.write(text.toString() + "\n");
            }
            pairsText.close();
        

    }
    // ====================================================================================================================================

}
