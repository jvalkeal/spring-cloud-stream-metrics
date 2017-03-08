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

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.metrics.exporter.ApplicationMetrics;
import org.springframework.cloud.stream.metrics.exporter.Metrics;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;

/**
 * @author Vinicius Carvalho
 */
public class BinderMetricsTests {


	@Test(expected = NoSuchBeanDefinitionException.class)
	public void checkDisabledConfiguration() throws Exception {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(BinderExporterApplication.class,
				"--server.port=0",
				"--spring.jmx.enabled=false",
				"--spring.cloud.stream.metrics.delay-millis=500"
		);
		try {
			applicationContext.getBean(Metrics.class);
		} catch (Exception e){
			throw e;
		}
		finally {
			applicationContext.close();
		}

	}

	@Test
	public void testDefaultIncludes() throws Exception {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(BinderExporterApplication.class,
				"--server.port=0",
				"--spring.jmx.enabled=false",
				"--spring.cloud.stream.metrics.delay-millis=500",
				"--spring.cloud.stream.bindings.streamMetrics.destination=foo");
		Metrics metricsSource = applicationContext.getBean(Metrics.class);
		MessageCollector collector = applicationContext.getBean(MessageCollector.class);
		Message message = collector.forChannel(metricsSource.output()).poll(1000, TimeUnit.MILLISECONDS);
		Assert.assertNotNull(message);
		ObjectMapper mapper = applicationContext.getBean(ObjectMapper.class);
		ApplicationMetrics applicationMetrics = mapper.readValue((String)message.getPayload(),ApplicationMetrics.class);
		Assert.assertTrue(contains("integration.channel.errorChannel.errorRate.mean", applicationMetrics.getMetrics()));
		Assert.assertFalse(contains("mem",applicationMetrics.getMetrics()));
		applicationContext.close();
	}

	@Test
	public void testIncludesExcludes() throws Exception {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(BinderExporterApplication.class,
				"--server.port=0",
				"--spring.jmx.enabled=false",
				"--spring.cloud.stream.metrics.delay-millis=500",
				"--spring.cloud.stream.bindings.streamMetrics.destination=foo",
				"--spring.cloud.stream.metrics.includes=mem**",
				"--spring.cloud.stream.metrics.excludes=integration**");
		Metrics metricsSource = applicationContext.getBean(Metrics.class);
		MessageCollector collector = applicationContext.getBean(MessageCollector.class);
		Message message = collector.forChannel(metricsSource.output()).poll(1000, TimeUnit.MILLISECONDS);
		Assert.assertNotNull(message);
		ObjectMapper mapper = applicationContext.getBean(ObjectMapper.class);
		ApplicationMetrics applicationMetrics = mapper.readValue((String)message.getPayload(),ApplicationMetrics.class);
		Assert.assertFalse(contains("integration.channel.errorChannel.errorRate.mean", applicationMetrics.getMetrics()));
		Assert.assertTrue(contains("mem",applicationMetrics.getMetrics()));
		applicationContext.close();
	}

	private boolean contains(String metric, Collection<Metric> metrics){
		boolean contains = false;
		for(Metric entry : metrics){
			contains = entry.getName().equals(metric);
			if(contains){
				break;
			}
		}
		return contains;
	}

	@EnableAutoConfiguration
	public static class BinderExporterApplication {

	}
}
