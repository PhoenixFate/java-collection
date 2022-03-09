package com.phoenix.test;

import com.phoenix.dao.CustomerDao;
import com.phoenix.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class) //声明spring提供的单元测试环境
@ContextConfiguration(locations = "classpath:applicationContext.xml")  //指定spring容器的配置信息
public class CustomerNamedFunctionTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void testFindAllByNamedFunction(){
        List<Customer> customerList = customerDao.findByCustName("tomcat");
        for(Customer customer:customerList){
            System.out.println(customer);
        }
    }


    @Test
    public void testFindAllByNamedFunctionLike(){
        List<Customer> customerList = customerDao.findByCustNameLike("%tomcat%");
        for(Customer customer:customerList){
            System.out.println(customer);
        }
    }

    /**
     * 多条件查询
     */
    @Test
    public void testFindAllByNamedFunctionMoreConditional(){
        Customer customer = customerDao.findFirstByCustNameAndCustIndustry("tomcat","it");
        System.out.println(customer);
    }


}
