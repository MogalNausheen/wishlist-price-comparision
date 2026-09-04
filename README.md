# 🛍️ Wishlist Product Price Comparison

A full-stack web application that helps users **search for products, compare prices from different online stores, identify the best available price, and save products to a personal wishlist**.

The application uses a **Java Spring Boot backend**, **HTML/CSS/JavaScript + Bootstrap frontend**, **MySQL database**, and **SerpApi Google Shopping** for retrieving product information and prices.

---

## 📌 Project Overview

Finding the best price for a product often requires checking multiple online stores.

This project provides a simple solution by allowing users to search for a product and view available products from different stores in one place.

Users can:

* 🔎 Search for products
* 💰 Compare prices from different stores
* 🏆 Identify the best new-product price
* 🔄 Identify the best refurbished-product price
* ❤️ Add products to a wishlist
* 🗑️ Remove products from the wishlist
* 🔗 Open the product's original shopping page
* 📱 Use the application on desktop and mobile devices

---

## ✨ Features

### 🔎 Product Search

Users can enter a product name and search for matching products.

The application retrieves product information through the backend and displays:

* Product name
* Price
* Store
* Product image
* Product link
* New/refurbished status

### 💰 Price Comparison

Products are automatically sorted from **lowest price to highest price**.

The application identifies:

* 🏆 Best New Price
* 🔄 Best Refurbished Price

Monthly installment prices such as prices containing `/mo` are excluded from the comparison.

### 🎯 Product Matching

The backend contains product-matching logic to reduce irrelevant search results.

For example, when searching for a specific phone model, accessories such as:

* Phone cases
* Chargers
* Cables
* Screen protectors
* Adapters
* Replacement parts

are filtered out.

The application also handles model-specific matching to avoid displaying unrelated variants.

### ❤️ Wishlist

Users can add products to their wishlist.

Wishlist information is stored in the MySQL database.

Users can:

* Add products
* View saved products
* Remove products

### 🔗 View Product

The **View Product** button opens the corresponding shopping page so users can continue to the original store.

### 📱 Responsive Design

The frontend is designed to work across:

* Desktop
* Laptop
* Tablet
* Mobile devices

Bootstrap and responsive CSS are used to create the user interface.

---

## 🛠️ Technologies Used

### Frontend

* HTML5
* CSS3
* JavaScript (ES6+)
* Bootstrap 5

### Backend

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* REST APIs
* Maven

### Database

* MySQL

### External API

* SerpApi
* Google Shopping Search

### Development Tools

* Eclipse
* Visual Studio Code
* Git
* GitHub
* Postman

---

## 🏗️ Project Architecture

```text
                    ┌─────────────────────┐
                    │       User          │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Frontend        │
                    │ HTML/CSS/JS/Bootstrap│
                    └──────────┬──────────┘
                               │
                         REST API Calls
                               │
                               ▼
                    ┌─────────────────────┐
                    │   Spring Boot       │
                    │      Backend        │
                    └──────┬───────┬──────┘
                           │       │
                ┌──────────┘       └──────────┐
                ▼                             ▼
       ┌─────────────────┐           ┌─────────────────┐
       │     MySQL       │           │    SerpApi      │
       │    Database     │           │ Google Shopping │
       └─────────────────┘           └─────────────────┘
```

---

## 📂 Project Structure

```text
Wishlist Project/
│
├── .gitignore
├── README.md
│
├── wishlist-frontend/
│   ├── images/
│   │   └── iphone15.jpg
│   ├── index.html
│   ├── script.js
│   └── style.css
│
└── wishlist-price-comparision/
    │
    ├── pom.xml
    ├── mvnw
    ├── mvnw.cmd
    │
    └── src/
        ├── main/
        │   ├── java/
        │   │   └── com/team/wishlist/
        │   │       ├── WishlistPriceComparisionApplication.java
        │   │       │
        │   │       ├── controller/
        │   │       │   ├── ExternalProductController.java
        │   │       │   ├── ProductController.java
        │   │       │   └── WishlistController.java
        │   │       │
        │   │       ├── entity/
        │   │       │   ├── ExternalProduct.java
        │   │       │   ├── Product.java
        │   │       │   └── WishlistItem.java
        │   │       │
        │   │       ├── repository/
        │   │       │   ├── ProductRepository.java
        │   │       │   └── WishlistRepository.java
        │   │       │
        │   │       └── service/
        │   │           ├── ExternalProductService.java
        │   │           ├── ProductService.java
        │   │           └── WishlistService.java
        │   │
        │   └── resources/
        │       └── application.properties
        │
        └── test/
```

> `application.properties` contains environment-specific configuration and API credentials, so it is excluded from Git using `.gitignore`.

---

## 🔄 Application Workflow

### 1. Search

The user enters a product name in the frontend.

```text
User → Frontend → Spring Boot Backend
```

### 2. External Product Search

The backend sends the search request to SerpApi Google Shopping.

```text
Spring Boot → SerpApi → Google Shopping Results
```

### 3. Product Processing

The backend:

* Extracts product information
* Filters irrelevant products
* Removes monthly installment prices
* Identifies new/refurbished products
* Sorts products by price

### 4. Display Results

The frontend displays the processed products and highlights the best prices.

### 5. Add to Wishlist

When a user clicks **Add to Wishlist**:

```text
Frontend
   ↓
POST /api/wishlist
   ↓
Spring Boot
   ↓
MySQL
```

### 6. Load Wishlist

The saved wishlist products are retrieved from MySQL through the Spring Boot REST API.

---

## 🔌 REST API Endpoints

### External Product Search

```http
GET /api/external-products/search?name={productName}
```

Used to search for products through the external shopping API.

### Get Products

```http
GET /api/products
```

Retrieves products stored in the application database.

### Add Wishlist Item

```http
POST /api/wishlist
```

Adds a product to the user's wishlist.

### Get Wishlist

```http
GET /api/wishlist
```

Retrieves saved wishlist products.

### Delete Wishlist Item

```http
DELETE /api/wishlist/{id}
```

Removes a product from the wishlist.

---

## 🗄️ Database

The application uses **MySQL**.

The wishlist stores information such as:

* Product name
* Price
* Store
* Image URL
* Product URL

The backend uses **Spring Data JPA** to communicate with the database.

---

## 🔐 Environment Variables

Sensitive configuration is not stored directly in GitHub.

The application uses environment variables such as:

```text
DB_PASSWORD
SERPAPI_KEY
```

The local `application.properties` uses these variables instead of exposing credentials.

Example:

```properties
spring.datasource.password=${DB_PASSWORD}

serpapi.api.key=${SERPAPI_KEY}
```

> Never commit real API keys or database passwords to GitHub.

---

## ▶️ How to Run the Project

### Prerequisites

Install:

* Java JDK
* Maven
* MySQL
* Git
* A modern web browser

You also need a SerpApi API key.

### 1. Clone the Repository

```bash
git clone https://github.com/MogalNausheen/wishlist-price-comparision.git
```

### 2. Configure MySQL

Create the database:

```sql
CREATE DATABASE wishlist_db;
```

Configure your local database credentials through environment variables.

### 3. Configure API Key

Set:

```text
SERPAPI_KEY
```

as an environment variable.

Also set:

```text
DB_PASSWORD
```

for your MySQL password.

### 4. Start the Backend

Navigate to:

```text
wishlist-price-comparision/
```

Then run the Spring Boot application.

Using Maven:

```bash
mvn spring-boot:run
```

Or run:

```text
WishlistPriceComparisionApplication.java
```

from Eclipse.

The backend runs on:

```text
http://localhost:8080
```

### 5. Start the Frontend

Open:

```text
wishlist-frontend/index.html
```

using a browser or VS Code Live Server.

The frontend communicates with the Spring Boot backend through REST APIs.

---

## 🧪 Testing

The application was tested for:

* Product searching
* Price sorting
* Product filtering
* New/refurbished price detection
* Adding products to wishlist
* Loading wishlist
* Removing wishlist items
* Opening product links
* Long product URLs
* Responsive UI
* API communication
* Database persistence

---

## 🚀 Future Enhancements

Possible future improvements include:

* 👤 User authentication and registration
* 🔔 Price-drop notifications
* 📊 Price history charts
* 💾 Multiple wishlists
* 🔍 Advanced filters
* ⭐ Product ratings and reviews
* 📧 Email notifications
* 🌐 Deployment to a cloud platform
* 📱 Progressive Web App support

---

## 👩‍💻 Author

**Mogal Nausheen Begum**

B.Tech – Computer Science and Engineering

GitHub:
https://github.com/MogalNausheen

---

## 📄 License

This project was developed as a college/academic project for learning and demonstrating full-stack web development concepts.
