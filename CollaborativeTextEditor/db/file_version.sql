CREATE TABLE `file_version` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `file_id` int(11) NOT NULL,
 `version_number` int(11) NOT NULL,
 `date` date NOT NULL,
 `author` varchar(100) NOT NULL,
 `file_name_composed` varchar(100) NOT NULL,
 PRIMARY KEY (`id`),
 UNIQUE KEY `file_name_composed` (`file_name_composed`),
 KEY `file_id` (`file_id`),
 CONSTRAINT `FK_FV_F` FOREIGN KEY (`file_id`) REFERENCES `file` (`id`)
)