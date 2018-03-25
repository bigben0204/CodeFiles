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

Warning Because of this feature, scrolling through cells instead of accessing them directly will create them all in memory, even if you don��t assign them a value.
Something like
>>> for i in xrange(0,100):
...             for j in xrange(0,100):
...                     ws.cell(row = i, column = j)
will create 100x100 cells in memory, for nothing.
However, there is a way to clean all those unwanted cells, we��ll see that later.

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
#û������ͨ����
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

If it��s a list: all values are added in order, starting from the first column
If it��s a dict: values are assigned to the columns indicated by the keys (numbers or letters)
Parameters:	
list_or_dict (list/tuple or dict) �C list or dict containing values to append
Usage:

append([��This is A1��, ��This is B1��, ��This is C1��])
or append({��A�� : ��This is A1��, ��C�� : ��This is C1��})
or append({0 : ��This is A1��, 2 : ��This is C1��})
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

Usage: cell(coodinate=��A15��) or cell(row=15, column=1)

If coordinates are not given, then row and column must be given.

Cells are kept in a dictionary which is empty at the worksheet creation. Calling cell creates the cell in memory when they are first accessed, to reduce memory usage.

Parameters:	
coordinate (string) �C coordinates of the cell (e.g. ��B12��)
row (int) �C row index of the cell (e.g. 4)
column (int) �C column index of the cell (e.g. 3)
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
    name[1::2] = list("ABCDE")#Ҫ������ֶ�������Ͱ��ղ����ָ�����ͬ
    print name 	
	
    name = list("helloworld")
    print name.count("l")

    a = [1, 2, 3]
    b = [4, 5, 6]
    a.extend(b)
	#a[len(a):] = b #�ɶ��Բ���extend
    print a	
    print a.index(2)
	a.insert(2, "four")#a[2:2] = ["four"]#�ɶ��Բ���insert
	a.pop()
	a.pop(2)#pop������Ψһһ�������޸��б��ַ���Ԫ��ֵ������None�����б���
	a.remove(1)
	a.reverse()#list(reversed(a))
	a.sort()
	
	a = ["a", 1, 3, 2]
    b = a[:]#����Ч�ʵĸ��������б�ķ�����ֻ�Ǽ򵥵İ�a��ֵ��b��û�õ�
    b.sort()
	
	��һ�ֻ�ȡ��������б��������ǣ����������κ����У��ܷ���һ���б�
	b = sorted(a)
	
	b.sort(cmp)#����Ĭ�ϵıȽϺ������õ����ڽ�����cmp
	
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
#pythonѹ���ͽ�ѹ��
import zipfile
    f = zipfile.ZipFile("db.zip", "w", zipfile.ZIP_DEFLATED)
    f.write("db.xlsx")
    f.write("main.py")
    f.close()
	
    f = zipfile.ZipFile("db.zip", "r")
    f.extractall("c:\Users\Ben\Desktop", ["db.xlsx"])#�����ڶ����������ѹ�������ļ�
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
    fileMain = f.read("main.py")#���������fileMain�ǡ�str����������ĳ�fileMain = f.read("db.xlsx")����ʱ�����Ķ�����excel�ļ���ӡ����������
    print fileMain
    f.close()
	
#��zip.exeʵ��ѹ��
#����Ŀ¼������������Ŀ¼��δ����ѹ������
import os
if __name__ == '__main__':
    cmdOrder = r'zip -rJ "d:\Program Files\eclipse workspace\PyLearngingProgram\main\abc.zip" "d:\Program Files\eclipse workspace\PyLearngingProgram\main\dbDir\*.*"' #��ʱ����Ҫ��zip.exe����python����Ŀ¼���߻�����������Ŀ¼��
    os.system(cmdOrder)
	
#���������Ŀ¼����������Ҫ������Ӧ���ļ����У�����Ҫ���ļ����������
# encoding=utf-8
import os
if __name__ == '__main__':
    dstDir = r"d:\Program Files\eclipse workspace\PyLearngingProgram\main\dbDir"
    curDir = os.getcwd()
    os.chdir(dstDir)
    cmdOrder = r'zip -rJ "abc.zip" *.*' #��ʱ��һ��Ҫ��zip.exe���뻷����������Ŀ¼�У���Ϊ��ʱ����ǰĿ¼���Ѿ��л���Ҫ�����Ŀ¼���ˣ�zip.exe������ֱ����������
    os.system(cmdOrder)
    os.chdir(curDir)
	#����shutil.copy2��abc.zip������Ŀ¼�м���

#���zip.exe��ѹ������������Ѿ�����ѹ�����ˣ��ٶԸ�ѹ��������zip��������ֻ����²���������ѹ�����е������ļ�����update��û�е��ļ�����add�������Ὣ���ɵ�ѹ�����滻��������ʱ���ͬ����.zip�ļ����ᱻ�ٴδ򵽰��У��������other.zip���ƽ��д������ղŵ�ѹ�����ᱻ�����°�other.zip�С�
#-----------------------------------------------------------------------------------------
#pythonĿ¼����
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
	os.rmdir("%s/newDir" % currentPath)#��Ҫ˵�����ǣ�ʹ��os.rmdirɾ����Ŀ¼����Ϊ��Ŀ¼������������
	
	
	time_of_last_access = os.path.getatime(myfile) 
	time_of_last_modification = os.path.getmtime(myfile) 
	size = os.path.getsize(myfile) 	
	
	#�����ʱ������Ϊ��λ�����Ҵ�1970��1��1�տ�ʼ����Ϊ�˻�ȡ����Ϊ��λ�����������ڣ�����ʹ�����д��룺 
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
	#if not operator.isSequenceType(files):#���õĲ�������
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
    shutil.copy2("db.xlsx", "newDb.xlsx")#����������ʱ�������޸�ʱ��
	shutil.copytree("dbdir", "newDbdir", True)
	
	shutil.copy(os.path.join(os.curdir, "db.xlsx"), os.pardir)
	os.rename("dbdir", "dbdir2")#�ļ��л��ļ�
#-----------------------------------------------------------------------------------------
#�������Ҳ����������ͬ���ļ�ϵͳ֮���ƶ��ļ���������ǽ�myfile�ƶ���Ŀ¼d���棺 
os.rename(myfile, os.path.join(d, myfile)) 

#�ڿ��ļ�ϵͳ�ƶ��ļ���ʱ�򣬿�����ʹ��shutil.copy2�������ļ���Ȼ����ɾ��ԭ���ĸ������ɣ����£� 
shutil.copy2(myfile, os.path.join(d, myfile)) 
os.remove(myfile)
���������ƶ��ļ��ķ������ȫ�ġ� 
#-----------------------------------------------------------------------------------------
import os
import shutil
if __name__ == '__main__':
    currentPath = os.getcwd()
    dbXlsx = os.path.join(currentPath, "db.xlsx")
    print dbXlsx
    baseName = os.path.basename(dbXlsx)#�õ��ļ�����Ŀ¼��������չ��
    dirName = os.path.dirname(dbXlsx)#�õ���Ŀ¼��
	#��dirName, baseName = os.path.split(dbXlsx)
    print baseName
    print dirName
    print os.path.dirname(dirName)
	root, extension = os.path.splitext(dbXlsx)
    print root
    print extension 
������£�
D:\Program Files\eclipse workspace\PyLearngingProgram\main\db.xlsx
db.xlsx
D:\Program Files\eclipse workspace\PyLearngingProgram\main
D:\Program Files\eclipse workspace\PyLearngingProgram
D:\Program Files\eclipse workspace\PyLearngingProgram\main\db
.xlsx

������fname�е���չ�����ּ�.py����������extension�������ಿ���򸳸��˱���root�������õ�������ŵ���չ���Ļ���ֻ��ʹ��os.path.splitext(fname)[1][1:]���ɡ�
����һ���ļ���Ϊf������չ�����⣬���뽫����չ����Ϊext������ʹ������Ĵ��룺 
����newfile = os.path.splitext(f)[0] + ext 
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
������£�
D:\Program Files\eclipse workspace\PyLearngingProgram\main has the files ['a', 'db.xlsx', 'dbdir2', 'empty_book.xlsx', 'main.py', 'new.txt', 'newdbdir', '__init__.py']
D:\Program Files\eclipse workspace\PyLearngingProgram\main\a has the files ['b']
D:\Program Files\eclipse workspace\PyLearngingProgram\main\a\b has the files ['c']
D:\Program Files\eclipse workspace\PyLearngingProgram\main\a\b\c has the files []
D:\Program Files\eclipse workspace\PyLearngingProgram\main\dbdir2 has the files ['another.txt']
D:\Program Files\eclipse workspace\PyLearngingProgram\main\newdbdir has the files []

�����У�����arg���Ǳ��裬������os.path.walk����������ȡֵΪNone���ɡ�

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
������£�
[(5727L, 'db.xlsx'), (5825L, 'empty_book.xlsx'), (457L, 'main.py'), (19L, 'new.txt'), (0L, '__init__.py'), (0L, 'another.txt')]

����dirname�ǵ�ǰ���ڷ��ʵ�Ŀ¼�ľ���·����������files�ڵ��ļ������������dirname�����·�����ڴ��ڼ䣬��ǰ����Ŀ¼��û�иı䣬�Ǿ���˵�ýű���Ȼ���ڽű�����ʱ�����ڵ�Ŀ¼�С������Ϊʲô������Ҫ��filepathŪ�ɴ���dirname��file�ľ���·����ԭ����Ҫ�ı䵱ǰ����Ŀ¼Ϊdirname��ֻҪ�����ÿ��Ŀ¼����os.path.walk�ĺ����е���һ��os.chdir(dirname)��Ȼ���ڸú�����ĩβ���µ���os.chdir(dirname)����ǰ����Ŀ¼�Ļ�ԭֵ���ɣ�������ʾ�� 
def somefunc(arg, dirname, files): 
	origdir = os.getcwd()
	os.chdir(dirname) 
��#...�� 
	os.chdir(origdir)
	
#��һ�ֱ���Ŀ¼�ķ���
# encoding=utf-8
import os

if __name__ == '__main__':
    rootdir = r"d:\Program Files\eclipse workspace\PyLearngingProgram\main"
    for parent, dirnames, filenames in os.walk(rootdir):
        print parent.decode("gbk")
        print dirnames#ûʲô�ô����õ�һ����Ŀ¼�͵������ļ�������ƴ�������ļ���ȫĿ¼
        print filenames
������£�
d:\Program Files\eclipse workspace\PyLearngingProgram\main
['dbDir']
['abc.bmp', 'main.py', 'simple.xls', 'test.sh', 'test.txt', 'test.xls', 'test.xlsx', '__init__.py']
d:\Program Files\eclipse workspace\PyLearngingProgram\main\dbDir
[]
['test.xlsx']		
#-----------------------------------------------------------------------------------------
#�Լ�ʵ��os.path.walk����
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
����arg�����˾޴������ԡ����ǿ���ʹ������ͬʱ����������ݺ����ɵ����ݽṹ����һ���������ռ����д���һ���ߴ�Ĵ��й涨��չ�����ļ����ļ����ʹ�С������Ľ�������ļ���С���С�
bigfiles = {'filelist': [], # �ļ����ʹ�С�б� 
'extensions': ('.*ps', '.tiff', '.bmp'), 
'size_limit': 1000000, # 1 Mb 
} 
find(checksize3, os.environ['HOME'], bigfiles) 

import fnmatch # Unix��shell����ͨ���ƥ��
def checksize3(fullpath, arg): 
	treat_file = False 
	ext = os.path.splitext(fullpath)[1] 
	for s in arg['extensions']: 
		if fnmatch.fnmatch(ext, s): 
			treat_file = True # fullpath������ȷ����չ�� 
	size = os.path.getsize(fullpath) 
	if treat_file and size > arg['size_limit']: 
		size = '%.2fMb' % (size/1000000.0) # ��ӡ 
		arg['filelist'].append({'size': size, 'name': fullpath}) 
		
# ���մ�С�����ļ� 
def filesort(a, b): 
	return cmp(float(a['size'][:-2]), float(b['size'][:-2])) 

bigfiles['filelist'].sort(filesort) 
bigfiles['filelist'].reverse() 
for fileinfo in bigfiles['filelist']: 
	print fileinfo['name'], fileinfo['size'] 		
ע��Ϊ�б�����ĺ�����bigfiles['filelist']�����е�ÿ��Ԫ�ؾ���һ���ֵ䣬��size������һ���ַ����������ڽ��бȽ�֮ǰ���Ǳ��뽫��λMb(��������ַ�)ȥ����������ת��Ϊ�������� 	
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
        Test.show(self)#�������Ա���������������(self)
        
    def show(self):
        print "world"#����ȫ�ֺ���
        show2()#���п��Ե���ȫ�ֺ������޹�λ��
        
def show():
    print "hello"
    
def show2():
    print "nice"   

if __name__ == '__main__':
    c = Test()
    #c.show()
#-----------------------------------------------------------------------------------------
ȫ�ֱ�����ͬģ��ĵ�����ʹ��
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
�о�����ͨ�����ַ�ʽ��C++�д��麯��һ�����������
def show():
    pass

if __name__ == '__main__':
    show()
#-----------------------------------------------------------------------------------------
xlrd��ȡexcel
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
# encoding : utf-8       #���ñ��뷽ʽ  
  
import xlrd                    #����xlrdģ��  
  
#��ָ���ļ�·����excel�ļ�  
  
xlsfile = r'D:\AutoPlan\apisnew.xls'   
book = xlrd.open_workbook(xlsfile)     #���excel��book����  
  
#��ȡsheet���󣬷�����2�֣�  
sheet_name=book.sheet_names()[0]          #���ָ��������sheet����  
print sheet_name  
sheet1=book.sheet_by_name(sheet_name)  #ͨ��sheet��������ȡ����Ȼ�����֪��sheet�����˿���ֱ��ָ��  
sheet0=book.sheet_by_index(0)     #ͨ��sheet�������sheet����  
  
#��ȡ������������  
  
nrows = sheet.nrows    #������  
ncols = sheet.ncols   #������  
  
#���ָ���С��е�ֵ�����ض���Ϊһ��ֵ�б�  
  
row_data = sheet.row_values(0)   #��õ�1�е������б�  
col_data = sheet.col_values(0)  #��õ�һ�е������б�Ȼ��Ϳ��Ե��������������  
  
#ͨ��cell��λ��������ָ��cell��ֵ  
cell_value1 = sheet.cell_value(0,1)  ##ֻ��cell��ֵ���ݣ��磺http://xxx.xxx.xxx.xxx:8850/2/photos/square/  
print cell_value1  
cell_value2 = sheet.cell(0,1) ##����cellֵ�����⻹�и������ԣ��磺text:u'http://xxx.xxx.xxx.xxx:8850/2/photos/square/'  
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
            print ','.join(values)#��,���������ַ���
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
    wb = Workbook(encoding = "utf-8")#��������������壬�����ﲻ��Ҫ��utf-8���룬Ĭ��wb = Workbook()����
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
                            'alignment: horizontal left; alignment: vertical center;' % (u"����")
                            )
  
    for i in range(1, 1000):
        fillInExcel(ws, formatForValue, i, "It", "is", "a", "nice", "day")#��д����������504ʱ��һ��Ҫ��format��Ϊ��������д�뺯��fillInExcel�У�������ú�����ֱ����forѭ����дҲ���ԣ��������format������fillInExcel�У���ᱨ���ʽ������
    
    ws.col(0).width = 5000#width:18.86
    ws.col(1).width = 6000
    ws.col(2).width = 7000
    ws.col(3).width = 8000
    ws.col(4).width = 8000
    wb.save("test.xls")
#-----------------------------------------------------------------------------------------
#��д�ļ�����b�������ƶ�ȡ�����Ա����ļ���ʽ
def getEnterSymbol(line):#����ֻ��һ�����ݣ�û�л��з����ļ��������������
    if len(line) == 1:#������г���Ϊ1����ôҪô��һ�����з���Ҫô��һ��ֻ��һ���ַ����ļ���
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
��
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
�ڴ�����־�ļ���ʱ�򣬳����������������������־�ļ��޴󣬲�����һ���԰������ļ����뵽�ڴ��н��д���������Ҫ��һ̨�����ڴ�Ϊ 2GB �Ļ����ϴ���һ�� 2GB ����־�ļ������ǿ���ϣ��ÿ��ֻ�������� 200MB �����ݡ�
�� Python �У����õ� File ����ֱ���ṩ��һ�� readlines(sizehint) ������������������顣������Ĵ���Ϊ����

file = open('test.log', 'r')
sizehint = 209715200   # 200M
position = 0
lines = file.readlines(sizehint)
while not file.tell() - position < 0:
	position = file.tell()
	lines = file.readlines(sizehint)

ÿ�ε��� readlines(sizehint) �������᷵�ش�Լ 200MB �����ݣ����������صı�Ȼ���������������ݣ����������£����ص����ݵ��ֽ�������΢�� sizehint ָ����ֵ��һ�㣨�����һ�ε��� readlines(sizehint) ������ʱ�򣩡�ͨ������£�Python ���Զ����û�ָ���� sizehint ��ֵ�������ڲ������С����������

file��python��һ����������ͣ���������python�����ж��ⲿ���ļ����в�������python��һ�ж��Ƕ���fileҲ�����⣬file��file�ķ��������ԡ�������������δ���һ��file����
file(name[, mode[, buffering]]) 
file()�������ڴ���һ��file��������һ��������open()�����ܸ�����һЩ�����������ú��������������Ĳ������������������ַ�������ʽ���ݵġ�name���ļ������֡�
mode�Ǵ򿪵�ģʽ����ѡ��ֵΪr w a U���ֱ�������Ĭ�ϣ� д ���֧�ָ��ֻ��з���ģʽ����w��aģʽ���ļ��Ļ�������ļ������ڣ���ô���Զ����������⣬��wģʽ��һ���Ѿ����ڵ��ļ�ʱ��ԭ���ļ������ݻᱻ��գ���Ϊһ��ʼ�ļ��Ĳ����ı�������ļ��Ŀ�ͷ�ģ���ʱ�����д���������ɻ��ԭ�е����ݸ�Ĩ����������ʷ��ԭ�򣬻��з��ڲ�ͬ��ϵͳ���в�ͬģʽ�������� unix����һ��\n������windows���ǡ�\r\n������Uģʽ���ļ�������֧�����еĻ���ģʽ��Ҳ��˵��\r�� '\n' '\r\n'���ɱ�ʾ���У�����һ��tuple������������ļ����õ����Ļ��з�����������˵�����ж���ģʽ������python��ͳһ��\n���档��ģʽ�ַ��ĺ��棬�����Լ���+ b t�����ֱ�ʶ���ֱ��ʾ���Զ��ļ�ͬʱ���ж�д�������ö�����ģʽ���ı�ģʽ��Ĭ�ϣ����ļ���
buffering���Ϊ0��ʾ�����л���;���Ϊ1��ʾ���С��л��塰;�����һ������1������ʾ�������Ĵ�С��Ӧ�������ֽ�Ϊ��λ�ġ�

file�������Լ������Ժͷ�������������file�����ԡ�
closed #����ļ��Ƿ��Ѿ��رգ���close()��д 
encoding #�ļ����� 
mode #��ģʽ 
name #�ļ��� 
newlines #�ļ����õ��Ļ���ģʽ����һ��tuple 
softspace #boolean�ͣ�һ��Ϊ0����˵����print 

F.read([size]) #sizeΪ��ȡ�ĳ��ȣ���byteΪ��λ 
F.readline([size]) 
#��һ�У����������size���п��ܷ��ص�ֻ��һ�е�һ���� 
F.readlines([size]) 
#���ļ�ÿһ����Ϊһ��list��һ����Ա�����������list����ʵ�����ڲ���ͨ��ѭ������readline()��ʵ�ֵġ�����ṩsize������size�Ǳ�ʾ��ȡ���ݵ��ܳ���Ҳ����˵����ֻ�����ļ���һ���֡� 
F.write(str) 
#��strд���ļ��У�write()��������str�����һ�����з� 
F.writelines(seq) 
#��seq������ȫ��д���ļ��С��������Ҳֻ����ʵ��д�룬������ÿ�к�������κζ����� 
file������������
F.close() 
#�ر��ļ���python����һ���ļ����ú��Զ��ر��ļ���������һ����û�б�֤����û��������Լ��رյ�ϰ�ߡ����һ���ļ��ڹرպ󻹶�����в��������ValueError 
F.flush() 
#�ѻ�����������д��Ӳ�� 
F.fileno() 
#����һ�������͵ġ��ļ���ǩ�� 
F.isatty() 
#�ļ��Ƿ���һ���ն��豸�ļ���unixϵͳ�еģ� 
F.tell() 
#�����ļ�������ǵĵ�ǰλ�ã����ļ��Ŀ�ͷΪԭ�� 
F.next() 
#������һ�У������ļ��������λ�Ƶ���һ�С���һ��file����for ... in file���������ʱ�����ǵ���next()������ʵ�ֱ����ġ� 
F.seek(offset[,whence]) 
#���ļ����������Ƶ�offset��λ�á����offsetһ����������ļ��Ŀ�ͷ������ģ�һ��Ϊ������������ṩ��whence�����Ͳ�һ���ˣ�whence����Ϊ0��ʾ��ͷ��ʼ���㣬1��ʾ�Ե�ǰλ��Ϊԭ����㡣2��ʾ���ļ�ĩβΪԭ����м��㡣��Ҫע�⣬����ļ���a��a+��ģʽ�򿪣�ÿ�ν���д����ʱ���ļ�������ǻ��Զ����ص��ļ�ĩβ�� 
F.truncate([size]) 
#���ļ��óɹ涨�Ĵ�С��Ĭ�ϵ��ǲõ���ǰ�ļ�������ǵ�λ�á����size���ļ��Ĵ�С��Ҫ������ϵͳ�Ĳ�ͬ�����ǲ��ı��ļ���Ҳ��������0���ļ�������Ӧ�Ĵ�С��Ҳ��������һЩ��������ݼ���ȥ�� 
#-----------------------------------------------------------------------------------------
#�����쳣
if __name__ == '__main__':
    try:
        a = "abc" + 1
    except Exception, e:
        print e
�����cannot concatenate 'str' and 'int' objects
#-----------------------------------------------------------------------------------------
#����
# encoding=utf-8
'''
Created on 2012-12-27

@author: Ben
'''    

if __name__ == '__main__':
    print "���"
	

# encoding=utf-8
'''
Created on 2012-12-27

@author: Ben
'''    
#��ʱ������ļ���ʽΪutf-8����������������ʾ
if __name__ == '__main__':
    f = open("test.txt", "r")
    l = f.readline()
    print l
    f.close()

#��ʱ������ļ���ʽΪansi������Ҫ��gbk����
# encoding=utf-8
if __name__ == '__main__':
    f = open("test.txt", "r")
    l = f.readline().decode("gbk")
    print l
    f.close()
����
# encoding=utf-8
import codecs
if __name__ == '__main__':
    f = open("test.txt")
    (encoder, decoder, reader, writer) = codecs.lookup("gbk")
    f = reader(f)
    l = f.readline()
    print l
    f.close()
����
# encoding=utf-8
import codecs

if __name__ == '__main__':
    f = codecs.open("test.txt", "r", "gbk")
    l = f.readline()
    print l
    f.close()

#ͨ�����´��룬���Դ���������utf-8���뻹��ansi������ļ�
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
		
#дutf-8����ʱ����������ģ���utf-8��bom��ʽ�����ֻ��ascii�ַ��������ļ�����ansi��ʽ
# encoding=utf-8
import codecs

if __name__ == '__main__':
    f = codecs.open("test.txt", "w", "utf-8")
    txt = unicode("���\n", "utf-8")
    f.write(txt)
    f.close()
	
#���ǿ��Ҫ��������û�����ģ�����utf-8��ʽ���������±�д
# encoding=utf-8
import codecs

if __name__ == '__main__':
    f = codecs.open("test.txt", "w", "utf-8-sig")
    txt = unicode("qwer\n")
    f.write(txt)
    f.close()

#���û̫������ʲô��
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

import codecs

txt = u"qwer"
file=codecs.open("test","w","utf-8")
file.write(txt)
file.close()

#ʲô��ʽ���������Զ�Ϊansi��ʽ�����ϣ�����ֵ�����ʽ������Ҫ��дʱ����wb���������ֻ��з���windows:\r\n��unix:\n��mac:\r�������е����⣬��ôд����windows��
# encoding=utf-8
import codecs

if __name__ == '__main__':
    f = codecs.open("test.txt", "wb")
    txt = unicode("hello\n")
    f.write(txt)
    f.close()
	
# -*- coding: UTF-8 -*- ���Ǹ�ע����
��������˵�����PythonԴ�����ļ���ʹ�õı��롣ȱʡ�������ĳ�����Ҫʹ��ascii����д�������������д���ĵĻ���python������һ��ᱨ����������������õ��ļ����룬python�ͻ��Զ������ٱ���
������ʽ������д�ɣ�
#coding=utf-8
��
#coding:utf-8	
#-----------------------------------------------------------------------------------------
#����־û�
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
#win32com����excel
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
#��ˮ����
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
#python�ĺ������ݣ�����C++�ĺ���ָ��
def printHello(msg):
    print msg
    
def functionHandler(functionName, msg):
    functionName(msg)
    
if __name__ == '__main__': 
    functionHandler(printHello, "hello, world")
#-----------------------------------------------------------------------------------------
py2exeʹ�����
from distutils.core import setup
import py2exe

setup(windows=[{"script": "D:/Program Files/eclipse workspace/PyLearngingProgram/main/main.py", "icon_resources": [(1, "D:/Installations/python-2/py2exe/1.ico")]}])
#setup(console=[{"script": "D:/Program Files/eclipse workspace/PyLearngingProgram/main/main.py", "icon_resources": [(1, "D:/Installations/python-2/py2exe/1.ico")]}])
#-----------------------------------------------------------------------------------------
���self�������ΪC++�е�this
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
*var�ڶ��庯������ʱ���ǿɱ������������˼���ڵ���ʱ����Ҫunpack tuple����˼���磺
t = (1,2,3)ֱ�Ӵ�����һ������������*t�����3��������

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

������£�
1 2 3 4
((1, 2, 3, 4),)
(1, 2, 3, 4)
11 12 13 14
([11, 12, 13, 14],)
(11, 12, 13, 14)
#-----------------------------------------------------------------------------------------
#��Ԫ�����������ʵ��
���濴һ��python�Ľ������һ��
def trans(v):  
	return 1 if v==0 else v  
Ҳ����if else���ļ�д��ʽ����˼һ�������ף�������������   
   
�����ǽ����������
def trans(v):  
    return v==0 and 1 or v  
�õ����������������ԡ�
����һ�£����v����0Ϊtrue�����1�������㣬Ϊtrue���򲻽��к���Ļ����㣬ֱ�ӷ���1�����v����0Ϊfalse�����1�������㣬Ϊfalse���������л����㣬����v��
#-----------------------------------------------------------------------------------------
#����xml
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
#enumerate����
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
������£�
2147483647 hello
0 world
1 good
2147483647 nice
0 day
1 how

#-----------------------------------------------------------------------------------------
#ʹ��ElementTree����xml
import sys
import time
import string
 
import xml.etree.ElementTree as etree

#����Ĭ���ַ���ΪUTF8 ��Ȼ��Щʱ��ת��������
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
    title_txt = u'%s' % '���Ļ���ð��'
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

������£�
<?xml version='1.0' encoding='UTF-8'?>
<data><interface_version>5</interface_version><site>www.xxx.com</site><lastmod>2016-06-11</lastmod><app><title>���Ļ���ð��</title><appid>222</appid></app></data>


#coding:utf-8

'''
Created on 2016��6��10��

@author: Ben
'''

from xml.etree.ElementTree import Element, ElementTree

if __name__ == '__main__':
    root = Element('bookstore')
    tree = ElementTree(root)
    
    #����1���ӽڵ�
    child0 = Element('book', {'category' : "COOKING"} )
    root.append(child0)
    
    #����2���ӽڵ�
    child00 = Element('title', {'language' : "English"} )
    child00.text = 'Everyday Italian' #2���ӽڵ��ı�
    child0.append(child00)
    
    tree.write('test.xml', encoding="UTF-8", method="xml", xml_declaration=True)
    

http://pycoders-weekly-chinese.readthedocs.io/en/latest/issue6/processing-xml-in-python-with-element-tree.html
ElementTree �� һ�� API ������ʵ��
ElementTree ��������Ϊ�˴��� XML ������ Python ��׼����������ʵ�֡�һ���Ǵ� Python ʵ������ xml.etree.ElementTree ������һ�����ٶȿ�һ��� xml.etree.cElementTree ����Ҫ��ס�� ����ʹ�� C ����ʵ�ֵ����֣���Ϊ���ٶȸ��죬�������ĵ��ڴ���١������ĵ�����û�� _elementtree (��ע��4) ��ô����Ҫ��������

try:
    import xml.etree.cElementTree as ET
except ImportError:
    import xml.etree.ElementTree as ET
#-----------------------------------------------------------------------------------------
#lambda���ʽ
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
    print "one sequence: ", map(lambda x: x + 2, l1)  # ������ӳ��
    print "two sequence: ", map(lambda x, y: x + y, l1, l2)  # �������߶�����ӳ��
    print "three sequences: ", map(lambda x, y, z: x + y + z, l1, l2, [7, 8, 9])

    print reduce(lambda x, y: x + y, l1)  # �޳�ʼֵ
    print reduce(lambda x, y: x + y, l1, 10)  # �г�ʼֵ
    print reduce(add, l1, 20)  # ʹ�ú���
    print reduce(lambda sumLen, s: sumLen + len(s), ["abcd", "de", "f"], 0) #����������󳤶Ⱥͣ���Ҫ����ʼֵ�����м��ۼ�ֵ��������lambda���ʽ�ĵ�һ��������������ṩ��ʼֵ0���򽫱���TypeError: cannot concatenate 'str' and 'int' objects

    print all([1, 2, 3]), all([0, 1, 2, 3]) #�Ƿ����еĵ�������ΪTrue
    print any([1, 2, 3]), any([0, 1, 2, 3]) #�Ƿ�������һ����������ΪTrue
�����
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

#-----�ַ�����split��join����
# encoding=utf-8


if __name__ == "__main__":
    s = "a b   c"
    print s.split()  # �޲�����ʹ��" "�ָ���["a", "b", "c"]
    print " ".join(s.split()) #�õ�һ��str�������������ж��� a b c
#-----�Ƿ�Ҫʹ��lambda���ʽ
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
    processFunc = lambda collapse: collapse and (lambda s: " ".join(s.split())) or (lambda s: s) #lambda���ʽ�����������һ��boolֵ������ֵ��һ��lambda���ʽ
    testStr = "a   b  c"
    print processFunc(True)(testStr) #�ȴ���True���õ�����һ��lambda���ʽ��lambda s: " ".join(s.split())������lambda���ʽ����"a   b  c"��������ѹ���ո���
    # print getStr(True, testStr) #ͬ���Ĳ�������Ҫ5�ж���+ʵ��
    print processFunc(False)(testStr) #�ȴ���False���õ���һ��lambdag���ʽ��lambda s: s���������������

    
ͨ�������ӣ����Ƿ��֣�lambda��ʹ�ô������˴��룬ʹ�����������������ֵ��ע����ǣ������һ���̶��Ͻ��ʹ���Ŀɶ��ԡ�������Ƿǳ���Ϥpython���˻����Դ˸е�������⡣
--lambda ������һ����������
--lambda �����������������Ч�ʵ���ߣ�ֻ��ʹ�������ࡣ
--�������ʹ��for...in...if����ɵģ��������lambda��
--���ʹ��lambda��lambda�ڲ�Ҫ����ѭ��������У�����Ը���庯������ɣ�ʹ�����ÿ������Ժ͸��õĿɶ��ԡ�
�ܽ᣺lambda ��Ϊ�˼��ٵ��к����Ķ�������ڵġ�
#-----------------------------------------------------------------------------------------
#��lambda���ʽ�ж�С�����Ƿ�ƥ��
# encoding=utf-8

def balance(chars):
    def f(cnt, c):
        if cnt >= 0:
            if c == '(':
                cnt += 1
            elif c == ')':
                cnt -= 1
        return cnt

    return 0 == reduce(lambda cnt, c: f(cnt, c), chars, 0)  # ��reduce����Ҫ��ĵ�һ������function�У�Ҫ���һ����������ʼ����ֵ
    # return 0 == reduce(lambda c, cnt: f(cnt, c), chars, 0) #�����lambda���ʽ����Ϊ lambda c, cnt������ʼֵ0�ḳֵ��c�����ַ�����һ���ַ��ḳֵ��cnt


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
    for i, paren in enumerate(PARENS):  # ����index, seq[index]��������
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

�����
False
True
True
False
True
True
True
#-----------------------------------------------------------------------------------------
#dict�����ֹ��췽ʽ
# coding=utf-8

if __name__ == "__main__":
    d = {'a': 10, 'b': 20}
    print d

    d = dict(a=10, b=20)
    print d

    l = [("a", 10), ("b", 20), ('c', 30)]
    d = dict(l)
    print d
�����
{'a': 10, 'b': 20}
{'a': 10, 'b': 20}
{'a': 10, 'c': 30, 'b': 20}
#-----------------------------------------------------------------------------------------
#����ѧϰhttp://python.jobbole.com/81336/
# coding=utf-8
import urllib2

if __name__ == "__main__":
    response = urllib2.urlopen("http://www.baidu.com")
    print response.read()

#post��ʽ��¼ 
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

#get��ʽ��¼
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
    
#����Headers
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

#ʹ��DebugLog
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
�����
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
�����
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
#��ǰ�������ж�
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
        

#��ȡCookie���浽����
# coding=utf-8
import cookielib
import urllib2

if __name__ == "__main__":
    cookie = cookielib.CookieJar()
    # ����urllib2���HTTPCookieProcessor����������cookie������
    handler = urllib2.HTTPCookieProcessor(cookie)
    # ͨ��handler������opener
    opener = urllib2.build_opener(handler)
    # �˴���open����ͬurllib2��urlopen������Ҳ���Դ���request
    response = opener.open('http://www.baidu.com')
    for item in cookie:
        print 'Name = ' + item.name
        print 'Value = ' + item.value

#����Cookie���ļ�
# coding=utf-8
import cookielib
import urllib2

if __name__ == "__main__":
    # ���ñ���cookie���ļ���ͬ��Ŀ¼�µ�cookie.txt
    filename = 'cookie.txt'
    # ����һ��MozillaCookieJar����ʵ��������cookie��֮��д���ļ�
    cookie = cookielib.MozillaCookieJar(filename)
    # ����urllib2���HTTPCookieProcessor����������cookie������
    handler = urllib2.HTTPCookieProcessor(cookie)
    # ͨ��handler������opener
    opener = urllib2.build_opener(handler)
    # ����һ������ԭ��ͬurllib2��urlopen
    response = opener.open("http://www.baidu.com")
    # ����cookie���ļ�
    cookie.save(ignore_discard=True, ignore_expires=True)
    
#���ļ��л�ȡCookie������
# coding=utf-8
import cookielib
import urllib2

if __name__ == "__main__":
    # ����MozillaCookieJarʵ������
    cookie = cookielib.MozillaCookieJar()
    # ���ļ��ж�ȡcookie���ݵ�����
    cookie.load('cookie.txt', ignore_discard=True, ignore_expires=True)
    # ���������request
    req = urllib2.Request("http://www.baidu.com")
    # ����urllib2��build_opener��������һ��opener
    opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookie))
    response = opener.open(req)
    print response.read()
    

#����cookieģ����վ��¼
import urllib
import urllib2
import cookielib
 
filename = 'cookie.txt'
#����һ��MozillaCookieJar����ʵ��������cookie��֮��д���ļ�
cookie = cookielib.MozillaCookieJar(filename)
opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookie))
postdata = urllib.urlencode({
			'stuid':'201200131012',
			'pwd':'23342321'
		})
#��¼����ϵͳ��URL
loginUrl = 'http://jwxt.sdu.edu.cn:7890/pls/wwwbks/bks_login2.login'
#ģ���¼������cookie���浽����
result = opener.open(loginUrl,postdata)
#����cookie��cookie.txt��
cookie.save(ignore_discard=True, ignore_expires=True)
#����cookie���������һ����ַ������ַ�ǳɼ���ѯ��ַ
gradeUrl = 'http://jwxt.sdu.edu.cn:7890/pls/wwwbks/bkscjcx.curscopre'
#������ʳɼ���ѯ��ַ
result = opener.open(gradeUrl)
print result.read()

����һ������cookie��opener���ڷ��ʵ�¼��URLʱ������¼���cookie����������Ȼ���������cookie������������ַ��
���¼֮����ܲ鿴�ĳɼ���ѯѽ����ѧ�ڿα�ѽ�ȵ���ַ��ģ���¼����ôʵ����

4.Python Reģ��
Python �Դ���reģ�飬���ṩ�˶�������ʽ��֧�֡���Ҫ�õ��ķ����о�����
#����pattern����
re.compile(string[,flag])  
#����Ϊƥ�����ú���
re.match(pattern, string[, flags])
re.search(pattern, string[, flags])
re.split(pattern, string[, maxsplit])
re.findall(pattern, string[, flags])
re.finditer(pattern, string[, flags])
re.sub(pattern, repl, string[, count])
re.subn(pattern, repl, string[, count])

? re.I(ȫƴ��IGNORECASE): ���Դ�Сд��������������д������ͬ��
? re.M(ȫƴ��MULTILINE): ����ģʽ���ı�'^'��'$'����Ϊ���μ���ͼ��
? re.S(ȫƴ��DOTALL): ������ƥ��ģʽ���ı�'.'����Ϊ
? re.L(ȫƴ��LOCALE): ʹԤ���ַ��� \w \W \b \B \s \S ȡ���ڵ�ǰ�����趨
? re.U(ȫƴ��UNICODE): ʹԤ���ַ��� \w \W \b \B \s \S \d \D ȡ����unicode������ַ�����
? re.X(ȫƴ��VERBOSE): ��ϸģʽ�����ģʽ��������ʽ�����Ƕ��У����Կհ��ַ��������Լ���ע�͡�

//
# coding=utf-8
import re

if __name__ == "__main__":
    pattern = re.compile(r'hello')
    # ʹ��re.matchƥ���ı������ƥ�������޷�ƥ��ʱ������None
    result1 = re.match(pattern, 'hello hello')
    result2 = re.match(pattern, 'helloo CQC!') #�����ͷƥ�䣬���aahello���޷�ƥ��
    result3 = re.match(pattern, 'helo CQC!')
    result4 = re.match(pattern, 'hello CQC!')

    # ���1ƥ��ɹ�
    if result1:
        # ʹ��Match��÷�����Ϣ
        print result1.group()
        print result1.string
        print result1.re #ͬ pattern
        print result1.endpos
        print result1.lastindex
        print result1.lastgroup
    else:
        print '1ƥ��ʧ�ܣ�'

    # ���2ƥ��ɹ�
    if result2:
        # ʹ��Match��÷�����Ϣ
        print result2.group()
    else:
        print '2ƥ��ʧ�ܣ�'

    # ���3ƥ��ɹ�
    if result3:
        # ʹ��Match��÷�����Ϣ
        print result3.group()
    else:
        print '3ƥ��ʧ�ܣ�'

    # ���4ƥ��ɹ�
    if result4:
        # ʹ��Match��÷�����Ϣ
        print result4.group()
    else:
        print '4ƥ��ʧ�ܣ�'
�����
hello
hello hello
<_sre.SRE_Pattern object at 0x04A1B840>
11
None
None
hello
3ƥ��ʧ�ܣ�
hello

ƥ�����
1.��һ��ƥ�䣬pattern������ʽΪ��hello��������ƥ���Ŀ���ַ���stringҲΪhello����ͷ��β��ȫƥ�䣬ƥ��ɹ���
2.�ڶ���ƥ�䣬stringΪhelloo CQC����stringͷ��ʼƥ��pattern��ȫ����ƥ�䣬patternƥ�������ͬʱƥ����ֹ�������o CQC����ƥ�䣬����ƥ��ɹ�����Ϣ��
3.������ƥ�䣬stringΪhelo CQC����stringͷ��ʼƥ��pattern�����ֵ� ��o�� ʱ�޷����ƥ�䣬ƥ����ֹ������None
4.���ĸ�ƥ�䣬ͬ�ڶ���ƥ��ԭ����ʹ�����˿ո��Ҳ������Ӱ�졣

#����
# coding=utf-8
import re

if __name__ == "__main__":
    m = re.match(r'(\w+) (\w+)(?P<sign>.*)', 'hello world!') #!hello world!��ƥ�䲻��

    print "m.string:", m.string
    print "m.re:", m.re
    print "m.pos:", m.pos
    print "m.endpos:", m.endpos
    print "m.lastindex:", m.lastindex #ָ���һ��������������(?P<sign>.*)��������
    print "m.lastgroup:", m.lastgroup #ָ���һ������ı���
    print "m.group():", m.group() #ƥ�䵽���ַ���
    print "m.group(1,2):", m.group(1, 2)
    print "m.groups():", m.groups()
    print "m.groupdict():", m.groupdict()
    print "m.start(2):", m.start(2) #��2��������ʼλ��
    print "m.end(2):", m.end(2) #��2���������λ��
    print "m.span(2):", m.span(2) #��2��������ʼ�ͽ���λ��
    print r"m.expand(r'\g \g\g'):", m.expand(r'\2 \1\3')
�����
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

��2��re.search(pattern, string[, flags])
search������match�����������ƣ���������match()����ֻ���re�ǲ�����string�Ŀ�ʼλ��ƥ�䣬search()��ɨ������string����ƥ�䣬match����ֻ����0λ��ƥ��ɹ��Ļ����з��أ�������ǿ�ʼλ��ƥ��ɹ��Ļ���match()�ͷ���None��ͬ����search�����ķ��ض���ͬ��match()���ض���ķ��������ԡ�������һ�����Ӹ���һ��
# coding=utf-8
import re

if __name__ == "__main__":
    # ��������ʽ�����Pattern����
    pattern = re.compile(r'world')
    # ʹ��search()����ƥ����Ӵ�����������ƥ����Ӵ�ʱ������None
    # ���������ʹ��match()�޷��ɹ�ƥ��
    match = re.search(pattern, 'hello world!')
    if match:
        # ʹ��Match��÷�����Ϣ
        print match.group()
    ### ��� ###
    # world

��3��re.split(pattern, string[, maxsplit])
�����ܹ�ƥ����Ӵ���string�ָ�󷵻��б�maxsplit����ָ�����ָ��������ָ����ȫ���ָ����ͨ����������Ӹ���һ�¡�
# coding=utf-8
import re

if __name__ == "__main__":
    pattern = re.compile(r'\d+')
    print re.split(pattern, 'one1two2three3four4')

    ### ��� ###
    # ['one', 'two', 'three', 'four', '']

��4��re.findall(pattern, string[, flags])
����string�����б���ʽ����ȫ����ƥ����Ӵ�������ͨ���������������һ��
# coding=utf-8
import re

if __name__ == "__main__":
    pattern = re.compile(r'\d+')
    print re.findall(pattern, 'one1two2three3four4')

    ### ��� ###
    # ['1', '2', '3', '4']

��5��re.finditer(pattern, string[, flags])
����string������һ��˳�����ÿһ��ƥ������Match���󣩵ĵ�����������ͨ�����������������һ��
# coding=utf-8
import re

if __name__ == "__main__":
    pattern = re.compile(r'\d+')
    for m in re.finditer(pattern, 'one1two2three3four4'):
        print m.group(),

    ### ��� ###
    # 1 2 3 4

��6��re.sub(pattern, repl, string[, count])
ʹ��repl�滻string��ÿһ��ƥ����Ӵ��󷵻��滻����ַ�����
��repl��һ���ַ���ʱ������ʹ��\id��\g��\g���÷��飬������ʹ�ñ��0��
��repl��һ������ʱ���������Ӧ��ֻ����һ��������Match���󣩣�������һ���ַ��������滻�����ص��ַ����в��������÷��飩��
count����ָ������滻��������ָ��ʱȫ���滻��
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

��7��re.subn(pattern, repl, string[, count])
���� (sub(repl, string[, count]), �滻����)��
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

5. Python Reģ�����һ��ʹ�÷�ʽ
���������ǽ�����7�����߷���������match��search�ȵȣ��������÷�ʽ���� re.match��re.search�ķ�ʽ����ʵ��������һ�ֵ��÷�ʽ������ͨ��pattern.match��pattern.search���ã����� ���ñ㲻�ý�pattern��Ϊ��һ�����������ˣ�������������ýԿɡ�
Pattern.match(string[, pos[, endpos]]) | re.match(pattern, string[, flags])
Pattern.search(string[, pos[, endpos]]) | re.search(pattern, string[, flags])
Pattern.split(string[, maxsplit]) | re.split(pattern, string[, maxsplit])
Pattern.findall(string[, pos[, endpos]]) | re.findall(pattern, string[, flags])
Pattern.finditer(string[, pos[, endpos]]) | re.finditer(pattern, string[, flags])
Pattern.sub(repl, string[, count]) | re.sub(pattern, repl, string[, count])
Pattern.subn(repl, string[, count]) |re.sub(pattern, repl, string[, count])

4. ���� Beautiful Soup ����
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
    soup = BeautifulSoup(html, "lxml") #�������lxml������ʾ��ǰ�ڱ���ʹ��lxml��ȥ������������ʾ��һ�£�soup = BeautifulSoup(open('index.html'))
    print soup.prettify()
�����
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

5. �Ĵ��������
Beautiful Soup������HTML�ĵ�ת����һ�����ӵ����νṹ,ÿ���ڵ㶼��Python����,���ж�����Թ���Ϊ4��:
Tag
NavigableString
BeautifulSoup
Comment
�������ǽ���һһ����
��1��Tag
Tag ��ʲô��ͨ�׵㽲���� HTML �е�һ������ǩ������
print soup.title
print soup.head
print soup.a
print soup.p
�����
<title>The Dormouse's story</title>
<head><title>The Dormouse's story</title></head>
<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>
<p class="title" name="dromouse"><b>The Dormouse's story</b></p>
���ǿ������� soup�ӱ�ǩ�����ɵػ�ȡ��Щ��ǩ�����ݣ��ǲ��Ǹо���������ʽ������ˣ�������һ���ǣ������ҵ��������������еĵ�һ������Ҫ��ı�ǩ�����Ҫ��ѯ���еı�ǩ�������ں�����н��ܡ�

���ǿ�����֤һ����Щ���������
print type(soup.a)
# <class 'bs4.element.Tag'>

���� Tag������������Ҫ�����ԣ��� name �� attrs���������Ƿֱ�������һ��
--name
print soup.name
print soup.head.name
#[document]
#head
soup ������Ƚ����⣬���� name ��Ϊ [document]�����������ڲ���ǩ�������ֵ��Ϊ��ǩ��������ơ�

--attrs
print soup.p.attrs
#{'class': ['title'], 'name': 'dromouse'}
��������ǰ� p ��ǩ���������Դ�ӡ����˳������õ���������һ���ֵ䡣

���������Ҫ������ȡĳ�����ԣ������������������ǻ�ȡ���� class ��ʲô
print soup.p['class']
#['title']
����������������get�������������Ե����ƣ������ǵȼ۵�
print soup.p.get('class')
#['title']
���ǿ��Զ���Щ���Ժ����ݵȵȽ����޸ģ�����
soup.p['class']="newClass"
print soup.p
#<p class="newClass" name="dromouse"><b>The Dormouse's story</b></p>
�����Զ�������Խ���ɾ��������
del soup.p['class']
print soup.p
#<p name="dromouse"><b>The Dormouse's story</b></p>

��2��NavigableString
��Ȼ�����Ѿ��õ��˱�ǩ�����ݣ���ô�������ˣ�����Ҫ���ȡ��ǩ�ڲ���������ô���أ��ܼ򵥣��� .string ���ɣ�����
print soup.p.string
#The Dormouse's story

�������Ǿ����ɻ�ȡ���˱�ǩ��������ݣ����������������ʽҪ���鷳������������һ�� NavigableString����������� ���Ա������ַ���������������û��ǳ���Ӣ�����ְɡ�
print type(soup.p.string)
#<class 'bs4.element.NavigableString'>

��3��BeautifulSoup
BeautifulSoup �����ʾ����һ���ĵ���ȫ������.�󲿷�ʱ��,���԰������� Tag ������һ������� Tag�����ǿ��Էֱ��ȡ�������ͣ����ƣ��Լ�����������һ��
print type(soup.name)
#<type 'unicode'>
print soup.name 
# [document]
print soup.attrs 
#{} ���ֵ�

��4��Comment
Comment ������һ���������͵� NavigableString ������ʵ�����������Ȼ������ע�ͷ��ţ�����������úô����������ܻ�����ǵ��ı�����������벻�����鷳��
������һ����ע�͵ı�ǩ
print soup.a
print soup.a.string
print type(soup.a.string)
Python �C ��������
��ҳ�������¹۵��붯̬����֪ʶϵ�н̳�ʵ����Ŀ�������ܹ�����ԴPythonС��
�������� > Python - �������� > �������� > ʵ����Ŀ > Python�������ţ�8����Beautiful Soup���÷�
Python�������ţ�8����Beautiful Soup���÷�

2015/04/25 �� ʵ����Ŀ, ϵ�н̳� �� 6 ���� �� ����
������ 27
�������ߣ� �������� - ����� ��δ��������ɣ���ֹת�أ�
��ӭ���벮������ ר�����ߡ�
Python�������ţ�1��������
Python�������ţ�2������������˽�
Python�������ţ�3����Urllib��Ļ���ʹ��
Python�������ţ�4����Urllib��ĸ߼��÷�
Python�������ţ�5����URLError�쳣����
Python�������ţ�6����Cookie��ʹ��
Python�������ţ�7����������ʽ
Python�������ţ�8����Beautiful Soup���÷�
��һ�����ǽ�����������ʽ������������ʵ��������ģ����һ������ƥ�����в�أ��ǿ��ܳ���ʹ������õ�ѭ��֮�У������е�С�����Ҳ��д����� ��ʽ��д���õò�������û��ϵ�����ǻ���һ����ǿ��Ĺ��ߣ���Beautiful Soup�����������ǿ��Ժܷ������ȡ��HTML��XML��ǩ�е����ݣ�ʵ���Ƿ��㣬��һ�ھ�������һ��������һ��Beautiful Soup��ǿ��ɡ�

1. Beautiful Soup�ļ��

����˵��Beautiful Soup��python��һ���⣬����Ҫ�Ĺ����Ǵ���ҳץȡ���ݡ��ٷ��������£�

Beautiful Soup�ṩһЩ�򵥵ġ�pythonʽ�ĺ����������������������޸ķ������ȹ��ܡ�����һ�������䣬ͨ�������ĵ�Ϊ�û��ṩ��Ҫץȡ�����ݣ���Ϊ�򵥣����Բ���Ҫ���ٴ���Ϳ���д��һ��������Ӧ�ó���

Beautiful Soup�Զ��������ĵ�ת��ΪUnicode���룬����ĵ�ת��Ϊutf-8���롣�㲻��Ҫ���Ǳ��뷽ʽ�������ĵ�û��ָ��һ�����뷽ʽ����ʱ��Beautiful Soup�Ͳ����Զ�ʶ����뷽ʽ�ˡ�Ȼ���������Ҫ˵��һ��ԭʼ���뷽ʽ�Ϳ����ˡ�

Beautiful Soup�ѳ�Ϊ��lxml��html6libһ����ɫ��python��������Ϊ�û������ṩ��ͬ�Ľ������Ի�ǿ�����ٶȡ�
�ϻ�����˵����������һ�°�~

2. Beautiful Soup ��װ

Beautiful Soup 3 Ŀǰ�Ѿ�ֹͣ�������Ƽ������ڵ���Ŀ��ʹ��Beautiful Soup 4���������Ѿ�����ֲ��BS4�ˣ�Ҳ����˵����ʱ������Ҫ import bs4 ���������������õİ汾�� Beautiful Soup 4.3.2 (���BS4)�������˵ BS4 �� Python3 ��֧�ֲ����ã��������õ��� Python2.7.7�������С����õ��� Python3 �汾�����Կ������� BS3 �汾��

������õ����°��Debain��Ubuntu,��ô����ͨ��ϵͳ���������������װ���������������°汾��Ŀǰ��4.2.1�汾

Python

sudo apt-get install Python-bs4
1
sudo apt-get install Python-bs4
����밲װ���µİ汾����ֱ�����ذ�װ�����ֶ���װ��Ҳ��ʮ�ַ���ķ������������Ұ�װ���� Beautiful Soup 4.3.2

Beautiful Soup 3.2.1Beautiful Soup 4.3.2

�������֮���ѹ

����������������ɰ�װ

Python

sudo python setup.py install
1
sudo python setup.py install
����ͼ��ʾ��֤����װ�ɹ���

2015-03-11 00:15:41 ����Ļ��ͼ

Ȼ����Ҫ��װ lxml

Python

sudo apt-get install Python-lxml
1
sudo apt-get install Python-lxml
Beautiful Soup֧��Python��׼���е�HTML������,��֧��һЩ�������Ľ�������������ǲ���װ������ Python ��ʹ�� PythonĬ�ϵĽ�������lxml ����������ǿ���ٶȸ��죬�Ƽ���װ��

3. ����Beautiful Soup ֮��

�������ȷ���ٷ��ĵ����ӣ�������������Щ�࣬Ҳ���������ڴ˱�������һ���������Ҳο���

�ٷ��ĵ�

4. ���� Beautiful Soup ����

���ȱ���Ҫ���� bs4 ��

Python

from bs4 import BeautifulSoup
1
from bs4 import BeautifulSoup
���Ǵ���һ���ַ�����������������Ǳ����������ʾ

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
���� beautifulsoup ����

Python

soup = BeautifulSoup(html)
1
soup = BeautifulSoup(html)
���⣬���ǻ������ñ��� HTML �ļ���������������

Python

soup = BeautifulSoup(open('index.html'))
1
soup = BeautifulSoup(open('index.html'))
������������ǽ����� index.html �ļ��򿪣����������� soup ����

������������ӡһ�� soup ��������ݣ���ʽ�����

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
���ϱ�������������ʽ����ӡ�����������ݣ�������������õ���С�����Ҫ�Ǻÿ���

5. �Ĵ��������

Beautiful Soup������HTML�ĵ�ת����һ�����ӵ����νṹ,ÿ���ڵ㶼��Python����,���ж�����Թ���Ϊ4��:

Tag
NavigableString
BeautifulSoup
Comment
�������ǽ���һһ����

��1��Tag

Tag ��ʲô��ͨ�׵㽲���� HTML �е�һ������ǩ������
<title>The Dormouse's story</title>

&lt;a class="sister" href="http://example.com/elsie" id="link1"&gt;Elsie&lt;/a&gt;
1
&lt;a class="sister" href="http://example.com/elsie" id="link1"&gt;Elsie&lt;/a&gt;
����� title a �ȵ� HTML ��ǩ����������������ݾ��� Tag����������������һ�������� Beautiful Soup ������ػ�ȡ Tags

����ÿһ�δ�����ע�Ͳ��ּ�Ϊ���н��
print soup.title
#<title>The Dormouse's story</title>
print soup.head
#<head><title>The Dormouse's story</title></head>
print soup.a
#<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>
print soup.p #ͬsoup.body.p
#<p class="title" name="dromouse"><b>The Dormouse's story</b></p>

���ǿ������� soup�ӱ�ǩ�����ɵػ�ȡ��Щ��ǩ�����ݣ��ǲ��Ǹо���������ʽ������ˣ�������һ���ǣ������ҵ��������������еĵ�һ������Ҫ��ı�ǩ�����Ҫ��ѯ���еı�ǩ�������ں�����н��ܡ�

���ǿ�����֤һ����Щ���������
print type(soup.a)
#<class 'bs4.element.Tag'>

���� Tag������������Ҫ�����ԣ��� name �� attrs���������Ƿֱ�������һ��
name
print soup.name
print soup.head.name
#[document]
#head
soup ������Ƚ����⣬���� name ��Ϊ [document]�����������ڲ���ǩ�������ֵ��Ϊ��ǩ��������ơ�

attrs
print soup.p.attrs
#{'class': ['title'], 'name': 'dromouse'}
��������ǰ� p ��ǩ���������Դ�ӡ����˳������õ���������һ���ֵ䡣

���������Ҫ������ȡĳ�����ԣ������������������ǻ�ȡ���� class ��ʲô
print soup.p['class']
#['title']

����������������get�������������Ե����ƣ������ǵȼ۵�
print soup.p.get('class')
#['title']

���ǿ��Զ���Щ���Ժ����ݵȵȽ����޸ģ�����
soup.p['class']="newClass"
print soup.p

#<p class="newClass" name="dromouse"><b>The Dormouse's story</b></p>
�����Զ�������Խ���ɾ��������

del soup.p['class']
print soup.p
#<p name="dromouse"><b>The Dormouse's story</b></p>

�����������޸�ɾ���Ĳ������������ǵ���Ҫ��;���ڴ˲�����ϸ�����ˣ��������Ҫ����鿴ǰ���ṩ�Ĺٷ��ĵ�

��2��NavigableString

��Ȼ�����Ѿ��õ��˱�ǩ�����ݣ���ô�������ˣ�����Ҫ���ȡ��ǩ�ڲ���������ô���أ��ܼ򵥣��� .string ���ɣ�����
print soup.p.string
#The Dormouse's story
�������Ǿ����ɻ�ȡ���˱�ǩ��������ݣ����������������ʽҪ���鷳������������һ�� NavigableString����������� ���Ա������ַ���������������û��ǳ���Ӣ�����ְɡ�
print type(soup.p.string)
#<class 'bs4.element.NavigableString'>
�����һ����������
print type(soup.p.string)
#<class 'bs4.element.NavigableString'>

��3��BeautifulSoup

BeautifulSoup �����ʾ����һ���ĵ���ȫ������.�󲿷�ʱ��,���԰������� Tag ������һ������� Tag�����ǿ��Էֱ��ȡ�������ͣ����ƣ��Լ�����������һ��
print type(soup.name)
#<type 'unicode'> #�������н����<type 'str'>
print soup.name 
# [document]
print soup.attrs 
#{} ���ֵ�

��4��Comment
Comment ������һ���������͵� NavigableString ������ʵ�����������Ȼ������ע�ͷ��ţ�����������úô����������ܻ�����ǵ��ı�����������벻�����鷳��
������һ����ע�͵ı�ǩ
print soup.a
print soup.a.string
print type(soup.a.string)

���н������
<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>
 Elsie 
<class 'bs4.element.Comment'>

a ��ǩ�������ʵ������ע�ͣ���������������� .string ������������ݣ����Ƿ������Ѿ���ע�ͷ���ȥ���ˣ���������ܻ�����Ǵ�������Ҫ���鷳��
�������Ǵ�ӡ������������ͣ���������һ�� Comment ���ͣ����ԣ�������ʹ��ǰ�����һ���жϣ��жϴ�������
if type(soup.a.string) == bs4.element.Comment:
    print soup.a.string
����Ĵ����У����������ж����������ͣ��Ƿ�Ϊ Comment ���ͣ�Ȼ���ٽ����������������ӡ�����

6. �����ĵ���
��1��ֱ���ӽڵ�
Ҫ�㣺.contents .children ����
.contents
tag �� .content ���Կ��Խ�tag���ӽڵ����б�ķ�ʽ���
print soup.head.contents 
#[<title>The Dormouse's story</title>]
�����ʽΪ�б����ǿ������б���������ȡ����ĳһ��Ԫ��
print soup.head.contents[0]
#<title>The Dormouse's story</title>

.children
�����صĲ���һ�� list���������ǿ���ͨ��������ȡ�����ӽڵ㡣
���Ǵ�ӡ��� .children ��һ�£����Է�������һ�� list ����������
print soup.head.children
# <listiterator object at 0x0000000005237E48>
for child in soup.head.children:
    print child
# <title>The Dormouse's story</title>

for child in soup.body.children:
    print child
    print "type: ", type(child) #����˺ܶ���У���Ϊ��NavigableString
�����

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

��������еĻ��з�ȥ�������ӡ����
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

��2����������ڵ�
֪ʶ�㣺.descendants ����
.descendants
.contents �� .children ���Խ�����tag��ֱ���ӽڵ㣬.descendants ���Կ��Զ�����tag������ڵ���еݹ�ѭ������ children���ƣ�����Ҳ��Ҫ������ȡ���е����ݡ�
for child in soup.descendants:
    print child
���н�����£����Է��֣����еĽڵ㶼����ӡ�����ˣ����������� HTML��ǩ����δ� head ��ǩһ�������룬�Դ����ơ�

3���ڵ�����
֪ʶ�㣺.string ����
���tagֻ��һ�� NavigableString �����ӽڵ�,��ô���tag����ʹ�� .string �õ��ӽڵ㡣���һ��tag����һ���ӽڵ�,��ô���tagҲ����ʹ�� .string ����,�������뵱ǰΨһ�ӽڵ�� .string �����ͬ��
ͨ�׵�˵���ǣ����һ����ǩ����û�б�ǩ�ˣ���ô .string �ͻ᷵�ر�ǩ��������ݡ������ǩ����ֻ��Ψһ��һ����ǩ�ˣ���ô .string Ҳ�᷵������������ݡ�����
print soup.head.string
#The Dormouse's story
print soup.title.string #ͬsoup.title.get_text()
#The Dormouse's story

���tag�����˶���ӽڵ�,tag���޷�ȷ����string ����Ӧ�õ����ĸ��ӽڵ������, .string ���������� None
print soup.html.string
# None

��4���������
֪ʶ�㣺 .strings .stripped_strings ����
.strings
��ȡ������ݣ�������Ҫ������ȡ���������������
for string in soup.html.strings:
    print (repr(string))
�����
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
������ַ����п��ܰ����˺ܶ�ո�����,ʹ�� .stripped_strings ����ȥ������հ�����
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

��5�����ڵ�
֪ʶ�㣺 .parent ����
p = soup.p
print p.parent.name
#body

content = soup.head.title.string
print content.parent.name
#title

��6��ȫ�����ڵ�
֪ʶ�㣺.parents ����
ͨ��Ԫ�ص� .parents ���Կ��Եݹ�õ�Ԫ�ص����и����ڵ㣬����
content = soup.head.title.string
for parent in  content.parents:
    print parent.name
title
head
html
[document]

��7���ֵܽڵ�
֪ʶ�㣺.next_sibling .previous_sibling ����
�ֵܽڵ�������Ϊ�ͱ��ڵ㴦��ͳһ���Ľڵ㣬.next_sibling ���Ի�ȡ�˸ýڵ����һ���ֵܽڵ㣬.previous_sibling ����֮�෴������ڵ㲻���ڣ��򷵻� None
ע�⣺ʵ���ĵ��е�tag�� .next_sibling �� .previous_sibling ����ͨ�����ַ�����հף���Ϊ�հ׻��߻���Ҳ���Ա�����һ���ڵ㣬���Եõ��Ľ�������ǿհ׻��߻���
print repr(soup.p.next_sibling)
#       ʵ�ʸô�Ϊ�հ� u'\n'
print soup.p.prev_sibling #ԭ������ƴ��prev_sibling���Ǹ�û�е����ԣ����Դ����None
# None   û��ǰһ���ֵܽڵ㣬���� None
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
# ��һ���ڵ����һ���ֵܽڵ������ǿ��Կ����Ľڵ�

��8��ȫ���ֵܽڵ�
֪ʶ�㣺.next_siblings .previous_siblings ����
ͨ�� .next_siblings �� .previous_siblings ���Կ��ԶԵ�ǰ�ڵ���ֵܽڵ�������
for sibling in soup.a.next_siblings:
    print repr(sibling)
# u',\n'
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>
# u' and\n'
# <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>
# u';\nand they lived at the bottom of a well.'

��9��ǰ��ڵ�
֪ʶ�㣺.next_element .previous_element ����
�� .next_sibling .previous_sibling ��ͬ����������������ֵܽڵ㣬���������нڵ㣬���ֲ��
���� head �ڵ�Ϊ
<head><title>The Dormouse's story</title></head>
��ô������һ���ڵ���� title�����ǲ��ֲ�ι�ϵ��
print soup.head.next_element
#<title>The Dormouse's story</title>

�Ƚ����£�
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

��10������ǰ��ڵ�
֪ʶ�㣺.next_elements .previous_elements ����
ͨ�� .next_elements �� .previous_elements �ĵ������Ϳ�����ǰ���������ĵ��Ľ�������,�ͺ����ĵ����ڱ�����һ��
aTags = soup.find_all("a")
print aTags
last_a_tag = aTags[len(aTags) - 1]
for element in last_a_tag.next_elements:
    print(repr(element))
�����
[<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>, <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>, <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]
u'Tillie'
u';\nand they lived at the bottom of a well.'
u'\n'
<p class="story">...</p>
u'...'
u'\n'

7.�����ĵ���
��1��find_all( name , attrs , recursive , text , **kwargs )
find_all() ����������ǰtag������tag�ӽڵ�,���ж��Ƿ���Ϲ�����������
1��name ����
name �������Բ�����������Ϊ name ��tag,�ַ�������ᱻ�Զ����Ե�
A.���ַ���
��򵥵Ĺ��������ַ���.�����������д���һ���ַ�������,Beautiful Soup��������ַ�������ƥ�������,������������ڲ����ĵ������е�<b>��ǩ
print soup.find_all('b')
# [<b>The Dormouse's story</b>]
print soup.find_all('a')
#[<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>, <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>, <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]

B.��������ʽ
�������������ʽ��Ϊ����,Beautiful Soup��ͨ��������ʽ�� match() ��ƥ������.�����������ҳ�������b��ͷ�ı�ǩ,���ʾ<body>��<b>��ǩ��Ӧ�ñ��ҵ�
import re
for tag in soup.find_all(re.compile("^b")):
    print(tag.name)
# body
# b

C.���б�
��������б����,Beautiful Soup�Ὣ���б�����һԪ��ƥ������ݷ���.��������ҵ��ĵ�������<a>��ǩ��<b>��ǩ
for tag in soup.find_all(["a", "b"]):
    print tag
# [<b>The Dormouse's story</b>,
#  <a class="sister" href="http://example.com/elsie" id="link1">Elsie</a>,
#  <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>,
#  <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]

D.�� True
True ����ƥ���κ�ֵ,���������ҵ����е�tag,���ǲ��᷵���ַ����ڵ�
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

E.������
���û�к��ʹ�����,��ô�����Զ���һ������,����ֻ����һ��Ԫ�ز��� [4] ,�������������� True ��ʾ��ǰԪ��ƥ�䲢�ұ��ҵ�,��������򷴻� False
���淽��У���˵�ǰԪ��,������� class ����ȴ������ id ����,��ô������ True:
def has_class_but_no_id(tag):
    return tag.has_attr('class') and not tag.has_attr('id')

for tag in soup.find_all(has_class_but_no_id):
    print tag.name
# p
# p
# p

2��keyword ����
ע�⣺���һ��ָ�����ֵĲ��������������õĲ�����,����ʱ��Ѹò�������ָ������tag������������,�������һ������Ϊ id �Ĳ���,Beautiful Soup������ÿ��tag�ġ�id������
for tag in soup.find_all(id='link2'):
    print tag
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>

������� href ����,Beautiful Soup������ÿ��tag�ġ�href������
for tag in soup.find_all(href=re.compile('elsie')):
    print tag
# <a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>

ʹ�ö��ָ�����ֵĲ�������ͬʱ����tag�Ķ������
for tag in soup.find_all(href=re.compile('elsie'), id='link1'):
    print tag
# <a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>

�������������� class ���ˣ����� class �� python �Ĺؼ��ʣ�����ô�죿�Ӹ��»��߾Ϳ���
for tag in soup.find_all("a", class_="sister"): #ͬsoup.find_all("a", attrs={"class": "sister"})
    print tag
# <a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>
# <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>

��Щtag��������������ʹ��,����HTML5�е� data-* ����
���ǿ���ͨ�� find_all() ������ attrs ��������һ���ֵ���������������������Ե�tag
for tag in soup.find_all("a", attrs={"class": "sister"}):
    print tag
# <a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>
# <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>

3��text ����
ͨ�� text �������������ĵ��е��ַ�������.�� name �����Ŀ�ѡֵһ��, text �������� �ַ��� , ������ʽ , �б�, True
print soup.find_all(text="Elsie") #�ڱ��أ�û������������ΪElsie��ע���ַ���
# [u'Elsie']
print soup.find_all(text=["Tillie", "Elsie", "Lacie"])
# [u'Elsie', u'Lacie', u'Tillie']
print soup.find_all(text=re.compile("Dormouse"))
# [u"The Dormouse's story", u"The Dormouse's story"]

4��limit ����
find_all() ��������ȫ���������ṹ,����ĵ����ܴ���ô���������.������ǲ���Ҫȫ�����,����ʹ�� limit �������Ʒ��ؽ��������.Ч����SQL�е�limit�ؼ�������,���������Ľ�������ﵽ limit ������ʱ,��ֹͣ�������ؽ��.
�ĵ�������3��tag������������,�����ֻ������2��,��Ϊ���������˷�������
soup.find_all("a", limit=2)
[<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>, <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>]

5��recursive ����
����tag�� find_all() ����ʱ,Beautiful Soup�������ǰtag����������ڵ�,���ֻ������tag��ֱ���ӽڵ�,����ʹ�ò��� recursive=False .
soup.html.find_all("title")
# [<title>The Dormouse's story</title>]
 
soup.html.find_all("title", recursive=False)
# []

��2��find( name , attrs , recursive , text , **kwargs )
���� find_all() ����Ψһ�������� find_all() �����ķ��ؽ����ֵ����һ��Ԫ�ص��б�,�� find() ����ֱ�ӷ��ؽ��
print soup.html.find("a", recursive=True)
# <a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>
print soup.html.find("a", recursive=False)
# None

��3��find_parents() find_parent()
find_all() �� find() ֻ������ǰ�ڵ�������ӽڵ�,���ӽڵ��. find_parents() �� find_parent() ����������ǰ�ڵ�ĸ����ڵ�,������������ͨtag������������ͬ,�����ĵ������ĵ�����������
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
�����
[<p class="story">Once upon a time there were three little sisters; and their names were\n<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>,\n<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a> and\n<a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>;\nand they lived at the bottom of a well.</p>, <body class="body">\n<p class="title" name="dromouse"><b>The Dormouse's story</b></p>\n<p class="story">Once upon a time there were three little sisters; and their names were\n<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>,\n<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a> and\n<a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>;\nand they lived at the bottom of a well.</p>\n<p class="story">...</p>\n</body>]
<p class="story">Once upon a time there were three little sisters; and their names were
<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>,
<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a> and
<a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>;
and they lived at the bottom of a well.</p>

4��find_next_siblings() find_next_sibling()
��2������ͨ�� .next_siblings ���ԶԵ� tag �����к���������ֵ� tag �ڵ���е���, find_next_siblings() �����������з��������ĺ�����ֵܽڵ�,find_next_sibling() ֻ���ط��������ĺ���ĵ�һ��tag�ڵ�
print soup.a.find_next_siblings(has_class)
# [<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>, <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]
print soup.a.find_next_sibling(has_class)
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>

��5��find_previous_siblings() find_previous_sibling()
��2������ͨ�� .previous_siblings ���ԶԵ�ǰ tag ��ǰ��������ֵ� tag �ڵ���е���, find_previous_siblings() �����������з���������ǰ����ֵܽڵ�, find_previous_sibling() �������ص�һ������������ǰ����ֵܽڵ�

��6��find_all_next() find_next()
��2������ͨ�� .next_elements ���ԶԵ�ǰ tag ��֮��� tag ���ַ������е���, find_all_next() �����������з��������Ľڵ�, find_next() �������ص�һ�����������Ľڵ�
print soup.a.find_all_next(has_class)
# [<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>, <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>, <p class="story">...</p>]
print soup.a.find_next(has_class)
# <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>

��7��find_all_previous() �� find_previous()
��2������ͨ�� .previous_elements ���ԶԵ�ǰ�ڵ�ǰ��� tag ���ַ������е���, find_all_previous() �����������з��������Ľڵ�, find_previous()�������ص�һ�����������Ľڵ�

8.CSSѡ����
������д CSS ʱ����ǩ�������κ����Σ�����ǰ�ӵ㣬id��ǰ�� #������������Ҳ�����������Ƶķ�����ɸѡԪ�أ��õ��ķ����� soup.select()������������ list
��1��ͨ����ǩ������
print soup.select("title")
# [<title>The Dormouse's story</title>]
print soup.select("a")
# [<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>, <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>, <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]
print soup.select("b")
# [<b>The Dormouse's story</b>]

��2��ͨ����������
print soup.select(".sister")
[<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>, <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>, <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]

3��ͨ�� id ������
print soup.select("#link1")
#[<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>]

��4����ϲ���
��ϲ��Ҽ���д class �ļ�ʱ����ǩ����������id�����е����ԭ����һ���ģ�������� p ��ǩ�У�id ���� link1�����ݣ�������Ҫ�ÿո�ֿ�
print soup.select('p #link2')
#[<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>]
ֱ���ӱ�ǩ����
print soup.select('html > head')
# [<head><title>The Dormouse's story</title></head>]
print soup.select('html > title')
# []
���Բ����ӱ�ǩ
print soup.select("html title")
# [<title>The Dormouse's story</title>]

��5�����Բ���
����ʱ�����Լ�������Ԫ�أ�������Ҫ����������������ע�����Ժͱ�ǩ����ͬһ�ڵ㣬�����м䲻�ܼӿո񣬷�����޷�ƥ�䵽��
print soup.select('a[id="link1"]')
#[<a class="sister" href="http://example.com/elsie" id="link1"><!-- Elsie --></a>]
ͬ����������Ȼ�������������ҷ�ʽ��ϣ�����ͬһ�ڵ�Ŀո������ͬһ�ڵ�Ĳ��ӿո�
print soup.select('p a[id="link2"]') #soup.select('body a[id="link2"]')
#[<a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>]




#-----------------------------------------------------------------------------------------
#python str��repr������ http://www.cnpythoner.com/post/251.html
����str(),repr()��``���������Ժ͹��ܷ��涼�ǳ����ƣ���ʵ��repr()��``��������ȫһ�������飬���Ƿ��ص���һ������ġ��ٷ����ַ�����ʾ��Ҳ����˵�����������¿���ͨ����ֵ���㣨ʹ���ڽ�����eval()�����µõ��ö���

��str()��������ͬ��str()����������һ������Ŀɶ��Ժõ��ַ�����ʾ�����ķ��ؽ��ͨ���޷�����eval()��ֵ�������ʺ�����print����������Ҫ�ٴ����ѵ��ǣ�����������repr()���ص��ַ������ܹ��� eval()�ڽ������õ�ԭ���Ķ��� Ҳ����˵ repr() ����� Python�Ƚ��Ѻã���str()��������û��Ƚ��Ѻá�

��Ȼ��ˣ��ܶ�����������ߵ������Ȼ������ȫһ���ġ� ��ҿ��Կ�������Ĵ��룬�����жԱ�

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


#--------------------BeautifulSoup ��ȡĳ��tag��ǩ���������
# �ο�http://blog.csdn.net/willib/article/details/52246086
# ��ȡ������վ��IP�Ͷ˿�
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
�����
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
# With ��䣬�Զ��ͷ���Դ
#�ο�https://www.ibm.com/developerworks/cn/opensource/os-cn-pythonwith/
#-----------------------------------------------------------------------------------------
#���ͼ��
def my_abs(x):
    if not isinstance(x, (int, float)):
        raise TypeError('bad operand type')
    if x >= 0:
        return x
    else:
        return -x
����˲�����������������Ĳ������ͣ������Ϳ����׳�һ������

>>> my_abs('A')
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
  File "<stdin>", line 3, in my_abs
TypeError: bad operand type
#-----------------------------------------------------------------------------------------
#Ĭ�ϲ���Ҫָ�򲻱���� https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431752945034eb82ac80a3e64b9bb4929b16eeed1eb9000
�ȶ���һ������������һ��list�����һ��END�ٷ��أ�
def add_end(L=[]):
    L.append('END')
    return L
������������ʱ������ƺ�����

>>> add_end([1, 2, 3])
[1, 2, 3, 'END']
>>> add_end(['x', 'y', 'z'])
['x', 'y', 'z', 'END']
����ʹ��Ĭ�ϲ�������ʱ��һ��ʼ���Ҳ�ǶԵģ�

>>> add_end()
['END']
���ǣ��ٴε���add_end()ʱ������Ͳ����ˣ�

>>> add_end()
['END', 'END']
>>> add_end()
['END', 'END', 'END']
�ܶ��ѧ�ߺ��ɻ�Ĭ�ϲ�����[]�����Ǻ����ƺ�ÿ�ζ�����ס�ˡ��ϴ������'END'���list��

ԭ��������£�

Python�����ڶ����ʱ��Ĭ�ϲ���L��ֵ�ͱ���������ˣ���[]����ΪĬ�ϲ���LҲ��һ����������ָ�����[]��ÿ�ε��øú���������ı���L�����ݣ����´ε���ʱ��Ĭ�ϲ��������ݾͱ��ˣ������Ǻ�������ʱ��[]�ˡ�

 ����Ĭ�ϲ���Ҫ�μ�һ�㣺Ĭ�ϲ�������ָ�򲻱����

def add_end(L=None):
    if L is None:
        L = []
    L.append('END')
    return L
���ڣ����۵��ö��ٴΣ������������⣺

>>> add_end()
['END']
>>> add_end()
['END']

#-----------------------------------------------------------------------------------------
#Ҫ����봫��ؼ��֣�ʹ��*
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431752945034eb82ac80a3e64b9bb4929b16eeed1eb9000
def person(name, age, *, city, job):
    print(name, age, city, job)

def person(name, age, *args, city, job): #���˿ɱ����֮�󣬾Ͳ���Ҫһ��������*�ָ���
    print(name, age, args, city, job)
    
if __name__ == "__main__":
    person("dingben", 28, city="sh", job="Engineer")

���ԣ��������⺯����������ͨ������func(*args, **kw)����ʽ���������������Ĳ�������ζ���ġ�
#-----------------------------------------------------------------------------------------
#Slice 
https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431756919644a792ee4ead724ef7afab3f7f771b04f5000
ǰ10������ÿ����ȡһ����
>>> L[:10:2]
[0, 2, 4, 6, 8]

��������ÿ5��ȡһ����
>>> L[::5]
[0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95]

����ʲô����д��ֻд[:]�Ϳ���ԭ������һ��list��
>>> L[:]
[0, 1, 2, 3, ..., 99]

�ַ���'xxx'Ҳ���Կ�����һ��list��ÿ��Ԫ�ؾ���һ���ַ�����ˣ��ַ���Ҳ��������Ƭ������ֻ�ǲ�����������ַ�����
>>> 'ABCDEFG'[:3]
'ABC'
>>> 'ABCDEFG'[::2]
'ACEG'
#-----------------------------------------------------------------------------------------
#����
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014317793224211f408912d9c04f2eac4d2af0d5d3d7b2000
���ԣ�������ʹ��forѭ��ʱ��ֻҪ������һ���ɵ�������forѭ���Ϳ����������У������ǲ�̫���ĸö��󾿾���list���������������͡�

��ô������ж�һ�������ǿɵ��������أ�������ͨ��collectionsģ���Iterable�����жϣ�

>>> from collections import Iterable
>>> isinstance('abc', Iterable) # str�Ƿ�ɵ���
True
>>> isinstance([1,2,3], Iterable) # list�Ƿ�ɵ���
True
>>> isinstance(123, Iterable) # �����Ƿ�ɵ���
False

���һ��С���⣬���Ҫ��listʵ������Java�������±�ѭ����ô�죿Python���õ�enumerate�������԰�һ��list�������-Ԫ�ضԣ������Ϳ�����forѭ����ͬʱ����������Ԫ�ر���
>>> for i, value in enumerate(['A', 'B', 'C']):
...     print(i, value)
...
0 A
1 B
2 C
#-----------------------------------------------------------------------------------------
#�б�������
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431779637539089fd627094a43a8a7c77e6102e3a811000
����ѭ��̫���������б�����ʽ�������һ��������ѭ�����������list��

>>> [x * x for x in range(1, 11)]
[1, 4, 9, 16, 25, 36, 49, 64, 81, 100]
д�б�����ʽʱ����Ҫ���ɵ�Ԫ��x * x�ŵ�ǰ�棬�����forѭ�����Ϳ��԰�list����������ʮ�����ã���д���Σ��ܿ�Ϳ�����Ϥ�����﷨��

forѭ�����滹���Լ���if�жϣ��������ǾͿ���ɸѡ����ż����ƽ����

>>> [x * x for x in range(1, 11) if x % 2 == 0]
[4, 16, 36, 64, 100]
������ʹ������ѭ������������ȫ���У�

>>> [m + n for m in 'ABC' for n in 'XYZ']
['AX', 'AY', 'AZ', 'BX', 'BY', 'BZ', 'CX', 'CY', 'CZ']
#-----------------------------------------------------------------------------------------
#������
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014317799226173f45ce40636141b6abc8424e12b5fb27000
ͨ���б�����ʽ�����ǿ���ֱ�Ӵ���һ���б����ǣ��ܵ��ڴ����ƣ��б������϶������޵ġ����ң�����һ������100���Ԫ�ص��б�����ռ�úܴ�Ĵ洢�ռ䣬������ǽ�����Ҫ����ǰ�漸��Ԫ�أ��Ǻ���������Ԫ��ռ�õĿռ䶼�װ��˷��ˡ�

���ԣ�����б�Ԫ�ؿ��԰���ĳ���㷨����������������Ƿ������ѭ���Ĺ����в��������������Ԫ���أ������Ͳ��ش���������list���Ӷ���ʡ�����Ŀռ䡣��Python�У�����һ��ѭ��һ�߼���Ļ��ƣ���Ϊ��������generator��

Ҫ����һ��generator���кܶ��ַ�������һ�ַ����ܼ򵥣�ֻҪ��һ���б�����ʽ��[]�ĳ�()���ʹ�����һ��generator��

>>> L = [x * x for x in range(10)]
>>> L
[0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
>>> g = (x * x for x in range(10))
>>> g
<generator object <genexpr> at 0x1022ef630>
����L��g�����������������[]��()��L��һ��list����g��һ��generator��

���ǿ���ֱ�Ӵ�ӡ��list��ÿһ��Ԫ�أ���������ô��ӡ��generator��ÿһ��Ԫ���أ�

���Ҫһ��һ����ӡ����������ͨ��next()�������generator����һ������ֵ��

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
���ǽ�����generator��������㷨��ÿ�ε���next(g)���ͼ����g����һ��Ԫ�ص�ֵ��ֱ�����㵽���һ��Ԫ�أ�û�и����Ԫ��ʱ���׳�StopIteration�Ĵ���

��Ȼ���������ֲ��ϵ���next(g)ʵ����̫��̬�ˣ���ȷ�ķ�����ʹ��forѭ������ΪgeneratorҲ�ǿɵ�������

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


Ҳ����˵������ĺ�����generator��һ��֮ң��Ҫ��fib�������generator��ֻ��Ҫ��print(b)��Ϊyield b�Ϳ����ˣ�

def fib(max):
    n, a, b = 0, 0, 1
    while n < max:
        yield b
        a, b = b, a + b
        n = n + 1
    return 'done'
    
ͬ���ģ��Ѻ����ĳ�generator�����ǻ����ϴ���������next()����ȡ��һ������ֵ������ֱ��ʹ��forѭ����������

>>> for n in fib(6):
...     print(n)
...
1
1
2
3
5
8
������forѭ������generatorʱ�������ò���generator��return���ķ���ֵ�������Ҫ�õ�����ֵ�����벶��StopIteration���󣬷���ֵ������StopIteration��value�У�

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


������Ƕ������£�

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
��ÿһ�п���һ��list����дһ��generator�����������һ�е�list��
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
�����
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
#������ https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143178254193589df9c612d2449618ea460e7a672a366000
if __name__ == "__main__":
    print(isinstance([], Iterable))
    print(isinstance({}, Iterable))
    print(isinstance("abc", Iterable))
    print(isinstance((x for x in range(10)), Iterable))
    print(isinstance(100, Iterable))
�����
True
True
True
True
False

����������������������forѭ���������Ա�next()�������ϵ��ò�������һ��ֵ��ֱ������׳�StopIteration�����ʾ�޷�����������һ��ֵ�ˡ�
���Ա�next()�������ò����Ϸ�����һ��ֵ�Ķ����Ϊ��������Iterator��
����ʹ��isinstance()�ж�һ�������Ƿ���Iterator����
if __name__ == "__main__":
    print(isinstance([], Iterator))
    print(isinstance({}, Iterator))
    print(isinstance("abc", Iterator))
    print(isinstance((x for x in range(10)), Iterator))
    print(isinstance(100, Iterator))
�����
False
False
False
True
False

����������Iterator���󣬵�list��dict��str��Ȼ��Iterable��ȴ����Iterator��
��list��dict��str��Iterable���Iterator����ʹ��iter()������
if __name__ == "__main__":
    print(isinstance(iter([]), Iterator))
    print(isinstance(iter({}), Iterator))
    print(isinstance(iter("abc"), Iterator))
    print(isinstance((x for x in range(10)), Iterator))
�����
True
True
True
True

������ΪPython��Iterator�����ʾ����һ����������Iterator������Ա�next()�������ò����Ϸ�����һ�����ݣ�ֱ��û������ʱ�׳�StopIteration���󡣿��԰����������������һ���������У�������ȴ������ǰ֪�����еĳ��ȣ�ֻ�ܲ���ͨ��next()����ʵ�ְ��������һ�����ݣ�����Iterator�ļ����Ƕ��Եģ�ֻ������Ҫ������һ������ʱ���Ż���㡣

Iterator�������Ա�ʾһ�����޴��������������ȫ����Ȼ������ʹ��list����Զ�����ܴ洢ȫ����Ȼ���ġ�

if __name__ == "__main__":
    l = list(range(10))
    iterL = iter(l)
    try:
        while True:
            print(next(iterL))
    except StopIteration as e:
        print(e.value)
�����
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

С��

���ǿ�������forѭ���Ķ�����Iterable���ͣ�

���ǿ�������next()�����Ķ�����Iterator���ͣ����Ǳ�ʾһ�����Լ�������У�

��������������list��dict��str����Iterable������Iterator����������ͨ��iter()�������һ��Iterator����

Python��forѭ�������Ͼ���ͨ�����ϵ���next()����ʵ�ֵģ����磺

for x in [1, 2, 3, 4, 5]:
    pass
ʵ������ȫ�ȼ��ڣ�

# ���Ȼ��Iterator����:
it = iter([1, 2, 3, 4, 5])
# ѭ��:
while True:
    try:
        # �����һ��ֵ:
        x = next(it)
    except StopIteration:
        # ����StopIteration���˳�ѭ��
        break
        
#range�ɵ��������ǲ��ǵ���������list�ɱ�Ϊlist����
print(isinstance(range(10), Iterable)) #True
print(isinstance(range(10), Iterator)) #False
t = iter(range(10))
print(isinstance(t, Iterable)) #True
print(isinstance(t, Iterator)) #True

#�������п�����list����list
g = (x * x for x in range(10))
print(g, list(g))
<generator object <genexpr> at 0x000002657F96E360> [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
#-----------------------------------------------------------------------------------------
#����ʽ��� https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014317848428125ae6aa24068b4c50a7e71501ab275d52000
����ʽ��̾���һ�ֳ���̶Ⱥܸߵı�̷�ʽ������ĺ���ʽ������Ա�д�ĺ���û�б�������ˣ�����һ��������ֻҪ������ȷ���ģ��������ȷ���ģ����ִ��������ǳ�֮Ϊû�и����á�������ʹ�ñ����ĳ���������ԣ����ں����ڲ��ı���״̬��ȷ����ͬ�������룬���ܵõ���ͬ���������ˣ����ֺ������и����õġ�

����ʽ��̵�һ���ص���ǣ�����Ѻ���������Ϊ����������һ����������������һ��������

Python�Ժ���ʽ����ṩ����֧�֡�����Python����ʹ�ñ�������ˣ�Python���Ǵ�����ʽ������ԡ�
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
�����
[0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
<map object at 0x000002BB2F31A7F0>
True
True

map()����ĵ�һ��������f�����������������ڽ��r��һ��Iterator��Iterator�Ƕ������У����ͨ��list()�����������������ж��������������һ��list��
#����������python3��python2��һ����python3 map�õ�����һ��map����python2��ȡ������һ��list����


�ٿ�reduce���÷���reduce��һ������������һ������[x1, x2, x3, ...]�ϣ�������������������������reduce�ѽ�����������е���һ��Ԫ�����ۻ����㣬��Ч�����ǣ�
reduce(f, [x1, x2, x3, x4]) = f(f(f(x1, x2), x3), x4)

# -*- coding: utf-8 -*-
from functools import reduce

if __name__ == "__main__":
    r = reduce(lambda x, y : x + y, range(101))
    print(r) #5050 = sum(range(101))

���Ҫ������[1, 3, 5, 7, 9]�任������13579��reduce�Ϳ��������ó���
r = reduce(lambda x, y: x * 10 + y, range(1, 10, 2)) #13579

#ͬ��
def fn(x, y):
    return x * 10 + y
if __name__ == "__main__":
    r = reduce(fn, [1, 3, 5, 7, 9])
    print(r)
    
������ӱ���û����ô������ǣ�������ǵ��ַ���strҲ��һ�����У�������������ԼӸĶ������map()�����ǾͿ���д����strת��Ϊint�ĺ�����
from functools import reduce

def fn(x, y):
    return x * 10 + y

def char2num(s):
    digits = {'0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9}
    return digits[s]

if __name__ == "__main__":
    r = reduce(fn, map(char2num, '13579'))
    print(r) #13579
    
    
�����һ��str2int�ĺ������ǣ�
from functools import reduce

DIGITS = {'0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9}

def str2int(s):
    def fn(x, y):
        return x * 10 + y
    def char2num(s):
        return DIGITS[s]
    return reduce(fn, map(char2num, s))

#��lambda���ʽ��
# -*- coding: utf-8 -*-
from functools import reduce

DIGITS = {'0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9}


def str2int(s):
    def char2num(s):
        return DIGITS[s]

    return reduce(lambda x, y: x * 10 + y, map(char2num, s))


if __name__ == "__main__":
    print(str2int('1357913579135791357913579135791357913579135791357913579135791357913579135791357913579'))
�����
1357913579135791357913579135791357913579135791357913579135791357913579135791357913579
#-----------------------------------------------------------------------------------------
#filter()
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431821084171d2e0f22e7cc24305ae03aa0214d0ef29000
�ɼ���filter()����߽׺������ؼ�������ȷʵ��һ����ɸѡ��������

ע�⵽filter()�������ص���һ��Iterator��Ҳ����һ���������У�����Ҫǿ��filter()��ɼ���������Ҫ��list()����������н��������list��

#����
# -*- coding: utf-8 -*-
def filterEven(number):
    return number % 2 == 0


if __name__ == "__main__":
    l = (x for x in range(10))
    oddList = filter(filterEven, l)#oddList��һ���������У��˴���û�е���filterEven����oddListֵ��������forѭ�����ٵ���filterEven����
    for x in oddList:
        print(x)


��filter������

����������һ�������ǰ���ɸ���������㷨��������ǳ��򵥣�

���ȣ��г���2��ʼ��������Ȼ��������һ�����У�

2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...

ȡ���еĵ�һ����2����һ����������Ȼ����2�����е�2�ı���ɸ����

3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...

ȡ�����еĵ�һ����3����һ����������Ȼ����3�����е�3�ı���ɸ����

5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...

ȡ�����еĵ�һ����5��Ȼ����5�����е�5�ı���ɸ����

7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...

����ɸ��ȥ���Ϳ��Եõ����е�������

��Python��ʵ������㷨�������ȹ���һ����3��ʼ���������У�

def _odd_iter():
    n = 1
    while True:
        n = n + 2
        yield n
ע������һ����������������һ���������С�

Ȼ����һ��ɸѡ������

def _not_divisible(n):
    return lambda x: x % n > 0
��󣬶���һ�������������Ϸ�����һ��������

def primes():
    yield 2
    it = _odd_iter() # ��ʼ����
    while True:
        n = next(it) # �������еĵ�һ����
        yield n
        it = filter(_not_divisible(n), it) # ����������
����������ȷ��ص�һ������2��Ȼ������filter()���ϲ���ɸѡ����µ����С�

����primes()Ҳ��һ���������У����Ե���ʱ��Ҫ����һ���˳�ѭ����������

# ��ӡ1000���ڵ�����:
for n in primes():
    if n < 1000:
        print(n)
    else:
        break
ע�⵽Iterator�Ƕ��Լ�������У��������ǿ�����Python��ʾ��ȫ����Ȼ��������ȫ�����������������У�������ǳ���ࡣ

#ȫ������
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
        it = filter(notDivisible(n), it)#�������е�����ʱ��û�м���ÿ�������Ƿ���Ա�����������������ȡnext��ʱ��Ž��м��㣬��������nΪ5ʱ��������Ƿ�5 % 3 != 0��nΪ7ʱ������� 7 % 3 != 0 && 7 % 5 != 0����nΪ9ʱ��������9 % 3 != 0����n�����������һ��ֵ11������Ҫ����11 % 3 != 0 && 11 % 5 != 0 && 11 % 7 != 0����������...


if __name__ == "__main__":
    for n in primes():
        if n < 1000:
            print(n)
        else:
            break

#�ж��Ƿ��ǻ������֣�
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
�����
['about', 'bob', 'Credit', 'Zoo']

Ҫ���з������򣬲��ظĶ�key���������Դ������������reverse=True��
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
�����
[('Adam', 92), ('Bart', 66), ('Bob', 75), ('Lisa', 88)]
[('Bart', 66), ('Bob', 75), ('Lisa', 88), ('Adam', 92)]
#-----------------------------------------------------------------------------------------
#���� https://www.cnblogs.com/pengwang57/p/7183468.html
Python list����sort()������������Ҳ������python���õ�ȫ��sorted()�������Կɵ������������������µ����С�

1���������

�򵥵����������Ƿǳ����׵ġ�ֻ��Ҫ����sorted()������������һ���µ�list���µ�list��Ԫ�ػ���С�������(__lt__)������

>>> sorted([5, 2, 3, 1, 4])
[1, 2, 3, 4, 5]
������Ҳ����ʹ��list.sort()���������򣬴�ʱlist�������޸ġ�ͨ���˷�������sorted()���㣬��������㲻��Ҫ����ԭ����list���˷���������Ч��

>>> a = [5, 2, 3, 1, 4]
>>> a.sort()
>>> a
[1, 2, 3, 4, 5]
������һ����ͬ����list.sort()��������������list�У��෴��sorted()���������еĿɵ������ж���Ч��

>>> sorted({1: 'D', 2: 'B', 3: 'B', 4: 'E', 5: 'A'})
[1, 2, 3, 4, 5]
����

2��key����/����

��python2.4��ʼ��list.sort()��sorted()����������key������ָ��һ���������˺�������ÿ��Ԫ�رȽ�ǰ�����á� ����ͨ��keyָ���ĺ����������ַ����Ĵ�Сд��

>>> sorted("This is a test string from Andrew".split(), key=str.lower)
['a', 'Andrew', 'from', 'is', 'string', 'test', 'This']
����key������ֵΪһ���������˺���ֻ��һ�������ҷ���һ��ֵ�������бȽϡ���������ǿ��ٵ���Ϊkeyָ���ĺ�����׼ȷ�ض�ÿ��Ԫ�ص��á�

 

���㷺��ʹ��������ø��Ӷ����ĳЩֵ���Ը��Ӷ���������������磺

>>> student_tuples = [
        ('john', 'A', 15),
        ('jane', 'B', 12),
        ('dave', 'B', 10),
]
>>> sorted(student_tuples, key=lambda student: student[2])   # sort by age
[('dave', 'B', 10), ('jane', 'B', 12), ('john', 'A', 15)]
����ͬ���ļ�����ӵ���������Եĸ��Ӷ���Ҳ���ã����磺

���ƴ���
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
���ƴ���
3��Operator ģ�麯��

�����key������ʹ�÷ǳ��㷺�����python�ṩ��һЩ����ĺ�����ʹ�÷��ʷ����������׺Ϳ��١�operatorģ����itemgetter��attrgetter����2.6��ʼ��������methodcaller������ʹ����Щ����������Ĳ�������ø��Ӽ��Ϳ��٣�

>>> from operator import itemgetter, attrgetter
>>> sorted(student_tuples, key=itemgetter(2))
[('dave', 'B', 10), ('jane', 'B', 12), ('john', 'A', 15)]
>>> sorted(student_objects, key=attrgetter('age'))
[('dave', 'B', 10), ('jane', 'B', 12), ('john', 'A', 15)]
����operatorģ�黹����༶���������磬����grade��Ȼ������age������

>>> sorted(student_tuples, key=itemgetter(1,2))
[('john', 'A', 15), ('dave', 'B', 10), ('jane', 'B', 12)]
>>> sorted(student_objects, key=attrgetter('grade', 'age'))
[('john', 'A', 15), ('dave', 'B', 10), ('jane', 'B', 12)]


#-----------------------------------------------------------------------------------------
#���� #https://docs.python.org/3/howto/sorting.html
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
#���غ���
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431835236741e42daf5af6514f1a8917b8aaadff31bf000

������ʵ��һ���ɱ��������͡�ͨ������£���͵ĺ�������������ģ�

def calc_sum(*args):
    ax = 0
    for n in args:
        ax = ax + n
    return ax
���ǣ��������Ҫ������ͣ������ں���Ĵ����У�������Ҫ�ټ�����ô�죿���Բ�������͵Ľ�������Ƿ�����͵ĺ�����

def lazy_sum(*args):
    def sum():
        ax = 0
        for n in args:
            ax = ax + n
        return ax
    return sum
�����ǵ���lazy_sum()ʱ�����صĲ�������ͽ����������ͺ�����

>>> f = lazy_sum(1, 3, 5, 7, 9)
>>> f
<function lazy_sum.<locals>.sum at 0x101c6ed90>
���ú���fʱ��������������͵Ľ����

>>> f()
25

����������У������ں���lazy_sum���ֶ����˺���sum�����ң��ڲ�����sum���������ⲿ����lazy_sum�Ĳ����;ֲ���������lazy_sum���غ���sumʱ����ز����ͱ����������ڷ��صĺ����У����ֳ�Ϊ���հ���Closure�����ĳ���ṹӵ�м����������

����ע��һ�㣬�����ǵ���lazy_sum()ʱ��ÿ�ε��ö��᷵��һ���µĺ�������ʹ������ͬ�Ĳ�����

>>> f1 = lazy_sum(1, 3, 5, 7, 9)
>>> f2 = lazy_sum(1, 3, 5, 7, 9)
>>> f1==f2
False
f1()��f2()�ĵ��ý������Ӱ�졣


�հ�

ע�⵽���صĺ������䶨���ڲ������˾ֲ�����args�����ԣ���һ������������һ�����������ڲ��ľֲ����������º������ã����ԣ��հ��������򵥣�ʵ�������ɲ����ס�

��һ����Ҫע��������ǣ����صĺ�����û������ִ�У�����ֱ��������f()��ִ�С���������һ�����ӣ�

def count():
    fs = []
    for i in range(1, 4):
        def f():
             return i*i
        fs.append(f)
    return fs

f1, f2, f3 = count()
������������У�ÿ��ѭ������������һ���µĺ�����Ȼ�󣬰Ѵ�����3�������������ˡ�

�������Ϊ����f1()��f2()��f3()���Ӧ����1��4��9����ʵ�ʽ���ǣ�

>>> f1()
9
>>> f2()
9
>>> f3()
9
ȫ������9��ԭ������ڷ��صĺ��������˱���i��������������ִ�С��ȵ�3������������ʱ�����������õı���i�Ѿ������3��������ս��Ϊ9��

���رհ�ʱ�μ�һ�㣺���غ�����Ҫ�����κ�ѭ�����������ߺ����ᷢ���仯�ı�����
 
 ���һ��Ҫ����ѭ��������ô�죿�������ٴ���һ���������øú����Ĳ�����ѭ��������ǰ��ֵ�����۸�ѭ������������θ��ģ��Ѱ󶨵�����������ֵ���䣺

def count():
    def f(j):
        def g():
            return j*j
        return g
    fs = []
    for i in range(1, 4):
        fs.append(f(i)) # f(i)���̱�ִ�У����i�ĵ�ǰֵ������f()
    return fs
�ٿ��������

>>> f1, f2, f3 = count()
>>> f1()
1
>>> f2()
4
>>> f3()
9

���رհ�ʱ�μ�һ�㣺���غ�����Ҫ�����κ�ѭ�����������ߺ����ᷢ���仯�ı�����
���һ��Ҫ����ѭ��������ô�죿�������ٴ���һ���������øú����Ĳ�����ѭ��������ǰ��ֵ�����۸�ѭ������������θ��ģ��Ѱ󶨵�����������ֵ���䣺
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


#���ñհ�����һ��������������ÿ�ε��������ص���������
Python 3.x������nonlocal�ؼ��֣��������ڱ�ʶ�ⲿ������ı�����
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
#��������
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431843456408652233b88b424613aa8ec2fe032fd85a000
def build(x, y):
    return lambda: x * x + y * y

if __name__ == "__main__":
    f = build(3, 4)
    print(f())
#-----------------------------------------------------------------------------------------
#װ���� https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014318435599930270c0381a3b44db991cd6d858064ac0000
���ں���Ҳ��һ�����󣬶��Һ���������Ա���ֵ�����������ԣ�ͨ������Ҳ�ܵ��øú�����

>>> def now():
...     print('2015-3-25')
...
>>> f = now
>>> f()
2015-3-25
����������һ��__name__���ԣ������õ����������֣�

>>> now.__name__
'now'
>>> f.__name__
'now'

���ڣ���������Ҫ��ǿnow()�����Ĺ��ܣ����磬�ں�������ǰ���Զ���ӡ��־�����ֲ�ϣ���޸�now()�����Ķ��壬�����ڴ��������ڼ䶯̬���ӹ��ܵķ�ʽ����֮Ϊ��װ��������Decorator����

�����ϣ�decorator����һ�����غ����ĸ߽׺��������ԣ�����Ҫ����һ���ܴ�ӡ��־��decorator�����Զ������£�

def log(func):
    def wrapper(*args, **kw):
        print('call %s():' % func.__name__)
        return func(*args, **kw)
    return wrapper
�۲������log����Ϊ����һ��decorator�����Խ���һ��������Ϊ������������һ������������Ҫ����Python��@�﷨����decorator���ں����Ķ��崦��

@log
def now():
    print('2015-3-25')
����now()����������������now()������������������now()����ǰ��ӡһ����־��

>>> now()
call now():
2015-3-25
��@log�ŵ�now()�����Ķ��崦���൱��ִ������䣺

now = log(now)
����log()��һ��decorator������һ�����������ԣ�ԭ����now()������Ȼ���ڣ�ֻ������ͬ����now����ָ�����µĺ��������ǵ���now()��ִ���º���������log()�����з��ص�wrapper()������

wrapper()�����Ĳ���������(*args, **kw)����ˣ�wrapper()�������Խ�����������ĵ��á���wrapper()�����ڣ����ȴ�ӡ��־���ٽ����ŵ���ԭʼ������

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
�����
call now():
abc
wrapper

���decorator������Ҫ����������Ǿ���Ҫ��дһ������decorator�ĸ߽׺�����д����������ӡ����磬Ҫ�Զ���log���ı���
def log(text):
    def decorator(func):
        def wrapper(*args, **kw):
            print('%s %s():' % (text, func.__name__))
            return func(*args, **kw)
        return wrapper
    return decorator
���3��Ƕ�׵�decorator�÷����£�

@log('execute')
def now():
    print('2015-3-25')
ִ�н�����£�

>>> now()
execute now():
2015-3-25
������Ƕ�׵�decorator��ȣ�3��Ƕ�׵�Ч���������ģ�

>>> now = log('execute')(now)
�����������������䣬����ִ��log('execute')�����ص���decorator�������ٵ��÷��صĺ�����������now����������ֵ������wrapper������
��������decorator�Ķ��嶼û�����⣬���������һ������Ϊ���ǽ��˺���Ҳ�Ƕ�������__name__�����ԣ�����ȥ������decoratorװ��֮��ĺ��������ǵ�__name__�Ѿ���ԭ����'now'�����'wrapper'��
>>> now.__name__
'wrapper'

��Ϊ���ص��Ǹ�wrapper()�������־���'wrapper'�����ԣ���Ҫ��ԭʼ������__name__�����Ը��Ƶ�wrapper()�����У�������Щ��������ǩ���Ĵ���ִ�оͻ����
����Ҫ��дwrapper.__name__ = func.__name__�����Ĵ��룬Python���õ�functools.wraps���Ǹ�����µģ����ԣ�һ��������decorator��д�����£�

import functools

def log(func):
    @functools.wraps(func)
    def wrapper(*args, **kw):
        print('call %s():' % func.__name__)
        return func(*args, **kw)
    return wrapper
������Դ�������decorator��

import functools

def log(text):
    def decorator(func):
        @functools.wraps(func)
        def wrapper(*args, **kw):
            print('%s %s():' % (text, func.__name__))
            return func(*args, **kw)
        return wrapper
    return decorator
import functools�ǵ���functoolsģ�顣ģ��ĸ����Ժ򽲽⡣���ڣ�ֻ���ס�ڶ���wrapper()��ǰ�����@functools.wraps(func)���ɡ�
#-----------------------------------------------------------------------------------------
#ƫ���� https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143184474383175eeea92a8b0439fab7b392a8a32f8fa000
Python��functoolsģ���ṩ�˺ܶ����õĹ��ܣ�����һ������ƫ������Partial function����Ҫע�⣬�����ƫ��������ѧ�����ϵ�ƫ������һ����

�ڽ��ܺ���������ʱ�����ǽ�����ͨ���趨������Ĭ��ֵ�����Խ��ͺ������õ��Ѷȡ���ƫ����Ҳ����������һ�㡣�������£�

int()�������԰��ַ���ת��Ϊ���������������ַ���ʱ��int()����Ĭ�ϰ�ʮ����ת����

>>> int('12345')
12345
��int()�������ṩ�����base������Ĭ��ֵΪ10���������base�������Ϳ�����N���Ƶ�ת����

>>> int('12345', base=8)
5349
>>> int('12345', 16)
74565
����Ҫת�������Ķ������ַ�����ÿ�ζ�����int(x, base=2)�ǳ��鷳�����ǣ������뵽�����Զ���һ��int2()�ĺ�����Ĭ�ϰ�base=2����ȥ��

def int2(x, base=2):
    return int(x, base)
����������ת�������ƾͷǳ������ˣ�

>>> int2('1000000')
64
>>> int2('1010101')
85
functools.partial���ǰ������Ǵ���һ��ƫ�����ģ�����Ҫ�����Լ�����int2()������ֱ��ʹ������Ĵ��봴��һ���µĺ���int2��

>>> import functools
>>> int2 = functools.partial(int, base=2)
>>> int2('1000000')
64
>>> int2('1010101')
85
���ԣ����ܽ�functools.partial�����þ��ǣ���һ��������ĳЩ�������̶�ס��Ҳ��������Ĭ��ֵ��������һ���µĺ�������������º�������򵥡�

ע�⵽������µ�int2�����������ǰ�base���������趨Ĭ��ֵΪ2����Ҳ�����ں�������ʱ��������ֵ��

>>> int2('1000000', base=10)
1000000
��󣬴���ƫ����ʱ��ʵ���Ͽ��Խ��պ�������*args��**kw��3�������������룺

int2 = functools.partial(int, base=2)
ʵ���Ϲ̶���int()�����Ĺؼ��ֲ���base��Ҳ���ǣ�

int2('10010')
�൱�ڣ�

kw = { 'base': 2 }
int('10010', **kw)
�����룺

max2 = functools.partial(max, 10)
ʵ���ϻ��10��Ϊ*args��һ�����Զ��ӵ���ߣ�Ҳ���ǣ�

max2(5, 6, 7)
�൱�ڣ�

args = (10, 5, 6, 7)
max(*args)
���Ϊ10��

С��

�������Ĳ�������̫�࣬��Ҫ��ʱ��ʹ��functools.partial���Դ���һ���µĺ���������º������Թ̶�סԭ�����Ĳ��ֲ������Ӷ��ڵ���ʱ���򵥡�


# -*- coding: utf-8 -*-
import functools


def test(a=3, b=4):
    return a + b


if __name__ == "__main__":
    print(test())
    new_test = functools.partial(test, a=7, b=8)
    print(new_test(a=10, b=11))
    print(new_test(10, 11))#�൱��new_test(10, 11, a=7, b=8)
#-----------------------------------------------------------------------------------------
#ʹ��ģ�� https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431845183474e20ee7e7828b47f7b7607f2dc1e90dbb000
������

��һ��ģ���У����ǿ��ܻᶨ��ܶຯ���ͱ��������еĺ����ͱ�������ϣ��������ʹ�ã��еĺ����ͱ�������ϣ��������ģ���ڲ�ʹ�á���Python�У���ͨ��_ǰ׺��ʵ�ֵġ�

�����ĺ����ͱ������ǹ����ģ�public�������Ա�ֱ�����ã����磺abc��x123��PI�ȣ�

����__xxx__�����ı�����������������Ա�ֱ�����ã�������������;�����������__author__��__name__�������������helloģ�鶨����ĵ�ע��Ҳ�������������__doc__���ʣ������Լ��ı���һ�㲻Ҫ�����ֱ�������

����_xxx��__xxx�����ĺ�����������Ƿǹ����ģ�private������Ӧ�ñ�ֱ�����ã�����_abc��__abc�ȣ�

֮��������˵��private�����ͱ�������Ӧ�á���ֱ�����ã������ǡ����ܡ���ֱ�����ã�����ΪPython��û��һ�ַ���������ȫ���Ʒ���private��������������ǣ��ӱ��ϰ���ϲ�Ӧ������private�����������

private�����������Ӧ�ñ��������ã���������ʲô���أ��뿴���ӣ�

def _private_1(name):
    return 'Hello, %s' % name

def _private_2(name):
    return 'Hi, %s' % name

def greeting(name):
    if len(name) > 3:
        return _private_1(name)
    else:
        return _private_2(name)
������ģ���﹫��greeting()�����������ڲ��߼���private�������������ˣ�����������greeting()�������ù����ڲ���private����ϸ�ڣ���Ҳ��һ�ַǳ����õĴ����װ�ͳ���ķ���������

�ⲿ����Ҫ���õĺ���ȫ�������private��ֻ���ⲿ��Ҫ���õĺ����Ŷ���Ϊpublic��
#-----------------------------------------------------------------------------------------
#ģ������·��
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143186362353505516c5d4e38456fb225c18cc5b54ffb000
 
��������ͼ����һ��ģ��ʱ��Python����ָ����·����������Ӧ��.py�ļ�������Ҳ������ͻᱨ��

>>> import mymodule
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
ImportError: No module named mymodule
Ĭ������£�Python��������������ǰĿ¼�������Ѱ�װ������ģ��͵�����ģ�飬����·�������sysģ���path�����У�

>>> import sys
>>> sys.path
['', '/Library/Frameworks/Python.framework/Versions/3.6/lib/python36.zip', '/Library/Frameworks/Python.framework/Versions/3.6/lib/python3.6', ..., '/Library/Frameworks/Python.framework/Versions/3.6/lib/python3.6/site-packages']
�������Ҫ����Լ�������Ŀ¼�������ַ�����

һ��ֱ���޸�sys.path�����Ҫ������Ŀ¼��

>>> import sys
>>> sys.path.append('/Users/michael/my_py_scripts')
���ַ�����������ʱ�޸ģ����н�����ʧЧ��

�ڶ��ַ��������û�������PYTHONPATH���û������������ݻᱻ�Զ���ӵ�ģ������·���С����÷�ʽ������Path�����������ơ�ע��ֻ��Ҫ������Լ�������·����Python�Լ����������·������Ӱ�졣
#-----------------------------------------------------------------------------------------
#��������
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
    #print(ben.name) #�޷�����
    print(ben._Student__name)#���Է���
����󣬶����ⲿ������˵��ûʲô�䶯�������Ѿ��޷����ⲿ����ʵ������.__name��ʵ������.__score�ˣ�

��Ҫע����ǣ���Python�У�����������__xxx__�ģ�Ҳ������˫�»��߿�ͷ��������˫�»��߽�β�ģ��������������������ǿ���ֱ�ӷ��ʵģ�����private���������ԣ�������__name__��__score__�����ı�������

��Щʱ����ῴ����һ���»��߿�ͷ��ʵ��������������_name��������ʵ�������ⲿ�ǿ��Է��ʵģ����ǣ�����Լ���׳ɵĹ涨�����㿴�������ı���ʱ����˼���ǣ�����Ȼ�ҿ��Ա����ʣ����ǣ��������Ϊ˽�б�������Ҫ������ʡ���

˫�»��߿�ͷ��ʵ�������ǲ���һ�����ܴ��ⲿ�����أ���ʵҲ���ǡ�����ֱ�ӷ���__name����ΪPython�����������__name�����ĳ���_Student__name�����ԣ���Ȼ����ͨ��_Student__name������__name������

>>> bart._Student__name
'Bart Simpson'
����ǿ�ҽ����㲻Ҫ��ô�ɣ���Ϊ��ͬ�汾��Python���������ܻ��__name�ĳɲ�ͬ�ı�������

#-----------------------------------------------------------------------------------------
#�̳кͶ�̬ https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431865288798deef438d865e4c2985acff7e9fad15e3000
��̬���� vs ��̬����

���ھ�̬���ԣ�����Java����˵�������Ҫ����Animal���ͣ�����Ķ��������Animal���ͻ����������࣬���򣬽��޷�����run()������

����Python�����Ķ�̬������˵����һ����Ҫ����Animal���͡�����ֻ��Ҫ��֤����Ķ�����һ��run()�����Ϳ����ˣ�

class Timer(object):
    def run(self):
        print('Start...')
����Ƕ�̬���Եġ�Ѽ�����͡���������Ҫ���ϸ�ļ̳���ϵ��һ������ֻҪ����������Ѽ�ӣ�����·����Ѽ�ӡ��������Ϳ��Ա�������Ѽ�ӡ�

Python�ġ�file-like object������һ��Ѽ�����͡����������ļ���������һ��read()���������������ݡ����ǣ�������ֻҪ��read()������������Ϊ��file-like object������ຯ�����յĲ������ǡ�file-like object�����㲻һ��Ҫ�����������ļ�������ȫ���Դ����κ�ʵ����read()�����Ķ���
#-----------------------------------------------------------------------------------------
#��ȡ������Ϣ
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001431866385235335917b66049448ab14a499afd5b24db000
type()�������ص���ʲô�����أ������ض�Ӧ��Class���͡��������Ҫ��if������жϣ�����Ҫ�Ƚ�����������type�����Ƿ���ͬ��

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

�жϻ����������Ϳ���ֱ��дint��str�ȣ������Ҫ�ж�һ�������Ƿ��Ǻ�����ô�죿����ʹ��typesģ���ж���ĳ�����
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

ʹ��isinstance()

����class�ļ̳й�ϵ��˵��ʹ��type()�ͺܲ����㡣����Ҫ�ж�class�����ͣ�����ʹ��isinstance()������

���ǻع��ϴε����ӣ�����̳й�ϵ�ǣ�

object -> Animal -> Dog -> Husky
��ô��isinstance()�Ϳ��Ը������ǣ�һ�������Ƿ���ĳ�����͡��ȴ���3�����͵Ķ���

>>> a = Animal()
>>> d = Dog()
>>> h = Husky()
Ȼ���жϣ�

>>> isinstance(h, Husky)
True
û�����⣬��Ϊh����ָ��ľ���Husky����

���жϣ�

>>> isinstance(h, Dog)
True
h��Ȼ������Husky���ͣ�������Husky�Ǵ�Dog�̳������ģ����ԣ�hҲ����Dog���͡����仰˵��isinstance()�жϵ���һ�������Ƿ��Ǹ����ͱ�������λ�ڸ����͵ĸ��̳����ϡ�

��ˣ����ǿ���ȷ�ţ�h����Animal���ͣ�

>>> isinstance(h, Animal)
True
ͬ��ʵ��������Dog��dҲ��Animal���ͣ�

>>> isinstance(d, Dog) and isinstance(d, Animal)
True
���ǣ�d����Husky���ͣ�

>>> isinstance(d, Husky)
False
����type()�жϵĻ�������Ҳ������isinstance()�жϣ�

>>> isinstance('a', str)
True
>>> isinstance(123, int)
True
>>> isinstance(b'a', bytes)
True
���һ������ж�һ�������Ƿ���ĳЩ�����е�һ�֣���������Ĵ���Ϳ����ж��Ƿ���list����tuple��

>>> isinstance([1, 2, 3], (list, tuple))
True
>>> isinstance((1, 2, 3), (list, tuple))
True

ʹ��dir()

���Ҫ���һ��������������Ժͷ���������ʹ��dir()������������һ�������ַ�����list�����磬���һ��str������������Ժͷ�����

>>> dir('ABC')
['__add__', '__class__',..., '__subclasshook__', 'capitalize', 'casefold',..., 'zfill']
����__xxx__�����Ժͷ�����Python�ж�����������;�ģ�����__len__�������س��ȡ���Python�У���������len()������ͼ��ȡһ������ĳ��ȣ�ʵ���ϣ���len()�����ڲ������Զ�ȥ���øö����__len__()���������ԣ�����Ĵ����ǵȼ۵ģ�

>>> len('ABC')
3
>>> 'ABC'.__len__()
3
�����Լ�д���࣬���Ҳ����len(myObj)�Ļ������Լ�дһ��__len__()������

>>> class MyDog(object):
...     def __len__(self):
...         return 100
...
>>> dog = MyDog()
>>> len(dog)
100


���������Ժͷ����г����ǲ����ģ����getattr()��setattr()�Լ�hasattr()�����ǿ���ֱ�Ӳ���һ�������״̬��
>>> class MyObject(object):
...     def __init__(self):
...         self.x = 9
...     def power(self):
...         return self.x * self.x
...
>>> obj = MyObject()
�����ţ����Բ��Ըö�������ԣ�

>>> hasattr(obj, 'x') # ������'x'��
True
>>> obj.x
9
>>> hasattr(obj, 'y') # ������'y'��
False
>>> setattr(obj, 'y', 19) # ����һ������'y'
>>> hasattr(obj, 'y') # ������'y'��
True
>>> getattr(obj, 'y') # ��ȡ����'y'
19
>>> obj.y # ��ȡ����'y'
19
�����ͼ��ȡ�����ڵ����ԣ����׳�AttributeError�Ĵ���

>>> getattr(obj, 'z') # ��ȡ����'z'
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
AttributeError: 'MyObject' object has no attribute 'z'

���Դ���һ��default������������Բ����ڣ��ͷ���Ĭ��ֵ��

>>> getattr(obj, 'z', 404) # ��ȡ����'z'����������ڣ�����Ĭ��ֵ404
404
Ҳ���Ի�ö���ķ�����

>>> hasattr(obj, 'power') # ������'power'��
True
>>> getattr(obj, 'power') # ��ȡ����'power'
<bound method MyObject.power of <__main__.MyObject object at 0x10077a6a0>>
>>> fn = getattr(obj, 'power') # ��ȡ����'power'����ֵ������fn
>>> fn # fnָ��obj.power
<bound method MyObject.power of <__main__.MyObject object at 0x10077a6a0>>
>>> fn() # ����fn()�����obj.power()��һ����
81


С��

ͨ�����õ�һϵ�к��������ǿ��Զ�����һ��Python��������������õ����ڲ������ݡ�Ҫע����ǣ�ֻ���ڲ�֪��������Ϣ��ʱ�����ǲŻ�ȥ��ȡ������Ϣ���������ֱ��д��

sum = obj.x + obj.y
�Ͳ�Ҫд��

sum = getattr(obj, 'x') + getattr(obj, 'y')
һ����ȷ���÷����������£�

def readImage(fp):
    if hasattr(fp, 'read'):
        return readData(fp)
    return None
��������ϣ�����ļ���fp�ж�ȡͼ����������Ҫ�жϸ�fp�����Ƿ����read������������ڣ���ö�����һ��������������ڣ����޷���ȡ��hasattr()���������ó���

��ע�⣬��Python���ද̬�����У�����Ѽ�����ͣ���read()�������������fp�������һ���ļ�������Ҳ��������������Ҳ�������ڴ��е�һ���ֽ�������ֻҪread()�������ص�����Ч��ͼ�����ݣ��Ͳ�Ӱ���ȡͼ��Ĺ��ܡ�
#-----------------------------------------------------------------------------------------
#����Python��������ʵ�����Ե�����
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
�����
10 10 10
12 10 10
12 13 13

Python�����Բ��һ���

Python�����ԵĻ�ȡ����һ�����ϲ��һ��ƣ������������������˵����
Python��һ�нԶ���AAA���������obj1����ʵ�����󣬴Ӷ���ĽǶ�������AAA��obj1�������޹صĶ��󣬵��ǣ�Pythonͨ������Ĳ����������������AAA��ʵ������obj1��obj2֮��Ĺ�ϵ��
��ͼ��ʾ
        AAA
         |
       -----
      |     |  
    obj1   obj2
    
��������1�е���obj1.aaaʱ��Python���մ�obj1��AAA��˳�����µ��ϲ�������aaa��
ֵ��ע�����ʱ��obj1��û������aaa�ģ����ǣ�Python����AAA��ȥ���ң��ɹ��ҵ�������ʾ���������ԣ���������������AAA������aaaȷʵ�ǹ����������ʵ���ģ���Ȼ����ֻ�ǴӲ���������ʽģ�������ϵ��

Python�е���������

ԭ���ӵ�����Ҳָ������Ĺؼ���������2��obj1.aaa += 2��
Ϊʲô�أ�
��������ָ��obj.aaa += 2���������Ի�ȡ����������������������obj1.aaa += 2�ȼ���obj1.aaa = obj1.aaa + 2��
���е�ʽ�Ҳ��obj.aaa�������Ի�ȡ��������ǰ��������ᵽ�Ĳ��ҹ�����У�������ʱ�򣬻�ȡ������AAA������aaa�����Ե�ʽ����ֵΪ12��
�ڶ����������������ã���obj.aaa = 12���������������õ�ʱ��obj1���ʵ������û������aaa����˻�Ϊ����̬���һ������aaa��
���ڴӶ���ĽǶȣ�������ʵ�������������������Ķ������ԣ����aaa����ֻ����obj1��Ҳ����˵����ʱ�������AAA��ʵ������aaa������һ������aaa��
��ô��������3�У��ٴε���obj1.aaaʱ���������Ե��ò��ҹ������ʱ���ȡ������ʵ������obj1������aaa�������������AAA������aaa��
#-----------------------------------------------------------------------------------------
#ʹ��__slots__
#https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/00143186739713011a09b63dcbd42cc87f907a778b3ac73000ʹ��__slots__

��������£������Ƕ�����һ��class��������һ��class��ʵ�������ǿ��Ը���ʵ�����κ����Ժͷ���������Ƕ�̬���Ե�����ԡ��ȶ���class��

class Student(object):
    pass
Ȼ�󣬳��Ը�ʵ����һ�����ԣ�

>>> s = Student()
>>> s.name = 'Michael' # ��̬��ʵ����һ������
>>> print(s.name)
Michael
�����Գ��Ը�ʵ����һ��������

>>> def set_age(self, age): # ����һ��������Ϊʵ������
...     self.age = age
...
>>> from types import MethodType
>>> s.set_age = MethodType(set_age, s) # ��ʵ����һ������
>>> s.set_age(25) # ����ʵ������
>>> s.age # ���Խ��
25
���ǣ���һ��ʵ���󶨵ķ���������һ��ʵ���ǲ������õģ�

>>> s2 = Student() # �����µ�ʵ��
>>> s2.set_age(25) # ���Ե��÷���
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
AttributeError: 'Student' object has no attribute 'set_age'
Ϊ�˸�����ʵ�����󶨷��������Ը�class�󶨷�����

>>> def set_score(self, score):
...     self.score = score
...
>>> Student.set_score = set_score
��class�󶨷���������ʵ�����ɵ��ã�

>>> s.set_score(100)
>>> s.score
100
>>> s2.set_score(99)
>>> s2.score
99
ͨ������£������set_score��������ֱ�Ӷ�����class�У�����̬�����������ڳ������еĹ����ж�̬��class���Ϲ��ܣ����ھ�̬�����к���ʵ�֡�

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
    s1.set_name("Yan") #ԭ����__name��ΪΪ_Student__name����ǰ�Ǹ�s1�������°���һ������__name����δ�ı�ԭ����_Student__name
    print(s1.__dict__)
    print(s1.age)
    s1.print_age()
    s1.print_score()

    s1.set_score("89")
    s1.print_score()
�����
{'_Student__name': 'Ben', 'score': 99, 'set_age': <bound method set_age of <__main__.Student object at 0x0000025FE8E07898>>, 'set_name': <bound method set_name of <__main__.Student object at 0x0000025FE8E07898>>, 'print_age': <bound method print_age of <__main__.Student object at 0x0000025FE8E07898>>, 'set_score': <bound method set_score of <__main__.Student object at 0x0000025FE8E07898>>, 'age': 25, '__name': 'Yan'}
25
age[25]
Ben: 99
Ben: 89

ʹ��__slots__
���ǣ����������Ҫ����ʵ����������ô�죿���磬ֻ�����Studentʵ�����name��age���ԡ�
Ϊ�˴ﵽ���Ƶ�Ŀ�ģ�Python�����ڶ���class��ʱ�򣬶���һ�������__slots__�����������Ƹ�classʵ������ӵ����ԣ�
class Student(object):
    __slots__ = ('name', 'age') # ��tuple��������󶨵���������
Ȼ���������ԣ�

>>> s = Student() # �����µ�ʵ��
>>> s.name = 'Michael' # ������'name'
>>> s.age = 25 # ������'age'
>>> s.score = 99 # ������'score'
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
AttributeError: 'Student' object has no attribute 'score'
����'score'û�б��ŵ�__slots__�У����Բ��ܰ�score���ԣ���ͼ��score���õ�AttributeError�Ĵ���

ʹ��__slots__Ҫע�⣬__slots__��������Խ��Ե�ǰ��ʵ�������ã��Լ̳е������ǲ������õģ�

>>> class GraduateStudent(Student):
...     pass
...
>>> g = GraduateStudent()
>>> g.score = 9999
������������Ҳ����__slots__������������ʵ������������Ծ��������__slots__���ϸ����__slots__��
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

