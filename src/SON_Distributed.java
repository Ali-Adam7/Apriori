import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class SON_Distributed {

    public static void main(String[] args) throws IOException {
        System.out.println("=======================SON Distributed Using Apriori with Triangular Method=======================");

        
        File file = new File("./retail.txt"); // File object to access the baskets


        long startTime = System.nanoTime();
        int baskets = Methods.getBaskets(file); // Scan the file to get baske count
        double threshold = 0.01;
        double support = threshold * baskets;
        List<Integer> chunks = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8); // Number of chunks that the file will be diveded into
        List<HashMap<Integer, Integer>> itemCounts = new ArrayList<HashMap<Integer, Integer>>(chunks.size()); // List of hashmap to count items for each chunk
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
        // ====================================================================================================================================
        System.out.println("=======================First Pass=======================");
        // Handle each chunk in Parallel, where each chunk is ran by a thread.
        chunks.parallelStream().forEach(chunk -> {
            try {
                int start = (int) ((chunk - 1) * transactions) + 1; // First basket to process
                int end = (int) ((chunk) * transactions); // last basket to process
                if (chunk == 8) { // Add 4 more baskets to the last chunk to cover all baskets
                    end = end + 4;
                }
                Methods.firstPass(file, itemCounts.get(chunk - 1), start, end); // Run the first pass of the Apriori Algorithm
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        // ====================================================================================================================================
        long start2 = System.nanoTime();
        long totalTime = System.nanoTime() - startTime;
        System.out.println("Pass 1 Run Time: " + totalTime / 1000000000);
        HashMap<Integer, Integer> full = new HashMap<Integer, Integer>(); // Hashmap to combine counts from each thread
        for (int i = 0; i < itemCounts.size(); i++) {
            itemCounts.get(i).forEach((item, count) -> full.merge(item, count, Integer::sum)); // Combine count from each thread hashmap
        }
        BitSet frequentItems = Methods.getFrequentItems(full, support); // Summarize the hashmap as a Bit Map to save space
        System.out.println("Frequent Items: " + frequentItems.cardinality()); // Frequent Items
        HashMap<Integer, Integer> mapItems = new HashMap<>(); // Map frequent items to integers
        HashMap<Integer, Integer> mapItemsBack = new HashMap<>(); // Map frequent items to integers
        int n = Methods.mapItems(frequentItems, mapItems, mapItemsBack);
        int[] triangualrArray = new int[(n * (n - 1) / 2) + 1];
        ArrayList<int[]> listOfArrays = new ArrayList<int[]>();
        for (int i = 0; i < chunks.size(); i++) {
            listOfArrays.add(i, new int[(n * (n - 1) / 2) + 1]); // Add arrays to the list
        }
        // ====================================================================================================================================
        System.out.println("=======================Secong Pass=======================");
        // Process chunks in Parallel to count pairs
        chunks.parallelStream().forEach(chunk -> {
            try {
                System.out.println("Chunk " + chunk + " starting");
                int start = (int) ((chunk - 1) * transactions) + 1;
                int end = (int) ((chunk) * transactions);
                if (chunk == 8) {
                    end = end + 4;
                }
                Methods.secondPass(file, frequentItems, listOfArrays.get(chunk - 1), mapItems, start, end);
                System.out.println("Chunk " + chunk + " is done");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        // ====================================================================================================================================
        System.out.println("Memory Usage: after Pass 2 "
        + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");
        for (int i = 0; i < triangualrArray.length; i++) {
            for (int j = 0; j < chunks.size(); j++) {
                triangualrArray[i] = triangualrArray[i] + listOfArrays.get(j)[i]; // Combine counts of pairs from each thread
            }
        }
        ArrayList<Pair> frequentPairs = Methods.getFrequentPairs(triangualrArray, mapItemsBack, support, n); // Get Frequent pairs that pass the threshold
        System.out.println("Frequent Pairs: " + frequentPairs.size());
        long totalTime2 = System.nanoTime() - start2;
        System.out.println("Total Run Time: " + totalTime2 / 1000000000);

        Collections.sort(frequentPairs);
        // ====================================================================================================================================

        // Write pairs into file

        PrintWriter pairsText = new PrintWriter(new FileWriter("SON_Distributed_" + file.getName() + "_" + threshold+".txt"));
            for (Pair text : frequentPairs) {
                pairsText.write(text.toString() + "\n");
            }
            pairsText.close();

    }
}
