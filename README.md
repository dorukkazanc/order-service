# Order Service

A Spring Boot microservice for managing trading orders and assets with Spring Security authentication and role-based access control.

## Features

- Order management (create, update, delete, search, match)
- Asset management with search functionality
- Matching service integration
- Dynamic filtering and pagination
- Role-based authorization (ADMIN, CUSTOMER)
- H2 in-memory database
- RESTful API endpoints

## Prerequisites

- Java 17+
- Maven/Gradle
- Docker

## Quick Start

### 1. Run Locally

```bash
# Clone the repository
git clone <your-repo-url>
cd order-service

# Run the application
./gradlew bootRun
```

The service will be available at `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/create-admin` - Create admin user (Admin only)

### Orders (Customer Role)
- `POST /api/orders` - Create new order
- `POST /api/orders/search` - Search orders with dynamic filters
- `PUT /api/orders/{id}` - Update order
- `DELETE /api/orders/{id}` - Cancel order (pending orders only)

### Assets (Customer Role)
- `POST /api/assets/search` - Search customer's assets

### Admin Operations (Admin Role)
- `GET /api/admin/customers` - Get all customers
- `GET /api/admin/customers/{id}` - Get customer by ID
- `PUT /api/admin/customers/{id}` - Update customer
- `DELETE /api/admin/customers/{id}` - Delete customer
- `POST /api/admin/customers/search` - Search customers
- `GET /api/admin/assets` - Get all assets
- `GET /api/admin/assets/{id}` - Get asset by ID
- `PUT /api/admin/assets/{id}` - Update asset
- `DELETE /api/admin/assets/{id}` - Delete asset
- `POST /api/admin/assets/search` - Search assets
- `GET /api/admin/orders` - Get all orders
- `GET /api/admin/orders/{id}` - Get order by ID
- `PUT /api/admin/orders/{id}` - Update order
- `DELETE /api/admin/orders/{id}` - Delete order
- `POST /api/admin/orders/search` - Search orders
- `POST /api/admin/orders/match/{id}` - Match/approve order

## Authentication

Spring Security with session-based authentication

- **Default Admin**: `admin` / `123`

### Sample Login Request
```json
POST /api/auth/login
{
  "username": "admin", // alice, user (Customer roles)
  "password": "123"
}
```

## Dynamic Search

All search endpoints support dynamic filtering with operators:

### Supported Operators
- `equals`, `not_equals`
- `greater_than`, `greater_than_equals`
- `less_than`, `less_than_equals`
- `contains`, `not_contains`
- `starts_with`, `ends_with`
- `in`, `not_in`
- `is_null`, `is_not_null`
- `between`

### Sample Search Request
```json
POST /api/assets/search
{
  "filters": [
    {
      "field": "assetName",
      "operator": "contains",
      "value": "TRY"
    }
  ],
  "sortBy": "createdDate",
  "sortDirection": "desc",
  "page": 0,
  "size": 20
}
```

## Database

- **Type**: H2 In-Memory Database
- **Console**: `http://localhost:8080/h2-console`
- **URL**: `jdbc:h2:mem:order-service-db`
- **Username**: `sa`
- **Password**: (empty)

## Security Configuration

- Session-based authentication
- Role-based access control (@PreAuthorize)
- H2 console excluded from security for monitoring
- CSRF disabled for API usage
- Disabled password encoding for simplicity


## Key Features

### Dynamic Query Builder
- Builds JPA Specifications from filter criteria
- Supports type conversion for different field types
- Handles complex filtering operations

### Role-Based Security
- **CUSTOMER**: Can manage their own orders and view their assets
- **ADMIN**: Full access to all entities and operations

### Order Matching
- Admin can match/approve orders
- Integration with MatchService for order processing

## Testing with Postman

### Postman Collection

A complete Postman collection is available with most crucial API endpoints pre-configured:

**[Download Postman Collection](https://doruk-5717373.postman.co/workspace/doruk's-Workspace~b13a59ef-0ba7-4fca-8f27-8704f91eeec4/collection/43684775-6354d6fa-7cd8-4c8b-8e2e-f236dfeaf24d?action=share&source=copy-link&creator=43684775)**

### Import Instructions

1. Open Postman
2. Click "Import" button in the top left
3. Paste the collection link or upload the JSON file
4. The collection will be imported with all endpoints organized by role

### Authentication Flow

1. **Login First**: Use the `POST /api/auth/login` endpoint in the Auth folder
2. **Role-Based Testing**:
  - Use Admin endpoints after admin login
  - login with customer account for customer endpoints
