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

package org.springframework.cloud.stream.metrics.collector;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @author Vinicius Carvalho
 */
@Component
public class MetricsAggregator {


	private Map<String,Set<String>> groupIndex = new ConcurrentHashMap<>();

	private Map<String,ApplicationMetrics> groupedMetrics = new ConcurrentHashMap<>();

	@StreamListener(Collector.METRICS_CHANNEL_NAME)
	public void receive(ApplicationMetrics metrics) {
		String appMetrics = metrics.getName()+"_"+metrics.getId();
		groupedMetrics.put(appMetrics,metrics);
		Set<String> metricsGroup = groupIndex.get(metrics.getName());
		if(metricsGroup == null){
			metricsGroup = new HashSet<>();
			groupIndex.put(metrics.getName(),metricsGroup);
		}
		metricsGroup.add(appMetrics);
		groupedMetrics.put(appMetrics,metrics);
	}

	public Collection<ApplicationMetrics> findAll(){
		return groupedMetrics.values();
	}

	public Collection<ApplicationMetrics> findByAppName(String appName ){
		Set<String> appIndexes = groupIndex.get(appName);
		if(appIndexes == null){
			return Collections.emptyList();
		}
		Set<ApplicationMetrics> metrics = new HashSet<>();
		for(String key : appIndexes){
			metrics.add(groupedMetrics.get(key));
		}
		return metrics;
	}

}
