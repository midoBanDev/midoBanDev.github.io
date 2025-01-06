---
layout: single
title:  "VSCode - 프로젝트명 변경(gradle)"
categories:
  - VSCode
tags:
  - [VSCode, IDE, Rename]

toc: true
toc_sticky: true
---


## gradle 프로젝트의 이름 변경

### 1. settings.gradle 수정
- 프로젝트의 바로 아래 디렉터리에 있다.
```Plantext
rootProject.name = '변경할 프로젝트명'
```

### 2. 클래스명 변경
- 메인클래스(`src`)와 테스트클래스(`test`)에 있는 아래 파일을 모두 변경해줘야 한다.
- `src/main/java/com/../프로젝명Applicatino.java` 
- `test/java/com/../프로젝트명ApplicationTest.java`


### VSCode를 사용하는 경우
- 프로젝트 바로 아래 디렉토리에 `.vscode` > launch.json 파일 내용을 수정해 줘야한다.
- `mainClass` 항목의 경로를 수정해줘야 한다. 위 아래 두 군데 수정
- `name`도 바꿔주자. 위 아래 두 군데 수정
```Plantext
{
    "configurations": [
        {
            "type": "java",
            "name": "Spring Boot-변경할프로젝트명Application<변경할프로젝트명>",
            "request": "launch",
            "cwd": "${workspaceFolder}",
            "mainClass": "com.gt.변경할프로젝트명",
            "projectName": "gt-platform",
            "args": "",
            "envFile": "${workspaceFolder}/.env"
        },
        {
            "type": "java",
            "name": "Spring Boot-변경할프로젝트명Application<변경할프로젝트명>",
            "request": "launch",
            "cwd": "${workspaceFolder}",
            "mainClass": "com.gt.GtPlatformApplication",
            "projectName": "loan-manager-api",
            "args": "",
            "envFile": "${workspaceFolder}/.env"
        }
    ]
}
```

