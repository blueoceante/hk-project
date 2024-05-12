FROM openjdk:17-jdk

COPY hkproject-1.0.0.jar /usr/app/
COPY application.yml /usr/app/

WORKDIR /usr/app

EXPOSE 8080

# 设置启动命令，运行你的应用
ENTRYPOINT ["java", "-jar", "hkproject-1.0.0.jar"]
