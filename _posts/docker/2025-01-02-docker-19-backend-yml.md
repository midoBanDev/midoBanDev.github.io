---
layout: single
title:  "[Docker] 이미지 빌드 시 테스트 환경 구성(feat. application.yml)"
categories:
  - Docker
tags:
  - [Docker, Application.xml]

toc: true
toc_sticky: true
---


<br>

## 현재 상황 분석
현제 API 프로젝트에 `Srping REST Docs`를 적용 하였다.
Spring REST Docs은 테스트 기반 API 문서화 도구로써 빌드 시 테스트를 거쳐 통과한 경우 API 문서를 생성해 준다.

Local 환경에서는 `test/java/com/../` 밑에 application.xml 을 추가하여 일반(수동) 테스트, 빌드 테스트 모두 정상 작동이 되도록 하였다.
그런데 문제는 Docker 이미지 빌드 시 `gradle 빌드 시점`에는 `test/java/com/../application.xml`이 실행되지 않는다. 
테스트 결과 `main application.xml`이 실행된다. 방법을 모르는 것일 수도 있겠지..

<details>
<summary> 
<b><span>테스트 application.xml 내용</span></b>
</summary>

<div markdown="1">

```Yaml
spring:
  # Test Database Configuration
  datasource:
    url: jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password: 
    driver-class-name: org.h2.Driver

  # H2 Database Configuration
  h2:
    console:
      enabled: true
      path: /h2-console

  # OAuth2 Configuration
  security:
    oauth2:
      client:
        # client-id와 client-secret 값을 실제 OAuth 제공자에서 발급받은 값으로 교체해야 한다.
        # 운영 환경에서는 별도의 보안 저장소(예: AWS Secrets Manager, HashiCorp Vault 등)를 사용하는 것을 고려할 수 있다.
        registration:
          google:
            client-id: 331350555615-ojhbtis5pdvl8ajouq8ofa7b8o0t3ul4.apps.googleusercontent.com

  # JPA Configuration
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        highlight_sql: true
        use_sql_comments: true

  # Redis 설정 (Spring Boot 3.x 버전)
  data:
    redis:
      host: localhost
      port: 6379    

# JWT Configuration
jwt:
  secret: testsecretkey12345678901234567890
  expiration-seconds: 86400
  refresh-token-validity-in-milliseconds: 604800000

# Server Configuration
server:
  port: 8080

# Logging Configuration
logging:
  level:
    root: INFO
    com.gt: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql: TRACE    
```
</div>
</details>


이유는 다음과 같다고 받아드리고 해결 방법을 찾음(100% 확실하지는 않음)

로컬 환경에서 테스트 실행:
- IDE나 로컬 Gradle에 `src/test/resources`를 클래스패스에 추가되어 있다.
- 테스트 리소스가 메인 리소스보다 우선순위가 높게 설정되어 있다.
- 그래서 별도 설정 없이도 test/application.yml이 자동으로 인식된다.

<br>

Docker에서 Gradle 실행:

```dockerfile
FROM gradle:8.11.1-jdk17 AS builder
```
- Gradle Docker 이미지의 기본 환경에서 실행
- 클래스패스 구성이 로컬 환경과 다를 수 있음


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>


## 해결 방안
main application.xml에 환경별로 profile을 구성과 테스트 클래스에 activeprofile 설정을 통해 해결할 수 있었다. 
참고로 이 외에도 다양한 시도를 했고, 계속 실패를 했었다. 계속해서 test profiles가 active 되지 않는 문제를 겪었다.

시행착오 끝에 알아낸 원인은 이미지 빋드 시 도커 이미지 레이어 캐시가 내 발목을 잡고 있었다. 
사실 이 부분도 지금 생각해보면 엄밀히 말해 소스 코드 변경에 의해 캐시 이미지 레이어를 사용하지 않을텐데 당시에는 `--no-cache`로 해결되다 보니 그렇게 생각한 듯 하다.
왜냐면 test profiles을 active 시키기 위해 소스 코드를 매번 수정했기 때문이다. 그럼 도커의 이미지 레이어 규칙에 따라 새로운 레이어를 생성해야 하는데 당시에는 왜 안되었는지 모르겠다.

```bash
$ docker build --no-cache -t loan-manager-api .
```


### 1. test class에 `@ActiveProfiles("test")` 를 선언

- 처음에는 `-Dspring.profiles.active=test` 와 같이 환경 변수를 이용하려 했으나, `-D` 옵션이 Gradle 빌드 프로세스에는 전달되지만, 실제 테스트가 실행되는 JVM에는 전달되지 않는 것으로 보인다.
- `-D` 옵션은 local에서 빌드할 때, Dockerfile로 빌드할 때 모두 적용되지 않았다. 


- 참고로 `@ActiveProfiles("test")`를 선언하지 않아도 `export` 방법으로는 가능하다.

```bash
$ export SPRING_PROFILES_ACTIVE=test
$ ./gradlew build
```

### 2. appliation.xml을 다음과 같이 구성한다.

```yaml
spring:
  # 애플리케이션 이름
  application:
    name: loan-manager-api

  # 환경에 따라 프로파일 설정
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:local}

  #  show-sql: true: 실행되는 SQL 쿼리를 콘솔에 출력
  #  format_sql: true: SQL 쿼리를 보기 좋게 포맷팅
  #  highlight_sql: true: SQL 쿼리에 하이라이트 적용
  #  use_sql_comments: true: JPQL 주석도 함께 출력
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
        highlight_sql: true


---


# local 프로파일 설정
spring:
  config:
    activate:
      on-profile: local

  # PostgreSQL 연결 설정
  datasource:
    url: jdbc:postgresql://${DB_URL:loan-db}:${DB_PORT:5432}/${DB_NAME:mydb}
    username: ${DB_USERNAME:loan-user}
    password: ${DB_PASSWORD:loan-1234}
    driver-class-name: org.postgresql.Driver
  
  # 개발 도구 설정
  devtools:
    restart:
      enabled: true
    livereload:
      enabled: true


---

# test 프로파일 설정
spring:
  config:
    activate:
      on-profile: test
  
  # 테스트 데이터베이스 설정
  datasource:
    url: jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password: 
    driver-class-name: org.h2.Driver

  # h2 콘솔 설정
  h2:
    console:
      enabled: true
      path: /h2-console  



---


# prod 프로파일 설정
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: jdbc:postgresql://${DB_URL}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver


---


# 공통 설정
spring:
  # application.oauth.yml 파일을 포함
  profiles:
    include:
      - oauth

  # Redis 설정 (Spring Boot 3.x 버전)
  data:
    redis:
      host: ${REDIS_HOST:redis-server}
      port: ${REDIS_PORT:6379}

# JWT Configuration
jwt:
  secret: growtogether123456789012345678901234567890
  expiration-seconds: 86400
  refresh-token-validity-in-milliseconds: 604800000

# Logging Configuration
logging:
  #file:
  #  name: logs/application.log
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  level:
    root: INFO
    com:  
      gt: DEBUG
    org:
      springframework:
        web: INFO
        hibernate: debug
        SQL: debug
      hibernate:
        #SQL: debug
        type:
          descriptor:
            sql: TRACE

```


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>


## @ActiveProfiles("test")
Spring Boot에서 프로필 설정 `@ActiveProfiles("test")`은 두 가지 방식 모두 지원한다.

**1. 단일 파일 내 프로필 구분 (application.yml):**  

```yaml
yamlCopyspring:
  config:
    activate:
      on-profile: test
# 테스트 환경 설정...
---
spring:
  config:
    activate:
      on-profile: dev
# 개발 환경 설정...
```


**2. 파일 자체를 구분 (개별 파일):**  

- application.yml (공통 설정)
- application-test.yml (테스트 환경)
- application-dev.yml (개발 환경)

정리하면 @ActiveProfiles("test")는 아래 두 방식 모두에 적용된다.
- 단일 파일인 경우 -> on-profile: test인 영역을 활성화
- 개별 파일인 경우 -> application-test.yml 파일을 로드

하지만 Spring Boot는 실행 순서에 따라 설정 파일을 오버라이드 한다.
먼저 `application.yml`이 적용 되었다 하더라도 
나중에 `application-{profile}.yml`이 로드되면 해당 설정으로 오버라이드 된다.

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## Spring Boot의 설정 파일 로딩 순서
가장 기본이 되는 순서:

```plaintext
1. application.yml
2. application-{profile}.yml
```

더 자세한 전체 순서:

```plaintext
1. jar 외부의 application.yml
2. jar 외부의 application-{profile}.yml
3. jar 내부의 application.yml
4. jar 내부의 application-{profile}.yml
```

테스트 실행 시 추가되는 순서:

```plaintext
1. src/test/resources/application.yml
2. src/test/resources/application-{profile}.yml
3. src/main/resources/application.yml
4. src/main/resources/application-{profile}.yml
```

중요한 점은:
- 나중에 로드되는 설정이 이전 설정을 덮어쓴다.
- 같은 속성이 여러 곳에 정의되어 있다면 가장 마지막에 로드된 값이 최종 적용된다.
- 프로필 설정(application-{profile}.yml)은 항상 기본 설정(application.yml)을 오버라이드 한다.

예를 들어, 같은 속성이 여러 파일에 있다면:

```yaml
# application.yml
server:
  port: 8080

# application-test.yml
server:
  port: 8081
```
test 프로필이 활성화되면 최종적으로 port는 8081이 된다.


<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<!--<img src="" width="80%" height="80%"/>-->

