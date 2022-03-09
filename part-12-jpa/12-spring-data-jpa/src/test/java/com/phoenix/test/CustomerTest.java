package com.phoenix.test;

import com.phoenix.dao.CustomerDao;
import com.phoenix.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class) //声明spring提供的单元测试环境
@ContextConfiguration(locations = "classpath:applicationContext.xml")  //指定spring容器的配置信息
public class CustomerTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void testFindAll(){
        List<Customer> customerList = customerDao.findAll();
        for(Customer customer:customerList){
            System.out.println(customer);
        }
    }

    @Test
    public void testFindOne(){
        Customer one = customerDao.findOne(2l);
        System.out.println(one);
    }


    /**
     * save 保存或者更新
     *      根据传递的对象释放存在主键id
     *      如果没有id主键属性：保存
     *      存在id主键属性，则根据id查询数据，更新数据
     */
    @Test
    public void testSave(){
        Customer customer=new Customer();
        customer.setCustName("spring data jps");
        customer.setCustAddress("spring address");
        customer.setCustIndustry("spring industry");
        customerDao.save(customer);
    }

    @Test
    public void testUpdate(){
        Customer customer = customerDao.findOne(7L);
        customer.setCustLevel("new level");
        customerDao.save(customer);
    }


    @Test
    public void testDelete(){
        customerDao.delete(7L);

    }


    /**
     * 统计查询，查询总数
     */
    @Test
    public void testCount(){
        long count = customerDao.count();
        System.out.println(count);
    }


    /**
     * 判断id为4的客户是否存在
     */
    @Test
    public void testExists(){
        boolean exists = customerDao.exists(4L);
        System.out.println("id 为4的客户是否存在："+exists);

    }


    /**
     * getOne()
     *    需要在方法名上面+ @transactional 保证正常运行
     *
     * findOne():
     *      entityManager.find()  立即加载
     *
     * getOne():
     *      entityManager.getReference()  延迟加载
     *
     */
    @Test
    @Transactional
    public void testGetOne(){
        Customer customer = customerDao.getOne(4L);
        System.out.println(customer);

    }



}
