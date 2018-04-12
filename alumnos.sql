-- 
-- Base de datos: `alumnos` 
-- 
-- -------------------------------------------------------- 
--
-- Eliminamos base de datos existentes previas
--
DROP DATABASE IF EXISTS `alumnos`;
--
-- Creamos la base de datos
--
CREATE DATABASE IF NOT EXISTS `alumnos`;
--
-- Usamos la base de datos alumnos
--
USE `alumnos`;
--
-- Estructura de tabla para la tabla `alumnos` 
-- 
CREATE TABLE IF NOT EXISTS `alumnos` ( 
  `DNI` varchar(9) NOT NULL, 
  `Nombre` varchar(50) NOT NULL, 
  `Apellidos` varchar(70) NOT NULL, 
  `Direccion` varchar(100) NOT NULL, 
  `FechaNac` date NOT NULL, 
  PRIMARY KEY (`DNI`) 
) ENGINE=MyISAM DEFAULT CHARSET=latin1; 
-- 
-- Volcar la base de datos para la tabla `alumnos` 
-- 
INSERT INTO `alumnos` (`DNI`, `Nombre`, `Apellidos`, `Direccion`, `FechaNac`) VALUES 
('12345678A', 'José Alberto', 'González Pérez', 'C/Albahaca, nº14, 1ºD', '1986-07-15'), 
('23456789B', 'Almudena', 'Cantero Verdemar', 'Avd/ Profesor Alvarado, n27, 8ºA', '1988-11-04'), 
('14785236D', 'Martín', 'Díaz Jiménez', 'C/Luis de Gongora, nº2.', '1987-03-09'), 
('96385274F', 'Lucas', 'Buendia Portes', 'C/Pintor Sorolla, nº 16, 4ºB', '1988-07-10');
--
-- Estuctura de tabla para la tabla `calificaciones`
--
CREATE TABLE IF NOT EXISTS `calificaciones` (
  `id_curso`int NOT NULL AUTO_INCREMENT,
  `DNI` varchar(9) NOT NULL, 
  `NombreCurso` varchar(60) NOT NULL,
  `Curso` varchar(5) NOT NULL,
  `Nota` double NOT NULL,
  PRIMARY KEY (`id_curso`),
  FOREIGN KEY (`DNI`) REFERENCES `alumnos`(`DNI`) ON DELETE CASCADE
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
--
-- Volcar la base de datos para la tabla `calificaciones`
--
INSERT INTO `calificaciones` (`DNI`, `NombreCurso`, `Curso`, `Nota`) VALUES
('12345678A', 'Acceso a datos', '17-18', 7),
('23456789B', 'Desarrollo Web Entorno Servidor', '16-17', 9.2),
('14785236D', 'Bases de Datos', '15-16', 8.5),
('96385274F', 'Desarrollo Web Entorno Cliente', '17-18', 5.3);
