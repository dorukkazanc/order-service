-- src/main/resources/data.sql

-- Insert mock users
INSERT INTO customers (username, password, active, role) VALUES
                                                             ('admin', '123', true, 'ADMIN'),
                                                             ('user', '123', true, 'CUSTOMER'),
                                                             ('alice', '123', true, 'CUSTOMER');

-- Insert TRY assets for all users (required for trading)
INSERT INTO assets (customer_id, asset_name, size, usable_size) VALUES
                                                                    ('1', 'TRY', 1000000, 1000000),
                                                                    ('2', 'TRY', 50000, 50000),
                                                                    ('3', 'TRY', 75000, 75000);

-- Insert stock assets for regular users
INSERT INTO assets (customer_id, asset_name, size, usable_size) VALUES
                                                                    ('2', 'AAPL', 10, 8),
                                                                    ('2', 'TSLA', 5, 3),
                                                                    ('2', 'GOOGL', 2, 2),
                                                                    ('2', 'MSFT', 15, 15),
                                                                    ('3', 'AAPL', 20, 20),
                                                                    ('3', 'AMZN', 3, 3),
                                                                    ('3', 'NFLX', 8, 6),
                                                                    ('3', 'META', 12, 10);

-- Insert sample orders for testing different scenarios
INSERT INTO orders (customer_id, asset_name, order_side, size, price, status) VALUES
                                                                                  ('2', 'AAPL', 'BUY', 5, 150.00, 'PENDING'),
                                                                                  ('2', 'TSLA', 'SELL', 2, 200.00, 'PENDING'),
                                                                                  ('2', 'MSFT', 'BUY', 3, 300.00, 'PENDING'),
                                                                                  ('3', 'AAPL', 'SELL', 5, 155.00, 'PENDING'),
                                                                                  ('3', 'NFLX', 'SELL', 2, 400.00, 'PENDING'),
                                                                                  ('3', 'META', 'SELL', 2, 280.00, 'PENDING'),
                                                                                  ('2', 'GOOGL', 'BUY', 1, 2500.00, 'MATCHED'),
                                                                                  ('3', 'AMZN', 'BUY', 1, 3200.00, 'MATCHED'),
                                                                                  ('2', 'TSLA', 'BUY', 1, 180.00, 'CANCELED'),
                                                                                  ('3', 'AAPL', 'BUY', 10, 140.00, 'CANCELED');