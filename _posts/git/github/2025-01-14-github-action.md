---
layout: single
title:  "Github Actions"
categories:
  - Github
tags:
  - [Github, Github-Actions]

toc: true
toc_sticky: true
---

## 파이프라인(Pipeline)
파이프라인(pipeline)은 물이 흐르는 관을 의미한다. 
파이프라인의 시작 지점에 물을 넣으면 파이프라인의 끝까지 물이 자동으로 흐르게 된다. 
IT 운영 환경의 파이프라인 개념에서는 파이프라인 안에 물 대신에 소스코드가 흘러 간다고 생각하면 된다.
 
첫 지점에 넣은 소스코드는 파이프라인을 흐르면서 아티팩트로 빌드되고 이 아티팩트가 운영 환경까지 배포된다. 
이런 일련의 과정들이 자동화되어서 개발자가 소스코드를 푸시했을 때 배포까지 자동화가 이루어지는 것을 `CI/CD 파이프라인`이라고 부른다.
 
CI/CD 파이프라인은 개발자나 운영자, QA 담당자, 테스트 담당자 같은 사람들이 하던 각각의 작업을 자동화시켜서 각각의 환경에 배포까지 자동화하는 것이 목표이다.


## CI(Continuous Integration)/CD(Continuous Delivery/Deployment)
일반적으로 애플리케이션이 최종적으로 서비스되기 까지의 과정을 보면 크게 두가지로 나눌 수 있다.
- 소스 코드를 푸시하고 애플리케이션이나 이미지로 빌드하는 과정
- 테스트하고 배포하는 과정

큰 프로젝트는 일반적으로 여러 개발자들이 공동으로 개발한다. 
그리고 개발자들은 각자 개발한 내용을 한 저장소(github)에 모은(커밋 & 푸시) 후 최종 통합 소스 코드를 실제 운영 환경(운영 server)에 배포한다. 
소스 코드를 한 저장소에서 관리하기 위해서는 가각 개발된 소스 코드를 잘 병합해야 한다. 
하지만 병합 과정에서 많은 오류가 발생한다. 이는 또 다른 오류를 발생시키는 원인이 된다. 

이런 오류를 최소화 할 수 있는 방법은 작은 단위로 커밋하고 푸시하는 방법이다. 큰 기능을 작은 단위로 쪼갠 후 자주 커밋해야 한다. 
커밋은 소스 코드의 오류를 잘 찾을 수 있는 방법 중에 하나이다. 그래서 잦은 커밋은 오류를 줄일 수 있다. 
오랜시간 개발로 쌓인 소스 코드는 재앙의 시작이 될 수 있다. 
이렇게 자주 커밋하고 푸시하는 방식을 `CI(Continuous Integration), 지속적인 통합`이라고 말한다. 

CI(Continuous Integration)는 오류를 줄이고 개발자들이 소스 코드 병합에 적은 시간을 사용하는데 유용하다. 


이후 테스트하고 배포하는 과정을 자동화 하는 방법을 `CD(Continuous Delivery/Deployment), 지속적인 배포`라고 말한다. 
실제 환경에 아티팩트를 배포하는 단계이다.

CI/CD 파이프라인을 구성하면 이 전체 전체 과정을 자동화 할 수 있다.


## Github Actions
GitHub Actions는 빌드, 테스트 및 배포 파이프라인을 자동화할 수 있는 CI/CD(연속 통합 및 지속적인 업데이트) 플랫폼이다.


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>


<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>