-- Insertion des rôles
INSERT INTO roles (id, name) VALUES 
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER');

-- Insertion des utilisateurs de test
INSERT INTO utilisateurs (username, password) VALUES 
('testuser', 'password123'),
('admin', 'admin123'),
('user2', 'user123');

-- Association des utilisateurs avec les rôles
-- testuser -> ROLE_USER
INSERT INTO utilisateur_role (user_id, role_id)
SELECT u.id, 2 FROM utilisateurs u WHERE u.username = 'testuser';

-- admin -> ROLE_ADMIN  
INSERT INTO utilisateur_role (user_id, role_id)
SELECT u.id, 1 FROM utilisateurs u WHERE u.username = 'admin';

-- user2 -> ROLE_USER
INSERT INTO utilisateur_role (user_id, role_id)
SELECT u.id, 2 FROM utilisateurs u WHERE u.username = 'user2';

-- Insertion des aliments de test
-- 5 aliments pour l'admin
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
SELECT 'Pomme Rouge', 52.0, 'g', 0.2, 0.03, 0.01, 0.01, 0.0, 0.3, 14.0, 10.0, 2.4, 0.0, 0.0,
    54.0, 0.017, 0.026, 0.091, 0.061, 0.041, 0.005, 0.0, 0.0, 0.0, 0.0, 4.6, 0.0, 0.18, 2.2, 0.0,
    0.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.027, 0.12, 0.0, 0.0, 5.0, 0.035, 0.0, 1.0, 11.0, 107.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.04,
    u.id 
FROM utilisateurs u WHERE u.username = 'admin';

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
SELECT 'Banane', 89.0, 'g', 0.3, 0.11, 0.03, 0.07, 0.0, 1.1, 23.0, 12.0, 2.6, 0.0, 0.0,
    25.0, 0.003, 0.031, 0.073, 0.665, 0.334, 0.367, 0.0, 20.0, 0.0, 0.0, 8.7, 0.0, 0.1, 0.5, 0.0,
    0.0, 0.0, 5.0, 0.0, 9.8, 0.0, 0.0, 0.078, 0.26, 0.0, 0.0, 27.0, 0.27, 0.0, 1.0, 22.0, 358.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.15,
    u.id 
FROM utilisateurs u WHERE u.username = 'admin';

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
SELECT 'Riz Blanc', 130.0, 'g', 0.3, 0.08, 0.09, 0.08, 0.0, 2.7, 28.0, 0.1, 0.4, 0.0, 0.0,
    0.0, 0.0, 0.07, 0.049, 1.6, 1.01, 0.164, 0.0, 8.0, 0.0, 0.0, 0.0, 0.0, 0.11, 0.1, 0.0,
    0.0, 0.0, 28.0, 0.0, 2.2, 0.0, 0.0, 0.22, 0.8, 0.0, 0.0, 25.0, 1.09, 0.0, 5.0, 115.0, 35.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.09,
    u.id 
FROM utilisateurs u WHERE u.username = 'admin';

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
SELECT 'Poulet Grillé', 165.0, 'g', 3.6, 1.0, 1.2, 0.8, 0.0, 31.0, 0.0, 0.0, 0.0, 0.36, 85.0,
    16.0, 0.005, 0.07, 0.21, 13.7, 1.36, 0.6, 0.0, 4.0, 0.0, 0.3, 0.0, 0.1, 0.27, 0.4, 0.0,
    0.0, 0.0, 15.0, 0.0, 82.0, 0.0, 0.0, 0.019, 0.89, 0.0, 0.0, 27.0, 0.017, 0.0, 363.0, 228.0, 256.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.54,
    u.id 
FROM utilisateurs u WHERE u.username = 'admin';

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
SELECT 'Saumon Atlantique', 208.0, 'g', 12.4, 3.1, 3.8, 3.6, 0.0, 22.1, 0.0, 0.0, 0.0, 0.59, 55.0,
    58.0, 0.019, 0.081, 0.155, 8.7, 1.66, 0.6, 0.0, 25.0, 0.0, 3.2, 11.0, 3.4, 1.22, 0.1, 0.0,
    0.0, 0.0, 12.0, 0.0, 91.0, 0.0, 0.0, 0.25, 0.31, 0.0, 0.0, 29.0, 0.016, 0.0, 384.0, 252.0, 628.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.45,
    u.id 
FROM utilisateurs u WHERE u.username = 'admin';

-- 3 aliments pour testuser
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
SELECT 'Carotte', 41.0, 'g', 0.2, 0.04, 0.01, 0.12, 0.0, 0.9, 10.0, 4.7, 2.8, 0.069, 0.0,
    835.0, 0.084, 0.066, 0.058, 0.983, 0.273, 0.138, 0.0, 19.0, 0.0, 0.0, 5.9, 0.0, 0.66, 13.2, 0.0,
    0.0, 0.0, 33.0, 0.0, 8.8, 0.0, 0.0, 0.045, 0.3, 0.0, 0.0, 12.0, 0.143, 0.0, 69.0, 35.0, 320.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.24,
    u.id 
FROM utilisateurs u WHERE u.username = 'testuser';

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
SELECT 'Brocoli', 34.0, 'g', 0.4, 0.07, 0.01, 0.19, 0.0, 2.8, 7.0, 1.5, 2.6, 0.033, 0.0,
    31.0, 0.003, 0.071, 0.117, 0.639, 0.573, 0.175, 0.0, 63.0, 0.0, 0.0, 89.2, 0.0, 0.78, 101.6, 0.0,
    0.0, 0.0, 47.0, 0.0, 19.3, 0.0, 0.0, 0.049, 0.73, 0.0, 0.0, 21.0, 0.21, 0.0, 33.0, 66.0, 316.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.41,
    u.id 
FROM utilisateurs u WHERE u.username = 'testuser';

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
SELECT 'Yaourt Nature', 59.0, 'g', 0.4, 0.1, 0.1, 0.0, 0.0, 10.0, 3.6, 4.7, 0.0, 0.046, 5.0,
    27.0, 0.003, 0.142, 0.278, 0.75, 0.39, 0.063, 0.0, 7.0, 0.0, 0.4, 0.0, 0.0, 0.06, 0.2, 0.0,
    0.0, 0.0, 110.0, 0.0, 15.2, 0.0, 0.0, 0.009, 0.04, 0.0, 0.0, 12.0, 0.004, 0.0, 46.0, 95.0, 141.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.59,
    u.id 
FROM utilisateurs u WHERE u.username = 'testuser';

-- 2 aliments pour user2
INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
SELECT 'Avocat', 160.0, 'g', 14.7, 2.1, 9.8, 1.8, 0.0, 2.0, 9.0, 0.7, 6.7, 0.007, 0.0,
    146.0, 0.015, 0.067, 0.13, 1.738, 1.389, 0.257, 0.0, 81.0, 0.0, 0.0, 10.0, 0.0, 2.07, 21.0, 0.0,
    0.0, 0.0, 12.0, 0.0, 14.2, 0.0, 0.0, 0.19, 0.55, 0.0, 0.0, 29.0, 0.142, 0.0, 7.0, 52.0, 485.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.64,
    u.id 
FROM utilisateurs u WHERE u.username = 'user2';

INSERT INTO aliments (nom, calories, unite, Mat_Gras, Mat_Gras_S, Mat_Gras_MI, 
    Mat_Gras_PI, Mat_Gras_T, proteines, glucides, sucre, fibres, sel, cholesterol, 
    pro_vit_A, vit_A, vit_B1, vit_B2, vit_B3, vit_B5, vit_B6, vit_B8, 
    vit_B9, vit_B11, vit_B12, vit_C, vit_D, vit_E, vit_K1, vit_K2,
    Ars, B, Ca, Cl, choline, Cr, Co, Cu, Fe, F, I, Mg, Mn, Mo, Na, P, K, Rb, SiO, S, Se, V, Sn, Zn, 
    user_id)
SELECT 'Pain Complet', 247.0, 'g', 3.4, 0.6, 0.5, 1.4, 0.0, 13.0, 41.0, 6.0, 7.0, 1.31, 0.0,
    0.0, 0.0, 0.416, 0.21, 4.33, 0.44, 0.22, 0.0, 44.0, 0.0, 0.0, 0.3, 0.0, 2.46, 1.9, 0.0,
    0.0, 0.0, 107.0, 0.0, 31.2, 0.0, 0.0, 0.234, 3.6, 0.0, 0.0, 75.0, 1.65, 0.0, 520.0, 216.0, 248.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.81,
    u.id 
FROM utilisateurs u WHERE u.username = 'user2';


-- Code barre pour Riz Blanc (admin)
INSERT INTO code_barre (code_barre, aliment_id, marque)
SELECT '8901234567890', a.id, 'Uncle Bens'
FROM aliments a 
JOIN utilisateurs u ON a.user_id = u.id 
WHERE a.nom = 'Riz Blanc' AND u.username = 'admin';

-- Code barre pour Saumon Atlantique (admin)
INSERT INTO code_barre (code_barre, aliment_id, marque)
SELECT '7890123456789', a.id, 'Océan Frais'
FROM aliments a 
JOIN utilisateurs u ON a.user_id = u.id 
WHERE a.nom = 'Saumon Atlantique' AND u.username = 'admin';


-- Code barre pour Yaourt Nature (testuser)
INSERT INTO code_barre (code_barre, aliment_id, marque)
SELECT '5678901234567', a.id, 'Danone'
FROM aliments a
JOIN utilisateurs u ON a.user_id = u.id 
WHERE a.nom = 'Yaourt Nature' AND u.username = 'testuser';