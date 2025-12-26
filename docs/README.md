# Projeto Benefícios

Solução em camadas com banco de dados, EJB, backend Spring Boot e frontend estático para gestão de benefícios e transferência de valores entre registros.

## Arquitetura
- `db/`: scripts `schema.sql` e `seed.sql` (referência)
- `ejb-module/`: módulo EJB com validações de transferência, locking pessimista e rollback
- `backend-module/`: API REST com CRUD, endpoint de transferência, Swagger/OpenAPI e testes
- `frontend/`: página estática que consome a API
- `.github/workflows/`: CI (build do backend)

## Requisitos
- Java 17
- Maven 3.9+

## Execução
1) Backend
```bash
mvn -f backend-module clean spring-boot:run
```
API disponível em `http://localhost:8080`.

2) Swagger
- UI: `http://localhost:8080/swagger-ui.html`

3) Frontend
- Abra `frontend/index.html` no navegador para listar, criar e transferir.

## Endpoints principais
- `GET /api/v1/beneficios`
- `GET /api/v1/beneficios/{id}`
- `POST /api/v1/beneficios`
- `PUT /api/v1/beneficios/{id}`
- `DELETE /api/v1/beneficios/{id}`
- `POST /api/v1/beneficios/transfer` (body: `{ "fromId": 1, "toId": 2, "amount": 200.00 }`)

## Banco de dados
- H2 em memória aplicado automaticamente via `schema.sql` e `data.sql` no backend.
- Os scripts equivalentes estão em `db/` para referência.

## EJB
- `BeneficioEjbService` protege a operação de transferência com validações, `PESSIMISTIC_WRITE` e exceção de negócio com rollback.
- `Beneficio` possui `@Version` para controle de versão otimista.
- Interface remota `BeneficioEjbServiceRemote` disponível para integração via contêiner Jakarta EE (WildFly/Payara).

## Testes
```bash
mvn -f backend-module test
```
Inclui cenários de sucesso e de erro para transferência.

## CI
- GitHub Actions: `.github/workflows/ci.yml` (build do backend com JDK 17).
