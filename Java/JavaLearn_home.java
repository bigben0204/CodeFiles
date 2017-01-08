//------------------------------------------------------------------------------------------------
//Mockito Unit tests with Mockito - Tutorial
http://www.vogella.com/tutorials/Mockito/article.html
翻译版：http://www.jianshu.com/p/f6e3ab9719b9

4.3. 配置 mock
当我们需要配置某个方法的返回值的时候，Mockito 提供了链式的 API 供我们方便的调用
when(…?.).thenReturn(…?.)可以被用来定义当条件满足时函数的返回值，如果你需要定义多个返回值，可以多次定义。当你多次调用函数的时候，Mockito 会根据你定义的先后顺序来返回返回值。Mocks 还可以根据传入参数的不同来定义不同的返回值。譬如说你的函数可以将anyString 或者 anyInt作为输入参数，然后定义其特定的放回值。

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@Test
public void test1()  {
        //  创建 mock
        MyClass test = Mockito.mock(MyClass.class);

        // 自定义 getUniqueId() 的返回值
        when(test.getUniqueId()).thenReturn(43);

        // 在测试中使用mock对象
        assertEquals(test.getUniqueId(), 43);
}

// 返回多个值
@Test
public void testMoreThanOneReturnValue()  {
        Iterator i= mock(Iterator.class);
        when(i.next()).thenReturn("Mockito").thenReturn("rocks");
        String result=i.next()+" "+i.next();
        // 断言
        assertEquals("Mockito rocks", result);
}

// 如何根据输入来返回值
@Test
public void testReturnValueDependentOnMethodParameter()  {
        Comparable c= mock(Comparable.class);
        when(c.compareTo("Mockito")).thenReturn(1);
        when(c.compareTo("Eclipse")).thenReturn(2);
        // 断言
        assertEquals(1,c.compareTo("Mockito"));
}

// 如何让返回值不依赖于输入
@Test
public void testReturnValueInDependentOnMethodParameter()  {
        Comparable c= mock(Comparable.class);
        when(c.compareTo(anyInt())).thenReturn(-1);
        // 断言
        assertEquals(-1 ,c.compareTo(9));
}

// 根据参数类型来返回值
@Test
public void testReturnValueInDependentOnMethodParameter()  {
        Comparable c= mock(Comparable.class);
        when(c.compareTo(isA(Todo.class))).thenReturn(0);
        // 断言
        Todo todo = new Todo(5);
        assertEquals(todo ,c.compareTo(new Todo(1)));
}

对于无返回值的函数，我们可以使用doReturn(…?).when(…?).methodCall来获得类似的效果。例如我们想在调用某些无返回值函数的时候抛出异常，那么可以使用doThrow 方法。如下面代码片段所示
package test;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class MockTest {

    @Test(expected = IOException.class)
    public void testForIOException() throws IOException {
        // 创建并配置 mock 对象
        OutputStream mockStream = mock(OutputStream.class);
        doThrow(new IOException()).when(mockStream).close();

        // 使用 mock
        OutputStreamWriter streamWriter= new OutputStreamWriter(mockStream);
        streamWriter.close();
    }
}


4.4. 验证 mock 对象方法是否被调用
Mockito 会跟踪 mock 对象里面所有的方法和变量。所以我们可以用来验证函数在传入特定参数的时候是否被调用。这种方式的测试称行为测试，行为测试并不会检查函数的返回值，而是检查在传入正确参数时候函数是否被调用。

import static org.mockito.Mockito.*;

@Test
public void testVerify()  {
        // 创建并配置 mock 对象
        MyClass test = Mockito.mock(MyClass.class);
        when(test.getUniqueId()).thenReturn(43);

        // 调用mock对象里面的方法并传入参数为12
        test.testing(12);
        test.getUniqueId();
        test.getUniqueId();

        // 查看在传入参数为12的时候方法是否被调用
        verify(test).testing(Matchers.eq(12));//testing测试方法没有找到在哪个包中？

        // 方法是否被调用两次
        verify(test, times(2)).getUniqueId();

        // 其他用来验证函数是否被调用的方法
        verify(mock, never()).someMethod("never called");
        verify(mock, atLeastOnce()).someMethod("called at least once");
        verify(mock, atLeast(2)).someMethod("called at least twice");
        verify(mock, times(5)).someMethod("called five times");
        verify(mock, atMost(3)).someMethod("called at most 3 times");
}


4.5. 使用 Spy 封装 java 对象
@Spy或者spy()方法可以被用来封装 java 对象。被封装后，除非特殊声明（打桩 stub），否则都会真正的调用对象里面的每一个方法
package test;

import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class MockTest {

    @Test(expected = IndexOutOfBoundsException.class)
    public void testForIOException() throws IOException {
        // Lets mock a LinkedList
        List list = new LinkedList();
        List spy = spy(list);

        // 可用 doReturn() 来打桩
        doReturn("foo").when(spy).get(0); //如果这里定义doReturn("foo").when(spy).get(anyInt());则不会抛出异常
        // 或用如下语句打桩
        //when(spy.get(0)).thenReturn("foo");
        assertEquals("foo", spy.get(0));

        // 如果某个方法未被打桩，则真正的方法会被调用
        // 将会抛出 IndexOutOfBoundsException 的异常，因为 List 为空
        spy.get(1);
    }
}

//对于用Spy封装的对象，也是一个Mock对象，不同点就是如果某个方法被打桩，则调用时会调用打桩方法，如果某个方法没有被打桩，调用时会调用真正的对象方法
//也可以用@Spy注解来标识Spy对象，初始化时同@Mock对象
package test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

public class MockTest {

    @Spy
    List<String> spyOnStrings = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testForIOException() {
        // 可用 doReturn() 来打桩
        doReturn("foo").when(spyOnStrings).get(0); //如果这里定义doReturn("foo").when(spy).get(anyInt());则不会抛出异常
        // 或用如下语句打桩
        //when(spy.get(0)).thenReturn("foo");
        assertEquals("foo", spyOnStrings.get(0));

        // 如果某个方法未被打桩，则真正的方法会被调用
        // 将会抛出 IndexOutOfBoundsException 的异常，因为 List 为空
        spyOnStrings.get(1);
    }
}

//-----@Capture注解
package test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MockTest {

    @Captor
    private ArgumentCaptor<Integer> integerArgumentCaptor;

    @Mock
    private List<String> strings;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testForIOException() {
        assertEquals(null, strings.get(0));
        verify(strings).get(integerArgumentCaptor.capture());
        int value = integerArgumentCaptor.getValue();
        assertEquals(0, value);
        verifyNoMoreInteractions(strings);
    }
}


//-----@InjectMocks注解
http://hotdog.iteye.com/blog/937862
通过这个注解，可实现自动注入mock对象。当前版本只支持setter的方式进行注入（但是我如下用例试了，只能通过构造函数进行依赖注入，setter函数无法进行自动注入），Mockito首先尝试类型注入，如果有多个类型相同的mock对象，那么它会根据名称进行注入。当注入失败的时候Mockito不会抛出任何异常，所以你可能需要手动去验证它的安全性。 
例： 
//
package test;

import java.util.List;
import java.util.Map;

public class DataBaseMgr {
    private List<Integer> userIds;
    private Map<Integer, String> idNameMap;

    //如果只有一个如下一个参数的构造函数，同时提供了set函数，dataBaseMgr.addIdName(4, "James");方法将抛出空指针异常，idNameMap为空对象
//    DataBaseMgr(List<Integer> userIds) {
//        this.userIds = userIds;
//    }

    //提供了两个参数的构造函数，测试用例可以运行成功
    DataBaseMgr(List<Integer> userIds, Map<Integer, String> idNameMap) {
        this.userIds = userIds;
        this.idNameMap = idNameMap;
    }


    public void setIdNameMap(Map<Integer, String> idNameMap) {
        this.idNameMap = idNameMap;
    }

    public void addUserId(int id) {
        userIds.add(id);
    }

    public void addIdName(int id, String name) {
        idNameMap.put(id, name);
    }
}

//
package test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

public class DataBaseMgrTest {

    @Mock
    private List<Integer> userIds;

    @Mock
    private Map<Integer, String> idNameMap;

    @InjectMocks
    private DataBaseMgr dataBaseMgr;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInjectMocks() {
        dataBaseMgr.addUserId(3);
        dataBaseMgr.addIdName(4, "James");

        verify(userIds).add(3);
        verify(idNameMap).put(4, "James");
    }
}


4.7. 捕捉参数
ArgumentCaptor类允许我们在verification期间访问方法的参数。得到方法的参数后我们可以使用它进行测试。
//
package test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

public class MockTest {

    @Mock
    private List<String> strings;

    @Captor
    ArgumentCaptor<List<String>> stringArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCaptor() {
        strings.addAll(Arrays.asList("hello", "world"));
        verify(strings).addAll(stringArgumentCaptor.capture());

        List<String> capturedStrings = stringArgumentCaptor.getValue();
        assertThat(capturedStrings, hasItem("hello"));
        assertThat(capturedStrings, hasItem("world"));
    }
}

4.8. Mockito的限制
Mockito当然也有一定的限制。而下面三种数据类型则不能够被测试
final classes
anonymous classes
primitive types

8.1. 使用 Powermock 来模拟静态方法
http://huangyunbin.iteye.com/blog/2176728

因为Mockito使用继承的方式实现mock的，用CGLIB生成mock对象代替真实的对象进行执行，为了mock实例的方法，你可以在subclass中覆盖它，而static方法是不能被子类覆盖的，所以Mockito不能mock静态方法。
但PowerMock可以mock静态方法，因为它直接在bytecode上工作，类似这样：

还是以刚才的DataBaseMgr类来做测试，新增了一个getPassword方法，并且调用的是PasswordMgr的静态方法getPassword
//DataBaseMgr.java
package test;

import java.util.List;
import java.util.Map;

public class DataBaseMgr {
    private List<Integer> userIds;
    private Map<Integer, String> idNameMap;

    DataBaseMgr(List<Integer> userIds, Map<Integer, String> idNameMap) {
        this.userIds = userIds;
        this.idNameMap = idNameMap;
    }

    public void setIdNameMap(Map<Integer, String> idNameMap) {
        this.idNameMap = idNameMap;
    }

    public void addUserId(int id) {
        userIds.add(id);
    }

    public void addIdName(int id, String name) {
        idNameMap.put(id, name);
    }

    public String getUserPassword(Integer userId) {
        return PasswordMgr.getPasword(userId);
    }
}

//PasswordMgr.java
package test;

public class PasswordMgr {
    public static String getPasword(Integer userId) {
        return userId + "_hello";
    }
}

//DataBaseMgrTest.java
package test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
public class DataBaseMgrTest {

    @Mock
    private List<Integer> userIds;

    @Mock
    private Map<Integer, String> idNameMap;

    @InjectMocks
    private DataBaseMgr dataBaseMgr;

    //注掉initMocks也可以测试成功，据猜测PowerMockRunner.class也可以初始化@Mock对象
    // @Before
    // public void setUp() {
        // MockitoAnnotations.initMocks(this);
    // }

    @Test
    @PrepareForTest(PasswordMgr.class) //如果没有这个@PrepareForTest注解，PowerMockito.when无法无法识别静态方法
    public void testStaticMethod() {
        dataBaseMgr.addUserId(3);
        dataBaseMgr.addIdName(4, "James");

        verify(userIds).add(3);
        verify(idNameMap).put(4, "James");

        //如果PasswordMgr.getPasword这个静态方法很容易就调用了，没有复杂的依赖，那么可以直接调用其静态方法
        //assertEquals("101_hello", dataBaseMgr.getUserPassword(101));

        //如果PasswordMgr.getPasword静态方法有很多的依赖，不能很容易的调用，则需要将这个静态方法Mock掉，Mockito无法做到，只能通过PowerMockito
        PowerMockito.mockStatic(PasswordMgr.class);
        PowerMockito.when(PasswordMgr.getPasword(any())).thenReturn("password").thenReturn("helloworld");//这里的any()可以用具体的int，也可以用anyInt()
        assertEquals("password", dataBaseMgr.getUserPassword(100));
        assertEquals("helloworld", dataBaseMgr.getUserPassword(101));
    }
}


//-----PowerMock介绍
http://blog.csdn.net/jackiehff/article/details/14000779
一、为什么要使用Mock工具
在做单元测试的时候，我们会发现我们要测试的方法会引用很多外部依赖的对象，比如：（发送邮件，网络通讯，远程服务, 文件系统等等）。 而我们没法控制这些外部依赖的对象，为了解决这个问题，我们就需要用到Mock工具来模拟这些外部依赖的对象，来完成单元测试。
二、为什么要使用PowerMock
现如今比较流行的Mock工具如jMock 、EasyMock 、Mockito等都有一个共同的缺点：不能mock静态、final、私有方法等。而PowerMock能够完美的弥补以上三个Mock工具的不足。
三、PowerMock简介
PowerMock是一个扩展了其它如EasyMock等mock框架的、功能更加强大的框架。PowerMock使用一个自定义类加载器和字节码操作来模拟静态方法，构造函数，final类和方法，私有方法，去除静态初始化器等等。通过使用自定义的类加载器，简化采用的IDE或持续集成服务器不需要做任何改变。熟悉PowerMock支持的mock框架的开发人员会发现PowerMock很容易使用，因为对于静态方法和构造器来说，整个的期望API是一样的。PowerMock旨在用少量的方法和注解扩展现有的API来实现额外的功能。目前PowerMock支持EasyMock和Mockito。
四、PowerMock入门    
PowerMock有两个重要的注解：
–@RunWith(PowerMockRunner.class)
–@PrepareForTest( { YourClassWithEgStaticMethod.class })
如果你的测试用例里没有使用注解@PrepareForTest，那么可以不用加注解@RunWith(PowerMockRunner.class)，反之亦然。当你需要使用PowerMock强大功能（Mock静态、final、私有方法等）的时候，就需要加注解@PrepareForTest。
五、PowerMock基本用法
(1) 普通Mock： Mock参数传递的对象
测试目标代码：
public boolean callArgumentInstance(File file) {
     return file.exists();
}
测试用例代码： 
@Test 
public void testCallArgumentInstance() {
    File file = PowerMockito.mock(File.class); 
    ClassUnderTest underTest = new ClassUnderTest();
    PowerMockito.when(file.exists()).thenReturn(true);
    Assert.assertTrue(underTest.callArgumentInstance(file)); 
}
说明：普通Mock不需要加@RunWith和@PrepareForTest注解。
(2)  Mock方法内部new出来的对象
测试目标代码：
public class ClassUnderTest {
    public boolean callInternalInstance(String path) { 
        File file = new File(path); 
        return file.exists(); 
    } 
}
测试用例代码：    
@RunWith(PowerMockRunner.class) 
public class TestClassUnderTest {
    @Test 
    @PrepareForTest(ClassUnderTest.class) 
    public void testCallInternalInstance() throws Exception { 
        File file = PowerMockito.mock(File.class); 
        ClassUnderTest underTest = new ClassUnderTest(); 
        PowerMockito.whenNew(File.class).withArguments("bbb").thenReturn(file); 
        PowerMockito.when(file.exists()).thenReturn(true); 
        Assert.assertTrue(underTest.callInternalInstance("bbb")); 
    } 
}
说明：当使用PowerMockito.whenNew方法时，必须加注解@PrepareForTest和@RunWith。注解@PrepareForTest里写的类是需要mock的new对象代码所在的类。
(3) Mock普通对象的final方法
测试目标代码：
public class ClassUnderTest {
    public boolean callFinalMethod(ClassDependency refer) { 
        return refer.isAlive(); 
    } 
}

public class ClassDependency {
    public final boolean isAlive() {
        // do something 
        return false; 
    } 
}
测试用例代码：
@RunWith(PowerMockRunner.class) 
public class TestClassUnderTest {
    @Test 
    @PrepareForTest(ClassDependency.class) 
    public void testCallFinalMethod() {
        ClassDependency depencency =  PowerMockito.mock(ClassDependency.class);
        ClassUnderTest underTest = new ClassUnderTest();
        PowerMockito.when(depencency.isAlive()).thenReturn(true);
        Assert.assertTrue(underTest.callFinalMethod(depencency));
    }
}
说明： 当需要mock final方法的时候，必须加注解@PrepareForTest和@RunWith。注解@PrepareForTest里写的类是final方法所在的类。 
(4) Mock普通类的静态方法
测试目标代码：
public class ClassUnderTest {
    public boolean callStaticMethod() {
        return ClassDependency.isExist(); 
    }  
}

public class ClassDependency {
    public static boolean isExist() {
        // do something 
        return false; 
    } 
}
测试用例代码：
@RunWith(PowerMockRunner.class) 
public class TestClassUnderTest {
    @Test 
    @PrepareForTest(ClassDependency.class) 
    public void testCallStaticMethod() {
        ClassUnderTest underTest = new ClassUnderTest();
        PowerMockito.mockStatic(ClassDependency.class); 
        PowerMockito.when(ClassDependency.isExist()).thenReturn(true);
        Assert.assertTrue(underTest.callStaticMethod());
    }
}
说明：当需要mock静态方法的时候，必须加注解@PrepareForTest和@RunWith。注解@PrepareForTest里写的类是静态方法所在的类。
(5) Mock 私有方法
测试目标代码： 
public class ClassUnderTest {
    public boolean callPrivateMethod() { 
        return isExist(); 
    }       

    private boolean isExist() {
        return false; 
    }
}
     测试用例代码：  
@RunWith(PowerMockRunner.class) 
public class TestClassUnderTest {
    @Test 
    @PrepareForTest(ClassUnderTest.class) 
    public void testCallPrivateMethod() throws Exception { 
       ClassUnderTest underTest = PowerMockito.mock(ClassUnderTest.class); 
       PowerMockito.when(underTest.callPrivateMethod()).thenCallRealMethod(); 
       PowerMockito.when(underTest, "isExist").thenReturn(true);
       Assert.assertTrue(underTest.callPrivateMethod());
    }
}
说明：和Mock普通方法一样，只是需要加注解@PrepareForTest(ClassUnderTest.class)，注解里写的类是私有方法所在的类。 
(6) Mock系统类的静态和final方法 
测试目标代码：   
public class ClassUnderTest {
    public boolean callSystemFinalMethod(String str) {
        return str.isEmpty(); 
    } 

    public String callSystemStaticMethod(String str) {
        return System.getProperty(str); 
    }
}
测试用例代码：
@RunWith(PowerMockRunner.class) 
public class TestClassUnderTest {
  @Test 
  @PrepareForTest(ClassUnderTest.class) 
  public void testCallSystemStaticMethod() { 
      ClassUnderTest underTest = new ClassUnderTest(); 
      PowerMockito.mockStatic(System.class); 
      PowerMockito.when(System.getProperty("aaa")).thenReturn("bbb");
      Assert.assertEquals("bbb", underTest.callJDKStaticMethod("aaa")); 
  } 
}
说明：和Mock普通对象的静态方法、final方法一样，只不过注解@PrepareForTest里写的类不一样 ，注解里写的类是需要调用系统方法所在的类。
六 、无所不能的PowerMock
(1) 验证静态方法：
PowerMockito.verifyStatic();
Static.firstStaticMethod(param);
(2) 扩展验证:
PowerMockito.verifyStatic(Mockito.times(2)); //  被调用2次                                Static.thirdStaticMethod(Mockito.anyInt()); // 以任何整数值被调用
(3) 更多的Mock方法
http://code.google.com/p/powermock/wiki/MockitoUsage13
七、PowerMock简单实现原理
?  当某个测试方法被注解@PrepareForTest标注以后，在运行测试用例时，会创建一个新的org.powermock.core.classloader.MockClassLoader实例，然后加载该测试用例使用到的类（系统类除外）。

?   PowerMock会根据你的mock要求，去修改写在注解@PrepareForTest里的class文件（当前测试类会自动加入注解中），以满足特殊的mock需求。例如：去除final方法的final标识，在静态方法的最前面加入自己的虚拟实现等。

?   如果需要mock的是系统类的final方法和静态方法，PowerMock不会直接修改系统类的class文件，而是修改调用系统类的class文件，以满足mock需求。
//------------------------------------------------------------------------------------------------
//初始化@Mock注解对象的三个方法
http://blog.csdn.net/hotdust/article/details/51416670
(1) 在@Before注解中每次初始化mock对象
(2) 在测试类定义处使用@RunWith(MockitoJUnitRunner.class)注解
(3) 使用@Rule注解（没有找到合适的MockitoRule定义，不知道是什么问题？）

//
package test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) //(2)
public class MockTest {

    @Mock
    private List<String> strings;

//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule(); //(3)

//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this); //(1)
//    }

    @Test
    public void testMockitoJUnitRunner() {
        when(strings.get(0)).thenReturn("hello");
        assertEquals("hello", strings.get(0));
        verify(strings).get(0);
    }
}

//------------------------------------------------------------------------------------------------
//Mockito学习
http://blog.csdn.net/zhoudaxia/article/details/33056093
Mockito 框架
Mockito 是一个基于MIT协议的开源java测试框架。 
Mockito区别于其他模拟框架的地方主要是允许开发者在没有建立“预期”时验证被测系统的行为。对mock对象的一个批评是测试代码与被测系统高度耦合，由于Mockito试图通过移除“期望规范”来去除expect-run-verify模式（期望--运行--验证模式），因此使耦合度降低到最低。这样的突出特性简化了测试代码，使它更容易阅读和修改了。
//
package test;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.internal.InOrderImpl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockTest {
    @Test
    public void testMock() {
        // 模拟的创建，对接口进行模拟
        List mockedList = mock(List.class);
        // 使用模拟对象
        mockedList.add("one");//mockedList.Add("one")将报错无法解析Add，调用的方法一定是被Mock的类有的方法
        mockedList.clear();
        // 选择性地和显式地验证，即验证mock对象的函数调用了几次，可以不用预先设置期待返回，降低耦合
        verify(mockedList, times(1)).add("one");//同verify(mockedList).add("one");不加次数时默认为1次
        verify(mockedList, atLeast(1)).clear();//verify(mockedList, atLeastOnce()).clear()
        verify(mockedList, never()).add("two");//验证从未调用过
        verifyNoMoreInteractions(mockedList);//验证还有没有verify的交互，如果把verify(mockedList, atLeast(1)).clear();注释掉，则会报错mockedList.clear();未验证
        verifyZeroInteractions(mockedList);//同verifyNoMoreInteractions(mockedList)一样
        
        //初始化一个抓取器，用于抓取入参对象
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockedList).add(stringArgumentCaptor.capture());//抓取一个String对象
        String addedString = stringArgumentCaptor.getValue();//获得抓取的对象
        assertEquals("one", addedString);//验证抓取对象值

        verify(mockedList, times(1)).add("one");//可以重复验证
        verify(mockedList, atMost(2)).clear();
        verify(mockedList, timeout(100).times(1)).add("one");//验证someMethod()是否能在指定的100毫秒中执行完毕

//        List<Integer> integers = new ArrayList<>(); //如果不是Mock对象，则verify会报错
//        integers.add(1);
//        integers.add(2);
//        verify(integers).add(3);
    }

    @Test
    public void testMockReturn() {
        // 你不仅可以模拟接口,任何具体类都行
        LinkedList mockedList = mock(LinkedList.class);
        // 执行前准备测试数据
        when(mockedList.get(0)).thenReturn("first");
        // 接着打印"first"
        System.out.println(mockedList.get(0));
        // 因为get(999)未对准备数据,所以下面将打印"null".
        System.out.println(mockedList.get(999));
    }
    
    @Test
    public void testInOrder() {
        List<String> firstMock = mock(List.class);
        List<String> secondMock = mock(List.class);

        firstMock.add("was called first");
        firstMock.add("was called first");
        secondMock.add("was called second");
        secondMock.add("was called third");

        InOrder inOrder = new InOrderImpl(Arrays.asList(secondMock, firstMock));//如下定义也可以：InOrder inOrder = new InOrderImpl(Arrays.asList(firstMock, secondMock));
        inOrder.verify(firstMock, times(2)).add("was called first");
        inOrder.verify(secondMock).add("was called second");
        inOrder.verify(secondMock).add("was called third");//依次调用这三个顺序，如果不是这三个调用顺序，则报错
        inOrder.verifyNoMoreInteractions();//如果未验证完，则此校验不过
    }
}
输出：
first
null

//-----
//Person.java
package mockitodemo;

public class Person {
    private final Integer personID;
    private final String personName;

    public Person(Integer personID, String personName) {
        this.personID = personID;
        this.personName = personName;
    }

    public Integer getPersonID() {
        return personID;
    }

    public String getPersonName() {
        return personName;
    }
}

//PersonDao.java
package mockitodemo;

public interface PersonDao {
    Person fetchPerson(Integer personID);

    void update(Person person);
}

//PersonService.java
package mockitodemo;

public class PersonService {
    private final PersonDao personDao;

    public PersonService(PersonDao personDao) {
        this.personDao = personDao;
    }

    public boolean update(Integer personId, String name) {
        Person person = personDao.fetchPerson(personId);
        if (person != null) {
            Person updatedPerson = new Person(person.getPersonID(), name);
            personDao.update(updatedPerson);
            return true;
        } else {
            return false;
        }
    }
}

//PersonServiceTest.java
package mockitodemo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class PersonServiceTest {

    @Mock
    private PersonDao personDao;  // 模拟对象
    private PersonService personService;  // 被测类

    public PersonServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        //如果将personDao和personService改为static变量，并且在@BeforeClass中初始化，则全局所有用例都使用这两个变量
        //此时，第一个用例调用了personDao.fetchPerson方法后，第二个用例再次调用此方法，将导致此方法调用了2次
        //如shouldNotUpdateIfPersonNotFound()中的verify(personDao).fetchPerson(1);方法将验证失败
        //此时修改为verify(personDao, times(2)).fetchPerson(1);，则用例会通过
        //但是这样的话，就违反了用例测试的FIRST原则，F快速、I独立、R可重复、S自验证、T及时
        //personDao = mock(PersonDao.class);
        //personService = new PersonService(personDao);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    // 在@Test标注的测试方法之前运行
    @Before
    public void setUp() throws Exception {
        // 初始化测试用例类中由Mockito的注解标注的所有模拟对象
        MockitoAnnotations.initMocks(this);//一定要testClass已经完全生成好，所以无法放在@BeforeClass里
        // 用模拟对象创建被测类对象
        personService = new PersonService(personDao);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldUpdatePersonName() {
        Person person = new Person(1, "Phillip");
        // 设置模拟对象的返回预期值
        when(personDao.fetchPerson(1)).thenReturn(person);
        // 执行测试
        boolean updated = personService.update(1, "David");
        // 验证更新是否成功
        assertTrue(updated);
        // 验证模拟对象的fetchPerson(1)方法是否被调用了一次
        verify(personDao).fetchPerson(1);
        // 得到一个抓取器
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        // 验证模拟对象的update()是否被调用一次，并抓取调用时传入的参数值
        verify(personDao).update(personCaptor.capture());
        // 获取抓取到的参数值
        Person updatePerson = personCaptor.getValue();
        // 验证调用时的参数值
        assertEquals("David", updatePerson.getPersonName());
        // asserts that during the test, there are no other calls to the mock object.
        // 检查模拟对象上是否还有未验证的交互
        verifyNoMoreInteractions(personDao);
    }

    @Test
    public void shouldNotUpdateIfPersonNotFound() {
        // 设置模拟对象的返回预期值
        when(personDao.fetchPerson(1)).thenReturn(null);
        // 执行测试
        boolean updated = personService.update(1, "David");
        // 验证更新是否失败
        assertFalse(updated);
        // 验证模拟对象的fetchPerson(1)方法是否被调用了一次
        verify(personDao).fetchPerson(1);
        // 验证模拟对象是否没有发生任何交互
        verifyZeroInteractions(personDao);
        // 检查模拟对象上是否还有未验证的交互
        verifyNoMoreInteractions(personDao);
    }

    /**
     * Test of update method, of class PersonService.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        Integer personId = null;
        String name = "Phillip";
        PersonService instance = new PersonService(new PersonDao() {

            @Override
            public Person fetchPerson(Integer personID) {
                System.out.println("Not supported yet.");
                return null;
            }

            @Override
            public void update(Person person) {
                System.out.println("Not supported yet.");
            }
        });
        boolean expResult = false;
        boolean result = instance.update(personId, name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
这里setUpClass()、tearDownClass()、setUp()、tearDown()称为测试夹具（Fixture），就是测试运行程序（test runner）在运行测试方法之前进行初始化、或之后进行回收资源的工作。JUnit 4 之前是通过setUp、tearDown方法完成。在JUnit 4 中，仍然可以在每个测试方法运行之前初始化字段和配置环境，当然也是通过注解完成。在JUnit 4 中，通过@Before标注setUp方法；@After标注tearDown方法。在一个测试类中，甚至可以使用多个@Before来注解多个方法，这些方法都是在每个测试之前运行。说明一点，一个测试用例类可以包含多个打上@Test注解的测试方法，在运行时，每个测试方法都对应一个测试用例类的实例。@Before是在每个测试方法运行前均初始化一次，同理@Ater是在每个测试方法运行完毕后均执行一次。也就是说，经这两个注解的初始化和注销，可以保证各个测试之间的独立性而互不干扰，它的缺点是效率低。另外，不需要在超类中显式调用初始化和清除方法，只要它们不被覆盖，测试运行程序将根据需要自动调用这些方法。超类中的@Before方法在子类的@Before方法之前调用（与构造函数调用顺序一致），@After方法是子类在超类之前运行。
　　这里shouldUpdatePersonName()、shouldNotUpdateIfPersonNotFound()和testUpdate()都是测试PersonService的update()方法，它依赖于PersonDao接口。前两者使用了模拟测试。testUpdate()则没有使用模拟测试。下面是测试结果：

　　可以看出，使用模拟测试的两个测试成功了，没有使用模拟测试的testUpdate()失败。对于模拟测试，在测试用例类中要先声明依赖的各个模拟对象，在setUp()中用MockitoAnnotations.initMocks()初始化所有模拟对象。在进行模拟测试时，要先设置模拟对象上方法的返回预期值，执行测试时会调用模拟对象上的方法，因此要验证这些方法是否被调用，并且传入的参数值是否符合预期。对于testUpdate()测试，我们需要自己创建测试PersonService.update()所需的所有PersonDao数据，因为我们只知道公开的PersonDao接口，其具体实现类（比如从数据库中拿真实的数据，或写入到数据库中）可能由另一个团队在负责，以适配不同的数据库系统。这样的依赖关系无疑使单元测试比较麻烦，而要拿真正PersonDao实现来进行测试，那也应该是后期集成测试的任务，把不同的组件集成到一起在真实环境中测试。有了模拟测试框架，就可以最大限度地降低单元测试时的依赖耦合性。

//-----学习Mockito - Mock对象的行为验证
http://hotdog.iteye.com/blog/908827

--验证的基本方法 
我们已经熟悉了使用verify(mock).someMethod(…)来验证方法的调用。例子中，我们mock了List接口，然后调用了mock对象的一些方法。验证是否调用了mock.get(2)方法可以通过verify(mock).get(2)来进行。verify方法的调用不关心是否模拟了get(2)方法的返回值，只关心mock对象后，是否执行了mock.get(2)，如果没有执行，测试方法将不会通过。 

--验证未曾执行的方法 
在verify方法中可以传入never()方法参数来确认mock.get(3)方法不曾被执行过。另外还有很多调用次数相关的参数将会在下面提到。 

--查询多余的方法调用 
verifyNoMoreInteractions()方法可以传入多个mock对象作为参数，用来验证传入的这些mock对象是否存在没有验证过的调用方法。本例中传入参数mock，测试将不会通过，因为我们只verify了mock对象的get(2)方法，没有对get(0)和get(1)进行验证。为了增加测试的可维护性，官方不推荐我们过于频繁的在每个测试方法中都使用它，因为它只是测试的一个工具，只在你认为有必要的时候才用。 

--查询没有交互的mock对象 
verifyZeroInteractions()也是一个测试工具，源码和verifyNoMoreInteractions()的实现是一样的，为了提高逻辑的可读性，所以只不过名字不同。在例子中，它的目的是用来确认mock2对象没有进行任何交互，但mock2执行了get(0)方法，所以这里测试会报错。由于它和verifyNoMoreInteractions()方法实现的源码都一样，因此如果在verifyZeroInteractions(mock2)执行之前对mock.get(0)进行了验证那么测试将会通过。 

--验证方法调用的次数 
如果要验证Mock对象的某个方法调用次数，则需给verify方法传入相关的验证参数，它的调用接口是verify(T mock, VerificationMode mode)。如：verify(mock,times(3)).someMethod(argument)验证mock对象someMethod(argument)方法是否调用了三次。times(N)参数便是验证调用次数的参数，N代表方法调用次数。其实verify方法中如果不传调用次数的验证参数，它默认传入的便是times(1)，即验证mock对象的方法是否只被调用一次，如果有多次调用测试方法将会失败。 

Mockito除了提供times(N)方法供我们调用外，还提供了很多可选的方法： 
never() 没有被调用，相当于times(0) 
atLeast(N) 至少被调用N次 
atLeastOnce() 相当于atLeast(1) 
atMost(N) 最多被调用N次 

--超时验证 
Mockito提供对超时的验证，但是目前不支持在下面提到的顺序验证中使用。进行超时验证和上述的次数验证一样，也要在verify中进行参数的传入，参数为timeout(int millis)，timeout方法中输入的是毫秒值。下面看例子： 
验证someMethod()是否能在指定的100毫秒中执行完毕 
verify(mock, timeout(100)).someMethod(); 
结果和上面的例子一样，在超时验证的同时可进行调用次数验证，默认次数为1 
verify(mock, timeout(100).times(1)).someMethod(); 
在给定的时间内完成执行次数 
verify(mock, timeout(100).times(2)).someMethod(); 
给定的时间内至少执行两次 
verify(mock, timeout(100).atLeast(2)).someMethod(); 
另外timeout也支持自定义的验证模式， 
verify(mock, new Timeout(100, yourOwnVerificationMode)).someMethod(); 

--验证方法调用的顺序 
Mockito同样支持对不同Mock对象不同方法的调用次序进行验证。进行次序验证是，我们需要创建InOrder对象来进行支持。例： 

创建mock对象 
List<String> firstMock = mock(List.class); 
List<String> secondMock = mock(List.class); 

调用mock对象方法 
firstMock.add("was called first"); 
firstMock.add("was called first"); 
secondMock.add("was called second"); 
secondMock.add("was called third"); 

创建InOrder对象 
inOrder方法可以传入多个mock对象作为参数，这样便可对这些mock对象的方法进行调用顺序的验证InOrder inOrder = inOrder( secondMock, firstMock ); 

验证方法调用 
接下来我们要调用InOrder对象的verify方法对mock方法的调用顺序进行验证。注意，这里必须是你对调用顺序的预期。 

InOrder对象的verify方法也支持调用次数验证，上例中，我们期望firstMock.add("was called first")方法先执行并执行两次，所以进行了下面的验证inOrder.verify(firstMock,times(2)).add("was called first")。其次执行了secondMock.add("was called second")方法，继续验证此方法的执行inOrder.verify(secondMock).add("was called second")。如果mock方法的调用顺序和InOrder中verify的顺序不同，那么测试将执行失败。 

InOrder的verifyNoMoreInteractions()方法 
它用于确认上一个顺序验证方法之后，mock对象是否还有多余的交互。它和Mockito提供的静态方法verifyNoMoreInteractions不同，InOrder的验证是基于顺序的，另外它只验证创建它时所提供的mock对象，在本例中只对firstMock和secondMock有效。例如： 

inOrder.verify(secondMock).add("was called second"); 
inOrder.verifyNoMoreInteractions(); 

在验证secondMock.add("was called second")方法之后，加上InOrder的verifyNoMoreInteractions方法，表示此方法调用后再没有多余的交互。例子中会报错，因为在此方法之后还执行了secondMock.add("was called third")。现在将上例改成： 

inOrder.verify(secondMock).add("was called third"); 
inOrder.verifyNoMoreInteractions(); 

测试会恢复为正常，因为在secondMock.add("was called third")之后已经没有多余的方法调用了。如果这里换成Mockito类的verifyNoMoreInteractions方法测试还是会报错，它查找的是mock对象中是否存在没有验证的调用方法，和顺序是无关的。 

//-----
Mock对象时不依赖于类的构造函数，即使一个类没有无参数构造函数，也可以调用mock(SomeClass.class)方法来mock该对象
//
package test;

public class Person {
    private int id;
    private String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonTest {
    @Test
    public void testPersonMock() {
        Person mockedPerson = mock(Person.class);

        when(mockedPerson.getId()).thenReturn(10);
        assertEquals(10, mockedPerson.getId());
    }
}

//-----
参数捕获器
http://blog.csdn.net/mergades/article/details/51009631

Mockito - Argument Matcher（参数匹配器）
http://hotdog.iteye.com/blog/908381

package test;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockTest {
    @Test
    public void testMockCapture() {
        // 模拟的创建，对接口进行模拟
        List<String> mockedList = mock(List.class);
        mockedList.add("a");
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockedList).add("a");
        verify(mockedList).add(stringArgumentCaptor.capture());
        assertEquals("a", stringArgumentCaptor.getValue());

        mockedList.add("b");
        verify(mockedList, times(2)).add(stringArgumentCaptor.capture());//这里验证了times(2)，捕获器会把两次的值都捕获到，即"a", "b"
        assertEquals("b", stringArgumentCaptor.getValue());

        assertArrayEquals(new String[] {"a", "a", "b"}, stringArgumentCaptor.getAllValues().toArray());//总共捕获了三次，所以是"a", "b", "c"
    }

    @Test
    public void testMockReturn() {
        // 你不仅可以模拟接口,任何具体类都行
        LinkedList mockedList = mock(LinkedList.class);
        // 执行前准备测试数据
        when(mockedList.get(0)).thenReturn("first");
        assertEquals("first", mockedList.get(0));
        assertEquals("first", mockedList.get(0));

        when(mockedList.get(0)).thenReturn("second").thenReturn("third");//所有大于1次的调用都返回third
        assertEquals("second", mockedList.get(0));
        assertEquals("third", mockedList.get(0));
        assertEquals("third", mockedList.get(0));
    }

    @Test
    public void testMockAnyArg() {
        // 你不仅可以模拟接口,任何具体类都行
        List<String> mockedList = mock(LinkedList.class);
        when(mockedList.get(anyInt())).thenReturn("hello");
        assertEquals("hello", mockedList.get(0));
    }

    //如果使用了参数匹配器，那么所有的参数需要由匹配器来提供，否则将会报错。假如我们使用参数匹配器stubbing了mock对象的方法，那么在verify的时候也需要使用它。如下例
    //在最后的验证时如果只输入字符串”hello”是会报错的，必须使用Matchers类内建的eq方法。如果将anyInt()换成1进行验证也需要用eq(1)。 
    @Test
    public void argumentMatchersTest(){
        Map mapMock = mock(Map.class);
        when(mapMock.put(anyInt(), anyString())).thenReturn("world");
        mapMock.put(1, "hello");
        verify(mapMock).put(anyInt(), eq("hello"));//verify(mapMock).put(anyInt(), "hello");将报错
    }
    
    @Test
    public void testIsA() {
        when(strings.get(isA(Integer.class))).thenReturn("hello");
        assertEquals("hello", strings.get(0));
        verify(strings).get(0);
    }
}

//-----any()
package test;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockTest {

    @Test
    public void testForIOException() throws IOException {
        // Lets mock a LinkedList
        List<String> strings = mock(List.class);

        //这里用isA或者any都可以通过，个人感觉isA传入一个派生类对象也可以，而any只能是这个类对象
        when(strings.get(any(Integer.class))).thenReturn("hello");
        //when(strings.get(isA(Integer.class))).thenReturn("hello");
        assertEquals("hello", strings.get(any(Integer.class)));
    }
}
//------------------------------------------------------------------------------------------------
//技能鉴定3级 字母螺旋矩阵
http://blog.csdn.net/sunmenggmail/article/details/7779651

//AlphaSpiralMatrix.java
package test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AlphaSpiralMatrix {
    private final int maxRow;
    private final int maxCol;

    private static final char START_ALPHA = 'A';
    private static final int MAX_ALPHA_NUM = 26;

    public AlphaSpiralMatrix(int maxRow, int maxCol) {
        assert (maxRow > 0 && maxCol > 0);
        this.maxRow = maxRow;
        this.maxCol = maxCol;
    }

    String getRowAlpha(int rowIndex) {
        assert (rowIndex >= 0 && rowIndex < maxRow);

        return IntStream.range(0, maxCol).mapToObj(colIndex -> {
            long value = getValueByRowAndCol(rowIndex, colIndex);
            char charByValue = getCharByValue(value);
            System.out.println(String.format("[%d, %d] <--> %d <--> %c", rowIndex, colIndex, value, charByValue));
            return String.valueOf(charByValue);
        }).collect(Collectors.joining());

//        StringBuilder rowAlpha = new StringBuilder();
//        for (int colIndex = 0; colIndex < maxCol; ++colIndex) {
//            long value = getValueByRowAndCol(rowIndex, colIndex);
//            char charByValue = getCharByValue(value);
//            System.out.println(String.format("[%d, %d] <--> %d <--> %c", rowIndex, colIndex, value, charByValue));
//            rowAlpha.append(charByValue);
//        }
//        return rowAlpha.toString();
    }

    private long getValueByRowAndCol(int rowIndex, int colIndex) {
        int level = Math.min(Math.min(rowIndex, colIndex), Math.min(maxRow - 1 - rowIndex, maxCol - 1 - colIndex));
        int distance = rowIndex + colIndex - 2 * level;
        int startValue = getStartValue(level);

        //如果当前坐标位于该level矩形向右、向下的边，或者只有一条直线层时，坐标值为起始值+距离
        if (rowIndex == level || colIndex == maxCol - 1 - level || (maxCol < maxRow && level * 2 + 1 == maxCol)) {
            return startValue + distance;
        }

        //不在以上场景时，即当前坐标位于该level矩形向左、向上的边，则坐标值为下一level矩形起始值-距离
        int nextLevelStartValue = getStartValue(level + 1);
        return nextLevelStartValue - distance;
    }

    private int getStartValue(int level) {
        return 2 * level * (maxRow + maxCol - 2 * level) + 1;
    }

    private char getCharByValue(long value) {
        int remainder = (int) (value % MAX_ALPHA_NUM);
        return (char) (remainder - 1 + START_ALPHA);
    }
}

//AlphaSpiralMatrixTest.java
package test;

import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class AlphaSpiralMatrixTest {
    @Test
    public void testGetRowAlphaString_shouldGetAbc_whenRowIsOne() {
        AlphaSpiralMatrix alphaSpiralMatrix = new AlphaSpiralMatrix(1, 3);
        assertEquals("ABC", alphaSpiralMatrix.getRowAlpha(0));
    }

    @Test
    public void testCharString() {
        String s = Stream.of('a', 'b', 'c').map(String::valueOf).collect(Collectors.joining()).toString();//这里必须转为String，再做joining，不能直接用char做joining吗？
        System.out.println(s);
    }
}
//------------------------------------------------------------------------------------------------
//技能鉴定3级 石头分堆求最小差值
//StonesGrouper.java
package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StonesGrouper {

    public int getMinDiffValue(List<Integer> integers) {
        Collections.sort(integers);//Collections.sort(integers, (i1, i2) -> Integer.compare(i2, i1));
        int sum = integers.stream().mapToInt(x -> x).sum();

        Map<Integer, List<List<Integer>>> integerListMap = pickAllIntegers(integers);
        return getMinDiffValueFromAllIntegers(integerListMap, sum);
    }

    private Map<Integer, List<List<Integer>>> pickAllIntegers(List<Integer> integers) {
        Map<Integer, List<List<Integer>>> integerListMap = new HashMap<>();
        int size = integers.size();
        int maxCalculatedInteger = size % 2 == 0 ? size / 2 : (size - 1) / 2;

        for (int i = 1; i <= maxCalculatedInteger; ++i) {
            List<List<Integer>> groupedIntegers = pickIntegers(integers, i); //利用递归求出所有分组，可以完成任务，效率较低
            integerListMap.put(i, groupedIntegers);
        }
        return integerListMap;
    }

    private List<List<Integer>> pickIntegers(List<Integer> inputIntegers, int index) {
        if (index == 1) {
            return inputIntegers.stream().map(i -> Arrays.asList(i)).collect(Collectors.toList());
        }

        --index;
        List<List<Integer>> integersList = new ArrayList<>();
        List<Integer> copyOfInputIntegers = new ArrayList<>(inputIntegers);

        for (int i : inputIntegers) {
            copyOfInputIntegers.remove(0);//remove删除时是按索引删除
            List<List<Integer>> integersWithoutFirst = pickIntegers(copyOfInputIntegers, index);
            List<List<Integer>> integersCombinationWithFirst = integersWithoutFirst.stream().map(integers -> {
                List<Integer> copyOfIntegers = new ArrayList<>(integers);
                copyOfIntegers.add(i);
                return copyOfIntegers;
            }).collect(Collectors.toList());
            integersList.addAll(integersCombinationWithFirst);
        }
        return integersList;
    }

    private int getMinDiffValueFromAllIntegers(Map<Integer, List<List<Integer>>> integerListMap, Integer sum) {
        int minDiffValue = sum;
        for (Map.Entry<Integer, List<List<Integer>>> integerListEntry : integerListMap.entrySet()) {
            int tempMinDiffValue = integerListEntry.getValue().stream()
                .peek(integers -> System.out.print("One heap of stones: " + integers))
                .mapToInt(integers -> Math.abs(integers.stream().mapToInt(Integer::intValue).sum() * 2 - sum))
                .peek(i -> System.out.println(", weight difference: " + i))
                .min()
                .getAsInt();

            if (tempMinDiffValue < minDiffValue) {
                minDiffValue = tempMinDiffValue;
            }
        }
        return minDiffValue;
    }
}

//StonesGrouperTest.java
package test;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class StonesGrouperTest {
    @Test
    public void testGroupStones_multiStones() {
        int diffMinValue = new StonesGrouper().getMinDiffValue(Arrays.asList(5, 8, 13, 27, 14));
        assertEquals(3, diffMinValue);
    }

    @Test
    public void testGroupStones_multiStonesWithSameWeight() {
        int diffMinValue = new StonesGrouper().getMinDiffValue(Arrays.asList(5, 8, 10, 14, 10));
        assertEquals(1, diffMinValue);//按照排序后的简单叠加则得到差值为3
    }

    @Test
    public void testGroupStones_1Stones() {
        int diffMinValue = new StonesGrouper().getMinDiffValue(Arrays.asList(5));
        assertEquals(5, diffMinValue);
    }

    @Test
    public void testGroupStones_2Stones() {
        int diffMinValue = new StonesGrouper().getMinDiffValue(Arrays.asList(5, 12));
        assertEquals(7, diffMinValue);
    }
}
输出：（以一个测试用例为例）
One heap of stones: [5], weight difference: 57
One heap of stones: [8], weight difference: 51
One heap of stones: [13], weight difference: 41
One heap of stones: [14], weight difference: 39
One heap of stones: [27], weight difference: 13
One heap of stones: [8, 5], weight difference: 41
One heap of stones: [13, 5], weight difference: 31
One heap of stones: [14, 5], weight difference: 29
One heap of stones: [27, 5], weight difference: 3
One heap of stones: [13, 8], weight difference: 25
One heap of stones: [14, 8], weight difference: 23
One heap of stones: [27, 8], weight difference: 3
One heap of stones: [14, 13], weight difference: 13
One heap of stones: [27, 13], weight difference: 13
One heap of stones: [27, 14], weight difference: 15

//-----另在Stream发现如下问题，对流中的List对象做add操作会抛出异常，为什么？
package test;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ListAddInStreamTest {
    @Test
    public void testListAddInStream() {
        List<List<Integer>> integersList = Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(4, 5));
        integersList.stream().mapToInt(integers -> integers.size()).forEach(System.out::println); //这样也可以：i -> System.out.println(i)

        integersList.stream().mapToInt(integers -> {
            integers.add(1); //这里为什么add会抛出异常？
            return integers.size();
        }).forEach(i -> System.out.println(i));
    }
}
输出：
3
2

java.lang.UnsupportedOperationException
	at java.util.AbstractList.add(AbstractList.java:148)
	at java.util.AbstractList.add(AbstractList.java:108)
	at test.ListAddInStreamTest.lambda$testListAddInStream$2(ListAddInStreamTest.java:15)
	at java.util.stream.ReferencePipeline$4$1.accept(ReferencePipeline.java:210)
	at java.util.Spliterators$ArraySpliterator.forEachRemaining(Spliterators.java:948)
	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:481)
	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:471)
	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:151)
	at java.util.stream.ForEachOps$ForEachOp$OfInt.evaluateSequential(ForEachOps.java:189)
	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.util.stream.IntPipeline.forEach(IntPipeline.java:404)
	at test.ListAddInStreamTest.testListAddInStream(ListAddInStreamTest.java:17)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
	at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:51)
	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:237)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:147)


//------------------------------------------------------------------------------------------------
//命令行编译运行junit
1. 编译源文件：
javac -d classes src\com\huawei\demo\Hello.java
-d：指定生成的class文件目录

2. 将class文件打成jar包：
jar -cvf Hello.jar -C classes com
-c：生成新文件
-v：显示打包的详细信息
-f：指定jar包名字
-C：进入指定目录并将指定文件夹下的所有内容打包

3. 编译Test测试文件：
javac -d classes -classpath lib\junit.jar;src test\com\huawei\demo\HelloTest.java
-classpath：指定查找用户类文件和注释处理程序的位置，用;分隔指定多个查找位置，可以用-cp替换
-d：指定生成的class文件目录
说明：
--这里一定要指定src，其余src/com、src/com/huawei/demo等都不行，报错无法找到Hello，所以要按包com\huawei\demo来提供查找路径
--在当前lib目录下放置junit.jar包
--;号路径前后没有差异，如下也没有问题：javac -d classes -classpath src;lib\junit.jar test\com\huawei\demo\HelloTest.java
--还可以将-d参数与-cp参数互换位置：javac -classpath src;lib\junit.jar -d classes test\com\huawei\demo\HelloTest.java

4. 将Test.class文件打包：
jar -cvf HelloTest.jar -C classes com
说明：语句同2，所以第2步没有什么用，只是用来学习

5. 运行测试用例：
java -classpath lib\junit.jar;HelloTest.jar org.junit.runner.JUnitCore com.huawei.demo.HelloTest
-classpath：目录和 zip/jar 文件的类搜索路径，可以用-cp替换
org.junit.runner.JUnitCore [your junit test case class]
说明：运行的测试类要用完整的包和类名，com\huawei\demo\HelloTest
//------------------------------------------------------------------------------------------------
//命令行运行jar
http://lj830723.iteye.com/blog/1415766
http://blog.sina.com.cn/s/blog_98a617c70101jl91.html
1. 编译包含main函数的类：
javac Hello.java

2. 将class文件打成jar包：
jar -cvf Hello.jar Hello.class

3. 修改jar包中的文件MANIFEST.MF，添加“Main-Class: XXX”，如下第二行
Manifest-Version: 1.0
Main-Class: Hello
Created-By: 1.8.0_91 (Oracle Corporation)
说明：或者可以手工编辑MANIFEST.MF文件，在打jar包时将该文件包含进去，如下：jar -cvfm Hello.jar MANIFEST.MF Hello.class

4. 运行主函数：
java -jar Hello.jar
-jar：使用-jar参数后, 系统的Classpath变量不再起作用. 虚拟机将去MANIFEST.MF中的Class-Path下找相关的包。表示后面跟的jar包是有main class可独立运行，所以一定要在第3步添加Main-Class: XXX。
因为已经在MANIFEST.MF文件中指定了主函数，所以这里不用指定运行类Hello，当然也可以写成：java -jar Hello.jar Hello

5. 如果不采用3、4步骤，想要运行时动态指定：
java -classpath Hello.jar Hello
-classpath：可以用-cp替换

//-----
Manifest 技巧
总是以Manifest-Version属性开头
每行最长72个字符，如果超过的化，采用续行
确认每行都以回车结束，否则改行将会被忽略
如果Class-Path 中的存在路径，使用"/"分隔目录，与平台无关
使用空行分隔主属性和package属性
使用"/"而不是"."来分隔package 和class ,比如 com/example/myapp/
class 要以.class结尾，package 要以 / 结尾
//------------------------------------------------------------------------------------------------
//3n+1实现
//两个问题：
//1. 输入1怎么处理？
//2. 是否使用静态方法

出错的地方：
1. 下限1的处理方法，是否应该包括1
2. ThreeNPlusOneTransformer类的两个函数没有return返回值
3. ThreeNPlusOneTransformer类首次开发时没有使用静态方法，而测试类直接使用静态方法测试

//ThreeNPlusOneTransformer.java
package com.huawei.demo;

//import java.util.stream.Stream;

public class ThreeNPlusOneTransformer {
    private static final int MAX_INPUT = 1000000;
    private static final int MIN_INPUT = 0;
    private static final int INVALID_INPUT_RETURN_VALUE = -1;
    
    public static int getMaxStepsOf3NPlus1(int input1, int input2) {
        int minInput = Integer.min(input1, input2);
        int maxInput = Integer.max(input1, input2);
        
        if (checkInputInvalid(minInput, maxInput)) {
            return INVALID_INPUT_RETURN_VALUE;
        }
        
        return getMaxStepsOf3NPlus1OneByOne(minInput, maxInput);
    }
    
    private static boolean checkInputInvalid(int minInput, int maxInput) {
        return minInput <= MIN_INPUT || maxInput >= MAX_INPUT;
    }
    
    private static int getMaxStepsOf3NPlus1OneByOne(int minInput, int maxInput) {
        int maxSteps = 0;
        for (int i = minInput; i <= maxInput; ++i) {
            maxSteps = Integer.max(maxSteps, getStepsOf3NPlus1(i));
        }
        return maxSteps;
    }
    
    private static int getStepsOf3NPlus1(int input) {
        int steps = 1;
        while (input != 1) {
            ++steps;
            if (input % 2 == 0) {
                input /= 2;
            } else {
                input = 3 * input + 1;
            }
        }
        return steps;
        
        // int steps = (int) Stream.iterate(number, current -> {
            // if (current == 1) {
                // return 0;
            // }

            // if (current % 2 == 0) {
                // return current / 2;
            // }

            // return 3 * current + 1;
        // }).limit(100).filter(i -> i > 0).count();//这里实现得不好，没办法在得到1时终止操作，只能遍历到第100个元素结束

        // return steps - 1;
    }
}

//ThreeNPlusOneTransformerTest.java
package com.huawei.demo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ThreeNPlusOneTransformerTest {
    
    @Test
    public void test_getMaxSteps_differentValidInput() {
        assertEquals(8, ThreeNPlusOneTransformer.getMaxStepsOf3NPlus1(3, 5));
    }
    
    @Test
    public void test_getMaxSteps_sameValidInput() {
        assertEquals(8, ThreeNPlusOneTransformer.getMaxStepsOf3NPlus1(3, 3));
    }
    
    @Test
    public void test_getMaxSteps_inValidMinInput() {
        assertEquals(-1, ThreeNPlusOneTransformer.getMaxStepsOf3NPlus1(0, 3));
    }
    
    @Test
    public void test_getMaxSteps_inValidMaxInput() {
        assertEquals(-1, ThreeNPlusOneTransformer.getMaxStepsOf3NPlus1(5, 1000000));
    }
}
//------------------------------------------------------------------------------------------------
//Java8 Stream API
http://www.liaoxuefeng.com/article/001411309538536a1455df20d284b81a7bfa2f91db0f223000

Java 8 引入了全新的Stream API。这里的Stream和I/O流不同，它更像具有Iterable的集合类，但行为和集合类又有所不同。
Stream API引入的目的在于弥补Java函数式编程的缺陷。对于很多支持函数式编程的语言，map()、reduce()基本上都内置到语言的标准库中了，不过，Java 8的Stream API总体来讲仍然是非常完善和强大，足以用很少的代码完成许多复杂的功能。
创建一个Stream有很多方法，最简单的方法是把一个Collection变成Stream。我们来看最基本的几个操作：
//
package test;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamTest {
    @Test
    public void testStream() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Stream<Integer> integerStream = integers.stream();
        integerStream.filter(i -> i % 2 == 0)
            .map(i -> i * i)
            .limit(3) //取流中的前3个
            .forEach(System.out::println);
    }
}
输出：
4
16
36
//-----
为什么不在集合类实现这些操作，而是定义了全新的Stream API？Oracle官方给出了几个重要原因：
一是集合类持有的所有元素都是存储在内存中的，非常巨大的集合类会占用大量的内存，而Stream的元素却是在访问的时候才被计算出来，这种“延迟计算”的特性有点类似Clojure的lazy-seq，占用内存很少。
二是集合类的迭代逻辑是调用者负责，通常是for循环，而Stream的迭代是隐含在对Stream的各种操作中，例如map()。

要理解“延迟计算”，不妨创建一个无穷大小的Stream。
如果要表示自然数集合，显然用集合类是不可能实现的，因为自然数有无穷多个。但是Stream可以做到。
自然数集合的规则非常简单，每个元素都是前一个元素的值+1 ，因此，自然数发生器用代码实现如下：
//
package test;

import java.util.function.Supplier;

public class NaturalSupplier implements Supplier<Long> {
    private long l = 0;

    @Override
    public Long get() {
        return ++l;
    }
}

反复调用get()，将得到一个无穷数列，利用这个Supplier，可以创建一个无穷的Stream：
//
package test;

import org.junit.Test;

import java.util.stream.Stream;

public class StreamTest {
    @Test
    public void testNaturalSupplier() {
        Stream<Long> naturals = Stream.generate(new NaturalSupplier());
        naturals.map(l -> l * l)
            .limit(10)
            .forEach(System.out::println);
    }
}
输出：
1
4
9
16
25
36
49
64
81
100

对这个Stream做任何map()、filter()等操作都是完全可以的，这说明Stream API对Stream进行转换并生成一个新的Stream并非实时计算，而是做了延迟计算。
当然，对这个无穷的Stream不能直接调用forEach()，这样会无限打印下去。但是我们可以利用limit()变换，把这个无穷Stream变换为有限的Stream。
//-----
利用Stream API，可以设计更加简单的数据接口。例如，生成斐波那契数列，完全可以用一个无穷流表示（受限Java的long型大小，可以改为BigInteger）：
//
package test;

import java.util.function.Supplier;

public class FibonacciSupplier implements Supplier<Long> {
    private long a = 0;
    private long b = 1;

    @Override
    public Long get() {
        long x = a + b;
        a = b;
        b = x;
        return x;
    }
}

//
package test;

import org.junit.Test;

import java.util.stream.Stream;

public class StreamTest {
    @Test
    public void testFibonacciSupplier() {
        Stream<Long> fibonacci  = Stream.generate(new FibonacciSupplier());
        fibonacci.limit(10)
            .forEach(System.out::println);
    }
}
输出：
1
2
3
5
8
13
21
34
55
89

如果想取得数列的前10项，用limit(10)，如果想取得数列的第20~30 项，用：
List<Long> list = fibonacci.skip(20).limit(10).collect(Collectors.toList());
最后通过collect()方法把Stream变为List。该List存储的所有元素就已经是计算出的确定的元素了。
用Stream表示Fibonacci数列，其接口比任何其他接口定义都要来得简单灵活并且高效。

//-----
计算π可以利用π的展开式：
π/4 = 1 - 1/3 + 1/5 - 1/7 + 1/9 - ...
把π表示为一个无穷Stream如下：
//
package test;

import java.util.function.Supplier;

public class PiSupplier implements Supplier<Double> {
    private double sum = 0.0;
    private double current = 1.0;
    private boolean sign = true;

    @Override
    public Double get() {
        sum += (sign ? 4 : -4) / current;
        current += 2.0;
        sign = !sign;
        return sum;
    }
}

//
package test;

import org.junit.Test;

import java.util.stream.Stream;

public class StreamTest {
    @Test
    public void testPiSupplier() {
        Stream<Double> piStream  = Stream.generate(new PiSupplier());
        piStream.skip(10000).limit(10).forEach(System.out::println); //跳过前1000项，显示10个，可以把pi值确定在3.1416和3.1414之间
    }
}
输出：
3.1416926435905346
3.1414926735860353
3.1416926235985323
3.141492693574041
3.1416926036145227
3.141492713554056
3.141692583638501
3.141492733526085
3.1416925636704622
3.1414927534901333

这个级数从100项开始可以把π的值精确到3.13~3.15 之间：
3.1514934010709914
3.1317889675734545
3.1513011626954057
3.131977491197821
3.1511162471786824
3.1321589012071183
3.150938243930123
3.132333592767332
3.1507667724908344
3.1325019323081857
//-----
利用欧拉变换对级数进行加速，可以利用下面的公式：

用代码实现就是把一个流变成另一个流：
//
package test;

import java.util.function.Function;

public class EulerTransform implements Function<Double, Double> {
    private double n1 = 0.0;
    private double n2 = 0.0;
    private double n3 = 0.0;

    @Override
    public Double apply(Double t) {
        n1 = n2;
        n2 = n3;
        n3 = t;
        if (n1 == 0.0) {
            return 0.0;
        }
        return calc();
    }

    private Double calc() {
        double d = n3 - n2;
        return n3 - d * d / (n1 - 2 * n2 + n3);
    }
}

//
package test;

import org.junit.Test;

import java.util.stream.Stream;

public class StreamTest {
    @Test
    public void testPiSupplier() {
        Stream<Double> piStream  = Stream.generate(new PiSupplier());
        piStream.map(new EulerTransform()).skip(10).limit(10).forEach(System.out::println);
    }
}
输出：
3.1418396189294033
3.141406718496503
3.1417360992606667
3.1414796890042562
3.1416831892077566
3.1415189855952774
3.141653394197428
3.1415419859977844
3.14163535667939
3.141556330284574

可以在10项之内把π的值计算到3.141~3.142 之间：

0.0
0.0
3.166666666666667
3.1333333333333337
3.1452380952380956
3.13968253968254
3.1427128427128435
3.1408813408813416
3.142071817071818
3.1412548236077655

还可以多次应用这个加速器：
//
package test;

import org.junit.Test;

import java.util.stream.Stream;

public class StreamTest {
    @Test
    public void testPiSupplier() {
        Stream<Double> piStream  = Stream.generate(new PiSupplier());
        piStream.map(new EulerTransform())
            .map(new EulerTransform())
            .map(new EulerTransform())
            .map(new EulerTransform())
            .map(new EulerTransform())
            .limit(20)
            .forEach(System.out::println);
    }
}

20 项之内可以计算出极其精确的值：
...
3.14159265359053
3.1415926535894667
3.141592653589949
3.141592653589719

可见用Stream API可以写出多么简洁的代码，用其他的模型也可以写出来，但是代码会非常复杂。
//------------------------------------------------------------------------------------------------
//Java 8 中的 Streams API 详解
http://www.ibm.com/developerworks/cn/java/j-lo-java8streamapi/
Stream API 借助于同样新出现的 Lambda 表达式，极大的提高编程效率和程序可读性。同时它提供串行和并行两种模式进行汇聚操作，并发模式能够充分利用多核处理器的优势，使用 fork/join 并行方式来拆分任务和加速处理过程。通常编写并行代码很难而且容易出错, 但使用 Stream API 无需编写一行多线程的代码，就可以很方便地写出高性能的并发程序。所以说，Java 8 中首次出现的 java.util.stream 是一个函数式语言+多核时代综合影响的产物。
//清单 1. Java 7 的排序、取值实现

//而在 Java 8 使用 Stream，代码更加简洁易读；而且使用并发模式，程序执行速度更快。
//清单 2. Java 8 的排序、取值实现
//Cat.java
package test;

import java.awt.Color;

public class Cat {
    private final int id;
    private final int age;
    private final Color color;

    public Cat(int id, int age, Color color) {
        this.id = id;
        this.age = age;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public Color getColor() {
        return color;
    }
}

//StreamTest.java
package test;

import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static org.junit.Assert.assertEquals;

public class StreamTest {

    private static List<Cat> cats;
    private static Cat cat2;
    private static Cat cat3;
    private static Cat cat1;
    private static Cat cat4;

    @BeforeClass
    public static void before() {
        cat1 = new Cat(1, 10, Color.WHITE);
        cat2 = new Cat(2, 20, Color.BLACK);
        cat3 = new Cat(3, 30, Color.BLUE);
        cat4 = new Cat(4, 40, Color.WHITE);
        cats = Arrays.asList(cat2, cat3, cat1, cat4);
    }
    
    //在该BeforeClass中初始化的static变量，可以在static块中初始化
    // static {
        // cat1 = new Cat(1, 10, Color.WHITE);
        // cat2 = new Cat(2, 20, Color.BLACK);
        // cat3 = new Cat(3, 30, Color.BLUE);
        // cat4 = new Cat(4, 40, Color.WHITE);
        // cats = Arrays.asList(cat2, cat3, cat1, cat4);
    // }

    @Test
    public void testSort() {
        List<Cat> someCats = new ArrayList<>();

        for (Cat cat : cats) {
            if (cat.getColor() == Color.WHITE) {
                someCats.add(cat);
            }
        }

        Collections.sort(someCats, new Comparator<Cat>() {
            @Override
            public int compare(Cat cat1, Cat cat2) {
                return Integer.compare(cat2.getAge(), cat1.getAge());
            }
        });

        List<Integer> catsId = new ArrayList<>();
        for (Cat cat : someCats) {
            catsId.add(cat.getId());
        }

        assertResult(catsId);
    }

    @Test
    public void testSortUsingStream() {
        List<Integer> catsId = cats.stream() //cats.parallelStream()
            .filter(cat -> cat.getColor() == Color.WHITE)
            .sorted(comparing(Cat::getAge).reversed())
            .map(Cat::getId)
            .collect(Collectors.toList());

        assertResult(catsId);
    }

    private void assertResult(List<Integer> catsId) {
        assertEquals(2, catsId.size());
        assertEquals(Optional.of(4), Optional.ofNullable(catsId.get(0)));
        assertEquals(Optional.of(1), Optional.ofNullable(catsId.get(1)));
    }
}

Stream 总览
--什么是流
Stream 不是集合元素，它不是数据结构并不保存数据，它是有关算法和计算的，它更像一个高级版本的 Iterator。原始版本的 Iterator，用户只能显式地一个一个遍历元素并对其执行某些操作；高级版本的 Stream，用户只要给出需要对其包含的元素执行什么操作，比如 “过滤掉长度大于 10 的字符串”、“获取每个字符串的首字母”等，Stream 会隐式地在内部进行遍历，做出相应的数据转换。
Stream 就如同一个迭代器（Iterator），单向，不可往复，数据只能遍历一次，遍历过一次后即用尽了，就好比流水从面前流过，一去不复返。
而和迭代器又不同的是，Stream 可以并行化操作，迭代器只能命令式地、串行化操作。顾名思义，当使用串行方式去遍历时，每个 item 读完后再读下一个 item。而使用并行去遍历时，数据会被分成多个段，其中每一个都在不同的线程中处理，然后将结果一起输出。Stream 的并行操作依赖于 Java7 中引入的 Fork/Join 框架（JSR166y）来拆分任务和加速处理过程。Java 的并行 API 演变历程基本如下：
1. 1.0-1.4 中的 java.lang.Thread
2. 5.0 中的 java.util.concurrent
3. 6.0 中的 Phasers 等
4. 7.0 中的 Fork/Join 框架
5. 8.0 中的 Lambda
Stream 的另外一大特点是，数据源本身可以是无限的。

--流的构成
当我们使用一个流的时候，通常包括三个基本步骤：
获取一个数据源（source）→ 数据转换→执行操作获取想要的结果，每次转换原有 Stream 对象不改变，返回一个新的 Stream 对象（可以有多次转换），这就允许对其操作可以像链条一样排列，变成一个管道，如下图所示。

有多种方式生成 Stream Source：
--从 Collection 和数组
    Collection.stream()
    Collection.parallelStream()
    Arrays.stream(T array) or Stream.of()
--从 BufferedReader
    java.io.BufferedReader.lines()
--静态工厂
    java.util.stream.IntStream.range()
    java.nio.file.Files.walk()
--自己构建
    java.util.Spliterator
--其它
    Random.ints()
    BitSet.stream()
    Pattern.splitAsStream(java.lang.CharSequence)
    JarFile.stream()

流的操作类型分为两种：
Intermediate：一个流可以后面跟随零个或多个 intermediate 操作。其目的主要是打开流，做出某种程度的数据映射/过滤，然后返回一个新的流，交给下一个操作使用。这类操作都是惰性化的（lazy），就是说，仅仅调用到这类方法，并没有真正开始流的遍历。
Terminal：一个流只能有一个 terminal 操作，当这个操作执行后，流就被使用“光”了，无法再被操作。所以这必定是流的最后一个操作。Terminal 操作的执行，才会真正开始流的遍历，并且会生成一个结果，或者一个 side effect。

在对于一个 Stream 进行多次转换操作 (Intermediate 操作)，每次都对 Stream 的每个元素进行转换，而且是执行多次，这样时间复杂度就是 N（转换次数）个 for 循环里把所有操作都做掉的总和吗？其实不是这样的，转换操作都是 lazy 的，多个转换操作只会在 Terminal 操作的时候融合起来，一次循环完成。我们可以这样简单的理解，Stream 里有个操作函数的集合，每次转换操作就是把转换函数放入这个集合中，在 Terminal 操作的时候循环 Stream 对应的集合，然后对每个元素执行所有的函数。

还有一种操作被称为 short-circuiting。用以指：
    对于一个 intermediate 操作，如果它接受的是一个无限大（infinite/unbounded）的 Stream，但返回一个有限的新 Stream。
    对于一个 terminal 操作，如果它接受的是一个无限大的 Stream，但能在有限的时间计算出结果。
当操作一个无限大的 Stream，而又希望在有限时间内完成操作，则在管道内拥有一个 short-circuiting 操作是必要非充分条件。

清单 3. 一个流操作的示例
int sum = widgets.stream()
    .filter(w -> w.getColor() == RED)
    .mapToInt(w -> w.getWeight())
    .sum();
stream() 获取当前小物件的 source，filter 和 mapToInt 为 intermediate 操作，进行数据筛选和转换，最后一个 sum() 为 terminal 操作，对符合条件的全部小物件作重量求和。


--流的使用详解
简单说，对 Stream 的使用就是实现一个 filter-map-reduce 过程，产生一个最终结果，或者导致一个副作用（side effect）。

--流的构造与转换
下面提供最常见的几种构造 Stream 的样例。
清单 4. 构造流的几种常见方法
// 1. Individual values
Stream stream = Stream.of("a", "b", "c");
// 2. Arrays
String [] strArray = new String[] {"a", "b", "c"};
stream = Stream.of(strArray);
stream = Arrays.stream(strArray);
// 3. Collections
List<String> list = Arrays.asList(strArray);
stream = list.stream();

需要注意的是，对于基本数值型，目前有三种对应的包装类型 Stream：
IntStream、LongStream、DoubleStream。当然我们也可以用 Stream<Integer>、Stream<Long> >、Stream<Double>，但是 boxing 和 unboxing 会很耗时，所以特别为这三种基本数值型提供了对应的 Stream。
Java 8 中还没有提供其它数值型 Stream，因为这将导致扩增的内容较多。而常规的数值型聚合运算可以通过上面三种 Stream 进行。

清单 5. 数值流的构造
IntStream.of(new int[]{1, 2, 3}).forEach(System.out::println);
IntStream.range(1, 3).forEach(System.out::println); //[1, 3)
IntStream.rangeClosed(1, 3).forEach(System.out::println); //[1, 3]


清单 6. 流转换为其它数据结构
// 1. Array
String[] strArray1 = stream.toArray(String[]::new);
// Stream<String> stringStream = Stream.of("a", "b", "c");
// String[] strings = stringStream.toArray(String[]::new);
// for (String s : strings) { //查看数组内容需要手工遍历，如果用System.out.println(strings)无法打出数组元素
    // System.out.println(s);
// }

// 2. Collection
List<String> list1 = stream.collect(Collectors.toList()); //实现是ArrayList::new
List<String> list2 = stream.collect(Collectors.toCollection(ArrayList::new)); //List<String> list2 = stringStream.collect(Collectors.toCollection(LinkedList::new));//这里可以指定生成Collection的类型，也可以用LinkedList等

Set set1 = stream.collect(Collectors.toSet()); //HashSet::new
Stack stack1 = stream.collect(Collectors.toCollection(Stack::new));
// 3. String
String str = stream.collect(Collectors.joining()).toString();

一个 Stream 只可以使用一次，上面的代码为了简洁而重复使用了数次。

//测试如下
package test;

import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {

    @Test
    public void testToCollection() {
        Stream<String> stringStream = Stream.of("b", "a", "c");
        Set set1 = stringStream.collect(Collectors.toCollection(TreeSet::new));
        System.out.println(set1); //[a, b, c]
    }

    @Test
    public void testToString_emptyDelimiter() {
        Stream<String> stringStream = Stream.of("b", "a", "c");
        String str = stringStream.collect(Collectors.joining()).toString();
        System.out.println(str); //bac
    }

    @Test
    public void testToString_commaDelimiter() {
        Stream<String> stringStream = Stream.of("b", "a", "c");
        String str = stringStream.collect(Collectors.joining(", ")).toString();
        System.out.println(str); //b, a, c
    }

    @Test
    public void testToString_prefixsufixDelimiter() {
        Stream<String> stringStream = Stream.of("b", "a", "c");
        String str = stringStream.collect(Collectors.joining(", ", ":", ";")).toString();
        System.out.println(str); //:b, a, c;
    }
}

--流的操作
接下来，当把一个数据结构包装成 Stream 后，就要开始对里面的元素进行各类操作了。常见的操作可以归类如下。
Intermediate：
map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、 limit、 skip、 parallel、 sequential、 unordered
Terminal：
forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、 count、 anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 iterator
Short-circuiting：
anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 limit

我们下面看一下 Stream 的比较典型用法。
--map/flatMap
我们先来看 map。如果你熟悉 scala 这类函数式语言，对这个方法应该很了解，它的作用就是把 input Stream 的每一个元素，映射成 output Stream 的另外一个元素。
清单 7. 转换大写
List<String> output = wordList.stream().
    map(String::toUpperCase).
    collect(Collectors.toList());
这段代码把所有的单词转换为大写。

清单 8. 平方数
List<Integer> nums = Arrays.asList(1, 2, 3, 4);
List<Integer> squareNums = nums.stream().
map(n -> n * n).
collect(Collectors.toList());
这段代码生成一个整数 list 的平方数 {1, 4, 9, 16}。

从上面例子可以看出，map 生成的是个 1:1 映射，每个输入元素，都按照规则转换成为另外一个元素。还有一些场景，是一对多映射关系的，这时需要 flatMap。

清单 9. 一对多
Stream<List<Integer>> inputStream = Stream.of(
 Arrays.asList(1),
 Arrays.asList(2, 3),
 Arrays.asList(4, 5, 6)
 );
Stream<Integer> outputStream = inputStream.
flatMap((childList) -> childList.stream());

flatMap 把 input Stream 中的层级结构扁平化，就是将最底层元素抽出来放到一起，最终 output 的新 Stream 里面已经没有 List 了，都是直接的数字。
//样例
package test;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {

    @Test
    public void testToCollection() {
        Stream<List<String>> listStream = Stream.of(Arrays.asList("a"), Arrays.asList("b", "c"), Arrays.asList("d", "e", "f"));
        List<String> allLists = listStream.flatMap(lists -> lists.stream()).collect(Collectors.toList());
        System.out.println(allLists); //[a, b, c, d, e, f]
    }
}

--filter
filter 对原始 Stream 进行某项测试，通过测试的元素被留下来生成一个新 Stream。
清单 10. 留下偶数
Integer[] sixNums = {1, 2, 3, 4, 5, 6};
Integer[] evens = Stream.of(sixNums).filter(n -> n%2 == 0).toArray(Integer[]::new);
经过条件“被 2 整除”的 filter，剩下的数字为 {2, 4, 6}。

清单 11. 把单词挑出来
List<String> output = reader.lines().
 flatMap(line -> Stream.of(line.split(REGEXP))).
 filter(word -> word.length() > 0).
 collect(Collectors.toList());
这段代码首先把每行的单词用 flatMap 整理到新的 Stream，然后保留长度不为 0 的，就是整篇文章中的全部单词了。

--forEach
forEach 方法接收一个 Lambda 表达式，然后在 Stream 的每一个元素上执行该表达式。
清单 12. 打印姓名（forEach 和 pre-java8 的对比）
// Java 8
roster.stream()
 .filter(p -> p.getGender() == Person.Sex.MALE)
 .forEach(p -> System.out.println(p.getName()));
// Pre-Java 8
for (Person p : roster) {
 if (p.getGender() == Person.Sex.MALE) {
 System.out.println(p.getName());
 }
}
对一个人员集合遍历，找出男性并打印姓名。可以看出来，forEach 是为 Lambda 而设计的，保持了最紧凑的风格。而且 Lambda 表达式本身是可以重用的，非常方便。当需要为多核系统优化时，可以 parallelStream().forEach()，只是此时原有元素的次序没法保证，并行的情况下将改变串行时操作的行为，此时 forEach 本身的实现不需要调整，而 Java8 以前的 for 循环 code 可能需要加入额外的多线程逻辑。
但一般认为，forEach 和常规 for 循环的差异不涉及到性能，它们仅仅是函数式风格与传统 Java 风格的差别。

另外一点需要注意，forEach 是 terminal 操作，因此它执行后，Stream 的元素就被“消费”掉了，你无法对一个 Stream 进行两次 terminal 运算。下面的代码是错误的：
     stream.forEach(element -> doOneThing(element));
     stream.forEach(element -> doAnotherThing(element));
清单 13. peek 对每个元素执行操作并返回一个新的 Stream
Stream.of("one", "two", "three", "four")
 .filter(e -> e.length() > 3)
 .peek(e -> System.out.println("Filtered value: " + e))
 .map(String::toUpperCase)
 .peek(e -> System.out.println("Mapped value: " + e))
 .collect(Collectors.toList());
forEach 不能修改自己包含的本地变量值，也不能用 break/return 之类的关键字提前结束循环。

--findFirst
这是一个 termimal 兼 short-circuiting 操作，它总是返回 Stream 的第一个元素，或者空。
这里比较重点的是它的返回值类型：Optional。这也是一个模仿 Scala 语言中的概念，作为一个容器，它可能含有某值，或者不包含。使用它的目的是尽可能避免 NullPointerException。
清单 14. Optional 的两个用例
String strA = " abcd ", strB = null;
print(strA);
print("");
print(strB);
getLength(strA);
getLength("");
getLength(strB);
public static void print(String text) {
    // Java 8
    Optional.ofNullable(text).ifPresent(System.out::println);
    // Pre-Java 8
    if (text != null) {
        System.out.println(text);
    }
}
public static int getLength(String text) {
    // Java 8
    return Optional.ofNullable(text).map(String::length).orElse(-1);
    // Pre-Java 8
    return (text != null) ? text.length() : -1;
};

在更复杂的 if (xx != null) 的情况中，使用 Optional 代码的可读性更好，而且它提供的是编译时检查，能极大的降低 NPE 这种 Runtime Exception 对程序的影响，或者迫使程序员更早的在编码阶段处理空值问题，而不是留到运行时再发现和调试。
Stream 中的 findAny、max/min、reduce 等方法等返回 Optional 值。还有例如 IntStream.average() 返回 OptionalDouble 等等。

--reduce
这个方法的主要作用是把 Stream 元素组合起来。它提供一个起始值（种子），然后依照运算规则（BinaryOperator），和前面 Stream 的第一个、第二个、第 n 个元素组合。从这个意义上说，字符串拼接、数值的 sum、min、max、average 都是特殊的 reduce。例如 Stream 的 sum 就相当于
Integer sum = integers.reduce(0, (a, b) -> a+b);
或
Integer sum = integers.reduce(0, Integer::sum);
也有没有起始值的情况，这时会把 Stream 的前面两个元素组合起来，返回的是 Optional。
清单 15. reduce 的用例
// 字符串连接，concat = "ABCD"
String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat); 
// 求最小值，minValue = -3.0
double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min); 
// 求和，sumValue = 10, 有起始值
int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
// 求和，sumValue = 10, 无起始值
sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
// 过滤，字符串连接，concat = "ace"
concat = Stream.of("a", "B", "c", "D", "e", "F").
 filter(x -> x.compareTo("Z") > 0).
 reduce("", String::concat);
上面代码例如第一个示例的 reduce()，第一个参数（空白字符）即为起始值，第二个参数（String::concat）为 BinaryOperator。这类有起始值的 reduce() 都返回具体的对象。而对于第四个示例没有起始值的 reduce()，由于可能没有足够的元素，返回的是 Optional，请留意这个区别。

--limit/skip
limit 返回 Stream 的前面 n 个元素；skip 则是扔掉前 n 个元素（它是由一个叫 subStream 的方法改名而来）。
清单 16. limit 和 skip 对运行次数的影响
package test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {

    private class Person {
        public int no;
        private String name;

        public Person(int no, String name) {
            this.no = no;
            this.name = name;
        }

        public String getName() {
            System.out.println(name);
            return name;
        }
    }

    @Test
    public void testLimitAndSkip() {
        List<Person> persons = new ArrayList();
        for (int i = 1; i <= 10000; i++) {
            Person person = new Person(i, "name" + i);
            persons.add(person);
        }

        List<String> personList2 = persons.stream().
            map(Person::getName).limit(10).skip(3).collect(Collectors.toList());
        System.out.println(personList2);
    }
}
输出：
name1
name2
name3
name4
name5
name6
name7
name8
name9
name10
[name4, name5, name6, name7, name8, name9, name10]

这是一个有 10，000 个元素的 Stream，但在 short-circuiting 操作 limit 和 skip 的作用下，管道中 map 操作指定的 getName() 方法的执行次数为 limit 所限定的 10 次，而最终返回结果在跳过前 3 个元素后只有后面 7 个返回。

有一种情况是 limit/skip 无法达到 short-circuiting 目的的，就是把它们放在 Stream 的排序操作后，原因跟 sorted 这个 intermediate 操作有关：此时系统并不知道 Stream 排序后的次序如何，所以 sorted 中的操作看上去就像完全没有被 limit 或者 skip 一样。
清单 17. limit 和 skip 对 sorted 后的运行次数无影响
@Test
public void testLimitAfterSorted() {
    List<Person> persons = new ArrayList();
    for (int i = 1; i <= 5; i++) {
        Person person = new Person(i, "name" + i);
        persons.add(person);
    }
    
    List<String> personList2 = persons.stream().sorted((p1, p2) ->
            p1.getName().compareTo(p2.getName())).limit(2).map(Person::getName).collect(Collectors.toList());
        System.out.println(personList2);
}
输出：
name2
name1
name3
name2
name4
name3
name5
name4
name1 --这两个是map(Person::getName)的输出
name2
[name1, name2]

上面的示例对清单 13 做了微调，首先对 5 个元素的 Stream 排序，然后进行 limit 操作。输出结果如上。

最后有一点需要注意的是，对一个 parallel 的 Steam 管道来说，如果其元素是有序的，那么 limit 操作的成本会比较大，因为它的返回对象必须是前 n 个也有一样次序的元素。取而代之的策略是取消元素间的次序，或者不要用 parallel Stream。

--sorted
对 Stream 的排序通过 sorted 进行，它比数组的排序更强之处在于你可以首先对 Stream 进行各类 map、filter、limit、skip 甚至 distinct 来减少元素数量后，再排序，这能帮助程序明显缩短执行时间。我们对清单 14 进行优化：
清单 18. 优化：排序前进行 limit 和 skip
//另外，试验先limit再sorted，可以看到只对两个元素进行了排序
@Test
public void testLimitBeforeSorted() {
    List<Person> persons = new ArrayList();
    for (int i = 1; i <= 5; i++) {
        Person person = new Person(i, "name" + i);
        persons.add(person);
    }

    List<String> personList2 = persons.stream().limit(2).sorted((p1, p2) ->
        p1.getName().compareTo(p2.getName())).map(Person::getName).collect(Collectors.toList());
    System.out.println(personList2);
}
输出
name2
name1
name1
name2
[name1, name2]
当然，这种优化是有 business logic 上的局限性的：即不要求排序后再取值。

--min/max/distinct
min 和 max 的功能也可以通过对 Stream 元素先排序，再 findFirst 来实现，但前者的性能会更好，为 O(n)，而 sorted 的成本是 O(n log n)。同时它们作为特殊的 reduce 方法被独立出来也是因为求最大最小值是很常见的操作。
清单 19. 找出最长一行的长度
//abc.txt
day
hello
nice

//
package test;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;

public class StreamTest {

    @Test
    public void testLimitAndSkip() {
        try {
            FileReader fr = new FileReader("src/main/java/test/abc.txt");
            BufferedReader br = new BufferedReader(fr);

            int longest = br.lines().
                mapToInt(String::length).
                max().
                getAsInt();

            br.close();
            fr.close();
            System.out.println(longest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
输出：
5

下面的例子则使用 distinct 来找出不重复的单词。
清单 20. 找出全文的单词，转小写，并排序
//abc.txt
hello what a nice day
DAY day up GOOD good study
HELLO

//
package test;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {

    @Test
    public void testLimitAndSkip() {
        try {
            FileReader fr = new FileReader("src/main/java/test/abc.txt"); //这里如果路径为main/java/test/abc.txt，则找不到相应文件
            BufferedReader br = new BufferedReader(fr);

            List<String> words = br.lines().
                flatMap(line -> Stream.of(line.split(" "))).
                filter(word -> word.length() > 0).
                map(String::toLowerCase).
                distinct().
                sorted().
                collect(Collectors.toList());

            br.close();
            fr.close();
            System.out.println(words);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
输出：
[a, day, good, hello, nice, study, up, what]


--Match
Stream 有三个 match 方法，从语义上说：
allMatch：Stream 中全部元素符合传入的 predicate，返回 true
anyMatch：Stream 中只要有一个元素符合传入的 predicate，返回 true
noneMatch：Stream 中没有一个元素符合传入的 predicate，返回 true
它们都不是要遍历全部元素才能返回结果。例如 allMatch 只要一个元素不满足条件，就 skip 剩下的所有元素，返回 false。对清单 13 中的 Person 类稍做修改，加入一个 age 属性和 getAge 方法。
清单 21. 使用 Match
List<Person> persons = new ArrayList();
persons.add(new Person(1, "name" + 1, 10));
persons.add(new Person(2, "name" + 2, 21));
persons.add(new Person(3, "name" + 3, 34));
persons.add(new Person(4, "name" + 4, 6));
persons.add(new Person(5, "name" + 5, 55));
boolean isAllAdult = persons.stream().
 allMatch(p -> p.getAge() > 18);
System.out.println("All are adult? " + isAllAdult);
boolean isThereAnyChild = persons.stream().
 anyMatch(p -> p.getAge() < 12);
System.out.println("Any child? " + isThereAnyChild);
输出结果：
 All are adult? false
 Any child? true

//-----
进阶：自己生成流
--Stream.generate
通过实现 Supplier 接口，你可以自己来控制流的生成。这种情形通常用于随机数、常量的 Stream，或者需要前后元素间维持着某种状态信息的 Stream。把 Supplier 实例传递给 Stream.generate() 生成的 Stream，默认是串行（相对 parallel 而言）但无序的（相对 ordered 而言）。由于它是无限的，在管道中，必须利用 limit 之类的操作限制 Stream 大小。
清单 22. 生成 10 个随机整数
package test;

import org.junit.Test;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {

    @Test
    public void testRandom() {
        Random seed = new Random();
        Supplier<Integer> random = seed::nextInt;
        Stream.generate(random).limit(10).forEach(System.out::println);

        //Another way
        IntStream.generate(() -> (int) (System.nanoTime() % 100)).
            limit(10).forEach(System.out::println);
    }
}
输出：
943966293
2055700156
-844810453
817068663
444598564
-624756530
-260647001
-613986940
-1579427386
154983337
87
44
97
50
92
34
21
7
38
70

Stream.generate() 还接受自己实现的 Supplier。例如在构造海量测试数据的时候，用某种自动的规则给每一个变量赋值；或者依据公式计算 Stream 的每个元素值。这些都是维持状态信息的情形。
清单 23. 自实现 Supplier
//
package test;

import org.junit.Test;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class StreamTest {
    @Test
    public void testRandom() {
        Stream.generate(new PersonSupplier()).
            limit(10).
            forEach(p -> System.out.println(p.getName() + ", " + p.getAge()));
    }

    private class PersonSupplier implements Supplier<Person> {
        private int index = 0;
        private Random random = new Random();

        @Override
        public Person get() {
            return new Person(index++, "StormTestUser" + index, random.nextInt(100));
        }
    }

    private class Person {
        public int no;
        private String name;
        private int age;

        public Person(int no, String name, int age) {
            this.no = no;
            this.name = name;
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public String getName() {
            return name;
        }
    }
}

--Stream.iterate
iterate 跟 reduce 操作很像，接受一个种子值，和一个 UnaryOperator（例如 f）。然后种子值成为 Stream 的第一个元素，f(seed) 为第二个，f(f(seed)) 第三个，以此类推。
清单 24. 生成一个等差数列
package test;

import org.junit.Test;

import java.util.stream.Stream;

public class StreamTest {
    @Test
    public void testRandom() {
        Stream.iterate(0, n -> n + 3).limit(10). forEach(x -> System.out.print(x + " "));
    }
}
输出：
0 3 6 9 12 15 18 21 24 27
与 Stream.generate 相仿，在 iterate 时候管道必须有 limit 这样的操作来限制 Stream 大小。

//-----
进阶：用 Collectors 来进行 reduction 操作
java.util.stream.Collectors 类的主要作用就是辅助进行各类有用的 reduction 操作，例如转变输出为 Collection，把 Stream 元素进行归组。
groupingBy/partitioningBy
清单 25. 按照年龄归组
@Test
public void testRandom() {
    Stream<Person> personStream = Stream.generate(new PersonSupplier());
    Map<Integer, List<Person>> personGroups = personStream.limit(100).collect(Collectors.groupingBy(Person::getAge));
    Iterator<Map.Entry<Integer, List<Person>>> iterator = personGroups.entrySet().iterator();
    while (iterator.hasNext()) {
        Map.Entry<Integer, List<Person>> persons = iterator.next();
        System.out.println("Age " + persons.getKey() + " = " + persons.getValue().size());
    }
}
输出：
Age 0 = 3
Age 2 = 1
Age 3 = 1
Age 4 = 4
Age 5 = 1
Age 13 = 1
Age 15 = 1
Age 17 = 3
Age 18 = 1
Age 19 = 1
Age 20 = 4
Age 22 = 1
Age 23 = 3
Age 24 = 1
Age 25 = 1
Age 26 = 1
Age 28 = 1
Age 29 = 1
Age 36 = 5
Age 37 = 1
Age 39 = 1
Age 40 = 3
Age 42 = 1
Age 44 = 1
Age 45 = 1
Age 47 = 1
Age 48 = 2
Age 50 = 1
Age 51 = 1
Age 52 = 1
Age 55 = 1
Age 56 = 1
Age 57 = 3
Age 58 = 1
Age 59 = 1
Age 61 = 2
Age 62 = 2
Age 63 = 2
Age 65 = 1
Age 66 = 2
Age 70 = 1
Age 71 = 1
Age 72 = 1
Age 75 = 1
Age 76 = 1
Age 77 = 1
Age 78 = 2
Age 80 = 1
Age 81 = 2
Age 83 = 1
Age 87 = 1
Age 88 = 3
Age 89 = 4
Age 90 = 1
Age 91 = 1
Age 93 = 1
Age 94 = 2
Age 96 = 4
Age 97 = 1
Age 98 = 3
Age 99 = 2

清单 26. 按照未成年人和成年人归组
@Test
public void testRandom() {
    Map<Boolean, List<Person>> children = Stream.generate(new PersonSupplier()).
        limit(100).
        collect(Collectors.partitioningBy(p -> p.getAge() < 18));
    System.out.println("Children number: " + children.get(true).size());
    System.out.println("Adult number: " + children.get(false).size());
}
输出：
Children number: 13
Adult number: 87
在使用条件“年龄小于18”进行分组后可以看到，不到 18 岁的未成年人是一组，成年人是另外一组。partitioningBy 其实是一种特殊的 groupingBy，它依照条件测试的是否两种结果来构造返回的数据结构，get(true) 和 get(false) 能即为全部的元素对象。

//-----
结束语
总之，Stream 的特性可以归纳为：
不是数据结构
它没有内部存储，它只是用操作管道从 source（数据结构、数组、generator function、IO channel）抓取数据。
它也绝不修改自己所封装的底层数据结构的数据。例如 Stream 的 filter 操作会产生一个不包含被过滤元素的新 Stream，而不是从 source 删除那些元素。
所有 Stream 的操作必须以 lambda 表达式为参数
不支持索引访问
你可以请求第一个元素，但无法请求第二个，第三个，或最后一个。不过请参阅下一项。
很容易生成数组或者 List
惰性化
很多 Stream 操作是向后延迟的，一直到它弄清楚了最后需要多少数据才会开始。
Intermediate 操作永远是惰性化的。
并行能力
当一个 Stream 是并行化的，就不需要再写多线程代码，所有对它的操作会自动并行进行的。
可以是无限的
集合有固定大小，Stream 则不必。limit(n) 和 findFirst() 这类的 short-circuiting 操作可以对无限的 Stream 进行运算并很快完成。
//------------------------------------------------------------------------------------------------
//Java Stream 使用详解
http://www.codeceo.com/article/java-stream-usage.html
本节翻译整理自 Javadoc ，并对流的这些特性做了进一步的解释。

//-----
Stream接口还包含几个基本类型的子接口如IntStream, LongStream 和 DoubleStream。

关于流和其它集合具体的区别，可以参照下面的列表：
--不存储数据 。流是基于数据源的对象，它本身不存储数据元素，而是通过管道将数据源的元素传递给操作。
--函数式编程 。流的操作不会修改数据源，例如filter不会将数据源中的数据删除。
--延迟操作 。流的很多操作如filter,map等中间操作是延迟执行的，只有到终点操作才会将操作顺序执行。
--可以解绑 。对于无限数量的流，有些操作是可以在有限的时间完成的，比如limit(n)或findFirst()，这些操作可是实现”短路”(Short-circuiting)，访问到有限的元素后就可以返回。
--纯消费 。流的元素只能访问一次，类似Iterator，操作没有回头路，如果你想从头重新访问流的元素，对不起，你得重新生成一个新的流。
流的操作是以管道的方式串起来的。流管道包含一个数据源，接着包含零到N个中间操作，最后以一个终点操作结束。
//-----
并行 Parallelism
所有的流操作都可以串行执行或者并行执行。除非显示地创建并行流，否则Java库中创建的都是串行流。Collection.stream()为集合创建串行流而Collection.parallelStream()为集合创建并行流。IntStream.range(int, int)创建的是串行流。通过parallel()方法可以将串行流转换成并行流,sequential()方法将流转换成并行流。
除非方法的Javadoc中指明了方法在并行执行的时候结果是不确定(比如findAny、forEach)，否则串行和并行执行的结果应该是一样的。
//-----
不干涉 Non-interference
流可以从非线程安全的集合中创建，当流的管道执行的时候，非concurrent数据源不应该被改变。下面的代码会抛出java.util.ConcurrentModificationException异常：
List<String> l = new ArrayList(Arrays.asList("one", "two"));
Stream<String> sl = l.stream();
sl.forEach(s -> l.add("three"));

在设置中间操作的时候，可以更改数据源，只有在执行终点操作的时候，才有可能出现并发问题(抛出异常，或者不期望的结果)，比如下面的代码不会抛出异常：
List<String> l = new ArrayList(Arrays.asList("one", "two"));
Stream<String> sl = l.stream();
l.add("three");sl.forEach(System.out::println);

对于concurrent数据源，不会有这样的问题，比如下面的代码很正常：
List<String> l = new CopyOnWriteArrayList<>(Arrays.asList("one", "two"));
Stream<String> sl = l.stream();
sl.forEach(s -> l.add("three"));
虽然我们上面例子是在终点操作中对非并发数据源进行修改，但是非并发数据源也可能在其它线程中修改，同样会有并发问题。
//-----
无状态 Stateless behaviors
大部分流的操作的参数都是函数式接口，可以使用Lambda表达式实现。它们用来描述用户的行为，称之为行为参数(behavioral parameters)。
如果这些行为参数有状态，则流的操作的结果可能是不确定的，比如下面的代码:
List<String> l = new ArrayList(Arrays.asList("one", "two", ……));
class State {
    boolean s;
}
final State state = new State();
Stream<String> sl = l.stream().map(e -> {
    if (state.s)
        return "OK";    
    else {
        state.s = true;
        return e;
    } 
    });
sl.forEach(System.out::println);
上面的代码在并行执行时多次的执行结果可能是不同的。这是因为这个lambda表达式是有状态的。
//-----
副作用 Side-effects
有副作用的行为参数是（不？）被鼓励使用的。
副作用指的是行为参数在执行的时候有输入输入，比如网络输入输出等。
这是因为Java不保证这些副作用对其它线程可见，也不保证相同流管道上的同样的元素的不同的操作运行在同一个线程中。
很多有副作用的行为参数可以被转换成无副作用的实现。一般来说println()这样的副作用代码不会有害。
ArrayList<String> results = new ArrayList<>();
stream.filter(s -> pattern.matcher(s).matches())      
    .forEach(s -> results.add(s));  // 副作用代码
上面的代码可以改成无副作用的。
List<String>results = stream.filter(s -> pattern.matcher(s).matches())          
    .collect(Collectors.toList());  // No side-effects!
//-----
排序 Ordering
某些流的返回的元素是有确定顺序的，我们称之为encounter order。这个顺序是流提供它的元素的顺序，比如数组的encounter order是它的元素的排序顺序，List是它的迭代顺序(iteration order)，对于HashSet,它本身就没有encounter order。

一个流是否是encounter order主要依赖数据源和它的中间操作，比如数据源List和Array上创建的流是有序的(ordered)，但是在HashSet创建的流不是有序的。

sorted()方法可以将流转换成有序的，unordered可以将流转换成无序的。除此之外，一个操作可能会影响流的有序,比如map方法，它会用不同的值甚至类型替换流中的元素，所以输入元素的有序性已经变得没有意义了，但是对于filter方法来说，它只是丢弃掉一些值而已，输入元素的有序性还是保障的。

对于串行流，流有序与否不会影响其性能，只是会影响确定性(determinism)，无序流在多次执行的时候结果可能是不一样的。

对于并行流，去掉有序这个约束可能会提供性能，比如distinct、groupingBy这些聚合操作。
//-----
结合性 Associativity
一个操作或者函数op满足结合性意味着它满足下面的条件：
(a op b) op c == a op (b op c)

对于并发流来说，如果操作满足结合性，我们就可以并行计算：
a op b op c op d == (a op b) op (c op d)
比如min、max以及字符串连接都是满足结合性的。
//-----
创建Stream

可以通过多种方式创建流：

1、 通过集合的stream()方法或者parallelStream()，比如Arrays.asList(1,2,3).stream()。
    
2、 通过Arrays.stream(Object[])方法, 比如Arrays.stream(new int[]{1,2,3})。
    
3、 使用流的静态方法，比如Stream.of(Object[]),IntStream.range(int, int)或者Stream.iterate(Object, UnaryOperator)，如Stream.iterate(0, n -> n * 2)，或者generate(Supplier<T> s)如Stream.generate(Math::random)。
    
4、 BufferedReader.lines()从文件中获得行的流。
    
5、 Files类的操作路径的方法，如list、find、walk等。
    
6、 随机数流Random.ints()。
    
7、 其它一些类提供了创建流的方法，如BitSet.stream(),Pattern.splitAsStream(java.lang.CharSequence), 和JarFile.stream()。
    
8、 更底层的使用StreamSupport，它提供了将Spliterator转换成流的方法。
package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {
    @Before
    public void setUp() {
        System.out.println("before a test");
    }

    @After
    public void tearDown() {
        System.out.println("after a test");
    }

    @Test
    public void testStreamIterate() {
        Stream<Integer> integerStream = Stream.iterate(0, n -> n + 2);
        integerStream.limit(10).forEach(System.out::println);
    }

    @Test
    public void testStreamGenerate() {
        Stream<Double> doubleStream = Stream.generate(Math::random);
        doubleStream.limit(10).forEach(System.out::println);
    }

    @Test
    public void testArraysStream() {
        IntStream integerStream = Arrays.stream(new int[]{3, 5, 7});
        integerStream.limit(10).forEach(System.out::println); //limit(10)没有作用
    }

    @Test
    public void testStreamOf() {
        Stream<Integer> integerStream = Stream.of(3, 5, 7);
        integerStream.limit(10).forEach(System.out::println); //limit(10)没有作用
    }
}
输出：
before a test
0.6412578001250876
0.6807637928663443
0.4643683375012767
0.9000359460802849
0.7639279866143656
0.6827440857975002
0.32281157764858115
0.44884674944585
0.46244218856353414
0.6012744202148783
after a test
before a test
3
5
7
after a test
before a test
3
5
7
after a test
before a test
0
2
4
6
8
10
12
14
16
18
after a test
//-----
中间操作 intermediate operations

中间操作会返回一个新的流，并且操作是延迟执行的(lazy)，它不会修改原始的数据源，而且是由在终点操作开始的时候才真正开始执行。这个Scala集合的转换操作不同，Scala集合转换操作会生成一个新的中间集合，显而易见Java的这种设计会减少中间对象的生成。

下面介绍流的这些中间操作：

--distinct
distinct保证输出的流中包含唯一的元素，它是通过Object.equals(Object)来检查是否包含相同的元素。
List<String> l = Stream.of("a","b","c","b")        .distinct()        .collect(Collectors.toList());System.out.println(l); //[a, b, c]

--filter
filter返回的流中只包含满足断言(predicate)的数据。
下面的代码返回流中的偶数集合。
List<Integer> l = IntStream.range(1,10).filter( i -> i % 2 == 0).boxed().collect(Collectors.toList());
System.out.println(l); //[2, 4, 6, 8]
//
package test;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamTest {

    @Test
    public void testIntStreamRange() {
        List<Integer> l = IntStream.range(1,10)
            .filter(i -> i % 2 == 0)
            .boxed() //这里不用装箱操作返回Stream<Integer>，collect操作会编译错误
            .collect(Collectors.toList());
        System.out.println(l); //[2, 4, 6, 8]
    }

    @Test
    public void testIntStreamRangeForeach() {
        IntStream.range(1,10).boxed().forEach(System.out::println); //这里加不加boxed都没有关系
    }
}

--map
map方法将流中的元素映射成另外的值，新的值类型可以和原来的元素的类型不同。
下面的代码中将字符元素映射成它的哈希码(ASCII值)。
List<Integer> l = Stream.of('a','b','c')
    .map(c -> c.hashCode())
    .collect(Collectors.toList());
System.out.println(l); //[97, 98, 99]

--flatmap
flatmap方法混合了map+flattern的功能，它将映射后的流的元素全部放入到一个新的流中。它的方法定义如下：
<R> Stream<R> flatMap(Function<? super T,? extends Stream<? extends R>> mapper)
可以看到mapper函数会将每一个元素转换成一个流对象，而flatMap方法返回的流包含的元素为mapper生成的流中的元素。

下面这个例子中将一首唐诗生成一个按行分割的流，然后在这个流上调用flatmap得到单词的小写形式的集合，去掉重复的单词然后打印出来。
//
package test;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {

    @Test
    public void testFlagMap() {
        String poetry = "Where, before me, are the ages that have gone?/n"
            + "And where, behind me, are the coming generations?/n"
            + "I think of heaven and earth, without limit, without end,/n"
            + "And I am all alone and my tears fall down.";
        Stream<String> lines = Arrays.stream(poetry.split("/n"));
        Stream<String> words = lines.flatMap(line -> Arrays.stream(line.split(" "))); //flatMap是把每一个元素映射成一个stream
        List<String> l = words.map(w -> {
            if (w.endsWith(",") || w.endsWith(".") || w.endsWith("?"))
                return w.substring(0, w.length() - 1).trim().toLowerCase();
            else
                return w.trim().toLowerCase();
        })
            .distinct()
            .sorted()
            .collect(Collectors.toList());
        System.out.println(l); //[ages, all, alone, am, and, are, before, behind, coming, down, earth, end, fall, generations, gone, have, heaven, i, limit, me, my, of, tears, that, the, think, where, without]
    }
}
输出：
[ages, all, alone, am, and, are, before, behind, coming, down, earth, end, fall, generations, gone, have, heaven, i, limit, me, my, of, tears, that, the, think, where, without]

flatMapToDouble、flatMapToInt、flatMapToLong提供了转换成特定流的方法。

--peek
peek方法方法会使用一个Consumer消费流中的元素，但是返回的流还是包含原来的流中的元素。
String[] arr = new String[]{"a","b","c","d"};
Arrays.stream(arr).peek(System.out::println) //a,b,c,d        .count();
//
package test;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {

    @Test
    public void testPeek() {
        String[] arr = new String[]{"a","b","c","d"};
        Arrays.stream(arr).peek(System.out::println).count(); //如果没有count()，则peek里的Consumer操作不会执行

        Stream.of("one", "two", "three", "four")
            .filter(e -> e.length() > 3)
            .peek(e -> System.out.println("Filtered value: " + e))
            .map(String::toUpperCase)
            .peek(e -> System.out.println("Mapped value: " + e))
            .collect(Collectors.toList());//如果没有collect，则peek里的Consumer操作不会执行；还有这里的输出显示流操作是按元素从头到尾，而不是所有元素操作完再进行下一元素
    }
}
输出：
a
b
c
d
Filtered value: three
Mapped value: THREE
Filtered value: four
Mapped value: FOUR

--sorted
sorted()将流中的元素按照自然排序方式进行排序，如果元素没有实现Comparable，则终点操作执行时会抛出java.lang.ClassCastException异常。sorted(Comparator<? super T> comparator)可以指定排序的方式。
对于有序流，排序是稳定的。对于非有序流，不保证排序稳定。
String[] arr = new String[]{"b_123","c+342","b#632","d_123"};
List<String> l  = Arrays.stream(arr)
    .sorted((s1,s2) -> {
        if (s1.charAt(0) == s2.charAt(0))
            return s1.substring(2).compareTo(s2.substring(2));
        else
            return s1.charAt(0) - s2.charAt(0);
    })
        .collect(Collectors.toList());
    System.out.println(l); //[b_123, b#632, c+342, d_123]
//-----
终点操作 terminal operations

--match
public boolean allMatch(Predicate<? super T> predicate)
public boolean anyMatch(Predicate<? super T> predicate)
public boolean noneMatch(Predicate<? super T> predicate)
这一组方法用来检查流中的元素是否满足断言。
allMatch只有在所有的元素都满足断言时才返回true,否则flase,流为空时总是返回true
anyMatch只有在任意一个元素满足断言时就返回true,否则flase,
noneMatch只有在所有的元素都不满足断言时才返回true,否则flase,

System.out.println(Stream.of(1,2,3,4,5).allMatch( i -> i > 0)); //true      
System.out.println(Stream.of(1,2,3,4,5).anyMatch( i -> i > 0)); //true      
System.out.println(Stream.of(1,2,3,4,5).noneMatch( i -> i > 0)); //false

System.out.println(Stream.<Integer>empty().allMatch( i -> i > 0)); //true      
System.out.println(Stream.<Integer>empty().anyMatch( i -> i > 0)); //false      
System.out.println(Stream.<Integer>empty().noneMatch( i -> i > 0)); //true

--count
count方法返回流中的元素的数量。它实现为：
mapToLong(e -> 1L).sum();

--collect
<R,A> R collect(Collector<? super T,A,R> collector)
<R> R collect(Supplier<R> supplier, BiConsumer<R,? super T> accumulator, BiConsumer<R,R> combiner)

使用一个collector执行mutable reduction操作。辅助类 Collectors 提供了很多的collector，可以满足我们日常的需求，你也可以创建新的collector实现特定的需求。它是一个值得关注的类，你需要熟悉这些特定的收集器，如聚合类averagingInt、最大最小值maxByminBy、计数counting、分组groupingBy、字符串连接joining、分区partitioningBy、汇总summarizingInt、化简reducing、转换toXXX等。

IntStream intStream = Arrays.stream(new int[]{10, 9, 8, 7, 6, 5});
intStream.boxed().collect(maxBy((i1, i2) -> Integer.compare(i1, i2))); //不太会用？

第二个提供了更底层的功能，它的逻辑类似下面的伪代码：
R result = supplier.get();
for (T element : this stream)
    accumulator.accept(result, element);
return result;

例子：
List<String> asList = stringStream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
String concat = stringStream.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();

--find
findAny()返回任意一个元素，如果流为空，返回空的Optional，对于并行流来说，它只需要返回任意一个元素即可，所以性能可能要好于findFirst()，但是有可能多次执行的时候返回的结果不一样。findFirst()返回第一个元素，如果流为空，返回空的Optional。

--forEach、forEachOrdered
forEach遍历流的每一个元素，执行指定的action。它是一个终点操作，和peek方法不同。这个方法不担保按照流的encounter order顺序执行，如果对于有序流按照它的encounter order顺序执行，你可以使用forEachOrdered方法。
Stream.of(1,2,3,4,5).forEach(System.out::println);

--最大最小值
max返回流中的最大值，min返回流中的最小值。

IntStream intStream = Arrays.stream(new int[]{10, 9, 8, 7, 6, 5});
System.out.println(intStream.max().getAsInt()); //10

--reduce
reduce是常用的一个方法，事实上很多操作都是基于它实现的。它有几个重载方法：

pubic Optional<T> reduce(BinaryOperator<T> accumulator)
pubic T reduce(T identity, BinaryOperator<T> accumulator)
pubic <U> U reduce(U identity, BiFunction<U,? super T,U> accumulator, BinaryOperator<U> combiner)

第一个方法使用流中的第一个值作为初始值，后面两个方法则使用一个提供的初始值。
Optional<Integer> total = Stream.of(1,2,3,4,5).reduce( (x, y) -> x +y);
Integer total2 = Stream.of(1,2,3,4,5).reduce(0, (x, y) -> x +y);
值得注意的是accumulator应该满足结合性(associative)。
//
package test;

import org.junit.Test;

import java.util.Optional;
import java.util.stream.Stream;

public class StreamTest {

    @Test
    public void test() {
        Optional<Integer> total = Stream.of(1, 2, 3, 4, 5).reduce((x, y) -> x + y); //以第1个元素为初始值依次累加，就有可能是NULL，所以返回值是Optional
        System.out.println(total);

        Optional<Integer> totalNull = Stream.<Integer>empty().reduce((x, y) -> x + y);
        System.out.println(totalNull.isPresent());
        System.out.println(totalNull);

        Integer total2 = Stream.of(1, 2, 3, 4, 5).reduce(0, (x, y) -> x + y); //以提供值为初始值，返回值为提供值类型
        System.out.println(total2);

        Integer total2Null = Stream.<Integer>empty().reduce(0, (x, y) -> x + y);
        System.out.println(total2Null);
    }
}
输出：
Optional[15]
false
Optional.empty
15
0

--toArray()
将流中的元素放入到一个数组中。


--组合
concat用来连接类型一样的两个流。
public static <T> Stream<T> concat(Stream<? extends T> a, Stream<? extends T> b)

--转换
toArray方法将一个流转换成数组，而如果想转换成其它集合类型，需要调用collect方法，利用Collectors.toXXX方法进行转换：
public static <T,C extends Collection<T>> Collector<T,?,C> toCollection(Supplier<C> collectionFactory)
public static …… toConcurrentMap(……)
public static <T> Collector<T,?,List<T>> toList()
public static …… 	toMap(……)
public static <T> Collector<T,?,Set<T>> toSet()
//------------------------------------------------------------------------------------------------
//Java 8 Streams API：对Stream分组和分区
http://www.importnew.com/17313.html
这篇文章展示了如何使用 Streams API 中的 Collector 及 groupingBy 和 partitioningBy 来对流中的元素进行分组和分区。
思考一下 Employee 对象流，每个对象对应一个名字、城市和销售数量，如下表所示：
+----------+------------+-----------------+
| Name     | City       | Number of Sales |
+----------+------------+-----------------+
| Alice    | London     | 200             |
| Bob      | London     | 150             |
| Charles  | New York   | 160             |
| Dorothy  | Hong Kong  | 190             |
+----------+------------+-----------------+
1. 分组
首先，我们利用（lambda表达式出现之前的）命令式风格Java 程序对流中的雇员按城市进行分组：
Map<String, List<Employee>> result = new HashMap<>();
for (Employee e : employees) {
  String city = e.getCity();
  List<Employee> empsInCity = result.get(city);
  if (empsInCity == null) {
    empsInCity = new ArrayList<>();
    result.put(city, empsInCity);
  }
  empsInCity.add(e);
}
你可能很熟悉写这样的代码，你也看到了，一个如此简单的任务就需要这么多代码！

而在 Java 8 中，你可以使用 groupingBy 收集器，一条语句就能完成相同的功能，像这样：
Map<String, List<Employee>> employeesByCity =
  employees.stream().collect(groupingBy(Employee::getCity));
结果如下面的 map 所示：
{New York=[Charles], Hong Kong=[Dorothy], London=[Alice, Bob]}

还可以计算每个城市中雇员的数量，只需传递一个计数收集器给 groupingBy 收集器。第二个收集器的作用是在流分类的同一个组中对每个元素进行递归操作。
Map<String, Long> numEmployeesByCity =
  employees.stream().collect(groupingBy(Employee::getCity, counting()));
结果如下面的 map 所示：
{New York=1, Hong Kong=1, London=2}
顺便提一下，该功能与下面的 SQL 语句是等同的：
select city, count(*) from Employee group by city

另一个例子是计算每个城市的平均年龄，这可以联合使用 averagingInt 和 groupingBy 收集器：
Map<String, Double> avgSalesByCity =
  employees.stream().collect(groupingBy(Employee::getCity,
结果如下 map 所示：
{New York=160.0, Hong Kong=190.0, London=175.0}

2. 分区
分区是一种特殊的分组，结果 map 至少包含两个不同的分组——一个true，一个false。例如，如果想找出最优秀的员工，你可以将所有雇员分为两组，一组销售量大于 N，另一组小于 N，使用 partitioningBy 收集器：
Map<Boolean, List<Employee>> partitioned =
  employees.stream().collect(partitioningBy(e -> e.getNumSales() > 150));
输出如下结果：
{false=[Bob], true=[Alice, Charles, Dorothy]}

你也可以将 groupingBy 收集器传递给 partitioningBy 收集器来将联合使用分区和分组。例如，你可以统计每个分区中的每个城市的雇员人数：
Map<Boolean, Map<String, Long>> result =
  employees.stream().collect(partitioningBy(e -> e.getNumSales() > 150,
                               groupingBy(Employee::getCity, counting())));
这样会生成一个二级 Map:
{false={London=1}, true={New York=1, Hong Kong=1, London=1}}

//测试代码如下：
//Employee.java
package test;

public class Employee {
    private String name;
    private String city;
    private int sales;

    public Employee(String name, String city, int sales) {
        this.name = name;
        this.city = city;
        this.sales = sales;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public int getSales() {
        return sales;
    }

}

//Grouper.java
package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.partitioningBy;

public class Grouper {
    public static Map<String, List<Employee>> groupByCityUsingOld(List<Employee> employees) {
        Map<String, List<Employee>> result = new HashMap<>();
        for (Employee e : employees) {
            String city = e.getCity();
            List<Employee> empsInCity = result.get(city);
            if (empsInCity == null) {
                empsInCity = new ArrayList<>();
                result.put(city, empsInCity);
            }
            empsInCity.add(e);
        }
        return result;
    }

    public static Map<String, List<Employee>> groupByCityUsingStream(List<Employee> employees) {
        Map<String, List<Employee>> result = employees.stream().collect(groupingBy(Employee::getCity));
        return result;
        //return employees.stream().collect(groupingBy(Employee::getCity));
    }

    public static Map<String, Long> groupNumByCity(List<Employee> employees) {
        Map<String, Long> result = employees.stream().collect(groupingBy(Employee::getCity, counting()));
        return result;
    }

    public static Map<String, Double> groupAverageSalesByCity(List<Employee> employees) {
        Map<String, Double> result = employees.stream().collect(groupingBy(Employee::getCity, averagingInt(Employee::getSales)));
        return result;
    }

    public static Map<Boolean, List<Employee>> partitionBySales(List<Employee> employees) {
        Map<Boolean, List<Employee>> result = employees.stream().collect(partitioningBy(e -> e.getSales() > 150));
        return result;
    }

    public static Map<Boolean, Map<String, Long>> partitionBySalseAndThenCountByCity(List<Employee> employees) {
        Map<Boolean, Map<String, Long>> result = employees.stream().collect(
            partitioningBy(e -> e.getSales() > 150, groupingBy(Employee::getCity, counting())));
        return result;
    }
}

//GouperTest.java
package test;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class GrouperTest {

    private static List<Employee> employees;
    private static Employee alice;
    private static Employee bob;
    private static Employee charles;
    private static Employee dorothy;

    @BeforeClass
    public static void before() {
        employees = new ArrayList<>();
        alice = new Employee("Alice", "London", 200);
        bob = new Employee("Bob", "London", 150);
        charles = new Employee("Charles", "New York", 160);
        dorothy = new Employee("Dorothy", "Hong Kong", 190);

        employees.add(alice);
        employees.add(bob);
        employees.add(charles);
        employees.add(dorothy);
    }

    @Test
    public void testGroupUsingOldCodeStyle() {
        Map<String, List<Employee>> result = Grouper.groupByCityUsingOld(employees);
        assertCityGrouperResult(result);
    }

    @Test
    public void testGroupUsingStream() {
        Map<String, List<Employee>> result = Grouper.groupByCityUsingStream(employees);
        assertCityGrouperResult(result);
    }

    private void assertCityGrouperResult(Map<String, List<Employee>> result) {
        assertEquals(3, result.size());

        List<Employee> empsInLondon = result.get("London");
        assertThat(empsInLondon, hasItem(alice));
        assertThat(empsInLondon, hasItem(bob));
        assertThat(empsInLondon, hasItems(alice, bob));
        //assertThat(empsInLondon, hasItem(new Employee("Alice", "London", 200))); 这个判断将失败，所以是基于引用的相等？

        List<Employee> empsInNewYork = result.get("New York");
        assertThat(empsInNewYork, hasItem(charles));

        List<Employee> empsInHongKong = result.get("Hong Kong");
        assertThat(empsInHongKong, hasItem(dorothy));
    }

    @Test
    public void testGroupAndCountUsingSteam() {
        Map<String, Long> empCityNumberMap = Grouper.groupNumByCity(employees);

        assertEquals(3, empCityNumberMap.size());
        assertThat(empCityNumberMap, hasEntry("London", 2L));
        assertThat(empCityNumberMap, hasEntry("New York", 1L));
        assertThat(empCityNumberMap, hasEntry("Hong Kong", 1L));
//        Long empsNumInLondon = empCityNumberMap.get("London");
//        assertEquals(2, empsNumInLondon.longValue());
//
//        Long empsNumInNewYork = empCityNumberMap.get("New York");
//        assertEquals(1, empsNumInNewYork.longValue());
//
//        Long empsNumInHongKong = empCityNumberMap.get("Hong Kong");
//        assertEquals(1, empsNumInHongKong.longValue());
    }

    @Test
    public void testGroupAndAverageUsingStream() {
        Map<String, Double> empCitySalesMap = Grouper.groupAverageSalesByCity(employees);
        assertEquals(3, empCitySalesMap.size());
        assertThat(empCitySalesMap, hasEntry("London", 175.0));
        assertThat(empCitySalesMap, hasEntry("New York", 160.0));
        assertThat(empCitySalesMap, hasEntry("Hong Kong", 190.0));
    }

    @Test
    public void testPartition() {
        Map<Boolean, List<Employee>> partitioned = Grouper.partitionBySales(employees);
        assertEquals(2, partitioned.size());

        List<Employee> falseList = partitioned.get(false);
        assertThat(falseList, hasItem(bob));

        List<Employee> trueList = partitioned.get(true);
        assertThat(trueList, hasItems(alice, charles, dorothy));

//        //也可以用如下方法测试
//        List<Employee> toBeTestedList = new ArrayList<>();
//        toBeTestedList.add(bob);
//        assertThat(partitioned, hasEntry(false, toBeTestedList));
//
//        toBeTestedList.clear();
//        toBeTestedList.addAll(Arrays.asList(alice, charles, dorothy));
//        assertThat(partitioned, hasEntry(true, toBeTestedList));
    }

    @Test
    public void testPartitionAndGroupCombination() {
        Map<Boolean, Map<String, Long>> result = Grouper.partitionBySalseAndThenCountByCity(employees);
        assertEquals(2, result.size());

        Map<String, Long> falseList = result.get(false);
        assertThat(falseList, hasEntry("London", 1L));

        Map<String, Long> trueList = result.get(true);
        assertThat(trueList, hasEntry("London", 1L));
        assertThat(trueList, hasEntry("New York", 1L));
        assertThat(trueList, hasEntry("Hong Kong", 1L));
    }
}

//------------------------------------------------------------------------------------------------
//Stream和Optional
http://www.jdon.com/idea/java/using-optional-effectively-in-java-8.html

如果不想每次都进行null值判断，Java 8 的Optional就发挥作用了，允许我们返回一个空的对象。
Optional<T>有方法 isPresent() 和 get() 是用来检查其包含的对象是否为空或不是，然后返回它，如：

Optional<SomeType> someValue = someMethod();
if (someValue.isPresent()) { // check
    someValue.get().someOtherMethod(); // retrieve and call
}

//1. ifPresent()
//例：将单词流中的第一个以“L”开头的单词取出，并转换为大写字母输出
package test;

import org.junit.Test;

import java.util.Optional;
import java.util.stream.Stream;

public class StreamTest {
    @Test
    public void testOptional_NotNull() {
        Stream<String> names = Stream.of("Large", "Good", "Hello", "Luck");
        Optional<String> nameWithL = names.filter(name -> name.startsWith("L")).findFirst();
        nameWithL.ifPresent(name -> {
            String s = name.toUpperCase();
            System.out.println("The first word starting with L in upper case is : " + s);
        });
    }

    @Test
    public void testOptional_Null() {
        Stream<String> names = Stream.of("Large", "Good", "Hello", "Luck");
        Optional<String> nameWithA = names.filter(name -> name.startsWith("A")).findFirst();//如果不重新定义Stream变量，会编译错误：stream已经被操作过或已经关闭
        nameWithA.ifPresent(name -> { //找不到，则对象为空，ifPresent将无法进入
            String s = name.toUpperCase();
            System.out.println("The first word starting with A in upper case is : " + s);
        });
    }
}
输出：
The first word starting with L in upper case is : LARGE

这里ifPresent() 是将一个Lambda表达式作为输入，T值如果不为空将传入这个lambda。那么这个lambda将不为空的单词转为大写输出显示。在前面names单词流寻找结果中，有可能找不到开始字母为L的单词，返回为空，也可能找到不为空，这两种情况都传入lambda中，无需我们打开盒子自己编写代码来判断，它自动帮助我们完成了，无需人工干预。

//2. filter() 过滤
@Test
public void testOptional_filter() {
    Stream<String> names = Stream.of("Large", "Good", "Hello", "Luck");
    Optional<String> nameWithA = names.filter(name -> name.startsWith("L")).findFirst();
    //Optional<String> nameWithAFilter = nameWithA.filter(name -> name.equals("Luck"));
    //Optional<String> nameWithAFilter = nameWithA.filter(name -> true);
    Optional<String> nameWithAFilter = nameWithA.filter(name -> {
        String s = name.toUpperCase();
        return s.equals("LARGE");
    });
    nameWithAFilter.ifPresent(name -> {
        String s = name.toUpperCase();
        System.out.println("The first word starting with A in upper case is : " + s);
    });
}
输出：
The first word starting with L in upper case is : LARGE

//3. map() 将value值映射成一个新的Optional<U>对象
@Test
public void testOptional_map() {
    Stream<String> names = Stream.of("Large", "Good", "Hello", "Luck");
    Optional<String> nameLengthGTFour = names.filter(name -> isLengthGTFour(name)).findFirst();
    //Optional<String> nameLengthLTFive = names.filter(name -> isLengthLTFive(name)).findFirst();
    Optional<String> nameMapToUpperCase = nameLengthGTFour.map(String::toUpperCase);
    nameMapToUpperCase.ifPresent(name -> {
        System.out.println("Map value: " + name);
    });
}

private boolean isLengthGTFour(String s) {
    return s.length() > 4;
}

private boolean isLengthLTFive(String s) {
    return s.length() < 5;
}

//--http://www.importnew.com/6675.html
新版本的Java，比如Java 8引入了一个新的Optional类。Optional类的Javadoc描述如下：
这是一个可以为null的容器对象。如果值存在则isPresent()方法会返回true，调用get()方法会返回该对象。
本文会逐个探讨Optional类包含的方法，并通过一两个示例展示如何使用。

1. of
为非null的值创建一个Optional。
of方法通过工厂方法创建Optional类。需要注意的是，创建对象时传入的参数不能为null。如果传入参数为null，则抛出NullPointerException 。
//调用工厂方法创建Optional实例
Optional<String> name = Optional.of("Sanaulla");
//传入参数为null，抛出NullPointerException.
Optional<String> someNull = Optional.of(null);

2. ofNullable
为指定的值创建一个Optional，如果指定的值为null，则返回一个空的Optional。
ofNullable与of方法相似，唯一的区别是可以接受参数为null的情况。示例如下：
//下面创建了一个不包含任何值的Optional实例
//例如，值为'null'
Optional empty = Optional.ofNullable(null);

3. isPresent
非常容易理解
如果值存在返回true，否则返回false。
类似下面的代码：
//isPresent方法用来检查Optional实例中是否包含值
if (name.isPresent()) {
  //在Optional实例内调用get()返回已存在的值
  System.out.println(name.get());//输出Sanaulla
}

4. get
如果Optional有值则将其返回，否则抛出NoSuchElementException。
上面的示例中，get方法用来得到Optional实例中的值。下面我们看一个抛出NoSuchElementException的例子：
//执行下面的代码会输出：No value present 
try {
  //在空的Optional实例上调用get()，抛出NoSuchElementException
  System.out.println(empty.get());
} catch (NoSuchElementException ex) {
  System.out.println(ex.getMessage());
}

5. ifPresent
如果Optional实例有值则为其调用consumer，否则不做处理
要理解ifPresent方法，首先需要了解Consumer类。简答地说，Consumer类包含一个抽象方法。该抽象方法对传入的值进行处理，但没有返回值。Java8支持不用接口直接通过lambda表达式传入参数。
如果Optional实例有值，调用ifPresent()可以接受接口段或lambda表达式。类似下面的代码：
//ifPresent方法接受lambda表达式作为参数。
//lambda表达式对Optional的值调用consumer进行处理。
name.ifPresent((value) -> {
  System.out.println("The length of the value is: " + value.length());
});

6. orElse
如果有值则将其返回，否则返回指定的其它值。
如果Optional实例有值则将其返回，否则返回orElse方法传入的参数。示例如下：
//如果值不为null，orElse方法返回Optional实例的值。
//如果为null，返回传入的消息。
//输出：There is no value present!
System.out.println(empty.orElse("There is no value present!"));
//输出：Sanaulla
System.out.println(name.orElse("There is some value!"));

7. orElseGet
orElseGet与orElse方法类似，区别在于得到的默认值。orElse方法将传入的字符串作为默认值，orElseGet方法可以接受Supplier接口的实现用来生成默认值。示例如下：
//orElseGet与orElse方法类似，区别在于orElse传入的是默认值，
//orElseGet可以接受一个lambda表达式生成默认值。
//输出：Default Value
System.out.println(empty.orElseGet(() -> "Default Value"));
//输出：Sanaulla
System.out.println(name.orElseGet(() -> "Default Value"));
//Supplier接口下：无入参数，返回T类型
public interface Supplier<T> {
    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}

8. orElseThrow
如果有值则将其返回，否则抛出supplier接口创建的异常。
在orElseGet方法中，我们传入一个Supplier接口。然而，在orElseThrow中我们可以传入一个lambda表达式或方法，如果值不存在来抛出异常。示例如下：
try {
  //orElseThrow与orElse方法类似。与返回默认值不同，
  //orElseThrow会抛出lambda表达式或方法生成的异常 
  empty.orElseThrow(ValueAbsentException::new);
} catch (Throwable ex) {
  //输出: No value present in the Optional instance
  System.out.println(ex.getMessage());
}
ValueAbsentException定义如下：
class ValueAbsentException extends Throwable {
 
  public ValueAbsentException() {
    super();
  }
 
  public ValueAbsentException(String msg) {
    super(msg);
  }
 
  @Override
  public String getMessage() {
    return "No value present in the Optional instance";
  }
}

//
@Test(expected=RuntimeException.class)
public void testOptional_orElseGet() {
    Optional<String> empty = Optional.ofNullable(null);
    System.out.println(empty.orElse("orElse::empty"));
    System.out.println(empty.orElseGet(() -> "orElseGet::empty"));
    //System.out.println(empty.orElseThrow(() -> new RuntimeException()));
    System.out.println(empty.orElseThrow(RuntimeException::new));
}

9. map
map方法文档说明如下：
如果有值，则对其执行调用mapping函数得到返回值。如果返回值不为null，则创建包含mapping返回值的Optional作为map方法返回值，否则返回空Optional。
map方法用来对Optional实例的值执行一系列操作。通过一组实现了Function接口的lambda表达式传入操作。如果你不熟悉Function接口，可以参考我的这篇博客。map方法示例如下：
//map方法执行传入的lambda表达式参数对Optional实例的值进行修改。
//为lambda表达式的返回值创建新的Optional实例作为map方法的返回值。
Optional<String> upperName = name.map((value) -> value.toUpperCase());
System.out.println(upperName.orElse("No value found"));

10. flatMap
如果有值，为其执行mapping函数返回Optional类型返回值，否则返回空Optional。flatMap与map（Funtion）方法类似，区别在于flatMap中的mapper返回值必须是Optional。调用结束时，flatMap不会对结果用Optional封装。
flatMap方法与map方法类似，区别在于mapping函数的返回值不同。map方法的mapping函数返回值可以是任何类型T，而flatMap方法的mapping函数必须是Optional。
参照map函数，使用flatMap重写的示例如下：
//flatMap与map（Function）非常类似，区别在于传入方法的lambda表达式的返回类型。
//map方法中的lambda表达式返回值可以是任意类型，在map函数返回之前会包装为Optional。 
//但flatMap方法中的lambda表达式返回值必须是Optionl实例。 
Optional<String> upperName = name.flatMap((value) -> Optional.of(value.toUpperCase()));
System.out.println(upperName.orElse("No value found"));//输出SANAULLA

@Test()
public void testOptional_map_flatMap() {
    Optional<String> hello = Optional.ofNullable("hello");
    Optional<String> upperHello = hello.map(String::toUpperCase);
    System.out.println(upperHello.orElse("No value"));

    Optional<String> upperhello2 = hello.flatMap(name -> Optional.of(name.toUpperCase()));
    System.out.println(upperhello2.orElse("No value"));
}
输出：
HELLO
HELLO

@Test()
public void testOptional_map_flatMap_null() {
    Optional<String> hello = Optional.ofNullable(null);
    Optional<String> upperHello = hello.map(String::toUpperCase);
    System.out.println(upperHello.orElse("No value"));

    Optional<String> upperhello2 = hello.flatMap(name -> Optional.of(name.toUpperCase()));
    System.out.println(upperhello2.orElse("No value"));
}
输出：
No value
No value

11. filter
filter个方法通过传入限定条件对Optional实例的值进行过滤。文档描述如下：
如果有值并且满足断言条件返回包含该值的Optional，否则返回空Optional。
读到这里，可能你已经知道如何为filter方法传入一段代码。是的，这里可以传入一个lambda表达式。对于filter函数我们应该传入实现了Predicate接口的lambda表达式。如果你不熟悉Predicate接口，可以参考这篇文章。
现在我来看看filter的各种用法，下面的示例介绍了满足限定条件和不满足两种情况：
//filter方法检查给定的Option值是否满足某些条件。
//如果满足则返回同一个Option实例，否则返回空Optional。
Optional<String> longName = name.filter((value) -> value.length() > 6);
System.out.println(longName.orElse("The name is less than 6 characters"));//输出Sanaulla
 
//另一个例子是Optional值不满足filter指定的条件。
Optional<String> anotherName = Optional.of("Sana");
Optional<String> shortName = anotherName.filter((value) -> value.length() > 6);
//输出：name长度不足6字符
System.out.println(shortName.orElse("The name is less than 6 characters"));
//------------------------------------------------------------------------------------------------
//volatile使用
http://blog.csdn.net/feier7501/article/details/20001083
在当前的Java内存模型下，线程可以把变量保存在本地内存（比如机器的寄存器）中，而不是直接在主存中进行读写。这就可能造成一个线程在主存中修改了一个变量的值，而另外一个线程还继续使用它在寄存器中的变量值的拷贝，造成数据的不一致。 
要解决这个问题，只需要像在本程序中的这样，把该变量声明为volatile（不稳定的）即可，这就指示JVM，这个变量是不稳定的，每次使用它都到主存中进行读取。一般说来，多任务环境下各任务间共享的标志都应该加volatile修饰。 
volatile修饰的成员变量在每次被线程访问时，都强迫从共享内存中重读该成员变量的值。而且，当成员变量发生变化时，强迫线程将变化值回写到共享内存。这样在任何时刻，两个不同的线程总是看到某个成员变量的同一个值。 

上面的情况，可以使用synchronized来对boolPattern加锁，但是synchronized开销比volatile大，volatile能够胜任上面的工作。
volatile不保证原子操作，所以，很容易读到脏数据。
使用建议：在两个或者更多的线程访问的成员变量上使用volatile。当要访问的变量已在synchronized代码块中，或者为常量时，不必使用。 
//------------------------------------------------------------------------------------------------
//学习重构到模式
//策略模式 猫咪排序
//CatSortAlg
package test;

public class CatSortAlg {
    public static void sortByAge(Cat[] cats) {
        for (int i = 1; i < cats.length; i++) {
            for (int j = 0; j < cats.length - i; j++) {
                if (cats[j].age() > cats[j + 1].age()) {
                    Cat temp = cats[j];
                    cats[j] = cats[j + 1];
                    cats[j + 1] = temp;
                }
            }
        }
    }

    public static void sortByName(Cat[] cats) {
        for (int i = 1; i < cats.length; i++)
            for (int j = 0; j < cats.length - i; j++) {
                if (cats[j].name().compareTo(cats[j + 1].name()) > 0) {
                    Cat temp = cats[j];
                    cats[j] = cats[j + 1];
                    cats[j + 1] = temp;
                }
            }
    }
}

//
package test;

public class Cat {
    private final int age;
    private final String name;
    private final int weight;

    public Cat(int age, String name, int weight) {
        this.age = age;
        this.name = name;
        this.weight = weight;
    }

    public int age() {
        return age;
    }

    public String name() {
        return name;
    }

    public int weight() {
        return weight;
    }
}


//测试用例
package test;

import org.junit.Test;

import static org.junit.Assert.*;

public class CatSortAlgTest {
    @Test
    public void testCatOrderByAge() {
        Cat[] cats = new Cat[]{new Cat(3, "MiMi", 5), new Cat(1, "DaHuang", 4), new Cat(2, "XiaoHei", 3)};
        CatSortAlg.sortByAge(cats);
        assertEquals(cats[0].age(), 1);
        assertEquals(cats[1].age(), 2);
        assertEquals(cats[2].age(), 3);
    }

    @Test
    public void testCatOrderByName() {
        Cat[] cats = new Cat[]{new Cat(3, "MiMi", 5), new Cat(1, "DaHuang", 4), new Cat(2, "XiaoHei", 3)};
        CatSortAlg.sortByName(cats);
        assertEquals(cats[0].name(), "DaHuang");
        assertEquals(cats[1].name(), "MiMi");
        assertEquals(cats[2].name(), "XiaoHei");
    }
}

(1)Ctrl + Alt + M：抽取方法
(2)Ctrl + Alt + P：抽取到方法参数
(3)Move：将抽取的方法移到外部类
(4)删除static方法
(5)Ctrl + Alt + V：抽取局部变量
(6)Ctrl + Shift + Up：将比较函数抽取到for循环外
(7)Ctrl + Shift + Alt + T：抽取Interface
(8)修改子类为接口
(9)Ctrl + Alt + M：两个for循环抽取为函数
(10)Ctrl + Alt + P：Comparator抽取为函数入参
(11)Ctrl + Alt + N：把sortByName内联，再把sortByAge内联

最终效果如下：
//
package test;

import org.junit.Test;

import static org.junit.Assert.*;

public class CatSortAlgTest {
    @Test
    public void testCatOrderByAge() {
        Cat[] cats = new Cat[]{new Cat(3, "MiMi", 5), new Cat(1, "DaHuang", 4), new Cat(2, "XiaoHei", 3)};
        CatSortAlg.sort(cats, new AgeComparator());
        assertEquals(cats[0].age(), 1);
        assertEquals(cats[1].age(), 2);
        assertEquals(cats[2].age(), 3);
    }

    @Test
    public void testCatOrderByName() {
        Cat[] cats = new Cat[]{new Cat(3, "MiMi", 5), new Cat(1, "DaHuang", 4), new Cat(2, "XiaoHei", 3)};
        CatSortAlg.sort(cats, new NameComparator());
        assertEquals(cats[0].name(), "DaHuang");
        assertEquals(cats[1].name(), "MiMi");
        assertEquals(cats[2].name(), "XiaoHei");
    }
    
    @Test
    public void testCatOrderByWeight() {
        Cat[] cats = new Cat[]{new Cat(3, "MiMi", 5), new Cat(1, "DaHuang", 4), new Cat(2, "XiaoHei", 3)};
        CatSortAlg.sort(cats, new WeightComparator());
        assertEquals(cats[0].weight(), 3);
        assertEquals(cats[1].weight(), 4);
        assertEquals(cats[2].weight(), 5);
    }
}

//
package test;

public class CatSortAlg {
    public static void sort(Cat[] cats, Comparator comparator) {
        for (int i = 1; i < cats.length; i++) {
            for (int j = 0; j < cats.length - i; j++) {
                if (comparator.compare(cats[j], cats[j + 1])) {
                    Cat temp = cats[j];
                    cats[j] = cats[j + 1];
                    cats[j + 1] = temp;
                }
            }
        }
    }
}


//
package test;

public interface Comparator {
    boolean compare(Cat cat1, Cat cat2);
}

//
package test;

public class AgeComparator implements Comparator {
    @Override
    public boolean compare(Cat cat1, Cat cat2) {
        return cat1.age() > cat2.age();
    }
}


//
package test;

public class NameComparator implements Comparator {
    @Override
    public boolean compare(Cat cat1, Cat cat2) {
        return cat1.name().compareTo(cat2.name()) > 0;
    }
}


//
package test;

public class WeightComparator implements Comparator {
    @Override
    public boolean compare(Cat cat1, Cat cat2) {
        return cat1.weight() > cat2.weight();
    }
}

//------------------------------------------------------------------------------------------------
//junit4 assert类中的assert方法总结
http://blog.sina.com.cn/s/blog_44d19b500102vf5f.html

junit中的assert方法全部放在Assert类中，总结一下junit类中assert方法的分类。
1.assertTrue/False([String message,]boolean condition);
判断一个条件是true还是false。感觉这个最好用了，不用记下来那么多的方法名。
2.fail([String message,]);
失败，可以有消息，也可以没有消息。
3.assertEquals([String message,]Object expected,Object actual);
判断是否相等，可以指定输出错误信息。
第一个参数是期望值，第二个参数是实际的值。
这个方法对各个变量有多种实现。在JDK1.5中基本一样。
但是需要主意的是float和double最后面多一个delta的值，可能是误差范围，不确定这个 单词什么意思，汗一个。
4.assertNotNull/Null([String message,]Object obj);
判读一个对象是否非空(非空)。
5.assertSame/NotSame([String message,]Object expected,Object actual);
判断两个对象是否指向同一个对象。看内存地址。
7.failNotSame/failNotEquals(String message, Object expected, Object actual)
当不指向同一个内存地址或者不相等的时候，输出错误信息。
注意信息是必须的，而且这个输出是格式化过的。


//------------------------------------------------------------------------------------------------
//JUnit4单元测试
http://www.blogjava.net/jnbzwm/archive/2010/12/15/340801.html
JUnit4的用法介绍：
1. 在 JUnit4 中，测试是由 @Test 注释来识别的，如下所示
2. JUnit4 中，我们仍然可以在每个测试方法运行之前初始化字段或准备数据。然而，完成这些操作的方法不再需要叫做 setUp()，只要用 @Before 注释来指示该方法即可。
JUnit4允许我们使用 @Before 来注释多个方法，这些方法都在每个测试之前运行
3. 清除方法与初始化方法类似。在 JUnit3 中，我们要将方法命名为 tearDown() 才可以实现清除方法，但在JUnit4中，只要给方法添加@After标注即可。
测试方法结束后清除为此测试用例准备的一些数据。
与 @Before 一样，也可以用 @After 来注释多个清除方法，这些方法都在每个测试之后运行。 
最后，我们不再需要显式调用在超类中的初始化和清除方法，只要它们不被覆盖，测试运行程序将根据需要自动为您调用这些方法。
超类中的 @Before 方法在子类中的 @Before 方法之前被调用（这反映了构造函数调用的顺序）。
@After 方法以反方向运行：子类中的方法在超类中的方法之前被调用。否则，多个 @Before 或 @After 方法的相对顺序就得不到保证。

浅谈TDD
测试驱动开发，它是敏捷开发的最重要的部分。方法主要是先根据客户的需求编写测试程序，然后再编码使其通过测试。在敏捷开发实施中，开发人员主要从两个方面去理解测试驱动开发。
a)在测试的辅助下，快速实现客户需求的功能。通过编写测试用例，对客户需求的功能进行分解，并进行系统设计。我们发现从使用角度对代码的设计通常更符合后期开发的需求。可测试的要求，对代码的内聚性的提高和复用都非常有益。 
b)在测试的保护下，不断重构代码，提高代码的重用性，从而提高软件产品的质量。可见测试驱动开发实施的好坏确实极大的影响软件产品的质量，贯穿了软件开发的始终。 
在测试驱动开发中，为了保证测试的稳定性，被测代码接口的稳定性是非常重要的。否则，变化的成本就会急剧的上升。所以，自动化测试将会要求您的设计依赖于接口，而不是具体的类。进而推动设计人员重视接口的设计，体现系统的可扩展性和抗变性。 

则Before、After方法的执行流程如图所示：
这种方法有明显的缺陷，如果要初始化的是数据库的链接，或者是一个大的对象的话，而这些资源恰恰是整个测试用例类可以共用的，每次都去申请，确实是种浪费。所以JUnit4引入了@BeforeClass和@AfterClass。

这个特定虽然很好，但是一定要小心对待这个特性。它有可能会违反测试的独立性，并引入非预期的混乱。如果一个测试在某种程度上改变了 @BeforeClass 所初始化的一个对象，那么它有可能会影响其他测试的结果。也就是说，由BeforeClass申请或创建的资源，如果是整个测试用例类共享的，那么尽量不要让其中任何一个测试方法改变那些共享的资源，这样可能对其他测试方法有影响。它有可能在测试套件中引入顺序依赖，并隐藏 bug。

--测试异常@Test(expected=XXXException.class)

--参数化测试
为了保证单元测试的严谨性，我们经常要模拟很多种输入参数，来确定我们的功能代码是可以正常工作的，为此我们编写大量的单元测试方法。可是这些测试方法都是大同小异：代码结构都是相同的，不同的仅仅是测试数据和期望输出值。
JUnit4 的参数化测试方法给我们提供了更好的方法，将测试方法中相同的代码结构提取出来，提高代码的重用度，减少复制粘贴代码的痛苦。

很明显，代码简单且很清晰了。在静态方法 words 中，我们使用二维数组来构建测试所需要的参数列表，其中每个数组中的元素的放置顺序并没有什么要求，只要和构造函数中的顺序保持一致就可以了。现在如果再增加一种测试情况，只需要在静态方法 words 中添加相应的数组即可，不再需要复制粘贴出一个新的方法出来了。
这种参数化的测试用例写法，很适用于一些共用的功能方法。
//------------------------------------------------------------------------------------------------
//JUnit进行单元测试
//FizzBuzz
package fizzbuzz;

import static java.lang.String.*;

public class FizzBuzz {
    public static String convert(int i) {
        if (i % 15 == 0) {
            return "FizzBuzz";
        } else if (i % 3 == 0) {
            return "Fizz";
        } else if (i % 5 == 0) {
            return "Buzz";
        } else {
            return format("%d", i);
        }
    }
}

//哪种风格更好？
package fizzbuzz;

import static java.lang.String.*;

public class FizzBuzz {
    public static String convert(int i) {
        if (i % 15 == 0) {
            return "FizzBuzz";
        }

        if (i % 3 == 0) {
            return "Fizz";
        }

        if (i % 5 == 0) {
            return "Buzz";
        }

        return format("%d", i);
    }
}


//FizzBuzzTest
package fizzbuzz;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FizzBuzzTest {
    @Test
    public void test_return_normal_when_input_normal() {
        assertEquals("1", FizzBuzz.convert(1));
        assertEquals("2", FizzBuzz.convert(2));
    }

    @Test
    public void test_return_fizz_when_input_fizz() {
        assertEquals("Fizz", FizzBuzz.convert(3));
        assertEquals("Fizz", FizzBuzz.convert(6));
    }

    @Test
    public void test_return_buzz_when_input_buzz() {
        assertEquals("Buzz", FizzBuzz.convert(5));
        assertEquals("Buzz", FizzBuzz.convert(10));
    }

    @Test
    public void test_return_fizzbuzz_when_input_fizzbuzz() {
        assertEquals("FizzBuzz", FizzBuzz.convert(15));
    }
}

//--
http://tonl.iteye.com/blog/1948869
package calculator;

public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int minus(int a, int b) {
        return a - b;
    }

    public int square(int n) {
        return n * n;
    }

    public void squareRoot(int n) {
        for (; ;) {
            ;
        }
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public int divide(int a, int b) throws Exception {
        if (0 == b) {
            throw new Exception("除数不能为零");
        }
        return a / b;
    }
}

//
package calculator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CalculatorTest {
    private Calculator calculator = new Calculator();

    @BeforeClass
    public static void before() {
        System.out.println("global before");
    }

    @AfterClass
    public static void after() {
        System.out.println("global after destory");
    }

    @Before
    public void setUp() {
        System.out.println("一个测试开始：");
    }

    @After
    public void tearDown() {
        System.out.println("一个测试结束");
    }

    @Test
    @Ignore //@Ignore("该方法还未实现")
    public void test_return_addValue_when_add() {
        int result = calculator.add(1, 2);
        assertEquals(3, result);
    }

    @Test
    public void test_return_minusValue_when_minus() {
        int result = calculator.minus(5, 2);
        assertEquals(3, result);
    }

    @Test
    public void test_return_multiplyValue_when_multiply() {
        int result = calculator.multiply(4, 2);
        assertEquals(8, result);
    }

    @Test(timeout = 1000) //单位为毫秒
    public void test_return_outoftime_when_runingSquareRoot() {
        calculator.squareRoot(4);
    }

    @Test(expected = Exception.class)
    public void test_throw_exception() throws Exception {
        System.out.println("test divide 4 / 0");
        calculator.divide(4, 0);
    }

    @Test
    public void test_return_divideValue_when_divide() {
        int result = 0;
        try {
            result = calculator.divide(10, 5);
        } catch (Exception e) {
            e.printStackTrace();
            fail(); //如果这行没有执行，说明这部分正确
        }
        assertEquals(2, result);
    }
}
输出：
一个测试开始：
一个测试结束

org.junit.runners.model.TestTimedOutException: test timed out after 1000 milliseconds

	at calculator.Calculator.squareRoot(Calculator.java:17)
	at calculator.CalculatorTest.test_return_outoftime_when_runingSquareRoot(CalculatorTest.java:57)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.internal.runners.statements.FailOnTimeout$CallableStatement.call(FailOnTimeout.java:298)
	at org.junit.internal.runners.statements.FailOnTimeout$CallableStatement.call(FailOnTimeout.java:292)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.lang.Thread.run(Thread.java:745)

一个测试开始：
一个测试结束
一个测试开始：
test divide 4 / 0
一个测试结束
一个测试开始：
一个测试结束

Test ignored.一个测试开始：
一个测试结束
global before
global after destory


//assertThat
package calculator;

import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

public class CalculatorTest {

    @Test
    public void test_that() {
        String s = "hello, world";
        assertThat(s, is(s));
        assertThat(s, containsString("hello"));

        //assertThat(16, greaterThan(10)); //没有在hamcrest找到greaterThan方法

        //String[] stringArray = new String[]{"hello", "world"};//数组不是可迭代的对象
        //可以如下转化为List
        //List<String> stringList = Arrays.asList(stringArray);
        
        ArrayList<String> stringList = new ArrayList<String>();
        stringList.add("hello");
        stringList.add("world");
        assertThat(stringList, hasItem("hello"));
    }
}

//同样的用例，多组测试数据，参数化测试
package calculator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CalculatorTest2 {
    private Calculator calculator = new Calculator();
    private int param;
    private int result;

    //构造函数，对变量进行初始化
    //定义一个待测试的类，并且定义两个变量，一个用于存放参数，一个用于存放期待的结果
    public CalculatorTest2(int param, int result) {
        this.param = param;
        this.result = result;
    }

    @Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][] {
                {2, 4}, {0, 0}, {-3, 9}
        });
    }

    @Test
    public void test_return_multiTestValue_when_inputMultiTest() {
        int temp = calculator.square(param);
        assertEquals(result, temp);
    }
}

测试代码提交给JUnit框架后，框架如何来运行代码呢?答案就是——Runner。在JUnit中有很多个 Runner，他们负责调用测试代码，每一个Runner都有各自的特殊功能，要根据需要选择不同的Runner来运行测试代码。JUnit中有一个默认Runner，如果没有指定，那么系统自动使用默认 Runner来运行你的代码。这里参数化测试就没有再用默认的Runner了。
//--自定义Runner
package calculator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 大家可以看到，这个功能也需要使用一个特殊的Runner，
 * 因此我们需要向@RunWith标注传递一个参数Suite.class。
 * 同时，我们还需要另外一个标注@Suite.SuiteClasses，
 * 来表明这个类是一个打包测试类。我们把需要打包的类作为参数传递给该标注就可以了。
 * 有了这两个标注之后，就已经完整的表达了所有的含义，因此下面的类已经无关紧要，
 * 随便起一个类名，内容全部为空既可
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CalculatorTest.class, CalculatorTest2.class})
public class AllCalculatorTests {
}

这个测试类包含了上面的CalculatorTest.class和CalculatorTest2.class里面所有的测试函数，它的目的就是进行打包所有的测试。


//--
http://www.cnblogs.com/eggbucket/archive/2012/02/02/2335697.html
四、Runner (运行器)
大家有没有想过这个问题，当你把测试代码提交给JUnit框架后，框架如何来运行你的代码呢？答案就是——Runner。在JUnit中有很多个Runner，他们负责调用你的测试代码，每一个Runner都有各自的特殊功能，你要根据需要选择不同的Runner来运行你的测试代码。可能你会觉得奇怪，前面我们写了那么多测试，并没有明确指定一个Runner啊？这是因为JUnit中有一个默认Runner，如果你没有指定，那么系统自动使用默认Runner来运行你的代码。换句话说，下面两段代码含义是完全一样的：
import org.junit.internal.runners.TestClassRunner;
import org.junit.runner.RunWith;
//使用了系统默认的TestClassRunner，与下面代码完全一样
public class CalculatorTest ...{...} 

@RunWith(TestClassRunner.class)
public class CalculatorTest ...{...}
//------------------------------------------------------------------------------------------------
//assertThat断言
http://langgufu.iteye.com/blog/1893927
//------------------------------------------------------------------------------------------------
//Java中的Pair结构
package test;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TestPair {
    public static void main(String args[]) {
        Map.Entry<String, Integer> strIntPair1 = new AbstractMap.SimpleEntry<>("DingBen", 1);
        Map.Entry<String, Integer> strIntPair2 = new AbstractMap.SimpleEntry<>("LiLei", 2);
        System.out.println(strIntPair1);
        System.out.println(strIntPair2);

        Map<Map.Entry<String, Integer>, String> map = new HashMap<>();//这里如果换成TreeMap，将会有运行错误，提示AbstractMap.SimpleEntry无法比较，可以参考http://blog.csdn.net/litoupu/article/details/9335007进行自定义排序
        map.put(strIntPair2, "hello");
        map.put(strIntPair1, "world");

        Set<Map.Entry<String, Integer>> set = map.keySet();
        Iterator<Map.Entry<String, Integer>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> key = iterator.next();
            String value = map.get(key);
            System.out.println("Key:" + key + ", Value:" + value);
        }
    }
}
输出：
DingBen=1
LiLei=2
Key:LiLei=2, Value:hello
Key:DingBen=1, Value:world

//用mapEntry进行遍历是最高效的
package test;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestMapIteration {

    public static void main(String args[]) {
        Map<String, Integer> stringIntegerMap = new LinkedHashMap<>();
        stringIntegerMap.put("abc", 1);
        stringIntegerMap.put("def", 2);
        stringIntegerMap.put("opq", 3);

        for (Map.Entry<String, Integer> mapEntry : stringIntegerMap.entrySet()) {
            System.out.println("key: " + mapEntry.getKey() + ", value: " + mapEntry.getValue());
        }
    }
}
key: abc, value: 1
key: def, value: 2
key: opq, value: 3

//------------------------------------------------------------------------------------------------
//http://blog.sina.com.cn/s/blog_48c0812c0101alaz.html
由于interface中的数据成员会自动成为public static final，所以我们可以利用此性质把需要的常量归结到一个接口中，如下：
public interface Months{
int JANURAUY = 1;
int FEBRUARY = 2;                   
int MARCH =3;
}
然后要用的话，就直接Months.JANURAUY＊10类似的直接用就行了
//------------------------------------------------------------------------------------------------
java 一个类不能同时继承多个类，一个类只能继承一个类(class)，但是可以实现多个接口(interface);一个接口(interface)能够继承多个接口(interface)
//------------------------------------------------------------------------------------------------
? extends T 
?表示某个泛型类型，该类型继承于T（上限是T），是T的子类。所以声明一个List<? extends T> list，表示声明一个对象类型?是T子类的List，是无法加入基类T。list.add(T)不行。
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
System.out.println("当前时间……" + System.currentTimeMillis());
会在程序执行的过程中输出一系列时间，可以看出在哪些代码执行的进度是多少，分析出哪一段代码执行的时间长。

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
26.4.1 动态代理机制 P855
所谓动态代理，即通过代理类Proxy的代理，接口和实现类之间可以不直接发生联系，而可以在运行期（Runtime）实现动态关联。
Java动态代理类Java.lang.reflect包下，一般主要涉及到以下两个类：
（1）接口InvocationHandler：该接口仅定义一个方法。
Object invoke(Object obj, Method method, Object[] args);
第一个参数obj一般是指代理类，method是被代理的方法，args是该方法的参数数组。

（2）Proxy：该类即为动态代理类，作用类实现了InvocationHandler接口的代理类，其中主要包含以下函数：
--protected Proxy(InvocationHandler h)：构造函数，用于给内部的h赋值。
--static Class getProxyClass (ClassLoader loader, Class[] interfaces)：获得一个代理类，其中loader是类装载器，interfaces是真实类所拥有的全部接口的数组。
--static Object newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h)：返回代理类的一个实例，返回后的代理类可以当做被代理类使用。
所谓Dynamic Proxy是这样一种类：它是在运行时生成的class，在生成它时你必须提供一组Interface给它，然后该class就宣称它实现了这些interface。你当然可以把该class的实例当做这些interface中的任何一个来用。当然，这个Dynamic Proxy其实就是一个Proxy，它不会替你做实质性的工作，在生成它的实现时你必须提供一个handler，由它接管实际的工作。

26.4.2 动态代理应用 P856
Java 1.3 引入了名为“动态代理类”（Dynamic Proxy Class）的新特性，利用它可为“已知接口的实现”动态地创建包装器（wrapper）类。
先从非动态代理的实现说起。
1. 定义接口和实现类直接调用
为了实现一系列的不同实现，首先我们定义了一个接口类Hello：
//Hello 接口
public interface Hello {
    void say();
}
分别定义两个实现类HelloWorld和HelloChina，分别执行不同的say()代码：
public class HelloWorld implements Hello {
    public void say() {
        System.out.println("Hello World!");
    }
}

public class HelloChina implements Hello {
    public void say() {
        System.out.println("Hello China!");
    }
}

创建它们的实例，调用其中方法：
Hello world = new HelloWorld();
world.say();
Hello china = new HelloChina();
china.say();

2. 使用包装类进行包装 P857 （即装饰模式）
假定我们现在想拦截对HelloWorld和HelloChina类发出的方法say()的调用，比如在调用前和调用后分别输出字符串"start to say:"和"end say!"，需要定义一个对它们的共有接口类Hello的包装类HelloWrapper：
//包装类HelloWrapper
public class HelloWrapper implements Hello {
    private Hello wrapped;//包装对象
    
    public HelloWrapper(Hello hello) {
        this.wrapped = hello;
    }
    
    //包装函数
    public void say() {
        System.out.println("start to say:");
        wrapped.say()
        System.out.println("end say!");
    }
}
Tips：该包装类也实现了接口Hello，目的是要求该包装类实现对Hello中所有接口函数的包装。

此时可以使用包装类对上面的实现world和china进行包装：
//包装调用
HelloWrapper wrapper1 = new HelloWrapper(world);
wrapper1.say();
HelloWrapper wrapper2 = new HelloWrapper(china);
wrapper2.say();

对于这种包装风格的HelloWrapper来说，一旦想修改Hello接口，缺点就会暴露无遗。为Hello接口添加一个方法，就得为HelloWrapper类添加一个包装器方法。为Hello添加10个方法，就得为HelloWrapper添加10个方法。这显示是效率极差的一种方案。

如果将HelloWrapper继承自HelloWorld，即为实现类HelloWorld实现包装器：
//包装类HelloWrapper，类似WindowAdapter
public class HelloWrapper extends HelloWorld {
    private Hello wrapped;
    
    public HelloWrapper(Hello hello) {
        this.wrapped = hello;
    }

    public void say() {
        System.out.println("start to say:");
        wrapped.say();
        System.out.println("end say!");
    }
}

这种方式在修改接口方法时不必修改包装器，但是又出现了新问题，只有HelloWorld对象才能使用包装器HelloWrapper。而在此之前，实现了Hello接口的任何对象都可以使用HelloWrapper。现在，由Java施加的“线性类出身限制”禁止我们将任意Hello变成一个HelloWrapper。

3. 使用动态代理 P858
动态代理则综合了以上两种方案的优点。使用动态代理，你创建的包装器类不要求为所有方法都使用显式的包装器。

Tips：动态代理仍然有一个限制。当你使用动态代理时，要包装/扩展的对象必须实现一个接口，该接口定义了准备在包装器中使用的所有方法。这一限制的宗旨是鼓励良好的设计，而不是为你带来更多的麻烦。根据经验，每个类都至少应该实现一个接口（nonconstant接口）。良好的接口用法不仅使动态代理成为可能，还有利于程序的模块化。

下面的代码演示了用动态代理来创建一个代理类HelloHandler。创建的这个HelloHandler类不需要实现Hello接口，而是实现了java.lang.reflect.InvocationHandler，只提供了一个invoke()方法，代理对象上的任何方法调用都要通过这一方法进行。观察invoke()的主体，它包含了被调用方法的反射参数Method，可以使用该参数确定当前执行方法的属性。
我们得到的仍然只是一个具有invoke()方法的InvocationHandler，而不是真正想要的Hello对象。动态代理真正的魅力要到创建实现的Hello实例时才能反映出来，通过调用HelloHandler的构造方法初始化了被包装的对象proxyed，代码如下：
//
package test.proxy;

public interface Hello { //接口也不需要加abstract，默认就是abstract
   void say();//接口方法无需加public修饰符，在类实现接口的方法时必须加上public修饰符。
}

//
package test.proxy;

//同一个包中不需要引用
public class HelloWorld implements Hello {
    @Override
    public void say() {
        System.out.println("Hello World!");
    }
}

//
package test.proxy;

public class HelloChina implements Hello {
    @Override
    public void say() {
        System.out.println("Hello China!");
    }
}

//
package test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HelloHandler implements InvocationHandler {
    private Object proxyed;

    public HelloHandler(Object proxyed) {
        this.proxyed = proxyed;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        //方法调用之前
        System.out.println("start to say!");

        //调用原始对象的方法
        result = method.invoke(this.proxyed, args);

        //方法调用之后
        System.out.println("end say!");
        return result;
    }

    public static void main(String[] args) {
        //代理调用world
        Hello world = new HelloWorld();
        InvocationHandler handler1 = new HelloHandler(world);
        Hello proxy1 = (Hello) Proxy.newProxyInstance(world.getClass().getClassLoader(), world.getClass().getInterfaces(), handler1);
        proxy1.say();

        //代理调用china
        Hello china = new HelloChina();
        InvocationHandler handler2 = new HelloHandler(china);
        Hello proxy2 = (Hello) Proxy.newProxyInstance(china.getClass().getClassLoader(), china.getClass().getInterfaces(), handler2);
        proxy2.say();
    }
}
输出：
start to say!
Hello World!
end say!
start to say!
Hello China!
end say!

这段代码实现了对目标对象world和china的代理调用。
--首先根据被代理对象创建一个代理类handler1， 此处是HelloHandler对象。
--创建动态代理对象proxy1，它的第一个参数为world类的加载器， 第二个参数为该类的接口，第三个对象为代理对象handler1。
--通过动态代理对象proxy1调用say()方法，此时会在原始对象HelloWorld.say()方法前后输出两句字符串。

上面代码作用简单，就是告诉Proxy类用一个指定的类加载器来动态创建一个对象，该对象要实现指定的接口（本例为Hello），并用提供的InvocationHandler来代替传统的方法主体。结果对象在一个instanceof Hello测试中返回true，并提供了在实现了Hello接口的任何类中都能找到的方法。
在HelloHandler类的invoke()方法中，完全不存在对Hello接口的引用。在本例中，以构造函数参数的形式，为HelloHandler提供了Hello的一个实例。代理Hello实例上的任何方法调用最终都由HelloHandler委托给这个“包装的”Hello。但是，虽然这是最常见的设计，但必须了解，InvocationHandler不一定非要委托给被代理的接口的另一个实例。事实上，InvocationHandler完全能自行提供方法主体，而无须一个委托目标。
最后注意，如果Hello接口中发生改变，那么HelloWorld中的invoke()方法将仍然可移植。如假定say()方法被重命名，那么新的方法名依然会被拦截。

26.4.3 基于动态代理的AOP实现 P860
上面的动态代理对实现类HelloWorld和HelloChina都实现了拦截，这实际上是实现了AOP的功能。AOP（Aspect Oriented Programming）面向切片编程，其中的一种实现方法便是用Proxy来实现的。
AOP的好处是，可以对类的实例统一实现拦截操作，通常应用在日志、权限、缓存、连接池中等。上面的代理拦截器在函数执行前后分别输出字符串，实际上就是一种日志拦截。但它的形式还不太节省，使用代理类进行拦截的代码需要重复编写。为了进行更好的拦截，将该代码抽象出来，通过要拦截的实例对象来创建拦截类实例，即AOP容器类：
//
package test.proxy;

import java.lang.reflect.Proxy;

public class AOPContainer {
    public static Object getBean(Object object) {
        HelloHandler handler = new HelloHandler(object);
        return Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), handler);
    }
}

//HelloHandler修改如下，main函数的位置不太好
package test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HelloHandler implements InvocationHandler {
    private Object proxyed;

    public HelloHandler(Object proxyed) {
        this.proxyed = proxyed;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        //方法调用之前
        System.out.println("start to say!");

        //调用原始对象的方法
        result = method.invoke(this.proxyed, args);

        //方法调用之后
        System.out.println("end say!");
        return result;
    }

    public static void main(String[] args) {
        //代理调用world
        Hello world = new HelloWorld();
        Hello aop1 = (Hello) AOPContainer.getBean(world);
        aop1.say();

        //代理调用china
        Hello china = new HelloChina();
        Hello aop2 = (Hello) AOPContainer.getBean(china);
        aop2.say();
    }
}
这就通过Java Proxy实现了一个简单的AOP容器。也简单展示了AOP的基本实现原理，可以以此为基础实现一个功能完善的AOP容器。
Tips：详细的AOP的应用知识请参见另一本AOP专题图书《开发者突击：精通AOP整合应用开发（AspectWerkz+AspectJ+Spring）》。

26.4.4 基于动态代理的字节码库 P861

26.5.3 课后上机作业 P863
实现一个反射任务工厂类TaskFactory。
//task.properties
login=test.task.impl.LoginTask
register=test.task.impl.RegisterTask
logout=test.task.impl.LogoutTask

//TaskException.java
package test.task;

/**
 * 处理器异常类
 */
public class TaskException extends Exception {
    private static final long serialVersionUID = 1L;
    private static final String COMMAND_NOTFOUND = "无法找到类：";
    private static final String FILE_NOTFOUND = "无法找到文件：";
    private static final String PROFILE_LOADFAIL = "加载property文件失败：";

    public TaskException(String message) {
        super(message);
    }

    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public static TaskException loadPropFileFailed(String fileName) {
        return new TaskException(PROFILE_LOADFAIL + fileName);
    }

    public static TaskException fileNotFound(String fileName) {
        return new TaskException(FILE_NOTFOUND + fileName);
    }

    public static TaskException taskNotFound(String clzName) {
        return new TaskException(COMMAND_NOTFOUND + clzName);
    }
}

//ITask.java
package test.task;

public interface ITask {
    String process(String value) throws TaskException;
}

//TaskFactory.java
package test.task;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

public class TaskFactory {
    private Properties properties = new Properties();

    /**
     * 单态工厂实例
     */
    private static TaskFactory m_instance = null;

    /**
     * 工厂构造函数，用于加载处理器组件配置文件
     *
     * @throws TaskException
     */
    public TaskFactory() throws TaskException {
        String fileName = "tasks.properties";
        try {
            InputStream in = getClass().getResourceAsStream(fileName);//快速读取文件内容到输入流的方法
            this.properties.load(in);
        } catch (FileNotFoundException e) {
            throw TaskException.fileNotFound(fileName);
        } catch (IOException e) {
            throw TaskException.loadPropFileFailed(fileName);
        }
    }

    /**
     * 创建工厂的唯一实例
     *
     * @return
     * @throws TaskException
     */
    public static TaskFactory getInstance() throws TaskException {
        if (m_instance == null) {
            m_instance = new TaskFactory();
        }
        return m_instance;
    }

    /**
     * 根据用户请求的地址，取得处理器对象
     * @param taskType
     * @return
     * @throws TaskException
     */
    public ITask getTask(String taskType) throws TaskException {
        return createTask(taskType);
    }

    /**
     * 根据用户请求的地址，取得处理器对象
     * @param taskType
     * @return
     * @throws TaskException
     */
    private ITask createTask(String taskType) throws TaskException {
        ITask task = null;

        //取得处理器类名
        String handlerName = this.properties.getProperty(String.valueOf(taskType));

        //未配置实现类
        if (handlerName == null) {
            throw TaskException.taskNotFound(String.valueOf(taskType));
        }

        //未找到实现类
        try {
            //加载类的实例
            Class<?> cls = Class.forName(handlerName);
            Constructor<?> ct = cls.getConstructor();
            task = (ITask) ct.newInstance();
        } catch (Exception e) {
            throw TaskException.taskNotFound(e.getMessage());
        }

        return task;
    }
}

//LoginTask.java
package test.task.impl;

import test.task.ITask;
import test.task.TaskException;

public class LoginTask implements ITask {
    @Override
    public String process(String value) throws TaskException {
        return value + "登录成功";
    }
}

//RegisterTask.java
package test.task.impl;

import test.task.ITask;
import test.task.TaskException;

public class RegisterTask implements ITask {
    @Override
    public String process(String value) throws TaskException {
        return value + "注册成功";
    }
}

//LogoutTask.java
package test.task.impl;

import test.task.ITask;
import test.task.TaskException;

public class LogoutTask implements ITask {
    @Override
    public String process(String value) throws TaskException {
        return value + "已注销";
    }
}

//Test.java
package test.task;

import java.util.Scanner;

public class Test {
    public static void main(String args[]) {
        try {
            while (true) {
                String str = getDataFromConsole();
                String[] arr = str.split(" ");

                String cmd = arr[0];
                String value = arr[1];

                //反射调用
                TaskFactory factory = TaskFactory.getInstance();
                ITask task = factory.getTask(cmd);

                String out = task.process(value);
                System.out.println(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getDataFromConsole() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
输出：
register hello
hello注册成功
login admin
admin登录成功
logout admin
admin已注销
abc
java.lang.ArrayIndexOutOfBoundsException: 1
	at test.task.Test.main(Test.java:13)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:144)
    
基于该框架可以实现更多的实现类，这就是面向接口编程的好处，它的核心是使用了Java的反射机制来动态加载类，在加载前的类名是动态的。
Tips：这种模式是一种经典的工厂模式，在类似的任务处理模型中都可以拿过来直接使用。希望可以学会、理解并记住这个案例。
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第27课 Java泛型编程 P871
27.1 了解Java泛型 P871
泛型是为了解决Java中的强制类型转换错误而产生的。
27.1.1 数据类型转换错误 P871
27.1.2 用泛型消除类型转换 P871
简单的定义Hashtable：
Hashtable h = new Hashtable();
h.put(new Integer(1), "admin");
String s = (String) h.get(new Integer(1));
System.out.println(s);

下例中，真正想要做的是创建Hashtable实例，它只将Integer映射为String，可以用新的Hashtable类来完成这件事：
Hashtable<Integer, String> h = new Hashtable<Integer, String>();
h.put(new Integer(1), "admin");
String s = h.get(new Integer(1));
System.out.println(s);
现在不再需要数据类型转换了。
这里使用泛型版本的Hashtable，就不用编写类型转换的代码了，类型转换的过程交给编译器来处理，方便而且安全。


package test.task;

import java.util.ArrayList;
import java.util.List;

public class TestGeneric {
    public static void main(String args[]) {
        List l = new ArrayList();//不声明泛型类型，则可以加入任意类型
        l.add("abc");
        l.add(3);
        for (Object o : l) {
            System.out.println(o);
        }

        List<String> strList = new ArrayList<>(10);//声明了泛型类型，则只能加入特定类型
        strList.add("hello");
        strList.add("world");
//        strList.add(1);
        for (String s : strList) {//编译器提示可以使用foreach流来代替
            System.out.println(s);
        }
    }
}

27.2 泛型类的开发与使用 P872
27.2.1 定义泛型类 P873
public class TestGeneric<K, V> {
}

定义泛型接口的方法类似，可以为接口添加泛型参数：
public interface TestInterface<T> {
    void func(T t);
}

//
package test.task;

import java.util.Hashtable;

public class TestGeneric<K, V> {
    private Hashtable<K, V> hash = new Hashtable<>();//写成这个形式new Hashtable<K, V>();，会提示<K, V>可以被隐式转换的<>代替

    public void put(K k, V v) {
        hash.put(k, v);
    }

    public V get(K k) {
        return hash.get(k);
    }

    public static void main(String args[]) {
        TestGeneric<String, String> testGeneric = new TestGeneric<>();
        testGeneric.put("001", "admin");
        testGeneric.put("002", "ben");
        String str = testGeneric.get("001");
        System.out.println(str);
    }
}
输出：
admin

在主函数中即可定义该类的实例，反省类型参数分别使用String和String，在使用get()方法时就不需要进行类型转换了。
Tips：泛型类使得类的使用更加方便，只需要在使用时来确定泛型参数的类型，大大增加了程序的通用性，就像是C++中的模板。目前Java中集合框架都已经被泛型化了。

27.2.2 无界通配符“?”
上面在使用泛型类型时指定了参数的类型，有时候也许我们并不能提前知道参数的类型，这时候可以使用无界通配符“?”。如下所示，定义的类型泛型使用问号表示，表示test的类型是未知的：
TestGeneric<?, ?> test = new TestGeneric<String, String>();
此进的test中不能加入任何元素，因为这不是类型安全的。这种做法通常用在面向接口编程中，无界通配符通常用做函数的返回参数，用以在不同的函数中实现不同类型的返回结果。

如下的两个实现函数所示，它们返回的都是ArrayList类型，使用的泛型参数分别是String和Integer，此时的接口函数返回值就可以使用无界通配符“?”来表示。
public List<?> getList() {
    return new ArrayList<String>();
}

public List<?> getList() {
    return new ArrayList<Integer>();
}

27.2.3 上限通配符extends P874
有时候想限制可能出现的泛型类的类型。在上面的示例中，类Hashtable的类型参数可以用想用的任何类型参数进行实例化，但时对于其他某些类，想将可能的类型参数集限定为给定类型范围内的子类型。
 
可能想定义泛型类TestGenericExtend，实现的功能与TestGeneric相似，只不过要求键的泛型参数必须是Number的子类。这时就可以使用extends关键字来限制K，用法如下：
public class TestGenericExtend<K extends Number, V> {
}
只需要在类的泛型参数中指定该泛型的父类即可，类中的代码不变。如果有多个接口的上限，可以使用&符号增加接口。如可以为K增加一个Serializable接口限制：
//实现java.io.Serializable 接口的类是可序列化的。没有实现此接口的类将不能使它们的任一状态被序列化或逆序列化。
public class TestGenericExtend<K extends Number & Serializable, V> {
}

//
package test.task;

import java.io.Serializable;
import java.util.Hashtable;

public class TestGenericExtend<K extends Number & Serializable, V> {
    private Hashtable<K, V> hash = new Hashtable<>();

    public void put(K k, V v) {
        hash.put(k, v);
    }

    public V get(K k) {
        return hash.get(k);
    }

    public static void main(String args[]) {
        TestGenericExtend<Integer, String> test = new TestGenericExtend<>();
        test.put(1, "admin");
        test.put(2, "ben");
        String str = test.get(1);
        System.out.println(str);
    }
}
使用该泛型的类时，K的类型必须是Number的子类，如果使用String代替K，将出现类型匹配异常。
当然可以完全不使用显式的范围，只要能确保没有使用不适当的类型来实例化类型参数。使用类型参数设定范围的两个原因：
--范围增加了静态类型检查功能。有了静态类型检查，就能保证泛型类型的每次实例化都符合所设定的范围。
--我们知道类型参数的每次实例化都是这个范围之内的子类，所以可以放心地调用类型参数实例出现在这个范围之内的任何方法。如果没有对参数设定显式的范围，那么默认情况下范围是Object，这意味着我们不能调用实例在Object中未曾出现的任何方法。

27.2.4 下限通配符super P875
通常所谈的主要的是关于上限通配符，还有一个下限通配符super，用来限制泛型最小的范围。
如，对于List<? super Number>是一个“元素类型”未知的列表，但是可能是Number或者Object的超类型，所以它可能是一个List<Number>或一个List<Object>。
//使用下限通配符
List<? super Number> l = new ArrayList<Object>();
l.add(new Integer(5));
由于Integer是Number类型，因此l的下限是Number类。下限通配符远没有上限通配符那样常见，但当需要它们的时候，它们就是必需的。
//
package test.task;

import java.util.ArrayList;
import java.util.List;

public class TestGenericExtend {

    public static void main(String args[]) {
        List<? super Number> l = new ArrayList<Object>();
        l.add(new Integer(10));
        //l.add("abc");

        List<? super Number> l2 = new ArrayList<>();//new ArrayList<Integer>();编译错误，类型下限是Number，即必须是Number的基类或者Number
        l2.add(new Integer(10));
        //l2.add("abc");//编译错误，不是特别懂？？？，表明l是一个下限为Number的类型List，那List<Object>为什么不让加入String
    }
}

27.2.5 定义多态方法 P876 即类不是泛型的，成员函数是泛型的
除了用类型参数对类进行参数化之外，用类型参数对方法进行参数化往往也同样很有用。泛型在Java编译用语中，用类型进行参数化的方法被称为多态方法（Polymorphic method）。
定义的形式为，在函数名前定义泛型参数，该参数可以在函数的返回值类型、参数类型、函数体代码中引用，如下：
public <T> List<T> getList<T obj> {
    return new ArrayList<T>();
}

//
package test;

public class TestGenericMethod {
    public <T> String getString(T obj) {
        return obj.toString();
    }

    public static void main(String args[]) {
        TestGenericMethod test = new TestGenericMethod();
        String s = "Hello";
        Integer i = 100;
        Float f = 95.1f;
        System.out.println(test.getString(s));
        System.out.println(test.getString(i));
        System.out.println(test.getString(f));
    }
}
输出：
Hello
100
95.1

同样，也可以进行类型范围的限制，如，要求T是Number的子类：
public <T extends Number> String getString(T obj) {
    return obj.toString();
}
此时函数只能输入Number的子类型函数，上例中的String类型参数，将会产生类型匹配异常。
Tips：多态方法这所以有用，是因为有时候在一些我们想执行的操作中，参数与返回值之间的类型相关性原本就是泛型的，但是这个泛型性质不依赖于任何类级的类型信息，而且对于各个方法调用都不同相同。

27.2.6 定义泛型异常 P877
在定义接口时，可以像定义类一样指定一个泛型异常，可以使用extends指定异常的类型为Exception，或者更小的范围。然后在接口的方法中可以抛出该异常。如：
//
package test;

public interface TestGenericException<E extends Exception> {
    void execute(int i) throws E;
}

//
package test;

import java.io.IOException;

public class TestGenericExceptionMain implements TestGenericException<IOException> {

    @Override
    public void execute(int i) throws IOException {
        if (i < 10) {
            throw new IOException();
        }
    }

    public static void main(String args[]) {
        try {
            TestGenericExceptionMain test = new TestGenericExceptionMain();
            test.execute(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
输出：
java.io.IOException
	at test.TestGenericExceptionMain.execute(TestGenericExceptionMain.java:10)
	at test.TestGenericExceptionMain.main(TestGenericExceptionMain.java:17)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:144)

Tips：泛型异常只能在接口中定义，在实现类中指定具体的异常。这种做法的好处是，可以根据不同的异常类型定义不同的实现类。

//------------------------------------------------------------------------------------------------
//Java核心编程技术 第28课 Java注释符编程 P881
28.1 JDK内置注释 P881
28.1.1 注释的格式与分类 P881
可以将注释分为3个基本种类。
--标记注释没有变量。注释显示简单，有名称标识，没有提供其他数据。如，@MarkerAnnotation是标记注释，它不包含数据，仅有注释名称。
@Deprecated

--单一值注释与标记注释类似，但提供一段数据。因为仅提供很少的一点数据，所以可以使用快捷语法（除了@标记外，这应该与普通的Java方法调用很像）：
@SingleValueAnnotation("my data")
如，下面的注释有一个参数：
@SuppressWarnings("deprecation")
public static void selfDestruct() {
    Thread.currentThread().stop();
}

--完整注释有多个数据成员。因此，必须使用更完整的语法（注释不再像普通的Java方法）：
@FullAnnotation(var1="data value 1", var2="data value 2", var3="data value 3")

28.1.2 覆盖注释@Override P882
第一个内置注释类型是@Override。仅用于方法（不用于类、包声明或其他构造），指明注释的方法将覆盖超类中的方法。
使用@Override的作用如下：
（1）它能够强制该函数的代码必须符合超类中该方法的定义格式
函数的参数、抛出异常等必须符合父类的函数，不会让你犯错。
（2）检查该方法必须是父类中的方法
如果使用了@Override，那么该方法必须是其父类中的方法。
在覆盖父类的方法时最好使用@Override，可以为函数形式做正确性检查，也能够标志该方法是被覆盖的方法。

28.1.3 过时注释@Deprecated P883
下一个标准注释类型是@Deprecated。与@Override一样，@Deprecated是标记注释，用来对不应再使用的方法进行注释，这通常用在对新版本代码的修改。如旧版本中提供的方法，在新版本中有了更好的方法，将提示用户可以放弃旧的方法的使用。
//
package test;

public class TestDeprecated {
    //把@Deprecated删掉，调用方法时就没有删除线了
    @Deprecated
    public void oldShow() {
        System.out.println("This is deprecated oldShow()");
    }

    public void newShow() {
        System.out.println("This is newShow()");
    }

    public static void main(String args[]) {
        TestDeprecated testDeprecated = new TestDeprecated();
        testDeprecated.oldShow();//编译器会自动将标注为@Deprecated的方法显示一条删除线
        testDeprecated.newShow();
    }
}
Tips：过时的函数不建议你爱我吗，但不是不可使用，只能说是该函数在新的版本中性能、安全性等方法不是那么完美了，因此才建议使用新的方法。

28.1.4 警告注释@SuppressWarnings P883
最后一个注释类型是@SuppressWarnings，作用是用来消除代码的警告。

@SuppressWarnings("unchecked")

@SuppressWarnings是具有变量的，可以将单一注释类型与该变量一起使用。
也可以以值数组的形式来提供变量，其中每个值指明要阻止的一种特定警告类型。@SuppressWarnings中变量的值采用数组，可以在同一注释中阻止多个警告。例如，@SuppressWarnings(value={"unchecked", "fallthrough"})使用两个值的数组。

28.2 自定义注释 P885
28.2.1 定义注释类型@interface P885
@interface声明：定义新的注释类型与创建接口有很多类似之处，只不过interface关键字之前要有一个@符号，最简单的注释类型示例：
public @interface TestInterface {
}

注释类与普通类文件一样，可以以注释类名作为文件名，如果编译这个注释类型，并将其放在类路径中，那么就可以在源代码方法中使用它，如下：
@TestInterface
public void test() {
}

28.2.2 添加成员变量 P885
注释类型可能有成员变量，用以添加更加复杂的元数据。注释类型中的数据成员被设置成使用有限的信息进行工作，定义数据成员后不需要分别定义访问和修改的方法。只需要定义一个方法，以成员的名称命名它，数据类型应该是该方法返回值的类型。
下面示例，为注释类型@TestInterface添加了3个成员变量，第一个为整型，第二和第三个为字符串型：
//
package test;

public @interface TestInterface {
    int number();
    String value();
    String description();
}

//
package test;

public class TestDeprecated {
    //这里无法省略number=
    @TestInterface(number = 10, value = "v", description = "d")
    public static void main(String args[]) {
        System.out.println("@TestInterface");
    }
}

//只有一个数据成员时
package test;

public @interface TestInterface {
    int number();
}
//
package test;

public class TestDeprecated {
    //这里编译不过，为什么？
    @TestInterface(10)
    public static void main(String args[]) {
        System.out.println("@TestInterface");
    }
}

28.2.3 设置默认值 P886
在成员变量后使用default指定默认值，默认值的类型必须与成员变量声明的类型完全相同。
package test;

public @interface TestInterface {
    int number() default 1;
    String value() default "hello";
    String description() default "world";
}
//
package test;

public class TestDeprecated {
    @TestInterface(value = "abc")
    public static void main(String args[]) {
        System.out.println("@TestInterface");
    }
}

28.2.4 设置目标范围@Target P886
对于自定义的注释类型，可以限定引用它的时机，可以在注释类前使用JDK 5.0 内置的注释@Target来指定注释的目标范围。
ElementType这个枚举定义了注释类型可应用的不同程序元素。

//演示@Target的用法
package test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})//如果把ElementType.METHOD注释掉，则会报编译错误，无法在main方法前使用
public @interface TestInterface {
    int number() default 1;
    String value() default "hello";
    String description() default "world";
}

Java编译器将把@TestInterface应用于类型、方法、构造函数和其他注释类型，这样有助于避免他人误用你的注释类型。

28.2.5 设置保持性@Retention P887
这个注释和Java编译器处理注释的注释类型的方式有关，编译器有以下几种不同的选择。
--将注释保留在编译后的类文件中，并在第一次加载类时读取它。
--将注释保留在编译后的类文件中，但是在运行时忽略它。
--按照规定使用注释，但是并不将它保留到编译后的类文件中。
这3种选项用java.lang.annotation.RetentionPolicy枚举表示。

@Retention注释类型使用枚举中的一个作为唯一的参数，将该元注释用于注释，如下：

package test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface TestInterface {
    int number() default 1;
    String value() default "hello";
    String description() default "world";
}
这将使得@TestInterface注释符只能够保持在Java源代码中。

28.2.6 添加公共文档@Documented P888
这是一个标记注释，没有成员变量，表示注释应该出现在类的Javadoc中，在默认情况下，注释不包括在Javadoc中。
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestInterface {
    int number() default 1;
    String value() default "hello";
    String description() default "world";
}
@Documented的一个实用技巧是保持性策略。上例中规定注释的保持性（retention）是RUNTIME，这是使用@Documented注释类型所必需的。Javadoc使用虚拟机从其类文件（而非源文件）中加载信息。确保JVM从这些类文件中获得生成Javadoc所需信息的唯一方法是将保持性规定为RetentionPolicy.RUNTIME。这样，注释就会保留在编译后的类文件中并且由虚拟机加载，然后Javadoc可以从抽取出来添加到类的HTML文件中。

28.2.7 设置继承@Inherited P888
添加@Inherited后，将看到@TestInterface出现在注释类的子类中。
当然，并不希望所有的注释类型都具有这种行为（因为默认值是不继承的）。如，TODO注释就不会（也不应该）被传播。
//------------------------------------------------------------------------------------------------
//Java核心编程技术 第29课 Java 5.0 语言新特性 P891
JDK 5.0 的11个特征中的两个重要特性——泛型和注释符。

29.1 自动装箱和拆箱（Boxing/Unboxing）P891
实现了基本数据类型与对象数据类型之间的隐式转换。
--基本数据类型至对象数据类型的转换称为自动装箱。
--对象数据类型至基本数据类型的转换称为自动拆箱。
这些类包括：
byte--Byte
short--Short
int--Integer
long--Long
float--Float
double--Double
char--Character
boolean--Boolean

//
package test;

import java.util.ArrayList;
import java.util.List;

public class TestAnnotation {
    public static void main(String args[]) {
        //旧的代码
        Integer intObject = new Integer(20);
        int intPrimitive = 10;
        List<Integer> list = new ArrayList<>();//int将提示primitive(原始)数据不能成为数据类型
        list.add(intObject);
        list.add(new Integer(intPrimitive));

        //如果将Integer类型转换为int，需要使用转换函数
        int intPrimitive2 = intObject.intValue();

        //新的代码
        list.add(intPrimitive);//自动装箱int-->Integer
        int intPrimitive3 = intObject;//自动拆箱Integer-->int
    }
}
该功能让我们在编码时不再需要进行显示转换，代码编写简单，符合编程思维。

29.2 枚举类（Enumeration Class） P892
通常在定义一些常用的键值时，会使用一些public static final int类型的常量来定义。
1. public static final int 的常量
如，要定义一系列颜色值：
public class Constants {
    public static final int RED = 1;
    public static final int YELLOW = 2;
    public static final int BLUE = 3;
    public static final int ORANGE = 4;
}

这种方法有一个隐患，就是这些常量的整型值不能相等。如，像下面这样引用这些变量：
public void paint(int color) {
    switch (color) {
    case Constants.RED:
        ;
    case Contants.YEELOW;
        ;
    case Constants.BLUE;
        ;
    case Constants.ORANGE;
        ;
    default:
        ;
    }
}
对于case分支，如果其中的颜色值有相等，则会出现错乱。为了解决这个问题，出现了枚举类。

2. 定义枚举类 P893
枚举类（enum）非常像public static final int声明，对int所做的最大也是最明显的改进是类型安全，你不能错误地用枚举的一种类型代替另一种类型，这和int不同，所有的int对编译器来说都是一样的。大部分情况下，可以使用enum对代码中所有的public static final int 做插入替换。它们是可比的，并且可以静态导入，所以对它们的引用看起来是等同的，即便是对于内部类（或内部枚举类型）。
可以像下面这样定义颜色列表：
enum Color {
    Red, Yellow, Blue, Orange;
}

像引用变量一样引用枚举变量：
Color color = Color.Red;//引用变量

对于以上paint()函数，枚举类这样使用：
public void paint(Color color) {
    switch (color) {
    case Red:
        ;
    case Yellow:
        ;
    default:
        ;
    }
}

case标签中的值可以引用自switch()中参数的枚举值，并可以使用values()取得枚举列表数组，遍历枚举值：
//
package test;

enum Color {
    Red, Green, Yellow, Orange;
}

public class TestEnum {
    public static void main(String args[]) {
        Color color = Color.Orange;

        switch (color) {
            case Red:
                System.out.println("Red");
                break;//需要加break来跳出switch
            case Green:
                System.out.println("Green");
                break;
            case Yellow:
                System.out.println("Yellow");
                break;
            default:
                System.out.println("Default color: " + color);
        }

        Color[] colors = Color.values();
        for (int i = 0; i < colors.length; ++i) {
            System.out.println(colors[i]);
        }
    }
}

3. 为枚举值添加参数 P894
枚举的变量名可以附带一个参数，用来表示一个附加的属性，如缩写等。参数类型可以是String、int等各种Java类型，形式为：
public enum Name {types(参数值), ...;}
如果附带了参数，必须为该参数添加一个变量名、构造函数和get函数，如下：
//枚举类型可以理解为就是一个类，可以有构造函数等各种函数
package test;

enum Color {
    Red("R"), Green("G"), Yellow("Y"), Orange;//如果这里Orange不提供参数，则需要有无参的构造函数Color()

    private String shortName;
    //这里会提示private是冗余的，可以不带限定符。但是如果使用public则报错public不允许
    private Color(String shortName) {
        this.shortName = shortName;
    }

    Color() {

    }

    public String getShortName() {
        return shortName;
    }
}

public class TestEnum {
    public static void main(String args[]) {
        Color[] colors = Color.values();
        for (int i = 0; i < colors.length; ++i) {
            System.out.println(colors[i]);
            System.out.println(colors[i].getShortName());
        }
    }
}
输出：
Red
R
Green
G
Yellow
Y
Orange
null

也可以用整数值来表示参数
enum Day {
    SUNDAY(0), MONDAY(1), ...;
    
    private int day;
    Day(int day) {
        this.day = day;
    }
    public int getDay() {
        return day;
    }
}

4. 枚举的映射（Map） P894
枚举提供了一些附加的特性，EnumMap和EnumSet这两个实用类是专门为枚举优化的标准集合实现。如果知道集合只包含枚举类型，应该使用这些专门的集合来代替HashMap或HashSet。
//
package test;

import java.util.EnumMap;

enum Color {
    Red, Green, Yellow, Orange;
}

public class TestEnum {
    public static void main(String args[]) {
        EnumMap<Color, String> map = new EnumMap<Color, String>(Color.class);
        map.put(Color.Red, "this is Red");
        map.put(Color.Yellow, "this is Yellow");
        map.put(Color.Green, "this is Green");
        map.put(Color.Orange, "this is Orange");

        Color[] colors = Color.values();
        for (int i = 0; i < colors.length; ++i) {
            System.out.println(map.get(colors[i]));
        }
    }
}
输出：
this is Red
this is Green
this is Yellow
this is Orange
这种做法好处是，可以保证Map列表中的键在枚举值的范围内，而不超出枚举值范围，EnumSet也是如此。
Tips：通常都应用用enum实例替换全部的枚举风格的public static final int结构，因为枚举是安全的。

29.3 可变参数（Variable Arguments） P895
使用3个点号表示一系列的同类型的参数，如下：
void func(Object... args)
3 个点号紧跟在对象类型Object后面，其中的Object可以是任意类型，包括Java基础数据类型和对象数据类型。
在引用该函数时，可以使用逗号分隔传递多个参数，同类型的参数都将作为该参数的一个值传入，如下所示：
func(arg1, arg2,..., argn);
传入的参数args实现上是一个Object类型的数组：
Object[] args;
因此可以像使用数组一样使用args变量。
Tips：由于可变参数的数量不定，因此可变参数只能作为函数的最后一个参数出现，因此函数最多只能有一个可变参数。

//
package test;

public class TestVarargs {
    public static int add(int... args) {
        int total = 0;
        for (int i = 0; i < args.length; ++i) {
            total += args[i];
        }
        return total;
    }

    public static void main(String args[]) {
        System.out.println(TestVarargs.add(1, 2));
        System.out.println(TestVarargs.add(1, 2, 3));
    }
}
输出：
3
6
Tips：正确地使用可变参数确实可以清理一些垃圾代码，它避免了往函数中传递一个长度不固定的数组，可以将待传递的数组中的各个值直接当做参数传递。

//可以参数也可以使用不同类型的参数，此时需要用Object来表示入参，并且需要对参数类型做判断
package test;

public class TestVarargs {
    public static void add(Object... args) {
        int total = 0;
        for (int i = 0; i < args.length; ++i) {
            Object o = args[i];
            if (o instanceof Integer) {
                int argInt = (int) o;
                System.out.println("Int: " + argInt);
            } else if (o instanceof String) {
                String argStr = (String) o;
                System.out.println("String: " + argStr);
            }

        }
    }

    public static void main(String args[]) {
        TestVarargs.add(1, 2);
        TestVarargs.add(3, "hello", "world");
    }
}
输出：
Int: 1
Int: 2
Int: 3
String: hello
String: world

29.4 可变返回类型（Covariant Return Types） P896
JDK5中可以改变被覆盖方法的返回类型。Covariant的意思是，在子类的重载方法中，其方法的返回值的范围比父类的对应类型缩小了，可以是父亲对应类型的子类型。
如下，B类继承自Number，其重载方法whoAreYou()返回的类型不再是Number，而是Number的子类型Integer，这样就缩小了重载方法的范围。
package test;

class Number {
    public Number whoAreYou() {
        return new Number();
    }
}

class Integer extends Number {
    @Override
    public Number whoAreYou() {
        return new Integer();
    }
}

class Float extends Number {
    @Override
    public Number whoAreYou() {
        return new Float();
    }
}

public class TestConvariant {
    public static void main(String args[]) {
        Float f = new Float();
        System.out.println(f.whoAreYou());
    }
}
Tips：这种做法的用意是，允许子类缩小返回类型的范围，使得各实现子类能够更加符合自身的意义。如对于父类Number，它的子类Integer、Float分别用于处理整型和浮点型数据，如果它们返回的值都是Number类型，那么对这两个子类来说，显示是不合适的，子类不应该提及父类，而是只处理自身范围内的数据才是合理的。

29.5 增强循环 Enhanced for Loop P897
通常对于数据列表：
List<String> list = new ArrayList<>();
list.add("how");
list.add("are");
list.add("you");
可以使用for语句来循环该列表：
for (int i = 0; i < list.size(); ++i) {
    String str = list.get(i);
    System.out.println(str);
}
或使用while语句来输出迭代对象：
Iterator<String> iter = list.iterator();
while (iter.hasNext()) {
    String str = iter.next();
    System.out.println(str);
}

JDK 5.0 中提供了增强的for循环功能，可以使用如下形式输出：
for (Object o : c) {
    //
}

此时可以如下输出：
for (String str : list) {
    System.out.println(str);
}
与旧的for循环相比，str对象不需要从list中使用get()取得了，不需要进行指针的累加；与while循环相比，不需要生成迭代对象，也不需要使用next()来循环指针。这样做的好处很显然，只需要理解str是list集合中的对象即可，也完全符合我们的正常思维过程。
将这种特殊的for语句格式称之为“foreach”语句，foreach并不是一个关键字。从英文字面理解foreach也就是“for 每一个”的意思。

foreach语句是for语句的特殊简化版本，但是foreach语句并不能完全取代for语句，然而，任何的foreach语句都可以改写为for语句版本。对于数组、列表、集合类型的数据对象都可以使用这种简化方式。上面的例子可以发现，如果要引用数组或者集合的索引，则foreach语句无法做到，foreach仅仅老老实实的遍历数组或者集合一遍。
Tips：foreach语句是for语句特殊情况下的增强版本，简化了编程，提高了代码的可读性和安全性（不用怕数组越界）。相对老的for语句来说是个很好的补充。提供能用foreach的地方就不要用for了。在用到对集合或者数组索引的情况下，foreach显得力不从心，这个时候是用for语句的时候了。

20.6 静态导入 Static Import P898
Java编程中，通常将固定的函数和变量写成静态的，可以直接通过类名来引用该函数和变量，不需要通过构造类的实现来实现它。如对于Math类的静态方法，可能这样引用：
//旧的方式
package test;

public class TestVarargs {
    public static void main(String args[]) {
        double d = Math.sqrt(Math.pow(3, 2) + Math.pow(4, 2));
        System.out.println(d);
    }
}

对于每一个方法，都需要使用类名来引用，在JDK 5.0 中可以使用静态导入一次性引入，语法格式如下：
import static java.lang.Math.*;
该语句放在类文件的import区域，使用关键字为“import static”，类名后需要添加“.*”，表示引入了所有的静态函数，也可以指定具体的函数名。
package test;

import static java.lang.Math.pow;
import static java.lang.Math.*;

public class TestVarargs {
    public static void main(String args[]) {
        double d = Math.sqrt(Math.pow(3, 2) + Math.pow(4, 2));
        System.out.println(d);

        //新的方式
        double d2 = sqrt(pow(1, 2) + pow(1, 2));
        System.out.println(d2);
    }
}
需要注意的是，默认包无法用静态导入，另外如果导入的类中有重复的方法和属性则需要写出类名，否则编译时无法通过。

29.7 控制台输入 （Console Input） P898
JDK 5.0 中可以通过java.utils.Scanner在控制台进行输入操作。
Scanner是一个可以使用正则表达式来解析基本类型和字符串的简单文本扫描器。Scanner使用分隔符模式将其输入分解为标记，在默认情况下该分隔符模式与空白匹配。然后可以使用不同的next方法将得到的标记转换为不同类型的值。
1. 读取控制台
以下代码使用户能够从System.in中读取一个数：
package test;

import java.util.Scanner;

public class TestVarargs {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);//输入非数字的字符将抛异常
        int i = sc.nextInt();
        System.out.println(i);
    }
}

2. 读取文件
//test.txt
123
456
abc
789

//
package test;

import java.io.File;
import java.util.Scanner;

public class TestVarargs {
    public static void main(String args[]) {
        try {
            Scanner sc = new Scanner(new File("src/test/test.txt"));//路径一定要从src下开始指定
            while (sc.hasNextLong()) {//读到不是long的对象后就退出
                long l = sc.nextLong();
                System.out.println(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
输出：
123
456

3. 读取字符串
扫描器还可以使用不同于空白的分隔符，下面是从一个字符串读取若干项的例子：
package test;

import java.util.Scanner;

public class TestVarargs {
    public static void main(String args[]) {
        String input = "1 fish   2 fish   red fish blue fish  ";
        Scanner s = new Scanner(input).useDelimiter("\\s*fish\\s*");//\s匹配任何不可见字符，包括空格、制表符、换页符等等。等价于[ \f\n\r\t\v]。
        System.out.println(s.nextInt());
        System.out.println(s.nextInt());
        System.out.println(s.next());
        System.out.println(s.next());
    }
}
输出
1
2
red
blue

以下代码使用正则表达式同时解析所有的4个标记，并可以产生与上例相同的输出结果：
package test;

import java.util.Scanner;
import java.util.regex.MatchResult;

public class TestVarargs {
    public static void main(String args[]) {
        String input = "1 fish   2 fish   red fish blue fish  ";
        Scanner s = new Scanner(input);
        s.findInLine("(\\d+)\\s*fish\\s*(\\d+)\\s*fish\\s*(\\w+)\\s*fish\\s*(\\w+)");
        MatchResult result = s.match();
        for (int i = 1; i <= result.groupCount(); ++i) {
            System.out.println(result.group(i));
        }
    }
}

Tips：该类为java.utils.Scanner，更多使用可以参数Java API文档。

29.8 StringBuilder类 P900

29.9 格式化I/O （Formatted I/O） P900
//------------------------------------------------------------------------------------------------
//Effective Java 第1章 引言 P1
不严格地讲，一个包的导出的API是由该包中的每个公有（public）类或者接口中所有公有的或者受保护的（protected）成员和构造器组成。
//------------------------------------------------------------------------------------------------
//Effective Java 第2章 创建和销毁对象 P4
//第1条：考虑用静态工厂方法代替构造器 P4
获取一个实例，类可以提供一个公有的静态工厂方法（static factory method），它只是一个返回类的实例的静态方法。下面是一个Boolean（基本类型boolean的包装类）的简单示例，这个方法将boolean基本类型值转换成了一个Boolean对象引用：
public static Boolean valueOf(boolean b) {
    return b ? Boolean.TRUE : Boolean.FALSE;
}

类可以通过静态工厂方法来提供它的客户端，而不是通过构造器。提供静态工厂方法而不是公有的构造器，这样做具有几大优势。
--静态工厂方法与构造器不同的第一大优势在于，它们有名称。如果构造器的参数本身没有确切地描述正被返回的对象，那么具有适当名称的静态工厂会更容易使用，产生的客户端代码也更易于阅读。
一个类只能有一个带有指定签名的构造器。
由于静态工厂方法有名称，所以它们不受上述的限制。当一个类需要多个带有相同签名的构造器时，就用静态工厂方法代替构造器，并且慎重地选择名称以便突出它们之间的区别。

--静态工厂方法与构造器不同的第二大优势在于，不必每次调用它们的时候都创建一个新对象。如果程序经常请求创建相同的对象，并且创建对象的代价很高，则这项技术可以极大地提升性能。
静态工厂方法能够为重复的调用返回相同对象，这样有助于类总能严格控制在某个时刻哪些实例应该存在。这种类被称作实例受控的类（instance-controlled）。编写实例受控的类有几个原因。可以确认它是一个Singleton（见第3条）或者是不可实例化的（见第4条）。它还使得不可变的类（见第15条）可以确保不会存在两个相等的实例，即当且仅当a==b时才有a.equals(b)为true。如果类保证了这一点，它的客户端就可以使用==操作符来代替equals(Object)方法，这样可以提升性能。枚举（enum）类型（见第30条）保证了这一点。

--静态工厂方法与构造器不同的第三大优势在于，它们可以返回原返回类型的任何子类型的对象。这样我们在选择返回对象的类时就有了更大的灵活性。

服务提供者框架模式有着无数种变体。如，服务访问API可以利用适配器（Adapter）模式，返回比提供者需要的更丰富的服务接口。下面是一个简单的样例，包含一个服务提供者接口和一个默认提供者：
//
package test;

public interface Service {
    String name();
}

class ServiceA implements Service {
    private String name = "ServiceA";

    @Override
    public String name() {
        return name;
    }
}

class ServiceB implements Service {
    private String name = "ServiceB";

    @Override
    public String name() {
        return name;
    }
}

//
package test;

public interface Provider {
    Service newService();
}

class ProviderA implements Provider {
    @Override
    public Service newService() {
        return new ServiceA();
    }
}

class ProviderB implements Provider {
    @Override
    public Service newService() {
        return new ServiceB();
    }
}

//
package test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Services {
    private Services() {
    }

    private static final Map<String, Provider> providers = new ConcurrentHashMap<>();
    public static final String DEFAULT_PROVIDER_NAME = "<default>";

    //Provider registration API
    public static void registerDefaultProvider(Provider p) {
        registerDefaultProvider(DEFAULT_PROVIDER_NAME, p);
    }

    public static void registerDefaultProvider(String name, Provider p) {
        providers.put(name, p);
    }

    //Service access API
    public static Service newInstance() {
        return newInstance(DEFAULT_PROVIDER_NAME);
    }

    public static Service newInstance(String name) {
        Provider p = providers.get(name);
        if (p == null) {
            throw new IllegalArgumentException("No provider registered with name: " + name);
        }
        return p.newService();
    }
}

//
package test;

public class TestServices {
    public static void main(String args[]) {
        Services.registerDefaultProvider("A", new ProviderA());
        Services.registerDefaultProvider("B", new ProviderB());

        Service s = Services.newInstance("A");
        System.out.println(s.name());
    }
}
输出：
ServiceA

--静态工厂方法的第四大优势在于，在创建参数化类型实例的时候，它们使代码变得更加简洁。在调用参数化类的构造器时，即便类型参数很明显，也必须指明。这通常要求接续两次提供类型参数：
Map<String, List<String>> m = new HashMap<String, List<String>>();//在1.8中已经可以省略第二个参数了
有了静态工厂方法，编译器就可以替你找到类型参数。这被称作类型推导（type inference）。如，假设HashMap提供了这个静态工厂：
public static <K, V> HashMap<K, V> newInstance() {
    return new HashMap<K, V>();
}
就可以用下面这句简洁的代码代替上面这段繁琐的声明：
Map<String, List<String>> m = HashMap.newInstance();//当前1.8中还未添加此静态方法

--静态方法的主要缺点在于，类如果不含公有的或者受保护的构造器，就不能被子类化。对于公有的静态工厂所返回的非公有类，也同样如此。如，想要将Collections Framework中的任何方便的实现类子类化，这是不可能的。这样也因祸得福，因为它鼓励程序员使用复合（composition），而不是继承（见第16条）。

--静态工厂方法的第二个缺点在于，它们与其他的静态方法实际上没有任何区别。在API文档中，它们没有像构造器那样在API文档中明确标识出来，因此，对于提供了静态工厂方法而不是构造器的类来说，要想查明如何实例化一个类，这是非常困难的。

简而言之，静态工厂方法和公有构造器都各有用处，需要理解它们各自的长处。静态工厂通常更加合适，因此切忌第一反应就是提供公有的构造器，而不先考虑静态工厂。
//------------------------------------------------------------------------------------------------
//Effective Java 第2章 创建和销毁对象 P4
//第2条：遇到多个构造器参数时要考虑用构建器 P9
静态工厂和构造器有个共同的局限性：它们都不能很好地扩展到大量的可选参数。考虑用一个类表示包装食品外面显示的营养成份标签。有大量的域。
对于这样的类，应该用哪种构造器或者静态方法来编写？程序员一向习惯采用重叠构造器（telescoping constructor）模式，在这种模式下，提供第一个只有必要参数的构造器，第二个构造器有一个可选参数，第三个有两个可选参数，依此类推，最后一个构造器包含所有可选参数。下面示例，为简单起见，只显示四个可选域：
//
package test;
//Telescoping constructor pattern - does not scale well!
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public NutritionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories) {
        this(servingSize, servings, calories, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium) {
        this(servingSize, servings, calories, fat, sodium, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }

    @Override
    public String toString() {
        return "[ServingSize:" + servingSize + "]@[Serving:" + servings + "]@[Calories:" + calories
                + "]@[Fat:" + fat + "]@[Sodium:" + sodium + "]@[Carbohydrate:" + carbohydrate + "]";
    }

    public static void main(String args[]) {
        NutritionFacts cocacola = new NutritionFacts(240, 8, 100, 0, 35, 27);
        System.out.println("Cocacola:" + cocacola);

        NutritionFacts bread = new NutritionFacts(100, 20);
        System.out.println("Bread:" + bread);
    }
}
输出：
Cocacola:[ServingSize:240]@[Serving:8]@[Calories:100]@[Fat:0]@[Sodium:35]@[Carbohydrate:27]
Bread:[ServingSize:100]@[Serving:20]@[Calories:0]@[Fat:0]@[Sodium:0]@[Carbohydrate:0]

这个构造器调用通常需要许多你本不想设置的参数，但还是不得不为它们传递值。这个例子中，我们给fat传递了一个值为0。随着参数数目的增加，它很快就失去了控制。
一句话：重叠构造器模式可行，但是当有许多参数的时候，客户端代码会很难编写，并且仍然较难以阅读。

遇到很多构造器参数的时候，还有第二种代替方法，即JavaBeans模式，在这种模式下，调用一个无参构造器来创建对象，然后调用setter方法来设置每个必要的参数，以及每个相关的可选参数：
//
package test;
//JavaBeans Pattern - allows inconsistency, mandates mutability
public class NutritionFacts {
    private int servingSize = -1;//如果用setter，则这里不能有final了，final同C++里的const一样，是不能修改的，只能在构造函数初始化
    private int servings = -1;
    private int calories;
    private int fat;
    private int sodium;
    private int carbohydrate;

    public NutritionFacts() {
    }

    //Setters
    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    @Override
    public String toString() {
        return "[ServingSize:" + servingSize + "]@[Serving:" + servings + "]@[Calories:" + calories
                + "]@[Fat:" + fat + "]@[Sodium:" + sodium + "]@[Carbohydrate:" + carbohydrate + "]";
    }

    public static void main(String args[]) {
        NutritionFacts cocacola = new NutritionFacts();
        cocacola.setCalories(100);
        System.out.println("Cocacola:" + cocacola);

        NutritionFacts bread = new NutritionFacts();
        System.out.println("Bread:" + bread);
    }
}
输出：
Cocacola:[ServingSize:-1]@[Serving:-1]@[Calories:100]@[Fat:0]@[Sodium:0]@[Carbohydrate:0]
Bread:[ServingSize:-1]@[Serving:-1]@[Calories:0]@[Fat:0]@[Sodium:0]@[Carbohydrate:0]

这种模式弥补了重叠构造器模式的不足。创建实例很容易，产生的代码读起来也容易。
但是，JavaBeans模式自身有着很严重的缺点。因为构造过程被分到了几个调用中，在构造过程中JavaBean可能处于不一致和状态。类无法仅仅通过检验构造器参数的有效性来保证一致性。试图使用处于不一致状态的对象，将会导致失败，这种失败与包含错误的代码大相径庭，因此它调试起来十分困难。另一点不足在于，JavaBeans模式阻止了把类做成不可变的可能（见第15条），这就需要程序员付出额外的努力来确保它的线程安全。

幸运的是，还有第三种替代方法，既能保证像重叠构造器模式那样的安全性，也能保证像JavaBeans模式那么好的可读性。这就是Builder模式。不直接生成想要的对象，而是让客户端利用所有必要的参数调用构造器（或者静态工厂），得到一个builder对象。然后客户端在builder对象上调用类似于setter方法，来设置每个相关的可选参数。最后，客户端调用无参的builder方法来生成不可变的对象。这个builder是它构建的类的静态成员类（见第22条）。下面示例：
package test;

//Builder Pattern
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    //静态内部类，可以不依赖于外部类实例存在 StaticInnerClass staticInnerClass = new OuterClass.StaticInnerClass();
    //非静态内部类，必须依赖外部类实例对象初始化。NoneStaticInnerClass noneStaticInnerClass = outerClass.new NoneStaticInnerClass();
    public static class Builder {
        //Required parameters
        private final int servingSize;
        private final int servings;

        //Optional parameters - initialized to default values
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int val) {
            this.calories = val;
            return this;
        }

        public Builder fat(int val) {
            this.fat = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            this.carbohydrate = val;
            return this;
        }

        public Builder sodium(int val) {
            sodium = val;
            return this;
        }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        servingSize = builder.servingSize;
        servings = builder.servings;
        calories = builder.calories;
        fat = builder.fat;
        sodium = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }

    @Override
    public String toString() {
        return "[ServingSize:" + servingSize + "]@[Serving:" + servings + "]@[Calories:" + calories
                + "]@[Fat:" + fat + "]@[Sodium:" + sodium + "]@[Carbohydrate:" + carbohydrate + "]";
    }

    public static void main(String args[]) {
        NutritionFacts cocacola = new NutritionFacts.Builder(240, 0).calories(100).sodium(35).carbohydrate(27).build();
        System.out.println("Cocacola:" + cocacola);

        NutritionFacts bread = new Builder(100, 30).build();//这里可以直接new Builder()
        System.out.println("Bread:" + bread);
    }
}
输出：
Cocacola:[ServingSize:240]@[Serving:0]@[Calories:100]@[Fat:0]@[Sodium:35]@[Carbohydrate:27]
Bread:[ServingSize:100]@[Serving:30]@[Calories:0]@[Fat:0]@[Sodium:0]@[Carbohydrate:0]

注意NutritionFacts是不可变的，所有的默认参数值都单独放在一个地方。builder的setter方法返回builder本身，以便可以把调用连接起来。
这样的客户端代码很容易编写，更为重要的是，易于阅读。builder模式模拟了具名的可选参数。

builder像个构造器一样，可以对其参数强加约束条件。build()方法可以检验这些约束条件。将参数从builder拷贝到对象中后，并在对象域而不是builder域（见第39条）中对它们进行校验，这一点很重要。如果违反了任何约束条件，build()方法就应该抛出IllegalStateException（见第60条）。异常的详细信息应该显示出违反了哪个约束条件（见第63条）。
对多个参数强加约束条件的另一种方法是，用多个setter方法对某个约束条件必须持有的所有参数进行检查。如果该约束条件没有得到满足，setter方法就会抛出IllegalArgumentException。这有个好处，就是一旦传递了无效的参数，立即就会发现约束条件失败，而不是等着调用build()方法。
与构造器相比，builder的微略优势在于，builder可以有多个可变（varargs）参数。构造器就像方法一样，只能有一个可变参数。因为builder利用单独的方法来设置每个参数，想要多少个可变参数，就可以有多少个，直到每个setter方法都有一个可变参数。
Builder模式十分灵活，可以利用单个builder构造多个对象。builder的参数可以在创建对象期间进行调整，也可以随着不同的对象而改变。builder可以自动填充某些域，例如每次创建对象时自动增加序列号。

设置了参数的builder生成了一个很好的抽象工厂（Abstract Factory）。客户端可以将这样一个builder传给方法，使该方法能够为客户端创建一个或者多个对象。要使用这种用法，需要有个类型来表示builder。1.5 及之后版本，只要一个泛型（见第26条）就能满足所有builder，无论它们在构造哪种类型的对象：
//A builder for objects of type T
public interface Builder<T> {
    public T build();
}
可以声明NutritionFacts.Builder类来实现Builder<NutritionFacts>。

Class.newInstance破坏了编译时的异常检查。上面讲过的Builder接口弥补了这些不足。

Builder模式的确也有不足。为了创建对象，必须先创建它的构造器。虽然创建构建器的开销在实践中可能不那么明显，但是在某些十分注意性能的情况下，可能就成问题了。Builder模式还比重叠构造器模式更加冗长，因此它只在有很多参数的时候才使用，比如4个或者更多个参数。但是记住，将来你可能需要添加参数。如果一开始就使用构造器或者静态工厂，等到类需要多个参数时才添加构建器，就会无法控制，那些过时的构造器或者静态工厂显得十分不协调。因此，通常最好一开始就使用构建器。

简而言之，如果类的构造器或者静态工厂中具有多个参数，设计这种类时Builder模式就是种不错的选择，特别是当大多数参数都是可选的时候。与使用传统的重叠构造器模式相比，使用Builder模式的客户端代码将更易于阅读和编写，构建器也比JavaBeans更加安全。

//------------------------------------------------------------------------------------------------
//Effective Java 第2章 创建和销毁对象 P4
//第3条：用私有构造器或者枚举类型强化Singleton属性 P14
Singleton指仅仅被实例化一次的类。Singleton通常被用来代表那些本质上唯一的系统组件，比如窗口管理器或者文件系统。使类成为Singleton会使它的客户端测试变得十分困难，因为无法给Singleton替换模拟实现，除非它实现一个充当其类型的接口。
//Singleton with public final field
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() {}

    public void leaveTheBuilding() {}
}

私有构造器仅被调用一次，用来实例化公有的静态final域Elvis.INSTANCE。要提醒一点：享有特权的客户端可以借助AccessibleObject.setAccessible方法，通过反射机制（见第53条）调用私有构造器。如果需要抵御这种苏南，可以修改构造器，让它在被要求创建第二个实例的时候抛出异常。

在实现Singleton的第二种方法中，公有的成员是个静态工厂方法：
//Singleton with static factory
public class Elvis {
    private static final Elvis INSTANCE = new Elvis();
    private Elvis() {}
    public static Elvis getInstance() { return INSTANCE;}

    public void leaveTheBuilding() {}
}
对于静态方法Elvis.getInstance的所有调用，都会返回同一个对象引用，所以，永远不会创建其他的Elvis实例（上述提醒依然适用）。

公有域方法的主要好处在于，组成类的成员的声明很清楚地表明了这个类是一个Singleton：公有的静态域是final的，所以该域将总是包含相同的对象引用。公有域方法在性能不上再有任何优势：现代的JVM实现几乎都能够将静态工厂方法的调用内联化。
工厂方法的优势之一在于，它提供了灵活性：在不改变其API的前提下，可以改变该类是否应该为Singleton的想法。工厂方法返回该类的唯一实例，但是，它可以很容易被修改，如改成每个调用该方法的线程返回一个唯一的实例。第二个优势与泛型（见第27条）有关。这些执行之间通常都不相关，public域（public-field）的方法比较简单。

为了使用利用这其中一种方法实现的Singleton类变成可序列化的，必须声明所有实例域都是瞬时（transient）的，并提供一个readResolve方法（见第77条）。否则，每次反序列化一个序列化的实例时，都会创建一个新的实例，在我们的例子中，会导致“假冒的Elvis”。为防止这种情况，要在Elvis类中加入下面这个readResolve方法：
private Object readResolve() {
    return INSTANCE;
}

从Java 1.5 发行版本起，实现Singleton还有第三种方法。只需编写一个包含单个元素的枚举类型：
//
package test;

public enum Elvis {
    INSTANCE;

    private int i = 3;

    public void set(int i) {
        this.i = i;
    }

    public int get() {
        return i;
    }
}

//
package test;

public class TestSingleton {
    public static void main(String args[]) {
        Elvis elvis = Elvis.INSTANCE;
        System.out.println(elvis.get());

        Elvis elvis1 = Elvis.INSTANCE;
        elvis1.set(5);

        System.out.println(elvis.get());
    }
}
输出：
3
5

这种方法在功能上与公有域方法相近，但是它更加简洁，无偿地提供了序列化机制，绝对防止多次实例化，即便是在面对复杂的序列化或者反射攻击的时候。虽然还没有广泛采用，但是元素的枚举类型已经成为实现Singleton的最佳方法。
//------------------------------------------------------------------------------------------------
//Effective Java 第2章 创建和销毁对象 P4
//第4条：通过私有构造器强化不可实现化的能力 P16

企图通过将类做成抽象类来强制该类不可被实例化，这是行不通的。该类可以被子类化，并且该子类也可以被实例化。这样做甚至会误导用户，以为这种类是专门为了继承而设计的（见第17条）。由于只有当类不包含显式的构造器时，编译器才会生成缺省的构造器，因此只要让这个类包含私有构造器，它就不能被实例化了：
//Noninstantiable utility class
public class UtilityClass {
    //Suppress default constructor for noninstantiability
    private UtilityClass() {
        throw new AssertionError();
    }
}

显式构造器是私有的，所以不可以在该类的外部访问它。AssertionError不是必需的，但是它可以避免不小心在类的内部调用构造器。它保证该类在任何情况下都不会被实例化。这种习惯用法有点违背直觉，好像构造器专门设计成不能被调用一样。因此，明智的做法就是在代码中增加一条注释，如上。
这种习惯用法也有副作用，它使得一个类不能被子类化。所有的构造器都必须显式或隐式地调用超类（superclass）构造器，在这种情况下，子类就没有可访问的超类构造器可调用了。
//------------------------------------------------------------------------------------------------
//Effective Java 第2章 创建和销毁对象 P4
//第5条：避免创建不必要的对象 P17
String s = new String("stringette");//不要这样写！
该语句每次执行的时候都创建一个新的String实例，但是这些创建对象的动作都是不必要的。传递给String构造器的参数（“stringette”）本身就是一个String实例，功能方面等同于构造器创建的所有对象。如果这种用法是在一个循环中，或者是在一个被频繁调用的方法中，就会创建出成千上万不必要的String实例。

改进后的版本如下：
String s = "stringette";
该版本只用了一个String实例，而不是每次执行的时候都创建一个新的实例。而且，它可以保证，对于所有同一台虚拟机中运行的代码，只要它们包含相同的字符串字面常量，该对象就会被重用。

对于同时提供了静态工厂方法（见第1条）和构造器的不可变类，通常可以使用静态工厂方法而不是构造器，以避免创建不必要的对象。如，静态工厂方法Boolean.valueOf(String)几乎总是优先于构造器Boolean(String)。构造器在每次调用的时候都会创建一个新的对象，而静态工厂方法则从来不要求这样做，实际上也不会这样做。

//
package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Person {
    private final Date birthDate;

    public Person(Date birthDate) {
        this.birthDate = birthDate;
    }

    //判断是否是出生峰年，不好的写法
    public boolean isBabyBoomer() {
        Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        gmtCal.set(1946, Calendar.JANUARY, 1, 0, 0, 0);
        Date boomStart = gmtCal.getTime();

        gmtCal.set(1965, Calendar.JANUARY, 1, 0, 0, 0);
        Date boomEnd = gmtCal.getTime();

        return birthDate.compareTo(boomStart) >= 0 && birthDate.compareTo(boomEnd) < 0;
    }

    public static void main(String args[]) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = simpleDateFormat.parse("1943-01-01");
            Person xiaoMing = new Person(date);
            System.out.println("XiaoMing is in boom birth year: " + xiaoMing.isBabyBoomer());

            Date date1 = simpleDateFormat.parse("1947-01-01");
            Person xiaoLi = new Person(date1);
            System.out.println("XiaoLi is in boom birth year: " + xiaoLi.isBabyBoomer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
输出：
XiaoMing is in boom birth year: false
XiaoLi is in boom birth year: true

isBabyBoomer每次被调用的时候，都会创建一个Calendar，一个TimeZone和两个Date实例，这是不必要的。下面的版本用一个静态的初始化器（initializer），避免了这种效率低下的情况：
//
package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Person {
    private final Date birthDate;
    private static final Date BOOM_START;
    private static final Date BOOM_END;

    public Person(Date birthDate) {
        this.birthDate = birthDate;
    }

    static {
        Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        gmtCal.set(1946, Calendar.JANUARY, 1, 0, 0, 0);
        BOOM_START = gmtCal.getTime();

        gmtCal.set(1965, Calendar.JANUARY, 1, 0, 0, 0);
        BOOM_END = gmtCal.getTime();
    }

    //判断是否是出生峰年，好的写法
    public boolean isBabyBoomer() {
        return birthDate.compareTo(BOOM_START) >= 0 && birthDate.compareTo(BOOM_END) < 0;
    }

    public static void main(String args[]) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = simpleDateFormat.parse("1943-01-01");
            Person xiaoMing = new Person(date);
            System.out.println("XiaoMing is in boom birth year: " + xiaoMing.isBabyBoomer());

            Date date1 = simpleDateFormat.parse("1947-01-01");
            Person xiaoLi = new Person(date1);
            System.out.println("XiaoLi is in boom birth year: " + xiaoLi.isBabyBoomer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

改进后的Person类只在初始化的时候创建Calendar、TimeZone和Date实例一次，而不是在每次调用isBabyBoomer的时候都创建这些实例。如果isBabyBoomer方法被频繁地调用，这种方法将会显著地提高性能。代码的含义也更加清晰了。

如果改进后的Person类被初始化了，它的isBabyBoomer方法却永远不会被调用，那就没有必要初始化BOOM_START和BOOM_END域。通过延迟初始化（lazily initializing）（见第71条），即把对这些域的初始化延迟到isBabyBoomer方法第一次被调用的时候进行，则有可能消除这些不必要的初始化工作，但是不建议这样做。正如延迟初始化中常见的情况一样，这样做会使方法的实例更加复杂，从而无法将性能显著提高到超过已经达到的水平（见第55条）。

Java 1.5 发行版本中，有一种创建多余对象的新方法，称作自动装箱（autoboxing），允许程序员将基本类型和装箱基本类型（Boxed Primitive Type）混用。按需要自动装箱和拆箱。自动装箱使得基本类型和装箱基本类型之间的差别变得模糊起来，但是并没有完全消除。在语义上还有着微妙和差别，在性能上也有着比较明显的差别（见第49条）。下面程序，计算所有int正值的总和，程序必须使用long。
//
package test;

public class TestLong {
    public static void main(String args[]) {
        Long sum = 0L;

        long startMillisTime = System.currentTimeMillis();
        System.out.println("startMillisTime=" + startMillisTime);

        for (int i = 0; i < Integer.MAX_VALUE; ++i) {
            sum += i;
        }
        System.out.println("Sum of all integers is:" + sum);

        long endMillisTime = System.currentTimeMillis();
        System.out.println("endMillisTime=" + endMillisTime);
        System.out.println("MillisTime(ms): " + (endMillisTime - startMillisTime));
    }
}
输出：
startMillisTime=1476720305497
Sum of all integers is:2305843005992468481
endMillisTime=1476720315801
MillisTime(ms): 10304

程序算出的答案是正确的，但是慢一些，只因为打错了一个字符。变量sum被声明成Long而不是long，意味着程序构造了大约2^31 个多余的Long实例。将sum的声明从Long改成long，运行时间从10秒减少到1秒。

将Long sum = 0L;修改为long sum = 0L;
输出：
startMillisTime=1476720390027
Sum of all integers is:2305843005992468481
endMillisTime=1476720391503
MillisTime(ms): 1476

结论很明显：要优先使用基本类型而不是装箱基本类型，要当心无意识的自动装箱。

不要错误地认为本条目所介绍的内容暗示着“创建对象的代价非常昂贵，我们应该要尽可能地避免创建对象”。相反，由于小对象的构造器只做很少量的显式工作，所以，小对象的创建和回收动作是非常廉价的，特别是在现代的JVM实现上更是如此。通过创建附加的对象，提升程序的清晰性、简洁性和功能性，这通常是件好事。

反之，通过维护自己的对象池（object pool）来避免创建对象并不是一种好的做法，除非池中的对象是非常重量级的。真正正确使用对象池的典型对象示例就是数据库连接池。但是，一般而言，维护自己的对象池改写会把代码弄得很乱，同时增加内存占用（footprint），并且还会损害性能。现代的JVM实现具有高度优化的垃圾回收器，其性能很容易就会超过轻量级对象池的性能。

与本条目对应的是第39条中有关“保护性拷贝（defensive copying）”的内容。本条目提及“当你应该重用现有对象的时候，请不要创建新的对象”，而第39条则说“当你应该创建新对象的时候，请不要重用现有的对象”。注意，在提倡使用保护性拷贝的时候，因重用对象而付出的代价要远远大于因创建重复对象而付出的代价。必要时如果没能实施保护性拷贝，将会导致潜在的错误和安全漏洞，而不必要地创建对象则只会影响程序的风格和性能。
//------------------------------------------------------------------------------------------------
//Effective Java 第2章 创建和销毁对象 P4
//第6条：消除过期的对象引用 P21
转到具有垃圾回收功能的语言，认为自己不再需要考虑内存管理的事情了，其实不然。
考虑下面简单的栈实现例子：
//
package test;

import java.util.Arrays;
import java.util.EmptyStackException;

public class Stack {
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Object[] elements;//private Object[] elements = new Object[DEFAULT_INITIAL_CAPACITY];//由于DEFAULT_INITIAL_CAPACITY是static，所以优先于elements初始化好，如果将static去掉，则DEFAULT_INITIAL_CAPACITY必须先于elements定义

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];//这里的new初始化可以在上面elements定义的地方直接初始化好
    }

    public void push(Object o) {
        ensureCapacity();
        elements[size++] = o;
    }

    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        return elements[--size];
    }

    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}

测试用例：
package test;

import org.junit.Test;

import static org.junit.Assert.*;

public class StackTest {
    @Test
    public void testPush_should_returnSize_when_pushElement() {
        Stack stack = new Stack();
        stack.push("hello");
        assertEquals(1, stack.size());
    }

    @Test
    public void testPop_should_returnElement_when_pop() {
        Stack stack = new Stack();

        String hello = "hello";
        String world = "world";

        stack.push(hello);
        stack.push(world);

        assertEquals(2, stack.size());
        assertEquals(world, stack.pop());
        assertEquals(1, stack.size());
    }
}

这段程序（它的泛型版本见第26条）中并没有很明显的错误。但这个程序中隐藏着一个问题，不严格地讲，这段程序有一个“内存泄漏”，随着垃圾回收器活动的增加，或者由于内存占用的不断增加，程序性能的降低会逐渐表现出来。极端的情况下，这种内存泄漏会导致磁盘交换（Disk Paging），甚至导致程序失败（OutOfMemoryError错误），但这种失败情形相对比较少见。

发生内存泄漏的地方是：如果一个栈先增长，然后再收缩，那么从栈中弹出来的对象将不会被当作垃圾回收，即便使用栈的程序不再引用这些对象，它们也不会被回收。这是因为，栈内部维护着对这些对象的过期引用（obsolete reference）。所谓的过期引用，是指永远也不会再被解除的引用。在本例中，凡是在elements数组的“活动部分（active portion）”之外的任何引用都是过期的。活动部分是指elements中下标小于ize的那些元素。

在支持垃圾回收的语言中，内存泄漏是很隐蔽的（称这类内存泄漏为“无意识的对象保持（unintentional object retention）”更为恰当）。如果一个对象引用被无意识地保留起来了，那么，垃圾回收机制不仅不会处理这个对象，而且也不会处理被这个对象所引用的所有其他对象。

修复的方法很简单：一旦对象引用已经过期，只需清空这些引用即可。对于上述例子中的Stack类，只要一个单元被弹出栈，指向它的引用就过期了，pop方法修改如下：
public Object pop() {
    if (size == 0) {
        throw new EmptyStackException();
    }
    Object result = elements[--size];
    elements[size] = null;
    return result;
}

清空过期引用的另一个好处是，如果它们以后又被错误地解除引用（这里是不是应该是引用），程序就会立即抛出NullPointerException异常，而不是悄悄地错误运行下去。尽快地检测出程序中的错误总是有益的。

清空对象引用应该是一种例外，而不是一种规范行为。消除过期引用最好的方法是让包含该引用的变量结束其生命周期。如果你是在最紧凑的作用域范围内定义每一个变量（见第45条），这种情形就会自然而然地发生。

一般而言，只是类是自己管理内存，程序员就应该警惕内存泄漏问题。一旦元素被释放掉，则该元素中包含的任何对象引用都应该被清空。

内存泄漏的另一个常见来源是缓存。如果正好要实现这样的缓存：只要在缓存之外存在对某个项的键的引用，该项就有意义，那么就可以用WeakHashMap代表缓存；当缓存中的项过期之后，它们就会自动被删除。记住只有当所要的缓存项的生命周期是由该键的外部引用而不是由值决定时，WeakHashMap才有用处。（是说键被其余地方引用时，WeakHashMap才会保留该项，如果键只是被WeakHashMap引用时，过段时间仍会删除该项？）

缓存应该时不时地清除掉没用的项。这项清除工作可以由一个后台线程（可能是Timer或者ScheduledThreadPoolExcutor）来完成，或者也可以在给缓存添加新条目的时候顺便进行清理。LinkedHashMap类利用它的removeEldestEntry方法可以很容易地实现后一种方案。对于更加复杂的缓存，必须直接使用java.lang.ref。

在内泄漏的第三个常见的来源是监听器和其他回调。如果实现了一个API，客户端在这个API中注册回调，却没有显式地取消注册，那么除非你采取某些动作，否则它们就会积聚。确保回调立即被当作垃圾回收的最佳方法是只保存它们的弱引用（weak reference），如，只将它们保存成WeakHashMap中的键。（学习一下WeakHashMap的功能？）
//------------------------------------------------------------------------------------------------
//Effective Java 第2章 创建和销毁对象 P4
//第7条：避免使用终结方法 P24
终结方法（finalizer）通常是不可预测的，也是很危险的，一般情况下是不必要的。使用终结方法会导致行为不稳定、降低性能，以及可移植性问题。终结方法也有其可用之处，但是根据经验，应该避免使用终结方法。

C++程序员被告知“不要把终结方法当作是C++中析构器（destructors）的对应物”。在C++中，析构器是回收一个对象所占用资源的常规方法，是构造器所必需的对应物。在Java中，当一个对象变得不可到达的时候，垃圾回收器会回收与该对象相关联的存储空间，并不需要程序员做专门的工作。C++的析构器也可以被用来回收其他的非内存资源。而在Java中，一般用try-finally块来成类似的工作。

终结方法的缺点在于不能保证会被及时地执行。从一个对象变得不可到达开始，到它的终结方法被执行，所花费的这段时间是任意长的。这意味着，注重时间(time-critical)的任务不应该由终结方法来完成。如，用终结方法来关闭已经打开的文件，这是严重错误，因为打开文件的描述符是一种很有限的资源，由于JVM会延迟执行终结方法，所以大量的文件会保留在打开状态，当一个程序再不能打开文件的时候，它可能会运行失败。

及时地执行终结方法正是垃圾回收算法的一个主要功能，这种算法在不同的JVM实现中会大相径庭。如果程序依赖于终结方法被执行的时间点，那么这个程序的行为在不同的JVM中运行的表现可能就会截然不同。

Java语言规范不仅不保证终结方法会被及时地执行，而且根本就不保证它们会被执行。当一个程序终止的时候，某些已经无法访问的对象上的终结方法却根本没有被执行，这是完全有可能的。结论是：不应该依赖终结方法来更新重要的持久状态。如，依赖终结方法来解放共享资源（比如数据库）上的永久锁，很容易让整个分布式系统垮掉。

不要被System.gc和System.runFinalization两个方法所诱惑，它们确实增加了终结方法被执行的机会，但是它们并不保证终结方法一定会被执行。唯一声称保证终结方法被执行的方法是System.runFinalizersOnExit，以及Runtime.runFinalizersOnExit。这两个方法都有致命的缺陷，已经被废弃了。

如果未被捕获的异常在终结过程中被抛出来，那么这种异常可以被忽略，并且该对象的终结过程也会终止。未被捕获的异常会使对象处于破坏的状态（a corrupt state），如果另一个线程企图使用这种被破坏的对象，则可能发生任何不确定的行为。正常情况下，未被捕获的异常将会使线程终止，并打印出栈轨迹（Stack Trace），但是如果异常发生在终结方法之中，则不会如此，甚至连警告都不会打印出来。

还有一点：使用终结方法有一个非常严重的（Severe）性能损失。在作者的机器上，创建和销毁一个简单对象的时间大约为5.6 ns。增加一个终结方法使时间增加到了2400 ns。换句话说，用终结方法创建和销毁对象慢了大约430倍。

如果类的对象中封装的资源（例如文件或者线程）确实需要终止，只需提供一个显式的终止方法，并要求该类的客户端在每个实例不再有用的时候调用这个方法。值得提及的一个细节是，该实例必须记录下自己是否已经被终止了：显式的终止方法必须在一个私有域中记录下“该对象已经不再有效”。如果这些方法是在对象已经终止之后被调用，其他的方法就必须检查这个域，并抛出IllegalStateException异常。（在调用终止方法之后，直接设置对象为null不就可以了吗？）

显式终止方法的典型例子是InputStream、OutputStream和java.sql.Connection上的close()方法。另一个例子是java.util.Timer上的cancel方法，它执行必要的状态改变，使得与Timer实例相关联的该线程温和地终止自己。java.awt中的例子还包括Graphics.dispose和Window.dispose。这些方法通常由于性能不好而不被关注。一个相关的方法是Image.flush，它会释放所有与Image实例相关联的资源，但是该实例仍然处于可用的状态，如果有必要的话，会重新分配资源。

显式的终止方法通常与try-finally结构结合起来使用，以确保及时终止。在finally子句内部调用显式的终止方法，可以保证即使使用对象的时候有异常抛出，该终止方法也会执行：
//try-finally block guarantees execution of termination method
Foo foo = new Foo();
try {
    //Do what must be done with foo
    ...
} finally {
    foo.terminate();//Explicit termination method
}

终结方法（即默认的finalizer方法）的好处，有两种合法用途。第一种用途是，当对象的所有者忘记调用前面段落中建议的显式终止方法时，终结方法可以充当“安全网”（safety net）。虽然这样做并不能保证终结方法会被及时地调用，但是在客户端无法通过调用显式的终止方法来正常结束操作情况下（希望这种情形尽可能地少发生），迟一点释放关键资源总比永远不释放要好。（即手工写终结方法，并在该方法是释放关键资源）。但是如果终结方法发现资源还未被终止，则应该在日志中记录一条警告，因为这表示客户端代码中的一个Bug，应该得到修复。如果你正考虑编写这样的安全网终结方法，就要认真考虑清楚，这种额外的保护是否值得你付出这份额外的代价。

显式终止方法模式的示例中所示的四个类（FileInputStream，FileOutputStream、Timer和Connection），都具有终结方法，当它们的终止方法未能被调用的情况下，这些终结方法充当了安全网。
//查看了FileInputStream类的finalize()方法，确实有调用close()方法
/**
 * Ensures that the <code>close</code> method of this file input stream is
 * called when there are no more references to it.
 *
 * @exception  IOException  if an I/O error occurs.
 * @see        java.io.FileInputStream#close()
 */
protected void finalize() throws IOException {
    if ((fd != null) &&  (fd != FileDescriptor.in)) {
        /* if fd is shared, the references in FileDescriptor
         * will ensure that finalizer is only called when
         * safe to do so. All references using the fd have
         * become unreachable. We can call close()
         */
        close();
    }
}

终结方法的第二种合理用途与对象的本地对等体（native peer）有关。本地对等体是一个本地对象（native object），普通对象通过本地方法（native method）委托给一个本地对象。因为本地对等体不是一个普通对象，所以垃圾回收器不会知道它，当它的Java对等体被回收的时候，它不会被回收。在本地对等体并不拥有关键资源的前提下，终结方法正是执行这项任务最合适的工具。如果本地对等体拥有必须被及时终止的资源，那么该类就应该具有一个显式的终止方法，如前所述。终止方法应该完成所有必要的工作以便释放关键的资源。终止方法可以是本地方法，或者它也可以调用本地方法。

注意，“终结方法链（finalizer chaining）”并不会被自动执行。如果类（不是Object）有终结方法，并且子类覆盖了终结方法，子类的终结方法就必须手工调用超类的终结方法。应该在一个try块中终结子类，并在相应的finally块中调用超类的终结方法。这样做可以保证：即便子类的终结过程抛出异常，超类的终结方法也会得到执行。

//Manual finalizer chaining
@Override
protected void finalize() throws Throwable {
    try {
        ...//Finalize subclass state
    } finally {
        super.finalize();
    }
}

如果子类实现者覆盖了超类的终结方法，但是忘了手工调用超类的终结方法（或者有意选择不调用超类的终结方法），那么超类的终结方法将永远也不会被调用。要防范这样粗心大意或者恶意的子类是有可能的，代价就是为每个将被终结的对象创建一个附加的对象。不是把终结方法放在要求终结处理的类中，而是把终结方法放在一个匿名的类（见第22条）中，该匿名类的唯一用途就是终结它的外围实例（enclosing instance）。该匿名类的单个实例被称为终结方法守卫者（finalizer guardian），外围类的每个实例都会创建这样一个守卫者。外围实例在它的私有实例域中保存着一个对其终结方法守卫者的唯一引用，因此终结方法守卫者与外围实例可以同时启动终结过程。当守卫者被终结的时候，它执行外围实例所期望的终结行为，就好像它的终结方法是外围对象上的一个方法一样：
//测试如下
package test;

public class TestFinalizerGuardian {
    private final Object finalizerGuardian = new Object() {
        @Override
        protected void finalize() throws Throwable {
            System.out.println("finalizerGuardian releases resource for outer class.");
        }
    };

    // @Override
    // protected void finalize() throws Throwable {
        // super.finalize();
        // System.out.println("Outer class releases resource.");
    // }

    public void sayHello() {
        System.out.println("hello");
    }

    public static void main(String args[]) {
        TestFinalizerGuardian testFinalizerGuardian = new TestFinalizerGuardian();
        testFinalizerGuardian.sayHello();
    }
}
输出：
hello
（并没有调用finalize()终结函数）

注意，外部公有类并没有终结方法（除了它从Object中继承了一个无关紧急的之外），所以子类的终结方法是否调用super.finalize()并不重要，对于每一个带有终结方法的非final公有类，都应该考虑使用这种方法。

总之，除非是作为安全网，或者是为了终止非关键的本地资源，否则请不要使用终结方法。在这些很少见的情况下，既然使用了终结方法，就要记住调用super.finalize。如果用终结方法作为安全网，要记得记录终结方法的非法用法。最后，如果需要把终结方法与公有的非final类关联起来，请考虑使用终结方法守卫者，以确保即使子类的终结方法未能调用super.finalize，该终结方法也会被执行。
//------------------------------------------------------------------------------------------------
//Effective Java 第3章 对于所有对象都通用的方法P28
//第8条：覆盖equals时请遵守通用约定 P28
最容易避免覆盖equals方法的问题就是不覆盖equals方法，这种情况下，类的每个实例都只与它自身相等。如果满足了以下任何一个条件，就是所期望的结果：
--类的每个实例本质上都是唯一的。对于代表活动实例而不是值（value）的类来说确实如此，例如Thread。Object提供的equals实现对于这些类来说正是正确的行为。
//
package test;

public class MyObject {
}
//
package test;

import org.junit.Test;

import static org.junit.Assert.*;

public class MyObjectTest {
    @Test
    public void testCatOrderByAge() {
        Integer i = new Integer(1);
        Integer i2 = new Integer(1);
        assertTrue(i.equals(i2));
    }

    @Test
    public void testEquals_should_equal_when_objectSame() {
        MyObject myObject = new MyObject();
        assertTrue(myObject.equals(myObject));

        MyObject myObject1 = new MyObject();
        assertFalse(myObject.equals(myObject1));
    }
}

--不关心类是否提供了“逻辑相等（logical equality）”的测试功能。
--超类已经覆盖了equals，从超类继承过来的行为对于子类也是合适的。
--类是私有的或是包级私有的，可以确定它的equals方法永远不会被调用。在这种情况下，应该覆盖equals方法，以防它被意外调用：
@Override
public boolean equals(Object c) {
    throw new AssertionError();//Method is never called
}

如果类具有自己特有的“逻辑相等”概念（不同于对象等同的概念），而且超类还没有覆盖equals以实现期望的行为，这时就需要覆盖equals方法。这通常属于“值类（value class）”的情形。值类仅仅是一个表示值的类，例如Integer或者Date。程序员在利用equals方法来比较值对象的引用时，希望知道它们在逻辑上是否相等，而不是想了解它们是否指向同一个对象。为了满足这种要求，不仅必需覆盖equals方法，而且这样做也使得这个类的实例可以被用做映射表（map）的键（key），或者集合（set）的元素，使映射或者集合表现出预期的行为。

有一种“值类”不需要覆盖equals方法，即用实例受控（见第1条）确保“每个值至多只存在一个对象”的类。枚举值（见第30条）就属于这种类。对于这样的类而言，逻辑相同与对象等同是一回事，因此Object的equals方法赞同于逻辑意义上的equals方法。

在覆盖equals方法的时候，必须要遵守它的通用约定。下面是约定的内容，来自Object的规范：
equals方法实现了等价关系（equivalence relation）：
--自反性（reflexive）。对于任何非null的引用值，x.equals(x)必须返回true。
--对称性（symmetric）。对于任何非null的引用值x和y，当且仅当y.equals(x)返回true时，x.equals(y)必须返回true。
--传递性（transitive）。对于任何非null的引用值x、y和z，如果x.equals(y)返回true，并且y.equals(z)也返回true，那么x.equals(z)也必须返回true。
--一致性（consistent）。对于任何非null的引用值x和y，只要equals的比较操作在对象中所用的信息没有被修改，多次调用x.equals(y)就会一致地返回true，或者一致地返回false。
--对于任何非null的引用值x，x.equals(null)必须返回false。

如果违反了它们，就会发现程序将会表现不正常，甚至崩溃，而且很难找到失败的根源。没有哪个类是孤立的。一个类的实例通常会被频繁地传递给另一个类的实例。有许多类，包括所有的集合类（collection class）在内，都依赖于传递给它们的对象是否遵守了equals约定。

下面按照顺序逐一查看以下5个要求：
1. 自反性（reflexivity）————第一个要求仅仅说明对象必须等于其自身。如果违背了这一条，然后把该类的实例添加到集合（collection）中，该集合的contains方法将果断地告诉你，该集合不包含你刚刚添加的实例。
2. 对称性（symmetry）————第二个要求是说，任何两个对象对于“它们是否相等”的问题都必须保持一致。如下面的类，它实现了一个区分大小写的字符串。字符串由toString保存，但在比较操作中被忽略。
//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CaseInsensitiveStringTest {
    @Test
    public void testEquals_should_equal_when_equalsString() {
        CaseInsensitiveString caseInsensitiveString = new CaseInsensitiveString("Hello");
        assertEquals(caseInsensitiveString, "Hello");
        assertEquals(caseInsensitiveString, "hello");

        assertNotEquals("Hello", caseInsensitiveString);//违反了对称性
        assertNotEquals("hello", caseInsensitiveString);//违反了对称性
    }
}

//
package test;

public class CaseInsensitiveString {

    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = s;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CaseInsensitiveString) {
            return s.equalsIgnoreCase(((CaseInsensitiveString) obj).s);
        }

        if (obj instanceof String) { //One-way interoperability
            return s.equalsIgnoreCase((String) obj);
        }

        return false;
    }
}

问题在于，虽然CaseInsensitiveString类中的equals方法知道普通字符串（String）对象，但是，String类中的equals方法却不知道不区分大小写的字符串。因此,s.equals(cis)返回false，显然违反了对称性。假设把不区分大小写的字符串对象放到一个集合中：
List<CaseInsensitiveString> list = new ArrayList<>();
list.add(cis);
此时list.contains(s)会返回的结果是什么没人知道。可能返回false，可能返回true，或者抛出一个运行时（runtime）异常。一旦违反了equals约定，当其他对象面对你的对象时，你完全不知道这些对象的行为会怎么样。

为了解决这个问题，只需把企图与String互操作的这段代码从equals方法中去掉就可以了，这样做之后，就可以重构该方法，使它变成一条单独的返回语句：
//测试用例
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CaseInsensitiveStringTest {
    @Test
    public void testEquals_should_equal_when_equalsString() {
        CaseInsensitiveString caseInsensitiveString = new CaseInsensitiveString("Hello");
        assertNotEquals(caseInsensitiveString, "Hello");
        assertNotEquals(caseInsensitiveString, "hello");

        assertNotEquals("Hello", caseInsensitiveString);
        assertNotEquals("hello", caseInsensitiveString);

        CaseInsensitiveString caseInsensitiveString1 = new CaseInsensitiveString("hello");
        assertEquals(caseInsensitiveString, caseInsensitiveString1);
        assertEquals(caseInsensitiveString1, caseInsensitiveString);
    }
}

//
package test;

public class CaseInsensitiveString {

    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = s;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CaseInsensitiveString &&
                s.equalsIgnoreCase(((CaseInsensitiveString) obj).s);
    }
}

3. 传递性（transitivity）————equals约定的第三个要求是，如果一个对象等于第二个对象，并且第二个对象又等于第三个对象，则第一个对象一定等于第三个对象。无意识地违反这条规则的情形也不难想像。考虑子类的情形，它将一个新的值组件（value component）添加到了超类中。子类增加的信息会影响到equals的比较结果。首先以一个简单的不可变的二维整数型Point类开始：

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PointTest {
    @Test
    public void testEquals_should_equal_when_equalsPoint() {
        Point p = new Point(1, 2);
        Point p1 = new Point(1, 2);
        assertEquals(p, p1);

        Point p2 = new Point(2, 2);
        assertNotEquals(p, p2);
    }
}

//
package test;

public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }
        Point point = (Point) obj;
        return point.x == x && point.y == y;
    }
}

扩展这个类，为一个点添加颜色信息：

//package test;

import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ColorPointTest {
    @Test
    public void testEquals_should_notEqual_when_pointEqualColorPoint() {
        Point p = new Point(1, 2);
        ColorPoint cp = new ColorPoint(1, 2, Color.RED);
        assertEquals(p, cp);
        assertNotEquals(cp, p);
    }
}

//
package test;

import java.awt.Color;

public class ColorPoint extends Point {
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    //Broken - violates symmetry!
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColorPoint)) {
            return false;
        }
        return super.equals(obj) && ((ColorPoint) obj).color == color;
    }
}

如果完全不提供equals方法，直接从Point继承，在equals做比较时颜色信息就被忽略掉了。虽然不违反equals约定，但是无法接受。假设编写了一个equals方法，只有当它的参数是另一个有色点时，并且具有同样的位置和颜色时，才返回true。见上。
这个方法问题在于，违反了对称性。p.equals(cp)返回true，cp.equals(p)则返回false。尝试修正这个问题，让ColorPoint.equals在进行“混合比较”时忽略颜色信息：
//package test;

import java.awt.Color;

public class ColorPoint extends Point {
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    //Broken - violates transitivity!
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }

        // If obj is a normal Point, do a color-blind comparison
        if (!(obj instanceof ColorPoint)) {
            return obj.equals(this);
        }
        return super.equals(obj) && ((ColorPoint) obj).color == color;
    }
}

这种方法确实提供了对称性，但是却牺牲了传递性：
//
package test;

import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ColorPointTest {
    @Test
    public void testEquals_should_notEqual_when_pointEqualColorPoint() {
        ColorPoint p1 = new ColorPoint(1, 2, Color.RED);
        Point p2 = new Point(1, 2);
        ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);
        assertEquals(p1, p2);
        assertEquals(p2, p3);
        assertNotEquals(p1, p3);
    }
}

此时，前两种比较不考虑颜色信息（“色盲”），而第三种比较则考虑了颜色信息。
怎么解决呢？事实上，这是面向对象语言中关于等价关系的一个基本问题。我们无法在扩展可实例化的类的同时，既增加新的值组件，同时又保留equals约定，除非愿意放弃面向对象的抽象所带来的优势。

可能听说，在equals方法中用getClass测试代替instanceof测试，可以扩展可实例化的类和增加新的值组件，同时保留equals约定：
//Broken - violates Liskov substitution principle(page 40)
@Override
public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass()) {
        return false;
    }
    Point point = (Point) obj;
    return point.x == x && point.y == y;
}
这段程序只有当对象具有相同的实现时，才能使对象等同。虽然这样也不算太糟糕，但是结果却是无法接受的。
假设要编写一个方法，以检验某个整值点是否处在单位圆中，下面是可以采用的其中一个方法：
//
package test;

import java.util.HashSet;
import java.util.Set;

public class UnitCircle {
    private static final Set<Point> unitCircle;

    static {
        unitCircle = new HashSet<Point>();
        unitCircle.add(new Point(1, 0));
        unitCircle.add(new Point(0, 1));
        unitCircle.add(new Point(-1, 0));
        unitCircle.add(new Point(0, -1));
    }

    public static boolean onUnitCircle(Point p) {
        return unitCircle.contains(p);
    }
}

//Point
package test;

public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Point point = (Point) obj;
        return point.x == x && point.y == y;
    }

    @Override
    public int hashCode() {
        return x + y;
    }
}

//
package test;

import org.junit.Test;

import java.nio.channels.Pipe;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PointTest {
    @Test
    public void testEquals_should_equal_when_equalsPoint() {
        Point p = new Point(1, 2);
        Point p1 = new Point(1, 2);
        assertEquals(p, p1);

        Point p2 = new Point(2, 2);
        assertNotEquals(p, p2);
    }

    @Test
    public void testHashSet_should_contain_when_contain() {
        Set<Point> points = new HashSet<Point>();
        points.add(new Point(1, 1));
        points.add(new Point(1, 3));

        assertTrue(points.contains(new Point(1, 1)));
        assertTrue(points.contains(new Point(1, 3)));
        assertFalse(points.contains(new Point(2, 2)));
    }
}

//Test
package test;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class UnitCircleTest {
    @Test
    public void testOnUnitCircle_should_contain_when_PointOnUnitCircle() {
        Point p = new Point(0, 1);
        assertTrue(UnitCircle.onUnitCircle(p));
    }
}

HashSet中contains的判断条件：contains()是根据equals()和hashCode()判断2个对象是否是同一个，没重写hashCode()，系统默认按照地址计算hashCode，2个地址不同，hashCode也不同，返回当然是false。
equals和hashcode方法要同时重写，并且要在equals为true的时候，hashCode必须要相同。这个已经是一种不成文的规定了，这两个方法要重写就要一起重写，而且IDE里也会只重写一个会视为警告。所以这两个方法要同时重写。
详细的可以去看HashMap的contains实现，哪里是equals和hashCode两个同时使用了，所以在有Map的时候，必须两个都要验证，但是在ArrayList里不验证hashCode，所以ArrayList里不重新这个hashCode也无所谓。

上述UnitCircle可能不是实现这种功能的最快方式，不过效果很好。但是假设通过某种不添加值组件的方式扩展了Point，例如让它的构造器记录创建了多少个实例：
//
package test;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterPoint extends Point {

    private static final AtomicInteger counter = new AtomicInteger();

    public CounterPoint(int x, int y) {
        super(x, y);
        counter.incrementAndGet();
    }

    public int numberCreated() {
        return counter.get();
    }
}

//测试
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CounterPointTest {
    @Test
    public void testCounter_should_equals_when_createCounterPoint() {
        CounterPoint counterPoint = new CounterPoint(1, 1);
        assertEquals(1, counterPoint.numberCreated());

        CounterPoint counterPoint1 = new CounterPoint(0, 1);
        assertEquals(2, counterPoint.numberCreated());
    }
    
    @Test
    public void testOnUnitCircle_should_contains_when_counterPointIn() {
        Point cp = new CounterPoint(0, 1);
        assertFalse(UnitCircle.onUnitCircle(cp)); //把CounterPoint传入contains函数，则无法返回true

        Point p = new Point(0, 1);
        assertTrue(UnitCircle.onUnitCircle(p));
    }
}

里氏替换原则（Liskov substitution principle）认为，一个类型的任何重要属性也将适用于它的子类型，因此为该类类型编写的任何方法，在它的子类型上也应该同样运行得很好。但是将CounterPoint实例传给了onUnitCircle方法，如果Point类使用了基于getClass的equals方法，无论CounterPoint的x和y值是什么，onUnitCircle方法都会返回false。之所以如此，是因为像onUnitCircle方法所用的HashSet这样的集合，利用equals方法检验包含条件，没有任何CounterPoint实例与任何Point对应。但是，如果在Point上使用适当的基于instanceof的equals方法，当遇到CounterPoint时，相同的onUnitCircle方法就会工作得很好。

虽然没有一种满意的办法可以既扩展不可实例化的类，又增加值组件，但还是有一种不错的权宜之计（workaround）。根据第16条的建议：复合优先于继承。我们不再让ColorPoint扩展Point，而是在ColorPoint中加入一个私有的Point域，以及一个公有的视图（view）方法（见么5条），此就去返回一个与该有色点处在相同位置的普通Point对象：
//
package test;

import java.awt.Color;

// Adds a value component without violating the equals contract
public class ColorPoint {
    private final Point point;
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        if (color == null) {
            throw new NullPointerException();
        }
        point = new Point(x, y);
        this.color = color;
    }

    /**
     * Returns the point-view of this color point
     */
    public Point asPoint() {
        return point;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColorPoint)) {
            return false;
        }
        ColorPoint cp = (ColorPoint) obj;
        return cp.point.equals(point) && cp.color.equals(color);
    }
}

//
package test;

public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }
        Point point = (Point) obj;
        return point.x == x && point.y == y;
    }

    @Override
    public int hashCode() {
        return x + y;
    }
}


//Test
package test;

import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ColorPointTest {
    @Test
    public void testColorPointEquals_should_equals_when_colorPointEqual() {
        ColorPoint cp = new ColorPoint(1, 1, Color.BLACK);
        ColorPoint cp1 = new ColorPoint(1, 1, Color.BLACK);
        ColorPoint cp2 = new ColorPoint(1, 1, Color.RED);

        Point p = new Point(1, 1);
        assertNotEquals(cp, p);
        assertEquals(cp, cp1);
        assertNotEquals(cp, cp2);
    }
}

注意，可以一个抽象（abstract）类的子类中增加新的值组件，而不会违反equals约定。对于根据第20条的建议“用类层次（class hierarchies）代替标签类（tagged class）”而得到的那种类层次结构来说，这一点非常重要。如，可能有一个抽象的Shape类，没有任何值组件，Circle子类添加了一个radius域，Rectangle子类添加了length和width域。只要不可能直接创建超类的实例，前面所述的种种问题就不会发生。

4. 一致性（consistency）————equals约定的第四个要求是：如果两个对象相等，它们就必须始终保持相等。当在写一个类的时候，应该仔细考虑它是否应该是不可变的（见第15条）。如果认为它应该是不可变的，就必须保证equals方法满足这样的限制条件：相等的对象永远相等，不相等的对象永远不相等。
无论类是否是不可变的，都不要使equals方法依赖于不可靠的资源。例如，java.net.URL的equals方法依赖于对URL中主机IP地址的比较。将一个主机名转变成IP地址可能需要访问网络，随着时间的推移，不确保会产生相同的结果。这样会导致URL的equals方法违反equals约定。除了极少娄的例外情况，equals方法都应该对驻留在内存中的对象执行确定性的计算。

//final测试 http://www.importnew.com/7553.html
final在Java中是一个保留的关键字，可以声明成员变量、方法、类以及本地变量。一旦你将引用声明作final，你将不能改变这个引用了，编译器会检查代码，如果你试图将变量再次初始化的话，编译器会报编译错误。

//
package test;

public class FinalKeywords {
    private int i; //如果这里声明为private final int i，则无法修改该值，setI编译错误

    public void setI(int i) {
        this.i = i;
    }

    public FinalKeywords(int i) {
        this.i = i;
    }
}

//Test
package test;

import org.junit.Test;

public class FinalKeywordsTest {
    @Test
    public void testFinalClass_should_notAllowedChangeRef_when_declaredFinal() {
        final FinalKeywords finalKeywords = new FinalKeywords(3);
        //finalKeywords = new FinalKeywords(4); //声明为final将不能改变引用，所以不能重新new
        finalKeywords.setI(4); //可以修改值，引用地址没有改变，内存空间上的数据有变化，所以没问题
    }
}

5. 非空性（Non-nullity）————最后一个要求没有名称，姑且称它为“非空性（Non-nullity）”，意思是指所有的对象都必须不等于null。
不需要显示地判断 if (o == null) return false;
在equals方法中，进行类型转换前，equals方法必须使用instanceof操作符，检查其参数是否为正确的类型：
@Override public boolean equals(Object o) {
    if (!(o instanceof MyType)) {
        return false;
    }
    MyType mt = (MyType) o;
    //...
}
如果漏掉了这一步的类型检查，并且传递给equals方法的参数又是错误的类型，那么equals方法将会抛出ClassCastException异常，这就违反了equals约定。但是如果instanceof的第一个操作数为null，那么，不管第二个操作数是哪种类型，instanceof操作符都指定应该返回false。因此，如果把null传给equals方法，类型检查就会返回false，所以不需要单独的null检查。

结果这些要求，得出了实现高质量equals方法的诀窍：
1. 使用==操作符检查“参数是否为这个对象的引用”。如果是，则返回true。这是一种性能优化，如果比较操作有可能很昂贵，就值得这么做。
2. 使用instanceof操作符检查“参数是否为正确的类型”。如果不是，则返回false。一般来说，所谓“正确的类型”是指equals方法所在的那个类。有些情况下，是指该类所实现的某个接口。如果类实现 的接口改进了equals约定，允许在实现了该接口的类之间进行比较，那么就使用接口。集合接口（collection interface）如Set、List、Map和Map.Entry具有这样的特性。
3. 把参数转换成正确的类型。转换之前进行过instanceof测试，所以确保会成功。
4. 对于该类中的每个“关键（significant）”域，检查参数中的域是否与该对象中对应的域相匹配。如果这些测试全部成功，则返回true，否则返回false。如果第2步中的类型是个接口，就必须通过接口方法访问参数中的域；如果该类型是个类，也许就能够直接访问参数中的域，这要取决于它们的可访问性。

对于既不是float也不是double类型的基本类型域，可以使用==操作符进行比较；
对于对象引用域，可以递归地调用equals方法；
对于float域，可以使用Float.compare方法；
对于double域，则使用Double.compare方法。
对于float和double域进行特殊的处理是有必要的，因为存在着Float.NaN、-0.0f 以及类似的double常量。
对于数组域，则要把以上这些指导原则应用到每个元素上。如果数组域中的每个元素都很重要，就可以使用发行版本1.5 中新增的其中一个Array.equals方法。

对于对象引用域包含null可能是合法的，所以，为了避免可能导致NullPointerException异常，则使用下面的习惯用法来比较这样的域：
(field == null ? o.field == null : field.equals(o.field))
如果field和o.field通常是相同的对象引用，那么下面的做法就会更快一些：
(field == o.field || (field != null && field.equals(o.field)))

对于有些类，如前面提到的CaseInsensitiveString类，域的比较要比简单的等同性测试复杂得多。如果是这种情况，可能会希望保存该域的一个“范式（canonical form）”，这样equals方法就可以根据这些范式进行低开销的精确比较，而不是高开销的非精确比较。这种方法对于不可变类（见第15条）是最为合适的；如果对象可能发生变化，就必须使用其范式保持最新。

域的比较顺序可能会影响到equals方法的性能。为了获得最佳的性能，应该最先比较最有可能不一致的域，或者是开销最低的域，最理想的情况是两个条件同时满足的域。不应该去比较那些不属于对象逻辑状态的域，如用于同步操作的Lock域。也不需要比较冗余域（redundant field），因为这些冗余域可以由“关键域”计算获得，但是这样做有可能提高equals方法的性能。如果冗余域代表了整个对象的综合描述，比较这相域可以节省当比较失败时去比较实际数据所需要的开销。如，假设有一个Polygon类，并缓存了该区域。如果两个多边形有着不同的区域，就没有必要去比较它们的边和至高点。

5. 当你编写完成了equals方法之后，应该问自己三个问题：它是否是对称的、传递的、一致的。当然，equals方法也必须满足其他两个特性（自反性和非空性），但是这两种特性通常会自动满足。

根据上面的诀窍构建的equals方法的具体例子，请参看第9条的PhoneNumber.equals。下面是最后一些告诫：
--覆盖equals时总要覆盖hashCode（见第9条）。
--不要企图让equals方法过于智能。如果只是简单地测试域中的值是否相等，则不难做到遵守equals约定。如果想过度地寻求各种等价关系，则很容易陷入麻烦之中。把任何一种别名形式考虑到等价的范围内，往往不会是好主意。
--不要将equals声明中的Object对象替换为其他的类型。如下：
public boolean equals(MyClass o) {
    //...
}
问题在于，这个方法并没有覆盖Object.equals，因为它的参数应该是Object类型，相反，它重载（overload）了Object.equals（见第41条）。在原有equals方法的基础上，再提供一个“强类型（strongly typed）”的equals方法，只要这两个方法返回同样的结果（没有强制的理由必须这样做），那么这就是可以接受的。在某些特定的情况下，这也许能够稍微改善性能，但是与增加的复杂性相比，这种做法是不值得的（见第55条）。
@Override注解的用法一致，可以防止这种错误（见第36条）。这个equals方法不能编译，错误消息会告诉你哪里出了问题：
@Overrid public boolean equals(MyClass o) {
    //...
}
//------------------------------------------------------------------------------------------------
//Effective Java 第3章 对于所有对象都通用的方法P28
//第9条：覆盖equals时总要覆盖hashCode P39
在每个覆盖了equals方法的类中，也必须覆盖hashCode方法。如果不这样做，就会违反Object.hashCode的通用约定，从而导致该类无法结合所有基于散列的集合一起正常动作，这样的集合包括HashMap、HashSet和Hashtable。

约定内容，如下：
--在应用程序的执行期间，只要对象的equals方法的比较操作所用到的信息没有被修改，那么对这同一个对象调用多次，hashCode方法都必须始终如一地返回同一个整数。在同一个应用程序的多次执行过程中，每次所返回的整数可以不一致。
--如果两个对象根据equals(Object)方法比较是相等的，那么调用这两个对象中任意一个对象的hashCode方法都必须产生同样的整数结果。
--如果两个对象根据equals(Object)方法比较是不相等的，那么调用这两个对象中任意一个对象的hashCode方法，则不一定要产生不同的整数结果。但是程序员应该知道，给不相等的对象产生截然不同的整数结果，有可能提高散列表（hash table）的性能。

因没有覆盖hashCode而违反的关键约定是第二条：相等的对象必须具有相等的散列码（hash code）。根据类的equals方法，两个截然不同的实例在逻辑上有可能是相等的，但是根据Object类的hashCode方法，它们仅仅是两个没有任何共同之外的对象。因此，对象的hashCode方法返回两个看起来是随机的整数，而不是要追第二个约定所要求的那样，返回两个相等的整数。
//
package test;

public final class PhoneNumber {

    private final int areaCode;
    private final int prefix;
    private final int lineNumber;

    public PhoneNumber(int areaCode, int prefix, int lineNumber) {
        rangeCheck(areaCode, 999, "area code");
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNumber = lineNumber;
    }

    private static void rangeCheck(int arg, int max, String name) {
        if (arg < 0 || arg > max) {
            throw new IllegalArgumentException(name + ": " + arg);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PhoneNumber)) {
            return false;
        }
        PhoneNumber pn = (PhoneNumber) obj;
        return pn.lineNumber == lineNumber
            && pn.prefix == prefix
            && pn.areaCode == areaCode;
    }

    //Broken - no hashCode method!
}

//Test
package test;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotEquals;

public class PhoneNumberTest {
    @Test
    public void testPhoneNumberMap_should_getPhoneNubmerObj_when_get() {
        Map<PhoneNumber, String> m = new HashMap<>();
        m.put(new PhoneNumber(707, 867, 5309), "Jenny");
        assertNotEquals(m.get(new PhoneNumber(707, 867, 5309)), null); //用例执行失败
    }
}

map.put和map.get里涉及两个PhoneNumber实例：第一个被用于插入到HashMap中，第二个实例与第一个相等，被用于（试图用于）获取。由于PhoneNumber类没有覆盖hashCode方法，从而导致两个相等的实例具有不相等的散列码，违反了hashCode的约定。因此，put方法把电话号码对象存放在一个散列桶（hash bucket）中，get方法却在另一个散列桶中查找这个电话号码。即便这两个实例下她被放到同一个散列桶中，get方法也必定会返回null，因为HashMap有一项优化，可以将与每个项相关联的散列码缓存起来，如果散列码不匹配，也不必检验对象的等同性。
修改这个问题非常简单，保需为PhoneNumber类提供一个适当的hashCode方法即可，那么，hashCode方法应该是什么样？编写一个合法但并不好用的hashCode方法没有任何价值。如，下面这个方法总是合法的，但永远都不应该被正式使用：
//The worst possible legal hash function - never use!
@Override public int hashCode() {
    return 42;
}
上面这个hashCode方法是合法的，因为它确保了相等的对象总是具有同样的散列码。但它也极为恶劣，因为它使得每个对象都具有同样的散列码。因此，每个对象都被映射到同一个散列桶中，使散列表退化为链表（linked list）。它使得本该线性时间运行的程序变成了以平方级时间在运行。对于规格很大的散列表而言，这会关系到散列表能否正常工作。

一个好的散列函数通常倾向于“为不相等的对象产生不相等的散列码”。这正是hashCode约定中第三条的含义。理想情况下，散列函数应该把集合中不相等的实例均匀地分布到所有可能的散列值上。要想完全达到这种理想的情况是非常困难的。相对接近这种理想情形则并不太困难。下面给出一种简单的解决方法：
1. 把某个非零的常数值，比如说17，保存在一个名为result的int类型的变量中。
2. 对于 对象中每个关键域f（指equals方法中涉及的每个域），完成以下步骤：
    a. 为该域计算int类型的散列码c：
        i. 如果该域是boolean类型，则计算（f ? 1 : 0） 。
        ii. 如果该域是byte、char、short或者int类型，则计算(int) f。
        iii. 如果该域是long类型，则计算(int)(f ^ (f >>> 32))。
        iv. 如果该域是float类型，则计算Float.floatToIntBits(f)。
        v. 如果该域是double类型，则计算Double.doubleToLongBits(f)，然后按照步骤2.a.iii，为得到的long类型值计算散列值。
        vi. 如果该域是一个对象引用，并且该类的equals方法通过递归地调用equals的方式来比较这个域，则同样为这个域递归的调用hashCode。如果需要更复杂的比较，则为这个域计算一个“范式（canonical respresentation）”，然后针对这个范式调用hashCode。如果这个域的值为null，则返回0（或者其他某个常数，但通常是0）。
        vii. 如果该域是一个数组，则要把每一个元素当做单独的域来处理。也就是说，递归地应用上述规则，对每个重要的元素计算一个散列码，然后根据步骤2.b中的做法把这些散列值组合起来。如果数组域中的每个元素都很重要，可以利用发行版本1.5 中增加的其中一个Arrays.hashCode方法。
    b. 按照下面的公式，把步骤2.a中计算得到的散列码c合并到result中：
    result = 31 * result + c;
3. 返回result。
4. 写完了hashCode方法之后，问问自己“相等的实例是否都具有相等的散列码”。要编写单元测试来验证你的推断。如果相等的实例有着不相等的散列码，则要找出原因，并修正错误。

在散列码的计算过程中，可以把冗余域（redundant field）排队在外。如果一个域的值可以根据参与计算的其他域值计算出来，则可以把这样的域排队在外。必须排队equals比较计算中没有用到的任何域，否则很有可能违反hashCode约定的第二条。

上述步骤1中用到了一个非零的初始值，因此步骤2.a中计算的散列值为0的那些初始域，会影响到散列值。如果步骤1中的初始值为0，则整个散列值将不受这些初始域的影响，因为这些初始域会增加冲突的可能性。值17则是任选的。
步骤2.b中的乘法部分使得散列值依赖于域的顺序，如果一个类包含多个相似的域，这样的乘法运算就会产生一个更好的散列函数。例如，如果String散列函数省略了这个乘法部分，那么只是字母顺序不同的所有字符串都会有相同的散列码。之所以选择31，是因为它是一个奇素数。如果乘数是偶数，并且乘法溢出的话，信息会丢失，因为与2相乘等价于移位运算。使用素数的好处并不很明显，但是习惯上都使用素数来计算散列结果。31有个很好的特性，即用移位和减法来代替简洁，可以得到更好的性能：31 * i == (i << 5) - i。现代的VM可以自动完成这种优化。

把上述解决办法用到PhoneNumber类中。它有三个关键域，都是short类型：
@Override
public int hashCode() {
    int result = 17;
    result = 31 * result + areaCode;
    result = 31 * result + prefix;
    result = 31 * result + lineNumber;
    return result;
}

用例执行成功。
因为这个方法返回的结果是一个简单的，确定的计算结果，它的输入只是PhoneNumber实例中的三个关键域，因此相等的PhoneNumber显然都会有相等的散列码。实际上，对于PhoneNumber的hashCode实现而言，上面这个方法是非常合理的，相当于Java平台类库中的实现。它的做法非常简单，也相当快捷，恰当地把不相等的电话号码分散到不同的散列桶中。

如果一个类是不可变的，并且计算散列码的开销也比较大，就应该考虑把散列码缓存在对象内部，而不是每次请求的时候都重新计算散列码。如果你觉得这种类型的大多数对象都会被用做散列键（hash keys），就应该在创建实例的时候计算散列码。否则，可以选择“延迟初始化（lazily initialize）”散列码，一直到hashCode被第一次调用的时候才初始化（见第71条）。现在尚不清楚我们的PhoneNumber类是否值得这样处理，但可以通过它来说明这种方法该如何实现：
//Lazily initialized, cached hashCode
private volatile int hashCode; // (See Item 71) volatile可以保证多个线程访问hashCode时，每个线程都会获取该域的最新数据，一定程度上降低重复计算。但是该字段并不能保证原子操作，有可能出现hashCode在一个线程为0时，另一个线程已经计算完成hashCode值，并同步到主内存中，此时在当前线程中，result计算完成后，会再次将结果赋给hashCode。只是重复计算，结果是一样的。
@Override public int hashCode() {
    int result = hashCode;
    if (result == 0) {
        result = 17;
        result = 31 * result + areaCode;
        result = 31 * result + prefix;
        result = 31 * result + lineNumber;
        hashCode = result;
    }
    return result;
}

本条目中介绍的方法对于绝大多数应用程序而言已经足够了。

不要试图从散列码计算中排除掉一个对象的关键部分来提高性能。虽然这样得到的散列函数运行起来可能更快，但它的效果不见得会好，可能会导致散列表慢到根本无法使用。
//------------------------------------------------------------------------------------------------
//Effective Java 第3章 对于所有对象都通用的方法P28
//第10条：始终要覆盖toString P44
java.lang.Object提供了toString方法的一个实现，但它返回的字符串通常并不是类的用户所期望看到的。它包含类的名称，以及一个“@”符号，接着是散列码的无符号十六进制表示法。如“PhoneNumber@163b91”。toString的通用约定指出，被返回的字符串应该是一个“简洁的，但信息丰富，并且易于阅读的表达形式”。toString的约定进一步指出，“建议所有的子类都覆盖个方法。”这是一个很好的建议！

//以第9条中的PhoneNumber为例：
public class PhoneNumberTest {
    @Test
    public void testPhoneNumberMap_should_getPhoneNubmerObj_when_get() {
        PhoneNumber phoneNumber = new PhoneNumber(707, 867, 5309);

        System.out.println(phoneNumber.hashCode());
        System.out.println(phoneNumber);

        int i = 0x12960c;
        System.out.println(i);
    }
}
输出：
1218060
test.PhoneNumber@12960c
1218060

虽然遵守toString的约定并不像遵守equals和hashCode的约定（见第8、9条）那么重要，但是提供好的toString实现可以使类用起来更加舒适。当对象被传递给println、printf、字符串联操作符（+）以及assert或者被调试器打印出来时，toString方法会被自动调用。（Java 1.5 发行版本在平台中增加了printf方法，还提供了包括String.format的相关方法，与C语言中的sprintf相似。）

如果为PhoneNumber提供了好的toString方法，那么，要产生有用的诊断消息会非常容易：
System.out.println("Failed to connect: " + phoneNumber);

在实际应用中，toString方法应该返回对象中包含的所有值得关注的信息，譬如上述电话号码例子那样。如果对象太大，或者对象中包含的状态信息难以用字符串表达，这样做就有点不切实际。这种情况下，toString应该返回一个摘要信息，如“Manhattan white pages(1487536 listings)”或者"Thread[main, 5, main]"。理想情况下，字符串应该是自描述的（self-explanatory），（Thread例子不满足这样的要求。）

无论是否决定指定格式，都应该在文档中明确地表明你的意图。如果要指定格式，则应该严格地这样去做。如，下面是第9条中PhoneNumber类的toString方法：
/**
 * Returns the string representation of this phone number.
 * The string consists of fourteen characters whose format
 * is "(XXX) YYY-ZZZZ", where XXX is the area code, YYY is
 * the prefix, and ZZZZ is the line number. (Each of the
 * capital letters represents a single decimal digit.)
 *
 * If any of the three parts of this phone number is too small
 * to fill up its field, the field is padded with leading zeros.
 * For example, if the value of the line number is 123, the last
 * four characters of the string representation will be "0123".
 *
 * Note that there is a single space separating the closing
 * parenthesis after the area code from the first digit of the
 * prefix.
 */
@Override
public String toString() {
    return String.format("(%03d) %03d-%04d", areaCode, prefix, lineNumber);
}

如果决定不指定格式，那么文档注释部分也应该有如下所示的指示信息：
/**
 * Returns a brief description of this potion. The exact details
 * of the representation are unspecified and subject to change, 
 * but the following may be regarded as typical:
 * "[Potion #9: type=love, smell=turpentine, look=india ink]"
 */
@Override
public String toString() {
    //...
}

对于那些依赖于格式的细节进行编程或者产生永久数据的程序员，在读到这段注释之后，一旦格式被改变，则只能自己承担后果。

无论是否指定格式，都为toString返回值中包含的所有信息，提供一种编程式的访问途径。如，PhoneNumber类应该包含针对area code、prefix和line number的访问方法。如果不这么做，就会强迫那些需要这些信息的程序员不得不自己去解析这些字符串。除了降低了程序的性能，使得程序员们去做这些不的工作之外，这个解析过程也很容易出错，会导致系统不稳定，如果格式发生变化，还会导致系统崩溃。如果没有提供这些访问方法，即便你已经指明了字符串的格式是可以变化的，这个字符串格式也成了事实上的API。
//------------------------------------------------------------------------------------------------
//Effective Java 第3章 对于所有对象都通用的方法P28
//第11条：谨慎地覆盖clone P46
clone方法不应该在构造的过程中，调用新对象中任何非final的方法（见第17条）。如果clone调用了一个被覆盖的方法，那么在该方法所在的子类有机会修正它在克隆对象中的状态之前，该方法就会先被执行，这样很有可能会导致克隆对象和原始对象之间的不一致。因此，上一段落中讨论到的put(key, value)方法应该要么是final的，要么是私有的（如果是私有的，它应该算是非final公有方法的“辅助方法”）。
Object的clone方法被声明为可抛出CloneNotSupportedException异常，但是，覆盖版本的clone方法可能会忽略这个声明。公有的clone方法应该省略这个声明，因为不会抛出受检异常（checked exception）的方法与会抛出异常的方法相比，使用起来更加轻松（见第59条）。如果专门为了继承而设计的类（见第17条）覆盖了clone方法，覆盖版本的clone方法就应该模拟Object.clone的行为：它应该被声明为protected、抛出CloneNotSupportedException异常，并且该类不应该实现Cloneable接口。这样做可以使子类具有实现或不实现Cloneable接口的自由，就仿佛它们直接扩展了Object一样。
还有一点，如果决定 用线程安全的类实现Cloneable接口，要记得它的clone方法必须得到很好的同步，就像任何其他方法一样（见第66条）。Object的clone方法没有同步，因此即便很满意，可能也必须编写同步的clone方法来调用super.clone()。

简而言之，所有实现了Cloneable接口的类都应该用一个公有的方法覆盖clone。此公有方法首先调用super.clone，然后修正任何需要修正的域。一般情况下，这意味着要拷贝任何包含内部“深层结构”的可变对象，并用指向新对象的引用 代替原来指向这些对象的引用。虽然，这些内部拷贝操作往往可以通过递归地调用clone来完成，但这通常并不是最佳方法。如果该类只包含基本类型的域，或者指向不可变对象的引用，那么多半的情况是没有域需要修正。这条规则也有例外，如代表序列号或其他唯一ID值的域，或者代表对象的创建时间地域，不管这些域是基本类型还是不可变的，它们也都需要被修正。

如果扩展一个实现了Cloneable接口的类，那么除了实现一个行为良好的clone方法外，没有别的选择。否则，最好提供某些其他的途径来代替对象拷贝，或者不提供这样的功能。例如，对于不可变类，支持对象拷贝并没有太大的意义，因为被拷贝的对象与原始对象并没有实质的不同。

另一个实现对象拷贝的好办法是提供一个拷贝构造器（copy constructor）或拷贝工厂（copy factory）。拷贝构造器只是一个构造器，它唯一的参数类型是包含该构造器的类，例如：
public Yum(Yum yum);

拷贝工厂是类似于拷贝构造器静态工厂：
public static Yum newInstance(Yum yum);

拷贝构造器的做法，及其静态工厂方法的变形，都比Cloneable/clone方法具有更多的优势：它们不依赖于某一种很有风险的、语言之外的对象创建机制；它们不要求遵守尚未制定好文档的规范；它们不会与final域的正常使用发生冲突；它们不会抛出不必要的受检异常（checked exception）；它们不需要进行类型转换。虽然不可能把拷贝构造器或者静态工厂放到接口中，但是由于Cloneable的接口缺少一个公有的clone方法，所以它也没有提供一个接口该有的功能。因此，使用拷贝构造器或者拷贝工厂来代替clone方法时，并没有放弃接口的功能特性。

更进一步，拷贝构造器或者拷贝工厂可以带一个参数，参数类型是通过该类实现的接口。例如，所有通用集合实现都提供了一个拷贝构造器，它的参数类型为Collection或者Map。基于接口的拷贝构造器和拷贝工厂（更准确的叫法应该是“转换构造器（conversion constructor）”和转换工厂（conversion factory）），允许客户选择拷贝的实现类型，而不是强迫客户接受原始的实现类型。例如，假设有一个HashSet，并且希望把它拷贝成一个TreeSet。clone方法无法提供这样的功能，但是用转换构造器很容易实现：new TreeSet(s)。

既然Cloneable具有上述那么多问题，可以肯定地说，其他的接口都不应该扩展（extend）这个接口，为了继承而设计的类（见第17条）也不应该实现（implement）这个接口。由于它具有这么多缺点，有些专家级的程序员干脆从来不去覆盖clone方法，也从来不去调用它，除非拷贝数组。必须清楚一点，对于一个专门为了继承而设计的类，如果未能提供行为良好的受保护的（protected）clone方法，它的子类就不可能实现Cloneable接口。
//------------------------------------------------------------------------------------------------
//Effective Java 第3章 对于所有对象都通用的方法P28
//第12条：考虑实现Comparable接口 P53
与本章中讨论的其他方法不同，compareTo方法并没有在Object中声明。它是Comparable接口中唯一的方法。compareTo方法不但允许进行简单的赞同性比较，而且允许执行顺序比较，它与Object的equals方法具有相似的特征，它还是个泛型。类实现了Comparable接口，就表明它的实例具有内存的排序关系（natural ordering）。为实现Comparable接口的对象数组进行排序就这么简单：
Arrays.sort(a);

Comparator<T>是让一个比较类实现的，要实现compara(T t1, T t2)接口，就成为了一个比较器，可用于作为入参传递给Set、Map等对象（在本文档中搜索Comparator<Student>），类似于C++中的函数对象。
Comparable<T>是让一个普通的对象类继承的，要实现compareTo(T anotherT)，该类就自动具有了比较功能。

//如Cat类
package test;

import java.util.Comparator;

public class Cat implements Comparable<Cat>{
    private final int age;
    private final String name;
    private final int weight;

    public Cat(int age, String name, int weight) {
        this.age = age;
        this.name = name;
        this.weight = weight;
    }

    public int age() {
        return age;
    }

    public String name() {
        return name;
    }

    public int weight() {
        return weight;
    }

    @Override
    public int compareTo(Cat anotherCat) {
        if (age > anotherCat.age) {
            return 1;
        } else if (age < anotherCat.age) {
            return -1;
        }

        if (name.compareTo(anotherCat.name) > 0) {
            return 1;
        } else if (name.compareTo(anotherCat.name) < 0) {
            return -1;
        }

        //return new Integer(weight).compareTo(new Integer(anotherCat.weight));
        return Integer.compare(weight, anotherCat.weight);
    }
}

//
package test;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CatSortAlgTest {
    @Test
    public void testCatOrderByAge() {
        Cat[] cats = new Cat[]{new Cat(3, "MiMi", 5), new Cat(1, "DaHuang", 4), new Cat(2, "XiaoHei", 3)};
        Arrays.sort(cats);
        assertEquals(cats[0].age(), 1);
        assertEquals(cats[1].age(), 2);
        assertEquals(cats[2].age(), 3);
    }

    @Test
    public void testCatOrderByName() {
        Cat[] cats = new Cat[]{new Cat(1, "MiMi", 5), new Cat(1, "DaHuang", 4), new Cat(1, "XiaoHei", 3)};
        Arrays.sort(cats);
        assertEquals(cats[0].name(), "DaHuang");
        assertEquals(cats[1].name(), "MiMi");
        assertEquals(cats[2].name(), "XiaoHei");
    }

    @Test
    public void testCatOrderByWeight() {
        Cat[] cats = new Cat[]{new Cat(1, "MiMi", 5), new Cat(1, "MiMi", 4), new Cat(1, "MiMi", 3)};
        Arrays.sort(cats);
        assertEquals(cats[0].weight(), 3);
        assertEquals(cats[1].weight(), 4);
        assertEquals(cats[2].weight(), 5);
    }

    @Test
    public void testCatOrderBySameAgeWithSome() {
        Cat[] cats = new Cat[]{new Cat(3, "MiMi", 5), new Cat(5, "DaHuang", 4), new Cat(3, "XiaoHei", 3)};
        Arrays.sort(cats);
        assertEquals(cats[0].name(), "MiMi");
        assertEquals(cats[1].name(), "XiaoHei");
        assertEquals(cats[2].name(), "DaHuang");
    }
}

对存储在集合中的Comparable对象进行搜索、计算极限值以及自动维护也同样简单。如，下面程序依赖于String实现了Comparable接口，去掉了命令行参数列表中的重复参数，并按字母顺序打印出来：
package test;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class CatSortAlgTest {
    @Test
    public void testCollectionAddAll() {
        String[] strings = {"world", "hello", "world"};
        Set<String> stringSet = new TreeSet<>();
        Collections.addAll(stringSet, strings);
        System.out.println(stringSet);
    }
}
输出：
[hello, world]

一旦类实现了Comparable接口，它就可以跟许多泛型算法（generic algorithm）以及依赖于该接口的集合实现（collection implementation）进行协作。付出很小的努力就可以获得非常强大的功能。Java平台类库中的所有值类（value classes）都实现了Comparable接口。如果正在编写一个值类，它具有非常明显的内存排序关系，比如按字母排序、按数值顺序或者按年代顺序，你就应该坚决考虑实现这个接口：
public interface Comparable<T> {
    int compareTo(T t);
}

compareTo方法的通用约定与equals方法的相似：
将这个对象与指定的对象进行比较。当该对象小于、等于或者大于指定对象的时候，分别返回一个负整数、零或者正整数。如果由于指定对象的类型而无法与该对象进行比较，则抛出ClassCastException异常。

在下面的说明中，符号sgn（表达式）表示数学中的signum函数，它根据表达式（expression）的值为负值、零和正值，分别返回-1 、0或者1。
--实现者必须确保所有的x和y都满足sgn(x.compareTo(y)) == -sgn(y.compareTo(x))。（这也暗示着，当且仅当y.compareTo(x)抛出异常时，x.compareTo(y)才必须抛出异常。）
--实现者还必须确保这个比较关系是传递的：(x.compareTo(y) > 0 && y.compareTo(z) > 0)暗示着x.compareTo(z) > 0 。
--实现者必须确保x.compareTo(y) == 0 暗示着所有的z都满足sgn(x.compareTo(z)) == sgn(y.compareTo(z))。
--强烈建议(x.compareTo(y) == 0) == (x.equals(y))，但这并非绝对必要。一般来说，任何实现了Comparable接口的类，若违反了这个条件，都应该明确予以说明。推荐使用这样的说法：“注意：该类具有内存的排序功能，但是与equals不一致。”

与equals不同，在跨越不同类的时候，compareTo可以不做比较：如果两个被比较的对象引用不同类的对象，compareTo可以抛出ClassCastException异常。

就好像违反了hashCode约定的类会破坏其他依赖于散列做法的类一样，违反compareTo约定的类也会破坏其他依赖于比较关系的类。依赖于比较关系的类包括有序集合类TreeSet和TreeMap，以及工具类Collections和Arrays，它们内部包含有搜索和排序算法。

回顾一下compareTo约定中的条款。第一条指出，如果颠倒了两个对象引用 之间的比较方法，就会发生下面的情况：如果第一个对象小于第二个对象，则第二个对象一定大于第一个对象；如果第一个对象等于第二个对象，则第二个对象一定等于第一个对象；如果第一个对象大于第二个对象，则第二个对象一定小于第一个对象。第二条指出，如果一个对象大于第二个对象，并且第二个对象又大于第三个对象，那么第一个对象一定大于第三个对象。最后一条指出，在比较时被认为相等的所有对象，它们跟别的对象做比较时一定会产生同样的结果。

这三个条款的一个直接结果是，由compareTo方法施加的等同性测试（equality test），也一定遵守相同于equals约定所施加的限制条件：自反性、对称性和传递性。因此，下面的告诫也同样适用：无法在用新的值组件扩展可实例化的类时，同时保持compareTo约定，除非愿意放弃面向对象的抽象优势（见第8条）。针对equals的权宜之计也同样适用于compareTo方法。如果想为一个实现了Comparable接口的类增加值组件，请不要扩展这个类；而是要编写一个不相关的类，其中包含第一个类的一个实例。然后提供一个“视图（view）”方法返回这个实例。这样既可以让你自由地在第二个类上实现compareTo方法，同时也允许你的客户端在必要的时候，把第二个类的实例视同第一个类的实例。

compareTo约定的最后一段是一个强烈的建议，而不是真正的规则，只是说明了compareTo方法施加的等同性测试，在通常情况下应该返回与equals方法同样的结果。如果遵守了这一条，那么由compareTo方法所施加的顺序关系就认为“与equals一致（consistent with equals）”。如果违反了这条规则，顺序关系就被认为“与equals不一致（inconsistent with equals）”。如果一个类的compareTo方法施加了一个与equals方法不致的顺序关系，它仍然能够正常工作，但是，如果一个有序集合（sorted collection）包含了该类的元素，这个集合就可能无法遵守相应集合接口（Collection、Set或Map）的通用约定。这是因为，对于这些接口的通用约定是按照equals方法来定义的，但是有序集合使用了由compareTo方法而不是equals方法所施加的等同性测试。尽管出现这种情况下不会等成灾难性的后果，但是应该有所了解。

如，考虑BigDecimal类，它的compareTo方法与equals不一致。如果创建了一个HashSet实例，并且添加new BigDecimal("1.0")和new BigDecimal("1.00")，这个集合就将包含两个元素，因为新增到集合中的两个BigDecimal实例，通过equals方法来比较时是不相等的。然而，如果使用TreeSet而不是HashSet来执行同样的过程，集合中将只包含一个元素，因为这两个BigDecimal实例在通过compareTo方法进行比较时是相等的。
//
package test;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.TreeSet;

public class BigDecimalTest {
    @Test
    public void testCollectionAddAll() {
        HashSet<BigDecimal> bigDecimalHashSet = new HashSet<>();
        bigDecimalHashSet.add(new BigDecimal("1.0"));
        bigDecimalHashSet.add(new BigDecimal("1.00"));
        System.out.println(bigDecimalHashSet);

        TreeSet<BigDecimal> bigDecimalTreeSet = new TreeSet<>();
        bigDecimalTreeSet.add(new BigDecimal("1.0"));
        bigDecimalTreeSet.add(new BigDecimal("1.00"));
        System.out.println(bigDecimalTreeSet);
    }
}
输出：
[1.0, 1.00]
[1.0]

编写compareTo方法与编写equals方法非常相似，也有几处重大差别。因为Comparable接口是参数化的，而且comparable（这里错了吧，感觉应该是compareTo）方法是静态的类型，因此不必进行类型检查，也不必对它的参数进行类型转换。如果参数的类型不合适，这个调用甚至无法编译。如果参数为null，这个调用应该抛出NullPointerException异常，并且一旦该方法试图访问它的成员时就应该抛出。

compareTo方法中域的比较是顺序的比较，而不是等同性的比较。比较对象引用域可以是通过递归地调用compareTo方法来实现。如果一个域并没有实现Comparable接口，或者你需要使用一个非标准的排序关系，就可以使用一个显式的Comparator来代替。或者编写自己的Comparator，或者使用已有的Comparator，譬如针对第8条中CaseInsensitiveString类的这个compareTo方法使用一个已有的Comparator：
public final class CaseInsensitiveString implements Comparable<CaseInsensitiveString> {
    public int compareTo(CaseInsensitiveString cis) {
        return String.CASE_INSENSITIVE_ORDER.compare(s, cis.s);
    }
    
    // Remainder omitted
}

System.out.println(String.CASE_INSENSITIVE_ORDER.compare("hello", "HELLO"));//0
System.out.println(String.CASE_INSENSITIVE_ORDER.compare("hello", "IELLO"));//-1
System.out.println(String.CASE_INSENSITIVE_ORDER.compare("hello", "GELLO"));//1

注意CaseInsensitiveString类实现了Comparable<CaseInsensitiveString>接口。CaseInsensitiveString引用只能与其他的Comparable<CaseInsensitiveString>引用进行比较。在声明类去实现Comparable接口时，这是常见的模式。还要注意compareTo方法的参数是CaseInsensitiveString，而不是Object。这是上述的类声音所要求的。

比较整数型基本类型的域，可以使用关系操作符<和>。如，浮点域用Double.compare和Float.compare，而不用关系操作符，当应用到浮点值时，它们没有遵守compareTo的通用约定。对于数组域，则要把这些指导原则应用到每个元素上。

如果一个类有多个关键域，则按什么样的顺序来比较这些域是非常关键的。必须从最关键的域开始，逐步进行到所有的重要域。如果某个域的比较产生了非零的结果（零代表相等），则整个比较操作结束，并返回该结果。如果最关键的域是相等的，则进一步比较次最关键的域，以此类推。如果所有的域都是相等的，则对象就是相等的，并返回零。通过第9条中的PhoneNumber类的compareTo方法来说明这种方法：
public int compareTo(PhoneNumber pn) {
    //compare area codes
    if (areaCode < pn.areaCode)
        return -1;
    if (areaCode > pn.areaCode)
        return 1;
    
    //area codes are equal, compare prefixes
    if (prefix < pn.prefix)
        return -1;
    if (prefix > pn.prefix)
        return 1;
    
    //area codes and prefixes are equal, compare line numbers
    if (lineNumber < pn.lineNumber)
        return -1;
    if (lineNumber > pn.lineNumber)
        return 1;
    
    return 0; //All fields are equal
}

虽然这个方法可选，但它还可以改进。compareTo方法的约定并没有指定返回值的大小（magnitude），而只是指定了返回值的符号。可以利用这一点来简化代码，或者还能提高运行速度：
public int compareTo(PhoneNumber pn) {
    //Compare area codes
    int areaCodeDiff = areaCode - pn.areaCode;
    if (areaCodeDiff != 0)
        return areaCodeDiff;
    
    //Area codes are equal, compare prefixes
    int prefixDiff = prefix - pn.prefix;
    if (prefixDiff != 0)
        return prefixDiff;
    
    //Area codes and prefixes are equal, compare line numbers
    return lineNumber - pn.lineNumber;
}

这项技巧在这里能够工作得很好，但是用起来要非常小心。除非你确信相关的域不会为负值，或者更一般的情况：最小和最大的可能域值之差小于或等于INTEGER.MAX_VALUE(2^32 -1)，否则就不要使用这种方法。这项技巧有时候不能正常工作的原因在于，一个有符号的32位整数还没有大到足以表达任意两个32位整数的差。如果i是一个很大的正整数（int类型），而j是一个很大的负整数（int类型），那么（i - j）将会溢出，并返回一个负值。这样就使得compareTo方法将对某些参数返回错误的结果，违反了compareTo约定的第一条和第二条。这不是一个纯粹的理论问题：它已经在实际的系统中导致了失败。这些失败可能非常难以调试，因为这样的compareTo方法对于大多数的输入值都能正常工作。
//------------------------------------------------------------------------------------------------
//Effective Java 第4章 类和接口 P58
//第13条：使类和成员的可访问性最小化 P58
设计良好的模块会隐藏所有的实现细节，把它的API与它的实现清晰地隔离开来。模块之前只通过它们的API进行通信，一个模块不需要知道其他模块的内部工作情况。这个概念被称为信息隐藏（information hiding）或封装（encapsulation），是软件设计的基本原则之一。

信息隐藏的几个重要原因：
它可以有效地解除组成系统的各模块之间的耦合关系，使得这些模块可以独立地开发、测试、优化、使用、理解和修改。这样可以加快系统开发的速度，因为这些模块可以并行开发。
它也减轻了维护的负担，程序员可以更快地理解这些模块，并且在调试它们的时候可以不影响其他模块。
信息隐藏本身无论是对内还是对外，都不会带来更好的性能，但是它可以有效地调节性能：一旦完成一个系统，并通过剖析确定了哪些模块影响了系统的性能（见第55条），那些模块就可以被进一步优化，而不是影响到其他模块的正确性。
信息隐藏提高了软件的可重用性，因为模块之间并不紧密相连，除了开发这些模块所使用的环境之外，它们在其他的环境中往往也很有用。
信息隐藏也降低了构建大型系统的风险，因为即便整个系统不可用，但是这些独立的模块却有可能是可用的。

Java程序设计语言提供了许多机制（facility）来协助信息隐藏。访问控制（access control）机制决定了类、接口和成员的可访问性（accessibility）。实体的可访问性是由该实体声音所在的位置，以及该实体声明中所出现的访问修饰符（private、protected和public）共同决定的。正确地使用这些修饰符对于实现信息隐藏是非常关键的。

第一规则很简单：尽可能地使每个类或者成员不被外界访问。换句话说，应该使用与你正在编写的软件的对应功能相一致的、尽可能最小的访问级别。

对于顶层的（非嵌套的）类和接口，只有两种可能的访问级别：包级私有的（package-private）和公有的（public）。如果你用public修饰符声明了顶层类或者接口，那它就是公有的；否则，它将是包级私有的。如果类或者接口能够被做成包级私有的，它就应该被做成包级私有。通过把类或者接口做成包级私有，它实际上成了这个包的实现的一部分，而不是该包导出的API的一部分，在以后的发行版本中，可以对它进行修改、替换，或者删除，而无需担心会影响到现有的客户端程序。如果把它做成公有的，你就有责任永远支持它，以保持它们的兼容性。

如果一个包级私有的顶层类（或者接口）只是在某一个类的内部被用到，就应该考虑使它成为唯一使用它的那个类的私有嵌套类（见第22条）。这样可以将它的可访问范围从包中的所有类缩小到了使用它的那个类。然而，降低不必要公有类的可访问性，比降低包级私有的顶层类的更重要得多：因为公有类是包的API的一部分，而包级私有的顶层类则已经是这个包的实现的一部分。

对于成员（域、方法、嵌套类和嵌套接口）有四种可能的访问级别，下面按照可访问性的递增顺序罗列出来：
--私有的（private）——只有在声明该成员的顶层类内部才可以访问这个成员。
--包级私有的（package-private）——声明该成员的包内部的任何类都可以访问这个成员。从技术上讲，它被称为“缺省（default）访问级别”，如果没有为成员指定访问修饰符，就采用这个访问级别。
--受保护的（protected）——声明该成员的类的子类可以访问这个成员（但有一些限制），并且，声明该成员的包内部的任何类也可以访问这个成员。
--公有的（public）——在任何地方都可以访问该成员。

当你设计了类的公有API之后，可能觉得应该把所有其他的成员都变成私有的。其实，只有当同一个包内的另一个类真正需要访问一个成员的时候，才应该删除private修饰符，使该成员变成包级私有的。如果你发现自己经常要做这样的事情，就应该重要检查你的系统设计，看看是否另一种分解方案所得到的类，与其他类之间的耦合度会更小。也就是说，私有成员和包级私有成员都是一个类的实现中的一部分，一般不会影响它的导出的API。然而，如果这个类实现了Serializable接口（见第74条和第75条），这些域就可能被“泄露（leak）”到导出的API中。

对于公有类的成员当访问级别从包级私有变成保护级别时，会大大增强可访问性。受保护的成员是类的导出的API的一部分，必须永远得到支持。导出的类的受保护成员也代表了该类对于某个实现细节的公开承诺（见第17条）。受保护的成员应该尽量少用。

有一条规则限制了降低方法的可访问性的能力。如果方法覆盖了超类中的一个方法，子类中的访问级别就不允许低于超类中的访问级别。这样可以确保任何可使用超类的实例的地方也都可以使用子类的实例。如果违反了这条规则，那试图编译该子类的时候，编译器就会产生一条错误消息。这条规则有种特殊的情形：如果一个类实现了一个接口，那么接口中所有的类方法在这个类中也都必须被声明为公有的。之所以如此，是因为接口中的所有方法都隐含着公有访问级别。

为了便于测试，可以试着使类、接口或者成员变得更容易访问。这么做在一定程度上来说是好的。为了测试而将一个公有类的私有成员变成包级私有的，这还可以接受，但是要将访问级别提高到超过它，这就无法接受了。换句话说，不能为了测试，而将类、接口或者成员变成包的导出的API的一部分。幸运的是，也没有必要这么做，因为可以让测试作为被测试的包的一部分来运行，从而能够访问它的包级私有的元素。

实例域决不能是公有的（见第14条）。如果域是非final的，或者是一个指向可变对象的final引用，那么一旦使这个域成为公有的，就放弃了对存储在这个域中的值进行限制的能力；这意味着，你也放弃了强制这个域不可变的能力。同时，当这个域被修改的时候，你也失去了对它采取任何行动的能力。因此，包含公有可变域的类并不是线程安全的。即便域是final的，并且引用不可变的对象，当把这个域变成公有的时候，也就放弃了“切换到一种新的内部数据表示法”的灵活性。

同样的建议也适用于静态域，只是有一种例外情况。假设常量构成了类提供的整个抽象中的一部分，可以通过公有的静态final域来暴露这些常量。按惯例，这种域的名称由大写字母组成，单词之间用下划线隔开（见第56条）。很重要的一点是，这些域要么包含基本类型的值，要么包含指向不可变对象的引用（见第15条）。如果final域包含可变对象的引用 ，它便具有非final域的所有缺点。虽然引用本身不能被修改，但是它所引用的对象却可以被修改——这会导致灾难性的后果。

长度非零的数组总是可变的，所以，类具有公有的静态final数组域，或者返回这种域的访问方法，这几乎总是错误的。如果类具有这样的域或者访问方法，客户端将能够修改数组中的内容。这是安全漏洞的一个常见根源：
// Potential security hole!
public static final Thing[] VALUES = {...};

要注意，许多IDE会产生返回指向私有数组域的引用的访问方法，这样就会产生这个问题。修改这个问题有两种方法。可以使用公有数组变成私有的，并增加一个公有的不可变列表：
private static final Thing[] PRIVATE_VALUES = {...};
public static final List<Thins> VALUES = Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));

另一种方法是，可以使数组变成私有的，并添加一个公有方法，它返回私有数组的一个备份：
private static final Thing[] PRIVATE_VALUES = {...};
public static final Thing[] values() {
    return PRIVATE_VALUES.clone();
}

要在这两种方法之间做出选择，得考虑客户端可能怎么处理这个结果。哪种返回类型更加方法，哪种会得到更好的性能。

总而言之，应该始终尽可能地降低可访问性。在仔细地设计了一个最小的公有API之后，应该防止把任何散乱的类、接口和成员变成API的一部分。除了公有静态final域特殊情形之外，公有类都不应该包含公有域。并且要确保公有静态final域所引用的对象都是不可变的。
//------------------------------------------------------------------------------------------------
//Effective Java 第4章 类和接口 P58
//第14条：在公有类中使用访问方法而非公有域 P62
对于公有类，坚持面向对象程序设计思想的看法是正确的：如果类可以在它所在的包的外部进行访问，就提供访问方法，以保留将来改变该类的内部表示法的灵活性。如果公有类暴露了它的数据域，要想在将来改变其内部表示法是不可能的，因为公有类的客户端代码已经遍布各处了。

然而，如果类是包级私有的，或者是私有的嵌套类，直接暴露它的数据域并没有本质的错误——假设这些数据域确实描述了该类所提供的抽象。这种方法比访问方法的做法更不会产生视觉混乱，无论是在类定义中，还是在使用该类的客户端代码中。虽然客户端代码与该类的内部表示法紧密相连，但是这些代码被限定在包含该类的包中。如有必要，不改变包之外的任何代码而只改变内部数据表示法也是可以的。在私有嵌套类的情况下，改变的作用范围被进一步限制在外围类中。

Java平台类库中有几个类违反了“公有类不应该直接暴露数据域”的告诫。显著的例子包括java.awt包中的Point和Dimension类。它们是反面的示例。如第55条中所述，决定暴露Dimension类的内部数据等成了严重的性能问题，而且，这个问题至今依然存在。

让公有类直接暴露域从来都不是种好办法，但是如果域是不可变的，这种做法的危害就比较小一些。如果不改变类的API，就无法改变这种类的表示法，当域被读取的时候，也无法采取辅助的行动，但是可以强加约束条件。如，这个类确保了每个实例都表示一个有效的时间：
// Public class with exposed immutable fields - questionable
public final class Time {
    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;
    
    public final int hour;
    public final int minute;
    
    public Time(int hour, int minute) {
        if (hour < 0 || hour >= HOURS_PER_DAY) {
            throw new IllegalArgumentException("Hour: " + hour);
        }
        if (minute < 0 || minute >= MINUTES_PER_HOUR) {
            throw new IlleagalArgumentException("Min: " + minute);
        }
        this.hour = hour;
        this.minute = minute;
    }
    
    // Remainder omitted
}

总之，公有类永远都不应该暴露可变的域。虽然还是有问题，但是让公有类暴露不可变的域其危害比较小。但是有时候会需要用包级私有的或者私有的嵌套类来暴露域，无论这个类是可变还是不可变的。
//------------------------------------------------------------------------------------------------
//Effective Java 第4章 类和接口 P58
//第15条：使可变性最小化 P58
不可变类只是其实例不能被修改的类。每个实例中包含的所有信息都必须在创建该实例的时候就提供，并在对象的整个生命周期（lifetime）内固定不变。Java平台类库中包含许多不可变的类，其中有String、基本类型的包装类、BigInteger和BigDecimal。存在不可变的类有许多理由：不可变的类比可变类更加易于设计、实现和使用。它们不容易出错，且更加安全。

为了使类成为不可变，要遵循下面五条规则：
1. 不要提供任何会修改对象状态的方法（也称为mutator）。
2. 保证类不会被扩展。这样可以防止粗心或者恶意的子类假装对象的状态已经改变，从而破坏该类的不可变行为。为了防止子类化，一般做法是使这个类成为final的，后面讨论其他的做法。
3. 使所有的域都是final的。通过系统的强制方式，这可以清楚地表明意图。而且，如果一个指向新创建实例的引用在缺乏同步机制的情况下，从一个线程被传递到另一个线程，就必须确保正确的行为，正如内存模型中所述。
4. 使所有的域都成为私有的。这样可以防止客户端获得访问被域引用的可变对象的权限，并防止客户端直接修改这些对象。虽然从技术上讲，允许不可变的类具有公有的final域，只要这些域包含基本类型的值或者指向不可变对象的引用，但是不建议这样做，因为这样会使得在以后的版本中无法再改变内部的表示法（见第13条）。
5. 确认对于任何可变组件的互斥访问。如果类具有指向可变对象的域，则必须确保该类的客户端无法获得指向这些对象的引用。并且，永远不要用客户端提供的对象引用来初始化这样的域，也不要从任何访问方法（accessor）中返回该对象引用。在构造器、访问方法和readObject方法（见第76条）中请使用保护性拷贝（defensive copy）技术（见第39条）。

前面条目中许多例子都是不可变的，其中一个例子是第9条中的PhoneNumber，它针对每个属性都有访问方法（accessor），但是没有对应的设值方法（mutator）。下面是个稍微复杂的例子：
//
package test;

public final class Complex {
    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double realPart() {
        return re;
    }

    public double imaginaryPart() {
        return im;
    }

    public Complex add(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    public Complex substract(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    public Complex multiply(Complex c) {
        return new Complex(re * c.re - im * c.im,
            re * c.im + im * c.re);
    }

    public Complex divide(Complex c) {
        double tmp = c.re * c.re + c.im * c.im;
        return new Complex((re * c.re + im * c.im) / tmp,
            (im * c.re - re * c.im) / tmp);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Complex)) {
            return false;
        }

        Complex c = (Complex) o;

        // See page 43 to find out why we use compare instead of ==. See the 12th Instruction.
        return Double.compare(re, c.re) == 0 &&
            Double.compare(im, c.im) == 0;

    }

    @Override
    public int hashCode() {
        int result = 17 + hashDouble(re);
        result = 31 * result + hashDouble(im);
        return result;
    }

    @Override
    public String toString() {
        return "(" + re + " + " + im + "i)";
    }

    private int hashDouble(double val) {
        long longBits = Double.doubleToLongBits(val);
        return (int) (longBits ^ (longBits >>> 32));
    }
}

这个类表示一个复数（complex number，具有实部和虚部）。除了标准的Object方法，还提供了针对实部和虚部的访问方法，以及4种基本的算术运算：加法、减法、乘法和除法。
注意这些算法运算是如何创建并返回新的Complex实例，而不是修改这个实例。大多数重要的不可变类都使用了这种模式。它被称为函数的（functional）做法，因为这引起方法返回了一个函数的结果，这些函数对操作数进行运行但不修改它。与之相对应的更常见的是过程的（procedural）或者命令式的（imperative）做法，使用这些方式时，将一个过程作用在它们的操作数上，会导致它的状态发生改变。

函数方式做法带来了不可变性，具有许多优点。不可变对象比较简单。不可变对象可以只有一种状态，即被创建时的状态。如果能够确保所有的构造器都建立了这个类的约束方法，就可以确保这些约束关系在整个生命周期内永远不再发生变化，你和使用这个类的程序员都无需再做额外的工作来维护这些约束关系。另一方法，可变的对象可以有任意复杂的状态空间。如果文档中没有对mutator方法所执行的状态转换提供精确的描述，要可靠地使用一个可变类是非常困难的，甚至是不可能的。

不可变对象本质上是线程安全的，它们不要求同步。当多个线程并发访问这样的对象时，它们不会遭到破坏。这是获得线程安全最容易的方法。实际上，没有任何线程会注意到其他线程对于不可变对象的影响。所以，不可变对象可以被自由地共享。不可变类应该充分利用这种优势，鼓励客户端尽可能地重用现有的实例。要做到这一点，一个很简便的办法就是，对于频繁用到的值，为它们提供公有的静态final常量。如，Complex类可能会提供下面的常量：
public static final Complex ZERO = new Complex(0, 0);
public static final Complex ONE = new Complex(1, 0);
public static final Complex I = new Complex(0, 1);

这种方法可以被进一步扩展。不可变的类可以提供一些静态工厂（见第1条），它们把频繁被请求的实例缓存起来，从而当现在实例可以符合请求的时候，就不必创建新的实例。所有基本类型的包装类和BigInteger都有这样的静态工厂。使用这样的静态工厂也复合客户端之间可以共享现在的实例，而不用创建新的实例，从而降低内存占用和垃圾回收的成本。在设计新的类时，选择用静态工厂代替公有的构造器可以让你以后有添加缓存的灵活性，而不必影响客户端。

“不可变对象可以被自由地共享”导致的结果是，永远也不需要进行保护性拷贝（见第39条）。实际上，根本无需做任何拷贝，这些拷贝始终等于原始的对象。因为，不需要，也不应该为不可变的类提供clone方法或者拷贝构造器（copy constructor，见第11条）。这一点在Java平台的早期并不好理解，所以String类仍然具有拷贝构造器，但是应该尽量少用它（见第5条）。

不仅可以共享不可变对象，甚至也可以共享它们的内部信息。例如，BigInteger类内部使用了符号数值表示法。符号用一个int类型的值来表示，数值则用一个int数组表示。negate方法产生一个新的BigInteger，其中数值是一样的，符号则是相反的。它并不需要拷贝数组；新建的BigInteger也指向原始实例中的同一个内部数组。

不可变对象为其他对象提供了大量的构造（building blocks），无论是可变的还是不可变的对象。如果知道一个复杂对象内部的组件对象不会改变，要维护它的不变性约束是比较容易的。这条原则的一种特例在于，不可变对象构成了大量的映射键（map key）和集合元素（set element）；一旦不可变对象进入到映射（map）或者集合（set）中，尽管这破坏了映射或者集合的不变性约束，但是也不用担心它们的值会发生变化。（没看懂？？）

不可变类真正唯一的缺点是，对于每个不同的值都需要一个单独的对象。创建这种对象的代价可能很高，特别是对于大型对象的情形。如，假设你有一个上百万位的BigInteger，想要改变它的低位：
BigInteger moby = ...;
moby = moby.flipBit(0);

flipBit方法创建了一个新的BigInteger实例，也有上百万位长，它与原来的对象只差一位不同。这项操作所消耗的时间和空间与BigInteger的长度成正比。拿它与java.util.BitSet进行比较。与BigInteger类似，BigSet代表一个任意长度的位序列，但是与BigInteger不同的是，BitSet是可变的。BitSet类提供了一个方法，允许在固定时间（constant time）内改变此“百万位”实例中单个位的状态。

BigInteger有一个包级私有的可变“配套类（companing class）”，它的用途是加速诸如“模指数（modular exponentiation）”这样的多步骤操作。由于前面提到的诸多原因，使用可变的配套类比使用BigInteger要困难得多，但幸运的是，并不需要这样做。因为BigInteger的实现者已经替你完成了所有的困难工作。

如果能够精确地预测出客户端将要在不可变的类上执行哪些复杂的多阶段操作，这种包级私有的可变配套类的方法可以工作得很好。如果无法预测，最好的办法就是提供一个公有的可变配套类。在Java平台类库中，这种方法的主要例子是String类，它的可变配套类是StringBuilder（不是线程安全的，单线程使用速度快）（和基本上已经废弃的StringBuffer，线程安全的）。可以这样认为，在特定的环境下，相对于BigInteger而言，BitSet同样扮演了可变配套类的角色。

为了确保不可变性，类绝对不允许自身被子类化。除了“使类成为final的”这种方法之外，还有另外一种更加灵活的办法可以做到这一点。让不可变的类变成final的另一种办法就是，让类的所有构造器都变成私有的或者包级私有的，并添加公有的静态工厂（static factory）来代替公有的构造器（见第1条）。

以Complex为例：
// Immutable class with static factories instead of constructors
public class Complex {
    private final double re;
    private final double im;

    private Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }
    
    public static Complex valueOf(double re, double im) {
        return new Complex(re, im);
    }
    
    // Remainder unchanged
}

虽然这种方法并不常用，但它经常是最好的替代方法。它最灵活，因为它允许使用多个包级私有的实现类。对于处在它的包外部的客户端而言，不可变的类实际上是final的，因为不可能把来自另一个包的类、缺省公有的或受保护的构造器的类进行扩展。除了允许多个实现类的灵活性之外，这种方法还使得有可能通过改善静态工厂的对象的缓存能力，在后续的发行版本中改进该类的性能。

静态工厂与构造器相比具有许多其他的优势，如第1条中所讨论的。如，假设希望提供一种“基于极坐标创建复数”的方式。如果使用构造器来实现这样的功能，可能会使得这个类很零乱，因为这样的构造器与已用的构造器Complex(double, double)具有相同的签名。通过静态工厂，这很容易做到。只需添加第二个静态工厂，并且工厂的名字清楚地表明了它的功能即可：
public static Complex valueOfPolar(double r, double theta) {
    return new Complex(r * Math.cos(theta), r * Math.sin(theta));
}

当BigInteger和BIgDecimal刚被编写出来的时候，对于“不可变的类必须为final的”还没有得到广泛地理解，所以它们的所有方法都有可能会被覆盖。遗憾的是，为了保持向后兼容，这个问题地直无法得以修正。如果在编写一个类，它的安全性依赖于（来自不可信客户端的）BigInteger或者BigDecimal参数的不可变性，就必须进行检查，以确定这个参数是否为“真正的”BigInteger或者BigDecimal，而不是不可信任子类的实例。如果是后者的话，就必须在假设它可能是可变的前提下对它进行保护性拷贝（见第39条）：
public static BigInteger safeInstance(BigInteger val) {
    if (val.getClass() != BigInteger.class) {
        return new BigInteger(val.toByteArray());
    }
    return val;
}

本条目形状处关于不可变类的诸多规则指出：没有方法会修改对象，并且它的所有域都必须是final的。实际上，这些规则比真正的要求更强硬了一点，为了提供性能可以有所放松。事实上应该：没有一个方法能够对对象的状态产生外部可见（externally visible）的改变。然而，许多不可变的类拥有一个或者多个非final的域，它们在第一次被请求执行这些计算的时候，把一些开销昂贵的计算结果缓存在这些域中。如果将来再次请求同样的计算，就直接返回这些缓存的值，从而节约了重新计算所需要的开销。这种技巧可以很好的工作，因为对象是不可变的，它的不可变性保持了这些计算如果被再次执行，就会产生同样的结果。
如，PhoneNumber类的hashCode方法（见第9条）在第一次被调用的时候，计算出散列码，然后把它缓存起来，以备将来被再次调用时使用。这种方法是延迟初始化（lazy initialization）（见第71条）的一个例子，String类也用到了。

有关序列化功能的一条告诫有必要在这里提出来。如果选择让自己的不可变类实现Serializable接口，并且它包含一个或者多个指向可变对象的域，就必须提供一个显式的readObject或者readResolve方法，或者使用ObjectOutputStream.writeUnshared和ObjectInputStream.readUnshared方法，即便默认的序列化形式是可以接受的，也是如此。否则攻击者可能从不可变的类创建可变的实例。这个话题的详细内容请参见第76条。

总之，坚决不要为每个get方法编写一个相应的set方法。除非有很好的理由要让类成为可变的类，否则就应该是不可变的。不可变的类有许多优点，唯一缺点是在特定的情况下存在潜在的性能问题。应该问题使一些小的值对象，比如PhoneNumber和Complex，成为不可变的（在Java平台类库中，有几个类如java.util.Date和java.awt.Point，它们本应该是不可变的，但实际上却不是）。也应该认真考虑把一些较大的值对象做成不可变的，如String和BigInteger。只能当确认有必要实现令人满意的性能时（见第55条），才应该为不可变的类提供公有的可变配套类。

对于有些类而言，其不可变性是不切实际的。如果类不能被做成是不可变的，仍然应该尽可能地限制它的可变性。降低对象可以存在的状态数，可以更容易地分析该对象的行为，同时降低出错的可能性。因此，除非有令人信服的理由要使域变成是非final的，否则要使每个域都是final。

构造器应该创建完全初始化的对象，并建立起所有的约束关系。不要在构造器或者静态工厂之外再提供公有的初始化方法，除非有令人信服的理由必须这么做。同样的，也不应该提供“重新初始化”方法（它使得对象可以被重用，就好像这个对象是由另一不同的初始状态构造出来一样）。与所增加的复杂性相比，“重新初始化”方法通常并没有带来太多的性能优势。

可以通过TimerTask类来说明这些原则。它是可变的，但是它的状态空间被有意地设计得非常小。你可以创建一个实例，对它进行调度使它执行起来，也可以随意地取消它。一旦一个定时器任务（timer task）已经完成，或者已经被取消，就不可能再对它重新调度。
//------------------------------------------------------------------------------------------------
//Effective Java 第4章 类和接口 P58
//第16条：复合优先于继承 P71
继承（inheritance）是实现代码重用的有力手段，但它并非永远是完成这项工作的最佳工具。使用不当会导致软件变得很脆弱。在包的内部使用继承是非常安全的，在那里，子类和超类的实现都处在同一个程序员的控制之下。对于专门为了继承而设计、并且具有很好的文档说明的类来说（见第17条），使用继承也是非常安全的。然而，对普通的具体类（concrete class）进行跨越包边界的继承，则是非常危险的。本书使用“继承”一词，含义是实现继承（implementation inheritance，当一个类扩展另一个类的时候）。本条目中讨论的问题并不适用于接口继承（interface inheritance，当一个类实现一个接口的时候，或者当一个接口扩展另一个接口的时候）。

与方法调用不同的是，继承打破了封装性。子类依赖于其超类的特定功能的实现细节。超类的实现有可能会随着发行版本的同不而有所变化，如果真的发生了变化，子类可能会遭到破坏，即便它的代码完全没有改变。因而，子类必须要跟着其超类的更新而演变，除非超类是专门为了扩展而设计的，并且具有很好的文档说明。

为了明说具体，假设有一个程序使用了HashSet。为了调优该程序的性能，需要查询HashSet，看一看自从它被创建以来曾经添加了多少个元素（不要与它当前的元素混淆起来，元素数目会随着元素的删除而递减）。为了提供这种功能，我们得编写一个HashSet变量，它记录下试图插入的元素数量，并针对该计数值导出一个访问方法。HashSet类包含两个可以增加元素的方法：add和addAll，因此两个方法都要覆盖：
//
package test;

import java.util.Collection;
import java.util.HashSet;

// Broken - Inappropriate use of inheritance!
public class InstrumentedHashSet<E> extends HashSet<E> {
    // The number of attempted element insertions
    private int addCount = 0;

    public InstrumentedHashSet() {

    }

    public InstrumentedHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    @Override
    public boolean add(E e) {
        ++addCount;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}

//
package test;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class InstrumentedHashSetTest {
    @Test
    public void testAddAll() {
        InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
        s.addAll(Arrays.asList("hello", "world", "good"));
        assertEquals(3, s.getAddCount());
    }
}
结果：
java.lang.AssertionError: 
Expected :3
Actual   :6

这个类看起来非常合理，但它并不能正常工作。
我们期望getAddCount方法将会返回3，实际上返回6。HashSet内部，addAll方法是基于它的add方法来实现的，即便HashSet方法中并没有说明这样的实现细节，这也是合理的。

只要去掉被覆盖的addAll方法，就可以“修正”这个子类。虽然这样得到的类可以正常工作，但是它的功能正确性则需要依赖于这样的事实：HashSet的addAll方法是在它的add方法上实现的。这种“自用性（self-use）”是实现细节，不是承诺，不能保证在Java平台的所有实现中都保持不变，不能保证随着发行版本的不同而不发生变化。因此，这样得到的InstrumentedHashSet类将是非常脆弱的。

稍微好一点的做法是，覆盖addAll方法来遍历指定的集合，为每个元素调用一次add方法。这样做可以保证得到正确的结果，不管HashSet的addAll方法是否是在add方法的基础上实现，因为HashSet的addAll实现将不会再被调用到。然而，这项技术并没有解决所有的问题，它相当于重新实现了超类的方法，这些超类的方法可能是自用的（self-use），也可能不是自用的，这种方法很困难，也非常耗时，并且容易出错。

导致子类脆弱的一个相关的原因是，它们的超类在后续的发行版本中可以获得新的方法。假设一个程序的安全性依赖于这样的事实：所有被插入到某个集合中的元素都满足某个先决条件。下面的做法就可以确保这一点：对集合进行子类化，并覆盖所有能够添加元素的方法，以便 确保在加入每个元素之前它是满足这个先决条件的。如果在后续的发行版本中，超类中没有增加能够插入元素的新方法，这种做法就可以正常工作。然而，一旦超类增加了这样的新方法，则很可能仅仅由于调用了这个未被子类覆盖的新方法，而将“非法的”元素添加到子类的实例。

上面两个问题都来源于覆盖（overriding）动作。如果在扩展一个类的时候，仅仅是增加新的方法，而不覆盖现有的方法。虽然这种扩展方式比较安全一些，但是也并非完全没有风险。如果超类在后续的发行版本中获得了一个新的方法，并且不幸的是，你给子类提供了一个签名相同但返回类型不同的方法，那么这样的子类将无法通过编译。如果给子类提供的方法带有与新的超类方法完全相同的签名和返回类型，实际上就覆盖了超类中的方法，因此又回到上述的两个问题上去了。此外，你的方法是否能够遵守新的超类方法的约定，也是很值得怀疑的，因为当你在编写子类方法的时候，这个约定根本8没有面世。

有一种方法可以避免前面提到的所有问题。不用扩展现有的类，而是在新的类中增加一个私有域，它引用现有类的一个实例。这种设计被称做“复合（composition）”，因为现有的类变成了新类的一个组件。新类中的每个实例方法都可以调用被包含的现有类实例中对应的方法，并返回它的结果。这被称为转发（forwarding），新类中的方法被称为转发方法（forwarding method）。这样得到的类将会非常稳固，它不依赖于现有类的实现细节。即便现有的类添加了新的方法，也不会影响新的类。请看下面例子，它用复合/转发的方法来代替InstrumentedHashSet类。这个实现分为两部分：类本身和可重用的转发类（forwarding class），包含了所有的转发方法，没有其他方法：
//ForwardingSet
package test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
//装饰模式
public class ForwardingSet<E> implements Set<E> {
    private final Set<E> s;

    public ForwardingSet(Set<E> s) {
        this.s = s;
    }

    @Override
    public int hashCode() {
        return s.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return s.equals(obj);
    }

    @Override
    public String toString() {
        return s.toString();
    }

    @Override
    public int size() {
        return s.size();
    }

    @Override
    public boolean isEmpty() {
        return s.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return s.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return s.iterator();
    }

    @Override
    public Object[] toArray() {
        return s.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return s.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return s.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return s.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return s.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return s.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return s.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return s.removeAll(c);
    }

    @Override
    public void clear() {
        s.clear();
    }
}

//InstrumentedSet
package test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

// Wrapper class - uses composition in place of inheritance
public class InstrumentedSet<E> extends ForwardingSet<E> {
    private int addCount = 0;

    public InstrumentedSet(Set<E> s) {
        super(s);
    }

    @Override
    public boolean add(E e) {
        ++addCount;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}

//测试类
package test;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class InstrumentedSetTest {
    @Test
    public void testAddAll() {
        InstrumentedSet<String> s = new InstrumentedSet<>(new HashSet<String>());//InstrumentedSet<String> s = new InstrumentedSet<>(new HashSet<>());，这里HashSet<>如果不写明String类型，报错Error:(13, 37) java: 不兼容的类型: test.InstrumentedSet<java.lang.Object>无法转换为test.InstrumentedSet<java.lang.String>，为什么不能自动识别？我理解的类型自动识别是只对当前new类型的，而不能对构造函数中的未知入参类型识别
        s.addAll(Arrays.asList("hello", "world", "good"));
        assertEquals(3, s.getAddCount());
    }
}
测试通过

Set接口的存在使得InstrumentedSet类的设计成为可能，因为Set接口保存了HashSet类的功能特性。除了获得健壮性之外，这种设计也带来了格外的灵活性。InstrumentedSet类实现了Set接口，并且拥有单个构造器，它的参数也是Set类型。从本质上讲，这个类把一个Set转变成了另一个Set，同时增加了计数的功能。这里的包装类（wrapper class）可以被用来包装任何Set实现，并且可以结合任何先前存在的构造器一起工作。如：
Set<Date> s = new InstrumentedSet<Date>(new TreeSet<Date>(cmp));
Set<E> s2 = new InstrumentedSet<E>(new HashSet<E>(capacity));

InstrumentedSet类甚至也可以用来临时替换一个原来没有计数特性的Set实例：
static void walk(Set<Dog> dogs) {
    InstrumentedSet<Dog> iDogs = new InstrumentedSet<Dog>(dogs);
    ... // Within this method use iDogs instead of dogs
}

因为每一个InstrumentedSet实例都把另一个Set实例包装起来了，所以InstrumentedSet类被称为包装类（wrapper class）。这也正是Decorator模式，因为InstrumentedSet类对一个集合进行了修饰，为它增加了计数特性。有时候，复合和转发的结合也被错误地称为“委托（delegation）”。从技术的角度而言，这不是委托，除非包装对象把自身传递给被包装的对象。

包装类几乎没有什么缺点。需要注意的一点是，包装类不适合用在回调框架（callback framework）中，在回调框架中，对象把自身的引用 传递给其他的对象，用于后续的调用（“回调”）。因为被包装起来的对象并不知道它外面的包装对象，所以它传递一个指向自身的引用（this），回调时避开了外面的包装对象。这被称为SELF问题。

只有当子类真正是超类的子类型（subtype）时，才适合用继承。换句话说，对于 两个类A和B，只有当两者之间确实存在“is-a”关系的时候，类B才应该扩展类A。如果打算让类B扩展类A，就应该问问自己：每个B确实也是A吗。如果不能确定这个问题的答案是肯定的，那么B就不应该扩展A。如果答案是否定的，通常情况下，B应该包含A的一个私有实例，并且暴露一个较小的、较简单的API：A本质上不是B的一部分，只是它的实现细节布局。

如果在适合于使用复合的地方使用了继承，则会不必要地暴露实现细节。这样得到的API会把你限制在原始的实现上，永远限定了类的性能。更为严重的是，由于暴露了内部的细节，客户端就有可能直接访问这些内部细节。这样至少会导致语义上的混淆。例如，如果p指向Properties实例，那么p.getProperty(key)就有可能产生与p.get(key)不同的结果：前者考虑了默认的属性表，而后者是继承自Hashtable的，它则没有考虑默认属性列表。最严重的是，客户有可能直接修改超类，从而破坏子类的约束条件。在Properties的情形中，设计者的目标是，只允许字符串作为键（key）和值（value），但是直接访问底层的Hashtable就可以违反这种约束条件。一旦违反了约束条件，就不可能再使用Properties API的其他部分（load和store）了。等到发现这个问题时，要改正它已经太晚了，因为客户端依赖于使用非字符串的键和值了。

在决定使用继承而不是复合之前，还应该问自己最后一组问题。对于正试图扩展的类，它的API中有没有缺陷呢，如果有，是否愿意把那些缺陷传播到类的API中。继承机制会把超类API中的所有缺陷传播到子类中，而复合则允许设计新的API来隐藏这些缺陷。

简而言之，继承的功能非常强大，但是也存在诸多问题，因为它违背了封装原则。只有当子类和超类之间确实存在子类型关系时，使用继承才是恰当的。即便如此，如果子类和超类处在不同的包中，并且超类并不是为了继承而设计的，那么继承将会导致脆弱性（fragility）。为了避免这咱脆弱性，可以用复合和转发机制来代替继承，尤其是当存在适当的接口可以实现包装类时。包装类不仅比子类更加健壮，而且也更加强大。
//------------------------------------------------------------------------------------------------
//Effective Java 第4章 类和接口 P58
//第17条：要么为继承而设计，并提供文档，要么就禁止继承 P17

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

