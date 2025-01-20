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

## 워크플로우(workflow)
워크플로(workflow)는 CI/CD 파이프라인에서 작업을 실행하는 자동화된 프로세스로, 파이프라인의 실행 계획을 의미한다.
워크플로는 `.github/workflows` 디렉터리에 YAML 파일로 정의된다. 

<br>

## 워크플로우(workflows) - backend 

```yaml
# build-and-push.yml
name: Backend Build and Push

on:
  push:
    branches: [ "main" ]  # main 브랜치에 push될 때
  pull_request:
    branches: [ "main" ]  # main 브랜치로 PR이 생성될 때
      
jobs:
  build-and-push:
    runs-on: ubuntu-latest  
    # 가장 최신의 Ubuntu 러너를 사용합니다.

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
      name: Login to Docker Hub
      uses: docker/login-action@v3
      with:
        username: ${{ secrets.DOCKERHUB_USERNAME }}  
        # GitHub Secret에서 Docker Hub 사용자 이름을 가져옵니다.
        password: ${{ secrets.DOCKERHUB_TOKEN }}     
        # GitHub Secret에서 Docker Hub 액세스 토큰을 가져옵니다.

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
        push: true  # 이미지를 레지스트리에 푸시합니다.
        tags: ${{ secrets.DOCKERHUB_USERNAME }}/loan-manager-api:${{ github.sha }}  
```

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>


## 워크플로우(workflows) - Database

```yaml

```

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
