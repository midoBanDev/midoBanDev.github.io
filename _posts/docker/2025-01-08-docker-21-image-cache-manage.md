---
layout: single
title:  "이미지 캐시 관리"
categories:
  - Docker
tags:
  - [Docker, Cache, Build-Cache]

toc: true
toc_sticky: true
---

## 이미지 캐싱
Docker는 이미지를 빌드하고 관리할 때 **레이어 기반의 캐싱 시스템**을 사용한다.
이는 빌드 시간을 단축하고 저장 공간을 효율적으로 사용하기 위한 핵심 기능이다.

1. Dockerfile에 `FROM nginx:1.23`으로 지정한 베이스 이미지를 처음 실행하는 경우에는 **모든 이미지 레이어를 다운로드** 받는다.
그리고 **호스트 시스템 내부의 Docker 캐시를 저장하는 독립된 저장소에 이미지 레이어를 저장**된다.(호스트가 바뀌면 이미지는 새로 다운로드 받는다)
<img src="https://github.com/user-attachments/assets/e322ed9b-7ecf-4252-9990-394d84bb4bd0" width="80%" height="80%"/>

2. 하지만 동일한 베이스 이미지로 다시 도커 빌드를 실행하면 `케싱된 레이어를 재사용`한다. 그래서 두 번째부터는 빌드 속도가 빠르다.
<img src="https://github.com/user-attachments/assets/06cadea8-3bbf-4972-a672-77188f32fee3" width="80%" height="80%"/>

캐시된 이미지 레이어는 OS 마다 다른 위치에 저장된다.  
- Linux는 `/var/lib/docker`, Windows는 `WSL2 가상 하드디스크`에 저장된다.  
- 클라우드 환경(AWS EC2 등)에서는 EC2 인스턴스에 Docker를 설치하면 해당 `인스턴스의 디스크`에 저장된다. 
그리고 캐싱된 이미지 레이어는 `재부팅해도 유지`된다.

이렇게 도커의 캐싱 시스템으로 동일한 이미지 레이어를 공유함으로써 `저장 공간을 효율적`으로 사용할 수 있고, `빌드 시간을 단축`할 수 있고, `네트워크 대역폭을 절약`할 수 있다.

캐싱된 이미지도 관리가 필요하다. 캐싱된 이미지는 직접 눈으로 확인할 수 없기 때문에 사용자의 관리가 필요하다.

<br>

### 캐시 관리 명령어

- 모든 이미지를 확인한다.
  
```bash
# 모든 이미지 레이어 확인
$ docker images -a
```
<br>

- dangling 이미지 확인 
  - 댕글링 이미지란 태그가 없는 이미지를 말한다.

```bash
# 댕글링 이미지 확인
$ docker images -f "dangling=true"
```
<br>

- 캐시된 데이터 용량을 확인
  - 주기적으로 시스템 용량을 확인하여 관리를 해주는 것이 좋다.
  
```bash
# 디스크 사용량 확인
$ docker system df
```
<img src="https://github.com/user-attachments/assets/7a6725ed-af65-43b5-887b-8b863e82707d" width="80%" height="80%"/>

<br>

- 이미지(Images) 영역의 `dangling 이미지` 삭제
  
```bash
$ docker image prune
```

<br>

- 사용되지 않는 모든 이미지 삭제
  - 사용하지 않는 이미지 레이어란 다음 조건을 모두 만족하는 이미지를 말한다.  
    - 해당 이미지로 실행 중인 컨테이너가 없어야 함
    - 다른 이미지의 부모 이미지(베이스 이미지)로 사용되지 않아야 함
  - `docker inspect` 명령어 사용하여 베이스 이미지를 확인할 수 있다. (하지만 정확하지 않은 경우도 있기 때문에 Dockerfile에 `LABEL base_image="nginx:1.23"`를 선언하는 것이 관리적 측면에서도 좋다)
  
```bash
# 사용하지 않는 모든 이미지 삭제
$ docker image prune -a 
```
<img src="https://github.com/user-attachments/assets/61cdc8c5-4c41-4b56-a472-bc5b1285d4b1" width="80%" height="80%"/>

<br>

- 도커 시스템에서 사용되지 않는 리소스 삭제
  - `docker system prune` 명령어는 매우 강력하기 때문에 조심해서 사용해야 한다.
  - docker system prune가 삭제하는 항목
    - `이미지 (Images)`: 사용되지 않는 모든 dangling 이미지를 삭제한다.
    - `컨테이너 (Containers)`: 실행 중이지 않고 정리되지 않은 모든 컨테이너를 삭제한다.
    - `볼륨 (Volumes)`: 참조되지 않는 모든 로컬 볼륨을 삭제한다.
    - `네트워크 (Networks)`: 사용되지 않는 모든 네트워크를 삭제한다.
    - `빌드 캐시 (Build Cache)`: 사용되지 않는 모든 빌드 캐시를 삭제한다.

```bash
# 모든 미사용 Docker 객체 삭제
$ docker system prune
```

<br>

## 빌드 캐시(Build Cache)
Docker는 빌드 캐시를 통해 동일한 빌드 작업에서 생성된 레이어를 재사용하여 빌드 속도를 최적화할 수 있다.

**특징**
- 이미지 빌드 시 각 명령어 단계에서 생성되는 레이어를 캐시한다.
- 이미지 빌드가 실패해도 생성된 레이어는 캐시로 유지된다.
- 명령어(`RUN`, `COPY`, 등)와 입력값이 동일하면 캐시를 재사용한다.
- `BuildKit 캐시 마운트`를 사용하여 캐시되는 데이터도 빌드 캐시 영역에 해당된다.


- 캐시 확인
- 실제 이미지를 빌드하면 Build Cache 용량이 증가하는 것을 확인할 수 있다.
  
```bash
$ docker system df
TYPE            TOTAL     ACTIVE    SIZE      RECLAIMABLE
Images          8         3         1.988GB   1.26GB (63%)
Containers      3         3         32.83kB   0B (0%)
Local Volumes   2         2         49.51MB   0B (0%)
Build Cache     28        0         0B        0B
```

- 사용하지 않는 빌드 캐시 삭제한다. `Build Cache` 용량 삭제
- 이미지 빌드 시 최초 생성된 캐시 레이어 또는 이미 캐시된 레이어를 다른 이미지가 재사용한 경우, 캐시 레이어와 연관된 이미지가 존재하지 않아야 삭제된다.
- 즉 연결된 이미지를 삭제해야 캐시 이미지도 삭제할 수 있다.(컨테이너와 무관)
```bash
$ docker builder prune
```


<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>