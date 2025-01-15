---
layout: single
title:  "[DockerDoc] How Compose works"
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

# Compose 작동 방식

Docker Compose를 사용할 때는 Compose 파일이라고 하는 YAML 설정 파일을 사용하여 애플리케이션의 서비스를 구성하고, Compose CLI를 통해 해당 구성의 모든 서비스를 생성하고 시작합니다.

Compose 파일(compose.yaml)은 다중 컨테이너 애플리케이션을 정의하는 방법에 대해 Compose 스펙에서 제공하는 규칙을 따릅니다. 이는 공식 Compose 스펙의 Docker Compose 구현체입니다.

## Compose 애플리케이션 모델

애플리케이션의 컴퓨팅 구성 요소는 서비스로 정의됩니다. 서비스는 플랫폼에서 동일한 컨테이너 이미지와 구성을 한 번 이상 실행하여 구현되는 추상적인 개념입니다.

서비스는 네트워크를 통해 서로 통신합니다. Compose 스펙에서 네트워크는 연결된 서비스 내의 컨테이너 간에 IP 경로를 설정하기 위한 플랫폼 기능 추상화입니다.

서비스는 볼륨에 영구 데이터를 저장하고 공유합니다. 스펙은 이러한 영구 데이터를 전역 옵션이 있는 고수준 파일시스템 마운트로 설명합니다.

일부 서비스는 런타임이나 플랫폼에 종속적인 구성 데이터가 필요합니다. 이를 위해 스펙은 전용 configs 개념을 정의합니다. 서비스 컨테이너 관점에서 configs는 컨테이너에 마운트되는 파일이라는 점에서 볼륨과 유사합니다. 하지만 실제 정의에는 이 유형으로 추상화된 고유한 플랫폼 리소스와 서비스가 포함됩니다.

secret은 보안 고려사항 없이는 노출되어서는 안 되는 민감한 데이터를 위한 특별한 형태의 구성 데이터입니다. secret은 컨테이너에 마운트되는 파일로 서비스에서 사용할 수 있지만, 민감한 데이터를 제공하기 위한 플랫폼별 리소스는 Compose 스펙 내에서 별도의 개념과 정의가 필요할 만큼 특수합니다.

> 참고: 볼륨, configs, secrets를 사용할 때 최상위 수준에서 간단한 선언을 하고 서비스 수준에서 플랫폼별 정보를 추가할 수 있습니다.

프로젝트는 플랫폼에서 애플리케이션 스펙의 개별 배포입니다. 최상위 name 속성으로 설정되는 프로젝트 이름은 리소스를 그룹화하고 다른 매개변수를 가진 동일한 Compose 스펙 애플리케이션의 다른 설치나 다른 애플리케이션과 격리하는 데 사용됩니다. 플랫폼에서 리소스를 생성하는 경우 리소스 이름 앞에 프로젝트를 붙이고 com.docker.compose.project 레이블을 설정해야 합니다.

Compose는 사용자 지정 프로젝트 이름을 설정하고 이 이름을 재정의할 수 있는 방법을 제공하므로, 동일한 compose.yaml 파일을 변경 없이 동일한 인프라에 두 번 배포할 수 있으며, 이는 단순히 고유한 이름을 전달하면 됩니다.

## Compose 파일

Compose 파일의 기본 경로는 작업 디렉터리에 위치한 compose.yaml(권장) 또는 compose.yml입니다. Compose는 이전 버전과의 호환성을 위해 docker-compose.yaml과 docker-compose.yml도 지원합니다. 두 파일이 모두 존재하는 경우 Compose는 표준인 compose.yaml을 선호합니다.

조각과 확장을 사용하여 Compose 파일을 효율적이고 유지보수하기 쉽게 만들 수 있습니다.

여러 Compose 파일을 병합하여 애플리케이션 모델을 정의할 수 있습니다. YAML 파일의 결합은 설정한 Compose 파일 순서에 따라 YAML 요소를 추가하거나 재정의하여 구현됩니다. 단순 속성과 맵은 가장 높은 순서의 Compose 파일로 재정의되고, 목록은 추가하여 병합됩니다. 병합되는 보완 파일이 다른 폴더에 있는 경우에도 상대 경로는 첫 번째 Compose 파일의 상위 폴더를 기준으로 해석됩니다. 일부 Compose 파일 요소는 단일 문자열이나 복잡한 객체로 모두 표현될 수 있으므로, 병합은 확장된 형식에 적용됩니다. 자세한 내용은 "여러 Compose 파일 작업하기"를 참조하세요.

다른 Compose 파일을 재사용하거나 애플리케이션 모델의 일부를 별도의 Compose 파일로 분리하려면 include를 사용할 수도 있습니다. 이는 Compose 애플리케이션이 다른 팀이 관리하는 다른 애플리케이션에 종속되어 있거나 다른 사람과 공유해야 하는 경우에 유용합니다.

## CLI

Docker CLI를 사용하면 docker compose 명령과 그 하위 명령을 통해 Docker Compose 애플리케이션과 상호 작용할 수 있습니다. CLI를 사용하여 compose.yaml 파일에 정의된 다중 컨테이너 애플리케이션의 수명 주기를 관리할 수 있습니다. CLI 명령을 사용하면 애플리케이션을 쉽게 시작, 중지 및 구성할 수 있습니다.

### 주요 명령어

compose.yaml 파일에 정의된 모든 서비스를 시작하려면:

```bash
docker compose up
```

실행 중인 서비스를 중지하고 제거하려면:

```bash
docker compose down
```

실행 중인 컨테이너의 출력을 모니터링하고 문제를 디버그하려면 다음과 같이 로그를 볼 수 있습니다:

```bash
docker compose logs
```

현재 상태와 함께 모든 서비스를 나열하려면:

```bash
docker compose ps
```

모든 Compose CLI 명령의 전체 목록은 참조 문서를 확인하세요.

## 예시

다음 예시는 위에서 설명한 Compose 개념을 설명합니다. 이 예시는 비규범적입니다.

프론트엔드 웹 애플리케이션과 백엔드 서비스로 분할된 애플리케이션을 고려해보겠습니다.

프론트엔드는 인프라에서 관리하는 HTTP 구성 파일로 런타임에 구성되며, 외부 도메인 이름과 플랫폼의 보안 시크릿 저장소에서 주입된 HTTPS 서버 인증서를 제공받습니다.

백엔드는 영구 볼륨에 데이터를 저장합니다.

두 서비스는 격리된 back-tier 네트워크에서 서로 통신하며, 프론트엔드는 front-tier 네트워크에도 연결되어 외부 사용을 위해 포트 443을 노출합니다.

### Compose 애플리케이션 예시

<img src="https://github.com/user-attachments/assets/bf4ce32e-dd92-411a-ac61-0187a91bf140" width="80%" height="80%"/>

예시 애플리케이션은 다음과 같은 부분으로 구성됩니다:

- Docker 이미지로 지원되는 2개의 서비스: webapp과 database
- 프론트엔드에 주입되는 1개의 시크릿(HTTPS 인증서)
- 프론트엔드에 주입되는 1개의 구성(HTTP)
- 백엔드에 연결된 1개의 영구 볼륨
- 2개의 네트워크

```yaml
services:
  frontend:
    image: example/webapp
    ports:
      - "443:8043"
    networks:
      - front-tier
      - back-tier
    configs:
      - httpd-config
    secrets:
      - server-certificate

  backend:
    image: example/database
    volumes:
      - db-data:/etc/data
    networks:
      - back-tier

volumes:
  db-data:
    driver: flocker
    driver_opts:
      size: "10GiB"

configs:
  httpd-config:
    external: true

secrets:
  server-certificate:
    external: true

networks:
  # 이러한 객체의 존재만으로도 정의하기에 충분합니다
  front-tier: {}
  back-tier: {}
```

`docker compose up` 명령은 프론트엔드와 백엔드 서비스를 시작하고, 필요한 네트워크와 볼륨을 생성하며, 구성과 시크릿을 프론트엔드 서비스에 주입합니다.

`docker compose ps`는 서비스의 현재 상태를 보여주어 어떤 컨테이너가 실행 중인지, 상태는 어떤지, 어떤 포트를 사용하고 있는지 쉽게 확인할 수 있게 해줍니다:

```bash
$ docker compose ps

NAME                IMAGE                COMMAND                  SERVICE             CREATED             STATUS              PORTS
example-frontend-1  example/webapp       "nginx -g 'daemon of…"   frontend            2 minutes ago       Up 2 minutes        0.0.0.0:443->8043/tcp
example-backend-1   example/database     "docker-entrypoint.s…"   backend             2 minutes ago       Up 2 minutes
```