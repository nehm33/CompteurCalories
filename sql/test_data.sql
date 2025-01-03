insert into roles(name) values ('ROLE_ADMIN');
insert into roles(name) values ('ROLE_USER');
insert into utilisateurs(username, password) values ('admin', 'admin');
insert into utilisateurs(username, password) values ('test', 'test');
insert into utilisateur_role values (1, 1);
insert into utilisateur_role values (1, 2);
insert into utilisateur_role values (2, 2);

INSERT INTO aliments (nom, calories, glucides, sucre, fibres, mat_gras, proteines, vit_c, vit_b6, ca, fe, mg, na, k, user_id) VALUES ('oignon', 40, 9, 4.2, 1.7, 0.1, 1.1, 7.4, 0.1, 23, 0.2, 10, 4, 146, 1);
INSERT INTO aliments (nom, calories, glucides, fibres, mat_gras, proteines, pro_vit_a, vit_b1, vit_b2, vit_b3, vit_b5, vit_b6, vit_b9, vit_c, vit_e, vit_k1, ca, cl, cu, fe, i, mg, mn, p, k, se, na, zn, user_id) VALUES ('tomate', 19.3, 2.5, 1.2, 0.26, 0.86, 449, 0.039, 0.019, 0.65, 0.21, 0.082, 22.7, 15.5, 0.66, 7.9, 8.14, 51, 0.029, 0.12, 0.2, 10.1, 0.066, 26.6, 256, 10, 3.22, 0.087, 1);
INSERT INTO aliments (nom, calories, glucides, sucre, fibres, mat_gras, proteines, pro_vit_a, vit_b1, vit_b2, vit_b3, vit_b5, vit_b6, vit_b9, vit_b12, vit_c, vit_e, vit_k1, ca, cl, cu, fe, i, mg, mn, p, k, se, na, zn, user_id) VALUES ('pomme de terre', 93.2, 17.2, 1.05, 1.96, 1.37, 2.01, 3.68, 0.07, 0.013, 1.08, 0.46, 0.24, 18.4, 0.03, 5.05, 0.12, 1.34, 9.62, 150, 0.095, 0.43, 6.78, 20.7, 0.12, 43.7, 450, 7.07, 48.8, 0.24, 1);
INSERT INTO aliments (nom, calories, glucides, mat_gras, mat_gras_S, mat_gras_MI, mat_gras_PI, proteines, vit_e, vit_k1, ca, fe, na, k, user_id) VALUES ('huile d''olive', 900, 0, 100, 14, 75.2, 6.9, 0, 25, 47.8, 1, 0.6, 2, 1, 1);
INSERT INTO aliments (nom, calories, glucides, mat_gras, mat_gras_S, mat_gras_MI, mat_gras_PI, proteines, cholesterol, vit_a, vit_b1, vit_b2, vit_b3, vit_b5, vit_b6, vit_b9, vit_b12, vit_c, vit_d, vit_e, vit_k1, ca, fe, i, mg, p, k, se, na, zn, user_id) VALUES ('blanc de poulet', 121, 0, 1.8, 0.6, 0.7, 0.4, 26.2, 70.4, 4.7, 0.2, 0.1, 11.1, 1.4, 0.5, 8, 0.3, 2.6, 0.3, 0.3, 0.3, 13.6, 0.4, 5, 26.5, 480, 290, 11, 415, 0.8, 2);
INSERT INTO aliments (nom, calories, glucides, sucre, fibres, mat_gras, proteines, vit_c, vit_b6, ca, fe, mg, na, k, user_id) VALUES ('oignon2', 40, 9, 4.2, 1.7, 0.1, 1.1, 7.4, 0.1, 23, 0.2, 10, 4, 146, 3);

INSERT INTO plats (nom, portions) VALUES ('pommes de terre sautées', 1);
INSERT INTO plats (nom, portions) VALUES ('pâte tomate', 2);
INSERT INTO plats (nom, portions) VALUES ('poulet grillé', 1);
INSERT INTO plats (nom, portions) VALUES ('pommes de terre sautées 2', 1);

INSERT INTO recettes VALUES (1, 3, 500);
INSERT INTO recettes VALUES (1, 4, 100);
INSERT INTO recettes VALUES (2, 4, 100);
INSERT INTO recettes VALUES (2, 1, 250);
INSERT INTO recettes VALUES (2, 2, 500);
INSERT INTO recettes VALUES (3, 4, 100);
INSERT INTO recettes VALUES (3, 5, 200);

INSERT INTO composition_journal_plat VALUES (1, '2023-12-25', 2);
INSERT INTO composition_journal_plat VALUES (3, '2023-12-25', 3);
INSERT INTO composition_journal_plat VALUES (1, '2023-12-22', 1);
INSERT INTO composition_journal_plat VALUES (2, '2023-12-22', 1);

INSERT INTO composition_journal_aliment VALUES (1, '2023-12-25', 2);
INSERT INTO composition_journal_aliment VALUES (2, '2023-12-25', 4);

INSERT INTO code_barre VALUES ('1233456789098', 3, 'fleury michel');
INSERT INTO code_barre VALUES ('3211456789045', 4, 'doree');