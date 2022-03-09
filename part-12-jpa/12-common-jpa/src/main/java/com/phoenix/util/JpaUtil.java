package com.phoenix.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * 解决实体管理器工厂的浪费资源和耗时问题
 * 通过静态代码块的形式：
 *  当程序第一次访问此工具的时候，创建一个公共的实体管理器工厂对象
 */
public class JpaUtil {

    private static EntityManagerFactory entityManagerFactory;

    static{
        //1.加载配置文件创建工厂（实体管理器工厂）对象
        //persistenceUnitName 就是persistence.xml中配置的name
        entityManagerFactory = Persistence.createEntityManagerFactory("myJpa");

    }

    /**
     * 获取EntityManager对象
     */
    public static EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }

}
