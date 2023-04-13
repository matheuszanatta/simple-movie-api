## Simple Movie API
This is a REST API that allows you to create, update, list, delete, and rate movies. The API is built using Spring Boot 3 and Java 20. It uses Spring Data JPA, Spring MVC, and H2 Database. The API is tested using JUnit, and managed with Maven.

## Requirements
To use this API, you will need:

- Java 20
- Maven
- A REST API client, such as Postman or cURL

## Getting Started
To get started with this API, follow these steps:

- Clone the repository
- Navigate to the project root directory in the terminal
- Run the command `mvn spring-boot:run`
- The API will now be accessible at `http://localhost:8080`

## Endpoints

This API exposes the following endpoints:

### GET /movies
Retrieves a list of all movies in the database.
### GET /movies/{id}
Retrieves the movie with the specified ID.
### POST /movies
Creates a new movie in the database.
### PUT /movies/{id}
Updates the movie with the specified ID.
### DELETE /movies/{id}
Deletes the movie with the specified ID.
### POST /movies/{id}/evaluate
Adds a rating to the movie with the specified ID.
### GET /movies/not-evaluated
Retrieves a movie that hasn't been rated yet.

## Running the application

To run the application, follow these steps:

1. Clone the repository to your local machine
2. Open the project in your preferred IDE
3. Run the `SimpleMovieApiApplication` class as a Java application
4. The API will be accessible at `http://localhost:8080`

## Running the tests

To run the unit tests for the application, follow these steps:

1. Open a terminal or command prompt and navigate to the root of the project
2. Run the command `mvn test`

## Conclusion

This is a simple movie API built with Spring Boot that allows you to create, update, delete, list and rate movies, as well as retrieve a movie that hasn't been rated yet. It uses Spring Data JPA and an H2 database to persist movie and rating data, and JUnit for unit testing.
