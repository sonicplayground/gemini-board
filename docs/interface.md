// generate documentation of interface between server and client
## User API

### Create User

-   **Endpoint:** `POST /api/v1/users`
-   **Request Body:**

    ```json
    {
        "name": "string",
        "nickname": "string",
        "gender": "MALE or FEMALE",
        "age": "integer",
        "address": "string",
        "userType": "SERVICE_ADMIN or SERVICE_USER or MAINTENANCE_ADMIN",
        "profilePicture": "string",
        "loginId": "string",
        "password": "string"
    }
    ```

-   **Response Body:**

    ```json
    {
        "userKey": "{{generated uuid}}"
    }
    ```

-   **Description:**
    -   Creates a new user.
    -   `name`, `gender`, `age`, `userType`, `loginId`, `password` are required.
    -   `gender` can be `MALE` or `FEMALE`.
    -   `userType` can be `SERVICE_ADMIN`, `SERVICE_USER`, or `MAINTENANCE_ADMIN`.
    -   Returns the `userKey` of the newly created user.

### Get Users

-   **Endpoint:** `GET /api/v1/users`
-   **Query Parameters:**
    -   `page`: Page number (default: 0).
    -   `size`: Page size (default: 10).
    -   `nickname`: Filter by nickname (optional).
    -   `gender`: Filter by gender (optional, `MALE` or `FEMALE`).
    -   `minAge`: Filter by minimum age (optional).
    -   `maxAge`: Filter by maximum age (optional).
    -   `userType`: Filter by user type (optional, `SERVICE_ADMIN`, `SERVICE_USER`, or `MAINTENANCE_ADMIN`).
-   **Response Body:**
    ```json
    {
        "content": [
            {
                "userKey": "{{uuid}}",
                "name": "string",
                "nickname": "string",
                "gender": "MALE or FEMALE",
                "age": "integer",
                "address": "string",
                "userType": "SERVICE_ADMIN or SERVICE_USER or MAINTENANCE_ADMIN",
                "profilePicture": "string",
                "loginId": "string"
            }
        ],
        "pageable": {
            "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
            },
            "offset": 0,
            "pageNumber": 0,
            "pageSize": 10,
            "paged": true,
            "unpaged": false
        },
        "last": true,
        "totalPages": 1,
        "totalElements": 1,
        "size": 10,
        "number": 0,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "first": true,
        "numberOfElements": 1,
        "empty": false
    }
    ```

-   **Description:**
    -   Retrieves a paginated list of users based on the provided filters.
    -   `page` and `size` parameters control the pagination.
    -   `nickname`, `gender`, `minAge`, `maxAge`, and `userType` are optional filters.
    -   `gender` can be `MALE` or `FEMALE`.
    -   `userType` can be `SERVICE_ADMIN`, `SERVICE_USER`, or `MAINTENANCE_ADMIN`.
    -   Returns a paginated list of user objects with details.
