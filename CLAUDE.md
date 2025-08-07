# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an APIJSON-based student-parent management system that demonstrates zero-code CRUD operations using the APIJSON framework. The project showcases how to build REST APIs without writing traditional controller logic by leveraging APIJSON's JSON-based query language.

## Development Commands

### Database Setup
```bash
# Initialize database tables and configuration
mvn compile exec:java -Dexec.mainClass="apijson.boot.DatabaseTest"
mvn compile exec:java -Dexec.mainClass="apijson.boot.CreateAccessTables"
```

### Build and Run
```bash
# Start the application
mvn spring-boot:run

# Compile the project
mvn compile

# Package the application
mvn package
```

### Testing
- No specific test commands found in the project
- Manual testing can be done via the demo UI at: http://localhost:8080/student-parent-demo.html
- API testing via endpoints: `/get`, `/post`, `/put`, `/delete`, `/head`

## Architecture Overview

### Core Framework
- **APIJSON 7.1.0**: Main framework providing zero-code CRUD capabilities
- **Spring Boot 3.2.5**: Web framework and dependency injection
- **MySQL 8.4**: Primary database (configured for localhost:30004)
- **FastJSON**: JSON processing

### Key Components

#### Main Application Class
- `DemoApplication.java`: Spring Boot entry point with APIJSON configuration
- Configures CORS, custom parsers, verifiers, and SQL executors
- Initializes access control and request validation

#### API Layer  
- `DemoController.java`: REST controller extending APIJSONController
- Provides unified CRUD endpoints (`/get`, `/post`, `/put`, `/delete`, `/head`)
- Includes authentication endpoints (`/login`, `/logout`, `/register`)
- Supports both tag-based and direct API calls

#### Data Access Layer
- `DemoSQLConfig.java`: Database connection and SQL configuration
- `DemoParser.java`: Request parsing and response formatting
- `DemoVerifier.java`: Access control and permission verification
- `DemoSQLExecutor.java`: SQL execution handling

### Database Configuration
- **Host**: localhost:30004
- **Database**: apijson
- **Username**: root
- **Password**: Best@2008
- **Schema**: Tables include Student, Parent with foreign key relationships

### API Design Pattern
APIJSON uses a JSON-based query language where:
- Table names become JSON keys
- Conditions, sorting, pagination are expressed in JSON
- Complex joins and nested queries are supported
- No traditional SQL or controller code needed

Example request structure:
```json
{
  "Student[]": {
    "Student": {
      "grade": "九年级", 
      "@order": "age-"
    }
  }
}
```

### Security & Access Control
- Role-based permissions through `Access` table
- Request structure validation via `Request` table  
- Session-based authentication
- Support for verification codes and password management

### Key Files to Understand
- `src/main/java/apijson/boot/DemoApplication.java` - Main configuration
- `src/main/java/apijson/boot/DemoController.java` - API endpoints
- `src/main/java/apijson/boot/DemoSQLConfig.java` - Database setup
- `src/main/resources/application.properties` - Configuration properties
- `src/main/resources/static/student-parent-demo.html` - Demo UI

When modifying this codebase, understand that most CRUD logic is handled by the APIJSON framework rather than traditional Spring MVC patterns. Focus on configuration and permission setup rather than writing new controller methods.