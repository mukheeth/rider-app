# Final Technology Stack

## Backend

| Component | Technology | Version | Justification |
|-----------|-----------|---------|---------------|
| Language | Java | 21 (LTS) | Long-term support, modern features (records, pattern matching, virtual threads) |
| Framework | Spring Boot | 3.3.0 | Production-ready, comprehensive ecosystem, excellent documentation |
| Build Tool | Maven | 3.9.6 | Standard Java build tool, widely adopted, stable |
| API Documentation | SpringDoc OpenAPI | 2.6.0 | OpenAPI 3.0 support, auto-generates API docs from annotations |
| Web Server | Embedded Tomcat | 10.1.24 | Included with Spring Boot, production-proven |
| WebSocket | Spring WebSocket | 6.1.8 | Native Spring integration, STOMP support |
| Database Driver | PostgreSQL JDBC | 42.7.1 | Official PostgreSQL driver, actively maintained |
| Redis Client | Lettuce | 6.3.1.RELEASE | Async Redis client, thread-safe, recommended for Spring |
| JWT Library | JJWT | 0.12.5 | Java JWT implementation, supports JWT/JWS/JWE |
| Password Hashing | BCrypt | Spring Security BCrypt | Industry standard, built into Spring Security |
| Validation | Jakarta Validation | 3.0.2 | Bean validation, integrated with Spring |
| Logging | Logback | 1.5.5 | Default Spring Boot logger, structured logging support |
| JSON Processing | Jackson | 2.16.1 | Default Spring Boot JSON library, high performance |

---

## Mobile

| Component | Technology | Version | Justification |
|-----------|-----------|---------|---------------|
| Framework | Flutter | 3.24.0 | Cross-platform, single codebase for iOS/Android, excellent performance |
| Language | Dart | 3.5.0 | Modern language, strong typing, async/await support |
| State Management | Provider | 6.1.1 | Simple, recommended by Flutter team, good performance |
| HTTP Client | Dio | 5.4.3 | Powerful HTTP client, interceptors, request cancellation |
| WebSocket | WebSocket Channel | Built-in | Flutter's native WebSocket support, no external dependency |
| Local Storage | SharedPreferences | 2.2.2 | Key-value storage for simple data (tokens, preferences) |
| JWT Handling | JWT Decode | 0.3.1 | Decode JWT tokens without verification (client-side) |
| Location Services | Geolocator | 11.1.0 | Accurate location services, supports both iOS and Android |
| Maps | Google Maps Flutter | 2.6.1 | Free tier available, comprehensive mapping features |
| Push Notifications | Firebase Cloud Messaging | 14.7.10 | Free tier available, reliable push notification delivery |

---

## Admin UI

| Component | Technology | Version | Justification |
|-----------|-----------|---------|---------------|
| Framework | React | 18.3.1 | Industry standard, large ecosystem, component-based |
| Language | TypeScript | 5.5.3 | Type safety, better IDE support, reduces bugs |
| Build Tool | Vite | 5.4.2 | Fast build tool, excellent developer experience |
| UI Library | Material-UI (MUI) | 6.0.1 | Comprehensive component library, production-ready |
| State Management | Zustand | 4.5.2 | Lightweight, simple API, no boilerplate |
| HTTP Client | Axios | 1.7.4 | Popular, interceptors, request/response transformation |
| WebSocket | Socket.io-client | 4.7.4 | Reliable WebSocket client, automatic reconnection |
| Routing | React Router | 6.24.1 | Standard routing library for React |
| Form Handling | React Hook Form | 7.52.0 | Performant forms, minimal re-renders |
| Charts | Recharts | 2.12.7 | Flexible charting library, built for React |
| Date Handling | date-fns | 3.6.0 | Lightweight date utility library |

---

## Infrastructure

| Component | Technology | Version | Justification |
|-----------|-----------|---------|---------------|
| Database | PostgreSQL | 16.3 | Open-source, ACID compliant, excellent performance, free |
| Cache/Messaging | Redis | 7.2.5 | In-memory data store, pub/sub, free and open-source |
| Containerization | Docker | 24.0.7 | Industry standard, consistent deployments |
| Orchestration | Kubernetes | 1.31.0 | Container orchestration, auto-scaling, self-healing |
| Reverse Proxy | Nginx | 1.25.5 | High-performance web server, load balancing, SSL termination |
| CI/CD | GitHub Actions | Latest | Free for public repos, integrated with GitHub |
| Monitoring | Prometheus | 2.51.0 | Open-source metrics collection, time-series database |
| Visualization | Grafana | 11.0.0 | Open-source analytics, integrates with Prometheus |
| Log Aggregation | Loki | 2.9.5 | Log aggregation system, integrates with Grafana, lightweight |
| Service Discovery | Kubernetes DNS | Built-in | Native Kubernetes service discovery |
| SSL/TLS | Let's Encrypt | Latest | Free SSL certificates, automated renewal |
| Version Control | Git | 2.45.0 | Distributed version control, industry standard |

---

## Testing Tools

| Component | Technology | Version | Justification |
|-----------|-----------|---------|---------------|
| Unit Testing (Backend) | JUnit | 5.10.2 | Standard Java testing framework |
| Mocking (Backend) | Mockito | 5.10.0 | Popular mocking framework for Java |
| Integration Testing (Backend) | Spring Boot Test | 3.3.0 | Integrated testing support, embedded database support |
| API Testing (Backend) | MockMvc | 6.1.8 | Spring's testing framework for web layer |
| Unit Testing (Mobile) | Flutter Test | 3.24.0 | Built-in Flutter testing framework |
| Widget Testing (Mobile) | Flutter Test | 3.24.0 | Integrated widget testing in Flutter |
| Integration Testing (Mobile) | Integration Test | 3.24.0 | Flutter's integration testing package |
| Unit Testing (Admin UI) | Vitest | 2.0.5 | Fast unit test framework, Vite-native |
| Component Testing (Admin UI) | React Testing Library | 16.0.1 | Simple and complete testing utilities for React |
| Mocking (Admin UI) | MSW | 2.3.1 | API mocking library for browser and Node.js |
| E2E Testing | Playwright | 1.43.0 | Cross-browser testing, reliable and fast |
| Load Testing | k6 | 0.48.0 | Open-source load testing tool, JavaScript-based |
| Code Coverage (Backend) | JaCoCo | 0.8.12 | Java code coverage tool, integrates with Maven |
| Code Coverage (Admin UI) | Vitest Coverage | 2.0.5 | Coverage plugin for Vitest |

---

## Development Tools

| Component | Technology | Version | Justification |
|-----------|-----------|---------|---------------|
| IDE (Backend) | IntelliJ IDEA Community | 2024.1 | Free, excellent Java support, Spring Boot plugins |
| IDE (Mobile) | Android Studio | 2024.1.1 | Free, official Flutter/Android IDE |
| IDE (Admin UI) | VS Code | 1.89.0 | Free, excellent TypeScript/React support, extensions |
| API Client | Postman | Latest | Free tier available, API testing and documentation |
| Database Client | DBeaver | 24.1.0 | Free, universal database tool, supports PostgreSQL |
| Container Management | Docker Desktop | 4.29.0 | Free, easy container management |
| Git GUI | GitKraken | Latest | Free tier available, visual git client |
| Code Quality (Java) | SpotBugs | 4.8.6 | Free static analysis tool for Java |
| Code Formatting (Java) | Google Java Format | 1.23.0 | Standard Java code formatter |
| Code Formatting (Flutter) | Dart Format | 3.5.0 | Built-in Dart formatter |
| Code Formatting (TypeScript) | Prettier | 3.3.3 | Opinionated code formatter, widely adopted |
| Linting (TypeScript) | ESLint | 9.2.0 | Pluggable JavaScript/TypeScript linter |

