


let items = [...];  // All items from your list
let comparisons = [];  // Store user choices
let currentPair = 0;

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