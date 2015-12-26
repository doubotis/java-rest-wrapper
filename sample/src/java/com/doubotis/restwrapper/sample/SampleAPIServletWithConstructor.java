/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.restwrapper.sample;

import com.doubotis.restwrapper.APIHttpServlet;
import com.doubotis.restwrapper.Constants;
import com.doubotis.restwrapper.responses.DataResponse;
import com.doubotis.restwrapper.DefaultSSO;
import com.doubotis.restwrapper.Dispatcher;
import com.doubotis.restwrapper.FileResourceDispatcher;
import com.doubotis.restwrapper.responses.JSONResponse;
import com.doubotis.restwrapper.responses.XMLResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Christophe
 */
public class SampleAPIServletWithConstructor extends APIHttpServlet {

    public SampleAPIServletWithConstructor()
    {
        // We can specify various settings for the APIHttpServlet.
        
        // Sets the parameter identifiers that will be used for specific
        // mechanics of the API.
        setParameterAlias(APIHttpServlet.PARAMETER_SSO, "ssoKey");
        
        // Sets a custom SSO class.
        setSSO(DefaultSSO.class);
        
        // Sets the dispatcher.
        // The dispatcher serves as the element that will be used to
        // find which class to instantiate and use for each request type.
        Dispatcher dispatcher = new FileResourceDispatcher(Constants.RESOURCE_PATH);
        setDispatcher(dispatcher);
        
        // Sets the return schemes the system can handle.
        // By default, JSON and XML responses are managed, but
        // you may need to add some other kind of responses, like
        // default HTML or specific binary data.
        // The first element of the array is used as the default behavior.
        setReturnTypes(new DataResponse[] {
            new JSONResponse(),
            new XMLResponse()
        });
    }
    
}
