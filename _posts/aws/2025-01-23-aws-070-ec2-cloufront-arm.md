---
layout: single
title:  "AWS와 외부 도메인 관리 및 CloudFront 연동 가이드"
categories:
  - Aws
tags:
  - [Aws, CloudFront, Route53]

toc: true
toc_sticky: true
---

### AWS와 외부 도메인 관리 및 CloudFront 연동 가이드

#### **1. AWS Route 53의 개념과 역할**

**Amazon Route 53**은 AWS에서 제공하는 **확장 가능하고 고가용성의 DNS(Domain Name System) 웹 서비스**입니다. Route 53의 주요 역할은 다음과 같습니다:

- **DNS 서버 역할**: 도메인 이름을 IP 주소로 매핑하여 사용자가 입력한 도메인 요청을 올바른 리소스로 전달합니다.
- **트래픽 관리**: 가중치 기반, 지연 시간 기반 등의 다양한 라우팅 정책을 제공해 트래픽을 효율적으로 제어합니다.
- **네임 서버 제공**: Route 53은 도메인의 네임 서버를 관리하며, 이를 통해 DNS 레코드를 설정 및 관리할 수 있습니다.
- **도메인 등록 및 관리**: 직접 도메인을 등록하거나 외부 도메인을 Route 53으로 이전할 수 있습니다.

#### **2. CloudFront와 외부 도메인 연동 시 주요 개념과 설정**

**Amazon CloudFront**는 콘텐츠를 빠르고 안전하게 배포하는 AWS의 CDN(Content Delivery Network) 서비스입니다. 외부 도메인을 CloudFront와 연동할 때는 DNS 설정과 CloudFront 내부 설정이 필요합니다.

##### **2.1 CNAME 레코드의 개념과 CloudFront에서의 역할**
- **DNS CNAME 레코드**:
  - 한 도메인을 다른 도메인의 별칭으로 설정.
  - 예: `www.example.com`의 CNAME 레코드를 `example.com`으로 설정하면, `www.example.com`으로 접속 시 DNS가 `example.com`을 참조합니다.

- **CloudFront의 CNAME 설정**:
  - CloudFront에서는 "대체 도메인 이름(Alternate Domain Name, CNAME)"이라는 용어로 사용됩니다.
  - 역할: CloudFront가 특정 도메인 요청을 처리하도록 허용하는 인증 단계.
  - 예: `www.example.com`을 CloudFront의 CNAME으로 추가하면, CloudFront는 해당 도메인의 요청을 처리합니다.

##### **2.2 CNAME 설정 혼란의 정리**
- **일반 CNAME 동작**: "A 도메인의 CNAME 레코드를 B 도메인으로 설정" → 사용자가 A로 접속하면 DNS가 이를 B로 해석.
- **CloudFront의 CNAME**: DNS CNAME과는 별개로, CloudFront 배포에 요청을 허용하기 위한 내부 인증 설정.
- 혼란을 방지하려면 CloudFront CNAME을 "대체 도메인 이름"으로 이해하는 것이 적합합니다.

#### **3. 외부 도메인과 CloudFront 연동**

외부 도메인과 CloudFront를 연결할 때 두 가지 주요 상황이 존재합니다:

##### **3.1 Route 53과 함께 사용하는 경우**
1. Route 53에서 **호스팅 영역(Hosted Zone)** 생성:
   - 도메인 이름과 동일한 호스팅 영역을 생성.
   - 생성 시 Route 53은 네임 서버(NS) 레코드를 자동으로 제공.
2. 외부 도메인의 네임 서버를 Route 53으로 변경:
   - 도메인 등록 기관의 DNS 관리 콘솔에서 Route 53이 제공한 네임 서버를 설정.
3. Route 53에서 CloudFront와 연결:
   - 도메인의 **별칭 레코드(Alias Record)**를 생성.
   - 별칭 대상: CloudFront 배포 도메인 (예: `d123456abcdef8.cloudfront.net`).

##### **3.2 Route 53 없이 사용하는 경우**
1. 외부 도메인의 DNS에서 CNAME 설정:
   - 외부 DNS 관리 콘솔에서 도메인의 CNAME 레코드를 추가.
   - **호스트 이름**: 연결하려는 서브도메인 (예: `www`).
   - **CNAME 값**: CloudFront 배포 도메인 (예: `d123456abcdef8.cloudfront.net`).
2. CloudFront의 CNAME 설정:
   - CloudFront 배포 설정에서 "대체 도메인 이름"에 해당 도메인 추가.

#### **4. ACM과 외부 도메인 CNAME 설정 이유**

**AWS Certificate Manager(ACM)**는 HTTPS를 사용하기 위해 SSL/TLS 인증서를 관리하는 서비스입니다. 외부 도메인과 CloudFront를 HTTPS로 연결하려면 다음 이유로 ACM에서 제공하는 CNAME 레코드를 외부 DNS에 설정해야 합니다:

1. **도메인 소유권 검증**:
   - ACM은 제공된 CNAME 레코드를 통해 해당 도메인이 소유자의 것임을 검증.
   - 외부 도메인의 DNS에 ACM의 CNAME 레코드를 등록해야 검증이 완료됩니다.

2. **HTTPS 활성화**:
   - CloudFront 배포에서 HTTPS를 사용하려면, ACM 인증서를 연결해야 합니다.
   - ACM 인증서는 미국 동부(버지니아 북부) 리전에서 발급되어야 합니다.

#### **5. 상황별 설정 예시**

##### **5.1 Route 53과 외부 도메인을 사용하는 경우**
- **DNS 구성**:
  - Route 53에서 별칭 레코드 생성.
  - 별칭 대상: CloudFront 배포 도메인.
- **CloudFront 설정**:
  - 대체 도메인 이름: 외부 도메인 추가.
  - ACM 인증서 연결.

##### **5.2 Route 53 없이 외부 도메인만 사용하는 경우**
- **DNS 구성**:
  - 외부 DNS에서 CNAME 레코드 추가.
  - CNAME 대상: CloudFront 배포 도메인.
- **CloudFront 설정**:
  - 대체 도메인 이름: 외부 도메인 추가.
  - ACM 인증서 연결.

#### **6. 결론**
- Route 53은 AWS의 DNS 서버로, 외부 도메인과 CloudFront를 쉽게 연결하고 고급 트래픽 관리 기능을 제공합니다.
- CloudFront의 CNAME 설정은 DNS CNAME 레코드와는 다른 역할을 하며, CloudFront 배포와 특정 도메인을 연결하기 위한 인증 단계입니다.
- ACM 설정은 HTTPS를 활성화하기 위해 필수이며, 외부 도메인의 DNS에 CNAME 레코드를 등록해야 소유권을 검증할 수 있습니다.
- 상황에 따라 Route 53을 활용하거나 외부 DNS 관리만으로도 CloudFront와 외부 도메인을 연결할 수 있습니다.

