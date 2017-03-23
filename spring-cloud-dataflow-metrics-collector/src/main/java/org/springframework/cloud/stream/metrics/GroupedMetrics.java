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

package org.springframework.cloud.stream.metrics;

import java.util.HashSet;
import java.util.Set;

import org.springframework.cloud.stream.metrics.collector.ApplicationMetrics;

/**
 * @author Vinicius Carvalho
 */
public class GroupedMetrics {
	private String name;
	private Set<ApplicationMetrics> instances = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ApplicationMetrics> getInstances() {
		return instances;
	}

	public void setInstances(Set<ApplicationMetrics> instances) {
		this.instances = instances;
	}
}
