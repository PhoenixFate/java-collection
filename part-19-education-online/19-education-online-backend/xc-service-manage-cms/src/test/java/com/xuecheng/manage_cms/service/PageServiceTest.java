package com.xuecheng.manage_cms.service;

import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {

    @Autowired
    private CmsPageService cmsPageService;

    @Test
    public void testPageService() throws TemplateException, IOException {
        String pageHtml = cmsPageService.getPageHtml("6235f46781ff98431c6d2919");
        System.out.println(pageHtml);

    }


}
