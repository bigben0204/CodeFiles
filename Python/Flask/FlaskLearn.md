# [Python之flask框架](https://www.cnblogs.com/zhaopanpan/p/9033100.html)

## 构造URL

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from flask import Flask, url_for

app = Flask(__name__)
app.debug = True


@app.route('/')
def hello_world():
    return 'Hello, world!'


@app.route('/login')
def login():
    return 'Login'


@app.route('/user/<username>')
def profile(username):
    # show the user profile for that user
    return 'User %s' % username


@app.route('/post/<path:post_id>')
def show_post(post_id):
    # show the post with the given id, the id is an integer
    return 'Post %s' % post_id


if __name__ == '__main__':
    # app.run()
    with app.test_request_context():
        print(url_for('hello_world'))
        print(url_for('login'))
        print(url_for('login', next='/'))
        print(url_for('profile', username='John Doe'))
# 输出：
/
/login
/login?next=%2F
/user/John%20Doe
```

## HTTP方法

如果需要处理具体的HTTP方法，在Flask中也很容易，使用`route`装饰器的`methods`参数设置即可。

```python
from flask import request

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        do_the_login()
    else:
        show_the_login_form()
```

## 模板生成

Flask默认使用[Jinja2](http://jinja.pocoo.org/docs/2.9/templates/)作为模板，Flask会自动配置Jinja 模板，所以我们不需要其他配置了。默认情况下，模板文件需要放在`templates`文件夹下。

使用 Jinja 模板，只需要使用`render_template`函数并传入模板文件名和参数名即可。

```python
from flask import render_template

@app.route('/hello/')
@app.route('/hello/<name>')
def hello(name=None):
    return render_template('hello.html', name=name)
```

相应的模板文件如下。

```
<!doctype html>
<title>Hello from Flask</title>
{% if name %}
  <h1>Hello {{ name }}!</h1>
{% else %}
  <h1>Hello, World!</h1>
{% endif %}
```

## 日志输出

Flask 为我们预配置了一个 Logger，我们可以直接在程序中使用。这个Logger是一个标准的Python Logger，所以我们可以向标准Logger那样配置它，详情可以参考[官方文档](https://docs.python.org/library/logging.html)或者我的文章[Python 日志输出](http://www.jianshu.com/p/9884a660050f)。

```python
@app.route('/hello/')
@app.route('/hello/<name>')
def hello(name=None):
    app.logger.info(f'hello name: {name}')
    return render_template('hello.html', name=name)

app.logger.debug('A value for debugging')
app.logger.warning('A warning occurred (%d apples)', 42)
app.logger.error('An error occurred')
```

## 处理请求

在 Flask 中获取请求参数需要使用`request`等几个全局对象，但是这几个全局对象比较特殊，它们是 **Context Locals** ，其实就是 Web 上下文中局部变量的代理。虽然我们在程序中使用的是全局变量，但是对于每个请求作用域，它们都是互不相同的变量。理解了这一点，后面就非常简单了。

### Request 对象

Request 对象是一个全局对象，利用它的属性和方法，我们可以方便的获取从页面传递过来的参数。

`method`属性会返回HTTP方法的类似，例如`post`和`get`。`form`属性是一个字典，如果数据是POST类型的表单，就可以从`form`属性中获取。下面是 Flask 官方的例子，演示了 Request 对象的`method`和`form`属性。

如果数据是由GET方法传送过来的，可以使用`args`属性获取，这个属性也是一个字典。

```python
@app.route('/login', methods=['POST', 'GET'])
def login():
    if request.method == 'POST':
        user_name = request.form['username']
        password = request.form['password']
    else:
        user_name = request.args.get('username', '')
        password = request.args.get('password', '')
    return f'UserName: {user_name}, password: {password}'
# GET http://127.0.0.1:5000/login?username=ben&password=benpassword
# POST http://127.0.0.1:5000/login
# POST参数 username=ben pasword=benpassword
# 输出：
UserName: ben, password: benpassword
```

### 重定向和错误

`redirect`和`abort`函数用于重定向和返回错误页面。

```python
@app.route('/')
def index():
    return redirect(url_for('login'))


@app.route('/login')
def login():
    abort(401)
    # this_is_never_executed()
```

默认的错误页面是一个空页面，如果需要自定义错误页面，可以使用`errorhandler`装饰器。

```python
from flask import render_template

@app.errorhandler(404)
def page_not_found(error):
    return render_template('page_not_found.html'), 404
```

### 响应处理

默认情况下，Flask会根据函数的返回值自动决定如何处理响应：如果返回值是响应对象，则直接传递给客户端；如果返回值是字符串，那么就会将字符串转换为合适的响应对象。我们也可以自己决定如何设置响应对象，方法也很简单，使用`make_response`函数即可。

```python
@app.errorhandler(404)
def not_found(error):
    resp = make_response(render_template('error.html'), 404)
    resp.headers['X-Something'] = 'A value'
    return resp
```

### Sessions

我们可以使用全局对象`session`来管理用户会话。Sesison 是建立在 Cookie 技术上的，不过在 Flask 中，我们还可以为 Session 指定密钥，这样存储在 Cookie 中的信息就会被加密，从而更加安全。直接看 Flask 官方的例子吧。

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from flask import Flask, session, redirect, url_for, escape, request

app = Flask(__name__)


@app.route('/')
def index():
    if 'username' in session:
        return 'Logged in as %s' % escape(session['username'])
    return 'You are not logged in'


@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        session['username'] = request.form['username']
        return redirect(url_for('index'))
    return '''
        <form method="post">
            <p><input type=text name=username>
            <p><input type=submit value=Login>
        </form>
    '''


@app.route('/logout')
def logout():
    # remove the username from the session if it's there
    session.pop('username', None)
    return redirect(url_for('index'))


# set the secret key.  keep this really secret:
app.secret_key = 'A0Zr98j/3yX R~XHH!jmN]LWX/,?RT'

if __name__ == '__main__':
    app.run()
```







