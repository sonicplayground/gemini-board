# User API Specification

This document outlines the API endpoints for managing users within the system.

## Base URL

`/api/v1/users`

## Authentication

All endpoints, unless otherwise specified, require authentication. Users must have appropriate roles (e.g., `SERVICE_ADMIN`) to access certain endpoints. Authentication is handled via Spring Security and the `@AuthenticationPrincipal` annotation, using a Bearer token.

## Endpoints

### 1. Get Users (Paginated and Filtered)

-   **Endpoint:** `GET /api/v1/users`
-   **Description:** Retrieves a paginated list of users, optionally filtered by various criteria.
-   **Query Parameters:**
    -   `page`: Page number (default: 0).
    -   `size`: Page size (default: 10).
    -   `nickname`: Filter by nickname (optional).
    -   `gender`: Filter by gender (optional). Possible values: `MALE`, `FEMALE`
    -   `minAge`: Filter by minimum age (optional).
    -   `maxAge`: Filter by maximum age (optional).
    -   `userType`: Filter by user type (optional). Possible values: `SERVICE_ADMIN`, `SERVICE_USER`, `MAINTENANCE_ADMIN`
-   **Request Headers:**
    -   `Authorization`: `Bearer <token>` (Required)
-   **Permissions:** `SERVICE_ADMIN`
-   **Response Body (Success - 200 OK):**
```json
{ "content": [ { "userKey": "{{uuid}}", "name": "string", "nickname": "string", "gender": "MALE or FEMALE", "birth": "yyyy-MM-ddTHH:mm:ss" ,  "address": "string", "userType": "SERVICE_ADMIN or SERVICE_USER or MAINTENANCE_ADMIN", "profilePicture": "string", "loginId": "string" } ], "pageable": { "sort": { "empty": true, "sorted": false, "unsorted": true }, "offset": 0, "pageNumber": 0, "pageSize": 10, "paged": true, "unpaged": false }, "last": true, "totalPages": 1, "totalElements": 1, "size": 10, "number": 0, "sort": { "empty": true, "sorted": false, "unsorted": true }, "first": true, "numberOfElements": 1, "empty": false }
```
    -   `content`: An array of user objects.
    -   `pageable`: Pagination information.
    -   `last`: Indicates if it's the last page.
    -   `totalPages`: Total number of pages.
    -   `totalElements`: Total number of users.
    -   `size`: Page size.
    -   `number`: Current page number.
    -   `first`: Indicates if it's the first page.
    -   `numberOfElements`: Number of elements in the current page.
    -   `empty`: Indicates if the page is empty.

### 2. Get User by Key

-   **Endpoint:** `GET /api/v1/users/{userKey}`
-   **Description:** Retrieves a specific user by their unique key.
-   **Path Parameters:**
    -   `userKey`: The UUID of the user to retrieve.
-   **Request Headers:**
    -   `Authorization`: `Bearer <token>` (Required)
-   **Permissions:** Any authenticated user.
-   **Response Body (Success - 200 OK):**
```json
{ "userKey": "{{uuid}}", "name": "string", "nickname": "string", "gender": "MALE or FEMALE", "birth": "yyyy-MM-ddTHH:mm:ss" ,  "address": "string", "userType": "SERVICE_ADMIN or SERVICE_USER or MAINTENANCE_ADMIN", "profilePicture": "string", "loginId": "string" }
```
    -   `userKey`: The unique identifier (UUID) of the user.
    -   `name`: The name of the user.
    -   `nickname`: The nickname of the user.
    -   `gender`: The gender of the user.
    -   `birth`: The birth date and time of the user.
    -   `address`: The address of the user.
    -   `userType`: The type of the user.
    -   `profilePicture`: The URL or path to the user's profile picture.
    -   `loginId`: The login ID of the user.

-   **Response Body (Error - 404 Not Found):**
```json
{ "timestamp": "2023-10-27T10:00:00Z"  , "status": 404, "error": "Not Found", "message": "User not found", "path": "/api/v1/users/{{ userKey} } " }
```
### 3. Update User

-   **Endpoint:** `PUT /api/v1/users/{userKey}`
-   **Description:** Updates an existing user's information.
-   **Path Parameters:**
    -   `userKey`: The UUID of the user to update.
-   **Request Body:**
```json
{ "nickname": "string", "address": "string", "profilePicture": "string" }
```
    -   `nickname`: The updated nickname of the user.
    -   `address`: The updated address of the user.
    -   `profilePicture`: The updated URL or path to the user's profile picture.
-   **Request Headers:**
    -   `Authorization`: `Bearer <token>` (Required)
-   **Permissions:** Any authenticated user.
-   **Response Body (Success - 200 OK):**
```json
{ "userKey": "{{uuid}}", "name": "string", "nickname": "string", "gender": "MALE or FEMALE", "birth": "yyyy-MM-ddTHH:mm:ss" ,  "address": "string", "userType": "SERVICE_ADMIN or SERVICE_USER or MAINTENANCE_ADMIN", "profilePicture": "string", "loginId": "string" }
```
- **Response Body (Error - 400 Bad Request):**
```json
{ "timestamp": "2023-10-27T10:00:00Z"  , "status": 400, "error": "Bad Request", "message": "Validation failed for object='updateUserRequest'  . Error count: 1", "path": "/api/v1/users/{{ userKey} } " }
```
### 4. Delete User

-   **Endpoint:** `DELETE /api/v1/users/{userKey}`
-   **Description:** Deletes a user.
-   **Path Parameters:**
    -   `userKey`: The UUID of the user to delete.
-   **Request Headers:**
    -   `Authorization`: `Bearer <token>` (Required)
-   **Permissions:** Any authenticated user.
-   **Response Body (Success - 200 OK):**
```json
{ "message": "User deleted successfully" }
```

    -   `message`: A message indicating the success of the operation.

- **Response Body (Error - 404 Not Found):**
```json
{ "timestamp": "2023-10-27T10:00:00Z"  , "status": 404, "error": "Not Found", "message": "User not found", "path": "/api/v1/users/{{ userKey} } " }
```
## Data Types

-   `string`: A sequence of characters.
-   `integer`: A whole number.
-   `uuid`: A universally unique identifier.
-   `yyyy-MM-ddTHH:mm:ss`: Date and time format.
-   `Bearer <token>` : Bearer token for authentication.
-   `SERVICE_ADMIN`, `SERVICE_USER`, `MAINTENANCE_ADMIN`: Enum for user type.
- `MALE`, `FEMALE`: Enum for user gender.















