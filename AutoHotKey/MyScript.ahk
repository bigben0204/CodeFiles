﻿;#: Win
;!: Alt 
;^: Ctrl
;+: Shift
;<: 使用成对按键中左边的那个. 例如 <!a 相当于 !a, 只是使用左边的 Alt 键才可以触发.
;>: 使用成对按键中右边的那个.
;&: 连接两个按钮合并成一个自定义热键
;~: 激发热键时, 不会屏蔽(被操作系统隐藏) 热键中按键原有的功能

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
    FileRead, fileContent, %Clipboard%
    if ErrorLevel ;如果失败则ErrorLevel为1
    {
        MsgBox % "Failed to read file: " . Clipboard
    }
    
    if InStr(fileContent, "error") or InStr(fileContent, "fail")
    {
        MsgBox, 16, Error, Word "error" or "fail" is found in current file!!!`nPlease check the file.
    }
    else
    {
        MsgBox, 0, Succeed, Word "error" or "fail" is not found in current file.
    }
    fileContent := ""
    return
}
#IfWinActive
;~ ^!e::
;~ {
    ;~ MsgBox You pressed Control+Alt+e in a window other than Total_cmd
    ;~ return
;~ }