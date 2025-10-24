CREATE TABLE item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    size VARCHAR(10),
    gender VARCHAR(10),
    quantity INT NOT NULL,
    created_at DATE NOT NULL,
    expiration_at DATE
);
