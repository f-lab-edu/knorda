DROP TABLE IF EXISTS "product";
DROP TABLE IF EXISTS "member";
CREATE TABLE "member"
(
    `member_id`         INT(10) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `member_name`       VARCHAR(20)         NOT NULL,
    `password`          VARCHAR(128)        NOT NULL,
    `last_logged_in_at` DATETIME            NOT NULL,
    `is_deleted`        TINYINT(1)          NOT NULL,
    `description`       MEDIUMTEXT          NOT NULL,
    `created_at`        DATETIME            NOT NULL,
    `modified_at`       DATETIME            NOT NULL,
    `modified_by`       VARCHAR(20)         NOT NULL
);

CREATE TABLE "product"
(
    `product_id`  integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `member_id`   integer             NOT NULL,
    `name`        varchar(200)        NOT NULL,
    `image_url`   varchar(200),
    `description` mediumtext          NOT NULL,
    `is_deleted`  boolean             NOT NULL,
    `created_at`  datetime            NOT NULL,
    `modified_at` datetime            NOT NULL,
    CONSTRAINT `product_name` UNIQUE (`name`)
);

ALTER TABLE "product"
    ADD FOREIGN KEY (`member_id`) REFERENCES "member" (`member_id`);
