# Spring Cloud Training Workspace

This repository contains Spring Boot/Spring Cloud demos across four employees app variants: plain REST, raw Kafka, Spring Cloud Stream, and Spring Cloud Stream + Avro.

## Tutor Repository

- [Training360/javax-sc-2026-05-06](https://github.com/Training360/javax-sc-2026-05-06)

## What Is In This Repo

### Base pair (plain REST)

- `employees-backend`: REST API + PostgreSQL + Liquibase (`:8081`)
- `employees-frontend`: Thymeleaf UI, REST client (`:8080`)

### Kafka pair (raw Spring Kafka)

- `employees-backend-kafka`: adds `KafkaGateway` with `@KafkaListener` (`:8081`)
- `employees-frontend-kafka`: sends create via raw `KafkaTemplate` (`:8080`)

### Stream pair (Spring Cloud Stream)

- `employees-backend-stream`: Spring Cloud Stream + Kafka binder (`:8081`)
- `employees-frontend-stream`: sends create via `StreamBridge` (`:8080`)

### Stream + Avro pair (Spring Cloud Stream + Schema Registry)

- `employees-backend-avro`: Spring Cloud Stream + Avro serialization (`:8081`)
- `employees-frontend-avro`: Avro producer/consumer with schema-aware messaging (`:8080`)

### Infra / other

- `kafka/`: Docker Compose — single-node KRaft Kafka + Kafdrop
- `employees-schema-registry`: Schema Registry for Kafka message schemas (`:8990`)
- `config-server-demo`: Spring Cloud Config Server (`:8888`)
- `config-client-demo`: Spring Cloud Config Client sample
- `java-se`: standalone Java SE Maven project (`training:java-se`)

## Spring Ecosystem Overview

| Project | Role |
| --- | --- |
| **Spring Framework** | Core foundation: DI, IoC, AOP, transactions, MVC/WebFlux |
| **Spring Boot** | Auto-configuration, embedded servers, sensible defaults, production features |
| **Spring Cloud** | Distributed systems toolbox: config, discovery, circuit breakers, gateways, tracing |
| **Spring Data** | Repository abstractions over SQL, JPA, JDBC, NoSQL, Redis, Elasticsearch |
| **Spring Security** | Auth, authorization, OAuth2, JWT, CSRF, method security |
| **Spring Integration** | Enterprise integration patterns: channels, routers, adapters |
| **Spring Batch** | Large-scale batch processing: chunked reads/writes, retries, restarts |
| **Spring AI** | LLM abstractions, embeddings, vector stores, RAG (not covered in this course) |

Spring Boot apps follow the [12-factor app methodology](https://12factor.net/).

## Training Themes

This workspace is centered around a typical microservice toolchain and the Spring portfolio that supports it.

- Microservice-oriented application design
- Modern Java language features on JDK 26
- Messaging with Kafka, Spring for Apache Kafka, and Spring Cloud Stream
- Event-driven architecture and Enterprise Integration Patterns
- Configuration management with Spring Cloud Config
- Security topics around OAuth2 and OpenID Connect
- Reliability topics around retries, resilience, and circuit breakers
- Observability and operations, including OpenTelemetry and Spring Boot Admin
- API Gateway and Backend for Frontend patterns

Not covered in this training repo, but relevant nearby topics:

- RabbitMQ as an alternative message broker for Spring Cloud Stream binders
- Spring AI for LLM integration
- GPU/model-training work in Java via newer platform initiatives and JEPs

## Java SE Language Showcase (`java-se`)

Modern Java 26 features demonstrated in this module:

- **Records** — immutable domain models: `Pont`, `Kor`, `Teglalap`
- **Sealed classes** — explicit permitted hierarchy: `Alakzat permits Kor, Teglalap`
- **Pattern matching `switch`** over a sealed type in `Main#szamitKozeppont`
- **Record patterns** in `switch` cases (`case Kor(Pont kozeppont, _)`)
- **Unnamed variables** — `_` wildcard in record patterns
- **Local variable type inference** — `var`
- **Virtual threads** — lightweight concurrency via `Thread.ofVirtual()` (Project Loom)
- **Data-oriented programming** — prefer immutable value types over deep OOP hierarchies; reduce class proliferation with sealed types + pattern matching

Practical direction from the course notes:

- Prefer immutable types where possible
- Avoid unnecessary class hierarchies when a local decision point or simple data shape is enough
- Keep abstractions justified by multiple implementations, not by habit

## Architecture

### Base pair

```mermaid
flowchart LR
    Browser[Browser] --> FE["employees-frontend :8080"]
    FE -->|REST| BE["employees-backend :8081"]
    BE --> PG[(PostgreSQL :5432)]
```

### Kafka pair

```mermaid
flowchart LR
    Browser[Browser] --> FEK["employees-frontend-kafka :8080"]
    FEK -->|REST GET list| BEK["employees-backend-kafka :8081"]
    FEK -->|publish CreateEmployeeRequest to employees-backend-request| Kafka["Kafka broker :9092"]
    Kafka -->|consume employees-backend-request| BEK
    BEK -->|publish EmployeeHasBeenCreatedEvent to employees-backend-events| Kafka
    BEK --> PG[(PostgreSQL :5432)]
    Kafdrop["Kafdrop :9000"] --> Kafka
```

### Stream pair

```mermaid
flowchart LR
    Browser[Browser] --> FES["employees-frontend-stream :8080"]
    FES -->|REST GET list| BES["employees-backend-stream :8081"]
    FES -->|StreamBridge backend-request to employees-backend-request| Kafka["Kafka broker :9092"]
    Kafka -->|binder consume createEmployeeFunction-in-0| BES
    BES -->|binder publish createEmployeeFunction-out-0 to employees-backend-response| Kafka
    BES --> PG[(PostgreSQL :5432)]
    Kafdrop["Kafdrop :9000"] --> Kafka
```

### Stream plus Avro pair

```mermaid
flowchart LR
    Browser[Browser] --> FEA["employees-frontend-avro :8080"]
    FEA -->|REST GET list| BEA["employees-backend-avro :8081"]
    FEA -->|Avro publish employees-backend-request| Kafka["Kafka broker :9092"]
    BEA -->|Avro publish employees-backend-response| Kafka
    FEA -->|schema resolve/register| SR["employees-schema-registry :8990"]
    BEA -->|schema resolve/register| SR
    BEA --> PG[(PostgreSQL :5432)]
```

### Config demo

```mermaid
flowchart LR
    Client[config-client-demo] --> CS["config-server-demo :8888"]
    CS --> Repo[(Local Git config repo)]
```

## Important Choice

Run only one employees pair at a time:

- base pair: `employees-backend` + `employees-frontend`
- Kafka pair: `employees-backend-kafka` + `employees-frontend-kafka`
- Stream pair: `employees-backend-stream` + `employees-frontend-stream`
- Stream + Avro pair: `employees-backend-avro` + `employees-frontend-avro`

All four pairs use the same ports (`8080` and `8081`), so running more than one together causes port conflicts.

## Kafka Infra (`kafka/docker-compose.yaml`)

Single-node KRaft cluster (no ZooKeeper).

| Service | Image | Ports |
| --- | --- | --- |
| `kafka` | `apache/kafka:4.2.0` | `9092` (host access) |
| `kafdrop` | `obsidiandynamics/kafdrop:4.2.0` | `9000` (web UI) |

Listener layout:

- `EXTERNAL` (`:9092`) for host-machine clients
- `CLIENT` (`kafka:9093`) for Docker-network clients (for example Kafdrop)
- `CONTROLLER` (`:9094`) for KRaft controller traffic

**Optional**: `employees-schema-registry` (port `:8990`) is a separate Spring Boot app for managing Avro/JSON schemas. Start it with `cd employees-schema-registry; .\mvnw.cmd spring-boot:run` if using schema-based serialization in the Stream variant.

## Prerequisites

- JDK 26
- Git
- IntelliJ IDEA Ultimate
- Docker (PostgreSQL + Kafka)
- Maven Wrapper (`mvnw`/`mvnw.cmd`) included in each module — no separate Maven install needed

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

Stream pair:

```powershell
cd employees-backend-stream
.\mvnw.cmd spring-boot:run
```

```powershell
cd employees-frontend-stream
.\mvnw.cmd spring-boot:run
```

Stream + Avro pair:

```powershell
cd employees-schema-registry
.\mvnw.cmd spring-boot:run
```

```powershell
cd employees-backend-avro
.\mvnw.cmd spring-boot:run
```

```powershell
cd employees-frontend-avro
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

### Base pair startup

```mermaid
sequenceDiagram
    participant DB as PostgreSQL
    participant BE as employees-backend
    participant FE as employees-frontend

    DB->>BE: Accept DB connections
    BE->>BE: Apply Liquibase changelog
    FE->>BE: GET /api/employees (list)
    FE->>BE: POST /api/employees (create)
    BE->>DB: Persist new employee
```

### Kafka pair startup

```mermaid
sequenceDiagram
    participant DB as PostgreSQL
    participant K as Kafka broker
    participant BEK as employees-backend-kafka
    participant FEK as employees-frontend-kafka

    DB->>BEK: Accept DB connections
    BEK->>BEK: Apply Liquibase changelog
    FEK->>BEK: GET /api/employees (list)
    FEK->>K: KafkaTemplate.send CreateEmployeeRequest to employees-backend-request
    K->>BEK: @KafkaListener consumes employees-backend-request
    BEK->>DB: Persist new employee
    BEK->>K: Publish EmployeeHasBeenCreatedEvent to employees-backend-events
```

### Stream pair startup

```mermaid
sequenceDiagram
    participant DB as PostgreSQL
    participant K as Kafka broker
    participant BES as employees-backend-stream
    participant FES as employees-frontend-stream

    DB->>BES: Accept DB connections
    BES->>BES: Apply Liquibase changelog
    FES->>BES: GET /api/employees (list)
    FES->>K: StreamBridge.send via backend-request binding to employees-backend-request
    K->>BES: Binder consumes createEmployeeFunction-in-0
    BES->>DB: Persist new employee
    BES->>K: Binder publishes createEmployeeFunction-out-0 to employees-backend-response
```

### Stream plus Avro pair startup

```mermaid
sequenceDiagram
    participant DB as PostgreSQL
    participant K as Kafka broker
    participant SR as Schema Registry
    participant BEA as employees-backend-avro
    participant FEA as employees-frontend-avro

    DB->>BEA: Accept DB connections
    BEA->>BEA: Apply Liquibase changelog
    FEA->>BEA: GET /api/employees (list)
    FEA->>SR: Resolve/register request schema
    FEA->>K: Publish Avro CreateEmployeeRequest to employees-backend-request
    K->>BEA: Binder consumes request
    BEA->>DB: Persist new employee
    BEA->>SR: Resolve/register response schema
    BEA->>K: Publish Avro CreateEmployeeResponse to employees-backend-response
    K->>FEA: Binder consumer receives response
```

## Architectural Notes

- **EDA**: event-driven architecture is a natural fit for Kafka and Spring Cloud Stream. Spring Integration is the Spring project that most directly implements the Enterprise Integration Patterns vocabulary.
- **Raw Kafka vs Stream**: the Kafka pair shows explicit topic handling with `KafkaTemplate` and `@KafkaListener`; the Stream pairs move messaging behind logical bindings and functions.
- **Binder portability**: Spring Cloud Stream keeps the code focused on message contracts. Switching from Kafka to another binder such as RabbitMQ is primarily a configuration concern.
- **Consumer groups and offsets**: Kafka tracks offsets per consumer group, so different groups can consume the same topic independently.
- **API Gateway**: a gateway can centralize routing, authentication, authorization, logging, rate limiting, and resilience policies in front of multiple services.
- **Backend for Frontend**: a BFF can shape data for a specific UI, reduce chatty frontend traffic, and perform API composition for web vs mobile clients.
- **API composition**: when one screen needs data from several services, composition can live in the frontend, a dedicated composition service, or a BFF depending on ownership and latency constraints.
- **Observability**: distributed systems become much easier to debug once metrics, logs, traces, and correlation IDs are treated as first-class concerns.
- **SBOM awareness**: software bills of materials matter when you need to understand dependency exposure and security posture across multiple services.

## Useful Endpoints

- Frontend UI: `http://localhost:8080`
- Backend API list: `GET http://localhost:8081/api/employees`
- Backend API by id: `GET http://localhost:8081/api/employees/{id}`
- Config client demo: `GET http://localhost:8080/api/hello` (when running `config-client-demo`)
- Kafdrop: `http://localhost:9000`

Ready-to-run API request collections:

- `employees-backend/employees.http`
- `employees-backend-kafka/employees.http`
- `employees-backend-stream/employees.http`
- `employees-backend-avro/employees.http`

## Reading Material

- [Microservice patterns](https://microservices.io/patterns/)
- [12-factor app](https://12factor.net/)
- [Enterprise Integration Patterns](https://www.enterpriseintegrationpatterns.com/)
- [Spring Integration](https://spring.io/projects/spring-integration)
- [Spring Cloud Stream](https://spring.io/projects/spring-cloud-stream)
- [Spring Cloud Schema Registry](https://docs.spring.io/spring-cloud-schema-registry/docs/current/reference/html/spring-cloud-schema-registry.html)
- [Spring Cloud Circuit Breaker overview](https://www.baeldung.com/spring-cloud-circuit-breaker)
- [Spring Core resilience features](https://spring.io/blog/2025/09/09/core-spring-resilience-features)
- [Resilience4j getting started](https://resilience4j.readme.io/docs/getting-started)
- [Chaos Monkey for Spring Boot](https://codecentric.github.io/chaos-monkey-spring-boot/)
- [Spring Modulith](https://www.jtechlog.hu/2022/12/19/spring-modulith.html)
- [Spring Boot Admin getting started](https://docs.spring-boot-admin.com/3.0.0/getting-started.html)
- [API Gateway overview](https://www.ibm.com/think/topics/api-gateway)
- [JPA equals/hashCode deep-dive](https://jpa-buddy.com/blog/hopefully-the-final-article-about-equals-and-hashcode-for-jpa-entities-with-db-generated-ids/)
- [QUIC / HTTP3](https://www.f5.com/glossary/quic-http3)

## Notes

- Config Server points to `file:///C:/Training/config`; adjust `config-server-demo/src/main/resources/application.properties` if needed.
- **Base**: create goes directly via `POST /api/employees` REST call.
- **Kafka**: create goes via raw `KafkaTemplate` → topic `employees-backend-request` → `@KafkaListener`; backend then publishes `EmployeeHasBeenCreatedEvent` to `employees-backend-events`. Use the `employees-backend-*` prefix consistently.
- **Stream**: create goes via `StreamBridge` → logical binding `backend-request` (mapped to Kafka topic `employees-backend-request` via `spring.cloud.stream.bindings` in frontend config) → Spring Cloud Stream Kafka binder consumes via `Function<CreateEmployeeRequest, CreateEmployeeResponse>` bean. Swapping the binder (e.g. RabbitMQ) requires no code change, only config.
- All three variants use REST (`GET /api/employees`) for listing.
- Tutor repository reference is listed near the top of this README.
- `java-se` is a standalone Maven module and currently targets Java 26 (`maven.compiler.source/target`).
- `employees-schema-registry` uses Spring Cloud Stream Schema Registry for Avro/JSON schema management and validation.
- Kafka consumer groups share an offset per group: each consumer group independently tracks which messages it has consumed; the broker maintains the offsets.
- `UserController` in frontend modules references extra auth-related properties for OAuth2/OIDC scenarios (Keycloak integration).
- API gateways and BFFs are not implemented in this repo, but they are natural next steps once the number of services and frontend consumers grows.
