---
layout: single
title:  "도커 용어"
categories:
  - Docker
tags:
  - [Docker, Words]

toc: true
toc_sticky: true
---

<br>


## 빌드 스테이지(Build Stage)
**빌드 스테이지(Build Stage)**는 Dockerfile에서 `FROM` 명령어로 시작되는 각각의 독립된 빌드 단계를 의미합니다.
멀티 스테이지 빌드에서 여러 스테이지를 사용하여 최종 이미지의 크기를 줄일 수 있습니다.

```dockerfile
FROM node:14 AS builder  # 첫 번째 스테이지
...
FROM nginx:alpine       # 두 번째 스테이지
```

<div style="padding-top:50px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:50px;"></div>


## 빌드 컨테이너
**빌드 컨테이너**는 Dockerfile의 각 명령어가 실행되는 임시 컨테이너를 말한다.
Docker는 각 명령어마다 새로운 임시 컨테이너를 생성하고, 명령어 실행이 완료되면 그 결과를 새로운 이미지 레이어로 저장한다. 
이 임시 컨테이너 내에서 WORKDIR로 지정된 디렉토리가 작업 디렉토리가 됩니다.


<div style="padding-top:50px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:50px;"></div>


## 멀티 스테이지 빌드
멀티 스테이지 빌드는 하나의 Dockerfile 안에서 여러 단계(스테이지)를 사용하여 최종 Docker 이미지를 생성하는 방법입니다. 주요 목적은 최종 이미지의 크기를 줄이는 것입니다.

```dockerfile
# 첫 번째 스테이지: 빌드 환경
FROM node:18 AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# 두 번째 스테이지: 실행 환경
FROM nginx:alpine
# builder 스테이지에서 빌드된 결과물만 복사
COPY --from=builder /app/build /usr/share/nginx/html
```
이 예시에서:

첫 번째 스테이지에서는 Node.js와 npm 패키지들을 포함한 큰 빌드 환경을 사용합니다.  
두 번째 스테이지에서는 가벼운 nginx 이미지만 사용하고, 첫 번째 스테이지에서 빌드된 결과물만 가져옵니다.

결과적으로 개발 도구나 빌드 종속성 없이, 실행에 필요한 파일들만 포함된 작은 크기의 최종 이미지를 얻을 수 있습니다.


<div style="padding-top:50px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:50px;"></div>


## 빌드 인자(Build Arguments)
Dockerfile에서 빌드 인자(Build Arguments)를 지정하는 방법은 크게 두 가지입니다

**ARG 명령어 사용**  
```dockerfile
# 빌드 인자 선언
ARG VERSION=latest
ARG USER=default-user

# 단순 변수 사용 가능.
FROM alpine:$VERSION

# 중괄호를 사용한 변수 사용을 권장함.
LABEL maintainer=${USER}

# 문자열과 결합
WORKDIR /home/${USER}/app
```

**빌드 시 명령줄에서 전달**  
```dockerfile
# 단일 인자 전달
docker build --build-arg VERSION=1.0 .

# 여러 인자 전달
docker build --build-arg USER=john --build-arg PORT=3000 .
```

**주요 특징**  
- `ARG 값은 빌드 시에만 사용 가능`하며, 컨테이너 실행 시에는 사용할 수 없습니다.  
- ARG는 ENV와 달리 빌드 시점에만 유효하며 최종 이미지에는 포함되지 않습니다.  
- 동일한 ARG를 여러 번 선언할 수 있으며, 마지막 값이 사용됩니다.  
- **FROM 명령어 이전에 ARG를 선언하면 FROM 명령어에서만 사용 가능합니다.** 


<div style="padding-top:50px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:50px;"></div>



<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<!--<img src="" width="80%" height="80%"/>-->
