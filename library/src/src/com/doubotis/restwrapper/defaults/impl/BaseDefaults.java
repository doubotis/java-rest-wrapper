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
package com.doubotis.restwrapper.defaults.impl;

import com.doubotis.restwrapper.data.JObject;
import com.doubotis.restwrapper.servlet.IGetHandler;
import com.doubotis.restwrapper.servlet.RequestImplementation;
import com.doubotis.restwrapper.servlet.exceptions.HTTPException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Christophe
 */
public class BaseDefaults extends RequestImplementation implements IGetHandler {

    @Override
    public void onInit(HttpServletRequest request, HttpServletResponse resp) throws HTTPException {
        
    }

    @Override
    public void onFinish() throws HTTPException {
        
    }

    @Override
    public JObject get(HttpServletRequest request, HttpServletResponse resp) throws Exception {
        
        JObject object = new JObject();
        object.put("version", "1.0");
        object.put("execution_date", new Date().getTime());
        return object;
    }
    
}
