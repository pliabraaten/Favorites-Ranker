// RANK LIST BY PROMPTING USER WITH PAIRWISE COMPARISONS AND USE BINARY INSERTION SORT

/* FIXME: ? Replace recursive binary search with an explicit state machine ?
     I am modeling binary search which is a synchronous algorithm with asynchronous user input via callbacks
     Control flow is split across multiple call frames instead of a step by step singe state
     But, stack depth is bounded by O(log n) per item
*/

// FIXME: concerns with the Arrow's impossibility theorem: A > B, B > C, but C > A is possible in preferences

    /* BINARY INSERTION SORT
        https://www.geeksforgeeks.org/dsa/binary-insertion-sort/
        https://en.wikipedia.org/wiki/Insertion_sort#Variants

        Time Complexity:
            Comparisons with binary search: O(n log n) -> O(log n) comparisons per element resulting in O(n log n) comparisons overall
            Insertion and shifting items: O(n^2)
            Overall: O(n^2)
         The algorithm as a whole has a running time of O(n^2) on average/worse case because of the swaps required for each insertion.
    */


// items list is populated from Thymeleaf when the page loads
console.log(items);  // FIXME: TESTING

let sortedItemIndex = 1;  // Tracks which items have been sorted -> skips first item already sorted


// START WHEN PAGE LOADS
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


// START RANKING FUNCTION: select item to be ranked, set boundaries, and call binarySearch()
function rankItem() {

    // If all items have been sorted
    if (sortedItemIndex >= items.length) {

        finishRanking();  // Print note and save
        return;
    }

    let selectedItem = items[sortedItemIndex];  // Item being ranked
    let L = 0;  // Left boundary
    let R = sortedItemIndex - 1;  // Right boundary is index prior to selected item

    binarySearch(sortedItemIndex, L, R, selectedItem);  // Find position with binary search method
}


// BINARY SEARCH: recursive with user prompts to find position for each item
function binarySearch(i, L, R, selectedItem) {

    // JavaScript is asynchronous, so using recursive function
    if (L > R || R < 0) {  // Stop if no middle value indicating position is found

        // Insert item in the found position
        insertItem(L, i, selectedItem);  // If condition is true, L is insert position

        moveToNextItem();  // NEXT ITEM

        return;
    }

    let M = Math.floor((L + (R - L)) / 2);  // Find middle element in the sorted (left) side of the array

    // PROMPT USER -> provide callback for after selection of winning item
    pairwisePrompt(i, M, function(winner) {
        if (winner === selectedItem) {  // If selected item is ranked higher than middle

            // Recursively search left
            binarySearch(i, L, M - 1, selectedItem);  // L stays, R <- old middle - 1; continue on left side of sorted
        } else {

            // Recursively search right
            binarySearch(i, M + 1, R, selectedItem);  // Move left to middle and continue search on right side of sorted
        }
    });
}


// PROMPT USER WITH COMPARISON: find and pass back the winning item
function pairwisePrompt(i, M, callback) {  // callback

    let hasResponded = false;  // Flag to prevent double clicks by user

    // Show user pairwise comparison of current item (i) to the middle item (M)
        // Update the HTML to show both items
    document.getElementById('item1-name').textContent = items[i].name;
    document.getElementById('item2-name').textContent = items[M].name;

    // Pass back winner when user clicks on it with click handlers
    document.getElementById('item1-btn').onclick = function() {
        if (hasResponded) return;  // Prevent double clicks
        hasResponded = true;  // Flag that click has occurred
        let winner = items[i];  // User clicked item 1 (selected item)
        callback(winner);  // Pass winner back with callback
    };
    document.getElementById('item2-btn').onclick = function() {
        if (hasResponded) return;  // Prevent double clicks
        hasResponded = true;  // Flag that click has occurred
        let winner = items[M];  // User clicked item 2 (current middle item)
        callback(winner);  // Pass winner back via callback
    };
}


// UPDATE ITEM POSITION VALUES: insert item and move all lower ranked items 1 index to the right
function insertItem(insertPosition, i, selectedItem) {

    let j = i - 1;  // j is prior item, i is current item

    while (j >= insertPosition) {

        items[j + 1] = items[j];  // Move item one place to the right (j starts as element immediately to the left of selectedItem)
            // Put prior element at the index of element to its right

        j--;  // Move to the next element to the left

    }
    // Keep moving elements to the right 1 until at insertPosition, then put value of selectedItem there
    items[j + 1] = selectedItem;
}


// INCREMENT TO THE NEXT ITEM TO BE SORTED
function moveToNextItem() {

    sortedItemIndex++;
    rankItem();
}


// ALL ITEMS RANKED: print message, reposition items in array, and save
function finishRanking() {

    // Hide comparison area
    document.getElementById('comparison-area').innerHTML =
        '<h3>Ranked Up!</h3><p>Saving your rankings...</p>';

    // Mapped values in the DTO rankedItems array based on ranked position in the object items array
    let rankedItems = items.map((item, index) => ({
        id: item.id,
        position: index + 1  // Set index + 1 as item.position value
    }));

    // Send DTO array to backend
    saveRankings(rankedItems);
}


// SAVE rankedItems LIST
function saveRankings(rankedItems) {
    fetch(`/api/lists/${listId}/save-rankings`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
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