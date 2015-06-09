-- TABLES
CREATE TABLE `Farm` ( `idFarm` INTEGER, `idVillage` INTEGER NOT NULL, `x` INTEGER NOT NULL, `y` INTEGER NOT NULL, UNIQUE(`idFarm`, `idVillage`));
CREATE TABLE `Report` ( `idReport` INTEGER NOT NULL, `idFarm` INTEGER, `attackTime` DATETIME, `spyedResources` INTEGER, `wood` INTEGER, `stone` INTEGER, `iron` INTEGER, `wall` INTEGER, PRIMARY KEY(`idReport`));
CREATE TABLE `FarmAttack` ( `idFarmAttack` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idVillage` INTEGER NOT NULL, `arrival` DATETIME NOT NULL, `possibleLoot` INTEGER NOT NULL);