-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.2.0 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  12.0.0.6468
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 shir-boot 的数据库结构
CREATE DATABASE IF NOT EXISTS `shir-boot` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `shir-boot`;

-- 导出  表 shir-boot.chat_history 结构
CREATE TABLE IF NOT EXISTS `CHAT_HISTORY` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `QUESTION` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户提问',
  `ANSWER` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'AI回答',
  `MASK` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '预计人设',
  `USER_ID` bigint NOT NULL,
  `CONSUME_TOKEN` float NOT NULL DEFAULT '0',
  `SESSION_ID` varchar(80) DEFAULT NULL COMMENT '会话ID',
  `CREATE_TIME` datetime NOT NULL,
  `ATTACH` varchar(500) DEFAULT '' COMMENT '会话附件：图片文档等',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 导出  表 shir-boot.chat_session 结构
CREATE TABLE IF NOT EXISTS `CHAT_SESSION` (
  `SESSION_ID` varchar(88) NOT NULL,
  `SESSION_NAME` varchar(150) NOT NULL,
  `USER_ID` bigint NOT NULL DEFAULT '0',
  `MODEL` varchar(50) NOT NULL COMMENT 'LLM Model',
  `MASK_ID` bigint DEFAULT '0',
  `MASK` varchar(255) DEFAULT NULL,
  `CREATE_TIME` timestamp NOT NULL,
  PRIMARY KEY (`SESSION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  shir-boot.chat_session 的数据：~4 rows (大约)
INSERT INTO `CHAT_SESSION` (`SESSION_ID`, `SESSION_NAME`, `USER_ID`, `MODEL`, `MASK_ID`, `MASK`, `CREATE_TIME`) VALUES
	('64eceeced58f268e685b6c2f41430674', 'ai assistant', 6, 'gpt-4o', 1, 'you are a ai assistant,answer my question', '2024-07-30 06:15:52'),
	('67075cdcdeb5301da8834a933dba1c1d', 'ai assistant', 1, 'gpt-4o', 1, 'you are a ai assistant,answer my question', '2024-05-24 10:09:07'),
	('b96da1a8276bf91ec80e62baa52ad539', '新的聊天', 6, 'gpt-4-1106-preview', 0, NULL, '2024-07-31 08:28:19'),
	('bbd2a8d9b9ad8c82d52fa9e521a06ad3', '论文写手', 1, 'gpt-3.5-turbo', 2, '你是一名论文写手', '2024-05-31 12:31:25');

-- 导出  表 shir-boot.dictionary 结构
CREATE TABLE IF NOT EXISTS `DICTIONARY` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `CODE` varchar(50) NOT NULL COMMENT 'dictionary code',
  `VALUE` varchar(200) NOT NULL COMMENT 'dictionary value',
  `TYPE` varchar(200) NOT NULL DEFAULT 'SYSTEM' COMMENT 'code type fill in system or custom',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  shir-boot.dictionary 的数据：~3 rows (大约)
-- 此处可以替换成你的openai api key，和代理地址（或者任意支持openai请求格式的API），也可以在后台管理页面修改
INSERT INTO `DICTIONARY` (`ID`, `NAME`, `CODE`, `VALUE`, `TYPE`) VALUES
	(1, 'OPENAI_API_KEY', 'OPENAI_API_KEY', 'sk-xxxxxxxx', 'SYSTEM'),
	(2, 'OPENAI_REQUEST_TIMEOUT', 'OPENAI_REQUEST_TIMEOUT', '30', 'SYSTEM'),
  (3, 'OPENAI_API_HOST', 'OPENAI_API_HOST', 'https://api.openai.com/v1', 'SYSTEM'),
	(4, 'test2', 'test2', 'test23', 'CUSTOM');

-- 导出  表 shir-boot.mask 结构
CREATE TABLE IF NOT EXISTS `MASK` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `MASK_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '人设名',
  `PROMPT` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '人设提示词',
  `SIZE` int DEFAULT NULL COMMENT '长度',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  shir-boot.mask 的数据：~18 rows (大约)
INSERT INTO `MASK` (`ID`, `MASK_NAME`, `PROMPT`, `SIZE`, `CREATE_TIME`) VALUES
	(1, 'ai assistant', 'you are a ai assistant,answer my question', 0, '2024-04-17 00:00:00'),
	(2, '论文写手', '你是一名论文写手', NULL, '2024-04-19 09:36:22'),

-- 导出  表 shir-boot.menu 结构
CREATE TABLE IF NOT EXISTS `MENU` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '菜单名称',
  `URL` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '跳转地址',
  `ICON` varchar(300) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '图标',
  `PERMISSION_CODES` varchar(150) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '标识权限码',
  `DESCRIPTION` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `LEFT_VALUE` int NOT NULL COMMENT '左节点值',
  `RIGHT_VALUE` int NOT NULL COMMENT '右节点值',
  `LEVEL` int NOT NULL COMMENT '节点等级',
  `PROPERTY` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '属性(自由使用标识)',
  `CREATE_DATE` datetime NOT NULL,
  `UPDATE_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 正在导出表  shir-boot.menu 的数据：~29 rows (大约)
INSERT INTO `MENU` (`ID`, `NAME`, `URL`, `ICON`, `PERMISSION_CODES`, `DESCRIPTION`, `LEFT_VALUE`, `RIGHT_VALUE`, `LEVEL`, `PROPERTY`, `CREATE_DATE`, `UPDATE_DATE`) VALUES
	(1, 'System management', '', 'el-icon-setting', NULL, '', 1, 36, 1, 'menu', '2023-11-01 16:15:56', '2023-11-01 16:47:06'),
	(2, 'User management', '/system/UserList', 'user', 'USER:LIST', NULL, 2, 11, 2, 'menu', '2023-11-01 16:25:57', '2023-11-01 16:43:54'),
	(3, 'Add user', NULL, NULL, 'USER:ADD', NULL, 3, 4, 3, 'button', '2023-11-01 16:28:18', NULL),
	(4, 'Update user', NULL, NULL, 'USER:UPDATE', NULL, 5, 6, 3, 'button', '2023-11-01 16:30:03', NULL),
	(5, 'Delete user', NULL, NULL, 'USER:DELETE', NULL, 7, 8, 3, 'button', '2023-11-01 16:30:25', NULL),
	(6, 'Reset password', NULL, NULL, 'USER:RESETPASSWORD', NULL, 9, 10, 3, 'button', '2023-11-01 16:31:25', NULL),
	(7, 'Role management', '/system/RoleList', 'people', 'ROLE:LIST', '', 12, 19, 2, 'menu', '2023-11-01 16:32:09', '2023-11-01 16:44:00'),
	(8, 'Add role', NULL, NULL, 'ROLE:ADD', NULL, 13, 14, 3, 'button', '2023-11-01 16:35:52', NULL),
	(9, 'Update role', NULL, NULL, 'ROLE:UPDATE', NULL, 15, 16, 3, 'button', '2023-11-01 16:36:12', NULL),
	(10, 'Delete role', NULL, NULL, 'ROLE:DELETE', NULL, 17, 18, 3, 'button', '2023-11-01 16:36:28', NULL),
	(11, 'Menu management', '/system/MenuList', 'component', NULL, NULL, 20, 27, 2, 'menu', '2023-11-01 16:39:02', '2023-11-01 16:44:08'),
	(12, 'Add menu', NULL, NULL, 'MENU:ADD', NULL, 21, 22, 3, 'button', '2023-11-01 16:39:17', NULL),
	(13, 'Update menu', NULL, NULL, 'MENU:UPDATE', NULL, 23, 24, 3, 'button', '2023-11-01 16:39:36', NULL),
	(14, 'Delete menu', NULL, NULL, 'MENU:DELETE', NULL, 25, 26, 3, 'button', '2023-11-01 16:39:56', NULL),
	(15, 'Development management', NULL, 'el-icon-cpu', NULL, '', 37, 42, 1, 'menu', '2023-12-06 10:47:43', '2023-12-06 11:39:41'),
	(16, 'Code generate', '/development/CodeGenerate', 'el-icon-s-tools', 'DEVELOPMENT:LIST', NULL, 38, 39, 2, 'menu', '2023-12-06 10:50:18', '2023-12-06 11:40:56'),
	(17, 'AI Chat', '/ai/ChatGPT', 'el-icon-chat-round', 'AICHAT:ALL', NULL, 44, 45, 2, 'menu', '2023-12-06 11:26:10', '2024-05-09 10:58:56'),
	(18, 'AI', NULL, 'el-icon-s-help', NULL, '', 43, 58, 1, 'menu', '2023-12-07 11:24:45', NULL),
	(19, 'Knowledge base', '/ai/KnowledgeBase', 'el-icon-notebook-1', NULL, NULL, 46, 47, 2, 'menu', '2023-12-07 11:26:56', '2024-04-17 10:12:00'),
	(20, 'GPT Model List', '/ai/GPTModelList', 'el-icon-notebook-2', NULL, NULL, 48, 49, 2, 'menu', '2024-02-27 15:26:02', '2024-04-17 10:12:06'),
	(21, 'Fine Tune', '/ai/FineTuneFiles', 'el-icon-files', NULL, NULL, 50, 51, 2, 'menu', '2024-02-27 15:30:44', '2024-04-17 10:12:10'),
	(22, 'Online SQL', NULL, 'el-icon-s-check', NULL, NULL, 40, 41, 2, 'MENU', '2024-04-09 18:18:35', '2024-04-17 10:13:27'),
	(23, 'Chat History', '/ai/ChatHistory', 'el-icon-document', 'CHATHISTORY:LIST', NULL, 52, 53, 2, 'menu', '2024-04-17 10:15:11', '2024-04-17 10:22:04'),
	(24, 'maskes', '/ai/Mask', 'el-icon-s-custom', 'MASK:LIST', NULL, 54, 55, 2, 'menu', '2024-04-17 10:54:29', NULL),
	(25, 'Dictionary management', '/system/Dictionary', 'el-icon-reading', 'DICTIONARY:LIST', NULL, 28, 35, 2, 'MENU', '2024-04-19 12:58:49', NULL),
	(26, 'Add dictionary', NULL, NULL, 'DICTIONARY:ADD', NULL, 29, 30, 3, 'button', '2024-04-19 12:59:28', NULL),
	(27, 'Update dictionary', NULL, NULL, 'DICTIONARY:UPDATE', NULL, 31, 32, 3, 'button', '2024-04-19 12:59:50', NULL),
	(28, 'Delete Dictionary', NULL, NULL, 'DICTIONARY:DELETE', NULL, 33, 34, 3, 'button', '2024-04-19 13:00:11', NULL),
	(29, 'Markdown', '/components-demo/markdown', NULL, NULL, NULL, 56, 57, 2, NULL, '2024-05-22 21:30:50', '2024-05-22 21:31:47');

-- 导出  表 shir-boot.role 结构
CREATE TABLE IF NOT EXISTS `ROLE` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `ROLE_NAME` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色名称',
  `DESCRIPTION` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `CREATE_DATE` datetime NOT NULL,
  `UPDATE_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 正在导出表  shir-boot.role 的数据：~2 rows (大约)
INSERT INTO `ROLE` (`ID`, `ROLE_NAME`, `DESCRIPTION`, `CREATE_DATE`, `UPDATE_DATE`) VALUES
	(5, 'admin', 'admin', '2023-11-02 14:50:06', '2024-04-19 13:00:26'),
	(6, 'user', 'user', '2023-11-02 15:29:37', '2024-05-09 10:37:23');

-- 导出  表 shir-boot.role_permission 结构
CREATE TABLE IF NOT EXISTS `ROLE_PERMISSION` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `ROLE_ID` bigint NOT NULL,
  `MENU_ID` bigint NOT NULL,
  `PERMISSION_CODES` varchar(150) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `ROLE_ID_INDEX` (`ROLE_ID`) USING BTREE,
  KEY `MENU_ID_INDEX` (`MENU_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=225 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 正在导出表  shir-boot.role_permission 的数据：~35 rows (大约)
INSERT INTO `ROLE_PERMISSION` (`ID`, `ROLE_ID`, `MENU_ID`, `PERMISSION_CODES`) VALUES
	(183, 5, 1, NULL),
	(184, 5, 2, 'USER:LIST'),
	(185, 5, 3, 'USER:ADD'),
	(186, 5, 4, 'USER:UPDATE'),
	(187, 5, 5, 'USER:DELETE'),
	(188, 5, 6, 'USER:RESETPASSWORD'),
	(189, 5, 7, 'ROLE:LIST'),
	(190, 5, 8, 'ROLE:ADD'),
	(191, 5, 9, 'ROLE:UPDATE'),
	(192, 5, 10, 'ROLE:DELETE'),
	(193, 5, 11, NULL),
	(194, 5, 12, 'MENU:ADD'),
	(195, 5, 13, 'MENU:UPDATE'),
	(196, 5, 14, 'MENU:DELETE'),
	(197, 5, 25, 'DICTIONARY:LIST'),
	(198, 5, 26, 'DICTIONARY:ADD'),
	(199, 5, 27, 'DICTIONARY:UPDATE'),
	(200, 5, 28, 'DICTIONARY:DELETE'),
	(201, 5, 15, NULL),
	(202, 5, 16, 'DEVELOPMENT:LIST'),
	(203, 5, 22, NULL),
	(204, 5, 18, NULL),
	(205, 5, 17, 'AICHAT:ALL'),
	(206, 5, 19, NULL),
	(207, 5, 20, NULL),
	(208, 5, 21, NULL),
	(209, 5, 23, 'CHATHISTORY:LIST'),
	(210, 5, 24, 'MASK:LIST'),
	(218, 6, 18, NULL),
	(219, 6, 17, 'AICHAT:ALL'),
	(220, 6, 19, NULL),
	(221, 6, 20, NULL),
	(222, 6, 21, NULL),
	(223, 6, 23, 'CHATHISTORY:LIST'),
	(224, 6, 24, 'MASK:LIST');

-- 导出  表 shir-boot.role_user 结构
CREATE TABLE IF NOT EXISTS `ROLE_USER` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `ROLE_ID` bigint NOT NULL,
  `USER_ID` varchar(150) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 正在导出表  shir-boot.role_user 的数据：~5 rows (大约)
INSERT INTO `ROLE_USER` (`ID`, `ROLE_ID`, `USER_ID`) VALUES
	(6, 6, '2'),
	(7, 5, '2'),
	(8, 5, '3'),
	(9, 5, '5'),
	(13, 6, '6');

-- 导出  表 shir-boot.system_log 结构
CREATE TABLE IF NOT EXISTS `SYSTEM_LOG` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `REQUEST_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求接口名称',
  `REQUEST_URI` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求路径',
  `REQUEST_BODY` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求参数体',
  `REQUEST_USER_ID` bigint DEFAULT NULL COMMENT '请求用户ID',
  `REQUEST_USER` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求用户名称',
  `REQUEST_IP` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求IP地址',
  `CREATE_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `REQUEST_ID` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求标识ID',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统日志';

-- 正在导出表  shir-boot.system_log 的数据：~0 rows (大约)

-- 导出  表 shir-boot.user 结构
CREATE TABLE IF NOT EXISTS `USER` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `PASSWORD` varchar(64) NOT NULL,
  `LOGIN_NAME` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登陆名',
  `NICK_NAME` varchar(100) NOT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `PHONE_NUMBER` varchar(50) DEFAULT NULL,
  `PROFILE` varchar(300) DEFAULT NULL COMMENT '用户头像',
  `CREATE_DATE` datetime NOT NULL,
  `UPDATE_DATE` datetime DEFAULT NULL,
  `STATUS` int NOT NULL DEFAULT '0' COMMENT '状态，0=正常，1=禁用',
  `REMARK` varchar(100) DEFAULT NULL,
  `IS_DELETE` int NOT NULL DEFAULT '0' COMMENT '删除状态，0=正常，1=删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- 正在导出表  shir-boot.user 的数据：~3 rows (大约)
INSERT INTO `USER` (`ID`, `PASSWORD`, `LOGIN_NAME`, `NICK_NAME`, `EMAIL`, `PHONE_NUMBER`, `PROFILE`, `CREATE_DATE`, `UPDATE_DATE`, `STATUS`, `REMARK`, `IS_DELETE`) VALUES
	(1, '7a57a5a743894a0e4a801fc3e19e9301f1abe6a46b8d3256cda0829a', 'admin', 'admin', '123', '123123', 'https://reiner.host/img/head.jpg', '2023-07-31 17:28:36', '2024-04-19 11:13:05', 0, NULL, 0),
	(5, 'bfa312155cb9449cc92cd8e8e19e9301f1abe6a46b8d3256cda0829a', 'monkeyKing', 'monkeyKing', '', '', 'https://tse4-mm.cn.bing.net/th/id/OIP-C.2vUTawLyzalDoTv7zF6JTQHaEo?pid=ImgDet&rs=1', '2023-11-03 15:47:49', NULL, 0, '', 0),
	(6, 'a4cb5697348e081f5753a33ce19e9301f1abe6a46b8d3256cda0829a', 'miyuki', 'miyuki', '', '', 'https://pic3.zhimg.com/50/v2-07ed9a142c3a8337f15c24e7a09b3d34_720w.jpg?source=1940ef5c', '2023-11-03 15:51:06', '2024-05-09 10:37:30', 0, '', 0);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
