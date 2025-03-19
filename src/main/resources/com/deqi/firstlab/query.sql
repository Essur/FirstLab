-- 1. Create the schema
CREATE SCHEMA IF NOT EXISTS my_schema;

-- 2. Set the schema to be used
SET search_path TO my_schema;

-- 3. Create the 'users' table
CREATE TABLE IF NOT EXISTS users (
                                     user_id SERIAL PRIMARY KEY,
                                     username VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- 4. Create the 'orders' table
CREATE TABLE IF NOT EXISTS orders (
                                      order_id SERIAL PRIMARY KEY,
                                      user_id INT REFERENCES users(user_id) ON DELETE CASCADE,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'pending'
    );

-- 5. Insert sample data into 'users' table
INSERT INTO users (username, email, password) VALUES
                                                  ('john_doe', 'john@example.com', 'hashedpassword1'),
                                                  ('jane_doe', 'jane@example.com', 'hashedpassword2'),
                                                  ('alice_smith', 'alice@example.com', 'hashedpassword3');


-- 6. Insert sample data into 'orders' table
INSERT INTO orders (user_id, amount, status) VALUES
                                                 (1, 150.00, 'completed'),
                                                 (1, 250.50, 'pending'),
                                                 (2, 90.75, 'shipped'),
                                                 (3, 120.00, 'completed');

-- 7. Query to check the data
SELECT * FROM users;
SELECT * FROM orders;
