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
package com.doubotis.restwrapper.servlet;

import com.doubotis.restwrapper.servlet.exceptions.HTTPException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Christophe
 */
@REST(path = "/")
public abstract class RequestImplementation {
    
    /** Could be used to retreive an information from the attributes inside the
     * RequestImplementation. This one serves the SSO object to allow any role
     * checking.
     */
    public static final String ATTRIBUTE_SSO = "sso";
    
    /** Triggered when the implementatoin is first initialized. You can
     * make your database connection work here for instance.
     */
    public abstract void onInit(HttpServletRequest request, HttpServletResponse resp) throws HTTPException;
    /** Triggered when the implementation is finished. You can dispose any
     * resources like database connections here.
     */
    public abstract void onFinish() throws HTTPException;
}
