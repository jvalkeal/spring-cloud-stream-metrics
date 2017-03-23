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

package org.springframework.cloud.stream;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Vinicius Carvalho
 */
public class ElasticSearchData {
	private String name;
	private String group;
	private int instanceIndex;
	@JsonProperty("@timestamp")
	private long timestamp;
	private Map<String,Object> metrics = new HashMap<>();

	ElasticSearchData(){}

	public ElasticSearchData(String name, String group, int instanceIndex, long timestamp) {
		this.name = name;
		this.group = group;
		this.instanceIndex = instanceIndex;
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getInstanceIndex() {
		return instanceIndex;
	}

	public void setInstanceIndex(int instanceIndex) {
		this.instanceIndex = instanceIndex;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@JsonAnyGetter
	public Map<String, Object> getMetrics() {
		return metrics;
	}

	@JsonAnySetter
	public void setMetric(String name, Object value){
		this.getMetrics().put(name,value);
	}

	public void setMetrics(Map<String, Object> metrics) {
		this.metrics = metrics;
	}
}
