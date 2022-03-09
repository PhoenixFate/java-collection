package com.phoenix.test;

import com.phoenix.dao.CustomerDao;
import com.phoenix.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class) //声明spring提供的单元测试环境
@ContextConfiguration(locations = "classpath:applicationContext.xml")  //指定spring容器的配置信息
public class CustomerSpecificationTest {

    @Autowired
    private CustomerDao customerDao;


    /**
     * 复杂查询
     */
    @Test
    public void testSpecification(){
        //匿名内部类
        /**
         * 自定义查询条件
         * 1.实现Specification接口（提供范型：查询的对象类型）
         * 2.实现toPredicate方法（构造查询条件）
         * 3.需要借助方法参数中的两个参数（
         *      root 获取需要查询的对象属性
         *      CriteriaBuilder 构造查询条件，内部封装了很多的查询条件（模糊匹配，精准匹配）
         *      ）
         * 查询条件
         *      1 查询方式
         *          cb对象
         *      2 比较的属性名称
         *          root对象
         */
        Specification<Customer> specificaton=new Specification<Customer>() {
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                //1.获取比较的属性
                Path custName = root.get("custName");
                //2.构造查询条件
                //第一个参数：需要比较的属性（Path对象）
                //第二个参数：当前需要比较的取值
                Predicate predicate = cb.equal(custName, "tomcat");
                return predicate;
            }
        };
        List<Customer> customerList = customerDao.findAll(specificaton);
        for(Customer customer:customerList){
            System.out.println(customer);
        }
    }


    /**
     * 多条件查询
     */
    @Test
    public void testSpecificationMore(){
        //匿名内部类
        /**
         * 自定义查询条件
         * 1.实现Specification接口（提供范型：查询的对象类型）
         * 2.实现toPredicate方法（构造查询条件）
         * 3.需要借助方法参数中的两个参数（
         *      root 获取需要查询的对象属性
         *      CriteriaBuilder 构造查询条件，内部封装了很多的查询条件（模糊匹配，精准匹配）
         *      ）
         * 查询条件
         *      1 查询方式
         *          cb对象
         *      2 比较的属性名称
         *          root对象
         */
        Specification<Customer> specificaton=new Specification<Customer>() {
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                //1.获取比较的属性
                Path custName = root.get("custName");
                Path custIndustry = root.get("custIndustry");
                //2.构造查询条件
                //第一个参数：需要比较的属性（Path对象）
                //第二个参数：当前需要比较的取值
                Predicate predicate = cb.equal(custName, "tomcat");
                Predicate predicate2=cb.equal(custIndustry,"it");
                return cb.and(predicate, predicate2);
            }
        };
        List<Customer> customerList = customerDao.findAll(specificaton);
        for(Customer customer:customerList){
            System.out.println(customer);
        }
    }

    /**
     * 模糊查询
     */
    @Test
    public void testSpecificationMoreLike(){
        //匿名内部类
        /**
         * 自定义查询条件
         * 1.实现Specification接口（提供范型：查询的对象类型）
         * 2.实现toPredicate方法（构造查询条件）
         * 3.需要借助方法参数中的两个参数（
         *      root 获取需要查询的对象属性
         *      CriteriaBuilder 构造查询条件，内部封装了很多的查询条件（模糊匹配，精准匹配）
         *      ）
         * 查询条件
         *      1 查询方式
         *          cb对象
         *      2 比较的属性名称
         *          root对象
         */
        Specification<Customer> specificaton=new Specification<Customer>() {
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                //1.获取比较的属性
                Path custName = root.get("custName");
                Path custIndustry = root.get("custIndustry");
                //2.构造查询条件
                //第一个参数：需要比较的属性（Path对象）
                //第二个参数：当前需要比较的取值
                Predicate predicate = cb.equal(custName, "tomcat");
                Predicate predicate2=cb.like(custIndustry,"it%");
                return cb.and(predicate, predicate2);
            }
        };
        List<Customer> customerList = customerDao.findAll(specificaton);
        for(Customer customer:customerList){
            System.out.println(customer);
        }
    }


    /**
     * 字段排序
     */
    @Test
    public void testSpecificationSort(){
        //匿名内部类
        /**
         * 自定义查询条件
         * 1.实现Specification接口（提供范型：查询的对象类型）
         * 2.实现toPredicate方法（构造查询条件）
         * 3.需要借助方法参数中的两个参数（
         *      root 获取需要查询的对象属性
         *      CriteriaBuilder 构造查询条件，内部封装了很多的查询条件（模糊匹配，精准匹配）
         *      ）
         * 查询条件
         *      1 查询方式
         *          cb对象
         *      2 比较的属性名称
         *          root对象
         */
        Specification<Customer> specificaton=new Specification<Customer>() {
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                //1.获取比较的属性
                Path custName = root.get("custName");
                Path custIndustry = root.get("custIndustry");
                //2.构造查询条件
                //第一个参数：需要比较的属性（Path对象）
                //第二个参数：当前需要比较的取值
                Predicate predicate = cb.equal(custName, "tomcat");
                Predicate predicate2=cb.like(custIndustry,"it%");
                return cb.and(predicate, predicate2);
            }
        };
        //添加排序
        //创建排序对象
        //第一个参数：正序、倒序
        //第二个参数，属性
        Sort sort=new Sort(Sort.Direction.DESC,"custId");
        List<Customer> customerList = customerDao.findAll(specificaton,sort);
        for(Customer customer:customerList){
            System.out.println(customer);
        }
    }

    /**
     * 按照字段进行排序
     */
    @Test
    public void testSpecificationSortMore(){
        //匿名内部类
        /**
         * 自定义查询条件
         * 1.实现Specification接口（提供范型：查询的对象类型）
         * 2.实现toPredicate方法（构造查询条件）
         * 3.需要借助方法参数中的两个参数（
         *      root 获取需要查询的对象属性
         *      CriteriaBuilder 构造查询条件，内部封装了很多的查询条件（模糊匹配，精准匹配）
         *      ）
         * 查询条件
         *      1 查询方式
         *          cb对象
         *      2 比较的属性名称
         *          root对象
         */
        Specification<Customer> specificaton=new Specification<Customer>() {
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                //1.获取比较的属性
                Path custName = root.get("custName");
                Path custIndustry = root.get("custIndustry");
                //2.构造查询条件
                //第一个参数：需要比较的属性（Path对象）
                //第二个参数：当前需要比较的取值
                Predicate predicate = cb.equal(custName, "tomcat");
                Predicate predicate2=cb.like(custIndustry,"it%");
                return cb.and(predicate, predicate2);
            }
        };
        //添加排序
        //创建排序对象
        //第一个参数：正序、倒序
        //第二个参数，属性
        List<Sort.Order> orders=new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"custId"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"custName"));
        Sort sort=new Sort(orders);
        List<Customer> customerList = customerDao.findAll(specificaton,sort);
        for(Customer customer:customerList){
            System.out.println(customer);
        }
    }

    /**
     * 分页查询
     */
    @Test
    public void testSpecificationPage(){
        /**
         * 分页参数 pageable：查询的页面、每页的查询的条件
         *
         */
        Pageable pageable=new PageRequest(0,3);
        Page<Customer> page = customerDao.findAll(pageable);
        System.out.println("总条数："+page.getTotalElements());

        for(Customer customer:page.getContent()){
            System.out.println(customer);
        }
    }




}
