/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

DROP TABLE IF EXISTS `gasto`;
DROP TABLE IF EXISTS `receita`;
DROP TABLE IF EXISTS `categoria`;
DROP TABLE IF EXISTS `usuario`;

CREATE TABLE `usuario` (
  `Id_Usuario` int NOT NULL AUTO_INCREMENT,
  `Nome` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Senha` varchar(255) NOT NULL,
  `Foto` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id_Usuario`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `categoria` (
  `Id_Categoria` int NOT NULL AUTO_INCREMENT,
  `Nome` varchar(50) NOT NULL,
  `Icone` varchar(100) DEFAULT NULL,
  `Id_Usuario` int DEFAULT NULL,
  PRIMARY KEY (`Id_Categoria`),
  KEY `Fk_Id_Usrio` (`Id_Usuario`),
  CONSTRAINT `Fk_Id_Usrio` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`Id_Usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `receita` (
  `Id_Receita` int NOT NULL AUTO_INCREMENT,
  `Valor` decimal(10,2) NOT NULL,
  `Descricao` varchar(100) DEFAULT NULL,
  `Metodo` varchar(50) DEFAULT NULL,
  `Data` date NOT NULL,
  `Id_Categoria` int NOT NULL,
  `Id_Usuario` int NOT NULL,
  PRIMARY KEY (`Id_Receita`),
  KEY `Fk_IdCategoria` (`Id_Categoria`),
  KEY `Fk_IdUsuario` (`Id_Usuario`),
  CONSTRAINT `Fk_IdCategoria` FOREIGN KEY (`Id_Categoria`) REFERENCES `categoria` (`Id_Categoria`),
  CONSTRAINT `Fk_IdUsuario` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`Id_Usuario`),
  CONSTRAINT `receita_chk_1` CHECK ((`Metodo` in ('Pix','Crédito','Débito','Dinheiro')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `gasto` (
  `Id_Gasto` int NOT NULL AUTO_INCREMENT,
  `Valor` decimal(10,2) NOT NULL,
  `Descricao` varchar(100) DEFAULT NULL,
  `Metodo` varchar(50) DEFAULT NULL,
  `Data` date NOT NULL,
  `Id_Categoria` int NOT NULL,
  `Id_Usuario` int NOT NULL,
  PRIMARY KEY (`Id_Gasto`),
  KEY `Fk_Id_Categoria` (`Id_Categoria`),
  KEY `Fk_Id_Usuario` (`Id_Usuario`),
  CONSTRAINT `Fk_Id_Categoria` FOREIGN KEY (`Id_Categoria`) REFERENCES `categoria` (`Id_Categoria`),
  CONSTRAINT `Fk_Id_Usuario` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`Id_Usuario`),
  CONSTRAINT `gasto_chk_1` CHECK ((`Metodo` in ('Pix','Crédito','Débito','Dinheiro')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Usuarios padrão
INSERT INTO `usuario` VALUES 
(1,'João Victor da Silva','joaovictor@email.com','123456',NULL),
(2,'Maria Souza','maria@email.com','123456',NULL),
(3,'Carlos Lima','carlos@email.com','123456',NULL);

-- Categorias padrão (Id_Usuario = NULL)
INSERT INTO `categoria` VALUES 
(1, 'Alimentação', 'restaurant', NULL),
(2, 'Transporte', 'directions-car', NULL),
(3, 'Assinaturas', 'subscriptions', NULL),
(4, 'Pessoal', 'person', NULL),
(5, 'Saúde', 'favorite', NULL),
(6, 'Moradia', 'home', NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;