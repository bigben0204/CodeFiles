# 1. Jinja2

<http://jinja.pocoo.org/docs/2.10/intro/#basic-api-usage>

<http://docs.jinkan.org/docs/jinja2/templates.html>

<https://www.cnblogs.com/dachenzi/p/8242713.html>

## 1.1. 使用样例

```python
# main.py
# -*- coding: utf-8 -*-
from jinja2 import Template

if __name__ == '__main__':
    template = Template('Hello {{ name }}!')
    print(template.render(name='John Doe'))
输出：
Hello John Doe!
```

或者使用txt:

```python
# template.txt
Hello {{ name }}!

# -*- coding: utf-8 -*-
from jinja2 import Template

if __name__ == '__main__':
    with open('template/template.txt') as f:
        content = f.read()

    template = Template(content)
    print(template.render(name='John Doe'))
```

for:

```python
# -*- coding: utf-8 -*-
from jinja2 import Template

if __name__ == '__main__':
    with open('template/template.txt') as f:
        content = f.read()

    names = ['ding', 'zhao', 'wang']
    template = Template(content)
    print(template.render(names=names))

# template.txt
names is : {{ names }}

names is : {{ names | join(' ') }}

{% for name in names %}
this is: {{ name }}
{% endfor %}

输出：
names is : ['ding', 'zhao', 'wang']

names is : ding zhao wang


this is: ding

this is: zhao

this is: wang

```

## 1.2. 空白控制

```python
{% for name in names -%}
this is: {{ name }}
{% endfor %}

输出：
this is: ding
this is: zhao
this is: wang
```

## 1.3. 转义

```python
{{ '{{' }}

{% raw %}
<ul>
{% for item in seq %}
    <li>{{ item }}</li>
{% endfor %}
</ul>
{% endraw %}
输出：
{{


<ul>
{% for item in seq %}
    <li>{{ item }}</li>
{% endfor %}
</ul>
```