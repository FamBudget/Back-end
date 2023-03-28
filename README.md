## Family Budget Web-Application

### API for calculation and planning of expenses and family expenses

### Backend Technology:
* Java 11
* Spring Boot (Data, Security)
* JWT
* MailSender
* Postgresql

### REST API endpoints:
`localhost:8080/api/v1/auth`  
**free access for everyone**  
* POST -- `/registration`
  * user registration with email confirmation
* GET -- `/activation/:code`
  * user activation via email link
* POST -- `/login`
  * user authorization by email and password + (token)
* POST -- `/logout`
  * exiting the user from the application

`localhost:8080/api/v1/user`  
**access only by token after login**
* GET -- `/user`
  * short output of data about the user by his id

`localhost:8080/api/v1/admin`  
**access only for moderator and admin by token**  
* GET -- `/users`
  * getting a list of all users with a full description
* POST -- `/users`
  * adding a new user
* GET -- `/users/:id`
  * getting full information about the user by id
* PUT -- `/users/:id`
  * user status change
* POST -- `/users/:id`
  * changing any user data
* DELETE -- `/users/:id`
  * deleting a user

#### _to be continued..._