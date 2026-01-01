


// items list is populated from Thymeleaf when the page loads
console.log(items);  // FIXME: TESTING

let currentItemIndex = 0;  //
let comparisons = [];  //
let sortedItemIndex = 0;  // Tracks which items have been sorted
let itemCount = 0;  // Number of items in the list

let L = 0;  // Track left index
let R = 0;  // For calculating the middle


function rank() {

    for (let i = sortedItemIndex; i < itemCount; i++) {

        if (i > 0) {  // Skip first item (already sorted)

            // Sorted portion of the array
            L = 0;  // Left
            R = i - 1;  // Right
            let j = i - 1;  // i is the index of selectedItem, j is prior element
            let selectedItem = items[i];  // Set to item object for comparisons

            // Find rank position with pairwise comparisons via a binary sort comparison method
            let insertPosition = binarySearch(i, L, R, selectedItem);

            // Move the element to the ranked value position in the array
            insertionSort(j, insertPosition, i, selectedItem);

        }
    }
}


function binarySearch(i, L, R, selectedItem) {

    // JavaScript is asynchronous, so using recursive function
    if (L > R || R < 0) {  // Stop if no middle value indicating position is found

        return L;  // Found position
    }

    let M = L + (R - L) / 2);  // Find middle element in the sorted (left) side of the array

    // Prompt user -> provide callback for after selection
    pairwisePrompt(i, M, function(winner) {
        if (winner === selectedItem) {  // If selected item is ranked higher than middle

            // Recursively search left
            binarySearch(i, L, M - 1, selectedItem);  // L stays, R <- old middle - 1; continue on left side of sorted
        } else {

            // Recursively search right
            binarySearch(i, M + 1, R, selectedItem);  // Move left to middle and continue search on right side of sorted
        }
    }
}


function pairwisePrompt(i, M, callback) {  // callback

    // Show user pairwise comparison of current item (i) to the middle item (M)
        // Update the HTML to show both items
    document.getElementById('item1-name').textContent = list[i].name;
    document.getElementById('item2-name').textContent = list[M].name;

    // Pass back winner when user clicks on it with click handlers
    document.getElementById('item1-btn').onclick = function() {

        let winner = list[i];  // User clicked item 1 (selected item)
        callback(winner);  // Pass winner back with callback
    }

    document.getElementById('item2-btn').onclick = function() {
        let winner = rankedList[M];  // User clicked item 2 (current middle item)
        callback(winner);  // Pass winner back via callback
    };

}

function insertionSort(j, insertPosition, i, list, selectedItem) {



}




function showNextComparison() {

    // Get next pair to compare
    let [item1, item2] = getNextPair();
    
    // Update the HTML
    document.getElementById('item1-name').textContent = item1.name;
    document.getElementById('item2-name').textContent = item2.name;
    
    // Set button click handlers
    document.getElementById('item1-btn').onclick = () => chooseItem(item1, item2);
    document.getElementById('item2-btn').onclick = () => chooseItem(item2, item1);
}




//## The Flow
//```
//User visits page
//    ↓
//Show comparison 1: "Pizza" vs "Tacos"
//    ↓
//User clicks "Pizza"
//    ↓
//Record: Pizza > Tacos
//    ↓
//Show comparison 2: "Burgers" vs "Pizza"
//    ↓
//User clicks "Pizza"
//    ↓
//Record: Pizza > Burgers
//    ↓
//... continue until done ...
//    ↓
//Send all comparisons to backend as JSON
//    ↓
//Backend sorts and saves