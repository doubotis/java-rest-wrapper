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

package com.doubotis.restwrapper;

import com.doubotis.restwrapper.data.JObject;
import com.doubotis.restwrapper.data.JObjectResponse;
import com.doubotis.restwrapper.servlet.APIPatternAnalyzer;
import com.doubotis.restwrapper.servlet.APIPatternAnalyzer.PatternDescriptor;
import com.doubotis.restwrapper.servlet.ProcessDuration;
import com.doubotis.restwrapper.servlet.exc.HTTPException;
import com.doubotis.restwrapper.servlet.exc.InternalErrorException;
import com.doubotis.restwrapper.servlet.exc.NotImplementedException;
import com.doubotis.restwrapper.servlet.exc.ResourceNotFoundException;
import com.doubotis.restwrapper.servlet.impl.NativeImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Christophe
 */
public class ServletAPI extends HttpServlet {

    public static String FORMAT_JSON = "json";
    public static String FORMAT_XML = "xml";
    
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
        PrintWriter out = response.getWriter();
        
        ProcessDuration processDuration = new ProcessDuration();
        processDuration.setStartDate(new Date());
        
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
        
        // Check for existence of SSO Key and storing it in attributes.
        String ssoKey = request.getParameter("key");
        if (ssoKey == null || ssoKey.equals(""))
        {
            ssoKey = request.getHeader("key");
        }
        // HERE you can specify the SSO class to use to represent the SSO key system.
        request.setAttribute(NativeImpl.ATTRIBUTE_SSO, new DefaultSSO(ssoKey));
        
        boolean showStackTrace = false;
        
        try
        {
            try
            {
                APIPatternAnalyzer apa = new APIPatternAnalyzer(Constants.RESOURCE_PATH);

                PatternDescriptor pd = apa.find(resource);
                if (pd == null)
                    throw new ResourceNotFoundException();

                    JObjectResponse objectResponse = handleInvocation(pd, request, response, out);
                    
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
    
    private JObjectResponse handleInvocation(PatternDescriptor pd, HttpServletRequest request, HttpServletResponse response, PrintWriter pw) throws Throwable
    {
        String httpMethod = request.getMethod();
        
        Class c = null;
        try
        {
            c = Class.forName(ServletAPI.class.getName().replace("ServletAPI","") + "servlet.impl." + pd.getImplementationClass());    
            if (!pd.getMethods().contains(httpMethod.toUpperCase()))
                throw new NotImplementedException();
            
        } catch (ClassNotFoundException cnfe) { throw new NotImplementedException(); }
                
        Method m = c.getMethod(httpMethod.toLowerCase(), HttpServletRequest.class, HttpServletResponse.class);
        Object o = c.newInstance();
        
        if (!(o instanceof NativeImpl))
            throw new IllegalArgumentException("The implementation class must extends NativeImpl");
        
        NativeImpl impl = (NativeImpl)o;
        impl.onCreate(request, response);
        
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
