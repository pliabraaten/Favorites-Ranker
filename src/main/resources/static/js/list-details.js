

// CSRF HELPER FUNCTIONS: extracts CSRF token from the meta tages in <head>
function getCsrfToken() {
    return document.querySelector('meta[name="_csrf"]')?.content || '';
}

function getCsrfHeader() {
    return document.querySelector('meta[name="_csrf_header"]')?.content || '';
}


// List Details State:
let currentListName;
let listId;


// UI FUNCTIONS
function enterEditMode() {  // Hides title, shows input field, and focus cursor
    document.getElementById('display-mode').style.display = 'none';
    document.getElementById('edit-mode').style.display = 'block';
    document.getElementById('list-name-input').focus();
}

function cancelEdit() {  // Reverts back to regular display mode
    document.getElementById('edit-mode').style.display = 'none';
    document.getElementById('display-mode').style.display = 'block';
    document.getElementById('list-name-input').value = currentListName;  // Prevents partial edits
}


// INITIALIZATION
function initListDetails({ id, name }) {
    listId = id;
    currentListName = name;

    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();

    // Event listeners
    document
        .getElementById('edit-name-btn')
        .addEventListener('click', enterEditMode);

    document
        .getElementById('cancel-edit-btn')
        .addEventListener('click', cancelEdit);

    const form = document.getElementById('edit-list-form');

    // Form submission
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const input = document.getElementById('list-name-input');
        const newName = input.value.trim();

        // Input validation
        if (!newName) {
            alert('List name cannot be empty');
            return;
        }

        // Build request headers
        const headers = {
            'Content-Type': 'application/json'
        };

        // Add CSRF if available
        if (csrfToken && csrfHeader) {
            headers[csrfHeader] = csrfToken;
        }

        try {
            const response = await fetch(`/api/lists/${listId}`, {
                method: 'PATCH',  // Patch for partial updates
                headers: headers,
                body: JSON.stringify({ listName: newName })
            });

            if (!response.ok) {
                alert('Error updating list name');
                return;
            }

            // Updates page title
            document.querySelector('#display-mode h1').textContent = newName;

            // Updates title
            const cardTitle = document.querySelector('.card-body h2');
            if (cardTitle) {
                cardTitle.textContent = newName;
            }

            currentListName = newName;
            cancelEdit();
            
        } catch (error) {
            console.error('Error:', error);
            alert('Error updating list name');
        }
    });
}