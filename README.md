# Favorites-Ranker
List ranking tool using pairwise comparison to determine preference order



Rank Algorithm: 
- initially started with iterating through every combination of elements
- updated to omit pairs of the same element
- changed to matrix to store winners as values and elements as indexes and to avoid repeated pairs (ab and then ba)
- changed back to list of elements and list of results (avoid repeated pairs)
  - ranked based on count of wins for each element
  - even omitting repeated pairs, it still is n*(n – 1)/2 comparisons or N^2
- change to binary insertion sort to decrease the number of comparisons for the user
