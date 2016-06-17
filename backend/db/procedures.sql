DROP PROCEDURE IF EXISTS findClosest;
DROP PROCEDURE IF EXISTS findWithin;
DROP FUNCTION IF EXISTS calculateDistance;
DROP PROCEDURE IF EXISTS search;
DROP PROCEDURE IF EXISTS menu;
DROP VIEW IF EXISTS PubText;
DROP PROCEDURE IF EXISTS servingWithin;
DROP PROCEDURE IF EXISTS suggestion;

DELIMITER //

CREATE FUNCTION calculateDistance(p1 POINT, p2 POINT)
RETURNS INTEGER
BEGIN
	RETURN 6371000 * 2 * ASIN(SQRT(
            POWER(SIN((X(p1) - abs(X(p2))) * pi()/180 / 2),
            2) + COS(X(p1) * pi()/180 ) * COS(abs(X(p2)) *
            pi()/180) * POWER(SIN((Y(p1) - Y(p2)) *
            pi()/180 / 2), 2) ));
END
//

CREATE PROCEDURE findClosest(p POINT)
BEGIN
	SELECT id, name, description, ST_AsText(gps) AS gps, calculateDistance(gps, p) AS distance FROM Pub
    ORDER BY distance ASC LIMIT 1;
END
//

CREATE PROCEDURE findWithin(p POINT, max_distance INTEGER)
BEGIN
	SELECT id, name, description, ST_AsText(gps) AS gps, calculateDistance(gps, p) AS distance FROM Pub
    HAVING distance < max_distance
    ORDER BY distance ASC;
END
//

CREATE PROCEDURE search(q varchar(50))
BEGIN
	SET @q = CONCAT('%', q, '%');
	SELECT * FROM Beer WHERE
    name LIKE @q OR type LIKE @q OR brewery LIKE @q;
    
END
//

CREATE PROCEDURE menu(id Integer)
BEGIN
	SELECT Beer.* FROM Beer
    INNER JOIN Serves ON Beer.id = Serves.beerid
    WHERE Serves.pubid = id;
END
//

CREATE PROCEDURE servingWithin(p POINT, max_distance INTEGER, bid INTEGER)
BEGIN
	SELECT id, name, description, ST_AsText(gps) AS gps, calculateDistance(gps, p) AS distance FROM Pub
    WHERE id IN (SELECT pubid FROM Serves WHERE Serves.beerid = bid)
    HAVING distance < max_distance
    ORDER BY distance ASC;
END
//

CREATE PROCEDURE suggestion(p POINT, ids JSON)
BEGIN
	/* Creating temporary table to store the ids that are to be filtered away */
	DROP TABLE IF EXISTS filterids;
	CREATE TEMPORARY TABLE filterids(
		id Integer PRIMARY KEY
	);

	SET @l = JSON_LENGTH(ids);
	WHILE @l != 0 DO
		SET @l = @l - 1;
		INSERT IGNORE INTO filterids VALUES (JSON_EXTRACT(ids, CONCAT('$[', @l, ']')));
	END WHILE;
	SET @closestid = (SELECT id
    FROM Pub WHERE id IN (SELECT DISTINCT pubid FROM Serves WHERE beerid NOT IN (SELECT id FROM filterids))
    ORDER BY calculateDistance(gps, p) ASC LIMIT 1);
    
	SELECT Beer.* FROM Beer
    INNER JOIN Serves ON Beer.id = Serves.beerid
    WHERE id NOT IN (SELECT id FROM filterids) AND Serves.pubid = @closestid
    ORDER BY RAND() LIMIT 1;
    
	DROP TABLE IF EXISTS filterids;
END
//
DELIMITER ;
CREATE VIEW PubText AS
SELECT id,name,description,ST_AsText(gps) AS gps FROM Pub;
