---
layout: single
title:  "[Docker] 이미지 빌드(Image Build) - Dockerfile"
categories:
  - Docker
tags:
  - [Docker, Image, Image-Build]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## IaC(Infrastructure as Code)
현대의 인프라 구성에서 가장 중요한 개념 중 하나인 `IaC` 방법론은 `코드를 통한 인프라 관리`를 의미한다.
도커에서도 이 IaC 방법을 활용해서 코드로 이미지를 관리하는데 이 방식을 `이미지 빌드`라 한다. 

사람이 인프라를 관리하면 실수가 나올 수 있다. 그리고 개인이 인프라를 관리하면 인프라의 상태를 변경한 기록들을 관리하기도 쉽지 않다. 
그래서 `IaC 방법`을 사용하면 `코드로 인프라의 상태를 관리`할 수 있다. 코드에 상세 작업 내용이 기재되어 있고 이 작업을 사람이 아닌 프로그램이 대신 수행해 준다. 
프로그램이 작업을 하기 때문에 작업들을 `더 빠르고 안전하게 실행`할 수 있다. 

코드에 들어가는 내용은 프로그램이 작업하기 위한 일련의 작업 명세서로 사용된다.
그리고 이런 명세서를 GitHub 같은 소스코드 레포지토리에 저장하면 인프라의 상태도 소스코드처럼 버전 관리를 할 수 있다.  
<img src="https://github.com/user-attachments/assets/2496f786-4bf1-406e-a1d3-364a47f6e599" width="80%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 이미지 빌드와 도커파일(Dockerfile)
Docker는 Dockerfile이라는 소스 코드를 사용해서 인프라의 상태를 저장하는 이미지를 생성할 수 있다.

도커의 또 다른 이미지 생성 방법인 `Commit 방식`은 이미지를 만들 때마다 컨테이너를 실행해야 되고 `사용자가 명령어를 직접 입력`해야 한다. 
그리고 커밋 하나당 이미지의 레이어가 하나씩 추가되기 때문에 여러 개의 레이어를 추가하고 싶을 때는 여러 번의 커밋을 수행해야 한다. 
이런 방식은 복잡하기 때문에 사람이 직접하면 문제가 발생할 가능성도 커진다. 그래서 도커 이미지를 생성할 때는 대부분 빌드 방식을 많이 사용한다.

`이미지 빌드 방식(이하 도커 빌드)`는 컨테이너를 생성하고 커밋하는 것을 `도커가 대신 수행`해 준다. 
`Dockerfile`에 `도커가 수행해야 할 작업을 코드로 작성`해 놓기만 하면 된다. IaC 방식에서 `소스 코드 역할`을 `Dockerfile`이 수행한다.
<img src="https://github.com/user-attachments/assets/ea2e592d-308e-4fbd-96c9-f350f4a0976c" width="80%" height="80%"/>

도커 빌드를 수행하면 도커는 임시로 컨테이너를 실행하고 Dockerfile에 정의한 작업을 수행한 뒤에 커밋을 실행한다. 또한 여러 개의 레이어도 쉽게 추가할 수 있다. 
레이어를 한 장 추가한 다음에 다시 그 레이어로 만든 이미지를 컨테이너로 만들고 그 컨테이너 위에서 변경 작업을 한 다음에 추가로 커밋하는 것을 도커가 알아서 반복해 준다.

정리하자면 **도커는 IaC 방식에 따라서 이미지를 Dockerfile이라는 소스 코드로 관리**할 수 있다. 
코드이기 때문에 애플리케이션 소스 코드와도 함께 관리할 수 있고 버전 관리도 할 수 있다.  
Dockerfile에는 이미지를 어떻게 만드는지에 대한 세부 내용이 기재되어 있고 도커는 이 Dockerfile을 해석해서 이미지를 제작해준다.
이미지 빌드는 도커에서 가장 많이 사용되는 기능 중 하나이다. 컨테이너 애플리케이션 개발에 있어서 가장 핵심 기능이라고 볼 수 있다.
<img src="https://github.com/user-attachments/assets/43257060-0064-47b4-b5ba-0d8a90bb020a" width="80%" height="80%"/>


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 이미지 빌드
이미지 빌드는 도커의 IaC 방법을 활용한 이미지를 생성하는 방식을 의미한다. 
이미지 빌드는 `docker build` 명령을 통해서 수행되며,  `-t` 옵션 뒤에 **빌드할 이미지의 이름**을 입력하고, 그 다음 **도커 파일이 저장되어 있는 경로**를 지정한다.

빌드 명령어 : `docker build -t 생성할이미지명 Dockerfile경로`

이미지를 빌드하기 위해선 문법에 맞게 도커 파일을 작성해야 한다. 도커 파일의 문법은 지시어와 지시어의 옵션으로 구성된다.
```dockerfile
# 베이스 이미지 명: 필수 값
FROM 이미지명

# 파일을 레이어에 복사
COPY 파일경로 복사할경로

# 컨테이너 실행 시 명령어 지정
CMD ["명령어"]
```


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 이미지 빌드 실습 - VSCode 사용.
### 1. 디렉토리 및 필수 파일 생성.
1. VSCode를 실행한다.
2. `easydocker` 폴더를 생성한다. 
3. `easydocker` 안에 `build` 폴더를 생성한다.
4. `build` 폴더 안에 `index.html` 파일과 `Dockerfile` 파일을 생성해 준다.
<img src="https://github.com/user-attachments/assets/8a74bdaa-5c2a-4795-bd83-288b2a782a68" width="50%" height="80%"/>

<br>

### 2. index.html 파일 작성
```html
hello nginx
```

<br>

### 3. Dockerfile 작성
Dockerfile 내용을 문법에 맞게 작성한다.

```dockerfile
# 베이스 이미지 지정
FROM nginx:1.23

# base 이미지를 메타데이터 정보에 추가한다.
LABEL base_image="nginx:1.23"

# 직접 만든 index.html을 nginx의 기본 디렉토리에 있는 index.html에 덮어씌운다.
COPY index.html /usr/share/nginx/html/index.html

# nginx 포그라운드 실행 명령어 지정
CMD ["nginx","-g","daemon off;"]
```

<br>

### 4. 도커 빌드 실행 - VSCode의 터미널에서 진행
명령어 : `docker build -t 생성할이미지명 Dockerfile경로`

도커 빌드 명령의 실행 위치(디렉토리)는 `Dockerfile경로`를 어떻게 지정하는 지에 따라 달라진다.
위치를 `.`으로 지정 했다면 Dockerfile이 있는 디렉터리로 이동 후 도커 빌드 명령어를 실행해야 한다.
만약 Dockerfile 디렉토리에서 명령어를 실행하지 않을 경우 명령어의 `Dockerfile경로` 부분을 루트부터 `/c/Users/wglee/Desktop/easydocker/build/01.buildnginx` 이렇게 지정해야 한다.
```bash
$ docker build -t midoban/build-nginx .
```
빌드 결과 생성된 이미지를 확인할 수 있다. `docker image ls`
<img src="https://github.com/user-attachments/assets/a3e0168a-f1a3-4a6f-8f41-a79f9e80f725" width="80%" height="80%"/>


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 이미지 캐싱
Docker는 이미지를 빌드하고 관리할 때 **레이어 기반의 캐싱 시스템**을 사용한다.
이는 빌드 시간을 단축하고 저장 공간을 효율적으로 사용하기 위한 핵심 기능이다.

1. Dockerfile에 `FROM nginx:1.23`으로 지정한 베이스 이미지를 처음 실행하는 경우에는 **모든 이미지 레이어를 다운로드** 받는다.
그리고 **호스트 시스템 내부의 Docker 캐시를 저장하는 독립된 저장소에 이미지 레이어를 저장**된다.(호스트가 바뀌면 이미지는 새로 다운로드 받는다)
<img src="https://github.com/user-attachments/assets/e322ed9b-7ecf-4252-9990-394d84bb4bd0" width="80%" height="80%"/>

2. 하지만 동일한 베이스 이미지로 다시 도커 빌드를 실행하면 `케싱된 레이어를 재사용`한다. 그래서 두 번째부터는 빌드 속도가 빠르다.
<img src="https://github.com/user-attachments/assets/06cadea8-3bbf-4972-a672-77188f32fee3" width="80%" height="80%"/>

캐시된 이미지 레이어는 OS 마다 다른 위치에 저장된다.  
Linux는 `/var/lib/docker`, Windows는 `WSL2 가상 하드디스크`에 저장된다.  
클라우드 환경(AWS EC2 등)에서는 EC2 인스턴스에 Docker를 설치하면 해당 `인스턴스의 디스크`에 저장된다. 
그리고 캐싱된 이미지 레이어는 `재부팅해도 유지`된다.

이렇게 도커의 캐싱 시스템으로 동일한 이미지 레이어를 공유함으로써 `저장 공간을 효율적`으로 사용할 수 있고, `빌드 시간을 단축`할 수 있고, `네트워크 대역폭을 절약`할 수 있다.

캐싱된 이미지도 관리가 필요하다. 캐싱된 이미지는 직접 눈으로 확인할 수 없기 때문에 사용자의 관리가 필요하다.
### 캐시 관리 명령어

모든 이미지를 확인한다.
```bash
# 모든 이미지 레이어 확인
$ docker images -a
```
<br>

댕글링 이미지란 태그가 없는 이미지를 말한다.
```bash
# 댕글링 이미지 확인
$ docker images -f "dangling=true"
```
<br>

캐시된 데이터 용량을 확인할 수 있다. 주기적으로 시스템 용량을 확인하여 관리를 해주는 것이 좋다.
```bash
# 디스크 사용량 확인
$ docker system df
```
<img src="https://github.com/user-attachments/assets/7a6725ed-af65-43b5-887b-8b863e82707d" width="80%" height="80%"/>

<br>

사용하지 않는 이미지 레이어란 다음 조건을 모두 만족하는 이미지를 말한다.  
**1. 해당 이미지로 실행 중인 컨테이너가 없어야 함**  
**2. 다른 이미지의 부모 이미지(베이스 이미지)로 사용되지 않아야 함**   
`docker inspect` 명령어 사용하여 베이스 이미지를 확인할 수 있다. 하지만 정확하지 않은 경우도 있기 때문에 Dockerfile에 `LABEL base_image="nginx:1.23"`를 선언하는 것이 관리적 측면에서도 좋다. 

`docker image prune` 명령어는 안전하게 캐시 데이터를 삭제하는 방법이다.
캐싱된 이미지 레이어를 공유 받아 생성된 이미지가 존재하는 경우 캐싱 데이터를 삭제할 수 없다.
```bash
# 사용하지 않는 이미지/레이어 삭제
$ docker image prune
```

<br>

`docker image prune -a` 명령어는 `컨테이너로 실행되지 않은 모든 이미지`를 삭제하는 명령어다. 조심해서 사용해야 한다.
```bash
# 사용하지 않는 모든 이미지 삭제
$ docker image prune -a 
```
<img src="https://github.com/user-attachments/assets/61cdc8c5-4c41-4b56-a472-bc5b1285d4b1" width="80%" height="80%"/>

<br>

`docker system prune` 명령어는 이미지뿐만 아니라 컨테이너도 삭제한다.  
조건은 다음과 같다.
- 모든 중지된 컨테이너들
- 최소한 하나의 컨테이너에서도 사용되지 않는 모든 네트워크
- 모든 댕글링(참조되지 않는) 이미지들
- 사용되지 않는 빌드 캐시

```bash
# 모든 미사용 Docker 객체 삭제
$ docker system prune
```

<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<!--<img src="" width="80%" height="80%"/>-->
