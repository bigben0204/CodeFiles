Option Explicit
Public Sub dbTest()
    Dim intArray() As Integer
    ReDim intArray(10)
    intArray(0) = 10
    intArray(1) = 11
    ReDim Preserve intArray(1)
    Debug.Print intArray(1)
End Sub
'----------------------------------------------------------------------------------------------------------
Sub Zoo1()
    Dim zoo(3) As String
    Dim i As Integer
    Dim response As String
    i = 0
    Do
        i = i + 1
        response = InputBox("Enter a name of animal:")
        zoo(i) = response
    Loop Until response = ""
End Sub

Sub Zoo2()
    'this procedure avoi ds the error "Subscript out of range"本过程避免“下标越界”错误
    Dim zoo(1 To 3) As String
    Dim i As Integer
    Dim response As String
    i = 1
    Do While i >= LBound(zoo) And i <= UBound(zoo)
        response = InputBox("Enter a name of animal:")
        If response = "" Then Exit Sub
        zoo(i) = response
        i = i + 1
    Loop
    For i = LBound(zoo) To UBound(zoo)
        MsgBox zoo(i)
    Next
End Sub
'----------------------------------------------------------------------------------------------------------
Function Avg(num1, num2, Optional num3)
    Dim totalNums As Integer
    totalNums = 3
    If IsMissing(num3) Then
        num3 = 0
        totalNums = totalNums - 1
    End If
    Avg = (num1 + num2 + num3) / totalNums
End Function
'----------------------------------------------------------------------------------------------------------
Function AddMultipleArgs(ParamArray myNumbers() As Variant) As Single
    Dim mySum As Single
    Dim myValue As Variant
    For Each myValue In myNumbers
       mySum = mySum + myValue
    Next
    AddMultipleArgs = mySum
End Function
'----------------------------------------------------------------------------------------------------------
curdir
curdir$
myDrive = left(CurDir$,1) & ":" 
Name "d:\My Documents\编程\VBA\newdb.txt" As "d:\My Documents\编程\VBA\db.txt"
使用函数Name，你可以将一个文件从一个文件夹移动到另外一个文件夹，但是，你不可以移动文件夹。
在重命名文件之前，你必须关闭该文件。文件名称里不能包含通配符“*”或者“?”。

Debug.Print Dir("d:\My Documents\编程\VBA\", vbNormal)
Dir函数常用来检查某个文件或文件夹是否存在，如果不存在，那么就返回空字符串（””）

Debug.Print Dir("d:\My Documents\编程\VBA\*.pdf", vbNormal)
函数Dir允许你在文件路径名中使用通配符——星号（*）代表多个字符，问号（?）代表单个字符：例如，要在WINDOWS文件夹中查找所有配置设置的文件，你可以查找所有的INI文件，如下： 
?Dir("C:\WINNT\*.ini", vbNormal)

Sub MyFiles()
    Dim mfile As String
    Dim mpath As String
    mpath = InputBox("Enter pathname,e.g., C:\Excel")
    If Right(mpath, 1) <> "\" Then mpath = mpath & "\"
    mfile = Dir(mpath & "*.*")
    If mfile <> "" Then Debug.Print "Files in the " & mpath & "folder"
    Debug.Print LCase$(mfile)
    If mfile = "" Then
        MsgBox "No files found."
        Exit Sub
    End If
    Do While mfile <> ""
        mfile = Dir
        Debug.Print LCase$(mfile)
    Loop
End Sub

Option Explicit
Sub GetFiles()
    Dim nfile As String
    Dim nextRow As Integer   'next row index
    nextRow = 1
    With Worksheets("Sheet1").Range("A1")
        nfile = Dir("d:\My Documents\编程\VBA\", vbNormal)
        .Value = nfile
        Do While nfile <> ""
            nfile = Dir
            .Offset(nextRow, 0).Value = nfile
            nextRow = nextRow + 1
        Loop
    End With
End Sub

Debug.Print FileDateTime("d:\My Documents\编程\VBA\vba学习_home.vb")
Debug.Print DateValue(FileDateTime("d:\My Documents\编程\VBA\vba学习_home.vb"))
Debug.Print TimeValue(FileDateTime("d:\My Documents\编程\VBA\vba学习_home.vb"))
Debug.Print Date
Debug.Print Time
Debug.Print Now

Debug.Print FileLen("d:\My Documents\编程\VBA\VBA编程基础.pdf")

Sub TotalBytesIni()
    Dim iniFile As String
    Dim allBytes As Long
    iniFile = Dir("C:\WINDOWS\*.ini")
    allBytes = 0
    Do While iniFile <> ""
        allBytes = allBytes + FileLen("C:\WINDOWS\" & iniFile)
        iniFile = Dir
    Loop
    Debug.Print "Total bytes: " & allBytes
End Sub

Debug.Print GetAttr("d:\My Documents\编程\VBA\vba学习_home.vb") And vbReadOnly

Sub GetAttributes()
    Dim attr As Integer
    Dim msg As String
    attr = GetAttr("C:\MSDOS.SYS")
    msg = ""
    If attr And vbReadOnly Then msg = msg & "Read-Only (R)"
    If attr And vbHidden Then msg = msg & Chr(10) & "Hidden (H)"
    If attr And vbSystem Then msg = msg & Chr(10) & "System (S)"
    If attr And vbArchive Then msg = msg & Chr(10) & "Archive (A)"
    MsgBox msg, , "MSDOS.SYS"
End Sub

SetAttr "d:\My Documents\编程\VBA\vba学习_home.vb", vbReadOnly + vbArchive

ChDrive "D"
ChDir "d:\My Documents\编程\VBA\"
'----------------------------------------------------------------------------------------------------------
'通过VB工程创建VBA程序，并调用宏过程
Module Module1

    Sub Main()
        Dim vbExcel As Excel.Application
        Dim wb As Workbook
        Dim dstWbPath As string
        vbExcel = Nothing
        wb = Nothing
        dstWbPath = "d:\My Documents\编程\VBA\VBALearning.xlsm"
        'dstWbPath = Command()
        Try
            vbExcel = CreateObject("Excel.Application")
            wb = vbExcel.Workbooks.Open(dstWbPath)
            vbExcel.Run("printMessage")
        Catch ex As Exception
            MsgBox("Error occurs:" & Chr(10) & Chr(10) _
                   & ex.Message)
        Finally
            If Not (wb Is Nothing) Then wb.Close(SaveChanges:=False) : wb = Nothing
            If Not (vbExcel Is Nothing) Then vbExcel.Quit() : vbExcel = Nothing
        End Try
    End Sub

End Module
'----------------------------------------------------------------------------------------------------------
Imports System
Module Module1
    Sub Main()
        System.Console.WriteLine("Hello, world!")
		'Console.WriteLine("Hello, world!")
    End Sub
End Module

Imports System
Module Module1
    Sub Main()
        Console.WriteLine("QuickSort VISUAL BASIC .NET Sample Application")
        Console.WriteLine()
        ' Prompt user for filenames
        Dim szSrcFile, szDestFile As String
        Console.Write("Source: ")
        szSrcFile = Console.ReadLine()
        Console.Write("Output: ")
        szDestFile = Console.ReadLine()
    End Sub
End Module

Imports System
Imports System.Collections
Module Module1
    Sub Main()
        Dim szContents As ArrayList 'Dim szContents As New ArrayList
        szContents = New ArrayList()
        szContents.Add("hello")
        Console.WriteLine(szContents(0))
        Console.WriteLine(szContents.Item(0))
    End Sub
End Module
'----------------------------------------------------------------------------------------------------------
Activecell.CurrentRegion.Select

Rows(1).Select
Rows("1").Select
Rows("1:2").Select
Range("9:9,13:13").Select

Columns(1).Select '用数字选择时不能用"1"
Range("E:F,I:I").Select

Sub selectTest()
    Dim rowString As String
    rowString = "9:10,13:13"
    Range(rowString).Select
End Sub

Sub selectTest()
    Dim rowString As String
    rowString = CStr(1) & ":" & CStr(5)
    Rows(rowString).Select
End Sub
'----------------------------------------------------------------------------------------------------------
Sub selectTest()
    Dim areaCount As Integer, i As Integer, a As Object
    Worksheets("Sheet1").Activate
    areaCount = Selection.Areas.Count
    If areaCount <= 1 Then
        MsgBox "The selection contains " & _
            Selection.Rows.Count & " rows."
    Else
        i = 1
        For Each a In Selection.Areas
            MsgBox "Area " & i & " of the selection contains " & _
                a.Rows.Count & " rows."
            i = i + 1
        Next a
    End If
End Sub

Set mc = Worksheets("Sheet1").Cells(1, 1)
MsgBox mc.Address()                              ' $A$1
MsgBox mc.Address(RowAbsolute:=False)            ' $A1
MsgBox mc.Address(ReferenceStyle:=xlR1C1)        ' R1C1
MsgBox mc.Address(ReferenceStyle:=xlR1C1, _
    RowAbsolute:=False,     _
    ColumnAbsolute:=False,  _
    RelativeTo:=Worksheets(1).Cells(3, 3))        ' R[-2]C[-2]
'------------------------------------------------------------------------------------------------------
'获取列号
Sub calculateColumnName1()
    Dim ColumnName As String
    Dim myCol As Integer
    Dim myRange As Range
    myCol = 55       '指定列标号
    Set myRange = Cells(1, myCol)    '指定该列标号的任意单元格
    ColumnName = Left(myRange.Range("A1").Address(True, False), _
        InStr(1, myRange.Range("A1").Address(True, False), "$", 1) - 1)
    MsgBox "列标号为 " & myCol & " 的单元格的列标字母为 " & ColumnName
    Set myRange = Nothing
End Sub

Sub calculateColumnName2()
    Dim ColumnName As String
    Dim myCol As Integer, k As Integer
    myCol = 55     '指定列标号
    k = (myCol - 1) \ 26 '商数取整
    Select Case k
        Case 0
        Case Else
            ColumnName = ColumnName & Chr(64 + k)
    End Select
    ColumnName = ColumnName & Chr(64 + ((myCol - 1) Mod 26) + 1)'Mod取余
    MsgBox "列标号为 " & myCol & " 的单元格的列标字母为 " & ColumnName
End Sub

Sub calculateColumnName3()
    Dim strArr() As String
    Dim myCol As Integer
    Dim myRange As Range
    myCol = 10
    Set myRange = Cells(1, myCol)
    strArr = Split(myRange.Address(True, False), "$")
    Debug.Print strArr(0)
End Sub

?Chr(64) '@
?Chr(65) 'A

本示例使用 Mod 运算符来对两数作除法运算，但返回其余数而非商数。如果两数中有一数为浮点数，该数会先被四舍五入成整型后再进行运算。

Dim MyResult
MyResult = 10 Mod 5    ' 返回 0。
MyResult = 10 Mod 3    ' 返回 1。
MyResult = 12 Mod 4.3    ' 返回 0。
MyResult = 12.6 Mod 5    ' 返回 3。

Function calculateColumnName(ByRef columnNumber As Integer) As String
    Dim myRange As Range
    Set myRange = Cells(1, columnNumber)    '指定该列标号的任意单元格
    calculateColumnName = Left(myRange.Range("A1").Address(True, False), _
        InStr(1, myRange.Range("A1").Address(True, False), "$", 1) - 1)
    Set myRange = Nothing
End Function
Sub columnNameTest()
    Dim k As Integer
    k = 3
    Dim columnName1 As String, columnName2 As String
    columnName1 = calculateColumnName(k)
    columnName2 = calculateColumnName(k + 1)
    Columns(columnName1 & ":" & columnName2).Select
End Sub
?Columns("BC").Column
'----------------------------------------------------------------------------------------------------------
'对多个单元格的选择操作
Sub selectionTest()
    Dim selectionRange As Range
    For Each selectionRange In Selection
        Debug.Print selectionRange.Address
    Next selectionRange
End Sub
'----------------------------------------------------------------------------------------------------------
'UsedRange
Sub selectionTest()
    Debug.Print ActiveSheet.UsedRange.Rows.Count
    Debug.Print ActiveSheet.UsedRange.Columns.Count
End Sub
'----------------------------------------------------------------------------------------------------------
'用创建Application去调用另一个workbook中的宏，并给该宏传参数
注解 
命名的参数不能使用此方法。参数必须通过位置传递。  
Run 方法返回被调用的宏返回的任何值。如果将对象作为参数传递给宏，该对象将转换为相应的值（通过对该对象应用 Value 属性）。这意味着不能用 Run 方法将对象传递给宏。 

Sub test2()
    Dim app As Application
    Set app = CreateObject("Excel.Application")
    
    Dim wb As Workbook
    Set wb = app.Workbooks.Open("d:\My Documents\编程\VBA\VBALearning2.xlsm")
    
    Call app.Run("test", "good luck")
	'Call app.Run("test", ThisWorkbook.Worksheets("Sheet1").Range("A2")) '将调用Range.Value方法，将值传入
    
    wb.Close False
    
    app.Quit
    Set app = Nothing
End Sub

Sub test(ByRef msg As String)
    MsgBox (msg)
End Sub
'----------------------------------------------------------------------------------------------------------
'VBA101 排序算法，按照C列升序，D列降序
Option Explicit

Sub TestWorksheetSort()
  
  ' Create sample data:
  Range("A1:D1") = Array("Name", "City", "State", "Age")
  Range("A2:D2") = Array("Pam", "Los Angeles", "CA", "16")
  Range("A3:D3") = Array("Jerry", "Boston", "MA", "14")
  Range("A4:D4") = Array("Juanita", "San Francisco", "CA", "15")
  Range("A5:D5") = Array("Xochitl", "Houston", "TX", "11")
  Range("A6:D6") = Array("Jozi", "New York", "NY", "7")
  Range("A7:D7") = Array("Aneka", "Houston", "TX", "18")
  Range("A8:D8") = Array("Brie", "Boston", "MA", "22")
  Range("A9:D9") = Array("Andrew", "Seattle", "WA", "23")
  Range("A10:D10") = Array("Grace", "Boston", "MA", "35")
  Range("A11:D11") = Array("Tom", "Houston", "TX", "12")
 
  ' Retrieve a reference to the used range:
  Dim rng As Range
  Set rng = ActiveSheet.UsedRange
 
  ' Create sort:
  Dim srt As Sort
  ' Include these two lines to make sure you get
  ' IntelliSense help as you work with the Sort object:
  Dim sht As Worksheet
  Set sht = ActiveSheet
  
  Set srt = sht.Sort
  
  ' Sort first by state ascending, and then by age descending.
  srt.SortFields.Clear
  srt.SortFields.Add Key:=Columns("C"), _
   SortOn:=xlSortOnValues, Order:=xlAscending
  srt.SortFields.Add Key:=Columns("D"), _
   SortOn:=xlSortOnValues, Order:=xlDescending
  ' Set the sort range:
  srt.SetRange rng
  srt.Header = xlYes
  srt.MatchCase = True
  ' Apply the sort:
  srt.Apply
End Sub
'----------------------------------------------------------------------------------------------------------
'VBA101 更改颜色以指示大于和小于范围中的平均值的值
Option Explicit

' Demonstrate the AddAboveAverage method
 
Sub TestAboveAverage()
    ' Fill the range with random numbers between
    ' -50 and 50.
    Dim rng As Range
    Set rng = Range("A1", "A20")
    SetupRandomData rng
   
    ' Create a conditional format for values above average.
    Dim aa As AboveAverage
    Set aa = rng.FormatConditions.AddAboveAverage
    aa.AboveBelow = xlAboveAverage
    aa.Font.Bold = True
    aa.Font.Color = vbRed
   
    ' Create a conditional format for values below average.
    Dim ba As AboveAverage
    Set ba = rng.FormatConditions.AddAboveAverage
    ba.AboveBelow = xlBelowAverage
    ba.Font.Color = vbBlue
End Sub
 
Sub SetupRandomData(rng As Range)
    rng.Formula = "=RANDBETWEEN(-50, 50)"
End Sub
'----------------------------------------------------------------------------------------------------------
'关闭所有工作薄
Option Explicit

Sub test()
    Dim wb As Workbook
    For Each wb In Application.Workbooks
        If wb.Name <> ThisWorkbook.Name Then wb.Close False
    Next wb
    ThisWorkbook.Close False
End Sub
'----------------------------------------------------------------------------------------------------------
'以编程方式显示范围中的前 10% 部分
Option Explicit

Sub DemoAddTop10()
  ' Fill a range with random numbers.
  ' Mark the top 10% of items in green, and the bottom
  ' 10% of the items in red.
  
  ' Set up a range, and fill it with random numbers.
  Dim rng As Range
  Set rng = Range("A1:E10")
  SetupRangeData rng
  
  ' Clear any existing format conditions.
  rng.FormatConditions.Delete
  
  ' Set up a condition that formats the top
  ' 10 percent of items on green.
  Dim fc As Top10
  Set fc = rng.FormatConditions.AddTop10
  fc.Percent = True
  fc.TopBottom = xlTop10Top
  fc.Interior.Color = vbGreen
  
  ' Set up a condition that formats the bottom
  ' 10 percent of items in red.
  Set fc = rng.FormatConditions.AddTop10
  fc.TopBottom = xlTop10Bottom
  fc.Percent = True
  fc.Interior.Color = vbRed
End Sub
 
Sub SetupRangeData(rng As Range)
  rng.Formula = "=RANDBETWEEN(1, 100)"
End Sub
'----------------------------------------------------------------------------------------------------------
'是否包含某个Sheet页
Option Explicit

Function containsAsheet(ByRef wb As Workbook, ByRef sheetName As String) As Boolean
    On Error GoTo ErrorHandler
    containsAsheet = True
    Dim sheet As Worksheet
    Set sheet = wb.Worksheets(sheetName)
    'Call wb.Worksheets(sheetName) '这种调用方法无效
    Exit Function
ErrorHandler:
    containsAsheet = False
End Function

Sub test()
    Dim sheetName As String
    sheetName = "Sheet0"
    If containsAsheet(ThisWorkbook, sheetName) Then
        MsgBox ("Contains sheet: " & sheetName)
    Else
        MsgBox ("Not contains sheet: " & sheetName)
    End If
    
    Call containsAsheet(ThisWorkbook, sheetName) '这种有返回值的Function可以用Call直接调用，而不用设置返回值，为什么上面的Worksheets方法不行呢？
End Sub
'----------------------------------------------------------------------------------------------------------
'VBA里没有Continue关键字，用Goto某个标签的方式可以实现循环Continue的效果
Option Explicit

Sub test()
    Dim k As Integer
    For k = 0 To 10
        If k = 3 Then GoTo Label
        Debug.Print k
Label:
    Next k
End Sub
'----------------------------------------------------------------------------------------------------------
'Worksheets的copy/paste方法
Option Explicit

Private Sub test()
    Dim sheet As Worksheet
    Set sheet = ThisWorkbook.Worksheets("Sheet1")
    sheet.Rows(1).Copy
    sheet.Paste Destination:=sheet.Rows("14:15")
End Sub
'----------------------------------------------------------------------------------------------------------
'控件ListView使用代码
Option Explicit

Private Sub BackButton_Click()
    MultiPage1.Value = MultiPage1.Value - 1
End Sub

Private Sub CancelButton_Click()
    Dim Msg As String
    Dim Ans As Integer
    Msg = "Cancel the wizard?"
    Ans = MsgBox(Msg, vbQuestion + vbYesNo, APPNAME)
    If Ans = vbYes Then Unload Me
End Sub

Private Sub NextButton_Click()
    'Debug.Print MultiPage1.Value
    MultiPage1.Value = MultiPage1.Value + 1
    'Debug.Print MultiPage1.Pages.Count
End Sub


Private Sub UserForm_Initialize()

    With ListView1                           '初始化listview
        .ColumnHeaders.Add , , "学号", 60, lvwColumnLeft
        .ColumnHeaders.Add , , "姓名", 60, lvwColumnCenter
        .ColumnHeaders.Add , , "班级", 70, lvwColumnCenter
        .View = lvwReport                                    '以报表的格式显示
        '.LabelEdit = lvwManual                       '使内容不可编辑
        .Gridlines = True
    End With
    
    Dim item As ListItem
    Set item = ListView1.ListItems.Add()
    item.Text = "a"
    item.SubItems(1) = "b"
    item.SubItems(2) = "c"

End Sub
'----------------------------------------------------------------------------------------------------------
'解析Xml代码
Xml中节点的属性值
?node_element
 1 
?node_attribute
 2 
?node_text
 3 

常用的几种类型
MSXML2.DOMDocument ：解析Xml的对象类型，由CreateObject("MSXML2.DOMDocument")创建得到
MSXML2.IXMLDOMNode ：节点，可以在不知道节点类型的前提下用这个变量，只不过特定节点的特定函数无法用这个类型来调用
MSXML2.IXMLDOMElement ：元素节点，有个GetElementsByTagName是IXMLDOMNode没有的
MSXML2.IXMLDOMNodeList ：List节点列表，由元素节点.ChildNodes得到，collection of the node s children
MSXML2.IXMLDOMText ：文本节点
MSXML2.IXMLDOMAttribute ：属性节点
MSXML2.IXMLDOMNamedNodeMap ：属性名称节点映射，由元素节点.Attributes得到，注意与MSXML2.IXMLDOMNodeList区分开来
MSXML2.IXMLDOMParseError ：解析错误类型，由MSXML2.DOMDocument.parseError得到，得到最后一个解析错误
MSXML2.IXMLDOMProcessingInstruction ：XML文件头类型，类似这种<?xml version="1.0" encoding="utf-8"?>

在DOM接口规范中，有四个基本的接口：Document，Node，NodeList以及NamedNodeMap。在这四个基本接口中，Document接口是对文档进行操作的入口，它是从Node接口继承过来的。Node接口是其他大多数接口的父类，象Documet，Element，Attribute，Text，Comment等接口都是从Node接口继承过来的。NodeList接口是一个节点的集合，它包含了某个节点中的所有子节点。NamedNodeMap接口也是一个节点的集合，通过该接口，可以建立节点名和节点之间的一一映射关系，从而利用节点名可以直接访问特定的节点。下面将对这四个接口分别做一些简单的介绍。
1、Document接口
Document接口代表了整个XML/HTML文档，因此，它是整棵文档树的根，提供了对文档中的数据进行访问和操作的入口。
由于元素、文本节点、注释、处理指令等都不能脱离文档的上下文关系而独立存在，所以在Document接口提供了创建其他节点对象的方法，通过该方法创建的节点对象都有一个ownerDocument属性，用来表明当前节点是由谁所创建的以及节点同Document之间的联系。
在DOM树中，Document节点是DOM树中的根节点，也即对XML文档进行操作的入口节点。通过Docuemt节点，可以访问到文档中的其他节点，如处理指令、注释、文档类型以及XML文档的根元素节点等等。另外，在一棵DOM树中，Document节点可以包含多个处理指令、多个注释作为其子节点，而文档类型节点和XML文档根元素节点都是唯一的。
关于Document接口的IDL（Interface Definition Language接口定义语言）定义和其中一些比较常用的属性和方法的详细介绍可以在MSDN中找到。
2、Node接口
Node接口在整个DOM树中具有举足轻重的地位，DOM接口中有很大一部分接口是从Node接口继承过来的，例如，Element、Attr、CDATASection等接口，都是从Node继承过来的。在DOM树中，Node接口代表了树中的一个节点。
3、NodeList接口
NodeList接口提供了对节点集合的抽象定义，它并不包含如何实现这个节点集的定义。NodeList用于表示有顺序关系的一组节点，比如某个节点的子节点序列。另外，它还出现在一些方法的返回值中，例如GetNodeByName。
在DOM中，NodeList的对象是"live"的，换句话说，对文档的改变，会直接反映到相关的NodeList对象中。例如，如果通过DOM获得一个NodeList对象，该对象中包含了某个Element节点的所有子节点的集合，那么，当再通过DOM对Element节点进行操作（添加、删除、改动节点中的子节点）时，这些改变将会自动地反映到NodeList对象中，而不需DOM应用程序再做其他额外的操作。
NodeList中的每个item都可以通过一个索引来访问，该索引值从0开始。
4、NamedNodeMap接口
实现了NamedNodeMap接口的对象中包含了可以通过名字来访问的一组节点的集合。不过注意，NamedNodeMap并不是从NodeList继承过来的，它所包含的节点集中的节点是无序的。尽管这些节点也可以通过索引来进行访问，但这只是提供了枚举NamedNodeMap中所包含节点的一种简单方法，并不表明在DOM规范中为NamedNodeMap中的节点规定了一种排列顺序。
NamedNodeMap表示的是一组节点和其唯一名字的一一对应关系，这个接口主要用在属性节点的表示上。
与NodeList相同，在DOM中，NamedNodeMap对象也是"live"的。

MSXML
从理论上说，根据XML的格式定义，我们可以自己编写一个XML的语法分析器，但实际上微软已经给我们提供了一个XML语法解析器，即一个叫做MSXML.DLL的动态链接库，实际上它是一个COM（Component Object Model）对象库，里面封装了进行XML解析时所需要的所有对象。因为COM是一种以二进制格式出现的和语言无关的可重用对象，所以你可以用任何语言(比如VB，VC，DELPHI，C++ Builder甚至是剧本语言等等)对它进行调用，在你的应用中实现对XML文档的解析。
MSXML.DLL所包括的主要COM接口有：
1. DOMDocument
DOMDocument对象是XML DOM的基础，你可以利用它所暴露的属性和方法来浏览、查询和修改XML文档的内容和结构。DOMDocument表示了树的顶层节点，它实现了DOM文档的所有的基本方法，并且提供了额外的成员函数来支持XSL和XSLT。它创建了一个文档对象，所有其他的对象都可以从这个文档对象中得到和创建。
2. IXMLDOMNode
IXMLDOMNode是文档对象模型(DOM)中的基本对象，元素、属性、注释、过程指令和其他的文档组件都可以认为是IXMLDOMNode。事实上，DOMDocument对象本身也是一个IXMLDOMNode对象。
3. IXMLDOMNodeList
IXMLDOMNodeList实际上是一个节点(Node)对象的集合，节点的增加、删除和变化都可以在集合中立刻反映出来，可以通过"for...next"结构来遍历所有的节点。
4. IXMLDOMParseError
IXMLDOMParseError接口用来返回在解析过程中所出现的详细的信息，包括错误号、行号、字符位置和文本描述。

使用方法：
在具体应用时可以用DOMDocument的Load方法来装载XML文档，用IXMLDOMNode 的selectNodes（查询的结果有多个，得到存放搜索结果的链表）或selectSingleNode（查询的结果有一个，在有多个的情况下返回找到的第一个节点）方法进行查询，用createNode和appendChild方法来创建节点和追加节点，用IXMLDOMElement的setAttribute和getAttribute方法来设置和获得节点的属性。

'样例Xml-1
<?xml version='1.0'?>
<root>
	<person name='张三'>
		<address>深州</address>
		<age>26</age>
	</person>
	<person name='李四'>
		<address>衡水</address>
		<age>25</age>
	</person>
</root>
'样例Xml-2
<?xml version='1.0'?>
<root>
	<person name='张三'>
		<person name='张三儿子'></person>
		<person name='张三女儿'></person>
		<address>深州</address>
		<age>26</age>
	</person>
	<person name='李四'>
		<person name='李四儿子'></person>
		<address>衡水</address>
		<age>25</age>
	</person>
</root>

Option Explicit

Sub parseXml()
    Dim xmlDoc As MSXML2.DOMDocument
    Dim root As MSXML2.IXMLDOMElement, oNodeList As MSXML2.IXMLDOMNodeList, item As MSXML2.IXMLDOMElement
    Dim childs As MSXML2.IXMLDOMNodeList, subitem As MSXML2.IXMLDOMElement
    Set xmlDoc = CreateObject("MSXML2.DOMDocument")
    'xmlDoc.LoadXML ("<?xml version='1.0'?><root><person name='张三'><address>深州</address><age>26</age></person><person name='李四'><address>衡水</address><age>25</age></person></root>") '这是从xml文本中解析
    xmlDoc.Load ("d:\My Documents\编程\VBA\VBA学习代码_home\person2.xml") '这是从xml文件中解析
    
    Dim parseError As MSXML2.IXMLDOMParseError
    Set parseError = xmlDoc.parseError
    If parseError.ErrorCode <> 0 Then '如果解析xml时出错
        Debug.Print parseError.reason
        Debug.Print parseError.Line
        Exit Sub
    End If
    
    Set root = xmlDoc.DocumentElement
    
    Dim nodeWithoutKnowingType As MSXML2.IXMLDOMNode
    Set nodeWithoutKnowingType = root.FirstChild '<person name='张三'>这个节点，类型是NODE_ELEMENT
    Set nodeWithoutKnowingType = nodeWithoutKnowingType.FirstChild '<address>深州</address>这个节点，类型是NODE_ELEMENT
    Set nodeWithoutKnowingType = nodeWithoutKnowingType.FirstChild '深州这个节点，类型是NODE_TEXT，nodeName是“#text”，没有tagName
    
    Dim nodeAttributes As MSXML2.IXMLDOMNamedNodeMap
    Set nodeAttributes = root.FirstChild.Attributes 'NODE_ELEMENT的.Attributes返回类型是IXMLDOMNamedNodeMap
    Set nodeWithoutKnowingType = nodeAttributes.getNamedItem("name") '"name='张三'"这个节点，类型是NODE_ATTRIBUTE。在这个例子中，root.FirstChild.Attributes.nextNode也是得到同样的属性节点NODE_ATTRIBUTE
    Debug.Print nodeAttributes(0).nodeName, nodeAttributes(0).NodeValue 'MSXML2.IXMLDOMNamedNodeMap对象是个collection，可以nodeAttributes.item(0)调用也可以nodeAttributes(0)。可以通过属性名称getNamedItem得到对象，也可以直接通过Index直接得到对象
    
    
    Set oNodeList = root.ChildNodes
    
    Set item = oNodeList.NextNode '从第一个子节点开始遍历所有子节点
    Set item = oNodeList.NextNode
    Set item = oNodeList.NextNode '超出迭代器则返回Nothing
    oNodeList.Reset '重置迭代器
    Set item = oNodeList.NextNode '又得到第一个子节点
    
    Set item = root.ChildNodes(1) '可以直接给ChildNodes加Index来返回第Index + 1的子节点，超出子节点个数则返回Nothing
    
    For Each item In oNodeList
        Debug.Print item.getAttribute("name")
        If IsNull(item.getAttribute("name1")) Then Debug.Print "Not has name1" '对于Null的处理用函数IsNull()来进行判断， 对于Nothing的处理用object is Nothing来进行判断
        Debug.Print item.XML
        Debug.Print item.Text '.text会返回该节点及该节点下的所有子节点Text连接起来生成，而不是单单该节点的Text值
        Set childs = item.ChildNodes
        For Each subitem In childs '当xml为这个时："<?xml version='1.0'?><root><person name='张三'>张三<address>深州</address><age>26</age></person><person name='李四'><address>衡水</address><age>25</age></person></root>"，“张三”这个IXMLDOMText类型也会被取到，这时subitem的类型就无法匹配了
            Debug.Print subitem.BaseName
            Debug.Print subitem.HasChildNodes
            Debug.Print "="
            Debug.Print subitem.Text
        Next
    Next
    
    Dim eachAttribute As MSXML2.IXMLDOMAttribute
    Dim selectNodes As MSXML2.IXMLDOMNodeList
    Set selectNodes = root.selectNodes("person") 'selectNodes如果用标签名来取节点的话，只能用标签取到子节点集合
    For Each item In selectNodes
        Debug.Print item.getAttributeNode("name").NodeValue '等同于item.getAttribute("name")，只不过当传入参数没有该标签名时，getAttribute("name")得到NULL；getAttributeNode("name")得到Nothing，此时再调用.NodeValue会抛异常
        For Each subitem In item.selectNodes("address")
            Debug.Print subitem.Text
        Next subitem
        For Each eachAttribute In item.Attributes
            Debug.Print eachAttribute.nodeName, eachAttribute.NodeValue
        Next eachAttribute
    Next item
    
    Set selectNodes = root.GetElementsByTagName("address") 'GetElementsByTagName 可以用标签取到任意层次节点。这里用selectNodes则取到的对象集合为空
    For Each item In selectNodes
        Debug.Print item.Text
    Next item
    
    Set selectNodes = root.selectNodes("//person/address") 'selectNodes如果想取到多层次子节点集合，则需要指定层次结构，此时同GetElementsByTagName一样
    For Each item In selectNodes
        Debug.Print item.Text
    Next item
    
    Set item = root.SelectSingleNode("//person[1]/address[0]") 'SelectSingleNode，从当前节点开始指定相对路径选定一个特定节点
    Debug.Print item.Text
    
    Set selectNodes = root.GetElementsByTagName("person") 'GetElementsByTagName是将所有此标签的节点全部取出，而不会管这个节点是在第几层中
    For Each item In selectNodes
        Debug.Print item.getAttribute("name")
    Next item
    Set selectNodes = root.selectNodes("//person/person") 'selectNodes明确指出第几层的节点，就可以只取到对应层次的节点
    For Each item In selectNodes
        Debug.Print item.getAttribute("name")
    Next item
    Set selectNodes = root.selectNodes("person") 'selectNodes如果用“//标签名”的话，与GetElementByTagName完全一样。如果只想取到直接子节点，需要用"标签名"
    For Each item In selectNodes
        Debug.Print item.getAttribute("name")
        For Each subitem In item.selectNodes("person")
            Debug.Print subitem.getAttribute("name")
        Next subitem
    Next item
End Sub
'----------------------------------------------------------------------------------------------------------
'[原创]VBA null的判断与使用
VBA程序中null 值出现在if语句中的条件表达式中，结果会是怎样？
IsNull是一个函数，用来判定他的参数是否被赋值过。
如果没有被赋值，该函数返回值为真，赋值过返回值为假。
null在这里不能单独使用，但可以理解为“未定义”、“未赋值”等含义。
'----------------------------------------------------------------------------------------------------------
'生成xml代码，仿照Python的生成xml类写的

'=========类CXmlGenerator开始
Option Explicit

Private xmlDoc_ As DOMDocument

Public Function createNode(ByRef nodeName As String) As IXMLDOMElement
    Set createNode = xmlDoc_.createElement(nodeName)
End Function

Public Sub addNode(ByRef node As IXMLDOMElement, Optional ByRef parentNode As IXMLDOMElement)
    If parentNode Is Nothing Then
        Call xmlDoc_.appendChild(node)
    Else
        Call parentNode.appendChild(node)
    End If
End Sub

Public Sub setNodeAttr(ByRef node As IXMLDOMElement, ByRef attr As String, ByRef value As String)
    Call node.setAttribute(attr, value) '如果没有attr的属性，就新增一个该属性的节点，如果已有该属性节点，则直接重新设置该属性值
    '以下四个语句等于上面的一条语句
'    Dim attrNode As IXMLDOMAttribute
'    Set attrNode = xmlDoc_.createAttribute(attr)
'    attrNode.value = value
'    Call node.setAttributeNode(attrNode)
End Sub

Public Sub setNodeValue(ByRef node As IXMLDOMElement, ByRef value As String)
    node.Text = value
End Sub

Public Sub genXml(ByRef xmlPath As String)
    Dim header As IXMLDOMProcessingInstruction
    Set header = xmlDoc_.createProcessingInstruction("xml", "version='1.0' encoding='utf-8'")
    Call xmlDoc_.InsertBefore(header, xmlDoc_.DocumentElement)'等效于Call xmlDoc_.InsertBefore(header, xmlDoc_.ChildNodes(0))'xmlDoc_.ChildNodes只有一个子节点，即根节点xmlDoc_.ChildNodes(0)
    Call xmlDoc_.Save(xmlPath)
End Sub

Private Sub Class_Initialize()
    Set xmlDoc_ = CreateObject("MSXML2.DOMDocument")
End Sub

Private Sub Class_Terminate()
    Set xmlDoc_ = Nothing
End Sub
'=========类CXmlGenerator结束

Option Explicit

Public Sub xmlTest()
    Dim xmlGenerator As New CXmlGenerator
    Dim node_book_store As IXMLDOMElement
    Set node_book_store = xmlGenerator.createNode("book_store")
    Call xmlGenerator.setNodeAttr(node_book_store, "name", "new hua")
    Call xmlGenerator.setNodeAttr(node_book_store, "website", "http://www.ourunix.org")
    Call xmlGenerator.addNode(node_book_store)
    
    Dim node_book_01 As IXMLDOMElement, node_book_01_Name As IXMLDOMElement, node_book_01_author As IXMLDOMElement
    Dim node_book_01_price As IXMLDOMElement, node_book_01_grade As IXMLDOMElement
    
    'book01
    Set node_book_01 = xmlGenerator.createNode("book")
    Call xmlGenerator.setNodeAttr(node_book_01, "index", "1")
    
    Set node_book_01_Name = xmlGenerator.createNode("name")
    Call xmlGenerator.setNodeValue(node_book_01_Name, "Hamlet")
    Call xmlGenerator.addNode(node_book_01_Name, node_book_01)
    
    Set node_book_01_author = xmlGenerator.createNode("author")
    Call xmlGenerator.setNodeValue(node_book_01_author, "William Shakespeare")
    Call xmlGenerator.addNode(node_book_01_author, node_book_01)
  
    Set node_book_01_price = xmlGenerator.createNode("price")
    Call xmlGenerator.setNodeValue(node_book_01_price, "$20")
    Call xmlGenerator.addNode(node_book_01_price, node_book_01)
  
    Set node_book_01_grade = xmlGenerator.createNode("grade")
    Call xmlGenerator.setNodeValue(node_book_01_grade, "good")
    Call xmlGenerator.addNode(node_book_01_grade, node_book_01)
    
    Call xmlGenerator.addNode(node_book_01, node_book_store)
    
    'book02
    Dim node_book_02 As IXMLDOMElement, node_book_02_Name As IXMLDOMElement, node_book_02_author As IXMLDOMElement
    Dim node_book_02_price As IXMLDOMElement, node_book_02_grade As IXMLDOMElement
    
    Set node_book_02 = xmlGenerator.createNode("book")
    Call xmlGenerator.setNodeAttr(node_book_02, "index", "2")
    'Call xmlGenerator.setNodeAttr(node_book_02, "index", "3")
      
    Set node_book_02_Name = xmlGenerator.createNode("name")
    Call xmlGenerator.setNodeValue(node_book_02_Name, "shuihu")
    Call xmlGenerator.addNode(node_book_02_Name, node_book_02)
      
    Set node_book_02_author = xmlGenerator.createNode("author")
    Call xmlGenerator.setNodeValue(node_book_02_author, "naian shi")
    Call xmlGenerator.addNode(node_book_02_author, node_book_02)
  
    Set node_book_02_price = xmlGenerator.createNode("price")
    Call xmlGenerator.setNodeValue(node_book_02_price, "$200")
    Call xmlGenerator.addNode(node_book_02_price, node_book_02)
  
    Set node_book_02_grade = xmlGenerator.createNode("grade")
    Call xmlGenerator.setNodeValue(node_book_02_grade, "good")
    Call xmlGenerator.addNode(node_book_02_grade, node_book_02)
      
    Call xmlGenerator.addNode(node_book_02, node_book_store)
    
    Call xmlGenerator.genXml("d:\My Documents\编程\VBA\book_store.xml")
End Sub

'====================
'网上找到的最初学习代码，完全客户自己管理所有xml对象，所以最好用上面的CXmlGenerator类来生成Xml
Public Sub makeXml()
    Dim xmlDoc_pvt As DOMDocument, node_book_store As IXMLDOMElement, header As IXMLDOMProcessingInstruction
    Set xmlDoc_pvt = CreateObject("MSXML2.DOMDocument")
    Set node_book_store = xmlDoc_pvt.createElement("HP_Scan_Iterm")
    Set xmlDoc_pvt.DocumentElement = node_book_store
    Set header = xmlDoc_pvt.createProcessingInstruction("xml", "version='1.0' encoding='utf-8'")
    'Call xmlDoc_pvt.InsertBefore(Header, xmlDoc_pvt.ChildNodes(0))'这一句同下句等效，xmlDoc_pvt.ChildNodes只有一个子节点，即根节点xmlDoc_pvt.ChildNodes(0)
    Call xmlDoc_pvt.InsertBefore(header, xmlDoc_pvt.DocumentElement)
    
    Call xmlDoc_pvt.Save(ThisWorkbook.Path & "\HP_Scan_Iterm.xml")
    Set xmlDoc_pvt = Nothing
    
    Call writeSummaryData(ThisWorkbook.Path & "\HP_Scan_Iterm.xml")
    
    Call StrConv(ThisWorkbook.Path & "\HP_Scan_Iterm.xml", vbUnicode) '返回按规定转换的“变量”（“字符串”）。
End Sub

Public Function writeSummaryData(strXMLfileSpec)
    Dim xmlDoc_pvt As DOMDocument, root As IXMLDOMElement
    Dim new_node As IXMLDOMElement, att As IXMLDOMAttribute, parent_Comp As IXMLDOMElement
    Dim maxCol As Integer, colIndex As Integer, sheetCount As Integer, i As Integer
    Dim strTestSum As String, strAtt As String
    
    Set xmlDoc_pvt = CreateObject("MSXML2.DOMDocument")
    Call xmlDoc_pvt.Load(strXMLfileSpec)
    Set root = xmlDoc_pvt.DocumentElement
    maxCol = Worksheets(1).Range("IV1").End(xlToLeft).Column
    For colIndex = 2 To maxCol
        strTestSum = "OS"
        strAtt = Worksheets(1).Cells(1, colIndex).value
        Set new_node = xmlDoc_pvt.createElement(strTestSum) '用一个标签生成一个节点
        Set att = xmlDoc_pvt.createAttribute("Version")
        att.value = strAtt
        Call new_node.setAttributeNode(att)
        'Set parent_Comp = xmlDoc_pvt.DocumentElement.appendChild(new_node)
        'Set parent_Comp = root.appendChild(new_node) '感觉这个parent_Comp就是new_node啊，appendChild会返回新增的孩子节点，可以不设置返回值
        Call root.appendChild(new_node)
        sheetCount = Worksheets.Count
        
        For i = 1 To sheetCount
            Dim tabName As String
            tabName = Worksheets(i).Name
            Call writeArgField(xmlDoc_pvt, new_node, tabName, strAtt)
        Next
    Next
    xmlDoc_pvt.Save strXMLfileSpec
    Set xmlDoc_pvt = Nothing
End Function

Public Sub writeArgField(xmlDoc_pvt, parent_Comp, tabName, deviceName)
    'Create the new Field node.
    Dim new_node As IXMLDOMElement, parent_Step As IXMLDOMElement, MyNode As IXMLDOMElement
    Dim att As IXMLDOMAttribute
    Dim maxRow As Integer, colIndex As Integer, i As Integer
    Dim strTestSum As String, strAtt As String
    
    Set new_node = xmlDoc_pvt.createElement(tabName)
    Set parent_Step = parent_Comp.appendChild(new_node)
    ' Create the sub-Field nodes.
    maxRow = Worksheets(tabName).Range("A65535").End(xlUp).Row
    colIndex = getColNumber(deviceName)
    For i = 2 To maxRow
        
        strTestSum = "Value"
        strAtt = Worksheets(tabName).Cells(i, 1).value
        ' Set Parent = xmlDoc_pvt.DocumentElement.SelectSingleNode(strTestSum)
        Set MyNode = xmlDoc_pvt.createElement(strTestSum)
        Set att = xmlDoc_pvt.createAttribute("Name")
        att.value = strAtt
        Call MyNode.setAttributeNode(att)
        
        MyNode.Text = Worksheets(tabName).Cells(i, colIndex).value
        Call parent_Step.appendChild(MyNode)
    Next
End Sub

Public Function getColNumber(DName)
    Dim maxCol As Integer, CIndex As Integer, i As Integer
    
    maxCol = Worksheets(1).Range("IV1").End(xlToLeft).Column
    CIndex = 1
    For i = 2 To maxCol
        CIndex = CIndex + 1
        If DName = Worksheets(1).Cells(1, i).value Then
        getColNumber = CIndex
        Exit Function
        End If
    Next
End Function
'----------------------------------------------------------------------------------------------------------
'递归解析Xml的样例代码
'样例Xml-1
<Condition>
	<Branch attribute="hello">
		<EnumItem value="h"/>
		<EnumItem value="e"/>
		<Condition>
			<Branch attribute="world">
				<EnumItem value="w"/>
			</Branch>
			<Branch attribute="world">
				<EnumItem value="o"/>
				<Condition>
					<Branch attribute="good">
						<EnumItem value="d"/>
					</Branch>
					<Branch attribute="good">
						<EnumItem value="o"/>
					</Branch>
				</Condition>
			</Branch>
		</Condition>
	</Branch>
</Condition>
'样例Xml-2
<Condition>
	<Branch attribute="hello">
		<EnumItem value="h"/>
		<EnumItem value="e"/>
		<Condition>
			<Branch attribute="world">
				<EnumItem value="w"/>
			</Branch>
			<Branch attribute="world">
				<EnumItem value="o"/>
			</Branch>
		</Condition>
		<Condition>
			<Branch attribute="good">
				<EnumItem value="d"/>
			</Branch>
			<Branch attribute="good">
				<EnumItem value="o"/>
			</Branch>
		</Condition>		
	</Branch>
</Condition>

'CItemFlag类开始===
Option Explicit

Private item_ As String
Private foundFlag_ As Boolean

Public Sub setItem(ByRef item As String)
    item_ = item
End Sub

Public Sub setFoundFlag()
    foundFlag_ = True
End Sub

Public Function getFoundFlag() As Boolean
    getFoundFlag = foundFlag_
End Function

Private Sub Class_Initialize()
    item_ = ""
    foundFlag_ = False
End Sub
'CItemFlag类结束===

'RecursiveBranchModule模块
Option Explicit

Private itemCollection As Collection
Private itemCollectionMatchNumber As Integer

Sub test()
    Dim xmlDoc As DOMDocument
    Set xmlDoc = CreateObject("MSXML2.DOMDocument")
    xmlDoc.Load ("d:\My Documents\编程\VBA\branch.xml")
    
    Dim root As IXMLDOMElement
    Set root = xmlDoc.DocumentElement
    
    Set itemCollection = New Collection
    Call getItemCollection(itemCollection)
    
    'Debug.Print contains(itemCollection, "hello_h")
    
    Dim oneBranchMatchFlag As Boolean, collectionItemsAllFoundFlag As Boolean
    '核查是否每一个分支里都有一个匹配条件在Collection中找到
    oneBranchMatchFlag = checkOneConditionMatch(root)
    '核查是否每一个Collection中的Item都在分支里找到
    collectionItemsAllFoundFlag = getAllFoundFlag(itemCollection)
    Stop
End Sub

Private Function getAllFoundFlag(ByRef itemCollection As Collection) As Boolean
    getAllFoundFlag = True
    Dim itemFlag As CItemFlag
    For Each itemFlag In itemCollection
        If itemFlag.getFoundFlag = False Then
            getAllFoundFlag = False
            Exit Function
        End If
    Next itemFlag
End Function

Private Sub getItemCollection(ByRef itemCollection As Collection)
    Dim v1 As String, v2 As String, v3 As String
    v1 = "hello_h"
    v2 = "world_o"
    v3 = "good_d"
    
    Dim itemFlag1 As New CItemFlag, itemFlag2 As New CItemFlag, itemFlag3 As New CItemFlag
    Call itemFlag1.setItem(v1)
    Call itemFlag2.setItem(v2)
    Call itemFlag3.setItem(v3)
    
    With itemCollection
        .Add itemFlag1, v1
        .Add itemFlag2, v2
        .Add itemFlag3, v3
    End With

'    Dim k As Integer
'    For k = 1 To ThisWorkbook.Worksheets("Sheet1").Range("A65535").End(xlUp).Row
'
'    Next k
End Sub

Private Function contains(ByRef col As Collection, ByRef value As Variant) As Boolean
    On Error GoTo ErrorHandler
    contains = True
    Call col(value)
    Exit Function
ErrorHandler:
    contains = False
End Function

'核查一个分支里的Item是否有匹配对象
Private Function checkOneBranchItemMatch(ByRef rootNode As IXMLDOMElement) As Boolean
    checkOneBranchItemMatch = False
    
    Dim attributeName As String, value As String, key As String
    attributeName = rootNode.getAttribute("attribute")
    Dim enumItemNode As IXMLDOMElement
    For Each enumItemNode In rootNode.selectNodes("EnumItem")
        value = enumItemNode.getAttribute("value")
        key = attributeName & "_" & value
        If contains(itemCollection, key) Then
            Dim itemFlag As CItemFlag
            Set itemFlag = itemCollection(key)
            Call itemFlag.setFoundFlag
            checkOneBranchItemMatch = True
            Exit Function
        End If
    Next enumItemNode
End Function

'核查多个Condition是否同时匹配
Private Function checkAllConditionMatch(ByRef rootNode As IXMLDOMElement) As Boolean
    Dim matchFlag As Boolean, conditionMatchFlag As Boolean
    matchFlag = True
    Dim conditionNode As IXMLDOMElement
    For Each conditionNode In rootNode.selectNodes("Condition")
        conditionMatchFlag = checkOneConditionMatch(conditionNode)
        matchFlag = matchFlag And conditionMatchFlag
    Next conditionNode
    checkAllConditionMatch = matchFlag
End Function

'核查一个分支是否匹配
Private Function checkOneBranchMatch(ByRef rootNode As IXMLDOMElement) As Boolean
    Dim enumItemNode As IXMLDOMElement
    Dim currentBrachEnumItemMatchFlag As Boolean, allConditionMatchFlag As Boolean
    currentBrachEnumItemMatchFlag = checkOneBranchItemMatch(rootNode)
    allConditionMatchFlag = checkAllConditionMatch(rootNode)
    checkOneBranchMatch = currentBrachEnumItemMatchFlag And allConditionMatchFlag
End Function

'检查一个Condition是否匹配
Private Function checkOneConditionMatch(ByRef rootNode As IXMLDOMElement) As Boolean
    Dim matchFlag As Boolean, branchMatchFlag As Boolean
    matchFlag = False
    Dim branchNode As IXMLDOMElement
    For Each branchNode In rootNode.selectNodes("Branch") 'rootNode.ChildNodes
        branchMatchFlag = checkOneBranchMatch(branchNode)
        If branchMatchFlag = True Then
            checkOneConditionMatch = True
            Exit Function
        End If
    Next branchNode
End Function
'----------------------------------------------------------------------------------------------------------
'编写按键程序代码
Private Sub Workbook_SheetActivate(ByVal Sh As Object)
    If Sh.Name = "Sheet1" Then
        Application.OnKey "{DEL}", "OnDel" '设置Delete键的运行程序，原Delete按键功能将丢失
    Else
        Application.OnKey "{DEL}" '恢复Delete按键功能默认
    End If
End Sub

Public Sub OnDel()
    Call MsgBox("Button Delete is pressed.")
End Sub
'----------------------------------------------------------------------------------------------------------
'锁定单元格代码
Public Sub lockTest()
    Dim ws As Worksheet
    Set ws = ThisWorkbook.Worksheets("Sheet1")
    
    ws.Unprotect
    
    ws.Cells.Locked = False
    With ws.Range("A1")
        .Locked = True
        .FormulaHidden = False '跟03的表现不太一样，03表格在这时可以双击锁定单元格，会提示无法编辑。在13格式下，完全无法点击
    End With
    ws.Protect AllowFormattingCells:=True
	
	'ws.ProtectContents 返回布尔值，可以查看是否处于锁定状态
End Sub
'----------------------------------------------------------------------------------------------------------
'用函数查找模糊关键字的公式
=IF(COUNTIF(A1,"*达*")=1,TRUE,FALSE)
'----------------------------------------------------------------------------------------------------------
'导入导出删除模块代码
http://club.excelhome.net/thread-1034490-1-1.html
Option Explicit

'工具-宏-安全性-可靠发行商-（勾选 信任对于“Visual Basic”项目的访问）
'--------------------------------
'导入模块
Sub test()
    Dim C, sPh As String
    Dim s As String
    'sPh = CreateObject("Shell.Application").BrowseForFolder(0, _
            "选择文件夹,程序将该文件夹保存的模块全部导入！", 0, 0).Self.Path
    sPh = "d:\My Documents\编程\VBA\ExportModule"
    For Each C In FileFullName(sPh)
        'Application.VBE.ActiveVBProject.VBComponents当前激活工程的所有VB组件
        'ActiveWorkbook.VBProject.VBComponents当前激活表格的工程所有VB组件
        Application.VBE.ActiveVBProject.VBComponents.Import C
        s = s + vbCrLf + C
    Next
    MsgBox "已导入：" & s
End Sub
'获得导入模块路径名
Function FileFullName(sPath As String) As String()
    Dim iCt As Integer
    Dim TemAr() As String
    Dim sDir As String
    sDir = Dir(sPath & "\" & "*.bas")
    While Len(sDir)
        ReDim Preserve TemAr(iCt)
        TemAr(iCt) = sPath & "\" & sDir
        iCt = iCt + 1
        sDir = Dir
    Wend
    FileFullName = TemAr
End Function

'某工程是否有特定名称的模块
Private Function containsAVbComponent(ByRef someVbProject As VBProject, ByVal moduleName As String) As Boolean
    On Error GoTo ErrorHandler
    containsAVbComponent = True
    Dim certainVbComponent As vbComponent
    Set certainVbComponent = someVbProject.VBComponents(moduleName)
    Exit Function
ErrorHandler:
    containsAVbComponent = False
End Function

Private Sub test2()
    Debug.Print containsAVbComponent(ActiveWorkbook.VBProject, "TestModule")
End Sub


'导出模块
Sub SaveThisModule()
    Dim C, sPh As String
    Dim s As String
    'sPh = CreateObject("Shell.Application").BrowseForFolder(0, _
            "选择文件夹,程序将本工作簿的所有模块导出至该文件夹！", 0, 0).Self.Path
    sPh = "d:\My Documents\编程\VBA\ExportModule"
    '但是C为什么是个String呢？
    For Each C In ThisWorkbook.VBProject.VBComponents
        'ThisWorkbook/Sheet1 Type都是100，模块是1，类是2，窗体是3
        If C.Type = 1 Then
            Application.VBE.ActiveVBProject.VBComponents(C.Name).Export (sPh & "\" & C.Name & ".bas")
            s = s + vbCrLf + C.Name
        End If
    Next
    MsgBox "已导出：" & s
End Sub

'删除宏模块代码
Sub MacroDel()
    Dim vbcCom As Variant, Vbc As Variant
    Set vbcCom = ActiveWorkbook.VBProject.VBComponents
    For Each Vbc In vbcCom
        If Vbc.Name Like "Sheet*" Or Vbc.Name Like "This*" Then
            'DeleteLines(StartLine as Long, [Count as Long = 1])
            Vbc.CodeModule.DeleteLines 1, Vbc.CodeModule.CountOfLines
        Else
            vbcCom.Remove (Vbc)
        End If
    Next Vbc
    'ThisWorkbook.Save
End Sub

'用SendKeys来打开另一个加密工程代码
Sub printModuleName()
    Dim vbp As VBProject
    Dim wb As Workbook
    Set wb = Application.Workbooks("ProjectWithKey.xlsm")
    Set vbp = wb.VBProject
    
    Dim strPassWord As String
    '关闭VBE主窗口
    'Application.VBE.MainWindow.Visible = False '不需要这句
    strPassWord = "123" '密码字符串
    '判断是否设置了工程保护,使用密码打开工程
    If wb.VBProject.Protection = vbext_pp_locked Then
        Application.VBE.CommandBars.FindControl(ID:=2578).Execute '这句有点问题，有时候并不能打开锁定工程的属性，导致发送密码失败
        SendKeys strPassWord & "{ENTER}{ESC}" '到Enter是用密码打开工程，ESC是退出工程属性界面
        DoEvents '更新工程状态
    End If
    
    Dim vbCom As Variant
    For Each vbCom In vbp.VBComponents
        Debug.Print vbCom.Name
    Next vbCom
End Sub

'--------
'用SendKeys来打开另一个加密工程代码
Sub printModuleName()
    On Error GoTo ErrorHandler
    Dim vbp As VBProject
    Dim wb As Workbook
    
    Dim app As Application
    Set app = CreateObject("Excel.Application")
    Set wb = app.Workbooks.Open("d:\My Documents\编程\VBA\ProjectWithKey.xlsm")
    Set vbp = wb.VBProject
    '关闭VBE主窗口
    Dim strPassWord As String
    strPassWord = "123" '密码字符串
    '判断是否设置了工程保护,使用密码打开工程
    'Application.VBE.MainWindow.Visible = False '不需要这句
    Call unlockProject(wb, strPassWord)
    
    Dim vbCom As Variant
    For Each vbCom In vbp.VBComponents
        Debug.Print vbCom.Name
    Next vbCom
    
    wb.Close True
    app.Quit
    Set app = Nothing
    Application.VBE.MainWindow.Visible = True '不需要这句
    Exit Sub
ErrorHandler:
    app.Quit
    Set app = Nothing
    Application.VBE.MainWindow.Visible = True '不需要这句
    Call MsgBox("Error Occurs")
End Sub

Private Sub unlockProject(ByRef wb As Workbook, ByRef strPassWord As String)
    '判断是否设置了工程保护,使用密码打开工程
    If wb.VBProject.Protection = vbext_pp_locked Then
        wb.Parent.VBE.CommandBars.FindControl(ID:=2578).Execute
        SendKeys "%{TAB}", True
        SendKeys "%{TAB}", True
        SendKeys strPassWord & "{ENTER}{TAB}{ENTER}", True '到Enter是用密码打开工程，ESC是退出工程属性界面
        DoEvents '更新工程状态
    End If
End Sub
'----------------------------------------------------------------------------------------------------------
'工程代码行数统计代码
http://www.accessoft.com/article-show.asp?id=4964
6.6 获取部件或模块中代码行信息
6.6.1 获取部件或模块中申明部分行数
'函数功能：获得指定部件或模块中申明部分总代码行数(含注释行及空行)
Public Function TotalDeclLinesInVBComp (CompsNameOrIndex) As Long
   Dim VBProj      As VBProject         '申明工程项目对象
   Dim VBComp    As VBComponent      '申明项目组件对象
   Dim CodeMod    As CodeModule       '申明组件代码
  
   '实例化对象
   Set VBProj = VBE.ActiveVBProject
   Set VBComp = VBProj.VBComponents(CompsNameOrIndex)
   Set CodeMod = VBComp.CodeModule
  
   '获得申明代码行数并输出
   TotalDeclLinesInVBComp = CodeMod.CountOfDeclarationLines
End Function
 
'******************************************************************
'调用示例：获得部件"bas_ProcInfo"模块中申明部分总代码行数
Debug.Print TotalDeclLinesInVBComp ("bas_ProcInfo")
 
 
6.6.2 获得指定模块中总代码行数
'函数功能：获得指定模块中总代码行数(含申明代码行、注释行及空行)
Public Function TotalCodeLinesInVBComp (CompsNameOrIndex) As Long
   Dim VBProj     As VBProject
   Dim VBComp   As VBComponent
   Dim CodeMod   As CodeModule
  
'实例化对象
   Set VBProj = VBE.ActiveVBProject
   Set VBComp = VBProj.VBComponents(CompsNameOrIndex)
   Set CodeMod = VBComp.CodeModule
 
 '获得部件或模块中代码总行数并输出
   TotalCodeLinesInVBComp = CodeMod.CountOfLines
End Function
 
'******************************************************************
'调用示例：获得部件"bas_ProcInfo"模块中总代码行数
Debug.Print TotalCodeLinesInVBComp ("bas_ProcInfo")
 
 
6.6.3 获得指定部件或模块中实际代码行数
'函数功能：获得指定部件或模块代码数。包括申明及代码，但不含注释代码行及空白行
Public Function CodeLinesInVBComp (CompsNameOrIndex) As Long
   Dim VBProj     As VBProject
   Dim VBComp   As VBComponent
   Dim CodeMod   As CodeModule
   Dim I          As Long
   Dim strCode    As String
   Dim LineCount  As Long
  
   '实例化对象
   Set VBProj = VBE.ActiveVBProject
   Set VBComp = VBProj.VBComponents(CompsNameOrIndex)
   Set CodeMod = VBComp.CodeModule
  
   With CodeMod
      '循环每行代码
      For I = 1 To .CountOfLines
         '将代码赋值给字符串变量
         strCode = .Lines(I, 1)
         If Trim (strCode) = vbNullString or Left (Trim (strCode), 1) = Chr (39) Then
           '跳过空行注释行
         Else
            LineCount = LineCount + 1
         End If
      Next I
   End With
   '获取实际代码计数输出
   CodeLinesInVBComp = LineCount
End Function
 
'******************************************************************
'调用示例：获得部件"bas_ProcInfo"模块中实际代码行数
Debug.Print CodeLinesInVBComp ("bas_ProcInfo")
 
 
6.7 获取工程代码行数信息
6.7.1工程总代码行数
'函数功能：工程总代码行数(含空及注释)
'调    用：TotalCodeLinesInVBComp
Public Function TotalCodeLinesInProject () As Long
   Dim VBProj      As VBProject
   Dim VBComp    As VBComponent
   Dim LineCount   As Long
  
   Set VBProj = VBE.ActiveVBProject
  
   '判断工程是否锁定，则退出函数,
   If VBProj.Protection = vbext_pp_locked Then
      TotalCodeLinesInProject = -1
      Exit Function
   End If
  
   '遍历当前工程中所有部件
   For Each VBComp In VBProj.VBComponents
      LineCount = LineCount + TotalCodeLinesInVBComp(VBComp.Name)
   Next VBComp
 
   TotalCodeLinesInProject = LineCount
End Function
 
6.7.2工程实际代码行数
'函数功能：工程实际代码行数(不含空及注释)
'调    用：CodeLinesInVBComp
Public Function CodeLinesInProject() As Long
   Dim VBProj      As VBProject
   Dim VBComp     As VBComponent
   Dim LineCount    As Long
  
   Set VBProj = VBE.ActiveVBProject
  
   '遍历当前工程中所有部件对象
   For Each VBComp In VBProj.VBComponents
      LineCount = LineCount + CodeLinesInVBComp(VBComp.Name)
   Next VBComp
  
   CodeLinesInProject = LineCount
End Function
'----------------------------------------------------------------------------------------------------------
'检测一个VBAProject中的非注释代码是否有中文字符

'删除注释内容，但是保留了注释的引号'
Private Sub eraseAnnotation(ByRef str As String)
    Dim pos As Long
    pos = InStr(str, "'")
    If pos <> 0 Then
        str = Mid(str, 1, pos)
    End If
End Sub

'判断该行是否是空行或者是注释行
Private Function emptyOrAnnotationLine(ByRef str As String) As Boolean
    emptyOrAnnotationLine = False
    If Trim(str) = vbNullString Or Left(Trim(str), 1) = "'" Then
        emptyOrAnnotationLine = True
    End If
End Function

'判断字符串中是否含有中文
Private Function stringWithChinese(ByRef str As String) As Boolean
    stringWithChinese = False
    
    '一个中文占两个字符，如果len长度小于Unicode长度，说明含有中文
    If Len(str) < LenB(StrConv(str, vbFromUnicode)) Then
        stringWithChinese = True
    End If
End Function

Private Sub checkChineseInAModule(ByRef vbc As vbComponent)
    Dim cm As VBIDE.CodeModule
    Set cm = vbc.CodeModule
    
    Dim str As String
    Dim lineNumber As Long
    For lineNumber = 1 To cm.CountOfLines
        str = cm.Lines(lineNumber, 1)
        Call eraseAnnotation(str)
        If Not emptyOrAnnotationLine(str) And stringWithChinese(str) Then
            Debug.Print "ModuleName: " & vbc.name & " <--> ProcedureName: " & cm.ProcOfLine(lineNumber, vbext_pk_Proc) _
                & " <==> " & str
            'Debug.Print "ModuleName: " & vbc.name & " <==> " & str
        End If
    Next lineNumber
End Sub

'检测一个VBAProject中的所有模块中的非注释代码是否含有中文
Private Sub checkChineseInWorkbook(ByRef wb As Workbook)
    Dim vbcCom As VBIDE.VBComponents, vbc As VBIDE.vbComponent
    If wb.VBProject.Protection = vbext_pp_locked Then
        Debug.Print wb.FullName & " can't be checked because the VBAProject is locked."
        Exit Sub
    End If
        
    Set vbcCom = wb.VBProject.VBComponents
    For Each vbc In vbcCom
        Call checkChineseInAModule(vbc)
    Next vbc
End Sub

Private Sub testChinese()
    Call checkChineseInWorkbook(ThisWorkbook)
End Sub

'CodeModule的Proc几个函数
Private Sub testCodeModule()
    Dim vbcCom As VBIDE.VBComponents, vbc As VBIDE.vbComponent
    Set vbcCom = ThisWorkbook.VBProject.VBComponents
    
    Set vbc = vbcCom("TestModule")
    Dim cm As VBIDE.CodeModule
    Set cm = vbc.CodeModule
    
    '一个函数的起始行
    Debug.Print cm.ProcStartLine("test", vbext_pk_Proc)
    '一个函数的所有行数
    Debug.Print cm.ProcCountLines("eraseAnnotation", vbext_pk_Proc)
    '给定一个行数，返回该行所在的函数名称
    Debug.Print cm.ProcOfLine(2, vbext_pk_Proc)
End Sub
'----------------------------------------------------------------------------------------------------------
'清立即窗口代码
Sub ClearImmediate()
    Debug.Print "this is atest"
    With Application.VBE.Windows("立即窗口")
         .Visible = True
         .SetFocus
         VBA.SendKeys "^{a}"
         VBA.SendKeys "{del}"
'        .Visible = False'关闭立即窗口！
    End With
End Sub
'----------------------------------------------------------------------------------------------------------
'VBA中使用正则表达式代码
http://justsee.iteye.com/blog/1468745

Sub getNum1()
    ' 这种使用方式需要"工具""引用"
    ' 引用Microsoft VBScript Regular Expressions 5.5类库
    Dim reg As New RegExp
    With reg
        .Global = True
        .IgnoreCase = True
        .Pattern = "\d+"'提取所有数字
    End With

    Dim mc As MatchCollection
    Dim m As Match
    Set mc = reg.Execute("123aaaaa987uiiui999")
    For Each m In mc
        Debug.Print m
    Next
    
    Debug.Print reg.test("1abc")
End Sub

Sub getNum2()
    Dim arr As Variant
    arr = Split("A12B-R1E2W-E1T-R2T-Q1B2Y3U4D", "-") ' split(字符串,"分隔符")拆分字符串
    MsgBox "arr(0)=" & arr(0) & ";arr(1)=" & arr(1)
    MsgBox Join(arr, ",") ' join(数组,"分隔符")用分隔连接数组的每个元成一个字符串

    Dim i As Integer
    Dim arrStr() As String
    ReDim arrStr(LBound(arr) To UBound(arr)) ' 为动态数组分配存储空间
    
    Dim reg As Object
    Set reg = CreateObject("VBSCRIPT.REGEXP") ' 生成一个正则表达式对象实例
    With reg
        For i = LBound(arr) To UBound(arr)
            .Global = True ' 设置全局可用，即替换所有符合匹配模式的字符串，默认是False，找到一个匹配即停止
            .Pattern = "[^A-Z]" ' 匹配模式为非大写字母
            arrStr(i) = .Replace(arr(i), "") ' 将arr(i)字符串中符合匹配模式的部分替换为空字符
        Next
    End With
    
    Cells.ClearContents
    Cells(1, 1).Resize(UBound(arr) + 1, 1) = Application.WorksheetFunction.Transpose(arrStr())
End Sub



'查看test方法中的字符串是否能和.Pattern中匹配上
Sub getNum1()
    ' 这种使用方式需要"工具""引用"
    ' 引用Microsoft VBScript Regular Expressions 5.5类库
    Dim reg As New RegExp
    With reg
        .Global = True
        .IgnoreCase = True
        .Pattern = "[\^<>]" '任意^ < >字符，这里的\为转义字符，表示后面^是要进行匹配的，而不是不包含
    End With
    
    Debug.Print reg.test("><>abc") 'true
End Sub

Sub getNum2()
    Dim reg As Object
    Set reg = CreateObject("VBSCRIPT.REGEXP") ' 生成一个正则表达式对象实例
    reg.Pattern = "[\^<>]"
    Debug.Print reg.test("&abc")
End Sub

'网上的另一个例子
Private Sub testRegExp()
    '这里有一个例子，
    '1，如果发现了字符“？”就要看该字符后面一个字符是什么
    '1.1 如果下一个字符是大写的A到Z中的任何一个，就要把“？”用4个空格取代
    '1.2 如果下一个字符是空格，就要把“？”用3个空格取代
    '1.3 如果下一个字符不是1.1或者1.2的情况，就要把“？”用2个空格取代
    ' 字符串里的“？”字符的数目是不固定的。
    Debug.Print change("??abc?A? ") '****abc****A***
End Sub

Private Function change(ByVal str As String)
    With CreateObject("vbscript.regexp")
        .Global = True
        .MultiLine = True
        .Pattern = "\?(?=[A-Z])" '\?匹配问号，(?=[A-Z])匹配任意一个或0个是A-Z的字符
        str = .Replace(str, "****")
        .Pattern = "\?(?= )"
        str = .Replace(str, "***")
        str = Replace(str, "?", "**")
        change = str
    End With
End Function
'----------------------------------------------------------------------------------------------------------
'CMap类
Option Explicit

Private keyCol_ As Collection
Private keyValueCol_ As Collection

Public Function hasKey(ByRef key As Variant) As Boolean
    On Error GoTo ErrorHandler
    key = encryptKey(key)
    
    hasKey = True
    Call keyCol_.Item(key)
    Exit Function
ErrorHandler:
    hasKey = False
End Function

Public Sub insert(ByRef key As Variant, ByRef value As Variant)
    key = encryptKey(key)
    
    If hasKey(key) Then
        keyCol_.Remove (key)
        keyValueCol_.Remove (key)
    End If
    
    keyCol_.Add Item:=key, key:=key
    keyValueCol_.Add Item:=value, key:=key
End Sub

Public Function getValue(ByRef key As Variant) As Variant
    On Error GoTo ErrorHandler
    key = encryptKey(key)
    
    If hasKey(key) Then
        getValue = keyValueCol_.Item(key)
    End If
    
    Exit Function
ErrorHandler:
    Set getValue = keyValueCol_.Item(key)
End Function

Public Function isEmpty() As Boolean
    isEmpty = (keyCol_.count = 0)
End Function

Public Function count() As Long
    count = keyCol_.count
End Function

Public Function keyCollection() As Collection
    Set keyCollection = keyCol_
End Function

Public Function valueCollection() As Collection
    Set valueCollection = keyValueCol_
End Function

Private Function encryptKey(ByRef key As Variant) As Variant
    On Error GoTo ErrorHandler
    If IsNumeric(key) Then
        encryptKey = CStr(key)
    Else
        encryptKey = key
    End If
    Exit Function
ErrorHandler:
    Set encryptKey = key
End Function

Private Sub Class_Initialize()
    'Set map_ = CreateObject("Scripting.Dictionary")
    Set keyCol_ = New Collection
    Set keyValueCol_ = New Collection
End Sub

'----------------------------------------------------------------------------------------------------------
'可变参数代码
请看下面函数参数的说明：

[Optional]   [ByVal   |   ByRef]   [ParamArray]   varname[()]   [As   type]   [=   defaultvalue] 
部分   描述   
Optional   可选的。表示参数不是必需的。如果使用了该选项，则   arglist   中的后续参数都必须是可选的，而且必须都使用   Optional   关键字声明。如果使用了   ParamArray，则任何参数都不能使用   Optional   声明。   
ByVal   可选的。表示该参数按值传递。   
ByRef   可选的。表示该参数按地址传递。ByRef   是   Visual   Basic   的缺省选项。   
ParamArray   可选的。只用于   arglist   的最后一个参数，指明最后这个参数是一个   Variant   元素的   Optional   数组。使用   ParamArray   关键字可以提供任意数目的参数。ParamArray   关键字不能与   ByVal，ByRef，或   Optional   一起使用。   
varname   必需的。代表参数的变量的名称；遵循标准的变量命名约定。   
type   可选的。传递给该过程的参数的数据类型；可以是   Byte、Boolean、Integer、Long、Currency、Single、Double、Decimal（目前尚不支持）、Date、 String（只支持变长）、Object   或   Variant。如果参数不是   Optional，则也可以是用户定义类型，或对象类型。   
defaultvalue   可选的。任何常数或常数表达式。只对于   Optional   参数时是合法的。如果类型为   Object，则显式缺省值只能是   Nothing。  

'可变参数
Public Function SUM_Customized(ParamArray vals() As Variant) As Double
    Dim val As Variant
    Dim sum As Double
    sum = 0
    For Each val In vals
        sum = sum + val
    Next
    SUM_Customized = sum
End Function
'----------------------------------------------------------------------------------------------------------
'Excel 中用 VBA 字典查找代替 VLOOKUP http://my.oschina.net/leejun2005/blog/294348
Timer 函数
返回一个 Single，代表从午夜开始到现在经过的秒数。

'=Application.VLookup(Range("A2"),Range("C:D"),2,0)
Sub VLOOKUP_Customized()
'
' 在机器表上生成一级分中心 Macro
'
    Application.Calculation = xlCalculationManual
    Application.ScreenUpdating = False
    
    '
    Dim t0 As Single
    t0 = Timer
    
    ' 词典
    Dim map_dict As Object
    Set map_dict = CreateObject("Scripting.Dictionary")
    
    ' 打开分中心映射表
    Dim map_sheet As Worksheet
    Set map_sheet = Worksheets("分中心映射表")
    ' map_nrows = map_sheet.Range("A:A").End(xlUp).Row
    ' Set my_rows = map_sheet.Range("A2" & map_nrows).Rows
    
    Dim my_rows As Range
    Set my_rows = map_sheet.UsedRange.Rows
     
    Dim my_row As Variant
    Dim center As Long
    Dim city As String
    ' 遍历分中心映射表，获得 分中心 对应的一级分中心，插入词典
    For Each my_row In my_rows
        center = my_row.Cells(1, 1).Value
        city = my_row.Cells(1, 2).Value
        If Not map_dict.Exists(center) Then
            map_dict.Add center, city
        End If
    Next my_row
     
    ' 打开机器表
    Dim dispatch_sheet As Worksheet
    Set dispatch_sheet = Worksheets("机器表")
    
    Dim dispatch_nrows As Long
    dispatch_nrows = dispatch_sheet.Range("a99999").End(xlUp).Row

    Set my_rows = dispatch_sheet.Range("a1:b" & dispatch_nrows).Rows
    
    Dim o_row As Variant
    ' 遍历开通表，通过词典获得 machine_id 对应的一级分中心，插入开通表
    For Each o_row In my_rows
       center = o_row.Cells(1, 2).Value
       o_row.Cells(1, 3).Value = map_dict.Item(center)
    Next o_row
     
    MsgBox "在机器表上生成一级分中心。共处理 " & dispatch_nrows & " 条记录，总耗时" & Timer - t0 & "秒。"
     
    ' 销毁建立的词典
    Set map_dict = Nothing
     
    ' 打开自动计算和屏幕刷新
    Application.Calculation = xlCalculationAutomatic
    Application.ScreenUpdating = True
End Sub

'----------------------------------------------------------------------------------------------------------
'IF函数使用
博达	=IF(A1="*达*","True","False")
world	o
	
40	=IF(A4="40",TRUE,FALSE)
'----------------------------------------------------------------------------------------------------------
'自定义函数代码
'定义成Public，才可以在函数里关联显示，定义成Private，可以用，但是无法关联
Public Function AREA_Customized(ByRef a As Double, ByRef b As Double) As Double
    AREA_Customized = a * b / 2
End Function
'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

'----------------------------------------------------------------------------------------------------------

