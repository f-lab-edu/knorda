DROP TABLE IF EXISTS "member";
CREATE TABLE "member" (
                        `member_id` INT(10) PRIMARY KEY NOT NULL AUTO_INCREMENT,
                        `member_name` VARCHAR(20) NOT NULL,
                        `password` VARCHAR(128) NOT NULL,
                        `last_logged_at` DATETIME NOT NULL,
                        `is_deleted` TINYINT(1) NOT NULL,
                        `description` MEDIUMTEXT NOT NULL,
                        `created_at` DATETIME NOT NULL,
                        `modified_at` DATETIME NOT NULL,
                        `modified_by` VARCHAR(20) NOT NULL
);
