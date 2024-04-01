---
layout: single
title:  "Github SSH 설정"
categories:
  - Git
tags:
  - [Github, Ssh]

toc: true
toc_sticky: true
---

## github ssh 설정

```
- 보안 채널을 통해 github와 연결하고자 할 경우  
- github 계정을 여러개 연결해야 하는 경우
```

<br>

### 사전 준비
> github 계정 생성 완료  
> repository 생성 완료

<br><br>

### SSH 디렉토리 생성(있으면 SKIP) 및 이동
```shell
$ mkdir ~/.ssh; cd ~/.ssh;
```

<br><br>

### github 계정 이메일로 ssh-key 생성
- 계정이 여러개인 경우 아래 명령어의 계정 부분을 바꿔가며 등록해주면 됨.
- `f` 옵션 명으로 설정한 `private key`와 `public key`가 생성된다.
- 만약 `f` 옵션을 주지 않으면 기본값인 `id_rsa`로 생성된다. 만약 기존에 생성되어 있는 key가 있는 경우 `override`되어 기존 key가 사라질 수 있다. 조심하자. 기존에 잘 되던 서버 연결이 안될 수 있다.

    ```shell
    $ ssh-keygen -t rsa -C "user1@gmail.com" -f "id_rsa_user1"

    # 계정이 여러개인 경우
    Ex. $ ssh-keygen -t rsa -C "user2@gmail.com" -f "id_rsa_user2"
    Ex. $ ssh-keygen -t rsa -C "user3@gmail.com" -f "id_rsa_user3"
    ```

- 간단한 옵션 설명
  
    `t` : 암호화하는 타입이다. 특별한 경우가 아니라면 가장 자주 사용하는 rsa 타입으로 설정.  
    `b` : 암호의 bit수를 의미하며 default값은 2048이다.(생략 가능)  
    `f` : 저장할 파일 위치와 이름.  
    `C` : 주석 부분으로 역할이 특별히 없다.  

<br>

- 1번, 2번은 그냥 엔터로 넘어가자.
    > ssh 접속할 때 비밀번호를 설정하는 단계이다.  
    > 우리는 ssh-agent를 통해 키 관리를 할 예정이므로 그냥 넘어가자.

    ![image](https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/152112b0-946d-4ea2-a7ed-e97cd36266bd)

<br>

- ssh-key 생성 확인  
    > id_rsa_midobandev - 개인 키  
    > id_rsa_midobandev.pub - 공개 키

  ![image](https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/81d0d81c-35cb-436b-a449-3eb272f30761)

<br><br>

### ssh-agent 실행 및 개인 키 저장
```
1. 키 저장 및 관리: 사용자의 SSH 개인 키를 메모리에 로드하여, 필요할 때마다 사용할 수 있도록 관리합니다. 이는 키를 디스크에 저장하는 것보다 안전합니다.

2. 자동 인증: ssh-agent에 로드된 키를 사용하여 SSH 접속 시 자동으로 인증 과정을 수행할 수 있습니다. 이를 통해, 사용자는 SSH 접속 시 매번 키의 패스워드를 입력할 필요 없이 보다 원활하게 접속할 수 있습니다.

3. 보안 강화: 키 자체는 ssh-agent가 실행되는 동안만 메모리에 유지되며, 시스템이 종료되면 메모리에서 사라지므로 보안성이 강화됩니다. 또한, ssh-agent는 개인 키를 직접적으로 노출하지 않기 때문에 보안에 유리합니다.

4. 다중 키 관리: 여러 개의 SSH 키를 사용하는 경우, 각각의 키를 ssh-agent에 추가함으로써 여러 서버나 서비스에 대한 접속을 간편하게 관리할 수 있습니다.
```

<br>

- agent 실행
  
    ```shell
    $ eval "$(ssh-agent -s)"
    ```
    ![image](https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/36989a15-111a-4ca9-bf23-b0068cac3a37)

<br>

- 개인 키 저장 (계정인 여러개인 경우 각각의 개인 키를 등록해 주면 됨)

    ```shell
    $ ssh-add ~/.ssh/id_rsa_midobandev

    # 계정이 여러개인 경우
    Ex. ssh-add ~/.ssh/id_rsa_user1
    Ex. ssh-add ~/.ssh/id_rsa_user2
    ```

- 개인 키 저장 확인 
  
    ```shell
    $ ssh-add -l
    ```  
  - 저장된 개인 키 없는 경우  
    
    ![image](https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/cfd96841-6c0f-476e-bfab-b5a0d8da9636)

  - 저장된 개인 키 있는 경우  
  
    ![image](https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/ffd0ca34-e1be-4bd5-8f80-c4efa9ac836f)

<br><br>

### SSH config 파일 추가 및 설정

- config 파일 없는 경우 추가 : 1번 또는 2번으로 진행.
    ```shell
    # 1. 파일 생성
    $ touch ~/.ssh/config

    # 2. 편집 후 저장하면서 생성
    $ vi ~/.ssh/config
    ```

- config 파일 수정( 위에서 2번으로 진행한 경우 SKIP)
    ```shell
    $ vi ~/.ssh/config
    ```

- 아래 내용 입력
    ```shell
    #jane 계정에 대한 SSH 설정
    Host github.com-원하는단어       # ssh 연결 시 Host 지시자로 사용 됨. 구분하기 좋은 단어 사용.
         HostName github.com         # 변경 없음.
         User github 계정            # github 이메일 계정
         IdentityFile 개인 키 경로    # Ex.~/.ssh/id_rsa_user1

    Ex.
    Host github.com-midoban
         HostName github.com
         User midoban@gmail.com
         IdentityFile ~/.ssh/id_rsa_midoban
        
    #계정 여러개인 경우 추가
    Host github.com-user1
         HostName github.com
         User user1@gmail.com
         IdentityFile ~/.ssh/id_rsa_user1
    ```

<br><br>

### Github에 SSH Public Key 추가해 주기   

- public key 복사하기 : 편집기 열어서 복사하면 된다.

    ```shell
    # 편집기 열기
    $ code ~/.ssh/id_rsa_user1.pub
    또는
    $ vi ~/.ssh/id_rsa_user1.pub
    ```

<br>

- github 계정 로그인 - `프로필` 클릭 - `Settings`

    ![image](https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/e4c3134c-05d7-4bfc-a4ad-b32f29362609)

<br>

- 왼쪽 대시보드 `SSH and GPG keys` 클릭 후 우측 `New SSH Key` 클릭 
- 3번은 key 생성 후 결과 
  
    ![image](https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/b0049e90-55ff-4543-b004-63541b328214)

<br>

- 위 2번 클릭 시 아래 `SSH Key` 추가 화면으로 이동. `key` 항목에 public key 붙여 놓으면 된다.

    ![image](https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/ffa15b3c-3963-4e07-a9a4-7c8c147f561e)

<br><br>

### SSH 연결 테스트
- `config` 파일에서 `Host`로 지정한 주소를 `git@` 다음에 넣어 준다.
  
    ```shell
    $ ssh -T git@github.com-user1
    ```
    ![image](https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/143f3e9c-051d-43d9-b7a3-9662d1570518)


<br><br>



### github repository Clone 

- github에 있는 Repository를 Local로 가져와 보자.
- github 사이트 접속 후 > `repository`로 이동하여 `Code` 클릭 → `SSH` 선택 후 복사 해주자.

    ![image](https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/abe5ea86-3f8b-417f-8161-81ba7ec231d7)

<br>

- 프로젝트 Clone 할 위치로 이동
    ```shell
    $ cd 원하는 위치 이동
    ```

<br>

- 복사한 SSH 주소 값 중 `git@` 와 `:깃헙주소` 사이의 `github.com` 값을 `config` 파일에 `HOST` 항목으로 설정 주소 값으로 변경해주자.
  
  ```shell
  # github 에서 복사한 SSH 주소 값
  git@github.com:깃헙주소

  # 아래와 같이 변경해 주자
  git@github.com-user1:깃헙주소
  ```

- 위에서 변경한 주소로 clone 하기

    ```shell
    $ git clone git@github.com-user1:깃헙주소
    ``` 

<br><br>

### 커밋이나 푸쉬 하기 전 user.name과 user.email을 확인해서 수정해주고 커밋하고 푸쉬해야한다.
- Repository Clone 한 위치로 이동 후 진행. 즉, `.git` 파일이 있는 경로로 이동 후 계정 확인 및 수정을 해줘야 한다.
- `.git` 별로(repository 별로) 따로 지정하지 않은 경우 global로 설정된 계정이 적용된다.

    ```shell
    # 계정 이름 및 이메일 확인
    $ git config user.name
    $ git config user.email

    # 계정 이름 및 이메일 수정
    $ git config user.name 계정이름
    $ git config user.email 계정이메일

    Ex. $ git config user.name user1
    Ex. $ git config user.email user1@gmail.com
    ```

<br><br>

### github 연동 확인
- 다 설정 후 `.git` 경로로 이동 후 `git remote -v` 명령어를 통해 설정된 github 주소를 확인해 보면 잘 되어 있는 걸 볼 수 있다.

    ![image](https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/07754e92-1315-48a0-a27c-06fbe0b8c614)
