package com.yaincoding.yacobooksapi.config;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ElasticSearchConfig {
	@Value("${elasticsearch.host}")
	private String ES_HOST;

	@Value("${elasticsearch.port}")
	private int ES_PORT;

	@Bean
	public RestHighLevelClient createRestHighLevelClient() throws IOException {

		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		String ES_USER = System.getenv("ES_USER");
		String ES_PASSWORD = System.getenv("ES_PASSWORD");
		credentialsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(ES_USER, ES_PASSWORD));

		RestClientBuilder builder = RestClient.builder(new HttpHost(ES_HOST, ES_PORT, "http"))
				.setHttpClientConfigCallback((httpClientBuilder) -> {
					return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
				});

		RestHighLevelClient esClient = new RestHighLevelClient(builder);
		if (esClient.ping(RequestOptions.DEFAULT)) {
			log.info("Successfully connected to elasticsearch cluster");
		}

		return esClient;
	}
}
