// ==============================
// main.js - Global Scripts
// ==============================


// -----------------------------
// DARK MODE
// -----------------------------
document.addEventListener('DOMContentLoaded', () => {
    const toggle = document.getElementById('dark-mode-toggle');
    const body = document.body;

    if (!toggle) return;  // Stop if toggle not present on page

    // Load saved preference
    if(localStorage.getItem('dark-mode') === 'enabled') {
        body.classList.add('dark-mode');
        updateToggleIcon(toggle, true);
    }

    toggle.addEventListener('click', (e) => {
        e.preventDefault();
        body.classList.toggle('dark-mode');

        const isDark = body.classList.contains('dark-mode');
        updateToggleIcon(toggle, isDark);

        // Save preference
        localStorage.setItem('dark-mode', isDark ? 'enabled' : 'disabled');
    });

    // Function to update icon and text
    function updateToggleIcon(toggleEl, isDark) {
        const icon = toggleEl.querySelector('i');
        if(!icon) return;

        if(isDark) {
            icon.classList.replace('bi-moon', 'bi-sun');
            toggleEl.textContent = ' Light Mode';
            toggleEl.prepend(icon);
        } else {
            icon.classList.replace('bi-sun', 'bi-moon');
            toggleEl.textContent = ' Dark Mode';
            toggleEl.prepend(icon);
        }
    }
});


// -----------------------------
// CSRF HELPER FUNCTIONS
// extracts CSRF token from the meta tags in <head>
// -----------------------------
function getCsrfToken() {
    return document.querySelector('meta[name="_csrf"]')?.content || '';
}

function getCsrfHeader() {
    return document.querySelector('meta[name="_csrf_header"]')?.content || '';
}
