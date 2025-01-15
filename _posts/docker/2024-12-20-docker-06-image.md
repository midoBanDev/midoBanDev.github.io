---
layout: single
title:  "[Docker] 이미지(Image)"
categories:
  - Docker
tags:
  - [Docker, Container]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## 이미지(image)란
- 프로그램을 실행 시키기 위해서는 `시스템 자원(파일 시스템, 메모리, CPU)`이 필요하다.
  - 시스템 자원은 OS를 통해 관리되고, 프로그램은 OS를 통해 시스템 자원을 할당 받아 실행된다.
- 또한 프로그램은 `언어, 실행에 필요한 라이브러리와 설정` 등의 프로그램 실행에 필요한 의존 요소들이 필요하다.
- 그리고 실제 `프로그램`이 필요하다. 

  <img src="https://github.com/user-attachments/assets/b1590b2d-18a5-49eb-ac29-1bcf8367dd99" width="80%" height="80%"/>


- `이미지(image)`란 프로그램 실행을 위해 필요한 모든 요소를 포함하는 파일 시스템의 스냅샷을 계층화하여 저장해 놓은 것을 말한다.
- 쉽게 말해 `프로그램을 실행하기 위해 필요한 모든 요소를 압축해 놓은 파일`이라고 생각하면 된다.
- 이미지는 OS의 `파일 시스템`과 `실행에 필용한 의존 요소`, `실제 프로그램`으로 구성되어 있다.
- 그래서 docker run 명령어로 이미지를 실행했을 때 프로그램을 실행 시킬 수 있었던 것이다.
  <img src="https://github.com/user-attachments/assets/449c0890-1069-4d14-a911-94733fda27e4" width="80%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 이미지의 기본 파일 시스템 
도커는 리눅스 기반으로 동작하기 때문에 기본적으로 제공되는 이미지는 리눅스의 파일 시스템 구조를 가진다.  
가장 일반적인 OS는 아래와 같다.
```
ubuntu:20.04
debian:slim
alpine:3.**14**
```
소프트웨어가 설치되고 사용되기 위해서는 OS가 필요하다.
그래서 이미지는 리눅스 OS를 설치하고 그 위에 소프트웨어가 설치된 시점의 스냅샷으로 만들어진다.
그래서 우리가 사용하는 소프트웨어 이미지들을 컨테이너로 실행하여 바로 사용할 수 있는 것이다.

jdk 이미지를 컨테이너로 실행하고 컨테이너 내부에 접속하면 아래와 같은 기본 파일 시스템 디렉터리 구조를 확인할 수 있다.
<img src="https://github.com/user-attachments/assets/fd52803f-e7d7-4dde-b393-36dd225eaa05" width="80%" height="80%"/>

하지만 한 가지 고려해야할 점이 있다. 
JDK 이미지는 기본적으로 실행할 명령어가 없다. 그런데 컨테이너는 실행할 프로세스가 없으면 자동으로 종료 버린다.
이렇게 실행할 명령어가 없는 이미지로 컨테이너를 실행해야 하는 경우 아래 명령어로 실행 가능하다. 
```bash
# 1. 대화형 셸로 실행 : 컨테이너 실행하면서 바로 접속
docker run -it --name jdk-container openjdk:11-jre-slim bash

# 2. sleep 명령어로 계속 실행 상태 유지
docker run -d --name jdk-container openjdk:11-jre-slim sleep infinity
```



<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 이미지와 컨테이너의 관계
- 프로그램은 실행되기 전 디스크 공간만 차지하는 형태로 존재한다. OS를 통해 자원을 할당 받고 실행이 되면 프로세스가 된다.
- 이미지와 컨테이너의 관계는 프로그램과 프로세스의 관계와 동일하게 이해하면 된다.
- 이미지는 프로그램이 실행되기 위한 환경이 모두 포함되어 있는 파일 시스템이고, 압축 파일의 형태로 호스트 머신의 특정 경로에 위치하여 디스크 공간만 차지하고 있다.
- 그리고 이 이미지를 실행한 것이 컨테이너이다.
- 하나의 프로그램으로 여러 프로세스를 만들 수 있듯이, 하나의 이미지로 여러 개의 컨테이너를 실행할 수 있다.
- 이미지를 컨테이너로 실행하는 순간부터 CPU와 메모리를 사용하게 된다.
- 정리하자면 이미지를 컨테이너로 실행시키는 것은 이미지에 저장되어 있는 모든 요소들을 격리된 공간(컨테이너)으로 만든 다음에 격리된 공간 안에서 프로그램을 프로세스로 실행시키는 단계를 거치는 것이다.

  <img src="https://github.com/user-attachments/assets/3b2c4376-9ac6-4188-a0ca-c263dc74d54a" width="80%" height="80%"/>

<br>

## 이미지(image) 조회: `docker image ls (이미지명)`
로컬 전체 이미지 조회: `docker image ls`  
로컬 특정 이미지 조회: `docker image ls (이미지명)`

<img src="https://github.com/user-attachments/assets/8c65bc8a-20f4-44de-b649-46b486a74fe3" width="80%" height="80%"/>

- REPOSITORY : 이미지 저장소 이름
- TAG: 이미지의 버전
- IMAGE ID: 이미지 고유번호
- CREATED: 만들어진 날짜
- SIZE: 이미지 크기


<br>


## 이미지 네이밍 규칙
- 이미지 이름 구조 : `레지스트리주소/프로젝트명/레포지토리명:이미지태그`
  1. 레지스트리 주소
     - 이미지를 `어떤 레지스트리에서 다운로드하고 업로드 할지`를 지정해야한다.
     - 레지스티리주소가 비어 있는 경우 기본 레지스트리 값인인 Docker Hub의 주소인 `docker.io`로 지지정된다.
     - 개인 레지스트리가 있는 경우 레지스트리 주소를 입력해야 한다.
  2. 프로젝트 명
     - 이미지를 `보관하는 폴더` 같은 개념이다.
     - `도커 허브`의 경우 가입한 `사용자의 계정명이 프로젝트 명`이 된다.
     - 프로젝트명이 비어 있는 경우 
       - 도커는 도커사가 직접 검증한 이미지는 오피셜 이미지로 제공하고 있다.
       - 그리고 이 오피셜 이미지는 라이브러리라는 프로젝트에서 관리한다.
       - 그래서 별도로 계정명을 입력하지 않으면 기본 값인 `library`로 지정된다.
  3. 레포지토리명
     - 다운받을 이미지의 `레포지토리 이름`이다.
     - 
  4. 이미지 태그
     - 이미지의 `버전 정보`이다.
     - 이미지 버전에는 숫자와 영문 모두 사용할 수 있다.
     - 이미지 태그가 비어 있는 경우 기본 값인 `latest`로 지정된다.

  <img src="https://github.com/user-attachments/assets/f5ef54b6-7dea-4093-92e4-7f72c6b14582" width="80%" height="80%"/>

<br>

**주요 핵심**
```markdown
- 이미지명은 `레포지토리명:태그`로 관리 된다.
- `docker run nginx` 명령어의 `ngninx`는 이미지명이 아니라 `레포지토리명`이다. 
- 숨어있는 latest 태그를 지정한 `nginx:latest` 가 온전한 이미지명이다.
```

<br>


## 이미지(image) 실행: `docker run -d --name {컨테이너명} 이미지명`
- 컨테이너 이름은 중복될 수 없다.
- `d` 옵션을 통해 컨테이너를 백그라운드에서 실행 시킬 수 있다.
- `docker ps`: 실행 중인 컨테이너 리스트를 확인할 수 있다.
- `docker rm -f 컨테이너명/ID`: `f` 옵션을 해야 실행 중인 컨테이너를 삭제할 수 있다. 
  - 컨테이녀명/ID를 띄어쓰기를 통해 여러개 동시 삭제가 가능하다.  
  ex. `docker rm -rf nginx1 nginx2 nginx3`


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 이미지 메타데이터 
- 이미지는 이미지의 이름이 무엇인지, 사이즈가 어떻게 되는지에 대한 정보를 저장하고 관리한다.
- `메타데이터`는 이미지 파일 데이터에 대한 데이터를 의미한다.
- 하나의 이미지는 `실제로 압축된 파일`과 이 파일의 정보가 저장되어 있는 `메타데이터`로 구성되어 있다.
- 메타데이터는 `이미지의 ID`, `이미지의 Name`, `파일 Size` 같은 정보와 `Env`, `Cmd` 필드 등으로 구성되어 있다.
  - `Env`는 애플리케이션이 사용하는 환경 설정 값을 의미한다. Env는 키와 값으로 이루어져 있다. `=`를 기준으로 왼쪽은 키, 오른쪽은 값으로 나뉘어져 있다.
  - `Cmd`는 이미지를 컨테이너로 실행할 때 프로세스가 수행할 명령어가 정의되어 있다.
- 이미지의 압축 파일과 이미지의 메타데이터를 사용해서 격리된 컨테이너가 만들어진다. 
- 그리고 이미지가 컨테이너로 실행될 때 Cmd 필드의 명령어가 실행되고, 프로세스가 실행될 때 Env 값이 사용된다.
- 메타데이터는 컨테이너를 실행할 때 새로운 값으로 덮어쓰기 할 수 있다.
- 컨테이너 생성 후 이미지의 메타데이터는 컨테이너 메타데이터로 복사된다.

  <img src="https://github.com/user-attachments/assets/8340f0a7-955e-4d41-bd2b-6af5587e99fd" width="80%" height="80%"/>

<br>

## 이미지 메타데이터 확인: `inspect`
- 이미지 세부 정보 조회 : `docker image inspect 이미지명`
- 컨테이너 세부 정보 조회 : `docker container inspect 컨테이너명`

  ```bash
  1. nginx 이미지의 메타데이터 확인
  $ docker image inspect nginx  

  2. 설정 변경사항 없이 컨테이너 실행
  $ docker run -d --name defaultCmd nginx  

  3. 실행된 컨테이너의 메타데이터 확인
  $ docker container inspect defaultCmd
  ```  

<br>

## 이미지 메타데이터 `Cmd` 덮어쓰기 : `docker run 이미지명 (실행명령)`
  
```bash
1. docker run 명령의 옵션 확인
$ docker run --help

2. 메타데이터를 수정할 cmd 명령을 포함한 컨테이너 실행, cat 명령으로 컨테이너 내 파일 내용 출력
$ docker run --name customCmd nginx cat usr/share/nginx/html/index.html

3. 실행된 컨테이너의 메타데이터 확인
$ docker container inspect customCmd

4. docker ps -a 명령으로 종료된 컨테이너 조회
$ docker ps -a
```

- `docker run` 명령어에 `cmd 명령어를 지정`하면 기존 이미지의 cmd 명령어는 무시된다.
- 또한 컨테이너의 메타데이터의 cmd는 컨테이너 실행 시 지정한 명령어로 변경된다.
  - cmd 명령어는 띄어쓰기(공백)을 기준으로 배열로 저장된다.

  <img src="https://github.com/user-attachments/assets/4da0ec52-5850-432b-8e98-52d0cb8e20a7" width="80%" height="80%"/>

<br>

## 이미지 메타데이터 `Env` 덮어쓰기 : `docker run --env KEY=VALUE 이미지명`
- 이미지 다운로드 : `docker pull 이미지명`

```bash
1. 이미지 다운로드
$ docker pull devwikirepo/envnodecolorapp

2. 이미지 메타데이터의 cmd, env 확인
$ docker image inspect devwikirepo/envnodecolorapp

3. 실행된 컨테이너의 메타데이터 확인, http://localhost:8080, http://localhost:8081 접속
$ docker run -d -p 8080:3000 --name defaultColorApp devwikirepo/envnodecolorapp
$ docker run -d -p 8081:3000 --name blueColorApp --env COLOR=blue devwikirepo/envnodecolorapp

4. 컨테이너 메타데이터 확인인
$ docker container inspect blueColorApp

4. docker rm -f 로 실습에 생성한 컨테이너 모두 삭제
$ docker rm -f defaultCmd customCmd defaultColorApp blueColorApp
```


<!--<img src="" width="80%" height="80%"/>-->
