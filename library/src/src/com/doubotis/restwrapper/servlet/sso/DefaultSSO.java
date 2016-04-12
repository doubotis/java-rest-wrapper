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
package com.doubotis.restwrapper.servlet.sso;

import com.doubotis.restwrapper.data.JObject;
import com.doubotis.restwrapper.servlet.sso.SSO;

/** A default implementation of SSO, that just returns true for any roles
 * and stores a client key in case of some key is provided.
 * @author Christophe
 */
public class DefaultSSO extends SSO {

    private String mClientKey;
    
    public DefaultSSO()
    {
        super();
    }
    
    @Override
    public void provideKey(String clientKey) {
        mClientKey = clientKey;
    }
    
    @Override
    public boolean hasRole(String role) {
        return true;
    }

    @Override
    public JObject getAuthentication() {
        JObject object = new JObject();
        object.put("clientKey", mClientKey);
        return object;
    }

    
    
    
    
}
