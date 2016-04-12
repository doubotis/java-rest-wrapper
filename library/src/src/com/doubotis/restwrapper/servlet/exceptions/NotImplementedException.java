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
public class NotImplementedException extends HTTPException
{
    public NotImplementedException() { super(); }
    public NotImplementedException(String message) { setDetails(message); }
    public NotImplementedException(Throwable rootCause)
    {
        super(rootCause);
    }
    public NotImplementedException(String message, Throwable rootCause)
    {
        super(rootCause);
        setDetails(message);
    }
    
    @Override
    public String getMessage()
    {
        String httpMessage = "Not Implemented";
        return httpMessage + ((getDetails() != null) ? " : " + getDetails() : "");
    }
    
    @Override
    public int getHTTPStatus()
    {
        return 501;
    }
}
