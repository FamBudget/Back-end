## Family Budget Web-Application
This is a RESTful API designed for calculating and planning family expenses. It provides endpoints for user authentication, category management, and user information retrieval. This application is built using Java 11, Spring Boot, Maven, Hibernate, Postgresql, JWT, MailSender, Lombok, and Swagger.

##  [Swagger service specification](family.json)</br>

#### Endpoints
**free access for everyone**

This controller provides endpoints for user authentication and registration.

- **POST -- /registration**: Registers a new user with the provided email, first name, last name, currency, password, and confirmation password. The body of the request is the RegistrationRequest class. The response body contains the UserDto class, which includes the user's ID, email, first name, last name, and currency. If the email already exists, or the passwords don't match or don't meet the format criteria, or the currency name is invalid, an appropriate exception will be thrown.
- **GET -- /activation/{activationCode}**: Activates the user account via email link.
- **POST -- /authentication**: Authenticates the user by matching the provided username and password with the user's information in the database. If the authentication is successful, a token is generated and sent in json format. The response body is the AuthenticationResponse class. Otherwise, an appropriate exception will be thrown.
UserController
localhost:8080/users

This controller provides an endpoint for retrieving user information.

- **GET -- /users/{id}**: Retrieves short information about the user by their ID. The user ID is sent as a path variable. The Authorization header is also added to the endpoint. It is equal to Bearer {token}. In case of successful verification of the token, the information about the user will be retrieved from the database.
CategoryController:
localhost:8080/categories

This controller provides endpoints for managing income and expense categories for a user.

- **GET -- /categories/income**: Retrieves a list of income categories for a user, based on the user's email, starting index, and maximum number of results.
- **GET -- /categories/expense**: Retrieves a list of expense categories for a user, based on the user's email, starting index, and maximum number of results.
- **POST -- /categories/income**: Creates a new income category for a user, based on the user's email and a new category DTO provided in the request body.
- **POST -- /categories/expense**: Creates a new expense category for a user, based on the user's email and a new category DTO provided in the request body.
- **PUT -- /categories/income**: Updates an income category for a user, based on the user's email and an update category DTO provided in the request body.
- **PUT -- /categories/expense**: Updates an expense category for a user, based on the user's email and an update category DTO provided in the request body.
- **DELETE -- /categories/income/{categoryId}**: Delete an existing income category by id.
- **DELETE -- /categories/expense/{categoryId}**: Delete an existing expense category by id.

This controller provides endpoints for managing income and expense categories for a user.

- **GET -- /accounts**: Retrieves a list of accounts for a user, based on the user's email, starting index, and maximum number of results.
- **POST -- /accounts**: Creates a new account for a user, based on the user's email and a new category DTO provided in the request body.
- **PUT -- /accounts**: Updates an account for a user, based on the user's email and an update accounts DTO provided in the request body.
- **DELETE -- /accounts/{accountId}**: Delete an existing account by id.

Backend Technology
The application is built using Java 11 and Spring Boot, along with several other technologies:

- **Java 11**
- **Maven**
- **Spring Boot (Data, Security)**
- **Hibernate**
- **Postgresql**
- **JWT**
- **MailSender**
- **Lombok**
- **Swagger**

### How to run this project :

#### Firstly one need to install PostgreSQL and create "budget" database in it.
#### User for DB: fambudget
#### Password for DB: 817b62

#### After that one need to build the project by Maven
```sh
##build the project
mvn clean package
```

After that one can run the project with the command:
```sh
##run the project
java -jar familybudget-0.0.1-SNAPSHOT.jar
```