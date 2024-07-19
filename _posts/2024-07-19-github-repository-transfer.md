---
layout: single
title:  "Github Repository 이전(Fork Repository 잔디 안보임)"
categories:
  - Github
tags:
  - [Github, Repository]

toc: true
toc_sticky: true
---

## Github의 Repository를 이전
- 나는 아래와 같은 이유로 Github 리포지토리를 이전하려고 한다.
  - 다른 리포지토리를 `Fork`하여 내 리포지토리로 가져온 이후 변경 사항을 push를 해도 `잔디`가 적용되지 않는 상황. ( Ex. `Github blog` 테마를 Fork해 온 경우 등)
  - A 계정의 리포지토리를 B 계정의 리포지토리로 옮겨야 하는 상황
- 리포지토리를 이전하면 경험한 내용도 같이 공유한다.


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## Fork 한 repositoy에 push해도 잔디 안보이는 상황 해결
- 만약 이전해야 할 repository가 Github 블로그라면 repository 이름을 먼저 변경하자.
- `repository` → `Settings` → `General`의 Repository name 에서 변경할 수 있다.

<br>

### 1. local에 fork 해온 내 repository를 bare clone 한다.
- `gitbah`, `vscode` 등을 사용하여 local PC에 clone를 진행한다. 
- 어떤 디렉토리에 받든 상관없다. 



```bash
$ git clone --bare [복사하고자 하는 내 repository 주소]
```

<br>

- bare clone을 진행하면 실제 repository를 내용이 아닌 아래와 같은 파일 및 디렉토리로 구성된다. (당황하지 말자)
- branch 부분에도 `BARE:main` 으로 표기된 걸 볼 수 있다.

<img src="https://github.com/user-attachments/assets/d4d3b934-7bfa-44d6-8961-3201213b9b02" width="80%" height="80%"/>

<br>

### 2. Github에 새로운 repository를 생성한다.
- 만약 Github 블로그를 변경하는 상황이라면 



<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>

