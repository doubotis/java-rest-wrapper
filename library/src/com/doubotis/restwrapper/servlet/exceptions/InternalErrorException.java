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
public class InternalErrorException extends HTTPException
{
    public InternalErrorException() { super(); }
    public InternalErrorException(String message) { setDetails(message); }
    public InternalErrorException(Throwable rootCause)
    {
        super(rootCause);
    }
    public InternalErrorException(String message, Throwable rootCause)
    {
        super(rootCause);
        setDetails(message);
    }
    
    @Override
    public String getMessage()
    {
        String httpMessage = "Internal Error";
        return httpMessage + ((getDetails() != null) ? " : " + getDetails() : "");
    }
    
    @Override
    public int getHTTPStatus()
    {
        return 500;
    }
}
