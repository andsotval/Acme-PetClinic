-- ADMIN

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (1,'admin','admin',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (1,'admin','admin');
INSERT IGNORE INTO administrator(id, user_id) VALUES (1, 1);

-- MANAGERS

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (2, 'manager1','manager1',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (2,'manager1','manager');
INSERT IGNORE INTO manager(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (1, 2, 'Alfonso', 'Nuñez', 'C/San Alberto, 5', 'Sevilla', '656719435', 'manager1@gmail.com');

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (3, 'manager2','manager2',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (3,'manager2','manager');
INSERT IGNORE INTO manager(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (2, 3, 'Pedro', 'Suarez', 'Av. de Andalucia, 247, Portal C, 1ºA', 'Vigo', '694602736', 'manager2@gmail.com');

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (4, 'manager3','manager3',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (4,'manager3','manager');
INSERT IGNORE INTO manager(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (3, 4, 'Jimeno', 'Balboa', 'C/San Juan, 23', 'Málaga', '656096435', 'manager3@gmail.com');

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (5, 'manager4','manager4',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (5,'manager4','manager');
INSERT IGNORE INTO manager(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (4, 5, 'Marisa', 'García', 'C/Destino, 1', 'Sevilla', '600122135', 'manager4@gmail.com');

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (6, 'manager5','manager5',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (6,'manager5','manager');
INSERT IGNORE INTO manager(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (5, 6, 'Paco', 'Sánchez', 'C/Fin, 99 1ºA', 'Huelva', '600125555', 'manager5@gmail.com');


-- PROVIDERS

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (7, 'provider1','provider1',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (7,'provider1','provider');
INSERT IGNORE INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (1, 7, 'Miguel', 'Patón', 'C/Obreros, 1', 'Madrid', '6453097624', 'provider1@gmail.com', 1);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (8, 'provider2','provider2',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (8,'provider2','provider');
INSERT IGNORE INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (2, 8, 'Mario', 'Patón', 'C/Marconi, 14', 'Madrid', '645031698', 'provider2@gmail.com', 2);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (9, 'provider3','provider3',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (9,'provider3','provider');
INSERT IGNORE INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (3, 9, 'Carlos', 'Gutierrez', 'C/Pedro Laín, 32', 'Dos Hermanas', '600001239', 'provider3@gmail.com', null);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (10, 'provider4','provider4',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (10,'provider4','provider');
INSERT IGNORE INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (4, 10, 'Pepe', 'Arquellada', 'C/Trafalgar Square, 58', 'Londres', '600001200', 'provider4@gmail.com', null);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (11, 'provider5','provider5',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (11,'provider5','provider');
INSERT IGNORE INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (5, 11, 'Jose', 'Redondo', 'C/La Mina, 23', 'Alcorcón', '642791211', 'provider5@gmail.com', null);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (12, 'provider6','provider6',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (12,'provider6','provider');
INSERT IGNORE INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (6, 12, 'Maria', 'Ortíz', 'C/Tajo, 43', 'Pamplona', '600007788', 'provider6@gmail.com', null);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (13, 'provider7','provider7',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (13,'provider7','provider');
INSERT IGNORE INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (7, 13, 'Luisa', 'Sanchez', 'Plaza de la Constitución, 143 Bajo A', 'Cuenca', '682340678', 'provider7@gmail.com', null);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (14, 'provider8','provider8',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (14,'provider8','provider');
INSERT IGNORE INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (8, 14, 'Agustín', 'Rebolledo', 'C/Cardador, 13', 'Caceres', '694031457', 'provider8@gmail.com', null);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (15, 'provider9','provider9',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (15,'provider9','provider');
INSERT IGNORE INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (9, 15, 'Antonio', 'Montaño', 'C/Doñana, 20', 'Torrent', '698360154', 'provider9@gmail.com', null);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (16, 'provider10','provider10',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (16,'provider10','provider');
INSERT IGNORE INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (10, 16, 'Isabel', 'Carmona', 'C/Desengaño, 21', 'Madrid', '678001234', 'provider10@gmail.com', null);


-- PRODUCTS

INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (1, 'Comida para perros castrados', 15.95, 3.07, true, 3);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (2, 'Comida para gatos castrados', 17.95, 3.48, true, 3);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (3, 'Hueso para perros', 8.95, 1.56, true, 3);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (4, 'Bisturí', 50.99, 9.79, true, 1);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (5, 'Guante', 5.95, 0.97, true, 1);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (6, 'Mascarilla', 7.50, 1.45, false, 1);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (7, 'Jeringuilla', 9.90, 1.93, true, 1);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (8, 'Jeringuilla', 12.90, 2.68, false, 2);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (9, 'Bisturís', 44.95, 9.32, true, 2);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (10, 'Juguete para gato', 2.95, 0.77, true, 4);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (11, 'Arenero para gato', 5.80, 1.63, true, 4);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (12, 'Correa para perro', 4.95, 1.26, true, 5);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (13, 'Champú para perro', 5.95, 1.69, false, 5);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (14, 'Pipeta para gato', 19.99, 3.87, true, 5);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (15, 'Jaula para pajaro', 33.35, 6.62, false, 6);
INSERT IGNORE INTO product(id, name, price, tax, is_available, provider_id) VALUES (16, 'Pecera', 81.60, 16.24, true, 6);

-- ORDER

INSERT IGNORE INTO purchase_order(id, order_date, is_accepted, manager_id) VALUES (1, '2020-04-18', true, 1);
INSERT IGNORE INTO purchase_order(id, order_date, is_accepted, manager_id) VALUES (2, '2020-04-19', false, 1);
INSERT IGNORE INTO purchase_order(id, order_date, is_accepted, manager_id) VALUES (3, '2020-04-20', false, 1);
INSERT IGNORE INTO purchase_order(id, order_date, is_accepted, manager_id) VALUES (4, '2020-04-18', false, 1);
INSERT IGNORE INTO purchase_order(id, order_date, is_accepted, manager_id) VALUES (5, '2020-04-08', true, 2);
INSERT IGNORE INTO purchase_order(id, order_date, is_accepted, manager_id) VALUES (6, '2020-03-11', false, 2);

-- PRODUCTS_ORDER

INSERT IGNORE INTO product_order(id, price, tax, amount, product_id, order_id) VALUES (1, 50.99, 9.79, 2, 4, 1);
INSERT IGNORE INTO product_order(id, price, tax, amount, product_id, order_id) VALUES (2, 5.95, 0.97, 17, 5, 1);
INSERT IGNORE INTO product_order(id, price, tax, amount, product_id, order_id) VALUES (3, 9.90, 1.93, 9, 7, 1);

INSERT IGNORE INTO product_order(id, price, tax, amount, product_id, order_id) VALUES (4, 50.99, 9.79, 5, 4, 2);
INSERT IGNORE INTO product_order(id, price, tax, amount, product_id, order_id) VALUES (5, 5.95, 0.97, 40, 5, 2);

INSERT IGNORE INTO product_order(id, price, tax, amount, product_id, order_id) VALUES (6, 9.90, 1.93, 25, 7, 3);
INSERT IGNORE INTO product_order(id, price, tax, amount, product_id, order_id) VALUES (7, 50.99, 9.79, 10, 4, 3);
INSERT IGNORE INTO product_order(id, price, tax, amount, product_id, order_id) VALUES (8, 5.95, 0.97, 80, 5, 3);

INSERT IGNORE INTO product_order(id, price, tax, amount, product_id, order_id) VALUES (9, 9.90, 1.93, 15, 7, 4);

INSERT IGNORE INTO product_order(id, price, tax, amount, product_id, order_id) VALUES (10, 12.90, 2.68, 5, 8, 5);
INSERT IGNORE INTO product_order(id, price, tax, amount, product_id, order_id) VALUES (11, 44.95, 9.32, 15, 9, 5);

INSERT IGNORE INTO product_order(id, price, tax, amount, product_id, order_id) VALUES (12, 44.95, 9.32, 18, 9, 6);

-- CLINICS

INSERT IGNORE INTO clinic(id,name,address,city,telephone,manager_id) VALUES (1, 'Clinic1', 'C/ Jaén, 98', 'Granada','954863547',1);
INSERT IGNORE INTO clinic(id,name,address,city,telephone,manager_id) VALUES (2, 'Clinic2', 'Paseo de la Castellana, 119', 'Madrid','912654456',2);
INSERT IGNORE INTO clinic(id,name,address,city,telephone,manager_id) VALUES (3, 'Clinic3', 'Avenida de los lobos, 3', 'Barcelona','966337858',3);
INSERT IGNORE INTO clinic(id,name,address,city,telephone,manager_id) VALUES (4, 'Clinic4', 'C/ José Palermo, 9', 'Málaga','900252029',4);
INSERT IGNORE INTO clinic(id,name,address,city,telephone,manager_id) VALUES (5, 'Clinic5', 'C/ Almanzara', 'Sevilla','989678985',5);
INSERT IGNORE INTO clinic(id,name,address,city,telephone,manager_id) VALUES (6, 'Clinic6', 'Avenida de Andalucía, 22', 'Huelva','922305704',null);
INSERT IGNORE INTO clinic(id,name,address,city,telephone,manager_id) VALUES (7, 'Clinic7', 'C/ Feria, 11', 'Sevilla','900686743',null);
INSERT IGNORE INTO clinic(id,name,address,city,telephone,manager_id) VALUES (8, 'Clinic8', 'C/Laraña, 53', 'Sevilla','948038444',null);

-- VETS

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (17, 'vet1','vet1',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (17,'vet1','veterinarian');
INSERT IGNORE INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (1, 17, 'Juan', 'Cardona', 'C/Ordesa, 7', 'Malaga', '670336994', 'vet1@gmail.com', 1);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (18, 'vet2','vet2',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (18,'vet2','veterinarian');
INSERT IGNORE INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (2, 18, 'Paula', 'Barrios', 'Av. de la Constitución, 54, 1ºE', 'Roma', '670024855', 'vet2@gmail.com', 2);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (19, 'vet3','vet3',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (19,'vet3','veterinarian');
INSERT IGNORE INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (3, 19, 'Raquel', 'Vega', 'Plaza Bobinadora, 36', 'Copenhague', '677750226', 'vet3@gmail.com', 3);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (20, 'vet4','vet4',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (20,'vet4','veterinarian');
INSERT IGNORE INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (4, 20, 'Jorge', 'Ruiz', 'C/Botica', 'Moscú', '633364795', 'vet4@gmail.com', 4);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (21, 'vet5','vet5',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (21,'vet5','veterinarian');
INSERT IGNORE INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (5, 21, 'Carmen', 'Ponce', 'Av. de España, 87, Portal A, 5ºC', 'La Rioja', '600142733', 'vet5@gmail.com', 5);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (22, 'vet6','vet6',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (22,'vet6','veterinarian');
INSERT IGNORE INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (6, 22, 'Lourdes', 'Villegas', 'C/Andalucia, 34', 'Bilbao', '679123162', 'vet6@gmail.com', null);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (23, 'vet7','vet7',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (23,'vet7','veterinarian');
INSERT IGNORE INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (7, 23, 'Daniel', 'Valdivia', 'C/Ruiz Gijon, 1', 'Granada', '678945210', 'vet7@gmail.com', null);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (24, 'vet8','vet8',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (24,'vet8','veterinarian');
INSERT IGNORE INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (8, 24, 'Daniela', 'Guitérrez', 'C/Patricia, 43', 'Sevilla', '671245510', 'vet8@gmail.com', null);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (25, 'vet9','vet9',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (25,'vet9','veterinarian');
INSERT IGNORE INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (9, 25, 'José', 'Villegas', 'C/Monsalves, 4', 'Sevilla', '678940010', 'vet9@gmail.com', null);

-- SPECIALTIES

INSERT IGNORE INTO specialty VALUES (1, 'radiology', true);
INSERT IGNORE INTO specialty VALUES (2, 'surgery', true);
INSERT IGNORE INTO specialty VALUES (3, 'dentistry', true);
INSERT IGNORE INTO specialty VALUES (4, 'traumatology', true);
INSERT IGNORE INTO specialty VALUES (5, 'cardiology', true);
INSERT IGNORE INTO specialty VALUES (6, 'ophtalmology', true);
INSERT IGNORE INTO specialty VALUES (7, 'neurology', true);
INSERT IGNORE INTO specialty VALUES (8, 'anaesthetist', false);
INSERT IGNORE INTO specialty VALUES (9, 'orthopedic', false);


-- VET_SPECIALTIES

INSERT IGNORE INTO vet_specialties VALUES (1, 1);
INSERT IGNORE INTO vet_specialties VALUES (1, 2);
INSERT IGNORE INTO vet_specialties VALUES (2, 2);
INSERT IGNORE INTO vet_specialties VALUES (3, 3);
INSERT IGNORE INTO vet_specialties VALUES (4, 4);
INSERT IGNORE INTO vet_specialties VALUES (5, 5);
INSERT IGNORE INTO vet_specialties VALUES (6, 6);
INSERT IGNORE INTO vet_specialties VALUES (7, 7);
INSERT IGNORE INTO vet_specialties VALUES (8, 8);
INSERT IGNORE INTO vet_specialties VALUES (9, 9);


-- PET_TYPE

INSERT IGNORE INTO pet_type VALUES (1, 'cat', true);
INSERT IGNORE INTO pet_type VALUES (2, 'dog', true);
INSERT IGNORE INTO pet_type VALUES (3, 'lizard', true);
INSERT IGNORE INTO pet_type VALUES (4, 'snake', true);
INSERT IGNORE INTO pet_type VALUES (5, 'bird', true);
INSERT IGNORE INTO pet_type VALUES (6, 'hamster', true);
INSERT IGNORE INTO pet_type VALUES (7, 'rabbit', false);


-- OWNERS

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (26, 'owner1','owner1',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (26,'owner1','owner');
INSERT IGNORE INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (1, 26, 'Encarnación', 'Padilla', 'C/Libertadores', 'Cadiz', '659813471', 'owner1@gmail.com', 1);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (27, 'owner2','owner2',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (27,'owner2','owner');
INSERT IGNORE INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (2,27, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '608555102', 'owner2@gmail.com', 2);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (28, 'owner3','owner3',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (28,'owner3','owner');
INSERT IGNORE INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (3,28, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '608555174', 'owner3@gmail.com', 3);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (29, 'owner4','owner4',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (29,'owner4','owner');
INSERT IGNORE INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (4,29, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '608555873', 'owner4@gmail.com', 4);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (30, 'owner5','owner5',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (30,'owner5','owner');
INSERT IGNORE INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (5,30, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '608555319', 'owner5@gmail.com', 5);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (31, 'owner6','owner6',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (31,'owner6','owner');
INSERT IGNORE INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (6,31, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '608555275', 'owner6@gmail.com', 6);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (32, 'owner7','owner7',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (32,'owner7','owner');
INSERT IGNORE INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (7,32, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '608555265', 'owner7@gmail.com', 7);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (33, 'owner8','owner8',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (33,'owner8','owner');
INSERT IGNORE INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (8,33, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '608555538', 'owner8@gmail.com', 8);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (34, 'owner9','owner9',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (34,'owner9','owner');
INSERT IGNORE INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (9,34, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '608557683', 'owner9@gmail.com', null);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (35, 'owner10','owner10',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (35,'owner10','owner');
INSERT IGNORE INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (10,35, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '605559435', 'owner10@gmail.com', null);

INSERT IGNORE INTO user_account(id, username,password,enabled) VALUES (36, 'owner11','owner11',TRUE);
INSERT IGNORE INTO authority(id,username,authority) VALUES (36,'owner11','owner');
INSERT IGNORE INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail, clinic_id) VALUES (11,36, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '608555487', 'owner11@gmail.com', null);

-- PETS

INSERT IGNORE INTO pet(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT IGNORE INTO pet(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT IGNORE INTO pet(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT IGNORE INTO pet(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT IGNORE INTO pet(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT IGNORE INTO pet(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT IGNORE INTO pet(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT IGNORE INTO pet(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT IGNORE INTO pet(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT IGNORE INTO pet(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT IGNORE INTO pet(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT IGNORE INTO pet(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT IGNORE INTO pet(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 11);

-- VISITS

INSERT IGNORE INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (1, 1, '2020-08-09 09:30:00', 'visit aceptada',true,1);
INSERT IGNORE INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (2, 2, '2020-06-12 12:30:00', 'visit rechazada',false,2);
INSERT IGNORE INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (3, 3, '2020-12-23 17:00:00', 'visit aceptada',true,3);
INSERT IGNORE INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (4, 4, '2020-06-09 11:30:00', 'visit pendiente',null,4);
INSERT IGNORE INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (5, 5, '2020-10-10 16:30:00', 'visit pendiente',null,5);
INSERT IGNORE INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (6, 6, '2020-11-05 13:00:00', 'visit pendiente',null,1);
INSERT IGNORE INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (7, 7, '2020-12-13 13:30:00', 'visit pendiente',null,2);
INSERT IGNORE INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (8, 8, '2020-06-01 19:00:00', 'visit pendiente',null,3);
INSERT IGNORE INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (9, 6, '2020-07-19 18:30:00', 'visit pendiente',null,4);
INSERT IGNORE INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (10, 10, '2020-07-14 10:00:00', 'visit pendiente',null,5);
INSERT IGNORE INTO visit(id,pet_id,visit_date,description,is_accepted,clinic_id) VALUES (11, 11, '2020-06-17 08:30:00', 'visit pendiente',null,1);

-- STAYS

INSERT IGNORE INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (1, '2020-06-09', '2020-06-12','stay pendiente', null,1,1);
INSERT IGNORE INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (2, '2020-06-09', '2020-06-12','stay pendiente', null,2,2);
INSERT IGNORE INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (3, '2020-06-09', '2020-06-13','stay rechazada', false,3,3);
INSERT IGNORE INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (4, '2020-06-09', '2020-06-14','stay aceptada', true,4,4);
INSERT IGNORE INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (5, '2020-06-09', '2020-06-15','stay rechazada', false,5,5);
INSERT IGNORE INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (6, '2020-06-09', '2020-06-11','stay pendiente', null,1,6);
INSERT IGNORE INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (7, '2020-06-09', '2020-06-12','stay pendiente', null,2,7);
INSERT IGNORE INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (8, '2020-06-09', '2020-06-13','stay pendiente', null,3,8);
INSERT IGNORE INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (9, '2020-06-09', '2020-06-14','stay pendiente', null,4,9);
INSERT IGNORE INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (10, '2020-06-09', '2020-06-15','stay pendiente', null,5,10);
INSERT IGNORE INTO stay(id,start_date,finish_date,description,is_accepted,clinic_id, pet_id) VALUES (11, '2020-06-09', '2020-06-11','stay pendiente', null,6,11);


-- SUGGESTIONS
INSERT IGNORE INTO suggestion(id,name,description,is_read,created,is_trash,is_available,user_id) VALUES (1, 'Mas clínicas', 'Estaría muy bien disponer de mas clínicas, no encuentro ninguna en mi zona', true, '2020-04-11 12:30:11',false, true, 26);
INSERT IGNORE INTO suggestion(id,name,description,is_read,created,is_trash,is_available,user_id) VALUES (2, 'Mas veterinarios', 'Estaría muy bien disponer de mas veterinarios, no me gusta ninguno', true, '2020-04-18 16:30:41',true, true, 27);
INSERT IGNORE INTO suggestion(id,name,description,is_read,created,is_trash,is_available,user_id) VALUES (3, 'Mas proveedores', 'Estaría muy bien disponer de mas proveedores, son todos muy caros', false, '2020-04-20 19:15:35',false, true, 2);
INSERT IGNORE INTO suggestion(id,name,description,is_read,created,is_trash,is_available,user_id) VALUES (4, 'Intervalo de tiempo en estancias', 'Estaría muy bien poder dejar mi mascota mas tiempo en la clínica', false, '2020-04-19 21:09:24',false, true, 26);
