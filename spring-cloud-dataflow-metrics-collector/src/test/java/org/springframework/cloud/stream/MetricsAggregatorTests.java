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

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.cloud.stream.metrics.collector.ApplicationMetrics;
import org.springframework.cloud.stream.metrics.collector.MetricsAggregator;

/**
 * @author Vinicius Carvalho
 */
public class MetricsAggregatorTests {

	@Test
	public void testAggregation() throws Exception{

		ApplicationMetrics foo1 = new ApplicationMetrics("foo","1", Collections.emptyList());
		ApplicationMetrics foo2 = new ApplicationMetrics("foo","2",Collections.emptyList());
		ApplicationMetrics bar1 = new ApplicationMetrics("bar","1",Collections.emptyList());

		MetricsAggregator aggregator = new MetricsAggregator();

		aggregator.receive(bar1);
		aggregator.receive(foo2);
		aggregator.receive(foo2);
		aggregator.receive(foo1);

		Assert.assertEquals(3,aggregator.findAll().size());
		Assert.assertEquals(2,aggregator.findByAppName("foo").size());
		Assert.assertEquals(1,aggregator.findByAppName("bar").size());
		Assert.assertEquals(0,aggregator.findByAppName("baz").size());
	}

}
