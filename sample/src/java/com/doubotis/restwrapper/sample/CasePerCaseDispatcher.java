/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.restwrapper.sample;

import com.doubotis.restwrapper.Dispatcher;
import com.doubotis.restwrapper.sample.impl.Base;
import com.doubotis.restwrapper.sample.impl.PostSample;
import com.doubotis.restwrapper.servlet.impl.NativeImpl;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Christophe
 */
public class CasePerCaseDispatcher extends Dispatcher {

    @Override
    public Class<? extends NativeImpl> getClassForRequest(HttpServletRequest request) {
        
        String action = (request.getParameter("action") != null) ? request.getParameter("action") : "";
        
        if (action.equals(""))
            return null;        // Will trigger an error ResourceNotFound.
        
        if (action.equals("getData1"))
            return Base.class;
        
        if (action.equals("postSample"))
            return PostSample.class;
        
        return null;
    }
    
}
