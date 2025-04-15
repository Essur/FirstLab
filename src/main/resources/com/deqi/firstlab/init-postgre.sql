-- 1. Create schema
CREATE SCHEMA IF NOT EXISTS my_schema;

-- 2. Use the schema
SET search_path TO my_schema;

-- 3. Create users table
CREATE TABLE IF NOT EXISTS users (
                                     user_id SERIAL PRIMARY KEY,
                                     username VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- 4. Create orders table
CREATE TABLE IF NOT EXISTS orders (
                                      order_id SERIAL PRIMARY KEY,
                                      user_id INT REFERENCES users(user_id) ON DELETE CASCADE,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'pending'
    );

-- 5. Insert sample data
INSERT INTO users (username, email, password) VALUES
                                                  ('andrew_post', 'andrew@pg.com', 'pg_pass1'),
                                                  ('beth_pg', 'beth@pg.com', 'pg_pass2'),
                                                  ('charlie_pg', 'charlie@pg.com', 'pg_pass3');

INSERT INTO orders (user_id, amount, status) VALUES
                                                 (1, 300.00, 'completed'),
                                                 (2, 200.50, 'processing'),
                                                 (3, 450.75, 'cancelled');
