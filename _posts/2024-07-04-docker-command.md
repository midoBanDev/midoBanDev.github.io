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

## alias 추가 필요.

#### docker 관련 alias 설정
```bash
$ echo "alias docker='winpty docker'" >> ~/.bashrc
```

#### alias 적용
```bash
$ source ~/.bashrc

또는

터미널 창 껐다 다시 켜기
```


## help 옵션


#### docker
```bash
$ docker --help
```

#### container
```bash
$ docker container --help
```

#### container run
```bash
$ docker container run --help
```


## 이미지

#### 이미지 형식


#### 이미지 가져오기
```bash
$ docker pull [이미지명]
```

#### 이미지 내보내기
```bash
$ docker push [이미지명]
```

#### 이미지 목록 확인
```bash
$ docker image ls
```

#### 이미지 삭제
```bash
$ docker image rm [이미지명]
```

#### 이미지 히스토리 확인
```bash
$ docker image history [이미지명]
```

#### 이미지 메타데이터 확인
```bash
$ docker image inspect [이미지명]
```



## 도커

#### 실행 중인 Docker Container 확인
- `ps` 명령어 사용

```bash
$ docker ps
```

<br>

#### 실행, 중지 Docker Container 모두 확인
- `ps` 명령어, `a` 옵션 사용

```bash
$ docker ps -a
```

<br>

#### Docker Container 삭제
- `rm` 명령어 사용

```bash
$ docker rm [컨테이너명/ID]
```

<br>

#### 실행 중인 Docker Container 삭제
- `rm` 명령어, `f` 옵션 사용

```bash
$ docker rm -f 컨테이너명/ID
```

<br>

#### Docker Container 실행(터미널에서 실시간 실행)
- 터미널에서 실시간으로 실행 됨. `ctrl + c` 누르면 실행 중지 됨.
- `run` 명령어, `p` 옵션, `name` 옵션 사용
- `name` 옵션은 생략하면 임의의 값으로 설정됨

```bash
$ docker run -p 80:80 --name 원하는컨테이너명 이미지명
```

<br>

#### Docker Container 백그라운드에서 실행
- `d` 옵션 사용
```bash
$ docker run -d -p 8081:80 --name 원하는컨테이너명 이미지명
```

<br>

#### Docker Container 실행 후 바로 실행 한 컨테이너의 터미널에 접속하기
- `it` 옵션 사용. `p` 옵션 사용. 
```bash
$ docker run -it -p 8090:80 --name 원하는컨테이너명 이미지명
```

<br>

#### 실행 중인 Docker Container를 이미지로 생성하기
- `m` 옵션 사용
```bash
$ docker commit -m "[커밋내용]" [실행중인컨테이너명] [생성할이미지명]
```

<br>

#### 실행 중인 Docker Container의 터미널에 접속하기
```bash
$ docker exec -it 컨테이너명/ID /bin/bash
```

<br>

#### Docker Container 중지
```bash
$ docker stop [컨테이너명/ID]
```

<br>

#### 중지된 Docker Container 실행
```bash
$ docker start [컨테이너명/ID]
```

<br>

#### 실행 중인 Docker Container 재시작 하기
```bash
$ docker restart [컨테이너명/ID]
```
