/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.restwrapper.servlet;

import com.doubotis.restwrapper.data.JObject;

/**
 *
 * @author Christophe
 */
public abstract class SSO
{    
    /** Has the SSO key the asked role */
    public abstract boolean hasRole(String role);
    
    /** Get the object described for this SSO key. */
    public abstract JObject getAuthentication();
}
