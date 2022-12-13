import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;

public class Apriori_HashMap {

    public static void main(String[] args) throws IOException {
        System.out.println("=======================Apriori Using Triples =======================");

        

        File file = new File("./retail.txt"); // File object to access the baskets

        
        int baskets = Methods.getBaskets(file); // Scan the file to get baske count
        long startTime = System.nanoTime();
        double threshold = 0.01;
        double dataSize = 1; // Percentage of the file to proccess, 1 = 100%
        double end = (baskets * dataSize); // Last basket to process
        HashMap<Integer, Integer> itemCount = new HashMap<Integer, Integer>(); // Hashmap to keep count of itemset of size 1
        double transactions = baskets * dataSize; // number of baskets to proccess
        double support = transactions * threshold;

        // ====================================================================================================================================
        int numOfItems = Methods.firstPass(file, itemCount, 0, end); // get item count into the hashmap and return  number of total items
        BitSet frequentItems = Methods.getFrequentItems(itemCount, support); // Summarize frequent items into a bit map to save space
        int numOfFreqItems = frequentItems.cardinality(); // get how many items that pass the threshold
        long endTimeOfPass1 = System.nanoTime();
        long passOneTime = endTimeOfPass1 - startTime;
        System.out.println("Memory Usage: after pass 1 with the HashTable "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");
        itemCount = null;
        System.gc();
        System.out.println("Memory Usage: after clearing the itemCount "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");
        HashMap<Pair, MutableShort> pairsCount = new HashMap<Pair, MutableShort>(); // Use Triples method to keep count of pairs
        // ====================================================================================================================================
        Methods.secondPass(file, frequentItems, support, pairsCount, 0, end); // Run second pass of Apriori to count
        // pairs using the HashMap
        ArrayList<Pair> frequentPairs = Methods.getFrequentPairs(pairsCount, support); // Create a list of frequent  pairs that pass the threshold
        long passTwoEnd = System.nanoTime();
        long totalTime = passTwoEnd - startTime;
        Collections.sort(frequentPairs);

        Methods.getStatistics(file, dataSize, baskets, support, threshold, transactions, numOfItems, numOfFreqItems,
                passOneTime, totalTime, endTimeOfPass1, frequentPairs.size(), pairsCount);
        // ====================================================================================================================================
        // Write pairs into file

        PrintWriter pairsText = new PrintWriter(new FileWriter("Apriori_Hash_" + file.getName() + "_" + threshold + ".txt"));
            for (Pair text : frequentPairs) {
                pairsText.write(text.toString() + "\n");
            }
            pairsText.close();
    }
}
