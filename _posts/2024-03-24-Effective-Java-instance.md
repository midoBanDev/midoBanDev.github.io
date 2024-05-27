---
layout: single
title:  "Java 인스턴스 생성"
categories:
  - Effective
tags:
  - [Java, Effective, Instance]

toc: true
toc_sticky: true
---


## 클래스의 인스턴스를 얻다. 또는 클래스의 인스턴스를 생성하다.
>■ Java는 객체지향 언어로서 기본적으로 class 단위로 사용된다.    
>■ 여기서 class는 객체를 생성하기 위한 설계도 라고 이해하면 쉽다.  
>■ class를 메모리에 할당하면 객체, 즉 인스턴스가 생성된다.  
>■ 따라서 클래스의 `인스턴스를 생성하다` 또는 `인스턴스 얻다` 라는 말은 class를 메모리에 할당 한다는 의미이다.  
>■ 인스턴스와 개체를 같은 의미도 해석하는 것이 맞다.

```java
/**
 * 객체를 생성하기 위한 설계도 
 */
public class ExampleClass {
  
}


public class Main {

    public static void main(String[] args) {
        // ExampleClass 클래스를 메모리에 할당하는 방법 : new 연산자를 사용하여 ExampleClass의 기본 생성자를 호출한다.
        new ExampleClass();

        /**
         * 위와 같이 ExampleClass 클래스의 인스턴스(객체)를 생성만 하면 다시 접근할 수 없다.
         * 결국 가비지(사용되지 않는 메모리)가 되어 가비지 컬렉터에 의해 소멸된다. 
         * 그래서 아래와 같이 ExampleClass 타입의 example 변수를 선언하여 ExampleClass 클래스의 인스턴스(객체)를 참조하게 한다.
         */ 
        ExampleClass example = new ExampleClass();
    }

}

```

<br><br>

## 클래스의 인스턴스를 생성하는 방법
#### 1. new 연산자  
```java
Example example = new Example();
```

<br>

#### 2. 리플렉션을 통한 인스턴스 생성  

```java
Class<Example> clazz = Example.class;

// Class.newInstance() 사용
Example example = clazz.newInstance();  

// constructor.newInstance() 사용
Constructor<Example> constructor = clazz.getConstructor();
Example example = constructor.newInstance();

```

<br>

#### 3. Clone을 통한 인스턴스 생성
- Cloneable 인터페이스를 구현한 후 clone() 메서드를 오버라이드 하여 인스턴스를 생성할 수 있다.

```java
public class Example implements Cloneable {
    private String name;
    private int age;

    @Override
    public Example clone() {
        try {
            return (Example) super.clone();;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can never happen
        }
    }
}

public class Main {
  public static void main(String[] args) {
      Example oriExample = new Example();
      Example clonedExample = oriExample.clone(); // Example 객체 복제

      System.out.println(oriExample == clonedExample);  // false, 다른 객체임
      System.out.println(oriExample.getClass() == clonedExample.getClass()); // true, 같은 클래스 타입임
  }
}
```
