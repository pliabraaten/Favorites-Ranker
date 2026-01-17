// CSRF HELPER FUNCTIONS
function getCsrfToken() {
    return document.querySelector('meta[name="_csrf"]')?.content || '';
}

function getCsrfHeader() {
    return document.querySelector('meta[name="_csrf_header"]')?.content || '';
}


// List Details State
let currentListName;
let listId;


// UI FUNCTIONS
function enterEditMode() {
    document.getElementById('display-mode').style.display = 'none';
    document.getElementById('edit-mode').style.display = 'block';
    document.getElementById('list-name-input').focus();
}

function cancelEdit() {
    document.getElementById('edit-mode').style.display = 'none';
    document.getElementById('display-mode').style.display = 'block';
    document.getElementById('list-name-input').value = currentListName;
}


// INITIALIZATION
function initListDetails({ id, name }) {  // ✅ Removed csrfToken and csrfHeader params
    listId = id;
    currentListName = name;

    // ✅ Use helper functions instead of redeclaring
    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();

    document
        .getElementById('edit-name-btn')
        .addEventListener('click', enterEditMode);

    document
        .getElementById('cancel-edit-btn')
        .addEventListener('click', cancelEdit);

    const form = document.getElementById('edit-list-form');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const input = document.getElementById('list-name-input');
        const newName = input.value.trim();

        if (!newName) {
            alert('List name cannot be empty');
            return;
        }

        // ✅ Build headers object
        const headers = {
            'Content-Type': 'application/json'
        };

        // Add CSRF if available
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

            // ✅ Update all instances of list name on page
            document.querySelector('#display-mode h1').textContent = newName;
            
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