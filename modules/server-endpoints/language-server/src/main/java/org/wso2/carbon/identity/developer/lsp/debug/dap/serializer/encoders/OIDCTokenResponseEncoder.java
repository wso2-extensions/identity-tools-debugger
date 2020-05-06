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
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.developer.lsp.debug.DAPConstants;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Encode the translated the OIDCTokenResponse variable.
 */
public class OIDCTokenResponseEncoder implements VariableEncoder {

    private static final Log log = LogFactory.getLog(OIDCTokenResponseEncoder.class);
    private static final int JWT_HEADER_INDEX = 0;
    private static final int JWT_PAYLOAD_INDEX = 1;
    private static final int JWT_PARTS = 3;

    @Override
    public JsonObject translate(Object oidcTokenResponseTranslated) {

        JsonObject arrayElement = new JsonObject();
        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_TYPE, DAPConstants.VARIABLE_TYPE_OBJECT);
        HashMap<String, Object> tokenResponseDetails = (HashMap<String, Object>) oidcTokenResponseTranslated;
        arrayElement.add(DAPConstants.JSON_KEY_FOR_VALUE, getTokenResponse(tokenResponseDetails));
        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, 0);
        return arrayElement;
    }

    private JsonObject getTokenResponse(HashMap<String, Object> tokenResponseDetails) {

        JsonObject valueObject = new JsonObject();
        for (Map.Entry<String, Object> tokenResponseDetail : tokenResponseDetails.entrySet()) {
            if (tokenResponseDetail.getValue() instanceof String) {
                valueObject.addProperty(tokenResponseDetail.getKey(),
                        (String) tokenResponseDetail.getValue());
                if (StringUtils.equals(tokenResponseDetail.getKey(), DAPConstants.ID_TOKEN)) {
                    final String[] jwtParts = ((String) tokenResponseDetail.getValue()).split("\\.");
                    JsonObject jwtObject = new JsonObject();
                    try {
                        if (jwtParts.length == JWT_PARTS) {
                            jwtObject.addProperty(DAPConstants.JWT_HEADER, org.apache.commons.codec.binary.
                                    StringUtils.newStringUtf8(Base64.decodeBase64(jwtParts[JWT_HEADER_INDEX])));
                            jwtObject.addProperty(DAPConstants.JWT_PAYLOAD, org.apache.commons.codec.binary.
                                    StringUtils.newStringUtf8(Base64.decodeBase64(jwtParts[JWT_PAYLOAD_INDEX])));
                            valueObject.add(DAPConstants.ID_TOKEN_DECODED, jwtObject);
                        } else {
                            throw new ParseException("Error Decoding the Jwt: InvalidJWT token", 0);
                        }
                    } catch (ParseException ex) {
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
}
