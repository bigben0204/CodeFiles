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

