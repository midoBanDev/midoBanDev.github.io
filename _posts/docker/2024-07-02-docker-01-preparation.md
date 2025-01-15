---
layout: single
title:  "[Docker] Docker 준비하기"
categories:
  - Docker
tags:
  - [Docker, Container]

toc: true
toc_sticky: true
---

## Git 설치 및 실행

### git 설치
- Git download URL : <https://git-scm.com/>
- 'Download for Windows' 클릭하여 다운로드 받은 후 git를 설치해 준다.
  - 설치 시 기본 설정된 'Next' 누르면서 그대로 설치를 하면 된다.

  <img src="https://github.com/user-attachments/assets/8555ec33-1a7f-49d4-938c-9143754a429e" width="80%" height="80%"/>

<br>

### gitbash 실행
- Git을 설치하게 되면 window에서 git bash 프로그램을 사용할 수 있다.
- git bash 실행 : Window 검색 > 'git bash' 입력 후 실행
- gitbash 터미널
  
  <img src="https://github.com/user-attachments/assets/f53553f7-acd9-42ce-acb1-af1090f9d309" width="80%" height="80%"/>


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## Docker Desktop 설치 & WSL2 설치 - Windows

### Docker Desktop 설치
```
Docker Desktop을 설치하면 
- 도커 엔진
- 도커 클라이언트 (CLI)
- 컨테이너 런타임
- 기타 도커 관련 도구들이 함께 설치된다.
```
- Docker Desktop Download URL : <https://www.docker.com/products/docker-desktop/>
- 다운로드 주소 접속 후 스크롤 내려서 `Download Docker Desktop` 클릭하여 다운로드 받아주자.
- 버트에 마우스를 내면 PC의 프로세서 아키텍처 버전에 따른 종류가 나온다. 자신의 PC에 맞게 선택하여 다운로드 하자.
- 내 PC 프로세서 아키텍처 확인방법
  ```
  # CMD 창에서
  echo %PROCESSOR_ARCHITECTURE%
  ```

  <img src="https://github.com/user-attachments/assets/b0b35e8f-71b5-42dd-9631-011a3083ee3d" width="80%" height="80%"/>

<br>

- 다운로드 받은 `Docker Desktop Installer.exe` 파일 실행하여 설치시작.
- 설치 시 추가 설정 없이 그대로 진행하면 된다.

<br>
<br>

### WSL2(Windows Subsystem for Linux) 설치
- Docker는 리눅스 기반에서 실행되기 때문에 윈도우에서 리눅스를 사용할 수 있도록 WSL2를 설치치해 주어야 한다.
- WSL2(Windows Subsystem for Linux)는 VM과 같은 도구 없이 윈도우 환경에서 Linux를 사용할 수 있도록 **Virtual Machine Platform** 을 제공한다.
- WSL2를 통해 `Virtual Machine Platform`이 활성화 되어 있지 않으면 `Docker Desktop` 실행 시 다음과 같은 에러 창이 나온다.

  <img src="https://github.com/user-attachments/assets/a0dc3f0e-269d-452a-a82c-ef9df77104c2" width="80%" height="80%"/>

- 또한 PowerShell 터미널을 통해 wsl 명령을 실행하면 Virtual Machine Platform가 활성화 되지 않았다는는 오류가 나온다.
  <img src="https://github.com/user-attachments/assets/9b71b626-ab36-4066-9e5a-3df9c8e5c3fb" width="80%" height="80%"/>

- 자 이제 `WSL2`를 설치해보자. 
  - 윈도우 검색을 통해 `PowerShell`을 검색한 후 마우스 오른쪽 버튼을 클릭하여 관리자 권한으로 실행해 주자.
  - 아래 명령어를 실행
    ```bash
    # Windows SubSystem Linux를 활성화시키는 명령어
    > dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart

    # VirtualMachinePlatform 기능을 활성화시키는 명령어 : WSL2 버전에 필요한 명령어
    > dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart
    ```

- WSL 설치가 완료된 후 PC를 재부팅 해야 적용된다. 재부팅을 해주자.

<br>
<br>

### Docker Desktop 실행
```
- Windows는 Linux 커널을 기본적으로 포함하지 않는다.
- Docker Desktop은 WSL2(Windows Subsystem for Linux)를 통해 Linux 환경을 제공한다.
- Docker Desktop이 실행되어야 도커 데몬이 작동하고, 이를 통해 도커 명령어를 실행할 수 있다.
- Docker Desktop을 종료하면 도커 데몬도 함께 종료되어 도커 명령어를 사용할 수 없게 된다.
따라서 Windows에서 도커를 사용하기 위해서는 Docker Desktop이 반드시 실행 중이어야 한다.
```
- 설치가 완료되면 `Docker Desktop`를 실행하여 아래 메인 화면이 뜨면 완료.

  <img src="https://github.com/user-attachments/assets/7ed2129b-c054-471f-bc8c-a4b5b519137f" width="80%" height="80%"/>


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## VSCode 설치
- VSCode Download URL : <https://code.visualstudio.com/Download>
- 다운로드 주소 접속 후 `Windows` 아이콘을 클릭하여 다운로드 받아주자.
 
<img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/dd0dc440-e9e2-4579-a4c3-c27702e65b76" width="80%" height="80%"/>

<br>

- 다운로드 받은 `VSCodeUserSetup-x64-1.91.0.exe` 파일을 실행하여 VSCode 설치 시작.

<br>

- 설치 시 추가 설정 : 특정 폴더에서 마우스 우클릭 시 VSCode로 바로 접근하기 위한 설정은 아래 체크박스 두개를 체크해 줘야 한다.
- 체크 후 설치를 진행하면 폴더에서 마우스 우 클릭 시 `Edit with VS Code` 항목이 보이게 된다.
- 그 외의 별도 설정은 필요하지 않다. 그대로 진행하자.

<img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/102303114/ee6f6b18-2690-4402-acb6-fbc720551995" width="80%" height="80%"/>


<!--<img src="" width="80%" height="80%"/>-->