/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.restwrapper.servlet.dispatchers;

import com.doubotis.restwrapper.servlet.Dispatcher;
import com.doubotis.restwrapper.servlet.APIPatternAnalyzer;
import com.doubotis.restwrapper.servlet.RequestImplementation;
import com.doubotis.restwrapper.servlet.exceptions.HTTPException;
import com.doubotis.restwrapper.servlet.exceptions.InternalErrorException;
import com.doubotis.restwrapper.servlet.exceptions.NotImplementedException;
import com.doubotis.restwrapper.servlet.exceptions.ResourceNotFoundException;
import javax.servlet.http.HttpServletRequest;

/** FileResourceDispatcher is an utility class that use a .txt resource file
 * including some information about the classes to use for each depending
 * request, based on a regex pattern.
 * <br/><br/>
 * For instance, here is a resource line :<br/>
 * <code>
 * GET POST com.doubotis.restwrapper.sample.impl.AnImplClass /base
 * </code>
 * @author Christophe
 */
public class FileResourceDispatcher extends Dispatcher {
    
    private String mPath;
    
    public FileResourceDispatcher(String path)
    {
        mPath = path;
    }

    @Override
    public Class<? extends RequestImplementation> getClassForRequest(HttpServletRequest request) {
        
        // Prepares the request path info.
        String requestInfo = request.getPathInfo();
        String resource;
        String extension;
        int reqPos;
        
        int dotPos = requestInfo.lastIndexOf(".");
        if (dotPos == -1) {
            extension = "json";
            reqPos = requestInfo.indexOf("?");
            if (reqPos == -1) reqPos = requestInfo.length();
            resource = requestInfo.substring(0, reqPos);
            if (resource.equals("")) resource = requestInfo;
        }
        else {
            dotPos++;
            reqPos = requestInfo.indexOf("?", dotPos);
            if (reqPos == -1) reqPos = requestInfo.length();
            extension = requestInfo.substring(dotPos, reqPos);
            if (extension.equals("")) extension = requestInfo.substring(dotPos);
            resource = requestInfo.substring(0, dotPos-1);
        }
        
        // Prepares now the API Pattern.
        APIPatternAnalyzer apa = new APIPatternAnalyzer(mPath);
        APIPatternAnalyzer.PatternDescriptor pd = apa.find(resource);
        
        String httpMethod = request.getMethod();
        
        Class c = null;
        try
        {
            c = Class.forName(pd.getImplementationClass());
            if (c.getSuperclass() != RequestImplementation.class)
                throw new InternalErrorException();
            
            return c;
            
        } catch (ClassNotFoundException cnfe) { return null; }
    }
    
}
