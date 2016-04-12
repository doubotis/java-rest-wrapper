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

import com.doubotis.restwrapper.servlet.sso.DefaultSSO;
import com.doubotis.restwrapper.responses.DataResponse;
import com.doubotis.restwrapper.responses.JSONResponse;
import com.doubotis.restwrapper.responses.XMLResponse;
import com.doubotis.restwrapper.servlet.api.APIHttpServlet;
import com.doubotis.restwrapper.servlet.Dispatcher;
import com.doubotis.restwrapper.servlet.sso.SSO;
import java.util.ArrayList;

/** A default implementation of APIHttpServlet that uses any
 * initialization parameters provided in web.xml to work. If no parameters
 * are provided, the servlet serves a basic 200 response to all requests.
 *
 * @author Christophe
 */
public class DefaultAPIHttpServlet extends APIHttpServlet {

    @Override
    public void onSetup() {
        
        // Build the default setup, in case some parameters are not
        // provided.
        buildDefaultSetup();
        
        // SSO parameter ?
        String ssoClassName = getServletConfig().getInitParameter("ssoClass");
        if (ssoClassName != null)
        {
            try
            {
                Class c = Class.forName(ssoClassName);
                SSO sso = (SSO)c.newInstance();
                setSSO(sso);
            } catch (Exception e) {}
        }
        
        /// Dispatcher parameter ?
        String dispatcherClassName = getServletConfig().getInitParameter("dispatcherClass");
        if (dispatcherClassName != null)
        {
            try
            {
                Class c = Class.forName(dispatcherClassName);
                Dispatcher dis = (Dispatcher)c.newInstance();
                setDispatcher(dis);
            } catch (Exception e) {}
        }
        
        // Return Types parameter ?
        String returnTypes = getServletConfig().getInitParameter("returnTypes");
        if (returnTypes != null)
        {
            String[] types = returnTypes.split(",");
            if (types.length > 0)
            {
                ArrayList<DataResponse> dataResponses = new ArrayList<DataResponse>();
                for (int i=0; i < types.length; i++)
                {
                    if (types[i].equals("json"))
                        dataResponses.add(new JSONResponse());
                    else if (types[i].equals("xml"))
                        dataResponses.add(new XMLResponse());
                }
                DataResponse[] array = new DataResponse[dataResponses.size()];
                dataResponses.toArray(array);
                setReturnTypes(array);
            }
        }
        
    }
    
    private void buildDefaultSetup()
    {
        setSSO(new DefaultSSO());
    }
    
}
