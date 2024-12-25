---
layout: single
title:  "리눅스 웹 취약점 점검 조치"
categories:
  - Linux
tags:
  - [Linux]

toc: true
toc_sticky: true
---


## 리눅스 서버 웹 취약점 점검 조치 사항


**OS** : `Rocky Linux release 8.9 (Green Obsidian)`

<br>

## root 계정  원격 접속 제한
```bash
$ vim /etc/ssh/sshd_config
PermitRootLogin no #yes -> no로 설정
```

<br>

## 패스워드 복잡성 설정

```bash
$ vim /etc/security/pwquality.conf

# --------------------  아래 내용 설정 함.----------------------
# Number of characters in the new password that must not be present in the 
# old password.
# 새 비밀번호에 이전 비밀번호에 사용된 문자 중에 가능한 문자수
# 3 : 이전 비밀번호에서 사용된 문자 중 동일한 문자 3개 이상 사용 못함.
difok = 3
#
# Minimum acceptable size for the new password (plus one if
# credits are not disabled which is the default). (See pam_cracklib manual.)
# Cannot be set to lower value than 6.
# 비밀번호 길이. 6보다 작으면 안된다.
minlen = 8
#
# The maximum credit for having digits in the new password. If less than 0
# it is the minimum number of digits in the new password.
# 숫자 최소 사용 수 설정
dcredit = -1
#
# The maximum credit for having uppercase characters in the new password.
# If less than 0 it is the minimum number of uppercase characters in the new
# password.
# 대문자 최소 사용 수 설정
ucredit = -1
#
# The maximum credit for having lowercase characters in the new password.
# If less than 0 it is the minimum number of lowercase characters in the new
# password.
# 소문자 최소 사용 수 설정
lcredit = -1
#
# The maximum credit for having other characters in the new password.
# If less than 0 it is the minimum number of other characters in the new
# password.
# 특수 문자 최소 사용수 설정
ocredit = -1
#
# The maximum number of allowed consecutive same characters in the new password.
# The check is disabled if the value is 0.
# 반복된 문자 제한 설정
# aaa, 111 사용 못함.
maxrepeat = 3
#
# The maximum number of allowed consecutive characters of the same class in the
# new password.
# The check is disabled if the value is 0.
# 연속된 문자 수 제한 설정. 123, abc 사용 못함.
maxclassrepeat = 3
#
# Whether to check if it contains the user name in some form.
# The check is enabled if the value is not 0.
# 아이디를 비밀번호로 사용 했는지 체크 활성
usercheck = 1
```

<br>

## 계정 잠금 임계값 설정

```bash
$ vim /etc/pam.d/system-auth
# remember 변수 추가, 비밀번호 5회 실패 시 접속 불가.
password    sufficient                                   pam_unix.so sha512 shadow nullok use_authtok remember=5
```

<br>

## 패스워드 최소 길이, 최대 사용기간, 최소 사용기간 설정

```bash
$ vim /etc/login.defs

# Password aging controls:
#
#       PASS_MAX_DAYS   Maximum number of days a password may be used.
#       PASS_MIN_DAYS   Minimum number of days allowed between password changes.
#       PASS_MIN_LEN    Minimum acceptable password length.
#       PASS_WARN_AGE   Number of days warning given before a password expires.
#
PASS_MAX_DAYS   90  # 비밀번호 최대 사용 기간. 90일 마다 변경해야 함.
PASS_MIN_DAYS   1   # 비밀번호 최소 사용 기간. 1일이 지나야 비밀번호 변경 가능 함.
PASS_MIN_LEN    8   # 비밀번호 길이
PASS_WARN_AGE   7   # 비밀번호 만료 경고일
```

<br>

## 호스트 파일 소유자 및 권한 설정

```bash
$ chmod 600 /etc/hosts

# 결과
-rw------- 1 root root 751 2024-06-13 11:09 /etc/hosts
```

<br>

## nfs 설정 비활성화
```bash
$ systemctl status nfs-server.service

# 조회 결과
* nfs-server.service - NFS server and services
   Loaded: loaded (/usr/lib/systemd/system/nfs-server.service; disabled; vendor preset: disabled)
   Active: inactive (dead)
```

<br>

## automountd 제거
```bash
$ rpm -q autofs

# 조회 결과
autofs 패키지가 설치되어 있지 않습니다
```




