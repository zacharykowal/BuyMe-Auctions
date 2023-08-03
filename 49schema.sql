CREATE DATABASE  IF NOT EXISTS `buyme` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `buyme`;
-- MySQL dump 10.13  Distrib 8.0.31, for macos12 (x86_64)
--
-- Host: localhost    Database: buyme
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Admin`
--

DROP TABLE IF EXISTS `Admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Admin` (
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`username`),
  CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`username`) REFERENCES `Users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Admin`
--

LOCK TABLES `Admin` WRITE;
/*!40000 ALTER TABLE `Admin` DISABLE KEYS */;
INSERT INTO `Admin` VALUES ('zach123');
/*!40000 ALTER TABLE `Admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alert`
--

DROP TABLE IF EXISTS `alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alert` (
  `alert_id` int NOT NULL AUTO_INCREMENT,
  `text` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`alert_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert`
--

LOCK TABLES `alert` WRITE;
/*!40000 ALTER TABLE `alert` DISABLE KEYS */;
INSERT INTO `alert` VALUES (1,'you won the auction'),(2,'an item you were waiting for is now available'),(3,'you won an auction'),(11,'you won an auction'),(12,'an item you were waiting for is now available'),(13,'an item you were waiting for is now available'),(14,'higher bid placed on auction you are bidding on'),(15,'bid placed on auction higher than your limit'),(16,'bid placed on auction higher than your limit'),(17,'bid placed on auction higher than your limit'),(18,'bid placed on auction higher than your limit'),(19,'higher bid placed on auction you are bidding on'),(20,'an item you were waiting for is now available'),(21,'an item you were waiting for is now available'),(22,'higher bid placed on auction you are bidding on'),(23,'you won an auction'),(24,'an item you were waiting for is now available'),(25,'an item you were waiting for is now available'),(26,'you won an auction'),(27,'an item you were waiting for is now available'),(28,'bid placed on auction higher than your limit'),(29,'bid placed on auction higher than your limit'),(30,'higher bid placed on auction you are bidding on');
/*!40000 ALTER TABLE `alert` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Answers`
--

DROP TABLE IF EXISTS `Answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Answers` (
  `username` varchar(50) NOT NULL,
  `question_id` int NOT NULL,
  PRIMARY KEY (`username`,`question_id`),
  KEY `question_id` (`question_id`),
  CONSTRAINT `answers_ibfk_1` FOREIGN KEY (`username`) REFERENCES `customerrep` (`username`),
  CONSTRAINT `answers_ibfk_2` FOREIGN KEY (`question_id`) REFERENCES `Question` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Answers`
--

LOCK TABLES `Answers` WRITE;
/*!40000 ALTER TABLE `Answers` DISABLE KEYS */;
INSERT INTO `Answers` VALUES ('john456',1),('john456',2),('rob625',3),('rob625',4),('john456',8),('john456',9);
/*!40000 ALTER TABLE `Answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Asks`
--

DROP TABLE IF EXISTS `Asks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Asks` (
  `username` varchar(50) NOT NULL,
  `question_id` int NOT NULL,
  PRIMARY KEY (`username`,`question_id`),
  KEY `question_id` (`question_id`),
  CONSTRAINT `asks_ibfk_1` FOREIGN KEY (`username`) REFERENCES `EndUser` (`username`),
  CONSTRAINT `asks_ibfk_2` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Asks`
--

LOCK TABLES `Asks` WRITE;
/*!40000 ALTER TABLE `Asks` DISABLE KEYS */;
INSERT INTO `Asks` VALUES ('dan789',1),('dan789',2),('mike27',3),('mike27',4),('mike27',8),('dan789',10),('dan789',11);
/*!40000 ALTER TABLE `Asks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auction`
--

DROP TABLE IF EXISTS `auction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auction` (
  `auction_id` int NOT NULL AUTO_INCREMENT,
  `end_date` date NOT NULL,
  `min_price` float NOT NULL,
  `winner` varchar(50) DEFAULT NULL,
  `status` varchar(10) NOT NULL,
  `increment` float NOT NULL,
  `initial` float NOT NULL,
  `winning_bid` float DEFAULT NULL,
  `current_bid` float DEFAULT NULL,
  `current_bid_user` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auction`
--

LOCK TABLES `auction` WRITE;
/*!40000 ALTER TABLE `auction` DISABLE KEYS */;
INSERT INTO `auction` VALUES (1,'2022-12-22',50,'jess19','closed',5,20,60,60,'jess19'),(2,'2023-01-20',100,'jess19','closed',10,70,120,120,'jess19'),(3,'2023-02-25',90,'joe90','closed',10,80,150,150,'joe90'),(4,'2023-03-14',20,NULL,'open',5,10,NULL,10,NULL),(7,'2023-01-16',40,NULL,'open',10,20,NULL,30,NULL),(9,'2022-12-15',500,'dan789','closed',100,300,500,500,'dan789'),(10,'2022-12-14',600,'mike27','closed',100,400,700,700,'mike27'),(11,'2022-12-15',1000,'joe90','closed',50,800,1050,1050,'joe90'),(12,'2022-12-15',1050,'jess19','closed',50,850,1100,1100,'jess19'),(13,'2022-12-15',60,'no winner','closed',1,50,NULL,55,'mike27'),(14,'2023-06-22',75,NULL,'open',5,50,NULL,NULL,NULL),(15,'2022-12-29',36,NULL,'open',1,21,NULL,37,'jess19'),(16,'2022-12-30',10,NULL,'open',1,5,NULL,17,'jess19'),(21,'2022-12-17',10,'dan789','closed',1,5,11,11,'dan789'),(22,'2022-12-17',25,'mike27','closed',1,15,26,26,'mike27'),(24,'2022-12-17',501,'no winner','closed',1,400,NULL,405,'mike27'),(25,'2022-12-28',67,NULL,'open',1,22,NULL,35,'jess19');
/*!40000 ALTER TABLE `auction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bid`
--

DROP TABLE IF EXISTS `bid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bid` (
  `bid_id` int NOT NULL AUTO_INCREMENT,
  `amount` float NOT NULL,
  `upper_limit` float DEFAULT NULL,
  PRIMARY KEY (`bid_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bid`
--

LOCK TABLES `bid` WRITE;
/*!40000 ALTER TABLE `bid` DISABLE KEYS */;
INSERT INTO `bid` VALUES (1,25,40),(2,300,350),(3,70,100),(11,500,1000),(12,700,1000),(13,1050,1200),(14,1100,2000),(15,55,59),(16,22,30),(17,6,8),(21,6,15),(22,11,20),(23,26,30),(24,405,500),(25,23,30),(26,31,40),(27,35,37);
/*!40000 ALTER TABLE `bid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clothingitem`
--

DROP TABLE IF EXISTS `clothingitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clothingitem` (
  `item_id` int NOT NULL AUTO_INCREMENT,
  `brand` varchar(50) DEFAULT NULL,
  `subcategory` varchar(10) NOT NULL,
  `color` varchar(20) NOT NULL,
  `size` varchar(5) NOT NULL,
  `arm_length` varchar(5) NOT NULL,
  `inseam` varchar(5) NOT NULL,
  `style` varchar(15) NOT NULL,
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clothingitem`
--

LOCK TABLES `clothingitem` WRITE;
/*!40000 ALTER TABLE `clothingitem` DISABLE KEYS */;
INSERT INTO `clothingitem` VALUES (1,'nike','shirt','black','L','5','n/a','n/a'),(2,'nike','pants','gray','M','n/a','30','n/a'),(3,'adidas','shirt','blue','L','6','n/a','n/a'),(4,'newera','hat','black','7','n/a','n/a','fitted'),(5,'reebok','shirt','red','S','4','n/a','n/a'),(6,'reebok','pants','gray','L','n/a','33','n/a'),(7,'nike','hat','black','7','n/a','n/a','fitted');
/*!40000 ALTER TABLE `clothingitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CustomerRep`
--

DROP TABLE IF EXISTS `CustomerRep`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CustomerRep` (
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`username`),
  CONSTRAINT `customerrep_ibfk_1` FOREIGN KEY (`username`) REFERENCES `Users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CustomerRep`
--

LOCK TABLES `CustomerRep` WRITE;
/*!40000 ALTER TABLE `CustomerRep` DISABLE KEYS */;
INSERT INTO `CustomerRep` VALUES ('john456'),('john92'),('rob625');
/*!40000 ALTER TABLE `CustomerRep` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EndUser`
--

DROP TABLE IF EXISTS `EndUser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EndUser` (
  `username` varchar(50) NOT NULL,
  `isbuyer` tinyint(1) NOT NULL,
  `isseller` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`),
  CONSTRAINT `enduser_ibfk_1` FOREIGN KEY (`username`) REFERENCES `Users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EndUser`
--

LOCK TABLES `EndUser` WRITE;
/*!40000 ALTER TABLE `EndUser` DISABLE KEYS */;
INSERT INTO `EndUser` VALUES ('dan789',0,0),('jess19',1,0),('joe90',1,1),('mike27',0,1),('nick25',1,1);
/*!40000 ALTER TABLE `EndUser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `has`
--

DROP TABLE IF EXISTS `has`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `has` (
  `bid_id` int NOT NULL,
  `auction_id` int NOT NULL,
  PRIMARY KEY (`bid_id`,`auction_id`),
  KEY `auction_id` (`auction_id`),
  CONSTRAINT `has_ibfk_1` FOREIGN KEY (`bid_id`) REFERENCES `bid` (`bid_id`) ON DELETE CASCADE,
  CONSTRAINT `has_ibfk_2` FOREIGN KEY (`auction_id`) REFERENCES `auction` (`auction_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `has`
--

LOCK TABLES `has` WRITE;
/*!40000 ALTER TABLE `has` DISABLE KEYS */;
INSERT INTO `has` VALUES (3,1),(1,2),(2,2),(11,9),(12,10),(13,11),(14,12),(15,13),(16,15),(17,16),(21,21),(22,21),(23,22),(24,24),(25,25),(26,25),(27,25);
/*!40000 ALTER TABLE `has` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hosts`
--

DROP TABLE IF EXISTS `hosts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hosts` (
  `auction_id` int NOT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`auction_id`,`username`),
  KEY `username` (`username`),
  CONSTRAINT `hosts_ibfk_1` FOREIGN KEY (`auction_id`) REFERENCES `auction` (`auction_id`) ON DELETE CASCADE,
  CONSTRAINT `hosts_ibfk_2` FOREIGN KEY (`username`) REFERENCES `enduser` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hosts`
--

LOCK TABLES `hosts` WRITE;
/*!40000 ALTER TABLE `hosts` DISABLE KEYS */;
INSERT INTO `hosts` VALUES (2,'dan789'),(22,'dan789'),(24,'dan789'),(7,'joe90'),(14,'joe90'),(1,'mike27'),(3,'mike27'),(15,'mike27'),(16,'mike27'),(21,'mike27'),(25,'mike27');
/*!40000 ALTER TABLE `hosts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `places`
--

DROP TABLE IF EXISTS `places`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `places` (
  `bid_id` int NOT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`bid_id`,`username`),
  KEY `username` (`username`),
  CONSTRAINT `places_ibfk_1` FOREIGN KEY (`bid_id`) REFERENCES `bid` (`bid_id`) ON DELETE CASCADE,
  CONSTRAINT `places_ibfk_2` FOREIGN KEY (`username`) REFERENCES `enduser` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `places`
--

LOCK TABLES `places` WRITE;
/*!40000 ALTER TABLE `places` DISABLE KEYS */;
INSERT INTO `places` VALUES (1,'dan789'),(3,'dan789'),(11,'dan789'),(21,'dan789'),(22,'dan789'),(14,'jess19'),(25,'jess19'),(27,'jess19'),(13,'joe90'),(16,'joe90'),(17,'joe90'),(2,'mike27'),(12,'mike27'),(15,'mike27'),(23,'mike27'),(24,'mike27'),(26,'nick25');
/*!40000 ALTER TABLE `places` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `question_id` int NOT NULL AUTO_INCREMENT,
  `question` varchar(200) NOT NULL,
  `answer` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`question_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,'how do i place a bid','click bid'),(2,'how do i set up an auction','click set up auction'),(3,'how do i pay for an item','click pay for item'),(4,'dummy question','dummy answer'),(8,'another dummy question','another dummy answer'),(9,'test question','test answer'),(10,'ask question',NULL),(11,'another question',NULL);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sells`
--

DROP TABLE IF EXISTS `sells`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sells` (
  `auction_id` int NOT NULL,
  `item_id` int NOT NULL,
  PRIMARY KEY (`auction_id`,`item_id`),
  KEY `item_id` (`item_id`),
  CONSTRAINT `auction_id` FOREIGN KEY (`auction_id`) REFERENCES `auction` (`auction_id`) ON DELETE CASCADE,
  CONSTRAINT `sells_ibfk_1` FOREIGN KEY (`auction_id`) REFERENCES `auction` (`auction_id`),
  CONSTRAINT `sells_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `clothingitem` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sells`
--

LOCK TABLES `sells` WRITE;
/*!40000 ALTER TABLE `sells` DISABLE KEYS */;
INSERT INTO `sells` VALUES (2,1),(4,1),(22,1),(1,2),(2,2),(7,2),(14,2),(24,2),(1,3),(3,3),(2,4),(21,4),(25,4),(16,5),(15,6);
/*!40000 ALTER TABLE `sells` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sentto`
--

DROP TABLE IF EXISTS `sentto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sentto` (
  `alert_id` int NOT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`alert_id`,`username`),
  KEY `username` (`username`),
  CONSTRAINT `sentto_ibfk_1` FOREIGN KEY (`alert_id`) REFERENCES `alert` (`alert_id`),
  CONSTRAINT `sentto_ibfk_2` FOREIGN KEY (`username`) REFERENCES `enduser` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sentto`
--

LOCK TABLES `sentto` WRITE;
/*!40000 ALTER TABLE `sentto` DISABLE KEYS */;
INSERT INTO `sentto` VALUES (2,'dan789'),(12,'dan789'),(20,'dan789'),(21,'dan789'),(22,'dan789'),(23,'dan789'),(24,'dan789'),(27,'dan789'),(11,'jess19'),(13,'jess19'),(16,'jess19'),(18,'jess19'),(25,'jess19'),(28,'jess19'),(29,'jess19'),(14,'joe90'),(15,'joe90'),(17,'joe90'),(19,'joe90'),(1,'mike27'),(3,'mike27'),(26,'mike27'),(30,'nick25');
/*!40000 ALTER TABLE `sentto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `setsalertfor`
--

DROP TABLE IF EXISTS `setsalertfor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `setsalertfor` (
  `username` varchar(50) NOT NULL,
  `item_id` int NOT NULL,
  PRIMARY KEY (`username`,`item_id`),
  KEY `item_id` (`item_id`),
  CONSTRAINT `setsalertfor_ibfk_1` FOREIGN KEY (`username`) REFERENCES `enduser` (`username`),
  CONSTRAINT `setsalertfor_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `clothingitem` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `setsalertfor`
--

LOCK TABLES `setsalertfor` WRITE;
/*!40000 ALTER TABLE `setsalertfor` DISABLE KEYS */;
INSERT INTO `setsalertfor` VALUES ('dan789',2),('jess19',2),('dan789',4);
/*!40000 ALTER TABLE `setsalertfor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Users` (
  `username` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `address` varchar(50) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES ('dan789','dan@yahoo.com','pass7890','34 Maple Road'),('jess19','jess@msn.com','passjess','78 Templar Rd'),('joe90','joe@aol.com','passjoe','5 Union Hill Road'),('john456','john@gmail.com','pass456','456 Broad Street'),('john92','john@yahoo.com','password','456 Main Street'),('mike27','mike@yahoo.com','abc39','62 Oak Lane'),('nick25','nick@gmail.com','pass25','25 Main Street'),('rob625','rob@hotmail.com','000','6 Madison Street'),('zach123','zach@gmail.com','pass123','123 Main Street');
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-17 15:03:58
