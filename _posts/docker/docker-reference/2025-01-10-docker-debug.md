---
layout: single
title:  "도커 디버깅"
categories:
  - DockerDoc
tags:
  - [Docker, Debug]

toc: true
toc_sticky: true
---

- Description : 모든 컨테이너나 이미지에 대해 셸 접근을 할 수 있습니다. `docker exec`를 통한 디버깅의 대안입니다.
- Usage : `debug [옵션] {컨테이너|이미지}`

<br>

## 설명
> 참고
> Docker Debug를 사용하려면 Pro, Team, 또는 Business 구독이 필요합니다. 이 명령어를 사용하기 위해서는 로그인해야 합니다.

Docker Debug는 이미지를 작고 안전하게 유지하는 모범 사례를 따르도록 도와주는 CLI 명령어입니다. Docker Debug를 사용하면 애플리케이션을 실행하는 데 필요한 최소한의 요소만 포함하도록 하면서도 이미지를 디버깅할 수 있습니다. 이는 모든 도구가 제거되어 일반적으로 디버깅이 어려운 슬림 이미지나 컨테이너에서 작업할 수 있게 해줍니다. 예를 들어, `docker exec -it my-app bash`와 같은 일반적인 디버깅 접근 방식이 슬림 컨테이너에서 작동하지 않을 수 있는 상황에서도 docker debug는 작동할 것입니다.

docker debug를 사용하면 셸이 포함되어 있지 않은 경우에도 모든 컨테이너나 이미지에 대해 디버그 셸을 얻을 수 있습니다. Docker Debug를 사용하기 위해 이미지를 수정할 필요가 없습니다. 또한 Docker Debug를 사용하더라도 이미지가 수정되지 않습니다. Docker Debug는 쉽게 사용자 정의할 수 있는 자체 도구 모음을 제공합니다. 이 도구 모음에는 vim, nano, htop, curl과 같은 많은 표준 Linux 도구가 미리 설치되어 있습니다. https://search.nixos.org/packages 에서 사용 가능한 추가 도구를 설치하려면 내장된 install 명령어를 사용하세요. Docker Debug는 bash, fish, zsh를 지원하며, 기본적으로 사용자의 셸을 자동으로 감지하려고 시도합니다.

내장 사용자 정의 도구:

- install [도구1] [도구2]: https://search.nixos.org/packages 에서 Nix 패키지 추가, 예제 참조
- uninstall [도구1] [도구2]: Nix 패키지 제거
- entrypoint: 엔트리포인트 출력, 린트, 또는 실행, 예제 참조
- builtins: 내장 사용자 정의 도구 표시

> 참고
> 이미지와 중지된 컨테이너의 경우, 셸을 종료할 때 모든 변경 사항이 삭제됩니다. 어떤 시점에서도 변경 사항이 실제 이미지나 컨테이너에 영향을 미치지 않습니다. 실행 중이거나 일시 중지된 컨테이너에 접근할 때는 모든 파일 시스템 변경 사항이 컨테이너에 직접 표시됩니다. /nix 디렉터리는 실제 이미지나 컨테이너에서 절대 보이지 않습니다.

## 옵션

| 옵션 | 기본값 | 설명 |
|------|--------|------|
| --shell | auto | 셸 선택. 지원되는 셸: bash, fish, zsh, auto |
| -c, --command | | 대화형 세션을 시작하는 대신 지정된 명령어를 평가, 예제 참조 |
| --host | | 연결할 도커 데몬 소켓. 예: ssh://root@example.org, unix:///some/path/docker.sock, 예제 참조 |

## 예제

### 셸이 없는 컨테이너 디버깅하기 (슬림 컨테이너)
hello-world 이미지는 매우 단순하며 /hello 바이너리만 포함하고 있습니다. 이는 슬림 이미지의 좋은 예시입니다. 다른 도구나 셸이 전혀 없습니다.

hello-world 이미지에서 컨테이너를 실행합니다:

```bash
docker run --name my-app hello-world
```

컨테이너는 즉시 종료됩니다. 내부에 디버그 셸을 얻으려면 다음을 실행하세요:

```bash
docker debug my-app
```

디버그 셸에서 파일 시스템을 검사할 수 있습니다:

```bash
docker > ls
dev  etc  hello  nix  proc  sys
```

/hello는 컨테이너를 실행할 때 실행된 바이너리입니다. 이를 직접 실행하여 확인할 수 있습니다:

```bash
docker > /hello
```

바이너리를 실행한 후에는 동일한 출력이 생성됩니다.

### (슬림) 이미지 디버깅하기
다음을 실행하여 이미지를 직접 디버깅할 수 있습니다:

```bash
docker debug hello-world
...
docker > ls
dev  etc  hello  nix  proc  sys
```

docker run 명령어처럼 docker debug가 자동으로 이미지를 가져오므로 이미지를 미리 가져올 필요도 없습니다.

### 실행 중인 컨테이너의 파일 수정하기
Docker debug를 사용하면 실행 중인 모든 컨테이너의 파일을 수정할 수 있습니다. 도구 모음에는 vim과 nano가 미리 설치되어 있습니다.

nginx 컨테이너를 실행하고 기본 index.html을 변경해보겠습니다:

```bash
docker run -d --name web-app -p 8080:80 nginx
d3d6074d0ea901c96cac8e49e6dad21359616bef3dc0623b3c2dfa536c31dfdb
```

nginx가 실행 중인지 확인하려면 브라우저를 열고 http://localhost:8080으로 이동하세요. 기본 nginx 페이지가 표시되어야 합니다. 이제 vim을 사용하여 변경해보겠습니다:

```bash
vim /usr/share/nginx/html/index.html
```

제목을 "Welcome to my app!"으로 변경하고 파일을 저장하세요. 이제 브라우저에서 페이지를 새로고침하면 업데이트된 페이지가 표시될 것입니다.

### install 명령어를 사용한 도구 모음 관리하기
내장된 install 명령어를 사용하면 https://search.nixos.org/packages의 모든 도구를 도구 모음에 추가할 수 있습니다. 도구를 추가해도 실제 이미지나 컨테이너는 절대 수정되지 않습니다. 도구는 사용자의 도구 모음에만 추가됩니다. docker debug를 실행한 다음 nmap을 설치해보세요:

```bash
docker debug nginx
...
docker > install nmap
Tip: You can install any package available at: https://search.nixos.org/packages.
installing 'nmap-7.93'
these 2 paths will be fetched (5.58 MiB download, 26.27 MiB unpacked):
/nix/store/brqjf4i23fagizaq2gn4d6z0f406d0kg-lua-5.3.6
/nix/store/xqd17rhgmn6pg85a3g18yqxpcya6d06r-nmap-7.93
copying path '/nix/store/brqjf4i23fagizaq2gn4d6z0f406d0kg-lua-5.3.6' from 'https://cache.nixos.org'...
copying path '/nix/store/xqd17rhgmn6pg85a3g18yqxpcya6d06r-nmap-7.93' from 'https://cache.nixos.org'...
building '/nix/store/k8xw5wwarh8dc1dvh5zx8rlwamxfsk3d-user-environment.drv'...

docker > nmap --version
Nmap version 7.93 ( https://nmap.org )
Platform: x86_64-unknown-linux-gnu
Compiled with: liblua-5.3.6 openssl-3.0.11 libssh2-1.11.0 nmap-libz-1.2.12 libpcre-8.45 libpcap-1.10.4 nmap-libdnet-1.12 ipv6
Compiled without:
Available nsock engines: epoll poll select
```

다른 이미지에 대한 디버그 셸을 얻어서 nmap이 이제 도구 모음의 일부가 되었는지 확인할 수 있습니다:

```bash
docker debug hello-world
...
docker > nmap --version

Nmap version 7.93 ( https://nmap.org )
Platform: x86_64-unknown-linux-gnu
Compiled with: liblua-5.3.6 openssl-3.0.11 libssh2-1.11.0 nmap-libz-1.2.12 libpcre-8.45 libpcap-1.10.4 nmap-libdnet-1.12 ipv6
Compiled without:
Available nsock engines: epoll poll select

docker > exit
```

nmap이 여전히 존재합니다.

### 컨테이너의 기본 시작 명령어 이해하기 (엔트리포인트)
Docker Debug에는 entrypoint라는 내장 도구가 함께 제공됩니다. hello-world 이미지에 들어가서 엔트리포인트가 /hello인지 확인해보세요:

```bash
docker debug hello-world
...
docker > entrypoint --print
/hello
```

entrypoint 명령어는 기본 이미지의 ENTRYPOINT와 CMD 문을 평가하여 결과 엔트리포인트를 출력, 린트, 또는 실행할 수 있게 해줍니다. 하지만 Understand how CMD and ENTRYPOINT interact의 모든 경우를 이해하기는 어려울 수 있습니다. 이러한 상황에서는 entrypoint가 도움이 될 수 있습니다.

entrypoint를 사용하여 Nginx 이미지에서 컨테이너를 실행할 때 실제로 어떤 일이 발생하는지 조사해보세요:

```bash
docker debug nginx
...
docker > entrypoint
Understand how ENTRYPOINT/CMD work and if they are set correctly.
From CMD in Dockerfile:
 ['nginx', '-g', 'daemon off;']

From ENTRYPOINT in Dockerfile:
 ['/docker-entrypoint.sh']

By default, any container from this image will be started with following command:

/docker-entrypoint.sh nginx -g daemon off;

path: /docker-entrypoint.sh
args: nginx -g daemon off;
cwd:
PATH: /usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

Lint results:
 PASS: '/docker-entrypoint.sh' found
 PASS: no mixing of shell and exec form
 PASS: no double use of shell form

Docs:
- https://docs.docker.com/reference/dockerfile/#cmd
- https://docs.docker.com/reference/dockerfile/#entrypoint
- https://docs.docker.com/reference/dockerfile/#understand-how-cmd-and-entrypoint-interact
```

출력을 보면 nginx 이미지를 시작할 때 /docker-entrypoint.sh 스크립트가 nginx -g daemon off; 인수와 함께 실행된다는 것을 알 수 있습니다. --run 옵션을 사용하여 엔트리포인트를 테스트할 수 있습니다:

```bash
docker debug nginx
...
docker > entrypoint --run
/docker-entrypoint.sh: /docker-entrypoint.d/ is not empty, will attempt to perform configuration
/docker-entrypoint.sh: Looking for shell scripts in /docker-entrypoint.d/
/docker-entrypoint.sh: Launching /docker-entrypoint.d/10-listen-on-ipv6-by-default.sh
10-listen-on-ipv6-by-default.sh: info: Getting the checksum of /etc/nginx/conf.d/default.conf
10-listen-on-ipv6-by-default.sh: info: Enabled listen on IPv6 in /etc/nginx/conf.d/default.conf
/docker-entrypoint.sh: Sourcing /docker-entrypoint.d/15-local-resolvers.envsh
/docker-entrypoint.sh: Launching /docker-entrypoint.d/20-envsubst-on-templates.sh
/docker-entrypoint.sh: Launching /docker-entrypoint.d/30-tune-worker-processes.sh
/docker-entrypoint.sh: Configuration complete; ready for start up
2024/01/19 17:34:39 [notice] 50#50: using the "epoll" event method
2024/01/19 17:34:39 [notice] 50#50: nginx/1.25.3
2024/01/19 17:34:39 [notice] 50#50: built by gcc 12.2.0 (Debian 12.2.0-14)
2024/01/19 17:34:39 [notice] 50#50: OS: Linux 5.15.133.1-microsoft-standard-WSL2
2024/01/19 17:34:39 [notice] 50#50: getrlimit(RLIMIT_NOFILE): 1048576:1048576
2024/01/19 17:34:39 [notice] 50#50: start worker processes
2024/01/19 17:34:39 [notice] 50#50: start worker process 77
...
```

이렇게 하면 실제 컨테이너를 실행하지 않고도 디버그 셸에서 nginx를 시작할 수 있습니다. Ctrl+C를 눌러 nginx를 종료할 수 있습니다.

### 명령어 직접 실행하기 (예: 스크립팅용)
대화형 세션을 시작하는 대신 --command 옵션을 사용하여 명령어를 직접 실행할 수 있습니다. 예를 들어, 이는 bash -c "arg1 arg2 ..."와 유사합니다. 다음 예제는 대화형 세션을 시작하지 않고 nginx 이미지에서 cat 명령어를 실행합니다.

```bash
docker debug --command "cat /usr/share/nginx/html/index.html" nginx

<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
html { color-scheme: light dark; }
body { width: 35em; margin: 0 auto;
font-family: Tahoma, Verdana, Arial, sans-serif; }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, the nginx web server is successfully installed and
working. Further configuration is required.</p>

<p>For online documentation and support please refer to
<a href="http://nginx.org/">nginx.org</a>.<br/>
Commercial support is available at
<a href="http://nginx.com/">nginx.com</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>
```

### --host 옵션을 사용한 원격 디버깅
다음 예제들은 --host 옵션을 사용하는 방법을 보여줍니다. 첫 번째 예제는 SSH를 사용하여 example.org의 원격 Docker 인스턴스에 root 사용자로 연결하고, my-container 컨테이너에 대한 셸을 얻습니다.

```bash
docker debug --host ssh://root@example.org my-container
```

다음 예제는 다른 로컬 Docker Engine에 연결하고, my-container 컨테이너에 대한 셸을 얻습니다.

```bash
docker debug --host=unix:///some/path/docker.sock my-container
```