---
layout: single
title:  "[AWS] 보안그룹(Security Group)"
categories:
  - Aws
tags:
  - [Aws, EC2, SecurityGroup]

toc: true
toc_sticky: true
---

## 보안 그룹
AWS의 보안 그룹은 **EC2 인스턴스에 대한 인바운드 및 아웃바운드 트래픽을 제어하는 가상 방화벽 역할**을 담당하는 서비스이다.

AWS의 대부분 보안 관련 정책은 “내가 명시적으로 허용하지 않으면 사용할 수 없다”는 원칙을 따른다. 
기본적으로 **모든 포트는 비활성화**되어 있으며, 명시적으로 허용해 줘야만 사용할 수 있다. 
따라서 선택적으로 트래픽이 지나갈 수 있는 포트와 소스를 허용해야 한다. 

반면, 트래픽을 명시적으로 **차단(Deny)** 하기 위해서는 **NACL(Network ACL)**이라는 서비스를 이용해야 한다.

보안 그룹은 **인스턴스 단위**로 동작한다. 
정확히는 **Elastic Network Interface(ENI)**라는 네트워크 인터페이스 단위로 동작하지만, 
일단은 **인스턴스 단위로 동작한다**고 이해하면 된다. 
하나의 인스턴스에 **하나 이상의 보안 그룹을 설정**할 수 있으며, 여러 보안 그룹이 적용될 경우 **모든 보안 그룹의 규칙**이 적용된다.



<div style="padding-top:60px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:60px;"></div>


<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>

