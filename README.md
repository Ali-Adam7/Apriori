# Apriori Algorithm Implementations

This repository contains various implementations of the Apriori algorithm in Java, including several optimization techniques and variants. The Apriori algorithm is widely used for mining frequent itemsets and generating association rules.

### Algorithms Implemented
#### Apriori: 
  The basic algorithm for finding frequent itemsets and generating association rules.
#### Triangular Array: 
An optimization technique to reduce memory usage by using a triangular array for storing itemsets.
#### Hashmap: 
Utilizes hashmaps to efficiently count itemsets and manage large datasets.
#### PCY (Park-Chen-Yu): 
An optimization that uses a bitmap to reduce the number of candidate itemsets by hashing.
#### Multihash & Multistage: 
Further optimizes the PCY algorithm by using multiple hash functions and multiple passes over the data.
#### SON (Savasere, Omiecinski, and Navathe): 
A distributed approach that processes data in chunks to handle large datasets.
#### Sampling using Apriori: 
Reduces the size of the dataset by sampling while maintaining the ability to find frequent itemsets.
