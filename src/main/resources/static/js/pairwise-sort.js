


let items = [];  //
let currentItemIndex = 0;  //
let comparisons = [];  //
let sortedItemIndex = 0;  // Tracks which items have been sorted
let itemCount = 0;  // Number of items in the list

let L = 0;  // Track left index
let R = 0;  // For calculating the middle
let selectedItem = "";  // Name of item being ranked


function rank() {

    for (let i = sortedItemIndex; i < itemCount; i++) {

        if (i > 0) {  // Skip first item (already sorted)

            // Sorted portion of the array
            L = 0;  // Left
            R = i - 1;  // Right
            let j = i - 1;  // i is the index of selectedItem, j is prior element

            // Find rank position with pairwise comparisons via a binary sort comparison method
            let insertPosition = binarySearch(i, L, R, rankedList, selectedItem);

            // Move the element to the ranked value position in the array
            insertionSort(j, insertPosition, i, rankedList, selectedItem);


        }
    }
}

function binarySearch(i, L, R, rankedList, selectedItem) {

    while (L <= R  && R >= 0) {  // Repeat until no more middle value

        int M = L + (R - L) / 2;  // Find middle element in the sorted (left) side of the array

        // Prompt user to compare nextItem (i) to middle item (M)
        let winner = pairwisePrompt(i, M, rankedList);

        // If selected item is ranked higher than middle
        if (winner.equals(selectedItem)) {

            R = M - 1;  // L stays, R <- old middle; continue on left side of sorted
        }
        // If selected items is ranked lower than middle
        else if (winner.equals(rankedList.get(M))) {

            L = M + 1;  // Move left to middle and continue search on right side of sorted
        }
    }
    return L;


}


function insertionSort(j, insertPosition, i, rankedList, selectedItem) {



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

function chooseItem(winner, loser) {
    // Record the comparison
    comparisons.push({
        item1Id: winner.id,
        item2Id: loser.id,
        winnerId: winner.id
    });
    
    // Show next pair or finish
    if (moreComparisonsNeeded()) {
        showNextComparison();
    } else {
        sendResultsToBackend();
    }
}
```

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