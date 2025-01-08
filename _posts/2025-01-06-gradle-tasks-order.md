---
layout: single
title:  "Gradle 태스크 실행 순서"
categories:
  - Gradle
tags:
  - [Gradle, Tasks]

toc: true
toc_sticky: true
---

## 1. 전체 실행 순서 (./gradlew build 실행 시)

```
[1] compileJava
    - src/main/java 컴파일
    
[2] processResources (1과 병렬 실행 가능)
    - src/main/resources 처리
    
[3] classes
    - compileJava와 processResources 결과물 취합
    
[4] compileTestJava
    - src/test/java 컴파일
    - compileJava 완료 후 실행 (의존관계)
    
[5] processTestResources (4와 병렬 실행 가능)
    - src/test/resources 처리
    
[6] testClasses
    - compileTestJava와 processTestResources 결과물 취합
    
[7] test
    - 단위 테스트 실행
    - classes와 testClasses 완료 후 실행
    
[8] bootClasspath (7과 병렬 실행 가능)
    - Spring Boot 클래스패스 구성
    
[9] bootJar
    - 실행 가능한 JAR 파일 생성
    - classes와 bootClasspath 완료 후 실행
    
[10] assemble
     - bootJar 완료 후 실행
    
[11] check
     - test 완료 후 실행
    
[12] build
     - assemble과 check 완료 후 실행
```

## 2. 의존관계 다이어그램 (실행 순서 포함)
```
build [12]
  ├── assemble [10]
  │    └── bootJar [9]
  │         ├── classes [3]
  │         │    ├── compileJava [1]
  │         │    └── processResources [2]
  │         └── bootClasspath [8]
  └── check [11]
       └── test [7]
            ├── classes [3]
            ├── testClasses [6]
            │    ├── compileTestJava [4]
            │    │    └── compileJava [1]
            │    └── processTestResources [5]
            └── processTestResources [5]
```

## 3. 병렬 실행 가능한 태스크 그룹

### 그룹 1 (초기 실행)
- compileJava [1]
- processResources [2]

### 그룹 2 (테스트 준비)
- compileTestJava [4]
- processTestResources [5]

### 그룹 3 (아티팩트 생성 준비)
- test [7]
- bootClasspath [8]

## 4. 순차적 실행이 필요한 태스크
1. `classes`는 compileJava와 processResources 완료 후
2. `testClasses`는 compileTestJava와 processTestResources 완료 후
3. `test`는 classes와 testClasses 완료 후
4. `bootJar`는 classes와 bootClasspath 완료 후
5. `assemble`은 bootJar 완료 후
6. `check`는 test 완료 후
7. `build`는 assemble과 check 완료 후

## 5. 주의사항
- 실제 실행 순서는 Gradle의 빌드 최적화에 따라 약간 달라질 수 있음
- 병렬 실행 설정에 따라 동시 실행되는 태스크가 달라질 수 있음
- 각 태스크의 `doFirst`와 `doLast` 블록은 해당 태스크 실행 시점에 순차적으로 실행됨
- 증분 빌드(incremental build) 시에는 변경된 부분만 실행될 수 있음

## 유용한 Gradle 명령어

```bash
# 전체 빌드
$ ./gradlew build

# 테스트 없이 빌드
$ ./gradlew build -x test

# 특정 태스크의 의존관계 확인
$ ./gradlew taskName --dry-run

# 캐시 삭제 및 새로운 빌드
$ ./gradlew clean build

# Spring Boot JAR 생성
$ ./gradlew bootJar

# 테스트만 실행
$ ./gradlew test
```