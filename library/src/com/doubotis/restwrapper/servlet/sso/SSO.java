/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.restwrapper.servlet.sso;

import com.doubotis.restwrapper.data.JObject;

/**
 *
 * @author Christophe
 */
public abstract class SSO
{    
    /** Provides the key to apply to check the roles. */
    public abstract void provideKey(String clientKey);
    
    /** Has the SSO key the asked role */
    public boolean hasRole(String role)
    {
        return true;
    }
    /** Get the object described for this SSO key. */
    public JObject getAuthentication()
    {
        return new JObject();
    }
}
