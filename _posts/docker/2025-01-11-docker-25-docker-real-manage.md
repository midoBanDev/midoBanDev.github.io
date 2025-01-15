---
layout: single
title:  "[Docker] 도커 실무 관리"
categories:
  - Docker
tags:
  - [Docker, Stateless]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>


## 이미지 레이어 관리
빌드된 최종 이미지는 각각의 이미지 레이어들이 쌓여 만들어진다. 
이미지 레이어가 많아지면 빌드 속도가 느려지기 때문에 Dockerfile 작성 시 
적절한 지시어 관리를 통해 빌드되는 이미지를 최적화하는 것이 좋다.

### RUN 지시어 사용
-`RUN` 지시어는 새로운 레이어를 생성하기 때문에 `&&`를 활용해 최대한 하나로 처리하는 것이 좋다.
- 마지막에 `&&` 뒤에 `\` 역슬래시를 추가하여 줄바꿈 형태로 여러 명령어를 삽입할 수 있다.
  
```bash
# RUN 지시어를 5번 사용하여 5개의 이미지 레이어가 생성된다.
FROM ubuntu:latest
RUN apt-get update
RUN apt-get install -y curl
RUN apt-get install -y xz-utils
RUN apt-get install -y git
RUN apt-get clean

# RUN 지시어를 1번 사용하여 1개의 이미지 레이어가 생성된다.
FROM ubuntu:latest
RUN apt-get update && \
apt-get install -y curl && \
apt-get install -y xz-utils && \
apt-get install -y git && \
apt-get clean
```

### 이미지 크기 관리
이미지의 크기는 가능한 작은 것이 좋다. 
보통 현업에서 이미지를 빌드하고 푸시하고 배포하는 과정은 모두 각각 다른 기기에서 이루어진다.
1. 새로운 버전으로 빌드한 이미지를 레지스트리에 푸시한다. 
2. 이미지를 레지스트리로부터 다운받아서 컨테이너를 업그레이드한다.

이 과정에서 이미지는 네트워크를 통해서 업로드되고 다운로드되기 때문에 이미지의 크기가 작은 것이
배포속도와 네트워크 사용량에 더 유리하다.  

<img src="https://github.com/user-attachments/assets/55352d7b-a72c-472e-8b1d-59d9bdac7385" width="90%" height="80%"/>


**1. 애플리케이션의 크기를 줄인다.**  
애플리케이션 소스에 불필요한 기능들을 줄이고
하나의 큰 모듈을 여러 모듈로 분리해서 애플리케이션의 크기를 줄인다. 
애플리케이션의 크기가 줄어들면 이 애플리케이션에 포함하고 있는 이미지의 크기도 함께 줄어든다.

<br>

**2. 베이스 이미지를 작은 이미지로 선택한다.**  

**스크래치 이미지(scratch image)**  
극단적으로 사이즈를 줄이려면 모든 이미지의 가장 뿌리가 되는 이미지인 `스크래치 이미지(scratch image)`를 활용할 수 있다.
스크래치 이미지는 이미지를 빌드하기 위한 가장 최소한된 이미지로 아무것도 빈 상태의 이미지를 말한다. 
운영체제의 기본 패키지 조차 없는 완전한 빈 상태이기 때문에 초경량화된 이미지를 만들어야 하는 경우 사용할 수 있다.

**Alpine 이미지**  
Alpine 이미지는 매우 작고 가벼운 Linux 배포판인 Alpine Linux를 기반으로 만든 Docker 이미지다.   
주요 특징은 다음과 같다:  
- 기본 이미지 크기가 약 5MB로 매우 작다(Ubuntu나 CentOS는 보통 수백 MB)
- 적은 용량으로 인해 컨테이너 시작 시간이 빠릅니다
- 메모리 사용량이 적어 리소스 효율적이다

보안:
- 최소한의 패키지만 포함하여 공격 표면이 작다
- musl libc와 busybox를 사용하여 보안성을 높였다

Alpine 이미지는 apk라는 패키지 매니저를 사용한다
`apk add` 명령어로 필요한 패키지를 설치할 수 있다

```bash
FROM alpine:latest
RUN apk update && \
apk add --no-cache curl && \
apk add --no-cache xz && \
apk add --no-cache git
```
실제로 ubuntu 이미지를 베이스 이미지로 사용한 것과 alpine 이미지를 베이스 이미지로 사용한 경우를 비교해보면 
alpine 이미지가 훨씬 작은 용량을 차지하는 것을 볼 수 있다. 

<img src="https://github.com/user-attachments/assets/7d934404-ba61-4463-a5d2-e0ab958eeb40" width="90%" height="80%"/>


도커 허브에 이미지 상세를 봤을 때 태그에 alpine이 붙어 있는 이미지들이 alpine 이미지를 의미한다.

**3. .doekerignore 파일 사용**  
`.doekerignore` 파일을 사용해서 불필요한 파일이 이미지 안으로 들어가지 않도록 관리해야 한다.
보통 COPOY 명령을 사용할 때 단일 파일이 아니라 디렉터리 전체를 복사하는 방식으로 사용한다.

```bash
COPY . .
```

이렇게 카피 점점으로 해서 루트 디렉터리에 있는 파일을 빌드 컨텍스트로 모두 이동시키는 케이스가 많다.

```bash
COPY ./custom.conf /etc/conf.d/custom.conf
COPY ./init.sql /etc/init/init.sql
COPY ./index.html /var/share/nginx/html/
COPY ./src /app/
```
위와 같이 COPY 지시어를 여러 번 나누어서 사용하면 불필요한 레이어의 개수가 늘어날 수 있다. 따라서 **COPY 지시어는 그대로(COPY . .)** 사용하면서
.doekerignore 파일을 사용해서 **빌드 컨택스트로 이동할 파일을 관리**하는 것이 좋다.


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>


## 3 Tier 아키텍처 구성
일반적으로 엔터프라이즈 웹 애플리케이션은 3가지 종류의 서버로 구성된다.
- 프론트엔드 소스를 제공하는 웹 서버
- 비즈니스 로직을 수행하는 웹 애플리케이션 서버
- 데이터베이스 서버
이 3가지 종류의 서버가 유기적으로 상호작용하면서 하나의 애플리케이션으로 구성되는 것을 `3-Tier 아키텍처(architecture)`라고 부른다.

Tier는 하나의 단계를 의미한다. 
웹 애플리케이션 아키텍쳐에서는 클라이언트가 서비스를 사용하는 과정에서 서버가 크게 3가지 단계로 구성되어 있다는 것을 의미한다.


많은 엔터프라이즈 웹 애플리케이션이 이 3T와 아키텍터를 기반으로 구성되어 있고요.
3-Tier 애플리케이션에서 가장 먼저 클라이언트의 첫 번째 진입점 역할은 웹 서버가 담당한다.
웹 서버의 구성 요소인 프론트엔드는 백엔드 애플리케이션으로 요청을 보낸 원하는 작업을 처리한 후 응답해준 정보를 클라이언트에게 제공한다.

보통 프론트엔드 애플리케이션의 소스에는 백앤드 애플리케이션에 대한 API 정보가 적혀 있다.
이를 통해 클라이언트의 브라우저가 프론트엔드 소스 코드의 API 정보를 읽어 백엔드 서버의 요청을 보내게 된다.
이 뜻은 클라이언트(브라우저)가 직접 백앤드 서버에 요청하는 것을 의미한다. 

하지만 이렇게 사용자(클라이언트)가 백앤드 애플리케이션을 직접 접근하는 것은 좋지 않다.
백앤드 애플리케이션은 시스템과 실제 데이터에 밀접한 연관이 있기 때문에 보안에도 특히 주의해야 한다.
API가 노출되어 전체 시스템의 위험을 주는 일이 없도록 백앤드 애플리케이션은 일반 사용자가 직접 접근하는 것을 막는 것이 일반적이다.

웹 서버인 nginx의 프록시 기능을 활용하면 백앤드 애플리케이션의 접근을 제한할 수 있다.
프록시 구조를 만들면 백앤드 애플리케이션에 대한 요청은 웹 서버를 통해서만 접근이 가능하다.  
<img src="https://github.com/user-attachments/assets/cfd95fc5-1aae-4552-a57e-7da85e442eac" width="90%" height="80%"/>


프록시 설정을 하기 위해서는 nginx 이미지를 빌드할 때 nginx.conf 설정 정보를 변경하여 프록시 기능을 적용하면 된ㄷ.

```nginx
server {
    listen       80;
    server_name  _;
    
    location / {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
    }
    
    location /api/ {
        proxy_pass http://leafy:8080;
    }
    
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }
}
```

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## 동적 서버 구성
Nginx 서버 설정에 백엔드 애플리케이션의 주소가 고정되어 있으면 환경 별로 Nginx가 프록시 해야 하는 주소가 바뀔 때마다 프록시 설정의 주소를 바꾸기 위해 이미지 빌드를 다시 해야 한다.
웹 서버의 설정 정보의 엔드 포인트 API 주소 부분을 동적으로 변경되도록 하여 이런 문제에 대해 유연하게 대처할 수 있다. 
즉 환경 별로 달라지는 정보는 시스템 환경 변수로 처리하면 컨테이너 실행 시 결정할 수 있다.

**1. nginx.conf 파일 설정 변경**  
- nginx 프록시 설정 안에 백엔드 엔드 포인트 API를 환경 변수로 설정한다.
  
```nginx
location /api/ {
  proxy_pass http://${BACKEND_HOST}:${BACKEND_PORT};
}
```

**2. Dockerfile 작성**
- Dockerfile 내에 프로덕션 스테이지 빌드 시 환경변수를 전달받아 최종 nginx.conf 파일을 생성한다.
- 실제 nginx.conf 파일에 변수를 적용하는 역할은 **docker-entrypoint.sh** 파일이 담당한다.
  
```dockerfile
# 빌드 이미지로 node:14 지정 
FROM node:14 AS build

# .. 중간 생략

# 소스코드 빌드
RUN npm run build


# 프로덕션 스테이지
FROM nginx:1.21.4-alpine 
# nginx 설정 파일을 만들기 위한 템플릿 파일을 복사
COPY nginx.conf /etc/nginx/conf.d/default.conf.template

# 전달 받을 환경 변수를 설정
# ENV 환경변수명 기본값
ENV BACKEND_HOST loan-api
ENV BACKEND_PORT 8080

# 전달 받은 환경 변수를 nginx 설정파일에 적용하기 위한 스크립트 파일을 복사
COPY docker-entrypoint.sh /usr/local/bin/
# 스크립트 파일 실행 권한 부여
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

# 빌드 이미지에서 생성된 dist 폴더를 nginx 이미지로 복사
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

# 실제 실행 명령어 : docker-entrypoint.sh nginx -g daemon off;
# docker-entrypoint.sh 스크립트 파일의 인자로 nginx -g daemon off; 를 전달한다는 의미
ENTRYPOINT ["docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
```

**3. docker-entrypoint.sh 파일 작성**
- `docker-entrypoint.sh` 파일은 환경 변수를 전달받아 최종 nginx.conf 파일을 생성한 후 nginx를 실행하는 스크립트 파일이다.

```shell
#!/bin/sh

# 파일명 : docker-entrypoint.sh

# set -e는 에러가 발생하면 스크립트를 즉시 종료하도록 설정한다.
# 특정 명령이 실패할 경우, 그 이후의 명령은 실행되지 않는다.
set -e

# default.conf.template 파일에서 환경 변수를 대체하고 결과를 default.conf에 저장
# envsubst는 환경 변수 값을 텍스트에 삽입(substitute)하는 유틸리티이다.
# ENV BACKEND_HOST=api-server 와 같이 환경 변수가 설정되면(또는 빌드 시 -e 옵션을 통해 환경 변수를 전달 받으면)
# 본 파일 실행 시 envsubst가 템플릿 파일(/etc/nginx/conf.d/default.conf.template)에서 ${BACKEND_HOST} 부분을 찾아 설정된 값(api-server)으로 대체한다.
# 최종 결과는 /etc/nginx/conf.d/default.conf 파일에 저장된다.
envsubst '${BACKEND_HOST} ${BACKEND_PORT}' < /etc/nginx/conf.d/default.conf.template > /etc/nginx/conf.d/default.conf

# exec는 현재 쉘 프로세스를 대체하여 지정된 명령을 실행합니다.
# "$@"는 스크립트 실행 시 전달된 모든 인수를 그대로 전달합니다.
# 예를 들어, docker-entrypoint.sh nginx -g daemon off;이 실행 되었으면 docker-entrypoint.sh 파일 실행 후 전달 받은 인수 nginx -g "daemon off;"를 최종 실행한다.
exec "$@"
```

- `/usr/local/bin/`
`/usr/local/bin/` 경로 대부분 Linux 시스템에서 기본적으로 PATH 환경변수에 포함되어 있다. 
`PATH`는 쉘이 실행 가능한 파일을 찾는 디렉토리 목록이다. 
그래서 해당 경로에 있는 스크립트 파일을 전체 경로를 포함하지 않고 파일만으로 실행할 수 있다.

`docker-entrypoint.sh` 이렇게 파일만 실행해도 PATH 환경변수로 경로를 찾아 실제로 `/usr/local/bin/docker-entrypoint.sh` 로 실행된다.
<img src="https://github.com/user-attachments/assets/9fd640cc-6984-401c-b4b0-7eb796e6661c" width="60%" height="80%"/>




<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
