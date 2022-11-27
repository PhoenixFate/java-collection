package com.phoenix;

import com.google.common.annotations.VisibleForTesting;
import org.junit.Test;

/**
 * @Author phoenix
 * @Date 11/27/22 21:25
 * @Version 1.0
 */
public class VisibleForTest {

    @VisibleForTesting
    public void testPrivate() {
        System.out.println("test private");
    }
}
