---
layout: single
title:  "[Docker] 도커 컴포즈 활용"
categories:
  - Docker
tags:
  - [Docker, Compose]

toc: true
toc_sticky: true
---

<br>

FRONT, API, DB 서버로 구성된 어플리케이션을 도커 컴포즈(Docker Compose)를 사용하여 로컬 환경에 구축해보려고 한다. 
각 프로젝트에 작성된 `Dockerfile`를 기준으로 compose.xml에 작성할 예정이다. 

도커 컴포즈(Docker Compose)를 위해 별도의 `compose-mng` 프로젝트를 생성했다. 
그리고 각 프로젝트의 소스 파일은 github에 저장되어 있다.

우선은 compose.xml를 단일 파일로 구성하여 진행한다. 
그리고 성공 시 Override 기능을 통해 환경 별 구성도 진행해 볼 예정이다.

## compose-mng 디렉터리 구조
compose.xml 관리를 위해 별도의 프로젝트를 생성했다. 
해당 프로젝트는 github으로 관리하지는 않았다.

```plaintext
compose-mng/
  ├── loan-db/
  │     ├── config/
  │     │     ├── conf.d/
  │     │     │     ├── logging.conf
  │     │     │     ├── performance.conf
  │     │     │     └── security.conf
  │     │     ├── custom.dev.conf
  │     │     └── custom.prod.conf
  │     ├── init/  
  │     │     └── 01-init.sql
  │     └── Dockerfile
  │
  ├── loan-api/
  │     └── Dockerfile
  │
  ├── loan-front/
  │     ├── docker-entrypoint.sh
  │     ├── nginx.conf
  │     └── Dockerfile
  │
  ├── compose.xml
  └── .env  
```


## DATABASE - Dockerfile
- postgresql 13 

```dockerfile
FROM postgres:13

RUN apt-get update && apt-get install -y \
    iputils-ping \
    net-tools \
    curl \
    vim \
    && rm -rf /var/lib/apt/lists/*

# 환경 변수 설정
ENV POSTGRES_USER=loan-user
ENV POSTGRES_PASSWORD=loan-1234
ENV POSTGRES_DB=mydb
ENV TZ=Asia/Seoul

# 환경별 변수
ARG ENV=dev

# 설정 파일 복사
COPY ./config/custom.${ENV}.conf /etc/postgresql/custom.conf
# conf.d에 있는 파일이 custom.conf에 include 되어 있기 때문에 파일 복사 이후 컨테이너 내부 경로도 문제가 없도록 맞춰주어야 한다.
COPY ./config/conf.d/ /etc/postgresql/conf.d/


# 초기화 스크립트 복사
COPY ./init/01-init.sql /docker-entrypoint-initdb.d/

# 볼륨 설정(익명볼륨)
VOLUME ["/var/lib/postgresql/data"]

# 포트 설정
EXPOSE 5432

# 설정 파일 적용
CMD ["postgres", "-c", "config_file=/etc/postgresql/custom.conf"]

```


## API - Dockefile
```dockerfile
# 빌드 이미지로 Gradle:8.11.1 & eclipse-temurin:17로 지정
FROM gradle:8.11.1-jdk17 AS builder

# apt-get update로로 Debian/Ubuntu 기반 리눅스 시스템에서 패키지 목록을 최신화 & Git 설치
RUN apt-get update && apt-get install -y git

# 프로젝트 클론
WORKDIR /app
RUN git clone https://github.com/midoBanDev/loan-manager-api.git .

# 또는 로컬 코드 베이스 정보 카피
#COPY . .

# gradlew 파일에 실행 권한 부여
RUN chmod +x gradlew
RUN ./gradlew build
# 빌드
#RUN gradle build


# 런타임 이미지로 eclipse-temurin:17-jre 사용
FROM eclipse-temurin:17-jre

# 기본 유틸리티 설치
# apt-get update: 패키지 목록을 최신 상태로 갱신.
# apt-get install: 필요한 패키지를 설치.
# rm -rf /var/lib/apt/lists/*: 패키지 설치 후 더 이상 필요 없는 캐시를 삭제.
RUN apt-get update && apt-get install -y \
    iputils-ping \
    net-tools \
    curl \
    vim \
    && rm -rf /var/lib/apt/lists/*

# 애플리케이션을 실행할 작업 디렉토리를 생성
WORKDIR /app

#COPY entrypoint.sh /etc/google/entrypoint.sh
#RUN chmod +x /etc/google/entrypoint.sh

ARG GOOGLE_CLIENT_ID
ENV GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}

# 빌드 이미지에서 생성된 JAR 파일을 런타임 이미지로 복사
COPY --from=builder /app/build/libs/loan-manager-api-0.0.1-SNAPSHOT.jar loan-manager-api.jar

EXPOSE 8080 
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "loan-manager-api.jar"]
```

## FRONTEND - Dockefile

```dockerfile
# 소스 다운로드 스테이지
FROM alpine/git as source
WORKDIR /app
# 소스 코드 다운로드
RUN git clone https://github.com/midoBanDev/loan-manager-front.git .

#----------------------------------------------------------------------

# 패키지 설치 스테이지
FROM node:22.11.0 as dependencies
WORKDIR /app

# 패키지 레지스트리 저장소 변경.
RUN npm config set registry https://registry.npmmirror.com

COPY --from=source /app/package*.json ./

# BuildKit 캐시 마운트 사용하여 캐시 저장
RUN --mount=type=cache,target=/root/.npm,id=npm \
    npm install --maxsockets 10

#----------------------------------------------------------------------

# 빌드 스테이지 분리
FROM node:22.11.0 as build
WORKDIR /app

COPY --from=source /app .
 
COPY --from=dependencies /app/node_modules ./node_modules

# 빌드 시 build-arg 로 환경변수 전달
# GOOGLE_CLIENT_ID는 빌드 전에 전달되어야 한다.
# node 기반의 프로젝트는 Java 기반 프로젝트와 다르게 빌드 전 환경변수를 설정한 후 빌드되어야 한다.
ARG REACT_APP_GOOGLE_CLIENT_ID
ENV REACT_APP_GOOGLE_CLIENT_ID=${REACT_APP_GOOGLE_CLIENT_ID}

RUN npm run build


#----------------------------------------------------------------------

# 실행 스테이지 분리
FROM nginx:alpine

# alpine 기반 이미지에서 패키지 설치
RUN apk update && apk add --no-cache \
    iputils \
    net-tools \
    curl \
    vim \ 
    envsubst

COPY nginx.conf /etc/nginx/conf.d/default.conf.template

# 환경변수 기본값 설정
ARG BACKEND_HOST
ARG BACKEND_PORT
ENV BACKEND_HOST=${BACKEND_HOST}
ENV BACKEND_PORT=${BACKEND_PORT}

# docker-entrypoint.sh 파일을 복사하고 실행 권한 부여
COPY docker-entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

# 빌드 결과물을 nginx의 서비스 디렉토리로 복사
COPY --from=build /app/build/ /usr/share/nginx/html/


# 80번 포트 노출
EXPOSE 80

# nginx 실행
ENTRYPOINT ["docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
```

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>


## 단일 파일 compose.xml

```yaml
networks:
  loan-backend-nw:
    external: true
  loan-api:
    external: true

volumes:
  postgres-data:

services:
  redis-server:
    image: redis
    networks:
      - loan-backend-nw
    restart: always

  loan-db:
    image: loan-postgres-img
    restart: on-failure
    depends_on:
      - redis-server
    networks:
      - loan-backend-nw
    volumes:
      - postgres-data:/var/lib/postgresql/data

  loan-api:
    build:
      context: ./loan-api/
    image: loan-api-img:1.0.0
    environment:
      - GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
    ports:
      - 8080:8080
    depends_on:
      - loan-db  
    restart: on-failure
    networks:
      - loan-backend-nw
      - loan-api

  loan-front:
    build:
      context: ./loan-front/
      args:
        - REACT_APP_GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
    image: loan-front-img:1.0.0
    environment:
      - BACKEND_HOST=${BACKEND_HOST}
      - BACKEND_PORT=${BACKEND_PORT}
    ports:
      - 3000:80
    depends_on:
      - loan-api  
    restart: on-failure
    networks:
      - loan-api
```


<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
