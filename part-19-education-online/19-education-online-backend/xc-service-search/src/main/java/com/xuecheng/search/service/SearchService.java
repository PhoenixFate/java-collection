package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Value("${xuecheng.course.index}")
    private String courseIndexName;

    @Value("${xuecheng.course.type}")
    private String courseIndexType;

    @Value("${xuecheng.course.source_field}")
    private String courseSourceField;

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    //课程搜索
    public QueryResponseResult<CoursePub> findList(int page, int size, CourseSearchParam courseSearchParam) throws IOException {
        if (courseSearchParam == null) {
            courseSearchParam = new CourseSearchParam();
        }

        //创建搜索的请求对象
        SearchRequest searchRequest = new SearchRequest(courseIndexName);
        //设置搜索类型
        searchRequest.types(courseIndexType);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //过滤源字段
        String[] sourceFieldArray = courseSourceField.split(",");
        searchSourceBuilder.fetchSource(sourceFieldArray, new String[]{});
        //创建一个布尔查询对象，用于拼接多个查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //搜索条件
        //根据关键字来搜索
        if (StringUtils.isNotEmpty(courseSearchParam.getKeyword())) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(), "name", "description", "teachplan")
                    .minimumShouldMatch("70%").field("name", 5);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        //利用过滤器来筛选分类
        if (StringUtils.isNotEmpty(courseSearchParam.getMt())) {
            //根据一级分类筛选
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt", courseSearchParam.getMt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getSt())) {
            //根据二级分类筛选
            boolQueryBuilder.filter(QueryBuilders.termQuery("st", courseSearchParam.getSt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getGrade())) {
            //根据难度等级
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade", courseSearchParam.getGrade()));
        }

        //设置查询对象
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        QueryResult<CoursePub> queryResult = new QueryResult<>();
        List<CoursePub> list = new ArrayList<>();
        //执行搜索
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        //响应结果
        SearchHits hits = searchResponse.getHits();
        //总记录数
        long total = hits.getTotalHits();
        queryResult.setTotal(total);
        //获取结果集
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            CoursePub coursePub = new CoursePub();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            coursePub.setName((String) sourceAsMap.get("name"));
            coursePub.setPic((String) sourceAsMap.get("pic"));
            coursePub.setGrade((String) sourceAsMap.get("grade"));
            if (sourceAsMap.get("price") != null) {
                coursePub.setPrice(Double.parseDouble(sourceAsMap.get("price").toString()));
            }
            if (sourceAsMap.get("price_old") != null) {
                coursePub.setPrice_old(Double.parseDouble(sourceAsMap.get("price_old").toString()));
            }
            list.add(coursePub);
        }
        queryResult.setList(list);
        return new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
    }
}
