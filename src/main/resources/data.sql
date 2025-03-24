-- First, clean up existing data (if any) in reverse order of dependencies
DELETE FROM product;

-- Sample data for Product entity
INSERT INTO product (name, description, price, quantity) VALUES
                                                             ('Laptop', 'High-performance laptop with 16GB RAM', 1299.99, 10),
                                                             ('Smartphone', 'Latest model with 128GB storage', 799.99, 20),
                                                             ('Headphones', 'Wireless noise-cancelling headphones', 199.99, 30),
                                                             ('Tablet', '10-inch screen with 64GB storage', 349.99, 15),
                                                             ('Smartwatch', 'Fitness tracking and notifications', 249.99, 25);