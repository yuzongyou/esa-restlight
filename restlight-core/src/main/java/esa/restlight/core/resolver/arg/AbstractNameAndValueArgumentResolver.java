/*
 * Copyright 2020 OPPO ESA Stack Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package esa.restlight.core.resolver.arg;

import esa.commons.ObjectUtils;
import esa.commons.StringUtils;
import esa.httpserver.core.AsyncRequest;
import esa.httpserver.core.AsyncResponse;
import esa.restlight.core.method.Param;
import esa.restlight.core.resolver.ArgumentResolver;
import esa.restlight.core.util.ConverterUtils;
import esa.restlight.server.bootstrap.WebServerException;

import java.util.function.Function;

public abstract class AbstractNameAndValueArgumentResolver implements ArgumentResolver {

    protected final Param param;
    protected final NameAndValue nav;

    public AbstractNameAndValueArgumentResolver(Param param) {
        this.param = param;
        this.nav = getNameAndValue(param);
    }

    public AbstractNameAndValueArgumentResolver(Param param, NameAndValue nav) {
        this.param = param;
        this.nav = updateNamedValueInfo(param, nav);
    }

    @Override
    public Object resolve(AsyncRequest request, AsyncResponse response) throws Exception {
        Object arg = this.resolveName(nav.name, request);
        if (isArgumentMissing(arg)) {
            if (nav.defaultValue == null && nav.required) {
                throw WebServerException.badRequest("Missing required value: " + nav.name);
            } else {
                arg = nav.defaultValue;
            }
        }
        return arg;
    }

    protected boolean isArgumentMissing(Object arg) {
        return arg == null || (arg instanceof String && ((String) arg).length() == 0);
    }

    protected NameAndValue getNameAndValue(Param param) {
        NameAndValue nav = createNameAndValue(param);
        nav = updateNamedValueInfo(param, nav);
        return nav;
    }

    /**
     * Create an instance of {@link NameAndValue} for the parameter.
     *
     * @param param parameter
     *
     * @return name and value
     */
    protected abstract NameAndValue createNameAndValue(Param param);

    protected boolean useObjectDefaultValueIfRequired(Param param, NameAndValue info) {
        return !param.isFieldParam();
    }

    /**
     * Try to resolve the value by the given name from the {@link AsyncRequest}
     *
     * @param name    name
     * @param request request
     *
     * @return resolved value
     * @throws Exception occurred
     */
    protected abstract Object resolveName(String name, AsyncRequest request) throws Exception;

    private NameAndValue updateNamedValueInfo(Param param, NameAndValue nav) {
        String name = nav.name;
        if (nav.name.isEmpty()) {
            name = param.name();
            if (name == null) {
                throw new IllegalArgumentException(
                        "Name for argument type [" + param.type().getName() +
                                "] not available, and parameter name information not found in class file either.");
            }
        }
        Object defaultValue = null;
        if (nav.defaultValue != null) {
            defaultValue = nav.defaultValue;
        } else if (!nav.required && (useObjectDefaultValueIfRequired(param, nav))) {
            defaultValue = ObjectUtils.defaultValue(param.type());
        }
        if (defaultValue instanceof String && !param.type().isInstance(defaultValue)) {
            defaultValue = ConverterUtils.forceConvertStringValue((String) defaultValue, param.genericType());
        }
        return new NameAndValue(name, nav.required, defaultValue);
    }
}
