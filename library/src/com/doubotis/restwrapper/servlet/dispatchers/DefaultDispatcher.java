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
package com.doubotis.restwrapper.servlet.dispatchers;

import com.doubotis.restwrapper.defaults.impl.BaseDefaults;
import com.doubotis.restwrapper.servlet.Dispatcher;
import com.doubotis.restwrapper.servlet.RequestImplementation;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Christophe
 */
public class DefaultDispatcher extends Dispatcher {

    @Override
    public Class<? extends RequestImplementation> getClassForRequest(HttpServletRequest request) {
        return BaseDefaults.class;
    }
    
}
