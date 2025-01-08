---
layout: single
title:  "npm run build 실행 과정"
categories:
  - Gradle
tags:
  - [Gradle, React-build]

toc: true
toc_sticky: true
---

### 1. 빌드 프로세스 시작
- npm run build는 실제로 react-scripts build 명령을 실행합니다
- 이 명령은 프로덕션용 최적화 빌드를 수행합니다

### 2. 빌드 결과물 생성 위치
- 모든 결과물은 프로젝트의 /build 디렉토리에 생성됩니다
- 기존 build 폴더가 있다면 삭제하고 새로 생성합니다

### 3. 생성되는 주요 파일들
```plaintext
build/
├── static/
│   ├── css/
│   │   ├── main.[hash].css        # 최적화된 CSS 파일
│   │   └── main.[hash].css.map    # 소스맵
│   ├── js/
│   │   ├── main.[hash].js         # 최적화된 JS 번들
│   │   └── main.[hash].js.map     # 소스맵
│   └── media/                      # 이미지 등 미디어 파일
├── asset-manifest.json            # 모든 에셋의 경로 정보
├── index.html                     # 최적화된 HTML 파일
├── favicon.ico
└── manifest.json                  # PWA를 위한 매니페스트
```

### 4. 최적화 과정
- JS/CSS 파일 압축 (minification)
- 중복 코드 제거 (code splitting)
- 정적 파일 최적화
- 캐싱을 위한 파일명 해싱
- 소스맵 생성


### 5. 결과물 특징
- 모든 파일은 프로덕션 배포에 최적화됨
- 정적(static) 파일들로만 구성
- 서버 없이도 웹 서버(nginx 등)로 바로 서비스 가능