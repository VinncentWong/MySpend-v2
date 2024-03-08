DROP TABLE IF EXISTS `artwork`;
CREATE TABLE IF NOT EXISTS `artwork`(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    category VARCHAR(255),
    description TEXT,
    stock BIGINT,
    photo VARCHAR(255),
    weight DECIMAL,
    dimension_x DECIMAL,
    dimension_y DECIMAL,
    dimension_z DECIMAL,
    is_preorder BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    is_active BOOLEAN,
    fk_user_id INT
) ENGINE = INNODB;

CREATE INDEX `artwork_index` ON `artwork`(name, category, stock);