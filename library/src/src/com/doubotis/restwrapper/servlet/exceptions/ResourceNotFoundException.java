/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.doubotis.restwrapper.servlet.exceptions;

/**
 *
 * @author Christophe
 */
public class ResourceNotFoundException extends HTTPException
{
    public ResourceNotFoundException() { super(); }
    public ResourceNotFoundException(String message) { setDetails(message); }
    public ResourceNotFoundException(Throwable rootCause)
    {
        super(rootCause);
    }
    public ResourceNotFoundException(String message, Throwable rootCause)
    {
        super(rootCause);
        setDetails(message);
    }
    
    @Override
    public String getMessage()
    {
        String httpMessage = "Resource not found";
        return httpMessage + ((getDetails() != null) ? " : " + getDetails() : "");
    }
    
    @Override
    public int getHTTPStatus()
    {
        return 404;
    }
}
