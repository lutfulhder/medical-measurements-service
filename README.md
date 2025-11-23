# Medical Measurements Microservice

A cloud-native Spring Boot microservice designed to collect, store, and retrieve periodic medical telemetry data (Blood Pressure & Heart Rate) from IoT devices.

## Key Features

* **Periodic Data Simulation (Virtual Device):**
    * Includes a built-in `MeasurementSimulator` that acts as a medical robot/sensor.
    * Automatically pushes telemetry data to the system every 5 seconds to demonstrate the "Periodic Receive" requirement without needing physical hardware.
* **Production-Grade Database:**
    * Uses **PostgreSQL** as the persistence layer.
    * Integrated with **Flyway** for strictly versioned, reproducible schema migrations.
* **Clean Architecture:**
    * Strict separation of concerns: `Controller → Service → Repository → Entity → Database`.
    * DTOs decoupled from Entities
    * Centralized Validation
    * Global Exception Handler
* **Observability:**
    * Auto-generated documentation via Swagger UI
    * Health & metrics via Spring Boot Actuator

## Tech Stack

* **Language:** Kotlin (JDK 21)
* **Framework:** Spring Boot 3.x
* **Database:** PostgreSQL 16 (Dockerized)
* **Migration:** Flyway
* **Testing:** JUnit 5, MockMvc, Testcontainers/H2
* **Docs:** JUnit 5, Mockito
* **Containerization:** Docker & Docker Compose

---

## Quick Start

You do not need to install PostgreSQL or configure local environment variables. Docker handles the entire infrastructure.

### 1. Start the System
Run the following command in the project root:

```bash
  docker-compose up -d
```
* Starts PostgreSQL container on port 5432.

* Initializes the database medicaldb.

### 2. Run the Application
You can run the app using your IDE (IntelliJ) or the command line:

```bash
  ./mvnw spring-boot:run
```
### 3. Verify it is Working
Once the application starts, check the console logs. You will see the **Simulator** pushing data automatically:

```text
INFO ... MeasurementSimulator : DEVICE_PUSH: Patient [patient-1] telemetry received
INFO ... MeasurementSimulator : DEVICE_PUSH: Patient [patient-3] telemetry received
```
## API Documentation

Access the interactive API documentation to try out the endpoints:

 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

| Method | Path                   | Description                      |
| ------ | ---------------------- | -------------------------------- |
| POST   | `/api/v1/measurements` | Store new measurement            |
| GET    | `/api/v1/measurements` | Fetch history (supports filters) |

## Usage Examples

* Create a measurement with Postman
```text
POST to  http://localhost:8080/api/v1/measurements \
"Content-Type: application/json" \
 '{
"patientId": "patient-123",
"systolic": 120,
"diastolic": 80,
"heartRate": 72
}'
```
* Search measurements with Postman
```bash
   http://localhost:8080/api/v1/measurements?patientId=patient-123

```
* Search with a time range
```bash
  http://localhost:8080/api/v1/measurements?patientId=patient-123&from=2025-11-23T00:00:00Z&to=2025-11-23T23:59:59Z

```

## GET Filtering Logic

| Parameter     | Optional? | Behavior                                 |
| ------------- | --------- | ---------------------------------------- |
| `patientId`   | Yes       | Filters by patient if provided           |
| `from` / `to` | Yes       | Defaults to last **24 hours** if omitted |

## API Documentation
  ```bash
   ./mvnw test
  ```
Unit tests mock the repository and verify:
* Measurement creation 
* Response mapping
* Search logic (patient-specific + global)

## Production Deployment (AWS)
Supports:
* AWS App Runner
* AWS ECS Fargate
* RDS PostgreSQL
* ECR for containers
