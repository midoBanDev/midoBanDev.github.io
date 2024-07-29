---
layout: single
title:  "객체지향 의존성을 관리하라"
categories:
  - Java
tags:
  - [Java, Denpendency]

toc: true
toc_sticky: true
---


## 의존성을 이용한 설계

- 객체지향 설계의 핵심은 `의존성`이다. 의존성을 어떻게 관리하는지에 따라 설계는 굉장히 많이 바뀐다.
- 어떻게 의존성을 관리해야 할까? 우선 의존성에 대해 알아보자.

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 의존성의 개념

- 설계란 코드를 어떻게 배치할 것인가에 대한 의사결정이다.
- 어떤 클래스에 어떤 코드를 넣을지, 어떤 패키지에 어떤 코드를 넣을지, 어떤 프로젝트에 어떤 코드를 넣을지에 따라 설계의 모양이 달라진다.
- 그럼 어디에 어떤 코드를 넣어야 할까? 핵심은 `변경`에 초점을 맞추면 된다. 
  - `같이 변경되는 코드`를 같이 넣어야 된다.
  - `같이 변경되지 않는 코드`를 따로 넣어야 한다.

- A가 B에 의존할 경우, A에서 B로 향하는 점선으로 표현한다.
- 이 의미는 B가 변경 될 때 A도 변경될 수 있다는 것이다. 즉, 의존성(Dependency)란 변경에 의해서 영향을 받을 수 있는 가능성을 말한다. 
  - 물론 설계를 잘하면 의존성이 있다고 무조건 변경이 일어나지는 않는다. 

  <img src="https://github.com/user-attachments/assets/42aa06bf-7afc-490b-a398-0a2fcdcee7f4" width="80%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 의존성이 종류

- 클래스 사이의 의존성
- 패키지 사이의 의존성

<br>

### 클래스 의존성의 종류

<img src="https://github.com/user-attachments/assets/57dd1898-ed2f-44c1-a847-59b89193c73b" width="80%" height="80%"/>

<br>

- 연관관계(Association) : A 클래스에 B클래스로 이동할 수 있도록 객체참조를 하는 경우 (영구적 관계)

  ```java
  class A {
    private B b;
  }
  ```

<br>

- 의존관계(Dependency) : A 클래스 메서드의 파라미터에 B 타입이 나오거나, 리턴타입에 B 타입이 나오거나, 메서드 안에서 B 타입의 인스턴스를 생성하는 경우 (일시적 관계)

  ```java
  class A {
    public B method(B b){
      return new B();
    }
  }
  ```

<br>

- 상속관계(Inheritance) : A가 B를 상속하는 경우
    - B클래스의 구현이 변경된 경우에도 A가 영향을 받는다.

  ```java
  class A extends B {}
  ```

<br>

- 실체화 관계(Realization) : A가 B 인터페이스를 구현하는 경우
    - B 인터페이스의 `오퍼레이션 시그니쳐`가 변경된 경우에만 영향을 받는다.

  ```java
  class A implements B {}
  ```

<br>

### 패키지 의존성

- `패키지A`가 `패키지B`에 의존한다는 말은 패키지 B에 있는 클래스의 변경이 일어난 경우 패키지 A에 있는 클래스에 영향을 주는 경우를 말한다.

- 다시 말해, 어떤 패키지 안에 있는 클래스가 다른 패키지 안에 있는 클래스와 `연관관계`, `의존관계`, `상속관계`, `실체화관계` 등의 관계가 맺어져 있으면 `두 패키지는 의존성이 있다`고 보면 된다.  

- 좀 더 쉽게 말하자면 A 패키지 안에 있는 어떤 `클래스의 상단`에 `B 패키지의 클래스가 import` 되어 있는 경우 A 패키지는 B 패키지에 의존한다고 볼 수 있다.

  <img src="https://github.com/user-attachments/assets/d0775626-9d34-45b5-b54f-3c93c1797ba6" width="80%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 좋은 의존성을 관리하기 위한 규칙

### 1. 양방향 의존성을 피하라.

- 양방향 연관관계의 경우 항상 동기화를 시켜줘야 한다.
- 이 외에도 신경써야 할 것들이 많아진다.
    
  ```java
  class A {
    private B b;
    
    public void setB(B b){
      this.b = b;
      this.b.setA(this); // 동기화
    }
  }

  class B {
    private A a;
    
    public void setA(A a){
      this.a = a;
    }
  }
  ```

- 단방향 연관관계로 변경해 주자.

  ```java
  class A {
    private B b;
    
    public void setB(B b){
      this.b = b
    }
  }

  class B {
  }
  ```

<br>

### 2. 다중성이 적은 방향을 선택하라

- 일대다의 관계
- 성능이슈 및 관계를 유지하기 위한 다양한 이슈가 발생할 수 있다.

  ```java
  class A {
    private Collection<B> bs;
  }

  class B {}
  ```

- 다대일의 관계로 변경해 주자.

  ```java
  class A{
  }

  class B {
    private A a;
  }
  ```

<br>

### 3. 의존성이 필요 없다면 제거하라.
- 의존성 관계는 피할 수 있으면 피하자.

  ```java
  class A {
    private B b;
  }
  class B {}
  ```

  ```java
  class A {}
  
  class B {}
  ```

<br>

### 4. 패키지 사이의 의존성 사이클을 제거하라.

- A,B,C 패키지 세 개 있을 때 패키지 사이의 의존성을 따라갔을 때 A→B→C→A로의 의존성이 있으면 안된다.
- 이 말은 세 패키지는 하나의 패키지와 같다는 말이 된다.

<br>

### 다시 말해 설계의 핵심은 코드를 배치하는 데 있어서 변경에 초점을 맞추면 된다.

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 출처

https://www.slideshare.net/baejjae93/ss-151545329


<!--<img src="" width="80%" height="80%"/>-->



