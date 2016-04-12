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
public class TeapotException extends HTTPException
{
    public TeapotException() { super(); }
    public TeapotException(String message) { setDetails(message); }
    public TeapotException(Throwable rootCause)
    {
        super(rootCause);
    }
    public TeapotException(String message, Throwable rootCause)
    {
        super(rootCause);
        setDetails(message);
    }
    
    @Override
    public String getMessage()
    {
        String httpMessage = "I'm a teapot !";
        return httpMessage + ((getDetails() != null) ? " : " + getDetails() : "");
    }
    
    @Override
    public int getHTTPStatus()
    {
        return 418;
    }
}
