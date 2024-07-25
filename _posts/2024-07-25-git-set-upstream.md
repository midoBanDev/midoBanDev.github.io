---
layout: single
title:  "git 원격 브랜치 설정방법(has no upstream branch)"
categories:
  - Git
tags:
  - [Git]

toc: true
toc_sticky: true
---

## 원격 브랜치 업스트림(upstream)

```
업스트림 브랜치(upstream branch)란

git pull 또는 git push 작업을 위해 기본적으로 추적하고 상호작용하는 원격 브랜치를 말한다.  
업스트림 브랜치(upstream branch)를 설정하지 않으면, github나 gitlab에 브랜치가 존재하더라도 Git은 어느 원격 브랜치와 비교해야하는지, 변경 사항을 어디로 푸시해야 하는지 알 수 없다. 
```

<br>

- 업스트림 브랜치가 설정되어 있지 않으면 `git push`를 할 경우 `has no upstream branch` 에러가 발생한다.

  <img src="https://github.com/user-attachments/assets/b19327a1-0cd9-4ace-8be0-fa327a089ae4" width="80%" height="80%"/>


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 원격 브랜치 업스트림(upstream) 확인


- 로컬 branch에 대응하는 원격 브랜치가 업스트림으로 설정되어 있는지 확인
  ```bash
  $ git branch -vv

  # 결과
    develop  8c9d32f 작업 중인 브랜치
  * feature  d1e92a1 [origin/feature] 새로운 기능 추가
    master   5b1d7e6 [origin/master] 메인 브랜치
  ```
  - `feature` 브랜치와 `master` 브랜치는 각각 `origin/feature`와 `origin/master` 브랜치를 추적하고 있다.
  - 하지만 `develop` 브랜치는 업스트림으로 설정된 원격 브랜치가 없다.
  
<br>  

- 업스트림 설정이 되어 있지 않은 경우.

  <img src="https://github.com/user-attachments/assets/feffaae2-d214-4cfe-9e73-f35ba2f7223d" width="80%" height="80%"/>  



<br><br>


## 원격 브랜치 업스트림(upstream) 설정

<br>

**설정만 하는 방법**
```bash
$ git branch --set-upstream-to=origin/develop develop
```
<img src="https://github.com/user-attachments/assets/69971c31-87b4-435f-871d-e0a118b86069" width="80%" height="80%"/>

- 설정 완료 후 `git push` 명령어만 실행해주면 된다.

<br><br>

**설정하면서 push하는 방법**
```bash
git push --set-upstream origin develop
```






<!--<img src="" width="80%" height="80%"/>-->



