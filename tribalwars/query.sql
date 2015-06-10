SELECT
	Farm.distance,
	CASE
		WHEN ((wood.production * ?) * (strftime("%s", datetime('now', 'localtime')) - strftime("%s", Report.attackTime))) > storage.storage THEN storage.storage
		ELSE ((wood.production * ?) * (strftime("%s", datetime('now', 'localtime')) - strftime("%s", Report.attackTime)))
	END + CASE
		WHEN ((stone.production * ?) * (strftime("%s", datetime('now', 'localtime')) - strftime("%s", Report.attackTime))) > storage.storage THEN storage.storage
		ELSE ((stone.production * ?) * (strftime("%s", datetime('now', 'localtime')) - strftime("%s", Report.attackTime)))
	END + CASE
		WHEN ((iron.production * ?) * (strftime("%s", datetime('now', 'localtime')) - strftime("%s", Report.attackTime))) > storage.storage THEN storage.storage
		ELSE ((iron.production * ?) * (strftime("%s", datetime('now', 'localtime')) - strftime("%s", Report.attackTime)))
	END AS 'possibleResources',
	Report.wall
FROM Report
JOIN Farm
	ON Farm.idFarm = Report.idFarm
JOIN Production AS wood
	ON wood.level = Report.wood
JOIN Production AS stone
	ON stone.level = Report.stone
JOIN Production AS iron
	ON iron.level = Report.iron
JOIN Storage AS storage
	ON storage.level = Report.storage
WHERE Farm.idVillage = ?
GROUP BY Report.idFarm
ORDER BY Farm.distance ASC;