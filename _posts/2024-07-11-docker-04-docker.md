---
layout: single
title:  "Docker란"
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

## 도커(Docker)란
- 도커는 컨테이너 가상화 기술을 사용하기 위한 도구이다.
- 도커와 같은 컨테이너 가상화 도구를 `컨테이너 플랫폼`이라고 부른다.
- 컨테이너 플랫폼은 자체적으로 가지고 있는 `컨테이너 엔진`과 `컨테이너 런타임`으로 구성되어 있다.
  - `컨테이너 엔진`은 사용자의 요청을 받아서 `컨테이너를 관리`해주는 역할을 한다.
  - `컨테이너 런타임`은 직접 커널과 통신하면서 실제로 `격리된 공간을 만드는 역할`을 한다.
- 도커의 구조는 크게 시스템 관점에서 `도커 엔진` + `컨테이너 런타임`로 구분할 수 있다. 
- 사용자 관점에서의 도커는 도커 엔진을 구성하는 `클라이언트` + `데몬`으로 구분할 수 있다.

  <img src="https://github.com/user-attachments/assets/c3693262-1601-4a43-bdc3-062d836a8a53" width="90%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 도커(도커 엔진)의 동작 원리
- 도커 엔진은 클라이언트 서버 모델로 실행된다.
- 도커 엔진은 `도커 클라이언트`와 `도커 데몬`이라는 서버로 구성되어 있다.
  - `클라이언트` : 사용자의 명령어를 전달해 준다.
  - `도커 데몬(도커 D)` : 도커 데몬은 호스트 OS에서 지속적으로 실행되면서, 커널의 기능을 활용해서 컨테이너를 관리해주는 역할을 한다.
    - 컨테이너 관리, 이미지 관리, 네트워킹 등등등

  <img src="https://github.com/user-attachments/assets/0d5520b3-b98f-4e11-849b-e226622801a8" width="80%" height="80%"/>

- 도커 데몬은 컨테이너를 관리하기 위한 API 명세를 제공한다. 
- 도커에게 명령을 전달하기 위해서는 API 명세에 맞게 전달되어야 도커를 실행하거나 관리할 수 있다.
- 도커는 사용자가 쉽게 도커에게 명령을 전달할 수 있도록 `클라이언트`에서 `Docker CLI(Command Line Interface)`를 제공한다.
- 사용자가가 클라이언트(Docker CLI)를 사용하여 명령어를 입력하면, 클라이언트는 이 명령어를 서버의 API 양식에 맞게 대신 만들어서 도커 데몬에게 전달해준다.
- 도커 데몬은 컨테이너 런타임을 통해서 컨테이너를 조작하고 그 결과를 CLI로 다시 전달한다.

    <img src="https://github.com/user-attachments/assets/57d38d63-bea3-4402-9dea-75a6bb7aad62" width="80%" height="80%"/>



<!--<img src="" width="80%" height="80%"/>-->
