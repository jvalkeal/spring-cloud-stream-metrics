/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.stream.metrics.exporter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Vinicius Carvalho
 */
@ConfigurationProperties(prefix = "spring.cloud.stream.metrics")
public class StreamMetricsProperties {


	@Value("${vcap.application.instance_index:${spring.application.index:${PID}}}")
	public  String id;

	@Value("${spring.cloud.application.group:group}.${spring.application.name:application}")
	private String prefix;

	private String[] includes;

	private String[] excludes;

	private Long delayMillis;

	public String[] getIncludes() {
		return includes;
	}

	public void setIncludes(String[] includes) {
		this.includes = includes;
	}

	public String[] getExcludes() {
		return excludes;
	}

	public void setExcludes(String[] excludes) {
		this.excludes = excludes;
	}

	public Long getDelayMillis() {
		return delayMillis;
	}

	public void setDelayMillis(Long delayMillis) {
		this.delayMillis = delayMillis;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getId() {
		return id;
	}
}
