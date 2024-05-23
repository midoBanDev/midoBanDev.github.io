---
layout: single
title:  "인텔리제이 단축키"
categories:
  - IntelliJ
tags:
  - [IntelliJ, shortcut-key]

toc: true
toc_sticky: true
---


## IntelliJ 단축키

- 행 삭제
   
  ```
  Ctrl + X
  ```
<br>

- 변수 자동 생성 : Introduce Variable
   
  ```
  변수 생성할 코드 제일 뒤에 커서 두고 → Ctrl + Shift + Alt + V → Introduce Variable 선택

  또는

  Ctrl + Alt + V
  ```
<br>

- Refactor Option 열기
   
  ```
  코드 위에 커서 두고 → Ctrl + Shift + Alt + T
  ```
<br>

- 변수 합치기 : Inline Variable 
   
  ```
  합칠 변수에 커서 두고 → Ctrl + Shift + Alt + T → Inline Variable 선택

  또는 

  Ctrl + Alt + N
  ```
  ```java
  ObjectBox integerBox = new ObjectBox();
  integerBox.set(10);

  // 두 라인을 하나로 합치기
  Object object = integerBox.get();
  Integer integer = (Integer) object;

  // 결과
  Integer integer = (Integer) integerBox.get();
  ```
<br>

- 참조된 모든 변수 이름 일괄 변경
   
  ```
  변경할 변수에 커서 두고 → Ctrl + Shift + Alt + T → Rename 선택

  또는 

  Shift + F6
  ```
<br>

- main 메서드 실행
  
  ```
  메서드 옆 ▷ 버튼 클릭 후 Run

  또는 

  Ctrl + Shift + F10
  ```
<br>  

- 메서드의 매개변수 타입 확인
   
  ```
  메서드 매개변수 입력 괄호에 커서 두고 → Ctrl + P
  ```  
  
    <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/16490c8f-3ea6-4a2c-98e8-e8b1dd75225b" width="50%" height="50%"/>

<br>  

<br><br>






<img src="" width="50%" height="50%"/>
<img src="" width="50%" height="50%"/>
<img src="" width="50%" height="50%"/>
<img src="" width="50%" height="50%"/>