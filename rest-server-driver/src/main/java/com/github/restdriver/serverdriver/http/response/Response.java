/**
 * Copyright © 2010-2011 Nokia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.restdriver.serverdriver.http.response;

import java.util.List;

import com.github.restdriver.serverdriver.http.Header;
import org.codehaus.jackson.JsonNode;
import org.w3c.dom.Element;

/**
 * Encapsulates a response from an HTTP server.
 */
public interface Response {

    /**
     * What was the response code?
     *
     * @return the HTTP status code of the response
     */
    int getStatusCode();

    /**
     * What was the content of the response?
     *
     * @return The response body content as a string
     */
    String getContent();

    /**
     * What headers did the server send?
     *
     * @return A list of headers
     */
    List<Header> getHeaders();

    /**
     * How long did the response take?
     * 
     * @return Round-trip response time in milliseconds
     */
    long getResponseTime();


    /**
     * Returns the JSON response content as a JsonNode, or throws RuntimeMappingException
     *
     * @return The JsonNode
     */
    JsonNode asJson();

    /**
     * Returns the XML response content as an org.w3c.Element, or throws RuntimeMappingException
     *
     * @return The Element
     */
    Element asXml();

}
