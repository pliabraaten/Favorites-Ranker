// ==============================
// pairwise-sort.js - Ranking Page
// ==============================

// RANK LIST BY PROMPTING USER WITH PAIRWISE COMPARISONS AND USE BINARY INSERTION SORT

/* FIXME: ? Replace recursive binary search with an explicit state machine ?
     I am modeling binary search which is a synchronous algorithm with asynchronous user input via callbacks
     Control flow is split across multiple call frames instead of a step by step single state
     But, stack depth is bounded by O(log n) per item
*/

/* FIXME: concerns with the Arrow's impossibility theorem:
   A > B, B > C, but C > A is possible in preferences
*/


/* BINARY INSERTION SORT
    https://www.geeksforgeeks.org/dsa/binary-insertion-sort/
    https://en.wikipedia.org/wiki/Insertion_sort#Variants

    Time Complexity:
        Comparisons with binary search: O(n log n) -> O(log n) comparisons per element resulting in O(n log n) comparisons overall
        Insertion and shifting items: O(n^2)
        Overall: O(n^2) on average/worst case because of the swaps required for each insertion.
*/


// -----------------------------
// START WHEN PAGE LOADS
// -----------------------------
// TODO:
let sortedItemIndex = 1;  // Tracks which items have been sorted -> skips first item already sorted

document.addEventListener('DOMContentLoaded', function() {

    if (items.length === 0) {  // If no items in the list
        alert('No items to rank!');
        return;
    }

    if (items.length === 1) {  // If only 1 item in list
        finishRanking();
        return;
    }

    rankItem();  // Start ranking
});


// -----------------------------
// START RANKING FUNCTION
// -----------------------------
function rankItem() {

    // If all items have been sorted
    if (sortedItemIndex >= items.length) {
        finishRanking();  // Move to print note and save
        return;
    }

    let selectedItem = items[sortedItemIndex];  // Item being ranked
    let L = 0;  // Left boundary
    let R = sortedItemIndex - 1;  // Right boundary is index prior to selected item

    binarySearch(sortedItemIndex, L, R, selectedItem);  // Find position with binary search method
}


// -----------------------------
// BINARY SEARCH
// -----------------------------
function binarySearch(i, L, R, selectedItem) {

  // Base case: search range exhausted, position found
  if (L > R || R < 0) {
    // Insert item at the found position (L is the insertion index)
    insertItem(L, i, selectedItem);
    moveToNextItem();
    return;
  }

  // Find middle element in the sorted portion of the array
  let M = L + Math.floor((R - L) / 2);

  // Prompt user to compare selectedItem with item at position M
  pairwisePrompt(i, M, function(winner) {
    if (winner.id === selectedItem.id) {
      // Selected item ranked higher - search left half
      binarySearch(i, L, M - 1, selectedItem);
    } else {
      // Middle item ranked higher - search right half
      binarySearch(i, M + 1, R, selectedItem);
    }
  });
}


// -----------------------------
// PROMPT USER WITH COMPARISON
// -----------------------------
function pairwisePrompt(i, M, callback) {  // callback
    let hasResponded = false;  // Flag to prevent double clicks by user

    // Show user pairwise comparison of current item (i) to the middle item (M)
    document.getElementById('item1-btn').textContent = items[i].name;
    document.getElementById('item2-btn').textContent = items[M].name;

    document.getElementById('item1-btn').onclick = function() {
        if (hasResponded) return;
        hasResponded = true;
        let winner = items[i];
        callback(winner);
    };
    document.getElementById('item2-btn').onclick = function() {
        if (hasResponded) return;
        hasResponded = true;
        let winner = items[M];
        callback(winner);
    };
}


// -----------------------------
// INSERT ITEM IN ARRAY
// -----------------------------
function insertItem(insertPosition, i, selectedItem) {
    let j = i - 1;

    while (j >= insertPosition) {
        items[j + 1] = items[j];
        j--;
    }

    items[j + 1] = selectedItem;
 }


// -----------------------------
// NEXT ITEM
// -----------------------------
function moveToNextItem() {
    sortedItemIndex++;
    updateListSortedCount(sortedItemIndex);  // Save updated sortedCount to db
    rankItem();
}


// -----------------------------
// UPDATE SORTED COUNT IN DATABASE
// -----------------------------
function updateListSortedCount(sortedCount) {
    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();

    fetch(`/api/lists/${listId}/sorted-count`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({ sortedCount: sortedCount })
    })
    .catch(error => {
        console.error('Error updating sorted count:', error);
    });
}


// -----------------------------
// FINISH RANKING
// -----------------------------
function finishRanking() {
    document.getElementById('comparison-area').innerHTML =
        '<h3>Ranked Up!</h3><p>Saving your rankings...</p>';

    // Map ranked items
    let rankedItems = items.map((item, index) => ({
        id: item.id,
        position: index + 1
    }));

    // Save rankings to backend
    saveRankings(rankedItems);
}


// -----------------------------
// SAVE RANKINGS
// -----------------------------
function saveRankings(rankedItems) {
    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();

    fetch(`/api/lists/${listId}/save-rankings`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({ items: rankedItems })
    })
    .then(response => {
        if (response.ok) {
            window.location.href = `/lists/${listId}`;
        } else {
            alert('Error saving rankings. Please try again.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error saving rankings. Please try again.');
    });
}
