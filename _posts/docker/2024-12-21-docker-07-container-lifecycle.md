---
layout: single
title:  "컨테이너 라이프사이클"
categories:
  - Docker
tags:
  - [Docker, Container, Container-lifecycle]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## 컨테이너 라이프사이클
- 컨테이너는 이미지에서부터 시작된다.

<img src="https://github.com/user-attachments/assets/598cfe8a-a441-469f-865c-030dc3777f5d" width="90%" height="80%"/>

### 생성 상태(Created) : `docker create 컨테이너명/ID`
- 첫 번째로 컨테이너가 가질 수 있는 상태는 `Created`, 생성 단계이다.
- `docker create` 명령어로 이미지를 컨테이너로 만들 수 있다.
- 생성 단계에서는 컨테이너를 실행하기 위한 격리된 공간이 만들어진 상태이다. 
- 네트워크, 스토리지, 환경 변수 같은 `모든 리소스가 격리된 공간이 컨테이너로 분리된 상태`이다.
- 생성 단계에서는 프로세스를 실제 실행하지 않기 때문에 호스트 OS의 `CPU`와 `메모리`를 사용하지 않는다.


### 실행 상태(Running) : `docker start 컨테이너명/ID`
- `docker start` 명령어를 사용하면 컨테이너의 `cmd` 값을 사용해서 컨테이너를 러닝 상태로 만든다.
- 실행 상태는 컨테이너 내에서 프로세스가 실행 중이라는 것을 의미한다.
- 이 프로세스는 `CPU`와 `메모리`를 사용한다.
- docker run 명령어는 create 명령어와 start 명령어가 합쳐진 명령어다.
- 현재 단계에서 `docker restart` 명령을 하면 프로세스를 재시작 시킬 수 있다.
- `restart` 명령어를 사용하면 10초 뒤 프로세스가 재시작 됩니다.
- 종료된 프로세스를 시작하면 컨테이너의 `프로세스가 처음부터 다시 실행`된다.


### 일시 정지 상태(Paused): `docker pause 컨테이너명/ID`
- 실행 중인 컨테이너를 `pause` 명령어로 일시 중지 할 수 있다.
- 일시 정지 상태는 컨테이너에서 실행 중인 모든 프로세스가 일시 중지된 상태다.
- 일시 중지한다는 것은 현재의 상태를 모두 메모리에 저장해 두는 것을 의미한다.
- 일시 정지 상태에서는 CPU는 사용하지 않고, 메모리만 사용하게 된다.
- 컨테이너 `STATUS` 값은 `Paused` 이다.


### 일시정지 재시작(unpaused): `docker unpause 컨테이너명/ID`
- 일시 정지 상태에서 `unpaused` 명령어로 프로세스를 재시작 할 수 있다.
- `unpaused` 명령어를 사용하면 프로세스를 `일시 정지했던 시점부터 재시작`하게 된다.
  

### 종료 상태(Stopped) : `docker stop 컨테이너명/ID`
- Stopped 상태는 컨테이너에서 실행 중인 프로세스를 완전히 중단 시킨 것을 의미한다.
- 이때는 `CPU`와 `메모리`이 중단된다.
- 실행 중인 프로세스를 stop 명령어를 통해 종료 시킬 수 있다.
- 기본적으로 `10초 동안 대기`한 후 종료된다.
- 컨테이너 `STATUS` 값은 `Exited` 이다.


### 삭제(Deleted) : `docker rm -f 컨테이너명/ID`, `docker rm 컨테이너명/ID`
- 실행 중인 컨테이너는 `docker rm` 에 `-f` 옵션을 통해 삭제할 수 있다.
- 종료 된 컨테이는 `docker rm` 명령어로 삭제할 수 있다.
  


## 컨테이너와 프로세스
- 프로세스가 실행되면 컨테이너도 실행된다.
- 프로세스가 멈추거나 종료되면 컨테이너도 정지되거나 종료된다.
- 결국 프로세스를 잘 설계하고 다루는 것이 컨테이너를 잘 사용하는 것과 방향성이 일치한다.



## 컨테이너 생성, 종료, 재시작
```bash
1. 컨테이너 생성
$ docker create --name tencounter devwikirepo/tencounter

2. 생성된 컨테이너 조회
$ docker ps -a

3. 컨테이너 실행
$ docker start tencounter

4. 컨테이너 실행 후 실행 및 종료 상태 확인
$ docker ps -a

5. 종료된 프로세스 재시작(-i 옵션은 컨테이너 출력을 터미널에 연결)
$ docker start -i tencounter

6. 컨테이너 삭제
$ docker rm -f tencounter
```


## 컨테이너 실행 및 로그 확인
- 로그 확인(일회성) : `docker logs 컨테이너명/ID`
- 로그 확인(실시간) : `-f` 옵션 사용, `docker logs -f 컨테이너명/ID`
  
```bash
1. 컨테이너 실행
$ docker run --name hundredcounter devwikirepo/hundredcounter

2. 컨테이너 중지 및 재시작
$ docker pause hundredcounter
$ docker unpause hundredcounter

3. 생성된 컨테이너 정지 후 상태 확인
$ docker stop hundredcounter
$ docker ps -a

4. 컨테이너 재실행
$ docker start -i hundredcounter

5. 컨테이너 재시작
$ docker restart hundredcounter

6. 컨테이너 상태 및 로그 확인
$ docker ps
$ docker logs hundredcounter
$ docker logs -f hundredcounter

7. 컨테이너 삭제
$ docker rm -f hundredcounter
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

<br>

## 이미지(image) 조회: `docker image ls (이미지명)`
로컬 전체 이미지 조회: `docker image ls`  
로컬 특정 이미지 조회: `docker image ls (이미지명)`

- REPOSITORY : 이미지의 이름
- TAG: 이미지의 버전
- IMAGE ID: 이미지 고유번호
- CREATED: 만들어진 날짜
- SIZE: 이미지 크기

<br>

## 이미지(image) 실행: `docker run -d --name {컨테이너명} 이미지명`
- 컨테이너 이름을 중복될 수 없다.
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
- 컨테이너 생성 후 이미지의 메타데이터는 `컨테이너 메타데이터`로 복사된다.
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
