---
layout: single
title:  "[Docker] 도커 컴포즈 - Docker Compose"
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

<br>

>### 도커 컴포즈 프로젝트(Docker Compose Project)

도커 컴포즈에서 서비스들은 프로젝트로 논리적인 그룹을 만든다. 
프로젝트는 Compose 파일이 속한 `디렉토리명`으로 생성된다. 
서로 다른 프로젝트명을 가진 서비스들은 각각 독립된 환경을 구성한다.

즉, 동일한 compose.xml 파일을 실행해도 프로젝트명을 변경하면 독립된 환경으로 구성된다. 
`-p` 옵션을 사용하여 프로젝트명을 지정할 수 있다. 이를 활용하여 로컬, 개발, 운영 등의 환경별 구성을 설정할 수 있다.  


**프로젝트의 역할**  
- 컨테이너 이름의 prefix로 사용됨
  - `{프로젝트명}_{서비스명}_{번호}`

- 네트워크 이름의 prefix로 사용됨
  - `{프로젝트명}_default`

- 볼륨 이름의 prefix로 사용됨
  - `{프로젝트명}_volume명`


```bash
# 로컬 환경
$ docker compose -p local up -d
[+] Running 2/2
 ✔ Network local_default    Created
 ✔ Container local-redis-1  Started  


# 개발 환경
$ docker compose -p dev up -d
[+] Running 2/2
 ✔ Network dev_default    Created
 ✔ Container dev-redis-1  Started 

# 운영 환경
$ docker compose -p prod up -d
[+] Running 2/2
 ✔ Network prod_default    Created
 ✔ Container prod-redis-1  Started 
```

프로젝트명을 변경하여 서버를 실행한 경우 삭제 시 해당 프로젝트명을 그대로 지정해야 한다.  
```bash
# 로컬 환경
$ docker compose -p fonrt-local down

# 개발 환경
$ docker compose -p fonrt-dev down

# 운영 환경
$ docker compose -p fonrt-prod down
```


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## Docker Compose 파일
Docker Compose 파일은 yaml 포맷을 사용한다. 
`YAML(YAML Ain't Markup Language)`은 "YAML은 마크업 언어가 아니다"라는 의미로 HTML과 XML 같은 마크업 언어가 아니라 데이터를 간단하게 표현하는 데에 중점을 둔 형식을 말한다.
yaml의 기본은 들여쓰기와 키-값 쌍으로 이루어져 있다.

**기본적인 docker-compose.yml 예시:** 

```yaml
version: '3'
services:
  web:
    build: ./app
    image: web-front:1.0.0
    ports:
      - "80:80"
  api:
    build: .
    image: "api-server:1.0"
    ports:
      - 8080:8080
```
- version: Docker Compose 파일 형식의 버전을 지정한다.
- services: 실행할 서비스들을 정의한다.
- build: Dockerfile이 있는 경로를 지정한다.
- image: 빌드될 이미지와 태그명 또는 생성할 이미지 이름과 태그명을 지정한다.
- ports: 포트 매핑 (호스트:컨테이너)

<br>

> ### build와 image

**image만 있는 경우:** 

```yaml
services:
  redis:
    image: "redis:alpine"
```
지정된 이미지(redis:alpine)를 직접 사용하여 컨테이너를 실행한다. 
로컬에 이미지가 없으면 자동으로 레지스트리에서 pull한 후 사용한다.

<br>

**build와 image가 같이 있는 경우:**  

```yaml
services:
  hitchecker:
    build: ./app
    image: hitchecker:1.0.0
```
build에 지정된 경로(./app)의 Dockerfile을 사용해서 image에 명시한 이름과 태그 명으로(hitchecker:1.0.0) 이미지를 생성한다. 

<br>

> ### 환경변수 설정

- `build: args:` 빌드 시점에 필요한 환경 변수를 주입할 수 있다.
- `environment`와 `env_file` 옵션은 컨테이너 런타임 시점에 환경변수를 설정할 수 있다. 

<br>

**`build: args:` 설정 방법**
 
Docker Compose의 `up -d --build` 명령어의 실행 과정을 보고 빌드 시점에 환경 변수가 어떻게 적용되는지 알아보자.

1. docker-compose.yml 파일과 .env 파일을 먼저 읽는다
- 이 때 docker-compose.yml의 변수들(${...})을 .env 파일의 값으로 대체한다.


2. 빌드 컨텍스트 준비
- build 지시어가 있는 서비스들에 대해 빌드 컨텍스트를 준비한다.
- `args`에 지정된 값들이 이 시점에서 결정된다.


3. Dockerfile을 사용한 이미지 빌드
- Dockerfile의 ARG는 compose의 build.args에서 전달된 값을 받는다.
- 이미지 빌드 과정이 진행된다.


4. 컨테이너 생성 및 실행
- 빌드된 이미지로 컨테이너를 생성한다.
- `environment`, `env_file`, `secrets` 등 런타임 설정이 적용된다.



예를 들어:
```yaml
# docker-compose.yml
services:
  app:
    build:
      context: .
      args:
        - BUILD_TIME_VAR=${ENV_FILE_VAR}  # .env에서 값을 가져옴
```

```plaintext
# .env
ENV_FILE_VAR=some_value
```

```dockerfile
# Dockerfile
ARG BUILD_TIME_VAR
ENV BUILD_TIME_VAR=$BUILD_TIME_VAR

RUN echo "Building with: $BUILD_TIME_VAR"
```
이 경우:

1. .env 파일의 ENV_FILE_VAR 값을 읽음
2. compose의 BUILD_TIME_VAR arg에 그 값을 전달
3. Dockerfile의 ARG가 그 값을 받아 빌드에 사용


<br>


**`environment` 설정 방법**  
- `environment`는 compose.yml 파일 내에 `키-값` 형태로 직접 작성한다. 
- 변수 값 셋팅은 `key=value` 형태로 작성해야 한다.
- environment 컨테이너 런타임에 주입되는 환경 변수다. 따라서 빌드 시점에 필요한 환경 변수는 `build: args:` 을 사용해야 한다.

**1. 직접 환경 변수 정의** 
```yaml
services:
  web-app:
    image: my-web-app:1.0
    environment:
      - DATABASE_HOST=db
      - DATABASE_PORT=5432
      - API_KEY=development_key
```

**2. 쉘 환경변수 사용**  
```yaml
version: '3'
services:
  web:
    image: busybox
    environment:
      - DEBUG=1
      - API_KEY=${API_KEY}
    # 환경 변수 출력 명령  
    command: "sh -c 'echo API API_KEY: $API_KEY && sleep 3600'"

  api:
    image: busybox
    environment:
      - DEBUG=1
      - API_KEY=${API_KEY}
    # 환경 변수 출력 명령
    command: "sh -c 'echo API API_KEY: $API_KEY && sleep 3600'"

```
- `DEBUG=1` 처럼 값을 직접 지정할 수도 있고, `API_KEY=${API_KEY}`처럼 인수 값을 전달 받도록 설정할 수도 있다. 

**3. 인수 값 전달 및 실행**  

```bash
$ API_KEY=your-secret-key docker compose up
```

<br>

**`env_file` 설정 방법**   

`env_file` 방식을 컨테이너 런타임 시 환경 변수를 주입할 수 있다. 
만약 빌드 시점에 필요한 환경 변수는 env_file 방식 대신 `build: args:` 을 사용해야 한다.

- 기본 파일을 사용하는 경우 : `.env`
  - `.env` 파일은 Docker Compose 실행 시 자동으로 로드되어 compose.yml에 적용된다.

  ```plaintext
  # .env 파일
  API_KEY=default-key
  ```
  ```bash
  # 실행
  $ docker compose up
  ```

<br>

- 별도의 파일명을 사용하는 경우 : `test.env`
  
  ```plaintext
  # test.env 파일
  API_KEY=default-key
  ```
  - compose 실행 시 `--env-file` 옵션으로 파일명을 지정해줘야 한다.

  ```bash
  # 실행
  $ docker compose --env-file test.env up
  ```

<br>

**--env-file 옵션 없이 특정 파일 지정 방법**  
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


> ### 시크릿(secret)

Docker Secret의 핵심 목적은 **민감한 설정 값을 컨테이너에 안전하게 전달**하는 것이다.
애플리케이션에서 주로 사용하는 `비밀번호`, `API 키`, `SSL 인증서`, `JWT 토큰`, `기타 민감한 설정 파일` 등을 도커 파일이나 compose.xml 파일에 직접 명시하면 위험하다. 
이때 `secret`을 사용하면 좀 더 안전하게 환경을 구성할 수 있다.

- secret은 암호화된 상태로 전송되며, 호스트 디스크에 기록되지 않고 메모리에만 저장된다. 
- docker inspect 명령어로도 값을 확인할 수 없다. 
- 또한 권한이 있는 컨테이너에만 선택적으로 전달도 가능하다.
- 단 Docker secrets는 컨테이너 런타임에 마운트된다. 따라서 빌드 시점에 사용해야 하는 환경 변수는 secrets로 사용할 수 없다.

**일반 환경변수와의 차이점** 
```yaml
# 일반 환경변수 (비추천)
services:
  app:
    environment:
      - DB_PASSWORD=very_secret_password    # 값이 노출됨

# Secret 사용 (추천)
services:
  app:
    secrets:
      - db_password    # 값이 암호화되어 전달됨
```

<br>

**Secret 사용 방법** 

**1. 기본 설정**
```yaml
version: '3.8'

secrets:
  # Secret 정의
  db_password:
    file: ./secrets/db_password.txt    # 실제 값이 저장된 파일

services:
  app:
    image: myapp
    secrets:
      # Secret을 컨테이너에 마운트
      - source: db_password
        target: /run/secrets/db_password    # 컨테이너 내부 경로
```

**2. Secret 값 사용 방법** 

**파일에서 직접 읽기**  
```java
import java.nio.file.Files;
import java.nio.file.Paths;

public class SecretReader {
    public String getDbPassword() {
        try {
            // Docker secrets are mounted at /run/secrets/<secret_name>
            return Files.readString(Paths.get("/run/secrets/db_password")).trim();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read DB password secret", e);
        }
    }
}
```

**환경변수로 변환하여 사용**  
```yaml
services:
  app:
    environment:
      # 환경변수로 Secret 파일 경로 전달
      - DB_PASSWORD_FILE=/run/secrets/db_password
```

- java
  
```java
@Value("${GOOGLE_CLIENT_ID}")  // 환경변수로 파일 경로를 받아옴
private String clientIdPath;

// 해당 경로의 파일 내용을 읽어서 사용
String clientId = new String(Files.readAllBytes(Paths.get(clientIdPath)));
```  

- node.js
  
```javascript
// Node.js에서 환경변수로 변환하여 사용
const fs = require('fs');
process.env.DB_PASSWORD = fs.readFileSync(
    process.env.DB_PASSWORD_FILE, 
    'utf8'
).trim();
```

**3. 실전 예시**  

**데이터베이스 설정**  
```yaml
version: '3.8'

secrets:
  # DB 관련 Secret 정의
  db_config:
    file: ./secrets/db.json
  db_password:
    file: ./secrets/password.txt

services:
  # 애플리케이션 서비스
  app:
    image: myapp
    secrets:
      - source: db_config
        target: /app/config/db.json
      - source: db_password
        target: /run/secrets/db_password
    environment:
      - DB_CONFIG_FILE=/app/config/db.json
      - DB_PASSWORD_FILE=/run/secrets/db_password

  # 데이터베이스 서비스
  db:
    image: postgres
    secrets:
      - source: db_password
        target: /run/secrets/db_password
    environment:
      - POSTGRES_PASSWORD_FILE=/run/secrets/db_password
```

**API 인증 설정**  
```yaml
secrets:
  api_keys:
    file: ./secrets/api_keys.json
  ssl_cert:
    file: ./secrets/cert.pem

services:
  api:
    image: api-server
    secrets:
      - source: api_keys
        target: /app/config/api_keys.json
      - source: ssl_cert
        target: /etc/ssl/cert.pem
```

Secret 파일은 `.gitignore`에 포함시키는 것이 좋다. 
운영 환경의 Secret 관리는 `AWS Secrets Manager` 등의 외부 시스템에서 관리하는 것이 좋다. 
컨테이너 내부에서 Secret 값을 로그로 출력하지 않아야 한다. 


<br>


> ### 확장 필드(Extended Field) 사용
YAML 형식을 따르는 Docker Compose 파일은 앵커(Anchor) 문법을 사용하여 중복되는 내용을 줄일 수 있다. 
도커에서는 확장 필드 기능에 앵커 문법을 활용한다.
- 확장 필드는 보통 `x-`로 시작하는 키 아래에 정의한다. 
- `&Anchor`를 사용해 필드를 정의하고, 필요한 곳에서 `*`를 사용해 `&에 정의한 필드명`을 재사용하면 된다.
- `x-` 와 `&`로 정의되는 필드명은 자유롭게 명명할 수 있다.


**환경 변수 값 직접 설정** 
  
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

**환경 변수 값 동적 설정** 
`${}` 안에 연동 될 변수명을 작성한다.

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



> ### 볼륨 설정

**1. 명시적으로 볼륨 선언하고 사용하기:** 
volumes 속성 아래 볼륨명을 선언할 수 있다. 
이렇게 선언된 볼륨은 named 볼륨으로 도커가 자동으로 생성한다.

```yaml
version: '3'
# 최상위 레벨에서 볼륨 정의
volumes:
  mydata:  # 볼륨 이름
    
services:
  web:
    image: nginx
    volumes:
      - mydata:/usr/share/nginx/html  # 볼륨 마운트
```

**2. 호스트 경로를 직접 마운트하기:** 
바인드 마운트를 사용하여 호스트 경로를 직접 마운트 할 수 있다.

```yaml
services:
  db:
    image: postgres
    volumes:
      - ./data:/var/lib/postgresql/data  # 호스트경로:컨테이너경로

      - /absolute/path:/container/path   # 절대 경로 사용
      - relative/path:/container/path    # 상대 경로 사용
```

**3. 볼륨 설정과 함께 사용하기:** 

```yaml
volumes:
  pgdata:
    driver: local  # 볼륨 드라이버 지정
    driver_opts:  # 드라이버 옵션
      type: none
      device: /path/on/host
      o: bind

services:
  db:
    image: postgres
    volumes:
      - pgdata:/var/lib/postgresql/data
```

**4. 읽기 전용 볼륨 설정:** 
`:ro` 는 read-only를 의미한다.

```yaml
services:
  web:
    image: nginx
    volumes:
      - ./config:/etc/nginx/conf.d:ro  # :ro는 read-only를 의미
```

<br>


> ### 네트워크

Docker Compose는 기본적으로 프로젝트별 기본 네트워크를 자동으로 생성한다. 
네트워크 이름은 `{프로젝트_디렉토리명}_default` 형식으로 생성되고, 
같은 compose 파일 내의 서비스들은 자동으로 이 네트워크에 연결된다. 
서비스들은 compose 파일의 서비스명으로 서로를 통신할 수 있다.

**네트워크 정의하기** 
최상위 계층에 `networks: frontend:`로 사용할 네트워크를 선언할 수 있다. 
선언된 네트워크는 `services` 내에서 사용되지 않으면 선언된 네트워크는 무시되며, 
도커에서 기본적으로 생성하는 `{프로젝트_디렉토리명}_default` 으로 네트워크를 생성한다. 
서비스 내에서 `web: networks: - frontend` 이와 같이 사용되어야 `frontend_default` 로 네트워크를 생성한다.


```yaml
networks:
  frontend:
  backend:

services:
  web:
    networks:
      - frontend
  api:
    networks:
      - frontend
      - backend
  db:
    networks:
      - backend
```

<br>

**네트워크 이름 지정** 

`name` 옵션으로 네트워크명을 지정할 수 있다. 
services 내에는 선언된 네트워크 명 `app-net`으로 사용한다.

```yaml
networks:
  app-net: 
    name: redis-network

services:
  redis-local:
    networks:
      - app-net  
```

**1. name을 지정한 경우** 
```yaml
networks:
  app-net:          # 네트워크 별칭
    name: mynet     # 실제 생성되는 네트워크 이름: mynet
```

**2. name을 지정하지 않은 경우** 
```yaml
networks:
  app-net:          # 네트워크 별칭
                    # 실제 생성되는 네트워크 이름: {프로젝트명}_app-net
```

<br>

**네트워크 주요 옵션**

```yaml
networks:
  mynetwork:
    driver: bridge  # 네트워크 드라이버 (bridge, host, none, overlay 등)
    driver_opts:  # 드라이버 옵션
      com.docker.network.bridge.name: "my-bridge"
    ipam:  # IP 주소 관리 설정
      driver: default
      config:
        - subnet: 172.28.0.0/16
    internal: false  # 외부 네트워크 연결 허용 여부
    attachable: true  # 외부 컨테이너의 네트워크 연결 허용
```

**기존 네트워크 사용**   
미리 만들어 놓은 네트워크를 사용할 수 있다. 다음과 같은 상황에서 사용하기 유용한 방법이다. 
- 여러 개의 독립된 Docker Compose 프로젝트가 같은 네트워크를 공유해야 할 때 사용
- 회사에서 미리 설정해둔 공통 네트워크를 사용해야 할 때
- 특별한 설정이 필요한 네트워크를 미리 구성해두고 재사용할 때

**1. 네트워크 생성**
  
```yaml
$ docker network create my-network
```

**2. compose.yml에서 사용하기**
  
```yaml
networks:
  my-network:       # 미리 생성한 네트워크 명
    external: true  # 외부에 이미 존재하는 네트워크임을 명시
```
`external: true`로 설정하면 Docker Compose는 새로운 네트워크를 만들지 않고 
시스템에 이미 존재하는 `my-network`를 찾아서 사용한다.



<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>


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
- 기존 compose.yml 파일을 수정하면 down 명령이 정상적으로 작동하지 않을 수 있다. 기존 파일을 수정해야 하는 경우 down 한 후 수정하거나, 별도의 파일에 작성해 놓은 것이 좋다.

<br>

- `logs` : compose로 실행한 모든 컨테이너 로그 확인
  
```bash
docker-compose logs
```

- `-f` : 로그 실시간 확인
  
```bash
docker-compose logs -f
```

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>


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

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## compose 파일 오버라이드(Override)
`Docker Compose의 오버라이드`는 기본 `docker-compose.yml` 파일의 설정을 다른 파일로 확장하거나 재정의하는 기능이다. 
주로 개발, 스테이징, 운영 환경별로 다른 설정이 필요할 때 사용할 수 있다.

도커의 오바라이드 기능을 사용하면 공통 설정 파일과 환경 별 파일을 구성하여 관리할 수 있다.

로컬, 개발, 운영에서 사용할 레디스 서버를 구축해 볼 예정이다.
우선 단일 파일로는 어떻게 설정할 수 있는지 살펴본 후 오버라이드 기능으로 환경 별 파일을 어떻게 구성 및 사용할 수 있는지 알아보자.

> ### 단일 파일
> 단일 파일을 사용하여 로컬, 개발, 운영 환경을 만들 수 있다.


- compose.yml
  
```yaml
# 볼륨을 환경 별로 지정한다.
volumes:
  redis-data-local:
  redis-data-dev:
  redis-data-prod:

# 네트워크를 환경 별로 지정한다.
networks:
  app-net-local:
  app-net-dev:
  app-net-prod:

services:
  redis-local:
    image: redis
    volumes:
      - redis-data-local:/data
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 64M
    restart: always
    ports:
      - 6379:6379
    networks:
      - app-net-local


  redis-dev:
    image: redis
    volumes:
      - redis-data-dev:/data
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 64M
    restart: always
    ports:
      - 6380:6379
    networks:
      - app-net-dev

  redis-prod:
    image: redis
    volumes:
      - redis-data-prod:/data
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 64M
    restart: always  
    ports:
      - 6381:6379
    networks:
      - app-net-prod      
```

**실행**  
```bash
$ docker compose up -d
```

**결과**  
<img src="https://github.com/user-attachments/assets/992db892-d39b-4270-ab9b-3ae19edebe9e" width="90%" height="80%"/>

파일을 하나로 사용하면 하나의 파일만 관리하면 되는 이점이 있지만 
자칫 실수 한번으로 모든 환경에 대한 설정을 변경할 수 있는 리스크가 있다. 
그래서 가능하면 공통 설정을 따로두고 환경별로 각각의 파일을 구성해서 관리하는 것이 오히려 큰 재앙을 막을 수 있다.

<br>

> ### 도커 컴포즈 오버라이드



<details>
<summary> 
<b><span>Docker Compose Override 규칙</span></b>
</summary>

<div markdown="1">

**1. 단일 값 필드 (Simple values):** 
- 기존 내용을 덮어쓴다.
  
```yaml
# docker-compose.yml
services:
  web:
    image: nginx:1.17
```
```yaml
# docker-compose.override.yml
services:
  web:
    image: nginx:1.18  # 덮어씁니다
```


**2. 리스트/배열 타입:** 
- 기존 내용에 추가된다.

```yaml
# docker-compose.yml
services:
  web:
    ports:
      - "8000:8000"
```

```yaml
# docker-compose.override.yml
services:
  web:
    ports:
      - "9000:9000"  # 추가됨 (병합)
```


**3. 매핑/딕셔너리 타입:** 
- 기존 내용에 추가된다.

```yaml
# docker-compose.yml
services:
  web:
    environment:
      - API_KEY=123
```

```yaml
# docker-compose.override.yml
services:
  web:
    environment:
      - DEBUG=true  # 추가됨 (병합)
```

**4. 병합을 방지하고 완전히 대체하고 싶을 경우:**
- 기존 파일을 완전히 대체한다.

```yaml
# docker-compose.override.yml
services:
  web:
    ports:
      - "9000:9000"
    deploy:
      replicas: 2
volumes:
  data:
    driver: local
      
x-docker-compose-override: true  # 이전 설정을 완전히 대체
```
오버라이드 되는 파일로 완전히 대체된다. 
</div>
</details>

<br>

- compose.yml : 공통 설정 파일

```yaml
# 여기서는 네트워크만 선언한다. 그리고 각 환경별 파일에서 네트워크 명을 지정한다.
networks:
  app-net: 

# 여기서는 볼륨만 선언한다. 그리고 각 환경별 파일에서 볼륨명을 지정한다.
volumes:
  redis-data:

services:
  redis:
    container_name: redis-server
    image: redis
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 64M
    restart: always
    # 네트워크 별칭만 사용한다.
    netwokrs:
      - app-net
```


단일 파일로 관리하는 경우 환경별 네트워크 별칭을 각가 다르게 선언해야 했지만 
파일을 분리하면 `name`으로 네트워크를 생성하여 독립된 네트워크를 구성할 수 있다.  

볼륨도 `name`으로 각 환경별 볼륨을 생성할 수 있다.

<br>

**compose-local.yml : 로컬 환경 파일**  
- 해당 파일에서 networks name과 volumes name 을 지정하면 공통 파일에서 별칭한 사용한 네트워크에 name으로 명명한 독립된 네트워크와 볼륨이 생성된다.
- 로컬 환경에서 사용할 image `redis:latest`로 변경된다.
- 로컬 환경에서 사용할 ports를 설정한다. (공통 파일에 ports 있으면 해당 포트가 추가된다. 덮어쓰기 X)
  
```yaml
networks:
  app-net: 
    name: app-net-local

volumes:
  redis-data:
    name: redis-data-local

services:
  redis:
    image: redis:latest
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 64M
    restart: always
    ports:
      - 6379:6379
```

<br>

**compose-dev.yml : 개발 환경 파일**  
- 해당 파일에서 networks name과 volumes name 을 지정하면 공통 파일에서 별칭한 사용한 네트워크에 name으로 명명한 독립된 네트워크와 볼륨이 생성된다.
- 개발 환경에 맞는 image `redis:7.4.2`로 변경한다.
- 개발 환경에 맞는 ports를 설정한다. (공통 파일에 ports 있으면 해당 포트가 추가된다. 덮어쓰기 X)
  
```yaml
networks:
  app-net: 
    name: app-net-dev

volumes:
  redis-data:
    name: redis-data-dev

services:
  redis:
    image: redis:7.4.2
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 64M
    restart: always
    ports:
      - 6380:6379
```

<br>

**compose-prod.yml : 운영 환경 파일**  
- 해당 파일에서 networks name과 volumes name 을 지정하면 공통 파일에서 별칭한 사용한 네트워크에 name으로 명명한 독립된 네트워크와 볼륨이 생성된다.
- 운영 환경에 맞는 image `redis:7.2-alpine`로 변경한다.
- 운영 환경에 맞는 ports를 설정한다. (공통 파일에 ports 있으면 해당 포트가 추가된다. 덮어쓰기 X)
  
```yaml
networks:
  app-net: 
    name: app-net-prod

volumes:
  redis-data:
    name: redis-data-prod

services:
  redis:
    image: redis:7.2-alpine
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 64M
    restart: always
    ports:
      - 6381:6379
```

<br>

>### 환경 별 컨테이너 실행

프로젝트명을 다르게 지정해야 각 환경별 컨테이너를 실행할 수 있다. 
프로젝트명을 변경해주지 않으면 마지막에 `compose up` 명령을 수행한 컨테이너만 실행된다.  
도커는 기본적으로 프로젝트명이 같으면 컨테이너명이 달라도 기존 컨테이너를 재사용하기 때문이다.  
<img src="https://github.com/user-attachments/assets/e7d72448-e2fe-40ed-b1bd-b91ee6d3c038" width="90%" height="80%"/>


- `p` 옵션을 사용하여 프로젝트명을 다르게 지정한 후 compose 실행
  
```bash
# 로컬 환경
$ docker compose -f ./compose.yml  -f ./compose-local.yml -p local-env up -d

# 개발 환경
$ docker compose -f ./compose.yml  -f ./compose-dev.yml -p dev-env up -d

# 운영 환경
$ docker compose -f ./compose.yml  -f ./compose-prod.yml -p prod-env up -d
```
<img src="https://github.com/user-attachments/assets/7de36039-97f3-47b7-8181-7ebeaaf5c303" width="90%" height="80%"/>


- 컨테이너 삭제 시 오버라이드로 생성된 `프로젝트명`과 `컨테이너명`을 찾아야 하기 때문에 기존 실행 명령을 그대로 적용해줘야 한다.

```bash
$ docker compose -f ./compose.yml  -f ./compose-local.yml -p local-env down -v

$ docker compose -f ./compose.yml  -f ./compose-dev.yml -p dev-env down -v

$ docker compose -f ./compose.yml  -f ./compose-prod.yml -p prod-env down -v
```

<br>

**네트워크와 볼륨 생성 확인** 

<img src="https://github.com/user-attachments/assets/e247677a-3761-4567-ad7a-51b27292b0f6" width="90%" height="80%"/>

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>



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



<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
