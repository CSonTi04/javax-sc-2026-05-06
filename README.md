# Spring Cloud Training Workspace

This repository contains multiple Spring Boot/Spring Cloud demo applications:

- `config-server-demo`: Spring Cloud Config Server
- `config-client-demo`: Spring Cloud Config Client sample
- `employees-backend`: REST API + PostgreSQL + Liquibase
- `employees-frontend`: server-side UI that calls `employees-backend`

## Architecture

```mermaid
flowchart LR
	Browser[Browser] --> FE[employees-frontend\n:8080]
	FE --> BE[employees-backend\n:8081]
	BE --> PG[(PostgreSQL\n:5432)]

	Client[config-client-demo] --> ConfigServer[config-server-demo\n:8888]
	ConfigServer --> ConfigRepo[(Local Git config repo\nfile:///C:/Training/config)]
```

## Modules

### `config-server-demo`

- App name: `config-server-demo`
- Port: `8888`
- Config source: local Git repo path set to `file:///C:/Training/config`

### `config-client-demo`

- App name: `config-client-demo`
- Intended to import config from the Config Server (`localhost:8888`)

### `employees-backend`

- App name: `employees-backend`
- Port: `8081`
- Database: PostgreSQL (`jdbc:postgresql://localhost:5432/employees`)
- DB migration: Liquibase (`classpath:db/db-changelog.yaml`)
- REST base path: `/api/employees`

### `employees-frontend`

- App name: `employees-frontend`
- Port: `8080`
- Calls backend at `http://localhost:8081`

## Prerequisites

- Java + Maven Wrapper (`mvnw` is included per module)
- Docker (for local PostgreSQL)

## Quick Start

### 1) Start PostgreSQL

```powershell
docker run -d -e POSTGRES_DB=employees -e POSTGRES_USER=employees -e POSTGRES_PASSWORD=employees -p 5432:5432 --name employees-postgres postgres
```

### 2) Run employees apps

Start backend:

```powershell
cd employees-backend
.\mvnw.cmd spring-boot:run
```

Start frontend (new terminal):

```powershell
cd employees-frontend
.\mvnw.cmd spring-boot:run
```

Open: `http://localhost:8080`

### 3) (Optional) Run config demo apps

Start Config Server:

```powershell
cd config-server-demo
.\mvnw.cmd spring-boot:run
```

Start Config Client (new terminal):

```powershell
cd config-client-demo
.\mvnw.cmd spring-boot:run
```

## Startup Order

```mermaid
sequenceDiagram
	participant DB as PostgreSQL
	participant BE as employees-backend
	participant FE as employees-frontend
	participant CS as config-server-demo
	participant CC as config-client-demo

	Note over DB,FE: Employees stack
	DB->>BE: Accept DB connections
	BE->>BE: Apply Liquibase changelog
	FE->>BE: HTTP calls to /api/employees

	Note over CS,CC: Config demo stack
	CS->>CS: Load config from local Git path
	CC->>CS: Request configuration
```

## Useful Endpoints

- Frontend UI: `http://localhost:8080`
- Backend API list: `GET http://localhost:8081/api/employees`
- Backend API by id: `GET http://localhost:8081/api/employees/{id}`

See `employees-backend/employees.http` for ready-to-run API requests.

## Notes

- The Config Server currently points to `file:///C:/Training/config`; ensure this path exists on your machine or adjust `config-server-demo/src/main/resources/application.properties`.
- Additional auth-related properties are referenced in `employees-frontend` (`UserController`) and may require extra environment/config in some scenarios.
