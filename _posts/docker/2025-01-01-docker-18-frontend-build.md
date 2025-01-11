---
layout: single
title:  "프론트엔드 빌드(feat. React)"
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

## 프론트엔드 이미지 빌드 과정 - react
1. 우선 베이스 이미지로 OS위에 nginx가 설치된 이미지를 사용한다.
2. 빌드 도구인 node.js 를 설치한다.
3. 소스 코드를 다운로드 받는다.
4. 의존성 라이브러리를 다운로드 및 설치한 후 소스 코드를 빌드한다.
5. 빌드 결과물을 Nginx의 기본 웹 루트 경로인 /usr/share/nginx/html에 복사한다.
6. 웹 서버를 실행한다.

>아래 예시 이미지는 vuejs 예시이다. react 빌드 파일은 build 디렉터리에 생성된다.
<img src="https://github.com/user-attachments/assets/24510cd6-4ff3-45cf-8fa6-72ccbc940199" width="40%" height="80%"/>

위 과정은 `싱글 스테이지 빌드(single-stage builds)` 과정이다. 
백엔드 애플리케이션을 싱글 스테이지로 빌드하면 최종 만들어지는 이미지 용량의 크기가 굉장히 커진다.
실제 애플리케이션 실행에 필요한 정보 뿐만 아니라 실제 애플리케이션 실행에 필요없는 전체 소소 코드를 포함하고 있기 때문이다.
따라서 `멀티 스테이징 기술`을 사용하여 애플리케이션에 필요한 정보만 담은 이미지를 만드는 것이 좋다.




<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>


## 프론트엔드 애플리케이션 멀티 스테이지 빌드
멀티 스테이지 빌드에서는 프론트엔드 애플리케이션의 빌드를 크게 두 부분으로 나눌 수 있다.
- 의존성 라이브러리의 다운 및 설치, 소스 코드를 빌드하는 과정
- 생성된 빌드 파일을 웹 서버에 복사 후 실행하는 과정

<br>

**첫 번째 빌드**  
1. 베이스 이미지는 빌드 도구인 node 이미지를 사용한다.
2. 소스 코드를 복사 또는 다운로드 받는다.
3. 의존성 라이브러리를 다운로드 및 설치한다.
4. 소스 코드를 빌드한다.

**두 번째 빌드**  
1. 베이스 이미지는 프론트엔드 실행을 위한 nginx 이미지를 사용한다.
2. 첫 번째 빌드에서 생성한 빌드 파일을 웹 서버 루트 경로에 복사한다.
3. 웹 서버를 실행한다.


>아래 예시 이미지는 vuejs 예시이다. react 빌드 파일은 build 디렉터리에 생성된다.
<img src="https://github.com/user-attachments/assets/64fac6e9-6e13-4480-8aed-13e119ee3a88" width="80%" height="80%"/>


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## Dockerfile 작성
- node 기반의 프론트엔드 프로젝의 경우 빌드 시 의존성 라이브러리의 다운로드 및 설치 과정이 필요하다.
- 해당 과정이 이미지 빌드의 총 시간에 많은 영향을 주어 때론 너무 많은 시간이 소요된다.
- 빌드 시간을 줄이기 위한 방법이 필요하다.
  1. 캐시를 사용하는 경우 : [빌드 속도 개선 방법](https://midobandev.github.io/docker/docker-20-buildkit-mount/)
  2. 레지스트리 변경 : `RUN npm config set registry https://registry.npmmirror.com` 추가

```dockerfile
# 소스 다운로드 스테이지 분리
FROM alpine/git as source
WORKDIR /app
# 소스 코드 다운로드
RUN git clone <github 주소> .

#----------------------------------------------------------------------

# 패키지 설치 스테이지 분리
# 베이스 이미지를 분리하면 변경된 소스 코드의 영향을 받지 않는다.
FROM node:22.11.0 as dependencies
WORKDIR /app

# 패키지 레지스트리 저장소 변경.
RUN npm config set registry https://registry.npmmirror.com

COPY --from=source /app/package*.json ./

# BuildKit 캐시 마운트 사용하여 캐시 저장
# maxsockets 10 으로 동시 다운로드 수 제한
RUN npm install --maxsockets 10

#----------------------------------------------------------------------

# 빌드 스테이지 분리
FROM node:22.11.0 as build
WORKDIR /app

COPY --from=source /app .
 
COPY --from=dependencies /app/node_modules ./node_modules

RUN npm run build


#----------------------------------------------------------------------

# 실행 스테이지 분리
FROM nginx:alpine

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

# 빌드 결과물을 nginx의 서비스 디렉토리로 복사
COPY --from=build /app/build/ /usr/share/nginx/html/

# 80번 포트 노출
EXPOSE 80

# nginx 실행
CMD ["nginx", "-g", "daemon off;"]
```


## 빌드 
```dockerfile
$ docker build -t front-img .
```



<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<!--<img src="" width="80%" height="80%"/>-->

