---
layout: single
title:  "VSCode - Markdown Preview 안 열릴 때(feat. 파일 복사도 안됨)"
categories:
  - VSCode
tags:
  - [VSCode, Markdown]

toc: true
toc_sticky: true
---


## could not register service workers invalidstateerror 에러 발생.

- 잘 사용하던 Markdown 파일의 `Preview` 기능이 갑자기 에러를 내면서 작동이 되지 않았다.
- `파일 복사`도 로딩만 계속 돌고 작동하지 않았다.
- 이런 문제는 원인을 굳이 파악하기 보단 해결방법만 숙지 후 사용하자.

## Cashe 파일 삭제

1. 우선 VSCode가 열려 있다면 닫아주자.
2. 아래 경로로 이동 후 Cashe 관련 파일 삭제해 주자.
   - `C:\Users\사용자명\AppData\Roaming\Code` 로 이동 후 아래 폴더 중 존재하는 폴더 모두 삭제
   - `Cache`, `CachedData`, `CachedExtensions`, `CachedExtensionVSIXs` `Code Cache`

3. VSCode 다시 실행.



