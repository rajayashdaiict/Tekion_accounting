package com.Jpalearning.jpalearning.config;
//
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.example.es.demo.repo")
//@ComponentScan(basePackages = {"com.example.es.demo"})
//public class Config extends AbstractElasticsearchConfiguration {
//
//    @Value("$elasticsearch.url")
//    public String elasticSearchUrl;
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//        final ClientConfiguration build = ClientConfiguration.builder().connectedTo(elasticSearchUrl).build();
//        RestHighLevelClient rest = RestClients.create(build).rest();
//        return rest;
//    }
//}


//import org.elasticsearch.client.RestHighLevelClient;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//import org.springframework.stereotype.Component;
//
//@Configuration
//public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
//    @Value("${elasticsearch.host}")
//    private String host;
//    @Value("${elasticsearch.port}")
//    private int port;
//    @Bean
//    @Override
//    public @NotNull RestHighLevelClient elasticsearchClient() {
//        ClientConfiguration clientConfiguration =
//                ClientConfiguration.builder().connectedTo(host + ":" + port).build();
//        return RestClients.create(clientConfiguration).rest();
//    }
//    @Bean
//    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
//        return new ElasticsearchRestTemplate(elasticsearchClient());
//    }
//}

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;


import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.http.HttpHeaders;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.grocerymongodb.repository")
public class BeanCreationConfig {

    @Bean
    public RestHighLevelClient client() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")).setDefaultHeaders(compatibilityHeaders()));
    }
    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }

    private Header[] compatibilityHeaders() {
        return new Header[] {
                new BasicHeader(HttpHeaders.ACCEPT, "application/vnd.elasticsearch+json;compatible-with=7"),
                new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7")
        };
    }
}
//    @Bean
//    public ElasticsearchClient elasticsearchClient() {
//
//        RestClient httpClient = RestClient.builder(new HttpHost("localhost", 9200)).build();
//
//        ElasticsearchTransport transport = new RestClientTransport(httpClient, new JacksonJsonpMapper());
//
//        ElasticsearchClient esClient = new ElasticsearchClient(transport);
//
//        return esClient;
//    }




//
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "*")
//public class ElasticsearchClientConfig {
//
//    @Value("${elasticsearch.host}")
//    private String host;
//
//    @Value("${elasticsearch.port}")
//    private int port;
//
//    @Value("${elasticsearch.protocol}")
//    private String protocol;
//
//    @Value("${elasticsearch.username}")
//    private String userName;
//
//    @Value("${elasticsearch.password}")
//    private String password;
//
//    @Bean(destroyMethod = "close")
//    public RestHighLevelClient restClient() {
//
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
//
//        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, protocol))
//                                              .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
//                                              .setDefaultHeaders(compatibilityHeaders());
//
//        return new RestHighLevelClient(builder);
//    }
//
//    private Header[] compatibilityHeaders() {
//        return new Header[]{new BasicHeader(HttpHeaders.ACCEPT, "application/vnd.elasticsearch+json;compatible-with=7"), new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7")};
//    }