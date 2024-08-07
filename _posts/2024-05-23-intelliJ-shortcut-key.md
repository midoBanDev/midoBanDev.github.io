---
layout: single
title:  "인텔리제이 단축키"
categories:
  - IntelliJ
tags:
  - [IntelliJ, IDE, shortcut-key]

toc: true
toc_sticky: true
---


## IntelliJ 단축키

- 설정 창
  -
  ```
  Ctrl + Alt + S
  ```
<br>

- 단축키 변경
  -
  ```
  설정창 오픈(Ctrl + Alt + S) → keymap → Editor Actions 목록에서 찾은 후 변경 가능.
  ```
<br>

- 행 삭제 : 설정에서 단축키 변경 했음.
  -
  ```
  Ctrl + D
  ```
<br>

- 코드 라인 복사 : 설정에서 단축키 변경 했음.
  -
  ```
  Ctrl + Alt + Down
  ```
<br>

- 변수 자동 생성 : Introduce Variable
  -
  ```
  변수 생성할 코드 제일 뒤에 커서 두고 → Ctrl + Shift + Alt + V → Introduce Variable 선택

  또는

  Ctrl + Alt + V
  ```
<br>

- Refactor Option 열기
  -
  `변수합치기`, `변수이름일괄변경`, `선택한 코드 메서드로 뽑아내기` 등 엄청 다양한 기능 제공
   
  ```
  코드 위에 커서 두고 → Ctrl + Shift + Alt + T
  ```
<br>

- 변수 합치기 : Inline Variable 
  -
  ```
  (Alt + Enter) → Inline Variable 선택
  
  또는

  Ctrl + Alt + N
  
  또는 

  합칠 변수에 커서 두고 → Refactor Option 창 오픈(Ctrl + Shift + Alt + T) → Inline Variable 선택
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

- 변수 이름 일괄 변경
  -
  ```
  변경할 변수에 커서 두고 → Ctrl + Shift + Alt + T → Rename 선택

  또는 

  Shift + F6
  ```
<br>

- 메서드 분리
  -
  원하는 코드 구간을 `드래그`로 선택한 후 메서드로 분리할 수 있다. 
   
  ```
  원하는 코드 드래그 → Ctrl + Shift + Alt + T → Extract Method 선택

  또는 

  Ctrl + Alt + M
  ```
<br>

- main 메서드 실행
  -
  main 메서드가 있는 java 파일에서 실행해야 한다.
  ```
  메서드 옆 ▷ 버튼 클릭 후 Run

  또는 

  Ctrl + Shift + F10
  ```

- main 메서드 재 실행
  -
  ```
  Ctrl + F5

  또는 

  하단 메뉴 Run 밑에 새로고침 아이콘 클릭
  ```
  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/4e9fcdaa-1437-41a9-94cd-184738ff7191" width="5%" height="5%"/>

<br>  

- 메서드의 매개변수 타입 확인
  -
  ```
  메서드 매개변수 입력 괄호에 커서 두고 → Ctrl + P
  ```  
  
  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/16490c8f-3ea6-4a2c-98e8-e8b1dd75225b" width="50%" height="50%"/>

<br>  

- Generate 옵션 창
  -
  `생성자`, `setter`, `getter`, `toString` 등 생성할 수 있고, 그외도 다양한 기능 제공( 아래 이미지 참고 )
   
  ```
  Alt + Ins(Insert key)
  ```  
  
  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/6df72526-9447-46d6-b747-d46767b88067" width="18%" height="30%"/>

<br>  

- Override/Implement 할 메서드 선택 창 열기 
  -
  `extends` 또는 `implemnet` 한 객체에 있는 override 가능한 메서드를 보여준다.
   
  ```
  Ctrl + O
  ```  

<br> 

- 메서드, 코드라인 등 위치 이동
  -
  메서드의 시작 행 또는 마지막 행에 커서를 두고 단축키를 실행하면 된다.   
  코드 라인도 위치 변경 가능하다.    
  
  기준  
  : {}로 감싸져 있는 코드의 첫 라인과 끝 라인.   
  : ;로 끝난 코드 라인

   
  ```
  Ctrl + Shift + 방향키 (↓ 또는 ↑)
  ```  

<br> 

- 부모 객체 생성자 생성
  -
  ```
  Alt + Enter → Create constructor matching super 선택  
  ```  
  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/8b816be1-0508-47a1-911b-143536d30fc6" width="80%" height="80%"/>"

<br> 

- 정의된 클래스 또는 메서드로 이동
  -
  ```
  Ctrl + 마우스 좌클릭

  또는

  F5
  ```  

<br> 

<br><br>







<img src="" width="50%" height="50%"/>
<img src="" width="50%" height="50%"/>
<img src="" width="50%" height="50%"/>