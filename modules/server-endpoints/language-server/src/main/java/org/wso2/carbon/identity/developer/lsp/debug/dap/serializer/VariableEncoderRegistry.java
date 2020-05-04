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

package org.wso2.carbon.identity.developer.lsp.debug.dap.serializer;

import org.wso2.carbon.identity.developer.lsp.debug.DAPConstants;
import org.wso2.carbon.identity.developer.lsp.debug.dap.serializer.encoders.HttpServletRequestEncoder;
import org.wso2.carbon.identity.developer.lsp.debug.dap.serializer.encoders.HttpServletResponseEncoder;
import org.wso2.carbon.identity.developer.lsp.debug.dap.serializer.encoders.OIDCAuthzRequestEncoder;
import org.wso2.carbon.identity.developer.lsp.debug.dap.serializer.encoders.OIDCTokenRequestEncoder;
import org.wso2.carbon.identity.developer.lsp.debug.dap.serializer.encoders.OIDCTokenResponseEncoder;
import org.wso2.carbon.identity.developer.lsp.debug.dap.serializer.encoders.SAMLRequestEncoder;
import org.wso2.carbon.identity.developer.lsp.debug.dap.serializer.encoders.SAMLResponseEncoder;
import org.wso2.carbon.identity.developer.lsp.debug.dap.serializer.encoders.VariableEncoder;

import java.util.HashMap;

/**
 * Registry to get the relevant Encoders for Translated Variables.
 */
public class VariableEncoderRegistry {

    private HashMap<String, VariableEncoder> registry;

    public VariableEncoderRegistry() {

        this.registry = new HashMap<>();
        addConfig();
    }

    private void addConfig() {

        registry.put(DAPConstants.HTTP_SERVLET_REQUEST, new HttpServletRequestEncoder());
        registry.put(DAPConstants.HTTP_SERVLET_RESPONSE, new HttpServletResponseEncoder());
        registry.put(DAPConstants.SAML_REQUEST, new SAMLRequestEncoder());
        registry.put(DAPConstants.SAML_RESPONSE, new SAMLResponseEncoder());
        registry.put(DAPConstants.OIDC_AUTHZ_REQUEST, new OIDCAuthzRequestEncoder());
        registry.put(DAPConstants.OIDC_AUTHZ_RESPONSE, new HttpServletRequestEncoder());
        registry.put(DAPConstants.OIDC_TOKEN_REQUEST, new OIDCTokenRequestEncoder());
        registry.put(DAPConstants.OIDC_TOKEN_RESPONSE, new OIDCTokenResponseEncoder());
    }

    /**
     * This method is to get the Variable Encoder for the relevant translated variable.
     *
     * @param key The name of translated variable.
     * @return The corresponding Variable Encoder.
     */
    public VariableEncoder getVariablesEncoder(String key) {

        return registry.get(key);
    }
}
