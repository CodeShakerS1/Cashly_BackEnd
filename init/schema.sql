-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: cashly
-- ------------------------------------------------------
-- Server version	9.6.0

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '2fd839d9-133b-11f1-886d-bdb34951bec3:1-33';

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `Id_Categoria` int NOT NULL AUTO_INCREMENT,
  `Nome` varchar(50) NOT NULL,
  `Icone` varchar(100) DEFAULT NULL,
  `Tipo` varchar(15) DEFAULT NULL,
  `Id_Usuario` int DEFAULT NULL,
  PRIMARY KEY (`Id_Categoria`),
  KEY `Fk_Id_Usrio` (`Id_Usuario`),
  CONSTRAINT `Fk_Id_Usrio` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`Id_Usuario`),
  CONSTRAINT `categoria_chk_1` CHECK ((`Tipo` in (_utf8mb4'receita',_utf8mb4'despesa')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conta`
--

DROP TABLE IF EXISTS `conta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conta` (
  `Id_Conta` int NOT NULL AUTO_INCREMENT,
  `Nome` varchar(100) NOT NULL,
  `Tipo` varchar(15) NOT NULL,
  `Saldo_Inicial` decimal(10,2) NOT NULL DEFAULT '0.00',
  `Saldo_Atual` decimal(10,2) NOT NULL DEFAULT '0.00',
  `Id_Usuario` int DEFAULT NULL,
  PRIMARY KEY (`Id_Conta`),
  KEY `Fk_Id_Usuario` (`Id_Usuario`),
  CONSTRAINT `Fk_Id_Usuario` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`Id_Usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conta`
--

LOCK TABLES `conta` WRITE;
/*!40000 ALTER TABLE `conta` DISABLE KEYS */;
/*!40000 ALTER TABLE `conta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orcamento`
--

DROP TABLE IF EXISTS `orcamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orcamento` (
  `Id_Orcamento` int NOT NULL AUTO_INCREMENT,
  `Valor_Limite` decimal(10,2) NOT NULL,
  `Mes_Referencia` date NOT NULL,
  `Id_Categoria` int DEFAULT NULL,
  PRIMARY KEY (`Id_Orcamento`),
  KEY `Fk_Categoria` (`Id_Categoria`),
  CONSTRAINT `Fk_Categoria` FOREIGN KEY (`Id_Categoria`) REFERENCES `categoria` (`Id_Categoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orcamento`
--

LOCK TABLES `orcamento` WRITE;
/*!40000 ALTER TABLE `orcamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `orcamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transacao`
--

DROP TABLE IF EXISTS `transacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transacao` (
  `Id_Transacao` int NOT NULL AUTO_INCREMENT,
  `Valor` decimal(10,2) NOT NULL,
  `Data_Transacao` timestamp NULL DEFAULT NULL,
  `Descricao` varchar(100) NOT NULL DEFAULT 'Não Informado',
  `Tipo` varchar(15) DEFAULT NULL,
  `Status_Transferencia` tinyint(1) DEFAULT NULL,
  `Id_Categoria` int DEFAULT NULL,
  `Id_Conta` int DEFAULT NULL,
  `Id_Usuario` int DEFAULT NULL,
  `Recorrente` tinyint(1) DEFAULT '0',
  `Deletado` timestamp NULL DEFAULT NULL,
  `Proxima_Cobranca` date DEFAULT NULL,
  PRIMARY KEY (`Id_Transacao`),
  KEY `Fk_Id_Categoria` (`Id_Categoria`),
  KEY `Fk_Id_Conta` (`Id_Conta`),
  KEY `idx_usuario_transacao_data` (`Id_Usuario`,`Data_Transacao`,`Tipo`),
  CONSTRAINT `Fk_Id_Categoria` FOREIGN KEY (`Id_Categoria`) REFERENCES `categoria` (`Id_Categoria`),
  CONSTRAINT `Fk_Id_Conta` FOREIGN KEY (`Id_Conta`) REFERENCES `conta` (`Id_Conta`),
  CONSTRAINT `Fk_Usuario` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`Id_Usuario`),
  CONSTRAINT `transacao_chk_1` CHECK ((`Tipo` in (_utf8mb4'receita',_utf8mb4'despesa',_utf8mb4'transferência')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transacao`
--

LOCK TABLES `transacao` WRITE;
/*!40000 ALTER TABLE `transacao` DISABLE KEYS */;
/*!40000 ALTER TABLE `transacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `Id_Usuario` int NOT NULL AUTO_INCREMENT,
  `Nome` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Senha` varchar(255) NOT NULL,
  `Moeda_Padrao` varchar(10) DEFAULT 'BRL',
  PRIMARY KEY (`Id_Usuario`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-16 15:22:09