---
layout: single
title:  "build.gradle 태스크 빌드 분석(feat. Spring REST Docs)"
categories:
  - Gradle
tags:
  - [Gradle]

toc: true
toc_sticky: true
---

## 현황 분석
Spring REST Docs을 통해 `index.html`을 만들고, 해당 파일이 `JAR`의 `BOOT-INF`에 삽입되어야 한다.
그래야 최종적으로 http://domain.com/docs/index.html 로 API 문서를 확인할 수 있다.
그런데 JAR 파일 내의 BOOT-INF에 해당 index.html이 생성되지 않는 문제가 발생하였다.
그 원인을 파악하기 위해 우선 기존 `build.gradle` 파일을 분석해보자.

- 전체적인 핵심만 간추려서 작성되었다.  

```groovy
// REST Docs 테스트
tasks.named('test').configure {
    doFirst {
        println "test task 시작"
    }
    outputs.dir snippetsDir // 테스트 실행 후 스패닛 문서들이 생성될 위치 지정
}

tasks.named('asciidoctor').configure {
    doFirst {
        println "asciidoctor task 시작"
    }

    inputs.dir snippetsDir  // asciidoctor는 해당 경로에 있는 스패닛 문서들을 가지고 최종 결과 HTML 문서를 생성한다.
    configurations 'asciidoctorExtensions'  // asciidoctorExtensions 설정을 사용하여 확장 기능을 활성화
    dependsOn test  // test 태스크가 먼저 실행되도록 의존성 설정
    
    // AsciiDoc 문서들을 찾을 때 어떤 파일을 찾을 패턴을 지정하는 설정.
    sources {
        include "**/index.adoc"
    }

    // asciidoctor 작업 이후 생성된 HTML 파일을 static/docs 로 copy
    doLast {
        copy {
            from file("build/docs/asciidoc")    // 해당 경로는 asciidoctor가 실행 후 작업 결과물을 저장하는 기본 위치이다. 
            into file("src/main/resources/static/docs") // 애플리케이션 실행 시 접근할 수 있도록 정적 파일 저장 위치
        }
        println "asciidoctor task 완료"
    }
}

tasks.named('bootJar').configure {
    doFirst {
        println "bootJar task 시작"
    }
    dependsOn asciidoctor // asciidoctor 태스크가 먼저 실행되도록 의존성 설정

    from ("${asciidoctor.outputDir}") {  
        into 'build/resources/main/static/docs' // 빌드 시 jar 파일에 포함시키기 위해 해당 경로에 복사한다.
    }
}
```

위 태스크들은 아래와 같은 의존성을 갖는다.  
build 관련 태스크에 관한 자세한 내용은 **[태스크 의존관계](https://midobandev.github.io/gradle/gradle-tasks-dependency/)** 포스트를 참고하자.

```plaintext
build [13]
  ├── assemble [11]
  │    └── bootJar [10]
  │         ├── asciidoctor [8]
  │         │    └── test [7]
  │         │         ├── classes [3]
  │         │         ├── testClasses [6]
  │         │         │    ├── compileTestJava [4]
  │         │         │    │    └── compileJava [1]
  │         │         │    └── processTestResources [5]
  │         │         └── processTestResources [5]
  │         ├── classes [3]
  │         │    ├── compileJava [1]
  │         │    └── processResources [2]
  │         └── bootClasspath [9]
  └── check [12]
       └── test [7]
```

<br>

위 전체 빌드 태스크에서 가장 핵심은 이래와 같다.

```plaintext
build [13]
  ├── assemble [11]
       └── bootJar [10]
            ├── asciidoctor [8]
            │    └── test [7]
            ├── classes [3]
            │    ├── compileJava [1]
            │    └── processResources [2]
            └── bootClasspath [9]
```

**[태스크 의존관계](https://midobandev.github.io/gradle/gradle-tasks-dependency/)**  
위 포스트에서 관련 태스크 역할을 확인하고 오자.


<br>

## Gradle의 태스크의 세 가지 단계

1. 구성 단계 (Configuration Phase)

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

2. 실행 준비 단계 (Execution Preparation)

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

3. 실행 단계 (Execution Phase)

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

### 주요 포인트:

1. 구성 단계에서는:
- 태스크의 속성 설정
- 의존성 정의
- 입/출력 정의
- 태스크 동작 방식 설정


2. 실행 준비 단계(doFirst)에서는:
- 사전 조건 확인
- 로깅
- 상태 검증
- 환경 준비


3. 실행 단계에서는:
- 실제 태스크 작업 수행
- doLast에서 후처리 작업
- 결과물 처리
- 완료 로깅


<br>

## 문제 원인 분석

`./gradlew build` 실행 시 태스크 의존 관계에 따라 `test -> asciidoctor -> bootJar` 순으로 실행된다.

문제가 된 부분은 `bootJar` 태스크의 `from` 블록이다. 
`from`, `into` 같은 설정 블록은 태스크의 구성 단계에서 실행된다. 

```groovy
tasks.named('bootJar').configure {
    doFirst {
        println "bootJar task 시작"
    }
    dependsOn asciidoctor 

    from ("${asciidoctor.outputDir}") {  
        into 'build/resources/main/static/docs' 
    }
}
```

`gradlew build` 명령을 요청하면 Gradle의 내부 태스크들이 수행되면서 `resources` 디렉터리 아래 파일 및 폴더를 자동으로 `build/resources/main/`로 복사한다.
최종적으로 `bootJar` 태스크의 실행 단계에서 이 파일들이 `BOOT-INF/classes` 디렉터리에 복사된 후 Jar 파일이 생성된다.

이를 토대로 보면 `asciidoctor` 태스크가 실행되어 API 문서를 `resources/static/docs`에 파일을 생성하고 `bootJar` 태스크가 `실행되기 전`까지 `build/resources/main/`에 파일을 복사해 놓으면
`bootJar` 태스크가 실행되면서 해당 파일을 `BOOT-INF/classes`에 복사한다. 

따라서 구성 단계가 아닌 `asciidoctor` 태스크 실행 후 ~ `bootJar` 사이에 `build/resources/main/`에 복사를 해 놓아야 정상적으로 `BOOT-INF/classes` 에 들어간다.

<br>

## 해결 방법

이 문제는 두 가지 방법으로 해결할 수 있다:

1. `asciidoctor` 태스크 실행 후후
- doLast 에서 COPY 작업을 처리한다.
```groovy
tasks.named('asciidoctor').configure {

    // asciidoctor 작업 이후 생성된 HTML 파일을 static/docs 로 copy
    doLast {
        copy {
            from file("build/docs/asciidoc")    // 해당 경로는 asciidoctor가 실행 후 작업 결과물을 저장하는 기본 위치이다. 
            into file("src/main/resources/static/docs") // 애플리케이션 실행 시 접근할 수 있도록 정적 파일 저장 위치
        }

        copy { 
            from file("${asciidoctor.outputDir}")
            into file("build/resources/main/static/docs")
        }
        println "asciidoctor task 완료"
    }
}
```


2. `bootJar` 태스크 실행 전 
- doFirst 에서 COPY 작업을 처리한다.
```groovy
tasks.named('bootJar').configure {
    doFirst {
        println "bootJar task 시작"

        copy { 
            from file("${asciidoctor.outputDir}")
            into file("build/resources/main/static/docs")
        }
    }
    dependsOn asciidoctor // asciidoctor 태스크가 먼저 실행되도록 의존성 설정
}
```

