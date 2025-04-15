-- 1. Create schema (MySQL calls it "database")
CREATE DATABASE IF NOT EXISTS myapp_db;

-- 2. Use the schema
USE myapp_db;

-- 3. Create users table
CREATE TABLE IF NOT EXISTS users (
                                     user_id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- 4. Create orders table
CREATE TABLE IF NOT EXISTS orders (
                                      order_id INT AUTO_INCREMENT PRIMARY KEY,
                                      user_id INT,
                                      order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'pending',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
    );

-- 5. Insert sample data
INSERT INTO users (username, email, password) VALUES
                                                  ('diana_mysql', 'diana@mysql.com', 'mysql_pass1'),
                                                  ('edward_mysql', 'edward@mysql.com', 'mysql_pass2'),
                                                  ('frank_mysql', 'frank@mysql.com', 'mysql_pass3');

INSERT INTO orders (user_id, amount, status) VALUES
                                                 (1, 99.99, 'completed'),
                                                 (1, 199.49, 'processing'),
                                                 (2, 299.99, 'shipped');
