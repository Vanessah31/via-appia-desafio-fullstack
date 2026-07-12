# Via Appia — Gestão de Ocorrências

Desafio Fullstack (Angular + Spring Boot + PostgreSQL) desenvolvido para o processo seletivo da Via Appia Informática.

## Stack utilizada

- **Backend**: Java 17, Spring Boot, Spring Security, Spring Data JPA, Flyway, JWT (jjwt), springdoc-openapi (Swagger)
- **Frontend**: Angular 22 (Standalone Components)
- **Banco de dados**: PostgreSQL 16
- **Containerização**: Docker + Docker Compose

## Como executar (Docker — recomendado)

### Pré-requisitos
- Docker + Docker Compose instalados

### Passos

```bash
git clone https://github.com/Vanessah31/via-appia-desafio-fullstack.git
cd via-appia-desafio-fullstack
docker compose up --build
```

Aguarde a construção das imagens e a inicialização dos containers (banco, backend e frontend).

### Endpoints disponíveis
- **Frontend**: http://localhost:4200
- **Backend**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html

## Credenciais de acesso (usuários seed)

| Email | Senha | Perfil |
|---|---|---|
| admin@viaappia.com | admin123 | READ_WRITE (leitura e escrita) |
| leitura@viaappia.com | leitura123 | READ_ONLY (somente leitura) |

## Fluxo de autenticação

1. Acesse http://localhost:4200 — você será redirecionado para a tela de login.
2. Informe um dos emails/senhas acima.
3. Um token JWT é gerado e armazenado no navegador, sendo anexado automaticamente em todas as requisições subsequentes.
4. Usuários com perfil **READ_ONLY** conseguem visualizar ocorrências, mas não podem criar, editar, excluir, comentar ou alterar status (o backend bloqueia essas ações com status 403).

## Funcionalidades implementadas

- CRUD completo de Ocorrências (Incidents)
- Comentários por ocorrência
- Alteração de status
- Filtros (status, prioridade, busca textual), paginação e ordenação
- Estatísticas agregadas (`/stats/incidents`)
- Autenticação JWT com dois perfis de autorização
- Documentação via Swagger/OpenAPI
- Execução completa via Docker Compose

## Anti-duplicação (DRY)

**Backend:**
- `normalizeTags(List<String>)` — normalização de tags reutilizada em criar/editar
- `touchUpdate(Incident)` — atualização de `dataAtualizacao` reutilizada em todas as operações de escrita
- `buildIncidentFilter(...)` — construção de filtros reutilizada em listagem e stats
- `ApiError` / `GlobalExceptionHandler` — tratamento de erros padronizado

**Frontend:**
- `ApiClient` — cliente HTTP genérico e tipado, usado em todas as chamadas ao backend
- `buildQueryParams(tableState, filters)` — construção de parâmetros de busca/paginação/ordenação
- `normalizeIncidentFormValue(formValue)` — normalização do formulário de incidents (trim, tags) antes do envio

## Executando sem Docker (desenvolvimento local)

### Backend
```bash
cd backend
./mvnw spring-boot:run
```
Requer um PostgreSQL rodando localmente (ou `docker compose up -d db`) com as credenciais configuradas em `application.yaml`.

### Frontend
```bash
cd frontend
ng serve
```
Acesse http://localhost:4200

## Solução de problemas

- **Backend não sobe**: confirme se o container do banco está saudável (`docker compose ps`).
- **Erro de CORS**: confirme que `APP_CORS_ALLOWED_ORIGINS` aponta para a URL correta do frontend.
- **401/403 nas requisições**: confirme que o login foi realizado e o token não expirou (validade de 8 horas).