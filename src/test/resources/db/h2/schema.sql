DROP TABLE IF EXISTS "member";
CREATE TABLE "member" (
                        `member_id` INT(10) PRIMARY KEY NOT NULL AUTO_INCREMENT,
                        `username` VARCHAR(20) NOT NULL,
                        `password` VARCHAR(128) NOT NULL,
                        `last_logged_at` DATETIME ,
                        `is_deleted` TINYINT(1),
                        `description` MEDIUMTEXT,
                        `created_at` DATETIME,
                        `modified_at` DATETIME,
                        `modified_by` VARCHAR(20)
);
