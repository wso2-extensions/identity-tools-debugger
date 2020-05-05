/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.developer.lsp.debug.dap.serializer.encoders;

import com.google.gson.JsonObject;
import org.wso2.carbon.identity.developer.lsp.debug.DAPConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Encode the translated the HttpServletResponse variable.
 */
public class HttpServletResponseEncoder implements VariableEncoder {

    @Override
    public JsonObject translate(Object httpServletResponseTranslated) {

        JsonObject arrayElement = new JsonObject();
        JsonObject valueObject = new JsonObject();
        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_TYPE, DAPConstants.VARIABLE_TYPE_OBJECT);
        HashMap<String, Object> responseDetails = (HashMap<String, Object>) httpServletResponseTranslated;
        valueObject.add(DAPConstants.JSON_KEY_FOR_HEADERS, this.getHeaders(responseDetails));
        valueObject.addProperty(DAPConstants.JSON_KEY_FOR_STATUS,
                this.getResponseStatus(responseDetails));
        arrayElement.add(DAPConstants.JSON_KEY_FOR_VALUE, valueObject);
        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, 0);
        return arrayElement;
    }

    private JsonObject getHeaders(HashMap<String, Object> requestdetails) {

        HashMap<String, String> headers =
                (HashMap<String, String>) requestdetails.get(DAPConstants.JSON_KEY_FOR_HEADERS);
        JsonObject arrayElement = new JsonObject();
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                arrayElement.addProperty(header.getKey(), header.getValue());
            }
        }
        return arrayElement;
    }

    private Integer getResponseStatus(HashMap<String, Object> responseDetails) {

        return (Integer) responseDetails.get(DAPConstants.JSON_KEY_FOR_STATUS);
    }
}
