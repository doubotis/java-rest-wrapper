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
public class UnavailableServiceException extends HTTPException
{
    public UnavailableServiceException() { super(); }
    public UnavailableServiceException(String message) { setDetails(message); }
    public UnavailableServiceException(Throwable rootCause)
    {
        super(rootCause);
    }
    public UnavailableServiceException(String message, Throwable rootCause)
    {
        super(rootCause);
        setDetails(message);
    }
    
    @Override
    public String getMessage()
    {
        String httpMessage = "Service Unavailable";
        return httpMessage + ((getDetails() != null) ? " : " + getDetails() : "");
    }
    
    @Override
    public int getHTTPStatus()
    {
        return 503;
    }
}
