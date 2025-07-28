# 🐾 Pet Owner Management System

This is a Spring Boot 3 application for managing pets and their owners. It supports features like:

- Registering pets and users
- Assigning multiple owners to a pet
- Querying pets by city or user
- Marking pets as deceased
- H2 in-memory or file-based database support
- Integrated with SonarQube for static code analysis

---

## 🔧 Technologies Used

- Java 17+
- Spring Boot 3
- Spring Data JPA
- H2 Database
- Maven or Gradle
- SonarQube
- JUnit + MockMvc (Integration Testing)

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/petcare/petowner/
│   │   ├── controller/
│   │   ├── entity/
│   │   ├── repository/
│   │   ├── service/
│   │   └── PetOwnerApplication.java
│   └── resources/
│       ├── application.properties
│       
└── test/
    └── java/com/petcare/petowner/
```

---

## ⚙️ Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/sourabhjinde/petowner.git
cd petowner
```

### 2. Configure H2 Database (File-Based Persistence)

In `src/main/resources/application.properties`:

```properties
spring.application.name=petowner
spring.datasource.url=jdbc:h2:file:./data/petdb;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update
```

Access the H2 console at:  
**http://localhost:8080/h2-console**  
Use JDBC URL: `jdbc:h2:file:./data/petdb`

---

### 3. Build & Run the Application

#### Using Maven:
```bash
./mvnw spring-boot:run
```

#### Using Gradle:
```bash
./gradlew bootRun
```

---

## 🧪 Running Tests

```bash
./gradlew test
```

---

## ✅ REST API Endpoints

| Method | Endpoint                              | Description                          |
|--------|---------------------------------------|--------------------------------------|
| GET    | `/pets/user/{userId}`                 | Get pets for a user                  |
| GET    | `/pets/city/{city}`                   | Get pets by city                     |
| PATCH  | `/pets/{petId}/death`                 | Mark a pet as deceased               |
| GET    | `/pets/female/{city}`                 | Pets owned by females in a city      |
| GET    | `/pets/owner?name=X&firstName=Y`      | Get pets by owner's full name        |

---


## ⚠️ Notes

- **Avoids N+1 problem** using `@EntityGraph` or `JOIN FETCH` in queries.
- **Pets can have multiple owners** from different addresses.
- **Addresses** are reused via address hash logic.

---

## 📄 License

This project is licensed under the MIT License.

---

Made using Spring Boot 3.
