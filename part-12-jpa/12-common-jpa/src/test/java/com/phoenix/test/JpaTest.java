package com.phoenix.test;

import com.phoenix.pojo.Customer;
import com.phoenix.util.JpaUtil;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class JpaTest {
    /**
     * 测试jpa的保存
     * jpa的操作步骤
     *    1.加载配置文件创建工厂（实体管理器工厂）对象
     *    2.通过实体管理器工厂获取实体管理器
     *    3.获取事物对象，开启事务
     *    4.完成增删改查操作
     *    5.提交事务（回滚事务）
     *    6.释放资源
     */
    @Test
    public void testSave(){
        //1.加载配置文件创建工厂（实体管理器工厂）对象
        //persistenceUnitName 就是persistence.xml中配置的name
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myJpa");
        //2.通过实体管理器工厂获取实体管理器
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        //3.获取事物对象，开启事务
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Customer customer=new Customer();
        customer.setCustName("tomcat");
        customer.setCustIndustry("it");
        //4.完成增删改查操作
        entityManager.persist(customer);//保存操作
        //5.提交事务（回滚事务）
        transaction.commit();
        //6.释放资源
        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    public void testSaveByUtil(){
        //1.通过工具类获取entityManager对象
        EntityManager entityManager = JpaUtil.getEntityManager();
        //2.获取事物对象，开启事务
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Customer customer=new Customer();
        customer.setCustName("tomcat2");
        customer.setCustIndustry("it2");
        //3.完成增删改查操作
        entityManager.persist(customer);//保存操作
        //4.提交事务（回滚事务）
        transaction.commit();
        //5.释放资源
        entityManager.close();
    }


    /**
     * find：（立即加载）
     *    立即发送sql进行查询
     */
    @Test
    public void testFindById(){
        //1.通过工具类获取entityManager对象
        EntityManager entityManager = JpaUtil.getEntityManager();
        //2.获取事物对象，开启事务
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        /**
         * find 根据id查询数据
         *      class查询数据的结果需要包装的实体类类型的字节码
         *      id 查询的主键
         */
        Customer customer = entityManager.find(Customer.class, 2L);
        System.out.println(customer);

        //4.提交事务（回滚事务）
        transaction.commit();
        //5.释放资源
        entityManager.close();

    }

    /**
     * getReference: （延时加载）
     *      1.获得的对象是一个动态代理对象
     *      2.调用getReference不会立即发送sql语句查询数据库
     *          当调用结果对象的时候，才会发送sql进行查询
     */
    @Test
    public void testReferenceById(){
        //1.通过工具类获取entityManager对象
        EntityManager entityManager = JpaUtil.getEntityManager();
        //2.获取事物对象，开启事务
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        /**
         * find 根据id查询数据
         *      class查询数据的结果需要包装的实体类类型的字节码
         *      id 查询的主键
         */
        Customer customer = entityManager.getReference(Customer.class, 2L);
        System.out.println(customer);

        //4.提交事务（回滚事务）
        transaction.commit();
        //5.释放资源
        entityManager.close();

    }

    /**
     * jpa更新数据
     */
    @Test
    public void testUpdate(){
        //1.通过工具类获取entityManager对象
        EntityManager entityManager = JpaUtil.getEntityManager();
        //2.获取事物对象，开启事务
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Customer customer = entityManager.find(Customer.class, 2L);
        customer.setCustName("merge2");
        Customer merge = entityManager.merge(customer);
        System.out.println(merge);

        //4.提交事务（回滚事务）
        transaction.commit();
        //5.释放资源
        entityManager.close();
    }

    /**
     * jpa删除数据
     */
    @Test
    public void testRemove(){
        //1.通过工具类获取entityManager对象
        EntityManager entityManager = JpaUtil.getEntityManager();
        //2.获取事物对象，开启事务
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Customer customer = entityManager.find(Customer.class, 1L);
        entityManager.remove(customer);


        //4.提交事务（回滚事务）
        transaction.commit();
        //5.释放资源
        entityManager.close();
    }

}
