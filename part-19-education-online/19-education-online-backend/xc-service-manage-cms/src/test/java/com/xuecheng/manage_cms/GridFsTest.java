package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTest {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    //用于打开下载流
    @Autowired
    private GridFSBucket gridFSBucket;

    //GridFs存文件
    @Test
    public void testGridFs() throws FileNotFoundException {
        //要存储的文件
        File file = new File("src/main/resources/templates/index_banner.ftl");
        System.out.println(file);
        //定义输入流
        FileInputStream inputStream = new FileInputStream(file);
        //向GridFs存储文件
        ObjectId objectId = gridFsTemplate.store(inputStream, "轮播图test005.txt");
        //得到文件id
        System.out.println(objectId.toString());
    }

    //GridFs取文件
    @Test
    public void queryFile() throws IOException {
        //根据文件id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("623a0fdab094c08b04c74fb8")));
        //打开一个下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建GridResource对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        //从流中区数据
        String s = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(s);
    }

    @Test
    public void deleteFile() {
        //根据文件id删除fs.files和fs.chunks中的记录
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is("623a0fdab094c08b04c74fb8")));
    }

    //上传课程模板 course.ftl
    @Test
    public void uploadCourseTemplate() throws FileNotFoundException {
        //要存储的文件
        File file = new File("src/main/resources/templates/course.ftl");
        System.out.println(file);
        //定义输入流
        FileInputStream inputStream = new FileInputStream(file);
        //向GridFs存储文件
        ObjectId objectId = gridFsTemplate.store(inputStream, "课程模板.ftl");
        //得到文件id
        System.out.println(objectId.toString());
    }

}
