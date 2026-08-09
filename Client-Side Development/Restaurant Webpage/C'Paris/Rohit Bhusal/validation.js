/**
 * validation.js
 * Author: Rohit Bhusal
 *
 * Custom client-side validation for the reservation form on the
 * booking page (#premiumBookingForm). Runs BEFORE booking.js so an
 * invalid submission never reaches the storage/receipt logic.
 *
 * Rules enforced:
 *  - Full Name    : letters, spaces, hyphens, apostrophes, 2-50 chars
 *  - Phone Number : optional "+", 7-15 digits (spaces/dashes/brackets allowed)
 *  - Email        : standard user@domain.tld pattern
 *  - Date         : required, cannot be in the past
 *  - Time Slot    : required (must not be the disabled placeholder)
 *  - Guests       : whole number between 1 and 10
 *  - Terms        : checkbox must be ticked
 *
 * Each field gets a live (on blur / on input) check plus a full
 * re-check on submit. Errors are shown as visible text under the
 * field (not just a red border) and announced via aria-live so
 * screen reader users get the same feedback as sighted users.
 */

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('premiumBookingForm');
    if (!form) return; // validation.js only runs on the booking page

    const fields = {
        fullName: {
            el: document.getElementById('fullName'),
            test: (v) => /^[A-Za-z][A-Za-z\s'-]{1,49}$/.test(v.trim()),
            message: 'Please enter a full name using letters only (2-50 characters).'
        },
        phone: {
            el: document.getElementById('phone'),
            test: (v) => {
                const digits = v.replace(/\D/g, '');
                return /^\+?[\d\s()-]{7,20}$/.test(v.trim()) && digits.length >= 7 && digits.length <= 15;
            },
            message: 'Please enter a valid phone number (7-15 digits, "+" allowed).'
        },
        email: {
            el: document.getElementById('email'),
            test: (v) => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(v.trim()),
            message: 'Please enter a valid email address, e.g. name@example.com.'
        },
        bookingDate: {
            el: document.getElementById('bookingDate'),
            test: (v) => {
                if (!v) return false;
                const chosen = new Date(v + 'T00:00:00');
                const today = new Date();
                today.setHours(0, 0, 0, 0);
                return chosen >= today;
            },
            message: 'Please choose today or a future date.'
        },
        bookingTime: {
            el: document.getElementById('bookingTime'),
            test: (v) => v !== '' ,
            message: 'Please select a time slot.'
        },
        guests: {
            el: document.getElementById('guests'),
            test: (v) => /^[0-9]+$/.test(v.trim()) && Number(v) >= 1 && Number(v) <= 10,
            message: 'Please enter a number of guests between 1 and 10.'
        },
        agreeTerms: {
            el: document.getElementById('agreeTerms'),
            test: (el) => el.checked,
            message: 'You must agree to the Terms & Cancellation Policy to continue.',
            isCheckbox: true
        }
    };

    // --- Build (once) a visible error <small> under every field ---
    Object.keys(fields).forEach((key) => {
        const field = fields[key];
        if (!field.el) return;

        const errorEl = document.createElement('small');
        errorEl.className = 'field-error';
        errorEl.id = `${key}-error`;
        errorEl.setAttribute('role', 'alert');
        errorEl.setAttribute('aria-live', 'polite');

        // Terms checkbox sits inside a <label>; everything else sits in a .form-group
        const insertAfterEl = field.isCheckbox ? field.el.closest('.terms-group') : field.el;
        insertAfterEl.insertAdjacentElement('afterend', errorEl);

        field.errorEl = errorEl;
        field.el.setAttribute('aria-describedby', errorEl.id);
    });

    function validateField(key) {
        const field = fields[key];
        if (!field.el) return true;

        const value = field.isCheckbox ? field.el : field.el.value;
        const valid = field.test(value);

        field.el.classList.toggle('input-invalid', !valid);
        field.el.classList.toggle('input-valid', valid);
        field.el.setAttribute('aria-invalid', String(!valid));
        field.errorEl.textContent = valid ? '' : field.message;

        return valid;
    }

    // Live validation as the user interacts with each field
    Object.keys(fields).forEach((key) => {
        const field = fields[key];
        if (!field.el) return;

        const eventName = field.isCheckbox || field.el.tagName === 'SELECT' ? 'change' : 'input';
        field.el.addEventListener(eventName, () => validateField(key));
        field.el.addEventListener('blur', () => validateField(key));
    });

    // Full validation on submit - runs before booking.js's own submit handler
    form.addEventListener('submit', (e) => {
        let isFormValid = true;
        let firstInvalidField = null;

        Object.keys(fields).forEach((key) => {
            const field = fields[key];
            if (!field.el) return;
            const valid = validateField(key);
            if (!valid) {
                isFormValid = false;
                if (!firstInvalidField) firstInvalidField = field.el;
            }
        });

        if (!isFormValid) {
            e.preventDefault();
            e.stopImmediatePropagation(); // stop booking.js's submit handler from also running
            if (firstInvalidField) firstInvalidField.focus();
        }
        // If valid, let the event continue on to booking.js which saves + shows the receipt
    });
});
