---
layout: single
title:  "이미지 레이어(Image Layer)"
categories:
  - Docker
tags:
  - [Docker, Image, Image-layer]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## 레이어드 파일 시스템(Layered File System)
도커 이미지는 저장소를 효율적으로 사용하기 위해서 `레이어드 파일 시스템(layered file system)`으로 구성되어 있다.
여러 개의 층으로 구성되어 있는 것에서 하나의 층을 `레이어(layer)`라고 표현한다.

Nginx를 실행했을 때 출력을 확인해 보자. 화면을 보면 Nginx라는 하나의 이미지를 다운받는 과정에서 `pull` 이 `여러 단계에 걸쳐서 실행`되는 것이 보인다.
여기서 한 줄 한 줄이 각각 하나의 레이어를 의미한다. 이 레이어들이 모여서 하나의 이미지로 구성되는 것이다. 각각의 레이어는 이미지의 일부분을 나타낸다.

<img src="https://github.com/user-attachments/assets/910dc4b3-20ba-4e03-a00d-02ab9195b8fb" width="90%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 레이어드 파일 시스템의 장점
하나의 이미지를 여러 개의 레이어로 구성하는 이유는 `재사용하기 유리한 구조`이기 때문이다. 레이어드 파일 시스템을 사용하면 `공간을 효율적으로 사용`할 수 있다.
그래서 이미지를 저장하고 전송할 때 스토리지와 네트워크 사용량을 절약할 수 있다.

건축 도면을 예로 레이어 구조를 이해해 보자.
건축 도면을 그리기 위해 투명한 도면 용지(레이어)를 여러 장 준비한다.
각각의 도면 용지에 건물의 구조, 토목, 전기, 조경 등 `각각의 주제별로 한 장씩` 그린다. 
이제 각각의 `도면 용지(레이어)를 하나로 합치면` 완성된 설계도 A(하나의 이미지)가 될 것이다.

<img src="https://github.com/user-attachments/assets/ee6a1d58-942f-40f6-9359-3b19fbee183b" width="60%" height="80%"/>
<br><br>

만약 설계도에서 조경 부분을 수정해야 하는 경우 이 설계도 A가 한 장의 도면으로 되어 있었다면 조경 부분을 수정하면 전체 도면이 영향을 받게 된다.
하지만 레이어 구조로 되어 있으면 조경 부분만 수정하면 나머지 전기, 토목, 구조는 변경의 영향을 받지 않게 된다. 즉, 변경 사항에 있어서 재활용이 유리한 구조라는 겁니다

<img src="https://github.com/user-attachments/assets/e10edc56-9b12-49b9-bda9-f1d824d2ab78" width="90%" height="80%"/>
<br><br>

그리고 이 설계도 A와 구조, 토목, 조경 부분이 동일하고 전기 배선만 다른 B 건물이 있다고 생각해 보자. 
만약에 레이어드 구조가 아니었으면 설계도 A와 설계도 B 두 장을 각각 통째로 관리해야 한다. 
하지만 레이어 구조를 활용하면 `겹치는 도면(레이어)는 재사용`할 수 있다.
이렇게 겹치는 레이어는 재사용하는 것이 종이 사용량도 줄이고 보관하기에도 효율적이다.

그리고 이렇게 겹치는 레이어를 재사용한 상태에서 왼쪽 순서대로 읽으면 설계도 A, 오른쪽 순서로 읽으면 설계도 B로 읽을 수 있다.
설계도 A의 조경, 토목 구조가 설계도 B와 겹치기 때문에 받는 사람은 전기 도면 한 장만 받으면 설계도 B를 만들 수 있게 된다.
만약 레이어드 구조가 아니었으면 전기 부분이 다르다는 것은 전체 설계도가 다르다는 걸 의미하기 때문에 완전히 `새로운 설계도 한 장을 통째로 전달`해야 된다. 즉 레이어 구조는 데이터 전송에 있어서도 더 유리한 구조이다.
<img src="https://github.com/user-attachments/assets/ac4a6d6a-b40d-44b5-83fd-547d6bbe5ba9" width="90%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 이미지 레이어
이미지의 레이어는 건축 도면과 비슷하지만 조금 다른 방식으로 구성된다. 이미지의 레이어는 바로 직전 단계의 레이어에서 변경된 내용들만 저장된다.

서버에 간단한 페이지를 출력하는 NGINX를 설치 한다고 생각해보자. 
- 먼저 OS를 설치한다.
- OS 위에 NGINX 소프트웨어를 설치한다.
- NGINX 설정 파일을 수정한다.
- 브라우저에서 표시되는 index.html 파일을 사용자에게 응답할 내용으로 수정한다.

이제 이 순서대로 이미지의 레이어를 구성한다고 생각해보자. 
- 먼저 OS를 준비한다.
- 그리고 OS 위에서 NGINX 소프트웨어를 설치한다.  
  소프트웨어를 설치하면 OS의 특정 폴더에 NGINX 소프트웨어와 관련된 파일들이 추가된다. 그래서 NGINX를 설치한다는 것은 기존 OS 파일 시스템에 추가되는 부분이 생기는 것을 말한다.
- 그리고 NGINX 설정 파일을 수정한다.
- 다음으로 브라우저에서 표시되는 index.html 파일을 사용자에게 응답할 내용으로 수정한다.

<img src="https://github.com/user-attachments/assets/8ef1e083-e4ef-489a-9b20-c2143b7dd116" width="80%" height="80%"/>

이미지에서 `한 번 저장된 레이어는 변경할 수 없다.` 변경 사항이 있으면 계속 새로운 레이어로 저장해야 한다. 마치 소스 코드에서 한번 푸시한 내용은 되돌릴 수 없는 것과 같다.

이렇게 **기존 레이어에서 변경되는 것들은** 기존의 레이어를 수정하는 것이 아니라 전 레이어와 비교해서 `추가되거나 변경된 파일들이 다음 레이어로 저장`되는 것이다. 그래서 두 번째 NGINX 설치 레이어에는 `NGINX 소프트웨어가 추가된 부분`만 따로 가지고 있다.

이렇게 이미지 A를 완성한 다음에 이미지 B를 새로 만드는데 이미지 B는 마지막 단계에서 `index.html 파일의 내용을 다른 내용으로 변경`한다고 가정해보자. 
이미지 B를 만들 때 NGINX의 소프트 버전과 NGINX 설정을 이미지 A와 모두 동일하게 설정하면 세 번째 Nginx 설정 레이어까지는 재사용을 하게 된다. 
마지막 index.html 파일만 내용이 다르기 때문에 `마지막 레이어만 새롭게 추가`되어서 이미지 B가 완성된다. 그래서 이미지 A와 B는 총 `3개의 레이어를 공유`하고 `각각 하나의 레이어를 별도로 사용`하는 구조가 된다.

정리하자면 이미지의 레이어는 순차적으로 쌓이고, 각각의 레이어는 이전 레이어에서 변경된 부분을 저장하고 있다 그리고 같은 변경이 일어난 레이어는 공유해서 재사용할 수 있다
<img src="https://github.com/user-attachments/assets/d0aa499d-477b-4fef-917d-17691e12c460" width="80%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 컨테이너 실행
`docker run` 명령으로 컨테이너를 실행하면 이미지의 가장 마지막 레이어 위에서 `새로운 읽기 쓰기 전용 레이어`가 추가된다.
이 추가된 레이어는 `컨테이너 레이어`라고 부른다. 애플리케이션에서 로그가 쌓이거나 컨테이너가 실행 중에 생기는 모든 변경 사항들은 `컨테이너 레이어`에 저장된다.  
<img src="https://github.com/user-attachments/assets/46d41743-856e-412e-b55b-78f24e7153b0" width="90%" height="80%"/>


`이미지의 레이어`는 변경 불가능한 `읽기 전용 레이어`다. 이미지의 레이어와 컨테이너의 레이어는 역할이 완전히 다르다. 
이미지의 레이어는 컨테이너를 실행하기 위한 `세이브 포인트 역할`을 하고 컨테이너의 레이어는 실제로 이 이미지를 컨테이너로 실행한 다음에 `프로세스가 변경하는 내용을 기록`하는 레이어다.   

컨테이너 레이어는 이미지의 상단에 추가돼서 컨테이너 실행 중에 변경되는 내용만 기록한다.
그러면 이미지 A로 두 번째 컨테이너인 컨테이너2를 실행하면 컨테이너2에 읽기 쓰기 레이어(컨테이너 레이어)가 생성된다. 
이 두 개의 컨테이너는 이미지가 같기 때문에 `동일한 읽기 전용 레이어를 공유`하게 된다.
따라서 컨테이너를 실행하면 전체 레이어를 복사하지 않고 읽기 전용 레이어인 이미지 위에 새로운 컨테이너 레이어가 하나씩 추가된다.  
<img src="https://github.com/user-attachments/assets/ad215de4-9776-4bd9-843c-be95e4447038" width="90%" height="80%"/>

동일한 이미지로 컨테이너를 아주 많이 만들어도 실행된 모든 컨테이너는 하나의 이미지를 공유해서 읽어온다. 
실제로 큰 부분을 차지하는 이미지를 하나로 유지할 수 있기 때문에 컨테이너를 생성할 때 속도가 빨라지고 저장소도 더 효율적으로 사용할 수 있다.
그리고 이미지에 포함되어 있는 index.html 파일의 내용이 동일하기 때문에 컨테이너1, 2는 웹 브라우저로 접근했을 때 hello nginx라는 동일한 문자를 출력한다.

<br>

이미지 B를 사용해서 새로운 컨테이너를 실행해 보자.  
3번 컨테이너는 컨테이너 1, 2와 세 번째 레이어인 nginx 설정까지만 공유한다. 그래서 3번 컨테이너로 접근하면 이미지 B의 index.html 파일의 내용인 커스텀 인덱스가 출력된다. 
<img src="https://github.com/user-attachments/assets/512971cd-78e6-4b53-8158-925d775d283b" width="90%" height="80%"/>

정리하자면 모든 컨테이너는 각자 자기만의 읽기 쓰기 레이어인 `컨테이너 레이어`를 한장씩 가진다. 
그리고 컨테이너를 만들 때 사용된 이미지에 따라서 이미지의 읽기 전용 레이어 전체를 공유할 수도 있고 일부만 공유할 수도 있다. 
이렇게 이미지의 읽기 전용 레이어를 활용하면 컨테이너를 실행할 때 `전체 공간을 복사하지 않아`도 되기 때문에 컨테이너를 빠르게 실행할 수 있다.
그리고 컨테이너가 늘어나면서 사용하는 공간을 최대한 작게 관리할 수 있습니다. 

이미지의 레이어와 컨테이너 레이어의 관계는 비유하자면 건축 도면으로 실제 건물을 지은것과 비슷하다.
건축 도면은 하나만 있어도 여러 개의 건물을 만들 수가 있다. 이 건물들은 컨테이너가 CPU와 메모리를 사용하는 것처럼 실제로 전기, 땅 같은 자원을 사용한다. 
각각의 건물은 다른 이름과 주소로 사용되면서 각각의 흔적이 남게 된다. 하지만 같은 설계도로 만들어졌기 때문에 상가, 집, 학교처럼 이 건물이 근본적으로 수행하는 목적은 동일하다.
마찬가지로 하나의 이미지로 컨테이너를 수십 개 실행해도 이 컨테이너가 수행하는 근본적인 기능은 동일하다. 
컨테이너가 실행되면서 읽기 쓰기 레이어에 저장되는 값은 `각각 고유한 값`들이 쌓이게 된다.
<img src="https://github.com/user-attachments/assets/186ddd9a-2b96-42d1-915f-5ba3cc0e0b3b" width="90%" height="80%"/>



<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 이미지 레이어 해시값
이미지 레이어는 `레이어의 내용을 바탕`으로 생성된 고유한 해시값이 존재한다.  
완벽히 **동일한 내용을 가진 레이어는 동일한 해시값**을 가진다.  
단 이미지의 레이어는 이전 파일에서 변경된 사항을 저장하기 때문에 완전히 동일한 명령을 수행할 지라도(같은 내용이라도) `구성한 순서가 다르면` 완전히 다른 레이어로 구성되어 해시값이 달라진다.  

아래의 `COPY index.html..` 명령의 결과가 동일하더라도 `imageB의 마지막 레이어의 해시값`은 이전 레이어의 추가로 인한 `imagA의 마지막 레이어의 해시값`과 다르다.

```bash
  # imagA 생성
  FROM nginx
  COPY index.html /usr/share/nginx/html/index.html # 마지막 레이어
```
```bash
  # imageB 생성
  FROM nginx
  COPY nginx.conf /etc/nginx/nginx.conf            # 추가 COPY 명령
  COPY index.html /usr/share/nginx/html/index.html # 마지막 레이어
```  

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 이미지 레이어 조회
이미지 레이어 이력 조회 : `docker image history 이미지명`

>`nginx:1.24.0` 의 베이스 이미지에 레이어를 추가한 `hello-nginx`와 `config-nginx` 이미지를 비교해 보면서 이미지의 레이어를 간단히 살펴보자.


### `hello-nginx` 이미지의 레이어
devwikirepo/hello-nginx 이미지를 다운로드 한 후 이미지 이력을 조회해 보자.

```bash
# 이이미 다운로드
$ docker pull devwikirepo/hello-nginx

# 이미지 이력 조회
$ docker image history devwikirepo/hello-nginx
```

<br>

`devwikirepo/hello-nginx` 이미지는 베이스 이미지 `nginx:1.24.0`에 호스트에 만들어 놓은 `index.html` 파일을 `nginx의 기본 경로에 있는 index.html 파일`에 덮어쓰기 해서 만든 이미지다.  
아래 히스토리를 보면 호스트에 있는 `index.html` 파일을 복사한 후 실행 될 nginx 디렉토리인 `/usr/share/nginx/html/` 경로에 붙여넣는 명령어를 수행하도록 했다.   
<img src="https://github.com/user-attachments/assets/3f376868-d777-41e4-8c03-078a82111f95" width="90%" height="80%"/>

>`COPY`  
COPY 명령은 실행 할 때마다 임시 컨테이너가 생성된다. 이 임시 컨테이너는 FROM 명령에 지정된 이미지를 토대로 만들어진 컨테이너 환경을 제공받는다.
이를 통해 `빌드 컨텍스트에 있는 파일`을 복사하여 `실행 될 이미지의 내부 파일 시스템의 경로`에 붙여 넣는 동작을 수행하게 된다.  
(동일한 파일이 있는 경우 덮어쓰기)  

이미지 메타데이터를 확인하는 `inspect` 옵션을 통해 해시값을 확인할 수 있다.

```bash
$ docker image inspect devwikirepo/hello-nginx
```

베이스 이미지인 `nginx:1.24.0` 의 레이어는 6개 층으로 이루어져 있고, 각 레이어의 해시값이 존재한다.  
7번째 층에는 `COPY` 명령을 통해 새로 추가된 이미지 레이어가 생성되어 해시값이 추가된 것을 볼 수 있다.  
<img src="https://github.com/user-attachments/assets/5bb290b8-0119-48df-b03c-545a60da3eaf" width="90%" height="80%"/>

<br>

### `config-nginx` 이미지의 레이어
devwikirepo/config-nginx 이미지를 다운로드 한 후 이미지 이력을 조회해 보자.

```bash
# 이이미 다운로드
$ docker image pull devwikirepo/config-nginx

# 이미지 이력 조회
$ docker image history devwikirepo/config-nginx
```
<br>

`devwikirepo/config-nginx` 이미지는 마찬가지로 `nginx:1.24.0` 베이스 이미지에 index.html 파일뿐만 아니라, nginx.conf 파일도 nginx 경로에 덮어쓰기 한 후 만들어진 이미지다.  
`COPY 명령을 두 번` 사용했기 때문에 `새로운 레이어는 2개`가 생성된다.  
<img src="https://github.com/user-attachments/assets/0f421b61-0da3-48b1-91fa-abb6ca36b82b" width="90%" height="80%"/>



베이스 이미지가 `nginx:1.24.0` 로 같기 때문에 6개까지의 레이어 해시값은 `hello-nginx`와 동일하다.  
이후 `두 번의 COPY 명령`으로 레이어 2개가 생성되었고, `각 레이어 해시값`을 통해 추가된 걸 확인할 수 있다.
<img src="https://github.com/user-attachments/assets/4a19f298-7511-4e8d-a6e3-a1554893eea7" width="90%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## COPY 명령어 동작
COPY 명령은 Dockerfile을 작성할 때 사용된다.  
Dockerfile의 간단한 구성을 보자.
```dockerfile
# 베이스 이미지 지정
FROM nginx:1.24.0

# 호스트의 파일들을 이미지 안으로 복사
COPY index.html /usr/share/nginx/html/

# (선택사항) nginx 설정 파일 복사
COPY nginx.conf /etc/nginx/nginx.conf

# 포트 설정 (선택사항이지만 문서화 목적으로 추가)
EXPOSE 80

# nginx 실행 (베이스 이미지에 이미 설정되어 있어서 생략 가능)
CMD ["nginx", "-g", "daemon off;"]
```
위 dockerfile을 보면 COPY 명령이 두 번 수행된다. index.html과 nginx.conf 파일을 특정 경로에 복사한다.  

이 명령을 조금 구체적으로 들어가보자. 
index.html은 어디에 있는 파일인가?  
COPY 명령의 `첫 번째 인자`에 들어가는 파일은 `호스트(내 컴퓨터)에서 Dockerfile이 위치한 디렉토리를 기준`으로 찾는다.  
즉 nginx 웹서버의 index.html를 내가 원하는 내용으로 수정하기 위해 내 호스트에 직접 만든 파일이다.  

두 번째 /usr/share/nginx/html/ 경로는 이미지 내의 nginx 소프트웨어에 있는 경로이다. 
이미지로 압축된 경로에 접근해서 복사가 가능한가? 비밀은 FROM 명령에 있다.  
`FROM` 명령은 이미지를 빌드하는 명령어다. 즉 압축된 nginx:1.24.0 이미지를 압축 해제를 하여 이미지의 `파일 시스템을 사용 가능한 상태`로 만든다.  
따라서 뒤에 나오는 COPY 명령어를 통해 이미지 내부 파일 시스템에 접근할 수 있고, 그 결과 원하는 변경사항을 새로운 레이어로 생성할 수 있는 것이다.

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 이미지 레이어 저장 및 공유
아래 4개의 이미지가 있다. 각각의 이미지는 `nginx:1.24.0 를 베이스 이미지로 만든어진 이미지다.
4개의 이미지를 다운로드 받게 되면 총 29개(6개 + 7개 + 8개 + 8개)의 레이어가 저장될까?
도커는 동일한 이미지의 레이어를 공유하는 구조이기 때문에 최초에는 6개를 모두 다운받고 이후 동일한 이미지 레이어는 제외하고 다운받게 된다.  
즉 결과적으로 총 10개(6개 + 1개 + 1개 + 2개)만 저장하게 된다.  

이렇게 도커는 레이어 구조와 레이어 공유을 통해 스토리지 공간을 효율적으로 사용할 수 있다.
<img src="https://github.com/user-attachments/assets/a808a052-de7a-4b0e-ba7c-bc04e055f124" width="90%" height="80%"/>

이렇게 동일한 Nginx 1.24 이미지에서 출발한 이미지들은 모두 같은 Nginx레이어를 공유한다.  
이후에 어떤 파일을 어떤 순서로 수정하냐에 따라서 같은 레이어를 공유할 수도 있고, 새로운 레이어가 만들어지는 경우도 있다.   
레이어 구조가 없었으면 각각의 이미지들이 투명하게 표시된 부분까지 전부 포함해야 된다.  
그렇게 되면 이미지를 저장하는 스토리지 공간이나 이미지를 네트워크로 전달할 때 데이터 전송용량도 더 커졌을 것이다.  
<img src="https://github.com/user-attachments/assets/8bb3b528-abd6-4321-a1c5-e8ec46b84000" width="90%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## Copy-on-Wright(CoW) 전략
copy-on-write 전략은 파일이 수정될 때 `기존 읽기 전용 레이어의 파일`을 `읽기 쓰기 레이어로 복사한 후 수정`하는 방식을 의미한다.

예를 들어서 Nginx 컨테이너를 실행한 다음에 이 실행 중인 컨테이너 안에서 index.html 파일의 내용을 다시 바꾼다고 가정해보자.
그러면 읽기 전용 레이어에 있는 index.html 파일의 내용을 수정하는 것이 아니라 컨테이너 레이어에 수정할 index.html 파일을 카피해 온 다음에 
복사 해온 파일을 수정, 라이트 하는 것이다. 그래서 Copy-on-Write 전략으로 부르는 것이다.  

Copy-on-Write는 파일을 처음 수정할 때 한 번만 발생하고, 이후 수정은 컨테이너 레이어에 있는 파일을 직접 수정하게 된다.  

카피온 라이트(Copy-on-Write) 전략을 사용하는 목적은 불변 레이어(immutable layer)를 위해서다. 
이미지의 레이어는 변하지 않는다는 의미로, 한 번 만들어진 이미지는 각 레이어가 해시로 암호화된 고유 값을 가지기 때문에 어떤 방식으로도 변경할 수 없다.
변경되는 사항은 카피온 라이트(Copy-on-Write)를 통해서 새로운 레이어로 추가된다. 
그래서 이미지는 일관적이고, 동일한 이미지를 사용하는 컨테이너는 모두 동일한 파일 시스템 상태로 사용되는 것이 보장된다.  

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 이미지 공유가 컨테이너 생성 속도에 미치는 영향
VM과 비교해보자.  
VM은 레이어 구조가 아니기 때문에 새로운 VM을 만들기 위해서는 OS와 소프트웨어를 모두 설치해야 한다.  
VM에서도 파일의 상태를 압축해서 저장해두는 스냅샷이라는 개념이 있지만, 스냅샷도 마찬가지로 레이어 구조가 아니기 때문에 스냅샷으로 VM을 만든다는 것은 완전히 새로운 환경으로 복사해서 만드는 것과 동일합니다.
그래서 시간이 오래 걸리게 되는 것이다.

반면에 이미지-컨테이너 구조는 읽기 전용 레이어를 공유하기 때문에 컨테이너를 실행할 때마다 이미지를 복사하지 않아도 된다.
그래서 속도가 VM을 실행하는 속도보다 훨씬 빠르게 된다. 물론 커널의 공유도 빠른 속도에 많은 이점을 준다.

A이미지를 통해 컨테이너를 실행한다 가정하면 A이미지를 '구성하는' 레이어들이 컨테이너가 '실행되기 전 부터' 다운로드 되어 어딘가에 저장되어 있고, 
이후 B이미지를 '다운로드'받을 때 A이미지와 공통된 레이어가 있다면, 새로운 레이어만 다운받기 때문에 빠르게 다운받을 수 있다.
그리고 A컨테이너, B컨테이너를 실행할 때 이미 읽기전용 레이어인 이미지를 다운받은 상태이기 때문에, 컨테이너 레이어만 추가하면 되서 생성속도가 VM과 비교했을때 아주 빠르다.

즉 결론은 독립된 공간을 만들기 위해 매번 OS, 소프트웨어, 의존성 요소 등을 매번 복사하는 시간이 필요없기 때문에 상대적으로 VM에 비해 독립된 가상화 공간의 생성 속도가 빠르다.
<img src="" width="90%" height="80%"/>

<!--<img src="" width="80%" height="80%"/>-->
