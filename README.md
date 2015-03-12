# java-rest-wrapper
Java Wrapper for managing easy Rest-based APIs

## Main features
Some main features this java wrapper have :
* HTML Page to test requests, with JSON colorization
* Basic API Documentation support
* Code of all requests are separated by files
* Using reflexion to make coding easier

## How to use

### Specify new API
To specify new API, use the command.api file. The file is like a single .txt file structured as `<HTTP supported methods> <Servlet name> <regex url>`.

Example :
```
GET Base /
GET POST Users /users
GET POST DELETE User /users/([^/]+)
GET POST Me /me
GET POST MeTrails /me/trails
```

