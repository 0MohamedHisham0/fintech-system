# Fintech Transaction Management System

## Overview
This fintech system manages customer transactions, including deposits and withdrawals. It utilizes various technologies to ensure security and reliability. Key features include JWT authentication for secure access, pessimistic locking to prevent transaction conflicts, and Bcrypt for password encryption. The system also incorporates detailed logging with Log4j2, modular role-based access controls, and comprehensive API documentation through Swagger. For deployment, Docker is used to containerize and streamline application deployment.

![flow diagarm](https://github.com/user-attachments/assets/8a78abfc-483a-4652-a053-74cd89ea7109)

## Features

### Transaction Handling

- **Withdrawals and Deposits:** The system allows customers to perform transactions such as withdrawals and deposits.
- **Pessimistic Locking:** To ensure that concurrent transactions do not lead to inconsistent states.

### Authentication and Authorization

- **JWT Authentication:** JSON Web Tokens (JWT) are used for authentication. Each token is stored in the database, allowing for easy invalidation when needed.
- **Token Validation:** Tokens are validated based on user types, ensuring that only authorized users can access specific functionalities.
- **Bcrypt Password Encryption:** User passwords are encrypted using Bcrypt before being stored in the database.

### Logging

- **Log4j2:** Logging is managed using Log4j2. The configuration is defined in the log4j2.xml file, which allows customization of the log structure.
- **Log Files:** Logs are written to app.log, which is excluded from version control (GitHub) to maintain security and confidentiality.

### Testing

- **Unit Testing:** Unit Testing: The project uses ```@DataJpaTest``` with an in-memory H2 database for testing JPA components, and ```@Transactional``` ensures isolated test transactions. ```@ExtendWith(SpringExtension.class)``` integrates Spring's test support with JUnit 5. This setup verifies repository operations and business logic with automatic schema management and clean test states

### Swagger Documentation

- **API Documentation:** Swagger is implemented to provide API documentation. You can view the API documentation at the following URL:
```{{baseUrl}}/swagger-ui/index.html```

### Role Management

- **Permissions:** Each admin has a specific list of permissions that control access to restricted APIs. The system allows for the creation of roles with detailed permission lists to manage access control effectively.

### Exception Handling

- **Global Exception Handler:** The ControllerExceptionHandler class is used to handle exceptions globally. It centralizes exception handling, allowing developers to throw exceptions without needing to handle them explicitly in each controller.
Deployment

### Dockerization

- **Docker:** A Dockerfile is included for deploying the application. This ensures that the application can be containerized and deployed consistently across different environments.

## Possible Future Improvements

- **OTP for User Transactions:** Implement One-Time Password (OTP) verification for transactions to enhance security and prevent unauthorized actions.
- **Transaction Auditing:** Introduce detailed auditing for all transactions to track changes and provide an audit trail for security and compliance purposes.
- **Enhanced User Role Management:** Develop more granular user roles and permissions to allow finer control over access to different parts of the application.

# Summary
This fintech system is robust, secure, and modular, incorporating essential features like transaction handling with pessimistic locking, JWT-based authentication, Bcrypt encryption, detailed logging with Log4j2, role-based permissions, global exception handling, and comprehensive API documentation with Swagger. The use of Docker facilitates easy deployment and scaling of the application.
