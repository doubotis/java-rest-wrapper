/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.doubotis.restwrapper.servlet.impl;

import com.doubotis.restwrapper.data.JObject;
import com.doubotis.restwrapper.servlet.IGetHandler;
import com.doubotis.restwrapper.servlet.SSO;
import com.doubotis.restwrapper.servlet.exc.ForbiddenAccessException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Christophe
 */
public class Base extends NativeImpl implements IGetHandler
{
    @Override
    public void onCreate(HttpServletRequest request, HttpServletResponse resp) {
        // Do something here when the implementation class is created (open DBs ?).
    }

    @Override
    public void onFinish() {
        // Do something here when the implementation class is destroyed (close DBs ?).
    }
    
    @Override
    public JObject get(HttpServletRequest request, HttpServletResponse resp) throws Exception
    {
        // If you must check if a SSO is valid, you can do this via attributes.
        SSO sso = (SSO)request.getAttribute(Base.ATTRIBUTE_SSO);
        if (!sso.hasRole("test"))
            throw new ForbiddenAccessException();
        
        // Then print what you want.
        PrintWriter pw = resp.getWriter();
        resp.setStatus(200);
        
        JObject object = new JObject();
        object.put("version", "1.0");
        object.put("compilation_date", new Date().getTime());
        return object;
    }

    
}
