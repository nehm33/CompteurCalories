CREATE TABLE utilisateurs (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(68) NOT NULL
);

CREATE TABLE roles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name ENUM('ROLE_USER','ROLE_ADMIN') NOT NULL
);

CREATE TABLE utilisateur_role (
  user_id BIGINT REFERENCES utilisateurs ON UPDATE CASCADE ON DELETE CASCADE,
  role_id BIGINT REFERENCES roles ON UPDATE CASCADE ON DELETE CASCADE,
  PRIMARY KEY (user_id, role_id)
);

CREATE TABLE aliments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    user_id BIGINT REFERENCES utilisateurs ON UPDATE CASCADE ON DELETE CASCADE,
    calories FLOAT(23) NOT NULL,
    unite VARCHAR(2) DEFAULT 'g',
    glucides FLOAT(23) NOT NULL,
    sucre FLOAT(23),
    fibres FLOAT(23),
    Mat_Gras FLOAT(23) NOT NULL,
    Mat_Gras_S FLOAT(23),
    Mat_Gras_MI FLOAT(23),
    Mat_Gras_PI FLOAT(23),
    Mat_Gras_T FLOAT(23),
    proteines FLOAT(23) NOT NULL,
    sel FLOAT(23),
    cholesterol FLOAT(23),
    pro_vit_A FLOAT(23),
    vit_A FLOAT(23),
    vit_B1 FLOAT(23),
    vit_B2 FLOAT(23),
    vit_B3 FLOAT(23),
    vit_B5 FLOAT(23),
    vit_B6 FLOAT(23),
    vit_B8 FLOAT(23),
    vit_B9 FLOAT(23),
    vit_B11 FLOAT(23),
    vit_B12 FLOAT(23),
    vit_C FLOAT(23),
    vit_D FLOAT(23),
    vit_E FLOAT(23),
    vit_K1 FLOAT(23),
    vit_K2 FLOAT(23),
    Ars FLOAT(23),
    B FLOAT(23),
    Ca FLOAT(23),
    Cl FLOAT(23),
    choline FLOAT(23),
    Cr FLOAT(23),
    Co FLOAT(23),
    Cu FLOAT(23),
    Fe FLOAT(23),
    F FLOAT(23),
    I FLOAT(23),
    Mg FLOAT(23),
    Mn FLOAT(23),
    Mo FLOAT(23),
    Na FLOAT(23),
    P FLOAT(23),
    K FLOAT(23),
    Rb FLOAT(23),
    SiO FLOAT(23),
    S FLOAT(23),
    Se FLOAT(23),
    V FLOAT(23),
    Sn FLOAT(23),
    Zn FLOAT(23),
    CONSTRAINT UC_Aliment UNIQUE (nom,user_id)
);

CREATE TABLE plats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aliment_id BIGINT REFERENCES aliments ON UPDATE CASCADE ON DELETE CASCADE,
    portions FLOAT(23)
);

CREATE TABLE recettes (
    plat_id BIGINT REFERENCES plats ON UPDATE CASCADE ON DELETE RESTRICT,
    aliment_id BIGINT REFERENCES aliments ON UPDATE CASCADE ON DELETE RESTRICT,
    quantite FLOAT(23),
    PRIMARY KEY (plat_id, aliment_id)
);

CREATE TABLE composition_journal_aliment (
    aliment_id BIGINT REFERENCES aliments ON UPDATE CASCADE ON DELETE RESTRICT,
    user_id BIGINT REFERENCES utilisateurs ON UPDATE CASCADE ON DELETE CASCADE,
    date_journal DATE,
    quantite FLOAT(23),
    PRIMARY KEY (aliment_id, user_id, date_journal)
);

CREATE TABLE code_barre (
    code_barre VARCHAR(13) PRIMARY KEY,
    aliment_id BIGINT REFERENCES aliments ON UPDATE CASCADE ON DELETE CASCADE,
    marque VARCHAR(30)
);