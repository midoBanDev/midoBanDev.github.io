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

- ### 제네릭 클랙스

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

- ### 제네릭 클래스 생성

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
  - 제네릭 클래스는 `생성 시점에 타입을 지정`한다.   
  - 제네릭 클래스는 `어떠한 타입이든 지정`할 수 있다. 단, `원시 타입`(int, double, boolean 등)은 지정할 수 없다.  

<br>

- ### 타입 추론
  
  ```java
  // 타입 추론 : 생성하는 제네릭 타입을 생략할 수 있다.
  GenericBox<Integer> integerGenericBox1 = new GenericBox<Integer>(); // 타입 직접 입력
  GenericBox<Integer> integerGenericBox2 = new GenericBox<>();        // 타입 추론
  ```
  - 제네릭 클래스 생성 시 참조 변수를 선언하는 왼쪽 클래스와 인스턴스를 생성하는 클래스 옆에 `<Integer>` 타입을 두 번 선언한다.  
  - 이때 자바는 왼쪽에 있는 클래스의 `<Integer>` 타입 정보를 보고 오른쪽 인스턴스 객체를 생성할 때 타입을 적용한다.
  - 즉 자바라 타입 정보를 추론하여 객체 생성 시점에 타입을 적용하는 것을 `타입 추론`이라고 한다.  
  - 그 결과 `new Generic<>();` 이런식으로 인스턴스 객체의 타입은 생략이 가능하다.     

<br><br>


## 제네릭(Generic) 용어

- 제네릭의 핵심은 타입을 미리 정하지 않는다는 것이다.  
- 타입을 제네릭 클래스를 정의하는 시점에 정하는 것이 아니라, 클래스를 생성하는 시점에 정한다.  
- 이는 마치 메서드의 매개변수와 인자(인수)와의 관계와 비슷하다.

<br>

- ### 메서드에 필요한 값을 메서드 정의 시점에 결정.  

  ```java
  public static void main(String[] args){
    method();
  }

  public void method(){
    print("hello");
  }
  ```
  - 메서드 정의 시점에 값을 고정했기 때문에 해당 메서드를 호출하면 무조건 `hello` 값만 출력된다.  
  - 이로 인해 메서드는 재사용성이 떨어진다.

<br>

- ### 메서드에 필요한 값을 메서드 사용 시점에 결정.
  - 매개변수(Parameter) : `String param`  
  - 인자,인수(Argument) : `arg`
  ```java
  public static void main(String[] args){
    String arg = "hello";
    method(arg);
  }

  public void method(String param){
    print(param);
  }
  ```
  - 메서드의 `정의 시점`이 아닌 메서드의 `사용 시점`에 메서드의 사용 값이 결정된다.  
  - 메서드의 `매개변수(String param)`를 선언하고, 메서드를 사용하는 시점에 원하는 값을 `인자("hello", "hi")`로 전달하면 된다.  
  - 따라서 해당 메서드가 필요한 여러 곳에서 필요한 인자값을 전달하면 원하는 결과를 얻을 수 있다.  
  - 이를 통해 코드의 재사용성이 높아진다.  

<br>

- ### 제네릭의 타입 매개변수와 타입 인자(인수)

  - 제네릭 클래스를 정의하는 시점에 클래스 내부에서 사용할 타입을 정하는 것이 아니라, 클래스를 사용하는 시점에 내부에서 사용할 타입을 결정한다.  
  - 이는 메서드의 매개변수와 인자와 비슷하다.  
  - 다른점은 메서드의 경우 메서드에서 사용할 값을 나중으로 미루는 것이고, 제네릭의 경우 사용할 타입 결정을 나중으로 미루는 것이다.
  - 메서드는 `매개변수`에 `인자` 값을 전달해서 사용할 값을 결정한다.
  - 제네릭 클래스는 값이 아닌 타입을 결정하는 것이기 때문에 매개변수와 인자 앞에 `타입`을 붙인다. 
  - 그래서 제네릭 클래스는 `타입 매개변수`에 `타입 인자`를 전달해서 사용할 타입을 결정한다.

  ```java
  public class GenericBox<T> {
    private T value;
  }

  public class Main {
    public static void main(String[] args) {
      GenericBox<Integer> box = new GenericBox<>(); // 타입 추론 사용.
    }
  }
  ```
  - 타입 매개변수 : `GenericBox<T>`의 `T`  
  - 타입 인자(인수) : `GenericBox<Integer>` 의 `Integer`  


- ### 용어 정리

  - 제네릭(Generic)
    - 제네릭의 사전적 의미는 일반적인, 범용적인이라는 뜻이다.
    - 풀어보면 특정 타입에 속한 것이 아니라 일반적으로, 범용적으로 사용할 수 있다는 뜻이다.

  - 제네릭 타입(Generic Type)
    - 클래스나 인터페이스를 정의할 때 타입 매개변수를 사용한 것을 말한다.
    - 제네릭 클랙스, 제네릭 인터페이스를 모두 합쳐 `제네릭 타입`이라고 부른다.  
    - 여기서 타입은 `클래스`,`인터페이스`,`기본형(int 등)`을 모두 합쳐서 부르는 말이다.

  - 타입 매개변수(Type Parameter)
    - 제네릭 타입이나 메서드에서 사용되는 변수로 실제 타입으로 대체된다.

  - 타입 인자(Type Argument)
    - 제네릭 타입을 사용할 때 제공되는 실제 타입이다.

<br>

- ### 제네릭 명명 관례