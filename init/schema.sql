-- CREATE DATABASE IF NOT EXISTS cashly;
-- USE cashly;

-- 1. Tabela de Usuários
CREATE TABLE IF NOT EXISTS `users` (
  `userid` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `userpassword` varchar(100) NOT NULL,
  `photo` LONGTEXT DEFAULT NULL,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 2. Tabela de Categorias
CREATE TABLE IF NOT EXISTS `category` (
  `categoryid` int NOT NULL AUTO_INCREMENT,
  `categoryname` varchar(50) NOT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `limit_amount` decimal(10,2) DEFAULT NULL,
  `userid` int DEFAULT NULL,
  PRIMARY KEY (`categoryid`),
  KEY `Fk_user_id` (`userid`),
  CONSTRAINT `Fk_category_user` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 3. Tabela de Receitas (Income)
CREATE TABLE IF NOT EXISTS `income` (
  `incomeid` int NOT NULL AUTO_INCREMENT,
  `incomename` varchar(100) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `method` varchar(50) NOT NULL,
  `incomedate` date NOT NULL,
  `userid` int DEFAULT NULL,
  PRIMARY KEY (`incomeid`),
  KEY `Fk_userid` (`userid`),
  CONSTRAINT `Fk_userid_income` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`),
  CONSTRAINT `income_chk_method` CHECK ((`method` in ('Pix','Credit','Debit','Cash')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 4. Tabela de Despesas (Expense)
CREATE TABLE IF NOT EXISTS `expense` (
  `expenseid` int NOT NULL AUTO_INCREMENT,
  `expensename` varchar(100) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `method` varchar(50) NOT NULL,
  `expensedate` date NOT NULL,
  `userid` int DEFAULT NULL,
  `categoryid` int DEFAULT NULL,
  PRIMARY KEY (`expenseid`),
  KEY `fk_user` (`userid`),
  KEY `Fk_category` (`categoryid`),
  CONSTRAINT `Fk_category_expense` FOREIGN KEY (`categoryid`) REFERENCES `category` (`categoryid`),
  CONSTRAINT `fk_user_expense` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`),
  CONSTRAINT `expense_chk_method` CHECK ((`method` in ('Pix','Credit','Debit','Cash')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 5. Tabela de Notificações
CREATE TABLE IF NOT EXISTS `notification` (
  `notificationid` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `message` varchar(255) NOT NULL,
  `type` varchar(50) NOT NULL,
  `is_read` tinyint(1) DEFAULT '0',
  `created_at` datetime NOT NULL,
  `userid` int NOT NULL,
  PRIMARY KEY (`notificationid`),
  KEY `fk_userr_id` (`userid`),
  CONSTRAINT `fk_userr_id_notif` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 6. Tabela de Transações (Histórico/Movimentações)
CREATE TABLE IF NOT EXISTS `transaction` (
  `transactionsid` int NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,2) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `method` varchar(50) DEFAULT NULL,
  `transactiondate` date NOT NULL,
  `userid` int NOT NULL,
  `expenseid` int DEFAULT NULL,
  `incomeid` int DEFAULT NULL,
  PRIMARY KEY (`transactionsid`),
  KEY `Fk_Id_User_` (`userid`),
  KEY `Fk_Id_Expense` (`expenseid`),
  KEY `Fk_Id_Income` (`incomeid`),
  CONSTRAINT `Fk_Id_Expense_trans` FOREIGN KEY (`expenseid`) REFERENCES `expense` (`expenseid`),
  CONSTRAINT `Fk_Id_Income_trans` FOREIGN KEY (`incomeid`) REFERENCES `income` (`incomeid`),
  CONSTRAINT `Fk_Id_User_trans` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;