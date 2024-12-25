---
layout: single
title:  "XSS 공격 방지 설정 - Lucy-XSS-Filter"
categories:
  - Web
tags:
  - [Web, Xss]

toc: true
toc_sticky: true
---


# Lucy-XSS-Filter 적용

## build.gradle

```xml
// Lucy-XSS-Filter(Naver)
implementation 'com.navercorp.lucy:lucy-xss-servlet:2.0.1'
```

<br>

## XssFilterConfig 추가

```java
package com.crizen.hp.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.crizen.hp.utils.HtmlCharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class XssFilterConfig implements WebMvcConfigurer {
	
	private final ObjectMapper objectMapper;
	
	@Bean
	FilterRegistrationBean<XssEscapeServletFilter> xssFilter() {
		FilterRegistrationBean<XssEscapeServletFilter> filter = new FilterRegistrationBean<>();
		filter.setFilter(new XssEscapeServletFilter());
		filter.setOrder(Ordered.LOWEST_PRECEDENCE);
		filter.addUrlPatterns("/*");  // 모든 URL에 대해 처리하도록 설정
		return filter;
	}
	
  /**
   * JSON 요청은 별도 처리
   */ 
	@Bean
    MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(copy);
    }
}
```

<br>

## HtmlCharacterEscapes 클래스 추가

```java
package com.crizen.hp.utils;

import org.apache.commons.lang3.StringEscapeUtils;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;

@SuppressWarnings({ "serial", "deprecation" })
public class HtmlCharacterEscapes extends CharacterEscapes{

	private final int[] asciiEscapes;
	
	public HtmlCharacterEscapes() {
		asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['&'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
	}

	@Override
	public int[] getEscapeCodesForAscii() {
		return asciiEscapes;
	}

	@Override
	public SerializableString getEscapeSequence(int ch) {
		return new SerializedString(StringEscapeUtils.escapeHtml4(Character.toString((char)ch)));
	}
}
```

<br>

## lucy-xss-servlet-filter-rule.xml 추가


```xml
<?xml version="1.0" encoding="UTF-8"?>
 
<config xmlns="http://www.navercorp.com/lucy-xss-servlet">
    <defenders>
        <!-- XssPreventer 등록 -->
        <defender>
            <name>xssPreventerDefender</name>
            <class>com.navercorp.lucy.security.xss.servletfilter.defender.XssPreventerDefender</class>
        </defender>

        <!-- XssSaxFilter 등록 -->
        <defender>
            <name>xssSaxFilterDefender</name>
            <class>com.navercorp.lucy.security.xss.servletfilter.defender.XssSaxFilterDefender</class>
            <init-param>
                <param-value>lucy-xss-sax.xml</param-value>   <!-- lucy-xss-filter의 sax용 설정파일 -->
                <param-value>false</param-value>        <!-- 필터링된 코멘트를 남길지 여부, 성능 효율상 false 추천 -->
            </init-param>
        </defender>

        <!-- XssFilter 등록 -->
        <defender>
            <name>xssFilterDefender</name>
            <class>com.navercorp.lucy.security.xss.servletfilter.defender.XssFilterDefender</class>
            <init-param>
                <param-value>lucy-xss.xml</param-value>    <!-- lucy-xss-filter의 dom용 설정파일 -->
                <param-value>false</param-value>         <!-- 필터링된 코멘트를 남길지 여부, 성능 효율상 false 추천 -->
            </init-param>
        </defender>
    </defenders>

    <!-- default defender 선언, 필터링 시 지정한 defender가 없으면 여기 정의된 default defender를 사용해 필터링 한다. -->
    <default>
        <defender>xssPreventerDefender</defender>
    </default>

    <!-- global 필터링 룰 선언 -->
    <global>
        <!-- 모든 url에서 들어오는 globalParameter 파라메터는 필터링 되지 않으며
                또한 globalPrefixParameter1로 시작하는 파라메터도 필터링 되지 않는다.
                globalPrefixParameter2는 필터링 되며 globalPrefixParameter3은 필터링 되지 않지만
                더 정확한 표현이 가능하므로 globalPrefixParameter2, globalPrefixParameter3과 같은 불분명한 표현은 사용하지 않는 것이 좋다. -->
        <params>
            <param name="globalParameter" useDefender="false" />
            <param name="globalPrefixParameter1" usePrefix="true" useDefender="false" />
            <param name="globalPrefixParameter2" usePrefix="true" />
            <param name="globalPrefixParameter3" usePrefix="false" useDefender="false" />
        </params>
    </global>

    <!-- url 별 필터링 룰 선언 -->
    <url-rule-set>

        <!-- url disable이 true이면 지정한 url 내의 모든 파라메터는 필터링 되지 않는다. -->
        <url-rule>
            <url disable="true">/disableUrl1.do</url>
        </url-rule>

        <!-- url disable이 false인 설정은 기본이기 때문에 불필요하다. 아래와 같은 불필요한 설정은 하지 않는다.-->
        <url-rule>
            <url disable="false">/disableUrl2.do</url>
        </url-rule>

        <!-- url disable이 true이면 지정한 url 내의 모든 파라메터가 필터링 되지 않기 때문에 <params> 로 선언한 설정은 적용되지 않는다. 
               아래와 같은 불필요한 설정은 하지 않는다. -->
        <url-rule>
            <url disable="true">/disableUrl3.do</url>
            <params>
                <param name="query" useDefender="false" />
                <param name="prefix1" usePrefix="true" />
                <param name="prefix2" usePrefix="false" useDefender="false" />
                <param name="prefix3" usePrefix="true" useDefender="true" />
                <param name="prefix4" usePrefix="true" useDefender="false" />
                <param name="prefix5" usePrefix="false" useDefender="true" />
            </params>
        </url-rule>

        <!-- url disable이 false인 설정은 기본이기 때문에 불필요하다. <params> 선언한 설정은 적용이 된다.-->
        <url-rule>
            <url disable="false">/disableUrl4.do</url>
            <params>
                <!-- disableUrl4.do 의 query 파라메터와 prefix4로 시작하는 파라메터들은 필터링 되지 않는다. 
                        usePrefix가 false, useDefender가 true인 설정은 기본이기 때문에 불필요하다. -->
                <param name="query" useDefender="false" />   
                <param name="prefix1" usePrefix="true" />
                <param name="prefix2" usePrefix="false" useDefender="false" />
                <param name="prefix3" usePrefix="true" useDefender="true" />
                <param name="prefix4" usePrefix="true" useDefender="false" />
                <param name="prefix5" usePrefix="false" useDefender="true" />
            </params>
        </url-rule>

        <!-- url1 내의 url1Parameter는 필터링 되지 않으며 또한 url1PrefixParameter로 시작하는 파라메터도 필터링 되지 않는다. -->
        <url-rule>
            <url>/url1.do</url>
            <params>
                <param name="url1Parameter" useDefender="false" />
                <param name="url1PrefixParameter" usePrefix="true" useDefender="false" />
            </params>
        </url-rule>

        <!-- url2 내의 url2Parameter1만 필터링 되지 않으며 url2Parameter2는 xssSaxFilterDefender를 사용해 필터링 한다.  -->
        <url-rule>
            <url>/url2.do</url>
            <params>
                <param name="url2Parameter1" useDefender="false" />
                <param name="url2Parameter2">
                    <defender>xssSaxFilterDefender</defender>
                </param>
            </params>
        </url-rule>
    </url-rule-set>
</config>

```


<br>

## 추가 정보

- 다른 Defender 사용하고 싶은 경우.
  - `xssSaxFilterDefender` `xssFilterDefender`
  - 자세한 내용은 아래 블로그 참고
  - https://velog.io/@dayoung_sarah/lucy-xss-servlet-filter%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EC%97%AC-XSS-%EA%B3%B5%EA%B2%A9-%EB%B0%A9%EC%96%B4%ED%95%98%EA%B8%B0-2


<br>

- OWASP XSS 공격 패턴 모음
  - https://cheatsheetseries.owasp.org/cheatsheets/XSS_Filter_Evasion_Cheat_Sheet.html


<br>


- lucy-xss-servlet-filter 공식 Github
  - https://github.com/naver/lucy-xss-servlet-filter?tab=readme-ov-file
  - 상세정보 : https://github.com/naver/lucy-xss-servlet-filter/blob/master/doc/manual.md

<br>

- lucy-xss-filter 공식 Github
  - https://github.com/naver/lucy-xss-filter