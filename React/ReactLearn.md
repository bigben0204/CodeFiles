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

现在你的组件中有一些内部的 state。但是你还没有操纵它们，因此 state 是静态的。一个练习 state 操作好方法是增加一些组件的交互。
让我们为列表中的每一项增加一个按钮。按钮的文案为“Dismiss”，意味着将从列表中删除该项。这个按钮在你希望保留未读列表和删除不感兴趣的项时会非常有用。  

```react
class App extends Component {
	// ...

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
                        <span>
                            <button
                                onClick={() => this.onDismiss(item.objectID)}
                                type="button"
                            >
                                Dismiss
                            </button>
                        </span>
                    </div>
                )}
            </div>
        );
    }
}
```

正如你看见的， onDismiss() 方法被另外一个函数包裹在‘ onClick ‘ 事件处理器中，它是一个箭头函数。这样你可以拿到 item 对象中的 objectID 属性来确定那一项会被删除掉。另外一种方法是在 ‘ onClick ‘ 处理器之外定义函数，并只将已定义的函数传到处理器。在后续的章节中会解释更多关于元素处理器的细节。

你有没有注意到按钮元素是多行代码的？元素中一行有多个属性会看起来比较混乱。所以这个按钮使用多行格式来书写以保持它的可读性。这虽然不是强制的，但这是我的极力推荐的代码风格。

现在你需要来完成 onDismiss() 的功能，它通过 id 来标示那一项需要被删除。此函数绑定到类，因此成为类方法。这就是为什么你访问它使用 this.onDismiss() 而不是 onDismiss()。this 对象是类的实例，为了将 onDismiss() 定义为类方法，你需要在构造函数中绑定它。绑定稍后将在另一章中详细解释。  

现在你可以定义方法内部的功能。总的来说你希望从列表中删除由 id 标识的项，并且保存更新后的列表到 state 中。随后这个更新后列表被使用到再次运行的 render() 方法中并渲染，最后这个被删除项就不再显示了。  

你可以通过 JavaScript 内置的 filter 方法来删除列表中的一项。 fitler 方法以一个函数作为输入。这个函数可以访问列表中的每一项，因为它会遍历整个列表。通过这种方式，你可以基于过滤条件来判断列表的每一项。如果该项判断结果为 true，则该项保留在列表中。否则将从列表中过滤掉。另外，好的一点是这个方法会返回一个新的列表而不是改变旧列表。它遵循了 React 中不可变数据的约定。

```react
constructor(props) {
    super(props);
    this.state = {
        list,
    }
    this.onDismiss = this.onDismiss.bind(this);  // 去掉这行也可以
}

onDismiss(id) {
    const updateList = this.state.list.filter(function isNotId(item) {
        return item.objectID !== id;
    });
}
```

在下一步中，你可以抽取函数并将其传递给 filter 函数。  

```react
onDismiss(id) {
    function isNotId(item) {
        return item.objectID !== id;
    }

    const updateList = this.state.list.filter(isNotId);
}
```

另外，可以通过使用 ES6 的箭头函数让代码更简洁。  

```react
onDismiss(id) {
    const isNotId = item => item.objectID !== id;
    const updateList = this.state.list.filter(isNotId);
}
```

你甚至可以内联到一行内完成，就像在按钮的 onClick 事件处理器做的一样，但如此会损失一些可读性。  

```react
onDismiss(id) {
    const updateList = this.state.list.filter(item => item.objectID !== id);
}
```

现在已经从列表中删除了点击项，但是 state 还并没有更新。因此你需要最后使用类方法setState() 来更新组件 satate 中的列表了。  

```react
onDismiss(id) {
    const updateList = this.state.list.filter(item => item.objectID !== id);
    this.setState({list: updateList});
}
```

现在重新运行你的程序并尝试点击“Dismiss”按钮，它应该是工作的。你现在所练习的就是 React 中的单向数据流。你在界面通过 onClick 触发一个动作，再通过函数或类方法修改组件的 state，最后组件的 render() 方法再次运行并更新界面。  

![单向数据流](pictures\React基础_单向数据流.png)

### 绑定  

当使用 ES6 编写的 React 组件时，了解在 JavaScript 类的绑定会非常重要。在前面章节，你已经在构造函数中绑定了 onDismiss() 方法。

```react
class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            list,
        }
        this.onDismiss = this.onDismiss.bind(this);
    }
    // ...
}
```

为什么一开始就需要这么做呢？绑定的步骤是非常重要的，因为类方法不会自动绑定 this到实例上。让我们通过下面的代码来做验证。  

```react
class ExplainBindingsComponent extends Component {
    onClickMe() {
        console.log(this);
    }

    render() {
        return (
            <button onClick={this.onClickMe}  // 这里改成onClick={() => this.onClickMe()}，则可以正常运行
                    type="button">
                Click Me
            </button>
        );
    }
}
```

组件正确的渲染，但是当你点击按钮时候，你会在开发调试控制台中得到 undefined 。这是使用 React 主要的 bug 来源，因为当你想在类方法中访问 this.state 时，由于 this 是undefined 所以并不能被检索到。所以为了确保 this 在类方法中是可访问的，你需要将this 绑定到类方法上。
在下面的组件中，类方法在构造函数中正确绑定。

```react
class ExplainBindingsComponent extends Component {
    constructor(props) {
        super(props);
        this.onClickMe = this.onClickMe.bind(this);
    }
    
    onClickMe() {
        console.log(this);
    }

    render() {
        return (
            <button onClick={this.onClickMe}
                    type="button">
                Click Me
            </button>
        );
    }
}
```

再次尝试点击按钮，这个 this 对象就指向了类的实例。你现在就可以访问到 this.state或者是后面会学习到的 this.props。
类方法的绑定也可以写起其他地方，比如写在 render() 函数中。  

```react
class ExplainBindingsComponent extends Component {
    onClickMe() {
        console.log(this);
    }

    render() {
        return (
            <button onClick={this.onClickMe.bind(this)}
                    type="button">
                Click Me
            </button>
        );
    }
}
```

但是你应该避免这样做，因为它会在每次 render() 方法执行时绑定类方法。总结来说组件每次运行更新时都会导致性能消耗。当在构造函数中绑定时，绑定只会在组件实例化时运行一次，这样做是一个更好的方式。

另外有一些人们提出在构造函数中定义业务逻辑类方法。  

```react
class ExplainBindingsComponent extends Component {
    constructor(props) {
        super(props);

        this.onClickMe = () => {
            console.log(this);
        }
    }

    render() {
        return (
            <button onClick={this.onClickMe}
                    type="button">
                Click Me
            </button>
        );
    }
}
```

你同样也应该避免这样，因为随着时间的推移它会让你的构造函数变得混乱。构造函数目的只是实例化你的类以及所有的属性。这就是为什么我们应该把业务逻辑应该定义在构造函数之外。  

```react
class ExplainBindingsComponent extends Component {
	constructor() {
        super();
        this.doSomething = this.doSomething.bind(this);
        this.doSomethingElse = this.doSomethingElse.bind(this);
	}
    doSomething() {
    // do something
    }
    doSomethingElse() {
    // do something else
    }
    ...
}
```

最后值得一提的是类方法可以通过 ES6 的箭头函数做到自动地绑定。  

```react
class ExplainBindingsComponent extends Component {
    onClickMe = () => {
        console.log(this);
    }

    render() {
        return (
            <button onClick={this.onClickMe}
                    type="button">
                Click Me
            </button>
        );
    }
}
```

如果在构造函数中的重复绑定对你有所困扰，你可以使用这种方式代替。 React 的官方文档中坚持在构造函数中绑定类方法，所以本书也会采用同样的方式。

### 事件处理  

本章节会让你对元素的事件处理有更深入的了解，在你的应用程序中，你将使用下面的按钮来从列表中忽略一项内容。  

```react
<button
	onClick={() => this.onDismiss(item.objectID)}
	type="button"
	>
	Dismiss
</button>
```

这已经是一个复杂的例子了，因为你必须传递一个参数到类的方法，因此你需要将它封装到另一个（箭头）函数中，基本上，由于要传递给事件处理器使用，因此它必须是一个函数。下面的代码不会工作，因为类方法会在浏览器中打开程序时立即执行。  

```react
<button
    onClick={this.onDismiss(item.objectID)}
    type="button"
>
    Dismiss
</button>
```

当使用 onClick={doSomething()} 时， doSomething() 函数会在浏览器打开程序时立即执行，由于监听表达式是函数执行的返回值而不再是函数，所以点击按钮时不会有任何事发生。但当使用 onClick={doSomething} 时，因为 doSomething 是一个函数，所以它会在点击按钮时执行。同样的规则也适用于在程序中使用的 onDismiss() 类方法。  

然而，使用 onClick={this.onDismiss} 并不够，因为这个类方法需要接收 item.objectID 属性来识别那个将要被忽略的项，这就是为什么它需要被封装到另一个函数中来传递这个属性。这个概念在 JavaScript 中被称为高阶函数，稍后会做简要解释。

其中一个解决方案是在外部定义一个包装函数，并且只将定义的函数传递给处理程序。因为需要访问特定的列表项，所以它必须位于 map 函数块的内部。  

```react
class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            list,
        }
        // this.onDismiss = this.onDismiss.bind(this);
    }

    onDismiss(id) {
        const updateList = this.state.list.filter(item => item.objectID !== id);
        this.setState({list: updateList});
    }

    render() {
        return (
            <div className="App">
                {this.state.list.map(item => {
                    const onHandleDismiss = () => this.onDismiss(item.objectID);
                    
                    return (
                        <div key={item.objectID}>
                            <span>
                                <a href={item.url}>{item.title}</a>
                            </span>
                            <span>{item.author}</span>
                            <span>{item.num_comments}</span>
                            <span>{item.points}</span>
                            <span>
                                <button
                                    onClick={onHandleDismiss}
                                    type="button"
                                >
                                    Dismiss
                                </button>
                            </span>
                        </div>
                    );
                    }
                )}
            </div>
        );
    }
}
```

它会在浏览器加载该程序时执行，但点击按钮时并不会。而下面的代码只会在点击按钮时执行。它是一个在触发事件时才会执行的函数。  

```react
...
<button
	onClick={function () {
		console.log(item.objectID)
	}}
	type="button"
>
	Dismiss
</button>
...
```

为了保持简洁，你可以把它转成一个 JavaScript ES6 的箭头函数，和我们在 onDismiss() 类方法时做的一样。

```react
...
<button
    onClick={() => console.log(item.objectID)}
    type="button"
>
	Dismiss
</button>
...
```

经常会有 React 初学者在事件处理中使用函数遇到困难，这就是为什么我要在这里更详细的解释它。最后，你应该使用下面的代码来拥有一个可以访问 item 对象的 objectID 属性简洁的内联 JavaScript ES6 箭头函数。  

```react
class App extends Component {
    ...

    render() {
        return (
            <div className="App">
                {this.state.list.map(item =>
                    <div key={item.objectID}>
                        ...
                        <span>
                            <button
                                onClick={() => this.onDismiss(item.objectID)}
                                type="button"
                            >
                                Dismiss
                            </button>
                        </span>
                    </div>
                )}
            </div>
        );
    }
}
```

另一个经常会被提到的性能相关话题是在事件处理程序中使用箭头函数的影响。例如，onClick 事件处理中的 onDismiss() 方法被封装在另一个箭头函数中以便能传递项标识。因此每次 render() 执行时，事件处理程序就会实例化一个高阶箭头函数，它可能会对你的程序性能产生影响，但在大多数情况下你都不会注意到这个问题。假设你有一个包含1000个项目的巨大数据表，每一行或者列在事件处理程序中都有这样一个箭头函数，这个时候就需要考虑性能影响，因此你可以实现一个专用的按钮组件来在构造函数中绑定方法，但这是一个不成熟的优化。所以现在，专注到学习 React 会更有价值。  

### 和表单交互  

让我们在程序中加入表单来体验 React 和表单事件的交互，我们将在程序中加入搜索功能，列表会根据输入框的内容对标题进行过滤。
第一步，你需要在 JSX 中定义一个带有输入框的表单。  

```react
class App extends Component {
    ...
    
    render() {
        return (
            <div className="App">
                <form>
                    <input type="text"/>
                </form>
                {this.state.list.map(item =>
                    ...
                )}
            </div>
        );
    }
}
```

在下面的场景中，将会使用在输入框中的内容作为搜索字段来临时过滤列表。为了能根据输入框的值过滤列表，你需要将输入框的值储存在你的本地状态中，但是如何访问这个值呢？你可以使用 React 的合成事件来访问事件返回值。

让我们为输入框定义一个 onChange 处理程序。  

```react
class App extends Component {
    ...
    
    render() {
        return (
            <div className="App">
                <form>
                    <input 
                        type="text"
                        onChange={this.onSearchChange}
                    />
                </form>
                {this.state.list.map(item =>
                    ...
                )}
            </div>
        );
    }
}
```

这个函数被绑定到组件上，因此再次成为一个类方法，你必定义方法并 bind 它。  

```react
class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
        	list,
    	};
        
        this.onSearchChange = this.onSearchChange.bind(this);
        this.onDismiss = this.onDismiss.bind(this);
    }

    onSearchChange() {
    	...
    }
    ...
}
```

在元素中使用监听时，你可以在回调函数的签名中访问到 React 的合成事件。  

```react
class App extends Component {
    ...
    onSearchChange(event) {
    	...
    }
    ...
}
```

event 对象的 target 属性中带有输入框的值，因此你可以使用 this.setState() 来更新本地的搜索词的状态了。  

```react
class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            list,
            searchTerm: '',
        }
    }

    onSearchChange(event) {
        this.setState({searchTerm: event.target.value});
    }
}
```

此外，你应该记住在构造函数中为 searchTerm 定义初始状态，输入框在开始时应该是空的，因此初始值应该是空字符串，如上。

现在你将会把输入框每次变化的输入值储存到组件的内部状态中。

关于一个在 React 组件中更新状态的简要说明，在使用 this.setState() 更新 searchTerm时应该把这个列表也传递进去来保留它才是公平的，但事实并非如此， React 的this.setState() 是一个浅合并，在更新一个唯一的属性时，他会保留状态对象中的其他属性，因此即使你已经在列表状态中排除了一个项，在更新 searchTerm 属性时也会保持不变。

让我们回到你的程序中，现在列表还没有根据储存在本地状态中的输入字段进行过滤。基本上，你已经具有了根据 searchTerm 临时过滤列表的所有东西。那么怎么暂时的过滤它呢？你可以在 render() 方法中，在 map 映射列表之前，插入一个过滤的方法。这个过滤方法将只会匹配标题属性中有 searchTerm 内容的列表项。你已经使用过了 JavaScript 内置的filter 功能，让我们再用一次，你可以在 map 函数之前加入 filter 函数，因为 filter 函数返回一个新的数组，所以 map 函数可以这样方便的使用。  

```react
{this.state.list.filter(...).map(item =>
	...
)}
```

让我们用一种不同的方式来处理过滤函数，我们想在 ES6 组件类之外定义一个传递给过滤函数的函数参数，在这里我们不能访问到组件内的状态，所以无法访问 searchTerm 属性来作为筛选条件求值，我们需要传递 searchTerm 到过滤函数并返回一个新函数来根据条件求值，这叫做高阶函数。

一般来说，我不会提到高阶函数，但在 React 中了解高阶函数是有意义的，因为在 React 中有一个高阶组件的概念，你将在这本书的后面了解到这个概念。但现在，让我们关注到过滤器的功能。

首先，你需要在 App 组件外定义一个高阶函数。  

```react
function isSearched(searchTerm) {
    return function(item) {
    	// some condition which returns true or false
    }
}

class App extends Component {
	...
}
```

该函数接受 searchTerm 并返回另一个函数，因为所有的 filter 函数都接受一个函数作为它的输入，返回的函数可以访问列表项目对象，因为它是传给 filter 函数的函数。此外，返回的函数将会根据函数中定义的条件对列表进行过滤。让我们来定义条件。  

```react
function isSearched(searchTerm) {
    return function(item) {
    	return item.title.toLowerCase().includes(searchTerm.toLowerCase());
    }
}

class App extends Component {
	...
}
```

条件是列表中项目的标题属性和输入的 searchTerm 参数相匹配，你可以使用 JavaScript 内置的 includes 功能来实现这一点。只有满足匹配时才会返回 true 并将项目保留在列表中。当不匹配时，项目会从列表中移除。但需要注意的是，你需要把输入内容和待匹配的内容都转换成小写，否则，搜索词 ‘redux’ 和列表中标题叫 ‘Redux’ 的项目无法匹配。由于我们使用的是一个不可变的列表，并使用 filter 函数返回一个新列表，所以本地状态中的原始列表根本就没有被修改过。

还有一点需要注意，我们使用了 Javascript 内置的 includes 功能，它已经是一个 ES6 的特性了。这在 ES5 中该如何实现呢？你将使用 indexOf() 函数来获取列表中项的索引，当项目在列表中时， indexOf() 将会返回它的索引。  

```react
// ES5
string.indexOf(pattern) !== -1

// ES6
string.includes(pattern)
```

另一个优雅的重构可以用 ES6 箭头函数完成，它可以让函数更加整洁:  

```react
// ES5
function isSearched(searchTerm) {
    return function(item) {
    	return item.title.toLowerCase().includes(searchTerm.toLowerCase());
    }
}

// ES6  // 看起来好像不是很直观
const isSearched = searchTerm => item => item.title.toLowerCase().includes(searchTerm.toLowerCase());
```

人们会争论哪个函数更易读，就我个人而言，我更习惯第二个。 React 的生态使用了大量的函数式编程概念。通常情况下，你会使用一个函数返回另一个函数（高阶函数）。在JavaScript ES6 中，可以使用箭头函数更简洁的表达这些。

最后，你需要使用定义的 isSearched() 函数来过滤你的列表，你从本地状态中传递searchTerm 属性返回一个根据条件过滤列表的输入过滤函数。之后它会映射过滤后的列表用于显示每个列表项的元素。  

```react
import './App.css';
import {Component} from "react";

const list = [
    {
        title: 'React',
        url: 'https://facebook.github.io/react/',
        author: 'Jordan Walke',
        num_comments: 3,
        points: 4,
        objectID: 0,
    },
    {
        title: 'Redux',
        url: 'https://github.com/reactjs/redux',
        author: 'Dan Abramov, Andrew Clark',
        num_comments: 2,
        points: 5,
        objectID: 1,
    },
];

const isSearched = searchTerm => item => item.title.toLowerCase().includes(searchTerm.toLowerCase());

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            list,
            searchTerm: '',
        }

        this.onSearchChange = this.onSearchChange.bind(this);  // 这里如果不绑定，会有报错：TypeError: Cannot read property 'setState' of undefined
        this.onDismiss = this.onDismiss.bind(this);
    }

    onDismiss(id) {
        const updateList = this.state.list.filter(item => item.objectID !== id);
        this.setState({list: updateList});
    }

    onSearchChange(event) {
        this.setState({searchTerm: event.target.value});
    }

    render() {
        return (
            <div className="App">
                <form>
                    <input
                        type="text"
                        onChange={this.onSearchChange}
                    />
                </form>
                {this.state.list.filter(isSearched(this.state.searchTerm)).map(item =>
                    <div key={item.objectID}>
                        <span>
                            <a href={item.url}>{item.title}</a>
                        </span>
                        <span>{item.author}</span>
                        <span>{item.num_comments}</span>
                        <span>{item.points}</span>
                        <span>
                            <button
                                onClick={() => this.onDismiss(item.objectID)}
                                type="button"
                            >
                                Dismiss
                            </button>
                        </span>
                    </div>
                )}
            </div>
        );
    }
}

export default App;
```

搜索功能现在应该起作用了，在浏览器中自己尝试一下。  

### ES6 解构  

在 JavaScript ES6 中有一种更方便的方法来访问对象和数组的属性，叫做解构。比较下面JavaScript ES5 和 ES6 的代码片段。

```react
const user = {
    firstname: 'Robin',
    lastname: 'Wieruch',
};
// ES5
var firstname = user.firstname;
var lastname = user.lastname;
console.log(firstname + ' ' + lastname);
// output: Robin Wieruch

// ES6
const { firstname, lastname } = user;
console.log(firstname + ' ' + lastname);
// output: Robin Wieruch
```

在 JavaScript ES5 中每次访问对象的属性都需要额外添加一行代码，但在 JavaScript ES6 中可以在一行中进行。可读性最好的方法是在将对象解构成多个属性时使用多行。

```react
const {
	firstname,
	lastname
} = user;  
```

对于数组一样可以使用解构，同样，多行代码会使你的代码保持可读性。

```react
const users = ['Robin', 'Andrew', 'Dan'];
const [
    userOne,
    userTwo,
    userThree
] = users;
console.log(userOne, userTwo, userThree);
// output: Robin Andrew Dan
```

也许你已经注意到，程序组件内的状态对象也可以使用同样的方式解构，你可以让 map 和 filter 部分的代码更简短。  

```react
render() {
        const {searchTerm, list} = this.state;
        return (
            <div className="App">
				...                
                {list.filter(isSearched(searchTerm)).map(item =>
                ...
                )}
            </div>
```

你也可以使用 ES5 或者 ES6 的方式来做：

```react
// ES5
var searchTerm = this.state.searchTerm;
var list = this.state.list;
// ES6
const { searchTerm, list } = this.state;
```

但由于这本书大部分时候都使用了 JavaScript ES6，所以你也可以坚持使用它。  

### 受控组件  

你已经了解了 React 中的单向数据流，同样的规则适用于更新本地状态 searchTerm 来过滤列表的输入框。当状态变化时， render() 方法将再次运行，并使用最新状态中的 searchTerm值来作为过滤条件。

但是我们是否忘记了输入元素的一些东西？一个 HTML 输入标签带有一个 value 属性，这个属性通常有一个值作为输入框的显示，在本例中，它是 searchTerm 属性。然而，看起来我们在 React 好像并不需要它。

这是错误的，表单元素比如 \<input\>, \<textarea\> 和 \<select\> 会以原生 HTML 的形式保存他们自己的状态。一旦有人从外部做了一些修改，它们就会修改内部的值，在 React 中这被称为不受控组件，因为它们自己处理状态。在 React 中，你应该确保这些元素变为受控组件。

你应该怎么做呢？你只需要设置输入框的值属性，这个值已经在 searchTerm 状态属性中保存了，那么为什么不从这里访问呢？  

```react
class App extends Component {
    ...

    render() {
        const {searchTerm, list} = this.state;
        return (
            <div className="App">
                <form>
                    <input
                        type="text"
                        value={searchTerm}
                        onChange={this.onSearchChange}
                    />
                </form>
                ...
            </div>
        );
    }
}
```

就是这样。现在输入框的单项数据流循环是自包含的，组件内部状态是输入框的唯一数据来源。

整个内部状态管理和单向数据流可能对你来说比较新，但你一旦习惯了它，你就会自然而然的在 React 中实现它。一般来说， React 带来一种新的模式，将单向数据流引入到单页面应用的生态中，到目前为止，它已经被几个框架和库所采用。  

### 拆分组件  





























