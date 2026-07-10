CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL CHECK (role IN ('READ_ONLY', 'READ_WRITE'))
);

INSERT INTO users (id, email, password, role) VALUES
                                                  (gen_random_uuid(), 'admin@viaappia.com', '$2b$10$dN2abSflf37KJOZ1hSJiR.Zffr4jyzZdo5metwQPrn7iwcwaMgtGK', 'READ_WRITE'),
                                                  (gen_random_uuid(), 'leitura@viaappia.com', '$2b$10$.Y30lv2TktCMZ5iCS6eu7OLSef/PquoJkxKsrMkfchqHNL5Uxo8ZG', 'READ_ONLY');