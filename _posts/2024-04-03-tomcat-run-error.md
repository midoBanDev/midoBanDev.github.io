---
layout: single
title:  "Tomcat과 JDK의 버전 호환성 문제"
categories:
  - Java
tags:
  - [Tomcat, Jdk]

toc: true
toc_sticky: true
---

## Tomcat과 JDK의 버전 호환성 문제로 인해 실행 에러 java.lang.reflect.InvocationTargetException

### 기존 환경
> OS  : CentOS release 6.9 (Final)  
> JDK : OpenJDK1.7.0_221
> WAS : Jeus

<br>

### 목표
>`-` 서버에 구동중인 애플리케이션을 임시로 하나 더 구축한다.  
>`-` WAS는 Jeus 대신 Tomcat을 사용한다.  
>`-` Java는 OpenJDK 대신 Oracle JDK1.8 버전을 사용한다.  
>`-` 위 과정에서 발생한 오류 및 해결 과정을 기록한다.  

<br>

#### 프로젝트 Deploy 전 기본 Tomcat 설정하여 인터넷으로 접근 되는지 확인(고양이)

### 1차 시도 : 실패
> OS  : CentOS release 6.9 (Final)  
> JDK : JDK1.8.0_361  
> WAS : Apache Tomcat 8.5.97

### 오류 발생
```shell
java.lang.reflect.InvocationTargetException
```

<br>

### 2차 시도 : 실패
> OS  : CentOS release 6.9 (Final)  
> JDK : JDK1.8.0_361  
> WAS : Apache Tomcat 8.5.100

### 같은 오류 발생
```shell
java.lang.reflect.InvocationTargetException
```

<br>

### 3차 시도 : 성공 ( 최종 환경 )
> OS  : CentOS release 6.9 (Final)  
> JDK : JDK1.8.0_261  
> WAS : Apache Tomcat 9.0.68

- Tomcat의 `catalina.sh`에 JAVA_HOME 및 JRE_HOME 설정  
    ```shell
    # catalina.sh 내용 편집기 열기
    $ vim apache-tomcat/bin/catalina.sh

    ## catalina.sh 상단에 아래 내용 입력
    JAVA_HOME=/usr/local/java/jdk1.8_361
    JRE_HOME=/usr/local/java/jdk1.8_361/jre
    ```

<br>

### 결론
- 결국 Tomcat 버전과 JDK 버전의 호환성 문제로 발생한 오류였다. 
- 마이너 버전까지 맞춰야 에러가 사라진다. 
- 경험상 오류를 해결하려고 하는 것 보다 맞는 버전을 사용하는데 현명하다.
- 따라서 안정적인 환경을 기록해 두어야 한다.
- 단, 해당 오류가 버전 문제인지는 아닌지 판단할 수 있어야 한다.
  


