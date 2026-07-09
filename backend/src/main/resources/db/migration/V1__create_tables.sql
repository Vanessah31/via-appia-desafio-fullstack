CREATE TABLE incident (
                          id UUID PRIMARY KEY,
                          titulo VARCHAR(120) NOT NULL,
                          descricao TEXT,
                          prioridade VARCHAR(10) NOT NULL CHECK (prioridade IN ('BAIXA','MEDIA','ALTA')),
                          status VARCHAR(15) NOT NULL CHECK (status IN ('ABERTA','EM_ANDAMENTO','RESOLVIDA','CANCELADA')),
                          responsavel_email VARCHAR(255) NOT NULL,
                          tags TEXT,
                          data_abertura TIMESTAMP NOT NULL,
                          data_atualizacao TIMESTAMP NOT NULL
);

CREATE TABLE comment (
                         id UUID PRIMARY KEY,
                         incident_id UUID NOT NULL REFERENCES incident(id) ON DELETE CASCADE,
                         autor VARCHAR(120) NOT NULL,
                         mensagem TEXT NOT NULL,
                         data_criacao TIMESTAMP NOT NULL
);

CREATE INDEX idx_incident_status ON incident(status);
CREATE INDEX idx_incident_prioridade ON incident(prioridade);
CREATE INDEX idx_comment_incident_id ON comment(incident_id);