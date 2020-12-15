>>> from openpyxl import Workbook
>>> wb = Workbook()
>>> ws = wb.get_active_sheet()
>>> ws1 = wb.create_sheet() # insert at the end (default)
# or
>>> ws2 = wb.create_sheet(0) # insert at first position
ws.title = "New Title"
>>> ws3 = wb.get_sheet_by_name("New Title")
>>> ws is ws3
>>> print wb.get_sheet_names() #['Sheet2', 'New Title', 'Sheet1']

Warning Because of this feature, scrolling through cells instead of accessing them directly will create them all in memory, even if you don’t assign them a value.
Something like
>>> for i in xrange(0,100):
...             for j in xrange(0,100):
...                     ws.cell(row = i, column = j)
will create 100x100 cells in memory, for nothing.
However, there is a way to clean all those unwanted cells, we’ll see that later.

>>> ws.range('A1:C2')
((<Cell Sheet1.A1>, <Cell Sheet1.B1>, <Cell Sheet1.C1>),
(<Cell Sheet1.A2>, <Cell Sheet1.B2>, <Cell Sheet1.C2>))

>>> for row in ws.range('A1:C2'):
...             for cell in row:
...                     print cell
<Cell Sheet1.A1>
<Cell Sheet1.B1>
<Cell Sheet1.C1>
<Cell Sheet1.A2>
<Cell Sheet1.B2>
<Cell Sheet1.C2>

If you need to iterate through all the rows or columns of a file, you can instead use the openpyxl.worksheet.Worksheet.rows() property:
>>> ws = wb.get_active_sheet()
>>> ws.cell('C9').value = 'hello world'
>>> ws.rows
((<Cell Sheet.A1>, <Cell Sheet.B1>, <Cell Sheet.C1>),
(<Cell Sheet.A2>, <Cell Sheet.B2>, <Cell Sheet.C2>),
(<Cell Sheet.A3>, <Cell Sheet.B3>, <Cell Sheet.C3>),
(<Cell Sheet.A4>, <Cell Sheet.B4>, <Cell Sheet.C4>),
(<Cell Sheet.A5>, <Cell Sheet.B5>, <Cell Sheet.C5>),
(<Cell Sheet.A6>, <Cell Sheet.B6>, <Cell Sheet.C6>),
(<Cell Sheet.A7>, <Cell Sheet.B7>, <Cell Sheet.C7>),
(<Cell Sheet.A8>, <Cell Sheet.B8>, <Cell Sheet.C8>),
(<Cell Sheet.A9>, <Cell Sheet.B9>, <Cell Sheet.C9>))

>>> c.value = 'hello, world'
>>> print c.value
'hello, world'

>>> d.value = 3.14
>>> print d.value
3.14

>>> c.value = '12%'
>>> print c.value
0.12

>>> import datetime
>>> d.value = datetime.datetime.now()
>>> print d.value
datetime.datetime(2010, 9, 10, 22, 25, 18)

>>> c.value = '31.50'
>>> print c.value
31.5

>>> wb = Workbook()
>>> wb.save('balances.xlsx')
>>> from openpyxl import load_workbook
>>> wb2 = load_workbook('test.xlsx')
>>> print wb2.get_sheet_names()
['Sheet2', 'New Title', 'Sheet1']

from openpyxl import load_workbook
wb = load_workbook(filename = r'empty_book.xlsx')
sheet_ranges = wb.get_sheet_by_name(name = 'range names')
print sheet_ranges.cell('D18').value # D18	


#单元格样式设置
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from openpyxl import Workbook
from openpyxl.styles import Font, Border, Side, Alignment

if __name__ == '__main__':
    # https://www.cnblogs.com/BlueSkyyj/p/7571787.html
    # https://blog.csdn.net/weixin_41595432/article/details/79349995
    # https://www.cnblogs.com/Unikfox/p/9124767.html

    wb = Workbook()
    ws = wb.active
    ws.title = "我的页签"

    ws.cell(row=1, column=1).value = 99
    ws.cell(row=2, column=1, value=100)
    ws['A3'] = 4  # write
    ws['A4'] = u"这里是单元格"
    ws.append(['This is A1', 'This is B1', 'This is C1'])
    ws.append({'A': 'This is A1', 'C': 'This is C1'})
    ws.append({1: 'This is A1', 3: 'This is C1'})

    # 设置单元格字体
    font1 = Font(name='Arial',
                 size=20,
                 bold=True,
                 italic=False,
                 vertAlign=None,
                 underline='none',
                 strike=False,
                 color='FF000000')
    ws['A3'].font = font1
    ws['A4'].font = font1
    ws['A5'].font = font1
    # row = ws.row_dimensions[6]
    # row.font = font

    # 设置单元格边框
    border = Border(left=Side(border_style='thin',
                              color='FF000000'),
                    right=Side(border_style='thin',
                               color='FF000000'),
                    top=Side(border_style='thick',
                             color='FF000000'),
                    bottom=Side(border_style='thick',
                                color='FF000000'))
    ws['A3'].border = border

    #设置对齐方式
    alignment1 = Alignment(horizontal='right',
                           vertical='bottom',
                           text_rotation=0,
                           wrap_text=True,
                           shrink_to_fit=False,
                           indent=0)
    ws['B5'].alignment = alignment1

    alignment2 = Alignment(horizontal='left',
                           vertical='center',
                           text_rotation=0,
                           wrap_text=True,
                           shrink_to_fit=False,
                           indent=0)
    ws['C5'].alignment = alignment2

    # 设置行高和列宽
    ws.row_dimensions[6].height = 50
    ws.column_dimensions['C'].width = 100

    wb.save(filename="testExcel.xlsx")
#-----------------------------------------------------------------------------------------
#from openpyxl import Workbook
from openpyxl import load_workbook
import datetime

if __name__ == '__main__':
    #wb1=Workbook()
    wb = load_workbook("db.xlsx")
    ws = wb.get_active_sheet()
    #ws1.title="sheet2"
    #ws2 = wb.get_sheet_by_name("sheet2")
    #print ws2 is ws1 
    #print wb.get_sheet_names()

    cell = ws.cell("B3")
    cell.value = datetime.datetime.now()
    print cell.value
#    print ws.range("A1:B3")
#    for row in ws.range("A1:B3"):
#        for cell in row:
#            print cell
    print ws.rows
    print ws.columns
    wb.save("db.xlsx")
#-----------------------------------------------------------------------------------------
from openpyxl import Workbook
from openpyxl.cell import get_column_letter
wb = Workbook()
dest_filename = r'empty_book.xlsx'
ws = wb.worksheets[0]
ws.title = "range names"
for col_idx in xrange(1, 3):
    col = get_column_letter(col_idx)
    for row in xrange(1, 6):
        ws.cell('%s%s'%(col, row)).value = '%s%s' % (col, row)
ws = wb.create_sheet()
ws.title = 'Pi'
ws.cell('F5').value = 3.14
wb.save(filename = dest_filename)
#-----------------------------------------------------------------------------------------
import datetime
from openpyxl import Workbook

wb = Workbook()
ws = wb.worksheets[0]
# set date using a Python datetime
ws.cell('A1').value = datetime.datetime(2010, 7, 21)
print ws.cell('A1').style.number_format.format_code # returns 'yyyy-mm-dd'
# set percentage using a string followed by the percent sign
ws.cell('B1').value = '3.14%'
print ws.cell('B1').value # returns 0.031400000000000004
print ws.cell('B1').style.number_format.format_code # returns '0%'
wb.save("db.xlsx")
#-----------------------------------------------------------------------------------------
#没有运行通过？
from openpyxl import load_workbook
wb = load_workbook(filename = 'db.xlsx', use_iterators = True)
ws = wb.get_sheet_by_name(name = 'sheet') # ws is now an IterableWorksheet
for row in ws.iter_rows(): # it brings a new method: iter_rows()
    for cell in row:
        print cell.internal_value
#-----------------------------------------------------------------------------------------	
from openpyxl import Workbook
wb = Workbook(optimized_write = True)
ws = wb.create_sheet()
# now we'll fill it with 10k rows x 200 columns
for irow in xrange(10):
    ws.append(['%d' % i for i in xrange(10)])
wb.save('db.xlsx') # don't forget to save !
#-----------------------------------------------------------------------------------------
Warning When creating your workbook using optimized_write set to True, you will only be able to call this function once. Subsequents attempts to modify or save the file will raise an openpyxl.shared.exc.WorkbookAlreadySaved exception.

from openpyxl import Workbook
from openpyxl import load_workbook
wb = Workbook()
#print wb.get_named_ranges()
ws = wb.create_sheet(title = "test")
ws.cell("A1").value = "A1"
print wb.get_index(ws)
print wb.get_sheet_names()
wb.remove_sheet(ws)
print wb.get_sheet_names()
#wb.add_sheet(ws)
#wb.add_sheet(ws, 1)
wb.save('db.xlsx') # don't forget to save !
#-----------------------------------------------------------------------------------------
append(list_or_dict)[source]
Appends a group of values at the bottom of the current sheet.

If it’s a list: all values are added in order, starting from the first column
If it’s a dict: values are assigned to the columns indicated by the keys (numbers or letters)
Parameters:	
list_or_dict (list/tuple or dict) – list or dict containing values to append
Usage:

append([‘This is A1’, ‘This is B1’, ‘This is C1’])
or append({‘A’ : ‘This is A1’, ‘C’ : ‘This is C1’})
or append({0 : ‘This is A1’, 2 : ‘This is C1’})
Raise :	TypeError when list_or_dict is neither a list/tuple nor a dict
#-----------------------------------------------------------------------------------------
from openpyxl import Workbook
from openpyxl import load_workbook
wb = Workbook()
#print wb.get_named_ranges()
ws = wb.create_sheet(title = "test")
ws.cell("A1").value = "A1"
ws.cell("B2").value = "B2"
print ws.calculate_dimension()
rangeStr = ws.calculate_dimension()
print rangeStr.find(":")
wb.save('db.xlsx') # don't forget to save !
#-----------------------------------------------------------------------------------------
cell(coordinate=None, row=None, column=None)[source]
Returns a cell object based on the given coordinates.

Usage: cell(coodinate=’A15’) or cell(row=15, column=1)

If coordinates are not given, then row and column must be given.

Cells are kept in a dictionary which is empty at the worksheet creation. Calling cell creates the cell in memory when they are first accessed, to reduce memory usage.

Parameters:	
coordinate (string) – coordinates of the cell (e.g. ‘B12’)
row (int) – row index of the cell (e.g. 4)
column (int) – column index of the cell (e.g. 3)
Raise :	
InsufficientCoordinatesException when coordinate or (row and column) are not given
Return type:	
openpyxl.cell.Cell
#-----------------------------------------------------------------------------------------
TYPE_STRING = 's'
TYPE_FORMULA = 'f'
TYPE_NUMERIC = 'n'
TYPE_BOOL = 'b'
TYPE_NULL = 's'
TYPE_INLINE = 'inlineStr'
TYPE_ERROR = 'e'
TYPE_FORMULA_CACHE_STRING = 'str'

from openpyxl import Workbook
from openpyxl import load_workbook
wb = Workbook()
#print wb.get_named_ranges()
ws = wb.create_sheet(title = "test")
ws.cell("A1").value = "A1"
ws.cell("B2").value = "B2"
ws.cell("B3").value = "B3"
print ws.get_cell_collection()
cellA1 = ws.get_cell_collection()[0]
print cellA1.value
print ws.get_highest_column()
print ws.get_highest_row()
print ws.range("A1:B2", 1, 2)
cellB2 = ws.range("A1:A1", 1, 1)
print cellB2
print cellB2[0][0].value
print ws.title
ws.title = "hello"
print cellA1.address
print cellA1.get_coordinate()
print cellA1.data_type
print cellA1.bind_value("world")
print cellA1.check_numeric("123")
cellA1.value = cellA1.check_numeric("123")
print cellA1.data_type
cellA1.value = cellA1.check_string("abc")
print cellA1.data_type
#print cellA1.has_style()
print cellA1.hyperlink
print cellA1.is_date()
anotherCell = cellA1.offset(1, 1)
print anotherCell.address
print cellA1.style
cellA1.set_value_explicit(value = "123.4000", data_type = "s")
#cellA1.set_value_explicit(data_type = "n")
wb.save('db.xlsx') # don't forget to save !
#-----------------------------------------------------------------------------------------
list("hello")

    sequence = [None] * 5
    sequence[2] = 3
    sequence[4] = "what"
    print sequence
	
	name = list("perl")
    name[1:] = list("ython")
    print name 
	
	name = list("perl")
    name[1:1] = list("ABC")
    print name 
	
    name = list("perl")
    name[1:3] = []
    print name 

    name = list("helloworld")
    name[1::2] = list("ABCDE")#要插入的字段数必须和按照步长分隔的相同
    print name 	
	
    name = list("helloworld")
    print name.count("l")

    a = [1, 2, 3]
    b = [4, 5, 6]
    a.extend(b)
	#a[len(a):] = b #可读性不如extend
    print a	
    print a.index(2)
	a.insert(2, "four")#a[2:2] = ["four"]#可读性不如insert
	a.pop()
	a.pop(2)#pop方法是唯一一个既能修改列表又返回元素值（除了None）的列表方法
	a.remove(1)
	a.reverse()#list(reversed(a))
	a.sort()
	
	a = ["a", 1, 3, 2]
    b = a[:]#很有效率的复制整个列表的方法，只是简单的把a赋值给b是没用的
    b.sort()
	
	另一种获取已排序的列表副本方法是（可以用于任何序列，总返回一个列表）
	b = sorted(a)
	
	b.sort(cmp)#这是默认的比较函数，用的是内建函数cmp
	
	b = ["a", "abc", "de"]
    b.sort()
    print b
    b.sort(key = len)
    print b
	
	b = ["a", "abc", "de"]
    b.sort()
    print b
    b.sort(key = len, reverse = True)
    print b
#-----------------------------------------------------------------------------------------
#python压缩和解压缩
import zipfile
    f = zipfile.ZipFile("db.zip", "w", zipfile.ZIP_DEFLATED)
    f.write("db.xlsx")
    f.write("main.py")
    f.close()
	
    f = zipfile.ZipFile("db.zip", "r")
    f.extractall("c:\Users\Ben\Desktop", ["db.xlsx"])#不带第二个参数则解压缩所有文件
    f.close()

    newtxt = open("new.txt", "w")
    newtxt.write("this is a new txt\n")
    newtxt.close()
    f = zipfile.ZipFile("db.zip", "a", zipfile.ZIP_DEFLATED)
    f.write("new.txt")
    f.close()

	print zipfile.is_zipfile("db.zip")	
	
    f = zipfile.ZipFile("db.zip", "a", zipfile.ZIP_DEFLATED)
    f.close()
    print f.namelist()	
	print f.printdir()	
	
	
    f = zipfile.ZipFile("db.zip", "w", zipfile.ZIP_DEFLATED)
    f.write("db.xlsx")
    f.write("main.py")
	#f.close()
    #f = zipfile.ZipFile("db.zip", "r")
    txtFile = f.open("main.py")
    lines = txtFile.readlines()
    for line in lines:
        line = line.rstrip()
        print line
    txtFile.close()
    f.close()
	
    f = zipfile.ZipFile("db.zip", "a", zipfile.ZIP_DEFLATED)
    f.write("db.xlsx", "dbdir/db.xlsx")
    f.close()	
if __name__ == '__main__':
    f = zipfile.ZipFile("db.zip", "r", zipfile.ZIP_DEFLATED)
    fileMain = f.read("main.py")#这里读出的fileMain是“str”对象，如果改成fileMain = f.read("db.xlsx")，此时读出的二进制excel文件打印出的是乱码
    print fileMain
    f.close()
	
#用zip.exe实现压缩
#带着目录操作则会把所有目录层次打包到压缩包中
import os
if __name__ == '__main__':
    cmdOrder = r'zip -rJ "d:\Program Files\eclipse workspace\PyLearngingProgram\main\abc.zip" "d:\Program Files\eclipse workspace\PyLearngingProgram\main\dbDir\*.*"' #这时候需要把zip.exe放入python工程目录或者环境变量搜索目录中
    os.system(cmdOrder)
	
#如果不带着目录操作，则需要进入相应的文件夹中，把需要的文件打包，见下
# encoding=utf-8
import os
if __name__ == '__main__':
    dstDir = r"d:\Program Files\eclipse workspace\PyLearngingProgram\main\dbDir"
    curDir = os.getcwd()
    os.chdir(dstDir)
    cmdOrder = r'zip -rJ "abc.zip" *.*' #这时候一定要把zip.exe放入环境变量搜索目录中，因为此时“当前目录”已经切换至要打包的目录中了，zip.exe不再能直接搜索到了
    os.system(cmdOrder)
    os.chdir(curDir)
	#再用shutil.copy2把abc.zip拷到父目录中即可

#这个zip.exe的压缩操作，如果已经存在压缩包了，再对该压缩包进行zip操作，则只会更新操作，即对压缩包中的已有文件进行update，没有的文件进行add，而不会将生成的压缩包替换掉，而此时这个同名的.zip文件不会被再次打到包中；如果换个other.zip名称进行打包，则刚才的压缩包会被打入新包other.zip中。
#-----------------------------------------------------------------------------------------
#python目录操作
import os

if __name__ == '__main__':
    currentPath = os.getcwd()
    print currentPath
    for fileName in os.listdir(currentPath):
        print fileName
		
if __name__ == '__main__':
    currentPath = os.getcwd()
    print currentPath
    os.mkdir("%s/newDir" % currentPath)
	print os.path.isdir("%s/newDir" % currentPath)#True
	print os.path.isfile("%s/newDir" % currentPath)#False
	print os.path.islink("%s/newDir" % currentPath)#False	
	os.rmdir("%s/newDir" % currentPath)#需要说明的是，使用os.rmdir删除的目录必须为空目录，否则函数出错。
	
	
	time_of_last_access = os.path.getatime(myfile) 
	time_of_last_modification = os.path.getmtime(myfile) 
	size = os.path.getsize(myfile) 	
	
	#这里的时间以秒为单位，并且从1970年1月1日开始算起。为了获取以天为单位的最后访问日期，可以使用下列代码： 
	time_of_last_access = os.path.getatime("db.xlsx") 
    age_in_days = (time.time()-time_of_last_access)/(60*60*24) 
    print age_in_days
	print os.access("db.xlsx", os.R_OK|os.W_OK|os.X_OK)
	
	os.remove("db.zip")

import shutil	
shutil.rmtree("newdir")

filelist = glob.glob('*.xlsx') + glob.glob('*.py')
print filelist 
print isinstance(filelist, list)
print isinstance(filelist, str) 
#-----------------------------------------------------------------------------------------
import os
import shutil
import glob
import operator

def remove(files):
    if isinstance(files,str):
        files = [files]
    if not isinstance(files, list):
	#if not operator.isSequenceType(files):#更好的测试条件
        return
    for eachFile in files:
        if os.path.isdir(eachFile):
            shutil.rmtree(eachFile)
        elif os.path.isfile(eachFile):
            os.remove(eachFile)     

if __name__ == '__main__':
    for i in range(2):
        os.mkdir("tmp_%s" % str(i))
        f = open("tmp__%s.txt" % str(i), "w")
        f.close()
    remove("tmp_1")
    remove(glob.glob("tmp_[0-1]") + glob.glob("tmp__[0-1].txt")) 
	
import shutil
import os

if __name__ == '__main__':
	shutil.copy("db.xlsx", "newDb.xlsx")
    shutil.copy2("db.xlsx", "newDb.xlsx")#拷贝最后访问时间和最后修改时间
	shutil.copytree("dbdir", "newDbdir", True)
	
	shutil.copy(os.path.join(os.curdir, "db.xlsx"), os.pardir)
	os.rename("dbdir", "dbdir2")#文件夹或文件
#-----------------------------------------------------------------------------------------
#这个函数也可用来在相同的文件系统之内移动文件。这里，我们将myfile移动到目录d下面： 
os.rename(myfile, os.path.join(d, myfile)) 

#在跨文件系统移动文件的时候，可以先使用shutil.copy2来复制文件，然后再删除原来的副本即可，如下： 
shutil.copy2(myfile, os.path.join(d, myfile)) 
os.remove(myfile)
后面这种移动文件的方法是最安全的。 
#-----------------------------------------------------------------------------------------
import os
import shutil
if __name__ == '__main__':
    currentPath = os.getcwd()
    dbXlsx = os.path.join(currentPath, "db.xlsx")
    print dbXlsx
    baseName = os.path.basename(dbXlsx)#得到文件名或目录名，带扩展名
    dirName = os.path.dirname(dbXlsx)#得到父目录名
	#或dirName, baseName = os.path.split(dbXlsx)
    print baseName
    print dirName
    print os.path.dirname(dirName)
	root, extension = os.path.splitext(dbXlsx)
    print root
    print extension 
输出如下：
D:\Program Files\eclipse workspace\PyLearngingProgram\main\db.xlsx
db.xlsx
D:\Program Files\eclipse workspace\PyLearngingProgram\main
D:\Program Files\eclipse workspace\PyLearngingProgram
D:\Program Files\eclipse workspace\PyLearngingProgram\main\db
.xlsx

这样，fname中的扩展名部分即.py被赋给变量extension，而其余部分则赋给了变量root。如果想得到不带点号的扩展名的话，只需使用os.path.splitext(fname)[1][1:]即可。
假设一个文件名为f，其扩展名随意，若想将其扩展名改为ext，可以使用下面的代码： 
　　newfile = os.path.splitext(f)[0] + ext 
#-----------------------------------------------------------------------------------------
if __name__ == '__main__':
    currentPath = os.getcwd()
    print currentPath
    os.chdir(os.pardir)
    print os.getcwd() 

if __name__ == '__main__':	
    currentPath = os.getcwd()
    newDir = os.path.join(currentPath, "newdbdir")
    if not os.path.isdir(newDir):
        os.mkdir(newDir)
    os.chdir(newDir)
    print os.getcwd()	

if __name__ == '__main__':
    currentPath = os.getcwd()
    newDir = os.path.join(currentPath, "a", "b", "c")
    os.makedirs(newDir)	
#-----------------------------------------------------------------------------------------
import os

def showDir(arg, dirName, files):
    print dirName, "has the files", files

if __name__ == '__main__':
    os.path.walk(os.getcwd(), showDir, None)
输出如下：
D:\Program Files\eclipse workspace\PyLearngingProgram\main has the files ['a', 'db.xlsx', 'dbdir2', 'empty_book.xlsx', 'main.py', 'new.txt', 'newdbdir', '__init__.py']
D:\Program Files\eclipse workspace\PyLearngingProgram\main\a has the files ['b']
D:\Program Files\eclipse workspace\PyLearngingProgram\main\a\b has the files ['c']
D:\Program Files\eclipse workspace\PyLearngingProgram\main\a\b\c has the files []
D:\Program Files\eclipse workspace\PyLearngingProgram\main\dbdir2 has the files ['another.txt']
D:\Program Files\eclipse workspace\PyLearngingProgram\main\newdbdir has the files []

本例中，参数arg并非必需，所以在os.path.walk调用中让其取值为None即可。

import os

def showDir(arg, dirName, files):
    for eachFile in files:
        filePath = os.path.join(dirName, eachFile)
        if os.path.isfile(filePath):
            size = os.path.getsize(filePath)
            #sizeInKB = size / 1000
            arg.append((size, eachFile))

if __name__ == '__main__':
    fileList = []
    os.path.walk(os.getcwd(), showDir, fileList)
输出如下：
[(5727L, 'db.xlsx'), (5825L, 'empty_book.xlsx'), (457L, 'main.py'), (19L, 'new.txt'), (0L, '__init__.py'), (0L, 'another.txt')]

参数dirname是当前正在访问的目录的绝对路径，而参数files内的文件名则是相对于dirname的相对路径。在此期间，当前工作目录并没有改变，那就是说该脚本仍然呆在脚本启动时刻所在的目录中。这就是为什么我们需要把filepath弄成带有dirname和file的绝对路径的原因。若要改变当前工作目录为dirname，只要在针对每个目录调用os.path.walk的函数中调用一下os.chdir(dirname)，然后在该函数的末尾重新调用os.chdir(dirname)将当前工作目录改回原值即可，如下所示： 
def somefunc(arg, dirname, files): 
	origdir = os.getcwd()
	os.chdir(dirname) 
　#...　 
	os.chdir(origdir)
	
#另一种遍历目录的方法
# encoding=utf-8
import os

if __name__ == '__main__':
    rootdir = r"d:\Program Files\eclipse workspace\PyLearngingProgram\main"
    for parent, dirnames, filenames in os.walk(rootdir):
        print parent.decode("gbk")
        print dirnames#没什么用处，用第一个父目录和第三个文件名即可拼出所有文件的全目录
        print filenames
输出如下：
d:\Program Files\eclipse workspace\PyLearngingProgram\main
['dbDir']
['abc.bmp', 'main.py', 'simple.xls', 'test.sh', 'test.txt', 'test.xls', 'test.xlsx', '__init__.py']
d:\Program Files\eclipse workspace\PyLearngingProgram\main\dbDir
[]
['test.xlsx']		
#-----------------------------------------------------------------------------------------
#自己实现os.path.walk函数
import os

def find(func, rootDir, arg = None):
    files = os.listdir(rootDir)
    files.sort(lambda a, b: cmp(a.lower(), b.lower()))
    for eachFile in files:
        fullPath = os.path.join(rootDir, eachFile)
        if os.path.islink(fullPath):
            pass
        elif os.path.isdir(fullPath):
            find(func, fullPath, arg)
        elif os.path.isfile(fullPath):
            func(fullPath, arg)
        else:
            print "find: cannot treat", fullPath

def checkSize(fullPath, fileList):
    size = os.path.getsize(fullPath)
    fileList.append((size, fullPath))

if __name__ == '__main__':
    fileList = []
    currentDir = os.getcwd()
    find(checkSize, currentDir, fileList)
	for fileInfo in fileList:
        print fileInfo
#-----------------------------------------------------------------------------------------
参数arg带来了巨大的灵活性。我们可以使用它来同时存放输入数据和生成的数据结构。下一个范例将收集所有大于一定尺寸的带有规定扩展名的文件的文件名和大小。输出的结果按照文件大小排列。
bigfiles = {'filelist': [], # 文件名和大小列表 
'extensions': ('.*ps', '.tiff', '.bmp'), 
'size_limit': 1000000, # 1 Mb 
} 
find(checksize3, os.environ['HOME'], bigfiles) 

import fnmatch # Unix的shell风格的通配符匹配
def checksize3(fullpath, arg): 
	treat_file = False 
	ext = os.path.splitext(fullpath)[1] 
	for s in arg['extensions']: 
		if fnmatch.fnmatch(ext, s): 
			treat_file = True # fullpath带有正确的扩展名 
	size = os.path.getsize(fullpath) 
	if treat_file and size > arg['size_limit']: 
		size = '%.2fMb' % (size/1000000.0) # 打印 
		arg['filelist'].append({'size': size, 'name': fullpath}) 
		
# 按照大小排列文件 
def filesort(a, b): 
	return cmp(float(a['size'][:-2]), float(b['size'][:-2])) 

bigfiles['filelist'].sort(filesort) 
bigfiles['filelist'].reverse() 
for fileinfo in bigfiles['filelist']: 
	print fileinfo['name'], fileinfo['size'] 		
注意为列表排序的函数，bigfiles['filelist']函数中的每个元素就是一个字典，键size保存着一个字符串，不过在进行比较之前我们必须将单位Mb(最后两个字符)去掉，并将其转换为浮点数。 	
#-----------------------------------------------------------------------------------------
import copy

class delTest(object):
    def __init__(self):
        print "init"
    
    def __del__(self):
        print "del"    
     

if __name__ == '__main__':
    c = delTest()
    d = copy.deepcopy(c)
    del c
    print "abc"
#-----------------------------------------------------------------------------------------	
class Test(object):
    def __init__(self):
        print "init"
        show()
        Test.show(self)#调用类成员函数必须加类名和(self)
        
    def show(self):
        print "world"#调用全局函数
        show2()#类中可以调用全局函数，无关位置
        
def show():
    print "hello"
    
def show2():
    print "nice"   

if __name__ == '__main__':
    c = Test()
    #c.show()
#-----------------------------------------------------------------------------------------
全局变量不同模块的导入与使用
#moduleA
class Test(object):
    def __init__(self):
        self.number = None
    
    def set(self, n):
        self.number = n
    
    def show(self):
        print self.number
        
t = Test()      

#moduleB
from moduleA import t

def functionB():
    t.show()

#main
from moduleB import t, functionB

if __name__ == '__main__':
    t.set(10)
    t.show()
    functionB()	
#-----------------------------------------------------------------------------------------
感觉可以通过这种方式像C++中纯虚函数一样构造虚基类
def show():
    pass

if __name__ == '__main__':
    show()
#-----------------------------------------------------------------------------------------
xlrd读取excel
import xlrd

if __name__ == '__main__':
    wb = xlrd.open_workbook("test.xls")
    ws = wb.sheet_by_index(0)
    print wb.nsheets
    print ws.nrows
    print ws.ncols
    print ws.cell_value(0, 0)
    print ws.cell(0, 0)
	
	rowData = ws.row_values(0)
    cellValue = rowData[0]
    print cellValue
    print rowData
    print ws.col_values(2) 
	
	ws = wb.sheet_by_name("Sheet1")
    nameList = wb.sheet_names()
    print ws.cell(0, 0).value
    print nameList
#-----------------------------------------------------------------------------------------
# encoding : utf-8       #设置编码方式  
  
import xlrd                    #导入xlrd模块  
  
#打开指定文件路径的excel文件  
  
xlsfile = r'D:\AutoPlan\apisnew.xls'   
book = xlrd.open_workbook(xlsfile)     #获得excel的book对象  
  
#获取sheet对象，方法有2种：  
sheet_name=book.sheet_names()[0]          #获得指定索引的sheet名字  
print sheet_name  
sheet1=book.sheet_by_name(sheet_name)  #通过sheet名字来获取，当然如果你知道sheet名字了可以直接指定  
sheet0=book.sheet_by_index(0)     #通过sheet索引获得sheet对象  
  
#获取行数和列数：  
  
nrows = sheet.nrows    #行总数  
ncols = sheet.ncols   #列总数  
  
#获得指定行、列的值，返回对象为一个值列表  
  
row_data = sheet.row_values(0)   #获得第1行的数据列表  
col_data = sheet.col_values(0)  #获得第一列的数据列表，然后就可以迭代里面的数据了  
  
#通过cell的位置坐标获得指定cell的值  
cell_value1 = sheet.cell_value(0,1)  ##只有cell的值内容，如：http://xxx.xxx.xxx.xxx:8850/2/photos/square/  
print cell_value1  
cell_value2 = sheet.cell(0,1) ##除了cell值内容外还有附加属性，如：text:u'http://xxx.xxx.xxx.xxx:8850/2/photos/square/'  
print cell_value2  
#-----------------------------------------------------------------------------------------
from xlrd import open_workbook

if __name__ == '__main__':
    wb = open_workbook('test.xls')
    for s in wb.sheets():
        print 'Sheet:', s.name
        for row in range(s.nrows):
            values = []
            for col in range(s.ncols):
                values.append(s.cell(row,col).value)
            print ','.join(values)#用,连接所有字符串
        print
	
	
from xlrd import open_workbook

if __name__ == '__main__':
    book = open_workbook('test.xls')
    print book.nsheets
    for sheet_index in range(book.nsheets):
        print book.sheet_by_index(sheet_index)
    print book.sheet_names()
    for sheet_name in book.sheet_names():
        print book.sheet_by_name(sheet_name)
    for sheet in book.sheets():
        print sheet	

		
from xlrd import open_workbook

if __name__ == '__main__':
    book = open_workbook('test.xls')
    sheet = book.sheet_by_index(0)
    print sheet.name
    print sheet.nrows
    print sheet.ncols
    for row_index in range(sheet.nrows):
        for col_index in range(sheet.ncols):
            #print cell.name(row_index,col_index),'-',
            print sheet.cell(row_index,col_index).value	

			
from xlrd import open_workbook

if __name__ == '__main__':
    book = open_workbook('test.xls')
    sheet = book.sheet_by_index(0)
    cell = sheet.cell(0,0)
    print cell
    print cell.value
    print cell.ctype==XL_CELL_TEXT
    for i in range(sheet.ncols):
        print sheet.cell_type(1,i),sheet.cell_value(1,i)

from xlrd import open_workbook

if __name__ == '__main__':    
    book = open_workbook('test.xls')
    sheet0 = book.sheet_by_index(0)
    sheet1 = book.sheet_by_index(1)
    print sheet0.row(0)
    print sheet0.col(0)
    print
    print sheet0.row_slice(0,1)
    print sheet0.row_slice(0,1,2)
    print sheet0.row_values(0,1)
    print sheet0.row_values(0,1,2)
    print sheet0.row_types(0,1)
    print sheet0.row_types(0,1,2)
    print
    print sheet1.col_slice(0,1)
    print sheet0.col_slice(0,1,2)
    print sheet1.col_values(0,1)
    print sheet0.col_values(0,1,2)
    print sheet1.col_types(0,1)
    print sheet0.col_types(0,1,2)

from xlrd import cellname, cellnameabs, colname

if __name__ == '__main__':    
    print cellname(0,0),cellname(10,10),cellname(100,100)
    print cellnameabs(3,1),cellnameabs(41,59),cellnameabs(265,358)
    print colname(0),colname(10),colname(100)

It is recommended that flush_row_data is called for every 1000 or so rows of a normal size that are written to an xlwt.Workbook. If the rows are huge, that number should be reduced.	
#-----------------------------------------------------------------------------------------
from xlwt import Workbook

if __name__ == '__main__':    
    book = Workbook()
    sheet1 = book.add_sheet('Sheet 1')
	sheet1.name = "abc"
    book.add_sheet('Sheet 2')
    sheet1.write(0,0,'A1')
    sheet1.write(0,1,'B1')
    row1 = sheet1.row(1)
    row1.write(0,'A2')
    row1.write(1,'B2')
    sheet1.col(0).width = 10000
    sheet2 = book.get_sheet(1)
    sheet2.row(0).write(0,'Sheet 2 A1')
    sheet2.row(0).write(1,'Sheet 2 B1')
    sheet2.flush_row_data()
    sheet2.write(1,0,'Sheet 2 A3')
    sheet2.col(0).width = 5000
    sheet2.col(0).hidden = True
    book.save('simple.xls')
	

from xlwt import Workbook
book = Workbook()
sheet1 = book.add_sheet('Sheet 1',cell_overwrite_ok=True)
sheet1.write(0,0,'original')
sheet = book.get_sheet(0)
sheet.write(0,0,'new')
sheet2 = book.add_sheet('Sheet 2')
sheet2.write(0,0,'original')
sheet2.write(0,0,'new')

from datetime import date,time,datetime
from decimal import Decimal
from xlwt import Workbook,Style
wb = Workbook()
ws = wb.add_sheet('Type examples')
ws.row(0).write(0,u'\xa3')
ws.row(0).write(1,'Text')
ws.row(1).write(0,3.1415)
ws.row(1).write(1,15)
ws.row(1).write(2,265L)
ws.row(1).write(3,Decimal('3.65'))
ws.row(2).set_cell_number(0,3.1415)
ws.row(2).set_cell_number(1,15)
ws.row(2).set_cell_number(2,265L)
ws.row(2).set_cell_number(3,Decimal('3.65'))
ws.row(3).write(0,date(2009,3,18))
ws.row(3).write(1,datetime(2009,3,18,17,0,1))
ws.row(3).write(2,time(17,1))
ws.row(4).set_cell_date(0,date(2009,3,18))
ws.row(4).set_cell_date(1,datetime(2009,3,18,17,0,1))
ws.row(4).set_cell_date(2,time(17,1))
ws.row(5).write(0,False)
ws.row(5).write(1,True)
ws.row(6).set_cell_boolean(0,False)
ws.row(6).set_cell_boolean(1,True)
ws.row(7).set_cell_error(0,0x17)
ws.row(7).set_cell_error(1,'#NULL!')
ws.row(8).write(
    0,'',Style.easyxf('pattern: pattern solid, fore_colour
green;'))
ws.row(8).write(
    1,None,Style.easyxf('pattern: pattern solid, fore_colour
blue;'))
ws.row(9).set_cell_blank(
    0,Style.easyxf('pattern: pattern solid, fore_colour
yellow;'))
ws.row(10).set_cell_mulblanks(
    5,10,Style.easyxf('pattern: pattern solid, fore_colour
red;')
    )
wb.save('types.xls')

from datetime import date
from xlwt import Workbook, XFStyle, Borders, Pattern, Font

fnt = Font()
fnt.name = 'Arial'
borders = Borders()
borders.left = Borders.THICK
borders.right = Borders.THICK
borders.top = Borders.THICK
borders.bottom = Borders.THICK
pattern = Pattern()
pattern.pattern = Pattern.SOLID_PATTERN
pattern.pattern_fore_colour = 0x0A
style = XFStyle()
style.num_format_str='YYYY-MM-DD'
style.font = fnt
style.borders = borders
style.pattern = pattern
book = Workbook()
sheet = book.add_sheet('A Date')
sheet.write(1,1,date(2009,3,18),style)
book.save('simple.xls')

from datetime import date
from xlwt import Workbook, easyxf
book = Workbook()
sheet = book.add_sheet('A Date')
sheet.write(1,1,date(2009,3,18),easyxf(
    'font: name Arial;'
    'borders: left thick, right thick, top thick, bottom thick;'
    'pattern: pattern solid, fore_colour red;',
    num_format_str='YYYY-MM-DD'
    ))
sheet.write(0, 0, "hello")
book.save('simple.xls')


The human readable text breaks roughly as follows, in pseudo-regular expression syntax:
 (<element>:(<attribute> <value>,)+;)

from xlwt import Workbook, easyxf
from xlwt.Utils import rowcol_to_cell
row = easyxf('pattern: pattern solid, fore_colour blue')
col = easyxf('pattern: pattern solid, fore_colour green')
cell = easyxf('pattern: pattern solid, fore_colour red')
book = Workbook()
sheet = book.add_sheet('Precedence')
for i in range(0,10,2):
    sheet.row(i).set_style(row)
for i in range(0,10,2):
    sheet.col(i).set_style(col)
for i in range(10):
    sheet.write(i,i,None,cell)
sheet = book.add_sheet('Hiding')
for rowx in range(10):
    for colx in range(10):
        sheet.write(rowx,colx,rowcol_to_cell(rowx,colx))
for i in range(0,10,2):
    sheet.row(i).hidden = True
    sheet.col(i).hidden = True
sheet = book.add_sheet('Row height and Column width')
for i in range(10):
    sheet.write(0,i,0)
for i in range(10):
    sheet.row(i).set_style(easyxf('font:height '+str(200*i)))
    sheet.col(i).width = 256*i
book.save('simple.xls')

from xlwt import Utils
print 'AA ->',Utils.col_by_name('AA')
print 'A ->',Utils.col_by_name('A')
print 'A1 ->',Utils.cell_to_rowcol('A1')
print '$A$1 ->',Utils.cell_to_rowcol('$A$1')
print 'A1 ->',Utils.cell_to_rowcol2('A1')
print (0,0),'->',Utils.rowcol_to_cell(0,0)
print (0,0,False,True),'->',
print Utils.rowcol_to_cell(0,0,False,True)
print (0,0,True,True),'->',
print Utils.rowcol_to_cell(
          row=0,col=0,row_abs=True,col_abs=True
          )
print '1:3 ->',Utils.cellrange_to_rowcol_pair('1:3')
print 'B:G ->',Utils.cellrange_to_rowcol_pair('B:G')
print 'A2:B7 ->',Utils.cellrange_to_rowcol_pair('A2:B7')
print 'A1 ->',Utils.cellrange_to_rowcol_pair('A1')
print (0,0,100,100),'->',
print Utils.rowcol_pair_to_cellrange(0,0,100,100)
print (0,0,100,100,True,False,False,False),'->',
print Utils.rowcol_pair_to_cellrange(
          row1=0,col1=0,row2=100,col2=100,
          row1_abs=True,col1_abs=False,
          row2_abs=False,col2_abs=True
          )
for name in (
    '',"'quoted'","O'hare","X"*32,"[]:\\?/*\x00"
    ):
    print 'Is %r a valid sheet name?' % name,
    if Utils.valid_sheet_name(name):
        print "Yes"
    else:
        print "No"

from xlwt import Workbook
w = Workbook()
ws = w.add_sheet('Image')
ws.insert_bitmap('abc.bmp', 0, 0)
w.save("simple.xls")

from xlwt import Workbook,easyxf
style = easyxf(
    'pattern: pattern solid, fore_colour red;'
    'align: vertical center, horizontal center;'
    )
w = Workbook()
ws = w.add_sheet('Merged')
ws.write_merge(1,5,1,5,'Merged',style)
w.save("simple.xls")

from xlwt import Workbook,easyxf
tl = easyxf('border: left thick, top thick')
t = easyxf('border: top thick')
tr = easyxf('border: right thick, top thick')
r = easyxf('border: right thick')
br = easyxf('border: right thick, bottom thick')
b = easyxf('border: bottom thick')
bl = easyxf('border: left thick, bottom thick')
l = easyxf('border: left thick')
w = Workbook()
ws = w.add_sheet('Border')
ws.write(1,1,style=tl)
ws.write(1,2,style=t)
ws.write(1,3,style=tr)
ws.write(2,3,style=r)
ws.write(3,3,style=br)
ws.write(3,2,style=b)
ws.write(3,1,style=bl)
ws.write(2,1,style=l)
w.save("simple.xls")

from xlwt import Workbook
data = [
    ['','','2008','','2009'],
    ['','','Jan','Feb','Jan','Feb'],
    ['Company X'],
    ['','Division A'],
    ['','',100,200,300,400],
    ['','Division B'],
    ['','',100,99,98,50],
    ['Company Y'],
    ['','Division A'],
    ['','',100,100,100,100],
    ['','Division B'],
    ['','',100,101,102,103],
    ]
w = Workbook()
ws = w.add_sheet('Outlines')
for i,row in enumerate(data):
    for j,cell in enumerate(row):
        ws.write(i,j,cell)
ws.row(2).level = 1
ws.row(3).level = 2
ws.row(4).level = 3
ws.row(5).level = 2
ws.row(6).level = 3
ws.row(7).level = 1
ws.row(8).level = 2
ws.row(9).level = 3
ws.row(10).level = 2
ws.row(11).level = 3
ws.col(2).level = 1
ws.col(3).level = 2
ws.col(4).level = 1
ws.col(5).level = 2
w.save("simple.xls")

from xlwt import Workbook
w = Workbook()
ws = w.add_sheet('Normal')
ws.write(0,0,'Some text')
ws.normal_magn = 75
ws = w.add_sheet('Page Break Preview')
ws.write(0,0,'Some text')
ws.preview_magn = 150
ws.page_preview = True
w.save("simple.xls")


from xlrd import open_workbook
from xlwt import easyxf
from xlutils.copy import copy
rb = open_workbook('source.xls',formatting_info=True)
rs = rb.sheet_by_index(0)
wb = copy(rb)
ws = wb.get_sheet(0)
plain = easyxf('')
for i,cell in enumerate(rs.col(2)):
    if not i:
        continue
    ws.write(i,2,cell.value,plain)
for i,cell in enumerate(rs.col(4)):
    if not i:
        continue
    ws.write(i,4,cell.value-1000)
wb.save('output.xls')		

from xlrd import open_workbook
from xlwt import Workbook
from xlutils.copy import copy
rb = open_workbook("simple.xls")
wb = copy(rb)
ws = wb.add_sheet('hello')
wb.save("simple.xls")

# encoding=utf-8
from xlwt import Workbook, easyxf

def fillInExcel(ws, cellFormat,rowNumber, val0, val1, val2, val3, val4):
    ws.write(rowNumber, 0, val0, formatForValue)
    ws.write(rowNumber, 1, val1, formatForValue)
    ws.write(rowNumber, 2, val2, formatForValue)
    ws.write(rowNumber, 3, val3, formatForValue)
    ws.write(rowNumber, 4, val4, formatForValue)

if __name__ == '__main__':
    wb = Workbook(encoding = "utf-8")#如果不用中文字体，则这里不需要用utf-8编码，默认wb = Workbook()即可
    ws = wb.add_sheet("Test")
    formatForTitle = easyxf('font: name Times New Roman; font: bold True; font: height 300;'
                            'borders: left thin, right thin, top thin, bottom thin;'
                            'alignment: wrap True; alignment: horizontal left; alignment: vertical center;'
                            )
    row0 = ws.row(0)
    row0.write(0, "Moc", formatForTitle)
    row0.write(1, "Attribute", formatForTitle)
    row0.write(2, "Parameter Comparison", formatForTitle)
    row0.write(3, "Template Application", formatForTitle)
    row0.write(4, "Summary Customization(Only for New BaseStation)", formatForTitle)
    
    formatForValue = easyxf('font: name %s;'
                            'borders: left thin, right thin, top thin, bottom thin;'
                            'alignment: horizontal left; alignment: vertical center;' % (u"宋体")
                            )
  
    for i in range(1, 1000):
        fillInExcel(ws, formatForValue, i, "It", "is", "a", "nice", "day")#当写的行数大于504时，一定要将format作为参数传入写入函数fillInExcel中（如果不用函数，直接在for循环中写也可以）；如果把format定义在fillInExcel中，则会报错格式有问题
    
    ws.col(0).width = 5000#width:18.86
    ws.col(1).width = 6000
    ws.col(2).width = 7000
    ws.col(3).width = 8000
    ws.col(4).width = 8000
    wb.save("test.xls")
#-----------------------------------------------------------------------------------------
#读写文件带着b，二进制读取，可以保留文件格式
def getEnterSymbol(line):#对于只有一行内容，没有换行符的文件处理还是有问题的
    if len(line) == 1:#如果这行长度为1，那么要么是一个换行符，要么是一个只有一个字符的文件。
        return line
    else:
        lastTwoSymbol = line[-2:]
        if lastTwoSymbol == "\r\n":
            return lastTwoSymbol
        else:
            return  lastTwoSymbol[1]       

if __name__ == '__main__':
    testFile = open(r"c:\Users\Ben\Desktop\test.txt", "rb")
    newTestFile = open(r"c:\Users\Ben\Desktop\newtest.txt", "wb")
    line = testFile.readline()
    enterSymbol = getEnterSymbol(line)
    lineNumber = 1
    while line:        
        newTestFile.write("line%s%s:" % (str(lineNumber), enterSymbol))
        newTestFile.write(line)
        line = testFile.readline()
        lineNumber += 1
    testFile.close()
    newTestFile.close()
或：
if __name__ == '__main__':
    testFile = open(r"c:\Users\Ben\Desktop\test.txt", "rb")
    newTestFile = open(r"c:\Users\Ben\Desktop\newtest.txt", "wb")
    lines = testFile.readlines()
    enterSymbol = getEnterSymbol(lines[0])
    lineNumber = 1
    for line in lines:
        newTestFile.write("line%s%s:" % (str(lineNumber), enterSymbol))
        newTestFile.write(line)
        #line = testFile.readline()
        lineNumber += 1
    testFile.close()
    newTestFile.close()
#-----------------------------------------------------------------------------------------
在处理日志文件的时候，常常会遇到这样的情况：日志文件巨大，不可能一次性把整个文件读入到内存中进行处理，例如需要在一台物理内存为 2GB 的机器上处理一个 2GB 的日志文件，我们可能希望每次只处理其中 200MB 的内容。
在 Python 中，内置的 File 对象直接提供了一个 readlines(sizehint) 函数来完成这样的事情。以下面的代码为例：

file = open('test.log', 'r')
sizehint = 209715200   # 200M
position = 0
lines = file.readlines(sizehint)
while not file.tell() - position < 0:
	position = file.tell()
	lines = file.readlines(sizehint)

每次调用 readlines(sizehint) 函数，会返回大约 200MB 的数据，而且所返回的必然都是完整的行数据，大多数情况下，返回的数据的字节数会稍微比 sizehint 指定的值大一点（除最后一次调用 readlines(sizehint) 函数的时候）。通常情况下，Python 会自动将用户指定的 sizehint 的值调整成内部缓存大小的整数倍。

file在python是一个特殊的类型，它用于在python程序中对外部的文件进行操作。在python中一切都是对象，file也不例外，file有file的方法和属性。下面先来看如何创建一个file对象：
file(name[, mode[, buffering]]) 
file()函数用于创建一个file对象，它有一个别名叫open()，可能更形象一些，它们是内置函数。来看看它的参数。它参数都是以字符串的形式传递的。name是文件的名字。
mode是打开的模式，可选的值为r w a U，分别代表读（默认） 写 添加支持各种换行符的模式。用w或a模式打开文件的话，如果文件不存在，那么就自动创建。此外，用w模式打开一个已经存在的文件时，原有文件的内容会被清空，因为一开始文件的操作的标记是在文件的开头的，这时候进行写操作，无疑会把原有的内容给抹掉。由于历史的原因，换行符在不同的系统中有不同模式，比如在 unix中是一个\n，而在windows中是‘\r\n’，用U模式打开文件，就是支持所有的换行模式，也就说‘\r’ '\n' '\r\n'都可表示换行，会有一个tuple用来存贮这个文件中用到过的换行符。不过，虽说换行有多种模式，读到python中统一用\n代替。在模式字符的后面，还可以加上+ b t这两种标识，分别表示可以对文件同时进行读写操作和用二进制模式、文本模式（默认）打开文件。
buffering如果为0表示不进行缓冲;如果为1表示进行“行缓冲“;如果是一个大于1的数表示缓冲区的大小，应该是以字节为单位的。

file对象有自己的属性和方法。先来看看file的属性。
closed #标记文件是否已经关闭，由close()改写 
encoding #文件编码 
mode #打开模式 
name #文件名 
newlines #文件中用到的换行模式，是一个tuple 
softspace #boolean型，一般为0，据说用于print 

F.read([size]) #size为读取的长度，以byte为单位 
F.readline([size]) 
#读一行，如果定义了size，有可能返回的只是一行的一部分 
F.readlines([size]) 
#把文件每一行作为一个list的一个成员，并返回这个list。其实它的内部是通过循环调用readline()来实现的。如果提供size参数，size是表示读取内容的总长，也就是说可能只读到文件的一部分。 
F.write(str) 
#把str写到文件中，write()并不会在str后加上一个换行符 
F.writelines(seq) 
#把seq的内容全部写到文件中。这个函数也只是忠实地写入，不会在每行后面加上任何东西。 
file的其他方法：
F.close() 
#关闭文件。python会在一个文件不用后自动关闭文件，不过这一功能没有保证，最好还是养成自己关闭的习惯。如果一个文件在关闭后还对其进行操作会产生ValueError 
F.flush() 
#把缓冲区的内容写入硬盘 
F.fileno() 
#返回一个长整型的”文件标签“ 
F.isatty() 
#文件是否是一个终端设备文件（unix系统中的） 
F.tell() 
#返回文件操作标记的当前位置，以文件的开头为原点 
F.next() 
#返回下一行，并将文件操作标记位移到下一行。把一个file用于for ... in file这样的语句时，就是调用next()函数来实现遍历的。 
F.seek(offset[,whence]) 
#将文件打操作标记移到offset的位置。这个offset一般是相对于文件的开头来计算的，一般为正数。但如果提供了whence参数就不一定了，whence可以为0表示从头开始计算，1表示以当前位置为原点计算。2表示以文件末尾为原点进行计算。需要注意，如果文件以a或a+的模式打开，每次进行写操作时，文件操作标记会自动返回到文件末尾。 
F.truncate([size]) 
#把文件裁成规定的大小，默认的是裁到当前文件操作标记的位置。如果size比文件的大小还要大，依据系统的不同可能是不改变文件，也可能是用0把文件补到相应的大小，也可能是以一些随机的内容加上去。 
#-----------------------------------------------------------------------------------------
#捕获异常
if __name__ == '__main__':
    try:
        a = "abc" + 1
    except Exception, e:
        print e
输出：cannot concatenate 'str' and 'int' objects
#-----------------------------------------------------------------------------------------
#编码
# encoding=utf-8
'''
Created on 2012-12-27

@author: Ben
'''    

if __name__ == '__main__':
    print "你好"
	

# encoding=utf-8
'''
Created on 2012-12-27

@author: Ben
'''    
#此时，如果文件格式为utf-8，则可以正常输出显示
if __name__ == '__main__':
    f = open("test.txt", "r")
    l = f.readline()
    print l
    f.close()

#此时，如果文件格式为ansi，则需要用gbk解码
# encoding=utf-8
if __name__ == '__main__':
    f = open("test.txt", "r")
    l = f.readline().decode("gbk")
    print l
    f.close()
或者
# encoding=utf-8
import codecs
if __name__ == '__main__':
    f = open("test.txt")
    (encoder, decoder, reader, writer) = codecs.lookup("gbk")
    f = reader(f)
    l = f.readline()
    print l
    f.close()
或者
# encoding=utf-8
import codecs

if __name__ == '__main__':
    f = codecs.open("test.txt", "r", "gbk")
    l = f.readline()
    print l
    f.close()

#通过如下代码，可以处理无论是utf-8编码还是ansi编码的文件
# encoding=utf-8
import codecs

if __name__ == '__main__':
    try:
        f = codecs.open("test.txt", "r", "utf-8")#f = codecs.open("test.txt", "r", encoding="utf-8")
        l = f.readline()
        print l
        f.close()
    except Exception, e:
        print e
        f.close()
        f = codecs.open("test.txt", "r", "gbk")
        l = f.readline()
        print l
        f.close()
		
#写utf-8，这时候如果有中文，是utf-8无bom格式；如果只有ascii字符串，则文件还是ansi格式
# encoding=utf-8
import codecs

if __name__ == '__main__':
    f = codecs.open("test.txt", "w", "utf-8")
    txt = unicode("你好\n", "utf-8")
    f.write(txt)
    f.close()
	
#如果强制要求无论有没有中文，都是utf-8格式，可以如下编写
# encoding=utf-8
import codecs

if __name__ == '__main__':
    f = codecs.open("test.txt", "w", "utf-8-sig")
    txt = unicode("qwer\n")
    f.write(txt)
    f.close()

#这个没太看懂是什么用
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

import codecs

txt = u"qwer"
file=codecs.open("test","w","utf-8")
file.write(txt)
file.close()

#什么格式都不带，自动为ansi格式，如果希望保持档案格式，则需要在写时带着wb，并且区分换行符，windows:\r\n，unix:\n，mac:\r（好像有点问题，怎么写都是windows）
# encoding=utf-8
import codecs

if __name__ == '__main__':
    f = codecs.open("test.txt", "wb")
    txt = unicode("hello\n")
    f.write(txt)
    f.close()
	
# -*- coding: UTF-8 -*- 这是个注释吗？
这是用来说明你的Python源程序文件用使用的编码。缺省情况下你的程序需要使用ascii码来写，但如果在其中写中文的话，python解释器一般会报错，但如果加上你所用的文件编码，python就会自动处理不再报错。
上述格式还可以写成：
#coding=utf-8
或
#coding:utf-8	
#-----------------------------------------------------------------------------------------
#对象持久化
# encoding=utf-8
'''
Created on 2012-12-27

@author: Ben
'''    
import pickle

if __name__ == '__main__':
    l = ["a", "b", "c"]
    f = open("test.txt", "w")
    pickle.dump(l, f)
    f.close()
#    nameMap = {"Mayian":["a", "b", "c"], "Dingben":["d", "e"]}
#    f = open("test.txt", "w")
#    pickle.dump(nameMap, f)
#    f.close()
	
# encoding=utf-8
import pickle

if __name__ == '__main__':
    f = open("test.txt", "r")
    l = pickle.load(f)
    print l
    f.close()
#    f = open("test.txt", "r")
#    nameMap = pickle.load(f)
#    print nameMap
#    f.close()

#shelve
# encoding=utf-8
import shelve

if __name__ == '__main__':
    dbase = shelve.open("test.sh")
    dbase["a"] = "A"
    dbase["b"] = "B"
    dbase.close()
	
# encoding=utf-8
import shelve

if __name__ == '__main__':
    dbase = shelve.open("test.sh")
    for key, value in dbase.items():
        print key, value
    dbase.close()
#-----------------------------------------------------------------------------------------
#win32com操纵excel
import win32com.client

if __name__ == '__main__':
    xlApp = win32com.client.DispatchEx("Excel.Application")
    xlBook = xlApp.Workbooks.Add()
    xlBook.SaveAs(r"d:\Program Files\eclipse workspace\PyLearngingProgram\main\Hello.xlsx")   
    xlBook.Close(SaveChanges=0)
    del xlApp

	
import win32com.client

if __name__ == '__main__':
    xlApp = win32com.client.DispatchEx("Excel.Application")
    excelPath = r"d:\Program Files\eclipse workspace\PyLearngingProgram\main\Hello.xlsx"
    xlBook = xlApp.Workbooks.Open(excelPath)
    sht = xlBook.Worksheets("Sheet1")
    sht.Cells(1, 1).value = "Hello"
    xlBook.Save()   
    xlBook.Close(SaveChanges=0)
    del xlApp
#-----------------------------------------------------------------------------------------
#量水问题
# encoding=utf-8
import sys
import copy 
#transfer matrix
gmat = 0 
maxint = 99999999 
def build_state_matrix(size):
    global gmat
    global maxint    
    gmat = size*[maxint]
    tmp = []
    for i in range(size):
        tmp.append(copy.deepcopy(gmat))        
    gmat = tmp 

def draw_lines(mystate,a,b):
    global gmat    
    aa = mystate[0]
    bb = mystate[1]    
    state_ord = aa*(b+1) + bb    
    if (aa!=0) :    
    #clean a , so (aa,bb)--> (0,bb)       
        next_state_ord =  bb        
        gmat[state_ord][next_state_ord] = 1    
    if (bb!=0) :    
    #clean b , so (aa,bb)--> (aa,0)        
        next_state_ord =  aa*(b+1)        
        gmat[state_ord][next_state_ord] = 1    
    if (aa!=a) :    
    #full a , so (aa,bb)--> (a,bb)        
        next_state_ord =  a*(b+1) + bb        
        gmat[state_ord][next_state_ord] = 1    
    if (bb!=b) :    
    #full b , so (aa,bb)--> (aa,b)        
        next_state_ord =  aa*(b+1) + b        
        gmat[state_ord][next_state_ord] = 1    
    if (aa!=0) :    
    #a to b , 1 final,a get empty    
    #(aa,bb)--->(0,bb+aa)        
        if((bb+aa)<=b) :            
            next_state_ord = bb+aa ;            
            gmat[state_ord][next_state_ord] = 1        
        #a to b , 2 final,b get full    
        #(aa,bb)--->(aa+bb-b,b,)        
        if((aa+bb-b)>=0) :            
            next_state_ord = (aa+bb-b)*(b+1) + b            
            gmat[state_ord][next_state_ord] = 1    
    if (bb!=0) :    
    #b to a , 1 final,b get empty    
    #(aa,bb)--->(aa+bb,0)        
        if((bb+aa)<=a) :            
            next_state_ord = (aa+bb)*(b+1) ;            
            gmat[state_ord][next_state_ord] = 1       
        #a to b , 2 final,a get full    
        #(aa,bb)--->(a,aa+bb-a)        
        if((aa+bb-a)>=0) :            
            next_state_ord = a*(b+1) + (aa+bb-a)            
            gmat[state_ord][next_state_ord] = 1                 

def dijkstra(mat,s,e,n) :    
    path = []    
    pair = {}.fromkeys(range(n))    
    global maxint    
    d = n*[maxint]    
    flag = n*[0]    
    for i in range(n) :        
        if(mat[s][i]==1) :            
            d[i] = 1             
            pair[i] = s        
        else : 
            d[i] = maxint     
    flag[s] = 1    
    while True :        
        mint = maxint        
        key = -1        
        for i in range(n) :            
            if(flag[i]==1) : continue            
            if ( d[i] < mint ) :                
                mint = d[i] ;                
                key = i        
        if(key==-1) : 
            return -1        
        flag[key]=1        
        if(key==e) : break        
        for i in range(n) :            
            if (i==key) : 
                continue             
            if ( mat[key][i] !=1 ) : 
                continue             
            if ( d[i] > (d[key]+mat[key][i]) ) :                
                d[i] = d[key] + mat[key][i]                 
                pair[i] = key    
    path.insert(0,e)    
    parent = pair[e]    
    while (parent!=0) :        
        path.insert(0,parent)        
        parent = pair[parent]    
    path.insert(0,0)    
    return path    

def pour(a,b,c) :    
    #1st step,build state     
    size = (a+1)*(b+1)    
    build_state_matrix(size)    
    #(0,0) is the 0th state    
    #(m,n) is the m*(b+1) + n     
    #2nd step,draw the arrows    
    global gmat    
    for aa in range(a+1) :        
        for bb in range(b+1) :            
            #(aa,bb)            
            #six operation            
            # clean A            
            # clean B            
            # fill A            
            # fill B            
            # A to B            
            # B to A            
            draw_lines((aa,bb),a,b)                
    #dijkstra to find the best way    
    s = 0 #(0,0)    
    e = c  #(0,c)    
    if c <= b :        
        path1 = dijkstra(gmat,s,e,size)    
    else : 
        path1 = -1    
    e = c*(b+1) #(c,0)    
    if c <= a :        
        path2 = dijkstra(gmat,s,e,size)    
    else : 
        path2 = -1        
    
    if( path1 == -1 and path2 == -1 ) :        
        print "No solution!"        
        return []    
    if ( path1 == -1 ) :        
        path = path2    
    elif ( path2 == -1 ) :       
        path = path1    
    else :        
        if(len(path1)<len(path2)) :            
            path = path1        
        else : path = path2    
    print len(path)    
    for i in path :        
        bi = i%(b+1)        
        ai = (i-bi)/(b+1)        
        print "(","%d" % ai,"%d" % bi ,")" ,     
        print      

if __name__ == "__main__" :    
#    a = int(sys.argv[1])    
#    b = int(sys.argv[2])    
#    c = int(sys.argv[3])    
    a = 5
    b = 3
    c = 2
    pour(a,b,c)
#-----------------------------------------------------------------------------------------
#python的函数传递，类似C++的函数指针
def printHello(msg):
    print msg
    
def functionHandler(functionName, msg):
    functionName(msg)
    
if __name__ == '__main__': 
    functionHandler(printHello, "hello, world")
#-----------------------------------------------------------------------------------------
py2exe使用语句
from distutils.core import setup
import py2exe

setup(windows=[{"script": "D:/Program Files/eclipse workspace/PyLearngingProgram/main/main.py", "icon_resources": [(1, "D:/Installations/python-2/py2exe/1.ico")]}])
#setup(console=[{"script": "D:/Program Files/eclipse workspace/PyLearngingProgram/main/main.py", "icon_resources": [(1, "D:/Installations/python-2/py2exe/1.ico")]}])
#-----------------------------------------------------------------------------------------
类的self可以理解为C++中的this
def thisTest(test):
    print test.l

class Test(object):
    def __init__(self):
        self.l = ["abc", "def"]
    
    def show(self):
        thisTest(self)
        
if __name__ == '__main__': 
    test = Test()
    test.show()
#-----------------------------------------------------------------------------------------
*var在定义函数参数时，是可变个数参数的意思。在调用时，是要unpack tuple的意思。如：
t = (1,2,3)直接带入是一个参数，但是*t后就是3个参数了

#@PydevCodeAnalysisIgnore
#encoding=utf-8
#!/usr/bin/env python

def testStar(a, b, c, d):
    print a, b, c, d
    
def testStar2(*arg):
    print arg
    
if __name__ == '__main__':     
    a = (1, 2, 3, 4)
    testStar(*a)
    testStar2(a)
    testStar2(*a)
    
    b = [11, 12, 13, 14]
    testStar(*b)
    testStar2(b)
    testStar2(*b)

输出如下：
1 2 3 4
((1, 2, 3, 4),)
(1, 2, 3, 4)
11 12 13 14
([11, 12, 13, 14],)
(11, 12, 13, 14)
#-----------------------------------------------------------------------------------------
#三元操作符的替代实现
下面看一下python的解决方案一：
def trans(v):  
	return 1 if v==0 else v  
也就是if else语句的简写形式，意思一看就明白，看起来还不错。   
   
下面是解决方案二：
def trans(v):  
    return v==0 and 1 or v  
用到了与或运算符的特性。
解释一下：如果v等于0为true，则跟1做与运算，为true，则不进行后面的或运算，直接返回1；如果v等于0为false，则跟1做与运算，为false，继续进行或运算，返回v。
#-----------------------------------------------------------------------------------------
#生成xml
'''
Created on 2013-9-29

@author: Ben
'''

import xml.dom.minidom as Dom  
  
class XmlGenerator:  
    def __init__(self, xml_name):  
        self.doc = Dom.Document()  
        self.xml_name = xml_name  
          
    def createNode(self, node_name):  
        return self.doc.createElement(node_name)  
      
    def addNode(self, node, prev_node = None):  
        cur_node = node  
        if prev_node is not None:  
            prev_node.appendChild(cur_node)  
        else:  
            self.doc.appendChild(cur_node)  
        return cur_node  
  
    def setNodeAttr(self, node, att_name, value):  
        cur_node = node  
        cur_node.setAttribute(att_name, value)  
  
    def setNodeValue(self, cur_node, value):  
        node_data = self.doc.createTextNode(value)  
        cur_node.appendChild(node_data)  
  
    def genXml(self):  
        f = open(self.xml_name, "w")  
        f.write(self.doc.toprettyxml(indent = "\t", newl = "\n", encoding = "utf-8"))  
        f.close()  
          
if __name__ == "__main__":  
    myXMLGenerator = XmlGenerator("book_store.xml")  
      
    #xml root node  
    node_book_store = myXMLGenerator.createNode("book_store")  
    myXMLGenerator.setNodeAttr(node_book_store, "name", "new hua")  
    myXMLGenerator.setNodeAttr(node_book_store, "website", "http://www.ourunix.org")  
    myXMLGenerator.addNode(node = node_book_store)  
      
    #book01  
    node_book_01 = myXMLGenerator.createNode("book")
    myXMLGenerator.setNodeAttr(node_book_01, "index", "1")
      
    node_book_01_name = myXMLGenerator.createNode("name")  
    myXMLGenerator.setNodeValue(node_book_01_name, "Hamlet")  
    myXMLGenerator.addNode(node_book_01_name, node_book_01)  
      
    node_book_01_author = myXMLGenerator.createNode("author")  
    myXMLGenerator.setNodeValue(node_book_01_author, "William Shakespeare")  
    myXMLGenerator.addNode(node_book_01_author, node_book_01)  
  
    node_book_01_price = myXMLGenerator.createNode("price")  
    myXMLGenerator.setNodeValue(node_book_01_price, "$20")  
    myXMLGenerator.addNode(node_book_01_price, node_book_01)  
  
    node_book_01_grade = myXMLGenerator.createNode("grade")  
    myXMLGenerator.setNodeValue(node_book_01_grade, "good")  
    myXMLGenerator.addNode(node_book_01_grade, node_book_01)  
      
    myXMLGenerator.addNode(node_book_01, node_book_store)  
      
    #book 02  
    node_book_02 = myXMLGenerator.createNode("book")  
    myXMLGenerator.setNodeAttr(node_book_02, "index", "2")
      
    node_book_02_name = myXMLGenerator.createNode("name")  
    myXMLGenerator.setNodeValue(node_book_02_name, "shuihu")  
    myXMLGenerator.addNode(node_book_02_name, node_book_02)  
      
    node_book_02_author = myXMLGenerator.createNode("author")  
    myXMLGenerator.setNodeValue(node_book_02_author, "naian shi")  
    myXMLGenerator.addNode(node_book_02_author, node_book_02)  
  
    node_book_02_price = myXMLGenerator.createNode("price")  
    myXMLGenerator.setNodeValue(node_book_02_price, "$200")  
    myXMLGenerator.addNode(node_book_02_price, node_book_02)  
  
    node_book_02_grade = myXMLGenerator.createNode("grade")  
    myXMLGenerator.setNodeValue(node_book_02_grade, "good")  
    myXMLGenerator.addNode(node_book_02_grade, node_book_02)  
      
    myXMLGenerator.addNode(node_book_02, node_book_store)  
      
    #gen  
    myXMLGenerator.genXml()  
#-----------------------------------------------------------------------------------------
#enumerate方法
enumerate Found at: __builtin__
enumerate(iterable[, start]) -> iterator for index, value of iterable
    
    Return an enumerate object.  iterable must be another object that supports
    iteration.  The enumerate object yields pairs containing a count (from
    start, which defaults to zero) and a value yielded by the iterable argument.
    enumerate is useful for obtaining an indexed list:
    (0, seq[0]), (1, seq[1]), (2, seq[2]), ...
	
import sys
rows = (("hello", "world", "good"), ("nice", "day", "how"))

if __name__ == '__main__':
    for item in rows:
        #index = f.list.InsertStringItem(sys.maxint, item[0])
        print sys.maxint, item[0]
        for col, text in enumerate(item[1:]):
            #self.list.SetStringItem(index, col + 1, text)
            print col, text
输出如下：
2147483647 hello
0 world
1 good
2147483647 nice
0 day
1 how

#-----------------------------------------------------------------------------------------
#使用ElementTree生成xml
import sys
import time
import string
 
import xml.etree.ElementTree as etree

#设置默认字符集为UTF8 不然有些时候转码会出问题
default_encoding = 'utf-8'
if sys.getdefaultencoding() != default_encoding:
    reload(sys)
    sys.setdefaultencoding(default_encoding)

def create_xml():

    data = etree.Element("data")
    #1 interface_version
    interface_version_txt = '5'
    interface_version = etree.SubElement(data, 'interface_version')
    interface_version.text = interface_version_txt
    #2 site
    site_txt = 'www.xxx.com'
    site = etree.SubElement(data, 'site')
    site.text = site_txt
    #3 lastmod
    lastmod_txt = time.strftime('%Y-%m-%d', time.localtime())
    lastmod = etree.SubElement(data, 'lastmod')
    lastmod.text = lastmod_txt
    #5 app
    app = etree.SubElement(data, 'app')
    #6 title 
    title_txt = u'%s' % '真心话大冒险'
    #title_txt = etree.CDATA(title_txt)
    title = etree.SubElement(app, 'title')
    title.text = title_txt
    #7 appid
    appid = etree.SubElement(app, 'appid')
    appid.text = '%s' % '222'

    #dataxml = etree.tostring(data, pretty_print=True, encoding="UTF-8", method="xml", xml_declaration=True, standalone=None)
    dataxml = etree.tostring(data, encoding="UTF-8", method="xml")
    print dataxml
    
     
if __name__ == '__main__':
    create_xml()

输出如下：
<?xml version='1.0' encoding='UTF-8'?>
<data><interface_version>5</interface_version><site>www.xxx.com</site><lastmod>2016-06-11</lastmod><app><title>真心话大冒险</title><appid>222</appid></app></data>


#coding:utf-8

'''
Created on 2016年6月10日

@author: Ben
'''

from xml.etree.ElementTree import Element, ElementTree

if __name__ == '__main__':
    root = Element('bookstore')
    tree = ElementTree(root)
    
    #设置1级子节点
    child0 = Element('book', {'category' : "COOKING"} )
    root.append(child0)
    
    #设置2级子节点
    child00 = Element('title', {'language' : "English"} )
    child00.text = 'Everyday Italian' #2级子节点文本
    child0.append(child00)
    
    tree.write('test.xml', encoding="UTF-8", method="xml", xml_declaration=True)
    

http://pycoders-weekly-chinese.readthedocs.io/en/latest/issue6/processing-xml-in-python-with-element-tree.html
ElementTree － 一个 API ，两种实现
ElementTree 生来就是为了处理 XML ，它在 Python 标准库中有两种实现。一种是纯 Python 实现例如 xml.etree.ElementTree ，另外一种是速度快一点的 xml.etree.cElementTree 。你要记住： 尽量使用 C 语言实现的那种，因为它速度更快，而且消耗的内存更少。如果你的电脑上没有 _elementtree (见注释4) 那么你需要这样做：

try:
    import xml.etree.cElementTree as ET
except ImportError:
    import xml.etree.ElementTree as ET
#-----------------------------------------------------------------------------------------
#lambda表达式
http://www.cnblogs.com/evening/archive/2012/03/29/2423554.html
# encoding=utf-8


def add(a, b):
    return a + b


if __name__ == "__main__":
    g = lambda x: x + 1
    print g(1)

    print (lambda x: x + 2)(10)

    foo = [2, 18, 9, 22, 17, 24, 8, 12, 27]
    listAfterFilter = filter(lambda x: x % 3 == 0, foo)
    print listAfterFilter

    l1 = [1, 2, 3]
    l2 = [4, 5, 6]
    print "one sequence: ", map(lambda x: x + 2, l1)  # 单序列映射
    print "two sequence: ", map(lambda x, y: x + y, l1, l2)  # 两个或者多序列映射
    print "three sequences: ", map(lambda x, y, z: x + y + z, l1, l2, [7, 8, 9])

    print reduce(lambda x, y: x + y, l1)  # 无初始值
    print reduce(lambda x, y: x + y, l1, 10)  # 有初始值
    print reduce(add, l1, 20)  # 使用函数
    print reduce(lambda sumLen, s: sumLen + len(s), ["abcd", "de", "f"], 0) #计算迭代对象长度和，需要将起始值（或中间累加值）定义在lambda表达式的第一个参数。如果不提供初始值0，则将报错TypeError: cannot concatenate 'str' and 'int' objects

    print all([1, 2, 3]), all([0, 1, 2, 3]) #是否所有的迭代对象都为True
    print any([1, 2, 3]), any([0, 1, 2, 3]) #是否有任意一个迭代对象为True
输出：
2
12
[18, 9, 24, 12, 27]
one sequence:  [3, 4, 5]
two sequence:  [5, 7, 9]
three sequences:  [12, 15, 18]
6
16
26
7
True False
True True

#-----字符串的split和join函数
# encoding=utf-8


if __name__ == "__main__":
    s = "a b   c"
    print s.split()  # 无参数则使用" "分隔，["a", "b", "c"]
    print " ".join(s.split()) #用第一个str变量来连接所有对象 a b c
#-----是否要使用lambda表达式
# encoding=utf-8


def getProcess(collapse):
    return collapse and (lambda s: " ".join(s.split())) or (lambda s: s)


def getStr(collapse, s):
    if collapse:
        return " ".join(s.split())
    else:
        return s


if __name__ == "__main__":
    # print getProcess(True)("a   b  c")
    processFunc = lambda collapse: collapse and (lambda s: " ".join(s.split())) or (lambda s: s) #lambda表达式函数，入参是一个bool值，返回值是一个lambda表达式
    testStr = "a   b  c"
    print processFunc(True)(testStr) #先传入True，拿到的是一个lambda表达式：lambda s: " ".join(s.split())，给该lambda表达式传入"a   b  c"，将进行压缩空格处理
    # print getStr(True, testStr) #同样的操作，需要5行定义+实现
    print processFunc(False)(testStr) #先传入False，拿到是一个lambdag表达式：lambda s: s，返回入参数对象

    
通过此例子，我们发现，lambda的使用大量简化了代码，使代码简练清晰。但是值得注意的是，这会在一定程度上降低代码的可读性。如果不是非常熟悉python的人或许会对此感到不可理解。
--lambda 定义了一个匿名函数
--lambda 并不会带来程序运行效率的提高，只会使代码更简洁。
--如果可以使用for...in...if来完成的，坚决不用lambda。
--如果使用lambda，lambda内不要包含循环，如果有，我宁愿定义函数来完成，使代码获得可重用性和更好的可读性。
总结：lambda 是为了减少单行函数的定义而存在的。
#-----------------------------------------------------------------------------------------
#用lambda表达式判断小括号是否匹配
# encoding=utf-8

def balance(chars):
    def f(cnt, c):
        if cnt >= 0:
            if c == '(':
                cnt += 1
            elif c == ')':
                cnt -= 1
        return cnt

    return 0 == reduce(lambda cnt, c: f(cnt, c), chars, 0)  # 在reduce中所要求的第一个参数function中，要求第一个参数是起始迭代值
    # return 0 == reduce(lambda c, cnt: f(cnt, c), chars, 0) #如果将lambda表达式定义为 lambda c, cnt，则起始值0会赋值给c，而字符串第一个字符会赋值给cnt


def balance_recurse(chars, cnt=0):
    if len(chars) == 0:
        return cnt == 0

    if chars[0] == "(":
        cnt += 1
    elif chars[0] == ")":
        cnt -= 1
    return cnt >= 0 and balance_recurse(chars[1:], cnt)


PARENS = (("(", ")"), ("[", "]"), ("{", "}"))
def balance_recurse_mul(chars, cnt=[0] * len(PARENS)):
    if len(chars) == 0:
        return all(map(lambda b: b == 0, cnt))
    for i, paren in enumerate(PARENS):  # 生成index, seq[index]迭代对象
        if chars[0] == paren[0]:
            cnt[i] += 1
        elif chars[0] == paren[1]:
            cnt[i] -= 1
    return all(map(lambda b: b >= 0, cnt)) and balance_recurse_mul(chars[1:], cnt)


if __name__ == "__main__":
    print balance(")()(")
    print balance("(())")
    print balance("()()")
    
    print balance_recurse(")()(")
    print balance_recurse("(())")
    print balance_recurse("()()")

    print balance_recurse_mul("{([)]}")

输出：
False
True
True
False
True
True
True
#-----------------------------------------------------------------------------------------
#dict的三种构造方式
# coding=utf-8

if __name__ == "__main__":
    d = {'a': 10, 'b': 20}
    print d

    d = dict(a=10, b=20)
    print d

    l = [("a", 10), ("b", 20), ('c', 30)]
    d = dict(l)
    print d
输出：
{'a': 10, 'b': 20}
{'a': 10, 'b': 20}
{'a': 10, 'c': 30, 'b': 20}
#-----------------------------------------------------------------------------------------
#爬虫学习http://python.jobbole.com/81336/
# coding=utf-8
import urllib2

if __name__ == "__main__":
    response = urllib2.urlopen("http://www.baidu.com")
    print response.read()

#post方式登录 
# coding=utf-8
import urllib
import urllib2

if __name__ == "__main__":
    values = {"username": "bigben0204", "password": "ilovesjtu8624"}
    data = urllib.urlencode(values)
    url = "https://passport.csdn.net/account/login?from=http://my.csdn.net/my/mycsdn"
    request = urllib2.Request(url, data)
    response = urllib2.urlopen(request)
    print response.read()

#get方式登录
# coding=utf-8
import urllib
import urllib2

if __name__ == "__main__":
    values = {'username': "bigben0204", 'password': "ilovesjtu8624"}
    data = urllib.urlencode(values)
    url = "http://passport.csdn.net/account/login"
    geturl = url + "?" + data
    request = urllib2.Request(geturl)
    response = urllib2.urlopen(request)
    print response.read()
    
#设置Headers
# coding=utf-8
import urllib
import urllib2

if __name__ == "__main__":
    user_agent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.221 Safari/537.36 SE 2.X MetaSr 1.0'
    referer = 'https://passport.csdn.net/account/verify'
    headers = {'User-Agent': user_agent,
               'Referer': referer,
               'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
               # 'Accept-Encoding': 'gzip, deflate, sdch',
               'Accept-Language': 'zh-CN,zh;q=0.8',
               'Connection': 'keep-alive',
               }

    values = {'username': "bigben0204", 'password': "ilovesjtu8624"}
    data = urllib.urlencode(values)

    url = "https://passport.csdn.net/account/login?from=http://my.csdn.net/my/mycsdn"
    request = urllib2.Request(url, data, headers)
    response = urllib2.urlopen(request)
    page = response.read()
    # print page
    file_object = open('test.html', 'w')
    file_object.write(page)
    file_object.close()

#使用DebugLog
# coding=utf-8
import urllib2

if __name__ == "__main__":
    httpHandler = urllib2.HTTPHandler(debuglevel=1)
    httpsHandler = urllib2.HTTPSHandler(debuglevel=1)
    opener = urllib2.build_opener(httpHandler, httpsHandler)
    urllib2.install_opener(opener)
    response = urllib2.urlopen('http://www.baidu.com')
    page = response.read()
    # print page
    file_object = open('test.html', 'w')
    file_object.write(page)
    file_object.close()
输出：
send: 'GET / HTTP/1.1\r\nAccept-Encoding: identity\r\nHost: www.baidu.com\r\nConnection: close\r\nUser-Agent: Python-urllib/2.7\r\n\r\n'
reply: 'HTTP/1.1 200 OK\r\n'
header: Date: Tue, 09 Jan 2018 15:19:11 GMT
header: Content-Type: text/html; charset=utf-8
header: Transfer-Encoding: chunked
header: Connection: Close
header: Vary: Accept-Encoding
header: Set-Cookie: BAIDUID=FE00878D716349FA8FF9E5C929E7F6F2:FG=1; expires=Thu, 31-Dec-37 23:55:55 GMT; max-age=2147483647; path=/; domain=.baidu.com
header: Set-Cookie: BIDUPSID=FE00878D716349FA8FF9E5C929E7F6F2; expires=Thu, 31-Dec-37 23:55:55 GMT; max-age=2147483647; path=/; domain=.baidu.com
header: Set-Cookie: PSTM=1515511151; expires=Thu, 31-Dec-37 23:55:55 GMT; max-age=2147483647; path=/; domain=.baidu.com
header: Set-Cookie: BDSVRTM=0; path=/
header: Set-Cookie: BD_HOME=0; path=/
header: Set-Cookie: H_PS_PSSID=1431_19036_21085_20718; path=/; domain=.baidu.com
header: P3P: CP=" OTI DSP COR IVA OUR IND COM "
header: Cache-Control: private
header: Cxy_all: baidu+b8c667b8da39e37523e1b3045d5162a9
header: Expires: Tue, 09 Jan 2018 15:18:59 GMT
header: X-Powered-By: HPHP
header: Server: BWS/1.1
header: X-UA-Compatible: IE=Edge,chrome=1
header: BDPAGETYPE: 1
header: BDQID: 0xd5adebaf000038b6
header: BDUSERID: 0

#URLError
# coding=utf-8
import urllib2

if __name__ == "__main__":
    requset = urllib2.Request('http://www.xxxxx.com')
    try:
        urllib2.urlopen(requset)
    except urllib2.URLError, e:
        print e.reason
输出：
[Errno 10060] 

#2.HTTPError
# coding=utf-8
import urllib2

if __name__ == "__main__":
    req = urllib2.Request('http://blog.csdn.net/cqcre')
    try:
        urllib2.urlopen(req)
    except urllib2.HTTPError, e:
        print e.code
    except urllib2.URLError, e:
        print e.reason
    else:
        print "OK"
#提前对属性判断
# coding=utf-8
import urllib2

if __name__ == "__main__":
    req = urllib2.Request('http://blog.csdn.net/cqcre')
    try:
        urllib2.urlopen(req)
    except urllib2.URLError, e:
        if hasattr(e, "code"):
            print e.code
        if hasattr(e, "reason"):
            print e.reason
    else:
        print "OK"
        

#获取Cookie保存到变量
# coding=utf-8
import cookielib
import urllib2

if __name__ == "__main__":
    cookie = cookielib.CookieJar()
    # 利用urllib2库的HTTPCookieProcessor对象来创建cookie处理器
    handler = urllib2.HTTPCookieProcessor(cookie)
    # 通过handler来构建opener
    opener = urllib2.build_opener(handler)
    # 此处的open方法同urllib2的urlopen方法，也可以传入request
    response = opener.open('http://www.baidu.com')
    for item in cookie:
        print 'Name = ' + item.name
        print 'Value = ' + item.value

#保存Cookie到文件
# coding=utf-8
import cookielib
import urllib2

if __name__ == "__main__":
    # 设置保存cookie的文件，同级目录下的cookie.txt
    filename = 'cookie.txt'
    # 声明一个MozillaCookieJar对象实例来保存cookie，之后写入文件
    cookie = cookielib.MozillaCookieJar(filename)
    # 利用urllib2库的HTTPCookieProcessor对象来创建cookie处理器
    handler = urllib2.HTTPCookieProcessor(cookie)
    # 通过handler来构建opener
    opener = urllib2.build_opener(handler)
    # 创建一个请求，原理同urllib2的urlopen
    response = opener.open("http://www.baidu.com")
    # 保存cookie到文件
    cookie.save(ignore_discard=True, ignore_expires=True)
    
#从文件中获取Cookie并访问
# coding=utf-8
import cookielib
import urllib2

if __name__ == "__main__":
    # 创建MozillaCookieJar实例对象
    cookie = cookielib.MozillaCookieJar()
    # 从文件中读取cookie内容到变量
    cookie.load('cookie.txt', ignore_discard=True, ignore_expires=True)
    # 创建请求的request
    req = urllib2.Request("http://www.baidu.com")
    # 利用urllib2的build_opener方法创建一个opener
    opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookie))
    response = opener.open(req)
    print response.read()
    

#利用cookie模拟网站登录
import urllib
import urllib2
import cookielib
 
filename = 'cookie.txt'
#声明一个MozillaCookieJar对象实例来保存cookie，之后写入文件
cookie = cookielib.MozillaCookieJar(filename)
opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookie))
postdata = urllib.urlencode({
			'stuid':'201200131012',
			'pwd':'23342321'
		})
#登录教务系统的URL
loginUrl = 'http://jwxt.sdu.edu.cn:7890/pls/wwwbks/bks_login2.login'
#模拟登录，并把cookie保存到变量
result = opener.open(loginUrl,postdata)
#保存cookie到cookie.txt中
cookie.save(ignore_discard=True, ignore_expires=True)
#利用cookie请求访问另一个网址，此网址是成绩查询网址
gradeUrl = 'http://jwxt.sdu.edu.cn:7890/pls/wwwbks/bkscjcx.curscopre'
#请求访问成绩查询网址
result = opener.open(gradeUrl)
print result.read()

创建一个带有cookie的opener，在访问登录的URL时，将登录后的cookie保存下来，然后利用这个cookie来访问其他网址。
如登录之后才能查看的成绩查询呀，本学期课表呀等等网址，模拟登录就这么实现啦

4.Python Re模块
Python 自带了re模块，它提供了对正则表达式的支持。主要用到的方法列举如下
#返回pattern对象
re.compile(string[,flag])  
#以下为匹配所用函数
re.match(pattern, string[, flags])
re.search(pattern, string[, flags])
re.split(pattern, string[, maxsplit])
re.findall(pattern, string[, flags])
re.finditer(pattern, string[, flags])
re.sub(pattern, repl, string[, count])
re.subn(pattern, repl, string[, count])

? re.I(全拼：IGNORECASE): 忽略大小写（括号内是完整写法，下同）
? re.M(全拼：MULTILINE): 多行模式，改变'^'和'$'的行为（参见上图）
? re.S(全拼：DOTALL): 点任意匹配模式，改变'.'的行为
? re.L(全拼：LOCALE): 使预定字符类 \w \W \b \B \s \S 取决于当前区域设定
? re.U(全拼：UNICODE): 使预定字符类 \w \W \b \B \s \S \d \D 取决于unicode定义的字符属性
? re.X(全拼：VERBOSE): 详细模式。这个模式下正则表达式可以是多行，忽略空白字符，并可以加入注释。

//
# coding=utf-8
import re

if __name__ == "__main__":
    pattern = re.compile(r'hello')
    # 使用re.match匹配文本，获得匹配结果，无法匹配时将返回None
    result1 = re.match(pattern, 'hello hello')
    result2 = re.match(pattern, 'helloo CQC!') #必须从头匹配，如果aahello将无法匹配
    result3 = re.match(pattern, 'helo CQC!')
    result4 = re.match(pattern, 'hello CQC!')

    # 如果1匹配成功
    if result1:
        # 使用Match获得分组信息
        print result1.group()
        print result1.string
        print result1.re #同 pattern
        print result1.endpos
        print result1.lastindex
        print result1.lastgroup
    else:
        print '1匹配失败！'

    # 如果2匹配成功
    if result2:
        # 使用Match获得分组信息
        print result2.group()
    else:
        print '2匹配失败！'

    # 如果3匹配成功
    if result3:
        # 使用Match获得分组信息
        print result3.group()
    else:
        print '3匹配失败！'

    # 如果4匹配成功
    if result4:
        # 使用Match获得分组信息
        print result4.group()
    else:
        print '4匹配失败！'
输出：
hello
hello hello
<_sre.SRE_Pattern object at 0x04A1B840>
11
None
None
hello
3匹配失败！
hello

匹配分析
1.第一个匹配，pattern正则表达式为’hello’，我们匹配的目标字符串string也为hello，从头至尾完全匹配，匹配成功。
2.第二个匹配，string为helloo CQC，从string头开始匹配pattern完全可以匹配，pattern匹配结束，同时匹配终止，后面的o CQC不再匹配，返回匹配成功的信息。
3.第三个匹配，string为helo CQC，从string头开始匹配pattern，发现到 ‘o’ 时无法完成匹配，匹配终止，返回None
4.第四个匹配，同第二个匹配原理，即使遇到了空格符也不会受影响。

#例子
# coding=utf-8
import re

if __name__ == "__main__":
    m = re.match(r'(\w+) (\w+)(?P<sign>.*)', 'hello world!') #!hello world!将匹配不上

    print "m.string:", m.string
    print "m.re:", m.re
    print "m.pos:", m.pos
    print "m.endpos:", m.endpos
    print "m.lastindex:", m.lastindex #指最后一个分组索引，即(?P<sign>.*)的索引数
    print "m.lastgroup:", m.lastgroup #指最后一个分组的别名
    print "m.group():", m.group() #匹配到的字符串
    print "m.group(1,2):", m.group(1, 2)
    print "m.groups():", m.groups()
    print "m.groupdict():", m.groupdict()
    print "m.start(2):", m.start(2) #第2个分组起始位置
    print "m.end(2):", m.end(2) #第2个分组结束位置
    print "m.span(2):", m.span(2) #第2个分组起始和结束位置
    print r"m.expand(r'\g \g\g'):", m.expand(r'\2 \1\3')
输出：
m.string: hello world!
m.re: <_sre.SRE_Pattern object at 0x02719760>
m.pos: 0
m.endpos: 12
m.lastindex: 3
m.lastgroup: sign
m.group(): hello world!
m.group(1,2): ('hello', 'world')
m.groups(): ('hello', 'world', '!')
m.groupdict(): {'sign': '!'}
m.start(2): 6
m.end(2): 11
m.span(2): (6, 11)
m.expand(r'\g \g\g'): world hello!

（2）re.search(pattern, string[, flags])
search方法与match方法极其类似，区别在于match()函数只检测re是不是在string的开始位置匹配，search()会扫描整个string查找匹配，match（）只有在0位置匹配成功的话才有返回，如果不是开始位置匹配成功的话，match()就返回None。同样，search方法的返回对象同样match()返回对象的方法和属性。我们用一个例子感受一下
# coding=utf-8
import re

if __name__ == "__main__":
    # 将正则表达式编译成Pattern对象
    pattern = re.compile(r'world')
    # 使用search()查找匹配的子串，不存在能匹配的子串时将返回None
    # 这个例子中使用match()无法成功匹配
    match = re.search(pattern, 'hello world!')
    if match:
        # 使用Match获得分组信息
        print match.group()
    ### 输出 ###
    # world

（3）re.split(pattern, string[, maxsplit])
按照能够匹配的子串将string分割后返回列表。maxsplit用于指定最大分割次数，不指定将全部分割。我们通过下面的例子感受一下。
# coding=utf-8
import re

if __name__ == "__main__":
    pattern = re.compile(r'\d+')
    print re.split(pattern, 'one1two2three3four4')

    ### 输出 ###
    # ['one', 'two', 'three', 'four', '']

（4）re.findall(pattern, string[, flags])
搜索string，以列表形式返回全部能匹配的子串。我们通过这个例子来感受一下
# coding=utf-8
import re

if __name__ == "__main__":
    pattern = re.compile(r'\d+')
    print re.findall(pattern, 'one1two2three3four4')

    ### 输出 ###
    # ['1', '2', '3', '4']

（5）re.finditer(pattern, string[, flags])
搜索string，返回一个顺序访问每一个匹配结果（Match对象）的迭代器。我们通过下面的例子来感受一下
# coding=utf-8
import re

if __name__ == "__main__":
    pattern = re.compile(r'\d+')
    for m in re.finditer(pattern, 'one1two2three3four4'):
        print m.group(),

    ### 输出 ###
    # 1 2 3 4

（6）re.sub(pattern, repl, string[, count])
使用repl替换string中每一个匹配的子串后返回替换后的字符串。
当repl是一个字符串时，可以使用\id或\g、\g引用分组，但不能使用编号0。
当repl是一个方法时，这个方法应当只接受一个参数（Match对象），并返回一个字符串用于替换（返回的字符串中不能再引用分组）。
count用于指定最多替换次数，不指定时全部替换。
# coding=utf-8
import re


def func(m):
    return m.group(1).title() + ' ' + m.group(2).title()


if __name__ == "__main__":
    pattern = re.compile(r'(\w+) (\w+)')
    s = 'i say, hello world!'

    print re.sub(pattern, r'\2 \1', s)
    print re.sub(pattern, func, s)
    ### output ###
    # say i, world hello!
    # I Say, Hello World!

（7）re.subn(pattern, repl, string[, count])
返回 (sub(repl, string[, count]), 替换次数)。
# coding=utf-8
import re


def func(m):
    return m.group(1).title() + ' ' + m.group(2).title()


if __name__ == "__main__":
    pattern = re.compile(r'(\w+) (\w+)')
    s = 'i say, hello world!'

    print re.subn(pattern, func, s) #pattern.subn(func, s)
    print re.subn(pattern, r'\2 \1', s) #pattern.subn(r'\2 \1', s)

    ### output ###
    # ('I Say, Hello World!', 2)
    # ('say i, world hello!', 2)

5. Python Re模块的另一种使用方式
在上面我们介绍了7个工具方法，例如match，search等等，不过调用方式都是 re.match，re.search的方式，其实还有另外一种调用方式，可以通过pattern.match，pattern.search调用，这样 调用便不用将pattern作为第一个参数传入了，大家想怎样调用皆可。
Pattern.match(string[, pos[, endpos]]) | re.match(pattern, string[, flags])
Pattern.search(string[, pos[, endpos]]) | re.search(pattern, string[, flags])
Pattern.split(string[, maxsplit]) | re.split(pattern, string[, maxsplit])
Pattern.findall(string[, pos[, endpos]]) | re.findall(pattern, string[, flags])
Pattern.finditer(string[, pos[, endpos]]) | re.finditer(pattern, string[, flags])
Pattern.sub(repl, string[, count]) | re.sub(pattern, repl, string[, count])
Pattern.subn(repl, string[, count]) |re.sub(pattern, repl, string[, count])

4. 创建 Beautiful Soup 对象
# coding=utf-8

from bs4 import BeautifulSoup

html = """
<html><head><title>The Dormouse's story</title></head>
<body>
<p class="title" name="dromouse"><b>The Dormouse's story</b></p>
<p class="story">Once upon a time there were three little sisters; and their names were
<a href="http://example.com/elsie" class="sister" id="link1"><!-- Elsie --></a>,
<a href="http://example.com/lacie" class="sister" id="link2">Lacie</a> and
<a href="http://example.com/tillie" class="sister" id="link3">Tillie</a>;
and they lived at the bottom of a well.</p>
<p class="story">...</p>
"""

if __name__ == "__main__":
    soup = BeautifulSoup(html, "lxml") #如果不带lxml，则提示当前在本地使用lxml，去其他机器会显示不一致；soup = BeautifulSoup(open('index.html'))
    print soup.prettify()
输出：
<html>
 <head>
  <title>
   The Dormouse's story
  </title>
 </head>
 <body>
  <p class="title" name="dromouse">
   <b>
    The Dormouse's story
   </b>
  </p>
  <p class="story">
   Once upon a time there were three little sisters; and their names were
   <a class="sister" href="http://example.com/elsie" id="link1">
    <!-- Elsie -->
   </a>
   ,
   <a class="sister" href="http://example.com/lacie" id="link2">
    Lacie
   </a>
   and
   <a class="sister" href="http://example.com/tillie" id="link3">
    Tillie
   </a>
   ;
and they lived at the bottom of a well.
  </p>
  <p class="story">
   ...
  </p>
 </body>
</html>

5. 四大对象种类
Beautiful Soup将复杂HTML文档转换成一个复杂的树形结构,每个节点都是Python对象,所有对象可以归纳为4种:
Tag
NavigableString
BeautifulSoup
Comment
下面我们进行一一介绍
（1）Tag
Tag 是什么？通俗点讲就是 HTML 中的一个个标签，例如
print soup.title
print soup.head
print soup.a
print soup.p
输出：
<title>The Dormouse's story</title>
<head><title>The Dormouse's story</title></head>
<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>
<p class="title" name="dromouse"><b>The Dormouse's story</b></p>
我们可以利用 soup加标签名轻松地获取这些标签的内容，是不是感觉比正则表达式方便多了？不过有一点是，它查找的是在所有内容中的第一个符合要求的标签，如果要查询所有的标签，我们在后面进行介绍。

我们可以验证一下这些对象的类型
print type(soup.a)
# <class 'bs4.element.Tag'>

对于 Tag，它有两个重要的属性，是 name 和 attrs，下面我们分别来感受一下
--name
print soup.name
print soup.head.name
#[document]
#head
soup 对象本身比较特殊，它的 name 即为 [document]，对于其他内部标签，输出的值便为标签本身的名称。

--attrs
print soup.p.attrs
#{'class': ['title'], 'name': 'dromouse'}
在这里，我们把 p 标签的所有属性打印输出了出来，得到的类型是一个字典。

如果我们想要单独获取某个属性，可以这样，例如我们获取它的 class 叫什么
print soup.p['class']
#['title']
还可以这样，利用get方法，传入属性的名称，二者是等价的
print soup.p.get('class')
#['title']
我们可以对这些属性和内容等等进行修改，例如
soup.p['class']="newClass"
print soup.p
#<p class="newClass" name="dromouse"><b>The Dormouse's story</b></p>
还可以对这个属性进行删除，例如
del soup.p['class']
print soup.p
#<p name="dromouse"><b>The Dormouse's story</b></p>

（2）NavigableString
既然我们已经得到了标签的内容，那么问题来了，我们要想获取标签内部的文字怎么办呢？很简单，用 .string 即可，例如
print soup.p.string
#The Dormouse's story

这样我们就轻松获取到了标签里面的内容，想想如果用正则表达式要多麻烦。它的类型是一个 NavigableString，翻译过来叫 可以遍历的字符串，不过我们最好还是称它英文名字吧。
print type(soup.p.string)
#<class 'bs4.element.NavigableString'>

（3）BeautifulSoup
BeautifulSoup 对象表示的是一个文档的全部内容.大部分时候,可以把它当作 Tag 对象，是一个特殊的 Tag，我们可以分别获取它的类型，名称，以及属性来感受一下
print type(soup.name)
#<type 'unicode'>
print soup.name 
# [document]
print soup.attrs 
#{} 空字典

（4）Comment
Comment 对象是一个特殊类型的 NavigableString 对象，其实输出的内容仍然不包括注释符号，但是如果不好好处理它，可能会对我们的文本处理造成意想不到的麻烦。
我们找一个带注释的标签
print soup.a
print soup.a.string
print type(soup.a.string)
Python – 伯乐在线
首页所有文章观点与动态基础知识系列教程实践项目工具与框架工具资源Python小组
伯乐在线 > Python - 伯乐在线 > 所有文章 > 实践项目 > Python爬虫入门（8）：Beautiful Soup的用法
Python爬虫入门（8）：Beautiful Soup的用法

2015/04/25 · 实践项目, 系列教程 · 6 评论 · 爬虫
分享到： 27
本文作者： 伯乐在线 - 崔庆才 。未经作者许可，禁止转载！
欢迎加入伯乐在线 专栏作者。
Python爬虫入门（1）：综述
Python爬虫入门（2）：爬虫基础了解
Python爬虫入门（3）：Urllib库的基本使用
Python爬虫入门（4）：Urllib库的高级用法
Python爬虫入门（5）：URLError异常处理
Python爬虫入门（6）：Cookie的使用
Python爬虫入门（7）：正则表达式
Python爬虫入门（8）：Beautiful Soup的用法
上一节我们介绍了正则表达式，它的内容其实还是蛮多的，如果一个正则匹配稍有差池，那可能程序就处在永久的循环之中，而且有的小伙伴们也对写正则表 达式的写法用得不熟练，没关系，我们还有一个更强大的工具，叫Beautiful Soup，有了它我们可以很方便地提取出HTML或XML标签中的内容，实在是方便，这一节就让我们一起来感受一下Beautiful Soup的强大吧。

1. Beautiful Soup的简介

简单来说，Beautiful Soup是python的一个库，最主要的功能是从网页抓取数据。官方解释如下：

Beautiful Soup提供一些简单的、python式的函数用来处理导航、搜索、修改分析树等功能。它是一个工具箱，通过解析文档为用户提供需要抓取的数据，因为简单，所以不需要多少代码就可以写出一个完整的应用程序。

Beautiful Soup自动将输入文档转换为Unicode编码，输出文档转换为utf-8编码。你不需要考虑编码方式，除非文档没有指定一个编码方式，这时，Beautiful Soup就不能自动识别编码方式了。然后，你仅仅需要说明一下原始编码方式就可以了。

Beautiful Soup已成为和lxml、html6lib一样出色的python解释器，为用户灵活地提供不同的解析策略或强劲的速度。
废话不多说，我们来试一下吧~

2. Beautiful Soup 安装

Beautiful Soup 3 目前已经停止开发，推荐在现在的项目中使用Beautiful Soup 4，不过它已经被移植到BS4了，也就是说导入时我们需要 import bs4 。所以这里我们用的版本是 Beautiful Soup 4.3.2 (简称BS4)，另外据说 BS4 对 Python3 的支持不够好，不过我用的是 Python2.7.7，如果有小伙伴用的是 Python3 版本，可以考虑下载 BS3 版本。

如果你用的是新版的Debain或Ubuntu,那么可以通过系统的软件包管理来安装，不过它不是最新版本，目前是4.2.1版本

Python

sudo apt-get install Python-bs4
1
sudo apt-get install Python-bs4
如果想安装最新的版本，请直接下载安装包来手动安装，也是十分方便的方法。在这里我安装的是 Beautiful Soup 4.3.2

Beautiful Soup 3.2.1Beautiful Soup 4.3.2

下载完成之后解压

运行下面的命令即可完成安装

Python

sudo python setup.py install
1
sudo python setup.py install
如下图所示，证明安装成功了

2015-03-11 00:15:41 的屏幕截图

然后需要安装 lxml

Python

sudo apt-get install Python-lxml
1
sudo apt-get install Python-lxml
Beautiful Soup支持Python标准库中的HTML解析器,还支持一些第三方的解析器，如果我们不安装它，则 Python 会使用 Python默认的解析器，lxml 解析器更加强大，速度更快，推荐安装。

3. 开启Beautiful Soup 之旅

在这里先分享官方文档链接，不过内容是有些多，也不够条理，在此本文章做一下整理方便大家参考。

官方文档

4. 创建 Beautiful Soup 对象

首先必须要导入 bs4 库

Python

from bs4 import BeautifulSoup
1
from bs4 import BeautifulSoup
我们创建一个字符串，后面的例子我们便会用它来演示

Python

html = """
<html><head><title>The Dormouse's story</title></head>
<body>
<p class="title" name="dromouse"><b>The Dormouse's story</b></p>
<p class="story">Once upon a time there were three little sisters; and their names were
<a href="http://example.com/elsie" class="sister" id="link1"><!-- Elsie --></a>,
<a href="http://example.com/lacie" class="sister" id="link2">Lacie</a> and
<a href="http://example.com/tillie" class="sister" id="link3">Tillie</a>;
and they lived at the bottom of a well.</p>
<p class="story">...</p>
"""
1
2
3
4
5
6
7
8
9
10
11
html = """
<html><head><title>The Dormouse's story</title></head>
<body>
<p class="title" name="dromouse"><b>The Dormouse's story</b></p>
<p class="story">Once upon a time there were three little sisters; and their names were
<a href="http://example.com/elsie" class="sister" id="link1"><!-- Elsie --></a>,
<a href="http://example.com/lacie" class="sister" id="link2">Lacie</a> and
<a href="http://example.com/tillie" class="sister" id="link3">Tillie</a>;
and they lived at the bottom of a well.</p>
<p class="story">...</p>
"""
创建 beautifulsoup 对象

Python

soup = BeautifulSoup(html)
1
soup = BeautifulSoup(html)
另外，我们还可以用本地 HTML 文件来创建对象，例如

Python

soup = BeautifulSoup(open('index.html'))
1
soup = BeautifulSoup(open('index.html'))
上面这句代码便是将本地 index.html 文件打开，用它来创建 soup 对象

下面我们来打印一下 soup 对象的内容，格式化输出

Python

print soup.prettify()
1
print soup.prettify()
Python

&lt;html&gt;
 &lt;head&gt;
  &lt;title&gt;
   The Dormouse's story
  &lt;/title&gt;
 &lt;/head&gt;
 &lt;body&gt;
  &lt;p class="title" name="dromouse"&gt;
   &lt;b&gt;
    The Dormouse's story
   &lt;/b&gt;
  &lt;/p&gt;
  &lt;p class="story"&gt;
   Once upon a time there were three little sisters; and their names were
   &lt;a class="sister" href="http://example.com/elsie" id="link1"&gt;
    &lt;!-- Elsie --&gt;
   &lt;/a&gt;
   ,
   &lt;a class="sister" href="http://example.com/lacie" id="link2"&gt;
    Lacie
   &lt;/a&gt;
   and
   &lt;a class="sister" href="http://example.com/tillie" id="link3"&gt;
    Tillie
   &lt;/a&gt;
   ;
and they lived at the bottom of a well.
  &lt;/p&gt;
  &lt;p class="story"&gt;
   ...
  &lt;/p&gt;
 &lt;/body&gt;
&lt;/html&gt;
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
&lt;html&gt;
 &lt;head&gt;
  &lt;title&gt;
   The Dormouse's story
  &lt;/title&gt;
 &lt;/head&gt;
 &lt;body&gt;
  &lt;p class="title" name="dromouse"&gt;
   &lt;b&gt;
    The Dormouse's story
   &lt;/b&gt;
  &lt;/p&gt;
  &lt;p class="story"&gt;
   Once upon a time there were three little sisters; and their names were
   &lt;a class="sister" href="http://example.com/elsie" id="link1"&gt;
    &lt;!-- Elsie --&gt;
   &lt;/a&gt;
   ,
   &lt;a class="sister" href="http://example.com/lacie" id="link2"&gt;
    Lacie
   &lt;/a&gt;
   and
   &lt;a class="sister" href="http://example.com/tillie" id="link3"&gt;
    Tillie
   &lt;/a&gt;
   ;
and they lived at the bottom of a well.
  &lt;/p&gt;
  &lt;p class="story"&gt;
   ...
  &lt;/p&gt;
 &lt;/body&gt;
&lt;/html&gt;
以上便是输出结果，格式化打印出了它的内容，这个函数经常用到，小伙伴们要记好咯。

5. 四大对象种类

Beautiful Soup将复杂HTML文档转换成一个复杂的树形结构,每个节点都是Python对象,所有对象可以归纳为4种:

Tag
NavigableString
BeautifulSoup
Comment
下面我们进行一一介绍

（1）Tag

Tag 是什么？通俗点讲就是 HTML 中的一个个标签，例如
<title>The Dormouse's story</title>

&lt;a class="sister" href="http://example.com/elsie" id="link1"&gt;Elsie&lt;/a&gt;
1
&lt;a class="sister" href="http://example.com/elsie" id="link1"&gt;Elsie&lt;/a&gt;
上面的 title a 等等 HTML 标签加上里面包括的内容就是 Tag，下面我们来感受一下怎样用 Beautiful Soup 来方便地获取 Tags

下面每一段代码中注释部分即为运行结果
print soup.title
#<title>The Dormouse's story</title>
print soup.head
#<head><title>The Dormouse's story</title></head>
print soup.a
#<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>
print soup.p #同soup.body.p
#<p class="title" name="dromouse"><b>The Dormouse's story</b></p>

我们可以利用 soup加标签名轻松地获取这些标签的内容，是不是感觉比正则表达式方便多了？不过有一点是，它查找的是在所有内容中的第一个符合要求的标签，如果要查询所有的标签，我们在后面进行介绍。

我们可以验证一下这些对象的类型
print type(soup.a)
#<class 'bs4.element.Tag'>

对于 Tag，它有两个重要的属性，是 name 和 attrs，下面我们分别来感受一下
name
print soup.name
print soup.head.name
#[document]
#head
soup 对象本身比较特殊，它的 name 即为 [document]，对于其他内部标签，输出的值便为标签本身的名称。

attrs
print soup.p.attrs
#{'class': ['title'], 'name': 'dromouse'}
在这里，我们把 p 标签的所有属性打印输出了出来，得到的类型是一个字典。

如果我们想要单独获取某个属性，可以这样，例如我们获取它的 class 叫什么
print soup.p['class']
#['title']

还可以这样，利用get方法，传入属性的名称，二者是等价的
print soup.p.get('class')
#['title']

我们可以对这些属性和内容等等进行修改，例如
soup.p['class']="newClass"
print soup.p

#<p class="newClass" name="dromouse"><b>The Dormouse's story</b></p>
还可以对这个属性进行删除，例如

del soup.p['class']
print soup.p
#<p name="dromouse"><b>The Dormouse's story</b></p>

不过，对于修改删除的操作，不是我们的主要用途，在此不做详细介绍了，如果有需要，请查看前面提供的官方文档

（2）NavigableString

既然我们已经得到了标签的内容，那么问题来了，我们要想获取标签内部的文字怎么办呢？很简单，用 .string 即可，例如
print soup.p.string
#The Dormouse's story
这样我们就轻松获取到了标签里面的内容，想想如果用正则表达式要多麻烦。它的类型是一个 NavigableString，翻译过来叫 可以遍历的字符串，不过我们最好还是称它英文名字吧。
print type(soup.p.string)
#<class 'bs4.element.NavigableString'>
来检查一下它的类型
print type(soup.p.string)
#<class 'bs4.element.NavigableString'>

（3）BeautifulSoup

BeautifulSoup 对象表示的是一个文档的全部内容.大部分时候,可以把它当作 Tag 对象，是一个特殊的 Tag，我们可以分别获取它的类型，名称，以及属性来感受一下
print type(soup.name)
#<type 'unicode'> #本地运行结果是<type 'str'>
print soup.name 
# [document]
print soup.attrs 
#{} 空字典

（4）Comment
Comment 对象是一个特殊类型的 NavigableString 对象，其实输出的内容仍然不包括注释符号，但是如果不好好处理它，可能会对我们的文本处理造成意想不到的麻烦。
我们找一个带注释的标签
print soup.a
print soup.a.string
print type(soup.a.string)

运行结果如下
<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>
 Elsie 
<class 'bs4.element.Comment'>

a 标签里的内容实际上是注释，但是如果我们利用 .string 来输出它的内容，我们发现它已经把注释符号去掉了，所以这可能会给我们带来不必要的麻烦。
另外我们打印输出下它的类型，发现它是一个 Comment 类型，所以，我们在使用前最好做一下判断，判断代码如下
if type(soup.a.string) == bs4.element.Comment:
    print soup.a.string
上面的代码中，我们首先判断了它的类型，是否为 Comment 类型，然后再进行其他操作，如打印输出。

6. 遍历文档树
（1）直接子节点
要点：.contents .children 属性
.contents
tag 的 .content 属性可以将tag的子节点以列表的方式输出
print soup.head.contents 
#[<title>The Dormouse's story</title>]
输出方式为列表，我们可以用列表索引来获取它的某一个元素
print soup.head.contents[0]
#<title>The Dormouse's story</title>

.children
它返回的不是一个 list，不过我们可以通过遍历获取所有子节点。
我们打印输出 .children 看一下，可以发现它是一个 list 生成器对象
print soup.head.children
# <listiterator object at 0x0000000005237E48>
for child in soup.head.children:
    print child
# <title>The Dormouse's story</title>

for child in soup.body.children:
    print child
    print "type: ", type(child) #打出了很多空行，认为是NavigableString
输出：

type:  <class 'bs4.element.NavigableString'>
<p class="title" name="dromouse"><b>The Dormouse's story</b></p>
type:  <class 'bs4.element.Tag'>


type:  <class 'bs4.element.NavigableString'>
<p class="story">Once upon a time there were three little sisters; and their names were
<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>,
<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a> and
<a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>;
and they lived at the bottom of a well.</p>
type:  <class 'bs4.element.Tag'>


type:  <class 'bs4.element.NavigableString'>
<p class="story">...</p>
type:  <class 'bs4.element.Tag'>


type:  <class 'bs4.element.NavigableString'>

如果把所有的换行符去掉，则打印出：
<p class="title" name="dromouse"><b>The Dormouse's story</b></p>
type:  <class 'bs4.element.Tag'>
<p class="story">Once upon a time there were three little sisters; and their names were
<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>,
<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a> and
<a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>;
and they lived at the bottom of a well.</p>
type:  <class 'bs4.element.Tag'>
<p class="story">...</p>
type:  <class 'bs4.element.Tag'>

（2）所有子孙节点
知识点：.descendants 属性
.descendants
.contents 和 .children 属性仅包含tag的直接子节点，.descendants 属性可以对所有tag的子孙节点进行递归循环，和 children类似，我们也需要遍历获取其中的内容。
for child in soup.descendants:
    print child
运行结果如下，可以发现，所有的节点都被打印出来了，先生最外层的 HTML标签，其次从 head 标签一个个剥离，以此类推。

3）节点内容
知识点：.string 属性
如果tag只有一个 NavigableString 类型子节点,那么这个tag可以使用 .string 得到子节点。如果一个tag仅有一个子节点,那么这个tag也可以使用 .string 方法,输出结果与当前唯一子节点的 .string 结果相同。
通俗点说就是：如果一个标签里面没有标签了，那么 .string 就会返回标签里面的内容。如果标签里面只有唯一的一个标签了，那么 .string 也会返回最里面的内容。例如
print soup.head.string
#The Dormouse's story
print soup.title.string #同soup.title.get_text()
#The Dormouse's story

如果tag包含了多个子节点,tag就无法确定，string 方法应该调用哪个子节点的内容, .string 的输出结果是 None
print soup.html.string
# None

（4）多个内容
知识点： .strings .stripped_strings 属性
.strings
获取多个内容，不过需要遍历获取，比如下面的例子
for string in soup.html.strings:
    print (repr(string))
输出：
u"The Dormouse's story"
u'\n'
u'\n'
u"The Dormouse's story"
u'\n'
u'Once upon a time there were three little sisters; and their names were\n'
u',\n'
u'Lacie'
u' and\n'
u'Tillie'
u';\nand they lived at the bottom of a well.'
u'\n'
u'...'

.stripped_strings
输出的字符串中可能包含了很多空格或空行,使用 .stripped_strings 可以去除多余空白内容
for string in soup.stripped_strings:
    print(repr(string))
    # u"The Dormouse's story"
    # u"The Dormouse's story"
    # u'Once upon a time there were three little sisters; and their names were'
    # u'Elsie'
    # u','
    # u'Lacie'
    # u'and'
    # u'Tillie'
    # u';\nand they lived at the bottom of a well.'
    # u'...'

（5）父节点
知识点： .parent 属性
p = soup.p
print p.parent.name
#body

content = soup.head.title.string
print content.parent.name
#title

（6）全部父节点
知识点：.parents 属性
通过元素的 .parents 属性可以递归得到元素的所有父辈节点，例如
content = soup.head.title.string
for parent in  content.parents:
    print parent.name
title
head
html
[document]

（7）兄弟节点
知识点：.next_sibling .previous_sibling 属性
兄弟节点可以理解为和本节点处在统一级的节点，.next_sibling 属性获取了该节点的下一个兄弟节点，.previous_sibling 则与之相反，如果节点不存在，则返回 None
注意：实际文档中的tag的 .next_sibling 和 .previous_sibling 属性通常是字符串或空白，因为空白或者换行也可以被视作一个节点，所以得到的结果可能是空白或者换行
print repr(soup.p.next_sibling)
#       实际该处为空白 u'\n'
print soup.p.prev_sibling #原文这里拼错prev_sibling，是个没有的属性，所以打出了None
# None   没有前一个兄弟节点，返回 None
print repr(soup.p.previous_sibling)
# u'\n'
print soup.p.previous_sibling.previous_sibling
# None
print soup.p.next_sibling.next_sibling
# <p class="story">Once upon a time there were three little sisters; and their names were
# <a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>,
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a> and
# <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>;
# and they lived at the bottom of a well.</p>
# 下一个节点的下一个兄弟节点是我们可以看到的节点

（8）全部兄弟节点
知识点：.next_siblings .previous_siblings 属性
通过 .next_siblings 和 .previous_siblings 属性可以对当前节点的兄弟节点迭代输出
for sibling in soup.a.next_siblings:
    print repr(sibling)
# u',\n'
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>
# u' and\n'
# <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>
# u';\nand they lived at the bottom of a well.'

（9）前后节点
知识点：.next_element .previous_element 属性
与 .next_sibling .previous_sibling 不同，它并不是针对于兄弟节点，而是在所有节点，不分层次
比如 head 节点为
<head><title>The Dormouse's story</title></head>
那么它的下一个节点便是 title，它是不分层次关系的
print soup.head.next_element
#<title>The Dormouse's story</title>

比较如下：
print soup.p.previous_sibling.previous_sibling
# None
print soup.p.previous_element.previous_element
# <body>
# <p class="title" name="dromouse"><b>The Dormouse's story</b></p>
# <p class="story">Once upon a time there were three little sisters; and their names were
# <a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>,
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a> and
# <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>;
# and they lived at the bottom of a well.</p>
# <p class="story">...</p></body>

（10）所有前后节点
知识点：.next_elements .previous_elements 属性
通过 .next_elements 和 .previous_elements 的迭代器就可以向前或向后访问文档的解析内容,就好像文档正在被解析一样
aTags = soup.find_all("a")
print aTags
last_a_tag = aTags[len(aTags) - 1]
for element in last_a_tag.next_elements:
    print(repr(element))
输出：
[<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>, <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>, <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]
u'Tillie'
u';\nand they lived at the bottom of a well.'
u'\n'
<p class="story">...</p>
u'...'
u'\n'

7.搜索文档树
（1）find_all( name , attrs , recursive , text , **kwargs )
find_all() 方法搜索当前tag的所有tag子节点,并判断是否符合过滤器的条件
1）name 参数
name 参数可以查找所有名字为 name 的tag,字符串对象会被自动忽略掉
A.传字符串
最简单的过滤器是字符串.在搜索方法中传入一个字符串参数,Beautiful Soup会查找与字符串完整匹配的内容,下面的例子用于查找文档中所有的<b>标签
print soup.find_all('b')
# [<b>The Dormouse's story</b>]
print soup.find_all('a')
#[<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>, <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>, <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]

B.传正则表达式
如果传入正则表达式作为参数,Beautiful Soup会通过正则表达式的 match() 来匹配内容.下面例子中找出所有以b开头的标签,这表示<body>和<b>标签都应该被找到
import re
for tag in soup.find_all(re.compile("^b")):
    print(tag.name)
# body
# b

C.传列表
如果传入列表参数,Beautiful Soup会将与列表中任一元素匹配的内容返回.下面代码找到文档中所有<a>标签和<b>标签
for tag in soup.find_all(["a", "b"]):
    print tag
# [<b>The Dormouse's story</b>,
#  <a class="sister" href="http://example.com/elsie" id="link1">Elsie</a>,
#  <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>,
#  <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]

D.传 True
True 可以匹配任何值,下面代码查找到所有的tag,但是不会返回字符串节点
for tag in soup.find_all(True):
    print tag.name
# html
# head
# title
# body
# p
# b
# p
# a
# a
# a
# p

E.传方法
如果没有合适过滤器,那么还可以定义一个方法,方法只接受一个元素参数 [4] ,如果这个方法返回 True 表示当前元素匹配并且被找到,如果不是则反回 False
下面方法校验了当前元素,如果包含 class 属性却不包含 id 属性,那么将返回 True:
def has_class_but_no_id(tag):
    return tag.has_attr('class') and not tag.has_attr('id')

for tag in soup.find_all(has_class_but_no_id):
    print tag.name
# p
# p
# p

2）keyword 参数
注意：如果一个指定名字的参数不是搜索内置的参数名,搜索时会把该参数当作指定名字tag的属性来搜索,如果包含一个名字为 id 的参数,Beautiful Soup会搜索每个tag的”id”属性
for tag in soup.find_all(id='link2'):
    print tag
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>

如果传入 href 参数,Beautiful Soup会搜索每个tag的”href”属性
for tag in soup.find_all(href=re.compile('elsie')):
    print tag
# <a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>

使用多个指定名字的参数可以同时过滤tag的多个属性
for tag in soup.find_all(href=re.compile('elsie'), id='link1'):
    print tag
# <a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>

在这里我们想用 class 过滤，不过 class 是 python 的关键词，这怎么办？加个下划线就可以
for tag in soup.find_all("a", class_="sister"): #同soup.find_all("a", attrs={"class": "sister"})
    print tag
# <a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>
# <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>

有些tag属性在搜索不能使用,比如HTML5中的 data-* 属性
但是可以通过 find_all() 方法的 attrs 参数定义一个字典参数来搜索包含特殊属性的tag
for tag in soup.find_all("a", attrs={"class": "sister"}):
    print tag
# <a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>
# <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>

3）text 参数
通过 text 参数可以搜搜文档中的字符串内容.与 name 参数的可选值一样, text 参数接受 字符串 , 正则表达式 , 列表, True
print soup.find_all(text="Elsie") #在本地，没有搜索到，因为Elsie在注释字符串
# [u'Elsie']
print soup.find_all(text=["Tillie", "Elsie", "Lacie"])
# [u'Elsie', u'Lacie', u'Tillie']
print soup.find_all(text=re.compile("Dormouse"))
# [u"The Dormouse's story", u"The Dormouse's story"]

4）limit 参数
find_all() 方法返回全部的搜索结构,如果文档树很大那么搜索会很慢.如果我们不需要全部结果,可以使用 limit 参数限制返回结果的数量.效果与SQL中的limit关键字类似,当搜索到的结果数量达到 limit 的限制时,就停止搜索返回结果.
文档树中有3个tag符合搜索条件,但结果只返回了2个,因为我们限制了返回数量
soup.find_all("a", limit=2)
[<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>, <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>]

5）recursive 参数
调用tag的 find_all() 方法时,Beautiful Soup会检索当前tag的所有子孙节点,如果只想搜索tag的直接子节点,可以使用参数 recursive=False .
soup.html.find_all("title")
# [<title>The Dormouse's story</title>]
 
soup.html.find_all("title", recursive=False)
# []

（2）find( name , attrs , recursive , text , **kwargs )
它与 find_all() 方法唯一的区别是 find_all() 方法的返回结果是值包含一个元素的列表,而 find() 方法直接返回结果
print soup.html.find("a", recursive=True)
# <a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>
print soup.html.find("a", recursive=False)
# None

（3）find_parents() find_parent()
find_all() 和 find() 只搜索当前节点的所有子节点,孙子节点等. find_parents() 和 find_parent() 用来搜索当前节点的父辈节点,搜索方法与普通tag的搜索方法相同,搜索文档搜索文档包含的内容
# coding=utf-8
from bs4 import BeautifulSoup

html = """
<html><head><title>The Dormouse's story</title></head>
<body class="body">
<p class="title" name="dromouse"><b>The Dormouse's story</b></p>
<p class="story">Once upon a time there were three little sisters; and their names were
<a href="http://example.com/elsie" class="sister" id="link1"><!-- Elsie --></a>,
<a href="http://example.com/lacie" class="sister" id="link2">Lacie</a> and
<a href="http://example.com/tillie" class="sister" id="link3">Tillie</a>;
and they lived at the bottom of a well.</p>
<p class="story">...</p>
"""


def has_class(tag):
    return tag.has_attr('class')


if __name__ == "__main__":
    soup = BeautifulSoup(html, "lxml")
    print soup.a.find_parents(has_class)
    print soup.a.find_parent(has_class)
输出：
[<p class="story">Once upon a time there were three little sisters; and their names were\n<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>,\n<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a> and\n<a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>;\nand they lived at the bottom of a well.</p>, <body class="body">\n<p class="title" name="dromouse"><b>The Dormouse's story</b></p>\n<p class="story">Once upon a time there were three little sisters; and their names were\n<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>,\n<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a> and\n<a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>;\nand they lived at the bottom of a well.</p>\n<p class="story">...</p>\n</body>]
<p class="story">Once upon a time there were three little sisters; and their names were
<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>,
<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a> and
<a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>;
and they lived at the bottom of a well.</p>

4）find_next_siblings() find_next_sibling()
这2个方法通过 .next_siblings 属性对当 tag 的所有后面解析的兄弟 tag 节点进行迭代, find_next_siblings() 方法返回所有符合条件的后面的兄弟节点,find_next_sibling() 只返回符合条件的后面的第一个tag节点
print soup.a.find_next_siblings(has_class)
# [<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>, <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]
print soup.a.find_next_sibling(has_class)
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>

（5）find_previous_siblings() find_previous_sibling()
这2个方法通过 .previous_siblings 属性对当前 tag 的前面解析的兄弟 tag 节点进行迭代, find_previous_siblings() 方法返回所有符合条件的前面的兄弟节点, find_previous_sibling() 方法返回第一个符合条件的前面的兄弟节点

（6）find_all_next() find_next()
这2个方法通过 .next_elements 属性对当前 tag 的之后的 tag 和字符串进行迭代, find_all_next() 方法返回所有符合条件的节点, find_next() 方法返回第一个符合条件的节点
print soup.a.find_all_next(has_class)
# [<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>, <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>, <p class="story">...</p>]
print soup.a.find_next(has_class)
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>

（7）find_all_previous() 和 find_previous()
这2个方法通过 .previous_elements 属性对当前节点前面的 tag 和字符串进行迭代, find_all_previous() 方法返回所有符合条件的节点, find_previous()方法返回第一个符合条件的节点

8.CSS选择器
我们在写 CSS 时，标签名不加任何修饰，类名前加点，id名前加 #，在这里我们也可以利用类似的方法来筛选元素，用到的方法是 soup.select()，返回类型是 list
（1）通过标签名查找
print soup.select("title")
# [<title>The Dormouse's story</title>]
print soup.select("a")
# [<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>, <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>, <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]
print soup.select("b")
# [<b>The Dormouse's story</b>]

（2）通过类名查找
print soup.select(".sister")
[<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>, <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>, <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]

3）通过 id 名查找
print soup.select("#link1")
#[<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>]

（4）组合查找
组合查找即和写 class 文件时，标签名与类名、id名进行的组合原理是一样的，例如查找 p 标签中，id 等于 link1的内容，二者需要用空格分开
print soup.select('p #link2')
#[<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>]
直接子标签查找
print soup.select('html > head')
# [<head><title>The Dormouse's story</title></head>]
print soup.select('html > title')
# []
可以查找子标签
print soup.select("html title")
# [<title>The Dormouse's story</title>]

（5）属性查找
查找时还可以加入属性元素，属性需要用中括号括起来，注意属性和标签属于同一节点，所以中间不能加空格，否则会无法匹配到。
print soup.select('a[id="link1"]')
#[<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>]
同样，属性仍然可以与上述查找方式组合，不在同一节点的空格隔开，同一节点的不加空格
print soup.select('p a[id="link2"]') #soup.select('body a[id="link2"]')
#[<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>]




#-----------------------------------------------------------------------------------------
#python str和repr的区别 http://www.cnpythoner.com/post/251.html
尽管str(),repr()和``运算在特性和功能方面都非常相似，事实上repr()和``做的是完全一样的事情，它们返回的是一个对象的“官方”字符串表示，也就是说绝大多数情况下可以通过求值运算（使用内建函数eval()）重新得到该对象。

但str()则有所不同，str()致力于生成一个对象的可读性好的字符串表示，它的返回结果通常无法用于eval()求值，但很适合用于print语句输出。需要再次提醒的是，并不是所有repr()返回的字符串都能够用 eval()内建函数得到原来的对象。 也就是说 repr() 输出对 Python比较友好，而str()的输出对用户比较友好。

虽然如此，很多情况下这三者的输出仍然都是完全一样的。 大家可以看下下面的代码，来进行对比

>>> s = 'Hello, world.'
>>> str(s)
'Hello, world.'
>>> repr(s)
"'Hello, world.'"
>>> str(0.1)
'0.1'
>>> repr(0.1)
'0.10000000000000001'
>>> x = 10 * 3.25
>>> y = 200 * 200
>>> s = 'The value of x is ' + repr(x) + ', and y is ' + repr(y) + '...'
>>> print s
The value of x is 32.5, and y is 40000...
>>> # The repr() of a string adds string quotes and backslashes:
... hello = 'hello, world\n'
>>> hellos = repr(hello)
>>> print hellos
'hello, world\n'
>>> # The argument to repr() may be any Python object:
... repr((x, y, ('spam', 'eggs')))
"(32.5, 40000, ('spam', 'eggs'))"


#--------------------BeautifulSoup 提取某个tag标签里面的内容
# 参考http://blog.csdn.net/willib/article/details/52246086
# 提取代理网站的IP和端口
# coding=utf-8
import re
import urllib2

from bs4 import BeautifulSoup

if __name__ == "__main__":
    user_agent = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5)'
    headers = {'User-Agent': user_agent}
    request = urllib2.Request("http://www.xicidaili.com/nn/1", headers=headers)
    response = urllib2.urlopen(request)
    page = response.read()

    soup = BeautifulSoup(page, "lxml")
    trTags = soup.find_all("tr", attrs={"class": re.compile('odd|')}) #soup.find_all("tr", class_=re.compile('odd|'))
    for tr in trTags:
        tdTags = tr.find_all("td")
        print tdTags[1].string
        print tdTags[2].string
输出：
222.136.171.248
8118
171.211.210.126
808
223.199.171.39
8118
39.86.42.244
8118
202.120.29.28
...

#-----------------------------------------------------------------------------------------
# With 语句，自动释放资源
#参考https://www.ibm.com/developerworks/cn/opensource/os-cn-pythonwith/
#-----------------------------------------------------------------------------------------
#类型检查
def my_abs(x):
    if not isinstance(x, (int, float)):
        raise TypeError('bad operand type')
    if x >= 0:
        return x
    else:
        return -x
添加了参数检查后，如果传入错误的参数类型，函数就可以抛出一个错误：

>>> my_abs('A')
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
  File "<stdin>", line 3, in my_abs
TypeError: bad operand type
#-----------------------------------------------------------------------------------------
#默认参数要指向不变对象 https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431752945034eb82ac80a3e64b9bb4929b16eeed1eb9000
先定义一个函数，传入一个list，添加一个END再返回：
def add_end(L=[]):
    L.append('END')
    return L
当你正常调用时，结果似乎不错：

>>> add_end([1, 2, 3])
[1, 2, 3, 'END']
>>> add_end(['x', 'y', 'z'])
['x', 'y', 'z', 'END']
当你使用默认参数调用时，一开始结果也是对的：

>>> add_end()
['END']
但是，再次调用add_end()时，结果就不对了：

>>> add_end()
['END', 'END']
>>> add_end()
['END', 'END', 'END']
很多初学者很疑惑，默认参数是[]，但是函数似乎每次都“记住了”上次添加了'END'后的list。

原因解释如下：

Python函数在定义的时候，默认参数L的值就被计算出来了，即[]，因为默认参数L也是一个变量，它指向对象[]，每次调用该函数，如果改变了L的内容，则下次调用时，默认参数的内容就变了，不再是函数定义时的[]了。

 定义默认参数要牢记一点：默认参数必须指向不变对象！

def add_end(L=None):
    if L is None:
        L = []
    L.append('END')
    return L
现在，无论调用多少次，都不会有问题：

>>> add_end()
['END']
>>> add_end()
['END']

#-----------------------------------------------------------------------------------------
#要求必须传入关键字，使用*
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431752945034eb82ac80a3e64b9bb4929b16eeed1eb9000
def person(name, age, *, city, job):
    print(name, age, city, job)

def person(name, age, *args, city, job): #加了可变参数之后，就不需要一个单独的*分隔了
    print(name, age, args, city, job)
    
if __name__ == "__main__":
    person("dingben", 28, city="sh", job="Engineer")

所以，对于任意函数，都可以通过类似func(*args, **kw)的形式调用它，无论它的参数是如何定义的。
#-----------------------------------------------------------------------------------------
#Slice 
https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431756919644a792ee4ead724ef7afab3f7f771b04f5000
前10个数，每两个取一个：
>>> L[:10:2]
[0, 2, 4, 6, 8]

所有数，每5个取一个：
>>> L[::5]
[0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95]

甚至什么都不写，只写[:]就可以原样复制一个list：
>>> L[:]
[0, 1, 2, 3, ..., 99]

字符串'xxx'也可以看成是一种list，每个元素就是一个字符。因此，字符串也可以用切片操作，只是操作结果仍是字符串：
>>> 'ABCDEFG'[:3]
'ABC'
>>> 'ABCDEFG'[::2]
'ACEG'
#-----------------------------------------------------------------------------------------
#迭代
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014317793224211f408912d9c04f2eac4d2af0d5d3d7b2000
所以，当我们使用for循环时，只要作用于一个可迭代对象，for循环就可以正常运行，而我们不太关心该对象究竟是list还是其他数据类型。

那么，如何判断一个对象是可迭代对象呢？方法是通过collections模块的Iterable类型判断：

>>> from collections import Iterable
>>> isinstance('abc', Iterable) # str是否可迭代
True
>>> isinstance([1,2,3], Iterable) # list是否可迭代
True
>>> isinstance(123, Iterable) # 整数是否可迭代
False

最后一个小问题，如果要对list实现类似Java那样的下标循环怎么办？Python内置的enumerate函数可以把一个list变成索引-元素对，这样就可以在for循环中同时迭代索引和元素本身：
>>> for i, value in enumerate(['A', 'B', 'C']):
...     print(i, value)
...
0 A
1 B
2 C
#-----------------------------------------------------------------------------------------
#列表生成器
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431779637539089fd627094a43a8a7c77e6102e3a811000
但是循环太繁琐，而列表生成式则可以用一行语句代替循环生成上面的list：

>>> [x * x for x in range(1, 11)]
[1, 4, 9, 16, 25, 36, 49, 64, 81, 100]
写列表生成式时，把要生成的元素x * x放到前面，后面跟for循环，就可以把list创建出来，十分有用，多写几次，很快就可以熟悉这种语法。

for循环后面还可以加上if判断，这样我们就可以筛选出仅偶数的平方：

>>> [x * x for x in range(1, 11) if x % 2 == 0]
[4, 16, 36, 64, 100]
还可以使用两层循环，可以生成全排列：

>>> [m + n for m in 'ABC' for n in 'XYZ']
['AX', 'AY', 'AZ', 'BX', 'BY', 'BZ', 'CX', 'CY', 'CZ']
#-----------------------------------------------------------------------------------------
#生成器
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014317799226173f45ce40636141b6abc8424e12b5fb27000
通过列表生成式，我们可以直接创建一个列表。但是，受到内存限制，列表容量肯定是有限的。而且，创建一个包含100万个元素的列表，不仅占用很大的存储空间，如果我们仅仅需要访问前面几个元素，那后面绝大多数元素占用的空间都白白浪费了。

所以，如果列表元素可以按照某种算法推算出来，那我们是否可以在循环的过程中不断推算出后续的元素呢？这样就不必创建完整的list，从而节省大量的空间。在Python中，这种一边循环一边计算的机制，称为生成器：generator。

要创建一个generator，有很多种方法。第一种方法很简单，只要把一个列表生成式的[]改成()，就创建了一个generator：

>>> L = [x * x for x in range(10)]
>>> L
[0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
>>> g = (x * x for x in range(10))
>>> g
<generator object <genexpr> at 0x1022ef630>
创建L和g的区别仅在于最外层的[]和()，L是一个list，而g是一个generator。

我们可以直接打印出list的每一个元素，但我们怎么打印出generator的每一个元素呢？

如果要一个一个打印出来，可以通过next()函数获得generator的下一个返回值：

>>> next(g)
0
>>> next(g)
1
>>> next(g)
4
>>> next(g)
9
>>> next(g)
16
>>> next(g)
25
>>> next(g)
36
>>> next(g)
49
>>> next(g)
64
>>> next(g)
81
>>> next(g)
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
StopIteration
我们讲过，generator保存的是算法，每次调用next(g)，就计算出g的下一个元素的值，直到计算到最后一个元素，没有更多的元素时，抛出StopIteration的错误。

当然，上面这种不断调用next(g)实在是太变态了，正确的方法是使用for循环，因为generator也是可迭代对象：

>>> g = (x * x for x in range(10))
>>> for n in g:
...     print(n)
... 
0
1
4
9
16
25
36
49
64
81


也就是说，上面的函数和generator仅一步之遥。要把fib函数变成generator，只需要把print(b)改为yield b就可以了：

def fib(max):
    n, a, b = 0, 0, 1
    while n < max:
        yield b
        a, b = b, a + b
        n = n + 1
    return 'done'
    
同样的，把函数改成generator后，我们基本上从来不会用next()来获取下一个返回值，而是直接使用for循环来迭代：

>>> for n in fib(6):
...     print(n)
...
1
1
2
3
5
8
但是用for循环调用generator时，发现拿不到generator的return语句的返回值。如果想要拿到返回值，必须捕获StopIteration错误，返回值包含在StopIteration的value中：

>>> g = fib(6)
>>> while True:
...     try:
...         x = next(g)
...         print('g:', x)
...     except StopIteration as e:
...         print('Generator return value:', e.value)
...         break
...
g: 1
g: 1
g: 2
g: 3
g: 5
g: 8
Generator return value: done


杨辉三角定义如下：

          1
         / \
        1   1
       / \ / \
      1   2   1
     / \ / \ / \
    1   3   3   1
   / \ / \ / \ / \
  1   4   6   4   1
 / \ / \ / \ / \ / \
1   5   10  10  5   1
把每一行看做一个list，试写一个generator，不断输出下一行的list：
# -*- coding: utf-8 -*-
def triangles():
    l = [1]
    yield l
    for rowIndex in range(1, 10):
        l = [l[i] + l[i + 1] for i in range(len(l) - 1)]
        l.insert(0, 1)
        l.append(1)
        yield l

if __name__ == "__main__":
    for t in triangles():
        print(t)
输出：
[1],
[1, 1],
[1, 2, 1],
[1, 3, 3, 1],
[1, 4, 6, 4, 1],
[1, 5, 10, 10, 5, 1],
[1, 6, 15, 20, 15, 6, 1],
[1, 7, 21, 35, 35, 21, 7, 1],
[1, 8, 28, 56, 70, 56, 28, 8, 1],
[1, 9, 36, 84, 126, 126, 84, 36, 9, 1]
#-----------------------------------------------------------------------------------------
#迭代器 https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143178254193589df9c612d2449618ea460e7a672a366000
if __name__ == "__main__":
    print(isinstance([], Iterable))
    print(isinstance({}, Iterable))
    print(isinstance("abc", Iterable))
    print(isinstance((x for x in range(10)), Iterable))
    print(isinstance(100, Iterable))
输出：
True
True
True
True
False

而生成器不但可以作用于for循环，还可以被next()函数不断调用并返回下一个值，直到最后抛出StopIteration错误表示无法继续返回下一个值了。
可以被next()函数调用并不断返回下一个值的对象称为迭代器：Iterator。
可以使用isinstance()判断一个对象是否是Iterator对象：
if __name__ == "__main__":
    print(isinstance([], Iterator))
    print(isinstance({}, Iterator))
    print(isinstance("abc", Iterator))
    print(isinstance((x for x in range(10)), Iterator))
    print(isinstance(100, Iterator))
输出：
False
False
False
True
False

生成器都是Iterator对象，但list、dict、str虽然是Iterable，却不是Iterator。
把list、dict、str等Iterable变成Iterator可以使用iter()函数：
if __name__ == "__main__":
    print(isinstance(iter([]), Iterator))
    print(isinstance(iter({}), Iterator))
    print(isinstance(iter("abc"), Iterator))
    print(isinstance((x for x in range(10)), Iterator))
输出：
True
True
True
True

这是因为Python的Iterator对象表示的是一个数据流，Iterator对象可以被next()函数调用并不断返回下一个数据，直到没有数据时抛出StopIteration错误。可以把这个数据流看做是一个有序序列，但我们却不能提前知道序列的长度，只能不断通过next()函数实现按需计算下一个数据，所以Iterator的计算是惰性的，只有在需要返回下一个数据时它才会计算。

Iterator甚至可以表示一个无限大的数据流，例如全体自然数。而使用list是永远不可能存储全体自然数的。

if __name__ == "__main__":
    l = list(range(10))
    iterL = iter(l)
    try:
        while True:
            print(next(iterL))
    except StopIteration as e:
        print(e.value)
输出：
0
1
2
3
4
5
6
7
8
9
None

小结

凡是可作用于for循环的对象都是Iterable类型；

凡是可作用于next()函数的对象都是Iterator类型，它们表示一个惰性计算的序列；

集合数据类型如list、dict、str等是Iterable但不是Iterator，不过可以通过iter()函数获得一个Iterator对象。

Python的for循环本质上就是通过不断调用next()函数实现的，例如：

for x in [1, 2, 3, 4, 5]:
    pass
实际上完全等价于：

# 首先获得Iterator对象:
it = iter([1, 2, 3, 4, 5])
# 循环:
while True:
    try:
        # 获得下一个值:
        x = next(it)
    except StopIteration:
        # 遇到StopIteration就退出循环
        break
        
#range可迭代，但是不是迭代器，用list可变为list变量
print(isinstance(range(10), Iterable)) #True
print(isinstance(range(10), Iterator)) #False
t = iter(range(10))
print(isinstance(t, Iterable)) #True
print(isinstance(t, Iterator)) #True

#惰性序列可以用list生成list
g = (x * x for x in range(10))
print(g, list(g))
<generator object <genexpr> at 0x000002657F96E360> [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
#-----------------------------------------------------------------------------------------
#函数式编程 https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014317848428125ae6aa24068b4c50a7e71501ab275d52000
函数式编程就是一种抽象程度很高的编程范式，纯粹的函数式编程语言编写的函数没有变量，因此，任意一个函数，只要输入是确定的，输出就是确定的，这种纯函数我们称之为没有副作用。而允许使用变量的程序设计语言，由于函数内部的变量状态不确定，同样的输入，可能得到不同的输出，因此，这种函数是有副作用的。

函数式编程的一个特点就是，允许把函数本身作为参数传入另一个函数，还允许返回一个函数！

Python对函数式编程提供部分支持。由于Python允许使用变量，因此，Python不是纯函数式编程语言。
#-----------------------------------------------------------------------------------------
# map/reduce
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014317852443934a86aa5bb5ea47fbbd5f35282b331335000
# -*- coding: utf-8 -*-
def f(x):
    return x * x

if __name__ == "__main__":
    r = map(f, list(range(10)))
    print(list(r))
    print(r)
    print(isinstance(r, Iterator))
    print(isinstance(r, Iterable))
输出：
[0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
<map object at 0x000002BB2F31A7F0>
True
True

map()传入的第一个参数是f，即函数对象本身。由于结果r是一个Iterator，Iterator是惰性序列，因此通过list()函数让它把整个序列都计算出来并返回一个list。
#！！！这里python3和python2不一样，python3 map拿到的是一个map对象，python2获取到的是一个list对象


再看reduce的用法。reduce把一个函数作用在一个序列[x1, x2, x3, ...]上，这个函数必须接收两个参数，reduce把结果继续和序列的下一个元素做累积计算，其效果就是：
reduce(f, [x1, x2, x3, x4]) = f(f(f(x1, x2), x3), x4)

# -*- coding: utf-8 -*-
from functools import reduce

if __name__ == "__main__":
    r = reduce(lambda x, y : x + y, range(101))
    print(r) #5050 = sum(range(101))

如果要把序列[1, 3, 5, 7, 9]变换成整数13579，reduce就可以派上用场：
r = reduce(lambda x, y: x * 10 + y, range(1, 10, 2)) #13579

#同下
def fn(x, y):
    return x * 10 + y
if __name__ == "__main__":
    r = reduce(fn, [1, 3, 5, 7, 9])
    print(r)
    
这个例子本身没多大用处，但是，如果考虑到字符串str也是一个序列，对上面的例子稍加改动，配合map()，我们就可以写出把str转换为int的函数：
from functools import reduce

def fn(x, y):
    return x * 10 + y

def char2num(s):
    digits = {'0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9}
    return digits[s]

if __name__ == "__main__":
    r = reduce(fn, map(char2num, '13579'))
    print(r) #13579
    
    
整理成一个str2int的函数就是：
from functools import reduce

DIGITS = {'0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9}

def str2int(s):
    def fn(x, y):
        return x * 10 + y
    def char2num(s):
        return DIGITS[s]
    return reduce(fn, map(char2num, s))

#用lambda表达式简化
# -*- coding: utf-8 -*-
from functools import reduce

DIGITS = {'0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9}


def str2int(s):
    def char2num(s):
        return DIGITS[s]

    return reduce(lambda x, y: x * 10 + y, map(char2num, s))


if __name__ == "__main__":
    print(str2int('1357913579135791357913579135791357913579135791357913579135791357913579135791357913579'))
输出：
1357913579135791357913579135791357913579135791357913579135791357913579135791357913579
#-----------------------------------------------------------------------------------------
#filter()
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431821084171d2e0f22e7cc24305ae03aa0214d0ef29000
可见用filter()这个高阶函数，关键在于正确实现一个“筛选”函数。

注意到filter()函数返回的是一个Iterator，也就是一个惰性序列，所以要强迫filter()完成计算结果，需要用list()函数获得所有结果并返回list。

#测试
# -*- coding: utf-8 -*-
def filterEven(number):
    return number % 2 == 0


if __name__ == "__main__":
    l = (x for x in range(10))
    oddList = filter(filterEven, l)#oddList是一个惰性序列，此处并没有调用filterEven计算oddList值，而是在for循环中再调用filterEven函数
    for x in oddList:
        print(x)


用filter求素数

计算素数的一个方法是埃氏筛法，它的算法理解起来非常简单：

首先，列出从2开始的所有自然数，构造一个序列：

2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...

取序列的第一个数2，它一定是素数，然后用2把序列的2的倍数筛掉：

3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...

取新序列的第一个数3，它一定是素数，然后用3把序列的3的倍数筛掉：

5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...

取新序列的第一个数5，然后用5把序列的5的倍数筛掉：

7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...

不断筛下去，就可以得到所有的素数。

用Python来实现这个算法，可以先构造一个从3开始的奇数序列：

def _odd_iter():
    n = 1
    while True:
        n = n + 2
        yield n
注意这是一个生成器，并且是一个无限序列。

然后定义一个筛选函数：

def _not_divisible(n):
    return lambda x: x % n > 0
最后，定义一个生成器，不断返回下一个素数：

def primes():
    yield 2
    it = _odd_iter() # 初始序列
    while True:
        n = next(it) # 返回序列的第一个数
        yield n
        it = filter(_not_divisible(n), it) # 构造新序列
这个生成器先返回第一个素数2，然后，利用filter()不断产生筛选后的新的序列。

由于primes()也是一个无限序列，所以调用时需要设置一个退出循环的条件：

# 打印1000以内的素数:
for n in primes():
    if n < 1000:
        print(n)
    else:
        break
注意到Iterator是惰性计算的序列，所以我们可以用Python表示“全体自然数”，“全体素数”这样的序列，而代码非常简洁。

#全部代码
# -*- coding: utf-8 -*-


def oddIter():
    n = 1
    while True:
        n += 2
        yield n


def notDivisible(n):
    return lambda x: x % n != 0


def primes():
    yield 2
    it = oddIter()
    while True:
        n = next(it)
        yield n
        it = filter(notDivisible(n), it)#代码运行到这里时并没有计算每个数字是否可以被整除，而是真正获取next的时候才进行计算，举例：当n为5时，会计算是否5 % 3 != 0；n为7时，会计算 7 % 3 != 0 && 7 % 5 != 0；当n为9时，不满足9 % 3 != 0，则n会继续迭代下一个值11，则需要满足11 % 3 != 0 && 11 % 5 != 0 && 11 % 7 != 0；依次类推...


if __name__ == "__main__":
    for n in primes():
        if n < 1000:
            print(n)
        else:
            break

#判断是否是回文数字：
# -*- coding: utf-8 -*-
from math import floor


def is_palindrome(n):
    s = str(n)
    lenS = len(s)
    for i in range(0, floor(lenS / 2)):
        if s[i] != s[-1 - i]:
            return False
    return True


if __name__ == "__main__":
    output = filter(is_palindrome, range(1, 1000))
    print(list(output))
#-----------------------------------------------------------------------------------------
# sorted https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014318230588782cac105d0d8a40c6b450a232748dc854000
print(sorted(['bob', 'about', 'Zoo', 'Credit'], key=str.lower))
输出：
['about', 'bob', 'Credit', 'Zoo']

要进行反向排序，不必改动key函数，可以传入第三个参数reverse=True：
sorted(['bob', 'about', 'Zoo', 'Credit'], key=str.lower, reverse=True)

# -*- coding: utf-8 -*-

def by_name(t):
    return t[0]


def by_score(t):
    return t[1]


if __name__ == "__main__":
    L = [('Bob', 75), ('Adam', 92), ('Bart', 66), ('Lisa', 88)]
    print(sorted(L, key=by_name))
    print(sorted(L, key=by_score))
输出：
[('Adam', 92), ('Bart', 66), ('Bob', 75), ('Lisa', 88)]
[('Bart', 66), ('Bob', 75), ('Lisa', 88), ('Adam', 92)]
#-----------------------------------------------------------------------------------------
#排序 https://www.cnblogs.com/pengwang57/p/7183468.html
Python list内置sort()方法用来排序，也可以用python内置的全局sorted()方法来对可迭代的序列排序生成新的序列。

1）排序基础

简单的升序排序是非常容易的。只需要调用sorted()方法。它返回一个新的list，新的list的元素基于小于运算符(__lt__)来排序。

>>> sorted([5, 2, 3, 1, 4])
[1, 2, 3, 4, 5]
　　你也可以使用list.sort()方法来排序，此时list本身将被修改。通常此方法不如sorted()方便，但是如果你不需要保留原来的list，此方法将更有效。

>>> a = [5, 2, 3, 1, 4]
>>> a.sort()
>>> a
[1, 2, 3, 4, 5]
　　另一个不同就是list.sort()方法仅被定义在list中，相反地sorted()方法对所有的可迭代序列都有效。

>>> sorted({1: 'D', 2: 'B', 3: 'B', 4: 'E', 5: 'A'})
[1, 2, 3, 4, 5]
　　

2）key参数/函数

从python2.4开始，list.sort()和sorted()函数增加了key参数来指定一个函数，此函数将在每个元素比较前被调用。 例如通过key指定的函数来忽略字符串的大小写：

>>> sorted("This is a test string from Andrew".split(), key=str.lower)
['a', 'Andrew', 'from', 'is', 'string', 'test', 'This']
　　key参数的值为一个函数，此函数只有一个参数且返回一个值用来进行比较。这个技术是快速的因为key指定的函数将准确地对每个元素调用。

 

更广泛的使用情况是用复杂对象的某些值来对复杂对象的序列排序，例如：

>>> student_tuples = [
        ('john', 'A', 15),
        ('jane', 'B', 12),
        ('dave', 'B', 10),
]
>>> sorted(student_tuples, key=lambda student: student[2])   # sort by age
[('dave', 'B', 10), ('jane', 'B', 12), ('john', 'A', 15)]
　　同样的技术对拥有命名属性的复杂对象也适用，例如：

复制代码
>>> class Student:
        def __init__(self, name, grade, age):
                self.name = name
                self.grade = grade
                self.age = age
        def __repr__(self):
                return repr((self.name, self.grade, self.age))
>>> student_objects = [
        Student('john', 'A', 15),
        Student('jane', 'B', 12),
        Student('dave', 'B', 10),
]
>>> sorted(student_objects, key=lambda student: student.age)   # sort by age
[('dave', 'B', 10), ('jane', 'B', 12), ('john', 'A', 15)]
复制代码
3）Operator 模块函数

上面的key参数的使用非常广泛，因此python提供了一些方便的函数来使得访问方法更加容易和快速。operator模块有itemgetter，attrgetter，从2.6开始还增加了methodcaller方法。使用这些方法，上面的操作将变得更加简洁和快速：

>>> from operator import itemgetter, attrgetter
>>> sorted(student_tuples, key=itemgetter(2))
[('dave', 'B', 10), ('jane', 'B', 12), ('john', 'A', 15)]
>>> sorted(student_objects, key=attrgetter('age'))
[('dave', 'B', 10), ('jane', 'B', 12), ('john', 'A', 15)]
　　operator模块还允许多级的排序，例如，先以grade，然后再以age来排序：

>>> sorted(student_tuples, key=itemgetter(1,2))
[('john', 'A', 15), ('dave', 'B', 10), ('jane', 'B', 12)]
>>> sorted(student_objects, key=attrgetter('grade', 'age'))
[('john', 'A', 15), ('dave', 'B', 10), ('jane', 'B', 12)]


#-----------------------------------------------------------------------------------------
#排序 #https://docs.python.org/3/howto/sorting.html
>>> def reverse_numeric(x, y):
...     return y - x
>>> sorted([5, 2, 4, 1, 3], cmp=reverse_numeric) 
[5, 4, 3, 2, 1]

When porting code from Python 2.x to 3.x, the situation can arise when you have the user supplying a comparison function and you need to convert that to a key function. The following wrapper makes that easy to do:

def cmp_to_key(mycmp):
    'Convert a cmp= function into a key= function'
    class K:
        def __init__(self, obj, *args):
            self.obj = obj
        def __lt__(self, other):
            return mycmp(self.obj, other.obj) < 0
        def __gt__(self, other):
            return mycmp(self.obj, other.obj) > 0
        def __eq__(self, other):
            return mycmp(self.obj, other.obj) == 0
        def __le__(self, other):
            return mycmp(self.obj, other.obj) <= 0
        def __ge__(self, other):
            return mycmp(self.obj, other.obj) >= 0
        def __ne__(self, other):
            return mycmp(self.obj, other.obj) != 0
    return K
To convert to a key function, just wrap the old comparison function:

>>>
>>> sorted([5, 2, 4, 1, 3], key=cmp_to_key(reverse_numeric))
[5, 4, 3, 2, 1]

In Python 3.2, the functools.cmp_to_key() function was added to the functools module in the standard library.
#-----------------------------------------------------------------------------------------
#返回函数
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431835236741e42daf5af6514f1a8917b8aaadff31bf000

我们来实现一个可变参数的求和。通常情况下，求和的函数是这样定义的：

def calc_sum(*args):
    ax = 0
    for n in args:
        ax = ax + n
    return ax
但是，如果不需要立刻求和，而是在后面的代码中，根据需要再计算怎么办？可以不返回求和的结果，而是返回求和的函数：

def lazy_sum(*args):
    def sum():
        ax = 0
        for n in args:
            ax = ax + n
        return ax
    return sum
当我们调用lazy_sum()时，返回的并不是求和结果，而是求和函数：

>>> f = lazy_sum(1, 3, 5, 7, 9)
>>> f
<function lazy_sum.<locals>.sum at 0x101c6ed90>
调用函数f时，才真正计算求和的结果：

>>> f()
25

在这个例子中，我们在函数lazy_sum中又定义了函数sum，并且，内部函数sum可以引用外部函数lazy_sum的参数和局部变量，当lazy_sum返回函数sum时，相关参数和变量都保存在返回的函数中，这种称为“闭包（Closure）”的程序结构拥有极大的威力。

请再注意一点，当我们调用lazy_sum()时，每次调用都会返回一个新的函数，即使传入相同的参数：

>>> f1 = lazy_sum(1, 3, 5, 7, 9)
>>> f2 = lazy_sum(1, 3, 5, 7, 9)
>>> f1==f2
False
f1()和f2()的调用结果互不影响。


闭包

注意到返回的函数在其定义内部引用了局部变量args，所以，当一个函数返回了一个函数后，其内部的局部变量还被新函数引用，所以，闭包用起来简单，实现起来可不容易。

另一个需要注意的问题是，返回的函数并没有立刻执行，而是直到调用了f()才执行。我们来看一个例子：

def count():
    fs = []
    for i in range(1, 4):
        def f():
             return i*i
        fs.append(f)
    return fs

f1, f2, f3 = count()
在上面的例子中，每次循环，都创建了一个新的函数，然后，把创建的3个函数都返回了。

你可能认为调用f1()，f2()和f3()结果应该是1，4，9，但实际结果是：

>>> f1()
9
>>> f2()
9
>>> f3()
9
全部都是9！原因就在于返回的函数引用了变量i，但它并非立刻执行。等到3个函数都返回时，它们所引用的变量i已经变成了3，因此最终结果为9。

返回闭包时牢记一点：返回函数不要引用任何循环变量，或者后续会发生变化的变量。
 
 如果一定要引用循环变量怎么办？方法是再创建一个函数，用该函数的参数绑定循环变量当前的值，无论该循环变量后续如何更改，已绑定到函数参数的值不变：

def count():
    def f(j):
        def g():
            return j*j
        return g
    fs = []
    for i in range(1, 4):
        fs.append(f(i)) # f(i)立刻被执行，因此i的当前值被传入f()
    return fs
再看看结果：

>>> f1, f2, f3 = count()
>>> f1()
1
>>> f2()
4
>>> f3()
9

返回闭包时牢记一点：返回函数不要引用任何循环变量，或者后续会发生变化的变量。
如果一定要引用循环变量怎么办？方法是再创建一个函数，用该函数的参数绑定循环变量当前的值，无论该循环变量后续如何更改，已绑定到函数参数的值不变：
def count():
    fs = []
    for i in range(1, 4):
        def g():
            return i * i
        fs.append(g)
    return fs

def count2():
    def f(j):
        def g():
            return j * j
        return g

    fs = []
    for i in range(1, 4):
        fs.append(f(i))
    return fs


if __name__ == "__main__":
    f1, f2, f3 = count()
    print(f1(), f2(), f3())
    f1, f2, f3 = count2()
    print(f1(), f2(), f3())


#利用闭包返回一个计数器函数，每次调用它返回递增整数：
Python 3.x引入了nonlocal关键字，可以用于标识外部作用域的变量。
# -*- coding: utf-8 -*-

def createCounter():
    i = 0
    def counter():
        nonlocal i
        i += 1
        return i
    return counter

if __name__ == "__main__":
    counter = createCounter()
    print(counter(), counter(), counter())
#-----------------------------------------------------------------------------------------
#匿名函数
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431843456408652233b88b424613aa8ec2fe032fd85a000
def build(x, y):
    return lambda: x * x + y * y

if __name__ == "__main__":
    f = build(3, 4)
    print(f())
#-----------------------------------------------------------------------------------------
#装饰器 https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014318435599930270c0381a3b44db991cd6d858064ac0000
由于函数也是一个对象，而且函数对象可以被赋值给变量，所以，通过变量也能调用该函数。

>>> def now():
...     print('2015-3-25')
...
>>> f = now
>>> f()
2015-3-25
函数对象有一个__name__属性，可以拿到函数的名字：

>>> now.__name__
'now'
>>> f.__name__
'now'

现在，假设我们要增强now()函数的功能，比如，在函数调用前后自动打印日志，但又不希望修改now()函数的定义，这种在代码运行期间动态增加功能的方式，称之为“装饰器”（Decorator）。

本质上，decorator就是一个返回函数的高阶函数。所以，我们要定义一个能打印日志的decorator，可以定义如下：

def log(func):
    def wrapper(*args, **kw):
        print('call %s():' % func.__name__)
        return func(*args, **kw)
    return wrapper
观察上面的log，因为它是一个decorator，所以接受一个函数作为参数，并返回一个函数。我们要借助Python的@语法，把decorator置于函数的定义处：

@log
def now():
    print('2015-3-25')
调用now()函数，不仅会运行now()函数本身，还会在运行now()函数前打印一行日志：

>>> now()
call now():
2015-3-25
把@log放到now()函数的定义处，相当于执行了语句：

now = log(now)
由于log()是一个decorator，返回一个函数，所以，原来的now()函数仍然存在，只是现在同名的now变量指向了新的函数，于是调用now()将执行新函数，即在log()函数中返回的wrapper()函数。

wrapper()函数的参数定义是(*args, **kw)，因此，wrapper()函数可以接受任意参数的调用。在wrapper()函数内，首先打印日志，再紧接着调用原始函数。

# -*- coding: utf-8 -*-
def log(func):
    def wrapper(*args, **kw):
        print('call %s():' % func.__name__)
        return func(*args, **kw)

    return wrapper

@log
def now():
    print("abc")


if __name__ == "__main__":
    now()
    print(now.__name__)
输出：
call now():
abc
wrapper

如果decorator本身需要传入参数，那就需要编写一个返回decorator的高阶函数，写出来会更复杂。比如，要自定义log的文本：
def log(text):
    def decorator(func):
        def wrapper(*args, **kw):
            print('%s %s():' % (text, func.__name__))
            return func(*args, **kw)
        return wrapper
    return decorator
这个3层嵌套的decorator用法如下：

@log('execute')
def now():
    print('2015-3-25')
执行结果如下：

>>> now()
execute now():
2015-3-25
和两层嵌套的decorator相比，3层嵌套的效果是这样的：

>>> now = log('execute')(now)
我们来剖析上面的语句，首先执行log('execute')，返回的是decorator函数，再调用返回的函数，参数是now函数，返回值最终是wrapper函数。
以上两种decorator的定义都没有问题，但还差最后一步。因为我们讲了函数也是对象，它有__name__等属性，但你去看经过decorator装饰之后的函数，它们的__name__已经从原来的'now'变成了'wrapper'：
>>> now.__name__
'wrapper'

因为返回的那个wrapper()函数名字就是'wrapper'，所以，需要把原始函数的__name__等属性复制到wrapper()函数中，否则，有些依赖函数签名的代码执行就会出错。
不需要编写wrapper.__name__ = func.__name__这样的代码，Python内置的functools.wraps就是干这个事的，所以，一个完整的decorator的写法如下：

import functools

def log(func):
    @functools.wraps(func)
    def wrapper(*args, **kw):
        print('call %s():' % func.__name__)
        return func(*args, **kw)
    return wrapper
或者针对带参数的decorator：

import functools

def log(text):
    def decorator(func):
        @functools.wraps(func)
        def wrapper(*args, **kw):
            print('%s %s():' % (text, func.__name__))
            return func(*args, **kw)
        return wrapper
    return decorator
import functools是导入functools模块。模块的概念稍候讲解。现在，只需记住在定义wrapper()的前面加上@functools.wraps(func)即可。
#-----------------------------------------------------------------------------------------
#偏函数 https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143184474383175eeea92a8b0439fab7b392a8a32f8fa000
Python的functools模块提供了很多有用的功能，其中一个就是偏函数（Partial function）。要注意，这里的偏函数和数学意义上的偏函数不一样。

在介绍函数参数的时候，我们讲到，通过设定参数的默认值，可以降低函数调用的难度。而偏函数也可以做到这一点。举例如下：

int()函数可以把字符串转换为整数，当仅传入字符串时，int()函数默认按十进制转换：

>>> int('12345')
12345
但int()函数还提供额外的base参数，默认值为10。如果传入base参数，就可以做N进制的转换：

>>> int('12345', base=8)
5349
>>> int('12345', 16)
74565
假设要转换大量的二进制字符串，每次都传入int(x, base=2)非常麻烦，于是，我们想到，可以定义一个int2()的函数，默认把base=2传进去：

def int2(x, base=2):
    return int(x, base)
这样，我们转换二进制就非常方便了：

>>> int2('1000000')
64
>>> int2('1010101')
85
functools.partial就是帮助我们创建一个偏函数的，不需要我们自己定义int2()，可以直接使用下面的代码创建一个新的函数int2：

>>> import functools
>>> int2 = functools.partial(int, base=2)
>>> int2('1000000')
64
>>> int2('1010101')
85
所以，简单总结functools.partial的作用就是，把一个函数的某些参数给固定住（也就是设置默认值），返回一个新的函数，调用这个新函数会更简单。

注意到上面的新的int2函数，仅仅是把base参数重新设定默认值为2，但也可以在函数调用时传入其他值：

>>> int2('1000000', base=10)
1000000
最后，创建偏函数时，实际上可以接收函数对象、*args和**kw这3个参数，当传入：

int2 = functools.partial(int, base=2)
实际上固定了int()函数的关键字参数base，也就是：

int2('10010')
相当于：

kw = { 'base': 2 }
int('10010', **kw)
当传入：

max2 = functools.partial(max, 10)
实际上会把10作为*args的一部分自动加到左边，也就是：

max2(5, 6, 7)
相当于：

args = (10, 5, 6, 7)
max(*args)
结果为10。

小结

当函数的参数个数太多，需要简化时，使用functools.partial可以创建一个新的函数，这个新函数可以固定住原函数的部分参数，从而在调用时更简单。


# -*- coding: utf-8 -*-
import functools


def test(a=3, b=4):
    return a + b


if __name__ == "__main__":
    print(test())
    new_test = functools.partial(test, a=7, b=8)
    print(new_test(a=10, b=11))
    print(new_test(10, 11))#相当于new_test(10, 11, a=7, b=8)
#-----------------------------------------------------------------------------------------
#使用模块 https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431845183474e20ee7e7828b47f7b7607f2dc1e90dbb000
作用域

在一个模块中，我们可能会定义很多函数和变量，但有的函数和变量我们希望给别人使用，有的函数和变量我们希望仅仅在模块内部使用。在Python中，是通过_前缀来实现的。

正常的函数和变量名是公开的（public），可以被直接引用，比如：abc，x123，PI等；

类似__xxx__这样的变量是特殊变量，可以被直接引用，但是有特殊用途，比如上面的__author__，__name__就是特殊变量，hello模块定义的文档注释也可以用特殊变量__doc__访问，我们自己的变量一般不要用这种变量名；

类似_xxx和__xxx这样的函数或变量就是非公开的（private），不应该被直接引用，比如_abc，__abc等；

之所以我们说，private函数和变量“不应该”被直接引用，而不是“不能”被直接引用，是因为Python并没有一种方法可以完全限制访问private函数或变量，但是，从编程习惯上不应该引用private函数或变量。

private函数或变量不应该被别人引用，那它们有什么用呢？请看例子：

def _private_1(name):
    return 'Hello, %s' % name

def _private_2(name):
    return 'Hi, %s' % name

def greeting(name):
    if len(name) > 3:
        return _private_1(name)
    else:
        return _private_2(name)
我们在模块里公开greeting()函数，而把内部逻辑用private函数隐藏起来了，这样，调用greeting()函数不用关心内部的private函数细节，这也是一种非常有用的代码封装和抽象的方法，即：

外部不需要引用的函数全部定义成private，只有外部需要引用的函数才定义为public。
#-----------------------------------------------------------------------------------------
#模块搜索路径
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143186362353505516c5d4e38456fb225c18cc5b54ffb000
 
当我们试图加载一个模块时，Python会在指定的路径下搜索对应的.py文件，如果找不到，就会报错：

>>> import mymodule
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
ImportError: No module named mymodule
默认情况下，Python解释器会搜索当前目录、所有已安装的内置模块和第三方模块，搜索路径存放在sys模块的path变量中：

>>> import sys
>>> sys.path
['', '/Library/Frameworks/Python.framework/Versions/3.6/lib/python36.zip', '/Library/Frameworks/Python.framework/Versions/3.6/lib/python3.6', ..., '/Library/Frameworks/Python.framework/Versions/3.6/lib/python3.6/site-packages']
如果我们要添加自己的搜索目录，有两种方法：

一是直接修改sys.path，添加要搜索的目录：

>>> import sys
>>> sys.path.append('/Users/michael/my_py_scripts')
这种方法是在运行时修改，运行结束后失效。

第二种方法是设置环境变量PYTHONPATH，该环境变量的内容会被自动添加到模块搜索路径中。设置方式与设置Path环境变量类似。注意只需要添加你自己的搜索路径，Python自己本身的搜索路径不受影响。
#-----------------------------------------------------------------------------------------
#访问限制
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014318650247930b1b21d7d3c64fe38c4b5a80d4469ad7000

#!/usr/bin/env python3
# -*- coding: utf-8 -*-

class Student(object):
    def __init__(self, name, score):
        self.__name = name
        self.__score = score

    def print_score(self):
        print('%s: %s' % (self.__name, self.__score))


if __name__ == "__main__":
    ben = Student("Ben", 59)
    ben.print_score()
    #print(ben.name) #无法访问
    print(ben._Student__name)#可以访问
改完后，对于外部代码来说，没什么变动，但是已经无法从外部访问实例变量.__name和实例变量.__score了：

需要注意的是，在Python中，变量名类似__xxx__的，也就是以双下划线开头，并且以双下划线结尾的，是特殊变量，特殊变量是可以直接访问的，不是private变量，所以，不能用__name__、__score__这样的变量名。

有些时候，你会看到以一个下划线开头的实例变量名，比如_name，这样的实例变量外部是可以访问的，但是，按照约定俗成的规定，当你看到这样的变量时，意思就是，“虽然我可以被访问，但是，请把我视为私有变量，不要随意访问”。

双下划线开头的实例变量是不是一定不能从外部访问呢？其实也不是。不能直接访问__name是因为Python解释器对外把__name变量改成了_Student__name，所以，仍然可以通过_Student__name来访问__name变量：

>>> bart._Student__name
'Bart Simpson'
但是强烈建议你不要这么干，因为不同版本的Python解释器可能会把__name改成不同的变量名。

#-----------------------------------------------------------------------------------------
#继承和多态 https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431865288798deef438d865e4c2985acff7e9fad15e3000
静态语言 vs 动态语言

对于静态语言（例如Java）来说，如果需要传入Animal类型，则传入的对象必须是Animal类型或者它的子类，否则，将无法调用run()方法。

对于Python这样的动态语言来说，则不一定需要传入Animal类型。我们只需要保证传入的对象有一个run()方法就可以了：

class Timer(object):
    def run(self):
        print('Start...')
这就是动态语言的“鸭子类型”，它并不要求严格的继承体系，一个对象只要“看起来像鸭子，走起路来像鸭子”，那它就可以被看做是鸭子。

Python的“file-like object“就是一种鸭子类型。对真正的文件对象，它有一个read()方法，返回其内容。但是，许多对象，只要有read()方法，都被视为“file-like object“。许多函数接收的参数就是“file-like object“，你不一定要传入真正的文件对象，完全可以传入任何实现了read()方法的对象。
#-----------------------------------------------------------------------------------------
#获取对象信息
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431866385235335917b66049448ab14a499afd5b24db000
type()函数返回的是什么类型呢？它返回对应的Class类型。如果我们要在if语句中判断，就需要比较两个变量的type类型是否相同：

>>> type(123)==type(456)
True
>>> type(123)==int
True
>>> type('abc')==type('123')
True
>>> type('abc')==str
True
>>> type('abc')==type(123)
False

判断基本数据类型可以直接写int，str等，但如果要判断一个对象是否是函数怎么办？可以使用types模块中定义的常量：
>>> import types
>>> def fn():
...     pass
...
>>> type(fn)==types.FunctionType
True
>>> type(abs)==types.BuiltinFunctionType
True
>>> type(lambda x: x)==types.LambdaType
True
>>> type((x for x in range(10)))==types.GeneratorType
True

使用isinstance()

对于class的继承关系来说，使用type()就很不方便。我们要判断class的类型，可以使用isinstance()函数。

我们回顾上次的例子，如果继承关系是：

object -> Animal -> Dog -> Husky
那么，isinstance()就可以告诉我们，一个对象是否是某种类型。先创建3种类型的对象：

>>> a = Animal()
>>> d = Dog()
>>> h = Husky()
然后，判断：

>>> isinstance(h, Husky)
True
没有问题，因为h变量指向的就是Husky对象。

再判断：

>>> isinstance(h, Dog)
True
h虽然自身是Husky类型，但由于Husky是从Dog继承下来的，所以，h也还是Dog类型。换句话说，isinstance()判断的是一个对象是否是该类型本身，或者位于该类型的父继承链上。

因此，我们可以确信，h还是Animal类型：

>>> isinstance(h, Animal)
True
同理，实际类型是Dog的d也是Animal类型：

>>> isinstance(d, Dog) and isinstance(d, Animal)
True
但是，d不是Husky类型：

>>> isinstance(d, Husky)
False
能用type()判断的基本类型也可以用isinstance()判断：

>>> isinstance('a', str)
True
>>> isinstance(123, int)
True
>>> isinstance(b'a', bytes)
True
并且还可以判断一个变量是否是某些类型中的一种，比如下面的代码就可以判断是否是list或者tuple：

>>> isinstance([1, 2, 3], (list, tuple))
True
>>> isinstance((1, 2, 3), (list, tuple))
True

使用dir()

如果要获得一个对象的所有属性和方法，可以使用dir()函数，它返回一个包含字符串的list，比如，获得一个str对象的所有属性和方法：

>>> dir('ABC')
['__add__', '__class__',..., '__subclasshook__', 'capitalize', 'casefold',..., 'zfill']
类似__xxx__的属性和方法在Python中都是有特殊用途的，比如__len__方法返回长度。在Python中，如果你调用len()函数试图获取一个对象的长度，实际上，在len()函数内部，它自动去调用该对象的__len__()方法，所以，下面的代码是等价的：

>>> len('ABC')
3
>>> 'ABC'.__len__()
3
我们自己写的类，如果也想用len(myObj)的话，就自己写一个__len__()方法：

>>> class MyDog(object):
...     def __len__(self):
...         return 100
...
>>> dog = MyDog()
>>> len(dog)
100


仅仅把属性和方法列出来是不够的，配合getattr()、setattr()以及hasattr()，我们可以直接操作一个对象的状态：
>>> class MyObject(object):
...     def __init__(self):
...         self.x = 9
...     def power(self):
...         return self.x * self.x
...
>>> obj = MyObject()
紧接着，可以测试该对象的属性：

>>> hasattr(obj, 'x') # 有属性'x'吗？
True
>>> obj.x
9
>>> hasattr(obj, 'y') # 有属性'y'吗？
False
>>> setattr(obj, 'y', 19) # 设置一个属性'y'
>>> hasattr(obj, 'y') # 有属性'y'吗？
True
>>> getattr(obj, 'y') # 获取属性'y'
19
>>> obj.y # 获取属性'y'
19
如果试图获取不存在的属性，会抛出AttributeError的错误：

>>> getattr(obj, 'z') # 获取属性'z'
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
AttributeError: 'MyObject' object has no attribute 'z'

可以传入一个default参数，如果属性不存在，就返回默认值：

>>> getattr(obj, 'z', 404) # 获取属性'z'，如果不存在，返回默认值404
404
也可以获得对象的方法：

>>> hasattr(obj, 'power') # 有属性'power'吗？
True
>>> getattr(obj, 'power') # 获取属性'power'
<bound method MyObject.power of <__main__.MyObject object at 0x10077a6a0>>
>>> fn = getattr(obj, 'power') # 获取属性'power'并赋值到变量fn
>>> fn # fn指向obj.power
<bound method MyObject.power of <__main__.MyObject object at 0x10077a6a0>>
>>> fn() # 调用fn()与调用obj.power()是一样的
81


小结

通过内置的一系列函数，我们可以对任意一个Python对象进行剖析，拿到其内部的数据。要注意的是，只有在不知道对象信息的时候，我们才会去获取对象信息。如果可以直接写：

sum = obj.x + obj.y
就不要写：

sum = getattr(obj, 'x') + getattr(obj, 'y')
一个正确的用法的例子如下：

def readImage(fp):
    if hasattr(fp, 'read'):
        return readData(fp)
    return None
假设我们希望从文件流fp中读取图像，我们首先要判断该fp对象是否存在read方法，如果存在，则该对象是一个流，如果不存在，则无法读取。hasattr()就派上了用场。

请注意，在Python这类动态语言中，根据鸭子类型，有read()方法，不代表该fp对象就是一个文件流，它也可能是网络流，也可能是内存中的一个字节流，但只要read()方法返回的是有效的图像数据，就不影响读取图像的功能。
#-----------------------------------------------------------------------------------------
#关于Python类属性与实例属性的讨论
#http://python.jobbole.com/85100/
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

class Student(object):
    COUNT = 10
    def __init__(self, name, score):
        self.__name = name
        self.__score = score

    def print_score(self):
        print('%s: %s' % (self.__name, self.__score))


if __name__ == "__main__":
    s1 = Student("Ben", 99)
    s2 = Student("Mei", 80)
    print(s1.COUNT, s2.COUNT, Student.COUNT)
    s1.COUNT += 2
    print(s1.COUNT, s2.COUNT, Student.COUNT)
    Student.COUNT += 3
    print(s1.COUNT, s2.COUNT, Student.COUNT)
输出：
10 10 10
12 10 10
12 13 13

Python中属性查找机制

Python中属性的获取存在一个向上查找机制，还是拿上面的例子做说明：
Python中一切皆对象，AAA属于类对象，obj1属于实例对象，从对象的角度来看，AAA与obj1是两个无关的对象，但是，Python通过下面的查找树建立了类对象AAA与实例对象obj1、obj2之间的关系。
如图所示
        AAA
         |
       -----
      |     |  
    obj1   obj2
    
但是情形1中调用obj1.aaa时，Python按照从obj1到AAA的顺序由下到上查找属性aaa。
值得注意的这时候obj1是没有属性aaa的，于是，Python到类AAA中去查找，成功找到，并显示出来。所以，从现象上来看，AAA的属性aaa确实是共享给其所有实例的，虽然这里只是从查找树的形式模拟了其关系。

Python中的属性设置

原帖子的作者也指出问题的关键在于情形2中obj1.aaa += 2。
为什么呢？
上面我们指出obj.aaa += 2包含了属性获取及属性设置两个操作。即obj1.aaa += 2等价于obj1.aaa = obj1.aaa + 2。
其中等式右侧的obj.aaa属于属性获取，其规则是按照上面提到的查找规则进行，即，这时候，获取到的是AAA的属性aaa，所以等式左侧的值为12。
第二个操作是属性设置，即obj.aaa = 12。当发生属性设置的时候，obj1这个实例对象没有属性aaa，因此会为自身动态添加一个属性aaa。
由于从对象的角度，类对象和实例对象属于两个独立的对象，所以，这个aaa属性只属于obj1，也就是说，这时候类对象AAA和实例对象aaa各自有一个属性aaa。
那么，在情形3中，再次调用obj1.aaa时，按照属性调用查找规则，这个时候获取到的是实例对象obj1的属性aaa，而不是类对象AAA的属性aaa。
#-----------------------------------------------------------------------------------------
#使用__slots__
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143186739713011a09b63dcbd42cc87f907a778b3ac73000使用__slots__

正常情况下，当我们定义了一个class，创建了一个class的实例后，我们可以给该实例绑定任何属性和方法，这就是动态语言的灵活性。先定义class：

class Student(object):
    pass
然后，尝试给实例绑定一个属性：

>>> s = Student()
>>> s.name = 'Michael' # 动态给实例绑定一个属性
>>> print(s.name)
Michael
还可以尝试给实例绑定一个方法：

>>> def set_age(self, age): # 定义一个函数作为实例方法
...     self.age = age
...
>>> from types import MethodType
>>> s.set_age = MethodType(set_age, s) # 给实例绑定一个方法
>>> s.set_age(25) # 调用实例方法
>>> s.age # 测试结果
25
但是，给一个实例绑定的方法，对另一个实例是不起作用的：

>>> s2 = Student() # 创建新的实例
>>> s2.set_age(25) # 尝试调用方法
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
AttributeError: 'Student' object has no attribute 'set_age'
为了给所有实例都绑定方法，可以给class绑定方法：

>>> def set_score(self, score):
...     self.score = score
...
>>> Student.set_score = set_score
给class绑定方法后，所有实例均可调用：

>>> s.set_score(100)
>>> s.score
100
>>> s2.set_score(99)
>>> s2.score
99
通常情况下，上面的set_score方法可以直接定义在class中，但动态绑定允许我们在程序运行的过程中动态给class加上功能，这在静态语言中很难实现。

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from types import MethodType


class Student(object):
    def __init__(self, name, score):
        self.__name = name
        self.score = score

    def print_score(self):
        print('%s: %s' % (self.__name, self.score))

def set_age(self, age):
    self.age = age

def set_name(self, name):
    self.__name = name

def set_score(self, score):
    self.score = score

def print_age(self):
    # print('Name[%s]: score[%s] age[%s]' % (self.__name, self.__score, self.age))
    print('age[%s]' % self.age)


if __name__ == "__main__":
    s1 = Student("Ben", 99)
    s1.set_age = MethodType(set_age, s1)
    s1.set_name = MethodType(set_name, s1)
    s1.print_age = MethodType(print_age, s1)
    s1.set_score = MethodType(set_score, s1)
    s1.set_age(25)
    s1.set_name("Yan") #原来的__name变为为_Student__name，当前是给s1对象重新绑定了一个属性__name，并未改变原来的_Student__name
    print(s1.__dict__)
    print(s1.age)
    s1.print_age()
    s1.print_score()

    s1.set_score("89")
    s1.print_score()
输出：
{'_Student__name': 'Ben', 'score': 99, 'set_age': <bound method set_age of <__main__.Student object at 0x0000025FE8E07898>>, 'set_name': <bound method set_name of <__main__.Student object at 0x0000025FE8E07898>>, 'print_age': <bound method print_age of <__main__.Student object at 0x0000025FE8E07898>>, 'set_score': <bound method set_score of <__main__.Student object at 0x0000025FE8E07898>>, 'age': 25, '__name': 'Yan'}
25
age[25]
Ben: 99
Ben: 89

使用__slots__
但是，如果我们想要限制实例的属性怎么办？比如，只允许对Student实例添加name和age属性。
为了达到限制的目的，Python允许在定义class的时候，定义一个特殊的__slots__变量，来限制该class实例能添加的属性：
class Student(object):
    __slots__ = ('name', 'age') # 用tuple定义允许绑定的属性名称
然后，我们试试：

>>> s = Student() # 创建新的实例
>>> s.name = 'Michael' # 绑定属性'name'
>>> s.age = 25 # 绑定属性'age'
>>> s.score = 99 # 绑定属性'score'
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
AttributeError: 'Student' object has no attribute 'score'
由于'score'没有被放到__slots__中，所以不能绑定score属性，试图绑定score将得到AttributeError的错误。

使用__slots__要注意，__slots__定义的属性仅对当前类实例起作用，对继承的子类是不起作用的：

>>> class GraduateStudent(Student):
...     pass
...
>>> g = GraduateStudent()
>>> g.score = 9999
除非在子类中也定义__slots__，这样，子类实例允许定义的属性就是自身的__slots__加上父类的__slots__。
#-----------------------------------------------------------------------------------------
#使用@property
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143186781871161bc8d6497004764b398401a401d4cce000

@property标签类似VBA的Let_Property和Get_Property，可以直接获取和设置对象属性

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from types import MethodType


class Student(object):
    def __init__(self, name, score):
        self.__name = name
        self.__score = score

    def print_score(self):
        print('%s: %s' % (self.__name, self.__score))

    @property #需要Setter函数和Getter函数和@score的名字三者一样
    def score(self):
        return self.__score

    @score.setter
    def score(self, value):
        if not isinstance(value, int):
            raise ValueError('score must be an integer!')
        if value < 0 or value > 100:
            raise ValueError('score must between 0 ~ 100!')
        self.__score = value

if __name__ == "__main__":
    s1 = Student("Ben", 99)
    # s1._Student__score = 9999
    print(s1.__dict__)
    print(s1.__dir__())
    s1.score = 97
    print(s1.score)
    s1.print_score()


#-----------------------------------------------------------------------------------------
#多重继承
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014318680104044a55f4a9dbf8452caf71e8dc68b75a18000
MixIn

在设计类的继承关系时，通常，主线都是单一继承下来的，例如，Ostrich继承自Bird。但是，如果需要“混入”额外的功能，通过多重继承就可以实现，比如，让Ostrich除了继承自Bird外，再同时继承Runnable。这种设计通常称之为MixIn。

为了更好地看出继承关系，我们把Runnable和Flyable改为RunnableMixIn和FlyableMixIn。类似的，你还可以定义出肉食动物CarnivorousMixIn和植食动物HerbivoresMixIn，让某个动物同时拥有好几个MixIn：

class Dog(Mammal, RunnableMixIn, CarnivorousMixIn):
    pass
MixIn的目的就是给一个类增加多个功能，这样，在设计类的时候，我们优先考虑通过多重继承来组合多个MixIn的功能，而不是设计多层次的复杂的继承关系。

Python自带的很多库也使用了MixIn。举个例子，Python自带了TCPServer和UDPServer这两类网络服务，而要同时服务多个用户就必须使用多进程或多线程模型，这两种模型由ForkingMixIn和ThreadingMixIn提供。通过组合，我们就可以创造出合适的服务来。

比如，编写一个多进程模式的TCP服务，定义如下：

class MyTCPServer(TCPServer, ForkingMixIn):
    pass
编写一个多线程模式的UDP服务，定义如下：

class MyUDPServer(UDPServer, ThreadingMixIn):
    pass
如果你打算搞一个更先进的协程模型，可以编写一个CoroutineMixIn：

class MyTCPServer(TCPServer, CoroutineMixIn):
    pass
这样一来，我们不需要复杂而庞大的继承链，只要选择组合不同的类的功能，就可以快速构造出所需的子类。
#-----------------------------------------------------------------------------------------
#定制类
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014319098638265527beb24f7840aa97de564ccc7f20f6000
__str__

我们先定义一个Student类，打印一个实例：

>>> class Student(object):
...     def __init__(self, name):
...         self.name = name
...
>>> print(Student('Michael'))
<__main__.Student object at 0x109afb190>
打印出一堆<__main__.Student object at 0x109afb190>，不好看。

怎么才能打印得好看呢？只需要定义好__str__()方法，返回一个好看的字符串就可以了：

>>> class Student(object):
...     def __init__(self, name):
...         self.name = name
...     def __str__(self):
...         return 'Student object (name: %s)' % self.name
...
>>> print(Student('Michael'))
Student object (name: Michael)
这样打印出来的实例，不但好看，而且容易看出实例内部重要的数据。

但是细心的朋友会发现直接敲变量不用print，打印出来的实例还是不好看：

>>> s = Student('Michael')
>>> s
<__main__.Student object at 0x109afb310>
这是因为直接显示变量调用的不是__str__()，而是__repr__()，两者的区别是__str__()返回用户看到的字符串，而__repr__()返回程序开发者看到的字符串，也就是说，__repr__()是为调试服务的。

解决办法是再定义一个__repr__()。但是通常__str__()和__repr__()代码都是一样的，所以，有个偷懒的写法：

class Student(object):
    def __init__(self, name):
        self.name = name
    def __str__(self):
        return 'Student object (name=%s)' % self.name
    __repr__ = __str__
    

__iter__

如果一个类想被用于for ... in循环，类似list或tuple那样，就必须实现一个__iter__()方法，该方法返回一个迭代对象，然后，Python的for循环就会不断调用该迭代对象的__next__()方法拿到循环的下一个值，直到遇到StopIteration错误时退出循环。

我们以斐波那契数列为例，写一个Fib类，可以作用于for循环：

class Fib(object):
    def __init__(self):
        self.a, self.b = 0, 1 # 初始化两个计数器a，b

    def __iter__(self):
        return self # 实例本身就是迭代对象，故返回自己

    def __next__(self):
        self.a, self.b = self.b, self.a + self.b # 计算下一个值
        if self.a > 100000: # 退出循环的条件
            raise StopIteration()
        return self.a # 返回下一个值
现在，试试把Fib实例作用于for循环：

>>> for n in Fib():
...     print(n)
...
1
1
2
3
5
...
46368
75025

__getitem__

Fib实例虽然能作用于for循环，看起来和list有点像，但是，把它当成list来使用还是不行，比如，取第5个元素：

>>> Fib()[5]
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
TypeError: 'Fib' object does not support indexing
要表现得像list那样按照下标取出元素，需要实现__getitem__()方法：

class Fib(object):
    def __getitem__(self, n):
        a, b = 1, 1
        for x in range(n):
            a, b = b, a + b
        return a
现在，就可以按下标访问数列的任意一项了：

>>> f = Fib()
>>> f[0]
1
>>> f[1]
1
>>> f[2]
2
>>> f[3]
3
>>> f[10]
89
>>> f[100]
573147844013817084101
但是list有个神奇的切片方法：

>>> list(range(100))[5:10]
[5, 6, 7, 8, 9]
对于Fib却报错。原因是__getitem__()传入的参数可能是一个int，也可能是一个切片对象slice，所以要做判断：

class Fib(object):
    def __getitem__(self, n):
        if isinstance(n, int): # n是索引
            a, b = 1, 1
            for x in range(n):
                a, b = b, a + b
            return a
        if isinstance(n, slice): # n是切片
            start = n.start
            stop = n.stop
            if start is None:
                start = 0
            a, b = 1, 1
            L = []
            for x in range(stop):
                if x >= start:
                    L.append(a)
                a, b = b, a + b
            return L
现在试试Fib的切片：

>>> f = Fib()
>>> f[0:5]
[1, 1, 2, 3, 5]
>>> f[:10]
[1, 1, 2, 3, 5, 8, 13, 21, 34, 55]
但是没有对step参数作处理：

>>> f[:10:2]
[1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89]
也没有对负数作处理，所以，要正确实现一个__getitem__()还是有很多工作要做的。

此外，如果把对象看成dict，__getitem__()的参数也可能是一个可以作key的object，例如str。

与之对应的是__setitem__()方法，把对象视作list或dict来对集合赋值。最后，还有一个__delitem__()方法，用于删除某个元素。

总之，通过上面的方法，我们自己定义的类表现得和Python自带的list、tuple、dict没什么区别，这完全归功于动态语言的“鸭子类型”，不需要强制继承某个接口。


__getattr__

正常情况下，当我们调用类的方法或属性时，如果不存在，就会报错。比如定义Student类：

class Student(object):

    def __init__(self):
        self.name = 'Michael'
调用name属性，没问题，但是，调用不存在的score属性，就有问题了：

>>> s = Student()
>>> print(s.name)
Michael
>>> print(s.score)
Traceback (most recent call last):
  ...
AttributeError: 'Student' object has no attribute 'score'
错误信息很清楚地告诉我们，没有找到score这个attribute。

要避免这个错误，除了可以加上一个score属性外，Python还有另一个机制，那就是写一个__getattr__()方法，动态返回一个属性。修改如下：

class Student(object):

    def __init__(self):
        self.name = 'Michael'

    def __getattr__(self, attr):
        if attr=='score':
            return 99
当调用不存在的属性时，比如score，Python解释器会试图调用__getattr__(self, 'score')来尝试获得属性，这样，我们就有机会返回score的值：

>>> s = Student()
>>> s.name
'Michael'
>>> s.score
99
返回函数也是完全可以的：

class Student(object):

    def __getattr__(self, attr):
        if attr=='age':
            return lambda: 25
只是调用方式要变为：

>>> s.age()
25
注意，只有在没有找到属性的情况下，才调用__getattr__，已有的属性，比如name，不会在__getattr__中查找。

此外，注意到任意调用如s.abc都会返回None，这是因为我们定义的__getattr__默认返回就是None。要让class只响应特定的几个属性，我们就要按照约定，抛出AttributeError的错误：

class Student(object):

    def __getattr__(self, attr):
        if attr=='age':
            return lambda: 25
        raise AttributeError('\'Student\' object has no attribute \'%s\'' % attr)
这实际上可以把一个类的所有属性和方法调用全部动态化处理了，不需要任何特殊手段。

这种完全动态调用的特性有什么实际作用呢？作用就是，可以针对完全动态的情况作调用。

举个例子：

现在很多网站都搞REST API，比如新浪微博、豆瓣啥的，调用API的URL类似：

http://api.server/user/friends
http://api.server/user/timeline/list
如果要写SDK，给每个URL对应的API都写一个方法，那得累死，而且，API一旦改动，SDK也要改。

利用完全动态的__getattr__，我们可以写出一个链式调用：

class Chain(object):

    def __init__(self, path=''):
        self._path = path

    def __getattr__(self, path):
        return Chain('%s/%s' % (self._path, path))

    def __str__(self):
        return self._path

    __repr__ = __str__
试试：

>>> Chain().status.user.timeline.list #每一个.属性获取的都是一个Chain对象
'/status/user/timeline/list'
这样，无论API怎么变，SDK都可以根据URL实现完全动态的调用，而且，不随API的增加而改变！

还有些REST API会把参数放到URL中，比如GitHub的API：

GET /users/:user/repos
调用时，需要把:user替换为实际用户名。如果我们能写出这样的链式调用：

Chain().users('michael').repos
就可以非常方便地调用API了。有兴趣的童鞋可以试试写出来。
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

class Chain(object):
    def __init__(self, path=''):
        self._path = path

    def __getattr__(self, path):
        if path == "users":
            return lambda path: Chain('%s/users/%s' % (self._path, path))
        return Chain('%s/%s' % (self._path, path)) #这里返回的是一个Chain()的实例化对象，没有(str)函数，所以要如果users('str')时，需要返回一个可调用的对象，即返回一个函数，该函数接受一个参数，返回一个新的Chain对象

    def __str__(self):
        return self._path

    __repr__ = __str__


if __name__ == "__main__":
    s = Chain().abc.users('michael').repos.opq
    print(s)
输出：
/abc/users/michael/repos/opq


__call__

一个对象实例可以有自己的属性和方法，当我们调用实例方法时，我们用instance.method()来调用。能不能直接在实例本身上调用呢？在Python中，答案是肯定的。

任何类，只需要定义一个__call__()方法，就可以直接对实例进行调用。请看示例：

class Student(object):
    def __init__(self, name):
        self.name = name

    def __call__(self):
        print('My name is %s.' % self.name)
调用方式如下：

>>> s = Student('Michael')
>>> s() # self参数不要传入
My name is Michael.
__call__()还可以定义参数。对实例进行直接调用就好比对一个函数进行调用一样，所以你完全可以把对象看成函数，把函数看成对象，因为这两者之间本来就没啥根本的区别。

如果你把对象看成函数，那么函数本身其实也可以在运行期动态创建出来，因为类的实例都是运行期创建出来的，这么一来，我们就模糊了对象和函数的界限。

那么，怎么判断一个变量是对象还是函数呢？其实，更多的时候，我们需要判断一个对象是否能被调用，能被调用的对象就是一个Callable对象，比如函数和我们上面定义的带有__call__()的类实例：

>>> callable(Student())
True
>>> callable(max)
True
>>> callable([1, 2, 3])
False
>>> callable(None)
False
>>> callable('str')
False
通过callable()函数，我们就可以判断一个对象是否是“可调用”对象。

学习了__call__方法，上面的Chain类可以修改为：
class Chain(object):
    def __init__(self, path=''):
        self._path = path

    def __getattr__(self, path):
        return Chain('%s/%s' % (self._path, path))

    def __str__(self):
        return self._path

    def __call__(self, s):
        return Chain('%s/%s' % (self._path, s))

    __repr__ = __str__
#-----------------------------------------------------------------------------------------
#使用枚举类
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143191235886950998592cd3e426e91687cdae696e64b000

当我们需要定义常量时，一个办法是用大写变量通过整数来定义，例如月份：

JAN = 1
FEB = 2
MAR = 3
...
NOV = 11
DEC = 12
好处是简单，缺点是类型是int，并且仍然是变量。

更好的方法是为这样的枚举类型定义一个class类型，然后，每个常量都是class的一个唯一实例。Python提供了Enum类来实现这个功能：

from enum import Enum

Month = Enum('Month', ('Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'))
这样我们就获得了Month类型的枚举类，可以直接使用Month.Jan来引用一个常量，或者枚举它的所有成员：

for name, member in Month.__members__.items():
    print(name, '=>', member, ',', member.value)
value属性则是自动赋给成员的int常量，默认从1开始计数。


#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from enum import Enum


if __name__ == "__main__":
    Month = Enum('Month', ('Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'))
    print(type(Month.__members__))
    for name, member in Month.__members__.items():
        print(name, '=>', member, ',', member.value)
输出：
<class 'mappingproxy'>
Jan => Month.Jan , 1
Feb => Month.Feb , 2
Mar => Month.Mar , 3
Apr => Month.Apr , 4
May => Month.May , 5
Jun => Month.Jun , 6
Jul => Month.Jul , 7
Aug => Month.Aug , 8
Sep => Month.Sep , 9
Oct => Month.Oct , 10
Nov => Month.Nov , 11
Dec => Month.Dec , 12

如果需要更精确地控制枚举类型，可以从Enum派生出自定义类：

from enum import Enum, unique

@unique
class Weekday(Enum):
    Sun = 0 # Sun的value被设定为0
    Mon = 1
    Tue = 2
    Wed = 3
    Thu = 4
    Fri = 5
    Sat = 6
@unique装饰器可以帮助我们检查保证没有重复值。
访问这些枚举类型可以有若干种方法：
>>> day1 = Weekday.Mon
>>> print(day1)
Weekday.Mon
>>> print(Weekday.Tue)
Weekday.Tue
>>> print(Weekday['Tue'])
Weekday.Tue
>>> print(Weekday.Tue.value)
2
>>> print(day1 == Weekday.Mon)
True
>>> print(day1 == Weekday.Tue)
False
>>> print(Weekday(1))
Weekday.Mon
>>> print(day1 == Weekday(1))
True
>>> Weekday(7)
Traceback (most recent call last):
  ...
ValueError: 7 is not a valid Weekday
>>> for name, member in Weekday.__members__.items():
        print(name, '=>', member, member.value)
...
Sun => Weekday.Sun 0
Mon => Weekday.Mon 1
Tue => Weekday.Tue 2
Wed => Weekday.Wed 3
Thu => Weekday.Thu 4
Fri => Weekday.Fri 5
Sat => Weekday.Sat 6
可见，既可以用成员名称引用枚举常量，又可以直接根据value的值获得枚举常量。

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from enum import Enum, unique

@unique
class Weekday(Enum):
    Sun = 0
    Mon = 1
    Tue = 2
    Wed = 3
    Thu = 4
    Fri = 5
    Sat = 0

if __name__ == "__main__":
    day1 = Weekday.Mon
    print(day1)

#-----------------------------------------------------------------------------------------
#使用元类 https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014319106919344c4ef8b1e04c48778bb45796e0335839000

type()

动态语言和静态语言最大的不同，就是函数和类的定义，不是编译时定义的，而是运行时动态创建的。

比方说我们要定义一个Hello的class，就写一个hello.py模块：

class Hello(object):
    def hello(self, name='world'):
        print('Hello, %s.' % name)
当Python解释器载入hello模块时，就会依次执行该模块的所有语句，执行结果就是动态创建出一个Hello的class对象，测试如下：

>>> from hello import Hello
>>> h = Hello()
>>> h.hello()
Hello, world.
>>> print(type(Hello))
<class 'type'>
>>> print(type(h))
<class 'hello.Hello'>
type()函数可以查看一个类型或变量的类型，Hello是一个class，它的类型就是type，而h是一个实例，它的类型就是class Hello。

我们说class的定义是运行时动态创建的，而创建class的方法就是使用type()函数。

type()函数既可以返回一个对象的类型，又可以创建出新的类型，比如，我们可以通过type()函数创建出Hello类，而无需通过class Hello(object)...的定义：

>>> def fn(self, name='world'): # 先定义函数
...     print('Hello, %s.' % name)
...
>>> Hello = type('Hello', (object,), dict(hello=fn)) # 创建Hello class
>>> h = Hello()
>>> h.hello()
Hello, world.
>>> print(type(Hello))
<class 'type'>
>>> print(type(h))
<class '__main__.Hello'>
要创建一个class对象，type()函数依次传入3个参数：

class的名称；
继承的父类集合，注意Python支持多重继承，如果只有一个父类，别忘了tuple的单元素写法；
class的方法名称与函数绑定，这里我们把函数fn绑定到方法名hello上。
通过type()函数创建的类和直接写class是完全一样的，因为Python解释器遇到class定义时，仅仅是扫描一下class定义的语法，然后调用type()函数创建出class。

正常情况下，我们都用class Xxx...来定义类，但是，type()函数也允许我们动态创建出类来，也就是说，动态语言本身支持运行期动态创建类，这和静态语言有非常大的不同，要在静态语言运行期创建类，必须构造源代码字符串再调用编译器，或者借助一些工具生成字节码实现，本质上都是动态编译，会非常复杂。

metaclass
除了使用type()动态创建类以外，要控制类的创建行为，还可以使用metaclass。
metaclass，直译为元类，简单的解释就是：

当我们定义了类以后，就可以根据这个类创建出实例，所以：先定义类，然后创建实例。

但是如果我们想创建出类呢？那就必须根据metaclass创建出类，所以：先定义metaclass，然后创建类。

连接起来就是：先定义metaclass，就可以创建类，最后创建实例。

所以，metaclass允许你创建类或者修改类。换句话说，你可以把类看成是metaclass创建出来的“实例”。

metaclass是Python面向对象里最难理解，也是最难使用的魔术代码。正常情况下，你不会碰到需要使用metaclass的情况，所以，以下内容看不懂也没关系，因为基本上你不会用到。

我们先看一个简单的例子，这个metaclass可以给我们自定义的MyList增加一个add方法：

定义ListMetaclass，按照默认习惯，metaclass的类名总是以Metaclass结尾，以便清楚地表示这是一个metaclass：

# metaclass是类的模板，所以必须从`type`类型派生：
class ListMetaclass(type):
    def __new__(cls, name, bases, attrs):
        attrs['add'] = lambda self, value: self.append(value)
        return type.__new__(cls, name, bases, attrs)
有了ListMetaclass，我们在定义类的时候还要指示使用ListMetaclass来定制类，传入关键字参数metaclass：

class MyList(list, metaclass=ListMetaclass):
    pass
当我们传入关键字参数metaclass时，魔术就生效了，它指示Python解释器在创建MyList时，要通过ListMetaclass.__new__()来创建，在此，我们可以修改类的定义，比如，加上新的方法，然后，返回修改后的定义。

__new__()方法接收到的参数依次是：

当前准备创建的类的对象；

类的名字；

类继承的父类集合；

类的方法集合。

测试一下MyList是否可以调用add()方法：

>>> L = MyList()
>>> L.add(1)
>> L
[1]
而普通的list没有add()方法：

>>> L2 = list()
>>> L2.add(1)
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
AttributeError: 'list' object has no attribute 'add'
动态修改有什么意义？直接在MyList定义中写上add()方法不是更简单吗？正常情况下，确实应该直接写，通过metaclass修改纯属变态。

但是，总会遇到需要通过metaclass修改类定义的。ORM就是一个典型的例子。

ORM全称“Object Relational Mapping”，即对象-关系映射，就是把关系数据库的一行映射为一个对象，也就是一个类对应一个表，这样，写代码更简单，不用直接操作SQL语句。

要编写一个ORM框架，所有的类都只能动态定义，因为只有使用者才能根据表的结构定义出对应的类来。

让我们来尝试编写一个ORM框架。

#https://blog.csdn.net/johnsonguo/article/details/585193
#https://www.zhihu.com/question/20040039

class Base(object):
    def __init__(self):
        print('Base.__init__')

class A(Base):
    def __init__(self):
        print('A.__init__ begin')
        super(A, self).__init__()
        print('A.__init__ end')

class B(Base):
    def __init__(self):
        print('B.__init__ begin')
        super(B, self).__init__()
        print('B.__init__ end')

class C(A,B):
    def __init__(self):
        print('C.__init__ begin')
        super(C, self).__init__()  # Only one call to super() here
        print('C.__init__ end')

c = C()
print C.__mro__
运行结果：C.__init__ begin
A.__init__ begin
B.__init__ begin
Base.__init__
B.__init__ end
A.__init__ end
C.__init__ end
(<class '__main__.C'>, <class '__main__.A'>, <class '__main__.B'>, <class '__main__.Base'>, <type 'object'>)
当你使用 super() 函数时，Python会在MRO列表上继续搜索下一个类。 只要每个重定义的方法统一使用 super() 并只调用它一次， 那么控制流最终会遍历完整个MRO列表，每个方法也只会被调用一次。 这也是为什么在第二个例子中你不会调用两次 Base.__init__() 的原因。

1. super并不是一个函数，是一个类名，形如super(B, self)事实上调用了super类的初始化函数，
产生了一个super对象；
2. super类的初始化函数并没有做什么特殊的操作，只是简单记录了类类型和具体实例；
3. super(B, self).func的调用并不是用于调用当前类的父类的func函数；
4. Python的多继承类是通过mro的方式来保证各个父类的函数被逐一调用，而且保证每个父类函数
只调用一次（如果每个类都使用super）；
5. 混用super类和非绑定的函数是一个危险行为，这可能导致应该调用的父类函数没有调用或者一
个父类函数被调用多次。



#!/usr/bin/env python3
# -*- coding: utf-8 -*-

' XXX module '

__author__ = 'DingBen'

if __name__=='__main__':
    pass


class Field(object):
    def __init__(self, name, column_type):
        self.name = name
        self.column_type = column_type

    def __str__(self):
        return '<%s:%s>' % (self.__class__.__name__, self.name)


class StringField(Field):
    def __init__(self, name):
        super(StringField, self).__init__(name, 'varchar(100')


class IntegerField(Field):
    def __init__(self, name):
        super(IntegerField, self).__init__(name, 'bigint')
        
        
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

' XXX module '
from src.Field import Field

__author__ = 'DingBen'


class ModelMetaClass(type):
    def __new__(cls, name, bases, attrs):
        if name == 'Model':
            return type.__new__(cls, name, bases, attrs)
        print("Found model: %s" % name)
        mappings = dict()
        for k, v in attrs.items():
            if isinstance(v, Field):
                print("Found mapping: %s ==> %s" % (k, v))
                mappings[k] = v
        for k in mappings.keys():
            attrs.pop(k)
        attrs['__mappings__'] = mappings
        attrs['__table__'] = name
        return type.__new__(cls, name, bases, attrs)


class Model(dict, metaclass=ModelMetaClass):
    def __init__(self, **kw):
        super(Model, self).__init__(**kw)

    def __getattr__(self, key):
        try:
            return self[key]
        except KeyError:
            raise AttributeError(r"'Model' object has no attribute '%s'" % key)

    def __setattr__(self, key, value):
        self[key] = value

    def save(self):
        fields = []
        params = []
        args = []
        for k, v in self.__mappings__.items():
            fields.append(v.name)
            params.append('?')
            args.append(getattr(self, k, None))
        sql = 'insert into %s (%s) values (%s)' % (self.__table__, ','.join(fields), ','.join(params))
        print('SQL: %s' % sql)
        print('ARGS: %s' % str(args))

        
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from src.Field import IntegerField, StringField
from src.Model import Model


class User(Model):
    # 定义类的属性到列的映射：
    id = IntegerField('id')
    name = StringField('username')
    email = StringField('email')
    password = StringField('password')


if __name__ == "__main__":
    u = User(id=12345, name="Michael", email="test@orm.org", password="my--pwd")
    u.save()

当用户定义一个class User(Model)时，Python解释器首先在当前类User的定义中查找metaclass，如果没有找到，就继续在父类Model中查找metaclass，找到了，就使用Model中定义的metaclass的ModelMetaclass来创建User类，也就是说，metaclass可以隐式地继承到子类，但子类自己却感觉不到。

在ModelMetaclass中，一共做了几件事情：

排除掉对Model类的修改；

在当前类（比如User）中查找定义的类的所有属性，如果找到一个Field属性，就把它保存到一个__mappings__的dict中，同时从类属性中删除该Field属性，否则，容易造成运行时错误（实例的属性会遮盖类的同名属性）；

把表名保存到__table__中，这里简化为表名默认为类名。

在Model类中，就可以定义各种操作数据库的方法，比如save()，delete()，find()，update等等。

我们实现了save()方法，把一个实例保存到数据库中。因为有表名，属性到字段的映射和属性值的集合，就可以构造出INSERT语句。

编写代码试试：

u = User(id=12345, name='Michael', email='test@orm.org', password='my-pwd')
u.save()
输出如下：

Found model: User
Found mapping: email ==> <StringField:email>
Found mapping: password ==> <StringField:password>
Found mapping: id ==> <IntegerField:uid>
Found mapping: name ==> <StringField:username>
SQL: insert into User (password,email,username,id) values (?,?,?,?)
ARGS: ['my-pwd', 'test@orm.org', 'Michael', 12345]
可以看到，save()方法已经打印出了可执行的SQL语句，以及参数列表，只需要真正连接到数据库，执行该SQL语句，就可以完成真正的功能。

不到100行代码，我们就通过metaclass实现了一个精简的ORM框架，是不是非常简单？

小结

metaclass是Python中非常具有魔术性的对象，它可以改变类创建时的行为。这种强大的功能使用起来务必小心。
#-----------------------------------------------------------------------------------------
#错误处理
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143191375461417a222c54b7e4d65b258f491c093a515000
try

让我们用一个例子来看看try的机制：

try:
    print('try...')
    r = 10 / 0
    print('result:', r)
except ZeroDivisionError as e:
    print('except:', e)
finally:
    print('finally...')
print('END')

int()函数可能会抛出ValueError，所以我们用一个except捕获ValueError，用另一个except捕获ZeroDivisionError。
此外，如果没有错误发生，可以在except语句块后面加一个else，当没有错误发生时，会自动执行else语句：
try:
    print('try...')
    r = 10 / int('2')
    print('result:', r)
except ValueError as e:
    print('ValueError:', e)
except ZeroDivisionError as e:
    print('ZeroDivisionError:', e)
else:
    print('no error!')
finally:
    print('finally...')
print('END')

Python所有的错误都是从BaseException类派生的，常见的错误类型和继承关系看这里：
#https://docs.python.org/3/library/exceptions.html#exception-hierarchy
The class hierarchy for built-in exceptions is:

BaseException
 +-- SystemExit
 +-- KeyboardInterrupt
 +-- GeneratorExit
 +-- Exception
      +-- StopIteration
      +-- StopAsyncIteration
      +-- ArithmeticError
      |    +-- FloatingPointError
      |    +-- OverflowError
      |    +-- ZeroDivisionError
      +-- AssertionError
      +-- AttributeError
      +-- BufferError
      +-- EOFError
      +-- ImportError
      |    +-- ModuleNotFoundError
      +-- LookupError
      |    +-- IndexError
      |    +-- KeyError
      +-- MemoryError
      +-- NameError
      |    +-- UnboundLocalError
      +-- OSError
      |    +-- BlockingIOError
      |    +-- ChildProcessError
      |    +-- ConnectionError
      |    |    +-- BrokenPipeError
      |    |    +-- ConnectionAbortedError
      |    |    +-- ConnectionRefusedError
      |    |    +-- ConnectionResetError
      |    +-- FileExistsError
      |    +-- FileNotFoundError
      |    +-- InterruptedError
      |    +-- IsADirectoryError
      |    +-- NotADirectoryError
      |    +-- PermissionError
      |    +-- ProcessLookupError
      |    +-- TimeoutError
      +-- ReferenceError
      +-- RuntimeError
      |    +-- NotImplementedError
      |    +-- RecursionError
      +-- SyntaxError
      |    +-- IndentationError
      |         +-- TabError
      +-- SystemError
      +-- TypeError
      +-- ValueError
      |    +-- UnicodeError
      |         +-- UnicodeDecodeError
      |         +-- UnicodeEncodeError
      |         +-- UnicodeTranslateError
      +-- Warning
           +-- DeprecationWarning
           +-- PendingDeprecationWarning
           +-- RuntimeWarning
           +-- SyntaxWarning
           +-- UserWarning
           +-- FutureWarning
           +-- ImportWarning
           +-- UnicodeWarning
           +-- BytesWarning
           +-- ResourceWarning

           
调用栈
如果错误没有被捕获，它就会一直往上抛，最后被Python解释器捕获，打印一个错误信息，然后程序退出。来看看err.py：
# err.py:
def foo(s):
    return 10 / int(s)

def bar(s):
    return foo(s) * 2

def main():
    bar('0')

main()
执行，结果如下：

$ python3 err.py
Traceback (most recent call last):
  File "err.py", line 11, in <module>
    main()
  File "err.py", line 9, in main
    bar('0')
  File "err.py", line 6, in bar
    return foo(s) * 2
  File "err.py", line 3, in foo
    return 10 / int(s)
ZeroDivisionError: division by zero
出错并不可怕，可怕的是不知道哪里出错了。解读错误信息是定位错误的关键。我们从上往下可以看到整个错误的调用函数链：

错误信息第1行：

Traceback (most recent call last):
告诉我们这是错误的跟踪信息。

第2~3行：

  File "err.py", line 11, in <module>
    main()
调用main()出错了，在代码文件err.py的第11行代码，但原因是第9行：

  File "err.py", line 9, in main
    bar('0')
调用bar('0')出错了，在代码文件err.py的第9行代码，但原因是第6行：

  File "err.py", line 6, in bar
    return foo(s) * 2
原因是return foo(s) * 2这个语句出错了，但这还不是最终原因，继续往下看：

  File "err.py", line 3, in foo
    return 10 / int(s)
原因是return 10 / int(s)这个语句出错了，这是错误产生的源头，因为下面打印了：

ZeroDivisionError: integer division or modulo by zero
根据错误类型ZeroDivisionError，我们判断，int(s)本身并没有出错，但是int(s)返回0，在计算10 / 0时出错，至此，找到错误源头。

 出错的时候，一定要分析错误的调用栈信息，才能定位错误的位置。

记录错误
如果不捕获错误，自然可以让Python解释器来打印出错误堆栈，但程序也被结束了。既然我们能捕获错误，就可以把错误堆栈打印出来，然后分析错误原因，同时，让程序继续执行下去。
Python内置的logging模块可以非常容易地记录错误信息：
# err_logging.py

import logging

def foo(s):
    return 10 / int(s)

def bar(s):
    return foo(s) * 2

def main():
    try:
        bar('0')
    except Exception as e:
        logging.exception(e)

main()
print('END')
同样是出错，但程序打印完错误信息后会继续执行，并正常退出：

$ python3 err_logging.py
ERROR:root:division by zero
Traceback (most recent call last):
  File "err_logging.py", line 13, in main
    bar('0')
  File "err_logging.py", line 9, in bar
    return foo(s) * 2
  File "err_logging.py", line 6, in foo
    return 10 / int(s)
ZeroDivisionError: division by zero
END
通过配置，logging还可以把错误记录到日志文件里，方便事后排查。


抛出错误
因为错误是class，捕获一个错误就是捕获到该class的一个实例。因此，错误并不是凭空产生的，而是有意创建并抛出的。Python的内置函数会抛出很多类型的错误，我们自己编写的函数也可以抛出错误。
如果要抛出错误，首先根据需要，可以定义一个错误的class，选择好继承关系，然后，用raise语句抛出一个错误的实例：
# err_raise.py
class FooError(ValueError):
    pass

def foo(s):
    n = int(s)
    if n==0:
        raise FooError('invalid value: %s' % s)
    return 10 / n

foo('0')
执行，可以最后跟踪到我们自己定义的错误：

$ python3 err_raise.py 
Traceback (most recent call last):
  File "err_throw.py", line 11, in <module>
    foo('0')
  File "err_throw.py", line 8, in foo
    raise FooError('invalid value: %s' % s)
__main__.FooError: invalid value: 0
只有在必要的时候才定义我们自己的错误类型。如果可以选择Python已有的内置的错误类型（比如ValueError，TypeError），尽量使用Python内置的错误类型。

最后，我们来看另一种错误处理的方式：

# err_reraise.py

def foo(s):
    n = int(s)
    if n==0:
        raise ValueError('invalid value: %s' % s)
    return 10 / n

def bar():
    try:
        foo('0')
    except ValueError as e:
        print('ValueError!')
        raise

bar()
在bar()函数中，我们明明已经捕获了错误，但是，打印一个ValueError!后，又把错误通过raise语句抛出去了，这不有病么？

其实这种错误处理方式不但没病，而且相当常见。捕获错误目的只是记录一下，便于后续追踪。但是，由于当前函数不知道应该怎么处理该错误，所以，最恰当的方式是继续往上抛，让顶层调用者去处理。好比一个员工处理不了一个问题时，就把问题抛给他的老板，如果他的老板也处理不了，就一直往上抛，最终会抛给CEO去处理。

raise语句如果不带参数，就会把当前错误原样抛出。此外，在except中raise一个Error，还可以把一种类型的错误转化成另一种类型：

try:
    10 / 0
except ZeroDivisionError:
    raise ValueError('input error!')
只要是合理的转换逻辑就可以，但是，决不应该把一个IOError转换成毫不相干的ValueError。
#-----------------------------------------------------------------------------------------
#调试
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431915578556ad30ab3933ae4e82a03ee2e9a4f70871000
用print()最大的坏处是将来还得删掉它，想想程序里到处都是print()，运行结果也会包含很多垃圾信息。所以，我们又有第二种方法。

断言
凡是用print()来辅助查看的地方，都可以用断言（assert）来替代：

def foo(s):
    n = int(s)
    assert n != 0, 'n is zero!'
    return 10 / n

def main():
    foo('0')
assert的意思是，表达式n != 0应该是True，否则，根据程序运行的逻辑，后面的代码肯定会出错。

如果断言失败，assert语句本身就会抛出AssertionError：
$ python err.py
Traceback (most recent call last):
  ...
AssertionError: n is zero!
程序中如果到处充斥着assert，和print()相比也好不到哪去。不过，启动Python解释器时可以用-O参数来关闭assert：

$ python -O err.py
Traceback (most recent call last):
  ...
ZeroDivisionError: division by zero
关闭后，你可以把所有的assert语句当成pass来看。

logging
把print()替换为logging是第3种方式，和assert比，logging不会抛出错误，而且可以输出到文件：
import logging

s = '0'
n = int(s)
logging.info('n = %d' % n)
print(10 / n)
logging.info()就可以输出一段文本。运行，发现除了ZeroDivisionError，没有任何信息。怎么回事？

别急，在import logging之后添加一行配置再试试：

import logging
logging.basicConfig(level=logging.INFO)
看到输出了：

$ python err.py
INFO:root:n = 0
Traceback (most recent call last):
  File "err.py", line 8, in <module>
    print(10 / n)
ZeroDivisionError: division by zero
这就是logging的好处，它允许你指定记录信息的级别，有debug，info，warning，error等几个级别，当我们指定level=INFO时，logging.debug就不起作用了。同理，指定level=WARNING后，debug和info就不起作用了。这样一来，你可以放心地输出不同级别的信息，也不用删除，最后统一控制输出哪个级别的信息。

logging的另一个好处是通过简单的配置，一条语句可以同时输出到不同的地方，比如console和文件。

#https://blog.csdn.net/z_johnny/article/details/50740528 logging讲解
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import logging
#等于或高于level级别的语句才会输出，默认只显示了大于等于WARNING级别
logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(filename)s [line:%(lineno)d] %(levelname)s %(message)s',
                    datefmt='%a %d %b %Y %H:%M:%S',
                    filename='test.log',
                    filemode='w')
                    
if __name__ == "__main__":
    logging.debug("debug") #从debug/info/warning/error/critical依次输出级别越来越高
    logging.info("info")
    logging.warning("warning")
    logging.error("error")
    logging.critical("critical")
输出：
Fri 30 Mar 2018 23:27:20 main.py [line:12] DEBUG debug
Fri 30 Mar 2018 23:27:20 main.py [line:13] INFO info
Fri 30 Mar 2018 23:27:20 main.py [line:14] WARNING warning
Fri 30 Mar 2018 23:27:20 main.py [line:15] ERROR error
Fri 30 Mar 2018 23:27:20 main.py [line:16] CRITICAL critical

可见在logging.basicConfig()函数中可通过具体参数来更改logging模块默认行为，可用参数有
filename：   用指定的文件名创建FiledHandler（后边会具体讲解handler的概念），这样日志会被存储在指定的文件中。
filemode：   文件打开方式，在指定了filename时使用这个参数，默认值为“a”还可指定为“w”。
format：      指定handler使用的日志显示格式。
datefmt：    指定日期时间格式。
level：        设置rootlogger（后边会讲解具体概念）的日志级别
stream：     用指定的stream创建StreamHandler。可以指定输出到sys.stderr,sys.stdout或者文件，默认为sys.stderr。
                  若同时列出了filename和stream两个参数，则stream参数会被忽略。

format参数中可能用到的格式化串：
%(name)s             Logger的名字
%(levelno)s          数字形式的日志级别
%(levelname)s     文本形式的日志级别
%(pathname)s     调用日志输出函数的模块的完整路径名，可能没有
%(filename)s        调用日志输出函数的模块的文件名
%(module)s          调用日志输出函数的模块名
%(funcName)s     调用日志输出函数的函数名
%(lineno)d           调用日志输出函数的语句所在的代码行
%(created)f          当前时间，用UNIX标准的表示时间的浮 点数表示
%(relativeCreated)d    输出日志信息时的，自Logger创建以 来的毫秒数
%(asctime)s                字符串形式的当前时间。默认格式是 “2003-07-08 16:49:45,896”。逗号后面的是毫秒
%(thread)d                 线程ID。可能没有
%(threadName)s        线程名。可能没有
%(process)d              进程ID。可能没有
%(message)s            用户输出的消息
#-----------------------------------------------------------------------------------------
#单元测试
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143191629979802b566644aa84656b50cd484ec4a7838000
编写单元测试时，我们需要编写一个测试类，从unittest.TestCase继承。

以test开头的方法就是测试方法，不以test开头的方法不被认为是测试方法，测试的时候不会被执行。

对每一类测试都需要编写一个test_xxx()方法。由于unittest.TestCase提供了很多内置的条件判断，我们只需要调用这些方法就可以断言输出是否是我们所期望的。最常用的断言就是assertEqual()：

self.assertEqual(abs(-1), 1) # 断言函数返回的结果与1相等
另一种重要的断言就是期待抛出指定类型的Error，比如通过d['empty']访问不存在的key时，断言会抛出KeyError：

with self.assertRaises(KeyError):
    value = d['empty']
而通过d.empty访问不存在的key时，我们期待抛出AttributeError：

with self.assertRaises(AttributeError):
    value = d.empty
    
    
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import unittest
from src.mydict import Dict


class TestDict(unittest.TestCase):
    def test_init(self):
        d = Dict(a=1, b='test')
        self.assertEqual(d.a, 1)
        self.assertEqual(d.b, 'test')
        self.assertTrue(isinstance(d, dict))

    def test_key(self):
        d = Dict()
        d['key'] = 'value'
        self.assertEqual(d.key, 'value')

    def test_attr(self):
        d = Dict()
        d.key = 'value'
        self.assertTrue('key' in d)
        self.assertEqual(d['key'], 'value')

    def test_keyerror(self):
        d = Dict()
        with self.assertRaises(KeyError):
            value = d['empty']

    def test_attrerror(self):
        d = Dict()
        with self.assertRaises(AttributeError):
            value = d.empty

if __name__ == "__main__":
    unittest.main()
    
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

' XXX module '
class Dict(dict):
    def __init__(self, **kw):
        super().__init__(**kw)

    def __getattr__(self, key):
        try:
            return self[key]
        except KeyError:
            raise AttributeError(r"'Dict' object has no attribute '%s'" % key)

    def __setattr__(self, key, value):
        self[key] = value

运行单元测试
一旦编写好单元测试，我们就可以运行单元测试。最简单的运行方式是在mydict_test.py的最后加上两行代码：
if __name__ == '__main__':
    unittest.main()
这样就可以把mydict_test.py当做正常的python脚本运行：

另一种方法是在命令行通过参数-m unittest直接运行单元测试：
E:\Program Files\JetBrains\PythonProject\Py3TestProject>python -m unittest src\main.py
.....
----------------------------------------------------------------------
Ran 5 tests in 0.001s

OK
这是推荐的做法，因为这样可以一次批量运行很多单元测试，并且，有很多工具可以自动来运行这些单元测试。

setUp与tearDown
可以在单元测试中编写两个特殊的setUp()和tearDown()方法。这两个方法会分别在每调用一个测试方法的前后分别被执行。
setUp()和tearDown()方法有什么用呢？设想你的测试需要启动一个数据库，这时，就可以在setUp()方法中连接数据库，在tearDown()方法中关闭数据库，这样，不必在每个测试方法中重复相同的代码：
class TestDict(unittest.TestCase):

    def setUp(self):
        print('setUp...')

    def tearDown(self):
        print('tearDown...')
可以再次运行测试看看每个测试方法调用前后是否会打印出setUp...和tearDown...。
#-----------------------------------------------------------------------------------------
#文件读写
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431917715991ef1ebc19d15a4afdace1169a464eecc2000

file-like Object

像open()函数返回的这种有个read()方法的对象，在Python中统称为file-like Object。除了file外，还可以是内存的字节流，网络流，自定义流等等。file-like Object不要求从特定类继承，只要写个read()方法就行。

StringIO就是在内存中创建的file-like Object，常用作临时缓冲。

二进制文件

前面讲的默认都是读取文本文件，并且是UTF-8编码的文本文件。要读取二进制文件，比如图片、视频等等，用'rb'模式打开文件即可：

>>> f = open('/Users/michael/test.jpg', 'rb')
>>> f.read()
b'\xff\xd8\xff\xe1\x00\x18Exif\x00\x00...' # 十六进制表示的字节


字符编码

要读取非UTF-8编码的文本文件，需要给open()函数传入encoding参数，例如，读取GBK编码的文件：

>>> f = open('/Users/michael/gbk.txt', 'r', encoding='gbk')
>>> f.read()
'测试'
遇到有些编码不规范的文件，你可能会遇到UnicodeDecodeError，因为在文本文件中可能夹杂了一些非法编码的字符。遇到这种情况，open()函数还接收一个errors参数，表示如果遇到编码错误后如何处理。最简单的方式是直接忽略：

>>> f = open('/Users/michael/gbk.txt', 'r', encoding='gbk', errors='ignore')

小结
在Python中，文件读写是通过open()函数打开的文件对象完成的。使用with语句操作文件IO是个好习惯。
#-----------------------------------------------------------------------------------------
#StringIO
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431918785710e86a1a120ce04925bae155012c7fc71e000

很多时候，数据读写不一定是文件，也可以在内存中读写。

StringIO顾名思义就是在内存中读写str。

要把str写入StringIO，我们需要先创建一个StringIO，然后，像文件一样写入即可：

>>> from io import StringIO
>>> f = StringIO()
>>> f.write('hello')
5
>>> f.write(' ')
1
>>> f.write('world!')
6
>>> print(f.getvalue())
hello world!
getvalue()方法用于获得写入后的str。

要读取StringIO，可以用一个str初始化StringIO，然后，像读文件一样读取：

>>> from io import StringIO
>>> f = StringIO('Hello!\nHi!\nGoodbye!')
>>> while True:
...     s = f.readline()
...     if s == '':
...         break
...     print(s.strip())
...
Hello!
Hi!
Goodbye!
BytesIO

StringIO操作的只能是str，如果要操作二进制数据，就需要使用BytesIO。

BytesIO实现了在内存中读写bytes，我们创建一个BytesIO，然后写入一些bytes：

>>> from io import BytesIO
>>> f = BytesIO()
>>> f.write('中文'.encode('utf-8'))
6
>>> print(f.getvalue())
b'\xe4\xb8\xad\xe6\x96\x87'
请注意，写入的不是str，而是经过UTF-8编码的bytes。

和StringIO类似，可以用一个bytes初始化BytesIO，然后，像读文件一样读取：

>>> from io import BytesIO
>>> f = BytesIO(b'\xe4\xb8\xad\xe6\x96\x87')
>>> f.read()
b'\xe4\xb8\xad\xe6\x96\x87'
小结

StringIO和BytesIO是在内存中操作str和bytes的方法，使得和读写文件具有一致的接口。
#-----------------------------------------------------------------------------------------
#操作文件和目录
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431925324119bac1bc7979664b4fa9843c0e5fcdcf1e000

import os

if __name__ == "__main__":
    print(os.name)
    #print(os.uname())#AttributeError: module 'os' has no attribute 'uname'
    print(os.environ)
    print(os.environ.get("JAVA_HOME"))
输出：
nt
environ({'ALLUSERSPROFILE': 'C:\\ProgramData', 'APPDATA': 'C:\\Users\\Ben\\AppData\\Roaming', 'CATALINA_HOME': 'e:\\Program Files\\Apache Software Foundation\\Tomcat 9.0', 'CLASSPATH': '.;e:\\Program Files\\Java\\jdk1.8.0_112\\lib;e:\\Program Files\\Java\\jdk1.8.0_112\\lib\\tools.jar', 'COMMANDER_DRIVE': 'E:', 'COMMANDER_EXE': 'E:\\Program Files\\TotalCMD64\\Totalcmd64.exe', 'COMMANDER_INI': 'E:\\Program Files\\TotalCMD64\\wincmd.ini', 'COMMANDER_PATH': 'E:\\Program Files\\TotalCMD64', 'COMMONPROGRAMFILES': 'C:\\Program Files\\Common Files', 'COMMONPROGRAMFILES(X86)': 'C:\\Program Files (x86)\\Common Files', 'COMMONPROGRAMW6432': 'C:\\Program Files\\Common Files', 'COMPUTERNAME': 'DESKTOP-18O9S0P', 'COMSPEC': 'C:\\WINDOWS\\system32\\cmd.exe', 'FPS_BROWSER_APP_PROFILE_STRING': 'Internet Explorer', 'FPS_BROWSER_USER_PROFILE_STRING': 'Default', 'HOMEDRIVE': 'C:', 'HOMEPATH': '\\Users\\Ben', 'JAVA_HOME': 'e:\\Program Files\\Java\\jdk1.8.0_112', 'LNKENV': 'C:\\Program Files (x86)\\Internet Explorer\\IEXPLORE.EXE', 'LOCALAPPDATA': 'C:\\Users\\Ben\\AppData\\Local', 'LOGONSERVER': '\\\\DESKTOP-18O9S0P', 'MOZ_PLUGIN_PATH': 'C:\\Program Files (x86)\\Foxit Software\\Foxit Reader Plus\\plugins\\', 'NUMBER_OF_PROCESSORS': '8', 'ONEDRIVE': 'C:\\Users\\Ben\\OneDrive', 'OS': 'Windows_NT', 'PATH': 'D:\\Anaconda3\\Library\\bin;C:\\Program Files (x86)\\Intel\\iCLS Client\\;C:\\Program Files\\Intel\\iCLS Client\\;C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\;C:\\Program Files (x86)\\Intel\\Intel(R) Management Engine Components\\DAL;C:\\Program Files\\Intel\\Intel(R) Management Engine Components\\DAL;C:\\Program Files (x86)\\Intel\\Intel(R) Management Engine Components\\IPT;C:\\Program Files\\Intel\\Intel(R) Management Engine Components\\IPT;C:\\Program Files (x86)\\NVIDIA Corporation\\PhysX\\Common;e:\\Program Files\\QuickStart;C:\\Program Files\\Microsoft SQL Server\\110\\Tools\\Binn\\;C:\\WINDOWS\\system32;C:\\WINDOWS;C:\\WINDOWS\\System32\\Wbem;C:\\WINDOWS\\System32\\WindowsPowerShell\\v1.0\\;e:\\Program Files\\Java\\jdk1.8.0_112\\bin;e:\\Program Files\\Java\\jdk1.8.0_112\\jre\\bin;C:\\Program Files (x86)\\Windows Kits\\8.1\\Windows Performance Toolkit\\;C:\\Program Files\\Microsoft SQL Server\\120\\Tools\\Binn\\;d:\\Anaconda3\\;C:\\Users\\Ben\\AppData\\Local\\Microsoft\\WindowsApps;', 'PATHEXT': '.COM;.EXE;.BAT;.CMD;.VBS;.VBE;.JS;.JSE;.WSF;.WSH;.MSC', 'PROCESSOR_ARCHITECTURE': 'AMD64', 'PROCESSOR_IDENTIFIER': 'Intel64 Family 6 Model 94 Stepping 3, GenuineIntel', 'PROCESSOR_LEVEL': '6', 'PROCESSOR_REVISION': '5e03', 'PROGRAMDATA': 'C:\\ProgramData', 'PROGRAMFILES': 'C:\\Program Files', 'PROGRAMFILES(X86)': 'C:\\Program Files (x86)', 'PROGRAMW6432': 'C:\\Program Files', 'PSMODULEPATH': 'C:\\WINDOWS\\system32\\WindowsPowerShell\\v1.0\\Modules\\;C:\\Program Files\\Intel\\', 'PUBLIC': 'C:\\Users\\Public', 'PYCHARM_HOSTED': '1', 'PYTHONIOENCODING': 'UTF-8', 'PYTHONPATH': 'E:\\Program Files\\JetBrains\\PythonProject\\Py3TestProject', 'PYTHONUNBUFFERED': '1', 'SESSIONNAME': 'Console', 'SYSTEMDRIVE': 'C:', 'SYSTEMROOT': 'C:\\WINDOWS', 'TEMP': 'C:\\Users\\Ben\\AppData\\Local\\Temp', 'TMP': 'C:\\Users\\Ben\\AppData\\Local\\Temp', 'USERDOMAIN': 'DESKTOP-18O9S0P', 'USERDOMAIN_ROAMINGPROFILE': 'DESKTOP-18O9S0P', 'USERNAME': 'Ben', 'USERPROFILE': 'C:\\Users\\Ben', 'VS120COMNTOOLS': 'D:\\Program Files\\Microsoft Visual Studio 12.0\\Common7\\Tools\\', 'VS140COMNTOOLS': 'D:\\Program Files\\Microsoft Visual Studio 14.0\\Common7\\Tools\\', 'WINDIR': 'C:\\WINDOWS'})
e:\Program Files\Java\jdk1.8.0_112

操作文件和目录
操作文件和目录的函数一部分放在os模块中，一部分放在os.path模块中，这一点要注意一下。查看、创建和删除目录可以这么调用：

# 查看当前目录的绝对路径:
>>> os.path.abspath('.')
'/Users/michael'
# 在某个目录下创建一个新目录，首先把新目录的完整路径表示出来:
>>> os.path.join('/Users/michael', 'testdir')
'/Users/michael/testdir'
# 然后创建一个目录:
>>> os.mkdir('/Users/michael/testdir')
# 删掉一个目录:
>>> os.rmdir('/Users/michael/testdir')
把两个路径合成一个时，不要直接拼字符串，而要通过os.path.join()函数，这样可以正确处理不同操作系统的路径分隔符。在Linux/Unix/Mac下，os.path.join()返回这样的字符串：

part-1/part-2
而Windows下会返回这样的字符串：

part-1\part-2
同样的道理，要拆分路径时，也不要直接去拆字符串，而要通过os.path.split()函数，这样可以把一个路径拆分为两部分，后一部分总是最后级别的目录或文件名：

>>> os.path.split('/Users/michael/testdir/file.txt')
('/Users/michael/testdir', 'file.txt')
os.path.splitext()可以直接让你得到文件扩展名，很多时候非常方便：

>>> os.path.splitext('/path/to/file.txt')
('/path/to/file', '.txt')

import os

if __name__ == "__main__":
    txt = "abc/def/opq.txt"
    print(os.path.split(txt))
    print(os.path.splitext(txt))
    print(os.path.basename(txt))
    print(os.path.dirname(txt))
输出：
('abc/def', 'opq.txt')
('abc/def/opq', '.txt')
opq.txt
abc/def

这些合并、拆分路径的函数并不要求目录和文件要真实存在，它们只对字符串进行操作。
文件操作使用下面的函数。假定当前目录下有一个test.txt文件：
# 对文件重命名:
>>> os.rename('test.txt', 'test.py')
# 删掉文件:
>>> os.remove('test.py')
但是复制文件的函数居然在os模块中不存在！原因是复制文件并非由操作系统提供的系统调用。理论上讲，我们通过上一节的读写文件可以完成文件复制，只不过要多写很多代码。

幸运的是shutil模块提供了copyfile()的函数，你还可以在shutil模块中找到很多实用函数，它们可以看做是os模块的补充。

最后看看如何利用Python的特性来过滤文件。比如我们要列出当前目录下的所有目录，只需要一行代码：

>>> [x for x in os.listdir('.') if os.path.isdir(x)]
['.lein', '.local', '.m2', '.npm', '.ssh', '.Trash', '.vim', 'Applications', 'Desktop', ...]

要列出所有的.py文件，也只需一行代码：
>>> [x for x in os.listdir('.') if os.path.isfile(x) and os.path.splitext(x)[1]=='.py']
['apis.py', 'config.py', 'models.py', 'pymonitor.py', 'test_db.py', 'urls.py', 'wsgiapp.py']
#-----------------------------------------------------------------------------------------
#序列化
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143192607210600a668b5112e4a979dd20e4661cc9c97000
在程序运行的过程中，所有的变量都是在内存中，比如，定义一个dict：

d = dict(name='Bob', age=20, score=88)
可以随时修改变量，比如把name改成'Bill'，但是一旦程序结束，变量所占用的内存就被操作系统全部回收。如果没有把修改后的'Bill'存储到磁盘上，下次重新运行程序，变量又被初始化为'Bob'。

我们把变量从内存中变成可存储或传输的过程称之为序列化，在Python中叫pickling，在其他语言中也被称之为serialization，marshalling，flattening等等，都是一个意思。

序列化之后，就可以把序列化后的内容写入磁盘，或者通过网络传输到别的机器上。

反过来，把变量内容从序列化的对象重新读到内存里称之为反序列化，即unpickling。

Python提供了pickle模块来实现序列化。

首先，我们尝试把一个对象序列化并写入文件：

>>> import pickle
>>> d = dict(name='Bob', age=20, score=88)
>>> pickle.dumps(d)
b'\x80\x03}q\x00(X\x03\x00\x00\x00ageq\x01K\x14X\x05\x00\x00\x00scoreq\x02KXX\x04\x00\x00\x00nameq\x03X\x03\x00\x00\x00Bobq\x04u.'
pickle.dumps()方法把任意对象序列化成一个bytes，然后，就可以把这个bytes写入文件。或者用另一个方法pickle.dump()直接把对象序列化后写入一个file-like Object：

>>> f = open('dump.txt', 'wb')
>>> pickle.dump(d, f)
>>> f.close()
看看写入的dump.txt文件，一堆乱七八糟的内容，这些都是Python保存的对象内部信息。

当我们要把对象从磁盘读到内存时，可以先把内容读到一个bytes，然后用pickle.loads()方法反序列化出对象，也可以直接用pickle.load()方法从一个file-like Object中直接反序列化出对象。我们打开另一个Python命令行来反序列化刚才保存的对象：

>>> f = open('dump.txt', 'rb')
>>> d = pickle.load(f)
>>> f.close()
>>> d
{'age': 20, 'score': 88, 'name': 'Bob'}
变量的内容又回来了！

当然，这个变量和原来的变量是完全不相干的对象，它们只是内容相同而已。

Pickle的问题和所有其他编程语言特有的序列化问题一样，就是它只能用于Python，并且可能不同版本的Python彼此都不兼容，因此，只能用Pickle保存那些不重要的数据，不能成功地反序列化也没关系。

JSON

如果我们要在不同的编程语言之间传递对象，就必须把对象序列化为标准格式，比如XML，但更好的方法是序列化为JSON，因为JSON表示出来就是一个字符串，可以被所有语言读取，也可以方便地存储到磁盘或者通过网络传输。JSON不仅是标准格式，并且比XML更快，而且可以直接在Web页面中读取，非常方便。
JSON表示的对象就是标准的JavaScript语言的对象，JSON和Python内置的数据类型对应如下：
JSON类型	Python类型
{}	dict
[]	list
"string"	str
1234.56	int或float
true/false	True/False
null	None
Python内置的json模块提供了非常完善的Python对象到JSON格式的转换。我们先看看如何把Python对象变成一个JSON：

>>> import json
>>> d = dict(name='Bob', age=20, score=88)
>>> json.dumps(d)
'{"age": 20, "score": 88, "name": "Bob"}'
dumps()方法返回一个str，内容就是标准的JSON。类似的，dump()方法可以直接把JSON写入一个file-like Object。

要把JSON反序列化为Python对象，用loads()或者对应的load()方法，前者把JSON的字符串反序列化，后者从file-like Object中读取字符串并反序列化：

>>> json_str = '{"age": 20, "score": 88, "name": "Bob"}'
>>> json.loads(json_str)
{'age': 20, 'score': 88, 'name': 'Bob'}
由于JSON标准规定JSON编码是UTF-8，所以我们总是能正确地在Python的str与JSON的字符串之间转换。

JSON进阶

Python的dict对象可以直接序列化为JSON的{}，不过，很多时候，我们更喜欢用class表示对象，比如定义Student类，然后序列化：

import json

class Student(object):
    def __init__(self, name, age, score):
        self.name = name
        self.age = age
        self.score = score

s = Student('Bob', 20, 88)
print(json.dumps(s))
运行代码，毫不留情地得到一个TypeError：

Traceback (most recent call last):
  ...
TypeError: <__main__.Student object at 0x10603cc50> is not JSON serializable
错误的原因是Student对象不是一个可序列化为JSON的对象。

如果连class的实例对象都无法序列化为JSON，这肯定不合理！

别急，我们仔细看看dumps()方法的参数列表，可以发现，除了第一个必须的obj参数外，dumps()方法还提供了一大堆的可选参数：

https://docs.python.org/3/library/json.html#json.dumps

这些可选参数就是让我们来定制JSON序列化。前面的代码之所以无法把Student类实例序列化为JSON，是因为默认情况下，dumps()方法不知道如何将Student实例变为一个JSON的{}对象。

可选参数default就是把任意一个对象变成一个可序列为JSON的对象，我们只需要为Student专门写一个转换函数，再把函数传进去即可：

def student2dict(std):
    return {
        'name': std.name,
        'age': std.age,
        'score': std.score
    }
这样，Student实例首先被student2dict()函数转换成dict，然后再被顺利序列化为JSON：

>>> print(json.dumps(s, default=student2dict))
{"age": 20, "name": "Bob", "score": 88}
不过，下次如果遇到一个Teacher类的实例，照样无法序列化为JSON。我们可以偷个懒，把任意class的实例变为dict：

print(json.dumps(s, default=lambda obj: obj.__dict__))
因为通常class的实例都有一个__dict__属性，它就是一个dict，用来存储实例变量。也有少数例外，比如定义了__slots__的class。

同样的道理，如果我们要把JSON反序列化为一个Student对象实例，loads()方法首先转换出一个dict对象，然后，我们传入的object_hook函数负责把dict转换为Student实例：

def dict2student(d):
    return Student(d['name'], d['age'], d['score'])
运行结果如下：

>>> json_str = '{"age": 20, "score": 88, "name": "Bob"}'
>>> print(json.loads(json_str, object_hook=dict2student))
<__main__.Student object at 0x10cd3c190>
打印出的是反序列化的Student实例对象。


if __name__ == "__main__":
    obj = dict(name='小明', age=20)
    s = json.dumps(obj, ensure_ascii=True)
    print(s)
#-----------------------------------------------------------------------------------------
#多进程
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431927781401bb47ccf187b24c3b955157bb12c5882d000

要让Python程序实现多进程（multiprocessing），我们先了解操作系统的相关知识。

Unix/Linux操作系统提供了一个fork()系统调用，它非常特殊。普通的函数调用，调用一次，返回一次，但是fork()调用一次，返回两次，因为操作系统自动把当前进程（称为父进程）复制了一份（称为子进程），然后，分别在父进程和子进程内返回。

子进程永远返回0，而父进程返回子进程的ID。这样做的理由是，一个父进程可以fork出很多子进程，所以，父进程要记下每个子进程的ID，而子进程只需要调用getppid()就可以拿到父进程的ID。

Python的os模块封装了常见的系统调用，其中就包括fork，可以在Python程序中轻松创建子进程：

import os

print('Process (%s) start...' % os.getpid())
# Only works on Unix/Linux/Mac:
pid = os.fork()
if pid == 0:
    print('I am child process (%s) and my parent is %s.' % (os.getpid(), os.getppid()))
else:
    print('I (%s) just created a child process (%s).' % (os.getpid(), pid))
运行结果如下：

Process (876) start...
I (876) just created a child process (877).
I am child process (877) and my parent is 876.
由于Windows没有fork调用，上面的代码在Windows上无法运行。由于Mac系统是基于BSD（Unix的一种）内核，所以，在Mac下运行是没有问题的，推荐大家用Mac学Python！

有了fork调用，一个进程在接到新任务时就可以复制出一个子进程来处理新任务，常见的Apache服务器就是由父进程监听端口，每当有新的http请求时，就fork出子进程来处理新的http请求。

multiprocessing
如果你打算编写多进程的服务程序，Unix/Linux无疑是正确的选择。由于Windows没有fork调用，难道在Windows上无法用Python编写多进程的程序？
由于Python是跨平台的，自然也应该提供一个跨平台的多进程支持。multiprocessing模块就是跨平台版本的多进程模块。

multiprocessing模块提供了一个Process类来代表一个进程对象，下面的例子演示了启动一个子进程并等待其结束：
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import os
from multiprocessing import Process

class RunProc(object):
    def __call__(self, name):
        print('Run child process %s (%s)...' % (name, os.getpid()))

def run_proc(name):
    print('Run child process %s (%s)...' % (name, os.getpid()))

if __name__=='__main__':
    print('Parent process %s.' % os.getpid())
    p = Process(target=run_proc, args=('test',)) #p = Process(target=RunProc(), args=('test',)) 写一个函数对象
    print('Child process will start.')
    p.start()
    p.join() #如果不加这条语句，则子进程和下面的end语句不一定谁先完成
    print('Child process end.')
输出：
Parent process 8196.
Child process will start.
Run child process test (7296)...
Child process end.

创建子进程时，只需要传入一个执行函数和函数的参数，创建一个Process实例，用start()方法启动，这样创建进程比fork()还要简单。
join()方法可以等待子进程结束后再继续往下运行，通常用于进程间的同步。

Pool
如果要启动大量的子进程，可以用进程池的方式批量创建子进程：
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from multiprocessing import Pool
import os, time, random

def long_time_task(name):
    print('Run task %s (%s)...' % (name, os.getpid()))
    start = time.time()
    time.sleep(4) #random.random() * 3
    end = time.time()
    print('Task %s runs %0.2f seconds.' % (name, (end - start)))

if __name__=='__main__':
    print('Parent process %s.' % os.getpid())
    poolNum = 20
    p = Pool(poolNum) #进程池数，可以同时最多运行多少个进程
    for i in range(poolNum):
        p.apply_async(long_time_task, args=(i,))
    print('Waiting for all subprocesses done...')
    p.close()
    p.join()
    print('All subprocesses done.')
输出：
Parent process 8120.
Waiting for all subprocesses done...
Run task 0 (9104)...
Run task 1 (4744)...
Run task 2 (5036)...
Run task 3 (11012)...
Task 0 runs 4.00 seconds.
Task 2 runs 4.00 seconds.
Task 3 runs 4.00 seconds.
Task 1 runs 4.00 seconds.
All subprocesses done.

代码解读：

对Pool对象调用join()方法会等待所有子进程执行完毕，调用join()之前必须先调用close()，调用close()之后就不能继续添加新的Process了。

请注意输出的结果，task 0，1，2，3是立刻执行的，而task 4要等待前面某个task完成后才执行，这是因为Pool的默认大小在我的电脑上是4，因此，最多同时执行4个进程。这是Pool有意设计的限制，并不是操作系统的限制。如果改成：

p = Pool(5)
就可以同时跑5个进程。

由于Pool的默认大小是CPU的核数，如果你不幸拥有8核CPU，你要提交至少9个子进程才能看到上面的等待效果。（这里不是特别懂，只要待运行的进程数大于Pool的数量，总能看到等待的效果）


子进程
很多时候，子进程并不是自身，而是一个外部进程。我们创建了子进程后，还需要控制子进程的输入和输出。
subprocess模块可以让我们非常方便地启动一个子进程，然后控制其输入和输出。
下面的例子演示了如何在Python代码中运行命令nslookup www.python.org，这和命令行直接运行的效果是一样的：
import subprocess
if __name__=='__main__':
    print('$ nslookup www.python.org')
    r = subprocess.call(['nslookup', 'www.python.org'])
    print('Exit code:', r)
输出：
$ nslookup www.python.org
Server:        192.168.19.4
Address:    192.168.19.4#53

Non-authoritative answer:
www.python.org    canonical name = python.map.fastly.net.
Name:    python.map.fastly.net
Address: 199.27.79.223

Exit code: 0

如果子进程还需要输入，则可以通过communicate()方法输入：
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import subprocess
if __name__=='__main__':
    print('$ nslookup')
    p = subprocess.Popen(['nslookup'], stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    output, err = p.communicate(b'set q=mx\npython.org\nexit\n')
    print(output.decode('gbk')) #utf-8
    print('Exit code:', p.returncode)
上面的代码相当于在命令行执行命令nslookup，然后手动输入：
set q=mx
python.org
exit

输出：
$ nslookup
默认服务器:  dnspai-public-dns.dnspai.com
Address:  140.207.198.6

> > 服务器:  dnspai-public-dns.dnspai.com
Address:  140.207.198.6

python.org	MX preference = 50, mail exchanger = mail.python.org
> 
Exit code: 0

进程间通信
Process之间肯定是需要通信的，操作系统提供了很多机制来实现进程间的通信。Python的multiprocessing模块包装了底层的机制，提供了Queue、Pipes等多种方式来交换数据。
我们以Queue为例，在父进程中创建两个子进程，一个往Queue里写数据，一个从Queue里读数据：
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from multiprocessing import Process, Queue
import os, time, random

# 写数据进程执行的代码:
def write(q):
    print('Process to write: %s' % os.getpid())
    for value in ['A', 'B', 'C']:
        print('Put %s to queue...' % value)
        q.put(value)
        time.sleep(3)#time.sleep(random.random())

# 读数据进程执行的代码:
def read(q):
    print('Process to read: %s' % os.getpid())
    while True:
        value = q.get(True)
        print('Get %s from queue.' % value)

if __name__=='__main__':
    # 父进程创建Queue，并传给各个子进程：
    q = Queue()
    pw = Process(target=write, args=(q,))
    pr = Process(target=read, args=(q,))
    # 启动子进程pw，写入:
    pw.start()
    # 启动子进程pr，读取:
    pr.start()
    # 等待pw结束:
    pw.join()
    # pr进程里是死循环，无法等待其结束，只能强行终止:
    pr.terminate()
输出：
Process to write: 12176
Put A to queue...
Process to read: 5216
Get A from queue.
Put B to queue...
Get B from queue.
Put C to queue...
Get C from queue.

在Unix/Linux下，multiprocessing模块封装了fork()调用，使我们不需要关注fork()的细节。由于Windows没有fork调用，因此，multiprocessing需要“模拟”出fork的效果，父进程所有Python对象都必须通过pickle序列化再传到子进程去，所有，如果multiprocessing在Windows下调用失败了，要先考虑是不是pickle失败了。

小结
在Unix/Linux下，可以使用fork()调用实现多进程。
要实现跨平台的多进程，可以使用multiprocessing模块。
进程间通信是通过Queue、Pipes等实现的。
#-----------------------------------------------------------------------------------------
#多线程
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143192823818768cd506abbc94eb5916192364506fa5d000
多任务可以由多进程完成，也可以由一个进程内的多线程完成。

我们前面提到了进程是由若干线程组成的，一个进程至少有一个线程。

由于线程是操作系统直接支持的执行单元，因此，高级语言通常都内置多线程的支持，Python也不例外，并且，Python的线程是真正的Posix Thread，而不是模拟出来的线程。

Python的标准库提供了两个模块：_thread和threading，_thread是低级模块，threading是高级模块，对_thread进行了封装。绝大多数情况下，我们只需要使用threading这个高级模块。

启动一个线程就是把一个函数传入并创建Thread实例，然后调用start()开始执行：

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import time, threading

# 新线程执行的代码:
def loop():
    print('thread %s is running...' % threading.current_thread().name)
    n = 0
    while n < 5:
        n = n + 1
        print('thread %s >>> %s' % (threading.current_thread().name, n))
        time.sleep(1)
    print('thread %s ended.' % threading.current_thread().name)

if __name__=='__main__':
    print('thread %s is running...' % threading.current_thread().name)
    t = threading.Thread(target=loop, name='LoopThread')
    t.start()
    t.join()
    print('thread %s ended.' % threading.current_thread().name)
输出：
thread MainThread is running...
thread LoopThread is running...
thread LoopThread >>> 1
thread LoopThread >>> 2
thread LoopThread >>> 3
thread LoopThread >>> 4
thread LoopThread >>> 5
thread LoopThread ended.
thread MainThread ended.

#如果不加name参数，则线程就按顺序命名，无论之前的线程是否结束
t = threading.Thread(target=loop)
t2 = threading.Thread(target=loop)
t.start()
time.sleep(10)
t2.start()
t.join()
t2.join()
输出：
thread MainThread is running...
thread Thread-1 is running...
thread Thread-1 >>> 1
thread Thread-1 >>> 2
thread Thread-1 >>> 3
thread Thread-1 >>> 4
thread Thread-1 >>> 5
thread Thread-1 ended.
thread Thread-2 is running...
thread Thread-2 >>> 1
thread Thread-2 >>> 2
thread Thread-2 >>> 3
thread Thread-2 >>> 4
thread Thread-2 >>> 5
thread Thread-2 ended.
thread MainThread ended.

由于任何进程默认就会启动一个线程，我们把该线程称为主线程，主线程又可以启动新的线程，Python的threading模块有个current_thread()函数，它永远返回当前线程的实例。主线程实例的名字叫MainThread，子线程的名字在创建时指定，我们用LoopThread命名子线程。名字仅仅在打印时用来显示，完全没有其他意义，如果不起名字Python就自动给线程命名为Thread-1，Thread-2……

Lock
多线程和多进程最大的不同在于，多进程中，同一个变量，各自有一份拷贝存在于每个进程中，互不影响，而多线程中，所有变量都由所有线程共享，所以，任何一个变量都可以被任何一个线程修改，因此，线程之间共享数据最大的危险在于多个线程同时改一个变量，把内容给改乱了。

来看看多个线程同时操作一个变量怎么把内容给改乱了：
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import threading

balance = 0

def change_it(n):
    # 先存后取，结果应该为0:
    global balance
    balance = balance + n
    balance = balance - n

def run_thread(n):
    for i in range(100000):
        change_it(n)

if __name__=='__main__':
    t1 = threading.Thread(target=run_thread, args=(5,))
    t2 = threading.Thread(target=run_thread, args=(8,))
    t1.start()
    t2.start()
    t1.join()
    t2.join()
    print(balance)
输出：
有时候为0，有时为5

究其原因，是因为修改balance需要多条语句，而执行这几条语句时，线程可能中断，从而导致多个线程把同一个对象的内容改乱了。

两个线程同时一存一取，就可能导致余额不对，你肯定不希望你的银行存款莫名其妙地变成了负数，所以，我们必须确保一个线程在修改balance的时候，别的线程一定不能改。

如果我们要确保balance计算正确，就要给change_it()上一把锁，当某个线程开始执行change_it()时，我们说，该线程因为获得了锁，因此其他线程不能同时执行change_it()，只能等待，直到锁被释放后，获得该锁以后才能改。由于锁只有一个，无论多少线程，同一时刻最多只有一个线程持有该锁，所以，不会造成修改的冲突。创建一个锁就是通过threading.Lock()来实现：
balance = 0
lock = threading.Lock()

def run_thread(n):
    for i in range(100000):
        # 先要获取锁:
        lock.acquire()
        try:
            # 放心地改吧:
            change_it(n)
        finally:
            # 改完了一定要释放锁:
            lock.release()
当多个线程同时执行lock.acquire()时，只有一个线程能成功地获取锁，然后继续执行代码，其他线程就继续等待直到获得锁为止。
获得锁的线程用完后一定要释放锁，否则那些苦苦等待锁的线程将永远等待下去，成为死线程。所以我们用try...finally来确保锁一定会被释放。
锁的好处就是确保了某段关键代码只能由一个线程从头到尾完整地执行，坏处当然也很多，首先是阻止了多线程并发执行，包含锁的某段代码实际上只能以单线程模式执行，效率就大大地下降了。其次，由于可以存在多个锁，不同的线程持有不同的锁，并试图获取对方持有的锁时，可能会造成死锁，导致多个线程全部挂起，既不能执行，也无法结束，只能靠操作系统强制终止。
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import threading, multiprocessing

def loop():
    x = 0
    while True:
        x = x ^ 1

if __name__=='__main__':
    cpu_count = multiprocessing.cpu_count()
    for i in range(cpu_count):
        t = threading.Thread(target=loop)
        t.start()
电脑是4核8线程，只看到了两个核占用率增加

启动与CPU核心数量相同的N个线程，在4核CPU上可以监控到CPU占用率仅有102%，也就是仅使用了一核。
但是用C、C++或Java来改写相同的死循环，直接可以把全部核心跑满，4核就跑到400%，8核就跑到800%，为什么Python不行呢？
因为Python的线程虽然是真正的线程，但解释器执行代码时，有一个GIL锁：Global Interpreter Lock，任何Python线程执行前，必须先获得GIL锁，然后，每执行100条字节码，解释器就自动释放GIL锁，让别的线程有机会执行。这个GIL全局锁实际上把所有线程的执行代码都给上了锁，所以，多线程在Python中只能交替执行，即使100个线程跑在100核CPU上，也只能用到1个核。

GIL是Python解释器设计的历史遗留问题，通常我们用的解释器是官方实现的CPython，要真正利用多核，除非重写一个不带GIL的解释器。

所以，在Python中，可以使用多线程，但不要指望能有效利用多核。如果一定要通过多线程利用多核，那只能通过C扩展来实现，不过这样就失去了Python简单易用的特点。

不过，也不用过于担心，Python虽然不能利用多线程实现多核任务，但可以通过多进程实现多核任务。多个Python进程有各自独立的GIL锁，互不影响。
#-----------------------------------------------------------------------------------------
#ThreadLocal
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431928972981094a382e5584413fa040b46d46cce48e000

在多线程环境下，每个线程都有自己的数据。一个线程使用自己的局部变量比使用全局变量好，因为局部变量只有线程自己能看见，不会影响其他线程，而全局变量的修改必须加锁。

但是局部变量也有问题，就是在函数调用的时候，传递起来很麻烦：

def process_student(name):
    std = Student(name)
    # std是局部变量，但是每个函数都要用它，因此必须传进去：
    do_task_1(std)
    do_task_2(std)

def do_task_1(std):
    do_subtask_1(std)
    do_subtask_2(std)

def do_task_2(std):
    do_subtask_2(std)
    do_subtask_2(std)
每个函数一层一层调用都这么传参数那还得了？用全局变量？也不行，因为每个线程处理不同的Student对象，不能共享。

如果用一个全局dict存放所有的Student对象，然后以thread自身作为key获得线程对应的Student对象如何？

global_dict = {}

def std_thread(name):
    std = Student(name)
    # 把std放到全局变量global_dict中：
    global_dict[threading.current_thread()] = std
    do_task_1()
    do_task_2()

def do_task_1():
    # 不传入std，而是根据当前线程查找：
    std = global_dict[threading.current_thread()]
    ...

def do_task_2():
    # 任何函数都可以查找出当前线程的std变量：
    std = global_dict[threading.current_thread()]
    ...
这种方式理论上是可行的，它最大的优点是消除了std对象在每层函数中的传递问题，但是，每个函数获取std的代码有点丑。

有没有更简单的方式？

ThreadLocal应运而生，不用查找dict，ThreadLocal帮你自动做这件事：
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import threading

# 创建全局ThreadLocal对象:
local_school = threading.local()

def process_student():
    # 获取当前线程关联的student:
    std = local_school.student
    print('Hello, %s (in %s)' % (std, threading.current_thread().name))


def process_thread(name):
    # 绑定ThreadLocal的student:
    local_school.student = name
    process_student()

if __name__=='__main__':
    t1 = threading.Thread(target=process_thread, args=('Alice',), name='Thread-A')
    t2 = threading.Thread(target=process_thread, args=('Bob',), name='Thread-B')
    t1.start()
    t2.start()
    t1.join()
    t2.join()
输出：
Hello, Alice (in Thread-A)
Hello, Bob (in Thread-B)

全局变量local_school就是一个ThreadLocal对象，每个Thread对它都可以读写student属性，但互不影响。你可以把local_school看成全局变量，但每个属性如local_school.student都是线程的局部变量，可以任意读写而互不干扰，也不用管理锁的问题，ThreadLocal内部会处理。

可以理解为全局变量local_school是一个dict，不但可以用local_school.student，还可以绑定其他变量，如local_school.teacher等等。

ThreadLocal最常用的地方就是为每个线程绑定一个数据库连接，HTTP请求，用户身份信息等，这样一个线程的所有调用到的处理函数都可以非常方便地访问这些资源。

小结

一个ThreadLocal变量虽然是全局变量，但每个线程都只能读写自己线程的独立副本，互不干扰。ThreadLocal解决了参数在一个线程中各个函数之间互相传递的问题。
#-----------------------------------------------------------------------------------------
#进程 vs. 线程
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014319292979766bd3285c9d6b4942a8ea9b4e9cfb48d8000
我们介绍了多进程和多线程，这是实现多任务最常用的两种方式。现在，我们来讨论一下这两种方式的优缺点。

首先，要实现多任务，通常我们会设计Master-Worker模式，Master负责分配任务，Worker负责执行任务，因此，多任务环境下，通常是一个Master，多个Worker。

如果用多进程实现Master-Worker，主进程就是Master，其他进程就是Worker。

如果用多线程实现Master-Worker，主线程就是Master，其他线程就是Worker。

多进程模式最大的优点就是稳定性高，因为一个子进程崩溃了，不会影响主进程和其他子进程。（当然主进程挂了所有进程就全挂了，但是Master进程只负责分配任务，挂掉的概率低）著名的Apache最早就是采用多进程模式。

多进程模式的缺点是创建进程的代价大，在Unix/Linux系统下，用fork调用还行，在Windows下创建进程开销巨大。另外，操作系统能同时运行的进程数也是有限的，在内存和CPU的限制下，如果有几千个进程同时运行，操作系统连调度都会成问题。

多线程模式通常比多进程快一点，但是也快不到哪去，而且，多线程模式致命的缺点就是任何一个线程挂掉都可能直接造成整个进程崩溃，因为所有线程共享进程的内存。在Windows上，如果一个线程执行的代码出了问题，你经常可以看到这样的提示：“该程序执行了非法操作，即将关闭”，其实往往是某个线程出了问题，但是操作系统会强制结束整个进程。

在Windows下，多线程的效率比多进程要高，所以微软的IIS服务器默认采用多线程模式。由于多线程存在稳定性的问题，IIS的稳定性就不如Apache。为了缓解这个问题，IIS和Apache现在又有多进程+多线程的混合模式，真是把问题越搞越复杂。

线程切换

无论是多进程还是多线程，只要数量一多，效率肯定上不去，为什么呢？

我们打个比方，假设你不幸正在准备中考，每天晚上需要做语文、数学、英语、物理、化学这5科的作业，每项作业耗时1小时。

如果你先花1小时做语文作业，做完了，再花1小时做数学作业，这样，依次全部做完，一共花5小时，这种方式称为单任务模型，或者批处理任务模型。

假设你打算切换到多任务模型，可以先做1分钟语文，再切换到数学作业，做1分钟，再切换到英语，以此类推，只要切换速度足够快，这种方式就和单核CPU执行多任务是一样的了，以幼儿园小朋友的眼光来看，你就正在同时写5科作业。

但是，切换作业是有代价的，比如从语文切到数学，要先收拾桌子上的语文书本、钢笔（这叫保存现场），然后，打开数学课本、找出圆规直尺（这叫准备新环境），才能开始做数学作业。操作系统在切换进程或者线程时也是一样的，它需要先保存当前执行的现场环境（CPU寄存器状态、内存页等），然后，把新任务的执行环境准备好（恢复上次的寄存器状态，切换内存页等），才能开始执行。这个切换过程虽然很快，但是也需要耗费时间。如果有几千个任务同时进行，操作系统可能就主要忙着切换任务，根本没有多少时间去执行任务了，这种情况最常见的就是硬盘狂响，点窗口无反应，系统处于假死状态。

所以，多任务一旦多到一个限度，就会消耗掉系统所有的资源，结果效率急剧下降，所有任务都做不好。

计算密集型 vs. IO密集型

是否采用多任务的第二个考虑是任务的类型。我们可以把任务分为计算密集型和IO密集型。

计算密集型任务的特点是要进行大量的计算，消耗CPU资源，比如计算圆周率、对视频进行高清解码等等，全靠CPU的运算能力。这种计算密集型任务虽然也可以用多任务完成，但是任务越多，花在任务切换的时间就越多，CPU执行任务的效率就越低，所以，要最高效地利用CPU，计算密集型任务同时进行的数量应当等于CPU的核心数。

计算密集型任务由于主要消耗CPU资源，因此，代码运行效率至关重要。Python这样的脚本语言运行效率很低，完全不适合计算密集型任务。对于计算密集型任务，最好用C语言编写。

第二种任务的类型是IO密集型，涉及到网络、磁盘IO的任务都是IO密集型任务，这类任务的特点是CPU消耗很少，任务的大部分时间都在等待IO操作完成（因为IO的速度远远低于CPU和内存的速度）。对于IO密集型任务，任务越多，CPU效率越高，但也有一个限度。常见的大部分任务都是IO密集型任务，比如Web应用。

IO密集型任务执行期间，99%的时间都花在IO上，花在CPU上的时间很少，因此，用运行速度极快的C语言替换用Python这样运行速度极低的脚本语言，完全无法提升运行效率。对于IO密集型任务，最合适的语言就是开发效率最高（代码量最少）的语言，脚本语言是首选，C语言最差。

异步IO

考虑到CPU和IO之间巨大的速度差异，一个任务在执行的过程中大部分时间都在等待IO操作，单进程单线程模型会导致别的任务无法并行执行，因此，我们才需要多进程模型或者多线程模型来支持多任务并发执行。

现代操作系统对IO操作已经做了巨大的改进，最大的特点就是支持异步IO。如果充分利用操作系统提供的异步IO支持，就可以用单进程单线程模型来执行多任务，这种全新的模型称为事件驱动模型，Nginx就是支持异步IO的Web服务器，它在单核CPU上采用单进程模型就可以高效地支持多任务。在多核CPU上，可以运行多个进程（数量与CPU核心数相同），充分利用多核CPU。由于系统总的进程数量十分有限，因此操作系统调度非常高效。用异步IO编程模型来实现多任务是一个主要的趋势。

对应到Python语言，单线程的异步编程模型称为协程，有了协程的支持，就可以基于事件驱动编写高效的多任务程序。我们会在后面讨论如何编写协程。

#-----------------------------------------------------------------------------------------
#分布式进程
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431929340191970154d52b9d484b88a7b343708fcc60000

在Thread和Process中，应当优选Process，因为Process更稳定，而且，Process可以分布到多台机器上，而Thread最多只能分布到同一台机器的多个CPU上。

Python的multiprocessing模块不但支持多进程，其中managers子模块还支持把多进程分布到多台机器上。一个服务进程可以作为调度者，将任务分布到其他多个进程中，依靠网络通信。由于managers模块封装很好，不必了解网络通信的细节，就可以很容易地编写分布式多进程程序。

举个例子：如果我们已经有一个通过Queue通信的多进程程序在同一台机器上运行，现在，由于处理任务的进程任务繁重，希望把发送任务的进程和处理任务的进程分布到两台机器上。怎么用分布式进程实现？

原有的Queue可以继续使用，但是，通过managers模块把Queue通过网络暴露出去，就可以让其他机器的进程访问Queue了。

我们先看服务进程，服务进程负责启动Queue，把Queue注册到网络上，然后往Queue里面写入任务：
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# task_master.py

import random, time, queue
from multiprocessing.managers import BaseManager

# 发送任务的队列:
task_queue = queue.Queue()
# 接收结果的队列:
result_queue = queue.Queue()

def return_task_queue():
    global task_queue
    return task_queue


def return_result_queue():
    global result_queue
    return result_queue

# 从BaseManager继承的QueueManager:
class QueueManager(BaseManager):
    pass

if __name__ == '__main__':
    # 把两个Queue都注册到网络上, callable参数关联了Queue对象:
    QueueManager.register('get_task_queue', callable=return_task_queue) #这里与原文不一致 参考https://blog.csdn.net/lilong117194/article/details/76051843
    QueueManager.register('get_result_queue', callable=return_result_queue) #这里与原文不一致
    # 绑定端口5000, 设置验证码'abc':
    manager = QueueManager(address=('127.0.0.1', 5000), authkey=b'abc')
    # 启动Queue:
    manager.start()
    # 获得通过网络访问的Queue对象:
    task = manager.get_task_queue()
    result = manager.get_result_queue()
    # 放几个任务进去:
    for i in range(10):
        n = random.randint(0, 10000)
        print('Put task %d...' % n)
        task.put(n)
    # 从result队列读取结果:
    print('Try get results...')
    for i in range(10):
        r = result.get(timeout=10) #这里待了10s，如果获取不到，则异常queue.Empty
        print('Result: %s' % r)
    # 关闭:
    manager.shutdown()
    print('master exit.')

请注意，当我们在一台机器上写多进程程序时，创建的Queue可以直接拿来用，但是，在分布式多进程环境下，添加任务到Queue不可以直接对原始的task_queue进行操作，那样就绕过了QueueManager的封装，必须通过manager.get_task_queue()获得的Queue接口添加。

然后，在另一台机器上启动任务进程（本机上启动也可以）：
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# task_worker.py

import time, sys, queue
from multiprocessing.managers import BaseManager

# 创建类似的QueueManager:
class QueueManager(BaseManager):
    pass

if __name__ == '__main__':
    # 由于这个QueueManager只从网络上获取Queue，所以注册时只提供名字:
    QueueManager.register('get_task_queue')
    QueueManager.register('get_result_queue')

    # 连接到服务器，也就是运行task_master.py的机器:
    server_addr = '127.0.0.1'
    print('Connect to server %s...' % server_addr)
    # 端口和验证码注意保持与task_master.py设置的完全一致:
    m = QueueManager(address=(server_addr, 5000), authkey=b'abc')
    # 从网络连接:
    m.connect()
    # 获取Queue的对象:
    task = m.get_task_queue()
    result = m.get_result_queue()
    # 从task队列取任务,并把结果写入result队列:
    for i in range(10):
        try:
            n = task.get(timeout=1)
            print('run task %d * %d...' % (n, n))
            r = '%d * %d = %d' % (n, n, n*n)
            time.sleep(1)
            result.put(r)
        except queue.Empty: #如果把master的put的循环删除，则worker的get这里将有10次异常
            print('task queue is empty.')
    # 处理结束:
    print('worker exit.')

务进程要通过网络连接到服务进程，所以要指定服务进程的IP。

现在，可以试试分布式进程的工作效果了。先启动task_master.py服务进程：

$ python3 task_master.py 
Put task 3411...
Put task 1605...
Put task 1398...
Put task 4729...
Put task 5300...
Put task 7471...
Put task 68...
Put task 4219...
Put task 339...
Put task 7866...
Try get results...
task_master.py进程发送完任务后，开始等待result队列的结果。现在启动task_worker.py进程：

$ python3 task_worker.py
Connect to server 127.0.0.1...
run task 3411 * 3411...
run task 1605 * 1605...
run task 1398 * 1398...
run task 4729 * 4729...
run task 5300 * 5300...
run task 7471 * 7471...
run task 68 * 68...
run task 4219 * 4219...
run task 339 * 339...
run task 7866 * 7866...
worker exit.

task_worker.py进程结束，在task_master.py进程中会继续打印出结果：
Result: 3411 * 3411 = 11634921
Result: 1605 * 1605 = 2576025
Result: 1398 * 1398 = 1954404
Result: 4729 * 4729 = 22363441
Result: 5300 * 5300 = 28090000
Result: 7471 * 7471 = 55815841
Result: 68 * 68 = 4624
Result: 4219 * 4219 = 17799961
Result: 339 * 339 = 114921
Result: 7866 * 7866 = 61873956

这个简单的Master/Worker模型有什么用？其实这就是一个简单但真正的分布式计算，把代码稍加改造，启动多个worker，就可以把任务分布到几台甚至几十台机器上，比如把计算n*n的代码换成发送邮件，就实现了邮件队列的异步发送。

Queue对象存储在哪？注意到task_worker.py中根本没有创建Queue的代码，所以，Queue对象存储在task_master.py进程中：

                                             │
┌─────────────────────────────────────────┐     ┌──────────────────────────────────────┐
│task_master.py                           │  │  │task_worker.py                        │
│                                         │     │                                      │
│  task = manager.get_task_queue()        │  │  │  task = manager.get_task_queue()     │
│  result = manager.get_result_queue()    │     │  result = manager.get_result_queue() │
│              │                          │  │  │              │                       │
│              │                          │     │              │                       │
│              ▼                          │  │  │              │                       │
│  ┌─────────────────────────────────┐    │     │              │                       │
│  │QueueManager                     │    │  │  │              │                       │
│  │ ┌────────────┐ ┌──────────────┐ │    │     │              │                       │
│  │ │ task_queue │ │ result_queue │ │<───┼──┼──┼──────────────┘                       │
│  │ └────────────┘ └──────────────┘ │    │     │                                      │
│  └─────────────────────────────────┘    │  │  │                                      │
└─────────────────────────────────────────┘     └──────────────────────────────────────┘
                                             │

                                          Network
而Queue之所以能通过网络访问，就是通过QueueManager实现的。由于QueueManager管理的不止一个Queue，所以，要给每个Queue的网络调用接口起个名字，比如get_task_queue。

authkey有什么用？这是为了保证两台机器正常通信，不被其他机器恶意干扰。如果task_worker.py的authkey和task_master.py的authkey不一致，肯定连接不上。

小结

Python的分布式进程接口简单，封装良好，适合需要把繁重任务分布到多台机器的环境下。

注意Queue的作用是用来传递任务和接收结果，每个任务的描述数据量要尽量小。比如发送一个处理日志文件的任务，就不要发送几百兆的日志文件本身，而是发送日志文件存放的完整路径，由Worker进程再去共享的磁盘上读取文件。
#-----------------------------------------------------------------------------------------
#正则表达式
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143193331387014ccd1040c814dee8b2164bb4f064cff000

#-----------------------------------------------------------------------------------------
#datetime
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431937554888869fb52b812243dda6103214cd61d0c2000
datetime是Python处理日期和时间的标准库。

获取当前日期和时间

我们先看如何获取当前日期和时间：

>>> from datetime import datetime
>>> now = datetime.now() # 获取当前datetime
>>> print(now)
2015-05-18 16:28:07.198690
>>> print(type(now))
<class 'datetime.datetime'>
注意到datetime是模块，datetime模块还包含一个datetime类，通过from datetime import datetime导入的才是datetime这个类。

如果仅导入import datetime，则必须引用全名datetime.datetime。

datetime.now()返回当前日期和时间，其类型是datetime。

获取指定日期和时间

要指定某个日期和时间，我们直接用参数构造一个datetime：

>>> from datetime import datetime
>>> dt = datetime(2015, 4, 19, 12, 20) # 用指定日期时间创建datetime
>>> print(dt)
2015-04-19 12:20:00
datetime转换为timestamp

在计算机中，时间实际上是用数字表示的。我们把1970年1月1日 00:00:00 UTC+00:00时区的时刻称为epoch time，记为0（1970年以前的时间timestamp为负数），当前时间就是相对于epoch time的秒数，称为timestamp。

你可以认为：

timestamp = 0 = 1970-1-1 00:00:00 UTC+0:00
对应的北京时间是：

timestamp = 0 = 1970-1-1 08:00:00 UTC+8:00
可见timestamp的值与时区毫无关系，因为timestamp一旦确定，其UTC时间就确定了，转换到任意时区的时间也是完全确定的，这就是为什么计算机存储的当前时间是以timestamp表示的，因为全球各地的计算机在任意时刻的timestamp都是完全相同的（假定时间已校准）。

把一个datetime类型转换为timestamp只需要简单调用timestamp()方法：

>>> from datetime import datetime
>>> dt = datetime(2015, 4, 19, 12, 20) # 用指定日期时间创建datetime
>>> dt.timestamp() # 把datetime转换为timestamp
1429417200.0
注意Python的timestamp是一个浮点数。如果有小数位，小数位表示毫秒数。

某些编程语言（如Java和JavaScript）的timestamp使用整数表示毫秒数，这种情况下只需要把timestamp除以1000就得到Python的浮点表示方法。

timestamp转换为datetime

要把timestamp转换为datetime，使用datetime提供的fromtimestamp()方法：

>>> from datetime import datetime
>>> t = 1429417200.0
>>> print(datetime.fromtimestamp(t))
2015-04-19 12:20:00
注意到timestamp是一个浮点数，它没有时区的概念，而datetime是有时区的。上述转换是在timestamp和本地时间做转换。

本地时间是指当前操作系统设定的时区。例如北京时区是东8区，则本地时间：

2015-04-19 12:20:00
实际上就是UTC+8:00时区的时间：

2015-04-19 12:20:00 UTC+8:00
而此刻的格林威治标准时间与北京时间差了8小时，也就是UTC+0:00时区的时间应该是：

2015-04-19 04:20:00 UTC+0:00
timestamp也可以直接被转换到UTC标准时区的时间：

>>> from datetime import datetime
>>> t = 1429417200.0
>>> print(datetime.fromtimestamp(t)) # 本地时间
2015-04-19 12:20:00
>>> print(datetime.utcfromtimestamp(t)) # UTC时间
2015-04-19 04:20:00
str转换为datetime

很多时候，用户输入的日期和时间是字符串，要处理日期和时间，首先必须把str转换为datetime。转换方法是通过datetime.strptime()实现，需要一个日期和时间的格式化字符串：

>>> from datetime import datetime
>>> cday = datetime.strptime('2015-6-1 18:19:59', '%Y-%m-%d %H:%M:%S')
>>> print(cday)
2015-06-01 18:19:59
字符串'%Y-%m-%d %H:%M:%S'规定了日期和时间部分的格式。详细的说明请参考Python文档。

注意转换后的datetime是没有时区信息的。

datetime转换为str

如果已经有了datetime对象，要把它格式化为字符串显示给用户，就需要转换为str，转换方法是通过strftime()实现的，同样需要一个日期和时间的格式化字符串：

>>> from datetime import datetime
>>> now = datetime.now()
>>> print(now.strftime('%a, %b %d %H:%M'))
Mon, May 05 16:28
datetime加减

对日期和时间进行加减实际上就是把datetime往后或往前计算，得到新的datetime。加减可以直接用+和-运算符，不过需要导入timedelta这个类：

>>> from datetime import datetime, timedelta
>>> now = datetime.now()
>>> now
datetime.datetime(2015, 5, 18, 16, 57, 3, 540997)
>>> now + timedelta(hours=10)
datetime.datetime(2015, 5, 19, 2, 57, 3, 540997)
>>> now - timedelta(days=1)
datetime.datetime(2015, 5, 17, 16, 57, 3, 540997)
>>> now + timedelta(days=2, hours=12)
datetime.datetime(2015, 5, 21, 4, 57, 3, 540997)
可见，使用timedelta你可以很容易地算出前几天和后几天的时刻。

本地时间转换为UTC时间

本地时间是指系统设定时区的时间，例如北京时间是UTC+8:00时区的时间，而UTC时间指UTC+0:00时区的时间。

一个datetime类型有一个时区属性tzinfo，但是默认为None，所以无法区分这个datetime到底是哪个时区，除非强行给datetime设置一个时区：

>>> from datetime import datetime, timedelta, timezone
>>> tz_utc_8 = timezone(timedelta(hours=8)) # 创建时区UTC+8:00
>>> now = datetime.now()
>>> now
datetime.datetime(2015, 5, 18, 17, 2, 10, 871012)
>>> dt = now.replace(tzinfo=tz_utc_8) # 强制设置为UTC+8:00
>>> dt
datetime.datetime(2015, 5, 18, 17, 2, 10, 871012, tzinfo=datetime.timezone(datetime.timedelta(0, 28800)))
如果系统时区恰好是UTC+8:00，那么上述代码就是正确的，否则，不能强制设置为UTC+8:00时区。

时区转换

我们可以先通过utcnow()拿到当前的UTC时间，再转换为任意时区的时间：

# 拿到UTC时间，并强制设置时区为UTC+0:00:
>>> utc_dt = datetime.utcnow().replace(tzinfo=timezone.utc)
>>> print(utc_dt)
2015-05-18 09:05:12.377316+00:00
# astimezone()将转换时区为北京时间:
>>> bj_dt = utc_dt.astimezone(timezone(timedelta(hours=8)))
>>> print(bj_dt)
2015-05-18 17:05:12.377316+08:00
# astimezone()将转换时区为东京时间:
>>> tokyo_dt = utc_dt.astimezone(timezone(timedelta(hours=9)))
>>> print(tokyo_dt)
2015-05-18 18:05:12.377316+09:00
# astimezone()将bj_dt转换时区为东京时间:
>>> tokyo_dt2 = bj_dt.astimezone(timezone(timedelta(hours=9)))
>>> print(tokyo_dt2)
2015-05-18 18:05:12.377316+09:00
时区转换的关键在于，拿到一个datetime时，要获知其正确的时区，然后强制设置时区，作为基准时间。

利用带时区的datetime，通过astimezone()方法，可以转换到任意时区。

注：不是必须从UTC+0:00时区转换到其他时区，任何带时区的datetime都可以正确转换，例如上述bj_dt到tokyo_dt的转换。

小结

datetime表示的时间需要时区信息才能确定一个特定的时间，否则只能视为本地时间。

如果要存储datetime，最佳方法是将其转换为timestamp再存储，因为timestamp的值与时区完全无关。
#-----------------------------------------------------------------------------------------
#collections
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431953239820157155d21c494e5786fce303f3018c86000
collections是Python内建的一个集合模块，提供了许多有用的集合类。

namedtuple

我们知道tuple可以表示不变集合，例如，一个点的二维坐标就可以表示成：

>>> p = (1, 2)
但是，看到(1, 2)，很难看出这个tuple是用来表示一个坐标的。

定义一个class又小题大做了，这时，namedtuple就派上了用场：

>>> from collections import namedtuple
>>> Point = namedtuple('Point', ['x', 'y'])
>>> p = Point(1, 2)
>>> p.x
1
>>> p.y
2
namedtuple是一个函数，它用来创建一个自定义的tuple对象，并且规定了tuple元素的个数，并可以用属性而不是索引来引用tuple的某个元素。

这样一来，我们用namedtuple可以很方便地定义一种数据类型，它具备tuple的不变性，又可以根据属性来引用，使用十分方便。

可以验证创建的Point对象是tuple的一种子类：

>>> isinstance(p, Point)
True
>>> isinstance(p, tuple)
True
类似的，如果要用坐标和半径表示一个圆，也可以用namedtuple定义：

# namedtuple('名称', [属性list]):
Circle = namedtuple('Circle', ['x', 'y', 'r'])
deque

使用list存储数据时，按索引访问元素很快，但是插入和删除元素就很慢了，因为list是线性存储，数据量大的时候，插入和删除效率很低。

deque是为了高效实现插入和删除操作的双向列表，适合用于队列和栈：

>>> from collections import deque
>>> q = deque(['a', 'b', 'c'])
>>> q.append('x')
>>> q.appendleft('y')
>>> q
deque(['y', 'a', 'b', 'c', 'x'])
deque除了实现list的append()和pop()外，还支持appendleft()和popleft()，这样就可以非常高效地往头部添加或删除元素。

defaultdict

使用dict时，如果引用的Key不存在，就会抛出KeyError。如果希望key不存在时，返回一个默认值，就可以用defaultdict：

>>> from collections import defaultdict
>>> dd = defaultdict(lambda: 'N/A')
>>> dd['key1'] = 'abc'
>>> dd['key1'] # key1存在
'abc'
>>> dd['key2'] # key2不存在，返回默认值
'N/A'
注意默认值是调用函数返回的，而函数在创建defaultdict对象时传入。

除了在Key不存在时返回默认值，defaultdict的其他行为跟dict是完全一样的。

OrderedDict

使用dict时，Key是无序的。在对dict做迭代时，我们无法确定Key的顺序。

如果要保持Key的顺序，可以用OrderedDict：

>>> from collections import OrderedDict
>>> d = dict([('a', 1), ('b', 2), ('c', 3)])
>>> d # dict的Key是无序的
{'a': 1, 'c': 3, 'b': 2}
>>> od = OrderedDict([('a', 1), ('b', 2), ('c', 3)])
>>> od # OrderedDict的Key是有序的
OrderedDict([('a', 1), ('b', 2), ('c', 3)])
注意，OrderedDict的Key会按照插入的顺序排列，不是Key本身排序：

>>> od = OrderedDict()
>>> od['z'] = 1
>>> od['y'] = 2
>>> od['x'] = 3
>>> list(od.keys()) # 按照插入的Key的顺序返回
['z', 'y', 'x']
OrderedDict可以实现一个FIFO（先进先出）的dict，当容量超出限制时，先删除最早添加的Key：

from collections import OrderedDict

class LastUpdatedOrderedDict(OrderedDict):

    def __init__(self, capacity):
        super(LastUpdatedOrderedDict, self).__init__()
        self._capacity = capacity

    def __setitem__(self, key, value):
        containsKey = 1 if key in self else 0
        if len(self) - containsKey >= self._capacity:
            last = self.popitem(last=False)
            print('remove:', last)
        if containsKey:
            del self[key]
            print('set:', (key, value))
        else:
            print('add:', (key, value))
        OrderedDict.__setitem__(self, key, value)
Counter

Counter是一个简单的计数器，例如，统计字符出现的个数：

>>> from collections import Counter
>>> c = Counter()
>>> for ch in 'programming':
...     c[ch] = c[ch] + 1
...
>>> c
Counter({'g': 2, 'm': 2, 'r': 2, 'a': 1, 'i': 1, 'o': 1, 'n': 1, 'p': 1})
Counter实际上也是dict的一个子类，上面的结果可以看出，字符'g'、'm'、'r'各出现了两次，其他字符各出现了一次。

小结

collections模块提供了一些有用的集合类，可以根据需要选用。
#-----------------------------------------------------------------------------------------
#contextlib
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001478651770626de401ff1c0d94f379774cabd842222ff000
在Python中，读写文件这样的资源要特别注意，必须在使用完毕后正确关闭它们。正确关闭文件资源的一个方法是使用try...finally：
try:
try:
    f = open('test.txt', 'r')
    print(f.read())
finally:
    if f:
        f.close()

写try...finally非常繁琐。Python的with语句允许我们非常方便地使用资源，而不必担心资源没有关闭，所以上面的代码可以简化为：
with open('test.txt', 'r') as f:
    print(f.read())

这样我们就可以把自己写的资源对象用于with语句：
with Query('Bob') as q:
    q.query()
    
#example.py
class Query(object):
    def __init__(self, name):
        self.name = name

    def __enter__(self):
        print('Begin')
        return self

    def __exit__(self, exc_type, exc_value, traceback):
        if exc_type:
            print('Error')
        else:
            print('End')

    def query(self):
        print('Query info about %s...' % self.name)

if __name__=='__main__':
    with Query('Bob') as q:
        q.query()
        #raise RuntimeError("error")
输出：
Begin
Query info about Bob...
End

@contextmanager
编写__enter__和__exit__仍然很繁琐，因此Python的标准库contextlib提供了更简单的写法，上面的代码可以改写如下：
from contextlib import contextmanager
class Query(object):
    def __init__(self, name):
        self.name = name

    def query(self):
        print('Query info about %s...' % self.name)

@contextmanager
def create_query(name):
    print('Begin')
    q = Query(name)
    yield q
    print('End')

if __name__=='__main__':
    with create_query('Bob') as q:
        print("Before q.query()")
        q.query()
        print("After q.query()")
输出：
Begin
Before q.query()
Query info about Bob...
After q.query()
End

很多时候，我们希望在某段代码执行前后自动执行特定代码，也可以用@contextmanager实现。例如：
from contextlib import contextmanager

@contextmanager
def tag(name):
    print("<%s>" % name)
    yield
    print("</%s>" % name)

if __name__=='__main__':
    with tag("h1"):
        print("hello")
        print("world")
输出：
<h1>
hello
world
</h1>

代码的执行顺序是：
with语句首先执行yield之前的语句，因此打印出<h1>；
yield调用会执行with语句内部的所有语句，因此打印出hello和world；
最后执行yield之后的语句，打印出</h1>。
因此，@contextmanager让我们通过编写generator来简化上下文管理。

@closing
如果一个对象没有实现上下文，我们就不能把它用于with语句。这个时候，可以用closing()来把该对象变为上下文对象。例如，用with语句使用urlopen()：
from contextlib import closing
from urllib.request import urlopen

with closing(urlopen('https://www.python.org')) as page:#这个例子并不恰当，不使用closing，也可以使用with语句with urlopen('https://www.python.org') as page:
    for line in page:
        print(line)

如果对象要在with as中使用，则该对象需要实现__enter__和__exit__方法，如果没有实现这两个方法，又想在with as中使用对象，则只要提供了close方法，就可以用closing方法：
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from contextlib import closing


class Query(object):
    def __init__(self, name):
        self.name = name

    def query(self):
        print('Query info about %s...' % self.name)

    def close(self):
        print('Query object close')

if __name__=='__main__':
    with closing(Query('Bob')) as q:
        q.query()

#https://www.cnblogs.com/DswCnblog/p/6126588.html
with真正强大之处是它可以处理异常。可能你已经注意到Sample类的__exit__方法有三个参数- val, type 和 trace。 这些参数在异常处理中相当有用。我们来改一下代码，看看具体如何工作的。
#!/usr/bin/env python
# with_example02.py
 
class Sample:
    def __enter__(self):
        return self
 
    def __exit__(self, type, value, trace):
        print "type:", type
        print "value:", value
        print "trace:", trace
 
    def do_something(self):
        bar = 1/0
        return bar + 10
 
with Sample() as sample:
    sample.do_something()

这个例子中，with后面的get_sample()变成了Sample()。这没有任何关系，只要紧跟with后面的语句所返回的对象有__enter__()和__exit__()方法即可。此例中，Sample()的__enter__()方法返回新创建的Sample对象，并赋值给变量sample。
代码执行后：

bash-3.2$ ./with_example02.py
type: <type 'exceptions.ZeroDivisionError'>
value: integer division or modulo by zero
trace: <traceback object at 0x1004a8128>
Traceback (most recent call last):
  File "./with_example02.py", line 19, in <module>
    sample.do_something()
  File "./with_example02.py", line 15, in do_something
    bar = 1/0
ZeroDivisionError: integer division or modulo by zero

实际上，在with后面的代码块抛出任何异常时，__exit__()方法被执行。正如例子所示，异常抛出时，与之关联的type，value和stack trace传给__exit__()方法，因此抛出的ZeroDivisionError异常被打印出来了。开发库时，清理资源，关闭文件等等操作，都可以放在__exit__方法当中。
因此，Python的with语句是提供一个有效的机制，让代码更简练，同时在异常产生时，清理工作更简单。

#https://blog.csdn.net/zly9923218/article/details/53404849
另外，__exit__除了用于tear things down，还可以进行异常的监控和处理，注意后几个参数。要跳过一个异常，只需要返回该函数True即可。下面的样例代码跳过了所有的TypeError，而让其他异常正常抛出。
def __exit__(self, type, value, traceback):
    return isinstance(value, TypeError)

在python2.5及以后，file对象已经写好了enter和exit函数，我们可以这样测试：

>>> f = open("x.txt")
>>> f
<open file 'x.txt', mode 'r' at 0x00AE82F0>
>>> f.__enter__()
<open file 'x.txt', mode 'r' at 0x00AE82F0>
>>> f.read(1)
'X'
>>> f.__exit__(None, None, None)
>>> f.read(1)
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
ValueError: I/O operation on closed file
之后，我们如果要打开文件并保证最后关闭他，只需要这么做：
with open("x.txt") as f:
    data = f.read()
    do something with data
如果有多个项，我们可以这么写：

with open("x.txt") as f1, open('xxx.txt') as f2:
    do something with f1,f2
#-----------------------------------------------------------------------------------------
# chardet https://www.liaoxuefeng.com/wiki/1016959663602400/1183255880134144
字符串编码一直是令人非常头疼的问题，尤其是我们在处理一些不规范的第三方网页的时候。虽然Python提供了Unicode表示的str和bytes两种数据类型，并且可以通过encode()和decode()方法转换，但是，在不知道编码的情况下，对bytes做decode()不好做。

对于未知编码的bytes，要把它转换成str，需要先“猜测”编码。猜测的方式是先收集各种编码的特征字符，根据特征字符判断，就能有很大概率“猜对”。

当然，我们肯定不能从头自己写这个检测编码的功能，这样做费时费力。chardet这个第三方库正好就派上了用场。用它来检测编码，简单易用。

安装chardet
如果安装了Anaconda，chardet就已经可用了。否则，需要在命令行下通过pip安装：
$ pip install chardet

如果遇到Permission denied安装失败，请加上sudo重试。

使用chardet
当我们拿到一个bytes时，就可以对其检测编码。用chardet检测编码，只需要一行代码：
>>> chardet.detect(b'Hello, world!')
{'encoding': 'ascii', 'confidence': 1.0, 'language': ''}
检测出的编码是ascii，注意到还有个confidence字段，表示检测的概率是1.0（即100%）。

我们来试试检测GBK编码的中文：
>>> data = '离离原上草，一岁一枯荣'.encode('gbk')
>>> chardet.detect(data)
{'encoding': 'GB2312', 'confidence': 0.7407407407407407, 'language': 'Chinese'}
检测的编码是GB2312，注意到GBK是GB2312的超集，两者是同一种编码，检测正确的概率是74%，language字段指出的语言是'Chinese'。

对UTF-8编码进行检测：
>>> data = '离离原上草，一岁一枯荣'.encode('utf-8')
>>> chardet.detect(data)
{'encoding': 'utf-8', 'confidence': 0.99, 'language': ''}

我们再试试对日文进行检测：
>>> data = '最新の主要ニュース'.encode('euc-jp')
>>> chardet.detect(data)
{'encoding': 'EUC-JP', 'confidence': 0.99, 'language': 'Japanese'}

可见，用chardet检测编码，使用简单。获取到编码后，再转换为str，就可以方便后续处理。

chardet支持检测的编码列表请参考官方文档Supported encodings。

小结
使用chardet检测编码非常容易，chardet支持检测中文、日文、韩文等多种语言。
#-----------------------------------------------------------------------------------------
#TCP编程
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001432004374523e495f640612f4b08975398796939ec3c000
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# 导入socket库:
import socket

if __name__ == '__main__':
    # 创建一个socket:
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # 建立连接:
    s.connect(('www.sina.com.cn', 80))

    # 发送数据:
    s.send(b'GET / HTTP/1.1\r\nHost: www.sina.com.cn\r\nConnection: close\r\n\r\n')

    buffer = []
    while True:
        # 每次最多接收1k字节:
        d = s.recv(1024)
        if d:
            buffer.append(d)
        else:
            break
    data = b''.join(buffer)

    # 关闭连接:
    s.close()

    header, html = data.split(b'\r\n\r\n', 1)
    print(header.decode('utf-8'))
    # 把接收的数据写入文件:
    with open('sina.html', 'wb') as f:
        f.write(html)
        
        
#server.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# 导入socket库:
import socket
import threading
import time


def tcplink(sock, addr):
    print('Accept new connection from %s:%s...' % addr)
    sock.send(b'Welcome!')
    while True:
        data = sock.recv(1024)
        time.sleep(1)
        if not data or data.decode('utf-8') == 'exit':
            break
        sock.send(('Hello, %s!' % data.decode('utf-8')).encode('utf-8'))
    sock.close()
    print('Connection from %s:%s closed.' % addr)


if __name__ == '__main__':
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # 监听端口:
    s.bind(('127.0.0.1', 9999))
    s.listen(5)
    print('Waiting for connection...')
    while True:
        # 接受一个新连接:
        sock, addr = s.accept()
        # 创建新线程来处理TCP连接:
        t = threading.Thread(target=tcplink, args=(sock, addr))
        t.start()
输出：
Waiting for connection...
Accept new connection from 127.0.0.1:9327...
Connection from 127.0.0.1:9327 closed.


#client.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# client.py
import socket

if __name__ == '__main__':
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # 建立连接:
    s.connect(('127.0.0.1', 9999))
    # 接收欢迎消息:
    print(s.recv(1024).decode('utf-8'))
    for data in [b'Michael', b'Tracy', b'Sarah']:
        # 发送数据:
        s.send(data)
        print(s.recv(1024).decode('utf-8'))
    s.send(b'exit')
    s.close()
输出：
Welcome!
Hello, Michael!
Hello, Tracy!
Hello, Sarah!

小结
用TCP协议进行Socket编程在Python中十分简单，对于客户端，要主动连接服务器的IP和指定端口，对于服务器，要首先监听指定端口，然后，对每一个新的连接，创建一个线程或进程来处理。通常，服务器程序会无限运行下去。

同一个端口，被一个Socket绑定了以后，就不能被别的Socket绑定了。
#-----------------------------------------------------------------------------------------
#UDP编程
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001432004977916a212e2168e21449981ad65cd16e71201000

#server.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# 导入socket库:
import socket

if __name__ == '__main__':
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    # 绑定端口:
    s.bind(('127.0.0.1', 9999))
    print('Bind UDP on 9999...')
    while True:
        # 接收数据:
        data, addr = s.recvfrom(1024)
        print('Received from %s:%s.' % addr)
        s.sendto(b'Hello, %s!' % data, addr)
输出：
Bind UDP on 9999...
Received from 127.0.0.1:57897.
Received from 127.0.0.1:57897.
Received from 127.0.0.1:57897.

#client.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# client.py
import socket

if __name__ == '__main__':
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    for data in [b'Michael', b'Tracy', b'Sarah']:
        # 发送数据:
        s.sendto(data, ('127.0.0.1', 9999))
        # 接收数据:
        print(s.recv(1024).decode('utf-8'))
    s.close()
输出：
Hello, Michael!
Hello, Tracy!
Hello, Sarah!

小结
UDP的使用与TCP类似，但是不需要建立连接。此外，服务器绑定UDP端口和TCP端口互不冲突，也就是说，UDP的9999端口与TCP的9999端口可以各自绑定。
#-----------------------------------------------------------------------------------------
#电子邮件
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001432005156604f38836be1707453eb025ce8c3079978d000
Email的历史比Web还要久远，直到现在，Email也是互联网上应用非常广泛的服务。

几乎所有的编程语言都支持发送和接收电子邮件，但是，先等等，在我们开始编写代码之前，有必要搞清楚电子邮件是如何在互联网上运作的。

我们来看看传统邮件是如何运作的。假设你现在在北京，要给一个香港的朋友发一封信，怎么做呢？

首先你得写好信，装进信封，写上地址，贴上邮票，然后就近找个邮局，把信仍进去。

信件会从就近的小邮局转运到大邮局，再从大邮局往别的城市发，比如先发到天津，再走海运到达香港，也可能走京九线到香港，但是你不用关心具体路线，你只需要知道一件事，就是信件走得很慢，至少要几天时间。

信件到达香港的某个邮局，也不会直接送到朋友的家里，因为邮局的叔叔是很聪明的，他怕你的朋友不在家，一趟一趟地白跑，所以，信件会投递到你的朋友的邮箱里，邮箱可能在公寓的一层，或者家门口，直到你的朋友回家的时候检查邮箱，发现信件后，就可以取到邮件了。

电子邮件的流程基本上也是按上面的方式运作的，只不过速度不是按天算，而是按秒算。

现在我们回到电子邮件，假设我们自己的电子邮件地址是me@163.com，对方的电子邮件地址是friend@sina.com（注意地址都是虚构的哈），现在我们用Outlook或者Foxmail之类的软件写好邮件，填上对方的Email地址，点“发送”，电子邮件就发出去了。这些电子邮件软件被称为MUA：Mail User Agent——邮件用户代理。

Email从MUA发出去，不是直接到达对方电脑，而是发到MTA：Mail Transfer Agent——邮件传输代理，就是那些Email服务提供商，比如网易、新浪等等。由于我们自己的电子邮件是163.com，所以，Email首先被投递到网易提供的MTA，再由网易的MTA发到对方服务商，也就是新浪的MTA。这个过程中间可能还会经过别的MTA，但是我们不关心具体路线，我们只关心速度。

Email到达新浪的MTA后，由于对方使用的是@sina.com的邮箱，因此，新浪的MTA会把Email投递到邮件的最终目的地MDA：Mail Delivery Agent——邮件投递代理。Email到达MDA后，就静静地躺在新浪的某个服务器上，存放在某个文件或特殊的数据库里，我们将这个长期保存邮件的地方称之为电子邮箱。

同普通邮件类似，Email不会直接到达对方的电脑，因为对方电脑不一定开机，开机也不一定联网。对方要取到邮件，必须通过MUA从MDA上把邮件取到自己的电脑上。

所以，一封电子邮件的旅程就是：

发件人 -> MUA -> MTA -> MTA -> 若干个MTA -> MDA <- MUA <- 收件人
有了上述基本概念，要编写程序来发送和接收邮件，本质上就是：

编写MUA把邮件发到MTA；

编写MUA从MDA上收邮件。

发邮件时，MUA和MTA使用的协议就是SMTP：Simple Mail Transfer Protocol，后面的MTA到另一个MTA也是用SMTP协议。

收邮件时，MUA和MDA使用的协议有两种：POP：Post Office Protocol，目前版本是3，俗称POP3；IMAP：Internet Message Access Protocol，目前版本是4，优点是不但能取邮件，还可以直接操作MDA上存储的邮件，比如从收件箱移到垃圾箱，等等。

邮件客户端软件在发邮件时，会让你先配置SMTP服务器，也就是你要发到哪个MTA上。假设你正在使用163的邮箱，你就不能直接发到新浪的MTA上，因为它只服务新浪的用户，所以，你得填163提供的SMTP服务器地址：smtp.163.com，为了证明你是163的用户，SMTP服务器还要求你填写邮箱地址和邮箱口令，这样，MUA才能正常地把Email通过SMTP协议发送到MTA。

类似的，从MDA收邮件时，MDA服务器也要求验证你的邮箱口令，确保不会有人冒充你收取你的邮件，所以，Outlook之类的邮件客户端会要求你填写POP3或IMAP服务器地址、邮箱地址和口令，这样，MUA才能顺利地通过POP或IMAP协议从MDA取到邮件。

在使用Python收发邮件前，请先准备好至少两个电子邮件，如xxx@163.com，xxx@sina.com，xxx@qq.com等，注意两个邮箱不要用同一家邮件服务商。

最后特别注意，目前大多数邮件服务商都需要手动打开SMTP发信和POP收信的功能，否则只允许在网页登录：

#-----------------------------------------------------------------------------------------
#SMTP发送邮件
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001432005226355aadb8d4b2f3f42f6b1d6f2c5bd8d5263000

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from email.header import Header
from email.mime.text import MIMEText
import smtplib

def send_email(SMTP_host, from_account, from_passwd, to_account, subject, content):
    email_client = smtplib.SMTP(SMTP_host, 25)
    email_client.login(from_account, from_passwd)
    # create msg
    msg = MIMEText(content, 'plain', 'utf-8')
    msg['Subject'] = Header(subject, 'utf-8')  # subject
    msg['From'] = from_account
    msg['To'] = to_account
    email_client.set_debuglevel(1)
    email_client.sendmail(from_account, to_account, msg.as_string())

    email_client.quit()

if __name__=='__main__':
    send_email('smtp.163.com', 'bigben0204@163.com', 'ilovesjtu8624', '464754973@qq.com', 'abc', 'hello, send by Ben')

我们用set_debuglevel(1)就可以打印出和SMTP服务器交互的所有信息。SMTP协议就是简单的文本命令和响应。login()方法用来登录SMTP服务器，sendmail()方法就是发邮件，由于可以一次发给多个人，所以传入一个list，邮件正文是一个str，as_string()把MIMEText对象变成str。

发送HTML邮件
如果我们要发送HTML邮件，而不是普通的纯文本文件怎么办？方法很简单，在构造MIMEText对象时，把HTML字符串传进去，再把第二个参数由plain变为html就可以了：

msg = MIMEText('<html><body><h1>Hello</h1>' +
    '<p>send by <a href="http://www.python.org">Python</a>...</p>' +
    '</body></html>', 'html', 'utf-8')
再发送一遍邮件，你将看到以HTML显示的邮件：


SMTP发送邮件
阅读: 89487
SMTP是发送邮件的协议，Python内置对SMTP的支持，可以发送纯文本邮件、HTML邮件以及带附件的邮件。

Python对SMTP支持有smtplib和email两个模块，email负责构造邮件，smtplib负责发送邮件。

首先，我们来构造一个最简单的纯文本邮件：

from email.mime.text import MIMEText
msg = MIMEText('hello, send by Python...', 'plain', 'utf-8')
注意到构造MIMEText对象时，第一个参数就是邮件正文，第二个参数是MIME的subtype，传入'plain'表示纯文本，最终的MIME就是'text/plain'，最后一定要用utf-8编码保证多语言兼容性。

然后，通过SMTP发出去：

# 输入Email地址和口令:
from_addr = input('From: ')
password = input('Password: ')
# 输入收件人地址:
to_addr = input('To: ')
# 输入SMTP服务器地址:
smtp_server = input('SMTP server: ')

import smtplib
server = smtplib.SMTP(smtp_server, 25) # SMTP协议默认端口是25
server.set_debuglevel(1)
server.login(from_addr, password)
server.sendmail(from_addr, [to_addr], msg.as_string())
server.quit()
我们用set_debuglevel(1)就可以打印出和SMTP服务器交互的所有信息。SMTP协议就是简单的文本命令和响应。login()方法用来登录SMTP服务器，sendmail()方法就是发邮件，由于可以一次发给多个人，所以传入一个list，邮件正文是一个str，as_string()把MIMEText对象变成str。

如果一切顺利，就可以在收件人信箱中收到我们刚发送的Email：

send-mail

仔细观察，发现如下问题：

邮件没有主题；
收件人的名字没有显示为友好的名字，比如Mr Green <green@example.com>；
明明收到了邮件，却提示不在收件人中。
这是因为邮件主题、如何显示发件人、收件人等信息并不是通过SMTP协议发给MTA，而是包含在发给MTA的文本中的，所以，我们必须把From、To和Subject添加到MIMEText中，才是一封完整的邮件：

from email import encoders
from email.header import Header
from email.mime.text import MIMEText
from email.utils import parseaddr, formataddr

import smtplib

def _format_addr(s):
    name, addr = parseaddr(s)
    return formataddr((Header(name, 'utf-8').encode(), addr))

from_addr = input('From: ')
password = input('Password: ')
to_addr = input('To: ')
smtp_server = input('SMTP server: ')

msg = MIMEText('hello, send by Python...', 'plain', 'utf-8')
msg['From'] = _format_addr('Python爱好者 <%s>' % from_addr)
msg['To'] = _format_addr('管理员 <%s>' % to_addr)
msg['Subject'] = Header('来自SMTP的问候……', 'utf-8').encode()

server = smtplib.SMTP(smtp_server, 25)
server.set_debuglevel(1)
server.login(from_addr, password)
server.sendmail(from_addr, [to_addr], msg.as_string())
server.quit()
我们编写了一个函数_format_addr()来格式化一个邮件地址。注意不能简单地传入name <addr@example.com>，因为如果包含中文，需要通过Header对象进行编码。

msg['To']接收的是字符串而不是list，如果有多个邮件地址，用,分隔即可。

再发送一遍邮件，就可以在收件人邮箱中看到正确的标题、发件人和收件人：

mail-with-header

你看到的收件人的名字很可能不是我们传入的管理员，因为很多邮件服务商在显示邮件时，会把收件人名字自动替换为用户注册的名字，但是其他收件人名字的显示不受影响。

如果我们查看Email的原始内容，可以看到如下经过编码的邮件头：

From: =?utf-8?b?UHl0aG9u54ix5aW96ICF?= <xxxxxx@163.com>
To: =?utf-8?b?566h55CG5ZGY?= <xxxxxx@qq.com>
Subject: =?utf-8?b?5p2l6IeqU01UUOeahOmXruWAmeKApuKApg==?=
这就是经过Header对象编码的文本，包含utf-8编码信息和Base64编码的文本。如果我们自己来手动构造这样的编码文本，显然比较复杂。

发送HTML邮件
如果我们要发送HTML邮件，而不是普通的纯文本文件怎么办？方法很简单，在构造MIMEText对象时，把HTML字符串传进去，再把第二个参数由plain变为html就可以了：

msg = MIMEText('<html><body><h1>Hello</h1>' +
    '<p>send by <a href="http://www.python.org">Python</a>...</p>' +
    '</body></html>', 'html', 'utf-8')
再发送一遍邮件，你将看到以HTML显示的邮件：

html-mail

发送附件
如果Email中要加上附件怎么办？带附件的邮件可以看做包含若干部分的邮件：文本和各个附件本身，所以，可以构造一个MIMEMultipart对象代表邮件本身，然后往里面加上一个MIMEText作为邮件正文，再继续往里面加上表示附件的MIMEBase对象即可：

# 邮件对象:
msg = MIMEMultipart()
msg['From'] = _format_addr('Python爱好者 <%s>' % from_addr)
msg['To'] = _format_addr('管理员 <%s>' % to_addr)
msg['Subject'] = Header('来自SMTP的问候……', 'utf-8').encode()

# 邮件正文是MIMEText:
msg.attach(MIMEText('send with file...', 'plain', 'utf-8'))

# 添加附件就是加上一个MIMEBase，从本地读取一个图片:
with open('/Users/michael/Downloads/test.png', 'rb') as f:
    # 设置附件的MIME和文件名，这里是png类型:
    mime = MIMEBase('image', 'png', filename='test.png')
    # 加上必要的头信息:
    mime.add_header('Content-Disposition', 'attachment', filename='test.png')
    mime.add_header('Content-ID', '<0>')
    mime.add_header('X-Attachment-Id', '0')
    # 把附件的内容读进来:
    mime.set_payload(f.read())
    # 用Base64编码:
    encoders.encode_base64(mime)
    # 添加到MIMEMultipart:
    msg.attach(mime)
    
发送图片
如果要把一个图片嵌入到邮件正文中怎么做？直接在HTML邮件中链接图片地址行不行？答案是，大部分邮件服务商都会自动屏蔽带有外链的图片，因为不知道这些链接是否指向恶意网站。

要把图片嵌入到邮件正文中，我们只需按照发送附件的方式，先把邮件作为附件添加进去，然后，在HTML中通过引用src="cid:0"就可以把附件作为图片嵌入了。如果有多个图片，给它们依次编号，然后引用不同的cid:x即可。

把上面代码加入MIMEMultipart的MIMEText从plain改为html，然后在适当的位置引用图片：

msg.attach(MIMEText('<html><body><h1>Hello</h1>' +
    '<p><img src="cid:0"></p>' +
    '</body></html>', 'html', 'utf-8'))
再次发送，就可以看到图片直接嵌入到邮件正文的效果：


同时支持HTML和Plain格式
如果我们发送HTML邮件，收件人通过浏览器或者Outlook之类的软件是可以正常浏览邮件内容的，但是，如果收件人使用的设备太古老，查看不了HTML邮件怎么办？

办法是在发送HTML的同时再附加一个纯文本，如果收件人无法查看HTML格式的邮件，就可以自动降级查看纯文本邮件。

利用MIMEMultipart就可以组合一个HTML和Plain，要注意指定subtype是alternative：

msg = MIMEMultipart('alternative')
msg['From'] = ...
msg['To'] = ...
msg['Subject'] = ...

msg.attach(MIMEText('hello', 'plain', 'utf-8'))
msg.attach(MIMEText('<html><body><h1>Hello</h1></body></html>', 'html', 'utf-8'))
# 正常发送msg对象...
加密SMTP
使用标准的25端口连接SMTP服务器时，使用的是明文传输，发送邮件的整个过程可能会被窃听。要更安全地发送邮件，可以加密SMTP会话，实际上就是先创建SSL安全连接，然后再使用SMTP协议发送邮件。

某些邮件服务商，例如Gmail，提供的SMTP服务必须要加密传输。我们来看看如何通过Gmail提供的安全SMTP发送邮件。

必须知道，Gmail的SMTP端口是587，因此，修改代码如下：

smtp_server = 'smtp.gmail.com'
smtp_port = 587
server = smtplib.SMTP(smtp_server, smtp_port)
server.starttls()
# 剩下的代码和前面的一模一样:
server.set_debuglevel(1)
...
只需要在创建SMTP对象后，立刻调用starttls()方法，就创建了安全连接。后面的代码和前面的发送邮件代码完全一样。

如果因为网络问题无法连接Gmail的SMTP服务器，请相信我们的代码是没有问题的，你需要对你的网络设置做必要的调整。

小结
使用Python的smtplib发送邮件十分简单，只要掌握了各种邮件类型的构造方法，正确设置好邮件头，就可以顺利发出。

构造一个邮件对象就是一个Messag对象，如果构造一个MIMEText对象，就表示一个文本邮件对象，如果构造一个MIMEImage对象，就表示一个作为附件的图片，要把多个对象组合起来，就用MIMEMultipart对象，而MIMEBase可以表示任何对象。它们的继承关系如下：

Message
+- MIMEBase
   +- MIMEMultipart
   +- MIMENonMultipart
      +- MIMEMessage
      +- MIMEText
      +- MIMEImage
这种嵌套关系就可以构造出任意复杂的邮件。你可以通过email.mime文档查看它们所在的包以及详细的用法。
#-----------------------------------------------------------------------------------------
#POP3收取邮件
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014320098721191b70a2cf7b5441deb01595edd8147196000
#-----------------------------------------------------------------------------------------
#HTTP协议简介
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001432011939547478fd5482deb47b08716557cc99764e0000
#-----------------------------------------------------------------------------------------
#WSGI接口
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001432012393132788f71e0edad4676a3f76ac7776f3a16000

运行WSGI服务
我们先编写hello.py，实现Web应用程序的WSGI处理函数：

# hello.py

def application(environ, start_response):
    start_response('200 OK', [('Content-Type', 'text/html')])
    return [b'<h1>Hello, web!</h1>']
然后，再编写一个server.py，负责启动WSGI服务器，加载application()函数：

# server.py
# 从wsgiref模块导入:
from wsgiref.simple_server import make_server
# 导入我们自己编写的application函数:
from hello import application

# 创建一个服务器，IP地址为空，端口是8000，处理函数是application:
httpd = make_server('', 8000, application)
print('Serving HTTP on port 8000...')
# 开始监听HTTP请求:
httpd.serve_forever()
确保以上两个文件在同一个目录下，然后在命令行输入python server.py来启动WSGI服务器：

wsgiref-start

注意：如果8000端口已被其他程序占用，启动将失败，请修改成其他端口。

启动成功后，打开浏览器，输入http://localhost:8000/，就可以看到结果了：

hello-web

在命令行可以看到wsgiref打印的log信息：

Serving HTTP on port 8000...
127.0.0.1 - - [14/Apr/2018 23:16:26] "GET / HTTP/1.1" 200 20
127.0.0.1 - - [14/Apr/2018 23:16:26] "GET /favicon.ico HTTP/1.1" 200 20


按Ctrl+C终止服务器。

如果你觉得这个Web应用太简单了，可以稍微改造一下，从environ里读取PATH_INFO，这样可以显示更加动态的内容：
# hello.py
def application(environ, start_response):
    start_response('200 OK', [('Content-Type', 'text/html')])
    body = '<h1>Hello, %s!</h1>' % (environ['PATH_INFO'][1:] or 'web')
    return [body.encode('utf-8')]
你可以在地址栏输入用户名作为URL的一部分，将返回Hello, xxx!：

hello-michael

是不是有点Web App的感觉了？

小结
无论多么复杂的Web应用程序，入口都是一个WSGI处理函数。HTTP请求的所有输入信息都可以通过environ获得，HTTP响应的输出都可以通过start_response()加上函数返回值作为Body。

复杂的Web应用程序，光靠一个WSGI函数来处理还是太底层了，我们需要在WSGI之上再抽象出Web框架，进一步简化Web开发。
#-----------------------------------------------------------------------------------------
#使用Web框架
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001432012745805707cb9f00a484d968c72dbb7cfc90b91000

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# 从wsgiref模块导入:
from flask import Flask
from flask import request

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def home():
    return '<h1>Home</h1>'

@app.route('/signin', methods=['GET']) #不带用户输入参数
def signin_form():
    return '''<form action="/signin" method="post">
              <p><input name="username"></p>
              <p><input name="password" type="password"></p>
              <p><button type="submit">Sign In</button></p>
              </form>'''

@app.route('/signin', methods=['POST']) #带用户输入参数
def signin():
    # 需要从request对象读取表单内容：
    if request.form['username']=='admin' and request.form['password']=='password':
        return '<h3>Hello, admin!</h3>'
    return '<h3>Bad username or password.</h3>'

if __name__ == '__main__':
    app.run()

    
实际的Web App应该拿到用户名和口令后，去数据库查询再比对，来判断用户是否能登录成功。

除了Flask，常见的Python Web框架还有：

Django：全能型Web框架；

web.py：一个小巧的Web框架；

Bottle：和Flask类似的Web框架；

Tornado：Facebook的开源异步Web框架。

当然了，因为开发Python的Web框架也不是什么难事，我们后面也会讲到开发Web框架的内容。

小结
有了Web框架，我们在编写Web应用时，注意力就从WSGI处理函数转移到URL+对应的处理函数，这样，编写Web App就更加简单了。

在编写URL处理函数时，除了配置URL外，从HTTP请求拿到用户数据也是非常重要的。Web框架都提供了自己的API来实现这些功能。Flask通过request.form['name']来获取表单的内容。
#-----------------------------------------------------------------------------------------
#使用模板
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014320129740415df73bf8f81e478982bf4d5c8aa3817a000

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from flask import Flask, request, render_template

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def home():
    return render_template('home.html')

@app.route('/signin', methods=['GET'])
def signin_form():
    return render_template('form.html')

@app.route('/signin', methods=['POST'])
def signin():
    username = request.form['username']
    password = request.form['password']
    if username=='admin' and password=='password':
        return render_template('signin-ok.html', username=username)
    return render_template('form.html', message='Bad username or password', username=username)

if __name__ == '__main__':
    app.run()

#templates/home.html
<html>
<head>
  <title>Home</title>
</head>
<body>
  <h1 style="font-style:italic">Home</h1>
</body>
</html>

#templates/form.html
<html>
<head>
  <title>Please Sign In</title>
</head>
<body>
  {% if message %}
  <p style="color:red">{{ message }}</p>
  {% endif %}
  <form action="/signin" method="post">
    <legend>Please sign in:</legend>
    <p><input name="username" placeholder="Username" value="{{ username }}"></p>
    <p><input name="password" placeholder="Password" type="password"></p>
    <p><button type="submit">Sign In</button></p>
  </form>
</body>
</html>

#templates/signin-ok.html
<html>
<head>
  <title>Welcome, {{ username }}</title>
</head>
<body>
  <p>Welcome, {{ username }}!</p>
</body>
</html>

通过MVC，我们在Python代码中处理M：Model和C：Controller，而V：View是通过模板处理的，这样，我们就成功地把Python代码和HTML代码最大限度地分离了。
使用模板的另一大好处是，模板改起来很方便，而且，改完保存后，刷新浏览器就能看到最新的效果，这对于调试HTML、CSS和JavaScript的前端工程师来说实在是太重要了。

在Jinja2模板中，我们用{{ name }}表示一个需要替换的变量。很多时候，还需要循环、条件判断等指令语句，在Jinja2中，用{% ... %}表示指令。

比如循环输出页码：

{% for i in page_list %}
    <a href="/page/{{ i }}">{{ i }}</a>
{% endfor %}
如果page_list是一个list：[1, 2, 3, 4, 5]，上面的模板将输出5个超链接。

除了Jinja2，常见的模板还有：

Mako：用<% ... %>和${xxx}的一个模板；

Cheetah：也是用<% ... %>和${xxx}的一个模板；

Django：Django是一站式框架，内置一个用{% ... %}和{{ xxx }}的模板。

小结
有了MVC，我们就分离了Python代码和HTML代码。HTML代码全部放到模板里，写起来更有效率。
#-----------------------------------------------------------------------------------------
#异步IO
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143208573480558080fa77514407cb23834c78c6c7309000
#-----------------------------------------------------------------------------------------
#协程
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001432090171191d05dae6e129940518d1d6cf6eeaaa969000
Python对协程的支持是通过generator实现的。

在generator中，我们不但可以通过for循环来迭代，还可以不断调用next()函数获取由yield语句返回的下一个值。

但是Python的yield不但可以返回一个值，它还可以接收调用者发出的参数。

来看例子：

传统的生产者-消费者模型是一个线程写消息，一个线程取消息，通过锁机制控制队列和等待，但一不小心就可能死锁。

如果改用协程，生产者生产消息后，直接通过yield跳转到消费者开始执行，待消费者执行完毕后，切换回生产者继续生产，效率极高：

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
def consumer():
    r = ''
    while True:
        nFromProducer = yield r
        if not nFromProducer:
            return
        print('[CONSUMER] Consuming %s...' % nFromProducer)
        r = '200 OK'

def produce(c):
    c.send(None)
    n = 0
    while n < 5:
        n = n + 1
        print('[PRODUCER] Producing %s...' % n)
        r = c.send(n)
        print('[PRODUCER] Consumer return: %s' % r)
    c.close()

if __name__ == '__main__':
    c = consumer()
    produce(c)
输出：
[PRODUCER] Producing 1...
[CONSUMER] Consuming 1...
[PRODUCER] Consumer return: 200 OK
[PRODUCER] Producing 2...
[CONSUMER] Consuming 2...
[PRODUCER] Consumer return: 200 OK
[PRODUCER] Producing 3...
[CONSUMER] Consuming 3...
[PRODUCER] Consumer return: 200 OK
[PRODUCER] Producing 4...
[CONSUMER] Consuming 4...
[PRODUCER] Consumer return: 200 OK
[PRODUCER] Producing 5...
[CONSUMER] Consuming 5...
[PRODUCER] Consumer return: 200 OK

注意到consumer函数是一个generator，把一个consumer传入produce后：

首先调用c.send(None)启动生成器；

然后，一旦生产了东西，通过c.send(n)切换到consumer执行；

consumer通过yield拿到消息，处理，又通过yield把结果传回；

produce拿到consumer处理的结果，继续生产下一条消息；

produce决定不生产了，通过c.close()关闭consumer，整个过程结束。

整个流程无锁，由一个线程执行，produce和consumer协作完成任务，所以称为“协程”，而非线程的抢占式多任务。

最后套用Donald Knuth的一句话总结协程的特点：

“子程序就是协程的一种特例。”

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
def generatorTest():
    for i in range(5):
        n = yield i
        print("receiving %s" % n)

if __name__ == '__main__':
    t = generatorTest() #这是一个Generator
    t.send(None) #必须先对一个Generator进行send(None)，以启动Generator
    m = "abc"
    print(t.send(m)) #以后每次send值时，都是从yield开始执行，将入参赋值给yield等号左边的变量
    t.close()
#-----------------------------------------------------------------------------------------
#asyncio
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001432090954004980bd351f2cd4cc18c9e6c06d855c498000

asyncio是Python 3.4版本引入的标准库，直接内置了对异步IO的支持。

asyncio的编程模型就是一个消息循环。我们从asyncio模块中直接获取一个EventLoop的引用，然后把需要执行的协程扔到EventLoop中执行，就实现了异步IO。

用asyncio实现Hello world代码如下：

@asyncio.coroutine把一个generator标记为coroutine类型，然后，我们就把这个coroutine扔到EventLoop中执行。

hello()会首先打印出Hello world!，然后，yield from语法可以让我们方便地调用另一个generator。由于asyncio.sleep()也是一个coroutine，所以线程不会等待asyncio.sleep()，而是直接中断并执行下一个消息循环。当asyncio.sleep()返回时，线程就可以从yield from拿到返回值（此处是None），然后接着执行下一行语句。

把asyncio.sleep(1)看成是一个耗时1秒的IO操作，在此期间，主线程并未等待，而是去执行EventLoop中其他可以执行的coroutine了，因此可以实现并发执行。

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import asyncio

@asyncio.coroutine
def hello():
    print("Hello world!")
    # 异步调用asyncio.sleep(1):
    r = yield from asyncio.sleep(1)
    print("Hello again!")

if __name__ == '__main__':
    # 获取EventLoop:
    loop = asyncio.get_event_loop()
    # 执行coroutine
    loop.run_until_complete(hello())
    loop.close()

我们用Task封装两个coroutine试试：
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import threading
import asyncio

@asyncio.coroutine
def hello(s):
    print('Hello world! (%s)' % threading.currentThread())
    print("Before %s" % s)
    yield from asyncio.sleep(3)
    print("After %s" % s)
    print('Hello again! (%s)' % threading.currentThread())

if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    tasks = [hello(1), hello(2), hello(3), hello(4)]
    loop.run_until_complete(asyncio.wait(tasks))
    loop.close()

观察执行过程：
Hello world! (<_MainThread(MainThread, started 2640)>)
Before 4
Hello world! (<_MainThread(MainThread, started 2640)>)
Before 2
Hello world! (<_MainThread(MainThread, started 2640)>)
Before 1
Hello world! (<_MainThread(MainThread, started 2640)>)
Before 3
约等3秒
After 4
Hello again! (<_MainThread(MainThread, started 2640)>)
After 1
Hello again! (<_MainThread(MainThread, started 2640)>)
After 2
Hello again! (<_MainThread(MainThread, started 2640)>)
After 3
Hello again! (<_MainThread(MainThread, started 2640)>)
由打印的当前线程名称可以看出，两个coroutine是由同一个线程并发执行的。

如果把asyncio.sleep()换成真正的IO操作，则多个coroutine就可以由一个线程并发执行。

我们用asyncio的异步网络连接来获取sina、sohu和163的网站首页：
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import asyncio

@asyncio.coroutine
def wget(host):
    print('wget %s...' % host)
    connect = asyncio.open_connection(host, 80)

    print("Before yield from connect processing host [%s]" % host)
    reader, writer = yield from connect
    print("After yield from connect processing host [%s]" % host)

    header = 'GET / HTTP/1.0\r\nHost: %s\r\n\r\n' % host
    writer.write(header.encode('utf-8'))
    yield from writer.drain()
    while True:
        line = yield from reader.readline()
        if line == b'\r\n':
            break
        print('%s header > %s' % (host, line.decode('utf-8').rstrip()))
    # Ignore the body, close the socket
    writer.close()

if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    tasks = [wget(host) for host in ['www.sina.com.cn', 'www.sohu.com', 'www.163.com']]
    loop.run_until_complete(asyncio.wait(tasks))
    loop.close()
输出：
wget www.sohu.com...
Before yield from connect processing host [www.sohu.com]
wget www.sina.com.cn...
Before yield from connect processing host [www.sina.com.cn]
wget www.163.com...
Before yield from connect processing host [www.163.com]
After yield from connect processing host [www.sohu.com]
www.sohu.com header > HTTP/1.1 200 OK
www.sohu.com header > Content-Type: text/html;charset=UTF-8
www.sohu.com header > Connection: close
www.sohu.com header > Server: nginx
www.sohu.com header > Date: Sun, 15 Apr 2018 07:02:14 GMT
www.sohu.com header > Cache-Control: max-age=60
www.sohu.com header > X-From-Sohu: X-SRC-Cached
www.sohu.com header > Content-Encoding: gzip
www.sohu.com header > FSS-Cache: HIT from 13539701.18454911.21477824
www.sohu.com header > FSS-Proxy: Powered by 10131777.11639115.18069848
After yield from connect processing host [www.sina.com.cn]
After yield from connect processing host [www.163.com]
www.sina.com.cn header > HTTP/1.1 200 OK
www.sina.com.cn header > Server: nginx
www.sina.com.cn header > Date: Sun, 15 Apr 2018 07:02:36 GMT
www.sina.com.cn header > Content-Type: text/html
www.sina.com.cn header > Content-Length: 603236
www.sina.com.cn header > Connection: close
www.sina.com.cn header > Last-Modified: Sun, 15 Apr 2018 07:00:39 GMT
www.sina.com.cn header > Vary: Accept-Encoding
www.sina.com.cn header > X-Powered-By: shci_v1.03
www.sina.com.cn header > Expires: Sun, 15 Apr 2018 07:03:28 GMT
www.sina.com.cn header > Cache-Control: max-age=60
www.sina.com.cn header > Age: 2
www.sina.com.cn header > Via: http/1.1 ctc.ningbo.ha2ts4.97 (ApacheTrafficServer/6.2.1 [cHs f ]), http/1.1 ctc.ningbo.ha2ts4.106 (ApacheTrafficServer/6.2.1 [cHs f ])
www.sina.com.cn header > X-Via-Edge: 152377575604889f65d65eebeee734a4224ac
www.sina.com.cn header > X-Cache: HIT.106
www.sina.com.cn header > X-Via-CDN: f=edge,s=ctc.ningbo.ha2ts4.102.nb.sinaedge.com,c=101.93.246.137;f=Edge,s=ctc.ningbo.ha2ts4.106,c=115.238.190.102
www.163.com header > HTTP/1.1 200 OK
www.163.com header > Expires: Sun, 15 Apr 2018 07:03:56 GMT
www.163.com header > Date: Sun, 15 Apr 2018 07:02:36 GMT
www.163.com header > Server: nginx
www.163.com header > Content-Type: text/html; charset=GBK
www.163.com header > Vary: Accept-Encoding,User-Agent,Accept
www.163.com header > Cache-Control: max-age=80
www.163.com header > X-Via: 1.1 qzh148:2 (Cdn Cache Server V2.0), 1.1 PSshqzdx2mj185:2 (Cdn Cache Server V2.0)
www.163.com header > Connection: close

小结
asyncio提供了完善的异步IO支持；

异步操作需要在coroutine中通过yield from完成；

多个coroutine可以封装成一组Task然后并发执行。

参考如下学习协程
#http://python.jobbole.com/87310/
#-----------------------------------------------------------------------------------------
#async/await
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00144661533005329786387b5684be385062a121e834ac7000

用asyncio提供的@asyncio.coroutine可以把一个generator标记为coroutine类型，然后在coroutine内部用yield from调用另一个coroutine实现异步操作。

为了简化并更好地标识异步IO，从Python 3.5开始引入了新的语法async和await，可以让coroutine的代码更简洁易读。

请注意，async和await是针对coroutine的新语法，要使用新的语法，只需要做两步简单的替换：

把@asyncio.coroutine替换为async；
把yield from替换为await。
让我们对比一下上一节的代码：

@asyncio.coroutine
def hello():
    print("Hello world!")
    r = yield from asyncio.sleep(1)
    print("Hello again!")
用新语法重新编写如下：

async def hello():
    print("Hello world!")
    r = await asyncio.sleep(1)
    print("Hello again!")
#-----------------------------------------------------------------------------------------
#aiohttp
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014320981492785ba33cc96c524223b2ea4e444077708d000

#-----------------------------------------------------------------------------------------
#sklearn 学习 
https://www.bilibili.com/video/av17003173/?p=8

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from __future__ import print_function
from sklearn import datasets
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier

if __name__ == '__main__':
    iris = datasets.load_iris()
    iris_X = iris.data
    iris_y = iris.target

    # print(iris_X[:2, :])
    # print(iris_y)

    X_train, X_test, y_train, y_test = train_test_split(iris_X, iris_y, test_size=0.3)
    # print(y_train)

    knn = KNeighborsClassifier() #生成KNN分类器
    knn.fit(X_train, y_train) #训练数据集
    y_predict = knn.predict(X_test) #根据测试集生成预测结果
    print(y_predict)
    print(y_test)

    # 这是自己写的评估函数
    correctNum = sum(map(lambda x, y: x == y, y_predict, y_test))
    correctRatio = correctNum / len(y_test)
    print(correctRatio)

    # Sklearn自带的评估函数
    score = knn.score(X_test, y_test)
    print(score)
#-----------------------------------------------------------------------------------------
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from sklearn import datasets
from sklearn.linear_model import LinearRegression

if __name__ == '__main__':
    load_data = datasets.load_boston()
    data_X = load_data.data
    data_y = load_data.target
    #     print(data_X)
    #     print(data_y)
    model = LinearRegression()
    model.fit(data_X, data_y)
    
#     print(model.predict(data_X[:4]))
#     print(data_y[:4])

#     print(model.coef_)
#     print(model.intercept_)
#     print(model.get_params())
    print(model.score(data_X, data_y)) #R^2 coefficient of determination
#-----------------------------------------------------------------------------------------
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from sklearn import datasets
from sklearn.linear_model import LinearRegression
import matplotlib.pyplot as plt

if __name__ == '__main__':
    X, y = datasets.make_regression(n_samples=200, n_features=1, n_targets=1, noise=1)
    plt.scatter(X, y)
    plt.show()
    
    model = LinearRegression()
    model.fit(X, y)
    
    score = model.score(X, y)
    print(model.predict(X[:4]))
    print(y[:4])
    print(score)
#-----------------------------------------------------------------------------------------
7. normalization 标准化数据

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from sklearn import preprocessing
import numpy as np
from sklearn.datasets import make_classification
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.svm import SVC

if __name__ == '__main__':
    # a = np.array([[10, 2.7, 3.6],
    #              [-100, 5, -2],
    #              [120, 20, 40]], dtype=np.float64)
    # print(a)
    # print(preprocessing.scale(a))

    X, y = make_classification(n_samples=300, n_features=2, n_redundant=0, n_informative=2, random_state=22,
                               n_clusters_per_class=1, scale=100)
    # plt.scatter(X[:, 0], X[:, 1], c=y)
    # plt.show()
    # X = preprocessing.scale(X) #去掉这行标准化，测试准确率大大下降
    # X = preprocessing.minmax_scale(X, feature_range=(-1, 1))
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=.3)
    clf = SVC()
    clf.fit(X_train, y_train)
    print(clf.score(X_test, y_test))
#-----------------------------------------------------------------------------------------
8. cross_validation 1

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from sklearn.model_selection import cross_val_score
from sklearn.datasets import load_iris # iris数据集
from sklearn.model_selection import train_test_split # 分割数据模块
from sklearn.neighbors import KNeighborsClassifier # K最近邻(kNN，k-NearestNeighbor)分类算法
import matplotlib.pyplot as plt

if __name__ == '__main__':
    # 加载iris数据集
    iris = load_iris()
    X = iris.data
    y = iris.target

    # 分割数据并
    # X_train, X_test, y_train, y_test = train_test_split(X, y, random_state=4)
    #
    # # 建立模型
    # knn = KNeighborsClassifier(n_neighbors=5)
    #
    # # 训练模型
    # knn.fit(X_train, y_train)
    #
    # # 将准确率打印出
    # print(knn.score(X_test, y_test))

    # 分成多组测试集和训练集
    # knn = KNeighborsClassifier(n_neighbors=5)
    # scores = cross_val_score(knn, X, y, cv=5, scoring='accuracy')
    # print(scores.mean()) # print(scores)

    k_range = range(1, 31)
    k_scores = []
    for k in k_range:
        knn = KNeighborsClassifier(n_neighbors=k)
        # scores = cross_val_score(knn, X, y, cv=10, scoring='accuracy') # for classification
        # k_scores.append(scores.mean())
        loss = -cross_val_score(knn, X, y, cv=10, scoring='mean_squared_error')  # for regression
        k_scores.append(loss.mean())

    plt.plot(k_range, k_scores)
    plt.xlabel('Value of K for KNN')
    plt.ylabel('Cross-Validated Accuracy')
    plt.show()

#-----------------------------------------------------------------------------------------
9. cross_validation 2

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import matplotlib.pyplot as plt
import numpy as np
from sklearn.datasets import load_digits
from sklearn.model_selection import learning_curve
from sklearn.svm import SVC

if __name__ == '__main__':
    digits = load_digits()
    X = digits.data
    y = digits.target

    train_sizes, train_loss, test_loss = learning_curve(
        SVC(gamma=0.001), X, y, cv=10, scoring='mean_squared_error',
        train_sizes=[0.1, 0.25, 0.5, 0.75, 1])

    # 平均每一轮所得到的平均方差(共5轮，分别为样本10%、25%、50%、75%、100%)
    train_loss_mean = -np.mean(train_loss, axis=1)
    test_loss_mean = -np.mean(test_loss, axis=1)

    plt.plot(train_sizes, train_loss_mean, 'o-', color="r", label="Training")
    plt.plot(train_sizes, test_loss_mean, 'o-', color="g", label="Cross-validation")

    plt.xlabel("Training examples")
    plt.ylabel("Loss")
    plt.legend(loc="best")
    plt.show()

#-----------------------------------------------------------------------------------------
10. cross_validation 3
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import matplotlib.pyplot as plt
import numpy as np
from sklearn.datasets import load_digits
from sklearn.model_selection import validation_curve
from sklearn.svm import SVC

if __name__ == '__main__':
    digits = load_digits()
    X = digits.data
    y = digits.target

    param_range = np.logspace(-6, -2.3, 5)
    train_loss, test_loss = validation_curve(
        SVC(), X, y, param_name='gamma', param_range=param_range, cv=10,
        scoring='mean_squared_error')

    # 平均每一轮所得到的平均方差(共5轮，分别为样本10%、25%、50%、75%、100%)
    train_loss_mean = -np.mean(train_loss, axis=1)
    test_loss_mean = -np.mean(test_loss, axis=1)

    plt.plot(param_range, train_loss_mean, 'o-', color="r", label="Training")
    plt.plot(param_range, test_loss_mean, 'o-', color="g", label="Cross-validation")

    plt.xlabel("Gamma")
    plt.ylabel("Loss")
    plt.legend(loc="best")
    plt.show()

#-----------------------------------------------------------------------------------------
11 save
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from sklearn import svm
from sklearn import datasets
import pickle

from sklearn.externals import joblib

if __name__ == '__main__':
    clf = svm.SVC()
    digits = datasets.load_digits()
    X, y= digits.data, digits.target
    clf.fit(X, y)
    #
    # save
    # # method 1: pickle
    # with open('save/clf.pickle', 'wb') as f:
    #     pickle.dump(clf, f)
    #
    # restore
    # with open('save/clf.pickle', 'rb') as f:
    #     clf2 = pickle.load(f)
    #     print(clf2.predict(X[0:1]))

    # method 2: joblib
    # save
    joblib.dump(clf, 'save/clf.pkl')
    # restore
    clf3 = joblib.load('save/clf.pkl') #速度会更快些
    print(clf3.predict(X[0:1]))
#-----------------------------------------------------------------------------------------
# str.format https://www.cnblogs.com/Alexzzzz/p/6832253.html
format方法被用于字符串的格式化输出。

 print('{0}+{1}={2}'.format(1,2,1+2))   #in
1+2=3   #out
可见字符串中大括号内的数字分别对应着format的几个参数。

若省略数字：
print('{}+{}={}'.format(1,2,1+2))   #in
可以得到同样的输出结果。但是替换顺序默认按照[0],[1],[2]...进行。

若替换{0}和{1}：
print('{1}+{0}={2}'.format(1,2,1+2))   #in
2+1=3   #out

输出字符串:
print('{0} am {1}'.format('i','alex'))  
i am alex   #out

输出参数的值:
length = 4
name = 'alex'
print('the length of {0} is {1}'.format(name,length))
the length of alex is 4

精度控制：
print('{0:.3}'.format(1/3))
0.333

宽度控制：
print('{0:7}{1:7}'.format('use','python'))
use    python 

精宽度控制(宽度内居左)：
print('{0:<7.3}..'.format(1/3))   
0.333  ..
其实精宽度控制很类似于C中的printf函数。
同理'>'为居右，'^'为居中。符号很形象。

补全：
#!/usr/bin/python
#python3.6
print('{0:0>3}'.format(1)) #居右，左边用0补全
print('{0:{1}>3}'.format(1,0))  #也可以这么写
#当输出中文使用空格补全的时候，系统会自动调用英文空格，这可能会造成不对齐
#for example
blog = {'1':'中国石油大学','2':'浙江大学','3':'南京航空航天大学'}
print('不对齐：')
print('{0:^4}\t\t{1:^8}'.format('序号','名称'))
for no,name in blog.items(): #字典的items()方法返回一个键值对，分别赋值给no和name
    print('{0:^4}\t\t{1:^8}'.format(no,name))
print('\n对齐：')
print('{0:^4}\t\t{1:{2}^8}'.format('序号','名称',chr(12288))) #chr(12288)为UTF-8中的中文空格
for no,name in blog.items():
    print('{0:^4}\t\t{1:{2}^8}'.format(no,name,chr(12288)))
#out
001
001
不对齐：
 序号              名称   
 1               中国石油大学 
 2                浙江大学  
 3              南京航空航天大学

对齐：
 序号           　　　名称　　　
 1              　中国石油大学　
 2              　　浙江大学　　
 3              南京航空航天大学
#-----------------------------------------------------------------------------------------
# pdb调试 https://www.cnblogs.com/xiaohai2003ly/p/8529472.html
#pytest.py
def add(a, b):
    return a + b

s = '5'
n = int(s)
c = add(n, 10)
print(c)

Python 调试器之pdb
使用PDB的方式有两种:
1. 单步执行代码,通过命令 python -m pdb xxx.py 启动脚本，进入单步执行模式
 pdb命令行：
1）进入命令行Debug模式，python -m pdb xxx.py
2）h：（help）帮助
3）w：（where）打印当前执行堆栈
4）d：（down）执行跳转到在当前堆栈的深一层（进入到下一个深层堆栈，可以理解为下一个函数）
5）u：（up）执行跳转到当前堆栈的上一层（返回到上一层堆栈，可以理解为删除当前堆栈桢）
6）b：（break）添加断点 （这里添加的断点，只要程序不退出，则断点一直存在，如果exit，或者q，则设置的断点会失效）
             b 列出当前所有断点，和断点执行到统计次数
             b line_no：当前脚本的line_no行添加断点
             b filename:line_no：脚本filename的line_no行添加断点
             b function：在函数function的第一条可执行语句处添加断点
7）tbreak：（temporary break）临时断点
             在第一次执行到这个断点之后，就自动删除这个断点，用法和b一样
8）cl：（clear）清除断点
            cl 清除所有断点
            cl bpnumber1 bpnumber2... 清除断点号为bpnumber1,bpnumber2...的断点
            cl lineno 清除当前脚本lineno行的断点
            cl filename:line_no 清除脚本filename的line_no行的断点
9）disable：停用断点，参数为bpnumber，和cl的区别是，断点依然存在，只是不启用
10）enable：激活断点，参数为bpnumber
11）s：（step）执行下一条命令 （执行下一条命令，如果下一句为函数，则会进入到函数中）
            如果本句是函数调用，则s会执行到函数的第一句
12）n：（next）执行下一条语句 （执行下一条命令，如果下一句为函数，则会执行完函数）
            如果本句是函数调用，则执行函数，接着执行当前执行语句的下一条。
13）r：（return）执行当前运行函数到结束 （已经走到当前函数的结束语句，无法再使用j linenumber来在当前函数内跳转了）
14）c：（continue）继续执行，直到遇到下一条断点
15）l：（list）列出源码 （还有ll语句）
             l 列出当前执行语句周围11条代码
             l first 列出first行周围11条代码
             l first, second 列出first--second范围的代码，如果second<first，second将被解析为行数 （如l 10,15，将列出10到15行）
16）a：（args）列出当前执行函数的函数
17）p expression：（print）输出expression的值
18）pp expression：好看一点的p expression
19）run：重新启动debug，相当于restart
20）q：（quit）退出debug
21）j lineno：（jump）设置下条执行的语句函数 （单纯的跳转语句，可以跳转到执行过的语句来重新执行，也可以跳转到后续语句，则中间跳过的语句都会略过不执行）
            只能在堆栈的最底层跳转，向后重新执行，向前可直接执行到行号
22）unt：（until）执行到下一行（跳出循环），或者当前堆栈结束 （执行到特定语句，则接行号，如：unt 7，使用unt也可以执行到当前函数的最后一行，但是并没有走到函数结束语句，可以使用j linenumber来跳转）
23）condition bpnumber conditon，给断点设置条件，当参数condition返回True的时候bpnumber断点有效，否则bpnumber断点无效 （只能在已设置的断点上增加条件，并且第2个参数是断点号，如：condition 3 a == 4，给3号断点增加条件a == 4时）

注意：
1：直接输入Enter，会执行上一条命令；
2：输入PDB不认识的命令，PDB会把他当做Python语句在当前环境下执行；



2. pdb单步执行太麻烦了，所以第二种方法是import pdb 之后，直接在代码里需要调试的地方放一个pdb.set_trace()，就可以设置一个断点， 程序会在pdb.set_trace()暂停并进入pdb调试环境，可以用pdb 变量名查看变量，或者c继续运行
#pdbtest.py
import pdb

def add(a, b):
    print("hello, world")
    print("good day")
    pdb.set_trace()
    return a + b

def loop(n):
    i = 1
    pdb.set_trace()
    while (i < n):
        m = i * i
        print(m)
        i += 1
    print("i = {}".format(i))

s = '2'
n = int(s)
c = add(n, 10)
c += c
loop(c)
print(c)

如果使用参数 -m pdb，则程序会进入调试模式，会进停止在第一行，并且程序运行过程中的print()信息不会显示出来：
python -m pdb pdbtest.py

如果不使用参数，则程序会直接正常运行，并且会停止在打了pdb.set_trace()的后一句，也会把程序中的print()信息正常输出到控制台：
python pdbtest.py


#https://blog.csdn.net/qq_21398167/article/details/52325464
在调试的时候动态改变值 。在调试的时候可以动态改变变量的值，具体如下实例。需要注意的是下面有个错误，原因是 b 已经被赋值了，如果想重新改变 b 的赋值，则应该使用！ B。
清单 9. 在调试的时候动态改变值
[root@rcc-pok-idg-2255 ~]# python epdb2.py 
 > /root/epdb2.py(10)?() 
 -> b = "bbb"
 (Pdb) var = "1234"
 (Pdb) b = "avfe"
 *** The specified object '= "avfe"' is not a function 
 or was not found along sys.path. 
 (Pdb) !b="afdfd"
 (Pdb)
#-----------------------------------------------------------------------------------------
# python 3.5引入的TypeHint 只能做静态检查，无法对输入或输出报错
# https://blog.csdn.net/sunt2018/article/details/83022493
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# 入参类型是str，返回类型是str
def get(param: str) -> object:
    if param == "hello":
        return 1
    return param * 2


class Test(object):
    @staticmethod
    def hello() -> None:
        print("hello")


if __name__ == '__main__':
    my_str = get("hello")
    print(my_str)

    i: int = get(5)
    print(i)

    Test.hello()
    hello: Test = Test() # 所有的变量后都可以冒号来指明类型
    hello.hello()
#-----------------------------------------------------------------------------------------
# Python3中使用urllib访问https网站
import ssl
import urllib.request

if __name__ == '__main__':
    url = "http://baike.baidu.com/item/Python/407313"

    ssl._create_default_https_context = ssl._create_unverified_context
    with urllib.request.urlopen(url) as response1:
        print(response1.getcode())
        print(len(response1.read()))
#-----------------------------------------------------------------------------------------
# Python3 urljoin
#
from urllib.parse import urljoin

if __name__ == '__main__':
    url = r'https://baike.baidu.com/item/Python/407313'
    new_url = r'/item/秒懂星课堂'
    new_full_url = urljoin(url, new_url)  # 拼接URL
    print(new_full_url)
输出：
https://baike.baidu.com/item/秒懂星课堂

#
from posixpath import normpath
from urllib.parse import urljoin
from urllib.parse import urlparse
from urllib.parse import urlunparse


def myjoin(base, url):
    url1 = urljoin(base, url)
    arr = urlparse(url1)
    path = normpath(arr[2])
    return urlunparse((arr.scheme, arr.netloc, path, arr.params, arr.query, arr.fragment))


if __name__ == '__main__':
    print(myjoin("http://www.baidu.com/lmn", "def/lmn/abc.html"))
    print(myjoin("http://www.baidu.com", "/../../abc.html"))
    print(myjoin("http://www.baidu.com/xxx", "./../../abc.html"))
    print(myjoin("http://www.baidu.com", "abc.html?key=value&m=x"))
输出：
http://www.baidu.com/def/lmn/abc.html
http://www.baidu.com/abc.html
http://www.baidu.com/abc.html
http://www.baidu.com/abc.html?key=value&m=x
#-----------------------------------------------------------------------------------------
# urlopen读出来的bytes和string转换
# 参考 https://blog.csdn.net/haoxizh/article/details/44649451
import contextlib
import urllib.request

if __name__ == '__main__':
    url = 'http://www.baidu.com'
    with contextlib.closing(urllib.request.urlopen(url)) as response:  # 使用contextlib.closing()也可以，直接使用with as 也可以，说明urlopen()返回的对象既有__enter__()方法和__exit__()，又有close()方法，可以通过help(response)来查看
        page = response.read()
        the_page = page.decode("UTF-8")
        b_page = the_page.encode("UTF-8")
        print(f"page type is {type(page)}, the_page type is {type(the_page)}, b_page type is {type(b_page)}")
输出：
page type is <class 'bytes'>, the_page type is <class 'str'>, b_page type is <class 'bytes'>
#-----------------------------------------------------------------------------------------
# unitteset 方法 https://blog.csdn.net/xiaoxinyu316/article/details/53170463
# 1、通过unittest.main()来执行测试用例的方式：
#!/usr/bin/env python3
# -*- coding: utf-8 -*-


import unittest


class UCTestCase(unittest.TestCase):
    def setUp(self):
        # 测试前需执行的操作
        print("setUp")
        pass

    def tearDown(self):
        # 测试用例执行完后所需执行的操作
        print("tearDown")
        pass

    # 测试用例1
    def testCreateFolder(self):
        # 具体的测试脚本
        print("testCreateFolder")

    # 测试用例2
    def testDeleteFolder(self): # 只有以test开始的函数，才会运行，如果把函数名改为t2estXXX，则不会运行
        # 具体的测试脚本
        print("testDeleteFolder")


if __name__ == "__main__":
    unittest.main()

    
# 2、通过testsuit来执行测试用例的方式：
if __name__ == "__main__":
    # 构造测试集
    suite = unittest.TestSuite()
    suite.addTest(UCTestCase("testCreateFolder"))
    # suite.addTest(UCTestCase("testDeleteFolder"))
    # 执行测试
    runner = unittest.TextTestRunner()
    runner.run(suite)

# 在PyCharm中运行
经测试，直接使用PyCharm Ctrl+Shift+F10运行，即使只add一个Test，仍会运行全部测试用例，此时是用的Configuration里的Python tests运行的。

如果将Configuration中修改为Python运行，则会按正常的addTest来运行。

# 在命令行中运行
可以以命令行方式运行，python src\test\unitest_learn.py 此时只会运行一个测试用例，如下：
D:\Program Files\JetBrains\PythonProject\Py3TestProject>python src\test\unitest_learn.py
setUp
testCreateFolder
tearDown
.
----------------------------------------------------------------------
Ran 1 test in 0.002s

OK

如果加参数 -m unittest，则仍会运行全部用例：
D:\Program Files\JetBrains\PythonProject\Py3TestProject>python -m unittest src\test\unitest_learn.py
setUp
testCreateFolder
tearDown
.setUp
testDeleteFolder
tearDown
.
----------------------------------------------------------------------
Ran 2 tests in 0.005s

OK

说明：以suite.addTest(UCTestCase("testCreateFolder"))这种方式来运行测试用例，即使函数名不以test开关，仍可以正常运行。


# 3、通过testLoader方式：
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import unittest


class TestCase1(unittest.TestCase):
    # def setUp(self):
    # def tearDown(self):
    def testCase1(self):
        print('aaa')

    def testCase2(self):
        print('bbb')


class TestCase2(unittest.TestCase):
    # def setUp(self):
    # def tearDown(self):
    def testCase1(self):
        print('aaa1')

    def testCase2(self):
        print('bbb1')


if __name__ == "__main__":
    # 此用法可以同时测试多个类
    suite1 = unittest.TestLoader().loadTestsFromTestCase(TestCase1)
    suite2 = unittest.TestLoader().loadTestsFromTestCase(TestCase2)
    suite = unittest.TestSuite([suite1, suite2])
    unittest.TextTestRunner(verbosity=2).run(suite)

说明：unittest.TextTestRunner(verbosity=2).run(suite)运行用例，TextTestRunner类将用例执行的结果以text形式输出，verbosity默认值为1，不限制完整结果，即单个用例成功输出’.’,失败输出’F’,错误输出’E’;verbosity=2将输出完整的信息，verbosity=2是指测试结果的输出的详细程度，有0-6级，具体代码实现可看Python27\Lib\unittest\runner.py源代码。 （https://cloud.tencent.com/info/2ab386c4b78655f1f5d277023d702983.html）
#-----------------------------------------------------------------------------------------
# scrapy xpath()中的/与//的区别 https://blog.csdn.net/changer_WE/article/details/84553986

from lxml import etree
myhtml = """
    <body>
        <div id="div1">
            <div id="div2">
                <p>hello world</p>
                <div id="div3">
                    <a href="xxxA.com">转到A</a>
                    <a href="xxxB.com">转到B</a>
                </div>    
            </div>
            <h1>你好世界</h1>
        </div>
    </body>
    """
selector = etree.HTML(myhtml)
content1 = selector.xpath('//div[@id="div1"]/p/text()')  # 用/提取p标签的内容，提取不到 
print(content1)      # /情况下的xpath的内容
content2 = selector.xpath('//div[@id="div1"]//p/text()')  # 用//提取p标签的内容
print(content2)      # //情况下的xpath的内容
content3 = selector.xpath('//div[@id="div1"]/div[@id="div2"]/div[@id="div3"]/a/text()')  # 用/提取a标签的内容
print(content3)
content4 = selector.xpath('//div[@id="div1"]//a/text()')  # 用//提取a标签的内容

这个实验可以看出：
/ ? ?就类似于我们所说的绝对路径
// ? 是一种容错性更高的写法，可以不很具体，可以跨过很多层
所以说，爬虫可以用 ?// ? 写的，就尽量别用 ?/ ?，提高自己代码的一些容错，指不定就会出现我自己做的时候的错误（一些网页div包着div）
这三种模式：

//a//b/@abc 指的是文档中所有a元素的属性为abc的后代b元素（包括子代元素）（多级）；
//a/b/@abc 指的是文档中所有a元素的属性为abc的子代b元素（一级）；
/a/b/@abc 指的是根节点b元素的属性为abc的子代b元素（一级）；

通常用第一种就好了
#-----------------------------------------------------------------------------------------
# pymysql连接mysql http://www.runoob.com/python3/python3-mysql.html
# mysql_test.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import pymysql

if __name__ == '__main__':
    connect = pymysql.connect(host='localhost', port=3306, user='root', passwd='root123',
                              db='hibernate', charset='utf8')
    cursor = connect.cursor()
    # 使用 execute()  方法执行 SQL 查询
    cursor.execute("SELECT VERSION()")

    # 使用 fetchone() 方法获取单条数据.
    # data = cursor.fetchone()
    #
    # print("Database version : %s " % data)  # Database version : 8.0.11 

    data = {'serial_number': '248', 'movie_name': '枪火', 'introduce': '1999/香港/剧情动作犯罪', 'star': '8.7',
            'evaluate': '126052人评价', 'description': '一群演技精湛的戏骨，奉献出一个精致的黑帮小品，成就杜琪峰群戏的巅峰之作。'}

    sql_list = """
    insert into douban_movie(serial_number, movie_name, introduce, star, evaluate, description)
        values(%s, %s, %s, %s, %s, %s)
    """

    cursor.execute(sql_list, (data['serial_number'], data['movie_name'], data['introduce'], data['star'], data['evaluate'], data['description']))

    # sql_dict = """
    # insert into douban_movie(%(name)s, %(name)s, %(name)s, %(name)s, %(name)s, %(name)s)
    #     values(%s, %s, %s, %s, %s, %s)
    # """
    # cursor.execute(sql_dict, data)  # 没找到dict使用样例

    connect.commit()
    # 关闭数据库连接
    connect.close()
#-----------------------------------------------------------------------------------------
# Python不能使用函数重载 https://www.cnblogs.com/erbaodabao0611/p/7490439.html
def test(a):
    print(a)


def test(a, b):
    print(a, b)
    
test(1)
test(1, 2)
运行报错：
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 33, in <module>
    test(1)
TypeError: test() missing 1 required positional argument: 'b'
#-----------------------------------------------------------------------------------------
# 判断字符串里是否有中文
# main.py
def has_chinese(word):
    for ch in word:
        if '\u4e00' <= ch <= '\u9fff':
            return True
    return False

# test_has_chinese.py
from unittest import TestCase

from src.test.main import has_chinese


class TestHas_chinese(TestCase):
    def test_str_all_chars(self):
        s = 'abc'
        self.assertFalse(has_chinese(s))

    def test_str_has_chinese(self):
        s = '你好，world'
        self.assertTrue(has_chinese(s))

#-----------------------------------------------------------------------------------------
# 299. Bulls and Cows https://leetcode.com/problems/bulls-and-cows/
#
import collections


class Solution:
    def getHint(self, secret: str, guess: str) -> str:
        # 极简算法
        A = sum(s == g for s, g in zip(secret, guess))
        B = sum((collections.Counter(secret) & collections.Counter(guess)).values()) - A
        return "{A}A{B}B".format(A=A, B=B)

#
from unittest import TestCase

from src.test.main import Solution


class TestSolution(TestCase):
    def test_getHint(self):
        output = Solution().getHint("1807", "7810")
        self.assertEqual(output, '1A3B')
#-----------------------------------------------------------------------------------------
# 列表推导式构造dict，map函数构造dict
if __name__ == '__main__':
    d1 = {i: i for i in [1, 2]}
    print(d1)
    d2 = dict(map(lambda i: (i, i), [1, 2]))
    print(d2)

#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------

