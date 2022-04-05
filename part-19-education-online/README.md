# 学成教育前后端代码
## 基础配置
* 配置域名 \
127.0.0.1 www.xuecheng.test \
127.0.0.1 eureka01 \
127.0.0.1 eureka02 \

* 单个微服务启动多个实例
```
eureka01 -> 127.0.0.1 ; eureka02 -> 127.0.0.1
idea->run->edit configurations:
eureka01: vm options: -DPORT=50101 -DEUREKA_SERVER=http://eureka02:50102/eureka/ -DEUREKA_DOMAIN=erueka01
eureka02: vm options: -DPORT=50102 -DEUREKA_SERVER=http://eureka01:50101/eureka/ -DEUREKA_DOMAIN=erueka02
```

* nginx配置
```
    upstream cms_server_pool {
        server 127.0.0.1:31001 weight=10;
    }
    server {
        listen       80;
        server_name  www.xuecheng.test;
        ssi on;
        ssi_silent_errors on;
        location / {
            alias   C:/Users/phoenix/IdeaProjects/java-collection/part-19-education-online/xc-ui-pc-static-portal/;
            index  index.html index.htm;
        }
        # 静态资源，包括系统所需要的图片、js、css等静态资源
        location /static/img/ {
            alias C:/Users/phoenix/IdeaProjects/java-collection/part-19-education-online/xc-ui-pc-static-portal/img/;
        }

        location /static/js/ {
            alias C:/Users/phoenix/IdeaProjects/java-collection/part-19-education-online/xc-ui-pc-static-portal/js/;
        }

        location /static/css/ {
            alias C:/Users/phoenix/IdeaProjects/java-collection/part-19-education-online/xc-ui-pc-static-portal/css/;
        }

        location /static/plugins/ {
            alias C:/Users/phoenix/IdeaProjects/java-collection/part-19-education-online/xc-ui-pc-static-portal/plugins/;
            add_header Access-Control-Allow-Origin http://ucenter.xuecheng.com;
            add_header Access-Control-Allow-Credentials true;
            add_header Access-Control-Allow-Methods GET;
        }
        #页面预览
        location /cms/preview/ {
            proxy_pass http://cms_server_pool/cms/preview/;
        }
        #反向代理
        location /static/company/ {
            proxy_pass http://static_server_pool;
        }

        location /static/teacher/ {
            proxy_pass http://static_server_pool;
        }

        location /static/stat/ {
            proxy_pass http://static_server_pool;
        }

        location /static/detail/ {
            proxy_pass http://static_server_pool;
        }
    }
    # 学成网静态资源
    server {
        listen 91;
        server_name localhost;
        # 公司信息
        location /static/company/ {
            alias C:/Users/phoenix/IdeaProjects/java-collection/part-19-education-online/static/company/;
        }
        # 老师信息
        location /static/teacher/ {
            alias C:/Users/phoenix/IdeaProjects/java-collection/part-19-education-online/static/teacher/;
        }
        # 统计信息
        location /static/stat/ {
            alias C:/Users/phoenix/IdeaProjects/java-collection/part-19-education-online/static/stat/;
        }
        # 课程详情
        location /course/detail/ {
            alias C:/Users/phoenix/IdeaProjects/java-collection/part-19-education-online/static/course/detail;
        }
    }

```