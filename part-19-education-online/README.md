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
* nginx配置 \
  详见nginx.conf
* nuxt.js \
  nuxt.js有标准的目录结构，官方提供了模板工程，可以模板工程快速创建nuxt项目。
  模板工程地址：https://github.com/nuxt-community/starter-template/archive/master.zip
