# ============================================
# Mass 后端服务 Dockerfile
# ============================================
# 多阶段构建：减少最终镜像体积
# ============================================

# 阶段1：构建阶段
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# 复制 pom.xml 和依赖文件
COPY pom.xml .
COPY mass-dependencies/pom.xml mass-dependencies/
COPY mass-framework/pom.xml mass-framework/
COPY mass-framework/mass-spring-boot-starter-security/pom.xml mass-framework/mass-spring-boot-starter-security/
COPY mass-framework/mass-spring-boot-starter-mybatis/pom.xml mass-framework/mass-spring-boot-starter-mybatis/
COPY mass-framework/mass-spring-boot-starter-redis/pom.xml mass-framework/mass-spring-boot-starter-redis/
COPY mass-framework/mass-spring-boot-starter-web/pom.xml mass-framework/mass-spring-boot-starter-web/
COPY mass-framework/mass-spring-boot-starter-mq/pom.xml mass-framework/mass-spring-boot-starter-mq/
COPY mass-framework/mass-common/pom.xml mass-framework/mass-common/
COPY mass-module-infra/pom.xml mass-module-infra/
COPY mass-module-system/pom.xml mass-module-system/
COPY mass-module-bulong/pom.xml mass-module-bulong/
COPY mass-module-ai/pom.xml mass-module-ai/
COPY mass-module-storage/pom.xml mass-module-storage/
COPY mass-server/pom.xml mass-server/

# 下载依赖（利用 Docker 缓存）
RUN mvn dependency:go-offline -B || true

# 复制源代码
COPY mass-framework mass-framework
COPY mass-module-infra mass-module-infra
COPY mass-module-system mass-module-system
COPY mass-module-bulong mass-module-bulong
COPY mass-module-ai mass-module-ai
COPY mass-module-storage mass-module-storage
COPY mass-server mass-server

# 构建项目
RUN mvn clean package -DskipTests -pl mass-server -am

# 阶段2：运行环境
FROM eclipse-temurin:21-jre-alpine

# 安装必要的工具
RUN apk add --no-cache curl tzdata

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo '$TZ' > /etc/timezone

# 创建应用目录
RUN mkdir -p /app

WORKDIR /app

# 从构建阶段复制 jar 文件
COPY --from=builder /app/mass-server/target/*.jar app.jar

# 创建 logs 目录
RUN mkdir -p /app/logs

# 配置 JVM
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=5 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
