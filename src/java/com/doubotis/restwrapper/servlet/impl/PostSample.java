/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.doubotis.restwrapper.servlet.impl;

import com.doubotis.restwrapper.data.JArray;
import com.doubotis.restwrapper.data.JObject;
import com.doubotis.restwrapper.servlet.IGetHandler;
import com.doubotis.restwrapper.servlet.IPostHandler;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Christophe
 */
public class PostSample extends NativeImpl implements IGetHandler, IPostHandler
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
        PrintWriter pw = resp.getWriter();
        resp.setStatus(200);
        
        JObject object = new JObject();
        object.put("aString", "HelloWorld");
        object.put("anInt", 5);
        object.put("aDouble", 4.6786);
        
        JArray array = new JArray();
        JObject subObject = new JObject();
        subObject.put("element", "test1");
        array.add(subObject);
        
        object.put("anArray", array);
        return object;
    }

    @Override
    public JObject post(HttpServletRequest request, HttpServletResponse resp) throws Exception {
        
        // Do your POST logic here.
        // Parameters could be gathered from request var.
        return null;
    }
    
}
