# vehicle-system

Spring Boot REST API for managing agents and vehicles.

## Stack

- Java 17
- Spring Boot 4.0.5
- Spring Data JPA
- MySQL (`mysql-connector-j`) and H2

`src/main/resources/application.properties` uses an in-memory H2 database (`jdbc:h2:mem:vehicledb`, `H2Dialect`, `ddl-auto=create-drop`). The H2 console is enabled. `mysql-connector-j` remains on the classpath; commented MySQL settings in that file show an alternate datasource.

## Run

From the Maven project root (the repository root):

```bash
./mvnw spring-boot:run
```

The server listens on port 8080.

## Model

`Agent` has an id, an embedded `Name`, email, and mobile. `Vehicle` is an abstract JPA entity with joined inheritance, and stores an id, model, price, and agent. `Car`, `Truck`, and `Motorcycle` extend `Vehicle`. Controllers exist for agents, cars, and vehicles.

## Endpoints

### `/api/agent`

- `POST /api/agent` create
- `GET /api/agent` list
- `GET /api/agent/{id}` read
- `PUT /api/agent/{id}` update
- `DELETE /api/agent/{id}` delete
- `GET /api/agent/search` search by `firstName` or `lastName`

### `/api/cars`

- `POST /api/cars` create
- `GET /api/cars` list
- `GET /api/cars/{id}` read
- `PUT /api/cars/{id}` update
- `DELETE /api/cars/{id}` delete
- `GET /api/cars/search` search by `model` or `agentId`

### `/api/vehicles`

- `POST /api/vehicles` create
- `GET /api/vehicles` list
- `GET /api/vehicles/{id}` read
- `PUT /api/vehicles/{id}` update
- `DELETE /api/vehicles/{id}` delete
- `GET /api/vehicles/search` search by `model` or `agentId`

`GET /api/vehicles/search` also accepts `minPrice` and `maxPrice`, but those parameters are not applied.
