# Docker image for springboot file run
# VERSION 0.0.1
# Author: eangulee
# 基础镜像使用java
FROM ibalbal/arm64java:17
# 作者
MAINTAINER ibalbal <ibalbal@163.com>

ENV JAVA_OPTS="\
-server \
-Xmx512m  \
-Xms512m \
-Xmn512m \
-XX:SurvivorRatio=8 \
-XX:MetaspaceSize=256m \
-XX:MaxMetaspaceSize=512m \
-XX:+UseParallelGC \
-XX:ParallelGCThreads=4 \
-XX:+UseParallelOldGC \
-XX:+UseAdaptiveSizePolicy \
-XX:+PrintGCDetails \
-XX:+PrintTenuringDistribution \
-XX:+PrintGCTimeStamps \
-XX:+HeapDumpOnOutOfMemoryError \
-XX:HeapDumpPath=/heapdump.hprof \
-Xloggc:/gc.log \
-XX:+UseGCLogFileRotation \
-XX:NumberOfGCLogFiles=5 \
-XX:GCLogFileSize=10M"

#RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
#RUN echo "Asia/Shanghai" > /etc/timezone

# 将jar包添加到容器中并更名为app.jar
ADD summer-gateway.jar /app.jar
# 运行jar包
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=prod"]
ENTRYPOINT ["java","-jar","app.jar"]