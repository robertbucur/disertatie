CREATE TABLE `file_changes` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `file_id` int(11) NOT NULL,
 `file_version_id` int(11) NOT NULL,
 `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 `char_row` int(11) NOT NULL,
 `char_column` int(11) NOT NULL,
 `char_position` int(11) NOT NULL,
 `char_value` varchar(1) NOT NULL,
 PRIMARY KEY (`id`)
)