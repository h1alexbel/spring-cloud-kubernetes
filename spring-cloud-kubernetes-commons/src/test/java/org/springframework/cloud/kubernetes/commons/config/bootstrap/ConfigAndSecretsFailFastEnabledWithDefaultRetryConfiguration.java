/*
 * Copyright 2013-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.kubernetes.commons.config.bootstrap;

import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.kubernetes.commons.config.App;
import org.springframework.cloud.kubernetes.commons.config.ConfigMapConfigProperties;
import org.springframework.cloud.kubernetes.commons.config.RetryProperties;
import org.springframework.cloud.kubernetes.commons.config.SecretsConfigProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Isik Erhan
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = App.class,
		properties = { "spring.cloud.kubernetes.config.fail-fast=true",
				"spring.cloud.kubernetes.secrets.fail-fast=true", "spring.main.cloud-platform=KUBERNETES" })
public class ConfigAndSecretsFailFastEnabledWithDefaultRetryConfiguration {

	@Autowired
	private ConfigurableApplicationContext context;

	@Autowired
	private ConfigMapConfigProperties configMapConfigProperties;

	@Autowired
	private SecretsConfigProperties secretsConfigProperties;

	@Test
	void shouldDefineRequiredBeans() {
		Map<String, RetryOperationsInterceptor> retryInterceptors = context
				.getBeansOfType(RetryOperationsInterceptor.class);
		assertThat(retryInterceptors.containsKey("kubernetesConfigRetryInterceptor")).isTrue();
		assertThat(retryInterceptors.containsKey("kubernetesSecretsRetryInterceptor")).isTrue();
	}

	@Test
	void retryConfigurationShouldBeDefault() {
		RetryProperties defaultRetryProperties = RetryProperties.DEFAULT;
		RetryProperties configMapRetryProperties = configMapConfigProperties.retry();

		assertThat(configMapRetryProperties.maxAttempts()).isEqualTo(defaultRetryProperties.maxAttempts());
		assertThat(configMapRetryProperties.initialInterval()).isEqualTo(defaultRetryProperties.initialInterval());
		assertThat(configMapRetryProperties.maxInterval()).isEqualTo(defaultRetryProperties.maxInterval());
		assertThat(configMapRetryProperties.multiplier()).isEqualTo(defaultRetryProperties.multiplier());

		RetryProperties secretsRetryProperties = secretsConfigProperties.retry();

		assertThat(secretsRetryProperties.maxAttempts()).isEqualTo(defaultRetryProperties.maxAttempts());
		assertThat(secretsRetryProperties.initialInterval()).isEqualTo(defaultRetryProperties.initialInterval());
		assertThat(secretsRetryProperties.maxInterval()).isEqualTo(defaultRetryProperties.maxInterval());
		assertThat(secretsRetryProperties.multiplier()).isEqualTo(defaultRetryProperties.multiplier());
	}

}
