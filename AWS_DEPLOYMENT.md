# AWS Deployment Guide

This microservice is containerized and stateless, making it ideal for deployment on **AWS App Runner** (Managed Container Service) or **Amazon ECS**.

## Prerequisites
* AWS CLI installed and configured (`aws configure`).
* Docker Desktop installed and running.
* An active AWS account.
* AmazonEC2ContainerRegistryFullAccess
* AppRunnerFullAccess
* AmazonRDSFullAccess
* A default VPC

## Phase 1: Database Setup (RDS)
1. Open AWS Console → RDS → Create Database
2. **Choose**:
    * Easy Create
    * PostgreSQL

* Settings
  | Setting         | Value                     |
  | --------------- | ------------------------- |
  | DB Identifier   | `medical-measurements-db` |
  | Master Username | `<your-username>`         |
  | Master Password | `<your-password>`         |
  | DB Name         | `medicaldb`               |

  Instance size: smallest available (db.t3.micro / db.t4g.micro)

3. Connectivity
   * Select your default VPC
   * Create or choose a security group
   * Add inbound rule: Type: PostgreSQL 
   * Port: 5432
4. After creation note down the **Endpoint URL**, **Master Username**, and **Master Password**.

## Phase 2: Build & Push Docker Image to AWS ECR
We need to upload the Docker image to AWS.

1. **Create a Repository:**
   ```bash
   aws ecr create-repository --repository-name medical-measurements-service --region eu-north-1

2. **Login Docker to AWS:**

    ```bash
   aws ecr get-login-password --region eu-north-1 | docker login --username AWS --password-stdin <your-account-id>.dkr.ecr.eu-north-1.amazonaws.com

3. Build the Docker Image
   Mac M1/M2/M3:
   ```bash
   docker build --platform linux/amd64 -t medical-measurements-service .
   ```
   Others:
   ```bash
   docker build -t medical-measurements-service .
   ```
4. Tag and Push the Image
   ```bash
    docker tag medical-measurements-service:latest \
    <your-account-id>.dkr.ecr.eu-north-1.amazonaws.com/medical-measurements-service:latest
    
    docker push \
    <your-account-id>.dkr.ecr.eu-north-1.amazonaws.com/medical-measurements-service:latest


## Phase 3: Deploy Service (AWS App Runner)
App Runner is the easiest way to run containers.

1. Open App Runner → Create Service
2. Source & Deployment: 
    * Container Registry → Amazon ECR
    * Select the container image you pushed
    * Deployment method: Automatic
3. Service Configuration

   | Setting      | Value                           |
   | ------------ | ------------------------------- |
   | Service Name | `medical-service` (or any name) |
   | Port         | `8080`                          |

4. Create ECR access role or use existing.

5. ** Add Environment Variables:**
   Add the following variables to override your local `application.yml` settings:

   | Key | Value                                                                            |
      | :--- |:---------------------------------------------------------------------------------|
   | `SPRING_PROFILES_ACTIVE` | `prod`                                                                           |
   | `SPRING_DATASOURCE_URL` | `jdbc:postgresql://<endpoint>:5432/medicaldb?sslmode=require`                    |
   | `SPRING_DATASOURCE_USERNAME` | `medical`                                                                       |
   | `SPRING_DATASOURCE_PASSWORD` | `<your-rds-password>`                                                            |
   | `SPRING_JPA_HIBERNATE_DDL_AUTO` | `validate`                                                                       |

TLS parameter sslmode=require is important for sensitive data.

6. Click **Create & Deploy**.


## Phase 4: Verification
Once the App Runner service status shows Running, verify that the microservice is healthy and able to communicate with the database.

1. Copy the **service-url** provided by App Runner (`https://xyz.awsapprunner.com`).
   * Health Check
   ```bash
   GET https://<service-url>/actuator/health
   ```
   * Expected Response
   ```bash
    {
    "status": "UP"
    }
   ```
2. View API Documentation (Swagger UI)
   * Open in browser:
      ```bash
      https://<service-url>/swagger-ui/index.html
      ```
3. You should see the Swagger UI for the Medical Measurements API, including:
      ```bash
     *POST /api/measurements
     *GET /api/measurements

