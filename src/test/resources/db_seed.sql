-- 1. Create Table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'user',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Insert Mock Data (Matches your DBValidationTest assertions)
INSERT IGNORE INTO users (username, email, role, is_active) VALUES 
('admin_user', 'admin@test.com', 'admin', 1),
('standard_user', 'user@test.com', 'user', 1),
('locked_user', 'locked@test.com', 'user', 0),
('new_signup', 'newbie@test.com', 'guest', 1),
('api_bot', 'bot@api.com', 'system', 1);