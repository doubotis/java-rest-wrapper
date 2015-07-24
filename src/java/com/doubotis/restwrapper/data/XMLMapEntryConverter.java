/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.restwrapper.data;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Christophe
 */
public class XMLMapEntryConverter implements Converter
{
    public boolean canConvert(Class clazz) {
            return AbstractMap.class.isAssignableFrom(clazz) || ArrayList.class.isAssignableFrom(clazz);
    }

    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {

        if (value instanceof ArrayList)
        {
            ArrayList arr = (ArrayList) value;
            int index = 0;
            for (Object obj : arr)
            {
                writer.startNode("element" + index);
                
                if (obj != null)
                {
                    if (obj instanceof ArrayList)
                    {
                        marshal(obj, writer, context);
                    }
                    else if (obj instanceof AbstractMap)
                    {
                        marshal(obj, writer, context);
                    }
                    else
                        writer.setValue(obj.toString());
                }
                
                writer.endNode();
            }
        }
        else
        {
            AbstractMap map = (AbstractMap) value;
            for (Object obj : map.entrySet())
            {
                Map.Entry entry = (Map.Entry) obj;
                writer.startNode(entry.getKey().toString());
                Object val = entry.getValue();
                if ( null != val ) {
                    if (val instanceof ArrayList)
                    {
                        marshal(val, writer, context);
                    }
                    else if (val instanceof AbstractMap)
                    {
                        marshal(val, writer, context);
                    }
                    else
                        writer.setValue(val.toString());
                }
                writer.endNode();
            }
        }
        

    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

        Map<String, String> map = new HashMap<String, String>();

        while(reader.hasMoreChildren()) {
            reader.moveDown();

            String key = reader.getNodeName(); // nodeName aka element's name
            String value = reader.getValue();
            map.put(key, value);

            reader.moveUp();
        }

        return map;
    }    
}
