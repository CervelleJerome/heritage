// Custom JavaScript for the application

// Wait for the DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    console.log('Application initialized');
    
    // Add active class to current nav item based on URL
    highlightCurrentNavItem();
    
    // Initialize tooltips if they exist
    initializeTooltips();
    
    // Add any custom event listeners
    addEventListeners();
});

// Highlight the current navigation item based on URL
function highlightCurrentNavItem() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.navbar-nav .nav-link');
    
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href === currentPath || 
            (href !== '/' && currentPath.startsWith(href))) {
            link.classList.add('active');
        }
    });
}

// Initialize Bootstrap tooltips
function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

// Add custom event listeners
function addEventListeners() {
    // Example: Add confirmation to delete buttons
    const deleteButtons = document.querySelectorAll('.btn-danger[href*="delete"]');
    deleteButtons.forEach(button => {
        if (!button.hasAttribute('onclick')) {
            button.addEventListener('click', function(e) {
                if (!confirm('Êtes-vous sûr de vouloir supprimer cet élément ?')) {
                    e.preventDefault();
                }
            });
        }
    });
}