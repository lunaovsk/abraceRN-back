CREATE TABLE item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    category VARCHAR(100) NOT NULL,
    type VARCHAR(100),
    size VARCHAR(100),
    gender VARCHAR(100),
    expiration_at DATE,
    quantity INT NOT NULL,
    create_at TIMESTAMP NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE
);