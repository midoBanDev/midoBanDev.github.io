---
layout: single
title:  "[AWS] CloudFront"
categories:
  - Aws
tags:
  - [AWS, CloudFront]

toc: true
toc_sticky: true
---


## CloudFront









## 에지 로케이션(Edge Location)
에지 로케이션은 AWS의 콘텐츠 배포 네트워크(CDN, Content Delivery Network)를 구성하는 물리적 거점을 말한다. 
에지 로케이션은 전 세계적으로 리전보다 훨씬 많은 곳에 분포되어 있다. 
AWS가 전 세계 사용자에게 데이터를 빠르고 효율적으로 제공하기 위해 구축되어 데이터 처리 및 전달 지점 역할을 담당하고 있다.  
<img src="https://github.com/user-attachments/assets/48245603-58e9-4e92-ac0d-58e4e19e3e5c" width="90%" height="80%"/>

>CDN(Content Delivery Network)
CDN은 콘텐츠를 사용자에게 더 빠르고 안정적으로 전달하기 위해 설계된 분산 네트워크 시스템을 말한다. 
주로 웹 페이지, 동영상 스트리밍, 이미지와 같은 정적 콘텐츠를 전 세계 여러 위치에 미리 캐싱해두고, 사용자에게 가까운 서버에서 데이터를 제공함으로써 지연 시간을 줄이고 성능을 향상시키기는 역할을 한다.

AWS의 CloudFront는 이러한 CDN 서비스의 대표적인 예로, 에지 로케이션을 활용해 사용자와 가까운 위치에서 스트리밍 콘텐츠나 정적 파일을 제공함으로써 지연 시간을 줄이고 안정성을 높인다.

예를 들어, 서울에서 스트리밍 서비스를 운영하며 전 세계에 콘텐츠를 제공한다고 가정해자. 
모든 요청을 서울의 서버에서 처리한다면, 거리가 먼 지역에서는 속도가 느려지고 연결이 불안정해질 수 있다. 
하지만 AWS 에지 로케이션을 사용하면, 각 지역의 에지 로케이션에 콘텐츠를 미리 캐싱하여 사용자가 가까운 지점에서 데이터를 받을 수 있다. 
이를 통해 콘텐츠 전달 속도는 빨라지고 서버 부하도 줄어들게 됩니다.

에지 로케이션은 단순히 캐싱 기능만 제공하는 것이 아니다. 
CloudFront의 보안 기능과 연결되어, 데이터 전송 암호화 및 애플리케이션 계층의 보호 기능을 제공한다. 
이렇게 물리적으로 존재하는 에지 로케이션은 글로벌 AWS 네트워크의 핵심적인 구성 요소로, 사용자 경험을 극대화한다.



<img src="https://github.com/user-attachments/assets/0fc05b28-e00a-46d7-a40a-a20970587cc3" width="90%" height="80%"/>


<img src="https://github.com/user-attachments/assets/c14bf5bc-a165-407c-9976-1054b975bb92" width="90%" height="80%"/>


<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>