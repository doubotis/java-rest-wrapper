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
public class UnauthorizedAccessException extends HTTPException
{
    public UnauthorizedAccessException() { super(); }
    public UnauthorizedAccessException(String message) { setDetails(message); }
    public UnauthorizedAccessException(Throwable rootCause)
    {
        super(rootCause);
    }
    public UnauthorizedAccessException(String message, Throwable rootCause)
    {
        super(rootCause);
        setDetails(message);
    }
    
    @Override
    public String getMessage()
    {
        String httpMessage = "Unauthorized";
        return httpMessage + ((getDetails() != null) ? " : " + getDetails() : "");
    }
    
    @Override
    public int getHTTPStatus()
    {
        return 401;
    }
}
