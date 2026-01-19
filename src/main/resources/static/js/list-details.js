// ==============================
// List Details Page
// ==============================

// -----------------------------
// STATE
// -----------------------------
let currentListName;
let listId;

// -----------------------------
// LIST NAME EDITING
// -----------------------------
function enterListEditMode() {
    document.getElementById('display-mode').style.display = 'none';  // Hide the title
    document.getElementById('edit-mode').style.display = 'block';  // Show the input form
    document.getElementById('list-name-input').focus();  // Put cursor in input
}

function cancelListEdit() {
    document.getElementById('edit-mode').style.display = 'none';  // Hide input form
    document.getElementById('display-mode').style.display = 'block';  // Show the title
    document.getElementById('list-name-input').value = currentListName;  // Reset any changes
}

async function handleListNameSubmit(e) {
    e.preventDefault();

    const input = document.getElementById('list-name-input');
    const newName = input.value.trim();

    if (!newName) {
        alert('List name cannot be empty');
        return;
    }

    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();
    const headers = { 'Content-Type': 'application/json' };

    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    try {
        const response = await fetch(`/api/lists/${listId}`, {
            method: 'PATCH',
            headers: headers,
            body: JSON.stringify({ listName: newName })
        });

        if (!response.ok) {
            alert('Error updating list name');
            return;
        }

        // Update UI
        document.querySelector('#display-mode h1').textContent = newName;
        const cardTitle = document.querySelector('.card-body h2');
        if (cardTitle) cardTitle.textContent = newName;

        currentListName = newName;
        cancelListEdit();

    } catch (error) {
        console.error('Error:', error);
        alert('Error updating list name');
    }
}

// -----------------------------
// ITEM NAME EDITING
// -----------------------------
function enterItemEditMode(button) {
    closeAllItemEditModes();  // Close any open edit modes

    // Find and store html references
    const listItem = button.closest('li');
    const container = listItem.querySelector('.item-container');
    const actionButtons = container.querySelector('.item-action-buttons');
    const editMode = container.querySelector('.item-edit-mode');
    const displayMode = container.querySelector('.item-display-mode');

    // Hide action buttons and display mode (item name)
    actionButtons.classList.remove('d-flex');  // Remove the d-flex display
    actionButtons.classList.add('d-none');  // Hide the action buttons div
    displayMode.classList.remove('d-flex');
    displayMode.classList.add('d-none');

    // Show edit mode
    editMode.style.display = 'block';  // Make the .item-edit-mode class visible ias block element
    editMode.querySelector('.item-name-input').focus();  // Put cursor in
}

function cancelItemEdit(button) {

    // Find and store refrences
    const listItem = button.closest('li');
    const container = listItem.querySelector('.item-container');
    const actionButtons = container.querySelector('.item-action-buttons');
    const editMode = container.querySelector('.item-edit-mode');
    const displayMode = container.querySelector('.item-display-mode');

    // Hide edit mode
    editMode.style.display = 'none';

    // Show display mode
    displayMode.style.display = 'flex';

    // Show action buttons and display mode
    actionButtons.classList.remove('d-none');
    actionButtons.classList.add('d-flex');
    displayMode.classList.add('d-flex');
    displayMode.classList.remove('d-none');

    // Reset input value and placeholder to original
    const originalName = displayMode.dataset.itemName;
    const input = editMode.querySelector('.item-name-input');
    input.value = originalName;
    input.placeholder = originalName;
}

function closeAllItemEditModes() {

    // Hide all edit modes
    document.querySelectorAll('.item-edit-mode').forEach(el => {
        el.style.display = 'none';
    });

    // Show all display modes
    document.querySelectorAll('.item-display-mode').forEach(el => {
        el.style.display = 'flex';
    });

    // Show all action buttons
    document.querySelectorAll('.item-action-buttons').forEach(el => {
        el.classList.remove('d-none');
        el.classList.add('d-flex');
    });
}

async function handleItemNameSubmit(e, form) {
    e.preventDefault();

    const container = form.closest('.item-container');
    const itemId = container.dataset.itemId;
    const input = container.querySelector('.item-name-input');
    const newName = input.value.trim();

    if (!newName) {
        alert('Item name cannot be empty');
        return;
    }

    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();
    const headers = { 'Content-Type': 'application/json' };

    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    try {
        const response = await fetch(`/api/items/${itemId}`, {
            method: 'PATCH',
            headers: headers,
            body: JSON.stringify({ itemName: newName })
        });

        if (!response.ok) {
            alert('Error updating item name');
            return;
        }

        // Update display mode text
        const displayMode = container.querySelector('.item-display-mode');
        displayMode.querySelector('.item-name-display').textContent = newName;
        displayMode.dataset.itemName = newName;

        // Exit edit mode
        cancelItemEdit(form.querySelector('.cancel-edit-item-btn'));

    } catch (error) {
        console.error('Error:', error);
        alert('Error updating item name');
    }
}

// -----------------------------
// EVENT DELEGATION
// -----------------------------
function setupItemEventListeners() {
    // Use event delegation for dynamically created item elements
    document.addEventListener('click', function(e) {
        // Edit button click
        if (e.target.closest('.edit-item-btn')) {
            enterItemEditMode(e.target.closest('.edit-item-btn'));
        }

        // Cancel button click
        if (e.target.closest('.cancel-edit-item-btn')) {
            cancelItemEdit(e.target.closest('.cancel-edit-item-btn'));
        }
    });

    // Form submission
    document.addEventListener('submit', function(e) {
        if (e.target.classList.contains('edit-item-form')) {
            handleItemNameSubmit(e, e.target);
        }
    });
}

// -----------------------------
// INITIALIZATION
// -----------------------------
function initListDetails({ id, name }) {
    listId = id;
    currentListName = name;

    // List name editing
    document.getElementById('edit-name-btn')
        ?.addEventListener('click', enterListEditMode);

    document.getElementById('cancel-edit-btn')
        ?.addEventListener('click', cancelListEdit);

    document.getElementById('edit-list-form')
        ?.addEventListener('submit', handleListNameSubmit);

    // Item editing (uses event delegation)
    setupItemEventListeners();
}