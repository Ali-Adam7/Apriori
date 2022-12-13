import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class Methods {

    /**
     * Scans the input file once and count how many baskets are there
     * 
     * @param file File to be scanned
     * @return Number of baskets in the file
     */
    public static int getBaskets(File file) throws FileNotFoundException {
        System.out.println("Counting Number of Baskets");

        int baskets = 0;
        Scanner dataset = new Scanner(file);
        while (dataset.hasNextLine()) {
            baskets++;
            dataset.nextLine();
        }
        dataset.close();
        System.out.println("# of Baskets: " + baskets);
        return baskets;

    }

    /**
     * Prints out statistics after running the Algorithm and generating frequent
     * pairs
     * 
     * @param file               Dataset processed
     * @param dataSize           Percentage of the file processed
     * @param baskets            Number of baskets in the file
     * @param support            Count of baskets that meets the threshold
     * @param threshold          Percentage of basket the itemset needs to be
     *                           present in to pass as frequent
     * @param transaction        Number of baskets that was processed
     * @param itemCount          Total number of items found
     * @param numOfFreqItems     Number of items found to be frequent
     * @param passOneTime        Runtime of pass 1
     * @param totalTime          Runtime of the Algorithm
     * @param endTimeOfPass1     The time pass 2 starts
     * @param numOfFrequentPairs Number of Pairs that were found to be frequent
     * @param pairsCount         Hashmap that contains count of each pair
     * 
     * 
     */
    public static void getStatistics(File file, double dataSize, int baskets, double support, double threshold,
            double transaction, int itemCount, int numOfFreqItems, long passOneTime, long totalTime,
            long endTimeOfPass1, int numOfFrequentPairs, HashMap<Pair, MutableShort> pairsCount) {
        System.out.println("=======================Parameters=======================");

        System.out.println("Dataset : " + file.getName());
        System.out.println("Threshold: " + threshold * 100 + "%");
        System.out.println("Data percentage: " + dataSize * 100 + "%");
        System.out.println("Transactions: " + transaction);
        System.out.println("Support: " + support);

        System.out.println("=======================Pass 1=======================");
        System.out.println("Pass 1 Run Time: " + passOneTime / 1000000000 + "s");
        System.out.println("# of Item: " + itemCount);
        double Candidates = (numOfFreqItems * (numOfFreqItems - 1) / 2);
        System.out.println("# of Frequent Items: " + numOfFreqItems);
        System.out.println("# of Candidate Pair: " + (Candidates));
        System.out.println("=======================Pass 2=======================");

        System.out.println("Pass 2 Run Time: " + (totalTime - passOneTime) / 1000000000);
        System.out.println("Total Run Time: " + totalTime / 1000000000);
        if (pairsCount != null) {
            System.out.println("# of Pairs that actually occured " + pairsCount.size() + " ("
                    + ((double) (pairsCount.size() / Candidates * 100)) + "%)");

        }

        System.out.println("# Of Frequent Pairs: " + numOfFrequentPairs);

    }

    /**
     * Prints out statistics after running the PCY Algorithm and generating frequent
     * pairs
     * 
     * @param file               Dataset processed
     * @param dataSize           Percentage of the file processed
     * @param baskets            Number of baskets in the file
     * @param support            Count of baskets that meets the threshold
     * @param threshold          Percentage of basket the itemset needs to be
     *                           present in to pass as frequent
     * @param transaction        Number of baskets that was processed
     * @param itemCount          Total number of items found
     * @param numOfFreqItems     Number of items found to be frequent
     * @param passOneTime        Runtime of pass 1
     * @param totalTime          Runtime of the Algorithm
     * @param endTimeOfPass1     The time pass 2 starts
     * @param numOfFrequentPairs Number of Pairs that were found to be frequent
     * @param PCYpairs           Number of canidate pairs after checking the buckets
     * 
     */
    public static void getStatisticsPCY(File file, double dataSize, int baskets, double support, double threshold,
            double transaction, int itemCount, int numOfFreqItems, long passOneTime, long totalTime,
            long endTimeOfPass1, int numOfFrequentPairs, int PCYpairs) {
        System.out.println("=======================Parameters=======================");

        System.out.println("Dataset : " + file.getName());
        System.out.println("Threshold: " + threshold * 100 + "%");
        System.out.println("Data percentage: " + dataSize * 100 + "%");
        System.out.println("Transactions: " + transaction);
        System.out.println("Support: " + support);

        System.out.println("=======================Pass 1=======================");
        System.out.println("Pass 1 Run Time: " + passOneTime / 1000000000 + "s");
        System.out.println("# of Item: " + itemCount);
        int num = ((numOfFreqItems * (numOfFreqItems - 1) / 2));
        System.out.println("# of Frequent Items: " + numOfFreqItems);
        System.out.println("# of Candidate Pair: Without PCY: " + num);
        System.out.println("# of Candidate Pair: With PCY: " + PCYpairs);
        System.out.println("Candidates eliminated: " + (num - PCYpairs) + " ("
                + (((double) ((double) (num - PCYpairs) / num)) * 100) + "%)");

        System.out.println("=======================Pass 2=======================");

        System.out.println("Pass 2 Run Time: " + (totalTime - passOneTime) / 1000000000);
        System.out.println("Total Run Time: " + totalTime / 1000000000);
        System.out.println("# Of Frequent Pairs: " + numOfFrequentPairs);
    }

    /**
     * Get a list of frequent pairs from triangular array method
     * 
     * @param matrix   Triangular array to store count of pairs
     * @param mapItems Hashmap to map back to the original item
     * @param support
     * @param size     Size of the tringular array
     * 
     * @return A list of frequent pairs
     */

    public static ArrayList<Pair> getFrequentPairs(int[] matrix, HashMap<Integer, Integer> mapItems, double support,
            int j) {

        ArrayList<Pair> frequentPairs = new ArrayList<Pair>();
        for (int i = 1; i < j + 1; i++) {
            for (int k = i + 1; k < j + 1; k++) {
                if (matrix[(((k - 1) * (k - 2)) / 2 + i)] >= support) {

                    frequentPairs.add(new Pair(mapItems.get(i), mapItems.get(k)));
                }

            }

        }

        return frequentPairs;
    }

    /**
     * Get a list of frequent pairs from Triples method (HashMap)
     * 
     * @param pairsCount HashMap to store count of pairs
     * @param support
     * @return A list of frequent pairs
     */

    public static ArrayList<Pair> getFrequentPairs(HashMap<Pair, MutableShort> pairsCount, double support) {
        ArrayList<Pair> frequentPairs = new ArrayList<Pair>();
        for (Entry<Pair, MutableShort> pair : pairsCount.entrySet()) {
            if (pair.getValue().get() >= support) {
                frequentPairs.add(pair.getKey());
            }

        }

        return frequentPairs;
    }

    /**
     * Get a Bit Map of frequent items from a HashMap count
     * 
     * @param itemCount HashMap to store count of items
     * @param support
     * @return A bit map of frequent items
     */
    public static BitSet getFrequentItems(HashMap<Integer, Integer> itemCount, double support) {
        BitSet frequentItems = new BitSet(itemCount.size());
        for (java.util.Map.Entry<Integer, Integer> item : itemCount.entrySet()) {
            if (item.getValue() >= support) {
                frequentItems.set(item.getKey(), true);

            } else {
                frequentItems.set(item.getKey(), false);

            }
        }
        return frequentItems;
    }

    /**
     * Run the Second Pass of the Apriori Algorithm using the Triangualr Matrix
     * approach to store count of pairs
     * 
     * @param file          Dataset to process
     * @param frequentItems Bit Map of frequent items
     * @param Array         Triangular Array to store count
     * @param MapItem       Maps items to ones in the array
     * @param start         first basket to process
     * @param end           last basket to process
     */
    public static void secondPass(File file, BitSet frequentItems, int[] Array,
            HashMap<Integer, Integer> MapItem, double start, double end)
            throws FileNotFoundException {
        int baskets = 0;
        Scanner dataset = new Scanner(file);
        while (dataset.hasNextLine()) {
            baskets++;
            String basket = dataset.nextLine();

            if (baskets < start) {
                continue;
            }

            if (baskets > end) {
                break;
            }
            String[] items = basket.split(" ");
            ArrayList<Integer> basketFreqItems = new ArrayList<>();
            for (int i = 0; i < items.length; i++) {
                int item = Integer.parseInt(items[i]);
                if (frequentItems.get(item) == true) {
                    basketFreqItems.add(item);
                }
            }
            Collections.sort(basketFreqItems);

            for (int i = 0; i < basketFreqItems.size(); i++) {
                for (int j = i + 1; j < basketFreqItems.size(); j++) {

                    int p3 = (MapItem.get(basketFreqItems.get(i)));
                    int p4 = (MapItem.get(basketFreqItems.get(j)));
                    incrementArray(Array, p3, p4);

                }
            }

        }

        dataset.close();
    }

    /**
     * Run the Second Pass of the Apriori Algorithm using the Hashmap Matrix to
     * store count of pairs
     * 
     * @param file          Dataset to process
     * @param frequentItems Bit Map of frequent items
     * @param pairsCount    HashMap to store count
     * @param start         first basket to process
     * @param end           last basket to process
     */
    public static void secondPass(File file, BitSet frequentItems, double support,
            HashMap<Pair, MutableShort> pairsCount,
            double start, double end)
            throws FileNotFoundException {
        System.out.println("Second Pass:");

        int baskets = 0;
        Scanner dataset = new Scanner(file);
        while (dataset.hasNextLine()) {
            baskets++;
            String basket = dataset.nextLine();

            if (baskets < start) {
                continue;
            }

            if (baskets > end) {
                break;
            }
            String[] items = basket.split(" ");
            ArrayList<Integer> basketFreqItems = new ArrayList<>();
            for (int i = 0; i < items.length; i++) {
                int item = Integer.parseInt(items[i]);
                if (frequentItems.get(item) == true) {
                    basketFreqItems.add(item);
                }
            }
            Collections.sort(basketFreqItems);
            for (int i = 0; i < basketFreqItems.size(); i++) {
                for (int j = i + 1; j < basketFreqItems.size(); j++) {

                    int p3 = basketFreqItems.get(i);
                    int p4 = basketFreqItems.get(j);

                    Pair p = new Pair(p3, p4);
                    MutableShort count = pairsCount.get(p);

                    if (count != null && count.get() < support) {
                        count.increment();
                    } else if (count == null) {
                        MutableShort newP = new MutableShort();
                        newP.increment();
                        pairsCount.put(p, newP);

                    }

                }
            }

        }
        System.out.println("Memory Usage: after Pass 2 "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");
        dataset.close();
    }

    /**
     * Run the Second Pass of the SON Algorithm to count the pairs found to be
     * locally frequent in the chunks
     * 
     * @param file          Dataset to process
     * @param frequentItems Bit Map of frequent items
     * @param pairsCount    HashMap to store count
     * @param start         first basket to process
     * @param end           last basket to process
     */
    public static void SONGlobalPairs(File file, BitSet frequentItems, double support,
            HashMap<Pair, MutableShort> pairsCount,
            double start, double end)
            throws FileNotFoundException {
        System.out.println("Second Pass:");

        int baskets = 0;
        Scanner dataset = new Scanner(file);
        while (dataset.hasNextLine()) {
            baskets++;
            String basket = dataset.nextLine();

            if (baskets < start) {
                continue;
            }

            if (baskets > end) {
                break;
            }
            String[] items = basket.split(" ");
            ArrayList<Integer> basketFreqItems = new ArrayList<>();
            for (int i = 0; i < items.length; i++) {
                int item = Integer.parseInt(items[i]);
                if (frequentItems.get(item) == true) {
                    basketFreqItems.add(item);
                }
            }
            Collections.sort(basketFreqItems);
            for (int i = 0; i < basketFreqItems.size(); i++) {
                for (int j = i + 1; j < basketFreqItems.size(); j++) {

                    int p3 = basketFreqItems.get(i);
                    int p4 = basketFreqItems.get(j);

                    Pair p = new Pair(p3, p4);

                    MutableShort count = pairsCount.get(p);

                    if (count != null && count.get() < support) {
                        count.increment();

                    }
                }
            }

        }
        System.out.println("Memory Usage: after Pass 2 "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");
        dataset.close();
    }

    /**
     * Increment count of the pair consiting of item1 and item2
     * 
     * @PreCondition item1 < item2
     * @param Array Dataset to process
     * @param item1
     * @param item2
     */
    public static void incrementArray(int[] Array, int item1, int item2) {
        // From the Mining of Massive Dataset book:
        Array[((((item2 - 1) * (item2 - 2))) / 2 + item1)]++;

    }

    /**
     * Run the Second Pass of the PCY Algorithm to store count of pairs in a hashmap
     * 
     * @param file          Dataset to process
     * @param frequentItems Bit Map of frequent items
     * @param pairsCount    HashMap to store count
     * @param support
     * @param bitmap        Bit Map to check if Pair is a canidate
     * @param start         irst basket to process
     * @param end           last basket to process
     * @throws FileNotFoundException
     */
    public static void secondPCYPass(File file, BitSet frequentItems, Map<Pair, MutableShort> pairsCount,
            double support,
            BitSet bitmap, double start, double end)
            throws FileNotFoundException {
        int baskets = 0;
        Scanner dataset = new Scanner(file);
        while (dataset.hasNextLine()) {
            baskets++;
            String basket = dataset.nextLine();

            if (baskets < start) {
                continue;
            }

            if (baskets > end) {

                break;
            }
            String[] items = basket.split(" ");

            ArrayList<Integer> basketFreqItems = new ArrayList<Integer>();
            for (int i = 0; i < items.length; i++) {
                int item = Integer.parseInt(items[i]);
                if (frequentItems.get(item) == true) {
                    basketFreqItems.add(item);
                }
            }
            Collections.sort(basketFreqItems);
            for (int i = 0; i < basketFreqItems.size(); i++) {
                for (int j = i + 1; j < basketFreqItems.size(); j++) {

                    int x = basketFreqItems.get(i);
                    int y = basketFreqItems.get(j);

                    Pair p = new Pair(x, y);
                    MutableShort old = pairsCount.get(p);
                    if (old != null && old.get() < support) {

                        old.increment();

                    }

                }
            }
        }

        System.out.println("Memory Usage: after pass 2: "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + " MB");

        dataset.close();

    }

    /**
     * Extact pairs written in a text file and save them in a HashSet
     * 
     * @param file Dataset to process
     * @throws FileNotFoundException
     * @returns HashSet containing pairs
     */

    public static HashSet<Pair> getPairsFromFile(File file) throws FileNotFoundException {
        HashSet<Pair> pairs = new HashSet<Pair>();
        Scanner dataset = new Scanner(file);
        while (dataset.hasNextLine()) {
            String basket = dataset.nextLine();
            String[] pair = basket.split(",");
            String x = "";
            String y = "";
            for (int i = 1; i < pair[0].length(); i++) {
                x = x + pair[0].charAt(i);
            }
            for (int i = 0; i < pair[1].length() - 1; i++) {
                y = y + pair[1].charAt(i);
            }

            Pair p = new Pair(Integer.parseInt(x), Integer.parseInt(y));

            pairs.add(p);

        }
        dataset.close();
        return pairs;
    }

    /**
     * First Pass of the PCY Algorithim with 2 arrays of buckets for hasing pairs
     * 
     * 
     * @param file       Dataset to process
     * @param itemCount  HashMap to store item count
     * @param pairsCount HashMap to store count
     * @param buckets
     * @param buckets2   Buckets of shorts to hash pairs
     * @param support    Second Buckets of shorts to hash pairs
     * @param start      first basket to process
     * @param end        last basket to process
     * @throws FileNotFoundException
     */
    public static void secondMultiStagePass(File file, BitSet frequentItems, BitSet bitmap, short[] buckets,
            double support,
            double start, double end)
            throws FileNotFoundException {
        int baskets = 0;
        Scanner dataset = new Scanner(file);
        while (dataset.hasNextLine()) {
            baskets++;
            String basket = dataset.nextLine();

            if (baskets < start) {
                continue;
            }

            if (baskets > end) {

                break;
            }

            String[] items = basket.split(" ");
            for (int i = 0; i < items.length; i++) {
                int item = Integer.parseInt(items[i]);
                if (frequentItems.get(item)) {
                    for (int j = i + 1; j < items.length; j++) {
                        int item2 = Integer.parseInt(items[j]);
                        if (frequentItems.get(item2)) {
                            int x = Math.min(item, item2);
                            int y = Math.max(item, item2);
                            int hash1 = fastModulo(getHashCode2(x, y), bitmap.size());
                            if (bitmap.get(hash1)) {
                                int hash = Methods.getHashCode(x, y);
                                int index = Methods.fastModulo(hash, buckets.length);
                                if (buckets[index] <= support) {
                                    buckets[index]++;

                                }
                            }

                        }

                    }

                }

            }

        }
        dataset.close();
    }

    /**
     * First Pass of the PCY Algorithim with 1 array of buckets for hasing pairs
     * 
     * 
     * @param file       Dataset to process
     * @param itemCount  HashMap to store item count
     * @param pairsCount HashMap to store count
     * @param buckets
     * @param support    Second Buckets of shorts to hash pairs
     * @param start      first basket to process
     * @param end        last basket to process
     * @throws FileNotFoundException
     */
    public static int firstPCYPass(File file, HashMap<Integer, Integer> itemCount, short[] buckets, double support,
            double start, double end)
            throws FileNotFoundException {
        int baskets = 0;
        Scanner dataset = new Scanner(file);
        while (dataset.hasNextLine()) {
            baskets++;
            String basket = dataset.nextLine();

            if (baskets < start) {
                continue;
            }

            if (baskets > end) {

                break;
            }

            String[] items = basket.split(" ");
            for (int i = 0; i < items.length; i++) {
                int item = Integer.parseInt(items[i]);
                if (itemCount.containsKey(item)) {
                    itemCount.put(item, itemCount.get(item) + 1);
                } else {
                    itemCount.put(item, 1);

                }

                for (int j = i + 1; j < items.length; j++) {
                    int item2 = Integer.parseInt(items[j]);
                    int x = Math.min(item, item2);
                    int y = Math.max(item, item2);
                    int hash = Methods.getHashCode2(x, y);
                    int index = Methods.fastModulo(hash, buckets.length);
                    if (buckets[index] <= support) {
                        buckets[index]++;

                    }

                }

            }
        }
        dataset.close();
        return itemCount.size();

    }

    /**
     * First Pass of the SON Algorithim to count the occurnces of local items and
     * determine which ones are gloabally frequent
     * 
     * 
     * @param file       Dataset to process
     * @param itemCount  HashMap to store item count
     * @param pairsCount HashMap to store count
     * @param start      first basket to process
     * @param end        last basket to process
     * @throws FileNotFoundException
     */
    public static int SONGlobalItems(File file, HashMap<Integer, Integer> itemCount, double start, double end)
            throws FileNotFoundException {
        int baskets = 0;
        Scanner dataset = new Scanner(file);
        while (dataset.hasNextLine()) {
            baskets++;
            String basket = dataset.nextLine();
            if (baskets < start) {
                continue;
            }
            if (baskets > end) {

                break;
            }
            String[] items = basket.split(" ");
            for (int i = 0; i < items.length; i++) {
                int item = Integer.parseInt(items[i]);

                if (itemCount.containsKey(item)) {
                    itemCount.put(item, itemCount.get(item) + 1);
                }

            }

        }
        dataset.close();
        return itemCount.size();

    }

    /**
     * Put Keys of Pairs into a hashmap with value 0 to count them later in Pass 2
     * 
     * @param frequentItems Bitmap of frequment items
     * @param pairsCount    HashMap to store Pair count
     * @param bitmap        Bitmap of the buckets that were used in hashing pairs in
     *                      Pass 1
     */
    public static void putPairsInTable(BitSet frequentItems, HashMap<Pair, MutableShort> pairsCount, BitSet bitmap) {
        for (int i = 0; i < frequentItems.size(); i++) {
            if (frequentItems.get(i)) {
                for (int j = i + 1; j < frequentItems.size(); j++) {
                    if (frequentItems.get(j)) {
                        Pair p = new Pair(i, j);
                        int hash2 = (Methods.getHashCode2(i, j));
                        if (bitmap.get(Methods.fastModulo(hash2, bitmap.size()))) {
                            pairsCount.put(p, new MutableShort());

                        }
                    }
                }
            }
        }
    }

    /**
     * Put Keys of Pairs into a hashmap with value 0 to count them later in Pass 2
     * 
     * @param frequentItems Bitmap of frequment items
     * @param pairsCount    HashMap to store Pair count
     * @param bitmap        Bitmap of the buckets that were used in hashing pairs in
     *                      Pass 1
     * @param bitmap2       Second Bitmap of the buckets that were used in hashing
     *                      pairs in Pass 1
     */
    public static void putPairsInTableMulti(BitSet frequentItems, HashMap<Pair, MutableShort> pairsCount, BitSet bitmap,
            BitSet bitmap2) {
        for (int i = 0; i < frequentItems.size(); i++) {
            if (frequentItems.get(i)) {
                for (int j = i + 1; j < frequentItems.size(); j++) {
                    if (frequentItems.get(j)) {
                        Pair p = new Pair(i, j);
                        int hash2 = (Methods.getHashCode(i, j));
                        int hash = (Methods.getHashCode2(i, j));

                        if (bitmap.get(Methods.fastModulo(hash, bitmap.size()))
                                && bitmap2.get(Methods.fastModulo(hash2, bitmap2.size()))) {
                            pairsCount.put(p, new MutableShort());

                        }
                    }
                }
            }
        }
    }

    /**
     * First Pass of the Apriori Algorithim to count the occurnces of items of size
     * 1
     * 
     * @param file      Dataset to process
     * @param itemCount HashMap to store item count
     * @param start     first basket to process
     * @param end       last basket to process
     * @throws FileNotFoundException
     */
    public static int firstPass(File file, HashMap<Integer, Integer> itemCount, double start, double end)
            throws FileNotFoundException {
        int baskets = 0;
        Scanner dataset = new Scanner(file);
        while (dataset.hasNextLine()) {
            baskets++;
            String basket = dataset.nextLine();

            if (baskets < start) {
                continue;
            }

            if (baskets > end) {

                break;
            }

            String[] items = basket.split(" ");
            for (int i = 0; i < items.length; i++) {
                int item = Integer.parseInt(items[i]);
                if (itemCount.containsKey(item)) {
                    itemCount.put(item, itemCount.get(item) + 1);
                } else {
                    itemCount.put(item, 1);

                }
            }

        }
        dataset.close();
        return itemCount.size();

    }

    /**
     * First Pass of the Apriori Algorithim to count the occurnces of items of size
     * 1
     * 
     * @param x     HashCode of the Pair
     * @param N     Number of Buckets
     * @param Index Bucket Index to increment count
     */
    public static int fastModulo(int x, int N) {
        // If N is a power of 2, then the Modulo operation can be performed faster using
        // AND bitwise operation:
        // https://www.chrisnewland.com/high-performance-modulo-operation-317
        return x & (N - 1);

    }

    /**
     * Pairs two numbers to a unique number using the szudzik pairing function
     * 
     * @param x        HashCode of the Pair
     * @param y        Number of Buckets
     * @param HashCode Hash code of the pair
     */
    public static int getHashCode(int x, int y) {
        // szudzik pairing function
        // http://szudzik.com/ElegantPairing.pdf
        return ((y * y) + x);

    }

    /**
     * Pairs two numbers to a unique number using the triangular mapping approach if
     * x < y
     * 
     * @param x        HashCode of the Pair
     * @param y        Number of Buckets
     * @param HashCode Hash code of the pair
     */
    public static int getHashCode2(Integer x, Integer y) {
        // From the Mining of Massive Dataset book:
        return (((y) * (y - 1)) / 2) + ((x));

    }

    /**
     * Map non consecative item numbers to consecative numbers starting from 1 to N
     * 
     * @param frequentItems HashCode of the Pair
     * @param map1          Map items to consecative numbers
     * @param map2          Map items back to their original number
     * @param n             Number of items
     */
    public static int mapItems(BitSet frequentItems, HashMap<Integer, Integer> map1, HashMap<Integer, Integer> map2) {
        int n = 0;
        for (int i = 0; i < frequentItems.size(); i++) {
            if (frequentItems.get(i) == true) {
                n++;
                map1.put(i, n);
                map2.put(n, i);
            }
        }
        return n;
    }

    /**
     * Summarize buckets into Bit Map to save space
     * 
     * @param buckets Bucket of shorts that contain count of pairs that hashed to
     *                that bucket
     * @param support Support of Items
     * @param bitmap  Bit map of frequent buckets, each bucket is summarized to
     *                either True Or False, which is 1 bit.
     */
    public static BitSet bucketsToBitMap(short[] buckets, double support) {
        BitSet bitmap = new BitSet(buckets.length);
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] >= support) {
                bitmap.set(i, true);
            } else {
                bitmap.set(i, false);
            }
        }
        return bitmap;
    }

}
