---
layout: single
title:  "객체 생성과 파괴"
categories:
  - Effective
tags:
  - [Java, Effective]

toc: true
toc_sticky: true
---


## 생성자 대신 정적 펙터리 메서드를 고려하라.

- 클라인언트가 인스턴스를 얻는 전통적인 수단은 public 생성자다.

```java
/**
 * 클라이언트용 클래스
 */
public class Main {

    public static void main(String[] args) {
        // ExampleClass 클래스의 인스턴스를 얻는 전통적인 방법
        Example example = new Example();
    }

}

public class Example {
    // 기본 생성자. 아래 처럼 직접 코딩하지 않을 경우 컴파일 시점에 java complier 자동으로 추가해 준다.
    public Example(){};
}

```

- 클래스는 생성자와 별도로 정적 펙토리 메서드를 제공할 수 있다.  
- 정적 펙터리 메서드란 일반적인 객체 생성 방식인 `new` 키워드를 사용하지 않고, 정적 메서드(static method)를 이용하여 객체를 생성 및 반환하는 방식을 말한다.

```java

public class Example {
  
  // 기본 생성자.
  public Example(){}

  // 정적 팩터리 메서드 : 생성자 반환
  public static Example newInstance(){
    return new Example();
  }
}
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 정적 펙터리 메서드 장점

- 이름을 가질 수 있다.
- 호출될 때마다 인스턴스를 새로 생성하지는 않아도 된다.
- 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.
- 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.
- 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 정적 펙터리 메서드 단점