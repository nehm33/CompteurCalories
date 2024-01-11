CREATE DATABASE compteur_calories;

CREATE TABLE aliments (
    nom VARCHAR(100) PRIMARY KEY,
    calories NUMERIC(8,3) NOT NULL,
    unite VARCHAR(2) DEFAULT 'g',
    glucides NUMERIC(5,2) NOT NULL,
    sucre NUMERIC(5,2) DEFAULT 0,
    fibres NUMERIC(4,2) DEFAULT 0,
    Mat_Gras NUMERIC(5,2) NOT NULL,
    Mat_Gras_S NUMERIC(4,2) DEFAULT 0,
    Mat_Gras_MI NUMERIC(4,2) DEFAULT 0,
    Mat_Gras_PI NUMERIC(4,2) DEFAULT 0,
    Mat_Gras_T NUMERIC(4,2) DEFAULT 0,
    proteines NUMERIC(5,2) NOT NULL,
    sel NUMERIC(4,2) DEFAULT 0,
    cholesterol NUMERIC(6,2) DEFAULT 0,
    pro_vit_A NUMERIC(8,3) DEFAULT 0,
    vit_A NUMERIC(8,3) DEFAULT 0,
    vit_B1 NUMERIC(8,3) DEFAULT 0,
    vit_B2 NUMERIC(8,3) DEFAULT 0,
    vit_B3 NUMERIC(8,3) DEFAULT 0,
    vit_B5 NUMERIC(8,3) DEFAULT 0,
    vit_B6 NUMERIC(8,3) DEFAULT 0,
    vit_B8 NUMERIC(8,3) DEFAULT 0,
    vit_B9 NUMERIC(8,3) DEFAULT 0,
    vit_B11 NUMERIC(8,3) DEFAULT 0,
    vit_B12 NUMERIC(8,3) DEFAULT 0,
    vit_C NUMERIC(8,3) DEFAULT 0,
    vit_D NUMERIC(8,3) DEFAULT 0,
    vit_E NUMERIC(8,3) DEFAULT 0,
    vit_K NUMERIC(8,3) DEFAULT 0,
    Ars NUMERIC(8,3) DEFAULT 0,
    B NUMERIC(8,3) DEFAULT 0,
    Ca NUMERIC(8,3) DEFAULT 0,
    Cl NUMERIC(8,3) DEFAULT 0,
    choline NUMERIC(8,3) DEFAULT 0,
    Cr NUMERIC(8,3) DEFAULT 0,
    Co NUMERIC(8,3) DEFAULT 0,
    Cu NUMERIC(8,3) DEFAULT 0,
    Fe NUMERIC(8,3) DEFAULT 0,
    F NUMERIC(8,3) DEFAULT 0,
    I NUMERIC(8,3) DEFAULT 0,
    Mg NUMERIC(8,3) DEFAULT 0,
    Mn NUMERIC(8,3) DEFAULT 0,
    Mo NUMERIC(8,3) DEFAULT 0,
    Na NUMERIC(8,3) DEFAULT 0,
    P NUMERIC(8,3) DEFAULT 0,
    K NUMERIC(8,3) DEFAULT 0,
    Rb NUMERIC(8,3) DEFAULT 0,
    SiO NUMERIC(8,3) DEFAULT 0,
    S NUMERIC(8,3) DEFAULT 0,
    Se NUMERIC(8,3) DEFAULT 0,
    V NUMERIC(8,3) DEFAULT 0,
    Sn NUMERIC(8,3) DEFAULT 0,
    Zn NUMERIC(8,3) DEFAULT 0
);

CREATE TABLE plats (
    nom VARCHAR(100) PRIMARY KEY,
    portions NUMERIC(5,2)
);

CREATE TABLE recettes (
    nom_plat VARCHAR(100) REFERENCES plats(nom) ON UPDATE CASCADE ON DELETE RESTRICT,
    nom_aliment VARCHAR(100) REFERENCES aliments(nom) ON UPDATE CASCADE ON DELETE RESTRICT,
    quantite NUMERIC(5,2),
    PRIMARY KEY (nom_plat, nom_aliment)
);

CREATE TABLE composition_journal_aliment (
    nom_aliment VARCHAR(100) REFERENCES aliments(nom) ON UPDATE CASCADE ON DELETE RESTRICT,
    date_journal DATE,
    quantite NUMERIC(5,2),
    PRIMARY KEY (nom_aliment, date_journal)
);

CREATE TABLE composition_journal_plat (
    nom_plat VARCHAR(100) REFERENCES plats(nom) ON UPDATE CASCADE ON DELETE RESTRICT,
    date_journal DATE,
    portions NUMERIC(5,2),
    PRIMARY KEY (nom_plat, date_journal)
);

CREATE TABLE code_barre (
    code_barre CHAR(13) PRIMARY KEY,
    nom_aliment VARCHAR(100) REFERENCES aliments(nom) ON UPDATE CASCADE ON DELETE CASCADE,
    marque VARCHAR(30)
);

CREATE TABLE users (
  username VARCHAR(50) PRIMARY KEY,
  password CHAR(68) NOT NULL,
  enabled SMALLINT NOT NULL
);

CREATE TABLE authorities (
  username varchar(50) REFERENCES users,
  authority varchar(50) NOT NULL,
  PRIMARY KEY (username,authority)
);