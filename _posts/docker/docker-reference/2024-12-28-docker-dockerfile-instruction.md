---
layout: single
title:  "도커파일 지시어(Dockerfile Instruction)"
categories:
  - DockerDoc
tags:
  - [Docker, Dockerfile, Instruction]

toc: true
toc_sticky: true
---

<br>

아래 내용은 [Dockerfile reference](https://docs.docker.com/reference/dockerfile/)를 번역한 내용이다.

# FROM
---

```dockerfile
FROM [--platform=<platform>] <image> [AS <name>]
```

또는

```dockerfile
FROM [--platform=<platform>] <image>[:<tag>] [AS <name>]
```

또는

```dockerfile
FROM [--platform=<platform>] <image>[@<digest>] [AS <name>]
```

`FROM` 명령어는 새로운 빌드 단계를 초기화하고 후속 명령어들을 위한 기본 이미지를 설정합니다. 따라서 유효한 Dockerfile은 반드시 `FROM` 명령어로 시작해야 합니다. 이미지는 유효한 모든 이미지가 될 수 있습니다.

* Dockerfile에서 `ARG`는 `FROM` 명령어보다 앞에 올 수 있는 유일한 명령어입니다.
* 하나의 Dockerfile 내에서 여러 개의 이미지를 생성하거나 한 빌드 단계를 다른 단계의 의존성으로 사용하기 위해 `FROM`이 여러 번 나타날 수 있습니다. 각각의 새로운 `FROM` 명령어 이전에 커밋에 의해 출력된 마지막 이미지 ID를 기록해두면 됩니다. 각 `FROM` 명령어는 이전 명령어들에 의해 생성된 모든 상태를 초기화합니다.
* `FROM` 명령어에 `AS name`을 추가하여 새로운 빌드 단계에 이름을 선택적으로 지정할 수 있습니다. 이 이름은 이후의 `FROM <name>`, `COPY --from=<name>`, 그리고 `RUN --mount=type=bind,from=<name>` 명령어에서 이 단계에서 빌드된 이미지를 참조하는 데 사용될 수 있습니다.
* `tag`나 `digest` 값은 선택사항입니다. 이들 중 하나를 생략하면, 빌더는 기본적으로 `latest` 태그를 가정합니다. 빌더가 `tag` 값을 찾을 수 없는 경우 오류를 반환합니다.
* 선택적인 `--platform` 플래그는 `FROM`이 멀티플랫폼 이미지를 참조하는 경우 이미지의 플랫폼을 지정하는 데 사용될 수 있습니다. 예를 들어, `linux/amd64`, `linux/arm64`, 또는 `windows/amd64`입니다. 기본적으로 빌드 요청의 대상 플랫폼이 사용됩니다. 전역 빌드 인자들은 이 플래그의 값으로 사용될 수 있습니다. 예를 들어, 자동 플랫폼 ARG를 사용하면 네이티브 빌드 플랫폼(`--platform=$BUILDPLATFORM`)으로 단계를 강제할 수 있으며, 이를 사용하여 단계 내에서 대상 플랫폼으로 크로스 컴파일할 수 있습니다.

**ARG와 FROM이 상호작용하는 방식 이해하기**

`FROM` 명령어는 첫 번째 `FROM` 이전에 발생하는 모든 `ARG` 명령어에 의해 선언된 변수들을 지원합니다.

```dockerfile
ARG  CODE_VERSION=latest
FROM base:${CODE_VERSION}
CMD  /code/run-app

FROM extras:${CODE_VERSION}
CMD  /code/run-extras
```

`FROM` 이전에 선언된 `ARG`는 빌드 단계 외부에 있으므로 `FROM` 이후의 어떤 명령어에서도 사용될 수 없습니다. `FROM` 이전에 선언된 `ARG`의 기본값을 사용하려면 빌드 단계 내에서 값이 없는 `ARG` 명령어를 사용하세요:

```dockerfile
ARG VERSION=latest
FROM busybox:$VERSION
ARG VERSION
RUN echo $VERSION > image_version
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


# WORKDIR
---
`임시 컨테이너 생성`, `새로운 레이어 추가`

```dockerfile
WORKDIR /path/to/workdir
```

`WORKDIR` 명령어는 Dockerfile에서 이후에 나오는 모든 `RUN`, `CMD`, `ENTRYPOINT`, `COPY`, `ADD` 명령어들에 대한 작업 디렉토리를 설정합니다. 
만약 지정된 `WORKDIR`이 존재하지 않는다면, 이후 Dockerfile 명령어에서 사용되지 않더라도 자동으로 생성됩니다.

`WORKDIR` 명령어는 하나의 Dockerfile 내에서 여러 번 사용할 수 있습니다. 상대 경로가 제공되면, 이는 이전 `WORKDIR` 명령어의 경로를 기준으로 합니다. 예를 들어:

```dockerfile
WORKDIR /a
WORKDIR b
WORKDIR c
RUN pwd
```

이 Dockerfile에서 최종 `pwd` 명령어의 출력은 `/a/b/c`가 됩니다.

`WORKDIR` 명령어는 `ENV`를 사용하여 이전에 설정된 환경 변수를 처리할 수 있습니다. 단, Dockerfile에서 명시적으로 설정된 환경 변수만 사용할 수 있습니다. 예를 들어:

```dockerfile
ENV DIRPATH=/path
WORKDIR $DIRPATH/$DIRNAME
RUN pwd
```

이 Dockerfile에서 최종 `pwd` 명령어의 출력은 `/path/$DIRNAME`이 됩니다.

작업 디렉토리를 지정하지 않으면, 기본 작업 디렉토리는 `/`입니다. 실제로, 처음부터 Dockerfile을 만드는 것이 아니라면(`FROM scratch`가 아닌 경우), `WORKDIR`은 사용하는 기본 이미지에 의해 이미 설정되어 있을 수 있습니다.

따라서, 알 수 없는 디렉토리에서의 의도하지 않은 작업을 방지하기 위해, `WORKDIR`을 명시적으로 설정하는 것이 가장 좋은 방법입니다.

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


# COPY
---
`임시 컨테이너 생성`, `새로운 레이어 생성`


COPY는 두 가지 형식이 있습니다. 두 번째 형식은 경로에 공백이 포함된 경우에 사용해야 합니다.

```dockerfile
COPY [OPTIONS] <src> ... <dest>
COPY [OPTIONS] ["<src>", ... "<dest>"]
```

사용 가능한 `[OPTIONS]`는 다음과 같습니다:
- `--from`: 다른 빌드 스테이지나 이미지에서 복사
- `--chown`: 파일의 소유권 설정
- `--chmod`: 파일 권한 설정 (Dockerfile 버전 1.2 이상 필요)
- `--link`: 하드링크 사용 (Dockerfile 버전 1.4 이상 필요)
- `--parents`: 부모 디렉토리 구조 유지 (Dockerfile 버전 1.7-labs 이상 필요)
- `--exclude`: 특정 파일 제외 (Dockerfile 버전 1.7-labs 이상 필요)

`COPY` 명령어는 `<src>`에서 새로운 파일이나 디렉토리를 복사하여 이미지의 파일시스템의 `<dest>` 경로에 추가합니다. 파일과 디렉토리는 빌드 컨텍스트, 빌드 스테이지, 이름이 지정된 컨텍스트, 또는 이미지에서 복사할 수 있습니다.

`ADD`와 `COPY` 명령어는 기능적으로 비슷하지만, 약간 다른 목적으로 사용됩니다.

## 소스

`COPY`를 사용하여 여러 소스 파일이나 디렉토리를 지정할 수 있습니다. 
마지막 인수는 항상 목적지여야 합니다.  
예를 들어, 빌드 컨텍스트에서 `file1.txt`와 `file2.txt` 두 파일을 빌드 컨테이너의 `/usr/src/things/`로 복사하려면:

```dockerfile
COPY file1.txt file2.txt /usr/src/things/
```

여러 소스 파일을 직접 지정하거나 와일드카드를 사용하여 지정하는 경우, 목적지는 반드시 디렉토리여야 합니다(슬래시 `/`로 끝나야 함).

`COPY`는 `--from=<name>` 플래그를 사용하여 빌드 스테이지, 컨텍스트, 또는 이미지를 소스 위치로 지정할 수 있습니다.  
다음 예제는 `build`라는 스테이지에서 파일을 복사합니다:

```dockerfile
FROM golang AS build
WORKDIR /app
RUN --mount=type=bind,target=. go build -o /myapp ./cmd

COPY --from=build /myapp /usr/bin/
```

이름이 지정된 소스에서 복사하는 방법에 대한 자세한 내용은 `--from` 플래그 설명을 참조하세요.

## 빌드 컨텍스트에서 복사하기

빌드 컨텍스트에서 소스 파일을 복사할 때, 파일 경로는 컨텍스트의 루트를 기준으로 해석됩니다. 만약 빌드 컨텍스트 외부로 이어지는 상대 경로를 지정하면(예: `COPY ../something /something`), 상위 디렉토리 경로는 자동으로 제거됩니다. 이 예제에서 실제 적용되는 소스 경로는 `COPY something /something`이 됩니다.

**출발지가 디렉토리인 경우:**  
- 디렉토리의 내용이 파일시스템 메타데이터와 함께 복사됩니다
- 디렉토리 자체는 복사되지 않고, 그 내용만 복사됩니다
- 하위 디렉토리도 함께 복사되며, 목적지의 기존 디렉토리와 병합됩니다
- 파일 충돌이 발생하면 새로 추가되는 내용이 파일별로 우선됩니다
- 단, 디렉토리를 기존 파일 위에 복사하려고 하면 오류가 발생합니다
- 만약 목적지에 동일한 이름의 디렉터리가 있는 경우 목적지의 디렉터리를 덮어씁니다

```dockerfile
# 현재 디렉토리 전체를 복사
COPY . /app/

# 특정 디렉토리를 복사
COPY ./src /app/
COPY config/ /app/config/
```
<br>

**출발지가 파일인 경우:** 
- 파일과 그 메타데이터가 목적지로 복사됩니다
- 파일 권한이 보존됩니다
- 소스가 파일이고 목적지에 같은 이름의 디렉토리가 있는 경우 오류가 발생합니다

```dockerfile
# 단일 파일 복사
COPY package.json /app/
COPY app.js /app/
```
<br>

**특수한 경우:**
1. Dockerfile을 표준 입력으로 전달하는 경우 (`docker build - < Dockerfile`):
   - 빌드 컨텍스트가 없습니다
   - `COPY` 명령어는 `--from` 플래그를 사용하여 다른 스테이지, 명명된 컨텍스트, 또는 이미지에서만 파일을 복사할 수 있습니다

2. tar 아카이브를 표준 입력으로 전달하는 경우 (`docker build - < archive.tar`):
   - 아카이브의 루트에 있는 Dockerfile과 나머지 아카이브 내용이 빌드 컨텍스트로 사용됩니다

3. Git 저장소를 빌드 컨텍스트로 사용하는 경우:
   - 복사되는 파일의 기본 권한은 644입니다
   - 실행 권한이 설정된 파일은 755 권한으로 설정됩니다
   - 디렉토리의 권한은 755로 설정됩니다


## 패턴 매칭 (Pattern Matching)

Dockerfile의 `COPY` 명령에서 로컬 파일을 사용할 때, `<src>`에는 와일드카드(*)를 포함할 수 있으며, 이는 Go의 `filepath.Match` 규칙을 따릅니다.

**예시 1: 특정 확장자를 가진 파일 복사**  
빌드 컨텍스트의 루트에서 `.png`로 끝나는 모든 파일과 디렉터리를 추가하려면 다음과 같이 작성합니다:

```dockerfile
COPY *.png /dest/
```

<br>

**예시 2: 단일 문자 와일드카드 사용**  
`?`는 단일 문자 와일드카드로, 예를 들어 `index.js`와 `index.ts`에 매칭됩니다:

```dockerfile
COPY index.?s /dest/
```

<br>

**예시 3: 특수 문자가 포함된 파일 복사**  
파일명에 `[`, `]` 같은 특수 문자가 포함된 경우, Go 언어 규칙에 따라 경로를 이스케이프 처리해야 합니다. 예를 들어, `arr[0].txt`라는 파일을 추가하려면 다음과 같이 작성합니다:

```dockerfile
COPY arr[[]0].txt /dest/
```
<br>

## 대상 경로(Destination)
COPY 명령어의 대상(destination) 경로 지정 방법

```dockerfile 
# <src> : 소스 경로, <dest> : 대상 경로
COPY <src> <dest>
```

<br>

**절대 경로 사용**  
대상 경로가 슬래시(/)로 시작하면 절대 경로로 해석되며, 소스 파일은 현재 [**빌드 스테이지**](https://midobandev.github.io/docker/docker-00-docker-words/)의 루트를 기준으로 지정된 위치에 복사됩니다.

```dockerfile
# /abs/test.txt 파일 생성
COPY test.txt /abs/
```

<br>

**후행 슬래시의 중요성**  
후행 슬래시(trailing slash)는 중요한 의미를 가집니다.   

소스가 파일인 경우: 다른 결과
- `COPY test.txt /abs` : `/abs`라는 이름의 파일이 생성된다.
- `COPY test.txt /abs/` : `/abs/test.txt` 파일이 생성된다.

소스가 디렉토리인 경우: 동일한 결과
- `COPY src /abs` : src 폴더에 있는 파일 및 하위 디렉터리가 `/abs/` 디렉터리 안으로 복사된다.
- `COPY src /abs/` : src 폴더에 있는 파일 및 하위 디렉터리가 `/abs/` 디렉터리 안으로 복사된다.

<br>

**상대 경로 사용**  
대상 경로가 슬래시(/)로 시작하지 않으면, [**빌드 컨테이너**](https://midobandev.github.io/docker/docker-00-docker-words/)의 작업 디렉토리(working directory)를 기준으로 한 상대 경로로 해석됩니다.

```dockerfile
WORKDIR /usr/src/app
# /usr/src/app/rel/test.txt 파일 생성
COPY test.txt rel/
```

<br>

**경로 자동 생성**  
- **대상 경로에 지정한 디렉터리가 존재하지 않는 경우, 해당 경로상의 모든 누락된 디렉토리와 함께 자동으로 생성됩니다.**
- 소스가 파일이고 대상 경로가 후행 슬래시 없이 끝나는 경우, 소스 파일은 대상 경로에 파일로 작성됩니다.

<br>

**주의사항**  
- 경로 지정 시 후행 슬래시의 유무에 따라 결과가 달라질 수 있으므로 신중하게 사용해야 합니다.
- 상대 경로 사용 시 현재 작업 디렉토리(WORKDIR)를 정확히 파악해야 합니다.
- 자동 디렉토리 생성 기능을 활용하면 편리하지만, 의도하지 않은 디렉토리 구조가 생성될 수 있으므로 주의가 필요합니다.

<br>

## COPY--from

**기본 개념**  
기본적으로 `COPY` 명령어는 빌드 컨텍스트에서 파일을 복사합니다. 
`COPY --from` 플래그를 사용하면 이미지, 빌드 스테이지, 또는 명명된 컨텍스트에서 파일을 복사할 수 있습니다.

<br>

**문법**  
```dockerfile
COPY [--from=<image|stage|context>] <src> ... <dest>
```

<br>

**멀티 스테이지 빌드에서의 사용**  
멀티 스테이지 빌드에서 특정 스테이지의 파일을 복사하려면, 복사하고자 하는 스테이지의 이름을 지정하면 됩니다. 
스테이지 이름은 `FROM` 명령어와 함께 `AS` 키워드를 사용하여 지정합니다.

```dockerfile
# syntax=docker/dockerfile:1
FROM alpine AS build
COPY . .
RUN apk add clang
RUN clang -o /hello hello.c

FROM scratch
COPY --from=build /hello /
```

<br>

**이미지와 명명된 컨텍스트에서 복사**  
명명된 컨텍스트(`--build-context <name>=<source>`로 지정) 또는 이미지에서 직접 파일을 복사할 수도 있습니다. 
명명된 컨텍스트는 Dockerfile을 빌드할 때 추가적인 소스를 참조할 수 있게 해주는 기능입니다.

**명명된 컨텍스트**
`docker build` 명령어 사용 시 아래와 같이 빌드 컨텍스트를 내가 원하는 곳을 참조하도록 지정할 수 있습니다.
```bash
# 기본 구문
$ docker build --build-context name=source

# 실제 예시
# 1. 로컬 디렉토리 참조
$ docker build --build-context configs=./configs --build-context scripts=./scripts .

# 2. git 저장소 참조
$ docker build --build-context deps=github.com/user/repo.git .

# 3. 다른 이미지 참조
$ docker build --build-context base=docker-image:tag .
```

위 빌드 명령 시 지정한 컨텍스트를 Dockerfile에서 사용하는 방법은 다음과 같습니다.
```dockerfile
# 1. 로컬 디렉토리
COPY --from=configs ./app.config /etc/app/config

# 2. git 저장소
COPY --from=deps /src/lib ./lib

# 3. 다른 이미지
COPY --from=base /etc/nginx/nginx.conf /etc/nginx/
```

종합 예시
```bash
# 빌드 명령어
docker build \
  --build-context configs=./config-files \
  --build-context scripts=./deploy-scripts \
  --build-context libs=github.com/myorg/shared-libs.git \
  -t myapp:latest .
```

```dockerfile
# 베이스 이미지로 Node.js 사용
FROM node:18-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# configs 컨텍스트에서 설정 파일 복사
COPY --from=configs ./app.config ./config/app.config
COPY --from=configs ./database.config ./config/database.config

# libs 컨텍스트에서 공유 라이브러리 복사
COPY --from=libs /src/common ./lib/common
COPY --from=libs /src/utils ./lib/utils

# scripts 컨텍스트에서 배포 스크립트 복사
COPY --from=scripts ./startup.sh ./
COPY --from=scripts ./healthcheck.sh ./

# 시작 명령어
CMD ["./startup.sh"]
```

<br>

다음 예시는 공식 Nginx 이미지에서 `nginx.conf` 파일을 복사하는 방법을 보여줍니다.

```dockerfile
COPY --from=nginx:latest /etc/nginx/nginx.conf /nginx.conf
```

`COPY --from`의 소스 경로는 항상 지정된 이미지나 스테이지의 파일시스템 루트를 기준으로 해석됩니다.

<br>

**활용 팁**  
- 멀티 스테이지 빌드에서 빌드 결과물만 최종 이미지로 가져올 때 유용합니다.
- 기존 이미지의 설정 파일이나 바이너리를 재사용할 때 활용할 수 있습니다.
- 명명된 컨텍스트를 사용하면 빌드 시점에 외부 소스에서 파일을 가져올 수 있습니다.

<br>

## COPY --chown --chmod
Docker COPY 명령어의 권한 설정 (--chown, --chmod)

**기본 문법**  
```dockerfile
COPY [--chown=<user>:<group>] [--chmod=<perms> ...] <src> ... <dest>
```

<br>

**운영체제 제한사항**  
- `--chown`과 `--chmod` 기능은 Linux 컨테이너를 빌드하는 Dockerfile에서만 지원됩니다.
- Windows 컨테이너에서는 작동하지 않습니다.
- 이는 Linux와 Windows 간의 사용자 및 그룹 소유권 개념이 다르기 때문입니다.
- `/etc/passwd`와 `/etc/group`을 사용하여 사용자 및 그룹 이름을 ID로 변환하는 방식은 Linux 기반 컨테이너에서만 가능합니다.

<br>

### 소유권 설정 (--chown)
**기본 동작**  
빌드 컨텍스트에서 복사되는 모든 파일과 디렉토리는 `--chown` 플래그를 사용하지 않으면 UID와 GID가 `0`으로 생성됩니다.
`--chown` 플래그를 통해 지정된 값은 COPY 명령으로 복사된 파일이나 디렉토리의 `소유자(user)`와 `그룹(group) 소유권`으로 설정합니다.

<br> 

**`--chown` 플래그 사용법 및 특징**  
사용자명과 그룹명을 문자열로 지정할 수 있습니다. 
또한 직접적으로 UID와 GID에 정수값을 사용할 수 있습니다.
그리고 문자열과 정수값을 조합 할 수도 있습니다.

그룹명 없이 사용자명만 제공하거나 GID 없이 UID만 제공하면 동일한 숫자가 GID로 사용됩니다.
사용자명이나 그룹명을 제공하면 컨테이너의 루트 파일시스템의 `/etc/passwd`와 `/etc/group` 파일을 사용하여 UID나 GID로 변환합니다.

<br>

**사용 예시**  
```dockerfile
COPY --chown=55:mygroup files* /somedir/
COPY --chown=bin files* /somedir/
COPY --chown=1 files* /somedir/
COPY --chown=10:11 files* /somedir/
COPY --chown=myuser:mygroup --chmod=644 files* /somedir/
```

위 예시 설명
```
COPY --chown=55:mygroup files* /somedir/

숫자 UID(55)와 그룹명(mygroup)을 사용
files로 시작하는 모든 파일을 /somedir/ 디렉토리로 복사
복사된 파일들의 소유자는 UID 55, 그룹은 'mygroup'으로 설정됨


COPY --chown=bin files* /somedir/

'bin'이라는 사용자명만 지정 (그룹은 지정하지 않음)
그룹을 지정하지 않았으므로, 'bin' 사용자의 UID가 GID로도 사용됨
파일들의 소유자와 그룹 모두 'bin' 사용자로 설정됨


COPY --chown=1 files* /somedir/

숫자 1만 지정 (단일 숫자)
UID와 GID 모두 1로 설정됨
일반적으로 UID/GID 1은 Unix/Linux 시스템에서 'daemon' 사용자를 나타냄


COPY --chown=10:11 files* /somedir/

명시적으로 UID(10)와 GID(11)를 숫자로 지정
파일들의 소유자는 UID 10, 그룹은 GID 11로 설정됨
숫자 ID를 사용하므로 /etc/passwd나 /etc/group 파일이 없어도 동작


COPY --chown=myuser:mygroup --chmod=644 files* /somedir/

사용자명(myuser)과 그룹명(mygroup)을 지정
--chmod=644로 파일 권한도 설정
644는 8진수로, 소유자에게 읽기/쓰기(6), 그룹과 기타 사용자에게 읽기만(4) 허용
일반적으로 644는 텍스트 파일에 많이 사용되는 권한 설정임
```

<br>

**주의사항**  
- 컨테이너 루트 파일시스템에 `/etc/passwd` 또는 `/etc/group` 파일이 없는 경우, `--chown` 플래그에 사용자나 그룹 이름을 사용하면 빌드가 실패합니다.
- 숫자 ID를 사용하면 파일시스템 검색이 필요 없어 컨테이너 루트 파일시스템 내용에 의존하지 않습니다.
- 따라서 숫자로 UID/GID를 직접 지정하면 시스템이 `/etc/passwd`나 `/etc/group` 파일을 참조할 필요가 없어서, 이 파일들이 없어도 COPY 명령이 정상적으로 실행됩니다. 

<br>

### 권한 설정 (--chmod)
Dockerfile 문법 버전 1.10.0 이상에서는 `--chmod` 플래그가 변수 보간(interpolation)을 지원합니다. 
이를 통해 빌드 인자(ARG)를 사용하여 권한 비트를 정의할 수 있습니다:

```dockerfile
# syntax=docker/dockerfile:1.10
FROM alpine
WORKDIR /src
ARG MODE=440
COPY --chmod=$MODE . .
```

<br>

**권한 설정 시 고려사항**  
- 권한은 8진수 형식으로 지정해야 합니다
- 빌드 인자를 사용하면 유연한 권한 설정이 가능합니다
- 파일 시스템 보안을 고려하여 적절한 권한을 설정해야 합니다




<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>



# RUN
---

**기본 개념**  
`RUN` 명령어는 현재 이미지 위에 새로운 레이어를 생성하기 위해 명령을 실행합니다. 
추가된 레이어는 Dockerfile의 다음 단계에서 사용됩니다.  

RUN은 두 가지 형식이 있습니다:  
```dockerfile
# 쉘 형식:
RUN [OPTIONS] <command> ...
# Exec 형식:
RUN [OPTIONS] [ "<command>", ... ]
```

**쉘 형식 사용**  
쉘 형식이 가장 일반적으로 사용되며, 긴 명령어를 여러 줄로 나누어 작성할 수 있습니다. 줄바꿈 이스케이프나 heredocs를 사용할 수 있습니다:

```dockerfile
RUN <<EOF
apt-get update
apt-get install -y curl
EOF
```

<br>

**사용 가능한 옵션**  
`RUN` 명령어에 사용 가능한 `[OPTIONS]`:  

| 옵션 | 최소 Dockerfile 버전 |
|------|---------------------|
| `--mount` | 1.2 |
| `--network` | 1.3 |
| `--security` | 1.1.2-labs |

<br>

## 캐시 무효화
**RUN 명령어의 캐시 동작**  
- RUN 명령어의 캐시는 다음 빌드에서 자동으로 무효화되지 않습니다.
- 예를 들어 `RUN apt-get dist-upgrade -y`의 캐시는 다음 빌드에서도 재사용됩니다.
- 캐시는 `--no-cache` 플래그를 사용하여 무효화할 수 있습니다. (예: `docker build --no-cache`)
- `ADD`와 `COPY` 명령어에 의해서도 캐시가 무효화될 수 있습니다.

<br>

## RUN --mount

**기본 구조**  
```dockerfile
RUN --mount=[type=TYPE][,option=<value>[,option=<value>]...]
```

`RUN --mount`를 사용하면 빌드가 접근할 수 있는 파일시스템 마운트를 생성할 수 있습니다.  
다음과 같은 용도로 사용됩니다:  
- 호스트 파일시스템이나 다른 빌드 스테이지에 대한 바인드 마운트 생성
- 빌드 시크릿이나 ssh-agent 소켓에 접근
- 빌드 속도 향상을 위한 영구적인 패키지 관리 캐시 사용

<br>

**지원되는 마운트 유형**  

| 유형 | 설명 |
|------|------|
| `bind` (기본값) | 컨텍스트 디렉토리를 바인드 마운트(읽기 전용) |
| `cache` | 컴파일러와 패키지 매니저를 위한 캐시 디렉토리로 임시 디렉토리 마운트 |
| `tmpfs` | 빌드 컨테이너에 `tmpfs` 마운트 |
| `secret` | 이미지나 빌드 캐시에 포함시키지 않고 보안 파일(개인 키 등)에 접근 가능 |
| `ssh` | 패스프레이즈 지원과 함께 SSH 에이전트를 통한 SSH 키 접근 허용 |

<br>

**모범 사례**  
- 보안이 중요한 작업에는 `secret` 마운트 사용
- 빌드 성능 향상을 위해 `cache` 마운트 활용
- 민감한 정보는 이미지에 포함되지 않도록 주의
- 필요한 경우에만 적절한 마운트 옵션 사용

<br>

## RUN --mount=type=bind
이 마운트 타입은 빌드 컨테이너에 파일이나 디렉토리를 바인딩할 수 있게 해줍니다. 바인드 마운트는 기본적으로 읽기 전용입니다.

**옵션 설명**

| 옵션 | 설명 |
|------|------|
| `target`, `dst`, `destination` | 마운트 경로 |
| `source` | `from`의 소스 경로. 기본값은 `from`의 루트 |
| `from` | 소스의 루트에 대한 빌드 스테이지, 컨텍스트 또는 이미지 이름. 기본값은 빌드 컨텍스트 |
| `rw`, `readwrite` | 마운트에 쓰기를 허용. 작성된 데이터는 폐기됨 |

<br>

## RUN --mount=type=cache
이 마운트 타입은 빌드 컨테이너가 컴파일러와 패키지 매니저를 위한 디렉토리를 캐시할 수 있게 해줍니다.

**옵션 설명**

| 옵션 | 설명 |
|------|------|
| `id` | 개별/다른 캐시를 구분하기 위한 선택적 ID. 기본값은 `target`의 값 |
| `target`, `dst`, `destination` | 마운트 경로 |
| `ro`, `readonly` | 설정 시 읽기 전용 |
| `sharing` | `shared`, `private`, 또는 `locked` 중 하나. 기본값은 `shared`. `shared` 캐시 마운트는 여러 작성자가 동시에 사용할 수 있음. `private`은 여러 작성자가 있을 경우 새로운 마운트를 생성. `locked`는 첫 번째 작성자가 마운트를 해제할 때까지 두 번째 작성자를 일시 중지 |
| `from` | 캐시 마운트의 기반으로 사용할 빌드 스테이지, 컨텍스트 또는 이미지 이름. 기본값은 빈 디렉토리 |
| `source` | 마운트할 `from`의 하위 경로. 기본값은 `from`의 루트 |
| `mode` | 새 캐시 디렉토리의 8진수 파일 모드. 기본값 `0755` |
| `uid` | 새 캐시 디렉토리의 사용자 ID. 기본값 `0` |
| `gid` | 새 캐시 디렉토리의 그룹 ID. 기본값 `0` |

캐시 디렉토리의 내용은 명령어 캐시를 무효화하지 않고 빌더 호출 사이에 유지됩니다. 캐시 마운트는 성능 향상을 위해서만 사용해야 합니다. 다른 빌드가 파일을 덮어쓰거나 저장 공간이 더 필요한 경우 GC가 정리할 수 있으므로, 빌드는 캐시 디렉토리의 모든 내용으로 작동해야 합니다.

<br>

**예시: Go 패키지 캐시하기**  

```dockerfile
# syntax=docker/dockerfile:1
FROM golang
RUN --mount=type=cache,target=/root/.cache/go-build \
  go build ...
```

<br>

**예시: apt 패키지 캐시하기**  

```dockerfile
# syntax=docker/dockerfile:1
FROM ubuntu
RUN rm -f /etc/apt/apt.conf.d/docker-clean; echo 'Binary::apt::APT::Keep-Downloaded-Packages "true";' > /etc/apt/apt.conf.d/keep-cache
RUN --mount=type=cache,target=/var/cache/apt,sharing=locked \
  --mount=type=cache,target=/var/lib/apt,sharing=locked \
  apt update && apt-get --no-install-recommends install -y gcc
```

apt는 데이터에 대한 독점적인 접근이 필요하므로, 캐시는 `sharing=locked` 옵션을 사용합니다. 이는 동일한 캐시 마운트를 사용하는 여러 병렬 빌드가 서로를 기다리고 동시에 동일한 캐시 파일에 접근하지 않도록 보장합니다. 이 경우 각 빌드가 다른 캐시 디렉토리를 생성하도록 `sharing=private`를 사용할 수도 있습니다.

<br>

## RUN --mount=type=tmpfs
이 마운트 타입은 빌드 컨테이너에 `tmpfs`를 마운트할 수 있게 해줍니다.

**옵션 설명**

| 옵션 | 설명 |
|------|------|
| `target`, `dst`, `destination` | 마운트 경로 |
| `size` | 파일시스템 크기의 상한선 지정 |

<br>

## RUN --mount=type=secret
이 마운트 타입은 빌드 컨테이너가 토큰이나 개인 키와 같은 비밀값에 접근할 수 있게 해주며, 이러한 값들을 이미지에 직접 포함시키지 않습니다.

기본적으로 비밀은 파일로 마운트됩니다. `env` 옵션을 설정하여 비밀을 환경 변수로 마운트할 수도 있습니다.

**옵션 설명**

| 옵션 | 설명 |
|------|------|
| `id` | 비밀의 ID. 설정되지 않은 경우 대상 경로의 기본 이름으로 기본 설정됨 |
| `target`, `dst`, `destination` | 지정된 경로에 비밀을 마운트. 설정되지 않고 `env`도 설정되지 않은 경우 `/run/secrets/` + `id`가 기본값 |
| `env` | 비밀을 파일 대신 또는 추가로 환경 변수로 마운트 (Dockerfile v1.10.0부터) |
| `required` | `true`로 설정 시 비밀을 사용할 수 없을 때 명령어 오류 발생. 기본값은 `false` |
| `mode` | 비밀 파일의 8진수 파일 모드. 기본값 `0400` |
| `uid` | 비밀 파일의 사용자 ID. 기본값 `0` |
| `gid` | 비밀 파일의 그룹 ID. 기본값 `0` |

<br>

**예시: S3 접근하기**  

```dockerfile
# syntax=docker/dockerfile:1
FROM python:3
RUN pip install awscli
RUN --mount=type=secret,id=aws,target=/root/.aws/credentials \
  aws s3 cp s3://... ...
```

```console
$ docker buildx build --secret id=aws,src=$HOME/.aws/credentials .
```

<br>

**예시: 환경 변수로 마운트하기**  
다음 예시는 `API_KEY` 비밀을 동일한 이름의 환경 변수로 마운트합니다.

```dockerfile
# syntax=docker/dockerfile:1
FROM alpine
RUN --mount=type=secret,id=API_KEY,env=API_KEY \
    some-command --token-from-env $API_KEY
```

빌드 환경에서 `API_KEY` 환경 변수가 설정되어 있다고 가정하면, 다음 명령어로 빌드할 수 있습니다:

```console
$ docker buildx build --secret id=API_KEY .
```

<br>

## RUN --mount=type=ssh
이 마운트 타입은 빌드 컨테이너가 SSH 에이전트를 통해 패스프레이즈를 지원하는 SSH 키에 접근할 수 있게 해줍니다.

**옵션 설명**

| 옵션 | 설명 |
|------|------|
| `id` | SSH 에이전트 소켓 또는 키의 ID. 기본값은 "default" |
| `target`, `dst`, `destination` | SSH 에이전트 소켓 경로. 기본값은 `/run/buildkit/ssh_agent.${N}` |
| `required` | `true`로 설정 시 키를 사용할 수 없을 때 명령어 오류 발생. 기본값은 `false` |
| `mode` | 소켓의 8진수 파일 모드. 기본값 `0600` |
| `uid` | 소켓의 사용자 ID. 기본값 `0` |
| `gid` | 소켓의 그룹 ID. 기본값 `0` |

<br>

**예시: GitLab 접근하기**  

```dockerfile
# syntax=docker/dockerfile:1
FROM alpine
RUN apk add --no-cache openssh-client
RUN mkdir -p -m 0700 ~/.ssh && ssh-keyscan gitlab.com >> ~/.ssh/known_hosts
RUN --mount=type=ssh \
  ssh -q -T git@gitlab.com 2>&1 | tee /hello
# 빌드 진행 타입이 'plain'으로 정의된 경우
# "Welcome to GitLab, @GITLAB_USERNAME_ASSOCIATED_WITH_SSHKEY"가 출력되어야 합니다
```

```console
$ eval $(ssh-agent)
$ ssh-add ~/.ssh/id_rsa
(여기에 패스프레이즈 입력)
$ docker buildx build --ssh default=$SSH_AUTH_SOCK .
```

`$SSH_AUTH_SOCK` 대신 호스트의 `*.pem` 파일 경로를 직접 지정할 수도 있습니다. 단, 패스프레이즈가 있는 pem 파일은 지원되지 않습니다.

<br>

## RUN --network

```dockerfile
RUN --network=TYPE
```

`RUN --network`는 명령어가 실행되는 네트워크 환경을 제어할 수 있게 해줍니다.

지원되는 네트워크 타입:

| 타입 | 설명 |
|------|------|
| `default` (기본값) | 기본 네트워크에서 실행 |
| `none` | 네트워크 접근 없이 실행 |
| `host` | 호스트의 네트워크 환경에서 실행 |

**RUN --network=default**  
플래그를 전혀 제공하지 않은 것과 동일하며, 명령어는 빌드의 기본 네트워크에서 실행됩니다.

**RUN --network=none**  
명령어는 네트워크 접근 없이 실행됩니다(`lo`는 여전히 사용 가능하지만 이 프로세스로 격리됨)

<br>

**예시: 외부 영향 격리하기**  

```dockerfile
# syntax=docker/dockerfile:1
FROM python:3.6
ADD mypackage.tgz wheels/
RUN --network=none pip install --find-links wheels mypackage
```

`pip`는 이전 빌드 단계에서 제어할 수 있는 tar 파일에 제공된 패키지만 설치할 수 있습니다.

<br>

**RUN --network=host**  
명령어는 호스트의 네트워크 환경에서 실행됩니다(`docker build --network=host`와 유사하지만 명령어별로 적용)

**주의**
`--network=host` 사용은 `network.host` 권한으로 보호되며, buildkitd 데몬을 시작할 때 `--allow-insecure-entitlement network.host` 플래그나 buildkitd 설정에서 활성화해야 하고, 빌드 요청 시 `--allow network.host` 플래그가 필요합니다.

<br>

## RUN --security

**참고**  
안정 버전 문법에서는 아직 사용할 수 없으며, `docker/dockerfile:1-labs` 버전을 사용하세요.

```dockerfile
RUN --security=<sandbox|insecure>
```

기본 보안 모드는 `sandbox`입니다. `--security=insecure`를 사용하면, 빌더는 샌드박스 없이 비보안 모드에서 명령어를 실행하며, 이는 높은 권한이 필요한 작업(예: containerd)을 실행할 수 있게 해줍니다. 이는 `docker run --privileged`를 실행하는 것과 동일합니다.

<br>

**주의**  
이 기능에 접근하려면, buildkitd 데몬을 시작할 때 `--allow-insecure-entitlement security.insecure` 플래그나 buildkitd 설정에서 `security.insecure` 권한을 활성화해야 하고, 빌드 요청 시 `--allow security.insecure` 플래그가 필요합니다.

기본 샌드박스 모드는 `--security=sandbox`를 통해 활성화할 수 있지만, 이는 실제로 아무 효과가 없습니다.

<br>

**예시: 권한 확인하기**  

```dockerfile
# syntax=docker/dockerfile:1-labs
FROM ubuntu
RUN --security=insecure cat /proc/self/status | grep CapEff
```

```text
#84 0.093 CapEff:	0000003fffffffff
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

# EXPOSE 

## 명령어 기본 구문
```dockerfile
EXPOSE <포트> [<포트>/<프로토콜>...]
```

<br>

## EXPOSE의 역할과 특징

EXPOSE는 컨테이너가 실행 중에 특정 네트워크 포트에서 수신 대기한다는 것을 Docker에 알려주는 명령어입니다. 하지만 이 명령어에 대해 다음과 같은 중요한 특징들을 이해해야 합니다:

**1. 문서화 역할**  
- EXPOSE는 실제로 포트를 외부에 공개(publish)하지 않습니다
- 이미지 제작자와 컨테이너 실행자 간의 일종의 문서화 역할을 합니다
- 어떤 포트가 사용될 것인지를 명시하는 용도입니다

**2. 실제 포트 공개 방법**  
컨테이너 실행 시 포트를 실제로 공개하려면 다음 두 가지 방법을 사용할 수 있습니다:
- `-p(소문자)` 플래그: 특정 포트를 지정하여 공개
- `-P(대문자)` 플래그: EXPOSE로 지정된 모든 포트를 높은 번호의 포트에 자동 매핑

<br>

## 프로토콜 지정

**TCP 프로토콜 (기본값)**  
```dockerfile
EXPOSE 80
# 또는
EXPOSE 80/tcp
```

**UDP 프로토콜**  
```dockerfile
EXPOSE 80/udp
```

**TCP와 UDP 동시 지정**  
같은 포트에 대해 TCP와 UDP를 모두 사용하려면 다음과 같이 각각 지정해야 합니다:
```dockerfile
EXPOSE 80/tcp
EXPOSE 80/udp
```

<br>

## 실제 사용 예시

**1. Dockerfile에서의 EXPOSE**  
```dockerfile
FROM nginx:latest
EXPOSE 80/tcp
EXPOSE 443/tcp
```

**2. 컨테이너 실행 시 포트 매핑**  
```bash
# EXPOSE된 모든 포트를 자동으로 매핑
docker run -P nginx

# 특정 포트를 수동으로 매핑
docker run -p 8080:80 nginx

# TCP와 UDP 포트 모두 매핑
docker run -p 80:80/tcp -p 80:80/udp nginx
```

<br>

## 주의사항

1. `-P` 플래그 사용 시:
   - 호스트의 임시 포트(ephemeral port)가 사용됩니다
   - TCP와 UDP는 서로 다른 호스트 포트에 매핑됩니다

    <details>
    <summary> 
    <b><span>대문자 -P 옵션 설명</span></b>
    </summary>

    <div markdown="1">

    docker run -P (대문자 P)를 사용하면, Dockerfile의 EXPOSE로 지정된 포트를 호스트의 임시 포트(ephemeral port)에 자동으로 매핑합니다.  
    작동 방식을 예시로 설명하면:
    ```dockerfile
    # Dockerfile
    EXPOSE 80
    EXPOSE 443
    ```
    이렇게 설정된 이미지를 -P 옵션으로 실행할 경우:
    ```bash
    $ docker run -P nginx
    ```
    Docker는 시스템의 임시 포트 범위(일반적으로 32768-65535)에서 사용 가능한 포트를 자동으로 할당합니다.  
    실제 할당된 포트는 다음 명령어로 확인할 수 있습니다:
    ```bash
    $ docker ps
    ```

    출력 예시:
    ```
    CONTAINER ID   IMAGE   ...   PORTS
    abc123def456   nginx   ...   0.0.0.0:32768->80/tcp, 0.0.0.0:32769->443/tcp
    ```
    이 경우:

    - 컨테이너의 80포트는 호스트의 32768포트로 매핑
    - 컨테이너의 443포트는 호스트의 32769포트로 매핑

    따라서:

    - http://localhost:32768로 80포트 접근 가능
    - https://localhost:32769로 443포트 접근 가능

    이 방식의 장단점:

    - 장점: 포트 충돌 걱정 없이 여러 컨테이너 실행 가능
    - 단점: 매번 다른 포트가 할당되어 예측하기 어려움

    그래서 보통 개발 환경에서는 -p 옵션으로 명시적인 포트 매핑을 선호합니다.
    </div>
    </details>


2. 런타임 오버라이드:
   - Dockerfile의 EXPOSE 설정과 관계없이 `docker run` 시 `-p` 플래그로 다른 포트 매핑이 가능합니다

<br>

## 네트워크 통신 대안

Docker 네트워크를 사용하면 EXPOSE나 포트 공개 없이도 컨테이너 간 통신이 가능합니다:
```bash
# 네트워크 생성
docker network create mynetwork

# 컨테이너를 네트워크에 연결
docker run --network mynetwork myapp
```

이 경우 같은 네트워크상의 컨테이너들은 모든 포트를 통해 서로 통신할 수 있습니다.

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<!--<img src="" width="80%" height="80%"/>-->
