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

package com.doubotis.restwrapper.servlet;

import com.doubotis.restwrapper.servlet.exceptions.BadRequestException;
import java.util.Vector;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Christophe
 */
public class RequestAnalyzer
{    
    // Variables
    private HttpServletRequest mRequest;
    private String[] mFields;
    
    public RequestAnalyzer(HttpServletRequest request)
    {
        mRequest = request;
    }
    
    public SingleRequestResult analyzeSingle() throws BadRequestException
    {
        // When single, parameters fields is detected.
        String fieldValue = mRequest.getParameter("fields");
        if (fieldValue == null) fieldValue = "";
        String[] fields = fieldValue.split(",");
        if (fields.length == 0)
            throw new BadRequestException();

        SingleRequestResult srs = new SingleRequestResult();
        srs.fields = fields;
        return srs;
    }
    
    public ListRequestResult analyzeList() throws BadRequestException
    {
        // When single, parameters fields is detected.
        String queryValue = (mRequest.getParameter("q") != null) ? mRequest.getParameter("q") : "";
        String paginationCount = (mRequest.getHeader("X-Pagination") != null) ? mRequest.getHeader("X-Pagination") : "25";
        String pageValue = (mRequest.getParameter("page") != null) ? mRequest.getParameter("page") : "1";
        String orderByValue = (mRequest.getParameter("sort") != null) ? mRequest.getParameter("sort") : "";

        ListRequestResult lrs = new ListRequestResult();
        
        try
        {
            lrs.elementsPerPage = Integer.parseInt(paginationCount);
            lrs.page = Integer.parseInt(pageValue);
            lrs.query = queryValue;
            if (orderByValue.equals(""))
                lrs.orderBy = new String[] {};
            else
                lrs.orderBy = orderByValue.split(",");
            
            lrs.filterBy = analyzeQuery(queryValue);
            
            if (lrs.elementsPerPage < 5 || lrs.elementsPerPage > 50)
                throw new BadRequestException();
            
            
        } catch (NumberFormatException e) { throw new BadRequestException(); }
        
        return lrs;
    }

    private String[] analyzeQuery(String queryValue)
    {
        System.out.println("Query analyzing...");
        Vector<String> queries = new Vector<String>();
        
        System.out.println("Loop on query");
        String[] queryProperties = queryValue.split(";|\\|");
        for (int i=0; i < queryProperties.length; i++)
        {
            System.out.println(" - " + queryProperties[i]);
        }
        
        System.out.println("Query analyze ended!");
        return queryProperties;
    }
    
    protected static abstract class RequestResult { }
    
    public static class SingleRequestResult extends RequestResult
    {
        public String[] fields;
    }
    
    public static class ListRequestResult extends RequestResult
    {
        public int elementsPerPage;
        public int page;
        public String query;
        public String[] orderBy;
        public String[] filterBy;
    }
}
