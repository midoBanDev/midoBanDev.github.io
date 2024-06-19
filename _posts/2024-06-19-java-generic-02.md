---
layout: single
title:  "제네릭 용어"
categories:
  - Java
tags:
  - [Java, Generic]

toc: true
toc_sticky: true
---


## 제네릭(Generic) 생성

#### 제네릭 클랙스

```java
public class GenericBox<T> {

    private T value;

    public void set(T value){
        this.value = value;
    }

    public T get(){
        return value;
    }
}
```

<br>

#### 제네릭 클래스 생성

  
```java
public class BoxMain3 {
  public static void main(String[] args) {
    // 객체 생성 시점에 T의 타입이 결정.
    GenericBox<Integer> genericBox = new GenericBox<Integer>(); 
    
    // 모든 타입 지정 가능.
    GenericBox<String> stringBox = new GenericBox<String>();
    GenericBox<Double> stringBox = new GenericBox<Double>();
    GenericBox<Boolean> stringBox = new GenericBox<Boolean>();
    

    // 원시 타입은 지정할 수 없다. 컴파일 오류 발생
    GenericBox<double> doubleBox = new GenericBox<double>();


    // 타입 추론 : 생성하는 제네릭 타입을 생략할 수 있다.
    GenericBox<Integer> integerGenericBox1 = new GenericBox<Integer>(); // 타입 직접 입력
    GenericBox<Integer> integerGenericBox2 = new GenericBox<>();        // 타입 추론
  }
}
```
● 제네릭 클래스는 `생성 시점에 타입을 지정`한다.   
● 제네릭 클래스는 `어떠한 타입도 지정`할 수 있다. 단, `원시 타입`(int, double, boolean 등)은 지정할 수 없다.  

● 제네릭 클래스는 생성할 때 `타입 추론`이 적용된다.  
● 제네릭 클래스 생성 시 참조 변수를 선언하는 왼쪽 클래스와 인스턴스를 생성하는 클래스 옆에 `<Integer>` 타입을 두 번 선언한다.  
● 이때 자바는 왼쪽에 있는 클래스의 `<Integer>` 타입을 보고 추론하여 오른쪽 인스턴스 객체를 생성할 때 타입을 적용한다.  
● 따라서 인스턴스 객체의 타입은 생략이 가능하다. `new Generic<>();`  
● 이를 `타입 추론`이라 한다.  

<br>




## 제네릭(Generic) 용어

● 제네릭의 핵심은 타입을 미리 정하지 않는다는 것이다.  
● 타입을 제네릭 클래스를 정의하는 시점에 정하는 것이 아니라, 클래스를 생성하는 시점에 정한다.  
● 이는 마치 메서드의 매개변수와 인자와의 관계와 비슷하다.

#### 메서드에 필요한 값을 메서드 정의 시점에 결정.
```java
public static void main(String[] args){
  method();
}

public void method(){
  print("hello");
}
```
● 메서드 정의 시점에 값을 고정했기 때문에 해당 메서드를 호출하면 무조건 `hello` 값만 출력된다.  
● 이로 인해 메서드는 재사용성이 떨어진다.

<br>

#### 메서드에 필요한 값을 메서드 사용 시점에 결정.
```java
public static void main(String[] args){
  String arg = "hello";
  method(arg);
}

public void method(String param){
  print(param);
}
```
● 메서드의 정의 시점이 아닌 메서드의 사용 시점에 매개변수를 통해 인자값을 전달한다.  
● 따라서 다양한 곳에서 호출하여 사용이 가능하다.   
● 메서드가 호출되는 곳에서 필요한 인자값을 전달하면 원하는 결과를 얻을 수 있다.  
● 메서드의 `매개변수(String param)`를 선언하고, 메서드를 사용하는 시점에 원하는 값을 `인자("hello", "hi")`로 전달하면 된다.  
● 이를 통해 코드의 재사용성이 높아진다.  

● 매개변수(Parameter) : `String param`  
● 인자,인수(Argument) : `arg`
