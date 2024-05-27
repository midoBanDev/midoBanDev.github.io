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

### 
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

