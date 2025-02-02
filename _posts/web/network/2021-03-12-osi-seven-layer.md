---
layout: single
title:  "OSI 7 Layer Model"
categories:
  - Network
tags:
  - [Network, OSI7]

toc: true
toc_sticky: true
---

<br>

**본 내용은 AWS 강의실님 강의 "[AWS를 이해하기 위한 기초지식 : OSI 7 Layer](https://www.youtube.com/watch?v=DufRXdDF9zI&list=PLfth0bK2MgIYuZguspeqGFdXXVckWwrlg)" 내용을 바탕으로 정리한 내용입니다.**

<br>

# OSI(Open Systems Interconnection) 7 레이어 모델
OSI(Open Systems Interconnection) 7 레이어 모델은 컴퓨터 네트워크 및 통신을 7개의 계층으로 나누어 설명하는 개념적 모델이다. 
이 모델은 국제표준화기구(ISO)에서 제안한 표준으로, **서로 다른 시스템 간의 네트워크 통신**을 원활하게 수행할 수 있도록 설계되었다. 

OSI 모델은 계층적인 구조를 가지며, 각각의 계층은 특정한 기능을 수행하고, 상위 계층과 하위 계층 간에 데이터를 전달하는 역할을 한다. 
일반적으로 하위 계층에서 상위 계층으로 데이터가 전송되며, 반대로 상위 계층에서 하위 계층으로도 데이터가 전달된다.

OSI 7 레이어 모델은 밑에서부터 **Physical, Data Link, Network, Transport, Session, Presentation, Application** 이렇게 구성이 된다.  
<img src="https://github.com/user-attachments/assets/494b1e9a-9f76-4b5e-a0a1-c4c35877a909" width="90%" height="80%"/>

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## 물리 계층(Physical Layer)
피지컬 레이어는 OSI 7 레이어 중에 맨 밑에 위치하고 가장 기본적인 레이어다.

피지컬 레이어에서는 장치를 연결하기 위한 매체의 물리적인 사항을 정의한다.
예를 들어 전압, 주기, 시간, 전선의 규격, 거리 등 통신을 하기 위해서 가장 기본적인 매체들을 어떻게 사용할지를 정의한다. 

피지컬 레이어의 주요 단위는 비트이며, 비트의 대표적인 구성 요소는 케이블, 안테나, RF 등 전송 매체들이 있고, 그 다음에 허브와 리피터가 있다.  

피지컬 레이어에서 정의하는 것은 두 주체 간에 어떤 방식으로 데이터를 교환할 것인가에 대한 물리적인 내용이다. 
그래서 와이파이, 블루투스, 광섬유, 구리선 등 다양한 방법과 매체로 데이터를 전송할 수 있다.  
<img src="https://github.com/user-attachments/assets/e20911e7-ebfd-44ad-ad30-af98394ec7c3" width="90%" height="80%"/>


피지컬 레이어를 좀 더 구체적으로 보면 1과 0으로 이루어진 비트라고 불리는 데이터 스트림을 어떤 방식으로 전송하는 가에 대한 것이다. 
예를 들어, 구리선이라면 전압이 플러스면 1이고, 마이너스면 0으로 정의하여 전송할 수 있다. 
혹은 전파라면 전파가 있으면 1이고, 없으면 0으로 전달할 수 있다. 
즉 물리적으로 어떻게 비트 스트림을 전달할 것인가에 대한 것이 바로 피지컬 레이어이다.  
<img src="https://github.com/user-attachments/assets/e1737217-ab90-4ec2-a909-8e851d2d523a" width="90%" height="80%"/>


자, 그럼 이제 아래와 같이 물리적으로 연결을 수립했다고 가정해보자.   
<img src="https://github.com/user-attachments/assets/3836e9b4-c0bb-4d2c-a974-38b829d7934e" width="90%" height="80%"/>  

그런데 주체가 많으면 많을수록 네트워크가 복잡진다. 
여기서는 네 개만 있어서 그렇지만, 다섯 개, 여섯 개, 일곱 개 이렇게 추가된다면 더 복잡한 연결이 수립되어야 하고,
이를 하나하나 관리하기가 더 어려울 것이다. 그래서 이를 중앙으로 모은 것이 바로 허브이다.  
<img src="https://github.com/user-attachments/assets/142823a3-cec9-4102-b584-329877388765" width="90%" height="80%"/>

이 허브는 피지컬 레이어 단위에서 다수의 기기들을 연결해 주는 장치이며, 특징은 받은 내용을 무조건 **브로드캐스트 방식**으로 그대로 전달하는 역할만 한다. 

예를 들어 클라이언트 A가 뭔가 요청을 보냈다면, 허브는 이를 그대로 모든 기기에 뿌려준다. 마치 확성기처럼 작동하게 된다. 
마찬가지로 클라이언트 C가 어떤 데이터를 전송하면, 허브는 이를 연결된 모든 기기에 전달해 주는 역할을 한다. 
이렇게 전달 받은 요청을 모든 대상에게 전달하는 방식을 **브로드캐스드(BroadCast) 방식**이라 한다.  
<img src="https://github.com/user-attachments/assets/f6b1c1bf-8c66-4c0b-91d4-989b1c3972b9" width="45%" height="80%"/>
<img src="https://github.com/user-attachments/assets/dfbeeb36-f37e-4ff0-b184-5612503e0e5c" width="45%" height="80%"/>

그런데 이렇게 물리적인 선으로 연결되어 있는 클라이언트 A, B, C, D가 동시에 데이터를 전송하면 어떻게 될까? 
모든 장치가 동일한 선을 사용하고 있기 때문에 데이터가 충돌하여 누구도 정상적으로 수신할 수 없는 상황이 발생한다. 
이를 **충돌(Collision)**이라고 한다.
<img src="https://github.com/user-attachments/assets/7fb62165-f6f0-4428-a251-ab62cc39ec5b" width="90%" height="80%"/>

네트워크 내에서 데이터 충돌이 발생할 수 있는 영역을 **콜리전 도메인(Collision Domain)**이라 한다. 
허브를 사용하여 연결된 모든 클라이언트는 하나의 콜리전 도메인에 속하게 된다. 
이는 모든 클라이언트가 서로에게 영향을 주는 충돌 범위에 속해 있기 때문이다.  

기본적으로 Layer1에서는 누군가 데이터를 전송하고 있는 경우, 즉 전기 신호가 전선을 점유하고 있으면 다른 장치는 물리적으로 통신이 불가능하다. 
하지만 동시에 데이터를 전송되는 상황에서는 충돌이 발생하게 된다. 

그런데 허브는 이 충돌을 막거나 조절할 수 있는 기능이 없다. 
왜냐하면 허브는 단순히 받은 데이터를 모든 장치에게 그대로 전달하는 역할만 하기 때문이다.

또한, 허브에는 또 다른 문제가 있다. 
예를 들어, 클라이언트 A가 클라이언트 B에게만 데이터를 전송하고 싶거나 A가 C에게만 특정 데이터를 보내고 싶을 수도 있다. 
하지만 허브를 통해 연결된 네트워크에서는 이런 개별적인 통신이 불가능하다. 
왜냐하면 허브는 특정 대상을 지정하는 기능 없이 모든 데이터 패킷을 연결된 모든 장치에 무조건 전달하기 때문이다. 

이러한 이유로 **피지컬 레이어에서는 충돌 방지나 특정 대상에게만 데이터를 전송하는 기능을 제공하지 않는다.** 
이러한 기능을 구현하려면 한 단계 위의 **데이터 링크 레이어(Data Link Layer)**까지 올라가야 한다.


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## 데이터 링크 계층(Data Link Layer)
**데이터 링크 레이어(Data Link Layer)**는 물리적인 통신을 제어하여 디바이스 간의 안정적인 통신 및 데이터 전송을 지원하는 역할을 한다.  
- 주요 단위는 **프레임(Frame)**이다.
- 주요 구성 요소는 **MAC 주소(Address), 스위치(Switch)** 등이 포함된다.

데이터 링크 레이어의 중요한 기능은 **CSMA/CD**(Carrier Sense Multiple Access with Collision Detection)과 **유니캐스트**(Unicast)이다.  
- **CSMA/CD**는 간단히 말하면 네트워크에서 디바이스들이 충돌 없이 원활하게 통신할 수 있도록 하는 방식을 말한다. 
- **유니캐스트(Unicast)**는 특정 대상 디바이스를 구별하여 통신을 지원하는 기능을 제공한다.

**브로드캐스트(Broadcast)**는 모든 장치에게 데이터를 전달하는 방식이며, **유니캐스트(Unicast)**는 특정 대상에게만 데이터를 전달하는 방식이다. 
데이터 링크 레이어는 기본적으로 브로드캐스트를 지원하면서도, 유니캐스트를 통해 개별적인 통신이 가능하도록 한다.

<br>

### MAC 주소(MAC Address)
데이터 링크 레이어의 구성 요소인 **MAC 주소(MAC Address)**는 네트워크 인터페이스에 부여된 고유한 주소를 말한다. 
MAC 주소는 데이터를 정확한 대상에게 전달하기 위해 사용되며, 특정 디바이스를 식별하는 역할을 한다. 

예를 들어, 누군가에게 소포를 보내고 싶다면 집 주소가 필요하고, 메시지를 보내고 싶다면 상대방의 아이디가 필요하듯이, 
네트워크에서도 특정 디바이스로 데이터를 보내려면 식별 가능한 주소가 필요하다. 바로 이 역할을 하는 것이 **MAC 주소**이다. 

MAC 주소는 **16진수(헥사데시멀) 형식**으로 구성되며, **6쌍(총 12자리)**의 문자로 표현된다. 
- 예: 00:1A:2B:3C:4D:5E

MAC 주소는 총 **48비트(6바이트)**로 구성되며, 두 부분으로 구분한다. 
- **앞 3바이트는 OUI(Organizationally Unique Identifier)**: 제조업체가 네트워크 인터페이스를 만들 때 부여받은 고유 번호이다.
- **나머지 3바이트는 NIC(Network Interface Controll)**: 제조업체가 개별 네트워크 인터페이스에 부여하는 고유 번호이다.

같은 제조사에서 만든 장치라면 **OUI 부분은 동일**하지만, **개별 네트워크 인터페이스별 고유 번호는 다르게 부여된다.**  
반대로, 다른 제조사에서 만든 장치라면 **OUI 값도 다르게 설정된다.**  

결론적으로, **MAC 주소는 네트워크 인터페이스에 부여된 고유 값이며, 변경되지 않는 고정된 주소**값 이다.

<br>

### 프레임(Frame)
프레임은 **레이어 2에서 사용하는 통신 단위**이며, 다음과 같은 구성 요소를 포함한다.  
<img src="https://github.com/user-attachments/assets/3358223a-05cb-4bd5-8cef-3c79c56b8874" width="90%" height="80%"/>

1. **프리앰블(Preamble)** - 프레임의 시작을 알리는 신호  
2. **대상의 MAC 주소** - 데이터를 수신할 디바이스의 주소  
3. **소스 MAC 주소** - 데이터를 보내는 디바이스의 주소  
4. **이더 타입(Ether Type)** - 상위 계층(레이어 3)의 프로토콜을 명시  
5. **실제 데이터(Payload)** - 전송할 내용  
6. **체크섬(Checksum)** - 오류 검출을 위한 정보  

데이터 링크 레이어에서 만들어진 프레임을 **피지컬 레이어(Physical Layer)**를 통해 비트 스트림(10101010)으로 변환하여 대상 디바이스에 전송하게 된다. 
<img src="https://github.com/user-attachments/assets/fe7929bc-58f0-454e-98c1-9d3fd92f9296" width="90%" height="80%"/>

<br>

### **스위치와 충돌 방지 (CSMA/CD)**
허브의 경우 **데이터 충돌(Collision)**이 발생하면 데이터를 전송할 수 없다. 
레이어 2 디바이스인 스위치는 **충돌을 방지할 수 있는 알고리즘**을 사용하여 이를 해결한다. 
그 알고리즘이 바로 **CSMA/CD(Carrier Sense Multiple Access with Collision Detection)**이다. 

이 기법은 **데이터를 전송하기 전에 먼저 네트워크 상태를 확인하고, 충돌이 발생하면 이를 감지하여 재전송하는 방식**이다. 
이를 통해 허브와 달리 스위치는 효율적인 데이터 전송을 가능하게 한다.

**CSMA/CD** 동작 방식은 다음과 같다.

**1. 캐리어 센스(Carrier Sense)**  
캐리어 센스는 **네트워크 상에서 신호가 존재하는지 확인하는 과정**이다. 두 디바이스가 통신을 하는 경우   
- 왼쪽 디바이스가 데이터를 전송하기 전에, 먼저 **피지컬 레이어에서 신호(캐리어 웨이브)가 있는지 확인**한다.
- 만약 신호가 없다면, 데이터를 전송한다.  
<img src="https://github.com/user-attachments/assets/3f60d395-1309-4904-b7f2-7291eda51071" width="45%" height="80%"/>
<img src="https://github.com/user-attachments/assets/b9764398-317e-40fa-815e-ef16bf4e2e69" width="45%" height="80%"/>

- 다른 쪽 클라이언트에서도 데이터를 전송하기 전 **피지컬 레이어에서 신호(캐리어 웨이브)가 있는지 확인**한 후 신호가 없으면 데이터를 전송한다.  
<img src="https://github.com/user-attachments/assets/e5fbfb7b-8736-4576-bf65-de78409c1737" width="45%" height="80%"/>
<img src="https://github.com/user-attachments/assets/405d0a7f-f231-4a8d-b839-cb1ba7e50d16" width="45%" height="80%"/>

- 만약 누군가 데이터를 보내고 있다면 충돌을 방지하기 위해 대기한다. 

즉, **전송 전 네트워크 상태를 확인하고, 비어 있을 때만 데이터를 보내는 방식**입니다.  

<br>

**2. 충돌 감지(Collision Detection)**  
- 만약 두 개의 장치가 거의 동시에 **네트워크가 비어 있다고 판단하고 데이터를 전송하면**, 충돌이 발생할 수 있다.  
- 이때 **콜리전 디텍션(Collision Detection)** 기능이 작동하여 충돌을 감지한다.
- 충돌이 감지되면, 충돌한 두 디아비스는 **즉시 데이터 전송을 멈춘다.**.  
- 그리고 **잼 신호(Jam Signal)**를 보내 상대 장치에게 **네트워크에 충돌이 발생했음을 알린다.**.  
- 충동을 인지한 디바이스는 일정 시간을 랜덤하게 기다린 후 다시 전송을 시도한다.  
<img src="https://github.com/user-attachments/assets/479e9995-848d-4c1d-8fa2-00fed78d6af7" width="45%" height="80%"/>
<img src="https://github.com/user-attachments/assets/fdcff2a5-04dd-47f3-a253-eee6fd7b661c" width="45%" height="80%"/>

이 방식 덕분에 네트워크에서 **충돌을 최소화하면서도 효율적인 데이터 전송이 가능**하게 된다. 

### 스위치(Switch)
스위치는 **레이어 2(Layer 2) 디바이스**이다. 이는 **데이터 링크 레이어(Data Link Layer)**를 이해하고 작동하는 장치라는 것을 의미한다. 

각 네트워크 인터페이스에는 **MAC 주소**가 부여되어 있으며, 이를 통해 특정 장치 간의 직접적인 통신이 가능하다. 
예를 들어, **A가 B에게 직접 데이터를 전송할 수 있는 것**이다. 스위치가 이를 어떻게 처리하는지 살펴보자. 

스위치 안에는 테이블이 존재한다. 
이 테이블에는 **포트별로 어떤 디바이스가 연결되어 있는지**에 대한 정보가 기록되어 있으며, 또한 프레임을 저장할 수 있는 공간도 포함되어 있다.  
<img src="https://github.com/user-attachments/assets/423f84c4-c1f1-41be-b026-910e3498f451" width="90%" height="80%"/>

이제 예를 들어, **클라이언트 A가 클라이언트 D와 통신을 하려 한다고 가정해 보자.**  
이 경우, A는 프레임을 생성하여 스위치로 전달하게 된다. 
스위치는 이 프레임을 받아서 대상이 D라는 것을 확인한 뒤, D에게만 데이터를 전송한다.  
<img src="https://github.com/user-attachments/assets/d10cdb8c-1f43-48d6-96db-661e0ff88ca0" width="90%" height="80%"/>

여기서 중요한 점은, **스위치는 각각의 통신 구간을 독립적으로 관리**하기 때문에, **B나 C의 상태를 고려할 필요가 없다는 것**이다. 
즉, A와 D가 통신하는 동안 B와 C는 방해받지 않고, 독립적으로 다른 작업을 수행할 수 있다.  

이것이 가능한 이유는 스위치가 포트 별로 독립된 콜리전 도메인을 가지기 때문이다. 
스위치는 연결된 모든 장치의 신호를 고려할 필요 없이, 특정 장치와만 통신할 수 있도록 콜리전 도메인을 분리한다. 

만약 동시에 다른 장치가 데이터를 전송하려 하여 **충돌(Collision)**이 발생하면 어떻게 될까?   
<img src="https://github.com/user-attachments/assets/93217618-e3c9-43bb-b983-927503f525c3" width="90%" height="80%"/>

스위치는 **레이어 2(Layer 2) 장치이므로 CSMA/CD(Carrier Sense Multiple Access with Collision Detection)를 이해하고 적용할 수 있다.**  
즉, 충돌이 감지되면 **잠시 기다렸다가 다시 전송하는 기능**을 수행하게 된다. 

이 과정에서 스위치는 **프레임을 일시적으로 저장했다가, 네트워크가 한가할 때 즉, 충돌이 없는 상태에서 다시 데이터를 전송**한다.  
<img src="https://github.com/user-attachments/assets/13c45d36-6e46-442f-a03c-3198bdef7ce1" width="90%" height="80%"/>

이러한 기능 덕분에 스위치는 허브와 다르게 **데이터 충돌을 최소화하고, 보다 효율적인 통신을 지원**할 수 있다. 


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>






<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
