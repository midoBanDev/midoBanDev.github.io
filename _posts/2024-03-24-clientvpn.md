---
layout: single
title:  "AWS Client VPN 설정"
categories:
  - Aws
tags:
  - [Blog, jekyll, Github, Git]

toc: true
toc_sticky: true
---


## AWS Client VPN 설정
- 가이드 문서 링크 : [AWS Client VPN 설정 공식 가이드 문서](https://docs.aws.amazon.com/ko_kr/vpn/latest/clientvpn-admin/cvpn-getting-started.html#cvpn-getting-started-certs)

<br><br>

## VPC 및 Client VPN 엔드포인트의 구성
![image](https://github.com/wglee-github/develop-issue/assets/102303114/cf1d5789-8556-4c34-aa22-bc524ddc3f11)

<br>

### 필독사항
> 아래 내용은 위 공식 가이드 문서를 쉽게 이해하기 위한 도움 문서이다. 
따라서 공식 문서에 대한 참고용으로 활용하기 바란다. 
설치 및 설정은 과정은 모두 가이드 문서를 참고해야 한다.

<br><br>

## 1단계: 서버와 클라이언트 인증서 및 키 생성
- 서버와 클라이언트에서 사용할 인증서 및 키를 생성하는 단계이다.  
- 서버는 AWS의 '_Client VPN 엔드포인트_' 를 말하고, 클라이언트는 '_사용자 PC_' 라고 이해하면 된다.
- Client VPN 엔드포인트는 쉽게 말해 우리가 집에서 PC를 통해 AWS의 특정 서버에 접근하고자 할 때 중간에서 VPN 연결을 하도록 도와주는 서비스라 생각하면 된다.
- 1단계에서 생성하는 인증서와 키는 서버 용과 클라이언트 용 각각 생성해야 한다.
- 생성된 인증서와 키를 AWS에서는 Client VPN 엔드포인트를 통해 관리되고, 사용자 PC에서는 별도의 Openvpn 프로그램을 통해 관리된다.

<br>

#### 1단계 설명 중 '상호인증' 부분에 대한 이해를 돕기 위해 작성한다.
```
# 가이드 문서 중 내용 발췌
이 목적으로 사용할 인증서가 아직 없는 경우 OpenVPN easy-rsa 유틸리티를 사용하여 인증서를 생성할 수 있습니다.  
[OpenVPN easy-rsa 유틸리티]를 사용하여 서버 및 클라이언트 인증서와 키를 생성하고  
ACM으로 가져오는 자세한 단계는 [상호 인증] 단원을 참조하세요.
```
- '상호인증' 부분은 실질적으로 인증서와 키를 생성하는 설명이다.
- AWS 상호인증 가이드 문서 링크 : [상호인증 가이드 문서](https://docs.aws.amazon.com/ko_kr/vpn/latest/clientvpn-admin/mutual.html)

<br>

**아래 번호는 가이드 문서 기준으로 작성되었다.**

→ 3번~8번 : [인증서 및 키 생성](https://github.com/wglee-github/develop-issue/wiki/AWS-Client-VPN-%EC%83%81%ED%98%B8%EC%9D%B8%EC%A6%9D)
→ 9번 : 마지막 업르드를(10번)을 위한 준비과정이다. 아무 폴더나 생성해서 가이드 문서대로 인증서와 키 파일을 옮겨주자.   
→ 10번 : 명령어 실행 전 사전 준비가 필요하다.
1. aws 명령어를 사용하기 위해서는 `AWS CLI`를 설치해야 한다. [AWS CLI 설치 가이드](https://github.com/wglee-github/develop-issue/wiki/AWS-CLI-%EC%84%A4%EC%B9%98)  

2. 설치가 완료 되었으면 `aws configure` 설정을 해줘야 한다.  
   
   a. 우선 AWS 계정과 연동을 위한 `access key`를 발급 받자. [AWS configure access key 발급 가이드](https://github.com/wglee-github/develop-issue/wiki/AWS-config-access-key)
   
   b. access key 발급 후 `aws configure` 설정을 해보자. [AWS configure 설정 가이드](https://github.com/wglee-github/develop-issue/wiki/aws-configure)


3. 서버 인증서와 클라이언트 인증서를 업로드 해주자. [인증서 및 키 업로드](https://github.com/wglee-github/develop-issue/wiki/ACM-Upload) 



## 2단계: 클라이언트 VPN 엔드포인트 생성

- 엔드포인트 생성 전 알아야할 정보 : `클라이언트 IPv4 CIDR`, `DNS 서버 IP`, `VPC`  
  - [클라이언트 VPN 엔드포인트 생성 가이드](https://github.com/wglee-github/develop-issue/wiki/AWS-Endpoint)



## 3단계: 대상 네트워크 연결

- [대상 네트워크 연결 가이드](https://github.com/wglee-github/develop-issue/wiki/Target-Network) 



## 4단계: VPC에 대한 권한 부여 규칙 추가

- [권한 부여 규칙 가이드](https://github.com/wglee-github/develop-issue/wiki/VPC-Authorization-Rules)



## 5,6 단계: SKIP



## 7단계: Client VPN 엔드포인트 구성 파일 다운로드

- [엔드포인트 구성 파일 다운로드 가이드](https://github.com/wglee-github/develop-issue/wiki/Client-Config-File-Download)



## 8단계: Client VPN 엔드포인트에 연결

- [Openvpn 기반 클라이언트 애플리케이션 설치 가이드](https://github.com/wglee-github/develop-issue/wiki/Openvpn-install)



## Client VPN 엔드포인트 삭제

1. 대상 네트워크 연결 해제
2. 권한 부여 규칙 제거
3. 1,2번이 삭제 된 후 Client VPN 엔드포인트 삭제가 가능하다.