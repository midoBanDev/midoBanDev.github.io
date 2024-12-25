---
layout: single
title:  "이펙티브 자바를 시작합니다"
categories:
  - Effective
tags:
  - [Java, Effective]

toc: true
toc_sticky: true
---


## Effective Java 씹어먹기

### Effective 책이 어려운 이유
- 용어가 너무 어렵다.
- 많은 사전 지식이 필요하다. 하지만 알고 있다 하더라도 글로 이해하는 건 다른 문제다.
- 예시를 들거나 제시한 내용에 대한 예제 코드가 생각보다 너무 없다. 

그래서 내가 한번 쉽게 만들어 보자란 생각으로 이 엄청난 도전을 시작하려고 한다.
예상컨데 Effective Java 보다 더 많은 내용이 담길거라 생각한다.
그리고 내가 개발자 10년차인데(아무리 내 실력이 많이 떨어진다 하더라도) 책 내용을 이해하지 못하는 건 책의 문제가 맞다.
(지극히 주관적인 생각이고 이 생각 때문에 이걸 시작하려고 결심했다)


<br>

## 내용 가이드

작성 방식은 아래 형식으로 작성된다.

### 1. 생성자 대신 정적 팩터리 메서드를 고려하라
> 주저리주저리주저리

### 예시 코드

```java
public class Test{
    public void listAdd(){
    
        String code = null;
        if(code.equals("123")){
            return;
        }
    }
}
```
