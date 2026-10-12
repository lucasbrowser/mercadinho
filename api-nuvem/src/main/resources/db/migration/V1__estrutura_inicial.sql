CREATE TABLE evento_sincronizacao (
    id UUID PRIMARY KEY,
    id_evento_origem UUID NOT NULL UNIQUE,
    tipo_evento VARCHAR(120) NOT NULL,
    dados JSONB NOT NULL,
    recebido_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processado_em TIMESTAMPTZ
);
