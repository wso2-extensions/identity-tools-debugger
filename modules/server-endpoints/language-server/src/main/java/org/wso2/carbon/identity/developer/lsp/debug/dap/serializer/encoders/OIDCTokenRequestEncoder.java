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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.wso2.carbon.identity.developer.lsp.debug.DAPConstants;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.common.translators.HttpRequestHeader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encode the translated the OIDCTokenRequest variable.
 */
public class OIDCTokenRequestEncoder implements VariableEncoder {

    @Override
    public JsonObject translate(Object oidcCTokenRequestTranslated) {

        JsonObject arrayElement = new JsonObject();
        arrayElement.addProperty(
                DAPConstants.JSON_KEY_FOR_TYPE, DAPConstants.VARIABLE_TYPE_OBJECT);
        HashMap<String, Object> tokenRequestDetails = (HashMap<String, Object>) oidcCTokenRequestTranslated;
        JsonObject values = new JsonObject();
        values.add(DAPConstants.JSON_KEY_FOR_REQUEST_PARAMETER,
                getOIDCTokenRequestParameters(tokenRequestDetails));
        values.add(DAPConstants.JSON_KEY_FOR_HTTP_REQUEST_HEADERS,
                getOIDCTokenRequestHeader(tokenRequestDetails));
        values.add(DAPConstants.CLIENT_AUTHN_CONTEXT,
                getOAuthClientAuthnContext(tokenRequestDetails));
        arrayElement.add(DAPConstants.JSON_KEY_FOR_VALUE, values);
        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, 0);
        return null;
    }

    private JsonObject getOIDCTokenRequestParameters(HashMap<String, Object> tokenRequestDetails) {

        JsonObject valueObject = new JsonObject();
        HashMap<String, String> requestParameters = (HashMap<String, String>)
                tokenRequestDetails.get(DAPConstants.JSON_KEY_FOR_REQUEST_PARAMETER);
        valueObject.add(DAPConstants.JSON_KEY_FOR_REQUEST_PARAMETER,
                new Gson().toJsonTree(requestParameters).getAsJsonObject());
        return valueObject;
    }

    private JsonArray getOIDCTokenRequestHeader(HashMap<String, Object> tokenRequestDetails) {

        List<HttpRequestHeader> httpHeaderList = (List<HttpRequestHeader>)
                tokenRequestDetails.get(DAPConstants.JSON_KEY_FOR_HTTP_REQUEST_HEADERS);
        JsonArray jsonArray = new JsonArray();
        for (HttpRequestHeader element : httpHeaderList) {
            jsonArray.add(new Gson().toJsonTree(element, HttpRequestHeader.class));
        }
        return jsonArray;
    }

    private JsonObject getOAuthClientAuthnContext(HashMap<String, Object> tokenRequestDetails) {

        HashMap<String, Object> oauthClientAuthnContextObjDetails = (HashMap<String, Object>)
                tokenRequestDetails.get(DAPConstants.CLIENT_AUTHN_CONTEXT);
        JsonObject valueObject = new JsonObject();
        for (Map.Entry<String, Object> oauthClientAuthnContextObjDetail :
                oauthClientAuthnContextObjDetails.entrySet()) {
            if (oauthClientAuthnContextObjDetail.getValue() instanceof String) {
                valueObject.addProperty(oauthClientAuthnContextObjDetail.getKey(),
                        (String) oauthClientAuthnContextObjDetail.getValue());
            } else if (oauthClientAuthnContextObjDetail.getValue() instanceof Boolean) {
                valueObject.addProperty(oauthClientAuthnContextObjDetail.getKey(),
                        (Boolean) oauthClientAuthnContextObjDetail.getValue());
            } else if (oauthClientAuthnContextObjDetail.getValue() instanceof List) {
                Gson gson = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree((List<String>) oauthClientAuthnContextObjDetail.getValue())
                        .getAsJsonArray();
                valueObject.add(DAPConstants.CLIENT_AUTHN_CONTEXT, myCustomArray);
            }
        }
        return valueObject;
    }

}
