-- Script pour configurer le schÃ©ma dans Oracle Live SQL
-- 1. Création de l’utilisateur et droits
DROP USER project_user CASCADE;
CREATE USER project_user IDENTIFIED BY secret;
GRANT CONNECT, RESOURCE TO project_user;
ALTER SESSION SET CURRENT_SCHEMA = project_user;

-- 3. Tables de paramétrage

CREATE TABLE ptyp (
  idtyp NUMBER PRIMARY KEY,
  lib   VARCHAR2(100) NOT NULL
);

CREATE TABLE psta (
  idsta NUMBER PRIMARY KEY,
  lib   VARCHAR2(100) NOT NULL
);

CREATE TABLE pprio (
  idprio NUMBER PRIMARY KEY,
  lib    VARCHAR2(100) NOT NULL
);

CREATE TABLE pdir (
  iddir NUMBER PRIMARY KEY,
  code  VARCHAR2(10)  NOT NULL,
  lib   VARCHAR2(100) NOT NULL
);

CREATE TABLE pkpi (
  idkpi NUMBER PRIMARY KEY,
  lib   VARCHAR2(100) NOT NULL,
  pct   NUMBER(5,2)      NOT NULL  -- Ex. 15.00 pour 15%
);

CREATE TABLE pequipro (
  idrole NUMBER PRIMARY KEY,
  lib    VARCHAR2(100) NOT NULL
);

-- 2. Séquences et triggers pour tables de paramétrage

CREATE SEQUENCE seq_ptyp   START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_ptyp_bi
BEFORE INSERT ON ptyp FOR EACH ROW WHEN (new.idtyp IS NULL)
BEGIN SELECT seq_ptyp.NEXTVAL INTO :new.idtyp FROM dual; END;
/

CREATE SEQUENCE seq_psta   START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_psta_bi
BEFORE INSERT ON psta FOR EACH ROW WHEN (new.idsta IS NULL)
BEGIN SELECT seq_psta.NEXTVAL INTO :new.idsta FROM dual; END;
/

CREATE SEQUENCE seq_pprio  START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_pprio_bi
BEFORE INSERT ON pprio FOR EACH ROW WHEN (new.idprio IS NULL)
BEGIN SELECT seq_pprio.NEXTVAL INTO :new.idprio FROM dual; END;
/

CREATE SEQUENCE seq_pdir   START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_pdir_bi
BEFORE INSERT ON pdir FOR EACH ROW WHEN (new.iddir IS NULL)
BEGIN SELECT seq_pdir.NEXTVAL INTO :new.iddir FROM dual; END;
/

CREATE SEQUENCE seq_pkpi   START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_pkpi_bi
BEFORE INSERT ON pkpi FOR EACH ROW WHEN (new.idkpi IS NULL)
BEGIN SELECT seq_pkpi.NEXTVAL INTO :new.idkpi FROM dual; END;
/

CREATE SEQUENCE seq_pequipro START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_pequipro_bi
BEFORE INSERT ON pequipro FOR EACH ROW WHEN (new.idrole IS NULL)
BEGIN SELECT seq_pequipro.NEXTVAL INTO :new.idrole FROM dual; END;
/

-- 5. Tables métier

CREATE TABLE tbutil (
  idutil NUMBER PRIMARY KEY,
  nom    VARCHAR2(50) NOT NULL,
  prenom VARCHAR2(50) NOT NULL,
  email  VARCHAR2(100) NOT NULL UNIQUE,
  tel    VARCHAR2(20),
  iddir  NUMBER NOT NULL,
  fonc   VARCHAR2(50),
  CONSTRAINT fk_tbutil_pdir FOREIGN KEY (iddir) REFERENCES pdir (iddir)
);

CREATE TABLE tbequipro (
  idequipro NUMBER PRIMARY KEY,
  lib       VARCHAR2(100) NOT NULL
);

CREATE TABLE tbmem (
  idequipro NUMBER NOT NULL,
  idutil    NUMBER NOT NULL,
  idrole    NUMBER NOT NULL,
  CONSTRAINT pk_tbmem PRIMARY KEY (idequipro, idutil),
  CONSTRAINT fk_tbmem_equipro FOREIGN KEY (idequipro) REFERENCES tbequipro (idequipro),
  CONSTRAINT fk_tbmem_util     FOREIGN KEY (idutil)    REFERENCES tbutil    (idutil),
  CONSTRAINT fk_tbmem_role     FOREIGN KEY (idrole)    REFERENCES pequipro  (idrole)
);

CREATE TABLE tbpro (
  idpro      NUMBER PRIMARY KEY,
  lib        VARCHAR2(200) NOT NULL,
  descr       CLOB,
  idtyp      NUMBER NOT NULL,
  idsta      NUMBER NOT NULL,
  idprio     NUMBER NOT NULL,
  iddir      NUMBER NOT NULL,
  idequipro  NUMBER NOT NULL,
  dd         DATE NOT NULL,
  df         DATE,
  dfr        DATE,
  CONSTRAINT fk_tbpro_ptyp     FOREIGN KEY (idtyp)      REFERENCES ptyp      (idtyp),
  CONSTRAINT fk_tbpro_psta     FOREIGN KEY (idsta)      REFERENCES psta      (idsta),
  CONSTRAINT fk_tbpro_pprio    FOREIGN KEY (idprio)     REFERENCES pprio     (idprio),
  CONSTRAINT fk_tbpro_pdir     FOREIGN KEY (iddir)      REFERENCES pdir      (iddir),
  CONSTRAINT fk_tbpro_equipro  FOREIGN KEY (idequipro)  REFERENCES tbequipro (idequipro)
);

CREATE TABLE tbplan (
  idplan NUMBER PRIMARY KEY,
  idpro  NUMBER NOT NULL,
  idkpi  NUMBER NOT NULL,
  CONSTRAINT fk_tbplan_pro  FOREIGN KEY (idpro) REFERENCES tbpro (idpro) ON DELETE CASCADE,
  CONSTRAINT fk_tbplan_kpi  FOREIGN KEY (idkpi) REFERENCES pkpi (idkpi)
);

CREATE TABLE tbact (
  idact NUMBER PRIMARY KEY,
  idplan NUMBER NOT NULL,
  lib   VARCHAR2(200) NOT NULL,
  idsta NUMBER NOT NULL,
  idutil NUMBER NOT NULL,
  dd    DATE NOT NULL,
  df    DATE,
  dfr   DATE,
  prog  NUMBER(5,2) DEFAULT 0,
  CONSTRAINT fk_tbact_plan FOREIGN KEY (idplan) REFERENCES tbplan (idplan) ON DELETE CASCADE,
  CONSTRAINT fk_tbact_psta FOREIGN KEY (idsta)   REFERENCES psta   (idsta),
  CONSTRAINT fk_tbact_util FOREIGN KEY (idutil)  REFERENCES tbutil (idutil),
  CONSTRAINT chk_tbact_prog CHECK (prog BETWEEN 0 AND 100)
);

CREATE TABLE tbiact (
  idiact NUMBER PRIMARY KEY,
  idact  NUMBER NOT NULL,
  idsta  NUMBER NOT NULL,
  dd     DATE NOT NULL,
  df     DATE,
  dfr    DATE,
  CONSTRAINT fk_tbiact_act FOREIGN KEY (idact) REFERENCES tbact (idact) ON DELETE CASCADE,
  CONSTRAINT fk_tbiact_psta FOREIGN KEY (idsta) REFERENCES psta (idsta)
);

CREATE TABLE tbadep (
  iddep  NUMBER PRIMARY KEY,
  idact  NUMBER NOT NULL,
  dep_on NUMBER NOT NULL,
  CONSTRAINT fk_tbadep_act    FOREIGN KEY (idact)  REFERENCES tbact (idact) ON DELETE CASCADE,
  CONSTRAINT fk_tbadep_requis FOREIGN KEY (dep_on) REFERENCES tbact (idact)
);

CREATE TABLE tbdoc (
  iddoc NUMBER PRIMARY KEY,
  idpro NUMBER NOT NULL,
  title VARCHAR2(200) NOT NULL,
  vers  VARCHAR2(20)  NOT NULL,
  idsta NUMBER NOT NULL,
  path  VARCHAR2(500),
  ub    NUMBER,
  ud    DATE DEFAULT SYSDATE,
  CONSTRAINT fk_tbdoc_pro  FOREIGN KEY (idpro) REFERENCES tbpro (idpro) ON DELETE CASCADE,
  CONSTRAINT fk_tbdoc_psta FOREIGN KEY (idsta) REFERENCES psta (idsta),
  CONSTRAINT fk_tbdoc_ub   FOREIGN KEY (ub)    REFERENCES tbutil (idutil)
);

CREATE TABLE tbprbudg (
  idbud NUMBER PRIMARY KEY,
  idpro NUMBER NOT NULL,
  BI    NUMBER(18,2) DEFAULT 0,
  BC    NUMBER(18,2) DEFAULT 0,
  CONSTRAINT fk_tbprbudg_pro FOREIGN KEY (idpro) REFERENCES tbpro (idpro) ON DELETE CASCADE,
  CONSTRAINT chk_tbprbudg    CHECK (BI >= 0 AND BC >= 0)
);

CREATE TABLE tbaulog (
  idlog   NUMBER PRIMARY KEY,
  tbn     VARCHAR2(30) NOT NULL,
  rid     VARCHAR2(100) NOT NULL,
  opetyp  VARCHAR2(6)  NOT NULL,
  mp      NUMBER       NOT NULL,
  dm      DATE DEFAULT SYSDATE,
  details CLOB,
  CONSTRAINT fk_tbaulog_mp FOREIGN KEY (mp) REFERENCES tbutil (idutil)
);

-- 6. Indexes conseillés sur FK

CREATE INDEX ix_tbutil_pdir   ON tbutil   (iddir);
CREATE INDEX ix_tbmem_util    ON tbmem    (idutil);
CREATE INDEX ix_tbmem_role    ON tbmem    (idrole);
CREATE INDEX ix_tbplan_kpi    ON tbplan   (idkpi);
CREATE INDEX ix_tbact_plan    ON tbact    (idplan);
CREATE INDEX ix_tbact_psta    ON tbact    (idsta);
CREATE INDEX ix_tbact_util    ON tbact    (idutil);
CREATE INDEX ix_tbiact_act    ON tbiact   (idact);
CREATE INDEX ix_tbadep_act    ON tbadep   (idact);
CREATE INDEX ix_tbadep_requis ON tbadep   (dep_on);
CREATE INDEX ix_tbdoc_psta    ON tbdoc    (idsta);
CREATE INDEX ix_tbdoc_ub      ON tbdoc    (ub);
CREATE INDEX ix_tbprbudg_pro  ON tbprbudg (idpro);
CREATE INDEX ix_tbaulog_mp    ON tbaulog  (mp);


-- 4. Séquences et triggers pour tables métier

CREATE SEQUENCE seq_tbutil    START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_tbutil_bi
BEFORE INSERT ON tbutil FOR EACH ROW WHEN (new.idutil IS NULL)
BEGIN SELECT seq_tbutil.NEXTVAL INTO :new.idutil FROM dual; END;
/

CREATE SEQUENCE seq_tbequipro START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_tbequipro_bi
BEFORE INSERT ON tbequipro FOR EACH ROW WHEN (new.idequipro IS NULL)
BEGIN SELECT seq_tbequipro.NEXTVAL INTO :new.idequipro FROM dual; END;
/

CREATE SEQUENCE seq_tbmem    START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
-- Pas de trigger, PK composite (idequipro,idutil)

CREATE SEQUENCE seq_tbpro     START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_tbpro_bi
BEFORE INSERT ON tbpro FOR EACH ROW WHEN (new.idpro IS NULL)
BEGIN SELECT seq_tbpro.NEXTVAL INTO :new.idpro FROM dual; END;
/

CREATE SEQUENCE seq_tbplan    START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_tbplan_bi
BEFORE INSERT ON tbplan FOR EACH ROW WHEN (new.idplan IS NULL)
BEGIN SELECT seq_tbplan.NEXTVAL INTO :new.idplan FROM dual; END;
/

CREATE SEQUENCE seq_tbact     START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_tbact_bi
BEFORE INSERT ON tbact FOR EACH ROW WHEN (new.idact IS NULL)
BEGIN SELECT seq_tbact.NEXTVAL INTO :new.idact FROM dual; END;
/

CREATE SEQUENCE seq_tbiact    START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_tbiact_bi
BEFORE INSERT ON tbiact FOR EACH ROW WHEN (new.idiact IS NULL)
BEGIN SELECT seq_tbiact.NEXTVAL INTO :new.idiact FROM dual; END;
/

CREATE SEQUENCE seq_tbadep   START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_tbadep_bi
BEFORE INSERT ON tbadep FOR EACH ROW WHEN (new.iddep IS NULL)
BEGIN SELECT seq_tbadep.NEXTVAL INTO :new.iddep FROM dual; END;
/

CREATE SEQUENCE seq_tbdoc    START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_tbdoc_bi
BEFORE INSERT ON tbdoc FOR EACH ROW WHEN (new.iddoc IS NULL)
BEGIN SELECT seq_tbdoc.NEXTVAL INTO :new.iddoc FROM dual; END;
/

CREATE SEQUENCE seq_tbprbudg START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_tbprbudg_bi
BEFORE INSERT ON tbprbudg FOR EACH ROW WHEN (new.idbud IS NULL)
BEGIN SELECT seq_tbprbudg.NEXTVAL INTO :new.idbud FROM dual; END;
/

CREATE SEQUENCE seq_tbaulog  START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE OR REPLACE TRIGGER trg_tbaulog_bi
BEFORE INSERT ON tbaulog FOR EACH ROW WHEN (new.idlog IS NULL)
BEGIN SELECT seq_tbaulog.NEXTVAL INTO :new.idlog FROM dual; END;
/
