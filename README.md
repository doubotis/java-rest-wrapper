# java-rest-wrapper
Java Wrapper for managing easy API mechanism. Basic support for Rest or parameter-based APIs.

## Main features
Some main features this java wrapper have :
* Library to include to your project, easy to use
* Configuration by subclassing or by configuring `web.xml`
* Code of all requests are separated by files
* Supports SSO mechanisms
* Handle many response types you want. Include XML and JSON response types by default. You use objects, the wrapper does the conversion.

## How to use

### First steps
1. Include the library jar into your project `library/dist/java-rest-wrapper.jar`.
2. Make sure to also include all librairies inside the `lib/` folder.

### Way 1 : Subclassing `APIHttpServlet`

The `APIHttpServlet` is the base Servlet to use with API mechanisms. Add a new class that extends `APIHttpServlet`. Put your configuration preferences inside the constructor. Here is an example :

```
public class SampleAPIServletWithConstructor extends APIHttpServlet {

    public SampleAPIServletWithConstructor()
    {
        // We can specify various settings for the APIHttpServlet.
        
        // Sets the parameter identifiers that will be used for specific
        // mechanics of the API.
        setParameterAlias(APIHttpServlet.PARAMETER_SSO, "ssoKey");
        
        // Sets a custom SSO class.
        setSSO(DefaultSSO.class);
        
        // Sets the dispatcher.
        // The dispatcher serves as the element that will be used to
        // find which class to instantiate and use for each request type.
        Dispatcher dispatcher = new FileResourceDispatcher(Constants.RESOURCE_PATH);
        setDispatcher(dispatcher);
        
        // Sets the return schemes the system can handle.
        // By default, JSON and XML responses are managed, but
        // you may need to add some other kind of responses, like
        // default HTML or specific binary data.
        // The first element of the array is used as the default behavior.
        setReturnTypes(new DataResponse[] {
            new JSONResponse(),
            new XMLResponse()
        });
    }
    
}
```

You can get additional information about what are ReturnTypes and Dispatchers further.
You can get a fully working example on the sample project.

### Way 2 : Add configuration information into web.xml

You can also use directly the `APIHttpServlet` class if you specify some parameters into your `web.wml` file :

```
<servlet>
        <servlet-name>SampleAPIServletWithConfig</servlet-name>
        <servlet-class>com.doubotis.restwrapper.APIHttpServlet</servlet-class>
        <init-params>
            <aliases>
                <alias key="sso" value="ssoKey" />
            </aliases
            <ssoClass>com.sample.YourSSOImplementation</ssoClass>
            <dispatcherClass>com.sample.YourDispatcher</dispatcherClass>
        </init-params>
    </servlet>
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
