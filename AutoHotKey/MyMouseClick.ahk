; 把小键盘作为鼠标使用 -- 作者: deguix
; https://www.autohotkey.com
; 此脚本用您的键盘来实现鼠标操作,
; 几乎相当于真正的鼠标(在一些任务中甚至更方便).
; 它支持多达五个鼠标按钮和鼠标滚轮的转动.
; 它还具有能自定义移动速度, 加速和 "轴反转" 的特点.

/*
o------------------------------------------------------------o
|Using Keyboard Numpad as a Mouse                            |
(------------------------------------------------------------)
| By deguix           / A Script file for AutoHotkey 1.0.22+ |
|                    ----------------------------------------|
|                                                            |
|    This script is an example of use of AutoHotkey. It uses |
| the remapping of numpad keys of a keyboard to transform it |
| into a mouse. Some features are the acceleration which     |
| enables you to increase the mouse movement when holding    |
| a key for a long time, and the rotation which makes the    |
| numpad mouse to "turn". I.e. NumpadDown as NumpadUp        |
| and vice-versa. See the list of keys used below:           |
|                                                            |
|------------------------------------------------------------|
| Keys                  | Description                        |
|------------------------------------------------------------|
| ScrollLock (toggle on)| Activates numpad mouse mode.       |
|-----------------------|------------------------------------|
| Numpad0               | Left mouse button click.           |
| Numpad5               | Middle mouse button click.         |
| NumpadDot             | Right mouse button click.          |
| NumpadDiv/NumpadMult  | X1/X2 mouse button click. (Win 2k+)|
| NumpadSub/NumpadAdd   | Moves up/down the mouse wheel.     |
|                       |                                    |
|-----------------------|------------------------------------|
| NumLock (toggled off) | Activates mouse movement mode.     |
|-----------------------|------------------------------------|
| NumpadEnd/Down/PgDn/  | Mouse movement.                    |
| /Left/Right/Home/Up/  |                                    |
| /PgUp                 |                                    |
|                       |                                    |
|-----------------------|------------------------------------|
| NumLock (toggled on)  | Activates mouse speed adj. mode.   |
|-----------------------|------------------------------------|
| Numpad7/Numpad1       | Inc./dec. acceleration per         |
|                       | button press.                      |
| Numpad8/Numpad2       | Inc./dec. initial speed per        |
|                       | button press.                      |
| Numpad9/Numpad3       | Inc./dec. maximum speed per        |
|                       | button press.                      |
| ^Numpad7/^Numpad1     | Inc./dec. wheel acceleration per   |
|                       | button press*.                     |
| ^Numpad8/^Numpad2     | Inc./dec. wheel initial speed per  |
|                       | button press*.                     |
| ^Numpad9/^Numpad3     | Inc./dec. wheel maximum speed per  |
|                       | button press*.                     |
| Numpad4/Numpad6       | Inc./dec. rotation angle to        |
|                       | right in degrees. (i.e. 180?=     |
|                       | = inversed controls).              |
|------------------------------------------------------------|
| * = These options are affected by the mouse wheel speed    |
| adjusted on Control Panel. If you don't have a mouse with  |
| wheel, the default is 3 +/- lines per option button press. |
o------------------------------------------------------------o
*/

;START OF CONFIG SECTION

#SingleInstance force
#MaxHotkeysPerInterval 500

; Using the keyboard hook to implement the Numpad hotkeys prevents
; them from interfering with the generation of ANSI characters such
; as ?  This is because AutoHotkey generates such characters
; by holding down ALT and sending a series of Numpad keystrokes.
; Hook hotkeys are smart enough to ignore such keystrokes.
#UseHook


;This is needed or key presses would faulty send their natural
;actions. Like NumpadDiv would send sometimes "/" to the
;screen.       
#InstallKeybdHook

Temp = 0
Temp2 = 0

MouseRotationAnglePart = %MouseRotationAngle%
;Divide by 45?because MouseMove only supports whole numbers,
;and changing the mouse rotation to a number lesser than 45?
;could make strange movements.
;
;For example: 22.5?when pressing NumpadUp:
;  First it would move upwards until the speed
;  to the side reaches 1.
MouseRotationAnglePart /= 45

MouseCurrentAccelerationSpeed = 0
MouseCurrentSpeed = %MouseSpeed%

MouseWheelCurrentAccelerationSpeed = 0
MouseWheelCurrentSpeed = %MouseSpeed%

SetKeyDelay, -1
SetMouseDelay, -1

Hotkey, *u, ButtonLeftClick
Hotkey, *NumpadIns, ButtonLeftClickIns
Hotkey, *Numpad5, ButtonMiddleClick
Hotkey, *NumpadClear, ButtonMiddleClickClear
Hotkey, *i, ButtonRightClick
Hotkey, *NumpadDel, ButtonRightClickDel
Hotkey, *NumpadDiv, ButtonX1Click
Hotkey, *NumpadMult, ButtonX2Click

Gosub, ~ScrollLock  ; Initialize based on current ScrollLock state.
return

;Key activation support

~ScrollLock::
; Wait for it to be released because otherwise the hook state gets reset
; while the key is down, which causes the up-event to get suppressed,
; which in turn prevents toggling of the ScrollLock state/light:
KeyWait, ScrollLock
GetKeyState, ScrollLockState, ScrollLock, T
If ScrollLockState = D
{
	Hotkey, *u, On
	Hotkey, *NumpadIns, On
	Hotkey, *Numpad5, On
	Hotkey, *i, On
	Hotkey, *NumpadDel, On
	Hotkey, *NumpadDiv, On
	Hotkey, *NumpadMult, On
}
else
{
	Hotkey, *u, Off
	Hotkey, *NumpadIns, Off
	Hotkey, *Numpad5, Off
	Hotkey, *i, Off
	Hotkey, *NumpadDel, Off
	Hotkey, *NumpadDiv, Off
	Hotkey, *NumpadMult, Off
}
return

;Mouse click support

ButtonLeftClick:
GetKeyState, already_down_state, LButton
If already_down_state = D
	return
Button2 = u
ButtonClick = Left
Goto ButtonClickStart
ButtonLeftClickIns:
GetKeyState, already_down_state, LButton
If already_down_state = D
	return
Button2 = NumpadIns
ButtonClick = Left
Goto ButtonClickStart

ButtonMiddleClick:
GetKeyState, already_down_state, MButton
If already_down_state = D
	return
Button2 = Numpad5
ButtonClick = Middle
Goto ButtonClickStart
ButtonMiddleClickClear:
GetKeyState, already_down_state, MButton
If already_down_state = D
	return
Button2 = NumpadClear
ButtonClick = Middle
Goto ButtonClickStart

ButtonRightClick:
GetKeyState, already_down_state, RButton
If already_down_state = D
	return
Button2 = i
ButtonClick = Right
Goto ButtonClickStart
ButtonRightClickDel:
GetKeyState, already_down_state, RButton
If already_down_state = D
	return
Button2 = NumpadDel
ButtonClick = Right
Goto ButtonClickStart

ButtonX1Click:
GetKeyState, already_down_state, XButton1
If already_down_state = D
	return
Button2 = NumpadDiv
ButtonClick = X1
Goto ButtonClickStart

ButtonX2Click:
GetKeyState, already_down_state, XButton2
If already_down_state = D
	return
Button2 = NumpadMult
ButtonClick = X2
Goto ButtonClickStart

ButtonClickStart:
MouseClick, %ButtonClick%,,, 1, 0, D
SetTimer, ButtonClickEnd, 10
return

ButtonClickEnd:
GetKeyState, kclickstate, %Button2%, P
if kclickstate = D
	return

SetTimer, ButtonClickEnd, Off
MouseClick, %ButtonClick%,,, 1, 0, U
return

;Mouse movement support