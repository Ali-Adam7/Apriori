import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;

public class MultiStage {

    public static void main(String[] args) throws IOException {
        System.out.println("=======================Multi-Stage=======================");
        

        File file = new File("./retail.txt"); // File object to access the baskets


        int baskets = Methods.getBaskets(file); // Scan the file to get baske count
        long startTime = System.nanoTime();
        double threshold = 0.02;
        double dataSize = 1; // Percentage of the file to proccess
        double end = (baskets * dataSize);  // Last basket to process
        short[] buckets = new short[67108864]; // Buckets to hash pairs into and count
        short[] buckets2 = new short[67108864]; // Second Array of Buckets to hash pairs into and count
        HashMap<Integer, Integer> itemCount = new HashMap<Integer, Integer>(17770);
        double support = (baskets * threshold) * dataSize;
        double transactions = dataSize * baskets; // number of baskets to proccess

    // ------------------------------------------------------------------------------------------------------------

        int numOfItems = Methods.firstPCYPass(file, itemCount, buckets ,support, 0, end); // Run first pass of PCY Algorithm with 2 Arrays of Buckets
        BitSet bitmap = Methods.bucketsToBitMap(buckets, support); // Summarize first array of buckets into a bitmap to save space
        buckets = null;  // Clear Buckets
        System.gc();  // Call Garbage Collection
        BitSet frequentItems = Methods.getFrequentItems(itemCount, support); // Summarize frequent items into a bit map to save space
        itemCount = null; // Clear ItemCount hashmap
        System.gc();  // Call Garbage Collection

        Methods.secondMultiStagePass(file, frequentItems,bitmap ,buckets2 ,support, 0, end); // Run second pass of the multistage algorithm using the second array of buckets
        BitSet bitmap2 = Methods.bucketsToBitMap(buckets2, support); // Summarize second array of buckets into a bitmap to save space
        int numOfFreqItems = frequentItems.cardinality();  // get how many items that pass the threshold
        HashMap<Pair, MutableShort> pairsCount = new HashMap<Pair, MutableShort>(2097152);
        long endTimeOfPass1 = System.nanoTime();
        long passOneTime = endTimeOfPass1 - startTime;
        System.out.println("Memory Usage: After Pass 1: "
        + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");
    
        buckets2 = null;  // Clear Buckets
        System.gc();  // Call Garbage Collection
        System.out.println("Memory Usage: before Pass 2 (After clearing the buckets): "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");
    // ------------------------------------------------------------------------------------------------------------
        Methods.putPairsInTableMulti(frequentItems, pairsCount, bitmap, bitmap2);    // Insert keys into the hashmap with values 0, I expect most pairs to actually occure so this step will minimize time due to hash calculation on each pair in the baskets.
        Methods.secondPCYPass(file, frequentItems, pairsCount, support, bitmap,0, end); // Run the second pass of PCY Algorithm to get pair count with 2 bit maps
        List<Pair> frequentPairs = Methods.getFrequentPairs(pairsCount, support); // Create a list of frequent pairs that pass the threshold
        long passTwoEnd = System.nanoTime();
        long totalTime = passTwoEnd - startTime;
        Methods.getStatisticsPCY(file, dataSize, baskets, support, threshold, transactions, numOfItems, numOfFreqItems,
                passOneTime, totalTime, endTimeOfPass1, frequentPairs.size(), pairsCount.size());
    // ------------------------------------------------------------------------------------------------------------
        // Write pairs into file
        Collections.sort(frequentPairs);

        PrintWriter pairsText =  new PrintWriter(new FileWriter("MultiStage_" + file.getName() + "_" + threshold+".txt"));
        for (Pair text : frequentPairs) {
            pairsText.write(text.toString() + "\n");
        }
        pairsText.close();
    }

}

