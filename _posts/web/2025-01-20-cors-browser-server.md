---
layout: single
title:  "[WEB] 서버의 CORS 검증"
categories:
  - Web
tags:
  - [Web, CORS]

toc: true
toc_sticky: true
---

## CORS의 두 가지 검증 계층

CORS는 브라우저와 서버 양쪽에서 각각 독립적으로 동작하는 검증 메커니즘을 가진다.

### 1.1. 브라우저의 CORS 정책
- 다른 출처 요청 시에만 동작
- Preflight 요청(OPTIONS)을 통한 사전 확인
- 서버의 CORS 응답 헤더를 검증
- 부적절한 응답 시 JavaScript에서 요청 차단

### 1.2. 서버의 CORS 정책 (Spring 기준)
- 모든 요청에 대해 origin 검증 가능
- WebMvcConfigurer를 통한 설정
- 허용되지 않은 origin 요청을 `403`으로 차단
- 브라우저의 CORS 정책과 무관하게 동작

<div style="padding-top:60px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:60px;"></div>

## 2. 실제 동작 사례

### 2.1. 다른 출처 요청의 경우
```
브라우저 (http://localhost:3000) → API 서버 (http://localhost:8080)

1. 브라우저 CORS 정책 적용
- Preflight 요청 발생
- CORS 헤더 검증

2. 서버 CORS 정책 적용
- WebMvcConfigurer 설정에 따른 origin 검증
- allowedOrigins 목록 확인
```

### 2.2. 같은 출처 요청의 경우
```
브라우저 (https://apiloan.p-e.kr) → API 서버 (https://apiloan.p-e.kr)

1. 브라우저 CORS 정책 미적용
- 같은 출처이므로 Preflight 요청 없음
- CORS 헤더 검증 안 함

2. 서버 CORS 정책 적용
- WebMvcConfigurer 설정이 있다면 여전히 origin 검증
- allowedOrigins에 없으면 403 반환
```

<div style="padding-top:60px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:60px;"></div>

## 3. Spring의 CORS 구현
서버에서는 브라우저의 요청에 대한 응답을 할 때 CORS 관련 헤더에 허가 정보를 담아 보낸다. 
단순히 허가 정보를 담아 응답을 할 수도 있다.

```java
// 헤더만 추가하고 실제 검증은 안함
response.setHeader("Access-Control-Allow-Origin", "*");
```

추가로 서버에서도 SpringSecurity 또는 WebMvcConfigurer 설정을 통해 CORS 검증을 통해 허용된 경우 응답하는 방식도 있다. 

### WebMvcConfigurer 설정
서버 측 보안 정책으로 동작하며, 브라우저의 CORS 상태와 무관하게 origin 등을 검증한다. 
또한 모든 HTTP 요청에 대해 검증 수행

**WebMvcConfigurer**는 MVC 요청 처리 단계에서 동작한다. 

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
              .allowedOrigins("https://allowed-origin.com") // 프론트엔드 도메인 허용
              .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
              .allowedHeaders("*"); // 허용할 헤더         
    }
}
```
`addMapping`  
- 요청 경로가 addMapping에 정의된 경로와 일치하는 지 검증한다.

`allowedOrigins`  
- 요청 Origin 값이 allowedOrigins에 정의된 주소와 동일한 지 검증한다. 
- allowedOrigins에 정의된 값과 일치하지 않는 경우 403 Forbidden을 반환한다.

`allowedMethods`  
- 요청 http 메서드가 allowedMethods에 정의된 값에 포함되는 지 검증한다.

`allowedHeaders`  
- 요청 헤더 중 allowedHeaders에 정의된 값에 포함되는 지 검증한다. allowedHeaders에는 허용 할 헤더를 정의한다.

위 검증 과정을 통과하면 응답에 CORS 헤더(Access-Control-Allow-Origin, Access-Control-Allow-Methods 등)를 추가한다.

**주요 특성**  
- 서버 측 보안 정책으로 동작
- 브라우저의 CORS 상태와 무관하게 origin 검증
- 모든 HTTP 요청에 대해 검증 수행


### SpringSecurity 설정

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.addAllowedOrigin("http://allow-origin.com"); // 허용할 출처
      configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
      configuration.addAllowedHeader("*"); // 모든 헤더 허용
      configuration.setAllowCredentials(true); // 쿠키/인증정보 허용

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .csrf(csrf -> csrf.disable())
      .sessionManagement(session -> 
          session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      
      .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), 
          UsernamePasswordAuthenticationFilter.class)
      
      .authorizeHttpRequests(auth -> auth
          .requestMatchers(
              "/api/v1/auth/**",
              "/swagger-ui/**",
              "/v3/api-docs/**",
              "/docs/**"
          ).permitAll()
          .anyRequest().authenticated())
      
      .exceptionHandling(exceptionHandling -> 
          exceptionHandling.authenticationEntryPoint(
              new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
      .build();
  }
}
```

**1. Security Filter Chain에서 CORS 필터 적용**  
- Spring Security는 요청을 처리하기 전, **Security Filter Chain**에서 `CorsFilter`를 호출한다.
- `CorsFilter`는 등록된 `CorsConfigurationSource`에서 설정된 정책에 따라 요청을 검증한다.

**2. CORS 정책 검증**  
- **출처 검증**: 요청의 `Origin` 헤더가 `addAllowedOrigin`에 등록된 값(`http://allow-origin.com`)과 일치하는지 확인한다.
  - 일치하지 않으면 요청이 차단된다.
- **HTTP 메서드 검증**: 요청의 HTTP 메서드가 `addAllowedMethod("*")`에 포함된 메서드인지 확인한다.
- **헤더 검증**: 요청의 `Access-Control-Request-Headers`에 포함된 헤더가 `addAllowedHeader("*")`에 허용된 헤더인지 확인한다.
- **쿠키/인증정보 검증**: 요청에 `withCredentials: true`가 포함된 경우, 서버가 `setAllowCredentials(true)`로 설정되어 있는지 확인한다.
  - 설정되어 있지 않으면 요청이 차단됩니다.

**3. 응답 헤더 추가**  
- 요청이 검증을 통과하면, 서버는 응답에 다음과 같은 CORS 헤더를 추가한다.
  - `Access-Control-Allow-Origin`: 허용된 출처.
  - `Access-Control-Allow-Methods`: 허용된 HTTP 메서드.
  - `Access-Control-Allow-Headers`: 허용된 요청 헤더.
  - `Access-Control-Allow-Credentials`: 인증 정보를 허용했는지 여부.

**4. 검증 실패 시**
- 요청이 검증을 통과하지 못하면 Spring Security는 요청을 차단하며, **403 Forbidden** 응답을 반환한다.


정리하면 **CORS 검증**은 Spring Security의 `CorsFilter`에서 수행되며, 설정된 `CorsConfigurationSource`에 따라 요청을 검증한다. 
CORS 검증에 실패하면 요청이 Security Filter Chain의 나머지 부분에 도달하지 못하고 차단된다. 



<div style="padding-top:60px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:60px;"></div>


### **두 방식의 매커니즘 차이**

| **특징**                     | **WebMvcConfigurer만 사용**                                | **Spring Security와 함께 사용**                           |
|------------------------------|---------------------------------------------------------|-------------------------------------------------------|
| **CORS 처리 위치**            | Spring MVC 요청 처리 단계에서 CORS 검증 수행               | Security Filter Chain 단계에서 CORS 검증 수행           |
| **검증 적용 대상**            | 컨트롤러(`/api/**` 등)로 매핑된 요청만 검증                  | 모든 HTTP 요청 검증 가능 (컨트롤러 외 요청 포함)          |
| **Preflight 요청 처리**       | 자동 처리 (Spring MVC에서 OPTIONS 요청 처리)                | 자동 처리, Security Filter Chain에서 처리 가능          |
| **우선순위**                  | Spring Security 에 설정 없으면 동작                        | Spring Security 설정이 있으면 WebMvcConfigurer 무시      |
| **유연성**                    | 단순한 CORS 정책 설정에 적합                                | 더 세밀한 보안 및 CORS 정책 설정 가능                   |

<div style="padding-top:60px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:60px;"></div>

### SpringSecurity + WebMvcConfigurer 설정 
두 설정을 혼합하여 사용할 수 있다. 

- WebMvcConfigurer 설정
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://allowed-origin.com")
                .allowedMethods("*");
    }
}
```

- SpringSecurity 설정에서 cors 설정을 WebMvcConfigurer에 위임한다.
```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .cors(cors -> {}) // WebMvcConfigurer로 CORS 위임
      .csrf(csrf -> csrf.disable())
      .sessionManagement(session -> 
          session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      
      .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), 
          UsernamePasswordAuthenticationFilter.class)
      
      .authorizeHttpRequests(auth -> auth
          .requestMatchers(
              "/api/v1/auth/**",
              "/swagger-ui/**",
              "/v3/api-docs/**",
              "/docs/**"
          ).permitAll()
          .anyRequest().authenticated())
      
      .exceptionHandling(exceptionHandling -> 
          exceptionHandling.authenticationEntryPoint(
              new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
      .build();
  }
}
```