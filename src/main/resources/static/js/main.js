// Custom JavaScript for the application

// Wait for the DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    console.log('Application initialized');

    try {
        // Add active class to current nav item based on URL
        console.log('Highlighting current nav item');
        highlightCurrentNavItem();
        console.log('Nav item highlighted successfully');
    } catch (error) {
        console.error('Error in highlightCurrentNavItem:', error);
    }

    try {
        // Initialize tooltips if they exist
        console.log('Initializing tooltips');
        initializeTooltips();
        console.log('Tooltips initialized successfully');
    } catch (error) {
        console.error('Error in initializeTooltips:', error);
    }

    try {
        // Add any custom event listeners
        console.log('Adding event listeners');
        addEventListeners();
        console.log('Event listeners added successfully');
    } catch (error) {
        console.error('Error in addEventListeners:', error);
    }

    try {
        // Initialize view toggle if it exists
        console.log('Initializing view toggle');
        initializeViewToggle();
        console.log('View toggle initialized successfully');
    } catch (error) {
        console.error('Error in initializeViewToggle:', error);
    }

    // Final attempt to make cards clickable, in case they weren't made clickable by other methods
    try {
        console.log('Final attempt to make cards clickable');
        setTimeout(makeCardsClickable, 200); // Longer delay to ensure all DOM updates are complete
    } catch (error) {
        console.error('Error in final makeCardsClickable call:', error);
    }
});

// Highlight the current navigation item based on URL
function highlightCurrentNavItem() {
    console.log('Starting highlightCurrentNavItem');
    const currentPath = window.location.pathname;
    console.log(`Current path: ${currentPath}`);

    const navLinks = document.querySelectorAll('.navbar-nav .nav-link');
    console.log(`Found ${navLinks.length} navigation links`);

    if (navLinks.length === 0) {
        console.log('No navigation links found on the page');
        return;
    }

    let activeFound = false;

    navLinks.forEach((link, index) => {
        try {
            const href = link.getAttribute('href');
            console.log(`Link ${index + 1} href: ${href}`);

            if (href === currentPath || (href !== '/' && currentPath.startsWith(href))) {
                console.log(`Marking link ${index + 1} as active`);
                link.classList.add('active');
                activeFound = true;
            }
        } catch (error) {
            console.error(`Error processing navigation link ${index + 1}:`, error);
        }
    });

    console.log(`Navigation highlighting complete. Active link found: ${activeFound}`);
}

// Initialize Bootstrap tooltips
function initializeTooltips() {
    // Check if bootstrap is defined
    if (typeof bootstrap === 'undefined') {
        console.error('Bootstrap is not defined. Make sure bootstrap.js is loaded correctly.');
        return;
    }

    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    if (tooltipTriggerList.length > 0) {
        console.log(`Found ${tooltipTriggerList.length} tooltip elements`);
        tooltipTriggerList.forEach(function (tooltipTriggerEl) {
            try {
                new bootstrap.Tooltip(tooltipTriggerEl);
            } catch (error) {
                console.error('Error initializing tooltip:', error);
            }
        });
    } else {
        console.log('No tooltip elements found');
    }
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

// Make entity cards clickable
function makeCardsClickable() {
    console.log('Starting makeCardsClickable');
    const entityCards = document.querySelectorAll('.entity-card');
    console.log(`Found ${entityCards.length} entity cards`);

    if (entityCards.length === 0) {
        console.log('No entity cards found on the page');
        return;
    }

    entityCards.forEach((card, index) => {
        try {
            console.log(`Processing card ${index + 1}`);

            // Find the edit button to extract the URL
            const editButton = card.querySelector('a[href*="/edit"]');
            if (!editButton) {
                console.log(`No edit button found for card ${index + 1}`);
                return;
            }

            const editUrl = editButton.getAttribute('href');
            console.log(`Edit URL for card ${index + 1}: ${editUrl}`);

            // Make the card clickable
            card.style.cursor = 'pointer';

            // Add click event to the card
            card.addEventListener('click', function(e) {
                try {
                    console.log(`Card ${index + 1} clicked`);

                    // Don't trigger if clicking on a button or link
                    if (e.target.tagName === 'A' || e.target.tagName === 'BUTTON' || 
                        e.target.closest('a') || e.target.closest('button')) {
                        console.log('Click on button or link, not triggering card click');
                        return;
                    }

                    console.log(`Navigating to ${editUrl}`);
                    // Navigate to the edit page
                    window.location.href = editUrl;
                } catch (error) {
                    console.error(`Error in click handler for card ${index + 1}:`, error);
                }
            });

            console.log(`Card ${index + 1} setup complete`);
        } catch (error) {
            console.error(`Error setting up card ${index + 1}:`, error);
        }
    });

    console.log('Card click setup complete');
}

// Initialize view toggle functionality
function initializeViewToggle() {
    console.log('Starting initializeViewToggle');
    const viewToggleGroups = document.querySelectorAll('.view-toggle-group');
    console.log(`Found ${viewToggleGroups.length} view toggle groups`);

    if (viewToggleGroups.length === 0) {
        console.log('No view toggle groups found on the page');
        return;
    }

    viewToggleGroups.forEach((group, index) => {
        console.log(`Processing view toggle group ${index + 1}`);
        const buttons = group.querySelectorAll('.view-toggle-btn');
        console.log(`Found ${buttons.length} toggle buttons in group ${index + 1}`);

        if (buttons.length === 0) {
            console.log(`No toggle buttons found in group ${index + 1}`);
            return;
        }

        const parentContainer = group.closest('.card-body');
        if (!parentContainer) {
            console.log(`No parent container found for group ${index + 1}`);
            return;
        }

        const listContainer = parentContainer.querySelector('.list-container');
        const cardContainer = parentContainer.querySelector('.card-container');

        if (!listContainer) {
            console.log(`No list container found for group ${index + 1}`);
            return;
        }

        if (!cardContainer) {
            console.log(`No card container found for group ${index + 1}`);
            return;
        }

        console.log(`Setting up click handlers for ${buttons.length} buttons in group ${index + 1}`);
        buttons.forEach((button, btnIndex) => {
            button.addEventListener('click', function() {
                try {
                    console.log(`Button ${btnIndex + 1} clicked in group ${index + 1}`);
                    const viewType = this.getAttribute('data-view');
                    console.log(`View type: ${viewType}`);

                    // Update active state on buttons within this group
                    group.querySelectorAll('.view-toggle-btn').forEach(btn => {
                        btn.classList.remove('active');
                    });
                    this.classList.add('active');

                    // Toggle visibility of containers
                    if (viewType === 'list') {
                        listContainer.classList.remove('d-none');
                        cardContainer.classList.add('d-none');
                        // Store preference in localStorage
                        try {
                            localStorage.setItem('preferredView', 'list');
                        } catch (e) {
                            console.error('Error saving to localStorage:', e);
                        }
                    } else {
                        listContainer.classList.add('d-none');
                        cardContainer.classList.remove('d-none');
                        // Store preference in localStorage
                        try {
                            localStorage.setItem('preferredView', 'card');
                        } catch (e) {
                            console.error('Error saving to localStorage:', e);
                        }

                        // Make cards clickable when switching to card view
                        try {
                            console.log('Calling makeCardsClickable after switching to card view');
                            makeCardsClickable();
                        } catch (e) {
                            console.error('Error making cards clickable:', e);
                        }
                    }
                } catch (error) {
                    console.error(`Error in button click handler for group ${index + 1}, button ${btnIndex + 1}:`, error);
                }
            });
        });
    });

    // Set initial view based on localStorage or default to list
    let preferredView = 'list'; // Default value
    try {
        const storedView = localStorage.getItem('preferredView');
        if (storedView) {
            preferredView = storedView;
        }
        console.log(`Preferred view from localStorage: ${preferredView}`);
    } catch (e) {
        console.error('Error reading from localStorage:', e);
    }

    // Apply the preferred view to all toggle groups
    viewToggleGroups.forEach((group, index) => {
        try {
            console.log(`Applying preferred view "${preferredView}" to group ${index + 1}`);
            const activeButton = group.querySelector(`.view-toggle-btn[data-view="${preferredView}"]`);

            if (activeButton) {
                console.log(`Found active button for view "${preferredView}" in group ${index + 1}`);
                activeButton.click();

                // If the preferred view is card, make cards clickable
                if (preferredView === 'card') {
                    try {
                        console.log('Calling makeCardsClickable for initial card view');
                        makeCardsClickable();
                    } catch (e) {
                        console.error('Error making cards clickable on initial load:', e);
                    }
                }
            } else {
                console.log(`No active button found for view "${preferredView}" in group ${index + 1}, defaulting to list view`);
                // Default to list view if no preference or buttons not found
                const parentContainer = group.closest('.card-body');
                if (!parentContainer) {
                    console.log(`No parent container found for group ${index + 1}`);
                    return;
                }

                const listContainer = parentContainer.querySelector('.list-container');
                const cardContainer = parentContainer.querySelector('.card-container');

                if (listContainer && cardContainer) {
                    listContainer.classList.remove('d-none');
                    cardContainer.classList.add('d-none');
                } else {
                    console.log(`List or card container not found for group ${index + 1}`);
                }
            }
        } catch (error) {
            console.error(`Error applying preferred view to group ${index + 1}:`, error);
        }
    });

    console.log('View toggle initialization complete');

    // Final check to make sure cards are clickable
    // This is especially important for pages that start with card view
    if (preferredView === 'card') {
        try {
            console.log('Final call to makeCardsClickable after view toggle initialization');
            setTimeout(makeCardsClickable, 100); // Small delay to ensure DOM is updated
        } catch (e) {
            console.error('Error in final call to makeCardsClickable:', e);
        }
    }
}
