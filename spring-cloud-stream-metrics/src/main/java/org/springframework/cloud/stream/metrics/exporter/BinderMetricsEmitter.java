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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.MetricsEndpoint;
import org.springframework.boot.actuate.endpoint.MetricsEndpointMetricReader;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.export.MetricCopyExporter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PatternMatchUtils;

/**
 * @author Vinicius Carvalho
 */
public class BinderMetricsEmitter {

	@Autowired
	private Metrics output;

	@Autowired
	private StreamMetricsProperties properties;

	private MetricsEndpointMetricReader metricsReader;

	public BinderMetricsEmitter(MetricsEndpoint endpoint){
		this.metricsReader = new MetricsEndpointMetricReader(endpoint);
	}

	@Scheduled(fixedRateString = "${spring.cloud.stream.metrics.delay-millis:5000}")
	public void sendMetrics(){
		ApplicationMetrics appMetrics = new ApplicationMetrics(this.properties.getPrefix(),this.properties.getId(),filter());
		output.output().send(MessageBuilder.withPayload(appMetrics).build());
	}

	/**
	 * Shameless copied from {@link MetricCopyExporter}
	 * @return
	 */
	protected Collection<Map<String, Number>> filter(){
		Collection<Map<String, Number>> result = new ArrayList<>();
		Iterable<Metric<?>> metrics = metricsReader.findAll();
		for(Metric metric : metrics){
			if(isMatch(metric)){
				result.add(Collections.singletonMap(metric.getName(),metric.getValue()));
			}
		}
		return result;
	}

	/**
	 * Shameless copied from {@link MetricCopyExporter}
	 * @return
	 */
	private boolean isMatch(Metric<?> metric) {
		String[] includes = this.properties.getIncludes();
		String[] excludes = this.properties.getExcludes();
		String name = metric.getName();
		if (ObjectUtils.isEmpty(includes)
				|| PatternMatchUtils.simpleMatch(includes, name)) {
			return !PatternMatchUtils.simpleMatch(excludes, name);
		}
		return false;
	}


}
