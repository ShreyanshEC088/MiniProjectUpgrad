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

## Results

## First Mini Project 

<img width="1433" height="899" alt="Screenshot 2025-08-04 at 3 31 20 PM" src="https://github.com/user-attachments/assets/79fb9e5b-b392-4e75-9d42-d24f44e34d01" />#

<img width="1433" height="899" alt="Screenshot 2025-08-04 at 3 31 20 PM" src="https://github.com/user-attachments/assets/36dc1627-0b21-478f-9aa1-70a9a98b80e4" />

<img width="1432" height="851" alt="Screenshot 2025-08-04 at 3 31 47 PM" src="https://github.com/user-attachments/assets/9954266b-1aae-4bdd-9311-f08354d831cb" />

<img width="1434" height="851" alt="Screenshot 2025-08-04 at 3 32 02 PM" src="https://github.com/user-attachments/assets/b8f0c998-4923-430a-8fff-44e634ed4769" />

<img width="1434" height="856" alt="Screenshot 2025-08-04 at 3 32 17 PM" src="https://github.com/user-attachments/assets/115ccfc7-12d1-4588-bb89-4cd228776c87" />

<img width="1432" height="856" alt="Screenshot 2025-08-04 at 3 32 25 PM" src="https://github.com/user-attachments/assets/b09ccba2-8a6a-4f39-aa08-2e716c9aec6d" />

<img width="1434" height="859" alt="Screenshot 2025-08-04 at 3 32 46 PM" src="https://github.com/user-attachments/assets/5fa05c6b-2d53-4435-a8a2-a357a5ae7d6c" />

<img width="1434" height="857" alt="Screenshot 2025-08-04 at 3 32 59 PM" src="https://github.com/user-attachments/assets/1a693087-c4dd-4b82-a4df-fc88e4f809b6" />

<img width="1433" height="858" alt="Screenshot 2025-08-04 at 3 32 35 PM" src="https://github.com/user-attachments/assets/9b8bb9e3-6ac1-455f-93b2-a91c2c547b79" />

## Second Mini Project

<img width="1080" height="801" alt="Screenshot 2025-08-14 at 5 17 38 PM" src="https://github.com/user-attachments/assets/2af353ea-6e62-445e-be1f-cb7ca8630c47" />

<img width="1084" height="726" alt="Screenshot 2025-08-14 at 5 17 50 PM" src="https://github.com/user-attachments/assets/a5045737-722c-46ea-a49b-ab92c88356f0" />

<img width="1080" height="709" alt="Screenshot 2025-08-14 at 5 17 59 PM" src="https://github.com/user-attachments/assets/635ed660-bcb2-4828-a4c9-ffa7e88dc8bf" />

<img width="1086" height="758" alt="Screenshot 2025-08-14 at 5 16 11 PM" src="https://github.com/user-attachments/assets/b373c0e2-c6a1-4b34-80c5-a128e6a69dba" />

<img width="1086" height="834" alt="Screenshot 2025-08-14 at 5 11 24 PM" src="https://github.com/user-attachments/assets/0bd3530b-a1bb-46a1-8aff-95aadbfb9f26" />

<img width="1078" height="838" alt="Screenshot 2025-08-14 at 5 10 43 PM" src="https://github.com/user-attachments/assets/ffedd661-9e26-4e9b-81b9-c959af248475" />

<img width="1086" height="834" alt="Screenshot 2025-08-14 at 5 11 24 PM" src="https://github.com/user-attachments/assets/a2b96b72-7698-4e10-b0ab-07a50ebddda3" />

<img width="1089" height="857" alt="Screenshot 2025-08-14 at 5 11 40 PM" src="https://github.com/user-attachments/assets/7a04869a-a46c-44a2-ae8b-3caf77529440" />

<img width="1088" height="771" alt="Screenshot 2025-08-14 at 5 18 07 PM" src="https://github.com/user-attachments/assets/6790a889-f838-4b54-a38c-709598d908b7" />

<img width="1083" height="672" alt="Screenshot 2025-08-14 at 5 16 27 PM" src="https://github.com/user-attachments/assets/cbe90479-d7a4-41cb-bf2b-8e055f8ca178" />

<img width="1083" height="721" alt="Screenshot 2025-08-14 at 5 17 24 PM" src="https://github.com/user-attachments/assets/a34806ec-f15b-4956-8def-a0007e0dc5f9" />

<img width="1089" height="857" alt="Screenshot 2025-08-14 at 5 11 40 PM" src="https://github.com/user-attachments/assets/1add0244-f55d-4a35-90fc-c1b49ef1b744" />

<img width="1104" height="831" alt="Screenshot 2025-08-14 at 5 11 49 PM" src="https://github.com/user-attachments/assets/7196b544-334a-4b33-b5bd-d337a5d4af0b" />

<img width="1058" height="822" alt="Screenshot 2025-08-14 at 5 15 24 PM" src="https://github.com/user-attachments/assets/d749d610-c2e7-4631-a885-978e263f3046" />

<img width="1104" height="831" alt="Screenshot 2025-08-14 at 5 11 49 PM" src="https://github.com/user-attachments/assets/41752c28-5e0d-4cea-b047-f8288429beac" />

<img width="1079" height="816" alt="Screenshot 2025-08-14 at 5 10 57 PM" src="https://github.com/user-attachments/assets/773a695c-7ecf-4572-a2b0-39668d265cb2" />

<img width="1087" height="390" alt="Screenshot 2025-08-14 at 5 11 11 PM" src="https://github.com/user-attachments/assets/4b6ff8d4-4328-4958-9a14-5ef1b3e5fe03" />

<img width="1088" height="647" alt="Screenshot 2025-08-14 at 5 15 33 PM" src="https://github.com/user-attachments/assets/4609e3e0-8958-4f51-a156-e6183b0fabad" />

<img width="1083" height="721" alt="Screenshot 2025-08-14 at 5 17 24 PM" src="https://github.com/user-attachments/assets/37a6c66b-0ba6-41f9-bf7f-c00d42d5cf65" />

