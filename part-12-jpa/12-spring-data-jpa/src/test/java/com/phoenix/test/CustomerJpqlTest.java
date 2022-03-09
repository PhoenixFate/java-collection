package com.phoenix.test;

import com.phoenix.dao.CustomerDao;
import com.phoenix.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class) //声明spring提供的单元测试环境
@ContextConfiguration(locations = "classpath:applicationContext.xml")  //指定spring容器的配置信息
public class CustomerJpqlTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void testFindByNameJpql(){
        Customer customer = customerDao.findByNameByJpql("tomcat4");
        System.out.println(customer);
    }

    @Test
    public void testFindByNameAndIdJpql(){
        Customer customer = customerDao.findByNameAndIdByJpql("tomcat4",4L);
        System.out.println(customer);
    }

    /**
     * springdata-jpa中使用update更新操作，
     * 1.需要手动添加事务 @transactional
     * 2.并且默认会自动回滚，需要手动添加 @rollback(value=false)
     *
     */
    @Test
    @Transactional  //添加事务支持
    @Rollback(value = false)
    public void testUpdate(){
        customerDao.updateCustomerName("update by jpql",4L);
    }

}
