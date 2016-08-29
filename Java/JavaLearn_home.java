//------------------------------------------------------------------------------------------------
? extends T 
?表示某个泛型类型，该类型继承于T，是T的子类。所以声明一个List<? extends T> list，表示声明一个对象类型?是T子类的List，是无法加入基类T。list.add(T)不行。
? super T
?表示某个泛型类型，该类型下限为T，是T的基类。所以声明一个List<? super T> list，表示声明一个对象类型?是T基类的List，可以加入基类T。list.add(T)可以。
//------------------------------------------------------------------------------------------------
//Java值传递，即传入的是引用的副本，在函数里会把该引用指向到新的对象上，但不会影响到函数入口处对象
public class Test {
	public static void swap(int a, int b) {
		int temp = a;
		a = b;
		b = temp;
		System.out.println("swap方法里，a的值是" + a + ";b的值是" + b);
	}

	public static void main(String args[]) {
		int a = 6;
		int b = 9;
		System.out.println("交换开始前，变量a的值是" + a + ";变量b的值是" + b);
		swap(a, b);
		System.out.println("交换结束后，变量a的值是" + a + ";变量b的值是" + b);
	}
}
输出如下：
交换开始前，变量a的值是6;变量b的值是9
swap方法里，a的值是9;b的值是6
交换结束后，变量a的值是6;变量b的值是9

//需要把待交换的值放入对象中
class DataWrap
{
	public int a;
	public int b;
}

public class Test
{
	//这里的DataWrap对象dw，仍是函数入口对象的引用副本，只不过这个副本地址与原对象地址一致，所以修其成员函数变量即可修改原对象成员函数
	public static void swap(DataWrap dw)
	{
		int tmp = dw.a;
		dw.a = dw.b;
		dw.b = tmp;
		System.out.println("swap方法里，a属性的值是" + dw.a + ";b属性的值是" + dw.b);
	}

	public static void main(String args[])
	{
		DataWrap dw = new DataWrap();
		dw.a = 6;
		dw.b = 9;
		System.out.println("交换开始前，a属性的值是" + dw.a + ";b属性的值是" + dw.b);
		swap(dw);
		System.out.println("交换结束后，a属性的值是" + dw.a + ";b属性的值是" + dw.b);
	}
}
输出如下：
交换开始前，a属性的值是6;b属性的值是9
swap方法里，a属性的值是9;b属性的值是6
交换结束后，a属性的值是9;b属性的值是6

//这种对象方式也无法实现，Integer尽管是封装类对象，但是一旦生成好，就没有办法修改值了
public class Test
{
	public static void main(String args[])
	{
		Integer a = new Integer(3);
		Integer b = new Integer(4);
		Test.swap(a, b);
		System.out.println(a);
		System.out.println(b);
	}
	
	public static void swap(Integer lhs, Integer rhs) {
		int temp = lhs;
		lhs = rhs;
		rhs = temp; 
	}
}
//------------------------------------------------------------------------------------------------
//Java从入门到精通 第2章 P66

public class HelloJava {
	private static String say = "我要学会你.";
	public static void main(String[] args) {
		System.out.println("你好 Java " + say);
	}
}

//一个*.java中最多只能有一个类为public，可以没有public
public class Hello{
	public static void main(String[] args){
		System.out.println("Hello, world!");
	}
}

class HelloA{
	public static void main(String[] args){
		System.out.println("Hello, world!");
	}
}

class HelloB{
	public static void main(String[] args){
		System.out.println("Hello, world!");
	}
}

以0开头的整型表示八进制数
以0x开头的整型表示十六进制数
长整型long定义的数要以L或l结尾
浮点数float定义的数要以F或f结尾
双精度浮点数double定义的数可以以D或d结尾

//P78
public class HelloJava {
	public static void main(String[] args) {
		char char1 = '\\';
		char char2 = '\u2605';
		System.out.println(char1);
		System.out.println(char2);
	}
}
输出如下：
\
★


//P80
public class HelloJava {
	private static final double PI = 3.141592653;//这里的private可以不加，现在还不太懂有什么差别
	private static int age = 23;//这里的private可以不加，现在还不太懂有什么差别
	public static void main(String[] args) {
		final int number;
		number = 1234;//对于final常量只能赋一次值，可以在声明时赋值，也可声明时不赋值
		//number = 22222;//这里再次赋值将编译报错
		age = 22;
		System.out.println(PI);
		System.out.println(number);
		System.out.println(age);
	}
}


//P81
public class HelloJava {
	static int times = 3;
	public static void main(String[] args) {
		int times = 4;
		System.out.println(times);
		System.out.println(HelloJava.times);
	}
}


//P86
&&与&都是逻辑与，||与|都是逻辑或，不同点是前者有短路特性，后者没有

public class HelloJava {
	public static void main(String[] args) {
		System.out.println(5 & -4);
		System.out.println(3 | 6);
		System.out.println(~7);
		System.out.println(10 ^ 3);
		System.out.println(48 << 1);
		System.out.println(48 >> 1);//>>>无符号右移运算符，以0填充
	}
}
输出如下：
4
7
-8
9
96
24

//变量交换 P90
//这个算法说白了就是：两个变量A和B，想交换值，B = A ^ B ^ B，A = A ^ B ^ A
import java.util.Scanner;

public class VariableExchange {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Please input value of variable A:");
		long A = scan.nextLong();
		System.out.println("Please input value of variable B:	");
		long B = scan.nextLong();
		//scan.close();//不加这行会有提示scan没有close
		System.out.println("A = " + A + "\tB = " + B);
		System.out.println("Exchange the two variables...");
		A = A ^ B;//可以这么写：A ^= B;
		B = B ^ A;//B = A ^ B;
		A = A ^ B;
		System.out.println("After exchange: A = " + A + "\tB = " + B);
	}
}
输出如下：
Please input value of variable A:
10
Please input value of variable B:
20
A = 10	B = 20
Exchange the two variables...
After exchange: A = 20	B = 10


//数据溢出 P93
public class HelloJava {
	public static void main(String[] args) {
		short s = 516;
		byte b = (byte)s;
		System.out.println(b);
	}
}

boolen类型不能与任何类型作转换，所以如下写法编译不过，与C++不同
public class HelloJava {
	public static void main(String[] args) {
		boolean b = true;
		if (0)
		{
			System.out.println(b);
		}
	}
}

//闰年的判断 P96
import java.util.Scanner;

public class HelloJava {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("请输入一个年份：");
		long year = scan.nextLong();
		scan.close();
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
		{
			System.out.println(year + "年是闰年！");
		}
		else
		{
			System.out.println(year + "年不是闰年！");
		}
	}
}

//P105 switch语句中的case内容，如果不加break，则会不进行判断直接执行后续case中的语句内容，直到遇到break或结束
public class HelloJava {
	public static void main(String[] args) {
		int a = 2;
		switch (a)
		{
		case 1:
			System.out.println("1");
			System.out.println("11");
		case 2:
			System.out.println("2");
		case 3:
			System.out.println("3");
		default:
			System.out.println("don't know");
		}
	}
}

//foreach语句，int x不能在for之前定义
public class HelloJava {
	public static void main(String[] args) {
		int arr[] = {0, 1, 2};
		System.out.println("数组元素为：");
		for (int x : arr) {
			System.out.println(x);
		}
	}
}

//P112 
public class HelloJava {
	public static void main(String[] args) {
		String[] aves = new String[] {"白鹭", "丹顶鹤"};//可以写为如下几种形式String aves[] = new String[] {"白鹭", "丹顶鹤"}或String aves[] = {"白鹭", "丹顶鹤"}或String[] aves = {"白鹭", "丹顶鹤"}
		int index = 0;
		System.out.println("鸟：");
		while (index < aves.length) {
			System.out.print(aves[index++] + " ");
		}
		
		//while可以写成如下for循环
		for (String b : aves) {
			System.out.print(b + " ");
		}
	}
}

//双层for循环遍历二维数组 P116
public class HelloJava {
	public static void main(String[] args) {
		int[][] scores = new int[][] {{1, 2, 3}, {4, 5, 6}};//维数可以不完全匹配{{1, 3}, {4, 5, 6}}
		for (int[] is : scores) {
			for (int i : is) {
				System.out.println(i);
			}
		}
	}
}


//P130矩阵转置
public class HelloJava {
	public static void main(String[] args) {
		int arr[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
		System.out.println("转置前的矩阵是：");
		printArray(arr);//
		
		int arr2[][] = new int[arr.length][arr.length];
		for (int i = 0; i < arr.length; ++i) {
			for (int j = 0; j < arr[i].length; ++j) {
				arr2[j][i] = arr[i][j];
			}
		}
		System.out.println("转置后的矩阵是：");
		printArray(arr2);
	}
	
	//这里如果不加static，会报错说静态函数不能引用非静态函数
	private static void printArray(int[][] arr) {
		for (int i = 0; i < arr.length; ++i) {
			for (int j = 0; j < arr[i].length; ++j) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println();
		}
	}
}

private static void print1DimensionArray(int[] arr) {
	for (int i = 0; i < arr.length; ++i) {
		System.out.println(arr[i] + " ");
	}
}

private static void print2DimensionArray(int[][] arr) {
	for (int i = 0; i < arr.length; ++i) {
		for (int j = 0; j < arr[i].length; ++j) {
			System.out.print(arr[i][j] + " ");
		}
		System.out.println();
	}
}

Arrays.fill 填充数组 P133
Arrays.sort 排序数组 P134
Arrays.copyOf/Arrays.copyOfRange 复制数组 P135


//对比一维二维数组所占内存 P137
public class HelloJava {
	public static void main(String[] args) {
		int num1 = 1024 * 1024 * 2;
		int arr1[] = new int[num1];
		for (int i = 0; i < arr1.length; ++i) {
			arr1[i] = i;
		}
		//获得占用内存总数，并将单位转换为MB
		long memory1 = Runtime.getRuntime().totalMemory() / 1024 / 1024;
		System.out.println("一维数组占用内存问题为：" + memory1);
		
		int num2 = 1024 * 1024;
		int arr2[][] = new int[num2][2];
		for (int i = 0; i < arr2.length; ++i) {
			arr2[i][0] = i;
			arr2[i][1] = i;
		}
		//获得占用内存总数，并将单位转换为MB
		long memory2 = Runtime.getRuntime().totalMemory() / 1024 / 1024;
		System.out.println("二维数组占用内存问题为：" + memory2);
	}
}

P144 在字符串和其他数据类型连接时，同样使用“+”连接符，连接之后的返回值是字符串。

P146 获取字符串信息
public class HelloJava {
	public static void main(String[] args) {
		String s = "";
		System.out.println(s.length());
		System.out.println(s.indexOf("a"));
		System.out.println(s.lastIndexOf("a"));
		s = "abc";
		System.out.println(s.charAt(2));
	}
}

s.trim();不对当前字符串修改，需要将返回值赋值 s = s.trim();

//去除字符串中的空格 P148
import java.util.StringTokenizer;

public class HelloJava {
	public static void main(String[] args) {
		String text = "   We are students     ";
		System.out.println("原字符串是：\n" + text);
		StringTokenizer st = new StringTokenizer(text, " ");//可以把空格换成任意字符串，如换成"tsn"，则去除所有的't','s','n'字符
		StringBuffer sb = new StringBuffer();
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
		}
		System.out.println("去掉字符串中所有空格之后的字符串是：\n" + sb.toString());
		String s = text.replaceAll("ts", "");
		System.out.println(s);
	}
}
输出如下：
原字符串是：
   We are students     
去掉字符串中所有空格之后的字符串是：
   We are ude     
   We are studen     
   
s.replaceAll(" ", "");

//P151 比较运算符“==”比较的是内存位置，不适宜拿来比较字符串，但整型等可以用比较运算符比较；equals()方法比较的两个字符串内容必须完全一样；equalsIgnoreCase()方法在忽略大小写的情况下内容必须一样。

public class HelloJava {
	public static void main(String[] args) {
		String text = "   We are students     ";
		System.out.println(text.toUpperCase());
	}
}

//split分割字符串
public class HelloJava {
	public static void main(String[] args) {
		String s = new String("abc,def,ghi,gkl");
		String news[] = s.split(",");//String news[] = s.split(",|g") 用|表示,和g都是分隔符，即遇到,也分隔，遇到g也分隔
		for (String str : news) {
			System.out.println(str);
		}
	}
}

//判断字符串是否是数字 P155
public class CheckNumber {
	public static void main(String[] args) {
		String s = "123456a";
		if (CheckNumber.isNumber(s)) {//这里可以不用指定类名，直接使用isNumber
			System.out.println(s + " is a number.");
		}
		else {
			System.out.println(s + " is not a number.");
		}
	}
	
	private static boolean isNumber(String str) {
		char c[] = str.toCharArray();
		for (int i = 0; i < c.length; ++i) {
			if (!Character.isDigit(c[i])) {
				return false;
			}
		}
		return true;
	}
}

//日期格式化 P158
import java.util.Date;
import java.util.Locale;

public class HelloJava {
	public static void main(String[] args) {
		Date today = new Date();
		String a = String.format(Locale.US, "%tb", today);//可以将US改成CHINESE/CHINA，或者省略，得到中文的显示
		String b = String.format(Locale.US, "%tB", today);
		String c = String.format("%ta", today);
		String d = String.format("%tA", today);
		String e = String.format("%tY", today);
		String f = String.format("%ty", today);
		String g = String.format("%tm", today);
		String h = String.format("%td", today);
		String i = String.format("%te", today);
		
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
		System.out.println(d);
		System.out.println(e);
		System.out.println(f);
		System.out.println(g);
		System.out.println(h);
		System.out.println(i);
	}
}
输出如下：
Apr
April
星期四
星期四
2015
15
04
02
2
//时间格式化 P158 日期时间组合格式化 P159 常规类型格式化 P160
//金额格式化成大写 P161
//数字格式化成货币格式 P163

//正则表达式 P165
public class HelloJava {
	public static void main(String[] args) {
		String s1 = "Mrkj007";
		String s2 = "mrdk007";
		String regexStr = "\\p{Upper}\\p{Lower}\\p{Lower}\\p{Lower}\\d\\d\\d";//String regexStr = "\\p{Upper}\\p{Lower}{3}\\d{3}";
		
		boolean bn1 = s1.matches(regexStr);
		boolean bn2 = s2.matches(regexStr);
		
		System.out.println(bn1);
		System.out.println(bn2);
	}
}

//P166
//通配符.前不用\\，匹配单个特定字符可以直接将这个字符写上，如@
public class HelloJava {
	public static void main(String[] args) {
		String s1 = "a@aa";
		
		String regexStr = "\\w{0,}@.{1}\\w{1,}";//"\\w{0,}@.{1}\\w{1,}3$"必须要求以3为结尾
		
		boolean bn1 = s1.matches(regexStr);
		
		System.out.println(bn1);
	}
}


public class HelloJava {
	public static void main(String[] args) {
		String telephone1 = "13024588795";
		if (check(telephone1)) {
			System.out.println("Legal");
		}
		else {
			System.out.println("Unlegal");
		}
	}
	
	public static boolean check(String handset) {
		String regex = "1[3,5,8]\\d{9}$";//[3,5,8]里的逗号可以省略为[358]，最后的$是字符串是以什么为结尾的匹配符
		if (handset.matches(regex)) {
			return true;
		}
		else {
			return false;
		}
	}
}

//P168 正则表达式验证IP地址合法性
//P168 统计汉字个数
import java.util.regex.Pattern;
public class HelloJava {
	public static void main(String[] args) {
		String text = "明日科技 soft";
		int amount = 0;
		for (int i = 0; i < text.length(); ++i) {
			boolean matches = Pattern.matches("^[\u4E00-\u9FA5]{0,}$", "" + text.charAt(i));//第二个参数是String，所以要将字符串与字符相加得到字符串入参，第一个参数感觉这样也可以"[\u4E00-\u9FA5]"
			if (matches) {
				++amount;
			}
		}
		System.out.println(text + " 汉字个数 = " + amount);
	}
}

//StringBuilder类 P170 感觉有点像C++里的stringstream类
public class HelloJava {
	public static void main(String[] args) {
		String s1 = "int";
		String s2 = "ser";
		StringBuilder builder = new StringBuilder(s1);
		builder.insert(2, s2);//builder.insert(builder.length(), s2)等于builder.append(s2)
		System.out.println(builder);
	}
}

//Java里可以正常打印出字符数组里的字符，而不必像C++一样加结束符'\0'
public class HelloJava {
	public static void main(String[] args) {
		char arr[] = new char[] {'a', 'b'};
		System.out.println(arr);
	}
}

//null与空字符串的差别
String str1=null;
String str2="";

System.out.print(str1.length());//空指针异常
System.out.print(str2.length());//无异常

意思就是
null 那个对象没有内存空间
"" 有内存空间 值为空


//字符串加密 P174
public class HelloJava {
	public static void main(String[] args) {
		String value = "我爱 Java";
		char secret = '祈';
		System.out.println("原字符串为：" + value);
		String encryt = EAndU(value, secret);
		System.out.println("加密后的值：" + encryt);
		String uncrypt = EAndU(encryt, secret);
		System.out.println("解密后的值：" + uncrypt);
	}
	
	private static String EAndU(String value, char secret) {
		byte bt[] = value.getBytes();
		for (int i = 0; i < bt.length; ++i) {
			bt[i] = (byte)(bt[i] ^ (int)secret);
		}
		return new String(bt, 0, bt.length);
	}
}

//P184 只可以在无参构造方法中的第一句使用this关键字调用有参构造方法
public class AnyThing {
	public AnyThing() {
		//String a;
		this("this 调用有参构造方法");
		System.out.println("无参构造方法");
	}
	
	public AnyThing(String name) {
		System.out.println("有参构造方法" + name);
	}
}

//P190
静态方法中不可以使用this关键字。
静态方法中不可以直接调用非静态方法。
Java中不能将方法体内的局部变量声明为static。
如果在执行类时，希望先执行类的初始化动作，可以使用static定义一个静态区域。
当这段代码被执行时，首先执行static块中的程序，并且只会执行一次。
public class AnyThing {
	static {
		System.out.println("static area");
	}
	public AnyThing() {
		this("this 调用有参构造方法");
		System.out.println("无参构造方法");
	}
	
	public AnyThing(String name) {
		System.out.println("有参构造方法" + name);
	}
}

public class HelloJava {
	public static void main(String[] args) {
		AnyThing a1 = new AnyThing();
		AnyThing a2 = new AnyThing();
	}
}
输出如下：
static area
有参构造方法this 调用有参构造方法
无参构造方法
有参构造方法this 调用有参构造方法
无参构造方法

//P190 权限修饰符
当声明类时不使用public/protected/private修饰符设置类的权限，则这个类预设为包存取范围，即只有一个包中的类可以调用这个类的成员变量或成员方法。
类的权限设定会约束类成员上的权限设定。

//可以对有main函数的类进行声明创建 P193
public class HelloJava {
	public static void main(String[] args) {
		HelloJava hj = new HelloJava();
		System.out.println(hj.plus(1, 2));
	}
	
	public double plus(double d1, double d2) {
		return d1 + d2;
	}
}

//当把plus定义为static后，会有提示静态方法应该通过静态方式访问，所以hj.plus应该修改为HelloJava.plus
public class HelloJava {
	public static void main(String[] args) {
		HelloJava hj = new HelloJava();//HelloJava hj = null;设置hj为null,下一句的输出仍是正常可用的，因为静态函数不依赖于对象，若去掉static，则编译报错
		System.out.println(hj.plus(1, 2));//System.out.println(HelloJava.plus(1, 2));
	}
	
	public static double plus(double d1, double d2) {
		return d1 + d2;
	}
}

P199 垃圾回收器只能回收那些由new操作符创建的对象，如果对象不是通过new操作符在内存中获取一块内存区域，这种对象可能不被垃圾回收机制所识别，所以在Java语言中提供了一个finalize()方法，是Object类的方法，它被声明为protected，用户可以在自己的类中定义这个方法，如果用户在类中定义了finalize()方法，在垃圾回收时首先调用该方法，并且在下一次垃圾回收动作发生时，才能真正回收对象占用的内存。
由于垃圾回收不受人为控制，具体执行时间也不确定，所以finalize()方法也就无法执行，为此，Java提供了System.gc()方法强制启动垃圾回收器，告知垃圾回收器来清理。

//P200 统计图书数量
import java.util.Random;

public class HelloJava {
	public static void main(String[] args) {
		String titles[] = {"Java1", "Java2", "Java3"};
		for (int i = 0; i < 5; ++i) {
			new Book(titles[new Random().nextInt(3)]);//这里的new Random().nextInt(3)会被识别为(new Random()).nextInt(3)，即new出来一个Random对象，并调用其成员函数nextInt。
		}
		System.out.println("总计卖书：" + Book.getCounter());
	}
}


public class Book {
	private static int counter = 0;
	public Book(String titel) {
		System.out.println("Sold book: " + titel);
		++counter;
	}
	
	public static int getCounter() {
		return counter;
	}
}

//Java里的默认对象的equals方法继承自Object，与==一样，只有当两个对象所引用的内存一致时，才返回true；String的==仍是默认比较内存是否一致，equals是重载过的比较字符串内容是否一样
public class HelloJava {
	public static void main(String[] args) {
		Book b1 = new Book("abc");
		Book b2 = new Book("abc");
		Book b3 = b1;
		System.out.println(b1 == b2);//System.out.println(b1.equals(b2));
		System.out.println(b1 == b3);//System.out.println(b1.equals(b3));
	}
}
输出如下：
Sold book: abc
Sold book: abc
false
true

//重新计算对象的哈希码 P200
import java.awt.Color;

public class HelloJava {
	public static void main(String[] args) {
		Cat cat1 = new Cat("Java", 12, 21, Color.BLACK);
		Cat cat2 = new Cat("C++", 12, 21, Color.WHITE);
		Cat cat3 = new Cat("Java", 12, 21, Color.BLACK);
		
		System.out.println("Cat1 hashCode: " + cat1.hashCode());
		System.out.println("Cat2 hashCode: " + cat2.hashCode());
		System.out.println("Cat3 hashCode: " + cat3.hashCode());
		
		System.out.println("Cat1 equals Cat2: " + cat1.equals(cat2));
		System.out.println("Cat1 equals Cat3: " + cat1.equals(cat3));
	}
}

import java.awt.Color;

public class Cat {
	private String name;
	private int age;
	private double weight;
	private Color color;
	
	public Cat(String name, int age, double weight, Color color) {
		this.name = name;
		this.age = age;//age = age这样赋值，将没有效果，不像书中所述可以去掉this
		this.weight = weight;
		this.color = color;
	}

	//@Override告诉编译器下面的函数是重载，如果识写成Equals，则会有编译错误，如果不加@Override，就算函数写错了，编译器也只会认为是新增了一个函数
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Cat cat = (Cat) obj;
		return name.equals(cat.name) && (age == cat.age)
				&& (weight == cat.weight) && (color.equals(cat.color));
	}
	
	@Override
	public int hashCode() {
		//这里的几个数字可以随便加，只是为了区分不同对象的哈希码
		//Integer和Double这些原始数据类型要转换成原数据类型才有hashCode函数，这也是这些数据类型可以直接用==比较的原因吗？
		return 7 * name.hashCode() + 11 * new Integer(age).hashCode()
				+ 13 * new Double(weight).hashCode() + 17 * color.hashCode();
	}
	
	public void show() {
		System.out.println(name + age + weight + color);
	}
}

//P201 汉诺塔
//P202 单例模式
//Java里没有static局部变量，所以不能像C++一样，在getInstance里声明一个static的单例对象
public class HelloJava {
	public static void main(String[] args) {
		Emperor emperor1 = Emperor.getInstance();
		Emperor emperor2 = Emperor.getInstance();
		Emperor emperor3 = Emperor.getInstance();
		
		emperor1.getName();
		emperor2.getName();
		emperor3.getName();
	}
}


public class Emperor {
	private static Emperor emperor = null;
	private Emperor() {
	}
	
	public static Emperor getInstance() {//只在使用时才创建
		if (null == emperor) {
			emperor = new Emperor();
		}
		return emperor;
	}
	
	public void getName() {
		System.out.println("我是皇帝：明日科技");
	}
}

//可参考Head First中所讲的，在程序初始化就将实例建好。在C++程序中搜“private static Singleton uniqueInstance = new Singleton();”
public class Emperor {
	private static Emperor emperor = new Emperor();
	private Emperor() {
	}
	
	public static Emperor getInstance() {
		return emperor;
	}
	
	public void getName() {
		System.out.println("我是皇帝：明日科技");
	}
}


//P209 接口
public class GifSaver implements ImageSaver {
	@Override
	public void save() {//如果不implements ImageSaver，则加了@Override会提供报错
		System.out.println("save as GIF");
	}
	
	public static void main(String[] args) {
		GifSaver gs = new GifSaver();
		gs.save();
	}
}

public interface ImageSaver {
	void save();//只能是public或不加即public，不能为private
}

//接口和继承的使用 P208
import java.awt.Point;

public class Image {//如果这里加了final，则此类将无法被继承
	private String name;//不加修饰符，则默认为public
	private Point size;
	
	public Image(String name, Point size) {
		this.name = name;
		this.size = size;
	}
	
	public String getName() {
		return name;
	}
	
	public Point getSize() {
		return size;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSize(Point size) {
		this.size = size;
	}
}


public interface ImageSaver {
	void save();
}


import java.awt.Point;

public class GifSaver extends Image implements ImageSaver {//如果有多个接口要实现，则用,分隔多个接口
	private static String extendsName = ".gif";
	
	public GifSaver(String name, Point size) {
		super(name, size);//这里调用父类的构造函数
	}
	
	@Override
	public void save() {
		System.out.println(getName() + " is saved as a GIF.");
	}
	
	@Override
	public String getName() {//这里改为private，将提示不能减少基类的可见性。可将基类的修饰符改为protected，这里改为public
		return super.getName() + extendsName;
	}
	
	public static void main(String[] args) {
		GifSaver gs = new GifSaver("hello", new Point(3, 4));//可改为：Image gs = new GifSaver("hello", new Point(3, 4));，则gs.save()将无法调用
		gs.save();
		System.out.println(gs.getName());
		
		Image i = new Image("abc", new Point(4, 5));//如果在Image的声明里加了abstract，则是虚基类，那么这里将不能实例化
		//i.save();//save是接口提供的函数，父类没有此函数
		System.out.println(i.getName());
	}
}
输出如下：
hello.gif is saved as a GIF.
hello.gif
abc

//P218 提到反射机制
在基类中 return this.getClass().getSimpleName();
有点类似C++中的typeid(someClass).name()

//P221 使用Comparable接口自定义排序
public class Employee implements Comparable<Employee>{
	private int id;
	private String name;
	private int age;
	
	public Employee(int id, String name, int age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}
	
	@Override
	public int compareTo(Employee o) {
		if (id > o.id) {
			return 1;
		}
		else if (id < o.id) {
			return -1;
		}
		return 0;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Id: " + id + ",");
		sb.append("name: " + name + ",");
		sb.append("age: " + age);
		return sb.toString();
	}
}


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {
	public static void main(String args[]) {
		Employee e1 = new Employee(3, "Li Lei", 26);
		Employee e2 = new Employee(2, "Han Meimei", 25);
		Employee e3 = new Employee(1, "Kily", 27);
		
		List<Employee> employeeList = new ArrayList<Employee>();//这是数组型的List，也可以创建链表型的LinkedList<>
		employeeList.add(e1);
		employeeList.add(e2);
		employeeList.add(e3);
		
		System.out.println("Before sort: ");//System.out.println(employeeList);可以直接打印整个List
		for (Employee e : employeeList) {
			System.out.println(e);
		}
		
		Collections.sort(employeeList);
		
		System.out.println("After sort: ");
		for (Employee e : employeeList) {
			System.out.println(e);
		}
	}
}
输出如下：
Before sort: 
Id: 3,name: Li Lei,age: 26
Id: 2,name: Han Meimei,age: 25
Id: 1,name: Kily,age: 27
After sort: 
Id: 1,name: Kily,age: 27
Id: 2,name: Han Meimei,age: 25
Id: 3,name: Li Lei,age: 26

//动态设置类的私有域 P222
public class Student {
	private int id;
	private String name;
	private boolean male;
	private double account;
	
	public Student(int id, String name, boolean male, double account) {
		this.id = id;
		this.name = name;
		this.male = male;
		this.account = account;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isMale() {
		return male;
	}
	
	public double getAccount() {
		return account;
	}
}


import java.lang.reflect.Field;

public class Test {
	public static void main(String args[]) {
		Student student = new Student(1, "Li Lei", true, 100);
		Class<?> clazz = student.getClass();
		System.out.println("The standard class name: " + clazz.getCanonicalName());
		System.out.println("The simple class name: " + clazz.getSimpleName());
		try {
			Field id = clazz.getDeclaredField("id");
			System.out.println("Before id is set: " + student.getId());
			id.setAccessible(true);
			id.setInt(student, 10);
			System.out.println("After id is set: " + student.getId());
			
			Field name = clazz.getDeclaredField("name");
			System.out.println("Before name is set: " + student.getName());
			name.setAccessible(true);
			name.set(student, "Han Meimei");
			System.out.println("After name is set: " + student.getName());
			
			Field male = clazz.getDeclaredField("male");
			System.out.println("Before male is set: " + student.isMale());
			male.setAccessible(true);
			male.setBoolean(student, false);
			System.out.println("After male is set: " + student.isMale());
			
			Field account = clazz.getDeclaredField("account");
			System.out.println("Before account is set: " + student.getAccount());
			account.setAccessible(true);
			account.setDouble(student, 200);
			System.out.println("After account is set: " + student.getAccount());
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
输出如下：
The standard class name: Student
The simple class name: Student
Before id is set: 1
After id is set: 10
Before name is set: Li Lei
After name is set: Han Meimei
Before male is set: true
After male is set: false
Before account is set: 100.0
After account is set: 200.0

//基类与子类的类型转换 P223 练习2
class HighSchoolStudent extends Student {
	public HighSchoolStudent(int id, String name, boolean male, double account) {
		super(id, name, male, account);
	}
	
	@Override
	public String getName() {
		return "High School: " + super.getName();
	}
	
	public void highSchoolSpecification() {
		System.out.println("I'm high school student.");
	}
}


public class Test {
	public static void main(String args[]) {
		HighSchoolStudent hss = new HighSchoolStudent(1, "Li Lei", true, 100);
		System.out.println(hss.getName());
		hss.highSchoolSpecification();
		
		Student s = (Student)hss;//类型转换直接用()即可
		System.out.println(s.getName());
		//s.highSchoolSpecification();//这里转换成基类后，没有子类函数了
		
		hss = (HighSchoolStudent)s;
		hss.highSchoolSpecification();
	}
}

P225 抽象方法不能使用private或static关键字进行修饰，包含一个或多个抽象方法的类必须被声明为抽象类。

//P229 内部类
public class OuterClass {
	innerClass in = new innerClass(); // 在外部类实例化内部类对象引用
	
	public void ouf() {
		in.inf(); // 在外部类方法中调用内部类方法
	}
	
	class innerClass {//这里加private，没有什么差异
		innerClass() { // 内部类构造方法
		}
		
		public void inf() { // 内部类成员方法
		}
		
		int y = 0; // 定义内部类成员变量
	}
	
	//这种写法编译不过，内部类必须依赖外部类的存在，而不能用静态函数来创建内部类
//	public static innerClass getInnerClass() {
//		return new innerClass();
//	}

	public innerClass doit() { // 外部类方法，返回值为内部类引用
		// y=4; //外部类不可以直接访问内部类成员变量
		in.y = 4;
		return new innerClass(); // 返回内部类引用
	}
	
	public static void main(String args[]) {
		OuterClass out = new OuterClass();
		// 内部类的对象实例化操作必须在外部类或外部类中的非静态方法中实现
		OuterClass.innerClass in = out.doit();
		System.out.println(in.y);
		System.out.println(out.in.y);
		OuterClass.innerClass in2 = out.new innerClass();//实例化内部类时需要用外部类对象来实例化，new out.innerClass()这种写法错误
	}
}

//P229 this关键字获取内部类与外部类的引用
public class TheSameName {
	private int x;
	
	public void doit() {
		x = 5;
		Inner in = new Inner();
		in.doit(x);
	}
	
	private class Inner {
		private int x = 9;
		public void doit(int x) {
			x++; // 调用的是形参x
			this.x++; // 调用内部类的变量x
			TheSameName.this.x++; // 调用外部类的变量x
		}
	}
	
	public static void main(String args[]) {
		TheSameName tsn = new TheSameName();
		tsn.doit();
	}
}

//对字符串的赋值，相当于对形参的引用改变，而对调用处的入参没有影响
public class TheSameName {
	public void doit(String s) {
		s = "abc";
	}
	
	public static void main(String args[]) {
		String s = "aaa";
		TheSameName tsn = new TheSameName();
		tsn.doit(s);
		System.out.println(s);
	}
}
输出如下：
aaa

http://www.cnblogs.com/dolphin0520/p/3736238.html
如果只有在想明确禁止 该方法在子类中被覆盖的情况下才将方法设置为final的。
注：类的private方法会隐式地被指定为final方法。
对于一个final变量，如果是基本数据类型的变量，则其数值一旦在初始化之后便不能更改；如果是引用类型的变量，则在对其初始化之后便不能再让其指向另一个对象。（类似C++中的引用）

public class Test {
	public static void main(String args[]) {
		String a = "hello2";  

        final String b = "hello";
        String d = "hello";
        String c = b + 2;  
        String e = d + 2;
        System.out.println((a == c));//true
        System.out.println((a == e));//false
	}
}
大家可以先想一下这道题的输出结果。为什么第一个比较结果为true，而第二个比较结果为fasle。这里面就是final变量和普通变量的区别了，当final变量是基本数据类型以及String类型时，如果在编译期间能知道它的确切值，则编译器会把它当做编译期常量使用。也就是说在用到该final变量的地方，相当于直接访问的这个常量，不需要在运行时确定。这种和C语言中的宏替换有点像。因此在上面的一段代码中，由于变量b被final修饰，因此会被当做编译器常量，所以在使用到b的地方会直接将变量b 替换为它的  值。而对于变量d的访问却需要在运行时通过链接来进行。想必其中的区别大家应该明白了，不过要注意，只有在编译期间能确切知道final变量值的情况下，编译器才会进行这样的优化。比如下面的这段代码就不会进行优化：
public class Test {
    public static void main(String[] args)  {
        String a = "hello2";  
        final String b = getHello();
        String c = b + 2;  
        System.out.println((a == c));//false
    }
     
    public static String getHello() {
        return "hello";
    }
}

上面这段代码好像让人觉得用final修饰之后，就不能在方法中更改变量i的值了。殊不知，方法changeValue和main方法中的变量i根本就不是一个变量，因为java参数传递采用的是值传递，对于基本类型的变量，相当于直接将变量进行了拷贝。所以即使没有final修饰的情况下，在方法内部改变了变量i的值也不会影响方法外的i。
运行这段代码就会发现输出结果为 helloworld。很显然，用final进行修饰并没有阻止在changeValue中改变buffer指向的对象的内容。有人说假如把final去掉了，万一在changeValue中让buffer指向了其他对象怎么办。有这种想法的朋友可以自己动手写代码试一下这样的结果是什么，如果把final去掉了，然后在changeValue中让buffer指向了其他对象，也不会影响到main方法中的buffer，原因在于java采用的是值传递，对于引用变量，传递的是引用的值，也就是说让实参和形参同时指向了同一个对象，因此让形参重新指向另一个对象对实参并没有任何影响。


P230 局部内部类
P231 匿名内部类
在图形化编程的事件监控器代码中，会大量使用匿名内部类，这样可大大简化代码，并增强代码的可读性。

public interface IStringDeal {
	public String filterBlankChar();
}

public class Test {
	public static void main(String args[]) {
		final String sourceStr = "abc de f";//这里不加final也没什么问题，但是在匿名类中不能修改sourceStr的值。2.为什么局部内部类和匿名内部类只能访问局部final变量？
		IStringDeal s = new IStringDeal() {
			@Override
			public String filterBlankChar() {
				String convertStr = sourceStr;//这里直接用sourceStr.replace(" ", "")也没什么问题？
				convertStr = convertStr.replace(" ", "");
				return convertStr;
			}
		};
		
		System.out.println("源字符串：" + sourceStr);
		System.out.println("转换后的字符串：" + s.filterBlankChar());
	}
}
输出如下：
源字符串：abc de f
转换后的字符串：abcdef

//如果不用使用匿名类，则需要修改为如下局部内部类方式。这种写法虽然能达到一样的效果，但是既冗长又难以维护，所以一般使用匿名内部类的方法来编写事件监听代码。同样的，匿名内部类也是不能有访问修饰符和static修饰符的。
public class Test {
	public static void main(String args[]) {
		final String sourceStr = "abc de f";
		
		class Dealer implements IStringDeal {
			@Override
			public String filterBlankChar() {
				String convertStr = sourceStr;
				convertStr = convertStr.replace(" ", "");
				return convertStr;
			}
		}
		
		IStringDeal s = new Dealer();
		
		System.out.println("源字符串：" + sourceStr);
		System.out.println("转换后的字符串：" + s.filterBlankChar());
	}
}

http://www.cnblogs.com/dolphin0520/p/3811445.html
代码中需要给按钮设置监听器对象，使用匿名内部类能够在实现父类或者接口中的方法情况下同时产生一个相应的对象，但是前提是这个父类或者接口必须先存在才能这样使用。当然像下面这种写法也是可以的，跟上面使用匿名内部类达到效果相同。
匿名内部类是唯一一种没有构造器的类。正因为其没有构造器，所以匿名内部类的使用范围非常有限，大部分匿名内部类用于接口回调。匿名内部类在编译的时候由系统自动起名为Outter$1.class。一般来说，匿名内部类用于继承其他类或是实现接口，并不需要增加额外的方法，只是对继承方法的实现或是重写。

最后补充一点知识：关于成员内部类的继承问题。一般来说，内部类是很少用来作为继承用的。但是当用来继承的话，要注意两点：
　　1）成员内部类的引用方式必须为 Outter.Inner.
　　2）构造器中必须有指向外部类对象的引用，并通过这个引用调用super()。这段代码摘自《Java编程思想》
//内部类的继承 P233
class WithInner {
    class Inner{
         
    }
}
class InheritInner extends WithInner.Inner {
    // InheritInner() 是不能通过编译的，一定要加上形参
    InheritInner(WithInner wi) {
        wi.super(); //必须有这句调用
    }
  
    public static void main(String[] args) {
        WithInner wi = new WithInner();
        InheritInner obj = new InheritInner(wi);
    }
}

//P233 编译不过
错误: 在类 Test 中找不到 main 方法, 请将 main 方法定义为:
   public static void main(String[] args)
否则 JavaFX 应用程序类必须扩展javafx.application.Application
public class Test {
	static int x = 100;
	
	static class Inner {
		static void doitinner() {
			System.out.println("外部类的成员变量：" + x);
		}
		
		public static void maint(String args[]) {
			doitinner();
		}
	}
}

//P233 局部内部类设置闹钟，没运行起来？
public class Test {
	public static void main(String args[]) {
		AlarmClock ac = new AlarmClock(1, true);
		ac.start();
	}
}

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

public class AlarmClock {
	private int delay;
	private boolean flag;
	
	public AlarmClock(int delay, boolean flag) {
		this.delay = delay;
		this.flag = flag;
	}
	
	public void start() {
		class Printer implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat format = new SimpleDateFormat("k:m:s");
				String result = format.format(new Date());
				System.out.println("Current time is: " + result);
				if (flag) {
					Toolkit.getDefaultToolkit().beep();
				}
			}
		}
		new Timer(delay, new Printer()).start();
	}
}


//P234 静态内部类求极值

public class MaxMin {
	public static class Result {//这里改为private则Test中不可见，编译报错
		private double max;
		private double min;
		
		public Result(double max, double min) {
			this.max = max;
			this.min = min;
		}
		
		public double getMax() {
			return max;
		}
		
		public double getMin() {
			return min;
		}
	}
	
	public static Result getResult(double[] array) {
		double max = Double.MIN_VALUE;//设置最小值给max
		double min = Double.MAX_VALUE;//设置最大值给max
		
		for (double i : array) {
			if (i > max) {
				max = i;
			}
			if (i < min) {
				min = i;
			}
		}
		
		return new Result(max, min);
	}
}

public class Test {
	public static void main(String args[]) {
		double array[] = new double[5];
		for (int i = 0; i < array.length; i++) {
			array[i] = 100 * Math.random();
		}
		System.out.println("源数组：");
		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i]);
		}
		
		MaxMin.Result result= MaxMin.getResult(array);
		System.out.println("最大值：" + result.getMax());
		System.out.println("最小值：" + result.getMin());
	}
}
输出如下：
源数组：
20.280779188500432
4.834134188174344
50.30131219984979
52.04190222448042
39.703315549452455
最大值：52.04190222448042
最小值：4.834134188174344

//P235 Class类与Java反射

//P238 访问构造方法
public class MoreConstructor {
	String s;
	int i, i2, i3;
	
	private MoreConstructor() {
	}
	
	protected MoreConstructor(String s, int i) {
		this.s = s;
		this.i = i;
	}
	
	public MoreConstructor(String... strings) throws NumberFormatException {
		if (0 < strings.length) {
			i = Integer.valueOf(strings[0]);
		}
		if (1 < strings.length) {
			i2 = Integer.valueOf(strings[1]);
		}
		if (2 < strings.length) {
			i3 = Integer.valueOf(strings[2]);
		}
	}
	
	public void print() {
		System.out.println("s=" + s);
		System.out.println("i=" + i);
		System.out.println("i2=" + i2);
		System.out.println("i3=" + i3);
	}
}

import java.lang.reflect.Constructor;

public class Test {
	public static void main(String args[]) {
		MoreConstructor example = new MoreConstructor();
		Class<?> exampleC = example.getClass();
		Constructor<?>[] declaredConstructors = exampleC.getDeclaredConstructors();
		for (int i = 0; i < declaredConstructors.length; ++i) {
			Constructor<?> constructor = declaredConstructors[i];
			System.out.println("查看是否允许带有可变数量的参数：" + constructor.isVarArgs());
			
			System.out.println("该构造方法的入口参数类型依次为：");
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			for (int j = 0; j < parameterTypes.length; j++) {
				System.out.println(" " + parameterTypes[j]);
			}
			
			System.out.println("该构造方法可能抛出的异常类型为：");
			Class<?>[] exceptionTypes = constructor.getExceptionTypes();
			for (int j = 0; j < exceptionTypes.length; j++) {
				System.out.println(" " + exceptionTypes[j]);
			}
			
			MoreConstructor example2 = null;
			while (example2 == null) {
				try {
					//这里有点问题的话，i=0的时候不一定是第一个声明顺序中的第一个构造函数，导致构造失败，死循环
					if (i == 0) {
						Object[] parameters = new Object[] {new String[] {"1", "2", "3"}};
						example2 = (MoreConstructor) constructor.newInstance(parameters);
					}
					else if (i == 1) {
						example2 = (MoreConstructor) constructor.newInstance("7", 5);
					}
					else {
						example2 = (MoreConstructor) constructor.newInstance();
					}
				}
				catch (Exception e) {
					System.out.println("在创建对象时抛出异常，下面执行setAccessible()方法");
					constructor.setAccessible(true);
				}
			}
			example2.print();
			System.out.println();
		}
	}
}

//P240访问成员变量
//P242访问方法
访问一个名称为print、入口参数类型依次为String和int型的方法，可以通过下面两种方式实现：
objectClass.getDeclaredMethod("print", String.class, int.class);
objectClass.getDeclaredMethod("print", new Class[] {String.class, int.class});

//字符串转成int，不能用(int)s进行转型
System.out.println(Integer.valueOf("100"));

//P244 利用反射查看类的成员
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;


public class ClassDeclarationViewer {
	public static void main(String args[]) throws ClassNotFoundException {
		Class<?> clazz = Class.forName("java.util.ArrayList");
		System.out.println("类的标准名称：" + clazz.getCanonicalName());
		System.out.println("类的修饰符：" + Modifier.toString(clazz.getModifiers()));
		
		//泛型参数
		TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
		System.out.print("类的泛型参数：");
		if (typeVariables.length != 0) {
			for (TypeVariable<?> typeVariable : typeVariables) {
				System.out.println(typeVariable + "\t");
			}
		}
		else {
			System.out.println("空");
		}
		
		Type[] interfaces = clazz.getGenericInterfaces();
		System.out.println("类所实现的接口：");
		if (interfaces.length != 0) {
			for (Type type : interfaces) {
				System.out.println("\t" + type);
			}
		}
		else {
			System.out.println("\t空");
		}
		
		Type superClass = clazz.getGenericSuperclass();
		System.out.print("类的直接继承类：");
		if (superClass != null) {
			System.out.println(superClass);
		}
		else {
			System.out.println("空");
		}
		
		Annotation[] annotations = clazz.getAnnotations();
		System.out.print("类的注释：");
		if (annotations.length != 0) {
			for (Annotation annotation : annotations) {
				System.out.println("\t" + annotation);
			}
		}
		else {
			System.out.println("空");
		}
	}
}
输出如下：
类的标准名称：java.util.ArrayList
类的修饰符：public
类的泛型参数：E	
类所实现的接口：
	java.util.List<E>
	interface java.util.RandomAccess
	interface java.lang.Cloneable
	interface java.io.Serializable
类的直接继承类：java.util.AbstractList<E>
类的注释：空

//P246 动态调用类中方法
import java.lang.reflect.Method;

public class MethodTest {
	public static void main(String args[]) {
		try {
			System.out.println("调用Math类的静态方法sin()");
			Method sin = Math.class.getDeclaredMethod("sin", Double.TYPE);//double.class一样
			double sin1 = (double) sin.invoke(null, new Integer(1));//double sin1 = (double) sin.invoke(null, 1);一样
			System.out.println("1的正弦值是：" + sin1);
			
			System.out.println("调用String类的非静态方法equals()");
			Method equals = String.class.getDeclaredMethod("equals", Object.class);
			boolean flag = (boolean) equals.invoke(new String("abc"), "abc");//boolean flag = (boolean) equals.invoke("abc", "abc");一样
			System.out.println("字符串是否相等：" + flag);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

//P246 利用反射重写toString()方法。Object类的toString()函数默认是输出类名和哈希码
public class MethodTest {
	public static void main(String args[]) {
		String s = "abc";
		System.out.println(s.toString());
	}
}

public class Test {
	public static void main(String args[]) {
		MethodTest mt = new MethodTest();
		System.out.println(mt.toString());
	}
}
输出如下：
MethodTest@1db9742

//书中例子
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class StringUtils {
	@SuppressWarnings("unchecked")//取消 unchecked 警告
	public String toString(Object object) {
		Class<?> clazz = object.getClass();
		StringBuilder sb = new StringBuilder();
		Package packageName = clazz.getPackage();
		sb.append("包名：" + packageName.getName() + "\t");
		
		String className = clazz.getSimpleName();
		sb.append("类名：" + className + "\n");
		
		//这三个部分感觉可以用泛型函数来写，目前还不会-.-？
		sb.append("公共构造方法：\n");
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			String modifier = Modifier.toString(constructor.getModifiers());
			if (modifier.contains("public")) {
				sb.append(constructor.toGenericString() + "\n");
			}
		}
		
		sb.append("公共域 ：\n");
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String modifier = Modifier.toString(field.getModifiers());
			if (modifier.contains("public")) {
				sb.append(field.toGenericString() + "\n");
			}
		}
		
		sb.append("公共方法：\n");
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			String modifier = Modifier.toString(method.getModifiers());
			if (modifier.contains("public")) {
				sb.append(method.toGenericString() + "\n");
			}
		}
		
		return sb.toString();
	}
	
	public static void main(String args[]) {
		System.out.println(new StringUtils().toString(new java.util.Date()));
	}
}
输出如下：
包名：java.util	类名：Date
公共构造方法：
public java.util.Date(java.lang.String)
public java.util.Date(int,int,int,int,int,int)
public java.util.Date(int,int,int,int,int)
public java.util.Date()
public java.util.Date(long)
public java.util.Date(int,int,int)
公共域 ：
公共方法：
public boolean java.util.Date.equals(java.lang.Object)
public java.lang.String java.util.Date.toString()
public int java.util.Date.hashCode()
public java.lang.Object java.util.Date.clone()
public int java.util.Date.compareTo(java.lang.Object)
public int java.util.Date.compareTo(java.util.Date)
public boolean java.util.Date.after(java.util.Date)
public boolean java.util.Date.before(java.util.Date)
public static long java.util.Date.parse(java.lang.String)
public int java.util.Date.getDate()
public long java.util.Date.getTime()
public void java.util.Date.setTime(long)
public java.time.Instant java.util.Date.toInstant()
public void java.util.Date.setDate(int)
public static long java.util.Date.UTC(int,int,int,int,int,int)
public static java.util.Date java.util.Date.from(java.time.Instant)
public int java.util.Date.getDay()
public int java.util.Date.getHours()
public int java.util.Date.getMinutes()
public int java.util.Date.getMonth()
public int java.util.Date.getSeconds()
public int java.util.Date.getTimezoneOffset()
public int java.util.Date.getYear()
public void java.util.Date.setHours(int)
public void java.util.Date.setMinutes(int)
public void java.util.Date.setMonth(int)
public void java.util.Date.setSeconds(int)
public void java.util.Date.setYear(int)
public java.lang.String java.util.Date.toGMTString()
public java.lang.String java.util.Date.toLocaleString()

//Set集合 P253
不允许存在重复值，可以使用Set集合中的addAll()方法，将Collection集合添加到Set集合中并除掉重复值。
//书中例子
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class Test {
	public static void main(String args[]) {
		List<String> list = new LinkedList<String>();
		list.add("apple");
		list.add("pear");
		list.add("banana");
		list.add("apple");
		//list.add("a");
		System.out.println(list);
		
		Set<String> set = new TreeSet<String>();//TreeSet自带默认排序功能，HashSet自带HashCode排序功能
		set.addAll(list);
		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next() + "\t");
			
		}
		
		set.clear();
		set.add("banana");
		set.add("pear");
		set.add("apple");
		System.out.println(set);
		
		set.clear();
		set.add("c");
		set.add("b");
		set.add("a");
		System.out.println(set);
	}
}

//P259 List接口的实现类
ArrayList：按索引查找快，插入删除慢
LinkedList：按索引查找慢，插入删除快
import java.util.ArrayList;

public class Test {
	public static void main(String args[]) {
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		
		int i = (int) (Math.random() * (list.size()));
		System.out.println("随机获取数组中的元素：" + list.get(i));
		list.remove(2);
		System.out.println("索引2的元素移除后，数组为：" + list);
	}
}

//P260 Set接口的实现类
HashSet/TreeSet。Set中的对象是无序的。

//如果Employee未实现Comparable的接口，则只能插入到HashSet中，插入到TreeSet中将报错
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Test {
	public static void main(String args[]) {
		Employee e1 = new Employee(3, "Li Lei", 26);
		Employee e2 = new Employee(2, "Han Meimei", 25);
		Employee e3 = new Employee(1, "Kily", 27);
		
		Set<Employee> employeeList = new HashSet<Employee>();//Employee实现了Comparable接口后，就可以用Set<Employee> employeeList = new TreeSet<Employee>();保存了，输出如下第2段输出，并且是排好序的
		employeeList.add(e1);
		employeeList.add(e2);
		employeeList.add(e3);
		
		System.out.println("The order in set:");
		for (Employee e : employeeList) {
			System.out.println(e);
		}
	}
}
输出如下：
The order in set:
Id: 1,name: Kily,age: 27
Id: 3,name: Li Lei,age: 26
Id: 2,name: Han Meimei,age: 25

换成TreeSet输出如下：
The order in set:
Id: 1,name: Kily,age: 27
Id: 2,name: Han Meimei,age: 25
Id: 3,name: Li Lei,age: 26

//P261 Map接口的实现类
Map接口常用的实现类有HashMap和TreeMap。通常建议用HashMap实现类实现Map集合，因为由HashMap类实现的Map集合对于添加和删除映射关系效率更好。HashMap是基于哈希表的Map接口的实现，HashMap通过哈希码对其内部的映射关系进行快速查找；由HashMap类实现的Map集合对于添加或删除映射关系效率较高；而TreeMap中的映射关系存在一定的顺序，如果希望Map集合中的对象存在一琮的顺序，应该使用TreeMap类实现Map集合。
1. HashMap类
基于哈希表的Map接口实现。允许使用null值和null键，但必须保证键的唯一性。
2. TreeMap类
不仅实现了Map接口，还实现了java.util.SortedMap接口，因此集合中的映射关系具有一定的顺序。但在添加、删除和定位映射关系上，TreeMap类比HashMap类的性能差一些。由于TreeMap类实现的Map集合中的映射关系是根据键对象按照一定的顺序排列的，因此不允许键对象是null。
//我这里的HashMap和TreeMap顺序都一样了
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Test {
	public static void main(String args[]) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("001", "Zhang San");
		map.put("005", "Li Si");
		map.put("004", "Wang Yi");
		
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		System.out.println("HashMap类实现的Map集合，无序：");
		while (it.hasNext()) {
			String str = (String) it.next();//if not using String convertion，不加类型转换也没有问题
			String name = (String) map.get(str);
			System.out.println(str + " " + name);
		}
		
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.putAll(map);
		Iterator<String> iter = treeMap.keySet().iterator();
		System.out.println("TreeMap类实现的Map集合，键对象升序：");
		while (iter.hasNext()) {
			String str = (String) iter.next();
			String name = (String) map.get(str);
			System.out.println(str + " " + name);
		}
	}
}
输出如下：
HashMap类实现的Map集合，无序：
001 Zhang San
004 Wang Yi
005 Li Si
TreeMap类实现的Map集合，键对象升序：
001 Zhang San
004 Wang Yi
005 Li Si

//可以把所有的Key值换成Integer，如下
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Test {
	public static void main(String args[]) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "Zhang San");
		map.put(5, "Li Si");
		map.put(4, "Wang Yi");
		
		Set<Integer> set = map.keySet();
		Iterator<Integer> it = set.iterator();
		System.out.println("HashMap类实现的Map集合，无序：");
		while (it.hasNext()) {
			int str = it.next();//if not using String convertion
			String name = map.get(str);
			System.out.println(str + " " + name);
		}
		
		TreeMap<Integer, String> treeMap = new TreeMap<Integer, String>();
		treeMap.putAll(map);
		Iterator<Integer> iter = treeMap.keySet().iterator();
		System.out.println("TreeMap类实现的Map集合，键对象升序：");
		while (iter.hasNext()) {//System.out.println(treeMap);可以直接这样打印
			int str = iter.next();
			String name = map.get(str);
			System.out.println(str + " " + name);
		}
	}
}

//P265 迭代器
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

public class Test {
	public static void main(String args[]) {
		ArrayList<Integer> array = new ArrayList<>();
		Collections.addAll(array, 1, 2, 3, 4, 5, 6);
		System.out.println("集合中的元素：" + array);
		ListIterator<Integer> iterator = array.listIterator();//迭代器声明好的时候，是指向位置0的元素的
		
		//iterator.set(10);此时迭代器还会返回过任何值，无法set
		
		//所有的next的操作，都是指当前迭代器所指向的元素，无论是next()，还是nextIndex()
		//所有的previous的操作，都是指迭代器的前一个元素的
		
		System.out.println(iterator.next());
		iterator.add(-1);
		System.out.println("集合中的元素：" + array);
		
		boolean hasNext = iterator.hasNext();
		System.out.println("集合是否具有下一个元素：" + hasNext);
		
		boolean hasPrevious = iterator.hasPrevious();
		System.out.println("集合是否具有前一个元素：" + hasPrevious);
		
		int next = iterator.next();
		System.out.println("获得集合的下一个元素：" + next);
		
		//iterator.next();
		
		int nextIndex = iterator.nextIndex();
		System.out.println("获得集合的下一个元素的索引：" + nextIndex);
		System.out.println("获得元素的上一个元素的索引：" + iterator.previousIndex());
		
		
		int previous = iterator.previous();//返回当前迭代器最后一次next()得到的元素值（也可以认为是当前指向的元素值），并且将迭代器后退一个元素
		System.out.println("获得集合的前一个元素：" + previous);
		
		int previousIndex = iterator.previousIndex();//返回当前迭代器所指向的元素索引
		System.out.println("获得集合的前一个元素的索引：" + previousIndex);
		
		iterator.add(7);//在迭代器当前所指向的元素位置 增加一个元素，并且会使迭代器前进1
		System.out.println("向集合中增加元素7后的集合：" + array);
		
		System.out.println("增加元素7后的next()：" + iterator.next());
		iterator.set(12);//修改迭代器最后一次返回的元素值
		System.out.println("将获得的下一个元素修改成12后的集合：" + array);
		
		iterator.remove();//删除的是迭代器最后一次返回的元素值，并且会使迭代器index减1
		System.out.println("将获得的下一个元素删除后的集合：" + array);
		
		System.out.println("remove()后的next()值：" + iterator.next());
	}
}

//P267 逆序打出所有List中的元素
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

public class Test {
	public static void main(String args[]) {
		ArrayList<Integer> array = new ArrayList<>();
		Collections.addAll(array, 1, 2, 3, 4, 5, 6);
		System.out.println("集合中的元素：" + array);
		ListIterator<Integer> iterator = array.listIterator();
		
		for (; iterator.hasNext();) {
			iterator.next();
		}
		
		for (; iterator.hasPrevious();) {
			System.out.print(iterator.previous() + " ");
		}
	}
}

//迭代器对象中有个变量lastRet，用来维护最近一次操作的元素索引，初始时lastRet=-1，无法remove，当remove后，lastRet也会被置为-1，因此不能重复的进行remove操作
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

public class Test {
	public static void main(String args[]) {
		ArrayList<Integer> array = new ArrayList<>();
		Collections.addAll(array, 6, 5, 4, 3, 2, 2, 1);
		System.out.println("集合中的元素：" + array);

		ListIterator<Integer> iter = array.listIterator();
		iter.next();
		iter.remove();
		iter.remove();//抛异常
		System.out.println("集合中的元素：" + array);
	}
}

//List中删除元素最好用迭代器，否则自己遍历会容易出错
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

public class Test {
	public static void main(String args[]) {
		ArrayList<Integer> array = new ArrayList<>();
		Collections.addAll(array, 6, 5, 4, 3, 2, 2, 1);
		System.out.println("集合中的元素：" + array);
		
		//这种删除方式，会导致跳过了一个索引值，只能删除一个2
//		for (int i = 0; i < array.size(); ++i) {
//			int n = array.get(i);
//			if (n == 2) {
//				array.remove(i);//List.remove(index)中的参数是索引，而不是具体值
//			}
//		}
		
		for (ListIterator<Integer> iter = array.listIterator(); iter.hasNext();) {
			if (iter.next() == 2) {
				iter.remove();
			}
		}
		
		System.out.println("集合中的元素：" + array);
	}
}
输出如下：
集合中的元素：[6, 5, 4, 3, 2, 2, 1]
集合中的元素：[6, 5, 4, 3, 1]
//------------------------------------------------------------------------------------------------
//Java从入门到精通 第11章 异常处理 P272
Throwable类是所有异常类的超类，该类的两个直接子类是Error和Exception。其中，Error及其子类用于指示合理的应用程序不应该试图捕获的严重问题，Exception及其子类给出了合理的应用程序需要捕获的异常。

可捕获的异常（即Exception类的子类）分为可控式异常和运行时异常两种类型。
可控式异常可以捕获到
运行时异常

//运行时异常
public class Test {
	public static void main(String args[]) {
		try {
			int i = 3/0;
		} catch (Exception e) {
			e.printStackTrace();//可以用System.out.println(e.getMessage());来输出
		}
	}
}
报如下错误：
java.lang.ArithmeticException: / by zero
	at Test.main(Test.java:6)

11.2.3 算术异常 P276
整数被0除产生的异常。浮点数被0除，将不引发算法异常。
public class Test {
	public static void main(String args[]) {
		try {
			double d = 3.0 / 0;
			System.out.println(d);
		} 
		catch (ArithmeticException e) {
			e.printStackTrace();
		}
	}
}
输出如下：
Infinity

在Java的异常处理机制中，有一个默认处理异常的程序。当程序出现异常时，默认处理程序将显示一个描述异常的字符串，打印异常发生处的堆栈轨迹，并终止程序。

11.2.4 数组下标越界异常 P277
import java.util.Arrays;

public class Test {
	public static void main(String args[]) {
		int array[] = new int[5];
		Arrays.fill(array, 8);
		for (int i = 0; i < 6; ++i) {
			System.out.println(array[i]);
		}
	}
}
遍历数组，推荐使用foreach循环，可以避免数组下标越界。如果要使用数组的下标，需要记住数组的下标是从0开始计算的。如果需要使用数组的长度，则推荐使用length属性。另外使用ArrayList类也可以避免这些问题。

P278 捕获异常信息
public class Test {
	public static void main(String args[]) {
		try {
			int i = 3/0;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getMessage方法：\t" + e.getMessage());
			System.out.println("getLocalizedMessage方法：\t" + e.getLocalizedMessage());
			System.out.println("toString方法：\t" + e.toString());
		}
	}
}
输出如下：
java.lang.ArithmeticException: / by zero
	at Test.main(Test.java:4)
getMessage方法：	/ by zero
getLocalizedMessage方法：	/ by zero
toString方法：	java.lang.ArithmeticException: / by zero

11.4 处理异常 P280
11.4.2 使用 try...catch...finally 处理异常
finally后的两个大括号内的语句，不管程序是否发生异常都要执行（也就是说程序执行完try和catch之间的语句或执行完catch后两个大括号内的语句都将执行finally后的语句），因此finally语句块通常用于执行垃圾回收、释放资源等操作。
在Java中进行异常处理时，应该尽量使用finally块进行资源回收，因为在 try...catch...finally 语句块中，不管程序是否发生异常，最终都会执行finally语句块，因此可以在finally块中添加释放资源的代码。
（不加finally，不是也会正常执行到？）

//P281 创建IO对象，关闭对象
import java.io.FileInputStream;
import java.io.IOException;
import java.security.spec.ECGenParameterSpec;

public class CloseIo {
	private FileInputStream in = null;
	
	public void readInfo() {
		try {
			in = new FileInputStream("src/IStringDeal.java");
			System.out.println("创建IO流，分配内存资源。");
		} 
		catch (IOException io) {
			io.printStackTrace();
			System.out.println("创建IO对象发生异常。");
		}
		//感觉这个finally块不加也没什么问题，反正都会走到
		finally {
			if (in != null) {
				try {
					in.close();
					System.out.println("关闭IO流，释放内存资源。");
				} 
				catch (IOException ioe) {
					ioe.printStackTrace();
					System.out.println("关闭IO对象发生异常。");
				}
			}
		}
	}
	
	public static void main(String args[]) {
		CloseIo ex = new CloseIo();
		ex.readInfo();
	}
}
输出如下：
创建IO流，分配内存资源。
关闭IO流，释放内存资源。

11.4.3 使用try...finally处理异常 P282
finally里的语句不管程序是否发生异常，都必定会走进去
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.spec.ECGenParameterSpec;

public class CloseIo {
	private FileInputStream in = null;
	
	public void readInfo() {
		try {
			try {
				in = new FileInputStream("src/IStringsDeal.java");
				System.out.println("创建IO流，分配内存资源。");
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("创建IO对象发生异常。");
			}
		}
		finally {
			if (in != null) {
				try {
					in.close();
					System.out.println("关闭IO流，释放内存资源。");
				} 
				catch (IOException ioe) {
					ioe.printStackTrace();
					System.out.println("关闭IO对象发生异常。");
				}
			}
		}
	}
	
	public static void main(String args[]) {
		CloseIo ex = new CloseIo();
		ex.readInfo();
	}
}

11.5.1 使用throws声明抛出异常 P283
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.spec.ECGenParameterSpec;

public class CloseIo {
	private FileInputStream in = null;
	
	public void showInfo() throws Exception {
		FileInputStream in = new FileInputStream("src/abc.java");//这里如果in成功打开文件，但却没有关闭文件，可能造成资源泄露
	}
	
	public static void main(String args[]) {
		CloseIo ex = new CloseIo();
		ex.showInfo();//调showInfo函数可能会抛出异常，但这里却没有处理
	}
}

//P286 使用throw抛出异常
public class CloseIo {
	final static double PI = 3.14;
	
	public void computeArea(double r) throws Exception {
		if (r <= 20.0) {
			throw new Exception("程序异常：\n半径为：" + r + "\n半径不能小于20。");
		}
		else {
			double circleArea = PI * r * r;
			System.out.println("半径是" + r + "的圆的面积是：" + circleArea);
		}
	}
	
	public static void main(String args[]) {
		CloseIo ex = new CloseIo();
		try {
			ex.computeArea(20.1);
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
}

11.5.3 方法中抛出异常
如果某个类还没有实现，可以先在其中抛出异常，这样其他类调用时，会抛出异常，以便以后修改完成。

public class CloseIo {
	public void computeArea(double r) {
		throw new UnsupportedOperationException("方法尚未实现");
	}
	
	public static void main(String args[]) {
		CloseIo ex = new CloseIo();
		ex.computeArea(20.1);
	}
}
输出如下：
Exception in thread "main" java.lang.UnsupportedOperationException: 方法尚未实现
	at CloseIo.computeArea(CloseIo.java:4)
	at CloseIo.main(CloseIo.java:9)

11.6.1 创建自定义异常类 P287
public class NewException extends Exception {
	public NewException(String s) {
		super(s);
	}
}

public class Test {	
	public static void main(String args[]) {
		try {
			throw new NewException("Throw a NewException.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
输出如下：
Throw a NewException.

11.6.2 使用自定义异常类 P288
//感觉书上的例子输出不太对
public class NewException extends Exception {
	public NewException(String s) {
		System.out.println(s);
	}
}

public class Test {	
	public static void main(String args[]) {
		try {
			throw new NewException("Throw a NewException.");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
输出如下：
Throw a NewException.
NewException

11.7 异常使用原则
--不要过多的使用异常，这样会增加系统的负担。
--使用try...catch语句块捕获异常时，要对异常作出处理。
--tyr...catch语句块的范围不要太大，这样不利于对异常的分析。
--一个方法被覆盖时，覆盖它的方法必须抛出相同的异常或者子异常。
//------------------------------------------------------------------------------------------------
//Java从入门到精通 第12章 输入输出 P293
Java语言定义了许多类专门负责各种方式的输入/输出，这些类都被放在java.io包中。其中所有输入流类都是抽象类InputStream（字节输入流）或抽象类Reader（字符输入流）的子类；而所有输出流都是抽象类OutputStream（字节输出流）或抽象类Writer（字符输出流）的子类。

12.2.1 输入流 P294

12.2.2 输出流 P295

12.2.4 查找替换文本文件内容 P297

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Test {	
	public static void main(String args[]) {
		String before = "today";
		String after = "今天";
		
		FileReader reader = null;
		FileWriter writer = null;
		StringBuilder sb = new StringBuilder();
		
		int flag = 0;
		final int maxLength = 1024;
		char temp[] = new char[maxLength];
						
		String textFile = "src/TestFile.txt";
		try {
			reader = new FileReader(textFile);
			while ((flag = reader.read(temp)) != -1) {
				//这里如果不做这个判断，那么会将temp的1024个字符全都写进文件中，如果未读满，则会出现一堆未定义字符
				if (flag == maxLength) {
					sb.append(temp);
				}
				else {
					//将字符数组的特定长度附加到sb中
					sb.append(temp, 0, flag);
					
					//或者将获取的特定长度字符生成String，并附加到sb中
					// String tempString = new String(temp, 0, flag);
					// sb.append(tempString);
					
					//如下方法，字符转成toString得到的将是类名加HashCode的字符串，而不原字符串，类似[C@1db9742的内容
//					String tempString = temp.toString();
//					tempString = tempString.substring(0, flag);
//					sb.append(tempString);
				}
			}
			
			String content = sb.toString().replace(before, after);
			writer = new FileWriter(textFile);
			writer.write(content);
		} 
		catch (FileNotFoundException e1) {
			System.out.println(e1);
		}
		catch (IOException e2) {
			e2.printStackTrace();
		}
		finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (writer != null) {
					writer.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
替换前的文件内容：
today is a nice day.
What day is is today?
Hello.


替换后的文件内容：
今天 is a nice day.
What day is is 今天?
Hello.

toString()：
Returns a string representation of the object. In general, the toString method returns a string that "textually represents" this object. The result should be a concise but informative representation that is easy for a person to read. It is recommended that all subclasses override this method. 
The toString method for class Object returns a string consisting of the name of the class of which the object is an instance, the at-sign character `@', and the unsigned hexadecimal representation of the hash code of the object. In other words, this method returns a string equal to the value of: 

 getClass().getName() + '@' + Integer.toHexString(hashCode())

12.3.1 文件的创建与删除 P298

import java.io.File;

public class Test {	
	public static void main(String args[]) {
		File file = new File("src","abc.txt");//同"src/abc.txt"，这里的路径文件夹一定要存在，否则在创建文件时会报错
		if (file.exists()) {
			file.delete();
			System.out.println("文件已删除");
		}
		else {
			try {
				file.createNewFile();//如果存在原文件，这里创建新文件不会创建，也不会报错
				System.out.println("文件已创建");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}

//P300
import java.io.File;

public class Test {	
	public static void main(String args[]) {
		File file = new File("src/abc.txt");
		if (file.exists()) {
			System.out.println("文件名称：" + file.getName());
			System.out.println("文件长度：" + file.length());
			System.out.println("文件是否是隐藏文件：" + file.isHidden());
			System.out.println("父路径：" + file.getParent());
			System.out.println("文件绝对路径：" + file.getAbsolutePath());
			System.out.println("文件路径：" + file.getPath());
			System.out.println("系统分隔符：" + File.separator);
		}
		else {
			System.out.println("文件不存在");
		}
		
		String newFileName = new String(file.getParent() + File.separator + "def.txt");//将文件重命名，入参为新文件类。如果路径不同，也可以用于移动文件
		file.renameTo(new File(newFileName));
	}
}
输出如下：
文件名称：abc.txt
文件长度：0
文件是否是隐藏文件：false
父路径：src
文件绝对路径：D:\Program Files\eclipseJava workspace\LearningProject\src\abc.txt
文件路径：src\abc.txt
系统分隔符：\

12.4.1 FileInputStream与FileOutputStream类 P302
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Test {	
	public static void main(String args[]) {
		File file = new File("src","abc.txt");
		try {
			FileOutputStream out = new FileOutputStream(file);
			byte buy[] = "Hello".getBytes();
			out.write(buy);
			out.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			FileInputStream in = new FileInputStream(file);
			byte byt[] = new byte[1024];
			int len = in.read(byt);
			System.out.println("文件中的信息是：" + new String(byt, 0, len));
			in.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
输出如下：
文件中的信息是：Hello

虽然Java语言在程序结束时自动关闭所有打开的流，但是当使用完流后，显式地关闭任何打开的流仍是一个好习惯。一个被打开的流有可能会用尽系统资源，这取决于平台和实现。如果没有将打开的流关闭，当另一个程序试图打开另一个流时，这些资源可能会得不到。

12.4.2 FileReader类和FileWriter类 P303

//P307 删除文件夹中所有文件
import java.io.File;

public class Test {
	public void deleteFile(String path) {
		File file = new File(path);
		StringBuilder sb = new StringBuilder();
		deleteFile(file, sb);
		System.out.print(sb.toString());
	}
	
	private void deleteFile(File root, StringBuilder sbLog) {
		if (!root.exists()) {
			sbLog.append("File or directory does not exist. Path: \n" + root.getAbsolutePath());
			return;
		}
		if (root.isFile()) {
			deleteOneFile(root, sbLog);
		} else {
			File files[] = root.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					deleteOneFile(file, sbLog);
				} else {
					deleteFile(file, sbLog);
				}
			}
			deleteOneFile(root, sbLog);
		}
	}
	
	private void deleteOneFile(File file, StringBuilder sbLog) {
		String s = new String(file.isFile() ? "File is deleted: " : "Directory is delete: ");
		file.delete();
		sbLog.append(s + file.getAbsolutePath() + "\n");
	}
	
	public static void main(String args[]) {
		Test t = new Test();
		t.deleteFile("d:/Program Files/eclipseJava workspace/dbTest");
	}
}
输出如下：
File is deleted: d:\Program Files\eclipseJava workspace\dbTest\b.txt
File is deleted: d:\Program Files\eclipseJava workspace\dbTest\DirA\a.txt
Directory is delete: d:\Program Files\eclipseJava workspace\dbTest\DirA
Directory is delete: d:\Program Files\eclipseJava workspace\dbTest

12.5 带缓存的输入/输出流
//P309
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Student {
	public static void main(String args[]) {
		String content[] = {"abc", "def", "opq"};
		File file = new File("src", "abc.txt");
		try {
			FileWriter fw = new FileWriter(file);//构造函数可以是File对象，也可以是字符串文件路径
			BufferedWriter bufw = new BufferedWriter(fw);
			for (int k = 0; k < content.length; ++k) {
				bufw.write(content[k]);
				bufw.newLine();
			}
			bufw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader bufr = new BufferedReader(fr);
			String s = null;
			int i = 0;
			while ((s = bufr.readLine()) != null) {
				++i;
				System.out.println("Line " + i + ": " + s);
			}
			bufr.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
输出如下：
Line 1: abc
Line 2: def
Line 3: opq


//P310 文件属性
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class Student {
	public static void main(String args[]) {
		File file = new File("src/abc.txt");
		Properties properties = new Properties();
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			properties.load(reader);
			Enumeration<?> keys = properties.propertyNames();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = properties.getProperty(key);
				System.out.println("Key: " + key + "-- Value: "+ value);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
	}
}
输出如下：
Key: abc-- Value: 你好
Key: def-- Value: 天气不错
Key: opq-- Value: 什么

文件内容：
abc=你好
def:天气不错
opq    :    什么

//P311 合并多个txt文件
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Student {
	public static void main(String args[]) {
		File file = new File("d:/Program Files/eclipseJava workspace/LearningProject/dbTest");
		File textFiles[] = file.listFiles();
		BufferedReader reader = null;
		FileWriter writer = null;
		try {
			writer = new FileWriter("d:/Program Files/eclipseJava workspace/LearningProject/Concatenation.txt");
			for (File textFile : textFiles) { 
				if (textFile.isDirectory()) {
					continue;
				}
				reader = new BufferedReader(new FileReader(textFile));
				String line;
				while ((line = reader.readLine()) != null) {
					writer.write(line + "\n");
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

12.6 数据输入/输出流 P312
//
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Student {
	public static void main(String args[]) {
		try {
			FileOutputStream fs = new FileOutputStream("src/Concatenation.txt");
			DataOutputStream ds = new DataOutputStream(fs);
			ds.writeUTF("使用writeUTF()方法写入数据");
			//ds.writeChars("使用writeChars()方法写入数据");//这两个方法，将无法读出数据
			//ds.writeBytes("使用writeBytes方法写入数据");
			ds.close();
			
			FileInputStream fis = new FileInputStream("src/Concatenation.txt");
			DataInputStream dis = new DataInputStream(fis);
			System.out.println(dis.readUTF());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

12.7 ZIP压缩输入/输出流 P313
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MyZip {
	private void zip(String zipFilename, File inputFile) throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilename));
		zip(out, inputFile, "");
		System.out.println("Zipping...");
		out.close();
	}
	
	private void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) {
			File files[] = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < files.length; ++i) {
				zip(out, files[i], base + files[i]);
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			System.out.println(base);
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}
	
	public static void main(String args[]) {
		MyZip book = new MyZip();
		try {
			book.zip("d:/Program Files/eclipseJava workspace/LearningProject/dbTest.zip", 
					new File("d:/Program Files/eclipseJava workspace/LearningProject/dbTest"));
			System.out.println("Finish zipping.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

12.7.2 解压缩ZIP文件 P315 未看

//简单的投票软件 P319
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Candidate {
	public static void main(String args[]) {
		Candidate c = new Candidate();
		System.out.println(c.getBallot("B"));
	}
	
	public int getBallot(String name) {
		File file = new File("src/count.txt");
		FileReader fr = null;
		BufferedReader br = null;
		int len = 0;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String str[] = new String[3];
			String size;
			int i = 0;
			while ((size = br.readLine()) != null) {
				str[i] = size.trim();
				if (str[i].startsWith(name)) {
					int length = str[i].indexOf(":");
					String sub = str[i].substring(length + 1, str[i].length());
					len = Integer.parseInt(sub);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return len;
	}
}

count.txt内容如下：
A:3
B:4
C:5
//------------------------------------------------------------------------------------------------
//Java从入门到精通 第13章 Swing程序设计 P322
13.2.1 JFrame框架窗体
import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;


public class JFrameExample {
	public static void main(String args[]) {
		new JFrameExample().CreateJFrame("创建一个JFrame窗体");
	}
	
	public void CreateJFrame(String title) {
		JFrame jf = new JFrame(title);//创建一个初始不可见的，但是具体标题的窗体，可以不加title参数
		Container container = jf.getContentPane();
		JLabel jl = new JLabel("这是一个JFrame窗体");
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(jl);
		container.setBackground(Color.white);
		jf.setVisible(true);
		jf.setSize(400, 300);
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//DO_NOTHING_ON_CLOSE时，点击X无法关闭窗体；HIDE_ON_CLOSE时，点击X也可以关闭窗体
	}
}

13.2.2 JDialog窗体 P325

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;


class MyJDialog extends JDialog{
	private static final long serialVersionUID = 1L;
	
	public MyJDialog(JFrame jFrame) {
		//实例化一个JDailog类对象，指定对话框的父窗体、窗体标题和类型
		super(jFrame, "第一个JDialog窗体", true);
		Container container = getContentPane();
		container.add(new JLabel("这是一个对话框"));
		setSize(100, 100);
		//setBounds(120, 120, 100, 100); // 设置对话框窗体大小，其中前两个参数，是离屏幕左上角的距离
	}
}

public class MyFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static void main(String args[]) {
		new MyFrame();
	}
	
	public MyFrame() {
		Container container = getContentPane();
		container.setLayout(null);
		
		//这三行的JLabel无法显示出来，把上一行的container.setLayout(null);去掉，又会导致JButton占据了整个窗体，目前不知道如何解决？
//		JLabel jl = new JLabel("这是一个JFrame窗体");
//		jl.setHorizontalAlignment(SwingConstants.CENTER);
//		container.add(jl);
		
		JButton bl = new JButton("弹出对话框");
		bl.setBounds(10, 10, 100, 21);//可以设置JButton在窗体中的位置和其大小
		bl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MyJDialog(MyFrame.this).setVisible(true);
			}
		});
		container.add(bl);
		
		container.setBackground(Color.white);
		setSize(200, 200);
		//setBounds(100, 100, 200, 200);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}

13.2.3 设置窗体大小 P326
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlFormSize extends JFrame {
	public static void main(String args[]) {
		new ControlFormSize();
	}
	
	public ControlFormSize() {
		setTitle("设置窗体大小");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);//设置当前窗体面板，与getContentPane是相反的过程
		JLabel label = new JLabel("宽度：400， 高度：300");
		contentPane.add(label, BorderLayout.CENTER);
		setVisible(true);
	}
	
}

13.2.4 禁止改变窗体的大小 P327
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class LimitChangeFormSize extends JFrame {
	public static void main(String args[]) {
		new LimitChangeFormSize();
	}
	
	public LimitChangeFormSize() {
		Container container = getContentPane();
		container.setLayout(null);//不进行setLayout的话，则JButton将占据整个面板大小
		
		setTitle("设置窗体大小");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		setLocation(100, 100);
		
		String title = new String("切换窗体是否可变状态");
		JButton jb = new JButton(title);//居然没有找到可以修改JButton名称的方法？
		//jb.setBounds(MAXIMIZED_BOTH, MAXIMIZED_BOTH, 200, 22);
		jb.setSize(200, 22);
		//jb.setHorizontalAlignment(SwingConstants.CENTER);//没效果？只有JLabel才有用？
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setResizable(!isResizable());
			}
		});
		container.add(jb);
		
		setVisible(true);
	}
}

13.3.2 图标的使用 P328
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class DrawIcon implements Icon {
	private int width;
	private int height;

	public DrawIcon(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public int getIconHeight() {
		// TODO Auto-generated method stub
		return this.height;
	}

	@Override
	public int getIconWidth() {
		// TODO Auto-generated method stub
		return this.width;
	}

	@Override
	public void paintIcon(Component arg0, Graphics arg1, int x, int y) {
		// TODO Auto-generated method stub
		arg1.fillOval(x, y, width, height);
	}
	
	public static void main(String args[]) {
		DrawIcon icon = new DrawIcon(15, 15);
		JLabel jb = new JLabel("测试", icon, SwingConstants.CENTER);
		JFrame jf = new JFrame();
		Container c = jf.getContentPane();
		c.add(jb);
		jf.setSize(100, 100);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}

//P329 使用图片图标
import java.awt.Container;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class MyImageIcon extends JFrame {
	public MyImageIcon() {
		Container container = getContentPane();
		JLabel jl = new JLabel("这是一个JFrame窗体", JLabel.CENTER);
		URL url = MyImageIcon.class.getResource("imageButton.jpg");//需要将imageButton.jpg与MyImageIcon.class文件放置在同级目录。目前试了只有.jpg好用，.bmp无法识别
		Icon icon = new ImageIcon(url);
		jl.setIcon(icon);
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		jl.setOpaque(true);//设置标签为不透明状态
		container.add(jl);
		setSize(250, 100);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
	}
	
	public static void main(String args[]) {
		new MyImageIcon();
	}
}

13.3.3 为图片添加说明 P330
import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class UseLabelFrame extends JFrame {
	public UseLabelFrame() {
		super();
		setTitle("使用标签组件");
		setBounds(100, 100, 330, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JLabel label = new JLabel();
		label.setText("这是美丽的山水照片：");
		getContentPane().add(label, BorderLayout.NORTH);
		URL url = UseLabelFrame.class.getResource("wxPython.jpg");
		Icon icon = new ImageIcon(url);
		final JLabel labelPicture = new JLabel();
		labelPicture.setIcon(icon);
		getContentPane().add(labelPicture, BorderLayout.CENTER);
	}
	
	public static void main(String args[]) {
		UseLabelFrame frame = new UseLabelFrame();
		frame.setVisible(true);
	}
}

13.4 常用布局管理器 P331
13.4.1 绝对布局
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class AbsolutePosition extends JFrame {
	public AbsolutePosition() {
		setTitle("本窗体使用绝对布局");
		setLayout(null);//取消布局管理器
		setBounds(0, 0, 200, 150);//设置每个组件的大小与位置
		Container c = getContentPane();
		JButton b1 = new JButton("按钮1");
		JButton b2 = new JButton("按钮2");
		b1.setBounds(10, 30, 80, 30);
		b2.setBounds(60, 70, 100, 20);
		c.add(b1);
		c.add(b2);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new AbsolutePosition();
	}
}

13.4.2 流布局管理器 P332

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class FlowLayoutPosition extends JFrame {
	public FlowLayoutPosition() {
		setTitle("本窗体使用流布局管理器");
		Container c = getContentPane();
		setLayout(new FlowLayout(2, 10, 10));//第一个参数，FlowLayout.LEFT=0，左对齐；FlowLayout.CENTER=1，右对齐；FlowLayout.RIGHT=2，右对齐，第2，3参数设置各组件之间的水平间隔与垂直间隔
		
		for (int i = 0; i < 10; i++) {
			c.add(new JButton("button" + i));
		}
		setSize(300, 200);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new FlowLayoutPosition();
	}
}

13.4.3 边界布局管理器 P334
在默认不指定窗体布局的情况下，Swing组件的布局模式是边界（BorderLayout）布局管理器。如例13.3 ，在容器中只添加了一个标签组件，这个标签被放置在窗体中间，并且整个组件占据了窗体的所有空间，实质上在这个容器中默认使用了边界布局管理器。
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class BorderlayoutPosition extends JFrame {
	String border[] = {BorderLayout.CENTER, BorderLayout.NORTH, BorderLayout.SOUTH, BorderLayout.WEST, BorderLayout.EAST};
	String buttonName[] = {"center button", "north button", "south button", "west button", "east button"};
	
	public BorderlayoutPosition() {
		setTitle("本窗体使用边界布局管理器");
		Container c = getContentPane();
		setLayout(new BorderLayout());
		
		for (int i = 0; i < border.length; ++i) {
			c.add(border[i], new JButton(buttonName[i]));//提供在容器中添加组件的功能，并同时设置组件的摆放位置
		}
		
		setSize(350, 200);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new BorderlayoutPosition();
	}
}

13.4.4 风格布局管理器 P335
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GridLayoutPosition extends JFrame {
	public GridLayoutPosition() {
		setTitle("本窗体使用风格布局管理器");
		Container c = getContentPane();
		setLayout(new GridLayout(7, 3, 5, 5));//前两个参数表格网格的行数与列数，只有一个可以为0，代表一行或一列可以排列任意多个组件。后两个参数指定网格之间的间距。
		
		for (int i = 0; i < 20; ++i) {
			c.add(new JButton("button" + i));
		}
		
		setSize(300, 300);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new GridLayoutPosition();
	}
}

13.5 常用面板 P336
13.5.1 JPanel面板 P336
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class JPanelTest extends JFrame {
	public JPanelTest() {
		setTitle("在这个窗体中使用中面板");
		Container c = getContentPane();
		setLayout(new GridLayout(2, 1, 10, 10));
		
		JPanel p1 = new JPanel(new GridLayout(1, 3, 10, 10));
		JPanel p2 = new JPanel(new GridLayout(1, 2, 10, 10));
		JPanel p3 = new JPanel(new GridLayout(1, 2, 10, 10));
		JPanel p4 = new JPanel(new GridLayout(2, 10, 10, 10));
		
		p1.add(new JButton("1"));
		p1.add(new JButton("2"));
		p1.add(new JButton("3"));
		
		p2.add(new JButton("4"));
		p2.add(new JButton("5"));
		
		p3.add(new JButton("6"));
		p3.add(new JButton("7"));
		
		p4.add(new JButton("8"));
		p4.add(new JButton("9"));
		
		c.add(p1);
		c.add(p2);
		c.add(p3);
		c.add(p4);
		
		setSize(600, 300);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new JPanelTest();
	}
}

13.5.2 JScrollPane面板 P337
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class JScrollPaneTest extends JFrame {
	public JScrollPaneTest() {
		setTitle("带滚动条的文字编辑器");
		Container c = getContentPane();
		JTextArea ta = new JTextArea(20, 50);//初始文本编辑器的行与列数
		ta.setText("带滚动条的文字编译器");
		JScrollPane sp = new JScrollPane(ta);
		c.add(sp);
		
		setSize(600, 300);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new JScrollPaneTest();
	}
}

//添加了四个JScrollPane
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class JScrollPaneTest extends JFrame {
	public JScrollPaneTest() {
		setTitle("带滚动条的文字编辑器");
		Container c = getContentPane();
		setLayout(new GridLayout(2, 2, 10, 10));
		JTextArea ta1 = new JTextArea(10, 50);
		ta1.setText("带滚动条的文字编译器1");
		JScrollPane sp1 = new JScrollPane(ta1);
		
		JTextArea ta2 = new JTextArea(20, 50);
		ta2.setText("带滚动条的文字编译器2");
		JScrollPane sp2 = new JScrollPane(ta2);
		
		JTextArea ta3 = new JTextArea(30, 50);
		ta3.setText("带滚动条的文字编译器3");
		JScrollPane sp3 = new JScrollPane(ta3);
		
		JTextArea ta4 = new JTextArea(40, 50);
		ta4.setText("带滚动条的文字编译器4");
		JScrollPane sp4 = new JScrollPane(ta4);
		
		c.add(sp1);
		c.add(sp2);
		c.add(sp3);
		c.add(sp4);
		
		setSize(600, 300);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new JScrollPaneTest();
	}
}

13.6.1 提交按钮组件 P338
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class JButtonTest extends JFrame {
	public JButtonTest() {
		Container c = getContentPane();
		setLayout(new GridLayout(3, 2, 5, 5));
		URL url = JButtonTest.class.getResource("imageButton.jpg");
		Icon icon = new ImageIcon(url);
		for (int i = 0; i < 5; ++i) {
			JButton j = new JButton("button" + i, icon);
			c.add(j);
			if (i % 2 == 0) {
				j.setEnabled(false);
			}
		}
		
		JButton jb = new JButton();
		jb.setMaximumSize(new Dimension(90, 30));
		jb.setIcon(icon);
		jb.setHideActionText(true);
		jb.setToolTipText("图片按钮");
		jb.setBorderPainted(false);
		c.add(jb);
		
		setSize(350, 150);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new JButtonTest();
	}
}

13.6.2 单选按钮组件 P339
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

public class JRadioButtonTest extends JFrame {
	public JRadioButtonTest() {
		setTitle("带滚动条的文字编辑器");
		Container c = getContentPane();
		setLayout(new BorderLayout());
		//setLayout(new GridLayout(3, 2, 5, 5));
		
		JRadioButton jr1 = new JRadioButton("radio button 1");
		JRadioButton jr2 = new JRadioButton("radio button 2");
		JRadioButton jr3 = new JRadioButton("radio button 3");
		ButtonGroup group = new ButtonGroup();//需要加到组中，才能在组中做单选操作
		group.add(jr1);
		group.add(jr2);
		group.add(jr3);
		
		c.add(jr1, BorderLayout.NORTH);
		c.add(jr2, BorderLayout.SOUTH);
		c.add(jr3, BorderLayout.CENTER);
		
		setSize(350, 150);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new JRadioButtonTest();
	}
}

13.6.3 复选框组件 P340
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class CheckBoxTest extends JFrame {
	public CheckBoxTest() {
		setTitle("复选框的使用");
		Container c = getContentPane();
		setLayout(new BorderLayout());
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		//panel1.setMinimumSize(new Dimension(300, 300));
		JTextArea ta = new JTextArea(20, 10);
		JScrollPane scrollPanel = new JScrollPane(ta);
		panel1.add(scrollPanel, BorderLayout.CENTER);
		c.add(panel1, BorderLayout.NORTH);
		
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(1, 20, 10));
		//panel2.setMinimumSize(new Dimension(300, 200));
		JCheckBox cb1 = new JCheckBox("1");
		JCheckBox cb2 = new JCheckBox("2");
		JCheckBox cb3 = new JCheckBox("3");
		panel2.add(cb1);
		panel2.add(cb2);
		panel2.add(cb3);
		cb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cb1.isSelected()) {
					ta.append("复选框1被选中\n");
				}
				else {
					ta.append("复选框1被去勾选\n");
				}
			}
		});
		c.add(panel2, BorderLayout.SOUTH);
		
		setSize(300, 500);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new CheckBoxTest();
	}
}

13.7.1 下拉列表框组件 P341

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.AbstractList;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataListener;

public class JComboBoxModelTest extends JFrame {
	//如果不自己实现ComboBoxModel，可以将Array传入来构造JComboBox
	//private String stringArray[] = {"a", "b", "c"};
	//private JComboBox<String> jc = new JComboBox<>(stringArray);
	
	JComboBox<String> jc = new JComboBox<>(new MyComboBox());
	JLabel jl = new JLabel("请选择证件:");
	
	public JComboBoxModelTest() {
		setTitle("在窗口中设置下拉列表框");
		Container c = getContentPane();
		setLayout(new BorderLayout());
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		JTextArea ta = new JTextArea(20, 10);
		JScrollPane scrollPanel = new JScrollPane(ta);
		panel1.add(scrollPanel, BorderLayout.CENTER);
		c.add(panel1, BorderLayout.NORTH);
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(1, 20, 10));
		jc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ta.append((String)jc.getSelectedItem() + "\n");
			}
		});
		panel2.add(jl);
		panel2.add(jc);
		c.add(panel2, BorderLayout.SOUTH);
		
		setSize(300, 500);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new JComboBoxModelTest();
	}
}

class MyComboBox extends AbstractList<String> implements ComboBoxModel<String> {
	String selectedItem = null;
	String test[] = {"身份证", "军人证", "学生证", "工作证"};
	
	@Override
	public String getElementAt(int index) {
		return test[index];
	}
	
	@Override
	public int getSize() {
		return test.length;
	}
	
	@Override
	public void setSelectedItem(Object item) {
		selectedItem = (String) item;
	}
	
	@Override
	public String getSelectedItem() {
		return selectedItem;
	}
	
	public int getIndex() {
		for (int i = 0; i < test.length; i++) {
			if (test[i].equals(getSelectedItem()))
				return i;
		}
		return 0;
	}
	
	@Override
	public void addListDataListener(ListDataListener l) {
		
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		
	}

	@Override
	public String get(int index) {
		return test[index];
	}

	@Override
	public int size() {
		return test.length;
	}
}

13.7.2 列表框组件 P343
//
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JListTest extends JFrame {
	private JList<String> jc = new JList<>(new MyListModel());
	private JLabel jl = new JLabel("请选择证件:");
	
	public JListTest() {
		setTitle("在窗口中设置下拉列表框");
		Container c = getContentPane();
		setLayout(new BorderLayout());
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		JTextArea ta = new JTextArea(20, 10);
		JScrollPane scrollPanel = new JScrollPane(ta);
		panel1.add(scrollPanel, BorderLayout.CENTER);
		c.add(panel1, BorderLayout.WEST);
		
		JScrollPane js = new JScrollPane(jc);//也没有看到滚动条的列表效果
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(1, 20, 10));
		jc.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				ta.append((String) jc.getSelectedValue() + "\n");//每次会把选择的值输出两次，不知道为什么？
			}
		});
		panel2.add(jl);
		panel2.add(js);
		c.add(panel2, BorderLayout.EAST);
		
		setSize(300, 500);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new JListTest();
	}
}

class MyListModel extends AbstractListModel<String> {
	String selectedItem = null;
	String contents[] = {"身份证", "军人证", "学生证", "工作证", "驾驶证", "签证", "老年证"};
	
	@Override
	public String getElementAt(int index) {
		return (index < contents.length) ? contents[index] : null;
	}
	
	@Override
	public int getSize() {
		return contents.length;
	}
}

13.8 文本组件 P345
13.3.1 文本框组件
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class JTextFieldTest extends JFrame {
	public JTextFieldTest() {
		setTitle("在窗口中设置下拉列表框");
		Container c = getContentPane();
		setLayout(new BorderLayout());

		final JTextField jt = new JTextField("aaa", 20);
		
		//换成密码框
		//final JPasswordField jt = new JPasswordField("aaa", 20);
		//jt.setEchoChar('#');
		
		final JButton jb = new JButton("清除");
		jt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				jt.setText("触发事件");
			}
		});
		
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jt.setText("");
				jt.requestFocus();
			}
		});
		
		c.add(jt, BorderLayout.NORTH);
		c.add(jb, BorderLayout.SOUTH);
		
		setSize(300, 200);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new JTextFieldTest();
	}
}

13.8.2 密码框组件 P346
final JPasswordField jt = new JPasswordField("aaa", 20);
jt.setEchoChar('#');

13.8.3 文本域组件 P346
JTextArea jt = new JTextArea("文本域", 6, 6);
jt.setLineWrap(true);

13.8.4 给文本域设置背景图片 P347

13.8.5 给文本区设置背景图片 P348

13.9.1 设置窗体标题框图标 P349
protected void do_button_actionPerformed(ActionEvent e) {
	String resource = "";
	if (e.getSource == button1) {
		resource = "icon1.png";
	}
	if (e.getSource == button2) {
		resource = "icon2.png";
	}
	URL url = getClass().getResource(resource);
	setIconImage(Toolkit.getDefaultToolkit().getImage(url));
}
//------------------------------------------------------------------------------------------------
//Java从入门到精通 第14章 高级处理事件 P355
14.1 键盘事件 P356
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class JTextFieldTest extends JFrame {
	public JTextFieldTest() {
		setTitle("在窗口中设置下拉列表框");
		Container c = getContentPane();
		setLayout(new BorderLayout());

		final JTextArea ja = new JTextArea("aaa", 10, 20);
		ja.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("此次输入的是：" + e.getKeyChar());
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				String keyText = KeyEvent.getKeyText(e.getKeyCode());
				System.out.println("您释放的是：" + keyText);
				System.out.println();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				String keyText = KeyEvent.getKeyText(e.getKeyCode());
				if (e.isActionKey()) { //是否为动作键，capital/break等
					System.out.println("您按下的是动作键：" + keyText);
				} else {
					System.out.println("您按下的是非动作键：" + keyText);
					int keyCode = e.getKeyCode();
					switch (keyCode) {
					case KeyEvent.VK_CONTROL:
						System.out.println("，Ctrl键被按下");
						break;
					case KeyEvent.VK_ALT:
						System.out.println("，Alt键被按下");
						break;
					case KeyEvent.VK_SHIFT:
						System.out.println("，Shift键被按下");
						break;
					}
				}
			}
		});
		
		c.add(ja, BorderLayout.NORTH);
		
		setSize(300, 200);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new JTextFieldTest();
	}
}

14.2 鼠标事件 P357

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class JTextFieldTest extends JFrame {
	public JTextFieldTest() {
		setTitle("在窗口中设置下拉列表框");
		Container c = getContentPane();
		setLayout(new BorderLayout());

		final JTextArea ja = new JTextArea("aaa", 10, 20);
		ja.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("此次输入的是：" + e.getKeyChar());
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				String keyText = KeyEvent.getKeyText(e.getKeyCode());
				System.out.println("您释放的是：" + keyText);
				System.out.println();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				String keyText = KeyEvent.getKeyText(e.getKeyCode());
				if (e.isActionKey()) { //是否为动作键，capital/break等
					System.out.println("您按下的是动作键：" + keyText);
				} else {
					System.out.println("您按下的是非动作键：" + keyText);
					int keyCode = e.getKeyCode();
					switch (keyCode) {
					case KeyEvent.VK_CONTROL:
						System.out.println("，Ctrl键被按下");
						break;
					case KeyEvent.VK_ALT:
						System.out.println("，Alt键被按下");
						break;
					case KeyEvent.VK_SHIFT:
						System.out.println("，Shift键被按下");
						break;
					}
				}
			}
		});
		
		ja.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.print("鼠标按钮被释放，");
				int i = e.getButton();
				if (i == MouseEvent.BUTTON1) {
					System.out.println("释放的是鼠标左键");
				}
				if (i == MouseEvent.BUTTON2) {
					System.out.println("释放的是鼠标滚轮");
				}
				if (i == MouseEvent.BUTTON3) {
					System.out.println("释放的是鼠标右键");
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.print("鼠标按钮被按下，");
				int i = e.getButton();
				if (i == MouseEvent.BUTTON1) {
					System.out.println("按下的是鼠标左键");
				}
				if (i == MouseEvent.BUTTON2) {
					System.out.println("按下的是鼠标滚轮");
				}
				if (i == MouseEvent.BUTTON3) {
					System.out.println("按下的是鼠标右键");
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				System.out.println("光标移出组件");
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				System.out.println("光标移入组件");
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {//如果鼠标移出组件范围，单击事件不会触发
				System.out.print("单击了鼠标按键，");
				int i = e.getButton();
				if (i == MouseEvent.BUTTON1) {
					System.out.println("单击的是鼠标左键");
				}
				if (i == MouseEvent.BUTTON2) {
					System.out.println("单击的是鼠标滚轮");
				}
				if (i == MouseEvent.BUTTON3) {
					System.out.println("单击的是鼠标右键");
				}
				int clickCount = e.getClickCount();
				System.out.println("单击次数为：" + clickCount);//双击鼠标时，第一次单击鼠标将触发一次单击事件
			}
		});
		
		c.add(ja, BorderLayout.NORTH);
		
		setSize(300, 200);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new JTextFieldTest();
	}
}

14.3 窗体事件 P359
14.3.1 捕获窗体焦点变化事件
14.3.2 捕获窗体状态变化事件
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;

public class WindowStateListener_Example extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static void main(String args[]) {
		WindowStateListener_Example frame = new WindowStateListener_Example();
		frame.setVisible(true);
	}
	public WindowStateListener_Example() {
		super();
		// 为窗体添加状态事件监听器
		addWindowStateListener(new MyWindowStateListener());
		setTitle("捕获窗体状态事件");
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	private class MyWindowStateListener implements WindowStateListener {
		public void windowStateChanged(WindowEvent e) {
			int oldState = e.getOldState();// 获得窗体以前的状态
			int newState = e.getNewState();// 获得窗体现在的状态
			String from = "";// 标识窗体以前状态的中文字符串
			String to = "";// 标识窗体现在状态的中文字符串
			switch (oldState) {// 判断窗台以前的状态
				case Frame.NORMAL:// 窗体处于正常化
					from = "正常化";
					break;
				case Frame.MAXIMIZED_BOTH:// 窗体处于最大化
					from = "最大化";
					break;
				default:// 窗体处于最小化
					from = "最小化";
			}
			switch (newState) {// 判断窗台现在的状态
				case Frame.NORMAL:// 窗体处于正常化
					to = "正常化";
					break;
				case Frame.MAXIMIZED_BOTH:// 窗体处于最大化
					to = "最大化";
					break;
				default:// 窗体处于最小化
					to = "最小化";
			}
			System.out.println(from + "——>" + to);
		}
	}
}

14.3.3 捕获其他窗体事件

14.4 选项事件 P363
ComboBox.addItemListener(new ItemListener(){})

14.5 表格模型事件 P364
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class TableModelEvent_Example extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;// 声明一个表格对象
	private DefaultTableModel tableModel;// 声明一个表格模型对象
	private JTextField aTextField;
	private JTextField bTextField;
	public static void main(String args[]) {
		TableModelEvent_Example frame = new TableModelEvent_Example();
		frame.setVisible(true);
	}
	public TableModelEvent_Example() {
		super();
		setTitle("表格模型事件示例");
		setBounds(100, 100, 650, 213);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		String[] columnNames = { "A", "B" };
		String[][] rowValues = { { "A1", "B1" }, { "A2", "B2" },
				{ "A3", "B3" }, { "A4", "B4" } };
		// 创建表格模型对象
		tableModel = new DefaultTableModel(rowValues, columnNames);
		// 为表格模型添加事件监听器
		tableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				int type = e.getType();// 获得事件的类型
				int row = e.getFirstRow() + 1;// 获得触发此次事件的表格行索引
				int column = e.getColumn() + 1;// 获得触发此次事件的表格列索引
				if (type == TableModelEvent.INSERT) {// 判断是否有插入行触发
					System.out.print("此次事件由 插入 行触发，");
					System.out.println("此次插入的是第 " + row + " 行！");
					// 判断是否有修改行触发
				} else if (type == TableModelEvent.UPDATE) {
					System.out.print("此次事件由 修改 行触发，");
					System.out.println("此次修改的是第 " + row + " 行第 " + column
							+ " 列！");
					// 判断是否有删除行触发
				} else if (type == TableModelEvent.DELETE) {
					System.out.print("此次事件由 删除 行触发，");
					System.out.println("此次删除的是第 " + row + " 行！");
				} else {
					System.out.println("此次事件由 其他原因 触发！");
				}
			}
		});
		table = new JTable(tableModel);// 利用表格模型对象创建表格对象，等同于table = new JTable();table.setModel(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		
		
		final JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		final JLabel aLabel = new JLabel("A：");
		panel.add(aLabel);
		
		aTextField = new JTextField(15);
		panel.add(aTextField);
		
		final JLabel bLabel = new JLabel("B：");
		panel.add(bLabel);
		
		bTextField = new JTextField(15);
		panel.add(bTextField);
		
		final JButton addButton = new JButton("添加");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] rowValues = { aTextField.getText(),
						bTextField.getText() };
				tableModel.addRow(rowValues);// 向表格模型中添加一行
				aTextField.setText(null);
				bTextField.setText(null);
			}
		});
		panel.add(addButton);
		
		final JButton updButton = new JButton("修改");
		updButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					tableModel.setValueAt(aTextField.getText(), selectedRow, 0);
					tableModel.setValueAt(bTextField.getText(), selectedRow, 1);
				}
			}
		});
		panel.add(updButton);
		
		final JButton delButton = new JButton("删除");
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 获得表格中的选中行
				int[] selectedRows = table.getSelectedRows();
				for (int row = 0; row < selectedRows.length; row++) {
					// 从表格模型中移除表格中的选中行
					tableModel.removeRow(selectedRows[row] - row);
				}
			}
		});
		panel.add(delButton);
	}
}

14.6 经典范例 P367
14.6.1 经典范例1：模拟相机拍摄
键盘移动操作KeyAdapter
14.6.2 经典范例2：打地鼠游戏
//------------------------------------------------------------------------------------------------
//Java从入门到精通 第15章 多线程 P372
15.2 实现线程的两种方式 P374
15.2.1 继承Thread类
在main()方法中，使线程执行需要调用Thread类中的start()方法，start()方法调用被覆盖的run()方法，如果不调用start()方法，线程永远都不会启动，在主方法没有调用start()方法之前，Thread对象只是一个实例，而不是一个真正的线程。
public class ThreadTest extends Thread {
	private int count = 10;
	private long timeInterval;
	
	public ThreadTest(long timeInterval) {
		this.timeInterval = timeInterval;
	}
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(timeInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.print(count + " ");
			if (--count == 0) {
				return;
			}
		}
	}
	
	public static void main(String args[]) {
		new ThreadTest(500).start();
		new ThreadTest(1000).start();
	}
}

15.2.2 实现Runnable接口
如果需要继承其他类（非Thread类）并使该程序可以使用线程，就需要使用Runnable接口。例如，一个扩展JFrame类的GUI程序不可能再继承Thread类，因为Java语言中不支持多继承，这时该类就需要实现Runnable接口使其具有使用线程的功能。
可以查询API，实质上Thread类就是实现了Runnable接口，其中的run()方法正是对Runnable接口中的run()方法的具体实现。

import java.awt.Container;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;



public class SwingAndThread extends JFrame {
	private JLabel jl = new JLabel();
	private static Thread t;
	private int count = 0;
	private Container container = getContentPane();
	
	public SwingAndThread() {
		setBounds(300, 200, 250, 100);
		container.setLayout(null);
		URL url = SwingAndThread.class.getResource("imageButton.jpg");
		Icon icon = new ImageIcon(url);
		jl.setIcon(icon);
		jl.setHorizontalAlignment(SwingConstants.LEFT);
		jl.setBounds(10, 10, 200, 50);
		jl.setOpaque(true);
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (count <= 200) {
					jl.setBounds(count, 10, 200, 50);
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
					count += 4;
					
					if (count == 200) {
						count = 10;//从0开始能恰好走到200，循环一次从10开始之后，走不到200，就退出while了
					}
				}
				
			}
		});
		t.start();
		container.add(jl);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new SwingAndThread();
	}
}

15.2.3 范例1：查看线程的运行状态 P377

public class ThreadState implements Runnable {
	public synchronized void waitForASecond() throws InterruptedException {
		wait(500);
	}
	
	public synchronized void waitForYears() throws InterruptedException {
		wait();
	}
	
	public synchronized void notifyNow() throws InterruptedException {
		notify();
	}
	
	public void run() {
		try {
			waitForASecond();
			waitForYears();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}


public class Test {
	public static void main(String args[]) {
		try {
			ThreadState state = new ThreadState();
			Thread thread = new Thread(state);
			System.out.println("New thread: " + thread.getState());

			thread.start();
			System.out.println("Start thread: " + thread.getState());

			Thread.sleep(100);
			System.out.println("Wait for timer: " + thread.getState());

			Thread.sleep(1000);
			System.out.println("Wait for thread: " + thread.getState());

			state.notifyNow();
			System.out.println("Wake thread: " + thread.getState());

			Thread.sleep(1000);
			System.out.println("Stop thread: " + thread.getState());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

15.2.4 范例2：查看JVM中的线程名

import java.util.ArrayList;
import java.util.List;

public class ThreadList {
	private static ThreadGroup getRootThreadGroups() {
		ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
		ThreadGroup lastGroup = rootGroup;
		while (true) {
			if (rootGroup != null && rootGroup.getClass() != null) {
				rootGroup = rootGroup.getParent();
			}
			
			if (rootGroup != null) {
				lastGroup = rootGroup;
			}
			else {
				break;
			}
			
		}
		return lastGroup;
	}
	
	public static List<String> getThreads(ThreadGroup group) {
		List<String> threadList = new ArrayList<String>();
		Thread threads[] = new Thread[group.activeCount()];//5个活动线程
		int count = group.enumerate(threads, false);//这里却复制了4个活动线程到threads中
		for (int i = 0; i < count; ++i) {
			threadList.add(group.getName() + "线程组：" + threads[i].getName());
		}
		return threadList;
	}
	
	public static List<String> getThreadGroups(ThreadGroup group) {
		List<String> threadList = getThreads(group);
		ThreadGroup groups[] = new ThreadGroup[group.activeGroupCount()];//1个活动线程组
		int count = group.enumerate(groups, false);
		for (int i = 0; i < count; ++i) {
			threadList.addAll(getThreads(groups[i]));
		}
		return threadList;
	}
	
	public static void main(String args[]) {
		for (String string : getThreadGroups(getRootThreadGroups())) {
			System.out.println(string);
		}
	}
}
输出如下：
system线程组：Reference Handler
system线程组：Finalizer
system线程组：Signal Dispatcher
system线程组：Attach Listener
main线程组：main

15.3 线程的状态
虽然多线程看起来像同时执行，但事实上在同一时间点上只有一个线程被执行，只是线程之间切换较快，所以会使人产生线程是同时进行的假象。

15.4 操作线程的方法 P382
15.4.1 线程的休眠 
Thread.sleep(mills);
使用了sleep()方法的线程在一段时间内会醒来，但是并不能保证它醒来后进入运行状态，只能保证它进入就绪状态。

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;

public class SleepMethodTest extends JFrame {
	private Thread t;
	
	private static Color color[] = {Color.BLACK, Color.BLUE, Color.CYAN, Color.GREEN, Color.ORANGE, Color.YELLOW, Color.RED, 
		Color.PINK, Color.LIGHT_GRAY};
	
	private static final Random rand = new Random();
	
	private static Color getC() {
		return color[rand.nextInt(color.length)];
	}
	
	public SleepMethodTest() {
		t = new Thread(new Runnable() {
			int x = 30;
			int y = 50;
			
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					Graphics graphics = getGraphics();
					graphics.setColor(getC());
					graphics.drawLine(x, y, 100, y++);//绘制直线并递增垂直坐标
					
					if (y >= 80) {
						y = 50;
					}
				}
			}
		});
		
		t.start();
	}
	
	public static void init(JFrame frame, int width, int height) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setVisible(true);
	}
	
	public static void main(String args[]) {
		init(new SleepMethodTest(), 100, 100);
	}
}

15.4.2 线程的加入 P383
join()
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class JoinTest extends JFrame {
	private Thread threadA;
	private Thread threadB;
	
	private final JProgressBar progressBar1 = new JProgressBar();
	private final JProgressBar progressBar2 = new JProgressBar();
	
	private int count = 0;
	
	public JoinTest() {
		//super();//不写会自动调用
		
		getContentPane().add(progressBar1, BorderLayout.NORTH);
		getContentPane().add(progressBar2, BorderLayout.SOUTH);
		
		progressBar1.setStringPainted(true);
		progressBar2.setStringPainted(true);
		
		threadA = new Thread(new Runnable() {
			int count = 0;
			
			@Override
			public void run() {
				while (true) {
					progressBar1.setValue(++count);
					try {
						Thread.sleep(10);
						threadB.join();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		threadA.start();
		
		threadB = new Thread(new Runnable() {
			int count = 0;
			
			@Override
			public void run() {
				while (true) {
					progressBar2.setValue(++count);
					try {
						Thread.sleep(10);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if (count == 100) {
						break;
					}
				}
			}
		});
		threadB.start();
	}
	
	public static void init(JFrame frame, int width, int height) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setVisible(true);
	}
	
	public static void main(String args[]) {
		init(new JoinTest(), 100, 100);
	}
}

15.4.3 线程的中断 P385
提供在run()方法中使用无限循环的形式，然后使用一个布尔型标记循环的停止。

如果线程是因为使用了sleep()或wait()方法进入了就绪状态，这时可以使用Thread类的interrupt()方法使线程离开run()方法，同时结束线程，但是程序会抛出InterruptedException异常。

public class InterrupedTest implements Runnable {
	private boolean isContinue = true;
	public void run() {
		while (true) {
			//...
			if (isContinue) {
				break;
			}
		}
	}
	public void setContinue() {
		this.isContinue = true;
	}
}
//参考下例，用两个按钮来进行isContinue变量的设置
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class TestThread extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel jPanel1 = new JPanel();
	JButton startButton = new JButton();
	JButton stopButton = new JButton();
	MyThread thread = null;
	private boolean isContinue;
	public TestThread() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jbInit() throws Exception {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startButton.setText("start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startButton_actionPerformed(e);
			}
		});
		stopButton.setText("stop");
		stopButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopButton_actionPerformed(e);
			}
		});
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		jPanel1.add(startButton);
		startButton.setBounds(36, 105, 82, 30);
		jPanel1.add(stopButton);
		stopButton.setBounds(160, 108, 100, 31);
	}
	
	void startButton_actionPerformed(ActionEvent e) {
		if (thread != null) {
			isContinue = true;
		}
		else {
			thread = new MyThread();
			thread.start();
		}
	}
	
	void stopButton_actionPerformed(ActionEvent e) {
		if (thread != null) {
			isContinue = false;
		}
		//thread = null;
	}
	
	public static void main(String[] args) {
		TestThread test = new TestThread();
		test.setBounds(300,300,300, 80);
		test.setVisible(true);
	}
	
	private class MyThread extends Thread {
		public MyThread() {
			isContinue = true;
		}
		
		public void run() {
			System.out.println("\n\n");
			while (true) {
				if (isContinue) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Java 编程词典\thttp://www.mrbccd.com");
				}
			}
		}
	}
}



import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class InterruptedSwing extends JFrame {
	private Thread threadA;
	
	public InterruptedSwing() {
		//super();
		final JProgressBar progressBar1 = new JProgressBar();
		getContentPane().add(progressBar1, BorderLayout.NORTH);
		
		progressBar1.setStringPainted(true);
		
		threadA = new Thread(new Runnable() {
			int count = 0;
			
			@Override
			public void run() {
				while (true) {
					progressBar1.setValue(++count);
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("当前线程被中断");
						System.out.println("Run exception");
					}
				}
			}
		});
		threadA.start();
		
		try {
			Thread.sleep(10000);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Contructor exception");
		}
		
		threadA.interrupt();
	}
	
	public static void init(JFrame frame, int width, int height) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setVisible(true);
	}
	
	public static void main(String args[]) {
		init(new InterruptedSwing(), 100, 100);
	}
}
输出如下：
java.lang.InterruptedException: sleep interrupted
	at java.lang.Thread.sleep(Native Method)
	at InterruptedSwing$1.run(InterruptedSwing.java:24)
	at java.lang.Thread.run(Unknown Source)
当前线程被中断
Run exception

15.4.4 线程的礼让 P386
使用yield()方法，给当前正处于运行状态下的线程一个提醒，告知它可以将资源礼让给其他线程，但这仅仅是一种暗示，没有任何一种机制保证当前线程会将资源礼让。
yield()方法使具有同样优先级的线程有进入可执行状态的机会，当当前线程放弃执行权时会再度回到就绪状态。对于支持多任务的操作系统来说，不需要调用yield()方法，因为操作系统会为线程自动分配CPU时间片来执行。

15.4.5 查看和修改线程的优先级 P386


15.4.6 休眠当前线程 P388

15.5 线程的优先级 P389
import java.awt.*;

import javax.swing.*;

public class PriorityTest extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Thread threadA;
	private Thread threadB;
	private Thread threadC;
	private Thread threadD;
	
	public PriorityTest() {
		getContentPane().setLayout(new GridLayout(4, 1));
		// 分别实例化4个线程
		final JProgressBar progressBar = new JProgressBar();
		final JProgressBar progressBar2 = new JProgressBar();
		final JProgressBar progressBar3 = new JProgressBar();
		final JProgressBar progressBar4 = new JProgressBar();
		getContentPane().add(progressBar);
		getContentPane().add(progressBar2);
		getContentPane().add(progressBar3);
		getContentPane().add(progressBar4);
		progressBar.setStringPainted(true);
		progressBar2.setStringPainted(true);
		progressBar3.setStringPainted(true);
		progressBar4.setStringPainted(true);
		threadA = new Thread(new MyThread(progressBar));
		threadB = new Thread(new MyThread(progressBar2));
		threadC = new Thread(new MyThread(progressBar3));
		threadD = new Thread(new MyThread(progressBar4));
		setPriority("threadA", 5, threadA);
		setPriority("threadB", 5, threadB);
		setPriority("threadC", 4, threadC);
		setPriority("threadD", 3, threadD);
	}
	
	// 定义设置线程的名称、优先级的方法
	public static void setPriority(String threadName, int priority,
			Thread t) {
		t.setPriority(priority); // 设置线程的优先级
		t.setName(threadName); // 设置线程的名称
		t.start(); // 启动线程
	}
	
	public static void main(String[] args) {
		init(new PriorityTest(), 100, 100);
	}
	
	public static void init(JFrame frame, int width, int height) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setVisible(true);
	}
	
	private final class MyThread implements Runnable { // 定义一个实现Runnable接口的类
		private final JProgressBar bar;
		int count = 0;
		
		private MyThread(JProgressBar bar) {
			this.bar = bar;
		}
		
		public void run() { // 重写run()方法
			while (true) {
				bar.setValue(count += 10); // 设置滚动条的值每次自增10
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("当前线程序被中断");
				}
			}
		}
	}
	
}

15.6 线程同步 P391
编写多线程程序时，应该考虑到线程安全问题。实质上线程安全问题来源于两个线程同时存取单一对象的数据。
//有问题的是这些线程都没有正常退出？
public class ThreadSafeTest implements Runnable {
	int num = 10;
	
	public void run() {
		while (true) {
			if (num > 0) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("车票还有 " + num-- + "张");
			}
		}
	}
	public static void main(String[] args) {
		ThreadSafeTest t = new ThreadSafeTest();
		Thread tA = new Thread(t);
		Thread tB = new Thread(t);
		Thread tC = new Thread(t);
		Thread tD = new Thread(t);
		tA.start();
		tB.start();
		tC.start();
		tD.start();
	}
}
输出如下：
车票还有 10张
车票还有 9张
车票还有 8张
车票还有 7张
车票还有 6张
车票还有 5张
车票还有 4张
车票还有 3张
车票还有 2张
车票还有 1张
车票还有 0张
车票还有 -1张
车票还有 -2张


15.6.2 线程同步机制 P392
1. 同步块
Java中提供了同步机制，可以有效地防止资源冲突，同步机制使用synchronized关键字。

public class ThreadSafeTest implements Runnable {
	int num = 10;
	
	public void run() {
		while (true) {
			synchronized ("") {
				if (num > 0) {
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("车票还有 " + num-- + "张");
				}
			}
		}
	}
	public static void main(String[] args) {
		ThreadSafeTest t = new ThreadSafeTest();
		Thread tA = new Thread(t);
		Thread tB = new Thread(t);
		Thread tC = new Thread(t);
		Thread tD = new Thread(t);
		tA.start();
		tB.start();
		tC.start();
		tD.start();
	}
}
打印到最后没有出现负数，这是因为将资源放置在了同步块中。这个同步块也被称为临界区，它使用synchronized关键字，语法格式如下：
synchronized(Object) {
	//...
}
通常将共享资源的操作放置在synchronized定义的区域中，当其他线程也获取到这个锁时，必须等待锁被释放时才能进入该区域。Object为任意一个对象，每个对象都存在一个标志位，并具有两个值，分别为0和1。一个线程运行到同步块时首先检查该对象的标志位，如果为0状态，表明此同步块中存在其他线程在运行。这时该线程就处于就绪状态，直到处于同步块中的线程执行完同步块中的代码为止。这时该对象的标识位被设置为1，该线程才能执行同步块中的代码，并将Object对象的标识位设置为0，防止其他线程执行同步块中的代码。

2. 同步方法
在方法前面修饰synchronzied关键字的方法，其语法格式如下：
synchronized void f() {}





public class ThreadSafeTest implements Runnable {
	int num = 10;
	
	public void run() {
		while (true) {
			//增加如下语句，如果票数为0，则退出，否则所有线程会一直等待资源，无法结束退出
			if (num == 0) {
				System.out.println("Thread exit");
				break;
			}
			
			sellTicket();
		}
	}
	
	private synchronized void sellTicket() {
		if (num > 0) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("车票还有 " + num-- + "张");
		}

	}
	
	public static void main(String[] args) {
		ThreadSafeTest t = new ThreadSafeTest();
		Thread tA = new Thread(t);
		Thread tB = new Thread(t);
		Thread tC = new Thread(t);
		Thread tD = new Thread(t);
		tA.start();
		tB.start();
		tC.start();
		tD.start();
	}
}
输出如下：
车票还有 10张
车票还有 9张
车票还有 8张
车票还有 7张
车票还有 6张
车票还有 5张
车票还有 4张
车票还有 3张
车票还有 2张
车票还有 1张
Thread exit
Thread exit
Thread exit
Thread exit

15.7 线程间的通信 P394
从同步的角度来说，调用sleep()方法的线程不释放锁，但调用wait()方法的线程释放锁。
wait()与notify()、notifyAll()方法只能在同步块或同步方法中使用。

//还是有些问题，有时候会走不下去
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JProgressBar;


public class Communicate extends JFrame {
	private Thread t1;
	private Thread t2;
	private final JProgressBar progressBar = new JProgressBar();
	private int count = 0;

	public Communicate() {
		getContentPane().add(progressBar, BorderLayout.NORTH);
		progressBar.setStringPainted(true);
		
		deValue();
		addValue();
		t1.start();
		
		try {
			Thread.currentThread().sleep(1000);//使当前线程休眠1000毫秒
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		t2.start();
	}
	
	private void addValue() {
		t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {				
					if (count >= 100) {
						System.out.println("进度条已满，递增线程等待");
					}
					
					if (count == 0) {
						progressBar.setValue(count += 100);
						System.out.println("进度条的当前值为：" + count);
						
						//确保通知t2时，t2线程已经启动
						try {
							Thread.currentThread().sleep(5000);//使当前线程休眠100毫秒
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						synchronized (t2) { //这里为什么要用t2？
							System.out.println("进度条已有值，可以进行递减操作");
							t2.notify();
						}
					}
					
					try {
						Thread.currentThread().sleep(500);//使当前线程休眠100毫秒
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
	}

	private void deValue() {
		t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (count == 0) {
						synchronized (this) {
							try {
								wait();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					
					count -= 20; 
					progressBar.setValue(count);
					System.out.println("进度条当前值为：" + count);
					
					try {
						Thread.currentThread().sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public static void init(JFrame frame, int width, int height) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		init(new Communicate(), 150, 100);
	}
}

15.8.1 线程的插队运行 P396

public class EmergencyThread implements Runnable {
	@Override
	public void run() {
		for (int i = 1; i < 6; ++i) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("紧急情况：" + i + "号车出发！");
		}
	}
}


public class EmergencyThread implements Runnable {
	@Override
	public void run() {
		for (int i = 1; i < 6; ++i) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("紧急情况：" + i + "号车出发！");
		}
	}
}
输出如下：（不唯一）
正常情况：1号车出发！
紧急情况：1号车出发！
紧急情况：2号车出发！
紧急情况：3号车出发！
紧急情况：4号车出发！
紧急情况：5号车出发！
正常情况：2号车出发！
正常情况：3号车出发！
正常情况：4号车出发！
正常情况：5号车出发！
//------------------------------------------------------------------------------------------------
//Java从入门到精通 第16章 网络通信 P400
TCP与UDP协议 P402
TCP协议是一种以固接连线为基础的黑方，它提供两台计算机间可选的数据传送。
UDP是无连接通信协议，不保证可选数据的传输，但能够向若干个目标发送数据，接收发自若干个源的数据。
一些防火墙和路由器会设置成不允许UDP数据包传输，因此若遇到UDP连接方面的问题，应先确定是否允许UDP协议。

16.1.3 端口和套接字 P402
端口被规定为一个在0~65535 之间的整数。HTTP服务一般使用80端口，FTP服务使用21端口。
通常0~1023 之间的端口数用于一些知名的网络服务和应用，用户的普通网络应用程序应该使用1024以上的端口数，以避免端口号与另一个应用或系统服务所用端口冲突。
网络程序中套接字（Socket）用于将应用程序与端口连接起来。


import java.net.InetAddress;
import java.net.UnknownHostException;


public class Address {
	public static void main(String args[]) {
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			String localName = ip.getHostName();
			String localIp = ip.getHostAddress();
			System.out.println("本地主机名：" + localName);
			System.out.println("本机IP地址：" + localIp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
输出如下：
本地主机名：Ben-PC
本机IP地址：192.168.1.106

16.2.2 ServerSocket类 P407

16.2.3 TCP网络程序 P408
//服务器端程序
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;


public class MyTcp {
	private BufferedReader reader;
	private ServerSocket server;
	private Socket socket;
	
	void getServer() {
		try {
			server = new ServerSocket(8998);
			server.setSoTimeout(5000);//设置超时时长
			System.out.println("服务器套接字已经创建成功");
			while (true) {
				System.out.println("等待客户机的连接");
				socket = server.accept();
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				getClientMessage();
			}
		} catch (SocketTimeoutException e) {
			System.out.println("连接超时...");
			JOptionPane.showMessageDialog(null, "连接超时...");//可以直接弹出提示面板
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getClientMessage() {
		try {
			while (true) {
				System.out.println("客户机：" + reader.readLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if (reader != null) {
				reader.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		MyTcp tcp = new MyTcp();
		tcp.getServer();
	}
}

输出如下：
服务器套接字已经创建成功
等待客户机的连接

本实例是服务器端程序，当本实例运行后，如果在服务器没有停止运行的情况下再次运行本实例将发生异常，这是由于服务器程序使用的端口号已经被占用，所以在服务器程序没有停止的情况下再次运行就发生了异常。

//客户端程序
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class MyClient extends JFrame {
	private PrintWriter writer;
	Socket socket;
	private JTextArea ta = new JTextArea();
	private JTextField tf = new JTextField();
	Container cc;
	
	public MyClient(String title) {
		super(title);
		cc = this.getContentPane();
		cc.add(ta, "North");
		cc.add(tf, "South");
		tf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputText = tf.getText();
				//如果是info，则显示客户端和服务器的IP及端口号
				if (inputText.equals("info")) { //字符串判断相等需要用euqals
					InetAddress netAddress = socket.getInetAddress();
					String netIp = netAddress.getHostAddress();
					int netPort = socket.getPort();
					
					InetAddress localAddress = socket.getLocalAddress();
					String localIp = localAddress.getHostAddress();
					int localPort = socket.getLocalPort();
					
					System.out.print("远程服务器的IP地址：" + netIp + "\n");
					System.out.print("远程服务器的端口号：" + netPort + "\n");
					System.out.print("客户机本地的IP地址：" + localIp + "\n");
					System.out.print("客户机本地的端口号：" + localPort + "\n");
//					ta.append("远程服务器的IP地址：" + netIp + "\n");
//					ta.append("远程服务器的端口号：" + netPort + "\n");
//					ta.append("客户机本地的IP地址：" + localIp + "\n");
//					ta.append("客户机本地的端口号：" + localPort + "\n");
					
				} else {
					writer.println(tf.getText());//将socket输出流中的内容输出到服务器端
					ta.append(tf.getText() + '\n');
				}
				tf.setText("");	
			}
		});
	}
	
	private void connect() {
		ta.append("尝试连接\n");
		try {
			socket = new Socket("192.168.1.106", 8998);
			writer = new PrintWriter(socket.getOutputStream(), true);//创建socket输出流的打印对象
			ta.append("完成连接\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		MyClient client = new MyClient("向服务器发送数据");
		client.setBounds(300, 260, 340, 220);
		client.setVisible(true);
		client.connect();
	}
}

为了使本实例能够正常运行，必须先运行16.3 的服务器端程序，并且实例的端口号要与例16.3 服务器端程序套接字的端口号一致，否则程序会发生错误。
当一台机器上安装了多个网络应用程序时，很可能指定的端口号已被占用，有时可能还会遇到以前很好的网络程序突然运行不起来了，这种情况很可能也是由于端口被其他程序占用了。此时可以运行netstat -help来获得帮助，使用命令netstat -an来查看该程序所使用的端口。P411

16.3 UDP程序设计基础 P413
DatagramSocket()
DatagramSocket(int port)
DatagramSocket(int port, InetAddress addr)
在接收程序时，必须指定一个端口号，不要让系统随机产生。可以使用第二种构造函数。在发送程序时，通常使用第一种构造函数，不指定端口号，这样系统就会为我们分配一个端口号。

//广播主机程序不断向外发出广播信息
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Weather extends Thread {
	String weather = "节目预报：八点有大型晚会，请收听";
	int port = 9898;
	InetAddress iAddress = null;
	MulticastSocket socket = null;
	public Weather() {
		try {
			iAddress = InetAddress.getByName("224.255.10.0");
			socket = new MulticastSocket(port);
			socket.setTimeToLive(1);
			socket.joinGroup(iAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			DatagramPacket packet = null;
			byte data[] = weather.getBytes();
			packet = new DatagramPacket(data, data.length, iAddress, port);
			System.out.println(new String(data));
			try {
				socket.send(packet);
				sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {
		Weather w = new Weather();
		w.start();
	}
}

//接收程序
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class Receive extends JFrame implements Runnable, ActionListener {
	int port;
	InetAddress group = null;
	MulticastSocket socket = null;
	JButton start = new JButton("开始接收");
	JButton stop = new JButton("停止接收");
	JTextArea inceAr = new JTextArea(10, 10);
	JTextArea inced = new JTextArea(10, 10);
	Thread thread;
	boolean b = false;
	
	public Receive() {
		super("广播数据报");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		thread = new Thread(this);
		start.addActionListener(this);
		stop.addActionListener(this);
		
		inceAr.setForeground(Color.blue);
		JPanel north = new JPanel();
		north.add(start);
		north.add(stop);
		add(north, BorderLayout.NORTH);
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(1, 2));
		center.add(inceAr);
		center.add(inced);
		add(center, BorderLayout.CENTER);
		
		validate();//刷新
		port = 9898;
		
		try {
			group = InetAddress.getByName("224.225.10.0");
			socket = new MulticastSocket(port);
			socket.joinGroup(group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setBounds(100, 50, 360, 380);
		setVisible(true);
	}
	
	public void run() {
		while (true) {
			byte data[] = new byte[1024];
			DatagramPacket packet = null;
			packet = new DatagramPacket(data, data.length, group, port);
			try {
				socket.setSoTimeout(2000);
				socket.receive(packet);
				String message = new String(packet.getData(), 0, packet.getLength());
				inceAr.setText("正在接收的内容：\n" + message);
				inced.append(message + "\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (b == true) {
				break;
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start) {
			start.setBackground(Color.red);
			stop.setBackground(Color.yellow);
			if (!(thread.isAlive())) {
				thread = new Thread(this);
			}
			thread.start();
			b = false;
		}
		
		if (e.getSource() == stop) {
			start.setBackground(Color.yellow);
			stop.setBackground(Color.red);
			b = true;
		}
	}
	
	public static void main(String args[]) {
		Receive rec = new Receive();
		rec.setSize(460, 200);
	}
}
一直没有收到，不知道为什么？？？

16.4.1 聊天室服务端 P418
16.4.2 聊天室客户端 P419
//------------------------------------------------------------------------------------------------
//Java从入门到精通 第17章 JDBC操作数据库 P424
没看

//------------------------------------------------------------------------------------------------
//Java从入门到精通 第18章 Swing高级组件 P452

18.1.1 创建表格 P453
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ExampleFrame_01 extends JFrame {
	public static void main(String args[]) {
		ExampleFrame_01 frame = new ExampleFrame_01();
		frame.setVisible(true);
	}
	
	public ExampleFrame_01() {
		super();
		setTitle("创建可以滚动的表格");
		setBounds(100, 100, 240, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String columnNames[] = {"A", "B"};
		String tableValues[][] = {{"A1", "B1"}, {"A2", "B2"}, {"A3", "B3"}, {"A4", "B4"}, {"A5", "B5"}};
		JTable table = new JTable(tableValues, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
}

//直接添加到容器中 P454
import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

public class ExampleFrame_01 extends JFrame {
	public static void main(String args[]) {
		ExampleFrame_01 frame = new ExampleFrame_01();
		frame.setVisible(true);
	}
	
	public ExampleFrame_01() {
		super();
		setTitle("创建可以滚动的表格");
		setBounds(100, 100, 240, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Vector<String> columnNameV = new Vector<>();
		columnNameV.add("A");
		columnNameV.add("B");
		columnNameV.add("C");
		
		Vector<Vector<String>> tableValueV = new Vector<>();
		for (int row = 1; row < 6; ++row) {
			Vector<String> rowV = new Vector<>();
			rowV.add("A" + row);
			rowV.add("B" + row);
			rowV.add("C" + row);
			tableValueV.add(rowV);
		}
		JTable table = new JTable(tableValueV, columnNameV);
		getContentPane().add(table, BorderLayout.CENTER);
		JTableHeader tableHeader = table.getTableHeader();
		getContentPane().add(tableHeader, BorderLayout.NORTH);//没有这句，则不会显示出标题
		
		tableHeader.setReorderingAllowed(true);//可以设置列是否可以拖动重排
	}
}

//通过定制自己的表格，重载特定的方法实现表格的特定功能 P455
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class ExampleFrame_01 extends JFrame {
	public static void main(String args[]) {
		ExampleFrame_01 frame = new ExampleFrame_01();
		frame.setVisible(true);
	}
	
	public ExampleFrame_01() {
		super();
		setTitle("创建可以滚动的表格");
		setBounds(100, 100, 240, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		
		Vector<String> columnNameV = new Vector<>();
		columnNameV.add("A");
		columnNameV.add("B");
		columnNameV.add("C");
		
		Vector<Vector<String>> tableValueV = new Vector<>();
		for (int row = 1; row < 6; ++row) {
			Vector<String> rowV = new Vector<>();
			rowV.add("A" + row);
			rowV.add("B" + row);
			rowV.add("C" + row);
			tableValueV.add(rowV);
		}
		JTable table = new MyTable(tableValueV, columnNameV);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setSelectionBackground(Color.YELLOW);
		table.setSelectionForeground(Color.RED);
		table.setRowHeight(30);
		scrollPane.setViewportView(table);
	}
	
	class MyTable extends JTable {
		public MyTable(Vector<Vector<String>> rowData, Vector<String> columnNames) {
			super(rowData, columnNames);
		}
		
		@Override
		public JTableHeader getTableHeader() {
			JTableHeader tableHeader = super.getTableHeader();
			tableHeader.setReorderingAllowed(false);
			DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
			hr.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
			return tableHeader;
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
		
		@Override
		public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
			DefaultTableCellRenderer cr = (DefaultTableCellRenderer) super.getDefaultRenderer(columnClass);
			cr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
			return cr;
		}
	}
}

18.1.3 操纵表格 P458
获得表格的各种信息，如总行数、总列数，选定的行和列等

18.1.4 范例1：列表元素与提示信息 P460 范例2：监听列表单击事件
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TipFrame extends JFrame {
	private JScrollPane scrollPane = null;
	private JLabel label = null;
	
	public TipFrame() {
		setTitle("带有提示信息的列表");
		setBounds(100, 100, 500, 375);
		Container c = getContentPane();
		scrollPane = new JScrollPane();
		c.add(scrollPane, BorderLayout.NORTH);
		
		label = new JLabel();
		label.setFont(new Font("Arial", Font.BOLD, 15));
		c.add(label, BorderLayout.SOUTH);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				String data[][] = new String[][] {
						{"book1", "Edition1"},
						{"book2", "Edition2"},
						{"book3", "Edition3"},
						{"book4", "Edition4"}};
				
				JList<Object> list = new ToolTipList(data);
				list.setFont(new Font("微软雅黑", Font.PLAIN, 16));
				scrollPane.setViewportView(list);
				list.addListSelectionListener(new ListSelectionListener() {
					
					@Override
					public void valueChanged(ListSelectionEvent e) {
						label.setText("Thanks for buying " + list.getSelectedValue());
					}
				});
			}
		});
	}
	
	public static void main(String args[]) {
		new TipFrame();
	}
}


class ToolTipList extends JList<Object> {
	private static final long serialVersionUID = 3396510300808442967L;
	private Object[][] data;
	public ToolTipList(Object[][] data) {
		this.data = data;
		Object[] listData = new Object[data.length];
		for (int i = 0; i < listData.length; ++i) {
			listData[i] = data[i][0];
		}
		setListData(listData);
	}
	
	@Override
	public String getToolTipText(MouseEvent event) {
		int index = locationToIndex(event.getPoint());
		if (index > -1) {
			return "<html><font face=微软雅黑 size=16 color=red>" + data[index][1] + "</font></html>";
		} else {
			return super.getToolTipText(event);
		}
	}
}

18.2.1 利用表格模型创建表格 P462

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ExampleFrame_01 extends JFrame {
	public static void main(String args[]) {
		ExampleFrame_01 frame = new ExampleFrame_01();
		frame.setVisible(true);
	}
	
	public ExampleFrame_01() {
		super();
		setTitle("创建可以滚动的表格");
		setBounds(100, 100, 240, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String columnNames[] = {"A", "B"};
		String tableValues[][] = {{"A1", "B1"}, {"A2", "B2"}, {"A3", "B3"}, {"A4", "B4"}, {"A5", "B5"}};
		
		DefaultTableModel tableModel = new DefaultTableModel(tableValues, columnNames);
		JTable table = new JTable(tableModel);
		table.setRowSorter(new TableRowSorter<>(tableModel));
		//JScrollPane scrollPane = new JScrollPane(table);//等于下面两句
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
}

18.2.2 维护表格模型 P464
见 14.5 表格模型事件 P364

18.2.3 范例3：实现自动排序的列表 P466
import java.awt.*;
import java.util.TreeSet;

import javax.swing.*;

public class SortedListModelTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static void main(String args[]) {
		new SortedListModelTest();
	}
	
	public SortedListModelTest() {
		setTitle("自动排序的列表");
		setBounds(100, 100, 500, 375);
		Container c = getContentPane();
		JScrollPane scrollPane = new JScrollPane();
		c.add(scrollPane, BorderLayout.NORTH);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SortedListModel model = new SortedListModel();
		model.add("a");
		model.add("f");
		model.add("c");
		model.add("d");
		model.add("e");
		model.add("b");
		
		JList<Object> list = new JList<>(model);
		scrollPane.setViewportView(list);
	}
}

class SortedListModel extends AbstractListModel<Object> {
	private static final long serialVersionUID = 3396510300808442967L;
	private TreeSet<Object> model = new TreeSet<Object>();
	
	@Override
	public Object getElementAt(int index) {
		return model.toArray()[index];
	}

	@Override
	public int getSize() {
		return model.size();
	}
	
	public void add(Object element) {
		if (model.add(element)) {
			fireContentsChanged(this, 0, getSize());//AbstractListModel 子类必须在列表的一个或多个元素发生更改之后调用此方法
		}
	}
}

18.2.4 范例4：可以预览字体的列表 P467
import java.awt.*;

import javax.swing.*;

public class FontTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static void main(String args[]) {
		new FontTest();
	}
	
	public FontTest() {
		setTitle("自动排序的列表");
		setBounds(100, 100, 500, 700);
		Container c = getContentPane();
		JScrollPane scrollPane = new JScrollPane();
		c.add(scrollPane, BorderLayout.NORTH);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String fontNames[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		DefaultListModel<Object> model = new DefaultListModel<>();
//		for (String fontName : fontNames) {
//			model.addElement(new Font(fontName, Font.PLAIN, 24));
//		}
		
		for (int i = 0; i < 10; ++i) {
			model.addElement(new Font(fontNames[i], Font.PLAIN, 24));
		}
		
		JList<Object> list = new JList<>(model);
		ListCellRenderer<Object> renderer = new FontListCellRenderer();
		list.setCellRenderer(renderer);
		scrollPane.setViewportView(list);
		
		revalidate();//需要调用面板的重新刷新操作，否则无法看到生成的字体
	}
}

class FontListCellRenderer implements ListCellRenderer<Object> {
	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
		JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		Font font = (Font) value;
		renderer.setFont(font);
		renderer.setText(font.getFontName());
		return renderer;
	}
}

18.3.2 范例5：提供行标题栏的表格 P469

18.4 Swing树组件 P472
18.4.1 简单的树
左侧的树是直接创建的，中间的树是采用默认方式判断节点的，这两个树中名称为“一级节点B”的节点图标均为叶子节点图标，右侧的树是采用非默认方式判断节点的，该树中名称为“一级子节点B”的节点图标均为非叶子节点图标。
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TreeTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public TreeTest() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("根节点");
		DefaultMutableTreeNode nodeFirst = new DefaultMutableTreeNode("一级子节点A");
		root.add(nodeFirst);
		
		DefaultMutableTreeNode nodeSecond = new DefaultMutableTreeNode("二级子节点", false);
		nodeFirst.add(nodeSecond);
		
		root.add(new DefaultMutableTreeNode("一级子节点B"));
		JTree treeRoot = new JTree(root);
		getContentPane().add(treeRoot, BorderLayout.WEST);
		
		
		DefaultTreeModel treeModelDefault = new DefaultTreeModel(root);
		JTree treeDefault = new JTree(treeModelDefault);
		getContentPane().add(treeDefault, BorderLayout.CENTER);
		
		
		DefaultTreeModel treeModelPointed = new DefaultTreeModel(root, true);
		JTree treePointed = new JTree(treeModelPointed);
		getContentPane().add(treePointed, BorderLayout.EAST);
		
		
		setSize(500, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new TreeTest();
	}
}

18.4.2 处理选中节点事件 P473


import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


public class TreeTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public TreeTest() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("根节点");
		DefaultMutableTreeNode nodeFirst = new DefaultMutableTreeNode("一级子节点A");
		root.add(nodeFirst);
		
		DefaultMutableTreeNode nodeSecond = new DefaultMutableTreeNode("二级子节点", false);
		nodeFirst.add(nodeSecond);
		
		root.add(new DefaultMutableTreeNode("一级子节点B"));
		JTree treeRoot = new JTree(root);
		getContentPane().add(treeRoot, BorderLayout.CENTER);
		
		TreeSelectionModel treeSelectionModel = treeRoot.getSelectionModel();
		treeSelectionModel.setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
		treeRoot.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if (!treeRoot.isSelectionEmpty()) {
					TreePath selectionPaths[] = treeRoot.getSelectionPaths();
					for (int i = 0; i < selectionPaths.length; i++) {
						TreePath treePath = selectionPaths[i];
						Object path[] = treePath.getPath();
						for (int j = 0; j < path.length; j++) {
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) path[j];
							System.out.print(node.getUserObject() + (j == (path.length - 1) ? "" : "-->"));
						}
						System.out.println();
					}
					System.out.println();
				}
			}
		});
		
		
		setSize(500, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new TreeTest();
	}
}


18.4.3 遍历树节点 P475
前序遍历：根-左-右
中序遍历：左-根-右
后序遍历：左-右-根

以广度优先遍历和以深度优先遍历是一组相对的遍历方式。
因为按后序遍历和以深度优先遍历这两种遍历方式的具体遍历方法相同，所以图18.46 和图18.48 是相同的，实际上depthFirstEnumeration()方法只是调用了postorderEnumeration()方法。
//各种遍历方式 P477
import java.awt.BorderLayout;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public TreeTest() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("根节点");
		DefaultMutableTreeNode nodeFirst = new DefaultMutableTreeNode("一级子节点A");
		root.add(nodeFirst);
		
		DefaultMutableTreeNode nodeSecond = new DefaultMutableTreeNode("二级子节点", false);
		nodeFirst.add(nodeSecond);
		
		root.add(new DefaultMutableTreeNode("一级子节点B"));
		JTree treeRoot = new JTree(root);
		getContentPane().add(treeRoot, BorderLayout.CENTER);
		
		Enumeration<DefaultMutableTreeNode> enumeration;
		//enumeration = root.preorderEnumeration();//前序遍历
		//enumeration = root.postorderEnumeration();//后序遍历
		//enumeration = root.breadthFirstEnumeration();//广度优先遍历
		//enumeration = root.depthFirstEnumeration();//深度优先遍历，输出与后序遍历一样
		enumeration = root.children();//遍历谝节点的子节点，获取直接子节点
		while (enumeration.hasMoreElements()) {
			DefaultMutableTreeNode node = enumeration.nextElement();
			for (int i = 0; i < node.getLevel(); ++i) {
				System.out.print("-----");
			}
			System.out.println(node.getUserObject());
		}
		
		setSize(500, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new TreeTest();
	}
}

18.4.4 定制树 P477
P478 设置分层导航栏
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

public class TreeTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public TreeTest() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("根节点");
		DefaultMutableTreeNode nodeFirst = new DefaultMutableTreeNode("一级子节点A");
		root.add(nodeFirst);
		
		DefaultMutableTreeNode nodeSecond = new DefaultMutableTreeNode("二级子节点", false);
		nodeFirst.add(nodeSecond);
		
		root.add(new DefaultMutableTreeNode("一级子节点B"));
		JTree treeRoot = new JTree(root);
		getContentPane().add(treeRoot, BorderLayout.CENTER);
		
		treeRoot.setRootVisible(false);//不显示树的根节点
		treeRoot.setRowHeight(20);//树节点的行高为20像素
		treeRoot.setFont(new Font("宋体", Font.BOLD, 14));//设置树节点的字体
		treeRoot.putClientProperty("JTree.lineStyle", "None");//节点间不采用连接线
		DefaultTreeCellRenderer treeCellRenderer = (DefaultTreeCellRenderer) treeRoot.getCellRenderer();//获得树节点的绘制对象
		treeCellRenderer.setLeafIcon(null);//设置叶子节点不采用图标
		treeCellRenderer.setClosedIcon(null);//设置节点折叠时不采用图标
		treeCellRenderer.setOpenIcon(null);//设置节点展开时不采用图标
		Enumeration<DefaultMutableTreeNode> enumeration = root.preorderEnumeration();//按前序遍历所有树节点
		while (enumeration.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
			if (!node.isLeaf()) {
				TreePath path = new TreePath(node.getPath());
				treeRoot.expandPath(path);
			}
		}
		
		setSize(500, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new TreeTest();
	}
}

18.4.5 维护树模型 P479
//功能有些问题，不太能正常的增加修改和删除？
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class TreeTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public TreeTest() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("根节点");
		DefaultMutableTreeNode nodeFirst = new DefaultMutableTreeNode("一级子节点A");
		root.add(nodeFirst);
		
		DefaultMutableTreeNode nodeSecond = new DefaultMutableTreeNode("二级子节点", false);
		nodeFirst.add(nodeSecond);
		
		root.add(new DefaultMutableTreeNode("一级子节点B"));
		JTree treeRoot = new JTree(root);
		getContentPane().add(treeRoot, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JTextField textField = new JTextField(10);
		panel.add(textField);
		
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		
		final JButton addButton = new JButton("添加");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(textField.getText());
				TreePath selectionPath = treeRoot.getSelectionPath();
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
				treeModel.insertNodeInto(node, parentNode, parentNode.getChildCount());
				TreePath path = selectionPath.pathByAddingChild(node);
				if (!treeRoot.isVisible(path)) {
					treeRoot.makeVisible(path);
				}
			}
		});
		panel.add(addButton);
		
		final JButton updButton = new JButton("修改");
		updButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath selectionPath = treeRoot.getSelectionPath();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
				node.setUserObject(textField.getText());
				treeModel.nodeChanged(node);
				treeRoot.setSelectionPath(selectionPath);
			}
		});
		panel.add(updButton);
		
		final JButton delButton = new JButton("删除");
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeRoot.getLastSelectedPathComponent();
				if (!node.isRoot()) {
					DefaultMutableTreeNode nextSelectedNode = node.getNextSibling();
					if (nextSelectedNode == null) {
						nextSelectedNode = (DefaultMutableTreeNode) node.getParent();
						treeModel.removeNodeFromParent(node);
						treeRoot.setSelectionPath(new TreePath(nextSelectedNode.getPath()));
					}
				}
			}
		});
		panel.add(delButton);
		
		setSize(500, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new TreeTest();
	}
}

18.4.6 处理展开节点事件 P481
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class TreeTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public TreeTest() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("根节点");
		DefaultMutableTreeNode nodeFirst = new DefaultMutableTreeNode("一级子节点A");
		root.add(nodeFirst);
		
		DefaultMutableTreeNode nodeSecond = new DefaultMutableTreeNode("二级子节点", false);
		nodeFirst.add(nodeSecond);
		
		root.add(new DefaultMutableTreeNode("一级子节点B"));
		JTree treeRoot = new JTree(root);
		getContentPane().add(treeRoot, BorderLayout.CENTER);
		
		treeRoot.addTreeWillExpandListener(new TreeWillExpandListener() {
			@Override
			public void treeWillExpand(TreeExpansionEvent e) {
				TreePath path = e.getPath();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				System.out.println("节点\"" + node + "\"将要被展开！");
			}
			
			@Override
			public void treeWillCollapse(TreeExpansionEvent e) {
				TreePath path = e.getPath();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				System.out.println("节点\"" + node + "\"将要被折叠！");
			}
		});
		
		treeRoot.addTreeExpansionListener(new TreeExpansionListener() {
			@Override
			public void treeExpanded(TreeExpansionEvent e) {
				TreePath path = e.getPath();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				System.out.println("节点\"" + node + "\"已被展开！");
				System.out.println();
			}
			
			@Override
			public void treeCollapsed(TreeExpansionEvent e) {
				TreePath path = e.getPath();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				System.out.println("节点\"" + node + "\"已被折叠！");
				System.out.println();
			}
		});
		
		setSize(500, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new TreeTest();
	}
}

18.4.7 范例6：为树节点增加提示信息 P483
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

public class TreeTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public TreeTest() {
		JTree tree = new JTree();
		getContentPane().add(tree, BorderLayout.CENTER);
		
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				DefaultMutableTreeNode root = new DefaultMutableTreeNode("明日科技新书");
				DefaultMutableTreeNode parent1 = new DefaultMutableTreeNode("从入门到精通系列");
				parent1.add(new DefaultMutableTreeNode("《Java从入门到精通》"));
				parent1.add(new DefaultMutableTreeNode("《PHP从入门到精通》"));
				parent1.add(new DefaultMutableTreeNode("《Visual Basic从入门到精通》"));
				parent1.add(new DefaultMutableTreeNode("《Visual C++从入门到精通》"));
				root.add(parent1);
				
				DefaultMutableTreeNode parent2 = new DefaultMutableTreeNode("编程词典系列");
				parent2.add(new DefaultMutableTreeNode("《Java编程词典》"));
				parent2.add(new DefaultMutableTreeNode("《PHP编程词典》"));
				parent2.add(new DefaultMutableTreeNode("《Visual Basic编程词典》"));
				parent2.add(new DefaultMutableTreeNode("《Visual C++编程词典》"));
				root.add(parent2);
				
				DefaultTreeModel model = new DefaultTreeModel(root);
				tree.setModel(model);
				ToolTipManager.sharedInstance().registerComponent(tree);//为树注册提示信息管理器
				Map<DefaultMutableTreeNode, String> map = new HashMap<DefaultMutableTreeNode, String>();
				map.put(root, "明日科技");
				map.put(parent1, "电子工业");
				map.put(parent2, "人民教育");
				tree.setCellRenderer(new ToolTipNode(map));
			}
		});
		
		setSize(500, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new TreeTest();
	}
}

class ToolTipNode implements TreeCellRenderer {
	private DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
	private Map<DefaultMutableTreeNode, String> map;
	public ToolTipNode(Map<DefaultMutableTreeNode, String> map) {
		this.map = map;
	}
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		renderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		renderer.setToolTipText("<html><font face=微软雅黑 size=16 color=red>" + map.get(value) + "</font></html>");
		return renderer;
	}
}

18.4.8 范例7：双击编辑树节点功能 P484
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;

public class TreeTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public TreeTest() {
		JTree tree = new JTree();
		getContentPane().add(tree, BorderLayout.CENTER);
		
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				DefaultMutableTreeNode root = new DefaultMutableTreeNode("明日科技新书");
				DefaultMutableTreeNode parent1 = new DefaultMutableTreeNode("从入门到精通系列");
				parent1.add(new DefaultMutableTreeNode("《Java从入门到精通》"));
				parent1.add(new DefaultMutableTreeNode("《PHP从入门到精通》"));
				parent1.add(new DefaultMutableTreeNode("《Visual Basic从入门到精通》"));
				parent1.add(new DefaultMutableTreeNode("《Visual C++从入门到精通》"));
				root.add(parent1);
				
				DefaultMutableTreeNode parent2 = new DefaultMutableTreeNode("编程词典系列");
				parent2.add(new DefaultMutableTreeNode("《Java编程词典》"));
				parent2.add(new DefaultMutableTreeNode("《PHP编程词典》"));
				parent2.add(new DefaultMutableTreeNode("《Visual Basic编程词典》"));
				parent2.add(new DefaultMutableTreeNode("《Visual C++编程词典》"));
				root.add(parent2);
				
				DefaultTreeModel model = new DefaultTreeModel(root);
				tree.setModel(model);
				ToolTipManager.sharedInstance().registerComponent(tree);//为树注册提示信息管理器
				Map<DefaultMutableTreeNode, String> map = new HashMap<DefaultMutableTreeNode, String>();
				map.put(root, "明日科技");
				map.put(parent1, "电子工业");
				map.put(parent2, "人民教育");
				tree.setCellRenderer(new ToolTipNode(map));
				
				JTextField textField = new JTextField();
				textField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
				TreeCellEditor editor = new DefaultCellEditor(textField);
				tree.setEditable(true);
				tree.setCellEditor(editor);
				tree.setShowsRootHandles(true);//如果不设置上根节点的显示手柄，则没有办法将根节点折叠了，因为双击会编辑文本框 P477
			}
		});
		
		setSize(500, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new TreeTest();
	}
}

class ToolTipNode implements TreeCellRenderer {
	private DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
	private Map<DefaultMutableTreeNode, String> map;
	public ToolTipNode(Map<DefaultMutableTreeNode, String> map) {
		this.map = map;
	}
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		renderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		renderer.setToolTipText("<html><font face=微软雅黑 size=16 color=red>" + map.get(value) + "</font></html>");
		return renderer;
	}
}

18.5.2 经典范例2：监听节点的选择事件 P486
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

public class TreeTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public TreeTest() {
		JTree tree = new JTree();
		getContentPane().add(tree, BorderLayout.WEST);
		
		JTextArea textArea = new JTextArea(20, 20);
		getContentPane().add(textArea, BorderLayout.EAST);
		
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				DefaultMutableTreeNode root = new DefaultMutableTreeNode("明日科技新书");
				DefaultMutableTreeNode parent1 = new DefaultMutableTreeNode("从入门到精通系列");
				parent1.add(new DefaultMutableTreeNode("《Java从入门到精通》"));
				parent1.add(new DefaultMutableTreeNode("《PHP从入门到精通》"));
				parent1.add(new DefaultMutableTreeNode("《Visual Basic从入门到精通》"));
				parent1.add(new DefaultMutableTreeNode("《Visual C++从入门到精通》"));
				root.add(parent1);
				
				DefaultMutableTreeNode parent2 = new DefaultMutableTreeNode("编程词典系列");
				parent2.add(new DefaultMutableTreeNode("《Java编程词典》"));
				parent2.add(new DefaultMutableTreeNode("《PHP编程词典》"));
				parent2.add(new DefaultMutableTreeNode("《Visual Basic编程词典》"));
				parent2.add(new DefaultMutableTreeNode("《Visual C++编程词典》"));
				root.add(parent2);
				
				DefaultTreeModel model = new DefaultTreeModel(root);
				tree.setModel(model);
				ToolTipManager.sharedInstance().registerComponent(tree);//为树注册提示信息管理器
				Map<DefaultMutableTreeNode, String> map = new HashMap<DefaultMutableTreeNode, String>();
				map.put(root, "明日科技");
				map.put(parent1, "电子工业");
				map.put(parent2, "人民教育");
				tree.setCellRenderer(new ToolTipNode(map));
				
				JTextField textField = new JTextField();
				textField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
				TreeCellEditor editor = new DefaultCellEditor(textField);
				tree.setEditable(true);
				tree.setCellEditor(editor);
				tree.setShowsRootHandles(true);//如果不设置上根节点的显示手柄，则没有办法将根节点折叠了，因为双击会编辑文本框 P477
				
				tree.addTreeSelectionListener(new TreeSelectionListener() {
					@Override
					public void valueChanged(TreeSelectionEvent e) {
						TreePath path = tree.getSelectionPath();
						if (path == null) {
							return;
						}
						
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
						String text1 = "《Java从入门到精通》\n《PHP从入门到精通》\n《Visual Basic从入门到精通》\n《Visual C++从入门到精通》";
						String text2 = "《Java编程词典》\n《PHP编程词典》\n《Visual Basic编程词典》\n《Visual C++编程词典》";
						
						if (node.toString().equals(parent1.toString())) {
							textArea.setText(text1);
						} else if (node.toString().equals(parent2.toString())) {
							textArea.setText(text2);
						}
					}
				});
			}
		});
		
		setSize(500, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new TreeTest();
	}
}

class ToolTipNode implements TreeCellRenderer {
	private DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
	private Map<DefaultMutableTreeNode, String> map;
	public ToolTipNode(Map<DefaultMutableTreeNode, String> map) {
		this.map = map;
	}
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		renderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		renderer.setToolTipText("<html><font face=微软雅黑 size=16 color=red>" + map.get(value) + "</font></html>");
		return renderer;
	}
}
//------------------------------------------------------------------------------------------------
//Java从入门到精通 第19章 高级布局管理器 P488
19.1 箱式布局管理器 P489
import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class BoxTest extends JFrame {
	private static final long serialVersionUID = 1L;

	public BoxTest() {
		Box topicBox = Box.createHorizontalBox();
		getContentPane().add(topicBox, BorderLayout.NORTH);
		topicBox.add(Box.createHorizontalStrut(5));//添加一个5像素宽的水平支柱
		JLabel topicLabel = new JLabel("主题：");
		topicBox.add(topicLabel);
		topicBox.add(Box.createHorizontalStrut(5));
		JTextField topicTextField = new JTextField(30);
		topicBox.add(topicTextField);
		
		//以下内容都由垂直box控制
		Box box = Box.createVerticalBox();
		getContentPane().add(box, BorderLayout.CENTER);
		box.add(Box.createVerticalStrut(5));
		
		Box contentBox = Box.createHorizontalBox();
		contentBox.setAlignmentX(1);//设置组件的水平调整值，靠右侧对齐；0是靠左侧对齐
		box.add(contentBox);
		
		contentBox.add(Box.createHorizontalStrut(5));//createHorizontalGlue()可以试下效果
		
		JLabel contentLabel = new JLabel("内容：");
		contentLabel.setAlignmentY(0);
		contentBox.add(contentLabel);
		contentBox.add(Box.createHorizontalStrut(5));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setAlignmentY(0);
		contentBox.add(scrollPane);
		
		JTextArea contentTextArea = new JTextArea();
		scrollPane.setViewportView(contentTextArea);
		box.add(Box.createVerticalStrut(5));
		JButton submitButton = new JButton("确定");
		submitButton.setAlignmentX(1);
		box.add(submitButton);
		
		setSize(500, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new BoxTest();
	}
}

19.2 卡片布局管理器 P491
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CardLayoutTest extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel cardPanel;
	private CardLayout cardLayout;
	
	
	public CardLayoutTest() {
		super();
		setTitle("使用卡片布局管理器");
		setBounds(100, 100, 600, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);//创建一个采用卡片布局管理器的面板对象
		getContentPane().add(cardPanel, BorderLayout.CENTER);
		String labelNames[] = {"Card A", "Card B", "Card C"};
		for (int i = 0; i < labelNames.length; ++i) {
			final JLabel label = new JLabel(labelNames[i]);
			cardPanel.add(label, labelNames[i]);//向采用卡片布局管理器的面板中添加卡片
		}
		
		final JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		String buttonNames[] = {"First", "Previous", "Card A", "Card B", "Card C", "Next", "Last"};
		for (int i = 0; i < buttonNames.length; ++i) {
			final JButton button = new JButton(buttonNames[i]);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String buttonText = button.getText();
					if (buttonText.equals(buttonNames[0])) {
						cardLayout.first(cardPanel);
					}
					else if (buttonText.equals(buttonNames[1])) {
						cardLayout.previous(cardPanel);
					}
					else if (buttonText.equals(buttonNames[2])) {
						cardLayout.show(cardPanel, buttonNames[2]);
					}
					else if (buttonText.equals(buttonNames[3])) {
						cardLayout.show(cardPanel, buttonNames[3]);
					}
					else if (buttonText.equals(buttonNames[4])) {
						cardLayout.show(cardPanel, buttonNames[4]);
					}
					else if (buttonText.equals(buttonNames[5])) {
						cardLayout.next(cardPanel);
					}
					else {
						cardLayout.last(cardPanel);
					}
				}
			});
			buttonPanel.add(button);
		}
	}
	
	public static void main(String args[]) {
		new CardLayoutTest();
	}
}

19.3 网格组布局管理器 P493
//ExampleFrame_11.java
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ExampleFrame_11 extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String args[]) {
		ExampleFrame_11 frame = new ExampleFrame_11();
		frame.setVisible(true);
	}
	
	public ExampleFrame_11() {
		super();
		getContentPane().setLayout(new GridBagLayout());
		setTitle("使用进度条");
		setBounds(100, 100, 500, 375);
		setBounds(100, 100, 266, 132);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JLabel label = new JLabel();
		label.setForeground(new Color(255, 0, 0));
		label.setFont(new Font("", Font.BOLD, 16));
		label.setText("欢迎使用在线升级功能！");
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		getContentPane().add(label, gridBagConstraints);
		
		final JProgressBar progressBar = new JProgressBar();// 创建进度条对象
		progressBar.setStringPainted(true);// 设置显示提示信息
		progressBar.setIndeterminate(true);// 设置采用不确定进度条
		progressBar.setString("升级进行中......");// 设置提示信息
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints_1.gridy = 1;
		gridBagConstraints_1.gridx = 0;
		getContentPane().add(progressBar, gridBagConstraints_1);
		
		final JButton button = new JButton();
		button.setText("完成");
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
		gridBagConstraints_2.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints_2.gridy = 2;
		gridBagConstraints_2.gridx = 1;
		getContentPane().add(button, gridBagConstraints_2);
		new Progress(progressBar, button).start();// 利用线程模拟一个在线升级任务
	}
	
	class Progress extends Thread {// 利用线程模拟一个在线升级任务
		private final int[] progressValue = { 6, 18, 27, 39, 51, 66, 81,
				100 };// 模拟任务完成百分比
		private JProgressBar progressBar;// 进度条对象
		private JButton button;// 完成按钮对象
		
		public Progress(JProgressBar progressBar, JButton button) {
			this.progressBar = progressBar;
			this.button = button;
		}
		
		public void run() {
			// 通过循环更新任务完成百分比
			for (int i = 0; i < progressValue.length; i++) {
				try {
					Thread.sleep(1000);// 令线程休眠1秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				progressBar.setValue(progressValue[i]);// 设置任务完成百分比
			}
			progressBar.setIndeterminate(false);// 设置采用确定进度条
			progressBar.setString("升级完成！");// 设置提示信息
			button.setEnabled(true);// 设置按钮可用
		}
	}
}

//网格组件管理器P499，这么一个一个设置感觉效率很低？
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GridBagLayoutTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public GridBagLayoutTest() {
		super();
		setTitle("使用网格布局管理器");
		setBounds(100, 100, 600, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new GridBagLayout());
		
		final JButton button = new JButton("A");
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;//起始点为第1行
		gridBagConstraints.gridx = 0;//起始点为第1列
		//gridBagConstraints.insets = new Insets(0, 5, 0, 0);//设置组件左侧的最小距离
		gridBagConstraints.weightx = 10;//第1列的分布方式为10%
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(button, gridBagConstraints);
		
		final JButton button_1 = new JButton("B");
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.gridy = 0;
		gridBagConstraints_1.gridx = 1;
		gridBagConstraints_1.insets = new Insets(0, 5, 0, 0);//设置组件左侧的最小距离
		gridBagConstraints_1.weightx = 20;//第1列的分布方式为20%
		gridBagConstraints_1.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(button_1, gridBagConstraints_1);
		
		final JButton button_2 = new JButton("C");
		final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
		gridBagConstraints_2.gridy = 0;
		gridBagConstraints_2.gridx = 2;
		gridBagConstraints_2.gridheight = 2;//组件占用两行
		gridBagConstraints_2.insets = new Insets(0, 5, 0, 0);
		gridBagConstraints_2.weightx = 30;//第1列的分布方式 为30%
		gridBagConstraints_2.fill = GridBagConstraints.BOTH;//同时调整组件的宽度和高度
		getContentPane().add(button_2, gridBagConstraints_2);
		
		final JButton button_3 = new JButton("D");
		final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
		gridBagConstraints_3.gridy = 0;
		gridBagConstraints_3.gridx = 3;
		gridBagConstraints_3.gridheight = 4;//组件占用四行
		gridBagConstraints_3.insets = new Insets(0, 5, 0, 5);//设置组件左侧和右侧的最小距离
		gridBagConstraints_3.weightx = 40;//第1列的分布方式 为40%
		gridBagConstraints_3.fill = GridBagConstraints.BOTH;//同时调整组件的宽度和高度
		getContentPane().add(button_3, gridBagConstraints_3);
		
		final JButton button_4 = new JButton("E");
		final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
		gridBagConstraints_4.gridy = 1;
		gridBagConstraints_4.gridx = 0;
		gridBagConstraints_4.gridwidth = 2;//组件占用两列
		gridBagConstraints_4.insets = new Insets(5, 0, 0, 0);//设置组件左侧和右侧的最小距离
		gridBagConstraints_4.fill = GridBagConstraints.HORIZONTAL;//同时调整组件的宽度和高度
		getContentPane().add(button_4, gridBagConstraints_4);
		
		final JButton button_5 = new JButton("F");
		final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
		gridBagConstraints_5.gridy = 2;
		gridBagConstraints_5.gridx = 0;
		gridBagConstraints_5.insets = new Insets(5, 0, 0, 0);//设置组件左侧和右侧的最小距离
		gridBagConstraints_5.fill = GridBagConstraints.HORIZONTAL;//同时调整组件的宽度
		getContentPane().add(button_5, gridBagConstraints_5);
		
		final JButton button_6 = new JButton("G");
		final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
		gridBagConstraints_6.gridy = 2;
		gridBagConstraints_6.gridx = 1;
		gridBagConstraints_6.gridwidth = 2;//组件占用两列
		gridBagConstraints_6.gridheight = 2;//组件占用两行
		gridBagConstraints_6.insets = new Insets(5, 5, 0, 0);//设置组件左侧和右侧的最小距离
		gridBagConstraints_6.fill = GridBagConstraints.BOTH;//同时调整组件的宽度和高度
		getContentPane().add(button_6, gridBagConstraints_6);
		
//		final JButton button_6 = new JButton("G");
//		final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
//		gridBagConstraints_6.gridy = 2;
//		gridBagConstraints_6.gridx = 1;
//		gridBagConstraints_6.gridwidth = 2;//组件占用两列
//		gridBagConstraints_6.gridheight = 2;//组件占用两行
//		gridBagConstraints_6.insets = new Insets(5, 5, 0, 0);//设置组件上侧和右侧的最小距离
//		gridBagConstraints_6.fill = GridBagConstraints.VERTICAL;//同时调整组件的宽度和高度
//		gridBagConstraints_6.ipadx = 30;//增加组件的首选宽度，在默认基础上宽度增加value值，可以为负
//		gridBagConstraints_6.anchor = GridBagConstraints.EAST;//显示在东方
//		getContentPane().add(button_6, gridBagConstraints_6);
		
		final JButton button_7 = new JButton("G");
		final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
		gridBagConstraints_7.gridy = 3;
		gridBagConstraints_7.gridx = 0;
		gridBagConstraints_7.insets = new Insets(5, 0, 0, 0);//设置组件左侧和右侧的最小距离
		gridBagConstraints_7.fill = GridBagConstraints.HORIZONTAL;//同时调整组件的宽度和高度
		getContentPane().add(button_7, gridBagConstraints_7);
	}
	
	public static void main(String args[]) {
		new GridBagLayoutTest();
	}
}


19.4 弹簧布局管理器 P499
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class SpringLayoutTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public SpringLayoutTest() {
		super();
		setTitle("使用弹簧布局管理器");
		setBounds(100, 100, 600, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SpringLayout springLayout = new SpringLayout();
		Container contentPane = getContentPane();
		contentPane.setLayout(springLayout);//修改窗体容器为采用弹簧布局管理器
		
		JLabel topicLabel = new JLabel("主题：");
		contentPane.add(topicLabel);
		springLayout.putConstraint(SpringLayout.NORTH, topicLabel, 5, SpringLayout.NORTH, contentPane);//主题标签北侧->容器北侧
		springLayout.putConstraint(SpringLayout.WEST, topicLabel, 5, SpringLayout.WEST, contentPane);//主题标签西侧->容器西侧
		
		JTextField topicTextField = new JTextField();
		contentPane.add(topicTextField);
		//几个入参数理解为第一个对象(para2)的哪一侧(para1)到第二个对象(p5)的哪一侧(p4)的距离(正数为->，负数为<-)
		springLayout.putConstraint(SpringLayout.NORTH, topicTextField, 5, SpringLayout.NORTH, contentPane);//主题文本框北侧->容器北侧
		springLayout.putConstraint(SpringLayout.WEST, topicTextField, 5, SpringLayout.EAST, topicLabel);//主题文本框西侧->容器西侧
		springLayout.putConstraint(SpringLayout.EAST, topicTextField, -5, SpringLayout.EAST, contentPane);//主题文本框东侧->容器东侧
		
		JLabel contentLabel = new JLabel("内容：");
		contentPane.add(contentLabel);
		springLayout.putConstraint(SpringLayout.NORTH, contentLabel, 5, SpringLayout.SOUTH, topicTextField);
		springLayout.putConstraint(SpringLayout.WEST, contentLabel, 5, SpringLayout.WEST, contentPane);
		
		JScrollPane contentScrollPane = new JScrollPane();
		contentScrollPane.setViewportView(new JTextArea());
		contentPane.add(contentScrollPane);
		springLayout.putConstraint(SpringLayout.NORTH, contentScrollPane, 5, SpringLayout.SOUTH, topicTextField);
		springLayout.putConstraint(SpringLayout.WEST, contentScrollPane, 5, SpringLayout.EAST, contentLabel);
		springLayout.putConstraint(SpringLayout.EAST, contentScrollPane, -5, SpringLayout.EAST, contentPane);
		
		JButton resetButton = new JButton("清空");
		contentPane.add(resetButton);
		springLayout.putConstraint(SpringLayout.SOUTH, resetButton, -5, SpringLayout.SOUTH, contentPane);
		
		JButton submitButton = new JButton("确定");
		contentPane.add(submitButton);
		springLayout.putConstraint(SpringLayout.SOUTH, submitButton, -5, SpringLayout.SOUTH, contentPane);
		springLayout.putConstraint(SpringLayout.EAST, submitButton, -5, SpringLayout.EAST, contentPane);
		springLayout.putConstraint(SpringLayout.SOUTH, contentScrollPane, -5, SpringLayout.NORTH, submitButton);
		springLayout.putConstraint(SpringLayout.EAST, resetButton, -5, SpringLayout.WEST, submitButton);//如果不加这句话，则resetButton将会默认贴在左侧。如果-5错写成5，则会使两个组件相贴
		//改为下句将使清空按钮贴在左侧，确定按钮拉长至整个宽度
		//springLayout.putConstraint(SpringLayout.WEST, submitButton, 5, SpringLayout.EAST, resetButton);//如果不加这句话，则resetButton将会默认贴在左侧
		
		validate();//不加这句话无法刷新出来
	}
	
	public static void main(String args[]) {
		new SpringLayoutTest();
	}
}

19.4.2 使用弹簧和支柱 P502

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Spring;
import javax.swing.SpringLayout;

public class SpringLayoutTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public SpringLayoutTest() {
		super();
		setTitle("使用弹簧布局管理器");
		setBounds(100, 100, 600, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SpringLayout springLayout = new SpringLayout();
		Container contentPane = getContentPane();
		contentPane.setLayout(springLayout);//修改窗体容器为采用弹簧布局管理器
		
		Spring vST = Spring.constant(20);//创建一个支柱
		Spring hSP = Spring.constant(20, 100, 500);//创建一个弹簧
		
		JButton lButton = new JButton("按钮L");
		contentPane.add(lButton);
		springLayout.putConstraint(SpringLayout.NORTH, lButton, vST, SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.WEST, lButton, hSP, SpringLayout.WEST, contentPane);
		
		JButton rButton = new JButton("按钮R");
		contentPane.add(rButton);
		springLayout.putConstraint(SpringLayout.NORTH, rButton, 0, SpringLayout.NORTH, lButton);//该语句表示rButton与lButton到北侧的距离一致，如果不设置，则rButton将会紧贴北侧
		springLayout.putConstraint(SpringLayout.EAST, rButton, Spring.scale(hSP, 10), SpringLayout.EAST, lButton);//中间距离按边距的两倍拉伸
		springLayout.putConstraint(SpringLayout.EAST, contentPane, hSP, SpringLayout.EAST, rButton);
	}
	
	public static void main(String args[]) {
		new SpringLayoutTest();
	}
}

19.4.3 利用弹簧控制组件大小 P503
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;

public class SpringLayoutTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public SpringLayoutTest() {
		super();
		setTitle("使用弹簧布局管理器");
		setBounds(100, 100, 600, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SpringLayout springLayout = new SpringLayout();
		Container contentPane = getContentPane();
		contentPane.setLayout(springLayout);//修改窗体容器为采用弹簧布局管理器
		
		Spring vST = Spring.constant(20);//创建一个支柱
		Spring hSP = Spring.constant(20, 100, 500);//创建一个弹簧
		
		JButton lButton = new JButton("按钮L");
		contentPane.add(lButton);
		springLayout.putConstraint(SpringLayout.NORTH, lButton, vST, SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.WEST, lButton, hSP, SpringLayout.WEST, contentPane);
		
		JButton rButton = new JButton("按钮R");
		contentPane.add(rButton);
		springLayout.putConstraint(SpringLayout.NORTH, rButton, 0, SpringLayout.NORTH, lButton);//该语句表示rButton与lButton到北侧的距离一致，如果不设置，则rButton将会紧贴北侧
		springLayout.putConstraint(SpringLayout.EAST, rButton, Spring.scale(hSP, 10), SpringLayout.EAST, lButton);//中间距离按边距的两倍拉伸
		springLayout.putConstraint(SpringLayout.EAST, contentPane, hSP, SpringLayout.EAST, rButton);
		
		//用弹簧和支柱分别可以设置按钮为宽度可拉伸，高度不变
		Spring widthSP = Spring.constant(60, 300, 600);//创建一个弹簧
		Spring heightST = Spring.constant(60);//创建一个支柱
		
		Constraints lButtonCons = springLayout.getConstraints(lButton);//获取“按钮L”的Constraints对象
		lButtonCons.setWidth(widthSP);//设置控制组件宽度的弹簧
		lButtonCons.setHeight(heightST);//设置控制组件高度的支柱
		
		Constraints rButtonCons = springLayout.getConstraints(rButton);//获取“按钮R”的Constraints对象
		rButtonCons.setWidth(widthSP);
		rButtonCons.setHeight(heightST);
	}
	
	public static void main(String args[]) {
		new SpringLayoutTest();
	}
}

19.5.1 制作圆形布局管理器 P504

19.5.1 制作阶梯布局管理器 P505
//------------------------------------------------------------------------------------------------
//Java从入门到精通 第20章 高级布局管理器 P488 AWT绘图技术 P508
20.1.1 Graphics
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawCircle extends JFrame {
	private static final long serialVersionUID = 1L;
	private final int OVAL_WIDTH = 80;
	private final int OVAL_HEIGHT = 80;
	
	public DrawCircle() {
		super();
		initialize();
	}
	
	private void initialize() {
		this.setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(new DrawPanel());
		this.setTitle("绘图实例");
	}
	
	public static void main(String args[]) {
		new DrawCircle().setVisible(true);;
	}
	
	class DrawPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.drawOval(10, 10, OVAL_WIDTH, OVAL_HEIGHT);
			g.drawOval(80, 10, OVAL_WIDTH, OVAL_HEIGHT);
			g.drawOval(150, 10, OVAL_WIDTH, OVAL_HEIGHT);
			g.drawOval(50, 70, OVAL_WIDTH, OVAL_HEIGHT);
			g.drawOval(120, 70, OVAL_WIDTH, OVAL_HEIGHT);//g.fillOval(120, 70, OVAL_WIDTH, OVAL_HEIGHT);//实心椭圆
		}
	}
}

Graphics类常用的图形绘制方法 P510

20.1.2 Graphics2D P511
Graphics2D继承Graphics类，实现了功能更强大的绘图操作的集合。
Graphics2D是推荐使用的绘图类，但是程序设计中提供的绘图对象大多是Graphics类的实例对象，这时应该使用强制类型转换将其转换为Graphics2D类型。
例如：
public void paint(Graphics g) {
	Graphics2D g2 = (Graphics2D) g;//强制畸形转换为Graphics2D类型
	g2.……
}

P512
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public DrawFrame() {
		super();
		initialize();
	}
	
	private void initialize() {
		this.setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new CanvasPanel());
		this.setTitle("绘图实例2");
	}
	
	public static void main(String args[]) {
		new DrawFrame().setVisible(true);;
	}
	
	class CanvasPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;
			Shape shapes[] = new Shape[4];
			shapes[0] = new Ellipse2D.Double(5, 5, 100, 100);//圆形，前两个参数分别为中心坐标，后两个参数为长短轴值，一样则为圆，不一样则为椭圆
			shapes[1] = new Rectangle2D.Double(110, 5, 100, 100);//矩形
			shapes[2] = new Rectangle2D.Double(15, 15, 80, 80);//矩形
			shapes[3] = new Ellipse2D.Double(120, 15, 80, 80);//圆形
			for (Shape shape : shapes) { //遍历图形数组
				Rectangle2D bounds = shape.getBounds2D();
				if (bounds.getWidth() == 80) {
					g2.fill(shape);
				}
				else {
					g2.draw(shape);
				}
			}
		}
	}
}

20.1.3 绘制指定角度的填充扇形 P513
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawSector extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public DrawSector() {
		super();
		initialize();
	}
	
	private void initialize() {
		this.setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(new DrawSectorPanel());//add(new DrawSectorPanel());效果一样
		this.setTitle("绘图实例");
	}
	
	public static void main(String args[]) {
		new DrawSector().setVisible(true);;
	}
	
	class DrawSectorPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			//super.paint(g);
			g.fillArc(40, 20, 80, 80, 0, 150);//6个参数分别为：坐标x，坐标y，width，height，起始角度，角度范围
			g.fillArc(140, 20, 80, 80, 180, -150);
			g.fillArc(40, 40, 80, 80, 0, -110);
			g.fillArc(140, 40, 80, 80, 180, 110);
		}
	}
}

20.1.4 绘制多边形 P513
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawSector extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public DrawSector() {
		super();
		initialize();
	}
	
	private void initialize() {
		this.setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new DrawPolygonPanel());
		this.setTitle("绘图实例");
	}
	
	public static void main(String args[]) {
		new DrawSector().setVisible(true);;
	}
	
	class DrawPolygonPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			int x1[] = {100, 120, 180, 140, 150, 100, 50, 60, 20, 80};//多边形的横坐标，各个拐点的坐标位置
			int y1[] = {20, 85, 90, 120, 180, 140, 180, 120, 90, 85};//多边形的纵坐标
			int n1 = 10;//多边形的边数，与两个数组个数匹配上的
			g.fillPolygon(x1, y1, n1);//五角形

			//g.setColor(Color.RED);设置颜色为红色，包括填充颜色和边框颜色，之后都是这个颜色，除非再次setColor设置
			
			int x2[] = {210, 270, 310, 270, 210, 170};
			int y2[] = {20, 20, 65, 110, 110, 65};
			int n2 = 6;
			g.fillPolygon(x2, y2, n2);
			
			int x3[] = {180, 220, 260, 240, 260, 220, 180, 200};
			int y3[] = {120, 140, 120, 160, 200, 180, 200, 160};
			int n3 = 8;
			g.drawPolygon(x3, y3, n3);
		}
	}
}

20.2 绘图颜色与笔画属性 P514
20.2.3 为图形填充渐变色 P517
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawSector extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public DrawSector() {
		super();
		initialize();
	}
	
	private void initialize() {
		this.setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new FillGradientPanel());
		this.setTitle("绘图实例");
	}
	
	public static void main(String args[]) {
		new DrawSector().setVisible(true);;
	}
	
	class FillGradientPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			Rectangle2D.Float rect = new Rectangle2D.Float(20, 20, 280, 140);
			GradientPaint paint = new GradientPaint(20, 20, Color.BLUE, 100, 80, Color.RED, true);
			g2.setPaint(paint);
			g2.fill(rect);
		}
	}
}

20.2.4 设置笔画的粗细 P517
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawSector extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public DrawSector() {
		super();
		initialize();
	}
	
	private void initialize() {
		this.setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new ChangeStrokeWidthPanel());
		this.setTitle("绘图实例");
	}
	
	public static void main(String args[]) {
		new DrawSector().setVisible(true);;
	}
	
	class ChangeStrokeWidthPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			
			BasicStroke stroke = new BasicStroke(1);//宽度为1的笔画对象
			g2.setStroke(stroke);
			Ellipse2D.Float ellipse = new Ellipse2D.Float(20, 20, 100, 60);
			g2.draw(ellipse);
			
			stroke = new BasicStroke(4);
			g2.setStroke(stroke);
			ellipse = new Ellipse2D.Float(160, 20, 100, 60);
			g2.draw(ellipse);
			
			stroke = new BasicStroke(6);
			g2.setStroke(stroke);
			ellipse = new Ellipse2D.Float(20, 100, 100, 60);
			g2.draw(ellipse);
			
			stroke = new BasicStroke(8);
			g2.setStroke(stroke);
			ellipse = new Ellipse2D.Float(160, 100, 100, 60);
			g2.draw(ellipse);
			
		}
	}
}

20.3 绘制文本 P518

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Date;

import javax.swing.JFrame;

public class DrawString extends JFrame {
	private static final long serialVersionUID = 1L;
	private Shape rect;
	private Font font;
	private Date date;
	
	public DrawString() {
		rect = new Rectangle2D.Double(10, 10, 200, 80);
		font = new Font("宋体", Font.BOLD, 16);
		date = new Date();
		
		this.setSize(230, 140);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new CanvasPanel());
	}
	
	public static void main(String args[]) {
		new DrawString().setVisible(true);;
	}
	
	class CanvasPanel extends Canvas {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.CYAN);
			g2.fill(rect);
			g2.setColor(Color.BLUE);
			g2.setFont(font);
			g2.drawString("现在时间是", 20, 30);
			g2.drawString(String.format("%tr", date), 50, 60);
		}
	}
}

20.3.3 设置文本的字体 P520
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TextFontFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public TextFontFrame() {
		super();
		initialize();
	}
	
	private void initialize() {
		this.setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new ChangeTextFontPanel());
		this.setTitle("绘图实例");
	}
	
	public static void main(String args[]) {
		new TextFontFrame().setVisible(true);;
	}
	
	class ChangeTextFontPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			String value = "明日编程词典社区";
			int x = 40;
			int y = 50;
			Font font = new Font("华文行楷", Font.BOLD + Font.ITALIC, 26);
			g.setFont(font);
			g.drawString(value, x, y);
			
			value = "http://community.mrbccd.com";
			x = 10;
			y = 100;
			font = new Font("宋体", Font.BOLD, 20);
			g.setFont(font);
			g.drawString(value, x, y);
		}
	}
}


20.3.4 设置文本的图形和颜色 P521
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TextFontFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public TextFontFrame() {
		super();
		initialize();
	}
	
	private void initialize() {
		this.setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new TextAndShapeColorPanel());
		this.setTitle("绘图实例");
	}
	
	public static void main(String args[]) {
		new TextFontFrame().setVisible(true);;
	}
	
	class TextAndShapeColorPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			String value = "只要努力————";
			int x = 60;
			int y = 60;
			Color color = new Color(255, 0, 0);
			g.setColor(color);
			g.drawString(value, x, y);
			
			value = "一切皆有可能";
			x = 140;
			y = 100;
			color = new Color(0, 0, 255);
			g.setColor(color);
			g.drawString(value, x, y);
			
			color = Color.ORANGE;
			g.setColor(color);
			g.drawRoundRect(40, 30, 200, 100, 40, 30);//外圈
			g.drawRoundRect(45, 35, 190, 90, 36, 26);//内圈
		}
	}
}


20.4 图片处理 P522

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawImage extends JFrame {
	private static final long serialVersionUID = 1L;
	Image img;
	
	public DrawImage() {
		super();
		initialize();
		
		URL imaUrl = DrawImage.class.getResource("sjtu.jpg");
		img = Toolkit.getDefaultToolkit().getImage(imaUrl);
	}
	
	private void initialize() {
		this.setSize(800, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new CanvasPanel());
		this.setTitle("绘制图片");
	}
	
	public static void main(String args[]) {
		new DrawImage().setVisible(true);;
	}
	
	class CanvasPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(img, 0, 0, this);//四个参数：要显示的图片对象，水平位置，垂直位置，要通知的图像观察者
		}
	}
}

20.4.2 放大与缩小 P523

import java.awt.*;
import java.net.*;
import javax.swing.*;
public class ImageZoom extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image img;
	private JPanel contentPanel = null;
	private JSlider jSlider = null;
	private int imgWidth, imgHeight;
	private Canvas canvas = null;
	public ImageZoom() {
		initialize(); // 调用初始化方法
	}
	// 界面初始化方法
	private void initialize() {
		URL imgUrl = ImageZoom.class.getResource("sjtu.jpg");// 获取图片资源的路径
		img = Toolkit.getDefaultToolkit().getImage(imgUrl);// 获取图片资源
		canvas = new MyCanvas();
		this.setBounds(100, 100, 800, 600); // 设置窗体大小和位置
		this.setContentPane(getContentPanel()); // 设置内容面板
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体关闭模式
		this.setTitle("绘制图片"); // 设置窗体标题
	}
	// 内容面板的布局
	private JPanel getContentPanel() { //这是新写的方法，与JFrame的getContentPane()不一样
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(getJSlider(), BorderLayout.SOUTH);
			contentPanel.add(canvas, BorderLayout.CENTER);
		}
		return contentPanel;
	}
	// 获取滑块组件
	private JSlider getJSlider() {
		if (jSlider == null) {
			jSlider = new JSlider();
			jSlider.setMaximum(1000);
			jSlider.setValue(100);
			jSlider.setMinimum(1);
			jSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					canvas.repaint();//将调用paint()方法
				}
			});
		}
		return jSlider;
	}
	// 主方法
	public static void main(String[] args) {
		new ImageZoom().setVisible(true);
	}
	// 画板类
	class MyCanvas extends Canvas {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			int newW = 0, newH = 0;
			imgWidth = img.getWidth(this); // 获取图片宽度
			imgHeight = img.getHeight(this); // 获取图片高度
			float value = jSlider.getValue();// 滑块组件的取值
			newW = (int) (imgWidth * value / 100);// 计算图片放大后的宽度
			newH = (int) (imgHeight * value / 100);// 计算图片放大后的高度
			g.drawImage(img, 0, 0, newW, newH, this);// 绘制指定大小的图片
		}
	}
}

repaint()方法将调用paint()方法，实现组件或画板的重画功能，类似于界面刷新

20.4.3 图片翻转 P525

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class PartImage extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image img;
	private int dx1, dy1, dx2, dy2;
	private int sx1, sy1, sx2, sy2;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private MyCanvas canvasPanel = null;
	
	private final int MaxWidth = 600;
	private final int MaxHeight = 400;
	
	public PartImage() {
		dx2 = sx2 = MaxWidth; // 初始化图像大小
		dy2 = sy2 = MaxHeight;
		initialize(); // 调用初始化方法
	}
	
	// 界面初始化方法
	private void initialize() {
		URL imgUrl = PartImage.class.getResource("sjtu.jpg");// 获取图片资源的路径
		img = Toolkit.getDefaultToolkit().getImage(imgUrl); // 获取图片资源
		this.setBounds(100, 100, MaxWidth, MaxHeight + 60); // 设置窗体大小和位置
		this.setContentPane(getJPanel());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体关闭模式
		this.setTitle("图片翻转"); // 设置窗体标题
	}
	
	// 获取内容面板的方法
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getControlPanel(), BorderLayout.SOUTH);
			jPanel.add(getMyCanvas1(), BorderLayout.CENTER);
		}
		return jPanel;
	}
	
	// 获取按钮控制面板的方法
	private JPanel getControlPanel() {
		if (jPanel1 == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(getJButton(), new GridBagConstraints());
			jPanel1.add(getJButton1(), gridBagConstraints);
		}
		return jPanel1;
	}
	
	// 获取水平翻转按钮
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("水平翻转");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					sx1 = Math.abs(sx1 - MaxWidth);
					sx2 = Math.abs(sx2 - MaxWidth);
					canvasPanel.repaint();
				}
			});
		}
		return jButton;
	}
	
	// 获取垂直翻转按钮
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("垂直翻转");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					sy1 = Math.abs(sy1 - MaxHeight);
					sy2 = Math.abs(sy2 - MaxHeight);
					canvasPanel.repaint();
				}
			});
		}
		return jButton1;
	}
	
	// 获取画板面板
	private MyCanvas getMyCanvas1() {
		if (canvasPanel == null) {
			canvasPanel = new MyCanvas();
		}
		return canvasPanel;
	}
	
	// 主方法
	public static void main(String[] args) {
		new PartImage().setVisible(true);
	}
	
	// 画板
	class MyCanvas extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, this);// 绘制指定大小的图片
		}
	}
} // @jve:decl-index=0:visual-constraint="10,10"

20.4.4 图片旋转 P527

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class RotateImage extends JFrame {
	private static final long serialVersionUID = 1L;
	private Image img;
	private MyCanvas canvasPanel = null;
	
	public RotateImage() {
		initialize(); // 调用初始化方法
	}
	
	// 界面初始化方法
	private void initialize() {
		URL imgUrl = RotateImage.class.getResource("sjtu.jpg");// 获取图片资源的路径
		img = Toolkit.getDefaultToolkit().getImage(imgUrl); // 获取图片资源
		canvasPanel = new MyCanvas();
		add(canvasPanel);
		this.setBounds(100, 100, 400, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体关闭模式
		this.setTitle("图片旋转"); // 设置窗体标题
	}
		
	// 主方法
	public static void main(String[] args) {
		new RotateImage().setVisible(true);
	}
	
	// 画板
	class MyCanvas extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.rotate(Math.toRadians(5));
			g2.drawImage(img, 70, 10, 300, 200, this);
			g2.rotate(Math.toRadians(5));
			g2.drawImage(img, 70, 10, 300, 200, this);
			g2.rotate(Math.toRadians(5));
			g2.drawImage(img, 70, 10, 300, 200, this);
		}
	}
}


20.4.5 图片倾斜 P528
import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class TiltImage extends JFrame {
	private static final long serialVersionUID = 1L;
	private Image img;
	private MyCanvas canvasPanel = null;
	
	public TiltImage() {
		initialize(); // 调用初始化方法
	}
	
	// 界面初始化方法
	private void initialize() {
		URL imgUrl = TiltImage.class.getResource("sjtu.jpg");// 获取图片资源的路径
		img = Toolkit.getDefaultToolkit().getImage(imgUrl); // 获取图片资源
		canvasPanel = new MyCanvas();
		add(canvasPanel);
		this.setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体关闭模式
		this.setTitle("图片倾斜"); // 设置窗体标题
	}
		
	// 主方法
	public static void main(String[] args) {
		new TiltImage().setVisible(true);
	}
	
	// 画板
	class MyCanvas extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.shear(0.4, 0.2);//同时水平倾斜和竖起倾斜
			g2.drawImage(img, 0, 0, 300, 200, this);
		}
	}
}

20.4.6 图形的交运算 P530
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

public class IntersectOperationImage extends JFrame {
	private static final long serialVersionUID = 1L;
	private IntersectOperationPanel jPanel = null;
	
	public IntersectOperationImage() {
		initialize(); // 调用初始化方法
	}
	
	// 界面初始化方法
	private void initialize() {
		jPanel = new IntersectOperationPanel();
		add(jPanel);
		this.setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体关闭模式
		this.setTitle("图片交"); // 设置窗体标题
	}
		
	// 主方法
	public static void main(String[] args) {
		new IntersectOperationImage().setVisible(true);
	}
	
	// 画板
	class IntersectOperationPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			Rectangle2D.Float rect = new Rectangle2D.Float(30, 30, 160, 120);
			Ellipse2D.Float ellipse = new Ellipse2D.Float(20, 30, 180, 180);
			Area area1 = new Area(rect);
			Area area2 = new Area(ellipse);
			area1.intersect(area2);
			g2.draw(area1);
				
			Ellipse2D.Float ellipse1 = new Ellipse2D.Float(190, 20, 100, 140);
			Ellipse2D.Float ellipse2 = new Ellipse2D.Float(240, 20, 100, 140);
			Area area3 = new Area(ellipse1);
			Area area4 = new Area(ellipse2);
			area3.intersect(area4);
			g2.fill(area3);
		}
	}
}


20.4.7 图形的异或运算 P530
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

public class ExclusiveOrOperationImage extends JFrame {
	private static final long serialVersionUID = 1L;
	private ExclusiveOrOperationPanel jPanel = null;
	
	public ExclusiveOrOperationImage() {
		initialize(); // 调用初始化方法
	}
	
	// 界面初始化方法
	private void initialize() {
		jPanel = new ExclusiveOrOperationPanel();
		add(jPanel);
		this.setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体关闭模式
		this.setTitle("图片异或"); // 设置窗体标题
	}
		
	// 主方法
	public static void main(String[] args) {
		new ExclusiveOrOperationImage().setVisible(true);
	}
	
	// 画板
	class ExclusiveOrOperationPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			Ellipse2D.Float ellipse1 = new Ellipse2D.Float(20, 70, 160, 60);
			Ellipse2D.Float ellipse2 = new Ellipse2D.Float(120, 20, 60, 160);
			Area area1 = new Area(ellipse1);
			Area area2 = new Area(ellipse2);
			area1.exclusiveOr(area2);
			g2.fill(area1);
				
			Ellipse2D.Float ellipse3 = new Ellipse2D.Float(200, 70, 160, 60);
			Ellipse2D.Float ellipse4 = new Ellipse2D.Float(250, 20, 60, 160);
			Area area3 = new Area(ellipse3);
			Area area4 = new Area(ellipse4);
			area3.exclusiveOr(area4);
			g2.fill(area3);
		}
	}
}

20.5.1 绘制花瓣 P531
import java.awt.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

public class DrawFlowerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private DrawFlowerPanel drawFlowerPanel = null;
	
	public DrawFlowerFrame() {
		initialize(); // 调用初始化方法
	}
	
	// 界面初始化方法
	private void initialize() {
		drawFlowerPanel = new DrawFlowerPanel();
		add(drawFlowerPanel);
		this.setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体关闭模式
		this.setTitle("绘制花瓣"); // 设置窗体标题
	}
		
	// 主方法
	public static void main(String[] args) {
		new DrawFlowerFrame().setVisible(true);
	}
	
	// 画板
	class DrawFlowerPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			//平衡坐标轴，把(0, 0)坐标原点移到画板中中心位置
			g2.translate(drawFlowerPanel.getWidth() / 2, drawFlowerPanel.getHeight() / 2);
			
			//绘制绿色花瓣
			Ellipse2D.Float ellipse = new Ellipse2D.Float(30, 0, 70, 20);
			Color color = new Color(0, 255, 0);
			g2.setColor(color);
			g2.fill(ellipse);
			int i = 0;
			while (i < 8) {
				g2.rotate(30);//g2.rotate(Math.toRadians(30));这时候传入的是角度，则需要12个才能转满一圈。360度=2Pi弧度
				g2.fill(ellipse);
				++i;
			}
			
			//绘制红色花瓣
			ellipse = new Ellipse2D.Float(20, 0, 60, 15);
			color = new Color(255, 0, 0);
			g2.setColor(color);
			g2.fill(ellipse);
			i = 0;
			while (i < 15) {
				g2.rotate(75);
				g2.fill(ellipse);
				++i;
			}
			
			//绘制黄色花瓣
			ellipse = new Ellipse2D.Float(10, 0, 50, 15);
			color = new Color(255, 255, 0);
			g2.setColor(color);
			g2.fill(ellipse);
			i = 0;
			while (i < 8) {
				g2.rotate(30);
				g2.fill(ellipse);
				++i;
			}
			
			//绘制红色中心点
			color = new Color(255, 0, 0);
			g2.setColor(color);
			ellipse = new Ellipse2D.Float(-10, -10, 20, 20);
			g2.fill(ellipse);
		}
	}
}


20.5.2 绘制艺术图案 P532

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import javax.swing.*;

public class ArtDesignFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private ArtDesignPanel drawFlowerPanel = null;
	
	public ArtDesignFrame() {
		initialize(); // 调用初始化方法
	}
	
	// 界面初始化方法
	private void initialize() {
		drawFlowerPanel = new ArtDesignPanel();
		add(drawFlowerPanel);
		this.setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体关闭模式
		this.setTitle("绘制花瓣"); // 设置窗体标题
	}
		
	// 主方法
	public static void main(String[] args) {
		new ArtDesignFrame().setVisible(true);
	}
	
	// 画板
	class ArtDesignPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			//平衡坐标轴，把(0, 0)坐标原点移到画板中中心位置
			g2.translate(drawFlowerPanel.getWidth() / 2, drawFlowerPanel.getHeight() / 2);
			
			Ellipse2D.Float ellipse = new Ellipse2D.Float(-80, 5, 160, 10);
			Random random = new Random();
			
			//drawRandomColor(g2, random, ellipse);
			int i = 0;
			while (i < 100) {
				drawRandomColor(g2, random, ellipse);
				++i;
			}
		}
		
		private void drawRandomColor(Graphics2D g2, Random random, Ellipse2D.Float ellipse) {
			int R = random.nextInt(256);
			int G = random.nextInt(256);
			int B = random.nextInt(256);
			Color color = new Color(R, G, B);
			g2.rotate(10);
			g2.setColor(color);
			g2.draw(ellipse);
		}
	}
}
//------------------------------------------------------------------------------------------------
Eclipse导出的可执行jar包，用如下语句可直接运行：
java -jar ArtDesignFrame.jar
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第5课 Java核心语法详解
P168 所有已被private修饰符限定为私有的方法，以及所有包含在final类中的方法，都被默认地认为是final的。

P169 5.2.4 访问控制符
当一个类被声明为public时，它就具有了被其他包中的类访问的可能性，只要这些其他包中的类在程序中使用import语句引入public类，就可以访问和引用这个类。每个Java程序的主类都必须是public类，也是基于相同的原因。
//HelloJava.java
package mypackage;

public class HelloJava {
	public static void show() {
		HelloJavaImpl.show();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloJava.show();
	}
}

class HelloJavaImpl {
	public static void show() {
		System.out.println("hello, world!");
	}
}

//Test.java
import mypackage.HelloJava;
import mypackage.HelloJavaImpl;//编译错误，私有类将不可见，无法引用

public class Test
{
	public static void main(String args[])
	{
		HelloJava.show();
	}
}

P170 用protected修饰的成员变量可以被3种类所引用：该类自身、与它在同一个包中的其他类、在其他包中的该类的子类。使用protected修饰符的主要作用是允许其他包中该类的子类来访问父类的特定属性。

默认访问控制符，该类只能被同一个包中的类访问和引用，而不可以被其他包中的类使用，这种访问特性又称为包访问性。
同样道理，类内的变量或方法如果没有访问控制符来限定，也就具有包访问性。

P180 赋值使等号（=）左边的值等于右边的值，这一点对于基本数据类型（如前面的int a和b）是显尔易见的。对于非基本数据类型（如Point对象），赋值修改的是对象引用，而不是对象本身。因此，x和y引用同一个对象，所以对x执行的所有方法与对y执行的方法都作用于同一个对象。
import java.awt.Point;

public class Test
{
	public static void main(String args[])
	{
		int a = 1;
		int b = 2;
		Point x = new Point(0, 0);
		Point y = new Point(1, 1);
		System.out.println("a is " + a);
		System.out.println("b is " + b);
		System.out.println("x is " + x);
		System.out.println("y is " + y);
		
		System.out.println("改变位置...");
		a = b;
		a++;
		x = y;
		x.setLocation(5, 5);
		System.out.println("a is " + a);
		System.out.println("b is " + b);
		System.out.println("x is " + x);
		System.out.println("y is " + y);
	}
}
输出如下：
a is 1
b is 2
x is java.awt.Point[x=0,y=0]
y is java.awt.Point[x=1,y=1]
改变位置...
a is 3
b is 2
x is java.awt.Point[x=5,y=5]
y is java.awt.Point[x=5,y=5]

P188 当向方法传递一个基本数据类型时，传递的是一个值。当传递的是一个对象时，则按引用传递。

import java.io.IOException;

public class Test
{
	public static void main(String args[]) throws IOException {
		char a;
		outer: //必须先在这里定义，否则break outer和continue outer无法识别
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					a = (char) System.in.read();
					if (a == 'b') {
						break outer;//直接跳出所有循环
					}
					if (a == 'c') {
						continue outer;//跳到outer标号处
					}
				}
			}
	}
}
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第6课 Java面向对象编程
//6.1 封装 P203
public class HelloWorld
{
	private String world = "World";

	public void setWorld(String world)
	{
		this.world = world;
	}

	public void say()
	{
		System.out.println("Hello " + world + "!");
	}

	public static void main(String args[])
	{
		HelloWorld inst = new HelloWorld();
		inst.setWorld("China");
		inst.say();

		//main函数是HelloWorld中的函数，所以可以直接访问其私有变量world，在其他类中则无法用此方法来访问其私有变量，建议在可能的情况下尽可能使用方法调用
		String value = new HelloWorld().world;
		System.out.println(value);
	}
}

//6.2 继承 P204
6.2.1 继承的概念——超类和子类
class Car
{
	int v;
	void drive()
	{
		System.out.println("Car 速度：" + v);
	}
}

class Bus extends Car
{
	int p;
	void carry()
	{
		System.out.println("Bus 载人：" + p);
	}
	
	void sum()
	{
		System.out.println("Bus 速度：" + v);
		System.out.println("Bus 载人：" + p);
	}
}

public class TestCar
{
	public static void main(String args[])
	{
		Car car = new Car();
		car.v = 60;
		Bus bus = new Bus();
		bus.v = 40;
		bus.p = 20;
		
		car.drive();
		bus.drive();
		bus.carry();
		bus.sum();
	}
}
输出如下：
Car 速度：60
Car 速度：40
Bus 载人：20
Bus 速度：40
Bus 载人：20

6.2.2 子类不能访问私有成员与方法
一个被定义成private的类成员为此类私有，它不能被该类外的所有代码访问，包括子类。

6.2.3 父类与子类的相互转换 P207
可以将子类的引用赋给父类的对象，也可以再通过强制类型转换将父类再转换成子类类型。
Bus bus = new Bus();
Car car = bus;
Bus bus2 = (Bus)Car;

6.2.4 使用this和super
（1）this
引用当类的变量或方法
（2）super
它引用当前对象的直接父类中的成员（用来访问直接父类中被隐藏的父类中成员数据或函数，基类与派生类中有相同成员定义时）。
（3）this(实参)
引用当前类的构造函数
（4）super(参数)
调用基类中的某一个构造函数

6.2.5 多态——方法的覆盖（override）、重载（overload）、重写（overwrite）

6.2.6 必须被继承的类——抽象类（abstract）
abstract class Car//只有抽象类才能定义抽象方法，所以这里的abstract不能缺少
{
	int v;
	abstract void drive();//这里的abstract可以不定义，则必须要给drive()定义实现，但是要求Car类必须继承
}

class Bus extends Car
{
	int p;
	void drive()//这里不能再加abstract
	{
		System.out.println("Bus 速度：" + v);
	}
	
	void carry()
	{
		System.out.println("Bus 载人：" + p);
	}
	
	void sum()
	{
		System.out.println("Bus 速度：" + v);
		System.out.println("Bus 载人：" + p);
	}
}

public class TestCar
{
	public static void main(String args[])
	{
		Bus bus = new Bus();
		bus.v = 40;
		bus.p = 20;
		
		bus.drive();
		bus.carry();
		bus.sum();
	}
}

6.2.7 不能被继承的类——最终类（final）
1. final类的定义
--final类不能被继承，没有子类，final类中的方法默认是final的
--final方法不能被子类的方法覆盖，但可以被继承
--final成员变量表示常量，只能被赋值一次，赋值后值不再改变
--final不能用于修饰构造方法
父类的private成员方法是不能被子类方法覆盖的，因此private类型的方法默认是final类型的。（这句话不是很对）
class Car
{
	int v;
	private void drive()//final private void drive()这样定义也可以，但是如果定义为final public void drive()则子类无法覆盖
	{
		System.out.println("Car drive");
	}
}

class Bus extends Car
{
	int p;
	void drive()//可以这样定义drive
	{
		System.out.println("Bus 速度：" + v);
	}
	
	void carry()
	{
		System.out.println("Bus 载人：" + p);
	}
	
	void sum()
	{
		System.out.println("Bus 速度：" + v);
		System.out.println("Bus 载人：" + p);
	}
}

public class TestCar
{
	public static void main(String args[])
	{
		Bus bus = new Bus();
		bus.v = 40;
		bus.p = 20;
		
		bus.drive();
		bus.carry();
		bus.sum();
	}
}

2. final方法的定义 P214
如果一个类不允许子类覆盖某个方法，则可以把这个方法声明为final方法。
使用final方法原因有两个：
--把方法锁定，防止任何继承类修改它的意义和实现。
--高效，编译器在遇到调用final方法时会转入内嵌机制，大大提高执行效率。
对于final static类型成员变量，static使得变量只存在一个副本，final使得它不能改变。
3. 关于final成员赋值的规定
--final成员能且只能被初始化一次
--final成员必须在声明时或者在构造方法中被初始化，而不能在其他的地方被初始化。

6.3 多重继承——接口类 P214
Java语言规范中，一个方法的特征仅包括方法的名字、参数的数目和类型，而不包括方法的返回类型、参数的名字及所抛出来的异常。在Java编译器检查方法的重载时，会根据这些条件判断两个方法是否是重载方法。但在Java编译器检查方法的转换时，则会进一步检查两个方法（分超类型和子类型）的返回值类型和抛出的异常是否相同。
接口继承和实现继承的规则不同，一个类只有一个直接父类，但可以实现多个接口。

6.3.2 接口的创建与实现
接口特性如下：
--接口中的方法可以有参数列表和返回类型，但不能任何方法体。
--接口中可以包含字段，但是会被隐式地声明为static和final。
--接口中的字段只是被存储在该接口的静态存储区域内，而不属于该接口。
--接口中的方法可以被声明为public或不声明，但结果都会按照public类型处理。
--实现一个接口时，需要将被定义的方法声明为public类型，否则为访问类型，Java编译器不允许这种情况。
--如果没有实现接口中的所有方法，那么创建的仍然是一个接口。
--扩展一个接口来生成新的接口应使用关键字extends，实现一个接口使用implements。
Java接口可以是public的，静态的和final属性。
public interface 接口名
{
	public static int a = 1;
	public final int b = 2;
}
可以通过implements关键字实现多个接口，那么该类必须实现这些接口的所有接口方法。

6.3.3 接口与抽象类的区别 P216
（1）抽象类可以提供实现方法，接口不能提供
（2）抽象类只能继承一个，而可以实现多个接口
结合（1）（2）点中抽象类和Java接口的各自优势，最精典的设计模式就出来了：声明类型的工作仍然由Java接口承担，但是同时给出一个Java抽象类，且实现了琮个接口，而其他同属于这个抽象类型的具体类可以选择实现这个Java接口，也可以选择继承这个抽象类，也就是说在层次结构中，Java接口在最上面，然后紧跟着抽象类，这就将两者的最大优点都能发挥到极致了。这个模式就是“默认适配模式”。
//(1)定义顶层接口
public interface ClassName1
{
	public void func1();
}
//(2)定义实现抽象类
public abstract class ClassName2
{
	public void func2()
	{
		
	}
}
//(3)定义实现类
public class ClassName3 extends ClassName2 implements ClassName1
{
	public void func1()//实现接口函数
	{
		func2();//调用抽象类函数
	}
}

接口在某些地方和抽象类有相似的地方，但是采用哪种方式来声明类主要参照以下两点：
--如果要创建不带任何方法定义和成员变量的基类，那么就应该选择接口而不是抽象类。
--如果知道某个类应该是基类，那么第一个选择的应该是让它成为一个接口，只有在必须要有方法定义和成员变量的时候，才应该选择抽象类。因为抽象类中允许存在一个或多个被具体实现的方法，只要方法没有被全部实现该类就仍是抽象类。

6.4.4 上机作业 P220
写一个计算器，实现加，减，乘，除
//（1）计算器接口类
package calculator;

public interface ICalculator
{
	public int calculate(String expression);
}

//（2）计算器抽象类
package calculator;

public abstract class AbstractCalculator
{
	public int[] split(String expression, String deliString)
	{
		String array[] = expression.split(deliString);
		int arrayInt[] = new int[2];
		arrayInt[0] = Integer.parseInt(array[0]);
		arrayInt[1] = Integer.parseInt(array[1]);
		return arrayInt;
	}
}

//（3）计算器实现类：加、减、乘除，以及默认计算类
//加法计算类
package calculator.impl;

import calculator.ICalculator;
import calculator.AbstractCalculator;

public class Plus extends AbstractCalculator implements ICalculator
{
	@Override
	public int calculate(String expression)
	{
		int arrayInt[] = split(expression, "\\+");//String.split函数中的入参是正则表达式，+和*有特殊含义，所有要用将其转义
		return arrayInt[0] + arrayInt[1];
	}
}

//减法计算类
package calculator.impl;

import calculator.ICalculator;
import calculator.AbstractCalculator;

public class Minus extends AbstractCalculator implements ICalculator
{
	@Override
	public int calculate(String expression)
	{
		int arrayInt[] = split(expression, "-");
		return arrayInt[0] - arrayInt[1];
	}
}

//乘法计算类
package calculator.impl;

import calculator.ICalculator;
import calculator.AbstractCalculator;

public class Multiply extends AbstractCalculator implements ICalculator
{
	@Override
	public int calculate(String expression)
	{
		int arrayInt[] = split(expression, "\\*");//String.split函数中的入参是正则表达式，+和*有特殊含义，所有要用将其转义
		return arrayInt[0] * arrayInt[1];
	}
}

//除法计算类
package calculator.impl;

import calculator.AbstractCalculator;
import calculator.ICalculator;

public class Devide extends AbstractCalculator implements ICalculator
{
	@Override
	public int calculate(String expression)
	{
		int arrayInt[] = split(expression, "/");
		return arrayInt[0] / arrayInt[1];
	}
}

//默认计算类
package calculator.impl;

import calculator.AbstractCalculator;
import calculator.ICalculator;

public class Default extends AbstractCalculator implements ICalculator
{
	@Override
	public int calculate(String expression)
	{
		System.out.println("Input is illegal!");
		return 0;
	}
}

//（4）测试类
package calculator;

import java.io.Console;
import java.util.Scanner;

import calculator.impl.Plus;
import calculator.impl.Minus;
import calculator.impl.Multiply;
import calculator.impl.Devide;
import calculator.impl.Default;

public class Test
{
	public static void main(String args[])
	{
		while (true)
		{
			//System.out.println("Ready to input:");
			//String expression = System.console().readLine();//书中的这种方法抛出了空指针异常
			String expression = readDataFromConsole("Ready to input:");
			if (expression.equals("exit"))
			{
				break;
			}
			
			ICalculator calculator;
			if (expression.indexOf("+") != -1)
			{
				calculator = new Plus();
			}
			else if (expression.indexOf("-") != -1)
			{
				calculator = new Minus();
			}
			else if (expression.indexOf("*") != -1)
			{
				calculator = new Multiply();
			}
			else if (expression.indexOf("/") != -1)
			{
				calculator = new Devide();
			}
			else
			{
				calculator = new Default();
			}
			
			int value = calculator.calculate(expression);
			System.out.println(expression + "=" + value);
		}
	}
	
	private static String readDataFromConsole(String prompt)
	{
//		Console console = System.console();
//        if (console == null) //编译能过，运行时每次都为null，抛异常
//        {  
//            throw new IllegalStateException("Console is not available!");  
//        }  
//        return console.readLine(prompt);  
		Scanner scanner = new Scanner(System.in);//有个scanner未close的提示
		System.out.println(prompt);  
		return scanner.nextLine();  
	}
}
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第7课 Java面向对象编程扩展 P225
7.1.1 静态类与非静态类的区别 P225
静态的类都是在运行时静态地加载到内存中的（一开始运行首先从静态的类开始），所以也不需要对它们进行初始化，也没有实例，因此在类的内部也不能用this。
如果是静态内部类的话，静态内部类不能操作访问外部数据，静态内部类的实例生成不需要从外部生成相关类。

7.1.2 静态方法
调用一个静态方法就是“类名.方法名”
一般来说，静态方法常常为应用程序中的其他类提供一些实用工具所用，在Java的类库中大量的静态方法正是出于此目的而定义的。

7.1.4 静态代码块
static变量有点类似于C中的全局变量的概念，值得探讨的是静态变量的初始化问题。
静态代码块在系统启动时就执行了输出，在static {后面跟着一段代码，这是用来显式地初始化静态变量，这段代码只会初始化一次，且在类被第一次装载时。
另外，static定义的变量会优先于任何其他非static变量，不论其出现的顺序如何。并且在涉及到继承的时候，会先初始化父类的static变量，然后是子类的，以此类推。
class StaticClass
{
	public static int i = 0;
	public StaticClass()
	{
		i = 15;
	}
	public StaticClass(int n)
	{
		i = n;
	}
	public static void inc()
	{
		i++;
	}
}

public class Test
{
	StaticClass v = new StaticClass(10);
	static StaticClass v1, v2;
	
	//静态代码块在系统启动时执行
	static
	{
		System.out.println("初始化：v1.c = " + v1.i + ", v2.i = " + v2.i);
		v1 = new StaticClass(27);
		System.out.println("初始化：v1.c = " + v1.i + ", v2.i = " + v2.i);
		v2 = new StaticClass(15);
		System.out.println("初始化：v1.c = " + v1.i + ", v2.i = " + v2.i);
	}
	
	public static void main(String args[])//main方法必须定义在与文件名称一致的public类中
	{
		Test test = new Test();
		System.out.println("test.i = " + test.v.i);
		System.out.println("v1.c = " + v1.i + ", v2.i = " + v2.i);
		v1.inc();
		System.out.println("v1.c = " + v1.i + ", v2.i = " + v2.i);
		System.out.println("test.i = " + test.v.i);
	}
}
输出如下：
初始化：v1.c = 0, v2.i = 0
初始化：v1.c = 27, v2.i = 27
初始化：v1.c = 15, v2.i = 15
test.i = 10
v1.c = 10, v2.i = 10
v1.c = 11, v2.i = 11
test.i = 11


7.1.5 静态内部类
static在类的内部定义静态变量、静态方法和静态代码块的使用情形，它还可以修饰class的定义。
通常一个普通类不允许声明为静态的，只有一个内部类才可以。这时这个声明为静态的内部类可以直接作为一个普通类来使用，而不需要实例化一个外部类。
如下代码所示，定义了一个静态内部类InnerClass，只需要通过“外部类.内部类”的形式引用该类
class Test
{
	//定义一个静态内部类
	public static class InnerClass
	{
		public InnerClass()
		{
			System.out.println("InnerClass");
		}
		public void print(String string)
		{
			System.out.println(string);
		}
	}
	
	public static void main(String args[])
	{
		Test.InnerClass ic = new Test.InnerClass();//引用静态内部类
		ic.print("this is a string");
	}
}
输出如下：
InnerClass
this is a string

7.2 Java匿名类（Anonymous Class） P228
public class A
{
	public void f()
	{
		class B extends Thread//定义一个内部类
		{
			public void run() {...}//具体实现
		}

		class C implements Runnable//定义一个内部接口
		{
			public void run() {...}//具体实现
		}
		
		B b = new B();
		C c = new C();
	}
}
在方法中再定义内部类往往显得累赘，于是上面的代码就可以用匿名内部类重写成：
public class A
{
	public void f()
	{
		Thread b = new Thread()//匿名内部类
		{
			public void run() {...}//具体实现
		}
		
		Runnable c = new Runnable()//匿名内部接口
		{
			public void run() {...}//具体实现
		}
	}
}
匿名内部类通过用在GUI编程添加事件监听器中，目的是简化代码编写。

7.2.1 匿名类的定义 P229
从技术上说，匿名类可被视为非静态的内部类，所以它们具有和方法内部声明的非静态内部类一样的权限和限制。如果要执行的任务需要一个对象，但却不值得创建全新的对象（原因可能是所需的类过于简单，或者是由于它只在一个方法内部使用），匿名类就显得非常有用。匿名类尤其适合在Swing应用程序中快速创建事件处理程序。
匿名类是不能有名称的类，所以没办法引用它们。必须在创建时，作为new语句的一部分来声明它们。这就要采用另一种形式的new语句，如下：
new<类或接口><类的主体>
这种形式的new语句声明一个新的匿名类，它对一个给定的类进行扩展，或者实现一个给定的接口。它还创建那个类的一个新实例，并把它作为语句的结果而返回。要扩展的类和要实现的接口是new语句的操作数，后跟匿名类的主体。如果匿名类对另一个类进行扩展，它的主体可以访问类的成员、覆盖它的方法等，这和其他任何标准的类都是一样的。如果匿名类实现了一个接口，它的主体必须实现接口的方法。
匿名类的声明是在编译时进行的，实例化在运行时运行。这意味着for循环中的一个new语句会创建相同的匿名类的几个实例，而不是创建几个不同匿名类的一个实例。

7.2.2 匿名类的使用实例
7.2.3 使用匿名类处理Swing事件

7.3 Java内部类（Inner Class）P231
匿名类，属于内部类的一种。内部类即是定义在另一个类的内部的类。Java内部类（Inner Class），实际上类似的概念在C++里就是嵌套类（Nested Class）。

7.3.1 内部类的含义
“内部类”是在另一个类的内部声明的类。从Java1.1 开始，可在一个类中声明另一个类。包装了内部类声明的类就称为“外部类”。Java语言规范允许定义以下几种内部类：
--在另一个类或者一个接口中声明一个类
--在另一个接口或者一个类中声明一个接口（可以不用实现接口中的内部接口）
--在一个方法中声明一个类
--类和接口声明可以嵌套任意深度

内部类是在一个类的内部嵌套定义的类，它可以是其他类的成员，也可以在一个语句块的内部定义，还可以在表达式内部匿名定义。
内部类有如下特性：
--一般用在定义它的类或语句块之内，在外部引用它时必须给出完整的名称，名字不能与包含它的类名相同。
--可以使用包含它的类的静态和实例成员变量，也可以使用它所在的方法的局部变量。
--可以定义为abstract
--可以声明为private或protected
--若被声明为static，就变成了顶层类，不能再使用局部变量。
--若想在Inner Class中声明任何static成员则该Inner Class必须声明为static。

（1）内部类的可视范围
内部类的可视范围是它的直接外嵌类（这一点和嵌套静态类不同，嵌套静态类在其外嵌类之外也是可视的），也就是说内部类可以直接引用外嵌类中的任何成员。因为内部类五外嵌类的实例相关，所以内部类拥有两个this指针，一个指向内部类的实例本身，一个指向外嵌类实例。
（2）使用内部类的好处
封装性好：直接通过操作得到内部类内容，甚至连这个内部类名字都没有看见。一个内部类对象可以访问创建它的外部类对象的内部，甚至包括私有变量。
在Java中的内部类和接口加在一起，可以解决常被C++程序员抱怨Java中存在的一个问题——没有多继承。实际上，C++的多继承设计起来很复杂，而Java通过内部类另上接口，可以很好地实现多继承的效果。
（3）内部类的用途
内部类的一个主要用途是用于GUI的事件处理程序

7.3.2 内部类的使用实例 P232

interface IContent
{
	String getContent();
}

interface IAddress
{
	String getAddress();
}

class Mail
{
	//邮件内部内部类
	private class Content implements IContent
	{
		private String content;
		
		private Content(String content)
		{
			this.content = content;
		}
		
		public String getContent()
		{
			return content;
		}
	}
	
	//接收地址内部类
	protected class Address implements IAddress
	{
		private String address;
		
		private Address(String address) 
		{
			this.address = address;
		}
		
		public String getAddress()
		{
			return address;
		}
	}
	
	public IContent getContent(String s)
	{
		return new Content(s);
	}
	
	public IAddress getAddress(String s)
	{
		return new Address(s);
	}
}


public class TestMail
{
	public static void main(String args[])//main方法必须定义在与文件名称一致的public类中
	{
		Mail mail = new Mail();
		IContent content = mail.getContent("这是邮件正文");//取得邮件正文实例
		IAddress address = mail.getAddress("bigben0204@163.com");//取得接收地址实例
	}
}
在这个例子里，类Content和Address被定义在了类Mail内部，并且由protected和private修饰符来控制访问级别。在后面的mail方法里，直接用IContent content和IAddress address进行操作，你甚至连这两个内部类的名字都没有看见。这样，内部类的第一个好处就体现出来了——隐藏了你不想让别人知道的操作，好封装性。
同时，也发现了在外部类作用范围之外得到内部类对象的一第一个方法，那就是利用其外部类的方法创建并返回。


7.3.3 局部内部类 P233
前面定义的内部类是在类内部定义的，还可以是局部的，即它可以定义在一个方法，或者一个代码块之内。
当内部类定义放在函数内部时，class前不能够使用private、protected和public修饰符，因为此时的类仅仅在该代码块作用可见，不需要对外部开发可视权限。
interface IContent
{
	String getContent();
}

interface IAddress
{
	String getAddress();
}

class Mail
{
	public IContent getContent(String s)
	{
		//邮件内部内部类
		class Content implements IContent//如果定义成局部类，则不能用修饰符public/protected/private，只能用abstract/final
		{
			private String content;
			
			private Content(String content)
			{
				this.content = content;
			}
			
			public String getContent()
			{
				return content;
			}
		}
		
		return new Content(s);
	}
	
	public IAddress getAddress(String s)
	{
		//接收地址内部类
		class Address implements IAddress
		{
			private String address;
			
			private Address(String address) 
			{
				this.address = address;
			}
			
			public String getAddress()
			{
				return address;
			}
		}
		
		return new Address(s);
	}
}


public class TestMail
{
	public static void main(String args[])//main方法必须定义在与文件名称一致的public类中
	{
		Mail mail = new Mail();
		IContent content = mail.getContent("这是邮件正文");//取得邮件正文实例
		IAddress address = mail.getAddress("bigben0204@163.com");//取得接收地址实例
	}
}

还可以将内部类放在代码块中，如果s不为空则定义内部类并返回实例，否则返回空。不能在if之外创建这个内部类的对象，因为这已经超出了它的作用域。不过在编译的时候，内部类Content、Address和其他类一样同时被编译，只不过它有自己的作用域，超出了这个范围就无效，除此之外它和其他内部类并没有什么区别。

7.3.4 内部类引用外部类对象 P235
根据内部类的含义可知，内部类的对象还可以引用外部对象。例子不写了，内部类引用外部类的私有数据成员变量。
一个内部类对象可以访问创建它的外部类对象的内容，甚至包括私有变量！这是一个非常有用的特性。要想实现这个功能，内部类对象就必须有指向外部类对象的引用。Java编译器创建内部类对象时，隐式地把其外部类对象的引用也传了进去并一直保存着。这样就使得内部类对象始终可以访问其外部类对象，同时这也是为什么在外部类作用范围之外要创建内部类对象必须先创建其外部类对象的原因。
如果内部类里的一个成员变量与外部类的一个成员变量同名，即外部类的同名成员变量被屏蔽了，Java里用如下格式表达外部类的引用：
外部类名.this.外部变量名
对于上面的变量，如果使用与Address内部同名的变量address，那么在Address内部需要使用“Mail.this.address”来引用它。

在创建非静态内部类对象时，一定要先创建相应的外部类对象，因为非静态内部类对象有着指向其外部类对象的引用。

和普通的类一样，内部类也可以有静态的。不过和非静态内部类相比，区别就在于静态内部类没有了指向外部的引用。这实际上和C++中的嵌套类很相似了，Java内部类与C++嵌套类最大的不同就在于是否有指向外部的引用这一点上。
除此之外，在任何非静态内部类中，都不能有静态数据、静态方法或者另外一个静态内部类（内部类的嵌套可以不止一层）。不过静态内部类却可以拥有这一切。

7.4 Java异常类（Exception Class） P237
在编程过程中，首先应当尽可能去避免错误和异常发生，对于不可避免、不可预测的情况则考虑在异常发生时如何处理。Java对异常的处理是按异常分类处理的，不同异常有不同的分类，每种异常都对应一个类型（class），每个异常都对应一个异常（类的）对象。
--异常类有两个来源：一是Java语言本身定义的一些基本异常类型；二是用户通过继承Exception类或者子类自己定义的异常。
--异常的对象有两个来源：一是Java运行时环境自动抛出系统生成的异常，而不管你是否愿意捕获和处理，它总要被抛出，如除数为0的异常；二是程序员自己抛出的异常，这个异常可以是程序员自己定义的，也可以是Java语言中定义的，用throw关键字抛出异常，这种异常常常用来向调用者汇报异常的一些信息。异常是针对方法来说的，抛出、声明抛出、捕获和处理异常都是在方法中进行的。

7.4.1 Java的异常层次结构
Java把异常当做对象来处理，并定义一个基类java.lang.Throwable作为所有异常的超类。Java API中定义的异常类分为两大类，即错误Error和异常Exception。
Throwable类是所有异常和错误的超类，它有两个子类Error和Exception，分别表示错误和异常。其中异常类Exception又分为运行时异常（Runtime Exception）和非运行时异常，这两种异常有很大区别，也称为不检查异常（Unchecked Exception）和检查异常（Checked Exception）。
1. Error与Exception
--Error是程序无法处理的错误，比如OutOfMemory Error、ThreadDeath等。这些异常发生时，Java虚拟机（JVM）一般会选择线程终止。常见的有程序进入死循环、内存泄漏等。这种情况，程序运行时本身无法解决，只能通过其他方法干预。
--Exception是程序本身可以处理的异常，这种异常分为运行时异常和非运行时异常。在程序中应当尽可能去处理这些异常。常见的有除数为0、数组越界等。这种情况，不像错误那样，程序运行时本身可以解决，由异常代码调整程序运行方向，使程序仍可继续运行直至直至正常结束。
2. 运行时异常和非运行时异常

每一个类的含义P230
（1）Throwable getCause()/getMessage()/printStackTrace()
（2）Exception
（3）Error
（4）RuntimeException
（5）ThreadDeath

--算术异常类：ArithmeticException
--空指针异常类：NullPointerException
--类型强制转换异常：ClassCassException
--数组负下标异常：NegativeArrayException
--数组下标越界异常：ArrayIndexOutOfBoundsException
--违背安全原则异常：SecurityException
--文件已结束异常：EOFException
--文件未找到异常：FileNotFoundException
--字符串转换为数字异常：NumberFormatException
--操作数据库异常：SQLException
--输入/输出异常：IOException
--方法未找到异常：NoSuchMethodException

7.4.2 异常的捕捉处理机制 P240
1. 异常处理的基本语法
try、catch、finally、throw、throws
finally语句块：紧跟catch语句后的语句块，这个语句块总是会在方法返回前执行，而不管try语句块是否发生异常，目的是给程序一个补救的机会，体现了Java语言的健壮性。
//如下，即使未进入catch语句块中，finally中的语句还是会执行到
public class TestMail
{
	public static void main(String args[])//main方法必须定义在与文件名称一致的public类中
	{
		int r = 30;
		try
		{
			if (r <= 20)
			{
				throw new Exception("程序异常：\n半径为：" + r + "\n半径不能小于20。");
			}
			else
			{
				return;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			System.out.println("before return");
		}
	}
}
输出如下：
before return

再如下，如果没有发生异常，则finally语句还是会按顺序执行
public class TestMail
{
	public static void main(String args[])//main方法必须定义在与文件名称一致的public类中
	{
		int r = 30;
		try
		{
			if (r <= 20)
			{
				throw new Exception("程序异常：\n半径为：" + r + "\n半径不能小于20。");
			}
//			else
//			{
//				return;
//			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//return;
		}
		finally
		{
			System.out.println("before return");
		}
		System.out.println("last sentence");
	}
}
输出如下：
before return
last sentence

2. try、catch、finally语句块应注意的问题
--try、catch、finally 3 个语句块均不能单独使用，3 者可以组成try...catch...finally、try...catch、try...finally3种结构，catch语句可以有一个或多个，finally语句最多一个。
--try、catch、finally 3 个代码块中变量的作用域为代码块内部，分别独立而不能相互访问。如果要在3个块中都可以访问，则需要将变量定义到这些块的外面。
--使用多个catch块时，只会匹配其中一个异常类并执行catch块代码，而不会再执行别的catch块，并且匹配catch语句的顺序是由上到下的。

3. throw、throws关键字
throw关键字是用于方法体内部，用来抛出一个Throwable类型的异常。
throws关键字用于方法体外部的方法声明部分，用来声明方法可能会抛出某些异常。仅当抛出了检查异常，该方法的调用者才必须处理或者重新抛出该异常。当方法的调用者无力处理该异常的时候，应该继续抛出，而不是囫囵吞枣一般在catch块中打印一下堆栈信息进行勉强处理。
--throw是语句抛出一个异常；throws是方法抛出一个异常。方法抛出异常类可以声明多个，用逗号分隔。
--throws可以单独使用，throw不能。
--throw要么和try...catch...finally语句配套使用，要么与throws配套使用。但throws可以单独使用，然后再由处理异常的方法捕获。
一个简单的例子：
public static void test() throws Exception
{
	throw new Exception("方法test()中的Exception");
}

7.4.3 使用异常和自定义异常 P241
1. 使用已有的异常类
try
{
	
}
catch(IOException ioe)
{
	
}
catch(SQLException sqle)
{
	
}
finally
{
	
}
2. 自定义异常类
public class MyException extends Exception
{
	public MyException() {}
	public MyException(String msg)
	{
		super(msg);
		System.out.println(msg);
	}
}
在异常类中，可以覆盖或重载父类Exception中的函数，用心实现自己的代码。
3. 使用自定义的异常
用throws声明方法可能抛出自定义的异常，并用throw语句在适当的地方抛出自定义的异常。
public void test() throws MyException
{
	...
	if (...)
	{
		throw new MyException();
	}
}
也可以将异常转型（也叫转译），使得异常更易读易于理解。所谓转型，即在捕捉到某一个异常时，抛出另外的一个自定义的异常，这样是为了外部更加统一地接收异常。
public void test() throws MyException
{
	...
	try
	{
		...
	}
	catch (SQLException e)
	{
		...
		throw new MyException();
	}
}

7.5.4 上机作业参考 P245
（1）添加异常类：
//CalculatorException.java
package calculator;

public class CalculatorException extends Exception
{
	public CalculatorException(String msg)
	{
		super("数字转换异常：" + msg);
	}
}

（2）捕获AbstractCalulator中的java.lang.NumberFormatException异常，并转译成CalculatorException异常抛出。
package calculator;

public abstract class AbstractCalculator
{
	public int[] split(String expression, String deliString) throws CalculatorException
	{
		String array[] = expression.split(deliString);
		int arrayInt[] = new int[2];
		
		try
		{
			arrayInt[0] = Integer.parseInt(array[0]);
			arrayInt[1] = Integer.parseInt(array[1]);
		}
		catch (NumberFormatException e)
		{
			throw new CalculatorException(e.getMessage());
		}
		
		return arrayInt;
	}
}

（3）加减乘除计算因增加计算函数抛出异常处理。
package calculator.impl;

import calculator.CalculatorException;
import calculator.ICalculator;
import calculator.AbstractCalculator;

public class Plus extends AbstractCalculator implements ICalculator
{
	@Override
	public int calculate(String expression) throws CalculatorException
	{
		int arrayInt[] = split(expression, "\\+");//String.split函数中的入参是正则表达式，+和*有特殊含义，所有要用将其转义
		return arrayInt[0] + arrayInt[1];
	}
}

接口类ICalulator的接口函数抛出异常：
package calculator;

public interface ICalculator
{
	public int calculate(String expression) throws CalculatorException;
}

（4）测试类Test中捕捉计算类抛出的自定义异常，输出到控制台：
package calculator;

import java.io.Console;
import java.util.Scanner;

import calculator.impl.Plus;
import calculator.impl.Minus;
import calculator.impl.Multiply;
import calculator.impl.Devide;
import calculator.impl.Default;

public class Test
{
	public static void main(String args[])
	{
		while (true)
		{
			//System.out.println("Ready to input:");
			//String expression = System.console().readLine();
			String expression = readDataFromConsole("Ready to input:");
			if (expression.equals("exit"))
			{
				break;
			}
			
			ICalculator calculator;
			if (expression.indexOf("+") != -1)
			{
				calculator = new Plus();
			}
			else if (expression.indexOf("-") != -1)
			{
				calculator = new Minus();
			}
			else if (expression.indexOf("*") != -1)
			{
				calculator = new Multiply();
			}
			else if (expression.indexOf("/") != -1)
			{
				calculator = new Devide();
			}
			else
			{
				calculator = new Default();
			}
			
			try
			{
				int value = calculator.calculate(expression);
				System.out.println(expression + "=" + value);
			}
			catch (CalculatorException e)
			{
				System.out.println(e);
			}
		}
	}
	
	private static String readDataFromConsole(String prompt)
	{ 
		Scanner scanner = new Scanner(System.in);  
        System.out.println(prompt);  
        return scanner.nextLine();  
	}
}
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第8课 Java编码规范与样式 P249
8.1.1 文件的命名
如果在源程序中包含有公共类的定义，则该源文件名必须与该公共类的名字完全一致，字母的大小写都必须一样。这是Java语言的一个严格的规定，如果不遵守，在编译时就会出错。因此，在一个Java源程序中至多只能有一个公共类的定义。如果源程序中不包含公共类的定义，则该文件名可以任意取名。如果在一个源程序中有多个类定义，则在编译时将为每个类生成一个.class文件。
//NoPublicClass.java
package calculator;

class DbTest
{
	public void show()
	{
		System.out.println("this is dbTest");
	}
}

class DbTest2
{
	public void show()
	{
		System.out.println("this is dbTest2");
	}
}

//Test.java
package calculator;

public class Test
{
	public static void main(String args[])
	{
		DbTest dbTest = new DbTest();//在同一个包中的源程序类可以直接引用，即便没有加public访问控制符
		dbTest.show();
	}
}
类的访问控制符只有public、abstract、final三种。后两种不说了，public类才能被其他包import进并使用，不加public则无法被其他包使用，只能在本包中使用。

8.1.2 包的命名
包的命名采用完整的英文描述符，应该都是由小写字母组成的。由于Java面向对象编程的特性，每一名Java程序员都可以编写属于自己的Java包，为了保障每个Java包命名的唯一性，要求程序员在自己定义的包的名称之前加上唯一的前缀。
包名按照域名的范围从大到小逐步列出，恰好和Internet上的域名命名规则相反，如：
net.sourceforge
com.ibm.util
com.sun.eng
com.apple.quicktime

8.1.3 类的命名 P250
类名是一个名词，采用大小写混合的方式，每个单词的首字母大写。
--类名由大写字母形状，其他字母小写，如Test
--类名由多个单词组成，每个单词首字母均应大写，如TestPage
--如果类名中包含单词缩写，则缩写词的每个字母均应大写，如XMLExample（个人感觉XmlExample更好一些）
还有一点命名技巧，由于类是设计用来代表对象的，所以在命名类时应尽量选择名词。

8.1.4 变量的命名
小写字母开头，后面单词用大写字母开头

8.1.5 常量的命名
类的常量是使用static final 来修饰的变量。但为了与普通的变量相区别，常量的名字应该都使用大写字母，并且指出该常量完整含义。如果其中包含多个单词，要以“_”连接，如：
static final int MIN_WIDTH = 4;
static final int MAX_WIDTH = 999;
static final int GET_THE_CPU = 1;

Tips：在Java代码中，无论什么时候，均提倡使用常量取代数字、固定字符串。也就是说，程序中除0、1以外，尽量不应该出现其他数字。常量可以集中在程序开始部分定义，或者在更宽的作用域内定义，如静态类的静态变量。

8.1.6 数组的命名 P251
数组也是变量，因此首先应该遵循变量的命名规则。数组应该总是用下面的方式来命名的：
byte[] buffer;
而不是：
byte buffer[];//但是是在C++中只有这种数组命名方式，所以统一这样命名不可以吗？

8.1.7 方法的命名
方法名的定义应该使用有意义的标识符。Java中方法名以小写字母开头，名字一般都使用动宾结构的数组，开始的动词字母小写，后跟一个首字母大写的名词，也可以在动宾结构中加入修饰词。
所有的布尔型获取函数必须用单词is做前缀，如：
public boolean isPersistent();
public boolean isString();

8.2 Java排版规范 P252
8.2.1 空格
8.2.2 空行
空行将逻辑相关的代码段分隔开，以提高可读性。
--如果一个文件中有几个类，类之间必须使用空行（建议用2个空行）。
--类的成员变量和类方法之间必须有空行（建议用2个空行）。
--在两个方法之间必须使用空行进行分隔（建议用2个空行）。
--对于那些属于同一语句块，但从逻辑上讲完成了不同功能的语句，应该用一个空行分开。
Tips：}语句永远单独作为一行。

8.2.3 缩进 P255
缩进排版通常以4个空格作为一个单位
--每个语句块向后缩进4个空格位置
--在语句很长的情况下，关于换行的缩进
	每一行Java代码都应该限制在80个字符以内。当一行代码过长时，要考虑折行。折行后，第2行的开头应当与第1行语句同等地位的词缩进对齐（使用“Tab”键无法对齐时，不要减小缩进的空格数目，而是错开，以“Tab”键缩进的空格为准）。换多行时，第2行以后的各行一定要对齐。
--注释应该和被注释的代码的缩进列数相同

8.2.4 页宽
尽量避免一行的长度超过80个字符。在任何情况下，越长的语句应该在一个逗号或者一个操作符后折行。

8.3 Java注释规范

8.4 Java文件样式 P257
8.4.2 包与引入
如果有package行，则它必须出现在除注释外的第一行。import行要跟在package的后面，之间空一行。import中标准的包名要在本地的包名之前，而且按照字母顺序排序。如果import行中包含了同一个包中的不同子目录，则应该用*来处理。
8.4.6 构造函数
它应该用递增的方式写（比如，参数多的写在后面）

8.4.8 toString()方法
一般情况下，每一个类都应该定义toString()方法，方便外部的类调用输出
public String toString()
{}

//------------------------------------------------------------------------------------------------
//Java核心编程技术 第9课 Java输入/输出流 P267
9.1.1 标准输入/输出类System
System类包含3个静态成员变量，都是关于输入和输出的变量。
static PrintStream out;//标准输出流
static PrintStream err;//标准错误输出流
static InputStream in;//标准输入流

1. out标准输出流——输出消息到控制台
System.out.println(data);
out对象的类型是PrintStream。该类提供了一系列的是print()函数，用于输出各种类型的数据，包括基本数据类型、对象数据类型和数组，函数列表如下：
void print(boolean b);
void print(char c);
void print(char[] s);//打印字符数组
void print(double d);
void print(float f);
void print(int i);
void print(long l);
void print(Object obj);
void print(String s);
另外，还提供了对应的换行输出函数 
void println(xxx xxx);
如果要输出一个换行，则可以直接使用下面的函数：
void println();
除此之外，还可以使用下面的方法直接输出字节数据（字节数据就是本章后面将要讲到的字节流）：
void write(byte[] buf, int off, int len);//将len字节从指定的初始偏移量为off的byte数组写入此流
void write(int b);//将指定的字节写入此流

2. err标准错误输出流——输出错误消息到控制台 P269
err是“标准”错误输出流。此流已打开并准备接收输出数据：
System.err.println(data);

Tips：标准输出和标准出错的一个区别是：标准输出往往是带缓存的，而标准出错没有缓存（默认设置，可以改）。所以如果你用标准出错打印出来的东西可以马上显示在屏幕上，而标准输出打印出来的东西可能要再积累几个字符才能一起打印出来。总地来说，System.out用于正常的输出，也就是程序真正想输出的内容；而System.err用于出错信息的输出，也就是你本来不期待看到的东西。

3. in标准输入流——接收键盘输入
in是“标准”输入流。in对象的类型是InputStream，它提供了两个方法。
（1）read()方法读取一个字节
read()用于从控制台读入一个字节（8bit）。

public class TestSystemInRead
{
	public static void main(String args[])
	{
		try
		{
			int c;
			while ((c = System.in.read()) != 0)
			{
				System.out.write(c);
			}
		}
		catch (Exception e)
		{
		}
	}
}
（2）read(byte[] b)读取一个字节数组
上面的函数一次仅仅能够读取一个字节。可以使用read(byte[] b)来读取一个字节数组。首先创建了一个长度为100的字节数组，表示一次可以读取100个字节，然后使用read(byte[] b)读取100个字节到byte中，再调用write()函数输出这个字节数组到控制台中。

public class TestSystemInReadByte
{
	public static void main(String args[])
	{
		try
		{
			byte[] b = new byte[100];//如果输入的字符串长度大于100，多余的字节就会被抛弃。
			System.in.read(b);
			System.out.write(b, 0, 100);
			//可以将读入的字节数组b转化为字符串输出
			//String str = new String(b);
			//System.out.println(str);
		}
		catch (Exception e)
		{
		}
	}
}

//循环输入
public class TestSystemInReadByte
{
	public static void main(String args[])
	{
		try
		{
			while (true)
			{
				byte[] b = new byte[100];
				System.in.read(b);
				String str = new String(b);
				System.out.println(str);
			}
		}
		catch (Exception e)
		{
		}
	}
}

Tips：System.in的read()函数读取，是在控制台中按回国后开始执行的。因此，如果在上面的程序中使用read()函数读取一个字节时，不是在用户输入一个字符后读取的，而是在按回车键后一次性读取的。

9.1.2 控制台读写类Console P271
上节程序读入字节数组，再转化为String，实现复杂。长度有100的限制，小于100会使用方框代替，大于100将会被抛弃。还必须捕捉异常。

public class TestConsole
{
	public static void main(String args[])
	{
		while (true)
		{
			String str = System.console().readLine();//用Eclipse将会抛出console空指针异常，使用cmd运行java TestConsole可以正常运行
			System.out.println(str);
		}
	}
}
这里使用了System.console()函数取得了Console对象，然后使用readLine()函数即可读取一行字符串的输入。该函数在用户执行回车时调用。
--不需要捕捉异常，该函数没有抛出异常。
--不需要将字节数组转化成字符串，直接读入的就是字符串。
--程序执行中，可以完整地读取一行输入并完整输出，不会有抛弃和方框填充。

（1）Console类及输出消息方法
Console类包含多个方法，可访问与当前Java虚拟机关联的基于字符的控制台设备（如果有），常指的就是键盘。
虚拟机是否具有控制台取决于底层平台，还取决于调用虚拟机的方式。如果虚拟机从一个交互式命令行开始启动，且没有重定向标准输入和输出流，那么其控制台将存在，通常连接到键盘并从虚拟机启动的地方显示。如果虚拟机是自动启动的（如，由后台作业调度程序启动），它通常没有控制台。如果此虚拟机具有控制台，那么它将由此类唯一的实例（可通过调用System.console()方法获得）表示。如果没有可用的控制台设备，对该方法的调用将返回null。
Tips：这就是为什么我们在执行如上程序时只能在控制台中执行，而不能在Eclipse中执行的原因。

该类用于从控制台读取消息，也可以输出消息，其输出方法是：
System.console().printf(str);
它的读写操作是同步的，以保证关键操作能完整完成。因此调用方法readLine()、printf()时可能阻塞。
（2）读取密码
如果应用程序需要读取密码或其他安全数据，则应该使用readPassword()或readPassword(String, Object)，并在执行后手工将返回的字符数组归零，以最大限度地缩短内存中敏感数据的生存期。

public class TestConsolePassword
{
	public static void main(String args[])
	{
		while (true)
		{
			char password[] = System.console().readPassword();//System.console().readPassword("[%s]", "Please input password");将有"[Please input password]"作为提示信息显示在控制台窗口中
			System.out.println(password);
		}
	}
}
输出如下：
D:\Program Files\eclipseJava workspace\LearningProject\bin>java TestConsolePassword

hello

it is amazing

9.2 文件基本处理 P273
9.2.1 文件操作类File
（1）创建File实例
--File(String pathname)
File path = new File("D:/demo");
File file = new File("D:/demo/test.txt");
--File(String parent, String child)
File file = new File("D:/demo", "test.txt")
--File(URI uri)
File file = new File("file://D:/demo/test.txt");
--File(File parent, String child)：根据parent抽象路径名和child路径名字符串创建一个新File实例。
File file = new File(path, "test.txt");

（2）判断是目录还是文件
isDirectory()：是否是目录
isFile()：是否是文件
如：
if (path.isDirectory())
{
	
}
if (file.isFile())
{
	
}

（3）检查目录或文件是否存在
if (path.exists())
{
	
}
if (file.exists())
{
	
}

（4）创建目录或文件
当目录或文件不存在时，可以通过下面的方法来创建目录或空文件。
--mkdir()：创建一个目录
--mkdirs()：创建所有目录
--createNewFile()：创建一个文件
其中，创建多层目录的时候要用mkdirs()，mkdir()只能在已有目录下创建一个子目录，而对于createNewFile()则需要捕捉IOException。如：
if (!path.exists())
{
	path.mkdir();
	path.mkdirs();
}
if (!file.exists())
{
	try
	{
		file.createNewFile();
	}
	catch (IOException e)
	{
		
	}
}
（5）查看目录下的文件列表，并输出路径名
对于目录来说，可以使用listFiles()取得该目录下的文件列表，它返回的是一个File类型的数组，可以循环该数组取得每一个文件对象，并使用getAbsolutePath()取得该对象的路径名。
如下所示：
File[] filelist = path.listFiles();
for (int i = 0; i < filelist.length; ++i)
{
	System.out.println(filelist[i].getAbsolutePath());
}

//例
import java.io.File;

public class TestConsolePassword
{
	public static void main(String args[])
	{
		File path = new File("f:\\PTDownload");
		File[] fileList = path.listFiles();//将会得到所有文件和文件夹，包括隐藏文件夹
		for (File f : fileList)
		{ 
			System.out.println(f.getAbsolutePath());
		}
	}
}
输出如下：
f:\PTDownload\f
f:\PTDownload\Rise.of.the.Tomb.Raider.CHT.CHS.RF.X360.GOD.WITH.TU2.DLC-ALI213
f:\PTDownload\使命召唤.BD1280超清中英双字.mp4
f:\PTDownload\八面埋伏.BD超清中英双字.mkv
f:\PTDownload\头脑特工队BD1280高清国英双语中字.mkv
f:\PTDownload\港囧HD1280国语中字.mp4
f:\PTDownload\滚蛋吧！肿瘤君.HD1280超清国语中英双字.mp4
f:\PTDownload\猫和老鼠之巨人大冒险BD中字.rmvb
f:\PTDownload\福尔摩斯先生BD1280高清中英双字.rmvb
f:\PTDownload\美式极端BD1280高清中英双字.rmvb

（6）重命名文件名
对于文件，可以使用renameTo()函数重命名一个新的文件名，参数为一个新File实例。例如：
File newFile = new File("D:/demo/test2.txt");
if (file.isFile())
{
	file.renameTo(newFile);
}

（7）删除目录或文件
对于目录或文件对象，可以执行delete()函数来删除目录或文件。如果对目录执行删除，则将会删除该目录下的所有文件和目录。例如：
path.delete();
file.delete();

9.2.2 文件搜索类FileFilter与FilenameFilter P275
按特定要求找出所有文件，可以使用两个文件搜索类FileFilter与FilenameFilter，它们都是接口，只需要我们编写自己的实现类来实现它们的函数accept()即可。FileFilter的主要作用就是检测文件是否存在，FileFilter和FilenameFilter唯一的不同是，FileFilter提供文件对象的访问方法，而FilenameFilter是按照目录和文件名的方式来工作的。
FileFilter的过滤函数接口，用户的实现类只需要实现该函数，对不同的文件进行过滤即可：
boolean accept(File file);

而FilenameFilter的过滤函数是按照目录和文件名来进行的：
boolean accept(File directory, String name);

1. FileFilter的使用
一个简单的例子是搜索特定的文件扩展名，可以使用FilenameFilter，但是出来的结果会让你很难判断到底是目录还是。要解决这个问题，需要使用FileFilter对象。
//ExtensionFilter.java
package test.file;

import java.io.File;
import java.io.FileFilter;

public class ExtensionFilter implements FileFilter
{
	private String extension;
	
	public ExtensionFilter(String extension)
	{
		this.extension = extension;
	}

	@Override
	public boolean accept(File file)
	{
		if (file.isDirectory())
		{
			return false;
		}
		
		String name = file.getName();
		//扩展名前的符号“.”
		int index = name.lastIndexOf(".");
		if (index == -1)//没有扩展名则返回false
		{
			return false;
		}
		else if (index == name.length() - 1)//以点号结尾则返回false
		{
			return false;
		}
		else
		{
			return extension.equals(name.substring(index + 1));
		}
	}
}

//TestFile.java
package test.file;

import java.io.File;

public class TestFile
{
	public static void main(String[] args)
	{
		File file = new File("f:/PTDownload");
		ExtensionFilter mp4Filter = new ExtensionFilter("mp4");
		File[] files = file.listFiles(mp4Filter);
		for (int i = 0; i < files.length; ++i)
		{
			System.out.println(files[i].getAbsolutePath());
		}
	}
}
输出如下：
f:\PTDownload\使命召唤.BD1280超清中英双字.mp4
f:\PTDownload\港囧HD1280国语中字.mp4
f:\PTDownload\滚蛋吧！肿瘤君.HD1280超清国语中英双字.mp4

2. FilenameFilter的使用 P277
FilenameFilter的过滤参数accept()则直接根据目录和文件名进行过滤，与FileFilter相比，多了一个文件名参数。如下例子过滤扩展名为.gif,.jpg,.png的文件。
//ImageFilter.java
package test.file;

import java.io.File;
import java.io.FilenameFilter;

public class ImageFilter implements FilenameFilter
{
	public boolean isGif(String filename)
	{
		if (filename.toLowerCase().endsWith(".gif"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean isJpg(String filename)
	{
		if (filename.toLowerCase().endsWith(".jpg"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean isPng(String filename) 
	{
		if (filename.toLowerCase().endsWith(".png"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public boolean accept(File dir, String filename)
	{
		return (isGif(filename) || isJpg(filename) || isPng(filename));
	}
}

//TestFile.java
package test.file;

import java.io.File;

public class TestFile
{
	public static void main(String[] args)
	{
		File file = new File("d:/Program Files/eclipseJava workspace/LearningProject/src");
		ImageFilter imageFilter= new ImageFilter();
		File[] images = file.listFiles(imageFilter);
		for (int i = 0; i < images.length; ++i)
		{
			System.out.println(images[i].getAbsolutePath());
		}
	}
}
输出如下：
d:\Program Files\eclipseJava workspace\LearningProject\src\imageButton.jpg
d:\Program Files\eclipseJava workspace\LearningProject\src\img.jpg
d:\Program Files\eclipseJava workspace\LearningProject\src\sjtu.jpg
d:\Program Files\eclipseJava workspace\LearningProject\src\wxPython.jpg

//上述ImageFilter.java中有较多的重复代码，重构后如下：
package test.file;

import java.io.File;
import java.io.FilenameFilter;

public class ImageFilter implements FilenameFilter
{
	@Override
	public boolean accept(File dir, String filename)
	{
		return (isGif(filename) || isJpg(filename) || isPng(filename));
	}
	
	private boolean isExtensionType(String filename, String extension)
	{
		return filename.toLowerCase().endsWith(extension);
	}
	
	private boolean isGif(String filename)
	{
		return isExtensionType(filename, ".gif");
	}
	
	private boolean isJpg(String filename)
	{
		return isExtensionType(filename, ".jpg");
	}
	
	private boolean isPng(String filename) 
	{
		return isExtensionType(filename, ".Png");
	}
}

Tips：通常建议使用FilenameFilter类进行文件过滤。

9.2.3 文件随机读写类RandomAccessFile P279
RandomAccessFile类能够随机访问文件，它将文件内容存储在一个大型byte数组中。它存在着指向该隐含byte数组的光标或索引，称为文件指针，该指针位置可以通过seek方法设置。
通常，如果此类中的所有读取例程在读取所需数量的字节之前已到达文件末尾，则抛出EOFException（是一种IOException）。如果由于某些原因无法读取任何字节，而不是读取所需数量的字节之前已到达文件末尾，则抛出IOException，而不是EOFException。需要特别指出，如果流已被关闭，则可能抛出IOException。
1. 打开与关闭文件
--RandomAccessFile(File file, String mode)
--RandomAccessFile(String name, String mode)
第一种通过一个File对象创建，第二种则直接指向文件名。其中的参数mode表示对目标文件的访问模式，可选值如表9-1 所示：
r
rw
rws
rwd
Tips：“rwd”模式可用于减少执行的“I/O”操作数量。使用“rwd”仅要求更新写入存储的文件的内容；使用“rws”要求更新要写入的文件内容及其元数据，这通常要求至少一个以上的低级别I/O操作。

采用读写模式打开文件的方法：
RandomAccessFile file = new RandomAccessFile("D:/demo/test.txt", "rw");
在完成对文件的一系列操作之后，还需要使用close()函数关闭文件：
file.close();
Tips：打开文件后必须关闭，否则该文件会被占用。

打开文件后，还可以在文件的长度范围内设置指针位置
（1）读取文件长度
可以使用length()取得文件的长度，该数值是该文件的字节数，如下例所示：
long size = file.length();
System.out.println("filesize:" + size);
（2）设置文件指针位置
打开文件后，默认的读取指针指向文件的最开始处，即0。RandomAccessFile类之所以是访问类，就是因为它能够自动定位读取的指针。此时可以使用seek(long pos)方法来定位读取位置。
file.seek(4);//设置开始指针位置为4
Tips：如果设置的指针位置超过了文件的字节数长度，那么会默认定位到文件的末尾。

2. 读取文件
打开文件后，即可从该文件中读取数据。
（1）读取一个数据字节：read()
read()函数从该文件的当前指针位置读取一个字节，可以将该字节转化为字符输出，如下：
int c = file.read();
System.out.println((char)c);
每调用一次read()函数，读取的指针就会往后移动一个字节的位置。
（2）读取字节数组：read(byte[] b)
该函数能够一次读取多个字节，读取的长度为字节数组b的长度，如下：
byte[] b = new byte[3];
file.read(b);
String str = new String(b);
System.out.println(str);
（3）灵活读取字节数组：read(byte[] b, int off, int len)
可以使用参数off指定填充数组的起始位置，并使用len指定从文件中讲稿的字节长度，如下：
byte[] b2 = new byte[3];
file.read(b2, 1, 2);
String str2 = new String(b2);
System.out.println(str2);
Tips：len必须不能大于从off开始所剩余的数组的长度，否则会出现数组越界的异常IndexOutOfBoundsException。
（4）读取固定类型的数值 P281
（5）读取一行字符串：readLine()
上面的读取函数都是读入的byte类型的字节，也就是按位读取的。读取文件的一行可以使用readLine()，它返回String类型的字符串，如下：
System.out.println(file.readLine());
（6）读取中文字符：readUTF()
System.out.println(file.readUTF());
//abc.txt
abc=你好
def:天气不错
opq    :    什么


//TestFile.java
package test.file;

import java.io.IOException;
import java.io.RandomAccessFile;

public class TestFile
{
	public static void main(String[] args) throws IOException
	{
		RandomAccessFile file = null;
		try
		{
			file = new RandomAccessFile("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/abc.txt", "r");
			int c = file.read();
			System.out.println((char)c);
			
			byte[] b = new byte[3];
			file.read(b);
			String str = new String(b);
			System.out.println(str);
			
			System.out.println(file.readLine());//当前行剩余字符读取出来，不包含该行已读取的字符
			System.out.println(file.readLine());
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
		finally
		{
			if (file != null)
			{
				file.close();
			}
		}
	}
}
输出如下：
a
bc=
????
def:?ì?????í

3. 写入文件 P281
（1）写入一个数据字节：write()
在该文件的当前指针位置写入一个字节，并覆盖原有的字节。如下：
file.write(100);
每调用一次write()函数，读取的指针就会往后移动一个字节的位置。
（2）写入字节数组：write(byte[] b)
一次性写入多个字节，写入的长度为字节数组b的长度。
byte[] bw = new byte[3];
bw[0] = 100;//字符d的ASCII码
bw[1] = 101;//e
bw[2] = 102;//f
file.write(bw);
执行后，会在指针位置写入“def”3个字符。
（3）灵活写入字节数组：read(byte[] b, int off, int len)
可以使用参数off指定写入数组的起始位置，并使用len指定从数组中写入的字节长度。如下：
file.write(bw, 1, 2);//写入从1开始，长度为2的数组，即ef
Tips：len必须不能大于从off开始所剩余的数组的长度，否则会出现数组越界的异常IndexOutOfBoundsException。
（4）写入固定类型的数值
（5）写入一个字符串：writeBytes()与writeChars()
需要写入一个字符串，此时可以使用writeBytes()。如下：
file.writeBytes("AA");
还可以使用writeChars()写入一个字符串，与writeBytes()不同的是，writeChars()写入的每一个字符老师按照双字节写入的，即一个字符占用2个字节。如下：
file.writeChars("DD");
这句话实际上是写入了“DD”4个字节，高位以0填充。
（6）写入中文字符：writeUTF()
file.writeUTF("中国");//写入中文字符

9.3 输入/输出流 P282
Java IO最关键的4个父类是：InputStream（输入字节流）、OutputStream（输出字节流）、Reader（输入字符流）、Writer（输出字符流）。都是public abstract class类。
InputStream、OutputStream对于数据的传送是以字节“byte”为单位的，而Reader和Writer对于数据的传送是以字符“character”为单位的。java.io包中的类大体上可以分为两大类，一类是以byte处理为主的Stream类，它们都是以XXXStream方式命名的；一类是以character处理为主的Read/Writer类，它们都是以XXXReader或XXXWriter的方式命名的。

9.3.1 流的动作原理
所谓流，就是数据的有序排列，而流可以是从某个源（称为流源）出来，到某个目的地去的。
--根据流的方向的不同，可以分成输入流和输出流，一个程序从输入流读取数据向输出流写数据。
--根据流的数据类型不同，又可以分为字节流和字符流，字节流可以实现向字符流的转换。

流处理器所处理的流必定都有流的输入源，而如果将流类所处理的流源分类的话，基本可以分成两大类。
--第一类：数组、String、File等，这一种叫原始流源。
--第二类：同样类型的流用做链接流类的流源，叫链接流源。

9.3.2 输入字节流InputStream P285
1. 字节数组作为输入源——ByteArrayInputStream
ByteArrayInputStream(byte[] buf);
ByteArrayInputStream(byte[] buf, int offset, int length);

//
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class TestByteArrayInputStream
{
	public static void main(String[] args) throws IOException
	{
		byte[] buf = new byte[3];
		buf[0] = 100;
		buf[1] = 101;
		buf[2] = 102;//'f'
		try
		{
			ByteArrayInputStream is = new ByteArrayInputStream(buf);
			
			byte[] b = new byte[3];
			is.read(b);
			System.out.println(new String(b));
			
			is.close();
		}
		catch (IOException e)
		{
			
		}
	}
}
输出如下：
def

从该类的使用实例来看，它只是将字节数组转换为另一个字节数组，从表面上看好像并没有什么实际意义。实际上，它将字节数组转化成了流式读取，这样就可以通过流的方式从内存中读取一个字节，而不需要直接操作数组，这就是转化的好处。同时，它还为字符流提供了输入源。
Tips：对流操作的代码进行了IOException异常的捕捉，凡是涉及输入流、输出流操作的，都需要进行异常捕捉。

2. 文件作为输入源——FileInputStream
FileInputStream从文件系统中的某个文件中获得输入字节，适用于读取如图像数据之类的原始字节流。它可以通过如下的两种方式创建：
FileInputStream(File file);
FileInputStream(String name);

//
import java.io.FileInputStream;
import java.io.IOException;

public class TestFileInputStream
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			FileInputStream is = new FileInputStream("D:/Program Files/eclipseJava workspace/LearningProject/dbTest/b.txt");
			while (is.available() > 0)
			{
				int c = is.read();
				System.out.print((char)c);
			}
			
			is.close();
		}
		catch (IOException e)
		{
			
		}
	}
}

3. 对象作为输入源——ObjectInputStream
ObjectOutputStream和ObjectInputStream分别与FileOutputStream和FileInputStream一起使用时，可以为应用程序提供对对象图形的持久存储。
例如，要从由ObjectOutputStream的示例写入的文件中读取数据，可通过如下代码实现：
FileInputStream fis = new FileInputStream("t.tmp");
ObjectInputStream ois = new ObjectInputStream(fis);

int i = ois.readInt();
String today = (String)ois.readObject();
Date date = (Date)osi.readObject();

ois.close();

Tips：由于文件test.tmp是写入类调用ObjectOutputStream写入的对象，每一个对象的写入都是有顺序的，因此这里的读取也必须采用同样的顺序。如果唾弃不一致，将会产生对象类型转换异常java.io.OptionalDataException，这是IOException的子类。

4. 字符串作为输入源——StringBufferInputStream P289
StringBufferInputStream(String s);

5. 管道输入流——PipedInputStream
创建管道输入流的实例时，需要以管道输出流的实例作为输入：
PipedInputStream(PipedOutputStream src);//使其连接到管道输出流
也可以创建一个未连接的管道输入流：
PipedInputStream();//创建尚未连接的PipedInputStream
然后调用PipedInputStream类的connect建立与输出流的连接：
void connect(PipedOutputStream src);//使此管道输入流连接到管道输出流src

//
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class TestPipedInputStream
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			PipedOutputStream os = new PipedOutputStream();
			PipedInputStream is = new PipedInputStream(os);
			
			byte[] b = new byte[] {'d', 'e', 'f'};
			os.write(b);
			
			while (is.available() > 0)
			{
				int c = is.read();
				System.out.print((char)c);
			}
			
		}
		catch (IOException e)
		{
			
		}
	}
}
输出如下：
def
Tips：该实例演示了PipedOutputStream的创建和使用。通常管道的使用不像这里的一样，简单地建立流的连接，这样实际上并没有意义。管道流通常用在多线程的系统中，一个线程可以随时往输出流管道写入数据，另一个线程可以随时从输入流管道读取数据，达到异步传输数据。

6. 串联输入源——SequenceInputStream
允许你连接多个InputStream流，表示其他输入流的逻辑串联。它从输入流的有序集合开始，并从第一个输入流开始读取，直到到达文件末尾，接着从第二个输入流读取，依次类推，直到到达包含的最后一个输入流的文件末尾为止。
不仅可以串联两个数据源，也可以串联一个Enumeration类型的数据源列表，分别使用下面的构造函数创建：
SequenceInputStream(InputStream s1, InputStream s2);
SequenceInputStream(Enumeration<? extends InputStream> e);
//c.txt中的内容为什么没有显示出来？

import java.io.FileInputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.Enumeration;
import java.util.Vector;

public class TestSequenceInputStream
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			FileInputStream fis1 = new FileInputStream("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/b.txt");
			FileInputStream fis2 = new FileInputStream("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/c.txt");
			Vector<FileInputStream> vector = new Vector<FileInputStream>();
			vector.add(fis1);
			vector.add(fis2);
			Enumeration<FileInputStream> e = vector.elements();
			
			SequenceInputStream is = new SequenceInputStream(e);
			
			while (is.available() > 0)
			{
				int c = is.read();
				System.out.print((char)c);
			}
			
			is.close();
		}
		catch (IOException e)
		{
			
		}
	}
}
b.txt:
It is a nice day.
How are you?
c.txt:
My name is Han Meimei.
What is your name?

输出如下：
It is a nice day.
How are you?

Tips：串联的数据源列表中，可以包含各种InputStream实现类的数据流对象。

7. 过滤输入流——FilterInputStream P292

8. 缓存输入流——BufferedInputStream
为另一个输入流添加一些功能，即缓存输入的能力。
如果需要一个具有缓存的文件输入流，则应当组合使用FileInputStream和BufferedInputStream，这将会提高读取的效率。
//

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class TestBufferedInputStream
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			FileInputStream is = new FileInputStream("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/b.txt");
			BufferedInputStream bis = new BufferedInputStream(is);
			
			while (bis.available() > 0)
			{
				int c = bis.read();
				System.out.print((char) c);
			}
			
			bis.close();
			is.close();
		}
		catch (IOException e)
		{
			
		}
	}
}
Tips：输入流对象的关闭顺序必须与其创建的顺序相反，后面的输出流也是如此。

9. 数据输入流——DataInputStream

Tips：DataInputStream对于多线程访问不一定是安全的。线程安全是可选的，它由此类方法的使用者负责。

10. 行号输入流——LineNumberInputStream
一般不直接使用此类，而使用其他Stream的嵌套间接对它进行使用，如：
LineNumberInputStream lis = new LineNumberInputStream(new FileInputStream("source"));
要演示的功能是，按照文件的行读取，输出每一行的秸和内容。因此，为了读取一行内容，我们可以通过DataInputStream进行输入流过滤，获得readLine()的功能。
//
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.LineNumberInputStream;

public class TestLineNumberInputStream
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			FileInputStream fis = new FileInputStream("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/b.txt");//文件的最后一行一定要是个空换行符，否则最后一行没有读到换行符，则最后一行的行号不会加1
			LineNumberInputStream lis = new LineNumberInputStream(fis);//此类弃用，不建议使用
			DataInputStream dis = new DataInputStream(lis);
			
			String line;
			while ((line = dis.readLine()) != null)
			{
				System.out.println(lis.getLineNumber() + ":" + line);
			}
			
			dis.close();
			lis.close();
			fis.close();
		}
		catch (IOException e)
		{
			
		}
	}
}
输出如下：
1:It is a nice day.
2:How are you?
3:Hello, world!

Tips：从JDK1.1 开始，对字符流的首选方法是通过新字符流类LineNumberReader进行操作。

11. 推回输入流——PushbackInputStream P295
PushbackInputStream为另一个输入流添加性能，即“推回（pushback）”或“取消读取（unread）”一个字节的能力。在代码片段可以很方便地读取由特定字节值分隔的不定数量的数据字节时，这很有用：在读取终止字节后，代码片段可以“取消读取”该字节，这样，输入流上的下一个读取将会重新读取被推回的字节。
//运行该程序将会重复输出第一个字符。

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

public class TestPushbackInputStream
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			FileInputStream fis = new FileInputStream("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/b.txt");
			PushbackInputStream pis = new PushbackInputStream(fis);//该类弃用，不建议使用
			
			int c = pis.read();
			System.out.print((char)c);
			
			pis.unread(c);
			c = pis.read();
			System.out.print((char)c);
			
			pis.close();
			fis.close();
		}
		catch (IOException e)
		{
			
		}
	}
}

12. 总结 P296
讲解了11种InputStream的实现，它们的功能各有不同。根据功能，可以分为3类。
（1）根据输入源的不同选用的类
--数组：ByteArrayInputStream
--文件：FileInputStream
--对象：ObjectInputStream
--字符串：StringBufferInputStream
（2）根据流的串联选择的类
--管道：PipeInputStream
--序列：SequenceInputStream
（3）对流进行过滤选择的类
--过滤：FilterInputStream
--缓存：BufferedInputStream
--数据：DataInputStream
--行号：LineNumberInputStream
--推回：PushbackInputStream

根据实际应用的需要，使用最多的是FileInputStream和BufferedInputStream的结合，即在读取文件时使用缓存读取，这样可以提高读取文件的效率。StringBufferInputStream也是一种比较常用的类，通常可能需要我们根据某一个字符串来创建一个输入流。
无论选择哪一种，除了输入源的类之外，其他的类都拥有以InputStream类型为参数的构造函数，即可以对输入流进行多次包装，这样这个流就拥有了多个功能。如程序9-18 ，既需要读取行号，又需要读取一行，因此就使用了三层包装。

9.3.3 输出字节流OutputStream P297
1. 字节数组作为输出流——ByteArrayOutputStream
2. 文件作为输出源——FileOutputStream
3. 对象作为输出源——ObjectOutputStream P299
4. 管道输出流——PipedOutputStream
PipedOutputStream可以将管道输出流连接到管道输入流来创建通信管道。管道输出流是管道的发送端。通常，数据由某个线程写入PipedOutputStream对象，并由其他线程从连接的PipedInputStream读取。不建议对这两个对象尝试使用单个线程，因为这样可能会造成该线程死锁。
package test.file;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class TestPipedOutputStream
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			Sender t1 = new Sender();
			PipedOutputStream out = t1.getOutStream();
			
			Receiver t2 = new Receiver();
			PipedInputStream in = t2.getInputStream();
			
			out.connect(in);
			
			new Thread(t1).start();
			new Thread(t2).start();
		}
		catch (IOException e)
		{
			
		}
	}
}

class Sender extends Thread
{
	private PipedOutputStream out = new PipedOutputStream();
	
	public PipedOutputStream getOutStream()
	{
		return out;
	}
	
	public void run()
	{
		String s = new String("hello, receiver, how are you");
		try
		{
			out.write(s.getBytes());
			out.close();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
}

class Receiver extends Thread
{
	private PipedInputStream in = new PipedInputStream();
	
	public PipedInputStream getInputStream()
	{
		return in;
	}
	
	public void run()
	{
		String s = null;
		byte[] buf = new byte[1024];
		try
		{
			int len = in.read(buf);
			s = new String(buf, 0, len);
			System.out.println("from:\n" + s);
			in.close();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
输出如下：
from:
hello, receiver, how are you

运行程序，即可实现发送者线程往接收者线程发送数据的功能，它们分别使用了管道输入流和输出流。

Tips：程序9-23 是管道流应用的常用模式，在多线程的数据流传输中，都离不开这两个类。本例通过两个子线程进行发送和接收，这种模式可以作为以后实际开发中的模板来使用。

5. 过滤输出流——FilterOutputStream P302
6. 缓存输出流——BufferedOutputStream P302
7. 数据输出流——DataOutputStream P303
8. 格式化输出流——PrintStream P304

9. 总结 P307
讲解了8种OutputStream的实现，它们的功能各有不同。根据它们的功能，可以分为3类。
（1）根据输出源的不同选用的类
--数组：ByteArrayOutputStream
--文件：FileOutputStream
--对象：ObjectOutputStream
（2）根据流的串联选择的类
--管道：PipeOutputStream
（3）对流进行过滤选择的类
--过滤：FilterOutputStream
--缓存：BufferedOutputStream
--数据：DataOutputStream
--格式化：PrintStream

9.3.4 输入字符流Reader P307
1. 字符数组作为输入源——CharArrayReader
//
import java.io.CharArrayReader;
import java.io.IOException;

public class TestCharArrayReader
{
	public static void main(String[] args) throws IOException
	{
		char[] buf = new char[] {'a', 'b', 'c'};
		try
		{
			CharArrayReader is = new CharArrayReader(buf);

			char[] b = new char[3];
			is.read(b);
			System.out.println(new String(b));

			is.close();
		}
		catch (IOException e)
		{
			// TODO: handle exception
		}
	}
}
从该类的使用实例来看，它只是将字符数组转换为了字符数组，并没有什么实际意义。实际上，它将字符数组转化成了流式读取，这样就可以通过流式的方式从内存中读取一个字符，而不需要直接数组，这就是转化的好处。

2. 文件作为输入源——FileReader P309
//
import java.io.FileReader;
import java.io.IOException;

public class TestFileReader
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			FileReader is = new FileReader("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/abc.txt");

			while (is.ready())
			{
				int c = is.read();
				System.out.print((char)c);
			}
			//这里的while写法同下
			// int c;
			// while ((c = is.read()) > 0)
			// {
				// System.out.print((char)c);
			// }
			
			is.close();
		}
		catch (IOException e)
		{
			
		}
	}
}
Tips：该程序能够正确输出中文文，因为FileReader是按照字符读取的；而FileInputStream会输出乱码，因为它是按照字节读取的。所以，如果要读取中文文件，请使用FileReader。

3. 字符串作为输入源——StringReader
4. 管道输入流——PipedReader P310
5. 缓存输入流——BufferedReader P312
如果需要一个具有缓存的文件输入流，则应当组合使用FileReader和BufferedReader，这将会提高读取的效率。
FileReader is = new FileReader("D:/demo/test.txt");
BufferedReader bis = new BufferedReader(is);

6. 行号输入流——LineNumberReader P312
可认为行在遇到以下符号之一时结束：换行符（'\n'）、回车符（'\r'）、回车符后紧跟换行符。
LineNumberReader继承自BufferedReader。
FileReader fis = new FileReader("D:/demo/test.txt");
LineNumberReader lis = new LineNumberReader(fis);
String line;
while((line = lis.readLine()) != null)
{
	System.out.println(lis.getLineNumber() + ":" + line);
}
lis.close;
fis.close;

7. 过滤输入流——FilterReader P313
8. 推回输入流——PushbackReader P313

9. 总结 P314
以上8种Reader的实现，功能各有不同。根据它们的功能，可以分为3类。
（1）根据输入源的不同选用的类
--数组：CharArrayReader
--文件：FileReader
--字符串：StringReader
（2）根据流的串联选择的类
--管道：PipedReader
（3）对流进行过滤选择的类
--过滤：FilterReader
--缓存：BufferedReader
--行号：LineNumberReader
--推回：PushbackReader

9.3.5 输出字符流Writer P315
1. 字符数组作为输出源——CharArrayWriter P316
2. 文件作为输出源——FileWriter
3. 字符串作为输出源——StringWriter
4. 管道输出流——PipedWriter P317
Tips：程序9-37 是管道流应用的常用模式，在多线程的数据流传输中，都离不开这两个类。本例通过两个子线程进行发送和接收，这种模式可以作为以后实际开发中的模板来使用。
5. 过滤输出流——FilterWriter
6. 缓存输出流——BufferedWriter P319
BufferedWriter将文本写入字符输出流，缓存各个字符，从而提供单个字符、数组和字符串的高效写入。该类提供了newLine()方法，它使用平台自己的行分隔符概念，此概念由系统属性line.separator定义。并非所有平台都使用新行符（'\n'）来终止各行，因此调用此方法来终止每个输出行要优于直接写入新行符。
通常Writer将其输出立即发送到底层字符或字节流。除非要求提示输出，否则建议用BufferedWriter包装所有其write()操作可能开销很高的Writer（如FileWriters和OutputStreamWriters）。
//
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TestBufferedWriter
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			FileWriter os = new FileWriter("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/test.txt");
			BufferedWriter bos = new BufferedWriter(os);
			
			char[] buf = new char[] {'a', 'b', '中'};
			bos.write(buf);
			bos.newLine();
			bos.write("你好吗？");
			bos.write("hello, world");
			
			bos.close();
			os.close();
		}
		catch (IOException e)
		{
			// TODO: handle exception
		}
	}
}


7. 格式化输出流——PrintWriter P320
package test.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class TestPrintWriter
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			FileWriter fos = new FileWriter("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/test.txt");
			PrintWriter pos = new PrintWriter(fos);
			
			pos.format("%1$tY年%1$tm月%1$td日%1$tH时%1$tm分%1$tS秒", new Date());
			pos.println();
			pos.println(12345);
			pos.println(true);
			pos.println(1000.0f);
			
			pos.close();
			fos.close();
		}
		catch (IOException e)
		{
			// TODO: handle exception
		}
	}
}
生成文件内容如下：
2015年12月05日00时12分11秒
12345
true
1000.0

8. 总结 P321
讲解了7种Writer的实现，功能各不相同。根据功能，分为3类：
（1）根据输出源的不同选用的类
--数组：CharArrayWriter
--文件：FileWriter
--字符串：StringWriter
（2）根据流的串联选择的类
--管道：PipeWriter
（3）对流进行过滤选择的类
--过滤：FilterWriter
--缓存：BufferedWriter
--格式化：PrintWriter

9.3.6 字节流与字符流的转换
以字符为导向的stream基本上有与之相对的以字节为导向的stream。两个对应类实现的功能相同，只是操作时的导向不同。如CharArrayReader和ByteArrayInputStream的作用都是把内存中的一个缓存区作为流使用，所不同的是前者每次从内存中读取一个字符的信息，而后者每次从内存中读取一个字节。（原书此处描述有误，byte字节，char字符）
字节流和字符流之间的区别却可以联系起来。InputStreamReader负责把输入字节流转换为输入字符流，OutputStreamReader负责把输出字节流转换为输出字符流。
1. 字节输入流转换为字符输入流 P322
//
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestInputStreamReader
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			FileInputStream fis = new FileInputStream("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/test.txt");//以字节方式读取文件
			InputStreamReader is = new InputStreamReader(fis);//将读取的字节转换成为字符
			BufferedReader bis = new BufferedReader(is);
			
			while (bis.ready())
			{
				int c = bis.read();
				System.out.print((char)c);
			}
			
			bis.close();
			is.close();
			fis.close();
		}
		catch (IOException e)
		{
			// TODO: handle exception
		}
	}
}

Tips：该程序与直接使用FileReader读取文件内容并使用BufferedReader包装的效果相同，都能够输出带中文的文件内容。但是此处是通过字节流来读取文件的，中间经过了字节流向字符流的转换；而FileReader是通过字符流来读取文件的，没有经过流的转换。所以通过FileReader的方式比该程序中的转换方式效率会高一些。

2. 字节输出流转换为字符输出流 P323
//
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TestOutputStreamWriter
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			FileOutputStream fos = new FileOutputStream("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/test.txt");//以字节的方式写入文件
			OutputStreamWriter os = new OutputStreamWriter(fos);//将字符转换为字节
			BufferedWriter bos = new BufferedWriter(os);
			
			char[] buf = new char[] {'a', 'b', '中'};
			bos.write(buf);
			
			bos.close();
			os.close();
			fos.close();
		}
		catch (IOException e)
		{
			// TODO: handle exception
		}
	}
}
Tips：该程序与直接使用FileWriter写入文件内容并使用BufferWriter包装的效果相同，都能够写入带中文的文件内容。但是此处是通过字节流来写入文件的，中间经过了字符流向字节流的转换；而FileWriter是通过字符流来写入文件的，没有经过流的转换。所以通过FileWriter的方式比该程序中的转换方式效率会高一些。

9.4.3 上机作业 P328
1. 文件编辑器VI
//
package vi;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class VI
{
	public static void main(String[] args)
	{
		String pathname = "D:/Downloads";//这里写"D:"，将会解析不到正常的路径，下面的file.getCanonicalPath()会拿到当前程序所在的文件路径
		while (true)
		{
			try
			{
				// String str = System.console().readLine();
				String str = readDataFromConsole();

				if (str.equals("list"))
				{
					File path = new File(pathname);
					File[] filelist = path.listFiles();
					for (File file : filelist)
					{
						String filepath = file.getCanonicalPath();
						if (file.isDirectory())
						{
							System.out.println("目录：" + filepath);
						}
						else
						{
							System.out.println("文件：" + filepath);
						}
					}
				}
				else if (str.startsWith("cd"))
				{
					String[] param = str.split(" ");// 如果文件夹的名字中有空格，这里将没有办法正常解析
					pathname = param[1];
				}
				else if (str.startsWith("mkdir"))
				{
					String[] param = str.split(" ");
					File path = new File(pathname + "/" + param[1]);
					path.mkdir();
				}
				else if (str.startsWith("vi")) // 追加内容
				{
					String[] param = str.split(" ");
					File file = new File(pathname, param[1]);

					if (!file.exists())
					{
						file.createNewFile();
					}

					RandomAccessFile afile = new RandomAccessFile(file, "rw");
					afile.seek(afile.length());
					afile.writeUTF(param[2]);//写入的文件会带有两个未知符号
					afile.close();
				}
				else if (str.startsWith("more")) // 显示内容，这里无法支持中文显示
				{
					String[] param = str.split(" ");
					File file = new File(pathname, param[1]);

					RandomAccessFile afile = new RandomAccessFile(file, "rw");
					String line;
					while ((line = afile.readLine()) != null)
					{
						System.out.println(line);
					}
					afile.close();
				}
				else if (str.equals("pwd"))
				{
					System.out.println(pathname);
				}
				else if (str.equals("bye"))
				{
					System.exit(0);;
				}
				else
				{
					System.out.println("输入命令不支持，请输入其它指令：");
				}
			}
			catch (Exception e)
			{
				System.out.println("命令解析有误，请重新输入：");
				e.printStackTrace();
			}
		}
	}

	private static String readDataFromConsole()
	{
		Scanner scanner = new Scanner(System.in);//有个scanner未close的提示
		return scanner.nextLine();
	}
}
运行该程序，可以往文本文件中写入英文字符串，也可以显示英文文本内容，但是不能够写入和显示中文，中文会显示乱码。


2. 字节流文本编辑器 ByteVI.java P331
//
package vi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ByteVI
{
	public static void main(String[] args)
	{
		String pathname = "D:/Downloads";//这里写"D:"，将会解析不到正常的路径，下面的file.getCanonicalPath()会拿到当前程序所在的文件路径
		while (true)
		{
			try
			{
				// String str = System.console().readLine();
				String str = readDataFromConsole();

				if (str.equals("list"))
				{
					File path = new File(pathname);
					File[] filelist = path.listFiles();
					for (File file : filelist)
					{
						String filepath = file.getCanonicalPath();
						if (file.isDirectory())
						{
							System.out.println("目录：" + filepath);
						}
						else
						{
							System.out.println("文件：" + filepath);
						}
					}
				}
				else if (str.startsWith("cd"))
				{
					String[] param = str.split(" ");// 如果文件夹的名字中有空格，这里将没有办法正常解析
					pathname = param[1];
				}
				else if (str.startsWith("mkdir"))
				{
					String[] param = str.split(" ");
					File path = new File(pathname + "/" + param[1]);
					path.mkdir();
				}
				else if (str.startsWith("vi")) // 追加内容
				{
					String[] param = str.split(" ");
					File file = new File(pathname, param[1]);

					if (!file.exists())
					{
						file.createNewFile();
					}

					FileOutputStream os = new FileOutputStream(file, true);//第二个参数append，表示是否追加方式
					BufferedOutputStream bos = new BufferedOutputStream(os);
					PrintStream pos = new PrintStream(bos);
					pos.print(param[2]);
					
					pos.close();
					bos.close();
					os.close();
				}
				else if (str.startsWith("more")) // 显示内容，这里无法支持中文显示
				{
					String[] param = str.split(" ");
					File file = new File(pathname, param[1]);

					FileInputStream is = new FileInputStream(file);
					BufferedInputStream bis = new BufferedInputStream(is);
					
					while (bis.available() > 0)
					{
						int c = bis.read();
						System.out.print((char)c);
					}
					
					bis.close();
					is.close();
				}
				else if (str.equals("pwd"))
				{
					System.out.println(pathname);
				}
				else if (str.equals("bye"))
				{
					System.exit(0);;
				}
				else
				{
					System.out.println("输入命令不支持，请输入其它指令：");
				}
			}
			catch (Exception e)
			{
				System.out.println("命令解析有误，请重新输入：");
				e.printStackTrace();
			}
		}
	}

	private static String readDataFromConsole()
	{
		Scanner scanner = new Scanner(System.in);//有个scanner未close的提示
		return scanner.nextLine();
	}
}
运行该程序，可以文本中文件中写入中英文字符串，也可以显示英文文本内容，但不能显示中文内容。能够写入中文是因为PrintStream类的print()函数实现了对中文的写入。

3. 字符流文本编辑器CharVI.java P332
package vi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class ByteVI
{
	public static void main(String[] args)
	{
		String pathname = "D:/Downloads";//这里写"D:"，将会解析不到正常的路径，下面的file.getCanonicalPath()会拿到当前程序所在的文件路径
		while (true)
		{
			try
			{
				// String str = System.console().readLine();
				String str = readDataFromConsole();

				if (str.equals("list"))
				{
					File path = new File(pathname);
					File[] filelist = path.listFiles();
					for (File file : filelist)
					{
						String filepath = file.getCanonicalPath();
						if (file.isDirectory())
						{
							System.out.println("目录：" + filepath);
						}
						else
						{
							System.out.println("文件：" + filepath);
						}
					}
				}
				else if (str.startsWith("cd"))
				{
					String[] param = str.split(" ");// 如果文件夹的名字中有空格，这里将没有办法正常解析
					pathname = param[1];
				}
				else if (str.startsWith("mkdir"))
				{
					String[] param = str.split(" ");
					File path = new File(pathname + "/" + param[1]);
					path.mkdir();
				}
				else if (str.startsWith("vi")) // 追加内容
				{
					String[] param = str.split(" ");
					File file = new File(pathname, param[1]);

					if (!file.exists())
					{
						file.createNewFile();
					}

					FileWriter fos = new FileWriter(file, true);//第二个参数append，表示是否追加方式
					BufferedWriter bos = new BufferedWriter(fos);
					PrintWriter pos = new PrintWriter(bos);
					pos.print(param[2]);
					
					pos.close();
					bos.close();
					fos.close();
				}
				else if (str.startsWith("more")) // 显示内容，这里无法支持中文显示
				{
					String[] param = str.split(" ");
					File file = new File(pathname, param[1]);

					FileReader is = new FileReader(file);
					BufferedReader bis = new BufferedReader(is);
					
					String line;
					while ((line = bis.readLine()) != null)//用readLine函数比一个一个字符读取快
					{
						System.out.println(line);
					}
//					while (bis.ready())
//					{
//						int c = bis.read();
//						System.out.print((char)c);
//					}
					
					bis.close();
					is.close();
				}
				else if (str.equals("pwd"))
				{
					System.out.println(pathname);
				}
				else if (str.equals("bye"))
				{
					System.exit(0);;
				}
				else
				{
					System.out.println("输入命令不支持，请输入其它指令：");
				}
			}
			catch (Exception e)
			{
				System.out.println("命令解析有误，请重新输入：");
				e.printStackTrace();
			}
		}
	}

	private static String readDataFromConsole()
	{
		Scanner scanner = new Scanner(System.in);//有个scanner未close的提示
		return scanner.nextLine();
	}
}
运行该程序，既可以往文本文件中写入中英文字符串，又可以显示中英文文本内容。这就是字符流的最大优点，能够实现中文的处理。

//------------------------------------------------------------------------------------------------
//Java核心编程技术 第10课 Java多线程编程 P335
10.1.1 进程与线程的区别 P336
（1）多个进程
每一个进程都可以独立启动，它们都代表一个独立的进程，各自都拥有各自的内存空间，内存空间之间相互独立没有共享，各自也能够并行运行。
（2）多个线程
线程也称为轻型进程（LWP）。线程只能在单个进程的作用域内活动，所以创建线程比创建进程要廉价得多。因为线程允许协作和数据交换，并且在计算资源方面非常廉价，所以线程比进程更可取。
线程，运行在一个进程中，因此它们拥有同一块内存区域，可以共享内存中的数据。它们可以并行执行，但实际上在CPU的调用上是各自分割时间片的，并且是顺序执行的。

10.1 2 线程的概念模型
线程是彼此互相独立的、能独立运行的子任务，并且每个线程都有自己的调用栈。所谓的多任务是通过周期性地将CPU时间片切换到不同的子任务，虽然从微观上看来，单核的CPU上只运行一个子任务，但从宏观来看，每个子任务似乎是同时连续运行的。

多任务是指在一个系统中可以同时运行多个程序，即有多个独立运行的任务，每个任务对应一个进程，同进程一样，一个线程也有从创建、运行到消亡的过程，称为线程的生命周期。用线程的状态（state）表明线程处在生命周期的哪个阶段。线程有创建、可运行、运行中、阻塞、死亡5种状态。通过线程的控制与调用可使线程在这几种状态间转化，每个程序至少自动拥有一个线程，称为主线程。当程序加载到内存时，启动主线程。

10.1.3 线程的运行状态 P337
线程的状态表示线程正在进行的活动及在此时间段内所能完成的任务。线程有创建、可运行、运行中、阻塞、死亡5种状态。一个具有生命的线程，总是处于这5种状态之一。
--新建状态：使用new运算符创建一个线程后，该线程仅仅是一个空对象，系统没有分配资源，称该线程牌创建状态（new thread）。
--可运行状态：使用start()方法启动一个线程后，系统为该线程分配除了CPU外的所需资源，使用该线程处于可运行状态（Runnable）。
--运行状态：Java运行系统通过调度选中一个Runnable的线程，使其占有CPU并转为运行中状态（Running），此时，系统真正执行线程的run()方法。
--阻塞状态：一个正在运行的线程因某种原因不能继续运行时，进入阻塞状态（Blocked）。
--死亡状态：线程结束后是死亡状态（Dead）。

独占方式
分时方式

10.2 线程的开发方法 P338

暂时先不看
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第11课 Java常用实体类 P373
11.1.1 系统类System P373
1. 访问系统属性
System共有两种访问系统属性的方法。
（1）第一种：取得所有系统属性列表 P374

package test.System;

import java.util.Enumeration;
import java.util.Properties;

public class SystemTest
{
	public static void main(String[] args)
	{
		Properties properties = System.getProperties();
		Enumeration<Object> e = properties.keys();
		while (e.hasMoreElements())
		{
			String key = (String)e.nextElement();
			String value = properties.getProperty(key);
			System.out.println(key + "=" + value);
		}
	}
}
输出如下：
java.runtime.name=Java(TM) SE Runtime Environment
sun.boot.library.path=E:\Program Files\Java\jre1.8.0_25\bin
java.vm.version=25.25-b02
java.vm.vendor=Oracle Corporation
java.vendor.url=http://java.oracle.com/
path.separator=;
java.vm.name=Java HotSpot(TM) Client VM
file.encoding.pkg=sun.io
user.country=CN
user.script=
sun.java.launcher=SUN_STANDARD
sun.os.patch.level=
java.vm.specification.name=Java Virtual Machine Specification
user.dir=D:\Program Files\eclipseJava workspace\LearningProject
java.runtime.version=1.8.0_25-b18
java.awt.graphicsenv=sun.awt.Win32GraphicsEnvironment
java.endorsed.dirs=E:\Program Files\Java\jre1.8.0_25\lib\endorsed
os.arch=x86
java.io.tmpdir=C:\Users\Ben\AppData\Local\Temp\
line.separator=

java.vm.specification.vendor=Oracle Corporation
user.variant=
os.name=Windows 8.1
sun.jnu.encoding=GBK
java.library.path=E:\Program Files\Java\jre1.8.0_25\bin;C:\Windows\Sun\Java\bin;C:\Windows\system32;C:\Windows;E:/Program Files/Java/jre1.8.0_25/bin/client;E:/Program Files/Java/jre1.8.0_25/bin;E:/Program Files/Java/jre1.8.0_25/lib/i386;C:\ProgramData\Oracle\Java\javapath;C:\Program Files (x86)\Intel\iCLS Client\;C:\Program Files\Intel\iCLS Client\;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0\;C:\Program Files (x86)\Intel\Intel(R) Management Engine Components\DAL;C:\Program Files\Intel\Intel(R) Management Engine Components\DAL;C:\Program Files (x86)\Intel\Intel(R) Management Engine Components\IPT;C:\Program Files\Intel\Intel(R) Management Engine Components\IPT;C:\Program Files (x86)\NVIDIA Corporation\PhysX\Common;e:\Program Files\QuickStart;C:\Program Files (x86)\Windows Kits\8.1\Windows Performance Toolkit\;C:\Program Files\Microsoft SQL Server\110\Tools\Binn\;e:\Program Files\Java\jdk1.8.0_25\bin;;c:\;;.
java.specification.name=Java Platform API Specification
java.class.version=52.0
sun.management.compiler=HotSpot Client Compiler
os.version=6.3
user.home=C:\Users\Ben
user.timezone=
java.awt.printerjob=sun.awt.windows.WPrinterJob
file.encoding=GBK
java.specification.version=1.8
java.class.path=D:\Program Files\eclipseJava workspace\LearningProject\bin
user.name=Ben
java.vm.specification.version=1.8
sun.java.command=test.System.SystemTest
java.home=E:\Program Files\Java\jre1.8.0_25
sun.arch.data.model=32
user.language=zh
java.specification.vendor=Oracle Corporation
awt.toolkit=sun.awt.windows.WToolkit
java.vm.info=mixed mode
java.version=1.8.0_25
java.ext.dirs=E:\Program Files\Java\jre1.8.0_25\lib\ext;C:\Windows\Sun\Java\lib\ext
sun.boot.class.path=E:\Program Files\Java\jre1.8.0_25\lib\resources.jar;E:\Program Files\Java\jre1.8.0_25\lib\rt.jar;E:\Program Files\Java\jre1.8.0_25\lib\sunrsasign.jar;E:\Program Files\Java\jre1.8.0_25\lib\jsse.jar;E:\Program Files\Java\jre1.8.0_25\lib\jce.jar;E:\Program Files\Java\jre1.8.0_25\lib\charsets.jar;E:\Program Files\Java\jre1.8.0_25\lib\jfr.jar;E:\Program Files\Java\jre1.8.0_25\classes
java.vendor=Oracle Corporation
file.separator=\
java.vendor.url.bug=http://bugreport.sun.com/bugreport/
sun.io.unicode.encoding=UnicodeLittle
sun.cpu.endian=little
sun.desktop=windows
sun.cpu.isalist=pentium_pro+mmx pentium_pro pentium+mmx pentium i486 i386 i86

（2）取得某一个系统属性的值 P376
如果知道要查询特性系统属性的值，可以直接使用System.getProperties(key)直接取得该键的值。如下：
package test.System;

public class SystemTest
{
	public static void main(String[] args)
	{
		String osname = System.getProperty("os.name");
		String osversion = System.getProperty("os.version");
		String javaname = System.getProperty("java.vm.name");
		String javaversion = System.getProperty("java.version");
		System.out.println("操作系统名称=" + osname);
		System.out.println("操作系统版本=" + osversion);
		System.out.println("JVM名称=" + javaname);
		System.out.println("Java版本=" + javaversion);
	}
}
输出如下：
操作系统名称=Windows 8.1
操作系统版本=6.3
JVM名称=Java HotSpot(TM) Client VM
Java版本=1.8.0_25

这几个值在上面的列表都出现过。
如果查找的系统属性的键不存在，将返回null值，此时可以使用下面的函数来设置变量不存在时的默认值：
String osname = System.getProperty("os.name", "Windows XP");
第二个参数即为不存在时返回的默认值。
除了查询，还可以修改这个值，如：
System.setProperty("java.version2", "aa");
该函数设置的值并不会写入操作系统，只会对当前运行程序后面执行的代码产生影响。如果该函数放在getProperties()前你会发现列表中有该属性，如果放在它之后，列表中就没有。
除此之外，还可以一次性设置多个属性：
System.setProperties(Properties props);
输入的值props是一个Properties类型的变量即可。
还可以移除指定键指示的系统属性，如：
System.clearProperties("java.version2");

当然，除了查询外，修改和删除我们通常不使用，只有在做Java安装程序这一类特殊程序时才会使用到。

package test.System;

public class SystemTest
{
	public static void main(String[] args)
	{
		String javaversion2 = System.getProperty("java.version2", "no key");
		System.out.println("Java版本2=" + javaversion2);
		
		System.setProperty("java.version2", "aa");
		javaversion2 = System.getProperty("java.version2");
		System.out.println("Java版本2=" + javaversion2);
		
		System.clearProperty("java.version2");
		javaversion2 = System.getProperty("java.version2", "no key");
		System.out.println("Java版本2=" + javaversion2);
	}
}
输出如下：
Java版本2=no key
Java版本2=aa
Java版本2=no key

2. 访问环境变量 P377
有两种访问系统环境变量的方法。
（1）第一种：取得所有环境变量列表
使用System.getenv()函数将取得一个Map对象，该对象包含了所有环境变量的键值对。
package test.System;

import java.util.Iterator;
import java.util.Map;

public class TestSystemEnv
{
	public static void main(String[] args)
	{
		Map<String, String> map = System.getenv();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext())
		{
			String key = it.next();
			String value = map.get(key);
			System.out.println(key + "=" + value);
		}
	}
}
输出如下：
USERDOMAIN_ROAMINGPROFILE=DESKTOP-18O9S0P
LOCALAPPDATA=C:\Users\Ben\AppData\Local
PROCESSOR_LEVEL=6
COMMANDER_EXE=E:\Program Files\TotalCMD64\Totalcmd64.exe
COMMANDER_DRIVE=E:
USERDOMAIN=DESKTOP-18O9S0P
FPS_BROWSER_APP_PROFILE_STRING=Internet Explorer
LOGONSERVER=\\MicrosoftAccount
#envTSLOGsss19592=148315072
SESSIONNAME=Console
ALLUSERSPROFILE=C:\ProgramData
PROCESSOR_ARCHITECTURE=x86
VS120COMNTOOLS=D:\Program Files\Microsoft Visual Studio 12.0\Common7\Tools\
PSModulePath=C:\Windows\system32\WindowsPowerShell\v1.0\Modules\;C:\Program Files\Intel\
SystemDrive=C:
COMMANDER_INI=E:\Program Files\TotalCMD64\wincmd.ini
APPDATA=C:\Users\Ben\AppData\Roaming
MOZ_PLUGIN_PATH=E:\Program Files\Foxit Software\Foxit Reader\plugins\
USERNAME=Ben
ProgramFiles(x86)=C:\Program Files (x86)
CommonProgramFiles=C:\Program Files (x86)\Common Files
Path=E:/Program Files/Java/jre1.8.0_25/bin/client;E:/Program Files/Java/jre1.8.0_25/bin;E:/Program Files/Java/jre1.8.0_25/lib/i386;C:\ProgramData\Oracle\Java\javapath;C:\Program Files (x86)\Intel\iCLS Client\;C:\Program Files\Intel\iCLS Client\;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0\;C:\Program Files (x86)\Intel\Intel(R) Management Engine Components\DAL;C:\Program Files\Intel\Intel(R) Management Engine Components\DAL;C:\Program Files (x86)\Intel\Intel(R) Management Engine Components\IPT;C:\Program Files\Intel\Intel(R) Management Engine Components\IPT;C:\Program Files (x86)\NVIDIA Corporation\PhysX\Common;e:\Program Files\QuickStart;C:\Program Files (x86)\Windows Kits\8.1\Windows Performance Toolkit\;C:\Program Files\Microsoft SQL Server\110\Tools\Binn\;e:\Program Files\Java\jdk1.8.0_25\bin;;c:\;
FPS_BROWSER_USER_PROFILE_STRING=Default
PATHEXT=.COM;.EXE;.BAT;.CMD;.VBS;.VBE;.JS;.JSE;.WSF;.WSH;.MSC
OS=Windows_NT
PROCESSOR_ARCHITEW6432=AMD64
COMPUTERNAME=DESKTOP-18O9S0P
PROCESSOR_REVISION=5e03
CLASSPATH=.;c:\Program Files\Java\jdk1.8.0_25\lib;
CommonProgramW6432=C:\Program Files\Common Files
ComSpec=C:\Windows\system32\cmd.exe
ProgramData=C:\ProgramData
ProgramW6432=C:\Program Files
COMMANDER_PATH=E:\Program Files\TotalCMD64
HOMEPATH=\Users\Ben
SystemRoot=C:\Windows
TEMP=C:\Users\Ben\AppData\Local\Temp
HOMEDRIVE=C:
PROCESSOR_IDENTIFIER=Intel64 Family 6 Model 94 Stepping 3, GenuineIntel
USERPROFILE=C:\Users\Ben
TMP=C:\Users\Ben\AppData\Local\Temp
CommonProgramFiles(x86)=C:\Program Files (x86)\Common Files
ProgramFiles=C:\Program Files (x86)
PUBLIC=C:\Users\Public
NUMBER_OF_PROCESSORS=8
windir=C:\Windows
=::=::\

这些环境变量的值可以通过Windows的系统环境变量设置来修改。

（2）第二种：取得某一个属性变量的值
如果知道要查询环境变量键，可以直接使用System.getenv(key)直接取得该键的值。如下所示：
package test.System;

public class TestSystemEnv
{
	public static void main(String[] args)
	{
		String javahome = System.getenv("JAVA_HOME");
		String path = System.getenv("PATH");
		String classpath = System.getenv("CLASSPATH");
		System.out.println("javahome=" + javahome);
		System.out.println("path=" + path);
		System.out.println("classpath=" + classpath);
	}
}
输出如下：
javahome=null
path=E:/Program Files/Java/jre1.8.0_25/bin/client;E:/Program Files/Java/jre1.8.0_25/bin;E:/Program Files/Java/jre1.8.0_25/lib/i386;C:\ProgramData\Oracle\Java\javapath;C:\Program Files (x86)\Intel\iCLS Client\;C:\Program Files\Intel\iCLS Client\;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0\;C:\Program Files (x86)\Intel\Intel(R) Management Engine Components\DAL;C:\Program Files\Intel\Intel(R) Management Engine Components\DAL;C:\Program Files (x86)\Intel\Intel(R) Management Engine Components\IPT;C:\Program Files\Intel\Intel(R) Management Engine Components\IPT;C:\Program Files (x86)\NVIDIA Corporation\PhysX\Common;e:\Program Files\QuickStart;C:\Program Files (x86)\Windows Kits\8.1\Windows Performance Toolkit\;C:\Program Files\Microsoft SQL Server\110\Tools\Binn\;e:\Program Files\Java\jdk1.8.0_25\bin;;c:\;
classpath=.;c:\Program Files\Java\jdk1.8.0_25\lib;

3. 加载文件和库 P379
如果需要加载动态库文件名或系统库，可以使用下面的函数。
--函数load(String filename)的作用是，从作为动态库的本地文件系统中以指定的文件名加载代码文件，文件名参数必须是完整的路径名，调用System.load(name)实际上等效于调用：
Runtime.getRuntime().load(name);
例如：
System.load("/home/avh/lib/libX11.so");
Runtime.getRuntime().load("/home/avh/lib/libX11.so");
--函数loadLibrary(String libname)的作用是，加载由libname参数指定的系统库，将库名映射到实际系统库的方法取决于系统，调用System.loadLibrary(name)实际上等效于调用：
Runtime.getRuntime().loadLibrary(name);
方法System.loadLibrary(String)是调用此方法的一种传统而便捷的方式。如果在某个类实现中使用本机方法（JNI调用），则标准的策略是将本机代码放入一个库文件中（称为LibFile），然后在类声明放入一个静态的初始值设定值：
static {
	System.loadLibrary("LibFile");
}
当加载并初始化这个类时，也将加载实现本机方法所需的本机代码。如果用相同库名多次调用此方法，则忽略第二次及后续的调用。

4. 快速复制数组 P380
可能使用System的函数arraycopy()快速地进行数组复制。
void arraycopy(Object src, int srcPos, Object dest, int destPos, int length);
如果dest为null，则抛出NullPointerException异常。
如果src为null，则抛出NullPointerException异常，并且不会修改目标数组。

package test.System;

public class TestSystemArrayCopy
{
	public static void main(String[] args)
	{
		int[] a = new int[10];
		for (int i = 0; i < 10; ++i)
		{
			a[i] = i + 1;
		}
		
		int[] b = new int[5];
		System.arraycopy(a, 2, b, 1, 4);
		
		for(int i = 0; i < 5; ++i)
		{
			System.out.println("b" + "=" + b[i]);
		}
	}
}
输出如下：
b=0
b=3
b=4
b=5
b=6

5. 取得系统时间 P381
System还有一个最常用的函数，就是取得当前系统时间的函数System.currentTimeMillis()。该函数返回的是毫秒时间，是一个long型整数，例如1221960727312。
该函数在进行系统性能监控时最常用到。可以在监控的前面和后面分别添加该函数，以计算这段代码执行的毫秒级时间，比如：
long start = System.currentTimeMillis();
//要监控的代码段
long end = System.currentTimeMillis();
System.out.print(end - start);
两个变量start和end，可以计算出执行的时间长度，这两个变量必须在同一个函数中，有时要监控的代码分布在各个类中，这时可以直接使用下面的代码输出该代码执行的时刻时间：
System.out.println("当前位置……" + System.currentTimeMillis());
会在程序执行的过程中输出一系列时间，可以看出在哪些代码执行的进户是多少，分析出哪一段代码执行的时间长。

除此之外，还可以使用System.nanoTime()返回最精确的可用系统计时器的当前值，以毫微秒为单位。此方法只能用于测量已过的时间。
如，测试某些代码执行的时间长度：
long startTime = System.nanoTime();
//...
long estimatedTime = System.nanoTime() - startTime;
可见，System.nanoTime()比System.currentTimeMillis()更精确。

//P382
package test.System;

public class TestSystemTime
{
	public static void main(String[] args)
	{
		long startMillisTime = System.currentTimeMillis();
		long startNanoTime = System.nanoTime();
		System.out.println("startMillisTime=" + startMillisTime);
		System.out.println("startNanoTime=" + startNanoTime);
		
		int[] a = new int[10];
		for (int i = 0; i < 10; ++i)
		{
			a[i] = i + 1;
		}
		
		int[] b = new int[5];
		System.arraycopy(a, 2, b, 1, 4);
		
		for(int i = 0; i < 5; ++i)
		{
			System.out.println("b" + "=" + b[i]);
		}
		long endMillisTime = System.currentTimeMillis();
		long endNanoTime = System.nanoTime();
		System.out.println("endMillisTime=" + endMillisTime);
		System.out.println("endNanoTime=" + endNanoTime);
		
		System.out.println("MillisTime: " + (endMillisTime - startMillisTime));
		System.out.println("NanoTime:" + (endNanoTime - startNanoTime));
	}
}
输出如下：
startMillisTime=1457859199354
startNanoTime=11571300768943
b=0
b=3
b=4
b=5
b=6
endMillisTime=1457859199356
endNanoTime=11571300995050
MillisTime: 2
NanoTime:226107

6. 系统退出命令 P383 
System.exit(int status)用来终止当前正在运行的Java虚拟机。非0状态码表示异常终止。该方法调用Runtime类中的exit()方法。可以调用System.exit(0)来正常退出系统。
package test.System;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestSystemExit
{
	public static void main(String[] args)
	{
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line;
			while ((line = br.readLine()) != null)
			{
				System.out.println(line);
				if (line.equals("bye"))
				{
					System.exit(0);
				}
			}
			br.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
Tips：System.exit(0)换成break也能够退出。System.exit(0)的作用不止这一种情况，在复杂的Java应用程序中，它可以强制所有的程序都退出系统，这就是它与使用break不同的地方。

7. 执行垃圾回收 P384
System.gc()用于运行垃圾回收器。
调用System.gc()实际上等效于调用：
Runtime.getRuntime().gc();
Tips：切不可在代码中频繁地调用垃圾回收。

11.1.2 运行时类Runtime P385
System的很多函数都等价于Runtime.getRuntime()，实际上System是依赖于Runtime类的。
Runtime类不同于System类，它的函数大部分都不是静态的，只有一个静态函数getRuntime()。每个Java应用程序都有一个Runtime类实例，使应用程序能够与其运行的环境相连接，可以通过该函数取得一个Runtime的实例，应用程序不能创建自己的Runtime类实例，然后再调用该类的其他函数。
（1）查看系统内存
package test.Runtime;

public class TestRuntimeMemory {
    public static void main(String[] args) {
        System.out.println("内存总量：" + Runtime.getRuntime().totalMemory());
        System.out.println("最大内存量：" + Runtime.getRuntime().maxMemory());
        System.out.println("空闲内存量：" + Runtime.getRuntime().freeMemory());
    }
}
输出如下：
内存总量：16252928
最大内存量：259522560
空闲内存量：15470896
这里内存数值单位是字节byte

（2）终止JVM虚拟机
使用exit(int status)通过启动虚拟机的关闭序列，终止当前正在运行的Java虚拟机。还有一个方法halt(int status)，用来强行终止正在运行的Java虚拟机。
halt()方法是强制性、不安全的，通常建议使用exit()，只有在迫不得已的情况下才使用halt()，如网络连接断掉这种不可控因素。

（3）运行系统程序
打开记事本：
package test.Runtime;

public class TestRuntimeExec {
    public static void main(String[] args) {
        try {
            Runtime.getRuntime().exec("notepad");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

打开word：
Runtime.getRuntime().exec("cmd /c start Winword");

执行批处理：通过批处理文件来执行一系列的任务，如执行Ant脚本等，对于编写的脚本文件run.bat，可以使用下面方式运行。
Runtime.getRuntime().exec("cmd.exe /C start d:/demo/run.bat");

还可以使用Process类的waitFor()函数来等待当前进程的执行，如有必要，一直要等到由该Process对象表示的进程已经终止。如下所示，可以根据10.2.1 节一行获得进程的输入流，等待进程运行结束后再继续Java主线程的运行：
Process p = Runtime.getRuntime().exec("d:/demo/run.bat");
InputStream in = p.getInputStream();
String line = null;
BufferedReader reader = new BufferedReader(new InputStreamReader(in));
while ((line = reader.readLine()) != null) {
    System.out.println(line);
}
in.close();
p.waitFor(); //等待进程结束

这在实际的开发中是需要的。如编写了一个Ant脚本，用来编译一批Java类，只有编译结束后，才能够继续执行下面的代码，来调用刚才Ant脚本编译后的class类文件，显然必须等待进程结束后才能够继续执行。

（4）使用关闭钩子 P386
关闭钩子应该快速地完成其工作，当程序调用exit时，虚拟机应该迅速地关闭并退出。由于用户注销或系统关闭而终止虚拟机时，底层的操作系统可能只允许在固定的时间内关闭并退出。因此在关闭钩子中尝试进行任何经用户交互或执行长时间的计算都是不明智的。
如下程序演示了添加关闭钩子的方法，在方法addShutDownHook()中定义了一个匿名内部类，其函数体run()中可以用来编写系统退出进需要执行的代码，比如系统数据的缓存等。
package test.Runtime;

import java.util.Scanner;

public class TestRuntimeHook {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("系统正在退出……");
            }
        });

        while (true) {
            String line = readDataFromConsole();
            System.out.println(line);
            if (line.equals("bye")) {
                System.exit(0);
            }
        }
    }
    
    private static String readDataFromConsole()
    {
        Scanner scanner = new Scanner(System.in);//有个scanner未close的提示
        return scanner.nextLine();
    }
}

11.2 Java字符串处理类 P388 
大量字符串实例的随意创建，给系统的效率带来了很大问题。
做测试，对比String类和StringBuffer的执行效率。
package test.Str;

public class TestStringProficience {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String str = "";
        for (int i = 0; i < 10000; ++i) {
            str += "," + i;
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
输出如下：
139
耗时139ms

package test.Str;

public class TestStringProficience {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < 10000; ++i) {
            str.append(",").append(i);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
输出如下：
1
耗时1ms

11.2.1 字符串类String P389
字符串是常量，它们的值在创建之后不能更改，字符串缓存区支持可变的字符串，因为String对象是不可变的，所以可以共享。
字符串的字义很简单，直接给一个字符串类型的变量赋值即可，如：
String str = "abc";
等价于：
char data[] = {'a', 'b', 'c'};
String str = new String(data);

String类包括的方法可用于检查字符串的单个字符、比较字符串、搜索字符串、提取子字符串、创建字符串副本并将所有字符串转换为大写或小写。
（1）提取单个字符
String str = "hello, world!";
System.out.println(str.charAt(10));

（2）提取子字符串
System.out.println(str.substring(1));//取到末尾
System.out.println(str.substring(0, 4));//取到索引endIndex - 1处，因为，该子字符串的长度为endIndex - beginIndex
为确保endIndex不越界，可以使用下面函数取得字符串长度：
int length();
再根据长度查找：substring(0, str.length() - 1);

（3）比较字符串
boolean equals(Object anObject);
boolean equalsIgnoreCase(String str);
Tips：
equals()用来比较两个字符串的值，而不是比较其对象是否相同。如果使用==比较两个字符串对象a==b，那么只有在这两个实例指向同一个字符串对象时才返回true。
//一个测试程序如下：
package test.Str;

public class TestString {
    public static void main(String[] args) {
        String str = "hello";
        String str2 = "hello";
        System.out.println(str == str2);//true
        
        StringBuffer sb = new StringBuffer();
        sb.append(str);
        String strFromSb = sb.toString();
        
        System.out.println(str == strFromSb);//false
        System.out.println(str.equals(strFromSb));//true
    }
}

有时可能比较两个字符串的大小，以便进行排序，可以使用如下两个函数：
int compareTo(String anotherString); //按字典顺序比较两个字符串
int compareToIngoreCase(String str); //忽略大小写

package test.Str;

public class TestString {
    public static void main(String[] args) {
        String str = "hello";
        String str2 = "helln";
        String str3 = "hellp";
        System.out.println(str.compareTo(str2));//1，可以简单的理解为用方法调用的字符串去减参考字符串，o比n大1，减的结果为1
        System.out.println(str.compareTo(str3));//-1，o比p小1，减的结果为-1
    }
}
比较结果为一个整数，有3种情况：
--按字典顺序此String对象位于参数字符串之前，则比较结果为一个负整数。
--按字典顺序此String对象位于参数字符串之后，则比较结果为一个正整数。
--两个字符串相等，则结果为0；compareTo只在方法equals(Object)返回true时才返回0。
（4）定位字符串
startsWith
endsWith

package test.Str;

public class TestString {
    public static void main(String[] args) {
        String str = "hello";
        System.out.println(str.startsWith("he"));//true
        System.out.println(str.startsWith("ll", 0));//false
        System.out.println(str.startsWith("ll", 2));//true
        System.out.println(str.endsWith("lo"));//true，只有一种形式，没有带offset的形式
    }
}

使用下面函数，取得指定字符或字符串在原有字符串中的位置：
indexOf
lastIndexOf

如果索引值为-1 ，表示不包含该字符或子字符串。
//
package test.Str;

public class TestString {
    public static void main(String[] args) {
        String str = "hello";
        System.out.println(str.indexOf('e'));//1
        System.out.println(str.indexOf("l"));//2
        System.out.println(str.indexOf("l", 3));//3
    }
}

（5）匹配字符串 P390
有一个更通用的函数，可以根据正则表达式来进行匹配：
boolean matches(String regex);//告知此字符串是否匹配给定的正则表达式

Tips：
正则表达式的写法将在第13课中讲解。

（6）拆分字符串
将字符串按照某一个子字符串分为一个字符串数组，可以使用函数：
String[] split(String regex);//根据给定正则表达式的匹配拆分此字符串
//
package test.Str;

public class TestString {
    public static void main(String[] args) {
        String str = "hello";
        String strArray[] = str.split("e");
        for (String s : strArray) {
            System.out.println(s);
        }
    }
}
输出如下：
h
llo

（7）替换字符串
replace
replaceAll
replaceFirst

//
package test.Str;

public class TestString {
    public static void main(String[] args) {
        String str = "hello";
        System.out.println(str.replace('e', 'c'));
        System.out.println(str.replaceAll("e", "f"));
        System.out.println(str.replaceFirst("l", "m"));
    }
}

（8）转换大小写
String toLowerCase();
String toUpperCase();

（9）格式化输出
String类还提供了两个静态函数，用来对字符串进行格式化输出：
static String format(Locale l, String format, Ojbect... args);//使用指定的语言环境、格式字符串和参数返回一个格式化字符串
static String format(String format, Object...args);//使用指定的格式字符串和参数返回一个格式化字符串
该格式化函数与格式化输出流PrintStream和PrintWriter用法相同。静态方法，可以直接引用，下面举一个例子：
//
package test.Str;

import java.util.Date;

public class TestString {
    public static void main(String[] args) {
        String str = String.format("%1$tY年%1$tm月%1$td日 %1$tH时%1$tm分%1$tS秒", new Date());
        System.out.println(str);
    }
}
输出如下：
2016年06月10日 18时06分30秒

11.2.2 字符串分隔类StringTokenizer P391
//
package test.Str;

import java.util.StringTokenizer;

public class TestString {
    public static void main(String[] args) {
        StringTokenizer st = new StringTokenizer("this is a test");
        while (st.hasMoreTokens()) {
            System.out.println(st.nextToken());
        }
    }
}
输出如下：
this
is
a
test

StringTokenizer是出于兼容性的原因而被保留的遗留类（虽然在新代码中并不鼓励使用它），建议所有寻求此功能的人使用String的split()方法或java.util.regex包。
//用String.split()方法，输出同上
package test.Str;

public class TestString {
    public static void main(String[] args) {
        String[] result = "this is a test".split("\\s");
        for (int i = 0; i < result.length; ++i) {
            System.out.println(result[i]);
        }
    }
}

11.2.3 线程安全的可变字符串类StringBuffer P392
StringBuffer是一个线程安全的可变字符串类，类似于String的字符串，不同的是它通过某些方法调用可以改变该序列的长度和内容。它可将字符串缓存区安全地用于多个线程，可以在必要时对这些方法进行同步，因此任意特定实例上的所有操作就好像是以串行顺序发生的，该顺序与所涉及的每个线程进行的方法调用顺序一致。
StringBuffer();
StringBuffer(String str);
StringBuffer上的主要操作是append()和insert()方法。append()方法始终将这些字符添加到缓存区的末端；而insert()方法则在指定的点添加字符。
//
package test.Str;

public class TestString {
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer("abc");
        sb.insert(2, "def");
        System.out.println(sb);//abdefc
    }
}
append可以添加boolean,char,char[]（以及带偏移量的）,CharSequence（int start, int end）,double,float,int,long,Object,String。insert拥有同样类型的列表。
同样的，可以使用下面的方法删除一个或多个字符
StringBuffer deleteCharAt(int index);
StringBuffer delete(int start, int end);//删除从start到end-1位置的字符

StringBuffer sb = new StringBuffer("abc");
sb.deleteCharAt(1);//不需要赋值，也可以赋值
System.out.println(sb);//ac

除此之外，StringBuffer还拥有与String同样的函数，如charAt(),indexOf(),lastIndexOf(),substring(),replace()等，它还提供了函数toString()来转化为String对象。

11.2.4 可变字符串类StringBuilder P393
StringBuilder是一个可变的字符序列。此类提供一个与StringBuffer兼容的API，但不保证同步。该类被设计用做StringBuffer的一个简易替换，用在字符串缓存区被单个线程使用的时候（这种情况很普遍）。如果可能，建议优先采用该类，因为在大多数实现中，这比StringBuffer要快。
在StringBuilder上的主要操作是append()和insert()方法，可重载这些方法，以接受任意类型的数据。
如果builder引用StringBuilder的实例，则builder.append(x)和builder.insert(builder.length(), x)具有相同的效果。每个字符串生成器都有一定的容量，只要字符串生成器所包含的字符序列的长度没有超出此容量，就无须分配新的内部缓存区。如果内部缓存区溢出，则此容量自动增大。
Tips：
将StringBuilder的实例用于多个线程是不安全的。如果需要在多个线程中使用字符串，则建议使用StringBuffer。

11.2.5 选择String、StringBuffer与StringBuilder
三者最大的区别如下：
--String是字符串常量
--StringBuffer是字符串变量（线程变量）
--StringBuilder是字符串变量（非线程安全）
简单地说，String类型和StringBuffer类型的主要性能区别其实在于，String是不可变的对象，因此在每次对String类型进行改变的时候，其实都等同于生成了一个新的String对象，然后将指针指向新的String对象，所以经常改变内容的字符串最好不要用String，因为每次生成对象都会对系统性能产生影响，特别当内存中无引用对象多了以后，JVM的GC就会开始工作，速度是一定会相当慢的。
如果是使用StringBuffer类，结果就不一样了，每次结果都会对StringBuffer对象本身进行操作，而不是生成新的对象，再改变对象引用。所以在一般情况下我们推荐使用StringBuffer，特别是字符串对象经常改变的情况下。而在某些特别情况下，String对象的字符串拼接其实是被JVM解释成了StringBuffer对象的拼接，所以这些时候String对象的速度并不会StringBuffer对象慢，而特别是以下字符串对象生成，String对象效率是远要比StringBuffer快的：
String str = "This is only a" + " simple" + " test";
StringBuffer builder = new StringBuffer("This is only a").append(" simple").append(" test");
生成str对象的速度简直太快了，而这个时候StringBuffer居然速度上根本一点都不占优势。其实这是JVM的一个把戏，实际上：
String str = "This is only a" + " simple" + " test";
就是：
String str = "This is only a simple test";
让你以不需要太多的时间。但要注意的是，如果字符串是来自另外的String对象的话，速度就没那么快了，如：
String str2 = "This is only a";
String str3 = " simple";
String str2 = " test";
String str2 = str2 + str3 + str4;

对于这3个类的使用，按照以下情况去选择：
--如果你偶尔对简单的字符串常量进行拼接，那么可以使用String，它足够简单而且轻量级。
--如果你需要经常进行字符串拼接、累加操作，请使用StringBuffer或StringBuilder。
--如果是在单线程的环境中，建议使用StringBuilder，它要比StringBuffer快；如果是在多线程的环境中，建议使用StringBuffer，它是线程安全的。
因此，StringBuilder实际上是我们的首选，只有在多线程时才可以考虑使用StringBuffer，只有在字符串的拼接足够简单时才使用String。

11.3 Java日期处理类 P396
11.3.1 日期类 Date
（1）创建日期
package test.Date;

import java.util.Date;

public class TestDate {
    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date);
        
        long cur = System.currentTimeMillis();
        Date date2 = new Date(cur);
        System.out.println(date2);
    }
}
输出如下：
Mon Jun 13 23:21:39 CST 2016
Mon Jun 13 23:21:39 CST 2016
（2）修改日期
（3）比较日期
（4）输出日期
默认实现了一个toString函数

11.3.2 日期格式化类SimpleDateFormat P397
ToRead

11.3.3 日历类Calendar P399
ToRead

11.4 Java数字处理类P400
11.4.1 数学函数类 Math P401
Math类包含用于执行基本数学运算的方法，如初等指数、对象、平方根和三角函数。提供了一系列的静态方法，用来执行各种数学函数，它提供的数学函数列表如下：
--绝对值函数
--最大值和最小值函数
--取整函数
--弦与切函数
--幂与开方函数
--对数函数
--角度函数
--随机函数

11.4.2 随机数类 Random P402
new Random();
Random(long seed);
Tips：使用种子创建的随机生成器，可以在每次设置不同的种子，这样就会产生不同的随机数生成器，保证不会出现相同顺序的随机数，通常我们会使用系统的当前时间毫秒数作为种子。
Random(System.currentTimeMillis());

可以使用下面函数产生各种类型的数据：
--整型数
--浮点数
--布尔值
--字节数组

很多应用程序使用Math.random()，该方法更易于使用，生成的是double数值，如：
public class TestMath {
    public static void main(String[] args) {
        System.out.println(Math.random());
    }
}
输出如下：
0.3454156494983418
将该数值乘以一个大的数值将它变为整数，即可作为整数随机数来使用。

11.4.3 基本数据类型转换 P403
Java提供了8种基本数据类型，同样，Java也为这些基本数据类型提供了相应的对象数据类型：
基本数据类型：
byte
short
int
long
float
double
char
boolean
对象数据类型：
Byte
Short
Integer
Long
Float
Double
Character
Boolean

这8种数据类型，除了Charater和Boolean不是数字外，其它的都是数字，因为Java为它们的这一共性提供了一个抽象的父类Number。
抽象类Number是BigDecimal、BigInteger、Byte、Double、Float、Integer、Long和Short类的超类，其中BigDecimal和BigInteger分别表示不可变的，任意精度的有符号十进制数和整数。在这些子类中，最常使用的是Double、Float、Integer、Long这4个类，通常需要使用它们来将字符串转换为浮点型或整型数值。
（1）根据字符串转换浮点数
Double类提供了一个静态的parse方法，Float类也提供了一个静态的parse方法。
package test.Number;

public class TestNumber {
    public static void main(String[] args) {
        double d = Double.parseDouble("123.456");
        float f = Float.parseFloat("123.456");
        System.out.println(d);
        System.out.println(f);
    }
}
输出如下：
123.456
123.456

（2）根据字符串转换整型数
Long类和Integer类，也都提供了一个静态的parse函数来将字符串转化为long和int类型数值：
package test.Number;

public class TestNumber {
    public static void main(String[] args) {
        long l = Long.parseLong("123456");
        int i = Integer.parseInt("123");
        System.out.println(l);
        System.out.println(i);
    }
}
输出如下：
123456
123

Integer类还提供了转换为二进制、八进制和十六进制的函数
package test.Number;

public class TestNumber {
    public static void main(String[] args) {
        String binaryStr = Integer.toBinaryString(10);
        System.out.println(binaryStr);
        
        String octalStr = Integer.toOctalString(10);
        System.out.println(octalStr);
        
        String hexStr = Integer.toHexString(10);
        System.out.println(hexStr);
    }
}
输出如下：
1010
12
a
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第12课 Java常用集合类 P407
12.1 集合类概述 P408
12.1.1 Iterator与Enumeration P408
1. 枚举（Enumeration）接口
Enumeration接口定义了可以对一个对象的类集中的元素进行枚举（一次获得一个）的方法。已经被Iterator所替代，对于新程序来说是过时的，然而它仍被几种从以前版本遗留下来的类（例如Vector、Hashtable和Properties）所定义的方法使用。
要查看Vector中的所有对象，一个办法是用Vector的get(int index)方法，不过这样效率比较低，另一个方法是用Vector的elements()方法返回一个Enumeration对象。
//
import java.util.Enumeration;
import java.util.Vector;

public class TestEnum {
    static public void main(String args[]) {
        Vector<String> strVec = new Vector<>();
        strVec.add("hello");
        strVec.add("world");
        strVec.add("nice day");

        Enumeration<String> enumeration = strVec.elements();
        while (enumeration.hasMoreElements()) {
            String s = enumeration.nextElement();
            System.out.println(s);
        }
    }
}
输出如下：
hello
world
nice day

2. 迭代器（Iterator）接口
推荐使用Iterator对Collection集合进行迭代。迭代器代替了Java集合框架（Java Collections Framework）中的Enumeration。
Collection接口中定义了iterator()方法，用于返回在此Collection的元素上进行迭代的迭代器。
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public class TestEnum {
    static public void main(String args[]) {
        Vector<String> strVec = new Vector<>();
        strVec.add("hello");
        strVec.add("world");
        strVec.add("nice day");

        Iterator<String> iter = strVec.iterator();
        while (iter.hasNext()) {
            String s = iter.next();
            System.out.println(s);
        }
    }
}
输出如下：
hello
world
nice day

import java.util.Iterator;
import java.util.Vector;

public class TestEnum {
    static public void main(String args[]) {
        Vector<String> strVec = new Vector<>();
        strVec.add("hello");
        strVec.add("world");
        strVec.add("nice day");

        Iterator<String> iter = strVec.iterator();
        while (iter.hasNext()) {
            String s = iter.next();
            if (s.equals("world")) {
                iter.remove();
            }
        }
        System.out.print(strVec);
    }
}
输出如下：
[hello, nice day]

Tips：从使用方法上来看Iterator与Enumeration类似。Iterator添加了一个可选的移除操作，并使用较短的方法名。新实现应该优先考虑使用Iterator接口。

12.1.2 Collections与Collection P409
--Collection是个java.util下的接口，提供了对集合对象进行基本操作的通用接口方法。在Java类库中有很多具体的实现，接口的意义是为各种具体的集合提供了最大化的统一操作方式。
--Collections是个java.util下的实体类，它包含有各种有关集合操作的静态方法，提供一系列静态方法实现对各种集合的搜索、排序、线程安全化等操作，就像一个工具类，服务于Java的Collection框架。
因此，Collections是对Collection集合类进行各种操作的服务类。
1. Collection
该接口为其实现类提供了统一的操作接口方法。
--新增一个或多个元素
add
addAll
--删除一个或多个元素
remove
removeAll
clear
isEmpty
size
--判断是否包含一个或多个元素
contains
containsAll
--产生元素集体的数组对象
iterator
toArray
<T>T[] toArray(T[] a);//返回数组的运行时类型与指定数组的运行时类型相同
实现该接口的子类都可以使用这些函数

2. Collections
Collections完全由在Collection上进行或返回Collection的静态方法组成。它包含在Collection上操作的多态算法，即“包装器”，包装器返回由指定Collection支持的新Collection，以及少数其他内容。
该类提供了一系列功能的静态操作函数。
--新增：
boolean addAll(Collection<? super T> c, T...elements);
--填充：
void fill(List<? super T> list, T obj);//使用指定元素替换指定列表中的所有元素
--复制
void copy(list<? super T> dest, List<? super T> src);
--替换
boolean replaceAll(List<T> list, T oldVal, T newVal);
--排序
void sort(List<T> list);
void sort(List<T> list, Comparator<? super T> c);
--搜索
int binarySearch(List<? extends Comparable<? super T> list, T key);//使用二分搜索法搜索指定列表，以获得指定对象
int binarySearch(List<? extends T> list, T key, Comparator<? super T> c);//使用二分搜索法搜索指定列表，以获得指定对象
--查找位置：
int indexOfSubList(List<?> source, List<?> target);
int lastIndexOfSubList(List<?> source, List<?> target);
--求最大或最小元素
max(Collection<? extends T> coll);//根据元素的自然顺序，返回给定Collection的最大元素
max(Collection<? extends T> coll, Comparator<? super T> comp);//根据指定比较器产生的顺序，返回给定Collection的最大元素
min(Collection<? extends T> coll);
min(Collection<? extends T> coll, Comparator<? super T> comp);

12.1.3 Arrays与数组 P411
在Java所有的“存储及随机访问一连串对象”的做法中，数组是最有效率的一种。普通的数组也有它的缺点：效率高，但容量固定且无法动态改变；无法判断其中实际存有多少元素，length只是告诉我们数组的容量。
Arrays类，专门用来操作数组。它拥有一组静态函数，包含用来操作数组（如排序和搜索）的各种方法。这些函数操作的数组都是Java基本数据类型的数组，具体的函数用法有很多，以操作整型数组为例讲解：
--填入 fill()
--复制 copyOf()
--比较 equals()
--搜索 binarySearch()
--排序 sort()
其中的数组赋值功能与System.arraycopy()相似。
public class TestEnum {
    static public void main(String args[]) {
        int array[] = new int[5];
        System.out.print(array.length);//5
    }
}

12.1.4 Dictionary字典 P411
Dictionary是字典类，它是任何可将键映射到相应值的类（如Hashtable）的抽象父类。每个键和每个值都是一个对象。在任何一个Dictionary对象中，每个键最多与一个值相关联。给定一个Dictionary和一个键，就可以查找所关联的元素。任何非null对象都可以用做键或值。
该抽象类提供了以下抽象方法，用来实现对Dictionary实例的枚举、查找、删除、保存操作，并可以取得数组大小，判断是否为空：
枚举：abstract Enumeration<V> elements();
聚会：abstract V get(Object key);
判断为空：abstract boolean isEmpty();
键枚举：abstract Enumeration<K> keys();
设值：abstact V put(K key, V value);
删除：abstract V remove(Object key);
取得大小：abstract int size();

这些函数是抽象的，它的子类必须实现它们。目前DIctionary类只有Hashtable在使用，因此在Hashtable中就提供了以上这些抽象函数的具体实现。Properties继承自Hashtable，因此它也拥有这些函数的功能。
Tips：此类已过时。新的实现应该实现Map接口，而不是扩展此类。

12.1.5 Queue队列 P412
从图12-1 中可以看出，实现Collection接口的第一个类是Queue队列类，该接口还有一个抽象子类AbstractQueue，继而有一系列的实现类，如优先队列PriorityQueue，还有ArrayBlockingQueue、XXX。
1. Queue队列接口
Queue除了基本的Collection操作外，还提供其他的插入、提取和检查操作。每个方法都存在两种形式：一种抛出异常（操作失败时），另一种返回一个特殊值（null或false，具体取决于操作）。插入操作的后一种形式是用于专门为有容量限制的Queue实现设计的；在大多数实现中，插入操作不会失败。
插入 add offer
获取移除 remove poll
获取不移除 element peek

remove()和poll()方法可移除和返回队列的头，到底从队列中移除哪个元素是队列排序策略的功能，而该策略在各种实现中是不同的。remove()和poll()方法仅在队列为空时其行为有所不同：remove()方法抛出一个异常，而poll()方法则返回null。
element()和peek()返回但不移除队列的头。

2. AbstractQueue抽象类
此类提供某些Queue操作的抽象实现。？（不是特别懂）

3. 优先级PriorityQueue P413
一个基于优先级堆的无办优先级队列。优先级队列的元素按照其自然顺序进行排序，或者根据构造队列时提供的Comparator进行排序，具体取决于所使用的构造方法。不允许使用null元素。依靠自然顺序的优先级队列还不允许插入不可比较的对象。
此队列的头是按指定排序方法确定的最小元素。如果多个元素都是最小值，则头是其中一个元素——选择方法是任意的。队列获取操作poll()、remove()、peek()和element()访问处于队列头的元素。
优先级队列是无界的，但是有一个内部容量，控制着用于存储队列元素的数组大小。它通常至少等于队列的大小。随着不断向优先级队列添加元素，其容量会自动增加。无须指定容量增加策略的细节。
此类及其迭代器实现了Collection和Iterator接口的所有可选方法。方法iterator()中提供的迭代器不保证以任何特定的顺序遍历优先级队列中的元素。如果需要按顺序遍历，请考虑使用Arrays.sort(pq.toArray())。
如下例：
import java.util.PriorityQueue;
import java.util.Queue;

public class TestEnum {
    static public void main(String args[]) {
        Queue queue = new PriorityQueue<>();
        queue.add("abc");
        queue.add("111");
        queue.add("def");
        System.out.println(queue.poll());
        System.out.println(queue.element());
    }
}
输出如下：
111
abc

由于它按照自然排序，因此输出结果为111，abc。（ANSI码中，111排在abc前）
Tips：此实现不是同步的。如果多个线程中的任意线程修改了队列，则这些线程不应同时访问PriorityQueue实例。相反，请使用线程安全的PriorityBlockingQueue类。
queue.add("222");
queue.add("111");
queue.add("333");
System.out.println(queue.poll());
System.out.println(queue.poll());
System.out.println(queue.element());
输出为：
111
222
333

12.2 列表类List P414
List接口对Collection进行了简单的扩充，具体实现类常用的有ArrayList和LinkedList。
--ArrayList是一种类似数组的形式进行存储，随机访问速度极快。
--LinkedList的内部实现是链表，适合于在链表中间需要频繁进行插入和删除操作。

12.2.1 抽象类AbstractList与AbstractSequentialList P414
AbstractList提供List接口的抽象实现，以最大限度地减少实现“随机访问”数据存储（如数组）支持的该接口所需的工作。对于连续的访问数据（如链表），应优先使用AbstractSequentialList，而不是此类。
在继承AbstractList时：
--如果要实现不可修改的列表，只需扩展此类，并提供get(int)和size()方法的实现。
--要实现可修改的列表，必须另外重写set(int,E)方法。
--如果列表为可变大小，则必须另外重写add(int,E)和remove(int)方法。
目前继承自AbstractList的实现类有ArrayList和Vector。
AbstractSequentialList与在列表的列表迭代器上实现“随机访问”方法的AbstractList类相对立。即AbstractList的迭代器方法是随机访问方法，AbstractSequentialList的迭代器方法不是随机访问方法。

12.2.2 链表LinkedList P415
LinkedList是List接口的链接列表实现。实现所有可选的列表操作，并且允许所有元素（包括null）。除了实现List接口外，LinkedList类还为在列表的开头及结尾get()、remove()和add()元素提供了统一的命名方法。这些操作允许将链接列表用做堆栈、队列或双端队列。如下：
addFirst
addLast

boolean offerFirst
boolean offerLast

getFirst
getLast

removeFirst
removeLast

peekFirst//获取不移除
peedLast

pollFirst//获取移除
pollLast

所有操作都是按照双重链接列表的需要执行的。在列表中编索引的操作将从开头或结尾遍历列表。

import java.util.LinkedList;

public class TestList {
    static public void main(String args[]) {
        LinkedList list = new LinkedList();
        list.add("abc");
        list.add("111");
        list.add("def");
        System.out.println(list.getFirst());
        System.out.println(list.getLast());
    }
}
输出如下：
abc
def
Tips：此实现不是同步的。如果多个线程同时访问一个链接列表，而其中至少一个线程从结构上修改了该列表，则它必须保持外部同步。最好在创建时完成这一操作，以防止对列表进行意外的不同步访问，如下所示：
List list = Collections.synchronizedList(new LinkedList(...));

12.2.3 可变数组 ArrayList P416
ArrayList是List接口的大小可变数组的实现。实现了所有可选列表操作，并允许包括null在内的所有元素。除了实现List接口外，还提供一些方法来操作内部用来存储列表的数组的大小（此大大致上等同于Vector类，除了此类是不同步的）。
每个ArrayList实例都有一个容量，指用来存储列表元素的数组的大小，它总是至少等于列表的大小。随着向ArrayList中不断添加元素，其容量也自动增长。并未指定增长策略的细节，因为这不只是添加元素会带来分摊固定时间开销那样简单。可以使用默认构造函数创建容量为10的列表，也可以初始化指定容量大小。
ArrayList();//构造一个初始容量为10的空列表
ArrayList(int initialCapacity);//构造一个具有指定初始容量的空列表

的添加大量元素前，可以使用ensureCapacity操作来增加ArrayList实例的容量。可以减少递增式再分配的数量。其函数形式如下：
void ensureCapacity(int minCapacity);//增加容量

//
import java.util.ArrayList;

public class TestList {
    static public void main(String args[]) {
        ArrayList list = new ArrayList(20);
        list.add("abc");
        list.add("111");
        list.add("def");
        list.ensureCapacity(100);
        System.out.println(list.size());
    }
}
输出如下：
3
Tips：此实现不是同步的。如果多个线程同时访问一个ArrayList实例，而其中至少一个线程从结构上修改了列表，那么它必须保持外部同步。最好在创建时完成，以防止意外对列表进行不同步的访问：
List list = Collections.synchronizedList(new ArrayList(...));

12.2.4 向量Vector P416
Vector类可以实现可增长的对象数组，与数组一样，可以使用整数索引进行访问。但是，Vector的大小可以根据需要增大或者缩小，以适应创建Vector后进行添加或移除项的操作。
import java.util.Vector;

public class TestList {
    static public void main(String args[]) {
        Vector v = new Vector();
        v.addElement("one");
        v.addElement("two");
        v.addElement("three");
        v.addElement("two");
        System.out.println(v);

        v.removeElement("two");//只能删除掉第一个
        System.out.println(v);

        v.addElement(111);//在不指定泛型类型的情况下，可以放入任意类型对象，如果v声明为Vector<String>，则此处编译报错
        System.out.println(v);
    }
}
输出如下：
[one, two, three, two]
[one, three, two]
[one, three, two, 111]

//
import java.util.Vector;

public class TestList {
    static public void main(String args[]) {
        Vector v = new Vector(4);
        v.add("test0");
        v.add("test1");
        v.add("test0");
        v.add("test2");
        v.add("test2");

        v.remove("test0");
        v.remove(0);
        int size = v.size();
        System.out.println("size:" + size);

        for (int i = 0; i < v.size(); ++i) {
            System.out.println(v.get(i));
        }
        // for (Object s : v) { //这里不能用String，类型不匹配
            // System.out.println(s);
        // }
    }
}
输出如下：
size:3
test0
test2
test2

Vector在参数传递中发挥着举足轻重的作用，通常都使用Vector作为参数传递的对象。

12.2.5 堆栈 P418
Stack类表示后进先出（LIFO）的对象堆栈。通过5个操作对类Vector进行了扩展，允许将向量视为堆栈。提供了通常的push()和pop()操作，以及取堆栈顶点的peek()方法、测试堆栈是否为空的empty()方法、在堆栈中查找项并确定到堆栈顶距离 的search()的方法。
import java.util.EmptyStackException;
import java.util.Stack;

public class TestList {
    static public void main(String args[]) {
        Stack st = new Stack();
        System.out.println("stack:" + st);
        st.push(new Integer(42));
        System.out.println("push(42)");
        System.out.println("stack:" + st);

        st.push(new Integer(43));
        System.out.println("push(43)");
        System.out.println("stack:" + st);

        System.out.print("pop -> ");
        Integer i = (Integer) st.pop();
        System.out.println(i);
        System.out.println("stack:" + st);

        Integer i1 = (Integer) st.pop();
        System.out.println(i1);
        System.out.println("stack:" + st);

        try {
            st.pop();
        } catch (EmptyStackException e) {
            System.out.println("empty stack");
        }
    }
}
输出如下：
stack:[]
push(42)
stack:[42]
push(43)
stack:[42, 43]
pop -> 43
stack:[42]
42
stack:[]
empty stack

12.3 集合类Set P419
Set接口也是Collection的一种扩展，与List不同的是，Set中的对象元素不能重复。常用具体实现有HashSet和TreeSet类。
--HashSet能快速定位一个元素，但是放到HashSet中的对象需要实现hashCode()方法，它使用哈希码的算法。
--TreeSet则将放入其中的元素按序存放，这就要求放入其中的对象是可排序的，这就用到了集合框架提供的另外两个实用类Comparable和Comparator。一个类是可排序的，它就应该实现Comparable接口。有时多个类具有相同的排序算法，就不需要分别在每个类中重复定义相同的排序算法，只要实现Comparator接口即可。集合框架中还有两个很实用的公用类：Collections和Arrays。Collections提供了对一个Collection容器进行诸如排序、复制、查找和填充等一些非常有用的方法，Arrays则是对一个数组进行类似的操作。

12.3.1 抽象类AbstractSet与接口SortedSet
Set有一个抽象类和接口，分别是AbstractSet和SortedSet。AbstractSet为实现哈希做准备，SortedSet为实现具有排序功能的TreeSet做准备。
1. AbstractSet通过扩展此类来实现一个Set的过程与通过扩展AbstractCollection来实现Collection的过程是相同的，除了此类的子类中的所有方法和构造方法都必须服从Set接口所强加的额外限制（例如，add()方法不允许将一个对象的多个实例添加到一个Set中）。
Tips：此类并没有重写AbstractCollection类中的任何实现，它仅仅添加了equals()和hashCode()的实现。

2. 排序接口SortedSet
SortedSet进一步提供了关于元素的总体排序的Set。这些元素使用其自然顺序进行排序，或者根据通常在创建有序Set时提供的Comparator进行排序。所有这些元素都必须是可互相比较的。
所有通用有序Set实现类都应该提供4个“标准”构造方法。
--void（无参数）构造方法：它创建一个空的有序Set，按照元素的自然顺序进行排序。
--带有一个Comparator类型参数的构造方法：它创建一个空的有序Set，根据指定的比较器进行排序。
--带有一个Collection类型参数的构造方法：它创建一个新的有序Set，其元素与参数相同，按照元素的自然顺序进行排序。
--带有一个SortedSet类型参数的构造方法：它创建一个新的有序Set，其元素和排序方法与输入的有序Set相同。
TreeSet就是一个排序接口SortedSet的实现。

12.3.2 哈希集合HashSet P420
HashSet由哈希表（实现上是一个HashMap实例）支持，为基本操作提供了稳定性能，这些基本操作包括add()、remove()、contains()和size()，假定哈希函数将这些元素正确地分布在桶中。对此set()进行迭代所需的时间与HashSet实例的大小（元素的数量）和底层HashMap实例（桶的数量）的“容量”的和成比例。因此，如果迭代性能很重要，则不要将初始容量设置得太高（或将加载因子设置得太低）。
为了在该类的实例中保存数据，数据对象必须拥有hashCode()函数。如下例：
//
import java.util.HashSet;
import java.util.Iterator;

class Student {
    private int num;
    private String name;

    public Student(int num, String name) {
        this.num = num;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return num * name.hashCode();
    }

    public boolean equals(Student student) {
        return num == student.num && name.equals(student.name);
    }
}

public class TestHashSet {
    static public void main(String args[]) {
        HashSet<Student> hs = new HashSet<>();
        hs.add(new Student(1, "zhangsan"));
        hs.add(new Student(2, "lisi"));
        hs.add(new Student(3, "wangwu"));
        hs.add(new Student(1, "zhangsan"));

        Iterator<Student> iter = hs.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
输出如下：
Student{num=3, name='wangwu'}
Student{num=2, name='lisi'}
Student{num=1, name='zhangsan'}
Student{num=1, name='zhangsan'}

由此可见，HashSet是一类特殊的List，它添加的元素必须实现hashCode()函数。
Tips：此实现不是同步的。如果多个线程同时访问一个哈希Set，而其中至少一个线程修改了该Set，那么它必须保持外部同步。这通常是通过对自然封装该Set的对象执行同步操作来完成的。如果不慧这样的对象，则应该使用Collections.synchronizedSet方法来“包装”Set。最好在创建时完成这一操作，以防止对该Set进行意外的非同步访问。
Set s = Collections.synchronizedSet(new HashSet(...));

12.3.3 树集合TreeSet P421
TreeSet是基于TreeMap的NavigableSet袜。使用元素的自然顺序对元素排序，或根据创建Set时提供的Comparator进行排序，取决于使用的构造方法。此实现为基本操作（add、remove和contains）提供受保证的log(n)时间开销。
自己写的类要能够添加到TreeSet中进行排序，该类需要实现Comaprable接口。如下：
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

class Student implements Comparable {
    private int num;
    private String name;

    public Student(int num, String name) {
        this.num = num;
        this.name = name;
    }

    @Override
    public int compareTo(Object o) {
        Student s = (Student) o;
        //如下比较是我自己加的
        if (this.num < s.num) {
            return -1;
        } else if (this.num > s.num) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }
}

public class TestHashSet {
    static public void main(String args[]) {
        TreeSet<Student> hs = new TreeSet<>();
        hs.add(new Student(3, "wangwu"));
        hs.add(new Student(1, "zhangsan"));
        hs.add(new Student(2, "lisi"));
        System.out.println(hs.add(new Student(1, "zhangsan2")));//增加失败

        Iterator<Student> iter = hs.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
输出如下：
false
Student{num=1, name='zhangsan'}
Student{num=2, name='lisi'}
Student{num=3, name='wangwu'}

//原书中代码
import java.util.Iterator;
import java.util.TreeSet;

class Student implements Comparable {
    private int num;
    private String name;

    public Student(int num, String name) {
        this.num = num;
        this.name = name;
    }

    @Override
    public int compareTo(Object o) {
        Student s = (Student) o;
        int ret = num > s.num ? 1 : (num == s.num ? 0 : -1);
        if (ret == 0) {
            ret = name.compareTo(s.name);//如果编号一样，再使用name比较
        }
        return ret;
    }

    @Override
    public String toString() {
        return "Student{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }
}

public class TestHashSet {
    static public void main(String args[]) {
        TreeSet<Student> hs = new TreeSet<>();
        hs.add(new Student(3, "wangwu"));
        hs.add(new Student(1, "zhangsan"));
        hs.add(new Student(2, "lisi"));
        System.out.println(hs.add(new Student(1, "zhangsan2")));

        Iterator<Student> iter = hs.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
输出如下：
true
Student{num=1, name='zhangsan'}
Student{num=1, name='zhangsan2'}
Student{num=2, name='lisi'}
Student{num=3, name='wangwu'}

由此可见，TreeSet是一类特殊的List，它添加的元素必须实现可比较的接口。

//另外使用额外的构造器的代码
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

class Student {
    protected int num;//修改为protected，同一个文件里的类就可以访问到
    protected String name;

    public Student(int num, String name) {
        this.num = num;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }
}

class StudentComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        int ret = s1.num > s2.num ? 1 : (s1.num == s2.num ? 0 : -1);
        if (ret == 0) {
            ret = s1.name.compareTo(s2.name);
        }
        return ret;
    }
}

public class TestHashSet {
    static public void main(String args[]) {
        TreeSet<Student> hs = new TreeSet<>(new StudentComparator());
        hs.add(new Student(3, "wangwu"));
        hs.add(new Student(1, "zhangsan"));
        hs.add(new Student(2, "lisi"));
        hs.add(new Student(1, "zhangsan2"));

        Iterator<Student> iter = hs.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
输出如下：
Student{num=1, name='zhangsan'}
Student{num=1, name='zhangsan2'}
Student{num=2, name='lisi'}
Student{num=3, name='wangwu'}

//还可以修改为这种匿名类的形式
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

class Student {
    protected int num;//修改为protected，同一个文件里的类就可以访问到
    protected String name;

    public Student(int num, String name) {
        this.num = num;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }
}

public class TestHashSet {
    static public void main(String args[]) {
        TreeSet<Student> hs = new TreeSet<>(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                int ret = o1.num > o2.num ? 1 : (o1.num == o2.num ? 0 : -1);
                if (ret == 0) {
                    ret = o1.name.compareTo(o2.name);
                }
                return ret;
            }
        });
        hs.add(new Student(3, "wangwu"));
        hs.add(new Student(1, "zhangsan"));
        hs.add(new Student(2, "lisi"));
        hs.add(new Student(1, "zhangsan2"));

        Iterator<Student> iter = hs.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
Tips：此实现不是同步的。如果多个线程同时访问一个TreeSet，而其中至少一个线程修改了该Set，那么它必须保持外部同步。这一般是通过对自然封装该Set的对象执行同步操作来完成的。如果不存在这样的对象，则应该使用Collections.synchronizedSortedSet方法来“包装”该Set。此操作最好在创建时进行，以防止对Set进行意外的非同步访问：
SortedSet s = Collections.synchronizedSortedSet(new TreeSet(...));

12.4 映射类Map P423
Map是一种把键对象和值对象进行关联的容器，而一个值对象又可以是一个Map。对于键对象来说，像Set一样，一个Map容器中的键对象不允许重复，这是为了保持查找结果的一致：如果有两个键对象一样，想得到那个键对象所对应的值对象时就有问题了。所以键的唯一性很重要，也是符合集合的性质的。当然在使用过程中，某个键所对对应的值对象可能会发生变化，这时会按照最后一次修改的值对象与键对应。
Map有两种比较常用的实现：HashMap和TreeMap。
--HashMap也用到了哈希码的算法，以便快速查找一个键。
--TreeMap是对键按序存放，因此它便有一些扩展的方法，如firstKey()、lastKey()等，还可以从TreeMap中指定一个范围以取得其子Map。
键和值的关联很简单，用put(Object key, Object value)方法。用get(Object key)可得到与此key对象所对应的值对象。

12.4.1 抽象类AbstractMap与接口SortedMap、NavigableMap
Map有一个抽象类和两个接口，分别是AbstractMap、SortedMap和NavigableMap，NavigableMap是SortedMap的子接口。Abstract为实现哈希做准备，SortedMap为实现具有排序功能的TreeMap做准备。
1. 哈希抽象类AbstractMap
此类提供Map接口的抽象实现，以最大限度地减少实现此接口所需的工作。它实现了对Hash映射的几种操作。
--保存键值V put(K key, V value);
--删除键值V remove(Object key);：如果存在一个键的映射关系，则将其从此映射中移除。
--取得键值V get(Object key);：返回指定键所映射的值；如果此映射不包含该键的映射关系，则返回null。
--取得键集合Set<K> keySet();：返回此映射中包含的键的Set视图。
要实现不可修改的映射，编程人员只需要扩展此类并提供entrySet()方法的实现即可，该方法将返回映射的映射关系Set视图。通常，返回的set()将依次在AbstractSet上实现。此set()不支持add()或remove()方法，其迭代器也不支持remove()方法。
要实现可修改的映射，编程人员必须另外重写此类的put()方法（否则将抛出UnsupportedOperationException），entrySet().iterator()返回的迭代器也必须另外实现其remove()方法。

2. 排序接口SortedMap
该接口进一步提供关于键的总体排序的Map。该映射是根据其键的自然顺序进行排序的，或者根据通常在创建有序映射时提供的Comparator进行排序。它为排序后的映射增加了如下两个函数，分别用于取得第一个和最后一个键：
K firstKey();
K lastKey();
插入有序映射的所有键都必须实现Comparable接口（或者被指定的比较器接受）。另外，所有这些键都必须是可互相比较的：对有序映射的中任意两个键k1和k2执行k1.compareTo(k2)（或comparator.compare(k1,k2)）都不得抛出ClassCastException。试图违反此限制将导致违反规则的方法或者构造方法调用抛出ClassCastException。

3. 导航接口NavigableMap
该接口扩展自SortedMap，具有针对给定搜索目标返回最接近匹配项的导航方法。方法lowerEntry()、floorEntry()、ceilingEntry()、higherEntry()分别返回小于、小于等于、大于等于、大于给定键的键关联的Map.Entry对象，如果不存在这样的键，则返回null。类似的，方法lowerKey()、floorKey()、ceilingKey()、higherKey()只返回关联的键。所有这些方法是为查找条目而不是遍历条目设计的。
此外，此接口还定义了firstEntry()、pollFirstEntry()、lastEntry()和pollLastEntry()方法，它们返回或移除最小和最大的映射关系（如果存在），否则返回null。

12.4.2 树映射TreeMap
TreeMap既继承了哈希抽象类AbstractMap，又实现了排序接口SortedMap和导航接口NavigableMap。它是基于红黑树（Red-Black tree）算法的实现。该映射根据其键的自然顺序进行排序，或者根据创建临山时提供的Comparator进行排序，具体取决于使用的构造方法：
TreeMap();
TreeMap(Comparator<? super K> comparator);
此实现为containsKey()、get()、put()和remove()操作提供受保证的log(n)时间开销。
与TreeSet相似，自己写的类要能够添加到TreeMap中进行排序，也需要实现Comparable接口或Comparator。
//
import java.util.Iterator;
import java.util.TreeMap;

class Student {
    protected int num;//修改为protected，同一个文件里的类就可以访问到
    protected String name;

    public Student(int num, String name) {
        this.num = num;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }
}

public class TestTreeMap {
    static public void main(String args[]) {
        TreeMap<String, Student> tmap = new TreeMap<>();
        tmap.put("A", new Student(3, "wangwu"));
        tmap.put("B", new Student(1, "zhangsan"));
        tmap.put("C", new Student(2, "lisi"));
        tmap.put("D", new Student(1, "zhangsan2"));

        Iterator<String> iter = tmap.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            Student value = tmap.get(key);
            System.out.println(key + "=" + value);
        }
        System.out.println(tmap);
    }
}
输出如下：
A=Student{num=3, name='wangwu'}
B=Student{num=1, name='zhangsan'}
C=Student{num=2, name='lisi'}
D=Student{num=1, name='zhangsan2'}
{A=Student{num=3, name='wangwu'}, B=Student{num=1, name='zhangsan'}, C=Student{num=2, name='lisi'}, D=Student{num=1, name='zhangsan2'}}

由此可见，TreeMap是一类特殊的Map，它添加的元素必须实现可比较的接口。
Tips：此实现不是同步的。如果多个线程同时访问一个映射，并且其中至少一个线程从结构上修改了该映射（结构上的修改是指添加或删除一个或多个映射关系的操作；仅改变与现有 键关系的值不是结构上的修改），则其必须保持外部同步。这一般是通过对自然封装该映射的对象执行同步操作来完成的。如果不存在这样的对象，则应该使用Collections.synchronizedSortedMap()方法来“包装”该映射。最好在创建时完成这一操作，以防止对映射进行意外的不同步访问，如下所示：
SortedMap m = Collections.synchronizedSortedMap(new TreeMap(...));

12.4.3 哈希映射HashMap P426
该类是基于哈希表的Map接口的实现。此实现提供所有可选的映射操作，并允许使用null值和null键（除了非同步和允许使用null之外，HashMap类与Hashtable大致相同）。此类不保证映射的顺序，特别是它不保证该顺序恒久不变。
此实现假定哈希函数将元素适当地分布在各桶之间，可为基本操作get()和put()提供稳定的性能。迭代Collection视图所需的时间与HashMap实例的“容量”（桶的数量）及其大小（键——值映射关系数）成比较。所以，如果迭代性能很重要，则不要将初始容量设置得太高（或将加载因子设置得太低）。
HashMap的实例有两个参数影响其性能：初始容量和加载因子。容量是哈希表中桶的数量，初始容量只是哈希表在创建时的容量。加载因子是哈希表在其容量自动增加之前可以达到多满的一种尺度。如果当哈希表中的条目数超出了加载因子与当前容量的乘积时，则要对该哈希表进行rehash()操作（即重建内部数据结构），从而哈希表将具有大约两倍的桶数。
通常，默认加载因子（0.75 ）在时间和空间成本上寻求一种折中。加载因子过高虽然减少了空间开销，但同时也增加了查询成本（在大多数HashMap类的操作中，包括get和put操作，都反映了这一点）。在设置初始容量时应该考虑到在映射中所需的条目数及其加载因子，以便最大限度地减少rehash操作次数。如果初始容量大于最大条目数除以加载因子，则不会发生rehash操作。
该类提供了3个可用的构造函数：
HashMap();//初始容量16，默认加载因子0.75
HashMap(int initialCapacity);//指定初始容量，默认加载因子0.75
HashMap(int initialCapacity, float loadFactor);//指定初始容量，指定加载因子

如果很多映射关系要存储在HashMap实例中，则相对于按需执行自动的rehash操作以增大表的容量来说，使用足够大的初始容量创建它将使得映射关系能更有效地存储。
与HashSet相似，自己写的类要能够添加到HashMap中，也需要实现hashCode()函数。
//
import java.util.HashMap;
import java.util.Iterator;

class Student {
    protected int num;//修改为protected，同一个文件里的类就可以访问到
    protected String name;

    public Student(int num, String name) {
        this.num = num;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return num * name.hashCode();
    }
}

public class TestHashMap {
    static public void main(String args[]) {
        HashMap<String, Student> tmap = new HashMap<>();
        tmap.put("B", new Student(1, "zhangsan"));
        tmap.put("C", new Student(2, "lisi"));
        tmap.put("A", new Student(3, "wangwu"));
        tmap.put("D", new Student(1, "zhangsan2"));

        Iterator<String> iter = tmap.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            Student value = tmap.get(key);
            System.out.println(key + "=" + value);
        }
        System.out.println(tmap);
    }
}
输出如下：
A=Student{num=3, name='wangwu'}
B=Student{num=1, name='zhangsan'}
C=Student{num=2, name='lisi'}
D=Student{num=1, name='zhangsan2'}
{A=Student{num=3, name='wangwu'}, B=Student{num=1, name='zhangsan'}, C=Student{num=2, name='lisi'}, D=Student{num=1, name='zhangsan2'}}

由此可见，HashMap是一类特殊的Map，它添加的元素必须实现hashCode()函数。
Tips：此实现不是同步的。如果多个线程同时访问一个哈希映射，而其中至少一个线程从结构上修改了该映射（结构上的修改是指添加或删除一个或多个映射关系的任何操作；仅改变与实例已经包含的键关系的值不是结构上的修改），则它必须保持外部同步。这一般通过对自然封装该映射的对象进行同步操作来完成。如果不存在这样的对象，则应该使用Collections.synchronizedMap()方法来“包装”该映射。最好在创建时完成这一操作，以防止对映射进行意外的非同步访问，如下：
Map m = Collections.synchronizedMap(new HashMap(...));

12.4.4 链表哈希映射LinkedHashMap P427
该类是Map接口的哈希表和链接列表实现的，具有可预知的迭代顺序。此实现与HashMap的不同之处在于前者维护着一个运行于所有条目的双重链接列表，此链接列表定义了迭代顺序，该迭代顺序通常就是将键插入到映射中的顺序（插入顺序）。如果在映射中重新插入键，则插入顺序不受影响。
此实现可以让客户避免未指定的、由HashMap（及Hashtable）所提供的通常为杂乱无章的排序工作，同时无须增加与TreeMap相关的成本。使用它可以生成一个与原来顺序相同的映射副本，而与原映射的实现无关：
void foo(Map m) {
    Map copy = new LinkedHashMap(m);
}
该类提供特殊的构造方法来创建链接哈希映射，该哈希映射的迭代顺序就是最后访问其条目的顺序，从近期访问最少到近期访问最多的顺序（访问顺序）。这种映射很适合构造LRU缓存。调用put()或get()方法将会访问相应的条目（假定调用完成后它还存在）。putAll()方法以指定映射的条目集迭代器提供的键——值映射关系的顺序，为指定映射的每个映射关系生成一个条目访问。
下面的函数用于构造一个带指定初始容量、加载因子和排序模式的空LinkedHashMap实例：
LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder);
--initialCapacity：初始容量。
--loadFactor：加载因子。
--accessOrder：排序模式，如果对于访问顺序，则为true；如果对于插入顺序，则为false。
允许null元素，与HashMap一样，可以为基本操作（add、contains和remove）提供稳定的性能，假定哈希函数将元素正确地分布到桶中。由于增加了维护链接列表的开支，其性能很可能比HashMap稍逊一筹，不过这一点例外：LinkedHashMap的Collection视图迭代时间与映射的大小成比例。HashMap迭代时间很可能开支较大，因为它所需要的时间与其容量成比例。
链接的哈希映射具有两个影响其性能的参数：初始容量和加载因子。它们的定义与HashMap极其相似。为初始容量选择非常高的值对此类的影响比对HashMap要小，因为此类的迭代时间不受容量的影响。
//
import java.util.Iterator;
import java.util.LinkedHashMap;

class Student {
    protected int num;//修改为protected，同一个文件里的类就可以访问到
    protected String name;

    public Student(int num, String name) {
        this.num = num;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return num * name.hashCode();
    }

    public boolean equals(Student student) {
        return num == student.num && name.equals(student.name);
    }
}

public class TestLinkedHashMap {
    static public void main(String args[]) {
        LinkedHashMap<String, Student> tmap = new LinkedHashMap<>();
        tmap.put("B", new Student(1, "zhangsan"));
        tmap.put("C", new Student(2, "lisi"));
        tmap.put("A", new Student(3, "wangwu"));
        tmap.put("D", new Student(1, "zhangsan2"));

        Iterator<String> iter = tmap.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            Student value = tmap.get(key);
            System.out.println(key + "=" + value);
        }
        System.out.println(tmap);
    }
}
Tips：此实现不是贴满的。如果多个线程同时访问链接的哈希映射，而其中至少一个线程从结构上修改了该映射，则它必须保持外部同步。这一般通过对自然封装该映射的对象进行同步操作来完成。如果不存在这样的对象，则应该使用Collections.synchronizedMap()方法来“包装”该映射。最好在创建时完成这一操作，以防止对映射进行意外的非同步访问。
Map m = Collections.synchronizedMap(new LinkedHashMap(...));

12.4.5 弱哈希映射 WeakHashMap P428

12.4.6 哈希表Hashtable P429
此类实现一个哈希表，该哈希表将键映射到相应的值。任何非null对象都可以用做键或值。为了成功地在哈希表中存储和获取对象，用做键的对象必须实现hashCode方法和equals方法。
Hashtable的实例有两个参数影响其性能：初始容量和加载因子。容量是哈希表中桶的数量，初始容量就是哈希表创建时的容量。注意，哈希表的状态为open，在发生“哈希冲突”的情况下，单个桶会存储多个条目，这些条目必须按顺序搜索。加载因子是对哈希表在其容量自动增加之前可以达到多满的一个尺度。初始容量和加载因子这两个参数只是对该实现的提示。关于何时及是否调用rehash()方法的具体细节则依赖于该实现。
通常，默认加载因子（0.75 ）在时间和空间成本上寻求一个折中。加载因子过高虽然减少了空间开销，但同时也增加了查找某个条目的时间（在大多数Hashtable操作中，包括get和put操作，都反映了这一点）。
初始容量主要控制空间消耗与执行rehash操作所需要的时间损耗之间的平衡。如果初始容量大于Hashtable所包含的最大条目数除以加载因子，则永远不会发生rehash操作。但是，将初始容量设置太高可能会浪费空间。
如果很多条目要存储在一个Hashtable中，那么与根据需要执行自动rehashing操作来增大表的容量的做法相比，使用足够大的初始容量创建哈希表或者可以更有效地插入条目。
//
import java.util.Hashtable;
import java.util.Iterator;

public class TestHashtable {
    static public void main(String args[]) {
        Hashtable<String, Integer> numbers = new Hashtable<>();
        numbers.put("one", 1);
        numbers.put("two", 2);
        numbers.put("three", 1);

        Iterator<String> iter = numbers.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            Integer value = numbers.get(key);
            System.out.println(key + "=" + value);
        }
        System.out.println(numbers);
    }
}
输出如下：
two=2
one=1
three=1
{two=2, one=1, three=1}
Tips：Hashtable是同步的。

12.4.7 属性Properties P430
Properties类表示了一个持久的属性集。Properties可保存在流中或从流中加载。属性列表中每个键及其对应值都是一个字符串。一个属性列表可包含另一个属性列表作为它的“默认值”；如果未能在原有的属性列表中搜索到属性键，则搜索第二个属性列表。
因为Properties继承于Hashtable，所以可对Properties对象应用put()和putAll()方法。但不建议使用这两个方法，因为它们允许调用者插入其键或值不是String的项。相反，应该使用setProperty()方法。如果在“不安全”的Properties对象（即包含非String的键或值）上调用store()或save()方法，则该调用将失败。如果在“不安全”的Properties对象（即包含非String的键）上调用propertyNames()或list()方法，则该调用将失败。
Tips：Properties主要提供了对属性文件的读取和写入功能，在第14课讲解。
Tips：此类是线程安全的：多个线程可享单个Properties对象而无须进行外部同步。

12.5 对比与选择 P430

12.5.3 Map选择：Hashtable、HashMap、WeakHashMap P433
（1）Hashtable类
由于作为key的对象将通过计算其散列函数来确定与之对应的value的位置，因此任何作为key的对象都必须实现hashCode和equals方法。hashCode和equals方法继承自根类Object，如果用自定义的类当做key的话，要相当小心，按照散列函数的定义，如果两个对象相同，即obj1.equals(obj2)==true，则它们的hashCode必须相同，但如果两个对象不同，则它们的hashCode不一定不同，如果两个不同对象的hashCode相同，这种现象称为冲突，冲突会导致操作哈希表的时间开销增大，所以尽量定义好hashCode()方法，这样能加快哈希表的操作。
如果相同的对象有不同的hashCode，对哈希表的操作会出现意想不到的结果（期待的get方法返回null），要避免这种问题，只需要牢记一条：要同时复写equals方法和hashCode方法，而不要只写其中一个，Hashtable是同步的。
（2）HashMap类
HashMap是非同步的，并且允许null，null value和null key。将HashMap视为Collection时（values()方法可返回Collection），其迭代子操作时间开销和HashMap的容量成比例。因为，如果迭代操作的性能相当重要的话，则不要将HashMap的初始化容量设得过高或者load factor过低。

//------------------------------------------------------------------------------------------------
//Java核心编程技术 第13课 Java正则表达式 P437
13.1 正则表达式语法 P437
13.1.2 正则表达式的匹配字符
Tips：本文采用/expression/的形式，其中的expression即为正则表达式。

/.\b./在匹配“@@@abc”时，匹配结果是：成功；匹配到的内容是：@a P441

“\b”与“^”和“$”类似，本身不匹配任何字符，但是它要求它在匹配结果中所处位置的左右两边，其中一边是“\w”范围，另一边是非“\w”的范围。

（7）向后引用 P442
正则表达式还有一个最重要的特性——后向引用：就是将匹配成功的字符串的某部分进行存储，供以后引用。将一个正则表达式模式或部分模式两边添加圆括号，将导致这部分表达式存储到一个临时缓存区中。如果不想存储，也可以使用非捕获元字符“?:”、“?=”、“?!”来忽略这部分正则表达式的保存。
^(?:Chapter|Section)[1-9][0-9]{0,1}$ 中的?:表达并不想存储括号中的子表达式，括号只是为了匹配|左右两部分

如下三个都为非捕获元：
?:：消除括号中的匹配缓存
?=：为正向预查，在任何开始匹配圆括号内的正则表达式模式的位置来匹配搜索字符串
?!：为负向预查，在任何开始不匹配该正则表达式模式的位置来匹配搜索字符串

所捕获的每个子匹配都按照在正则表达式模式中从左至右所遇到的内容存储。存储子匹配的缓存区编号从1开始，连续编号直至最大99个子表达式。每个缓存区都可以使用“\n”访问，其中n为一个标识特定缓存区的一位或两位十进制数。

后向引用有以下两个应用：
（1）向后引用一个最简单的应用，确定文字中连续出现两个相同单词的位置。
如It is the cost of of gasoline going up up?
表达式
/\b([a-z]+) \1\b/
这个示例中，子表达工是圆括号之间的每一项。所捕获的表达式包括一个或多个字母字符，即由“[a-z]+”所指定的。第二部分是对前面所捕获的子匹配的引用，也就是由附加表达式所匹配的第二次出现的单词。“\1 ”用来指定第一个子匹配。单词边界元字符确保只检测单独的单词。如果不这样，诸如“is issued”或“this is”这样的短语都会被该表达式不正确地识别。
该正则表达式可以匹配of of和up up
使用上面所示的正则表达式，下面的代码可以使用子匹配信息，在一个文字字符串中将连续出现两次的相同单词替换为一个相同的单词：
var ss = "It is the cost of of gasoline going up up?.\n";
var re = /\b([a-z]+) \1\b/
var rv = ss.replace(re, "$1");
在replace方法中使用“$1”来引用 所保存的第一个子匹配。如果有多个子匹配，则可以用“$2”、“$3”等继续引用 。

//如下代码没有替换成？初步分析Java的匹配是全字符匹配
public class TestStringRegex {
    static public void main(String args[]) {
        String ss = "It is the cost of of gasoline going up up?.\n";
        String re = "/\b([a-z]+) \1\b/";
        System.out.println(ss.replace(re, "$1"));
    }
}
//如下返回true，reg="It"则false
public class TestStringRegex {
    static public void main(String args[]) {
        String str = "It is the cost of of gasoline going up up?.";
        String reg = ".*";
        System.out.println(str.matches(reg));
    }
}

（2）向后引用的另一个用途是将一个通用资源指示符（URI）分解为组件部分。假定希望将下述的URI分解为协议（ftp、http、etc），域名地址及页面/路径：
http://msdn.microsoft.com:80/scripting/default.htm
下面的正则表达式可以提供这个功能：
/(\w+):\/\/([^/:]+)(:\d*)?[^# ]*/ //感觉第二个括号中/匹配有问题，不需要加转义符\吗？
将该正则表达式应用于上面所示的URI后，子匹配包含下述内容：
$1 包含 "http"
$2 包含 "msdn.microsoft.com"
$3 包含 ":80"
$4 包含 "/scripting/default.htm"

13.1.3 正则表达式的匹配规则 P444
[0-9\.\-] //匹配所有的数字，句号和减号。减号有特殊的区间含义，所以如果要匹配减号，需要有转义符
[ \f\r\t\n] //匹配所有的白字符

/^[[:alpha:]]{3}$/ //所有的3个字母的单词 这里的alpha不太懂？

特殊字符“?”与{0,1}是相等的，它们都代表着“0个或1个前面的内容”或“前面的内容是可选的”。
特殊字符“*”与{0,}是相等的，它们都代表着“0个或多个前面的内容”。
字符“+”与{1,}是相等的，表示“1个或多个前面的内容”。

在修饰匹配次数的特殊符号后再加一个“?”号，则可以使匹配次数不定的表达式尽可能少的匹配，使可匹配可不匹配的表达式，尽可能“不匹配”。这种匹配原则叫做“非贪婪”模式，也叫做“勉强”模式。如果少匹配就会导致整个表达式失败的时候，与贪婪模式类似，非贪婪模式会最小限度地再匹配一些，以使整个表达式匹配成功。

（5）匹配时反向引用规则 P447
表达式在匹配时，表达式引擎会将小括号“()”包含的表达式所匹配到的字符串记录下来。在获取匹配结果的时候，小括号包含的表达式所匹配到的字符串可以单独获取。在实际应用场合中，当用某种边界来查找，而所要获取的内容又不包含边界时，必须使用小括号来指定所要的范围。
小括号包含的表达式所匹配到的字符串不仅是在匹配结束后才可以使用，在匹配过程中也可以使用。表达式后边的部分，可以引用前面括号内的子匹配已经匹配到的字符串。引用方法是“\”加上一个数字。“\1 ”引用第1对括号内匹配到的字符串，“\2 ”引用第2对括号内匹配到的字符串，以此类推，如果一对括号内包含另一对括号，则外层的括号先排序号。换句话说，哪一对的左括号“(”在前，那这一对就先排序号。

例2：
/(\w)\1{4,}/ 要求\w范围的字符至少重复5次
/\w{5,}/ 表示至少有5个字符
例3：<(\w+)\s*(\w+(=('|").*?\4)?\s*)*>.*?</\1>在匹配 
<td id='td1' style="bgcolor:while"></td>
时，匹配结果是成功。如果<td>与</td>不配对，则会匹配失败；如果改成其他标签配对，也可以匹配成功。

（6）预搜索与懒惰搜索规则 P448
前面讲到了几个代表抽象意义的特殊符号：^ $ \b。它们本身不匹配任何字符，只是对字符串的两头或者字符之间的缝隙附加了一个条件。将介绍另外一种对“两头”或者“缝隙”附加条件的更加灵活的表示方法。
1) 正向预搜索：(?=xxxxx) (?!xxxxx)
格式："(?=xxxxx)"，在被匹配的字符串中，它对所处的“缝隙”或者“两头”附加的条件是：所在缝隙的右侧，必须能够匹配上xxxxx这部分的表达式。因为它只是在此作为这个缝隙上附加的条件，所以它并不影响后边的表达式去真正匹配这个缝隙之后的字符。这就类似“\b”，本身不匹配任何字符。“\b”只是将所在缝隙之前、之后的字符取来进行了一下判断，不会影响后边的表达式来真正的匹配。
例1：表达式/windows(?=NT|98)/在匹配windows98, windowsNT时，将只匹配windows，其它字样则不被匹配
例2：表达式/(\w)((?=\1\1\1)(\1))+/
在匹配字符串aaa ffffff 999999999 时，将可以匹配6个"f"的前4个，可以匹配9个"9"的前7个。这个表达式可以解读成：重复4次以上的字母数字，则匹配其剩下最后2位之前的部分。
（可以理解为：一个字符+(一个缝隙+该字符)*至少一次重复，该缝隙要求右侧有3个该字符，最终缝隙不占结果字符）

格式："(?!xxxxx)"，所在缝隙的右侧，不能匹配xxxxx这部分表达式。
例3：表达式/((?!\bstop\b).)+/
在匹配akldfj kajrlekfdsj adf stop ifdlsa fdksla时，将从头一直匹配到“stop”之前的位置，如果字符串中没有“stop”，则匹配整个字符串。
例4：表达式/do(?!\w)/在匹配字符串“done, do, dog”时，只能匹配“do”。本条举例中，“do”后边使用(?!\w)和使用\b效果是一样的。

2) 反向预搜索：(?<=xxxxx) (?<!xxxxx)
这两种格式的概念和正向预搜索是类似的，反向预搜索要求的条件是：所在缝隙的“左侧”，两种格式分别要求必须能够匹配和不能够匹配指定表达式，而不是去判断右侧。与“正向预搜索”一样的是：它们都是对所在缝隙的一种附加条件，本身都不匹配任何字符。
例5：表达式(?<=\d{4})\d+(?=\d{4})在匹配1234567890123456时，将匹配除了前4个数字和后4个数字之外的中间8个数字。

3) 限制性重复
即{min,max}语法。
可以用\b[1-9][0-9]{3}\b匹配1000~9999 之间的数字。\b[1-9][0-9]{2,4}匹配一个在100~99999 之间的数字。

想匹配单个标签
This is a <EM>first</EM> test.
用<.+>将匹配<EM>first</EM>
用<.+?>来防止贪婪性，将匹配<EM>和</EM>

6) 惰性扩展的一个替代方案，用：<[^>]+>
之所以说这是一个更好的方案在于使用惰性重复时，引擎会在找到一个成功匹配前对每一个字符进行回溯。而使用取反字符集则不需要进行回滚。

13.1.4 全部符号汇总表 P449

13.1.5 常用正则表达式举例 P451

13.2 Java正则表达式详解 P452
13.2.1 使用String类的匹配功能
Java中使用正则表达式的方法非常多，最简单的就是和字符串一起使用。
（1）匹配——matches()方法
matches方法可以判断当前的字符串是否匹配给定的正则表达式。如果匹配，返回true，否则返回false。
//
public class TestStringRegex {
    static public void main(String args[]) {
        String str = "lzb_box@163.com";
        String reg = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        System.out.println(str.matches(reg));//true
    }
}
输出结果：true。表明该地址是合法的。Java中需要使用\\表示一个\

（2）拆分——split()方法
public String[] split(String regex);
public String[] split(String regex, int limit);
其中，limit参数控制模式应用的次数，因此影响所得数组的长度。
若n大于0，模式最多应用n-1 次，数组的长度将不会大于n，最后一项将包含所有超出最后匹配的定界符的输入。
如果n为非正，模式将被应用尽可能多的次数，而且数组可以是任何长度。
如果n为0，模式将被应用尽可能多的次数，数组可以是任何长度，并且结尾空字符串被丢弃。
//
public class TestStringRegex {
    static public void main(String args[]) {
        String s = "GET /index.html HTTP/1.1";
        String ss[] = s.split(" +");
        for (String str : ss) {
            System.out.println(str);
        }
    }
}
输出：
GET
/index.html
HTTP/1.1

第二种重载形式中有一个limit参数，假设为2：
public class TestStringRegex {
    static public void main(String args[]) {
        String s = "GET /index.html HTTP/1.1";
        String ss[] = s.split(" +", 2);
        for (String str : ss) {
            System.out.println(str);
        }
    }
}
输出：
GET
/index.html HTTP/1.1

（3）替换——replaceAll()和replaceFirst()方法
//
public class TestStringRegex {
    static public void main(String args[]) {
        String str = "lzb_box@163.com";
        String reg = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        System.out.println(str.replaceAll(reg, "Email"));//true
    }
}
输出：
Email

13.2.2 使用正则表达式库Pattern和Matcher P453
（1）Pattern表达式
Pattern为字符串的正则表达式，可以直接根据一个正则表达式创建一个Pattern实例：
Pattern p = Pattern.compile("a*b");
当编译模式时，可以设置一个或多个标志，例如：
Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);

下面6个标志：
--CASE_INSENSITIVE
--UNICODE_CASE
--MULTILINE
--UNIX_LINES
--DOTALL
--CANON_EQ

(2)Matcher匹配器
然后使用Pattern对象获得Matcher匹配器对象，创建匹配器后，执行3种不同的匹配操作。
--matches()方法尝试将整个输入序列与该模式匹配。
--lookingAt()尝试将输入序列从头开始与该模式匹配。
--find()方法扫描输入序列以查找与该模式匹配的下一个子序列。
每个方法都返回一个表示成功或失败的布尔值。
//
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatternRegex {
    static public void main(String args[]) {
        Pattern p = Pattern.compile("a*b");
        Matcher m = p.matcher("aaaaab");//这里aaaabc将无法匹配，因为matches是整个输入序列与该模式匹配
        boolean b = m.matches();
        System.out.println(b);//true

        //m.reset("aaaaabc"); b2 = m.lookingAt();
        Matcher m2 = p.matcher("aaaaabc");//lookingAt从头匹配，如果daaaabc将无法匹配
        boolean b2 = m2.lookingAt();
        System.out.println(b2);//true

        //m.reset("aaaaab"); b3 = m.find();
        Matcher m3 = p.matcher("aaaaab");
        boolean b3 = m3.find();
        System.out.println(b3);//true
    }
}

在仅使用一次正则表达式，可以方便地通过此类定义matches()方法，此方法编译表达式并在单个调用中将输入序列与其匹配，如：
boolean b = Pattern.matches("a*b", "aaaab");
它等效于上面三个matches语句，尽管对于重复的匹配而言它效率不高，因为它不允许重用已编译的模式。
如果要多次使用一种模式，编译一次后重用此模式比每次都调用此方法效率更高。

--matches:整个匹配，只有整个字符序列完全匹配成功，才返回True，否则返回False。但如果前部分匹配成功，将移动下次匹配的位置。
--lookingAt:部分匹配，总是从第一个字符进行匹配,匹配成功了不再继续匹配，匹配失败了,也不继续匹配。
--find:部分匹配，从当前位置开始匹配，找到一个匹配的子串，将移动下次匹配的位置。
--reset:给当前的Matcher对象配上个新的目标，目标是就该方法的参数；如果不给参数，reset会把Matcher设到当前字符串的开始处。
//
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatternRegex {
    public static void main(String[] args){
        Pattern pattern = Pattern.compile("\\d{3,5}");
        String charSequence = "123-34345-234-00";
        Matcher matcher = pattern.matcher(charSequence);

        //虽然匹配失败，但由于charSequence里面的"123"和pattern是匹配的,所以下次的匹配从位置4开始
        print(matcher.matches());
        //测试匹配位置
        matcher.find();
        print(matcher.start());

        //使用reset方法重置匹配位置
        matcher.reset();//重载函数可以设置匹配字符串

        
        //find()
        //第一次find匹配以及匹配的目标和匹配的起始位置
        print(matcher.find());
        print(matcher.group()+" - "+matcher.start());
        //第二次find匹配以及匹配的目标和匹配的起始位置
        print(matcher.find());
        print(matcher.group()+" - "+matcher.start());

        
        //lookingAt()
        //第一次lookingAt匹配以及匹配的目标和匹配的起始位置
        print(matcher.lookingAt());
        print(matcher.group()+" - "+matcher.start());

        //第二次lookingAt匹配以及匹配的目标和匹配的起始位置
        print(matcher.lookingAt());
        print(matcher.group()+" - "+matcher.start());
    }

    public static void print(Object o){
        System.out.println(o);
    }
}
输出：
false
4
true
123 - 0
true
34345 - 4
true
123 - 0
true
123 - 0

13.2.3 正则表达式库的4个功能 P454
使用Matcher的匹配函数，共包括以下4种常用功能：
--查询——find()
如果查找时忽略大小写，写成：
Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);

--提取——group()
调用group()函数提取的是获取整个匹配字符串，group(1)是第一个小括号子串内容，group(2)是第二个小括号子串内容，以此类推：
//
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatternRegex {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile(".(\\d{3,5})-(\\d{3,5})..");
        String charSequence = "abcd-123-34345-234-00";
        Matcher matcher = pattern.matcher(charSequence);

        if (matcher.find()) {
            System.out.println(matcher.group());//整个匹配字符串内容
            System.out.println(matcher.group(0));//同group()

            for (int i = 1; i <= matcher.groupCount(); ++i) {
                System.out.println(matcher.group(i));
            }
        }
    }
}
输出如下：
-123-34345-2
123
34345

//
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatternRegex {
    public static void main(String[] args) {
        String regEx = ".+\\\\(.+)$";
        String str = "c:\\dir\\dir2\\name.txt";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean rs = m.find();

        System.out.println(m.group());
        System.out.println(m.group(0));//同group()

        for (int i = 1; i <= m.groupCount(); i++) {
            System.out.println(m.group(i));
        }
    }
}
输出如下：
c:\dir\dir2\name.txt
c:\dir\dir2\name.txt
name.txt

如果没有小括号包括的子串，则groupCount()为0，group()和group(0)仍是整个匹配的字符串。

--分隔——split():
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatternRegex {
    public static void main(String[] args) {
        String regEx = "::";
        Pattern p = Pattern.compile(regEx);
        String[] r = p.split("xd::abc::cde");
        for (int i = 0; i < r.length; ++i) {
            System.out.println(r[i]);
        }
    }
}
输出：
xd
abc
cde

更简单的方法：
String str = "xd::abc::cde";
String[] r = str.split("::");

--替换——replaceAll()：
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatternRegex {
    public static void main(String[] args) {
        String regEx = "a+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher("aaabbced a ccdeaaa");
        String s = m.replaceAll("A");
        System.out.println(s);
    }
}
输出：
Abbced A ccdeA

写成空串，可达到删除的功能，如：
String s = m.replaceAll("");

13.3.3 课后上机作业 P456
--分析某一网页URL地址的内容，取出其中的所有网页链接地址并输出。
根据网页内容，使用Pattern创建一个正则表达式，用以匹配以“href="”开头的、以“"”结尾的字符串。形如下面的链接代码中的地址：
<a href="http://XXXX/" target=_blank>
再取得Matcher，循环执行find()查找，输出其中的网页地址：
//
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class UrlAnalyzer {

    public String getUrlContent(String url) {
        StringBuffer content = new StringBuffer();

        InputStream is = null;
        BufferedReader br = null;
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
            connection.connect();

            is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            while ((str = br.readLine()) != null) {
                content.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                is.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return content.toString();
    }

    public List<String> getUrlList(String urlContent) {
        List<String> urlList = new ArrayList<>();
        Pattern p = Pattern.compile("(href=\")(http://.*?)(\")");
        Matcher m = p.matcher(urlContent);
        while (m.find()) {
            //String url2 = m.group();
            //urlList.add(url2.replace("href=\"", "").replace("\"", ""));
            //书中给的2行样例代码可以用如下语句代码，直接从group中获取
            urlList.add(m.group(2));
        }
        return urlList;
    }
}

public class TestPatternRegex {
    public static void main(String[] args) {
        UrlAnalyzer urlAnalyzer = new UrlAnalyzer();
        String urlContent = urlAnalyzer.getUrlContent("http://www.damai.cn/sh");
        //System.out.println(urlContent);
        List<String> urlList = urlAnalyzer.getUrlList(urlContent);
        //System.out.println(urlList);
        for (String url : urlList) {
            System.out.println(url);
        }
    }
}
输出：
http://dui.dmcdn.cn/??dm_2014/common/public-min.css,dm_2015/index/css/style-min.css,dm_2015/city/css/city-min.css,dm_2015/city/css/calendar-min.css
http://www.damai.cn/redirect.aspx?type=login
http://www.damai.cn/redirect.aspx?type=reg
http://my.damai.cn/
http://my.damai.cn/account/myinfo.aspx
http://order.damai.cn/index.aspx
http://my.damai.cn/trade/ewallet/myEwallet.aspx
......

这只是其中一部分网页链接。通常所示的搜索引擎蜘蛛，就是通过这种办法来进行网页内容分析的，对网页里链接的页面再进行抓取，即为深度抓取。分析的层次越深，则抓取尝试也越深，即通常所说的抓取深度。
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第14课 Java正则表达式 P459

14.1.6 总结 P462
JDOM和DOM在性能测试时表现不佳。
SAX表现较好，依赖于它特定的解析方式。
DOM4J性能最好。如果不考虑可移植性，则可以采用DOM4J。

14.2 使用XML库
建立一个books.xml放在src包下的xml包中，如下：
<?xml version="1.0" encoding="gb2312"?>
<books>
    <book isbn="7506342605">
        <name>《水浒传》</name>
        <price>80</price>
        <author>施耐庵</author>
        <year>元末</year>
    </book>
    <book isbn="7020008720">
        <name>《西游记》</name>
        <price>90</price>
        <author>吴承恩</author>
        <year>明代</year>
    </book>
    <book isbn="7111103033">
        <name>《三国演义》</name>
        <price>75</price>
        <author>罗贯中</author>
        <year>元末</year>
    </book>
    <book isbn="7807074930">
        <name>《红楼梦》</name>
        <price>79</price>
        <author>曹雪芹</author>
        <year>清代</year>
    </book>
</books>

14.2.1 使用DOM读取XML文件 P463
package test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class TestDOM {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            //DOM工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            //DOM解析器
            DocumentBuilder builder = factory.newDocumentBuilder();

            //解析文件
            File file = new File("src/xml/books.xml");
            Document doc = builder.parse(file);

            //取得根节点
            Element root = doc.getDocumentElement();

            //取得子节点列表
            NodeList books = root.getChildNodes();
            for (int i = 0; i < books.getLength(); i++) {
                Node book = books.item(i);

                if (book.getNodeType() == Node.ELEMENT_NODE) {
                    //取得属性值
                    String isbn = book.getAttributes().getNamedItem("isbn").getNodeValue();
                    System.out.print(isbn);

                    for (Node node = book.getFirstChild(); node != null; node = node.getNextSibling()) {
                        if (node.getNodeType() == node.ELEMENT_NODE) {
                            if (node.getNodeName().equals("name")) {
                                String name = node.getFirstChild().getNodeValue();//获取text值，需要先获取FirstChild
                                System.out.print("\t" + name);
                            } else if (node.getNodeName().equals("price")) {
                                String price = node.getFirstChild().getNodeValue();
                                System.out.print("\t" + price);
                            } else if (node.getNodeName().equals("author")) {
                                String author = node.getFirstChild().getNodeValue();
                                System.out.print("\t" + author);
                            } else if (node.getNodeName().equals("year")) {
                                String year = node.getFirstChild().getNodeValue();
                                System.out.println("\t" + year);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
输出：
7506342605	《水浒传》	80	施耐庵	元末
7020008720	《西游记》	90	吴承恩	明代
7111103033	《三国演义》	75	罗贯中	元末
7807074930	《红楼梦》	79	曹雪芹	清代
20

Tips：DOM只提供了解析XML文件的功能，不能使用它来写入一个XML文件。

14.2.2 使用SAX读取XML文件 P466
第二种选择是事件API，即SAX。这个概念是上述对象模型方式的一种反映。这种方法不根据XML语法定义通用的数据模型，其解析器依赖应用程序程序员建立定制的数据模型。
//
package test;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.Stack;

public class TestSAX extends DefaultHandler {
    Stack<String> tags = new Stack<>();

    public TestSAX() {
        super();
    }

    //接收元素中字符数据的通知
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String tag = (String) tags.peek();
        if (tag.equals("name")) {
            System.out.print("\t" + new String(ch, start, length));
        } else if (tag.equals("price")) {
            System.out.print("\t" + new String(ch, start, length));
        } else if (tag.equals("author")) {
            System.out.print("\t" + new String(ch, start, length));
        } else if (tag.equals("year")) {
            System.out.println("\t" + new String(ch, start, length));
        }
    }

    //接收文档开始的通知
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("book")) {
            System.out.print(attributes.getValue("isbn"));
        }
        tags.push(qName);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            //SAX工厂
            SAXParserFactory factory = SAXParserFactory.newInstance();

            //SAX解析器
            SAXParser parser = factory.newSAXParser();

            //parse file
            File file = new File("src/xml/books.xml");
            TestSAX sax = new TestSAX();
            parser.parse(file, sax);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Time eclipsed: " + (end - start));
    }
}
输出：
7506342605	《水浒传》	
        	80	
        	施耐庵	
        	元末
	
    
	
    
7020008720	《西游记》	
        	90	
        	吴承恩	
        	明代
	
    
	
    
7111103033	《三国演义》	
        	75	
        	罗贯中	
        	元末
	
    
	
    
7807074930	《红楼梦》	
        	79	
        	曹雪芹	
        	清代
	
    
	

Time eclipsed: 17

由于SAX是基于事件模型，DefaultHandler接口提供了一系列的事件接口，我们只需要在不同的事件接口中实现自己的代码就可以实现对XML的解析了。这些事件主要可分成如下几类：
--文档、元素或命名空间开始通知事件：
    void startDocument();
    void startElement(...);
    void startPrefixMapping(...);
--文档、元素或命名空间结束通知事件：
    void endDocument();
    void endElement(...);
    void endPrefixMapping(...);
--数据通知事件：
    void characters(...);
--错误通知事件：
    void warning(SAXParseException e);
    void errors(SAXParseException e);
    void fatalError(SAXParseException e);
    
我们只需要在对应的通知事件实现函数中捕捉当前元素的数据信息即可。如上所示实例中，仅仅实现了开始元素和数据通知事件。
--如果元素是book元素，即输出ISBN号。
--如果是book的子元素，即输出各子元素的文本。

Tips：SAX保提供了解析XML文件的功能，不能使用它来写入一个XML文件。

14.2.3 使用JDOM读写XML文件 P468
package test;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Document;

import java.io.File;
import java.util.List;

public class TestJDOM {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            //取得解析器
            SAXBuilder builder = new SAXBuilder();

            //解析文件
            File file = new File("src/xml/books.xml");
            Document doc = builder.build(file);

            //取得根节点
            Element root = doc.getRootElement();

            //取得子节点列表
            List<Element> books = root.getChildren();
            for (int i = 0; i < books.size(); i++) {
                Element book = books.get(i);

                //取得属性值
                String isbn = book.getAttributeValue("isbn");
                System.out.print(isbn);

                String name = book.getChild("name").getText();//等价于book.getChildText("name");
                String price = book.getChild("price").getText();
                String author = book.getChild("author").getText();
                String year = book.getChild("year").getText();

                System.out.print("\t" + name);
                System.out.print("\t" + price);
                System.out.print("\t" + author);
                System.out.println("\t" + year);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Time eclipsed: " + (end - start));
    }
}
输出：
7506342605	《水浒传》	80	施耐庵	元末
7020008720	《西游记》	90	吴承恩	明代
7111103033	《三国演义》	75	罗贯中	元末
7807074930	《红楼梦》	79	曹雪芹	清代
Time eclipsed: 39

JDOM是一个开源项目，它基于树型结构，利用纯Java技术对XML文档实现解析、生成、序列化及多种操作。把SAX和DOM的功能有效地结合起来。
JDOM由以下几个包组成：
--org.jdom
Tips：在JDOM里文档（Document）类由org.jdom.Document来表示。这要与org.w3c.dom中的Document区别开。
--org.jdom.adapters：包含了与DOM适配的Java类。
--org.jdom.filter：XML文档的过滤器类。
--org.jdom.input：读取XML文档的类。
--org.jdom.output：写入XML文档的类。
--org.jdom.transform：将JDOM XML文档接口转换为其XML文档接口。
--org.jdom.xpath：对XML文档XPath操作的类、JDOM类的说明。

由于JDOM提供了DOM和SAX两种实现，使用如下方式创建Document对象：
--DOMBuilder创建
--SAXBuilder创建
新版本中DOMBuilder已经去掉DOMBuilder.builder()，用SAX效率会比较快。

除了可以根据一个File对象创建，还可以将一个字符串讲稿解析，方法是：首先将字符串使用StringReader转化为Reader对象，然后调用build()函数解析。如下例：
String textXml = "<books><book isbn=\"7506342605\"><name>《水浒传》</name><price>80</price><author>施耐庵</author><year>元末</year></book><book isbn=\"7020008720\"><name>《西游记》</name><price>90</price><author>吴承恩</author><year>明代</year></book><book isbn=\"7111103033\"><name>《三国演义》</name><price>75</price><author>罗贯中</author><year>元末</year></book><book isbn=\"7807074930\"><name>《红楼梦》</name><price>79</price><author>曹雪芹</author><year>清代</year></book></books>";
Reader in = new StringReader(textXml);
Document doc = builder.build(in);

JDOM还提供了几个特殊的功能：写入XML文件、DTD验证、Schema验证、XPath读取等。
（1）写入XML
使用XMLOutputter输出到文件，如下例，可以直接使用XMLOutputter对象输出到某一个文件输出流：
package test;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;

public class TestJDOM {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            //取得解析器
            SAXBuilder builder = new SAXBuilder();

            //解析文件
            File file = new File("src/xml/books.xml");
            Document doc = builder.build(file);

            FileOutputStream fos = new FileOutputStream("src/xml/output.xml");
            XMLOutputter outp = new XMLOutputter();
            outp.output(doc, fos);//但是生成的文件是UTF-8 格式
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Time eclipsed: " + (end - start));
    }
}

上例输出到文件输出流，也可以输出到某一个Socket客户端：
outp.output(doc, socket.getOutputStream());

也可以输出到作为输出流类的System.out对象中：
//FileOutputStream fos = new FileOutputStream("src/xml/output.xml");
XMLOutputter outp = new XMLOutputter();
outp.output(doc, System.out);
//fos.close();

除了能够输出到OutputStream对象，还能够输出到Writer对象，如下：
FileWriter fos = new FileWriter("src/xml/output.xml");
XMLOutputter outp = new XMLOutputter();
outp.output(doc, fos);
fos.close();

（2）DTD验证 P472

（3）Schema验证 P472

（4）XPaht支持 P472
使用时先用XPath类的静态方法newInstance(String xpath)得到XPath对象，然后调用selectNodes(Object context)方法或selectSingleNode(Object context)方法，前者根据XPath语句返回一组节点（List对象），后者根据一个XPath语句返回符合条件的第一个节点（Object类型）。
如下两行：
//取得根节点
Element root = doc.getRootElement();

//取得子节点列表
List<Element> books = root.getChildren();

用XPath替换：
//取得根节点
XPath root = XPath.newInstance("//book");//代码此行报错，并且XPath是不推荐使用的

//取得子节点列表
List<?> books = root.selectNodes(doc);
for (int i = 0; i < books.size(); i++) {
    Element book = (Element) books.get(i);
}

XPath可以随意查询各种合法的XPath。

14.2.4 使用DOM4J读写XML文件 P472
package test;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

public class TestDOM4J {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            //取得解析器
            SAXReader reader = new SAXReader();

            //解析文件
            File file = new File("src/xml/books.xml");
            Document doc = reader.read(file);

            //取得根节点
            Element root = doc.getRootElement();

            //取得子节点列表
            for (int i = 0; i < root.nodeCount(); i++) {
                //取得某一个子节点
                Element book = (Element) root.node(i);//这里报错，类型转换有误？

                //取得属性值
                String isbn = book.attributeValue("isbn");
                System.out.print(isbn);

                String name = book.node(0).getText();
                String price = book.node(1).getText();
                String author = book.node(2).getText();
                String year = book.node(3).getText();

                System.out.print("\t" + name);
                System.out.print("\t" + price);
                System.out.print("\t" + author);
                System.out.println("\t" + year);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Time eclipsed: " + (end - start));
    }
}

//CSDN博客的DOM4J使用参考样例
http://blog.csdn.net/yyywyr/article/details/38359049
package test;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class TestDOM4J {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            //取得解析器
            SAXReader reader = new SAXReader();

            //解析文件
            File file = new File("src/xml/books.xml");
            Document doc = reader.read(file);

            //取得根节点
            Element root = doc.getRootElement();

            listNodes(root);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Time eclipsed: " + (end - start));
    }

    public static void listNodes(Element node) {
        System.out.println("当前节点的名称：" + node.getName());

        List<Attribute> list = node.attributes();

        for (Attribute attribute : list) {
            System.out.println("属性 " + attribute.getName() + ":" + attribute.getValue());
        }

        if (!(node.getTextTrim().equals(""))) {
            System.out.println("节点名称 " + node.getName() + ":" + node.getText());
        }

        Iterator<Element> iterator = node.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            listNodes(e);
        }
    }
}
输出：
当前节点的名称：books
当前节点的名称：book
属性 isbn:7506342605
当前节点的名称：name
节点名称 name:《水浒传》
当前节点的名称：price
节点名称 price:80
当前节点的名称：author
节点名称 author:施耐庵
当前节点的名称：year
节点名称 year:元末
当前节点的名称：book
属性 isbn:7020008720
当前节点的名称：name
节点名称 name:《西游记》
当前节点的名称：price
节点名称 price:90
当前节点的名称：author
节点名称 author:吴承恩
当前节点的名称：year
节点名称 year:明代
当前节点的名称：book
属性 isbn:7111103033
当前节点的名称：name
节点名称 name:《三国演义》
当前节点的名称：price
节点名称 price:75
当前节点的名称：author
节点名称 author:罗贯中
当前节点的名称：year
节点名称 year:元末
当前节点的名称：book
属性 isbn:7807074930
当前节点的名称：name
节点名称 name:《红楼梦》
当前节点的名称：price
节点名称 price:79
当前节点的名称：author
节点名称 author:曹雪芹
当前节点的名称：year
节点名称 year:清代
Time eclipsed: 85

DOM4J是非常优秀的Java XML API。越来越多的Java软件都在使用DOM4J来读写XML。
DOM4J提供了几个特殊的功能：XPath支持、字符串与XML的转换、XML输出。

（1）读取并解析XML文档 P474
读取XML文档主要依赖于org.dom4j.io包，其中提供了DOMReader和SAXReader两类不同方式，调用方式是一样的。这就是依赖接口的好处。
reader.read方法是重载的，可以从InputStream、File、URL等多种不同的源来读取，得到的Document对象就代表了整个XML。
（2）XPath支持 P474
DOM4J对XPath有良好的支持，如访问一个节点，可直接用XPath选择。
List list = document.selectNodes("//foo/bar");
Node node = document.selectSingleNode("//foo/bar/author");
String name = node.valueOf("@name");

想查找XHTML文档中所有的超链接，下面代码可以实现：
List list = document.selectNodes("//a/@href");
for (Iterator iter = list.iterator(); iter.hasNext(); ) {
    Attribute attr = (Attribute) iter.next();
    String url = attribute.getValue();
}

（3）字符串与XML的转换
//XML转字符串
String text = document.asXML();
//字符串转XML
String text = "<person><name>James</name></person>";
Document document = DocumentHelper.parseText(text);

（4）文件输出
一个简单的输出方法是将一个Document或任何的Node通过write方法输出：
//取得解析器
SAXReader reader = new SAXReader();

//解析文件
File file = new File("src/xml/books.xml");
Document doc = reader.read(file);

FileWriter out = new FileWriter("src/xml/outputByDom4J.xml");
doc.write(out);//输出为gbk格式的
out.close();

如果想改变输出的格式，如美化输出或缩减格式，可以用XMLWriter类：
package test;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestDOM4J {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            //取得解析器
            SAXReader reader = new SAXReader();

            //解析文件
            File file = new File("src/xml/books.xml");
            Document doc = reader.read(file);

            write(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Time eclipsed: " + (end - start));
    }

    public static void write(Document document) throws IOException {
        XMLWriter writer = new XMLWriter(new FileWriter("src/xml/outputByDom4J.xml"));
        writer.write(document);
        writer.close();
    }
    
    //会生成缩进为2空格的文件
    public static void write(Document document) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileWriter("src/xml/outputByDom4J.xml"), format);//XMLWriter writer = new XMLWriter(System.out, format);会输出到Console
        writer.write(document);
        writer.close();
    }
    
    //生成只有一行内容的XML
    public static void write(Document document) throws IOException {
        OutputFormat format = OutputFormat.createCompactFormat();
        XMLWriter writer = new XMLWriter(new FileWriter("src/xml/outputByDom4J.xml"), format);
        writer.write(document);
        writer.close();
    }
    //<?xml version="1.0" encoding="UTF-8"?>
    //<books><book isbn="7506342605"><name>《水浒传》</name><price>80</price><author>施耐庵</author><year>元末</year></book><book isbn="7020008720"><name>《西游记》</name><price>90</price><author>吴承恩</author><year>明代</year></book><book isbn="7111103033"><name>《三国演义》</name><price>75</price><author>罗贯中</author><year>元末</year></book><book isbn="7807074930"><name>《红楼梦》</name><price>79</price><author>曹雪芹</author><year>清代</year></book></books>
}
输出为UTF-8 的

14.2.5 使用StAX读取XML文件 P475
Tips：从以上程序可以看出，它的方式与SAX思路完全相同，只是SAX使用了事件函数，StAX使用了分支事件。

14.3 属性文件读写 P478
除了XML文件以外，通常属性文件多用来作为系统的配置文件使用。属性文件都是以键值对的形式出现：
username=admin
password=123

14.3.1 读写属性文件的方法
Java中提供了一个java.util.Properties工具类，继承自Hashtable（见第12章）。使用Properties类可以方便地从一个.properties属性文件中读取设置参数，示例如下：
//
package test;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class TestProperties {

    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/properties/test.properties"));
            String value = properties.getProperty("username");
            System.out.println("username:" + value);

            Set keySet = properties.keySet();
//            for (Object s : keySet) {
//                System.out.println(s + ":" + properties.getProperty((String) s));
//            }
            //遍历所有key值
            for (Iterator it = keySet.iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                System.out.println(key + ":" + properties.getProperty(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
输出：
username:admin
password:123
username:admin

如果properties文件打包到一个JAR或WAR文件中，可以使用ClassLoader的getResourceAsStream()方法得到一个InputStream对象，示例如下：
ClassLoader cl = this.getClass().getClassLoader();
InputStream is = c.getResourceAsStream("com/company/application/application.properties");

存储这些参数时，需要得到一个OutputStream对象，然后使用Properties的stores()方法。

14.3.2 读写文件实例 P479
//
package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;

public class TestProperties {

    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream("src/properties/test.properties");
            properties.load(fis);

            Iterator<?> keys = properties.keySet().iterator();
            for ( ; keys.hasNext(); ) {
                String key = (String) keys.next();
                String value = properties.getProperty(key);
                System.out.println(key + "=" + value);
            }

            //写入文件
            FileOutputStream fos = new FileOutputStream("src/properties/config.properties");
            properties.store(fos, "my testproperties");//第二个参数是comments
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
输出：
password=123
username=admin

#my testproperties
#Tue Jul 12 23:08:09 CST 2016
password=123
username=admin

Tips：这种做法通常用来保存系统变量使用。系统修改了参数保存在properties文件中，系统启动时再加载这些属性。

//------------------------------------------------------------------------------------------------
//Java核心编程技术 第15课 Java GUI库对比 P485
15.2.1 AWT实例 
略过没看？

15.2.2 Swing实例 P490
package test;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HelloSwing extends JFrame {

    public static void main(String[] args) {
        new HelloSwing().run();
    }

    private void run() {
        configureJFrame();
        createButton();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void configureJFrame() {
        setTitle("Hello Swing");
        getContentPane().setLayout(new FlowLayout());
        setSize(new Dimension(200, 200));
        setLocation(0, 0);
    }

    private void createButton() {
        Button button = new Button("Open");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDialog();
            }
        });

        getContentPane().add(button, BorderLayout.NORTH);
    }

    private void createDialog() {
        JOptionPane.showMessageDialog(HelloSwing.this, "This is a dialog", "dialog", JOptionPane.INFORMATION_MESSAGE);
    }
}

Swing实现Hello World实例，只需要少量的代码，而且实现的界面也比AWT美观。

15.2.3 SWT实例 P492
略过没看？

15.2.4 JFace实例 P494
略过没看？

//------------------------------------------------------------------------------------------------
//Java核心编程技术 第16课 AWT图形界面开发 P499
略过没看？
16.1 AWT界面组件 P499
（1）组件（Component）
Java图形用户界面的最基本组成部分是组件（Component），组件是一个可以以图形化的方式显示在屏幕上并能与用户进行交互的对象，如一个按钮，一个标签等。组件不能独立地显示出来，必须将组件放在一定的容器中才可以显示出来。
类java.awt.Component是许多组件类的父类，Component类中封装了组件通用的方法和属性，如图形组件对象、大小、显示位置、前景色和背景色、边界、可见性等。因此许多组件类也就继承了Component类的成员方法和成员变量。
（2）容器（Container）
容器java.awt.Container是Component的子类，因此容器本身也是一个组件，它具有组件的所有性质，但是它主要功能是容纳其他组件和容器。一个容器可以容纳多个组件，并使它们成为一个整体。所有容器都可以通过add()方法向容器中添加组件。
每个容器都有一个布局管理器（LayoutManager），当容器需要对某个组件进行定位或判断其大小时，就会调用其对应的布局管理器。
在程序中安排组件的位置和大小时，应该注意以下两点：
--在容器中的布局管理器负责各个组件的大小和位置，用户无法在这种情况下设置组件的这些属性，如果试图使用Java语言提供的setLocation()、setSize()、setBounds()等方法，都会被布局管理器覆盖。
--如果用户确实需要亲自设置组件大小或位置，则应取消该容器的布局管理器，方法为setLayout(null)。

16.1.2 窗口与对话框 P501
从组件树（P499）可以看出，共有3种类型的容器：窗口Window、面板Panel、滚动面板ScrollPane。Window的实现类有窗体类Frame和对话框类Dialog，ScrollPane的实现类有Applet（第21课讲解）。
（1）绘制窗体Frame
一般生成一个窗口，通常是用Window的子类Frame来进行实例化，而不是直接用Window类。每个Frame对象实例化后，都是没有大小和不可以见的。因此必须调用setSize()来设置大小（宽度和高度），调用setVisible(true)来设置窗口可见。
//
package test;

import java.awt.Color;
import java.awt.Frame;

public class TestFrame extends Frame {

    public static void main(String[] args) {
        TestFrame fr = new TestFrame("Test Frame");
        fr.setSize(200, 200);
        fr.setBackground(Color.red);
        fr.setVisible(true);
    }

    public TestFrame(String str) {
        super(str);
    }
}

（2）使用面板Panel
Panel是一个容器，放在Frame组件内，可以用于包装一组组件。作用是可以将Panel设置单独的布局，然后将该Panel作为一个单独的组件放在Frame中：
package test;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;

public class TestFrame extends Frame {

    public TestFrame(String str) {
        super(str);
    }

    public static void main(String[] args) {
        TestFrame fr = new TestFrame("Test Frame");
        fr.setSize(200, 200);
        fr.setBackground(Color.red);
        fr.setLayout(null);

        Panel pan = new Panel();
        pan.setSize(100, 100);
        pan.setBackground(Color.yellow);
        fr.add(pan);
        fr.setVisible(true);
    }
}

（3）对话框Dialog P503
Dialog不同于Frame，是一个显示提示信息的对话框。创建对话框时必须拥有一个Frame依赖类，表示从属于某一个窗体的对话框，并可以设置大小、布局。

（4）文本对话框

16.1.3 基本组件 P504
（1）文本Label
（2）按钮Button P505
Button b = new Button("退出");
按钮被单击后，会产生ActionEvent事件，需要使用ActionListener接口进行监听和处理事件。ActionEvent的对象调用getActionCommand()方法可以得到按钮的标识名，默认按钮名为label。用setActionCommand()可以为按钮设置组件标识符。
package test;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestDialog extends Frame {

    public TestDialog(String str) {
        super(str);
    }

    public static void main(String[] args) {
        TestDialog fr = new TestDialog("Test Frame");
        fr.setSize(200, 200);
        fr.setBackground(Color.white);
        fr.createButton();
        fr.createCheckbox();
        fr.setVisible(true);
    }

    private void createCheckbox() {
        Checkbox checkbox = new Checkbox("China");
        checkbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String state = "deselected";
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    state = "selected";
                }
                System.out.println(e.getItem() + " " + state);
            }
        });

        add(checkbox, BorderLayout.SOUTH);
    }

    private void createButton() {
        Button button = new Button("Open");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDialog();
            }
        });
        add(button, BorderLayout.NORTH);
    }

    private void createDialog() {
        final Dialog dialog = new Dialog(TestDialog.this, "Dialog", true);
        dialog.setSize(267, 117);
        dialog.setLayout(new GridLayout(2, 1));

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
            }
        });

        Panel topPanel = new Panel();
        Label label = new Label("This is a dialog");
        topPanel.add(label, BorderLayout.NORTH);
        dialog.add(topPanel);
        dialog.setVisible(true);
    }
}

（3）复选框 Checkbox P505 参考上例
用getStateChange()获取当前状态。使用getItem()获得被修改复选框的字符串对象

（4）复选框组 CheckboxGroup P506
    private void createCheckboxGroup() {
        CheckboxGroup group = new CheckboxGroup();
        Checkbox checkbox1 = new Checkbox("Beijing", group, true);
        Checkbox checkbox2 = new Checkbox("Shanghai", group, false);
        Checkbox checkbox3 = new Checkbox("Guangzhou", group, false);
        add(checkbox1);
        add(checkbox2);
        add(checkbox3);
    }
    
（5）下拉列表 Choice P506

（6）文本框 TextField P506
显示一个输入的文本框，回车键按下时，发生ActionEvent事件，通过ActionListener中的actionPerformed()方法对事件进行相应处理，使用getText()方法取得文本域的输入，使用setEditable(boolean)设置只读属性。

（7）文本区域TextArea P507

（8）列表List P507

（9）画布 Canvas P507
一个应用程序必须继承Canvas类才能获得有用的功能，如创建一个自定义组件。如果想在画面上完成一些图形处理，则Canvas类中的paint()方法必须被重写。Canvas组件监听各种鼠标、键盘事件。当在组件中输入字符时，必须先调用requestFocus()方法。
package test;

import java.awt.Canvas;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TestCanvas implements KeyListener, MouseListener {
    Canvas c;
    String s = "";

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("KeyTyped");
        s += e.getKeyChar();
        c.getGraphics().drawString(s, 0, 20);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("MouseClicked");
        c.requestFocus();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    public static void main(String[] args) {
        Frame f = new Frame("Test Canvas");
        TestCanvas testCanvas = new TestCanvas();
        testCanvas.c = new Canvas();
        f.add("Center", testCanvas.c);

        f.setSize(150, 150);
        testCanvas.c.addMouseListener(testCanvas);
        testCanvas.c.addKeyListener(testCanvas);
        f.setVisible(true);
    }
}

16.1.4 菜单组件 P508
（1）菜单栏 MenuBar
要添加菜单组件，首先需要添加菜单栏MenuBar。MenuBar只能被添加到Frame对句中，作为整个菜单树的根基。如下：
MenuBar mb = new MenuBar();
setMenuBar(mb);

（2）菜单 Menu 
Menu m1 = new Menu("File");
Menu m1 = new Menu("Edit");
Menu m1 = new Menu("Help");
mb.add(m1);
mb.add(m2);
mb.setHelpMenu(m3);//设置帮助菜单

（3）菜单选项 MenuItem P509
MenuItem mi1 = new MenuItem("Open");
MenuItem mi1 = new MenuItem("Exit");
m1.add(mi1);
m1.addSeparator();
m1.add(mi2);

MenuItem对象可以添加ActionListener事件，完成相应的监听操作。
//示例代码
package test;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestMenu extends Frame {
    public static void main(String[] args) {
        TestMenu tm = new TestMenu();
        tm.setTitle("Test Menu");
        tm.setSize(200, 200);
        tm.setVisible(true);

        MenuBar mb = new MenuBar();
        tm.setMenuBar(mb);

        Menu m1 = new Menu("File");
        Menu m2 = new Menu("Edit");
        Menu m3 = new Menu("Help");
        mb.add(m1);
        mb.add(m2);
        mb.setHelpMenu(m3);

        MenuItem mi1 = new MenuItem("Open");
        MenuItem mi2 = new MenuItem("Save");
        MenuItem mi3 = new MenuItem("Exit");
        m1.add(mi1);
        m1.add(mi2);
        m1.addSeparator();
        m1.add(mi3);

        mi1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open Event");
            }
        });

        mi3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}

16.2 AWT布局管理器 P509
在使用布局管理器之前，需要注意以下几点：
--Frame是一个顶级容窗口，Frame的默认布局管理器为BorderLayout。
--Panel无法单独显示，必须添加到某个容器中。Panel的默认布局管理器为FlowLayout。
--当把Panel作为一个组件添加到某个容器中后，该Panel仍然可以有自己的布局管理器。可以利用Panel使得BorderLayout中某个区域显示多个组件，达到设计复杂用户界面的目的。
--如果采用无布局管理器setLayout(null)，则必须使用setLocation()、setSize()、setBounds()等方法手工设置组件的大小和位置，此方法会导致平台相关，不鼓励使用。

16.2.1 流式布局 FlowLayout P510
FlowLayout是Panel、Applet的默认布局管理器。其组件的放置规则是从上到下，从左到右进行放置。
package test;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;

public class TestFlowLayout {
    public static void main(String[] args) {
        Frame f = new Frame("Test FlowLayout");
        f.setLayout(new FlowLayout());

        Button button1 = new Button("OK");
        Button button2 = new Button("Open");
        Button button3 = new Button("Close");

        f.add(button1);
        f.add(button2);
        f.add(button3);
        f.setSize(300, 100);
        f.setVisible(true);
    }
}
默认打开三个按钮都在同一行，窗口修改变窄，则按钮跑到第二行去了。该布局组件的大小不变，相对位置会发生变化。

16.2.2 区域布局 BorderLayout P511
BorderLayout是Window、Frame和Dialog的默认布局管理器。分成5个区域：North、South、East、West和Center，每个区域只能放置一个组件。
使用BorderLayout的时候，如果容器的大小变化，变化规律为：组件的相对位置不变，大小发生变化。如容器变高了，则North、South区域不变，West、Center、East区域变高；如果容器变宽了，则West、East区域不变，North、Center、South区域变宽。
package test;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;

public class TestBorderLayout {
    public static void main(String[] args) {
        Frame f = new Frame("Test FlowLayout");
        f.setLayout(new BorderLayout());

        f.add(BorderLayout.NORTH, new Button("North"));
        f.add(BorderLayout.SOUTH, new Button("South"));
        f.add(BorderLayout.EAST, new Button("East"));
        f.add(BorderLayout.WEST, new Button("West"));
        f.add(BorderLayout.CENTER, new Button("Center"));
        f.setSize(300, 200);
        f.setVisible(true);
    }
}
不一定所有的区域都有组件，如果四周的区域没有组件，则由Center区域去补充，但是如果Center区域没有组件，则保持空白。

16.2.3 网格布局 GridLayout P512
GridLayout使容器中各个组件呈网格状布局，平均占据容器的空间，创建该布局时需要指定风格的行数和列数，然后依次添加各个组件时，会按照先行后列的顺序依次添加。
package test;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;

public class TestGridLayout {
    public static void main(String[] args) {
        Frame f = new Frame("Test FlowLayout");
        f.setLayout(new GridLayout(3, 2));

        f.add(new Button("1"));
        f.add(new Button("2"));
        f.add(new Button("3"));
        f.add(new Button("4"));
        f.add(new Button("5"));
        f.add(new Button("6"));

        f.setSize(200, 200);
        f.setVisible(true);
    }
}

16.2.4 卡片布局 CardLayout P513
CardLayout卡片布局管理器能够帮助用户处理两个以至更多的成员共享同一显示空间，它把容器分成许多层，每层的显示空间占据整个容器的大小，但是每层只允许放置一个组件，当然每层都可以利用Panel来实现复杂的用户界面。
package test;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestCardLayout extends Frame implements ActionListener {
    private static final long serialVersionUID = 1L;
    CardLayout cardLayout = new CardLayout();
    Panel panelCard = new Panel();

    public TestCardLayout(String str) {
        super(str);
        setSize(400, 200);

        panelCard.setLayout(cardLayout);
        Panel panel1 = new Panel();
        Panel panel2 = new Panel();
        Panel panel3 = new Panel();
        Panel panel4 = new Panel();

        panel1.setBackground(Color.red);
        panel2.setBackground(Color.green);
        panel3.setBackground(Color.blue);
        panel4.setBackground(Color.black);

        panelCard.add(panel1, "1");//字符串1~4不太知道什么作用，不加也没什么问题
        panelCard.add(panel2, "2");
        panelCard.add(panel3, "3");
        panelCard.add(panel4, "4");
        add(panelCard, BorderLayout.CENTER);

        //创建按钮
        Button button1 = new Button("First Page");
        Button button2 = new Button("Previous Page");
        Button button3 = new Button("Next Page");
        Button button4 = new Button("Last Page");

        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);

        Panel panelButton = new Panel();
        panelButton.add(button1);
        panelButton.add(button2);
        panelButton.add(button3);
        panelButton.add(button4);
        add(panelButton, BorderLayout.NORTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("First Page")) {
            cardLayout.first(panelCard);
        } else if (e.getActionCommand().equals("Previous Page")) {
            cardLayout.previous(panelCard);
        } else if (e.getActionCommand().equals("Next Page")) {
            cardLayout.next(panelCard);
        } else if (e.getActionCommand().equals("Last Page")) {
            cardLayout.last(panelCard);
        }
    }

    public static void main(String[] args) {
        new TestCardLayout("Test CardLayout");
    }
}
单击4个按钮实现卡片的切换。
这种卡片程序通常用在需要多页显示的界面中。
Tips：GridBagLayout是所有AWT布局管理器当中最复杂的，也是功能最强大的。它提供了众多的可配置选项，你几乎可以完全地控制容器的布局方式。

16.2.5 容器的嵌套 P515
容器的嵌套都是使用Panel来完成的，每一个Panel可以设置布局，在一个Panel中可以添加多个组件，然后将该Panel作为一个整体添加到其他的组件中。
前方中16.1.2 节Panel组件的实例，就是嵌套的实例。其中Frame是一个顶层容器，Panel是一个面板容器，Panel被放入Frame中，就属于嵌套。

16.3 AWT事件处理 P515
16.3.1 事件授权模型 P515
事件处理的过程中，主要涉及以下3类对象：
--Event——事件：用户对界面操作在Java语言上的描述，以类的形式出现，如键盘操作对应的事件类是KeyEvent。
--Event Source——事件源：事件发生的场所，通常就是各个组件，如按钮Button。
--Event Handler——事件处理者：接收事件对象并对其进行处理的对象。

授权模型把事件的处理委托给外部的处理实例进行处理，实现 了将事件源和监听器分开的西岭雪山。事件处理者（监听器）通常是一个类，该类如果要能够处理某种类型的事件，就必须实现与该事件类型相对的接口。
使用授权处理模型进行事件处理的一般方法归纳如下：
--对于某种类型的事件XXXEvent，要想接收并处理这类事件，必须定义相应的事件监听器类，该类需要实现与该事件相对应的接口XXXListener。
--事件源实例化以后，必须进行授权，注册该类事件的监听器，使用addXXXListener(XXXListener)方法来注册监听器

16.3.2 授权模型：事件类型 P516
AWT提供了以下事件类型（大部分在java.awt.events包中）：

AWT事件共有10类，可以归为两大类：低级事件和高级事件。
（1）低级事件 P517
低级事件是指基于组件和容器的事件，当一个组件上发生事件，如：鼠标的进入、单击、拖放等，或组件的窗口开关等，触发了组件事件。
--ComponentEvent——组件事件：组件尺寸的变化、移动。
--ContainerEvent——容器事件：组件增加、移动。
--WindowEvent——窗口事件：关闭窗口、窗口闭合、图标化。
--FocusEvent——焦点事件：焦点的获得和丢失。
--KeyEvent——键盘事件：键按下、释放。
--MouseEvent——鼠标事件：鼠标单击、移动。

（2）高级事件
高级事件是基于语义的事件，它可以不和特定的动作相关联，而依赖于触发此类事件的类，如在TextField中按Enter键会触发ActionEvent事件、滑动滚动条触发AdjustmentEvent事件，或是选中项目列表的某一条就会触发ItemEvent事件。
--ActionEvent——动作事件：按钮按下，在TextField中按“Enter”键。
--AdjustmentEvent——调节事件：在滚动条上移动滑块以调节数值。
--ItemEvent——项目事件：选择项目，不选择项目改变。
--TextEvent——文本事件：文本对象改变。

16.3.3 授权模型：事件监听器 P517
每类事件都有对应的事件监听器，监听器是接口，根据动作来定义方法。通常对于AWT来说（也适用于Swing和SWT），每个事件类型都有一个相关的XXXListener接口（XXXAdapter的实现可能为空），其中XXX是去掉Event后缀的事件名，用来把事件传递给处理程序。应用程序会为自己感兴趣的事件的事件源（GUI组件或部件）进行注册。
如与键盘事件KeyEvent相对应的接口是：
public interface KeyListener extends EventListener {

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     */
    public void keyTyped(KeyEvent e);

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     */
    public void keyPressed(KeyEvent e);

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     */
    public void keyReleased(KeyEvent e);
}

又例如窗口事件接口：
public interface WindowListener extends EventListener {
    /**
     * Invoked the first time a window is made visible.
     */
    public void windowOpened(WindowEvent e);

    /**
     * Invoked when the user attempts to close the window
     * from the window's system menu.
     */
    public void windowClosing(WindowEvent e);

    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     */
    public void windowClosed(WindowEvent e);

    /**
     * Invoked when a window is changed from a normal to a
     * minimized state. For many platforms, a minimized window
     * is displayed as the icon specified in the window's
     * iconImage property.
     * @see java.awt.Frame#setIconImage
     */
    public void windowIconified(WindowEvent e);

    /**
     * Invoked when a window is changed from a minimized
     * to a normal state.
     */
    public void windowDeiconified(WindowEvent e);

    /**
     * Invoked when the Window is set to be the active Window. Only a Frame or
     * a Dialog can be the active Window. The native windowing system may
     * denote the active Window or its children with special decorations, such
     * as a highlighted title bar. The active Window is always either the
     * focused Window, or the first Frame or Dialog that is an owner of the
     * focused Window.
     */
    public void windowActivated(WindowEvent e);

    /**
     * Invoked when a Window is no longer the active Window. Only a Frame or a
     * Dialog can be the active Window. The native windowing system may denote
     * the active Window or its children with special decorations, such as a
     * highlighted title bar. The active Window is always either the focused
     * Window, or the first Frame or Dialog that is an owner of the focused
     * Window.
     */
    public void windowDeactivated(WindowEvent e);
}

不同类型的事件由不同的监听器监听，每一种监听器都提供了各种处理函数接口方法，在该方法中编写事件的处理代码。P518
不同的组件拥有不同的事件类型，比如文本框有键盘输入事件、按钮有鼠标单击事件等，因此不同的组件也对应了不同的监听器类型。 P519

16.3.4 使用事件监听器 P519
要使用监听器来监听事件，必须进行注册。AWT的组件类中提供注册和注销监听器的方法。
--注册监听器：
public void add<ListenerType>(<ListenerType>listener);
--注销监听器：
public void remove<ListenerType>(<ListenerType>listener);

要实现监听器的注册与注销，有3种方式：
（1）实现监听器接口
这种方法是创建一个类，要求其实现XXXListener接口，在该类中根据需要实现相应的事件处理函数。
下例演示了实现监听器接口的方式。类TestListener实现了3个监听器接口MouseMotionListener、MouseListener和WindowListener，用以监听鼠标移动、鼠标单击及窗体事件。
package test;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class TestListener implements MouseMotionListener, MouseListener, WindowListener {
    private Frame f;
    private TextField textField;

    public static void main(String[] args) {
        TestListener testListener = new TestListener();
        testListener.go();
    }

    public void go() {
        f = new Frame("Listener Sample");
        f.add(new Label("Manupalute Mouse"), BorderLayout.NORTH);
        textField = new TextField(30);
        f.add(textField, BorderLayout.SOUTH);//f.add(textField, "South");
        f.addMouseMotionListener(this);
        f.addMouseListener(this);
        f.addWindowListener(this);
        f.setSize(300, 200);
        f.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        String s = "Mouse pressed";
        textField.setText(s);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        String s = "Mouse released";
        textField.setText(s);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        String s = "Mouse entering";
        textField.setText(s);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        String s = "Mouse leaving";
        textField.setText(s);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        String s = "Mouse position: X=" + e.getX() + " Y=" + e.getY();
        textField.setText(s);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        String s = "Mouse moving";
        textField.setText(s);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    //为了使窗口能正常关闭、程序正常退出，需要实现windowClosing方法
    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(1);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}

上例几个特点：
--可以声明实现多个接口，接口之间用逗号隔开
--可以由同一个对象监听一个事件源上发生的多种事件
f.addMouseMotionListener(this);
f.addMouseListener(this);
f.addWindowListener(this);

对象f上发生的多个事件都将被同一个监听器接收和处理。
--事件处理者和事件源处在同一个类中。在本例中事件源是Frame f，事件处理者是类TestListener，其中事件源Frame f是类的成员变量。
--可以通过事件对象获得详细资料，如本例中就通过事件对象获得了鼠标发生时的坐标值。
Tips：Java语言类层次非常分明，因而只支持单继承，为了实现多重继承能力，Java用接口来实现，一个类可以实现多个接口，这种机制比多重继承具有更简单、灵活、更强的功能。在AWT中就经常用到声明和实现多个接口。记住无论实现了几个接口，接口中已定义的方法必须一一实现，如果对某事件不进行处理，可以不具体实现其方法，而用空的方法体来代替。但却必须把所有的方法都要写上。

（2）使用内部类 P522
内部类（Inner Class）是被定义于另一个类中的类，使用内部类的主要原因如下：
--一个内部类的对象可访问外部类的成员方法和变量，包括私有的成员。
--实现事件监听器时，采用内部类、匿名类编程非常容易实现其功能。
--编写事件驱动程序，内部类很方便。

因此内部类所能够应用的地方往往是在AWT的事件处理机制中。
package test;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class TestInnerClass {
    private Frame f;
    private TextField textField;

    public TestInnerClass() {
        f = new Frame("Test InnerClass");
        textField = new TextField(30);
    }

    public static void main(String[] args) {
        TestInnerClass testListener = new TestInnerClass();
        testListener.createFrame();
    }

    public void createFrame() {//这里private也没有关系，因为main函数也是在这个类中，如果是别的类需要访问，则必须是public
        f.add(new Label("Manupalute Mouse"), BorderLayout.NORTH);
        f.add(textField, BorderLayout.SOUTH);//f.add(textField, "South");

        //参数为内部类对象
        f.addMouseMotionListener(new MyMouseMotionListener());
        f.setSize(300, 200);
        f.setVisible(true);
    }

    private class MyMouseMotionListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            String s = "Mouse dragging: X=" + e.getX() + " Y=" + e.getY();
            textField.setText(s);
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }
}

（3）使用匿名类 P523
当一个内部类的类声名只是在创建此类对象时用了一次，而且要产生的新类需要继承于一个已有的父类或实现一个接口，才能考虑用匿名类（Anonymous Class），由于匿名类本身无名，因此它也就不存在构造方法，它需要显式地调用一个无参的父类的构造方法，并且重写父类的方法，所谓的匿名就是该类连名字都没有，只是显式地调用一个无参的父类的构造方法。
package test;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class TestAnonymousClass {
    private Frame f;
    private TextField textField;

    public TestAnonymousClass() {
        f = new Frame("Test InnerClass");
        textField = new TextField(30);
    }

    public static void main(String[] args) {
        TestAnonymousClass testListener = new TestAnonymousClass();
        testListener.createFrame();
    }

    private void createFrame() {
        f.add(new Label("Manupalute Mouse"), BorderLayout.NORTH);
        f.add(textField, BorderLayout.SOUTH);//f.add(textField, "South");

        //参数为内部类对象
        f.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                String s = "Mouse dragging: X=" + e.getX() + " Y=" + e.getY();
                textField.setText(s);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        f.setSize(300, 200);
        f.setVisible(true);
    }
}

16.3.5 使用事件适配器 P524
Java语言为一些Listener接口提供了适配器（Adapter）类。可以通过继承事件所对应的Adapter类，重写需要的方法即可，无关方法不用实现。事件适配器为我们提供了一种简单的实现监听器的手段，可以缩短程序。但是，由于Java的单一继承机制，当需要多种监听器或此类已有父类时，就无法采用事件适配器了。
java.awt.event包中定义的事件适配器类包括以下几个：
--ComponentAdapter组件适配器。
--ContainerAdapter容器适配器。
--FocusAdapter焦点适配器。
--KeyAdapter键盘适配器。
--MouseAdapter鼠标适配器。
--MouseMotionAdapter鼠标运行适配器。
--WindowAdapter窗口适配器。

WindowAdapter是一个抽象类，但是它实现了WindowListener的所有方法（都是空实现）。
16-19 中的内部类，它实现了接口MouseMotionListener。由于它只需要实现一个监听器，因此可以通过继承适配器的方式，这样就可以省略不需要的事件函数代码。
package test;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class TestAdapter {
    private Frame f;
    private TextField textField;

    public TestAdapter() {
        f = new Frame("Test InnerClass");
        textField = new TextField(30);
    }

    public static void main(String[] args) {
        TestAdapter testListener = new TestAdapter();
        testListener.createFrame();
    }

    private void createFrame() {
        f.add(new Label("Manupalute Mouse"), BorderLayout.NORTH);
        f.add(textField, BorderLayout.SOUTH);//f.add(textField, "South");

        //参数为内部类对象
        f.addMouseMotionListener(new MyMouseMotionListener() {
        });
        f.setSize(300, 200);
        f.setVisible(true);
    }

    private class MyMouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            String s = "Mouse dragging: X=" + e.getX() + " Y=" + e.getY();
            textField.setText(s);
        }
    }
}

16.4.3 Java文本编辑器 P528
package test;

import javafx.scene.layout.Pane;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TextEditor extends WindowAdapter implements ActionListener {
    private Frame frame;
    private TextArea textArea;
    private String fileName;

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    public void createEditor() {
        //添加菜单项
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("File");
        MenuItem menuItemNew = new MenuItem("New", new MenuShortcut(KeyEvent.VK_N));
        menuFile.add(menuItemNew);
        MenuItem menuItemOpen = new MenuItem("Open", new MenuShortcut(KeyEvent.VK_O));
        menuFile.add(menuItemOpen);
        MenuItem menuItemSave = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S));
        menuFile.add(menuItemSave);
        menuFile.add("Save as...");//内部实现会生成一个Label为该字符串的MenuItem
        menuFile.addSeparator();
        menuFile.add("Exit");
        menuFile.addActionListener(this);//点击事件是注册在Menu上的
        menuBar.add(menuFile);

        Menu menuHelp = new Menu("Help");
        menuHelp.add("About");
        menuHelp.addActionListener(this);
        menuBar.setHelpMenu(menuHelp);

        //主窗口
        frame = new Frame("Java Text Editor");
        frame.setMenuBar(menuBar);
        textArea = new TextArea();
        frame.add("Center", textArea);
        frame.addWindowListener(this);//注册窗口关闭监听器
        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        TextEditor textEditor = new TextEditor();
        textEditor.createEditor();
    }

    //菜单选择事件
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand() == "New") {
                textArea.setText("");
                fileName = null;
            } else if (e.getActionCommand() == "Open") {
                //Select a file
                fileName = getFileName("Open");
                if (fileName == null) {
                    return;
                }

                //Read file
                FileReader fileReader = new FileReader(fileName);
                BufferedReader br = new BufferedReader(fileReader);
                String str = "";
                while (br.ready()) {
                    int c = br.read();
                    str += (char) c;
                }
                textArea.setText(str);
                br.close();
                fileReader.close();
                frame.setTitle("Java Text Editor - " + fileName);
            } else if (e.getActionCommand() == "Save") {
                if (textArea.getText().equals("")) {
                    return;
                }
                if (fileName == null) {
                    fileName = getFileName("Save");
                }

                //取消时，将得到null
                if (fileName == null) {
                    return;
                } else {
                    saveFile(fileName, false);
                }
            } else if (e.getActionCommand() == "Save as...") {
                fileName = getFileName("Save as");

                //取消时，将得到null
                if (fileName == null) {
                    return;
                } else {
                    saveFile(fileName, false);
                }
            } else if (e.getActionCommand() == "Exit") {
                System.exit(0);
            } else if (e.getActionCommand() == "About") {
                //显示关于对话框
                final Dialog dialog = new Dialog(frame, "About", true);
                dialog.setSize(267, 117);
                //dialog.setLayout(new GridLayout(2, 1));//这里就一个topPanel，不需要设置GridLayout

                //窗口关闭事件
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        dialog.dispose();
                    }
                });

                //显示消息
                Panel topPanel = new Panel();
                
                Label label = new Label("Java Text Editor - Author: DingBen");
                topPanel.add(label, BorderLayout.NORTH);
                
                // Label labelAuthor = new Label("Java Text Editor - Author: DingBen");
                // Label labelVersion = new Label("Version 1.0");
                // Label labelDate = new Label("Release Date: 2016-7-27");
                // topPanel.setLayout(new GridLayout(3, 1));

                // topPanel.add(labelAuthor);
                // topPanel.add(labelVersion);
                // topPanel.add(labelDate);
                
                dialog.add(topPanel);
                dialog.setVisible(true);
            }
        } catch (FileNotFoundException e1) {

        } catch (IOException e1) {

        }
    }

    private void saveFile(String selectFileName, boolean overwrite) throws IOException {
        File file = new File(selectFileName);
        FileWriter fos = new FileWriter(file, overwrite);
        BufferedWriter bos = new BufferedWriter(fos);
        PrintWriter pos = new PrintWriter(bos);
        //写入对象数据
        pos.print(textArea.getText());

        pos.close();
        bos.close();
        fos.close();
    }

    private String getFileName(String dialogTitle) {
        FileDialog fileDialog = new FileDialog(frame, dialogTitle, 0);
        fileDialog.setVisible(true);
        if (fileDialog.getDirectory() == null)
        {
            return null;
        }
        else {
            String selectedFileName = fileDialog.getDirectory() + fileDialog.getFile();//getDirectory中最后带着分隔符
            return selectedFileName;
        }
    }
}

//------------------------------------------------------------------------------------------------
//Java核心编程技术 第17课 AWT多媒体编程 P535
17.1 图像处理——java.awt.image P535
17.1.1 加载图像
AWT可以很简单地两种格式的图像：GIF和JPEG。Toolkit类提供了两个getImage()方法来加载图像。
--Image getImage(URL url);
--Image getImage(String filename);
Toolkit是一个组件类，取得Toolkit的方法是：
Toolkit toolkit = Toolkit.getDefaultToolkit();
对于继承了Frame的类来说，可以直接使用下面的方法取得：
Toolkit toolkit = getToolkit();
下面是两个加载图片的实例：
Toolkit toolkit = Toolkit.getDefaultToolkit();
Image image1 = toolkit.getImage("imageFile.gif");
Image image2 = toolkit.getImage(new URL("http://.../people.gif"));
如果你的类是Applet，可以使用Applet的两个getImage()方法来直接加载图片：
Image getImage(URL url);
Image getImage(URL url, String name);

17.1.2 显示图像 P536
通过传递到paint()方法的Graphics对象可以很容易地显示图像。Graphics类声明了下面的4个drawImage()方法。它们都返回一个boolean值，虽然这个值很少被使用。如果图像已经被完全加载并且因此被完全绘制，返回值是true，否则，返回值是false。
组件可以指定this作为图像观察者的原因是，Component类实现了ImageObserver接口。当图像数据被加载时，它的实现会调用repaint()方法。
例如下面代码在组件区域的左上角(0, 0)以原始大小显示一个图像。
g.drawImage(myImage, 0, 0, this);
下面的代码在坐标(90, 0)处显示一个被缩放为宽300像素高62像素的图像。
g.drawImage(myImage, 90, 0, 300, 62, this);

17.1.3 实例一：显示图片 P536
编写一个窗体，用来加载图像并进行显示。该类继承自Frame，可以直接使用getToolkit()方法来取得Toolkit对象，然后使用getImage()来取得一张本地图片文件，最后在paint()中使用Graphics的drawImage()即可显示该图像。
package test;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;

public class ShowImage extends Frame {
    private String fileName;

    public ShowImage(String fileName) {
        setSize(470, 350);
        setVisible(true);
        this.fileName = fileName;
    }

    @Override
    public void paint(Graphics g) {
        //取得图片对象
        Image image = getToolkit().getImage(fileName);
        //画图
        g.drawImage(image, 50, 50, this);
    }

    public static void main(String[] args) {
        new ShowImage("src/image/1.jpg");
    }
}

17.1.4 实例二：缩放图片 P537
通过getImage()方法取得的是java.awt.Image类型的对象，也可以使用javax.imageio.ImageIO类的read()取得一个图像，返回的是BufferedImage对象。
BufferedImage ImageIO.read(Url);
BufferedImage是Image的子类，它描述了具有可访问图像数据缓存区的Image，可以通过该类来实现图片的缩放。
//
package test;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

public class ZoomImage {
    public void zoom(String file1, String file2) {
        try {
            File file = new File(file1);
            Image src = javax.imageio.ImageIO.read(file);//构造Image对象，返回的BufferedImage对象，是Image的子类
            int width = src.getWidth(null);
            int height = src.getHeight(null);

            BufferedImage tag = new BufferedImage(width / 2, height / 2, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(src, 0, 0, width / 2, height / 2, null);//绘制缩小的图

            //写入图片
            FileOutputStream out = new FileOutputStream(file2);//输出到文件流
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String file1 = "src/image/1.jpg";
        String file2 = "src/image/half1.jpg";
        new ZoomImage().zoom(file1, file2);
    }
}
运行该程序，即可生成一个长宽为原图一半的新图片。如果大家对图像处理有更高的要求，可以关注一下开源项目。如JMagick，可以使用JMagick实现图片的复制、信息获取、斜角、特效、组合、改变大小、加边框、旋转、切片、改变格式、去色等功能。

17.2 二维图像绘制——Java2D P538
Java2D的绘图过程：
（1）取得Graphics2D对象。
（2）设置Graphics2D属性。
（3）创建绘制对象。
（4）绘制对象。
17.2.1 Java2D简介

17.2.2 取得Graphics2D对象 P540
绘制图形时，可以在Graphics对象或者Graphics2D对象上进行，它们都代表了需要绘图的区域，选择哪个取决于是否要使用所增加的Java2D的图形功能。但要注意，所有的Java2D图形操作都必须在Graphics2D对象上调用。Graphics2D是Graphics的子类。
绘图的第一个步骤是创建Graphics2D对象，可以将Graphics转换为该对象：
public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
}
或者
public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
}

17.2.3 设置Graphics2D属性 P540

（1）设置填充方式 P541
如果需要对图像显示渐层式的颜色，可以设定Paint为GradientPaint的实例。
GradientPaint gp = new GradientPaint(180, 190, Color.yellow, 220, 210, Color.red, true);
g2.setPaint(gp);
（2）设置画笔颜色
BasicStroke pen = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
g2.setStroke(pen);

17.2.4 创建绘制对象 P541
Java2D中进行绘图时，不是采用对应的方法来袜，而是为要实现某种形状创建出相应的形状对象。这可以通过使用java.awt.geom包中的类来定义所要创建的形状。如线条Line2D.Float类、矩形Rectangle2D.Float或者Rectangle2D.Double类、椭圆Ellipse2D.Float、圆弧Arc2D.Float类等。
如下例所示：
Line2D.Float line = new Line2D.Float(20, 300, 200, 300);//直线
CubicCurve2D.Float cubic = new CubicCurve2D.Float(40, 100, 120, 50, 170, 270, 220, 100);//曲线
Ellipse2D.Float shape = new Ellipse2D.Float(200, 200, 60, 60);//椭圆

17.2.5 绘制对象 P542
针对不同的对象，可以使用不同的方法进行绘制。
--可以使用Graphics2D类中的方法draw()用于绘制轮廓，用fill()方法用于填充。它们都以前面所创建的图形对象作为参数。
--Java2D中的字符串的绘制仍然采用drawString()方法，有如下两个方法：
drawString(String s, float x, float y);
drawString(String str, int x, int y);
--绘制轮廓：draw(Shape s)。其中的Shape接口在Graphics2D中被定义。

新的Java2D Shape类都有“2D”后缀。这些新的形状使用浮点值（而不是整数）来描述其几何形状。
--Polygon类（int[] xpoints, int[] ypoints, int npoints）;
--RectangularShape抽象类，其子类有Arc2D、Ellipse2D、Rectangle2D。
--QuadCurve2D（二次贝塞尔样条曲线）。
--CubicCurve2D（三次贝塞尔样条曲线）。
--Area（区域）。
--GeneralPath（由直线、二次样条曲线、三次样条曲线所构成）。
--Line2D。

17.2.6 实例一：绘制图形 P542
//绘制直线、曲线及椭圆图形的实例：
package test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class DrawShape {
    public DrawShape() {
        Frame f = new Frame("Test Graphics2D");
        f.setSize(500, 500);
        f.setVisible(true);

        Panel p = new MyPanel();
        f.add(p);
    }

    private class MyPanel extends Panel {
        @Override
        public void paint(Graphics g) {
            //取得Graphics2D对象
            Graphics2D g2 = (Graphics2D) g;

            //设置渐进色
            GradientPaint gp = new GradientPaint(180, 190, Color.yellow, 220, 210, Color.red, true);
            g2.setPaint(gp);
            g2.setStroke(new BasicStroke(2.0f));//设定粗细

            //创建直线
            Line2D.Float line = new Line2D.Float(20, 300, 200, 300);
            g2.draw(line);

            //创建曲线
            CubicCurve2D.Float cubic = new CubicCurve2D.Float(70, 100, 120, 50, 170, 270, 220, 100);
            g2.draw(cubic);

            //创建椭圆
            Ellipse2D.Float shape = new Ellipse2D.Float(200, 200, 60, 60);
            g2.fill(shape);//g2.draw(shape);
        }
    }

    public static void main(String[] args) {
        new DrawShape();
    }
}

17.2.7 实例二：显示文字 P543
通过Java2D可以显示更细致的文字，下面程序显示出外边为淡蓝色的字符串：
package test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

public class DrawString {
    public DrawString() {
        Frame f = new Frame("Test Graphics2D");
        f.setSize(500, 500);
        f.setVisible(true);

        Panel p = new MyPanel();
        f.add(p);
    }

    private class MyPanel extends Panel {
        @Override
        public void paint(Graphics g) {
            //取得Graphics2D对象
            Graphics2D g2 = (Graphics2D) g;

            //显示文字
            FontRenderContext fontRenderContext = g2.getFontRenderContext();
            TextLayout textLayout = new TextLayout("Test Characters", new Font("Modern", Font.BOLD + Font.ITALIC, 20), fontRenderContext);

            //线形
            Shape shape = textLayout.getOutline(AffineTransform.getTranslateInstance(50, 180));
            g2.setColor(Color.blue);
            g2.setStroke(new BasicStroke(2.0f));
            g2.draw(shape);
            g2.setColor(Color.red);//g2.setColor(Color.white);
            g2.fill(shape);
        }
    }

    public static void main(String[] args) {
        new DrawString();
    }
}

17.2.8 实例三：显示图像 P544
通常希望图形有滤镜的效果，必须使用可以处理图形的绘图软件。在2D API中则提供了一些简单的方法，可以直接用程式码来对图形进行滤镜效果的控制。
//没看到效果？
package test;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class DrawImage {

    public void draw() {
        Frame f = new Frame("Test Graphics2D");
        f.setSize(500, 500);
        f.setVisible(true);

        String filename = "src/image/1.jpg";
        //Panel p = new MyPanel();
        f.add(new Panel() {
            @Override
            public void paint(Graphics g) {
                //取得Graphics2D对象
                Graphics2D g2 = (Graphics2D) g;

                float[] elements = {0.0f, -1.0f, 0.0f, -1.0f, 0.0f, -1.0f, 0.0f};

                //加载图片
                Image img = Toolkit.getDefaultToolkit().getImage(filename);
                int w = img.getWidth(this);
                int h = img.getHeight(this);
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

                //创建Graphics2D对象
                Graphics2D big = bi.createGraphics();
                big.drawImage(img, 0, 0, this);

                //阴影处理
                BufferedImageOp biop = null;
                AffineTransform at = new AffineTransform();
                BufferedImage bimg = new BufferedImage(img.getWidth(this), img.getHeight(this), BufferedImage.TYPE_INT_RGB);
                Kernel kernel = new Kernel(3, 3, elements);
                ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
                cop.filter(bi, bimg);
                biop = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

                //显示图像
                g2.drawImage(bimg, biop, 0, 0);
            }
        });
    }

    public static void main(String[] args) {
        new DrawImage().draw();
    }
}

17.3 音频录制与播放——JavaSound P545
暂时没有看？

17.4 视频拍照与播放——JMF多媒体库 P563
暂时没有看？
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第18课 Swing图形界面开发 P577
18.1 Swing界面组件 P577
Swing组件有如下几个特点：
--所有的Swing组件都继承自AWT的组件Component，都是AWT的派生类。
--所有的Swing件件都以字母“J”形状，以与AWT中的组件相区分。
--Swing组件对AWT的功能进行了扩展，包括容器、容器、工具栏、菜单栏、组件。

18.1.2 窗口与对话框 P579
（1）窗体JFame
package test;

import javax.swing.JFrame;

public class TestJFrame extends JFrame {

    public TestJFrame() {
        setTitle("Test JFrame");
        setLocation(20, 20);
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//获得屏幕大小
        // setLocation(screenSize.width / 2 - 300 / 2, screenSize.height / 2 - 200 / 2);//定位的位置是窗体的左上角，用屏幕长宽减去窗体的一半长宽，定位在屏幕正中央
        setSize(300, 200);
        setVisible(true);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置该属性，关闭窗口时退出程序
        // this.addWindowListener(new WindowAdapter() { //添加窗口关闭处理事件，实现一个WindowAdapter的匿名类
            // @Override
            // public void windowClosing(WindowEvent e) {
                // System.out.println("Procedure exit.");
                // System.exit(0);
            // }
        // });
        
        //设置跨平台的感观，没看到有什么效果
        String strLookFeel = UIManager.getCrossPlatformLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(strLookFeel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestJFrame();
    }
}

（2）内部窗体JInternalFrame P580
JInternalFrame是轻量级组件，不能单独出现，必须依附在顶层组件上。能够利用Java提供的Look and Feel功能做出完全不同于原有操作系统所提供的窗口外形，比JFrame更具有弹性。
JInternalFrame构造函数：
--title：窗口标题。
--resizable：可改变窗口大小。
--closable：可关闭窗口。
--maximizable：可最大化不可最小化窗口。
--iconifiable：可最大最小化的窗口。
如果某一个参数不存在，表示不具有该项功能。
package test;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestJInternalFrame extends JFrame implements ActionListener {
    public TestJInternalFrame() {
        super("Test JInternalFrame");
        JButton b = new JButton("Open JInternalFrame");
        b.addActionListener(this);
        add(b, BorderLayout.SOUTH);
        setSize(350, 350);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JInternalFrame internalFrame = new JInternalFrame("JInternalFrame", true, true, true, true);
        internalFrame.setLocation(20, 20);
        internalFrame.setSize(100, 100);
        internalFrame.setVisible(true);
        add(internalFrame);
        try {
            internalFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestJInternalFrame();
    }
}

（3）图层容器 JLayeredPane P582
JLayeredPane图层允许组件在需要时互相重叠，它将该深度范围分成几个不同的层。

（4）虚拟桌面容器JDesktopPane P583
JDesktopPane用于创建多文档界面或虚拟桌面的容器。用户可创建JInternalFrame对象并将其添加到JDesktopPane。JDesktopPane扩展了JLayeredPane，以管理可能的重叠内部窗体。
package test;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestJDesktopPane extends JFrame implements ActionListener {
    private JDesktopPane desktopPane = new JDesktopPane();
    int count = 1;

    public TestJDesktopPane() {
        super("Test JInternalFrame");
        JButton b = new JButton("Open JInternalFrame");
        b.addActionListener(this);

        Container contentPane = getContentPane();
        contentPane.add(b, BorderLayout.SOUTH);
        contentPane.add(desktopPane, BorderLayout.CENTER);//这里不用getContentPane没看到有什么不同，为什么要获取到contentPane？

        setSize(350, 350);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JInternalFrame internalFrame = new JInternalFrame("JInternalFrame" + count, true, true, true, true);
        ++count;

        internalFrame.setLocation(20, 20);
        internalFrame.setSize(200, 200);
        internalFrame.setVisible(true);
        desktopPane.add(internalFrame);
        try {
            internalFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestJDesktopPane();
    }
}

（5）对话框JDialog P584
对话框与框架（JFrame）有一些相似。对话框出现时，可以设定禁止其他窗口的输入，直到这个对话框被关闭。

package test;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestJDialog extends JFrame implements ActionListener {
    public TestJDialog() {
        super("Test JDialog");
        JButton b = new JButton("Open JDialog");
        b.addActionListener(this);

        Container contentPane = getContentPane();
        contentPane.add(b, BorderLayout.SOUTH);

        setSize(350, 350);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Open JDialog")) {
            JDialog jDialog1 = new JDialog(this, "Dialog", true);//设置为false，则可以互相切换
            jDialog1.setSize(200, 200);
            jDialog1.setLocation(450, 450);
            jDialog1.setVisible(true);
        }
    }

    public static void main(String[] args) {
        new TestJDialog();
    }
}

（6）文件选择对话框 JFileChooser P585
package test;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class TestJFileChooser extends JFrame implements ActionListener {
    public TestJFileChooser() {
        super("Test JFileChooser");
        JButton b = new JButton("Open JFileChooser");
        b.addActionListener(this);

        Container contentPane = getContentPane();
        contentPane.add(b, BorderLayout.SOUTH);

        setSize(350, 350);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Open JFileChooser")) {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setMultiSelectionEnabled(true);

            //图片过滤器
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");//这是个可变参数构造函数，可以传递多个后缀参数
            jFileChooser.setFileFilter(filter);
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//选择文件
            jFileChooser.setDialogTitle("Open JPEG file");//设置窗口标题
            int result = jFileChooser.showOpenDialog(this);//打开“打开文件”对话框
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] fileList = jFileChooser.getSelectedFiles();
                for (File file : fileList) {
                    System.out.println(file.getAbsolutePath());
                }
            }
        }
    }

    public static void main(String[] args) {
        new TestJFileChooser();
    }
}

（7）颜色选择对话框 P586
JColorChooser提供一个用于允许用户操作和选择颜色的控制器窗格。
//
package test;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestJColorChooser extends JFrame implements ActionListener {
    public TestJColorChooser() {
        super("Test JColorChooser");
        JButton b = new JButton("Open JColorChooser");
        b.addActionListener(this);

        Container contentPane = getContentPane();
        contentPane.add(b, BorderLayout.SOUTH);

        setSize(350, 350);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Open JColorChooser")) {
            Color color = JColorChooser.showDialog(this, "Choose Color", Color.red);
            System.out.println(color.getRGB());
        }
    }

    public static void main(String[] args) {
        new TestJColorChooser();
    }
}

18.1.3 基本组件 P587
Tips：以下输入组件都可以使用getText()取得组件的内容。
（1）文本JLabel
显示一段文本，创建如下：
Label label = new Label("this is a label");
调用容器的add()方法即可将label添加到容器中。
（2）按钮JButton
（3）复选框JCheckBox
（4）单选框JRadioButton
（5）文本框JTextField P588
（6）密码框JPasswordField
（7）文本域JTextArea
（8）文本文件域JTextPane
JTextPane可以编辑文本和图标，可以使用如下方法将编辑器与一个文本文档关联：
void setDocument(Document doc);
也可以将一个图标插入文档中，以替换当前选择的内容：
void insertIcon(Icon g);
（9）HTML编辑域JEditorPane
可以编纯文本、HTML和RTF内容，可以调用下面的方法来设置显示一个HTML页面：
pane.setPage(e.getURL());
也可以向下面这样取得其中的HTML内容：
HTMLDocument doc = (HTMLDocument) pane.getDocument();
（10）列表JList
String[] data = {"one", "two", "three"};
JList list = JList(data);
（11）滚动条JScrollBar P588
滚动条可以添加到任何的容器中，如下：
Container mainPane = this.getContentPane();
JScrollBar vscroll = new JScrollBar(JScrollBar.VERTICAL);
mainPane.add(vscroll, BordreLayout.EAST);

（12）进度条JProgressBar P589
创建进度条示例：
progressBar = new JProgressBar(0, task.getLengthOfTask());
progressBar.setValue(0);
progressBar.setStringPainted(true);

更新进度条值：
progressBar.setValue(task.getCurrent());

下面示例将进度条设置为不确定模式，然后在知道任务长度后切换回确定模式：
progressBar = new JProgressBar();
progressBar.setIndeterminate(true);//设置为不确定模式
progressBar.setMaximum(newLength);//设置最大值
progressBar.setValue(newValue);//设置当前值
progressBar.setIndeterminate(false);//设置为确定模式

（13）滑标组件 JSlider P589
滑块可以显示主刻度标记及主刻度之间的次刻度标记。刻度标记之间的值的个数由setMajorTickSpacing()和setMinorTickSpacing()来控制。刻度标记和绘制由setPaintTicks()控制。滑块可以在固定时间间隔（或在任意位置）沿滑块刻度打印文本标签。标签的绘制由setLabelTable()来setPaintLabels()控制。
package test;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Container;

public class TestJSlider extends JFrame {
    public TestJSlider() {
        super("Test JSlider");
        JSlider jSlider = new JSlider(0, 100, 0);
        //主刻度
        jSlider.setMajorTickSpacing(10);
        //次刻度
        jSlider.setMinorTickSpacing(5);
        jSlider.setPaintTicks(true);
        jSlider.setPaintLabels(true);
        jSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JSlider) {
                    System.out.println("Tick: " + ((JSlider) e.getSource()).getValue());
                }
            }
        });

        Container contentPane = getContentPane();
        contentPane.add(jSlider, BorderLayout.SOUTH);

        setSize(350, 350);
        setVisible(true);
    }

    public static void main(String[] args) {
        new TestJSlider();
    }
}

（14）表格组件 JTable P590
暂时没看？

（15）树形组件 JTree P593
JTree是Swing里面比较复杂的组件之一，这个类的实例代表一整棵树，而一棵树由许多节点组成。JTree中的每一个节点都必须实现TreeNode这个Interface，我们可以自己来实现，当然Swing也为我们提供了一个DefaultMutableTreeNode的实现。
package test;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.BorderLayout;
import java.awt.Container;

public class TestJTree extends JFrame {
    public TestJTree() {
        super("Test JSlider");

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("China");
        DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("Beijing");
        DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("Shanghai");
        DefaultMutableTreeNode cc = new DefaultMutableTreeNode("Haiding");

        JTree jt = new JTree(root);
        root.add(child1);
        root.add(child2);
        child1.add(cc);

        jt.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode n = (DefaultMutableTreeNode) jt.getLastSelectedPathComponent();
                if (n == null) {
                    return;
                }

                String content = (String) n.getUserObject();
                System.out.println(content);
            }
        });

        Container contentPane = getContentPane();
        contentPane.add(jt, BorderLayout.CENTER);

        setSize(350, 350);
        setVisible(true);
    }

    public static void main(String[] args) {
        new TestJTree();
    }
}

18.1.4 菜单栏组件 P594
（1）菜单栏 JMenuBar
添加菜单组件，首先需要添加菜单栏JMenuBar。MenuBar只能被添加到JFrame对象中，作为整个菜单树的根基。如下：
JMenuBar mb = new JMenuBar();
setJMenuBar(mb);

（2）菜单JMenu
JMenu m1 = new JMenu("File");
JMenu m2 = new JMenu("Edit");
JMenu m3 = new JMenu("Help");
mb.add(m1);
mb.add(m2);
mb.add(m3);

（3）菜单选项 JMenuItem
JMenuItem mi1 = new JMenuItem("Open");
JMenuItem mi2 = new JMenuItem("Save");
JMenuItem mi3 = new JMenuItem("Exit");
m1.add(mi1);
m1.add(mi2);
m1.addSeparator();
m1.add(mi3);

除此之外，还可以添加复选框和单选按钮选项：
JCheckBoxMenuItem mi4 = new JCheckBoxMenuItem("Modify");
JRadioButtonMenuItem mi5 = new JRadioButtonMenuItem("Bold");
m2.add(mi4);
m2.add(mi5);

JMenuItem对象可以添加ActionListener事件，例其能够完成相应的监听操作。如下：
mi1.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        System.out.println("Open event");
    }
});

//
package test;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestJMenu extends JFrame {
    public TestJMenu() {
        super("Test JMenu");
        JMenuBar mb = new JMenuBar();
        setJMenuBar(mb);

        JMenu m1 = new JMenu("File");
        JMenu m2 = new JMenu("Edit");
        JMenu m3 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        mb.add(m3);

        JMenuItem mi1 = new JMenuItem("Open");
        JMenuItem mi2 = new JMenuItem("Save");
        JMenuItem mi3 = new JMenuItem("Exit");
        m1.add(mi1);
        m1.add(mi2);
        m1.addSeparator();
        m1.add(mi3);

        JCheckBoxMenuItem mi4 = new JCheckBoxMenuItem("Modify");
        JRadioButtonMenuItem mi5 = new JRadioButtonMenuItem("Bold");
        m2.add(mi4);
        m2.add(mi5);

        mi1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open event");
            }
        });

        setSize(350, 350);
        setVisible(true);
    }

    public static void main(String[] args) {
        new TestJMenu();
    }
}

（4）弹出菜单 JPopupMenu P595
JPopupMenu是弹出菜单的实现。
下面代码创建一个JPopupMenu：
JPopupMenu jp = new JPopupMenu();
创建带有标题的JPopupMenu：
JPopupMenu jp = new JPopupMenu("Popup menu");

与JMenu一样，可以使用add()方法和insert()方法向JPopupMenu中添加或者插入JMenuItem与JComponent。JPopupMenu对添加到其中的每一个菜单项都赋予一个整数索引，并根据弹出式菜单的布局管理器调整菜单项显示的顺序。还可以使用addSeparator()方法添加分隔线，并且JPopupMenu也会为该分隔线指定一个整数索引。
通过调用弹出式菜单触发器对应的show()方法来显示弹出式菜单，show()方法会在菜单显示之前对其location和invoker属性加以设定。因此，应该检查所有的MouseEvent事件，看其是否是弹出式菜单触发器，然后在合适的时候显示弹出式菜单，下面的showJPopupMenu方法在收到适当的触发器事件时就会显示弹出式菜单，如下：

//
package test;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TestJPopupMenu extends JFrame {
    public TestJPopupMenu() {
        super("Test JPopupMenu");

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");
        JMenuItem exit = new JMenuItem("Exit");
        popupMenu.add(copy);
        popupMenu.add(paste);
        popupMenu.addSeparator();
        popupMenu.add(exit);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //if (e.isPopupTrigger()) {//这个不知道如何触发
                    //如果当前事件与鼠标事件相关，则弹出菜单
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                //}
            }
        });

        this.setBounds(100, 100, 250, 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setSize(350, 350);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new TestJPopupMenu();
    }
}

18.1.5 工具栏组件 JToolBar P596
JToolBar提供了一个用来显示常用的Action或控件的组件。用户可以将工具栏拖到单独的窗口中（除非floatable属性被设置为false）。为了正确执行拖动，建议将JToolBar实例添加到容器的四“边”中的一边（其中容器的布局管理器为BorderLayout）。并且不在其他四“边”中添加任何子级。
下面实例实现了一个工具栏，包含3个按钮，并设置为可以拖动的，如下：
package test;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class TestJToolBar extends JFrame {
    public TestJToolBar() {
        JToolBar bar = new JToolBar();
        JButton button1 = new JButton("New");
        JButton button2 = new JButton("Print");
        JButton button3 = new JButton("Exit");
        bar.add(button1);
        bar.add(button2);
        bar.addSeparator();
        bar.add(button3);

        bar.setFloatable(true);//可拖动
        bar.setToolTipText("Tool Bar");//提示

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(bar);
        this.add(panel, BorderLayout.NORTH);

        this.setTitle("Test JToolBar");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 200);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new TestJToolBar();
    }
}

18.2 Swing 布局管理器 P597
（1）BoxLayout
允许垂直或水平布置多个组件的布局管理器。垂直排列的组件在重新调整框架的大小时仍然被垂直排列。
BoxLayout管理器是用axis参数构造的，该参数指定了将进行的布局类型。
BoxLayout(Container target, int axis);
axis有以下4个选择：
--X_AXIS：从左到右水平布置组件
--Y_AXIS：从上到下垂直布置组件
--LINE_AXIS：根据容器的CompnentOrientation属性，按照文字在一行中的排列方式布置组件。如果容器的ComponentOrientation表示水平，则将组件水平放置，否则将它们垂直放置。对于水平方向，如果容器的ComponentOrientation表示从左到右，则组件从左到右放置，否则将它们从右到左放置。对于垂直方向，组件总是从上到下放置的。
--PAGE_AXIS：上LINE_AXIS相反
对于所有方向，组件按照将它们添加到容器中的顺序排列。

（2）GroupLayout P598
（3）OverlayLayout P598
（4）JScrollPaneLayout P598
（5）SpringLayout P598
（6）ViewportLayout P598

18.3 Swing事件处理 P598

18.4.3 Java文件编辑器 P600
package test;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class JTextEditor extends WindowAdapter implements ActionListener {
    private JFrame frame;//主窗体
    private JTextArea textArea;//文件输入区域
    private String fileName;//打开的文件名

    public void createEditor() {
        //建立文件菜单
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        //新建菜单
        JMenuItem menuNew = new JMenuItem("New");
        menuNew.addActionListener(this);
        menuFile.add(menuNew);
        //打开菜单
        JMenuItem menuOpen = new JMenuItem("Open");
        menuOpen.addActionListener(this);
        menuFile.add(menuOpen);
        //保存菜单
        JMenuItem menuSave = new JMenuItem("Save");
        menuSave.addActionListener(this);
        menuFile.add(menuSave);
        //另存为菜单
        JMenuItem menuSaveAs = new JMenuItem("Save as...");
        menuSaveAs.addActionListener(this);
        menuFile.add(menuSaveAs);
        //退出菜单
        JMenuItem menuExit = new JMenuItem("Exit");
        menuExit.addActionListener(this);
        menuFile.add(menuFile);

        menuBar.add(menuFile);

        JMenu menuHelp = new JMenu("Help");
        JMenuItem menuAbout = new JMenuItem("About");
        menuAbout.addActionListener(this);
        menuHelp.add(menuAbout);
        menuBar.add(menuHelp);

        //主窗口
        frame = new JFrame("Java Text Editor");
        frame.setJMenuBar(menuBar);
        textArea = new JTextArea();
        frame.add("Center", textArea);
        frame.addWindowListener(this);//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);？
        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        JTextEditor te = new JTextEditor();
        te.createEditor();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    //菜单选择事件
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand() == "New") {
                textArea.setText("");
            } else if (e.getActionCommand() == "Open") {
                //选择文件
                JFileChooser dlg = new JFileChooser();
                int result = dlg.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = dlg.getSelectedFile();
                    fileName = file.getAbsolutePath();

                    //读取文件
                    FileReader fr = new FileReader(fileName);
                    BufferedReader br = new BufferedReader(fr);
                    String str = "";
                    while (br.ready()) {
                        int c = br.read();
                        str += (char) c;
                    }
                    textArea.setText(str);
                    br.close();
                    fr.close();
                    frame.setTitle("Java Text Editor - " + fileName);
                }
            } else if (e.getActionCommand() == "Save") {
                //写入文件
                File file = new File(fileName);
                FileWriter fos = new FileWriter(file, true);
                BufferedWriter bos = new BufferedWriter(fos);
                PrintWriter pos = new PrintWriter(bos);
                pos.print(textArea.getText());

                pos.close();
                bos.close();
                fos.close();
            } else if (e.getActionCommand() == "Save as...") {
                JFileChooser dlg = new JFileChooser();
                int result = dlg.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = dlg.getSelectedFile();
                    //写入文件
                    FileWriter fos = new FileWriter(file, true);
                    BufferedWriter bos = new BufferedWriter(fos);
                    PrintWriter pos = new PrintWriter(bos);
                    pos.print(textArea.getText());

                    pos.close();
                    bos.close();
                    fos.close();
                }
            } else if (e.getActionCommand() == "Exit") {
                System.exit(0);
            } else if (e.getActionCommand() == "About") {
                final JDialog dialog = new JDialog(frame, "About", true);
                dialog.setSize(267, 117);
                dialog.setLayout(new GridLayout(1, 1));

                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        dialog.dispose();
                    }
                });

                JPanel topPanel = new JPanel();
                JLabel label = new JLabel("Java Text Editor - Author: DingBen");
                topPanel.add(label, BorderLayout.CENTER);
                dialog.add(topPanel);
                dialog.setVisible(true);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第19课 SWT图形界面开发 P607
暂时没看？
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第20课 SWT增强组件库 P643
暂时没看？
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第21课 Applet组件编程 P661
暂时没看？
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第22课 Java网络编程 P681
暂时没看？
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第23课 NIO非阻塞编程 P741
暂时没看？
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第24课 RMI分布式网络编程 P801
暂时没看？
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第25课 Corba分布式网络编程 P821
25.2 使用Java编写CORBA程序——HelloWorld实例 P827
使用Java编写CORBA程序主要依赖于IDL接口的编程与编译，共包括如下的5个过程。
（1）定义远程接口
（2）编译远程接口
（3）实现服务器
（4）实现客户机
（5）启动应用程序

25.2.1 创建IDL接口Hello.idl P828
编写IDL接口文件，放在src目录下：
module helloworld
{
    interface Hello
    {
        string sayHello(in string world);
    };
};
指定模块名为helloworld，接口名为Hello，定义了一个接口函数sayHello()，输入参数为string类型，并返回一个string类型的结果。

25.2.2 编译IDL接口产生6个文件
最新的JDK 1.3 以上版本中，提供了新的IDL编辑器idlj，位于JDK的bin\idlj.exe。
进入Hello.idl文件的目录，执行如下编译命令来编译IDL接口：
E:\Program Files\JetBrains\JavaProject\src\Hello.idl
编译后在当前目录下生成helloworld子目录，其中包括一些支持文件，如下：
--Hello.java：标记接口文件。CORBA规范指定这个文件必须扩展IDLEntity，并且与IDL接口同名。这个文件提供类型标记，从而使这个接口能用于其他接口的方法声明。内容如下：
package helloworld;
public interface Hello extends HelloOperations, org.omg.CORBA.Object, org.omg.CORBA.portable.IDLEntity 
{
} // interface Hello

--HelloOperations.java：内含Java公共接口——HelloOperations。规范指出，这个文件应该与具有Operations后缀的IDL接口同名，并且这个文件内含此接口映射的操作标记，上面定义的标记接口（Hello.java）可扩展这个接口。内容如下：
package helloworld;
public interface HelloOperations 
{
  String sayHello (String world);
} // interface HelloOperations

--HelloHelper.java：设计helper类的目的是，让所需要的许多内务处理功能脱离我们的接口，但又随时可用到实现过程。帮助程序文件含有重要的静态narrow方法，这种方法使org.omg.CORBA.Object收缩为一种更具体的类型的对象引用，在这种情况下，将是一个计算程序类型。内容如下：
package helloworld;
abstract public class HelloHelper
{
  private static String  _id = "IDL:helloworld/Hello:1.0";

  public static void insert (org.omg.CORBA.Any a, helloworld.Hello that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static helloworld.Hello extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (helloworld.HelloHelper.id (), "Hello");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static helloworld.Hello read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_HelloStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, helloworld.Hello value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static helloworld.Hello narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof helloworld.Hello)
      return (helloworld.Hello)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      helloworld._HelloStub stub = new helloworld._HelloStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static helloworld.Hello unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof helloworld.Hello)
      return (helloworld.Hello)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      helloworld._HelloStub stub = new helloworld._HelloStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }
  
}

--HelloHolder.java：holder类是一个专门化类，是为了需要通过引用来传递参数的任意数据类型而生成的。这个示例中将不使用holder类，将会在以后的栏目中经常见到它。内容如下：
package helloworld;
public final class HelloHolder implements org.omg.CORBA.portable.Streamable
{
  public helloworld.Hello value = null;

  public HelloHolder ()
  {
  }

  public HelloHolder (helloworld.Hello initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = helloworld.HelloHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    helloworld.HelloHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return helloworld.HelloHelper.type ();
  }

}

--HelloPOA.java：skeleton类为CORBA功能提供了请求——响应探测的一大部分。生成HelloPOA.java，是因为默认方式下的实现是基于继承的，如果我们选择基于委托的方式，输出就会不一样。内容如下：
package helloworld;
public abstract class HelloPOA extends org.omg.PortableServer.Servant
 implements helloworld.HelloOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("sayHello", new java.lang.Integer (0));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // helloworld/Hello/sayHello
       {
         String world = in.read_string ();
         String $result = null;
         $result = this.sayHello (world);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:helloworld/Hello:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Hello _this() 
  {
    return HelloHelper.narrow(
    super._this_object());
  }

  public Hello _this(org.omg.CORBA.ORB orb) 
  {
    return HelloHelper.narrow(
    super._this_object(orb));
  }


} // class HelloPOA

--_HelloStub.java：这是一个stub类。客户机将需要这个类来进行工作。内容如下：
package helloworld;
public class _HelloStub extends org.omg.CORBA.portable.ObjectImpl implements helloworld.Hello
{

  public String sayHello (String world)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("sayHello", true);
                $out.write_string (world);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return sayHello (world        );
            } finally {
                _releaseReply ($in);
            }
  } // sayHello

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:helloworld/Hello:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _HelloStub

上面生成的6个文件位于src目录下的helloworld中，正好是我们当前的开发目录。

25.2.3 创建IDL接口实现类HelloImpl.java P829
生成的文件在服务器上开始工作，必须编写实现接口的实现类HelloImpl。继承自HelloPOA类，从客户机发来一个请求时，该请求通过 ORB进入HelloPOA，最终调用HelloImpl来完成请求并启动响应。如下：
package corba;

import helloworld.HelloPOA;

/**
 * Created by Ben on 2016/8/18.
 */
public class HelloImpl extends HelloPOA {
    @Override
    public String sayHello(String world) {
        return "Hello " + world + "!";
    }
}

25.2.4 实现服务器 HelloServer.java P829
package corba;

import helloworld.Hello;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

/**
 * Created by Ben on 2016/8/18.
 */
public class HelloServer {
    public static void main(String args[]) {
        try {
            //根据端口创建ORB实例
            String str[] = {"-ORBInitialPort", "1050"};
            ORB orb = ORB.init(str, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("RootPOA");

            //创建服务对象
            org.omg.PortableServer.POA rootPOA = org.omg.PortableServer.POAHelper.narrow(objRef);
            org.omg.PortableServer.POAManager manager = rootPOA.the_POAManager();

            //具体实现类
            HelloImpl helloImpl = new HelloImpl();
            Hello hello = helloImpl._this(orb);

            //创建命名并注册
            NamingContext ncRef = NamingContextHelper.narrow(orb.resolve_initial_references("NameService"));
            NameComponent nc = new NameComponent("Hello", "");
            NameComponent path[] = {nc};
            ncRef.rebind(path, hello);
            manager.activate();
            System.out.println("Service is running...");

            //等待客户端调用
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

25.2.5 实现客户端 HelloClient.java P830
考虑一下正在发生的事件的机制，就会明白客户端和服务器实现上是互为映像的。客户端将所有的参数打包以创建一个请求，然后以它自己的方式来发送这个请求。服务器只是将请求中的参数解包、执行运行，将返回值和输出参数打包，然后向客户端发回响应。客户端则将返回值和输出参数解包，然后继续处理。这样，客户端打包什么，服务器就解包什么，反之亦然。这意味着将会看到客户端和服务器具有相似的结构。客户端还必须创建并初始化一个ORB。
（1）创建一个ORB。
（2）获取一个指向命名上下文的引用。
（3）在命名上下文中查找“Hello”并获得指向该CORBA对象的引用。
（4）调用对象的sayHello()操作并打印结果。
客户端程序如下：
package corba;

import helloworld.Hello;
import helloworld.HelloHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

/**
 * Created by Ben on 2016/8/18.
 */
public class HelloClient {
    public static void main(String args[]) {
        try {
            //根据端口创建ORB实例
            String str[] = {"-ORBInitialPort", "1050"};
            ORB orb = ORB.init(str, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            //获取一个指向命名上下文的引用
            NamingContext ncRef = NamingContextHelper.narrow(objRef);
            NameComponent nc = new NameComponent("Hello", "");
            NameComponent path[] = {nc};

            //查找Hello对象
            Hello hello = HelloHelper.narrow(ncRef.resolve(path));

            //调用函数
            String show = hello.sayHello("World");
            System.out.println(show);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

25.2.6 运行程序 P831
（1）启动一个MS-DOS命令解释器：
tnameserv -ORBInitialPort 1050

运行后会初始化引用端口为1050，输出的窗口如下：
C:\Users\Ben>tnameserv -ORBInitialPort 1050
初始的命名上下文:
IOR:000000000000002b49444c3a6f6d672e6f72672f436f734e616d696e672f4e616d696e67436f6e746578744578743a312e30000000000001000000000000009a000102000000000e3139322e3136382e312e31323100041a00000045afabcb0000000020000f424000000001000000000000000200000008526f6f74504f41000000000d544e616d65536572766963650000000000000008000000010000000114000000000000020000000100000020000000000001000100000002050100010001002000010109000000010001010000000026000000020002
TransientNameServer: 将初始对象引用端口设置为: 1050
准备就绪。

（2）运行HelloServer服务器：
看到Service is running...

（3）启动HelloClient应用程序客户端：
屏幕上会出现“Hello World!”的字样，说明实验成功了。

与传统的客户/服务应用开发完全不同，使用CROBA后，开发人员再不必关心客户和服务之间的通信问题，也不必处理客户和服务之间的协调问题，客户系统和服务系统可以在不同的机器系统中运行，并且可以用不同的语言来实现（如本例中的服务器程序完全可以用C++来编写），这些都由CORBA负责解决，对应用开发者来说都是透明的。

25.2.7 补充：IDL的语法规则 P832
接口描述语言即Interface Description Language或者缩写为IDL，是用来描述软件组件接口的一种计算机语言。IDL通过一种中立的方式来描述接口，使得在不同平台上运行的对象和用不同语言编写的程序可以相互通信交流，如，一个组件用C++写，另一个用Java写成。
1. OMG IDL文件结构
IDL文件以module开始命名模块名，相当于Java程序中的包名。然后在模块中定义数据类型和接口。如下：
module Compute
{
    typedef double radius;
    typedef long times;
    interface PI
    {double getResult(in radius aRadius, in times time);}
}

2. OMG IDL语法规则
采用ASCII字符集构成接口定义的所有标识符。标识符由字母、数字和下画线的任意组合构成，但第一个字符必须是ASCII字母。IDL认为大写字母和小写字母具有相同的含义，如anExample和AnExample是相同的。
程序设计人员不能将关键字用做变量或方法名。需要注意的是关键字的大小写，如：
typedef double context;//错误，变量context是关键字
typedef double CONTEXT;//错误，CONTEXT与关键字context冲突

3. 数据类型
--基本数据类型：OMG IDL基本数据类型包括short、long和相应的无符号（unsigned）类型，表示的字长分别为16、32位。
--浮点数类型：OMG IDL浮点数类型包括float、double和long double类型。
--字符和超大字符类型：OMG IDL定义字符类型char为面向字节的码集中编码的单字节字符；定义类型wchar为从任意字符集中编码的超大字符。
--逻辑类型：用boolean关键字定义的一个变量，聚会只有true和false。
--八进制类型：用octet关键字定义，在网络传输过程中不进行高低位转换的位元序列。
--any数据类型：引入该类型用于表示OMG IDL中任意数据类型。

4. 常量
const double PI = 3.1415926;
在IDL中，可以定义long、unsigned long、string等类型的常量。

5. 构造数据类型 P834
OMG IDL中构造数据类型包括结构、联合、枚举等形式，如下例。
结构类型：
typedef long GoodsNumber;
struct
{
    GoodsNumber number;
    string name;
    float price;
}

联合类型：
union stockIn switch(short)
{
    case 1: stocker: long;
    case 2: goodsName1: string;
    case 3: goodsName2: string;
}

枚举类型：
enum GoodsNumber{GOODS_SAILED, GOODS_INSTOCK};

6. 数组类型 P834
typedef long ADimension[20][100];

7. 模板(template)类型
OMG IDL提供两种类型的模板。
（1）序列（sequence）类型
用该方法可以定义长度可变的任意数值类型的存储序列，通常在定义时可以指定长度，也可以不指定，如：
typedef sequence<80> aSequence;//长度定义为80
typedef sequence anotherSequence;//长度不定

（2）字符串（string）序列
同样对于字符串序列类型，也有两种定义方式：
typedef string<80> aName;
typedef string anotherName;

8. 接口(interface)
接口作为服务对象功能的详细描述，封装了服务对象提供服务方法的全部信息，客户对象利用该接口获取服务对象的属性、访问服务对句中的方法。接口用关键字interface声明，其中包含的属性和方法对所有提出服务请求的客户对象是公开的，如下：
interface JobManager
{
    readonly attribute string FirstName;
    attribute string status;
    string QueryJobStatus(in long Number, out string property);
}
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第26课 Java反射编程与动态代码 P845
Java反射（Reflection）机制是在运行状态中，对于 任意一个类，都能够查找这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法；这种动态获取的信息及动态调用对象的方法的功能称为Java语言的反射机制。
Java反射机制主要提供了以下功能。
--在运行时判断任意一个对象所属的类。
--在运行时判断任意一个类所具有的成员变量和方法。
--在运行时构造任意一个类的对象。
--在运行时调用任意一个对象的方法和变量。
--生成动态代理（Dynamic Proxy）。

26.1 Java反射机制 P845
26.1.1 反射的概念 P845
开放性和原因连接是反射系统的两在基本要素。

26.1.2 Java中的反射 P846
Reflection是Java程序开发语言的特征之一，它允许运行中的Java程序对自身进行检查，或者说“自审”，并能直接操作程序的内部属性。例如，使用它能够获得Java类中各成员的名称并显示出来。

26.1.3 第一个反射的例子 P846

package test;

import java.lang.reflect.Method;//是用来描述某个类中单个方法的一个类

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class c = Class.forName("java.lang.System");//载入指定的类
            Method m[] = c.getDeclaredMethods();//获取这个类中的定义了的方法列表
            for (Method method : m) {
                System.out.println(method.toString());
            }
        } catch (Exception e) {
        }
    }
}
输出：
public static void java.lang.System.exit(int)
public static void java.lang.System.runFinalization()
public static void java.lang.System.runFinalizersOnExit(boolean)
private static void java.lang.System.initializeSystemClass()
public static java.lang.String java.lang.System.setProperty(java.lang.String,java.lang.String)
public static java.lang.String java.lang.System.getProperty(java.lang.String)
public static java.lang.String java.lang.System.getProperty(java.lang.String,java.lang.String)
public static native int java.lang.System.identityHashCode(java.lang.Object)
public static native long java.lang.System.currentTimeMillis()
public static native long java.lang.System.nanoTime()
public static native void java.lang.System.arraycopy(java.lang.Object,int,java.lang.Object,int,int)
private static native void java.lang.System.registerNatives()
public static java.lang.SecurityManager java.lang.System.getSecurityManager()
public static void java.lang.System.load(java.lang.String)
public static void java.lang.System.loadLibrary(java.lang.String)
public static native java.lang.String java.lang.System.mapLibraryName(java.lang.String)
private static void java.lang.System.checkIO()
private static void java.lang.System.checkKey(java.lang.String)
public static java.lang.String java.lang.System.clearProperty(java.lang.String)
public static java.io.Console java.lang.System.console()
public static void java.lang.System.gc()
public static java.util.Properties java.lang.System.getProperties()
public static java.lang.String java.lang.System.getenv(java.lang.String)
public static java.util.Map java.lang.System.getenv()
public static java.nio.channels.Channel java.lang.System.inheritedChannel() throws java.io.IOException
private static native java.util.Properties java.lang.System.initProperties(java.util.Properties)
public static java.lang.String java.lang.System.lineSeparator()
private static java.io.PrintStream java.lang.System.newPrintStream(java.io.FileOutputStream,java.lang.String)
public static void java.lang.System.setErr(java.io.PrintStream)
private static native void java.lang.System.setErr0(java.io.PrintStream)
public static void java.lang.System.setIn(java.io.InputStream)
private static native void java.lang.System.setIn0(java.io.InputStream)
private static void java.lang.System.setJavaLangAccess()
public static void java.lang.System.setOut(java.io.PrintStream)
private static native void java.lang.System.setOut0(java.io.PrintStream)
public static void java.lang.System.setProperties(java.util.Properties)
public static void java.lang.System.setSecurityManager(java.lang.SecurityManager)
private static synchronized void java.lang.System.setSecurityManager0(java.lang.SecurityManager)

26.1.4 Java反射API P847
Class是Java反射中的一个核心类，它代表了内存中的一个Java类。通过它可以取得类的各种操作属性，这些属性是通过java.lang.reflect包中的反射API来描述的。
--Constructor：描述一个类的构造方法
--Field：描述一个类的成员变量
--Method：描述一个类的方法
--Modifer：描述类内各元素的修饰符
--Array：用来对数组进行操作

对于任何一个类来说，都可以通过Class提供的反射调用以不同的方式来获得类的信息。除了可以使用newInstance()创建该类的实例外，可以使用Class取得类的包和类名：
Package getPackage();
String getName();

Class最重要的功能是提供了一组反射调用，用以取得该类的构造函数、变量及方法。
1. 取得构造函数——返回类型 Constructor P848

2. 取得变量——返回类型 Field P848

3. 取得方法——返回类型 Method P849
--使用特定的参数类型获得命名的方法
Method getMethod(String name, Class<?>...parameterTypes);
--获得所有方法列表：
Method[] getMethods();
--获取本地或匿名类的方法：
Method getEnclosingMethod();

4. Array类 P850
提供了动态创建和访问Java数组的方法，这些方法对应了8个Java的基本数据类型，形如：
static boolean getBoolean(Object array, int index);
static void setBoolean(Object array, int index, boolean z);

5. Modifier类 P850
提供了static方法和常量，对类和成员访问修符符进行解码。修饰符集被表示为整数，用不同的位置（bit position）表示不同的修饰符。
static int ABSTRACT;//表示abstract修饰符的int的值

26.2 Java反射应用——检测类 P850
package test.reflection;

public class MyObject {
    public int a;
    public int b;

    public MyObject(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int sum() {
        return a + b;
    }

    public int minus() {
        return a - b;
    }

    public int multiply() {
        return a * b;
    }

    public int divide() {
        return a / b;
    }

    public String longer(String s1, String s2) {
        return s1.length() > s2.length() ? s1 : s2;
    }
}


26.2.1 获取类 P851
在运行的Java程序中，用java.lang.Class类来描述类和接口等。
下面就是获得一个Class对象的方法之一：
Class cls = Class.forName("test.MyObject");
还有另一种方法，如下：
Class cls = test.MyObject.class;

26.2.2 获取类的方法
package test;

import java.lang.reflect.Method;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class cls = Class.forName("test.reflection.MyObject");
            Method m[] = cls.getDeclaredMethods();
            for (Method method : m) { //for (int i = 0; i < methods.length; ++i)
                System.out.println("方法名：" + method.getName());
                System.out.println("修饰符：" + method.getModifiers());
                System.out.println("返回值：" + method.getReturnType());

                Class parameters[] = method.getParameterTypes();
                for (int j = 0; j < parameters.length; ++j) {
                    System.out.println("参数：" + j + "：" + parameters[j]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
输出：
方法名：sum
修饰符：1
返回值：int
方法名：divide
修饰符：1
返回值：int
方法名：minus
修饰符：1
返回值：int
方法名：longer
修饰符：1
返回值：class java.lang.String
参数：0：class java.lang.String
参数：1：class java.lang.String
方法名：multiply
修饰符：1
返回值：int

修饰符public是1，private是2
getDeclaredMethods()来获取一系列的Method对象，分别描述了定义在类中的每一个访求，包括public方法、protected方法和private方法等。如果在程序中使用getMethods()，还勇气获得继承来的各个方法的信息。

26.2.3 获取类的构造器 P852
获取类构造器的用法与上述获取方法的用法类似，如下：
package test;

import java.lang.reflect.Constructor;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class cls = Class.forName("test.reflection.MyObject");
            Constructor constructors[] = cls.getDeclaredConstructors();
            for (Constructor constructor : constructors) { //for (int i = 0; i < methods.length; ++i)
                System.out.println("方法名：" + constructor.getName());
                System.out.println("修饰符：" + constructor.getModifiers());

                Class parameters[] = constructor.getParameterTypes();
                for (int j = 0; j < parameters.length; ++j) {
                    System.out.println("参数：" + j + "：" + parameters[j]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
输出：
方法名：test.reflection.MyObject
修饰符：1
参数：0：int
参数：1：int

26.2.4 获取类的变量 P852
找出一个类中定义了哪些数据字段也是可能的，如下：
package test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class cls = Class.forName("test.reflection.MyObject");
            Field fields[] = cls.getDeclaredFields();
            for (Field field : fields) { //for (int i = 0; i < methods.length; ++i)
                System.out.println("方法名：" + field.getName());
                System.out.println("修饰符：" + field.getModifiers());
                System.out.println("变量类型：" + field.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
输出：
方法名：a
修饰符：1
变量类型：int
方法名：b
修饰符：1
变量类型：int

和获取方法的情况一样，获取字段的时候也可以只取得在当前类中申明了的字段信息（使用getDeclaredFields()方法），或者也可以取得父类中定义的字段（使用getFields()方法）。

26.3 Java反射应用——处理对象 P853
可以分所构造器来实例化一个对象。由于类MyObject构造函数有两个int类型的参数，因此需要先创建一个类型数组，再根据类型数组取得构造函数：

package test;

import java.lang.reflect.Constructor;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class cls = Class.forName("test.reflection.MyObject");
            //设置参数类型数组
            Class paramTypes[] = new Class[2];
            paramTypes[0] = int.class;
            paramTypes[1] = int.class;

            //根据参数类型取得构造函数
            Constructor constructor = cls.getConstructor(paramTypes);
            Object object =   constructor.newInstance(3, 4);

            //调用默认构造函数，不过此例中MyObject没有默认构造函数，运行报错
//            constructor = cls.getConstructor();
//            object = constructor.newInstance();
            //System.out.println("3 + 4 = " + ((MyObject) object).sum());//应该不是这样调用函数？

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

26.3.2 改变变量的值 P853
package test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class cls = Class.forName("test.reflection.MyObject");
            //设置参数类型数组
            Class paramTypes[] = new Class[2];
            paramTypes[0] = int.class;
            paramTypes[1] = int.class;

            Constructor constructor = cls.getConstructor(paramTypes);
            Object object = constructor.newInstance(3, 4);

            //改变字段的值
            Field fieldA = cls.getField("a");
            System.out.println("改变前：" + fieldA.get(object));
            fieldA.setInt(object, 100);
            System.out.println("改变后：" + fieldA.get(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
输出：
改变前：3
改变后：100

26.3.3 执行类的方法 P854
与构造方法的调用过程相似，需要首先根据需要调用方法的参数类型创建一个参数类型数组，然后再创建一个输入值数组，执行方法的调用。如果函数没有参数，则直接调用即可。
package test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class cls = Class.forName("test.reflection.MyObject");
            //设置参数类型数组
            Class paramTypes[] = new Class[2];
            paramTypes[0] = int.class;
            paramTypes[1] = int.class;

            Constructor constructor = cls.getConstructor(paramTypes);
            Object object = constructor.newInstance(3, 4);

            //执行无入参方法
            Method sumMethod = cls.getMethod("sum");
            Object sumRet = sumMethod.invoke(object);
            System.out.println("3 + 4 = " + sumRet);

            //执行带入参的方法
            Class paraStringTypes[] = new Class[] {String.class, String.class};//这里的new数组里，如果后面跟了数组对象，则不能带数字new Class[2] {String.class, String.class}将编译错误
            Method longerMethod = cls.getMethod("longer", paraStringTypes);//这里必须将函数名与参数类型都传入
            String stringParam[] = {"Hello", "World"};
            Object longerRet = longerMethod.invoke(object, stringParam);//这里传入要触发的对象和参数值
            System.out.println("Longer of Hello and World is " + longerRet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

假如一个程序在执行的某处时才知道需要执行的某个方法，这个方法的名称是在程序的运行过程中指定的（如，JavaBean开发环境中就会做这样的事），上面的程序演示了如何做到。

26.3.4 使用数组 P854
package test;

import java.lang.reflect.Array;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class str = Class.forName("java.lang.String");//取得类
            Object arr = Array.newInstance(str, 10);//创建该类的数组
            Array.set(arr, 5, "this is a test");
            String str5 = (String) Array.get(arr, 5);
            System.out.println(str5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
输出：
this is a test

上例创建了10个单位长度的String数组，为第5个位置的字符串赋了值，最后将这个字符串从数组中取出并打印。

//
package test;

import java.lang.reflect.Array;

public class ReflectionTest {
    public static void main(String[] args) {
        try {
            int dims[] = new int[]{5, 10, 15};
            Object arr = Array.newInstance(Integer.TYPE, dims);//创建一个5*10*15的整型数组
            Object arrObj = Array.get(arr, 3);//第一个Array.get()之后，arrObj是一个10*15的数组
            Class cls = arrObj.getClass().getComponentType();
            System.out.println(cls);
            arrObj = Array.get(arrObj, 5);//再次Array.get()，取得其中一个元素，长度为15的数组
            Array.setInt(arrObj, 10, 37);//使用Array.setInt()为第10个元素赋值

            int arrCast[][][] = (int[][][]) arr;
            System.out.println(arrCast[3][5][10]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

Tips：创建数组时的类型是动态的，在编译时并不知道其类型。

26.4 Java动态代理 P855

Tips：创建数组时的类型是动态的，在编译时并不知道其类型。
//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------

