/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.restwrapper.servlet;

import javax.servlet.http.HttpServletRequest;

/** A dispatcher serves as a request decision maker. 
 * Depending on request parameters found, the dispatcher will ask to the 
 * server to use different implementations.
 * There some utility dispatchers classes that could be used "as is", like
 * {@link FileResourceDispatcher}, or you can create a dispatcher yourself
 * by subclassing {@link Dispatcher}.
 * @author Christophe
 */
public abstract class Dispatcher {
    
    /** Returns the implementation class for the specified request.
     * The returned class should implement NativeImpl.
     * @return the class that will be built to handle the request.
     */
    public abstract Class<? extends RequestImplementation> getClassForRequest(HttpServletRequest request);
}
