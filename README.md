# Customer Service API

## Overview
This is a Spring Boot-based Customer Service application that provides basic CRUD operations through a RESTful API. It also includes observability metrics, containerization, and Kubernetes deployment support for enhanced functionality and scalability.

## Features
- RESTful API with CRUD operations for Customer entity
- Observability metrics with Micrometer and Prometheus
- Dockerized for containerized deployment
- Kubernetes configuration files for deployment in a Kubernetes cluster
- CI/CD integration with GitHub Actions

## Prerequisites
- **Java 17** (Temurin recommended)
- **Docker** and **Docker Compose** installed
- **Kubernetes** installed (e.g., with [Kind](https://kind.sigs.k8s.io/) for local clusters)

## Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/xyHarry/customer-service.git
cd customer-service
```

### 2. Build the Project
Use Maven to build the project:
```bash
mvn clean install
```

### 3. Running Locally
```bash
mvn spring-boot:run
```
The application will be accessible at http://localhost:8080.

Test the API using `curl` commands:

- **POST** a new customer:
  ```bash
  curl -X POST http://localhost:8080/api/customers -H "Content-Type: application/json" -d '{
    "firstName": "harry",
    "lastName": "xu",
    "emailAddress": "hxu@example.com",
    "phoneNumber": "123-456-7890"
  }'
  ```

- **GET** all customers:
  ```bash
  curl http://localhost:8080/api/customers
  ```

- **GET** a specific customer by ID:
  ```bash
  curl http://localhost:8080/api/customers/{customerId}
  ```
  Replace `{customerId}` with the UUID of the customer returned from the POST request.


### 4. Running with Docker
Build and run the Docker container:
```bash
docker build -t customer-service .
docker run -p 8080:8080 customer-service
```

### 5. Deploying on Kubernetes
Ensure your Kubernetes cluster is running and that you've loaded the Docker image to the cluster if using a local setup (e.g., Kind).

Apply the Kubernetes configuration:
```bash
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

### 6. Observability
Metrics are exposed at /actuator/metrics. If running with Prometheus, it will scrape metrics automatically at the specified intervals.

### 7.CI/CD Pipeline
This project includes a GitHub Actions workflow for CI/CD, located in .github/workflows/ci.yml. It runs on each push to the main branch, executing build, test, and Docker build steps.







