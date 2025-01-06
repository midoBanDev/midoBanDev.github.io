---
layout: single
title:  "환경 별 Spring Boot application.yml 설정(feat. Docker)"
categories:
  - Quartz
tags:
  - [Quartz, Spring, SpringBoot, H2]

toc: true
toc_sticky: true
---

## 1. 프로젝트 의존성 추가

- 운영은 `postgreSQL`, 개발은 `H2 DB`를 사용하려고 합니다.

`build.gradle` 파일에 H2 데이터베이스 의존성을 추가합니다:

```gradle
dependencies {

    // 기존 의존성...

    // 운영 환경
	  implementation 'org.postgresql:postgresql:42.7.1'

    // 개발 환경경
    runtimeOnly 'com.h2database:h2'
    
}
```

## 2. application.yml 설정

```yaml
spring:
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:local}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect

---
# 로컬 개발 환경 (PostgreSQL 사용)
spring:
  config:
    activate:
      on-profile: local
  datasource:
    url: jdbc:postgresql://${DB_URL:loan-db}:${DB_PORT:5432}/${DB_NAME:mydb}
    username: ${DB_USERNAME:loan-user}
    password: ${DB_PASSWORD:loan-1234}
    driver-class-name: org.postgresql.Driver

---
# 테스트 환경 (H2 사용)
spring:
  config:
    activate:
      on-profile: dev
  datasource:
    url: jdbc:h2:mem:testdb;MODE=PostgreSQL
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.H2Dialect

---
# 운영 환경 (PostgreSQL 사용)
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: jdbc:postgresql://${DB_URL}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver
```

## 3. Dockerfile 설정

```dockerfile
# 빌드 이미지로 Gradle:8.11.1 & eclipse-temurin:17로 지정
FROM gradle:8.11.1-jdk17 AS builder

# apt-get update로로 Debian/Ubuntu 기반 리눅스 시스템에서 패키지 목록을 최신화 & Git 설치
RUN apt-get update && apt-get install -y git

# 프로젝트 클론
WORKDIR /app
RUN git clone https://github.com/midoBanDev/loan-manager-api.git .

# 또는 로컬 코드 베이스 정보 카피
#RUN . .

# 빌드
RUN gradle build

# 실행 스테이지
FROM eclipse-temurin:17-jre

# 애플리케이션을 실행할 작업 디렉토리를 생성
WORKDIR /app

# 빌드 이미지에서 생성된 JAR 파일을 런타임 이미지로 복사
COPY --from=builder /app/build/libs/loan-manager-api-0.0.1-SNAPSHOT.jar loan-manager-api.jar

EXPOSE 8080 
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "loan-manager-api.jar"]

```

## 4. 주요 설정 설명

### 프로파일 구성
- `local`: 로컬 개발 환경용 프로파일 (PostgreSQL 사용)
- `dev`: 개발 환경용 프로파일 (H2 사용)
- `prod`: 운영 환경용 프로파일 (PostgreSQL 사용)

### H2 데이터베이스 설정 특징
1. `MODE=PostgreSQL`: H2를 PostgreSQL 호환 모드로 실행
2. `mem:testdb`: 인메모리 데이터베이스 사용
3. H2 콘솔 활성화: `/h2-console`에서 접근 가능

### Dockerfile 멀티 스테이지 빌드
1. Builder 스테이지:
   - Gradle을 사용하여 애플리케이션 빌드
   - dev 프로파일로 빌드하여 H2 데이터베이스 사용
2. Run 스테이지:
   - 최소한의 런타임 환경만 포함
   - local 프로파일로 실행하여 PostgreSQL 사용

## 5. 사용 방법

### 로컬 개발 시
```bash
# 로컬 프로파일로 실행
./gradlew bootRun -Dspring.profiles.active=local
```

### 테스트 실행 시
```bash
# dev 프로파일로 개발 실행
./gradlew test -Dspring.profiles.active=dev
```

### Docker 빌드 및 실행
```bash
# 이미지 빌드
docker build -t loan-api .

# 컨테이너 실행 (loan-network 네트워크 사용)
docker run --network=loan-network -p 8080:8080 loan-api
```

## 6. 주의사항

1. 테스트 데이터베이스 초기화:
   - H2는 인메모리 데이터베이스이므로 애플리케이션 재시작 시 데이터가 초기화됩니다.
   - 필요한 경우 `data.sql`이나 `schema.sql`을 사용하여 초기 데이터를 설정할 수 있습니다.

2. 방언(Dialect) 설정:
   - 테스트 환경에서는 H2Dialect를 사용하지만, 실제 환경에서는 PostgreSQLDialect를 사용합니다.
   - 이로 인해 일부 데이터베이스 특정 기능에서 차이가 발생할 수 있습니다.

3. 환경변수:
   - 운영 환경에서는 반드시 필요한 데이터베이스 환경변수를 설정해야 합니다.
   - 기본값은 개발 편의를 위한 것이므로 운영 환경에서 사용하지 않습니다.