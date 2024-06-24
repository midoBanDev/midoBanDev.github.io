---
layout: single
title:  "JDBC 설정 정보 암호화 - Jasypt 사용"
categories:
  - Java
tags:
  - [JDBC, Jasypt, Encryption]

toc: true
toc_sticky: true
---

# JDBC 설정 정보 암호화

- JDBC 설정 정보를 평문으로 사용할 경우 보안에 매우 취약해 질 수 있다.
- 따라서 JDBC 설정 정보를 암호화하여 관리하고자 한다.
- 암/복호화는 `Jasypt` 라이브러리를 사용한다. 
- `Jasypt`의 암/복호화에 사용되는 `비밀번호`를 암호화하여 관리하는 방법을 제공한다.

<br>

## Jasypt
```
Jasypt는 개발자가 암호화 작동 방식에 대한 깊은 지식 없이도 최소한의 노력으로 자신의 프로젝트에 기본 암호화 기능을 추가할 수 있도록 하는 Java 라이브러리이다.

http://www.jasypt.org/
```

<br>

## 1. pom.xml 의존성 추가
- Jasypt 라이브러리 : 암/복호화 Java 라이브러리
- Bouncy Castle 라이브러리 : 다양한 암호화 알고리즘을 지원하는 Java 라이브러리

```xml
<!-- https://mvnrepository.com/artifact/com.github.ulisesbocchio/jasypt-spring-boot-starter -->
<dependency>
  <groupId>com.github.ulisesbocchio</groupId>
  <artifactId>jasypt-spring-boot-starter</artifactId>
  <version>3.0.5</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk18on -->
<dependency>
  <groupId>org.bouncycastle</groupId>
  <artifactId>bcprov-jdk18on</artifactId>
  <version>1.78.1</version>
</dependency>
```

<br>

<details>
<summary>알고리즘 종류</summary>

- 테스트

</details>

<details>
<summary> 
<b><span style="font-size:130%;">알고리즘 종류</span></b>
</summary>

- Jasypt는 기본적으로 JDK에서 제공하는 표준 알고리즘을 사용한다.  
  - PBEWithMD5AndDES
  - PBEWithMD5AndTripleDES
  - PBEWithSHA1AndDESede
  - PBEWithSHA1AndRC2_40
  - AES (Advanced Encryption Standard)
  - DESede (Triple DES)
  - RSA (Rivest-Shamir-Adleman)

* Bouncy Castle 라이브러리에서 지원되는 알고리즘
  * PBEWithSHA256And128BitAES-CBC-BC
  * PBEWithSHA256And192BitAES-CBC-BC
  * PBEWithSHA256And256BitAES-CBC-BC

- Bouncy Castle 라이브러리가 지원하는 암호화 알고리즘의 특징
  - PBE (Password-Based Encryption): 비밀번호를 기반으로 키를 생성하는 암호화 방식이다.
  - SHA-256: 해시 알고리즘으로, 256비트 해시 값을 생성한다. 이는 PBE의 키 스트레칭 단계에서 사용됩니다.
  - 128비트 AES: 고급 암호화 표준(AES)으로, 128비트 키 길이를 사용한다.
  - CBC (Cipher Block Chaining): 블록 암호화 모드로, 각 블록 암호화 시 이전 블록의 암호문을 사용하여 보안을 강화한다.
  - BC (Bouncy Castle): Bouncy Castle 프로바이더를 지정하여 해당 알고리즘을 사용함을 의미한다.
  - 이 알고리즘은 PBE와 AES, SHA-256을 결합하여 강력한 보안 수준을 제공한다. PBE는 사용자가 입력한 비밀번호를 기반으로 안전한 암호화 키를 생성하며, SHA-256 해시 알고리즘을 사용하여 키 스트레칭을 통해 보안을 강화한다. 생성된 키는 128비트 AES 암호화 알고리즘을 사용하여 데이터 암호화에 사용됩니다. CBC 모드는 각 블록을 암호화할 때 이전 블록의 암호문을 사용하므로 동일한 입력 데이터라도 서로 다른 암호문을 생성한다.

</details>

<br><br>

## 2. JasyptConfig class 추가

```java
package com.crizen.config;

import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.crizen.util.EncryptionUtil;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

	@Value("${jasypt.encryptor.password}")
	private String PASSWORD_KEY;
	
	@Value("${jasypt.encryptor.iv-spec}")
	private String IV_SPEC;
	
	@Bean("jasyptStringEncryptor")
	StringEncryptor stringEncryptor() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setProvider(new BouncyCastleProvider());
		config.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");
		config.setPassword(getPassword());
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("2");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator"); 
		config.setStringOutputType("base64");
        encryptor.setConfig(config);
		
		return encryptor;
	}
	
  /**
   * 암호화 된 키를 복호화 하여 얻어온다.
   */
	private String getPassword() {
		String password = EncryptionUtil.getKey("getPwd");
		String salt = EncryptionUtil.getKey("getSalt");
		String decrypteText = "";
		try {
			SecretKey key = EncryptionUtil.getSecretKey(password, salt);
			decrypteText = EncryptionUtil.decrypt(key, EncryptionUtil.stringToIv(IV_SPEC), PASSWORD_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decrypteText;
	}

	public static void main(String[] args) {
		StringEncryptor encryptor = new JasyptConfig().stringEncryptor();
    String driver = encryptor.encrypt("oracle.jdbc.OracleDriver");
    String url = encryptor.encrypt("jdbc:oracle:thin:@10.23.143.98:1521:ONPTWOP");
    String user = encryptor.encrypt("ONPTWOP_DEV");
    String password = encryptor.encrypt("on2_d1e3v4_dhsxndls"); // on2_d1e3v4_!@ on2_d1e3v4_dhsxndls

    System.out.println("encryptor driver = " + driver);
    System.out.println("encryptor url = " + url);
    System.out.println("encryptor user = " + user);
    System.out.println("encryptor password = " + password);

    System.out.println("decryptor driver = " + encryptor.decrypt(driver));
    System.out.println("decryptor url = " + encryptor.decrypt(url));
    System.out.println("decryptor user = " + encryptor.decrypt(user));
    System.out.println("decryptor password = " + encryptor.decrypt(password));
	}
}
```

### @EnableEncryptableProperties
- `Jasypt Spring Boot Starter` 라이브러리에서 제공.
- Jaspyt와 함께 사용할 때 SrpingBoot에서 암호화된 속성을 자동으로 복호화 해주는 기능을 제공한다. 
- `application.properties` 나 `application.yml` 파일에 암호화된 값을 `ENC(암호화값)` 형식으로 저장해 두면, springboot 실행될 때 자동으로 감지하여 복호화 처리를 한다.  

### SimpleStringPBEConfig 옵션 설명
- Jasypt에서 암호화 관련 설정을 쉽게 할 수 있도록 도와주는 객체이다.

- `setPassword` : 암호화에 사용되는 키 값을 설정.
- `setAlgorithm` : 사용할 암호화 알고리즘을 지정.
- `setKeyObtentionIterations` : 키 스트레칭을 반복할 횟수를 지정.
  - 키 스트레칭이란 최초 데이터를 암호화하여 얻은 값 그대로 다시 암호화를 반복하는 것.
- `setPoolSize` : 암호화기를 풀로 관리할 때 사용되는 풀의 크기
- `setSaltGeneratorClassName` : salt를 생성하는 클래스 이름을 지정.
  - RandomSaltGenerator는 무작위로 salt를 생성하여 동일한 비밀번호라도 매번 다른 암호화 결과를 생성하도록 한다.
- `setIvGeneratorClassName` : IV를 생성하는 클래스 이름을 지정.
- `setStringOutputType` : 암호화된 문자열의 출력 형식을 지정.

### getPassword()
- JDBC 설정 정보 복호화에 필요한 키 값을 얻어온다.

### main()
- JDBC 설정 정보의 암호화 결과값 출력
- 암호화 결과 값 출력 후 main 메서드는 class에서 삭제.

<br>

<details>
<summary> 
<b><span style="font-size:130%;"> 블록 암호 체인 - CBC 모드</span></b>
</summary>

* 암호학에서 특정 비트 수의 집합을 한꺼번에, 그러니까 일정 크기의 블록 단위로 구성하여 처리하는 암호 기법을 블록 암호(block cipher)라고 한다.  
* 블록 암호는 특정한 길이의 블록 단위로 동작하기 때문에, 가변적인 데이터를 암호화하기 위해서는 먼저 데이터를 나누어야 한다. 그리고 이 블록을 어떻게 암호화할지 정해야 하는데, 이때 블록들의 암호화 방식을 운용 방식(modes of operation)이라고 한다.
* 운용 방식 중 CBC(Cipher Block Chaining) 운용 방식이 있다. CBC 모드를 사용한 암호화 과정에서는 원문의 각 블록은 암호화되기 전에 이전 암호문 블록과 XOR 연산을 수행한다.  
* 여기서 초기화 벡터(Initialization Vector)라는 용어가 등장한다. 최초의 평문 블록을 암호화할 때 직전의 암호문 블록이 없기 때문에 이를 대체할 블록이 필요한데, 이를 초기화 벡터라고 하며 영문자 앞 글자만 따서 IV로도 표기한다.  
* CBC 모 암호화 과정을 간단히 살펴보자.  
  * 초기화 벡터(IV):  
  * 암호화 과정의 시작점에 임의의 초기화 벡터(IV)가 사용된다. (최초 데이터 입장에서는 이전 암호화 블록이 없기 때문)  
  * 이 IV는 첫 번째 블록 암호화 시에 사용되며, 이후 블록 암호화 시에는 이전 암호 블록이 사용된다.  
* 첫 번째 블록 암호화:  
  * 첫 번째 원문 블록(P1)은 IV와 XOR 연산을 수행한다.  
  * XOR 연산의 결과는 암호화 알고리즘(예: AES)을 통해 암호화되어 첫 번째 암호 블록(C1)을 생성한다.  
* 두 번째 블록 이후의 암호화:  
  * 두 번째 원문 블록(P2)은 첫 번째 암호 블록(C1)과 XOR 연산을 수행한다.  
  * XOR 연산의 결과는 다시 암호화되어 두 번째 암호 블록(C2)을 생성한다.  
  * 이 과정은 모든 블록에 대해 반복된다.  

<br>

* CBC 방식 이미지  

  <img src="https://github.com/midoBanDev/midoBanDev.github.io/assets/164727588/6e2b8daa-84b0-4df2-8cdd-134aaaa0de75" width="50%" height="50%"/>
</details>


<br>

<details>
<summary> 
<b><span style="font-size:130%;"> XOR 연산이란</span></b>
</summary>

* XOR 연산은 두 입력 비트가 같으면 0을, 다르면 1을 출력하는 특성을 갖고 있습니다. 이 특성은 데이터 암호화 및 복호화 과정에서 유용하게 활용된다.
* 예를 들어, 어떤 데이터를 XOR 연산을 통해 암호화한 후, 동일한 키로 다시 XOR 연산을 하면 원래의 데이터를 복원할 수 있다.
* 예시 
  ```
  0 XOR 0 = 0
  0 XOR 1 = 1
  1 XOR 0 = 1
  1 XOR 1 = 0 
  ```
</details>

<br>

## 3. application.yml 설정 변경
- stringEncryptor 클래스의 main 메서드에서 출력한 암호화 결과값을 `application.yml` 의 JDBC 설정정보에 평문 대신 넣어준다.
- `ENC()` 의 괄호 안에 암호화 값을 넣어주면 된다.

```xml
spring:
  config:
    activate:
      on-profile: local

  datasource:
    driver-class-name: ENC(3k5FDHmbMsBNiPRrUgl+y7aw+7B2iE50iF+7z5xiqX8vBLZDYrSJwtTqGp99scWUTwpAFWW11lqaGLSafRLQdw==)
    url: ENC(VUJK6HethGA6virjwe7Pg4mUBGhKBTl8XThsvh2xG4Hxjztb+HAcHF7wv4rYi0gaQiqssmKT9t7TwxM2B2fHpjSCf0L8HQGbEW4y0vcoaa4=)
    username: ENC(Y6TohAkjx2A6nuuKWx2UGsrbkSAF61gsxfzX4oPMs6mGs04JWu4EtksZx/9Qsk7K)
    password: ENC(ic9tw5YzBgE9yt/HIuXPv7gTVCHNyB559Hr+ZJ5jniLB3WP+hnhUwJK4t6KKUDuI)
    tomcat:
      max-active: 30
      max-idle: 10
```

<br><br>

# Password 암호화
- 기본적으로 양방향 암호화를 하기 위해서는 `key`가 필요하다.
- 암호화 시점에 `key`를 생성하고 복호화 시 해당 `key`를 사용하게 된다.

<br>

- 자 그럼 `Jasypt`를 통해 JDBC 설정 정보를 암호화할 때 `password`를 사용했다.
- 그런데 해당 `password`를 평문 그대로 소스 상에 입력해 놓거나, 특정 파일에 보관하는 할 수도 있지만 이 또한 리스크가 될 수 있다. 
- 따라서 `password` 관리를 위해 해당 `password`에 대한 암호화를 진행해 보자.

<br>

- 원하는 문자열을 암/복호화 하기 위한 클래스
- 위에서 말했다시피 무언가 암호화를 하기 위해서는 `key`가 필요하다.
- 그럼 해당 `key`를 또 관리해 줘야 하는 이슈가 발생한다. 
- 따라서 해당 클래스의 핵심은 암호화 뿐만 아니라 암호화 시 필요한 `key`를 어떻게 처리하는지가 핵심이다.

<br>

- 암호화 `key` 생성 알고리즘은 `AES + PBKDF2`를 사용한다.
- 데이터 암호화 방식이 `AES`일 경우 키 생성 시 AES용 키를 생성해 줘야 한다.
- PBKDF2 역할 
  - 키 스트레칭(Key Stretching) : 단순한 비밀번호나 패스프레이즈를 사용하여 더 강력한 암호화 키를 생성한다. 이를 통해 사전 공격이나 브루트 포스 공격에 대한 저항성을 높인다.
  - 솔트(Salt) : 비밀번호와 함께 사용되는 임의의 데이터를 추가하여 동일한 비밀번호라도 각기 다른 암호화 키를 생성할 수 있도록 한다. 이는 동일한 비밀번호에 대한 사전 계산된 해시 테이블(레인보우 테이블) 공격을 방지하는 데 도움이 된다.
  - 반복 횟수(Iteration Count) : 키를 생성하는 데(키 스트레칭) 반복적인 연산을 수행함으로써 키 생성 시간을 증가시켜 공격자가 키를 추정하는 데 필요한 연산량을 크게 증가시킨다.
  - PBKDF2는 이러한 방법들을 통해 안전하고 강력한 암호화 키를 생성할 수 있다.

<br>

- 데이터 암호화 알고리즘는 `AES + CBC + PKCS5Padding`를 사용한다.
- 암호화 키인 `SecretKey` 생성 시 사용된 `key` + `salt` 값과 암호화 시 사용된 `IV` 값은 암호화 시점에 생성된 값 그대로 보관해야 한다. 복호화 시 필요.

<br>

## 1. SecretCrypt 클래스 추가
- `SecretKey` 생성 시 필수 값인 `key`와 `salt`값을 보관하는 클래스
- 해당 클래스는 난독화 후 프로젝트에 포함시키면 안된다.
- 난독화를 진행하여 `EncryptionUtil` 클래스를 decompile 시 노출되지 않도록 한다.

```java
public class SecretCrypt {
	
	public static String getSalt() {
		return "spvcalekjfsldkfj";
	}
	
	public static String getPwd() {
		return "cri%zen_#solu!ti$on_gcvaefad";
	}
}

//class file 난독화
public static void main(String[] args) throws IOException {
		byte[] bytes = loadClassBytes("C:/Users/crizen/SecretCrypt.class");
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
		    sb.append(String.format("(byte)0x%02x, ", b));
		}
		System.out.println(sb.toString());
	}
```

<br>

## 2. EncryptionUtil 클래스 추가

```java
package com.crizen.util;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.xerces.impl.dv.util.Base64;

public class EncryptionUtil {

	
	/**
	 * 
	 * SecretKey를 생성한다.
	 * @param key
	 * @param salt
	 * @return
	 * @throws Exception
	 */
	public static SecretKey getSecretKey(String key, String salt) throws Exception {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(key.toCharArray(), Base64.decode(salt), 10000, 128);
    SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    return secret;
  }
	
  /**
   * 초기화 벡터 반환
   * CBC 운영모드에서는 초기화 백터 값은 16바이트(=128비트)여야 한다.
   * AES 블록 암호화 크기가 16바이트(128비트)이기 때문이다.
   */
  public static IvParameterSpec getIv() {
    byte[] iv = new byte[16];
    new SecureRandom().nextBytes(iv);
    return new IvParameterSpec(iv);
  }
    
  /**
   * 암호화 
   * @param key
   * @param iv
   * @param plainText
   * @return
   * @throws Exception
   */
	public static String encrypt(SecretKey key, IvParameterSpec iv, String plainText) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
		return new String(Base64.encode(encrypted));
	}

	/**
	 * 복호화
	 * @param key
	 * @param iv
	 * @param cipherText
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(SecretKey key, IvParameterSpec iv, String cipherText) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] decrypted = cipher.doFinal(Base64.decode(cipherText));
		return new String(decrypted, StandardCharsets.UTF_8);
	}
	
	/**
	 * String을 IV 객체로 변환
	 * @param ivString
	 * @return
	 */
	public static IvParameterSpec stringToIv(String ivString) {
    byte[] iv = Base64.decode(ivString);
    return new IvParameterSpec(iv);
  }
	
	/**
	* SecretCrypt 클래스(HASH)
	* @return
	*/
	private static Class<?> getCzCrypt() {
		Class<?> clazz = new ClassLoader(EncryptionUtil.class.getClassLoader()) {
      public Class<?> defineClass() {
        byte[] bytes = {(byte)0xca, (byte)0xfe, (byte)0xba, (byte)0xbe, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x37, (byte)0x00, (byte)0x14, (byte)0x0a, (byte)0x00, (byte)0x05, (byte)0x00, (byte)0x0f, (byte)0x08, (byte)0x00, (byte)0x10, (byte)0x08, (byte)0x00, (byte)0x11, (byte)0x07 ..생략};
          return super.defineClass("SecretCrypt", bytes, 0, bytes.length);
      }
		}.defineClass();
		
    return clazz;
	}
	/**
	* SecretCrypt 클래스에서 키값을 추출
	* @param keyType
	* @return
	*/
	public static String getKey(String keyType) {
		String returnKey = null;
		Method method = null;
		Class<?> clazz = getCzCrypt();
		try {
			method = clazz.getDeclaredMethod(keyType);
			method.setAccessible(true);
			returnKey = (String) method.invoke(null);
		} catch ( Exception e) {
			e.printStackTrace();
		}
		return returnKey;
	}
}
```

<br>

## 3. IV(초기화백터)와 원하는 문자열의 암호화 결과값 추출
- JDBC 설정 정보 암호화 시 사용한 `password`를 plainText에 넣어 주자.

```java
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String plainText = "gasdfeaeadvafdafeasef";
		String password = EncryptionUtil.getKey("getPwd");
		String salt = EncryptionUtil.getKey("getSalt");
    	
		SecretKey key = EncryptionUtil.getSecretKey(password, salt);
		IvParameterSpec iv = EncryptionUtil.getIv();
		
		String encypteText = EncryptionUtil.encrypt(key, iv, plainText);
		String decrypteText = EncryptionUtil.decrypt(key, iv, encypteText);
//		String decrypteText = EncryptionUtil.decrypt(key, stringToIv("qC9AEkvsOm/l3Z9+3MoOSQ=="), "XIVOCgLEma3bMzPqK1b3DA==");
		
		System.out.println("iv: " + Base64.encode(iv.getIV()));
		System.out.println("encypteText: " + encypteText);
		System.out.println("decrypteText: " + decrypteText);
	}
```
- getCzCrypt()
  - 난독화 된 SecretCrypt 클래스를 리플렉션으로 로드한다.

- getKey()
  - SecretCrypt 클래스의 `key`와 `salt`값을 추출하는 메서드를 호출한다.

<br>

## 4. application.yml에 password 와 IV 암호화 결과값 설정 정보 추가
```xml
spring:
  config:
    activate:
      on-profile: local

  datasource:
    driver-class-name: ENC(3k5FDHmbMsBNiPRrUgl+y7aw+7B2iE50iF+7z5xiqX8vBLZDYrSJwtTqGp99scWUTwpAFWW11lqaGLSafRLQdw==)
    url: ENC(VUJK6HethGA6virjwe7Pg4mUBGhKBTl8XThsvh2xG4Hxjztb+HAcHF7wv4rYi0gaQiqssmKT9t7TwxM2B2fHpjSCf0L8HQGbEW4y0vcoaa4=)
    username: ENC(Y6TohAkjx2A6nuuKWx2UGsrbkSAF61gsxfzX4oPMs6mGs04JWu4EtksZx/9Qsk7K)
    password: ENC(ic9tw5YzBgE9yt/HIuXPv7gTVCHNyB559Hr+ZJ5jniLB3WP+hnhUwJK4t6KKUDuI)
    tomcat:
      max-active: 30
      max-idle: 10

# password, IV 암호화 결과 값 
jasypt:
  encryptor:
    password: EWS0VtHe7U0T+rMujv7w95LLNezh2ZK754r6aZ3dYEQ=
    iv-spec: aNWEonurjpgG1nICeByKYA==
```