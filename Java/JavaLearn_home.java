//------------------------------------------------------------------------------------------------
? extends T 
?��ʾĳ���������ͣ������ͼ̳���T����T�����ࡣ��������һ��List<? extends T> list����ʾ����һ����������?��T�����List�����޷��������T��list.add(T)���С�
? super T
?��ʾĳ���������ͣ�����������ΪT����T�Ļ��ࡣ��������һ��List<? super T> list����ʾ����һ����������?��T�����List�����Լ������T��list.add(T)���ԡ�
//------------------------------------------------------------------------------------------------
//Javaֵ���ݣ�������������õĸ������ں������Ѹ�����ָ���µĶ����ϣ�������Ӱ�쵽������ڴ�����
public class Test {
	public static void swap(int a, int b) {
		int temp = a;
		a = b;
		b = temp;
		System.out.println("swap�����a��ֵ��" + a + ";b��ֵ��" + b);
	}

	public static void main(String args[]) {
		int a = 6;
		int b = 9;
		System.out.println("������ʼǰ������a��ֵ��" + a + ";����b��ֵ��" + b);
		swap(a, b);
		System.out.println("���������󣬱���a��ֵ��" + a + ";����b��ֵ��" + b);
	}
}
������£�
������ʼǰ������a��ֵ��6;����b��ֵ��9
swap�����a��ֵ��9;b��ֵ��6
���������󣬱���a��ֵ��6;����b��ֵ��9

//��Ҫ�Ѵ�������ֵ���������
class DataWrap
{
	public int a;
	public int b;
}

public class Test
{
	//�����DataWrap����dw�����Ǻ�����ڶ�������ø�����ֻ�������������ַ��ԭ�����ַһ�£����������Ա�������������޸�ԭ�����Ա����
	public static void swap(DataWrap dw)
	{
		int tmp = dw.a;
		dw.a = dw.b;
		dw.b = tmp;
		System.out.println("swap�����a���Ե�ֵ��" + dw.a + ";b���Ե�ֵ��" + dw.b);
	}

	public static void main(String args[])
	{
		DataWrap dw = new DataWrap();
		dw.a = 6;
		dw.b = 9;
		System.out.println("������ʼǰ��a���Ե�ֵ��" + dw.a + ";b���Ե�ֵ��" + dw.b);
		swap(dw);
		System.out.println("����������a���Ե�ֵ��" + dw.a + ";b���Ե�ֵ��" + dw.b);
	}
}
������£�
������ʼǰ��a���Ե�ֵ��6;b���Ե�ֵ��9
swap�����a���Ե�ֵ��9;b���Ե�ֵ��6
����������a���Ե�ֵ��9;b���Ե�ֵ��6

//���ֶ���ʽҲ�޷�ʵ�֣�Integer�����Ƿ�װ����󣬵���һ�����ɺã���û�а취�޸�ֵ��
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
//Java�����ŵ���ͨ ��2�� P66

public class HelloJava {
	private static String say = "��Ҫѧ����.";
	public static void main(String[] args) {
		System.out.println("��� Java " + say);
	}
}

//һ��*.java�����ֻ����һ����Ϊpublic������û��public
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

��0��ͷ�����ͱ�ʾ�˽�����
��0x��ͷ�����ͱ�ʾʮ��������
������long�������Ҫ��L��l��β
������float�������Ҫ��F��f��β
˫���ȸ�����double�������������D��d��β

//P78
public class HelloJava {
	public static void main(String[] args) {
		char char1 = '\\';
		char char2 = '\u2605';
		System.out.println(char1);
		System.out.println(char2);
	}
}
������£�
\
��


//P80
public class HelloJava {
	private static final double PI = 3.141592653;//�����private���Բ��ӣ����ڻ���̫����ʲô���
	private static int age = 23;//�����private���Բ��ӣ����ڻ���̫����ʲô���
	public static void main(String[] args) {
		final int number;
		number = 1234;//����final����ֻ�ܸ�һ��ֵ������������ʱ��ֵ��Ҳ������ʱ����ֵ
		//number = 22222;//�����ٴθ�ֵ�����뱨��
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
&&��&�����߼��룬||��|�����߼��򣬲�ͬ����ǰ���ж�·���ԣ�����û��

public class HelloJava {
	public static void main(String[] args) {
		System.out.println(5 & -4);
		System.out.println(3 | 6);
		System.out.println(~7);
		System.out.println(10 ^ 3);
		System.out.println(48 << 1);
		System.out.println(48 >> 1);//>>>�޷����������������0���
	}
}
������£�
4
7
-8
9
96
24

//�������� P90
//����㷨˵���˾��ǣ���������A��B���뽻��ֵ��B = A ^ B ^ B��A = A ^ B ^ A
import java.util.Scanner;

public class VariableExchange {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Please input value of variable A:");
		long A = scan.nextLong();
		System.out.println("Please input value of variable B:	");
		long B = scan.nextLong();
		//scan.close();//�������л�����ʾscanû��close
		System.out.println("A = " + A + "\tB = " + B);
		System.out.println("Exchange the two variables...");
		A = A ^ B;//������ôд��A ^= B;
		B = B ^ A;//B = A ^ B;
		A = A ^ B;
		System.out.println("After exchange: A = " + A + "\tB = " + B);
	}
}
������£�
Please input value of variable A:
10
Please input value of variable B:
20
A = 10	B = 20
Exchange the two variables...
After exchange: A = 20	B = 10


//������� P93
public class HelloJava {
	public static void main(String[] args) {
		short s = 516;
		byte b = (byte)s;
		System.out.println(b);
	}
}

boolen���Ͳ������κ�������ת������������д�����벻������C++��ͬ
public class HelloJava {
	public static void main(String[] args) {
		boolean b = true;
		if (0)
		{
			System.out.println(b);
		}
	}
}

//������ж� P96
import java.util.Scanner;

public class HelloJava {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("������һ����ݣ�");
		long year = scan.nextLong();
		scan.close();
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
		{
			System.out.println(year + "�������꣡");
		}
		else
		{
			System.out.println(year + "�겻�����꣡");
		}
	}
}

//P105 switch����е�case���ݣ��������break����᲻�����ж�ֱ��ִ�к���case�е�������ݣ�ֱ������break�����
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

//foreach��䣬int x������for֮ǰ����
public class HelloJava {
	public static void main(String[] args) {
		int arr[] = {0, 1, 2};
		System.out.println("����Ԫ��Ϊ��");
		for (int x : arr) {
			System.out.println(x);
		}
	}
}

//P112 
public class HelloJava {
	public static void main(String[] args) {
		String[] aves = new String[] {"����", "������"};//����дΪ���¼�����ʽString aves[] = new String[] {"����", "������"}��String aves[] = {"����", "������"}��String[] aves = {"����", "������"}
		int index = 0;
		System.out.println("��");
		while (index < aves.length) {
			System.out.print(aves[index++] + " ");
		}
		
		//while����д������forѭ��
		for (String b : aves) {
			System.out.print(b + " ");
		}
	}
}

//˫��forѭ��������ά���� P116
public class HelloJava {
	public static void main(String[] args) {
		int[][] scores = new int[][] {{1, 2, 3}, {4, 5, 6}};//ά�����Բ���ȫƥ��{{1, 3}, {4, 5, 6}}
		for (int[] is : scores) {
			for (int i : is) {
				System.out.println(i);
			}
		}
	}
}


//P130����ת��
public class HelloJava {
	public static void main(String[] args) {
		int arr[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
		System.out.println("ת��ǰ�ľ����ǣ�");
		printArray(arr);//
		
		int arr2[][] = new int[arr.length][arr.length];
		for (int i = 0; i < arr.length; ++i) {
			for (int j = 0; j < arr[i].length; ++j) {
				arr2[j][i] = arr[i][j];
			}
		}
		System.out.println("ת�ú�ľ����ǣ�");
		printArray(arr2);
	}
	
	//�����������static���ᱨ��˵��̬�����������÷Ǿ�̬����
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

Arrays.fill ������� P133
Arrays.sort �������� P134
Arrays.copyOf/Arrays.copyOfRange �������� P135


//�Ա�һά��ά������ռ�ڴ� P137
public class HelloJava {
	public static void main(String[] args) {
		int num1 = 1024 * 1024 * 2;
		int arr1[] = new int[num1];
		for (int i = 0; i < arr1.length; ++i) {
			arr1[i] = i;
		}
		//���ռ���ڴ�������������λת��ΪMB
		long memory1 = Runtime.getRuntime().totalMemory() / 1024 / 1024;
		System.out.println("һά����ռ���ڴ�����Ϊ��" + memory1);
		
		int num2 = 1024 * 1024;
		int arr2[][] = new int[num2][2];
		for (int i = 0; i < arr2.length; ++i) {
			arr2[i][0] = i;
			arr2[i][1] = i;
		}
		//���ռ���ڴ�������������λת��ΪMB
		long memory2 = Runtime.getRuntime().totalMemory() / 1024 / 1024;
		System.out.println("��ά����ռ���ڴ�����Ϊ��" + memory2);
	}
}

P144 ���ַ���������������������ʱ��ͬ��ʹ�á�+�����ӷ�������֮��ķ���ֵ���ַ�����

P146 ��ȡ�ַ�����Ϣ
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

s.trim();���Ե�ǰ�ַ����޸ģ���Ҫ������ֵ��ֵ s = s.trim();

//ȥ���ַ����еĿո� P148
import java.util.StringTokenizer;

public class HelloJava {
	public static void main(String[] args) {
		String text = "   We are students     ";
		System.out.println("ԭ�ַ����ǣ�\n" + text);
		StringTokenizer st = new StringTokenizer(text, " ");//���԰ѿո񻻳������ַ������绻��"tsn"����ȥ�����е�'t','s','n'�ַ�
		StringBuffer sb = new StringBuffer();
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
		}
		System.out.println("ȥ���ַ��������пո�֮����ַ����ǣ�\n" + sb.toString());
		String s = text.replaceAll("ts", "");
		System.out.println(s);
	}
}
������£�
ԭ�ַ����ǣ�
   We are students     
ȥ���ַ��������пո�֮����ַ����ǣ�
   We are ude     
   We are studen     
   
s.replaceAll(" ", "");

//P151 �Ƚ��������==���Ƚϵ����ڴ�λ�ã������������Ƚ��ַ����������͵ȿ����ñȽ�������Ƚϣ�equals()�����Ƚϵ������ַ������ݱ�����ȫһ����equalsIgnoreCase()�����ں��Դ�Сд����������ݱ���һ����

public class HelloJava {
	public static void main(String[] args) {
		String text = "   We are students     ";
		System.out.println(text.toUpperCase());
	}
}

//split�ָ��ַ���
public class HelloJava {
	public static void main(String[] args) {
		String s = new String("abc,def,ghi,gkl");
		String news[] = s.split(",");//String news[] = s.split(",|g") ��|��ʾ,��g���Ƿָ�����������,Ҳ�ָ�������gҲ�ָ�
		for (String str : news) {
			System.out.println(str);
		}
	}
}

//�ж��ַ����Ƿ������� P155
public class CheckNumber {
	public static void main(String[] args) {
		String s = "123456a";
		if (CheckNumber.isNumber(s)) {//������Բ���ָ��������ֱ��ʹ��isNumber
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

//���ڸ�ʽ�� P158
import java.util.Date;
import java.util.Locale;

public class HelloJava {
	public static void main(String[] args) {
		Date today = new Date();
		String a = String.format(Locale.US, "%tb", today);//���Խ�US�ĳ�CHINESE/CHINA������ʡ�ԣ��õ����ĵ���ʾ
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
������£�
Apr
April
������
������
2015
15
04
02
2
//ʱ���ʽ�� P158 ����ʱ����ϸ�ʽ�� P159 �������͸�ʽ�� P160
//����ʽ���ɴ�д P161
//���ָ�ʽ���ɻ��Ҹ�ʽ P163

//������ʽ P165
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
//ͨ���.ǰ����\\��ƥ�䵥���ض��ַ�����ֱ�ӽ�����ַ�д�ϣ���@
public class HelloJava {
	public static void main(String[] args) {
		String s1 = "a@aa";
		
		String regexStr = "\\w{0,}@.{1}\\w{1,}";//"\\w{0,}@.{1}\\w{1,}3$"����Ҫ����3Ϊ��β
		
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
		String regex = "1[3,5,8]\\d{9}$";//[3,5,8]��Ķ��ſ���ʡ��Ϊ[358]������$���ַ�������ʲôΪ��β��ƥ���
		if (handset.matches(regex)) {
			return true;
		}
		else {
			return false;
		}
	}
}

//P168 ������ʽ��֤IP��ַ�Ϸ���
//P168 ͳ�ƺ��ָ���
import java.util.regex.Pattern;
public class HelloJava {
	public static void main(String[] args) {
		String text = "���տƼ� soft";
		int amount = 0;
		for (int i = 0; i < text.length(); ++i) {
			boolean matches = Pattern.matches("^[\u4E00-\u9FA5]{0,}$", "" + text.charAt(i));//�ڶ���������String������Ҫ���ַ������ַ���ӵõ��ַ�����Σ���һ�������о�����Ҳ����"[\u4E00-\u9FA5]"
			if (matches) {
				++amount;
			}
		}
		System.out.println(text + " ���ָ��� = " + amount);
	}
}

//StringBuilder�� P170 �о��е���C++���stringstream��
public class HelloJava {
	public static void main(String[] args) {
		String s1 = "int";
		String s2 = "ser";
		StringBuilder builder = new StringBuilder(s1);
		builder.insert(2, s2);//builder.insert(builder.length(), s2)����builder.append(s2)
		System.out.println(builder);
	}
}

//Java�����������ӡ���ַ���������ַ�����������C++һ���ӽ�����'\0'
public class HelloJava {
	public static void main(String[] args) {
		char arr[] = new char[] {'a', 'b'};
		System.out.println(arr);
	}
}

//null����ַ����Ĳ��
String str1=null;
String str2="";

System.out.print(str1.length());//��ָ���쳣
System.out.print(str2.length());//���쳣

��˼����
null �Ǹ�����û���ڴ�ռ�
"" ���ڴ�ռ� ֵΪ��


//�ַ������� P174
public class HelloJava {
	public static void main(String[] args) {
		String value = "�Ұ� Java";
		char secret = '��';
		System.out.println("ԭ�ַ���Ϊ��" + value);
		String encryt = EAndU(value, secret);
		System.out.println("���ܺ��ֵ��" + encryt);
		String uncrypt = EAndU(encryt, secret);
		System.out.println("���ܺ��ֵ��" + uncrypt);
	}
	
	private static String EAndU(String value, char secret) {
		byte bt[] = value.getBytes();
		for (int i = 0; i < bt.length; ++i) {
			bt[i] = (byte)(bt[i] ^ (int)secret);
		}
		return new String(bt, 0, bt.length);
	}
}

//P184 ֻ�������޲ι��췽���еĵ�һ��ʹ��this�ؼ��ֵ����вι��췽��
public class AnyThing {
	public AnyThing() {
		//String a;
		this("this �����вι��췽��");
		System.out.println("�޲ι��췽��");
	}
	
	public AnyThing(String name) {
		System.out.println("�вι��췽��" + name);
	}
}

//P190
��̬�����в�����ʹ��this�ؼ��֡�
��̬�����в�����ֱ�ӵ��÷Ǿ�̬������
Java�в��ܽ��������ڵľֲ���������Ϊstatic��
�����ִ����ʱ��ϣ����ִ����ĳ�ʼ������������ʹ��static����һ����̬����
����δ��뱻ִ��ʱ������ִ��static���еĳ��򣬲���ֻ��ִ��һ�Ρ�
public class AnyThing {
	static {
		System.out.println("static area");
	}
	public AnyThing() {
		this("this �����вι��췽��");
		System.out.println("�޲ι��췽��");
	}
	
	public AnyThing(String name) {
		System.out.println("�вι��췽��" + name);
	}
}

public class HelloJava {
	public static void main(String[] args) {
		AnyThing a1 = new AnyThing();
		AnyThing a2 = new AnyThing();
	}
}
������£�
static area
�вι��췽��this �����вι��췽��
�޲ι��췽��
�вι��췽��this �����вι��췽��
�޲ι��췽��

//P190 Ȩ�����η�
��������ʱ��ʹ��public/protected/private���η��������Ȩ�ޣ��������Ԥ��Ϊ����ȡ��Χ����ֻ��һ�����е�����Ե��������ĳ�Ա�������Ա������
���Ȩ���趨��Լ�����Ա�ϵ�Ȩ���趨��

//���Զ���main������������������� P193
public class HelloJava {
	public static void main(String[] args) {
		HelloJava hj = new HelloJava();
		System.out.println(hj.plus(1, 2));
	}
	
	public double plus(double d1, double d2) {
		return d1 + d2;
	}
}

//����plus����Ϊstatic�󣬻�����ʾ��̬����Ӧ��ͨ����̬��ʽ���ʣ�����hj.plusӦ���޸�ΪHelloJava.plus
public class HelloJava {
	public static void main(String[] args) {
		HelloJava hj = new HelloJava();//HelloJava hj = null;����hjΪnull,��һ�����������������õģ���Ϊ��̬�����������ڶ�����ȥ��static������뱨��
		System.out.println(hj.plus(1, 2));//System.out.println(HelloJava.plus(1, 2));
	}
	
	public static double plus(double d1, double d2) {
		return d1 + d2;
	}
}

P199 ����������ֻ�ܻ�����Щ��new�����������Ķ������������ͨ��new���������ڴ��л�ȡһ���ڴ��������ֶ�����ܲ����������ջ�����ʶ��������Java�������ṩ��һ��finalize()��������Object��ķ�������������Ϊprotected���û��������Լ������ж����������������û������ж�����finalize()����������������ʱ���ȵ��ø÷�������������һ���������ն�������ʱ�������������ն���ռ�õ��ڴ档
�����������ղ�����Ϊ���ƣ�����ִ��ʱ��Ҳ��ȷ��������finalize()����Ҳ���޷�ִ�У�Ϊ�ˣ�Java�ṩ��System.gc()����ǿ��������������������֪����������������

//P200 ͳ��ͼ������
import java.util.Random;

public class HelloJava {
	public static void main(String[] args) {
		String titles[] = {"Java1", "Java2", "Java3"};
		for (int i = 0; i < 5; ++i) {
			new Book(titles[new Random().nextInt(3)]);//�����new Random().nextInt(3)�ᱻʶ��Ϊ(new Random()).nextInt(3)����new����һ��Random���󣬲��������Ա����nextInt��
		}
		System.out.println("�ܼ����飺" + Book.getCounter());
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

//Java���Ĭ�϶����equals�����̳���Object����==һ����ֻ�е��������������õ��ڴ�һ��ʱ���ŷ���true��String��==����Ĭ�ϱȽ��ڴ��Ƿ�һ�£�equals�����ع��ıȽ��ַ��������Ƿ�һ��
public class HelloJava {
	public static void main(String[] args) {
		Book b1 = new Book("abc");
		Book b2 = new Book("abc");
		Book b3 = b1;
		System.out.println(b1 == b2);//System.out.println(b1.equals(b2));
		System.out.println(b1 == b3);//System.out.println(b1.equals(b3));
	}
}
������£�
Sold book: abc
Sold book: abc
false
true

//���¼������Ĺ�ϣ�� P200
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
		this.age = age;//age = age������ֵ����û��Ч��������������������ȥ��this
		this.weight = weight;
		this.color = color;
	}

	//@Override���߱���������ĺ��������أ����ʶд��Equals������б�������������@Override�����㺯��д���ˣ�������Ҳֻ����Ϊ��������һ������
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
		//����ļ������ֿ������ӣ�ֻ��Ϊ�����ֲ�ͬ����Ĺ�ϣ��
		//Integer��Double��Щԭʼ��������Ҫת����ԭ�������Ͳ���hashCode��������Ҳ����Щ�������Ϳ���ֱ����==�Ƚϵ�ԭ����
		return 7 * name.hashCode() + 11 * new Integer(age).hashCode()
				+ 13 * new Double(weight).hashCode() + 17 * color.hashCode();
	}
	
	public void show() {
		System.out.println(name + age + weight + color);
	}
}

//P201 ��ŵ��
//P202 ����ģʽ
//Java��û��static�ֲ����������Բ�����C++һ������getInstance������һ��static�ĵ�������
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
	
	public static Emperor getInstance() {//ֻ��ʹ��ʱ�Ŵ���
		if (null == emperor) {
			emperor = new Emperor();
		}
		return emperor;
	}
	
	public void getName() {
		System.out.println("���ǻʵۣ����տƼ�");
	}
}

//�ɲο�Head First�������ģ��ڳ����ʼ���ͽ�ʵ�����á���C++�������ѡ�private static Singleton uniqueInstance = new Singleton();��
public class Emperor {
	private static Emperor emperor = new Emperor();
	private Emperor() {
	}
	
	public static Emperor getInstance() {
		return emperor;
	}
	
	public void getName() {
		System.out.println("���ǻʵۣ����տƼ�");
	}
}


//P209 �ӿ�
public class GifSaver implements ImageSaver {
	@Override
	public void save() {//�����implements ImageSaver�������@Override���ṩ����
		System.out.println("save as GIF");
	}
	
	public static void main(String[] args) {
		GifSaver gs = new GifSaver();
		gs.save();
	}
}

public interface ImageSaver {
	void save();//ֻ����public�򲻼Ӽ�public������Ϊprivate
}

//�ӿںͼ̳е�ʹ�� P208
import java.awt.Point;

public class Image {//����������final������ཫ�޷����̳�
	private String name;//�������η�����Ĭ��Ϊpublic
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

public class GifSaver extends Image implements ImageSaver {//����ж���ӿ�Ҫʵ�֣�����,�ָ�����ӿ�
	private static String extendsName = ".gif";
	
	public GifSaver(String name, Point size) {
		super(name, size);//������ø���Ĺ��캯��
	}
	
	@Override
	public void save() {
		System.out.println(getName() + " is saved as a GIF.");
	}
	
	@Override
	public String getName() {//�����Ϊprivate������ʾ���ܼ��ٻ���Ŀɼ��ԡ��ɽ���������η���Ϊprotected�������Ϊpublic
		return super.getName() + extendsName;
	}
	
	public static void main(String[] args) {
		GifSaver gs = new GifSaver("hello", new Point(3, 4));//�ɸ�Ϊ��Image gs = new GifSaver("hello", new Point(3, 4));����gs.save()���޷�����
		gs.save();
		System.out.println(gs.getName());
		
		Image i = new Image("abc", new Point(4, 5));//�����Image�����������abstract����������࣬��ô���ｫ����ʵ����
		//i.save();//save�ǽӿ��ṩ�ĺ���������û�д˺���
		System.out.println(i.getName());
	}
}
������£�
hello.gif is saved as a GIF.
hello.gif
abc

//P218 �ᵽ�������
�ڻ����� return this.getClass().getSimpleName();
�е�����C++�е�typeid(someClass).name()

//P221 ʹ��Comparable�ӿ��Զ�������
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
		
		List<Employee> employeeList = new ArrayList<Employee>();//���������͵�List��Ҳ���Դ��������͵�LinkedList<>
		employeeList.add(e1);
		employeeList.add(e2);
		employeeList.add(e3);
		
		System.out.println("Before sort: ");//System.out.println(employeeList);����ֱ�Ӵ�ӡ����List
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
������£�
Before sort: 
Id: 3,name: Li Lei,age: 26
Id: 2,name: Han Meimei,age: 25
Id: 1,name: Kily,age: 27
After sort: 
Id: 1,name: Kily,age: 27
Id: 2,name: Han Meimei,age: 25
Id: 3,name: Li Lei,age: 26

//��̬�������˽���� P222
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
������£�
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

//���������������ת�� P223 ��ϰ2
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
		
		Student s = (Student)hss;//����ת��ֱ����()����
		System.out.println(s.getName());
		//s.highSchoolSpecification();//����ת���ɻ����û�����ຯ����
		
		hss = (HighSchoolStudent)s;
		hss.highSchoolSpecification();
	}
}

P225 ���󷽷�����ʹ��private��static�ؼ��ֽ������Σ�����һ���������󷽷�������뱻����Ϊ�����ࡣ

//P229 �ڲ���
public class OuterClass {
	innerClass in = new innerClass(); // ���ⲿ��ʵ�����ڲ����������
	
	public void ouf() {
		in.inf(); // ���ⲿ�෽���е����ڲ��෽��
	}
	
	class innerClass {//�����private��û��ʲô����
		innerClass() { // �ڲ��๹�췽��
		}
		
		public void inf() { // �ڲ����Ա����
		}
		
		int y = 0; // �����ڲ����Ա����
	}
	
	//����д�����벻�����ڲ�����������ⲿ��Ĵ��ڣ��������þ�̬�����������ڲ���
//	public static innerClass getInnerClass() {
//		return new innerClass();
//	}

	public innerClass doit() { // �ⲿ�෽��������ֵΪ�ڲ�������
		// y=4; //�ⲿ�಻����ֱ�ӷ����ڲ����Ա����
		in.y = 4;
		return new innerClass(); // �����ڲ�������
	}
	
	public static void main(String args[]) {
		OuterClass out = new OuterClass();
		// �ڲ���Ķ���ʵ���������������ⲿ����ⲿ���еķǾ�̬������ʵ��
		OuterClass.innerClass in = out.doit();
		System.out.println(in.y);
		System.out.println(out.in.y);
		OuterClass.innerClass in2 = out.new innerClass();//ʵ�����ڲ���ʱ��Ҫ���ⲿ�������ʵ������new out.innerClass()����д������
	}
}

//P229 this�ؼ��ֻ�ȡ�ڲ������ⲿ�������
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
			x++; // ���õ����β�x
			this.x++; // �����ڲ���ı���x
			TheSameName.this.x++; // �����ⲿ��ı���x
		}
	}
	
	public static void main(String args[]) {
		TheSameName tsn = new TheSameName();
		tsn.doit();
	}
}

//���ַ����ĸ�ֵ���൱�ڶ��βε����øı䣬���Ե��ô������û��Ӱ��
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
������£�
aaa

http://www.cnblogs.com/dolphin0520/p/3736238.html
���ֻ��������ȷ��ֹ �÷����������б����ǵ�����²Ž���������Ϊfinal�ġ�
ע�����private��������ʽ�ر�ָ��Ϊfinal������
����һ��final����������ǻ����������͵ı�����������ֵһ���ڳ�ʼ��֮��㲻�ܸ��ģ�������������͵ı��������ڶ����ʼ��֮��㲻��������ָ����һ�����󡣣�����C++�е����ã�

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
��ҿ�������һ����������������Ϊʲô��һ���ȽϽ��Ϊtrue�����ڶ����ȽϽ��Ϊfasle�����������final��������ͨ�����������ˣ���final�����ǻ������������Լ�String����ʱ������ڱ����ڼ���֪������ȷ��ֵ�����������������������ڳ���ʹ�á�Ҳ����˵���õ���final�����ĵط����൱��ֱ�ӷ��ʵ��������������Ҫ������ʱȷ�������ֺ�C�����еĺ��滻�е�������������һ�δ����У����ڱ���b��final���Σ���˻ᱻ����������������������ʹ�õ�b�ĵط���ֱ�ӽ�����b �滻Ϊ����  ֵ�������ڱ���d�ķ���ȴ��Ҫ������ʱͨ�����������С�������е�������Ӧ�������ˣ�����Ҫע�⣬ֻ���ڱ����ڼ���ȷ��֪��final����ֵ������£��������Ż�����������Ż��������������δ���Ͳ�������Ż���
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

������δ���������˾�����final����֮�󣬾Ͳ����ڷ����и��ı���i��ֵ�ˡ��ⲻ֪������changeValue��main�����еı���i�����Ͳ���һ����������Ϊjava�������ݲ��õ���ֵ���ݣ����ڻ������͵ı������൱��ֱ�ӽ����������˿��������Լ�ʹû��final���ε�����£��ڷ����ڲ��ı��˱���i��ֵҲ����Ӱ�췽�����i��
������δ���ͻᷢ��������Ϊ helloworld������Ȼ����final�������β�û����ֹ��changeValue�иı�bufferָ��Ķ�������ݡ�����˵�����finalȥ���ˣ���һ��changeValue����bufferָ��������������ô�졣�������뷨�����ѿ����Լ�����д������һ�������Ľ����ʲô�������finalȥ���ˣ�Ȼ����changeValue����bufferָ������������Ҳ����Ӱ�쵽main�����е�buffer��ԭ������java���õ���ֵ���ݣ��������ñ��������ݵ������õ�ֵ��Ҳ����˵��ʵ�κ��β�ͬʱָ����ͬһ������������β�����ָ����һ�������ʵ�β�û���κ�Ӱ�졣


P230 �ֲ��ڲ���
P231 �����ڲ���
��ͼ�λ���̵��¼�����������У������ʹ�������ڲ��࣬�����ɴ��򻯴��룬����ǿ����Ŀɶ��ԡ�

public interface IStringDeal {
	public String filterBlankChar();
}

public class Test {
	public static void main(String args[]) {
		final String sourceStr = "abc de f";//���ﲻ��finalҲûʲô���⣬�������������в����޸�sourceStr��ֵ��2.Ϊʲô�ֲ��ڲ���������ڲ���ֻ�ܷ��ʾֲ�final������
		IStringDeal s = new IStringDeal() {
			@Override
			public String filterBlankChar() {
				String convertStr = sourceStr;//����ֱ����sourceStr.replace(" ", "")Ҳûʲô���⣿
				convertStr = convertStr.replace(" ", "");
				return convertStr;
			}
		};
		
		System.out.println("Դ�ַ�����" + sourceStr);
		System.out.println("ת������ַ�����" + s.filterBlankChar());
	}
}
������£�
Դ�ַ�����abc de f
ת������ַ�����abcdef

//�������ʹ�������࣬����Ҫ�޸�Ϊ���¾ֲ��ڲ��෽ʽ������д����Ȼ�ܴﵽһ����Ч�������Ǽ��߳�������ά��������һ��ʹ�������ڲ���ķ�������д�¼��������롣ͬ���ģ������ڲ���Ҳ�ǲ����з������η���static���η��ġ�
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
		
		System.out.println("Դ�ַ�����" + sourceStr);
		System.out.println("ת������ַ�����" + s.filterBlankChar());
	}
}

http://www.cnblogs.com/dolphin0520/p/3811445.html
��������Ҫ����ť���ü���������ʹ�������ڲ����ܹ���ʵ�ָ�����߽ӿ��еķ��������ͬʱ����һ����Ӧ�Ķ��󣬵���ǰ�������������߽ӿڱ����ȴ��ڲ�������ʹ�á���Ȼ����������д��Ҳ�ǿ��Եģ�������ʹ�������ڲ���ﵽЧ����ͬ��
�����ڲ�����Ψһһ��û�й��������ࡣ����Ϊ��û�й����������������ڲ����ʹ�÷�Χ�ǳ����ޣ��󲿷������ڲ������ڽӿڻص��������ڲ����ڱ����ʱ����ϵͳ�Զ�����ΪOutter$1.class��һ����˵�������ڲ������ڼ̳����������ʵ�ֽӿڣ�������Ҫ���Ӷ���ķ�����ֻ�ǶԼ̳з�����ʵ�ֻ�����д��

��󲹳�һ��֪ʶ�����ڳ�Ա�ڲ���ļ̳����⡣һ����˵���ڲ����Ǻ���������Ϊ�̳��õġ����ǵ������̳еĻ���Ҫע�����㣺
����1����Ա�ڲ�������÷�ʽ����Ϊ Outter.Inner.
����2���������б�����ָ���ⲿ���������ã���ͨ��������õ���super()����δ���ժ�ԡ�Java���˼�롷
//�ڲ���ļ̳� P233
class WithInner {
    class Inner{
         
    }
}
class InheritInner extends WithInner.Inner {
    // InheritInner() �ǲ���ͨ������ģ�һ��Ҫ�����β�
    InheritInner(WithInner wi) {
        wi.super(); //������������
    }
  
    public static void main(String[] args) {
        WithInner wi = new WithInner();
        InheritInner obj = new InheritInner(wi);
    }
}

//P233 ���벻��
����: ���� Test ���Ҳ��� main ����, �뽫 main ��������Ϊ:
   public static void main(String[] args)
���� JavaFX Ӧ�ó����������չjavafx.application.Application
public class Test {
	static int x = 100;
	
	static class Inner {
		static void doitinner() {
			System.out.println("�ⲿ��ĳ�Ա������" + x);
		}
		
		public static void maint(String args[]) {
			doitinner();
		}
	}
}

//P233 �ֲ��ڲ����������ӣ�û����������
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


//P234 ��̬�ڲ�����ֵ

public class MaxMin {
	public static class Result {//�����Ϊprivate��Test�в��ɼ������뱨��
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
		double max = Double.MIN_VALUE;//������Сֵ��max
		double min = Double.MAX_VALUE;//�������ֵ��max
		
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
		System.out.println("Դ���飺");
		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i]);
		}
		
		MaxMin.Result result= MaxMin.getResult(array);
		System.out.println("���ֵ��" + result.getMax());
		System.out.println("��Сֵ��" + result.getMin());
	}
}
������£�
Դ���飺
20.280779188500432
4.834134188174344
50.30131219984979
52.04190222448042
39.703315549452455
���ֵ��52.04190222448042
��Сֵ��4.834134188174344

//P235 Class����Java����

//P238 ���ʹ��췽��
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
			System.out.println("�鿴�Ƿ�������пɱ������Ĳ�����" + constructor.isVarArgs());
			
			System.out.println("�ù��췽������ڲ�����������Ϊ��");
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			for (int j = 0; j < parameterTypes.length; j++) {
				System.out.println(" " + parameterTypes[j]);
			}
			
			System.out.println("�ù��췽�������׳����쳣����Ϊ��");
			Class<?>[] exceptionTypes = constructor.getExceptionTypes();
			for (int j = 0; j < exceptionTypes.length; j++) {
				System.out.println(" " + exceptionTypes[j]);
			}
			
			MoreConstructor example2 = null;
			while (example2 == null) {
				try {
					//�����е�����Ļ���i=0��ʱ��һ���ǵ�һ������˳���еĵ�һ�����캯�������¹���ʧ�ܣ���ѭ��
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
					System.out.println("�ڴ�������ʱ�׳��쳣������ִ��setAccessible()����");
					constructor.setAccessible(true);
				}
			}
			example2.print();
			System.out.println();
		}
	}
}

//P240���ʳ�Ա����
//P242���ʷ���
����һ������Ϊprint����ڲ�����������ΪString��int�͵ķ���������ͨ���������ַ�ʽʵ�֣�
objectClass.getDeclaredMethod("print", String.class, int.class);
objectClass.getDeclaredMethod("print", new Class[] {String.class, int.class});

//�ַ���ת��int��������(int)s����ת��
System.out.println(Integer.valueOf("100"));

//P244 ���÷���鿴��ĳ�Ա
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;


public class ClassDeclarationViewer {
	public static void main(String args[]) throws ClassNotFoundException {
		Class<?> clazz = Class.forName("java.util.ArrayList");
		System.out.println("��ı�׼���ƣ�" + clazz.getCanonicalName());
		System.out.println("������η���" + Modifier.toString(clazz.getModifiers()));
		
		//���Ͳ���
		TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
		System.out.print("��ķ��Ͳ�����");
		if (typeVariables.length != 0) {
			for (TypeVariable<?> typeVariable : typeVariables) {
				System.out.println(typeVariable + "\t");
			}
		}
		else {
			System.out.println("��");
		}
		
		Type[] interfaces = clazz.getGenericInterfaces();
		System.out.println("����ʵ�ֵĽӿڣ�");
		if (interfaces.length != 0) {
			for (Type type : interfaces) {
				System.out.println("\t" + type);
			}
		}
		else {
			System.out.println("\t��");
		}
		
		Type superClass = clazz.getGenericSuperclass();
		System.out.print("���ֱ�Ӽ̳��ࣺ");
		if (superClass != null) {
			System.out.println(superClass);
		}
		else {
			System.out.println("��");
		}
		
		Annotation[] annotations = clazz.getAnnotations();
		System.out.print("���ע�ͣ�");
		if (annotations.length != 0) {
			for (Annotation annotation : annotations) {
				System.out.println("\t" + annotation);
			}
		}
		else {
			System.out.println("��");
		}
	}
}
������£�
��ı�׼���ƣ�java.util.ArrayList
������η���public
��ķ��Ͳ�����E	
����ʵ�ֵĽӿڣ�
	java.util.List<E>
	interface java.util.RandomAccess
	interface java.lang.Cloneable
	interface java.io.Serializable
���ֱ�Ӽ̳��ࣺjava.util.AbstractList<E>
���ע�ͣ���

//P246 ��̬�������з���
import java.lang.reflect.Method;

public class MethodTest {
	public static void main(String args[]) {
		try {
			System.out.println("����Math��ľ�̬����sin()");
			Method sin = Math.class.getDeclaredMethod("sin", Double.TYPE);//double.classһ��
			double sin1 = (double) sin.invoke(null, new Integer(1));//double sin1 = (double) sin.invoke(null, 1);һ��
			System.out.println("1������ֵ�ǣ�" + sin1);
			
			System.out.println("����String��ķǾ�̬����equals()");
			Method equals = String.class.getDeclaredMethod("equals", Object.class);
			boolean flag = (boolean) equals.invoke(new String("abc"), "abc");//boolean flag = (boolean) equals.invoke("abc", "abc");һ��
			System.out.println("�ַ����Ƿ���ȣ�" + flag);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

//P246 ���÷�����дtoString()������Object���toString()����Ĭ������������͹�ϣ��
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
������£�
MethodTest@1db9742

//��������
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class StringUtils {
	@SuppressWarnings("unchecked")//ȡ�� unchecked ����
	public String toString(Object object) {
		Class<?> clazz = object.getClass();
		StringBuilder sb = new StringBuilder();
		Package packageName = clazz.getPackage();
		sb.append("������" + packageName.getName() + "\t");
		
		String className = clazz.getSimpleName();
		sb.append("������" + className + "\n");
		
		//���������ָо������÷��ͺ�����д��Ŀǰ������-.-��
		sb.append("�������췽����\n");
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			String modifier = Modifier.toString(constructor.getModifiers());
			if (modifier.contains("public")) {
				sb.append(constructor.toGenericString() + "\n");
			}
		}
		
		sb.append("������ ��\n");
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String modifier = Modifier.toString(field.getModifiers());
			if (modifier.contains("public")) {
				sb.append(field.toGenericString() + "\n");
			}
		}
		
		sb.append("����������\n");
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
������£�
������java.util	������Date
�������췽����
public java.util.Date(java.lang.String)
public java.util.Date(int,int,int,int,int,int)
public java.util.Date(int,int,int,int,int)
public java.util.Date()
public java.util.Date(long)
public java.util.Date(int,int,int)
������ ��
����������
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

//Set���� P253
����������ظ�ֵ������ʹ��Set�����е�addAll()��������Collection������ӵ�Set�����в������ظ�ֵ��
//��������
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
		
		Set<String> set = new TreeSet<String>();//TreeSet�Դ�Ĭ�������ܣ�HashSet�Դ�HashCode������
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

//P259 List�ӿڵ�ʵ����
ArrayList�����������ҿ죬����ɾ����
LinkedList��������������������ɾ����
import java.util.ArrayList;

public class Test {
	public static void main(String args[]) {
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		
		int i = (int) (Math.random() * (list.size()));
		System.out.println("�����ȡ�����е�Ԫ�أ�" + list.get(i));
		list.remove(2);
		System.out.println("����2��Ԫ���Ƴ�������Ϊ��" + list);
	}
}

//P260 Set�ӿڵ�ʵ����
HashSet/TreeSet��Set�еĶ���������ġ�

//���Employeeδʵ��Comparable�Ľӿڣ���ֻ�ܲ��뵽HashSet�У����뵽TreeSet�н�����
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Test {
	public static void main(String args[]) {
		Employee e1 = new Employee(3, "Li Lei", 26);
		Employee e2 = new Employee(2, "Han Meimei", 25);
		Employee e3 = new Employee(1, "Kily", 27);
		
		Set<Employee> employeeList = new HashSet<Employee>();//Employeeʵ����Comparable�ӿں󣬾Ϳ�����Set<Employee> employeeList = new TreeSet<Employee>();�����ˣ�������µ�2��������������ź����
		employeeList.add(e1);
		employeeList.add(e2);
		employeeList.add(e3);
		
		System.out.println("The order in set:");
		for (Employee e : employeeList) {
			System.out.println(e);
		}
	}
}
������£�
The order in set:
Id: 1,name: Kily,age: 27
Id: 3,name: Li Lei,age: 26
Id: 2,name: Han Meimei,age: 25

����TreeSet������£�
The order in set:
Id: 1,name: Kily,age: 27
Id: 2,name: Han Meimei,age: 25
Id: 3,name: Li Lei,age: 26

//P261 Map�ӿڵ�ʵ����
Map�ӿڳ��õ�ʵ������HashMap��TreeMap��ͨ��������HashMapʵ����ʵ��Map���ϣ���Ϊ��HashMap��ʵ�ֵ�Map���϶�����Ӻ�ɾ��ӳ���ϵЧ�ʸ��á�HashMap�ǻ��ڹ�ϣ���Map�ӿڵ�ʵ�֣�HashMapͨ����ϣ������ڲ���ӳ���ϵ���п��ٲ��ң���HashMap��ʵ�ֵ�Map���϶�����ӻ�ɾ��ӳ���ϵЧ�ʽϸߣ���TreeMap�е�ӳ���ϵ����һ����˳�����ϣ��Map�����еĶ������һ����˳��Ӧ��ʹ��TreeMap��ʵ��Map���ϡ�
1. HashMap��
���ڹ�ϣ���Map�ӿ�ʵ�֡�����ʹ��nullֵ��null���������뱣֤����Ψһ�ԡ�
2. TreeMap��
����ʵ����Map�ӿڣ���ʵ����java.util.SortedMap�ӿڣ���˼����е�ӳ���ϵ����һ����˳�򡣵�����ӡ�ɾ���Ͷ�λӳ���ϵ�ϣ�TreeMap���HashMap������ܲ�һЩ������TreeMap��ʵ�ֵ�Map�����е�ӳ���ϵ�Ǹ��ݼ�������һ����˳�����еģ���˲������������null��
//�������HashMap��TreeMap˳��һ����
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
		System.out.println("HashMap��ʵ�ֵ�Map���ϣ�����");
		while (it.hasNext()) {
			String str = (String) it.next();//if not using String convertion����������ת��Ҳû������
			String name = (String) map.get(str);
			System.out.println(str + " " + name);
		}
		
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.putAll(map);
		Iterator<String> iter = treeMap.keySet().iterator();
		System.out.println("TreeMap��ʵ�ֵ�Map���ϣ�����������");
		while (iter.hasNext()) {
			String str = (String) iter.next();
			String name = (String) map.get(str);
			System.out.println(str + " " + name);
		}
	}
}
������£�
HashMap��ʵ�ֵ�Map���ϣ�����
001 Zhang San
004 Wang Yi
005 Li Si
TreeMap��ʵ�ֵ�Map���ϣ�����������
001 Zhang San
004 Wang Yi
005 Li Si

//���԰����е�Keyֵ����Integer������
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
		System.out.println("HashMap��ʵ�ֵ�Map���ϣ�����");
		while (it.hasNext()) {
			int str = it.next();//if not using String convertion
			String name = map.get(str);
			System.out.println(str + " " + name);
		}
		
		TreeMap<Integer, String> treeMap = new TreeMap<Integer, String>();
		treeMap.putAll(map);
		Iterator<Integer> iter = treeMap.keySet().iterator();
		System.out.println("TreeMap��ʵ�ֵ�Map���ϣ�����������");
		while (iter.hasNext()) {//System.out.println(treeMap);����ֱ��������ӡ
			int str = iter.next();
			String name = map.get(str);
			System.out.println(str + " " + name);
		}
	}
}

//P265 ������
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

public class Test {
	public static void main(String args[]) {
		ArrayList<Integer> array = new ArrayList<>();
		Collections.addAll(array, 1, 2, 3, 4, 5, 6);
		System.out.println("�����е�Ԫ�أ�" + array);
		ListIterator<Integer> iterator = array.listIterator();//�����������õ�ʱ����ָ��λ��0��Ԫ�ص�
		
		//iterator.set(10);��ʱ���������᷵�ع��κ�ֵ���޷�set
		
		//���е�next�Ĳ���������ָ��ǰ��������ָ���Ԫ�أ�������next()������nextIndex()
		//���е�previous�Ĳ���������ָ��������ǰһ��Ԫ�ص�
		
		System.out.println(iterator.next());
		iterator.add(-1);
		System.out.println("�����е�Ԫ�أ�" + array);
		
		boolean hasNext = iterator.hasNext();
		System.out.println("�����Ƿ������һ��Ԫ�أ�" + hasNext);
		
		boolean hasPrevious = iterator.hasPrevious();
		System.out.println("�����Ƿ����ǰһ��Ԫ�أ�" + hasPrevious);
		
		int next = iterator.next();
		System.out.println("��ü��ϵ���һ��Ԫ�أ�" + next);
		
		//iterator.next();
		
		int nextIndex = iterator.nextIndex();
		System.out.println("��ü��ϵ���һ��Ԫ�ص�������" + nextIndex);
		System.out.println("���Ԫ�ص���һ��Ԫ�ص�������" + iterator.previousIndex());
		
		
		int previous = iterator.previous();//���ص�ǰ���������һ��next()�õ���Ԫ��ֵ��Ҳ������Ϊ�ǵ�ǰָ���Ԫ��ֵ�������ҽ�����������һ��Ԫ��
		System.out.println("��ü��ϵ�ǰһ��Ԫ�أ�" + previous);
		
		int previousIndex = iterator.previousIndex();//���ص�ǰ��������ָ���Ԫ������
		System.out.println("��ü��ϵ�ǰһ��Ԫ�ص�������" + previousIndex);
		
		iterator.add(7);//�ڵ�������ǰ��ָ���Ԫ��λ�� ����һ��Ԫ�أ����һ�ʹ������ǰ��1
		System.out.println("�򼯺�������Ԫ��7��ļ��ϣ�" + array);
		
		System.out.println("����Ԫ��7���next()��" + iterator.next());
		iterator.set(12);//�޸ĵ��������һ�η��ص�Ԫ��ֵ
		System.out.println("����õ���һ��Ԫ���޸ĳ�12��ļ��ϣ�" + array);
		
		iterator.remove();//ɾ�����ǵ��������һ�η��ص�Ԫ��ֵ�����һ�ʹ������index��1
		System.out.println("����õ���һ��Ԫ��ɾ����ļ��ϣ�" + array);
		
		System.out.println("remove()���next()ֵ��" + iterator.next());
	}
}

//P267 ����������List�е�Ԫ��
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

public class Test {
	public static void main(String args[]) {
		ArrayList<Integer> array = new ArrayList<>();
		Collections.addAll(array, 1, 2, 3, 4, 5, 6);
		System.out.println("�����е�Ԫ�أ�" + array);
		ListIterator<Integer> iterator = array.listIterator();
		
		for (; iterator.hasNext();) {
			iterator.next();
		}
		
		for (; iterator.hasPrevious();) {
			System.out.print(iterator.previous() + " ");
		}
	}
}

//�������������и�����lastRet������ά�����һ�β�����Ԫ����������ʼʱlastRet=-1���޷�remove����remove��lastRetҲ�ᱻ��Ϊ-1����˲����ظ��Ľ���remove����
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

public class Test {
	public static void main(String args[]) {
		ArrayList<Integer> array = new ArrayList<>();
		Collections.addAll(array, 6, 5, 4, 3, 2, 2, 1);
		System.out.println("�����е�Ԫ�أ�" + array);

		ListIterator<Integer> iter = array.listIterator();
		iter.next();
		iter.remove();
		iter.remove();//���쳣
		System.out.println("�����е�Ԫ�أ�" + array);
	}
}

//List��ɾ��Ԫ������õ������������Լ����������׳���
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

public class Test {
	public static void main(String args[]) {
		ArrayList<Integer> array = new ArrayList<>();
		Collections.addAll(array, 6, 5, 4, 3, 2, 2, 1);
		System.out.println("�����е�Ԫ�أ�" + array);
		
		//����ɾ����ʽ���ᵼ��������һ������ֵ��ֻ��ɾ��һ��2
//		for (int i = 0; i < array.size(); ++i) {
//			int n = array.get(i);
//			if (n == 2) {
//				array.remove(i);//List.remove(index)�еĲ����������������Ǿ���ֵ
//			}
//		}
		
		for (ListIterator<Integer> iter = array.listIterator(); iter.hasNext();) {
			if (iter.next() == 2) {
				iter.remove();
			}
		}
		
		System.out.println("�����е�Ԫ�أ�" + array);
	}
}
������£�
�����е�Ԫ�أ�[6, 5, 4, 3, 2, 2, 1]
�����е�Ԫ�أ�[6, 5, 4, 3, 1]
//------------------------------------------------------------------------------------------------
//Java�����ŵ���ͨ ��11�� �쳣���� P272
Throwable���������쳣��ĳ��࣬���������ֱ��������Error��Exception�����У�Error������������ָʾ�����Ӧ�ó���Ӧ����ͼ������������⣬Exception������������˺����Ӧ�ó�����Ҫ������쳣��

�ɲ�����쳣����Exception������ࣩ��Ϊ�ɿ�ʽ�쳣������ʱ�쳣�������͡�
�ɿ�ʽ�쳣���Բ���
����ʱ�쳣

//����ʱ�쳣
public class Test {
	public static void main(String args[]) {
		try {
			int i = 3/0;
		} catch (Exception e) {
			e.printStackTrace();//������System.out.println(e.getMessage());�����
		}
	}
}
�����´���
java.lang.ArithmeticException: / by zero
	at Test.main(Test.java:6)

11.2.3 �����쳣 P276
������0���������쳣����������0�������������㷨�쳣��
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
������£�
Infinity

��Java���쳣��������У���һ��Ĭ�ϴ����쳣�ĳ��򡣵���������쳣ʱ��Ĭ�ϴ��������ʾһ�������쳣���ַ�������ӡ�쳣�������Ķ�ջ�켣������ֹ����

11.2.4 �����±�Խ���쳣 P277
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
�������飬�Ƽ�ʹ��foreachѭ�������Ա��������±�Խ�硣���Ҫʹ��������±꣬��Ҫ��ס������±��Ǵ�0��ʼ����ġ������Ҫʹ������ĳ��ȣ����Ƽ�ʹ��length���ԡ�����ʹ��ArrayList��Ҳ���Ա�����Щ���⡣

P278 �����쳣��Ϣ
public class Test {
	public static void main(String args[]) {
		try {
			int i = 3/0;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getMessage������\t" + e.getMessage());
			System.out.println("getLocalizedMessage������\t" + e.getLocalizedMessage());
			System.out.println("toString������\t" + e.toString());
		}
	}
}
������£�
java.lang.ArithmeticException: / by zero
	at Test.main(Test.java:4)
getMessage������	/ by zero
getLocalizedMessage������	/ by zero
toString������	java.lang.ArithmeticException: / by zero

11.4 �����쳣 P280
11.4.2 ʹ�� try...catch...finally �����쳣
finally��������������ڵ���䣬���ܳ����Ƿ����쳣��Ҫִ�У�Ҳ����˵����ִ����try��catch֮�������ִ����catch�������������ڵ���䶼��ִ��finally�����䣩�����finally����ͨ������ִ���������ա��ͷ���Դ�Ȳ�����
��Java�н����쳣����ʱ��Ӧ�þ���ʹ��finally�������Դ���գ���Ϊ�� try...catch...finally �����У����ܳ����Ƿ����쳣�����ն���ִ��finally���飬��˿�����finally��������ͷ���Դ�Ĵ��롣
������finally������Ҳ������ִ�е�����

//P281 ����IO���󣬹رն���
import java.io.FileInputStream;
import java.io.IOException;
import java.security.spec.ECGenParameterSpec;

public class CloseIo {
	private FileInputStream in = null;
	
	public void readInfo() {
		try {
			in = new FileInputStream("src/IStringDeal.java");
			System.out.println("����IO���������ڴ���Դ��");
		} 
		catch (IOException io) {
			io.printStackTrace();
			System.out.println("����IO�������쳣��");
		}
		//�о����finally�鲻��Ҳûʲô���⣬���������ߵ�
		finally {
			if (in != null) {
				try {
					in.close();
					System.out.println("�ر�IO�����ͷ��ڴ���Դ��");
				} 
				catch (IOException ioe) {
					ioe.printStackTrace();
					System.out.println("�ر�IO�������쳣��");
				}
			}
		}
	}
	
	public static void main(String args[]) {
		CloseIo ex = new CloseIo();
		ex.readInfo();
	}
}
������£�
����IO���������ڴ���Դ��
�ر�IO�����ͷ��ڴ���Դ��

11.4.3 ʹ��try...finally�����쳣 P282
finally�����䲻�ܳ����Ƿ����쳣�����ض����߽�ȥ
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
				System.out.println("����IO���������ڴ���Դ��");
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("����IO�������쳣��");
			}
		}
		finally {
			if (in != null) {
				try {
					in.close();
					System.out.println("�ر�IO�����ͷ��ڴ���Դ��");
				} 
				catch (IOException ioe) {
					ioe.printStackTrace();
					System.out.println("�ر�IO�������쳣��");
				}
			}
		}
	}
	
	public static void main(String args[]) {
		CloseIo ex = new CloseIo();
		ex.readInfo();
	}
}

11.5.1 ʹ��throws�����׳��쳣 P283
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.spec.ECGenParameterSpec;

public class CloseIo {
	private FileInputStream in = null;
	
	public void showInfo() throws Exception {
		FileInputStream in = new FileInputStream("src/abc.java");//�������in�ɹ����ļ�����ȴû�йر��ļ������������Դй¶
	}
	
	public static void main(String args[]) {
		CloseIo ex = new CloseIo();
		ex.showInfo();//��showInfo�������ܻ��׳��쳣��������ȴû�д���
	}
}

//P286 ʹ��throw�׳��쳣
public class CloseIo {
	final static double PI = 3.14;
	
	public void computeArea(double r) throws Exception {
		if (r <= 20.0) {
			throw new Exception("�����쳣��\n�뾶Ϊ��" + r + "\n�뾶����С��20��");
		}
		else {
			double circleArea = PI * r * r;
			System.out.println("�뾶��" + r + "��Բ������ǣ�" + circleArea);
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

11.5.3 �������׳��쳣
���ĳ���໹û��ʵ�֣��������������׳��쳣���������������ʱ�����׳��쳣���Ա��Ժ��޸���ɡ�

public class CloseIo {
	public void computeArea(double r) {
		throw new UnsupportedOperationException("������δʵ��");
	}
	
	public static void main(String args[]) {
		CloseIo ex = new CloseIo();
		ex.computeArea(20.1);
	}
}
������£�
Exception in thread "main" java.lang.UnsupportedOperationException: ������δʵ��
	at CloseIo.computeArea(CloseIo.java:4)
	at CloseIo.main(CloseIo.java:9)

11.6.1 �����Զ����쳣�� P287
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
������£�
Throw a NewException.

11.6.2 ʹ���Զ����쳣�� P288
//�о����ϵ����������̫��
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
������£�
Throw a NewException.
NewException

11.7 �쳣ʹ��ԭ��
--��Ҫ�����ʹ���쳣������������ϵͳ�ĸ�����
--ʹ��try...catch���鲶���쳣ʱ��Ҫ���쳣��������
--tyr...catch����ķ�Χ��Ҫ̫�����������ڶ��쳣�ķ�����
--һ������������ʱ���������ķ��������׳���ͬ���쳣�������쳣��
//------------------------------------------------------------------------------------------------
//Java�����ŵ���ͨ ��12�� ������� P293
Java���Զ����������ר�Ÿ�����ַ�ʽ������/�������Щ�඼������java.io���С����������������඼�ǳ�����InputStream���ֽ����������������Reader���ַ��������������ࣻ��������������ǳ�����OutputStream���ֽ���������������Writer���ַ�������������ࡣ

12.2.1 ������ P294

12.2.2 ����� P295

12.2.4 �����滻�ı��ļ����� P297

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Test {	
	public static void main(String args[]) {
		String before = "today";
		String after = "����";
		
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
				//���������������жϣ���ô�Ὣtemp��1024���ַ�ȫ��д���ļ��У����δ������������һ��δ�����ַ�
				if (flag == maxLength) {
					sb.append(temp);
				}
				else {
					//���ַ�������ض����ȸ��ӵ�sb��
					sb.append(temp, 0, flag);
					
					//���߽���ȡ���ض������ַ�����String�������ӵ�sb��
					// String tempString = new String(temp, 0, flag);
					// sb.append(tempString);
					
					//���·������ַ�ת��toString�õ��Ľ���������HashCode���ַ���������ԭ�ַ���������[C@1db9742������
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
�滻ǰ���ļ����ݣ�
today is a nice day.
What day is is today?
Hello.


�滻����ļ����ݣ�
���� is a nice day.
What day is is ����?
Hello.

toString()��
Returns a string representation of the object. In general, the toString method returns a string that "textually represents" this object. The result should be a concise but informative representation that is easy for a person to read. It is recommended that all subclasses override this method. 
The toString method for class Object returns a string consisting of the name of the class of which the object is an instance, the at-sign character `@', and the unsigned hexadecimal representation of the hash code of the object. In other words, this method returns a string equal to the value of: 

 getClass().getName() + '@' + Integer.toHexString(hashCode())

12.3.1 �ļ��Ĵ�����ɾ�� P298

import java.io.File;

public class Test {	
	public static void main(String args[]) {
		File file = new File("src","abc.txt");//ͬ"src/abc.txt"�������·���ļ���һ��Ҫ���ڣ������ڴ����ļ�ʱ�ᱨ��
		if (file.exists()) {
			file.delete();
			System.out.println("�ļ���ɾ��");
		}
		else {
			try {
				file.createNewFile();//�������ԭ�ļ������ﴴ�����ļ����ᴴ����Ҳ���ᱨ��
				System.out.println("�ļ��Ѵ���");
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
			System.out.println("�ļ����ƣ�" + file.getName());
			System.out.println("�ļ����ȣ�" + file.length());
			System.out.println("�ļ��Ƿ��������ļ���" + file.isHidden());
			System.out.println("��·����" + file.getParent());
			System.out.println("�ļ�����·����" + file.getAbsolutePath());
			System.out.println("�ļ�·����" + file.getPath());
			System.out.println("ϵͳ�ָ�����" + File.separator);
		}
		else {
			System.out.println("�ļ�������");
		}
		
		String newFileName = new String(file.getParent() + File.separator + "def.txt");//���ļ������������Ϊ���ļ��ࡣ���·����ͬ��Ҳ���������ƶ��ļ�
		file.renameTo(new File(newFileName));
	}
}
������£�
�ļ����ƣ�abc.txt
�ļ����ȣ�0
�ļ��Ƿ��������ļ���false
��·����src
�ļ�����·����D:\Program Files\eclipseJava workspace\LearningProject\src\abc.txt
�ļ�·����src\abc.txt
ϵͳ�ָ�����\

12.4.1 FileInputStream��FileOutputStream�� P302
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
			System.out.println("�ļ��е���Ϣ�ǣ�" + new String(byt, 0, len));
			in.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
������£�
�ļ��е���Ϣ�ǣ�Hello

��ȻJava�����ڳ������ʱ�Զ��ر����д򿪵��������ǵ�ʹ����������ʽ�عر��κδ򿪵�������һ����ϰ�ߡ�һ�����򿪵����п��ܻ��þ�ϵͳ��Դ����ȡ����ƽ̨��ʵ�֡����û�н��򿪵����رգ�����һ��������ͼ����һ����ʱ����Щ��Դ���ܻ�ò�����

12.4.2 FileReader���FileWriter�� P303

//P307 ɾ���ļ����������ļ�
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
������£�
File is deleted: d:\Program Files\eclipseJava workspace\dbTest\b.txt
File is deleted: d:\Program Files\eclipseJava workspace\dbTest\DirA\a.txt
Directory is delete: d:\Program Files\eclipseJava workspace\dbTest\DirA
Directory is delete: d:\Program Files\eclipseJava workspace\dbTest

12.5 �����������/�����
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
			FileWriter fw = new FileWriter(file);//���캯��������File����Ҳ�������ַ����ļ�·��
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
������£�
Line 1: abc
Line 2: def
Line 3: opq


//P310 �ļ�����
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
������£�
Key: abc-- Value: ���
Key: def-- Value: ��������
Key: opq-- Value: ʲô

�ļ����ݣ�
abc=���
def:��������
opq    :    ʲô

//P311 �ϲ����txt�ļ�
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

12.6 ��������/����� P312
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
			ds.writeUTF("ʹ��writeUTF()����д������");
			//ds.writeChars("ʹ��writeChars()����д������");//���������������޷���������
			//ds.writeBytes("ʹ��writeBytes����д������");
			ds.close();
			
			FileInputStream fis = new FileInputStream("src/Concatenation.txt");
			DataInputStream dis = new DataInputStream(fis);
			System.out.println(dis.readUTF());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

12.7 ZIPѹ������/����� P313
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

12.7.2 ��ѹ��ZIP�ļ� P315 δ��

//�򵥵�ͶƱ��� P319
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

count.txt�������£�
A:3
B:4
C:5
//------------------------------------------------------------------------------------------------
//Java�����ŵ���ͨ ��13�� Swing������� P322
13.2.1 JFrame��ܴ���
import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;


public class JFrameExample {
	public static void main(String args[]) {
		new JFrameExample().CreateJFrame("����һ��JFrame����");
	}
	
	public void CreateJFrame(String title) {
		JFrame jf = new JFrame(title);//����һ����ʼ���ɼ��ģ����Ǿ������Ĵ��壬���Բ���title����
		Container container = jf.getContentPane();
		JLabel jl = new JLabel("����һ��JFrame����");
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(jl);
		container.setBackground(Color.white);
		jf.setVisible(true);
		jf.setSize(400, 300);
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//DO_NOTHING_ON_CLOSEʱ�����X�޷��رմ��壻HIDE_ON_CLOSEʱ�����XҲ���Թرմ���
	}
}

13.2.2 JDialog���� P325

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
		//ʵ����һ��JDailog�����ָ���Ի���ĸ����塢������������
		super(jFrame, "��һ��JDialog����", true);
		Container container = getContentPane();
		container.add(new JLabel("����һ���Ի���"));
		setSize(100, 100);
		//setBounds(120, 120, 100, 100); // ���öԻ������С������ǰ����������������Ļ���Ͻǵľ���
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
		
		//�����е�JLabel�޷���ʾ����������һ�е�container.setLayout(null);ȥ�����ֻᵼ��JButtonռ�����������壬Ŀǰ��֪����ν����
//		JLabel jl = new JLabel("����һ��JFrame����");
//		jl.setHorizontalAlignment(SwingConstants.CENTER);
//		container.add(jl);
		
		JButton bl = new JButton("�����Ի���");
		bl.setBounds(10, 10, 100, 21);//��������JButton�ڴ����е�λ�ú����С
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

13.2.3 ���ô����С P326
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlFormSize extends JFrame {
	public static void main(String args[]) {
		new ControlFormSize();
	}
	
	public ControlFormSize() {
		setTitle("���ô����С");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);//���õ�ǰ������壬��getContentPane���෴�Ĺ���
		JLabel label = new JLabel("��ȣ�400�� �߶ȣ�300");
		contentPane.add(label, BorderLayout.CENTER);
		setVisible(true);
	}
	
}

13.2.4 ��ֹ�ı䴰��Ĵ�С P327
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
		container.setLayout(null);//������setLayout�Ļ�����JButton��ռ����������С
		
		setTitle("���ô����С");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		setLocation(100, 100);
		
		String title = new String("�л������Ƿ�ɱ�״̬");
		JButton jb = new JButton(title);//��Ȼû���ҵ������޸�JButton���Ƶķ�����
		//jb.setBounds(MAXIMIZED_BOTH, MAXIMIZED_BOTH, 200, 22);
		jb.setSize(200, 22);
		//jb.setHorizontalAlignment(SwingConstants.CENTER);//ûЧ����ֻ��JLabel�����ã�
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

13.3.2 ͼ���ʹ�� P328
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
		JLabel jb = new JLabel("����", icon, SwingConstants.CENTER);
		JFrame jf = new JFrame();
		Container c = jf.getContentPane();
		c.add(jb);
		jf.setSize(100, 100);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}

//P329 ʹ��ͼƬͼ��
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
		JLabel jl = new JLabel("����һ��JFrame����", JLabel.CENTER);
		URL url = MyImageIcon.class.getResource("imageButton.jpg");//��Ҫ��imageButton.jpg��MyImageIcon.class�ļ�������ͬ��Ŀ¼��Ŀǰ����ֻ��.jpg���ã�.bmp�޷�ʶ��
		Icon icon = new ImageIcon(url);
		jl.setIcon(icon);
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		jl.setOpaque(true);//���ñ�ǩΪ��͸��״̬
		container.add(jl);
		setSize(250, 100);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
	}
	
	public static void main(String args[]) {
		new MyImageIcon();
	}
}

13.3.3 ΪͼƬ���˵�� P330
import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class UseLabelFrame extends JFrame {
	public UseLabelFrame() {
		super();
		setTitle("ʹ�ñ�ǩ���");
		setBounds(100, 100, 330, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JLabel label = new JLabel();
		label.setText("����������ɽˮ��Ƭ��");
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

13.4 ���ò��ֹ����� P331
13.4.1 ���Բ���
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class AbsolutePosition extends JFrame {
	public AbsolutePosition() {
		setTitle("������ʹ�þ��Բ���");
		setLayout(null);//ȡ�����ֹ�����
		setBounds(0, 0, 200, 150);//����ÿ������Ĵ�С��λ��
		Container c = getContentPane();
		JButton b1 = new JButton("��ť1");
		JButton b2 = new JButton("��ť2");
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

13.4.2 �����ֹ����� P332

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class FlowLayoutPosition extends JFrame {
	public FlowLayoutPosition() {
		setTitle("������ʹ�������ֹ�����");
		Container c = getContentPane();
		setLayout(new FlowLayout(2, 10, 10));//��һ��������FlowLayout.LEFT=0������룻FlowLayout.CENTER=1���Ҷ��룻FlowLayout.RIGHT=2���Ҷ��룬��2��3�������ø����֮���ˮƽ����봹ֱ���
		
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

13.4.3 �߽粼�ֹ����� P334
��Ĭ�ϲ�ָ�����岼�ֵ�����£�Swing����Ĳ���ģʽ�Ǳ߽磨BorderLayout�����ֹ�����������13.3 ����������ֻ�����һ����ǩ����������ǩ�������ڴ����м䣬�����������ռ���˴�������пռ䣬ʵ���������������Ĭ��ʹ���˱߽粼�ֹ�������
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class BorderlayoutPosition extends JFrame {
	String border[] = {BorderLayout.CENTER, BorderLayout.NORTH, BorderLayout.SOUTH, BorderLayout.WEST, BorderLayout.EAST};
	String buttonName[] = {"center button", "north button", "south button", "west button", "east button"};
	
	public BorderlayoutPosition() {
		setTitle("������ʹ�ñ߽粼�ֹ�����");
		Container c = getContentPane();
		setLayout(new BorderLayout());
		
		for (int i = 0; i < border.length; ++i) {
			c.add(border[i], new JButton(buttonName[i]));//�ṩ���������������Ĺ��ܣ���ͬʱ��������İڷ�λ��
		}
		
		setSize(350, 200);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new BorderlayoutPosition();
	}
}

13.4.4 ��񲼾ֹ����� P335
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GridLayoutPosition extends JFrame {
	public GridLayoutPosition() {
		setTitle("������ʹ�÷�񲼾ֹ�����");
		Container c = getContentPane();
		setLayout(new GridLayout(7, 3, 5, 5));//ǰ����������������������������ֻ��һ������Ϊ0������һ�л�һ�п����������������������������ָ������֮��ļ�ࡣ
		
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

13.5 ������� P336
13.5.1 JPanel��� P336
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class JPanelTest extends JFrame {
	public JPanelTest() {
		setTitle("�����������ʹ�������");
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

13.5.2 JScrollPane��� P337
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class JScrollPaneTest extends JFrame {
	public JScrollPaneTest() {
		setTitle("�������������ֱ༭��");
		Container c = getContentPane();
		JTextArea ta = new JTextArea(20, 50);//��ʼ�ı��༭������������
		ta.setText("�������������ֱ�����");
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

//������ĸ�JScrollPane
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class JScrollPaneTest extends JFrame {
	public JScrollPaneTest() {
		setTitle("�������������ֱ༭��");
		Container c = getContentPane();
		setLayout(new GridLayout(2, 2, 10, 10));
		JTextArea ta1 = new JTextArea(10, 50);
		ta1.setText("�������������ֱ�����1");
		JScrollPane sp1 = new JScrollPane(ta1);
		
		JTextArea ta2 = new JTextArea(20, 50);
		ta2.setText("�������������ֱ�����2");
		JScrollPane sp2 = new JScrollPane(ta2);
		
		JTextArea ta3 = new JTextArea(30, 50);
		ta3.setText("�������������ֱ�����3");
		JScrollPane sp3 = new JScrollPane(ta3);
		
		JTextArea ta4 = new JTextArea(40, 50);
		ta4.setText("�������������ֱ�����4");
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

13.6.1 �ύ��ť��� P338
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
		jb.setToolTipText("ͼƬ��ť");
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

13.6.2 ��ѡ��ť��� P339
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

public class JRadioButtonTest extends JFrame {
	public JRadioButtonTest() {
		setTitle("�������������ֱ༭��");
		Container c = getContentPane();
		setLayout(new BorderLayout());
		//setLayout(new GridLayout(3, 2, 5, 5));
		
		JRadioButton jr1 = new JRadioButton("radio button 1");
		JRadioButton jr2 = new JRadioButton("radio button 2");
		JRadioButton jr3 = new JRadioButton("radio button 3");
		ButtonGroup group = new ButtonGroup();//��Ҫ�ӵ����У���������������ѡ����
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

13.6.3 ��ѡ����� P340
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
		setTitle("��ѡ���ʹ��");
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
					ta.append("��ѡ��1��ѡ��\n");
				}
				else {
					ta.append("��ѡ��1��ȥ��ѡ\n");
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

13.7.1 �����б����� P341

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
	//������Լ�ʵ��ComboBoxModel�����Խ�Array����������JComboBox
	//private String stringArray[] = {"a", "b", "c"};
	//private JComboBox<String> jc = new JComboBox<>(stringArray);
	
	JComboBox<String> jc = new JComboBox<>(new MyComboBox());
	JLabel jl = new JLabel("��ѡ��֤��:");
	
	public JComboBoxModelTest() {
		setTitle("�ڴ��������������б��");
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
	String test[] = {"���֤", "����֤", "ѧ��֤", "����֤"};
	
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

13.7.2 �б����� P343
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
	private JLabel jl = new JLabel("��ѡ��֤��:");
	
	public JListTest() {
		setTitle("�ڴ��������������б��");
		Container c = getContentPane();
		setLayout(new BorderLayout());
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		JTextArea ta = new JTextArea(20, 10);
		JScrollPane scrollPanel = new JScrollPane(ta);
		panel1.add(scrollPanel, BorderLayout.CENTER);
		c.add(panel1, BorderLayout.WEST);
		
		JScrollPane js = new JScrollPane(jc);//Ҳû�п������������б�Ч��
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(1, 20, 10));
		jc.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				ta.append((String) jc.getSelectedValue() + "\n");//ÿ�λ��ѡ���ֵ������Σ���֪��Ϊʲô��
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
	String contents[] = {"���֤", "����֤", "ѧ��֤", "����֤", "��ʻ֤", "ǩ֤", "����֤"};
	
	@Override
	public String getElementAt(int index) {
		return (index < contents.length) ? contents[index] : null;
	}
	
	@Override
	public int getSize() {
		return contents.length;
	}
}

13.8 �ı���� P345
13.3.1 �ı������
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
		setTitle("�ڴ��������������б��");
		Container c = getContentPane();
		setLayout(new BorderLayout());

		final JTextField jt = new JTextField("aaa", 20);
		
		//���������
		//final JPasswordField jt = new JPasswordField("aaa", 20);
		//jt.setEchoChar('#');
		
		final JButton jb = new JButton("���");
		jt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				jt.setText("�����¼�");
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

13.8.2 �������� P346
final JPasswordField jt = new JPasswordField("aaa", 20);
jt.setEchoChar('#');

13.8.3 �ı������ P346
JTextArea jt = new JTextArea("�ı���", 6, 6);
jt.setLineWrap(true);

13.8.4 ���ı������ñ���ͼƬ P347

13.8.5 ���ı������ñ���ͼƬ P348

13.9.1 ���ô�������ͼ�� P349
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
//Java�����ŵ���ͨ ��14�� �߼������¼� P355
14.1 �����¼� P356
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class JTextFieldTest extends JFrame {
	public JTextFieldTest() {
		setTitle("�ڴ��������������б��");
		Container c = getContentPane();
		setLayout(new BorderLayout());

		final JTextArea ja = new JTextArea("aaa", 10, 20);
		ja.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("�˴�������ǣ�" + e.getKeyChar());
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				String keyText = KeyEvent.getKeyText(e.getKeyCode());
				System.out.println("���ͷŵ��ǣ�" + keyText);
				System.out.println();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				String keyText = KeyEvent.getKeyText(e.getKeyCode());
				if (e.isActionKey()) { //�Ƿ�Ϊ��������capital/break��
					System.out.println("�����µ��Ƕ�������" + keyText);
				} else {
					System.out.println("�����µ��ǷǶ�������" + keyText);
					int keyCode = e.getKeyCode();
					switch (keyCode) {
					case KeyEvent.VK_CONTROL:
						System.out.println("��Ctrl��������");
						break;
					case KeyEvent.VK_ALT:
						System.out.println("��Alt��������");
						break;
					case KeyEvent.VK_SHIFT:
						System.out.println("��Shift��������");
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

14.2 ����¼� P357

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
		setTitle("�ڴ��������������б��");
		Container c = getContentPane();
		setLayout(new BorderLayout());

		final JTextArea ja = new JTextArea("aaa", 10, 20);
		ja.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("�˴�������ǣ�" + e.getKeyChar());
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				String keyText = KeyEvent.getKeyText(e.getKeyCode());
				System.out.println("���ͷŵ��ǣ�" + keyText);
				System.out.println();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				String keyText = KeyEvent.getKeyText(e.getKeyCode());
				if (e.isActionKey()) { //�Ƿ�Ϊ��������capital/break��
					System.out.println("�����µ��Ƕ�������" + keyText);
				} else {
					System.out.println("�����µ��ǷǶ�������" + keyText);
					int keyCode = e.getKeyCode();
					switch (keyCode) {
					case KeyEvent.VK_CONTROL:
						System.out.println("��Ctrl��������");
						break;
					case KeyEvent.VK_ALT:
						System.out.println("��Alt��������");
						break;
					case KeyEvent.VK_SHIFT:
						System.out.println("��Shift��������");
						break;
					}
				}
			}
		});
		
		ja.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.print("��갴ť���ͷţ�");
				int i = e.getButton();
				if (i == MouseEvent.BUTTON1) {
					System.out.println("�ͷŵ���������");
				}
				if (i == MouseEvent.BUTTON2) {
					System.out.println("�ͷŵ���������");
				}
				if (i == MouseEvent.BUTTON3) {
					System.out.println("�ͷŵ�������Ҽ�");
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.print("��갴ť�����£�");
				int i = e.getButton();
				if (i == MouseEvent.BUTTON1) {
					System.out.println("���µ���������");
				}
				if (i == MouseEvent.BUTTON2) {
					System.out.println("���µ���������");
				}
				if (i == MouseEvent.BUTTON3) {
					System.out.println("���µ�������Ҽ�");
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				System.out.println("����Ƴ����");
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				System.out.println("����������");
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {//�������Ƴ������Χ�������¼����ᴥ��
				System.out.print("��������갴����");
				int i = e.getButton();
				if (i == MouseEvent.BUTTON1) {
					System.out.println("��������������");
				}
				if (i == MouseEvent.BUTTON2) {
					System.out.println("��������������");
				}
				if (i == MouseEvent.BUTTON3) {
					System.out.println("������������Ҽ�");
				}
				int clickCount = e.getClickCount();
				System.out.println("��������Ϊ��" + clickCount);//˫�����ʱ����һ�ε�����꽫����һ�ε����¼�
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

14.3 �����¼� P359
14.3.1 �����役��仯�¼�
14.3.2 ������״̬�仯�¼�
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
		// Ϊ�������״̬�¼�������
		addWindowStateListener(new MyWindowStateListener());
		setTitle("������״̬�¼�");
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	private class MyWindowStateListener implements WindowStateListener {
		public void windowStateChanged(WindowEvent e) {
			int oldState = e.getOldState();// ��ô�����ǰ��״̬
			int newState = e.getNewState();// ��ô������ڵ�״̬
			String from = "";// ��ʶ������ǰ״̬�������ַ���
			String to = "";// ��ʶ��������״̬�������ַ���
			switch (oldState) {// �жϴ�̨��ǰ��״̬
				case Frame.NORMAL:// ���崦��������
					from = "������";
					break;
				case Frame.MAXIMIZED_BOTH:// ���崦�����
					from = "���";
					break;
				default:// ���崦����С��
					from = "��С��";
			}
			switch (newState) {// �жϴ�̨���ڵ�״̬
				case Frame.NORMAL:// ���崦��������
					to = "������";
					break;
				case Frame.MAXIMIZED_BOTH:// ���崦�����
					to = "���";
					break;
				default:// ���崦����С��
					to = "��С��";
			}
			System.out.println(from + "����>" + to);
		}
	}
}

14.3.3 �������������¼�

14.4 ѡ���¼� P363
ComboBox.addItemListener(new ItemListener(){})

14.5 ���ģ���¼� P364
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
	private JTable table;// ����һ��������
	private DefaultTableModel tableModel;// ����һ�����ģ�Ͷ���
	private JTextField aTextField;
	private JTextField bTextField;
	public static void main(String args[]) {
		TableModelEvent_Example frame = new TableModelEvent_Example();
		frame.setVisible(true);
	}
	public TableModelEvent_Example() {
		super();
		setTitle("���ģ���¼�ʾ��");
		setBounds(100, 100, 650, 213);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		String[] columnNames = { "A", "B" };
		String[][] rowValues = { { "A1", "B1" }, { "A2", "B2" },
				{ "A3", "B3" }, { "A4", "B4" } };
		// �������ģ�Ͷ���
		tableModel = new DefaultTableModel(rowValues, columnNames);
		// Ϊ���ģ������¼�������
		tableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				int type = e.getType();// ����¼�������
				int row = e.getFirstRow() + 1;// ��ô����˴��¼��ı��������
				int column = e.getColumn() + 1;// ��ô����˴��¼��ı��������
				if (type == TableModelEvent.INSERT) {// �ж��Ƿ��в����д���
					System.out.print("�˴��¼��� ���� �д�����");
					System.out.println("�˴β�����ǵ� " + row + " �У�");
					// �ж��Ƿ����޸��д���
				} else if (type == TableModelEvent.UPDATE) {
					System.out.print("�˴��¼��� �޸� �д�����");
					System.out.println("�˴��޸ĵ��ǵ� " + row + " �е� " + column
							+ " �У�");
					// �ж��Ƿ���ɾ���д���
				} else if (type == TableModelEvent.DELETE) {
					System.out.print("�˴��¼��� ɾ�� �д�����");
					System.out.println("�˴�ɾ�����ǵ� " + row + " �У�");
				} else {
					System.out.println("�˴��¼��� ����ԭ�� ������");
				}
			}
		});
		table = new JTable(tableModel);// ���ñ��ģ�Ͷ��󴴽������󣬵�ͬ��table = new JTable();table.setModel(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		
		
		final JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		final JLabel aLabel = new JLabel("A��");
		panel.add(aLabel);
		
		aTextField = new JTextField(15);
		panel.add(aTextField);
		
		final JLabel bLabel = new JLabel("B��");
		panel.add(bLabel);
		
		bTextField = new JTextField(15);
		panel.add(bTextField);
		
		final JButton addButton = new JButton("���");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] rowValues = { aTextField.getText(),
						bTextField.getText() };
				tableModel.addRow(rowValues);// ����ģ�������һ��
				aTextField.setText(null);
				bTextField.setText(null);
			}
		});
		panel.add(addButton);
		
		final JButton updButton = new JButton("�޸�");
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
		
		final JButton delButton = new JButton("ɾ��");
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ��ñ���е�ѡ����
				int[] selectedRows = table.getSelectedRows();
				for (int row = 0; row < selectedRows.length; row++) {
					// �ӱ��ģ�����Ƴ�����е�ѡ����
					tableModel.removeRow(selectedRows[row] - row);
				}
			}
		});
		panel.add(delButton);
	}
}

14.6 ���䷶�� P367
14.6.1 ���䷶��1��ģ���������
�����ƶ�����KeyAdapter
14.6.2 ���䷶��2���������Ϸ
//------------------------------------------------------------------------------------------------
//Java�����ŵ���ͨ ��15�� ���߳� P372
15.2 ʵ���̵߳����ַ�ʽ P374
15.2.1 �̳�Thread��
��main()�����У�ʹ�߳�ִ����Ҫ����Thread���е�start()������start()�������ñ����ǵ�run()���������������start()�������߳���Զ��������������������û�е���start()����֮ǰ��Thread����ֻ��һ��ʵ����������һ���������̡߳�
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

15.2.2 ʵ��Runnable�ӿ�
�����Ҫ�̳������ࣨ��Thread�ࣩ��ʹ�ó������ʹ���̣߳�����Ҫʹ��Runnable�ӿڡ����磬һ����չJFrame���GUI���򲻿����ټ̳�Thread�࣬��ΪJava�����в�֧�ֶ�̳У���ʱ�������Ҫʵ��Runnable�ӿ�ʹ�����ʹ���̵߳Ĺ��ܡ�
���Բ�ѯAPI��ʵ����Thread�����ʵ����Runnable�ӿڣ����е�run()�������Ƕ�Runnable�ӿ��е�run()�����ľ���ʵ�֡�

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
						count = 10;//��0��ʼ��ǡ���ߵ�200��ѭ��һ�δ�10��ʼ֮���߲���200�����˳�while��
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

15.2.3 ����1���鿴�̵߳�����״̬ P377

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

15.2.4 ����2���鿴JVM�е��߳���

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
		Thread threads[] = new Thread[group.activeCount()];//5����߳�
		int count = group.enumerate(threads, false);//����ȴ������4����̵߳�threads��
		for (int i = 0; i < count; ++i) {
			threadList.add(group.getName() + "�߳��飺" + threads[i].getName());
		}
		return threadList;
	}
	
	public static List<String> getThreadGroups(ThreadGroup group) {
		List<String> threadList = getThreads(group);
		ThreadGroup groups[] = new ThreadGroup[group.activeGroupCount()];//1����߳���
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
������£�
system�߳��飺Reference Handler
system�߳��飺Finalizer
system�߳��飺Signal Dispatcher
system�߳��飺Attach Listener
main�߳��飺main

15.3 �̵߳�״̬
��Ȼ���߳̿�������ͬʱִ�У�����ʵ����ͬһʱ�����ֻ��һ���̱߳�ִ�У�ֻ���߳�֮���л��Ͽ죬���Ի�ʹ�˲����߳���ͬʱ���еļ���

15.4 �����̵߳ķ��� P382
15.4.1 �̵߳����� 
Thread.sleep(mills);
ʹ����sleep()�������߳���һ��ʱ���ڻ����������ǲ����ܱ�֤���������������״̬��ֻ�ܱ�֤���������״̬��

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
					graphics.drawLine(x, y, 100, y++);//����ֱ�߲�������ֱ����
					
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

15.4.2 �̵߳ļ��� P383
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
		//super();//��д���Զ�����
		
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

15.4.3 �̵߳��ж� P385
�ṩ��run()������ʹ������ѭ������ʽ��Ȼ��ʹ��һ�������ͱ��ѭ����ֹͣ��

����߳�����Ϊʹ����sleep()��wait()���������˾���״̬����ʱ����ʹ��Thread���interrupt()����ʹ�߳��뿪run()������ͬʱ�����̣߳����ǳ�����׳�InterruptedException�쳣��

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
//�ο���������������ť������isContinue����������
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
					System.out.println("Java ��̴ʵ�\thttp://www.mrbccd.com");
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
						System.out.println("��ǰ�̱߳��ж�");
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
������£�
java.lang.InterruptedException: sleep interrupted
	at java.lang.Thread.sleep(Native Method)
	at InterruptedSwing$1.run(InterruptedSwing.java:24)
	at java.lang.Thread.run(Unknown Source)
��ǰ�̱߳��ж�
Run exception

15.4.4 �̵߳����� P386
ʹ��yield()����������ǰ����������״̬�µ��߳�һ�����ѣ���֪�����Խ���Դ���ø������̣߳����������һ�ְ�ʾ��û���κ�һ�ֻ��Ʊ�֤��ǰ�̻߳Ὣ��Դ���á�
yield()����ʹ����ͬ�����ȼ����߳��н����ִ��״̬�Ļ��ᣬ����ǰ�̷߳���ִ��Ȩʱ���ٶȻص�����״̬������֧�ֶ�����Ĳ���ϵͳ��˵������Ҫ����yield()��������Ϊ����ϵͳ��Ϊ�߳��Զ�����CPUʱ��Ƭ��ִ�С�

15.4.5 �鿴���޸��̵߳����ȼ� P386


15.4.6 ���ߵ�ǰ�߳� P388

15.5 �̵߳����ȼ� P389
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
		// �ֱ�ʵ����4���߳�
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
	
	// ���������̵߳����ơ����ȼ��ķ���
	public static void setPriority(String threadName, int priority,
			Thread t) {
		t.setPriority(priority); // �����̵߳����ȼ�
		t.setName(threadName); // �����̵߳�����
		t.start(); // �����߳�
	}
	
	public static void main(String[] args) {
		init(new PriorityTest(), 100, 100);
	}
	
	public static void init(JFrame frame, int width, int height) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setVisible(true);
	}
	
	private final class MyThread implements Runnable { // ����һ��ʵ��Runnable�ӿڵ���
		private final JProgressBar bar;
		int count = 0;
		
		private MyThread(JProgressBar bar) {
			this.bar = bar;
		}
		
		public void run() { // ��дrun()����
			while (true) {
				bar.setValue(count += 10); // ���ù�������ֵÿ������10
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("��ǰ�߳����ж�");
				}
			}
		}
	}
	
}

15.6 �߳�ͬ�� P391
��д���̳߳���ʱ��Ӧ�ÿ��ǵ��̰߳�ȫ���⡣ʵ�����̰߳�ȫ������Դ�������߳�ͬʱ��ȡ��һ��������ݡ�
//�����������Щ�̶߳�û�������˳���
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
				System.out.println("��Ʊ���� " + num-- + "��");
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
������£�
��Ʊ���� 10��
��Ʊ���� 9��
��Ʊ���� 8��
��Ʊ���� 7��
��Ʊ���� 6��
��Ʊ���� 5��
��Ʊ���� 4��
��Ʊ���� 3��
��Ʊ���� 2��
��Ʊ���� 1��
��Ʊ���� 0��
��Ʊ���� -1��
��Ʊ���� -2��


15.6.2 �߳�ͬ������ P392
1. ͬ����
Java���ṩ��ͬ�����ƣ�������Ч�ط�ֹ��Դ��ͻ��ͬ������ʹ��synchronized�ؼ��֡�

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
					System.out.println("��Ʊ���� " + num-- + "��");
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
��ӡ�����û�г��ָ�����������Ϊ����Դ��������ͬ�����С����ͬ����Ҳ����Ϊ�ٽ�������ʹ��synchronized�ؼ��֣��﷨��ʽ���£�
synchronized(Object) {
	//...
}
ͨ����������Դ�Ĳ���������synchronized����������У��������߳�Ҳ��ȡ�������ʱ������ȴ������ͷ�ʱ���ܽ��������ObjectΪ����һ������ÿ�����󶼴���һ����־λ������������ֵ���ֱ�Ϊ0��1��һ���߳����е�ͬ����ʱ���ȼ��ö���ı�־λ�����Ϊ0״̬��������ͬ�����д��������߳������С���ʱ���߳̾ʹ��ھ���״̬��ֱ������ͬ�����е��߳�ִ����ͬ�����еĴ���Ϊֹ����ʱ�ö���ı�ʶλ������Ϊ1�����̲߳���ִ��ͬ�����еĴ��룬����Object����ı�ʶλ����Ϊ0����ֹ�����߳�ִ��ͬ�����еĴ��롣

2. ͬ������
�ڷ���ǰ������synchronzied�ؼ��ֵķ��������﷨��ʽ���£�
synchronized void f() {}





public class ThreadSafeTest implements Runnable {
	int num = 10;
	
	public void run() {
		while (true) {
			//����������䣬���Ʊ��Ϊ0�����˳������������̻߳�һֱ�ȴ���Դ���޷������˳�
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
			System.out.println("��Ʊ���� " + num-- + "��");
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
������£�
��Ʊ���� 10��
��Ʊ���� 9��
��Ʊ���� 8��
��Ʊ���� 7��
��Ʊ���� 6��
��Ʊ���� 5��
��Ʊ���� 4��
��Ʊ���� 3��
��Ʊ���� 2��
��Ʊ���� 1��
Thread exit
Thread exit
Thread exit
Thread exit

15.7 �̼߳��ͨ�� P394
��ͬ���ĽǶ���˵������sleep()�������̲߳��ͷ�����������wait()�������߳��ͷ�����
wait()��notify()��notifyAll()����ֻ����ͬ�����ͬ��������ʹ�á�

//������Щ���⣬��ʱ����߲���ȥ
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
			Thread.currentThread().sleep(1000);//ʹ��ǰ�߳�����1000����
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
						System.out.println("�����������������̵߳ȴ�");
					}
					
					if (count == 0) {
						progressBar.setValue(count += 100);
						System.out.println("�������ĵ�ǰֵΪ��" + count);
						
						//ȷ��֪ͨt2ʱ��t2�߳��Ѿ�����
						try {
							Thread.currentThread().sleep(5000);//ʹ��ǰ�߳�����100����
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						synchronized (t2) { //����ΪʲôҪ��t2��
							System.out.println("����������ֵ�����Խ��еݼ�����");
							t2.notify();
						}
					}
					
					try {
						Thread.currentThread().sleep(500);//ʹ��ǰ�߳�����100����
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
					System.out.println("��������ǰֵΪ��" + count);
					
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

15.8.1 �̵߳Ĳ������ P396

public class EmergencyThread implements Runnable {
	@Override
	public void run() {
		for (int i = 1; i < 6; ++i) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("���������" + i + "�ų�������");
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
			System.out.println("���������" + i + "�ų�������");
		}
	}
}
������£�����Ψһ��
���������1�ų�������
���������1�ų�������
���������2�ų�������
���������3�ų�������
���������4�ų�������
���������5�ų�������
���������2�ų�������
���������3�ų�������
���������4�ų�������
���������5�ų�������
//------------------------------------------------------------------------------------------------
//Java�����ŵ���ͨ ��16�� ����ͨ�� P400
TCP��UDPЭ�� P402
TCPЭ����һ���Թ̽�����Ϊ�����ĺڷ������ṩ��̨��������ѡ�����ݴ��͡�
UDP��������ͨ��Э�飬����֤��ѡ���ݵĴ��䣬���ܹ������ɸ�Ŀ�귢�����ݣ����շ������ɸ�Դ�����ݡ�
һЩ����ǽ��·���������óɲ�����UDP���ݰ����䣬���������UDP���ӷ�������⣬Ӧ��ȷ���Ƿ�����UDPЭ�顣

16.1.3 �˿ں��׽��� P402
�˿ڱ��涨Ϊһ����0~65535 ֮���������HTTP����һ��ʹ��80�˿ڣ�FTP����ʹ��21�˿ڡ�
ͨ��0~1023 ֮��Ķ˿�������һЩ֪������������Ӧ�ã��û�����ͨ����Ӧ�ó���Ӧ��ʹ��1024���ϵĶ˿������Ա���˿ں�����һ��Ӧ�û�ϵͳ�������ö˿ڳ�ͻ��
����������׽��֣�Socket�����ڽ�Ӧ�ó�����˿�����������


import java.net.InetAddress;
import java.net.UnknownHostException;


public class Address {
	public static void main(String args[]) {
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			String localName = ip.getHostName();
			String localIp = ip.getHostAddress();
			System.out.println("������������" + localName);
			System.out.println("����IP��ַ��" + localIp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
������£�
������������Ben-PC
����IP��ַ��192.168.1.106

16.2.2 ServerSocket�� P407

16.2.3 TCP������� P408
//�������˳���
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
			server.setSoTimeout(5000);//���ó�ʱʱ��
			System.out.println("�������׽����Ѿ������ɹ�");
			while (true) {
				System.out.println("�ȴ��ͻ���������");
				socket = server.accept();
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				getClientMessage();
			}
		} catch (SocketTimeoutException e) {
			System.out.println("���ӳ�ʱ...");
			JOptionPane.showMessageDialog(null, "���ӳ�ʱ...");//����ֱ�ӵ�����ʾ���
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getClientMessage() {
		try {
			while (true) {
				System.out.println("�ͻ�����" + reader.readLine());
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

������£�
�������׽����Ѿ������ɹ�
�ȴ��ͻ���������

��ʵ���Ƿ������˳��򣬵���ʵ�����к�����ڷ�����û��ֹͣ���е�������ٴ����б�ʵ���������쳣���������ڷ���������ʹ�õĶ˿ں��Ѿ���ռ�ã������ڷ���������û��ֹͣ��������ٴ����оͷ������쳣��

//�ͻ��˳���
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
				//�����info������ʾ�ͻ��˺ͷ�������IP���˿ں�
				if (inputText.equals("info")) { //�ַ����ж������Ҫ��euqals
					InetAddress netAddress = socket.getInetAddress();
					String netIp = netAddress.getHostAddress();
					int netPort = socket.getPort();
					
					InetAddress localAddress = socket.getLocalAddress();
					String localIp = localAddress.getHostAddress();
					int localPort = socket.getLocalPort();
					
					System.out.print("Զ�̷�������IP��ַ��" + netIp + "\n");
					System.out.print("Զ�̷������Ķ˿ںţ�" + netPort + "\n");
					System.out.print("�ͻ������ص�IP��ַ��" + localIp + "\n");
					System.out.print("�ͻ������صĶ˿ںţ�" + localPort + "\n");
//					ta.append("Զ�̷�������IP��ַ��" + netIp + "\n");
//					ta.append("Զ�̷������Ķ˿ںţ�" + netPort + "\n");
//					ta.append("�ͻ������ص�IP��ַ��" + localIp + "\n");
//					ta.append("�ͻ������صĶ˿ںţ�" + localPort + "\n");
					
				} else {
					writer.println(tf.getText());//��socket������е������������������
					ta.append(tf.getText() + '\n');
				}
				tf.setText("");	
			}
		});
	}
	
	private void connect() {
		ta.append("��������\n");
		try {
			socket = new Socket("192.168.1.106", 8998);
			writer = new PrintWriter(socket.getOutputStream(), true);//����socket������Ĵ�ӡ����
			ta.append("�������\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		MyClient client = new MyClient("���������������");
		client.setBounds(300, 260, 340, 220);
		client.setVisible(true);
		client.connect();
	}
}

Ϊ��ʹ��ʵ���ܹ��������У�����������16.3 �ķ������˳��򣬲���ʵ���Ķ˿ں�Ҫ����16.3 �������˳����׽��ֵĶ˿ں�һ�£��������ᷢ������
��һ̨�����ϰ�װ�˶������Ӧ�ó���ʱ���ܿ���ָ���Ķ˿ں��ѱ�ռ�ã���ʱ���ܻ���������ǰ�ܺõ��������ͻȻ���в������ˣ���������ܿ���Ҳ�����ڶ˿ڱ���������ռ���ˡ���ʱ��������netstat -help����ð�����ʹ������netstat -an���鿴�ó�����ʹ�õĶ˿ڡ�P411

16.3 UDP������ƻ��� P413
DatagramSocket()
DatagramSocket(int port)
DatagramSocket(int port, InetAddress addr)
�ڽ��ճ���ʱ������ָ��һ���˿ںţ���Ҫ��ϵͳ�������������ʹ�õڶ��ֹ��캯�����ڷ��ͳ���ʱ��ͨ��ʹ�õ�һ�ֹ��캯������ָ���˿ںţ�����ϵͳ�ͻ�Ϊ���Ƿ���һ���˿ںš�

//�㲥�������򲻶����ⷢ���㲥��Ϣ
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Weather extends Thread {
	String weather = "��ĿԤ�����˵��д�����ᣬ������";
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

//���ճ���
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
	JButton start = new JButton("��ʼ����");
	JButton stop = new JButton("ֹͣ����");
	JTextArea inceAr = new JTextArea(10, 10);
	JTextArea inced = new JTextArea(10, 10);
	Thread thread;
	boolean b = false;
	
	public Receive() {
		super("�㲥���ݱ�");
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
		
		validate();//ˢ��
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
				inceAr.setText("���ڽ��յ����ݣ�\n" + message);
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
һֱû���յ�����֪��Ϊʲô������

16.4.1 �����ҷ���� P418
16.4.2 �����ҿͻ��� P419
//------------------------------------------------------------------------------------------------
//Java�����ŵ���ͨ ��17�� JDBC�������ݿ� P424
û��

//------------------------------------------------------------------------------------------------
//Java�����ŵ���ͨ ��18�� Swing�߼���� P452

18.1.1 ������� P453
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
		setTitle("�������Թ����ı��");
		setBounds(100, 100, 240, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String columnNames[] = {"A", "B"};
		String tableValues[][] = {{"A1", "B1"}, {"A2", "B2"}, {"A3", "B3"}, {"A4", "B4"}, {"A5", "B5"}};
		JTable table = new JTable(tableValues, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
}

//ֱ����ӵ������� P454
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
		setTitle("�������Թ����ı��");
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
		getContentPane().add(tableHeader, BorderLayout.NORTH);//û����䣬�򲻻���ʾ������
		
		tableHeader.setReorderingAllowed(true);//�����������Ƿ�����϶�����
	}
}

//ͨ�������Լ��ı�������ض��ķ���ʵ�ֱ����ض����� P455
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
		setTitle("�������Թ����ı��");
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

18.1.3 ���ݱ�� P458
��ñ��ĸ�����Ϣ��������������������ѡ�����к��е�

18.1.4 ����1���б�Ԫ������ʾ��Ϣ P460 ����2�������б����¼�
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
		setTitle("������ʾ��Ϣ���б�");
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
				list.setFont(new Font("΢���ź�", Font.PLAIN, 16));
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
			return "<html><font face=΢���ź� size=16 color=red>" + data[index][1] + "</font></html>";
		} else {
			return super.getToolTipText(event);
		}
	}
}

18.2.1 ���ñ��ģ�ʹ������ P462

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
		setTitle("�������Թ����ı��");
		setBounds(100, 100, 240, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String columnNames[] = {"A", "B"};
		String tableValues[][] = {{"A1", "B1"}, {"A2", "B2"}, {"A3", "B3"}, {"A4", "B4"}, {"A5", "B5"}};
		
		DefaultTableModel tableModel = new DefaultTableModel(tableValues, columnNames);
		JTable table = new JTable(tableModel);
		table.setRowSorter(new TableRowSorter<>(tableModel));
		//JScrollPane scrollPane = new JScrollPane(table);//������������
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
}

18.2.2 ά�����ģ�� P464
�� 14.5 ���ģ���¼� P364

18.2.3 ����3��ʵ���Զ�������б� P466
import java.awt.*;
import java.util.TreeSet;

import javax.swing.*;

public class SortedListModelTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static void main(String args[]) {
		new SortedListModelTest();
	}
	
	public SortedListModelTest() {
		setTitle("�Զ�������б�");
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
			fireContentsChanged(this, 0, getSize());//AbstractListModel ����������б��һ������Ԫ�ط�������֮����ô˷���
		}
	}
}

18.2.4 ����4������Ԥ��������б� P467
import java.awt.*;

import javax.swing.*;

public class FontTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static void main(String args[]) {
		new FontTest();
	}
	
	public FontTest() {
		setTitle("�Զ�������б�");
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
		
		revalidate();//��Ҫ������������ˢ�²����������޷��������ɵ�����
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

18.3.2 ����5���ṩ�б������ı�� P469

18.4 Swing����� P472
18.4.1 �򵥵���
��������ֱ�Ӵ����ģ��м�����ǲ���Ĭ�Ϸ�ʽ�жϽڵ�ģ���������������Ϊ��һ���ڵ�B���Ľڵ�ͼ���ΪҶ�ӽڵ�ͼ�꣬�Ҳ�����ǲ��÷�Ĭ�Ϸ�ʽ�жϽڵ�ģ�����������Ϊ��һ���ӽڵ�B���Ľڵ�ͼ���Ϊ��Ҷ�ӽڵ�ͼ�ꡣ
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TreeTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public TreeTest() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("���ڵ�");
		DefaultMutableTreeNode nodeFirst = new DefaultMutableTreeNode("һ���ӽڵ�A");
		root.add(nodeFirst);
		
		DefaultMutableTreeNode nodeSecond = new DefaultMutableTreeNode("�����ӽڵ�", false);
		nodeFirst.add(nodeSecond);
		
		root.add(new DefaultMutableTreeNode("һ���ӽڵ�B"));
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

18.4.2 ����ѡ�нڵ��¼� P473


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
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("���ڵ�");
		DefaultMutableTreeNode nodeFirst = new DefaultMutableTreeNode("һ���ӽڵ�A");
		root.add(nodeFirst);
		
		DefaultMutableTreeNode nodeSecond = new DefaultMutableTreeNode("�����ӽڵ�", false);
		nodeFirst.add(nodeSecond);
		
		root.add(new DefaultMutableTreeNode("һ���ӽڵ�B"));
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


18.4.3 �������ڵ� P475
ǰ���������-��-��
�����������-��-��
�����������-��-��

�Թ�����ȱ�������������ȱ�����һ����Եı�����ʽ��
��Ϊ�������������������ȱ��������ֱ�����ʽ�ľ������������ͬ������ͼ18.46 ��ͼ18.48 ����ͬ�ģ�ʵ����depthFirstEnumeration()����ֻ�ǵ�����postorderEnumeration()������
//���ֱ�����ʽ P477
import java.awt.BorderLayout;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public TreeTest() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("���ڵ�");
		DefaultMutableTreeNode nodeFirst = new DefaultMutableTreeNode("һ���ӽڵ�A");
		root.add(nodeFirst);
		
		DefaultMutableTreeNode nodeSecond = new DefaultMutableTreeNode("�����ӽڵ�", false);
		nodeFirst.add(nodeSecond);
		
		root.add(new DefaultMutableTreeNode("һ���ӽڵ�B"));
		JTree treeRoot = new JTree(root);
		getContentPane().add(treeRoot, BorderLayout.CENTER);
		
		Enumeration<DefaultMutableTreeNode> enumeration;
		//enumeration = root.preorderEnumeration();//ǰ�����
		//enumeration = root.postorderEnumeration();//�������
		//enumeration = root.breadthFirstEnumeration();//������ȱ���
		//enumeration = root.depthFirstEnumeration();//������ȱ����������������һ��
		enumeration = root.children();//�����ҽڵ���ӽڵ㣬��ȡֱ���ӽڵ�
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

18.4.4 ������ P477
P478 ���÷ֲ㵼����
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
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("���ڵ�");
		DefaultMutableTreeNode nodeFirst = new DefaultMutableTreeNode("һ���ӽڵ�A");
		root.add(nodeFirst);
		
		DefaultMutableTreeNode nodeSecond = new DefaultMutableTreeNode("�����ӽڵ�", false);
		nodeFirst.add(nodeSecond);
		
		root.add(new DefaultMutableTreeNode("һ���ӽڵ�B"));
		JTree treeRoot = new JTree(root);
		getContentPane().add(treeRoot, BorderLayout.CENTER);
		
		treeRoot.setRootVisible(false);//����ʾ���ĸ��ڵ�
		treeRoot.setRowHeight(20);//���ڵ���и�Ϊ20����
		treeRoot.setFont(new Font("����", Font.BOLD, 14));//�������ڵ������
		treeRoot.putClientProperty("JTree.lineStyle", "None");//�ڵ�䲻����������
		DefaultTreeCellRenderer treeCellRenderer = (DefaultTreeCellRenderer) treeRoot.getCellRenderer();//������ڵ�Ļ��ƶ���
		treeCellRenderer.setLeafIcon(null);//����Ҷ�ӽڵ㲻����ͼ��
		treeCellRenderer.setClosedIcon(null);//���ýڵ��۵�ʱ������ͼ��
		treeCellRenderer.setOpenIcon(null);//���ýڵ�չ��ʱ������ͼ��
		Enumeration<DefaultMutableTreeNode> enumeration = root.preorderEnumeration();//��ǰ������������ڵ�
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

18.4.5 ά����ģ�� P479
//������Щ���⣬��̫�������������޸ĺ�ɾ����
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
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("���ڵ�");
		DefaultMutableTreeNode nodeFirst = new DefaultMutableTreeNode("һ���ӽڵ�A");
		root.add(nodeFirst);
		
		DefaultMutableTreeNode nodeSecond = new DefaultMutableTreeNode("�����ӽڵ�", false);
		nodeFirst.add(nodeSecond);
		
		root.add(new DefaultMutableTreeNode("һ���ӽڵ�B"));
		JTree treeRoot = new JTree(root);
		getContentPane().add(treeRoot, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JTextField textField = new JTextField(10);
		panel.add(textField);
		
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		
		final JButton addButton = new JButton("���");
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
		
		final JButton updButton = new JButton("�޸�");
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
		
		final JButton delButton = new JButton("ɾ��");
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

18.4.6 ����չ���ڵ��¼� P481
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
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("���ڵ�");
		DefaultMutableTreeNode nodeFirst = new DefaultMutableTreeNode("һ���ӽڵ�A");
		root.add(nodeFirst);
		
		DefaultMutableTreeNode nodeSecond = new DefaultMutableTreeNode("�����ӽڵ�", false);
		nodeFirst.add(nodeSecond);
		
		root.add(new DefaultMutableTreeNode("һ���ӽڵ�B"));
		JTree treeRoot = new JTree(root);
		getContentPane().add(treeRoot, BorderLayout.CENTER);
		
		treeRoot.addTreeWillExpandListener(new TreeWillExpandListener() {
			@Override
			public void treeWillExpand(TreeExpansionEvent e) {
				TreePath path = e.getPath();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				System.out.println("�ڵ�\"" + node + "\"��Ҫ��չ����");
			}
			
			@Override
			public void treeWillCollapse(TreeExpansionEvent e) {
				TreePath path = e.getPath();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				System.out.println("�ڵ�\"" + node + "\"��Ҫ���۵���");
			}
		});
		
		treeRoot.addTreeExpansionListener(new TreeExpansionListener() {
			@Override
			public void treeExpanded(TreeExpansionEvent e) {
				TreePath path = e.getPath();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				System.out.println("�ڵ�\"" + node + "\"�ѱ�չ����");
				System.out.println();
			}
			
			@Override
			public void treeCollapsed(TreeExpansionEvent e) {
				TreePath path = e.getPath();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				System.out.println("�ڵ�\"" + node + "\"�ѱ��۵���");
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

18.4.7 ����6��Ϊ���ڵ�������ʾ��Ϣ P483
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
				DefaultMutableTreeNode root = new DefaultMutableTreeNode("���տƼ�����");
				DefaultMutableTreeNode parent1 = new DefaultMutableTreeNode("�����ŵ���ͨϵ��");
				parent1.add(new DefaultMutableTreeNode("��Java�����ŵ���ͨ��"));
				parent1.add(new DefaultMutableTreeNode("��PHP�����ŵ���ͨ��"));
				parent1.add(new DefaultMutableTreeNode("��Visual Basic�����ŵ���ͨ��"));
				parent1.add(new DefaultMutableTreeNode("��Visual C++�����ŵ���ͨ��"));
				root.add(parent1);
				
				DefaultMutableTreeNode parent2 = new DefaultMutableTreeNode("��̴ʵ�ϵ��");
				parent2.add(new DefaultMutableTreeNode("��Java��̴ʵ䡷"));
				parent2.add(new DefaultMutableTreeNode("��PHP��̴ʵ䡷"));
				parent2.add(new DefaultMutableTreeNode("��Visual Basic��̴ʵ䡷"));
				parent2.add(new DefaultMutableTreeNode("��Visual C++��̴ʵ䡷"));
				root.add(parent2);
				
				DefaultTreeModel model = new DefaultTreeModel(root);
				tree.setModel(model);
				ToolTipManager.sharedInstance().registerComponent(tree);//Ϊ��ע����ʾ��Ϣ������
				Map<DefaultMutableTreeNode, String> map = new HashMap<DefaultMutableTreeNode, String>();
				map.put(root, "���տƼ�");
				map.put(parent1, "���ӹ�ҵ");
				map.put(parent2, "�������");
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
		renderer.setToolTipText("<html><font face=΢���ź� size=16 color=red>" + map.get(value) + "</font></html>");
		return renderer;
	}
}

18.4.8 ����7��˫���༭���ڵ㹦�� P484
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
				DefaultMutableTreeNode root = new DefaultMutableTreeNode("���տƼ�����");
				DefaultMutableTreeNode parent1 = new DefaultMutableTreeNode("�����ŵ���ͨϵ��");
				parent1.add(new DefaultMutableTreeNode("��Java�����ŵ���ͨ��"));
				parent1.add(new DefaultMutableTreeNode("��PHP�����ŵ���ͨ��"));
				parent1.add(new DefaultMutableTreeNode("��Visual Basic�����ŵ���ͨ��"));
				parent1.add(new DefaultMutableTreeNode("��Visual C++�����ŵ���ͨ��"));
				root.add(parent1);
				
				DefaultMutableTreeNode parent2 = new DefaultMutableTreeNode("��̴ʵ�ϵ��");
				parent2.add(new DefaultMutableTreeNode("��Java��̴ʵ䡷"));
				parent2.add(new DefaultMutableTreeNode("��PHP��̴ʵ䡷"));
				parent2.add(new DefaultMutableTreeNode("��Visual Basic��̴ʵ䡷"));
				parent2.add(new DefaultMutableTreeNode("��Visual C++��̴ʵ䡷"));
				root.add(parent2);
				
				DefaultTreeModel model = new DefaultTreeModel(root);
				tree.setModel(model);
				ToolTipManager.sharedInstance().registerComponent(tree);//Ϊ��ע����ʾ��Ϣ������
				Map<DefaultMutableTreeNode, String> map = new HashMap<DefaultMutableTreeNode, String>();
				map.put(root, "���տƼ�");
				map.put(parent1, "���ӹ�ҵ");
				map.put(parent2, "�������");
				tree.setCellRenderer(new ToolTipNode(map));
				
				JTextField textField = new JTextField();
				textField.setFont(new Font("΢���ź�", Font.PLAIN, 16));
				TreeCellEditor editor = new DefaultCellEditor(textField);
				tree.setEditable(true);
				tree.setCellEditor(editor);
				tree.setShowsRootHandles(true);//����������ϸ��ڵ����ʾ�ֱ�����û�а취�����ڵ��۵��ˣ���Ϊ˫����༭�ı��� P477
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
		renderer.setToolTipText("<html><font face=΢���ź� size=16 color=red>" + map.get(value) + "</font></html>");
		return renderer;
	}
}

18.5.2 ���䷶��2�������ڵ��ѡ���¼� P486
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
				DefaultMutableTreeNode root = new DefaultMutableTreeNode("���տƼ�����");
				DefaultMutableTreeNode parent1 = new DefaultMutableTreeNode("�����ŵ���ͨϵ��");
				parent1.add(new DefaultMutableTreeNode("��Java�����ŵ���ͨ��"));
				parent1.add(new DefaultMutableTreeNode("��PHP�����ŵ���ͨ��"));
				parent1.add(new DefaultMutableTreeNode("��Visual Basic�����ŵ���ͨ��"));
				parent1.add(new DefaultMutableTreeNode("��Visual C++�����ŵ���ͨ��"));
				root.add(parent1);
				
				DefaultMutableTreeNode parent2 = new DefaultMutableTreeNode("��̴ʵ�ϵ��");
				parent2.add(new DefaultMutableTreeNode("��Java��̴ʵ䡷"));
				parent2.add(new DefaultMutableTreeNode("��PHP��̴ʵ䡷"));
				parent2.add(new DefaultMutableTreeNode("��Visual Basic��̴ʵ䡷"));
				parent2.add(new DefaultMutableTreeNode("��Visual C++��̴ʵ䡷"));
				root.add(parent2);
				
				DefaultTreeModel model = new DefaultTreeModel(root);
				tree.setModel(model);
				ToolTipManager.sharedInstance().registerComponent(tree);//Ϊ��ע����ʾ��Ϣ������
				Map<DefaultMutableTreeNode, String> map = new HashMap<DefaultMutableTreeNode, String>();
				map.put(root, "���տƼ�");
				map.put(parent1, "���ӹ�ҵ");
				map.put(parent2, "�������");
				tree.setCellRenderer(new ToolTipNode(map));
				
				JTextField textField = new JTextField();
				textField.setFont(new Font("΢���ź�", Font.PLAIN, 16));
				TreeCellEditor editor = new DefaultCellEditor(textField);
				tree.setEditable(true);
				tree.setCellEditor(editor);
				tree.setShowsRootHandles(true);//����������ϸ��ڵ����ʾ�ֱ�����û�а취�����ڵ��۵��ˣ���Ϊ˫����༭�ı��� P477
				
				tree.addTreeSelectionListener(new TreeSelectionListener() {
					@Override
					public void valueChanged(TreeSelectionEvent e) {
						TreePath path = tree.getSelectionPath();
						if (path == null) {
							return;
						}
						
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
						String text1 = "��Java�����ŵ���ͨ��\n��PHP�����ŵ���ͨ��\n��Visual Basic�����ŵ���ͨ��\n��Visual C++�����ŵ���ͨ��";
						String text2 = "��Java��̴ʵ䡷\n��PHP��̴ʵ䡷\n��Visual Basic��̴ʵ䡷\n��Visual C++��̴ʵ䡷";
						
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
		renderer.setToolTipText("<html><font face=΢���ź� size=16 color=red>" + map.get(value) + "</font></html>");
		return renderer;
	}
}
//------------------------------------------------------------------------------------------------
//Java�����ŵ���ͨ ��19�� �߼����ֹ����� P488
19.1 ��ʽ���ֹ����� P489
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
		topicBox.add(Box.createHorizontalStrut(5));//���һ��5���ؿ��ˮƽ֧��
		JLabel topicLabel = new JLabel("���⣺");
		topicBox.add(topicLabel);
		topicBox.add(Box.createHorizontalStrut(5));
		JTextField topicTextField = new JTextField(30);
		topicBox.add(topicTextField);
		
		//�������ݶ��ɴ�ֱbox����
		Box box = Box.createVerticalBox();
		getContentPane().add(box, BorderLayout.CENTER);
		box.add(Box.createVerticalStrut(5));
		
		Box contentBox = Box.createHorizontalBox();
		contentBox.setAlignmentX(1);//���������ˮƽ����ֵ�����Ҳ���룻0�ǿ�������
		box.add(contentBox);
		
		contentBox.add(Box.createHorizontalStrut(5));//createHorizontalGlue()��������Ч��
		
		JLabel contentLabel = new JLabel("���ݣ�");
		contentLabel.setAlignmentY(0);
		contentBox.add(contentLabel);
		contentBox.add(Box.createHorizontalStrut(5));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setAlignmentY(0);
		contentBox.add(scrollPane);
		
		JTextArea contentTextArea = new JTextArea();
		scrollPane.setViewportView(contentTextArea);
		box.add(Box.createVerticalStrut(5));
		JButton submitButton = new JButton("ȷ��");
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

19.2 ��Ƭ���ֹ����� P491
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
		setTitle("ʹ�ÿ�Ƭ���ֹ�����");
		setBounds(100, 100, 600, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);//����һ�����ÿ�Ƭ���ֹ�������������
		getContentPane().add(cardPanel, BorderLayout.CENTER);
		String labelNames[] = {"Card A", "Card B", "Card C"};
		for (int i = 0; i < labelNames.length; ++i) {
			final JLabel label = new JLabel(labelNames[i]);
			cardPanel.add(label, labelNames[i]);//����ÿ�Ƭ���ֹ��������������ӿ�Ƭ
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

19.3 �����鲼�ֹ����� P493
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
		setTitle("ʹ�ý�����");
		setBounds(100, 100, 500, 375);
		setBounds(100, 100, 266, 132);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JLabel label = new JLabel();
		label.setForeground(new Color(255, 0, 0));
		label.setFont(new Font("", Font.BOLD, 16));
		label.setText("��ӭʹ�������������ܣ�");
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		getContentPane().add(label, gridBagConstraints);
		
		final JProgressBar progressBar = new JProgressBar();// ��������������
		progressBar.setStringPainted(true);// ������ʾ��ʾ��Ϣ
		progressBar.setIndeterminate(true);// ���ò��ò�ȷ��������
		progressBar.setString("����������......");// ������ʾ��Ϣ
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints_1.gridy = 1;
		gridBagConstraints_1.gridx = 0;
		getContentPane().add(progressBar, gridBagConstraints_1);
		
		final JButton button = new JButton();
		button.setText("���");
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
		new Progress(progressBar, button).start();// �����߳�ģ��һ��������������
	}
	
	class Progress extends Thread {// �����߳�ģ��һ��������������
		private final int[] progressValue = { 6, 18, 27, 39, 51, 66, 81,
				100 };// ģ��������ɰٷֱ�
		private JProgressBar progressBar;// ����������
		private JButton button;// ��ɰ�ť����
		
		public Progress(JProgressBar progressBar, JButton button) {
			this.progressBar = progressBar;
			this.button = button;
		}
		
		public void run() {
			// ͨ��ѭ������������ɰٷֱ�
			for (int i = 0; i < progressValue.length; i++) {
				try {
					Thread.sleep(1000);// ���߳�����1��
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				progressBar.setValue(progressValue[i]);// ����������ɰٷֱ�
			}
			progressBar.setIndeterminate(false);// ���ò���ȷ��������
			progressBar.setString("������ɣ�");// ������ʾ��Ϣ
			button.setEnabled(true);// ���ð�ť����
		}
	}
}

//�������������P499����ôһ��һ�����øо�Ч�ʺܵͣ�
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GridBagLayoutTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public GridBagLayoutTest() {
		super();
		setTitle("ʹ�����񲼾ֹ�����");
		setBounds(100, 100, 600, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new GridBagLayout());
		
		final JButton button = new JButton("A");
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;//��ʼ��Ϊ��1��
		gridBagConstraints.gridx = 0;//��ʼ��Ϊ��1��
		//gridBagConstraints.insets = new Insets(0, 5, 0, 0);//�������������С����
		gridBagConstraints.weightx = 10;//��1�еķֲ���ʽΪ10%
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(button, gridBagConstraints);
		
		final JButton button_1 = new JButton("B");
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.gridy = 0;
		gridBagConstraints_1.gridx = 1;
		gridBagConstraints_1.insets = new Insets(0, 5, 0, 0);//�������������С����
		gridBagConstraints_1.weightx = 20;//��1�еķֲ���ʽΪ20%
		gridBagConstraints_1.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(button_1, gridBagConstraints_1);
		
		final JButton button_2 = new JButton("C");
		final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
		gridBagConstraints_2.gridy = 0;
		gridBagConstraints_2.gridx = 2;
		gridBagConstraints_2.gridheight = 2;//���ռ������
		gridBagConstraints_2.insets = new Insets(0, 5, 0, 0);
		gridBagConstraints_2.weightx = 30;//��1�еķֲ���ʽ Ϊ30%
		gridBagConstraints_2.fill = GridBagConstraints.BOTH;//ͬʱ��������Ŀ�Ⱥ͸߶�
		getContentPane().add(button_2, gridBagConstraints_2);
		
		final JButton button_3 = new JButton("D");
		final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
		gridBagConstraints_3.gridy = 0;
		gridBagConstraints_3.gridx = 3;
		gridBagConstraints_3.gridheight = 4;//���ռ������
		gridBagConstraints_3.insets = new Insets(0, 5, 0, 5);//������������Ҳ����С����
		gridBagConstraints_3.weightx = 40;//��1�еķֲ���ʽ Ϊ40%
		gridBagConstraints_3.fill = GridBagConstraints.BOTH;//ͬʱ��������Ŀ�Ⱥ͸߶�
		getContentPane().add(button_3, gridBagConstraints_3);
		
		final JButton button_4 = new JButton("E");
		final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
		gridBagConstraints_4.gridy = 1;
		gridBagConstraints_4.gridx = 0;
		gridBagConstraints_4.gridwidth = 2;//���ռ������
		gridBagConstraints_4.insets = new Insets(5, 0, 0, 0);//������������Ҳ����С����
		gridBagConstraints_4.fill = GridBagConstraints.HORIZONTAL;//ͬʱ��������Ŀ�Ⱥ͸߶�
		getContentPane().add(button_4, gridBagConstraints_4);
		
		final JButton button_5 = new JButton("F");
		final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
		gridBagConstraints_5.gridy = 2;
		gridBagConstraints_5.gridx = 0;
		gridBagConstraints_5.insets = new Insets(5, 0, 0, 0);//������������Ҳ����С����
		gridBagConstraints_5.fill = GridBagConstraints.HORIZONTAL;//ͬʱ��������Ŀ��
		getContentPane().add(button_5, gridBagConstraints_5);
		
		final JButton button_6 = new JButton("G");
		final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
		gridBagConstraints_6.gridy = 2;
		gridBagConstraints_6.gridx = 1;
		gridBagConstraints_6.gridwidth = 2;//���ռ������
		gridBagConstraints_6.gridheight = 2;//���ռ������
		gridBagConstraints_6.insets = new Insets(5, 5, 0, 0);//������������Ҳ����С����
		gridBagConstraints_6.fill = GridBagConstraints.BOTH;//ͬʱ��������Ŀ�Ⱥ͸߶�
		getContentPane().add(button_6, gridBagConstraints_6);
		
//		final JButton button_6 = new JButton("G");
//		final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
//		gridBagConstraints_6.gridy = 2;
//		gridBagConstraints_6.gridx = 1;
//		gridBagConstraints_6.gridwidth = 2;//���ռ������
//		gridBagConstraints_6.gridheight = 2;//���ռ������
//		gridBagConstraints_6.insets = new Insets(5, 5, 0, 0);//��������ϲ���Ҳ����С����
//		gridBagConstraints_6.fill = GridBagConstraints.VERTICAL;//ͬʱ��������Ŀ�Ⱥ͸߶�
//		gridBagConstraints_6.ipadx = 30;//�����������ѡ��ȣ���Ĭ�ϻ����Ͽ������valueֵ������Ϊ��
//		gridBagConstraints_6.anchor = GridBagConstraints.EAST;//��ʾ�ڶ���
//		getContentPane().add(button_6, gridBagConstraints_6);
		
		final JButton button_7 = new JButton("G");
		final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
		gridBagConstraints_7.gridy = 3;
		gridBagConstraints_7.gridx = 0;
		gridBagConstraints_7.insets = new Insets(5, 0, 0, 0);//������������Ҳ����С����
		gridBagConstraints_7.fill = GridBagConstraints.HORIZONTAL;//ͬʱ��������Ŀ�Ⱥ͸߶�
		getContentPane().add(button_7, gridBagConstraints_7);
	}
	
	public static void main(String args[]) {
		new GridBagLayoutTest();
	}
}


19.4 ���ɲ��ֹ����� P499
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
		setTitle("ʹ�õ��ɲ��ֹ�����");
		setBounds(100, 100, 600, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SpringLayout springLayout = new SpringLayout();
		Container contentPane = getContentPane();
		contentPane.setLayout(springLayout);//�޸Ĵ�������Ϊ���õ��ɲ��ֹ�����
		
		JLabel topicLabel = new JLabel("���⣺");
		contentPane.add(topicLabel);
		springLayout.putConstraint(SpringLayout.NORTH, topicLabel, 5, SpringLayout.NORTH, contentPane);//�����ǩ����->��������
		springLayout.putConstraint(SpringLayout.WEST, topicLabel, 5, SpringLayout.WEST, contentPane);//�����ǩ����->��������
		
		JTextField topicTextField = new JTextField();
		contentPane.add(topicTextField);
		//������������Ϊ��һ������(para2)����һ��(para1)���ڶ�������(p5)����һ��(p4)�ľ���(����Ϊ->������Ϊ<-)
		springLayout.putConstraint(SpringLayout.NORTH, topicTextField, 5, SpringLayout.NORTH, contentPane);//�����ı��򱱲�->��������
		springLayout.putConstraint(SpringLayout.WEST, topicTextField, 5, SpringLayout.EAST, topicLabel);//�����ı�������->��������
		springLayout.putConstraint(SpringLayout.EAST, topicTextField, -5, SpringLayout.EAST, contentPane);//�����ı��򶫲�->��������
		
		JLabel contentLabel = new JLabel("���ݣ�");
		contentPane.add(contentLabel);
		springLayout.putConstraint(SpringLayout.NORTH, contentLabel, 5, SpringLayout.SOUTH, topicTextField);
		springLayout.putConstraint(SpringLayout.WEST, contentLabel, 5, SpringLayout.WEST, contentPane);
		
		JScrollPane contentScrollPane = new JScrollPane();
		contentScrollPane.setViewportView(new JTextArea());
		contentPane.add(contentScrollPane);
		springLayout.putConstraint(SpringLayout.NORTH, contentScrollPane, 5, SpringLayout.SOUTH, topicTextField);
		springLayout.putConstraint(SpringLayout.WEST, contentScrollPane, 5, SpringLayout.EAST, contentLabel);
		springLayout.putConstraint(SpringLayout.EAST, contentScrollPane, -5, SpringLayout.EAST, contentPane);
		
		JButton resetButton = new JButton("���");
		contentPane.add(resetButton);
		springLayout.putConstraint(SpringLayout.SOUTH, resetButton, -5, SpringLayout.SOUTH, contentPane);
		
		JButton submitButton = new JButton("ȷ��");
		contentPane.add(submitButton);
		springLayout.putConstraint(SpringLayout.SOUTH, submitButton, -5, SpringLayout.SOUTH, contentPane);
		springLayout.putConstraint(SpringLayout.EAST, submitButton, -5, SpringLayout.EAST, contentPane);
		springLayout.putConstraint(SpringLayout.SOUTH, contentScrollPane, -5, SpringLayout.NORTH, submitButton);
		springLayout.putConstraint(SpringLayout.EAST, resetButton, -5, SpringLayout.WEST, submitButton);//���������仰����resetButton����Ĭ��������ࡣ���-5��д��5�����ʹ�����������
		//��Ϊ�¾佫ʹ��հ�ť������࣬ȷ����ť�������������
		//springLayout.putConstraint(SpringLayout.WEST, submitButton, 5, SpringLayout.EAST, resetButton);//���������仰����resetButton����Ĭ���������
		
		validate();//������仰�޷�ˢ�³���
	}
	
	public static void main(String args[]) {
		new SpringLayoutTest();
	}
}

19.4.2 ʹ�õ��ɺ�֧�� P502

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Spring;
import javax.swing.SpringLayout;

public class SpringLayoutTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public SpringLayoutTest() {
		super();
		setTitle("ʹ�õ��ɲ��ֹ�����");
		setBounds(100, 100, 600, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SpringLayout springLayout = new SpringLayout();
		Container contentPane = getContentPane();
		contentPane.setLayout(springLayout);//�޸Ĵ�������Ϊ���õ��ɲ��ֹ�����
		
		Spring vST = Spring.constant(20);//����һ��֧��
		Spring hSP = Spring.constant(20, 100, 500);//����һ������
		
		JButton lButton = new JButton("��ťL");
		contentPane.add(lButton);
		springLayout.putConstraint(SpringLayout.NORTH, lButton, vST, SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.WEST, lButton, hSP, SpringLayout.WEST, contentPane);
		
		JButton rButton = new JButton("��ťR");
		contentPane.add(rButton);
		springLayout.putConstraint(SpringLayout.NORTH, rButton, 0, SpringLayout.NORTH, lButton);//������ʾrButton��lButton������ľ���һ�£���������ã���rButton�����������
		springLayout.putConstraint(SpringLayout.EAST, rButton, Spring.scale(hSP, 10), SpringLayout.EAST, lButton);//�м���밴�߾����������
		springLayout.putConstraint(SpringLayout.EAST, contentPane, hSP, SpringLayout.EAST, rButton);
	}
	
	public static void main(String args[]) {
		new SpringLayoutTest();
	}
}

19.4.3 ���õ��ɿ��������С P503
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
		setTitle("ʹ�õ��ɲ��ֹ�����");
		setBounds(100, 100, 600, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SpringLayout springLayout = new SpringLayout();
		Container contentPane = getContentPane();
		contentPane.setLayout(springLayout);//�޸Ĵ�������Ϊ���õ��ɲ��ֹ�����
		
		Spring vST = Spring.constant(20);//����һ��֧��
		Spring hSP = Spring.constant(20, 100, 500);//����һ������
		
		JButton lButton = new JButton("��ťL");
		contentPane.add(lButton);
		springLayout.putConstraint(SpringLayout.NORTH, lButton, vST, SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.WEST, lButton, hSP, SpringLayout.WEST, contentPane);
		
		JButton rButton = new JButton("��ťR");
		contentPane.add(rButton);
		springLayout.putConstraint(SpringLayout.NORTH, rButton, 0, SpringLayout.NORTH, lButton);//������ʾrButton��lButton������ľ���һ�£���������ã���rButton�����������
		springLayout.putConstraint(SpringLayout.EAST, rButton, Spring.scale(hSP, 10), SpringLayout.EAST, lButton);//�м���밴�߾����������
		springLayout.putConstraint(SpringLayout.EAST, contentPane, hSP, SpringLayout.EAST, rButton);
		
		//�õ��ɺ�֧���ֱ�������ð�ťΪ��ȿ����죬�߶Ȳ���
		Spring widthSP = Spring.constant(60, 300, 600);//����һ������
		Spring heightST = Spring.constant(60);//����һ��֧��
		
		Constraints lButtonCons = springLayout.getConstraints(lButton);//��ȡ����ťL����Constraints����
		lButtonCons.setWidth(widthSP);//���ÿ��������ȵĵ���
		lButtonCons.setHeight(heightST);//���ÿ�������߶ȵ�֧��
		
		Constraints rButtonCons = springLayout.getConstraints(rButton);//��ȡ����ťR����Constraints����
		rButtonCons.setWidth(widthSP);
		rButtonCons.setHeight(heightST);
	}
	
	public static void main(String args[]) {
		new SpringLayoutTest();
	}
}

19.5.1 ����Բ�β��ֹ����� P504

19.5.1 �������ݲ��ֹ����� P505
//------------------------------------------------------------------------------------------------
//Java�����ŵ���ͨ ��20�� �߼����ֹ����� P488 AWT��ͼ���� P508
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
		this.setTitle("��ͼʵ��");
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
			g.drawOval(120, 70, OVAL_WIDTH, OVAL_HEIGHT);//g.fillOval(120, 70, OVAL_WIDTH, OVAL_HEIGHT);//ʵ����Բ
		}
	}
}

Graphics�ೣ�õ�ͼ�λ��Ʒ��� P510

20.1.2 Graphics2D P511
Graphics2D�̳�Graphics�࣬ʵ���˹��ܸ�ǿ��Ļ�ͼ�����ļ��ϡ�
Graphics2D���Ƽ�ʹ�õĻ�ͼ�࣬���ǳ���������ṩ�Ļ�ͼ��������Graphics���ʵ��������ʱӦ��ʹ��ǿ������ת������ת��ΪGraphics2D���͡�
���磺
public void paint(Graphics g) {
	Graphics2D g2 = (Graphics2D) g;//ǿ�ƻ���ת��ΪGraphics2D����
	g2.����
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
		this.setTitle("��ͼʵ��2");
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
			shapes[0] = new Ellipse2D.Double(5, 5, 100, 100);//Բ�Σ�ǰ���������ֱ�Ϊ�������꣬����������Ϊ������ֵ��һ����ΪԲ����һ����Ϊ��Բ
			shapes[1] = new Rectangle2D.Double(110, 5, 100, 100);//����
			shapes[2] = new Rectangle2D.Double(15, 15, 80, 80);//����
			shapes[3] = new Ellipse2D.Double(120, 15, 80, 80);//Բ��
			for (Shape shape : shapes) { //����ͼ������
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

20.1.3 ����ָ���Ƕȵ�������� P513
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
		setContentPane(new DrawSectorPanel());//add(new DrawSectorPanel());Ч��һ��
		this.setTitle("��ͼʵ��");
	}
	
	public static void main(String args[]) {
		new DrawSector().setVisible(true);;
	}
	
	class DrawSectorPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			//super.paint(g);
			g.fillArc(40, 20, 80, 80, 0, 150);//6�������ֱ�Ϊ������x������y��width��height����ʼ�Ƕȣ��Ƕȷ�Χ
			g.fillArc(140, 20, 80, 80, 180, -150);
			g.fillArc(40, 40, 80, 80, 0, -110);
			g.fillArc(140, 40, 80, 80, 180, 110);
		}
	}
}

20.1.4 ���ƶ���� P513
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
		this.setTitle("��ͼʵ��");
	}
	
	public static void main(String args[]) {
		new DrawSector().setVisible(true);;
	}
	
	class DrawPolygonPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			int x1[] = {100, 120, 180, 140, 150, 100, 50, 60, 20, 80};//����εĺ����꣬�����յ������λ��
			int y1[] = {20, 85, 90, 120, 180, 140, 180, 120, 90, 85};//����ε�������
			int n1 = 10;//����εı������������������ƥ���ϵ�
			g.fillPolygon(x1, y1, n1);//�����

			//g.setColor(Color.RED);������ɫΪ��ɫ�����������ɫ�ͱ߿���ɫ��֮���������ɫ�������ٴ�setColor����
			
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

20.2 ��ͼ��ɫ��ʻ����� P514
20.2.3 Ϊͼ����佥��ɫ P517
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
		this.setTitle("��ͼʵ��");
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

20.2.4 ���ñʻ��Ĵ�ϸ P517
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
		this.setTitle("��ͼʵ��");
	}
	
	public static void main(String args[]) {
		new DrawSector().setVisible(true);;
	}
	
	class ChangeStrokeWidthPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			
			BasicStroke stroke = new BasicStroke(1);//���Ϊ1�ıʻ�����
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

20.3 �����ı� P518

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
		font = new Font("����", Font.BOLD, 16);
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
			g2.drawString("����ʱ����", 20, 30);
			g2.drawString(String.format("%tr", date), 50, 60);
		}
	}
}

20.3.3 �����ı������� P520
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
		this.setTitle("��ͼʵ��");
	}
	
	public static void main(String args[]) {
		new TextFontFrame().setVisible(true);;
	}
	
	class ChangeTextFontPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			String value = "���ձ�̴ʵ�����";
			int x = 40;
			int y = 50;
			Font font = new Font("�����п�", Font.BOLD + Font.ITALIC, 26);
			g.setFont(font);
			g.drawString(value, x, y);
			
			value = "http://community.mrbccd.com";
			x = 10;
			y = 100;
			font = new Font("����", Font.BOLD, 20);
			g.setFont(font);
			g.drawString(value, x, y);
		}
	}
}


20.3.4 �����ı���ͼ�κ���ɫ P521
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
		this.setTitle("��ͼʵ��");
	}
	
	public static void main(String args[]) {
		new TextFontFrame().setVisible(true);;
	}
	
	class TextAndShapeColorPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void paint(Graphics g) {
			String value = "ֻҪŬ����������";
			int x = 60;
			int y = 60;
			Color color = new Color(255, 0, 0);
			g.setColor(color);
			g.drawString(value, x, y);
			
			value = "һ�н��п���";
			x = 140;
			y = 100;
			color = new Color(0, 0, 255);
			g.setColor(color);
			g.drawString(value, x, y);
			
			color = Color.ORANGE;
			g.setColor(color);
			g.drawRoundRect(40, 30, 200, 100, 40, 30);//��Ȧ
			g.drawRoundRect(45, 35, 190, 90, 36, 26);//��Ȧ
		}
	}
}


20.4 ͼƬ���� P522

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
		this.setTitle("����ͼƬ");
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
			g2.drawImage(img, 0, 0, this);//�ĸ�������Ҫ��ʾ��ͼƬ����ˮƽλ�ã���ֱλ�ã�Ҫ֪ͨ��ͼ��۲���
		}
	}
}

20.4.2 �Ŵ�����С P523

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
		initialize(); // ���ó�ʼ������
	}
	// �����ʼ������
	private void initialize() {
		URL imgUrl = ImageZoom.class.getResource("sjtu.jpg");// ��ȡͼƬ��Դ��·��
		img = Toolkit.getDefaultToolkit().getImage(imgUrl);// ��ȡͼƬ��Դ
		canvas = new MyCanvas();
		this.setBounds(100, 100, 800, 600); // ���ô����С��λ��
		this.setContentPane(getContentPanel()); // �����������
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô���ر�ģʽ
		this.setTitle("����ͼƬ"); // ���ô������
	}
	// �������Ĳ���
	private JPanel getContentPanel() { //������д�ķ�������JFrame��getContentPane()��һ��
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(getJSlider(), BorderLayout.SOUTH);
			contentPanel.add(canvas, BorderLayout.CENTER);
		}
		return contentPanel;
	}
	// ��ȡ�������
	private JSlider getJSlider() {
		if (jSlider == null) {
			jSlider = new JSlider();
			jSlider.setMaximum(1000);
			jSlider.setValue(100);
			jSlider.setMinimum(1);
			jSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					canvas.repaint();//������paint()����
				}
			});
		}
		return jSlider;
	}
	// ������
	public static void main(String[] args) {
		new ImageZoom().setVisible(true);
	}
	// ������
	class MyCanvas extends Canvas {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			int newW = 0, newH = 0;
			imgWidth = img.getWidth(this); // ��ȡͼƬ���
			imgHeight = img.getHeight(this); // ��ȡͼƬ�߶�
			float value = jSlider.getValue();// ���������ȡֵ
			newW = (int) (imgWidth * value / 100);// ����ͼƬ�Ŵ��Ŀ��
			newH = (int) (imgHeight * value / 100);// ����ͼƬ�Ŵ��ĸ߶�
			g.drawImage(img, 0, 0, newW, newH, this);// ����ָ����С��ͼƬ
		}
	}
}

repaint()����������paint()������ʵ������򻭰���ػ����ܣ������ڽ���ˢ��

20.4.3 ͼƬ��ת P525

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
		dx2 = sx2 = MaxWidth; // ��ʼ��ͼ���С
		dy2 = sy2 = MaxHeight;
		initialize(); // ���ó�ʼ������
	}
	
	// �����ʼ������
	private void initialize() {
		URL imgUrl = PartImage.class.getResource("sjtu.jpg");// ��ȡͼƬ��Դ��·��
		img = Toolkit.getDefaultToolkit().getImage(imgUrl); // ��ȡͼƬ��Դ
		this.setBounds(100, 100, MaxWidth, MaxHeight + 60); // ���ô����С��λ��
		this.setContentPane(getJPanel());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô���ر�ģʽ
		this.setTitle("ͼƬ��ת"); // ���ô������
	}
	
	// ��ȡ�������ķ���
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getControlPanel(), BorderLayout.SOUTH);
			jPanel.add(getMyCanvas1(), BorderLayout.CENTER);
		}
		return jPanel;
	}
	
	// ��ȡ��ť�������ķ���
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
	
	// ��ȡˮƽ��ת��ť
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("ˮƽ��ת");
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
	
	// ��ȡ��ֱ��ת��ť
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("��ֱ��ת");
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
	
	// ��ȡ�������
	private MyCanvas getMyCanvas1() {
		if (canvasPanel == null) {
			canvasPanel = new MyCanvas();
		}
		return canvasPanel;
	}
	
	// ������
	public static void main(String[] args) {
		new PartImage().setVisible(true);
	}
	
	// ����
	class MyCanvas extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, this);// ����ָ����С��ͼƬ
		}
	}
} // @jve:decl-index=0:visual-constraint="10,10"

20.4.4 ͼƬ��ת P527

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class RotateImage extends JFrame {
	private static final long serialVersionUID = 1L;
	private Image img;
	private MyCanvas canvasPanel = null;
	
	public RotateImage() {
		initialize(); // ���ó�ʼ������
	}
	
	// �����ʼ������
	private void initialize() {
		URL imgUrl = RotateImage.class.getResource("sjtu.jpg");// ��ȡͼƬ��Դ��·��
		img = Toolkit.getDefaultToolkit().getImage(imgUrl); // ��ȡͼƬ��Դ
		canvasPanel = new MyCanvas();
		add(canvasPanel);
		this.setBounds(100, 100, 400, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô���ر�ģʽ
		this.setTitle("ͼƬ��ת"); // ���ô������
	}
		
	// ������
	public static void main(String[] args) {
		new RotateImage().setVisible(true);
	}
	
	// ����
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


20.4.5 ͼƬ��б P528
import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class TiltImage extends JFrame {
	private static final long serialVersionUID = 1L;
	private Image img;
	private MyCanvas canvasPanel = null;
	
	public TiltImage() {
		initialize(); // ���ó�ʼ������
	}
	
	// �����ʼ������
	private void initialize() {
		URL imgUrl = TiltImage.class.getResource("sjtu.jpg");// ��ȡͼƬ��Դ��·��
		img = Toolkit.getDefaultToolkit().getImage(imgUrl); // ��ȡͼƬ��Դ
		canvasPanel = new MyCanvas();
		add(canvasPanel);
		this.setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô���ر�ģʽ
		this.setTitle("ͼƬ��б"); // ���ô������
	}
		
	// ������
	public static void main(String[] args) {
		new TiltImage().setVisible(true);
	}
	
	// ����
	class MyCanvas extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.shear(0.4, 0.2);//ͬʱˮƽ��б��������б
			g2.drawImage(img, 0, 0, 300, 200, this);
		}
	}
}

20.4.6 ͼ�εĽ����� P530
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

public class IntersectOperationImage extends JFrame {
	private static final long serialVersionUID = 1L;
	private IntersectOperationPanel jPanel = null;
	
	public IntersectOperationImage() {
		initialize(); // ���ó�ʼ������
	}
	
	// �����ʼ������
	private void initialize() {
		jPanel = new IntersectOperationPanel();
		add(jPanel);
		this.setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô���ر�ģʽ
		this.setTitle("ͼƬ��"); // ���ô������
	}
		
	// ������
	public static void main(String[] args) {
		new IntersectOperationImage().setVisible(true);
	}
	
	// ����
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


20.4.7 ͼ�ε�������� P530
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

public class ExclusiveOrOperationImage extends JFrame {
	private static final long serialVersionUID = 1L;
	private ExclusiveOrOperationPanel jPanel = null;
	
	public ExclusiveOrOperationImage() {
		initialize(); // ���ó�ʼ������
	}
	
	// �����ʼ������
	private void initialize() {
		jPanel = new ExclusiveOrOperationPanel();
		add(jPanel);
		this.setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô���ر�ģʽ
		this.setTitle("ͼƬ���"); // ���ô������
	}
		
	// ������
	public static void main(String[] args) {
		new ExclusiveOrOperationImage().setVisible(true);
	}
	
	// ����
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

20.5.1 ���ƻ��� P531
import java.awt.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

public class DrawFlowerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private DrawFlowerPanel drawFlowerPanel = null;
	
	public DrawFlowerFrame() {
		initialize(); // ���ó�ʼ������
	}
	
	// �����ʼ������
	private void initialize() {
		drawFlowerPanel = new DrawFlowerPanel();
		add(drawFlowerPanel);
		this.setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô���ر�ģʽ
		this.setTitle("���ƻ���"); // ���ô������
	}
		
	// ������
	public static void main(String[] args) {
		new DrawFlowerFrame().setVisible(true);
	}
	
	// ����
	class DrawFlowerPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			//ƽ�������ᣬ��(0, 0)����ԭ���Ƶ�����������λ��
			g2.translate(drawFlowerPanel.getWidth() / 2, drawFlowerPanel.getHeight() / 2);
			
			//������ɫ����
			Ellipse2D.Float ellipse = new Ellipse2D.Float(30, 0, 70, 20);
			Color color = new Color(0, 255, 0);
			g2.setColor(color);
			g2.fill(ellipse);
			int i = 0;
			while (i < 8) {
				g2.rotate(30);//g2.rotate(Math.toRadians(30));��ʱ������ǽǶȣ�����Ҫ12������ת��һȦ��360��=2Pi����
				g2.fill(ellipse);
				++i;
			}
			
			//���ƺ�ɫ����
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
			
			//���ƻ�ɫ����
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
			
			//���ƺ�ɫ���ĵ�
			color = new Color(255, 0, 0);
			g2.setColor(color);
			ellipse = new Ellipse2D.Float(-10, -10, 20, 20);
			g2.fill(ellipse);
		}
	}
}


20.5.2 ��������ͼ�� P532

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import javax.swing.*;

public class ArtDesignFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private ArtDesignPanel drawFlowerPanel = null;
	
	public ArtDesignFrame() {
		initialize(); // ���ó�ʼ������
	}
	
	// �����ʼ������
	private void initialize() {
		drawFlowerPanel = new ArtDesignPanel();
		add(drawFlowerPanel);
		this.setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô���ر�ģʽ
		this.setTitle("���ƻ���"); // ���ô������
	}
		
	// ������
	public static void main(String[] args) {
		new ArtDesignFrame().setVisible(true);
	}
	
	// ����
	class ArtDesignPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			//ƽ�������ᣬ��(0, 0)����ԭ���Ƶ�����������λ��
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
Eclipse�����Ŀ�ִ��jar��������������ֱ�����У�
java -jar ArtDesignFrame.jar
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��5�� Java�����﷨���
P168 �����ѱ�private���η��޶�Ϊ˽�еķ������Լ����а�����final���еķ���������Ĭ�ϵ���Ϊ��final�ġ�

P169 5.2.4 ���ʿ��Ʒ�
��һ���౻����Ϊpublicʱ�����;����˱��������е�����ʵĿ����ԣ�ֻҪ��Щ�������е����ڳ�����ʹ��import�������public�࣬�Ϳ��Է��ʺ���������ࡣÿ��Java��������඼������public�࣬Ҳ�ǻ�����ͬ��ԭ��
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
import mypackage.HelloJavaImpl;//�������˽���ཫ���ɼ����޷�����

public class Test
{
	public static void main(String args[])
	{
		HelloJava.show();
	}
}

P170 ��protected���εĳ�Ա�������Ա�3���������ã���������������ͬһ�����е������ࡢ���������еĸ�������ࡣʹ��protected���η�����Ҫ�����������������и�������������ʸ�����ض����ԡ�

Ĭ�Ϸ��ʿ��Ʒ�������ֻ�ܱ�ͬһ�����е�����ʺ����ã��������Ա��������е���ʹ�ã����ַ��������ֳ�Ϊ�������ԡ�
ͬ���������ڵı����򷽷����û�з��ʿ��Ʒ����޶���Ҳ�;��а������ԡ�

P180 ��ֵʹ�Ⱥţ�=����ߵ�ֵ�����ұߵ�ֵ����һ����ڻ����������ͣ���ǰ���int a��b�����Զ��׼��ġ����ڷǻ����������ͣ���Point���󣩣���ֵ�޸ĵ��Ƕ������ã������Ƕ�������ˣ�x��y����ͬһ���������Զ�xִ�е����з������yִ�еķ�����������ͬһ������
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
		
		System.out.println("�ı�λ��...");
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
������£�
a is 1
b is 2
x is java.awt.Point[x=0,y=0]
y is java.awt.Point[x=1,y=1]
�ı�λ��...
a is 3
b is 2
x is java.awt.Point[x=5,y=5]
y is java.awt.Point[x=5,y=5]

P188 ���򷽷�����һ��������������ʱ�����ݵ���һ��ֵ�������ݵ���һ������ʱ�������ô��ݡ�

import java.io.IOException;

public class Test
{
	public static void main(String args[]) throws IOException {
		char a;
		outer: //�����������ﶨ�壬����break outer��continue outer�޷�ʶ��
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					a = (char) System.in.read();
					if (a == 'b') {
						break outer;//ֱ����������ѭ��
					}
					if (a == 'c') {
						continue outer;//����outer��Ŵ�
					}
				}
			}
	}
}
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��6�� Java���������
//6.1 ��װ P203
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

		//main������HelloWorld�еĺ��������Կ���ֱ�ӷ�����˽�б���world���������������޷��ô˷�����������˽�б����������ڿ��ܵ�����¾�����ʹ�÷�������
		String value = new HelloWorld().world;
		System.out.println(value);
	}
}

//6.2 �̳� P204
6.2.1 �̳еĸ�������������
class Car
{
	int v;
	void drive()
	{
		System.out.println("Car �ٶȣ�" + v);
	}
}

class Bus extends Car
{
	int p;
	void carry()
	{
		System.out.println("Bus ���ˣ�" + p);
	}
	
	void sum()
	{
		System.out.println("Bus �ٶȣ�" + v);
		System.out.println("Bus ���ˣ�" + p);
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
������£�
Car �ٶȣ�60
Car �ٶȣ�40
Bus ���ˣ�20
Bus �ٶȣ�40
Bus ���ˣ�20

6.2.2 ���಻�ܷ���˽�г�Ա�뷽��
һ���������private�����ԱΪ����˽�У������ܱ�����������д�����ʣ��������ࡣ

6.2.3 ������������໥ת�� P207
���Խ���������ø�������Ķ���Ҳ������ͨ��ǿ������ת����������ת�����������͡�
Bus bus = new Bus();
Car car = bus;
Bus bus2 = (Bus)Car;

6.2.4 ʹ��this��super
��1��this
���õ���ı����򷽷�
��2��super
�����õ�ǰ�����ֱ�Ӹ����еĳ�Ա����������ֱ�Ӹ����б����صĸ����г�Ա���ݻ�����������������������ͬ��Ա����ʱ����
��3��this(ʵ��)
���õ�ǰ��Ĺ��캯��
��4��super(����)
���û����е�ĳһ�����캯��

6.2.5 ��̬���������ĸ��ǣ�override�������أ�overload������д��overwrite��

6.2.6 ���뱻�̳е��ࡪ�������ࣨabstract��
abstract class Car//ֻ�г�������ܶ�����󷽷������������abstract����ȱ��
{
	int v;
	abstract void drive();//�����abstract���Բ����壬�����Ҫ��drive()����ʵ�֣�����Ҫ��Car�����̳�
}

class Bus extends Car
{
	int p;
	void drive()//���ﲻ���ټ�abstract
	{
		System.out.println("Bus �ٶȣ�" + v);
	}
	
	void carry()
	{
		System.out.println("Bus ���ˣ�" + p);
	}
	
	void sum()
	{
		System.out.println("Bus �ٶȣ�" + v);
		System.out.println("Bus ���ˣ�" + p);
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

6.2.7 ���ܱ��̳е��ࡪ�������ࣨfinal��
1. final��Ķ���
--final�಻�ܱ��̳У�û�����࣬final���еķ���Ĭ����final��
--final�������ܱ�����ķ������ǣ������Ա��̳�
--final��Ա������ʾ������ֻ�ܱ���ֵһ�Σ���ֵ��ֵ���ٸı�
--final�����������ι��췽��
�����private��Ա�����ǲ��ܱ����෽�����ǵģ����private���͵ķ���Ĭ����final���͵ġ�����仰���Ǻܶԣ�
class Car
{
	int v;
	private void drive()//final private void drive()��������Ҳ���ԣ������������Ϊfinal public void drive()�������޷�����
	{
		System.out.println("Car drive");
	}
}

class Bus extends Car
{
	int p;
	void drive()//������������drive
	{
		System.out.println("Bus �ٶȣ�" + v);
	}
	
	void carry()
	{
		System.out.println("Bus ���ˣ�" + p);
	}
	
	void sum()
	{
		System.out.println("Bus �ٶȣ�" + v);
		System.out.println("Bus ���ˣ�" + p);
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

2. final�����Ķ��� P214
���һ���಻�������า��ĳ������������԰������������Ϊfinal������
ʹ��final����ԭ����������
--�ѷ�����������ֹ�κμ̳����޸����������ʵ�֡�
--��Ч������������������final����ʱ��ת����Ƕ���ƣ�������ִ��Ч�ʡ�
����final static���ͳ�Ա������staticʹ�ñ���ֻ����һ��������finalʹ�������ܸı䡣
3. ����final��Ա��ֵ�Ĺ涨
--final��Ա����ֻ�ܱ���ʼ��һ��
--final��Ա����������ʱ�����ڹ��췽���б���ʼ�����������������ĵط�����ʼ����

6.3 ���ؼ̳С����ӿ��� P214
Java���Թ淶�У�һ���������������������������֡���������Ŀ�����ͣ��������������ķ������͡����������ּ����׳������쳣����Java��������鷽��������ʱ���������Щ�����ж����������Ƿ������ط���������Java��������鷽����ת��ʱ������һ����������������ֳ����ͺ������ͣ��ķ���ֵ���ͺ��׳����쳣�Ƿ���ͬ��
�ӿڼ̳к�ʵ�ּ̳еĹ���ͬ��һ����ֻ��һ��ֱ�Ӹ��࣬������ʵ�ֶ���ӿڡ�

6.3.2 �ӿڵĴ�����ʵ��
�ӿ��������£�
--�ӿ��еķ��������в����б�ͷ������ͣ��������κη����塣
--�ӿ��п��԰����ֶΣ����ǻᱻ��ʽ������Ϊstatic��final��
--�ӿ��е��ֶ�ֻ�Ǳ��洢�ڸýӿڵľ�̬�洢�����ڣ��������ڸýӿڡ�
--�ӿ��еķ������Ա�����Ϊpublic����������������ᰴ��public���ʹ���
--ʵ��һ���ӿ�ʱ����Ҫ��������ķ�������Ϊpublic���ͣ�����Ϊ�������ͣ�Java���������������������
--���û��ʵ�ֽӿ��е����з�������ô��������Ȼ��һ���ӿڡ�
--��չһ���ӿ��������µĽӿ�Ӧʹ�ùؼ���extends��ʵ��һ���ӿ�ʹ��implements��
Java�ӿڿ�����public�ģ���̬�ĺ�final���ԡ�
public interface �ӿ���
{
	public static int a = 1;
	public final int b = 2;
}
����ͨ��implements�ؼ���ʵ�ֶ���ӿڣ���ô�������ʵ����Щ�ӿڵ����нӿڷ�����

6.3.3 �ӿ������������� P216
��1������������ṩʵ�ַ������ӿڲ����ṩ
��2��������ֻ�ܼ̳�һ����������ʵ�ֶ���ӿ�
��ϣ�1����2�����г������Java�ӿڵĸ������ƣ��������ģʽ�ͳ����ˣ��������͵Ĺ�����Ȼ��Java�ӿڳе�������ͬʱ����һ��Java�����࣬��ʵ���������ӿڣ�������ͬ��������������͵ľ��������ѡ��ʵ�����Java�ӿڣ�Ҳ����ѡ��̳���������࣬Ҳ����˵�ڲ�νṹ�У�Java�ӿ��������棬Ȼ������ų����࣬��ͽ����ߵ�����ŵ㶼�ܷ��ӵ������ˡ����ģʽ���ǡ�Ĭ������ģʽ����
//(1)���嶥��ӿ�
public interface ClassName1
{
	public void func1();
}
//(2)����ʵ�ֳ�����
public abstract class ClassName2
{
	public void func2()
	{
		
	}
}
//(3)����ʵ����
public class ClassName3 extends ClassName2 implements ClassName1
{
	public void func1()//ʵ�ֽӿں���
	{
		func2();//���ó����ຯ��
	}
}

�ӿ���ĳЩ�ط��ͳ����������Ƶĵط������ǲ������ַ�ʽ����������Ҫ�����������㣺
--���Ҫ���������κη�������ͳ�Ա�����Ļ��࣬��ô��Ӧ��ѡ��ӿڶ����ǳ����ࡣ
--���֪��ĳ����Ӧ���ǻ��࣬��ô��һ��ѡ���Ӧ����������Ϊһ���ӿڣ�ֻ���ڱ���Ҫ�з�������ͳ�Ա������ʱ�򣬲�Ӧ��ѡ������ࡣ��Ϊ���������������һ������������ʵ�ֵķ�����ֻҪ����û�б�ȫ��ʵ�ָ�������ǳ����ࡣ

6.4.4 �ϻ���ҵ P220
дһ����������ʵ�ּӣ������ˣ���
//��1���������ӿ���
package calculator;

public interface ICalculator
{
	public int calculate(String expression);
}

//��2��������������
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

//��3��������ʵ���ࣺ�ӡ������˳����Լ�Ĭ�ϼ�����
//�ӷ�������
package calculator.impl;

import calculator.ICalculator;
import calculator.AbstractCalculator;

public class Plus extends AbstractCalculator implements ICalculator
{
	@Override
	public int calculate(String expression)
	{
		int arrayInt[] = split(expression, "\\+");//String.split�����е������������ʽ��+��*�����⺬�壬����Ҫ�ý���ת��
		return arrayInt[0] + arrayInt[1];
	}
}

//����������
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

//�˷�������
package calculator.impl;

import calculator.ICalculator;
import calculator.AbstractCalculator;

public class Multiply extends AbstractCalculator implements ICalculator
{
	@Override
	public int calculate(String expression)
	{
		int arrayInt[] = split(expression, "\\*");//String.split�����е������������ʽ��+��*�����⺬�壬����Ҫ�ý���ת��
		return arrayInt[0] * arrayInt[1];
	}
}

//����������
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

//Ĭ�ϼ�����
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

//��4��������
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
			//String expression = System.console().readLine();//���е����ַ����׳��˿�ָ���쳣
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
//        if (console == null) //�����ܹ�������ʱÿ�ζ�Ϊnull�����쳣
//        {  
//            throw new IllegalStateException("Console is not available!");  
//        }  
//        return console.readLine(prompt);  
		Scanner scanner = new Scanner(System.in);//�и�scannerδclose����ʾ
		System.out.println(prompt);  
		return scanner.nextLine();  
	}
}
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��7�� Java�����������չ P225
7.1.1 ��̬����Ǿ�̬������� P225
��̬���඼��������ʱ��̬�ؼ��ص��ڴ��еģ�һ��ʼ�������ȴӾ�̬���࿪ʼ��������Ҳ����Ҫ�����ǽ��г�ʼ����Ҳû��ʵ�������������ڲ�Ҳ������this��
����Ǿ�̬�ڲ���Ļ�����̬�ڲ��಻�ܲ��������ⲿ���ݣ���̬�ڲ����ʵ�����ɲ���Ҫ���ⲿ��������ࡣ

7.1.2 ��̬����
����һ����̬�������ǡ�����.��������
һ����˵����̬��������ΪӦ�ó����е��������ṩһЩʵ�ù������ã���Java������д����ľ�̬�������ǳ��ڴ�Ŀ�Ķ�����ġ�

7.1.4 ��̬�����
static�����е�������C�е�ȫ�ֱ����ĸ��ֵ��̽�ֵ��Ǿ�̬�����ĳ�ʼ�����⡣
��̬�������ϵͳ����ʱ��ִ�����������static {�������һ�δ��룬����������ʽ�س�ʼ����̬��������δ���ֻ���ʼ��һ�Σ������౻��һ��װ��ʱ��
���⣬static����ı������������κ�������static��������������ֵ�˳����Ρ��������漰���̳е�ʱ�򣬻��ȳ�ʼ�������static������Ȼ��������ģ��Դ����ơ�
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
	
	//��̬�������ϵͳ����ʱִ��
	static
	{
		System.out.println("��ʼ����v1.c = " + v1.i + ", v2.i = " + v2.i);
		v1 = new StaticClass(27);
		System.out.println("��ʼ����v1.c = " + v1.i + ", v2.i = " + v2.i);
		v2 = new StaticClass(15);
		System.out.println("��ʼ����v1.c = " + v1.i + ", v2.i = " + v2.i);
	}
	
	public static void main(String args[])//main�������붨�������ļ�����һ�µ�public����
	{
		Test test = new Test();
		System.out.println("test.i = " + test.v.i);
		System.out.println("v1.c = " + v1.i + ", v2.i = " + v2.i);
		v1.inc();
		System.out.println("v1.c = " + v1.i + ", v2.i = " + v2.i);
		System.out.println("test.i = " + test.v.i);
	}
}
������£�
��ʼ����v1.c = 0, v2.i = 0
��ʼ����v1.c = 27, v2.i = 27
��ʼ����v1.c = 15, v2.i = 15
test.i = 10
v1.c = 10, v2.i = 10
v1.c = 11, v2.i = 11
test.i = 11


7.1.5 ��̬�ڲ���
static������ڲ����徲̬��������̬�����;�̬������ʹ�����Σ�������������class�Ķ��塣
ͨ��һ����ͨ�಻��������Ϊ��̬�ģ�ֻ��һ���ڲ���ſ��ԡ���ʱ�������Ϊ��̬���ڲ������ֱ����Ϊһ����ͨ����ʹ�ã�������Ҫʵ����һ���ⲿ�ࡣ
���´�����ʾ��������һ����̬�ڲ���InnerClass��ֻ��Ҫͨ�����ⲿ��.�ڲ��ࡱ����ʽ���ø���
class Test
{
	//����һ����̬�ڲ���
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
		Test.InnerClass ic = new Test.InnerClass();//���þ�̬�ڲ���
		ic.print("this is a string");
	}
}
������£�
InnerClass
this is a string

7.2 Java�����ࣨAnonymous Class�� P228
public class A
{
	public void f()
	{
		class B extends Thread//����һ���ڲ���
		{
			public void run() {...}//����ʵ��
		}

		class C implements Runnable//����һ���ڲ��ӿ�
		{
			public void run() {...}//����ʵ��
		}
		
		B b = new B();
		C c = new C();
	}
}
�ڷ������ٶ����ڲ��������Ե���׸����������Ĵ���Ϳ����������ڲ�����д�ɣ�
public class A
{
	public void f()
	{
		Thread b = new Thread()//�����ڲ���
		{
			public void run() {...}//����ʵ��
		}
		
		Runnable c = new Runnable()//�����ڲ��ӿ�
		{
			public void run() {...}//����ʵ��
		}
	}
}
�����ڲ���ͨ������GUI�������¼��������У�Ŀ���Ǽ򻯴����д��

7.2.1 ������Ķ��� P229
�Ӽ�����˵��������ɱ���Ϊ�Ǿ�̬���ڲ��࣬�������Ǿ��кͷ����ڲ������ķǾ�̬�ڲ���һ����Ȩ�޺����ơ����Ҫִ�е�������Ҫһ�����󣬵�ȴ��ֵ�ô���ȫ�µĶ���ԭ����������������ڼ򵥣�������������ֻ��һ�������ڲ�ʹ�ã�����������Ե÷ǳ����á������������ʺ���SwingӦ�ó����п��ٴ����¼��������
�������ǲ��������Ƶ��࣬����û�취�������ǡ������ڴ���ʱ����Ϊnew����һ�������������ǡ����Ҫ������һ����ʽ��new��䣬���£�
new<���ӿ�><�������>
������ʽ��new�������һ���µ������࣬����һ���������������չ������ʵ��һ�������Ľӿڡ����������Ǹ����һ����ʵ������������Ϊ���Ľ�������ء�Ҫ��չ�����Ҫʵ�ֵĽӿ���new���Ĳ��������������������塣������������һ���������չ������������Է�����ĳ�Ա���������ķ����ȣ���������κα�׼���඼��һ���ġ����������ʵ����һ���ӿڣ������������ʵ�ֽӿڵķ�����
��������������ڱ���ʱ���еģ�ʵ����������ʱ���С�����ζ��forѭ���е�һ��new���ᴴ����ͬ��������ļ���ʵ���������Ǵ���������ͬ�������һ��ʵ����

7.2.2 �������ʹ��ʵ��
7.2.3 ʹ�������ദ��Swing�¼�

7.3 Java�ڲ��ࣨInner Class��P231
�����࣬�����ڲ����һ�֡��ڲ��༴�Ƕ�������һ������ڲ����ࡣJava�ڲ��ࣨInner Class����ʵ�������Ƶĸ�����C++�����Ƕ���ࣨNested Class����

7.3.1 �ڲ���ĺ���
���ڲ��ࡱ������һ������ڲ��������ࡣ��Java1.1 ��ʼ������һ������������һ���ࡣ��װ���ڲ�����������ͳ�Ϊ���ⲿ�ࡱ��Java���Թ淶���������¼����ڲ��ࣺ
--����һ�������һ���ӿ�������һ����
--����һ���ӿڻ���һ����������һ���ӿڣ����Բ���ʵ�ֽӿ��е��ڲ��ӿڣ�
--��һ������������һ����
--��ͽӿ���������Ƕ���������

�ڲ�������һ������ڲ�Ƕ�׶�����࣬��������������ĳ�Ա��Ҳ������һ��������ڲ����壬�������ڱ��ʽ�ڲ��������塣
�ڲ������������ԣ�
--һ�����ڶ��������������֮�ڣ����ⲿ������ʱ����������������ƣ����ֲ������������������ͬ��
--����ʹ�ð���������ľ�̬��ʵ����Ա������Ҳ����ʹ�������ڵķ����ľֲ�������
--���Զ���Ϊabstract
--��������Ϊprivate��protected
--��������Ϊstatic���ͱ���˶����࣬������ʹ�þֲ�������
--������Inner Class�������κ�static��Ա���Inner Class��������Ϊstatic��

��1���ڲ���Ŀ��ӷ�Χ
�ڲ���Ŀ��ӷ�Χ������ֱ����Ƕ�ࣨ��һ���Ƕ�׾�̬�಻ͬ��Ƕ�׾�̬��������Ƕ��֮��Ҳ�ǿ��ӵģ���Ҳ����˵�ڲ������ֱ��������Ƕ���е��κγ�Ա����Ϊ�ڲ�������Ƕ���ʵ����أ������ڲ���ӵ������thisָ�룬һ��ָ���ڲ����ʵ������һ��ָ����Ƕ��ʵ����
��2��ʹ���ڲ���ĺô�
��װ�Ժã�ֱ��ͨ�������õ��ڲ������ݣ�����������ڲ������ֶ�û�п�����һ���ڲ��������Է��ʴ��������ⲿ�������ڲ�����������˽�б�����
��Java�е��ڲ���ͽӿڼ���һ�𣬿��Խ������C++����Ա��ԹJava�д��ڵ�һ�����⡪��û�ж�̳С�ʵ���ϣ�C++�Ķ�̳���������ܸ��ӣ���Javaͨ���ڲ������Ͻӿڣ����Ժܺõ�ʵ�ֶ�̳е�Ч����
��3���ڲ������;
�ڲ����һ����Ҫ��;������GUI���¼��������

7.3.2 �ڲ����ʹ��ʵ�� P232

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
	//�ʼ��ڲ��ڲ���
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
	
	//���յ�ַ�ڲ���
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
	public static void main(String args[])//main�������붨�������ļ�����һ�µ�public����
	{
		Mail mail = new Mail();
		IContent content = mail.getContent("�����ʼ�����");//ȡ���ʼ�����ʵ��
		IAddress address = mail.getAddress("bigben0204@163.com");//ȡ�ý��յ�ַʵ��
	}
}
������������Content��Address������������Mail�ڲ���������protected��private���η������Ʒ��ʼ����ں����mail�����ֱ����IContent content��IAddress address���в��������������������ڲ�������ֶ�û�п������������ڲ���ĵ�һ���ô������ֳ����ˡ����������㲻���ñ���֪���Ĳ������÷�װ�ԡ�
ͬʱ��Ҳ���������ⲿ�����÷�Χ֮��õ��ڲ�������һ��һ���������Ǿ����������ⲿ��ķ������������ء�


7.3.3 �ֲ��ڲ��� P233
ǰ�涨����ڲ����������ڲ�����ģ��������Ǿֲ��ģ��������Զ�����һ������������һ�������֮�ڡ�
���ڲ��ඨ����ں����ڲ�ʱ��classǰ���ܹ�ʹ��private��protected��public���η�����Ϊ��ʱ��������ڸô�������ÿɼ�������Ҫ���ⲿ��������Ȩ�ޡ�
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
		//�ʼ��ڲ��ڲ���
		class Content implements IContent//�������ɾֲ��࣬���������η�public/protected/private��ֻ����abstract/final
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
		//���յ�ַ�ڲ���
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
	public static void main(String args[])//main�������붨�������ļ�����һ�µ�public����
	{
		Mail mail = new Mail();
		IContent content = mail.getContent("�����ʼ�����");//ȡ���ʼ�����ʵ��
		IAddress address = mail.getAddress("bigben0204@163.com");//ȡ�ý��յ�ַʵ��
	}
}

�����Խ��ڲ�����ڴ�����У����s��Ϊ�������ڲ��ಢ����ʵ�������򷵻ؿա�������if֮�ⴴ������ڲ���Ķ�����Ϊ���Ѿ����������������򡣲����ڱ����ʱ���ڲ���Content��Address��������һ��ͬʱ�����룬ֻ���������Լ��������򣬳����������Χ����Ч������֮�����������ڲ��ಢû��ʲô����

7.3.4 �ڲ��������ⲿ����� P235
�����ڲ���ĺ����֪���ڲ���Ķ��󻹿��������ⲿ�������Ӳ�д�ˣ��ڲ��������ⲿ���˽�����ݳ�Ա������
һ���ڲ��������Է��ʴ��������ⲿ���������ݣ���������˽�б���������һ���ǳ����õ����ԡ�Ҫ��ʵ��������ܣ��ڲ������ͱ�����ָ���ⲿ���������á�Java�����������ڲ������ʱ����ʽ�ذ����ⲿ����������Ҳ���˽�ȥ��һֱ�����š�������ʹ���ڲ������ʼ�տ��Է������ⲿ�����ͬʱ��Ҳ��Ϊʲô���ⲿ�����÷�Χ֮��Ҫ�����ڲ����������ȴ������ⲿ������ԭ��
����ڲ������һ����Ա�������ⲿ���һ����Ա����ͬ�������ⲿ���ͬ����Ա�����������ˣ�Java�������¸�ʽ����ⲿ������ã�
�ⲿ����.this.�ⲿ������
��������ı��������ʹ����Address�ڲ�ͬ���ı���address����ô��Address�ڲ���Ҫʹ�á�Mail.this.address������������

�ڴ����Ǿ�̬�ڲ������ʱ��һ��Ҫ�ȴ�����Ӧ���ⲿ�������Ϊ�Ǿ�̬�ڲ����������ָ�����ⲿ���������á�

����ͨ����һ�����ڲ���Ҳ�����о�̬�ġ������ͷǾ�̬�ڲ�����ȣ���������ھ�̬�ڲ���û����ָ���ⲿ�����á���ʵ���Ϻ�C++�е�Ƕ����������ˣ�Java�ڲ�����C++Ƕ�������Ĳ�ͬ�������Ƿ���ָ���ⲿ��������һ���ϡ�
����֮�⣬���κηǾ�̬�ڲ����У��������о�̬���ݡ���̬������������һ����̬�ڲ��ࣨ�ڲ����Ƕ�׿��Բ�ֹһ�㣩��������̬�ڲ���ȴ����ӵ����һ�С�

7.4 Java�쳣�ࣨException Class�� P237
�ڱ�̹����У�����Ӧ��������ȥ���������쳣���������ڲ��ɱ��⡢����Ԥ�������������쳣����ʱ��δ���Java���쳣�Ĵ����ǰ��쳣���ദ��ģ���ͬ�쳣�в�ͬ�ķ��࣬ÿ���쳣����Ӧһ�����ͣ�class����ÿ���쳣����Ӧһ���쳣����ģ�����
--�쳣����������Դ��һ��Java���Ա������һЩ�����쳣���ͣ������û�ͨ���̳�Exception����������Լ�������쳣��
--�쳣�Ķ�����������Դ��һ��Java����ʱ�����Զ��׳�ϵͳ���ɵ��쳣�����������Ƿ�Ը�Ⲷ��ʹ�������Ҫ���׳��������Ϊ0���쳣�����ǳ���Ա�Լ��׳����쳣������쳣�����ǳ���Ա�Լ�����ģ�Ҳ������Java�����ж���ģ���throw�ؼ����׳��쳣�������쳣��������������߻㱨�쳣��һЩ��Ϣ���쳣����Է�����˵�ģ��׳��������׳�������ʹ����쳣�����ڷ����н��еġ�

7.4.1 Java���쳣��νṹ
Java���쳣��������������������һ������java.lang.Throwable��Ϊ�����쳣�ĳ��ࡣJava API�ж�����쳣���Ϊ�����࣬������Error���쳣Exception��
Throwable���������쳣�ʹ���ĳ��࣬������������Error��Exception���ֱ��ʾ������쳣�������쳣��Exception�ַ�Ϊ����ʱ�쳣��Runtime Exception���ͷ�����ʱ�쳣���������쳣�кܴ�����Ҳ��Ϊ������쳣��Unchecked Exception���ͼ���쳣��Checked Exception����
1. Error��Exception
--Error�ǳ����޷�����Ĵ��󣬱���OutOfMemory Error��ThreadDeath�ȡ���Щ�쳣����ʱ��Java�������JVM��һ���ѡ���߳���ֹ���������г��������ѭ�����ڴ�й©�ȡ������������������ʱ�����޷������ֻ��ͨ������������Ԥ��
--Exception�ǳ�������Դ�����쳣�������쳣��Ϊ����ʱ�쳣�ͷ�����ʱ�쳣���ڳ�����Ӧ��������ȥ������Щ�쳣���������г���Ϊ0������Խ��ȡ�������������������������������ʱ������Խ�������쳣��������������з���ʹ�����Կɼ�������ֱ��ֱ������������
2. ����ʱ�쳣�ͷ�����ʱ�쳣

ÿһ����ĺ���P230
��1��Throwable getCause()/getMessage()/printStackTrace()
��2��Exception
��3��Error
��4��RuntimeException
��5��ThreadDeath

--�����쳣�ࣺArithmeticException
--��ָ���쳣�ࣺNullPointerException
--����ǿ��ת���쳣��ClassCassException
--���鸺�±��쳣��NegativeArrayException
--�����±�Խ���쳣��ArrayIndexOutOfBoundsException
--Υ����ȫԭ���쳣��SecurityException
--�ļ��ѽ����쳣��EOFException
--�ļ�δ�ҵ��쳣��FileNotFoundException
--�ַ���ת��Ϊ�����쳣��NumberFormatException
--�������ݿ��쳣��SQLException
--����/����쳣��IOException
--����δ�ҵ��쳣��NoSuchMethodException

7.4.2 �쳣�Ĳ�׽������� P240
1. �쳣����Ļ����﷨
try��catch��finally��throw��throws
finally���飺����catch��������飬����������ǻ��ڷ�������ǰִ�У�������try�����Ƿ����쳣��Ŀ���Ǹ�����һ�����ȵĻ��ᣬ������Java���ԵĽ�׳�ԡ�
//���£���ʹδ����catch�����У�finally�е���仹�ǻ�ִ�е�
public class TestMail
{
	public static void main(String args[])//main�������붨�������ļ�����һ�µ�public����
	{
		int r = 30;
		try
		{
			if (r <= 20)
			{
				throw new Exception("�����쳣��\n�뾶Ϊ��" + r + "\n�뾶����С��20��");
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
������£�
before return

�����£����û�з����쳣����finally��仹�ǻᰴ˳��ִ��
public class TestMail
{
	public static void main(String args[])//main�������붨�������ļ�����һ�µ�public����
	{
		int r = 30;
		try
		{
			if (r <= 20)
			{
				throw new Exception("�����쳣��\n�뾶Ϊ��" + r + "\n�뾶����С��20��");
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
������£�
before return
last sentence

2. try��catch��finally����Ӧע�������
--try��catch��finally 3 ����������ܵ���ʹ�ã�3 �߿������try...catch...finally��try...catch��try...finally3�ֽṹ��catch��������һ��������finally������һ����
--try��catch��finally 3 ��������б�����������Ϊ������ڲ����ֱ�����������໥���ʡ����Ҫ��3�����ж����Է��ʣ�����Ҫ���������嵽��Щ������档
--ʹ�ö��catch��ʱ��ֻ��ƥ������һ���쳣�ಢִ��catch����룬��������ִ�б��catch�飬����ƥ��catch����˳�������ϵ��µġ�

3. throw��throws�ؼ���
throw�ؼ��������ڷ������ڲ��������׳�һ��Throwable���͵��쳣��
throws�ؼ������ڷ������ⲿ�ķ����������֣����������������ܻ��׳�ĳЩ�쳣�������׳��˼���쳣���÷����ĵ����߲ű��봦����������׳����쳣���������ĵ���������������쳣��ʱ��Ӧ�ü����׳�����������������һ����catch���д�ӡһ�¶�ջ��Ϣ������ǿ����
--throw������׳�һ���쳣��throws�Ƿ����׳�һ���쳣�������׳��쳣���������������ö��ŷָ���
--throws���Ե���ʹ�ã�throw���ܡ�
--throwҪô��try...catch...finally�������ʹ�ã�Ҫô��throws����ʹ�á���throws���Ե���ʹ�ã�Ȼ�����ɴ����쳣�ķ�������
һ���򵥵����ӣ�
public static void test() throws Exception
{
	throw new Exception("����test()�е�Exception");
}

7.4.3 ʹ���쳣���Զ����쳣 P241
1. ʹ�����е��쳣��
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
2. �Զ����쳣��
public class MyException extends Exception
{
	public MyException() {}
	public MyException(String msg)
	{
		super(msg);
		System.out.println(msg);
	}
}
���쳣���У����Ը��ǻ����ظ���Exception�еĺ���������ʵ���Լ��Ĵ��롣
3. ʹ���Զ�����쳣
��throws�������������׳��Զ�����쳣������throw������ʵ��ĵط��׳��Զ�����쳣��
public void test() throws MyException
{
	...
	if (...)
	{
		throw new MyException();
	}
}
Ҳ���Խ��쳣ת�ͣ�Ҳ��ת�룩��ʹ���쳣���׶�������⡣��νת�ͣ����ڲ�׽��ĳһ���쳣ʱ���׳������һ���Զ�����쳣��������Ϊ���ⲿ����ͳһ�ؽ����쳣��
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

7.5.4 �ϻ���ҵ�ο� P245
��1������쳣�ࣺ
//CalculatorException.java
package calculator;

public class CalculatorException extends Exception
{
	public CalculatorException(String msg)
	{
		super("����ת���쳣��" + msg);
	}
}

��2������AbstractCalulator�е�java.lang.NumberFormatException�쳣����ת���CalculatorException�쳣�׳���
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

��3���Ӽ��˳����������Ӽ��㺯���׳��쳣����
package calculator.impl;

import calculator.CalculatorException;
import calculator.ICalculator;
import calculator.AbstractCalculator;

public class Plus extends AbstractCalculator implements ICalculator
{
	@Override
	public int calculate(String expression) throws CalculatorException
	{
		int arrayInt[] = split(expression, "\\+");//String.split�����е������������ʽ��+��*�����⺬�壬����Ҫ�ý���ת��
		return arrayInt[0] + arrayInt[1];
	}
}

�ӿ���ICalulator�Ľӿں����׳��쳣��
package calculator;

public interface ICalculator
{
	public int calculate(String expression) throws CalculatorException;
}

��4��������Test�в�׽�������׳����Զ����쳣�����������̨��
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
//Java���ı�̼��� ��8�� Java����淶����ʽ P249
8.1.1 �ļ�������
�����Դ�����а����й�����Ķ��壬���Դ�ļ���������ù������������ȫһ�£���ĸ�Ĵ�Сд������һ��������Java���Ե�һ���ϸ�Ĺ涨����������أ��ڱ���ʱ�ͻ������ˣ���һ��JavaԴ����������ֻ����һ��������Ķ��塣���Դ�����в�����������Ķ��壬����ļ�����������ȡ���������һ��Դ�������ж���ඨ�壬���ڱ���ʱ��Ϊÿ��������һ��.class�ļ���
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
		DbTest dbTest = new DbTest();//��ͬһ�����е�Դ���������ֱ�����ã�����û�м�public���ʿ��Ʒ�
		dbTest.show();
	}
}
��ķ��ʿ��Ʒ�ֻ��public��abstract��final���֡������ֲ�˵�ˣ�public����ܱ�������import����ʹ�ã�����public���޷���������ʹ�ã�ֻ���ڱ�����ʹ�á�

8.1.2 ��������
������������������Ӣ����������Ӧ�ö�����Сд��ĸ��ɵġ�����Java��������̵����ԣ�ÿһ��Java����Ա�����Ա�д�����Լ���Java����Ϊ�˱���ÿ��Java��������Ψһ�ԣ�Ҫ�����Ա���Լ�����İ�������֮ǰ����Ψһ��ǰ׺��
�������������ķ�Χ�Ӵ�С���г���ǡ�ú�Internet�ϵ��������������෴���磺
net.sourceforge
com.ibm.util
com.sun.eng
com.apple.quicktime

8.1.3 ������� P250
������һ�����ʣ����ô�Сд��ϵķ�ʽ��ÿ�����ʵ�����ĸ��д��
--�����ɴ�д��ĸ��״��������ĸСд����Test
--�����ɶ��������ɣ�ÿ����������ĸ��Ӧ��д����TestPage
--��������а���������д������д�ʵ�ÿ����ĸ��Ӧ��д����XMLExample�����˸о�XmlExample����һЩ��
����һ���������ɣ�����������������������ģ�������������ʱӦ����ѡ�����ʡ�

8.1.4 ����������
Сд��ĸ��ͷ�����浥���ô�д��ĸ��ͷ

8.1.5 ����������
��ĳ�����ʹ��static final �����εı�������Ϊ������ͨ�ı��������𣬳���������Ӧ�ö�ʹ�ô�д��ĸ������ָ���ó����������塣������а���������ʣ�Ҫ�ԡ�_�����ӣ��磺
static final int MIN_WIDTH = 4;
static final int MAX_WIDTH = 999;
static final int GET_THE_CPU = 1;

Tips����Java�����У�����ʲôʱ�򣬾��ᳫʹ�ó���ȡ�����֡��̶��ַ�����Ҳ����˵�������г�0��1���⣬������Ӧ�ó����������֡��������Լ����ڳ���ʼ���ֶ��壬�����ڸ�����������ڶ��壬�羲̬��ľ�̬������

8.1.6 ��������� P251
����Ҳ�Ǳ������������Ӧ����ѭ������������������Ӧ������������ķ�ʽ�������ģ�
byte[] buffer;
�����ǣ�
byte buffer[];//��������C++��ֻ����������������ʽ������ͳһ����������������

8.1.7 ����������
�������Ķ���Ӧ��ʹ��������ı�ʶ����Java�з�������Сд��ĸ��ͷ������һ�㶼ʹ�ö����ṹ�����飬��ʼ�Ķ�����ĸСд�����һ������ĸ��д�����ʣ�Ҳ�����ڶ����ṹ�м������δʡ�
���еĲ����ͻ�ȡ���������õ���is��ǰ׺���磺
public boolean isPersistent();
public boolean isString();

8.2 Java�Ű�淶 P252
8.2.1 �ո�
8.2.2 ����
���н��߼���صĴ���ηָ���������߿ɶ��ԡ�
--���һ���ļ����м����࣬��֮�����ʹ�ÿ��У�������2�����У���
--��ĳ�Ա�������෽��֮������п��У�������2�����У���
--����������֮�����ʹ�ÿ��н��зָ���������2�����У���
--������Щ����ͬһ���飬�����߼��Ͻ�����˲�ͬ���ܵ���䣬Ӧ����һ�����зֿ���
Tips��}�����Զ������Ϊһ�С�

8.2.3 ���� P255
�����Ű�ͨ����4���ո���Ϊһ����λ
--ÿ�������������4���ո�λ��
--�����ܳ�������£����ڻ��е�����
	ÿһ��Java���붼Ӧ��������80���ַ����ڡ���һ�д������ʱ��Ҫ�������С����к󣬵�2�еĿ�ͷӦ�����1�����ͬ�ȵ�λ�Ĵ��������루ʹ�á�Tab�����޷�����ʱ����Ҫ��С�����Ŀո���Ŀ�����Ǵ����ԡ�Tab���������Ŀո�Ϊ׼����������ʱ����2���Ժ�ĸ���һ��Ҫ���롣
--ע��Ӧ�úͱ�ע�͵Ĵ��������������ͬ

8.2.4 ҳ��
��������һ�еĳ��ȳ���80���ַ������κ�����£�Խ�������Ӧ����һ�����Ż���һ�������������С�

8.3 Javaע�͹淶

8.4 Java�ļ���ʽ P257
8.4.2 ��������
�����package�У�������������ڳ�ע����ĵ�һ�С�import��Ҫ����package�ĺ��棬֮���һ�С�import�б�׼�İ���Ҫ�ڱ��صİ���֮ǰ�����Ұ�����ĸ˳���������import���а�����ͬһ�����еĲ�ͬ��Ŀ¼����Ӧ����*������
8.4.6 ���캯��
��Ӧ���õ����ķ�ʽд�����磬�������д�ں��棩

8.4.8 toString()����
һ������£�ÿһ���඼Ӧ�ö���toString()�����������ⲿ����������
public String toString()
{}

//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��9�� Java����/����� P267
9.1.1 ��׼����/�����System
System�����3����̬��Ա���������ǹ������������ı�����
static PrintStream out;//��׼�����
static PrintStream err;//��׼���������
static InputStream in;//��׼������

1. out��׼��������������Ϣ������̨
System.out.println(data);
out�����������PrintStream�������ṩ��һϵ�е���print()��������������������͵����ݣ����������������͡������������ͺ����飬�����б����£�
void print(boolean b);
void print(char c);
void print(char[] s);//��ӡ�ַ�����
void print(double d);
void print(float f);
void print(int i);
void print(long l);
void print(Object obj);
void print(String s);
���⣬���ṩ�˶�Ӧ�Ļ���������� 
void println(xxx xxx);
���Ҫ���һ�����У������ֱ��ʹ������ĺ�����
void println();
����֮�⣬������ʹ������ķ���ֱ������ֽ����ݣ��ֽ����ݾ��Ǳ��º��潫Ҫ�������ֽ�������
void write(byte[] buf, int off, int len);//��len�ֽڴ�ָ���ĳ�ʼƫ����Ϊoff��byte����д�����
void write(int b);//��ָ�����ֽ�д�����

2. err��׼����������������������Ϣ������̨ P269
err�ǡ���׼������������������Ѵ򿪲�׼������������ݣ�
System.err.println(data);

Tips����׼����ͱ�׼�����һ�������ǣ���׼��������Ǵ�����ģ�����׼����û�л��棨Ĭ�����ã����Ըģ�������������ñ�׼�����ӡ�����Ķ�������������ʾ����Ļ�ϣ�����׼�����ӡ�����Ķ�������Ҫ�ٻ��ۼ����ַ�����һ���ӡ�������ܵ���˵��System.out���������������Ҳ���ǳ�����������������ݣ���System.err���ڳ�����Ϣ�������Ҳ�����㱾�����ڴ������Ķ�����

3. in��׼�������������ռ�������
in�ǡ���׼����������in�����������InputStream�����ṩ������������
��1��read()������ȡһ���ֽ�
read()���ڴӿ���̨����һ���ֽڣ�8bit����

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
��2��read(byte[] b)��ȡһ���ֽ�����
����ĺ���һ�ν����ܹ���ȡһ���ֽڡ�����ʹ��read(byte[] b)����ȡһ���ֽ����顣���ȴ�����һ������Ϊ100���ֽ����飬��ʾһ�ο��Զ�ȡ100���ֽڣ�Ȼ��ʹ��read(byte[] b)��ȡ100���ֽڵ�byte�У��ٵ���write()�����������ֽ����鵽����̨�С�

public class TestSystemInReadByte
{
	public static void main(String args[])
	{
		try
		{
			byte[] b = new byte[100];//���������ַ������ȴ���100��������ֽھͻᱻ������
			System.in.read(b);
			System.out.write(b, 0, 100);
			//���Խ�������ֽ�����bת��Ϊ�ַ������
			//String str = new String(b);
			//System.out.println(str);
		}
		catch (Exception e)
		{
		}
	}
}

//ѭ������
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

Tips��System.in��read()������ȡ�����ڿ���̨�а��ع���ʼִ�еġ���ˣ����������ĳ�����ʹ��read()������ȡһ���ֽ�ʱ���������û�����һ���ַ����ȡ�ģ������ڰ��س�����һ���Զ�ȡ�ġ�

9.1.2 ����̨��д��Console P271
�Ͻڳ�������ֽ����飬��ת��ΪString��ʵ�ָ��ӡ�������100�����ƣ�С��100��ʹ�÷�����棬����100���ᱻ�����������벶׽�쳣��

public class TestConsole
{
	public static void main(String args[])
	{
		while (true)
		{
			String str = System.console().readLine();//��Eclipse�����׳�console��ָ���쳣��ʹ��cmd����java TestConsole������������
			System.out.println(str);
		}
	}
}
����ʹ����System.console()����ȡ����Console����Ȼ��ʹ��readLine()�������ɶ�ȡһ���ַ��������롣�ú������û�ִ�лس�ʱ���á�
--����Ҫ��׽�쳣���ú���û���׳��쳣��
--����Ҫ���ֽ�����ת�����ַ�����ֱ�Ӷ���ľ����ַ�����
--����ִ���У����������ض�ȡһ�����벢��������������������ͷ�����䡣

��1��Console�༰�����Ϣ����
Console���������������ɷ����뵱ǰJava����������Ļ����ַ��Ŀ���̨�豸������У�����ָ�ľ��Ǽ��̡�
������Ƿ���п���̨ȡ���ڵײ�ƽ̨����ȡ���ڵ���������ķ�ʽ������������һ������ʽ�����п�ʼ��������û���ض����׼��������������ô�����̨�����ڣ�ͨ�����ӵ����̲�������������ĵط���ʾ�������������Զ������ģ��磬�ɺ�̨��ҵ���ȳ�������������ͨ��û�п���̨���������������п���̨����ô�����ɴ���Ψһ��ʵ������ͨ������System.console()������ã���ʾ�����û�п��õĿ���̨�豸���Ը÷����ĵ��ý�����null��
Tips�������Ϊʲô������ִ�����ϳ���ʱֻ���ڿ���̨��ִ�У���������Eclipse��ִ�е�ԭ��

�������ڴӿ���̨��ȡ��Ϣ��Ҳ���������Ϣ������������ǣ�
System.console().printf(str);
���Ķ�д������ͬ���ģ��Ա�֤�ؼ�������������ɡ���˵��÷���readLine()��printf()ʱ����������
��2����ȡ����
���Ӧ�ó�����Ҫ��ȡ�����������ȫ���ݣ���Ӧ��ʹ��readPassword()��readPassword(String, Object)������ִ�к��ֹ������ص��ַ�������㣬������޶ȵ������ڴ����������ݵ������ڡ�

public class TestConsolePassword
{
	public static void main(String args[])
	{
		while (true)
		{
			char password[] = System.console().readPassword();//System.console().readPassword("[%s]", "Please input password");����"[Please input password]"��Ϊ��ʾ��Ϣ��ʾ�ڿ���̨������
			System.out.println(password);
		}
	}
}
������£�
D:\Program Files\eclipseJava workspace\LearningProject\bin>java TestConsolePassword

hello

it is amazing

9.2 �ļ��������� P273
9.2.1 �ļ�������File
��1������Fileʵ��
--File(String pathname)
File path = new File("D:/demo");
File file = new File("D:/demo/test.txt");
--File(String parent, String child)
File file = new File("D:/demo", "test.txt")
--File(URI uri)
File file = new File("file://D:/demo/test.txt");
--File(File parent, String child)������parent����·������child·�����ַ�������һ����Fileʵ����
File file = new File(path, "test.txt");

��2���ж���Ŀ¼�����ļ�
isDirectory()���Ƿ���Ŀ¼
isFile()���Ƿ����ļ�
�磺
if (path.isDirectory())
{
	
}
if (file.isFile())
{
	
}

��3�����Ŀ¼���ļ��Ƿ����
if (path.exists())
{
	
}
if (file.exists())
{
	
}

��4������Ŀ¼���ļ�
��Ŀ¼���ļ�������ʱ������ͨ������ķ���������Ŀ¼����ļ���
--mkdir()������һ��Ŀ¼
--mkdirs()����������Ŀ¼
--createNewFile()������һ���ļ�
���У��������Ŀ¼��ʱ��Ҫ��mkdirs()��mkdir()ֻ��������Ŀ¼�´���һ����Ŀ¼��������createNewFile()����Ҫ��׽IOException���磺
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
��5���鿴Ŀ¼�µ��ļ��б������·����
����Ŀ¼��˵������ʹ��listFiles()ȡ�ø�Ŀ¼�µ��ļ��б������ص���һ��File���͵����飬����ѭ��������ȡ��ÿһ���ļ����󣬲�ʹ��getAbsolutePath()ȡ�øö����·������
������ʾ��
File[] filelist = path.listFiles();
for (int i = 0; i < filelist.length; ++i)
{
	System.out.println(filelist[i].getAbsolutePath());
}

//��
import java.io.File;

public class TestConsolePassword
{
	public static void main(String args[])
	{
		File path = new File("f:\\PTDownload");
		File[] fileList = path.listFiles();//����õ������ļ����ļ��У����������ļ���
		for (File f : fileList)
		{ 
			System.out.println(f.getAbsolutePath());
		}
	}
}
������£�
f:\PTDownload\f
f:\PTDownload\Rise.of.the.Tomb.Raider.CHT.CHS.RF.X360.GOD.WITH.TU2.DLC-ALI213
f:\PTDownload\ʹ���ٻ�.BD1280������Ӣ˫��.mp4
f:\PTDownload\�������.BD������Ӣ˫��.mkv
f:\PTDownload\ͷ���ع���BD1280�����Ӣ˫������.mkv
f:\PTDownload\�ۇ�HD1280��������.mp4
f:\PTDownload\�����ɣ�������.HD1280���������Ӣ˫��.mp4
f:\PTDownload\è������֮���˴�ð��BD����.rmvb
f:\PTDownload\����Ħ˹����BD1280������Ӣ˫��.rmvb
f:\PTDownload\��ʽ����BD1280������Ӣ˫��.rmvb

��6���������ļ���
�����ļ�������ʹ��renameTo()����������һ���µ��ļ���������Ϊһ����Fileʵ�������磺
File newFile = new File("D:/demo/test2.txt");
if (file.isFile())
{
	file.renameTo(newFile);
}

��7��ɾ��Ŀ¼���ļ�
����Ŀ¼���ļ����󣬿���ִ��delete()������ɾ��Ŀ¼���ļ��������Ŀ¼ִ��ɾ�����򽫻�ɾ����Ŀ¼�µ������ļ���Ŀ¼�����磺
path.delete();
file.delete();

9.2.2 �ļ�������FileFilter��FilenameFilter P275
���ض�Ҫ���ҳ������ļ�������ʹ�������ļ�������FileFilter��FilenameFilter�����Ƕ��ǽӿڣ�ֻ��Ҫ���Ǳ�д�Լ���ʵ������ʵ�����ǵĺ���accept()���ɡ�FileFilter����Ҫ���þ��Ǽ���ļ��Ƿ���ڣ�FileFilter��FilenameFilterΨһ�Ĳ�ͬ�ǣ�FileFilter�ṩ�ļ�����ķ��ʷ�������FilenameFilter�ǰ���Ŀ¼���ļ����ķ�ʽ�������ġ�
FileFilter�Ĺ��˺����ӿڣ��û���ʵ����ֻ��Ҫʵ�ָú������Բ�ͬ���ļ����й��˼��ɣ�
boolean accept(File file);

��FilenameFilter�Ĺ��˺����ǰ���Ŀ¼���ļ��������еģ�
boolean accept(File directory, String name);

1. FileFilter��ʹ��
һ���򵥵������������ض����ļ���չ��������ʹ��FilenameFilter�����ǳ����Ľ������������жϵ�����Ŀ¼���ǡ�Ҫ���������⣬��Ҫʹ��FileFilter����
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
		//��չ��ǰ�ķ��š�.��
		int index = name.lastIndexOf(".");
		if (index == -1)//û����չ���򷵻�false
		{
			return false;
		}
		else if (index == name.length() - 1)//�Ե�Ž�β�򷵻�false
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
������£�
f:\PTDownload\ʹ���ٻ�.BD1280������Ӣ˫��.mp4
f:\PTDownload\�ۇ�HD1280��������.mp4
f:\PTDownload\�����ɣ�������.HD1280���������Ӣ˫��.mp4

2. FilenameFilter��ʹ�� P277
FilenameFilter�Ĺ��˲���accept()��ֱ�Ӹ���Ŀ¼���ļ������й��ˣ���FileFilter��ȣ�����һ���ļ����������������ӹ�����չ��Ϊ.gif,.jpg,.png���ļ���
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
������£�
d:\Program Files\eclipseJava workspace\LearningProject\src\imageButton.jpg
d:\Program Files\eclipseJava workspace\LearningProject\src\img.jpg
d:\Program Files\eclipseJava workspace\LearningProject\src\sjtu.jpg
d:\Program Files\eclipseJava workspace\LearningProject\src\wxPython.jpg

//����ImageFilter.java���н϶���ظ����룬�ع������£�
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

Tips��ͨ������ʹ��FilenameFilter������ļ����ˡ�

9.2.3 �ļ������д��RandomAccessFile P279
RandomAccessFile���ܹ���������ļ��������ļ����ݴ洢��һ������byte�����С���������ָ�������byte����Ĺ�����������Ϊ�ļ�ָ�룬��ָ��λ�ÿ���ͨ��seek�������á�
ͨ������������е����ж�ȡ�����ڶ�ȡ�����������ֽ�֮ǰ�ѵ����ļ�ĩβ�����׳�EOFException����һ��IOException�����������ĳЩԭ���޷���ȡ�κ��ֽڣ������Ƕ�ȡ�����������ֽ�֮ǰ�ѵ����ļ�ĩβ�����׳�IOException��������EOFException����Ҫ�ر�ָ����������ѱ��رգ�������׳�IOException��
1. ����ر��ļ�
--RandomAccessFile(File file, String mode)
--RandomAccessFile(String name, String mode)
��һ��ͨ��һ��File���󴴽����ڶ�����ֱ��ָ���ļ��������еĲ���mode��ʾ��Ŀ���ļ��ķ���ģʽ����ѡֵ���9-1 ��ʾ��
r
rw
rws
rwd
Tips����rwd��ģʽ�����ڼ���ִ�еġ�I/O������������ʹ�á�rwd����Ҫ�����д��洢���ļ������ݣ�ʹ�á�rws��Ҫ�����Ҫд����ļ����ݼ���Ԫ���ݣ���ͨ��Ҫ������һ�����ϵĵͼ���I/O������

���ö�дģʽ���ļ��ķ�����
RandomAccessFile file = new RandomAccessFile("D:/demo/test.txt", "rw");
����ɶ��ļ���һϵ�в���֮�󣬻���Ҫʹ��close()�����ر��ļ���
file.close();
Tips�����ļ������رգ�������ļ��ᱻռ�á�

���ļ��󣬻��������ļ��ĳ��ȷ�Χ������ָ��λ��
��1����ȡ�ļ�����
����ʹ��length()ȡ���ļ��ĳ��ȣ�����ֵ�Ǹ��ļ����ֽ�������������ʾ��
long size = file.length();
System.out.println("filesize:" + size);
��2�������ļ�ָ��λ��
���ļ���Ĭ�ϵĶ�ȡָ��ָ���ļ����ʼ������0��RandomAccessFile��֮�����Ƿ����࣬������Ϊ���ܹ��Զ���λ��ȡ��ָ�롣��ʱ����ʹ��seek(long pos)��������λ��ȡλ�á�
file.seek(4);//���ÿ�ʼָ��λ��Ϊ4
Tips��������õ�ָ��λ�ó������ļ����ֽ������ȣ���ô��Ĭ�϶�λ���ļ���ĩβ��

2. ��ȡ�ļ�
���ļ��󣬼��ɴӸ��ļ��ж�ȡ���ݡ�
��1����ȡһ�������ֽڣ�read()
read()�����Ӹ��ļ��ĵ�ǰָ��λ�ö�ȡһ���ֽڣ����Խ����ֽ�ת��Ϊ�ַ���������£�
int c = file.read();
System.out.println((char)c);
ÿ����һ��read()��������ȡ��ָ��ͻ������ƶ�һ���ֽڵ�λ�á�
��2����ȡ�ֽ����飺read(byte[] b)
�ú����ܹ�һ�ζ�ȡ����ֽڣ���ȡ�ĳ���Ϊ�ֽ�����b�ĳ��ȣ����£�
byte[] b = new byte[3];
file.read(b);
String str = new String(b);
System.out.println(str);
��3������ȡ�ֽ����飺read(byte[] b, int off, int len)
����ʹ�ò���offָ������������ʼλ�ã���ʹ��lenָ�����ļ��н�����ֽڳ��ȣ����£�
byte[] b2 = new byte[3];
file.read(b2, 1, 2);
String str2 = new String(b2);
System.out.println(str2);
Tips��len���벻�ܴ��ڴ�off��ʼ��ʣ�������ĳ��ȣ�������������Խ����쳣IndexOutOfBoundsException��
��4����ȡ�̶����͵���ֵ P281
��5����ȡһ���ַ�����readLine()
����Ķ�ȡ�������Ƕ����byte���͵��ֽڣ�Ҳ���ǰ�λ��ȡ�ġ���ȡ�ļ���һ�п���ʹ��readLine()��������String���͵��ַ��������£�
System.out.println(file.readLine());
��6����ȡ�����ַ���readUTF()
System.out.println(file.readUTF());
//abc.txt
abc=���
def:��������
opq    :    ʲô


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
			
			System.out.println(file.readLine());//��ǰ��ʣ���ַ���ȡ�����������������Ѷ�ȡ���ַ�
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
������£�
a
bc=
????
def:?��?????��

3. д���ļ� P281
��1��д��һ�������ֽڣ�write()
�ڸ��ļ��ĵ�ǰָ��λ��д��һ���ֽڣ�������ԭ�е��ֽڡ����£�
file.write(100);
ÿ����һ��write()��������ȡ��ָ��ͻ������ƶ�һ���ֽڵ�λ�á�
��2��д���ֽ����飺write(byte[] b)
һ����д�����ֽڣ�д��ĳ���Ϊ�ֽ�����b�ĳ��ȡ�
byte[] bw = new byte[3];
bw[0] = 100;//�ַ�d��ASCII��
bw[1] = 101;//e
bw[2] = 102;//f
file.write(bw);
ִ�к󣬻���ָ��λ��д�롰def��3���ַ���
��3�����д���ֽ����飺read(byte[] b, int off, int len)
����ʹ�ò���offָ��д���������ʼλ�ã���ʹ��lenָ����������д����ֽڳ��ȡ����£�
file.write(bw, 1, 2);//д���1��ʼ������Ϊ2�����飬��ef
Tips��len���벻�ܴ��ڴ�off��ʼ��ʣ�������ĳ��ȣ�������������Խ����쳣IndexOutOfBoundsException��
��4��д��̶����͵���ֵ
��5��д��һ���ַ�����writeBytes()��writeChars()
��Ҫд��һ���ַ�������ʱ����ʹ��writeBytes()�����£�
file.writeBytes("AA");
������ʹ��writeChars()д��һ���ַ�������writeBytes()��ͬ���ǣ�writeChars()д���ÿһ���ַ���ʦ����˫�ֽ�д��ģ���һ���ַ�ռ��2���ֽڡ����£�
file.writeChars("DD");
��仰ʵ������д���ˡ�DD��4���ֽڣ���λ��0��䡣
��6��д�������ַ���writeUTF()
file.writeUTF("�й�");//д�������ַ�

9.3 ����/����� P282
Java IO��ؼ���4�������ǣ�InputStream�������ֽ�������OutputStream������ֽ�������Reader�������ַ�������Writer������ַ�����������public abstract class�ࡣ
InputStream��OutputStream�������ݵĴ��������ֽڡ�byte��Ϊ��λ�ģ���Reader��Writer�������ݵĴ��������ַ���character��Ϊ��λ�ġ�java.io���е�������Ͽ��Է�Ϊ�����࣬һ������byte����Ϊ����Stream�࣬���Ƕ�����XXXStream��ʽ�����ģ�һ������character����Ϊ����Read/Writer�࣬���Ƕ�����XXXReader��XXXWriter�ķ�ʽ�����ġ�

9.3.1 ���Ķ���ԭ��
��ν�����������ݵ��������У����������Ǵ�ĳ��Դ����Ϊ��Դ����������ĳ��Ŀ�ĵ�ȥ�ġ�
--�������ķ���Ĳ�ͬ�����Էֳ����������������һ���������������ȡ�����������д���ݡ�
--���������������Ͳ�ͬ���ֿ��Է�Ϊ�ֽ������ַ������ֽ�������ʵ�����ַ�����ת����

������������������ض�������������Դ����������������������Դ����Ļ����������Էֳ������ࡣ
--��һ�ࣺ���顢String��File�ȣ���һ�ֽ�ԭʼ��Դ��
--�ڶ��ࣺͬ�����͵������������������Դ����������Դ��

9.3.2 �����ֽ���InputStream P285
1. �ֽ�������Ϊ����Դ����ByteArrayInputStream
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
������£�
def

�Ӹ����ʹ��ʵ����������ֻ�ǽ��ֽ�����ת��Ϊ��һ���ֽ����飬�ӱ����Ͽ�����û��ʲôʵ�����塣ʵ���ϣ������ֽ�����ת��������ʽ��ȡ�������Ϳ���ͨ�����ķ�ʽ���ڴ��ж�ȡһ���ֽڣ�������Ҫֱ�Ӳ������飬�����ת���ĺô���ͬʱ������Ϊ�ַ����ṩ������Դ��
Tips�����������Ĵ��������IOException�쳣�Ĳ�׽�������漰������������������ģ�����Ҫ�����쳣��׽��

2. �ļ���Ϊ����Դ����FileInputStream
FileInputStream���ļ�ϵͳ�е�ĳ���ļ��л�������ֽڣ������ڶ�ȡ��ͼ������֮���ԭʼ�ֽ�����������ͨ�����µ����ַ�ʽ������
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

3. ������Ϊ����Դ����ObjectInputStream
ObjectOutputStream��ObjectInputStream�ֱ���FileOutputStream��FileInputStreamһ��ʹ��ʱ������ΪӦ�ó����ṩ�Զ���ͼ�εĳ־ô洢��
���磬Ҫ����ObjectOutputStream��ʾ��д����ļ��ж�ȡ���ݣ���ͨ�����´���ʵ�֣�
FileInputStream fis = new FileInputStream("t.tmp");
ObjectInputStream ois = new ObjectInputStream(fis);

int i = ois.readInt();
String today = (String)ois.readObject();
Date date = (Date)osi.readObject();

ois.close();

Tips�������ļ�test.tmp��д�������ObjectOutputStreamд��Ķ���ÿһ�������д�붼����˳��ģ��������Ķ�ȡҲ�������ͬ����˳�����������һ�£����������������ת���쳣java.io.OptionalDataException������IOException�����ࡣ

4. �ַ�����Ϊ����Դ����StringBufferInputStream P289
StringBufferInputStream(String s);

5. �ܵ�����������PipedInputStream
�����ܵ���������ʵ��ʱ����Ҫ�Թܵ��������ʵ����Ϊ���룺
PipedInputStream(PipedOutputStream src);//ʹ�����ӵ��ܵ������
Ҳ���Դ���һ��δ���ӵĹܵ���������
PipedInputStream();//������δ���ӵ�PipedInputStream
Ȼ�����PipedInputStream���connect����������������ӣ�
void connect(PipedOutputStream src);//ʹ�˹ܵ����������ӵ��ܵ������src

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
������£�
def
Tips����ʵ����ʾ��PipedOutputStream�Ĵ�����ʹ�á�ͨ���ܵ���ʹ�ò��������һ�����򵥵ؽ����������ӣ�����ʵ���ϲ�û�����塣�ܵ���ͨ�����ڶ��̵߳�ϵͳ�У�һ���߳̿�����ʱ��������ܵ�д�����ݣ���һ���߳̿�����ʱ���������ܵ���ȡ���ݣ��ﵽ�첽�������ݡ�

6. ��������Դ����SequenceInputStream
���������Ӷ��InputStream������ʾ�������������߼����������������������򼯺Ͽ�ʼ�����ӵ�һ����������ʼ��ȡ��ֱ�������ļ�ĩβ�����Ŵӵڶ�����������ȡ���������ƣ�ֱ��������������һ�����������ļ�ĩβΪֹ��
�������Դ�����������Դ��Ҳ���Դ���һ��Enumeration���͵�����Դ�б��ֱ�ʹ������Ĺ��캯��������
SequenceInputStream(InputStream s1, InputStream s2);
SequenceInputStream(Enumeration<? extends InputStream> e);
//c.txt�е�����Ϊʲôû����ʾ������

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

������£�
It is a nice day.
How are you?

Tips������������Դ�б��У����԰�������InputStreamʵ���������������

7. ��������������FilterInputStream P292

8. ��������������BufferedInputStream
Ϊ��һ�����������һЩ���ܣ������������������
�����Ҫһ�����л�����ļ�����������Ӧ�����ʹ��FileInputStream��BufferedInputStream���⽫����߶�ȡ��Ч�ʡ�
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
Tips������������Ĺر�˳��������䴴����˳���෴������������Ҳ����ˡ�

9. ��������������DataInputStream

Tips��DataInputStream���ڶ��̷߳��ʲ�һ���ǰ�ȫ�ġ��̰߳�ȫ�ǿ�ѡ�ģ����ɴ��෽����ʹ���߸���

10. �к�����������LineNumberInputStream
һ�㲻ֱ��ʹ�ô��࣬��ʹ������Stream��Ƕ�׼�Ӷ�������ʹ�ã��磺
LineNumberInputStream lis = new LineNumberInputStream(new FileInputStream("source"));
Ҫ��ʾ�Ĺ����ǣ������ļ����ж�ȡ�����ÿһ�еĽպ����ݡ���ˣ�Ϊ�˶�ȡһ�����ݣ����ǿ���ͨ��DataInputStream�������������ˣ����readLine()�Ĺ��ܡ�
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
			FileInputStream fis = new FileInputStream("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/b.txt");//�ļ������һ��һ��Ҫ�Ǹ��ջ��з����������һ��û�ж������з��������һ�е��кŲ����1
			LineNumberInputStream lis = new LineNumberInputStream(fis);//�������ã�������ʹ��
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
������£�
1:It is a nice day.
2:How are you?
3:Hello, world!

Tips����JDK1.1 ��ʼ�����ַ�������ѡ������ͨ�����ַ�����LineNumberReader���в�����

11. �ƻ�����������PushbackInputStream P295
PushbackInputStreamΪ��һ��������������ܣ������ƻأ�pushback������ȡ����ȡ��unread����һ���ֽڵ��������ڴ���Ƭ�ο��Ժܷ���ض�ȡ���ض��ֽ�ֵ�ָ��Ĳ��������������ֽ�ʱ��������ã��ڶ�ȡ��ֹ�ֽں󣬴���Ƭ�ο��ԡ�ȡ����ȡ�����ֽڣ��������������ϵ���һ����ȡ�������¶�ȡ���ƻص��ֽڡ�
//���иó��򽫻��ظ������һ���ַ���

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
			PushbackInputStream pis = new PushbackInputStream(fis);//�������ã�������ʹ��
			
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

12. �ܽ� P296
������11��InputStream��ʵ�֣����ǵĹ��ܸ��в�ͬ�����ݹ��ܣ����Է�Ϊ3�ࡣ
��1����������Դ�Ĳ�ͬѡ�õ���
--���飺ByteArrayInputStream
--�ļ���FileInputStream
--����ObjectInputStream
--�ַ�����StringBufferInputStream
��2���������Ĵ���ѡ�����
--�ܵ���PipeInputStream
--���У�SequenceInputStream
��3���������й���ѡ�����
--���ˣ�FilterInputStream
--���棺BufferedInputStream
--���ݣ�DataInputStream
--�кţ�LineNumberInputStream
--�ƻأ�PushbackInputStream

����ʵ��Ӧ�õ���Ҫ��ʹ��������FileInputStream��BufferedInputStream�Ľ�ϣ����ڶ�ȡ�ļ�ʱʹ�û����ȡ������������߶�ȡ�ļ���Ч�ʡ�StringBufferInputStreamҲ��һ�ֱȽϳ��õ��࣬ͨ��������Ҫ���Ǹ���ĳһ���ַ���������һ����������
����ѡ����һ�֣���������Դ����֮�⣬�������඼ӵ����InputStream����Ϊ�����Ĺ��캯���������Զ����������ж�ΰ�װ�������������ӵ���˶�����ܡ������9-18 ������Ҫ��ȡ�кţ�����Ҫ��ȡһ�У���˾�ʹ���������װ��

9.3.3 ����ֽ���OutputStream P297
1. �ֽ�������Ϊ���������ByteArrayOutputStream
2. �ļ���Ϊ���Դ����FileOutputStream
3. ������Ϊ���Դ����ObjectOutputStream P299
4. �ܵ����������PipedOutputStream
PipedOutputStream���Խ��ܵ���������ӵ��ܵ�������������ͨ�Źܵ����ܵ�������ǹܵ��ķ��Ͷˡ�ͨ����������ĳ���߳�д��PipedOutputStream���󣬲��������̴߳����ӵ�PipedInputStream��ȡ���������������������ʹ�õ����̣߳���Ϊ�������ܻ���ɸ��߳�������
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
������£�
from:
hello, receiver, how are you

���г��򣬼���ʵ�ַ������߳����������̷߳������ݵĹ��ܣ����Ƿֱ�ʹ���˹ܵ����������������

Tips������9-23 �ǹܵ���Ӧ�õĳ���ģʽ���ڶ��̵߳������������У����벻���������ࡣ����ͨ���������߳̽��з��ͺͽ��գ�����ģʽ������Ϊ�Ժ�ʵ�ʿ����е�ģ����ʹ�á�

5. �������������FilterOutputStream P302
6. �������������BufferedOutputStream P302
7. �������������DataOutputStream P303
8. ��ʽ�����������PrintStream P304

9. �ܽ� P307
������8��OutputStream��ʵ�֣����ǵĹ��ܸ��в�ͬ���������ǵĹ��ܣ����Է�Ϊ3�ࡣ
��1���������Դ�Ĳ�ͬѡ�õ���
--���飺ByteArrayOutputStream
--�ļ���FileOutputStream
--����ObjectOutputStream
��2���������Ĵ���ѡ�����
--�ܵ���PipeOutputStream
��3���������й���ѡ�����
--���ˣ�FilterOutputStream
--���棺BufferedOutputStream
--���ݣ�DataOutputStream
--��ʽ����PrintStream

9.3.4 �����ַ���Reader P307
1. �ַ�������Ϊ����Դ����CharArrayReader
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
�Ӹ����ʹ��ʵ����������ֻ�ǽ��ַ�����ת��Ϊ���ַ����飬��û��ʲôʵ�����塣ʵ���ϣ������ַ�����ת��������ʽ��ȡ�������Ϳ���ͨ����ʽ�ķ�ʽ���ڴ��ж�ȡһ���ַ���������Ҫֱ�����飬�����ת���ĺô���

2. �ļ���Ϊ����Դ����FileReader P309
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
			//�����whileд��ͬ��
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
Tips���ó����ܹ���ȷ��������ģ���ΪFileReader�ǰ����ַ���ȡ�ģ���FileInputStream��������룬��Ϊ���ǰ����ֽڶ�ȡ�ġ����ԣ����Ҫ��ȡ�����ļ�����ʹ��FileReader��

3. �ַ�����Ϊ����Դ����StringReader
4. �ܵ�����������PipedReader P310
5. ��������������BufferedReader P312
�����Ҫһ�����л�����ļ�����������Ӧ�����ʹ��FileReader��BufferedReader���⽫����߶�ȡ��Ч�ʡ�
FileReader is = new FileReader("D:/demo/test.txt");
BufferedReader bis = new BufferedReader(is);

6. �к�����������LineNumberReader P312
����Ϊ�����������·���֮һʱ���������з���'\n'�����س�����'\r'�����س�����������з���
LineNumberReader�̳���BufferedReader��
FileReader fis = new FileReader("D:/demo/test.txt");
LineNumberReader lis = new LineNumberReader(fis);
String line;
while((line = lis.readLine()) != null)
{
	System.out.println(lis.getLineNumber() + ":" + line);
}
lis.close;
fis.close;

7. ��������������FilterReader P313
8. �ƻ�����������PushbackReader P313

9. �ܽ� P314
����8��Reader��ʵ�֣����ܸ��в�ͬ���������ǵĹ��ܣ����Է�Ϊ3�ࡣ
��1����������Դ�Ĳ�ͬѡ�õ���
--���飺CharArrayReader
--�ļ���FileReader
--�ַ�����StringReader
��2���������Ĵ���ѡ�����
--�ܵ���PipedReader
��3���������й���ѡ�����
--���ˣ�FilterReader
--���棺BufferedReader
--�кţ�LineNumberReader
--�ƻأ�PushbackReader

9.3.5 ����ַ���Writer P315
1. �ַ�������Ϊ���Դ����CharArrayWriter P316
2. �ļ���Ϊ���Դ����FileWriter
3. �ַ�����Ϊ���Դ����StringWriter
4. �ܵ����������PipedWriter P317
Tips������9-37 �ǹܵ���Ӧ�õĳ���ģʽ���ڶ��̵߳������������У����벻���������ࡣ����ͨ���������߳̽��з��ͺͽ��գ�����ģʽ������Ϊ�Ժ�ʵ�ʿ����е�ģ����ʹ�á�
5. �������������FilterWriter
6. �������������BufferedWriter P319
BufferedWriter���ı�д���ַ����������������ַ����Ӷ��ṩ�����ַ���������ַ����ĸ�Чд�롣�����ṩ��newLine()��������ʹ��ƽ̨�Լ����зָ�������˸�����ϵͳ����line.separator���塣��������ƽ̨��ʹ�����з���'\n'������ֹ���У���˵��ô˷�������ֹÿ�������Ҫ����ֱ��д�����з���
ͨ��Writer��������������͵��ײ��ַ����ֽ���������Ҫ����ʾ�������������BufferedWriter��װ������write()�������ܿ����ܸߵ�Writer����FileWriters��OutputStreamWriters����
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
			
			char[] buf = new char[] {'a', 'b', '��'};
			bos.write(buf);
			bos.newLine();
			bos.write("�����");
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


7. ��ʽ�����������PrintWriter P320
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
			
			pos.format("%1$tY��%1$tm��%1$td��%1$tHʱ%1$tm��%1$tS��", new Date());
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
�����ļ��������£�
2015��12��05��00ʱ12��11��
12345
true
1000.0

8. �ܽ� P321
������7��Writer��ʵ�֣����ܸ�����ͬ�����ݹ��ܣ���Ϊ3�ࣺ
��1���������Դ�Ĳ�ͬѡ�õ���
--���飺CharArrayWriter
--�ļ���FileWriter
--�ַ�����StringWriter
��2���������Ĵ���ѡ�����
--�ܵ���PipeWriter
��3���������й���ѡ�����
--���ˣ�FilterWriter
--���棺BufferedWriter
--��ʽ����PrintWriter

9.3.6 �ֽ������ַ�����ת��
���ַ�Ϊ�����stream����������֮��Ե����ֽ�Ϊ�����stream��������Ӧ��ʵ�ֵĹ�����ͬ��ֻ�ǲ���ʱ�ĵ���ͬ����CharArrayReader��ByteArrayInputStream�����ö��ǰ��ڴ��е�һ����������Ϊ��ʹ�ã�����ͬ����ǰ��ÿ�δ��ڴ��ж�ȡһ���ַ�����Ϣ��������ÿ�δ��ڴ��ж�ȡһ���ֽڡ���ԭ��˴���������byte�ֽڣ�char�ַ���
�ֽ������ַ���֮�������ȴ������ϵ������InputStreamReader����������ֽ���ת��Ϊ�����ַ�����OutputStreamReader���������ֽ���ת��Ϊ����ַ�����
1. �ֽ�������ת��Ϊ�ַ������� P322
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
			FileInputStream fis = new FileInputStream("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/test.txt");//���ֽڷ�ʽ��ȡ�ļ�
			InputStreamReader is = new InputStreamReader(fis);//����ȡ���ֽ�ת����Ϊ�ַ�
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

Tips���ó�����ֱ��ʹ��FileReader��ȡ�ļ����ݲ�ʹ��BufferedReader��װ��Ч����ͬ�����ܹ���������ĵ��ļ����ݡ����Ǵ˴���ͨ���ֽ�������ȡ�ļ��ģ��м侭�����ֽ������ַ�����ת������FileReader��ͨ���ַ�������ȡ�ļ��ģ�û�о�������ת��������ͨ��FileReader�ķ�ʽ�ȸó����е�ת����ʽЧ�ʻ��һЩ��

2. �ֽ������ת��Ϊ�ַ������ P323
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
			FileOutputStream fos = new FileOutputStream("d:/Program Files/eclipseJava workspace/LearningProject/dbTest/test.txt");//���ֽڵķ�ʽд���ļ�
			OutputStreamWriter os = new OutputStreamWriter(fos);//���ַ�ת��Ϊ�ֽ�
			BufferedWriter bos = new BufferedWriter(os);
			
			char[] buf = new char[] {'a', 'b', '��'};
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
Tips���ó�����ֱ��ʹ��FileWriterд���ļ����ݲ�ʹ��BufferWriter��װ��Ч����ͬ�����ܹ�д������ĵ��ļ����ݡ����Ǵ˴���ͨ���ֽ�����д���ļ��ģ��м侭�����ַ������ֽ�����ת������FileWriter��ͨ���ַ�����д���ļ��ģ�û�о�������ת��������ͨ��FileWriter�ķ�ʽ�ȸó����е�ת����ʽЧ�ʻ��һЩ��

9.4.3 �ϻ���ҵ P328
1. �ļ��༭��VI
//
package vi;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class VI
{
	public static void main(String[] args)
	{
		String pathname = "D:/Downloads";//����д"D:"�������������������·���������file.getCanonicalPath()���õ���ǰ�������ڵ��ļ�·��
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
							System.out.println("Ŀ¼��" + filepath);
						}
						else
						{
							System.out.println("�ļ���" + filepath);
						}
					}
				}
				else if (str.startsWith("cd"))
				{
					String[] param = str.split(" ");// ����ļ��е��������пո����ｫû�а취��������
					pathname = param[1];
				}
				else if (str.startsWith("mkdir"))
				{
					String[] param = str.split(" ");
					File path = new File(pathname + "/" + param[1]);
					path.mkdir();
				}
				else if (str.startsWith("vi")) // ׷������
				{
					String[] param = str.split(" ");
					File file = new File(pathname, param[1]);

					if (!file.exists())
					{
						file.createNewFile();
					}

					RandomAccessFile afile = new RandomAccessFile(file, "rw");
					afile.seek(afile.length());
					afile.writeUTF(param[2]);//д����ļ����������δ֪����
					afile.close();
				}
				else if (str.startsWith("more")) // ��ʾ���ݣ������޷�֧��������ʾ
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
					System.out.println("�������֧�֣�����������ָ�");
				}
			}
			catch (Exception e)
			{
				System.out.println("��������������������룺");
				e.printStackTrace();
			}
		}
	}

	private static String readDataFromConsole()
	{
		Scanner scanner = new Scanner(System.in);//�и�scannerδclose����ʾ
		return scanner.nextLine();
	}
}
���иó��򣬿������ı��ļ���д��Ӣ���ַ�����Ҳ������ʾӢ���ı����ݣ����ǲ��ܹ�д�����ʾ���ģ����Ļ���ʾ���롣


2. �ֽ����ı��༭�� ByteVI.java P331
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
		String pathname = "D:/Downloads";//����д"D:"�������������������·���������file.getCanonicalPath()���õ���ǰ�������ڵ��ļ�·��
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
							System.out.println("Ŀ¼��" + filepath);
						}
						else
						{
							System.out.println("�ļ���" + filepath);
						}
					}
				}
				else if (str.startsWith("cd"))
				{
					String[] param = str.split(" ");// ����ļ��е��������пո����ｫû�а취��������
					pathname = param[1];
				}
				else if (str.startsWith("mkdir"))
				{
					String[] param = str.split(" ");
					File path = new File(pathname + "/" + param[1]);
					path.mkdir();
				}
				else if (str.startsWith("vi")) // ׷������
				{
					String[] param = str.split(" ");
					File file = new File(pathname, param[1]);

					if (!file.exists())
					{
						file.createNewFile();
					}

					FileOutputStream os = new FileOutputStream(file, true);//�ڶ�������append����ʾ�Ƿ�׷�ӷ�ʽ
					BufferedOutputStream bos = new BufferedOutputStream(os);
					PrintStream pos = new PrintStream(bos);
					pos.print(param[2]);
					
					pos.close();
					bos.close();
					os.close();
				}
				else if (str.startsWith("more")) // ��ʾ���ݣ������޷�֧��������ʾ
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
					System.out.println("�������֧�֣�����������ָ�");
				}
			}
			catch (Exception e)
			{
				System.out.println("��������������������룺");
				e.printStackTrace();
			}
		}
	}

	private static String readDataFromConsole()
	{
		Scanner scanner = new Scanner(System.in);//�и�scannerδclose����ʾ
		return scanner.nextLine();
	}
}
���иó��򣬿����ı����ļ���д����Ӣ���ַ�����Ҳ������ʾӢ���ı����ݣ���������ʾ�������ݡ��ܹ�д����������ΪPrintStream���print()����ʵ���˶����ĵ�д�롣

3. �ַ����ı��༭��CharVI.java P332
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
		String pathname = "D:/Downloads";//����д"D:"�������������������·���������file.getCanonicalPath()���õ���ǰ�������ڵ��ļ�·��
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
							System.out.println("Ŀ¼��" + filepath);
						}
						else
						{
							System.out.println("�ļ���" + filepath);
						}
					}
				}
				else if (str.startsWith("cd"))
				{
					String[] param = str.split(" ");// ����ļ��е��������пո����ｫû�а취��������
					pathname = param[1];
				}
				else if (str.startsWith("mkdir"))
				{
					String[] param = str.split(" ");
					File path = new File(pathname + "/" + param[1]);
					path.mkdir();
				}
				else if (str.startsWith("vi")) // ׷������
				{
					String[] param = str.split(" ");
					File file = new File(pathname, param[1]);

					if (!file.exists())
					{
						file.createNewFile();
					}

					FileWriter fos = new FileWriter(file, true);//�ڶ�������append����ʾ�Ƿ�׷�ӷ�ʽ
					BufferedWriter bos = new BufferedWriter(fos);
					PrintWriter pos = new PrintWriter(bos);
					pos.print(param[2]);
					
					pos.close();
					bos.close();
					fos.close();
				}
				else if (str.startsWith("more")) // ��ʾ���ݣ������޷�֧��������ʾ
				{
					String[] param = str.split(" ");
					File file = new File(pathname, param[1]);

					FileReader is = new FileReader(file);
					BufferedReader bis = new BufferedReader(is);
					
					String line;
					while ((line = bis.readLine()) != null)//��readLine������һ��һ���ַ���ȡ��
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
					System.out.println("�������֧�֣�����������ָ�");
				}
			}
			catch (Exception e)
			{
				System.out.println("��������������������룺");
				e.printStackTrace();
			}
		}
	}

	private static String readDataFromConsole()
	{
		Scanner scanner = new Scanner(System.in);//�и�scannerδclose����ʾ
		return scanner.nextLine();
	}
}
���иó��򣬼ȿ������ı��ļ���д����Ӣ���ַ������ֿ�����ʾ��Ӣ���ı����ݡ�������ַ���������ŵ㣬�ܹ�ʵ�����ĵĴ���

//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��10�� Java���̱߳�� P335
10.1.1 �������̵߳����� P336
��1���������
ÿһ�����̶����Զ������������Ƕ�����һ�������Ľ��̣����Զ�ӵ�и��Ե��ڴ�ռ䣬�ڴ�ռ�֮���໥����û�й�������Ҳ�ܹ��������С�
��2������߳�
�߳�Ҳ��Ϊ���ͽ��̣�LWP�����߳�ֻ���ڵ������̵��������ڻ�����Դ����̱߳ȴ�������Ҫ���۵öࡣ��Ϊ�߳�����Э�������ݽ����������ڼ�����Դ����ǳ����ۣ������̱߳Ƚ��̸���ȡ��
�̣߳�������һ�������У��������ӵ��ͬһ���ڴ����򣬿��Թ����ڴ��е����ݡ����ǿ��Բ���ִ�У���ʵ������CPU�ĵ������Ǹ��Էָ�ʱ��Ƭ�ģ�������˳��ִ�еġ�

10.1 2 �̵߳ĸ���ģ��
�߳��Ǳ˴˻�������ġ��ܶ������е������񣬲���ÿ���̶߳����Լ��ĵ���ջ����ν�Ķ�������ͨ�������Եؽ�CPUʱ��Ƭ�л�����ͬ����������Ȼ��΢���Ͽ��������˵�CPU��ֻ����һ�������񣬵��Ӻ��������ÿ���������ƺ���ͬʱ�������еġ�

��������ָ��һ��ϵͳ�п���ͬʱ���ж�����򣬼��ж���������е�����ÿ�������Ӧһ�����̣�ͬ����һ����һ���߳�Ҳ�дӴ��������е������Ĺ��̣���Ϊ�̵߳��������ڡ����̵߳�״̬��state�������̴߳����������ڵ��ĸ��׶Ρ��߳��д����������С������С�����������5��״̬��ͨ���̵߳Ŀ�������ÿ�ʹ�߳����⼸��״̬��ת����ÿ�����������Զ�ӵ��һ���̣߳���Ϊ���̡߳���������ص��ڴ�ʱ���������̡߳�

10.1.3 �̵߳�����״̬ P337
�̵߳�״̬��ʾ�߳����ڽ��еĻ���ڴ�ʱ�����������ɵ������߳��д����������С������С�����������5��״̬��һ�������������̣߳����Ǵ�����5��״̬֮һ��
--�½�״̬��ʹ��new���������һ���̺߳󣬸��߳̽�����һ���ն���ϵͳû�з�����Դ���Ƹ��߳��ƴ���״̬��new thread����
--������״̬��ʹ��start()��������һ���̺߳�ϵͳΪ���̷߳������CPU���������Դ��ʹ�ø��̴߳��ڿ�����״̬��Runnable����
--����״̬��Java����ϵͳͨ������ѡ��һ��Runnable���̣߳�ʹ��ռ��CPU��תΪ������״̬��Running������ʱ��ϵͳ����ִ���̵߳�run()������
--����״̬��һ���������е��߳���ĳ��ԭ���ܼ�������ʱ����������״̬��Blocked����
--����״̬���߳̽�����������״̬��Dead����

��ռ��ʽ
��ʱ��ʽ

10.2 �̵߳Ŀ������� P338

��ʱ�Ȳ���
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��11�� Java����ʵ���� P373
11.1.1 ϵͳ��System P373
1. ����ϵͳ����
System�������ַ���ϵͳ���Եķ�����
��1����һ�֣�ȡ������ϵͳ�����б� P374

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
������£�
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

��2��ȡ��ĳһ��ϵͳ���Ե�ֵ P376
���֪��Ҫ��ѯ����ϵͳ���Ե�ֵ������ֱ��ʹ��System.getProperties(key)ֱ��ȡ�øü���ֵ�����£�
package test.System;

public class SystemTest
{
	public static void main(String[] args)
	{
		String osname = System.getProperty("os.name");
		String osversion = System.getProperty("os.version");
		String javaname = System.getProperty("java.vm.name");
		String javaversion = System.getProperty("java.version");
		System.out.println("����ϵͳ����=" + osname);
		System.out.println("����ϵͳ�汾=" + osversion);
		System.out.println("JVM����=" + javaname);
		System.out.println("Java�汾=" + javaversion);
	}
}
������£�
����ϵͳ����=Windows 8.1
����ϵͳ�汾=6.3
JVM����=Java HotSpot(TM) Client VM
Java�汾=1.8.0_25

�⼸��ֵ��������б����ֹ���
������ҵ�ϵͳ���Եļ������ڣ�������nullֵ����ʱ����ʹ������ĺ��������ñ���������ʱ��Ĭ��ֵ��
String osname = System.getProperty("os.name", "Windows XP");
�ڶ���������Ϊ������ʱ���ص�Ĭ��ֵ��
���˲�ѯ���������޸����ֵ���磺
System.setProperty("java.version2", "aa");
�ú������õ�ֵ������д�����ϵͳ��ֻ��Ե�ǰ���г������ִ�еĴ������Ӱ�졣����ú�������getProperties()ǰ��ᷢ���б����и����ԣ����������֮���б��о�û�С�
����֮�⣬������һ�������ö�����ԣ�
System.setProperties(Properties props);
�����ֵprops��һ��Properties���͵ı������ɡ�
�������Ƴ�ָ����ָʾ��ϵͳ���ԣ��磺
System.clearProperties("java.version2");

��Ȼ�����˲�ѯ�⣬�޸ĺ�ɾ������ͨ����ʹ�ã�ֻ������Java��װ������һ���������ʱ�Ż�ʹ�õ���

package test.System;

public class SystemTest
{
	public static void main(String[] args)
	{
		String javaversion2 = System.getProperty("java.version2", "no key");
		System.out.println("Java�汾2=" + javaversion2);
		
		System.setProperty("java.version2", "aa");
		javaversion2 = System.getProperty("java.version2");
		System.out.println("Java�汾2=" + javaversion2);
		
		System.clearProperty("java.version2");
		javaversion2 = System.getProperty("java.version2", "no key");
		System.out.println("Java�汾2=" + javaversion2);
	}
}
������£�
Java�汾2=no key
Java�汾2=aa
Java�汾2=no key

2. ���ʻ������� P377
�����ַ���ϵͳ���������ķ�����
��1����һ�֣�ȡ�����л��������б�
ʹ��System.getenv()������ȡ��һ��Map���󣬸ö�����������л��������ļ�ֵ�ԡ�
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
������£�
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

��Щ����������ֵ����ͨ��Windows��ϵͳ���������������޸ġ�

��2���ڶ��֣�ȡ��ĳһ�����Ա�����ֵ
���֪��Ҫ��ѯ����������������ֱ��ʹ��System.getenv(key)ֱ��ȡ�øü���ֵ��������ʾ��
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
������£�
javahome=null
path=E:/Program Files/Java/jre1.8.0_25/bin/client;E:/Program Files/Java/jre1.8.0_25/bin;E:/Program Files/Java/jre1.8.0_25/lib/i386;C:\ProgramData\Oracle\Java\javapath;C:\Program Files (x86)\Intel\iCLS Client\;C:\Program Files\Intel\iCLS Client\;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0\;C:\Program Files (x86)\Intel\Intel(R) Management Engine Components\DAL;C:\Program Files\Intel\Intel(R) Management Engine Components\DAL;C:\Program Files (x86)\Intel\Intel(R) Management Engine Components\IPT;C:\Program Files\Intel\Intel(R) Management Engine Components\IPT;C:\Program Files (x86)\NVIDIA Corporation\PhysX\Common;e:\Program Files\QuickStart;C:\Program Files (x86)\Windows Kits\8.1\Windows Performance Toolkit\;C:\Program Files\Microsoft SQL Server\110\Tools\Binn\;e:\Program Files\Java\jdk1.8.0_25\bin;;c:\;
classpath=.;c:\Program Files\Java\jdk1.8.0_25\lib;

3. �����ļ��Ϳ� P379
�����Ҫ���ض�̬���ļ�����ϵͳ�⣬����ʹ������ĺ�����
--����load(String filename)�������ǣ�����Ϊ��̬��ı����ļ�ϵͳ����ָ�����ļ������ش����ļ����ļ�������������������·����������System.load(name)ʵ���ϵ�Ч�ڵ��ã�
Runtime.getRuntime().load(name);
���磺
System.load("/home/avh/lib/libX11.so");
Runtime.getRuntime().load("/home/avh/lib/libX11.so");
--����loadLibrary(String libname)�������ǣ�������libname����ָ����ϵͳ�⣬������ӳ�䵽ʵ��ϵͳ��ķ���ȡ����ϵͳ������System.loadLibrary(name)ʵ���ϵ�Ч�ڵ��ã�
Runtime.getRuntime().loadLibrary(name);
����System.loadLibrary(String)�ǵ��ô˷�����һ�ִ�ͳ����ݵķ�ʽ�������ĳ����ʵ����ʹ�ñ���������JNI���ã������׼�Ĳ����ǽ������������һ�����ļ��У���ΪLibFile����Ȼ��������������һ����̬�ĳ�ʼֵ�趨ֵ��
static {
	System.loadLibrary("LibFile");
}
�����ز���ʼ�������ʱ��Ҳ������ʵ�ֱ�����������ı������롣�������ͬ������ε��ô˷���������Եڶ��μ������ĵ��á�

4. ���ٸ������� P380
����ʹ��System�ĺ���arraycopy()���ٵؽ������鸴�ơ�
void arraycopy(Object src, int srcPos, Object dest, int destPos, int length);
���destΪnull�����׳�NullPointerException�쳣��
���srcΪnull�����׳�NullPointerException�쳣�����Ҳ����޸�Ŀ�����顣

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
������£�
b=0
b=3
b=4
b=5
b=6

5. ȡ��ϵͳʱ�� P381
System����һ����õĺ���������ȡ�õ�ǰϵͳʱ��ĺ���System.currentTimeMillis()���ú������ص��Ǻ���ʱ�䣬��һ��long������������1221960727312��
�ú����ڽ���ϵͳ���ܼ��ʱ��õ��������ڼ�ص�ǰ��ͺ���ֱ���Ӹú������Լ�����δ���ִ�еĺ��뼶ʱ�䣬���磺
long start = System.currentTimeMillis();
//Ҫ��صĴ����
long end = System.currentTimeMillis();
System.out.print(end - start);
��������start��end�����Լ����ִ�е�ʱ�䳤�ȣ�����������������ͬһ�������У���ʱҪ��صĴ���ֲ��ڸ������У���ʱ����ֱ��ʹ������Ĵ�������ô���ִ�е�ʱ��ʱ�䣺
System.out.println("��ǰλ�á���" + System.currentTimeMillis());
���ڳ���ִ�еĹ��������һϵ��ʱ�䣬���Կ�������Щ����ִ�еĽ����Ƕ��٣���������һ�δ���ִ�е�ʱ�䳤��

����֮�⣬������ʹ��System.nanoTime()�����ȷ�Ŀ���ϵͳ��ʱ���ĵ�ǰֵ���Ժ�΢��Ϊ��λ���˷���ֻ�����ڲ����ѹ���ʱ�䡣
�磬����ĳЩ����ִ�е�ʱ�䳤�ȣ�
long startTime = System.nanoTime();
//...
long estimatedTime = System.nanoTime() - startTime;
�ɼ���System.nanoTime()��System.currentTimeMillis()����ȷ��

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
������£�
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

6. ϵͳ�˳����� P383 
System.exit(int status)������ֹ��ǰ�������е�Java���������0״̬���ʾ�쳣��ֹ���÷�������Runtime���е�exit()���������Ե���System.exit(0)�������˳�ϵͳ��
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
Tips��System.exit(0)����breakҲ�ܹ��˳���System.exit(0)�����ò�ֹ��һ��������ڸ��ӵ�JavaӦ�ó����У�������ǿ�����еĳ����˳�ϵͳ�����������ʹ��break��ͬ�ĵط���

7. ִ���������� P384
System.gc()��������������������
����System.gc()ʵ���ϵ�Ч�ڵ��ã�
Runtime.getRuntime().gc();
Tips���в����ڴ�����Ƶ���ص����������ա�

11.1.2 ����ʱ��Runtime P385
System�ĺܶຯ�����ȼ���Runtime.getRuntime()��ʵ����System��������Runtime��ġ�
Runtime�಻ͬ��System�࣬���ĺ����󲿷ֶ����Ǿ�̬�ģ�ֻ��һ����̬����getRuntime()��ÿ��JavaӦ�ó�����һ��Runtime��ʵ����ʹӦ�ó����ܹ��������еĻ��������ӣ�����ͨ���ú���ȡ��һ��Runtime��ʵ����Ӧ�ó����ܴ����Լ���Runtime��ʵ����Ȼ���ٵ��ø��������������
��1���鿴ϵͳ�ڴ�
package test.Runtime;

public class TestRuntimeMemory {
    public static void main(String[] args) {
        System.out.println("�ڴ�������" + Runtime.getRuntime().totalMemory());
        System.out.println("����ڴ�����" + Runtime.getRuntime().maxMemory());
        System.out.println("�����ڴ�����" + Runtime.getRuntime().freeMemory());
    }
}
������£�
�ڴ�������16252928
����ڴ�����259522560
�����ڴ�����15470896
�����ڴ���ֵ��λ���ֽ�byte

��2����ֹJVM�����
ʹ��exit(int status)ͨ������������Ĺر����У���ֹ��ǰ�������е�Java�����������һ������halt(int status)������ǿ����ֹ�������е�Java�������
halt()������ǿ���ԡ�����ȫ�ģ�ͨ������ʹ��exit()��ֻ�����Ȳ����ѵ�����²�ʹ��halt()�����������Ӷϵ����ֲ��ɿ����ء�

��3������ϵͳ����
�򿪼��±���
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

��word��
Runtime.getRuntime().exec("cmd /c start Winword");

ִ��������ͨ���������ļ���ִ��һϵ�е�������ִ��Ant�ű��ȣ����ڱ�д�Ľű��ļ�run.bat������ʹ�����淽ʽ���С�
Runtime.getRuntime().exec("cmd.exe /C start d:/demo/run.bat");

������ʹ��Process���waitFor()�������ȴ���ǰ���̵�ִ�У����б�Ҫ��һֱҪ�ȵ��ɸ�Process�����ʾ�Ľ����Ѿ���ֹ��������ʾ�����Ը���10.2.1 ��һ�л�ý��̵����������ȴ��������н������ټ���Java���̵߳����У�
Process p = Runtime.getRuntime().exec("d:/demo/run.bat");
InputStream in = p.getInputStream();
String line = null;
BufferedReader reader = new BufferedReader(new InputStreamReader(in));
while ((line = reader.readLine()) != null) {
    System.out.println(line);
}
in.close();
p.waitFor(); //�ȴ����̽���

����ʵ�ʵĿ���������Ҫ�ġ����д��һ��Ant�ű�����������һ��Java�ֻ࣬�б�������󣬲��ܹ�����ִ������Ĵ��룬�����øղ�Ant�ű�������class���ļ�����Ȼ����ȴ����̽�������ܹ�����ִ�С�

��4��ʹ�ùرչ��� P386
�رչ���Ӧ�ÿ��ٵ�����乤�������������exitʱ�������Ӧ��Ѹ�ٵعرղ��˳��������û�ע����ϵͳ�رն���ֹ�����ʱ���ײ�Ĳ���ϵͳ����ֻ�����ڹ̶���ʱ���ڹرղ��˳�������ڹرչ����г��Խ����κξ��û�������ִ�г�ʱ��ļ��㶼�ǲ����ǵġ�
���³�����ʾ����ӹرչ��ӵķ������ڷ���addShutDownHook()�ж�����һ�������ڲ��࣬�亯����run()�п���������дϵͳ�˳�����Ҫִ�еĴ��룬����ϵͳ���ݵĻ���ȡ�
package test.Runtime;

import java.util.Scanner;

public class TestRuntimeHook {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("ϵͳ�����˳�����");
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
        Scanner scanner = new Scanner(System.in);//�и�scannerδclose����ʾ
        return scanner.nextLine();
    }
}

11.2 Java�ַ��������� P388 
�����ַ���ʵ�������ⴴ������ϵͳ��Ч�ʴ����˺ܴ����⡣
�����ԣ��Ա�String���StringBuffer��ִ��Ч�ʡ�
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
������£�
139
��ʱ139ms

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
������£�
1
��ʱ1ms

11.2.1 �ַ�����String P389
�ַ����ǳ��������ǵ�ֵ�ڴ���֮���ܸ��ģ��ַ���������֧�ֿɱ���ַ�������ΪString�����ǲ��ɱ�ģ����Կ��Թ���
�ַ���������ܼ򵥣�ֱ�Ӹ�һ���ַ������͵ı�����ֵ���ɣ��磺
String str = "abc";
�ȼ��ڣ�
char data[] = {'a', 'b', 'c'};
String str = new String(data);

String������ķ��������ڼ���ַ����ĵ����ַ����Ƚ��ַ����������ַ�������ȡ���ַ����������ַ����������������ַ���ת��Ϊ��д��Сд��
��1����ȡ�����ַ�
String str = "hello, world!";
System.out.println(str.charAt(10));

��2����ȡ���ַ���
System.out.println(str.substring(1));//ȡ��ĩβ
System.out.println(str.substring(0, 4));//ȡ������endIndex - 1������Ϊ�������ַ����ĳ���ΪendIndex - beginIndex
Ϊȷ��endIndex��Խ�磬����ʹ�����溯��ȡ���ַ������ȣ�
int length();
�ٸ��ݳ��Ȳ��ң�substring(0, str.length() - 1);

��3���Ƚ��ַ���
boolean equals(Object anObject);
boolean equalsIgnoreCase(String str);
Tips��
equals()�����Ƚ������ַ�����ֵ�������ǱȽ�������Ƿ���ͬ�����ʹ��==�Ƚ������ַ�������a==b����ôֻ����������ʵ��ָ��ͬһ���ַ�������ʱ�ŷ���true��
//һ�����Գ������£�
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

��ʱ���ܱȽ������ַ����Ĵ�С���Ա�������򣬿���ʹ����������������
int compareTo(String anotherString); //���ֵ�˳��Ƚ������ַ���
int compareToIngoreCase(String str); //���Դ�Сд

package test.Str;

public class TestString {
    public static void main(String[] args) {
        String str = "hello";
        String str2 = "helln";
        String str3 = "hellp";
        System.out.println(str.compareTo(str2));//1�����Լ򵥵����Ϊ�÷������õ��ַ���ȥ���ο��ַ�����o��n��1�����Ľ��Ϊ1
        System.out.println(str.compareTo(str3));//-1��o��pС1�����Ľ��Ϊ-1
    }
}
�ȽϽ��Ϊһ����������3�������
--���ֵ�˳���String����λ�ڲ����ַ���֮ǰ����ȽϽ��Ϊһ����������
--���ֵ�˳���String����λ�ڲ����ַ���֮����ȽϽ��Ϊһ����������
--�����ַ�����ȣ�����Ϊ0��compareToֻ�ڷ���equals(Object)����trueʱ�ŷ���0��
��4����λ�ַ���
startsWith
endsWith

package test.Str;

public class TestString {
    public static void main(String[] args) {
        String str = "hello";
        System.out.println(str.startsWith("he"));//true
        System.out.println(str.startsWith("ll", 0));//false
        System.out.println(str.startsWith("ll", 2));//true
        System.out.println(str.endsWith("lo"));//true��ֻ��һ����ʽ��û�д�offset����ʽ
    }
}

ʹ�����溯����ȡ��ָ���ַ����ַ�����ԭ���ַ����е�λ�ã�
indexOf
lastIndexOf

�������ֵΪ-1 ����ʾ���������ַ������ַ�����
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

��5��ƥ���ַ��� P390
��һ����ͨ�õĺ��������Ը���������ʽ������ƥ�䣺
boolean matches(String regex);//��֪���ַ����Ƿ�ƥ�������������ʽ

Tips��
������ʽ��д�����ڵ�13���н��⡣

��6������ַ���
���ַ�������ĳһ�����ַ�����Ϊһ���ַ������飬����ʹ�ú�����
String[] split(String regex);//���ݸ���������ʽ��ƥ���ִ��ַ���
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
������£�
h
llo

��7���滻�ַ���
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

��8��ת����Сд
String toLowerCase();
String toUpperCase();

��9����ʽ�����
String�໹�ṩ��������̬�������������ַ������и�ʽ�������
static String format(Locale l, String format, Ojbect... args);//ʹ��ָ�������Ի�������ʽ�ַ����Ͳ�������һ����ʽ���ַ���
static String format(String format, Object...args);//ʹ��ָ���ĸ�ʽ�ַ����Ͳ�������һ����ʽ���ַ���
�ø�ʽ���������ʽ�������PrintStream��PrintWriter�÷���ͬ����̬����������ֱ�����ã������һ�����ӣ�
//
package test.Str;

import java.util.Date;

public class TestString {
    public static void main(String[] args) {
        String str = String.format("%1$tY��%1$tm��%1$td�� %1$tHʱ%1$tm��%1$tS��", new Date());
        System.out.println(str);
    }
}
������£�
2016��06��10�� 18ʱ06��30��

11.2.2 �ַ����ָ���StringTokenizer P391
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
������£�
this
is
a
test

StringTokenizer�ǳ��ڼ����Ե�ԭ����������������ࣨ��Ȼ���´����в�������ʹ����������������Ѱ��˹��ܵ���ʹ��String��split()������java.util.regex����
//��String.split()���������ͬ��
package test.Str;

public class TestString {
    public static void main(String[] args) {
        String[] result = "this is a test".split("\\s");
        for (int i = 0; i < result.length; ++i) {
            System.out.println(result[i]);
        }
    }
}

11.2.3 �̰߳�ȫ�Ŀɱ��ַ�����StringBuffer P392
StringBuffer��һ���̰߳�ȫ�Ŀɱ��ַ����࣬������String���ַ�������ͬ������ͨ��ĳЩ�������ÿ��Ըı�����еĳ��Ⱥ����ݡ����ɽ��ַ�����������ȫ�����ڶ���̣߳������ڱ�Ҫʱ����Щ��������ͬ������������ض�ʵ���ϵ����в����ͺ������Դ���˳�����ģ���˳�������漰��ÿ���߳̽��еķ�������˳��һ�¡�
StringBuffer();
StringBuffer(String str);
StringBuffer�ϵ���Ҫ������append()��insert()������append()����ʼ�ս���Щ�ַ���ӵ���������ĩ�ˣ���insert()��������ָ���ĵ�����ַ���
//
package test.Str;

public class TestString {
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer("abc");
        sb.insert(2, "def");
        System.out.println(sb);//abdefc
    }
}
append�������boolean,char,char[]���Լ���ƫ�����ģ�,CharSequence��int start, int end��,double,float,int,long,Object,String��insertӵ��ͬ�����͵��б�
ͬ���ģ�����ʹ������ķ���ɾ��һ�������ַ�
StringBuffer deleteCharAt(int index);
StringBuffer delete(int start, int end);//ɾ����start��end-1λ�õ��ַ�

StringBuffer sb = new StringBuffer("abc");
sb.deleteCharAt(1);//����Ҫ��ֵ��Ҳ���Ը�ֵ
System.out.println(sb);//ac

����֮�⣬StringBuffer��ӵ����Stringͬ���ĺ�������charAt(),indexOf(),lastIndexOf(),substring(),replace()�ȣ������ṩ�˺���toString()��ת��ΪString����

11.2.4 �ɱ��ַ�����StringBuilder P393
StringBuilder��һ���ɱ���ַ����С������ṩһ����StringBuffer���ݵ�API��������֤ͬ�������౻�������StringBuffer��һ�������滻�������ַ����������������߳�ʹ�õ�ʱ������������ձ飩��������ܣ��������Ȳ��ø��࣬��Ϊ�ڴ����ʵ���У����StringBufferҪ�졣
��StringBuilder�ϵ���Ҫ������append()��insert()��������������Щ�������Խ����������͵����ݡ�
���builder����StringBuilder��ʵ������builder.append(x)��builder.insert(builder.length(), x)������ͬ��Ч����ÿ���ַ�������������һ����������ֻҪ�ַ������������������ַ����еĳ���û�г���������������������µ��ڲ�������������ڲ��������������������Զ�����
Tips��
��StringBuilder��ʵ�����ڶ���߳��ǲ���ȫ�ġ������Ҫ�ڶ���߳���ʹ���ַ���������ʹ��StringBuffer��

11.2.5 ѡ��String��StringBuffer��StringBuilder
���������������£�
--String���ַ�������
--StringBuffer���ַ����������̱߳�����
--StringBuilder���ַ������������̰߳�ȫ��
�򵥵�˵��String���ͺ�StringBuffer���͵���Ҫ����������ʵ���ڣ�String�ǲ��ɱ�Ķ��������ÿ�ζ�String���ͽ��иı��ʱ����ʵ����ͬ��������һ���µ�String����Ȼ��ָ��ָ���µ�String�������Ծ����ı����ݵ��ַ�����ò�Ҫ��String����Ϊÿ�����ɶ��󶼻��ϵͳ���ܲ���Ӱ�죬�ر��ڴ��������ö�������Ժ�JVM��GC�ͻῪʼ�������ٶ���һ�����൱���ġ�
�����ʹ��StringBuffer�࣬����Ͳ�һ���ˣ�ÿ�ν�������StringBuffer��������в����������������µĶ����ٸı�������á�������һ������������Ƽ�ʹ��StringBuffer���ر����ַ������󾭳��ı������¡�����ĳЩ�ر�����£�String������ַ���ƴ����ʵ�Ǳ�JVM���ͳ���StringBuffer�����ƴ�ӣ�������Щʱ��String������ٶȲ�����StringBuffer�����������ر��������ַ����������ɣ�String����Ч����ԶҪ��StringBuffer��ģ�
String str = "This is only a" + " simple" + " test";
StringBuffer builder = new StringBuffer("This is only a").append(" simple").append(" test");
����str������ٶȼ�ֱ̫���ˣ������ʱ��StringBuffer��Ȼ�ٶ��ϸ���һ�㶼��ռ���ơ���ʵ����JVM��һ����Ϸ��ʵ���ϣ�
String str = "This is only a" + " simple" + " test";
���ǣ�
String str = "This is only a simple test";
�����Բ���Ҫ̫���ʱ�䡣��Ҫע����ǣ�����ַ��������������String����Ļ����ٶȾ�û��ô���ˣ��磺
String str2 = "This is only a";
String str3 = " simple";
String str2 = " test";
String str2 = str2 + str3 + str4;

������3�����ʹ�ã������������ȥѡ��
--�����ż���Լ򵥵��ַ�����������ƴ�ӣ���ô����ʹ��String�����㹻�򵥶�����������
--�������Ҫ���������ַ���ƴ�ӡ��ۼӲ�������ʹ��StringBuffer��StringBuilder��
--������ڵ��̵߳Ļ����У�����ʹ��StringBuilder����Ҫ��StringBuffer�죻������ڶ��̵߳Ļ����У�����ʹ��StringBuffer�������̰߳�ȫ�ġ�
��ˣ�StringBuilderʵ���������ǵ���ѡ��ֻ���ڶ��߳�ʱ�ſ��Կ���ʹ��StringBuffer��ֻ�����ַ�����ƴ���㹻��ʱ��ʹ��String��

11.3 Java���ڴ����� P396
11.3.1 ������ Date
��1����������
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
������£�
Mon Jun 13 23:21:39 CST 2016
Mon Jun 13 23:21:39 CST 2016
��2���޸�����
��3���Ƚ�����
��4���������
Ĭ��ʵ����һ��toString����

11.3.2 ���ڸ�ʽ����SimpleDateFormat P397
ToRead

11.3.3 ������Calendar P399
ToRead

11.4 Java���ִ�����P400
11.4.1 ��ѧ������ Math P401
Math���������ִ�л�����ѧ����ķ����������ָ��������ƽ���������Ǻ������ṩ��һϵ�еľ�̬����������ִ�и�����ѧ���������ṩ����ѧ�����б����£�
--����ֵ����
--���ֵ����Сֵ����
--ȡ������
--�����к���
--���뿪������
--��������
--�ǶȺ���
--�������

11.4.2 ������� Random P402
new Random();
Random(long seed);
Tips��ʹ�����Ӵ����������������������ÿ�����ò�ͬ�����ӣ������ͻ������ͬ�����������������֤���������ͬ˳����������ͨ�����ǻ�ʹ��ϵͳ�ĵ�ǰʱ���������Ϊ���ӡ�
Random(System.currentTimeMillis());

����ʹ�����溯�������������͵����ݣ�
--������
--������
--����ֵ
--�ֽ�����

�ܶ�Ӧ�ó���ʹ��Math.random()���÷���������ʹ�ã����ɵ���double��ֵ���磺
public class TestMath {
    public static void main(String[] args) {
        System.out.println(Math.random());
    }
}
������£�
0.3454156494983418
������ֵ����һ�������ֵ������Ϊ������������Ϊ�����������ʹ�á�

11.4.3 ������������ת�� P403
Java�ṩ��8�ֻ����������ͣ�ͬ����JavaҲΪ��Щ�������������ṩ����Ӧ�Ķ����������ͣ�
�����������ͣ�
byte
short
int
long
float
double
char
boolean
�����������ͣ�
Byte
Short
Integer
Long
Float
Double
Character
Boolean

��8���������ͣ�����Charater��Boolean���������⣬�����Ķ������֣���ΪJavaΪ���ǵ���һ�����ṩ��һ������ĸ���Number��
������Number��BigDecimal��BigInteger��Byte��Double��Float��Integer��Long��Short��ĳ��࣬����BigDecimal��BigInteger�ֱ��ʾ���ɱ�ģ����⾫�ȵ��з���ʮ������������������Щ�����У��ʹ�õ���Double��Float��Integer��Long��4���࣬ͨ����Ҫʹ�����������ַ���ת��Ϊ�����ͻ�������ֵ��
��1�������ַ���ת��������
Double���ṩ��һ����̬��parse������Float��Ҳ�ṩ��һ����̬��parse������
package test.Number;

public class TestNumber {
    public static void main(String[] args) {
        double d = Double.parseDouble("123.456");
        float f = Float.parseFloat("123.456");
        System.out.println(d);
        System.out.println(f);
    }
}
������£�
123.456
123.456

��2�������ַ���ת��������
Long���Integer�࣬Ҳ���ṩ��һ����̬��parse���������ַ���ת��Ϊlong��int������ֵ��
package test.Number;

public class TestNumber {
    public static void main(String[] args) {
        long l = Long.parseLong("123456");
        int i = Integer.parseInt("123");
        System.out.println(l);
        System.out.println(i);
    }
}
������£�
123456
123

Integer�໹�ṩ��ת��Ϊ�����ơ��˽��ƺ�ʮ�����Ƶĺ���
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
������£�
1010
12
a
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��12�� Java���ü����� P407
12.1 ��������� P408
12.1.1 Iterator��Enumeration P408
1. ö�٣�Enumeration���ӿ�
Enumeration�ӿڶ����˿��Զ�һ��������༯�е�Ԫ�ؽ���ö�٣�һ�λ��һ�����ķ������Ѿ���Iterator������������³�����˵�ǹ�ʱ�ģ�Ȼ�����Ա����ִ���ǰ�汾�����������ࣨ����Vector��Hashtable��Properties��������ķ���ʹ�á�
Ҫ�鿴Vector�е����ж���һ���취����Vector��get(int index)��������������Ч�ʱȽϵͣ���һ����������Vector��elements()��������һ��Enumeration����
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
������£�
hello
world
nice day

2. ��������Iterator���ӿ�
�Ƽ�ʹ��Iterator��Collection���Ͻ��е�����������������Java���Ͽ�ܣ�Java Collections Framework���е�Enumeration��
Collection�ӿ��ж�����iterator()���������ڷ����ڴ�Collection��Ԫ���Ͻ��е����ĵ�������
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
������£�
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
������£�
[hello, nice day]

Tips����ʹ�÷���������Iterator��Enumeration���ơ�Iterator�����һ����ѡ���Ƴ���������ʹ�ý϶̵ķ���������ʵ��Ӧ�����ȿ���ʹ��Iterator�ӿڡ�

12.1.2 Collections��Collection P409
--Collection�Ǹ�java.util�µĽӿڣ��ṩ�˶Լ��϶�����л���������ͨ�ýӿڷ�������Java������кܶ�����ʵ�֣��ӿڵ�������Ϊ���־���ļ����ṩ����󻯵�ͳһ������ʽ��
--Collections�Ǹ�java.util�µ�ʵ���࣬�������и����йؼ��ϲ����ľ�̬�������ṩһϵ�о�̬����ʵ�ֶԸ��ּ��ϵ������������̰߳�ȫ���Ȳ���������һ�������࣬������Java��Collection��ܡ�
��ˣ�Collections�Ƕ�Collection��������и��ֲ����ķ����ࡣ
1. Collection
�ýӿ�Ϊ��ʵ�����ṩ��ͳһ�Ĳ����ӿڷ�����
--����һ������Ԫ��
add
addAll
--ɾ��һ������Ԫ��
remove
removeAll
clear
isEmpty
size
--�ж��Ƿ����һ������Ԫ��
contains
containsAll
--����Ԫ�ؼ�����������
iterator
toArray
<T>T[] toArray(T[] a);//�������������ʱ������ָ�����������ʱ������ͬ
ʵ�ָýӿڵ����඼����ʹ����Щ����

2. Collections
Collections��ȫ����Collection�Ͻ��л򷵻�Collection�ľ�̬������ɡ���������Collection�ϲ����Ķ�̬�㷨��������װ��������װ��������ָ��Collection֧�ֵ���Collection���Լ������������ݡ�
�����ṩ��һϵ�й��ܵľ�̬����������
--������
boolean addAll(Collection<? super T> c, T...elements);
--��䣺
void fill(List<? super T> list, T obj);//ʹ��ָ��Ԫ���滻ָ���б��е�����Ԫ��
--����
void copy(list<? super T> dest, List<? super T> src);
--�滻
boolean replaceAll(List<T> list, T oldVal, T newVal);
--����
void sort(List<T> list);
void sort(List<T> list, Comparator<? super T> c);
--����
int binarySearch(List<? extends Comparable<? super T> list, T key);//ʹ�ö�������������ָ���б��Ի��ָ������
int binarySearch(List<? extends T> list, T key, Comparator<? super T> c);//ʹ�ö�������������ָ���б��Ի��ָ������
--����λ�ã�
int indexOfSubList(List<?> source, List<?> target);
int lastIndexOfSubList(List<?> source, List<?> target);
--��������СԪ��
max(Collection<? extends T> coll);//����Ԫ�ص���Ȼ˳�򣬷��ظ���Collection�����Ԫ��
max(Collection<? extends T> coll, Comparator<? super T> comp);//����ָ���Ƚ���������˳�򣬷��ظ���Collection�����Ԫ��
min(Collection<? extends T> coll);
min(Collection<? extends T> coll, Comparator<? super T> comp);

12.1.3 Arrays������ P411
��Java���еġ��洢���������һ�������󡱵������У�����������Ч�ʵ�һ�֡���ͨ������Ҳ������ȱ�㣺Ч�ʸߣ��������̶����޷���̬�ı䣻�޷��ж�����ʵ�ʴ��ж���Ԫ�أ�lengthֻ�Ǹ������������������
Arrays�࣬ר�������������顣��ӵ��һ�龲̬���������������������飨��������������ĸ��ַ�������Щ�������������鶼��Java�����������͵����飬����ĺ����÷��кܶ࣬�Բ�����������Ϊ�����⣺
--���� fill()
--���� copyOf()
--�Ƚ� equals()
--���� binarySearch()
--���� sort()
���е����鸳ֵ������System.arraycopy()���ơ�
public class TestEnum {
    static public void main(String args[]) {
        int array[] = new int[5];
        System.out.print(array.length);//5
    }
}

12.1.4 Dictionary�ֵ� P411
Dictionary���ֵ��࣬�����κοɽ���ӳ�䵽��Ӧֵ���ࣨ��Hashtable���ĳ����ࡣÿ������ÿ��ֵ����һ���������κ�һ��Dictionary�����У�ÿ���������һ��ֵ�����������һ��Dictionary��һ�������Ϳ��Բ�����������Ԫ�ء��κη�null���󶼿�����������ֵ��
�ó������ṩ�����³��󷽷�������ʵ�ֶ�Dictionaryʵ����ö�١����ҡ�ɾ�������������������ȡ�������С���ж��Ƿ�Ϊ�գ�
ö�٣�abstract Enumeration<V> elements();
�ۻ᣺abstract V get(Object key);
�ж�Ϊ�գ�abstract boolean isEmpty();
��ö�٣�abstract Enumeration<K> keys();
��ֵ��abstact V put(K key, V value);
ɾ����abstract V remove(Object key);
ȡ�ô�С��abstract int size();

��Щ�����ǳ���ģ������������ʵ�����ǡ�ĿǰDIctionary��ֻ��Hashtable��ʹ�ã������Hashtable�о��ṩ��������Щ�������ľ���ʵ�֡�Properties�̳���Hashtable�������Ҳӵ����Щ�����Ĺ��ܡ�
Tips�������ѹ�ʱ���µ�ʵ��Ӧ��ʵ��Map�ӿڣ���������չ���ࡣ

12.1.5 Queue���� P412
��ͼ12-1 �п��Կ�����ʵ��Collection�ӿڵĵ�һ������Queue�����࣬�ýӿڻ���һ����������AbstractQueue���̶���һϵ�е�ʵ���࣬�����ȶ���PriorityQueue������ArrayBlockingQueue��XXX��
1. Queue���нӿ�
Queue���˻�����Collection�����⣬���ṩ�����Ĳ��롢��ȡ�ͼ�������ÿ������������������ʽ��һ���׳��쳣������ʧ��ʱ������һ�ַ���һ������ֵ��null��false������ȡ���ڲ���������������ĺ�һ����ʽ������ר��Ϊ���������Ƶ�Queueʵ����Ƶģ��ڴ����ʵ���У������������ʧ�ܡ�
���� add offer
��ȡ�Ƴ� remove poll
��ȡ���Ƴ� element peek

remove()��poll()�������Ƴ��ͷ��ض��е�ͷ�����״Ӷ������Ƴ��ĸ�Ԫ���Ƕ���������ԵĹ��ܣ����ò����ڸ���ʵ�����ǲ�ͬ�ġ�remove()��poll()�������ڶ���Ϊ��ʱ����Ϊ������ͬ��remove()�����׳�һ���쳣����poll()�����򷵻�null��
element()��peek()���ص����Ƴ����е�ͷ��

2. AbstractQueue������
�����ṩĳЩQueue�����ĳ���ʵ�֡����������ر𶮣�

3. ���ȼ�PriorityQueue P413
һ���������ȼ��ѵ��ް����ȼ����С����ȼ����е�Ԫ�ذ�������Ȼ˳��������򣬻��߸��ݹ������ʱ�ṩ��Comparator�������򣬾���ȡ������ʹ�õĹ��췽����������ʹ��nullԪ�ء�������Ȼ˳������ȼ����л���������벻�ɱȽϵĶ���
�˶��е�ͷ�ǰ�ָ�����򷽷�ȷ������СԪ�ء�������Ԫ�ض�����Сֵ����ͷ������һ��Ԫ�ء���ѡ�񷽷�������ġ����л�ȡ����poll()��remove()��peek()��element()���ʴ��ڶ���ͷ��Ԫ�ء�
���ȼ��������޽�ģ�������һ���ڲ����������������ڴ洢����Ԫ�ص������С����ͨ�����ٵ��ڶ��еĴ�С�����Ų��������ȼ��������Ԫ�أ����������Զ����ӡ�����ָ���������Ӳ��Ե�ϸ�ڡ�
���༰�������ʵ����Collection��Iterator�ӿڵ����п�ѡ����������iterator()���ṩ�ĵ���������֤���κ��ض���˳��������ȼ������е�Ԫ�ء������Ҫ��˳��������뿼��ʹ��Arrays.sort(pq.toArray())��
��������
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
������£�
111
abc

������������Ȼ�������������Ϊ111��abc����ANSI���У�111����abcǰ��
Tips����ʵ�ֲ���ͬ���ġ��������߳��е������߳��޸��˶��У�����Щ�̲߳�Ӧͬʱ����PriorityQueueʵ�����෴����ʹ���̰߳�ȫ��PriorityBlockingQueue�ࡣ
queue.add("222");
queue.add("111");
queue.add("333");
System.out.println(queue.poll());
System.out.println(queue.poll());
System.out.println(queue.element());
���Ϊ��
111
222
333

12.2 �б���List P414
List�ӿڶ�Collection�����˼򵥵����䣬����ʵ���ೣ�õ���ArrayList��LinkedList��
--ArrayList��һ�������������ʽ���д洢����������ٶȼ��졣
--LinkedList���ڲ�ʵ���������ʺ����������м���ҪƵ�����в����ɾ��������

12.2.1 ������AbstractList��AbstractSequentialList P414
AbstractList�ṩList�ӿڵĳ���ʵ�֣�������޶ȵؼ���ʵ�֡�������ʡ����ݴ洢�������飩֧�ֵĸýӿ�����Ĺ��������������ķ������ݣ���������Ӧ����ʹ��AbstractSequentialList�������Ǵ��ࡣ
�ڼ̳�AbstractListʱ��
--���Ҫʵ�ֲ����޸ĵ��б�ֻ����չ���࣬���ṩget(int)��size()������ʵ�֡�
--Ҫʵ�ֿ��޸ĵ��б�����������дset(int,E)������
--����б�Ϊ�ɱ��С�������������дadd(int,E)��remove(int)������
Ŀǰ�̳���AbstractList��ʵ������ArrayList��Vector��
AbstractSequentialList�����б���б��������ʵ�֡�������ʡ�������AbstractList�����������AbstractList�ĵ�����������������ʷ�����AbstractSequentialList�ĵ�������������������ʷ�����

12.2.2 ����LinkedList P415
LinkedList��List�ӿڵ������б�ʵ�֡�ʵ�����п�ѡ���б������������������Ԫ�أ�����null��������ʵ��List�ӿ��⣬LinkedList�໹Ϊ���б�Ŀ�ͷ����βget()��remove()��add()Ԫ���ṩ��ͳһ��������������Щ�������������б�������ջ�����л�˫�˶��С����£�
addFirst
addLast

boolean offerFirst
boolean offerLast

getFirst
getLast

removeFirst
removeLast

peekFirst//��ȡ���Ƴ�
peedLast

pollFirst//��ȡ�Ƴ�
pollLast

���в������ǰ���˫�������б����Ҫִ�еġ����б��б������Ĳ������ӿ�ͷ���β�����б�

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
������£�
abc
def
Tips����ʵ�ֲ���ͬ���ġ��������߳�ͬʱ����һ�������б�����������һ���̴߳ӽṹ���޸��˸��б��������뱣���ⲿͬ��������ڴ���ʱ�����һ�������Է�ֹ���б��������Ĳ�ͬ�����ʣ�������ʾ��
List list = Collections.synchronizedList(new LinkedList(...));

12.2.3 �ɱ����� ArrayList P416
ArrayList��List�ӿڵĴ�С�ɱ������ʵ�֡�ʵ�������п�ѡ�б���������������null���ڵ�����Ԫ�ء�����ʵ��List�ӿ��⣬���ṩһЩ�����������ڲ������洢�б������Ĵ�С���˴�����ϵ�ͬ��Vector�࣬���˴����ǲ�ͬ���ģ���
ÿ��ArrayListʵ������һ��������ָ�����洢�б�Ԫ�ص�����Ĵ�С�����������ٵ����б�Ĵ�С��������ArrayList�в������Ԫ�أ�������Ҳ�Զ���������δָ���������Ե�ϸ�ڣ���Ϊ�ⲻֻ�����Ԫ�ػ������̯�̶�ʱ�俪�������򵥡�����ʹ��Ĭ�Ϲ��캯����������Ϊ10���б�Ҳ���Գ�ʼ��ָ��������С��
ArrayList();//����һ����ʼ����Ϊ10�Ŀ��б�
ArrayList(int initialCapacity);//����һ������ָ����ʼ�����Ŀ��б�

����Ӵ���Ԫ��ǰ������ʹ��ensureCapacity����������ArrayListʵ�������������Լ��ٵ���ʽ�ٷ�����������亯����ʽ���£�
void ensureCapacity(int minCapacity);//��������

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
������£�
3
Tips����ʵ�ֲ���ͬ���ġ��������߳�ͬʱ����һ��ArrayListʵ��������������һ���̴߳ӽṹ���޸����б���ô�����뱣���ⲿͬ��������ڴ���ʱ��ɣ��Է�ֹ������б���в�ͬ���ķ��ʣ�
List list = Collections.synchronizedList(new ArrayList(...));

12.2.4 ����Vector P416
Vector�����ʵ�ֿ������Ķ������飬������һ��������ʹ�������������з��ʡ����ǣ�Vector�Ĵ�С���Ը�����Ҫ���������С������Ӧ����Vector�������ӻ��Ƴ���Ĳ�����
import java.util.Vector;

public class TestList {
    static public void main(String args[]) {
        Vector v = new Vector();
        v.addElement("one");
        v.addElement("two");
        v.addElement("three");
        v.addElement("two");
        System.out.println(v);

        v.removeElement("two");//ֻ��ɾ������һ��
        System.out.println(v);

        v.addElement(111);//�ڲ�ָ���������͵�����£����Է����������Ͷ������v����ΪVector<String>����˴����뱨��
        System.out.println(v);
    }
}
������£�
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
        // for (Object s : v) { //���ﲻ����String�����Ͳ�ƥ��
            // System.out.println(s);
        // }
    }
}
������£�
size:3
test0
test2
test2

Vector�ڲ��������з����ž������ص����ã�ͨ����ʹ��Vector��Ϊ�������ݵĶ���

12.2.5 ��ջ P418
Stack���ʾ����ȳ���LIFO���Ķ����ջ��ͨ��5����������Vector��������չ������������Ϊ��ջ���ṩ��ͨ����push()��pop()�������Լ�ȡ��ջ�����peek()���������Զ�ջ�Ƿ�Ϊ�յ�empty()�������ڶ�ջ�в����ȷ������ջ������ ��search()�ķ�����
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
������£�
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

12.3 ������Set P419
Set�ӿ�Ҳ��Collection��һ����չ����List��ͬ���ǣ�Set�еĶ���Ԫ�ز����ظ������þ���ʵ����HashSet��TreeSet�ࡣ
--HashSet�ܿ��ٶ�λһ��Ԫ�أ����Ƿŵ�HashSet�еĶ�����Ҫʵ��hashCode()��������ʹ�ù�ϣ����㷨��
--TreeSet�򽫷������е�Ԫ�ذ����ţ����Ҫ��������еĶ����ǿ�����ģ�����õ��˼��Ͽ���ṩ����������ʵ����Comparable��Comparator��һ�����ǿ�����ģ�����Ӧ��ʵ��Comparable�ӿڡ���ʱ����������ͬ�������㷨���Ͳ���Ҫ�ֱ���ÿ�������ظ�������ͬ�������㷨��ֻҪʵ��Comparator�ӿڼ��ɡ����Ͽ���л���������ʵ�õĹ����ࣺCollections��Arrays��Collections�ṩ�˶�һ��Collection���������������򡢸��ơ����Һ�����һЩ�ǳ����õķ�����Arrays���Ƕ�һ������������ƵĲ�����

12.3.1 ������AbstractSet��ӿ�SortedSet
Set��һ��������ͽӿڣ��ֱ���AbstractSet��SortedSet��AbstractSetΪʵ�ֹ�ϣ��׼����SortedSetΪʵ�־��������ܵ�TreeSet��׼����
1. AbstractSetͨ����չ������ʵ��һ��Set�Ĺ�����ͨ����չAbstractCollection��ʵ��Collection�Ĺ�������ͬ�ģ����˴���������е����з����͹��췽�����������Set�ӿ���ǿ�ӵĶ������ƣ����磬add()����������һ������Ķ��ʵ����ӵ�һ��Set�У���
Tips�����ಢû����дAbstractCollection���е��κ�ʵ�֣������������equals()��hashCode()��ʵ�֡�

2. ����ӿ�SortedSet
SortedSet��һ���ṩ�˹���Ԫ�ص����������Set����ЩԪ��ʹ������Ȼ˳��������򣬻��߸���ͨ���ڴ�������Setʱ�ṩ��Comparator��������������ЩԪ�ض������ǿɻ���Ƚϵġ�
����ͨ������Setʵ���඼Ӧ���ṩ4������׼�����췽����
--void���޲��������췽����������һ���յ�����Set������Ԫ�ص���Ȼ˳���������
--����һ��Comparator���Ͳ����Ĺ��췽����������һ���յ�����Set������ָ���ıȽ�����������
--����һ��Collection���Ͳ����Ĺ��췽����������һ���µ�����Set����Ԫ���������ͬ������Ԫ�ص���Ȼ˳���������
--����һ��SortedSet���Ͳ����Ĺ��췽����������һ���µ�����Set����Ԫ�غ����򷽷������������Set��ͬ��
TreeSet����һ������ӿ�SortedSet��ʵ�֡�

12.3.2 ��ϣ����HashSet P420
HashSet�ɹ�ϣ��ʵ������һ��HashMapʵ����֧�֣�Ϊ���������ṩ���ȶ����ܣ���Щ������������add()��remove()��contains()��size()���ٶ���ϣ��������ЩԪ����ȷ�طֲ���Ͱ�С��Դ�set()���е��������ʱ����HashSetʵ���Ĵ�С��Ԫ�ص��������͵ײ�HashMapʵ����Ͱ���������ġ��������ĺͳɱ�������ˣ�����������ܺ���Ҫ����Ҫ����ʼ�������õ�̫�ߣ��򽫼����������õ�̫�ͣ���
Ϊ���ڸ����ʵ���б������ݣ����ݶ������ӵ��hashCode()��������������
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
������£�
Student{num=3, name='wangwu'}
Student{num=2, name='lisi'}
Student{num=1, name='zhangsan'}
Student{num=1, name='zhangsan'}

�ɴ˿ɼ���HashSet��һ�������List������ӵ�Ԫ�ر���ʵ��hashCode()������
Tips����ʵ�ֲ���ͬ���ġ��������߳�ͬʱ����һ����ϣSet������������һ���߳��޸��˸�Set����ô�����뱣���ⲿͬ������ͨ����ͨ������Ȼ��װ��Set�Ķ���ִ��ͬ����������ɵġ�������������Ķ�����Ӧ��ʹ��Collections.synchronizedSet����������װ��Set������ڴ���ʱ�����һ�������Է�ֹ�Ը�Set��������ķ�ͬ�����ʡ�
Set s = Collections.synchronizedSet(new HashSet(...));

12.3.3 ������TreeSet P421
TreeSet�ǻ���TreeMap��NavigableSet�ࡣʹ��Ԫ�ص���Ȼ˳���Ԫ�����򣬻���ݴ���Setʱ�ṩ��Comparator��������ȡ����ʹ�õĹ��췽������ʵ��Ϊ����������add��remove��contains���ṩ�ܱ�֤��log(n)ʱ�俪����
�Լ�д����Ҫ�ܹ���ӵ�TreeSet�н������򣬸�����Ҫʵ��Comaprable�ӿڡ����£�
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
        //���±Ƚ������Լ��ӵ�
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
        System.out.println(hs.add(new Student(1, "zhangsan2")));//����ʧ��

        Iterator<Student> iter = hs.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
������£�
false
Student{num=1, name='zhangsan'}
Student{num=2, name='lisi'}
Student{num=3, name='wangwu'}

//ԭ���д���
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
            ret = name.compareTo(s.name);//������һ������ʹ��name�Ƚ�
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
������£�
true
Student{num=1, name='zhangsan'}
Student{num=1, name='zhangsan2'}
Student{num=2, name='lisi'}
Student{num=3, name='wangwu'}

�ɴ˿ɼ���TreeSet��һ�������List������ӵ�Ԫ�ر���ʵ�ֿɱȽϵĽӿڡ�

//����ʹ�ö���Ĺ������Ĵ���
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

class Student {
    protected int num;//�޸�Ϊprotected��ͬһ���ļ������Ϳ��Է��ʵ�
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
������£�
Student{num=1, name='zhangsan'}
Student{num=1, name='zhangsan2'}
Student{num=2, name='lisi'}
Student{num=3, name='wangwu'}

//�������޸�Ϊ�������������ʽ
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

class Student {
    protected int num;//�޸�Ϊprotected��ͬһ���ļ������Ϳ��Է��ʵ�
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
Tips����ʵ�ֲ���ͬ���ġ��������߳�ͬʱ����һ��TreeSet������������һ���߳��޸��˸�Set����ô�����뱣���ⲿͬ������һ����ͨ������Ȼ��װ��Set�Ķ���ִ��ͬ����������ɵġ���������������Ķ�����Ӧ��ʹ��Collections.synchronizedSortedSet����������װ����Set���˲�������ڴ���ʱ���У��Է�ֹ��Set��������ķ�ͬ�����ʣ�
SortedSet s = Collections.synchronizedSortedSet(new TreeSet(...));

12.4 ӳ����Map P423
Map��һ�ְѼ������ֵ������й�������������һ��ֵ�����ֿ�����һ��Map�����ڼ�������˵����Setһ����һ��Map�����еļ����������ظ�������Ϊ�˱��ֲ��ҽ����һ�£����������������һ������õ��Ǹ�����������Ӧ��ֵ����ʱ���������ˡ����Լ���Ψһ�Ժ���Ҫ��Ҳ�Ƿ��ϼ��ϵ����ʵġ���Ȼ��ʹ�ù����У�ĳ�������Զ�Ӧ��ֵ������ܻᷢ���仯����ʱ�ᰴ�����һ���޸ĵ�ֵ���������Ӧ��
Map�����ֱȽϳ��õ�ʵ�֣�HashMap��TreeMap��
--HashMapҲ�õ��˹�ϣ����㷨���Ա���ٲ���һ������
--TreeMap�ǶԼ������ţ����������һЩ��չ�ķ�������firstKey()��lastKey()�ȣ������Դ�TreeMap��ָ��һ����Χ��ȡ������Map��
����ֵ�Ĺ����ܼ򵥣���put(Object key, Object value)��������get(Object key)�ɵõ����key��������Ӧ��ֵ����

12.4.1 ������AbstractMap��ӿ�SortedMap��NavigableMap
Map��һ��������������ӿڣ��ֱ���AbstractMap��SortedMap��NavigableMap��NavigableMap��SortedMap���ӽӿڡ�AbstractΪʵ�ֹ�ϣ��׼����SortedMapΪʵ�־��������ܵ�TreeMap��׼����
1. ��ϣ������AbstractMap
�����ṩMap�ӿڵĳ���ʵ�֣�������޶ȵؼ���ʵ�ִ˽ӿ�����Ĺ�������ʵ���˶�Hashӳ��ļ��ֲ�����
--�����ֵV put(K key, V value);
--ɾ����ֵV remove(Object key);���������һ������ӳ���ϵ������Ӵ�ӳ�����Ƴ���
--ȡ�ü�ֵV get(Object key);������ָ������ӳ���ֵ�������ӳ�䲻�����ü���ӳ���ϵ���򷵻�null��
--ȡ�ü�����Set<K> keySet();�����ش�ӳ���а����ļ���Set��ͼ��
Ҫʵ�ֲ����޸ĵ�ӳ�䣬�����Աֻ��Ҫ��չ���ಢ�ṩentrySet()������ʵ�ּ��ɣ��÷���������ӳ���ӳ���ϵSet��ͼ��ͨ�������ص�set()��������AbstractSet��ʵ�֡���set()��֧��add()��remove()�������������Ҳ��֧��remove()������
Ҫʵ�ֿ��޸ĵ�ӳ�䣬�����Ա����������д�����put()�����������׳�UnsupportedOperationException����entrySet().iterator()���صĵ�����Ҳ��������ʵ����remove()������

2. ����ӿ�SortedMap
�ýӿڽ�һ���ṩ���ڼ������������Map����ӳ���Ǹ����������Ȼ˳���������ģ����߸���ͨ���ڴ�������ӳ��ʱ�ṩ��Comparator����������Ϊ������ӳ�����������������������ֱ�����ȡ�õ�һ�������һ������
K firstKey();
K lastKey();
��������ӳ������м�������ʵ��Comparable�ӿڣ����߱�ָ���ıȽ������ܣ������⣬������Щ���������ǿɻ���Ƚϵģ�������ӳ���������������k1��k2ִ��k1.compareTo(k2)����comparator.compare(k1,k2)���������׳�ClassCastException����ͼΥ�������ƽ�����Υ������ķ������߹��췽�������׳�ClassCastException��

3. �����ӿ�NavigableMap
�ýӿ���չ��SortedMap��������Ը�������Ŀ�귵����ӽ�ƥ����ĵ�������������lowerEntry()��floorEntry()��ceilingEntry()��higherEntry()�ֱ𷵻�С�ڡ�С�ڵ��ڡ����ڵ��ڡ����ڸ������ļ�������Map.Entry������������������ļ����򷵻�null�����Ƶģ�����lowerKey()��floorKey()��ceilingKey()��higherKey()ֻ���ع����ļ���������Щ������Ϊ������Ŀ�����Ǳ�����Ŀ��Ƶġ�
���⣬�˽ӿڻ�������firstEntry()��pollFirstEntry()��lastEntry()��pollLastEntry()���������Ƿ��ػ��Ƴ���С������ӳ���ϵ��������ڣ������򷵻�null��

12.4.2 ��ӳ��TreeMap
TreeMap�ȼ̳��˹�ϣ������AbstractMap����ʵ��������ӿ�SortedMap�͵����ӿ�NavigableMap�����ǻ��ں������Red-Black tree���㷨��ʵ�֡���ӳ������������Ȼ˳��������򣬻��߸��ݴ�����ɽʱ�ṩ��Comparator�������򣬾���ȡ����ʹ�õĹ��췽����
TreeMap();
TreeMap(Comparator<? super K> comparator);
��ʵ��ΪcontainsKey()��get()��put()��remove()�����ṩ�ܱ�֤��log(n)ʱ�俪����
��TreeSet���ƣ��Լ�д����Ҫ�ܹ���ӵ�TreeMap�н�������Ҳ��Ҫʵ��Comparable�ӿڻ�Comparator��
//
import java.util.Iterator;
import java.util.TreeMap;

class Student {
    protected int num;//�޸�Ϊprotected��ͬһ���ļ������Ϳ��Է��ʵ�
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
������£�
A=Student{num=3, name='wangwu'}
B=Student{num=1, name='zhangsan'}
C=Student{num=2, name='lisi'}
D=Student{num=1, name='zhangsan2'}
{A=Student{num=3, name='wangwu'}, B=Student{num=1, name='zhangsan'}, C=Student{num=2, name='lisi'}, D=Student{num=1, name='zhangsan2'}}

�ɴ˿ɼ���TreeMap��һ�������Map������ӵ�Ԫ�ر���ʵ�ֿɱȽϵĽӿڡ�
Tips����ʵ�ֲ���ͬ���ġ��������߳�ͬʱ����һ��ӳ�䣬������������һ���̴߳ӽṹ���޸��˸�ӳ�䣨�ṹ�ϵ��޸���ָ��ӻ�ɾ��һ������ӳ���ϵ�Ĳ��������ı������� ����ϵ��ֵ���ǽṹ�ϵ��޸ģ���������뱣���ⲿͬ������һ����ͨ������Ȼ��װ��ӳ��Ķ���ִ��ͬ����������ɵġ���������������Ķ�����Ӧ��ʹ��Collections.synchronizedSortedMap()����������װ����ӳ�䡣����ڴ���ʱ�����һ�������Է�ֹ��ӳ���������Ĳ�ͬ�����ʣ�������ʾ��
SortedMap m = Collections.synchronizedSortedMap(new TreeMap(...));

12.4.3 ��ϣӳ��HashMap P426
�����ǻ��ڹ�ϣ���Map�ӿڵ�ʵ�֡���ʵ���ṩ���п�ѡ��ӳ�������������ʹ��nullֵ��null�������˷�ͬ��������ʹ��null֮�⣬HashMap����Hashtable������ͬ�������಻��֤ӳ���˳���ر���������֤��˳���ò��䡣
��ʵ�ּٶ���ϣ������Ԫ���ʵ��طֲ��ڸ�Ͱ֮�䣬��Ϊ��������get()��put()�ṩ�ȶ������ܡ�����Collection��ͼ�����ʱ����HashMapʵ���ġ���������Ͱ�������������С��������ֵӳ���ϵ�����ɱȽϡ����ԣ�����������ܺ���Ҫ����Ҫ����ʼ�������õ�̫�ߣ��򽫼����������õ�̫�ͣ���
HashMap��ʵ������������Ӱ�������ܣ���ʼ�����ͼ������ӡ������ǹ�ϣ����Ͱ����������ʼ����ֻ�ǹ�ϣ���ڴ���ʱ�����������������ǹ�ϣ�����������Զ�����֮ǰ���Դﵽ������һ�ֳ߶ȡ��������ϣ���е���Ŀ�������˼��������뵱ǰ�����ĳ˻�ʱ����Ҫ�Ըù�ϣ�����rehash()���������ؽ��ڲ����ݽṹ�����Ӷ���ϣ�����д�Լ������Ͱ����
ͨ����Ĭ�ϼ������ӣ�0.75 ����ʱ��Ϳռ�ɱ���Ѱ��һ�����С��������ӹ�����Ȼ�����˿ռ俪������ͬʱҲ�����˲�ѯ�ɱ����ڴ����HashMap��Ĳ����У�����get��put����������ӳ����һ�㣩�������ó�ʼ����ʱӦ�ÿ��ǵ���ӳ�����������Ŀ������������ӣ��Ա�����޶ȵؼ���rehash���������������ʼ�������������Ŀ�����Լ������ӣ��򲻻ᷢ��rehash������
�����ṩ��3�����õĹ��캯����
HashMap();//��ʼ����16��Ĭ�ϼ�������0.75
HashMap(int initialCapacity);//ָ����ʼ������Ĭ�ϼ�������0.75
HashMap(int initialCapacity, float loadFactor);//ָ����ʼ������ָ����������

����ܶ�ӳ���ϵҪ�洢��HashMapʵ���У�������ڰ���ִ���Զ���rehash������������������˵��ʹ���㹻��ĳ�ʼ������������ʹ��ӳ���ϵ�ܸ���Ч�ش洢��
��HashSet���ƣ��Լ�д����Ҫ�ܹ���ӵ�HashMap�У�Ҳ��Ҫʵ��hashCode()������
//
import java.util.HashMap;
import java.util.Iterator;

class Student {
    protected int num;//�޸�Ϊprotected��ͬһ���ļ������Ϳ��Է��ʵ�
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
������£�
A=Student{num=3, name='wangwu'}
B=Student{num=1, name='zhangsan'}
C=Student{num=2, name='lisi'}
D=Student{num=1, name='zhangsan2'}
{A=Student{num=3, name='wangwu'}, B=Student{num=1, name='zhangsan'}, C=Student{num=2, name='lisi'}, D=Student{num=1, name='zhangsan2'}}

�ɴ˿ɼ���HashMap��һ�������Map������ӵ�Ԫ�ر���ʵ��hashCode()������
Tips����ʵ�ֲ���ͬ���ġ��������߳�ͬʱ����һ����ϣӳ�䣬����������һ���̴߳ӽṹ���޸��˸�ӳ�䣨�ṹ�ϵ��޸���ָ��ӻ�ɾ��һ������ӳ���ϵ���κβ��������ı���ʵ���Ѿ������ļ���ϵ��ֵ���ǽṹ�ϵ��޸ģ����������뱣���ⲿͬ������һ��ͨ������Ȼ��װ��ӳ��Ķ������ͬ����������ɡ���������������Ķ�����Ӧ��ʹ��Collections.synchronizedMap()����������װ����ӳ�䡣����ڴ���ʱ�����һ�������Է�ֹ��ӳ���������ķ�ͬ�����ʣ����£�
Map m = Collections.synchronizedMap(new HashMap(...));

12.4.4 �����ϣӳ��LinkedHashMap P427
������Map�ӿڵĹ�ϣ��������б�ʵ�ֵģ����п�Ԥ֪�ĵ���˳�򡣴�ʵ����HashMap�Ĳ�֮ͬ������ǰ��ά����һ��������������Ŀ��˫�������б��������б����˵���˳�򣬸õ���˳��ͨ�����ǽ������뵽ӳ���е�˳�򣨲���˳�򣩡������ӳ�������²�����������˳����Ӱ�졣
��ʵ�ֿ����ÿͻ�����δָ���ġ���HashMap����Hashtable�����ṩ��ͨ��Ϊ�������µ���������ͬʱ����������TreeMap��صĳɱ���ʹ������������һ����ԭ��˳����ͬ��ӳ�丱��������ԭӳ���ʵ���޹أ�
void foo(Map m) {
    Map copy = new LinkedHashMap(m);
}
�����ṩ����Ĺ��췽�����������ӹ�ϣӳ�䣬�ù�ϣӳ��ĵ���˳���������������Ŀ��˳�򣬴ӽ��ڷ������ٵ����ڷ�������˳�򣨷���˳�򣩡�����ӳ����ʺϹ���LRU���档����put()��get()�������������Ӧ����Ŀ���ٶ�������ɺ��������ڣ���putAll()������ָ��ӳ�����Ŀ���������ṩ�ļ�����ֵӳ���ϵ��˳��Ϊָ��ӳ���ÿ��ӳ���ϵ����һ����Ŀ���ʡ�
����ĺ������ڹ���һ����ָ����ʼ�������������Ӻ�����ģʽ�Ŀ�LinkedHashMapʵ����
LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder);
--initialCapacity����ʼ������
--loadFactor���������ӡ�
--accessOrder������ģʽ��������ڷ���˳����Ϊtrue��������ڲ���˳����Ϊfalse��
����nullԪ�أ���HashMapһ��������Ϊ����������add��contains��remove���ṩ�ȶ������ܣ��ٶ���ϣ������Ԫ����ȷ�طֲ���Ͱ�С�����������ά�������б�Ŀ�֧�������ܺܿ��ܱ�HashMap��ѷһ�������һ�����⣺LinkedHashMap��Collection��ͼ����ʱ����ӳ��Ĵ�С�ɱ�����HashMap����ʱ��ܿ��ܿ�֧�ϴ���Ϊ������Ҫ��ʱ�����������ɱ�����
���ӵĹ�ϣӳ���������Ӱ�������ܵĲ�������ʼ�����ͼ������ӡ����ǵĶ�����HashMap�������ơ�Ϊ��ʼ����ѡ��ǳ��ߵ�ֵ�Դ����Ӱ��ȶ�HashMapҪС����Ϊ����ĵ���ʱ�䲻��������Ӱ�졣
//
import java.util.Iterator;
import java.util.LinkedHashMap;

class Student {
    protected int num;//�޸�Ϊprotected��ͬһ���ļ������Ϳ��Է��ʵ�
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
Tips����ʵ�ֲ��������ġ��������߳�ͬʱ�������ӵĹ�ϣӳ�䣬����������һ���̴߳ӽṹ���޸��˸�ӳ�䣬�������뱣���ⲿͬ������һ��ͨ������Ȼ��װ��ӳ��Ķ������ͬ����������ɡ���������������Ķ�����Ӧ��ʹ��Collections.synchronizedMap()����������װ����ӳ�䡣����ڴ���ʱ�����һ�������Է�ֹ��ӳ���������ķ�ͬ�����ʡ�
Map m = Collections.synchronizedMap(new LinkedHashMap(...));

12.4.5 ����ϣӳ�� WeakHashMap P428

12.4.6 ��ϣ��Hashtable P429
����ʵ��һ����ϣ���ù�ϣ����ӳ�䵽��Ӧ��ֵ���κη�null���󶼿�����������ֵ��Ϊ�˳ɹ����ڹ�ϣ���д洢�ͻ�ȡ�����������Ķ������ʵ��hashCode������equals������
Hashtable��ʵ������������Ӱ�������ܣ���ʼ�����ͼ������ӡ������ǹ�ϣ����Ͱ����������ʼ�������ǹ�ϣ����ʱ��������ע�⣬��ϣ���״̬Ϊopen���ڷ�������ϣ��ͻ��������£�����Ͱ��洢�����Ŀ����Щ��Ŀ���밴˳�����������������ǶԹ�ϣ�����������Զ�����֮ǰ���Դﵽ������һ���߶ȡ���ʼ�����ͼ�����������������ֻ�ǶԸ�ʵ�ֵ���ʾ�����ں�ʱ���Ƿ����rehash()�����ľ���ϸ���������ڸ�ʵ�֡�
ͨ����Ĭ�ϼ������ӣ�0.75 ����ʱ��Ϳռ�ɱ���Ѱ��һ�����С��������ӹ�����Ȼ�����˿ռ俪������ͬʱҲ�����˲���ĳ����Ŀ��ʱ�䣨�ڴ����Hashtable�����У�����get��put����������ӳ����һ�㣩��
��ʼ������Ҫ���ƿռ�������ִ��rehash��������Ҫ��ʱ�����֮���ƽ�⡣�����ʼ��������Hashtable�������������Ŀ�����Լ������ӣ�����Զ���ᷢ��rehash���������ǣ�����ʼ��������̫�߿��ܻ��˷ѿռ䡣
����ܶ���ĿҪ�洢��һ��Hashtable�У���ô�������Ҫִ���Զ�rehashing������������������������ȣ�ʹ���㹻��ĳ�ʼ����������ϣ����߿��Ը���Ч�ز�����Ŀ��
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
������£�
two=2
one=1
three=1
{two=2, one=1, three=1}
Tips��Hashtable��ͬ���ġ�

12.4.7 ����Properties P430
Properties���ʾ��һ���־õ����Լ���Properties�ɱ��������л�����м��ء������б���ÿ���������Ӧֵ����һ���ַ�����һ�������б�ɰ�����һ�������б���Ϊ���ġ�Ĭ��ֵ�������δ����ԭ�е������б������������Լ����������ڶ��������б�
��ΪProperties�̳���Hashtable�����Կɶ�Properties����Ӧ��put()��putAll()��������������ʹ����������������Ϊ������������߲��������ֵ����String����෴��Ӧ��ʹ��setProperty()����������ڡ�����ȫ����Properties���󣨼�������String�ļ���ֵ���ϵ���store()��save()��������õ��ý�ʧ�ܡ�����ڡ�����ȫ����Properties���󣨼�������String�ļ����ϵ���propertyNames()��list()��������õ��ý�ʧ�ܡ�
Tips��Properties��Ҫ�ṩ�˶������ļ��Ķ�ȡ��д�빦�ܣ��ڵ�14�ν��⡣
Tips���������̰߳�ȫ�ģ�����߳̿�����Properties�������������ⲿͬ����

12.5 �Ա���ѡ�� P430

12.5.3 Mapѡ��Hashtable��HashMap��WeakHashMap P433
��1��Hashtable��
������Ϊkey�Ķ���ͨ��������ɢ�к�����ȷ����֮��Ӧ��value��λ�ã�����κ���Ϊkey�Ķ��󶼱���ʵ��hashCode��equals������hashCode��equals�����̳��Ը���Object��������Զ�����൱��key�Ļ���Ҫ�൱С�ģ�����ɢ�к����Ķ��壬�������������ͬ����obj1.equals(obj2)==true�������ǵ�hashCode������ͬ���������������ͬ�������ǵ�hashCode��һ����ͬ�����������ͬ�����hashCode��ͬ�����������Ϊ��ͻ����ͻ�ᵼ�²�����ϣ���ʱ�俪���������Ծ��������hashCode()�����������ܼӿ��ϣ��Ĳ�����
�����ͬ�Ķ����в�ͬ��hashCode���Թ�ϣ��Ĳ�����������벻���Ľ�����ڴ���get��������null����Ҫ�����������⣬ֻ��Ҫ�μ�һ����Ҫͬʱ��дequals������hashCode����������Ҫֻд����һ����Hashtable��ͬ���ġ�
��2��HashMap��
HashMap�Ƿ�ͬ���ģ���������null��null value��null key����HashMap��ΪCollectionʱ��values()�����ɷ���Collection����������Ӳ���ʱ�俪����HashMap�������ɱ�������Ϊ��������������������൱��Ҫ�Ļ�����Ҫ��HashMap�ĳ�ʼ��������ù��߻���load factor���͡�

//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��13�� Java������ʽ P437
13.1 ������ʽ�﷨ P437
13.1.2 ������ʽ��ƥ���ַ�
Tips�����Ĳ���/expression/����ʽ�����е�expression��Ϊ������ʽ��

/.\b./��ƥ�䡰@@@abc��ʱ��ƥ�����ǣ��ɹ���ƥ�䵽�������ǣ�@a P441

��\b���롰^���͡�$�����ƣ�����ƥ���κ��ַ���������Ҫ������ƥ����������λ�õ��������ߣ�����һ���ǡ�\w����Χ����һ���Ƿǡ�\w���ķ�Χ��

��7��������� P442
������ʽ����һ������Ҫ�����ԡ����������ã����ǽ�ƥ��ɹ����ַ�����ĳ���ֽ��д洢�����Ժ����á���һ��������ʽģʽ�򲿷�ģʽ�������Բ���ţ��������ⲿ�ֱ��ʽ�洢��һ����ʱ�������С��������洢��Ҳ����ʹ�÷ǲ���Ԫ�ַ���?:������?=������?!���������ⲿ��������ʽ�ı��档
^(?:Chapter|Section)[1-9][0-9]{0,1}$ �е�?:��ﲢ����洢�����е��ӱ��ʽ������ֻ��Ϊ��ƥ��|����������

����������Ϊ�ǲ���Ԫ��
?:�����������е�ƥ�仺��
?=��Ϊ����Ԥ�飬���κο�ʼƥ��Բ�����ڵ�������ʽģʽ��λ����ƥ�������ַ���
?!��Ϊ����Ԥ�飬���κο�ʼ��ƥ���������ʽģʽ��λ����ƥ�������ַ���

�������ÿ����ƥ�䶼������������ʽģʽ�д������������������ݴ洢���洢��ƥ��Ļ�������Ŵ�1��ʼ���������ֱ�����99���ӱ��ʽ��ÿ��������������ʹ�á�\n�����ʣ�����nΪһ����ʶ�ض���������һλ����λʮ��������

������������������Ӧ�ã�
��1���������һ����򵥵�Ӧ�ã�ȷ����������������������ͬ���ʵ�λ�á�
��It is the cost of of gasoline going up up?
���ʽ
/\b([a-z]+) \1\b/
���ʾ���У��ӱ�﹤��Բ����֮���ÿһ�������ı��ʽ����һ��������ĸ�ַ������ɡ�[a-z]+����ָ���ġ��ڶ������Ƕ�ǰ�����������ƥ������ã�Ҳ�����ɸ��ӱ��ʽ��ƥ��ĵڶ��γ��ֵĵ��ʡ���\1 ������ָ����һ����ƥ�䡣���ʱ߽�Ԫ�ַ�ȷ��ֻ��ⵥ���ĵ��ʡ���������������硰is issued����this is�������Ķ��ﶼ�ᱻ�ñ��ʽ����ȷ��ʶ��
��������ʽ����ƥ��of of��up up
ʹ��������ʾ��������ʽ������Ĵ������ʹ����ƥ����Ϣ����һ�������ַ����н������������ε���ͬ�����滻Ϊһ����ͬ�ĵ��ʣ�
var ss = "It is the cost of of gasoline going up up?.\n";
var re = /\b([a-z]+) \1\b/
var rv = ss.replace(re, "$1");
��replace������ʹ�á�$1�������� ������ĵ�һ����ƥ�䡣����ж����ƥ�䣬������á�$2������$3���ȼ������� ��

//���´���û���滻�ɣ���������Java��ƥ����ȫ�ַ�ƥ��
public class TestStringRegex {
    static public void main(String args[]) {
        String ss = "It is the cost of of gasoline going up up?.\n";
        String re = "/\b([a-z]+) \1\b/";
        System.out.println(ss.replace(re, "$1"));
    }
}
//���·���true��reg="It"��false
public class TestStringRegex {
    static public void main(String args[]) {
        String str = "It is the cost of of gasoline going up up?.";
        String reg = ".*";
        System.out.println(str.matches(reg));
    }
}

��2��������õ���һ����;�ǽ�һ��ͨ����Դָʾ����URI���ֽ�Ϊ������֡��ٶ�ϣ����������URI�ֽ�ΪЭ�飨ftp��http��etc����������ַ��ҳ��/·����
http://msdn.microsoft.com:80/scripting/default.htm
�����������ʽ�����ṩ������ܣ�
/(\w+):\/\/([^/:]+)(:\d*)?[^# ]*/ //�о��ڶ���������/ƥ�������⣬����Ҫ��ת���\��
����������ʽӦ����������ʾ��URI����ƥ������������ݣ�
$1 ���� "http"
$2 ���� "msdn.microsoft.com"
$3 ���� ":80"
$4 ���� "/scripting/default.htm"

13.1.3 ������ʽ��ƥ����� P444
[0-9\.\-] //ƥ�����е����֣���źͼ��š���������������京�壬�������Ҫƥ����ţ���Ҫ��ת���
[ \f\r\t\n] //ƥ�����еİ��ַ�

/^[[:alpha:]]{3}$/ //���е�3����ĸ�ĵ��� �����alpha��̫����

�����ַ���?����{0,1}����ȵģ����Ƕ������š�0����1��ǰ������ݡ���ǰ��������ǿ�ѡ�ġ���
�����ַ���*����{0,}����ȵģ����Ƕ������š�0������ǰ������ݡ���
�ַ���+����{1,}����ȵģ���ʾ��1������ǰ������ݡ���

������ƥ�������������ź��ټ�һ����?���ţ������ʹƥ����������ı��ʽ�������ٵ�ƥ�䣬ʹ��ƥ��ɲ�ƥ��ı��ʽ�������ܡ���ƥ�䡱������ƥ��ԭ���������̰����ģʽ��Ҳ��������ǿ��ģʽ�������ƥ��ͻᵼ���������ʽʧ�ܵ�ʱ����̰��ģʽ���ƣ���̰��ģʽ����С�޶ȵ���ƥ��һЩ����ʹ�������ʽƥ��ɹ���

��5��ƥ��ʱ�������ù��� P447
���ʽ��ƥ��ʱ�����ʽ����ὫС���š�()�������ı��ʽ��ƥ�䵽���ַ�����¼�������ڻ�ȡƥ������ʱ��С���Ű����ı��ʽ��ƥ�䵽���ַ������Ե�����ȡ����ʵ��Ӧ�ó����У�����ĳ�ֱ߽������ң�����Ҫ��ȡ�������ֲ������߽�ʱ������ʹ��С������ָ����Ҫ�ķ�Χ��
С���Ű����ı��ʽ��ƥ�䵽���ַ�����������ƥ�������ſ���ʹ�ã���ƥ�������Ҳ����ʹ�á����ʽ��ߵĲ��֣���������ǰ�������ڵ���ƥ���Ѿ�ƥ�䵽���ַ��������÷����ǡ�\������һ�����֡���\1 �����õ�1��������ƥ�䵽���ַ�������\2 �����õ�2��������ƥ�䵽���ַ������Դ����ƣ����һ�������ڰ�����һ�����ţ�����������������š����仰˵����һ�Ե������š�(����ǰ������һ�Ծ�������š�

��2��
/(\w)\1{4,}/ Ҫ��\w��Χ���ַ������ظ�5��
/\w{5,}/ ��ʾ������5���ַ�
��3��<(\w+)\s*(\w+(=('|").*?\4)?\s*)*>.*?</\1>��ƥ�� 
<td id='td1' style="bgcolor:while"></td>
ʱ��ƥ�����ǳɹ������<td>��</td>����ԣ����ƥ��ʧ�ܣ�����ĳ�������ǩ��ԣ�Ҳ����ƥ��ɹ���

��6��Ԥ������������������ P448
ǰ�潲���˼���������������������ţ�^ $ \b�����Ǳ���ƥ���κ��ַ���ֻ�Ƕ��ַ�������ͷ�����ַ�֮��ķ�϶������һ������������������һ�ֶԡ���ͷ�����ߡ���϶�����������ĸ������ı�ʾ������
1) ����Ԥ������(?=xxxxx) (?!xxxxx)
��ʽ��"(?=xxxxx)"���ڱ�ƥ����ַ����У����������ġ���϶�����ߡ���ͷ�����ӵ������ǣ����ڷ�϶���Ҳ࣬�����ܹ�ƥ����xxxxx�ⲿ�ֵı��ʽ����Ϊ��ֻ���ڴ���Ϊ�����϶�ϸ��ӵ�����������������Ӱ���ߵı��ʽȥ����ƥ�������϶֮����ַ���������ơ�\b��������ƥ���κ��ַ�����\b��ֻ�ǽ����ڷ�϶֮ǰ��֮����ַ�ȡ��������һ���жϣ�����Ӱ���ߵı��ʽ��������ƥ�䡣
��1�����ʽ/windows(?=NT|98)/��ƥ��windows98, windowsNTʱ����ֻƥ��windows�����������򲻱�ƥ��
��2�����ʽ/(\w)((?=\1\1\1)(\1))+/
��ƥ���ַ���aaa ffffff 999999999 ʱ��������ƥ��6��"f"��ǰ4��������ƥ��9��"9"��ǰ7����������ʽ���Խ���ɣ��ظ�4�����ϵ���ĸ���֣���ƥ����ʣ�����2λ֮ǰ�Ĳ��֡�
���������Ϊ��һ���ַ�+(һ����϶+���ַ�)*����һ���ظ����÷�϶Ҫ���Ҳ���3�����ַ������շ�϶��ռ����ַ���

��ʽ��"(?!xxxxx)"�����ڷ�϶���Ҳ࣬����ƥ��xxxxx�ⲿ�ֱ��ʽ��
��3�����ʽ/((?!\bstop\b).)+/
��ƥ��akldfj kajrlekfdsj adf stop ifdlsa fdkslaʱ������ͷһֱƥ�䵽��stop��֮ǰ��λ�ã�����ַ�����û�С�stop������ƥ�������ַ�����
��4�����ʽ/do(?!\w)/��ƥ���ַ�����done, do, dog��ʱ��ֻ��ƥ�䡰do�������������У���do�����ʹ��(?!\w)��ʹ��\bЧ����һ���ġ�

2) ����Ԥ������(?<=xxxxx) (?<!xxxxx)
�����ָ�ʽ�ĸ��������Ԥ���������Ƶģ�����Ԥ����Ҫ��������ǣ����ڷ�϶�ġ���ࡱ�����ָ�ʽ�ֱ�Ҫ������ܹ�ƥ��Ͳ��ܹ�ƥ��ָ�����ʽ��������ȥ�ж��Ҳࡣ�롰����Ԥ������һ�����ǣ����Ƕ��Ƕ����ڷ�϶��һ�ָ���������������ƥ���κ��ַ���
��5�����ʽ(?<=\d{4})\d+(?=\d{4})��ƥ��1234567890123456ʱ����ƥ�����ǰ4�����ֺͺ�4������֮����м�8�����֡�

3) �������ظ�
��{min,max}�﷨��
������\b[1-9][0-9]{3}\bƥ��1000~9999 ֮������֡�\b[1-9][0-9]{2,4}ƥ��һ����100~99999 ֮������֡�

��ƥ�䵥����ǩ
This is a <EM>first</EM> test.
��<.+>��ƥ��<EM>first</EM>
��<.+?>����ֹ̰���ԣ���ƥ��<EM>��</EM>

6) ������չ��һ������������ã�<[^>]+>
֮����˵����һ�����õķ�������ʹ�ö����ظ�ʱ����������ҵ�һ���ɹ�ƥ��ǰ��ÿһ���ַ����л��ݡ���ʹ��ȡ���ַ�������Ҫ���лع���

13.1.4 ȫ�����Ż��ܱ� P449

13.1.5 ����������ʽ���� P451

13.2 Java������ʽ��� P452
13.2.1 ʹ��String���ƥ�书��
Java��ʹ��������ʽ�ķ����ǳ��࣬��򵥵ľ��Ǻ��ַ���һ��ʹ�á�
��1��ƥ�䡪��matches()����
matches���������жϵ�ǰ���ַ����Ƿ�ƥ�������������ʽ�����ƥ�䣬����true�����򷵻�false��
//
public class TestStringRegex {
    static public void main(String args[]) {
        String str = "lzb_box@163.com";
        String reg = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        System.out.println(str.matches(reg));//true
    }
}
��������true�������õ�ַ�ǺϷ��ġ�Java����Ҫʹ��\\��ʾһ��\

��2����֡���split()����
public String[] split(String regex);
public String[] split(String regex, int limit);
���У�limit��������ģʽӦ�õĴ��������Ӱ����������ĳ��ȡ�
��n����0��ģʽ���Ӧ��n-1 �Σ�����ĳ��Ƚ��������n�����һ��������г������ƥ��Ķ���������롣
���nΪ������ģʽ����Ӧ�þ����ܶ�Ĵ�������������������κγ��ȡ�
���nΪ0��ģʽ����Ӧ�þ����ܶ�Ĵ���������������κγ��ȣ����ҽ�β���ַ�����������
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
�����
GET
/index.html
HTTP/1.1

�ڶ���������ʽ����һ��limit����������Ϊ2��
public class TestStringRegex {
    static public void main(String args[]) {
        String s = "GET /index.html HTTP/1.1";
        String ss[] = s.split(" +", 2);
        for (String str : ss) {
            System.out.println(str);
        }
    }
}
�����
GET
/index.html HTTP/1.1

��3���滻����replaceAll()��replaceFirst()����
//
public class TestStringRegex {
    static public void main(String args[]) {
        String str = "lzb_box@163.com";
        String reg = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        System.out.println(str.replaceAll(reg, "Email"));//true
    }
}
�����
Email

13.2.2 ʹ��������ʽ��Pattern��Matcher P453
��1��Pattern���ʽ
PatternΪ�ַ�����������ʽ������ֱ�Ӹ���һ��������ʽ����һ��Patternʵ����
Pattern p = Pattern.compile("a*b");
������ģʽʱ����������һ��������־�����磺
Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);

����6����־��
--CASE_INSENSITIVE
--UNICODE_CASE
--MULTILINE
--UNIX_LINES
--DOTALL
--CANON_EQ

(2)Matcherƥ����
Ȼ��ʹ��Pattern������Matcherƥ�������󣬴���ƥ������ִ��3�ֲ�ͬ��ƥ�������
--matches()�������Խ����������������ģʽƥ�䡣
--lookingAt()���Խ��������д�ͷ��ʼ���ģʽƥ�䡣
--find()����ɨ�����������Բ������ģʽƥ�����һ�������С�
ÿ������������һ����ʾ�ɹ���ʧ�ܵĲ���ֵ��
//
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatternRegex {
    static public void main(String args[]) {
        Pattern p = Pattern.compile("a*b");
        Matcher m = p.matcher("aaaaab");//����aaaabc���޷�ƥ�䣬��Ϊmatches�����������������ģʽƥ��
        boolean b = m.matches();
        System.out.println(b);//true

        //m.reset("aaaaabc"); b2 = m.lookingAt();
        Matcher m2 = p.matcher("aaaaabc");//lookingAt��ͷƥ�䣬���daaaabc���޷�ƥ��
        boolean b2 = m2.lookingAt();
        System.out.println(b2);//true

        //m.reset("aaaaab"); b3 = m.find();
        Matcher m3 = p.matcher("aaaaab");
        boolean b3 = m3.find();
        System.out.println(b3);//true
    }
}

�ڽ�ʹ��һ��������ʽ�����Է����ͨ�����ඨ��matches()�������˷���������ʽ���ڵ��������н�������������ƥ�䣬�磺
boolean b = Pattern.matches("a*b", "aaaab");
����Ч����������matches��䣬���ܶ����ظ���ƥ�������Ч�ʲ��ߣ���Ϊ�������������ѱ����ģʽ��
���Ҫ���ʹ��һ��ģʽ������һ�κ����ô�ģʽ��ÿ�ζ����ô˷���Ч�ʸ��ߡ�

--matches:����ƥ�䣬ֻ�������ַ�������ȫƥ��ɹ����ŷ���True�����򷵻�False�������ǰ����ƥ��ɹ������ƶ��´�ƥ���λ�á�
--lookingAt:����ƥ�䣬���Ǵӵ�һ���ַ�����ƥ��,ƥ��ɹ��˲��ټ���ƥ�䣬ƥ��ʧ����,Ҳ������ƥ�䡣
--find:����ƥ�䣬�ӵ�ǰλ�ÿ�ʼƥ�䣬�ҵ�һ��ƥ����Ӵ������ƶ��´�ƥ���λ�á�
--reset:����ǰ��Matcher�������ϸ��µ�Ŀ�꣬Ŀ���Ǿ͸÷����Ĳ������������������reset���Matcher�赽��ǰ�ַ����Ŀ�ʼ����
//
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatternRegex {
    public static void main(String[] args){
        Pattern pattern = Pattern.compile("\\d{3,5}");
        String charSequence = "123-34345-234-00";
        Matcher matcher = pattern.matcher(charSequence);

        //��Ȼƥ��ʧ�ܣ�������charSequence�����"123"��pattern��ƥ���,�����´ε�ƥ���λ��4��ʼ
        print(matcher.matches());
        //����ƥ��λ��
        matcher.find();
        print(matcher.start());

        //ʹ��reset��������ƥ��λ��
        matcher.reset();//���غ�����������ƥ���ַ���

        
        //find()
        //��һ��findƥ���Լ�ƥ���Ŀ���ƥ�����ʼλ��
        print(matcher.find());
        print(matcher.group()+" - "+matcher.start());
        //�ڶ���findƥ���Լ�ƥ���Ŀ���ƥ�����ʼλ��
        print(matcher.find());
        print(matcher.group()+" - "+matcher.start());

        
        //lookingAt()
        //��һ��lookingAtƥ���Լ�ƥ���Ŀ���ƥ�����ʼλ��
        print(matcher.lookingAt());
        print(matcher.group()+" - "+matcher.start());

        //�ڶ���lookingAtƥ���Լ�ƥ���Ŀ���ƥ�����ʼλ��
        print(matcher.lookingAt());
        print(matcher.group()+" - "+matcher.start());
    }

    public static void print(Object o){
        System.out.println(o);
    }
}
�����
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

13.2.3 ������ʽ���4������ P454
ʹ��Matcher��ƥ�亯��������������4�ֳ��ù��ܣ�
--��ѯ����find()
�������ʱ���Դ�Сд��д�ɣ�
Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);

--��ȡ����group()
����group()������ȡ���ǻ�ȡ����ƥ���ַ�����group(1)�ǵ�һ��С�����Ӵ����ݣ�group(2)�ǵڶ���С�����Ӵ����ݣ��Դ����ƣ�
//
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatternRegex {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile(".(\\d{3,5})-(\\d{3,5})..");
        String charSequence = "abcd-123-34345-234-00";
        Matcher matcher = pattern.matcher(charSequence);

        if (matcher.find()) {
            System.out.println(matcher.group());//����ƥ���ַ�������
            System.out.println(matcher.group(0));//ͬgroup()

            for (int i = 1; i <= matcher.groupCount(); ++i) {
                System.out.println(matcher.group(i));
            }
        }
    }
}
������£�
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
        System.out.println(m.group(0));//ͬgroup()

        for (int i = 1; i <= m.groupCount(); i++) {
            System.out.println(m.group(i));
        }
    }
}
������£�
c:\dir\dir2\name.txt
c:\dir\dir2\name.txt
name.txt

���û��С���Ű������Ӵ�����groupCount()Ϊ0��group()��group(0)��������ƥ����ַ�����

--�ָ�����split():
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
�����
xd
abc
cde

���򵥵ķ�����
String str = "xd::abc::cde";
String[] r = str.split("::");

--�滻����replaceAll()��
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
�����
Abbced A ccdeA

д�ɿմ����ɴﵽɾ���Ĺ��ܣ��磺
String s = m.replaceAll("");

13.3.3 �κ��ϻ���ҵ P456
--����ĳһ��ҳURL��ַ�����ݣ�ȡ�����е�������ҳ���ӵ�ַ�������
������ҳ���ݣ�ʹ��Pattern����һ��������ʽ������ƥ���ԡ�href="����ͷ�ġ��ԡ�"����β���ַ�����������������Ӵ����еĵ�ַ��
<a href="http://XXXX/" target=_blank>
��ȡ��Matcher��ѭ��ִ��find()���ң�������е���ҳ��ַ��
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
            //���и���2������������������������룬ֱ�Ӵ�group�л�ȡ
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
�����
http://dui.dmcdn.cn/??dm_2014/common/public-min.css,dm_2015/index/css/style-min.css,dm_2015/city/css/city-min.css,dm_2015/city/css/calendar-min.css
http://www.damai.cn/redirect.aspx?type=login
http://www.damai.cn/redirect.aspx?type=reg
http://my.damai.cn/
http://my.damai.cn/account/myinfo.aspx
http://order.damai.cn/index.aspx
http://my.damai.cn/trade/ewallet/myEwallet.aspx
......

��ֻ������һ������ҳ���ӡ�ͨ����ʾ����������֩�룬����ͨ�����ְ취��������ҳ���ݷ����ģ�����ҳ�����ӵ�ҳ���ٽ���ץȡ����Ϊ���ץȡ�������Ĳ��Խ���ץȡ����ҲԽ���ͨ����˵��ץȡ��ȡ�
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��14�� Java������ʽ P459

14.1.6 �ܽ� P462
JDOM��DOM�����ܲ���ʱ���ֲ��ѡ�
SAX���ֽϺã����������ض��Ľ�����ʽ��
DOM4J������á���������ǿ���ֲ�ԣ�����Բ���DOM4J��

14.2 ʹ��XML��
����һ��books.xml����src���µ�xml���У����£�
<?xml version="1.0" encoding="gb2312"?>
<books>
    <book isbn="7506342605">
        <name>��ˮ䰴���</name>
        <price>80</price>
        <author>ʩ����</author>
        <year>Ԫĩ</year>
    </book>
    <book isbn="7020008720">
        <name>�����μǡ�</name>
        <price>90</price>
        <author>��ж�</author>
        <year>����</year>
    </book>
    <book isbn="7111103033">
        <name>���������塷</name>
        <price>75</price>
        <author>�޹���</author>
        <year>Ԫĩ</year>
    </book>
    <book isbn="7807074930">
        <name>����¥�Ρ�</name>
        <price>79</price>
        <author>��ѩ��</author>
        <year>���</year>
    </book>
</books>

14.2.1 ʹ��DOM��ȡXML�ļ� P463
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
            //DOM����
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            //DOM������
            DocumentBuilder builder = factory.newDocumentBuilder();

            //�����ļ�
            File file = new File("src/xml/books.xml");
            Document doc = builder.parse(file);

            //ȡ�ø��ڵ�
            Element root = doc.getDocumentElement();

            //ȡ���ӽڵ��б�
            NodeList books = root.getChildNodes();
            for (int i = 0; i < books.getLength(); i++) {
                Node book = books.item(i);

                if (book.getNodeType() == Node.ELEMENT_NODE) {
                    //ȡ������ֵ
                    String isbn = book.getAttributes().getNamedItem("isbn").getNodeValue();
                    System.out.print(isbn);

                    for (Node node = book.getFirstChild(); node != null; node = node.getNextSibling()) {
                        if (node.getNodeType() == node.ELEMENT_NODE) {
                            if (node.getNodeName().equals("name")) {
                                String name = node.getFirstChild().getNodeValue();//��ȡtextֵ����Ҫ�Ȼ�ȡFirstChild
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
�����
7506342605	��ˮ䰴���	80	ʩ����	Ԫĩ
7020008720	�����μǡ�	90	��ж�	����
7111103033	���������塷	75	�޹���	Ԫĩ
7807074930	����¥�Ρ�	79	��ѩ��	���
20

Tips��DOMֻ�ṩ�˽���XML�ļ��Ĺ��ܣ�����ʹ������д��һ��XML�ļ���

14.2.2 ʹ��SAX��ȡXML�ļ� P466
�ڶ���ѡ�����¼�API����SAX�������������������ģ�ͷ�ʽ��һ�ַ�ӳ�����ַ���������XML�﷨����ͨ�õ�����ģ�ͣ������������Ӧ�ó������Ա�������Ƶ�����ģ�͡�
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

    //����Ԫ�����ַ����ݵ�֪ͨ
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

    //�����ĵ���ʼ��֪ͨ
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
            //SAX����
            SAXParserFactory factory = SAXParserFactory.newInstance();

            //SAX������
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
�����
7506342605	��ˮ䰴���	
        	80	
        	ʩ����	
        	Ԫĩ
	
    
	
    
7020008720	�����μǡ�	
        	90	
        	��ж�	
        	����
	
    
	
    
7111103033	���������塷	
        	75	
        	�޹���	
        	Ԫĩ
	
    
	
    
7807074930	����¥�Ρ�	
        	79	
        	��ѩ��	
        	���
	
    
	

Time eclipsed: 17

����SAX�ǻ����¼�ģ�ͣ�DefaultHandler�ӿ��ṩ��һϵ�е��¼��ӿڣ�����ֻ��Ҫ�ڲ�ͬ���¼��ӿ���ʵ���Լ��Ĵ���Ϳ���ʵ�ֶ�XML�Ľ����ˡ���Щ�¼���Ҫ�ɷֳ����¼��ࣺ
--�ĵ���Ԫ�ػ������ռ俪ʼ֪ͨ�¼���
    void startDocument();
    void startElement(...);
    void startPrefixMapping(...);
--�ĵ���Ԫ�ػ������ռ����֪ͨ�¼���
    void endDocument();
    void endElement(...);
    void endPrefixMapping(...);
--����֪ͨ�¼���
    void characters(...);
--����֪ͨ�¼���
    void warning(SAXParseException e);
    void errors(SAXParseException e);
    void fatalError(SAXParseException e);
    
����ֻ��Ҫ�ڶ�Ӧ��֪ͨ�¼�ʵ�ֺ����в�׽��ǰԪ�ص�������Ϣ���ɡ�������ʾʵ���У�����ʵ���˿�ʼԪ�غ�����֪ͨ�¼���
--���Ԫ����bookԪ�أ������ISBN�š�
--�����book����Ԫ�أ����������Ԫ�ص��ı���

Tips��SAX���ṩ�˽���XML�ļ��Ĺ��ܣ�����ʹ������д��һ��XML�ļ���

14.2.3 ʹ��JDOM��дXML�ļ� P468
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
            //ȡ�ý�����
            SAXBuilder builder = new SAXBuilder();

            //�����ļ�
            File file = new File("src/xml/books.xml");
            Document doc = builder.build(file);

            //ȡ�ø��ڵ�
            Element root = doc.getRootElement();

            //ȡ���ӽڵ��б�
            List<Element> books = root.getChildren();
            for (int i = 0; i < books.size(); i++) {
                Element book = books.get(i);

                //ȡ������ֵ
                String isbn = book.getAttributeValue("isbn");
                System.out.print(isbn);

                String name = book.getChild("name").getText();//�ȼ���book.getChildText("name");
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
�����
7506342605	��ˮ䰴���	80	ʩ����	Ԫĩ
7020008720	�����μǡ�	90	��ж�	����
7111103033	���������塷	75	�޹���	Ԫĩ
7807074930	����¥�Ρ�	79	��ѩ��	���
Time eclipsed: 39

JDOM��һ����Դ��Ŀ�����������ͽṹ�����ô�Java������XML�ĵ�ʵ�ֽ��������ɡ����л������ֲ�������SAX��DOM�Ĺ�����Ч�ؽ��������
JDOM�����¼�������ɣ�
--org.jdom
Tips����JDOM���ĵ���Document������org.jdom.Document����ʾ����Ҫ��org.w3c.dom�е�Document���𿪡�
--org.jdom.adapters����������DOM�����Java�ࡣ
--org.jdom.filter��XML�ĵ��Ĺ������ࡣ
--org.jdom.input����ȡXML�ĵ����ࡣ
--org.jdom.output��д��XML�ĵ����ࡣ
--org.jdom.transform����JDOM XML�ĵ��ӿ�ת��Ϊ��XML�ĵ��ӿڡ�
--org.jdom.xpath����XML�ĵ�XPath�������ࡢJDOM���˵����

����JDOM�ṩ��DOM��SAX����ʵ�֣�ʹ�����·�ʽ����Document����
--DOMBuilder����
--SAXBuilder����
�°汾��DOMBuilder�Ѿ�ȥ��DOMBuilder.builder()����SAXЧ�ʻ�ȽϿ졣

���˿��Ը���һ��File���󴴽��������Խ�һ���ַ�����������������ǣ����Ƚ��ַ���ʹ��StringReaderת��ΪReader����Ȼ�����build()������������������
String textXml = "<books><book isbn=\"7506342605\"><name>��ˮ䰴���</name><price>80</price><author>ʩ����</author><year>Ԫĩ</year></book><book isbn=\"7020008720\"><name>�����μǡ�</name><price>90</price><author>��ж�</author><year>����</year></book><book isbn=\"7111103033\"><name>���������塷</name><price>75</price><author>�޹���</author><year>Ԫĩ</year></book><book isbn=\"7807074930\"><name>����¥�Ρ�</name><price>79</price><author>��ѩ��</author><year>���</year></book></books>";
Reader in = new StringReader(textXml);
Document doc = builder.build(in);

JDOM���ṩ�˼�������Ĺ��ܣ�д��XML�ļ���DTD��֤��Schema��֤��XPath��ȡ�ȡ�
��1��д��XML
ʹ��XMLOutputter������ļ���������������ֱ��ʹ��XMLOutputter���������ĳһ���ļ��������
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
            //ȡ�ý�����
            SAXBuilder builder = new SAXBuilder();

            //�����ļ�
            File file = new File("src/xml/books.xml");
            Document doc = builder.build(file);

            FileOutputStream fos = new FileOutputStream("src/xml/output.xml");
            XMLOutputter outp = new XMLOutputter();
            outp.output(doc, fos);//�������ɵ��ļ���UTF-8 ��ʽ
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Time eclipsed: " + (end - start));
    }
}

����������ļ��������Ҳ���������ĳһ��Socket�ͻ��ˣ�
outp.output(doc, socket.getOutputStream());

Ҳ�����������Ϊ��������System.out�����У�
//FileOutputStream fos = new FileOutputStream("src/xml/output.xml");
XMLOutputter outp = new XMLOutputter();
outp.output(doc, System.out);
//fos.close();

�����ܹ������OutputStream���󣬻��ܹ������Writer�������£�
FileWriter fos = new FileWriter("src/xml/output.xml");
XMLOutputter outp = new XMLOutputter();
outp.output(doc, fos);
fos.close();

��2��DTD��֤ P472

��3��Schema��֤ P472

��4��XPaht֧�� P472
ʹ��ʱ����XPath��ľ�̬����newInstance(String xpath)�õ�XPath����Ȼ�����selectNodes(Object context)������selectSingleNode(Object context)������ǰ�߸���XPath��䷵��һ��ڵ㣨List���󣩣����߸���һ��XPath��䷵�ط��������ĵ�һ���ڵ㣨Object���ͣ���
�������У�
//ȡ�ø��ڵ�
Element root = doc.getRootElement();

//ȡ���ӽڵ��б�
List<Element> books = root.getChildren();

��XPath�滻��
//ȡ�ø��ڵ�
XPath root = XPath.newInstance("//book");//������б�������XPath�ǲ��Ƽ�ʹ�õ�

//ȡ���ӽڵ��б�
List<?> books = root.selectNodes(doc);
for (int i = 0; i < books.size(); i++) {
    Element book = (Element) books.get(i);
}

XPath���������ѯ���ֺϷ���XPath��

14.2.4 ʹ��DOM4J��дXML�ļ� P472
package test;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

public class TestDOM4J {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            //ȡ�ý�����
            SAXReader reader = new SAXReader();

            //�����ļ�
            File file = new File("src/xml/books.xml");
            Document doc = reader.read(file);

            //ȡ�ø��ڵ�
            Element root = doc.getRootElement();

            //ȡ���ӽڵ��б�
            for (int i = 0; i < root.nodeCount(); i++) {
                //ȡ��ĳһ���ӽڵ�
                Element book = (Element) root.node(i);//���ﱨ������ת������

                //ȡ������ֵ
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

//CSDN���͵�DOM4Jʹ�òο�����
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
            //ȡ�ý�����
            SAXReader reader = new SAXReader();

            //�����ļ�
            File file = new File("src/xml/books.xml");
            Document doc = reader.read(file);

            //ȡ�ø��ڵ�
            Element root = doc.getRootElement();

            listNodes(root);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Time eclipsed: " + (end - start));
    }

    public static void listNodes(Element node) {
        System.out.println("��ǰ�ڵ�����ƣ�" + node.getName());

        List<Attribute> list = node.attributes();

        for (Attribute attribute : list) {
            System.out.println("���� " + attribute.getName() + ":" + attribute.getValue());
        }

        if (!(node.getTextTrim().equals(""))) {
            System.out.println("�ڵ����� " + node.getName() + ":" + node.getText());
        }

        Iterator<Element> iterator = node.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            listNodes(e);
        }
    }
}
�����
��ǰ�ڵ�����ƣ�books
��ǰ�ڵ�����ƣ�book
���� isbn:7506342605
��ǰ�ڵ�����ƣ�name
�ڵ����� name:��ˮ䰴���
��ǰ�ڵ�����ƣ�price
�ڵ����� price:80
��ǰ�ڵ�����ƣ�author
�ڵ����� author:ʩ����
��ǰ�ڵ�����ƣ�year
�ڵ����� year:Ԫĩ
��ǰ�ڵ�����ƣ�book
���� isbn:7020008720
��ǰ�ڵ�����ƣ�name
�ڵ����� name:�����μǡ�
��ǰ�ڵ�����ƣ�price
�ڵ����� price:90
��ǰ�ڵ�����ƣ�author
�ڵ����� author:��ж�
��ǰ�ڵ�����ƣ�year
�ڵ����� year:����
��ǰ�ڵ�����ƣ�book
���� isbn:7111103033
��ǰ�ڵ�����ƣ�name
�ڵ����� name:���������塷
��ǰ�ڵ�����ƣ�price
�ڵ����� price:75
��ǰ�ڵ�����ƣ�author
�ڵ����� author:�޹���
��ǰ�ڵ�����ƣ�year
�ڵ����� year:Ԫĩ
��ǰ�ڵ�����ƣ�book
���� isbn:7807074930
��ǰ�ڵ�����ƣ�name
�ڵ����� name:����¥�Ρ�
��ǰ�ڵ�����ƣ�price
�ڵ����� price:79
��ǰ�ڵ�����ƣ�author
�ڵ����� author:��ѩ��
��ǰ�ڵ�����ƣ�year
�ڵ����� year:���
Time eclipsed: 85

DOM4J�Ƿǳ������Java XML API��Խ��Խ���Java�������ʹ��DOM4J����дXML��
DOM4J�ṩ�˼�������Ĺ��ܣ�XPath֧�֡��ַ�����XML��ת����XML�����

��1����ȡ������XML�ĵ� P474
��ȡXML�ĵ���Ҫ������org.dom4j.io���������ṩ��DOMReader��SAXReader���಻ͬ��ʽ�����÷�ʽ��һ���ġ�����������ӿڵĺô���
reader.read���������صģ����Դ�InputStream��File��URL�ȶ��ֲ�ͬ��Դ����ȡ���õ���Document����ʹ���������XML��
��2��XPath֧�� P474
DOM4J��XPath�����õ�֧�֣������һ���ڵ㣬��ֱ����XPathѡ��
List list = document.selectNodes("//foo/bar");
Node node = document.selectSingleNode("//foo/bar/author");
String name = node.valueOf("@name");

�����XHTML�ĵ������еĳ����ӣ�����������ʵ�֣�
List list = document.selectNodes("//a/@href");
for (Iterator iter = list.iterator(); iter.hasNext(); ) {
    Attribute attr = (Attribute) iter.next();
    String url = attribute.getValue();
}

��3���ַ�����XML��ת��
//XMLת�ַ���
String text = document.asXML();
//�ַ���תXML
String text = "<person><name>James</name></person>";
Document document = DocumentHelper.parseText(text);

��4���ļ����
һ���򵥵���������ǽ�һ��Document���κε�Nodeͨ��write���������
//ȡ�ý�����
SAXReader reader = new SAXReader();

//�����ļ�
File file = new File("src/xml/books.xml");
Document doc = reader.read(file);

FileWriter out = new FileWriter("src/xml/outputByDom4J.xml");
doc.write(out);//���Ϊgbk��ʽ��
out.close();

�����ı�����ĸ�ʽ�������������������ʽ��������XMLWriter�ࣺ
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
            //ȡ�ý�����
            SAXReader reader = new SAXReader();

            //�����ļ�
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
    
    //����������Ϊ2�ո���ļ�
    public static void write(Document document) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileWriter("src/xml/outputByDom4J.xml"), format);//XMLWriter writer = new XMLWriter(System.out, format);�������Console
        writer.write(document);
        writer.close();
    }
    
    //����ֻ��һ�����ݵ�XML
    public static void write(Document document) throws IOException {
        OutputFormat format = OutputFormat.createCompactFormat();
        XMLWriter writer = new XMLWriter(new FileWriter("src/xml/outputByDom4J.xml"), format);
        writer.write(document);
        writer.close();
    }
    //<?xml version="1.0" encoding="UTF-8"?>
    //<books><book isbn="7506342605"><name>��ˮ䰴���</name><price>80</price><author>ʩ����</author><year>Ԫĩ</year></book><book isbn="7020008720"><name>�����μǡ�</name><price>90</price><author>��ж�</author><year>����</year></book><book isbn="7111103033"><name>���������塷</name><price>75</price><author>�޹���</author><year>Ԫĩ</year></book><book isbn="7807074930"><name>����¥�Ρ�</name><price>79</price><author>��ѩ��</author><year>���</year></book></books>
}
���ΪUTF-8 ��

14.2.5 ʹ��StAX��ȡXML�ļ� P475
Tips�������ϳ�����Կ��������ķ�ʽ��SAX˼·��ȫ��ͬ��ֻ��SAXʹ�����¼�������StAXʹ���˷�֧�¼���

14.3 �����ļ���д P478
����XML�ļ����⣬ͨ�������ļ���������Ϊϵͳ�������ļ�ʹ�á������ļ������Լ�ֵ�Ե���ʽ���֣�
username=admin
password=123

14.3.1 ��д�����ļ��ķ���
Java���ṩ��һ��java.util.Properties�����࣬�̳���Hashtable������12�£���ʹ��Properties����Է���ش�һ��.properties�����ļ��ж�ȡ���ò�����ʾ�����£�
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
            //��������keyֵ
            for (Iterator it = keySet.iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                System.out.println(key + ":" + properties.getProperty(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
�����
username:admin
password:123
username:admin

���properties�ļ������һ��JAR��WAR�ļ��У�����ʹ��ClassLoader��getResourceAsStream()�����õ�һ��InputStream����ʾ�����£�
ClassLoader cl = this.getClass().getClassLoader();
InputStream is = c.getResourceAsStream("com/company/application/application.properties");

�洢��Щ����ʱ����Ҫ�õ�һ��OutputStream����Ȼ��ʹ��Properties��stores()������

14.3.2 ��д�ļ�ʵ�� P479
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

            //д���ļ�
            FileOutputStream fos = new FileOutputStream("src/properties/config.properties");
            properties.store(fos, "my testproperties");//�ڶ���������comments
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
�����
password=123
username=admin

#my testproperties
#Tue Jul 12 23:08:09 CST 2016
password=123
username=admin

Tips����������ͨ����������ϵͳ����ʹ�á�ϵͳ�޸��˲���������properties�ļ��У�ϵͳ����ʱ�ټ�����Щ���ԡ�

//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��15�� Java GUI��Ա� P485
15.2.1 AWTʵ�� 
�Թ�û����

15.2.2 Swingʵ�� P490
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

Swingʵ��Hello Worldʵ����ֻ��Ҫ�����Ĵ��룬����ʵ�ֵĽ���Ҳ��AWT���ۡ�

15.2.3 SWTʵ�� P492
�Թ�û����

15.2.4 JFaceʵ�� P494
�Թ�û����

//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��16�� AWTͼ�ν��濪�� P499
�Թ�û����
16.1 AWT������� P499
��1�������Component��
Javaͼ���û�������������ɲ����������Component���������һ��������ͼ�λ��ķ�ʽ��ʾ����Ļ�ϲ������û����н����Ķ�����һ����ť��һ����ǩ�ȡ�������ܶ�������ʾ���������뽫�������һ���������вſ�����ʾ������
��java.awt.Component����������ĸ��࣬Component���з�װ�����ͨ�õķ��������ԣ���ͼ��������󡢴�С����ʾλ�á�ǰ��ɫ�ͱ���ɫ���߽硢�ɼ��Եȡ������������Ҳ�ͼ̳���Component��ĳ�Ա�����ͳ�Ա������
��2��������Container��
����java.awt.Container��Component�����࣬�����������Ҳ��һ�������������������������ʣ���������Ҫ�������������������������һ�������������ɶ���������ʹ���ǳ�Ϊһ�����塣��������������ͨ��add()��������������������
ÿ����������һ�����ֹ�������LayoutManager������������Ҫ��ĳ��������ж�λ���ж����Сʱ���ͻ�������Ӧ�Ĳ��ֹ�������
�ڳ����а��������λ�úʹ�Сʱ��Ӧ��ע���������㣺
--�������еĲ��ֹ����������������Ĵ�С��λ�ã��û��޷�����������������������Щ���ԣ������ͼʹ��Java�����ṩ��setLocation()��setSize()��setBounds()�ȷ��������ᱻ���ֹ��������ǡ�
--����û�ȷʵ��Ҫ�������������С��λ�ã���Ӧȡ���������Ĳ��ֹ�����������ΪsetLayout(null)��

16.1.2 ������Ի��� P501
���������P499�����Կ���������3�����͵�����������Window�����Panel���������ScrollPane��Window��ʵ�����д�����Frame�ͶԻ�����Dialog��ScrollPane��ʵ������Applet����21�ν��⣩��
��1�����ƴ���Frame
һ������һ�����ڣ�ͨ������Window������Frame������ʵ������������ֱ����Window�ࡣÿ��Frame����ʵ�����󣬶���û�д�С�Ͳ����Լ��ġ���˱������setSize()�����ô�С����Ⱥ͸߶ȣ�������setVisible(true)�����ô��ڿɼ���
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

��2��ʹ�����Panel
Panel��һ������������Frame����ڣ��������ڰ�װһ������������ǿ��Խ�Panel���õ����Ĳ��֣�Ȼ�󽫸�Panel��Ϊһ���������������Frame�У�
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

��3���Ի���Dialog P503
Dialog��ͬ��Frame����һ����ʾ��ʾ��Ϣ�ĶԻ��򡣴����Ի���ʱ����ӵ��һ��Frame�����࣬��ʾ������ĳһ������ĶԻ��򣬲��������ô�С�����֡�

��4���ı��Ի���

16.1.3 ������� P504
��1���ı�Label
��2����ťButton P505
Button b = new Button("�˳�");
��ť�������󣬻����ActionEvent�¼�����Ҫʹ��ActionListener�ӿڽ��м����ʹ����¼���ActionEvent�Ķ������getActionCommand()�������Եõ���ť�ı�ʶ����Ĭ�ϰ�ť��Ϊlabel����setActionCommand()����Ϊ��ť���������ʶ����
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

��3����ѡ�� Checkbox P505 �ο�����
��getStateChange()��ȡ��ǰ״̬��ʹ��getItem()��ñ��޸ĸ�ѡ����ַ�������

��4����ѡ���� CheckboxGroup P506
    private void createCheckboxGroup() {
        CheckboxGroup group = new CheckboxGroup();
        Checkbox checkbox1 = new Checkbox("Beijing", group, true);
        Checkbox checkbox2 = new Checkbox("Shanghai", group, false);
        Checkbox checkbox3 = new Checkbox("Guangzhou", group, false);
        add(checkbox1);
        add(checkbox2);
        add(checkbox3);
    }
    
��5�������б� Choice P506

��6���ı��� TextField P506
��ʾһ��������ı��򣬻س�������ʱ������ActionEvent�¼���ͨ��ActionListener�е�actionPerformed()�������¼�������Ӧ����ʹ��getText()����ȡ���ı�������룬ʹ��setEditable(boolean)����ֻ�����ԡ�

��7���ı�����TextArea P507

��8���б�List P507

��9������ Canvas P507
һ��Ӧ�ó������̳�Canvas����ܻ�����õĹ��ܣ��紴��һ���Զ��������������ڻ��������һЩͼ�δ�����Canvas���е�paint()�������뱻��д��Canvas�������������ꡢ�����¼�����������������ַ�ʱ�������ȵ���requestFocus()������
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

16.1.4 �˵���� P508
��1���˵��� MenuBar
Ҫ��Ӳ˵������������Ҫ��Ӳ˵���MenuBar��MenuBarֻ�ܱ���ӵ�Frame�Ծ��У���Ϊ�����˵����ĸ��������£�
MenuBar mb = new MenuBar();
setMenuBar(mb);

��2���˵� Menu 
Menu m1 = new Menu("File");
Menu m1 = new Menu("Edit");
Menu m1 = new Menu("Help");
mb.add(m1);
mb.add(m2);
mb.setHelpMenu(m3);//���ð����˵�

��3���˵�ѡ�� MenuItem P509
MenuItem mi1 = new MenuItem("Open");
MenuItem mi1 = new MenuItem("Exit");
m1.add(mi1);
m1.addSeparator();
m1.add(mi2);

MenuItem����������ActionListener�¼��������Ӧ�ļ���������
//ʾ������
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

16.2 AWT���ֹ����� P509
��ʹ�ò��ֹ�����֮ǰ����Ҫע�����¼��㣺
--Frame��һ�������ݴ��ڣ�Frame��Ĭ�ϲ��ֹ�����ΪBorderLayout��
--Panel�޷�������ʾ��������ӵ�ĳ�������С�Panel��Ĭ�ϲ��ֹ�����ΪFlowLayout��
--����Panel��Ϊһ�������ӵ�ĳ�������к󣬸�Panel��Ȼ�������Լ��Ĳ��ֹ���������������Panelʹ��BorderLayout��ĳ��������ʾ���������ﵽ��Ƹ����û������Ŀ�ġ�
--��������޲��ֹ�����setLayout(null)�������ʹ��setLocation()��setSize()��setBounds()�ȷ����ֹ���������Ĵ�С��λ�ã��˷����ᵼ��ƽ̨��أ�������ʹ�á�

16.2.1 ��ʽ���� FlowLayout P510
FlowLayout��Panel��Applet��Ĭ�ϲ��ֹ�������������ķ��ù����Ǵ��ϵ��£������ҽ��з��á�
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
Ĭ�ϴ�������ť����ͬһ�У������޸ı�խ����ť�ܵ��ڶ���ȥ�ˡ��ò�������Ĵ�С���䣬���λ�ûᷢ���仯��

16.2.2 ���򲼾� BorderLayout P511
BorderLayout��Window��Frame��Dialog��Ĭ�ϲ��ֹ��������ֳ�5������North��South��East��West��Center��ÿ������ֻ�ܷ���һ�������
ʹ��BorderLayout��ʱ����������Ĵ�С�仯���仯����Ϊ����������λ�ò��䣬��С�����仯������������ˣ���North��South���򲻱䣬West��Center��East�����ߣ������������ˣ���West��East���򲻱䣬North��Center��South������
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
��һ�����е������������������ܵ�����û�����������Center����ȥ���䣬�������Center����û��������򱣳ֿհס�

16.2.3 ���񲼾� GridLayout P512
GridLayoutʹ�����и������������״���֣�ƽ��ռ�������Ŀռ䣬�����ò���ʱ��Ҫָ������������������Ȼ��������Ӹ������ʱ���ᰴ�����к��е�˳��������ӡ�
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

16.2.4 ��Ƭ���� CardLayout P513
CardLayout��Ƭ���ֹ������ܹ������û�����������������ĳ�Ա����ͬһ��ʾ�ռ䣬���������ֳ����㣬ÿ�����ʾ�ռ�ռ�����������Ĵ�С������ÿ��ֻ�������һ���������Ȼÿ�㶼��������Panel��ʵ�ָ��ӵ��û����档
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

        panelCard.add(panel1, "1");//�ַ���1~4��̫֪��ʲô���ã�����Ҳûʲô����
        panelCard.add(panel2, "2");
        panelCard.add(panel3, "3");
        panelCard.add(panel4, "4");
        add(panelCard, BorderLayout.CENTER);

        //������ť
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
����4����ťʵ�ֿ�Ƭ���л���
���ֿ�Ƭ����ͨ��������Ҫ��ҳ��ʾ�Ľ����С�
Tips��GridBagLayout������AWT���ֹ�����������ӵģ�Ҳ�ǹ�����ǿ��ġ����ṩ���ڶ�Ŀ�����ѡ��㼸��������ȫ�ؿ��������Ĳ��ַ�ʽ��

16.2.5 ������Ƕ�� P515
������Ƕ�׶���ʹ��Panel����ɵģ�ÿһ��Panel�������ò��֣���һ��Panel�п�����Ӷ�������Ȼ�󽫸�Panel��Ϊһ��������ӵ�����������С�
ǰ����16.1.2 ��Panel�����ʵ��������Ƕ�׵�ʵ��������Frame��һ������������Panel��һ�����������Panel������Frame�У�������Ƕ�ס�

16.3 AWT�¼����� P515
16.3.1 �¼���Ȩģ�� P515
�¼�����Ĺ����У���Ҫ�漰����3�����
--Event�����¼����û��Խ��������Java�����ϵ��������������ʽ���֣�����̲�����Ӧ���¼�����KeyEvent��
--Event Source�����¼�Դ���¼������ĳ�����ͨ�����Ǹ���������簴ťButton��
--Event Handler�����¼������ߣ������¼����󲢶�����д���Ķ���

��Ȩģ�Ͱ��¼��Ĵ���ί�и��ⲿ�Ĵ���ʵ�����д���ʵ�� �˽��¼�Դ�ͼ������ֿ�������ѩɽ���¼������ߣ���������ͨ����һ���࣬�������Ҫ�ܹ�����ĳ�����͵��¼����ͱ���ʵ������¼�������ԵĽӿڡ�
ʹ����Ȩ����ģ�ͽ����¼������һ�㷽���������£�
--����ĳ�����͵��¼�XXXEvent��Ҫ����ղ����������¼������붨����Ӧ���¼��������࣬������Ҫʵ������¼����Ӧ�Ľӿ�XXXListener��
--�¼�Դʵ�����Ժ󣬱��������Ȩ��ע������¼��ļ�������ʹ��addXXXListener(XXXListener)������ע�������

16.3.2 ��Ȩģ�ͣ��¼����� P516
AWT�ṩ�������¼����ͣ��󲿷���java.awt.events���У���

AWT�¼�����10�࣬���Թ�Ϊ�����ࣺ�ͼ��¼��͸߼��¼���
��1���ͼ��¼� P517
�ͼ��¼���ָ����������������¼�����һ������Ϸ����¼����磺���Ľ��롢�������Ϸŵȣ�������Ĵ��ڿ��صȣ�����������¼���
--ComponentEvent��������¼�������ߴ�ı仯���ƶ���
--ContainerEvent���������¼���������ӡ��ƶ���
--WindowEvent���������¼����رմ��ڡ����ڱպϡ�ͼ�껯��
--FocusEvent���������¼�������Ļ�úͶ�ʧ��
--KeyEvent���������¼��������¡��ͷš�
--MouseEvent��������¼�����굥�����ƶ���

��2���߼��¼�
�߼��¼��ǻ���������¼��������Բ����ض��Ķ�����������������ڴ��������¼����࣬����TextField�а�Enter���ᴥ��ActionEvent�¼�����������������AdjustmentEvent�¼�������ѡ����Ŀ�б��ĳһ���ͻᴥ��ItemEvent�¼���
--ActionEvent���������¼�����ť���£���TextField�а���Enter������
--AdjustmentEvent���������¼����ڹ��������ƶ������Ե�����ֵ��
--ItemEvent������Ŀ�¼���ѡ����Ŀ����ѡ����Ŀ�ı䡣
--TextEvent�����ı��¼����ı�����ı䡣

16.3.3 ��Ȩģ�ͣ��¼������� P517
ÿ���¼����ж�Ӧ���¼����������������ǽӿڣ����ݶ��������巽����ͨ������AWT��˵��Ҳ������Swing��SWT����ÿ���¼����Ͷ���һ����ص�XXXListener�ӿڣ�XXXAdapter��ʵ�ֿ���Ϊ�գ�������XXX��ȥ��Event��׺���¼������������¼����ݸ��������Ӧ�ó����Ϊ�Լ�����Ȥ���¼����¼�Դ��GUI����򲿼�������ע�ᡣ
��������¼�KeyEvent���Ӧ�Ľӿ��ǣ�
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

�����細���¼��ӿڣ�
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

��ͬ���͵��¼��ɲ�ͬ�ļ�����������ÿһ�ּ��������ṩ�˸��ִ������ӿڷ������ڸ÷����б�д�¼��Ĵ�����롣P518
��ͬ�����ӵ�в�ͬ���¼����ͣ������ı����м��������¼�����ť����굥���¼��ȣ���˲�ͬ�����Ҳ��Ӧ�˲�ͬ�ļ��������͡� P519

16.3.4 ʹ���¼������� P519
Ҫʹ�ü������������¼����������ע�ᡣAWT����������ṩע���ע���������ķ�����
--ע���������
public void add<ListenerType>(<ListenerType>listener);
--ע����������
public void remove<ListenerType>(<ListenerType>listener);

Ҫʵ�ּ�������ע����ע������3�ַ�ʽ��
��1��ʵ�ּ������ӿ�
���ַ����Ǵ���һ���࣬Ҫ����ʵ��XXXListener�ӿڣ��ڸ����и�����Ҫʵ����Ӧ���¼���������
������ʾ��ʵ�ּ������ӿڵķ�ʽ����TestListenerʵ����3���������ӿ�MouseMotionListener��MouseListener��WindowListener�����Լ�������ƶ�����굥���������¼���
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

    //Ϊ��ʹ�����������رա����������˳�����Ҫʵ��windowClosing����
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

���������ص㣺
--��������ʵ�ֶ���ӿڣ��ӿ�֮���ö��Ÿ���
--������ͬһ���������һ���¼�Դ�Ϸ����Ķ����¼�
f.addMouseMotionListener(this);
f.addMouseListener(this);
f.addWindowListener(this);

����f�Ϸ����Ķ���¼�������ͬһ�����������պʹ���
--�¼������ߺ��¼�Դ����ͬһ�����С��ڱ������¼�Դ��Frame f���¼�����������TestListener�������¼�ԴFrame f����ĳ�Ա������
--����ͨ���¼���������ϸ���ϣ��籾���о�ͨ���¼�����������귢��ʱ������ֵ��
Tips��Java�������ηǳ����������ֻ֧�ֵ��̳У�Ϊ��ʵ�ֶ��ؼ̳�������Java�ýӿ���ʵ�֣�һ�������ʵ�ֶ���ӿڣ����ֻ��Ʊȶ��ؼ̳о��и��򵥡�����ǿ�Ĺ��ܡ���AWT�о;����õ�������ʵ�ֶ���ӿڡ���ס����ʵ���˼����ӿڣ��ӿ����Ѷ���ķ�������һһʵ�֣������ĳ�¼������д������Բ�����ʵ���䷽�������ÿյķ����������档��ȴ��������еķ�����Ҫд�ϡ�

��2��ʹ���ڲ��� P522
�ڲ��ࣨInner Class���Ǳ���������һ�����е��࣬ʹ���ڲ������Ҫԭ�����£�
--һ���ڲ���Ķ���ɷ����ⲿ��ĳ�Ա�����ͱ���������˽�еĳ�Ա��
--ʵ���¼�������ʱ�������ڲ��ࡢ�������̷ǳ�����ʵ���书�ܡ�
--��д�¼����������ڲ���ܷ��㡣

����ڲ������ܹ�Ӧ�õĵط���������AWT���¼���������С�
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

    public void createFrame() {//����privateҲû�й�ϵ����Ϊmain����Ҳ����������У�����Ǳ������Ҫ���ʣ��������public
        f.add(new Label("Manupalute Mouse"), BorderLayout.NORTH);
        f.add(textField, BorderLayout.SOUTH);//f.add(textField, "South");

        //����Ϊ�ڲ������
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

��3��ʹ�������� P523
��һ���ڲ����������ֻ���ڴ����������ʱ����һ�Σ�����Ҫ������������Ҫ�̳���һ�����еĸ����ʵ��һ���ӿڣ����ܿ����������ࣨAnonymous Class�������������౾�������������Ҳ�Ͳ����ڹ��췽��������Ҫ��ʽ�ص���һ���޲εĸ���Ĺ��췽����������д����ķ�������ν���������Ǹ��������ֶ�û�У�ֻ����ʽ�ص���һ���޲εĸ���Ĺ��췽����
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

        //����Ϊ�ڲ������
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

16.3.5 ʹ���¼������� P524
Java����ΪһЩListener�ӿ��ṩ����������Adapter���ࡣ����ͨ���̳��¼�����Ӧ��Adapter�࣬��д��Ҫ�ķ������ɣ��޹ط�������ʵ�֡��¼�������Ϊ�����ṩ��һ�ּ򵥵�ʵ�ּ��������ֶΣ��������̳��򡣵��ǣ�����Java�ĵ�һ�̳л��ƣ�����Ҫ���ּ�������������и���ʱ�����޷������¼��������ˡ�
java.awt.event���ж�����¼���������������¼�����
--ComponentAdapter�����������
--ContainerAdapter������������
--FocusAdapter������������
--KeyAdapter������������
--MouseAdapter�����������
--MouseMotionAdapter���������������
--WindowAdapter������������

WindowAdapter��һ�������࣬������ʵ����WindowListener�����з��������ǿ�ʵ�֣���
16-19 �е��ڲ��࣬��ʵ���˽ӿ�MouseMotionListener��������ֻ��Ҫʵ��һ������������˿���ͨ���̳��������ķ�ʽ�������Ϳ���ʡ�Բ���Ҫ���¼��������롣
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

        //����Ϊ�ڲ������
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

16.4.3 Java�ı��༭�� P528
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
        //��Ӳ˵���
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("File");
        MenuItem menuItemNew = new MenuItem("New", new MenuShortcut(KeyEvent.VK_N));
        menuFile.add(menuItemNew);
        MenuItem menuItemOpen = new MenuItem("Open", new MenuShortcut(KeyEvent.VK_O));
        menuFile.add(menuItemOpen);
        MenuItem menuItemSave = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S));
        menuFile.add(menuItemSave);
        menuFile.add("Save as...");//�ڲ�ʵ�ֻ�����һ��LabelΪ���ַ�����MenuItem
        menuFile.addSeparator();
        menuFile.add("Exit");
        menuFile.addActionListener(this);//����¼���ע����Menu�ϵ�
        menuBar.add(menuFile);

        Menu menuHelp = new Menu("Help");
        menuHelp.add("About");
        menuHelp.addActionListener(this);
        menuBar.setHelpMenu(menuHelp);

        //������
        frame = new Frame("Java Text Editor");
        frame.setMenuBar(menuBar);
        textArea = new TextArea();
        frame.add("Center", textArea);
        frame.addWindowListener(this);//ע�ᴰ�ڹرռ�����
        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        TextEditor textEditor = new TextEditor();
        textEditor.createEditor();
    }

    //�˵�ѡ���¼�
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

                //ȡ��ʱ�����õ�null
                if (fileName == null) {
                    return;
                } else {
                    saveFile(fileName, false);
                }
            } else if (e.getActionCommand() == "Save as...") {
                fileName = getFileName("Save as");

                //ȡ��ʱ�����õ�null
                if (fileName == null) {
                    return;
                } else {
                    saveFile(fileName, false);
                }
            } else if (e.getActionCommand() == "Exit") {
                System.exit(0);
            } else if (e.getActionCommand() == "About") {
                //��ʾ���ڶԻ���
                final Dialog dialog = new Dialog(frame, "About", true);
                dialog.setSize(267, 117);
                //dialog.setLayout(new GridLayout(2, 1));//�����һ��topPanel������Ҫ����GridLayout

                //���ڹر��¼�
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        dialog.dispose();
                    }
                });

                //��ʾ��Ϣ
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
        //д���������
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
            String selectedFileName = fileDialog.getDirectory() + fileDialog.getFile();//getDirectory�������ŷָ���
            return selectedFileName;
        }
    }
}

//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��17�� AWT��ý���� P535
17.1 ͼ������java.awt.image P535
17.1.1 ����ͼ��
AWT���Ժܼ򵥵����ָ�ʽ��ͼ��GIF��JPEG��Toolkit���ṩ������getImage()����������ͼ��
--Image getImage(URL url);
--Image getImage(String filename);
Toolkit��һ������࣬ȡ��Toolkit�ķ����ǣ�
Toolkit toolkit = Toolkit.getDefaultToolkit();
���ڼ̳���Frame������˵������ֱ��ʹ������ķ���ȡ�ã�
Toolkit toolkit = getToolkit();
��������������ͼƬ��ʵ����
Toolkit toolkit = Toolkit.getDefaultToolkit();
Image image1 = toolkit.getImage("imageFile.gif");
Image image2 = toolkit.getImage(new URL("http://.../people.gif"));
����������Applet������ʹ��Applet������getImage()������ֱ�Ӽ���ͼƬ��
Image getImage(URL url);
Image getImage(URL url, String name);

17.1.2 ��ʾͼ�� P536
ͨ�����ݵ�paint()������Graphics������Ժ����׵���ʾͼ��Graphics�������������4��drawImage()���������Ƕ�����һ��booleanֵ����Ȼ���ֵ���ٱ�ʹ�á����ͼ���Ѿ�����ȫ���ز�����˱���ȫ���ƣ�����ֵ��true�����򣬷���ֵ��false��
�������ָ��this��Ϊͼ��۲��ߵ�ԭ���ǣ�Component��ʵ����ImageObserver�ӿڡ���ͼ�����ݱ�����ʱ������ʵ�ֻ����repaint()������
������������������������Ͻ�(0, 0)��ԭʼ��С��ʾһ��ͼ��
g.drawImage(myImage, 0, 0, this);
����Ĵ���������(90, 0)����ʾһ��������Ϊ��300���ظ�62���ص�ͼ��
g.drawImage(myImage, 90, 0, 300, 62, this);

17.1.3 ʵ��һ����ʾͼƬ P536
��дһ�����壬��������ͼ�񲢽�����ʾ������̳���Frame������ֱ��ʹ��getToolkit()������ȡ��Toolkit����Ȼ��ʹ��getImage()��ȡ��һ�ű���ͼƬ�ļ��������paint()��ʹ��Graphics��drawImage()������ʾ��ͼ��
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
        //ȡ��ͼƬ����
        Image image = getToolkit().getImage(fileName);
        //��ͼ
        g.drawImage(image, 50, 50, this);
    }

    public static void main(String[] args) {
        new ShowImage("src/image/1.jpg");
    }
}

17.1.4 ʵ����������ͼƬ P537
ͨ��getImage()����ȡ�õ���java.awt.Image���͵Ķ���Ҳ����ʹ��javax.imageio.ImageIO���read()ȡ��һ��ͼ�񣬷��ص���BufferedImage����
BufferedImage ImageIO.read(Url);
BufferedImage��Image�����࣬�������˾��пɷ���ͼ�����ݻ�������Image������ͨ��������ʵ��ͼƬ�����š�
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
            Image src = javax.imageio.ImageIO.read(file);//����Image���󣬷��ص�BufferedImage������Image������
            int width = src.getWidth(null);
            int height = src.getHeight(null);

            BufferedImage tag = new BufferedImage(width / 2, height / 2, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(src, 0, 0, width / 2, height / 2, null);//������С��ͼ

            //д��ͼƬ
            FileOutputStream out = new FileOutputStream(file2);//������ļ���
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
���иó��򣬼�������һ������Ϊԭͼһ�����ͼƬ�������Ҷ�ͼ�����и��ߵ�Ҫ�󣬿��Թ�עһ�¿�Դ��Ŀ����JMagick������ʹ��JMagickʵ��ͼƬ�ĸ��ơ���Ϣ��ȡ��б�ǡ���Ч����ϡ��ı��С���ӱ߿���ת����Ƭ���ı��ʽ��ȥɫ�ȹ��ܡ�

17.2 ��άͼ����ơ���Java2D P538
Java2D�Ļ�ͼ���̣�
��1��ȡ��Graphics2D����
��2������Graphics2D���ԡ�
��3���������ƶ���
��4�����ƶ���
17.2.1 Java2D���

17.2.2 ȡ��Graphics2D���� P540
����ͼ��ʱ��������Graphics�������Graphics2D�����Ͻ��У����Ƕ���������Ҫ��ͼ������ѡ���ĸ�ȡ�����Ƿ�Ҫʹ�������ӵ�Java2D��ͼ�ι��ܡ���Ҫע�⣬���е�Java2Dͼ�β�����������Graphics2D�����ϵ��á�Graphics2D��Graphics�����ࡣ
��ͼ�ĵ�һ�������Ǵ���Graphics2D���󣬿��Խ�Graphicsת��Ϊ�ö���
public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
}
����
public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
}

17.2.3 ����Graphics2D���� P540

��1��������䷽ʽ P541
�����Ҫ��ͼ����ʾ����ʽ����ɫ�������趨PaintΪGradientPaint��ʵ����
GradientPaint gp = new GradientPaint(180, 190, Color.yellow, 220, 210, Color.red, true);
g2.setPaint(gp);
��2�����û�����ɫ
BasicStroke pen = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
g2.setStroke(pen);

17.2.4 �������ƶ��� P541
Java2D�н��л�ͼʱ�����ǲ��ö�Ӧ�ķ������࣬����ΪҪʵ��ĳ����״��������Ӧ����״���������ͨ��ʹ��java.awt.geom���е�����������Ҫ��������״��������Line2D.Float�ࡢ����Rectangle2D.Float����Rectangle2D.Double�ࡢ��ԲEllipse2D.Float��Բ��Arc2D.Float��ȡ�
��������ʾ��
Line2D.Float line = new Line2D.Float(20, 300, 200, 300);//ֱ��
CubicCurve2D.Float cubic = new CubicCurve2D.Float(40, 100, 120, 50, 170, 270, 220, 100);//����
Ellipse2D.Float shape = new Ellipse2D.Float(200, 200, 60, 60);//��Բ

17.2.5 ���ƶ��� P542
��Բ�ͬ�Ķ��󣬿���ʹ�ò�ͬ�ķ������л��ơ�
--����ʹ��Graphics2D���еķ���draw()���ڻ�����������fill()����������䡣���Ƕ���ǰ����������ͼ�ζ�����Ϊ������
--Java2D�е��ַ����Ļ�����Ȼ����drawString()����������������������
drawString(String s, float x, float y);
drawString(String str, int x, int y);
--����������draw(Shape s)�����е�Shape�ӿ���Graphics2D�б����塣

�µ�Java2D Shape�඼�С�2D����׺����Щ�µ���״ʹ�ø���ֵ���������������������伸����״��
--Polygon�ࣨint[] xpoints, int[] ypoints, int npoints��;
--RectangularShape�����࣬��������Arc2D��Ellipse2D��Rectangle2D��
--QuadCurve2D�����α������������ߣ���
--CubicCurve2D�����α������������ߣ���
--Area�����򣩡�
--GeneralPath����ֱ�ߡ������������ߡ������������������ɣ���
--Line2D��

17.2.6 ʵ��һ������ͼ�� P542
//����ֱ�ߡ����߼���Բͼ�ε�ʵ����
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
            //ȡ��Graphics2D����
            Graphics2D g2 = (Graphics2D) g;

            //���ý���ɫ
            GradientPaint gp = new GradientPaint(180, 190, Color.yellow, 220, 210, Color.red, true);
            g2.setPaint(gp);
            g2.setStroke(new BasicStroke(2.0f));//�趨��ϸ

            //����ֱ��
            Line2D.Float line = new Line2D.Float(20, 300, 200, 300);
            g2.draw(line);

            //��������
            CubicCurve2D.Float cubic = new CubicCurve2D.Float(70, 100, 120, 50, 170, 270, 220, 100);
            g2.draw(cubic);

            //������Բ
            Ellipse2D.Float shape = new Ellipse2D.Float(200, 200, 60, 60);
            g2.fill(shape);//g2.draw(shape);
        }
    }

    public static void main(String[] args) {
        new DrawShape();
    }
}

17.2.7 ʵ��������ʾ���� P543
ͨ��Java2D������ʾ��ϸ�µ����֣����������ʾ�����Ϊ����ɫ���ַ�����
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
            //ȡ��Graphics2D����
            Graphics2D g2 = (Graphics2D) g;

            //��ʾ����
            FontRenderContext fontRenderContext = g2.getFontRenderContext();
            TextLayout textLayout = new TextLayout("Test Characters", new Font("Modern", Font.BOLD + Font.ITALIC, 20), fontRenderContext);

            //����
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

17.2.8 ʵ��������ʾͼ�� P544
ͨ��ϣ��ͼ�����˾���Ч��������ʹ�ÿ��Դ���ͼ�εĻ�ͼ�������2D API�����ṩ��һЩ�򵥵ķ���������ֱ���ó�ʽ������ͼ�ν����˾�Ч���Ŀ��ơ�
//û����Ч����
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
                //ȡ��Graphics2D����
                Graphics2D g2 = (Graphics2D) g;

                float[] elements = {0.0f, -1.0f, 0.0f, -1.0f, 0.0f, -1.0f, 0.0f};

                //����ͼƬ
                Image img = Toolkit.getDefaultToolkit().getImage(filename);
                int w = img.getWidth(this);
                int h = img.getHeight(this);
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

                //����Graphics2D����
                Graphics2D big = bi.createGraphics();
                big.drawImage(img, 0, 0, this);

                //��Ӱ����
                BufferedImageOp biop = null;
                AffineTransform at = new AffineTransform();
                BufferedImage bimg = new BufferedImage(img.getWidth(this), img.getHeight(this), BufferedImage.TYPE_INT_RGB);
                Kernel kernel = new Kernel(3, 3, elements);
                ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
                cop.filter(bi, bimg);
                biop = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

                //��ʾͼ��
                g2.drawImage(bimg, biop, 0, 0);
            }
        });
    }

    public static void main(String[] args) {
        new DrawImage().draw();
    }
}

17.3 ��Ƶ¼���벥�š���JavaSound P545
��ʱû�п���

17.4 ��Ƶ�����벥�š���JMF��ý��� P563
��ʱû�п���
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��18�� Swingͼ�ν��濪�� P577
18.1 Swing������� P577
Swing��������¼����ص㣺
--���е�Swing������̳���AWT�����Component������AWT�������ࡣ
--���е�Swing����������ĸ��J����״������AWT�е���������֡�
--Swing�����AWT�Ĺ��ܽ�������չ���������������������������˵����������

18.1.2 ������Ի��� P579
��1������JFame
package test;

import javax.swing.JFrame;

public class TestJFrame extends JFrame {

    public TestJFrame() {
        setTitle("Test JFrame");
        setLocation(20, 20);
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//�����Ļ��С
        // setLocation(screenSize.width / 2 - 300 / 2, screenSize.height / 2 - 200 / 2);//��λ��λ���Ǵ�������Ͻǣ�����Ļ�����ȥ�����һ�볤����λ����Ļ������
        setSize(300, 200);
        setVisible(true);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//���ø����ԣ��رմ���ʱ�˳�����
        // this.addWindowListener(new WindowAdapter() { //��Ӵ��ڹرմ����¼���ʵ��һ��WindowAdapter��������
            // @Override
            // public void windowClosing(WindowEvent e) {
                // System.out.println("Procedure exit.");
                // System.exit(0);
            // }
        // });
        
        //���ÿ�ƽ̨�ĸйۣ�û������ʲôЧ��
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

��2���ڲ�����JInternalFrame P580
JInternalFrame����������������ܵ������֣����������ڶ�������ϡ��ܹ�����Java�ṩ��Look and Feel����������ȫ��ͬ��ԭ�в���ϵͳ���ṩ�Ĵ������Σ���JFrame�����е��ԡ�
JInternalFrame���캯����
--title�����ڱ��⡣
--resizable���ɸı䴰�ڴ�С��
--closable���ɹرմ��ڡ�
--maximizable������󻯲�����С�����ڡ�
--iconifiable���������С���Ĵ��ڡ�
���ĳһ�����������ڣ���ʾ�����и���ܡ�
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

��3��ͼ������ JLayeredPane P582
JLayeredPaneͼ�������������Ҫʱ�����ص�����������ȷ�Χ�ֳɼ�����ͬ�Ĳ㡣

��4��������������JDesktopPane P583
JDesktopPane���ڴ������ĵ����������������������û��ɴ���JInternalFrame���󲢽�����ӵ�JDesktopPane��JDesktopPane��չ��JLayeredPane���Թ�����ܵ��ص��ڲ����塣
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
        contentPane.add(desktopPane, BorderLayout.CENTER);//���ﲻ��getContentPaneû������ʲô��ͬ��ΪʲôҪ��ȡ��contentPane��

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

��5���Ի���JDialog P584
�Ի������ܣ�JFrame����һЩ���ơ��Ի������ʱ�������趨��ֹ�������ڵ����룬ֱ������Ի��򱻹رա�

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
            JDialog jDialog1 = new JDialog(this, "Dialog", true);//����Ϊfalse������Ի����л�
            jDialog1.setSize(200, 200);
            jDialog1.setLocation(450, 450);
            jDialog1.setVisible(true);
        }
    }

    public static void main(String[] args) {
        new TestJDialog();
    }
}

��6���ļ�ѡ��Ի��� JFileChooser P585
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

            //ͼƬ������
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");//���Ǹ��ɱ�������캯�������Դ��ݶ����׺����
            jFileChooser.setFileFilter(filter);
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//ѡ���ļ�
            jFileChooser.setDialogTitle("Open JPEG file");//���ô��ڱ���
            int result = jFileChooser.showOpenDialog(this);//�򿪡����ļ����Ի���
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

��7����ɫѡ��Ի��� P586
JColorChooser�ṩһ�����������û�������ѡ����ɫ�Ŀ���������
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

18.1.3 ������� P587
Tips�������������������ʹ��getText()ȡ����������ݡ�
��1���ı�JLabel
��ʾһ���ı����������£�
Label label = new Label("this is a label");
����������add()�������ɽ�label��ӵ������С�
��2����ťJButton
��3����ѡ��JCheckBox
��4����ѡ��JRadioButton
��5���ı���JTextField P588
��6�������JPasswordField
��7���ı���JTextArea
��8���ı��ļ���JTextPane
JTextPane���Ա༭�ı���ͼ�꣬����ʹ�����·������༭����һ���ı��ĵ�������
void setDocument(Document doc);
Ҳ���Խ�һ��ͼ������ĵ��У����滻��ǰѡ������ݣ�
void insertIcon(Icon g);
��9��HTML�༭��JEditorPane
���Աി�ı���HTML��RTF���ݣ����Ե�������ķ�����������ʾһ��HTMLҳ�棺
pane.setPage(e.getURL());
Ҳ��������������ȡ�����е�HTML���ݣ�
HTMLDocument doc = (HTMLDocument) pane.getDocument();
��10���б�JList
String[] data = {"one", "two", "three"};
JList list = JList(data);
��11��������JScrollBar P588
������������ӵ��κε������У����£�
Container mainPane = this.getContentPane();
JScrollBar vscroll = new JScrollBar(JScrollBar.VERTICAL);
mainPane.add(vscroll, BordreLayout.EAST);

��12��������JProgressBar P589
����������ʾ����
progressBar = new JProgressBar(0, task.getLengthOfTask());
progressBar.setValue(0);
progressBar.setStringPainted(true);

���½�����ֵ��
progressBar.setValue(task.getCurrent());

����ʾ��������������Ϊ��ȷ��ģʽ��Ȼ����֪�����񳤶Ⱥ��л���ȷ��ģʽ��
progressBar = new JProgressBar();
progressBar.setIndeterminate(true);//����Ϊ��ȷ��ģʽ
progressBar.setMaximum(newLength);//�������ֵ
progressBar.setValue(newValue);//���õ�ǰֵ
progressBar.setIndeterminate(false);//����Ϊȷ��ģʽ

��13��������� JSlider P589
���������ʾ���̶ȱ�Ǽ����̶�֮��Ĵο̶ȱ�ǡ��̶ȱ��֮���ֵ�ĸ�����setMajorTickSpacing()��setMinorTickSpacing()�����ơ��̶ȱ�Ǻͻ�����setPaintTicks()���ơ���������ڹ̶�ʱ��������������λ�ã��ػ���̶ȴ�ӡ�ı���ǩ����ǩ�Ļ�����setLabelTable()��setPaintLabels()���ơ�
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
        //���̶�
        jSlider.setMajorTickSpacing(10);
        //�ο̶�
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

��14�������� JTable P590
��ʱû����

��15��������� JTree P593
JTree��Swing����Ƚϸ��ӵ����֮һ��������ʵ������һ����������һ���������ڵ���ɡ�JTree�е�ÿһ���ڵ㶼����ʵ��TreeNode���Interface�����ǿ����Լ���ʵ�֣���ȻSwingҲΪ�����ṩ��һ��DefaultMutableTreeNode��ʵ�֡�
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

18.1.4 �˵������ P594
��1���˵��� JMenuBar
��Ӳ˵������������Ҫ��Ӳ˵���JMenuBar��MenuBarֻ�ܱ���ӵ�JFrame�����У���Ϊ�����˵����ĸ��������£�
JMenuBar mb = new JMenuBar();
setJMenuBar(mb);

��2���˵�JMenu
JMenu m1 = new JMenu("File");
JMenu m2 = new JMenu("Edit");
JMenu m3 = new JMenu("Help");
mb.add(m1);
mb.add(m2);
mb.add(m3);

��3���˵�ѡ�� JMenuItem
JMenuItem mi1 = new JMenuItem("Open");
JMenuItem mi2 = new JMenuItem("Save");
JMenuItem mi3 = new JMenuItem("Exit");
m1.add(mi1);
m1.add(mi2);
m1.addSeparator();
m1.add(mi3);

����֮�⣬��������Ӹ�ѡ��͵�ѡ��ťѡ�
JCheckBoxMenuItem mi4 = new JCheckBoxMenuItem("Modify");
JRadioButtonMenuItem mi5 = new JRadioButtonMenuItem("Bold");
m2.add(mi4);
m2.add(mi5);

JMenuItem����������ActionListener�¼��������ܹ������Ӧ�ļ������������£�
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

��4�������˵� JPopupMenu P595
JPopupMenu�ǵ����˵���ʵ�֡�
������봴��һ��JPopupMenu��
JPopupMenu jp = new JPopupMenu();
�������б����JPopupMenu��
JPopupMenu jp = new JPopupMenu("Popup menu");

��JMenuһ��������ʹ��add()������insert()������JPopupMenu����ӻ��߲���JMenuItem��JComponent��JPopupMenu����ӵ����е�ÿһ���˵������һ�����������������ݵ���ʽ�˵��Ĳ��ֹ����������˵�����ʾ��˳�򡣻�����ʹ��addSeparator()������ӷָ��ߣ�����JPopupMenuҲ��Ϊ�÷ָ���ָ��һ������������
ͨ�����õ���ʽ�˵���������Ӧ��show()��������ʾ����ʽ�˵���show()�������ڲ˵���ʾ֮ǰ����location��invoker���Լ����趨����ˣ�Ӧ�ü�����е�MouseEvent�¼��������Ƿ��ǵ���ʽ�˵���������Ȼ���ں��ʵ�ʱ����ʾ����ʽ�˵��������showJPopupMenu�������յ��ʵ��Ĵ������¼�ʱ�ͻ���ʾ����ʽ�˵������£�

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
                //if (e.isPopupTrigger()) {//�����֪����δ���
                    //�����ǰ�¼�������¼���أ��򵯳��˵�
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

18.1.5 ��������� JToolBar P596
JToolBar�ṩ��һ��������ʾ���õ�Action��ؼ���������û����Խ��������ϵ������Ĵ����У�����floatable���Ա�����Ϊfalse����Ϊ����ȷִ���϶������齫JToolBarʵ����ӵ��������ġ��ߡ��е�һ�ߣ����������Ĳ��ֹ�����ΪBorderLayout�������Ҳ��������ġ��ߡ�������κ��Ӽ���
����ʵ��ʵ����һ��������������3����ť��������Ϊ�����϶��ģ����£�
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

        bar.setFloatable(true);//���϶�
        bar.setToolTipText("Tool Bar");//��ʾ

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

18.2 Swing ���ֹ����� P597
��1��BoxLayout
����ֱ��ˮƽ���ö������Ĳ��ֹ���������ֱ���е���������µ�����ܵĴ�Сʱ��Ȼ����ֱ���С�
BoxLayout����������axis��������ģ��ò���ָ���˽����еĲ������͡�
BoxLayout(Container target, int axis);
axis������4��ѡ��
--X_AXIS��������ˮƽ�������
--Y_AXIS�����ϵ��´�ֱ�������
--LINE_AXIS������������CompnentOrientation���ԣ�����������һ���е����з�ʽ������������������ComponentOrientation��ʾˮƽ�������ˮƽ���ã��������Ǵ�ֱ���á�����ˮƽ�������������ComponentOrientation��ʾ�����ң�����������ҷ��ã��������Ǵ��ҵ�����á����ڴ�ֱ����������Ǵ��ϵ��·��õġ�
--PAGE_AXIS����LINE_AXIS�෴
�������з���������ս�������ӵ������е�˳�����С�

��2��GroupLayout P598
��3��OverlayLayout P598
��4��JScrollPaneLayout P598
��5��SpringLayout P598
��6��ViewportLayout P598

18.3 Swing�¼����� P598

18.4.3 Java�ļ��༭�� P600
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
    private JFrame frame;//������
    private JTextArea textArea;//�ļ���������
    private String fileName;//�򿪵��ļ���

    public void createEditor() {
        //�����ļ��˵�
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        //�½��˵�
        JMenuItem menuNew = new JMenuItem("New");
        menuNew.addActionListener(this);
        menuFile.add(menuNew);
        //�򿪲˵�
        JMenuItem menuOpen = new JMenuItem("Open");
        menuOpen.addActionListener(this);
        menuFile.add(menuOpen);
        //����˵�
        JMenuItem menuSave = new JMenuItem("Save");
        menuSave.addActionListener(this);
        menuFile.add(menuSave);
        //���Ϊ�˵�
        JMenuItem menuSaveAs = new JMenuItem("Save as...");
        menuSaveAs.addActionListener(this);
        menuFile.add(menuSaveAs);
        //�˳��˵�
        JMenuItem menuExit = new JMenuItem("Exit");
        menuExit.addActionListener(this);
        menuFile.add(menuFile);

        menuBar.add(menuFile);

        JMenu menuHelp = new JMenu("Help");
        JMenuItem menuAbout = new JMenuItem("About");
        menuAbout.addActionListener(this);
        menuHelp.add(menuAbout);
        menuBar.add(menuHelp);

        //������
        frame = new JFrame("Java Text Editor");
        frame.setJMenuBar(menuBar);
        textArea = new JTextArea();
        frame.add("Center", textArea);
        frame.addWindowListener(this);//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);��
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

    //�˵�ѡ���¼�
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand() == "New") {
                textArea.setText("");
            } else if (e.getActionCommand() == "Open") {
                //ѡ���ļ�
                JFileChooser dlg = new JFileChooser();
                int result = dlg.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = dlg.getSelectedFile();
                    fileName = file.getAbsolutePath();

                    //��ȡ�ļ�
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
                //д���ļ�
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
                    //д���ļ�
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
//Java���ı�̼��� ��19�� SWTͼ�ν��濪�� P607
��ʱû����
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��20�� SWT��ǿ����� P643
��ʱû����
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��21�� Applet������ P661
��ʱû����
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��22�� Java������ P681
��ʱû����
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��23�� NIO��������� P741
��ʱû����
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��24�� RMI�ֲ�ʽ������ P801
��ʱû����
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��25�� Corba�ֲ�ʽ������ P821
25.2 ʹ��Java��дCORBA���򡪡�HelloWorldʵ�� P827
ʹ��Java��дCORBA������Ҫ������IDL�ӿڵı������룬���������µ�5�����̡�
��1������Զ�̽ӿ�
��2������Զ�̽ӿ�
��3��ʵ�ַ�����
��4��ʵ�ֿͻ���
��5������Ӧ�ó���

25.2.1 ����IDL�ӿ�Hello.idl P828
��дIDL�ӿ��ļ�������srcĿ¼�£�
module helloworld
{
    interface Hello
    {
        string sayHello(in string world);
    };
};
ָ��ģ����Ϊhelloworld���ӿ���ΪHello��������һ���ӿں���sayHello()���������Ϊstring���ͣ�������һ��string���͵Ľ����

25.2.2 ����IDL�ӿڲ���6���ļ�
���µ�JDK 1.3 ���ϰ汾�У��ṩ���µ�IDL�༭��idlj��λ��JDK��bin\idlj.exe��
����Hello.idl�ļ���Ŀ¼��ִ�����±�������������IDL�ӿڣ�
E:\Program Files\JetBrains\JavaProject\src\Hello.idl
������ڵ�ǰĿ¼������helloworld��Ŀ¼�����а���һЩ֧���ļ������£�
--Hello.java����ǽӿ��ļ���CORBA�淶ָ������ļ�������չIDLEntity��������IDL�ӿ�ͬ��������ļ��ṩ���ͱ�ǣ��Ӷ�ʹ����ӿ������������ӿڵķ����������������£�
package helloworld;
public interface Hello extends HelloOperations, org.omg.CORBA.Object, org.omg.CORBA.portable.IDLEntity 
{
} // interface Hello

--HelloOperations.java���ں�Java�����ӿڡ���HelloOperations���淶ָ��������ļ�Ӧ�������Operations��׺��IDL�ӿ�ͬ������������ļ��ں��˽ӿ�ӳ��Ĳ�����ǣ����涨��ı�ǽӿڣ�Hello.java������չ����ӿڡ��������£�
package helloworld;
public interface HelloOperations 
{
  String sayHello (String world);
} // interface HelloOperations

--HelloHelper.java�����helper���Ŀ���ǣ�������Ҫ��������������������ǵĽӿڣ�������ʱ���õ�ʵ�ֹ��̡����������ļ�������Ҫ�ľ�̬narrow���������ַ���ʹorg.omg.CORBA.Object����Ϊһ�ָ���������͵Ķ������ã�����������£�����һ������������͡��������£�
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

--HelloHolder.java��holder����һ��ר�Ż��࣬��Ϊ����Ҫͨ�����������ݲ����������������Ͷ����ɵġ����ʾ���н���ʹ��holder�࣬�������Ժ����Ŀ�о������������������£�
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

--HelloPOA.java��skeleton��ΪCORBA�����ṩ�����󡪡���Ӧ̽���һ�󲿷֡�����HelloPOA.java������ΪĬ�Ϸ�ʽ�µ�ʵ���ǻ��ڼ̳еģ��������ѡ�����ί�еķ�ʽ������ͻ᲻һ�����������£�
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

--_HelloStub.java������һ��stub�ࡣ�ͻ�������Ҫ����������й������������£�
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

�������ɵ�6���ļ�λ��srcĿ¼�µ�helloworld�У����������ǵ�ǰ�Ŀ���Ŀ¼��

25.2.3 ����IDL�ӿ�ʵ����HelloImpl.java P829
���ɵ��ļ��ڷ������Ͽ�ʼ�����������дʵ�ֽӿڵ�ʵ����HelloImpl���̳���HelloPOA�࣬�ӿͻ�������һ������ʱ��������ͨ�� ORB����HelloPOA�����յ���HelloImpl���������������Ӧ�����£�
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

25.2.4 ʵ�ַ����� HelloServer.java P829
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
            //���ݶ˿ڴ���ORBʵ��
            String str[] = {"-ORBInitialPort", "1050"};
            ORB orb = ORB.init(str, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("RootPOA");

            //�����������
            org.omg.PortableServer.POA rootPOA = org.omg.PortableServer.POAHelper.narrow(objRef);
            org.omg.PortableServer.POAManager manager = rootPOA.the_POAManager();

            //����ʵ����
            HelloImpl helloImpl = new HelloImpl();
            Hello hello = helloImpl._this(orb);

            //����������ע��
            NamingContext ncRef = NamingContextHelper.narrow(orb.resolve_initial_references("NameService"));
            NameComponent nc = new NameComponent("Hello", "");
            NameComponent path[] = {nc};
            ncRef.rebind(path, hello);
            manager.activate();
            System.out.println("Service is running...");

            //�ȴ��ͻ��˵���
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

25.2.5 ʵ�ֿͻ��� HelloClient.java P830
����һ�����ڷ������¼��Ļ��ƣ��ͻ����׿ͻ��˺ͷ�����ʵ�����ǻ�Ϊӳ��ġ��ͻ��˽����еĲ�������Դ���һ������Ȼ�������Լ��ķ�ʽ������������󡣷�����ֻ�ǽ������еĲ��������ִ�����У�������ֵ��������������Ȼ����ͻ��˷�����Ӧ���ͻ����򽫷���ֵ��������������Ȼ����������������ͻ��˴��ʲô���������ͽ��ʲô����֮��Ȼ������ζ�Ž��ῴ���ͻ��˺ͷ������������ƵĽṹ���ͻ��˻����봴������ʼ��һ��ORB��
��1������һ��ORB��
��2����ȡһ��ָ�����������ĵ����á�
��3���������������в��ҡ�Hello�������ָ���CORBA��������á�
��4�����ö����sayHello()��������ӡ�����
�ͻ��˳������£�
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
            //���ݶ˿ڴ���ORBʵ��
            String str[] = {"-ORBInitialPort", "1050"};
            ORB orb = ORB.init(str, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            //��ȡһ��ָ�����������ĵ�����
            NamingContext ncRef = NamingContextHelper.narrow(objRef);
            NameComponent nc = new NameComponent("Hello", "");
            NameComponent path[] = {nc};

            //����Hello����
            Hello hello = HelloHelper.narrow(ncRef.resolve(path));

            //���ú���
            String show = hello.sayHello("World");
            System.out.println(show);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

25.2.6 ���г��� P831
��1������һ��MS-DOS�����������
tnameserv -ORBInitialPort 1050

���к���ʼ�����ö˿�Ϊ1050������Ĵ������£�
C:\Users\Ben>tnameserv -ORBInitialPort 1050
��ʼ������������:
IOR:000000000000002b49444c3a6f6d672e6f72672f436f734e616d696e672f4e616d696e67436f6e746578744578743a312e30000000000001000000000000009a000102000000000e3139322e3136382e312e31323100041a00000045afabcb0000000020000f424000000001000000000000000200000008526f6f74504f41000000000d544e616d65536572766963650000000000000008000000010000000114000000000000020000000100000020000000000001000100000002050100010001002000010109000000010001010000000026000000020002
TransientNameServer: ����ʼ�������ö˿�����Ϊ: 1050
׼��������

��2������HelloServer��������
����Service is running...

��3������HelloClientӦ�ó���ͻ��ˣ�
��Ļ�ϻ���֡�Hello World!����������˵��ʵ��ɹ��ˡ�

�봫ͳ�Ŀͻ�/����Ӧ�ÿ�����ȫ��ͬ��ʹ��CROBA�󣬿�����Ա�ٲ��ع��Ŀͻ��ͷ���֮���ͨ�����⣬Ҳ���ش���ͻ��ͷ���֮���Э�����⣬�ͻ�ϵͳ�ͷ���ϵͳ�����ڲ�ͬ�Ļ���ϵͳ�����У����ҿ����ò�ͬ��������ʵ�֣��籾���еķ�����������ȫ������C++����д������Щ����CORBA����������Ӧ�ÿ�������˵����͸���ġ�

25.2.7 ���䣺IDL���﷨���� P832
�ӿ��������Լ�Interface Description Language������дΪIDL�������������������ӿڵ�һ�ּ�������ԡ�IDLͨ��һ�������ķ�ʽ�������ӿڣ�ʹ���ڲ�ͬƽ̨�����еĶ�����ò�ͬ���Ա�д�ĳ�������໥ͨ�Ž������磬һ�������C++д����һ����Javaд�ɡ�
1. OMG IDL�ļ��ṹ
IDL�ļ���module��ʼ����ģ�������൱��Java�����еİ�����Ȼ����ģ���ж����������ͺͽӿڡ����£�
module Compute
{
    typedef double radius;
    typedef long times;
    interface PI
    {double getResult(in radius aRadius, in times time);}
}

2. OMG IDL�﷨����
����ASCII�ַ������ɽӿڶ�������б�ʶ������ʶ������ĸ�����ֺ��»��ߵ�������Ϲ��ɣ�����һ���ַ�������ASCII��ĸ��IDL��Ϊ��д��ĸ��Сд��ĸ������ͬ�ĺ��壬��anExample��AnExample����ͬ�ġ�
���������Ա���ܽ��ؼ������������򷽷�������Ҫע����ǹؼ��ֵĴ�Сд���磺
typedef double context;//���󣬱���context�ǹؼ���
typedef double CONTEXT;//����CONTEXT��ؼ���context��ͻ

3. ��������
--�����������ͣ�OMG IDL�����������Ͱ���short��long����Ӧ���޷��ţ�unsigned�����ͣ���ʾ���ֳ��ֱ�Ϊ16��32λ��
--���������ͣ�OMG IDL���������Ͱ���float��double��long double���͡�
--�ַ��ͳ����ַ����ͣ�OMG IDL�����ַ�����charΪ�����ֽڵ��뼯�б���ĵ��ֽ��ַ�����������wcharΪ�������ַ����б���ĳ����ַ���
--�߼����ͣ���boolean�ؼ��ֶ����һ���������ۻ�ֻ��true��false��
--�˽������ͣ���octet�ؼ��ֶ��壬�����紫������в����иߵ�λת����λԪ���С�
--any�������ͣ�������������ڱ�ʾOMG IDL�������������͡�

4. ����
const double PI = 3.1415926;
��IDL�У����Զ���long��unsigned long��string�����͵ĳ�����

5. ������������ P834
OMG IDL�й����������Ͱ����ṹ�����ϡ�ö�ٵ���ʽ����������
�ṹ���ͣ�
typedef long GoodsNumber;
struct
{
    GoodsNumber number;
    string name;
    float price;
}

�������ͣ�
union stockIn switch(short)
{
    case 1: stocker: long;
    case 2: goodsName1: string;
    case 3: goodsName2: string;
}

ö�����ͣ�
enum GoodsNumber{GOODS_SAILED, GOODS_INSTOCK};

6. �������� P834
typedef long ADimension[20][100];

7. ģ��(template)����
OMG IDL�ṩ�������͵�ģ�塣
��1�����У�sequence������
�ø÷������Զ��峤�ȿɱ��������ֵ���͵Ĵ洢���У�ͨ���ڶ���ʱ����ָ�����ȣ�Ҳ���Բ�ָ�����磺
typedef sequence<80> aSequence;//���ȶ���Ϊ80
typedef sequence anotherSequence;//���Ȳ���

��2���ַ�����string������
ͬ�������ַ����������ͣ�Ҳ�����ֶ��巽ʽ��
typedef string<80> aName;
typedef string anotherName;

8. �ӿ�(interface)
�ӿ���Ϊ��������ܵ���ϸ��������װ�˷�������ṩ���񷽷���ȫ����Ϣ���ͻ��������øýӿڻ�ȡ�����������ԡ����ʷ���Ծ��еķ������ӿ��ùؼ���interface���������а��������Ժͷ��������������������Ŀͻ������ǹ����ģ����£�
interface JobManager
{
    readonly attribute string FirstName;
    attribute string status;
    string QueryJobStatus(in long Number, out string property);
}
//------------------------------------------------------------------------------------------------
//Java���ı�̼��� ��26�� Java�������붯̬���� P845
Java���䣨Reflection��������������״̬�У����� ����һ���࣬���ܹ������������������Ժͷ�������������һ�����󣬶��ܹ�������������һ�����������ֶ�̬��ȡ����Ϣ����̬���ö���ķ����Ĺ��ܳ�ΪJava���Եķ�����ơ�
Java���������Ҫ�ṩ�����¹��ܡ�
--������ʱ�ж�����һ�������������ࡣ
--������ʱ�ж�����һ���������еĳ�Ա�����ͷ�����
--������ʱ��������һ����Ķ���
--������ʱ��������һ������ķ����ͱ�����
--���ɶ�̬����Dynamic Proxy����

26.1 Java������� P845
26.1.1 ����ĸ��� P845
�����Ժ�ԭ�������Ƿ���ϵͳ�����ڻ���Ҫ�ء�

26.1.2 Java�еķ��� P846
Reflection��Java���򿪷����Ե�����֮һ�������������е�Java�����������м�飬����˵�����󡱣�����ֱ�Ӳ���������ڲ����ԡ����磬ʹ�����ܹ����Java���и���Ա�����Ʋ���ʾ������

26.1.3 ��һ����������� P846

package test;

import java.lang.reflect.Method;//����������ĳ�����е���������һ����

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class c = Class.forName("java.lang.System");//����ָ������
            Method m[] = c.getDeclaredMethods();//��ȡ������еĶ����˵ķ����б�
            for (Method method : m) {
                System.out.println(method.toString());
            }
        } catch (Exception e) {
        }
    }
}
�����
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

26.1.4 Java����API P847
Class��Java�����е�һ�������࣬���������ڴ��е�һ��Java�ࡣͨ��������ȡ����ĸ��ֲ������ԣ���Щ������ͨ��java.lang.reflect���еķ���API�������ġ�
--Constructor������һ����Ĺ��췽��
--Field������һ����ĳ�Ա����
--Method������һ����ķ���
--Modifer���������ڸ�Ԫ�ص����η�
--Array��������������в���

�����κ�һ������˵��������ͨ��Class�ṩ�ķ�������Բ�ͬ�ķ�ʽ����������Ϣ�����˿���ʹ��newInstance()���������ʵ���⣬����ʹ��Classȡ����İ���������
Package getPackage();
String getName();

Class����Ҫ�Ĺ������ṩ��һ�鷴����ã�����ȡ�ø���Ĺ��캯����������������
1. ȡ�ù��캯�������������� Constructor P848

2. ȡ�ñ��������������� Field P848

3. ȡ�÷��������������� Method P849
--ʹ���ض��Ĳ������ͻ�������ķ���
Method getMethod(String name, Class<?>...parameterTypes);
--������з����б�
Method[] getMethods();
--��ȡ���ػ�������ķ�����
Method getEnclosingMethod();

4. Array�� P850
�ṩ�˶�̬�����ͷ���Java����ķ�������Щ������Ӧ��8��Java�Ļ����������ͣ����磺
static boolean getBoolean(Object array, int index);
static void setBoolean(Object array, int index, boolean z);

5. Modifier�� P850
�ṩ��static�����ͳ���������ͳ�Ա�����޷������н��롣���η�������ʾΪ�������ò�ͬ��λ�ã�bit position����ʾ��ͬ�����η���
static int ABSTRACT;//��ʾabstract���η���int��ֵ

26.2 Java����Ӧ�á�������� P850
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


26.2.1 ��ȡ�� P851
�����е�Java�����У���java.lang.Class����������ͽӿڵȡ�
������ǻ��һ��Class����ķ���֮һ��
Class cls = Class.forName("test.MyObject");
������һ�ַ��������£�
Class cls = test.MyObject.class;

26.2.2 ��ȡ��ķ���
package test;

import java.lang.reflect.Method;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class cls = Class.forName("test.reflection.MyObject");
            Method m[] = cls.getDeclaredMethods();
            for (Method method : m) { //for (int i = 0; i < methods.length; ++i)
                System.out.println("��������" + method.getName());
                System.out.println("���η���" + method.getModifiers());
                System.out.println("����ֵ��" + method.getReturnType());

                Class parameters[] = method.getParameterTypes();
                for (int j = 0; j < parameters.length; ++j) {
                    System.out.println("������" + j + "��" + parameters[j]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
�����
��������sum
���η���1
����ֵ��int
��������divide
���η���1
����ֵ��int
��������minus
���η���1
����ֵ��int
��������longer
���η���1
����ֵ��class java.lang.String
������0��class java.lang.String
������1��class java.lang.String
��������multiply
���η���1
����ֵ��int

���η�public��1��private��2
getDeclaredMethods()����ȡһϵ�е�Method���󣬷ֱ������˶��������е�ÿһ�����󣬰���public������protected������private�����ȡ�����ڳ�����ʹ��getMethods()����������ü̳����ĸ�����������Ϣ��

26.2.3 ��ȡ��Ĺ����� P852
��ȡ�๹�������÷���������ȡ�������÷����ƣ����£�
package test;

import java.lang.reflect.Constructor;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class cls = Class.forName("test.reflection.MyObject");
            Constructor constructors[] = cls.getDeclaredConstructors();
            for (Constructor constructor : constructors) { //for (int i = 0; i < methods.length; ++i)
                System.out.println("��������" + constructor.getName());
                System.out.println("���η���" + constructor.getModifiers());

                Class parameters[] = constructor.getParameterTypes();
                for (int j = 0; j < parameters.length; ++j) {
                    System.out.println("������" + j + "��" + parameters[j]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
�����
��������test.reflection.MyObject
���η���1
������0��int
������1��int

26.2.4 ��ȡ��ı��� P852
�ҳ�һ�����ж�������Щ�����ֶ�Ҳ�ǿ��ܵģ����£�
package test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class cls = Class.forName("test.reflection.MyObject");
            Field fields[] = cls.getDeclaredFields();
            for (Field field : fields) { //for (int i = 0; i < methods.length; ++i)
                System.out.println("��������" + field.getName());
                System.out.println("���η���" + field.getModifiers());
                System.out.println("�������ͣ�" + field.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
�����
��������a
���η���1
�������ͣ�int
��������b
���η���1
�������ͣ�int

�ͻ�ȡ���������һ������ȡ�ֶε�ʱ��Ҳ����ֻȡ���ڵ�ǰ���������˵��ֶ���Ϣ��ʹ��getDeclaredFields()������������Ҳ����ȡ�ø����ж�����ֶΣ�ʹ��getFields()��������

26.3 Java����Ӧ�á���������� P853
���Է�����������ʵ����һ������������MyObject���캯��������int���͵Ĳ����������Ҫ�ȴ���һ���������飬�ٸ�����������ȡ�ù��캯����

package test;

import java.lang.reflect.Constructor;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class cls = Class.forName("test.reflection.MyObject");
            //���ò�����������
            Class paramTypes[] = new Class[2];
            paramTypes[0] = int.class;
            paramTypes[1] = int.class;

            //���ݲ�������ȡ�ù��캯��
            Constructor constructor = cls.getConstructor(paramTypes);
            Object object =   constructor.newInstance(3, 4);

            //����Ĭ�Ϲ��캯��������������MyObjectû��Ĭ�Ϲ��캯�������б���
//            constructor = cls.getConstructor();
//            object = constructor.newInstance();
            //System.out.println("3 + 4 = " + ((MyObject) object).sum());//Ӧ�ò����������ú�����

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

26.3.2 �ı������ֵ P853
package test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class cls = Class.forName("test.reflection.MyObject");
            //���ò�����������
            Class paramTypes[] = new Class[2];
            paramTypes[0] = int.class;
            paramTypes[1] = int.class;

            Constructor constructor = cls.getConstructor(paramTypes);
            Object object = constructor.newInstance(3, 4);

            //�ı��ֶε�ֵ
            Field fieldA = cls.getField("a");
            System.out.println("�ı�ǰ��" + fieldA.get(object));
            fieldA.setInt(object, 100);
            System.out.println("�ı��" + fieldA.get(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
�����
�ı�ǰ��3
�ı��100

26.3.3 ִ����ķ��� P854
�빹�췽���ĵ��ù������ƣ���Ҫ���ȸ�����Ҫ���÷����Ĳ������ʹ���һ�������������飬Ȼ���ٴ���һ������ֵ���飬ִ�з����ĵ��á��������û�в�������ֱ�ӵ��ü��ɡ�
package test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class cls = Class.forName("test.reflection.MyObject");
            //���ò�����������
            Class paramTypes[] = new Class[2];
            paramTypes[0] = int.class;
            paramTypes[1] = int.class;

            Constructor constructor = cls.getConstructor(paramTypes);
            Object object = constructor.newInstance(3, 4);

            //ִ������η���
            Method sumMethod = cls.getMethod("sum");
            Object sumRet = sumMethod.invoke(object);
            System.out.println("3 + 4 = " + sumRet);

            //ִ�д���εķ���
            Class paraStringTypes[] = new Class[] {String.class, String.class};//�����new�������������������������ܴ�����new Class[2] {String.class, String.class}���������
            Method longerMethod = cls.getMethod("longer", paraStringTypes);//������뽫��������������Ͷ�����
            String stringParam[] = {"Hello", "World"};
            Object longerRet = longerMethod.invoke(object, stringParam);//���ﴫ��Ҫ�����Ķ���Ͳ���ֵ
            System.out.println("Longer of Hello and World is " + longerRet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

����һ��������ִ�е�ĳ��ʱ��֪����Ҫִ�е�ĳ������������������������ڳ�������й�����ָ���ģ��磬JavaBean���������оͻ����������£�������ĳ�����ʾ�����������

26.3.4 ʹ������ P854
package test;

import java.lang.reflect.Array;

public class ReflectionTest {

    public static void main(String[] args) {
        try {
            Class str = Class.forName("java.lang.String");//ȡ����
            Object arr = Array.newInstance(str, 10);//�������������
            Array.set(arr, 5, "this is a test");
            String str5 = (String) Array.get(arr, 5);
            System.out.println(str5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
�����
this is a test

����������10����λ���ȵ�String���飬Ϊ��5��λ�õ��ַ�������ֵ���������ַ�����������ȡ������ӡ��

//
package test;

import java.lang.reflect.Array;

public class ReflectionTest {
    public static void main(String[] args) {
        try {
            int dims[] = new int[]{5, 10, 15};
            Object arr = Array.newInstance(Integer.TYPE, dims);//����һ��5*10*15����������
            Object arrObj = Array.get(arr, 3);//��һ��Array.get()֮��arrObj��һ��10*15������
            Class cls = arrObj.getClass().getComponentType();
            System.out.println(cls);
            arrObj = Array.get(arrObj, 5);//�ٴ�Array.get()��ȡ������һ��Ԫ�أ�����Ϊ15������
            Array.setInt(arrObj, 10, 37);//ʹ��Array.setInt()Ϊ��10��Ԫ�ظ�ֵ

            int arrCast[][][] = (int[][][]) arr;
            System.out.println(arrCast[3][5][10]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

Tips����������ʱ�������Ƕ�̬�ģ��ڱ���ʱ����֪�������͡�

26.4 Java��̬���� P855

Tips����������ʱ�������Ƕ�̬�ģ��ڱ���ʱ����֪�������͡�
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

