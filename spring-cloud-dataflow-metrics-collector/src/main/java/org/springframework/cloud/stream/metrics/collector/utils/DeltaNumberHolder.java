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

package org.springframework.cloud.stream.metrics.collector.utils;

import java.util.Date;


/**
 * @author Vinicius Carvalho
 */
public class DeltaNumberHolder extends NumberHolder<Double>{

	private Double previousValue;
	private Date timestamp;

	public DeltaNumberHolder(){}

	public DeltaNumberHolder(Double value){
		setValue(value);
	}

	@Override
	public Double getValue() {
		if(previousValue == null)
			return null;
		return value-previousValue;
	}



	@Override
	public void setValue(Double value) {
		this.timestamp = new Date();
		if(this.value != null) {
			previousValue = this.value;
		}
		super.setValue(value);
	}
}
