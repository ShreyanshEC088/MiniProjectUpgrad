CREATE TABLE customer (
    customer_id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE restaurant_table (
    table_id SERIAL PRIMARY KEY,
    table_number VARCHAR(20),
    capacity INTEGER,
    is_available BOOLEAN DEFAULT TRUE
);

CREATE TABLE booking (
    booking_id SERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL,
    table_id INTEGER NOT NULL,
    booking_time TIMESTAMP NOT NULL,
    status VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (table_id) REFERENCES restaurant_table(table_id)
);

CREATE TABLE menu_item (
    item_id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    price DECIMAL(10, 2),
    is_available BOOLEAN DEFAULT TRUE,
    category VARCHAR(50)
);

CREATE TABLE order_(
    order_id SERIAL PRIMARY KEY,
    table_id INTEGER NOT NULL,
    booking_id INTEGER,
    order_time TIMESTAMP NOT NULL,
    status VARCHAR(20),
    FOREIGN KEY (table_id) REFERENCES restaurant_table(table_id),
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id)
);

CREATE TABLE order_item (
    order_item_id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    special_instructions TEXT,
    FOREIGN KEY (order_id) REFERENCES order_(order_id),
    FOREIGN KEY (item_id) REFERENCES menu_item(item_id)
);

CREATE TABLE bill (
    bill_id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL,
    total_amount DECIMAL(10, 2),
    discount DECIMAL(10, 2),
    tax DECIMAL(10, 2),
    final_amount DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES order_(order_id)
);

CREATE TABLE payment (
    payment_id SERIAL PRIMARY KEY,
    bill_id INTEGER NOT NULL,
    payment_method VARCHAR(50),
    payment_time TIMESTAMP NOT NULL,
    payment_status VARCHAR(20),
    FOREIGN KEY (bill_id) REFERENCES bill(bill_id)
);

CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50)
);

CREATE TABLE user (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id INTEGER NOT NULL,
    name VARCHAR(100),
    contact VARCHAR(100),
    FOREIGN KEY (role_id) REFERENCES role(role_id)
);
