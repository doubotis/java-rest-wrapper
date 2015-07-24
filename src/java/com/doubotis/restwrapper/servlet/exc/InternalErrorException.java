/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.doubotis.restwrapper.servlet.exc;

/**
 *
 * @author Christophe
 */
public class InternalErrorException extends HTTPException
{
    public InternalErrorException() { super(); }
    public InternalErrorException(Throwable rootCause) { super(rootCause); }
    
    @Override
    public String getMessage()
    {
        return "Internal Error";
    }
    
    @Override
    public int getHTTPStatus()
    {
        return 500;
    }
}
