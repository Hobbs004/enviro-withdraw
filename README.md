# Enviro365 Investments — Withdrawal Notice System

## Quick Start

### Prerequisites
- Java 25
- Maven 3.8+
- Node.js 18+ & Angular CLI (`npm install -g @angular/cli`)

### Backend
```bash
cd backend
mvn spring-boot:run
```
API runs on **http://localhost:8080**
H2 Console: **http://localhost:8080/h2-console** (JDBC URL: `jdbc:h2:mem:enviro365db`)

### Frontend
```bash
cd enviro365-frontend
npm install
ng serve
```
UI runs on **http://localhost:4200**

---

## Project Structure

### Backend
backend/src/main/java/com/enviro/assessment/junior/nhletelo/
├── model/           # JPA entities (Investor, InvestmentProduct, WithdrawalNotice)
├── repository/      # Spring Data JPA repositories
├── dto/             # Request/Response DTOs with Bean Validation
├── service/         # Business logic (InvestorService, WithdrawalService, CsvExportService)
├── controller/      # REST controllers
├── exception/       # Custom exceptions + GlobalExceptionHandler
└── config/          # CORS configuration

### Frontend

enviro365-frontend/src/app/
├── components/
│   ├── navbar/                # Navigation bar
│   ├── portfolio-dashboard/   # Investor portfolio view
│   ├── add-investor/          # Create investor form
│   ├── add-product/           # Create investment product form
│   ├── withdrawal-form/       # Submit withdrawal form
│   └── withdrawal-history/    # Withdrawal history + CSV export
├── services/
│   ├── investor.service.ts    # Investor API calls
│   ├── withdrawal.service.ts  # Withdrawal API calls
│   └── investment-product.ts  # Product API calls
└── models/
├── investor.model.ts      # Investor and InvestmentProduct interfaces
├── withdrawal.model.ts    # WithdrawalRequest and WithdrawalNotice interfaces
└── api-response.model.ts  # Generic API response wrapper

---

## API Documentation

### Investors
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/investors` | List all investors |
| GET | `/api/investors/{id}` | Get investor with portfolio |
| GET | `/api/investors/{id}/products` | Get investor products |
| POST | `/api/investors` | Create investor |
| PUT | `/api/investors/{id}` | Update investor |
| DELETE | `/api/investors/{id}` | Delete investor |

### Investment Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | List all products |
| GET | `/api/products/{id}` | Get product by ID |
| POST | `/api/products` | Create product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |

### Withdrawals
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/withdrawals` | Submit a withdrawal notice |
| GET | `/api/withdrawals/investor/{id}` | Get withdrawal history |
| GET | `/api/withdrawals/investor/{id}?from=&to=` | Filter by date range |
| GET | `/api/withdrawals/investor/{id}/export` | Download CSV statement |

---

## Sample Requests

### Create Investor
```json
POST /api/investors
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "dateOfBirth": "1955-03-15"
}
```

### Create Product
```json
POST /api/products
{
  "productName": "Retirement Annuity",
  "productType": "RETIREMENT",
  "balance": 500000.00,
  "investorId": 1
}
```

### Submit Withdrawal
```json
POST /api/withdrawals
{
  "productId": 1,
  "withdrawalAmount": 50000.00
}
```

### Withdrawal Response
```json
{
  "success": true,
  "message": "Withdrawal approved successfully",
  "data": {
    "id": 1,
    "productId": 1,
    "productName": "Retirement Annuity",
    "investorName": "John Doe",
    "withdrawalAmount": 50000.00,
    "balanceBefore": 500000.00,
    "balanceAfter": 450000.00,
    "createdAt": "2026-06-11T10:30:00",
    "status": "APPROVED"
  }
}
```

---

## Business Rules

| Rule | Description |
|------|-------------|
| Retirement Age | RETIREMENT products only withdrawable if investor age is older than 65 |
| Balance Limit | Withdrawal amount cannot exceed current product balance |
| 90% Cap | Withdrawal amount cannot exceed 90% of current balance |

---

## Advanced Features Implemented

- [x] Global Exception Handler — `GlobalExceptionHandler.java` catches all exceptions centrally and returns consistent error responses
- [x] DTO Layer — All API communication uses DTOs, raw entities are never exposed
- [x] Input Validation — `@Valid`, `@NotNull`, `@DecimalMin`, `@Digits` annotations on all request DTOs
- [x] Unit Tests — `WithdrawalServiceTest` covering all business rule violations and happy paths
- [x] UI Validation — Angular reactive forms with real-time field-level error feedback

---

## AI Usage Disclosure

This project was developed with AI assistance (Claude by Anthropic) for:
- Initial project scaffolding and boilerplate structure
- Unit test generation
- README documentation

All generated code was reviewed, understood, and adapted to meet the business requirements. The business logic including rule enforcement, balance calculations, age validation, and CSV export was designed and verified independently.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.2, Spring Data JPA |
| Database | H2 In-Memory Database |
| Frontend | Angular 21, TypeScript, Reactive Forms |
| Build Tools | Maven, Angular CLI |
| API Style | REST with consistent JSON response wrapper |

---

## Notes

- The H2 database resets on every backend restart. Data does not persist between restarts.
- No authentication is implemented as it was not required by the assessment.
- The frontend communicates with the backend via CORS configured for `http://localhost:4200`.
