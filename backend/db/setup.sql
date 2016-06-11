DROP TABLE IF EXISTS Serves;
DROP TABLE IF EXISTS Pub;
DROP TABLE IF EXISTS Beer;
DROP TABLE IF EXISTS BeerType;
DROP TABLE IF EXISTS Brewery;
DROP PROCEDURE IF EXISTS findClosest;
DROP PROCEDURE IF EXISTS findWithin;
DROP FUNCTION IF EXISTS calculateDistance;
DROP PROCEDURE IF EXISTS search;

CREATE TABLE BeerType(
	type_name varchar(20) PRIMARY KEY
);

CREATE TABLE Brewery(
	name varchar(20) PRIMARY KEY
);

CREATE TABLE Beer(
	id Integer PRIMARY KEY AUTO_INCREMENT,
	name varchar(35) NOT NULL,
    type varchar(20) NOT NULL,
    brewery varchar(30) NOT NULL,
    description TINYTEXT,
    
    
    CONSTRAINT fk_beer_beertype
	FOREIGN KEY (type) REFERENCES BeerType(type_name),
    
    CONSTRAINT fk_beer_brewery
    FOREIGN KEY (brewery) REFERENCES Brewery(name)
);

CREATE TABLE Pub(
	id Integer PRIMARY KEY AUTO_INCREMENT,
	name varchar(30) NOT NULL,
    gps POINT NOT NULL,
    description TEXT
);

CREATE TABLE Serves(
	pubid Integer NOT NULL,
    beerid Integer NOT NULL,
    
    CONSTRAINT pk_serves PRIMARY KEY (pubid, beerid),
    
    CONSTRAINT fk_serves_pub FOREIGN KEY (pubid) REFERENCES Pub(id),
    CONSTRAINT fk_serves_beer FOREIGN KEY (beerid) REFERENCES Beer(id)
);

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
	SELECT *, calculateDistance(gps, p) AS distance FROM Pub
    ORDER BY distance ASC LIMIT 1;
END
//

CREATE PROCEDURE findWithin(p POINT, max_distance INTEGER)
BEGIN
	SELECT *, calculateDistance(gps, p) AS distance FROM Pub
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
DELIMITER ;
