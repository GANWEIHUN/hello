import car.Car;
import car.CarBuilder;
import com.thoughtworks.xstream.XStream;
import myAnnotation.CheckAnnotation;
import observer.Admin;
import observer.Customer;
import observer.Product;
import observer.Store;
import org.junit.Test;
import sun.misc.VM;

import java.awt.Color;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class MyTest {

    private int stateSet;

    public static void main(String[] args) {
        //用于生成代理类class文件
        //System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        //MyTest myTest = new MyTest();
        //myTest.testProxy();
    }

    @Test
    public void myTest() throws InterruptedException {
        //hello world
        testHello();
        //大型小数
        testBigFloat();
        //获取安全随机数
        testSecureRandom();
        //位运算
        testBitCalculate();
        //16进制颜色
        testHexColor();
        //泛型
        testFanXing();
        //对象的hashCode
        testHashCode();
        //栈
        testStack();
        //注解
        testAnnotation();
        //list列表转数组
        testList();
        //创建stream方式一，传入 supplier。对比list存储大量数据占用内存，stream几乎不占用内存，认为你传入的supplier是一个算法，需要用到了数据在去算
        testSupplierStream();
        //stream映射为新的stream
        testMapStream();
        //测试map的key，必须正确重写equals和hashCode
        testMapKey();
        //测试Integer缓存大小
        testIntegerCache();
        //测试单列
        testSingleton();
        //适配器
        testAdapter();
        //组合
        testComposite();
        //装饰器
        testDecorator();
        //工厂
        testFactory();
        //责任链
        testChain();
        //迭代器
        testIterator();
        //策略
        testStrategy();
        //建造者
        testBuilder();
        //多线程
        testThreads();
        //任务队列
        testTaskQueue();
        //获取异步结果
        testCompletableFuture();
        //fork/join线程池
        testForkJoin();
        //线程局部变量
        testThreadLocal();
        //测试类加载
        testLoadClass();
        //测试序列化
        testXmlSerialize();
        //测试按位运算判断枚举
        testBitEnum();
        //代理
        testProxy();
        //观察者
        testObserver();
    }

    private void testObserver() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //观察者模式，也叫发布/订阅模式（pub/sub），是一种一对多的消息通知机制，使得双方无需关心对方，只关心通知本身。
        Store store = new Store();
        store.addObserver(new Admin());
        store.addObserver(new Customer());
        Product product = new Product();
        product.setName("香蕉");
        product.setPrice(3.5f);
        store.addNewProduct(product);
        Product product2 = new Product();
        product2.setName("火龙果");
        product2.setPrice(10.0f);
        store.addNewProduct(product2);
        product.setPrice(3.0f);
        store.changePrice(product);
        //主线程等待一下，要不然异步线程打印不出来
        try {
            Thread.sleep(3000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private void testProxy() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //有没有可能不编写实现类，直接在运行期创建某个interface的实例呢？
        //这是可能的，因为Java标准库提供了一种动态代理（Dynamic Proxy）的机制：可以在运行期动态创建某个interface的实例。
        //原理：代理模式通过封装一个已有接口，并向调用方返回相同的接口类型，能让调用方在不改变任何代码的前提下增强某些功能（例如，鉴权、延迟加载、连接池复用等）。
        // 使用Proxy模式要求调用方持有接口，作为Proxy的类也必须实现相同的接口类型。
        //Proxy.newProxyInstance返回$proxy0实际上是QueryService的代理类，他实现了QueryService接口，所以这里$proxy0能强转为QueryService
        //动态代理实际上是JVM在运行期动态创建class字节码并加载的过程，它并没有什么黑魔法
        QueryService proxy = (QueryService) Proxy.newProxyInstance(QueryService.class.getClassLoader(),
                new Class[]{QueryService.class}, new MyInvocationHandler(UserType.Admin));
        Object result = proxy.query("A");
        System.out.println("结果：" + result);
    }

    private void testHexColor() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //原理：色值范围0-255，所以用2位16进制刚好能满足范围：0-ff
        Color read = new Color(0xFF0000);//红色
        Color read2 = new Color(255, 0, 0);//红色
        //Color内部实际存储方式：透明+rgb通过16进制存成一个整数
        int alpha = 255 & 0xff;
        //int alpha2 = 0b11111111;
        int r = 255 & 0xff;
        //int r2 = 0b11111111;
        int g = 0 & 0xff;
        //int g2 = 0b0;
        int b = 0 & 0xff;
        //int b2 = 0b0;
        int value = alpha << 24 | r << 16 | g << 8 | b << 0;
        //int value2 = 0b11111111111111110000000000000000;
        System.out.println("value和read2比较:" + (value == read2.getRGB()));
        Color green = new Color(0x00ff00);//绿色
        Color blue = new Color(0x0000ff);//蓝色
        Color c1 = new Color(0x323232);
        System.out.println("read:" + read.getRGB());
        System.out.println("green:" + green.getRGB());
        System.out.println("blue:" + blue.getRGB());
        System.out.println("c1:" + c1.getRGB());
    }

    private void testBitEnum() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //原理：1.枚举值定义为1,2,4,8（0001,0010,0100,1000）即每个枚举值对应一个二进制值，且对应比特位都是1
        //     2.设置枚举值 ，使用按位或运算。枚举1 | 枚举2 =0001 | 0010=0011(枚举s)，可以发现第1，第2比特位都是1，说明同时设置了2个枚举值（枚举1，枚举2）
        //     3.去掉枚举值 ，使用按位非，按位与运算。枚举s & ~枚举2 =0011 & ~0010=0011 & 1101=0001，可以发现只有第一个比特位是1，说明枚举s只剩下了枚举1
        //     4.判断枚举值 ，使用按位与运算。枚举s & 枚举2 =0011 & 0010=0010，可以发现0010等于枚举2，说明枚举s包含枚举2
        //     5.变量枚举s:操作所有枚举值之后的状态值
        stateSet = 0;
        setEnum(MyEnum.Left, true);
        System.out.println("left:" + containEnum(MyEnum.Left));
        setEnum(MyEnum.Top, true);
        System.out.println("top:" + containEnum(MyEnum.Top));
        setEnum(MyEnum.Right, true);
        System.out.println("right:" + containEnum(MyEnum.Right));
        setEnum(MyEnum.Bottom, true);
        System.out.println("bottom:" + containEnum(MyEnum.Bottom));
        System.out.println("----------------要开始删除枚举啦----------------");
        setEnum(MyEnum.Top, false);
        System.out.println("top:" + containEnum(MyEnum.Top));
    }

    private boolean containEnum(MyEnum top) {
        if ((stateSet & top.getValue()) == top.getValue()) {
            return true;
        }
        return false;
    }

    private void setEnum(MyEnum myEnum, boolean flag) {
        if (flag) {
            //使用枚举
            stateSet |= myEnum.getValue();
        } else {
            //删除枚举
            stateSet &= ~myEnum.getValue();
        }
    }

    private void testXmlSerialize() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //使用工作包：xStream显示xml序列化，反序列化
        XStream xStream = new XStream();
        XStream.setupDefaultSecurity(xStream);
        xStream.processAnnotations(StudentInfo.class);
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setAge(18);
        studentInfo.setCode("12498");
        studentInfo.setName("伍六七");
        String result = xStream.toXML(studentInfo);
        System.out.println(result);
    }

    private void testLoadClass() {
        // 1）第一次创建对象要加载类.
        // 2）调用静态方法时要加载类,访问静态属性时会加载类。
        // 3）加载子类时必定会先加载父类。
        // 4）创建对象引用不加载类.
        // 5) 子类调用父类的静态方法时
        //         (1)当子类没有覆盖父类的静态方法时，只加载父类，不加载子类
        //         (2)当子类有覆盖父类的静态方法时，既加载父类，又加载子类
        // 6）访问静态常量，如果编译器可以计算出常量的值，则不会加载类,例如:public static final int a =123;否则会加载类,例如:public static final int a = math.PI。
        // 7)Class.forName()语句也会加载类
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        Class<?> cls = null;
        try {
            //ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            //cls = Class.forName("TestLoadClass", false, classLoader);//会加载类，不会初始化，不会执行静态代码块
            cls = Class.forName("TestLoadClass");//会加载类，会执行静态代码块
            //Object testLoadClass = cls.newInstance();//实例化，会进入构造函数
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //TestLoadClass testLoadClass = new TestLoadClass();//必然会加载
        //TestLoadClass testLoadClass1 = null;//类不会加载（4）创建对象引用不加载类）
        //TestLoadClass[] testLoadClasses = new TestLoadClass[2];//类不会加载吗 (4）创建对象引用不加载类)
        assert cls != null;
        System.out.println("调用完forName：" + cls.getName());
    }

    private void testCompletableFuture() {
        //原理 ：java8之后提供completableFuture，支持获取异步处理结果
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //调用supplyAsync方法会把执行对象交给默认线程池处理
        System.out.println("创建completableFuture");
        CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
            int count = 0;
            try {
                for (int i = 0; i < 9; i++) {
                    count++;
                    System.out.println("计算处理中：" + count);
                    Thread.sleep(500);
                }
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            return count;
        });
        //异步处理完成
        completableFuture.thenAccept(o -> System.out.println("异步处理结果：" + o));
        //异步处理异常
        completableFuture.exceptionally(throwable -> {
            System.out.println("异步处理异常");
            throwable.printStackTrace();
            return null;
        });
        System.out.println("completableFuture调用完毕");
        //主线程不要立刻结束，否则completableFuture使用的默认线程池会立刻关闭
        try {
            Thread.sleep(5000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private void testThreadLocal() {
        //这种在一个线程中，横跨若干方法调用，需要传递的对象，我们通常称之为上下文（Context），它是一种状态，可以是用户身份、任务信息等。
        //给每个方法增加一个context参数非常麻烦，而且有些时候，如果调用链有无法修改源码的第三方库，User对象就传不进去了。
        //Java标准库提供了一个特殊的ThreadLocal，它可以在一个线程中传递同一个对象。
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        UserContext.User user = new UserContext.User();
        user.setName("tomato");
        user.setGender("男");
        user.setAge(19);
        try (UserContext ignored = new UserContext(user)) {
            printName();
            printAage();
        }
    }

    private void printAage() {
        UserContext.User user = UserContext.getCurrentUser();
        System.out.println("age:" + user.getAge());
    }

    private void printName() {
        UserContext.User user = UserContext.getCurrentUser();
        System.out.println("name:" + user.getName());
    }

    private void testForkJoin() {
        //fork/join线程池，基于“分治”原理，把一个大任务分成多个小任务并发处理
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        Random random = new Random(0);
        long[] array = new long[2000];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(10000);
        }
        SumTask sumTask = new SumTask(array, 0, array.length);
        long result = ForkJoinPool.commonPool().invoke(sumTask);
        System.out.println("result:" + result);
    }

    private void testTaskQueue() throws InterruptedException {
        //任务队列，通过wait和notify多线程协同工作
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //传统方式，synchronized配合wait和notify多线程协同工作
        //TaskQueue taskQueue = new TaskQueue();
        //新方式，reentrantLock配合condition多线程协同工作，新方式更灵活
        TaskQueue2 taskQueue2 = new TaskQueue2();
        List<Thread> threads = new ArrayList<>();
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    String task = taskQueue2.get();
                    System.out.println("getTask:" + task);
                } catch (InterruptedException exception) {
                    //exception.printStackTrace();
                    System.out.println("任务完成");
                    break;
                }
            }
        });
        thread.start();
        threads.add(thread);
        Thread addThread = new Thread(() -> {
            for (int i = 0; i < 8; i++) {
                String task = "task" + i;
                taskQueue2.add(task);
                System.out.println("addTask:" + task);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        });
        addThread.start();
        addThread.join();
        System.out.println("任务添加完成");
        Thread.sleep(1000);
        for (Thread t : threads) {
            t.interrupt();
        }
    }

    private void testThreads() {
        //多线程同时读写共享变量，需通过synchronized进行加锁
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        AddThread addThread = new AddThread();
        DecreaseThread decreaseThread = new DecreaseThread();
        addThread.start();
        decreaseThread.start();
        try {
            addThread.join();
            decreaseThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("count:" + Counter.num);
    }

    private void testBuilder() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。
        //建造者模式，创建一个完整对象由多个部件组成。参考stringBuilder链式代码stringBuilder.append().append().append()
        Car car = CarBuilder.getBuilder().setEngine(new InhaleEngine()).setEnginePosition(new FrontPosition()).
                setDriveMode(new AutoMode()).setTyre(new RubberTyre()).build();
        car.run();
    }

    private void testStrategy() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //策略思想，主流算法不变，其中容易变化的部分抽出来以参数的形式传进去，使得主体算法不用改变。
        DiscountContext discountContext = new DiscountContext();
        discountContext.setDiscountStrategy(new UserDiscountStrategy());
        BigDecimal pay1 = discountContext.calculateDiscount(BigDecimal.valueOf(105));
        System.out.println("pay1:" + pay1);

        discountContext.setDiscountStrategy(new OverDiscountStrategy());
        BigDecimal pay2 = discountContext.calculateDiscount(BigDecimal.valueOf(105));
        System.out.println("pay2:" + pay2);

        discountContext.setDiscountStrategy(new PrimeDiscountStrategy());
        BigDecimal pay3 = discountContext.calculateDiscount(BigDecimal.valueOf(105));
        System.out.println("pay3:" + pay3);
    }

    private void testIterator() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //自定义迭代器
        ReverseArray<Integer> array = new ReverseArray<>(1, 2, 3, 4, 5);
        for (Integer integer : array) {
            System.out.println(integer);
        }
    }

    private void testChain() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //责任链，一个请求在责任链流转。某个环节处理不符合要求，终止责任链流转或者继续流转。根据具体需求而定
        Request request = new Request("小明", new BigDecimal(1090));
        HandlerChain handlerChain = new HandlerChain(request);
        DirectorHandler directorHandler = new DirectorHandler();
        handlerChain.add(directorHandler);
        handlerChain.add(new ManagerHandler());
        handlerChain.process();

        handlerChain.setRequest(new Request("张三", new BigDecimal(1080)));
        handlerChain.process();

    }

    private void testFactory() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //一个工厂应该提供工厂接口和产品接口
        NumberFactory numberFactory = NumberFactory.getFactory();
        Number number = numberFactory.parse("209");
        System.out.println("number:" + number.toString());

    }

    private void testDecorator() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //原理：持有核心类/接口，在核心类上面增加附加功能
        TextLabel textLabel = new SpanLabel();
        textLabel.setText("你好");
        //文本加粗装饰
        LabelDecorator nodeDecorator = new BoldDecorator(textLabel);
        System.out.println(textLabel.getText());
        System.out.println(nodeDecorator.getText());
    }

    private void testComposite() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //原理：功能拆分一个个类，然后组合实现一个大的功能. 类似一棵树部分-整体的层次结构
        ElementNode root = new ElementNode("school");
        root.add(new ElementNode("1班").add(new TextNode("张三")).add(new TextNode("王五")));
        root.add(new ElementNode("2班").add(new TextNode("jack")).add(new TextNode("tomato")).
                add(new CommentNode("注释")).add(new TextNode("窃格瓦拉")));
        System.out.println(root.toXml());
    }

    private void testAdapter() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //原理：接口B持有接口A，B里面实现A的接口。A和B都是抽象接口
        Calculator calculator = new Calculator(100);
        //Thread thread = new Thread(calculator);//编译报错
        RunnableAdapter runnableAdapter = new RunnableAdapter(calculator);
        Thread thread = new Thread(runnableAdapter);//Runnable适配器
        thread.start();
    }

    private void testSingleton() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        System.out.println(Singleton.class);
        Singleton singleton = Singleton.getInstance();
        System.out.println(singleton);
    }

    private void testIntegerCache() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //启动参数 -Djava.lang.Integer.IntegerCache.high=256
        String high = VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
        System.out.printf("IntegerCache.high：%s%n", high);
    }

    private void testMapKey() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, Integer> integerMap = new HashMap<>();
        String key1 = "A".toLowerCase();
        integerMap.put(key1, 123);
        String key2 = "a";
        System.out.printf("key1=key2比较结果：%s%n", key1 == key2);
        //key1和key2不是同一个对象，但是key2依然能取到key1的value。那是因为map里面是通过key的hashCode确定value索引的。
        int value = integerMap.get(key2);
        System.out.printf("value:%s%n", value);
        System.out.printf("key1.equals(key2)比较结果：%s%n", key1.equals(key2));//字符串比较内容
        System.out.printf("key1HashCode:%s key2HashCode:%s%n", key1.hashCode(), key2.hashCode());
    }

    private void testMapStream() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        Stream<Integer> integerStream = Stream.of(1, 2, 3);
        Stream<Integer> integerStream1 = integerStream.map(n -> n * n);
        integerStream1.forEach(System.out::println);
    }

    private void testSupplierStream() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        Stream<Integer> integerStream = Stream.generate(new NumberSupplier());
        integerStream.limit(5).forEach(System.out::println);
    }

    private void testList() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(123);
        list.add(29);
        list.add(8);
        Integer[] array = list.toArray(new Integer[0]);
        System.out.printf("array:%s%n", array.length);
        Integer[] array2 = new Integer[list.size()];
        list.toArray(array2);
        System.out.printf("array2:%s%n", array2.length);
    }

    private void testAnnotation() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        Student student = new Student();
        student.Gender = "未知生物";
        student.Name = "";
        try {
            boolean result = CheckAnnotation.check(student);
            System.out.printf("检查结果%s%n", result);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void testStack() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //  栈（Stack）是一种后进先出（LIFO）的数据结构，操作栈的元素的方法有：
        //• 把元素压栈：push(E)；
        //• 把栈顶的元素“弹出”：pop(E)；
        //• 取栈顶元素但不弹出：peek(E)。
        //  在Java中，我们用Deque可以实现Stack的功能，注意只调用push()/pop()/peek()方法，避免调用Deque的其他方法。
        Deque<String> stack2 = new LinkedList<>();
        stack2.push("hello");
        stack2.push("haha");
        stack2.push("world");
        int size = stack2.size();
        for (int i = 0; i < size; i++) {
            System.out.println(stack2.pop());
        }
    }

    private void testHashCode() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        String a1 = String.valueOf(0);
        String a2 = "0";
        System.out.println("a1 hashCode:" + a1.hashCode());
        System.out.println("a2 hashCode:" + a2.hashCode());
        String b1 = String.valueOf(1);
        String b2 = "1";
        System.out.println("b1 hashCode:" + b1.hashCode());
        System.out.println("b2 hashCode:" + b2.hashCode());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    private void testFanXing() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        //泛型类
        NumClass<Integer> intCls = new NumClass<>();
        intCls.setName(1);
        System.out.println(intCls.getName());
        //？ extends通配符修饰方法参数时
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        readOnlyNumber(list);


    }

    private void readOnlyNumber(List<? extends Integer> list) {
        for (Integer n : list) {
            //？ extends通配符修饰方法参数时，只能只读方法，不能修改list
            //list.set(0,10);
            System.out.println(n);
        }
    }

    private void testBitCalculate() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        int a = 1 & 0;//按位与0等于0
        int b = 2 & 2;//2个相等数按位与等于其本身
        int c1 = 1 & 4;
        int d = 1 | 4;

        System.out.println(a);
        System.out.println(b);
        System.out.println(c1);
        System.out.println(d);
    }

    private void testSecureRandom() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        SecureRandom sr;
        try {
            sr = SecureRandom.getInstanceStrong(); // 获取高强度安全随机数生成器
        } catch (NoSuchAlgorithmException e) {
            sr = new SecureRandom(); // 获取普通的安全随机数生成器
        }
        byte[] buffer = new byte[16];
        sr.nextBytes(buffer); // 用安全随机数填充buffer
        System.out.println(Arrays.toString(buffer));
    }

    private void testBigFloat() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        BigDecimal bigDecimal = new BigDecimal(10);
        BigDecimal bigDecimal2 = new BigDecimal(3);
        BigDecimal div = bigDecimal.divide(bigDecimal2, 10, RoundingMode.HALF_UP);
        System.out.printf("div:%s%n", div);
        //两个BigDecimal比较不能用equals，必须用compareTo
        bigDecimal = new BigDecimal("1.2");
        bigDecimal2 = new BigDecimal("1.20");
        System.out.printf("equals结果：%s%n", bigDecimal.equals(bigDecimal2));
        System.out.printf("compareTo结果：%s%n", bigDecimal.compareTo(bigDecimal2));
    }

    private void testHello() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        String text = "hello";
//        text += " world";
        System.out.printf("text:%s%n", text);
    }

    static class NumberSupplier implements Supplier<Integer> {

        private int number;

        @Override
        public Integer get() {
            return ++number;
        }
    }

    private static class NumClass<I extends Number> {

        private I name;

        public I getName() {
            return name;
        }

        public void setName(I name) {
            this.name = name;
        }
    }
}

