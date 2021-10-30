CREATE DATABASE  IF NOT EXISTS `service_table` CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE USER 'yokudlela'@'%' IDENTIFIED BY 'yokudlela';
GRANT ALL PRIVILEGES ON `service_table`.* TO `yokudlela`@`%`; 