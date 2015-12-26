/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.restwrapper;

import com.doubotis.restwrapper.data.JObject;
import com.doubotis.restwrapper.servlet.SSO;

/**
 *
 * @author Christophe
 */
public class DefaultSSO extends BaseSSO {

    public DefaultSSO(String key)
    {
        super(key);
    }
    
    @Override
    public boolean hasRole(String role) {
        return true;
    }

    @Override
    public JObject getAuthentication() {
        return new JObject();
    }
    
    
    
}
