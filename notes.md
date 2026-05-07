Haladó Java képzés - Vicián István

2026. május 6., szerda
9:46

Commands:


Minden microservice alapon
Ehhez hogyan kapcsolódik a Spring portfolió
Ehhez modern nyelvi elemek
    • Rekordok
    • Seald classes
    • Pattern mathcing
    • Virtual thread
    • Unnamed variable?

Kafka + Spring
Spring for Apache Kafka + Spring Cloud stream
Spring cloud curcuit breaker -> hibajavítás
spring cloud gateway -> apie gateway
Spring security 2 - OAuth2, open id
Observability
OpenTelemetry
Rabbit MQ
Spring AI nem lesz

1 órás etapok, 10 perces szünetek
Ebéd 11:30
17:00-ig max, de nem valószínű, hogy 17:00 lesz

JDK 26
Git
Idea Ultimate
Docker

Java-ban GPU programozás, Java-ban modell training -> JEP már megvan

https://microservices.io/patterns/

https://www.f5.com/glossary/quic-http3

https://www.jtechlog.hu/2022/12/19/spring-modulith.html

Spring Boot
Makes Spring applications easy to start and run by providing auto-configuration, embedded servers, sensible defaults, and production features. It brought “convention over configuration” to Spring, removing a lot of XML/manual setup pain.
Spring Framework
The core foundation of the Spring ecosystem: dependency injection, inversion of control, AOP, transaction handling, MVC/WebFlux, and integration infrastructure. It brought a cleaner way to build Java enterprise apps without tightly coupling everything together.
Spring Cloud
A collection of tools for building distributed systems and microservices: configuration, service discovery, circuit breakers, gateways, tracing, and cloud-native patterns. It brought the “microservice toolbox” on top of Spring Boot.
Spring AI
Provides abstractions for working with LLMs, embeddings, vector stores, prompts, tools/function calling, and retrieval-augmented generation. It brings AI integration into Spring apps in a familiar Spring-style way.
Spring Data
Simplifies database access across SQL, NoSQL, Redis, Elasticsearch, MongoDB, JPA, JDBC, etc. It brought repository abstractions, query generation from method names, and less boilerplate for persistence code.
Spring Integration
Helps connect systems using enterprise integration patterns: message channels, routers, transformers, filters, adapters, etc. It brought a structured way to build event/message-driven integrations between applications and external systems.
Spring Batch
Framework for large-scale batch processing: reading, processing, and writing data in chunks, with retries, restarts, transactions, and job tracking. It brought industrial-strength ETL/job processing to Spring.
Spring Security
Handles authentication, authorization, password encoding, sessions, OAuth2, JWT, method security, CSRF protection, and more. It brought a comprehensive security layer that can be plugged into web apps, APIs, and enterprise systems.

Spring Boot app -> https://12factor.net/

Spring Core Retry
https://spring.io/blog/2025/09/09/core-spring-resilience-features
https://resilience4j.readme.io/docs/getting-started

Event Driven Achitecure - EDA
https://www.enterpriseintegrationpatterns.com/
Ezt implementálja a https://spring.io/projects/spring-integration

https://jpa-buddy.com/blog/hopefully-the-final-article-about-equals-and-hashcode-for-jpa-entities-with-db-generated-ids/




2.nap
Api Gateway
Backend for frontend

Üzleti alkalmazás nem használ reflectiont
Csak framework, de azt üzleti pénzen nem írunk :D

Java-ba most kezd megjelenni a Data oriented programming
OOP nagyon bonyi, feleslegesen elbúrjánzik?
Miért rakjuk oda mindenhova, ha csak egy helyen használjuk? -> 10-11 osztály kiváltása egy if segítségével
Legyen minden immutable

EDA-hoz
https://spring.io/projects/spring-cloud-stream
https://docs.spring.io/spring-cloud-schema-registry/docs/current/reference/html/spring-cloud-schema-registry.html

Kliens group id szerint is működik az közös offset-ezés
Melyik kliens melyik offset -> kafka szerver viszi tovább




+ absztrakciós szinte a cuircuit break-erhez, mivel csak egy implementáció van most még nem éri meg
https://www.baeldung.com/spring-cloud-circuit-breaker
https://codecentric.github.io/chaos-monkey-spring-boot/

SBOM -> függőségek

Spring boot admin
https://docs.spring-boot-admin.com/3.0.0/getting-started.html

Api-gateway -> FE csak az api gateway címét tudja, az api gateway az url alapján elirányítja a megfelelő végpontra, majd visszadobja a frontendnek

https://www.ibm.com/think/topics/api-gateway

Lehet load balancing is itt, authentikáció, authorizáció, itt is lehet oauth 2, után végig megy a tokennel a ms felé
Napllózni is tud, kérés/válaszokat

Ha van 5 ms, ami mindben van resilience 4 j és security probléma van, akkor mindben cserélni kell
Mi lenne ha lenne valami közbenső cucc, ami ezt kezelné -> apigateway lehet ms-ek között is, nem csak fe és be ms között

ESB? Lehet belső és külső api gateway is -> egy resilience 4 j és kész -> single point of failure, de állapotmentes, mehetne belőle 100

Cloud gateway-ek nem tudnak api compositiönt -> több ms-ből kérünk adatot, majd azt aggregáljuk -> megy tovább -> lehetne előre felé graphql majd hátra api gateway hívások

Hova lehet még tenni api compositiont rakni? Pl FE
Lehetne közbenső api composition ms is

Backend for frontend???
Granulált hálózati forgalom ellen jól jöhet
Ekkor a be for fe előrágja az fe-nek az adatokat, ott is lehet api composition
Graphql -like rest but with params
Lehet prootkoll fordítás is be for fe, vagy api gateway

BE for FE tipikusan arra let kitalálva, hogy más adat kell az asztali és mobil fe-nek, így két külön be for fe lehet.

Ki írja a BE for FE-t -> általában jobba, ha node-ban az FE-s írja meg, kevesebb turnover

Cache-t is tud
