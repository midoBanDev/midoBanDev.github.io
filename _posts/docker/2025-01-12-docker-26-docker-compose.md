---
layout: single
title:  "도커 컴포즈 - Docker Compose"
categories:
  - Docker
tags:
  - [Docker, Compose]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## 도커 컴포즈(Docker Compose)
도커 컴포즈는 여러 개의 컨테이너를 하나의 파일에 정의하고 관리하기 위한 도구이다. 
yaml 파일 하나로 관리되며, 컨테이너로 실행할 여러 서비스를 정의하여 서비스 간의 의존성이나 네트워크, 볼륨 등을 파일 안에서 설정할 수 있다.
도커 컴포즈는 도커 데스크탑을 설치할 때 기본적으로 포함되어 있기 때문에 별도로 도커 컴포즈를 설치할 필요는 없다.  
<img src="https://github.com/user-attachments/assets/5e481b9b-7c0f-4ea0-b456-de58e7686a6f" width="90%" height="80%"/>

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## Docker Compose 파일
Docker Compose 파일은 yaml 포맷을 사용한다. 
YAML(YAML Ain't Markup Language)은 "YAML은 마크업 언어가 아니다"라는 의미로 HTML과 XML 같은 마크업 언어가 아니라 데이터를 간단하게 표현하는 데에 중점을 둔 형식을 말한다.
yaml의 기본은 들여쓰기와 키-값 쌍으로 이루어져 있다.

**기본적인 docker-compose.yml 예시:** 

```yaml
version: '3'
services:
  hitchecker:
    build: ./app
    image: hitchecker:1.0.0
    ports:
      - "8080:5000"
  redis:
    image: "redis:alpine"
```
- version: Docker Compose 파일 형식의 버전을 지정한다.
- services: 실행할 서비스들을 정의한다.
- build: Dockerfile이 있는 경로를 지정한다.
- image: 빌드될 이미지 이름과 태그를 지정한다.
- ports: 포트 매핑 (호스트:컨테이너)

<br>

### build와 image

**image만 있는 경우:** 

```yaml
services:
  redis:
    image: "redis:alpine"
```
- 지정된 이미지(redis:alpine)를 직접 사용하여 컨테이너를 실행
- 로컬에 이미지가 없으면 자동으로 pull 해서 사용

<br>

**build가 같이 있는 경우:**  

```yaml
services:
  hitchecker:
    build: ./app
    image: hitchecker:1.0.0
```
- 지정된 경로(./app)의 Dockerfile을 사용해서 이미지를 빌드
- image 태그가 함께 있으면 빌드된 이미지에 해당 이름과 태그를 부여
- docker-compose up 실행 시 이미지가 없으면 자동으로 빌드

<br>

### 환경변수 설정
- `environment` 또는 `env_file`을 사용하여 환경변수를 설정할 수 있다.

**1. environment 설정 방법** 
```yaml
version: '3'
services:
  web:
    image: busybox
    environment:
      - DEBUG=1
      - API_KEY=${API_KEY}
    command: "sh -c 'echo API API_KEY: $API_KEY && sleep 3600'"

  api:
    image: busybox
    environment:
      - DEBUG=1
      - API_KEY=${API_KEY}
    command: "sh -c 'echo API API_KEY: $API_KEY && sleep 3600'"

```
- `environment`는 compose.yml 파일 내에 `키-값` 형태로 직접 작성한다. 
- 변수 값 셋티은 `key=value` 형태로 선언해야 한다.
- `DEBUG=1` 처럼 값을 직접 지정할 수도 있고, `API_KEY=${API_KEY}`처럼 인수 값을 전달 받도록 설정할 수도 있다. 

**실행**  

```bash
$ API_KEY=your-secret-key docker compose up
```

<br>

**2. env_file 설정 방법**   
- 기본 파일 사용하는 경우 : `.env`
- `.env` 파일은 Docker Compose 실행 시 자동으로 로드되어 compose.yml에 적용된다.

```plaintext
# .env 파일
API_KEY=default-key
```
```bash
$ docker compose up
```


- 별도의 파일명을 사용하는 경우 : `test.env`
  
```plaintext
# test.env 파일
API_KEY=default-key
```
- compose 실행 시 `--env-file` 옵션으로 파일명을 지정해줘야 한다.

```bash
$ docker compose --env-file test.env up
```

<br>

**3. --env-file 옵션 없이 특정 파일 지정 방법**  
- compose 파일 안에 직접 환경 파일을 지정하면 된다.
  
```yaml
version: '3'
services:
  web:
    environment:
      - DEBUG=1
      - API_KEY=${API_KEY}
    env_file:
      - test.env
```

<br>

### 확장 필드 사용
도커의 확장 필드를 사용하면 공통된 환경 변수를 한 번에 모아서 설정할 수 있다. 
확장 필드는 보통 `x-`로 시작하는 키 아래에 정의한다.
`&`를 사용해 필드를 정의하고, 필요한 곳에서 `*`를 사용해 `&에 정의한 필드명`을 재사용하면 된다.
- `x-` 와 `&`로 정의되는 필드명은 임의의 이름을 사용해도 무방하다.


- 환경 변수 값 직접 설정
```yaml
x-com: &common-env
  DEBUG: "2"
  API_KEY: "api-common-key"

services:
  web:
    image: busybox
    environment:
      <<: *common-env
    # API_KEY 콘솔 출력  
    command: "sh -c 'echo API API_KEY: $$API_KEY && sleep 3600'"

  api:
    image: busybox
    environment:
      <<: *common-env
    # API_KEY 콘솔 출력
    command: "sh -c 'echo API API_KEY: $$API_KEY && sleep 3600'"
```
- `<<` 병합 키워드
  - YAML에서 확장 필드를 병합할 때 사용한다.
  - 위 예제에서 `<<: *common-env`는 x-com의 값을 해당 위치에 병합한다.

<br>

- 환경 변수 값 동적 설정
```yaml
x-com: &common-env
  DEBUG: "2"
  API_KEY: ${API_KEY}

services:
  web:
    image: busybox
    environment:
      <<: *common-env
    # API_KEY 콘솔 출력  
    command: "sh -c 'echo API API_KEY: $$API_KEY && sleep 3600'"

  api:
    image: busybox
    environment:
      <<: *common-env
    # API_KEY 콘솔 출력
    command: "sh -c 'echo API API_KEY: $$API_KEY && sleep 3600'"
```

<br>

**환경 변수를 설정 시 YAML의 문법 차이를 알아 두면 좋다.** 

**직접 environment를 정의할 때 (= 사용)** 

```yaml
environment:
  - DEBUG=1           # 이것은 shell 환경변수 스타일
  - API_KEY=${API_KEY}
```
이 경우는 Docker Compose가 직접 환경변수 리스트를 처리할 때의 형식이다. 
Docker Compose는 이 형식을 shell 환경변수처럼 해석한다.

<br>

**YAML 앵커와 병합을 사용할 때 (: 사용)** 
```yaml
x-common-variables: &common-variables
  environment:
    - DEBUG: 1           # 이것은 YAML 객체/맵 스타일
    - API_KEY: ${API_KEY}
```
이 경우는 YAML의 앵커(&)와 병합(<<)을 사용하기 때문에, YAML의 기본 문법을 따라야 한다. 
YAML에서는 키-값 쌍을 표현할 때 : 을 사용한다.

- `=`는 Docker Compose가 환경변수를 직접 처리할 때 사용하는 문법이다.  
- `:`는 YAML의 기본 문법으로, YAML 앵커와 병합을 사용할 때는 이 문법을 따라야 한다.



### 볼륨 설정




<br>





## Compose 기본 명령어

- `up` : 이미지를 빌드하고 컨테이너를 실행한다.
    
```bash
$ docker compose up
```
- 기본적으로 포그라운드에서 실행되며 터미널에 로그를 실시간으로 출력한다.
- 이미지 빌드는 `image`에 정의한 이미지가 이미 호스트에 없는 경우 자동으로 빌드하고, 있는 경우 기존 이미지 사용한다.

<br>

- `-d` : 컨테이너를 백그라운드로 실행한다.
  
```bash
$ docker compose up -d
```
- 로그는 `docker compose logs`로 확인

<br>

- `--build` : 이미지 강제로 빌드한다.
  
```bash
$ docker compose up --build
```
- 기존 이미지 존재 여부와 상관 없이 항상 새로 이미지를 빌드한다.
- 주로 Dockerfile이나 소스 코드 변경 시 사용한다.
- 기존 이미지는 <none>:<none>으로 태그 변경된다.

<br>

- `build` : 이미지 빌드 명령 수행
  
```bash
$ docker compose build
```
- `image`에 정의한 이미지만 빌드하고 컨테이너를 실행하지 않는다.


<br>

- `down` : 컨테이너 삭제
  
```bash
docker-compose down
```
- 컨테이너와 네트워크를 삭제한다.
- 완전히 새로 시작하고 싶을 때 사용한다.

<br>

- `logs` : compose로 실행한 모든 컨테이너 로그 확인
  
```bash
docker-compose logs
```

- `-f` : 로그 실시간 확인
  
```bash
docker-compose logs -f
```


## Compose 특징

**1. 컨테이너 명명 규칙**  
   
```
{project name}_{service name}_{number}
예: myapp_hitchecker_1
```
- project name : 기본값으로 Docker compose 파일이 들어있던 디렉토리명
- service name : Compose 파일에 정의된 service 이름으로 DNS 상의 domain 이름으로도 쓰인다.
- number : Container의 경우 번호를 부여


**2. 컨테이너 재사용**  
compose는 소스 코드나 도커 파일의 변경이 없는 경우 `docker compose up` 명령을 여러 번 실행해도 이미 실행 중인 컨테이너가 있다면 그대로 유지하고 아무 변화도 주지 않는다.
만약 컨테이너가 중지된 상태라면 기존 컨테이너를 다시 시작할 뿐 새로운 컨테이너를 생성하지 않는다.


**3. 네트워크 관리**  
Compose를 실행하면 자동으로 격리된 네트워크를 생성한다. (프로젝트명_default 형식) 
Compose 파일 내에 포함된 서비스들은 자동으로 같은 네트워크에 연결된다.

- 서비스간 통신은 `compose.yml`에 정의한 서비스명을 호스트명으로 사용할 수 있다.
  
```yaml
version: '3'
services:
  redis:
    image: redis:alpine
```

- `application.yml`에서 아래와 같이 `compose.yml` 에서 정의한 서비스명을 호스트명으로 사용할 수 있다.
  
```yaml
// application.yml
spring:
  redis:
    host: redis
    port: 6379
```


## compose 활용

1. 실시간 로그 모니터링
   - 포그라운드 모드로 실행하여 로그 실시간 확인
   - 개발 중 문제 발생 시 즉시 확인 가능

2. 코드 변경 시 빌드
   - --build 옵션으로 새로운 변경사항 반영
   - 기존 컨테이너 재사용으로 빠른 개발 사이클

3. 리소스 관리
   - 불필요한 이미지는 docker image prune으로 정리
   - 완전 초기화가 필요할 때는 docker-compose down 활용



<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>




<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
