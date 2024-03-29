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

现在，你有一个大型的 App 组件。它在不停地扩展，最终可能会变得混乱。你可以开始将它拆分成若干个更小的组件。

让我们开始使用一个用于搜索的输入组件和一个用于展示的列表组件。  

你可以给组件传递属性并在组件中使用它们。至于 App 组件，它需要传递由本地状态(state) 托管的属性和它自己的类方法。  

现在你可以接着 App 组件定义这些组件。这些组件仍然是 ES6 类组件，它们会渲染和之前相同的元素。

第一个是 Search 组件。  

第二个是 Table 组件。  

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

class Search extends Component {
    render() {
        const {value, onChange} = this.props;
        return (
            <form>
                <input
                    type="text"
                    value={value}
                    onChange={onChange}
                />
            </form>
        );
    }
}

class Table extends Component {
    render() {
        const {list, pattern, onDismiss} = this.props;
        return (
            <div>
                {list.filter(isSearched(pattern)).map(item =>
                    <div key={item.objectID}>
                        <span>
                            <a href={item.url}>{item.title}</a>
                        </span>
                        <span>{item.author}</span>
                        <span>{item.num_comments}</span>
                        <span>{item.points}</span>
                        <span>
                            <button
                                onClick={() => onDismiss(item.objectID)}
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

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            list,
            searchTerm: '',
        }

        this.onSearchChange = this.onSearchChange.bind(this);
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
        const {searchTerm, list} = this.state;
        return (
            <div className="App">
                <Search
                    value={searchTerm}
                    onChange={this.onSearchChange}
                />
                <Table
                    list={list}
                    pattern={searchTerm}
                    onDismiss={this.onDismiss}
                />
            </div>
        );
    }
}

export default App;
```

现在你有了三个 ES6 类组件。你可能已经注意到， props 对象可以通过这个类实例的 this来访问。 props 是 properties 的简写，当你在 App 组件里面使用它时，它有你传递给这些组件的所有值。这样，组件可以沿着组件树向下传递属性。

从 App 组件中提取这些组件之后，你就可以在别的地方去重用它们了。因为组件是通过props 对象来获取它们的值，所以当你在别的地方重用它时，你可以每一次都传递不同的props，这些组件就变得可复用了。  

### 可组合组件

在 props 对象中还有一个小小的属性可供使用: children 属性。通过它你可以将元素从上层传递到你的组件中，这些元素对你的组件来说是未知的，但是却为组件相互组合提供了可能性。让我们来看一看，当你只将一个文本（字符串）作为子元素传递到 Search 组件中会怎样。  

```react
class App extends Component {
    ...

    render() {
        const {searchTerm, list} = this.state;
        return (
            <div className="App">
                <Search
                    value={searchTerm}
                    onChange={this.onSearchChange}
                >
                    Search
                </Search>
                <Table
                    list={list}
                    pattern={searchTerm}
                    onDismiss={this.onDismiss}
                />
            </div>
        );
    }
}
```

现在 Search 组件可以从 props 对象中解构出 children 属性。然后它就可以指定这个 children 应该显示在哪里。  

```react
class Search extends Component {
    render() {
        const {value, onChange, children} = this.props;
        return (
            <form>
                {children} <input
                type="text"
                value={value}
                onChange={onChange}
            />
            </form>
        );
    }
}
```

现在，你应该可以在输入框旁边看到这个 “Search” 文本了。当你在别的地方使用 Search 组件时，如果你喜欢，你可以选择一个不同的文本。总之，它不仅可以把文本作为子元素传递，还可以将一个元素或者元素树（它还可以再次封装成组件）作为子元素传递。 children 属性让组件相互组合到一起成为可能。  

### 可复用组件

可复用和可组合组件让你能够思考合理的组件分层，它们是 React 视图层的基础。前面几章提到了可重用性的术语。现在你可以复用 Search 和 Table 组件了。甚至 App 组件都是可复用的了，因为你可以在别的地方重新实例化它。

让我们再来定义一个可复用组件 Button，最终会被更频繁地复用。  

**注：这里通过中括号来对对象取值。**

```react
class Button extends Component {
    render() {
        const {onClick, className, children} = this.props;
        return (
            <button
                onClick={onClick}
                className={className}
                type="button"
            >
                {children}
            </button>
        );
    }
}
```

声明这样一个组件可能看起来有点多余。你将会用 Button 组件来替代 button 元素。它只省去了 type="button"。当你想使用 Button 组件的时候，你还得去定义除了 type 之外的所有属性。但这里你必须要考虑到长期投资。想象在你的应用中有若干个 button，但是你想改变它们的一个属性、样式或者行为。如果没有这个组件的话，你就必须重构每个 button。相反， Button 组件拥有单一可信数据源。一个 Button 组件可以立即重构所有 button。一个Button 组件统治所有的 button。

既然你已经有了 button 元素，你可以用 Button 组件代替。它省略了 type 属性，因为 Button组件已经指定了。  

```react
class Table extends Component {
    render() {
        const {list, pattern, onDismiss} = this.props;
        return (
            <div>
                {list.filter(isSearched(pattern)).map(item =>
                    <div key={item.objectID}>
                        <span>
                            <a href={item.url}>{item.title}</a>
                        </span>
                        <span>{item.author}</span>
                        <span>{item.num_comments}</span>
                        <span>{item.points}</span>
                        <span>
                            <Button
                                onClick={() => onDismiss(item.objectID)}
                            >
                                Dismiss
                            </Button>
                        </span>
                    </div>
                )}
            </div>
        );
    }
}
```

Button 组件期望在 props 里面有一个 className 属性. className 属性是 React 基于 HTML 属性 class 的另一个衍生物。但是当使用 Button 组件时，我们并没有传递任何 className 属性，所以在 Button 组件的代码中，我们更应该明确地标明 className 是可选的。

因此，你可以使用默认参数，它是一个 JavaScript ES6 的特性。  

```react
class Button extends Component {
    render() {
        const {onClick, className='', children} = this.props;
        return (
            <button
                onClick={onClick}
                className={className}
                type="button"
            >
                {children}
            </button>
        );
    }
}
```

这样当使用 Button 组件时，若没有指定 className 属性，它的值就是一个空字符串，而非undefined。  

现在你已经有四个 ES6 类组件了，但是你可以做得更好。让我来介绍一下函数式无状态组件 (functional stateless components)，作为除了 ES6 类组件的另一个选择。在重构你的组件之前，让我来介绍一下 React 不同的组件类型。

*  函数式无状态组件: 这类组件就是函数，它们接收一个输入并返回一个输出。输入是props，输出就是一个普通的 JSX 组件实例。到这里，它和 ES6 类组件非常的相似。然而，函数式无状态组件是函数（函数式的），并且它们没有本地状态（无状态的）。你不能通过 this.state 或者 this.setState() 来访问或者更新状态，因为这里没有 this对象。此外，它也没有生命周期方法。虽然你还没有学过生命周期方法，但是你已经用到了其中两个： constructor() and render()。 constructor 在一个组件的生命周期中只执行一次，而 render() 方法会在最开始执行一次，并且每次组件更新时都会执行。当你阅读到后面关于生命周期方法的章节时，要记得函数式无状态组件是没有生命周期方法的。
* ES6 类组件: 在你的四个组件中，你已经使用过这类组件了。在类的定义中，它们继承自 React 组件。 extend 会注册所有的生命周期方法，只要在 React component API 中，都可以在你的组件中使用。通过这种方式你可以使用 render() 类方法。此外，通过使用 this.state 和 this.setState()，你可以在 ES6 类组件中储存和操控 state。
* React.createClass: 这类组件声明曾经在老版本的 React 中使用，仍然存在于很多 ES5 React 应用中。但是为了支持 JavaScript ES6， Facebook 声明它已经不推荐使用了。他们还在 React 15.5 中加入了不推荐使用的警告。你不会在本书使用它。  

因此这里基本只剩下两种组件声明了。但是什么时候更适合使用函数式无状态组件而非ES6 类组件？一个经验法则就是当你不需要本地状态或者组件生命周期方法时，你就应该使用函数式无状态组件。最开始一般使用函数式无状态组件来实现你的组件，一旦你需要访问 state 或者生命周期方法时，你就必须要将它重构成一个 ES6 类组件。在我们的应用中，为了学习 React，我们采取了相反的方式。  

让我们回到你的应用中。 App 组件使用内部状态，这就是为什么它必须作为 ES6 类组件存在的原因。但是你的其他三个 ES6 类组件都是无状态的，它们不需要使用 this.state 或者this.setState()，甚至都不需要使用生命周期函数。让我们一起把 Search 组件重构成一个函数式无状态组件。 Table 和 Button 组件的重构会留做你的练习。  

```react
function Search(props) {
    const {value, onChange, children} = props;
    return (
        <form>
            {children} <input
            type="text"
            value={value}
            onChange={onChange}
        />
        </form>
    );
}
```

基本上就是这样了。 props 可以在函数签名71（译者注：这里应指函数入参）中访问，返回值是 JSX。你已经知道 ES6 解构了，所以在函数式无状态组件中，你可以优化之前的写法。最佳实践就是在函数签名中通过解构 props 来使用它。  

```react
function Search({value, onChange, children}) {
    return (
        <form>
            {children} <input
            type="text"
            value={value}
            onChange={onChange}
        />
        </form>
    );
}
```

但是它还可以变得更好。你已经知道， ES6 箭头函数允许让你保持你的函数简洁。你可以移除函数的块声明（译者注：即花括号 {}）。在简化的函数体中，表达式会自动作为返回值，因此你可以将 return 语句移除。因为你的函数式无状态组件是一个函数，你同样可以用这种方式来简化它。  

```react
const Search = ({value, onChange, children}) =>
    <form>
        {children} <input
        type="text"
        value={value}
        onChange={onChange}
    />
    </form>;
```

最后一步对于强制只用 props 作为输入和 JSX 作为输出非常有用。这之间没有任何别的东西。但是你仍然可以在 ES6 箭头函数块声明中做一些事情。  

```react
const Search = ({value, onChange, children}) => {
    // do something
    return (
        <form>
            {children} <input
            type="text"
            value={value}
            onChange={onChange}
        />
        </form>
    );
}
```

但是你现在并不需要这样做，这也是为什么你可以让之前的版本没有块声明。当使用块声明时，人们往往容易在这个函数里面做过多的事情。通过移除块声明，你可以专注在函数的输入和输出上。

现在你已经有一个轻量的函数式无状态组件了。一旦你需要访问它的内部组件状态或者生命周期方法，你最好将它重构成一个 ES6 类组件。另外，你也已经看到， JavaScript ES6 是如何被用到 React 组件中并让它们变得更加的简洁和优雅。  

### 给组件声明样式  

让我们给你的应用和组件添加一些基本的样式。你可以复用 src/App.css 和 src/index.css 文件。因为你是用 create-react-app 来创建的，所以这些文件应该已经在你的项目中了。它们应该也被引入到你的 src/App.js 和 src/index.js 文件中了。我准备了一些 CSS，你可以直接复制粘贴到这些文件中，你也可以随意使用你自己的样式。  

首先，给你的整个应用声明样式。  

```css
body {
    color: #222;
    background: #f4f4f4;
    font: 400 14px CoreSans, Arial, sans-serif;
}

a {
    color: #222;
}

a:hover {
    text-decoration: underline;
}

ul, li {
    list-style: none;
    padding: 0;
    margin: 0;
}

input {
    padding: 10px;
    border-radius: 5px;
    outline: none;
    margin-right: 10px;
    border: 1px solid #dddddd;
}

button {
    padding: 10px;
    border-radius: 5px;
    border: 1px solid #dddddd;
    background: transparent;
    color: #808080;
    cursor: pointer;
}

button:hover {
    color: #222;
}

*:focus {
    outline: none;
}
```

其次，在 App 文件中给你的组件声明样式。  

```css
.page {
    margin: 20px;
}

.interactions {
    text-align: center;
}

.table {
    margin: 20px 0;
}

.table-header {
    display: flex;
    line-height: 24px;
    font-size: 16px;
    padding: 0 10px;
    justify-content: space-between;
}

.table-empty {
    margin: 200px;
    text-align: center;
    font-size: 16px;
}

.table-row {
    display: flex;
    line-height: 24px;
    white-space: nowrap;
    margin: 10px 0;
    padding: 10px;
    background: #ffffff;
    border: 1px solid #e3e3e3;
}

.table-header > span {
    overflow: hidden;
    text-overflow: ellipsis;
    padding: 0 5px;
}

.table-row > span {
    overflow: hidden;
    text-overflow: ellipsis;
    padding: 0 5px;
}

.button-inline {
    border-width: 0;
    background: transparent;
    color: inherit;
    text-align: inherit;
    -webkit-font-smoothing: inherit;
    padding: 0;
    font-size: inherit;
    cursor: pointer;
}

.button-active {
    border-radius: 0;
    border-bottom: 1px solid #38BB6C;
}
```

现在你可以在一些组件中使用这些样式。但是别忘了使用 React 的 className，而不是HTML 的 class 属性。
首先，将它应用到你的 App ES6 类组件中。

```react
class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            list,
            searchTerm: '',
        }

        this.onSearchChange = this.onSearchChange.bind(this);
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
        const {searchTerm, list} = this.state;
        return (
            <div className="page">
                <div className="interactions">
                    <Search
                        value={searchTerm}
                        onChange={this.onSearchChange}
                    >
                        Search
                    </Search>
                </div>
                <Table
                    list={list}
                    pattern={searchTerm}
                    onDismiss={this.onDismiss}
                />
            </div>
        );
    }
}
```

其次，将它应用到你的 Table 函数式无状态组件中。  

```react
const Table = ({list, pattern, onDismiss}) =>
    <div className="table">
        {list.filter(isSearched(pattern)).map(item =>
            <div key={item.objectID} className="table-row">
                        <span>
                            <a href={item.url}>{item.title}</a>
                        </span>
                <span>{item.author}</span>
                <span>{item.num_comments}</span>
                <span>{item.points}</span>
                <span>
                    <Button
                        onClick={() => onDismiss(item.objectID)}
                        className="button-inline"
                    >
                        Dismiss
                    </Button>
                </span>
            </div>
        )}
    </div>
```

现在你已经给你的应用和组件添加了基本的 CSS 样式，看起来应该非常不错。如你所知， JSX 混合了 HTML 和 JavaScript。现在有人呼吁将 CSS 也加入进去，这就叫作内联样式(inline style)。你可以定义 JavaScript 对象，并传给一个元素的 style 属性。

让我们通过使用内联样式来使 Table 的列宽自适应。  

```react
const Table = ({list, pattern, onDismiss}) =>
    <div className="table">
        {list.filter(isSearched(pattern)).map(item =>
            <div key={item.objectID} className="table-row">
                <span style={{width: '40%'}}>
                    <a href={item.url}>{item.title}</a>
                </span>
                <span style={{width: '30%'}}>
                    {item.author}
                </span>
                <span style={{width: '10%'}}>
                    {item.num_comments}
                </span>
                <span style={{width: '10%'}}>
                    {item.points}
                </span>
                <span style={{width: '10%'}}>
                    <Button
                        onClick={() => onDismiss(item.objectID)}
                        className="button-inline"
                    >
                        Dismiss
                    </Button>
                </span>
            </div>
        )}
    </div>
```

现在样式已经内联了。你可以在你的元素之外定义一个 style 对象，这样可以让它变得更整洁。  

```react
const largeColumn = {
    width: '40%',
};
const midColumn = {
    width: '30%',
};
const smallColumn = {
    width: '10%',
};

const Table = ({list, pattern, onDismiss}) =>
    <div className="table">
        {list.filter(isSearched(pattern)).map(item =>
            <div key={item.objectID} className="table-row">
                <span style={largeColumn}>
                    <a href={item.url}>{item.title}</a>
                </span>
                <span style={midColumn}>
                    {item.author}
                </span>
                <span style={smallColumn}>
                    {item.num_comments}
                </span>
                <span style={smallColumn}>
                    {item.points}
                </span>
                <span style={smallColumn}>
                    <Button
                        onClick={() => onDismiss(item.objectID)}
                        className="button-inline"
                    >
                        Dismiss
                    </Button>
                </span>
            </div>
        )}
    </div>
```

随后你可以将它们用于你的 columns ： `<span style={smallColumn}>`。

总而言之，关于 React 中的样式，你会找到不同的意见和解决方案。现在你已经用过纯 CSS 和内联样式了。这足以开始。

在这里我不想下定论，但是想给你一些更多的选择。你可以自行阅读并应用它们。但是如果你刚开始使用 React，目前我会推荐你坚持纯 CSS 和内联样式 。

你已经学习了编写一个 React 应用所需要的基础知识了！让我们来回顾一下前面几个章节:
• React
– 使用 this.state 和 setState() 来管理你的内部组件状态
– 将函数或者类方法传递到你的元素处理器
– 在 React 中使用表单或者事件来添加交互
– 在 React 中单向数据流是一个非常重要的概念
– 拥抱 controlled components
– 通过 children 和可复用组件来组合组件
– ES6 类组件和函数式无状态组件的使用方法和实现
– 给你的组件声明样式的方法
• ES6
– 绑定到一个类的函数叫作类方法
– 解构对象和数组
– 默认参数
• General
– 高阶函数
该休息一下了，吸收这些知识然后转化成你自己的东西。你可以用你已有的代码来做个实验。另外，你可以进一步阅读官方文档75
你可以在官方代码仓库76找到源码。  

## 使用真实的 API  

现在是时候使用真实的 API 了，老是处理样本数据会变得很无聊。

如果你对 API 不熟悉，我建议你去读读我的博客，里面有关于我是怎样了解 API 的。

你知道 Hacker News78 这个平台吗？它是一个很棒的技术新闻整合平台。在本书中，你将使用它的 API 来获取热门资讯。它有一个基础 API 79 和一个搜索 API 80来获取数据。后者使我们可以去搜索 Hacker News 上的资讯。你也可以通过 API 规范来了解它的数据结构。  

### 生命周期方法  

在你开始在组件中通过 API 来获取数据之前，你需要知道 React 的生命周期方法。这些方法是嵌入 React 组件生命周期中的一组挂钩。它们可以在 ES6 类组件中使用，但是不能在无状态组件中使用。

你还记得前章中讲过的 JavaScript ES6 类以及如何在 React 中使用它们吗？除了 render() 方法外，还有几个方法可以在 React ES6 类组件中被覆写。所有的这些都是生命周期方法。现在让我们来深入了解他们：

通过之前的学习，你已经知道两种能够用在 ES6 类组件中的生命周期方法： constructor() 和 render()。

constructor（构造函数）只有在组件实例化并插入到 DOM 中的时候才会被调用。组件实例化的过程称作组件的挂载（mount）。

render() 方法也会在组件挂载的过程中被调用，同时当组件更新的时候也会被调用。每当组件的状态（state）或者属性（props）改变时，组件的 render() 方法都会被调用。

现在你了解了更多关于这两个生命周期方法的知识，也知道它们什么时候会被调用了。你也已经在前面的学习中使用过它们了。但是 React 里还有更多的生命周期方法。  

在组件挂载的过程中还有另外两个生命周期方法： componentWillMount() 和 componentDidMount()。构造函数（constructor）最先执行， componentWillMount() 会在 render() 方法之前执行，而 componentDidMount() 在 render() 方法之后执行。  

总而言之，在挂载过程中有四个生命周期方法，它们的调用顺序是这样的：

* constructor()
* componentWillMount()
* render()
* componentDidMount()  

但是当组件的状态或者属性改变的时候用来更新组件的生命周期是什么样的呢？总的来说，它一共有5个生命周期方法用于组件更新，调用顺序如下：  

* componentWillReceiveProps()
* shouldComponentUpdate()
* componentWillUpdate()
* render()
* componentDidUpdate()  

最后但同样重要的，组件卸载也有生命周期。它只有一个生命周期方法： componentWillUnmount()。  

但是毕竟你不用一开始就了解所有生命周期方法。这样可能吓到你，而你也并不会用到所有的方法。即使在一个很大的 React 应用当中，除了 constructor() 和 render() 比较常用外，你只会用到一小部分生命周期函数。即使这样，了解每个生命周期方法的适用场景还是对你有帮助的：  

* constructor(props) - 它在组件初始化时被调用。在这个方法中，你可以设置初始化状态以及绑定类方法。
* componentWillMount() - 它在 render() 方法之前被调用。这就是为什么它可以用作去设置组件内部的状态，因为它不会触发组件的再次渲染。但一般来说，还是推荐在 constructor() 中去初始化状态。
* render() - 这个生命周期方法是必须有的，它返回作为组件输出的元素。这个方法应该是一个纯函数，因此不应该在这个方法中修改组件的状态。它把属性和状态作为输入并且返回（需要渲染的）元素
* componentDidMount() - 它仅在组件挂载后执行一次。这是发起异步请求去 API 获取数据的绝佳时期。获取到的数据将被保存在内部组件的状态中然后在 render() 生命周期方法中展示出来。
* componentWillReceiveProps(nextProps) - 这个方法在一个更新生命周（update lifecycle）中被调用。新的属性会作为它的输入。因此你可以利用 this.props 来对比之后的属性和之前的属性，基于对比的结果去实现不同的行为。此外，你可以基于新的属性来设置组件的状态。
* shouldComponentUpdate(nextProps, nextState) - 每次组件因为状态或者属性更改而更新时，它都会被调用。你将在成熟的 React 应用中使用它来进行性能优化。在一个更新生命周期中，组件及其子组件将根据该方法返回的布尔值来决定是否重新渲染。这样你可以阻止组件的渲染生命周期（render lifecycle）方法，避免不必要的渲染。
* componentWillUpdate(nextProps, nextState) - 这个方法是 render() 执行之前的最后一个方法。你已经拥有下一个属性和状态，它们可以在这个方法中任由你处置。你可以利用这个方法在渲染之前进行最后的准备。注意在这个生命周期方法中你不能再触发 setState()。如果你想基于新的属性计算状态，你必须利用componentWillReceiveProps()。
* componentDidUpdate(prevProps, prevState) - 这个方法在 render() 之后立即调用。你可以用它当成操作 DOM 或者执行更多异步请求的机会。
* componentWillUnmount() - 它会在组件销毁之前被调用。你可以利用这个生命周期
  方法去执行任何清理任务。  

之前你已经用过了 constructor() 和 render() 生命周期方法。对于 ES6 类组件来说他们是常用的生命周期方法。实际上 render() 是必须有的，否则它将不会返回一个组件实例。 

还有另一个生命周期方法： componentDidCatch(error, info)。它在 React 1681 中引入，用来捕获组件的错误。举例来说，在你的应用中展示样本数据本来是没问题的。但是可能会有列表的本地状态被意外设置成 null 的情况发生（例如从外部 API 获取列表失败时，你把本地状态设置为空了）。然后它就不能像之前一样去过滤（filter）和映射（map）这个列表，因为它不是一个空列表（[]）而是 null。这时组件就会崩溃，然后整个应用就会挂掉。现在你可以用 componentDidCatch() 来捕获错误，将它存在本地的状态中，然后像用户展示一条信息，说明应用发生了错误。  

### 获取数据  

现在你已经做好了从 Hacker News API 获取数据的准备。我们可以用上文所提到过的componentDidMount() 生命周期方法来获取数据。你将使用 JavaScript 原生的 fetch API 来发起请求。

在开始之前，让我们设置好 URL 常量和默认参数，来将 API 请求分解成几步。  

```react
import './App.css';
import {Component} from "react";

const DEFAULT_QUERY = 'redux';

const PATH_BASE = 'https://hn.algolia.com/api/v1';
const PATH_SEARCH = '/search';
const PARAM_SEARCH = 'query=';
```

在 JavaScript ES6 中，你可以用模板字符串（template strings）去连接字符串。你将用它来拼接最终的 API 访问地址。  

```react
// ES6
const url = `${PATH_BASE}${PATH_SEARCH}?${PARAM_SEARCH}${DEFAULT_QUERY}`;

// ES5
var url = PATH_BASE + PATH_SEARCH + '?' + PARAM_SEARCH + DEFAULT_QUERY;

console.log(url);
// output: https://hn.algolia.com/api/v1/search?query=redux
```

这样就可以保证以后你 URL 组合的灵活性。

让我们开始使用 API 请求，在这个请求中将用到上述的网址。整个数据获取的过程在下面代码中一次给出，但后面会对每一步做详细解释。  

```react
class App extends Component {
    constructor(props) {
        super(props);

        this.state = {
            result: null,
            searchTerm: DEFAULT_QUERY,
        }

        this.setSearchTopStories = this.setSearchTopStories.bind(this);
        this.fetchSearchTopStories = this.fetchSearchTopStories.bind(this);
        this.onSearchChange = this.onSearchChange.bind(this);
        this.onDismiss = this.onDismiss.bind(this);
    }

    setSearchTopStories(result) {
        this.setState({result});
    }

    fetchSearchTopStories(searchTerm) {
        fetch(`${PATH_BASE}${PATH_SEARCH}?${PARAM_SEARCH}${DEFAULT_QUERY}`)
            .then(response => response.json())
            .then(result => this.setSearchTopStories(result))
            .catch(e => e);
    }

    componentDidMount() {
        const {searchTerm} = this.state;
        this.fetchSearchTopStories(searchTerm);
    }

    onDismiss(id) {
        const {result} = this.state;
        const list = result.hits;
        const updateList = list.filter(item => item.objectID !== id);
        result.hits = updateList;
        this.setState({result});
    }

    onSearchChange(event) {
        this.setState({searchTerm: event.target.value});
    }

    render() {
        const {searchTerm, result} = this.state;
        if (!result) {
            return null;
        }

        return (
            <div className="page">
                <div className="interactions">
                    <Search
                        value={searchTerm}
                        onChange={this.onSearchChange}
                    >
                        Search
                    </Search>
                </div>
                <Table
                    list={result.hits}
                    pattern={searchTerm}
                    onDismiss={this.onDismiss}
                />
            </div>
        );
    }
}
```

这段代码做了很多事。我想把它分成更小的代码段，但是那样又会让人很难去理解每段代码之间的关系。接下来我就来详细解释代码中的每一步。

首先，你可以移除样本列表了，因为你将从 Hacker News API 得到一个真实的列表。这些样本数据已经没用了。现在组件将一个空的列表结果以及一个默认的搜索词作为初始状态。这个默认搜索词也同样用在 Search 组件的输入字段和第一个 API 请求中。

其次，在组件挂载之后，它用了 componentDidMount() 生命周期方法去获取数据。在第一次获取数据时，使用的是本地状态中的默认搜索词。它将获取与“redux”相关的资讯，因为它是默认的参数。

再次，这里使用的是原生的 fetch API。 JavaScript ES6 模板字符串允许组件利用 searchTerm来组成 URL。该 URL 是原生 fetch API 函数的参数。返回的响应需要被转化成 JSON 格式的数据结构。这是在处理 JSON 数据结构时，原生的 fetch API 中的强制步骤。最后将处理后的响应赋值给组件内部状态中的结果。此外，我们用一段 catch 代码来处理出错的情况。如果在发起请求时出现错误，这个函数会进入到 catch 中而不是 then 中。在本书之后的章节中，将涵盖错误处理的内容。

最后但同样重要的是，不要忘记在构造函数中绑定你的组件方法。

现在你可以用获取的数据去代替样本数据了。然而，你必须注意一点，这个结果不仅仅是一个数据的列表。它也是一个复杂的对象， 它包含了元数据信息以及一系列的 hits，在我们的应用里就是这些资讯86。你可以在 render() 方法中用 console.log(this.state); 将这些信息打印出来，以便有一个直观的认识。

在接下来的步骤中，你将把之前的得到的结果渲染出来。但我们不会什么都渲染，在刚开始没有拿到结果时，我们会返回空。一旦 API 请求成功，我们会将结果保存在状态里，然后 App 组件将用更新后的状态重新渲染。  

让我们回顾一下在组将的整个生命周期中发生了什么。首先组件通过构造函数得到初始化，之后它将初始化的状态渲染出来。但是你阻止了组件的显示，因为此时本地状态中的结果为空。 React 允许组件通过返回 null 来不渲染任何东西。接着 componentDidMount() 生命周期函数执行。在这个方法中你从 Hacker News API 中异步地拿到了数据。一旦数据到达，组件就通过 setSearchTopStories() 函数改变组件内部的状态。之后，因为状态的更新，更新生命周期开始运行。组件再次执行 render() 方法，但这次组件的内部状态中的结果已经填充，不再是空了。因此组件将重新渲染 Table 组件的内容。

你使用了大多数浏览器支持的原生 fetch API 来执行对 API 的异步请求。 create-react-app 中的配置保证了它被所有浏览器支持。你也可以使用第三方库来代替原生 fetch API，例如：superagent87 和 axios88。

让我们重回到你的应用，现在你应该可以看到资讯列表了。然而，现在应用中仍然存在两个 bug。第一，“Dismiss”按钮不工作。因为它还不能处理这个复杂的 result 对象。当我们点击“Dismiss”按钮时，它仍然在操作之前那个简单的 result 对象。第二，当这个列表显示出来之后，你再尝试搜索其他的东西时，它只会在客户端过滤已有的列表，即使初始化的资讯搜索是在服务器端进行的。我们期待的行为是：当我们使用 Search 组件时，从 API拿到新的结果，而不是去过滤样本数据。不用担心，两个 bug 都将在之后的章节中得到修复。  

### 扩展操作符  

“Dismiss”按钮之所以不工作，是因为 onDismiss() 方法不能处理复杂的 result 对象。它现在还只能处理一个本地状态中的简单列表。但是现在这个列表已经不再是简单的平铺列表了。现在，让我们去操作这个 result 对象而不是去操作列表。

```react 
    onDismiss(id) {
        const isNotId = item => item.objectID != id;
        const updateHits = this.state.result.hits.filter(isNotId);
        this.setState({
            ...
        });
    }
```

那现在 setState() 中发生了什么呢？很遗憾，这个 result 是一个复杂的对象。资讯（hits）列表只是这个对象的众多属性之一。所以，当某一项资讯从 result 对象中移除时，只能更新资讯列表，其他的属性还是得保持原样。

解决方法之一是直接改变 result 对象中的 hits 字段。我将演示这个方法，但实际操作中我们一般不这样做。  

```react
// don't do this
this.state.result.hits = updateHits;
```

React 拥护不可变的数据结构。因此你不应该改变一个对象（或者直接改变状态）。更好的做法是基于现在拥有的资源来创建一个新的对象。这样就没有任何对象被改变了。这样做的好处是数据结构将保持不变，因为你总是返回一个新对象，而之前的对象保持不变。  

因此你可以用 JavaScript ES6 中的 Object.assign() 函数来到达这样的目的。它把接收的第一个参数作为目标对象，后面的所有参数作为源对象。然后把所有的源对象合并到目标对象中。只要把目标对象设置成一个空对象，我们就得到了一个新的对象。这种做法是拥抱不变性的，因为没有任何源对象被改变。以下是代码实现：  

```react
const updatedHitsObj = {hits: updatedHits};
const updatedResult = Object.assign({}, this.state.result, updatedHitsObj);
```

当遇到相同的属性时，排在后面的对象会覆写先前对象的该属性。现在让我们用它来改写onDismiss() 方法：  

```react
    onDismiss(id) {
        const isNotId = item => item.objectID !== id;
        const updatedHits = this.state.result.hits.filter(isNotId);
        this.setState({
            result: Object.assign({}, this.state.result, {hits: updatedHits})
        });
    }
```

这已经是一个解决方案了。但是在 JavaScript ES6 以及之后的 JavaScript 版本中还有一个更简单的方法。现在我将向你介绍扩展操作符。它只由三个点组成： ...。当使用它时，数组或对象中的每一个值都会被拷贝到一个新的数组或对象。

让我们先来看一下 ES6 中数组的扩展运算符，虽然你现在还用不到它。

```react
const userList = ['Robin', 'Andrew', 'Dan'];
const additionalUser = 'Jordan';
const allUsers = [...userList, additionalUser];

console.log(allUsers);
// output: ['Robin', 'Andrew', 'Dan', 'Jordan']
```

这里 allUsers 是一个全新的数组变量，而变量 userList 和 additionalUser 还是和原来一样。用这个运算符，你甚至可以合并两个数组到一个新的数组中。  

```react
const oldUsers = ['Robin', 'Andrew'];
const newUsers = ['Dan', 'Jordan'];
const allUsers = [ ...oldUsers, ...newUsers ];

console.log(allUsers);
// output: ['Robin', 'Andrew', 'Dan', 'Jordan']
```

现在让我们来看看对象的扩展运算符。它并不是 JavaScript ES6 中的用法。它是针对下一个 JavaScript 版本的提出的92，然而它已经在 React 社区开始使用了。这就是为什么需要在create-react-app 配置中加入了这个功能。

本质上来说，对象的扩展运算符和数组的扩展运算符是一样的，只是用在了对象上。  

```react
const userNames = { firstname: 'Robin', lastname: 'Wieruch' };
const age = 28;
const user = { ...userNames, age };  // { firstname: 'Robin', lastname: 'Wieruch', age: 28 }

console.log(user);
// output: { firstname: 'Robin', lastname: 'Wieruch', age: 28 }
```

本地试验，如果不使用...，则只是对象引用：

```react
const userNames = { firstname: 'Robin', lastname: 'Wieruch' };
const age = 28;
const user = { userNames, age };  // 这里是两个对象，userNames和age，而不是{ firstname: 'Robin', lastname: 'Wieruch', age: 28 }
console.log(user);

userNames.firstname = 'Jason';
console.log(userNames);
console.log(user);
```

类似于之前数组的例子，以下是扩展多个对象的例子。  

```react
const userNames = { firstname: 'Robin', lastname: 'Wieruch' };
const userAge = { age: 28 };
const user = { ...userNames, ...userAge };
console.log(user);
// output: { firstname: 'Robin', lastname: 'Wieruch', age: 28 }
```

最终，它可以用来代替 Object.assign()。  

```react
onDismiss(id) {
    const isNotId = item => item.objectID !== id;
    const updatedHits = this.state.result.hits.filter(isNotId);
    this.setState({
        result: {...this.state.result, hits: updatedHits}
    });
}
```

现在 “Dismiss” 按钮可以再次工作了，因为 onDismiss() 方法已经能够处理这个复杂的 result对象了，并且知道当要忽略掉列表中的某一项时怎么去更新列表了。  

### 条件渲染  

React 应用很早就引入了条件渲染。但本书还没有提到过，因为目前为止还没有合适的用例。条件渲染用于你需要决定渲染哪个元素时。有些时候也可以是渲染一个元素或者什么都不渲染。其实最简单的条件渲染，只需要用 JSX 中的 if-else 就可以实现。

组件内部状态中的 result 对象的初始值为空。当 API 的结果还没返回时，此时的 App 组件没有返回任何元素。这已经是一个条件渲染了，因为在某个特定条件下， render() 方法提前返回了。根据条件， App 组件渲染它的元素或者什么都不渲染。  

现在，让我们更进一步。因为只有 Table 组件的渲染依赖于 result，所以将它包在一个独立的条件渲染中才比较合理。即使 result 为空，其它的所有组件还是应该被渲染。你只需要在 JSX 中加上一个三元运算符就可以达到这样的目的。  

通过这样的方式，即使`fetchSearchTopStories`方法未获得列表，界面仍可显示`Search`对象：

```react
class App extends Component {
    constructor(props) {
        super(props);

        this.state = {
            result: null,
            searchTerm: DEFAULT_QUERY,
        }

        this.setSearchTopStories = this.setSearchTopStories.bind(this);
        this.fetchSearchTopStories = this.fetchSearchTopStories.bind(this);
        this.onSearchChange = this.onSearchChange.bind(this);
        this.onDismiss = this.onDismiss.bind(this);
    }

    setSearchTopStories(result) {
        this.setState({result});
    }

    fetchSearchTopStories(searchTerm) {
        fetch(`${PATH_BASE}${PATH_SEARCH}?${PARAM_SEARCH}${DEFAULT_QUERY}`)
            .then(response => response.json())
            .then(result => this.setSearchTopStories(result))
            .catch(e => e);
    }

    componentDidMount() {
        const {searchTerm} = this.state;
        this.fetchSearchTopStories(searchTerm);
    }

    onDismiss(id) {
        const isNotId = item => item.objectID !== id;
        const updatedHits = this.state.result.hits.filter(isNotId);
        this.setState({
            result: {...this.state.result, hits: updatedHits}
        });
    }

    onSearchChange(event) {
        this.setState({searchTerm: event.target.value});
    }

    render() {
        const {searchTerm, result} = this.state;
        
        return (
            <div className="page">
                <div className="interactions">
                    <Search
                        value={searchTerm}
                        onChange={this.onSearchChange}
                    >
                        Search
                    </Search>
                </div>
                {result
                    ? <Table
                        list={result.hits}
                        pattern={searchTerm}
                        onDismiss={this.onDismiss}
                    />
                    : null
                }

            </div>
        );
    }
}
```

这是实现条件渲染的第二种方式。第三种则是运用 && 逻辑运算符。在 JavaScript 中， true && 'Hello World' 的值永远是“Hello World”。而 false && 'Hello World' 的值则永远是false。  

```react
const result = true && 'Hello World';
console.log(result);
// output: Hello World
const result = false && 'Hello World';
console.log(result);
// output: false
```

在 React 中你也可以利用这个运算符。如果条件判断为 true， && 操作符后面的表达式的值将会被输出。如果条件判断为 false， React 将会忽略并跳过后面的表达式。这个操作符可以用来实现 Table 组件的条件渲染，因为它返回一个 Table 组件或者什么都不返回（**应该是返回一个Table组件或返回null**）。

```react
{result && <Table
    list={result.hits}
    pattern={searchTerm}
    onDismiss={this.onDismiss}
    />
}
```

这是 React 中使用条件渲染的一些方式。你可以在条件渲染代码大全95中找到更多的选择，了解不同的条件渲染方式和它们的适用场景。

现在，你应该能够在你的应用中看到获取的数据。并且当数据正在获取时，你也可以看到除了 Table 组件以外的所有东西。一旦请求完成并且数据存入本地状态之后， Table 组件也将被渲染出来。因为 render() 方法再次执行，而且这时条件渲染判定为展示 Table 组件。  

### 客户端或服务端搜索  

























