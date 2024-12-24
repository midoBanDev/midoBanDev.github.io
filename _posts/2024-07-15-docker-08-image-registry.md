---
layout: single
title:  "이미지 레지스트리"
categories:
  - Docker
tags:
  - [Docker, Image, Image-registry]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## 이미지 레지스트리(Image Registry )
- 이미지 레지스트리는 이미지를 저장하고 공유하는 저장소이다. 대표적으로 `도커 허브(docker hub)`가 있다.
- 이미지 레지스트리 기능
  - 이미지를 `다운로드`하고 `업로드` 할 수 있다.
  - 이미지를 `검색`할 수 있다.
  - 이미지의 `버전을 관리`할 수 있다.
  - 원하는 사용자만 이미지를 다운받을 수 있도록 `인증 처리`와 `권한 관리` 기능을 제공한다.
  - 안전한 이미지를 다운로드 받을 수 있도록 `업로드된 이미지의 보안을 검증하는 기능`을 제공한다.
  - DevOps 파이프라인 기능과 연계해서 `이미지를 업로드했을 때 자동으로 배포`가 될 수 있도록 연계 기능 및 `알림 기능`을 제공한다.

  <img src="https://github.com/user-attachments/assets/5833e195-996a-4258-89d0-581559a22bab" width="90%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 이미지 저장 공간
- 이미지 저장 공간은 크게 세 가지로 분류할 수 있다.
1. 로컬 스토리지
   - 도커가 실행되는 호스트 OS의 특정 폴더를 의미한다.
2. 온라인 저장소
   - 프라이빗 레지스트리 : 특정 네트워크에서만 접근이 가능하다.(사내망, 내부망)
   - 퍼블릭 레지스트리 : 모든 네트워크에서 접근이 가능하다.(docker hub)

- `docker run 이미지명` 명령어를 실행하면 
  1. `로컬 스토리지에 이미지가 있는지 확인`한다. 로컬 스토리지에 이미지가 있으면 해당 이미지로 컨테이너를 실행한다.
  2. 로컬 스토리지에 이미지가 없으면 `온라인 레지스트리에서 이미지를 로컬 스토리지로 다운로드` 받는다.
  3. `다운 받은 이미지를 사용해서 컨테이너를 실행`한다.
  4. 이 후 동일한 이미지를 사용하는 경우 로컬 스토리지의 이미지를 통해 컨테이너를 실행한다.

  <img src="https://github.com/user-attachments/assets/4dcf0f09-cf3a-4930-a18e-6b7f97b8a5b0" width="80%" height="80%"/>

<br>

## 나만의 레지스트리 만들기
- 서버에 레지스트리 설치해서 사용하는 방법이 있다.
  - 서버에 `하버(HARBOR)`나 `도커 프라이빗`같은 레지스트리 소프트웨어 제품을 설치해서 사용할 수 있다.
- 퍼블릭 클라우드의 서비스를 이용하는 방법이 있다.
  - 퍼블릭 클라우드의 서비스로는 AWS의 `ECR`, Azure의 `Container Registry (ACR)` 같은 서비스를 사용할 수 있다.

  <img src="https://github.com/user-attachments/assets/10c55265-f499-488c-bc28-13bf7e671a03" width="80%" height="80%"/>

<br>

## 이미지 네이밍 규칙 : `레지스트리주소/프로젝트명/레포지토리명:이미지태그`

**레지스트리 주소**
- 이미지를 `어떤 레지스트리에서 다운로드하고 업로드 할지`를 지정해야 한다.
- 레지스티리주소가 비어 있는 경우 기본 레지스트리 값인인 Docker Hub의 주소인 `docker.io`로 지정된다.
- 개인 레지스트리가 있는 경우 레지스트리 주소를 입력해야 한다.

**프로젝트 명**
- 이미지를 `보관하는 폴더` 같은 개념이다.
- `도커 허브`의 경우 가입한 `사용자의 계정명이 프로젝트 명`이 된다.
- 프로젝트명이 비어 있는 경우 
 - 도커는 도커사가 직접 검증한 이미지는 오피셜 이미지로 제공하고 있다.
 - 그리고 이 오피셜 이미지는 라이브러리라는 프로젝트에서 관리한다.
 - 그래서 별도로 계정명을 입력하지 않으면 기본 값인 `library`로 지정된다.

**레포지토리명**
- 다운받을 이미지의 `레포지토리 이름`이다.
- 일반적으로 레포지토리 이름을 이미지 이름으로 만든다. 그래서 보통 레포지토리 명을 이미지 명이라 부른다. 

**이미지 태그**
- 이미지의 `버전 정보`이다.
- 이미지 버전에는 숫자와 영문 모두 사용할 수 있다.
- 이미지 태그가 비어 있는 경우 기본 값인 `latest`로 지정된다.

<img src="https://github.com/user-attachments/assets/f5ef54b6-7dea-4093-92e4-7f72c6b14582" width="80%" height="80%"/>

<br>

로컬 스토리지에 이미지를 저장하면 `프로젝트명(사용자)/레포지토리명` 로 조합되어 `REPOSITORY`에 저장된다.
- 저장된 이미지를 검색해 보면 알 수 있다.

```bash
$ docker image ls
```
<img src="https://github.com/user-attachments/assets/8c65bc8a-20f4-44de-b649-46b486a74fe3" width="80%" height="80%"/>  



<br>

- 명령어 예시 : 레지스트리주소 생략, 이미지태그 생략

```bash
# 사용자 기준 명령어
$ docker run devwikirepo/tencounter

# 시스템이 실제 실행하는 명령어 : `레지스트리주소/프로젝트명/이미지명:이미지태그`
$ docker run docker.io/devwikirepo/tencounter:latest
```

<br>

- 명령어 예시 : 레지스트리주소 생략, 프로젝트명 생략, 이미지태그 생략

```bash
# 사용자 기준 명령어
$ docker run nginx

# 시스템이 실제 실행하는 명령어 : `레지스트리주소/프로젝트명/이미지명:이미지태그`
$ docker urn docker.io/library/nginx:latest
```

<br>

**주요 핵심**
```markdown
- 이미지명은 `레포지토리명:태그`로 관리 된다.
- `docker run nginx` 명령어의 `ngninx`는 이미지명이 아니라 `레포지토리명`이다. 
- 숨어있는 latest 태그를 지정한 `nginx:latest` 가 온전한 이미지명이다.
```


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## Docker Hub의 이미지
- 도커 허브(Docker Hub)에서 이미지를 검색하면 원하는 이미지를 찾을 수 있다.

### Docker Hub의 이미지 분류
- 왼쪽 필터링 메뉴의 `Trusted Content` 항목을 보면 이미지를 세 가지로 분류하고 있다.
  - `Docker Official Image` : 도커의 라이브러리 프로젝트에서 제공하는 공식 이미지를 의미한다.
  - `Verified Publisher` : 외부 기업에 의해 제공된 이미지로 도커의 검증을 통과한 이미지를 의미한다.
  - `Sponsored OSS` : 도커가 후원하는 오픈 소스 프로젝트들의 이미지를 의미한다.
- `docker run nginx` 명령어를 실행하면 `Docker Official Image`로 지정된 `nginx 이미지`를 다운로드 받게 되는 것이다.
<img src="https://github.com/user-attachments/assets/67d4a45d-bc8d-4a82-9756-2191424400c3" width="80%" height="80%"/>

<br>

### Docker Hub의 이미지 상세 정보
- 이미지를 클릭하면 다운로드 수와 좋아요 수를 확인할 수 있다.
<img src="https://github.com/user-attachments/assets/750c45fb-0a98-4311-85ca-9ab8f80bb242" width="80%" height="80%"/>

<br>

### Docker Hub의 이미지 상세 정보 - Tags
- 이미지 상세정보의 `Tag` 탭을 클릭하면 다양한 버전의 이미지들을 확인할 수 있다.
- 태그에 `stable` 이라고 적혀 있는 것은 안정적인 버전이라 생각하면 된다.
- 한 이미지당 `OS와 시스템 아키텍처`에 따라 여러 파일들이 존재하는 걸 볼 수 있다. 
- 도커는 이미지 다운로드 요청이 오면 먼저 `명령어를 실행한 시스템의 아키텍처와 운영체제를 확인`한 후 해당 시스템과 호환되는 `가장 적절한 버전을 자동을 선택`해서 다운로드 해준다.
<img src="https://github.com/user-attachments/assets/85f4b145-3ad6-494d-ad09-28bdf55348a2" width="80%" height="80%"/>


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## Docker Hub 레포지토리
- 이미지를 업로드 하면 저장되는 공간이다.
- 도커 허브의 경우 사용자 계정명이 레포지토리명으로 지정된다.
<img src="https://github.com/user-attachments/assets/68e53750-1b12-4dea-8f18-e2ac40b90dc5" width="80%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## Docker Hub에 이미지 업로드 하기
- 이미지명은 네이밍 규칙에 맞게 작성한다.

### 이미지 다운로드
명령어 : `docker pull 레포지토리명:태그`

```bash
$ docker pull devwikirepo/simple-web:1.0
```
<br>

### 이미지명 변경
명령어 : `docker tag 기존[사용자명/레포지토리명:태그] 추가할[사용자명/레포지토리명:태그]`

```bash
$ docker tag devwikirepo/simple-web:1.0 midoban/my-simple-web:1.0
```

- `tag` : 새로운 이미지 명(레포지토리명:태그)을 추가하는 명령어다. 좀 더 정확하게 말하자면, 실제 이미지 파일은 하나만 존재하고 하나의 이미지 파일에 태그를 추가하는 것이다.
- 실제 이미지와 이미지 명은 파일과 참조 링크 관계라고 생각하면 된다.
- tag 명령어로 이미지 명을 추가한 후 `docker image ls` 명령어로 확인해 보면 `기존 이미지와 추가된 이미지의 IMAGE ID가 동일`한 것을 확인할 수 있다.
  <img src="https://github.com/user-attachments/assets/89d1bd78-c569-47fb-b8d0-0bcbc152e2a7" width="80%" height="80%"/>

- `tag` 명령어를 통해 새로운 이미지를 만드는 이유는 사용자명 변경 후 새로운 레지스트리에 업로드 하기 위해서다.
   -  만약 다른 레지스트리에서 이미지를 다운로드 받았다면 프로젝트명(사용자명)이 지정되어 있을 수 있다.
   - `docker pull otheruser/nginx` 이렇게 다운로드 받은 후 로컬 스토리지를 보면 레포지토리에 `otheruser/nginx`로 지정되어 있다. 
   - 이미지 네이밍 규칙 상 `레지스트리주소/프로젝트명/레포지토리명:이미지태그` 이 구조를 지켜야 하는데 이미 `otheruser/nginx` 이렇게 지정되어 있으면 `프로젝트명(사용자명)`을 내껄로 변경할 수가 없다.
   - 그로 인해 내 레포지토리에 업로드 할 수가 없다. 따라서 tag 명령어로 `otheruser/nginx` -> `my-nginx` 이렇게 변경해 줘야 한다.
   - 그래야 이후 내가 `myuser/my-nginx:l.0` 이렇게 업로드 할 수 있다.
  
  <img src="https://github.com/user-attachments/assets/05d9c54a-7a75-4252-9474-9785a79c370a" width="80%" height="80%"/>

<br>

### 이미지 업로드
명령어 : `docker push 레포지토리명:태그`

```bash
$ docker push midoban/my-simple-web:1.0
```

- 도커 허브에 이미지를 업로드할 때는 업로드할 레지스트리 계정의 인증 정보가 필요하다. 
- 그래서 우선 도커 허브의 계정에 로그인을 해야 한다. 로그인이 되어 있지 않으면 `denied: requested access to the resource is denied` 접근이 거부된다.
<img src="https://github.com/user-attachments/assets/24fe481f-3c6f-464e-afc4-b020b5c801f8" width="80%" height="80%"/>

<br>

#### 이미지 레지스트리 인증 정보 생성
명령어 : `docker login`  

```bash
$ docker login
```
- 도커 로그인 명령으로 로그인하면 스토리지의 특정 공간에 인증 정보가 파일 형태로 저장된다.
- 아이디와 비밀번호를 입력해야 한다.
<img src="https://github.com/user-attachments/assets/d3892f88-83ea-4269-9260-b940f857220d" width="80%" height="80%"/>

<br>

#### 이미지 레지스트리 인증 정보 삭제
명령어 : `docker logout`

```bash
$ docker logout
```
- 도커 로그아웃을 입력하면 인증 정보가 저장되어 있는 파일이 삭제된다.
<img src="https://github.com/user-attachments/assets/294b4c9b-bd1e-44b2-9798-7c9202b05de3" width="80%" height="80%"/>


<br>

### 이미지 삭제
명령어 : `docker image rm 레포지토리명:태그`

```bash
$ docker image rm midoban/my-simple-web:1.0
```
<br>

### 도커 허브에 이미지 업로드
명령어 : `docker push 레포지토리명:태그`

```bash
$ docker push midoban/my-simple-web:1.0
```
<img src="https://github.com/user-attachments/assets/b089748c-7550-49fa-8734-7d0977390112" width="80%" height="80%"/>


<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>


## 도커 허브 이미지로 컨테이너 실행
명령어 : `docker run -d -p 80:80 midoban/my-simple-web:1.0`

- 로컬 스토리지에 있는 이미지를 삭제한 후 진행하자.

```bash
# 이미지 삭제
$ docker image rm 레포지토리명:태그

# 컨테이너 실행
$ docker run -d -p 80:80 midoban/my-simple-web:1.0
```

<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>


## 이미지 공유와 개발 환경 구축
- 지금까지 다른 레지스트리에 있는 이미지를 다운 받고 내 레지스트리의 레포지토리에 이미지를 업로드 해 보았다. 
- 그리고 내 레포지토리에 있는 이미지를 가지고 컨테이너를 실행까지 해 보았다.
  
- 도커를 사용하기 전에는 이미지가 아닌 소스 코드나 애플리케이션을 파일로 공유했다. 
- 실제로 애플리케이션을 실행할 때 이 OS나 라이브러리가 PC별로 차이가 있기 때문에 개발자의 개발용 PC에서는 잘 실행되던 애플리케이션도 운영 환경이나 다른 PC에서는 제대로 동작하지 않는 문제가 빈번하게 발생한다.
- 그래서 운영 환경을 테스트할 수 있는 QA 환경을 만들고 운영 환경과 완벽하게 일치시키는데 많은 노력을 기울이지만 그럼에도 불구하고 환경의 불일치로 인해서 많은 문제들이 발생한다.

- 컨테이너를 사용하면 이렇게 환경의 차이에서 나오는 문제들을 근본적으로 해결할 수 있다.
- 간단한 웹서버부터 복잡한 애플리케이션 서버 구성까지 이미지에 실행 가능한 형태로 저장해서 공유하면 서버를 운영하는 비용을 줄이고 새로운 서버를 구성하는 시간을 크게 단축시킬 수 있다.



<!--<img src="" width="80%" height="80%"/>-->
