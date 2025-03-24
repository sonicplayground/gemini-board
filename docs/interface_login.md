# Login API Specification

This document outlines the API endpoints for user login and registration within the system.

## Base URL

`/api/v1/login`

## Authentication

-   The `/sign-up` endpoint does not require authentication.
-   The `/sign-in` endpoint does not require authentication.
- After sign-in, you will get a token.

## Endpoints

### 1. Sign Up (Create User)

-   **Endpoint:** `POST /api/v1/login/sign-up`
-   **Description:** Creates a new user account.
-   **Request Body:**
```json
{ "name": "John Doe", "nickname": "johndoe123", "gender": "MALE", "birth": "2023-11-15T10:30:00" ,  "address": "123 Main St, Anytown", "userType": "USER", "profilePicture": "https://example.com/ profile. jpg" ,  "loginId": "johndoe", "password": "securePassword123" }
```
    -   `name` (String, required): User's full name.
    -   `nickname` (String, optional): User's chosen nickname.
    -   `gender` (String, required): User's gender. Possible values: `MALE`, `FEMALE`.
    -   `birth` (String, required): User's date and time of birth in `yyyy-MM-ddTHH:mm:ss` format.
    -   `address` (String, optional): User's address.
    -   `userType` (String, required): Type of user. Possible values: `USER`, `ADMIN`.
    -   `profilePicture` (String, optional): URL to the user's profile picture.
    -   `loginId` (String, required): User's chosen login ID (must be unique).
    -   `password` (String, required): User's chosen password.
-   **Request Headers:**
    -   None
-   **Permissions:** None (Public endpoint)
-   **Response Body (Success - 200 OK):**
```json
{ "userKey": "f47ac10b-58cc-4372- a567- 0e02b2c3d479"  }
```
    -   `userKey` (UUID): The unique identifier for the newly created user.
-   **Response Body (Error - 400 Bad Request):**
```json
{ "timestamp": "2023-11-15T10:30:00" ,  "status": 400, "error": "Bad Request", "message": "require param : name", "path": "/api/v1/login/sign- up"  }
```
    -   Returned when the request body is invalid, missing required fields, or the `loginId` is already taken.
- **Response Body (Error - 500 Internal Server Error):**
```json
 { "timestamp": "2023-11-15T10:30:00" ,  "status": 500, "error": "Internal Server Error", "message": "Internal Server Error", "path": "/api/v1/login/sign- up"  }
```
    -   Returned when the server has an unexpected error.

### 2. Sign In (Authenticate User)

-   **Endpoint:** `POST /api/v1/login/sign-in`
-   **Description:** Authenticates a user and returns a JWT token.
-   **Request Body:**
```json
{ "loginId": "johndoe", "password": "securePassword123" }
```
    -   `loginId` (String, required): User's login ID.
    -   `password` (String, required): User's password.
-   **Request Headers:**
    -   None
-   **Permissions:** None (Public endpoint)
-   **Response Body (Success - 200 OK):**
```json
{ "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9. . . "  }
```
    -   `token` (String): The JWT token for the authenticated user.
-   **Response Body (Error - 400 Bad Request):**
```json
{ "timestamp": "2023-11-15T10:30:00" ,  "status": 400, "error": "Bad Request", "message": "Bad Request", "path": "/api/v1/login/sign- in"  }
```
    -   Returned when the request body is invalid or missing required fields.
-   **Response Body (Error - 401 Unauthorized):**
```json
{ "timestamp": "2023-11-15T10:30:00" ,  "status": 401, "error": "Unauthorized", "message": "Unauthorized", "path": "/api/v1/login/sign- in"  }
```
    -   Returned when the user provides incorrect login credentials.
- **Response Body (Error - 500 Internal Server Error):**
```json
{ "timestamp": "2023-11-15T10:30:00" ,  "status": 500, "error": "Internal Server Error", "message": "Internal Server Error", "path": "/api/v1/login/sign- in"  }
```
    -   Returned when the server has an unexpected error.

## Data Types

-   `string`: A sequence of characters.
-   `uuid`: A universally unique identifier.
-   `yyyy-MM-ddTHH:mm:ss`: Date and time format.
-   `USER`, `ADMIN`: Enum for user type.
-   `MALE`, `FEMALE`: Enum for user gender.
- `token` : JWT token for authentication.








