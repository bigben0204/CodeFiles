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


if __name__ == "__main__":
    print balance(")()(")
    print balance("(())")
    print balance("()()")

    print balance_recurse(")()(")
    print balance_recurse("(())")
    print balance_recurse("()()")

�����
False
True
True
False
True
True
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

