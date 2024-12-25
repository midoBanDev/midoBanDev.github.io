---
layout: single
title:  "제네릭이 필요한 이유"
categories:
  - Java
tags:
  - [Java, Generic]

toc: true
toc_sticky: true
---


## 제네릭(Generic)이 필요한 이유
- 제네릭을 사용하면 코드의 재사용성과 효율성을 높일 수 있다.
- 하지만 제네릭을 이해하기가 쉽지 않다. 
- 지금부터 단계별 과정을 거치면서 제네릭을 이해하고 제네릭이 왜 필요한지에 대해서 알아보자.

<br>

## Step 01. 기본 클래스 사용

- ### 숫자를 보관하고 꺼내는 기능을 가진 박스 만들기


    ```java
    /**
     * 숫자를 보관하는 박스
     */
    public class IntegerBox {

        private Integer value;

        public void set(Integer value){
            this.value = value;
        }

        public Integer get(){
            return value;
        }
    }
    ```

<br>

- ### 문자열를 보관하고 꺼내는 기능을 가진 박스 만들기

    ```java
    /**
     * 문자열을 보관하는 박스
     */
    public class StringBox {

        private String value;

        public void set(String value){
            this.value = value;
        }

        public String get(){
            return value;
        }
    }
    ```

<br>

- ### 이제 숫자 박스와 문자열 박스를 사용하여 숫자, 문자를 각각 담고 꺼내보자.
  
    ```java
    public class BoxMain1 {
        public static void main(String[] args) {
            IntegerBox integerBox = new IntegerBox();
            integerBox.set(10);
            Integer integer = integerBox.get();
            System.out.println("integer = " + integer);

            StringBox stringBox = new StringBox();
            stringBox.set("hello");
            String str = stringBox.get();
            System.out.println("str = " + str);
        }
    }
    ```
  - 위 코드를 보면 `숫자 박스`에 숫자 `10`을 넣고 꺼냈다. `문자열 박스`에는 문자열 `hello`를 넣고 꺼냈다.  
  - 우리는 어렵지 않게 우리가 원하는 형태의 값을 넣고 꺼내는 기능을 가진 박스를 만들 수 있다.   
  - 만약 Double, Boolean 등을 담을 박스가 필요하면 위와 같이 어렵지 않게 만들면 된다.  
  - 그런데 만약 담아야 할 형태의 종류가 수십, 수백가지라면 수십, 수백개의 박스를 만들어야만 한다. 생각만 해도 귀찮은 작업이다.  
  - 하나의 박스만 만들어서 재사용할 수 있다면 좋을 거 같다. 어떻게 하면 좋을지 알아보자.

<br><br>

## Step 02. 다형성을 적용한 클래스 사용. (중복 제거)

- Object는 모든 타입의 부모이다.  
- Object를 사용해 다향성을 활용하여 중복 문제를 해결해 보자.

<br>

- ### Object 박스를 만든다.

    ```java
    public class ObjectBox {

        private Object value;

        public void set(Object object){
            this.value = object;
        }
        public Object get(){
            return value;
        }
    }
    ```

<br>

- ### Object 박스를 사용해보자.

    ```java
    public class BoxMain2 {
        public static void main(String[] args) {
            ObjectBox integerBox = new ObjectBox();
            integerBox.set(10);
            Integer integer = (Integer) integerBox.get();
            System.out.println("integer = " + integer);

            ObjectBox stringBox = new ObjectBox();
            stringBox.set("hello");
            String str = (String) stringBox.get();
            System.out.println("str = " + str);
        }
    }
    ```

  - 이전과 다르게 `ObjectBox`를 사용하여 Integer 타입 박스와 String 타입 박스로 사용할 수 있게 되었다.    
  - 이게 가능한 이유는 말했다시피 Object는 모든 타입의 부모이기 때문이다.  
  - 이제는 Integer, String, Double, Boolean 등 어떤 타입이든 상관없이 담고 꺼낼 수 있게 되었다.  
  - 하지만 `Object`이기 때문에 생기는 문제가 존재한다.  

<br>

- ### Object 사용의 한계

    ```java
    public class BoxMain2 {
        public static void main(String[] args) {
            ObjectBox integerBox = new ObjectBox();
            integerBox.set(10);
            integerBox.set("hello");  // 컴파일 에러가 발생하지 않는다.
            Integer integer = (Integer) integerBox.get(); // 캐스팅 오류가 발생한다.
            System.out.println("integer = " + integer);
        }
    }
    ```

  - `ObjectBox`를 사용할 때 `Integer` 타입만 담도록 약속을 하고 박스를 만들었다.  
  - 그런데 누군가 `IntegerBox`에 `String` 타입의 문자열 `hello`를 넣었다.  
  - 하지만 넣을 당시에는 아무런 문제가 발생하지 않는다.  
  - 문제는 박스에서 꺼낼 때 발생한다.   

<br>

- ObjectBox에 담긴 값을 꺼낼 때 아래와 같은 과정을 거치게 된다.
- 우선 integerBox의 반환형이 Object 타입으로 받은 후 Integer 타입으로 캐스팅 해준다.
    ```java
    Object object = integerBox.get();
    Integer num = (Integer) object;
    ``` 

<br>

- 하지만 우리는 보통 위 과정을 축약하여 아래와 같이 다운 캐스팅 처리를 한다.  
    ```java
    Integer integer = (Integer) integerBox.get();
    ```

<br>

- 일반적으로 `IntegerBox`에는 `Integer` 타입의 값만 존재한다고 믿고 개발을 진행한다.  
- 하지만 누군가가 잘못된 타입의 인수를 전달한 경우(IntegerBox에 String 타입의 hello 전달)   
- 값을 꺼낼 때 `String`은 `Integer`로 캐스팅 되지 않는 문제로 인해 캐스팅 오류(ClassCastException)가 발생한다.

<br>

- Object를 사용하여 `다형성`을 활용한 덕분에 코드를 재사용을 할 수 있게 되어 중복 문제를 해결할 수 있었다.  
- 하지만 `Object`는 모든 타입을 받을 수 있다보니 `타입 안정성` 문제가 발생하게 된다.  

<br>

- 그럼 `재사용성`과 `타입안정성`이라는 두 마리 토끼를 다 잡을 수는 없을까?

<br><br>

## Step 03. 제네릭(Generic) 도입

- ### 제네릭을 적용한 GenericBox를 만든다.

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
  - 클래스에 제네릭을 적용하려면 클래스명 오른쪽에 `<>` 기호를 사용한다.  
  - `<>` 기호를 사용한 클래스를 `제네릭 클래스`라 한다.  
  - 그리고 `<>` 기호안에 `T`를 넣어준다. 여기서 `T`를 `타입 매개변수`라 한다.   
  - `타입 매개변수`는 추후에 특정 타입이 적용될 변수이다.   
  - 제네릭 클래스는 타입을 미리 결정하지 않는다. 타입은 제네릭 클래스를 사용하는 시점에 결정된다.  
  - 최종적으로 클래스명 오른쪽에 `<T>`를 넣어 `GenericBox<T>` 이런 형태로 사용된다.  
  - 그리고 클래스 내부에서 타입 매개변수가 필요한 곳에 `T`를 사용해 주면 된다.  

<br>

- ### GenericBox를 사용해 보자.

    ```java
    public class BoxMain3 {
    public static void main(String[] args){
        GenericBox<Integer> integerBox = new GenericBox<Integer>();
        integerBox.set(10);
        Integer integerValue = integerBox.get();
        System.out.println("integerValue = " + integerValue);

        GenericBox<String> stringBox = new GenericBox<String>();
        stringBox.set("hello");
        String stringValue = stringBox.get();
        System.out.println("stringValue = " + stringValue);

        GenericBox<Double> doubleBox = new GenericBox<Double>();
        doubleBox.set(1.5);
        Double doubleValue = doubleBox.get();
        System.out.println("doubleValue = " + doubleValue);
    }
    }
    ```
  - Generic를 적용한 클래스는 위와같이 재사용이 가능하다.  
  - GenericBox 클래스를 생성하는 시점에 `<>` 안에 `new Generic<Integer>();` 이와 같이 타입을 지정해 준다.   
  - 타입을 넣은 제네릭 클래스 생성하면 그때 제네릭 클래스의 타입이 결정된다.

<br>

- ### `new Generic<Integer>()` 로 생성된 GenericBox 클래스는 아래와 같이 적용된다.  

    ```java
    public class GenericBox<Integer> {

        private Integer value;

        public void set(Integer value){
            this.value = value;
        }
        public Integer get(){
            return value;
        }
    }
    ```
  - 타입 매개변수인 `T`가 모두 `Integer`로 변하게 된다.  
  - 그 결과 `set()` 메서드를 통해서는 Integer 타입의 값만 받을 수 있다.  
  - 다른 타입을 인수로 전달하게 되면 컴파일 오류가 발생하게 된다.  
  - `get()` 메서드를 통해서는 Integer 타입의 값을 반환하게 된다.  
  - 그래서 Object일 때는 `Integer`로 캐스팅을 해줘야 했지만 제네릭 클래스는 캐스팅을 해주지 않아도 된다.  

<br>

- 참고로 실제 코드가 위 처럼 `GenericBox<Integer>`로 변하는 것이 아니다.  
- 자바 컴파일러가 우리가 입력한 타입을 기반으로 클래스를 컴파일 하는 시점에 타입 정보를 반영한다.  
- 따라서 다른 타입 인수(인자)를 전달하려는 경우 컴파일 오류가 발생하는 것이다.

<br>

- ### 다른 타입 인수(인자) 전달 시 컴파일 오류 발생

    ```java
    public class BoxMain3 {
    public static void main(String[] args){
        GenericBox<Integer> integerBox = new GenericBox<Integer>();
        integerBox.set(10);
        integerBox.set("hello"); // 컴파일 오류
        Integer integerValue = integerBox.get();
        System.out.println("integerValue = " + integerValue);
    }
    }
    ```
  - `GenericBox`를 `Integer` 타입으로 지정한 경우 `String` 타입 인수(인자)를 전달하면 `컴파일 오류`가 발생한다.  

<br>

  - 컴파일 시점에 오류를 찾아낼 수 있도록 작성된 코드가 좋은 코드의 조건 중 하나이다.  
  - 제네릭을 적용한 결과 재사용성과 타입 안전성 두 가지 모두를 가지게 된 코드가 탄생했다.

