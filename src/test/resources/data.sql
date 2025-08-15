-- Insertion des rôles
INSERT INTO roles (id, name) VALUES 
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER');

-- Insertion des utilisateurs de test
INSERT INTO utilisateurs (username, password) VALUES 
('admin', 'admin123'),
('testuser', 'password123'),
('user2', 'user123');

-- Association des utilisateurs avec les rôles
-- testuser -> ROLE_USER
INSERT INTO utilisateur_role (user_id, role_id)
VALUES (2, 2);

-- admin -> ROLE_ADMIN, ROLE_USER
INSERT INTO utilisateur_role (user_id, role_id)
VALUES (1, 1);
INSERT INTO utilisateur_role (user_id, role_id)
VALUES (1, 2);

-- user2 -> ROLE_USER
INSERT INTO utilisateur_role (user_id, role_id)
VALUES (3, 2);

-- Insertion des aliments de test
-- 5 aliments pour l'admin

-- La pomme rouge sera renommée pomme rouge modifiée
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
VALUES ('Pomme Rouge', 52.0, 'g', 0.2, 0.03, 0.01, 0.01, 0.0, 0.3, 14.0, 10.0, 2.4, 0.0, 0.0,
    54.0, 0.017, 0.026, 0.091, 0.061, 0.041, 0.005, 0.0, 0.0, 0.0, 0.0, 4.6, 0.0, 0.18, 2.2, 0.0,
    0.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.027, 0.12, 0.0, 0.0, 5.0, 0.035, 0.0, 1.0, 11.0, 107.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.04,
    1);

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
VALUES ('Banane', 89.0, 'g', 0.3, 0.11, 0.03, 0.07, 0.0, 1.1, 23.0, 12.0, 2.6, 0.0, 0.0,
    25.0, 0.003, 0.031, 0.073, 0.665, 0.334, 0.367, 0.0, 20.0, 0.0, 0.0, 8.7, 0.0, 0.1, 0.5, 0.0,
    0.0, 0.0, 5.0, 0.0, 9.8, 0.0, 0.0, 0.078, 0.26, 0.0, 0.0, 27.0, 0.27, 0.0, 1.0, 22.0, 358.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.15,
    1);

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
VALUES ('Riz Blanc', 130.0, 'g', 0.3, 0.08, 0.09, 0.08, 0.0, 2.7, 28.0, 0.1, 0.4, 0.0, 0.0,
    0.0, 0.0, 0.07, 0.049, 1.6, 1.01, 0.164, 0.0, 8.0, 0.0, 0.0, 0.0, 0.0, 0.11, 0.1, 0.0,
    0.0, 0.0, 28.0, 0.0, 2.2, 0.0, 0.0, 0.22, 0.8, 0.0, 0.0, 25.0, 1.09, 0.0, 5.0, 115.0, 35.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.09,
    1);

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
VALUES ('Poulet Grillé', 165.0, 'g', 3.6, 1.0, 1.2, 0.8, 0.0, 31.0, 0.0, 0.0, 0.0, 0.36, 85.0,
    16.0, 0.005, 0.07, 0.21, 13.7, 1.36, 0.6, 0.0, 4.0, 0.0, 0.3, 0.0, 0.1, 0.27, 0.4, 0.0,
    0.0, 0.0, 15.0, 0.0, 82.0, 0.0, 0.0, 0.019, 0.89, 0.0, 0.0, 27.0, 0.017, 0.0, 363.0, 228.0, 256.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.54,
    1);

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
VALUES ('Saumon Atlantique', 208.0, 'g', 12.4, 3.1, 3.8, 3.6, 0.0, 22.1, 0.0, 0.0, 0.0, 0.59, 55.0,
    58.0, 0.019, 0.081, 0.155, 8.7, 1.66, 0.6, 0.0, 25.0, 0.0, 3.2, 11.0, 3.4, 1.22, 0.1, 0.0,
    0.0, 0.0, 12.0, 0.0, 91.0, 0.0, 0.0, 0.25, 0.31, 0.0, 0.0, 29.0, 0.016, 0.0, 384.0, 252.0, 628.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.45,
    1);

-- 3 aliments pour testuser
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
VALUES ('Carotte', 41.0, 'g', 0.2, 0.04, 0.01, 0.12, 0.0, 0.9, 10.0, 4.7, 2.8, 0.069, 0.0,
    835.0, 0.084, 0.066, 0.058, 0.983, 0.273, 0.138, 0.0, 19.0, 0.0, 0.0, 5.9, 0.0, 0.66, 13.2, 0.0,
    0.0, 0.0, 33.0, 0.0, 8.8, 0.0, 0.0, 0.045, 0.3, 0.0, 0.0, 12.0, 0.143, 0.0, 69.0, 35.0, 320.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.24,
    2);

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
VALUES ('Brocoli', 34.0, 'g', 0.4, 0.07, 0.01, 0.19, 0.0, 2.8, 7.0, 1.5, 2.6, 0.033, 0.0,
    31.0, 0.003, 0.071, 0.117, 0.639, 0.573, 0.175, 0.0, 63.0, 0.0, 0.0, 89.2, 0.0, 0.78, 101.6, 0.0,
    0.0, 0.0, 47.0, 0.0, 19.3, 0.0, 0.0, 0.049, 0.73, 0.0, 0.0, 21.0, 0.21, 0.0, 33.0, 66.0, 316.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.41,
    2);

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
VALUES ('Yaourt Nature', 59.0, 'g', 0.4, 0.1, 0.1, 0.0, 0.0, 10.0, 3.6, 4.7, 0.0, 0.046, 5.0,
    27.0, 0.003, 0.142, 0.278, 0.75, 0.39, 0.063, 0.0, 7.0, 0.0, 0.4, 0.0, 0.0, 0.06, 0.2, 0.0,
    0.0, 0.0, 110.0, 0.0, 15.2, 0.0, 0.0, 0.009, 0.04, 0.0, 0.0, 12.0, 0.004, 0.0, 46.0, 95.0, 141.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.59,
    2);

-- 2 aliments pour user2
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
VALUES ('Avocat', 160.0, 'g', 14.7, 2.1, 9.8, 1.8, 0.0, 2.0, 9.0, 0.7, 6.7, 0.007, 0.0,
    146.0, 0.015, 0.067, 0.13, 1.738, 1.389, 0.257, 0.0, 81.0, 0.0, 0.0, 10.0, 0.0, 2.07, 21.0, 0.0,
    0.0, 0.0, 12.0, 0.0, 14.2, 0.0, 0.0, 0.19, 0.55, 0.0, 0.0, 29.0, 0.142, 0.0, 7.0, 52.0, 485.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.64,
    3);

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
VALUES ('Pain Complet', 247.0, 'g', 3.4, 0.6, 0.5, 1.4, 0.0, 13.0, 41.0, 6.0, 7.0, 1.31, 0.0,
    0.0, 0.0, 0.416, 0.21, 4.33, 0.44, 0.22, 0.0, 44.0, 0.0, 0.0, 0.3, 0.0, 2.46, 1.9, 0.0,
    0.0, 0.0, 107.0, 0.0, 31.2, 0.0, 0.0, 0.234, 3.6, 0.0, 0.0, 75.0, 1.65, 0.0, 520.0, 216.0, 248.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.81,
    3);


-- Code barre pour Riz Blanc (admin)
INSERT INTO code_barre (code_barre, aliment_id, marque)
VALUES ('8901234567890', 3, 'Uncle Bens');

-- Code barre pour Saumon Atlantique (admin)
INSERT INTO code_barre (code_barre, aliment_id, marque)
VALUES ('7890123456789', 5, 'Océan Frais');

-- Code barre pour Yaourt Nature (testuser)
INSERT INTO code_barre (code_barre, aliment_id, marque)
VALUES ('5678901234567', 8, 'Danone');

--Insertion d'un aliment à supprimer
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI,
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol,
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8,
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn,
    user_id)
VALUES ('Banane à supprimer', 89.0, 'g', 0.3, 0.11, 0.03, 0.07, 0.0, 1.1, 23.0, 12.0, 2.6, 0.0, 0.0,
    25.0, 0.003, 0.031, 0.073, 0.665, 0.334, 0.367, 0.0, 20.0, 0.0, 0.0, 8.7, 0.0, 0.1, 0.5, 0.0,
    0.0, 0.0, 5.0, 0.0, 9.8, 0.0, 0.0, 0.078, 0.26, 0.0, 0.0, 27.0, 0.27, 0.0, 1.0, 22.0, 358.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.15,
    1);


-- Ajout des plats pour l'utilisateur testuser (id = 2)

-- Plat 1: Bol Protéiné au Poulet (3 portions)
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI,
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol,
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8,
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn,
    user_id)
VALUES ('Bol Protéiné au Poulet', 83.0, 'portion', 1.6, 0.37, 0.44, 0.32, 0.0, 12.6, 9.33, 3.23, 0.87, 0.132, 28.33,
    295.67, 0.032, 0.055, 0.101, 5.17, 0.66, 0.258, 0.0, 30.0, 0.0, 0.1, 32.5, 0.033, 0.31, 38.87, 0.0,
    0.0, 0.0, 31.33, 0.0, 32.6, 0.0, 0.0, 0.031, 0.43, 0.0, 0.0, 21.33, 0.083, 0.0, 142.0, 86.0, 261.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.66,
    2);

INSERT INTO plats (aliment_id, portions) VALUES (12, 3.0);

-- Recettes pour Bol Protéiné au Poulet (plat_id = 1)
-- 100g poulet grillé + 100g carotte + 50g riz blanc
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (1, 4, 100.0); -- Poulet Grillé
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (1, 6, 100.0); -- Carotte
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (1, 3, 50.0);  -- Riz Blanc

-- Plat 2: Salade de Saumon aux Légumes (2 portions)
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI,
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol,
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8,
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn,
    user_id)
VALUES ('Salade de Saumon aux Légumes', 111.0, 'portion', 6.35, 1.58, 1.915, 1.835, 0.0, 12.45, 4.5, 3.1, 1.3, 0.31, 30.0,
    463.0, 0.052, 0.076, 0.136, 5.19, 1.16, 0.388, 0.0, 57.0, 0.0, 1.6, 54.05, 1.7, 0.94, 57.8, 0.0,
    0.0, 0.0, 28.5, 0.0, 59.4, 0.0, 0.0, 0.162, 0.435, 0.0, 0.0, 28.0, 0.143, 0.0, 226.5, 139.0, 472.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.345,
    2);

INSERT INTO plats (aliment_id, portions) VALUES (13, 2.0);

-- Recettes pour Salade de Saumon aux Légumes (plat_id = 2)
-- 100g saumon + 100g carotte + 100g brocoli
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (2, 5, 100.0); -- Saumon Atlantique
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (2, 6, 100.0); -- Carotte
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (2, 7, 100.0); -- Brocoli

-- Plat 3: Riz au Poulet et Pomme (4 portions)
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI,
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol,
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8,
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn,
    user_id)
VALUES ('Riz au Poulet et Pomme', 73.25, 'portion', 1.0, 0.265, 0.31, 0.22, 0.0, 8.425, 14.25, 5.25, 0.8, 0.09, 21.25,
    138.5, 0.024, 0.049, 0.08, 3.68, 0.7, 0.211, 0.0, 3.25, 0.0, 0.075, 1.15, 0.025, 0.2225, 0.875, 0.0,
    0.0, 0.0, 11.25, 0.0, 21.05, 0.0, 0.0, 0.061, 0.475, 0.0, 0.0, 16.25, 0.285, 0.0, 92.25, 85.75, 71.25, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.4075,
    2);

INSERT INTO plats (aliment_id, portions) VALUES (14, 4.0);

-- Recettes pour Riz au Poulet et Pomme (plat_id = 3)
-- 100g poulet + 150g riz + 50g pomme rouge
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (3, 4, 100.0); -- Poulet Grillé
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (3, 3, 150.0); -- Riz Blanc
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (3, 1, 50.0);  -- Pomme Rouge

-- Plat 4: Smoothie Bowl Banane-Yaourt (1 portion)
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI,
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol,
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8,
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn,
    user_id)
VALUES ('Smoothie Bowl Banane-Yaourt', 74.0, 'portion', 0.35, 0.105, 0.065, 0.035, 0.0, 5.55, 13.3, 8.35, 1.3, 0.023, 2.5,
    13.5, 0.017, 0.052, 0.176, 0.71, 0.362, 0.215, 0.0, 13.5, 0.0, 0.2, 4.35, 0.0, 0.08, 0.35, 0.0,
    0.0, 0.0, 57.5, 0.0, 12.4, 0.0, 0.0, 0.044, 0.15, 0.0, 0.0, 19.5, 0.137, 0.0, 23.5, 58.5, 249.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.37,
    2);

INSERT INTO plats (aliment_id, portions) VALUES (15, 1.0);

-- Recettes pour Smoothie Bowl Banane-Yaourt (plat_id = 4)
-- 100g banane + 100g yaourt nature
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (4, 2, 100.0); -- Banane
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (4, 8, 100.0); -- Yaourt Nature

-- Plat 5: Mix Saumon-Pomme-Brocoli (2 portions)
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI,
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol,
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8,
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn,
    user_id)
VALUES ('Mix Saumon-Pomme-Brocoli', 104.5, 'portion', 6.4, 1.585, 1.905, 1.8, 0.0, 12.45, 5.5, 5.75, 1.3, 0.312, 27.5,
    44.5, 0.021, 0.078, 0.133, 4.675, 1.145, 0.388, 0.0, 44.0, 0.0, 1.6, 51.1, 1.7, 0.98, 51.9, 0.0,
    0.0, 0.0, 26.5, 0.0, 50.5, 0.0, 0.0, 0.147, 0.385, 0.0, 0.0, 25.0, 0.093, 0.0, 195.5, 139.0, 471.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.275,
    2);

INSERT INTO plats (aliment_id, portions) VALUES (16, 2.0);

-- Recettes pour Mix Saumon-Pomme-Brocoli (plat_id = 5)
-- 100g saumon + 50g pomme rouge + 100g brocoli
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (5, 5, 100.0); -- Saumon Atlantique
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (5, 1, 50.0);  -- Pomme Rouge
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (5, 7, 100.0); -- Brocoli

-- Ajout des plats pour l'utilisateur user2 (id = 3)

-- Plat 6: Toast Complet Saumon-Avocat (3 portions)
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI,
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol,
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8,
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn,
    user_id)
VALUES ('Toast Complet Saumon-Avocat', 205.0, 'portion', 14.37, 2.93, 6.77, 2.87, 0.0, 16.03, 16.67, 2.23, 4.33, 0.973, 28.33,
    101.0, 0.094, 0.122, 0.142, 5.335, 1.505, 0.423, 0.0, 41.33, 0.0, 1.83, 7.13, 2.133, 1.497, 10.7, 0.0,
    0.0, 0.0, 39.67, 0.0, 41.73, 0.0, 0.0, 0.222, 1.47, 0.0, 0.0, 51.0, 0.551, 0.0, 301.0, 144.0, 520.33, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.85,
    3);

INSERT INTO plats (aliment_id, portions) VALUES (17, 3.0);

-- Recettes pour Toast Complet Saumon-Avocat (plat_id = 6)
-- 100g saumon + 100g avocat + 100g pain complet
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (6, 5, 100.0);  -- Saumon Atlantique
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (6, 9, 100.0);  -- Avocat
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (6, 10, 100.0); -- Pain Complet

-- Plat 7: Bowl Tropical Poulet-Banane (2 portions)
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI,
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol,
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8,
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn,
    user_id)
VALUES ('Bowl Tropical Poulet-Banane', 190.5, 'portion', 6.45, 1.555, 2.115, 1.435, 0.0, 24.15, 34.5, 18.0, 3.9, 0.543, 63.5,
    20.5, 0.051, 0.105, 0.282, 10.175, 1.35, 0.967, 0.0, 14.0, 0.0, 0.45, 8.7, 0.15, 0.38, 0.9, 0.0,
    0.0, 0.0, 22.5, 0.0, 61.9, 0.0, 0.0, 0.097, 1.15, 0.0, 0.0, 40.5, 0.287, 0.0, 271.5, 187.5, 486.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.345,
    3);

INSERT INTO plats (aliment_id, portions) VALUES (18, 2.0);

-- Recettes pour Bowl Tropical Poulet-Banane (plat_id = 7)
-- 150g poulet + 150g banane + 100g avocat
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (7, 4, 150.0); -- Poulet Grillé
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (7, 2, 150.0); -- Banane
INSERT INTO recettes (plat_id, aliment_id, quantite) VALUES (7, 9, 100.0); -- Avocat