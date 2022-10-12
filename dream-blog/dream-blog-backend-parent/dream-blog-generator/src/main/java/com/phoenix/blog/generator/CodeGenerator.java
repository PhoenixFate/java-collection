package com.phoenix.blog.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.phoenix.blog.common.util.DESUtil;

import java.util.Scanner;

// 执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    // 生成的代码放到哪个工程中
    private static final String PROJECT_NAME = "dream-blog-article";

    // 数据库名称
    private static final String DATABASE_NAME = "dream_blog_article";

    // 子包名
    private static final String MODULE_NAME = "article";

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://www.bytes-space.com:10306/" + DATABASE_NAME + "?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword(DESUtil.decryptString("If9MLzEu3wKNUS1OhkAnGg=="));
        mpg.setDataSource(dsc);

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir") + "/";
        gc.setOutputDir(projectPath + PROJECT_NAME + "/src/main/java");
        gc.setIdType(IdType.ASSIGN_ID); // 分布式id
        gc.setAuthor("phoenix");
        gc.setFileOverride(true); //覆盖现有的
        gc.setOpen(false); //是否生成后打开
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true); //实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.phoenix.blog"); //父包名
        pc.setController(MODULE_NAME + ".controller"); // com.mengxuegu.blog.aritcle.controller
        pc.setService(MODULE_NAME + ".service");
        pc.setServiceImpl(MODULE_NAME + ".service.impl");
        pc.setMapper(MODULE_NAME + ".mapper");
        pc.setXml(MODULE_NAME + ".mapper.xml");
        pc.setEntity("entity");//实体类存储包名 com.phoenix.blog.entity
        mpg.setPackageInfo(pc);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 类名：命名规则：将下划线变成驼峰大写
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 属性名: 命名规则：将下划线变成驼峰大写
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true); //使用lombok
        strategy.setEntitySerialVersionUID(true);// 实体类的实现接口Serializable
        strategy.setRestControllerStyle(true); // @RestController
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        //请求路径驼峰转斜杠： @RequestMapping("/managerUserActionHistory") -> @RequestMapping("/manager-user-action-history")
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix("blog_"); // 去掉表前缀
        mpg.setStrategy(strategy);

        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }


    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入").append(tip).append("：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

}