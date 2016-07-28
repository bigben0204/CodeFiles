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
    'this procedure avoi ds the error "Subscript out of range"�����̱��⡰�±�Խ�硱����
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
Name "d:\My Documents\���\VBA\newdb.txt" As "d:\My Documents\���\VBA\db.txt"
ʹ�ú���Name������Խ�һ���ļ���һ���ļ����ƶ�������һ���ļ��У����ǣ��㲻�����ƶ��ļ��С�
���������ļ�֮ǰ�������رո��ļ����ļ������ﲻ�ܰ���ͨ�����*�����ߡ�?����

Debug.Print Dir("d:\My Documents\���\VBA\", vbNormal)
Dir�������������ĳ���ļ����ļ����Ƿ���ڣ���������ڣ���ô�ͷ��ؿ��ַ�����������

Debug.Print Dir("d:\My Documents\���\VBA\*.pdf", vbNormal)
����Dir���������ļ�·������ʹ��ͨ��������Ǻţ�*���������ַ����ʺţ�?���������ַ������磬Ҫ��WINDOWS�ļ����в��������������õ��ļ�������Բ������е�INI�ļ������£� 
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
        nfile = Dir("d:\My Documents\���\VBA\", vbNormal)
        .Value = nfile
        Do While nfile <> ""
            nfile = Dir
            .Offset(nextRow, 0).Value = nfile
            nextRow = nextRow + 1
        Loop
    End With
End Sub

Debug.Print FileDateTime("d:\My Documents\���\VBA\vbaѧϰ_home.vb")
Debug.Print DateValue(FileDateTime("d:\My Documents\���\VBA\vbaѧϰ_home.vb"))
Debug.Print TimeValue(FileDateTime("d:\My Documents\���\VBA\vbaѧϰ_home.vb"))
Debug.Print Date
Debug.Print Time
Debug.Print Now

Debug.Print FileLen("d:\My Documents\���\VBA\VBA��̻���.pdf")

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

Debug.Print GetAttr("d:\My Documents\���\VBA\vbaѧϰ_home.vb") And vbReadOnly

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

SetAttr "d:\My Documents\���\VBA\vbaѧϰ_home.vb", vbReadOnly + vbArchive

ChDrive "D"
ChDir "d:\My Documents\���\VBA\"
'----------------------------------------------------------------------------------------------------------
'ͨ��VB���̴���VBA���򣬲����ú����
Module Module1

    Sub Main()
        Dim vbExcel As Excel.Application
        Dim wb As Workbook
        Dim dstWbPath As string
        vbExcel = Nothing
        wb = Nothing
        dstWbPath = "d:\My Documents\���\VBA\VBALearning.xlsm"
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

Columns(1).Select '������ѡ��ʱ������"1"
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
'��ȡ�к�
Sub calculateColumnName1()
    Dim ColumnName As String
    Dim myCol As Integer
    Dim myRange As Range
    myCol = 55       'ָ���б��
    Set myRange = Cells(1, myCol)    'ָ�����б�ŵ����ⵥԪ��
    ColumnName = Left(myRange.Range("A1").Address(True, False), _
        InStr(1, myRange.Range("A1").Address(True, False), "$", 1) - 1)
    MsgBox "�б��Ϊ " & myCol & " �ĵ�Ԫ����б���ĸΪ " & ColumnName
    Set myRange = Nothing
End Sub

Sub calculateColumnName2()
    Dim ColumnName As String
    Dim myCol As Integer, k As Integer
    myCol = 55     'ָ���б��
    k = (myCol - 1) \ 26 '����ȡ��
    Select Case k
        Case 0
        Case Else
            ColumnName = ColumnName & Chr(64 + k)
    End Select
    ColumnName = ColumnName & Chr(64 + ((myCol - 1) Mod 26) + 1)'Modȡ��
    MsgBox "�б��Ϊ " & myCol & " �ĵ�Ԫ����б���ĸΪ " & ColumnName
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

��ʾ��ʹ�� Mod ����������������������㣬���������������������������������һ��Ϊ���������������ȱ�������������ͺ��ٽ������㡣

Dim MyResult
MyResult = 10 Mod 5    ' ���� 0��
MyResult = 10 Mod 3    ' ���� 1��
MyResult = 12 Mod 4.3    ' ���� 0��
MyResult = 12.6 Mod 5    ' ���� 3��

Function calculateColumnName(ByRef columnNumber As Integer) As String
    Dim myRange As Range
    Set myRange = Cells(1, columnNumber)    'ָ�����б�ŵ����ⵥԪ��
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
'�Զ����Ԫ���ѡ�����
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
'�ô���Applicationȥ������һ��workbook�еĺ꣬�����ú괫����
ע�� 
�����Ĳ�������ʹ�ô˷�������������ͨ��λ�ô��ݡ�  
Run �������ر����õĺ귵�ص��κ�ֵ�������������Ϊ�������ݸ��꣬�ö���ת��Ϊ��Ӧ��ֵ��ͨ���Ըö���Ӧ�� Value ���ԣ�������ζ�Ų����� Run ���������󴫵ݸ��ꡣ 

Sub test2()
    Dim app As Application
    Set app = CreateObject("Excel.Application")
    
    Dim wb As Workbook
    Set wb = app.Workbooks.Open("d:\My Documents\���\VBA\VBALearning2.xlsm")
    
    Call app.Run("test", "good luck")
	'Call app.Run("test", ThisWorkbook.Worksheets("Sheet1").Range("A2")) '������Range.Value��������ֵ����
    
    wb.Close False
    
    app.Quit
    Set app = Nothing
End Sub

Sub test(ByRef msg As String)
    MsgBox (msg)
End Sub
'----------------------------------------------------------------------------------------------------------
'VBA101 �����㷨������C������D�н���
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
'VBA101 ������ɫ��ָʾ���ں�С�ڷ�Χ�е�ƽ��ֵ��ֵ
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
'�ر����й�����
Option Explicit

Sub test()
    Dim wb As Workbook
    For Each wb In Application.Workbooks
        If wb.Name <> ThisWorkbook.Name Then wb.Close False
    Next wb
    ThisWorkbook.Close False
End Sub
'----------------------------------------------------------------------------------------------------------
'�Ա�̷�ʽ��ʾ��Χ�е�ǰ 10% ����
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
'�Ƿ����ĳ��Sheetҳ
Option Explicit

Function containsAsheet(ByRef wb As Workbook, ByRef sheetName As String) As Boolean
    On Error GoTo ErrorHandler
    containsAsheet = True
    Dim sheet As Worksheet
    Set sheet = wb.Worksheets(sheetName)
    'Call wb.Worksheets(sheetName) '���ֵ��÷�����Ч
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
    
    Call containsAsheet(ThisWorkbook, sheetName) '�����з���ֵ��Function������Callֱ�ӵ��ã����������÷���ֵ��Ϊʲô�����Worksheets���������أ�
End Sub
'----------------------------------------------------------------------------------------------------------
'VBA��û��Continue�ؼ��֣���Gotoĳ����ǩ�ķ�ʽ����ʵ��ѭ��Continue��Ч��
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
'Worksheets��copy/paste����
Option Explicit

Private Sub test()
    Dim sheet As Worksheet
    Set sheet = ThisWorkbook.Worksheets("Sheet1")
    sheet.Rows(1).Copy
    sheet.Paste Destination:=sheet.Rows("14:15")
End Sub
'----------------------------------------------------------------------------------------------------------
'�ؼ�ListViewʹ�ô���
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

    With ListView1                           '��ʼ��listview
        .ColumnHeaders.Add , , "ѧ��", 60, lvwColumnLeft
        .ColumnHeaders.Add , , "����", 60, lvwColumnCenter
        .ColumnHeaders.Add , , "�༶", 70, lvwColumnCenter
        .View = lvwReport                                    '�Ա���ĸ�ʽ��ʾ
        '.LabelEdit = lvwManual                       'ʹ���ݲ��ɱ༭
        .Gridlines = True
    End With
    
    Dim item As ListItem
    Set item = ListView1.ListItems.Add()
    item.Text = "a"
    item.SubItems(1) = "b"
    item.SubItems(2) = "c"

End Sub
'----------------------------------------------------------------------------------------------------------
'����Xml����
Xml�нڵ������ֵ
?node_element
 1 
?node_attribute
 2 
?node_text
 3 

���õļ�������
MSXML2.DOMDocument ������Xml�Ķ������ͣ���CreateObject("MSXML2.DOMDocument")�����õ�
MSXML2.IXMLDOMNode ���ڵ㣬�����ڲ�֪���ڵ����͵�ǰ���������������ֻ�����ض��ڵ���ض������޷����������������
MSXML2.IXMLDOMElement ��Ԫ�ؽڵ㣬�и�GetElementsByTagName��IXMLDOMNodeû�е�
MSXML2.IXMLDOMNodeList ��List�ڵ��б���Ԫ�ؽڵ�.ChildNodes�õ���collection of the node s children
MSXML2.IXMLDOMText ���ı��ڵ�
MSXML2.IXMLDOMAttribute �����Խڵ�
MSXML2.IXMLDOMNamedNodeMap ���������ƽڵ�ӳ�䣬��Ԫ�ؽڵ�.Attributes�õ���ע����MSXML2.IXMLDOMNodeList���ֿ���
MSXML2.IXMLDOMParseError �������������ͣ���MSXML2.DOMDocument.parseError�õ����õ����һ����������
MSXML2.IXMLDOMProcessingInstruction ��XML�ļ�ͷ���ͣ���������<?xml version="1.0" encoding="utf-8"?>

��DOM�ӿڹ淶�У����ĸ������Ľӿڣ�Document��Node��NodeList�Լ�NamedNodeMap�������ĸ������ӿ��У�Document�ӿ��Ƕ��ĵ����в�������ڣ����Ǵ�Node�ӿڼ̳й����ġ�Node�ӿ�������������ӿڵĸ��࣬��Documet��Element��Attribute��Text��Comment�Ƚӿڶ��Ǵ�Node�ӿڼ̳й����ġ�NodeList�ӿ���һ���ڵ�ļ��ϣ���������ĳ���ڵ��е������ӽڵ㡣NamedNodeMap�ӿ�Ҳ��һ���ڵ�ļ��ϣ�ͨ���ýӿڣ����Խ����ڵ����ͽڵ�֮���һһӳ���ϵ���Ӷ����ýڵ�������ֱ�ӷ����ض��Ľڵ㡣���潫�����ĸ��ӿڷֱ���һЩ�򵥵Ľ��ܡ�
1��Document�ӿ�
Document�ӿڴ���������XML/HTML�ĵ�����ˣ����������ĵ����ĸ����ṩ�˶��ĵ��е����ݽ��з��ʺͲ�������ڡ�
����Ԫ�ء��ı��ڵ㡢ע�͡�����ָ��ȶ����������ĵ��������Ĺ�ϵ���������ڣ�������Document�ӿ��ṩ�˴��������ڵ����ķ�����ͨ���÷��������Ľڵ������һ��ownerDocument���ԣ�����������ǰ�ڵ�����˭���������Լ��ڵ�ͬDocument֮�����ϵ��
��DOM���У�Document�ڵ���DOM���еĸ��ڵ㣬Ҳ����XML�ĵ����в�������ڽڵ㡣ͨ��Docuemt�ڵ㣬���Է��ʵ��ĵ��е������ڵ㣬�紦��ָ�ע�͡��ĵ������Լ�XML�ĵ��ĸ�Ԫ�ؽڵ�ȵȡ����⣬��һ��DOM���У�Document�ڵ���԰����������ָ����ע����Ϊ���ӽڵ㣬���ĵ����ͽڵ��XML�ĵ���Ԫ�ؽڵ㶼��Ψһ�ġ�
����Document�ӿڵ�IDL��Interface Definition Language�ӿڶ������ԣ����������һЩ�Ƚϳ��õ����Ժͷ�������ϸ���ܿ�����MSDN���ҵ���
2��Node�ӿ�
Node�ӿ�������DOM���о��о������صĵ�λ��DOM�ӿ����кܴ�һ���ֽӿ��Ǵ�Node�ӿڼ̳й����ģ����磬Element��Attr��CDATASection�Ƚӿڣ����Ǵ�Node�̳й����ġ���DOM���У�Node�ӿڴ��������е�һ���ڵ㡣
3��NodeList�ӿ�
NodeList�ӿ��ṩ�˶Խڵ㼯�ϵĳ����壬�������������ʵ������ڵ㼯�Ķ��塣NodeList���ڱ�ʾ��˳���ϵ��һ��ڵ㣬����ĳ���ڵ���ӽڵ����С����⣬����������һЩ�����ķ���ֵ�У�����GetNodeByName��
��DOM�У�NodeList�Ķ�����"live"�ģ����仰˵�����ĵ��ĸı䣬��ֱ�ӷ�ӳ����ص�NodeList�����С����磬���ͨ��DOM���һ��NodeList���󣬸ö����а�����ĳ��Element�ڵ�������ӽڵ�ļ��ϣ���ô������ͨ��DOM��Element�ڵ���в�������ӡ�ɾ�����Ķ��ڵ��е��ӽڵ㣩ʱ����Щ�ı佫���Զ��ط�ӳ��NodeList�����У�������DOMӦ�ó���������������Ĳ�����
NodeList�е�ÿ��item������ͨ��һ�����������ʣ�������ֵ��0��ʼ��
4��NamedNodeMap�ӿ�
ʵ����NamedNodeMap�ӿڵĶ����а����˿���ͨ�����������ʵ�һ��ڵ�ļ��ϡ�����ע�⣬NamedNodeMap�����Ǵ�NodeList�̳й����ģ����������Ľڵ㼯�еĽڵ�������ġ�������Щ�ڵ�Ҳ����ͨ�����������з��ʣ�����ֻ���ṩ��ö��NamedNodeMap���������ڵ��һ�ּ򵥷���������������DOM�淶��ΪNamedNodeMap�еĽڵ�涨��һ������˳��
NamedNodeMap��ʾ����һ��ڵ����Ψһ���ֵ�һһ��Ӧ��ϵ������ӿ���Ҫ�������Խڵ�ı�ʾ�ϡ�
��NodeList��ͬ����DOM�У�NamedNodeMap����Ҳ��"live"�ġ�

MSXML
��������˵������XML�ĸ�ʽ���壬���ǿ����Լ���дһ��XML���﷨����������ʵ����΢���Ѿ��������ṩ��һ��XML�﷨����������һ������MSXML.DLL�Ķ�̬���ӿ⣬ʵ��������һ��COM��Component Object Model������⣬�����װ�˽���XML����ʱ����Ҫ�����ж�����ΪCOM��һ���Զ����Ƹ�ʽ���ֵĺ������޹صĿ����ö���������������κ�����(����VB��VC��DELPHI��C++ Builder�����Ǿ籾���Եȵ�)�������е��ã������Ӧ����ʵ�ֶ�XML�ĵ��Ľ�����
MSXML.DLL����������ҪCOM�ӿ��У�
1. DOMDocument
DOMDocument������XML DOM�Ļ��������������������¶�����Ժͷ������������ѯ���޸�XML�ĵ������ݺͽṹ��DOMDocument��ʾ�����Ķ���ڵ㣬��ʵ����DOM�ĵ������еĻ��������������ṩ�˶���ĳ�Ա������֧��XSL��XSLT����������һ���ĵ��������������Ķ��󶼿��Դ�����ĵ������еõ��ʹ�����
2. IXMLDOMNode
IXMLDOMNode���ĵ�����ģ��(DOM)�еĻ�������Ԫ�ء����ԡ�ע�͡�����ָ����������ĵ������������Ϊ��IXMLDOMNode����ʵ�ϣ�DOMDocument������Ҳ��һ��IXMLDOMNode����
3. IXMLDOMNodeList
IXMLDOMNodeListʵ������һ���ڵ�(Node)����ļ��ϣ��ڵ�����ӡ�ɾ���ͱ仯�������ڼ��������̷�ӳ����������ͨ��"for...next"�ṹ���������еĽڵ㡣
4. IXMLDOMParseError
IXMLDOMParseError�ӿ����������ڽ��������������ֵ���ϸ����Ϣ����������š��кš��ַ�λ�ú��ı�������

ʹ�÷�����
�ھ���Ӧ��ʱ������DOMDocument��Load������װ��XML�ĵ�����IXMLDOMNode ��selectNodes����ѯ�Ľ���ж�����õ�������������������selectSingleNode����ѯ�Ľ����һ�������ж��������·����ҵ��ĵ�һ���ڵ㣩�������в�ѯ����createNode��appendChild�����������ڵ��׷�ӽڵ㣬��IXMLDOMElement��setAttribute��getAttribute���������úͻ�ýڵ�����ԡ�

'����Xml-1
<?xml version='1.0'?>
<root>
	<person name='����'>
		<address>����</address>
		<age>26</age>
	</person>
	<person name='����'>
		<address>��ˮ</address>
		<age>25</age>
	</person>
</root>
'����Xml-2
<?xml version='1.0'?>
<root>
	<person name='����'>
		<person name='��������'></person>
		<person name='����Ů��'></person>
		<address>����</address>
		<age>26</age>
	</person>
	<person name='����'>
		<person name='���Ķ���'></person>
		<address>��ˮ</address>
		<age>25</age>
	</person>
</root>

Option Explicit

Sub parseXml()
    Dim xmlDoc As MSXML2.DOMDocument
    Dim root As MSXML2.IXMLDOMElement, oNodeList As MSXML2.IXMLDOMNodeList, item As MSXML2.IXMLDOMElement
    Dim childs As MSXML2.IXMLDOMNodeList, subitem As MSXML2.IXMLDOMElement
    Set xmlDoc = CreateObject("MSXML2.DOMDocument")
    'xmlDoc.LoadXML ("<?xml version='1.0'?><root><person name='����'><address>����</address><age>26</age></person><person name='����'><address>��ˮ</address><age>25</age></person></root>") '���Ǵ�xml�ı��н���
    xmlDoc.Load ("d:\My Documents\���\VBA\VBAѧϰ����_home\person2.xml") '���Ǵ�xml�ļ��н���
    
    Dim parseError As MSXML2.IXMLDOMParseError
    Set parseError = xmlDoc.parseError
    If parseError.ErrorCode <> 0 Then '�������xmlʱ����
        Debug.Print parseError.reason
        Debug.Print parseError.Line
        Exit Sub
    End If
    
    Set root = xmlDoc.DocumentElement
    
    Dim nodeWithoutKnowingType As MSXML2.IXMLDOMNode
    Set nodeWithoutKnowingType = root.FirstChild '<person name='����'>����ڵ㣬������NODE_ELEMENT
    Set nodeWithoutKnowingType = nodeWithoutKnowingType.FirstChild '<address>����</address>����ڵ㣬������NODE_ELEMENT
    Set nodeWithoutKnowingType = nodeWithoutKnowingType.FirstChild '��������ڵ㣬������NODE_TEXT��nodeName�ǡ�#text����û��tagName
    
    Dim nodeAttributes As MSXML2.IXMLDOMNamedNodeMap
    Set nodeAttributes = root.FirstChild.Attributes 'NODE_ELEMENT��.Attributes����������IXMLDOMNamedNodeMap
    Set nodeWithoutKnowingType = nodeAttributes.getNamedItem("name") '"name='����'"����ڵ㣬������NODE_ATTRIBUTE������������У�root.FirstChild.Attributes.nextNodeҲ�ǵõ�ͬ�������Խڵ�NODE_ATTRIBUTE
    Debug.Print nodeAttributes(0).nodeName, nodeAttributes(0).NodeValue 'MSXML2.IXMLDOMNamedNodeMap�����Ǹ�collection������nodeAttributes.item(0)����Ҳ����nodeAttributes(0)������ͨ����������getNamedItem�õ�����Ҳ����ֱ��ͨ��Indexֱ�ӵõ�����
    
    
    Set oNodeList = root.ChildNodes
    
    Set item = oNodeList.NextNode '�ӵ�һ���ӽڵ㿪ʼ���������ӽڵ�
    Set item = oNodeList.NextNode
    Set item = oNodeList.NextNode '�����������򷵻�Nothing
    oNodeList.Reset '���õ�����
    Set item = oNodeList.NextNode '�ֵõ���һ���ӽڵ�
    
    Set item = root.ChildNodes(1) '����ֱ�Ӹ�ChildNodes��Index�����ص�Index + 1���ӽڵ㣬�����ӽڵ�����򷵻�Nothing
    
    For Each item In oNodeList
        Debug.Print item.getAttribute("name")
        If IsNull(item.getAttribute("name1")) Then Debug.Print "Not has name1" '����Null�Ĵ����ú���IsNull()�������жϣ� ����Nothing�Ĵ�����object is Nothing�������ж�
        Debug.Print item.XML
        Debug.Print item.Text '.text�᷵�ظýڵ㼰�ýڵ��µ������ӽڵ�Text�����������ɣ������ǵ����ýڵ��Textֵ
        Set childs = item.ChildNodes
        For Each subitem In childs '��xmlΪ���ʱ��"<?xml version='1.0'?><root><person name='����'>����<address>����</address><age>26</age></person><person name='����'><address>��ˮ</address><age>25</age></person></root>"�������������IXMLDOMText����Ҳ�ᱻȡ������ʱsubitem�����;��޷�ƥ����
            Debug.Print subitem.BaseName
            Debug.Print subitem.HasChildNodes
            Debug.Print "="
            Debug.Print subitem.Text
        Next
    Next
    
    Dim eachAttribute As MSXML2.IXMLDOMAttribute
    Dim selectNodes As MSXML2.IXMLDOMNodeList
    Set selectNodes = root.selectNodes("person") 'selectNodes����ñ�ǩ����ȡ�ڵ�Ļ���ֻ���ñ�ǩȡ���ӽڵ㼯��
    For Each item In selectNodes
        Debug.Print item.getAttributeNode("name").NodeValue '��ͬ��item.getAttribute("name")��ֻ�������������û�иñ�ǩ��ʱ��getAttribute("name")�õ�NULL��getAttributeNode("name")�õ�Nothing����ʱ�ٵ���.NodeValue�����쳣
        For Each subitem In item.selectNodes("address")
            Debug.Print subitem.Text
        Next subitem
        For Each eachAttribute In item.Attributes
            Debug.Print eachAttribute.nodeName, eachAttribute.NodeValue
        Next eachAttribute
    Next item
    
    Set selectNodes = root.GetElementsByTagName("address") 'GetElementsByTagName �����ñ�ǩȡ�������νڵ㡣������selectNodes��ȡ���Ķ��󼯺�Ϊ��
    For Each item In selectNodes
        Debug.Print item.Text
    Next item
    
    Set selectNodes = root.selectNodes("//person/address") 'selectNodes�����ȡ�������ӽڵ㼯�ϣ�����Ҫָ����νṹ����ʱͬGetElementsByTagNameһ��
    For Each item In selectNodes
        Debug.Print item.Text
    Next item
    
    Set item = root.SelectSingleNode("//person[1]/address[0]") 'SelectSingleNode���ӵ�ǰ�ڵ㿪ʼָ�����·��ѡ��һ���ض��ڵ�
    Debug.Print item.Text
    
    Set selectNodes = root.GetElementsByTagName("person") 'GetElementsByTagName�ǽ����д˱�ǩ�Ľڵ�ȫ��ȡ���������������ڵ����ڵڼ�����
    For Each item In selectNodes
        Debug.Print item.getAttribute("name")
    Next item
    Set selectNodes = root.selectNodes("//person/person") 'selectNodes��ȷָ���ڼ���Ľڵ㣬�Ϳ���ֻȡ����Ӧ��εĽڵ�
    For Each item In selectNodes
        Debug.Print item.getAttribute("name")
    Next item
    Set selectNodes = root.selectNodes("person") 'selectNodes����á�//��ǩ�����Ļ�����GetElementByTagName��ȫһ�������ֻ��ȡ��ֱ���ӽڵ㣬��Ҫ��"��ǩ��"
    For Each item In selectNodes
        Debug.Print item.getAttribute("name")
        For Each subitem In item.selectNodes("person")
            Debug.Print subitem.getAttribute("name")
        Next subitem
    Next item
End Sub
'----------------------------------------------------------------------------------------------------------
'[ԭ��]VBA null���ж���ʹ��
VBA������null ֵ������if����е��������ʽ�У��������������
IsNull��һ�������������ж����Ĳ����Ƿ񱻸�ֵ����
���û�б���ֵ���ú�������ֵΪ�棬��ֵ������ֵΪ�١�
null�����ﲻ�ܵ���ʹ�ã����������Ϊ��δ���塱����δ��ֵ���Ⱥ��塣
'----------------------------------------------------------------------------------------------------------
'����xml���룬����Python������xml��д��

'=========��CXmlGenerator��ʼ
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
    Call node.setAttribute(attr, value) '���û��attr�����ԣ�������һ�������ԵĽڵ㣬������и����Խڵ㣬��ֱ���������ø�����ֵ
    '�����ĸ������������һ�����
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
    Call xmlDoc_.InsertBefore(header, xmlDoc_.DocumentElement)'��Ч��Call xmlDoc_.InsertBefore(header, xmlDoc_.ChildNodes(0))'xmlDoc_.ChildNodesֻ��һ���ӽڵ㣬�����ڵ�xmlDoc_.ChildNodes(0)
    Call xmlDoc_.Save(xmlPath)
End Sub

Private Sub Class_Initialize()
    Set xmlDoc_ = CreateObject("MSXML2.DOMDocument")
End Sub

Private Sub Class_Terminate()
    Set xmlDoc_ = Nothing
End Sub
'=========��CXmlGenerator����

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
    
    Call xmlGenerator.genXml("d:\My Documents\���\VBA\book_store.xml")
End Sub

'====================
'�����ҵ������ѧϰ���룬��ȫ�ͻ��Լ���������xml������������������CXmlGenerator��������Xml
Public Sub makeXml()
    Dim xmlDoc_pvt As DOMDocument, node_book_store As IXMLDOMElement, header As IXMLDOMProcessingInstruction
    Set xmlDoc_pvt = CreateObject("MSXML2.DOMDocument")
    Set node_book_store = xmlDoc_pvt.createElement("HP_Scan_Iterm")
    Set xmlDoc_pvt.DocumentElement = node_book_store
    Set header = xmlDoc_pvt.createProcessingInstruction("xml", "version='1.0' encoding='utf-8'")
    'Call xmlDoc_pvt.InsertBefore(Header, xmlDoc_pvt.ChildNodes(0))'��һ��ͬ�¾��Ч��xmlDoc_pvt.ChildNodesֻ��һ���ӽڵ㣬�����ڵ�xmlDoc_pvt.ChildNodes(0)
    Call xmlDoc_pvt.InsertBefore(header, xmlDoc_pvt.DocumentElement)
    
    Call xmlDoc_pvt.Save(ThisWorkbook.Path & "\HP_Scan_Iterm.xml")
    Set xmlDoc_pvt = Nothing
    
    Call writeSummaryData(ThisWorkbook.Path & "\HP_Scan_Iterm.xml")
    
    Call StrConv(ThisWorkbook.Path & "\HP_Scan_Iterm.xml", vbUnicode) '���ذ��涨ת���ġ������������ַ���������
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
        Set new_node = xmlDoc_pvt.createElement(strTestSum) '��һ����ǩ����һ���ڵ�
        Set att = xmlDoc_pvt.createAttribute("Version")
        att.value = strAtt
        Call new_node.setAttributeNode(att)
        'Set parent_Comp = xmlDoc_pvt.DocumentElement.appendChild(new_node)
        'Set parent_Comp = root.appendChild(new_node) '�о����parent_Comp����new_node����appendChild�᷵�������ĺ��ӽڵ㣬���Բ����÷���ֵ
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
'�ݹ����Xml����������
'����Xml-1
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
'����Xml-2
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

'CItemFlag�࿪ʼ===
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
'CItemFlag�����===

'RecursiveBranchModuleģ��
Option Explicit

Private itemCollection As Collection
Private itemCollectionMatchNumber As Integer

Sub test()
    Dim xmlDoc As DOMDocument
    Set xmlDoc = CreateObject("MSXML2.DOMDocument")
    xmlDoc.Load ("d:\My Documents\���\VBA\branch.xml")
    
    Dim root As IXMLDOMElement
    Set root = xmlDoc.DocumentElement
    
    Set itemCollection = New Collection
    Call getItemCollection(itemCollection)
    
    'Debug.Print contains(itemCollection, "hello_h")
    
    Dim oneBranchMatchFlag As Boolean, collectionItemsAllFoundFlag As Boolean
    '�˲��Ƿ�ÿһ����֧�ﶼ��һ��ƥ��������Collection���ҵ�
    oneBranchMatchFlag = checkOneConditionMatch(root)
    '�˲��Ƿ�ÿһ��Collection�е�Item���ڷ�֧���ҵ�
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

'�˲�һ����֧���Item�Ƿ���ƥ�����
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

'�˲���Condition�Ƿ�ͬʱƥ��
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

'�˲�һ����֧�Ƿ�ƥ��
Private Function checkOneBranchMatch(ByRef rootNode As IXMLDOMElement) As Boolean
    Dim enumItemNode As IXMLDOMElement
    Dim currentBrachEnumItemMatchFlag As Boolean, allConditionMatchFlag As Boolean
    currentBrachEnumItemMatchFlag = checkOneBranchItemMatch(rootNode)
    allConditionMatchFlag = checkAllConditionMatch(rootNode)
    checkOneBranchMatch = currentBrachEnumItemMatchFlag And allConditionMatchFlag
End Function

'���һ��Condition�Ƿ�ƥ��
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
'��д�����������
Private Sub Workbook_SheetActivate(ByVal Sh As Object)
    If Sh.Name = "Sheet1" Then
        Application.OnKey "{DEL}", "OnDel" '����Delete�������г���ԭDelete�������ܽ���ʧ
    Else
        Application.OnKey "{DEL}" '�ָ�Delete��������Ĭ��
    End If
End Sub

Public Sub OnDel()
    Call MsgBox("Button Delete is pressed.")
End Sub
'----------------------------------------------------------------------------------------------------------
'������Ԫ�����
Public Sub lockTest()
    Dim ws As Worksheet
    Set ws = ThisWorkbook.Worksheets("Sheet1")
    
    ws.Unprotect
    
    ws.Cells.Locked = False
    With ws.Range("A1")
        .Locked = True
        .FormulaHidden = False '��03�ı��ֲ�̫һ����03�������ʱ����˫��������Ԫ�񣬻���ʾ�޷��༭����13��ʽ�£���ȫ�޷����
    End With
    ws.Protect AllowFormattingCells:=True
	
	'ws.ProtectContents ���ز���ֵ�����Բ鿴�Ƿ�������״̬
End Sub
'----------------------------------------------------------------------------------------------------------
'�ú�������ģ���ؼ��ֵĹ�ʽ
=IF(COUNTIF(A1,"*��*")=1,TRUE,FALSE)
'----------------------------------------------------------------------------------------------------------
'���뵼��ɾ��ģ�����
http://club.excelhome.net/thread-1034490-1-1.html
Option Explicit

'����-��-��ȫ��-�ɿ�������-����ѡ ���ζ��ڡ�Visual Basic����Ŀ�ķ��ʣ�
'--------------------------------
'����ģ��
Sub test()
    Dim C, sPh As String
    Dim s As String
    'sPh = CreateObject("Shell.Application").BrowseForFolder(0, _
            "ѡ���ļ���,���򽫸��ļ��б����ģ��ȫ�����룡", 0, 0).Self.Path
    sPh = "d:\My Documents\���\VBA\ExportModule"
    For Each C In FileFullName(sPh)
        'Application.VBE.ActiveVBProject.VBComponents��ǰ����̵�����VB���
        'ActiveWorkbook.VBProject.VBComponents��ǰ������Ĺ�������VB���
        Application.VBE.ActiveVBProject.VBComponents.Import C
        s = s + vbCrLf + C
    Next
    MsgBox "�ѵ��룺" & s
End Sub
'��õ���ģ��·����
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

'ĳ�����Ƿ����ض����Ƶ�ģ��
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


'����ģ��
Sub SaveThisModule()
    Dim C, sPh As String
    Dim s As String
    'sPh = CreateObject("Shell.Application").BrowseForFolder(0, _
            "ѡ���ļ���,���򽫱�������������ģ�鵼�������ļ��У�", 0, 0).Self.Path
    sPh = "d:\My Documents\���\VBA\ExportModule"
    '����CΪʲô�Ǹ�String�أ�
    For Each C In ThisWorkbook.VBProject.VBComponents
        'ThisWorkbook/Sheet1 Type����100��ģ����1������2��������3
        If C.Type = 1 Then
            Application.VBE.ActiveVBProject.VBComponents(C.Name).Export (sPh & "\" & C.Name & ".bas")
            s = s + vbCrLf + C.Name
        End If
    Next
    MsgBox "�ѵ�����" & s
End Sub

'ɾ����ģ�����
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

'��SendKeys������һ�����ܹ��̴���
Sub printModuleName()
    Dim vbp As VBProject
    Dim wb As Workbook
    Set wb = Application.Workbooks("ProjectWithKey.xlsm")
    Set vbp = wb.VBProject
    
    Dim strPassWord As String
    '�ر�VBE������
    'Application.VBE.MainWindow.Visible = False '����Ҫ���
    strPassWord = "123" '�����ַ���
    '�ж��Ƿ������˹��̱���,ʹ������򿪹���
    If wb.VBProject.Protection = vbext_pp_locked Then
        Application.VBE.CommandBars.FindControl(ID:=2578).Execute '����е����⣬��ʱ�򲢲��ܴ��������̵����ԣ����·�������ʧ��
        SendKeys strPassWord & "{ENTER}{ESC}" '��Enter��������򿪹��̣�ESC���˳��������Խ���
        DoEvents '���¹���״̬
    End If
    
    Dim vbCom As Variant
    For Each vbCom In vbp.VBComponents
        Debug.Print vbCom.Name
    Next vbCom
End Sub

'--------
'��SendKeys������һ�����ܹ��̴���
Sub printModuleName()
    On Error GoTo ErrorHandler
    Dim vbp As VBProject
    Dim wb As Workbook
    
    Dim app As Application
    Set app = CreateObject("Excel.Application")
    Set wb = app.Workbooks.Open("d:\My Documents\���\VBA\ProjectWithKey.xlsm")
    Set vbp = wb.VBProject
    '�ر�VBE������
    Dim strPassWord As String
    strPassWord = "123" '�����ַ���
    '�ж��Ƿ������˹��̱���,ʹ������򿪹���
    'Application.VBE.MainWindow.Visible = False '����Ҫ���
    Call unlockProject(wb, strPassWord)
    
    Dim vbCom As Variant
    For Each vbCom In vbp.VBComponents
        Debug.Print vbCom.Name
    Next vbCom
    
    wb.Close True
    app.Quit
    Set app = Nothing
    Application.VBE.MainWindow.Visible = True '����Ҫ���
    Exit Sub
ErrorHandler:
    app.Quit
    Set app = Nothing
    Application.VBE.MainWindow.Visible = True '����Ҫ���
    Call MsgBox("Error Occurs")
End Sub

Private Sub unlockProject(ByRef wb As Workbook, ByRef strPassWord As String)
    '�ж��Ƿ������˹��̱���,ʹ������򿪹���
    If wb.VBProject.Protection = vbext_pp_locked Then
        wb.Parent.VBE.CommandBars.FindControl(ID:=2578).Execute
        SendKeys "%{TAB}", True
        SendKeys "%{TAB}", True
        SendKeys strPassWord & "{ENTER}{TAB}{ENTER}", True '��Enter��������򿪹��̣�ESC���˳��������Խ���
        DoEvents '���¹���״̬
    End If
End Sub
'----------------------------------------------------------------------------------------------------------
'���̴�������ͳ�ƴ���
http://www.accessoft.com/article-show.asp?id=4964
6.6 ��ȡ������ģ���д�������Ϣ
6.6.1 ��ȡ������ģ����������������
'�������ܣ����ָ��������ģ�������������ܴ�������(��ע���м�����)
Public Function TotalDeclLinesInVBComp (CompsNameOrIndex) As Long
   Dim VBProj      As VBProject         '����������Ŀ����
   Dim VBComp    As VBComponent      '������Ŀ�������
   Dim CodeMod    As CodeModule       '�����������
  
   'ʵ��������
   Set VBProj = VBE.ActiveVBProject
   Set VBComp = VBProj.VBComponents(CompsNameOrIndex)
   Set CodeMod = VBComp.CodeModule
  
   '��������������������
   TotalDeclLinesInVBComp = CodeMod.CountOfDeclarationLines
End Function
 
'******************************************************************
'����ʾ������ò���"bas_ProcInfo"ģ�������������ܴ�������
Debug.Print TotalDeclLinesInVBComp ("bas_ProcInfo")
 
 
6.6.2 ���ָ��ģ�����ܴ�������
'�������ܣ����ָ��ģ�����ܴ�������(�����������С�ע���м�����)
Public Function TotalCodeLinesInVBComp (CompsNameOrIndex) As Long
   Dim VBProj     As VBProject
   Dim VBComp   As VBComponent
   Dim CodeMod   As CodeModule
  
'ʵ��������
   Set VBProj = VBE.ActiveVBProject
   Set VBComp = VBProj.VBComponents(CompsNameOrIndex)
   Set CodeMod = VBComp.CodeModule
 
 '��ò�����ģ���д��������������
   TotalCodeLinesInVBComp = CodeMod.CountOfLines
End Function
 
'******************************************************************
'����ʾ������ò���"bas_ProcInfo"ģ�����ܴ�������
Debug.Print TotalCodeLinesInVBComp ("bas_ProcInfo")
 
 
6.6.3 ���ָ��������ģ����ʵ�ʴ�������
'�������ܣ����ָ��������ģ����������������������룬������ע�ʹ����м��հ���
Public Function CodeLinesInVBComp (CompsNameOrIndex) As Long
   Dim VBProj     As VBProject
   Dim VBComp   As VBComponent
   Dim CodeMod   As CodeModule
   Dim I          As Long
   Dim strCode    As String
   Dim LineCount  As Long
  
   'ʵ��������
   Set VBProj = VBE.ActiveVBProject
   Set VBComp = VBProj.VBComponents(CompsNameOrIndex)
   Set CodeMod = VBComp.CodeModule
  
   With CodeMod
      'ѭ��ÿ�д���
      For I = 1 To .CountOfLines
         '�����븳ֵ���ַ�������
         strCode = .Lines(I, 1)
         If Trim (strCode) = vbNullString or Left (Trim (strCode), 1) = Chr (39) Then
           '��������ע����
         Else
            LineCount = LineCount + 1
         End If
      Next I
   End With
   '��ȡʵ�ʴ���������
   CodeLinesInVBComp = LineCount
End Function
 
'******************************************************************
'����ʾ������ò���"bas_ProcInfo"ģ����ʵ�ʴ�������
Debug.Print CodeLinesInVBComp ("bas_ProcInfo")
 
 
6.7 ��ȡ���̴���������Ϣ
6.7.1�����ܴ�������
'�������ܣ������ܴ�������(���ռ�ע��)
'��    �ã�TotalCodeLinesInVBComp
Public Function TotalCodeLinesInProject () As Long
   Dim VBProj      As VBProject
   Dim VBComp    As VBComponent
   Dim LineCount   As Long
  
   Set VBProj = VBE.ActiveVBProject
  
   '�жϹ����Ƿ����������˳�����,
   If VBProj.Protection = vbext_pp_locked Then
      TotalCodeLinesInProject = -1
      Exit Function
   End If
  
   '������ǰ���������в���
   For Each VBComp In VBProj.VBComponents
      LineCount = LineCount + TotalCodeLinesInVBComp(VBComp.Name)
   Next VBComp
 
   TotalCodeLinesInProject = LineCount
End Function
 
6.7.2����ʵ�ʴ�������
'�������ܣ�����ʵ�ʴ�������(�����ռ�ע��)
'��    �ã�CodeLinesInVBComp
Public Function CodeLinesInProject() As Long
   Dim VBProj      As VBProject
   Dim VBComp     As VBComponent
   Dim LineCount    As Long
  
   Set VBProj = VBE.ActiveVBProject
  
   '������ǰ���������в�������
   For Each VBComp In VBProj.VBComponents
      LineCount = LineCount + CodeLinesInVBComp(VBComp.Name)
   Next VBComp
  
   CodeLinesInProject = LineCount
End Function
'----------------------------------------------------------------------------------------------------------
'���һ��VBAProject�еķ�ע�ʹ����Ƿ��������ַ�

'ɾ��ע�����ݣ����Ǳ�����ע�͵�����'
Private Sub eraseAnnotation(ByRef str As String)
    Dim pos As Long
    pos = InStr(str, "'")
    If pos <> 0 Then
        str = Mid(str, 1, pos)
    End If
End Sub

'�жϸ����Ƿ��ǿ��л�����ע����
Private Function emptyOrAnnotationLine(ByRef str As String) As Boolean
    emptyOrAnnotationLine = False
    If Trim(str) = vbNullString Or Left(Trim(str), 1) = "'" Then
        emptyOrAnnotationLine = True
    End If
End Function

'�ж��ַ������Ƿ�������
Private Function stringWithChinese(ByRef str As String) As Boolean
    stringWithChinese = False
    
    'һ������ռ�����ַ������len����С��Unicode���ȣ�˵����������
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

'���һ��VBAProject�е�����ģ���еķ�ע�ʹ����Ƿ�������
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

'CodeModule��Proc��������
Private Sub testCodeModule()
    Dim vbcCom As VBIDE.VBComponents, vbc As VBIDE.vbComponent
    Set vbcCom = ThisWorkbook.VBProject.VBComponents
    
    Set vbc = vbcCom("TestModule")
    Dim cm As VBIDE.CodeModule
    Set cm = vbc.CodeModule
    
    'һ����������ʼ��
    Debug.Print cm.ProcStartLine("test", vbext_pk_Proc)
    'һ����������������
    Debug.Print cm.ProcCountLines("eraseAnnotation", vbext_pk_Proc)
    '����һ�����������ظ������ڵĺ�������
    Debug.Print cm.ProcOfLine(2, vbext_pk_Proc)
End Sub
'----------------------------------------------------------------------------------------------------------
'���������ڴ���
Sub ClearImmediate()
    Debug.Print "this is atest"
    With Application.VBE.Windows("��������")
         .Visible = True
         .SetFocus
         VBA.SendKeys "^{a}"
         VBA.SendKeys "{del}"
'        .Visible = False'�ر��������ڣ�
    End With
End Sub
'----------------------------------------------------------------------------------------------------------
'VBA��ʹ��������ʽ����
http://justsee.iteye.com/blog/1468745

Sub getNum1()
    ' ����ʹ�÷�ʽ��Ҫ"����""����"
    ' ����Microsoft VBScript Regular Expressions 5.5���
    Dim reg As New RegExp
    With reg
        .Global = True
        .IgnoreCase = True
        .Pattern = "\d+"'��ȡ��������
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
    arr = Split("A12B-R1E2W-E1T-R2T-Q1B2Y3U4D", "-") ' split(�ַ���,"�ָ���")����ַ���
    MsgBox "arr(0)=" & arr(0) & ";arr(1)=" & arr(1)
    MsgBox Join(arr, ",") ' join(����,"�ָ���")�÷ָ����������ÿ��Ԫ��һ���ַ���

    Dim i As Integer
    Dim arrStr() As String
    ReDim arrStr(LBound(arr) To UBound(arr)) ' Ϊ��̬�������洢�ռ�
    
    Dim reg As Object
    Set reg = CreateObject("VBSCRIPT.REGEXP") ' ����һ��������ʽ����ʵ��
    With reg
        For i = LBound(arr) To UBound(arr)
            .Global = True ' ����ȫ�ֿ��ã����滻���з���ƥ��ģʽ���ַ�����Ĭ����False���ҵ�һ��ƥ�伴ֹͣ
            .Pattern = "[^A-Z]" ' ƥ��ģʽΪ�Ǵ�д��ĸ
            arrStr(i) = .Replace(arr(i), "") ' ��arr(i)�ַ����з���ƥ��ģʽ�Ĳ����滻Ϊ���ַ�
        Next
    End With
    
    Cells.ClearContents
    Cells(1, 1).Resize(UBound(arr) + 1, 1) = Application.WorksheetFunction.Transpose(arrStr())
End Sub



'�鿴test�����е��ַ����Ƿ��ܺ�.Pattern��ƥ����
Sub getNum1()
    ' ����ʹ�÷�ʽ��Ҫ"����""����"
    ' ����Microsoft VBScript Regular Expressions 5.5���
    Dim reg As New RegExp
    With reg
        .Global = True
        .IgnoreCase = True
        .Pattern = "[\^<>]" '����^ < >�ַ��������\Ϊת���ַ�����ʾ����^��Ҫ����ƥ��ģ������ǲ�����
    End With
    
    Debug.Print reg.test("><>abc") 'true
End Sub

Sub getNum2()
    Dim reg As Object
    Set reg = CreateObject("VBSCRIPT.REGEXP") ' ����һ��������ʽ����ʵ��
    reg.Pattern = "[\^<>]"
    Debug.Print reg.test("&abc")
End Sub

'���ϵ���һ������
Private Sub testRegExp()
    '������һ�����ӣ�
    '1������������ַ���������Ҫ�����ַ�����һ���ַ���ʲô
    '1.1 �����һ���ַ��Ǵ�д��A��Z�е��κ�һ������Ҫ�ѡ�������4���ո�ȡ��
    '1.2 �����һ���ַ��ǿո񣬾�Ҫ�ѡ�������3���ո�ȡ��
    '1.3 �����һ���ַ�����1.1����1.2���������Ҫ�ѡ�������2���ո�ȡ��
    ' �ַ�����ġ������ַ�����Ŀ�ǲ��̶��ġ�
    Debug.Print change("??abc?A? ") '****abc****A***
End Sub

Private Function change(ByVal str As String)
    With CreateObject("vbscript.regexp")
        .Global = True
        .MultiLine = True
        .Pattern = "\?(?=[A-Z])" '\?ƥ���ʺţ�(?=[A-Z])ƥ������һ����0����A-Z���ַ�
        str = .Replace(str, "****")
        .Pattern = "\?(?= )"
        str = .Replace(str, "***")
        str = Replace(str, "?", "**")
        change = str
    End With
End Function
'----------------------------------------------------------------------------------------------------------
'CMap��
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
'�ɱ��������
�뿴���溯��������˵����

[Optional]   [ByVal   |   ByRef]   [ParamArray]   varname[()]   [As   type]   [=   defaultvalue] 
����   ����   
Optional   ��ѡ�ġ���ʾ�������Ǳ���ġ����ʹ���˸�ѡ���   arglist   �еĺ��������������ǿ�ѡ�ģ����ұ��붼ʹ��   Optional   �ؼ������������ʹ����   ParamArray�����κβ���������ʹ��   Optional   ������   
ByVal   ��ѡ�ġ���ʾ�ò�����ֵ���ݡ�   
ByRef   ��ѡ�ġ���ʾ�ò�������ַ���ݡ�ByRef   ��   Visual   Basic   ��ȱʡѡ�   
ParamArray   ��ѡ�ġ�ֻ����   arglist   �����һ��������ָ��������������һ��   Variant   Ԫ�ص�   Optional   ���顣ʹ��   ParamArray   �ؼ��ֿ����ṩ������Ŀ�Ĳ�����ParamArray   �ؼ��ֲ�����   ByVal��ByRef����   Optional   һ��ʹ�á�   
varname   ����ġ���������ı��������ƣ���ѭ��׼�ı�������Լ����   
type   ��ѡ�ġ����ݸ��ù��̵Ĳ������������ͣ�������   Byte��Boolean��Integer��Long��Currency��Single��Double��Decimal��Ŀǰ�в�֧�֣���Date�� String��ֻ֧�ֱ䳤����Object   ��   Variant�������������   Optional����Ҳ�������û��������ͣ���������͡�   
defaultvalue   ��ѡ�ġ��κγ����������ʽ��ֻ����   Optional   ����ʱ�ǺϷ��ġ��������Ϊ   Object������ʽȱʡֵֻ����   Nothing��  

'�ɱ����
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
'Excel ���� VBA �ֵ���Ҵ��� VLOOKUP http://my.oschina.net/leejun2005/blog/294348
Timer ����
����һ�� Single���������ҹ��ʼ�����ھ�����������

'=Application.VLookup(Range("A2"),Range("C:D"),2,0)
Sub VLOOKUP_Customized()
'
' �ڻ�����������һ�������� Macro
'
    Application.Calculation = xlCalculationManual
    Application.ScreenUpdating = False
    
    '
    Dim t0 As Single
    t0 = Timer
    
    ' �ʵ�
    Dim map_dict As Object
    Set map_dict = CreateObject("Scripting.Dictionary")
    
    ' �򿪷�����ӳ���
    Dim map_sheet As Worksheet
    Set map_sheet = Worksheets("������ӳ���")
    ' map_nrows = map_sheet.Range("A:A").End(xlUp).Row
    ' Set my_rows = map_sheet.Range("A2" & map_nrows).Rows
    
    Dim my_rows As Range
    Set my_rows = map_sheet.UsedRange.Rows
     
    Dim my_row As Variant
    Dim center As Long
    Dim city As String
    ' ����������ӳ������ ������ ��Ӧ��һ�������ģ�����ʵ�
    For Each my_row In my_rows
        center = my_row.Cells(1, 1).Value
        city = my_row.Cells(1, 2).Value
        If Not map_dict.Exists(center) Then
            map_dict.Add center, city
        End If
    Next my_row
     
    ' �򿪻�����
    Dim dispatch_sheet As Worksheet
    Set dispatch_sheet = Worksheets("������")
    
    Dim dispatch_nrows As Long
    dispatch_nrows = dispatch_sheet.Range("a99999").End(xlUp).Row

    Set my_rows = dispatch_sheet.Range("a1:b" & dispatch_nrows).Rows
    
    Dim o_row As Variant
    ' ������ͨ��ͨ���ʵ��� machine_id ��Ӧ��һ�������ģ����뿪ͨ��
    For Each o_row In my_rows
       center = o_row.Cells(1, 2).Value
       o_row.Cells(1, 3).Value = map_dict.Item(center)
    Next o_row
     
    MsgBox "�ڻ�����������һ�������ġ������� " & dispatch_nrows & " ����¼���ܺ�ʱ" & Timer - t0 & "�롣"
     
    ' ���ٽ����Ĵʵ�
    Set map_dict = Nothing
     
    ' ���Զ��������Ļˢ��
    Application.Calculation = xlCalculationAutomatic
    Application.ScreenUpdating = True
End Sub

'----------------------------------------------------------------------------------------------------------
'IF����ʹ��
����	=IF(A1="*��*","True","False")
world	o
	
40	=IF(A4="40",TRUE,FALSE)
'----------------------------------------------------------------------------------------------------------
'�Զ��庯������
'�����Public���ſ����ں����������ʾ�������Private�������ã������޷�����
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

