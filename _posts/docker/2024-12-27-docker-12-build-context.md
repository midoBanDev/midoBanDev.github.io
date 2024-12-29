---
layout: single
title:  "빌드 컨텍스트(Build Context)"
categories:
  - Docker
tags:
  - [Docker, Build-Context]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## 빌드 컨텍스트
이미지 빌드 방식은 도커 데몬이 Dockerfile에 작성된 내용을 수행한 결과로 이미지를 생성한다. 
이를 위해 도커 클라이언트는 도커 데몬에게 Dockerfile과 빌드에 사용되는 파일들을 전달해 주어야 하는데 이때 Dockerfile이 있는 폴더 자체를 넘겨 준다.
이렇게 도커 데몬에게 전달된 폴더를 `빌드 컨텍스트`라고 한다.

도커 데몬은 전달 받은 컨텍스트안에 있는 도커 파일로 이미지를 빌드한다. 
그리고 도커 데몬은 빌드 컨텍스트에 있는 파일만 카피 명령으로 복사할 수 있다.

`docker build` 명령을 실행할 때 맨 뒤에 `Dockerfile경로`를 입력하는데 이 경로가 빌드 컨텍스트를 지정하는 것이다.
정리하자면 빌드 컨텍스트는 도커 데몬이 이미지를 빌드할 때 전달되는 폴더이고 이 폴더 안에 도커 파일과 카피에 사용할 파일들이 모두 들어있어야 한다.


### .dockerignore 파일
`.dockerignore` 파일은 빌드 컨텍스트에 있는 파일을 제외할 수 있는 기능을 제공한다.
예를 들어서 빌드 컨텍스트에 largejunk라는 3GB 크기의 파일이 있다고 가정해 보자. 
이 파일이 이미지 빌드에 필요하지 않은 파일인 경우 .dockerignore 파일 내용에 전달하고 싶지 않은 파일명을 적어 놓으면 
빌드 컨텍스트 폴더에 있더라도 실제 도커 데몬에는 이 파일을 제외하고 전달할 수 있다.

### Dockerfile 관리
Dockerfile은 별도의 폴더에서 관리되는 것이 좋다.
예를 들어서 Dockerfile이 C 드라이브의 최상단 폴더에 있게 되면 이 Dockerfile이 포함된 C드라이브 전체가 빌드 컨텍스트가 되어버린다.
이 빌드 컨텍스트는 도커 데몬에게 전달돼야 하기 때문에 빌드 컨텍스트의 크기가 커질수록 전송 시간이 길어지고 이 폴더의 크기가 비정상적으로 커지면 빌드에 문제가 발생할 수도 있다. 
그래서 Dockerfile과 빌드에 사용되는 파일만 별도의 폴더로 관리해야 한다.
<img src="https://github.com/user-attachments/assets/2070915a-bedd-49b7-90d0-2a4cb73ac2fc" width="80%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 임시 컨테이너와 새로운 레이어
Dockerfile 내에서 사용되는 명령어 중에 실제 컨테이너가 필요하거나 컨테이너 내부에 접근해야만 수행할 수 있는 명령어들이 존재한다.
이를 위해 도커 데몬은 빌드 중 `임시 컨테이너`를 생성한다. 

도커 데몬은 컨테이너가 필요한 명령이 감지되면 FROM에 지정한 베이스 이미지의 환경을 기반으로 임시 컨테이너를 생성한다.
이 후 생성되는 임시 컨테이너는 기존 레이어의 누적된 환경으로 실행된다.

### 임시 컨테이너 생성 명령어
임시 컨테이너를 생성하는 명령에는 `COPY`, `ADD`, `RUN`, `ENTRYPOINT`, `CMD`, `WORKDIR`, `ENV`, `VOLUME`, `USER`, `ONBUILD` 이 있다.
도커 데몬은 위 명령어 들이 실행될 때마다 임시 컨테이너를 생성하고 명령의 수행이 완료되면 임시 컨테이너를 삭제한다.

### 임시 컨테이너 생성 및 새로운 레이어 생성 명령어
위 명령어 중 `COPY`, `RUN`, `ADD`, `WORKDIR`, `USER`, `ENV` 명령어는 새로운 레이어를 생성한다. 모두 기존 레이어의 변경을 필요로하는 명령어들이다.
도커의 이미지 레이어는 불변이기 때문에 이 명령어들이 실행되면 새로운 레이어를 생성해서 저장해야 한다. 

예를 들어 COPY 명령은 베이스 이미지의 파일 시스템에 접근해야 하기 때문에 컨테이너가 필요하다. 
이를 위해 도커 데몬은 임시 컨테이너를 생성해준다.
COPY 명령이 완료되면 수행 결과를 새로운 레이어에 Commit 된다.
이후 임시 컨테이너는 도커 데몬에 의해 종료되고 삭제된다.

<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<!--<img src="" width="80%" height="80%"/>-->
