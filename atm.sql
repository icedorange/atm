/*
Navicat MySQL Data Transfer

Source Server         : 本地连接
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : atm

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2017-06-07 22:24:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for a_card
-- ----------------------------
DROP TABLE IF EXISTS `a_card`;
CREATE TABLE `a_card` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账户id',
  `card_num` varchar(255) NOT NULL COMMENT '银行卡号',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `money` int(11) DEFAULT NULL COMMENT '余额',
  `user_id` int(11) NOT NULL COMMENT '用户',
  `version` int(11) NOT NULL COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for a_record
-- ----------------------------
DROP TABLE IF EXISTS `a_record`;
CREATE TABLE `a_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水单号',
  `oper_id` int(11) NOT NULL COMMENT '操作人',
  `aim_id` int(11) NOT NULL COMMENT '流向',
  `oper_type` tinyint(4) NOT NULL COMMENT '操作类型：1取款；2存款；3转账',
  `money` int(255) NOT NULL COMMENT '操作金额',
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
