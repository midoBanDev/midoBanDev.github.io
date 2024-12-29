---
layout: single
title:  "멀티 스테이지 빌드(multi-stage builds)"
categories:
  - Docker
tags:
  - [Docker, Multi-stage Builds]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## 멀티 스테이지 빌드(multi-stage builds)
멀티 스테이지 빌드는 도커 파일에서 두 개의 베이스 이미지를 활용하는 방법이다.
보통 애플리케이션을 빌드하는 과정에서 만들어지는 파일들은 용량을 많이 차지한다. 
이 파일들은 실제로 애플리케이션이 실행되는 데는 사용되지 않기 때문에 이미지 빌드에 사용하는 이미지와 실행에 사용하는 이미지로 나누는 것이다.
이 멀티 스테이지 빌드를 잘 활용하면 실제로 애플리케이션이 실행되는 이미지의 크기를 줄일 수 있다.

멀티 스테이지 빌드에 대해 예시를 통해 알아보자.


## Java 애플리케이션
Java 기반의 Spring Boot 프레임워크로 개발되어 았는 애플리케이션을 멀티 스테이지 빌드를 해보자. 

자바로 개발된 소프트웨어는 jar나 war라는 파일로 소스 코드를 실행 가능한 아티팩트로 빌드할 수 있다.
jar 파일을 실행시키기 위해서는 `OS에 자바 런타임`이 설치되어 있어야 한다.
그리고 이 소스 코드를 애플리케이션으로 빌드하려면, `메이븐`이나 `Gradle`이라는 빌드 도구가 필요하다. 
여기서는 메이븐을 사용한다. 

자바 애플리케이션을 실행할 수 있는 환경을 만드는 것을 순서대로 정리 해보자.   
1. 먼저 OS 위에 자바 런타임과 메이븐을 설치한다.  
2. 소스코드를 다운로드 받는다.  
3. `mvn clean package`라는 maven 명령을 사용해서 애플리케이션 빌드를 실행한다.  
4. 빌드를 하면 애플리케이션 artifact 파일인 app.jar 파일이 만들어진다. `java -jar` 명령을 통해 jar 파일을 실행하면 이 애플리케이션을 실행시킨다.    
<img src="https://github.com/user-attachments/assets/02930c2e-aef0-4154-80a1-ec7ea5119701" width="40%" height="80%"/>


### 싱글 스테이지 빌드 

파일명 : Dockerfile-singleStage  
```dockerfile
# 빌드 환경 설정
FROM maven:3.6-jdk-11

# 작업 디렉터리 설정
WORKDIR /app

# pom.xml과 src/ 디렉터리 복사
COPY pom.xml .
COPY src ./src/

# 소스 코드 빌드
RUN mvn clean package

# 빌드된 JAR 파일을 실행 환경으로 복사
RUN cp /app/target/*.jar ./app.jar

# 애플리케이션 실행
EXPOSE 8080
CMD ["java","-jar","app.jar"]
```

**도커 빌드**  
```bash
$ docker build -f Dockerfile-singleStage --name build-single .
```

**도커 실행**  
```bash
$ docker run -d -p 8080:8080 --name single-docker /bin/bash
```



<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<!--<img src="" width="80%" height="80%"/>-->
