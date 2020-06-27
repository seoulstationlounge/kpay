CREATE TABLE `kpay` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `origin_user_id` bigint NOT NULL DEFAULT '0',
  `room_id` varchar(50) NOT NULL,
  `user_id` bigint NOT NULL,
  `pre_money` int DEFAULT NULL,
  `given_money` int DEFAULT NULL,
  `cur_money` int NOT NULL,
  `token` varchar(3) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`,`token`,`origin_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `kuser` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1235 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;