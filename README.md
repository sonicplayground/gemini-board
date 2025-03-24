# Gemini Board Project

This project is a Spring Boot-based application designed for managing users and vehicles. It includes features for user registration, login, vehicle management, and more.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
    - [User Management](#user-management)
    - [Login](#login)
    - [Vehicle Management](#vehicle-management)
- [Security](#security)
- [Contributing](#contributing)
- [License](#license)

## Features

- **User Management:**
    - User registration (sign-up).
    - User login (sign-in).
    - Retrieve user information.
    - Update user information.
    - Delete user.
    - Search and list users with filtering options (nickname, gender, age, user type).
- **Vehicle Management:**
    - Create a new vehicle.
    - Retrieve vehicle information.
    - Search and list vehicles with filtering options (VIN, owner, manufacturer, model, mileage).
    - Update vehicle information.
    - Update vehicle mileage.
    - Record vehicle maintenance (e.g., tire replacement).
    - Delete a vehicle.
- **Authentication and Authorization:**
    - JWT (JSON Web Token) based authentication.
    - Role-based authorization (e.g., `SERVICE_ADMIN`, `SERVICE_USER`).
- **Pagination:**
    - Support for paginated responses for user and vehicle lists.
- **Validation:**
    - Input validation for requests.

## Technologies Used

- **Java 17:** The programming language.
- **Spring Boot 3.4.3:** The web framework.
- **Spring Data JPA:** For database interaction.
- **Spring Security:** For authentication and authorization.
- **MariaDB:** The database.
- **Lombok:** For reducing boilerplate code.
- **JWT (JSON Web Token):** For secure authentication.
- **Gradle:** The build tool.
- **Validation:** for request validation

## Getting Started

### Prerequisites

- **Java Development Kit (JDK) 17:** Make sure you have JDK 17 installed.
- **MariaDB:** You need a running MariaDB instance.
- **Gradle:** The project uses Gradle for building.
- **IDE (Optional):** IntelliJ IDEA or Eclipse are recommended.

### Installation

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    ```
    (Replace `<repository-url>` with the actual URL of your repository.)
2.  **Navigate to the project directory:**
    ```bash
    cd gemini-board
    ```
3.  **Database Setup:**
    - Create a database in MariaDB.
    - Configure the database connection in `src/main/resources/application.properties` or `application.yml`.
     ```properties
     spring.datasource.url=jdbc:mariadb://localhost:3306/<your_database_name>
     spring.datasource.username=<your_database_username>
     spring.datasource.password=<your_database_password>
     spring.jpa.hibernate.ddl-auto=none
     ```
    (Replace placeholders with your actual database details.)
### Running the Application

1.  **Build the project:**
    ```bash
    ./gradlew build
    ```
2.  **Run the application:**
    ```bash
    ./gradlew bootRun
    ```
3.  **Access the application:**
    - The application will be running at `http://localhost:8080`.

## API Endpoints

### User Management

-   **`POST /api/v1/login/sign-up`:** Create a new user (registration).
    -   Request Body: `UserDto.RegisterRequest`
    -   Response Body: `UserDto.RegisterResponse`
-   **`POST /api/v1/login/sign-in`:** User login.
    -   Request Body: `LoginDto.SingInRequest`
    -   Response Body: `LoginDto.SignInResponse`
-   **`GET /api/v1/users`:** Get a list of users (admin only).
    -   Query Parameters: `page`, `size`, `nickname`, `gender`, `minAge`, `maxAge`, `userType`
    -   Response Body: `PagedContent<UserDto.UserResponse>`
-   **`GET /api/v1/users/{userKey}`:** Get user details.
    -   Response Body: `UserDto.UserResponse`
-   **`PUT /api/v1/users/{userKey}`:** Update user details.
    -   Request Body: `UserDto.UserUpdateRequest`
    -   Response Body: `UserDto.UserResponse`
-   **`DELETE /api/v1/users/{userKey}`:** Delete a user.
    -   Response Body: `UserDto.DeleteResponse`

### Login

-   **`POST /api/v1/login/sign-in`**: sign in

### Vehicle Management

-   **`POST /api/v1/vehicles`:** Create a new vehicle.
    -   Request Body: `VehicleDto.CreateVehicleRequest`
    -   Response Body: `VehicleDto.CreateVehicleResponse`
-   **`GET /api/v1/vehicles/{vehicleKey}`:** Get vehicle details.
    -   Response Body: `VehicleDto.VehicleResponse`
-   **`GET /api/v1/vehicles`:** Get a list of vehicles.
    -   Query Parameters: `page`, `size`, `vin`, `ownerUserKey`, `manufacturer`, `modelName`, `vehicleKey`, `minMileage`, `maxMileage`
    -   Response Body: `PagedContent<VehicleDto.VehicleResponse>`
-   **`PUT /api/v1/vehicles/{vehicleKey}`:** Update vehicle details.
    -   Request Body: `VehicleDto.UpdateVehicleRequest`
    -   Response Body: `VehicleDto.VehicleResponse`
-   **`PATCH /api/v1/vehicles/{vehicleKey}/maintenance`:** Record vehicle maintenance.
    -   Request Body: `VehicleDto.MaintenanceRequest`
    -   Response Body: `VehicleDto.ResultMessageResponse`
-   **`PATCH /api/v1/vehicles/{vehicleKey}/mileage`:** Update vehicle mileage.
    -   Request Body: `VehicleDto.UpdateMileageRequest`
    -   Response Body: `VehicleDto.ResultMessageResponse`
-   **`DELETE /api/v1/vehicles/{vehicleKey}`:** Delete a vehicle.
    -   Response Body: `VehicleDto.ResultMessageResponse`

## Security

-   **JWT Authentication:** The application uses JWT for authentication.
-   **Role-Based Authorization:** Access to certain endpoints is restricted based on user roles (e.g., `SERVICE_ADMIN`, `SERVICE_USER`).
-   **PreAuthorize:** `@PreAuthorize` annotations are used to secure endpoints.


