package com.phoenix.test;

import com.phoenix.pojo.Customer;
import com.phoenix.util.JpaUtil;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

/**
 * jpql查询测试
 */
public class JpqlTest {

    /**
     * 查询全部
     *      jpql  from Customer
     *      sql  select * from cst_customer
     */
    @Test
    public void testFindAll(){
        //1.获取entityManager对象
        EntityManager entityManager= JpaUtil.getEntityManager();
        //2.开启事务　
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        //3.查询全部
        String jpql="from Customer order by custId desc";
        Query query = entityManager.createQuery(jpql);//创建查询query对象，query对象才是执行jpql的对象
        //发送查询，并封装结果集
        List<Customer> resultList = query.getResultList();
        for(Customer customer:resultList){
            System.out.println(customer);
        }
        transaction.commit();
    }

    /**
     * 统计查询
     */
    @Test
    public void testCount(){
        //1.获取entityManager对象
        EntityManager entityManager= JpaUtil.getEntityManager();
        //2.开启事务　
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        //3.查询全部
        //3.1根据jpql语句创建query对象
        String jpql="select count(custId) from Customer";
        Query query = entityManager.createQuery(jpql);//创建查询query对象，query对象才是执行jpql的对象
        //3.2设置参数
        //3.3发送查询，并封装结果集
        Object singleResult = query.getSingleResult();
        System.out.println(singleResult);
        transaction.commit();
    }

    /**
     * 分页查询
     */
    @Test
    public void testPage(){
        //1.获取entityManager对象
        EntityManager entityManager= JpaUtil.getEntityManager();
        //2.开启事务　
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        //3.查询全部
        //3.1根据jpql语句创建query对象
        String jpql="from Customer";
        Query query = entityManager.createQuery(jpql);//创建查询query对象，query对象才是执行jpql的对象
        //3.2对参数赋值
        //分页参数设置
        //起始索引
        query.setFirstResult(1);
        //设置每页查询对条数
        query.setMaxResults(2);
        //上面两句话 等价于 limit 1,3
        //3.3发送查询，并封装结果集
        List<Customer> resultList = query.getResultList();
        for (Customer customer:resultList){
            System.out.println(customer);
        }
        transaction.commit();
    }


    /**
     * 条件查询
     */
    @Test
    public void testConditional(){
        //1.获取entityManager对象
        EntityManager entityManager= JpaUtil.getEntityManager();
        //2.开启事务　
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        //3.查询全部
        //3.1根据jpql语句创建query对象
        String jpql="from Customer where custName like ?";
        Query query = entityManager.createQuery(jpql);//创建查询query对象，query对象才是执行jpql的对象
        //3.2对参数赋值
        //对占位符赋值，第一个参数是索引值，从1开始
        query.setParameter(1,"%4");
        //3.3发送查询，并封装结果集
        List<Customer> resultList = query.getResultList();
        for (Customer customer:resultList){
            System.out.println(customer);
        }
        transaction.commit();
    }





}
