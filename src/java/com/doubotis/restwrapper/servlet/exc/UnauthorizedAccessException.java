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
public class UnauthorizedAccessException extends HTTPException
{
    @Override
    public String getMessage()
    {
        return "Unauthorized";
    }
    
    @Override
    public int getHTTPStatus()
    {
        return 401;
    }
}
