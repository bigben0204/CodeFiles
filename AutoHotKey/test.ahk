;=========================================说明 开始==========================================
;需要保存.ahk文件为UTF-8-BOM格式，才能在该脚本文件中支持中文

;#: Win
;!: Alt 
;^: Ctrl
;+: Shift
;<: 使用成对按键中左边的那个. 例如 <!a 相当于 !a, 只是使用左边的 Alt 键才可以触发.
;>: 使用成对按键中右边的那个.
;&: 连接两个按钮合并成一个自定义热键
;~: 激发热键时, 不会屏蔽(被操作系统隐藏) 热键中按键原有的功能

;如下多个特性可以合并使用，如c*，也可以增加空格c *。默认终止符为：-()[]{}':;"/\,.?!`n `t
;::abc:: 需要tab,space,enter等终止符来触发热字串，并且这三个键将作为输入
;:o:abc:: 需要tab,space,enter来触发热字串，并且这三个键将不作为输入
;:*:abc:: 不需要额外的按键来触发热字串，直接执行
;:c:abc:: 区分大小写
;:b0:abc:: 不进行擦除热字串
;:?:abc:: 在另一个单词中也可以触发
;:c1:abc:: 不遵循输入的大小写形式. 使用此选项可以让自动替换热字串不区分大小写且阻止它们遵循您实际输入字符的大小写形式。遵循大小写形式的热字串(这是默认状态) 会在您输入的缩写都为大写时产生大写形式的的替换文本. 如果您的输入的首字母为大写, 那么替换的首字母也会为标题格式(大写, 如果首个字符是字母). 您按其他任何大小写形式输入时, 替换会准确按照定义进行发送
;:r:abc:: 发送替换文本的原始文本; 即准确地按照原样发送, 而不把 {Enter} 转换成 Enter
;:x:abc:: [v1.1.28+]: 执行. 取代替换文本, hotstring 接受一个命令或表达式来执行. 例如, :X:~mb::MsgBox 会在用户输入 "~mb" 时显示一个消息框, 而不是用 "MsgBox" 自动替换. 在定义大量热字串调用函数时, 这是最有用的, 否则每一个热字串就需要三行.
;:z:11:: 这个非常少用的选项会在每次热字串触发后重置热字串识别器. 换句话说, 脚本将开始等待全新的热字串, 而不考虑您之前输入的任何内容
;=========================================说明 结束==========================================

;自动执行段，脚本加载完成后，会自动从头运行脚本，直到遇到第一个return/exit
;gosub Label1  ; 代码运行时, 第一个对话框内容为 "Label1".
;
;Label1:
;MsgBox %A_ThisLabel%  ; 第二个对话框内容为空.
;return

;~ ; 示例 #1: 当不想要的窗口出现时关闭它们:
;~ #Persistent
;~ SetTimer, CloseMailWarnings, 2000
;~ return

;~ CloseMailWarnings:
;~ WinClose, Microsoft Outlook, A timeout occured while communicating
;~ WinClose, Microsoft Outlook, A connection to the server could not be established
;~ WinClose ahk_class Notepad
;~ WinClose abc.txt - 记事本
;~ return

;~ ; 示例 #2: 等待特定的窗口出现, 然后通知用户:
;~ #Persistent
;~ SetTimer, Alert1, 3000
;~ return

;~ Alert1:
;~ ;IfWinNotExist, Video Conversion, Process Complete
;~ SetTitleMatchMode Slow
;~ IfWinNotExist, ahk_class Notepad, abc ;需要指定SetTitleMatchMode Slow才能检测到Notepad中的Wintext abc，Window Spy 中会显示哪些部分的窗口文本(如果有) 需要慢速模式才能匹配到.
    ;~ return
;~ ; Otherwise:
;~ SetTimer, Alert1, Off  ; 即此处计时器关闭自己.
;~ SplashTextOn, , , The video conversion is finished.
;~ Sleep, 3000
;~ SplashTextOff
;~ return

;~ #Persistent  ; 让脚本持续运行, 直到用户退出.
;~ Menu, Tray, Add  ; 创建分隔线.
;~ Menu, Tray, Add, Item1, MenuHandler  ; 创建新菜单项.
;~ return

;~ MenuHandler:
;~ MsgBox You selected %A_ThisMenuItem% from menu %A_ThisMenu%.
;~ return

;~ ; 添加一些菜单项来创建弹出菜单.
;~ Menu, MyMenu, Add, Item1, MenuHandler
;~ Menu, MyMenu, Add, Item2, MenuHandler
;~ Menu, MyMenu, Add  ; 添加分隔线.

;~ ; 添加子菜单到上面的菜单中.
;~ Menu, Submenu1, Add, Item1, MenuHandler
;~ Menu, Submenu1, Add, Item2, MenuHandler

;~ ; 创建第一个菜单的子菜单(右箭头指示符). 当用户选择它时会显示第二个菜单.
;~ Menu, MyMenu, Add, My Submenu, :Submenu1

;~ Menu, MyMenu, Add  ; 在子菜单下添加分隔线.
;~ Menu, MyMenu, Add, Item3, MenuHandler  ; 在子菜单下添加另一个菜单项.
;~ return  ; 脚本的自动运行段结束.

;~ MenuHandler:
;~ MsgBox You selected %A_ThisMenuItem% from the menu %A_ThisMenu%.
;~ return

;~ #z::Menu, MyMenu, Show  ; 即按下 Win-Z 热键来显示菜单.

;~ ; 这是个添加图标到其菜单项的可运行脚本:
;~ Menu, FileMenu, Add, Script Icon, MenuHandler
;~ Menu, FileMenu, Add, Suspend Icon, MenuHandler
;~ Menu, FileMenu, Add, Pause Icon, MenuHandler
;~ Menu, FileMenu, Icon, Script Icon, %A_AhkPath%, 2 ; 使用文件中的第二个图标组
;~ Menu, FileMenu, Icon, Suspend Icon, %A_AhkPath%, -206 ; 使用资源标识符 206 表示的图标
;~ Menu, FileMenu, Icon, Pause Icon, %A_AhkPath%, -207 ; 使用资源表示符 207 表示的图标
;~ Menu, MyMenuBar, Add, &File, :FileMenu
;~ Gui, Menu, MyMenuBar
;~ Gui, Add, Button, gExit, Exit This Example
;~ Gui, Show
;~ return

;~ MenuHandler:
;~ return

;~ Exit:
;~ ExitApp

;~ #Persistent
;~ #SingleInstance
;~ Menu, Tray, Add ; 分隔符
;~ Menu, Tray, Add, TestToggle&Check
;~ Menu, Tray, Add, TestToggleEnable
;~ Menu, Tray, Add, TestDefault
;~ Menu, Tray, Add, TestStandard
;~ Menu, Tray, Add, TestDelete
;~ Menu, Tray, Add, TestDeleteAll
;~ Menu, Tray, Add, TestRename
;~ Menu, Tray, Add, Test
;~ return

;~ ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;~ TestToggle&Check:
;~ Menu, Tray, ToggleCheck, TestToggle&Check
;~ Menu, Tray, Enable, TestToggleEnable ; 同时启用了下一行的测试, 因为它不能撤销自己的禁用状态.
;~ Menu, Tray, Add, TestDelete ; 类似于上面.
;~ return

;~ TestToggleEnable:
;~ Menu, Tray, ToggleEnable, TestToggleEnable
;~ return

;~ TestDefault:
;~ if (Default = "TestDefault")
;~ {
    ;~ Menu, Tray, NoDefault
    ;~ Default := ""
;~ }
;~ else
;~ {
    ;~ Menu, Tray, Default, TestDefault
    ;~ Default := "TestDefault"
;~ }
;~ return

;~ TestStandard:
;~ if (Standard <> false)
;~ {
    ;~ Menu, Tray, NoStandard
    ;~ Standard := false
;~ }
;~ else
;~ {
    ;~ Menu, Tray, Standard
    ;~ Standard := true
;~ }
;~ return

;~ TestDelete:
;~ Menu, Tray, Delete, TestDelete
;~ return

;~ TestDeleteAll:
;~ Menu, Tray, DeleteAll
;~ return

;~ TestRename:
;~ if (NewName <> "renamed")
;~ {
    ;~ OldName := "TestRename"
    ;~ NewName := "renamed"
;~ }
;~ else
;~ {
    ;~ OldName := "renamed"
    ;~ NewName := "TestRename"
;~ }
;~ Menu, Tray, Rename, %OldName%, %NewName%
;~ return

;~ Test:
;~ MsgBox, You selected "%A_ThisMenuItem%" in menu "%A_ThisMenu%".
;~ returnR

;重新加载此文件
#+r::
{
    SendInput ^s
    Reload % A_ScriptFullPath ;A_ScriptFullPath当前脚本的全路径
    return
}

#+s::
{
    Suspend
    return
}

;~ ^j::
;~ {
    ;~ Send, My First Script
    ;~ return
;~ }

;~ ^j::
;~ {
    ;~ MsgBox Wow!
    ;~ MsgBox this is
    ;~ Run, Notepad.exe
    ;~ Winactivate, 无标题 - 记事本  ; 无标题 - 记事本
    ;~ WinWaitActive, 无标题 - 记事本  ; 无标题 - 记事本
    ;~ Send, 7 lines{!}{enter}
    ;~ SendInput, inside the ctrl{+}j hotkey
    ;~ Return
;~ }

;ftw按完需要按space、tab或enter，会把这三个按键作为输入显示出来
::ftw::Free the whales

;:o:效果同::，但是不会把这三个按键作为输入
:o:good::good luck

;打完hello，自动出来后面热字串
:*:hello::Hello, world

;esc::
;{
;    MsgBox Escape!!!!
;    Return
;}

;~ ::btw::
;~ {
    ;myVar := "Hello, world" ;:=为表达式，=为传统语法，如MyVar = This is text.
    ;MsgBox % "The value is " myVar
    ;MsgBox % "The value is " . myVar
    ;MsgBox % Format("You are using AutoHotkey v{1} {2}-bit.", myVar, myVar)
    ;MsgBox % Format("You are using AutoHotkey v{} {}-bit.", myVar, myVar)
    ;MsgBox You typed "btw".
    ;MsgBox, := This would be an assignment without the comma. ;此处的MsgBox后的逗号不能省略，否则被处理为赋值
    ;MsgBox,, Second, Third ;第一个参数被省略时，逗号也不能被省略
    
    ;OldStr := "a bc d e"
    ;StringReplace, NewStr, OldStr, %A_Space%, +, All
    ;MsgBox % NewStr
    
/*     var1 := "abc"
    var2 := "ABC"
    if (var1 = var2) ;不区分大小写
    {
        MsgBox, var1 = var2
    }
    else
    {
        MsgBox, var1 != var2
    }

    if (var1 == var2) ;区分大小写
    {
        MsgBox, var1 == var2
    }
    else
    {
        MsgBox, var1 !== var2
    } 
    */
    
/*     var := 1.1
    if var is float
        MsgBox, %var% is a floating point number.
    else if var is integer
        MsgBox, %var% is an integer.
    if var is time
        MsgBox, %var% is also a valid date-time.
 */   

/*     
    ; 示例 #1:
    Loop Files, %A_ProgramFiles%\*.txt, R  ; 递归子文件夹.
    {
        MsgBox, 4, , Filename = %A_LoopFileFullPath%`n`nContinue?
        IfMsgBox, No
            break
    }
  */   
/*     
    ; 示例 #2: 计算文件夹的大小, 包括其所有子文件夹中的文件:
    SetBatchLines, -1  ; 让操作以最快速度进行.
    FolderSizeKB = 0
    FileSelectFolder, WhichFolder  ; 让用户选取文件夹.
    Loop, Files, %WhichFolder%\*.*, R
        FolderSizeKB += %A_LoopFileSizeKB%
    MsgBox Size of %WhichFolder% is %FolderSizeKB% KB.
 */

    ;~ MsgBox % SubStr(37*12, 1, 2) ;带%才可以正常计算
    ;~ MsgBox % SubStr(A_Now, 7, 2)
    ;~ MsgBox %A_Hour% %A_Now%
    
    ;~ InputBox, OutputVar, Question 1, What is your first name?
    ;~ if (OutputVar="Bill")
       ;~ MsgBox, That's an awesome name`, %OutputVar%.
    
    ;~ InputBox, OutputVar2, Question 2, Do you like AutoHotkey?
    ;~ if (OutputVar2 = "yes")
       ;~ MsgBox, Thank you for answering %OutputVar2%`, %OutputVar%! We will become great friends.
    ;~ else
       ;~ MsgBox, %OutputVar%`, That makes me sad.
    
    ;~ IniFile := "D:\abc.txt"
    ;~ if FileExist(IniFile)
    ;~ {
        ;~ MsgBox % IniFile " exists"
    ;~ }
    ;~ else
    ;~ {
        ;~ MsgBox % IniFile " not exists"
    ;~ }
    ;~ MsgBox, 4,, Would you like to continue?
    ;~ IfMsgBox, No
        ;~ return  ; 如果选择 No, 脚本将会终止.
    ;~ MsgBox You pressed YES.  ; 否则, 用户选择了YES.
    
    ;~ Var = Text  ; 赋值一些文本给一个变量(传统的).
    ;~ Number := 6  ; 赋值一个数字给一个变量(表达式).
    ;~ Var2 = %Var%  ; 赋值一个变量给另一个(传统的).
    ;~ Var3 := Var  ; 赋值一个变量给另一个(表达式).
    ;~ Var4 .= Var  ; 追加一个变量到另一个的末尾(表达式).
    ;~ Var5 += Number  ; 将变量的值与另一个相加(表达式).
    ;~ Var5 -= Number  ; 将变量的值减去另一个(表达式).
    ;~ Var6 := SubStr(Var, 2, 2)  ; 变量在函数中. 这总是一个表达式.
    ;~ Var7 = %Var% Text  ; 赋值一个变量给另一个变量并带有一些额外的文本(传统的).
    ;~ Var8 := Var " Text"  ; 赋值一个变量给另一个变量并带有一些额外的文本(表达式).
    ;~ MsgBox, %Var%  ; 变量在命令中.
    ;~ StringSplit, Var, Var, x  ; 在命令中的变量, 但是它们作为输入或输出变量.
    ;~ if (Number = 6)  ; 只要 IF 有括号, 它就是一个表达式, 所以不需要百分号.
    ;~ if (Var != Number)  ; 只要 IF 有括号, 它就是一个表达式, 所以不需要百分号.
    ;~ if Number = 6  ; 如果没有括号, 那么 IF 是传统的. 不过, 只有赋值语句"右边"的变量需要百分号. 
    ;~ if Var1 < %Var2%  ; 如果没有括号, 那么 IF 是传统的. 不过, 只有赋值语句"右边"的变量需要百分号. 
    
    ;~ MyObject := ["a", "b", "c"]
    ;~ MsgBox % "Length is " MyObject.Length()
    ;~ MsgBox % "Count is " MyObject.Count()
    ;~ MsgBox % MyObject[1]
    ;~ RemovedValue := MyObject.RemoveAt(1)
    ;~ MsgBox % RemovedValue
    
    ;~ Banana := {"Color": "Yellow", "Taste": "Delicious", "Price": 3}
    ;~ ;Banana.Pop() ;调用该方法无效
    ;~ MsgBox % "Count is " Banana.Count() ;3
    ;~ MsgBox % "Length is " Banana.Length() ;调用返回0
    
    ;~ MsgBox, %A_AhkVersion%
    
    ;~ car := "old"
    ;~ if (car="old")
    ;~ {
        ;~ MsgBox, The car is really old
        ;~ if (wheels = "flat")
        ;~ {
            ;~ MsgBox, This car is not safe to Drive.
            ;~ return
        ;~ }
        ;~ else
        ;~ {
            ;~ MsgBox, Be careful! This old car will be dangerous to Drive.
        ;~ }
    ;~ }
    ;~ else
    ;~ {
        ;~ MsgBox, My`, what a shiny new vehicle you have there.
    ;~ }
    
    ;~ #NoEnv ;新脚本建议都加上#NoEnv指令以提高性能
    ;~ MsgBox %WinDir% ;增加了#NoEnv之后，不检查空变量是否为环境变量，所以这里显示空
    ;~ MsgBox %A_WinDir% ;这里A_WinDir是环境变量，所以这里显示C:\Windows

    ;~ Run, cmd.exe
    ;~ Sleep, 1000
    ;~ SetTitleMatchMode, 2
    ;~ ControlSend, , abc, cmd.exe
    
    ;~ if (not WinExist("ahk_class Notepad"))
    ;~ {
        ;~ Run Notepad
        ;~ WinWait ahk_class Notepad
    ;~ }
    ;~ else
        ;~ WinActivate ahk_class Notepad
    ;~ Send Hello`, world!
    
    ;~ i = 10
    ;~ modifyValue()
    ;~ MsgBox % i
    
    ;~ Return
;~ }

;~ ;增加ByRef是按引用传递，调用处的i值将修改
;~ modifyValue(ByRef i) 
;~ {
    ;~ i = 3
;~ }

;你可以通过在两个按键(除手柄键) 之间, 使用  &  来定义一个组合热键. 在下面的例子中, 你要按下Numpad0, 再按下Numpad1 或 Numpad2, 才能触发热键:
;~ Numpad0 & Numpad1::
;~ {
    ;~ MsgBox You pressed Numpad1 while holding down Numpad0.
    ;~ Return
;~ }

;~ Numpad0 & Numpad2::
;~ {
    ;~ Run Notepad
    ;~ Return
;~ }

;~ #z::Run https://www.baidu.com  ; Win+Z

^!n::  ; Ctrl+Alt+n
if WinExist("无标题 - 记事本")
    WinActivate
else
    Run Notepad
return

;CoordMode, Mouse, Screen
;~LButton::
;{
;    MouseGetPos, begin_x, begin_y
;    while GetKeyState("LButton")
;    {
;        MouseGetPos, x, y
;        ToolTip, % begin_x ", " begin_y "`n" Abs(begin_x-x) " x " Abs(begin_y-y)
;        Sleep, 10
;    }
;    ToolTip
;    return
;}

#IfWinActive ahk_class Notepad
^!c::MsgBox You pressed Control+Alt+C in Notepad.
#IfWinActive ahk_class WordPadClass
^!c::MsgBox You pressed Control+Alt+C in WordPad.
#IfWinActive
^!c::MsgBox You pressed Control+Alt+C in a window other than Notepad/WordPad.

;#IfWinActive/Exist 会设置最近找到的窗口(但不包括 #IfWinNotActive/NotExist). 例如:
#IfWinExist ahk_class Notepad
#n::WinActivate  ; 激活由 #IfWin 找到的窗口.

;~ #If MouseIsOver("ahk_class Shell_TrayWnd") ;没找到此函数
;~ WheelUp::Send {Volume_Up}     ; 在任务栏上滚动滚轮:增加/减小音量.
;~ WheelDown::Send {Volume_Down} ;

;~ #IfWinActive 无标题 - 记事本  ; 无标题 - 记事本
;~ #space::
;~ MsgBox You pressed Win+Spacebar in Notepad.
;~ Return

;;#if 像是宏的用法
;#if WinActive("无标题 - 记事本")
;#space::
;MsgBox You pressed Win+Spacebar in Notepad.
;Return

;~ ; 无标题 - 记事本
;~ #IfWinActive 无标题 - 记事本
;~ !q::
;~ MsgBox, You pressed Alt+Q in Notepad.
;~ Return

;~ ; 任何不是无标题 - 记事本的窗口
;~ #IfWinActive
;~ !q::
;~ MsgBox, You pressed Alt+Q in any window.
;~ Return

;~ ; 记事本
;~ #IfWinActive ahk_class Notepad
;~ #space::
;~ MsgBox, You pressed Win+Spacebar in Notepad.
;~ Return
;~ ::msg::You typed msg in Notepad

;~ ; 画图
;~ #IfWinActive 无标题 - 画图  ; 无标题 - 画图
;~ #space::
;~ MsgBox, You pressed Win+Spacebar in MSPaint!
;~ Return
;~ ::msg::You typed msg in MSPaint!

;~ #i::
;~ Run, http://www.google.com/
;~ Return

;~ ^p::
;~ Run, notepad.exe
;~ Return

;~ ~j::
;~ Send, ack
;~ Return
;~ ~RButton::MsgBox You clicked the right mouse button.
;~ ~RButton & C::MsgBox You pressed C while holding down the right mouse button.

;~ :*:acheiv::achiev
;~ ::achievment::achievement
;~ ::acquaintence::acquaintance
;~ :*:adquir::acquir
;~ ::aquisition::acquisition
;~ :*:agravat::aggravat
;~ :*:allign::align
;~ ::ameria::America

;~ ^b::  ; Ctrl+B 热键
;~ Send, {ctrl down}c{ctrl up}  ; 复制选定的文本. 也可以使用 ^c, 但这种方法更加可靠.
;~ SendInput, [b]{ctrl down}v{ctrl up}[/b] ; 粘贴所复制的文本, 并在文本前后加上加粗标签.
;~ Return  ; 热键内容结束, 这之后的内容将不会触发.

;~ ; 当你创建热键时...
;~ ; 错误的
;~ {LCtrl}::
;~ Send, AutoHotkey
;~ Return

;~ ; 正确的
;~ LCtrl::
;~ Send, AutoHotkey
;~ Return

;~ ; 下面这个例子表示按下一个键的时候再按下另一个键(或多个键).
;~ ; 如果其中一个方法不奏效, 试试另一个.
;~ ^j::
;~ Send, ^s                     ; 都表示发送 CTRL+s 键击
;~ Send, {ctrl down}s{ctrl up}  ; 都表示发送 CTRL+s 键击
;~ Send, {ctrl down}c{ctrl up}
;~ Send, {b down}{b up}
;~ Send, {Tab down}{Tab up}
;~ Send, {Up down}  ; 按下向上键.
;~ Sleep, 1000      ; 保持1秒.
;~ Send, {Up up}    ; 然后松开向上键.
;~ Send,
;~ (
;~ Line 1
;~ Line 2
;~ Apples are a fruit.
;~ )
;~ return

;~ a. 什么时候使用百分号
;~ 关于变量一个最常见的问题是什么时候使用百分号(%). 希望下面这些内容能够消除一些困惑.

;~ 什么时候要使用百分号:

;~ 当你使用命令时(见前文), 参数是输出变量 OutputVar 或输入变量 InputVar 时除外.
;~ 当你使用传统模式(等号前面没有其它符号) 给一个变量赋值时.
;~ 什么时候不要使用百分号:

;~ 参数中的输入或输出变量. 例如: StringLen, OutputVar, InputVar
;~ 赋值时左边的变量: 例如: Var = 123abc
;~ 在传统 If 语句(不包括表达式) 中, 中左边的变量: If Var1 < %Var2%
;~ 在表达式中的变量, 例如:
;~ If (Var1 != Var2)
  ;~ Var1 := Var2 + 100
  
;~ 方括号语法
;~ MyObject := ["one", "two", "three", 17]
;~ 这将从有时被称为"索引数组"的内容开始. 索引数组是一个表示项目列表的对象, 索引号从 1 开始连续递增. 在本例中, 值 "one" 存储在对象键 1(又叫做索引号1), 值 17 存储在对象键 4(又叫做索引号 4).

;~ 大括号语法
;~ Banana := {"Color": "Yellow", "Taste": "Delicious", "Price": 3}
;~ 这将通过定义有时被称为"关联数组"来开始. 关联数组是数据的集合, 其中每个条目都有自己的名称. 在这个例子中, 值 "Yellow" 存储在对象键 "Color" 中. 同样的, 值 3 存储在对象键 "Price" 中.

;~ 数组函数
;~ MyObject := Array("one", "two", "three", 17)
;~ 这种方式跟方括号语法形式一样, 区别仅仅是采用了函数的形式.

;~ 对象函数
;~ Banana := Object("Color", "Yellow", "Taste", "Delicious", "Price", 3)
;~ 这种方式跟大括号语法形式一样, 区别仅仅是采用了函数的形式.

;~ 请注意, 所有这些方法都创建了同一样东西(也就是对象), 区别仅仅是对象的键不一样.

;~ 方括号表示法
;~ Banana["Pickled"] := True ; 这个香蕉烂透了. 呃...
;~ 在对象中设置值跟设置变量的值一样简单. 你需要做的是把方括号所代表的值在表达式赋值运算符 := 的左边.

;~ 句点表示法
;~ Banana.Consistency := "Mushy"
;~ 和上面一样, 但是用点(.) 符号.

;~ ^!r Up::MsgBox You pressed and released Ctrl+Alt+R

; 函数热键
;[v1.1.28+]: 热字串也可以这样定义. 可以将多个热键或热字串组合在一起, 以调用相同的函数.
;在 热键/热字串 标签或标签和函数之间仅允许空白, 注释或者指令. 以这种方式的热键/热字串标签对 IsLabel, Gosub 或其他命令是不可见的; 然而, 即使关联了函数, 自动执行段也会以第一个热键/热字串结束.
;使用函数的的主要好处是可以使用局部变量, 当两个或多个热键为了不同的目的使用相同的变量名时, 避免了冲突. 它也鼓励自我记录热键, 就像上面的代码在函数内描述热键.
; Ctrl+Shift+O 在资源管理器中打开包含文件夹.
; Ctrl+Shift+E 打开文件夹并选中当前编辑文件.
; 支持 SciTE 和 Notepad++.
;~ ^+o::
;~ ^+e::
    ;~ editor_open_folder() {
        ;~ WinGetTitle, path, A
        ;~ if RegExMatch(path, "\*?\K(.*)\\[^\\]+(?= [-*] )", path)
            ;~ if (FileExist(path) && A_ThisHotkey = "^+e")
                ;~ Run explorer.exe /select`,"%path%"
            ;~ else
                ;~ Run explorer.exe "%path1%"
    ;~ }

;~ ;按住左Ctrl，滚动鼠标滚轮来进行横向滚动条
;~ ~LControl & WheelUp::  ; 向左滚动.
;~ ControlGetFocus, fcontrol, A
;~ Loop 20  ; <-- 增加这个值来加快滚动速度.
    ;~ SendMessage, 0x114, 0, 0, %fcontrol%, A  ; 0x114 是 WM_HSCROLL, 它后面的 0 是 SB_LINELEFT.
;~ return

;~ ~LControl & WheelDown::  ; 向右滚动.
;~ ControlGetFocus, fcontrol, A
;~ Loop 20  ; <-- 增加这个值来加快滚动速度.
    ;~ SendMessage, 0x114, 1, 0, %fcontrol%, A  ; 0x114 是 WM_HSCROLL, 它后面的 1 是 SB_LINERIGHT.
;~ return


;~ ~RButton & LButton::MsgBox You pressed the left mouse button while holding down the right.
;~ RButton & WheelUp::MsgBox You turned the mouse wheel up while holding down the right button.

;~ ^!s::Send {Delete}
;~ ;按下 Control+Alt+S 会让系统以为您按下了 Control+Alt+Delete(由于系统对 Control+Alt+Delete 的侵略性检测). 要变通解决此问题, 请使用 KeyWait 来等待按键释放; 例如:
;~ ^!s::
;~ KeyWait Control
;~ KeyWait Alt
;~ Send {Delete}
;~ return

;~ 如何在不退出脚本的情况下停止重复的动作?
;~ #MaxThreadsPerHotkey 3
;~ #z::  ; Win+Z 热键(可根据您的喜好改变此热键).
;~ #MaxThreadsPerHotkey 1
;~ if KeepWinZRunning  ;  这说明一个潜在的线程正在下面的循环中运行.
;~ {
    ;~ KeepWinZRunning := false  ; 向那个线程的循环发出停止的信号.
    ;~ return  ; 结束此线程, 这样才可以让下面的线程恢复并得知上一行所做的更改.
;~ }
;~ ; 否则:
;~ KeepWinZRunning := true
;~ Loop
;~ {
    ;~ ; 以下四行是您要重复的动作(可根据您的需要修改它们):
    ;~ ToolTip, Press Win-Z again to stop this from flashing.
    ;~ Sleep 1000
    ;~ ToolTip
    ;~ Sleep 1000
    ;~ ; 但请不要修改下面剩下的内容.
    ;~ if not KeepWinZRunning  ; 用户再次按下 Win-Z 来向循环发出停止的信号.
        ;~ break  ; 跳出此循环.
;~ }
;~ KeepWinZRunning := false  ; 复位, 为下一次使用热键做准备.
;~ return

;~ Alt-Tab 热键
;~ 每个 Alt-Tab 热键必须是两个键的组合, 通常通过和符号(&) 实现. 在下面的例子中, 按住右 Alt 键并按下 J 或 K 可以导航 alt-tab 菜单:
;~ RAlt & j::AltTab
;~ RAlt & k::ShiftAltTab

;~ MButton::AltTabMenu
;~ WheelDown::AltTab
;~ WheelUp::ShiftAltTab

;~ LCtrl & CapsLock::AltTab
;~ !MButton::  ; 鼠标中键. ! 前缀让它在按住 Alt 键时激发(当 alt-tab 菜单可见时, alt 键是按住的).
;~ IfWinExist ahk_class #32771  ; 表示 alt-tab 菜单出现在屏幕上.
    ;~ Send !{Escape}{Alt up}
;~ return

;~ :*:]d::  ; 此热字串通过后面的命令把 "]d" 替换成当前日期和时间.
;~ FormatTime, CurrentDateTime,, M/d/yyyy h:mm:ss tt  ; 看起来会像 9/1/2005 3:53 PM 这样
;~ SendInput %CurrentDateTime%
;~ return

;~ :?c*:ben@::dingben@huawei.com ;匹配大小写，在单词中也触发
;~ :*b0:<em>::</em>{left 5} ;不进行自动擦除
;~ :X:~mb::MsgBox
;~ :zb0*?:11:: ;如果选项为b0*?将会出现11xx1xx1xx的场景，增加选项z后将会避免
;~ SendInput xx
;~ return

;~ ::text1::
;~ (
;~ Any text between the top and bottom parentheses is treated literally, including commas and percent signs.
;~ By default, the hard carriage return (Enter) between the previous line and this one is also preserved.
    ;~ By default, the indentation (tab) to the left of this line is preserved.

;~ See continuation section for how to change these default behaviors.
;~ )

;~ #Hotstring NoMouse ;屏蔽鼠标引起的光标移动
;要在替换文本后发送额外的空格或 tab, 可以把它们加在替换文本后, 但需要在末尾加上重音符/反引号(`)
;~ :*:eee::By the way `

;~ 尽管在自动替换文本(在没有使用原始选项时) 中支持 Send 命令的特殊字符例如 {Enter}, 但热字串自身却不使用这种方式. 而是使用 `n 表示 Enter 键, `t(或原义的 tab) 表示 Tab(请参阅转义序列了解完整的列表). 例如, 当您输入"ab"后跟着 tab 时会触发热字串 :*:ab`t::
;~ :*:ab`t::abc

;~ 为了识别热字串, 您输入的所有退格都会被计算进来. 然而, 使用↑, →, ↓, ←, PageUp, PageDown, Home 和 End 在编辑器中导航会重置热字串识别过程. 换句话说, 它会开始等待全新的热字串.

;~ 函数热字串 [v1.1.28+]
;~ 紧跟在热字串标签后面的函数, 通过简单的定义, 一个或多个热字串可以关联到此函数, 如下例所示:

;~ ; 这个示例还演示了在脚本中实现案例一致性的一种方法.
;~ :C:BTW::  ; 输入所有大写字母.
;~ :C:Btw::  ; 只有第一个字母输入大写字母.
;~ : :btw::  ; 输入任何其他组合.
    ;~ case_conform_btw() {
        ;~ hs := A_ThisHotkey  ; 为了方便, 以防被打断.
        ;~ if (hs == ":C:BTW")
            ;~ Send BY THE WAY
        ;~ else if (hs == ":C:Btw")
            ;~ Send By the way
        ;~ else
            ;~ Send by the way
    ;~ }

;~ #h::  ; Win+H 热键
;~ ; 获取当前选择的文本. 使用剪贴板代替
;~ ; "ControlGet Selected" 是因为它可以工作于更大范围的编辑器
;~ ; (即文字处理软件).  保存剪贴板当前的内容
;~ ; 以便在后面恢复. 尽管这里只能处理纯文本,
;~ ; 但总比没有好:
;~ ClipboardOld := Clipboard
;~ Clipboard := "" ; 必须清空, 才能检测是否有效.
;~ Send ^c
;~ ClipWait 1
;~ if ErrorLevel  ; ClipWait 超时.
    ;~ return
;~ ; 替换 CRLF 和/或 LF 为 `n 以便用于 "发送原始模式的" 热字串:
;~ ; 对其他任何在原始模式下可能出现问题
;~ ; 的字符进行相同的处理:
;~ ClipContent := StrReplace(Clipboard, "``", "````")  ; 首先进行此替换以避免和后面的操作冲突.
;~ ClipContent := StrReplace(ClipContent, "`r`n", "``r")  ; 在 MS Word 等软件中中使用 `r 会比 `n 工作的更好.
;~ ClipContent := StrReplace(ClipContent, "`n", "``r")
;~ ClipContent := StrReplace(ClipContent, "`t", "``t")
;~ ClipContent := StrReplace(ClipContent, "`;", "```;")
;~ Clipboard := ClipboardOld  ; 恢复剪贴板之前的内容.
;~ ShowInputBox(":T:`::" ClipContent)
;~ return

;~ ShowInputBox(DefaultValue)
;~ {
    ;~ ; 这里会移动 InputBox 的光标到更人性化的位置:
    ;~ SetTimer, MoveCaret, 10 ;设置一个定时器，等待出现一个标题为New Hotstring的窗口
    ;~ ; 显示 InputBox, 提供默认的热字串:
    ;~ InputBox, UserInput, New Hotstring,
    ;~ (
    ;~ Type your abreviation at the indicated insertion point. You can also edit the replacement text if you wish.

    ;~ Example entry: :R:btw`::by the way
    ;~ ),,,,,,,, %DefaultValue%
    ;~ if ErrorLevel  ; 用户选择了取消.
        ;~ return

    ;~ ;用正则表达式来给Hotstring对象设置参数
    ;~ if RegExMatch(UserInput, "O)(?P<Label>:.*?:(?P<Abbreviation>.*?))::(?P<Replacement>.*)", Hotstring)
    ;~ {
        ;~ if !Hotstring.Abbreviation
            ;~ MsgText := "You didn't provide an abbreviation"
        ;~ else if !Hotstring.Replacement
            ;~ MsgText := "You didn't provide a replacement"
        ;~ else
        ;~ {
            ;~ Hotstring(Hotstring.Label, Hotstring.Replacement)  ; 现在激活热字串.
            ;~ FileAppend, `n%UserInput%, %A_ScriptFullPath%  ; 保存热字串以备以后使用.
        ;~ }
    ;~ }
    ;~ else
        ;~ MsgText := "The hotstring appears to be improperly formatted"

    ;~ if MsgText
    ;~ {
        ;~ MsgBox, 4,, %MsgText%. Would you like to try again?
        ;~ IfMsgBox, Yes
            ;~ ShowInputBox(DefaultValue)
    ;~ }
    ;~ return
    
    ;~ MoveCaret:
    ;~ WinWait, New Hotstring ;如果出现New Hotstring的字符串，则移动光标
    ;~ ; 否则移动 InputBox 中的光标到用户输入缩写的位置.
    ;~ Send {Home}{Right 3}
    ;~ SetTimer,, Off
    ;~ return
;~ }

;~ #h::  ; Win+H 热键
    ;~ ; 获取当前选择的文本. 使用剪贴板代替
    ;~ ; "ControlGet Selected", 是因为它可以工作于更大范围的编辑器
    ;~ ; (即文字处理软件). 保存剪贴板当前的内容
    ;~ ; 以便在后面恢复. 尽管这里只能处理纯文本,
    ;~ ; 但总比没有好:
    ;~ AutoTrim Off  ; 保留剪贴板中任何前导和尾随空白字符.
    ;~ ClipboardOld = %ClipboardAll%
    ;~ Clipboard =  ; 必须清空, 才能检测是否有效.
    ;~ Send ^c
    ;~ ClipWait 1
    ;~ if ErrorLevel  ; ClipWait 超时.
        ;~ return
    ;~ ; 替换 CRLF 和/或 LF 为 `n 以便用于 "发送原始模式的" 热字串:
    ;~ ; 对其他任何在原始模式下可能出现问题
    ;~ ; 的字符进行相同的处理:
    ;~ StringReplace, Hotstring, Clipboard, ``, ````, All  ; 首先进行此替换以避免和后面的操作冲突.
    ;~ StringReplace, Hotstring, Hotstring, `r`n, ``r, All  ; 在 MS Word 等软件中中使用 `r 会比 `n 工作的更好.
    ;~ StringReplace, Hotstring, Hotstring, `n, ``r, All
    ;~ StringReplace, Hotstring, Hotstring, %A_Tab%, ``t, All
    ;~ StringReplace, Hotstring, Hotstring, `;, ```;, All
    ;~ Clipboard = %ClipboardOld%  ; 恢复剪贴板之前的内容.
    ;~ ; 这里会移动 InputBox 的光标到更人性化的位置:
    ;~ SetTimer, MoveCaret, 10
    ;~ ; 显示 InputBox, 提供默认的热字串:
    ;~ InputBox, Hotstring, New Hotstring, Type your abreviation at the indicated Insertion point. You can also edit the replacement text if you wish.`n`nExample entry: :R:btw`::by the way,,,,,,,, :R:`::%Hotstring%
    ;~ if ErrorLevel  ; 用户选择了取消.
        ;~ return
    ;~ IfInString, Hotstring, :R`:::
    ;~ {
        ;~ MsgBox You didn't provide an abbreviation. The hotstring has not been added.
        ;~ return
    ;~ }
    ;~ ; 否则添加热字串并重新加载脚本:
    ;~ FileAppend, `n%Hotstring%, %A_ScriptFullPath%  ; 在开始处放置 `n 以防文件末尾没有空行.
    ;~ Reload
    ;~ Sleep 200 ; 如果加载成功, Reload 会在 Sleep 期间关闭当前实例, 所以永远不会执行到下面的语句.
    ;~ MsgBox, 4,, The hotstring just added appears to be improperly formatted. Would you like to open the script for editing? Note that the bad hotstring is at the bottom of the script.
    ;~ IfMsgBox, Yes, Edit
        ;~ return

;~ MoveCaret:
    ;~ IfWinNotActive, New Hotstring
        ;~ return
    ;~ ; 否则移动 InputBox 中的光标到用户输入缩写的位置.
    ;~ Send {Home}{Right 3}
    ;~ SetTimer, MoveCaret, Off
    ;~ return

;~ 重映射按键 限制: 下面描述的 AutoHotkey 重映射功能通常不如直接通过 Windows 注册表进行映射那么直接有效. 对于每种方法的优点和缺点, 请参阅注册表重映射. 
;~ 内置重映射功能的语法为 OriginKey::DestinationKey 
;~ a::b
;~ b::a
;~ a::B

;~ 使用 #IfWinActive/Exist 指令可以让重映射仅在指定的窗口中有效. 例如:
;~ #IfWinActive ahk_class Notepad
;~ a::b  ; 让 'a' 键到 'b' 键的映射仅在记事本中有效.
;~ #IfWinActive  ; 这里让后续的重映射和热键对所有窗口生效.

;使用键盘模拟鼠标移动和点击
;~ *#up::MouseMove, 0, -10, 0, R  ; Win+UpArrow 热键 => 上移光标
;~ *#Down::MouseMove, 0, 10, 0, R  ; Win+DownArrow => 下移光标
;~ *#Left::MouseMove, -10, 0, 0, R  ; Win+LeftArrow => 左移光标
;~ *#Right::MouseMove, 10, 0, 0, R  ; Win+RightArrow => 右移光标

;~ *<#RCtrl::  ; LeftWin + RightControl => Left-click (按住 Control/Shift 来进行 Control-Click 或 Shift-Click).
;~ SendEvent {Blind}{LButton down}
;~ KeyWait RCtrl  ; 防止键盘自动重复导致的重复鼠标点击.
;~ SendEvent {Blind}{LButton up}
;~ return

;~ *<#AppsKey::  ; LeftWin + AppsKey => Right-click
;~ SendEvent {Blind}{RButton down}
;~ KeyWait AppsKey  ; 防止键盘自动重复导致重复的鼠标点击.
;~ SendEvent {Blind}{RButton up}
;~ return

;~ https://wyagd001.github.io/zh-cn/docs/scripts/NumpadMouse.htm 使用键盘模拟鼠标的完整程序

;~ 脚本加载完成后, 它会从顶行开始执行, 直到遇到 Return, Exit, 热键/热字串标签或脚本的底部(无论最先遇到哪个). 脚本的这个顶端部分被称为 自动执行段.

;~ 把过长的行分割成一系列短行
;~ 在(后面增加参数可以修改延续片段的行为,如( LTrim Join| %
;~ LTrim：清除每行开始的空格和tab
;~ Join：指定每行连接方式， 如果指定单词 Join 自身, 则行与行之间直接连接而不添加任何字符. 或者在单词 Join 后可以紧跟着多达 15 个字符. 例如, Join`s 会在除最后一行外的每行末尾插入一个空格("`s"表示原义的空格, 这是一个只能被 Join 识别的特殊转义序列). 另一个例子是 Join`r`n, 它会在行与行之间插入 CR+LF. 同样地, Join| 会在行之间插入管道符. 要让延续片段的最后一行也以连接字符串结尾, 需要在它的闭括号上一行添加一个空行.
;~ RTrim0：保留每行结尾的空格和tab
;~ Comments(或 Comment 或 Com 或 C) [v1.0.45.03+]: 允许在延续片段中使用分号注释(但不支持 /*..*/). 这样的注释(以及它们左边的空格和 tab) 会在连接时完全被忽略而不是当成原义文本处理. 每个注释可以放在一行的右侧或单独一行.
;~ %(百分号): 把百分号视为原义字符而不是变量引用. 这样就不需要把每个百分号转义成原义字符. 在百分号已经为原义的地方不需要使用此选项, 例如, 自动替换热字串.
;~ ,(逗号): 把逗号作为分隔符而不是原义逗号. 这个非常少用的选项只有在命令的参数之间才需要, 因为在函数调用中逗号的类型没有影响. 并且, 此选项只会转换那些真正的分隔参数的逗号. 换句话说, 一旦到了命令的最后一个参数(或者命令没有参数), 那么会忽略此选项而把后续的逗号当成原义逗号.
;~ `(重音符): 把每个反引号视为原义字符而不是转义符. 此选项同时也避免了需要分别对逗号和百分号进行明确地转义. 此外, 它还阻止对任何特定的转义序列例如 `r 和 `t 进行转义.
;~ ) [v1.1.01+]: 如果一个右括号出现在延续代码的参数中(除了作为 Join 的选项参数), 那么这一行将重新解释为一个表达式, 而不是作为一段延续代码的开始. 这将避免类似 (x.y)[z]() 的表达式需要对左括号进行转义.
;~ #j::
;~ ; 示例 #1:
;~ Var =
;~ (
;~ Line 1 of the text.
;~ Line 2 of the text. By default, a linefeed (`n) is present between lines.
;~ )

;~ ; 示例 #2:
;~ FileAppend,  ; 此时逗号是不能缺少的.
;~ (RTrim0
;~ A line of text.     ;this is comment
;~ By default, the hard carriage return (Enter) between the previous line and this one will be written to the file as a linefeed (`n).
    ;~ By default, the tab to the left of this line will also be written to the file (the same is true for spaces).     
;~ By default, variable references such as %Var% are resolved to the variable's contents.`n
;~ ), E:\My File.txt
;~ return

;~ 使用延续片段无法生成总长度超过 16,383 字符的行(如果尝试这么做, 那么程序在启动时会弹出警告). 解决此问题的一种方法是把一系列内容连接到变量中. 例如:

;~ Var =
;~ (
;~ ...
;~ )
;~ Var = %Var%`n  ; 通过另一个延续片段添加更多文本到此变量中.
;~ (
;~ ...
;~ )
;~ FileAppend, %Var%, C:\My File.txt

;~ 因为闭括号表示延续片段的结束, 所以要让某一行以原义的闭括号开头, 需要在其前面加上重音符/反引号: `).
;~ #Warn UseUnsetLocal 

;~ #j::
;~ {
    ;~ abc := "Hello, world"R
    ;~ OutputDebug, %A_Now%: good ;在调试时会发送信息到界面显示，用于调试用，正常运行不会显示
    ;~ ListVars ;用来打印调试
    ;~ Pause
    ;~ MsgBox % abc
    ;~ return
;~ }

;~ var := "abc"
;~ MsgBox % var != ""
;~ 相关提示, 任何无效的表达式例如 (x +* 3) 会产生空字符串.
;~ var := 1.0e4 ;必须带小数点的数字，科学计数法才有用
;~ MsgBox % var + 1000

;~ #j::
;~ {
    ;~ WinGet, currentStatus, MinMax, Calculator
    ;~ MsgBox % currentStatus
    ;~ WinSet, AlwaysOnTop, Toggle, Calculator ; 切换计算器的置顶状态.
    ;~ return
;~ }

;~ #t::  ; 按下 Win+T 来让鼠标光标下的颜色透明.
;~ MouseGetPos, MouseX, MouseY, MouseWin
;~ PixelGetColor, MouseRGB, %MouseX%, %MouseY%, RGB
;~ ; 似乎有必要首先关闭任何现有的透明度:
;~ WinSet, TransColor, Off, ahk_id %MouseWin%
;~ WinSet, TransColor, %MouseRGB% 220, ahk_id %MouseWin%
;~ return

;~ #o::  ; 按下 Win+O 来关闭鼠标下窗口的透明度.
;~ MouseGetPos,,, MouseWin
;~ WinSet, TransColor, Off, ahk_id %MouseWin%
;~ return

;~ #g::  ; 按下 Win+G 来显示鼠标下窗口的当前透明设置..
;~ MouseGetPos,,, MouseWin
;~ WinGet, Transparent, Transparent, ahk_id %MouseWin%
;~ WinGet, TransColor, TransColor, ahk_id %MouseWin%
;~ ToolTip Translucency:`t%Transparent%`nTransColor:`t%TransColor%
;~ return

;~ 示例 #4
;~ 要使整个系统上的所有或选定的菜单都透明, 请保持如下所示的脚本始终运行着. 请注意, 尽管这样的脚本不能使自己的菜单透明, 但它可以使其他脚本的菜单透明:

;~ #Persistent
;~ SetTimer, WatchForMenu, 5
;~ return  ; 自动执行段结束.

;~ WatchForMenu:
;~ DetectHiddenWindows, On  ; 可以让检测菜单更早.
;~ if WinExist("ahk_class #32768")
    ;~ WinSet, Transparent, 150  ; 使用上面一行找到的窗口.
;~ return

;~ 示例 #5
;~ Region 子命令的各种例子:

;~ WinSet, Region, 50-0 W200 H250, ahk_class Notepad  ; 隐藏记事本在这矩形之外的所有部分.
;~ WinSet, Region, 50-0 W200 H250 R40-40, ahk_class Notepad  ; 与上面相同, 不过带有 40x40 的圆角.
;~ WinSet, Region, 50-0 W200 H250 E, ahk_class Notepad  ; 椭圆形窗口.
;~ WinSet, Region, 50-0 250-0 150-250, ahk_class Notepad  ; 倒三角形窗口.
;~ WinSet, Region,, ahk_class Notepad ; 将窗口恢复原来/默认的形状.
;~ S↓
;~ 示例 #6
;~ 这是一个 region 带有更复杂的区域. 它在记事本(或任何其他窗口) 内创建一个透明的矩形孔. 下面指定了两个矩形: 一个外部的, 一个内部的. 每个矩形由 5 对 X/Y 坐标组成, 因为第一对是用来 "闭合" 每个矩形的重复坐标:

;~ WinSet, Region, 0-0 300-0 300-300 0-300 0-0   100-100 200-100 200-200 100-200 100-100, ahk_class Notepad

;~ 表达式中的运算符 https://wyagd001.github.io/zh-cn/docs/Variables.htm

;~ MyVar = 5,3,7,9,1,13,999,-4
;~ Sort MyVar, N D,  ; 数值排序, 使用逗号作为分隔符.
;~ MsgBox %MyVar%   ; 结果是 -4,1,3,5,7,9,13,999

; 下面的例子对文件的内容进行排序:
;~ FileRead, Contents, E:\Address List.txt
;~ if not ErrorLevel  ; 装载成功.
;~ {
    ;~ Sort, Contents
    ;~ FileDelete, E:\Address List (alphabetical).txt
    ;~ FileAppend, %Contents%, E:\Address List (alphabetical).txt
    ;~ Contents =  ; 释放内存.
;~ }

; 下面的例子设置 Win+C 热键来从打开的资源管理器窗口中
; 选择多个文件名复制文件, 对其中的文件名进行排序后放回剪贴板:
;~ #c::
;~ Clipboard = ; 必须为空以检测是否有效.
;~ Send ^c
;~ ClipWait 2
;~ if ErrorLevel
    ;~ return
;~ Sort Clipboard
;~ MsgBox Ready to be pasted:`n%Clipboard%
;~ return

; 下面的例子演示了通过回调函数进行自定义排序.
;~ MyVar = def`nabc`nmno`nFGH`nco-op`ncoop`ncop`ncon`n
;~ Sort, MyVar, F StringSort
;~ MsgBox % MyVar
;~ StringSort(a1, a2)
;~ {
    ;~ return a1 > a2 ? 1 : a1 < a2 ? -1 : 0  ; 基于 StringCaseSense 的设置按字母顺序进行排序.
;~ }

;~ MyVar = 5,3,7,9,1,13,999,-4
;~ Sort, MyVar, F IntegerSort D,
;~ MsgBox % MyVar
;~ IntegerSort(a1, a2)
;~ {
    ;~ return a1 - a2  ; 按上升的数值顺序进行排序.  此方法只有在两个数字的差不会超出 64 位有符号整数的范围才有效.
;~ }

;~ MyVar = 1,2,3,4
;~ Sort, MyVar, F ReverseDirection D,  ; 反转列表, 这样它包含了 4,3,2,1
;~ MsgBox % MyVar
;~ ReverseDirection(a1, a2, offset)
;~ {
    ;~ return offset  ; 如果原始列表中 a2 在 a1 的后面则偏移是正数; 否则为负数.
;~ }

;~ 不推荐: 不推荐在新脚本中使用 <> 运算符. 使用 != 运算符作为代替.

;~ MsgBox % Add(3, 4)
;~ Add(x, y)
;~ {
    ;~ return x + y   ; "Return" 可直接返回表达式的结果.
;~ }

;~ 参数
;~ 定义函数时, 其参数都在其名称后面的括号中列出(函数名和左括号之间不能有空格). 如果函数不接受任何参数, 请把括号留空, 例如: GetCurrentTimestamp().

;~ ByRef 参数: 从函数的角度看, 参数本质上是局部变量, 除非它们被定义为 ByRef, 例如:
;~ Swap(ByRef Left, ByRef Right)
;~ {
    ;~ temp := Left
    ;~ Left := Right
    ;~ Right := temp
;~ }

;~ 传递大字符串给函数时, 使用 ByRef 提高了性能, 并且通过避免生成字符串的副本节约了内存. 同样地, 使用 ByRef 送回长字符串给调用者通常比类似 Return HugeString 的方式执行的更好.

;~ [v1.0.90+]: 如果传递给 ByRef 参数的变量是不可修改的, 那么函数会表现的就像没有 "ByRef" 关键字一样. 例如, Swap(A_Index, i) 保存 A_Index 的值到 i 中, 但是当 Swap 函数返回时赋给 Left 的值会被丢弃.(译者注: 因为 A_Index 是只读的内置变量.)
;~ 如果 ParameterVar 是 ByRef 参数(按地址传递的参数) 而且调用者传递了这个参数则返回 1; 否则当 ParameterVar 为其他类型参数时返回 0.
;~ MsgBox, % Function(MyVar)
;~ Function(ByRef Param) ;如果入参类型修改为ByRef Param:=""，则Function()将返回False
;~ {
    ;~ return IsByRef(Param) ;如果Param参数没有ByRef，则返回False
;~ }

;~ [v1.0.97+]: 可以使用对象和数组返回多值甚至是命名值:
;~ Test1 := returnArray1()
;~ MsgBox % Test1[1] "," Test1[2]

;~ Test2 := returnArray2()
;~ MsgBox % Test2[1] "," Test2[2]

;~ Test3 := returnObject()
;~ MsgBox % Test3.id "," Test3.val

;~ returnArray1() {
  ;~ Test := [123,"ABC"]
  ;~ return Test
;~ }

;~ returnArray2() {
  ;~ x := 456
  ;~ y := "EFG"
  ;~ return [x, y]
;~ }

;~ returnObject() {
  ;~ Test := {id: 789, val: "HIJ"}
  ;~ return Test
;~ }

;~ 定义函数时, 在最后一个参数后面写一个星号来标记此函数为可变参数的, 这样让它可以接收可变数目的参数:
;~ 可变参数函数调用
;~ 虽然可变参数函数可以 接受 可变数目的参数, 不过在函数调用中使用相同的语法可以把数组作为参数传递给 任何 函数:
;~ Join(sep, params*) {
    ;~ str := ""
    ;~ for index,param in params
        ;~ str .= param . sep ;在这个函数之前调用MsgBox % index方法，则发现只有最后一个param才能走到下一句str方法，不知道为什么
    ;~ for index in params.MaxIndex() ;调用.MaxIndex()方法没看到结果
        ;~ str .= param[index] . sep
    ;~ return SubStr(str, 1, -StrLen(sep)) ;指定负的 Length 从而在返回字符串的末尾省略这个数目的字符(如果省略了全部或过多字符则返回空字符串).
;~ }
;~ #j::
;~ {
    ;~ MsgBox % Join("`n", "one", "two", "three")
    ;~ substrings := ["one", "two", "three", "four"]
    ;~ MsgBox % Join("`n", substrings*)
    ;~ return
;~ }

;~ 强制局部模式 [v1.1.27+]: 如果函数的第一行是单词 "local", 则假定所有变量引用(甚至是动态的) 都是局部的, 除非它们在函数 内 声明为全局的. 与默认模式不同, 强制局部模式具有以下行为:

;~ 全局变量
;~ 要在函数中引用现有的全局变量(或创建新的), 需要在使用前声明此变量为全局的. 例如:

;~ LogToFile(TextToLog)
;~ {
    ;~ global LogFileName  ; 此全局变量之前已经在函数外的某个地方赋值了.
    ;~ FileAppend, %TextToLog%`n, %LogFileName%
;~ }

;~ 假设全局模式: 如果函数需要访问或创建大量的全局变量, 通过在函数的首行使用单词 "global" 或声明局部变量可以把函数定义为假设其所有的变量都是全局的(除了它的参数). 例如:

;~ SetDefaults()
;~ {
    ;~ global  ; 如果此函数的首行是类似于 "local MyVar" 这样的, 那么这个单词可以省略.
    ;~ MyGlobal := 33  ; 把 33 赋值给全局变量, 必要时首先创建这个变量.
    ;~ local x, y:=0, z  ; 在这种模式中局部变量必须进行声明, 否则会假设它们为全局的.
;~ }

;~ 函数还可以使用这种假设全局模式来创建全局数组, 例如赋值给 Array%A_Index% 的循环.

;~ GetFromStaticArray(WhichItemNumber)
;~ {
    ;~ static
    ;~ static FirstCallToUs := true  ; 静态声明初始化仍然只运行一次(在脚本执行前).
    ;~ if FirstCallToUs  ; 在首次调用而不在后续的调用时创建静态数组.
    ;~ {
        ;~ FirstCallToUs := false
        ;~ Loop 10
            ;~ StaticArray%A_Index% := "Value #" . A_Index
    ;~ }
    ;~ return StaticArray%WhichItemNumber%
;~ }
;~ #j::
;~ {
    ;~ MsgBox % GetFromStaticArray(5)
    ;~ MsgBox % GetFromStaticArray(7)
    ;~ MsgBox % StaticArray3 ;这里拿到了全局变量，没太看懂为啥可以在外部访问到静态伪数组
    ;~ return
;~ }


#j::
{
    InitGlobal()
    DemonstrateForceStatic()
}

InitGlobal()
{
    global MyVar := "This is global"
}

DemonstrateForceStatic()
{
    global MyVar
    MsgBox % MyVar
    ;~ local
    ;~ static
    MyVar2 := "This is static"
    ListVars
    MsgBox
}

;~ 动态调用函数
;~ [v1.0.47.06+]: 通过百分号可以动态调用函数(包括内置函数). 例如,%Var%(x, "fox") 将调用名称保存在 Var 中的函数. 同样地, Func%A_Index%() 将调用 Func1() 或 Func2() 等, 这取决于 A_Index 的当前值.

;~ [v1.1.07.00+]: 在 %Var%() 中的 Var 可包含函数名或函数对象. 如果此函数不存在, 则调用默认基对象的 __Call 元函数.

;~ 如果由于下面的某个原因无法调用函数, 计算包含调用的表达式时可能会过早静默停止, 这样可能会产生问题.

;~ 调用不存在的函数, 这可以通过使用 If IsFunc(VarContainingFuncName) 来避免. 除了内置函数, 被调用函数的定义必须真实存在于脚本中, 通过类似 #Include 或对库函数的非动态调用的方法.
;~ 传递过少的参数, 这可以通过检查 IsFunc() 的返回值来避免(这是强制参数的数目加上一的数字). [v1.0.48+]: 注意允许传递过多的参数; 每个额外的参数在被完全计算(包括任何函数调用) 后丢弃.
;~ 最后, 对函数的动态调用比正常调用稍慢, 因为正常的调用在脚本开始运行前解析(查询).

;~ SplashTextOn, , , Displays only a title bar.
;~ Sleep, 2000
;~ SplashTextOn, 400, 300, Clipboard, The clipboard contains:`n%clipboard%
;~ WinMove, Clipboard, , 0, 0  ; 把弹出窗口移动到左上角.
;~ MsgBox, Press OK to dismiss the SplashText
;~ SplashTextOff

; 示例 #3: 检测热键的单次, 两次和三次按下. 这样
; 允许热键根据您按下次数的多少
; 执行不同的操作:
;~ #c::
;~ if winc_presses > 0 ; SetTimer 已经启动, 所以我们记录键击.
;~ {
    ;~ winc_presses += 1
    ;~ return
;~ }
;~ ; 否则, 这是新开始系列中的首次按下. 把次数设为 1 并启动
;~ ; 计时器:
;~ winc_presses = 1
;~ SetTimer, KeyWinC, -400 ; 在 400 毫秒内等待更多的键击.
;~ return

;~ KeyWinC:
;~ if winc_presses = 1 ; 此键按下了一次.
;~ {
    ;~ Run, m:\  ; 打开文件夹.
    ;~ MsgBox, Press 1
;~ }
;~ else if winc_presses = 2 ; 此键按下了两次.
;~ {
    ;~ Run, m:\multimedia  ; 打开不同的文件夹.
    ;~ MsgBox, Press 2
;~ }
;~ else if winc_presses > 2
;~ {
    ;~ MsgBox, Three or more clicks detected.
;~ }
;~ ; 不论触发了上面的哪个动作, 都对 count 进行重置
;~ ; 为下一个系列的按下做准备:
;~ winc_presses = 0
;~ return

;~ #c::
;~ {
    ;~ counter := new SecondCounter
    ;~ counter.Start()
    ;~ Sleep 5000
    ;~ counter.Stop()
    ;~ Sleep 2000
    ;~ return
;~ }

; 一个记录秒数的示例类...
;~ class SecondCounter {
    ;~ __New() {
        ;~ this.interval := 1000
        ;~ this.count := 0
        ;~ ; Tick() 有一个隐式参数 "this" 引用一个对象
        ;~ ; 所以
        ;~ ; 我们需要创建一个封装 "this" 和 Tick 方法的函数来调用:
        ;~ this.timer := ObjBindMethod(this, "Tick")
    ;~ }
    ;~ Start() {
        ;~ ; 已知限制: SetTimer 需要一个纯变量引用.
        ;~ timer := this.timer
        ;~ SetTimer % timer, % this.interval
        ;~ ToolTip % "Counter started"
    ;~ }
    ;~ Stop() {
        ;~ ; 在此之前传递一个相同的对象来关闭计时器:
        ;~ timer := this.timer
        ;~ SetTimer % timer, Off
        ;~ ToolTip % "Counter stopped at " this.count
        ;~ Sleep 2000
        ;~ ToolTip
    ;~ }
    ;~ ; 本例中, 计时器调用了以下方法:
    ;~ Tick() {
        ;~ ToolTip % ++this.count
    ;~ }
;~ }
;~ 示例 #4 的备注:

;~ 我们也可使用 this.timer := this.Tick.Bind(this). 当 this.timer 被调用时, 它实际上是调用 this.Tick.Call(this) 元方法(除非 this.Tick 没有再次执行). 而 ObjBindMethod 则是创建了一个调用 this.Tick() 的对象.
;~ 如果我们将 Tick 更名为 Call, 可直接使用 this 而非 this.timer. 这样也不会产生临时变量. 但 ObjBindMethod 在对象拥有多个可被不同事件源触发的方法时会很有用, 如热键, 菜单项, GUI 控件等.
;~ 如果计时器被自身调用的 函数/方法 修改或删除时, 它可能会简单的忽略 Label 参数. 在某些情况下, 这样就避免了需要保留原始对象传递给计时器, 这样可以省略一个临时变量(就像上面例子中的 timer 那样).


;~ 如果函数内的执行流在遇到 Return 前到达了函数的闭括号, 那么函数结束并返回空值(空字符串) 给其调用者. 当函数明确省略 Return 的参数时也返回空值.

;~ #c::
;~ {
    ;~ gosub MyLabel ; gosub是去执行子函数，是会正常返回到当前堆栈，goto是跳转标签，不会再返回到当前代码处
    ;~ MsgBox % "abcdef"
    ;~ return
    
;~ MyLabel:
    ;~ MsgBox % A_ThisLabel
    ;~ return
;~ }

;~ 热键和热字串标签也可以作为 Goto, Gosub 以及其他命令的目标. 然而, 如果一个热键或热字串含有多个变体, 则使用最接近脚本顶部的那个变体. 所有的热键修饰键或热字串选项都是标签名的一部分, 但不包括最后的双冒号(::).
;~ #c::
;~ {
    ;~ ; goto :o:xyz ;可以goto或gosub跳转到热字串，但如果热字符串是单行自动替换热字串如::xyz::abcdef，则只会执行热字串的return语句
    ;~ gosub ^!n ;也可以goto或gosub跳转到热键
    ;~ MsgBox % "this is after goto"
    ;~ return
;~ }

;~ :o:xyz::
;~ {
    ;~ MsgBox % "xyz"
    ;~ return
;~ }

;~ ; 从内嵌循环内部直接继续新的外层循环. 鼓励使用 Break 和 Continue 代替 goto 因为它们通常让脚本更容易阅读和维护.
;~ outer:
;~ Loop 3
;~ {
    ;~ x := A_Index
    ;~ Loop 3
    ;~ {
        ;~ if (x*A_Index = 4)
            ;~ continue outer  ; 等同于 continue 2 或 goto continue_outer.
        ;~ MsgBox %x%,%A_Index%
    ;~ }
    ;~ continue_outer: ; 用于 goto 命令.
    ;~ ErrorLevel:=ErrorLevel ; 在修订号 57 之前, 标签不能指向区块的末尾.
;~ }

;ComObjCreate()
;~ #c::
;~ {
    ;~ ie := ComObjCreate("InternetExplorer.Application")
    ;~ ie.Visible := true  ; 已知此语句在 IE7 上无法正常执行.
    ;~ ie.Navigate("https://www.baidu.com/")
    ;~ return
;~ }

; 简单数组
;~ array := ["one", "two", "three"]

;~ ; 从 1 依次递加到数组的项目数:
;~ Loop % array.Length()
    ;~ MsgBox % array[A_Index]

;~ ; 枚举数组内容:
;~ For index, value in array
    ;~ MsgBox % "Item " index " is '" value "'"
    
;关联数组  
;~ array := {ten: 10, twenty: 20, thirty: 30}
;~ For key, value in array
    ;~ MsgBox %key% = %value%


;~ 由于在释放一个对象时, 到这个对象的所有引用都必须被释放, 所以包含循环引用的对象无法被自动释放. 例如, 如果 x.child 引用 y 且 y.parent 引用了 x, 则清除 x 和 y 是不够的, 因为父对象仍然包含到这个子对象的引用, 反之亦然. 要避免此问题, 请首先移除循环引用.
;~ x := {}, y := {}             ; 创建两个对象.
;~ x.child := y, y.parent := x  ; 创建循环引用.

;~ y.parent := ""               ; 在释放对象前必须移除循环引用.
;~ x := "", y := ""             ; 如果没有上一行, 则此行无法释放对象.

;~ 当方法调用紧接着赋值运算符, 那么它等同于用参数来设置属性. 例如, 下面的方式是等同的:
;~ obj.item(x) := y
;~ obj.item[x] := y

;~ 默认情况下, 字符串键 "base" 对应对象的 base 属性, 因此不能通过正常的赋值来存储普通值. 但是, 任何属性都可以通过其他方式存储值来覆盖, 例如 ObjRawSet(Object, "base", "") 或 Object.SetCapacity("base", 0). 一旦完成, 键 "base" 就跟其他字符串一样了.
;~ 尽管内置方法的名称可以作为键名来使用, 这样将阻止同名对应的方法被调用, 比如一个键的名称是 "Length", 那么你将无法调用 obj.Length() 方法. (除非键的值是相应函数的引用, 比如 ObjLength).

;~ ; 获取函数名为 "StrLen" 的函数引用.
;~ fn := Func("StrLen")
;~ ; 显示函数的相关信息.
;~ MsgBox % fn.Name "() 是 " (fn.IsBuiltIn ? "内置函数." : "用户定义的函数.")

;~ 如果变量 func 包含一个函数名, 此函数可以通过两种方式进行调用: %func%() 或 func.(). 然而, 由于每次都需要解析函数名, 所以多次调用时效率低下. 为了改善性能, 脚本可以获取到函数的引用并保存以供后面使用:
;~ Add(x, y)
;~ {
    ;~ return x + y   ; "Return" 可直接返回表达式的结果.
;~ }
;~ myFunc := Func("Add")
;~ ; MsgBox % myFunc.Call(1, 2)
;~ MsgBox % %myFunc%(1, 2)
;可使用如下语法通过引用来调用函数: 函数引用

;~ RetVal := %Func%(Params)     ; 需要 [v1.1.07+]
;~ RetVal := Func.Call(Params)  ; 需要 [v1.1.19+]
;~ RetVal := Func.(Params)      ; 不推荐

;~ 数组嵌套
;~ AutoHotkey 通过显式地把数组存储到其他数组中来支持"多维"数组. 例如, 表格可以表示为行数组, 其中每一行本身是一个列数组. 在这种情况下, x 行 y 列的内容可以用以下两种方法的其中一个进行设置:
;~ table[x][y] := content  ; A
;~ table[x, y] := content  ; B
;~ 如果 table[x] 不存在, A 和 B 在两个方面有区别:

;~ A 失败而 B 会自动创建一个对象并把它存储到 table[x] 中.
;~ 如果 table 的 base 定义了元函数, 可以用如下方式调用它们:
;~ table.base.__Get(table, x)[y] := content   ; A
;~ table.base.__Set(table, x, y, content)     ; B
;~ S↓
;~ 因此, B 允许对象为整个赋值定义自定义行为.
;~ 类似 table[a, b, c, d] := value 这样的多维赋值按以下方式处理:

;~ 如果仅剩一个键, 则执行赋值操作并返回. 在其他情况时:
;~ 在对象中查找列表中的首个键.
;~ 如果找到非对象值, 则失败.
;~ 如果没有找到对象, 则创建一个并保存.
;~ 重复调用子对象, 从顶部开始把剩下的键和值传递过去.
;~ 这种行为仅适用于由脚本创建的对象, 而不适合特殊的对象类型例如 COM 对象或 COM 数组.


;~ array := [Func("FirstFunc"), Func("SecondFunc")]

;~ ; 调用每个函数, 传递 "foo" 参数:
;~ Loop 2
    ;~ array[A_Index].Call("foo")

;~ ; 调用每个函数, 隐式地把数组自己作为参数传递:
;~ Loop 2
    ;~ array[A_Index]()

;~ FirstFunc(param) {
    ;~ MsgBox % A_ThisFunc ": " (IsObject(param) ? "object" : param)
;~ }
;~ SecondFunc(param) {
    ;~ MsgBox % A_ThisFunc ": " (IsObject(param) ? "object" : param)
;~ }

;自定义对象
;由脚本创建的对象可以不包含预定义结构. 相反, 每个对象可以从其 base 对象(基对象, 也称为"原型"或"类") 中继承属性和方法. 还可以随时添加或移除对象中的属性和方法, 这些改变会影响它的所有派生对象. 更多复杂或专用方案, 可通过定义元函数来覆盖它所派生对象的标准行为.
;
;基对象只是普通对象, 通常有两种创建方法:

;~ class baseObject {
    ;~ static foo := "bar"
;~ }
;~ ; 或
;~ baseObject := {foo: "bar"}
;~ baseObject2 := {abc: "abc"}
;~ ;要继承其他对象来创建新对象, 脚本可以赋值为 base 属性或使用 new 关键字:

;~ obj1 := Object(), obj1.base := baseObject
;~ obj2 := {base: baseObject}
;~ obj3 := new baseObject
;~ MsgBox % obj1.foo " " obj2.foo " " obj3.foo
;~ obj2.base := baseObject2 ;可以重新给对象赋base，用以替换对象继承的所有属性和方法
;~ ; obj2["base"] := baseObject2
;~ MsgBox % obj2.abc "&" obj2.foo

;原型
;原型或 base 对象的创建和操作和其他任何对象一样. 例如, 带有单属性和单方法的普通对象可以这样创建:

;~ ; 创建对象.
;~ thing := {}
;~ ; 存储值.
;~ thing.foo := "bar"
;~ ; 通过存储函数引用创建方法.
;~ thing.test := Func("thing_test")
;~ ; 调用方法.
;~ thing.test()

;~ thing_test(this) {
   ;~ MsgBox % this.foo
;~ }
;~ ;如果另一个对象继承自某个对象, 那么这个对象被称为原型或基:
;~ other := {}
;~ other.base := thing
;~ other.test()

;~ 类 [v1.1.00+]
;~ 从根本上讲, "类"是具有相同属性和行为的一类事物. 由基类或原型定义了一系列属性和行为的对象, 就可以被称为 类 对象. 为了方便, 用 "Class" 关键字定义基对象可以像下面这样:

;~ class ClassName extends BaseClassName
;~ {
    ;~ InstanceVar := Expression		; 实例变量(实例属性)
    ;~ static ClassVar := Expression	; 静态变量(类属性)

    ;~ class NestedClass			; 嵌套类
    ;~ {
        ;~ ...
    ;~ }

    ;~ Method()				; 方法, 类定义中的函数称作方法
    ;~ {
        ;~ ...
    ;~ }

    ;~ Property[]  			; 属性定义, 方括号是可选的
    ;~ {
        ;~ get {
            ;~ return ...
        ;~ }
        ;~ set {
            ;~ return ... := value		; value 在此处为关键字, 不可用其他名称
        ;~ }
    ;~ }
;~ }

;~ class Hello
;~ {
    ;a := "" 如果不在__New里定义则可以省略
    ;~ __New(para)
    ;~ {
        ;~ this.a := para
        ;return this 这行可以省略
    ;~ }
    
    ;~ ; __Delete 不可被任何含有 "__Class" 键的对象调用. Class objects(类对象) 默认包含这个键.
    ;~ __Delete()
    ;~ {
        ;~ MsgBox % "delete Hello " . this.a
    ;~ }
    
    ;~ value[]  			; 属性定义, 方括号是可选的
    ;~ {
        ;~ get {
            ;~ return this.a
        ;~ }
        ;~ set {
            ;~ return this.a := value		; value 在此处为关键字, 不可用其他名称
        ;~ }
    ;~ }
;~ }

;~ #c::
;~ {
    ;~ h := new Hello("note")
    ;~ MsgBox % h.value
    ;~ h.value := "luck"
    ;~ MsgBox % h.value
    ;h := "" ;如果不加这行，则对象未自动调用释放，只在停止脚本的时候释放 或者 再次调用该热键时会把h的引用对象释放，此时会调用__Delete方法
;~ }

;元函数(Meta-Functions)
;方法语法:
;class ClassName {
;    __Get([Key, Key2, ...])
;    __Set([Key, Key2, ...], Value)
;    __Call(Name [, Params...])
;}
;
;函数语法:
;MyGet(this [, Key, Key2, ...])
;MySet(this [, Key, Key2, ...], Value)
;MyCall(this, Name [, Params...])
;
;ClassName := { __Get: Func("MyGet"), __Set: Func("MySet"), __Call: Func("MyCall") }


;~ class Color
;~ {
    ;~ __New(aRGB)
    ;~ {
        ;~ this.RGB := aRGB
    ;~ }
    
    ;~ static Shift := {R:16, G:8, B:0}

    ;~ __Get(aName)
    ;~ {
        ;~ ; 注意: 如果这里用 this.Shift 将导致死循环! 因为 this.Shift 将递归调用 __Get 元方法.
        ;~ shift := Color.Shift[aName]  ; 将位元数赋值给 shift.
        ;~ if (shift != "")  ; 检查是否为已知属性.
            ;~ return (this.RGB >> shift) & 0xff
        ;~ ; 注意: 这里用 'return' 可终止 this.RGB 属性调用逻辑.
    ;~ }

    ;~ __Set(aName, aValue)
    ;~ {
        ;~ if ((shift := Color.Shift[aName]) != "")
        ;~ {
            ;~ aValue &= 255  ; 截取为适合的范围.

            ;~ ; 计算并保存新的 RGB 值.
            ;~ this.RGB := (aValue << shift) | (this.RGB & ~(0xff << shift))

            ;~ ; 'Return' 表示一个新的 键值对 被创建.
            ;~ ; 同时还定义了 'x := clr[name] := val' 中的 'x' 所保存的值是什么:
            ;~ return aValue
        ;~ }
        ;~ ; 注意: 这里用 'return' 终止 this.stored_RGB 和 this.RGB 的逻辑.
    ;~ }

    ;~ ; 元函数可以混合多个属性:
    ;~ RGB {
        ;~ get {
            ;~ ; 返回它的十六进制格式:
            ;~ return format("0x{:06x}", this.stored_RGB)
        ;~ }
        ;~ set {
            ;~ return this.stored_RGB := value
        ;~ }
    ;~ }
;~ }

;~ ;由于使用元函数出错的风险较大, 应该尽量避免使用(见上面代码中的注意).
;~ #c::
;~ {
    ;~ red  := new Color(0xff0000), red.R -= 5 ;调用属性时，优先从__Get方法中获取，如果没有return，则会调用this.RGB属性
    ;~ MsgBox % "red: " red.R "," red.G "," red.B " = " red.RGB
    
    ;~ cyan := new Color(0), cyan.G := 255, cyan.B := 255
    ;~ MsgBox % "cyan: " cyan.R "," cyan.G "," cyan.B " = " cyan.RGB
    
    ;~ red := ""
    ;~ cyan := ""
;~ }

;函数对象
class FunctionObject {
    __Call(method, args*) {
        if (method = "")
            return this.Call(args*)
        if (IsObject(method))
            return this.Call(method, args*)
    }
}

;~ class FuncArrayType extends FunctionObject {
    ;~ Call(obj, params*) {
        ;~ ; 调用函数列表.
        ;~ Loop % this.Length()
            ;~ this[A_Index].Call(params*)
    ;~ }
;~ }

;~ One(param1, param2) {
    ;~ ListVars
    ;~ MsgBox
;~ }
;~ Two(param1, param2) {
    ;~ ListVars
    ;~ MsgBox
;~ }

;~ #c::
;~ {
    ;~ ; 创建一个函数数组.
    ;~ funcArray := new FuncArrayType
    ;~ ; 向数组中添加函数(可以在任何位置完成).
    ;~ funcArray.Push(Func("One")) ;push调用的是数组对象的增加对象方法
    ;~ funcArray.Push(Func("Two"))
    ;~ ; 创建一个使用数组作为方法的对象.
    ;~ obj := {method: funcArray}
    ;~ ; 调用方法.
    ;~ obj.method("foo", "bar")
;~ }

;绑定函数对象
;~ fn := Func("RealFn").Bind(1)

;~ %fn%(2)    ; 显示 "1, 2"
;~ fn.Call(3) ; 显示 "1, 3"

;~ RealFn(a, b) {
    ;~ MsgBox %a%, %b%
;~ }

;~ #c::
;~ {
    ;~ currentFile := FileOpen(A_ScriptFullPath, "r")
    ;~ getLine := ObjBindMethod(currentFile, "ReadLine")
    ;~ MsgBox % %getLine%()  ;显示此文件的第一行.
;~ }

;对象作为函数
;~ class Properties extends FunctionObject
;~ {
    ;~ Call(aTarget, aName, aParams*)
    ;~ {
        ;~ ; 如果该属性保存了一个半属性的定义则调用它.
        ;~ if ObjHasKey(this, aName)
            ;~ return this[aName].Call(aTarget, aParams*)
    ;~ }
;~ }

;~ class Color
;~ {
    ;~ __New(aRGB)
    ;~ {
        ;~ this.RGB := aRGB
    ;~ }

    ;~ class __Get extends Properties
    ;~ {
        ;~ R() {
            ;~ return (this.RGB >> 16) & 255
        ;~ }
        ;~ G() {
            ;~ return (this.RGB >> 8) & 255
        ;~ }
        ;~ B() {
            ;~ return this.RGB & 255
        ;~ }
    ;~ }

    ;~ ;...
;~ }

;~ #c::
;~ {
    ;~ blue := new Color(0x0000ff)
    ;~ MsgBox % blue.R "," blue.G "," blue.B
    ;~ blue = ""
;~ }


;子类化数组嵌套
;在多参数赋值, 例如 table[x, y] := content 会隐式地创建一个新对象, 这个新对象一般不含基, 因此没有自定义方法或特殊行为. __Set 可以用来初始化这样的对象, 如下所示.
;~ x_Setter(x, p1, p2, p3) {
    ;~ x[p1] := new x.base
;~ }

;~ x_Addr(x) {
    ;~ return &x
;~ }

;~ #c::
;~ {
    ;~ x := {base: {addr: Func("x_Addr"), __Set: Func("x_Setter")}}
    
    ;~ ; 赋值, 隐式调用 x_Setter 来创建子对象.
    ;~ x[1,2,3] := "..."

    ;~ ; 获取值并调用示例方法.
    ;~ MsgBox % x[1,2,3] "`n" x.addr() "`n" x[1].addr() "`n" x[1,2].addr()
;~ }

;由于 x_Setter 含有四个必需参数, 所以只有在有两个或更多键参数时才会调用它. 当上面的赋值出现时, 会发生下面的情况:
;
;x[1] 不存在, 所以调用 x_Setter(x,1,2,3)(由于参数过少所以 "..." 不会被传递).
;x[1] 被赋值为与 x 含有相同基的新对象.
;不返回任何值 – 赋值继续.
;x[1][2] 不存在, 所以调用 x_Setter(x[1],2,3,"...").
;x[1][2] 被赋值为与 x[1] 含有相同基的新对象.
;不返回任何值 – 赋值继续.
;x[1][2][3] 不存在, 但由于 x_Setter 需要四个参数而这里只有三个(x[1][2], 3, "..."), 所以不会调用它且赋值正常完成.


;默认基对象
;当非对象值用于对象语法时, 则调用 默认基对象. 这可以用于调试或为字符串, 数字和/或变量定义全局的类对象行为. 默认基可以使用带任何非对象值的 .base 进行访问; 例如, "".base. 尽管默认基无法像 "".base := Object() 这样进行 set, 不过它可以有自己的基如同在 "".base.base := Object() 中那样.
;自动初始化变量
;当使用空变量作为 set 运算的目标时, 它直接被传递给 __Set 元函数, 这样它就有机会插入新对象到变量中. 为简洁起见, 此示例不支持多个参数; 如果需要, 可以使用可变参数函数实现.
;~ "".base.__Set := Func("Default_Set_AutomaticVarInit")

;~ empty_var.foo := "bar"
;~ MsgBox % empty_var.foo

;~ Default_Set_AutomaticVarInit(ByRef var, key, value)
;~ {
    ;~ if (var = "")
        ;~ var := Object(key, value)
;~ }

;~ Default_Get_PseudoProperty(nonobj, key)
;~ {
    ;~ if (key = "length")
        ;~ return StrLen(nonobj)
;~ }

;~ Default_is(nonobj, type)
;~ {
    ;~ if nonobj is %type%
        ;~ return true
    ;~ return false
;~ }

;~ #c::
;~ {
    ;~ "".base.__Get := Func("Default_Get_PseudoProperty")
    ;~ "".base.is    := Func("Default_is")

    ;~ MsgBox % A_AhkPath.length " == " StrLen(A_AhkPath)
    ;~ MsgBox % A_AhkPath.length.is("integer")
;~ }

;~ ;注意也可以使用内置函数, 不过这时不能省略大括号:

;~ "".base.length := Func("StrLen")
;~ MsgBox % A_AhkPath.length() " == " StrLen(A_AhkPath)

;调试
;如果不希望把一个值视为对象, 每当调用非对象值可以显示警告:
;~ Default__Warn(nonobj, p1="", p2="", p3="", p4="")
;~ {
    ;~ ListLines
    ;~ MsgBox A non-object value was improperly invoked.`n`nSpecifically: %nonobj%
;~ }

;~ #c::
;~ {
    ;~ "".base.__Get := "".base.__Set := "".base.__Call := Func("Default__Warn")

    ;~ empty_var.foo := "bar"
    ;~ x := (1 + 1).is("integer")
;~ }

;~ ; 对象的指针
;~ ; 在一些罕见的情况中, 可能需要通过 DllCall 传递对象到外部代码或把它存储到二进制数据结构以供以后检索. 可以通过 address := &object 来检索对象的地址; 不过, 这样实际上创建了一个对象的两个引用, 但程序只知道对象中的一个. 如果对象的最后一个 已知 引用被释放, 该对象将被删除. 因此, 脚本必须设法通知对象它的引用增加了. 有两种方法实现:

;~ ; 方法 #1: 显式地增加引用计数.
;~ address := &object
;~ ObjAddRef(address)

;~ ; 方法 #2: 使用 Object(), 增加一个引用并返回地址.
;~ address := Object(object)

;~ ;这个函数也可以把地址转换回引用:
;~ object := Object(address)

;~ ;无论用的上述哪种方法, 脚本都需要在完成对象的引用之后通知对象:
;~ ; 减少对象的引用计数, 以允许它被释放:
;~ ObjRelease(address)
;~ ;一般来说, 对象地址的每个新副本都应该被视为对象的另一个引用, 所以脚本必须在获得副本之后立即调用 ObjAddRef, 并在丢弃副本之前立即调用 ObjRelease. 例如, 每当通过类似 x := address 这样复制地址时, 就应该调用一次 ObjAddRef. 同样的, 当脚本使用 x 完时(或者用其他值覆盖 x), 就应该调用一次 ObjRelease.

;~ ;注意, Object() 函数甚至可以在对象创建之前就可以使用, 比如 COM 对象和 File 对象.

;~ currentFilePath := Clipboard
;~ MsgBox % currentFilePath
;~ file := FileOpen(currentFilePath, "r")
;~ if not IsObject(file)
;~ {
    ;~ MsgBox % "Failed to open file: " currentFilePath
;~ }
; Loop是自动循环每行内容，如果需要读取整个文档内容，建议使用FileRead
;~ Loop, read, %currentFilePath%
;~ {
    ;~ Loop, parse, A_LoopReadLine, %A_Tab%
    ;~ {
        ;~ MsgBox, Field number %A_Index% is %A_LoopField%.
    ;~ }
;~ }

;~ FileRead, Contents, C:\Address List.txt
;~ if not ErrorLevel  ; 加载成功
;~ {
    ;~ Sort, Contents
    ;~ FileDelete, C:\Address List (alphabetical).txt
    ;~ FileAppend, %Contents%, C:\Address List (alphabetical).txt
    ;~ Contents =  ; 清空占用的内存.
;~ }

;$^c::
;Send,^c
;Send,^!d
;return
;前面加一个$就不会递归了 https://www.zhihu.com/question/21568668/answer/54531030

;Space & i::
;{
;    SendInput {Up}
;    Return
;}
;
;Space & k::
;{
;    SendInput {Down}
;    Return
;}
;
;Space & j::
;{
;    SendInput {Left}
;    Return
;}
;
;Space & l::
;{
;    SendInput {Right}
;    Return
;}
;
;$Space::
;{
;    SendInput {Space}
;}