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

/**
 * Encode the translated the SAMLResponse variable.
 */
public class SAMLResponseEncoder implements VariableEncoder {

    @Override
    public JsonObject translate(Object samlResponseTranslated) {

        JsonObject arrayElement = new JsonObject();
        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_TYPE, DAPConstants.VARIABLE_TYPE_STRING);
        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VALUE, (String) samlResponseTranslated);
        arrayElement.addProperty(DAPConstants.JSON_KEY_FOR_VARIABLE_REFERENCE, 0);
        return arrayElement;
    }
}
