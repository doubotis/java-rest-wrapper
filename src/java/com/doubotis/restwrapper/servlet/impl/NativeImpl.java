/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.restwrapper.servlet.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Christophe
 */
public abstract class NativeImpl
{
    public static String ATTRIBUTE_SSO = "sso";
    
    public abstract void onCreate(HttpServletRequest request, HttpServletResponse resp);
    public abstract void onFinish();
}
