/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.restwrapper.data;

import com.thoughtworks.xstream.XStream;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author Christophe
 */
public class JObject extends HashMap<String, Object> implements JSONCompatible, XMLCompatible {

    @Override
    public String toJSONString() {
        
        JSONObject jsonObject = new JSONObject(this);
        return jsonObject.toJSONString();
    }
    
    @Override
    public String toXMLString() {
        
        XStream xStream = new XStream();
        xStream.registerConverter(new XMLMapEntryConverter());
        xStream.alias("root", Map.class);

        String xml = xStream.toXML(this);
        return xml;
    }
    
    
    
}
