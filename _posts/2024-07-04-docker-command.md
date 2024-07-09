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

# help 옵션 사용

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
- container 명령어 생략 가능

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
```
Usage:  docker image COMMAND
```

<br>

## 이미지 옵션
<details>
<summary> 
<b><span>옵션</span></b>
</summary>

<div markdown="1">

- `build` : Dockerfile을 사용하여 이미지를 빌드한다.
  ```
  Usage:  docker buildx build [OPTIONS] PATH | URL | -
  ```

- `history` : 이미지의 레이어 히스토리를 보여준다.
  ```
  Usage:  docker image history [OPTIONS] IMAGE
  ```
  
- `import` : tarball의 내용을 가져와 파일 시스템 이미지를 생성한다.
- `inspect` : 하나 이상의 이미지 상세 정보(metadata)를 보여준다.(여러 이미지는 공백으로 구분)
- `load` : tar 아카이브 또는 STDIN에서 이미지를 로드한다.
- `ls` : 이미지의 목록을 보여준다.
- `prune` : 사용하지 않는 이미지를 제거합니다.
- `pull` : 레지스트리에서 이미지를 다운로드 한다.
- `push` : 이미지를 레지스트리에 업로드 한다.
- `rm` : 하나 이상의 이미지를 제거한다.(여러 이미지는 공백으로 구분)
- `save` : 하나 이상의 이미지를 tar 아카이브로 저장한다.
- `tag` : source 이미지를 참조하여 새로운 이미지를 생성한다.

</div>
</details>

<br>

## 이미지 네이밍 규칙
```
레지스트리주소/프로젝트명/이미지명:이미지태그
```
- `레지스트리주소`를 생략할 경우 default로 `docker.io` 주소가 셋팅된다.
- `프로젝트명`을 생략할 경우 default로 `library` 가 셋팅된다.
- `이미지태그`를 생략할 경우 default로 `latest`가 셋팅된다.


```bash
# 기본 문법
$ docker COMMAND (이미지명)


# 1. naginx 라는 이미지명만 입력한 경우
$ docker COMMAND nginx 

# 실제 Docker에서 아래와 같이 default 값을 붙여서 실행되게 된다.
$ docker COMMAND docker.io/library/nginx:latest



# 2. 프로젝트명과 이미지명만 입력한 경우
$ docker COMMAND myproject/nginx

# 실제 Docker에서 아래와 같이 default 값을 붙여서 실행되게 된다.
$ docker COMMAND docker.io/myproject/nginx:latest
```

<br>

## 로컬 레지스트리로 이미지 다운로드
- `pull` 명령어 사용

```bash
$ docker pull (이미지명)
```

<br>

## 로컬 레지스트리의 새로운 이미지명 추가
- `tag` 명령어 사용

```bash
$ docker tag (기존이미지명) (추가할이미지명)
```

<br>

## 레지스트리에 이미지 업로드
- `push` 명령어 사용

```bash
$ docker push (이미지명)
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
$ docker image rm (이미지명)
```

<br>

## 이미지 히스토리 확인
- `image` 명령어(생략가능), `history` 명령어 사용

```bash
$ docker [image] history (이미지명)
```

<br>

## 이미지 세부정보(메타데이터) 확인
- `image` 명령어(생략가능), `inspect` 명령어 사용

```bash
$ docker [image] inspect (이미지명)
```

<br>

## 실행 중인 Docker Container를 이미지로 생성하기
- `commit` 명령어, `m` 옵션 사용

```bash
$ docker commit -m "(커밋내용)" (실행중인컨테이너명) (생성할이미지명)
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

# 컨테이너(Container)

<br>

## Container 옵션
<details style="margin:10px 10px; font-size:15px;">
<summary> 
<b><span>옵션</span></b>
</summary>

<div markdown="1">

- `attach` : 실행 중인 컨테이너에 로컬 표준 입력, 출력 및 오류 스트림을 연결한다.

</div>
</details>

<br>

## 실행 중인 Docker Container의 세부정보(메타데이터) 확인
- `container` 명령어(생략가능), `inspect` 명령어 사용

```bash
$ docker [container] inspect (컨테이너명/ID)
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
$ docker rm (컨테이너명/ID)
```

<br>

## 실행 중인 Docker Container 삭제
- `rm` 명령어, `f` 옵션 사용

```bash
$ docker rm -f (컨테이너명/ID)
```

<br>

## Docker build

```bash
$ docker buid -t (이미지명) (Dockerfile경로)
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
- `p` : 포트포워딩 옵션, {Host의포트}:{컨테이너의포트}
- `name` : 생략하면 임의의 값으로 설정됨

```bash
$ docker run -p 80:80 --name (원하는컨테이너명) (이미지명)
```

<br>

## Docker Container 백그라운드에서 실행
- `d` 옵션 사용

```bash
$ docker run -d -p 8081:80 --name (원하는컨테이너명) (이미지명)
```

<br>

## 실행 할 Docker Container의 CMS 명령어 덮어쓰기
- 실행할 이미지의 기존 CMD 명령어는 이미지의 메타데이터를 확인하는 `inspect` 명령어로 확인 가능하다.
- 실행할 `Image명` 뒤에 추가해 주면 된다.

```bash
$ docker run -d -p 8081:80 --name (원하는컨테이너명) (이미지명) (COMMAND)
```

<br>

## Docker Container 실행 시 network 지정
- `--network` 를 지정하지 않는 경우 `default bridge network`가 적용된다.
- `run` 명령어, `d` 옵션, `network` 옵션, `name` 옵션 사용

```bash
$ docker run -d --network (네트워크명) --name (원하는컨테이너명) (이미지명)

# Ex
$ docker run -d --network second-bridge --name ubuntuC devwikirepo/pingbuntu
```

<br>

## Docker Container 실행 후 바로 실행 한 컨테이너 터미널에 접속하기
- Container에 바로 접속한 다음 해당 터미널에서 `exit`로 빠져나오게 되면 Container는 중지 된다.
- `it` 옵션 사용. `p` 옵션 사용. 

```bash
$ docker run -it -p 8090:80 --name (원하는컨테이너명) (이미지명)
```

<br>

## 실행 중인 Docker Container 터미널에 접속하기
- `exec` 명령어, `it` 옵션 사용

```bash
$ docker exec -it (컨테이너명/ID) /bin/bash
```

<br>

## Docker Container 중지
- `stop` 명령어 사용

```bash
$ docker stop (컨테이너명/ID)
```

<br>

## 중지된 Docker Container 실행
- `start` 명령어 사용


```bash
$ docker start (컨테이너명/ID)
```

<br>

## 실행 중인 Docker Container 재시작 하기
- `restart` 명령어 사용

```bash
$ docker restart (컨테이너명/ID)
```

<br>

## 실행 중인 Docker Container 로그 확인
- `logs` 명령어, `f` 옵션 사용

```bash
$ docker logs -f (컨테이너명/ID)
```

<br>

## Container와 호스트 머신 간 파일 복사
- `cp` 명령어 사용

```bash
$ docker cp (원본위치) (복사위치)
```

<br>

## Container에서 호스트 머신으로 파일 복사
- `cp` 명령어 사용

```bash
$ docker cp (컨테이너명:원본위치) (복사위치)
```

<br>

## 호스트 머신에서 Container로 파일 복사
- `cp` 명령어 사용

```bash
$ docker cp (원복위치) (컨테이너명:복사위치)
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

# Docker Network 
- 브릿지 네트워크(Bridge) : 도커 브릿지를 활용해 컨테이너간 통신한다. NAT와 포트포워딩 기술을 활용해 외부 통신을 지원한다.
- 호스트 네트워크(Host) : 호스트의 네트워크를 공유한다. 모든 컨테이너는 `호스트 머신과 동일한 IP`를 사용하지만 포트는 중복이 불가능하다.
- 오버레이 네트워크(Overlay) : Kubernetes에서 사용한다. 호스트 머신이 다수일 때 네트워크를 관리하기 위한 기술이다.
- Macvlan 네트워크 : 컨테이너에 MAC 주소를 할당하여 물리 네트워크 인터페이스에 직접 연결한다.

<br>

## 네트워크 목록 확인
```bash
$ docker network ls
```

<br>

## 네트워크 상세 정보 조회
- `network` 명령어(생략가능), `inspect` 명령어 사용

```bash
$ docker [network] inspect (네트워크명)
```

<br>

## 네트워크 생성
- `network` 명령어, `create` 명령어 사용

```bash
$ docker network create (원하는네트워크명)
```

<br>

## 브릿지 네트워크 추가

- `subnet` : 해당 네트워크에서 생서되는 컨테이너들이 할당받는 IP의 범위
- `gateway` : 브릿지의 IP 주소
- `driver` 명령어, `subnet` 명령어, `gateway` 명령어 사용

```bash
$ docker network create --driver (드라이버종류) --subnet (subnet IP) --gateway (gateway IP) (원하는네트워크명)

# Ex.
$ docker network create --driver bridge --subnet 10.0.0.0/24 --gateway 10.0.0.1 second-bridge
```

<br>

## 네트워크 삭제
- `network` 명령어, `rm` 명령어 사용

```bash
$ docker network rm (네트워크명)
```

<br>

## DNS 서버 확인
- `기본 bridge 네트워크`에는 `DNS 서버`가 존재하지 않는다.
- 추가된 bridge 네트워에서만 존재한다.
- `DNS 서버` 가 존재하는 네트워크에서는 `컨테이너명`으로 통신이 가능하다.
- 컨테이너 실행한 다음 터미널에 접속한 후 `/etc/resolv.conf` 파일에서 확인 가능하다.

```bash
root@3b7914e19a2a:/# cat /etc/resolv.conf
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

# Docker Volume && Bind Mount
- `Volume`은 도커가 자동으로 관리하는 영역이기 때문에 사용자가 별도로 확인하거나 수정할 수 없다.
- 사용자가 원하는 경로로 지정 후 관리하고 싶은 경우에는 `Bind Mount`를 사용해야 한다.

<br>

## Volume 리스트 조회
- `volume` 명령어 사용

```bash
$ docker volume ls
```

<br>

## Volume 상세 정보 조회
- `volume` 명령어, `rm` 명령어 사용

```bash
$ docker [volume] inspect (볼륨명)
```

<br>

## Volume 생성
- `volume` 명령어, `create` 명령어 사용

```bash
$ docker volume create (볼륨명)
```

<br>

## Volume 삭제
- `volume` 명령어, `rm` 명령어 사용

```bash
$ docker volume rm (볼륨명)
```

<br>

## Volume Mount
- 아래 예제는 `mydata` 라는 volume을 생성한 상황이다.
- `v` 옵션 사용. {볼륨명}:{컨테이너 내부 경로}

```bash
$ docker run -d --name (원하는컨테이너명) -v (불륨명):(컨테이너내부경로) (이미지명)

# Ex.
$ docker run -d --name my-postgres-2 -v mydata://var/lib/postgresql/data postgres:13
```

<br>

## Bind Mount
- `v` 옵션 사용

```bash
$ docker run -d --name (원하는컨테이너명) -v (HostOS의경로):(컨테이너내부경로) (이미지명)

# Ex.
$ docker run -d --name my-nginx-b -v C:\Users\crizen\Desktop\index:/usr/share/nginx/html nginx
```

<br>

## Mount 상세 정보 확인
- `v` 옵션 사용

```bash
$ docker container inspect (컨테이너명)
```
- Type : Mount Type
- Source : HostOS의 경로
- Destination : 컨테이너 내부 경로
```
"Mounts": [
            {
                "Type": "bind",
                "Source": "C:\\Users\\crizen\\Desktop\\easydocker\\index",
                "Destination": "/usr/share/nginx/html",
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            }
        ],
```