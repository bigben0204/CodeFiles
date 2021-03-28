P368 
P371 
declare -i number=$RANDOM*10/32767; echo $number

#shell的PID，$符
echo $$
1857
#上一个指令的回传码，?符
echo $?
0
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ 12name=abc
12name=abc: command not found
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo $?
127

P377
read 读取一输入到一个变量
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ read atest
This is a test
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo $atest
This is a test
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ read -p "Please keyin your name: " -t 30 named
Please keyin your name: bigben
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo $named 
bigben

P378 declare
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ sum=100+300+50
#文字型的变量属性
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo $sum
100+300+50
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ declare -i sum=100+300+50
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo $sum
450
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ declare -x sum #和export sum一样
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ bash
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo $sum
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ declare -r sum #只读
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ sum=ddd
bash: sum: readonly variable

bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ var[1]="small ming"
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ var[2]="big ming"
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ var[3]="nice ming"
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo ${var[1]},${var[2]},${var[3]}
small ming,big ming,nice ming
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo "${var[1]},${var[2]},${var[3]}"
small ming,big ming,nice ming

P380
echo 
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ vbird="/home/vbird/testing/testing.x.sh"
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo $vbird #第一种显示变量的方法
/home/vbird/testing/testing.x.sh
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo ${vbird} #第二种显示变量的方法，可以在{}中做一些额外的操作
/home/vbird/testing/testing.x.sh
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo ${vbird##/*/} # ##从头开始删除匹配的变量，贪婪匹配
testing.x.sh
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo ${vbird#/*/} # #从头开始删除匹配的变量，非贪婪匹配
vbird/testing/testing.x.sh
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo ${vbird%%/*} # %%从尾开始删除匹配的变量，贪婪匹配
#全删光了
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo ${vbird%/*} # %从尾开始删除匹配的变量，非贪婪匹配
/home/vbird/testing
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo ${vbird/testing/TEST} # /找到的第一个字符串替换
/home/vbird/TEST/testing.x.sh
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ echo ${vbird//testing/TEST} # //找到的所有字符串替换
/home/vbird/TEST/TEST.x.sh

P380 根据变量是否设定进行三元判断和设定
var=${str-expr}
var=${str:-expr}

P382
alias unalias 命名别名设定
alias lm='ls -l | more'

P384
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ !! #执行上一个指令，类似于“方向键上+回车”
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ !203 #执行第203条指令
echo $HISTSIZE
1000
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ !echo #执行最近的以echo开头的指令
echo $HISTSIZE
1000

#直接用more来查看文件内容
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ more -2 sh01.sh 

P390
source file #读取环境变量到目前的shell环境中
source ~/.bashrc
. ~/.bashrc

P391 
stty 设置终端机

P394
ls test*
ls test?
ls test???
cp test[1-5] /tmp #test1/test2/.../test5，若存在，则拷贝到/tmp
cp test[!1-5] /tmp #不是test1-5的拷贝
quot #`command`
内的指令会先被执行，如
cd /lib/modules/`uname -r`/kernel
``也可以用$()来取代，如：
cd /lib/modules/$(uname -r)/kernel

P398
命令执行的判断依据：; && ||
;执行多个命令
&&前面的命令执行成功（回传0），则去执行后面的命令
||前面的命令执行不成功（回传不为0），则去执行后面的命令


P443
给sh01.sh加上x执行，并且用./sh01.sh来执行，就不会显示-e参数了，如果用sh sh01.sh则会显示-e Hello World !
#!/bin/bash
# Program:
#	This program is used to show "Hello World !" in screen.
# History:
# 2016/01/23 Ben Frist release

export PATH
echo -e "Hello World ! \a \n"
exit 0

bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh02.sh 
#!/bin/bash
# Program:
#	Let user keyin their first and last name, and show their full name.
# History:
# 2016/01/23	Ben	Frist release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH
read -p "Please input your first name: " firstname
read -p "Please input your last name: " lastname
echo -e "\nYour full name is: $firstname $lastname"
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ./sh02.sh 
Please input your first name: ding
Please input your last name: ben

Your full name is: ding ben

bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh03.sh 
#!/bin/bash
# Program:
#	User can keyin filename to touch 3 new files.
# History:
# 2016/01/23	Ben	Frist release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

# 1.get the filename from user
echo -e "I will use 'touch' command to create 3 files"
read -p "Please input the filename you want to create with: " fileuser

# 2. use ${var:-str} to create the filename in case of empty filename
filename=${fileuser:-"filename"} #如果fileuser没有设置或者为空，则将"filename"赋值给filename；如果fileuser不为空，则将$fileuser赋值给filename

# 3. use command date to get filename
date1=`date --date='2 days ago' +%Y%m%d`
date2=`date --date='1 days ago' +%Y%m%d`
date3=`date +%Y%m%d`
file1="$filename""$date1"
file2="$filename""$date2"
file3="$filename""$date3"

# 4. create files
touch $file1
touch $file2
touch $file3
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ./sh03.sh 
I will use 'touch' command to create 3 files
Please input the filename you want to create with: ben
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ll
total 20
drwxrwxr-x 2 bigben0204 bigben0204 4096 Jan 31 19:13 ./
drwxrwxr-x 3 bigben0204 bigben0204 4096 Jan 31 11:54 ../
-rw-rw-r-- 1 bigben0204 bigben0204    0 Jan 31 19:13 ben20160129
-rw-rw-r-- 1 bigben0204 bigben0204    0 Jan 31 19:13 ben20160130
-rw-rw-r-- 1 bigben0204 bigben0204    0 Jan 31 19:13 ben20160131
-rwxrw-r-- 1 bigben0204 bigben0204  244 Jan 31 12:17 sh01.sh*
-rwxrw-r-- 1 bigben0204 bigben0204  371 Jan 31 19:01 sh02.sh*
-rwxrw-r-- 1 bigben0204 bigben0204  737 Jan 31 19:13 sh03.sh*

#date使用
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ date #显示当前时间
Sun Jan 31 19:15:46 CST 2016
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ date --date='2 days ago' #显示2天前的当前时间
Fri Jan 29 19:16:05 CST 2016
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ date --date='2 days ago' +%Y%m%d #以年月日的方式显示两天前的当前时间
20160129

P445 数值运算
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh04.sh 
#!/bin/bash
#Program:
#	User can input 2 integer to cross by!
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH
echo -e "You SHOULD input 2 numbers, I will cross them! \n"
read -p "first number: " firstnu
read -p "second number: " secnu
echo -e "first number is --> $firstnu"
echo -e "second number is --> $secnu"
total=$(($firstnu*$secnu))
#declare -i total=$firstnu*$secnu
echo -e "\nThe number $firstnu x $secnu is ==> $total"

var=$((运算内容))
+，-，*，/，%。 %取余
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ nu=$((13%3)); echo $nu
1

P446 善用判断式 test
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ test -e sh04.sh && echo "exist" || echo "not exist"
exist
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ test -e sh05.sh && echo "exist" || echo "not exist"
not exist
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ test -f sh04.sh && echo "file" || echo "not a file"
file
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ test -d sh04.sh && echo "directory" || echo "not a directory"
not a directory

bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ test "abc" && echo "not empty" || echo "empty"
not empty
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ test "" && echo "not empty" || echo "empty"
empty

bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh05.sh 
#!/bin/bash
#Program:
#	Let user input a filename, the program will search the filename
#	1.) exist 2.)file/directory? 3.)file permissions
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

echo -e "The program will show you that filename exists which is input by you. \n\n"
read -p "Input a filename: " filename
test -z $filename && echo "You MUST input a filename." && exit 0

test ! -e $filename && echo "The filename $filename DOES NOT exist" && exit 0

test -f $filename && filetype="regulare file"
test -d $filename && filetype="directory"
test -r $filename && perm="readable"
test -w $filename && perm="$perm writable"
test -x $filename && perm="$perm executable"

echo "The filename: $filename is a $filetype"
echo "And the permission are: $perm"
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ./sh05.sh 
The program will show you that filename exists which is input by you. 


Input a filename: sh04.sh
The filename: sh04.sh is a regulare file
And the permission are: readable writable executable

P449 [ ]
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh06.sh 
#!/bin/bash
#Program:
#	This program will show the user's choice
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

read -p "Please input (Y/N): " yn
[ "$yn" == "Y" -o "$yn" == "y" ] && echo "OK, continue" && exit 0
[ "$yn" == "N" -o "$yn" == "n" ] && echo "Oh, interrupt!" && exit 0
echo "I do not know what is your choice" && exit 0
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ./sh06.sh 
Please input (Y/N): a
I do not know what is your choice
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ./sh06.sh 
Please input (Y/N): n
Oh, interrupt!
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ./sh06.sh 
Please input (Y/N): Y
OK, continue

P450 预设参数
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh07.sh 
#!/bin/bash
#Program:
#	The program will show it's name and first 3 parameters.
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

echo "The script name is ==> $0"
[ -n "$1" ] && echo "The 1st parameter is ==> $1" || exit 0
[ -n "$2" ] && echo "The 2nd parameter is ==> $2" || exit 0
[ -n "$3" ] && echo "The 3th parameter is ==> $3" || exit 0
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ./sh07.sh 
The script name is ==> ./sh07.sh
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ./sh07.sh a b c
The script name is ==> ./sh07.sh
The 1st parameter is ==> a
The 2nd parameter is ==> b
The 3th parameter is ==> c

P451 if then
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh06-2.sh 
#!/bin/bash
#Program:
#	This program will show the user's choice
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

read -p "Please input (Y/N): " yn
if [ "$yn" == "Y" ] || [ "$yn" == "y" ]; then
	echo "OK, continue"
	exit 0
fi
if [ "$yn" == "N" ] || [ "$yn" == "n" ]; then
	echo "Oh, interrupt!" 
	exit 0
fi
echo "I don't know what is your choice" && exit 0

if [ ]; then
else
fi

if [ ]; then
elif
else
fi

bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh06-3.sh 
#!/bin/bash
#Program:
#	This program will show the user's choice
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

read -p "Please input (Y/N): " yn
if [ "$yn" == "Y" ] || [ "$yn" == "y" ]; then
	echo "OK, continue"
elif [ "$yn" == "N" ] || [ "$yn" == "n" ]; then
	echo "Oh, interrupt!" 
else
	echo "I don't know what is your choice" && exit 0
fi

bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh08.sh 
#!/bin/bash
#Program:
#	Show "Hello" from $1...
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

if [ "$1" == "hello" ]; then
	echo "Hello, how are you?"
elif [ "$1" == "" ]; then #elif [ -z "$1" ]; then
	echo "You MUST input parameters, ex> $0 someword"
else
	echo "The only parameter is 'hello'"
fi
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ./sh08.sh 
You MUST input parameters, ex> ./sh08.sh someword
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ./sh08.sh aaa
The only parameter is 'hello'
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ./sh08.sh hello
Hello, how are you?

P453 netstat指令，可以查询到目前主机有开启的网络服务端口（service ports），相关功能会在服务器架设篇，可以利用
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh09.sh 
#!/bin/bash
#Program:
#	Using netstat and grep to detect WWW,SSH,FTP and Mail services
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

echo "Now, the services of your Linux system will detect!"
echo -e "The www, ftp, ssh, and mail will be detect! \n"

testing=`netstat -tuln | grep ":80"`
if [ "$testing" != "" ]; then
	echo "WWW is running in your system."
fi

testing=`netstat -tuln | grep ":22"`
if [ "$testing" != "" ]; then
	echo "SSH is running in your system."
fi

testing=`netstat -tuln | grep ":21"`
if [ "$testing" != "" ]; then
	echo "FTP is running in your system."
fi

testing=`netstat -tuln | grep ":25"`
if [ "$testing" != "" ]; then
	echo "Mail is running in your system."
fi

P455 计算当前时间与输入时间相差多久
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh10.sh 
#!/bin/bash
#Program:
#	Tring to calculate your demobilization date at how many days later...
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

echo "This program will try to calculate: "
echo "How many days about the input date..."
read -p "Please input a date (YYYYMMDD ex>20050401): " date2

date_d=`echo $date2 | grep '[0-9]\{8\}'`
if [ "$date_d" == "" ]; then
	echo "You input the wrong format of date..."
	exit 1
fi

declare -i date_input=`date --date="$date2" +%s`
declare -i date_now=`date +%s`
declare -i date_total_s=$(($date_input-$date_now))
declare -i date_d=$(($date_total_s/60/60/24))
if [ "$date_total_s" -lt "0" ]; then
	echo "You had input a date before: " $((-1*$date_d)) " days ago."
else
	declare -i date_secondsInADay=$(($date_total_s-$date_d*60*60*24))
	declare -i date_h=$(($date_secondsInADay/60/60))
	
	declare -i date_secondsInAHour=$(($date_total_s-$date_d*60*60*24-$date_h*60*60))
	declare -i date_m=$(($date_secondsInAHour/60))
	echo "You input a date after $date_d days and $date_h hours and $date_m minutes."
fi
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ./sh10.sh 
This program will try to calculate: 
How many days about the input date...
Please input a date (YYYYMMDD ex>20050401): 20160309
You input a date after 1 days and 1 hours and 18 minutes.

P457 case...esac
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh08-2.sh 
#!/bin/bash
#Program:
#	Show "Hello" from $1... by using case ...esac
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

case $1 in
	"hello" | "HELLO")
		echo "Hello, how are you?"
		;;
	"")
		echo "You MUST input parameters, ex> $0 someword"
		;;
	*)
		echo "Usage $0 {hello}"
		;;
esac

#用case与用户输入交互
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh11.sh 
#!/bin/bash
#Program:
#	Let user input one, two, three and show in screen.
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

echo "This program will print your selection !"
read -p "Input your choice: " choice
case $choice in
	"one" | "two" | "three")
		echo "Your choice is $choice"
		;;
	*)
		echo "You can only input one or two or three."
		;;
esac

P459 function
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh11-2.sh 
#!/bin/bash
#Program:
#	Let user input one, two, three and show in screen.
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

function printit()
{
	echo -n "Your choice is " #echo -n不输出新行
}

echo "This program will print your selection !"
case $1 in
	"one" | "two" | "three")
		printit; echo $1 | tr 'a-z' 'A-Z' #tr 字符转换，这里是把a-z转化为大写；tr -d 'abc'，删除所有的a/b/c
		;;
	*)
		echo "Usage {one|tow|three}"
		;;
esac

bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh11-3.sh 
#!/bin/bash
#Program:
#	Let user input one, two, three and show in screen.
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

function printit()
{
	echo "Your choice is $1" #函数名为$0，入参数分别为$1/$2/...
}

echo "This program will print your selection !"
case $1 in
	"one" | "two" | "three")
		printit $1
		;;
	*)
		echo "Usage {one|tow|three}"
		;;
esac

P461 while do done
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh12.sh 
#!/bin/bash
#Program:
#	Use loop to try find your input.
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

while [ "$yn" != "yes" ] && [ "$yn" != "YES" ]
do
	read -p "Please input yes/YES to stop this program: " yn
done

P461 until do done
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh12-2.sh 
#!/bin/bash
#Program:
#	Use loop to try find your input.
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

until [ "$yn" == "yes" ] || [ "$yn" == "YES" ]
do
	read -p "Please input yes/YES to stop this program: " yn
done

P461 
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh13.sh 
#!/bin/bash
#Program:
#	Try to use loop to calculate the result "1+2+3...+100".
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

s=0
i=0
while [ "$i" != "100" ]
do
	i=$(($i+1))
	s=$(($s+$i))
done
echo "The result of '1+2+3+...+100' is ==> $s"

P462 for do done
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh14.sh 
#!/bin/bash
#Program:
#	Try to calculate the result "1+2+3...+100".
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

s=0
for ((i=1; i<=100; i=i+1))
do
	s=$(($s+$i))
done
echo "The result of '1+2+3+...+100' is ==> $s"

bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh15.sh 
#!/bin/bash
#Program:
#	Using for ... loop to print 3 animals
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

for animal in dog cat elephant
do
	echo "There are ""$animal""s ..." #可以拼接出dogs, cats的效果
done
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ ./sh15.sh 
There are dogs ...
There are cats ...
There are elephants ...

#for do done应用，打印一个指定目录下所有一级子文件（夹）权限
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat sh16.sh 
#!/bin/bash
#Program:
#	Let user input a directory and find the whole file's permission.
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

read -p "Please input a directory: " dir
if [ "$dir" = "" ] || [ ! -d "$dir" ]; then
	echo "The $dir does NOT exist in your system."	
	exit 1
fi

filelist=`ls $dir`
for filename in $filelist
do
	perm=""
	test -r "$dir/$filename" && perm="$perm readable"
	test -w "$dir/$filename" && perm="$perm writeable"
	test -x "$dir/$filename" && perm="$perm executable"
	echo "The file $dir/$filename's permission is $perm"
done

#--------------------------------------------------------------------------------------------------------------------------
P464 shell script的追踪与debug
sh [-nvx] scripts.sh
-n：不执行，仅查询语法问题
-v：在执行scripts前，先将scripts的内容输出到屏幕上
-x：将使用到的script内容显示到屏幕上，非常有用！
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ sh -n sh16.sh
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ sh -v sh15.sh 
#!/bin/bash
#Program:
#	Using for ... loop to print 3 animals
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

for animal in dog cat elephant
do
	echo "There are $animal" "s ..."
done
There are dog s ...
There are cat s ...
There are elephant s ...
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ sh -v sh16.sh 
#!/bin/bash
#Program:
#	Let user input a directory and find the whole file's permission.
#History:
#2016/03/06	Ben	First release
PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

read -p "Please input a directory: " dir
Please input a directory: /home
if [ "$dir" = "" ] || [ ! -d "$dir" ]; then
	echo "The $dir does NOT exist in your system."	
	exit 1
fi

filelist=`ls $dir`
for filename in $filelist
do
	perm=""
	test -r "$dir/$filename" && perm="$perm readable"
	test -w "$dir/$filename" && perm="$perm writeable"
	test -x "$dir/$filename" && perm="$perm executable"
	echo "The file $dir/$filename's permission is $perm"
done
The file /home/bigben0204s permission is  readable writeable executable
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ sh -x sh16.sh 
+ PAHT=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:/home/bigben0204/bin
+ export PATH
+ read -p Please input a directory:  dir
Please input a directory: /home
+ [ /home =  ]
+ [ ! -d /home ]
+ ls /home
+ filelist=bigben0204
+ perm=
+ test -r /home/bigben0204
+ perm= readable
+ test -w /home/bigben0204
+ perm= readable writeable
+ test -x /home/bigben0204
+ perm= readable writeable executable
+ echo The file /home/bigben0204's permission is  readable writeable executable
The file /home/bigben0204's permission is  readable writeable executable
#--------------------------------------------------------------------------------------------------------------------------
P466 计算离生日还有多少天
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat birthday.sh 
#!/bin/bash
# Program:
#	This program is used to calculate the days of your birthday.
# History:
# 2016/01/23 Ben Frist release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

read -p "Please input your birthday (MMDD, ex>0709): " bir
now=`date +%m%d`
if [ "$bir" == "$now" ]; then
	echo "Happy birthday to you!"
elif [ "$bir" -gt "$now" ]; then
	year=`date +%Y`
	total_d=$(($((`date --date="$year$bir" +%s` - `date +%s`))/60/60/24))
	echo "Your birthday will be $total_d later"
else
	year=$((`date +%Y`+1))
	total_d=$(($((`date --date="$year$bir" +%s` - `date +%s`))/60/60/24))
	echo "Your birthday will be $total_d later"
fi

#用函数实现计算时间差
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat birthday.sh 
#!/bin/bash
# Program:
#	This program is used to calculate the days of your birthday.
# History:
# 2016/01/23 Ben Frist release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

function getDayDifference()
{
	total_d=$(($((`date --date="$1$2" +%s` - `date +%s`))/60/60/24))
	return $total_d
}

read -p "Please input your birthday (MMDD, ex>0709): " bir
now=`date +%m%d`
if [ "$bir" == "$now" ]; then
	echo "Happy birthday to you!"
elif [ "$bir" -gt "$now" ]; then
	year=`date +%Y`
	total_d=$(getDayDifference $year $bir)
	echo "Your birthday will be $total_d later"
else
	year=$((`date +%Y`+1))
	total_d=$(getDayDifference $year $bir)
	echo "Your birthday will be $total_d later"
fi

#--------------------------------------------------------------------------------------------------------------------------
#P467 查看所有用户
bigben0204@ubuntu:~/Desktop/ShellLearn/scripts$ cat account.sh 
#!/bin/bash

accounts=`cat /etc/passwd | cut -d':' -f1` #用:分隔符拆分，取第一列
i=0
for account in $accounts
do
	declare -i i=$i+1
	echo "The $i account is $account."
done

#--------------------------------------------------------------------------------------------------------------------------
P470 账号密码管理
/etc/passwd
/etc/shadow

P491 su - -c可以使用root身份执行一次语句
bigben0204@ubuntu:~$ su - -c "head -n 3 /etc/shadow"
Password: 
root:$6$kHGZEAwh$iNHB/B0Spx/FuW6/nFLg9em9eKSPJ5NmQW7Zp0fIRfrxoMsCBpKELpYwlb0pSqJU6U4x5TQ9e5K2yUBiNyG4b/:16873:0:99999:7:::
daemon:*:16729:0:99999:7:::
bin:*:16729:0:99999:7:::

su切换用户需要知道密码

#--------------------------------------------------------------------------------------------------------------------------
#grep -v参数，不显示当前自己进程
ps -ef | grep redis | grep -v grep
#--------------------------------------------------------------------------------------------------------------------------
# Linux下统计当前文件夹下的文件个数、目录个数 https://blog.csdn.net/panda62/article/details/82703645
1) 统计当前文件夹下文件的个数
ls -l |grep "^-"|wc -l

2) 统计当前文件夹下目录的个数
ls -l |grep "^d"|wc -l

3) 统计当前文件夹下文件的个数，包括子文件夹里的
ls -lR|grep "^-"|wc -l

4) 统计文件夹下目录的个数，包括子文件夹里的
ls -lR|grep "^d"|wc -l

说明：
ls -l
长列表输出当前文件夹下文件信息(注意这里的文件，不同于一般的文件，可能是目录、链接、设备文件等)

grep "^-"
这里将长列表输出信息过滤一部分，只保留一般文件，如果只保留目录就是 ^d

wc -l
统计输出信息的行数，因为已经过滤得只剩一般文件了，所以统计结果就是一般文件信息的行数，又由于一行信息对应一个文件，所以也就是文件的个数。
#--------------------------------------------------------------------------------------------------------------------------
# 设置overlay https://blog.csdn.net/luckyapple1028/article/details/78075358
sudo mount -t overlay overlay -o lowerdir=/home/ben/Study/lower1:/home/ben/Study/lower2,upperdir=/home/ben/Study/upper,workdir=/home/ben/Study/work /home/ben/Study/merged

# 卸载挂载盘
sudo umount /home/ben/Study/merged
#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------------------------------------


