/* 
 * Copyright (C) 2015 Christophe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.doubotis.restwrapper.servlet.api;

import com.doubotis.restwrapper.responses.DataResponse;
import com.doubotis.restwrapper.data.JObject;
import com.doubotis.restwrapper.data.JObjectResponse;
import com.doubotis.restwrapper.servlet.APIPatternAnalyzer;
import com.doubotis.restwrapper.servlet.APIPatternAnalyzer.PatternDescriptor;
import com.doubotis.restwrapper.servlet.Dispatcher;
import com.doubotis.restwrapper.servlet.ProcessDuration;
import com.doubotis.restwrapper.servlet.ProcessDuration;
import com.doubotis.restwrapper.servlet.sso.SSO;
import com.doubotis.restwrapper.servlet.exceptions.HTTPException;
import com.doubotis.restwrapper.servlet.exceptions.InternalErrorException;
import com.doubotis.restwrapper.servlet.exceptions.NotImplementedException;
import com.doubotis.restwrapper.servlet.exceptions.ResourceNotFoundException;
import com.doubotis.restwrapper.servlet.RequestImplementation;
import com.doubotis.restwrapper.servlet.RequestImplementation;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Christophe
 */
public abstract class APIHttpServlet extends HttpServlet {
    
    private SSO mSSO;
    private Dispatcher mDispatcher;
    private DataResponse[] mResponses = new DataResponse[] {};
    private HashMap<String,String> mAliases = new HashMap<String, String>();

    public static final String FORMAT_JSON = "json";
    public static final String FORMAT_XML = "xml";
    
    /** Could be used to set an alias for the SSO key parameter. */
    public static final String PARAMETER_SSO = "sso";
    /** Could be used to set an alias for the current page. */
    public static final String PARAMETER_PAGE = "page";
    /** Could be used to set an alias for the maximum number of lines. */
    public static final String PARAMETER_MAX_LINES = "maxLines";
    
    private boolean mSetupDone = false;
    
    /** Process any additional setup on start of the servlet. Called just before
     * to handle {@link processRequest}. Override it to add your setup.
     */
    public abstract void onSetup();
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        // Before any request, setup the work.
        if (mSetupDone == false)
        {
            onSetup();
            mSetupDone = true;
        }
        
        PrintWriter out = response.getWriter();
        
        ProcessDuration processDuration = new ProcessDuration();
        processDuration.setStartDate(new Date());
        
        String requestInfo = (request.getPathInfo() == null) ? "" : request.getPathInfo();
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
        
        // Check for existence of SSO Key and storing it in attributes.
        String key = mAliases.containsKey(PARAMETER_SSO) ? mAliases.get(PARAMETER_SSO) : PARAMETER_SSO;
        String ssoKey = request.getParameter(key);
        if (ssoKey == null || ssoKey.equals(""))
        {
            ssoKey = request.getHeader(key);
        }
        
        // HERE we use the instanciated SSO object, if exists.
        // If the SSO object doesn't exists we ignore its behavior.
        SSO sso = mSSO;
        
        if (sso != null)
            request.setAttribute(RequestImplementation.ATTRIBUTE_SSO, sso);
        
        boolean showStackTrace = true;
        
        try
        {
            try
            {
                Class c = mDispatcher.getClassForRequest(request);
                if (c == null)
                    throw new ResourceNotFoundException();
                
                JObjectResponse objectResponse = handleInvocation(c, request, response, out);
                
                processDuration.setEndDate(new Date());
                long timestampDuration = processDuration.getDurationTimestamp(TimeUnit.MILLISECONDS);
                objectResponse.put("timestamp", timestampDuration);
                if (extension.equals(FORMAT_XML))
                {
                    response.setHeader("Content-Type", "text/xml");
                    out.print(objectResponse.toXMLString());
                }
                else
                {
                    response.setHeader("Content-Type", "text/json");
                    out.print(objectResponse.toJSONString());
                }
                   
            }
            catch (HTTPException httpe)
            {
                response.setStatus(httpe.getHTTPStatus());
                PrintWriter pw = response.getWriter();
                JObjectResponse objectResponse = JObjectResponse.fromException(httpe, showStackTrace);
                processDuration.setEndDate(new Date());
                long timestampDuration = processDuration.getDurationTimestamp(TimeUnit.MILLISECONDS);
                objectResponse.put("timestamp", timestampDuration);
                if (extension.equals(FORMAT_XML))
                {
                    response.setHeader("Content-Type", "text/xml");
                    pw.print(objectResponse.toXMLString());
                }
                else
                {
                    response.setHeader("Content-Type", "text/json");
                    pw.print(objectResponse.toJSONString());
                }
            }
            catch (Throwable t)
            {
                throw t;
            }
            
        } catch (Throwable e)
        {
            InternalErrorException iee = new InternalErrorException(e);
            response.setStatus(500);
            PrintWriter pw = response.getWriter();
            JObjectResponse objectResponse = JObjectResponse.fromException(iee, showStackTrace);
            processDuration.setEndDate(new Date());
            long timestampDuration = processDuration.getDurationTimestamp(TimeUnit.MILLISECONDS);
            objectResponse.put("timestamp", timestampDuration);
            pw.flush();
            if (extension.equals(FORMAT_XML))
            {
                response.setHeader("Content-Type", "text/xml");
                pw.print(objectResponse.toXMLString());
            }
            else
            {
                response.setHeader("Content-Type", "text/json");
                pw.print(objectResponse.toJSONString());
            }
        }
        out.close();
    }
    
    private JObjectResponse handleInvocation(Class fromClass, HttpServletRequest request, HttpServletResponse response, PrintWriter pw) throws Throwable
    {
        String httpMethod = request.getMethod();
        
        Method m = fromClass.getMethod(httpMethod.toLowerCase(), HttpServletRequest.class, HttpServletResponse.class);
        Object o = fromClass.newInstance();
        
        if (!(o instanceof RequestImplementation))
            throw new IllegalArgumentException("The request implementation class must extends com.doubotis.restwrapper.servlet.RequestImplementation");
        
        RequestImplementation impl = (RequestImplementation)o;
        impl.onInit(request, response);
        
        JObjectResponse responseNode = new JObjectResponse(true);
        try
        {
            JObject jObject = (JObject)m.invoke(o, request, response);
            responseNode.put("data", jObject);
            
        } catch (InvocationTargetException ite)
        {
            throw ite.getCause();
        }
        
        impl.onFinish();
        
        return responseNode;
    }
    
    /* =============================================== */
    
    /** Sets a custom SSO class that will be used by the mechanism
     * and allow you to check the roles.
     */
    protected void setSSO(SSO sso)
    {
        mSSO = sso;
    }
    
    /** Sets a dispatcher for this servlet. A dispatcher serves as a
     * request decision maker. Depending on request parameters found,
     * the dispatcher will ask to the server to use different
     * implementations. See {@see Dispatcher}.
     * @param dispatcher 
     */
    protected void setDispatcher(Dispatcher dispatcher)
    {
        mDispatcher = dispatcher;
    }
    
    protected void setReturnTypes(DataResponse[] responses)
    {
        mResponses = responses;
    }
    
    /** Sets aliases for standard parameters. You can use either
     * {@link APIHttpServlet#PARAMETER_SSO}, 
     * {@link APIHttpServlet#PARAMETER_PAGE},
     * {@link APIHttpServlet.PARAMETER_MAXLINES} and all others constants 
     * beginning by PARAMETER_.
     * @param nativeParameter
     * @param alias 
     */
    protected void setParameterAlias(String nativeParameter, String alias)
    {
        mAliases.put(nativeParameter, alias);
    }
    
    /* =============================================== */

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
