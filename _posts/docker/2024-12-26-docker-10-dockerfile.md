---
layout: single
title:  "[Docker] 도커파일(Dockerfile) 지시어"
categories:
  - Docker
tags:
  - [Docker, Dockerfile]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## 도커파일(Dockerfile) 지시어

### FORM, COPY, RUN, CMD

`FROM 이미지명`
- 베이스 이미지를 지정

`COPY 빌드컨텍스트경로 레이어경로`(새로운 레이어 추가)
- 빌드 컨텍스트의 파일을 레이어에 복사

`RUN 명령어` (새로운 레이어 추가)
- 명령어 실행

`CMD [“명령어”]`
- 컨테이너 실행 시 명령어 지정

```dockerfile
FROM node:14

COPY ./ /

RUN npm install

CMD ["npm", "start"]
```

**Dockerfile 빌드 실행**
도커파일명이 Dockerfile이 아닌 경우 `-f` 옵션으로 지정해줘야 한다.  

명령어 : `docker build -f 도커파일명 -t 이미지명 Dockerfile경로`

```bash
$ docker build -f Dockerfile-Custom -t nginx .
```

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

### WORKDIR, USER, EXPOSE

`WORKDIR 폴더명` (새로운 레이어 추가)
- 컨테이너 내의 작업 디렉토리를 지정

`USER 유저명` (새로운 레이어 추가)
- 명령을 실행할 사용자 변경

`EXPOSE 포트번호`
- 컨테이너가 사용할 포트를 명시. 실제 컨테이너가 사용하는 포트를 지정하는 것이 아님

```dockerfile
FROM node:14

WORKDIR /app

COPY ./ /

RUN npm install

USER node

EXPOSE 3000

CMD ["npm", "start"]
```

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

### ARG, ENV

`ARG 변수명 변수값`
- 이미지 **빌드 시점의 환경 변수** 설정한다.
- 빌드 과정에서만 사용되며 어플리케이션 내부에서 사용되는 환경 변수가 아니다.
- `docker build --build-arg 변수명=변수값` 으로 덮어쓰기가 가능하다.

`ENV 변수명 변수값` (새로운 레이어 추가)
- 이미지 빌드 및 컨테이너 **실행 시점의 환경 변수**를 설정할 때 사용된다. 
- 실제 어플리케이션 내부에서 사용해야 하는 경우 사용된다.
- ENV에 설정된 환경 변수 값은 이미지 빌드 후 이미지 내에 저장되어 컨테이너 실행 시 사용된다.
- 컨테이너 실행 시 `docker run -e 변수명=변수값` 을 사용하면 이미지 내에 저장된 환경 변수를 덮어쓴다.

```dockerfile
FROM node:14

WORKDIR /app

COPY ./ /

RUN npm install

# 파일 안에서만 사용된다.
ARG COLOR=red

# 실제 어플리케이션 안에 COLOLR로 지정된 환경 변수에 맵핑된다.
# ${COLOR}는 위 ARG에 선언한 COLOR 값이 맵핑된다.
ENV COLOR=${COLOR}

USER node

EXPOSE 3000

CMD ["npm", "start"]
```

**Dockerfile 빌드 실행**

- `--build-arg` 에 정의된 `COLOR=green`가 Dockerfile 내의 `ARG COLOR=red` 의 COLOR값에 덮어쓰기 된다.
- `-e` 옵션으로 지정한 키=값 이 컨테이너 실행 시 내부로 전달된다.

```bash
# 이미지 빌드
$ docker build --build-arg COLOR=green --name buildapp .

# 컨테이너 실행
$ docker run -d -p 80:80 -e COLOR=green --name buildapp .
```

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

### ENTRYPOINT, CMD

`ENTRYPOINT [“명령어”]`
- 고정된 명령어를 지정

`CMD [“명령어”]`
- 컨테이너 실행 시 실행 명령어 지정

```dockerfile
FROM node:14

WORKDIR /app

COPY ./ /

RUN npm install

ARG COLOR=red
ENV COLOR=${COLOR}

USER node

EXPOSE 3000

ENTRYPOINT ["npm"]

CMD ["start"]
```

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<!--<img src="" width="80%" height="80%"/>-->
