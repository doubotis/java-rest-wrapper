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
public abstract class HTTPException extends Exception
{
    public HTTPException() {};
    public HTTPException(Throwable t) { super(t); };
    
    public abstract int getHTTPStatus();
}
