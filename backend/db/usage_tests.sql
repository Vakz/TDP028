SELECT * FROM Brewery;

CALL menu(2);

SELECT * FROM PubText;

SELECT * FROM Pub;

SELECT * FROM Beer;

CALL findWithin(GeomFromText("POINT(58.4115954 15.202247000000034)"), 300000);

SET GLOBAL log_output = 'TABLE';

SET GLOBAL general_log = 'ON';

SELECT * FROM mysql.general_log;

SET @j = JSON_OBJECT('[1,2]');
SELECT @j;