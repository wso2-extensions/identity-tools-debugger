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

package org.wso2.carbon.identity.developer.lsp.debug.runtime.translators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.developer.lsp.debug.DAPConstants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Translator to translate the  SAML Request arguments.
 */
public class OIDCTokenRequestTranslator implements VariableTranslator {

    private static Log log = LogFactory.getLog(OIDCTokenRequestTranslator.class);
    private OIDCTokenRequestTranslator() {

    }

    private static class OIDCAuthzRequestTranslatorHolder {

        private static final OIDCTokenRequestTranslator INSTANCE = new OIDCTokenRequestTranslator();
    }

    /**
     * This static method allow to get the instance of the SAMLRequestTranslator.
     *
     * @return The SAMLRequestTranslatorHolder instance.
     */
    public static OIDCTokenRequestTranslator getInstance() {

        return OIDCAuthzRequestTranslatorHolder.INSTANCE;
    }

    @Override
    public Object translate(Object object, int variablesReference) {

        if (object instanceof HttpServletRequestWrapper) {
            HttpServletRequest request = (HttpServletRequestWrapper) object;
            HashMap<String, Object> requestDetails = new HashMap<>();
            if (request.getParameterNames() != null) {
                HashMap<String, String> requestParameters = new HashMap<>();
                while (request.getParameterNames().hasMoreElements()) {
                    String key = request.getParameterNames().nextElement();
                    String value = request.getParameter(key);
                    requestParameters.put(key, value);
                }
                requestDetails.put(DAPConstants.JSON_KEY_FOR_REQUEST_PARAMETER,requestParameters);
            }

            Object oauthClientAuthnContextObj = request.getAttribute(DAPConstants.CLIENT_AUTHN_CONTEXT);
            if (oauthClientAuthnContextObj != null) {
                HashMap<String, Object> oauthClientAuthnContextObjDetails = new HashMap<>();
                for (Field field : object.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = null;
                    try {
                        value = field.get(object);
                    } catch (IllegalAccessException e) {
                        log.error("Could not found the class", e);
                    }
                    if (value != null) {
                        oauthClientAuthnContextObjDetails.put(field.getName(), value);
                    }
                }
                requestDetails.put(DAPConstants.CLIENT_AUTHN_CONTEXT, oauthClientAuthnContextObjDetails);
            }

            Enumeration headerNames = request.getHeaderNames();
            if (headerNames != null) {
                List<HttpRequestHeader> httpHeaderList = new ArrayList<>();
                while (headerNames.hasMoreElements()) {
                    String headerName = (String) headerNames.nextElement();
                    // since it is possible for some headers to have multiple values let's add them all.
                    Enumeration headerValues = request.getHeaders(headerName);
                    List<String> headerValueList = new ArrayList<>();
                    if (headerValues != null) {
                        while (headerValues.hasMoreElements()) {
                            headerValueList.add((String) headerValues.nextElement());
                        }
                    }
                    httpHeaderList.add(
                            new HttpRequestHeader(headerName,
                                    headerValueList.toArray(new String[headerValueList.size()])));
                }
                requestDetails.put(DAPConstants.JSON_KEY_FOR_HTTP_REQUEST_HEADERS, httpHeaderList);
            }
            return requestDetails;
        }
        return "NO Token Request Added.";
    }
}
