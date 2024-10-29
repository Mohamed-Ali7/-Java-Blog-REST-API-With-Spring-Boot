DROP DATABASE IF EXISTS `crimson_code`;
CREATE DATABASE IF NOT EXISTS `crimson_code`;
USE `crimson_code`;

DROP USER IF EXISTS 'crimson_code_dev'@'localhost';
CREATE USER IF NOT EXISTS 'crimson_code_dev'@'localhost' IDENTIFIED BY 'crimson_code_dev';

GRANT ALL PRIVILEGES ON `crimson_code`.* TO `crimson_code_dev`@`localhost`;

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`public_id` VARCHAR(60) NOT NULL UNIQUE,
`email` VARCHAR(120) NOT NULL UNIQUE,
`password` VARCHAR(100) NOT NULL,
`first_name` VARCHAR(50) NOT NULL,
`last_name` VARCHAR(50) NOT NULL,
`joined_at` TIMESTAMP NOT NULL,
`email_verification_token` VARCHAR(250),
`is_user_verified` BOOLEAN NOT NULL
);

DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE IF NOT EXISTS `users_roles` (
`user_id` INT NOT NULL,
`role_id` INT NOT NULL,
PRIMARY KEY (`user_id`, `role_id`),
CONSTRAINT `FK_USER` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
);

DROP TABLE IF EXISTS `posts`;
CREATE TABLE IF NOT EXISTS `posts` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`title` VARCHAR(255) NOT NULL,
`content` TEXT NOT NULL,
`user_id` INT NOT NULL,
`user_public_id` VARCHAR(150) NOT NULL,
`created_at` timestamp NOT NULL,

CONSTRAINT `FK_POST_USER` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

DROP TABLE IF EXISTS `comments`;
CREATE TABLE IF NOT EXISTS `comments` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`content` TEXT NOT NULL,
`user_id` INT NOT NULL,
`user_public_id` VARCHAR(150) NOT NULL,
`post_id` INT NOT NULL,
`created_at` timestamp NOT NULL,

CONSTRAINT `FK_COMMENT_USER` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
CONSTRAINT `FK_COMMENT_POST` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
);

DROP TABLE IF EXISTS `password_reset_token`;
CREATE TABLE IF NOT EXISTS `password_reset_token` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`token` TEXT NOT NULL,
`user_id` INT NOT NULL UNIQUE,

CONSTRAINT `FK_PWD_RESET_TOKEN_USER` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

DROP TABLE IF EXISTS `token_blocklist`;
CREATE TABLE IF NOT EXISTS `token_blocklist` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`token` TEXT NOT NULL
);

