---
layout: single
title:  "Gradle 태스크 의존관계 및 설명"
categories:
  - Gradle
tags:
  - [Gradle, Tasks, Dependency]

toc: true
toc_sticky: true
---

## 1. 태스크 의존관계 다이어그램
```
build
  ├── assemble
  │    └── bootJar
  │         ├── classes
  │         │    ├── compileJava
  │         │    └── processResources
  │         └── bootClasspath
  └── check
       └── test
            ├── classes       # main classes
            ├── testClasses   # test classes
            │    ├── compileTestJava
            │    │    └── compileJava
            │    └── processTestResources
            └── processTestResources
```

태스크 실행 순서는 하위 의존관계부터 실행되며, 점진적으로 상위로 올라간다.  

<br>

## 2. 각 태스크 설명

### 2.1 최상위 태스크

```
build
  ├── assemble
  └── check
```

#### build
- 프로젝트의 전체 빌드를 수행하는 라이프사이클 태스크
- `컴파일, 테스트, JAR 생성` 등 모든 빌드 과정을 포함
- **assemble**과 **check** 태스크에 의존


#### assemble
- 프로젝트의 결과물(아티팩트)을 생성하는 라이프사이클 태스크
- 실행 가능한 JAR 파일 생성을 담당
- **bootJar** 태스크에 의존


#### check
- 프로젝트의 검증을 담당하는 라이프사이클 태스크
- `테스트 실행과 품질 검사`를 담당
- **test** 태스크에 의존

<br>

### 2.2 실행 파일 생성 관련 태스크

```
build
  ├── assemble
       └── bootJar
            │
            └── bootClasspath
```

#### bootJar
- Spring Boot 실행 가능한 JAR 파일을 생성
- `모든 의존성을 포함`한 `실행 가능한 JAR` 생성
- 결과물 위치: `build/libs/`
- `classes`와 `bootClasspath` 태스크에 의존
- Spring Boot Loader 클래스들을 포함

#### bootClasspath
- Spring Boot 애플리케이션의 클래스패스 설정
- 필요한 의존성들의 클래스패스를 구성
- Spring Boot 실행에 필요한 라이브러리들의 경로 설정

<br>

### 2.3 메인 소스 컴파일 관련 태스크

```
build
  ├── assemble
       └── bootJar
            ├── classes
                 ├── compileJava
                 └── processResources
```

#### classes
- 컴파일된 클래스들과 리소스들을 취합
- `compileJava`와 `processResources` 태스크에 의존
- 결과물 위치: build/classes/java/main/

#### compileJava
- `src/main/java` 디렉토리의 자바 소스 파일을 컴파일
- 자바 컴파일러 옵션 적용 (자바 버전, 인코딩 등)
- 결과물 위치: `build/classes/java/main/`

#### processResources
- `src/main/resources`의 리소스 파일을 `build/resources/main/` 로 복사
- 파일 필터링 및 프로퍼티 치환 수행
- 결과물 위치: build/resources/main/
- application.properties, application.yml 등 처리

<br>

### 2.4 테스트 관련 태스크

```
build
  │
  └── check
       └── test
            ├── classes       # main classes
            ├── testClasses   # test classes
            │    ├── compileTestJava
            │    │    └── compileJava
            │    └── processTestResources
            └── processTestResources
```

#### test
- 단위 테스트를 실행하는 태스크
- `classes`와 `testClasses` 태스크에 의존
- JUnit/TestNG 등의 테스트 프레임워크 실행
- 테스트 리포트 생성 (build/reports/tests/)
- 테스트 결과 XML 생성 (build/test-results/)

#### testClasses
- 테스트 클래스들과 테스트 리소스들을 취합
- `compileTestJava`와 `processTestResources` 태스크에 의존
- 결과물 위치: build/classes/java/test/

#### compileTestJava
- `src/test/java` 디렉토리의 테스트 소스 파일을 컴파일
- `compileJava` 태스크에 의존 (테스트가 메인 코드 참조)
- 결과물 위치: build/classes/java/test/

#### processTestResources
- `src/test/resources`의 테스트 리소스 파일을 `build/resources/test/`로 복사
- 테스트용 설정 파일 등을 처리
- 결과물 위치: build/resources/test/

## 3. 주요 디렉토리 구조

```
project-root/
├── build/
│   ├── classes/
│   │   ├── java/
│   │   │   ├── main/      # compileJava 결과물
│   │   │   └── test/      # compileTestJava 결과물
│   │   └── resources/
│   │       ├── main/      # processResources 결과물
│   │       └── test/      # processTestResources 결과물
│   ├── libs/              # bootJar 결과물
│   ├── reports/           # 테스트 리포트
│   └── test-results/      # 테스트 실행 결과
└── src/
    ├── main/
    │   ├── java/          # 메인 소스 코드
    │   └── resources/     # 메인 리소스
    └── test/
        ├── java/          # 테스트 소스 코드
        └── resources/     # 테스트 리소스
```