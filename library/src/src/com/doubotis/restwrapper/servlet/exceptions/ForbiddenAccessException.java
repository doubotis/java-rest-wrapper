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
public class ForbiddenAccessException extends HTTPException
{
    public ForbiddenAccessException() { super(); }
    public ForbiddenAccessException(String message) { setDetails(message); }
    public ForbiddenAccessException(Throwable rootCause)
    {
        super(rootCause);
    }
    public ForbiddenAccessException(String message, Throwable rootCause)
    {
        super(rootCause);
        setDetails(message);
    }
    
    @Override
    public String getMessage()
    {
        String httpMessage = "Forbidden";
        return httpMessage + ((getDetails() != null) ? " : " + getDetails() : "");
    }
    
    @Override
    public int getHTTPStatus()
    {
        return 403;
    }
}
