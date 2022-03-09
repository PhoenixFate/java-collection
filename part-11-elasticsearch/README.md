# ElasticSearch
## 安装server
1. 下载地址: 
- https://www.elastic.co/downloads/elasticsearch
2. 修改配置 
- 关闭ssl的：修改config/elasticsearch.yml里面的
    ```
    xpack.security.http.ssl enabled:false
    ```
- 关闭账号 默认账号elastic / changeme  :修改config/elasticsearch.yml里面的
    ```
    xpack.security.enabled:false
    ```
- 绑定端口：修改config/elasticsearch.yml里面的 
    ```
    network.host: 0.0.0.0
    ```
- 打开允许跨域：修改config/elasticsearch.yml里面的 添加:
    ```
    http.cors.enabled: true
    http.cors.allow-origin: "*"
    ```
3. 启动服务
- 双击bin/elasticsearch.bat or sh bin/elasticsearch.sh

## 安装客户端elasticsearch-head
1. 下载地址
   https://github.com/mobz/elasticsearch-head
2. 运行
    npm install & npm run start