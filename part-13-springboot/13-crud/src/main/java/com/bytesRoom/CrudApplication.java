package com.bytesRoom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * spring 默认静态资源路径：
 * - classpath:/META-INF/resources/
 * - classpath:/resources/
 * - classpath:/static/
 * - classpath:/public
 */
@SpringBootApplication
// mapper的位置
@MapperScan("com.bytesRoom.mapper")
@Slf4j
public class CrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudApplication.class, args);
    }


    /**
     * @param dataSource
     * @return
     */
    // @Bean
    // public ApplicationRunner runner(DataSource dataSource) {
    //     return args -> {
    //         log.info("dataSource: {}", dataSource);
    //         Connection connection = dataSource.getConnection();
    //         log.info("connection: {}", connection);
    //     };
    // }

}
