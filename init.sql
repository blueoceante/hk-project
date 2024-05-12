CREATE DATABASE IF NOT EXISTS hkproject CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hkproject;

CREATE TABLE `order_tab` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_status` tinyint(1) unsigned NOT NULL,
  `distance` int(10) unsigned NOT NULL,
  `origin_longitude` decimal(9,6) NOT NULL,
  `origin_latitude` decimal(9,6) NOT NULL,
  `dest_longitude` decimal(9,6) NOT NULL,
  `dest_latitude` decimal(9,6) NOT NULL,
  `ctime` int(10) unsigned NOT NULL,
  `mtime` int(10) unsigned NOT NULL,
  `is_del` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
