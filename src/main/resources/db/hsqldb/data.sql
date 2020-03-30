
-- ADMIN

INSERT INTO user(id, username,password,enabled) VALUES (1,'admin','admin',TRUE);
INSERT INTO authority VALUES ('admin','admin');
INSERT INTO administrator(id, user_id) VALUES (1, 1);

-- MANAGERS

INSERT INTO user(id, username,password,enabled) VALUES (2, 'manager1','manager1',TRUE);
INSERT INTO authority VALUES ('manager1','manager');
INSERT INTO manager(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (1, 2, 'Alfonso', 'Nuñez', 'C/San Alberto, 5', 'Sevilla', '656719435', 'manager1@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (3, 'manager2','manager2',TRUE);
INSERT INTO authority VALUES ('manager2','manager');
INSERT INTO manager(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (2, 3, 'Pedro', 'Suarez', 'Av. de Andalucia, 247, Portal C, 1ºA', 'Vigo', '694602736', 'manager2@gmail.com');


-- PROVIDERS

-- providers with a manager assigned
INSERT INTO user(id, username,password,enabled) VALUES (4, 'provider1','provider1',TRUE);
INSERT INTO authority VALUES ('provider1','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (1, 4, 'Miguel', 'Patón', 'C/Obreros, 1', 'Madrid', '6453097624', 'provider1@gmail.com', 1);
INSERT INTO user(id, username,password,enabled) VALUES (5, 'provider2','provider2',TRUE);
INSERT INTO authority VALUES ('provider2','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (2, 5, 'Mario', 'Patón', 'C/Marconi, 14', 'Madrid', '645031698', 'provider2@gmail.com', 1);
INSERT INTO user(id, username,password,enabled) VALUES (6, 'provider3','provider3',TRUE);
INSERT INTO authority VALUES ('provider3','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (3, 6, 'Carlos', 'Gutierrez', 'C/Pedro Laín, 32', 'Dos Hermanas', '600001239', 'provider3@gmail.com', 2);
INSERT INTO user(id, username,password,enabled) VALUES (7, 'provider4','provider4',TRUE);
INSERT INTO authority VALUES ('provider4','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (4, 7, 'Pepe', 'Arquellada', 'C/Trafalgar Square, 58', 'Londres', '600001200', 'provider4@gmail.com', 2);
INSERT INTO user(id, username,password,enabled) VALUES (8, 'provider5','provider5',TRUE);
INSERT INTO authority VALUES ('provider5','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (5, 8, 'Jose', 'Redondo', 'C/La Mina, 23', 'Alcorcón', '642791211', 'provider5@gmail.com', 2);
INSERT INTO user(id, username,password,enabled) VALUES (9, 'provider6','provider6',TRUE);
INSERT INTO authority VALUES ('provider6','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (6, 9, 'Maria', 'Ortíz', 'C/Tajo, 43', 'Pamplona', '600007788', 'provider6@gmail.com', 2);

-- providers without manager assigned
INSERT INTO user(id, username,password,enabled) VALUES (10, 'provider7','provider7',TRUE);
INSERT INTO authority VALUES ('provider7','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (7, 10, 'Luisa', 'Sanchez', 'Plaza de la Constitución, 143 Bajo A', 'Cuenca', '682340678', 'provider7@gmail.com', null);
INSERT INTO user(id, username,password,enabled) VALUES (11, 'provider8','provider8',TRUE);
INSERT INTO authority VALUES ('provider8','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (8, 11, 'Agustín', 'Rebolledo', 'C/Cardador, 13', 'Caceres', '694031457', 'provider8@gmail.com', null);
INSERT INTO user(id, username,password,enabled) VALUES (12, 'provider9','provider9',TRUE);
INSERT INTO authority VALUES ('provider9','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (9, 12, 'Antonio', 'Montaño', 'C/Doñana, 20', 'Torrent', '698360154', 'provider9@gmail.com', null);
INSERT INTO user(id, username,password,enabled) VALUES (13, 'provider10','provider10',TRUE);
INSERT INTO authority VALUES ('provider10','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (10, 13, 'Isabel', 'Carmona', 'C/Desengaño, 21', 'Madrid', '678001234', 'provider10@gmail.com', null);


-- PRODUCTS

-- One product 
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (1, 'Comida para perros castrados', 15.95, true, 3);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (2, 'Comida para gatos castrados', 17.95, true, 3);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (3, 'Hueso para perros', 8.95, true, 3);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (4, 'Bisturí', 50.99, true, 1);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (5, 'Guante', 5.95, true, 1);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (6, 'Mascarilla', 7.50, true, 1);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (7, 'Jeringuilla', 9.90, false, 1);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (8, 'Jeringuilla', 12.90, false, 2);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (9, 'Bisturís', 44.95, true, 2);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (10, 'Juguete para gato', 2.95, true, 4);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (11, 'Arenero para gato', 5.80, true, 4);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (12, 'Correa para perro', 4.95, true, 5);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (13, 'Champú para perro', 5.95, false, 5);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (14, 'Pipeta para gato', 19.99, true, 5);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (15, 'Jaula para pajaro', 33.35, false, 6);
INSERT INTO product(id, name, price, is_available, provider_id) VALUES (16, 'Pecera', 81.60, true, 6);

-- VETS

INSERT INTO user(id, username,password,enabled) VALUES (14, 'vet1','vet1',TRUE);
INSERT INTO authority VALUES ('vet1','veterinarian');
<<<<<<< HEAD
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
=======
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (1, 14, 'Juan', 'Cardona', 'C/Ordesa, 7', 'Malaga', '670336994', 'vet1@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (15, 'vet2','vet2',TRUE);
INSERT INTO authority VALUES ('vet2','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (2, 15, 'Paula', 'Barrios', 'Av. de la Constitución, 54, 1ºE', 'Roma', '670024855', 'vet2@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (16, 'vet3','vet3',TRUE);
INSERT INTO authority VALUES ('vet3','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (3, 16, 'Raquel', 'Vega', 'Plaza Bobinadora, 36', 'Copenhague', '677750226', 'vet3@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (17, 'vet4','vet4',TRUE);
INSERT INTO authority VALUES ('vet4','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (4, 17, 'Jorge', 'Ruiz', 'C/Botica', 'Moscú', '633364795', 'vet4@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (18, 'vet5','vet5',TRUE);
INSERT INTO authority VALUES ('vet5','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (5, 18, 'Carmen', 'Ponce', 'Av. de España, 87, Portal A, 5ºC', 'La Rioja', '600142733', 'vet5@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (19, 'vet6','vet6',TRUE);
INSERT INTO authority VALUES ('vet6','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (6, 19, 'Lourdes', 'Villegas', 'C/Andalucia, 34', 'Bilbao', '679123162', 'vet6@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (20, 'vet7','vet7',TRUE);
INSERT INTO authority VALUES ('vet7','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (7, 20, 'Daniel', 'Valdivia', 'C/Ruiz Gijon, 1', 'Granada', '678945210', 'vet7@gmail.com');

-- SPECIALTIES
>>>>>>> origin/group-B

INSERT INTO specialty VALUES (1, 'radiology');
INSERT INTO specialty VALUES (2, 'surgery');
INSERT INTO specialty VALUES (3, 'dentistry');

<<<<<<< HEAD
=======
-- VET_SPECIALTIES

INSERT INTO vet_specialties VALUES (1, 3);
>>>>>>> origin/group-B
INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

<<<<<<< HEAD
=======
-- PET_TYPE

>>>>>>> origin/group-B
INSERT INTO pet_type VALUES (1, 'cat');
INSERT INTO pet_type VALUES (2, 'dog');
INSERT INTO pet_type VALUES (3, 'lizard');
INSERT INTO pet_type VALUES (4, 'snake');
INSERT INTO pet_type VALUES (5, 'bird');
INSERT INTO pet_type VALUES (6, 'hamster');

<<<<<<< HEAD
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
=======
-- OWNERS

INSERT INTO user(id, username,password,enabled) VALUES (21, 'owner1','owner1',TRUE);
INSERT INTO authority VALUES ('owner1','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (1, 21, 'Encarnación', 'Padilla', 'C/Libertadores', 'Cadiz', '659813471', 'owner1@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (22, 'owner2','owner2',TRUE);
INSERT INTO authority VALUES ('owner2','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (2,22, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '608555102', 'owner2@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (23, 'owner3','owner3',TRUE);
INSERT INTO authority VALUES ('owner3','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (3,23, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '608555174', 'owner3@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (24, 'owner4','owner4',TRUE);
INSERT INTO authority VALUES ('owner4','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (4,24, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '608555873', 'owner4@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (25, 'owner5','owner5',TRUE);
INSERT INTO authority VALUES ('owner5','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (5,25, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '608555319', 'owner5@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (26, 'owner6','owner6',TRUE);
INSERT INTO authority VALUES ('owner6','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (6,26, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '608555275', 'owner6@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (27, 'owner7','owner7',TRUE);
INSERT INTO authority VALUES ('owner7','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (7,27, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '608555265', 'owner7@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (28, 'owner8','owner8',TRUE);
INSERT INTO authority VALUES ('owner8','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (8,28, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '608555538', 'owner8@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (29, 'owner9','owner9',TRUE);
INSERT INTO authority VALUES ('owner9','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (9,29, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '608557683', 'owner9@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (30, 'owner10','owner10',TRUE);
INSERT INTO authority VALUES ('owner10','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (10,30, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '605559435', 'owner10@gmail.com');
INSERT INTO user(id, username,password,enabled) VALUES (31, 'owner11','owner11',TRUE);
INSERT INTO authority VALUES ('owner11','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (11,31, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '608555487', 'owner11@gmail.com');

-- PETS
>>>>>>> origin/group-B

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

<<<<<<< HEAD
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (1, 'Clinic1', 'AddresClinic1', 'CityClinic1','999999999',1);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (2, 'Clinic2', 'AddresClinic2', 'CityClinic2','999999999',1);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (3, 'Clinic3', 'AddresClinic3', 'CityClinic3','999999999',1);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (4, 'Clinic4', 'AddresClinic4', 'CityClinic4','999999999',1);

INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (1, 7, '2020-06-09', 'visit aceptada',true,1);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (2, 8, '2020-06-09', 'visit rechazada',false,2);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (3, 8, '2020-06-09', 'visit aceptada',true,3);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (4, 7, '2020-06-09', 'visit pendiente',null,4);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (5, 8, '2020-06-09', 'visit pendiente',null,2);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (6, 8, '2020-06-09', 'visit pendiente',null,3);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (7, 7, '2020-06-09', 'visit pendiente',null,4);

INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (1, '2020-06-09', '2020-06-12','stay pendiente', null,1,1);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (2, '2020-06-09', '2020-06-12','stay pendiente', null,1,2);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (3, '2020-06-09', '2020-06-13','stay rechazada', false,1,3);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (4, '2020-06-09', '2020-06-14','stay aceptada', true,2,3);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (5, '2020-06-09', '2020-06-15','stay rechazada', false,2,4);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (6, '2020-06-09', '2020-06-11','stay aceptada', true,3,4);
=======
-- CLINICS

INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (1, 'Clinic1', 'AddresClinic1', 'CityClinic1','911111111',1);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (2, 'Clinic2', 'AddresClinic2', 'CityClinic2','922222222',2);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (3, 'Clinic3', 'AddresClinic3', 'CityClinic3','933333333',null);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (4, 'Clinic4', 'AddresClinic4', 'CityClinic4','944444444',null);

-- VISITS

INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (1, 7, '2013-01-01', 'rabies shot',true,1);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (2, 8, '2013-01-02', 'rabies shot',false,2);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (3, 8, '2013-01-03', 'neutered',true,3);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (4, 7, '2013-01-04', 'spayed',false,4);
>>>>>>> origin/group-B
