---
layout: single
title:  "도커 가상 네트워크"
categories:
  - Docker
tags:
  - [Docker, Network]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## NAT와 포트포워딩
네트워크 주소 변환(Network Address Translation, NAT)과 포트포워딩은 현대 네트워크 인프라의 핵심 구성 요소이다. 
NAT와 포트포워딩의 기본 개념과 동작 방식애 대해 알아보자.

NAT는 네트워크 주소를 변환하는 기술이다. 
주로 사설 IP 주소를 사용하는 내부 네트워크의 장치들이 하나의 공인 IP를 공유하여 외부 인터넷과 통신할 수 있게 해주는 기술이다. 
NAT는 IPv4 주소 고갈 문제를 해결하고, 내부 네트워크의 보안을 강화하는 데 크게 기여했다.

### NAT의 동작 방식

**1. 아웃바운드 트래픽 처리**  
아웃바운드 통신은 내부 네트워크에서 외부로 나가는 트래픽을 의미한다.  
과정은 다음과 같다.
1. 내부 호스트가 외부로 요청을 보내면, NAT 장비는 자동으로 NAT 테이블에 변환 정보를 기록한다.
2. 이 정보는 출발지 IP/포트, 목적지 IP/포트, 변환된 IP/포트 등을 포함한다.
3. 외부에서 응답이 돌아오면, NAT 테이블을 참조하여 원래 요청을 보낸 내부 호스트로 정확히 전달된다.
4. NAT 테이블의 엔트리는 일정 시간이 지나면 자동으로 삭제된다.

<img src="https://github.com/user-attachments/assets/4c151cdb-ce79-4c4e-b6c8-8798c1fc6ada" width="80%" height="80%"/>

<br>

**2. 인바운드 트래픽과 포트포워딩**  
기본적으로 NAT는 보안을 위해 외부에서 내부로의 직접적인 접근을 차단한다. 
하지만 웹 서버나 게임 서버와 같이 외부에서 접근해야 하는 서비스를 운영할 때는 포트포워딩이 필요하다.

1. 외부 클라이언트의 요청이 오면 NAT 장비가 이 요청을 수신한다.
2. NAT 장비는 수신한 패킷의 목적지 포트 번호를 확인한 후 포트포워딩 규칙이 설정된 NAT 테이블을 검색한다.
3. 매칭되는 포트포워딩 규칙을 찾으면, 목적지 주소를 내부 서버의 사설 IP로 변환한다. 필요한 경우 포트 번호도 함께 변환된다.
4. 변환된 주소 정보로 패킷을 내부 네트워크의 해당 서버로 전달한다. 내부 서버는 이 요청을 처리하고 응답을 생성한다.
5. 내부 서버의 응답은 다시 NAT 장비를 거치면서 출발지 주소를 공인 IP로 변환하여 외부 클라이언트에게 전송한다.

포트포워딩의 특징:
- 관리자가 수동으로 설정해야 한다.
- NAT 테이블에 영구적으로 저장된다.
- 설정된 포트 외의 인바운드 트래픽은 모두 차단된다.

<img src="https://github.com/user-attachments/assets/1302c6a8-64c8-4116-968b-d1249eeae677" width="80%" height="80%"/>

**NAT 테이블의 구조와 관리**  
NAT 테이블은 모든 주소 변환 정보를 관리하는 중요한 구성 요소이다.  
테이블에는 다음과 같은 정보들이 포함된다.
- 프로토콜 유형(TCP/UDP)
- 외부 IP 주소와 포트 번호
- 내부 IP 주소와 포트 번호
- NAT 엔트리의 상태 정보
- 타임아웃 정보


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>


## 도커의 가상 네트워크
도커는 가상 네트워크 기술을 활용해서 컨테이너의 네트워크를 구성한다. 
가상 네트워크는 실제로 인터넷 선이나 공유기가 없이 오로지 한 대의 서버 내에서 논리적으로 정의되어 있는 네트워크이기 때문에 가상 네트워크라고 부릅니다.

그러면 실제로 가상 네트워크가 어떻게 구성되는지 알아보자.
도커를 설치하고 실행시키면 도커는 가상의 네트워크인 `브릿지 네트워크`와 가상의 공유기인 `docker0`를 만듭니다.
여기서 가상의 공유기 `docker0`를 Docker에서는 `브릿지`라고 부릅니다.

이 브릿지는 일반적으로는 172.17.0.1의 가상 IP 주소를 할당 받는다.
가상 IP 주소는 실제로 존재하는 IP가 아닌 서버나 PC 안에서 논리적으로 정의되는 IP를 말한다.  
<img src="https://github.com/user-attachments/assets/b691e7e5-90a0-454e-b3f5-343a9bae87de" width="80%" height="80%"/>
<div class="br15"/>

도커의 브릿지 네트워크는 컨테이너가 실행하면 브릿지 네트워크의 IP 주소 범위 안에서 IP 주소를 하나 할당해 준다.
마치 공유기에 연결된 기기 한 대에 사설 IP가 할당되는 것과 동일하다.
차이는 물리적인 기계나 인터넷 선이 없이 오로지 소프트웨어적으로만 가상의 네트워크가 구성된다는 점이다.  
<img src="https://github.com/user-attachments/assets/5a58dd1f-da59-4c7c-b4a2-5c308cdd2c03" width="80%" height="80%"/>
<div class="br15"/>

컨테이너를 여러 대 실행하면 각각의 컨테이너들은 같은 대역대에서 겹치지 않는 IP를 할당받는다.
이렇게 같은 브릿지에서 생성된 네트워크는 브릿지를 통해서 서로 통신할 수 있다.
172.17.0.2의 IP를 가진 컨테이너1에서 172.17.0.5로 요청을 보내면 브릿지를 통해서 4번 컨테이너로 네트워크 패킷이 전달된다.  
<img src="https://github.com/user-attachments/assets/a46cd497-908f-4ca7-b330-e316b706141f" width="80%" height="80%"/>
<div class="br15"/>

정리하자면 도커는 브릿지 네트워크를 통해서 가상 네트워크를 구성하고 컨테이너의 IP를 할당하면서 컨테이너 간의 통신을 전달해 준다.
이렇게 논리적으로 네트워크 환경을 구성하는 기술을 `SDN(Software Defined Network)`라고 부른다.


가상 네트워크에서 네트워크 신호가 어떤 원리로 전달되는지 알아보자.

일반적인 PC나 서버를 설치하면 물리적인 네트워크 인터페이스에 인터넷 선을 꽂아서 사용하게 된다. 
이렇게 물리 인터페이스에 인터넷 선을 연결하면 기기의 공인 IP나 사설 IP를 할당받는다. 
그리고 해당 PC 위에 도커를 설치하고 실행하면 도커는 가상의 인터페이스를 한 개 생성한다. 
그리고 이 가상의 인터페이스는 호스트 OS에 만들어진다.
<img src="https://github.com/user-attachments/assets/895440d2-e607-4efe-af75-2c1e6a4166e6" width="80%" height="80%"/>
<div class="br15"/>

컨테이너들을 실행하면 각각의 컨테이너에 해당하는 가상 인터페이스들이 `Veth`라는 접두어로 호스트 OS에 만들어진다.
이 `Veth` 뒤에는 랜덤하게 고유한 번호가 할당된다.
최종적으로 호스트 OS에서는 실제로 존재하는 하드웨어인 물리 인터페이스 1개와 소프트웨어로 만들어진 가상의 인터페이스가 5개 존재한다.
<img src="https://github.com/user-attachments/assets/1ba800a2-fa1b-4a2b-968d-e82aed14976a" width="80%" height="80%"/>
<div class="br15"/>

가상의 인터페이스로 전달되는 네트워크 신호는 각각의 인터페이스에 해당되는 컨테이너로 전달된다. 
예를 들어서 호스트 OS에서 Veth2 인터페이스로 네트워크 신호를 보내면 Veth2 인터페이스는 컨테이너 2번으로 네트워크 신호를 전달한다. 
<img src="https://github.com/user-attachments/assets/ae921c37-b27c-4c2b-8a2e-1ff5c4b1406f" width="80%" height="80%"/>
<div class="br15"/>

이 구조에서 1번 컨테이너에서 2번 컨테이너로 요청을 보내면 1번 컨테이너에서는 172.17.0.3을 도착지로 해서 요청을 보낸다. 
그러면 172.17.0.3으로 보내는 요청은 veth1 인터페이스에서 출발해서 veth2 인터페이스로 전달되어야 한다.
물리 네트워크에서는 이렇게 도착지의 IP에 맞게 네트워크를 통해서 기기에 전달하는 것을 네트워크 장비들이 알아서 해주지만 
가상 네트워크에서는 별도의 장비가 없기 때문에 소프트웨어적으로 트래픽을 전달해줘야 한다. 

여기서 가상의 인터페이스 간의 네트워크 패킷을 전달하는 규칙은 호스트 OS의 커널 소프트웨어인 `Iptables`에 정의된다. 
**'172.17.0.3으로 향하는 요청은 Veth2 인터페이스로 전달하자'** 라는 규칙이 이 Iptables 안에 정의되어 있다. 
Iptables는 Linux OS의 패킷 필터링 시스템으로 서버 내부에서 네트워크 트래픽을 제어하고 라우팅 규칙을 정의한다. 
그래서 내부에서 발생하는 네트워크 트래픽은 Iptables에 정의된 대로 흘러가게 되어있다. 
<img src="https://github.com/user-attachments/assets/b541a576-eacb-4fea-9e62-611c90531810" width="80%" height="80%"/>
<div class="br15"/>

Iptables에는 특정 IP 주소로 네트워크를 보냈을 때 어떤 인터페이스로 전달할지에 대한 규칙이 설정되어 있다.
컨테이너 1번에서 172.17.0.3으로 네트워크 요청을 보내면 Iptables의 규칙에 따라서 Veth2번으로 전달되고 Veth2 인터페이스를 통해서 컨테이너 2로 전달된다. 
그리고 도커는 컨테이너가 생성되거나 삭제될 때마다 호스트 OS의 Iptables 규칙을 업데이트한다.
<img src="https://github.com/user-attachments/assets/45bb5d31-e2dd-4bae-ae0d-14e92e99c638" width="80%" height="80%"/>
<div class="br15"/>

정리하자면 도커는 컨테이너의 통신을 위해서 `브리지 네트워크`를 정의하고 호스트 OS에 `가상 인터페이스`들을 생성한다.
그리고 호스트 OS의 Iptables 규칙을 관리하면서 가상 인터페이스들 간의 통신 규칙을 만든다. 
따라서 사용자의 별도 설정 없이 같은 브리지 네트워크에서 생성된 컨테이너들은 서로 통신을 할 수 있는 상태로 구성된다.

도커는 가상 네트워크 내부에서 여러 개의 브릿지 네트워크를 관리할 수 있다. 
기본적으로 같은 네트워크 안에 속해 있는 컨테이너들끼리는 통신이 가능하기 때문에 브릿지 네트워크 단위로 분리하게 되면 특정 컨테이너들끼리는 통신이 되지 않도록 구성할 수도 있다. 
<img src="https://github.com/user-attachments/assets/7a9929df-2a26-4076-83e9-39e7abb27ef3" width="80%" height="80%"/>


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## 네트워크 명령어

<br>

**네트워크 목록 조회**

```bash
$ docker network ls
```
- `NETWORK ID` : 네트워크 고유 식별자
- `NAME` : 네트워크 이름
- `DRIVER` : 네트워크의 종류
  - bridge: 단일 호스트 내 컨테이너 간 통신을 위한 기본 네트워크 드라이버로 도커 브릿지 를 활용해 컨테이너간 통신통신, NAT 및 포트포워딩 기술을 활용해 외부 통신 지원한다.
  - host: 컨테이너가 호스트의 네트워크를 공유하며, 모든 컨테이너는 호스트 머신과 동일한IP를 사용한다. 포트는 중복이 불가능하다.
  - none: 모든 네트워킹을 비활성화
  - overlay: 여러 호스트 머신 간 컨테이너 통신을 위한 드라이버이다. 쿠버네티스에서 사용한다.
  - macvlan: 컨테이너에 MAC 주소를 할당하여 물리 네트워크 인터페이스에 직접 연결을 제공한다.
- `SCOPE` : 네트워크 범위
  - local: 단일 호스트에서만 사용 가능한 네트워크
  - swarm: 도커 스웜의 모든 노드에서 사용 가능한 네트워크
  - global: 글로벌 범위의 네트워크

<br>

**네트워크 메타데이터 조회 : `docker network inspect <네트워크이름/ID>`**

```bash
$ docker network inspect bridge
```
- Name: bridge
  - 도커의 기본 브리지 네트워크 이름

- IPAM (IP Address Management)
  - Subnet: 이 네트워크에서 사용하는 IP 대역
  - Gateway: 브리지 네트워크의 게이트웨이 주소

- Containers
  - 현재 이 네트워크에 연결된 컨테이너 목록
  - 각 컨테이너의 IPv4 주소와 MAC 주소 정보 포함

>아래 이미지는 loan-network 라는 별도로 만든 네트워크의 메타데이터를 조회한 결과이다. 
<img src="https://github.com/user-attachments/assets/86fcf209-721f-4251-839c-459aba63e15d" width="80%" height="80%"/>

<br>

**네트워크 추가** : docker network create <생성할네트워크명>

```bash
$ docker network create mynetwork
```
- `--driver` 옵션을 지정하지 않으면 기본적으로 'bridge' 드라이버가 사용된다.
- `--subnet` 옵션을 지정하지 않으면 기본적으로 172.16.0.0/12 범위 내에서 사용하지 않는 subnet을 찾아 할당한다.
(172.16.0.0 ~ 172.31.255.255)

<br>

**새로운 브릿지 네트워크 생성**
  
```bash
$ docker network create --driver bridge --subnet 10.0.0.0/24 --gateway 10.0.0.1 second-bridge
```
<img src="https://github.com/user-attachments/assets/36244369-15ee-4518-8f71-1aad07b9515b" width="80%" height="80%"/>


**컨테이너에 네트워크 추가, 변경, 삭제**
- 기본 네트워크(--network 옵션으로 처음 지정한 네트워크)는 실행 중에 변경할 수 없다. 기본 네트워크를 변경하려면 컨테이너를 중지, 삭제 한 후 새로운 네트워크로 컨테이너를 재생성해야 한다.

- (실행 중인)컨테이너에 네트워크를 추가할 수 있다.
```bash
# docker network connect <네트워크명> <컨테이너명/ID>
$ docker network connect mynetwork mycontainer
```
- (실행 중인)컨테이너에 네트워크를 제거(연결 해제)할 수 있다.
```bash
# docker network disconnect <네트워크명> <컨테이너명/ID>
$ docker network disconnect mynetwork mycontainer
```


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## 브릿지 네트워크
브릿지 네트워크는 동일한 도커 호스트 내에서 실행되는 컨테이너들이 서로 통신할 수 있게 해주는 가상 네트워크이다. 
브릿지 네트워크는 여러 개 생성할 수 있으며, 각 컨테이너는 여러 브리지 네트워크에 동시에 연결될 수 있다. 
이는 마치 물리적인 컴퓨터에 여러 개의 네트워크 인터페이스를 설치하는 것과 유사하다.

컨테이너 실행 시 별도의 네트워크를 지정하지 않으면 자동으로 `bridge` 네트워크에 속하게 된다.
이렇게 생성된 컨테이너들은 같은 네트워크로 묶이게 되어 별도 설정없이 서로 통신이 가능하다.

브릿지 네트워크는 독립적인 네트워크 영역과 IP 대역을 사용할 수 있다. 
브릿지 네트워크가 다른 컨테이너들은 직접적인 통신이 불가능하다. 
이는 보안적으로 유리한 이점을 제공한다.

<img src="https://github.com/user-attachments/assets/36e3e8c0-5497-4ff0-b456-6fa3d1d36958" width="80%" height="80%"/>

컨테이너는 여러 브릿지 네트워크에 동시 연결이 가능하다. 
또한 이미 실행 중인 컨테이너에 네트워크를 추가할 수도 있다. 
이는 독립된 네트워크를 가진 컨테이너들을 연결할 수 있는 유연성을 제공한다.

### 브릿지 네트워크 실전 예시

- 브릿지 네트워크의 특징을 활용하면 `3-tier` 아키텍처를 구성할 수 있다.
- 이는 실제 프로덕션 프로젝트 운영에 보안상 이점을 제공한다.
```plaintext
[Frontend Network]
└── Frontend (React/Vue etc.)
    └── ↕️ (API 통신)
        Backend (Node/Spring etc.) ──┐
                                     ↓
[Backend Network]                    │
└── Database (MySQL/Postgres etc.) ←─┘
```

- 기본 웹 애플리케이션 구조인 `front` + `backend` + `db` 구조를 보자.
- backend 와 db는 직접적인 연결 구조를 가진다. 하지만 front 에서 db가 접근되는 것은 위험하다.
- 물리서버를 구축할 때도 front 서버는 DMZ망에 구성하고, backend와 db 서버는 내부망에 구성한다.
- 이와 마찬가지로 도커 컨테이너 네트워크를 구성할 수 있다.

**3-Tier 아키텍처**  
```bash
# 네트워크 생성
docker network create frontend-tier
docker network create backend-tier

# Web 서버 배포
docker run -d --name nginx \
  --network frontend-tier \
  -p 80:80 \
  nginx

# API 서버 배포 (양쪽 네트워크에 연결)
docker run -d --name api \
  --network backend-tier \
  my-api-image
docker network connect frontend-tier api

# Database 배포
docker run -d --name postgres \
  --network backend-tier \
  -e POSTGRES_PASSWORD=secret \
  postgres
```

이 구성에서:
- nginx는 frontend-tier 네트워크를 통해 api와 통신
- api는 backend-tier 네트워크를 통해 postgres와 통신
- nginx는 postgres와 직접 통신 불가


## 체크 사항
실제 컨테이너를 3-tier로 구성했는데 front에서 db로 ping이 간다. 
IP로는 접근이 안되는데 도메인으로 접근이 되는 상황.
추후 확인해 보자.


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>


## 도커 포트포워딩
가상 네트워크도 NAT 기술을 사용하여 내부에서 외부로, 외부에서 내부로 접근할 수 있다. 
가상 네트워크 밖에 있는 호스트 OS에서 컨테이너에 접근하기 위해서는 포트포워드 기술을 사용해야 한다.

**포트포워딩 p 옵션 : `docker run -p <HostOS의포트의포트>:<컨테이너의포트>`**

```bash
$ docker run -p 80:80 nginx
```

- 포트포워드가 설정된 호스트 OS와 같은 네트워크에 있는 다른 호스트도 컨테이너에 접근할 수 있다.
- 단 포트포워드가 설정된 호스트를 통해서만 접근이 가능하다.
- 예를 들어 집에 있는 노트북 호스트(192.168.0.10)와 핸드폰 호스트(192.168.0.20)가 하나의 공유기를 통해 할당받은 IP를 사용하고 있다면 같은 네트워크에 있다고 볼 수 있다.
- 그리고 노트북을 호스트 OS로 내부에 컨테이너가 존재한다면 핸드폰에서 노트북 호스트 IP인 192.168.0.10를 통해 내부 컨테이너에 접근할 수 있다.
<img src="https://github.com/user-attachments/assets/6ab3486c-fedf-4e74-b08d-21db145a3703" width="80%" height="80%"/>

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## 도커 DNS
도커는 컨테이너들이 기본적으로 사용할 수 있는 DNS 서버를 제공한다. 
DNS 서버에는 `IP 주소`와 `도메인 명`이 저장되어 있다. 
여기서 도메인은 `컨테이너의 이름`으로 자동으로 저장된다. 
그래서 컨테이너들은 기본적으로 컨테이너의 IP가 아닌 컨테이너의 이름으로 서로 통신할 수 있다. 

<img src="https://github.com/user-attachments/assets/c4d76a94-e230-4f6c-9d04-fad10263cffb" width="80%" height="80%"/>

DNS 서버는 외부의 DNS 서버와 연동이 되어 있기 때문에 컨테이너는 구글 같은 외부 도메인도 사용할 수 있다. 
다만 기본으로 생성되는 브릿지 네트워크는 이 DNS 기능이 제공되지 않는다.
사용자가 직접 생성한 브릿지만 컨테이너의 이름을 통해서 통신할 수 있다. 

도메인 주소를 사용하면 IP 주소가 바뀌는 환경에서 다른 서버들이 영향을 받지 않고 연결을 일관적으로 유지할 수 있다.

<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>