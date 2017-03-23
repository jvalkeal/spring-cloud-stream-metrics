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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * @author Vinicius Carvalho
 */
public class ElasticSearchRandomDataExporter {

	private ObjectMapper mapper = new ObjectMapper();
	Random random = new Random();
	private TransportClient client;

	ElasticSearchRandomDataExporter(){
		try {
			this.client  = new PreBuiltTransportClient(Settings.builder()
					.put("cluster.name", "elasticsearch")
					.build())
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void generateData(){
		Long now = System.currentTimeMillis();
		Long start = now - 24*3600*1000;
		int count = 0;
		Long current = start;
		List<ElasticSearchData> datapoints = new ArrayList<>();
		while(current < now){
			count++;
			datapoints.addAll(sample(current));
			current += 5000;
			if(count %1000==0){
				insert(datapoints);
				datapoints.clear();
			}
		}
		insert(datapoints);

	}

	private void insert(List<ElasticSearchData> dataList){
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for(ElasticSearchData data : dataList){
			try {
				bulkRequest.add(client.prepareIndex("metrics_nested","metrics").setSource(mapper.writeValueAsBytes(data)));
			}
			catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		BulkResponse response = bulkRequest.get();
		if(response.hasFailures()){
			for(BulkItemResponse item : response.getItems()){
			}
		}
	}

	public List<ElasticSearchData> sample(long tick){
		List<ElasticSearchData> points = new ArrayList<>();

		ElasticSearchData http = new ElasticSearchData("http","simple",0,tick);
		http.getMetrics().put("mem",range(127,128)*1000);
		http.getMetrics().put("integration.channel.input.send",25+random.nextInt(15));
		http.getMetrics().put("integration.channel.streamMetrics.send",random.nextInt(2));

		ElasticSearchData log = new ElasticSearchData("log","simple",0,tick);
		log.getMetrics().put("mem",range(127,128)*1000);
		log.getMetrics().put("integration.channel.input.send",25+random.nextInt(15));
		log.getMetrics().put("integration.channel.streamMetrics.send",random.nextInt(2));

		ElasticSearchData twitterStream = new ElasticSearchData("twitterstream","twitterStream",0,tick);
		twitterStream.getMetrics().put("mem",range(250,256)*1000);
		twitterStream.getMetrics().put("integration.channel.input.send",25+random.nextInt(75));
		twitterStream.getMetrics().put("integration.channel.streamMetrics.send",random.nextInt(2));


		ElasticSearchData splitter = new ElasticSearchData("splitter","twitterStream",0,tick);
		splitter.getMetrics().put("mem",range(250,256)*1000);
		splitter.getMetrics().put("integration.channel.input.send",250+random.nextInt(75));
		splitter.getMetrics().put("integration.channel.streamMetrics.send",random.nextInt(2));

		ElasticSearchData twitterLog1 = new ElasticSearchData("log","twitterStream",0,tick);
		twitterLog1.getMetrics().put("mem",range(127,128)*1000);
		twitterLog1.getMetrics().put("integration.channel.input.send",120+random.nextInt(75));
		twitterLog1.getMetrics().put("integration.channel.streamMetrics.send",random.nextInt(2));

		ElasticSearchData twitterLog2 = new ElasticSearchData("log","twitterStream",1,tick);
		twitterLog2.getMetrics().put("mem",range(127,128)*1000);
		twitterLog2.getMetrics().put("integration.channel.input.send",120+random.nextInt(75));
		twitterLog2.getMetrics().put("integration.channel.streamMetrics.send",random.nextInt(2));

		ElasticSearchData twitterLog3 = new ElasticSearchData("log","twitterStream",2,tick);
		twitterLog3.getMetrics().put("mem",range(127,128)*1000);
		twitterLog3.getMetrics().put("integration.channel.input.send",120+random.nextInt(75));
		twitterLog3.getMetrics().put("integration.channel.streamMetrics.send",random.nextInt(2));

		points.add(http);
		points.add(log);
		points.add(twitterStream);
		points.add(splitter);
		points.add(twitterLog1);
		points.add(twitterLog2);
		points.add(twitterLog3);
		return points;
	}


	private float range(float min, float max){
		return min + (max-min)*random.nextFloat();
	}

	public static void main(String[] args) {
		ElasticSearchRandomDataExporter exporter = new ElasticSearchRandomDataExporter();
		exporter.generateData();
	}
}
