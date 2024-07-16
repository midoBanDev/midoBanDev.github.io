---
layout: single
title:  "가상화기술 및 하이퍼바이저 가상화"
categories:
  - Docker
tags:
  - [Docker, hypervisor]

toc: true
toc_sticky: true
---


## 가상화 기술
- Docker를 사용하기 전 우선 가상화 기술에 대해 알아보자.
- 가상 : 실제로 존재하지는 않지만 마치 존재하는 것처럼 보이는 현상.

<br>

- `가상화 기술`은 컴퓨터에서 사용되는 기술로써, 실제 존재하는 컴퓨터는 아니지만 마치 존재하는 컴퓨터가 있는 것처럼 만들어주는 기술이다.
- 가상화 기술을 이용하면 하나의 컴퓨터안에서 여러대의 컴퓨터를 실행시킬 수 있다. 

  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/8f6817d9-6b76-40a8-b116-7eed10715017" width="80%" height="80%"/>

<br>

- IT 산업에서는 실제로 존재하는 것을 `물리적`, 가상으로 존재하는 것을 `논리적`으로 존재한다고 표현한다.
- 가상화 기술을 기술적으로 다시 정의하자면 물리적인 컴퓨팅 환경 내부에서 논리적인 컴퓨팅 환경을 만들 수 있는 기술이라고 정의할 수 있다.

  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/7dee3dca-b8d8-4389-8005-e56da92d595f" width="80%" height="80%"/>


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 가상화 기술을 사용하는 이유
- 하나의 컴퓨터 환경에서 여러개의 프로그램을 실행하는 경우 
- 하나의 프로그램의 문제가 발생할 경우 다른 프로그램에 영향을 줄 수 있다.
- 또한 하나의 프로그램의 사용량이 갑자기 증가하여 컴퓨터의 리소스를 모두 소모하게 되면 나머지 프로그램은 정상적으로 작동하지 않을 수 있다.

  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/9cf7b8aa-eb72-49bd-b059-7aafb98839ca" width="80%" height="80%"/>


<br><br>

- 가상화 기술을 사용하면 하나의 컴퓨터에 여러대의 논리적인 OS 환경을 만들 수 있다.
- 논리적 OS 환경을 만든다는 것은 물리적 컴퓨터의 리소스를 각각 분배, 할당하여 독립적인(격리된) OS환경을 만든다는 것이다.
- 따라서 각각의 논리적 OS환경은 서로 격리되어 있기 때문에 서로에게 영향을 줄 수 없다.

<br>

- 여러 프로그램을 분리된 논리적 OS환경에서 실행하면 기존에 발생했던 문제들을 해결할 수 있다.
- 하나의 프로그램의 문제가 발생해도 논리적으로 분리된 OS환경에서 실행 중인 프로그램에 영향을 줄 수 없다.
- 하나의 프로그램의 사용량이 증가하여 리소스를 모두 사용하는 경우 본인의 논리적 OS환경의 리소스만 사용할 수 있기 때문에 다른 프로그램에 영향을 줄 수 없다.

  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/91d8b941-eab9-4a37-912a-2b0885f1fd1b" width="80%" height="80%"/>

<br>

**가상화 기술을 사용하면 각각의 소프트웨어를 안정적으로 실행하여 운영할 수 있다.**




<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>



## 하이퍼바이저 가상화

### 하이퍼바이저란
- 전통적인 가상화 기술 중 하나로 컴퓨터에 설치되는 프로그램이다.
- 하이퍼바이저는 가상화 환경을 관리할 수 있다.
- 가상 OS를 실행시키고, 종료할 수 있다.
- 가상 OS를 만들면 사용자가 지정해 놓은 CPU나 메모리만큼 컴퓨터의 격리된 공간을 만들 수 있다.

  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/61e4778b-2973-4018-89d2-31be02816f04" width="80%" height="80%"/> 

<br>

- 가상환경은 일반적인 프로그램과 비슷하다.
- 가상환경을 만들 때마다 프로그램을 설치하는 것처럼 `디스크 공간`을 차지한다.
- 또한 사용자가 지정한 만큼의 `CPU`와 `메모리`를 사용하게 된다.
- 위 이미지에서는 `가상환경1`과 `가상환경2`만 실행된 상태이다.
- `가상환경3`은 실행되지 않은 상태로 실행 전에는 `디스크 공간`만 차지하고 있다. 이후 실행하게 되면 그때 `CPU`와 `메모리`를 사용하게 된다.

<br>

**이제부터 위 환경을 실무적인 용어로 정리해보자**

- 물리적인 서버에 설치되는 OS를 `호스트OS(Host OS)`라고 부른다.
- `호스트OS(Host OS)`에 하이퍼바이저를 설치해서 가상환경을 만들 수 있다.
- 하이퍼바이저는 `호스트OS(Host OS)`의 자원을 격리해서 새로운 OS인 `게스트OS(Guest OS)`를 실행한다.
- `호스트OS(Host OS)`는 물리적인 하드웨어와 직접 연결되어 있고, `게스트OS(Guest OS)`는 호스트OS의 리소스를 나눈 논리적인 공간으로 존재한다. 이런 게스트OS를 `가상머신`이라고 부른다.
- 이 `가상머신`에서 `웹서버`나, `WAS`, `DB`같은 서버 프로그램을 프로세스로 실행해서 운영한다. `프로세스`는 실행 중인 프로그램을 의미한다.

  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/c47b99f4-0b64-49bc-863f-bc62f45836dc" width="80%" height="80%"/>

<br>

### 하이퍼바이저의 동작원리

- 프로세스는 실행되기 위해 `CPU`나 `메모리` 같은 리소스를 사용해야 한다.
- 프로세스가 하드웨어(CPU, 메모리)를 사용하기 위해서는 OS를 통해서만 사용할 수 있다.
- OS는 하드웨어를 사용하기 위해 `커널(Kernal)`이라는 도구가 설치되어 있다.
- 일반적인 프로그램인 웹브라우저나 문서 편집 프로그램의 프로세스들은 OS의 `커널`에게 하드웨어 리소스를 요청한다. 
- 하지만 이 요청은 매우 복잡하고 중요하기 때문에 `시스템 콜` 이라는 인터페이스를 통해 커널에 전달된다. 

  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/6da52b2d-30c6-4793-af5d-9c56a428b807" width="80%" height="80%"/>
- **정리하면 프로세스의 요청은 `시스템콜`을 통해 `커널`에 전달되고, `커널`은 하드웨어 자원에 접근하여 요청을 처리한 후 다시 `시스템콜`을 통해 프로세스로 반환된다.**

<br>

- OS는 대표적으로 `윈도우`와, `리눅스`, `맥OS`가 있다.
- 각각의 OS는 다른 종류의 커널을 사용한다. 이로 인해 `시스템 콜`도 다르다.
- 자 그럼 가상화 기술을 사용해서 가상머신에 리눅스OS와 맥OS를 실행한다고 생각해보자.
- 게스트OS의 커널은 실제로 물리적인 하드웨어가 없기 때문에 리소스를 사용하려면 호스트OS의 커널에 리소스 사용 요청을 해야한다.
- 그런데 호스트OS는 자신과 다른 OS을 가진 게스트OS의 리소스 요청을 처리할 수 없다.
- 여기서 `하이퍼바이저`가 다른 커널 간의 언어를 통역해 주는 역할을 수행한다.

  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/a71e5c50-aa93-400b-a153-66c4037c44b9" width="80%" height="80%"/>

<br>

- 하이퍼바이저는 특정 제품이 아닌 기술의 종류이기 때문에 하이퍼바이저의 역할을 수행하는 다양한 소프트웨어가 있다.
  - `VirtualBox`, `VMware`, `Red Hat RHEV(KVM)`


<!--<img src="" width="80%" height="80%"/>-->

