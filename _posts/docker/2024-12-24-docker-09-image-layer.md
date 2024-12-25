---
layout: single
title:  "이미지 레이어(Image Layer)"
categories:
  - Docker
tags:
  - [Docker, Image, Image-layer]

toc: true
toc_sticky: true
---

<br>

**본 내용은 인프런의 데브위키님 강의 "[개발자를 위한 쉬운 도커](https://www.inflearn.com/course/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EC%89%AC%EC%9A%B4-%EB%8F%84%EC%BB%A4)" 내용을 바탕으로 정리한 내용입니다.**

<br>

## 레이어드 파일 시스템(Layered File System)
도커 이미지는 저장소를 효율적으로 사용하기 위해서 `레이어드 파일 시스템(layered file system)`으로 구성되어 있다.
레이어(layer)라는 것은 하나의 층을 의미하고, 여러 개의 층으로 구성되어 있는 것에서 하나의 층을 `레이어(layer)`라고 표현한다.

Nginx를 실행했을 때 출력을 확인해 보자. 화면을 보면 Nginx라는 하나의 이미지를 다운받는 과정에서 `pull` 이 `여러 단계에 걸쳐서 실행`되는 것이 보인다.
여기서 한 줄 한 줄이 각각 하나의 레이어를 의미한다. 이 레이어들이 모여서 하나의 이미지로 구성되는 것이다. 각각의 레이어는 이미지의 일부분을 나타낸다.

<img src="https://github.com/user-attachments/assets/910dc4b3-20ba-4e03-a00d-02ab9195b8fb" width="90%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 레이어드 파일 시스템의 장점
하나의 이미지를 여러 개의 레이어로 구성하는 이유는 `재사용하기 유리한 구조`이기 때문이다. 레이어드 파일 시스템을 사용하면 `공간을 효율적으로 사용`할 수 있다.
그래서 이미지를 저장하고 전송할 때 스토리지와 네트워크 사용량을 절약할 수 있다.

건축 도면을 예로 레이어 구조를 이해해 보자.
건축 도면을 그리기 위해 투명한 도면 용지(레이어)를 여러 장 준비한다.
각각의 도면 용지에 건물의 구조, 토목, 전기, 조경 등 `각각의 주제별로 한 장씩` 그린다. 
이제 각각의 `도면 용지(레이어)를 하나로 합치면` 완성된 설계도 A(하나의 이미지)가 될 것이다.

<img src="https://github.com/user-attachments/assets/ee6a1d58-942f-40f6-9359-3b19fbee183b" width="60%" height="80%"/>
<br><br>

만약 설계도에서 조경 부분을 수정해야 하는 경우 이 설계도 A가 한 장의 도면으로 되어 있었다면 조경 부분을 수정하면 전체 도면이 영향을 받게 된다.
하지만 레이어 구조로 되어 있으면 조경 부분만 수정하면 나머지 전기, 토목, 구조는 변경의 영향을 받지 않게 된다. 즉, 변경 사항에 있어서 재활용이 유리한 구조라는 겁니다

<img src="https://github.com/user-attachments/assets/e10edc56-9b12-49b9-bda9-f1d824d2ab78" width="90%" height="80%"/>
<br><br>

그리고 이 설계도 A와 구조, 토목, 조경 부분이 동일하고 전기 배선만 다른 B 건물이 있다고 생각해 보자. 
만약에 레이어드 구조가 아니었으면 설계도 A와 설계도 B 두 장을 각각 통째로 관리해야 한다. 
하지만 레이어 구조를 활용하면 `겹치는 도면(레이어)는 재사용`할 수 있다.
이렇게 겹치는 레이어는 재사용하는 것이 종이 사용량도 줄이고 보관하기에도 효율적이다.

그리고 이렇게 겹치는 레이어를 재사용한 상태에서 왼쪽 순서대로 읽으면 설계도 A, 오른쪽 순서로 읽으면 설계도 B로 읽을 수 있다.
설계도 A의 조경, 토목 구조가 설계도 B와 겹치기 때문에 받는 사람은 전기 도면 한 장만 받으면 설계도 B를 만들 수 있게 된다.
만약 레이어드 구조가 아니었으면 전기 부분이 다르다는 것은 전체 설계도가 다르다는 걸 의미하기 때문에 완전히 `새로운 설계도 한 장을 통째로 전달`해야 된다. 즉 레이어 구조는 데이터 전송에 있어서도 더 유리한 구조이다.
<img src="https://github.com/user-attachments/assets/ac4a6d6a-b40d-44b5-83fd-547d6bbe5ba9" width="90%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 이미지 레이어
이미지의 레이어는 건축 도면과 비슷하지만 조금 다른 방식으로 구성된다. 이미지의 레이어는 바로 직전 단계의 레이어에서 변경된 내용들만 저장된다.

서버에 간단한 페이지를 출력하는 NGINX를 설치 한다고 생각해보자. 
- 먼저 OS를 설치한다.
- OS 위에 NGINX 소프트웨어를 설치한다.
- NGINX 설정 파일을 수정한다.
- 브라우저에서 표시되는 index.html 파일을 사용자에게 응답할 내용으로 수정한다.

이제 이 순서대로 이미지의 레이어를 구성한다고 생각해보자. 
- 먼저 OS를 준비한다.
- 그리고 OS 위에서 NGINX 소프트웨어를 설치한다.  
  소프트웨어를 설치하면 OS의 특정 폴더에 NGINX 소프트웨어와 관련된 파일들이 추가된다. 그래서 NGINX를 설치한다는 것은 기존 OS 파일 시스템에 추가되는 부분이 생기는 것을 말한다.
- 그리고 NGINX 설정 파일을 수정한다.
- 다음으로 브라우저에서 표시되는 index.html 파일을 사용자에게 응답할 내용으로 수정한다.

<img src="https://github.com/user-attachments/assets/8ef1e083-e4ef-489a-9b20-c2143b7dd116" width="80%" height="80%"/>

이렇게 **기존 레이어에서 변경되는 것들은** 기존의 레이어를 수정하는 것이 아니라 전 레이어와 비교해서 `추가되거나 변경된 파일들이 다음 레이어로 저장`되는 것이다. 그래서 두 번째 NGINX 설치 레이어에는 이전 레이어인 OS 레이어에서 `NGINX 소프트웨어가 추가된 부분`만 따로 가지고 있다.

이미지에서 `한 번 저장된 레이어는 변경할 수 없다.` 변경 사항이 있으면 계속 새로운 레이어로 저장해야 한다. 마치 소스 코드에서 한번 푸시한 내용은 되돌릴 수 없는 것과 같다.

이렇게 이미지 A를 완성한 다음에 이미지 B를 새로 만드는데 이미지 B는 마지막 단계에서 `index.html 파일의 내용을 다른 내용으로 변경`한다고 가정해보자. 이미지 B를 만들 때 NGINX의 소프트 버전과 NGINX 설정을 이미지 A와 모두 동일하게 설정하면 세 번째 Nginx 설정 레이어까지는 재사용을 하게 된다. 마지막 index.html 파일만 내용이 다르기 때문에 `마지막 레이어만 새롭게 추가`되어서 이미지 B가 완성된다. 그래서 이미지 A와 B는 총 `3개의 레이어를 공유`하고 `각각 하나의 레이어를 별도로 사용`하는 구조가 된다.

정리하자면 이미지의 레이어는 순차적으로 쌓이고, 각각의 레이어는 이전 레이어에서 변경된 부분을 저장하고 있다 그리고 같은 변경이 일어난 레이어는 공유해서 재사용할 수 있다

<img src="https://github.com/user-attachments/assets/d0aa499d-477b-4fef-917d-17691e12c460" width="80%" height="80%"/>

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>



<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>
<img src="" width="90%" height="80%"/>

<!--<img src="" width="80%" height="80%"/>-->
