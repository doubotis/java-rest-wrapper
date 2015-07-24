# java-rest-wrapper
Java Wrapper for managing easy Rest-based APIs

## Main features
Some main features this java wrapper have :
* HTML Page to test requests, with JSON colorization
* Basic API Documentation support
* Code of all requests are separated by files
* Using reflexion to make coding easier
* Bootstrap project that allows you to customize exactly how you want.

## How to use

### Specify new Resource
To specify new resource, use the _resource.txt file. The file is like a single .txt file structured as `<HTTP supported methods> <Servlet name> <regex url>`. Each line is a resource that can be accessed by several HTTP methods. The resource is specified with a regex.

Example :
```
GET Base /
GET POST Users /users
GET POST DELETE User /users/([^/]+)
GET POST Me /me
GET POST MeTrails /me/trails
```

You can specify a specific URL path to access this file by modifying the `Constants` class.

### Servlet Example
For above example, let's see the `GET Base /` line.
Create a `Base` class extending `NativeImpl` and implementing `IGetHandler` because this is the only supported method.
Next, override the get method, like that :
```java
@Override
public void get(HttpServletRequest request, HttpServletResponse resp) throws Exception
{
    PrintWriter pw = resp.getWriter();
    resp.setStatus(200);
    
    JSONObject obj = new JSONObject();
    obj.put("version", 10000);
    obj.put("compilation_date", new Date().getTime());
    
    return obj;
}
```

### Subclassing NativeImpl class
`NativeImpl` class is the base class for all implementation classes. You can extends this to add custom code to automatically login to your database when the implementation class is created, and close the connection when the implementation class is destroyed.

```
public class ConnectedImpl extends NativeImpl
{
    @Override
    public void onCreate(HttpServletRequest request, HttpServletResponse resp) {
        // Do something here when the implementation class is created (open DBs ?).
    }

    @Override
    public void onFinish() {
        // Do something here when the implementation class is destroyed (close DBs ?).
    }
}
```

### Managing SSO
A basic SSO system is created on this demo with an exemple named `DefaultSSO`. DefaultSSO extends an abstract class `SSO`, allowing you to test if some user has sufficient privileges to follow a request.

You can set another SSO class by modifying this line in `ServletAPI` class :
```
request.setAttribute(NativeImpl.ATTRIBUTE_SSO, new DefaultSSO(ssoKey));
```

You can next control the way the SSO must be checked by controlling them inside implementation classes :

```
SSO sso = (SSO)request.getAttribute(Base.ATTRIBUTE_SSO);
if (!sso.hasRole("test"))
    throw new ForbiddenAccessException();
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
