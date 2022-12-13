import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class SON {

    public static void main(String[] args) throws IOException {
        System.out.println("=======================SON Using Apriori with Triangular Method=======================");


        File file = new File("./retail.txt"); // File object to access the baskets
        

        long startTime = System.nanoTime();
        int baskets = Methods.getBaskets(file); // Scan the file to get baske count
        List<Integer> chunks = Arrays.asList(1, 2, 3, 4,5,6,7,8); // Number of chunks that the file will be diveded into
        double threshold = 0.01;
        double support = threshold * baskets;
        double localSupport = (double) (support / (chunks.size())); // Local Support per chunk
        BitSet frequentItems = new BitSet(); // Bit map for local frequent itemset for all chunks
        List<HashMap<Integer, Integer>> itemCounts = new ArrayList<HashMap<Integer, Integer>>(chunks.size()); // List of Hashmap for each chunk
        for (int i = 0; i < chunks.size(); i++) {
            itemCounts.add(new HashMap<Integer, Integer>()); // Adding a hashmap object to the list
        }
        double transactions = Math.floor(baskets / chunks.size()); // Number of baskets for each chunk to process
        // ====================================================================================================================================
        System.out.println("=======================Parameters=======================");
        System.out.println("Dataset : " + file.getName());
        System.out.println("Threshold: " + threshold * 100 + "%");
        System.out.println("Chunks: " + chunks.size());
        System.out.println("Baskets per chunk: " + transactions);
        System.out.println("Support: " + support);
        System.out.println("Local Support: " + localSupport);

        // ====================================================================================================================================
        System.out.println("=======================First Pass=======================");
        // Handle each chunk:
        chunks.forEach(chunk -> {
            try {
                int start = (int) ((chunk - 1) * transactions) + 1; // First basket to process
                int end = (int) ((chunk) * transactions); // last basket to process
                if(chunk == 8){ // Add 4 more baskets to the last chunk to cover all baskets
                    end = end + 4;
                }
                Methods.firstPass(file, itemCounts.get(chunk - 1), start, end); // Run the first pass of the Apriori Algorithm
                BitSet local = Methods.getFrequentItems(itemCounts.get(chunk - 1), localSupport); // Get Local Frequent items
                System.out.println("Chunk " + chunk + " Frequent Items: " + local.cardinality()); // Number of frequent local items
                frequentItems.or(local); // Combine bitmaps using Logical OR to get all local frequent items

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        });
        // ====================================================================================================================================

        System.out.println("=======================Check Global Frequent Items=======================");
        HashMap<Integer, Integer> ItemCount = new HashMap<Integer, Integer>(); // Hashmap to count locally frequent item sets globally
        for(int i = 0; i < frequentItems.size(); i ++) {
            if(frequentItems.get(i)) {
                ItemCount.put(i,0); // add items to the hashamp
            }
        }
        Methods.SONGlobalItems(file, ItemCount, 0, baskets); // Count items only found to be local in at least one chunk
        BitSet trueFrequent = Methods.getFrequentItems(ItemCount, support); // Get Global frequent items
        System.out.println("Global Frequent Items: " + trueFrequent.cardinality());
        long start2 = System.nanoTime();
        long totalTime = System.nanoTime() - startTime;
        System.out.println("Pass 1 Run Time: " + totalTime / 1000000000);
        // ====================================================================================================================================
        System.out.println("=======================Secong Pass=======================");
        HashMap<Pair, MutableShort> pairsCount = new HashMap<Pair, MutableShort>(); // Hashmap to count pairs found frequent locally

        chunks.forEach(chunk -> {
            try {
                // Map each item to a consecative item number to make the triangular array
                HashMap<Integer, Integer> mapItems = new HashMap<>(); //
                HashMap<Integer, Integer> mapItemsBack = new HashMap<>();
                int n = 0;
                for (int i = 0; i < frequentItems.size(); i++) {
                    if (frequentItems.get(i) == true) {
                        n++;
                        mapItems.put(i, n);
                        mapItemsBack.put(n, i);
                    }
                }
                int[] triangualrArray = new int[(n * (n - 1) / 2) + 1];
                int start = (int) ((chunk - 1) * transactions) + 1;
                int end = (int) ((chunk) * transactions);
                if(chunk == 8){
                    end = end + 4;
                }
                Methods.secondPass(file, trueFrequent, triangualrArray, mapItems, start, end); // Run the second pass of the Apriori algorithm to count locally
                ArrayList<Pair> localFreq = Methods.getFrequentPairs(triangualrArray, mapItemsBack, localSupport, n); // get local frequent pairs
                System.out.println("Chunk " + chunk+ " Local Frequent Pairs: " + localFreq.size());
                for(Pair p : localFreq) {
                    pairsCount.put(p, new MutableShort());
                }
                triangualrArray = null;
                localFreq = null;
                mapItems = null;
                mapItemsBack = null;
                System.gc();
                System.out.println("Memory Usage: after Pass 2: "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        });
        // ====================================================================================================================================
        System.out.println("=======================Check Global Frequent Pairs=======================");

  
        Methods.secondPass(file, trueFrequent, support, pairsCount, 0, baskets); // Run second pass of Apriori Algorithm to count only pairs found to be lcoal in at least one chunk
        ArrayList<Pair> globalFreqPairs = Methods.getFrequentPairs(pairsCount, support); // Get globally frequent pairs
        System.out.println("Global Frequent Pairs: " + globalFreqPairs.size());

          
        long totalTime2 = System.nanoTime() - start2;
        System.out.println("Total Run Time: " + totalTime2 / 1000000000);
        // ====================================================================================================================================

        // Write pairs into file

        PrintWriter pairsText = new PrintWriter(new FileWriter("SON_" + file.getName() + "_" + threshold+".txt"));
            for (Pair text : globalFreqPairs) {
                pairsText.write(text.toString() + "\n");
            }
            pairsText.close();
      

    }

}
