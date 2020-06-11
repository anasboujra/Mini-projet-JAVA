-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Jun 11, 2020 at 05:03 PM
-- Server version: 10.4.10-MariaDB
-- PHP Version: 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `java`
--

-- --------------------------------------------------------

--
-- Table structure for table `vehicule`
--

DROP TABLE IF EXISTS `vehicule`;
CREATE TABLE IF NOT EXISTS `vehicule` (
  `numImmatriculation` int(20) NOT NULL,
  `marque` varchar(30) NOT NULL,
  `type` varchar(30) NOT NULL,
  `carburant` varchar(30) NOT NULL,
  `compteurKM` int(30) NOT NULL,
  `dateMiseCirculation` date NOT NULL,
  `parking` varchar(30) DEFAULT 'Aucun',
  PRIMARY KEY (`numImmatriculation`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `vehicule`
--

INSERT INTO `vehicule` (`numImmatriculation`, `marque`, `type`, `carburant`, `compteurKM`, `dateMiseCirculation`, `parking`) VALUES
(1234, 'BMW', '4x4', 'diesel', 1010, '2020-02-06', 'Atlas'),
(3, 'Citroën', 'voiture', 'Diesel', 20000, '2020-04-08', 'Aucun'),
(9999, 'Dacia', 'SS', 'Essence', 22, '2018-06-07', '22');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
