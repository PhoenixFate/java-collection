package com.phoenix.test;

import com.phoenix.dao.CustomerDao;
import com.phoenix.dao.LinkManDao;
import com.phoenix.domain.Customer;
import com.phoenix.domain.LinkMan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class) //声明spring提供的单元测试环境
@ContextConfiguration(locations = "classpath:applicationContext.xml")  //指定spring容器的配置信息
public class CustomerOneToManyTest {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private LinkManDao linkManDao;

    /**
     * 保存一个客户、
     * 保存一个联系人
     * Hibernate: insert into cst_customer (cust_address, cust_industry, cust_level, cust_name, cust_phone, cust_source) values (?, ?, ?, ?, ?, ?)
     * Hibernate: insert into cst_linkman (lkm_cust_id, lkm_email, lkm_gender, lkm_memo, lkm_mobile, lkm_name, lkm_phone, lkm_position) values (?, ?, ?, ?, ?, ?, ?, ?)
     * Hibernate: update cst_linkman set lkm_cust_id=? where lkm_id=?
     *
     * 会有一条多余的update语句
     *      由于一的一方可以维护外键，会发送update语句
     *      解决此问题，只需要在一的一方放弃维护权即可
     *
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testOneToMany(){
        //创建一个客户、创建一个联系人
        Customer customer=new Customer();
        customer.setCustName("one customer");

        LinkMan linkMan=new LinkMan();
        linkMan.setLkmMobile("18751801512");
        linkMan.setLkmName("phoenix");

        //由于配置了多的一方到一的一方的关联关系（当保存的时候，就已经对外键赋值）
        linkMan.setCustomer(customer);
        //由于配置了一的一方到多的一方的关联关系（会发送一条update语句）
        customer.getLinkMans().add(linkMan);
        customerDao.save(customer);
        linkManDao.save(linkMan);
    }

    /**
     * 级联添加
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testOneToManyCascadeAdd(){
        //创建一个客户、创建一个联系人
        Customer customer=new Customer();
        customer.setCustName("one customer cascade add");

        LinkMan linkMan=new LinkMan();
        linkMan.setLkmMobile("18751801512");
        linkMan.setLkmName("phoenix cascade add");

        //由于配置了多的一方到一的一方的关联关系（当保存的时候，就已经对外键赋值）
        linkMan.setCustomer(customer);
        //由于配置了一的一方到多的一方的关联关系（会发送一条update语句）
        customer.getLinkMans().add(linkMan);
        customerDao.save(customer);
        linkManDao.save(linkMan);
    }


    /**
     * 级联删除
     *      删除客户的同时，删除所有联系人
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testOneToManyCascadeDelete(){
        Customer customer = customerDao.findOne(10L);
        customerDao.delete(customer);
    }




}
