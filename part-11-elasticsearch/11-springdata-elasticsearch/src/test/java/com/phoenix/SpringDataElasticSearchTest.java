package com.phoenix;

import com.phoenix.entity.Article;
import com.phoenix.repository.ArticleRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringDataElasticSearchTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Test
    public void createIndex() {
        //创建索引，并配置映射关系
        elasticsearchTemplate.createIndex(Article.class);

    }

    /**
     * 添加文档
     * 1.创建article对象
     * 2.使用repository来添加文档
     */
    @Test
    public void addDocument() {
        //创建一个Article对象
        Article article = new Article();
        article.setId(5L);
        article.setTitle("我们仙女你是深刻搭街坊卡拉");
        article.setContent("名字v看啥附件为i发安吉拉地方");
        //把文档写入索引库
        articleRepository.save(article);
    }

    /**
     * 删除文档
     */
    @Test
    public void deleteDocument(){
        articleRepository.deleteById(2L);
    }


    /**
     * 修改文档
     */
    @Test
    public void updateDocument(){
        Article article = new Article();
        article.setId(1L);
        article.setTitle("文件撒旦解放了就啊即使对方了解按时灯笼裤飞机啊精神科大夫拉萨解放的离开卢卡斯的房价-update");
        article.setContent("微积分女警第戎i的方式规划设计书法家爱上了对方家里咖啡的距离");
        articleRepository.save(article);
    }

    /**
     * 查询索引库
     */
    @Test
    public void findAll(){
        Iterable<Article> all = articleRepository.findAll();
        all.forEach(System.out::println);
    }

    @Test
    public void findById(){
        Optional<Article> article =  articleRepository.findById(1L);
        System.out.println(article.get());
    }

    /**
     * 自定义查询
     */
    @Test
    public void findByTitle(){
        List<Article> list = articleRepository.findByTitle("文件");
        list.forEach(System.out::println);
    }

    @Test
    public void findByTitleOrContent(){
        List<Article> list = articleRepository.findByTitleOrContent("文件","只能");
        list.forEach(System.out::println);
    }

    /**
     * 设置分页
     */
    @Test
    public void findByTitleOrContent2(){
        Pageable pageable= PageRequest.of(1,1);
        List<Article> list = articleRepository.findByTitleOrContent("文件","只能",pageable);
        list.forEach(System.out::println);
    }

    /**
     * 使用原生的查询进行查询
     * 1. 创建一个NativeSearchQuery对象
     * 2. 使用ElasticSearchTemplate对象执行查询
     * 3. 取查询结果
     */
    @Test
    public void nativeSearchQuery(){
        //创建一个查询对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder=new NativeSearchQueryBuilder().withQuery(QueryBuilders.queryStringQuery("文件abc撒旦").defaultField("title")).withPageable(PageRequest.of(0,1));
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        List<Article> articles = elasticsearchTemplate.queryForList(nativeSearchQuery, Article.class);
        articles.forEach(System.out::println);
    }




}
