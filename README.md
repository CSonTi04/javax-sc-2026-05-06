# Spring Cloud Training Workspace

This repository contains Spring Boot/Spring Cloud demos, with both non-Kafka and Kafka-enabled employee app variants.

## Tutor Repository

- [Training360/javax-sc-2026-05-06](https://github.com/Training360/javax-sc-2026-05-06)

## What Is In This Repo

- `config-server-demo`: Spring Cloud Config Server (`:8888`)
- `config-client-demo`: Spring Cloud Config Client sample
- `employees-backend`: REST API + PostgreSQL + Liquibase (`:8081`)
- `employees-frontend`: server-side UI for `employees-backend` (`:8080`)
- `employees-backend-kafka`: backend variant (same API/DB profile, `:8081`)
- `employees-frontend-kafka`: frontend variant with Kafka producer (`:8080`)
- `java-se/java-se`: standalone Java SE Maven project (`training:java-se`)
- `kafka/`: Docker Compose infra for Kafka + Kafdrop

## Java SE Language Showcase (`java-se/java-se`)

- Records for immutable domain models: `Pont`, `Kor`, `Teglalap`
- Sealed hierarchy with explicit permitted types: `Alakzat permits Kor, Teglalap`
- Pattern matching `switch` over a sealed type in `Main#szamitKozeppont`
- Record patterns in `switch` cases (`case Kor(Pont kozeppont, _)`)
- Local variable type inference with `var`
- Modern compiler target set to Java 26 in `java-se/java-se/pom.xml`

## Architecture

```mermaid
flowchart LR
    subgraph EmployeesBase[Employees stack (base)]
        Browser[Browser] --> FE[employees-frontend\n:8080]
        FE --> BE[employees-backend\n:8081]
        BE --> PG[(PostgreSQL\n:5432)]
    end

    subgraph EmployeesKafka[Employees stack (Kafka variant)]
        BrowserK[Browser] --> FEK[employees-frontend-kafka\n:8080]
        FEK --> BEK[employees-backend-kafka\n:8081]
        BEK --> PG
        FEK -->|publish event| Kafka[Kafka broker\n:9092]
        Kafdrop[Kafdrop UI\n:9000] --> Kafka
    end

    Client[config-client-demo] --> ConfigServer[config-server-demo\n:8888]
    ConfigServer --> ConfigRepo[(Local Git config repo\nfile:///C:/Training/config)]
```

## Important Choice

Run only one employees pair at a time:

- base pair: `employees-backend` + `employees-frontend`
- Kafka pair: `employees-backend-kafka` + `employees-frontend-kafka`

Both pairs use the same ports (`8080` and `8081`), so running both together causes port conflicts.

## Kafka Infra (`kafka/docker-compose.yaml`)

Single-node KRaft cluster (no ZooKeeper).

| Service | Image | Ports |
|---|---|---|
| `kafka` | `apache/kafka:4.2.0` | `9092` (host access) |
| `kafdrop` | `obsidiandynamics/kafdrop:4.2.0` | `9000` (web UI) |

Listener layout:

- `EXTERNAL` (`:9092`) for host-machine clients
- `CLIENT` (`kafka:9093`) for Docker-network clients (for example Kafdrop)
- `CONTROLLER` (`:9094`) for KRaft controller traffic

## Prerequisites

- Java + Maven Wrapper (`mvnw`/`mvnw.cmd` in each module)
- Docker (PostgreSQL + Kafka)

## Quick Start

### 1) Start PostgreSQL

```powershell
docker run -d -e POSTGRES_DB=employees -e POSTGRES_USER=employees -e POSTGRES_PASSWORD=employees -p 5432:5432 --name employees-postgres postgres
```

### 2) Start Kafka + Kafdrop

```powershell
docker compose -f kafka/docker-compose.yaml up -d
```

Kafdrop UI: `http://localhost:9000`

Stop Kafka stack:

```powershell
docker compose -f kafka/docker-compose.yaml down
```

### 3) Start one employees app pair

Base pair:

```powershell
cd employees-backend
.\mvnw.cmd spring-boot:run
```

```powershell
cd employees-frontend
.\mvnw.cmd spring-boot:run
```

Kafka pair:

```powershell
cd employees-backend-kafka
.\mvnw.cmd spring-boot:run
```

```powershell
cd employees-frontend-kafka
.\mvnw.cmd spring-boot:run
```

Open UI: `http://localhost:8080`

### 4) Optional config demo

```powershell
cd config-server-demo
.\mvnw.cmd spring-boot:run
```

```powershell
cd config-client-demo
.\mvnw.cmd spring-boot:run
```

## Startup Order

```mermaid
sequenceDiagram
    participant DB as PostgreSQL
    participant K as Kafka
    participant FE as employees-frontend(-kafka)
    participant BE as employees-backend(-kafka)
    participant CS as config-server-demo
    participant CC as config-client-demo

    Note over DB,BE: Employees stack
    DB->>BE: Accept DB connections
    BE->>BE: Apply Liquibase changelog
    FE->>BE: Call /api/employees
    FE->>K: Publish event (Kafka frontend variant)

    Note over CS,CC: Config demo stack
    CS->>CS: Load config from local Git path
    CC->>CS: Request configuration
```

## Useful Endpoints

- Frontend UI: `http://localhost:8080`
- Backend API list: `GET http://localhost:8081/api/employees`
- Backend API by id: `GET http://localhost:8081/api/employees/{id}`
- Config client demo: `GET http://localhost:8080/api/hello` (when running `config-client-demo`)
- Kafdrop: `http://localhost:9000`

Ready-to-run API request collections:

- `employees-backend/employees.http`
- `employees-backend-kafka/employees.http`

## Notes

- Config Server points to `file:///C:/Training/config`; adjust `config-server-demo/src/main/resources/application.properties` if needed.
- Kafka topics used by the Kafka variant: `employees-backend-request`, `employees-backend-events`, and `employees-backend-response`.
- Use the `employees-backend-*` topic prefix consistently (avoid the `employees-bakcend-*` typo).
- Create flow: frontend publishes `CreateEmployeeRequest` to `employees-backend-request`; backend listener persists to DB; backend publishes `EmployeeHasBeenCreatedEvent` to `employees-backend-events`.
- Tutor repository reference is listed near the top of this README.
- `java-se/java-se` is a separate Maven module and currently targets Java 26 (`maven.compiler.source/target`).
- `UserController` in frontend modules references extra auth-related properties for some scenarios.
