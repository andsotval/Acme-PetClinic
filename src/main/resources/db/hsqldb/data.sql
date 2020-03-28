
-- ADMIN

-- One admin user, named admin with password admin and authority admin
INSERT INTO user(id, username,password,enabled) VALUES (1,'admin','admin',TRUE);
INSERT INTO authority VALUES ('admin','admin');
INSERT INTO administrator(id, user_id) VALUES (1, 1);

-- MANAGERS

-- One manager user, named manager1 with password manager1
INSERT INTO user(id, username,password,enabled) VALUES (2, 'manager1','manager1',TRUE);
INSERT INTO authority VALUES ('manager1','manager');
INSERT INTO manager(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (1, 2, 'pepito', 'pepi', 'C/ Pepi numero 1', 'Sevilla', '600000000', 'pepi@gmail.com');

-- One manager user, named manager2 with password manager2
INSERT INTO user(id, username,password,enabled) VALUES (8, 'manager2','manager2',TRUE);
INSERT INTO authority VALUES ('manager2','manager');
INSERT INTO manager(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (2, 8, 'pedro', 'suarez', 'C/ Pedro numero 1', 'Sevilla', '600012345', 'pedro@gmail.com');

-- OWNERS

-- One owner user, named owner1 with password owner1
INSERT INTO user(id, username,password,enabled) VALUES (3, 'owner1','owner1',TRUE);
INSERT INTO authority VALUES ('owner1','owner');
INSERT INTO owner(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (1, 3, 'encarni', 'carni', 'C/ Carni numero 1', 'Cadiz', '600000001', 'carni@gmail.com');

-- VETS

-- One vet user, named vet1 with password vet1
INSERT INTO user(id, username,password,enabled) VALUES (4, 'vet1','vet1',TRUE);
INSERT INTO authority VALUES ('vet1','veterinarian');
INSERT INTO vet(id, user_id, first_name, last_name, address, city, telephone, mail) VALUES (1, 4, 'juanito', 'juan', 'C/ Juan numero 1', 'Malaga', '600000002', 'juan@gmail.com');

-- PROVIDERS

-- One Provider user, named provider1 with password provider1
INSERT INTO user(id, username,password,enabled) VALUES (5, 'provider1','provider1',TRUE);
INSERT INTO authority VALUES ('provider1','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (1, 5, 'miguel', 'patón', 'C/ Miguel numero 1', 'Madrid', '600000009', 'miguel@gmail.com', 1);

-- One Provider user, named provider2 with password provider2
INSERT INTO user(id, username,password,enabled) VALUES (6, 'provider2','provider2',TRUE);
INSERT INTO authority VALUES ('provider2','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (2, 6, 'mario', 'patón', 'C/ Mario numero 2', 'Madrid', '600000019', 'mario@gmail.com', 1);

-- One Provider user, named provider3 with password provider3
INSERT INTO user(id, username,password,enabled) VALUES (7, 'provider3','provider3',TRUE);
INSERT INTO authority VALUES ('provider3','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (3, 7, 'carlos', 'gutierrez', 'C/ Carlos numero 1', 'Cuenca', '600001239', 'carlos@gmail.com', null);

-- One Provider user, named provider3 with password provider3
INSERT INTO user(id, username,password,enabled) VALUES (9, 'provider4','provider4',TRUE);
INSERT INTO authority VALUES ('provider4','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (4, 9, 'pepe', 'gutierrez', 'C/ pepe numero 1', 'Londres', '600001200', 'pepe@gmail.com', null);

-- One Provider user, named provider3 with password provider3
INSERT INTO user(id, username,password,enabled) VALUES (10, 'provider5','provider5',TRUE);
INSERT INTO authority VALUES ('provider5','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (5, 10, 'jose', 'gutierrez', 'C/ jose numero 1', 'Paris', '600001211', 'jose@gmail.com', null);

-- One Provider user, named provider3 with password provider3
INSERT INTO user(id, username,password,enabled) VALUES (11, 'provider6','provider6',TRUE);
INSERT INTO authority VALUES ('provider6','provider');
INSERT INTO provider(id, user_id, first_name, last_name, address, city, telephone, mail, manager_id) VALUES (6, 11, 'maria', 'ortiz', 'C/ maria numero 1', 'Pamplona', '600007788', 'maria@gmail.com', null);

