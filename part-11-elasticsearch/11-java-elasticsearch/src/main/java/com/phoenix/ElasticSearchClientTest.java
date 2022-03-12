package com.phoenix;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

public class ElasticSearchClientTest {

    private TransportClient client;

    @Before
    public void init() throws UnknownHostException {
        Settings settings= Settings.builder().build();
        this.client =  new PreBuiltTransportClient(settings);
        this.client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }



    /**
     * 创建索引库
     */
    @Test
    public void createIndex() throws UnknownHostException {
        /*
         * 1. 创建一个Settings对象，相当于一个配置信息，配置集群名称
         * 2. 创建一个客户端Client对象
         * 3. 使用Client对象创建一个索引库
         * 4. 关闭client对象
         */
        Settings settings= Settings.builder().build();
        TransportClient client =  new PreBuiltTransportClient(settings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //创建一个索引库
        CreateIndexRequestBuilder java_index = client.admin().indices().prepareCreate("java_index");
        //执行操作
        CreateIndexResponse createIndexResponse = java_index.get();
        System.out.println(createIndexResponse);
    }


    /**
     * 设置mapping
     */
    @Test
    public void setMapping() throws IOException {
        /*
         * 1. 创建Settings对象
         * 2. 创建Client对象
         * 3. 创建一个mapping信息，mapping为json数据
         * 4. 使用client向es 服务器发送mapping信息
         * 5. 关闭client对象
         */
        Settings settings= Settings.builder().build();
        TransportClient client =  new PreBuiltTransportClient(settings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        //创建一个mappings信息
        /*
         * {
            "article":{
                    "properties":{
                        "id":{
                            "type":"long",
                            "store":true,
                            "index":false
                        },
                        "title":{
                            "type":"text",
                            "store":true,
                            "index":true,
                            "analyzer":"ik_smart"
                        },
                        "content":{
                            "type":"text",
                            "store":true,
                            "index":true,
                            "analyzer":"ik_smart"
                        }
                    }
                 }
             }
         */
        XContentBuilder xContentBuilder= XContentFactory.jsonBuilder().startObject().startObject("article");
        xContentBuilder.startObject("properties");
        xContentBuilder.startObject("id").field("type","long").field("store",true).endObject();
        xContentBuilder.startObject("title").field("type","text").field("store",true).field("analyzer","ik_smart").endObject();
        xContentBuilder.startObject("content").field("type","text").field("store",true).field("analyzer","ik_smart").endObject();
        xContentBuilder.endObject().endObject().endObject();

        //使用客户端，把mapping信息设置到索引库中
        PutMappingRequestBuilder putMappingRequestBuilder = client.admin().indices().preparePutMapping("java_index").setType("article").setSource(xContentBuilder);

        //执行操作
        putMappingRequestBuilder.get();
        //关闭
        client.close();

    }

    @Test
    public void createDocument() throws IOException {
        //创建文档对象
        XContentBuilder xContentBuilder= XContentFactory.jsonBuilder().startObject();
        xContentBuilder.field("id",22);
        xContentBuilder.field("title","为界限的大量释放酒啊就开了");
        xContentBuilder.field("content","撒旦解放垃圾覅微积分阿斯顿发射点");
        xContentBuilder.endObject();
        //把文档对象添加到索引库
        this.client.prepareIndex("java_index","article","22").setSource(xContentBuilder).get();
    }

    /**
     * 添加文档的第二种方式：
     *   创建一个pojo类，把pojo转换成json
     */
    @Test
    public void addDocument2() throws JsonProcessingException {
        //创建Article对象
        Article article=new Article();
        article.setId(2L);
        article.setTitle("外界i手机相册v");
        article.setContent("名称v面临困境i哦为今日是的JFK");
        //设置对象的属性
        ObjectMapper objectMapper=new ObjectMapper();
        //把article对象转换成json格式的字符串
        String json = objectMapper.writeValueAsString(article);
        //使用client对象把文档写入索引库
        client.prepareIndex("java_index","article","2").setSource(json, XContentType.JSON).get();

    }

    private void search(QueryBuilder queryBuilder){
        //创建查询对象
        //执行查询
        SearchResponse searchResponse = client.prepareSearch("java_index").setTypes("article").setQuery(queryBuilder).get();
        //取查询结果
        SearchHits hits = searchResponse.getHits();
        //取查询结果的总记录数
        System.out.println("查询结果的总记录数: "+hits.getTotalHits());
        //查询结果列表
        for (SearchHit next : hits) {
            //打印文档对象
            System.out.println(next.getSourceAsString());
            Map<String, Object> sourceAsMap = next.getSourceAsMap();
            System.out.println("id: " + sourceAsMap.get("id"));
            System.out.println("title: " + sourceAsMap.get("title"));
            System.out.println("content: " + sourceAsMap.get("content"));
        }
    }

    /**
     * 通过id查询
     */
    @Test
    public void queryBuilder1(){
        //创建查询对象
        IdsQueryBuilder queryBuilder= QueryBuilders.idsQuery().addIds("22","44");
        this.search(queryBuilder);
    }





    /**
     * 根据term查询
     */
    @Test
    public void queryByTerm(){
        //创建一个termQuery对象
        QueryBuilder queryBuilder=QueryBuilders.termQuery("title","界限");
        this.search(queryBuilder);

    }



    /**
     * 根据query string来查询
     */
    @Test
    public void queryByQueryString(){
        //创建一个QueryBuilder对象
        QueryBuilder queryBuilder=QueryBuilders.queryStringQuery("界限边界").defaultField("title");
        this.search(queryBuilder);

    }


    /**
     * 添加文档的第二种方式：
     *   创建一个pojo类，把pojo转换成json
     * 批量添加数据
     */
    @Test
    public void addDocument3() throws JsonProcessingException {
        for(int i=0;i<10;i++){
            //创建Article对象
            Article article=new Article();
            article.setId((long) (i+10));
            article.setTitle("批量添加的数据title加"+i);
            article.setContent("批量添加的内容content加"+i);
            //设置对象的属性
            ObjectMapper objectMapper=new ObjectMapper();
            //把article对象转换成json格式的字符串
            String json = objectMapper.writeValueAsString(article);
            //使用client对象把文档写入索引库
            client.prepareIndex("java_index","article",i+10+"").setSource(json, XContentType.JSON).get();
        }


    }

    /**
     * 分页查询
     * 默认分页，一页10条
     * from：起始的行号
     * size：每页条数
     */
    @Test
    public void testQueryStringPage(){
        QueryBuilder queryBuilder=QueryBuilders.queryStringQuery("批量").defaultField("title");
        //设置分页数据
        SearchResponse searchResponse = client.prepareSearch("java_index").setTypes("article").setQuery(queryBuilder).setFrom(0).setSize(5).get();
        //取查询结果
        SearchHits hits = searchResponse.getHits();
        //取查询结果的总记录数
        System.out.println("查询结果的总记录数: "+hits.getTotalHits());
        //查询结果列表
        for (SearchHit next : hits) {
            //打印文档对象
            System.out.println(next.getSourceAsString());
            Map<String, Object> sourceAsMap = next.getSourceAsMap();
            System.out.println("id:  " + sourceAsMap.get("id"));
            System.out.println("title:  " + sourceAsMap.get("title"));
            System.out.println("content:  " + sourceAsMap.get("content"));
        }

    }

    /**
     * 高亮显示
     * 高亮的配置
     *      设置高亮显示的字段
     *      设置高亮显示的前缀
     *      设置高亮显示的后缀
     * 在client对象执行查询之前，设置高亮显示的信息
     * 便利结果列表时可以从结果中取出高亮的结果
     *
     *
     */
    @Test
    public void testHighLight(){
        QueryBuilder queryBuilder=QueryBuilders.queryStringQuery("批量数据").defaultField("title");
        //设置高亮信息
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<em>");
        highlightBuilder.postTags("</em>");
        SearchResponse searchResponse = client.prepareSearch("java_index").setTypes("article").setQuery(queryBuilder).highlighter(highlightBuilder).get();
        //取查询结果
        SearchHits hits = searchResponse.getHits();
        //取查询结果的总记录数
        System.out.println("查询结果的总记录数: "+hits.getTotalHits());
        //查询结果列表
        for (SearchHit next : hits) {
            //打印文档对象
            System.out.println(next.getSourceAsString());
            Map<String, Object> sourceAsMap = next.getSourceAsMap();
            System.out.println("id:   " + sourceAsMap.get("id"));
            System.out.println("title:   " + sourceAsMap.get("title"));
            System.out.println("content:   " + sourceAsMap.get("content"));
            System.out.println("---------------高亮结果start----------------------");
            Map<String, HighlightField> highlightFields = next.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            Text[] fragments = title.getFragments();
            if(fragments!=null && fragments.length>0){
                System.out.println(fragments[0]);
            }
            System.out.println("---------------高亮结果end----------------------");

        }


    }


}
