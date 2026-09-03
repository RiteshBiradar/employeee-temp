/**
 * Employee Leave & Attendance Tracker — Client-side JavaScript
 * Provides UX enhancements: form validation preview, working days calculation.
 * NOTE: Server-side validation is the authoritative source. This is UX only.
 */

document.addEventListener('DOMContentLoaded', function () {

    // ==================== Working Days Preview ====================
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const workingDaysPreview = document.getElementById('workingDaysPreview');

    if (startDateInput && endDateInput && workingDaysPreview) {
        const updateWorkingDays = function () {
            const startVal = startDateInput.value;
            const endVal = endDateInput.value;

            if (startVal && endVal) {
                const start = new Date(startVal);
                const end = new Date(endVal);

                if (start > end) {
                    workingDaysPreview.textContent = 'Start date cannot be after end date';
                    workingDaysPreview.style.color = '#e53e3e';
                    return;
                }

                const days = calculateWorkingDays(start, end);
                workingDaysPreview.textContent = days + ' working day(s)';
                workingDaysPreview.style.color = days > 0 ? '#38a169' : '#e53e3e';
            } else {
                workingDaysPreview.textContent = '';
            }
        };

        startDateInput.addEventListener('change', updateWorkingDays);
        endDateInput.addEventListener('change', updateWorkingDays);

        // Trigger on load if values are pre-filled
        updateWorkingDays();
    }

    // ==================== Leave Form Validation (UX) ====================
    const leaveForm = document.getElementById('leaveForm');
    if (leaveForm) {
        leaveForm.addEventListener('submit', function (e) {
            const leaveType = document.getElementById('leaveType');
            const reason = document.getElementById('reason');

            let errors = [];

            if (leaveType && !leaveType.value) {
                errors.push('Please select a leave type.');
            }
            if (startDateInput && !startDateInput.value) {
                errors.push('Please select a start date.');
            }
            if (endDateInput && !endDateInput.value) {
                errors.push('Please select an end date.');
            }
            if (reason && !reason.value.trim()) {
                errors.push('Please enter a reason.');
            }

            if (startDateInput && endDateInput && startDateInput.value && endDateInput.value) {
                const start = new Date(startDateInput.value);
                const end = new Date(endDateInput.value);
                if (start > end) {
                    errors.push('Start date cannot be after end date.');
                }
                if (calculateWorkingDays(start, end) === 0) {
                    errors.push('The selected date range has no working days.');
                }
            }

            if (errors.length > 0) {
                e.preventDefault();
                showFormErrors(errors);
            }
        });
    }

    // ==================== Confirm Actions ====================
    const confirmForms = document.querySelectorAll('[data-confirm]');
    confirmForms.forEach(function (form) {
        form.addEventListener('submit', function (e) {
            const message = form.getAttribute('data-confirm');
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });

    // ==================== Auto-dismiss alerts ====================
    const successAlerts = document.querySelectorAll('.alert-success');
    successAlerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function () {
                alert.remove();
            }, 500);
        }, 5000);
    });
});

/**
 * Calculate working days between two dates (Mon-Fri, inclusive).
 * This is a client-side preview — server recalculates authoritatively.
 */
function calculateWorkingDays(startDate, endDate) {
    let count = 0;
    let current = new Date(startDate);
    const end = new Date(endDate);

    while (current <= end) {
        const day = current.getDay();
        if (day !== 0 && day !== 6) { // Not Sunday (0) or Saturday (6)
            count++;
        }
        current.setDate(current.getDate() + 1);
    }
    return count;
}

/**
 * Show form validation errors in an alert box.
 */
function showFormErrors(errors) {
    // Remove existing error alert
    const existing = document.getElementById('jsValidationAlert');
    if (existing) existing.remove();

    const alert = document.createElement('div');
    alert.id = 'jsValidationAlert';
    alert.className = 'alert alert-danger';
    alert.innerHTML = errors.join('<br>');

    const form = document.getElementById('leaveForm');
    if (form) {
        form.parentNode.insertBefore(alert, form);
        alert.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
}
