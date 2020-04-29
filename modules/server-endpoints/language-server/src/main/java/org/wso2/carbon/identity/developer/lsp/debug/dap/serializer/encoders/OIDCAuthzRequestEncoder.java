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
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.developer.lsp.debug.DAPConstants;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Encode the translated the OIDCAuthzRequest variable.
 */
public class OIDCAuthzRequestEncoder implements VariableEncoder {

    @Override
    public JsonObject translate(Object oidcAuthzRequestTranslated) {

        JsonObject arrayElement = new JsonObject();
        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_TYPE, DAPConstants.VARIABLE_TYPE_OBJECT);
        HashMap<String, Object> authzRequestDetails = (HashMap<String, Object>) oidcAuthzRequestTranslated;
        arrayElement.add(DAPConstants.JSON_KEY_FOR_VALUE, getOAuthMessage(authzRequestDetails));
        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, 0);
        return arrayElement;
    }

    private JsonObject getOAuthMessage(HashMap<String, Object> authzRequestDetails) {

        JsonObject valueObject = new JsonObject();
        for (Map.Entry<String, Object> authzRequestDetail : authzRequestDetails.entrySet()) {
            if (StringUtils.equals(authzRequestDetail.getKey(), DAPConstants.OAUTH_MESSAGE_REQUEST) &&
                    authzRequestDetail.getValue() instanceof HttpServletRequest) {
                HttpServletRequest httpServletRequest = (HttpServletRequest) authzRequestDetail.getValue();
                valueObject.addProperty(DAPConstants.OAUTH_RESPONSE_TYPE,
                        httpServletRequest.getParameter(DAPConstants.OAUTH_RESPONSE_TYPE));
                valueObject.addProperty(DAPConstants.OAUTH_CALLBACK_URI,
                        httpServletRequest.getParameter(DAPConstants.OAUTH_CALLBACK_URI));
                valueObject.addProperty(DAPConstants.OAUTH_SCOPE,
                        httpServletRequest.getParameter(DAPConstants.OAUTH_SCOPE));
                valueObject.addProperty(DAPConstants.OAUTH_MESSAGE_CLIENT_ID,
                        getClientId(httpServletRequest));
                valueObject.addProperty(DAPConstants.OAUTH_MESSAGE_SESSION_DATA_KEY,
                        getSessionDataKey(httpServletRequest));
                valueObject.addProperty(FrameworkConstants.RequestParams.TO_COMMONAUTH,
                        isRequestToCommonauth(httpServletRequest));
            } else if (StringUtils.equals(authzRequestDetail.getKey(), DAPConstants.OAUTH_MESSAGE_FORCE_AUTHENTICATE)) {
                valueObject.addProperty(DAPConstants.OAUTH_MESSAGE_FORCE_AUTHENTICATE,
                        (Boolean) authzRequestDetail.getValue());
            } else if (StringUtils.equals(authzRequestDetail.getKey(),
                    DAPConstants.OAUTH_MESSAGE_IS_PASSIVE_AUTHENTICATION)) {
                valueObject.addProperty(DAPConstants.OAUTH_MESSAGE_IS_PASSIVE_AUTHENTICATION,
                        (Boolean) authzRequestDetail.getValue());
            }
        }
        return valueObject;
    }

    private String getClientId(HttpServletRequest request) {

        return request.getParameter(DAPConstants.OAUTH_MESSAGE_CLIENT_ID);
    }

    private String getSessionDataKey(HttpServletRequest request) {

        String sessionDataKey = (String) request.getAttribute(DAPConstants.OAUTH_MESSAGE_SESSION_DATA_KEY);
        if (sessionDataKey == null) {
            sessionDataKey = request.getParameter(DAPConstants.OAUTH_MESSAGE_SESSION_DATA_KEY);
        }
        return sessionDataKey;
    }

    private boolean isRequestToCommonauth(HttpServletRequest request) {

        return Boolean.parseBoolean(request.getParameter(FrameworkConstants.RequestParams.TO_COMMONAUTH));
    }
}
