# The Golden Leaf — Restaurant Reservation Website

## Project Theme

**The Golden Leaf** is a premium multi-page restaurant website built for Client-Side Development Coursework Two. The site showcases a fine-dining restaurant business and gives visitors everything they need to explore the menu, view the gallery, read reviews, and book a table — all through a fully client-side, interactive experience.

The core functional requirement — a validated web form — is delivered through the **table reservation form**, which collects guest details (name, phone, email, date, time, party size, occasion, and special requests), validates every field with custom JavaScript, and stores confirmed bookings in the browser using `localStorage`.

## Live Repository

🔗 **GitHub Repository:** [ADD YOUR GITHUB REPO URL HERE]

## Technologies Used

- Semantic HTML5
- CSS3 (Flexbox, Grid, transitions & keyframe animations)
- Vanilla JavaScript (DOM manipulation, dynamic event handling, `localStorage`)
- jQuery (admin dashboard: live filtering, search, and DOM updates)
- AOS (Animate On Scroll) library for scroll-triggered animations
- Font Awesome for iconography

## Site Structure

```
Book-IT-main/
├── index.html                  # Homepage: hero, about, live availability stats
├── image/                      # Shared favicon and background images
├── Bibash Mahat/                # Reservation system
│   ├── bibash.html              # Table booking form + confirmation modal
│   ├── bibash.css
│   ├── booking.js               # Handles submission, receipt generation, localStorage save
│   └── storage.js               # localStorage read/write helpers for bookings
├── Prem Kumar Jha/              # Menu, admin dashboard & AI receptionist
│   ├── index.html                # Menu page (dynamic filtering by category)
│   ├── admin.html                # Staff dashboard (bookings table, stats, CSV export)
│   ├── admin.css / style.css
│   ├── app.js                    # Navbar, mobile menu, AI chat widget
│   ├── dashboard.js              # jQuery-driven admin table (search/filter/status/export)
│   └── menu.js                   # Dynamic menu data + category filter rendering
├── Rohit Bhusal/                # Gallery, reviews & form validation
│   ├── rohit.html                # Gallery + guest testimonials
│   ├── rohit.css
│   └── validation.js             # Custom regex validation for the booking form,
│                                  # with live + on-submit checks and accessible error messages
└── Satirtha Dhar/               # Staff login & shared styling
    ├── login.html                # Staff portal login page
    ├── style.css / responsive.css # Shared/base styles and responsive breakpoints
    └── utils.js                  # Shared formatting helpers (date, time, booking ID)
```

## Individual Roles & Contributions

| Team Member | Role | Key Contributions |
|---|---|---|
| **Bibash Mahat** | Reservation System Developer | Built the table booking form and its UI, wired up the submission flow, receipt/confirmation modal, and `localStorage` persistence of bookings (`booking.js`, `storage.js`, `bibash.html/css`). |
| **Prem Kumar Jha** | Menu & Admin Dashboard Developer | Built the homepage layout, dynamic menu rendering with category filters, the staff admin dashboard with jQuery-powered search/filter/status updates and CSV export, and the AI receptionist chat widget (`menu.js`, `dashboard.js`, `app.js`, `admin.html/css`). |
| **Rohit Bhusal** | Gallery, Reviews & Validation Developer | Built the gallery and testimonials page with scroll animations, and implemented the custom JavaScript form validation layer (`validation.js`) — regex-based checks, live field feedback, and accessible on-page error messages for the reservation form. |
| **Satirtha Dhar** | Shared Styling & Utilities Developer | Built the staff login page, the shared/base stylesheet and responsive breakpoints used across the site, and shared JS utility functions for date/time formatting and booking ID generation (`login.html`, `style.css`, `responsive.css`, `utils.js`). |

## How to Run

1. Clone or download this repository.
2. Open `index.html` in any modern web browser (Chrome, Firefox, Edge, Safari) — no build step or server required.
3. To use the table reservation form, navigate to the **Reserve Table** link from any page.
4. To view the admin dashboard, go to the **Staff Portal** (login page) and submit the form — this is a front-end mock login and will redirect straight to the dashboard.

## Notes

- All bookings are stored client-side in the browser's `localStorage`; data will persist between visits on the same browser but will not sync across devices.
- The "Talk to our AI Receptionist" feature expects a backend chat endpoint. Without a backend running, it gracefully falls back to an offline message rather than breaking the page.
