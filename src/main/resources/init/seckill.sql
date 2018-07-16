DROP database if EXISTS seckill;
CREATE DATABASE seckill  DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
DROP TABLE if EXISTS `sk_shop`;
CREATE TABLE `sk_shop` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '库存数量',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀开启时间',
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `version` int(11) NOT NULL COMMENT '版本号',
  PRIMARY KEY (`seckill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1004  COMMENT='秒杀库存表';
insert  into `sk_shop`(`seckill_id`,`name`,`number`,`start_time`,`end_time`,`create_time`,`version`)
values (1000,'1000元秒杀iphone8',10,'2018-07-10 15:31:53','2022-05-10 15:31:53','2018-05-10 15:31:53',0),
  (1001,'500元秒杀ipad2',100,'2018-07-10 15:31:53','2022-05-10 15:31:53','2018-05-10 15:31:53',0),
  (1002,'300元秒杀小米4',100,'2018-07-10 15:31:53','2022-05-10 15:31:53','2018-05-10 15:31:53',0),
  (1003,'200元秒杀红米note',100,'2018-07-10 15:31:53','2022-05-10 15:31:53','2018-05-10 15:31:53',0);
