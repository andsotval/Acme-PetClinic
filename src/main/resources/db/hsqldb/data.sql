-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO user(id, username,password,enabled) VALUES (1,'admin','admin',TRUE);
INSERT INTO authority VALUES ('admin','admin');
INSERT INTO administrator(id, user_id) VALUES (1, 1);
-- One manager user, named manager1 with passwor manager1
INSERT INTO user(id, username,password,enabled) VALUES (2, 'manager1','manager1',TRUE);
INSERT INTO authority VALUES ('manager1','manager');
INSERT INTO manager(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (1, 2, 'pepito', 'pepi', 'C/ Pepi numero 1', 'Sevilla', '600000000', 'pepi@gmail.com');
-- One owner user, named owner1 with passwor owner1
INSERT INTO user(id, username,password,enabled) VALUES (3, 'owner1','owner1',TRUE);
INSERT INTO authority VALUES ('owner1','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (1, 3, 'encarni', 'carni', 'C/ Carni numero 1', 'Cadiz', '600000001', 'carni@gmail.com');
-- One vet user, named vet1 with passwor vet1
INSERT INTO user(id, username,password,enabled) VALUES (4, 'vet1','vet1',TRUE);
INSERT INTO authority VALUES ('vet1','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (1, 4, 'juanito', 'juan', 'C/ Juan numero 1', 'Malaga', '600000002', 'juan@gmail.com');
