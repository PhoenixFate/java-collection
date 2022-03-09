package com.bytesRoom;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrudApplication.class)
@Slf4j
public class LogTest {

    private static final Logger logger= LoggerFactory.getLogger(com.bytesRoom.LogTest.class);

    @Test
    public void test01(){
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
    }

    @Test
    public void test02(){
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
    }
}
