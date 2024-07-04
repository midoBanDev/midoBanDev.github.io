---
layout: single
title:  "인텔리제이 가이드"
categories:
  - IntelliJ
tags:
  - [IntelliJ, IDE]

toc: true
toc_sticky: true
---


## IntelliJ 다운로드
- 다운로드 링크 : [링크](https://www.jetbrains.com/ko-kr/idea/download/?section=windows)
- 위 링크 접속 후 아래로 스크롤을 내리면 `IntelliJ IDEA Community Edition` 을 다운로드 할 수 있다.
  
<br>  

<img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/3c7f9746-c7f6-4162-af94-a860970da7c3" width="80%" height="80%"/>

<br><br>

## IntelliJ 설치
- 다운받은 exe 파일을 실행한다.
- 설치 화면에서 특이사항 없다. 기본 설정 그대로 `next` 누르고 진행하면 된다.

<br><br>

## IntelliJ 패키지 tree 구조로 보이게 하기

- 왼쪽 메뉴 우측 상단에 `세점` 클릭.  
  
<img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/4f79a1f4-3302-43fe-bb88-9d50a69df43d" width="80%" height="80%"/>

<br>

- Tree Appearance > Compact Middle packages 체크를 풀어준다. 
  
<img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/74c37191-b8b9-406b-acbe-ba47ac792bec" width="80%" height="80%"/>

<br>

- 아래와 같이 중간 패키지도 Tree 구조로 보임. 
  
<img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/6c244bbf-bae7-4b63-8d23-14d616665257" width="80%" height="80%"/>



## Lombok 사용하기

- build.gradle 추가
```xml
dependencies {
    ... 생략

    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'com.h2database:h2'
    annotationProcessor 'org.projectlombok:lombok'

    //test lombok 사용
    testCompileOnly 'org.projectlombok:lombok'
    testAnnotationProcessor 'org.projectlombok:lombok'
}
```

<br>

- IntelliJ 설정 
  - [File] → [Settings] → [Plugs] 이동 후 `Lombok` 검색
  - 난 이미 설치 했기 때문에 체크 표시가 보이지만 최초에는 `Install` 버튼이 나온다. 
  - `Install` 클릭.

  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/db1fa299-a58f-4051-be63-ffe949e2ea9f" width="80%" height="80%"/>


<br>

## IntelliJ Gradle 대신 자바로 직접 실행하게 하기
- IntelliJ는 Gradle을 통해 실행하는 것이 기본 설정이다.
- 이렇게 되면 실행속도가 느리다.
- 또한 아래와 같은 경고 메세지가 출력된다.
  `Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.`

<br>

- 자바에서 바로 실행하도록 변경하자.
- [File] → [Settings] → [Build, Excute, Development] → [Build Tools] → [Gradle] 로 이동 후
- Build and run using: Gradle -> `IntelliJ IDEA` 로 변경 해주자.
- Run tests using: Gradle -> `IntelliJ IDEA` 로 변경 해주자.

<img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/5e03bc2d-1905-4b06-9d95-5ff4207e5494" width="80%" height="80%"/>

<!-- 
<img src="" width="50%" height="50%"/>
<img src="" width="50%" height="50%"/>
<img src="" width="50%" height="50%"/>
-->