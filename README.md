# Apriori
Apriori Algorithm Implementations


Algorithms implemented in Java:

1) Apriori 
  - Triangular Array
  - Hashmap
 
2) PCY
3) Mutlihash & Multistage
4) Son (Distributed & Chunks)
5) Sampling using Apriori

Performance on the Netflix Dataset (~500,000 Baskets): 

| Algorithm         |   1% Support   |   2% Support |
| Apriori (Array)   |      397       |      275     |
| Apriori (HashMap) |      1192      |      877     |
| PCY               |      1467      |      956     | 
| Multistage        |      1666      |      1317    |
| Sampling          |      76        |      63      |
| SON               |      1535      |      1060    |
| SON Distributed   |      95        |      70      |

