# VR ASSIGNMENT

## Overview
This project is a Spring Boot web service that listens to an AWS SQS queue, processes S3 notifications, reads a JSON file from an S3 bucket, transforms the message template from the CMS system structure to the domain structure, and stores the transformed message template in an AWS DynamoDB table.

If a message template with the same ID already exists in the DynamoDB table, it is updated with the new content.

## Tech Requirements
- **Java 21**
- **Spring Boot**
- **Maven**
- **AWS SDK** (for S3, SQS, and DynamoDB integration)
- **Localstack**
- **JUnit & Mockito**

## Functional Requirements
- Listen to an AWS SQS queue for S3 bucket notifications.
- Process the JSON file uploaded in AWS S3 and extract the text in it.
- Store the processed message template in AWS DynamoDB.
- Update an existing message template if a duplicate ID template is uploaded.

---

## Local Development Setup

### **1. Prerequisites**
Ensure you have the following installed locally:
- Java 21
- Maven
- Docker
- Aws-cli

### **2. Start Localstack (Local AWS Environment)**
Navigate to the `docker` folder and start the Localstack container:
```sh
cd docker
docker-compose up -d
```

To stop Localstack:
```sh
docker-compose down
```

### **3. Build and Run the Application**
Run the following command to build and start the Spring Boot service
```sh
mvn clean install
mvn spring-boot:run
```

### **4. Running Tests**
To execute unit tests:
```sh
mvn test
```

### **5. Test App localy**
- Go to `docker folder and run the fileUpload.py python file to upload template.json file to s3

```sh
cd docker/localstack
python fileUpload.py
```
-Or else use the aws commads to upload the test file (docker/localstack/template.json) to s3 

