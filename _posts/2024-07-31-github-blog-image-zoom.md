---
layout: single
title:  "Github 블로그 이미지 확대 기능(feat. Medium Zoom)"
categories:
  - Github-Blog
tags:
  - [Github-Blog, Image]

toc: true
toc_sticky: true
---


## Medium Zoom JS 링크 추가 - `_includes/head.html`
- _includes/head.html 파일에 아래 내용 추가
- 위치는 상단 아무곳이나 상관없다. 나는 meta 태그 위에 넣었다.

```html
<script src="https://cdn.jsdelivr.net/npm/medium-zoom/dist/medium-zoom.min.js"></script>
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>



## 줌 스크립트 코드 추가 - `_layouts/default.html`
- 아래 코드를 `_layouts/default.html` 파일의 끝 부분에 추가한다.

```javascript
<script>
  mediumZoom('img:not(.no-zoom)', {
    margin: 24,
    background: 'rgba(0,0,0,0.8)'
  });
</script>
```

### 여기까지 설정했으면 끝이다. 


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 확대하고 싶지 않은 이미지 설정
- 확대하고 싶지 않은 이미지에 아래 no-zoom 클래스를 추가하면 된다.

```html
<img src="image.jpg" class="no-zoom" />
```



<!--<img src="" width="80%" height="80%"/>-->



