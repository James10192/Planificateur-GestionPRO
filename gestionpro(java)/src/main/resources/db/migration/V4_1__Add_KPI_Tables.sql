-- Script de migration pour l'ajout des tables KPI
-- V4_1__Add_KPI_Tables.sql

-- 1. Table paramétrage KPI metrics
CREATE TABLE pkpim (
  idkpim NUMBER PRIMARY KEY,
  code   VARCHAR2(50) NOT NULL UNIQUE,
  lib    VARCHAR2(100) NOT NULL,
  descr  VARCHAR2(500),
  unit   VARCHAR2(50),
  thwarn NUMBER,
  thcrit NUMBER,
  hibett NUMBER(1) DEFAULT 1,
  calc   VARCHAR2(1000),
  updfrq NUMBER,
  ennot  NUMBER(1) DEFAULT 0,
  idkpi  NUMBER,
  dtcrea TIMESTAMP NOT NULL,
  dtmod  TIMESTAMP,
  actif  NUMBER(1) DEFAULT 1 NOT NULL,
  CONSTRAINT fk_pkpim_pkpi FOREIGN KEY (idkpi) REFERENCES pkpi (idkpi)
);

-- Sequence for pkpim
CREATE SEQUENCE seq_pkpim START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- Trigger for pkpim
CREATE OR REPLACE TRIGGER trg_pkpim_bi
BEFORE INSERT ON pkpim FOR EACH ROW WHEN (new.idkpim IS NULL)
BEGIN SELECT seq_pkpim.NEXTVAL INTO :new.idkpim FROM dual; END;
/

-- 2. Table KPI values
CREATE TABLE tbkpiv (
  idkpiv NUMBER PRIMARY KEY,
  idkpim NUMBER NOT NULL,
  idpro  NUMBER,
  idkpi  NUMBER,
  val    NUMBER NOT NULL,
  mdate  TIMESTAMP NOT NULL,
  comm   VARCHAR2(500),
  warnbr NUMBER(1) DEFAULT 0,
  critbr NUMBER(1) DEFAULT 0,
  notsnt NUMBER(1) DEFAULT 0,
  dtcrea TIMESTAMP NOT NULL,
  dtmod  TIMESTAMP,
  actif  NUMBER(1) DEFAULT 1 NOT NULL,
  CONSTRAINT fk_kpiv_metric FOREIGN KEY (idkpim) REFERENCES pkpim (idkpim),
  CONSTRAINT fk_kpiv_pro FOREIGN KEY (idpro) REFERENCES tbpro (idpro),
  CONSTRAINT fk_kpiv_pkpi FOREIGN KEY (idkpi) REFERENCES pkpi (idkpi)
);

-- Sequence for tbkpiv
CREATE SEQUENCE seq_tbkpiv START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- Trigger for tbkpiv ID generation
CREATE OR REPLACE TRIGGER trg_tbkpiv_bi
BEFORE INSERT ON tbkpiv FOR EACH ROW WHEN (new.idkpiv IS NULL)
BEGIN SELECT seq_tbkpiv.NEXTVAL INTO :new.idkpiv FROM dual; END;
/

-- Trigger for tbkpiv data integrity - ensures phase consistency
CREATE OR REPLACE TRIGGER trg_tbkpiv_phase
BEFORE INSERT OR UPDATE ON tbkpiv
FOR EACH ROW
DECLARE
  v_metric_phase NUMBER;
BEGIN
  -- Get the phase of the metric
  SELECT idkpi INTO v_metric_phase
  FROM pkpim
  WHERE idkpim = :new.idkpim;
  
  -- If metric is phase-specific, enforce the same phase for the KPI value
  IF v_metric_phase IS NOT NULL THEN
    :new.idkpi := v_metric_phase;
  END IF;
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    RAISE_APPLICATION_ERROR(-20001, 'Referenced KPI metric does not exist');
END;
/

-- 3. Indexes for foreign keys
CREATE INDEX ix_pkpim_pkpi ON pkpim (idkpi);
CREATE INDEX ix_tbkpiv_metric ON tbkpiv (idkpim);
CREATE INDEX ix_tbkpiv_pro ON tbkpiv (idpro);
CREATE INDEX ix_tbkpiv_pkpi ON tbkpiv (idkpi); 