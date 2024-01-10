CREATE DATABASE compteur_calories;

CREATE TABLE aliments (
    nom VARCHAR(50) PRIMARY KEY,
    calories NUMERIC(7,2) NOT NULL,
    unite VARCHAR(2) DEFAULT 'g',
    glucides NUMERIC(4,1) NOT NULL,
    sucre NUMERIC(4,1) DEFAULT 0,
    fibres NUMERIC(3,1) DEFAULT 0,
    Mat_Gras NUMERIC(4,1) NOT NULL,
    Mat_Gras_S NUMERIC(3,1) DEFAULT 0,
    Mat_Gras_MI NUMERIC(3,1) DEFAULT 0,
    Mat_Gras_PI NUMERIC(3,1) DEFAULT 0,
    Mat_Gras_T NUMERIC(3,1) DEFAULT 0,
    proteines NUMERIC(3,1) NOT NULL,
    sel NUMERIC(3,1) DEFAULT 0,
    cholesterol NUMERIC(5,1) DEFAULT 0,
    vit_A NUMERIC(6,1) DEFAULT 0,
    vit_B1 NUMERIC(6,1) DEFAULT 0,
    vit_B2 NUMERIC(6,1) DEFAULT 0,
    vit_B3 NUMERIC(6,1) DEFAULT 0,
    vit_B5 NUMERIC(6,1) DEFAULT 0,
    vit_B6 NUMERIC(6,1) DEFAULT 0,
    vit_B8 NUMERIC(6,1) DEFAULT 0,
    vit_B11 NUMERIC(6,1) DEFAULT 0,
    vit_B12 NUMERIC(6,1) DEFAULT 0,
    vit_C NUMERIC(6,1) DEFAULT 0,
    vit_D NUMERIC(6,1) DEFAULT 0,
    vit_E NUMERIC(6,1) DEFAULT 0,
    vit_K NUMERIC(6,1) DEFAULT 0,
    Ars NUMERIC(6,1) DEFAULT 0,
    B NUMERIC(6,1) DEFAULT 0,
    Ca NUMERIC(6,1) DEFAULT 0,
    Cl NUMERIC(6,1) DEFAULT 0,
    choline NUMERIC(6,1) DEFAULT 0,
    Cr NUMERIC(6,1) DEFAULT 0,
    Co NUMERIC(6,1) DEFAULT 0,
    Cu NUMERIC(6,1) DEFAULT 0,
    Fe NUMERIC(6,1) DEFAULT 0,
    F NUMERIC(6,1) DEFAULT 0,
    I NUMERIC(6,1) DEFAULT 0,
    Mg NUMERIC(6,1) DEFAULT 0,
    Mn NUMERIC(6,1) DEFAULT 0,
    Mo NUMERIC(6,1) DEFAULT 0,
    P NUMERIC(6,1) DEFAULT 0,
    K NUMERIC(6,1) DEFAULT 0,
    Rb NUMERIC(6,1) DEFAULT 0,
    SiO NUMERIC(6,1) DEFAULT 0,
    S NUMERIC(6,1) DEFAULT 0,
    Se NUMERIC(6,1) DEFAULT 0,
    V NUMERIC(6,1) DEFAULT 0,
    Sn NUMERIC(6,1) DEFAULT 0,
    Zn NUMERIC(6,1) DEFAULT 0
);

CREATE TABLE plats (
    nom VARCHAR(50) PRIMARY KEY,
    portions NUMERIC(4, 1)
);

CREATE TABLE recettes (
    nom_plat VARCHAR(50) REFERENCES plats(nom) ON UPDATE CASCADE ON DELETE RESTRICT,
    nom_aliment VARCHAR(50) REFERENCES aliments(nom) ON UPDATE CASCADE ON DELETE RESTRICT,
    quantite NUMERIC(6,1),
    PRIMARY KEY (nomPlat, nomAliment)
);

CREATE TABLE composition_journal_aliment (
    nom_aliment VARCHAR(50) REFERENCES aliments(nom) ON UPDATE CASCADE ON DELETE RESTRICT,
    date_journal DATE,
    quantite NUMERIC(6,1),
    PRIMARY KEY (nom, dateJournal)
);

CREATE TABLE composition_journal_plat (
    nom_plat VARCHAR(50) REFERENCES plats(nom) ON UPDATE CASCADE ON DELETE RESTRICT,
    date_journal DATE,
    portions NUMERIC(4,1),
    PRIMARY KEY (nom, dateJournal)
);

CREATE TABLE code_barre (
    code_barre CHAR(13) PRIMARY KEY,
    nom_aliment VARCHAR(50) REFERENCES aliments(nom) ON UPDATE CASCADE ON DELETE CASCADE,
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