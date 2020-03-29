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
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (1, 4, 'juanito', 'juan', 'C/ Juan numero 1', 'Malaga', '600000002', 'juan2@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (5, 'vet2','vet2',TRUE);
INSERT INTO authority VALUES ('vet2','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (2, 5, 'juanito', 'juan', 'C/ Juan numero 1', 'Malaga', '600000002', 'juan3@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (6, 'vet3','vet3',TRUE);
INSERT INTO authority VALUES ('vet3','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (3, 6, 'juanito', 'juan', 'C/ Juan numero 1', 'Malaga', '600000002', 'juan4@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (7, 'vet4','vet4',TRUE);
INSERT INTO authority VALUES ('vet4','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (4, 7, 'juanito', 'juan', 'C/ Juan numero 1', 'Malaga', '600000002', 'juan5@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (8, 'vet5','vet5',TRUE);
INSERT INTO authority VALUES ('vet5','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (5, 8, 'juanito', 'juan', 'C/ Juan numero 1', 'Malaga', '600000002', 'juan6@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (9, 'vet6','vet6',TRUE);
INSERT INTO authority VALUES ('vet6','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (6, 9, 'juanito', 'juan', 'C/ Juan numero 1', 'Malaga', '600000002', 'juan7@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (10, 'vet7','vet7',TRUE);
INSERT INTO authority VALUES ('vet7','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (7, 10, 'juanito', 'juan', 'C/ Juan numero 1', 'Malaga', '600000002', 'juan8@gmail.com');

INSERT INTO specialty VALUES (1, 'radiology');
INSERT INTO specialty VALUES (2, 'surgery');
INSERT INTO specialty VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO pet_type VALUES (1, 'cat');
INSERT INTO pet_type VALUES (2, 'dog');
INSERT INTO pet_type VALUES (3, 'lizard');
INSERT INTO pet_type VALUES (4, 'snake');
INSERT INTO pet_type VALUES (5, 'bird');
INSERT INTO pet_type VALUES (6, 'hamster');

INSERT INTO user(id, username,password,enabled) VALUES (11, 'owner2','owner2',TRUE);
INSERT INTO authority VALUES ('owner2','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (2,11, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner2@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (12, 'owner3','owner3',TRUE);
INSERT INTO authority VALUES ('owner3','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (3,12, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner3@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (13, 'owner4','owner4',TRUE);
INSERT INTO authority VALUES ('owner4','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (4,13, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner4@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (14, 'owner5','owner5',TRUE);
INSERT INTO authority VALUES ('owner5','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (5,14, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner5@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (15, 'owner6','owner6',TRUE);
INSERT INTO authority VALUES ('owner6','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (6,15, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner6@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (16, 'owner7','owner7',TRUE);
INSERT INTO authority VALUES ('owner7','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (7,16, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner7@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (17, 'owner8','owner8',TRUE);
INSERT INTO authority VALUES ('owner8','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (8,17, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner8@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (18, 'owner9','owner9',TRUE);
INSERT INTO authority VALUES ('owner9','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (9,18, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner9@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (19, 'owner10','owner10',TRUE);
INSERT INTO authority VALUES ('owner10','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (10,19, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner10@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (20, 'owner11','owner11',TRUE);
INSERT INTO authority VALUES ('owner11','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (11,20, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner11@gmail.com');

INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);

INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (1, 'Clinic1', 'AddresClinic1', 'CityClinic1','999999999',1);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (2, 'Clinic2', 'AddresClinic2', 'CityClinic2','999999999',null);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (3, 'Clinic3', 'AddresClinic3', 'CityClinic3','999999999',null);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (4, 'Clinic4', 'AddresClinic4', 'CityClinic4','999999999',null);

INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (1, 7, '2013-01-01', 'rabies shot',true,1);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (2, 8, '2013-01-02', 'rabies shot',false,2);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (3, 8, '2013-01-03', 'neutered',true,3);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (4, 7, '2013-01-04', 'spayed',false,4);
