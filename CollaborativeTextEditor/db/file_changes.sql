CREATE TABLE `file_changes` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `file_id` int(11) NOT NULL,
 `file_version_id` int(11) NOT NULL,
 `datetime` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
 `char_position` int(11) NOT NULL,
 `char_value` varchar(2) NOT NULL,
 `author` varchar(256) NOT NULL,
 PRIMARY KEY (`id`)
)