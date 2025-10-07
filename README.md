# Social Network MDD

Social Network MDD (Monde de Dév) is a platform designed to help developers connect, collaborate, and create a talent pool for recruitment. This README documents the setup, structure, and implemented functionalities for the **MVP version** of the project.

## Table of Contents

1. Prerequisites  
2. Clone the Project from GitHub  
3. Database Setup  
4. Set Environment Variables  
5. Back-End Setup  
6. Front-End Setup  
7. Running the Application  
8. MVP Functionalities  
   - Back-End  
   - Front-End  
9. Documentation
   - Javadoc
   - Swagger
10. Technologies & Libraries Used  
11. Troubleshooting  

---

## 1. Prerequisites

Before running the application, you need the following tools installed:

- **Node.js** (version 18)  
- **Angular CLI** (version 18)  
- **MySQL**  
- **Java 17**  
- **Maven**  

---

## 2. Clone the Project from GitHub

Open your terminal or command prompt and run:
```
git clone https://github.com/Erika-Belicova/social-network-mdd.git

cd social-network-mdd
```

The repository contains two main folders:

- **back** – Spring Boot back-end  
- **front** – Angular front-end  

---

## 3. Database Setup

Make sure MySQL is installed and running.

### Create the Database

Run the following commands in your MySQL client:
```
CREATE DATABASE mdd;

USE mdd;
```

- The database `mdd` will be used by the back-end  
- The tables (User, Topic, Post, Comment) are automatically created and managed by JPA (`spring.jpa.hibernate.ddl-auto=update`) when the back-end runs for the first time

### Start the Back-End Once

Start the back-end so that JPA can create the necessary tables:
```
cd back

mvn spring-boot:run
```

### Adjust Column Sizes for Long Text

By default, Spring Boot generates `VARCHAR(255)` for string fields. To allow longer text for topics, posts, and comments, run the following SQL commands to adjust the column sizes:
```
ALTER TABLE TOPICS MODIFY description VARCHAR(1000);
ALTER TABLE POSTS MODIFY content VARCHAR(1000);
ALTER TABLE COMMENTS MODIFY content VARCHAR(1000);
```
- This allows topic descriptions and the content of posts and comments to store up to 1000 characters.

### Add Pre-Existing Topics

The MVP requires at least one topic in the database to work correctly. As this MVP version does not include an admin or back-office interface, add topics manually using SQL commands. Include timestamps to match the entity fields.

It is recommended to add 3–4 topics to properly test the application, for example:
```
INSERT INTO TOPICS (title, description, created_at, updated_at) VALUES ('Java', 'All things Java programming', NOW(), NOW());
INSERT INTO TOPICS (title, description, created_at, updated_at) VALUES ('Spring Boot', 'Back-end development with Spring Boot', NOW(), NOW());
INSERT INTO TOPICS (title, description, created_at, updated_at) VALUES ('Angular', 'Front-end development with Angular', NOW(), NOW());
INSERT INTO TOPICS (title, description, created_at, updated_at) VALUES ('SQL', 'Database management and queries', NOW(), NOW());
```
- Ensure at least one topic exists before continuing.

---

## 4. Set Environment Variables

Set the following system environment variables for database connection and JWT authentication:
```
DATABASE_USERNAME=your_mysql_username

DATABASE_PASSWORD=your_mysql_password

JWT_SECRET_KEY=your_jwt_secret_key
```

- Replace `your_mysql_username` and `your_mysql_password` with your MySQL credentials  
- `JWT_SECRET_KEY` is used for signing and verifying tokens  

---

## 5. Back-End Setup

Navigate to the back-end directory and install dependencies:
```
cd back

mvn clean install
```

Start the back-end application:

```
mvn spring-boot:run
```

- The API will be available at **http://localhost:3001**  

---

## 6. Front-End Setup

Navigate to the front-end directory and install dependencies:
```
cd front

npm install
```

Start the front-end application:

```
npm run start
```

- The Angular app will be available at **http://localhost:4200**  

---

## 7. Running the Application

1. Ensure MySQL is running and the `mdd` database is accessible  
2. Start the back-end:
 
   ```
   cd back
   
   mvn spring-boot:run
   ```
   
3. Start the front-end:
 
   ```
   cd front

   npm run start
   ```
   
4. Open your browser at **http://localhost:4200**

You should see the front-end UI, which will interact with the back-end API running at **http://localhost:3001**

---

## 8. MVP Functionalities

### Back-End

- **User Management:** Registration, login, logout, profile view/update; optional password update; password encryption; JWT authentication  
- **Topic Subscriptions:** List all topics, subscribe/unsubscribe, manage user-specific subscriptions  
- **Posts & Comments:** Create posts, view posts, add comments (non-recursive); author and date automatically set  
- **DTOs & Mapping:** Request and response DTOs for secure data transfer; ModelMapper automates conversion  
- **Exception Handling:** Global @ControllerAdvice and custom exceptions for specific cases  
- **Database:** MySQL with JPA entities: User, Topic, Post, Comment  
- **Security:** Spring Security + JWT; unauthorized access returns HTTP 401  

### Front-End

- **Components:** Home, Login, Register, PostList, TopicList, PostForm, PostDetail, Me, NotFound  
- **Navigation & Routing:** AppRoutingModule, AuthGuard for protected routes, routerLink navigation, dynamic navbar, NotFound component for invalid URLs  
- **Forms & Validation:** ReactiveForms for validation, Snackbars for error messages, optional password update  
- **Data Flow:** RxJS Observables for async data handling; strongly typed DTOs  
- **UI & Responsiveness:** Angular Material components where possible; CSS with media queries for mobile (≤768px); hamburger menu on mobile  
- **Synchronization:** DTO mapping ensures front-end logic matches back-end services and maquettes  

---

## 9. Documentation

### Javadoc

Each class in the back-end has a Javadoc comment explaining its purpose.

To generate Javadoc locally:
```
cd back

mvn javadoc:javadoc
```
- Open the generated documentation in your browser: **target/reports/apidocs/index.html**

### Swagger

The back-end API documentation is available at: **http://localhost:3001/swagger-ui.html**  

- Provides details on all API endpoints for testing and reference  

---

## 10. Technologies & Libraries Used

**Back-End:** Java 17, Spring Boot, Spring Security, JWT, MySQL, Maven, JPA, ModelMapper, Javadoc, Swagger UI  

**Front-End:** Angular 18, Angular CLI 18.2.20, Node.js 18, ReactiveForms, Angular Material, RxJS, CSS with media queries  

---

## 11. Troubleshooting

- **Back-End does not start:** Verify that MySQL server is running and ensure environment variables are set. Then, ensure dependencies are installed:
  
  ```
  mvn clean install
  mvn spring-boot:run
  ```
  
- **Front-End does not start:** Ensure dependencies are installed
  
  ```
  npm install
  npm run start
  ```
   
- **API endpoints not responding:** Confirm back-end is running at **http://localhost:3001** and JWT secret is configured  
- **Mobile UI issues:** Verify screen width ≤768px triggers responsive layout  
