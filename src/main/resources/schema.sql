CREATE TABLE sanctioned_person (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(1000) NOT NULL,
                       normalized_name VARCHAR(1000),
                       is_deleted BOOLEAN NOT NULL DEFAULT false
);
