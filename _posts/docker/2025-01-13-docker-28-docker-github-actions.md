---
layout: single
title:  "[Docker] 도커 CI 환경 구성 - Github Actions"
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

## 워크플로우(workflow)
워크플로(workflow)는 CI/CD 파이프라인에서 작업을 실행하는 자동화된 프로세스로, 파이프라인의 실행 계획을 의미한다.
워크플로는 `.github/workflows` 디렉터리에 YAML 파일로 정의된다. 


## 워크플로우 작성 방법
Docker Compose의 내용을 워크플로우에 적용하는 과정을 통해 작성 방법을 알아보자.

```yaml

```


```yaml
name: CI/CD Pipeline  # 워크플로우의 이름

# 워크플로우가 실행되는 조건
on:
  push:
    branches: [ "main" ]  # main 브랜치에 push될 때
  pull_request:
    branches: [ "main" ]  # main 브랜치로 PR이 생성될 때

# 실행할 작업들을 정의
jobs:
  build:  # 작업 이름
    runs-on: ubuntu-latest  # 실행될 환경

    # 작업의 단계들
    steps:
      - name: Checkout repository  # 단계 이름
        uses: actions/checkout@v2  # 사용할 액션
      
      - name: Set up Node.js
        uses: actions/setup-node@v2
        with:
          node-version: '16'
      
      - name: Install dependencies
        run: npm install  # 실행할 명령어
      
      - name: Run tests
        run: npm test
```

주요 구성 요소를 살펴보겠습니다:

1. `name`: 워크플로우의 이름을 정의합니다.

2. `on`: 워크플로우가 실행될 트리거를 정의합니다.
   - push, pull_request, schedule 등 다양한 이벤트 지정 가능
   - 특정 브랜치나 태그로 제한 가능

3. `jobs`: 실행할 작업들을 정의합니다.
   - 각 job은 독립적인 환경에서 실행
   - `runs-on`으로 실행 환경 지정
   - `steps`로 실행할 단계들을 정의

4. `steps`: 각 작업의 실행 단계를 정의합니다.
   - `uses`: 미리 만들어진 액션을 사용
   - `run`: 직접 명령어를 실행
   - `with`: 액션에 전달할 파라미터 지정

더 복잡한 예시를 보여드리겠습니다:

```yaml
name: Deploy to Production

on:
  push:
    tags:
      - 'v*'  # v로 시작하는 태그가 생성될 때

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Run tests
        run: |
          npm install
          npm test

  deploy:
    needs: test  # test job이 성공한 후에 실행
    runs-on: ubuntu-latest
    environment: production
    
    steps:
      - uses: actions/checkout@v2
      
      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v1
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: ap-northeast-2
      
      - name: Deploy to AWS
        run: |
          npm install
          npm run build
          aws s3 sync dist/ s3://my-bucket/
```

이 예시에서는:
- 태그 생성 시 워크플로우 실행
- 테스트와 배포를 별도의 job으로 분리
- 환경 변수와 시크릿 사용
- 여러 명령어를 파이프로 연결
- AWS 인증 정보 설정과 배포 과정을 포함

워크플로우를 작성할 때 주의할 점들:
- 시크릿과 민감한 정보는 GitHub Secrets에 저장하여 사용
- 각 job은 새로운 환경에서 시작되므로, 필요한 설정을 매번 해야 함
- `needs`를 사용하여 job 간의 의존성 설정 가능
- 캐싱을 활용하여 빌드 시간 단축 가능

워크플로우에 대해 더 특정한 부분이나 추가로 알고 싶으신 내용이 있다면 말씀해 주세요.

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>


<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
