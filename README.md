# Entrance Visitor Display

## About
Entrance Visitor Display is a web application that allows receptionists and administrators to manage visitors, companies, and appointments through a web-based administration interface. The front page displays today's appointments and welcomes the visitors.

The application features a modern, responsive interface with clear visual hierarchy, real-time updates, and easy navigation. Important appointments can be highlighted, and all data management tasks can be performed via CRUD operations.

## Used technologies
- **Java 21**
	Programming language used for the entire backend logic.
- **Spring Boot 3**
	Simplifies creation of Spring-based standalone applications.
- **Spring Framework**
	Framework for developing enterprise-grade Java applications following clean design patterns.
- **Spring Data JPA**
	Simplifies database access by eliminating boilerplate DAO code, used together with H2 database.
- **Vaadin 24**
	Java framework for building web-based UIs with server-side rendering.
- **H2 Database**
  Lightweight, file-based relational database for local development and testing.
- **Maven**
  Build and dependency management tool.


## Project structure overview
- ***src/main/resources***:
	- */db/initialdata*:
		- SQL scripts to populate the database with initial entries for testing.

	- */frontend/static*:
		- CSS: Stylesheets for each view
		- Images & Icons
		- Fonts & JavaScript libraries


- ***/frontend/templates***:
    - HTML / Vaadin templates and fragments for UI components

- ***/frontend/templates***:
    Central configuration for database connection, server port, logging, and framework settings.


- ***src/main/java/com/sh/evd***:
  - **controller/**:
  Classes that handle application logic. Accept requests from the frontend, retrieve data via services, and return views.

	  - VisitorController 
    - CompanyController 
    - VisitScheduleController


  - **service/**:
  Handle business logic, process data models, perform validation, and return results to controllers.

    - VisitorService 
    - CompanyService 
    - VisitScheduleService


  - **repository/**:
  Spring Data JPA repositories for CRUD operations on database entities. Automatic implementations provided by Spring.

    - VisitorRepository
    - CompanyRepository
    - VisitScheduleRepository


  - **entity/**:  
  Hibernate entities mapping database tables to Java objects.

    - Visitor
    - Company
    - VisitSchedule


  - **dto/**:
  Data Transfer Objects (DTOs) for transferring only necessary data between views and services.

      - VisitScheduleDto


  - **views/**:
  UI components (Vaadin Views):

    - MainView: Central management of visits, filtering, sorting, marking important appointments
    - CompanyView: Company management
    - BlackboardView: Fullscreen visitor display for reception
    - LoginView: Authentication and role-based access control


## Usage hints:
- **Site access for development:**

	- Main View (Besucherübersicht): [localhost:8080](localhost:8080)
  - Company overview (Firmenübersicht): [localhost:8080/company](localhost:8080/company)
  - Entrance-Visitor-Display (Heutige Besucher): [localhost:8080/visitors-today](http://localhost:8080/visitors-today)

- **Database connection string:**
	jdbc:h2:file:./data/evd-db
- **Set database user password:**
	ALTER USER sa PASSWORD '  ';



© ZGS Bildungs-GmbH - All rights reserved.
You may not use, distribute or modify this code without explicit permission by ZGS Bildungs-GmbH.