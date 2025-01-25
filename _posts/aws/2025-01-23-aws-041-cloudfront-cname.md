---
layout: single
title:  "[AWS] CNAME 개념과 동작 원리(feat. CloudFront)"
categories:
  - Aws
tags:
  - [Blog, jekyll, Github, Git]

toc: true
toc_sticky: true
---

### CNAME 개념과 동작 원리 정리

#### **1. CNAME의 기본 개념**
- **CNAME(Canonical Name)**은 하나의 도메인을 다른 도메인의 **별칭(alias)**으로 설정하는 DNS 레코드 타입입니다.
- CNAME은 도메인 이름 간의 매핑만 정의하며, 최종적으로 참조하는 도메인의 A 레코드(IP 주소)를 반환합니다.

#### **2. CNAME의 동작**
1. **CNAME 설정 예시**:
   - `www.example.com`의 CNAME 레코드를 `example.com`으로 설정.
2. **동작 과정**:
   - 사용자가 `www.example.com`에 요청.
   - DNS는 `www.example.com`의 CNAME 레코드를 확인하고, `example.com`으로 요청을 전달.
   - 최종적으로 `example.com`의 A 레코드(IP 주소)를 반환하여 연결.

#### **3. CloudFront와 CNAME의 차이점**
- **DNS CNAME**:
  - DNS 수준에서 도메인 요청을 다른 도메인으로 매핑.
  - 예: `www.example.com` → `example.com`.

- **CloudFront의 CNAME(대체 도메인 이름)**:
  - CloudFront 배포가 특정 도메인 요청을 처리하도록 허용하는 인증 단계.
  - DNS의 CNAME 레코드와는 별개의 설정이며, CloudFront와 연결될 도메인을 지정하는 역할을 합니다.

#### **4. CloudFront와 CNAME 설정 시 동작**
1. **외부 DNS에서 CNAME 설정**:
   - 외부 DNS에서 `A 도메인`의 CNAME을 CloudFront 도메인(예: `d123456abcdef8.cloudfront.net`)으로 설정.
   - 사용자가 `A 도메인`으로 요청하면 DNS는 CloudFront 도메인으로 연결.

2. **CloudFront 내부 CNAME 설정**:
   - CloudFront에서 "대체 도메인 이름"으로 `A 도메인`을 추가.
   - CloudFront는 해당 도메인의 요청을 허용하고 처리.

#### **5. 주의사항**
1. **CNAME의 방향성**:
   - CNAME은 항상 "설정된 도메인(A) → 참조 도메인(B)" 방향으로 동작.
   - 예: `A.example.com`의 CNAME이 `B.example.com`이라면, 사용자가 `A.example.com`에 접속 시 DNS가 이를 `B.example.com`으로 해석.

2. **CloudFront와 일반 CNAME의 차이**:
   - 일반 CNAME은 도메인을 다른 도메인으로 매핑하는 기능만 제공.
   - CloudFront의 CNAME(대체 도메인 이름)은 요청 인증 및 CloudFront 배포와의 연계를 위한 설정.

3. **Route 53과 CNAME**:
   - Route 53을 사용하면 별칭(Alias) 레코드를 통해 CloudFront를 연결 가능.
   - 별칭 레코드는 CNAME과 유사하지만, A 레코드처럼 작동해 루트 도메인(`example.com`)에도 적용 가능.

#### **6. 요약**
- CNAME은 DNS에서 도메인 간 매핑을 제공하며, "설정된 도메인(A)을 참조 도메인(B)의 별칭으로 만드는 것"을 의미합니다.
- CloudFront의 CNAME은 DNS의 CNAME과는 개념적으로 다르며, CloudFront 배포와 특정 도메인을 연결하기 위한 인증 역할을 합니다.
- 외부 도메인과 CloudFront를 연결할 때는 DNS에서 CNAME 설정과 CloudFront의 CNAME 설정이 모두 필요합니다.

