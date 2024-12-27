---
layout: single
title:  "이미지 커밋(Image Commit)"
categories:
  - Docker
tags:
  - [Docker, Image, Image-Commit]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## 이미지를 만드는 방법
이미지를 만드는 방법은 크게 두 가지가 있다.  
`실행 중인 컨테이너를 그 상태 그대로` 이미지로 만들어내는 커밋 방식과 `도커 파일이라는 명세서`를 통해서 이미지를 만드는 빌드 방식이 있다.  
일반적으로 빌드 방식을 사용하지만 빌드 방식이 커밋을 기반으로 동작하기 때문에 커밋 방식도 알아두는 것이 좋다  
<img src="https://github.com/user-attachments/assets/69ec9e6c-41a7-4296-9547-51800ef5eb0d" width="90%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 커밋 방식을 통한 NGINX 웹서버 이미지 생성 
1. `docker run` 명령을 사용해서 NGINX 이미지를 실행하면 NGINX 이미지 위에 읽기 쓰기 레이어인 컨테이너 레이어가 만들어진다.
2. 그리고 컨테이너 내부로 접속 후 NGINX가 기본으로 제공되는 기본 index.html을 수정한다.
3. 수정을 감지한 도커는 `copy-on-write` 전략을 통해 읽기 전용 레이어에 있는 index.html를 카피해서 읽기 쓰기 전용인 컨테이너 레이어로 가져온 다음 수정된 내용을 파일에 라이트 한다.  
4. 이 상태에서 도커의 커밋 기능을 사용해서 컨테이너 레이어까지 포함한 모든 레이어의 상태를 이미지로 저장할 수 있다.  
5. 결과적으로 기존에 있던 레이어들의 맨 위에서 사용되던 컨테이너 레이어까지 포함해서 한 장이 추가된 상태의 새로운 이미지가 만들어진다.
<img src="https://github.com/user-attachments/assets/5b993bdb-2df9-4025-9907-8c38c3f676e7" width="90%" height="80%"/>


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 컨테이너 이미지 생성 실습

### 1. nginx 이미지로 컨테이너 실행
컨테이너 실행과 동시에 컨테이너 쉘 접속 : `docker run -it --name 컨테이너명 이미지명 bin/bash`
>
`-it` 옵션  
`-i`: interactive(상호작용) 모드. 표준 입력(stdin)을 활성화된다.  
`-t`: tty(터미널) 할당. 터미널 세션을 사용할 수 있게 한다.  
`-it`를 같이 사용하면 컨테이너와 상호작용할 수 있는 터미널 환경이 제공된다.  
>
`bin/bash`  
컨테이너가 시작될 때 실행할 명령어다.
bash 셸을 실행하라는 의미로 이 명령어로 인해 컨테이너에 접속해서 리눅스 셸 환경을 사용할 수 있다.

```bash
# 컨테이너 실행하면서 터미널에 바로 접속, # 원래 CMD가 bin/bash로 덮어써짐
$ docker run -it --name officialNginx nginx bin/bash
```
단 위와 같이 실행하는 경우 고려해야 하는 부분이 생긴다.  
위 명령어는 실행하면서 기존 이미지의 `CMD`를 `bin/bash 로 덮어`쓰게 된다.  

그래서 나중에 commit으로 이미지를 만들어 줄 때 CMD를 다시 기존 명령어로 변경해 주지 않으면 nginx가 자동으로 실행하지 않는다.  

그래서 컨테이너 실행을 백그라운드로 한 후 접속하는 방식으로 아래와 같이 진행하면 나중에 commit 시 CMD를 변경해 주지 않아도 된다.

```bash
# 컨테이너 실행
$ docker run -d --name officialNginx nginx

# 컨테이너 쉘 접속: docker exec -it 컨테이너명 bin/bash
$ docker exec -it officialNginx bin/bash
```

<br>

### 2. index.html 수정
```bash
root@39f4ffab90e2: echo my-hollo-nginx > /usr/share/nginx/html/index.html
```

<br>

### 3. 컨테이너 실행 상태로 컨테이너 레이어 commit
커밋 명령어 :`docker commit -m "커밋 메시지" 실행중인컨테이너명 생성할이미지명`
```bash
# CMD 명령어를 기존 명령어로 변경해 주어야 한다. 단, 컨테이너를 백그라운드로 실행 후 컨테이너 쉘에 접속한 방식을 진행하면 CMD 추가 해주지 않아도 된다.
$ docker commit -m "index.html edited by wglee" -c 'CMD ["nginx", "-g", "daemon off;"]' officialNginx midoban/my-nginx
```

<br>


### nginx -g daemon off 명령어 설명

> nginx -g daemon off 명령어는 Nginx를 포그라운드(foreground) 모드로 실행하는 명령어다.

Nginx는 보통 백그라운드로 실행되도록 설계되었다.  
Nginx를 데몬 모드(백그라운드)로 실행하면:  
**1. nginx 마스터 프로세스가 자식 프로세스를 생성한다.**  
**2. 그리고 마스터 프로세스는 백그라운드로 이동하고 종료된다.**  

이때 Docker는 PID 1(마스터 프로세스)이 종료된 것을 감지하고 컨테이너를 종료시켜 버린다.

그래서 `nginx -g daemon off` 명령어로 Docker가 `nginx가 계속 실행 중임을 알 수 있도록` 포그라운드로 실행해야 한다.  
쉽게 말해서 "nginx야, 숨어서 실행하지 말고 계속 앞에서 실행해줘"라고 하는 것과 같다.



<img src="" width="90%" height="80%"/>

<!--<img src="" width="80%" height="80%"/>-->
