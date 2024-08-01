---
layout: single
title:  "애플리케이션 아키텍처 구조 - 안티패턴"
categories:
  - Java
tags:
  - [Java, Architecture]

toc: true
toc_sticky: true
---

<br>

```markdown
# 아키텍처는 사용되는 범주가 굉장히 넓다. 여러 사람들이 다양한 의미로 사용하다 보니 명확한 정의를 내리기가 어려운 부분이 있다.   
# 여기서 아키텍처는 비전과 구조로 구분하겠다.  
# 비전은 어떤 소프트웨어를 만들 것인지에 대한 부분이고, 구조는 소프트웨어의 구조를 말한다.  
# 오늘은 그 중에서 아키텍처 중 구조에 대해 말하고자 한다.  
# 좋은 구조를 가지기 위해 어떤 원칙들을 따르는 것이 좋은지에 대해 알아보자.  
# 추가로 안 좋은 구조의 사례를 통해 앞으로 아키텍처의 구조를 잘 설계하기 위한 포석을 다져보도록 하자.  
```
<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 우리가 지켜야 하는 원칙들

### DRY(Don't Repeat Yourself) 중복 배제 원칙
```markdown
이 원칙은 [Andy Hunt]와 [Dave Thomas]가 쓴 `The Pragmatic Programmer`에서 공식화 되었다.   
책에서 저자는 "모든 지식 조각은 시스템 내에서 하나의 모호하지 않고 권위 있는 표현을 가져야 한다"라고 표현하였다.  
(Every piece of knowledge must have a single, unambiguous, authoritative representation within a system")
```

- 쉽게 말게 애플리케이션 내에서 `동일한 기능을 가진 코드`를 반복하여 작성하지 말아야 한다는 원칙이다.
- 이는 우리가 잘 알고 있는 객체지향의 SOLID 원칙 중 `SRP(Single Responsibillity Principle)`와도 일맥상통하는 말이다.
- 업계에서 아래와 같은 말로도 불린다.
  - Once and Only Once
  - Abstraction Principle


<br><br>


### OCP(Open-Closed Principle) 개방 폐쇄 원칙

- 객체지향의 5대 원칙인 `SOLID` 중 하나로, `확장에는 열려있어야 하고, 변경에는 닫혀 있어야 한다`는 원칙이다.
- `SOLID` 원칙 중 OCP를 제외한 `SLID` 원칙은 OCP의 하부 원칙이라 볼 수 있다. OCP를 지키기 위해 다른 원칙들이 필요하다고 보면 된다.

<br>

- OCP는 쉽게 말해 어떤 기능을 추가하려고 했을 때 다른 곳을 변경해야하는 일이 발생해서는 안된다는 말이다.
- 이는 추상화, 다형성과 관련이 깊다. 
- 객체지향에서 객체는 역할과 책임을 가진다. 객체는 혼자서 할 수 있는 것이 없다. 서로 다른 객체들과의 협력을 해야만 원하는 결과를 얻을 수 있다.
- 객체들과의 협력은 결국 의존성이 생기게되고, 이런 의존성은 결합도를 높여 어떤 객체의 변경이 협력하고 있는 다른 객체들에게 영향을 주게된다.
- 따라서 `OCP`를 지키기 위해서는 `추상화`,`다형성`을 사용하여 `결합도`를 낮추어야 가능해 진다.
- 이렇게 설계된 구조에서 객체들 사이의 추상화된 부분은 변경되어서는 안되고(Closed), 객체는 추가나 변경(Open)이 가능해야 한다.

<img src="https://github.com/user-attachments/assets/99c4ba4e-5859-4d99-9ca5-f2ac64d805d4" width="60%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 다중 계층 아키텍처(Multi-Layered Architecture)
- 오늘날 우리는 다중 계층 아키텍처를 사용한다.
- 추상화 수준에 따라 애플리케이션을 여러 그룹으로 분해하여 애플리케이션의 구조를 체계화 한다.
- 일반적으로 다음과 같이 분류가 가능하다.
  - 표현(Presentation) 계층
  - 비즈니스(Service) 계층
  - 데이터(Data) 계층
- 계층의 이름은 다양한 표현으로 사용된다. 또한 프로젝트에 따라 더 많은 기준으로 분류되기도 한다.
  - 웹 계층
  - 서비스 계층(도메인 계층)
  - 퍼시스턴스 계층


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 안티패턴 1 - 연통 배관
- 연통배관이란 애플리케이션의 각 모듈이 독립적으로 개발되어 다른 모듈과 로직이나 데이터를 공유하지 않고 상호작용을 하지도 않는 것을 말한다.
- 우리는 일반적으로 프로젝트를 개발할 때 화면단위로 작업을 분배한다. `Controller` → `Service` → `Dao` 의 횡적 구조로 개발이 이루어 진다.
- 누군가 나와 비슷한 기능을 추가했는지에 대한 부분은 고려하지 않고, 마치 연통 배관처럼 각자 맡은 부분만을 처리하게 된다. 
  - 이런 구조는 코드의 재사용성이 어렵기 때문에 결국 복붙으로 인한 중복 코드가 많이 발생한다. 
  - 만약 구조를 변경해야 할 경우 모든 코드를 변경해야 한다는 문제에 봉착한다.
- `관심사에 따른 계층 설계`를 통해 이러한 문제점을 해소할 수 있다.

  <img src="https://github.com/user-attachments/assets/07e9e534-49b2-46ad-a004-f118ebf58f78" width="80%" height="80%"/>

<br>

- 관심사 분리 원칙에 따라 계층도 각 계층의 관심사에 따라 설계되어야 한다.
- `Presentation(Controller) 계층`은 화면과 관심사가 같다.
- `Data(Dao) 계층`은 DB의 구성와 관심사가 같다.
- `Service 계층`은 표현계층과 데이터계층의 매개역할로 `기능단위`로 구성이 되어야 한다.

  <img src="https://github.com/user-attachments/assets/325a9228-8399-45f5-b07d-8683c7f92403" width="80%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 안티패턴 2 - 스마트 DAO
- 대부분의 비즈니스 로직을 프로그래밍 언어가 아닌 SQL에 담고 프로그래밍 언어는 이 SQL을 준비하고, 실행하고 결과를 받는 작업을 수행하는 데 사용한다.

<br>

**스마트 DAO의 문제점**
1. 재사용 할 수 없는 1회용 쿼리 사용으로 로직의 중복이 발생하고 유지보수성이 떨어진다. 

    ```sql
    SELECT username, userpassword FROM users WHERE userid = 1;

    -- 해당 쿼리를 생성하면서 위 쿼리가 쓸모없어짐.
    SELECT username, userpasswrd, firstname, birthday, rate, status, createdate FROM users WHERE userid = 1;
    ```

<br>

2. 비즈니스 로직이 SQL로 표현된다. 
   - 애플리케이션은 SQL로 매개변수를 전달하고 쿼리 결과를 가공하는 역할만 담당하게 된다.
   - 실제 아래 쿼리를 보면 조건절에 나오는 것들은 비지니스 로직에서 처리되어야 하는 부분이다.
     - 고려해야 할 것은 성능이슈가 발생할 수 있다. 건수가 많을 경우 비즈니스에서 처리하는 것보다 SQL에서 필터링 후 가져오는 것이 성능이 좋을 수 있다.
    ```sql
    SELECT username, userpassword 
    FROM users 
    WHERE userid = 1 AND firstname = 'JOHN' AND rate > 40 AND birthday > '20210101';

    UPDATE users 
    set rate = rate * 20
    WHERE userid = 1 AND firstname = 'JOHN' AND rate > 40 AND birthday > '20210101';
    ```
     - 소프트웨어는 복잡도를 다루는 여러 기술을 제공한다. 
     - 하지만 SQL은 이런 기능을 제공하지 않는다. 결국 유지보수가 어려워 진다.

<br>

**스마트 DAO의 문제점 해결**
- 웬만하면 SQL로 비즈니스 로직을 처리하지 말자.
- SQL 추상화 기술을 사용하자.
  - ORM(Hibernate, JPA)
  - Active Record
  - Query Builder(JooQ, QueryDSL)
  - Table Data Gateway

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 안티패턴 3 - 뒤범벅 아키텍처
- 관심사에 따른 계층 설계를 했음에도 애플리케이션의 횡적인 설계 요소와 중적인 설계 요소가 혼합되어서 변경이 어려운 아키텍처가 만들어질 수 있다.
  - 중적인 설계 요소 : 사용자의 기능 요청을 구현하는 관심
  - 횡적인 설계 요소 : 여러 기능에 공통으로 적용되어야 하는 관심(log, transaction 등)
- 실제 구현 로직에 횡적인 관심사에 해당되는 로직이 반복적으로 추가되는 문제점이 야기된다.

<br>

**해결법 1 - 파이프 & 필터 패턴(=데코레이터 패턴)**

```markdown
`파이프 & 필터 패턴`은 여러 단위 처리 모듈을 순서대로 나열하고 한 필터에 데이터를 입력해 출력을 얻는다.  
그리고 그 출력을 그 다음 필터의 입력으로 삼도록 구성하는 아키텍처 패턴이다.  
```
- 뒤범벅 아키텍처의 문제는 종적 관심사와 횡적 관심사가 같이 섞여 있는 데에 있다.
- 이를 해결하기 위해 우선 `파이프 & 필터 패턴`을 사용하여 횡적인 관심사를 기존 코드에서 분리하여 모듈화를 한다.
- 이 후 모듈화된 횡적 관심사와 종적 관심사를 조립하여 뒤범벅된 코드의 정리가 가능해 진다.
  - 조립방식에는 애플리케이션 계층을 추가하는 방식과 위임을 하는 방식이 있다.(명확한 방법은 구현을 해봐야 알 수 있을 것 같다)
  
  <img src="https://github.com/user-attachments/assets/be2b8808-98b2-45ba-aae9-1af4db47e188" width="80%" height="80%"/>

<br>


**해결법 2 - 관점 지향 프로그래맹(AOP)**

```markdown
AOP는 Aspect Oriented Programming의 약자로 `관점 지향 프로그래밍`이라고 불린다.  
관점 지향은 쉽게 말해 어떤 로직을 기준으로 `핵심적인 관점`, `부가적인 관점`으로 나누어서 보고  
그 관점을 기준으로 각각 모듈화하겠다는 것이다.  
여기서 `모듈화`란 어떤 `공통된 로직이나 기능`을 하나의 단위로 묶는 것을 말한다. 
```
  <img src="https://github.com/user-attachments/assets/bc43c1f4-d223-4c03-946c-95846783ff8b" width="80%" height="80%"/>

- 스프링을 사용하는 경우라면 AOP를 사용하여 문제를 해결할 수 있다.


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 안티패턴 4 - 긴 공개 매서드
- 클래스에 공개된 메서드만 존재하며, 그 메서드의 크기가 너무 크다. 
- 이는 메서드 단위의 리펙토링의 부재로 인한 것이다.
- 객체 단위에서만 기능 분리가 필요한 것이 아니고, 메서드 단위에서도 기능이 분리되어야 한다.
- 메서드도 더 작은 단위에서 하나의 책임이 존재하도록 분리하면 공개 메서드의 크기는 작아지고, 비공개 메서드의 갯수가 늘어난다.
- `SRP(단일책임원칙)`를 객체 기준으로만 생각하는 경우가 있는데, `메서드 단위`에서도 `SRP`는 적용되어야 한다.

<br>

**해결법 1 - 조합 메서드**

```markdown
조합 메서드란 메서드 내에서 직접 연산을 하지 않고 다른 메서드의 호출로만 조합된 메서드를 말한다.
```
- 이를 통해 얻을 수 있는 이점이 있다.
  1. 메서드 단위의 추상화가 이루어 진다.
     - 여러 `세부사항(연산 코드)들을 공통된 하나의 범주로 묶는 것`을 `추상화`라 한다.
     - 즉, 직접 연산(저수준 작업)하는 부분을 메서드로 묶어 추상화를 한 후 이를 조합 메서드에서 호출(고수준 작업)함으로써 메서드 단위의 추상화를 이룰 수 있다.
  2. 서비스 안에서 메서드 크기가 줄어든다.
  3. 메서드 하나하나가 또 다른 수준의 언어가 된다.

<br>

**해결법 2 - 합수 작성법**

1. 공개 메서드는 이야기(의도, 작업 흐름) 흐름을 나타내라.
2. 비공개 메서드는 이야기의 의미를 정의하라.
3. 작게 만들고 한 가지 일만 해라(SRP)
   - 함수가 한 가지 일만 한다는 말은 꼭 하나의 기능만을 가진다는 의미 보다는 하나의 추상화 수준에서 동작해야 한다는 의미이다.
4. 함수내의 동일한 추상화 수준을 유지하라.
    <details style="margin-top:-10px;">
      <summary> 
      <b><span style="font-size:100%;font-color:brown;">동일한 추상화 수준(feat. 예시 코드)</span></b>
      </summary>

      <div markdown="1" style="padding-top:10px; padding-left:10px;">
      
      ```
      동일한 추상화 수준을 유지란 기능을 정의하는 연산 부분(저수준 추상화)과 메서드의 호출(고수준 추상화)을  
      혼합하여 사용하지 말라는 의미이다.
      ```
      - 동일한 추상화 수준 예시

        ```java
        public class ShoppingCart {
            public double calculateTotalPrice() {
                double subtotal = calculateSubtotal();  // 고수준 작업
                double tax = calculateTax(subtotal);    // 고수준 작업
                return calculateTotal(subtotal, tax);   // 고수준 작업
            }

            private double calculateSubtotal() {
                double subtotal = 0;
                for (Item item : items) {
                    subtotal += item.getPrice() * item.getQuantity(); // 저수준 작업
                }
                return subtotal;
            }

            private double calculateTax(double amount) {
                double taxRate = 0.08;
                return amount * taxRate;  // 저수준 작업
            }

            private double calculateTotal(double subtotal, double tax) {
                return subtotal + tax; // 저수준 작업
            }
        }
        ```

      - 다양한 추상화 수준 혼합

        ```java
        public class ShoppingCart {
            public double calculateTotalPrice() {
                double total = 0;
                for (Item item : items) {
                    total += item.getPrice() * item.getQuantity();  // 저수준 작업
                }
                double taxRate = 0.08;
                total += total * taxRate;  // 저수준 작업
                logToDatabase(total);  // 고수준 작업
                System.out.println("Total price calculated: " + total);  // 고수준 작업
                return total;
            }

            private void logToDatabase(double total) {
                // 데이터베이스 연결 코드 (저수준 작업)
                DatabaseConnection connection = new DatabaseConnection("db_url");
                connection.connect();
                connection.insert("INSERT INTO totals (amount) VALUES (?)", total);
                connection.close();
            }
        }
        ```

      <br>

      **추상화 수준 분류**

      - 높은 추상화 수준 `getHtml()` : 전체적인 흐름이나 주요 단계를 보여준다. 세부 구현은 드러내지 않는다. 주로 다른 메서드를 호출하는 방식으로 동작한다. 
      
        ```java
        // 높은 수준의 추상화를 갖는 메서드. getPagePath() 보다는 getHtml() 메서드의 추상화가 높다.
        public String getHtml() {
            String pagePath = getPagePath();  // 높은 추상화 수준
            String renderedContent = renderPage(pagePath);  // 높은 추상화 수준
            return postProcess(renderedContent);  // 높은 추상화 수준
        }
        ```
      - 중간 추상화 수준 `pageParser.render(pagePath)` : 특정 작업의 주요 단계를 구현한다. 세부 구현을 드러내지 않고 무엇을 하는지 유추가 가능하다.
    
        ```java
        // 중간 수준의 추상화를 갖는 메서드
        public String renderPage(String pagePath) {
            return pageParser.render(pagePath); // 중간 추상화 수준
        }
        ```
      - 낮은 추상화 수준 `builder.append("\n")`: 구체적인 작업을 수행한다. 
    
        ```java
        // 낮은 수준의 추상화를 갖는 메서드
        public void appendNewLine(StringBuilder builder) {
            builder.append("\n"); // 낮은 추상화 수준
        }
        ```
    
      - 예시 추가
      
        ```java
        public class NumberProcessor {
            
            public void processNumbers() {
                List<Integer> numbers = readNumbers();         // 높은 추상화 수준 메서드 호출
                List<Integer> processedNumbers = transformNumbers(numbers); // 높은 추상화 수준 메서드 호출
                writeNumbers(processedNumbers);                // 높은 추상화 수준 메서드 호출
            }

            private List<Integer> readNumbers() {
                return getNumbersFromSource(); // 저수준 추상화 수준 메서드 호출
            }

            private List<Integer> transformNumbers(List<Integer> numbers) {
                List<Integer> transformedNumbers = new ArrayList<>();
                for (int number : numbers) {
                    transformedNumbers.add(square(number)); // 저수준 추상화 수준 메서드 호출
                }
                return transformedNumbers;
            }

            private void writeNumbers(List<Integer> numbers) {
                outputNumbers(numbers); // 저수준 추상화 수준 메서드 호출
            }

            private List<Integer> getNumbersFromSource() {
                // 저수준 추상화
                List<Integer> numbers = new ArrayList<>();
                numbers.add(1);
                numbers.add(2);
                numbers.add(3);
                return numbers;
            }

            private int square(int number) {
                return number * number; // 저수준 추상화
            }

            private void outputNumbers(List<Integer> numbers) {
                // 저수준 추상화
                for (int number : numbers) {
                    System.out.println(number);
                }
            }
        }
        ```
      </div>
    </details>
   
5. 서술적인 이름을 사용하라.
    ```markdown
    이름이 길어도 괜찮다. 겁먹을 필요없다.  
    길고 서술적인 이름이 짧고 어려운 이름보다 좋다.  
    길고 서술적인 이름이 길고 서술적인 주석보다 좋다
    by 로버트 C 마틴
    ```
6. 명령과 조회를 분리하라(CQS, Command Query Separation)
    <details style="margin-top:-10px;">
      <summary> 
      <b><span style="font-size:100%;font-color:brown;">명령과 조회 분리(feat. 예시 코드)</span></b>
      </summary>

      <div markdown="1" style="padding-top:10px; padding-left:10px;">
      
      ```
      명령과 조회의 분리(CQS, Command Query Separation) 원칙은  
      메서드가 상태를 변경하는 명령(Command)과 상태를 반환하는 조회(Query)를 분리하여  
      한 메서드에서 두 가지 역할을 동시에 하지 않도록 합니다
      ```
      - 명령과 조회를 분리하지 않은 경우

        ```java
        public class Account {
            private double balance;

            public double deposit(double amount) {
                balance += amount;
                return balance;
            }
        }
        ```

      - 명령과 조회를 분리

        ```java
        public class Account {
            private double balance;

            // 명령 메서드: 상태를 변경하지만, 값을 반환하지 않습니다.
            public void deposit(double amount) {
                balance += amount;
            }

            // 조회 메서드: 상태를 반환하지만, 상태를 변경하지 않습니다.
            public double getBalance() {
                return balance;
            }
        }
        ```
      </div>
    </details>


7. 오류 코드보다 예외를 던져라.
<!--8. 고차 함수를 사용하여 함수 수준의 추상화가 가능하다.(람다식)-->


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 안티패턴 5 - 하는 놈 따로, 아는 놈 따로
- OOP 언어를 사용하고 클래스를 만들지만 여전히 구조적으로 프로그래밍을 함으로 로직과 데이터가 분리되어 있다.
- DB 의존적 애플리케이션을 만든다.
  - 모든 상태를 DB에만 보관한다.
  - 상태 조작은 SQL로 처리한다.
  - 애플리케이션 코드는 단순히 인지와 SQL 결과를 전달하는 역할만 한다.

<br>

**해결 - 도메인 모델 도입**
- `도메인 모델`이란 행위와 데이터를 포함하는 비즈니스 영역의 도메인 객체를 말한다.
- 도메인 모델을 도입하면 상태를 메모리에서 관리한다.
- 데이터의 저장은 메모리상 상태의 영속화 개념으로 바뀐다.


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 안티패턴 6 - 단일 객체 서비스(Single Class Service)
- 정해진 구조에서 벗어나는 것을 거부한다.
- 이로 인해 서비스 객체 하나로 구성되고, 모든 로직은 서비스 객체에서만 처리된다.
- 서비스 객체의 적절한 분화가 이루어져 있지 않다.

<br>

**문제점**
- 이는 낮은 응집도와 높은 결합도를 만든다.
  - 중복된 코드의 생산이 증가된다.
  - 추상화 수준이 낮아 코드의 변경이 일어난 경우 어디까지 변경에 영향이 있을지 예측하기 어렵다. 
  <img src="https://github.com/user-attachments/assets/1025ac3e-c97f-41e5-99c3-a36e66610a4c" width="80%" height="80%"/>

<br>

**서비스 객체의 용도**
- 객체간의 경계와 연결만을 담당하다. 
- 실제 처리는 각 객체(class)에서 처리되고 서비스 객체는 인터페이스의 연결과 객체간 연결의 역할만 담당하는 것이 좋다. 

<br>

**해결**
1. 높은 응집도와 낮은 결합도를 유지해야 한다.
2. Class 추가 공포증에서 벗어나자.
3. 추상화를 적극적으로 활용하자.

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 최종 정리
1. AOP를 사용하여 종적 관심사와 횡적 관심사를 분리하자.
2. 조합 메서드를 활용하자.
3. 도데인 모델을 도입하자.
4. 큰 서비스를 작은 클래스로 분해하고 위임하자.
5. 높은 응집도와 낮은 결합도를 갖도록 설계하자.


<!--

<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>-->

<!--https://kji6252.github.io/2015/12/16/ksug-anti-pattern/-->


