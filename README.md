## Family Budget Web-Application

### API for calculation and planning of expenses and family expenses

### Backend Technology:

* Java 11
* Spring Boot (Data, Security)
* Maven
* Hibernate
* Postgresql
* JWT
* MailSender
* Lombok
* Swagger

 ##  [Swagger service specification](family.json)</br>

### REST API endpoints:
`localhost:8080/`  
**free access for everyone**  
- **POST** -- /*registration* endpoint.
  The email, firstName, lastName, currency, password and confirmPassword are sent in json format. The body of 
the request is the RegistrationRequest class. Add new user with password to database. The body of the response 
is the UserDto class (it contains id, email, firstName, lastName and currency). If a user with the same email 
already exists or password has not correct format or password and confirmPassword don't match or currency has 
incorrect name, an appropriate exception will be thrown

- **GET** -- */activation/{activationCode}*
  * user activation via email link
  
- **POST** /*authentication* endpoint.
  The username and password are sent in json format. The body of the request is the AuthenticationRequest class.
If the name and password match the entry in the database, then a token is generated and sent in json format.
The body of the response is the AuthenticationResponse class. Else an appropriate exception will be thrown.

`localhost:8080/users`  
**access only by token after authentication**

- **GET** /*users/{id}*
  * This method get short information about the user by his id.
The userId is sent as pathVariable. The ***Authorization*** header is also added to the endpoint. 
It is equal to **Bearer {token}**. In case of successful verification of the token, the information 
about user will be uploaded from the database.

### How to run this project :

#### Firstly one need to install PostgreSQL and create "budget" database in it.
#### User for DB: postgres
#### Password for DB: 817b62

#### After that one need to build the project by Maven
```sh
##build the project
mvn clean install
```

After that one can run the project with the command:
```sh
##run the project
java -jar familybudget-0.0.1-SNAPSHOT.jar
```