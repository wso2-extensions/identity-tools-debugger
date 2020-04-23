/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.developer.lsp.debug.dap.serializer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.developer.lsp.debug.DAPConstants;
import org.wso2.carbon.identity.developer.lsp.debug.dap.messages.Argument;
import org.wso2.carbon.identity.developer.lsp.debug.dap.messages.VariablesResponse;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.translators.HttpRequestHeader;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Serializes the variables response.
 */
public class VariablesResponseSerializer implements JsonSerializer<VariablesResponse> {

    private static Log log = LogFactory.getLog(VariablesResponseSerializer.class);

    @Override
    public JsonElement serialize(VariablesResponse response, Type type,
                                 JsonSerializationContext jsonSerializationContext) {

        JsonObject object = new JsonObject();
        object.addProperty(DAPConstants.JSON_KEY_FOR_JSONRPC, DAPConstants.JSON_RPC_VERSION);
        object.addProperty(DAPConstants.JSON_KEY_FOR_ID, response.getId());
        object.add(DAPConstants.JSON_KEY_FOR_RESULT, generateResultObject(response));

        return object;
    }

    private JsonElement generateResultObject(VariablesResponse response) {

        JsonObject object = new JsonObject();
        object.addProperty(DAPConstants.JSON_KEY_FOR_COMMAND, response.getCommand());
        object.addProperty(DAPConstants.JSON_KEY_FOR_MESSAGE, response.getMessage());

        JsonObject body = new JsonObject();
        object.add(DAPConstants.JSON_KEY_FOR_BODY, body);
        body.add(DAPConstants.JSON_KEY_FOR_VARIABLES, generateVariablesArray(response));
        return object;
    }

    private JsonElement generateVariablesArray(VariablesResponse response) {

        JsonArray jsonArray = new JsonArray();

        if (response.getBody() != null) {
            Argument<HashMap<String, Object>> body = response.getBody();
            HashMap<String, Object> variables = body.getValue();

            if (variables == null) {
                return jsonArray;
            }

            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                JsonObject arrayElement = new JsonObject();
                arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_NAME, entry.getKey());
                JsonObject valueObject = new JsonObject();

                switch (entry.getKey()) {
                    case DAPConstants.HTTP_SERVLET_REQUEST:
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_TYPE, DAPConstants.VARIABLE_TYPE_OBJECT);
                        HashMap<String, Object> requestDetails = (HashMap<String, Object>) entry.getValue();
                        valueObject.add(DAPConstants.JSON_KEY_FOR_COOKIES, this.getCookies(requestDetails));
                        valueObject.add(DAPConstants.JSON_KEY_FOR_HEADERS, this.getHeaders(requestDetails));
                        arrayElement.add(DAPConstants.JSON_KEY_FOR_VALUE, valueObject);
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, 0);

                        break;
                    case DAPConstants.HTTP_SERVLET_RESPONSE:
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_TYPE, DAPConstants.VARIABLE_TYPE_OBJECT);
                        HashMap<String, Object> responseDetails = (HashMap<String, Object>) entry.getValue();
                        valueObject.add(DAPConstants.JSON_KEY_FOR_HEADERS, this.getHeaders(responseDetails));
                        valueObject.addProperty(DAPConstants.JSON_KEY_FOR_STATUS,
                                this.getResponseStatus(responseDetails));
                        arrayElement.add(DAPConstants.JSON_KEY_FOR_VALUE, valueObject);
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, 0);

                        break;
                    case DAPConstants.SAML_REQUEST:
                    case DAPConstants.SAML_RESPONSE:
                    case DAPConstants.OIDC_AUTHZ_RESPONSE:
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_TYPE, DAPConstants.VARIABLE_TYPE_STRING);
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VALUE, (String) entry.getValue());
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, 0);

                        break;

                    case DAPConstants.OIDC_AUTHZ_REQUEST:
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_TYPE, DAPConstants.VARIABLE_TYPE_OBJECT);
                        HashMap<String, Object> authzRequestDetails = (HashMap<String, Object>) entry.getValue();
                        arrayElement.add(DAPConstants.JSON_KEY_FOR_VALUE, getOAuthMessage(authzRequestDetails));
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, 0);

                        break;

                    case DAPConstants.OIDC_TOKEN_REQUEST:
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_TYPE, DAPConstants.VARIABLE_TYPE_OBJECT);
                        HashMap<String, Object> tokenRequestDetails = (HashMap<String, Object>) entry.getValue();
                        JsonObject values = new JsonObject();
                        values.add(DAPConstants.JSON_KEY_FOR_REQUEST_PARAMETER,
                                getOIDCTokenRequestParameters(tokenRequestDetails));
                        values.add(DAPConstants.JSON_KEY_FOR_HTTP_REQUEST_HEADERS,
                                getOIDCTokenRequestHeader(tokenRequestDetails));
                        values.add(DAPConstants.CLIENT_AUTHN_CONTEXT,
                                getOAuthClientAuthnContext(tokenRequestDetails));
                        arrayElement.add(DAPConstants.JSON_KEY_FOR_VALUE, values);
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, 0);

                        break;

                    case DAPConstants.OIDC_TOKEN_RESPONSE:
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_TYPE, DAPConstants.VARIABLE_TYPE_OBJECT);
                        HashMap<String, Object> tokenResponseDetails = (HashMap<String, Object>) entry.getValue();
                        arrayElement.add(DAPConstants.JSON_KEY_FOR_VALUE, getTokenResponse(tokenResponseDetails));
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, 0);

                        break;

                    default:
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_TYPE, DAPConstants.VARIABLE_TYPE_UNKNOWN);
                        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, 0);
                        break;
                }
                jsonArray.add(arrayElement);
            }
        }

        return jsonArray;
    }

    private Integer getResponseStatus(HashMap<String, Object> responseDetails) {

        return (Integer) responseDetails.get(DAPConstants.JSON_KEY_FOR_STATUS);
    }

    private JsonArray getCookies(HashMap<String, Object> requestDetails) {

        Object object = requestDetails.get(DAPConstants.JSON_KEY_FOR_COOKIES);
        JsonArray cookieJsonArray = new JsonArray();
        if (object != null) {
            Cookie[] cookies = (Cookie[]) object;
            for (Cookie cookie : cookies) {
                JsonObject valueObject = new JsonObject();
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_NAME, cookie.getName());
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_VALUE, cookie.getValue());
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_VERSION, cookie.getVersion());
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_SECURE, cookie.getSecure());
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_PATH, cookie.getPath());
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_MAXAGE, cookie.getMaxAge());
                valueObject.addProperty(DAPConstants.JSON_KEY_FOR_DOMAIN, cookie.getDomain());
                cookieJsonArray.add(valueObject);
            }
        }
        return cookieJsonArray;
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

    private int getVariablesReference(HashMap<String, Object> requestdetails) {

        return (int) requestdetails.get(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE);
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

    public String getClientId(HttpServletRequest request) {

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

    private JsonObject getTokenResponse(HashMap<String, Object> tokenResponseDetails) {

        JsonObject valueObject = new JsonObject();
        for (Map.Entry<String, Object> tokenResponseDetail : tokenResponseDetails.entrySet()) {
            if (tokenResponseDetail.getValue() instanceof String) {
                valueObject.addProperty(tokenResponseDetail.getKey(),
                        (String) tokenResponseDetail.getValue());
                if (StringUtils.equals(tokenResponseDetail.getKey(), DAPConstants.ID_TOKEN)) {
                    try {
                        DecodedJWT jwt = JWT.decode((String) tokenResponseDetail.getValue());

                        JsonObject jwtObject = new JsonObject();
                        jwtObject.addProperty(DAPConstants.JWT_HEADER, org.apache.commons.codec.binary.
                                StringUtils.newStringUtf8(Base64.decodeBase64(jwt.getHeader())));
                        jwtObject.addProperty(DAPConstants.JWT_PAYLOAD, org.apache.commons.codec.binary.
                                StringUtils.newStringUtf8(Base64.decodeBase64(jwt.getPayload())));
                        valueObject.add(DAPConstants.ID_TOKEN_DECODED, jwtObject);
                    } catch (JWTDecodeException ex) {
                        log.error("Error Decoding the Jwt: InvalidJWT token", ex);
                    }
                }
            } else if (tokenResponseDetail.getValue() instanceof Boolean) {
                valueObject.addProperty(tokenResponseDetail.getKey(),
                        (Boolean) tokenResponseDetail.getValue());
            } else if (tokenResponseDetail.getValue() instanceof Long) {
                valueObject.addProperty(tokenResponseDetail.getKey(),
                        (Long) tokenResponseDetail.getValue());
            }
        }
        return valueObject;
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
