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

package org.wso2.carbon.identity.developer.lsp.debug.runtime;

import org.wso2.carbon.identity.developer.lsp.debug.DAPConstants;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.builders.OIDCAuthzRequestVariableBuilder;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.builders.OIDCAuthzResponseVariableBuilder;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.builders.OIDCTokenRequestVariableBuilder;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.builders.OIDCTokenResponseVariableBuilder;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.builders.SAMLEntryVariableBuilder;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.builders.SAMLExitVariableBuilder;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.builders.VariableBuilder;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.translators.HttpServletRequestTranslator;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.translators.HttpServletResponseTranslator;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.translators.OIDCAuthzRequestTranslator;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.translators.OIDCAuthzResponseTranslator;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.translators.OIDCTokenRequestTranslator;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.translators.OIDCTokenResponseTranslator;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.translators.SAMLRequestTranslator;
import org.wso2.carbon.identity.developer.lsp.debug.runtime.translators.SAMLResponseTranslator;
import org.wso2.carbon.identity.java.agent.host.MethodContext;

import java.util.HashMap;

/**
 * Registry to get the relevant Translator for the Method Context.
 * This class is used to get the corresponding Builder for the Method Context and Get relevant Translators for the
 * arguments.
 */
public class VariableTranslateRegistry {

    private HashMap<String, VariableBuilder> registry = new HashMap<>();

    public VariableTranslateRegistry() {

        readConfig();
    }

    private void readConfig() {

        addSAMLConfig();
        addOIDCConfig();

    }

    private void addSAMLConfig() {

        String samlEntryKey = getKeyFromContext(DAPConstants.SAML_ENTRY_CLASS, DAPConstants.SAML_ENTRY_METHOD,
                DAPConstants.SAML_ENTRY_SIGNATURE);

        String samlExitKey = getKeyFromContext(DAPConstants.SAML_EXIT_CLASS, DAPConstants.SAML_EXIT_METHOD,
                DAPConstants.SAML_EXIT_SIGNATURE);

        registry.put(samlEntryKey, new SAMLEntryVariableBuilder(this));
        registry.put(samlExitKey, new SAMLExitVariableBuilder(this));
    }

    private void addOIDCConfig() {

        String oidcAuthzRequestKey = getKeyFromContext(DAPConstants.OIDC_AUTHZ_CLASS,
                DAPConstants.OIDC_AUTHZ_REQUEST_METHOD,
                DAPConstants.OIDC_AUTHZ_REQUEST_SIGNATURE);
        String oidcAuthzResponseKey = getKeyFromContext(DAPConstants.OIDC_AUTHZ_CLASS,
                DAPConstants.OIDC_AUTHZ_RESPONSE_METHOD,
                DAPConstants.OIDC_AUTHZ_RESPONSE_SIGNATURE);
        String oidcTokenRequestKey = getKeyFromContext(DAPConstants.OIDC_TOKEN_CLASS,
                DAPConstants.OIDC_TOKEN_REQUEST_METHOD,
                DAPConstants.OIDC_TOKEN_REQUEST_SIGNATURE);
        String oidcTokenResponseKey = getKeyFromContext(DAPConstants.OIDC_TOKEN_CLASS,
                DAPConstants.OIDC_TOKEN_RESPONSE_METHOD,
                DAPConstants.OIDC_TOKEN_RESPONSE_SIGNATURE);

        registry.put(oidcAuthzRequestKey, new OIDCAuthzRequestVariableBuilder(this));
        registry.put(oidcAuthzResponseKey, new OIDCAuthzResponseVariableBuilder(this));
        registry.put(oidcTokenRequestKey, new OIDCTokenRequestVariableBuilder(this));
        registry.put(oidcTokenResponseKey, new OIDCTokenResponseVariableBuilder(this));
    }

    /**
     * This method is to get the Variable Builder using MethodContext.
     *
     * @param methodContext Holds the intercepted method context.
     * @return The corresponding Variable Builder.
     */
    public VariableBuilder getVariablesBuilder(MethodContext methodContext) {

        return registry.get(getKeyFromContext(methodContext.getClassName(), methodContext.getMethodName(),
                methodContext.getMethodSignature()));
    }

    /**
     * This method is to generate the key for the method Signature.
     *
     * @param className       Intercepted class name.
     * @param methodName      Intercepted Method name.
     * @param methodSignature Intercepted Method Signature.
     * @return Key for the method context.
     */
    private String getKeyFromContext(String className, String methodName, String methodSignature) {

        return className + "#" + methodName + "#" + methodSignature;
    }

    /**
     * This method is to  translate the argument through reusable HttpServletRequestTranslator.
     *
     * @param argument           Which holds all the arguments from the intercepted method.
     * @param variablesReference Variable Reference number send from the extension.
     * @return The translated object from HttpRequest.
     */
    public Object translateHttpRequest(Object argument, int variablesReference) {

        return HttpServletRequestTranslator.getInstance().translate(argument, variablesReference);
    }

    /**
     * This method is to  translate the argument through reusable SAMLRequestTranslator.
     *
     * @param argument           Which holds all the arguments from the intercepted method.
     * @param variablesReference Variable Reference number send from the extension.
     * @return The translated object from SAMLRequest.
     */
    public Object translateSAMLRequest(Object argument, int variablesReference) {

        return SAMLRequestTranslator.getInstance().translate(argument, variablesReference);
    }

    /**
     * This method is to  translate the argument through reusable OIDCAuthzRequestTranslator.
     *
     * @param argument           Which holds all the arguments from the intercepted method.
     * @param variablesReference Variable Reference number send from the extension.
     * @return The translated object from SAMLResponse.
     */
    public Object translateOIDCAuthzRequest(Object argument, int variablesReference) {

        return OIDCAuthzRequestTranslator.getInstance().translate(argument, variablesReference);
    }

    /**
     * This method is to  translate the argument through reusable OIDCAuthzResponseTranslator.
     *
     * @param argument           Which holds all the arguments from the intercepted method.
     * @param variablesReference Variable Reference number send from the extension.
     * @return The translated object from OIDCAuthzResponse.
     */
    public Object translateOIDCAuthzResponse(Object argument, int variablesReference) {

        return OIDCAuthzResponseTranslator.getInstance().translate(argument, variablesReference);
    }

    /**
     * This method is to  translate the argument through reusable OIDCTokenRequestTranslator.
     *
     * @param argument           Which holds all the arguments from the intercepted method.
     * @param variablesReference Variable Reference number send from the extension.
     * @return The translated object from OIDCTokenRequest.
     */
    public Object translateOIDCTokenRequest(Object argument, int variablesReference) {

        return OIDCTokenRequestTranslator.getInstance().translate(argument, variablesReference);
    }
    /**
     * This method is to translate the argument through reusable OIDCTokenResponseTranslator.
     *
     * @param argument           Which holds all the arguments from the intercepted method.
     * @param variablesReference Variable Reference number send from the extension.
     * @return The translated object from OIDCTokenResponse.
     */
    public Object translateOIDCTokenResponse(Object argument, int variablesReference) {

        return OIDCTokenResponseTranslator.getInstance().translate(argument, variablesReference);
    }

    /**
     * This method is to  translate the argument through reusable HttpServletResponseTranslator.
     *
     * @param argument           Which holds all the arguments from the intercepted method.
     * @param variablesReference Variable Reference number send from the extension.
     * @return The translated object from HttpResponse.
     */
    public Object translateHttpResponse(Object argument, int variablesReference) {

        return HttpServletResponseTranslator.getInstance().translate(argument, variablesReference);
    }

    /**
     * This method is to  translate the argument through reusable SAMLResponseTranslator.
     *
     * @param argument           Which holds all the arguments from the intercepted method.
     * @param variablesReference Variable Reference number send from the extension.
     * @return The translated object from SAMLResponse.
     */
    public Object translateSAMLResponse(Object argument, int variablesReference) {

        return SAMLResponseTranslator.getInstance().translate(argument, variablesReference);
    }
}
