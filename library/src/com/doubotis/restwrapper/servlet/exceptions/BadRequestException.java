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
public class BadRequestException extends HTTPException
{
    public BadRequestException() { super(); }
    public BadRequestException(String message) { setDetails(message); }
    public BadRequestException(Throwable rootCause)
    {
        super(rootCause);
    }
    public BadRequestException(String message, Throwable rootCause)
    {
        super(rootCause);
        setDetails(message);
    }
    
    @Override
    public String getMessage()
    {
        String httpMessage = "Bad Request";
        return httpMessage + ((getDetails() != null) ? " : " + getDetails() : "");
    }
    
    @Override
    public int getHTTPStatus()
    {
        return 400;
    }
}
