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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.metrics.collector.utils.MetricJsonSerializer;
import org.springframework.context.annotation.Bean;

/**
 * @author Vinicius Carvalho
 */
@SpringBootApplication
@EnableBinding(Sink.class)
public class MetricsCollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetricsCollectorApplication.class, args);
	}

	@Bean
	public MetricJsonSerializer jsonSerializer(){
		return new MetricJsonSerializer();
	}
}
