---
layout: single
title:  "Gradle 태스크 실행 단계"
categories:
  - Gradle
tags:
  - [Gradle, Tasks]

toc: true
toc_sticky: true
---

## Gradle의 태스크의 세 가지 단계

### 1. 구성 단계 (Configuration Phase)
- 태스크의 속성 설정
- 의존성 정의
- 입/출력 정의
- 태스크 동작 방식 설정
  
```groovy
tasks.named('someTask').configure {
    // 여기는 구성 단계
    description = "태스크 설명"
    group = "build"
    
    // 입력과 출력 정의
    inputs.file('input.txt')
    outputs.file('output.txt')
    
    // 의존성 정의
    dependsOn otherTask
    
    // from/into 같은 태스크 설정
    from 'src'
    into 'dest'
}
```

### 2. 실행 준비 단계 (Execution Preparation)
- 사전 조건 확인
- 로깅
- 상태 검증
- 환경 준비
  
```groovy
tasks.named('someTask').configure {
    doFirst {
        // 태스크 실행 직전에 수행
        println "태스크 시작"
        // 유효성 검사
        // 상태 확인
        // 로깅
    }
}
```

### 3. 실행 단계 (Execution Phase)
- 실제 태스크 작업 수행
- doLast에서 후처리 작업
- 결과물 처리
- 완료 로깅
  
```groovy
tasks.named('someTask').configure {
    // 실제 태스크 내용 (execution)
    println "태스크 실행 중"
    
    doLast {
        // 태스크 실행 후에 수행
        println "태스크 완료"
        // 결과 처리
        // 파일 복사 등 후처리
    }
}
```

### 실제 예시를 들어 살펴보자.
```groovy
tasks.named('asciidoctor').configure {
    // 1. 구성 단계
    inputs.dir snippetsDir
    dependsOn test
    
    // 2. 실행 준비 단계
    doFirst {
        println "asciidoctor task 시작"
        // 디렉토리 존재 확인 및 삭제
    }
    
    // 3. 실행 단계
    // (asciidoctor 문서 생성 작업)
    
    // 실행 완료 후 처리
    doLast {
        copy {
            from file("build/docs/asciidoc")
            into file("src/main/resources/static/docs")
        }
        println "asciidoctor task 완료"
    }
}
```