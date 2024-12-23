---
layout: single
title:  "컨테이너 실행"
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

## 컨테이너 실행 전 간단한 명령어 학습 - gitbash(터미널) 사용
- `docker version`: docker 버전 정보 확인
- `docker info`: docker의 상세 정보를 확인한다.
  - docker 버전, container 정보, docker가 설치된 host OS의 스팩 등
- `docker --help`: docker 명령어 목록을 제공한다.
  - 명령어 포맷: docker (Management Command) Command
  - (Management Command)는 생략이 가능하다. Ex. docker container run.. -> docker run..

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 컨테이너 실행: `docker run (실행옵션) 이미지명`
- `run` 명령어는 컨테이너를 생성하고 컨테이너를 실행 시킨다.
- `docker run (실행옵션) 이미지명` 명령어는 이미지(실행 할 서비스)를 담은 컨테이너를 생성하고 실행시키는 명령어다.
<br>

- nginx 웹서버의 컨테이너를 생성한 후 실행 시켜보자.
  - 명령어 : `docker run -p 80:80 --name hellonginx nginx`
  - nginx 웹서버를 담은 컨테이너를 `hellonginx`라는 이름으로 생성하고 실행 시켰다.
  <img src="https://github.com/user-attachments/assets/ec325988-8369-4fad-87f0-434a12b98e41" width="80%" height="80%"/>

## 컨테이너 실행 확인: `docker ps`
- `docker ps` 명령어는 현재 실행 중인 컨테이너의 목록을 확인할 수 있다.
- 현재 실행시킨 `hellonginx` 컨테이너는 터미널이 점유한 채 실행하고 있다. 따라서 새로운 터미널을 열고 확인을 해야 한다.
- 새로운 터미널(gitbash)을 열어서 `docker ps` 명령어를 실행 보자.
  - 목록에 `hellonginx` 이름의 컨테이너가 보이면 실행 중이라는 뜻이다.
  <img src="https://github.com/user-attachments/assets/504c4687-4859-45e0-bc19-b082dacf7e67" width="80%" height="80%"/>

## 컨테이너 삭제: `docker rm 컨테이너명/ID`
- docker rm 명령어를 통해 실행 중인 컨테이너를 삭제할 수 있다.
- nignx를 실행 중인 컨테이너를 삭제해 보자.
  - 명령어: `docker rm hellonginx`
  - 삭제한 컨테이너 이름이 출력되면 제대로 삭제된 것이다.
  <img src="https://github.com/user-attachments/assets/c81fa5e7-b626-43b3-8c37-4fe1932ac5c6" width="80%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 컨테이너 실행 과정
1. 도커의 클라이언트가 제공한 CLI 환경에서 사용자가 `명령어를 실행`한다.
2. 클라이언트는 사용자가 입력한 명령어를 API 명세에 맞게 변환한 후 `도커 데몬에게 전달`한다.
3. 도커 데몬은 `명령어를 분석`해서 이미지가 없는 경우 이미지를 다운로드 받는다. 이후 `컨테이너 런타임`에게 컨테이너 생성을 요청한다.
4. 컨테이너 런타임은 `runc`를 통해 `컨테이너 생성`과 함께 컨테이너에 필요한 `자원을 할당`하고, `프로세스를 실행` 시킨다.
5. 컨테이너 런타임은 생성 결과를 도커 데몬에게 반환하고, 도커 데몬은 결과를 클라이언트에게 반환한다.  
   
  <img src="https://github.com/user-attachments/assets/bd80b206-f9be-4df2-a9d1-3a350615aea1" width="80%" height="80%"/>

<!--<img src="" width="80%" height="80%"/>-->
