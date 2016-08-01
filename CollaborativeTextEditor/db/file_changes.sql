CREATE TABLE `file_changes` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `file_id` int(11) NOT NULL,
 `file_version_id` int(11) NOT NULL,
 `char_position` int(11) NOT NULL,
 `char_value` varchar(2) NOT NULL,
 `author` varchar(256) NOT NULL,
 `datetime` bigint(20) NOT NULL,
 PRIMARY KEY (`id`)
)