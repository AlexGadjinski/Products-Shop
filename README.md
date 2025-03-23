# Products Shop

## Description
**Products Shop** is a platform where users can buy and sell products in various categories. The application allows users to register, buy products, sell products, and manage their profiles. The system includes features for querying and exporting data, making it easy to view and interact with products, users, and categories.

## Features
- **User Management:**
  - Users can register with personal information.
  - Users can sell and buy products.
  - Users can form friendships with other users.

- **Product Management:**
  - Products are assigned to both sellers and buyers.
  - Products belong to multiple categories.
  - Products include detailed information like name, price, buyer, seller, etc.

- **Category Management:**
  - Products are grouped into various categories.
  - Categories are associated with a variety of products.

## Database Design
- **Users:** Stores information about users, including their personal details and products they have bought or sold.
- **Products:** Products have details like name, price, and associations with buyers and sellers.
- **Categories:** Each category contains a collection of products.

## Seeded Data
Initial data is seeded from the following files:
- `users.json`: Contains user data.
- `products.json`: Contains product data.
- `categories.json`: Contains category data.

The products are randomly assigned buyers and sellers, and some products are left unsold (i.e., no buyer).

## Queries and Data Export

### Query 1: Products in Range
- Get products in a specified price range that have no buyer.
- Data is exported to `products-in-range.json`.

Example:
```json
[
  {
    "name": "TRAMADOL HYDROCHLORIDE",
    "price": 516.48,
    "seller": "Christine Gomez"
  },
  ...
]
