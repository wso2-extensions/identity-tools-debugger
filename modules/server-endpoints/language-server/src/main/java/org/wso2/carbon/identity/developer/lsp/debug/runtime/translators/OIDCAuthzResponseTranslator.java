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
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.developer.lsp.debug.DAPConstants;
import org.wso2.carbon.identity.sso.saml.util.SAMLSSOUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Translator to translate the  OIDC Response arguments.
 */
public class OIDCAuthzResponseTranslator implements VariableTranslator {

    private static Log log = LogFactory.getLog(OIDCAuthzResponseTranslator.class);

    private OIDCAuthzResponseTranslator() {

    }

    private static class OIDCAuthzRequestTranslatorHolder {

        private static final OIDCAuthzResponseTranslator INSTANCE = new OIDCAuthzResponseTranslator();
    }

    /**
     * This static method allow to get the instance of the OIDCResponseTranslator.
     *
     * @return The OIDCAuthzRequestTranslatorHolder instance.
     */
    public static OIDCAuthzResponseTranslator getInstance() {

        return OIDCAuthzRequestTranslatorHolder.INSTANCE;
    }

    @Override
    public Object translate(Object object, int variablesReference) {

        if (object instanceof String) {
            String redirectURI = (String) object;
            return redirectURI;
        }
        return "No OIDC Response Redirect URL Added";
    }
}
