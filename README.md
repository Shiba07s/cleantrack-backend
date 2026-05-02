# ClearTrack

ClearTrack is a Spring Boot REST API for tracking public cleanliness reports. Users can register, log in, submit reports with location details and an image, and view their submitted reports. Admin endpoints can review all reports, approve or reject reports, and award reward points when reports are approved. Uploaded images are stored in AWS S3.

## Tech Stack

- Java 17
- Spring Boot 3.5.11
- Spring Web
- Spring Data JPA
- MySQL
- AWS SDK for Java S3
- Lombok
- Springdoc OpenAPI / Swagger UI
- Maven Wrapper

## Project Structure

```text
src/main/java/com/cleartrack/ClearTrack
|-- ClearTrackApplication.java
|-- config
|   `-- AwsConfig.java
|-- controller
|   |-- AdminController.java
|   |-- AuthController.java
|   `-- ReportController.java
|-- entity
|   |-- Report.java
|   `-- User.java
|-- repositories
|   |-- ReportRepository.java
|   `-- UserRepository.java
`-- services
    |-- ReportService.java
    |-- S3Service.java
    `-- UserService.java
```

## Features

- User registration and login
- Report creation with image upload
- User-specific report listing
- Report lookup by ID
- Admin report dashboard endpoints
- Report approval and rejection
- Reward point increment on report approval
- After-cleaning image upload
- AWS S3 image storage
- OpenAPI documentation through Swagger UI

## Prerequisites

- Java 17 or later
- MySQL running locally or remotely
- AWS S3 bucket
- AWS access key and secret key with permission to upload objects to the bucket

## Configuration

Application configuration is stored in `src/main/resources/application.yaml`.

Current defaults:

```yaml
server:
  port: 1010

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cleantrack
    username: root
    password: ${password}

aws:
  accessKey: ${accessKeys}
  secretKey: ${secretKeys}
  region: ap-south-1
  bucketName: cleantrack-image
```

Create the MySQL database before running the app:

```sql
CREATE DATABASE cleantrack;
```

Set the required environment variables:

```powershell
$env:password="your_mysql_password"
$env:accessKeys="your_aws_access_key"
$env:secretKeys="your_aws_secret_key"
```

For macOS/Linux:

```bash
export password="your_mysql_password"
export accessKeys="your_aws_access_key"
export secretKeys="your_aws_secret_key"
```

## Run Locally

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

The API starts at:

```text
http://localhost:1010
```

Swagger UI is available at:

```text
http://localhost:1010/swagger-ui/index.html
```

## Build and Test

Run tests:

```powershell
.\mvnw.cmd test
```

Build the application:

```powershell
.\mvnw.cmd clean package
```

Run the packaged JAR:

```powershell
java -jar target\ClearTrack-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### Authentication

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/auth/register` | Register a user |
| `POST` | `/api/auth/login?email={email}&password={password}` | Log in a user |

Example register body:

```json
{
  "fullName": "Test User",
  "email": "test@example.com",
  "password": "password123",
  "role": "USER"
}
```

### Reports

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/reports/create/{userId}` | Create a report with image, description, and location |
| `GET` | `/api/reports/user/{userId}` | Get reports submitted by a user |
| `GET` | `/api/reports/{reportId}` | Get a report by ID |
| `GET` | `/api/reports/user/get-all` | Get all reports |
| `POST` | `/api/reports/upload-after-photo/{reportId}` | Upload an after-cleaning image and mark report as cleaned |

`/api/reports/create/{userId}` expects `multipart/form-data`:

| Field | Type | Required |
| --- | --- | --- |
| `image` | File | Yes |
| `description` | String | Yes |
| `location` | String | Yes |

### Admin

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/admin/reports` | Get all reports |
| `GET` | `/api/admin/pending/reports` | Get pending reports |
| `GET` | `/api/admin/approve/reports` | Get approved reports |
| `GET` | `/api/admin/reject/reports` | Get rejected reports |
| `PUT` | `/api/admin/approve/{id}` | Approve a report and add 10 reward points to the report owner |
| `PUT` | `/api/admin/reject/{id}` | Reject a report |

## Data Model

### User

| Field | Description |
| --- | --- |
| `id` | Primary key |
| `fullName` | User's full name |
| `email` | Unique email address |
| `password` | User password |
| `rewardPoints` | Points earned from approved reports |
| `role` | User role, such as `USER` or `ADMIN` |

### Report

| Field | Description |
| --- | --- |
| `id` | Primary key |
| `imageUrl` | Original report image URL from S3 |
| `afterImageUrl` | After-cleaning image URL from S3 |
| `description` | Report description |
| `location` | Report location |
| `status` | Report status: `PENDING`, `APPROVED`, `REJECTED`, or `CLEANED` |
| `createdAt` | Report creation timestamp |
| `user` | User who submitted the report |

## Notes

- Hibernate is configured with `ddl-auto: update`, so tables are created or updated automatically from the JPA entities.
- Passwords are currently stored and compared as plain text. Use password hashing before production use.
- Admin routes are not currently protected by authentication or authorization.
- AWS S3 upload URLs are built for the `ap-south-1` region.
- The configured S3 bucket is `cleantrack-image`; update `application.yaml` if you use a different bucket.
