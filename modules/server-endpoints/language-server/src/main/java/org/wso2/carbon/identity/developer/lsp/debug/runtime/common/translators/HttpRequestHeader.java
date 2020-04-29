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

import java.io.Serializable;

/**
 * This class is used to store http request header information.
 */
public class HttpRequestHeader implements Serializable {

    private static final long serialVersionUID = 5419655486789962879L;

    private String name;
    private String[] values;

    /**
     * Instantiate a HTTPHeader object for the given name and values.
     *
     * @param name   Header name.
     * @param values Parameter values.
     */
    public HttpRequestHeader(String name, String... values) {

        this.name = name;
        this.values = values;
    }

    /**
     * Returns the Header name.
     *
     * @return Header name.
     */
    public String getName() {

        return name;
    }

    /**
     * Returns the header value.
     *
     * @return Header value.
     */
    public String[] getValue() {

        return values;
    }

}
