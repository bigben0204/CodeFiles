# [入门教程: 认识 React](https://zh-hans.reactjs.org/tutorial/tutorial.html)

# React学习之道

## React 基础

### 组件内部状态  

组件内部状态也被称为局部状态，允许你保存、修改和删除存储在组件内部的属性。使用 ES6 类组件可以在构造函数中初始化组件的状态。构造函数只会在组件初始化时调用一次。

让我们引入类构造函数。

```react
class App extends Component {
	constructor(props) {
		super(props);
	}
	...
}
```

当你使用 ES6 编写的组件有一个构造函数时，它需要强制地调用 super(); 方法，因为这个App 组件是 Component 的子类。因此在你的 APP 组件要声明 extends Component 。你会在后续内容中更详细地了解使用 ES6 编写的组件。

你也可以调用 super(props);，它会在你的构造函数中设置 this.props 以供在构造函数中访问它们。否则当在构造函数中访问 this.props ，会得到 undefined。稍后你将了解更多关于 React 组件的 props。

现在，在你的示例中，组件中的初始状态应该是一个列表。

```react
class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            list: list,
        }
    }
	...
}
```

state 通过使用 this 绑定在类上。因此，你可以在整个组件中访问到 state。例如它可以用在 render() 方法中。此前你已经在 render() 方法中映射一个在组件外定义静态列表。现在你可以在组件中使用 state 里的 list 了。  

```react
class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            list: list,
        }
    }

    render() {
        return (
            <div className="App">
                {this.state.list.map(item =>
                    <div key={item.objectID}>
                        <span>
                            <a href={item.url}>{item.title}</a>
                        </span>
                        <span>{item.author}</span>
                        <span>{item.num_comments}</span>
                        <span>{item.points}</span>
                    </div>
                )}
            </div>
        );
    }
}
```

现在 list 是组件的一部分。它驻留在组件的 state 中。你可以从 list 中添加、修改或者删除列表项。**每次你修改组件的内部状态，组件的 render 方法会再次运行**。这样你可以简单地修改组件内部状态，确保组件重新渲染并且展示从内部状态获取到的正确数据。
但是需要注意，不要直接修改 state。你必须使用 setState() 方法来修改它。你将在接下来的章节了解到它。  

### ES6 对象初始化  

在 ES6 中，你可以通过简写属性更加简洁地初始化对象。想象下面的对象初始化：  

```react
const name = 'Robin';
const user = {
    name: name,
}
```

当你的对象中的属性名与变量名相同时，您可以执行以下的操作：  

```react
const name = 'Robin';
const user = {
    name,
}
```

在应用程序中，你也可以这样做。列表变量名和状态属性名称共享同一名称。

```react
// ES5
this.state = {
	list: list,
};
// ES6
this.state = {
	list,
};  
```

另一个简洁的辅助办法是简写方法名。在 ES6 中，你能更简洁地初始化一个对象的方法。  

```react
const user = {
    firstname: 'Robin',
    lastname: 'Wieruch',
};

// ES5
const userService = {
    getUserName: function (user) {
        return user.firstname + ' ' + user.lastname;
    }
}

// ES6
const userService = {
    getUserName(user) {
        return user.firstname + ' ' + user.lastname;
    }
}

// 调用
const name = userService.getUserName(user);
```

最后值得一提的是你可以在 ES6 中使用计算属性名。  

```react
// ES5
var user1 = {
    name: 'Robin'
}

// ES6
const key = 'name';
const user2 = {
    [key]: 'Robin'
}
```

或许你目前还觉得计算属性名没有意义。为什么需要他们呢？在后续的章节中，当你为一个对象动态地根据 key 分配值时便会涉及到。在 JavaScript 中生成查找表是很简单的。  

### 单向数据流  







































































