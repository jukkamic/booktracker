# Security and Validation Implementation

## Overview
This document describes the Spring Security and Bean Validation features implemented in the Book Tracker application.

## Security Configuration

### Roles
Two roles have been defined:
- **USER**: Can view books but cannot add, edit, or delete them
- **ADMIN**: Full access to add, edit, and delete books

### Demo Accounts
The application uses in-memory authentication with the following demo accounts:

| Username | Password | Role |
|----------|----------|------|
| admin    | admin123 | ADMIN |
| user     | user123  | USER |

**Note:** Passwords use `{noop}` encoding for demonstration purposes. In production, use proper password encoding.

### Security Rules
- **Public Access**: Home page (`/`), API endpoints (`/api/books/**`), and static resources
- **ADMIN Only**: Add book form, create/update/delete operations (`/books/new`, `/books`, `/books/update/**`, `/books/delete/**`)
- **Authenticated**: All other pages require login

### Login Page
A custom login page is available at `/login` with:
- Username and password fields
- Error messages for invalid credentials
- Logout confirmation messages
- Demo account credentials displayed for convenience

## Bean Validation (JSR-303)

### Validation Rules
The Book model includes the following validations:

| Field    | Annotation           | Rule                                     |
|----------|----------------------|------------------------------------------|
| title    | @NotBlank            | Cannot be empty                          |
| title    | @Size(min=1, max=200) | Must be 1-200 characters                 |
| author   | @NotBlank            | Cannot be empty                          |
| author   | @Size(min=1, max=100) | Must be 1-100 characters                 |
| status   | @NotNull             | Must be selected                         |
| rating   | @Min(1)              | Must be at least 1 (if provided)        |
| rating   | @Max(5)              | Must be at most 5 (if provided)          |

### Validation Error Display
All forms (Add Book and Edit Book) display validation errors with:
- Red highlighting on invalid fields using Bootstrap's `is-invalid` class
- Error messages below each invalid field
- Icons to indicate error status
- Form is redisplayed with preserved input if validation fails

### Controller Changes
Both `addBook()` and `updateBook()` methods in `WebController` now:
1. Accept `@Valid` annotated `Book` object
2. Check `BindingResult` for validation errors
3. Return to the form with error messages if validation fails
4. Proceed with save/update only if validation passes

## Thymeleaf Spring Security Extras

The `index.html` template uses the Thymeleaf Spring Security Extras library to conditionally display content:

### Conditional Buttons
- **Login Button**: Shown only to anonymous users (`sec:authorize="isAnonymous()"`)
- **Logout Button**: Shown only to authenticated users (`sec:authorize="isAuthenticated()"`)
- **Add Book Button**: Shown only to users with ADMIN role (`sec:authorize="hasRole('ADMIN')"`)

### User Information
- Username is displayed in the navbar for authenticated users
- Uses `sec:authentication="name"` to show the current username

## How to Test

### 1. Test Security Features
1. Start the application
2. Navigate to `http://localhost:8080`
3. As a guest, you should see a "Login" button (not "Add Book")
4. Click "Login" and try the demo accounts:
   - Login as `admin/admin123` - you'll see "Add Book" button and can edit/delete books
   - Login as `user/user123` - you'll see the books but no "Add Book" button
5. Logout using the button in the navbar

### 2. Test Validation Features
1. Login as admin
2. Click "Add New Book"
3. Try submitting the form with empty fields - you should see validation errors
4. Try submitting with invalid data:
   - Empty title: "Title is required"
   - Title longer than 200 characters: "Title must be between 1 and 200 characters"
   - No status selected: "Status is required"
   - Rating of 0 or 6: "Rating must be at least 1" or "Rating must be at most 5"
5. Verify that form data is preserved when validation fails
6. Test the same validation on the Edit Book form

## Implementation Details

### Files Modified/Created

1. **pom.xml** - Added dependencies:
   - `spring-boot-starter-security`
   - `thymeleaf-extras-springsecurity6`
   - `spring-boot-starter-validation`

2. **SecurityConfig.java** (new) - Security configuration with:
   - Role-based access control
   - Custom login page
   - In-memory user details service

3. **Book.java** - Added validation annotations:
   - `@NotBlank`, `@Size`, `@NotNull`, `@Min`, `@Max`

4. **WebController.java** - Updated methods:
   - Added `@Valid` and `BindingResult` to `addBook()` and `updateBook()`
   - Validation error handling logic

5. **login.html** (new) - Custom login page with demo account info

6. **index.html** - Added security integration:
   - Thymeleaf Security Extras namespace
   - Conditional Login/Logout buttons
   - Conditional "Add Book" button for admins
   - Display of current username

7. **add-book.html** - Added validation error display:
   - Global error messages
   - Field-specific error messages
   - Visual highlighting of invalid fields

8. **edit-book.html** - Added validation error display (same as add-book)

## Best Practices Implemented

1. **Security**:
   - Role-based access control (RBAC)
   - Secure by default (all endpoints protected unless explicitly allowed)
   - CSRF protection (disabled only for API endpoints)
   - Proper logout handling

2. **Validation**:
   - Declarative validation using annotations
   - User-friendly error messages
   - Clear visual feedback for errors
   - Form data preservation on validation failure

3. **User Experience**:
   - Clear indication of user authentication state
   - Intuitive error messages
   - Demo account credentials visible for easy testing
   - Responsive design with Bootstrap