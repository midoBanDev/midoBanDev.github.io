---
layout: single
title:  "컨테이너 가상화"
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

## 컨테이너 가상화
- 가상화 기술 중 하나로 현대 애플리케이션 운영 환경에서 하이퍼바이저 방식보다 더 선호되는 가상화 기술이다.
- 컨테이너 가상화 방식은 하이퍼바이저 가상화 방식보다 더 빠르고, 더 가볍다는 장점이 있다.

  <img src="https://github.com/user-attachments/assets/9595c5ba-f6fb-42fb-b031-ccc3b13f8dd1" width="80%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 컨테이너 가상화 방식의 특징
- 하이퍼바이저 방식에서는 격리된 공간을 만드는 역할을 하이퍼바이저 소프트웨어가 했지만 컨테이너 가상화 방식에서는 그 담당을 커널 자체가 한다.
- `컨테이너 가상화`는 리눅스 커널이 제공하는 `LXC` 라는 자체 격리 기술에서 발전했다.
- 커널의 LXC 기술을 사용하여 만들어진 격리된 공간을 `컨테이너`라고 부른다.
- `컨테이너 가상화`는 리눅스 커널이 제공하는 `LXC(Linux Containers)` 라는 자체 격리 기술에서 발전했다.
- LXC는 리눅스 커널의 `네임스페이스(namespaces)`와 `c그룹(cgroups)` 기술을 기반으로 한다.
  - `namespaces`는 프로세스를 격리된 환경에서 실행할 수 있게 해준다.
  - `cgroups`는 프로세스의 리소스 사용을 제한하고 관리한다.
- 커널의 LXC 기술을 사용하여 만들어진 격리된 공간을 `컨테이너`라고 부른다.
- 현대의 도커는 초기 버전의 LXC 대신 자체적인 `containerd`와 `runc`를 사용한다.
  - `containerd`는 컨테이너의 전반적인 생명주기를 관리하는 고수준 런타임이다. 도커 데몬과 저수준 런타임(runc) 사이의 중간 계층이다.
  - `runc`는 실제로 리눅스 커널의 격리 기능(namespaces, cgroups)을 사용하여 컨테이너를 생성하고 실행하는 저수준 런타임이다.
- 이러한 컨테이너는 `호스트 OS의 커널을 공유`하면서도 `독립된 실행 환경`을 제공한다.
- 컨테이너들은 각각의 독립된 커널이 없기 때문에 커널 간의 통신을 할 필요가 없어져 `오버헤드`가 적고(가볍고), 컨테이너의 `실행 시간`이 빠르다.
- 단 컨테이너는 호스트OS의 커널을 공유하기 때문에 다른 종류의 OS를 실행할 수 없다.

  <img src="https://github.com/user-attachments/assets/2892229f-83c5-422f-94ba-b486607eb7af" width="80%" height="80%"/>


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 도커가 필요한 이유
- 커널이 자체적으로 제공하는 가상화 기술은 사용자가 직접 컨트롤하기 어렵다.
- `도커(Docker)`는 이 커널의 컨테이너 가상화 기술을 편리하게 사용하기 위해 만들어진 스프트웨어이다.
- 사용자는 `도커(Docker)`를 통해서 컨테이너를 만들고 운영할 수 있다.

  <img src="https://github.com/user-attachments/assets/32f5366a-9d4b-47fe-8b25-721a0d7a8ba7" width="80%" height="80%"/>


<!--<img src="" width="80%" height="80%"/>-->
