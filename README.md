# Restaurant Management System

A Java-based Restaurant Management System built with `HttpServer` (from `com.sun.net.httpserver`) to expose RESTful APIs for managing bookings, orders, kitchen operations, billing, and admin functionalities. It follows a clean layered architecture with controllers, services, DAOs, DTOs, and models, and persists data in a relational SQL database via JDBC.

---

## Features

- **Table Booking:** Book tables, view bookings, and manage table availability.
- **Order Management:** Place orders, associate them with bookings/tables, and view all.
- **Kitchen Operations:** View live kitchen orders and mark them as prepared.
- **Billing:** Generate bills and mark them as paid.
- **Admin Panel:** Update menu item prices and generate daily sales reports.

---

## Tech Stack

- **Language:** Java
- **Framework:** Minimal setup using `HttpServer` (Spring Boot not used)
- **Database:** Relational SQL (via JDBC)
- **Build Tool:** Maven

---

## Project Structure

##  Project Structure

```plaintext
src/
├── controller/      → Handles HTTP requests (Booking, Order, Kitchen, etc.)
├── service/         → Business logic
├── dao/             → Data access logic (JDBC)
├── model/           → Domain models (Booking, Order, Bill, etc.)
├── dto/             → Data Transfer Objects (Requests & Responses)
└── util/            → Utility classes (JSON parsing, HTTP response helpers)

```


---

## API Endpoints

| Endpoint                        | Method | Description                        |
|----------------------------------|--------|------------------------------------|
| `/booking`                      | POST   | Book a table                       |
| `/booking`                      | GET    | List all bookings                  |
| `/order`                        | POST   | Place an order                     |
| `/order`                        | GET    | List all orders                    |
| `/kitchen/orders`               | GET    | Get live kitchen orders            |
| `/kitchen/orders/markPrepared`  | POST   | Mark kitchen order as prepared     |
| `/bill/generate`                | POST   | Generate bill for an order         |
| `/bill/pay`                     | POST   | Mark bill as paid                  |
| `/admin/update-price`           | POST   | Update menu item price             |
| `/admin/sales-report`           | GET    | Get daily sales report             |
| `/health`                       | GET    | Health check                       |

---

## 🧪 Getting Started

### 1. Clone the Repository
```sh
git clone https://github.com/yourusername/RestaurantManagement.git
```
```sh
cd RestaurantManagement
```
### 2. Configure Database
```sh
Edit application.properties with your database URL, username, and password.
```
### 3. Build the Project
```sh
mvn clean install
```
### 4. Run the Application
```sh
mvn exec:java -Dexec.mainClass="org.zeta.resturant.Main"
```
### 5. Access the API
The server runs by default at:
```sh
http://localhost:8080
```
Example Request
Book a Table
```sh
curl -X POST http://localhost:8080/booking \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Alice",
    "phoneNumber": "1234567890",
    "tableNumber": 5
  }'
```

### Class Diagram 
```mermaid
classDiagram
    %% ====== Controllers ======
    class Main {
        +main()
    }

    class BookingController {
        +handle(HttpExchange)
    }

    class OrderController {
        +handle(HttpExchange)
    }

    class KitchenOrderController {
        +handle(HttpExchange)
    }

    class BillController {
        +handle(HttpExchange)
    }

    class AdminController {
        +handle(HttpExchange)
    }

    %% ====== Services ======
    class BookingService {
        +createBooking()
    }

    class OrderService {
        +createOrder()
        +sendToKitchen()
    }

    class BillService {
        +generateBillFromOrder()
        +markBillAsPaid()
    }

    %% ====== DAOs ======
    class BookingDao {
        +createBooking()
        +getBookingById()
    }

    class TableDao {
        +getAvailableTables()
        +updateAvailability()
    }

    class OrderDao {
        +createOrder()
        +getOrderById()
    }

    class OrderItemDao {
        +createOrderItem()
        +getItemsByOrderId()
    }

    class KitchenOrderDao {
        +createKitchenOrder()
        +getPendingOrders()
        +markPrepared()
    }

    class MenuItemDao {
        +getAll()
        +updatePrice()
    }

    class BillDao {
        +createBill()
        +getBillByOrderId()
        +markPaid()
        +getSalesByDate()
    }

    %% ====== Models ======
    class Booking {
        -id: long
        -customerName: String
        -phoneNumber: String
        -bookingTime: Timestamp
        -tableNumber: int
        -status: String
    }

    class Table {
        -id: long
        -tableNumber: int
        -capacity: int
        -isAvailable: boolean
    }

    class MenuItem {
        -id: long
        -name: String
        -description: String
        -price: double
        -isAvailable: boolean
    }

    class Order {
        -id: long
        -tableNumber: String
        -bookingId: long
        -createdAt: Timestamp
        -status: String
        -items: List~OrderItem~
    }

    class OrderItem {
        -id: long
        -menuItem: MenuItem
        -quantity: int
    }

    class KitchenOrder {
        -id: long
        -orderId: long
        -tableNumber: int
        -itemName: String
        -quantity: int
        -status: String
    }

    class Bill {
        -id: long
        -orderId: long
        -totalAmount: double
        -isPaid: boolean
        -createdAt: Timestamp
    }

    %% ====== Relationships ======

    %% Controller -> Service
    BookingController --> BookingService
    OrderController --> OrderService
    BillController --> BillService
    KitchenOrderController --> KitchenOrderDao
    AdminController --> MenuItemDao
    AdminController --> BillDao

    %% Service -> DAO
    BookingService --> BookingDao
    BookingService --> TableDao
    OrderService --> OrderDao
    OrderService --> OrderItemDao
    OrderService --> MenuItemDao
    OrderService --> KitchenOrderDao
    BillService --> BillDao
    BillService --> OrderDao

    %% Order contains OrderItem
    Order "1" --> "*" OrderItem
    OrderItem --> MenuItem

    %% DAO <-> Models
    BookingDao --> Booking
    TableDao --> Table
    OrderDao --> Order
    OrderItemDao --> OrderItem
    MenuItemDao --> MenuItem
    KitchenOrderDao --> KitchenOrder
    BillDao --> Bill

    %% Main creates all contexts
    Main --> BookingController
    Main --> OrderController
    Main --> KitchenOrderController
    Main --> BillController
    Main --> AdminController

```

### ER Diagram
```mermaid
   erDiagram
    bookings {
        int id PK
        string customer_name
        string phone_number
        string booking_time
        int table_number
        string status
    }

    tables {
        int id PK
        int table_number
        int capacity
        boolean is_available
    }

    orders {
        int id PK
        string table_number
        int booking_id FK
        string created_at
        string status
    }

    menu_items {
        int id PK
        string name
        string description
        float price
        boolean is_available
    }

    order_items {
        int id PK
        int order_id FK
        int menu_item_id FK
        int quantity
    }

    kitchen_orders {
        int id PK
        int order_id FK
        int table_number
        string item_name
        int quantity
        string status
        string created_at
    }

    bills {
        int id PK
        int order_id FK
        float total_amount
        boolean is_paid
        string created_at
    }

    %% Relationships
    bookings ||--o{ orders : has
    orders ||--o{ order_items : contains
    menu_items ||--o{ order_items : includes
    orders ||--o{ kitchen_orders : generates
    orders ||--|| bills : billed_by
```

```mermaid
flowchart TD
    A([Start]) --> B{Request Path/Method}
    B -- "/bill/generate + POST" --> C[handleBillGenerate]
    B -- "/bill/pay + POST" --> D[handleBillPayment]
    B -- else --> E[Send 404 Response]
    C --> F{Parse orderId from JSON}
    F -- Order not found --> G[Send 404 Response]
    F -- Order found --> H[Generate Bill]
    H --> I[Send Bill as JSON]
    D --> J{Parse billId from JSON}
    J --> K[Mark Bill as Paid]
    K --> L[Send Success JSON]
    G --> M([End])
    I --> M
    L --> M
    E --> M
```

```mermaid
flowchart TD
    A([Start]) --> B{Request Path/Method}
    B -- "/admin/update-price + POST" --> C[handleUpdatePrice]
    B -- "/admin/sales-report + GET" --> D[handleSalesReport]
    B -- else --> E[Send 404 Response]
    C --> F[Parse itemId and newPrice from JSON]
    F --> G[Update Menu Price]
    G --> H[Send Success JSON]
    D --> I[Parse date from Query]
    I --> J[Get Sales Report]
    J --> K[Send Report as JSON]
    H --> L([End])
    K --> L
    E --> L
```

```mermaid
stateDiagram-v2
    [*] --> CheckMethod
    CheckMethod --> PostOrder: POST
    CheckMethod --> GetOrders: GET
    CheckMethod --> MethodNotAllowed: Other
    PostOrder --> Send201
    GetOrders --> Send200
    MethodNotAllowed --> Send405
    Send201 --> [*]
    Send200 --> [*]
    Send405 --> [*]
```

