---
title: "AWS"
layout: archive
permalink: categories/aws
author_profile: true
sidebar_main: true
---


{% assign posts = site.categories.Aws %}<!-- post에 등록된 글 상단에 선언한 categorise  -->
{% for post in posts %} {% include archive-single2.html type=page.entries_layout %} {% endfor %}
