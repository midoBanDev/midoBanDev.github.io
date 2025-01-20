---
layout: single
title:  "Github Actions Workflows"
categories:
  - Github
tags:
  - [Github, Workflows]

toc: true
toc_sticky: true
---

# 워크플로우(workflow)
워크플로우(workflow)는 CI/CD 파이프라인에서 작업을 실행하는 자동화된 프로세스로, 파이프라인의 실행 계획을 의미한다.
워크플로우는 `.github/workflows` 디렉터리에 YAML 파일로 정의된다. 

워크플로우 파일은 프로젝트 폴더 아래 `.github/workflows` 디렉터리를 만든 후 안에 저장해야 한다.

<div style="padding-top:20px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:20px;"></div>


# 워크플로우(workflow) 구문

>워크플로우 구문에 이해를 돕기 위해 구성 요소에 대한 설명 시 자유롭게 작성 가능한 요소와 참조 값을 사용해야야 되는 요소로 구분했다.  
`<custom>`은 **자유롭게 작성**할 수 있는 요소를 의미한다.  
`<ref>`는 **정해져 있는 참조 값**만 사용해야 되는 요소를 의미한다.  
아무 표시도 없는 요소는 하위 요소를 포함하는 상위 요소의 역할을 나타낸다.

```yaml
name: backend workflows  # 워크플로우의 이름

# 워크플로우가 실행되는 조건
on:
  push:
    branches:   # main 브랜치에 push될 때
      - main

# 실행할 작업들을 정의
jobs:
  job:  # 작업 이름
    runs-on: ubuntu-latest  # 실행될 환경

    # 작업의 단계들
    steps:
      - name: Checkout repository  # 단계 이름
        uses: actions/checkout@v2  # 사용할 액션
```

주요 구성 요소

## name `<custom>`
워크플로우의 이름을 정의한다. 자유롭게 작성하면 된다.

<div style="padding-top:20px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:20px;"></div>

## on
워크플로우가 실행될 트리거를 정의한다.  
push, pull_request, schedule 등 다양한 이벤트를 지정할 수 있다.  
  
### branches `<ref>`  
지정된 브랜치에 push 된 경우 트리거로 작동한다.

<div style="padding-top:20px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:20px;"></div>

## jobs
실행 할 작업들을 정의하는 최상위 요소이다. 하위 요소로 job을 정의할 수 있다.

### job `<custom>`
각 job은 독립된 환경에서 실행된다. `job`부분을 실행될 작업을 의미하는 이름으로 자유롭게 지정하면 된다.
`runs-on`에 지정된 환경의 가상머신 또는 컨테이너에서 실행된다.

   
### runs-on `<ref>`  
job이 실행되는 환경을 지정한다.  
실행 환경은 github의 러너(runner)에서 실행되며, Github 러너(runner)는 크게 `Github 호스팅 러너(Github-hosted runner)` 와 `셀프 호스팅 러너(self-hosted runner)`로 구분된다.  
사용하는 러너에 맞게 정의해야 한다. 
([Github 공식 문서 - 실행 환경 ](https://docs.github.com/en/actions/writing-workflows/choosing-where-your-workflow-runs/choosing-the-runner-for-a-job?utm_source=chatgpt.com))
- Ubuntu: ubuntu-latest, ubuntu-22.04, ubuntu-20.04
- Windows: windows-latest, windows-2022, windows-2019
- macOS: macos-latest, macos-12, macos-11

<div style="padding-top:20px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:20px;"></div>

## steps
작업의 실행 단계를 정의하는 요소이다. 
하위 `uses`를 단위로 실행될 작업을 정의할 수 있다.

```yaml
jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      # 소스 체크아웃
      - id: checkout
        name: Checkout Repository
        uses: actions/checkout@v3
          repository: my-repo
          ref: main

      # Node.js 설정
      - id: setup-node
        name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
          cache: 'npm'
```

### id `<custom>`    
step을 식별하기 위한 고유 식별자를 지정한다. 
step은 `-` 하이픈을 기준으로 구분된다.

### name `<custom>`    
실행 할 단계의 작업 이름을 지정한다.  

### uses `<ref>`  
실행 할 작업의 Actions을 정의한다. 
Actions란 워크플로우에서 실행하는 `작업 단위`로 특정 작업을 자동화하기 위해 설계된 `코드 패키지`라 보면 된다. 
예를 들어 `github에서 코드를 체크아웃 하기`, `Docker 이미지 빌드하기`, `애플리케이션 빌드하기`, `테스트 스크립트 실행하기` 등을 할 수 있다.  

Actions는 공식 Actions을 사용할 수도 있고, 다른 사람이 만들어 놓은 Actions를 사용할 수도 있고, 직접 Actions를 만들어서 사용할 수도 있다.
일반적으로는 Github에서 제공하는 액션을 사용한다. ([Github Marketplace](https://github.com/marketplace?type=actions))
하지만 CI/CD 과정에서 별도의 테스트 작업이나 특별한 작업을 수행하고자 할 경우 Actions를 직접 만들어 활용할 수 있다. 

Actions을 활용하면 반복적인 작업을 자동화하여 쉽고 효율적으로 처리할 수 있다.

### with 
action에 전달할 입력 매개변수를 설정한다.   
**Actions의 입력값(input)**을 설정하는 데 사용되기 때문에 uses에 정의된 actions에 따라 사용되는 옵션 값이 달라진다. 
그래서 action 별로 사용되는 주요 변수를 익혀두는 것이 좋다. 여기서는 별도의 변수 설명은 하지 않는다. 

([Github Marketplace](https://github.com/marketplace?type=actions))에 접속해서 actions별 정의된 변수를 확인할 수 있다.

<div style="padding-top:20px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:20px;"></div>

## env `<custom>`
env는 워크플로우 내에서 사용할 환경 변수를 설정한다. 
`env`의 설정 위치에 따라 적용되는 범위가 다르다.

기본 YAML 문법은 `키-값`을 정의할 때 콜론을 사용한다.

<br>

### 워크플로우에서 사용

**1. `env`가 step 레벨에서 정의된 경우:**  
```yaml
steps:
  - name: HI
    env:
      VAR_NAME: "value"
    run: echo $VAR_NAME
    
  - name: Hello
    run: echo $VAR_NAME
```
- **해당 step에만 적용**된다.
- step 내에서 실행되는 모든 명령어(`run`) 또는 액션(`uses`)에서 환경 변수를 사용할 수 있다.
- 첫 번째 step에서는 `VAR_NAME`을 사용할 수 있지만, 두 번째 step에서는 사용할 수 없다.

<br>

**2. `env`가 job 레벨에서 정의된 경우:**   
```yaml
jobs:
  example-job:
    runs-on: ubuntu-latest
    
    env:
      VAR_NAME: "value"
    
    steps:
      - name: First step
        run: echo $VAR_NAME
    
      - name: Second step
        uses: actions/checkout@v3
        run: echo $VAR_NAME
```
- **해당 job의 모든 step에 적용**된다.
- `VAR_NAME`은 모든 step에서 접근 가능하다. `run` 명령어와 `uses`로 호출된 액션 모두에서 사용할 수 있다.

<br>

**3. `env`와 `uses`의 관계:**  
```yaml
steps:
  - name: Use an action with env variable
    env:
      VAR_NAME: "value"
    uses: username/repo-name@v1
```
- `uses`로 호출된 액션 내부에서도 환경 변수를 사용할 수 있다.  
- 액션이 환경 변수를 참조하도록 설계된 경우 해당 환경 변수가 적용된다.
- `username/repo-name` 액션에서 `VAR_NAME`이 필요하다면 전달된다.


### 도커파일(Dockerfile)에 전달
워크플로우에서는 build-args와 env로 환경 변수를 설정할 수 있다.
- **`build-args`**: 빌드 시점에 사용할 환경 변수를 설정할 수 있다.  
- **`env`**: 런타임에 전달할 환경 변수를 설정할 수 있다.


Dockerfile에는 **`ARG`**와 **`ENV`**라는 환경 변수 지시어가 존재한다.
- **`ARG`**: 빌드 과정에서만 사용되는 환경 변수를 지정할 때 사용된다.  
- **`ENV`**: 컨테이너 런타임에서 어플리케이션 내부에서 사용될 환경 변수를 지정할 때 사용된다.


워크플로우에서 Dockerfile로 환경 변수를 전달할 수 있다.
- `build-args`로 설정된 환경 변수 값을 도커 파일로 전달할 수 있다. 이때 Dockerfile에는 반드시 **`ARG` 지시어로 환경 변수를 선언**해야 값을 받을 수 있다.  
- 어플리케이션에서 사용 할(런타임 시 사용 할) 환경 변수가 필요한 경우, **`build-args`를 사용해 ARG로 값을 받고 이를 ENV로 전달**해야 된다.
- 참고로 `env`에 설정된 값은 도커 파일로 전달되지 않는다. 

워크플로우에서 컨테이너 실행 명령까지 진행하는 경우가 아니고, 이미지만 빌드 후 컨테이너 실행은 따로 진행하는 경우에는 이미지 안에 환경 변수를 포함시키거나, 컨테이너 실행 시점에 직접 환경 변수를 넘겨줘야 한다.

이미지에 환경 변수를 포함시키려면 위에서 말한 바와 같이 `build-args`를 사용하면 된다.

```yaml
name: Docker Build

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:       
      # Docker 빌드 및 푸시
      - name: Build and push
        uses: docker/build-push-action@v5
        with:
          # 환경변수 전달 방법 1: build-args로 전달
          build-args: |
            API_URL=${{ secrets.API_URL }}
            DB_HOST=${{ vars.DB_HOST }}
            NODE_ENV=production
          # 환경변수 전달 방법 2: 런타임 환경변수로 전달
          env: |
            RUNTIME_VAR1=${{ secrets.RUNTIME_SECRET }}
            RUNTIME_VAR2=${{ vars.RUNTIME_VARIABLE }}
```

```dockerfile
# build-args로 전달된 값을 런타임시 사용하려면 아래와 같이 ENV로 전달해줘야 한다.
ARG DB_HOST
ENV DB_HOST=${DB_HOST}

# env로 전달된 값은 이렇게 변수만 선언해도 값이 전달된다.
ENV RUNTIME_VAR1
ENV RUNTIME_VAR2
```

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

```yaml
steps:
  - id: build
    name: Build application
    uses: actions/setup-node@v2
    with:
      node-version: '16'
    working-directory: ./frontend
    env:
      NODE_ENV: production
    continue-on-error: false
    timeout-minutes: 10
    run: |
      npm install
      npm run build
```

## continue-on-error `<ref>`
continue-on-error는 step의 작업을 실행하는 중 오류가 발생했을 때 워크플로우를 계속 실행할지 여부를 설정할 수 있다.
기본값이 false 이기 때문에 별도의 설정이 없으면 오류가 발생하면 워크플로우는 중단된다. 
`true`로 설정하면 이 step이 실패해도 다음 step이 실행된다.


## timeout-minutes `<custom>`
step의 최대 실행 시간을 설정할 수 있다. 
분 단위로 설정되고, 기본 값은 360분이다. 
지정된 시간을 초과하면 step이 자동으로 중단된다.


## run `<custom>`
shell에서 실행될 명령어를 지정할 수 있다.
`|` 파이프를 사용하면 여러 줄의 명령어로 실행할 수 있다.


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

# 보안 설정
워크플로우가 실행되면 GitHub 리소스에 접근하여 필요한 자원을 사용한다. 
이는 워크플로우를 통해 원하는 작업을 손쉽게 할 수 있는 큰 장점이다. 
하지만 반대로 말하면 워크플로우가 보안 리스크의 시발점이 될 수 있음을 시사한다. 

워크플로우는 아래와 같은 작업들이 가능하다.  
- repository의 모든 secrets에 접근 가능
- 다른 브랜치 생성/삭제 가능
- issues 생성/수정/삭제 가능
- PR 생성/수정/닫기 가능
- GitHub Packages에 패키지 업로드/삭제 가능

즉 워크플로우 통해 내가 아닌 누군가도 위와 같은 것들을 할 수 있다는 말이 된다. 
보안 위험 사례로는 워크플로우에서 사용되는 actions나 패키지(npm, pip 등 의존성 패키지) 등의 악의적인 코드가 심어져 있는 경우나 
PR시 악의적인 코드를 push한 경우 등이 있을 수 있다.

따라서 워크플로우 보안을 위한 기본적인 설정들은 반드시 필요하다.


## permissions
`permissions`는 워크플로우가 GitHub 리소스에 접근할 수 있는 권한 범위를 정의하는 설정이다. 
permissions는 크게 워크플로우 레벨과 job 레벨로 정의할 수 있다.

**워크플로우 레벨** 
워크플로우 레빌로 설정할 수 있는 스코프는 `write-all`와 `read-all` 이다. 
- **write-all**: 읽고, 수정하고, 삭제하는 등의 모든 권한을 가짐.
- **read-all**: 리소스를 읽을 수만 있음.
- permissions를 명시하지 않으면 기본값은 `write-all`이다.

```yaml
# 워크플로우 전체에 대한 기본 권한 설정
name: CI Pipeline
permissions: read-all  # 또는 write-all

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
```

**Job 레벨 설정** 
Job 기준으로 권한을 설정할 수 있다.
```yaml
name: CI Pipeline
permissions: read-all  # 기본적으로 모든 권한 읽기로 제한

jobs:
  test:
    runs-on: ubuntu-latest
    # job별로 필요한 권한만 추가
    permissions:
      contents: read
      pull-requests: write
```

- Job 스코프
  
```yaml
# Job 스코프
permissions:
  actions: read|write|none       # GitHub Actions 관련
  checks: read|write|none        # 체크 실행 결과
  contents: read|write|none      # 리포지토리 컨텐츠
  deployments: read|write|none   # 배포 관련
  issues: read|write|none        # 이슈 관련
  packages: read|write|none      # 패키지 관련
  pull-requests: read|write|none # PR 관련
  repository-projects: read|write|none  # 프로젝트 관련
  security-events: read|write|none      # 보안 이벤트
  statuses: read|write|none      # 커밋 상태
```

기본적인 전략은 워크플로우의 전체 권한은 읽기 전용으로 설정하고, 
각 Job별로 필요한 권한만 추가하면 된다.

```yaml
name: CI/CD Pipeline
# 기본적으로 모든 권한을 읽기로 제한
permissions: read-all

jobs:
  test:
    runs-on: ubuntu-latest
    # 테스트 job은 코드를 읽고, 결과를 PR에 코멘트만 하면 됨
    permissions:
      contents: read
      pull-requests: write
    steps:
      - uses: actions/checkout@v3
      - run: npm test

  security-scan:
    runs-on: ubuntu-latest
    # 보안 검사 job은 코드를 읽고, 보안 결과를 등록
    permissions:
      contents: read
      security-events: write
    steps:
      - uses: actions/checkout@v3
 

  deploy:
    runs-on: ubuntu-latest
    # 배포 job은 패키지 배포와, 배포 상태 업데이트 필요
    permissions:
      contents: read
      packages: write
      deployments: write
    steps:
      - uses: actions/checkout@v3

```

<br>

## pull_request
GitHub Actions의 pull_request 이벤트는 Pull Request(PR)와 관련된 다양한 활동이 발생했을 때 워크플로우를 실행하도록 트리거하는 이벤트이다.

누군가 악의적인 PR을 올렸을 때 위험을 줄이기 위해서는 push 이벤트만 사용하는 방법은 자제하고 pull_request를 거치도록 전략을 짜는 것이 좋다.
이는 단순히 보안을 위해서 뿐만 아니라 개발자들의 실수를 줄이기 위한 방법으로도 충분히 활용할 수 있다.

**기본 정의**  
```yaml
on:
  pull_request:    # PR 관련 모든 이벤트에 반응
    branches:      # 특정 브랜치로의 PR만 감지
      - main
      - dev
    types:         # 특정 PR 액션만 감지
      - opened     # PR이 생성될 때
      - synchronize # PR이 업데이트될 때
      - reopened   # PR이 다시 열릴 때
```

**주요 types:**
타입 설정으로 다양한 PR 이벤트에 맞는 설정을 할 수 있다.
- opened: PR이 처음 생성될 때
- synchronize: PR에 새로운 커밋이 추가될 때
- reopened: 닫혔던 PR이 다시 열릴 때
- closed: PR이 닫히거나 머지될 때
- ready_for_review: 드래프트 PR이 리뷰 준비 상태가 될 때

types를 지정하지 않으면 opened, synchronize, reopened가 기본값으로 설정된다.

**주의사항:** 
fork된 레포지토리에서의 PR은 보안상 추가 검증이 필요할 수 있다. 
민감한 secrets는 PR 환경에서 노출되지 않도록 주의해야 한다.


### 기본 전략
GitHub Actions은 PR이 요청된 브랜치의 코드와 대상 브랜치(예: main)를 자동으로 병합한 상태에서 워크플로우를 실행한다. 
작동 방식은 아래와 같다:  
- PR이 생성되면 GitHub은 가상의 병합 커밋(merge commit)을 생성한다.
- `actions/checkout@v4` 액션은 이 가상 병합 상태의 코드를 체크아웃 한다.
- 따라서 빌드나 테스트는 **"PR이 실제로 병합되었을 때의 상태"**를 미리 검증할 수 있다.

**기본 전략은 다음과 같다.**  
- 별도의 dev 브랜치를 생성하고, main 브랜치로 직접적인 push는 막는다.
- main 브랜치로는 PR을 통해서만 push 할 수 있다.
- dev 브랜치에서 main 브랜치로 PR이 생성하면 기본적인 검증을 진행한다. 이 시점에 PR 워크플로우 job이 실행된다.
- PR 워크플로우 검증이 성공하면 병합 후 main 브랜치로 push가 되면서 실제 job을 실행한다.


PR(Pull Request) 워크플로우 파일을 추가로 만든다.
이렇게 하면 유지보수에 유리하다.  

**적용할 서비스: Gradle 기반 API Server**  
- main 브랜칠

```yaml
name: PR Pipeline

on:
 pull_request:
   branches: 
     - main

jobs:
  pull_request_build_test:
    runs-on: ubuntu-latest  
    steps:
    - 
      name: Checkout Repository
      uses: actions/checkout@v4  
      # 현재 리포지토리를 체크아웃합니다.

    - 
      name: Set up Docker Buildx
      uses: docker/setup-buildx-action@v3  
      # Docker Buildx를 설정합니다.

    - 
      name: Build and Push
      uses: docker/build-push-action@v2
      with:
        # 빌드 컨텍스트 : Dockerfile이 있는 위치
        context: .
        # Dockerfile의 경로
        file: Dockerfile
        build-args: |
            GOOGLE_CLIENT_ID=${{ secrets.GOOGLE_CLIENT_ID }}
        # 이미지 푸시 여부
        push: false  

    -
      name: Notify Failure
      if: failure()
      run: echo "Docker build failed."
```

### 브랜치 룰셋 설정
PR(Pull-Request) 생성 시 PR 워크플로우를 실행하기 위해 룰셋을 생성해야 한다.

**1. 레포지토리 > Settgins > Branches > "Add branch ruleset" 클릭**  
<img src="https://github.com/user-attachments/assets/e0da0302-795b-4a04-b48a-870864d30fd7" width="90%" height="80%"/>

**2. "Ruleset Name" 이름 작성한다.**  
**3. Target branches > Add target을 설정한다.**  
- Target은 룰셋을 적용할 브랜치를 선택하는 과정이다.
- Add target에 적용할 브랜치는 "Include by pattern"에 직접 브랜치명을 적어줘야 한다.
  
<img src="https://github.com/user-attachments/assets/6098aa12-b60f-4d79-ae55-f3f7c8c032a2" width="90%" height="80%"/>

**4. "Require a pull request before merging" 체크. 추가 항목은 일단 패스**  
- 룰셋이 적용된 브랜치는 병합 전 pull request를 필수로 해야 한다는 의미이다. 직접 push가 불가능하게 만들어 준다.
  
<img src="https://github.com/user-attachments/assets/42945ab0-4487-450a-acc1-40fb3682f003" width="90%" height="80%"/>

**5. "Require status checks to pass" 체크.**  
- 하위 항목 "Require branches to be up to date before merging" 체크
- "Add checks"는 실행할 워크플로우 job을 등록하는 항목이다.
- 실행할 워크플로우 job 이름을 입력하면 자동 검색된다. 검색된 job을 선택해준다.
  
<img src="https://github.com/user-attachments/assets/5a663c2d-3c5e-42d7-a52e-3c1c9b55b840" width="90%" height="80%"/>








<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>



# Actions 시크릿(Secrets)과 변수(Variables)
GitHub Actions에서 **Secrets**와 **Variables**는 워크플로우의 효율성과 보안을 높이기 위해 사용되는 두 가지 주요 개념이다.
이러한 기능을 활용하면 워크플로우의 **보안**과 **유연성**을 높일 수 있다. 

## Secrets (비밀)

**Secrets**는 API 키, 비밀번호 등과 같은 **민감한 정보**를 안전하게 저장하고 워크플로우에서 사용할 수 있도록 도와준다.

**설정 수준:**  
- **리포지토리 수준:** 특정 리포지토리 내에서만 사용 가능.
- **환경(Environment) 수준:** 배포 환경별로 설정하여 세분화된 관리 가능.
- **조직(Organization) 수준:** 조직 내 여러 리포지토리에서 공유 가능.

**사용 방법:**
1. 리포지토리의 **Settings** > **Secrets and variables** > **Actions**로 이동한다.
2. **Secrets** 탭에서 **New repository secret**을 클릭한다.
3. 이름과 값을 입력하고 **Add secret**을 클릭하여 저장한다.

**워크플로우에서 참조:**  
```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v3
      - name: Use secret
        run: echo "Secret is ${{ secrets.SECRET_NAME }}"
        env: |

```
- 위 예시에서 `${{ secrets.SECRET_NAME }}`를 통해 저장된 시크릿을 참조할 수 있다.
- 시크릿 값은 GitHub 인터페이스에서 조회할 수 없으며, 한 번 설정하면 수정 또는 삭제만 가능하다.
- 워크플로우 로그에 시크릿이 노출되지 않도록 주의해야 하며, GitHub는 로그에서 시크릿 값을 자동으로 마스킹 처리한다.


## **Variables (변수)**
**Variables**는 민감하지 않은 구성 값이나 설정 정보를 저장하는 데 사용된다.

**설정 위치:**
- **리포지토리 수준:** 해당 리포지토리 내에서 사용 가능.
- **환경(Environment) 수준:** 특정 환경에 맞는 변수 설정 가능.

- **사용 방법:**
1. 리포지토리의 **Settings** > **Secrets and variables** > **Actions**로 이동한다.
2. **Variables** 탭에서 **New repository variable**을 클릭한다.
3. 이름과 값을 입력하고 **Add variable**을 클릭하여 저장한다.

**워크플로우에서 참조:**
```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v3
      - name: Use variable
        run: echo "Variable is ${{ vars.VARIABLE_NAME }}"
```
- 위 예시에서 `${{ vars.VARIABLE_NAME }}`를 통해 저장된 변수를 참조할 수 있습니다.
- 변수 값은 GitHub 인터페이스에서 **조회 및 수정**이 가능합니다.
- 민감한 정보는 변수 대신 시크릿에 저장해야 합니다.

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

# 환경 변수 키-값 콜론(`:`)과 등호(`=`)의 사용 규칙

## 기본 규칙

### 내부 처리용 YAML 구문 (콜론 `:` 사용)
- 워크플로우 내부에서만 사용되거나 actions으로 전달되는 환경 변수는 YAML의 기본 문법을 따라 **콜론 (`:`)**을 사용한다.
  
```yaml

jobs:
  job:
    env:
      MODE_ENV: "dev" 

      steps:

        # 1. Shell 명령어에서 사용
        - name: Use in shell
          run: echo "Current mode is ${{ env.MODE_ENV }}"
        
        # 2. 조건문에서 사용
        - name: Conditional step
          if: env.MODE_ENV == 'dev'
          run: echo "Development mode specific tasks"

        # 3. Composite Action에 전달
        - name: Use in composite action
          uses: ./my-composite-action
          with:
            environment: ${{ env.MODE_ENV }}

        # 시크릿 값 actions으로 전달
        - name: Login to Docker Hub
          uses: docker/login-action@v3
          with:
            username: ${{ secrets.DOCKERHUB_USERNAME }}
            password: ${{ secrets.DOCKERHUB_TOKEN }}
```


### 외부 전달용 할당 구문 (등호 `=` 사용)
- Docker나 shell과 같은 외부 시스템으로 전달하는 목적으로 환경 변수를 설정하는 경우가 있다.
- 이때는 YAML 문법이 아닌 shell 명령어 형식을 따라 **등호(`=`)**를 사용해서 환경 변수를 매핑해야 한다.
- `build-args`, `run` 는 CLI의 **Shell 명령어 스타일**로 처리되므로, **등호(`=`)**를 사용해야 한다.

```yaml
name: Build and Push
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Build and push
        uses: docker/build-push-action@v5
        with:
          context: .
          push: true
          build-args: |
            DB_PASSWORD=${{ secrets.DB_PASSWORD }}
```

### 다중 문자열 (등호 `=` 사용)
다중 문자열을 처리하기 위해 `파이프(|)`를 사용한다. 
`파이프(|)`를 사용하면 Shell 명령어로 처리되기 때문에 `등호(=)`를 써야 한다.

```yaml
# env 기본 문법 콜론(:) 사용
env:
  VAR_NAME_1: "value"
  VAR_NAME_2: "value"
  VAR_NAME_3: "value"

# 다중 문자열 입력을 위해 파이프(|)를 사용하는 경우 등호(=) 사용
env: |
  VAR_NAME_1="value"
  VAR_NAME_2="value"
  VAR_NAME_3="value"
```

## 환경변수 전달 시나리오

### 시나리오 1: 워크플로우 → Compose → Docker
컨테이너 실행 시점에 환경변수 주입 (Dockerfile에 `ENV 불필요`)

**1. GitHub Actions Workflow**  
```yaml
name: Deploy with Compose
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - name: Deploy
        env:
          DB_PASSWORD=${{ secrets.DB_PASSWORD }}
        run: docker-compose up -d
```

**2. docker-compose.yml**  
```yaml
services:
  app:
    image: myapp
    environment:
      - DB_PASSWORD=${DB_PASSWORD}
```

**3. Dockerfile**  
```dockerfile
FROM node:16
# ENV 설정 불필요 (Compose가 실행 시 주입)
```

### 시나리오 2: 워크플로우 → Dockerfile → 이미지
도커 파일에 환경 변수를 전달하기 위해서는 `build-args`를 사용해야 한다. 
`env`에 설정한 환경 변수 값은 도커 파일로 전달되지 않는다. 

**1. GitHub Actions Workflow**  
```yaml
name: Build and Push
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Build and push
        uses: docker/build-push-action@v5
        with:
          context: .
          push: true
          build-args: |
            DB_PASSWORD=${{ secrets.DB_PASSWORD }}
```

**2. Dockerfile**  
```dockerfile
FROM node:16
ENV DB_PASSWORD=""  # 필수! (이미지에 포함)
```

## 정리
- GitHub Actions 내부 설정 → 콜론(`:`) 사용
  - `with` 섹션의 모든 설정
  - 워크플로우 자체 환경변수
- 외부 시스템 전달 → 등호(`=`) 사용
  - Docker 빌드 인자
  - 컨테이너 환경변수
  - Shell 스크립트 변수

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>



# 멀티 환경(Multi Environments)
## environment `<ref>`
GitHub Actions에서 **Environments**는 배포 대상(예: 프로덕션, 스테이징, 개발 등)을 정의하고 관리하는 기능이다. 
이를 통해 실제 서비스를 운영할 때 각 환경에 맞게 배포 프로세스를 제어하고, 
환경 별로 다른 시크릿(Secrets)과 변수(Variables)를 설정하며, 승인 절차나 보호 규칙을 적용할 수 있다. 
>Docker Compose에서 Override를 활용해 환경별 파일을 관리하던 부분을 대체할 수 있다.

예를 들어 개발 환경과 운영 환경이 있다고 가정해보자. 
두 환경이 갖는 내부 설정 값은 다르게 적용되어야 다른 환경을 구성할 수 있다. 
environment를 사용하면 이런 환경 구성을 하나의 워크플로우 파일 안에서 설정할 수 있다.

우선 environment를 사용하려면 github 레포지토리에 등록을 해야한다. 
- 리포지토리의 [Settings] 탭에서 [Environments]를 선택한다.
- [New environment]를 클릭하여 새로운 환경을 생성하고, 이름을 지정한다.  
<img src="https://github.com/user-attachments/assets/78a0250f-7b47-41f9-b2a3-4c5b84810751" width="90%" height="80%"/>

사전에 등록된 environment `name`을 워크플로우의 job 단위로 지정할 수 있다.

```yaml
name: Deploy Workflow

on:
  push:
    branches:
      - main

jobs:
  deploy-development:
    runs-on: ubuntu-latest
    # 개발 환경 정의
    environment: development
    steps:
      - name: Deploy to Development
        with:
          build-args: |
            API_URL=${{ secrets.API_URL }}
            DB_HOST=${{ vars.DB_HOST }}
            NODE_ENV=development
          env: |
            RUNTIME_VAR1=${{ secrets.RUNTIME_SECRET }}
            RUNTIME_VAR2=${{ vars.RUNTIME_VARIABLE }}

  deploy-production:
    runs-on: ubuntu-latest
    # 운영 환경 정의
    environment: 
      name: production
    steps:
      - name: Deploy to Production
        with:
          build-args: |
            API_URL=${{ secrets.API_URL }}
            DB_HOST=${{ vars.DB_HOST }}
            NODE_ENV=production
          env: |
            RUNTIME_VAR1=${{ secrets.RUNTIME_SECRET }}
            RUNTIME_VAR2=${{ vars.RUNTIME_VARIABLE }}
```

## Environments의 시크릿(Secrets)과 변수(Variables)
환경 별 시크릿과 변수를 등록할 수 있다.
<img src="https://github.com/user-attachments/assets/0eaa7310-5a00-4eb1-b396-da1a8338ba6e" width="90%" height="80%"/>


이렇게 environment로 지정된 job들은 각각의 environment에 등록된 시크릿(secrets)과 변수(variables)를 사용할 수 있다.   
<img src="https://github.com/user-attachments/assets/39ed48f5-f358-45bf-b4c2-0da3ce516eef" width="90%" height="80%"/>


### name `<ref>`
environment에 값을 직접 지정할 수도 있고, name을 사용하여 지정할 수도 있다.

<br>

## Environments의 시크릿과 변수 사용

등록된 environment를 지정한 job에 environment에 등록된 시크릿과 변수를 참조할 수 있다.
- `secrets.변수명`
- `vars.변수명`

레포지토리 수준에 등록된 시크릿과 변수는 워크플로우 내의 모든 job에서 참조할 수 있다. 접근 방법은 동일하다.
- `secrets.변수명`
- `vars.변수명`


<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

# 멀티 환경 워크플로우 실행
## if `<custom>`
- step의 실행 조건을 정의할 때 사용한다. 
- 조건이 true일 때만 step이 실행된다.


```yaml
name: Deploy Workflow

on:
  push:
    branches:
      - develop
      - main

jobs:
  deploy-development:
    runs-on: ubuntu-latest
    # develop 브랜치에 push 된 경우
    if: github.ref == 'refs/heads/develop'
    # 개발 환경 정의
    environment: development
    steps:
      - name: Deploy to Development
        with:
          build-args: |
            API_URL=${{ secrets.API_URL }}
            DB_HOST=${{ vars.DB_HOST }}
            NODE_ENV=development
          env: |
            RUNTIME_VAR1=${{ secrets.RUNTIME_SECRET }}
            RUNTIME_VAR2=${{ vars.RUNTIME_VARIABLE }}

  deploy-production:
    runs-on: ubuntu-latest
    # main 브랜치에 push 된 경우
    if: github.ref == 'refs/heads/main'
    # 운영 환경 정의
    environment: production
    steps:
      - name: Deploy to Production
        with:
          build-args: |
            API_URL=${{ secrets.API_URL }}
            DB_HOST=${{ vars.DB_HOST }}
            NODE_ENV=production
          env: |
            RUNTIME_VAR1=${{ secrets.RUNTIME_SECRET }}
            RUNTIME_VAR2=${{ vars.RUNTIME_VARIABLE }}
```

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

# 워크플로우 수동 실행

## workflow_dispatch 
**workflow_dispatch**는 GitHub Actions에서 사용자가 수동으로 워크플로우를 실행할 수 있도록 입력 파라미터를 제공하는 이벤트다.
workflow_dispatch를 설정하면 Actions 페이지에서 워크플로우를 실행 할 때 environment 입력 값을 설정하는 UI를 표시할 수 있다.
Actions 페이지에서 "Run workflow" 버튼을 통해 실행할 수 있다.

예를 들어 멀티 환경이 설정된 워크플로우(한 번에 모든 환경을 실행할 수 없게 설정된 경우)를 수동으로 실행할 경우에는 모든 환경에 배포되도록 설정할 수 있다. 
아래 워크플로우는 다음과 같은 상황에 실행된다.
- develop 브랜치에 push 된 경우 **deploy-development** job이 실행된다.
- main 브랜치에 push 된 경우 **deploy-production** job이 실행된다.
- 워크플로우가 수동으로 실행될 때 입력된 파라미터가 **development** 인 경우 **deploy-development** job이 실행된다.
- 워크플로우가 수동으로 실행될 때 입력된 파라미터가 **production** 인 경우 **deploy-production** job이 실행된다.
- 워크플로우가 수동으로 실행될 때 입력된 파라미터가 **all** 인 경우 **deploy-development** job과 **deploy-production** job이 모두 실행된다.

```yaml
name: Deploy
on:
  push:
    branches: ["develop", "main"]
  workflow_dispatch:
    inputs:
      environment:
        description: 'Deploy to environment'
        required: true
        default: 'all'
        type: choice
        options:
          - all
          - development
          - production

jobs:
  deploy-development:
    if: >
      github.ref == 'refs/heads/develop' || 
      (github.event_name == 'workflow_dispatch' && 
      (github.event.inputs.environment == 'development' || github.event.inputs.environment == 'all'))
    runs-on: ubuntu-latest
    environment: development
    steps:
      - name: Deploy to dev
        run: echo "Deploying to development environment"

  deploy-production:
    if: >
      github.ref == 'refs/heads/main' || 
      (github.event_name == 'workflow_dispatch' && 
      (github.event.inputs.environment == 'production' || github.event.inputs.environment == 'all'))
    runs-on: ubuntu-latest
    environment: production
    steps:
      - name: Deploy to prod
        run: echo "Deploying to production environment"
```

### inputs
배포할 환경의 입력 파라미터를 정의하는 최상위 요소

### environment `<custom>`
입력 파라미터를 식별하는 고유한 이름을 지정한다. 자유롭게 작성하면 된다.

### required `<ref>` 
입력 값의 필수 여부를 설정한다.  
- `true`: 워크플로우 실행 시 반드시 값을 입력해야 한다.  
- `false`: 선택적으로 값을 입력할 수 있다.

### description `<custom>`
어떤 환경인지 사용자가 인지할 수 있도록 자유롭게 설명을 작성한다.

### default
최초 사용자에게 기본으로 보이는 값을 의미한다.
문자열, 숫자, boolean 등 type에 맞는 값을 사용할 수 있다.

### type `<ref>`
입력 될 값의 형태를 지정한다.

`choice | boolean | string | number | environment`

#### choice
미리 정의된 옵션 중에서 선택 (options 필요)

```yaml
type: choice
options:
  - option1
  - option2
```

#### boolean
true/false 선택

```yaml
debug_mode:
  type: boolean
  default: false
```

#### string
자유 텍스트 입력

```yaml
commit_message:
  type: string
  default: 'automated deployment'
```

#### number
숫자 입력

```yaml
timeout_minutes:
  type: number
  default: 30
```

#### environment
GitHub Environments 중 선택

```yaml
target_env:
  type: environment
```

복합 예시
```yaml
workflow_dispatch:
  inputs:
    deploy_target:
      description: 'Deployment target'
      required: true
      type: choice
      options:
        - aws-prod
        - aws-dev
        - gcp-prod
        - gcp-dev
      default: 'aws-dev'
    
    debug_enabled:
      description: 'Enable debug mode'
      required: false
      type: boolean
      default: false
    
    release_version:
      description: 'Version to deploy'
      required: false
      type: string
      default: 'latest'
    
    retry_attempts:
      description: 'Number of retry attempts'
      required: false
      type: number
      default: 3
```

## workflow_dispatch 설정 값 사용
workflow_dispatch에 설정된 값을 job에 전달하여 사용할 수 있다.
입력 파라미터를 식별하기 위해 정의 이름으로 입력 값에 접근할 수 있다.

```yaml
name: Manual Deployment Workflow

on:
  workflow_dispatch:
    inputs:
      environment:
        description: 'Choose deployment environment'
        required: true
        default: 'staging'
      version:
        description: 'Version to deploy'
        required: true
        default: 'latest'
```

- environment로 정의된 입력 파라미터의 입력된 값에 접근
  - `github.event.inputs.environment`
- version 정의된 입력 파라미터의 입력된 값에 접근
  - `github.event.inputs.version`

- `github.event_name`
  - workflow_dispatch 이벤트로 워크플로우가 실행된 경우 **github.event_name**의 값은 항상 workflow_dispatch이다.
  
```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - name: Check out repository
        uses: actions/checkout@v3

      - name: Display selected options
        env:
          VERSION=#{{github.event.inputs.version}}
        run: |
          echo "event name: ${{github.event_name}}"
          echo "Environment: ${{ github.event.inputs.environment }}"
          echo "Version: ${{ github.event.inputs.version }}"

      - name: Deploy to environment
        run: |
          echo "Deploying version ${{ github.event.inputs.version }} to ${{ github.event.inputs.environment }}"
```

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

# Actions 캐시 활용

## GitHub Actions 캐시란?
GitHub Actions의 캐시 시스템은 워크플로우 실행 사이에 종속성이나 빌드 출력물을 저장하고 재사용할 수 있게 해주는 기능이다. 
이를 통해 빌드 시간을 크게 단축할 수 있다.

캐시를 사용하면 빌드 시간 단축, 네트워크 대역폭 절약, 의존성 다운로드 최소화, 빌드 안정성 향상을 도모할 수 있다.

## 캐시 유형

### 1. actions/cache
GitHub에서 공식 제공하는 액션으로, 워크플로 내에서 특정 파일이나 디렉토리를 캐시하여 이후 실행 시 해당 데이터를 재사용함으로써 빌드 시간을 단축한다. 
actions/cache는 종속성 관리 및 빌드 최적화에 매우 유용하며, 적절한 키 관리와 설정으로 빌드 시간을 효과적으로 단축할 수 있다.

파일 시스템 레벨의 캐싱에 사용되며 주로 Node.js의 node_modules, Python의 venv 등 다양한 디렉토리나 파일을 캐시하는 데 사용된다.
```yaml
- uses: actions/cache@v3
  with:
    path: ~/.npm
    key: npm-${{ runner.os }}-${{ hashFiles('**/package-lock.json') }}
```

**동작 방식** 
**1. 저장 단계:**  
- 사용자가 지정한 파일 경로나 디렉토리(path)를 기반으로 데이터를 압축하여 캐시한다.
- 캐시는 **사용자가 정의한 키(key)**를 사용하여 저장된다.
- 동일한 키로 캐시가 이미 존재하면 캐시가 업데이트되지 않는다.

**2. 복원 단계:**  
- 동일한 키(key)를 사용하여 캐시를 복원한다.
- 키가 없거나 일치하지 않으면, `restore-keys`에 지정된 프리픽스 키를 사용하여 대체 캐시를 찾는다.


**특징**  
- 캐시는 GitHub의 내부 캐시 저장소에 저장된다.
- 캐시는 같은 리포지토리 내에서만 공유 및 재사용할 수 있다.
- 각 리포지토리당 최대 10GB까지 캐시를 저장할 수 있다.
- 캐시가 오래되거나 공간이 부족하면 GitHub가 자동으로 제거한다.
- 캐시 키 및 무효화 전략을 사용자 정의해야 하므로, 적절한 키 관리가 필요하다.

<br>

### 2. GHA 캐시
Docker Buildx와 같은 도구에서 BuildKit의 캐시를 GitHub Actions의 내부 캐시 저장소에 저장하고 복원하는 기능을 제공한다.

```dockerfile
# Dockerfile
RUN --mount=type=cache,target=/root/.npm \
    npm install
```

```yaml
# GitHub Actions workflow
- uses: docker/build-push-action@v5
  with:
    context: .
    cache-from: type=gha
    cache-to: type=gha,mode=max
```
GitHub Actions의 내부 캐시 서비스를 사용하여 BuildKit의 캐시 데이터를 저장하고 복원한다.
추가적인 설정 없이 GitHub 인프라를 통해 자동으로 관리된다.

`캐시 저장 (cache-to)`  
- `type=gha`로 설정하면, BuildKit의 캐시 데이터가 GitHub Actions의 중앙 캐시 저장소로 전송되어 저장된다.
- 이는 러너의 로컬 디스크에 저장되는 것이 아니기 때문에, 러너 세션이 종료되어도 캐시 데이터가 유지된다.

`캐시 불러오기 (cache-from)`  
- 빌드 시, GitHub Actions의 중앙 캐시 저장소에서 캐시 데이터를 가져온다.
- 이 데이터는 워크플로우 내에서 BuildKit이 직접 사용한다.

**특징**  
- type=gha 캐시는 동일한 리포지토리 내에서만 공유 및 재사용할 수 있다.
- 다른 리포지토리나 외부 프로젝트와는 캐시를 공유할 수 없다.
- GitHub Actions 환경에서 자동으로 인증되며, 추가적인 인증이나 구성 없이 바로 사용할 수 있다.
- 캐시는 GitHub Actions의 내부 인프라에 저장되며, 리포지토리당 10GB 제한이 적용된다.
- 캐시가 오래되거나 공간이 부족하면 GitHub가 자동으로 삭제할 수 있다.

<br>

### 3 레지스트리 캐시
레지스트리 캐시는 Docker 레지스트리를 사용한 캐싱 방식을 말한다.

```yaml
- uses: docker/build-push-action@v5
  with:
    context: .
    cache-from: type=registry,ref=user/app:cache
    cache-to: type=registry,ref=user/app:cache,mode=max
```

**특징**  
- Docker 레지스트리(예: Docker Hub, GCR, ECR)에 저장된다.
- 여러 저장소나 CI/CD 환경에서 공유 가능하다.
- 레지스트리 인증 필요하다.
- 레지스트리의 스토리지 제한과 비용 정책을 따른다.
- 캐시 이미지가 레지스트리에 별도로 저장되어 추가적인 관리가 필요하다.

예를 들어, 여러 저장소에서 공통으로 사용하는 베이스 이미지의 캐시를 공유하고 싶다면 레지스트리 캐시가 유용할 수 있다. 
반면, 단일 저장소의 빌드 최적화만 필요하다면 GHA 캐시로 충분하다.


<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>