package com.xuecheng.search.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.elasticsearch.client.RestClientBuilder.DEFAULT_CONNECTION_REQUEST_TIMEOUT_MILLIS;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration
public class ElasticsearchConfig {

    /**
     * 超时时间设为5分钟
     */
    private static final int MAX_RETRY_TIMEOUT = 5 * 60 * 1000;

    private static final int CONNECTION_TIME_OUT = 60 * 1000;

    private static final int SOCKET_TIME_OUT = 60 * 1000;

    private static final int REQUEST_TIMEOUT = 30 * 1000;

    @Value("${xuecheng.elasticsearch.hostList}")
    private String hostList;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        HttpHost[] httpHostArray = this.getHttpHostArray();
        //创建RestHighLevelClient客户端
        RestClientBuilder restClientBuilder = RestClient.builder(httpHostArray).setMaxRetryTimeoutMillis(MAX_RETRY_TIMEOUT).setHttpClientConfigCallback(httpClientBuilder -> {
            RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                    //超时时间5分钟
                    .setConnectTimeout(CONNECTION_TIME_OUT)
                    //这就是Socket超时时间设置
                    .setSocketTimeout(SOCKET_TIME_OUT)
                    .setConnectionRequestTimeout(REQUEST_TIMEOUT);
            httpClientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());
            return httpClientBuilder;
        });
        return new RestHighLevelClient(restClientBuilder);
    }

    //项目主要使用RestHighLevelClient，对于低级的客户端暂时不用
    @Bean
    public RestClient restClient() {
        HttpHost[] httpHostArray = this.getHttpHostArray();
        return RestClient.builder(httpHostArray).setMaxRetryTimeoutMillis(MAX_RETRY_TIMEOUT).setHttpClientConfigCallback(httpClientBuilder -> {
                    RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                            //超时时间5分钟
                            .setConnectTimeout(CONNECTION_TIME_OUT)
                            //这就是Socket超时时间设置
                            .setSocketTimeout(SOCKET_TIME_OUT)
                            .setConnectionRequestTimeout(REQUEST_TIMEOUT);
                    httpClientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());
                    return httpClientBuilder;
                })
                .build();
    }

    private HttpHost[] getHttpHostArray() {
        //解析hostList配置信息
        String[] split = hostList.split(",");
        //创建HttpHost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHostArray = new HttpHost[split.length];
        for (int i = 0; i < split.length; i++) {
            String item = split[i];
            if (item.startsWith("http://")) {
                item = item.replace("http://", "");
                if (item.split(":").length > 1) {
                    httpHostArray[i] = new HttpHost(item.substring(0, item.lastIndexOf(":")), Integer.parseInt(item.split(":")[1]), "http");
                } else {
                    httpHostArray[i] = new HttpHost(item, 80, "http");
                }
            } else if (item.startsWith("https://")) {
                item = item.replace("https://", "");
                if (item.split(":").length > 1) {
                    httpHostArray[i] = new HttpHost(item.substring(0, item.lastIndexOf(":")), Integer.parseInt(item.split(":")[1]), "https");
                } else {
                    httpHostArray[i] = new HttpHost(item, 443, "https");
                }
            } else {
                if (item.split(":").length > 1) {
                    httpHostArray[i] = new HttpHost(item.substring(0, item.lastIndexOf(":")), Integer.parseInt(item.split(":")[1]), "http");
                } else {
                    httpHostArray[i] = new HttpHost(item, 80, "http");
                }
            }
        }
        return httpHostArray;
    }

}
