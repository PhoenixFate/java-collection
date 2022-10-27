# 指定基础镜像 
# 你的开发环境是jdk8,就使用下面命令
#FROM openjdk:8-jdk-alpine
# openjdk9 在18年3月分开始不支持了, 支持从11开始，如果说你使用jdk1.9，则使用jdk14
# FROM openjdk:14.0-jdk

FROM openjdk:8-alpine

# 修改源 修改时间为东八区时间
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
RUN apk update \
    && apk add ttf-dejavu libuuid tzdata \
    && cp -r -f /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata && rm -rf /var/cache/apk/*

# 项目环境变量
#ENV SPRING_PROFILES_ACTIVE=dev

# 复制文件
ADD dream-blog-*.jar app.jar

# 这个可以不写，主要是记录当前镜像需要对外暴露的端口，而非限制端口
EXPOSE 7001

# Docker容器启动时执行的命令，注意：是 -D 开头。如果有换行，后面要加上英文状态的 \ 进行连接
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar", \
#    "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "/app.jar"]

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]