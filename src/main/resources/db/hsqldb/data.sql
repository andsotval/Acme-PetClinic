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

INSERT INTO user(id, username,password,enabled) VALUES (4, 'manager3','manager3',TRUE);
INSERT INTO authority VALUES ('manager3','manager');
INSERT INTO manager(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (3, 4, 'Jimeno', 'Balboa', 'C/San Juan, 23', 'Málaga', '656096435', 'manager3@gmail.com');

INSERT INTO user(id, username,password,enabled) VALUES (5, 'manager4','manager4',TRUE);
INSERT INTO authority VALUES ('manager4','manager');
INSERT INTO manager(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (4, 5, 'Marisa', 'García', 'C/Destino, 1', 'Sevilla', '600122135', 'manager4@gmail.com');

INSERT INTO user(id, username,password,enabled) VALUES (6, 'manager5','manager5',TRUE);
INSERT INTO authority VALUES ('manager5','manager');
INSERT INTO manager(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (5, 6, 'Paco', 'Sánchez', 'C/Fin, 99 1ºA', 'Huelva', '600125555', 'manager5@gmail.com');


-- PROVIDERS

INSERT INTO user(id, username,password,enabled) VALUES (7, 'provider1','provider1',TRUE);
INSERT INTO authority VALUES ('provider1','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (1, 7, 'Miguel', 'Patón', 'C/Obreros, 1', 'Madrid', '6453097624', 'provider1@gmail.com', 1);

INSERT INTO user(id, username,password,enabled) VALUES (8, 'provider2','provider2',TRUE);
INSERT INTO authority VALUES ('provider2','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (2, 8, 'Mario', 'Patón', 'C/Marconi, 14', 'Madrid', '645031698', 'provider2@gmail.com', 2);

INSERT INTO user(id, username,password,enabled) VALUES (9, 'provider3','provider3',TRUE);
INSERT INTO authority VALUES ('provider3','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (3, 9, 'Carlos', 'Gutierrez', 'C/Pedro Laín, 32', 'Dos Hermanas', '600001239', 'provider3@gmail.com', null);

INSERT INTO user(id, username,password,enabled) VALUES (10, 'provider4','provider4',TRUE);
INSERT INTO authority VALUES ('provider4','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (4, 10, 'Pepe', 'Arquellada', 'C/Trafalgar Square, 58', 'Londres', '600001200', 'provider4@gmail.com', null);

INSERT INTO user(id, username,password,enabled) VALUES (11, 'provider5','provider5',TRUE);
INSERT INTO authority VALUES ('provider5','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (5, 11, 'Jose', 'Redondo', 'C/La Mina, 23', 'Alcorcón', '642791211', 'provider5@gmail.com', null);

INSERT INTO user(id, username,password,enabled) VALUES (12, 'provider6','provider6',TRUE);
INSERT INTO authority VALUES ('provider6','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (6, 12, 'Maria', 'Ortíz', 'C/Tajo, 43', 'Pamplona', '600007788', 'provider6@gmail.com', null);

INSERT INTO user(id, username,password,enabled) VALUES (13, 'provider7','provider7',TRUE);
INSERT INTO authority VALUES ('provider7','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (7, 13, 'Luisa', 'Sanchez', 'Plaza de la Constitución, 143 Bajo A', 'Cuenca', '682340678', 'provider7@gmail.com', null);

INSERT INTO user(id, username,password,enabled) VALUES (14, 'provider8','provider8',TRUE);
INSERT INTO authority VALUES ('provider8','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (8, 14, 'Agustín', 'Rebolledo', 'C/Cardador, 13', 'Caceres', '694031457', 'provider8@gmail.com', null);

INSERT INTO user(id, username,password,enabled) VALUES (15, 'provider9','provider9',TRUE);
INSERT INTO authority VALUES ('provider9','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (9, 15, 'Antonio', 'Montaño', 'C/Doñana, 20', 'Torrent', '698360154', 'provider9@gmail.com', null);

INSERT INTO user(id, username,password,enabled) VALUES (16, 'provider10','provider10',TRUE);
INSERT INTO authority VALUES ('provider10','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (10, 16, 'Isabel', 'Carmona', 'C/Desengaño, 21', 'Madrid', '678001234', 'provider10@gmail.com', null);


-- PRODUCTS

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

-- CLINICS

INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (1, 'Clinic1', 'C/ Jaén, 98', 'Granada','954863547',1);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (2, 'Clinic2', 'Paseo de la Castellana, 119', 'Madrid','912654456',2);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (3, 'Clinic3', 'Avenida de los lobos, 3', 'Barcelona','966337858',3);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (4, 'Clinic4', 'C/ José Palermo, 9', 'Málaga','900252029',4);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (5, 'Clinic5', 'C/ Almanzara', 'Sevilla','989678985',5);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (6, 'Clinic6', 'Avenida de Andalucía, 22', 'Huelva','922305704',null);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (7, 'Clinic7', 'C/ Feria, 11', 'Sevilla','900686743',null);
INSERT INTO clinic(id,name,address,city,telephone,manager_id) VALUES (8, 'Clinic8', 'C/Laraña, 53', 'Sevilla','948038444',null);

-- VETS

INSERT INTO user(id, username,password,enabled) VALUES (17, 'vet1','vet1',TRUE);
INSERT INTO authority VALUES ('vet1','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (1, 17, 'Juan', 'Cardona', 'C/Ordesa, 7', 'Malaga', '670336994', 'vet1@gmail.com', 1);

INSERT INTO user(id, username,password,enabled) VALUES (18, 'vet2','vet2',TRUE);
INSERT INTO authority VALUES ('vet2','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (2, 18, 'Paula', 'Barrios', 'Av. de la Constitución, 54, 1ºE', 'Roma', '670024855', 'vet2@gmail.com', 2);

INSERT INTO user(id, username,password,enabled) VALUES (19, 'vet3','vet3',TRUE);
INSERT INTO authority VALUES ('vet3','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (3, 19, 'Raquel', 'Vega', 'Plaza Bobinadora, 36', 'Copenhague', '677750226', 'vet3@gmail.com', 3);

INSERT INTO user(id, username,password,enabled) VALUES (20, 'vet4','vet4',TRUE);
INSERT INTO authority VALUES ('vet4','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (4, 20, 'Jorge', 'Ruiz', 'C/Botica', 'Moscú', '633364795', 'vet4@gmail.com', 4);

INSERT INTO user(id, username,password,enabled) VALUES (21, 'vet5','vet5',TRUE);
INSERT INTO authority VALUES ('vet5','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (5, 21, 'Carmen', 'Ponce', 'Av. de España, 87, Portal A, 5ºC', 'La Rioja', '600142733', 'vet5@gmail.com', 5);

INSERT INTO user(id, username,password,enabled) VALUES (22, 'vet6','vet6',TRUE);
INSERT INTO authority VALUES ('vet6','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (6, 22, 'Lourdes', 'Villegas', 'C/Andalucia, 34', 'Bilbao', '679123162', 'vet6@gmail.com', null);

INSERT INTO user(id, username,password,enabled) VALUES (23, 'vet7','vet7',TRUE);
INSERT INTO authority VALUES ('vet7','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (7, 23, 'Daniel', 'Valdivia', 'C/Ruiz Gijon, 1', 'Granada', '678945210', 'vet7@gmail.com', null);

INSERT INTO user(id, username,password,enabled) VALUES (24, 'vet8','vet8',TRUE);
INSERT INTO authority VALUES ('vet8','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (8, 24, 'Daniela', 'Guitérrez', 'C/Patricia, 43', 'Sevilla', '671245510', 'vet8@gmail.com', null);

INSERT INTO user(id, username,password,enabled) VALUES (25, 'vet9','vet9',TRUE);
INSERT INTO authority VALUES ('vet9','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (9, 25, 'José', 'Villegas', 'C/Monsalves, 4', 'Sevilla', '678940010', 'vet9@gmail.com', null);

-- SPECIALTIES

INSERT INTO specialty VALUES (1, 'radiology');
INSERT INTO specialty VALUES (2, 'surgery');
INSERT INTO specialty VALUES (3, 'dentistry');
INSERT INTO specialty VALUES (4, 'traumatology');
INSERT INTO specialty VALUES (5, 'cardiology');
INSERT INTO specialty VALUES (6, 'ophtalmology');
INSERT INTO specialty VALUES (7, 'neurology');
INSERT INTO specialty VALUES (8, 'anaesthetist');
INSERT INTO specialty VALUES (9, 'orthopedic');


-- VET_SPECIALTIES

INSERT INTO vet_specialties VALUES (1, 1);
INSERT INTO vet_specialties VALUES (1, 2);
INSERT INTO vet_specialties VALUES (2, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 4);
INSERT INTO vet_specialties VALUES (5, 5);
INSERT INTO vet_specialties VALUES (6, 6);
INSERT INTO vet_specialties VALUES (7, 7);
INSERT INTO vet_specialties VALUES (8, 8);
INSERT INTO vet_specialties VALUES (9, 9);


-- PET_TYPE

INSERT INTO pet_type VALUES (1, 'cat');
INSERT INTO pet_type VALUES (2, 'dog');
INSERT INTO pet_type VALUES (3, 'lizard');
INSERT INTO pet_type VALUES (4, 'snake');
INSERT INTO pet_type VALUES (5, 'bird');
INSERT INTO pet_type VALUES (6, 'hamster');

-- OWNERS

INSERT INTO user(id, username,password,enabled) VALUES (26, 'owner1','owner1',TRUE);
INSERT INTO authority VALUES ('owner1','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (1, 26, 'Encarnación', 'Padilla', 'C/Libertadores', 'Cadiz', '659813471', 'owner1@gmail.com', 1);

INSERT INTO user(id, username,password,enabled) VALUES (27, 'owner2','owner2',TRUE);
INSERT INTO authority VALUES ('owner2','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (2,27, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '608555102', 'owner2@gmail.com', 2);

INSERT INTO user(id, username,password,enabled) VALUES (28, 'owner3','owner3',TRUE);
INSERT INTO authority VALUES ('owner3','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (3,28, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '608555174', 'owner3@gmail.com', 3);

INSERT INTO user(id, username,password,enabled) VALUES (29, 'owner4','owner4',TRUE);
INSERT INTO authority VALUES ('owner4','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (4,29, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '608555873', 'owner4@gmail.com', 4);

INSERT INTO user(id, username,password,enabled) VALUES (30, 'owner5','owner5',TRUE);
INSERT INTO authority VALUES ('owner5','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (5,30, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '608555319', 'owner5@gmail.com', 5);

INSERT INTO user(id, username,password,enabled) VALUES (31, 'owner6','owner6',TRUE);
INSERT INTO authority VALUES ('owner6','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (6,31, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '608555275', 'owner6@gmail.com', 6);

INSERT INTO user(id, username,password,enabled) VALUES (32, 'owner7','owner7',TRUE);
INSERT INTO authority VALUES ('owner7','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (7,32, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '608555265', 'owner7@gmail.com', 7);

INSERT INTO user(id, username,password,enabled) VALUES (33, 'owner8','owner8',TRUE);
INSERT INTO authority VALUES ('owner8','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (8,33, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '608555538', 'owner8@gmail.com', 8);

INSERT INTO user(id, username,password,enabled) VALUES (34, 'owner9','owner9',TRUE);
INSERT INTO authority VALUES ('owner9','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (9,34, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '608557683', 'owner9@gmail.com', null);

INSERT INTO user(id, username,password,enabled) VALUES (35, 'owner10','owner10',TRUE);
INSERT INTO authority VALUES ('owner10','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (10,35, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '605559435', 'owner10@gmail.com', null);

INSERT INTO user(id, username,password,enabled) VALUES (36, 'owner11','owner11',TRUE);
INSERT INTO authority VALUES ('owner11','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (11,36, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '608555487', 'owner11@gmail.com', null);

-- PETS

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
INSERT INTO pet(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 11);

-- VISITS

INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (1, 1, '2020-06-09', 'visit aceptada',true,1);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (2, 2, '2020-06-09', 'visit rechazada',false,2);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (3, 3, '2020-06-09', 'visit aceptada',true,3);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (4, 4, '2020-06-09', 'visit pendiente',null,4);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (5, 5, '2020-06-09', 'visit pendiente',null,5);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (6, 6, '2020-06-09', 'visit pendiente',null,1);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (7, 7, '2020-06-09', 'visit pendiente',null,2);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (8, 8, '2020-06-09', 'visit pendiente',null,3);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (9, 6, '2020-06-09', 'visit pendiente',null,4);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (10, 10, '2020-06-09', 'visit pendiente',null,5);
INSERT INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (11, 11, '2020-06-09', 'visit pendiente',null,1);

-- STAYS

INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (1, '2020-06-09', '2020-06-12','stay pendiente', null,1,1);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (2, '2020-06-09', '2020-06-12','stay pendiente', null,2,2);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (3, '2020-06-09', '2020-06-13','stay rechazada', false,3,3);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (4, '2020-06-09', '2020-06-14','stay aceptada', true,4,4);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (5, '2020-06-09', '2020-06-15','stay rechazada', false,5,5);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (6, '2020-06-09', '2020-06-11','stay pendiente', null,1,6);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (7, '2020-06-09', '2020-06-12','stay pendiente', null,2,7);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (8, '2020-06-09', '2020-06-13','stay pendiente', null,3,8);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (9, '2020-06-09', '2020-06-14','stay pendiente', null,4,9);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (10, '2020-06-09', '2020-06-15','stay pendiente', null,5,10);
INSERT INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (11, '2020-06-09', '2020-06-11','stay pendiente', null,6,11);
