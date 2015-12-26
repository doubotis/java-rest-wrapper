# java-rest-wrapper
Java Wrapper for managing easy Rest-based APIs

## Main features
Some main features this java wrapper have :
* HTML Page to test requests, with JSON colorization
* Basic API Documentation support
* Code of all requests are separated by files
* Using reflexion to make coding easier


## How to use

### Specify new Resource
To specify new resource, use the command.api file. The file is like a single .txt file structured as `<HTTP supported methods> <Servlet name> <regex url>`. Each line is a resource that can be accessed by several HTTP methods. The resource is specified with a regex.

Example :
```
GET Base /
GET POST Users /users
GET POST DELETE User /users/([^/]+)
GET POST Me /me
GET POST MeTrails /me/trails
```

### Servlet Example
For above example, let's see the `GET Base /` line.
Create a `Base` class implementing `IGetHandler` because this is the only supported method.
Next, override the get method, like that :
```java
@Override
public void get(HttpServletRequest request, HttpServletResponse resp, JSONWriter jw) throws Exception
{
    PrintWriter pw = resp.getWriter();
    resp.setStatus(200);
    
    JSONObject obj = new JSONObject();
    obj.put("version", 10000);
    obj.put("compilation_date", new Date().getTime());
    jw.putDataObject(obj);
    
}
```

### Throwing exceptions
If something goes wrong into the process, you can throw many exceptions :
* BadRequestException : means parameters of this resource are missing or wrong.
* ForbiddenAccessException : the access to this resource is forbidden.
* InternalErrorException : the server encountered an internal error.
* HTTPException : the base exception class.
* NotImplementedException : the resource is not implemented yet.
* ResourceNotFoundException : the resource cannot be found. Useful when using regex for asking a specific username for instance.
* TooManyRequestsException : the resource cannot be accessed because the user have done too many requests.
* UnauthorizedAccessException : the resource is not authorized, but can be authorized by specifying some details.
* UnavailableServiceException : the resource is not available.

Each of these exceptions handle the status HTTP code and wrap the stack exception to allow external users to debug what is wrong. You can add a detailed description of the exception by using the `setMessage()` method.

By specifying the header `X-Show-Stacktrace` any user can see the entire stack exception. You can of course limit the use of this header to specific users.

### Write documentation

The `doc` package can be used to write some documentation wiki. The documentation page is based on WikiCreole markup with a Bootstrap template. Add .txt files into the `doc` package. For the above example :

```
Base.txt
Base_GET.txt
Base_POST.txt
```
If you do not write any documentation the resource will have a clean wiki page.

## Utils
This wrapper comes with some class utilities, like a SQL Builder, allowing to make requests like that :
```java
String req = new SQLServerBuilder().select("USERS.login", "USERS.gold", "USERS.email", 
                                        "USERS.name", "USERS.firstname", "USERS.address", 
                                        "Users.portal_code", "USERS.city", "Pays.Pays AS country",
                                        "USERS.departement", "USERS.LangMat", "Users.Etoiles", 
                                        "USERS.alias_auteur", "USERS.birth_date", "COALESCE (UserPoints.points, 0) AS pts")
                                    .from("USERS")
                                    .leftOuterJoin("UserPoints", "Users.login = UserPoints.user_id")
                                    .innerJoin("Pays", "USERS.country = Pays.ID")
                                    .where("USERS.login LIKE ?")
                                    .toString();
stmt = conn.prepareStatement(req);
stmt.setString(1, username);
rs = stmt.executeQuery();
// Rest of code
```
