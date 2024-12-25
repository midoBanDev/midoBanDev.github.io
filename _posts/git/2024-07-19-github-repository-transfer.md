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

## Github의 Repository 이전
- github repository를 이전해야 상황은 여러가지가 있을 수 있다고 본다.
- 나의 경우에는 아래와 같은 이유로 인해 repository 이전을 결정했다.

<br>

### Case 1
- Github 블로그 테마 repository를 fork 해온 이유 잔디가 적용되지 않음.  

원인 
- 공식문서를 보면 잔디가 제대로 적용되기 위해서는 아래와 같은 조건이 필요하다.
```
1. commit한 계정이 Github 계정과 같아야 한다.
2. commit이 fork한 repository가 아니어야 한다.
3. commit이 메인 브랜치에서 일어나야 한다.
```

해결 
- 난 2번 사항에 해당 되었고, 그래서 repository 이전을 통해 해결하고자 한다.

----

### Case 2
- Github 계정을 두 개 사용하고 있었는데 하나로 합치고 싶어졌다. 
- 다행히 A계정이 주 계정이고 B계정은 repository가 하나밖에 없었다.  

해결
- B 계정의 리포지토리를 A 계정의 리포지토리로 이전하여 해결하고자 한다.  

----

결론
- 결국 디테일한 상황만 다를 뿐 큰 틀에서 보면 `같은 계정 내에서의 repository 이전`과 `다른 계정 간의 repository 이전` 으로 나눌 수 있다.  
- 아래 해결 방법은 이 두 가지의 기준으로 작성되었다.


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 동일 계정 내에서 repository를 이전하는 방법
- 만약 이전해야 할 repository가 `Github 블로그`인 경우 아래와 같은 사전 작업을 해주면 좋다.
  - repository 이름을 변경해 주자.
  - Github 블로그를 사용하기 위해서는 repository 명을 `계정명 + github.io` 로 지정해야한다. 
  - 즉, 이름을 임의로 지정할 수 없기 때문에 새로운 repository로 Github 블로그를 사용하기 위해 기존 Github 블로그의 repository 이름을 변경해주자.
    - `repository` 선택 후 `Settings` → `General`의 Repository name 항목에서 변경할 수 있다.
  - 막 변경해도 된다. 어차피 나중에 삭제할 repository다.

  <img src="https://github.com/user-attachments/assets/ccc6ca65-44b2-418f-8ede-37ac45a416eb" width="80%" height="80%"/>

<br>

### 1. bare clone 진행 : Local PC에서 진행
- `gitbah`, `vscode의 터미널` 등을 사용하여 local PC에 복사할 repository의 clone를 진행한다. 
- 어떤 디렉토리에 받든 상관없다. 

  ```bash
  $ git clone --bare [복사하고자 하는 내 repository 주소]
  ```

  <img src="https://github.com/user-attachments/assets/ff8e542e-4fc2-4b6b-b2be-dc10fa9df299" width="80%" height="80%"/>

<br>

- bare clone을 진행하면 실제 repository를 내용이 아닌 아래와 같은 파일 및 디렉토리로 구성된다. (당황하지 말자)
- branch 부분에도 `BARE:main` 으로 표기된 걸 볼 수 있다.

  <img src="https://github.com/user-attachments/assets/d4d3b934-7bfa-44d6-8961-3201213b9b02" width="80%" height="80%"/>

<br>

### 2. 새로운 repository를 생성 : Github 사이트에서 진행
- 만약 Github 블로그의 repository로 사용할 예정이면 repository 명을 `계정명+github.io` 로 입력하자.

<br>

### 3. push mirror 진행 : Local PC에서 진행

- 2번에서 생성한 repository 주소를 복사해놓자.
- 1번에서 `bare clone`을 진행한 디렉토리로 이동한다.
- 아래 명령어 실행하자.
  - mirror 옵션을 통해 bare clone한 소스를 새로 생성한 repository로 push한다.

  ```bash
  $ git push --mirror [새로 생성한 repository 주소]
  ```

<br>

#### ※ permission 오류가 발생한 경우 
```
이 오류는 A 계정에서 B 계정으로 repository를 이전할 때도 발생할 수 있다. 동일하게 조치해 주자.
```

<img src="https://github.com/user-attachments/assets/f5bd0fad-03f1-4641-9139-6532ed3da67b" width="80%" height="80%"/>

- github 계정을 여러개 사용하는 경우 발생할 수 있는 상황이다. 
- 우리는 github의 repository로 push를 할 때마다 계정정보(로그인정보)를 입력하지 않는다.
- 그 이유는 window에서 자격증명을 등록해 놓기 때문이다. 
- 그래서 내가 push 하려는 계정이 `hello` 인데, 자격증명에 등록된 계정이 `Hi` 인 경우 permission 오류가 발생하게 된다.
- 기존에 등록되어 있는 정보를 보면 내가 push 하려는 계정정보와 다른 것을 확인할 수 있다. 
- 자 그럼 등록된 자격증명을 제거해 주자. 
  - `제어판` → `사용자계정` → `자격증명관리자`를 보면 `Window 자격 증명 관리` 정보를 확인할 수 있다.

  <img src="https://github.com/user-attachments/assets/5ced7f71-606a-4923-a52e-d1656f09664e" width="80%" height="80%"/>

<br>

- 자격증명을 제거해 준 후 다시 push 명령을 진행해 주자.

  ```bash
  $ git push --mirror [새로 생성한 repository 주소]
  ```
- 그러면 인증을 위해 계정정보를 입력하라는 창이 뜬다. 브라우져를 통해 로그인을 진행해 주면 된다. 

  <img src="https://github.com/user-attachments/assets/2bef4b99-fb27-4047-8900-67b80826502d" width="80%" height="80%"/>

<br>

- 또는 미리 자격증명을 등록해 주는 방법도 있다. 아래와 같이 등록이 가능하다.

  <img src="https://github.com/user-attachments/assets/74508ae3-a565-407f-905a-62feb602cec3" width="80%" height="80%"/>  

<br>

- 이제 정상적으로 진행되는 것을 확인할 수 있다.

  <img src="https://github.com/user-attachments/assets/321bcd5a-a699-41aa-a13f-6344619f3fe6" width="80%" height="80%"/>



- 이제 Github으로 가서 확인해 보자.

<img src="https://github.com/user-attachments/assets/a8ab5330-1d27-4ce2-8bde-0519bdffb535" width="80%" height="80%"/>


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 다른 계정 간의 repository를 이전하는 방법

- 방법은 계정내에서 repository를 이전하는 방법과 완전히 동일하다. 
- 위 방법을 그대로 진행하면서 `새로운 repository 생성`을 내가 옮기려고 하는 Github 계정에서 생성해 주자. 
- 이전이 완료 되었으면 Local에 이전한 repository를 받은 후 다음 내용을 진행하자.

<br>

```
만약 복사한 repository의 잔디 정보가 필요없는 경우 추가로 진행해줘야 할 건 없다.  
여기까지가 끝이다.
```

<br>

- 자 그럼 repository 이전을 완료 했다는 가정하에 이후 내용을 진행하겠다.
- 계정간의 repository 이전에서는 커밋 히스토리 정보를 변경해 주는 과정이 필요하다.
- 커밋 히스토리에 등록된 사용자명(author)과 계정정보(email)을 이전 계정정보에서 이전한 계정정보로 변경해 줘야 잔디가 제대로 보이게 된다.
  
- git log를 통해 커밋 히스토리를 살펴보면 이전 계정정보로 커밋된 히스토리를 확인할 수 있다.

  ```bash
  $ git log
  ```
<br>

- 커밋 히스토리 변경은 생각보다 간단하다 다만 히스토리가 많은 경우 시간이 올래 걸리기 때문에 시간적 여유가 있는 상황에서 진행하자.
- 아래 명령어를 실행해 주면 된다. 필요한 건 `계정명`과 `계정메일`만 있으면 된다.

  ```bash
  $ git filter-branch -f --env-filter "GIT_AUTHOR_NAME='변경할 계정명'; GIT_AUTHOR_EMAIL='변경할 계정메일'; GIT_COMMITTER_NAME='변경할 계정명'; GIT_COMMITTER_EMAIL='변경할 계정메일';" HEAD
  ```
  <img src="https://github.com/user-attachments/assets/92f62c26-fbaa-4146-bd01-c2005a79e73c" width="80%" height="80%"/>

<br>

- local 커밋 히스토리는 변경 되었다. 이제 push 해주자
- 'refs/heads/main'의 `main` 부분은 자신의 branch에 맞게 수정해 줘야 한다.

  ```bash
  $ git push --force --tags origin 'refs/heads/main'
  ```

  <img src="https://github.com/user-attachments/assets/8e0fc2b1-7914-49db-b27d-688d0c45352b" width="80%" height="80%"/>




<!--<img src="" width="80%" height="80%"/>-->

