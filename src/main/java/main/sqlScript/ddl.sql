CREATE TABLE specialty
(
    id                  SERIAL PRIMARY KEY,
    name                VARCHAR(50) UNIQUE NOT NULL
);
CREATE TABLE doctor
(
    dni                 VARCHAR(9) PRIMARY KEY,
    name                VARCHAR(50) NOT NULL,
    surname1            VARCHAR(50) NOT NULL,
    surname2            VARCHAR(50) NOT NULL,
    telephone           VARCHAR(15),
    email               VARCHAR(100),
    specialty           INT NOT NULL,
    FOREIGN KEY (specialty) REFERENCES specialty (id) ON DELETE CASCADE
);
CREATE TABLE patient
(
    dni                 VARCHAR(9) PRIMARY KEY,
    name                VARCHAR(50) NOT NULL,
    surname1            VARCHAR(50) NOT NULL,
    surname2            VARCHAR(50) NOT NULL,
    birth_date          DATE,
    telephone           VARCHAR(9),
    email               VARCHAR(100),
    city                VARCHAR(50),
    postal_code         VARCHAR(5),
    street              VARCHAR(50),
    number              VARCHAR(50),
    password            VARCHAR(100) NOT NULL
);
CREATE TABLE appointment
(
    id                  SERIAL PRIMARY KEY,
    doctor              VARCHAR(10) NOT NULL,
    patient             VARCHAR(10) NOT NULL,
    date                DATE NOT NULL,
    time                TIME NOT NULL,
    state               VARCHAR(12) DEFAULT 'SCHEDULED',
    FOREIGN KEY (doctor) REFERENCES doctor (dni) ON DELETE CASCADE,
    FOREIGN KEY (patient) REFERENCES patient (dni) ON DELETE CASCADE
);
CREATE TABLE visit
(
    id                  SERIAL PRIMARY KEY,
    doctor              VARCHAR(10) NOT NULL,
    patient             VARCHAR(10) NOT NULL,
    appointment         INT,
    prescription        INT,
    diagnosis           INT,
    date                DATE NOT NULL,
    time                TIME NOT NULL,
    notes               TEXT,
    FOREIGN KEY (doctor) REFERENCES doctor (dni) ON DELETE CASCADE,
    FOREIGN KEY (patient) REFERENCES patient (dni) ON DELETE CASCADE
);
CREATE TABLE prescription
(
    id                  SERIAL PRIMARY KEY,
    doctor              VARCHAR(10),
    patient             VARCHAR(10),
    start_date          DATE,
    end_date            DATE,
    description         TEXT,
    FOREIGN KEY (doctor) REFERENCES doctor (dni) ON DELETE CASCADE,
    FOREIGN KEY (patient) REFERENCES patient (dni) ON DELETE CASCADE
);
CREATE TABLE diagnosis
(
    id                  SERIAL PRIMARY KEY,
    doctor              VARCHAR(10),
    patient             VARCHAR(10),
    start_date          DATE,
    end_date            DATE,
    description         TEXT,
    FOREIGN KEY (doctor) REFERENCES doctor (dni) ON DELETE CASCADE,
    FOREIGN KEY (patient) REFERENCES patient (dni) ON DELETE CASCADE
);
CREATE TABLE test
(
    id                  SERIAL PRIMARY KEY,
    doctor              VARCHAR(10) NOT NULL,
    patient             VARCHAR(10) NOT NULL,
    date                DATE,
    notes               TEXT,
    FOREIGN KEY (doctor) REFERENCES doctor (dni) ON DELETE CASCADE,
    FOREIGN KEY (patient) REFERENCES patient (dni) ON DELETE CASCADE
);
CREATE TABLE blood_count_test
(
    red_blood_cell      INT,
    hemoglobin          INT,
    hematocrit          INT,
    platet              INT
) INHERITS (test);

CREATE TABLE antibody_test
(
    anti_SSA            INT,
    anti_tTG            INT,
    anti_CCP            INT,
    anti_SRP            INT
) INHERITS (test);

CREATE TABLE blood_culture_test
(
    salmonella          INT,
    acinetobacter       INT,
    escherichiaColi     INT,
    citrobacter         INT
) INHERITS (test);

CREATE TABLE other_test
(
    type                VARCHAR(100),
    results             TEXT
) INHERITS (test);