DROP TABLE IF EXISTS Serves;
DROP TABLE IF EXISTS Pub;
DROP TABLE IF EXISTS Beer;
DROP TABLE IF EXISTS BeerType;
DROP TABLE IF EXISTS Brewery;



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