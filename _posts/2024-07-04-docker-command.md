---
layout: single
title:  "Docker 명령어"
categories:
  - Docker
tags:
  - [Docker, Command]

toc: true
toc_sticky: true
---

## docker 관련 alias 설정
```bash
$ echo "alias docker='winpty docker'" >> ~/.bashrc
```

## alias 적용
```bash
$ source ~/.bashrc

또는

터미널 창 껐다 다시 켜기
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

# help 옵션

## docker
```bash
$ docker --help
```

<br>

## container
```bash
$ docker container --help
```

<br>

## container run
```bash
$ docker container run --help
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

# 이미지(Image)
- 컨테이너를 실행하기 위한 프로그램이 들어있는 압축파일

<br>

## 이미지 네이밍 규칙
```
레지스트리주소/프로젝트명/이미지명:이미지태그
```
- `레지스트리주소`를 생략할 경우 default로 `docker.io` 주소가 셋팅된다.
- `프로젝트명`을 생략할 경우 default로 `library` 가 셋팅된다.
- `이미지태그`를 생략할 경우 default로 `latest`가 셋팅된다.


```bash
# naginx 라는 이미지명만 입력한 경우
$ docker [명령어] nginx 

# 실제 Docker에서 아래와 같이 default 값을 붙여서 실행되게 된다.
$ docker [명령어] docker.io/library/nginx:latest

# 프로젝트명과 이미지명만 입력한 경우
$ docker [명령어] myproject/nginx

# 실제 Docker에서 아래와 같이 default 값을 붙여서 실행되게 된다.
$ docker [명령어] docker.io/myproject/nginx:latest
```

<br>

## 로컬 레지스트리로 이미지 다운로드
- `pull` 명령어 사용

```bash
$ docker pull [이미지명]
```

<br>

## 로컬 레지스트리의 새로운 이미지명 추가
- `tag` 명령어 사용

```bash
$ docker tag [기존이미지명] [추가할이미지명]
```

<br>

## 레지스트리에 이미지 업로드
- `push` 명령어 사용

```bash
$ docker push [이미지명]
```

<br>

## 이미지 목록 확인
- `image` 명령어, `ls` 명령어 사용

```bash
$ docker image ls
```

<br>

## 이미지 삭제
- `image` 명령어, `rm` 명령어 사용

```bash
$ docker image rm [이미지명]
```

<br>

## 이미지 히스토리 확인
- `image` 명령어(생략가능), `history` 명령어 사용

```bash
$ docker (image) history [이미지명]
```

<br>

## 이미지 세부정보(메타데이터) 확인
- `image` 명령어(생략가능), `inspect` 명령어 사용

```bash
$ docker (image) inspect [이미지명]
```

<br>

## 실행 중인 Docker Container를 이미지로 생성하기
- `commit` 명령어, `m` 옵션 사용

```bash
$ docker commit -m "[커밋내용]" [실행중인컨테이너명] [생성할이미지명]
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

# 컨테이너(Container)

## 실행 중인 Docker Container의 세부정보(메타데이터) 확인
- `container` 명령어(생략가능), `inspect` 명령어 사용

```bash
$ docker (container) inspect [컨테이너명/ID]
```


## 실행 중인 Docker Container 확인
- `ps` 명령어 사용

```bash
$ docker ps
```

<br>

## 모든 Docker Container 확인
- `ps` 명령어, `a` 옵션 사용

```bash
$ docker ps -a
```

<br>

## 중지된 Docker Container 삭제
- `rm` 명령어 사용

```bash
$ docker rm [컨테이너명/ID]
```

<br>

## 실행 중인 Docker Container 삭제
- `rm` 명령어, `f` 옵션 사용

```bash
$ docker rm -f [컨테이너명/ID]
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

# Doker run
- [] 괄호는 생략 가능

```bash
Usage : docker run [OPTIONS] IMAGE [COMMAND] [ARG...]
```

<details>
<summary> 
<b><span>run 옵션</span></b>
</summary>

<div markdown="1">

|옵션|설명|예제|
|:------|:---|:---|
|-e, --env key:value|환경 변수 설정| -e COLOR=blue, --evn PATH=/dir |
|테스트1|테스트2|테스트3|
|테스트1|테스트2|테스트3|

</div>
</details>

<br><br>

## Docker Container 실행
- 터미널에서 실시간으로 출력 됨. `ctrl + c` 누르면 Container 실행 중지 됨.
- `run` 명령어, `p` 옵션, `name` 옵션 사용
- `name` 옵션은 생략하면 임의의 값으로 설정됨

```bash
$ docker run -p 80:80 --name [원하는컨테이너명] [이미지명]
```

<br>

## Docker Container 백그라운드에서 실행
- `d` 옵션 사용

```bash
$ docker run -d -p 8081:80 --name [원하는컨테이너명] [이미지명]
```

<br>

## 실행할 Docker Container의 CMS 명령어 덮어쓰기
- 실행할 이미지의 기존 CMD 명령어는 이미지의 메타데이터를 확인하는 `inspect` 명령어로 확인 가능하다.
- 실행할 `Image명` 뒤에 추가해 주면 된다.

```bash
$ docker run -d -p 8081:80 --name 원하는컨테이너명 이미지명 [COMMAND]
```
<br>

## Docker Container 실행 후 바로 실행 한 컨테이너 터미널에 접속하기
- `it` 옵션 사용. `p` 옵션 사용. 

```bash
$ docker run -it -p 8090:80 --name [원하는컨테이너명] [이미지명]
```

<br>

## 실행 중인 Docker Container 터미널에 접속하기
- `exec` 명령어, `it` 옵션 사용

```bash
$ docker exec -it [컨테이너명/ID] /bin/bash
```

<br>

## Docker Container 중지
- `stop` 명령어 사용

```bash
$ docker stop [컨테이너명/ID]
```

<br>

## 중지된 Docker Container 실행
- `start` 명령어 사용


```bash
$ docker start [컨테이너명/ID]
```

<br>

## 실행 중인 Docker Container 재시작 하기
- `restart` 명령어 사용

```bash
$ docker restart [컨테이너명/ID]
```

<br>

## 실행 중인 Docker Container 로그 확인
- `logs` 명령어, `f` 옵션 사용

```bash
$ docker logs -f [컨테이너명/ID]
```

<br>

## Container와 호스트 머신 간 파일 복사
- `cp` 명령어 사용

```bash
$ docker cp [원본위치] [복사위치]
```

<br>

## Container에서 호스트 머신으로 파일 복사
- `cp` 명령어 사용

```bash
$ docker cp [컨테이너명:원본위치] [복사위치]
```

<br>

## 호스트 머신에서 Container로 파일 복사
- `cp` 명령어 사용

```bash
$ docker cp [원복위치] [컨테이너명:복사위치]
```

<br><br>

# Docker Network 

## 네트워크 목록 확인

```bash
$ docker network ls
```

<br>

## 네트워크 생성
- `network` 명령어, `create` 명령어 사용

```bash
$ docker network create [네트워크명]
```


## Docker build

```bash
$ docker buid -t [이미지명] [Dockerfile경로]
```