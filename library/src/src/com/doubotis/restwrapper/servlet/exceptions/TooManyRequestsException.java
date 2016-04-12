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
public class TooManyRequestsException extends HTTPException
{
    public TooManyRequestsException() { super(); }
    public TooManyRequestsException(String message) { setDetails(message); }
    public TooManyRequestsException(Throwable rootCause)
    {
        super(rootCause);
    }
    public TooManyRequestsException(String message, Throwable rootCause)
    {
        super(rootCause);
        setDetails(message);
    }
    
    @Override
    public String getMessage()
    {
        String httpMessage = "Too many Requests";
        return httpMessage + ((getDetails() != null) ? " : " + getDetails() : "");
    }
    
    @Override
    public int getHTTPStatus()
    {
        return 429;
    }
}
