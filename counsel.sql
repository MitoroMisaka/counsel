/*
 Navicat Premium Data Transfer

 Source Server         : Arkidian
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : 1.117.156.226:3306
 Source Schema         : counsel

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 31/05/2023 13:53:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gender` int NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `admin_user` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, '东雪莲', 'root', 'adc3949ba5', 'admin', '1234', 0, '1234', '1234', '金牌员工');

-- ----------------------------
-- Table structure for arrange
-- ----------------------------
DROP TABLE IF EXISTS `arrange`;
CREATE TABLE `arrange`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` datetime NULL DEFAULT NULL,
  `creator` bigint NULL DEFAULT NULL,
  `last_update_time` datetime NULL DEFAULT NULL,
  `last_updater` bigint NULL DEFAULT NULL,
  `year` int NULL DEFAULT NULL,
  `month` int NULL DEFAULT NULL,
  `day` int NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  `user` bigint NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `weekday` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `local_date` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of arrange
-- ----------------------------
INSERT INTO `arrange` VALUES (1, '2023-05-24 02:48:21', 1, '2023-05-24 02:48:24', 1, 2023, 5, 24, '2023-05-24 13:00:00', '2023-05-24 17:00:00', 3, 'Counselor', '3', '2023-05-24 02:48:56');
INSERT INTO `arrange` VALUES (3, '2023-05-24 02:48:21', 1, '2023-05-24 02:48:24', 1, 2023, 5, 24, '2023-05-24 19:00:00', '2023-05-24 21:00:00', 3, 'Counselor', '3', '2023-05-24 02:48:56');
INSERT INTO `arrange` VALUES (15, '2023-05-24 02:48:21', 1, '2023-05-24 02:48:24', 1, 2023, 5, 24, '2023-05-24 19:00:00', '2023-05-24 21:00:00', 15, 'Counselor', '3', '2023-05-24 02:48:56');

-- ----------------------------
-- Table structure for conversation
-- ----------------------------
DROP TABLE IF EXISTS `conversation`;
CREATE TABLE `conversation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` datetime NULL DEFAULT NULL,
  `creator` bigint NULL DEFAULT NULL,
  `last_update_time` datetime NULL DEFAULT NULL,
  `last_updater` bigint NULL DEFAULT NULL,
  `year` int NULL DEFAULT NULL,
  `month` int NULL DEFAULT NULL,
  `day` int NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  `user` bigint NULL DEFAULT NULL,
  `counselor` bigint NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `visitor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evaluate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `conversation_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of conversation
-- ----------------------------
INSERT INTO `conversation` VALUES (1, '2023-05-24 14:05:14', 1, '2023-05-24 14:05:21', 1, 2023, 5, 24, '2023-05-24 13:00:00', '2023-05-24 14:00:00', 4, 3, 'active', 'nanami', '0', '0');
INSERT INTO `conversation` VALUES (2, '2023-05-24 14:05:14', 1, '2023-05-24 14:05:21', 1, 2023, 5, 24, '2023-05-24 15:00:00', '2023-05-24 16:00:00', 4, 3, 'active', 'nanami', '0', '0');
INSERT INTO `conversation` VALUES (3, '2023-05-25 14:05:14', 1, '2023-05-25 14:05:21', 1, 2023, 5, 25, '2023-05-25 15:00:00', '2023-05-25 16:00:00', 4, 3, 'active', 'nanami', '0', '0');

-- ----------------------------
-- Table structure for counselor
-- ----------------------------
DROP TABLE IF EXISTS `counselor`;
CREATE TABLE `counselor`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `age` int NULL DEFAULT NULL,
  `id_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `enabled` bit(1) NULL DEFAULT NULL,
  `deleted` bit(1) NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rating` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `supervisors` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `max_consult` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of counselor
-- ----------------------------
INSERT INTO `counselor` VALUES (3, '鬼屎东', 'wuhugsd', '1879b0d0c8', 'COUNSELOR', '3', 1824, '110101199001011234', '13881006993', '123@gmail.com', '1', '1', 'q=tbn:ANd9GcQKKEsVP4BbdXrF0d2Jve5mpwHkTrQ3BPtZxg&usqp=CAU', '2023-05-24 01:48:36', '2023-05-24 14:24:43', b'1', b'0', 'ONLINE', '0', NULL, NULL);
INSERT INTO `counselor` VALUES (15, '瓜皮', 'guapi', '123456', 'COUNSELOR', '0', 18, '110101199001011234', '13981006993', '1233@gmail.com', '1', '1', 'q=tbn:ANd9GcQKKEsVP4BbdXrF0d2Jve5mpwHkTrQ3BPtZxg&usqp=CAU', '2023-05-24 23:47:34', NULL, b'1', b'0', 'OFFLINE', '0', NULL, NULL);

-- ----------------------------
-- Table structure for supervise
-- ----------------------------
DROP TABLE IF EXISTS `supervise`;
CREATE TABLE `supervise`  (
  `counselor_id` bigint NOT NULL,
  `supervisor_id` bigint NOT NULL,
  PRIMARY KEY (`counselor_id`, `supervisor_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of supervise
-- ----------------------------
INSERT INTO `supervise` VALUES (3, 2);

-- ----------------------------
-- Table structure for supervisor
-- ----------------------------
DROP TABLE IF EXISTS `supervisor`;
CREATE TABLE `supervisor`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gender` int NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `qualification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `qualification_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `supervisor_user` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of supervisor
-- ----------------------------
INSERT INTO `supervisor` VALUES (2, '刁徳二', 'guapi', '123456', 'COUNSELOR', 'q=tbn:ANd9GcQKKEsVP4BbdXrF0d2Jve5mpwHkTrQ3BPtZxg&usqp=CAU', 0, '13981006993', '1', '1', NULL, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '东雪莲', 'root', '1879b0d0c8', 'admin');
INSERT INTO `user` VALUES (2, '刁德一', 'wuhudsm', '1879b0d0c8', 'supervisor');
INSERT INTO `user` VALUES (3, '鬼屎东', 'wuhugsd', '996404a265', 'counselor');
INSERT INTO `user` VALUES (4, '林忆宁', 'nanami', '1879b0d0c8', 'visitor');
INSERT INTO `user` VALUES (5, NULL, NULL, NULL, 'visitor');
INSERT INTO `user` VALUES (6, NULL, NULL, NULL, 'visitor');
INSERT INTO `user` VALUES (7, NULL, NULL, NULL, 'visitor');
INSERT INTO `user` VALUES (11, NULL, '1234567890', NULL, 'visitor');
INSERT INTO `user` VALUES (15, '瓜皮', 'guapi', 'adc3949ba5', 'COUNSELOR');
INSERT INTO `user` VALUES (41, 'Alice', 'Ace', NULL, 'visitor');
INSERT INTO `user` VALUES (43, NULL, NULL, NULL, 'visitor');
INSERT INTO `user` VALUES (44, NULL, NULL, NULL, 'visitor');
INSERT INTO `user` VALUES (45, NULL, NULL, NULL, 'visitor');
INSERT INTO `user` VALUES (46, NULL, NULL, NULL, 'visitor');
INSERT INTO `user` VALUES (47, NULL, NULL, NULL, 'visitor');
INSERT INTO `user` VALUES (48, 'Alice', 'ace', NULL, 'visitor');

-- ----------------------------
-- Table structure for visitor
-- ----------------------------
DROP TABLE IF EXISTS `visitor`;
CREATE TABLE `visitor`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gender` int NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `emergent_contact` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `emergent_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `openid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `available` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `visitor_user` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of visitor
-- ----------------------------
INSERT INTO `visitor` VALUES (4, '林忆宁', 'nanami', 'adc3949ba5', 'visitor', '1234', 0, '1234', '1234', '金牌员工', '叔叔', '1234', 'sadasdas', NULL);
INSERT INTO `visitor` VALUES (11, '乔拜登', NULL, NULL, 'visitor', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKKEsVP4BbdXrF0d2Jve5mpwHkTrQ3BPtZxg&usqp=CAU', 1, '13812345678', NULL, NULL, '克林顿', '13912345678', '1234567890', NULL);
INSERT INTO `visitor` VALUES (48, 'Alice', 'ace', NULL, 'visitor', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKKEsVP4BbdXrF0d2Jve5mpwHkTrQ3BPtZxg&usqp=CAU', 1, '18812345678', 'ecnu', 'dcdsc', 'strange', '18887654321', 'o1aWn5bZOlE2G0Wm_KMlyQ8Q8oZM', NULL);

SET FOREIGN_KEY_CHECKS = 1;
