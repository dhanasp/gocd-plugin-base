/*
 * Copyright 2019 ThoughtWorks, Inc.
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

package com.github.bdpiparva.plugin.base.dispatcher.secrets;

import com.github.bdpiparva.plugin.base.dispatcher.RequestDispatcher;
import com.github.bdpiparva.plugin.base.executors.secrets.LookupExecutor;
import com.github.bdpiparva.plugin.base.validation.ValidationResult;
import com.github.bdpiparva.plugin.base.validation.Validator;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static com.github.bdpiparva.plugin.base.DispatcherBuilder.REQUEST_GET_ICON;
import static com.github.bdpiparva.plugin.base.dispatcher.secrets.SecretsBuilderV1.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class SecretsBuilderV1Test {
    @Mock
    private GoPluginApiRequest request;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void shouldSupportGetIconCall() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(REQUEST_GET_ICON);
        final RequestDispatcher dispatcher = new SecretsBuilderV1()
                .icon("/gocd.png", "image/png")
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
    }

    @Test
    void shouldSupportGetSecretConfigMetadata() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(REQUEST_GET_CONFIG_METADATA);
        final RequestDispatcher dispatcher = new SecretsBuilderV1()
                .configMetadata(SecretConfig.class)
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
    }

    @Test
    void shouldSupportGetSecretConfigView() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(REQUEST_GET_CONFIG_VIEW);
        final RequestDispatcher dispatcher = new SecretsBuilderV1()
                .configView("/dummy-template.html")
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
    }

    @Test
    void shouldSupportValidateSecretConfig() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(REQUEST_VALIDATE_CONFIG);
        final Validator validator = mock(Validator.class);
        when(validator.validate(any())).thenReturn(new ValidationResult());

        final RequestDispatcher dispatcher = new SecretsBuilderV1()
                .validateSecretConfig(validator)
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        verify(validator).validate(any());
    }

    @Test
    void shouldSupportLookupSecretConfig() throws UnhandledRequestTypeException {
        when(request.requestName()).thenReturn(REQUEST_SECRETS_LOOKUP);

        final RequestDispatcher dispatcher = new SecretsBuilderV1()
                .lookup(new DummyLookupExecutor())
                .build();

        final GoPluginApiResponse response = dispatcher.dispatch(request);

        assertThat(response.responseCode()).isEqualTo(200);
        assertThat(response.responseBody()).isEqualTo("result");
    }
}
class DummyLookupExecutor extends LookupExecutor<String> {

    @Override
    protected GoPluginApiResponse execute(String request) {
        return DefaultGoPluginApiResponse.success("result");
    }

    @Override
    protected String parseRequest(String body) {
        return body;
    }
}
class SecretConfig {
}