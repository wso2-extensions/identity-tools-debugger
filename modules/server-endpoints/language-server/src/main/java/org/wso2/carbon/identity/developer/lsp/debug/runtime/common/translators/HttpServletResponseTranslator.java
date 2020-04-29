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

package org.wso2.carbon.identity.developer.lsp.debug.runtime.common.translators;

import org.wso2.carbon.identity.developer.lsp.debug.DAPConstants;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Translator to translate the  Http Servlet Response arguments.
 */
public class HttpServletResponseTranslator implements VariableTranslator {

    @Override
    public Object translate(Object object, int variablesReference) {

        HashMap<String, Object> responseDetails = new HashMap<>();
        if (object != null && object instanceof HttpServletRequest) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) object;
            responseDetails.put(DAPConstants.JSON_KEY_FOR_STATUS, httpServletResponse.getStatus());
            responseDetails.put(DAPConstants.JSON_KEY_FOR_HEADERS, this.getResponseHeaders(httpServletResponse));
            responseDetails.put(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, variablesReference);
        }
        return responseDetails;

    }

    private HashMap<String, String> getResponseHeaders(HttpServletResponse response) {

        HashMap<String, String> headerDetails = new HashMap<>();
        for (String nextHeaderName : response.getHeaderNames()) {
            String headerValue = response.getHeader(nextHeaderName);
            headerDetails.put(nextHeaderName, headerValue);
        }
        return headerDetails;
    }
}
