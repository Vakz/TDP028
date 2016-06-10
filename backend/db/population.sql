INSERT IGNORE INTO BeerType VALUES ("Pale Lager"), ("India Pale Ale"), ("Dark Lager"), ("Porter and Stout"), ("Ale"), ("Irish Ale"), ("Wheat Beer");

INSERT IGNORE INTO Brewery VALUES ("Carlsberg"), ("Staropramen"), ("Guinness"), ("Anheuser-Busch"), ("Boston Beer Company"), ("Modelo"), ("Ängö Kvartesbryggeri"), ("InBev");

INSERT IGNORE INTO Beer(name, type, brewery, description) VALUES
("Stella Artois", "Pale Lager", "InBev", "Fruktig smak med inslag av honung, citrus och ljust bröd."),
("Carlsberg Export", "Pale Lager", "Carlsberg", "Maltig smak med inslag av knäckebröd, honung och citrusskal."),
("Tuborg Guld", "Pale Lager", "Carlsberg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque ut porttitor ex. Suspendisse potenti. Vivamus vehicula, enim ac rhoncus semper."),
("Falcon Julöl", "Dark Lager", "Carlsberg", "Maltig, rostad smak med inslag av kavring, kakao och pomerans."),
("Jacobsen Golden Naked Christmas Ale", "Ale", "Carlsberg", "Fruktig, maltig smak med sötma, inslag av aprikosmarmelad, ljus sirap, mörkt bröd och apelsin."),
("Staropramen", "Pale Lager", "Staropramen", "Maltig smak med liten sötma, inslag av knäckebröd, torkade aprikoser, örter och apelsinskal."),
("Staropramen Granat", "Dark Lager", "Staropramen", "Maltig smak med inslag av knäck, torkad frukt och rågbröd."),
("Staropramen Dark", "Dark Lager", "Staropramen", "Maltig smak med inslag av knäck, surdegsbröd, choklad, kaffe och torkade aprikoser."),
("Guinness Extra Stout", "Porter and Stout", "Guinness", "Smakrik, rostad öl med tydlig beska, inslag av kaffe, soja, pumpernickel, charkuterier, apelsinskal och lakrits."),
("Guinness Draught", "Porter and Stout", "Guinness", "Maltig, rostad smak inslag av charkuterier, lakrits, kaffe, mörkt bröd och pomerans."),
("Kilkenny", "Irish Ale", "Guinness", "Maltig smak med inslag av knäck, rågbröd, citrusskal och torkade örter."),
("Budweiser", "Pale Lager", "Anheuser-Busch", "Fruktig smak med inslag av honung och citrus."),
("Samuel Adams Boston Ale", "Ale", "Boston Beer Company", "Fruktig smak med inslag av torkade aprikoser, farinsocker och apelsinskal."),
("Samuel Adams Boston Lager", "Dark Lager", "Boston Beer Company", "Nyanserad, humlearomatisk smak med inslag av apelsinskal, honung, knäck, örter och sirapslimpa."),
("Samuel Adams Winter Lager", "Dark Lager", "Boston Beer Company", "Fruktig smak med liten sötma inslag av torkade aprikoser, sirapslimpa, apelsinskal, örter och honung."),
("Samuel Adams Imperial Stout", "Porter and Stout", "Boston Beer Company", "Nyanserad, maltig smak med sötma, inslag av kavring, mjuk pepparkaka, chokladkola, kaffe, nougat, pomerans och lakrits."),
("Samuel Adams Rebel IPA", "India Pale Ale", "Boston Beer Company", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque ut porttitor ex."),
("Corona", "Pale Lager", "Modelo", "Brödig smak med inslag av citrus och örter."),
("Negra Modelo", "Dark Lager", "Modelo", "Nyanserad, knäckig smak med liten sötma, inslag av torkad frukt, mörkt bröd, russin, choklad och pomerans."),
("Ängöl Ljuva Livets", "Ale", "Ängö Kvartesbryggeri", "Maltig smak med inslag av torkad frukt, honung, knäckebröd och smörkola."),
("Ängöl Lata Dagars IPA", "India Pale Ale", "Ängö Kvartesbryggeri", "Humlearomatisk smak med inslag av grapefrukt, aprikos, örter, honung och ljust bröd."),
("Ängöl Alla Tiders", "Pale Lager", "Ängö Kvartesbryggeri", "Maltig smak med inslag av apelsin, ljust bröd, honung och örter."),
("Löwenbräu", "Pale Lager", "InBev", "Brödig smak med inslag av örter, citrus och honung."),
("Leffe Blonde", "Ale", "InBev", "Fruktig, kryddig smak med sötma, inslag av aprikos, banan, örter och honung."),
("Hoegaarden", "Wheat Beer", "InBev", "Fruktig smak med inslag av vetebröd, citron och jasminris.");

INSERT IGNORE INTO Pub(name, gps, description) VALUES
("O'Learys Kalmar", ST_PointFromText("POINT(56.6624875 16.36129959999994)"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed convallis sapien id fermentum condimentum. Pellentesque eros lorem, ultricies at ex quis, rhoncus tempor sapien. Integer accumsan pretium est, vitae malesuada nisl consequat nec. Suspendisse vel sollicitudin ex. Pellentesque euismod pellentesque quam, a blandit diam elementum vel. Nullam semper sed dolor eu facilisis. Donec porta ut odio nec hendrerit. Nam faucibus fermentum luctus. Mauris rutrum suscipit urna, a bibendum quam placerat sed. Nam nec auctor mi, semper cursus ligula. Suspendisse potenti. Curabitur quis turpis gravida, scelerisque sapien nec, porttitor lectus. Ut ac nunc dictum, mollis enim vitae, sodales erat. Suspendisse varius commodo orci, a condimentum mauris pulvinar quis. "),
("New York Legends", ST_PointFromText("POINT(58.4117745 15.622129500000028)"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed convallis sapien id fermentum condimentum."),
("Flamman Pub&Disco", ST_PointFromText("POINT(58.4115954 15.602247000000034)"), "Flamman Pub&Disco är en studentkrog som ligger i Gottfridsberg i Linköping. Vårt mål är att erbjuda Linköpings studenter stans bästa studentklubb. På Flamman är det alltid en härlig stämning, hög puls och studentvänliga priser."),
("Nivå 22", ST_PointFromText("POINT(59.3328095 18.02810790000001)"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed convallis sapien id fermentum condimentum. Pellentesque eros lorem, ultricies at ex quis, rhoncus tempor sapien."),
("The Rover", ST_PointFromText("POINT(57.6991516 11.949041699999952)"), "Ta med polarna, familjen, vänner, ovänner, livskamraten eller annat löst folk som du hittat på vägen hit. Ät oförskämt bra mat, drick öl och sprit i världsklass kombinera det med en personlig miljö skapad av oss.");

INSERT IGNORE INTO Serves(pubid, beerid) VALUES
(1,1), (1,4), (1,5), (1,7), (1,12), (1,14), (1,16), (1,17), (1,18), (1,19), (1,22), (1,25),
(2,1), (2,2), (2,6), (2,8), (2,9), (2,12), (2,14), (2,16), (2,18), (2,21), (2,22), (2,23),
(3,2), (3,6), (3,7), (3,10), (3,11), (3,13), (3,15), (3,16), (3,17), (3,20), (3,21), (3,24),
(4,3), (4,9), (4,10), (4,11), (4,12), (4,15), (4,16), (4,20), (4,21), (4,22), (4,23), (4,25),
(5,1), (5,2), (5,3), (5,8), (5,10), (5,11), (5,15), (5,17), (5,20), (5,21), (5,23), (5,25);