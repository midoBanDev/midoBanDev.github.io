---
layout: single
title:  "PostgreSQL 빌드"
categories:
  - Docker
tags:
  - [Docker, PostgreSQL]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## PostgreSQL 프로젝트 관리
PostgreSQL은 데이터를 저장하고 조회할 수 있는 데이터베이스 서버이다.
Postgres는 Nginx처럼 이미지에 소프트웨어가 포함되어 있기 때문에 별도의 설정없이 Postgres 이미지만 실행해도 데이터베이스를 사용할 수 있다.

하지만 실제 운영 환경에서는 PostgreSQL 베이스 이미지만 실행해서는 애플리케이션의 운영을 원하는 수준으로 관리하기 어렵다.
간단히만 살펴보면 기본 이미지에는 아무 데이터도 없기 때문에 초기 데이터를 구성하는 SQL도 작성해야 하고, 개발, 운영 환경에 따른 설정 파일도 관리해야 된다. 
또한 운영 중 밟생하는 여러 변경에 대응해야 하는 상황도 지속적으로 발생한다. 

따라서 PostgreSQL 프로젝트를 별도로 관리하는 것이 좋다.

왜 별도의 프로젝트로 관리해야 할까?
데이터베이스는 우리 서비스의 핵심 기반이며, 특히 PostgreSQL과 같은 강력한 데이터베이스 시스템은 세심한 관리와 설정이 필요하다. 
만약 애플리케이션 코드와 함께 관리하면 다음과 같은 문제가 발생할 수 있다:
- `설정의 복잡성`: 환경별로 다른 데이터베이스 설정이 애플리케이션 코드와 뒤섞여 관리가 어려워집니다.
- `버전 관리의 어려움`: 데이터베이스 스키마 변경이나 마이그레이션 이력을 추적하기 어렵습니다.
- `보안 위험`: 데이터베이스 접근 정보가 여러 곳에 분산되어 보안 취약점이 발생할 수 있습니다.

별도의 프로젝트로 관리함으로써 얻는 이점은 다음과 같다.
- 환경별 설정 관리을 편리하게 할 수 있다.
- 데이터베이스 변경 이력의 명확한 추적할 수 있다.
- 접근 권한과 보안 설정의 중앙화된 관리를 할 수 있다.
- 백업 및 복구를 용이하게 해준다.

프로젝트 구조는 다음과 같다. 
프로젝트명 `postgres-infra` 를 만든 후 아래와 같은 구조를 가지는 프로젝트를 만든다.
이를 `github`, `gitlab` 등의 버전 관리 시스템을 통해 관리한다.
```Plaintext
postgres-infra/
├── Dockerfile.dev
├── Dockerfile.prod
├── config/
│   ├── custom.dev.conf
│   ├── custom.prod.conf
│   └── conf.d/
│       ├── logging.conf
│       ├── performance.conf
│       └── security.conf
├── init/
│   └── init.sql
├── docker-compose.dev.yml
├── docker-compose.prod.yml
└── README.md
```

주요 구성 요소 설명
1. Dockerfile 분리 (dev/prod)
- 개발 환경과 운영 환경의 요구사항이 다르기 때문에 Dockerfile을 분리한다. 
- 개발 환경에서는 디버깅 도구나 추가 확장 기능을 포함할 수 있고, 운영 환경에서는 보안과 성능에 초점을 맞출 수 있다.

2. 설정 파일 구조 (config/)
- custom.dev.conf: 개발 환경 특화 설정
- custom.prod.conf: 운영 환경 특화 설정
- conf.d/: 목적별로 분리된 설정 파일들
  - logging.conf: 로깅 관련 설정
  - performance.conf: 성능 튜닝 설정
  - security.conf: 보안 관련 설정

3. 초기화 스크립트 (init/)
- 데이터베이스 초기 설정, 기본 스키마 생성, 필수 데이터 입력 등을 관리한다.


### 실제 사용 예시

**1. 환경별 다른 Dockerfile 사용**

```dockerfile
Dockerfile.dev
Dockerfile.prod
```
<br>

**2. 빌드 인자(ARG)를 사용하여 하나의 Dockerfile로 관리**  
```dockerfile
ARG ENV=dev
COPY ./config/custom.${ENV}.conf /etc/postgresql/custom.conf
```

**빌드 시:**
```bash
# 개발 환경용
docker build --build-arg ENV=dev -t mypostgres:dev .

# 운영 환경용
docker build --build-arg ENV=prod -t mypostgres:prod .
```
<br>

**3. 환경변수(ENV)로 실행 시점에 설정**  
```dockerfile
ENV CONFIG_FILE=/etc/postgresql/custom.conf
COPY ./config/custom.*.conf /etc/postgresql/
CMD ["sh", "-c", "postgres -c config_file=${CONFIG_FILE}"]
```

**실행 시:**  
```bash
# 개발 환경
docker run -e CONFIG_FILE=/etc/postgresql/custom.dev.conf ...

# 운영 환경
docker run -e CONFIG_FILE=/etc/postgresql/custom.prod.conf ...
```

보통은 2번이나 3번 방식을 더 선호한다. 
하나의 Dockerfile로 관리하는 것이 유지보수에 더 유리하기 때문이다.



<br>

## DB 컨테이너 구성 - PostgreSQL
우선 베이스 이미지를 Postgres 13 버전으로 사용한다.
이 베이스 이미지 안에는 기본 OS 파일시스템 위에 PostgreSQL DB를 설치한 상태로 저장되어 있기 때문에 바로 사용할 수 있다.

다음으로 서버 설정을 변경한다. 커스텀한 설정 파일 `postgresql.conf`를 빌드 컨텍스트에 만든 후 Postgres 컨테이너 내부의 `/etc/postgresql/custom.conf` 에 덮어쓰기를 한다.

다음르로 DB 서버의 초기 데이터를 세팅해야 한다. 
내가 필요한 테이블을 생성하고 데이터를 삽입하는 SQL 파일을 빌드 컨텍스트에 만든 후 Postgres 컨테이너 내부의 `docker-entrypoint-initdb.d` 폴더에 넣는다. 
그러면 Postgres 이미지가 컨테이너로 실행될 때 자동으로 이 폴더 안에 있는 SQL문을 실행해 준다. 

마지막으로 컨테이너를 실행할 때의 Postgres를 실행시키기 위해 실행 명령은 `cmd ["Postgres","-c","config_file=/etc/posdtgresql/custom.conf"]`로 설정한다.
<img src="https://github.com/user-attachments/assets/e788099a-6680-4766-9807-ffc47a938274" width="80%" height="80%"/>

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## PostgreSQL Docker 이미지 빌드 설정 가이드

### 1. 기본 Dockerfile 구조
```dockerfile
# Base 이미지 선택
FROM postgres:13

# 필수 환경 변수 설정
ENV POSTGRES_USER=myuser
ENV POSTGRES_PASSWORD=mypassword
ENV POSTGRES_DB=mydb

# 타임존 설정 (선택사항)
ENV TZ=Asia/Seoul

# 포트 설정
EXPOSE 5432
```
PostgreSQL의 공식 이미지를 선택할 때는 버전 선택이 중요합니다.  
일반적으로 최신 버전(latest)보다는 특정 버전을 지정하는 것이 안정성 측면에서 좋습니다.   

`POSTGRES_USER`
- 데이터베이스 superuser 계정명 지정한다. 
- 지정하지 않으면 기본값 'postgres' 사용자가 생성된다.
- 이 계정은 모든 데이터베이스 관리 권한을 가진다.

`POSTGRES_PASSWORD`
- superuser 계정의 비밀번호
- 보안을 위해 반드시 설정해야 함
- 환경 변수나 시크릿으로 관리하는 것이 좋음

`POSTGRES_DB`
- 초기에 생성될 기본 데이터베이스 이름
- 지정하지 않으면 POSTGRES_USER값과 동일한 이름의 DB 생성

`타임존 설정`
- TZ 환경변수로 컨테이너의 시간대 설정
- 로그 기록이나 시간 관련 작업시 중요
- Asia/Seoul과 같이 지역 기반으로 설정


<details>
<summary> 
<b><span>PostgreSQL 초기화 시 ENV 설정</span></b>
</summary>

<div markdown="1">

**환경 변수 설정**  

환경 변수는 컨테이너 초기화 시 매우 중요한 역할을 합니다.  
컨테이너 실행 시 PostgreSQL을 처음 초기화할 때 `ENV`로 지정한 `POSTGRES_USER`, `POSTGRES_PASSWORD`,`POSTGRES_DB` 환경변수를 읽어서 DB와 유저를 생성합니다.
아래 경우를 예로 postgreSQL이 어떻게 초기화 되는지 살펴봅시다.

**1. `POSTGRES_PASSWORD` 만 설정**  
```dockerfile
# POSTGRES_PASSWORD는 필수
ENV POSTGRES_PASSWORD=mypassword 
```
- `postgres` 데이터베이스(DB)가 생성됩니다.(무조건 생성)
- default `postgres` 유저가 생성 됩니다.
- postgres 데이터베이스의 `Owner`는 `postgres` 유저가 됩니다. 
- `postgres` 유저의 비밀번호는 `POSTGRES_PASSWORD`에 입력된 `mypassword`로 설정되고, 이 비밀번호는 postgreSQL의 마스터 비밀번호 입니다.

<br>

**2. `POSTGRES_USER`, `POSTGRES_PASSWORD` 설정**  
```dockerfile
ENV POSTGRES_USER=myuser
# POSTGRES_PASSWORD는 필수
ENV POSTGRES_PASSWORD=mypassword
```
- `postgres` 데이터베이스가 생성됩니다.(무조건 생성)
- default `postgres` 유저 대신 `POSTGRES_USER`에 입력된 `myuser`가 생성됩니다. (postgres 유저 생성 안됨)
- `POSTGRES_USER`를 설정하고 `POSTGRES_DB`에 대한 설정이 없으면 `POSTGRES_USER`에 입력된 유저명으로 `myuser` 데이터베이스(DB)가 자동 생성됩니다.
- postgres 데이터베이스의 `Owner`는 `myuser`가 됩니다.
- myuser 데이터베이스의 `Owner`도 `myuser`가 됩니다.
- `myuser`의 비밀번호는 `POSTGRES_PASSWORD`에 입력된 `mypassword`로 설정되고, 이 비밀번호는 postgreSQL의 마스터 비밀번호인 동시에 myuser의 비밀번호가 됩니다.

<br>

**3. `POSTGRES_USER`, `POSTGRES_PASSWORD`,`POSTGRES_DB` 모두 설정**  
```dockerfile
ENV POSTGRES_USER=myuser
# POSTGRES_PASSWORD는 필수
ENV POSTGRES_PASSWORD=mypassword
ENV POSTGRES_DB=mydb
```
- `postgres` 데이터베이스가 생성됩니다.(무조건 생성)
- default `postgres` 유저 대신 `POSTGRES_USER`에 입력된 `myuser` 가 생성됩니다. (postgres 유저 생성 안됨)
- `POSTGRES_DB`에 입력된 `mydb` 데이터베이스(DB)도 생성됩니다.
- postgres 데이터베이스의 `Owner`는 `myuser`가 됩니다.
- mydb 데이터베이스의 `Owner`도 `myuser`가 됩니다.
- `myuser`의 비밀번호는 `POSTGRES_PASSWORD`에 입력된 `mypassword`로 설정되고, 이 비밀번호는 postgreSQL의 마스터 비밀번호인 동시에 myuser의 비밀번호가 됩니다.


PostgreSQL을 처음 초기화하는 시점에는 `POSTGRES_PASSWORD` 를 필수로 지정해야 postgreSQL이 실행이 된다.(설정 안하면 실행 안됨)  
`postgres DB` 와 `Owner` 는 `postgres`로 자동 생성 되지만 비밀번호는 사용자가 지정해야 한다.
`POSTGRES_USER` 생성된 계정은 관리자 계정의 권한을 가집니다.


관리자 계정으로 접속하여 다음과 같은 작업을 수행할 수 있다.
- 새로운 데이터베이스나 유저를 생성할 때
- 데이터베이스 백업이나 복원할 때
- DB 설정을 변경할 때
- 다른 어플리케이션에서 DB 관리자 권한으로 접속할 때

</div>
</details>


<br>

**1.1. 컨테이너 밖에서 psql에 접속하는 방법**
  
```bash
# 기본적인 접속 형식
psql -h localhost -p 5432 -U myuser -d mydb

# 또는 연결 문자열(URL) 사용
psql "postgresql://myuser@localhost:5432/mydb"
```
- `-h` : 호스트 지정(도커 실행 IP 주소) 
- `-p` : 포트 지정(컨테이너 공개 포트)

<br>

**1.2. 컨테이너 안에서 접속하는 방법**
```bash
# 컨테이너에 접속
docker exec -it [컨테이너이름] bash

# 컨테이너 내부에서 psql 접속
psql -U myuser -d mydb

# 또는 한번에 실행도 가능
docker exec -it [컨테이너이름] psql -U myuser -d mydb
```

<br>

### 2. 초기화 스크립트 설정
#### 2.1 디렉토리 구조
```plaintext
.
├── Dockerfile
├── init/
│   ├── 01-init.sql      # 데이터베이스 및 테이블 생성
│   ├── 02-functions.sql # 함수 생성
│   └── 03-data.sql      # 초기 데이터 삽입
└── config/
    └── postgresql.conf   # PostgreSQL 설정 파일
```
<br>

#### 2.2 초기화 스크립트 복사
```dockerfile
# 실행 순서대로 번호를 붙여 복사
COPY ./init/01-init.sql /docker-entrypoint-initdb.d/
COPY ./init/02-functions.sql /docker-entrypoint-initdb.d/
COPY ./init/03-data.sql /docker-entrypoint-initdb.d/
```

<details>
<summary> 
<b><span>초기화 스크립트 상세 설명</span></b>
</summary>

<div markdown="1">


초기화 스크립트는 /docker-entrypoint-initdb.d/ 디렉토리에 위치해야 하며, 알파벳 순서로 실행됩니다.
번호를 붙이면 순서를 제어할 수 있다.

**01-init.sql**
- 데이터베이스 구조 생성
- 테이블, 스키마 정의
- 인덱스 생성
- 실행 순서가 가장 먼저여야 함


**02-functions.sql**
- 저장 프로시저
- 사용자 정의 함수
- 트리거
- 테이블 생성 후 실행되어야 하므로 두 번째 순서


**03-data.sql**
- 초기 데이터 삽입
- 마스터 데이터 설정
- 테스트 데이터 (개발 환경의 경우)
- 구조와 함수 생성 후 마지막으로 실행


**초기화 스크립트 실행 메커니즘**
- 컨테이너 최초 실행 시에만 동작
- `/var/lib/postgresql/data`가 비어있을 때만 실행
- .sql, .sql.gz, .sh 파일 지원
- 실행 중 오류 발생 시 컨테이너 시작이 중단됨
</div>
</details>


<br>

#### 2.3 스크립트 수동으로 실행
- `cp` 명령어로 컨테이너 외부에서 컨테이너 내부로 파일 복사할 수 있다.
- 나는 윈도우 환경에서 `gitbash`를 사용하는 경우 `<컨테이너명>:` 뒤에 `/` 슬래시를 붙이면 컨테이너명을 찾지 못한다는 에러가 나왔다.
- 명령어 자체를 잘못 인식하는 것 같다. 하지만 다른 환경에서는 정상 작동할 수 있다.
- 원인으로는 이렇단다.
>Git Bash가 POSIX 스타일 경로를 Windows 경로로 자동 변환하려고 시도한다. 슬래시를 제거함으로써 이러한 자동 변환을 피할 수 있다.

- 슬러시 없이 루트 경로부터 작성해주면 되었다.
```bash
# docker cp <빌드컨텍스트파일> <container_id>:<컨테이너내부파일>
$ docker cp ./new_script.sql my-container:tmp/new_script.sql
```

- 컨테이너 외부에서 파일 실행
```bash
# docker exec -it <컨테이너명/ID> psql -U <DB유저명> -d <DB명> -f <경로포함파일>
$ docker exec -it my-container psql -U myuser -d mydb -f /tmp/new_script.sql
```

- 컨테이너 내부에서 파일 실행
```bash
# 컨테이너 접속
$ docker exec -it <컨테이너명/ID> /bin/bash
--
# 원하는 파일 실행
# psql -U <DB유저명> -d <DB명> -f /파일경로/파일
$ psql -U myuser -d mydb -f /docker-entrypoint-initdb.d/init.sql
```


<br>

### 3. PostgreSQL 설정
#### 3.1 postgresql.conf 설정
```dockerfile
# 설정 파일 복사
COPY ./config/postgresql.conf /etc/postgresql/custom.conf

# 설정 파일 적용
CMD ["postgres", "-c", "config_file=/etc/postgresql/custom.conf"]
```

<details>
<summary> 
<b><span>conf 파일 관리 운영 팁팁</span></b>
</summary>

<div markdown="1">
실제 운영환경에서는 PostgreSQL의 설정을 커스터마이징하는 것이 일반적이다. 
`/etc/postgresql` 아래에 커스텀 설정 파일을 두는 것은 리눅스 시스템의 관례를 따르는 것이며, 
PostgreSQL에서도 권장하는 방식이다.

1. postgreSQL의 기본 설정 파일은 변경하지 않는다.
```Plaintext
/var/lib/postgresql/data/postgresql.conf  # 기본 설정 파일 위치
```

2. 환경별 conf 파일 분리
```Plaintext
/etc/postgresql/
├── custom.conf          # 기본 커스텀 설정
├── custom.dev.conf      # 개발 환경 설정
├── custom.prod.conf     # 운영 환경 설정
└── conf.d/             # 모듈화된 설정들
    ├── logging.conf
    ├── performance.conf
    └── security.conf
```

프로젝트에서 Dockerfile  빌드 컨텍스트의 

</div>
</details>

<br>

#### 3.2 주요 postgresql.conf 설정 옵션
```plaintext
# 메모리 설정
shared_buffers = '256MB'           # 공유 메모리 버퍼 크기
work_mem = '16MB'                  # 작업 메모리: 개별 연결해서 사용 가능한 메모리 양
maintenance_work_mem = '64MB'      # 유지보수 작업 메모리
effective_cache_size = 2GB         # 임시 파일 및 인덱스 생성 시 사용할 메모리 크기

# 연결 설정
max_connections = 100              # 최대 연결 수: 동시 접속자 수 제한
listen_addresses = '*'             # IP 주소, 호스트명 또는 '*'로 모든 IP에 대한 연결을 허용합니다.
authentication_timeout = 5min      # 인증 시간 초과 시간 (5분)
password_encryption = md5          # 패스워드 암호화 방식

# WAL 설정
wal_level = 'replica'              # WAL 레벨: 스트리밍 복제 구성
max_wal_senders = 5                # 스트리밍 복제 전송자의 최대 수
max_wal_size = '1GB'               # 최대 WAL 크기
min_wal_size = '80MB'              # 최소 WAL 크기

# 로깅 설정
log_destination = 'stderr'         # 로그 출력 위치
logging_collector = on             # 로그 수집기 활성화
log_directory = 'log'              # 로그 파일이 저장될 디렉토리 경로
log_filename = 'postgresql-%Y-%m-%d_%H%M%S.log'  # 로그 파일명 형식 지정

# PERFORMANCE
effective_io_concurrency = 200     # 파일 I/O 수행을 위해 사용할 동시성 레벨
random_page_cost = 1.1             # 임의 액세스 비용 인덱스 스캔시 고려
```

<details>
<summary> 
<b><span>postgresql.conf 주요 설정 항목 설명</span></b>
</summary>

<div markdown="1">

**메모리 관련 설정**  

`shared_buffers`: PostgreSQL이 공유 메모리로 사용할 크기
- 일반적으로 총 RAM의 25% 정도 할당
- 너무 크게 설정하면 시스템 성능 저하 가능

`work_mem`: 정렬 작업에 사용되는 메모리
- 복잡한 쿼리나 정렬이 많은 경우 증가
- connection 수를 고려하여 설정

`maintenance_work_mem`: 관리 작업용 메모리
- VACUUM, CREATE INDEX 등에 사용
- 주기적 유지보수 작업 시 중요

<br>

**연결 설정**

`max_connections`: 동시 접속 가능한 최대 연결 수
- 서버 리소스를 고려하여 설정
- 너무 높게 설정하면 메모리 부족 발생 가능


`listen_addresses`: 접속 허용 IP 주소
- '*'는 모든 IP 허용
- 보안상 필요한 IP만 허용하는 것이 좋음

<br>

**WAL(Write-Ahead Logging) 설정**  

WAL은 데이터베이스의 복구와 복제에 중요한 역할을 합니다:

`wal_level`
- minimal: 기본 복구에 필요한 최소한의 로깅
- replica: 복제에 필요한 정보 포함
- logical: 논리적 복제 지원

`max_wal_size`
- WAL 파일의 최대 크기
- 디스크 공간과 체크포인트 빈도 고려

`min_wal_size`
- WAL 파일 유지의 최소 크기
- 너무 작게 설정하면 잦은 WAL 파일 생성

</div>
</details>

<br>

#### 3.3 컨테이너와 호스트 간 파일 복사 : `cp`
호스트 파일을 `cp` 명령어를 통해 컨테이너 내부로 복사할 수 있다. 
또한 컨테이너 내부 파일을 호스트로 복사할 수 있다.

- 나는 윈도우 환경에서 `gitbash`를 사용하는 경우 `<컨테이너명>:` 뒤에 `/` 슬래시를 붙이면 컨테이너명을 찾지 못한다는 에러가 나왔다.
- 명령어 자체를 잘못 인식하는 것 같다. 하지만 **다른 환경에서는 슬러시가 있어야 정상 작동할 수 있다.**
- 원인으로는 이렇단다.
>Git Bash가 POSIX 스타일 경로를 Windows 경로로 자동 변환하려고 시도한다. 슬래시를 제거함으로써 이러한 자동 변환을 피할 수 있다.

- 슬러시 없이 루트 경로부터 작성해주면 되었다.


- 호스트 -> 컨테이너: `docker cp 원본위치 컨테이너명:복사위치`

```bash
# 현재 디렉토리 기준 복사
$ docker cp ./config/postgresql.conf my-postgresql:etc/postgresql/custom.conf

# 별도의 경로는 루트부터 경로 지정
$ docker cp loan-api:app/loan-manager-api.jar /C/Users/wglee/Downloads/
```

<br>

- 컨테이너 -> 호스트: `docker cp 컨테이너명:복사위치 원본위치` 

```bash
# 현재 디렉토리 기준으로 복사
$ docker cp my-postgresql:etc/postgresql/custom.conf ./config/postgresql.conf

# 별도의 경로는 루트부터 경로 지정
$ docker cp loan-api:app/loan-manager-api.jar /C/Users/wglee/Downloads/
```

<br>

### 4. 데이터 영구 저장을 위한 볼륨 설정
```dockerfile
# 데이터 디렉토리 볼륨 설정
VOLUME ["/var/lib/postgresql/data"]
```

<br><br>

### 5. 보안 설정
#### 5.1 pg_hba.conf 설정
```dockerfile
# pg_hba.conf 파일 복사
COPY ./config/pg_hba.conf /etc/postgresql/pg_hba.conf

# 설정 적용
CMD ["postgres", "-c", "config_file=/etc/postgresql/custom.conf", "-c", "hba_file=/etc/postgresql/pg_hba.conf"]
```

<br>

#### 5.2 pg_hba.conf 예시
```plaintext
# TYPE  DATABASE        USER            ADDRESS                 METHOD
local   all            all                                     trust
host    all            all             127.0.0.1/32            md5
host    all            all             ::1/128                 md5
host    all            all             0.0.0.0/0               md5
```

<br>

### 6. 성능 최적화 설정
```plaintext
# postgresql.conf 성능 관련 설정
effective_cache_size = '1GB'        # 시스템 캐시 크기
random_page_cost = 1.1             # SSD 사용시 권장값
effective_io_concurrency = 200     # SSD 사용시 권장값
checkpoint_completion_target = 0.9  # 체크포인트 완료 목표
```

<br>

### 7. 전체 Dockerfile 예시
```dockerfile
FROM postgres:13

# 환경 변수 설정
ENV POSTGRES_USER=myuser
ENV POSTGRES_PASSWORD=mypassword
ENV POSTGRES_DB=mydb
ENV TZ=Asia/Seoul

# 설정 파일 복사
COPY ./config/postgresql.conf /etc/postgresql/custom.conf
COPY ./config/pg_hba.conf /etc/postgresql/pg_hba.conf

# 초기화 스크립트 복사
COPY ./init/01-init.sql /docker-entrypoint-initdb.d/
COPY ./init/02-functions.sql /docker-entrypoint-initdb.d/
COPY ./init/03-data.sql /docker-entrypoint-initdb.d/

# 볼륨 설정
VOLUME ["/var/lib/postgresql/data"]

# 포트 설정
EXPOSE 5432

# 설정 파일 적용
CMD ["postgres", "-c", "config_file=/etc/postgresql/custom.conf", "-c", "hba_file=/etc/postgresql/pg_hba.conf"]
```

<br>

### 8. 주의사항
1. 초기화 스크립트(.sql)는 알파벳 순서로 실행되므로 번호를 붙여 순서 제어
2. 환경 변수는 반드시 POSTGRES_PASSWORD 또는 POSTGRES_HOST_AUTH_METHOD 중 하나를 설정
3. 볼륨 설정 시 호스트의 적절한 권한 설정 필요
4. production 환경에서는 보안을 위해 적절한 pg_hba.conf 설정 필수
5. postgresql.conf 설정은 서버 사양에 맞게 조정 필요

<br>

### 9. 빌드 및 실행 예시
- `-p` 옵션을 사용하지 않으면 외부에서 접근하지 못한다. (커테이너 내부 포트는 기본 5432 설정됨)
- 단 같은 네트워크에서는 접근이 가능하다. 

```bash
# 이미지 빌드
docker build -t my-postgres .

# 컨테이너 실행
docker run -d \
  --name my-postgres \
  -p 5432:5432 \
  -v postgres_data:/var/lib/postgresql/data \
  my-postgres
```


<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<!--<img src="" width="80%" height="80%"/>-->

