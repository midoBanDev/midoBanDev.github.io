---
layout: single
title:  "Github 블로그 Font 스타일 변경(feat. Kelly)"
categories:
  - Github-Blog
tags:
  - [Github-Blog, Font]

toc: true
toc_sticky: true
---


## Font 추가 - `main.scss`

- 원하는 폰트 추가
- main.scss 파일에 아래 내용 추가해주면 된다.

```css
/*[modify] 배민 주아체*/
@font-face {
    font-family: 'BMJUA';
    src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_one@1.0/BMJUA.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}

/*[modify] 고운바탕*/
@font-face {
    font-family: 'GowunBatang-Regular';
    src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_2108@1.1/GowunBatang-Regular.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>



## 블로그 전체 Font 적용 - `_variables.scss`

- `$customFont` 변수를 선언해 주고 main.scss에 추가한 `font-family` 값 그대로 적용해주자.
- `$serif`, `$sans-serif`, `$monospace` 변수에 `$customFont`를 셋팅해주면 된다.

```css
/* system typefaces 
   [modify] font 설정
*/
$customFont: "GowunBatang-Regular";
$serif: $customFont, Georgia, Times, serif !default;
$sans-serif: $customFont, -apple-system, BlinkMacSystemFont, "Roboto", "Segoe UI",
  "Helvetica Neue", "Lucida Grande", Arial, sans-serif !default;
$monospace: $customFont, Monaco, Consolas, "Lucida Console", monospace !default;
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## Coding box 스타일 설정 - `_syntax.scss`

- `_syntax.scss` 파일의 코드가 입력되는 box의 스타일을 설정할 수 있다.
- `.highlight pre`는 코드 입력 box를 설정하는 부분이다.
- `.highlight pre code`를 추가하여 코드 입력 내부 스타일을 변경해 주었다.

```css
/*[modify] coding box 영역*/
.highlight pre {
  width: 100%;
}

/*[modify] coding 내부 영역*/
.highlight pre code {
  font-size: 100%;
  font-family: Consolas;
}
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>

## 코드블록, 카테고리, 태그 스타일 설정 - `_page.scss`

- `:not(pre) > code`는 `코드블록` ← 이 부분의 스타일을 수정하는 부분이다.
```css
/* [modify] code block css 설정 */
  :not(pre) > code {
    font-size: 95%;
    padding-left: 0.1rem;
    padding-right: 0.1rem;
    padding-bottom: 0.1rem;
    background: #b5621a85; // 녹색 - #32929285, 갈색 - #b5621a85
    border-radius: $border-radius;

    &::before,
    &::after {
      letter-spacing: -0.2em;
      content: "\00a0"; /* non-breaking space*/
    }
  }
```

<br>

- `.page__taxonomy-item-category`는 카테고리 스타일을 수정하는 부분이다.

```css
/*[modify] 카테고리 스타일*/
.page__taxonomy-item-category {
  font-weight: bold;
  /* border: aliceblue; */
  font-size: 115%;
  display: inline-block;
  margin-right: 5px;
  margin-bottom: 8px;
  padding: 3px 4px;
  text-decoration: none;
  border: 2px solid #a7282863;
  border-radius: 8px;
  background-color: #bb302094;
}
```

<br>

- `.page__taxonomy-item-tag`는 태그 스타일을 수정하는 부분이다.

```css
/*[modify] 태그 스타일*/
.page__taxonomy-item-tag {
  font-weight: bold;
  /* border: aliceblue; */
  font-size: 115%;
  display: inline-block;
  margin-right: 5px;
  margin-bottom: 8px;
  padding: 3px 4px;
  text-decoration: none;
  border: 2px solid #3d4046;
  border-radius: 8px;
  background-color: #20babb94;
}
```

<!--<img src="" width="80%" height="80%"/>-->



