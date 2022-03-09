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

import java.util.Set;

/**
 * important
 * 对象导航查询
 */
@RunWith(SpringJUnit4ClassRunner.class) //声明spring提供的单元测试环境
@ContextConfiguration(locations = "classpath:applicationContext.xml")  //指定spring容器的配置信息
public class CustomerObjectNavigationTest {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private LinkManDao linkManDao;

    /**
     * 测试对象导航（查询一个对象的时候，通过此对象查询所有的关联对象）
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testObjectNavigation(){
        //getOne() 延迟加载
        Customer customer = customerDao.getOne(5L);
        //对象导航查询
        Set<LinkMan> linkMans = customer.getLinkMans();
        for(LinkMan linkMan:linkMans){
            System.out.println(linkMan);
        }

    }

    /**
     * 测试对象导航（查询一个对象的时候，通过此对象查询所有的关联对象）
     *
     * 对象导航查询
     *      默认使用的是延迟加载的形式查询的
     *          调用get方法并不会立即发送查询，而是在使用关联查询对象的时候才会延迟查询
     * 可修改配置，将延迟加载改为立即加载
     *      fetch，需要配置到多表映射的关系的注解上
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testObjectNavigation2(){
        //findOne() 立即加载
        Customer customer = customerDao.findOne(5L);
        //对象导航查询
        Set<LinkMan> linkMans = customer.getLinkMans();
        for(LinkMan linkMan:linkMans){
            System.out.println(linkMan);
        }

    }


    /**
     * 从多的一方查询一的一方，默认是立即加载
     * 从联系人对象导航查询他所属的客户
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testObjectNavigation3(){
        LinkMan linkMan = linkManDao.findOne(1L);
        Customer customer = linkMan.getCustomer();
        System.out.println(customer);
    }


}
