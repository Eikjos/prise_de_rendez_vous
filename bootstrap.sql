-- MySQL dump 10.13  Distrib 8.0.29, for Linux (x86_64)
--
-- Host: localhost    Database: prdv_projet
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `prdv_projet`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `prdv_projet` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `prdv_projet`;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(64) NOT NULL,
  `first_name` varchar(32) NOT NULL,
  `last_name` varchar(32) NOT NULL,
  `password` binary(60) NOT NULL,
  `type` int NOT NULL,
  `username` char(8) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_q0uja26qgu1atulenwup9rxyr` (`email`),
  UNIQUE KEY `UK_gex1lmaqpg0ir5g1f5eftyaa1` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'admin@univ-rouen.fr','Admin','ADMIN',_binary '$2y$10$yOMHPbMeN3bZkn8gxc4KkezSNFYHupu4lBK5CkYJfHW6hvUR0GilS',2,'adminacc');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` longtext,
  `location` varchar(64) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `author_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_mt8ulcc4k7fnc56rxaeu1sa33` (`name`),
  KEY `FK114vmf8at3e4x7vy259ugudco` (`author_id`),
  CONSTRAINT `FK114vmf8at3e4x7vy259ugudco` FOREIGN KEY (`author_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_oy92ak6u4rmbq75jgb14npht7` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group`
--

LOCK TABLES `group` WRITE;
/*!40000 ALTER TABLE `group` DISABLE KEYS */;
/*!40000 ALTER TABLE `group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_accounts`
--

DROP TABLE IF EXISTS `group_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_accounts` (
  `groups_id` bigint NOT NULL,
  `accounts_id` bigint NOT NULL,
  PRIMARY KEY (`groups_id`,`accounts_id`),
  KEY `FKk4k3w9jpo8phfbyotd1j09rny` (`accounts_id`),
  CONSTRAINT `FKk4k3w9jpo8phfbyotd1j09rny` FOREIGN KEY (`accounts_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKqjwyio2mmdkk2vot59q7dpi45` FOREIGN KEY (`groups_id`) REFERENCES `group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_accounts`
--

LOCK TABLES `group_accounts` WRITE;
/*!40000 ALTER TABLE `group_accounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_events`
--

DROP TABLE IF EXISTS `group_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_events` (
  `groups_id` bigint NOT NULL,
  `events_id` bigint NOT NULL,
  PRIMARY KEY (`groups_id`,`events_id`),
  KEY `FKg7edbndjyhq77sdcrr4vljw75` (`events_id`),
  CONSTRAINT `FKg7edbndjyhq77sdcrr4vljw75` FOREIGN KEY (`events_id`) REFERENCES `event` (`id`),
  CONSTRAINT `FKs68beg467q0wj1sp630sgxr20` FOREIGN KEY (`groups_id`) REFERENCES `group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_events`
--

LOCK TABLES `group_events` WRITE;
/*!40000 ALTER TABLE `group_events` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `slot`
--

DROP TABLE IF EXISTS `slot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `slot` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` longtext,
  `start` datetime(6) NOT NULL,
  `booker_id` bigint DEFAULT NULL,
  `event_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK812cpcwkxgcxiiegx0m5jin1p` (`booker_id`),
  KEY `FKrm2krw9sl1o3o7v75g50eqnpm` (`event_id`),
  CONSTRAINT `FK812cpcwkxgcxiiegx0m5jin1p` FOREIGN KEY (`booker_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKrm2krw9sl1o3o7v75g50eqnpm` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slot`
--

LOCK TABLES `slot` WRITE;
/*!40000 ALTER TABLE `slot` DISABLE KEYS */;
/*!40000 ALTER TABLE `slot` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-05-26 11:52:11
