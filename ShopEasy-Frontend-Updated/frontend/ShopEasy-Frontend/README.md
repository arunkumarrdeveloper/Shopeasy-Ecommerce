# ShopEasy Frontend

React + Vite storefront and role-based admin workspace for the ShopEasy Spring Boot API.

## Requirements
- Node.js 18+
- ShopEasy backend running at `http://localhost:8080`

## Run
```bash
npm install
npm run dev
```
Open `http://localhost:5173`.

Optional API URL: copy `.env.example` to `.env` and set `VITE_API_BASE_URL`.

## User features
- User registration and login
- Browse, search and filter products by category
- Add and remove cart items (cart is stored in the backend)
- Cash-on-delivery checkout
- View personal order history and order status

## Admin features
- Separate administrator login
- Dashboard summary and recent orders
- Product create, read, update and delete
- Category create, read, update and delete
- View all orders and update order status
- View registered customers

## Access control
The frontend hides role-specific routes, but the backend is the security boundary. Spring Security checks the JWT role for every protected API. Public registration always creates a `USER`; it cannot create an administrator. The admin account is created by the backend bootstrap configuration.
