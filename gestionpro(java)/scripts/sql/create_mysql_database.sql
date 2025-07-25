-- Script d'initialisation de la base de données MySQL pour XAMPP
-- À exécuter via phpMyAdmin ou ligne de commande MySQL

-- Création de la base de données
CREATE DATABASE IF NOT EXISTS nsia_project_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Utilisation de la base de données
USE nsia_project_db;

-- Création de l'utilisateur (optionnel, root est suffisant pour XAMPP)
-- CREATE USER IF NOT EXISTS 'nsia_user'@'localhost' IDENTIFIED BY 'nsia_password';
-- GRANT ALL PRIVILEGES ON nsia_project_db.* TO 'nsia_user'@'localhost';
-- FLUSH PRIVILEGES;

-- Tables de paramètres
CREATE TABLE IF NOT EXISTS ptyp (
    idtyp INT AUTO_INCREMENT PRIMARY KEY,
    libtyp VARCHAR(100) NOT NULL COMMENT 'Libellé du type de projet',
    destyp TEXT COMMENT 'Description du type',
    statyp CHAR(1) DEFAULT 'A' COMMENT 'Statut: A=Actif, I=Inactif',
    datcre TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    datmaj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    utilcre VARCHAR(50),
    utilmaj VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS psta (
    idsta INT AUTO_INCREMENT PRIMARY KEY,
    libsta VARCHAR(100) NOT NULL COMMENT 'Libellé du statut',
    dessta TEXT COMMENT 'Description du statut',
    couleur VARCHAR(20) DEFAULT '#007bff' COMMENT 'Couleur pour affichage',
    ordsta INT DEFAULT 0 COMMENT 'Ordre d''affichage',
    statsta CHAR(1) DEFAULT 'A',
    datcre TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    datmaj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    utilcre VARCHAR(50),
    utilmaj VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS pprio (
    idprio INT AUTO_INCREMENT PRIMARY KEY,
    libprio VARCHAR(100) NOT NULL COMMENT 'Libellé de la priorité',
    desprio TEXT COMMENT 'Description de la priorité',
    niveauprio INT NOT NULL COMMENT 'Niveau numérique (1=Faible, 5=Critique)',
    couleur VARCHAR(20) DEFAULT '#28a745',
    statprio CHAR(1) DEFAULT 'A',
    datcre TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    datmaj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    utilcre VARCHAR(50),
    utilmaj VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS pdir (
    iddir INT AUTO_INCREMENT PRIMARY KEY,
    nomdir VARCHAR(100) NOT NULL COMMENT 'Nom de la direction',
    codedir VARCHAR(10) UNIQUE COMMENT 'Code court de la direction',
    desdir TEXT COMMENT 'Description de la direction',
    statdir CHAR(1) DEFAULT 'A',
    datcre TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    datmaj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    utilcre VARCHAR(50),
    utilmaj VARCHAR(50)
);

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS tbutil (
    idutil INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    motpasse VARCHAR(255) NOT NULL,
    telephone VARCHAR(20),
    poste VARCHAR(100),
    iddir INT,
    role ENUM('ADMIN', 'MANAGER', 'USER') DEFAULT 'USER',
    statutil CHAR(1) DEFAULT 'A',
    dernconnex TIMESTAMP NULL,
    datcre TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    datmaj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    utilcre VARCHAR(50),
    utilmaj VARCHAR(50),
    FOREIGN KEY (iddir) REFERENCES pdir(iddir)
);

-- Table des projets
CREATE TABLE IF NOT EXISTS tbpro (
    idpro INT AUTO_INCREMENT PRIMARY KEY,
    nompro VARCHAR(200) NOT NULL COMMENT 'Nom du projet',
    despro TEXT COMMENT 'Description détaillée',
    idtyp INT COMMENT 'Type de projet',
    idsta INT COMMENT 'Statut du projet',
    idprio INT COMMENT 'Priorité du projet',
    iddir INT COMMENT 'Direction responsable',
    idchef INT COMMENT 'Chef de projet',
    datdeb DATE COMMENT 'Date de début prévue',
    datfin DATE COMMENT 'Date de fin prévue',
    datdebreel DATE COMMENT 'Date de début réelle',
    datfinreel DATE COMMENT 'Date de fin réelle',
    budgetprev DECIMAL(15,2) DEFAULT 0 COMMENT 'Budget prévisionnel',
    budgetcons DECIMAL(15,2) DEFAULT 0 COMMENT 'Budget consommé',
    pourcentavance DECIMAL(5,2) DEFAULT 0 COMMENT 'Pourcentage d''avancement',
    objetpro TEXT COMMENT 'Objectif du projet',
    risques TEXT COMMENT 'Risques identifiés',
    contraintes TEXT COMMENT 'Contraintes du projet',
    livrables TEXT COMMENT 'Livrables attendus',
    statpro CHAR(1) DEFAULT 'A',
    datcre TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    datmaj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    utilcre VARCHAR(50),
    utilmaj VARCHAR(50),
    FOREIGN KEY (idtyp) REFERENCES ptyp(idtyp),
    FOREIGN KEY (idsta) REFERENCES psta(idsta),
    FOREIGN KEY (idprio) REFERENCES pprio(idprio),
    FOREIGN KEY (iddir) REFERENCES pdir(iddir),
    FOREIGN KEY (idchef) REFERENCES tbutil(idutil)
);

-- Table des équipes projet
CREATE TABLE IF NOT EXISTS tbequipro (
    idequipro INT AUTO_INCREMENT PRIMARY KEY,
    idpro INT NOT NULL COMMENT 'Projet associé',
    idutil INT NOT NULL COMMENT 'Membre de l''équipe',
    roleequipe ENUM('CHEF_PROJET', 'MEMBRE', 'CONSULTANT', 'OBSERVATEUR') DEFAULT 'MEMBRE',
    datdeb DATE COMMENT 'Date de début de participation',
    datfin DATE COMMENT 'Date de fin de participation',
    pourcentaffect DECIMAL(5,2) DEFAULT 100 COMMENT 'Pourcentage d''affectation',
    statequipro CHAR(1) DEFAULT 'A',
    datcre TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    datmaj TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    utilcre VARCHAR(50),
    utilmaj VARCHAR(50),
    FOREIGN KEY (idpro) REFERENCES tbpro(idpro) ON DELETE CASCADE,
    FOREIGN KEY (idutil) REFERENCES tbutil(idutil),
    UNIQUE KEY unique_membre_projet (idpro, idutil)
);

-- Données d'exemple
INSERT INTO pdir (nomdir, codedir, desdir, utilcre) VALUES
('Direction Générale', 'DG', 'Direction Générale de NSIA', 'system'),
('Direction Informatique', 'DI', 'Direction des Systèmes d''Information', 'system'),
('Direction Financière', 'DF', 'Direction Financière et Comptable', 'system'),
('Direction Commerciale', 'DC', 'Direction Commerciale et Marketing', 'system');

INSERT INTO ptyp (libtyp, destyp, utilcre) VALUES
('Développement', 'Projets de développement d''applications', 'system'),
('Infrastructure', 'Projets d''infrastructure technique', 'system'),
('Formation', 'Projets de formation et accompagnement', 'system'),
('Amélioration', 'Projets d''amélioration continue', 'system');

INSERT INTO psta (libsta, dessta, couleur, ordsta, utilcre) VALUES
('En attente', 'Projet en attente de validation', '#6c757d', 1, 'system'),
('En cours', 'Projet en cours d''exécution', '#007bff', 2, 'system'),
('En pause', 'Projet temporairement suspendu', '#ffc107', 3, 'system'),
('Terminé', 'Projet terminé avec succès', '#28a745', 4, 'system'),
('Annulé', 'Projet annulé', '#dc3545', 5, 'system');

INSERT INTO pprio (libprio, desprio, niveauprio, couleur, utilcre) VALUES
('Faible', 'Priorité faible - peut être reporté', 1, '#28a745', 'system'),
('Normale', 'Priorité normale - planning standard', 2, '#007bff', 'system'),
('Élevée', 'Priorité élevée - à traiter rapidement', 3, '#fd7e14', 'system'),
('Urgente', 'Priorité urgente - traitement immédiat', 4, '#dc3545', 'system'),
('Critique', 'Priorité critique - impact majeur', 5, '#6f42c1', 'system');

-- Utilisateur admin par défaut (mot de passe: admin)
INSERT INTO tbutil (nom, prenom, email, motpasse, role, iddir, utilcre) VALUES
('Administrateur', 'System', 'admin@nsia.ci', '$2a$10$N.BK7VlJY5f5Lf5z8cJ8muZ8Z.F8F8F8F8F8F8F8F8F8F8F8F8F8', 'ADMIN', 1, 'system');

-- Projet d'exemple
INSERT INTO tbpro (nompro, despro, idtyp, idsta, idprio, iddir, idchef, datdeb, datfin, budgetprev, pourcentavance, utilcre) VALUES
('Système de Gestion de Projets', 'Développement d''un système complet de gestion de projets pour NSIA', 1, 2, 3, 2, 1, '2024-01-01', '2024-12-31', 50000000.00, 85.00, 'system');

COMMIT;

-- Messages de confirmation
SELECT 'Base de données MySQL créée avec succès!' as Message;
SELECT 'Tables créées et données d''exemple insérées.' as Status;
SELECT 'Vous pouvez maintenant démarrer l''application Spring Boot.' as Info;