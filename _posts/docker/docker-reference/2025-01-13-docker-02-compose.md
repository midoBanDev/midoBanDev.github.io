---
layout: single
title:  "docker compose"
categories:
  - DockerDoc
tags:
  - [Docker, Debug]

toc: true
toc_sticky: true
---

설명: Docker Compose  
사용법: docker compose

## 설명
Docker를 사용하여 다중 컨테이너 애플리케이션을 정의하고 실행합니다.

## 옵션

| 옵션 | 기본값 | 설명 |
|------|--------|------|
| --all-resources | | 서비스에서 사용하지 않는 리소스를 포함한 모든 리소스 포함 |
| --ansi | auto | ANSI 제어 문자 출력 시기 제어 ("never"\|"always"\|"auto") |
| --compatibility | | 이전 버전 호환성 모드로 compose 실행 |
| --dry-run | | 드라이 런 모드로 명령 실행 |
| --env-file | | 대체 환경 파일 지정 |
| -f, --file | | Compose 구성 파일 |
| --parallel | -1 | 최대 병렬 처리 제어, -1은 무제한 |
| --profile | | 활성화할 프로필 지정 |
| --progress | auto | 진행 출력 유형 설정 (auto, tty, plain, json, quiet) |
| --project-directory | | 대체 작업 디렉토리 지정 (기본값: 첫 번째로 지정된 Compose 파일의 경로) |
| -p, --project-name | | 프로젝트 이름 |

## 예시

### Compose 파일 지정하기
`-f` 플래그를 사용하여 하나 이상의 Compose 파일의 이름과 경로를 지정할 수 있습니다.

### 여러 Compose 파일 지정하기
여러 `-f` 구성 파일을 제공할 수 있습니다. 여러 파일을 제공하면 Compose는 이들을 단일 구성으로 결합합니다. Compose는 파일을 제공한 순서대로 구성을 빌드합니다. 이후 파일은 이전 파일을 재정의하고 추가합니다.

예를 들어, 다음과 같은 명령줄을 고려해보세요:

```bash
docker compose -f docker-compose.yml -f docker-compose.admin.yml run backup_db
```

docker-compose.yml 파일에서 webapp 서비스를 지정할 수 있습니다:

```yaml
services:
  webapp:
    image: examples/web
    ports:
      - "8000:8000"
    volumes:
      - "/data"
```

docker-compose.admin.yml에서도 동일한 서비스를 지정하면, 일치하는 필드는 이전 파일을 재정의합니다. 새로운 값은 webapp 서비스 구성에 추가됩니다:

```yaml
services:
  webapp:
    build: .
    environment:
      - DEBUG=1
```

여러 Compose 파일을 사용할 때, 파일의 모든 경로는 `-f`로 지정된 첫 번째 구성 파일을 기준으로 합니다. `--project-directory` 옵션을 사용하여 이 기본 경로를 재정의할 수 있습니다.

`-f`와 함께 `-`(대시)를 파일 이름으로 사용하여 표준 입력에서 구성을 읽을 수 있습니다. 표준 입력을 사용할 때 구성의 모든 경로는 현재 작업 디렉토리를 기준으로 합니다.

`-f` 플래그는 선택사항입니다. 명령줄에서 이 플래그를 제공하지 않으면, Compose는 작업 디렉토리와 상위 디렉토리에서 compose.yaml 또는 docker-compose.yaml 파일을 찾습니다.

### 단일 Compose 파일 경로 지정하기
현재 디렉토리에 없는 Compose 파일의 경로를 지정하기 위해 `-f` 플래그를 사용할 수 있습니다. 이는 명령줄에서 직접 지정하거나 셸의 COMPOSE_FILE 환경 변수 또는 환경 파일에서 설정할 수 있습니다.

예를 들어, Compose Rails 샘플을 실행하고 있고 sandbox/rails 디렉토리에 compose.yaml 파일이 있다면, 다음과 같이 `-f` 플래그를 사용하여 어디서든 db 서비스용 postgres 이미지를 가져올 수 있습니다:

```bash
docker compose -f ~/sandbox/rails/compose.yaml pull db
```

### 프로젝트 이름 지정하기
각 구성에는 프로젝트 이름이 있습니다. Compose는 다음 메커니즘을 우선순위대로 사용하여 프로젝트 이름을 설정합니다:

1. `-p` 명령줄 플래그
2. COMPOSE_PROJECT_NAME 환경 변수
3. 구성 파일의 최상위 name: 변수 (또는 `-f`로 지정된 여러 구성 파일 중 마지막 name:)
4. 구성 파일이 포함된 프로젝트 디렉토리의 기본 이름 (또는 `-f`로 지정된 첫 번째 구성 파일이 포함된 디렉토리)
5. 구성 파일이 지정되지 않은 경우 현재 디렉토리의 기본 이름

프로젝트 이름은 소문자, 숫자, 대시, 밑줄만 포함할 수 있으며, 소문자나 숫자로 시작해야 합니다. 프로젝트 디렉토리나 현재 디렉토리의 기본 이름이 이 제약을 위반하는 경우, 다른 메커니즘을 사용해야 합니다.

```bash
$ docker compose -p my_project ps -a
NAME                 SERVICE    STATUS     PORTS
my_project_demo_1    demo       running

$ docker compose -p my_project logs
demo_1  | PING localhost (127.0.0.1): 56 data bytes
demo_1  | 64 bytes from 127.0.0.1: seq=0 ttl=64 time=0.095 ms
```

### 프로필을 사용하여 선택적 서비스 활성화하기
`--profile`을 사용하여 하나 이상의 활성 프로필을 지정할 수 있습니다. `docker compose --profile frontend up`을 실행하면 frontend 프로필이 있는 서비스와 프로필이 지정되지 않은 서비스가 시작됩니다. `docker compose --profile frontend --profile debug up`과 같이 여러 프로필을 활성화할 수도 있습니다.

프로필은 COMPOSE_PROFILES 환경 변수로도 설정할 수 있습니다.

### 병렬 처리 구성
`--parallel`을 사용하여 동시 엔진 호출의 최대 병렬 수준을 지정할 수 있습니다. `docker compose --parallel 1 pull`을 실행하면 Compose 파일에 정의된 가져올 수 있는 이미지를 한 번에 하나씩 가져옵니다. 이는 빌드 동시성을 제어하는 데도 사용할 수 있습니다.

병렬 처리는 COMPOSE_PARALLEL_LIMIT 환경 변수로도 설정할 수 있습니다.

### 환경 변수 설정
`-f`, `-p`, `--profiles` 플래그를 포함한 다양한 docker compose 옵션에 대한 환경 변수를 설정할 수 있습니다.

- COMPOSE_FILE 환경 변수 설정은 `-f` 플래그와 동일
- COMPOSE_PROJECT_NAME 환경 변수는 `-p` 플래그와 동일
- COMPOSE_PROFILES 환경 변수는 `--profiles` 플래그와 동일
- COMPOSE_PARALLEL_LIMIT는 `--parallel` 플래그와 동일

명령줄에서 플래그가 명시적으로 설정된 경우 관련 환경 변수는 무시됩니다.

COMPOSE_IGNORE_ORPHANS 환경 변수를 true로 설정하면 docker compose가 프로젝트의 고아 컨테이너를 감지하지 않습니다.

COMPOSE_MENU 환경 변수를 false로 설정하면 연결 모드에서 docker compose up 실행 시 도우미 메뉴가 비활성화됩니다. 또는 `docker compose up --menu=false`를 실행하여 도우미 메뉴를 비활성화할 수 있습니다.

### 드라이 런 모드로 명령 테스트하기
`--dry-run` 플래그를 사용하여 애플리케이션 스택 상태를 변경하지 않고 명령을 테스트할 수 있습니다. 드라이 런 모드는 Compose가 명령을 실행할 때 적용하는 모든 단계를 보여줍니다. 예:

```bash
docker compose --dry-run up --build -d
[+] Pulling 1/1
 ✔ DRY-RUN MODE -  db Pulled                                                0.9s
[+] Running 10/8
 ✔ DRY-RUN MODE -    build service backend                                  0.0s
 ✔ DRY-RUN MODE -  ==> writing image dryRun-754a08ddf8bcb1cf22f310f09206dd783d42f7dd  0.0s
 ✔ DRY-RUN MODE -  ==> naming to nginx-golang-mysql-backend                 0.0s
 ✔ DRY-RUN MODE -  Network nginx-golang-mysql_default Created               0.0s
 ✔ DRY-RUN MODE -  Container nginx-golang-mysql-db-1 Created                0.0s
 ✔ DRY-RUN MODE -  Container nginx-golang-mysql-backend-1 Created           0.0s
 ✔ DRY-RUN MODE -  Container nginx-golang-mysql-proxy-1 Created             0.0s
 ✔ DRY-RUN MODE -  Container nginx-golang-mysql-db-1 Healthy                0.5s
 ✔ DRY-RUN MODE -  Container nginx-golang-mysql-backend-1 Started           0.0s
 ✔ DRY-RUN MODE -  Container nginx-golang-mysql-proxy-1 Started             0.0s
```

위 예시에서 첫 번째 단계는 db 서비스에 정의된 이미지를 가져온 다음 backend 서비스를 빌드하는 것입니다. 그 다음 컨테이너가 생성됩니다. db 서비스가 시작되고, backend와 proxy는 db 서비스가 정상 상태가 될 때까지 기다린 후 시작됩니다.

드라이 런 모드는 거의 모든 명령과 함께 작동합니다. ps, ls, logs와 같이 Compose 스택의 상태를 변경하지 않는 명령과는 드라이 런 모드를 사용할 수 없습니다.

## 하위 명령어

| 명령어 | 설명 |
|--------|------|
| docker compose alpha | 실험적 명령어 |
| docker compose build | 서비스 빌드 또는 재빌드 |
| docker compose config | Compose 파일을 파싱, 해석하고 표준 형식으로 렌더링 |
| docker compose cp | 서비스 컨테이너와 로컬 파일시스템 간에 파일/폴더 복사 |
| docker compose create | 서비스용 컨테이너 생성 |
| docker compose down | 컨테이너, 네트워크 중지 및 제거 |
| docker compose events | 컨테이너로부터 실시간 이벤트 수신 |
| docker compose exec | 실행 중인 컨테이너에서 명령 실행 |
| docker compose images | 생성된 컨테이너가 사용하는 이미지 나열 |
| docker compose kill | 서비스 컨테이너 강제 중지 |
| docker compose logs | 컨테이너 출력 보기 |
| docker compose ls | 실행 중인 compose 프로젝트 나열 |
| docker compose pause | 서비스 일시 중지 |
| docker compose port | 포트 바인딩의 공개 포트 출력 |
| docker compose ps | 컨테이너 나열 |
| docker compose pull | 서비스 이미지 가져오기 |
| docker compose push | 서비스 이미지 푸시 |
| docker compose restart | 서비스 컨테이너 재시작 |
| docker compose rm | 중