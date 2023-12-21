;#: Win
;!: Alt 
;^: Ctrl
;+: Shift
;<: 使用成对按键中左边的那个. 例如 <!a 相当于 !a, 只是使用左边的 Alt 键才可以触发.
;>: 使用成对按键中右边的那个.
;&: 连接两个按钮合并成一个自定义热键
;~: 激发热键时, 不会屏蔽(被操作系统隐藏) 热键中按键原有的功能

#Hotstring EndChars `t

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

#IfWinActive ahk_class TTOTAL_CMD
^!e::
{
    SendInput ^3
    Sleep 100 ;需要等100ms，否则路径还未拷贝到剪切板
    if SubStr(Clipboard, -3) != ".txt" and SubStr(Clipboard, -3) != ".log"
    {
        MsgBox, 64, FileTypeError, Select file is not txt or log file. File full path: `n%Clipboard%
        return
    }
    
    FileRead, fileContent, %Clipboard%
    if ErrorLevel ;如果失败则ErrorLevel为1
    {
        MsgBox % "Failed to read file: " . Clipboard
        return
    }
    
    if InStr(fileContent, "error") or InStr(fileContent, "fail")
    {
        MsgBox, 16, ContentCheckError, Word "error" or "fail" is found in current file!!!`nPlease check the file.
    }
    else
    {
        MsgBox, 0, ContentCheckSucceed, Word "error" or "fail" is not found in current file.
    }
    fileContent := ""
    return
}

#IfWinActive ahk_exe Code.exe
generateVscCode(ByRef language)
{
    SendInput, ``````%language%
    SendInput, {Enter 2}
    SendInput, ``````
    SendInput, {Left 4}
}

:o:``p::
{
    generateVscCode("python")
    return
}

:o:``j::
{
    generateVscCode("java")
    return
}

:o:``c::
{
    generateVscCode("c{+}{+}") ;+要用{+}转义
    return
}

#IfWinActive ahk_exe Typora.exe
generateTyporaCode(ByRef language)
{
    SendInput, ``````%language%
    SendInput, {Enter}
}

:o:``p::
{
    generateTyporaCode("python")
    return
}

:o:``j::
{
    generateTyporaCode("java")
    return
}

:o:``c::
{
    generateTyporaCode("c{+}{+}") ;+要用{+}转义
    return
}

:o:``r::
{
    generateTyporaCode("react")
    return
}

:o:``g::
{
    generateTyporaCode("go")
    return
}

#IfWinActive ahk_class Chrome_WidgetWin_1
^;::
{
    SendInput {End}
    return
}
^+;::
{
    SendInput {Home}
    return
}

#IfWinActive
;~ ^!e::
;~ {
    ;~ MsgBox You pressed Control+Alt+e in a window other than Total_cmd
    ;~ return
;~ }

;win+a
#a::
{
    SendInput xxx{Tab}xxx{"}xxx
    return
}

;:*:]::{ButtonRightClick}

;屏蔽屏蔽Win+l键：https://blog.csdn.net/sxwxyfp/article/details/78222191
#j::SendInput {Left}
;#l::SendInput {Right}
#k::SendInput {Down}
#i::SendInput {Up}

:o:clionsh::/home/ben/software/JetBrains/clion-2023.2.2/bin/clion.sh > /dev/null 2> /home/ben/software/JetBrains/logs/clion.log &
:o:clionsh44::/data01/dingben/software/JetBrains/clion-2022.3.3/bin/clion.sh > /dev/null 2> /data01/dingben/software/JetBrains/logs/clion.log &
:o:pycharmsh::/home/ben/Softwares/JetBrains/pycharm-2020.3/bin/pycharm.sh > /dev/null 2> /home/ben/softwares/JetBrains/logs/pycharm.log &
:o:ideash::/home/ben/software/JetBrains/idea-IU-232.10227.8/bin/idea.sh > /dev/null 2> /home/ben/software/JetBrains/logs/idea.log &
:o:ideash44::/data01/dingben/software/JetBrains/idea-IU-231.8109.175/bin/idea.sh > /dev/null 2> /data01/dingben/software/JetBrains/logs/idea.log &
:o:golandsh::/home/ben/Softwares/JetBrains/GoLand-2022.1.1/bin/goland.sh > /dev/null 2> /home/ben/softwares/JetBrains/logs/goland.log &


; =================================================Activate Total Commander Begin==============================================================
DetectHiddenWindows,On
SetTitleMatchMode,2

; 激活或者隐藏 Total Commander 10.00 主窗口的快捷键为:
; alt+shift+s
!+s::
{
    ActivateOrHideWindowHotkey()
    return
}

ActivateOrHideWindowHotkey()
{
    ; MsgBox,PressedHotkey检测到已经按下快捷键
    Sleep,2
    IfWinActive,ahk_class TTOTAL_CMD
    {
        Sleep,2
        if(WinActive(ahk_class TTOTAL_CMD))
        {
            Sleep,2
            WinGetClass, MyClass, A
            IfInString,MyClass,TTOTAL_CMD
            {
                Sleep,2
                IfWinActive,Total Commander
                {
                    TCTitle:="Total Commander"
                    Sleep,2
                    WinGetActiveTitle,Title
                    IfInString,Title,%TCTitle%
                    {
                        ; MsgBox,MinimizeWindow最小化指定窗口
                        WinMinimize,Total Commander ahk_class TTOTAL_CMD
                        Sleep,2
                        ;~ ExitApp
                    }
                }
            }
        }
    }
    else
    {
        ; MsgBox,ActivateWindow显示并且最大化并且激活指定窗口
        ;~ SetTimer,ActivateWindowTimer,20
        ActivateWindowTimer()
        ;~ ExitApp
    }
}

;根据窗口类名显示并激活Total Commander窗口
ActivateWindowTimer()
{
    ; 1、如果程序没有运行则运行程序,这里我把可执行文件名改成了TOTALCMD64.EXE
    Process,Exist,TOTALCMD64.EXE
    if (%ErrorLevel%=0)
    {
        IfExist,D:/Program Files/TotalCMD_v10.52_64bit/TOTALCMD64.EXE
        {
            Run,D:/Program Files/TotalCMD_v10.52_64bit/TOTALCMD64.EXE
        }
        else
        {
            MsgBox,找不到文件 D:/Program Files/TotalCMD_v10.52_64bit/TOTALCMD64.EXE
            return
        }
    }
    ; 2、显示并激活（切换到）指定窗口
    WinShow,Total Commander ahk_class TTOTAL_CMD
    WinMove,Total Commander ahk_class TTOTAL_CMD,,0,0,A_ScreenWidth,A_ScreenHeight
    WinActivate,Total Commander ahk_class TTOTAL_CMD
    WinMaximize,Total Commander ahk_class TTOTAL_CMD
    Sleep,2
    DetectHiddenWindows,On
    SetTitleMatchMode,2
    WinGet, WinID, ID,Total Commander ahk_class TTOTAL_CMD
    DllCall("SwitchToThisWindow", "UInt", WinID, "UInt", 1)
    ; 3、检查指定窗口是否激活成功，
    ; 假如激活成功则退出Timer计时器循环执行，
    ; 如果激活失败则继续尝试激活指定窗口

    Sleep,2
    IfWinActive,ahk_class TTOTAL_CMD
    {
        Sleep,2
        if(WinActive(ahk_class TTOTAL_CMD))
        {
            Sleep,2
            WinGetClass, MyClass, A
            IfInString,MyClass,TTOTAL_CMD
            {
                Sleep,2
                IfWinActive,Total Commander
                {
                    TCTitle:="Total Commander"
                    Sleep,2
                    WinGetActiveTitle,Title
                    IfInString,Title,%TCTitle%
                    {
                        SetTimer,ActivateWindowTimer,Delete
                        Sleep,2
                        ;~ ExitApp
                    }
                }
            }
        }
    }
}
; =================================================Activate Total Commander End==============================================================