-- TABLES
CREATE TABLE `VorlageItem` ( `idVorlage` INTEGER NOT NULL, `position` INTEGER NOT NULL, `idBuilding` INTEGER NOT NULL, `level` INTEGER NOT NULL);
CREATE TABLE `Vorlage` ( `idVorlage` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `name` INTEGER NOT NULL);
CREATE TABLE `Villages` (
	`VillageID`	INTEGER NOT NULL UNIQUE,
	`xCoord`	INTEGER NOT NULL,
	`yCoord`	INTEGER NOT NULL,
	`name`	TEXT,
	`farm`	NUMERIC DEFAULT 0,
	`ramm`	NUMERIC DEFAULT 0,
	PRIMARY KEY(VillageID)
);
CREATE TABLE `SystemLog` ( `idLogItem` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `message` INTEGER);
CREATE TABLE `SubmissionAssignation` (
	`AssignationID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`SubmissionID`	INTEGER,
	`VillageID`	INTEGER
);
CREATE TABLE `RammAssignment` (
	`RammID`	INTEGER,
	`FarmID`	INTEGER,
	`rammed`	NUMERIC DEFAULT 0
);
CREATE TABLE `FarmSubmission` (
	`FarmSubmissionID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`Speer`	INTEGER,
	`Schwert`	INTEGER,
	`Axt`	INTEGER,
	`bogi`	INTEGER,
	`spaeher`	INTEGER,
	`lk`	INTEGER,
	`berittenerbogi`	INTEGER,
	`sk`	INTEGER,
	`ramm`	INTEGER,
	`kata`	INTEGER
);
CREATE TABLE `FarmAssignation` (
	`FarmAssignationID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`VillageID`	INTEGER NOT NULL,
	`FarmID`	INTEGER NOT NULL,
	`farmed`	NUMERIC NOT NULL DEFAULT 0
);
CREATE TABLE `Farm` (
	`FarmID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`xCoord`	INTEGER,
	`yCoord`	INTEGER,
	`hasOwner`	NUMERIC,
	`farm`	NUMERIC
);
CREATE TABLE `Building` ( `idBuilding` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `name` TEXT NOT NULL, `displayName` TEXT NOT NULL);
CREATE TABLE `BuildLog` ( `idLogItem` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `message` INTEGER);
CREATE TABLE `AngriffLog` ( `idLogItem` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `message` INTEGER);

-- TRIGGER
CREATE TRIGGER buildLogTrimmer AFTER INSERT ON `BuildLog` BEGIN  DELETE FROM BuildLog WHERE idLogItem NOT IN (SELECT idLogItem FROM BuildLog ORDER BY idLogItem DESC LIMIT 160); END; 
CREATE TRIGGER angriffLogTrimmer AFTER INSERT ON `AngriffLog` BEGIN  DELETE FROM AngriffLog WHERE idLogItem NOT IN (SELECT idLogItem FROM AngriffLog ORDER BY idLogItem DESC LIMIT 160); END; 
CREATE TRIGGER systemLogTrimmer AFTER INSERT ON `SystemLog` BEGIN  DELETE FROM SystemLog WHERE idLogItem NOT IN (SELECT idLogItem FROM SystemLog ORDER BY idLogItem DESC LIMIT 160); END; 
CREATE TRIGGER `farmAssignationTrimmer2` AFTER DELETE ON `Villages` BEGIN  DELETE FROM `FarmAssignation` WHERE `VillageID`=OLD.`VillageID`; END;
CREATE TRIGGER `farmAssignationTrimmer1` AFTER DELETE ON `Farm` BEGIN  DELETE FROM `FarmAssignation` WHERE `FarmID`=OLD.`FarmID`; END;
CREATE TRIGGER `villageSubmissionAssignationTrimmer` AFTER DELETE ON `Villages` BEGIN  DELETE FROM `SubmissionAssignation` WHERE `VillageID`=OLD.`VillageID`; END;
CREATE TRIGGER `farmSubmissionTrimmer` AFTER DELETE ON `FarmSubmission` BEGIN  DELETE FROM `SubmissionAssignation` WHERE `SubmissionID`=OLD.`FarmSubmissionID`; END;
CREATE TRIGGER `rammAssignmentTrimmer` AFTER DELETE ON `Farm` BEGIN  DELETE FROM `RammAssignment` WHERE `FarmID`=OLD.`FarmID`; END;

-- DEFAULT VALUES
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (1, 'main', '<img src="images/haupthaus.png">&nbsp;Hauptgeb&auml;de');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (2, 'barracks', '<img src="images/att1.png">&nbsp;Kaserne');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (3, 'stable', '<img src="images/stall.png">&nbsp;Stall');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (4, 'garage', '<img src="images/werkstaette.png">&nbsp;Werkstatt');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (5, 'snob', '<img src="images/adelshof.png">&nbsp;Adelshof');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (6, 'smith', '<img src="images/schmiede.png">&nbsp;Schmiede');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (7, 'place', '<img src="images/platz.png">&nbsp;Versammlungsplatz');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (8, 'market', '<img src="images/marktplatz.png">&nbsp;Marktplatz');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (9, 'wood', '<img src="images/holzmine.png">&nbsp;Holzf&auml;ller');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (10, 'stone', '<img src="images/lehmmine.png">&nbsp;Lehmgrube');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (11, 'iron', '<img src="images/eisenmine.png">&nbsp;Eisenmine');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (12, 'farm', '<img src="images/farm.png">&nbsp;Bauernhof');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (13, 'storage', '<img src="images/speicher.png">&nbsp;Speicher');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (14, 'hide', '<img src="images/verstecke.png">&nbsp;Versteck');
INSERT INTO `Building` (`idBuilding`, `name`, `displayName`) VALUES (15, 'wall', '<img src="images/wall.png">&nbsp;Wall');

-- Deffensiv
INSERT INTO `Vorlage` (`idVorlage`, `name`) VALUES (1, 'Deffensiv');
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 1, 1, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 2, 7, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 3, 10, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 4, 9, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 5, 11, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 6, 10, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 7, 9, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 8, 10, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 9, 9, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 10, 11, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 11, 9, 4);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 12, 10, 4);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 13, 13, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 14, 9, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 15, 10, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 16, 9, 6);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 17, 11, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 18, 10, 6);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 19, 13, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 20, 1, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 21, 2, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 22, 13, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 23, 2, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 24, 9, 7);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 25, 8, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 26, 13, 4);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 27, 9, 9);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 28, 10, 7);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 29, 9, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 30, 10, 8);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 31, 9, 11);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 32, 13, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 33, 12, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 34, 10, 9);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 35, 11, 4);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 36, 9, 12);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 37, 10, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 38, 13, 6);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 39, 12, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 40, 2, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 41, 15, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 42, 9, 13);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 43, 13, 7);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 44, 10, 11);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 45, 11, 6);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 46, 12, 4);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 47, 1, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 48, 6, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 49, 9, 14);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 50, 10, 12);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 51, 12, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 52, 13, 9);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 53, 9, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 54, 11, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 55, 10, 13);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 56, 13, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 57, 1, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 58, 12, 8);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 59, 13, 11);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 60, 9, 16);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 61, 10, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 62, 11, 14);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 63, 2, 12);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 64, 6, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 65, 15, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 66, 8, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 67, 12, 11);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 68, 13, 12);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 69, 10, 16);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 70, 9, 17);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 71, 11, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 72, 6, 8);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 73, 8, 6);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 74, 6, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 75, 2, 13);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 76, 3, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 77, 9, 18);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 78, 10, 17);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 79, 13, 16);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 80, 12, 16);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 81, 2, 14);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 82, 15, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 83, 10, 18);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 84, 9, 19);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 85, 10, 19);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 86, 1, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 87, 15, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 88, 12, 18);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 89, 13, 17);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 90, 9, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 91, 10, 22);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 92, 9, 21);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 93, 11, 18);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 94, 12, 21);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 95, 13, 19);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 96, 9, 22);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 97, 8, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 98, 2, 18);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 99, 10, 24);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 100, 3, 8);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 101, 6, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 102, 2, 19);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 103, 13, 22);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 104, 12, 22);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 105, 10, 25);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 106, 12, 23);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 107, 2, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 108, 9, 24);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 109, 11, 21);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 110, 1, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 111, 6, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 112, 5, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 113, 2, 21);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 114, 9, 25);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 115, 10, 26);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 116, 12, 25);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 117, 11, 22);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 118, 13, 25);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 119, 3, 13);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 120, 9, 26);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 121, 2, 24);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 122, 9, 27);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 123, 10, 27);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 124, 9, 28);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 125, 10, 28);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 126, 11, 26);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 127, 13, 27);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 128, 12, 27);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 129, 8, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 130, 3, 18);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 131, 10, 29);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 132, 9, 29);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 133, 11, 27);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 134, 12, 28);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 135, 11, 28);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 136, 13, 28);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 137, 4, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 138, 2, 25);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 139, 13, 29);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 140, 10, 30);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 141, 9, 30);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 142, 11, 30);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 143, 12, 30);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 144, 3, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (1, 145, 4, 5);
-- Offensiv
INSERT INTO `Vorlage` (`idVorlage`, `name`) VALUES (2, 'Offensiv');
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 1, 1, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 2, 7, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 3, 9, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 4, 10, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 5, 11, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 6, 9, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 7, 10, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 8, 9, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 9, 12, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 10, 10, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 11, 11, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 12, 12, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 13, 9, 4);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 14, 10, 4);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 15, 1, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 16, 13, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 17, 1, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 18, 2, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 19, 13, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 20, 12, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 21, 9, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 22, 10, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 23, 11, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 24, 12, 4);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 25, 2, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 26, 13, 4);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 27, 1, 4);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 28, 12, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 29, 1, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 30, 6, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 31, 1, 6);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 32, 13, 6);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 33, 11, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 34, 12, 6);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 35, 1, 7);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 36, 9, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 37, 10, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 38, 12, 7);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 39, 8, 2);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 40, 1, 8);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 41, 2, 4);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 42, 12, 8);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 43, 1, 9);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 44, 2, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 45, 15, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 46, 11, 6);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 47, 6, 4);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 48, 12, 9);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 49, 6, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 50, 12, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 51, 1, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 52, 3, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 53, 12, 11);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 54, 3, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 55, 11, 7);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 56, 3, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 57, 12, 12);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 58, 9, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 59, 10, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 60, 11, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 61, 6, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 62, 4, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 63, 11, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 64, 1, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 65, 12, 14);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 66, 15, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 67, 2, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 68, 9, 16);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 69, 10, 16);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 70, 9, 17);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 71, 10, 17);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 72, 12, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 73, 9, 18);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 74, 10, 18);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 75, 9, 19);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 76, 10, 19);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 77, 9, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 78, 10, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 79, 1, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 80, 9, 21);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 81, 10, 21);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 82, 9, 22);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 83, 10, 22);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 84, 12, 16);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 85, 2, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 86, 3, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 87, 8, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 88, 6, 14);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 89, 11, 17);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 90, 12, 17);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 91, 9, 23);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 92, 10, 23);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 93, 12, 18);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 94, 9, 24);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 95, 10, 24);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 96, 9, 25);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 97, 12, 19);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 98, 10, 25);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 99, 9, 26);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 100, 10, 26);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 101, 11, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 102, 4, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 103, 6, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 104, 9, 27);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 105, 10, 27);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 106, 11, 21);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 107, 3, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 108, 11, 22);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 109, 12, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 110, 9, 28);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 111, 10, 28);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 112, 5, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 113, 12, 30);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 114, 9, 29);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 115, 10, 29);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 116, 9, 30);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 117, 8, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 118, 10, 30);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 119, 11, 25);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 120, 2, 25);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 121, 3, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 122, 11, 30);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (2, 123, 13, 30);
-- Rohstoffe
INSERT INTO `Vorlage` (`idVorlage`, `name`) VALUES (3, 'Rohstoffe');
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 1, 9, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 2, 10, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 3, 11, 3);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 4, 10, 6);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 5, 9, 6);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 6, 10, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 7, 9, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 8, 11, 7);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 9, 1, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 10, 2, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 11, 9, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 12, 10, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 13, 11, 12);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 14, 1, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 15, 15, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 16, 9, 25);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 17, 10, 25);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 18, 11, 25);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 19, 2, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 20, 6, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 21, 9, 30);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 22, 10, 30);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 23, 11, 30);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 24, 1, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 25, 13, 30);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 26, 12, 30);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 27, 7, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 28, 8, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 29, 15, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 30, 6, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 31, 3, 5);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 32, 6, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 33, 5, 1);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 34, 3, 10);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 35, 2, 25);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 36, 3, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 37, 4, 15);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 38, 8, 20);
INSERT INTO `VorlageItem` (`idVorlage`, `position`, `idBuilding`, `level`) VALUES (3, 39, 8, 22);