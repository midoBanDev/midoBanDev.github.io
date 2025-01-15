---
layout: single
title:  "[Docker] 멀티 스테이지 빌드(multi-stage builds)"
categories:
  - Docker
tags:
  - [Docker, Multi-Stage Builds]

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

<br>

## Java 애플리케이션
Java 기반의 Spring Boot 프레임워크로 개발되어 았는 애플리케이션을 멀티 스테이지 빌드를 해보자. 

자바로 개발된 소프트웨어는 jar나 war라는 파일로 소스 코드를 실행 가능한 아티팩트로 빌드할 수 있다.
jar 파일을 실행시키기 위해서는 `OS에 자바 런타임`이 설치되어 있어야 한다.
그리고 이 소스 코드를 애플리케이션으로 빌드하려면, `메이븐`이나 `Gradle`이라는 빌드 도구가 필요하다. 
여기서는 메이븐을 사용한다. 

<br>

### 싱글 스테이지 빌드 과정
자바 애플리케이션을 실행할 수 있는 환경을 만드는 것을 순서대로 정리 해보자.   
1. 먼저 OS 위에 자바 런타임과 메이븐을 설치한다.  
2. 소스코드를 다운로드 받는다.  
3. `mvn clean package`라는 maven 명령을 사용해서 애플리케이션 빌드를 실행한다.  
4. 빌드를 하면 애플리케이션 artifact 파일인 app.jar 파일이 만들어진다. `java -jar` 명령을 통해 jar 파일을 실행하면 이 애플리케이션을 실행시킨다.    
<img src="https://github.com/user-attachments/assets/02930c2e-aef0-4154-80a1-ec7ea5119701" width="40%" height="80%"/>

<br>

**Dockerfile 작성**  
파일명 : Dockerfile.singleStage  
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

# 실행 포트 명시(실제 실행될 포트를 설정하는 건 아님)
EXPOSE 8080

# 애플리케이션 실행
CMD ["java","-jar","app.jar"]
```

**도커 빌드**  
```bash
$ docker build -f Dockerfile.singleStage --name build-single .
```

**도커 실행**  
```bash
$ docker run -d -p 8080:8080 --name single-docker build-single
```

**컨테이너 접속**  
```bash
$ docker exec -it single-docker /bin/bash
```

<br>

### 멀티 스테이지 빌드 과정
싱글 스테이지 빌드는 실제 애플리케이션 실행에 필요하지 않은 이미지 레이어들이 많이 만들어진다.
실제로 메이븐 도구는 애플리케이션을 빌드할 때만 사용되고 애플리케이션이 실행에는 전혀 필요하지 않다.
그럼에도 메이븐 도구를 사용해야 하기 때문에 메이븐 도구의 큰 사이즈로 인해 실제 애플리케이션 이미지의 사이즈도 함께 커지게 된다.
이미지의 사이즈가 커지면 이미지를 전송하고 다운로드 받는 시간이 더 걸리게 되고 애플리케이션을 실행하는 시간에도 영향을 줄 수 있다.

멀티 스테이징 기술을 활용하면 애플리케이션을 빌드할 때는 메이븐 이미지를 사용하고 애플리케이션을 실행하는 이미지는 메이븐 이미지가 만들어낸 jar 파일만 가지고 만들수 있다.
애플리케이션을 빌드할 때는 Maven 도구와 소스 코드 정보만 필요하고, 애플리케이션을 실행할 때는 자바 런타임과 jar 파일만 있으면 된다.
따라서 전체 과정을 다음과 같이 두 단계로 쪼갤 수 있다.  
1. 첫 번째 단계에서는 소스 코드를 복사한 다음에 mvn clean 패키지 명령을 사용해서 jar 파일을 만드는 빌드하는 과정 수행한다.
2. 두 번재 단계에서는 애플리케이션을 실행에 필요한 자바 런타임만 있는 이미지를 가져온 다음에 앞서 빌드한 결과물에서 jar 파일만 복사해서 애플리케이션 실행에 사용한다.

이렇게 이미지를 빌드할 때 애플리케이션 빌드에 사용하는 `빌드 스테이지`와 이미지를 실행하는 `실행 스테이지` 두 개로 나누어서 빌드하는 방식을 `멀티 스테이지 빌드`라고 부릅니다.  
<img src="https://github.com/user-attachments/assets/d4e45170-537e-4f07-847f-0fbb6e9ea539" width="90%" height="80%"/>


<br>

**Dockerfile 작성** 
최종적으로 만들어지는 이미지는 `가장 마지막 FROM`(여기서는 두 번째 FROM) 이후의 이미지 레이어만 포함된다.  
파일명 : Dockerfile.multiStage  
```dockerfile
# 첫 번째 단계: 빌드 환경 설정
FROM maven:3.6 AS build

# 작업 디렉토리 생성
WORKDIR /app

# pom.xml과 src/ 디렉터리 복사
COPY pom.xml .
COPY src ./src

# 소스 코드 빌드: jar 파일 생성
RUN maven clean package

# 두 번째 단계: 빌드 환경 설정 - 이전 빌드 스테이지와 완전히 독립적이다.
FROM openjdk:11-jre-slim

# 작업 디렉터리 생성
WORKDIR /app

# 이전 빌드 스테이지에서 만든 jar 파일 복사
COPY --from=build /app/target/*.jar ./app.jar

# 실행 포트 명시
EXPOSE 8080

# 애플리케이션 실행
CMD ["java","-jar","app.jar"]
```

**도커 빌드**  
```bash
$ docker build -f Dockerfile.multiStage --name build-multi .
```

**도커 실행**  
```bash
$ docker run -d -p 8080:8080 --name multi-docker build-multi
```

**컨테이너 접속**  
```bash
$ docker exec -it multi-docker-docker /bin/bash
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 싱글 스테이지 빌드와 멀티 스테이지 빌드
싱글 스테이지 빌드를 사용했을 때는 이 이미지 안에 복사해온 소스 코드와 애플리케이션을 빌드할 때 다운 받았던 외부 라이브러리 파일들까지 모두 저장되기 때문에 불필요한 파일들이 많이 쌓인다.
그리고 빌드를 사용한 Maven 도구도 애플리케이션을 빌드할 때만 사용하지 애플리케이션을 실행할 때는 사용 되지 않기 때문에 애플리케이션을 실행하는 베이스 이미지도 불필요하게 커진 상태가 된다.  
<img src="https://github.com/user-attachments/assets/a4dff463-f6d3-4456-8f2f-ea0e1503a249" width="90%" height="80%"/>

그래서 싱글 스테이지 빌드를 두 가지 단계로 쪼개서 첫 번째 이미지는 빌드에 필요한 Maven만 포함되어 있는 Maven 이미지를 사용하고 
두 번째로 애플리케이션 실행에 사용하는 이미지는 자바 런타임만 포함되어 있는 OpenJDK 이미지를 사용했다. 

첫 번째 Maven 이미지에서 소스 코드를 복사하고 이 소스 코드를 애플리케이션으로 빌드한 다음 결과물로 만들어진 아티팩트인 app.jar 파일을 실행에 사용하는 OpenJDK 이미지로 복사해온 것이다. 
이렇게 복사를 해왔기 때문에 실제 결과물로 만들어지는 이미지에는 OpenJDK 이미지와 애플리케이션인 jar 파일만 남아있게 된다.

이렇게 멀티 스테이징 기술을 잘 활용하시면 애플리케이션 이미지의 크기를 크게 줄일 수 있다   
<img src="https://github.com/user-attachments/assets/c215ff7b-f9db-4302-8406-aec3d85e4002" width="90%" height="80%"/>

<img src="" width="80%" height="80%"/>
<!--<img src="" width="80%" height="80%"/>-->
