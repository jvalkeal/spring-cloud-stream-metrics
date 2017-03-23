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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.metrics.GroupedMetrics;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vinicius Carvalho
 */
@Component
@RestController
public class MetricsAggregator {


	private Map<String,GroupedMetrics> groupedMetrics = new ConcurrentHashMap<>();
	private Map<String,Set<String>> groupIndex = new ConcurrentHashMap<>();


	@StreamListener(Sink.INPUT)
	public void receive(ApplicationMetrics metrics) {
		GroupedMetrics group = groupedMetrics.get(metrics.getName());
		if(group == null){
			group = new GroupedMetrics();
			group.setName(metrics.getName());
			group.getInstances().add(metrics);
			groupedMetrics.put(metrics.getName(),group);
		}else{
			group.getInstances().remove(metrics);
			group.getInstances().add(metrics);
		}
	}

	@RequestMapping(value = "/collector/metrics", method = RequestMethod.GET)
	public Collection<GroupedMetrics> findAll(){
		return groupedMetrics.values();
	}

}
