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

## 컨테이너 가상화
- 가상화 기술 중 하나로 현대 애플리케이션 운영 환경에서 하이퍼바이저 방식보다 더 선호되는 가상화 기술이다.
- 컨테이너 가상화 방식은 하이퍼바이저 가상화 방식보다 더 빠르고, 더 가볍다는 장점이 있다.

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 컨테이너 가상화 방식의 특징
- 하이퍼바이저 방식에서는 격리된 공간을 만드는 역할을 하이퍼바이저 소프트웨어가 했지만 컨테이너 가상화 방식에서는 그 담당을 커널 자체가 한다.
- `컨테이너 가상화`는 리눅스 커널이 제공하는 `LXC` 라는 자체 격리 기술에서 발전했다.
- LXC 기술을 사용하면 커널의 자체 기능만을 사용해서 격리된 공간을 만들 수 있다.
- 컨테이너 가상화는 커널의 격리 기능을 활용하기 때문에 호스트OS의 커널을 공유해서 사용한다.
- 컨테이너들은 각각의 독립된 커널이 없기 때문에 커널 간의 통신을 할 필요가 없어져 `오버헤드`가 적고(가볍고), 컨테이너의 `실행 시간`이 빠르다.
- 단 컨테이너는 호스트OS의 커널을 공유하기 때문에 다른 종류의 OS를 실행할 수 없다.

  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/dae010ea-8b8f-411d-a5a5-1bfc93974103" width="80%" height="80%"/>


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 도커가 필요한 이유
- 커널이 자체적으로 제공하는 가상화 기술은 사용자가 직접 컨트롤하기 어렵다.
- `도커(Docker)`는 이 커널의 컨테이너 가상화 기술을 편리하게 사용하기 위해 만들어진 스프트웨어이다.
- 사용자는 `도커(Docker)`를 통해서 컨테이너를 만들고 운영할 수 있다.

  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/ff06a15a-b643-4183-b6e8-1fb50fb014d6" width="80%" height="80%"/>


<!--<img src="" width="80%" height="80%"/>-->
