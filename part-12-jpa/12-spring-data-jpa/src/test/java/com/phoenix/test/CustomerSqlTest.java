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

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class) //声明spring提供的单元测试环境
@ContextConfiguration(locations = "classpath:applicationContext.xml")  //指定spring容器的配置信息
public class CustomerSqlTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void testFindAllBySql(){
        List<Customer> customerList = customerDao.findAllBySql();
        for(Customer customer:customerList){
            System.out.println(customer);
        }
    }

    @Test
    public void testFindByNameBySql(){
        List<Customer> customerList=customerDao.findByNameBySql("%tomcat%");
        for(Customer customer:customerList){
            System.out.println(customer);
        }
    }


}
