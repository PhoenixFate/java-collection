package com.xuecheng.manage_course.dao;


import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsTemplateRepository extends MongoRepository<CmsTemplate, String> {

    CmsTemplate findByTemplateName(String templateName);

}
