# java-rest-wrapper
Java Wrapper for managing easy API logic. Basic support for Rest or parameter-based APIs.

## Main features
Some main features this java wrapper have :
* Library easy to include to your project
* Configuration of the API by subclassing or by configuring `web.xml`
* Code of all requests are separated : one request = one file
* Supports SSO logic
* Handle XML and JSON response types. Possibility to extend with your response types.

## Quick Guide

### First steps
1. Include the library jar into your project `library/dist/java-rest-wrapper.jar`.
2. Make sure to also include all librairies inside the `lib/` folder.

### Way 1 : Subclassing `APIHttpServlet`

The `APIHttpServlet` is the base Servlet to use with API logic. Add a new class that extends `APIHttpServlet`. Put your configuration preferences inside the constructor. Here is an example :

```
public class SampleAPIServletWithConstructor extends APIHttpServlet {

    public SampleAPIServletWithConstructor()
    {
        // We can specify various settings for the APIHttpServlet.
        
        // Sets the parameter identifiers that will be used for specific
        // mechanics of the API.
        setParameterAlias(APIHttpServlet.PARAMETER_SSO, "ssoKey");
        
        // Sets a custom SSO class.
        setSSO(new DefaultSSO());
        
        // Sets the dispatcher.
        // The dispatcher serves as the element that will be used to
        // find which class to instantiate and use for each request type.
        Dispatcher dispatcher = new DefaultDispatcher();
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

## Setup Configuration

The base class need some information to perform API requests as you want. That means using at least a dispatcher and optionnally setup response types and SSO logic you want.

### Dispatcher

A dispatcher serves as a request decision maker. Depending on request parameters found, the dispatcher will ask to the 
server to use different implementation files. For instance, a bootstrap Dispatcher named `DefaultDispatcher` serves all requests to the `BaseDefaults` request implementation class. This implementation class returns the current execution date of the system. You can extend `Dispatcher` abstract class to tell the API logic which class you want to run depending request parameters.

If you are interested by implementing a REST-based API, you can use `FileResourceDispatcher` that uses a file to detect what implementation class to run depending on the request path. See examples.

To set a custom dispatcher, you can :
1. Call `setDispatcher(Dispatcher dispatcher)` in a subclass of `APIHttpServlet` (override `onInit()` and put it inside).
2. Tell the name of the class in `web.xml`. In this case, the `Dispatcher` subclass will be instantiated with an empty constructor.

### SSO

This is optional. If you need your API be forbidden with a system of roles, you can extend the `SSO` abstract class to implement your own way to check. For instance, `DefaultSSO` returns true for every role asked, that means `DefaultSSO` will tell "yes" for all requests. But by overriding the `hasRole(String role)` method, you can modify this behavior to tell "no" when the role is not present for an API user key.

To set a custom SSO, you can :
1. Call `setSSO(SSO sso)` in a subclass of `APIHttpServlet` (override `onInit()` and put it inside).
2. Tell the name of the class in `web.xml`. In this case, the `SSO` subclass will be instantiated with an empty constructor.

### Request Implementation Class

These are the classes referenced inside your `Dispatcher` subclass. They are runned depending on parameters and the decision code you write inside the `Dispatcher` subclass. These classes must extend `RequestImplementation` and potentially implements one of many of the followings :
* IGetHandler (for HTTP GET)
* IPostHandler (for HTTP POST)
* IPutHandler (for HTTP PUT)
* IDeleteHandler (for HTTP DELETE)

Override the `onInit(HttpServletRequest request, HttpServletResponse resp)` to include all process to open DB or files you want, and `onFinish()` to include all process to close DB, files or dispose things you want.

By implementing `IGetHandler`, your request implementation class tells that it can handle HTTP GET requests. If you doesn't implement `IGetHandler` and somebody try to make a HTTP GET that triggers this request implementation class, an exception will be triggered.

### Throwing exceptions
Inside your request implementation classes, you can throw these exceptions if something goes wrong :
* BadRequestException : means parameters of this resource are missing or wrong.
* ForbiddenAccessException : the access to this resource is forbidden.
* InternalErrorException : the server encountered an internal error.
* NotImplementedException : the resource is not implemented yet.
* ResourceNotFoundException : the resource cannot be found. Useful when using regex for asking a specific username for instance.
* TooManyRequestsException : the resource cannot be accessed because the user have done too many requests.
* UnauthorizedAccessException : the resource is not authorized, but can be authorized by specifying some details.
* UnavailableServiceException : the resource is not available.

You can put additional message and root cause for each of these exceptions. They handle the status HTTP code and wrap the stack exception to allow external users to debug what is wrong. You can add a detailed description of the exception by using the `setMessage()` method.

By specifying the header `X-Show-Stacktrace` any user can see the entire stack exception. You can of course limit the use of this header to specific users.
