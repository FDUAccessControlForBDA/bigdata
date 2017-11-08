CREATE DATABASE  IF NOT EXISTS `manageplatform` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `manageplatform`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: manageplatform
-- ------------------------------------------------------
-- Server version	5.6.37-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bd_user`
--

DROP TABLE IF EXISTS `bd_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bd_user` (
  `id` bigint(20) NOT NULL,
  `login_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `salt` varchar(255) DEFAULT NULL COMMENT '二维码',
  `roles` varchar(255) DEFAULT NULL COMMENT '角色',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `email` varchar(255) DEFAULT NULL COMMENT '电子邮箱',
  `mobile` varchar(65) DEFAULT NULL COMMENT '手机号码',
  `data_use` varchar(500) DEFAULT NULL COMMENT '使用情况',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `vm_ip` varchar(255) DEFAULT NULL COMMENT '虚拟机ip',
  `space_total` float DEFAULT NULL COMMENT '空间总大小',
  `space_use` float DEFAULT NULL COMMENT '已使用空间大小',
  `register_data` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `delete_flag` varchar(255) DEFAULT NULL COMMENT '删除标志位',
  `creator` varchar(255) DEFAULT NULL,
  `user_grade` int(11) DEFAULT NULL,
  `encode_password` varchar(255) DEFAULT NULL,
  `filepaths` text COMMENT '用户上传文件地址',
  `deadline` datetime DEFAULT NULL COMMENT '有效期',
  `public_key` text,
  `private_key` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bd_user`
--

LOCK TABLES `bd_user` WRITE;
/*!40000 ALTER TABLE `bd_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `bd_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bd_user_authority_list`
--

DROP TABLE IF EXISTS `bd_user_authority_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bd_user_authority_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `table_id` bigint(20) DEFAULT NULL COMMENT '表id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `type` int(11) DEFAULT NULL COMMENT '规则类型',
  `status` int(11) DEFAULT '0' COMMENT '导出状态(默认不可导出)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bd_user_authority_list`
--

LOCK TABLES `bd_user_authority_list` WRITE;
/*!40000 ALTER TABLE `bd_user_authority_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `bd_user_authority_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bd_user_table_relation`
--

DROP TABLE IF EXISTS `bd_user_table_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bd_user_table_relation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `table_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bd_user_table_relation`
--

LOCK TABLES `bd_user_table_relation` WRITE;
/*!40000 ALTER TABLE `bd_user_table_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `bd_user_table_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bd_vote_action`
--

DROP TABLE IF EXISTS `bd_vote_action`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bd_vote_action` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `table_id` bigint(20) DEFAULT NULL COMMENT '表id',
  `sponsor_id` bigint(20) DEFAULT NULL COMMENT '投票发起人id',
  `type` int(11) DEFAULT NULL COMMENT '投票类型',
  `status` bigint(20) DEFAULT NULL COMMENT '投票状态',
  `sponsor_time` datetime DEFAULT NULL COMMENT '发起投票时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bd_vote_action`
--

LOCK TABLES `bd_vote_action` WRITE;
/*!40000 ALTER TABLE `bd_vote_action` DISABLE KEYS */;
/*!40000 ALTER TABLE `bd_vote_action` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bd_vote_status`
--

DROP TABLE IF EXISTS `bd_vote_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bd_vote_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `voter_id` bigint(20) DEFAULT NULL COMMENT '投票人id',
  `action_id` bigint(20) DEFAULT NULL COMMENT '投票id',
  `vote_time` datetime DEFAULT NULL COMMENT '表决时间',
  `user_decision` int(11) DEFAULT NULL COMMENT '用户表决',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bd_vote_status`
--

LOCK TABLES `bd_vote_status` WRITE;
/*!40000 ALTER TABLE `bd_vote_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `bd_vote_status` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-06 13:38:24
