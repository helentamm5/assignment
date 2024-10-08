# Name matching service

<img src="https://www.nicepng.com/png/full/31-314820_logo-spring-spring-framework-logo-svg.png" width="350" height="250" />

## General information

- App is working on port: 8080
- Using Spring Boot
- Using Java 17
- Using Gradle
- Using H2 database

Compile the project using `gradle assemble`
Run the application with gradle task `gradle bootRun`

## Database

- **Driver Class**: org.h2.Driver
- **JDBC URL**: jdbc:h2:mem:testdb
- **User Name**: sa
- **Password**: password
- H2 database console URL:
  ```sh 
  http://localhost:8080/h2-console
  ```

## REST

### 1. Validate Sanctioned Person Name
**POST** `http://localhost:8080/api/sanctioned-person/check`

**Description**: Validates if the provided name matches any name in the sanctioned persons list

**Sample Request Body**:
```json
{
  "name": "Siim Suslik"
}
```
**Sample Response**:
```json
{
  "isNameSuspicious": "true",
  "matchingName": "Siim The Suslik"
}
```

### 2. Create Sanctioned Person
**POST** `http://localhost:8080/api/sanctioned-person`

**Description**: Creates sanctioned person with name provided in request body

**Sample Request Body**:
```json
{
  "name": "Siim Suslik"
}
```

### 3. Change Sanctioned Person
**PUT** `http://localhost:8080/api/sanctioned-person/{id}`

**Description**: Changes sanctioned person name

### 4. Delete Sanctioned Person
**DELETE** `http://localhost:8080/api/sanctioned-person/{id}`

**Description**: Deletes sanctioned person


