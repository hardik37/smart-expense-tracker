# Smart Expense Tracker

A production-ready REST API for tracking personal expenses built with Spring Boot 3, MySQL, and JWT authentication.

## Features

- JWT-based authentication
- CRUD operations for expenses
- Category management
- Expense summary by date range
- Docker support
- GitHub Actions CI/CD

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Security
- MySQL 8
- Maven
- Docker

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker (optional)

### Local Development

1. Clone the repository
2. Update `application.properties` with your MySQL credentials
3. Run: `mvn spring-boot:run`

### Docker

```bash
docker-compose up --build
```

### API Endpoints

- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login
- `GET /api/expenses` - Get all expenses
- `POST /api/expenses` - Create expense
- `PUT /api/expenses/{id}` - Update expense
- `DELETE /api/expenses/{id}` - Delete expense
- `GET /api/expenses/summary?startDate=&endDate=` - Get expense summary

## License

MIT