CREATE TABLE `file` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `name` varchar(50) NOT NULL,
 `lastModified` datetime NOT NULL,
 `lastEditor` varchar(50) NOT NULL,
 PRIMARY KEY (`id`)
)