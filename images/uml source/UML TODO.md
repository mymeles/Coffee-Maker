Domain Objects to add: <br>
* User
fields
long id
string Name 
string password 
string role 

methods
user(name, password, role): User 
checkPass(string attemptedPass): bool 
setters and getters 

API Controllers to Add: <br>
* User 
* Login

Services to add: <br> 
* UserService 
methods 
createUser 
deleteUser 
editUser 
getUser

Respositories to add: <br> 
* UserRespository