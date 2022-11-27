package com.phoenix;

import org.junit.Test;

/**
 * @Author phoenix
 * @Date 11/27/22 21:27
 * @Version 1.0
 */
public class Test02 {

    @Test
    public void test01() {
        VisibleForTest visibleForTest = new VisibleForTest();
        //正常是无法访问私有方法的
        visibleForTest.testPrivate();
    }


}
