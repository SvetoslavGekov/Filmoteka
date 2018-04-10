CREATE DATABASE  IF NOT EXISTS `filmoteka` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `filmoteka`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: filmoteka
-- ------------------------------------------------------
-- Server version	5.7.21-log

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
-- Table structure for table `genres`
--

DROP TABLE IF EXISTS `genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `genres` (
  `genre_id` tinyint(4) NOT NULL AUTO_INCREMENT COMMENT 'Primary key - GENRE ID',
  `value` varchar(45) NOT NULL COMMENT 'Genre''s name',
  PRIMARY KEY (`genre_id`),
  UNIQUE KEY `values_unique` (`value`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genres`
--

LOCK TABLES `genres` WRITE;
/*!40000 ALTER TABLE `genres` DISABLE KEYS */;
INSERT INTO `genres` VALUES (1,'ACTION'),(2,'ADVENTURE'),(3,'ANIMATION'),(4,'BIOGRAPHY'),(5,'COMEDY'),(6,'CRIME'),(7,'DOCUMENTARY'),(8,'DRAMA'),(9,'FAMILY'),(10,'FANTASY'),(11,'HISTORY'),(12,'HORROR'),(13,'MUSIC'),(14,'MUSICAL'),(15,'MYSTERY'),(16,'ROMANCE'),(17,'SCIFI'),(18,'SPORT'),(19,'SUPERHERO'),(31,'tAKO'),(20,'THRILLER'),(21,'WAR'),(22,'WESTERN');
/*!40000 ALTER TABLE `genres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movies` (
  `product_id` int(11) NOT NULL,
  `director` varchar(80) DEFAULT NULL COMMENT 'Movie''s director',
  PRIMARY KEY (`product_id`),
  KEY `fk_MOVIE_Product1_idx` (`product_id`),
  CONSTRAINT `fk_MOVIE_Product1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movies`
--

LOCK TABLES `movies` WRITE;
/*!40000 ALTER TABLE `movies` DISABLE KEYS */;
INSERT INTO `movies` VALUES (1,'James Cameron'),(2,'James Cameron'),(3,'Joe Johnston');
/*!40000 ALTER TABLE `movies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_has_products`
--

DROP TABLE IF EXISTS `order_has_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_has_products` (
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `validity` date DEFAULT NULL,
  PRIMARY KEY (`order_id`,`product_id`),
  KEY `fk_order_has_products_PRODUCTS1_idx` (`product_id`),
  CONSTRAINT `fk_order_has_products_ORDERS1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_has_products_PRODUCTS1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_has_products`
--

LOCK TABLES `order_has_products` WRITE;
/*!40000 ALTER TABLE `order_has_products` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_has_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT 'Owner''s (user) ID',
  `date` date NOT NULL COMMENT 'Order''s date',
  `total_cost` decimal(7,2) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `fk_ORDER_User1_idx` (`user_id`),
  CONSTRAINT `fk_ORDER_User1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_has_genres`
--

DROP TABLE IF EXISTS `product_has_genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_has_genres` (
  `product_id` int(11) NOT NULL,
  `genre_id` tinyint(4) NOT NULL,
  PRIMARY KEY (`product_id`,`genre_id`),
  KEY `fk_PRODUCT_HAS_GENRE_GENRE1_idx` (`genre_id`),
  CONSTRAINT `GENRE_HAS_PRODUCT` FOREIGN KEY (`genre_id`) REFERENCES `genres` (`genre_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `PRODUCT_HAS_GENRE` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_has_genres`
--

LOCK TABLES `product_has_genres` WRITE;
/*!40000 ALTER TABLE `product_has_genres` DISABLE KEYS */;
INSERT INTO `product_has_genres` VALUES (1,1),(3,2),(4,3),(4,5),(6,6),(2,8),(5,8),(6,8),(3,9),(3,10),(6,15),(2,16),(5,16),(1,17);
/*!40000 ALTER TABLE `product_has_genres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_has_raters`
--

DROP TABLE IF EXISTS `product_has_raters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_has_raters` (
  `product_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `rating` decimal(3,1) NOT NULL,
  PRIMARY KEY (`product_id`,`user_id`),
  KEY `fk_PRODUCT_HAS_RATERS_User1_idx` (`user_id`),
  CONSTRAINT `fk_PRODUCT_HAS_RATERS_Product1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_PRODUCT_HAS_RATERS_User1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_has_raters`
--

LOCK TABLES `product_has_raters` WRITE;
/*!40000 ALTER TABLE `product_has_raters` DISABLE KEYS */;
INSERT INTO `product_has_raters` VALUES (1,1,3.1),(1,2,4.8),(4,1,5.5),(4,2,4.6),(6,3,3.2);
/*!40000 ALTER TABLE `product_has_raters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key за таблицата с продуктите (филми и сериали)',
  `name` varchar(80) NOT NULL COMMENT 'Product name',
  `release_year` year(4) NOT NULL COMMENT 'The year the product was released',
  `pg_rating` varchar(10) NOT NULL COMMENT 'PG rating',
  `duration` smallint(6) NOT NULL COMMENT 'Duration of the product in minutes',
  `rent_cost` decimal(5,2) NOT NULL COMMENT 'Rent cost in BGN',
  `buy_cost` decimal(5,2) NOT NULL COMMENT 'Buy cost in BGN',
  `description` varchar(1000) DEFAULT NULL COMMENT 'Description for a product. The length may be longer - most descriptions in the more popular web portals are around 200 symbols.',
  `poster` varchar(200) DEFAULT NULL COMMENT 'Path to the poster of the product.',
  `trailer` varchar(200) DEFAULT NULL COMMENT 'Path to the product''s trailer',
  `writers` varchar(200) DEFAULT NULL COMMENT 'Writers',
  `actors` varchar(1000) DEFAULT NULL COMMENT 'Actors',
  PRIMARY KEY (`product_id`),
  KEY `NAME_INDEX` (`name`),
  KEY `RELEASE_YEAR` (`release_year`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'The Terminator',1984,'R',107,5.00,10.00,'A seemingly indestructible humanoid cyborg is sent from 2029 to 1984 to assassinate a waitress, whose unborn son will lead humanity in a war against the machines, while a soldier from that war is sent to protect her at all costs.',NULL,NULL,'James Cameron, Gale Anne Hurd','Arnold Schwarzenegger, Linda Hamilton, Michael Biehn'),(2,'Titanic ',1997,'PG-13',194,10.00,15.00,'84 years later, a 100 year-old woman named Rose DeWitt Bukater tells the story to her granddaughter Lizzy Calvert, Brock Lovett, Lewis Bodine, Bobby Buell and Anatoly Mikailavich on the Keldysh about her life set in April 10th 1912, on a ship called Titanic when young Rose boards the departing ship with the upper-class passengers and her mother, Ruth DeWitt Bukater, and her fiancé, Caledon Hockley. Meanwhile, a drifter and artist named Jack Dawson and his best friend Fabrizio De Rossi win third-class tickets to the ship in a game. And she explains the whole story from departure until the death of Titanic on its first and last voyage April 15th, 1912 at 2:20 in the morning.',NULL,NULL,'James Cameron','Leonardo DiCaprio, Kate Winslet, Billy Zane'),(3,'Jumanji ',1995,'PG',104,5.00,7.50,'After being trapped in a jungle board game for 26 years, a Man-Child wins his release from the game. But, no sooner has he arrived that he is forced to play again, and this time sets the creatures of the jungle loose on the city. Now it is up to him to stop them.',NULL,NULL,'Jonathan Hensleigh, Greg Taylor','Robin Williams, Kirsten Dunst, Bonnie Hunt'),(4,'The Simpsons',1989,'-',22,3.00,5.00,'This is an animated sitcom about the antics of a dysfunctional family. Homer is the oafish unhealthy beer loving father, Marge is the hardworking homemaker wife, Bart is the perpetual ten-year-old underachiever (and proud of it), Lisa is the unappreciated eight-year-old genius, and Maggie is the cute, pacifier loving silent infant.',NULL,NULL,'James L. Brooks, Matt Groening, Sam Simon','Dan Castellaneta, Nancy Cartwright, Julie Kavner'),(5,'ER',1994,'-',44,3.00,5.00,'Michael Crichton has created a medical drama that chronicles life and death in a Chicago hospital emergency room. Each episode tells the tale of another day in the ER, from the exciting to the mundane, and the joyous to the heart-rending. Frenetic pacing, interwoven plot lines, and emotional rollercoastering is used to attempt to accurately depict the stressful environment found there. This show even portrays the plight of medical students in their quest to become physicians.',NULL,NULL,'Michael Crichton','Anthony Edwards, George Clooney, Julianna Margulies'),(6,'The Mentalist',2008,'-',43,3.00,5.00,'After a serial killer named Red John murdered Patrick Jane\'s wife and daughter, Jane dedicated his life to hunting down and killing Red John. To that end he gave up his lucrative pretense of being a psychic and joined the California Bureau of Investigation (CBI) as a consultant to the team responsible for investigating the Red John case, led by Senior Agent Teresa Lisbon. Using Jane\'s exceptional gift for observation and his mentalist tric able to close an unprecedented number of cases, but Jane\'s unconventional and often outright illegal methods also bring much censure down on Lisbon\'s head, making his assistance both a blessing and a curse. Meanwhile, the hunt for Red John continues...',NULL,NULL,'Bruno Heller','Simon Baker, Robin Tunney, Tim Kang');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tvseries`
--

DROP TABLE IF EXISTS `tvseries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tvseries` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `season` tinyint(4) NOT NULL,
  `finished_airing` date DEFAULT NULL COMMENT 'TVSeries'' date of last episode airing',
  PRIMARY KEY (`product_id`),
  KEY `fk_tvseries_products_idx` (`product_id`),
  CONSTRAINT `fk_tvseries_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tvseries`
--

LOCK TABLES `tvseries` WRITE;
/*!40000 ALTER TABLE `tvseries` DISABLE KEYS */;
INSERT INTO `tvseries` VALUES (4,1,NULL),(5,1,NULL),(6,1,NULL);
/*!40000 ALTER TABLE `tvseries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_favorite_products`
--

DROP TABLE IF EXISTS `user_has_favorite_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_has_favorite_products` (
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`product_id`),
  KEY `fk_USER_HAS_FAVORITE_PRODUCTS_Product1_idx` (`product_id`),
  CONSTRAINT `fk_USER_HAS_FAVORITE_PRODUCTS_Product1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_USER_HAS_FAVORITE_PRODUCTS_User1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_favorite_products`
--

LOCK TABLES `user_has_favorite_products` WRITE;
/*!40000 ALTER TABLE `user_has_favorite_products` DISABLE KEYS */;
INSERT INTO `user_has_favorite_products` VALUES (1,2),(2,2),(3,2);
/*!40000 ALTER TABLE `user_has_favorite_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_products`
--

DROP TABLE IF EXISTS `user_has_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_has_products` (
  `product_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `validity` date DEFAULT NULL COMMENT 'Validity for a product - null if bought and a date if rented',
  PRIMARY KEY (`product_id`,`user_id`),
  KEY `fk_USER_HAS_PRODUCT_User1_idx` (`user_id`),
  CONSTRAINT `fk_USER_HAS_PRODUCT_Product1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_USER_HAS_PRODUCT_User1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_products`
--

LOCK TABLES `user_has_products` WRITE;
/*!40000 ALTER TABLE `user_has_products` DISABLE KEYS */;
INSERT INTO `user_has_products` VALUES (1,1,NULL),(1,3,NULL),(2,2,NULL),(2,3,NULL),(3,2,NULL),(3,3,NULL),(4,1,NULL),(4,3,NULL),(5,2,NULL),(5,3,NULL),(6,3,NULL);
/*!40000 ALTER TABLE `user_has_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_watchlist_products`
--

DROP TABLE IF EXISTS `user_has_watchlist_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_has_watchlist_products` (
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`product_id`),
  KEY `fk_USER_HAS_WATCHLIST_PRODUCTS_Product1_idx` (`product_id`),
  CONSTRAINT `fk_USER_HAS_WATCHLIST_PRODUCTS_Product1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_USER_HAS_WATCHLIST_PRODUCTS_User1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_watchlist_products`
--

LOCK TABLES `user_has_watchlist_products` WRITE;
/*!40000 ALTER TABLE `user_has_watchlist_products` DISABLE KEYS */;
INSERT INTO `user_has_watchlist_products` VALUES (2,1),(3,1);
/*!40000 ALTER TABLE `user_has_watchlist_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_types`
--

DROP TABLE IF EXISTS `user_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_types` (
  `user_type_id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `value` varchar(45) NOT NULL,
  PRIMARY KEY (`user_type_id`),
  UNIQUE KEY `value_UNIQUE` (`value`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_types`
--

LOCK TABLES `user_types` WRITE;
/*!40000 ALTER TABLE `user_types` DISABLE KEYS */;
INSERT INTO `user_types` VALUES (1,'ADMIN'),(2,'CONSUMER');
/*!40000 ALTER TABLE `user_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key - arbitraty user ID',
  `user_type_id` tinyint(4) NOT NULL,
  `username` varchar(45) NOT NULL COMMENT 'User''s username',
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL COMMENT 'User''s password',
  `first_name` varchar(45) NOT NULL COMMENT 'User''s first name',
  `last_name` varchar(45) NOT NULL,
  `registration_date` date NOT NULL COMMENT 'Date of user''s registration',
  `phone` varchar(45) DEFAULT NULL COMMENT 'User''s phone number',
  `last_login` timestamp NULL DEFAULT NULL COMMENT 'User''s last login time',
  `profile_picture` varchar(200) DEFAULT NULL COMMENT 'Path to user''s profile picture',
  `money` decimal(7,2) DEFAULT '0.00' COMMENT 'User''s money',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `USERNAME_UNIQUE` (`username`),
  UNIQUE KEY `EMAIL_UNIQUE` (`email`),
  KEY `USERNAME_INDEX` (`username`),
  KEY `EMAIL_INDEX` (`email`),
  KEY `fk_User_USER_TYPE1_idx` (`user_type_id`),
  CONSTRAINT `fk_User_USER_TYPE1` FOREIGN KEY (`user_type_id`) REFERENCES `user_types` (`user_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,2,'sgekov','svetoslav_gekov@abv.bg','Sgekov123','Svetoslav','Gekov','2018-04-01',NULL,NULL,NULL,0.00),(2,2,'mdimitrov','mario0.bg@abv.bg','Mdimitrov123','Mario','Dimitrov','2018-04-01',NULL,NULL,NULL,0.00),(3,1,'admin','admin@filmoteka.bg','Admin123','Admin','Adminov','2018-04-01',NULL,NULL,NULL,0.00);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-07 14:58:30
