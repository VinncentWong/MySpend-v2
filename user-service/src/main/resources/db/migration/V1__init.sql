DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    birth_date TIMESTAMP,
    role VARCHAR(20),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    is_active BOOLEAN
) ENGINE = INNODB;

CREATE INDEX `user_index` ON `user`(name, email);