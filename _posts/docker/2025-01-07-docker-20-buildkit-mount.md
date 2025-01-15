---
layout: single
title:  "[Docker] npm install 빌드 속도 개선(feat. BuildKit 캐시)"
categories:
  - Docker
tags:
  - [Docker, BuildKit, Cache-mount]

toc: true
toc_sticky: true
---

`npm install`은 빌드 실행 시간에 많은 영향을 주는 부분 중 하나이다.
실제 나의 경우 npm install로 인해 50분 이상 소요된 경우도 있다.  
빌드 최적화를 위해 Dockerfile에서 활용할 수 있는 방법에 대해 알아보자.

<br>

## npm install 과정
`npm install` 명령은 `package.json`과 `package-lock.json`을 참고하여 의존성 패키지를 다운로드하고 설치하는 명령이다. 
결국 **npm install**은 node.js 위에서 실행되는 애플리케이션의 의존성 라이브러리를 관리하고 설치하는 명령이라고 보면 된다.

### .npm 캐시 디렉터리
패키지를 다운로드 하는 과정에서 호스트의 특정 경로에 존재하는 `.npm` 이라는 캐시 디렉터리를 사용한다.

**최초 `npm install`이 패키지를 다운로드 하는 경우:**
1. 네트워크 요청으로 `package.json`에 정의된 의존성 패키지를 모두 다운로드 받는다.
2. `.npm` 디렉터리에 다운로드 받은 패키지의 캐시 데이터를 저장한다.
```bash
# 윈도우 npm 캐시 저장소 확인 방법
$ npm config get cache
C:\Users\사용자명\AppData\Local\npm-cache
```
3. 다운로드된 패키지를 압축 해제하여 `node_modules` 디렉터리에 의존성 트리 구조에 따라 저장한다.
`의존성 트리`는 package.json에 정의된 의존성 및 하위 의존성에 맞게 구조화 한다.
<img src="https://github.com/user-attachments/assets/ae428ee8-66d2-4666-b79a-9693814b1950" width="80%" height="80%"/>

<br>

**두 번째 `npm install`을 통해 패키지를 다운로드 하는 경우:**
1. `.npm` 디렉터리에 원하는 패키지가 캐싱되어 있는지 확인하고, 있으면 캐시에서 읽어온다.
2. 만약 `.npm` 디렉터리에 원하는 패키지가 없는 경우 네트워크 요청으로 필요한 패키지를 다운로드 받는다. 다운로드 한 패키지의 캐시 데이터를 `.npm` 디렉터리에 저장한다.
3. 캐시에서 읽은 패키지와 다운로드된 패키지를 압축 해제하여 `node_module` 디렉터리에 의존성 트리의 구조화된 형태로 저장한다.

`npm intall`은 위 과정을 반복한다.

<br>

## 싱글 스테이지 빌드에서 npm install 과정

```dockerfile
FROM node:14
WORKDIR /app
# package.json 별도 복사
COPY package*.json ./
RUN npm install
# 소스 코드 복사
COPY . .
```
- 위 Dockerfile을 보면 소스 코드를 복사하기 전 package.json를 먼저 복사하여 npm install(의존성 패키지 다운로드)를 진행한다.
- 이와 같은 구조로 빌드를 진행하면 최초에만 의존성 패키지를 다운로드하고, 이후 빌드에서는 도커의 이미지 레이어 캐시로 인해 의존성 패키지를 다운로드하지 않는다.
- 즉, `package.json`과 `package-lock.json`의 데이터가 변경되지 않으면 반복된 빌드를 해도 최초에 캐싱된 이미지 레이어를 재사용한다. 

- 위 Dockerfile은 소스 코드 복사 전 package.json 미리 복사함으로써 npm install 까지 명령을 캐싱된 이미지 레이어를 재사용할 수 있었다.
- 그런데 소스 코드를 로컬이 아닌 github에서 관리하는 경우는 상황이 다르다.
- `package.json`과 소스 코드 복사를 위외 같은 구조로 분리할 수 없다.
```dockerfile
FROM node:14
WORKDIR /app
RUN git clone https://github.com/YOUR_REPO_URL.git .
RUN npm install
```

이렇게 되면 소스 코드가 변경될 때마다 패키지를 계속 다운로드하게 되어 빌드 시간이 오래걸린다.

<br>

## 멀티 스케이지 빌드 - 원격 저장소 사용 시 이미지 레이어 캐시 사용
- 멀티 스테이지 빌드를 활용하면 원격 저장소에 관리하는 프로젝트도 소스 코드와 package.json을 분리할 수 있다.
- FROM 명령을 기준으로 이미지를 빌드하기 때문에 FROM으로 나뉜 이전 빌드의 변경사항이 다음 빌드에 영향을 주지 않는다.
- 따라서서 `Dockerfile`과 `package.json(package-lock.json 포함)`이 변경되지 않았다면 캐싱된 이미지 레이어를 재사용하게 된다.

```dockerfile
# 최초 베이스 이미지
FROM alpine/git as source
WORKDIR /app
# 소스 코드 다운로드
RUN git clone https://github.com/midoBanDev/loan-manager-front.git .

# 베이스 이미지를 분리하면 변경된 소스 코드의 영향을 받지 않는다.
FROM node:22.11.0 as dependencies
WORKDIR /app

# 패키지 다운로드 속도 개선을 위해 레지스트리 저장소 변경.
RUN npm config set registry https://registry.npmmirror.com

# package*.json 파일만 복사하면 캐시 레이어 사용 가능.
COPY --from=source /app/package*.json ./
RUN npm install

# 베이스 이미지 분리
FROM node:22.11.0 as build
WORKDIR /app
# node_modules이 변경되지 않았다면 이전 빌드에서 캐싱된 레이어를 재사용할 수 있다.
COPY --from=dependencies /app/node_modules ./node_modules
# 소스코드 복사. 새 레이어 생성.
COPY --from=source /app .
RUN npm run build
```

<br>

## BuildKit 캐시 마운트 사용.
`BuildKit 캐시 마운트`란 Docker 빌드 성능을 향상시키기 위한 기능이다. 
BuildKit의 캐시 마운트는 **로컬 환경에서의 npm install 캐싱 매커니즘**을 Docker 데몬에서도 사용할 수 있도록 지원하는 기능을 제공한다.
- 빌드 과정에서 생성되는 임시 파일들을 캐시에 저장하여 재사용한다.
- 저장되는 캐시들은 호스트의 파일 시스템에 저장되며, 도커 데몬에 의해 관리된다.  
(호스트 저장 경로: C:\ProgramData\Docker\buildkit\)
- 이를 통해 컨테이너 이미지 빌드 시 이전 빌드의 결과물을 활용할 수 있다.

<br>

### 빌드 캐시가 저장되는 위치
- 로컬 개발 환경(Window)
  - WSL2를 사용하는 Docker Desktop 환경에서는 BuildKit 캐시가 WSL2 VM 내부에 저장된다.
  - 파일 탐색기에 `\\wsl$` 입력.
  - docker-desktop-data > mnt 경로 에서 `buildkit` 으로 검색.

- CI/CD 환경 (예: GitHub Actions)
  - 러너의 캐시 저장소에 저장된다.
  - 다음 빌드에서 재사용 가능하다.

<br>

**캐시 타입**
- `type=cache` - 빌드 캐시를 영구적으로 저장
- `type=tmp` - 임시 저장소로 사용되며 빌드 완료 후 삭제
- `type=secret` - 민감한 정보를 안전하게 처리

<br>

### BuildKit 캐시 마운트 사용 방법
- Dockerfile에서의 기본 문법은 다음과 같다.  
  
```dockerfile
RUN --mount=type=cache,target=/root/.npm, id=npm \
    --mount=type=cache,target=/app/node_modules,id=modules,sharing=locked \
    npm install
```
- `--mount=type=cache`은  **컨테이너 내부의 캐시 경로(target)**와 호스트 BuildKit 캐시 디렉터리가 연동된다.
- `target`은 호스트의 캐시 디렉터리와 마운트할 경로를 지정한다.
  - target 경로(/app/node_modules)가 컨테이너에 없을 경우, BuildKit이 해당 디렉터리를 자동으로 생성하여 캐시 데이터를 마운트한다.
- `id`는 캐시 디렉터리를 식별하기 위한 고유 식별자를 지정한다.
- `sharing=locked`는 BuildKit에서 캐시를 사용할 때 동시성 제어를 위한 사용된다.
1. npm install로 의존성 패키지를 다운로드 하면 빌드 컨테이너 내부의 `/root/.npm`에 다운로드 한 패키지를 저장한다.
2. 이때 연동된 호스트 캐시 디렉터리에 다운받은 패키지를 동기화한다.
- 빌드 컨테이너는 빌드 종료시 사라지기 때문에 실제 패키지를 호스트 캐시 디렉터리에서 저장 및 관리한다.

<br>

### 캐시 재사용
- `id`와 `target`이 캐시 재사용의 기준이 된다.
- id와 target이 둘다 명시된 경우 `id`를 기준으로 캐시를 재사용한다.
- id가 없고 target만 있는 경우 `target` 경로가 기준이 되어 캐시를 재사용한다. 

<br>

<details>
<summary> 
<b><span>shareing locked 옵션</span></b>
</summary>

<div markdown="1">


`/root/.npm (npm 캐시)`
- 이 디렉터리는 패키지 다운로드 캐시로 사용되며, npm install 과정에서 패키지를 다운로드하고 저장만 한다.
- 다운로드된 패키지는 변경되지 않으므로, 동시 쓰기 제어가 필요 없다.
- 따라서 sharing=locked이 필요하지 않다.

`/app/node_modules (패키지 설치 경로)`
- npm install은 패키지를 설치하며 node_modules 디렉터리를 수정한다.
- 빌드 프로세스가 병렬로 실행되면, 여러 빌드가 같은 node_modules를 동시에 수정하려고 할 수 있다.
- sharing=locked을 적용하면 동시에 쓰기를 방지하여 데이터 손상을 예방할 수 있다.
</div>
</details>

<br>

### NPM install 옵션

```dockerfile
RUN --mount=type=cache,target=/root/.npm,id=npm \
    npm install --prefer-offline --no-audit --no-fund --maxsockets 8
```

`--prefer-offline`
- 패키지 설치 시 로컬 캐시를 우선적으로 사용
- 캐시에 있는 패키지는 npm registry 확인을 건너뜀
- 캐시에 없는 패키지만 온라인으로 다운로드

```bash
# 내부적으로 이렇게 동작
1. 로컬 캐시 확인
2. 캐시에 있으면 → 바로 사용
3. 캐시에 없으면 → registry에서 다운로드
```

<br>

`--no-audit`
- npm의 보안 취약점 감사를 건너뜀
- npm audit 명령어가 실행되지 않음
- 의존성 트리 분석 과정을 생략하여 설치 속도 향상

```bash
# 기본 동작
$ npm install → 설치 완료 → 보안 감사 실행
# --no-audit 사용 시
$ npm install → 설치 완료 (보안 감사 생략)
```

<br>

`--no-fund`
- 자금 지원(funding) 메시지 표시를 비활성화
- 패키지 개발자들의 funding 정보 출력을 건너뜀
- 순수하게 설치 관련 출력만 표시

```bash
# funding 메시지 예시
4 packages are looking for funding
  run `npm fund` for details
# --no-fund 사용 시 이런 메시지가 표시되지 않음
```

<br>

`--maxsockets 8`
- 동시에 열 수 있는 HTTP/HTTPS 연결 수를 8개로 설정
- 병렬 다운로드 수를 증가시켜 설치 속도 향상
- 기본값은 대략 2-3개 정도

```javascript
// 내부적인 동작 방식
connections = Math.min(pendingPackages.length, maxsockets)
for (let i = 0; i < connections; i++) {
    downloadPackage()
}
```

<br>

### build 사용 방법
```bash
# 환경변수로 설정하는 방법
$ export DOCKER_BUILDKIT=1
$ docker build .

# 또는 build 명령어에 직접 포함
$ DOCKER_BUILDKIT=1 docker build .

# docker buildx 명령어 사용 (BuildKit이 기본적으로 활성화됨)
$ docker buildx build .
```
<br>

## BuildKit 캐시 마운트 실제 사용 결과

- NPM 레지스트리 https://registry.npmmirror.com 로 변경.
- `/root/.npm` 만 캐시하도록 설정
- `--maxsockets 10` 병렬처리 옵션 추가
  
```dockerfile
# 최초 베이스 이미지
FROM alpine/git as source
WORKDIR /app
# 소스 코드 다운로드
RUN git clone https://github.com/midoBanDev/loan-manager-front.git .

# 베이스 이미지를 분리하면 변경된 소스 코드의 영향을 받지 않는다.
FROM node:22.11.0 as dependencies
WORKDIR /app

# 패키지 레지스트리 저장소 변경.
RUN npm config set registry https://registry.npmmirror.com

COPY --from=source /app/package*.json ./

RUN --mount=type=cache,target=/root/.npm,id=npm \
    npm install --maxsockets 10

# 베이스 이미지 분리
FROM node:22.11.0 as build
WORKDIR /app

COPY --from=source /app .
 
COPY --from=dependencies /app/node_modules ./node_modules
    
RUN npm run build
```

- 최초 빌드 실행 결과 : 총 실행 시간 `3019.3초(약 50분)`
<img src="https://github.com/user-attachments/assets/8e3f8e85-a77e-489e-ae47-dbecd25fee90" width="80%" height="80%"/>

- 두 번째 빌드 실행 결과 : 총 실행 시간 `1.9초초`
<img src="https://github.com/user-attachments/assets/994f43d4-9158-453a-bcc1-333f21f542c7" width="80%" height="80%"/>

<br>

## 전체 Ddockerfile 
```dockerfile
# 소스코드 가져오기 스테이지
FROM alpine/git as source
WORKDIR /app
# Git repository clone
RUN git clone https://github.com/YOUR_REPO_URL.git .



# 빌드 스테이지
FROM node:22.11.0 as build
WORKDIR /app

# 소스코드에서 package*.json 파일만 먼저 복사
COPY --from=source /app/package*.json ./

# 레지스트리 변경
RUN npm config set registry https://registry.npmmirror.com

# BuildKit 캐시를 사용한 의존성 설치
# npm 캐시와 node_modules를 캐시 마운트로 관리
# .npm과 node_modules 둘다 캐시 마운트를 사용하는 경우 오버헤드가 너무 커서 빌드 시간이 엄청 오래걸린다.
# npm ci는 package-lock.json 엄격한 검증 수행, 제한적인 병렬 설치, 보수적인 캐시 활용 등으로 오래 걸림. 
RUN --mount=type=cache,target=/root/.npm,id=npm \
    --mount=type=cache,target=/app/node_modules,id=modules,sharing=locked \
    npm ci

# 
RUN --mount=type=cache,target=/root/.npm,id=npm \
    npm install --prefer-offline --no-audit --no-fund

# Git 소스코드 전체 복사
COPY --from=source /app .

# node_modules를 참조하지 못하는 오류가 발생함. 원인 아직 파악하지 못함.
#RUN --mount=type=cache,target=/app/node_modules,id=modules \
#    npm run build

# node_modules 복사
COPY --from=dependencies /app/node_modules ./node_modules

RUN npm run build



# 실행 스테이지
FROM nginx:alpine

# nginx 기본 설정 파일 제거
RUN rm -rf /etc/nginx/conf.d/*

# nginx 설정 파일 복사 (Git 소스에서)
COPY --from=source /app/nginx.conf /etc/nginx/conf.d/custom.conf

# 빌드 결과물을 nginx의 서비스 디렉토리로 복사
COPY --from=build /app/build /usr/share/nginx/html

# 80번 포트 노출
EXPOSE 80

# nginx 실행
CMD ["nginx", "-g", "daemon off;"]
```
<br>

## buildkit 관련 명령어
- BuildKit 버전 확인
```bash
$ docker buildx version
```

- BuildKit 캐시 마운트 확인
- 캐시 관련 메시지가 출력되지 않는다면, BuildKit 캐시 마운트가 설정되지 않았거나 작동하지 않은 것
```bash
$ docker buildx build --progress=plain -t <이미지명> .
```


- 빌드 캐시 상태 확인: 캐시가 저장되어 있는지 확인
```bash
$ docker builder du
#또는 
$ docker buildx du
```

- 사용하지 않는 빌드 캐시 삭제
```bash
$ docker builder prune
```


<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>