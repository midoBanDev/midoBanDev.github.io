---
layout: single
title:  "도커 실무 관리"
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



<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
