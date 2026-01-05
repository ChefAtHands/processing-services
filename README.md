# processing-services

This module contains lightweight processing service used by the ChefAtHands backend. It includes small, focused Spring Boot services that perform data processing tasks such as categorization, filtering and recommendations.

Services inside this module:

- `recommendation-service` — Provides recipe recommendations based on user ingredients and preferences.

This README provides quick notes on how to run and find more documentation for each service.

Prerequisites
- Java 17 (or the project JDK configured in `pom.xml`)
- Maven 3.6+
- (optional) Docker for container runs

Build & run a single service
- From the repository root, change to the target service directory and use Maven:

```
cd processing-services/recommendation-service
mvn package
# then run
mvn spring-boot:run
```

Or run directly from the root using Maven's `-pl` option:

```
mvn -pl processing-services/recommendation-service -am spring-boot:run
```

Configuration
- Each service reads `application.properties` from `src/main/resources` and also supports overriding configuration via environment variables.
- See the individual service `README.md` files (for example `processing-services/recommendation-service/README.md`) for service-specific keys and examples.

API documentation
- This repository contains an API specification file at the project root: `api_specification.yaml`. That is the canonical source for public API contracts and should be consulted when integrating with frontend or gateway components.

Docker
- To build a container image for a service:

```
cd processing-services/recommendation-service
docker build -t chefathands/recommendation-service:latest .
```

- When running multiple services locally, prefer the `frontend-gateway` or docker-compose orchestrations to ensure ports, service discovery and env vars are wired correctly.