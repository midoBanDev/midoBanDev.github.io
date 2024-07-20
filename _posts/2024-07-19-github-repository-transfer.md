---
layout: single
title:  "Github Repository 이전"
categories:
  - Github
tags:
  - [Github, Repository]

toc: true
toc_sticky: true
---

## Github의 Repository를 이전
- github repository를 이전해야 상황은 여러가지가 있을 수 있다고 본다.
- 나의 경우에는 아래와 같은 이유로 인해 repository 이전을 결정했다.

<br>

상황 : Github 블로그 테마 repository를 fork 해온 이유 잔디가 적용되지 않음.  
원인 : 공식문서를 보면 잔디가 제대로 적용되기 위해서는 아래와 같은 조건이 필요하다.

1. commit한 계정이 Github 계정과 같아야 한다.
2. commit이 fork한 repository가 아니어야 한다.
3. commit이 메인 브랜치에서 일어나야 한다.

해결 : 동일 계정 내에서 repository 이전을 통해 해결



상황 : Github 계정을 두개 사용하고 있었는데 하나로 합치고자 한다. 다행히 A계정이 주 계정이고 B계정은 repository가 하나밖에 없었다.  
해결 : A 계정의 리포지토리를 B 계정의 리포지토리로 이전하여 해결
리포지토리를 이전하면 경험한 내용도 같이 공유한다.


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 동일 계정 내에서 repository를 이전하는 방법
- 만약 이전해야 할 repository가 Github 블로그인 경우 사전 작업
  - 먼저 repository 이름을 변경해 주자.
  - Github 블로그를 사용하기 위해서는 repository 명을 `계정명 + github.io` 로 지정해야한다. 즉, 이름을 임의로 지정할 수 없기 때문에 새로 repository로 Github 블로그를 사용하기 위해 기존 Github 블로그로 사용한 repository의 이름을 변경해주자.
  - `repository` 선택 후 `Settings` → `General`의 Repository name 항목에서 변경할 수 있다.
  - 막 변경해도 된다. 어차피 나중에 삭제할 repository다.

  <img src="https://github.com/user-attachments/assets/ccc6ca65-44b2-418f-8ede-37ac45a416eb" width="80%" height="80%"/>

<br>

### 1. bare clone 진행
- `gitbah`, `vscode의 터미널` 등을 사용하여 local PC에 복사할 repository의 clone를 진행한다. 
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

