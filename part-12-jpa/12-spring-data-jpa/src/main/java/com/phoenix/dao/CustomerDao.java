package com.phoenix.dao;

import com.phoenix.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * SpringDataJpa dao层的接口规范
 *      继承两个接口：
 *          JpaRepository<操作的实体类，实体类主键类型>
 *              封装类crud基本操作
 *          JpaSpecificationExecutor<操作的实体类>
 *              封装类复杂查询：分页等
 */
public interface CustomerDao extends JpaRepository<Customer,Long>, JpaSpecificationExecutor<Customer> {


    /**
     * 使用jpql需要将jpaql语句配置到接口方法上
     *      1.特有的查询：需要在dao接口上配置方法
     *      2.在新添加的方法上，使用注解的形式配置jpql查询语句
     *      3.注解：@Query
     * jpql: from Customer where custName = ?
     */
    @Query(value = "from Customer where custName = ?1")
    Customer findByNameByJpql(String name);

    /**
     * 对于多个占位符参数
     *      赋值的时候，默认的情况下，占位符的位置需要和方法参数中的位置保持一致
     * 可以指定占位符参数的位置
     *      ？ 索引的方式，指定此占位的取值来源
     * @param name
     * @param id
     * @return
     */
    @Query("from Customer where custName = ?1 and custId=?2")
    Customer findByNameAndIdByJpql(String name,Long id);


    /**
     * jpql update
     *
     * @Query声明当前sql、jpql是一个查询操作
     * @Modifying声明当前sql、jpql是一个更新操作
     *
     * @param name
     * @param id
     */
    @Query("update Customer set custName=?1 where custId=?2")
    @Modifying
    void updateCustomerName(String name,Long id);


    /**
     * @Query
     * value ：jpql or sql语句
     * nativeQuery（是否使用本地查询）: false (默认，使用jpql查询) ｜ true 使用sql查询
     *
     */
    @Query(value = "select * from cst_customer",nativeQuery = true)
    List<Customer> findAllBySql();

    @Query(value = "select * from cst_customer where cust_name like ?1",nativeQuery = true)
    List<Customer> findByNameBySql(String name);


    /**
     * 按照方法名称规则进行查询
     */
    List<Customer> findByCustName(String custName);

    /**
     * findBy+属性名称
     * 默认使用属性名称完全匹配进行查询
     * findBy+属性名称+查询方法(like | isNull)
     *
     */
    List<Customer> findByCustNameLike(String custName);

    /**
     * 多条件查询
     * findBy+属性名称+查询方法(精准匹配可以省略)+ 多条件连接符（and|or） + 属性名称+查询方法
     *
     * 当返回有多个对象的时候，需要+First；
     */
    Customer findFirstByCustNameAndCustIndustry(String custName,String custIndustry);

}
