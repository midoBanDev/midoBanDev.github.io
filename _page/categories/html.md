---
title: "HTML"
layout: archive
permalink: /categories/html/
author_profile: true
sidebar_main: false
sidebar:
  nav: "docs"
---

{% assign posts = site.categories.html %}
HTML
{% for post in posts %} {% include archive-single.html type=page.entries_layout %} {% endfor %}
