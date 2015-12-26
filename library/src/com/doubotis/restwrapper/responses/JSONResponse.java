/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.restwrapper.responses;

import com.doubotis.restwrapper.data.JObject;

/**
 *
 * @author Christophe
 */
public class JSONResponse implements DataResponse {

    @Override
    public String convert(JObject object) {
        
        return object.toJSONString();
        
    }

    @Override
    public String getScheme() {
        return "json/data";
    }
    
}
