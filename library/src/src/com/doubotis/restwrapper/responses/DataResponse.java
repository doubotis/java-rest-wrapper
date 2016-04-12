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
package com.doubotis.restwrapper.responses;

import com.doubotis.restwrapper.data.JObject;

/** Represents a content-type the API can serve.
 * @author Christophe
 */
public interface DataResponse {
    
    public String convert(JObject object);
    public String getScheme();
}
