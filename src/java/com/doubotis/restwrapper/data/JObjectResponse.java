/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.restwrapper.data;

import com.doubotis.restwrapper.servlet.exc.HTTPException;
import com.thoughtworks.xstream.XStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.json.simple.JSONObject;

/**
 *
 * @author Christophe
 */
public class JObjectResponse extends JObject {

    public JObjectResponse(boolean statusOk)
    {
        this.put("status", (statusOk) ? "ok" : "ko");
        this.put("data", new JObject());
    }
    
    public static JObjectResponse fromException(Exception exception)
    {
        return fromException(exception, false);
    }
    
    public static JObjectResponse fromException(Exception exception, boolean showStackTrace)
    {
        JObjectResponse r = new JObjectResponse(false);
        
        StringWriter sw = null;
        if (exception instanceof HTTPException && exception.getCause() != null)
        {
            sw = new StringWriter();
            exception.getCause().printStackTrace(new PrintWriter(sw));
        }
        else
        {
            sw = new StringWriter();
            exception.printStackTrace(new PrintWriter(sw));
        }
        
        if (showStackTrace)
            r.put("stacktrace", sw.toString());
        r.put("message", exception.getMessage());
        r.put("status", "ko");
        return r;
    }
    
    @Override
    public String toJSONString()
    {
        JSONObject jsonObject = new JSONObject(this);
        return jsonObject.toJSONString();
    }
    
    @Override
    public String toXMLString()
    {
        XStream xStream = new XStream();
        xStream.registerConverter(new XMLMapEntryConverter());
        xStream.alias("response", JObjectResponse.class);

        String xml = xStream.toXML(this);
        return xml;
    }

}
