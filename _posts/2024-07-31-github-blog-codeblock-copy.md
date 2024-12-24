---
layout: single
title:  "Github 블로그 이미지 copy 기능 추가"
categories:
  - Github-Blog
tags:
  - [Github-Blog, Image]

toc: true
toc_sticky: true
---


## button css 작성
- 프로젝트의 `assets/css` 폴더에 `copy-button.css` 파일을 생성하고 다음 내용을 넣는다

```css
  .code-header {
    display: flex;
    justify-content: flex-end;
    padding: 4px;
    background: #405c53;
    border: 1px solid #e9ecef;
    border-bottom: none;
    border-radius: 8px 8px 0 0; /* 상단 모서리 */
  }
  
  .copy-button {
    display: inline-block;
    padding: 2px 8px;
    font-size: 13px;
    color: white;
    cursor: pointer;
    background-color: #555; 
    border: 1px solid #444;
    border-radius: 4px;
  }

  .copy-button:hover {
    background-color: #666;
  }

  .copy-button:active {
    background-color: #444;
  }
  
  /* 코드 블록 자체의 스타일 개선 */
  pre {
    margin-top: 0 !important; /* header와의 간격 제거 */
    background: #282c34 !important; /* VS Code 다크 테마와 비슷한 배경색 */
    border-radius: 0 0 16px 16px !important; /* 하단 모서리 */
    padding: 1em !important;
  }
  
  pre code {
    color: #abb2bf !important; /* 밝은 회색으로 가독성 개선 */
    font-size: 14px !important;
    line-height: 1.6 !important;
    font-family: 'Menlo', 'Monaco', 'Courier New', monospace !important;
  }
```

<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>



## copy-button.css 링크 추가 - `_includes/head.html`
- 아래 링크를  `_includes/head.html` 에 넣어준다.

```html
<!-- copy 버튼 css -->
<link rel="stylesheet" href="/assets/css/copy-button.css">
```


<div style="padding-top:100px;"></div>
<span style="margin-left:35%;">⊙</span>
<span style="margin-left:10%">⊙</span>
<span style="margin-left:10%">⊙</span>
<div style="padding-top:100px;"></div>


## 복사 기능 코드 추가 - `_includes/scripts.html`
- 아래 코드를 `_includes/scripts.html` 파일 최하단에 넣어준다.

```html
<script>
document.addEventListener('DOMContentLoaded', () => {
  // 모든 코드 블록을 찾아서 처리
  document.querySelectorAll('pre code').forEach((codeBlock) => {
    // 코드 블록의 부모인 pre 요소를 감쌀 div 생성
    const wrapper = document.createElement('div');
    
    // 복사 버튼을 포함할 헤더 div 생성
    const header = document.createElement('div');
    header.className = 'code-header';
    
    // 복사 버튼 생성
    const copyButton = document.createElement('button');
    copyButton.className = 'copy-button';
    copyButton.textContent = 'Copy';
    
    // 복사 버튼 클릭 이벤트 추가
    copyButton.addEventListener('click', () => {
      navigator.clipboard.writeText(codeBlock.textContent)
        .then(() => {
          copyButton.textContent = 'Copied!';
          setTimeout(() => {
            copyButton.textContent = 'Copy';
          }, 2000);
        })
        .catch((err) => {
          console.error('Failed to copy:', err);
          copyButton.textContent = 'Error';
        });
    });
    
    // 헤더에 버튼 추가
    header.appendChild(copyButton);
    
    // 원래 pre 요소를 wrapper로 감싸기
    const pre = codeBlock.parentNode;
    pre.parentNode.insertBefore(wrapper, pre);
    wrapper.appendChild(header);
    wrapper.appendChild(pre);
  });
});
</script>

```

### 끝이다.


<!--<img src="" width="80%" height="80%"/>-->



