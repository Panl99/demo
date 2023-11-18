/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : ds_0

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 18/11/2023 16:41:32
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`
(
    `id`          bigint                                          NOT NULL,
    `tenant_id`   bigint                                          NOT NULL,
    `region_code` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
    `amount`      decimal(12, 2)                                  NOT NULL,
    `mobile`      varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `create_time` datetime                                        NOT NULL,
    `update_time` datetime                                        NOT NULL,
    UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_order_detail`;
CREATE TABLE `t_order_detail`
(
    `id`          bigint                                          NOT NULL,
    `order_id`    bigint                                          NOT NULL COMMENT 't_order 表 主键',
    `tenant_id`   bigint                                          NOT NULL,
    `address`     varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `status`      tinyint                                         NOT NULL,
    `region_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `create_time` datetime                                        NOT NULL,
    `update_time` datetime                                        NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_item_0
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item_0`;
CREATE TABLE `t_order_item_0`
(
    `id`          bigint                                          NOT NULL,
    `tenant_id`   bigint                                          NOT NULL,
    `region_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_id`     varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_name`   varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `order_id`    bigint                                          NOT NULL,
    `create_time` datetime                                        NOT NULL,
    `update_time` datetime                                        NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_item_1
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item_1`;
CREATE TABLE `t_order_item_1`
(
    `id`          bigint                                          NOT NULL,
    `tenant_id`   bigint                                          NOT NULL,
    `region_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_id`     varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_name`   varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `order_id`    bigint                                          NOT NULL,
    `create_time` datetime                                        NOT NULL,
    `update_time` datetime                                        NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_item_2
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item_2`;
CREATE TABLE `t_order_item_2`
(
    `id`          bigint                                          NOT NULL,
    `tenant_id`   bigint                                          NOT NULL,
    `region_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_id`     varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_name`   varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `order_id`    bigint                                          NOT NULL,
    `create_time` datetime                                        NOT NULL,
    `update_time` datetime                                        NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_item_3
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item_3`;
CREATE TABLE `t_order_item_3`
(
    `id`          bigint                                          NOT NULL,
    `tenant_id`   bigint                                          NOT NULL,
    `region_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_id`     varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_name`   varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `order_id`    bigint                                          NOT NULL,
    `create_time` datetime                                        NOT NULL,
    `update_time` datetime                                        NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_item_4
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item_4`;
CREATE TABLE `t_order_item_4`
(
    `id`          bigint                                          NOT NULL,
    `tenant_id`   bigint                                          NOT NULL,
    `region_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_id`     varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_name`   varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `order_id`    bigint                                          NOT NULL,
    `create_time` datetime                                        NOT NULL,
    `update_time` datetime                                        NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_item_5
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item_5`;
CREATE TABLE `t_order_item_5`
(
    `id`          bigint                                          NOT NULL,
    `tenant_id`   bigint                                          NOT NULL,
    `region_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_id`     varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_name`   varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `order_id`    bigint                                          NOT NULL,
    `create_time` datetime                                        NOT NULL,
    `update_time` datetime                                        NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_item_6
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item_6`;
CREATE TABLE `t_order_item_6`
(
    `id`          bigint                                          NOT NULL,
    `tenant_id`   bigint                                          NOT NULL,
    `region_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_id`     varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_name`   varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `order_id`    bigint                                          NOT NULL,
    `create_time` datetime                                        NOT NULL,
    `update_time` datetime                                        NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_order_item_7
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item_7`;
CREATE TABLE `t_order_item_7`
(
    `id`          bigint                                          NOT NULL,
    `tenant_id`   bigint                                          NOT NULL,
    `region_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_id`     varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `good_name`   varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `order_id`    bigint                                          NOT NULL,
    `create_time` datetime                                        NOT NULL,
    `update_time` datetime                                        NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;
