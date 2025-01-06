---
layout: single
title:  "프론트엔드 빌드"
categories:
  - Docker
tags:
  - [Docker]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## 백엔드 애플리케이션 이미지 빌드 과정
1. 우선 베이스 이미지로 OS위에 Java Runtime이 설치된 이미지가 필요하다.
2. 그리고 애플리케이션을 실행할 수 있는 상태로 만들기 위해 빌드 과정이 필요한데 이를 위해 빌드 도구를 설치해야 한다. 
3. 빌드 도구를 설치한 후 소스 코드를 다운로드 받는다.
4. 다운로드 받은 소스 코드를 가지고 빌드 과정을 거쳐 애플리케이션이 실행 가능한 상태인 jar 파일을 생성한다.
5. 그리고 생성된 Jar 파일을 `Java -jar` 명령어를 통해 애플리케이션을 실행한다. 

위 과정은 `싱글 스테이지 빌드(single-stage builds)` 과정이다. 
백엔드 애플리케이션을 싱글 스테이지로 빌드하면 최종 만들어지는 이미지 용량의 크기가 굉장히 커진다.
실제 애플리케이션 실행에 필요한 정보 뿐만 아니라 실제 애플리케이션 실행에 필요없는 전체 소소 코드를 포함하고 있기 때문이다.
따라서 `멀티 스테이징 기술`을 사용하여 애플리케이션에 필요한 정보만 담은 이미지를 만드는 것이 좋다.

<img src="https://github.com/user-attachments/assets/af0bbc46-e924-4788-8b75-4fe23630defb" width="40%" height="80%"/>


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>


## 백엔드 애플리케이션 멀티 스테이지 빌드
멀티 스테이지 빌드에서는 백엔드 애플리케이션의 빌드를 크게 두 부분으로 나눌 수 있다.
- 소스 코드를 빌드하여 jar 파일로 만드는 과정
- 생성된 jar 파일로 실제 애플리케이션을 실행하는 과정

<br>

**첫 번째 빌드**  
1. 베이스 이미지는 빌드 도구 이미지를 사용한다. 일반적으로 빌드 도구 이미지에는 JDK가 설치되어 있다.
2. 소스 코드를 다운로드 받는다.
3. 다운받은 소스 코드를 빌드하여 jar 파일을 생성한다.

**두 번째 빌드**  
1. 베이스 임지는 JDK 이미지를 사용한다.
2. 첫 번째 빌드에서 생성한 jar 파일을 가져온다.
3. jar를 실행하여 애플리케이션을 실행한다.

<img src="https://github.com/user-attachments/assets/b03778a2-e247-4824-a505-2250fad82efb" width="80%" height="80%"/>


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## Dockerfile 작성

- gradle 버전을 확인한다.
Gradle 프로젝트에서 Gradle 버전은 `gradle/wrapper/gradle-wrapper.properties` 파일에서 확인할 수 있다. 

```dockerfile
# 빌드 이미지로 Gradle:8.11.1 & eclipse-temurin:17로 지정
FROM gradle:8.11.1-jdk17 AS builder

# apt-get update로로 Debian/Ubuntu 기반 리눅스 시스템에서 패키지 목록을 최신화 & Git 설치
RUN apt-get update && apt-get install -y git

# 프로젝트 클론
WORKDIR /app
RUN git clone https://github.com/your-org/your-project.git .

# 또는 로컬 코드 베이스 정보 카피피
#RUN . .

# 빌드
RUN gradle build --no-daemon

# 실행 스테이지
FROM eclipse-temurin:17-jre

# 애플리케이션을 실행할 작업 디렉토리를 생성
WORKDIR /app

# 빌드 이미지에서 생성된 JAR 파일을 런타임 이미지로 복사
COPY --from=builder /app/build/libs/*.jar backend-app.jar

EXPOSE 8080 
ENTRYPOINT ["java", "-jar", "backend-app.jar"]
```

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## application.properties
- `${DB_URL:localhost}` DB_URL을 환경변수로 선언하고, 만약 DB_URL 변수에 값이 넘어오지 않으면 localhost를 사용한다는 의미다.
- 사용 방법은 `docker run -e DB_URL=<원하는url입력>` 으로 사용 가능하다.
```Yaml
# Postgresql Database
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://${DB_URL:localhost}:${DB_PORT:5432}/${DB_NAME:mydb}
spring.datasource.username=${DB_USERNAME:myuser}
spring.datasource.password=${DB_PASSWORD:mypassword}
```

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## 이미지 빌드
- 빌드 컨텍스트에서 실행하면 된다.
```bash
$ docker build -t my-backend .
```

<br>

## 컨테이너 실행
- 같은 네트워크를 사용하면 외부에 공개되지 않은 DB에 접근할 수 있다.
- 방법은 백엔드의 `application.properties(or application.yml)`에 `${DB_URL}`로 환경 변수를 설정하고 컨테이너 실행 시 `DB 컨테이너명`을 적용하면 된다.
```Yaml
spring.datasource.url=jdbc:postgresql://${DB_URL:localhost}:${DB_PORT:5432}/${DB_NAME:mydb}
```

- 네트워크 생성
```bash
# docker network create <네트워크명명>
$ docker network create my-network
```

- 컨테이너 실행
```bash
# docker run -d -p <외부포트:내부포트> --network <네트워크명> -e DB_URL=<DB 컨테이너명> --name <컨테이너명> <이미지명>
$ docker run -d -p 8080:8080 --network my-network -e DB_URL=<DB 컨테이너명> --name backend-app my-backend
```

<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<!--<img src="" width="80%" height="80%"/>-->

