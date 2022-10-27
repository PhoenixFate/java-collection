# 指定基础镜像 
# 你的开发环境是jdk8,就使用下面命令
FROM openjdk:8-jdk-alpine
# openjdk9 在18年3月分开始不支持了, 支持从11开始，如果说你使用jdk1.9，则使用jdk14
# FROM openjdk:14.0-jdk

# 修改源
RUN echo "http://mirrors.aliyun.com/alpine/latest-stable/main/" > /etc/apk/repositories && \
     echo "http://mirrors.aliyun.com/alpine/latest-stable/community/" >> /etc/apk/repositories

# 安装需要的软件，解决时区问题
RUN apk --update add curl bash tzdata && \
    rm -rf /var/cache/apk/* \

#修改镜像为东八区时间
ENV TZ Asia/Shanghai

# 环境变量
# 在创建容器时使用 -e 可改变值，如 -e SPRING_PROFILES_ACTIVE=dev

# 项目环境变量
ENV SPRING_PROFILES_ACTIVE=dev

# 复制文件
ADD dream-blog-*.jar app.jar

EXPOSE 8001

# Docker容器启动时执行的命令，注意：是 -D 开头。如果有换行，后面要加上英文状态的 \ 进行连接
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar", \
#    "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "/app.jar"]

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]