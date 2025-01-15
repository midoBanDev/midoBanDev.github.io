---
layout: single
title:  "[Docker] 도커 볼륨 - Docker Volume"
categories:
  - Docker
tags:
  - [Docker, Volume]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>


## 도커 볼륨

컨테이너는 **스테이트리스(stateless)**하기 때문에 컨테이너가 삭제되거나 재생성되면 모든 데이터가 이미지의 상태로 초기화 된다. 
서버를 운영하다 보면 데이터를 유지해야 하는 경우가 있다. 데이터를 유지한다는 것을 IT 환경에서는 `영속성(persistence)`이 있다고 표현한다. 
특히 데이터베이스 서버 같은 경우에는 이 저장되는 데이터들이 DB서버의 특정 디렉터리에 저장되는데 
데이터들이 컨테이너가 삭제되거나 재생성될 때마다 초기화되면 운영이 어려워진다.  
<img src="https://github.com/user-attachments/assets/df71bb8c-9ab7-4e60-9f6b-822f55b3985b" width="60%" height="80%"/>
<div class="br15"/>

그리고 일반적으로 컨테이너를 운영하는 환경에서는 같은 역할을 하는 서버를 여러 개 운영하게 된다.
이것을 서버가 `이중화`되어 있다고 표현한다. 최소 서버 2대 이상을 두어 한 대의 서버에 문제가 생겼을 때 다른 한 대의 서버가 요청을 처리할 수 있다.
이는 시스템의 `고가용성(High Avalilabliity)`을 확보할 수 있게 해준다.

또한 트래픽이 늘어날 때마다 서버의 대수를 증가시켜서 트래픽을 처리할 수 있어야 한다.
이때 늘어난 컨테이너들은 동일한 기능을 제공해야 한다.
 
아래 그림을 보면 데이터베이스 이미지로 실행된 컨테이너1, 컨테이너2, 컨테이너3 세 대가 있다.
구조를 보면 클라이언트의 요청은 `로드벨런싱(Load Balancing)`이라는 네트워크 처리를 거쳐서 데이터베이스1, 데이터베이스2, 데이터베이스3 컨테이너들에게 랜덤하게 전달된다.
`로드벨런싱`은 한 서비스의 트래픽을 여러 대의 서버로 분산해주는 네트워크 기술이다.  
<img src="https://github.com/user-attachments/assets/68a3ebd8-a7f3-4443-9790-d56901c2bac6" width="80%" height="80%"/>
<div class="br15"/>

예를 들어 A라는 상품의 금액을 조회하는 요청을 여러 번 보냈을 때 루드벨런싱을 통해 여러 개의 컨테이너로 전달 되었다고 해보자.
여기서 만약 1번 컨테이너에서 응답한 데이터와 3번 컨테이너에서 응답한 데이터가 다르다면 문제가 될 수 있다. 
모든 컨테이너는 동일한 요청에 대한 응답이 모두 같아야 한다.
결국 영숙성이 필요한 데이터는 같은 종류의 모든 컨테이너가 함께 공유하고 있어야 한다.

정리하자면 컨테이너는 상태가 없기 때문에 재생성되면 데이터가 모두 삭제되기 때문에 영숙성이 필요한 데이터를 저장할 공간이 필요하다.
도커는 이렇게 영숙성이 필요한 데이터를 외부 공유 저장소에 저장할 수 있는 `도커 볼륨`이라는 기능을 제공한다.

도커 볼륨을 사용하면 컨테이너 내의 특정 폴더를 외부 저장소와 연결시킬 수 있다. 
`외부 저장소`를 `볼륨(volume)`이라고 하고, 특정 폴더를 볼륨에 `연결하는 것`을 `마운트(mount)`한다고 표현한다.
하지만 도커 볼륨은 컨테이너의 특정 경로를 외부 저장소에 연결하는 기능을 통칭하여 표현으로 사용하기도 한다. 
그래서 맥락에 맞게 이해하는 것이 필요하다.

<br>

### 마운트(mount)
`마운트(mount)`는 컴퓨터의 **특정 디렉토리를 외부 저장소와 연결한는 것**을 의미한다. 
예를 들어 USB를 PC에 연결하면 새로운 드라이브가 만들어진다. 
이 USB를 사용해서 데이터를 저장할 수도 있고, 해당 USB 다른 PC에 연결하면 데이터를 공유할 수도 있다. 
<img src="https://github.com/user-attachments/assets/13cdf9e6-aa3e-446a-9d02-0573e1f33d5f" width="70%" height="80%"/>
<div class="br15"/>

USB는 물리적으로 연결해야 하는 장치인 반면 네트워크에 연결되어 있으면 멀리 떨어진 외부 저장소에도 연결할 수 있는 `네트워크 파일 시스템(Network File System)`이라는 기술이 있다.
NFS를 통해 내 컴퓨터의 특정 경로를 외부 저장소에 마운트하여 파일을 저장할 수 있게 되는 것이다.
NFS는 한 번에 한 대만 연결 가능한 USB와는 다르게 동시에 여러 PC를 연결할 수도 있다.  
<img src="https://github.com/user-attachments/assets/262a6180-5dbc-4ae8-856f-942c60678446" width="80%" height="80%"/>
<div class="br15"/>

컨테이너들은 도커 볼륨을 컨테이너의 특정 경로에 마운트에서 사용한다.

예를 들어서 데이터베이스 컨테이너를 실행하면 컨테이너 안에 `/var/lib/postgresql/data`라는 폴더에 실제 데이터가 저장된다.
이 경로를 도커 볼륨에 마운트를 했다면 이후부터 컨테이너가 실행되어 저장된 데이터는 컨테이너 레이어에 저장되는 것이 아니라 마운트되어 있는 외부 볼륨에 저장된다.  
<img src="https://github.com/user-attachments/assets/5bdeacb2-6d0b-4ec6-bb62-9125bc75fd4c" width="80%" height="80%"/> 
<div class="br15"/>

볼륨 하나에 컨테이너 세 대를 마운트하면 모든 컨테이너는 동일한 데이터를 제공할 수 있다. 
그래서 컨테이너가 삭제되거나 새로운 컨테이너가 생성되어도 동일한 경로에 다시 마운트하면 볼륨의 데이터는 그대로 남아 있기 때문에 데이터의 영속성을 보장할 수 있게 된다.


### 마운트(mount) 방법
컨테이너를 실행할 때 `-v` 옵션으로 도커 볼륨을 마운트할 수 있다.
- `docker run -v <도커볼륨명>:<컨테이너내부경로>`
  
```bash
$ docker run -v myvolume:/var/lib/postgresql/data
```
>참고
윈도우 `gitbash` 환경에서는 볼륨을 마운트할 때 맨 앞 슬래시를 하나 더 붙여야 한다. 
`-v myvolume://var/lib/postgresql/data`
슬래시가 하나만 있으면 윈도우가 자체적으로 C드라이브의 경로로 변환해서 처리하기 때문에 에러가 발생한다.  

<img src="https://github.com/user-attachments/assets/185b42da-562f-4645-a4ee-11b84558828c" width="80%" height="80%"/>

<br>

**하나의 컨테이너에 여러 볼륨 연결**  
볼륨을 여러 개 만들어 하나의 컨테이너 내부의 서로 다른 경로를 연결할 수 있다.
- `docker run -v <도커볼륨명>:<컨테이너내부경로> -v <도커볼륨명>:<컨테이너내부경로>`
  
```bash
$ docker run -v volume1:/etc/postgresql -v volume2:/var/lib/postgresql/data
```
<img src="https://github.com/user-attachments/assets/e48ea630-87c4-49d9-8010-bb6669ed970a" width="80%" height="80%"/>


**여러 컨테이너에 볼륨 연결** 
하나의 불륨을 여러 컨테이너에 연결할 수 있다.
- `docker run -v <도커볼륨명>:<컨테이너내부경로>`
  
```bash
$ docker run --name postgres1 -v volume1:/var/lib/postgresql/data
$ docker run --name postgres2 -v volume1:/var/lib/postgresql/data
```
<img src="https://github.com/user-attachments/assets/b3d681d0-fae2-408b-ba16-5e9a2125adab" width="80%" height="80%"/>
<div class="br15"/>

### 도커 볼륨과 컨테이너의 관계
도커 불륨을 생성하면 호스트OS의 특정 공간에 데이터를 저장하는 볼륨 경로(`/volumes/volume1`)가 만들어진다. 

**도커 볼륨 생성**  
- `docker volume create <생성할볼륨명>`
  
```bash
$ docker volume create mydata
```

도커 볼륨 `volume1`을 생성한 후 nginx 컨테이너를 실행시키면서 사용할 볼륨은 `volume1`에 지정하고, 마운트 할 컨테이너의 내부 경로는 `/usr/share/nginx/html` 로 지정한다. 
그럼 컨테이너 내부 경로인 **/usr/share/nginx/html**에 저장되는 데이터는 **volume1**이 실제로 데이터를 저장하는 호스트OS의 `/volumes/volume1` 경로에 저장된다.  
<img src="https://github.com/user-attachments/assets/dd525cc1-d6c9-4ec3-add8-f0ec37481033" width="80%" height="80%"/>
<div class="br15"/>

사실 도커 볼륨의 저장 경로인 **/volumes/volume1**는 호스트 OS의 **도커가 관리하는 독립적인 저장소**에 저장된다.
그래서 `-v 옵션에 볼륨명을 지정하는 방식`은 데이터 관리를 도커에게 일임하는 것이라 볼 수 있다.


### 바인드 마운트(bind mount)
바인드 마운트는 도커가 직접 관리하는 독립된 저장소가 아닌 내 호스트 OS의 `원하는 경로를 마운트`하는 방식을 말한다.
-v 옵션의 볼륨명 자리에 호스트OS의 내부 경로를 지정하는 방식으로 사용할 수 있다.
- `docker run -v <호스트OS내부경로>:<컨테이너내부경로>`
- 실행 도구를 `gitbash`를 사용하는 경우 경로 지정에 오류가 발생할 수 있다. 정신 건강을 위해 `powershell`을 사용하는 걸 권장한다.

```powershell
# 절대경로
$ docker run -v /data/mypostgres:/var/lib/postgresql/data

# 현재 경로를 지정하고 싶은 경우 
$ docker run -d -v ${PWD}:/var/lib/postgresql/data -e POSTGRES_PASSWORD=pass --name postgres-data postgres:13
```

<img src="https://github.com/user-attachments/assets/945a48a7-9a58-4c73-a453-3aabfea4e9bd" width="80%" height="80%"/>
<div class="br15"/>

호스트OS에서 데이터를 직접 관리하고 싶은 경우에는 바인드 마운트 기능을 사용할 수 있다. 

또한 바운드 마운트에 NFS 기술을 접목시키면 호스트 OS가 아닌 멀리 떨어진 서버의 경로에도 저장할 수도 있다.
`호스트OS의 내부 경로를 미리 외부 저장소에 NFS 연결`해 놓은 후 바인드 마운트를 설정하면 
컨테이너 실행 중 생성된 데이터는 호스트 OS의 내부 경로를 거쳐 실제 NFS로 연결된 외부 저장소에 저장된다.
 
실제 운영 환경에서는 NFS를 활용하거나 클라우드 기반 스토리지를 사용하는 경우가 많다.

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## Docker 익명 볼륨(Anonymous Volume)
Docker 볼륨은 크게 두 가지로 나눌 수 있다. 
하나는 우리가 직접 이름을 지정해서 생성하는 `명명된 볼륨(Named Volume)`이고, 다른 하나는 Docker가 자동으로 생성해주는 `익명 볼륨(Anonymous Volume)`이다. 
Named 볼륨의 경우 우리가 직접 생성하고 관리할 수 있다.
기본적으로 Named 볼륨과 Anonymous 볼륨은 컨테이너가 삭제되어도 볼륨은 그대로 유지된다.

### 익명 볼륨(Anonymous Volume)의 사용
PostgreSQL과 같은 데이터베이스 이미지의 Dockerfile 내부를 보면 `VOLUME ["/var/lib/postgresql/data"]` 라는 지시어를 발견할 수 있다. 
이 지시어는 Docker에게 "이 경로는 볼륨이 필요해. 컨테이너를 실행할 때 볼륨을 하나 만들어서 여기에 마운트해줘"라고 알려주는 역할을 한다.

실제 익명 볼륨의 생성과 관리 과정을 살펴보자.  
1. 컨테이너 실행 시 Docker는 `VOLUME` 지시어에 의해 자동으로 익명 볼륨을 생성한다.
2. 생성된 볼륨이 지정된 경로(/var/lib/postgresql/data)에 마운트한다.
3. 볼륨은 호스트의 `/var/lib/docker/volumes/[volume-id]/_data`에 실제로 저장된다.
   - 이 볼륨은 호스트의 /var/lib/docker/volumes 디렉토리 아래에 긴 `해시값`을 이름으로 하여 생성된다.
4. 컨테이너를 삭제해도 볼륨은 유지된다.
5. 새 컨테이너 생성 시 새로운 익명 볼륨이 다시 생성되고, 위 과정이 반복된다.

여러 번의 데이터베이스 컨테이너 실행 결과 아래와 같이 여러 개의 익명 볼륨이 생성되는 것을 확인할 수 있다.

Docker volume 목록 확인:
```bash
$ docker volume ls
DRIVER    VOLUME NAME
local     286113e1781f3261e164fbaf26bd97799b1ff28d8b818659831420c3987c4750
local     c97b7403d78e335bf921bcf5f7e2ecc72714fae21375b31cc86ff64a0b18c1ec
local     dba17e15f091121c16ae8706beaaf39f01959d0d5abd056f90d5ff8d5c848362
local     ea82f300d995cea53b8790260ce5dad2c0d67e8f939af0a192b33f8a3c989f87
local     ea87e024b56509846a3316e4b5c2913a4d101ea5619d07ebe1517629dde8731c
local     ff651991f01381b5316eb8ae3aaf66b0d6b1b6d71b2fb193034d4478e21a1d01
local     mydata
```

여기서 중요한 점은 익명 볼륨의 특성이다. 
- 컨테이너를 삭제하더라도 익명 볼륨 내의 데이터는 그대로 보존된다. 
- 다만 익명 볼륨은 자동 생성된 긴 해시값을 이름으로 사용하기 때문에, 새로운 컨테이너에서 이 볼륨을 재사용하는 것은 실질적으로 불가능하다.  

즉, 익명 볼륨의 데이터는 그대로 있지만 볼륨 자체가 재사용 되지 않는다.
그래서 이렇게 사용되지 않는 익명 볼륨들을 관리하여 호스트의 저장 공간을 확보해주는 것이 필요하다.

볼륨들을 관리하기 위한 주요 Docker 명령어들은 다음과 같다:
```bash
# 볼륨 조회
$ docker volume ls

# 개별 볼륨 삭제
$ docker volume rm [볼륨이름]

# 컨테이너와 함께 볼륨 삭제
$ docker rm -v [컨테이너이름]

# 사용하지 않는 모든 볼륨 삭제
$ docker volume prune
```

단, 볼륨 삭제에는 몇 가지 중요한 규칙이 있다. 
- 현재 실행 중인 컨테이너에 마운트된 볼륨은 삭제할 수 없다.
- 익명 볼륨이든 Named 볼륨이든 상관없이 사용하지 않는 볼륨만 삭제할 수 있다. 
이는 실수로 중요한 데이터를 삭제하는 것을 방지하기 위한 안전장치이다.

결론적으로, Docker의 익명 볼륨 시스템은 컨테이너의 데이터를 자동으로 보관하면서도 유연한 관리가 가능하도록 설계되어 있다. 
다만 익명 볼륨은 재사용이 어렵다는 특징이 있어, 데이터베이스와 같이 중요한 데이터를 다루는 경우에는 Named 볼륨을 사용하여 명시적으로 관리하는 것이 권장된다.

<div style="padding-top:40px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:40px;"></div>

## 도커 볼륨 명령어

### Volume 리스트 조회
- `volume` 명령어 사용

```bash
$ docker volume ls
```

<br>

### Volume 상세 정보 조회
- `volume` 명령어, `rm` 명령어 사용

```bash
$ docker [volume] inspect (볼륨명)
```

<br>

### Volume 생성
- `volume` 명령어, `create` 명령어 사용

```bash
$ docker volume create (볼륨명)
```

<br>

### Volume 삭제
- 사용되지 않는 볼륨만 삭제할 수 있다.
- `volume` 명령어, `rm` 명령어 사용

```bash
$ docker volume rm (볼륨명)
```

<br>

### 컨테이너와 함께 볼륨 삭제
- 컨테이너 삭제 시 `v` 옵션으로 볼륨도 같이 삭제할 수 있다.
- 단 `익명 볼륨`인 경우에만 삭제되고 `Named 불륨`인 경우에는 삭제되지 않는다.

```bash
$ docker rm -v [컨테이너이름]
```

<br>

### 사용하지 않는 모든 볼륨 삭제
- 컨테이너 실행, 중지와 무관하게 컨테이너와 연결되지 않은 모든 볼륨을 삭제한다.
  
```bash
$ docker volume prune
```

<br>

### Volume Mount
- 아래 예제는 `mydata` 라는 volume을 생성한 상황이다.
- `v` 옵션 사용. {볼륨명}:{컨테이너 내부 경로}

```bash
$ docker run -d --name (원하는컨테이너명) -v (불륨명):(컨테이너내부경로) (이미지명)
# Ex.
$ docker run -d --name my-postgres-2 -v mydata://var/lib/postgresql/data postgres:13
```

<br>

### Bind Mount
- `v` 옵션 사용

```bash
$ docker run -d --name (원하는컨테이너명) -v (HostOS의경로):(컨테이너내부경로) (이미지명)
# Ex.
$ docker run -d --name my-nginx-b -v C:\Users\crizen\Desktop\index:/usr/share/nginx/html nginx
```

<br>

### Mount 상세 정보 확인
- `v` 옵션 사용

```bash
$ docker container inspect (컨테이너명)
```
- Type : Mount Type
- Source : HostOS의 경로
- Destination : 컨테이너 내부 경로
```
"Mounts": [
            {
                "Type": "bind",
                "Source": "C:\\Users\\crizen\\Desktop\\easydocker\\index",
                "Destination": "/usr/share/nginx/html",
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            }
        ],
```


<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>
<img src="" width="80%" height="80%"/>