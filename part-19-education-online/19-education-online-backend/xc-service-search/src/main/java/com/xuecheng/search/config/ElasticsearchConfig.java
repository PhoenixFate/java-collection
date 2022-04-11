package com.xuecheng.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration
public class ElasticsearchConfig {

    @Value("${xuecheng.elasticsearch.hostList}")
    private String hostList;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        HttpHost[] httpHostArray = this.getHttpHostArray();
        //创建RestHighLevelClient客户端
        return new RestHighLevelClient(RestClient.builder(httpHostArray));
    }

    //项目主要使用RestHighLevelClient，对于低级的客户端暂时不用
    @Bean
    public RestClient restClient(){
        HttpHost[] httpHostArray = this.getHttpHostArray();
        return RestClient.builder(httpHostArray).build();
    }
    
    private HttpHost[] getHttpHostArray(){
        //解析hostList配置信息
        String[] split = hostList.split(",");
        //创建HttpHost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHostArray = new HttpHost[split.length];
        for(int i=0;i<split.length;i++){
            String item = split[i];
            if(item.startsWith("http://")){
                item=item.replace("http://","");
                if(item.split(":").length>1){
                    httpHostArray[i] = new HttpHost(item.substring(0,item.lastIndexOf(":")), Integer.parseInt(item.split(":")[1]), "http");
                }else {
                    httpHostArray[i] = new HttpHost(item,80, "http");
                }
            }else if(item.startsWith("https://")){
                item=item.replace("https://","");
                if(item.split(":").length>1){
                    httpHostArray[i] = new HttpHost(item.substring(0,item.lastIndexOf(":")), Integer.parseInt(item.split(":")[1]), "https");
                }else {
                    httpHostArray[i] = new HttpHost(item,443, "https");
                }
            }else {
                if(item.split(":").length>1){
                    httpHostArray[i] = new HttpHost(item.substring(0,item.lastIndexOf(":")), Integer.parseInt(item.split(":")[1]), "http");
                }else {
                    httpHostArray[i] = new HttpHost(item,80, "http");
                }
            }
        }
        return httpHostArray;
    }

}
