# ShopEasy Backend

Spring Boot 3 REST API for the ShopEasy e-commerce application.

## Requirements
- Java 17+
- Maven
- MySQL 8+

## Configure MySQL
Create a database (the JDBC URL can also create it automatically):
```sql
CREATE DATABASE shopeasy_db;
```
Set your MySQL username/password in `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```
Change `app.jwt.secret` to a private random secret before deployment. Never commit real production credentials.

## Run
From the `shopeasy-backend` folder:
```bash
mvn spring-boot:run
```
API base URL: `http://localhost:8080/api`.

## Default local administrator
On first startup, the backend creates this administrator if it does not already exist:
- Email: `admin@shopeasy.com`
- Password: `Admin@12345`

Change this password for any non-local use. Override the bootstrap credentials with `app.admin.name`, `app.admin.email`, and `app.admin.password` application properties (or corresponding Spring environment variables). Public `/api/auth/register` always assigns `USER`; clients cannot submit an ADMIN role.

## Security and roles
- Public: registration, login, product reads and category reads.
- `USER`: cart operations and personal order history.
- `ADMIN`: product/category mutations, dashboard, customer listing, all-order listing and order-status updates.

JWT bearer tokens are required for protected endpoints. Role checks are enforced by Spring Security on the backend; frontend route checks are only a user-experience layer.

## API overview

### Authentication
- `POST /api/auth/register` — `{ "name":"Customer", "email":"customer@example.com", "password":"secret123" }`
- `POST /api/auth/login` — `{ "email":"admin@shopeasy.com", "password":"Admin@12345" }`
- Response: `token`, `id`, `name`, `email`, `role`.

### Products
- `GET /api/products`, optional `q` and `categoryId` filters
- `GET /api/products/{id}`
- `POST /api/products` (ADMIN)
- `PUT /api/products/{id}` (ADMIN)
- `DELETE /api/products/{id}` (ADMIN)

Product body: `{ "name":"Headphones", "description":"Wireless", "price":1999.00, "stock":20, "imageUrl":"https://example.com/item.jpg", "categoryId":1 }`.

### Categories
- `GET /api/categories`
- `POST /api/categories` (ADMIN)
- `PUT /api/categories/{id}` (ADMIN)
- `DELETE /api/categories/{id}` (ADMIN)

### Cart and checkout (USER)
- `GET /api/cart`
- `POST /api/cart/items` — `{ "productId":1, "quantity":1 }`
- `DELETE /api/cart/items/{cartItemId}`
- `POST /api/cart/checkout` — `{ "shippingAddress":"Door no, street, city, state, PIN" }`

Checkout uses Cash on Delivery, checks stock, creates an order and reduces inventory.

### Orders
- `GET /api/orders/my` (USER)
- `GET /api/orders` (ADMIN)
- `PUT /api/orders/{id}/status` (ADMIN) — `{ "status":"PROCESSING" }`
- Status values: `PLACED`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`.

### Admin
- `GET /api/admin/dashboard` — customer/product/order totals (ADMIN)
- `GET /api/admin/users` — registered customer list (ADMIN)

## Notes
This is a local-development learning project. Before production, add payment-gateway integration, image upload/storage, pagination, rate limiting, audit logging, stronger stock concurrency handling and automated integration tests. Configure HTTPS and secure secrets in deployment.
