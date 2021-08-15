# Java进阶训练营

## 第一周:JVM进阶-Java开发者大厂面试必知必会

### 思考和经验认识: 

编译器将java源码文件编译成class文件, 虚拟机的类加载器将class文件加载到内存中. 垃圾收集器根据class文件和分代假设设置一部分要管理的内存. 虚拟机开始运行程序, G1(不包括) 以前的垃圾收集器根据分代假设不断的在栈, 堆(eden, survivor, 老年区), 方法区之间进行垃圾回收和整理. 可以根据CPU数量, 内存大小, JDK版本选择垃圾收集器, 后期运行不断通过相关虚拟机检测工具计算(例如执行minorGC, majorGC, full GC, STW执行的频率和数量),分析并确定虚拟机堆、栈、方法区的大小甚至更换垃圾收集器和增加内存.

### 本课目标: 

- 内存出现问题时, 根据JVM信息进行分析诊断和调整

### 1.JVM基础知识 11:01

编程语言的分类方式:
-  面向过程,面向对象, 面向函数
-  静态类型, 动态类型: 静态类型是指运行时类型不能改变.
-  编译执行, 解释执行
-  有虚拟机, 无虚拟机
-  有GC, 无GC
  - GC: 整个JVM内部的内存管理器. 管理对象的创建到销毁的整个生命周期
- 跨平台方式:
  - C++跨平台: 源代码的跨平台.
  - Java: 二进制跨平台.


Java是一种面向对象, 静态类型, 编译执行, 有VM/GC和运行时, 跨平台的高级语言. 运行时是JVM 运行程序时需要启动的java进程,他依赖的一些库等**整个环境**.

Java 三个优势:
- 生态特别好
- 有自己的虚拟机和垃圾回收器
- 是一个非常好的跨平台语言.


### 2.Java字节码技术 44:32

Java 字节码(Java byteCode):Java 虚拟机执行的一种指令格式. 大多数操作码都是一个字节, 有些操作码需要操作数而表现为多个字节. 字节码操作的内存空间主要包括: 栈帧的局部变量表(JVM 栈每次调用方法都会生成一个栈帧.栈帧包含局部变量表, 动态链接, 方法出口操作数栈等), 栈, 运行时常量池(方法区的一部分, 主要加载了class 文件的常量池).

- javap:
  - `-c`: 对代码进行反编译
  - `-verbose`: 打印常量池, 标识符, 修饰符, 行号打印出来.
- javac:
  - `-g`: 在二进制文件中保存调试相关信息. 例如局部变量名称.
  - `-d`: 指定编译生成class文件默认地址. 不指定的话和原文件相同地址.

根据字节码的性质, 主要分为四大类:
1. 栈操作指令, 包括与局部变量交互的指令.
  - iload, iload_1, iload_2, iload_3...
  - iload, lload, fload, dload, aload...
2. 程序流程控制指令
  - if_icmpge, goto, return 等.
3. 对象操作指令, 包括方法调用指令
  - new...
  - invokespecal, invokestatic, invokevirtual, invokeinterface, 
  - invokedynamic: JDK7新增加的指令, 实现"动态类型语言"(Dynamic typed Language)支持而进行的改造. 同时也是JDK8 实现lambda 表达式语言的基础
4. 算术运算以及类型转换指令.
  - iadd, isub, imut, idiv, irem, ineg...
  - ladd, fadd, dadd...
  - i2l, i2f...

- 字节码详细信息(HelloByteCode)
```log

```

### 3.JVM 类加载器 33:45

类的生命周期: 加载到初始化是类加载过程.
1. 加载(Loading): 找class文件
2. 连接(Linking)
   1. 校验(verification): 校验class文件格式
   2. 准备(Prepare): 准备方法表和静态字段(对静态字段进行内存分配, 不包括成员变量)
   3. 解析(resolution): 符号引用解析为直接引用
3. 初始化(initialization): 构造器(java编译器根据用户的静态字段和静态代码块收集而成的编译器, 不是用户编写的编译器), 静态变量赋值, 静态代码块.
4. 使用(using)
5. 卸载(unloading)

类加载时机(必须初始化):
1. 虚拟机启动时, 初始化主类 
2. 使用new 创建对象时
3. 调用静态字段时, 会初始化静态字段所在类
4. 调用静态方法时, 初始化静态方法所在的类.
5. 子类加载时自动加载父类
6. 如果一个接口有default方法, 实例化该接口实现类时加载该接口.
7. 使用反射API对某个类进行调用时.
8. 初次使用MethodHandle实例时, 初始化该MethodHandle指向的方法所在的类.

不会初始化(可能会加载):
1. 通过子类调用父类的静态成员, 不会触发子类的初始化
2. 定义对象数组. 不会触发该类的初始化.
3. 常量(static final)在编译期间会存入调用类的常量池中, 本质上并没有直接引用定义常量的类, 不会触发定义常量所在的类, 常量需要在静态初始化块中赋值, 则会进行加载.
4. 通过类名获取Class对象. 例如A.class, 不会让A初始化.
5. 通过Class.forName加载指定类时, 如果initialize 默认为false. 不会对类初始化.
6. 通过ClassLoader默认loadClass方法, 也不会触发初始化动作(加载了, 但是不初始化);

加载器特点:
1. 双亲委托
2. 负责依赖
3. 缓存加载: 将加载的类缓存到内存中, 不会重复加载.

加载器分类:
1. 启动类加载器: 加载`<JAVA_HOME>\lib`或者`-Xbootclasspath` 指定的, 并且能够被虚拟机认识的文件名(如:rt.jar, tool.jar).
2. 扩展类加载器. 加载`<JAVA_HOME>\lib\ext`下的部分文件, 或者被`java.ext.dirs`参数指定的目录下的文件.
3. 应用类加载器. 加载自己编写的类文件或者`-classpath`指定的类文件.

添加引用类的方式:
1. 使用"-classpath" 指定类路径
2. 把类放在"lib\ext"或者"-Djava.ext.dirs" 指定路径下
3. 自定义classLoader加载
4. 拿到当前类的classLoader, 反射调用加载

类加载器作用:
1. 为class文件加密
2. 从不同的地方设置class文件
3. 检查加载的类, 处理加载类相关的异常

### 4.JVM 内存模型 18:01

- 线程栈
   - 栈帧
     - 操作数栈
     - 本地变量表
     - class, method指针
     - 返回类型
- 堆: 存放所有对象
   - 年轻代
      - 新生代(eden)
      - from survivor(存活区)
      - to survivor(存活区)
   - 老年代
- 非堆:官方称为`Java方法区`,也称为`永久代(Permanent Generation)`
   - metaSpace: 在Java7 中将持久代中字符串常量池, 静态常量从永久代移动到`metaSpace`, 到java8 时取消永久代, 并将剩余内容(主要是类型信息)移植到了`metaSpace`.
     - 常量区: 运行时常量池
   - CCS(Compress Class space): 
   - Code Cache: 存放即时编译后的class代码

JMM Java Memory Model And Thread Specification JVM模型:明确定义了不同线程之间, 在什么时候以什么方式看到其他线程共享的变量. 以及在必要时, 如何对共享变量的访问进行同步. 而且了定义了"先行发生原则"

### 5.JVM 启动参数 30:38

JVM 启动参数分类:
- `-`:JVM 标准参数: 稳定, 向后兼容
- `-D`: 设置系统属性. 覆盖系统设置
  - 可以在环境变量中配置
  - -Dfile.Encoding=UTF-8
- `-X`: 非标准参数, 基本都是传递给JVM的, 默认JVM实现这些参数的功能, 不保证所有JVM, 且不保证向后兼容. 
  - `java -X` 查询JVM支持的非标准参数 
  - `-X:max8g`: 
- `-XX`: 非稳定参数, 专门用于控制JVM行为, 对于不同版本随时可能取消..
  - "java -XX:+PrintCommandLineFlags -version": 查看虚拟机默认设置参数
  - "-XX: +-Flags" 对boolean值进行开关
    - "-XX:+UseG1GC"
  - "-XX: key=value" 指定选项值.
    - "-XX:MaxPermSize=256m"

JVM启动参数按照具体特点和作用分类:
1. 系统属性参数
   - JVM系统属性: `-Dfile.encoding=UTF-8`和`System.setProperty("file.encoding", "UTF-8")` 等价. Windows环境变量通过`System.getenv()` 获取一个环境变量的映射.
     - "-Duser.timezong=GMT+08" 设置时区
     - "-Dmaven.test.skip=true"
     - "-Dio.netty.eventLoopThreads=8"
   - linux 上还可以通过`a=A100 java xxx`方式为`java`传递参数
   - 系统内使用的开关或者数值.
2. 运行模式参数
   - -server: 64 JDK默认使用, 忽略`-client`. 启动速度比较慢, 运行时性能和内存管理效率比较高.
   - -client: JDK 1.7 之前32位的x86 机器默认值. 启动快, 运行时性能和内存管理效率比较高.
   - -Xint: 表示在解释模式(interpreted mode)下运行. 直接执行字节码指令的方式在JVM中成为解释模式.
   - -Xcomp: 在编译模式下运行. 会将字节码编译成本地代码. 编译比较花费时间.
   - -Xmixed: 混合模式, 将解释模式和编译模式混合使用. 可以使用`java -version`查看编译模式
3. 堆内存设置参数. 不包括堆外, 非堆. 一个资源比较多的服务器, 最好将Java程序虚拟化或逗号化运行在自己的虚拟机或者docker中, 方便为每个服务配置不同的资源.
   - -Xmx: 最大堆内存. 一般配置整个操作系统内存的60%-80%. 机器内存大于1G时, 默认为机器内存1/4, 机器内存小于1G时, 默认是机器内存1/2.
   - -Xms: 堆内存空间的初始值大小. 不过不是操作系统在一开始就分配这么多内存, 而是GC规划好, 用到才分配. 最好在启动时颈`-Xmx`和`Xms`配置为相同的值, 否则该开始启动时可能有几个`FullGC`. 如果配置不相同, 堆内存扩展时可能引发性能抖动.
   - -Xmn: 等价于`-XX:NewSize`=`-XX:MaxNewSize`. 设置堆中young区的初始大小和最大值相等. 一般设置为`-Xmx的1/2-1/4. 默认是1/3
   - `-XX:NewRatio=`: 设置堆中老年代和新生代的比例. 默认为2
   - `-XX:InitialSurvivorRatio=`: 设置年轻代和survivor区的比例. 默认是8. 8m Eden space就会有1m from survivor, 1m to survivor.
   - `-XX:MaxPermSize=size` 设置持久代最大值. `-XX:PermSize` 设置持久代初始化大小. 这是JDK1.7以及之前使用的(一般设置为64M, 最大设置为128M, 还不行就另外优化)Java8无效.
   - `-XX:MaxDirectMemorySize=size`.
     - 与`-Dsum.nio.MaxDirectMemorySize  有相同作用
     - 默认与Java堆最大值一致.堆内存分配过大而该内存不足会抛出OOM.
     - `Direct Memory(直接内存)`是因为引用了NIO, 方便Native函数直接分配堆外内存, 然后使用`DirectByteBuffer`引用分配的内存. 
     - Heap dump 文件中不会看到直接内存中存储情况.
   - `-Xss`和`-XX:ThreadStackSize=1m` 等价. 指定为每个线程栈的字节数.
4. GC相关参数. 占用了JVM参数一半以上.
   - `-XX:+UseG1GC`: 使用G1垃圾回收器
   - `-XX:+UseConcMarkSweepGC`: 使用CMS 垃圾回收器. CMS:Concurrent Mark Sweep
   - `-XX:+UseSerialGC`: 使用串行垃圾回收器
   - `-XX:+UseParallelGC`:使用并行垃圾回收器. **具体收集器名字是什么**.
   - `-XX:+UnlockExperimentalVMOptions`,`-XX:+UseZGC`: 在Java 11 中添加.
   - `-XX:+UnlockExperimentalVMOptions`,`-XX:+UseShenandoahGC`: 在Java12 添加
   - `-XX:MaxTenuringThreshold`: 从年轻代复制到老年代的次数.
   - `-XX:ParallelGCThreads`: 设置并行GC线程数.
5. 分析诊断参数
   - `-XX:+-HeapDumpOnOutOfMemoryError`: 当OutOfMemoryError产生, 自动Dump堆内存
   - `-XX:HeapDumpPath`与上一条命令一起使用, 指定内存溢出时Dump文件目录. 如果没有指定, 默认为启动Java程序的工作目录.
     - `java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/usr/local/`: 设置hprof(prof 是performance的缩写)文件存储到`/usr/local`.
   - `-XX:OnError`: 发生致命错误时(fatel error)执行的脚本.
     - 写一个脚本记录出错时间, 执行一些命令, 或者curl以下某个在线报警的url
     - `java -XX:OnError="gdb -%p" MyApp
   - `-XX: OnOutOfMemoryError`, 抛出OutOfMemoryError 错误时执行的脚本.
   - `-XX:ErrorFile=filename` 致命错误的日志文件名, 绝对路径或者相对路径.
   - `-Xdebug -Xrunjdwp:transport=dt_socket, server=y, suspend=n,address=1506`: JVM为IDE提供的参数, 运行时等待调试的线程挂上来. 远程调试.
6. JavaAgent: JVM的黑科技, 可以通过无侵入方式来做很多事情, 比如注入AOP代码, 执行统计等
   - `-agentlib:libname[options] 启用native方式的agent, 参考LD_LIBRARY_PATH路径.
   - `-agentpath:pathname[=options]` 启用native方式的agent
   - `-javaagent:jarpath[=options] 启用jar库, 比如pinpoint.jar`
   - `-Xnoagent` 禁用所有agent


### 6.JDK内置命令行工具 40:57 第二节

分析JVM情况的工具, 包括内部的内存,线程等指标.
Java自带的GC策略, 也就是JVM内存管理算法

JDK 自带的命令行工具:
- 分发, 编译, 打包, 安全
  - java 
  - javac
  - javap 反编译class文件的工具
  - javadoc 根据Java代码这标准注释, 生成相关API文档
  - javah JNI开发, 根据java代码生成需要的.h 文件.
  - extcheck 检查某个jar文件或者运行时扩展jar 有没有版本冲突. 开发时很少使用.
  - jdb java debugger. 可以调试本地和远端程序, 属于JPDA的一个远端实现, 供其他调试器参考. 开发时很少使用.
  - jdeps 探测class或jar包需要的依赖. 主要分析import 语句.
  - jar 打包工具, 可以将文件和目录打包成.jar文件
  - keytool 安全证书和秘钥的管理工具(支持生成, 导入, 导出等操作).
  - jarsigner Jar文件签名和验证工具.
  - policytool: 实际上这是一款图形界面工具, 管理本机的Java安全策略.
- java运行时常用命令
  - jps/jinfo Java进程相关信息. 
    - jps: 因为权限问题, 导致不能查看所有java进程. 也可能因为java版本不同, jps不能查看不同版本的进程信息.
    - jinfo在Mac 上不好使.
  - jstat 查看内部gc相关信息
    - `jstat -t` 打印执行时间
    - `jstat -gc pid interval times` 每经过interval 打印gc 信息, 一共打印times次
    - `jstat -gcutil pid interval times` 同上, 打印出来的是是内存占用比例
  - jmap 查看heap或类占用空间统计. 
    - 在Java8可以使用这些命令, 以后版本需要使用`jhsdb jmap -heap pid`,  `jhsdb jmap -histo pid`方式使用.
    - `jmap -histo pid` 打印直方图, 看那些类占用空间最多.
    - `jmap -heap pid`在windows 上不能执行.
    - 在mac 操作有bug. 在jdk9开始修复.
  - jstack 查看线程信息. 
    - `jstack -F pid` 强制执行thread dump, 可在java进程卡死的时候使用
    - `jstack -m pid` 混合模式, 将java帧和native 帧一起输出
    - `jstack -l pid` 输出线程所有锁信息.
    - 在Mac和linux 上使用`kill-3` 打印类似信息.
  - jcmd 执行JVM相关分析命令(整合命令)
    - `jcmd pid help` 查询jcmd 对于该进程支持的所有操作
    - `
  - jrunscript/jjs 执行js命令
    - jrunscirpt: "jrunscript -e "cat(https://baidu.com)"" 类似于"curl https://baidu.com"
    - jjs: 执行javascript 脚本.

### 7.JDK内置图形化工具 21:53

- jconsole: 显示堆内存, 线程, 类的加载和卸载, VM选项. 监控虚拟机性能.
  - tab页: 概要, 内存, 线程, 类, VM概要, MBean
- jvisualvm
   - java8的小版本号比较大, 大概从"26几"开始JDK不再有jvisualvm和jmc. 需要另外下载
   - 分析VM的很强大的工具  
   - tab页:Overview, Monitor, Threads
     - Sampler 抽样器
     - Profiler 性能分析
- visualGC: Eclipse或者IDEA中的插件, 类似于jvisualVM
- jmc(Java mission control): **非常建议大家把这个工具用熟**
  - BEA 有一个JDK实现,叫做JRockit, JRockit自带的jrmc. 后被Oracle收购了改名为jmc. 目前官方所有工具功能最强大的.
  - 支持上面的功能, 还支持一个飞行器的功能.  详细记录内存, GC相关事件, 内存的变化, 类信息的变化.
  - 使用eclipse 的awt库. 如果启动卡死. 可以找一个高版本的eclipse的awt替换掉.

### 8.GC的背景与一般原理 23:45

- 引用计数实现GC. 
  - 为每个对象的添加一个引用属性, 随着对象引用数的改变而改变.
  - 如果对象之间循环引用, 对象应用数一直大于0, 不能被回收
- 可达性分析. 使用GC Roots 跟踪对象, 标记-清除算法管理GC
  - GC Roots:
    - 虚拟机栈中引用的对象. 堆栈中使用的参数, 局部变量等.
    - 所有线程对象
    - 方法区中常量引用的对象, 譬如字符串常量池中的引用
    - 方法区中静态属性引用对象, 譬如Java类的引用类型静态字段   
    - 虚拟机内部的引用, 基本类型对应的Class对象, 一些常驻的异常对象, 还有系统类加载器
    - JNI应用
    - 所有被同步锁(synchronized)持有的对象
    - 反应Java虚拟机内部情况的JMXBean, JVMTI中注册的回调, 本地代码缓存.
  - 标记-清除算法
    - 标记(marking): 遍历所有可达对象, 并在本地内存(native memory)分门别类记下.
    - 清除(sweeping): 释放不可达对象的空间
    - 整理(Compact): 移动对象, 将在使用的空间放在一起, 使不可使用的空间放在一起.
    - CMS 的基础算法是标记-清除. 只扫描部分依赖.
  - GC算法
    - 使用STW(Stop the world):使所有JVM中线程停下来, 方便统计可达对象以及清除对象. 此阶段暂停的时间与Java堆内存, 对象数量没关系, 而是有存活对象(alive object)确定.
    - 基于分代假设进行. 内存区分为青年带(新生代, 存活区), 老年代, 永久代进行处理.
    - 对于不同内存区域使用不同算法.
      - 年轻代, 新创建的对象保存在Eden区, 隔一段时间, 执行一次STW, 统计Eden可达对象, 保存在Survivor01区. 隔一时间, 统计Eden和Survivor01区可达对象保存在survivor02, 清理Eden和Surivor01. 以后不断交替使用Survivor01和Survivor02.
      - 老年区: 使用标记-清除-整理算法. `-XX:MaxTenuringThreshold` 设置在年轻代移动到老年代的统计次数域值.
      - 永久区: 一般需要回收常量和类型信息. 回收效率比较低. 设置持久代, 元数据区参数
        - 1.8前`-XX:MaxPermSize=256`
        - 1.8以及之后`-XX:MaxMateSpaceSize=256`

### 9.串行GC/并行GC 10:25


- 串行GC(Serial GC)/ParNewGC
  - `-XX:+UseSerialGC`
  - 执行时会触发全线STW
  - 只适合几百兆内存, 单核机器
  - `-XX:UseParNewGC` 对串行GC在young 区进行并行的垃圾回收.可以配合CMS使用
- 并行GC(Parallel GC). 具体的GC名字是Parallel Scavenge 和Parallel Old GC.
  - `-XX:+UseParallelGC`, `-XX:+UseParallelOldGC`, `-XX:+UseParallelGC -XX:UseParallelOldGC` 等价.设置使用并行垃圾收集器
  - `-XX:ParallelGCThreads=`设置并发GC时的线程数. 默认为CPU核数
  - 年轻代或老年代执行都会触发STW
  - 适用于多核处理器, 可以增加吞吐量
    - 在GC期间, 使用多核处理执行GC. 总的暂停时间更短
    - 在两次GC周期的间隔期, 没有GC运行, 不会消耗任何系统资源
- `-XX:+UseAdaptiveSizePolicy` 会根据GC情况自动计算Eden, survivor 区大小.
- 测试的命令使用的是`java -Xmx1g -Xms1g -XX:+UseAdaptiveSizePolicy -XX:UseParallelGC -jar gateway-server.jar`

### 10.CMS GC 21:07

CMS GC(Mostly Concurrent Mark And Sweep Garbage collector 尽可能并发标记和清除 垃圾收集器). 对青年代使用ParNewGC的并行 mark-copy 算法. 对老年区使用mark-sweep 算法. 对老年代默认使用内核数的1/4 线程数运行. CMS GC 主要是为了降低GC停顿造成的延迟. 进行老年代并发清理时, 可能会伴随多个年轻代的minorGC

ParallelGC 和CMSGC的区别
1. ParaGC 对老年区使用mark-sweep-compact算法, CMS GC 对老年区使用mark-sweep算法, 使用空闲列表(free lists)对老年区内存进行划分
2. ParaGC 对老年区GC时使用全部内核数的线程, JVM是STW. CMS GC在对老年区进行GC时使用1/4内核数的线程. 可以和业务并行运行.

CMS GC的六个阶段
1. initial mark(初始标记): 标记所有根对象, 以及根对象所直接引用的对象. 标记年轻代指向老年代的对象. JVM 中有一个Remember Set(RSet), 主要用于对象的跨代引用. 这阶段做了STW, 标记的数据完全准确.
2. concurrent Mark(并发标记): CMS GC 遍历所有老年代, 标记所有存活对象. 从前一个阶段找到的跟根对象开始算起. 与应用程序并行, 数据可能不准确.
3. concurrent Preclean(并发预清理):上一阶段状态发生变化的内存称为脏区. CMS GC使用卡片标记(card marking)处理. 
4. final remark(最终标记):执行一次STW, 理清上一阶段状态发生改变的对象. CMS 尽可能是在young区空的情况下执行.因为young区满时可能会继续向老年代传送对象.
5. concurrent sweep(并发清理): CMS 删除不再使用的对象.回收它们占用的空间.
6. 重置CMS 相关的内部数据, 为下一次GC循环做准备.

Java8 young区的区别:
测试的命令使用的是`java -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:UseParallelGC -jar gateway-server.jar`
1. ParallelGC:年轻代是堆内存的1/3
2. CMS GC: 64M * GC线程数 * 13 / 10. Java11后和ParallelGC相同.

**有一个关于jmap --heap的解读.**
**对年轻代和老年代是否同时GC还是分开GC**

### 11.G1 GC 22:16

G1 设计目标是控制GC的STW时间.将GC暂停时间做一个可预期的值. 在吞吐量和延迟找到一个平衡点. 

G1垃圾回收, G1算法.

吞吐量和延迟
启发式算法: G1 GC 可以调整CSet(Collector Set), 也可以根据region的信息确定垃圾收集.

`-XX:UseG1GC -XX:MaxGCPauseMillis=`: 第二个选项用于设置每次GC最大的暂停时间, 默认值是200. 

G1 GC 配置参数
- `-XX:+UseG1GC`
- *`-XX:G1NewSizePercent`: 初始年轻代占用整个Java Heap的大小, 默认5%.
- *`-XX:G1MaxNewSizePercent`: 最大年轻代占用整个Java heap 大小. 默认60%
- *`-XX:G1HeapRegionSize`: 设置每个region大小, 单位MB. 需要为1, 2, 4, 8, 16, 32 某个值. 默认为堆内存的1/2000. 如果这个值设置比较大, 那么大对象可以留在Region. JVM 判断如果对象大小超过Region的1/2时是大对象.
- *`-XX:ConGCThreads`: 与Java应用一起执行的GC线程数量. 默认是Java线程数量的1/4. 减少这个值能够提升垃圾回收效率. 但过小会增加延迟时间.
- *`-XX:+InitiatingHeapOccupancyPercent`: 触发垃圾回收时堆内存空间的占用空间. 默认为堆内存的45%. 这里的占优比指的是non_young_capacity_bytes,
- *`-XX:G1HeapWastePercent`: G1停止回收时最小内存大小. 默认是堆内存的5%. 每次G1都不会回收完所有Region. 这样可以减少单词消耗的时间.
- `-XX:G1MixedGCCountTarget`: 
- `-XX:+G1PrintRegionLivenessInfo`: 这个参数和`-XX:+UnlockDiagnosticVMOptions`配合启动. 打印JVM调试信息. 每个Region里的对象存活信息.
- `-XX:+G1ReservePercent`: G1 保存的空间用于年代之间的提升. 默认堆空间的10%. 如果应用里有比较大的堆内存空间, 比较多的大对象存活, 这里需要保存一些空间
- `-XX:+G1SummarizeRSetStats: 这是一个VM调试信息. 启用后, 会在VM退出时打印Rsets的详细总结信息. 如果启用`-XX:+G1SummaryRSetStatsPeriod`, 会阶段性打印RSet信息
- `-XX:+G1TraceConcRefinement`: 这是一个JVM调试信息. 启用会详细打印回收阶段的调试信息.
- *`-XX:GCTimeRatio`计算花在GC的时间比例. 1/(1 + GCTimeRatio). Parallel GC 默认值时99. G1 GC default 9. 因为G1 GC和应用线程时并发执行, 所以可以设定更多时间. 
- `-XX:+UseStringDeduplication`手动开启Java String对象的去重工作, 这个是JDK8u20之后开启的参数. 避免String对象避免重复申请内存.
- *`-XX:MaxGCPauseMills`: 预期G1每次执行GC操作的暂停时间, 单位是毫秒, 默认值是200毫秒. G1会尽量保证控制在这个范围内. 可以设置到50

测试:
- `java -Xmx1g -Xms1g -XX:UseAdaptiveSizePolicy -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar gateway-server.jar`
- `jhsdb jmap --heap -pid`

GC 如何选择:
- Serial GC, Serial Old GC 单线程, 低延迟. 适合单核低内存机器. Java的客户端模式默认采用这个
- ParNew, CMS 多线程并行, 低延迟
- Parallel GC, Parallel Old GC 高吞吐量. 
- CMS更注重吞吐量, 延迟时间不太确定. 

### 12.ZGC/shenandoah GC 25:23

ZGC主要的特定:
1. GC最大停顿时间不超过10ms, 一般不超过5ms
2. 堆内存支持范围广. 小到几百兆,大道4T(JDK13升级到16T)
3. 与G1相比, 吞吐量下降不到15%
4. JDK11只支持Linux. JDK15后变成全平台. 

改造成ZGC比较容易. java9加入了模块化, 需要修改rt.jar的内容. 适当修改参数即可.

ShenandoahGC:
- Redhat 开发. 如果使用redhat系统, 可以提前到官方发布的java8高版本以上使用. 
- JDK12后可以在linux上使用. JDK15后可以全平台使用.
- `-XX:UnlockExperimentalVMOptions -XX:+UseShenandoahGC -Xmx16g`
- 可以有低延迟性, 不过没有ZGC那么低. 

### 作业
3. 展示Xmx, Xms等相关
   - 这些参数的关系标出来, 哪个参数包含另外的参数, 哪个参数根内存的哪个区域是直接有关系的. 

需要把现在课程学完. 每天一部分.每天干点其他事情.maven, 网络, ppt.

#### 作业流程

1. 建立作业文件夹
2. 建立IDEA项目
3. 编写代码
4. 绘图
5. 初始化git, 提交作业
6. 向极客时间提交作业

#### 作业提交

开始有点兴奋了. 感觉挺好

不过定的外围计划只是开始了一点. 以后时间不足, 专做课程.


#### 作业题目
1.（选做）自己写一个简单的 Hello.java，里面需要涉及基本类型，四则运行，if 和 for，然后自己分析一下对应的字节码，有问题群里讨论。

2.（必做）自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供。

3.（必做）画一张图，展示 Xmx、Xms、Xmn、Meta、DirectMemory、Xss 这些内存参数的关系。

4.（选做）检查一下自己维护的业务系统的 JVM 参数配置，用 jstat 和 jstack、jmap 查看一下详情，并且自己独立分析一下大概情况，思考有没有不合理的地方，如何改进。

注意：如果没有线上系统，可以自己 run 一个 web/java 项目。

5.（选做）本机使用 G1 GC 启动一个程序，仿照课上案例分析一下 JVM 情况。

### Question

- gateway-server 访问地址
  - `http://localhost:8088/api/hello`

- [question01](#3jvm-类加载器-3345): 这里的常量是静态的么? 不是静态的话必须是定义时进行初始化(常量在构造函数完成前初始化即可). **调用类的常量池** 是指调用类的类文件的常量么?

- [JVM启动参数-堆内存 相关问题](#5jvm-启动参数-3038)
  1. 如果什么都不配置会如何
   > -Xmx 默认是物理内存的1/4或1G, 最小1M
   > -Xms 默认为物理内存的1/64, 最小为1M
   > -Xmn 默认为堆内存的1/3
   > -XX:MaxPermSize -XX:PermSize 设置老年代最大值和初始值, java8默认无限大
   > -XX:MaxDirectMemory 默认为0, 由JVM自取
   > -Xss: 一般默认为512K.
  2. Xmx是否与Xms设置相等
   > 最好设置相等, 否则刚启动程序时可能好几次FullGC, 堆内存扩容时可能导致性能抖动.
  3. Xmx设置为机器内存的什么比例比较合适
  > 一般设置系统内存的60%-80%
- [JVM启动参数-GC 相关问题](#5jvm-启动参数-3038)
   1. 各个JVM版本的默认GC是什么
   > Java8 使用Parallel Scavenge, java9以后默认使用G1. 使用`java -XX:+PrintCommandLineFlags -version` 可以查看Java使用的GC.

## 第二周:JVM 核心技术

总共240分钟, 每天一个小时的课程.

### 需要阅读的资料

- "Netty权威实战" 526
- "Java NIO" 253
- "Netty, Redis, Zookeeper 高并发实战-尼恩" 613
- "Netty实战(v8)" 305
- "Netty实战" 272
- "nio-Doug Lea" 39
- "百亿级流量API网关的设计与实现" 37
- "百亿级流量的API网关设计-kimmking-v10" 28

### GC日志解读与分析 50:16

使用程序生成堆中新生区和老年区内存. 使用`-XX:+PrintGCDetails`, `-XX:+PrintGCDateStamps`, `-Xloggc:gc.demo.log` `-XX:+PrintGC`参数显示相关信息.

GC日志解读
- `GC Allocation Failure`: GC发生的原因是因为内存分配失败.
- `PYoungGen` 代表Parallel Scavenge 中Young区
- `DefNew` Serial GC 中Young 区
- `Tenured` 代表单线程old 区垃圾回收. 老年区垃圾收集器是Serial Old
- `full GC (ergonomics)` Full GC 发生的原因是`UseAdaptiveSizePolicy`. JVM进行自动内存分配造成的
- `Full GC (Metadata GC Threshold))` 引发Full GC的原因可能是 元空间可能不够. 
- `

内存分布教下会增加GC频率. 也可能抛出OutOfMemoryError.

Java8 默认打开自适应参数. `UseAdaptiveSizePolicy` 它可以改变年轻代进入老年代的晋升次数. 

gceasy 可以汇总日志信息. 

### JVM线程堆栈分析/内存分析与相关工具 40:15

JVM 中线程分类
- JVM内部线程
  - VM线程: 单例的VMThread对象, 负责执行VM操作
  - 定时任务线程: 单例的WatcherThread对象, 模拟在VM中执行定时操作的计时器中断
  - GC线程: 垃圾收集器中, 用于支持并行和并发垃圾回收的线程
  - 编译器线程: 将字节码编译成本地机器代码
  - 信号分发线程: 等待进程指示的信号, 并将其分配给Java级别的信号处理方法
    - 在运行java时发送<C-c>命令接收命令的线程.
- 业务线程, 业务执行时自定义的线程池. 

安全点: GC执行STW的原理.java线程通过JVM操作操作系统的线程, JVM会在操作系统线程中加入很多安全点, 通过安全点向操作系统发送暂停命令. 

fastthread: 调试线程的信息. 通过`jstate -l pid`查询指定进程的线程信息. 将信息赋值到fastthread 的网页, 等一小会儿. 就可以显示判断结果. 

对象内存组成:
- 对象头
  - 存储对象自身的运行时数据
  - 类型指针
  - 对象长度
    - 64位系统中, 自身信息至少8个字节. 4个字节的引用信息. 一个对象的长度 必须是8字节的倍数, 在没有对象体的情况下也需要对齐. 添加4个字节. 一个对象长度至少16个字节
    - 32位系统. 自身信息4个字节, 4个字节的引用信息. 一个对象长度需要是4个字节的倍数. 所以一个对象至少8个字节长.
    - 通常在32位JVM, 堆内存小于32G的64位JVM上(默认开启指针压缩), 一个应用占的内存默认是4个字节.
- 对象体
  - 实例数据
  - 内部空白(padding)
- 外部对齐(alignment): 

查看对象的大小:
- `Instrumentation.getObjectSize`.  Instrumentation 是一种java代理, 使用起来很麻烦.
- JOL 是一个开源的框架.
- `jmap -histo` 显示每个类型有多少对象, 总共占有多少个字节.
- `jcmd 查看VM heap info 看类型占用的空间.

OutOfMemoryError 异常原因和解决方法:
- OutOfMemoryError: Unable to create new native thread
  - 创建的线程数已达到做系统的限制
  - 解决方法
    - 使用`ulimit -a`查看相关参数(这个不能查看到进程所允许的线程数). 通过`echo `120000 > /proc/sys/kernal/threads-max`设置最大的线程数
    - 降低Xss等参数
    - 调整代码, 改变线程创建和使用方式.

### JVM 分析调优经验 29:51

分配速率: 单位时间内, 分配给新对象的内存量. 通常单位是M/sec.
- 高分配速率: 导致新生代非常快被填满. 导致YoungGC 发生速率非常高. 意味着STW 速率很高.
- 增大young区或者eden 区.这样会导致YoungGC时间比较长, 这是一种蓄水池效应.

晋升速率: 从年轻代提升到老年代这样的数据量.
- 通过提升速率判断对象是否过早的被从年轻代提升到了老年代. major GC不是为频繁回收设计的, 过早提升会导致GC暂停时间过程. 这会严重影响吞吐量.

过早提升表现为以下三种情况
1. 短时间内频繁执行Full GC
2. 每次fullGC后老年代使用频率都很低, 在10%-20%或以下
3. 提升速率接近于分配速率


### JVM疑难情况问题分析 27:17

### Java Socket编程 19:23

RPS(request per second 每秒请求数):

- 使用单线程, 多线程, 线程池完成ServerSocket.
  - 同一个端口的socket 是串行获得. 返回的socket 是在另一个端口返回. 可以多个线程执行返回操作.
- 使用wrk 测试应用程序性能

### 深入讨论IO 11:08

服务器通信模型分类:
1. CPU密集型
2. IO密集型. 文件读取, 写入. 数据库操作等

服务器通信优化: 
1. 创建多个线程, 一些线程等待IO操作时, 一些线程可以充分利用CPU计算资源. 
2. 数据拷贝过程"网卡->内核空间->JVM". 减少数据的复制也可以加快响应时间
3. 细分IO, 一次IO可能需要经过多个阶段的计算和IO操作. 针对每个阶段的具体模型进行优化.

### IO模型与相关概念 33:12

同步, 异步是通信模式
阻塞, 非阻塞是线程处理模式

- BIO(blocking IO): 
  - java 线程调用内核处理IO, 等待内核处理完毕并返回后继续执行.
- NIO(Non-blocking IO) 通信相关的各个线程没有完全被阻塞住, 阻塞的不彻底.
  - java 线程调用内核处理IO, 内核不能直接处理就直接返回, java线程可以执行其他任务, 不断循环访问直到得到结果.例如循环访问Future的isDone() 直到返回返回true为止.
- IO复用模型. 对IO的访问交给了Reactor 进行. 在NIO的基础上增大了效率. Netty 使用这个模型
  - IO复用本质和NIO类似, 不过由内核系统来执行请求线程来做的轮询操作. 在数据可用时向请求进程发出可使用的信号.例如从网卡到内核的数据复制完成. 
  - 会有两个阻塞点: 
    1. 用户进程调用select或 poll时, 会轮询多个fd(file descriptor) 查看是由有准备好的fd(可以对应的是socket). 没有准备好的fd, 返回一个可读条件.  
    2. 有准备好的socket 会通知对应的线程.并且赋值数据.
 - select/poll缺点
   - 每次调用select, 都会将fd从用户态拷贝到内核态, 这个开销在fd很多时会很大
   - 每次调用select都需要在内核遍历所有传进来的fd, 这个开销在fd很多时也很大
   - select支持的fd数量太少, 最多1024
 - epoll优点
   - 内核与用户空间共享一块内存
   - 通过回调解决遍历问题
   - fd数量没有限制, 会通过回调解决C10K问题.
  - C10K: 单机想要并发支持10K连接, 不太好支撑. 使用IO复用模型是一个很好思路.
  - Netty 大部分时候是基于Reactor的IO复用的IO模型. 
- 信号驱动的IO模型. 调用内核获取数据时, 如果不能立即返回, 接受一个SIGIO handler, 不用以后继续调用内核获取数据. 如果内核能够返回数据就给java线程一个SIGIO信号. 使Java线程继续读取数据. 这个是采用epoll技术
- 异步IO: 全程没有阻塞的IO. 会在处理好数据后给Java线程返回信号. Linux 不支持. window 的IOCP(称为Proactor)模型支持.

Web Service 发展历程
1. 线程池
2. EDA(Event driven Architecture 事件驱动架构))

### Netty 框架简介以及Netty使用示例 32:33    

使用Netty的框架
- Spring5的web flus
- Vert.x
-  Zuul 2.0(是netflix基于Netty 改进的网关)
- Spring Cloud 中Spring Cloud gateway 基于web flux
- Soul 基于web flux

Netty 概览
- Netty核心
  - Zero-Copy-capable rich byte buffer: 
  - Universal Communication API 统一网络通信API
  - Extensible Event Model(扩展的事件模块)
- 传输服务
  - Socket&Datagram: 支持TCP/IP, UDP
  - HTTP Tunnel:
  - In-VM Pipe: 在同一进程内两个不同组件的通信
- 协议支持
  - HTTP&HTTPS&WebSocket
  - SSL&StartTLS
  - Google Protobuf
  - zlib/gzip Compression
  - Large File Transfer
  - RTSP
  - Legacy Text&Binary Protocols with Unit Testability

网络应用开发框架特点:
1. 异步
2. 事件驱动
3. 基于NIO

适用于:
- 服务器
- 客户端: netty 将客户端和服务器的API做的非常像. 
- TCP/UDP

Netty服务器优点. 从netty实现机制和网络的表现时的优点:
- 高吞吐
- 低延迟
- 低开销: NIO完成了上面三个特点.
- 零拷贝: 用户进程和内核进程共享一段内存. 可以避免重内核内存到用户内存的拷贝. Linux的epoll添加的新功能.
- 可扩容: Netty 底层的Byte Buffer 可以扩容. 

从使用者角度看的优点:
- 松耦合: 网络和业务逻辑分离
- 使用方便, 可维护性好

Netty嵌入协议
- HTTP Server
- HTTPS Server
- webSocket: 构建在Http 1.1 基础上, 复用了Http1.1 的通道. 
  - 刚开始使用http协议和客户端联系, 然后升级协议, 使用TCP 进行二进制的传输. 可以方便服务器端给客户端推送数据量特别大的消息.
- TCP Server, UDP Server
- In VM Pipe

Netty, Apache(Apache Http server 简称), Nginx 是 HTTP Server: 可以正常被浏览器访问, 可以调用它的一些URL路径访问一些资源.
Tomcat, WebSphere, WebLogic, JBoss是web Server: 在HTTP Server支持Web, 比如说Servlet JSP, Session和Cookie
EJB 是J2EE Server: 在Web Server 基础上添加了很多J2EE的能力, 比如说JMS消息的处理, JNDI, 对命名服务的处理, 容器上的事务(在Server容器上去配置我们的数据库连接池和事务)这些EJB才有的能力

Netty基本概念:
- Channel: Java NIO 基本概念, 避免直接操作Socket.
- ChannelFuture: Java Future 接口. 只能查询操作完成的情况或者阻塞当前线程直到操作完成. 可以将回调方法传递给ChannelFuture, 在需要时自动执行.
- Event&Handler: netty 是事件驱动, 事件和处理器可以关联到入站和出站数据流.
- Encoder&Decoder: 处理网络IO时, 需要进行序列化和反序列化进行转换Java和字节流. 对入站数据进行解码, 基类是ByteToMessageDecoder, 对出站数据进行编码, 基类是MessageToByteEncoder
- ChannelPipeline: 数据处理管道就是事件处理器链. 有顺序的, 同一Channel的出站处理器和入站处理器在同一列表中.

事件处理接口
- ChannelHandler
- ChannelOutboundHandler
- ChannelInboundHandler
事件处理器适配器
- ChannelInboundHandlerAdapter
- ChannelOutBoundHandlerAdapter

入站事件:  比如server 端. 接收用户请求数据时
- 通道激活和停用
- 读操作事件
- 异常事件
- 用户事件

**出站事件**
- 打开链接
- 关闭链接
- 写入数据
- 刷新数据

- ServerBootstrap
- NioEventLoopGroup
- ChannelInitializer\<SocketChannel\>
- HttpServerCodec Http Server的编码器
- HttpObjectAggregator: Http报文聚合器
- ChannelInboundHandlerAdapter 读取用户请求的适配器

网关是微服务最靠前的, 对网络这块有最高要求的一环, 所以特别适合使用netty. 

### 作业

1.（选做）使用 GCLogAnalysis.java 自己演练一遍串行 / 并行 /CMS/G1 的案例。

使用Java8 运行 四个案例. 

2.（选做）使用压测工具（wrk 或 sb），演练 gateway-server-0.0.1-SNAPSHOT.jar 示例。

3.（选做）如果自己本地有可以运行的项目，可以按照 2 的方式进行演练。

4.（必做）根据上述自己对于 1 和 2 的演示，写一段对于不同 GC 和堆内存的总结，提交到 GitHub。

每次垃圾回收次数统计
  每次有不同堆内存
  每次对应的young和old版本

串行GC: 单线程运行速度
并行, CMS, G1 设置为单线程的现象
CMS, G1的多个步骤
并行GC对于高吞吐量的展示.

串行: 使用256M, 1G 运行比较数据
并行: 使用256, 1G, 4G 比较. 使用256 单线程比较
CMS: 和ParNew 结合, 取中了响应速度和吞吐量 245, 1G, 4G
G1 适合大内存, 响应速度很快, 测试256m, 4G和8G, 单线程,多线程比较

gateway 使用命令行工具对于内存的消耗.

5.（选做）运行课上的例子，以及 Netty 的例子，分析相关现象。
6.（必做）写一段代码，使用 HttpClient 或 OkHttp 访问  http://localhost:8801 ，代码提交到 GitHub
> 访问的服务器是本节课最开始实现的HTTPServer01.

## 第三周:NIO技术/Java并发编程

书籍:
- Java并发编程:设计原则与模式
- Java并发编程的艺术
- **Java并发编程实战**
- Java多线程与并发面试题

### 再谈什么是高性能 19:37

高性能:
1. 高并发用户(Concurrent Users):可以承载海量的高并发用户. 
   - 测试命令`wrk -c 40 -d 30s --latency http://localhost:8088` 中`-c`就是并发用户
2. 高吞吐量(Throughput)
    - 单位时间内能够处理足够多的业务.
    - 使用TPS(Transaction Per Second): 每秒钟处理的事务.例如成交的一笔订单, 处理一笔结算.
    - 使用QPS(Query Per Second): 每秒钟处理的查询量.
3. 低延迟(latency): 每个业务请求过来满足请求时间很短
    - `wrk --latency`: 会使程序打印出延迟数据.
    - 平均延迟: 偏差比较大
    - 分位数表示: p50等于3毫秒. 说明50%的请求是在3毫秒内完成的.
4. 容量: 

高性能的另一面
1. 系统复杂度*10以上
   1. 建设与维护成本++
   2. 故障或Bug导致的破坏性*10以上.

对复杂系统产生问题的应对策略:稳定性建设(相关学科:混沌工程):
1. 容量: 对容量进行预估. 可能要处理的容量, 自身能处理的容量
2. 爆炸半径: 可能发生事故的阶段, 范围.
3. 工程方面积累与改进: 把过往发生的问题记录下来, 什么人容易出现问题, 那个模块哪个团队哪些人容易出问题. 针性做些准备措施. 

### Netty如何实现高性能 26:41

Reactor Stream: 反应式编程相关规范及接口
Reactor: Spring 对Reactor Stream的实现
Web flux: 以Reactor为基础, 实现web 领域的反应式编程框架.

Dong Lea 将Reactor 分为三类
1. 单线程模型: 单线程处理事件分发, IO(服务器数据的收发), 其他事件处理.
2. 多线程模型: 单线程处理网络连接和事件分发. IO的处理和业务的处理在线程做了. 使用线程池处理.
3. Reactor 主从模型. 
   - mainReactor 接收客户端请求连接的状态维护.
   - subReactor 数据在内存中处理好可以读了. 负责相关事件的分发.
   - 线程池 线程处理分发到的数据. 

NIOEventLoop: 带了一个Selector的线程. . 内部是不断对channel传递过来事件的循环.  NIOEventLoop可以分配给一个到多个channel.  NIOEventLoopGroup可以根据参数创建多个EventLoop. EventLoop 在netty 中是所作为一个事件分发器使用. channel 挂了任务处理器.channel 包含ChannelPipeLine, pipe 中包含 ChannelInboundHandler 和ChannelOutboundHandler.

EventLoop:
- 和一个Thread绑定, 所有EventLoop处理的IO事件都将在它转有的Thread处理.
- 一个Channel 在它生命周期内只注册于一个EventLoop, 一个EventLoop可以分配给一个到多个Channel.
- 带有一个Selector

对上一段总结:
- Reactor: 分为mainRactor和subReactor. 接收client请求. netty 使用EventLoopGroup 作为reactor. 
- EventLoopGroup 包含EventLoop, 一个eventLoop 包含一条线程. 可以循环对事件进行分发. 伊特eventloop可以包含多个Channel
- Channel 包含ChannelPipeLine, 可以管道化挂载多个 ChannelInboundHandler 和ChannelOutboundHandler.

### Netty网络程序优化 34:41

- 网络协议的优化点
  - tcp 协议
    - 网络通信的所谓粘包和拆包问题. 
      - ByteToMessageDecoder提供的一些常见的实现类
        - FixedLengthFrameDecoder: 定长协议解码器
        - LineBasedFrameDecoder: 行分隔符解码器
        - LengthFieldBasedFrameDecoder: 长度编码解码器, 将报文划分为报文头/报文体. 报文头包含报文头和报文体长度.
        - JsonObjectDecoder: json格式解码器. 
    - 网络拥堵与Nagle算法优化.粘包和拆包的原因
      - 用户端会将请求的大数据量文件拆分为多个包. 这里涉及到MTU(maxitum transmission unit 最大传输单元)和MSS(maxitum segment size 最大分段大小). MTU 一般设置为1500 Byte. MSS 对应最大设置为1460(1500-20(IP头)-20(TCP头)). 有时候发送1461个字节和1460个字节的传输效率不一样可能就是因为网卡做了数据分包.
      - nagle算法可以使在发送小数据量时进行缓冲, 直到接近最大的发送量位置. `TCP_NODELAY`可以设置为不采用nagle算法. 
    - 链接优化. TCP:三次握手, 四次挥手. UDP 没有握手
      - 三次握手: 客户端主动连接服务器.
      - 四次挥手: 客户端和服务器都可以自己发起断开挥手. 如果客户端发起的最后一次挥手, 那么连接会根据不同系统保存两个MSL时间(连接处于TIME-WAIT状态). windows MSL 是2分钟, linux MSL是1分钟. 如果不间断的进行性能测试, 那可能会在第二次测试时抛出资源不足的异常. 例如接口冲突等. linux 可以通过修改`/proc/sys/net/ipv4/tcp_fin_timeout`文件修改参数. windows 需要通过修改注册表TcpTimedWaitDelay的选项.
      ![三次连接和四次挥手](images/三次握手和四次挥手.png)
- Netty 常见的网络程序优化
  - 不要阻塞EventLoop. eventLoop是一个单线程. 如果某个方法需要执行很长时间, 最好把该方法执行放入新的线程中.
  - linux 下可以通过`ulimit -a`查看系统参数. 
  - linux 可以通过修改`/proc/sys/net/ipv4/tcp_fin_timeout`文件修改参数. windows 需要通过修改注册表TcpTimedWaitDelay的选项.
  - 修改netty 缓冲区参数
    - `SO_REVBUF`, `SO_SNDBUF`设置收发仓库的大小. 如果仓库满了, 就会开始发送数据或者接受完成. 在ServerBootstrap 中可以设定
    - `SO_BACKLOG`设定处于正在连接的连接数, 超过可能报错. 连接分为两个状态:正在连接, 可以传输数据.
    - `REUSEXXX` 复用端口, 处于TIME-WAIT状态的连接也可以复用.
  - 心跳周期优化
    - 心跳机制与断线重连: 服务器故障或者网络出现短暂问题, 网关可以在适当的时候重新发起连接.
  - 内存与ByteBuffer优化
    - Netty 对ByteBuffer有两个实现. 
      - DirectBuffer: 是堆外内存, 不会受GC影响. 推荐使用
      - HeapBuffer: 是堆内缓存. 受GC影响, 效率可能稍低.
  - 其他优化
    - ioRatio: 设置IO操作的CPU时间和其他操作的CPU时间. 默认是50:50
    - Watermark(水位): Z设置IO的高水位和低水位
    - TrafficShaping(流量整形): 如果请求IO过道, 就可以将请求放入缓冲队列中. 稍后执行. 有保险丝作用. 

### 典型应用:API网关 19:33

网关的四大职能
- 请求接入
- 业务聚合
- 中介策略
- 统一管理

网关分类:
- 流量网关
- 业务网关

网关对比
- 性能非常好, 适合做流量网关
  - OpenResty: 通过LUA扩展NGINX实现的可伸缩的Web平台
  - Kong: 基于OpenResty 实现, 整合的更完善.
- 扩展性好, 适合业务网关, 二次开发
  - Spring Cloud Gateway
  - Zuul2

**17:28 源码兴趣小组**

### 自己动手实现API 网关 18:15

### 多线程基础 18:54

JVM 会将Java线程映射为操作系统线程.

随着发展线程和进程的区别越来越小. Linux的状况更是这样.

### Java多线程 29:33

Thread的状态改变操作
1. `Thread.sleep(long millis): 变成计时等待状态, 不释放锁.**让出CPU时间片**
2. `Thread.yield()`: 变成可运行状态, 正在执行的线程出让CPU时间段, 不出让锁资源. 让相同优先级线程轮流执行, 但不保证轮流执行, 由线程调度给的运气决定.
3. `thread.join()/thread.join(long millis)`: 当前线程调用了其他thread对象的join方法. 当前线程等待thread执行完毕或者经过millis毫秒. 当前线程不会释放任何对象锁. join方法调用了thread的wait方法, 释放thread对象锁. thread执行完毕或者时间已到, 当前线程进入就可运行状态. wait操作对应的notify()方法由JVM底层线程执行结束前出发.
4. `object.wait`当前线程调用对象的wait()方法, 进入等待队列, 当前线程释放掉obj对象锁, 依靠notify()或者notifyAll()唤醒.
5. `obj.notify()`:唤醒在此对象监视器等待队列的任意单个线程. `obj.notify()`唤醒在此对象监控器上所有线程.

Thread的中断与异常处理
1. 线程内部自己处理异常, 不溢出到外层(Future 可以封装线程的异常)
2. 如果线程被`obj.wait, thread.join, Thread.sleep`方法等待, 再对线程调用interrupt()方法, 线程会抛出interruptException异常, 并重置中断状态. 如果线程中断状态为true后被前面三个方法等待, 线程一会抛出异常并且重置等待状态.
   - 线程A调用线程B对象的join方法, 然后线程C调用线程B对象的interrupt()方法. 线程A和线程B都不会抛出interruptException. 如果线程C调用线程A的interrupt方法时, 线程A会在B对象的join方法调用处抛出interruptException异常.
3. 如果计算密集型操作怎么办
> 分段处理, 每个片段检查下中断状态.

对线程的主动操作
> wait, notify, lock, sleep, join, await等操作
对线程的被动操作
> 需要对象锁时的阻塞. join所在线程执行完后的notify 方法调用.

### 线程安全 32:11

名词:
竟态条件: 多线程竞争同一资源时, 如果结果对资源的访问顺序敏感, 就称为竞态条件
临界区: 导致竞态条件发生的代码区称为临界区.

并发相关的性质:
原子性: 
可见性:volatile 不保证原子性
有序性: 

Java语言的先行发生(Happens-Before). 该原则指导一部分线程安全性, 也可以使用这些原则判断数据是否存在竞争, 线程是否安全
1. 程序次序规则(Program order rule): 在一个线程中, 按照控制流顺序, 书写在前面的操作先行发生于书写在后面的操作
2. 管程锁定规则(Monitor Lock Rule): 一个unlock操作先行发生于对同一个锁的lock操作
3. volatile 变量规则(volatile variable rule): 对volatile变量的写操作先行发生于后面对这个变量的读操作
4. 线程启动规则(Thread State Rule): Thread 对象的start方法先行发生于此线程的每一个动作.
5. 线程终止规则(Thread Termination Rule): 线程中的所有操作都先行发生于对此线程的终止操作. 我们可以通过`Thread::join()` 方法是否结束, `Thread::isAlive()`的返回值等手段检测线程是否已经终止执行.
6. 线程中断规则(Thread interruption rule): 对线程interrupt() 方法的调用先行发生于被中断线程的代码检测到中断事件的发生.
7. 对象终结规则(Finalizer rule): 一个对象的初始化完成(构造函数执行结束)先行发生于它的finalize()方法的开始.
8. 传递性(Transitivity): 如果操作A先行发生于操作B, 操作B先行发生于操作C, 那就可以得出操作A先行发生于操作C的结论.

synchronized usage
- 在方法上添加关键字
- 在代码块上添加关键字
  - 可以使用多个对象作为synchronized的锁对象. 
  - 这样可以减少锁的粒度, 增加并发的可能性.

synchronzied的实现
偏向锁:一个线程释放锁后, 和其他线程相比释放锁的线程更有可能获取锁.
轻量级锁: 可以看做使用CAS(Compare And swap)实现的乐观锁
重量级锁: Java 开始的Synchronized 机制的实现.

volatile
1. 每次读取都强制从主内存刷数据
2. 使用场景: 单个线程写; 多个线程读
3. 能不用就不用, 不确定的时候也不用
4. 替代方案: Atomic原子操作类
5. 作为两块代码的栅栏. 避免在优化时交叉两块代码的次序

final
- 修饰类: 不能被继承
- 修饰方法: 方法不能被重写
- 修饰局部变量: 不允许赋值为null再赋其他值, 并且在被其他方法调用前必须被赋值.普通的方法调用和lambda语句块调用时对未赋值的警告语句不同.
- final static: 静态语句块完成前,`<init>` 方法执行前必须赋值


### 线程池原理与应用 40:53

线程池的优势:
1. 可以避免创建线程的负担
2. 可以方便控制线程的数量

线程池中线程比较多时, 线程之间的上下文切换也会是一个很大的负担.

线程池:
1. Executor
2. ExecutorService
   - awaitTermination(long timeout, Timeunit unit): 阻塞当前线程知道线程池任务完成, 或者超时, 或者当前线程被中断. 方便优雅停机.
3. ThreadPoolExecutor: 可以通过new 新建对象, 可以传入核心线程数, 最大线程数, 拒绝策略, 阻塞队列, ThreadFactory 对象等,
   - execute(Runnable r) 执行流程. 
     1. 比较当前执行的线程数与核心线程数大小
      - 小于核心线程数, 就新建线程并将r放入线程
      - 大于等于核心线程数. 将r放入缓冲队列中. Blocking Queue(缓冲了队列)的实现
        1. ArrayBlockingQueue
        2. LinkedBlockingQueue
        3. PriorityBlockingQueue
        4. SynchronizedQueue: 特殊的BlockingQueue, 对元素的存取必须交替完成.
      - 等待队列已满, 不能放下. 创建新线程执行r.
      - 当前线程数已大于等于最大线程数. 就拒绝添加.拒绝策略如下
        - ThreadPoolExecutor.AbortPolicy: 拒绝任务并抛出RejectedExecutionExceptjion. **默认方针**
        - ThreadPoolExecutor.DiscardPolicy: 丢弃任务, 但是不抛出异常
        - DiscardOldestPolicy: 丢弃队列最前面的任务, 重新提交被拒绝的任务.
        - CallerRunsPolicy: 由调用线程处理该任务. **最常用方针**
4. interface ThreadFactory: 创建统一属性的线程, 设置有关联属性的线程. 线程池中线程都试在线程工厂中创建的.
5. Executors: 创建线程池


### 作业

1.（必做）整合你上次作业的 httpclient/okhttp；

> 使用NettyHttpServer 调用okHttp或者httpClient调用其他服务, 返回给用户内容.另外一个服务可以编写serverSocket或者springBoot

2.（选做）使用 netty 实现后端 http 访问（代替上一步骤）

> 使用netty 做一个http 客户端代替上面okHttp

3.（必做）实现过滤器。

> 在NettyHttpServer中添加过滤器.例如在请求头中加入请求头, okHttp 使用该请求头访问下一个服务.

4.（选做）实现路由。

> 在NettyHttpServer 中添加路由实现. 

5.（选做）跑一跑课上的各个例子，加深对多线程的理解

6.（选做）完善网关的例子，试着调整其中的线程池参数
- CPU密集型设置为核心数相等或加1
- IO密集型, 线程池设为2N或2N+2

#### 大纲

1. 拷贝nio02 项目
2. 将 nio01 中的 HttpServer03 代码整合进来作为后端服务，改名为 BackendServer, 监听 8088 端口。
3. RandomHttpEndpointRouter 添加到项目中, 实现路由
4. 编写Netty客户端, 获取router返回地址并访问对应的BackendServer
5. 添加过滤器
6. 调整网关线程池参数

### Thinking

#### about multithread

## 第四周:Java并发编程

2021年5月19日
- 因为不知道第三周的内容该怎么学习, 不愿意放弃那一个学习过程而逃到这记录新内容

### 1.Java并发包 13:28

java.rmi.* 是Java rpc 相关调用包.

JUC(Java.util.concurrency)

并发的五个重要知识点
- 锁 synchronized/wait, Lock, Condition, ReentrantLock, ReadWriteLock, LockSupport
- 原子锁: AtomicInteger, AtomicLong, LongAdder
- 线程池: Executor: Future, Callable, Executor, ExecutorService
- 工具类: CountDownLatch, CyclicBarrier, Semaphore
- 集合类: CopyOnWriteArrayList, ConcurrentMap

### 2.到底什么是锁 26:48

synchronized 方式的问题
1. 同步块获取锁的过程无法中断: Lock::lockInterruptibly, Lock::tyrLock(long timeout, TimeUnit unti)
2. 同步块获取锁时无法控制超时: Lock::tyrLock(long timeout, TimeUnit unti)
3. 同步块获取锁时异步处理锁(不能立即知道是否可以得到锁) boolean Lock::tryLock()
4. 每个sync 只能有一个条件

- Lock
  - methods:
    - void lock()
    - boolean tryLock() 尝试获取锁, 获取不到就立即返回.
    - boolean tryLock(long timeout, TimeUnit unti) throws InterruptedException 计时等待获取锁, 在获取锁的过程中相应中断请求
    - void lockInterruptibly() throws InterruptedException 在获取锁的过程中可以相应中断请求
    - void unlock()
    - Condition newCondition()
      - await() throws Interrupted
      - awaitUninterruptibly();
      - boolean await(long time, TimeUnit) throws InterruptedException
      - awaitUntil(Date deadLine) throws InterruptedException
      - signal();
      - signalAll()
  - 实现类
    - ReentrantLock 
    - ReadLock
    - WriteLock
- ReadWriteLock: 可以获取读锁和写锁. 读锁获取后, 读到的内容在放弃读锁前一直不会发生改变. 写锁的线程写入的内容, 新获取的读锁才能读取最新内容
  - ReentrantReadWriteLock

LockSupport: 方法都是静态方法
- park(Object blocker): 暂停当前线程
- parkNanos(Object, long):暂停当前线程, 不过有超时时间的限制
- parkUntil(Object, long) 
- park()
- parkNanos(long)
- parkUntil(long)
- unpark(Thread t): 使线程t开始运行
- Object getBlocker(Thread t) 获取被暂停线程的blocker

Doug Lea "Java并发编程: 设计原则与模式"推荐的三个用锁的最佳实践:
1. 永远只在更新对象的成员变量时加锁
2. 永远只在访问可变的成员变量时加锁
3. 永远不再调用其他对象的方法时加锁

KK总结:
1. 降低锁范围: 锁定的代码的范围/作用域尽量小.
2. 细分所粒度: 将一个大锁, 拆分成多个小锁

### 3.并发原子类 17:35

所在包`java.util.concurrency.atomic`

无锁技术底层实现原理:
- Unsafe API - CompareAndSweap
- CPU硬件指令支持-CAS指令
- Value的可见性-volatile关键字. 主内存中的数据发生改变会同步到线程内存中. 不保证原子性是不保证对该值的操作不会被打断, 例如:`!value`不能保证对value的读取, 反转, 写入不被打断.

### 4.并发工具类详解 34:57
AQS(AbstractQueuedSynchronizer 队列同步器): 构建锁或其他同步组件的基础(如Semaphore, Lock, CountDownLatch等) 是JUC核心并发包的基础组件, 抽象了竞争的资源和线程队列.

为什么需要并发工具类:
- 多个内核需要并发执行增大效率. 使用synchronized执行, 会将程序编程串行, 效率比较低下. 使用wait\notify, Lock\Condition. 可以完成多线程之间的简单协调. 但是效率低下并且将锁操作直接放在业务代码中, 会造成代码使用难度和阅读难度增加. 并且这些操作不能控制要运行的线程的并发数量和时间(sysnchronzied 等操作的时间控制需要根据start 调用顺序执行). 

更细粒度, 更好的控制并发线程以及他们之间协作和配合的方式. AQS(AbstactQueuedSynchronizer抽象队列同步器), 它是构建锁和这些并发工具类的基础. Lock下面的实现类, 如"ReentrantLock", "WriteReadLock". AQS 几乎是java并发工具包里最核心的组件. AQS抽象了多个线程以及线程控制的资源. 

- Semaphore: 指定可使用的锁的数量. 保证只能小于等于指定的线程数目运行.
  - acquired
  - acquiredUninterrupted
  - release
- CountdownLatch: 通过countDown和await方法指定数量的线程运行完后继续运行当前线程.
- CyclicBarrier: 循环屏障, 比较简单, 不是基于AQS形成. 使使用的线程等待,直到等待的线程的个数达到要求为止. 可以重复使用.

### 5.常用线程安全类型/并发编程相关内容 1:04:19

多线程相关知识, 并发编程相关经验

- 常用线程安全类型
  - JDK基础类型与集合类
    - List
      - ArrayList: 可以设置初始容量或者初始值, 也可以有默认值.每次扩容会增加初始容量的1/2.
      - LinkedList:
      - Vecto(矢量): 可扩展数组, 线程安全
      - Stack(堆): 后进先出, 可扩展, 线程安全.
    - Set
      - LinkedSet, HashSet, TreeSet
    - Queue
      - Deque
      - LinkedList
    - Map
      - HashMap
      - LinkedHashMap
      - TreeMap
      - EnumMap(使用枚举类型作为键), EnumSet(使用枚举类型作为元素, 使用位序列实现)
    - Dictionary
      - HashTable
      - Properties
  - 将List 变为线程安全的类型
    - Collections.synchronizedList
    - Collections.unmodifiedList
    - 变为Vector
  - CopyOnWriteArrayList, CopyOnWriteArraySet
    - 修改时会新建一个副本, 原来读数据的线程读取的数据不会改变.
  - 链接散列集与映射
    - LinkedHashMap: 
    - LinkedHashSet
- 并发编程相关内容

Queue: 先进先出, Stack: 后进先出

ArrayList.elementData[] 是transient(序列化忽略). 因为elementData的长度可能要比实际元素的个数要大, 最好根据size 来序列化数组.

如果在ArrayList的所有方法上都加上synchronized 关键字, 该类仍然不是线程安全的. 因为一个线程在读, 

CopyOnWriteArrayList

HashMap: 
基本特定: 空间换时间. 
用途: 存放指定key的对象, 缓存对象
原理: 使用hash原理, 存k-v数据, 初始容量16, 扩容*2, 负载因子0.75. JDK8以后, 在链表长度到8& 数组长度到64后会自动变为红黑树

数组和排序的集合, 在元素比较少的时候采用冒泡算法, 比较多时候采用插入排序. 如果元素比较多时是快排.

ConcurrentHashMap: 将存储位置使用多个小锁进行分段, 一般是16个. 
- Java7: 将Key的高位进行取模分段, 在使用Key计算hash值并存放.
- java8: 基于无锁的改进, 去掉了分段. 如果数据量比较大时, 使用红黑树保存

LinkedHashMap: 插入循序, 访问顺序. 访问顺序可以支持缓存的LRU

### 6.并发编程经验总结及常见面试题 22:19

如果设置全局变量或者环境变量, 不同的线程访问可能会造成修改\读取的线程不安全. 可以使用ThreadLocal 设定某个线程保存某个对象, 线程在就可以一直获得. ThreadLocal 可以看成一个Context 模式.

stream的parallel 方法

伪并发问题. 跟并发冲突问题类似的场景很多
- 浏览器表单重复提交问题
  1. 客户端控制(调用方), 点击后按钮不可用, 跳转到其他页
  2. 服务器端控制(处理短), 在用户返回表单页面时, 提供一个sessionId, 在用户提交时校验用户的sessionId是否已经提交或者无效等. 

自旋锁: CAS可以做到, 避免使用锁.

线程间共享
- static/实例变量(堆内存))
- Lock
- synchronized
线程间协作
- Thread::join()
- Object::wait/notify/notifyAll
- Future/Callable
- CountdownLatch
- CyclicBarrier
- LockSupport

### 作业

1.（选做）把示例代码，运行一遍，思考课上相关的问题。也可以做一些比较。

2.（必做）思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这
个方法的返回值后，退出主线程? 写出你的方法，越多越好，提交到 GitHub。
一个简单的代码参考:  https://github.com/kimmking/JavaCourseCodes/tree/main/03concurrency/0301 /src/main/java/java0/conc0303/Homework03.java
> **主线程的退出需要在子线程之后**, **最好能写出十种以上的方法**
1. 对象内置锁
2. Lock
3. LockSupport
4. ThreadMethods join, yield, sleep
5. ThreadPool
6. ForkAbout
   1. Future
   2. CompletableFuture
   3. Callable
7. ConcurrentHashMap
8. BlockingQueue
9. Semaphore, CountDownLetch, CyclicBarr
10. volitile, final
11. CopyOnWriteArrayList, Set
12. Atomic

3.（选做）列举常用的并发操作 API 和工具类，简单分析其使用场景和优缺点。
4.（选做）请思考: 什么是并发? 什么是高并发? 实现高并发高可用系统需要考虑哪些 因素，对于这些你是怎么理解的?
5.（选做）请思考: 还有哪些跟并发类似 / 有关的场景和问题，有哪些可以借鉴的解决 办法。
6.（必做）把多线程和并发相关知识梳理一遍，画一个脑图，截图上传到 GitHub 上。 可选工具:xmind，百度脑图，wps，MindManage，或其他。

## 第五周:Java相关框架

### 1.Spring技术发展及框架设计 33:53

一个整合好的框架, 在上面加了通用的业务和方法. 组成了脚手架.

工具类-积累->工具箱, 类库
-添加其他功能->框架
->添加通用的业务模块
-> 脚手架-添加功能需求, 功能模块, 可以为用户使用
->做成了一个产品
->进行定制, 可以给其他业务用
-> 这就构成了平台-为了方便安装, 维护. 做成多租户的, 放在云端部署, 其他用户可以通过浏览器访问
->SAAS(Software as a service 软件即服务):
### 2.SpringAOP详解及SpringBean核心原理 49:36

#### Spring框架设计

Spring 核心功能: Bean的创建, Bean声明周期的管理, Bean的装配. 为了实现这个功能, 需要在源码和运行时增加一层处理, 所以需要SpringAOP(Aspect Oriented Programming).

Spring 循环依赖: 先初始化, 再加载. 所以通过setter方法设置不会有循环依赖. 构造器注入时会有. 相当于死锁. 也相当于垃圾回收时如果是计数器回收时, 就会产生死锁. 

#### Spring AOP 详解

Spring 核心功能对bean的创建, 管理. 整个生命周期内与其他对象之间的相互引用. AOP 帮助对bean进行管理增强.

IoC, DI.

有接口时使用JDK Proxy, 没有接口时使用CGLib. 可以通过设置强制使用CGlib

CGlib 的编译时间比动态代理时间长, 性能比动态代理好.

Spring AOP 使用的动态代理不是反射下的Proxy 类, 生成了com.sun.proxy 包下的代理类.

CGLib 使用了ASM类库. ASM 可以操纵类文件. CGlib(Code Generation library) 封装了ASM
AspectJ 是对AOP 语言级别的实现
Java Proxy Java动态代理
Javaassist(Java programming assistant) Java编译字节码的类库, 使java能够在运行时定义一个新类, 也可以在加载时修改类文件
Instrumentation(JDK 提供的新技术, 可以修改jar包中文件) 
ByteBuddy 字节码增强工具, 使用起来更方便.

#### Spring Bean 核心原理

@startuml Bean声明周期

start
:BeanFactoryPostProcessor.postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);
:InstantiationAwarePostProcessor.postProcessBeforeInstantiation; 
:实例化;
:InstantiationAwarePostProcessor.postProcessAfterInstantiation;
:InstantiationAwarePostProcessor.postProcessProperties;
:设置属性值;
:BeanNameAware;
:BeanFactoryAware;
:ApplicationContextAware;
:BeanPostProcessor.postProcessBeforeInstantiation;
:InitializingBean;
:init-method;
:BeanPostProcessor.postProcessAfterInstantiation;
if(scope) then (singleton)
:Spring 缓存池准备就绪的bean;
-> 容器销毁;
:DisposableBean.destroy();
:<destroy-method>;
else (prototype)
:准备就绪的bean交给调用者;
endif
stop

@enduml

Spring管理对象生命周期后, 也改变了编程和协作模式. 

### 3.Spring XML配置原理 21:18

XML 描述文档XSD(XML schema definition) 和DTD(document type defination)

spring xml 如何解析
- application.xml 中xmlns: 指定xsd命名空间(namespace)
- namespace 在xsi:schemaLocation 中指定对应的xsd文件名称
- schemaLocation 在spring.schemas 中指定对应的xsd文件本地的地址
- namespace 在spring.handlers 中指定xsd 文件的解析器. 其实就是在该命名空间下元素的属性怎么执行.

自己定义XSD 会很麻烦, spring-commons 中有自动化xml配置工具:XmlBeans, ActiveMQ 就是使用这个方式. 

POJO 到xsd 的生成的工具
- Xbeans
- XStream

XML 解析方式
- 全部加载xml文件 dom
- sax 流式加载

### 4.Spring Messaging等技术 24:11

- JMS(Java Message Service) 是Java消息队列接口, 是Java平台面向消息中间件的API. JMS 很多结果来自于"企业集成模式" 这本书. ActiveMQ 实现了这些API. Spring-messaging 采用了ActiveMQ
- AMQP(Advanced Message Queuing Protocol): 一个提供统一消息服务的应用层标准高级消息队列协议. RabbitMQ 使用Erlang 实现了该协议. 
- Kafka设计为集群运行, 从而能够实现很强的可扩展性.


JMS 定义消息有两种模式:
- Queue: 
  - Queue 消息只能给一个消费者
  - Producer
  - Consumer
- Topic
  - 会将消息分给所有订阅者
  - publisher
  - subscriber
- ActiveMQ 中queue 默认是持久化的. Topic默认是不支持持久化, 可以设置.

![ActiveMQ服务安装,启动](images/第五周-ActiveMQ服务安装_启动.png)
安装后浏览器默认访问接口是:8161. 发送消息, 接收消息时的端口是61616

### 5.从Spring到Spring Boot 29:35

Spring boot 自动装配和starter.

Spring boot 最熟悉的新功能-自动配置.
限定性框架, 非限定性框架

约定大于配置: 没有配置时会有默认值.

### 6.Spring Boot核心原理 27:24

Spring 开箱即用的配置
1. Maven的目录结构: 默认resources 存放文件, 默认打包方式为jar
2. 默认的配置文件application.properties application.yml
3. 默认使用spring.profiles.active 决定生产环境时的配置文件. 开发dev, 测试test, 预生产prepared, 生产环境prod
4. @EnableAutoConfiguration 默认对于依赖的starter进行自动装配. starter 依赖有其他框架时可能需要打开其他开关

@ConfigurationProperties 从application 中获取配置到当前类中变为属性. 
@EnableConfigurationProperties 从上一个注解的类中获取设置.
@Import 将其他@Configuration 引入到当前的configuration 中.

@SpringBootApplication
  - @SpringBootAutoConfiguration
    - Import(AutoConfigurationImportSelector.class):  spring.factories加载大量默认可以自动化配置的组件. spring.factories设置自动加载的类, selector 进行选择.

spring-boot-autoconfiguration 

条件化的自动配置
- @ConditionalOnBean: 当存在某个bean 才开始自动配置
- @ConditionalOnClass: 当某个class存在时自动配置
- @ConditionalOnMissingBean: 当某个类型bean 不存在时就配置
- @ConditionalOnProperty: 存在某种属性
- @ConditionalOnResource: 某种资源
- @ConditionalOnSingleCandidate: 当有一个bean 是主要的. @Primary 指定某个bean 是主要的. 
- @ConditionalOnWebApplication: 当前环境是一个web 项目时.
- 自定义一些conditional



### 7.Spring Boot Starter 详解 29:06

一个starter 用来继承第三方的类库或者组件. 必须存在的相关文件
- spring.provides: 当前包 
- spring.factories: 做自动配置时将自动配置的类写进去
- additional-spring-configuration-metadata.json: 描述当前spring boot 组件相关信息(例如application 文件中配置的信息).可以配什么节点, 有什么样的一些默认值.  这个文件时可选的 

@AutoConfigurationBefore(DataSourceAutoConfiguration.class): 当前starter 需要在DataSourceAuto...class 配置前配置

### 8.JDBC与数据库连接池/ORM-Hibernate/MyBatis 15:00
### 9.Spring/Spring Boot 继承ORM/JPA 46:27

JPA(Java Persistence API) 核心的类EntityManager. 调用方法可以直接操作数据库.

Spring JDBC和ORM区别
- Spring JDBC, JDBC, DataSource
- Spring ORM, JPA, EntityManager
- Spring 操作非关系数据库: NOSQL, Redies. 类似于操作JPA.

Spring事务配置参考
- 传播性: @Transactional(propagation=Propagation.REQUIRED)
- 隔离级别: @Transactional(isolation=Isolation.READ_UNCOMMITTED)
- 只读: @Tranactional(readOnly=true): 不能进行数据库修改.
- 超时: timeout=30 以秒为单位
- 回滚: rollbackFor{RuntimeException.class}

Spring/Spring Boot使用ORM经验
1. 本地事务: 事务包装的对象一定是通过spring获取的对象. 事务传播控制可能影响事务
2. 多数据源(配置, 静态配置, 动态切换). 默认只能配置一个数据源. 如果有多个数据源. 那么Spring boot 的SessionFactory可能报错. 
  - 只使用一个数据源
  - 将一个bean 标记为Primary.
  - Spring boot 提供的Routing数据源, 可以将多个数据源包装到一个AbstractRoutingDataSource
3. 连接池大小, 重连, 超时配置.
  - Mysql 不支持特别多的连接. 连接池开到50, 100 就够了. 连接大小可以通过压测出来
4. ORM 内的复杂sql, 级联查询不建议使用.
5. ORM辅助工具和插件

### 相关注解
- Autowired: 默认按照类型注入
    - required: true 表示初始化对象时, 注入依赖. false 是懒加载, 在使用时才加载.
- Resource: 默认按名称注入
    - name: **属性中添加这个名称有什么作用和影响.

### 作业

1.（选做）使 Java 里的动态代理，实现一个简单的 AOP。
2.（必做）写代码实现 Spring Bean 的装配，方式越多越好（XML、Annotation 都可以）, 提交到 GitHub。
> configuration 也可以
3.（选做）实现一个 Spring XML **自定义**配置，配置一组 Bean，例如：Student/Klass/School。

4.（选做，会添加到高手附加题）
4.1 （挑战）讲网关的 frontend/backend/filter/router 线程池都改造成 Spring 配置方式；
4.2 （挑战）基于 AOP 改造 Netty 网关，filter 和 router 使用 AOP 方式实现；
4.3 （中级挑战）基于前述改造，将网关请求前后端分离，中级使用 JMS 传递消息；
4.4 （中级挑战）尝试使用 ByteBuddy 实现一个简单的基于类的 AOP；
4.5 （超级挑战）尝试使用 ByteBuddy 与 Instrument 实现一个简单 JavaAgent 实现无侵入下的 AOP。

5.（选做）总结一下，单例的各种写法，比较它们的优劣。
6.（选做）maven/spring 的 profile 机制，都有什么用法？
7.（选做）总结 Hibernate 与 MyBatis 的各方面异同点。
8.（必做）给前面课程提供的 Student/Klass/School 实现**自动配置**和 Starter。
> 将Student Klass School 设定为自动配置一个项目
> 在 starter引用上面的jar包, 并配置student 信息. id name
9.（选做）学习 MyBatis-generator 的用法和原理，学会自定义 TypeHandler 处理复杂类型。
10.（必做）研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法：
1）使用 JDBC 原生接口，实现数据库的增删改查操作。
> RawJDBCRepository. H2创建表, 增删改查
2）使用事务，PrepareStatement 方式，批处理方式，改进上述操作。
> jdbc 事务 包围上面操作, PrepareStatement 替换Statement, 批处理执行操作
3）配置 Hikari 连接池，改进上述操作。提交代码到 GitHub。
> 从Hikari 获取连接执行上面操作.

附加题（可以后面上完数据库的课再考虑做）：
(挑战) 基于 AOP 和自定义注解，实现 @MyCache(60) 对于指定方法返回值缓存 60 秒。
(挑战) 自定义实现一个数据库连接池，并整合 Hibernate/Mybatis/Spring/SpringBoot。
(挑战) 基于 MyBatis 实现一个简单的分库分表 + 读写分离 + 分布式 ID 生成方案。

## 第六周 Java 相关框架/系统性能优化

### 1.Java8 Lambda 40:03
### 2.Java8 Stream 35:32
### 3.Lombok/Guava 33:37
### 4.设计原则与模式, 单元测试 49:20

面向对象设计原则S.O.L.I.D:
1. SRP(Single Responsibility Principle 单一责任原则)
2. OCP(Open Closed Priciple) 对修改封闭, 对扩展开放
3. LSP(The Liskov Substitution Priciple 里氏替换原则) 例如面相对象的时候, 任何一个父类都可以被子类替换.
4. ISP(The Interface Segregation Principle 接口分离原则): 接口与接口之间是相互隔离的, 不要尽量互相依赖.
5. DIP(The Dependency Inversion 依赖倒置原则) 上层类模块接口不应该依赖下层, 某个抽象或接口不要依赖于具体实现.

最小知识原则/KISS(Keep it Simple and Stupid)/迪米特法则: 依赖关系应该尽量少. 最终目标:高内聚低耦合:

模式更是一种经验的总结:
- 模式的3个层次: 解决方案层面(架构模式), 组件层面(框架模式), 代码层面(GOF设计模式)
- 其他模式: 继承模式, 事务模式, IO模式/Context模式, 设置状态机FSM, 规则引擎RE, workflow都是模式
- 反模式: 死用模式, 都是反模式.

测试: 可以体现自己对需求的理解. 对自己写出代码的确认.
- 黑盒
- 白盒
- 手动
- 自动化
- 单元测试: 白盒, 自动化测试.
- 集成测试: 测试整个服务, 流程
- 自动化端到端测试

单元测试: 只保证单个方法, 外部条件mock 掉. 
TTD: 测试驱动开发


checkstyle 检验是否符合规范. 
coverage 检验代码测试覆盖率

Junit 相关
- 相关注解
  - Junit5:@BeforeEach Junit4:@Before
  - BeforeAll BeforeClass
  - Tag  表示单元测试类别
  - DisplayName: 单元测试名称
  - Disabled Ignore 忽略测试
  - Repeat(times=) 重复测试
  - Timeout 测试超时
  - ExtendWith RunWith 为测试类或测试方法提供扩展.
- 断言
  - assertEquals
  - assertNotEquals
  - assertSame
  - assertNotSame
  - assertTrue
  - assertFalse
  - assertNull
  - assertNotNull
  - assertArrayEquals
  - assertAll 验证多个断言
- 前置条件:Assumptions. 出现异常后和@Disabled 类似, 不会出现在失败通知里面
- 嵌套测试: `@Nested`一般注解在内置类上, 外部测试不会调动内部的测试条件(@BeforeEach, @BeforeAll 等等). 内部测试可以调用外部的测试条件.
- 参数化测试:`@ParameterizedTest` 
  - `ValueSource` 为参数化测试指定入参来源, 支持八大基础类, String类型, Class类型
  - `@NullSource` 为参数化测试提供一个null 入参
  - `EnumSource` 表示为参数化测试提供一个枚举入参
  - `CsvFileSource` 表示读取指定CSV文件内容作为参数化擦拭入参
  - `MethodSource` 使用指定方法返回值作为入参. 方法需要static修饰, 返回值为Stream类型.

### 5.再聊聊性能优化与关系数据库MySQL 58:51 第五个大模块

#### 再聊聊性能优化

什么是性能: 并发用户的吞吐量, 延迟以及它的容量
- 吞吐和延迟可能没有直接关系
- 没有量化就没有改进: 监控与度量指标, 知道我们怎么去下手. 制定系统, 业务的相关指标. 
- 80/20原则: 业务问题优化可能是最大的问题.
- 过早的优化是万恶之源:指导我们要选择优化的时机
- 脱离场景谈性能都是耍流氓.

DB / SQL 优化是业务系统性能优化的核心
- 业务系统的分类：计算密集型、数据密集型(数据库IO, 网络IO)
- 业务处理本身无状态，数据状态最终要保存到数据库
- 一般来说，DB/SQL 操作的消耗在一次处理中占比最大
- 业务系统发展的不同阶段和时期，性能瓶颈要点不同，类似木桶装水

#### 关系数据库MySQL

数据库设计范式: 目标: 表之间的关系比较明确, 数据库的冗余比较少.

数据库设计范式: NF(normal form)
- 第一范式(1NF): 关系R属于第一范式, 当且仅当R中的每一个属性A的值域只包含原子项
- 第二范式(2NF): 在满足1NF的基础上, 消除非主属性对码部分的函数依赖. 码由多个属性组成, 非主属性对码的部分属性有依赖. 需要消除这种依赖. 非主属性->码的部分属性. 表中属性依赖部分主键形成唯一性.
- 第三范式(3NF): 在满足2NF的基础上, 消除非主属性对码传递性的函数依赖. 消除表中不依赖主键, 而依赖非主键行成唯一性..
- BC 范式(BCNF): 在满足3NF的基础上, 消除主属性对码的部分和传递函数依赖. 主属性依赖其他码的属性.
- 以下两个范式用的不多
  - 第四范式(4NF): 消除非平凡的多值依赖
  - 第五范式(5NF): 消除一些不适合的连接依赖

函数依赖: 如果存在模式(dept_name, budget), 则dept_name 可以作为主码. 这条规则定义为函数依赖(functional dependency).记为`dept_name -> budget`. 函数依赖形成记录的唯一性.

常见的数据库
- 关系数据库
  - 开源:mysql, postgreSQL
  - 商业: oracle, DB2, sql server 
  - 其他关系数据库: Access, sqlite, H2, Derby, Sybase, Infomix
- 内存数据库:
- 图数据库:存储微博, twitter的联系人
- 时序数据库: 物联网 一些数据的采集监控指标. 滴滴, 监控数据等. 
- No SQL 数据库: MongoDB, Hbase、Cassandra、CouchDB
- NewSQL/分布式数据库：TiDB、CockroachDB、NuoDB、OpenGauss、OB、TDSQL

- MYSQL 最为成功在后端的开源关系数据库: 5.7 是最主流的版本
- 使用最广泛的数据库: SQLite. Chrome浏览器和安卓手机都内置了sqlite
- mariaDB
- LAMP: 是2000年左右世界建站的主流


SQL语言分类
- 数据库查询语言(DQL data query language)
- 数据操作语言(DML: data manipulation language): insert update delete
- 数据定义语言(DDL: data define language): create 
- 数据控制语言(DCL: data controle langauge): GRANT REVOKE 权限控制
- 事务控制语言(TCL: transaction control language):commit savepoint rollback
- 指针控制语言(CCL: cursor control language): declate cursor fetch into

- 5.6/5.7的差异
5.7支持：
- 多主
- MGR 高可用: 分布式的高可用MGR.
- 分区表: partition. oracle 很早就支持.
- json
- 性能
- 修复XA 等

- 5.7/8.0的差异
  - 通用表达式:CTE
  - 窗口函数: 商用的关系数据库, PGSQL 都支持. 
  - 持久化参数
  - 自增列持久化
  - 默认编码utf8mb4
  - DDL 原子性
  - JSON 增强
  - 不再对group by 进行隐式排序？？==> 坑

### 6.深入数据库原理 36:19

MySQL 相关文件
- innodb_file_per_table=ON 独占模式
  - 日志组文件:ib_logfile0, ib_logfile1, 默认均为5M
  - 表结构文件: *.frm
  - 独占表空间文件: *.ibd. 存储数据. innodb_file_per_table=OFF 设置后会将一个数据库中所有表都存储在ibdata1 中.
  - 字符集和排序规则文件: db.opt
  - binlog 二进制日志文件: 记录主数据库服务器的DDL和DML操作
  - 二进制日志索引文件: master-bin.index

![MySQL执行引擎和状态]()

select count(*) from 查询时, MyISAM 比innodb 快, MyISAM会存储数据数量. 

查询语句子句的执行顺序:
1. from
2. on
3. join
4. where
5. group by
6. having
7. select
8. order by(可以使用select 子句中列的别名)
9. limit

为什么默认使用B+树存储:
1. 数据按页分块. 每次获取数据都是按块进行操作. 这样方便取出一块内容作为换窜
2. B+树根据主键索引存储. 它的每个数据项都包括上一个和下一个数据的索引. 方便取出相邻数据
3. 只有叶子节点存有数据, 可以储存更多地数据

为什么一般单表数据超过2000万?
1. B+数据结构3层是好性能的极限值.
2. 一般一个页16k. bigInt 主键占八个字节. 一个索引占6个字节. 一层索引是1024 * 16 / 14 = 1170个指针
3. 假设每个数据块是1K. 两层的B+数可以存储1170 * 16= 18720 个数据
4. 假设3层. 第一层1170 个指针, 每个指针指向第二层的一个块, 每个块也是1170个指针. 第三层每个块保存16条数据, 所以总共1170 * 1170 * 16 = 21902400

show variables like '%dir%': 查看所有文件夹相关信息
show variables like '%version%'
show variables like '%port%' 查看端口相关信息.

show create table tableName
show columns from tableName
show schemas; 查看所有数据库.
`create schema;` `create databases`
show create database databaseName.

### 7.MySQL配置优化与数据库设计优化 38:31

#### MySQL配置优化

`mysqld --default-files=` 为服务器指定默认配置文件. windows 上叫做my.ini. mac/linux 上叫做my.cnf
my.ini 结构
- [mysqld] server相关配置. 
- [mysql] client配置

`show variables` 查看当前用户的所有配置. 
`show variables like '%last%'`
`show global variables` 查看全局参数配置
`select @@last_insert_id` 查看变量
`select golbal @@` 查看全局变量
`set global` 设置全局变量. 5.6 5.7 全局设置默认不会持久化. 8 开始持久化

1. 连接请求的变量
  - max_connection: 最大连接数, `set global.connections 10000` 做测试时可以适量提高
  - back_log: 半连接状态的连接的数量
  - wait_timeout和interactive_timeout 最大等待时间和交互状态下最大连接.
2. 缓冲区变量
  - key_buffer_size: 索引内存的buffer大小, 影响索引的操作速度. MYISAM起作用
  - query_cache_size: 默认负责server查询结果的缓存. 5.6 5.7 有很大作用. 8.0 没有server层的缓存. 不建议使用.
  - max_connect_errors: 连接的时候允许的最大错误数.
  - **sort_buffer_size**: 排序时buffer的大小, 默认值1M.
  - **join_buffer_size**: 进行表连接时默认缓存大小.
  - max_allowed_packet: 向mysql server 发送最大数据包.
  - thread_cache_size 每个线程catch大小
3. 配置Innodb的几个变量
  - **innodb_buffer_pool_size=128M**: innodb 内存缓冲区大小. 查询的各种缓存都是使用这个.
  - innodb_flush_log_at_tx_commit
  - innodb_thread_concurrency=0 并发线程大小. 
  - innodb_log_buffer_size log 缓冲区大小
  - innodb_log_file_size log文件大小
  - innodb_log_file_in_group 
  - **read_buffer_size=1M** 读取数据缓存大小
  - **read_rnd_buffer_size=16M** 随机读的缓存大小
  - **bulk_insert_buffer_size=64M** 批量插入的缓存大小
  - binary log
    - 后面详细讲

#### MySQL设计优化

常见数据库库表设计
1. 如何恰当选择引擎: Myisam 不支持事务. Innodb 支持事务.临时表使用memory. 归档使用archive, toku
2. 库表如何命名: 不建议使用拼音, 缩写. 模块名作为数据库名称. 字段使用业务意义的名字. f表示float, 所有表前加上t. v表示是视图. p表示存储过程
3. 如何合理拆分宽表: 降低表的列数
4. 如何选择恰当数据类型: 明确, 尽量小
5. char, varchar, text, blob, clob: 后三者非常影响性能. 而且text, blob, clob 可能需要提交两次.
6. 文件, 图片是否要存入到数据库? 这样会导致数据库非常慢. 最好直接保存成文件. 数据库中保存路径
7. 时间日子存储问题: 
  - data datatime timestamp. 直接存成long类型. 如果对时间一致性要求比较高, 建议使用数据库的时间. 尽量避免时区产生的问题. 也可以在数据库中存成一个一大长串的数据自. 如160开头的一串数字, 这样避免时区的干扰.
8. 数值的精度问题: 英文float, double 不准确. 可以直接使用两个字段表示. 一个表示整数, 一个表示10^- 次方数. 
9.  是否使用外键, 触发器: 尽量不要使用.

MySQL 数据库设计优化-最佳实践
- 唯一约束和索引的关系: 唯一约束会自动生成索引
- 是否可以冗余字段: 根据需求
- 是否使用游标, 变量, 视图, 自定义函数, 存储过程: 不建议使用. 难移植, 数据库也不适合做业务问题.
- 自增主键的使用问题: 数据量不大时, 建议使用. 分布式数据时不要采用自增主键
- 能够在线修改表结构么(DDL): 会自动锁表. Mysqld dump 也会有类似结果. 尽量不要在线上干. 最好在停机维护时使用.
- 逻辑删除还是物理删除: 一般只做逻辑删除. 加一个标记删除位.
- 要不要加create_time, update_time 时间戳: 建议加上时间戳
- 数据库碎片问题: 定期优化. 优化时可能需要锁表. 在停机或没有压力时做.
- 如何快速导入导出, 备份数据. 
  - 直接查出来, 存储. 这时候需要锁表. 停机维护, 压力比较小时
  - 有主从结构, 把从库摘下来, 备份从库.
  - mysql 执行所有insert 导入或者load data. 通常先删除索引和约束条件.

### 作业

1.（选做）尝试使用 Lambda/Stream/Guava 优化之前作业的代码。

2.（选做）尝试使用 Lambda/Stream/Guava 优化工作中编码的代码。

3.（选做）根据课上提供的材料，系统性学习一遍设计模式，并在工作学习中思考如何用设计模式解决问题。

4.（选做）根据课上提供的材料，深入了解 Google 和 Alibaba 编码规范，并根据这些规范，检查自己写代码是否符合规范，有什么可以改进的。

5.（选做）基于课程中的设计原则和最佳实践，分析是否可以将自己负责的业务系统进行数据库设计或是数据库服务器方面的优化

6.（必做）基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交 DDL 的 SQL 文件到 Github（后面 2 周的作业依然要是用到这个表结构）。

> 保存地址: C:\Users\ck\Documents\Navicat\Premium\profiles

7.（选做）尽可能多的从“常见关系数据库”中列的清单，安装运行，并使用上一题的 SQL 测试简单的增删改查。

8.（选做）基于上一题，尝试对各个数据库测试 100 万订单数据的增删改查性能。

9.（选做）尝试对 MySQL 不同引擎下测试 100 万订单数据的增删改查性能。

10.（选做）模拟 1000 万订单数据，测试不同方式下导入导出（数据备份还原）MySQL 的速度，包括 jdbc 程序处理和命令行处理。思考和实践，如何提升处理效率。

11.（选做）对 MySQL 配置不同的数据库连接池（DBCP、C3P0、Druid、Hikari），测试增删改查 100 万次，对比性能，生成报告。

## 第七周:系统性能优化/超越分表分库

### 1.MySQL事务与锁 56:15

事务可靠性模型ACID:
- Atomicity: 原子性
- Consistency: 一致性
- Isolation: 原子性
- Durablity: 持久性

事务的锁分类
- 表级锁
  - 意向锁(Intent lock): 表明事务稍后要进行哪种类型的锁定. 表明事务稍后要进行哪种类型的锁定, 为了简化后面对锁的操作.
    - 共享意向锁(IS, Intent shared lock): 打算在某些行上设置共享锁.  select from tableName in share model
    - 排他意向锁(IX, Intent exclusive lock): 打算对某些行设置排它锁. 全表for update 上锁
    - Insert意向锁: Insert 操作设置的间隙锁
  - 自增锁(Auto-IN):  对自增加一个锁. 锁很快.
  - LOCK TABLES/DDL: 执行DDL 或者mysql dump 这些命令, 会直接导致锁表或者锁库. `lock tables tableName read/write` 将整个数据库的表都都锁住. `unlock tables` 解锁. 
- 行级锁
  - 记录锁(Record): 始终锁定索引记录, 注意隐藏的聚簇索引. 查询一条记录语句 或操作一条语句时上的锁. 上一个X锁或者S锁.`select * from tableName in share mode`. 为表上了一个S锁. `select * from tableName for update` 就上了一个X锁.
  - 间隙锁(Gap): 锁住一个范围. 对不存在的数据使用selct for update. 就锁住了一个范围. 
  - 临键锁(next-key): 记录锁+间隙锁的组合; 可"锁定" 表中不存在的记录
  - 谓词锁(Predicate): 空间索引
- 死锁
  - 阻塞与互相等待: 打破死锁的环,  kill掉一个事务或者为所有事务设定锁超时时间
  - 增删改, 锁定读
  - 死锁检查与自动回滚
  - 避免死锁
    - 锁粒度与程序设计
      - 降低锁粒度
      - 把业务尽量隔开, 尽量避免交叉

事务隔离级别
- read uncommitted
  - 很少使用
  - 不能保持一致性. 脏读, 不可重复读, 幻读
- read committed(常用数据库隔离级别)
  - 及支持基于行的bin-log
  - update优化: 半一致读(semi-consistent read))
- repeatable read(MySQL 默认级别, 历史的包袱)
  - 锁特点
    - 使用唯一索引的唯一查询条件时, 只锁定查找到的索引记录, 不锁定间隙
    - 其他查询条件, 会锁定扫描到的索引范围, 通过间隙锁或临键锁来阻止其他会话在这个范围中插入值
    - 可能的问题: InnoDB不能保证没有幻读, 需要加锁.
- 可串行化: serializable

MySQL事务隔离特点. 每个事务启动后都会有一个事务ID. 
- 设置全局隔离级别
- 设置会话隔离级别: 一个会话可能开启多个
- InnoDB与标准有差异

`show engine innodb status` 可以驱动器有哪些事务哪些锁

|   | X | IX | S | IS |
| -- | -- | -- | -- | -- |
| X | 冲突| 冲突 | 冲突 | 冲突 |
| IX | 冲突 | 兼容 | 冲突 | 兼容 | 
| S | 冲突| 冲突| 兼容| 兼容 |
| IS | 冲突| 兼容| 兼容 | 兼容 |

undo log: 撤销日志
- 保证事务的原子性
- 用处: 事务回滚, 一致性读, 崩溃恢复
- 记录事务回滚时所需的撤销操作
- 一条INSERT 语句, 对应一条delete的undo log
- 每个update 语句, 对应一条相反地update的undo log
保存位置:
- system tablespace(MySQL5.7 默认)
- undo tablespaces(MySLQ 8.0 默认)
回滚段(rollback segment)

redo log: 重做日志
- 确保事务的持久性, 防止事务提交后数据未刷新到磁盘就断点或崩溃
- 事务执行过程中写入 redo log, 记录事务对数据页做了哪些修改
- 提升性能WAL(Write-Ahead Logging)技术, 先写日志, 在写磁盘
- 日志文件: ib_logfile0, ib_logfile1
- 日志缓冲: innodb_log_buffer_size
- 强刷: fsync()

MVCC 实现机制: 只要用于解决可重复读问题
- 隐藏列: innodb在每个表的每行列都添加了隐藏列
| 隐藏列|DB_TRX_ID|DB_ROLL_PTR|DB_ROW_ID|
| --- | --- | --- | --- |
|长度|6-byte|6-byte| 6-byte |
|说明|指示最后插入或更新该行的事务ID.保证每个事务只看到自己所能看到的数据 | 回滚指针.指向回滚段中写入的undo log 记录. 查看旧版本的数据 | 聚簇row ID/ 聚簇索引 |
- 事务链表, 保证还未提交的事务, 事务提交则会从链表中摘除
- Read view: 每个SQL一个, 包括 rw_trx_ids, low_limit_id, up_limit_id, low_limit_no等
- 回滚段: 通过undo log 动态构建旧版本数据

演示事务和锁
mysql 8.0 可以通过performance_schema.data_locks 表直接查看锁的情况

存储引擎选择:
- InnoDB: 默认. 没有其他原因就使用这个.
- ToTuDB: 归档库
  - 高压缩比, 使用压缩和归档
  - 在线添加索引, 不影响归档
  - 支持完整的ACID特性和事务机制


### 2.DB与SQL优化 58:55

- 第一小节
  - `order by` 后面可以添加函数的`if(columnCondtion, trueResult, falseResult)`.
  - 设计表前可以阅读DBA指导手册, dbaprinciples
- 第二小节
  - 制定正确的数据引擎和数据类型
  - 隐式转换不支持索引
- 第三小节
  - 查看数据库问题:
    - 慢查询日志: 数据库自带的记录日志. 需要开启`slow_query_log`
    - 看运维和应用监控
  - 解决问题
    - 添加索引
      - 索引方式:
        - Hash 类型: 比较适合放在内存中.
        - B+Tree: 比较适合磁盘中创建索引. 数据只放在底部的叶子节点. 主键和指针放一起. 所有叶子节点块都使用双向指针指向. 
        - B-Tree:比较适合磁盘中创建索引. 数据和指针, 主键放在一起. 
    - 主要需要单调递增的是为了放置页分裂的问题. "页分裂" 就是根据序号排列在已存在数据的数据页中插入新的数据. 
  - 问题思考
    - 为什么不使用hash index
    - 为什么B+tree更适合做索引
    - 为什么主键长度不能过大. 
  - 主键和索引谁更快: 主键是聚集索引, 叶子节点处主键和数据是放在一起. 索引时二级索引, 索引和主键放在一起. 根据索引找到主键, 在根据主键找到数据.
  - 某个字段值的重复程度, 去掉重复后的值/字段总数=字段选择性. 字段选择性越高, 查询条件或索引字段越往左靠. 
  - 索引冗余
    - 长的索引包括短的索引, 短索引就会是冗余的.
    - 有唯一约束的, 和其他字段形成索引是冗余的.
  - 修改表结构的危害: 一般在用户量比较小的时候做. 或者某个维护的时候做. 
    - 添加索引后, 索引重建会很花时间. 重建索引时会锁表. 
    - 修改表, 花费大量系统资源.
    - 修改表时, 主从也会造成延迟. 
  - 数据量:
    1. 业务初期考虑不周, 字段类型使用不合理, 需要变更数据类型
    2. 随着业务发展, 需要增加新的字段. 最好添加从表, 从表和主表主键关联
    3. 在无索引字段增加新的业务查询, 需要增加索引. 
  - 大批量写入的优化: 
    - PreparedStatement 减少sql 解析
    - Multiple Values/ Add Batch 减少交互. 大sql 不能超过max packet. JDBC会自动将多条ps 组合为批量插入发送到数据库.
    - 批量使用cvs文件保存命令. 使用Load Data 直接导入
    - 索引和约束问题. 批量导入和先将约束和索引去掉加快导入速度. 再重建约束和索引.
  - 数据更新  
    - 数据范围更新
      1. 注意GAP Lock的问题. 可以先写一个select 查询出指定的记录. 再使用in 缩小范围. 而不是使用'>' 和'<' 确定法范围
      2. 导致锁范围扩大
  - 模糊查询
    1. Like的问题: like 默认在索引处只走前缀的问题. name like 'l%'. 如果是'%l' 就走不了索引了
    2. 使用全文索引, 而不是like 查遍表中所有数据
    3. solr/ES(ElasticSearch)
  - 连接查询
    1. 驱动表越小, 越明确
    2. 避免笛卡尔积
  - 索引失效
    - 刚才提到的like
    - not in, 函数走不了索引. 时间类型会调用相关的函数处理, 时间类型尽量使用long 类型
    - 减少使用or, or跟前面精确条件没有关系. 使用union(去重), union all
    - 大数据量下, 放弃所有条件组合都走索引的幻想, 使用"全文检索"
    - 必要时可以使用force index 来强制走某个索引
  - 查询SQL到低怎么设计
    1. 连表可以拆解成两个sql 查询.
    2. 避免不必须得大量重复数据传输. 一些计算可以放在数据库
    3. 避免使用临时文件排序或临时表. sort 查询时, 可能缓存空间不够, 可能会将查询结果放在文件中, 这样会非常慢.
    4. 分析类需求, 可以用汇总表: 把每年计算一次的结果分为每天计算.

### 3.常见场景分析 26:40

怎么实现主键ID
- 自增: 很方便, 不具有全局唯一性. 不支持分库分表. 
- sequence. oracle支持, mysql 不支持.
- 模拟seq: 在一个表中保存字段名称, 下一个数字, 步进长度. 所有表都通过这个表获得主键. 设置步长是为了避免获取每个数字都要select和update, 每个连接使用一段, 使用完再取.
- 使用前三者作为id, 可能被商业对手查到自己的业务规模.
- UUID: 一串随机数字. 没有顺序.随机. 最长128位. 平时可以取16位.
- 时间戳/随机数: 使用时间戳再加上几个随机数. 但不一定确保多个机器获取的id是不同的.
- snowflow: 及其号码+时间戳+自增数 可以确保唯一性和有序性.

高效分页:
- 分页: count/pageSize/pageNum: 条件查询
- 分页插件. 如mybatis-plus. 使用sql 嵌套一个select count 子句. 性能很差. 原理是`select count(1) from otherSelectWhereQuery`. 
- 改进方式1. 重写count. 获取记录的数量的查询可能不用那么多表. 可以重写count 后form子句.
- 大数量级分页的问题, limit 100000, 20;
  - 使用反序. 原来是正向获取 100000 开始的20条数据. 可以使用反向排序后, 从0开始的20条数据 order by desc limit 0 20
- 改进方式3. 技术向: 每次请求时自带开始的id. 然后可以避免一次count 查询
  - 需求向: 可能在15页挑战到20页. 可以忍受这期间的更新的不确定性. 可以返回15页的id. 计算到20页的id
- 需要在大量数据中精确的条件组合. 可以使用solar或者ES.

乐观锁与悲观锁

乐观锁:
``` sql
-- 悲观锁. 这个时候xxx表被锁定, 不能再被其他事务读取和设置. 不能利用多核的优势
select * from xxx for update;
update xxx
commit;

-- 乐观锁: 进行一次查询, 不影响其他事务的访问. 在读写量不是特别大的情况下可以增加读写效率.
select * from xxx;
-- 根据jdbc 返回的有修改的行的数量确定是否更新成功.
update xxx where value =xxx;
```

### 4.从单机到集群/MySQL主从复制 1:02:38

#### 从单机到集群

- 读写压力--多机集群--主从复制
- 高可用性--故障转移--主从切换
- 容量问题--数据库拆分--分库分表
- 一致性问题--分布式事务--XA/柔性问题

#### MySQL主从复制

- 主库写bin log
- 从库写relay log

MySQL 发展
- 2000年，MySQL 3.23.15版本引入了复制. MySQL Asynchronous Replication
- 2002年，MySQL 4.0.2版本分离 IO 和 SQL 线程，引入了relay log
- 2010年，MySQL 5.5版本引入半同步复制
  - 需要启动插件. 
  - Semisynchronous replication
  - 主库写入bin log 后, 将记录发送给从库, 从库写完relay log. 某个从库返回给主库写入成功, 主库提交. 如果从库执行实践过长, 主库就退化为传统主从复制.
- 2016年，MySQL 在5.7.17中引入 (MGR MySQL Group Replication 组复制). 多个节点看成对等的节点, 都可以看做主库. 基于Paxos 实现分布式协议.  在数据库完成事务前添加了一个验证阶段, 让先拿到事务或锁的数据库执行, 其他库将binlog 转换成relay log 再进行回放.

binlog 格式:
- ROW: 信息比较详细, 占用空间比较大
- Statement: 只记录了一些操作的SQL.
- Mixed: 数据库根据当时条件自己选择记录方式

`mysqlbinlog -vv binFileName`: 查看binlog.

主从复制的局限性
1. 主从延迟问题
2. 应用侧需要配合读写分离框架
3. 不解决高可用问题

在数据库安装过后, 通过命令可以直接将从数据设置备份数据库.

### 5.MySQL读写分离与高可用 1:09:36

#### MySQL 读写分离
- 读写分离-动态切换数据源版本1.0
  - 配置多个数据库, 实现读写分离
  - Spring/Spring boot 配置多个数据源
  - 根据具体service方法是否会操作数据, 注入不同数据源
    - 基于操作AbstractRoutingDataSource和自定义注解readOnly, 简化自动切换数据源
    - 支持配置多个从库
    - 支持多个从库的负载均衡 
- 读写分离-数据库框架版本2.0
  - 前一个设置的问题
    - 侵入性比较强
    - 将低侵入性会导致"写完读"不一致问题. 写完读: 在主数据库写数据, 在同一事务直接在从库中刚写的数据. 可能主从数据库不一致.
  - 改进方式, ShardingSphere-jdbc的Master-Slave功能
    - SQL解析和事务管理, 自动实现读写分离
    - 解决"写完读"不一致问题. TDL 这个问题解决的比较好. shardingSphere早期版本有问题.
- 读写分离-数据库中间件版本3.0
  - 前一版本的问题
    - 对业务系统还是有侵入
    - 对已存在的旧系统改造不友好
  - 改进方式, MyCat/ShardingShpere-Proxy的Master-Slave功能
    - 需要部署一个中间件，规则配置在中间件
    - 模拟一个 MySQL 服务器，对业务系统无侵入

#### MySQL高可用
- 为什么要高可用
  - 读写分离, 提升读的处理能力
  - 故障转移, 提供failover能力
    - failover(故障转移, 灾难恢复)
- 容灾:
  - 热备: 服务器都可以提供服务, 其中一个出现问题, 其他服务器将压力平摊.
  - 冷备: 有机器不工作在等其他机器出问题后自动启动.
- 常见的一些策略
  - 多个实例不再一个主机/机架上
  - 跨机房和可用区部署
  - 两地三中心(一个地方两个中心, 异地一个中心)容灾高可用方案.
- 高可用定义
  - SLA(服务水平协议)

高可用方案:
- 高可用0: 主从手动切换. 出现问题, 手工修改配置.
  - 可能的问题
    - **可能数据不一致**
    - 需要人工干预
    - 代码和配置的侵入性
-  高可用1: 主动手动切换
   - 手动处理主从切换
- 高可用2: MHA
   - MHA(Master High Availability)
- 高可用3:MGR
  - MGR 优点
    - 高容错性, 防止脑裂. 脑裂:如果有5个节点, 有三个节点联系不到, 剩下的两个节点不知道那三个节点是不能提供服务了还是只是自己联系不到了. 如果剩下两个节点不停止工作, 那么这两个节点和断开的三个节点数据可能造成不一致. 
- 高可用4: MySQL Cluster
- 高可用5: Orchestrator

### 作业
1.（选做）用今天课上学习的知识，分析自己系统的 SQL 和表结构
2.（必做）按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率
> 1. 普通insert
> 2. 共享一个事务的insert
> 3. addBatch(相关参数))
> 4. 关于rewriteBachedStatements=true 的说明
> 5. 使用cvs 插入
  - so1.log
3.（选做）按自己设计的表结构，插入 1000 万订单模拟数据，测试不同方式的插入效
4.（选做）使用不同的索引或组合，测试不同方式查询效率
5.（选做）调整测试数据，使得数据尽量均匀，模拟 1 年时间内的交易，计算一年的销售报表：销售总额，订单数，客单价，每月销售量，前十的商品等等（可以自己设计更多指标）
6.（选做）尝试自己做一个 ID 生成器（可以模拟 Seq 或 Snowflake）
7.（选做）尝试实现或改造一个非精确分页的程序
8.（选做）配置一遍异步复制，半同步复制、组复制
9.（必做）读写分离 - 动态切换数据源版本 1.0
> 通过不同的连接池注入, 
> 1. 使用两个连接池, 一个用于保存, 一个用于读取
> 2. 使用abstractRoutingDataSource
>   无注释
>   插入
>   获取
10.（必做）读写分离 - 数据库框架版本 2.0
> 3.shardingShpere-proxy 连接数据库. 
>   有主键插入
>   无主键插入
>   查询
11.（选做）读写分离 - 数据库中间件版本 3.0
12.（选做）配置 MHA，模拟 master 宕机
13.（选做）配置 MGR，模拟 master 宕机
14.（选做）配置 Orchestrator，模拟 master 宕机，演练 UI 调整拓扑结构



## 第八周:超越分库分表/分布式服务

### 1.数据库垂直拆分 39:17

- 读写压力--多机集群--主从复制
- 高可用性--故障转移--主从切换
- 容量问题--数据库拆分--分库分表
- 一致性问题--分布式事务--XA/柔性问题

### 2.数据库水平拆分 44:50

work-id: 不同机器配置不同的值.
### 3.相关的框架和中间件以及如何做数据迁移 28:51

#### 框架和中间件

- Consistency(一致性): All clients see the same data at the same time. mysql, MariaDB 不支持多个数据库一起的一致性.
- Availability(可用性): The system continuse to operate even in the presence of node failures
- Partition-Tolerance(分区容忍性): The systeem continuses to operate in spite of network failures. 分区之间不能再一起工作.

原来随着业务量和数据量的上涨, 硬件能力也符合摩尔定律的上涨. 现在硬件不能完成摩尔定理. 为了处理更大的业务量和数据分布式崛起.

#### 如何做数据迁移

- 全量迁移:全量数据导出和导入
1. 业务系统停机
2. 数据库迁移, 校验一致性
3. 业务系统升级, 接入新数据库
- 全量+增量

### 4.分布式事务以及XA分布式事务 47:35

#### 分布式事务

为了完成多个模块或者硬件的数据一致性, 需要分布式事务. 实现分布式事务有两种方式:
- 强一致性: XA协议
- 弱一致性: 
  - 不用事务, 业务侧补偿冲正
  - 所谓的柔性事务, 使用一套事务框架保证最终一致的事务.
  - 可以容忍一段时间的不一致性, 最终通过超时终止, 调度补偿等方式保证数据的最红一致性.

#### XA分布式事务

DTP(Distributed Transaction Process DTP 分布式事务处理). X/Open(现在的Open group) 定义了分布式事务处理模型(Distributed Transaction Processing Reference Model). 事务管理器与资源管理器通过XA接口规范, 使用两阶段提交来完成一个全局事务. XA 包括两套函数, xa_开头和ax_开头.

DTP 三个角色:
- 应用程序(Application Program AP): 用于定义事务边界(即定义事务的开始和结束), 并在是无边界内对资源进行操作.
- 资源管理器(Resource Manager RM): 如数据库, 文件系统等, 并提供访问资源的方式
- 事务管理器(Transaction Manager TM): 负责分配唯一事务标识, 监控事务的执行速度, 并负责事务的提交, 回滚等.

XA分布式事务协议中方法调用函数(C 函数)
- ax_reg: Register an RM with a TM
- ax_unreg: Unregister an RM with a TM.
- xa_close: Terminate the AP's use of an RM.
- xa_commit: Tell the RM to commit a transaction branch
- xa_complete: Test an asynchronous XA operation for completion
- xa_forget: Permit the RM to discard its knowledge of a heuristically-completed transaction branch. 允许RM 放弃探索性完成事务分支的状态.
- xa_open: initialise an RM for use by an AP. 初始化一个RM供AP使用
- xa_recover: get a list of XIDs the RM has prepared or heuristically completed. 返回一个RM已经准备好的XID或者探索性完成的列表.
- xa_rollback: Tell the RM to roll back a transaction branch. 告诉RM 回滚事务分支
- xa_start: Start or resume a transaction branch-associate an XID with future work that the thread requests of the RM. 开始或者重新开始一个事务分支. 关联一个XID和RM将要工作的一个线程请求.
- xa_end: Dissociate the thread from a transaction branch. 断开线程和事务分支. 不是线程完成. 是告诉RM 所有sql语句已经提交完毕.
- xa_prepare: ask the RM to prepare to commit a transaction branch. 询问RM是否准备好提交一个事务分支.

> 为什么XA事务叫做两阶段事务: XA第一个阶段时开始在RM分配xid, 并且启动事务. 所有RM执行过sql语句后, 第一阶段完成. TM开始提交或回滚数据.

`show engines`: 查看存储引擎是否支持xa

mysql 操作数据库语句
- `XA {start|begin} xid [JOIN|RESUME]`: 开启XA事务, start命令不支持[join|resume], xid 是唯一值, 表示事务分支标志.
- `xa end xid [suspend [for migrate]`:结束一个xa事务, 可以使用`suspend [for migrate]` 但它没有实际效果
- `xa commit xid [One phase]`: 如果使用one phase, 则表示使用一阶段提交. 两阶段提交协议中, 如果只有一个RM参与, 可以优化为一个阶段提交.
- `XA rollback xid`
- `xa recover [CONVERT XID]`: 列出所有处于Prepare阶段的xa事务

XA事务和非XA事务(本地事务) 是互斥的. 
思考一个问题：XA 过程中，事务失败怎么办？
1、业务 SQL 执行过程，某个 RM 崩溃怎么处理？
> 整个事务回滚掉.
2、全部 prepare 后，某个 RM 崩溃怎么处理？
> 全体rollback.
3、commit 时，某个 RM 崩溃怎么办？
> 记录那些成功, 哪些失败. 对失败的RM重新发送. 执行某种机制让失败的RM重新启动, 气虚发送请求. 必须成功.

XA并不直接改变当前数据库的隔离级别. 

不需要新建数据库.
xa start 'java1';
xa end 'java1;
xa prepare 'java1';
xa recover;
xa rollback
xa commit 'java1';


### 5.BASE柔性事务 12:07

本地事务->XA(2PC)->BASE

BASE 是下面三个要素的缩写
- 基本可用(Basically Available) 保证分布式事务参与方不一定同时存在
- 柔性状态(Soft state) 允许系统状态更新有一定延时, 这个延时对客户来说不一定察觉.
- 最终一致性(Eventually consistent): 通过消息传递的方式保证了最终一致性.

### 5.TCC/AT 以及相关框架 41:41

### 作业

1.（选做）分析前面作业设计的表，是否可以做垂直拆分。
2.（必做）设计对前面的订单表数据进行水平分库分表，拆分 2 个库，每个库 16 张表。并在新结构在演示常见的增删改查操作。代码、sql 和配置文件，上传到 Github。
> 调整sql数据表
> 调整mybatis generator
> 生成dao 专用的模块
> 依赖dao 配置ShardingShpere proxy.
> 配置常见增删改查操作
>   增加: 插入需要自动生成id的数据
>     插入包含user_id为0, 1, order_id 为0-31 数据
>   查询
>   修改: 把user_id 为1的记录修改为2, user_id为0的修改为3
>   查询
>   删除: 按照order_id=0, user_id 为0, 1 的情况删除
>     按照order_id 其他形式删除.

3.（选做）模拟 1000 万的订单单表数据，迁移到上面作业 2 的分库分表中。
4.（选做）重新搭建一套 4 个库各 64 个表的分库分表，将作业 2 中的数据迁移到新分库。

5.（选做）列举常见的分布式事务，简单分析其使用场景和优缺点。
6.（必做）基于 hmily TCC 或 ShardingSphere 的 Atomikos XA 实现一个简单的分布式事务应用 demo（二选一），提交到 Github。
7.（选做）基于 ShardingSphere narayana XA 实现一个简单的分布式事务 demo。
8.（选做）基于 seata 框架实现 TCC 或 AT 模式的分布式事务 demo。
9.（选做☆）设计实现一个简单的 XA 分布式事务框架 demo，只需要能管理和调用 2 个 MySQL 的本地事务即可，不需要考虑全局事务的持久化和恢复、高可用等。
10.（选做☆）设计实现一个 TCC 分布式事务框架的简单 Demo，需要实现事务管理器，不需要实现全局事务的持久化和恢复、高可用等。
11.（选做☆）设计实现一个 AT 分布式事务框架的简单 Demo，仅需要支持根据主键 id 进行的单个删改操作的 SQL 或插入操作的事务。

## 第九周:分布式服务

### 1.RPC基本原理及技术框架 31:55

#### RPC基本原理

RPC(Remove Procedure Call 远程过程调用)

RPC 和MQ 是整个分布式技术的基石.
- fx(framework)

RPC 简化版原理(核心是代理机制):
1. 本地代理存根:Stub
2. 本地序列化反序列化
3. 网络通信
4. 远程序列化反序列化
5. 远程服务存根: Skeleton
6. 调用实际业务服务
7. 

- RPC原理-共享信息
  1. 共享信息: POJO实体类定义, 接口定义
  2. REST 时使用WADL, WebService 使用WSDL, PB 使用IDL(Interface definition language)
  3. REST 也可以使用接口文档.
  4. rpcfx 的api子项目
- RPC原理-代理
  1. Java下, 选择动态代理. 或者AOP实现
  2. C# 直接有远程代理
  3. Flex使用动态方法和属性
  4. rpcfx 默认使用动态代理
- RPC原理-序列化
  1. 语言原生序列化, RMI, Remoting
  2. 二进制平台无关, Hessian, avro, kyro, fst等
  3. 文本, JSON, XML等
  4. rpcfx 里默认使用json
- RPC原理-网络传输
  1. TCP/SSL/TLS
  2. HTTP/HTTPS
  3. rpcfx 里默认使用HTTP
- RPC原理-查找实现类
  1. 通过接口查找具体的业务服务实现
  2. rpcfx 里的默认使用Spring getBean.
- RPC原理-异常处理

远程:服务提供者, 本地:服务消费者

#### 技术框架

远古时的技术尝试:
- Corba（Common ObjectRequest Broker Architecture）公共对象请求代理体系结构，OMG 组织在1991年提出的公用对象请求代理程序结构的技术规范。底层结构是基于面向对象模型的，由OMG 接口描述语言（OMG Interface Definition Language，OMG IDL）、对象请求代理（Objec tRequest Broker，ORB）和IIOP 标准协议（Internet Inter ORB Protocol，也称网络ORB交换协议）3个关键模块组成。
- COM（Component Object Model，组件对象模型）是微软公司于1993年提出的一种组件技术，它是一种平台无关、语言中立、位置透明、支持网络的中间件技术。很多老一辈程序员心目中的神书《COM 本质论》。

常见的RPC技术
- Corba/RMI/.NET Remoting
- JSON RPC, XML RPC(是ISO 定义的两种规范)，WebService(Axis2, CXF)
- 基于二进制的RPC框架
  - Hessian: 基于HTTP协议. 就叫hessian序列化
  - Thrift, facebook 开源的序列化技术. 使用二进制TCP的方式实现. 早期一个server是一个进程, 一个进程绑定一个端口. 这样可以通过端口指定服务. 后面进行升级. 一个server 可以指定多个端口.
  - Protocol Buffer(PB), 
  - gRPC: google 创建开源的RPC标准. 是云原生环境下的RPC技术的标准. 很多定义是从PB借鉴来的.

### 2.如何设计一个RPC 25:08


#### 从RPC到分布式服务化



### 3.Duboo框架介绍以及技术原理 59:04

#### Dubbo框架介绍

HSF(High-speed Server Firework)

Apache Dubbo 是一款高性能, 轻量级的开源Java服务框架. 六大核心能力: 面向接口代理的高性能RPC调用, 高度可扩展能力, 服务自动注册和发现, 智能负载均衡,  运行期流量调度, 可视化服务治理与运维.

Dubbo的主要功能
- 基础功能: RPC调用
  - 多协议(序列化, 传输, RPC)
  - 服务注册发现
  - 配置, 元数据管理
- 扩展功能: 集群, 高可用, 管控
  - 集群, 负载均衡
  - 治理, 路由
  - 控制台, 管理与监控 

#### Dubbo 技术原理

整体框架:
1. config配置层: 对外配置接口, 以ServiceConfig, ReferenceConfig为中心, 可以直接初始化配置类, 也可以通过spring解析配置生成的配置类
2. proxy服务代理层: 服务接口透明代理, 生成服务的客户端stub和服务端skeleton, 以serviceProxy 为中心, 扩展接口为ProxyFactory
3. registry 注册中心层: 封装服务地址的注册与发现, 以服务URL为中心, 扩展接口为RegistryFactory, Registry, RegistryService
4. cluster 路由器: 封装多个提供者的路由及负载均衡, 并桥接注册中心, 以Invoker为中心, 扩展接口为Cluster, Directory, Router, LoadBalance.
5. monitor架空层: RPC调用次数和调用时间监控, 以Statistics, 扩展接口为MonitorFactory, Monitor, MonitorService
6. Protocal 远程调用层: 封装RPC调用, 以Invocation, Result为中心, 扩展接口为Protocol, Invoker, Exporter
7. exchange信息交换层: 封装请求响应模式, 同步转异步, 以Request, Response为中心, 扩展接口为Exchanger, ExchangeChannel, ExchangeClient, ExchangeServer
8. transport 网络传输层: 抽象mina和netty 为统一接口, 以Message为中心, 扩展接口为Channel, Transporter, Client, Server, Codec
9. serialize 数据序列化层: 可复用的一些工具, 扩展接口为Serialization, ObjectInput, ObjectOutput, ThreadPool.

### 4.Dubbo应用场景以及最佳实践 39:31

#### Dubbo 应用场景

- 分布式服务化改造:
- 开放平台: SOA面向服务架构. EAI企业应用集成. ESB企业服务总线.
  - 开放模式
  - 容器模式
-  作为前端使用的后端(BFF Backend for Frontend)
-  通过Dubbo实现业务中台.
-  通过服务化建设中台.

#### Dubbo最佳实践

开发分包:
- 服务接口尽可能大力度.

roaring bitmap

#### 如何看Dubbo源码

核心重点模块:
- common
- config
- filter
- rpc/remoting/serialization
集群与分布式
- cluster
- registry/configcenter/metadata

### 作业

1.（选做）实现简单的 Protocol Buffer/Thrift/gRPC(选任一个) 远程调用 demo。
2.（选做）实现简单的 WebService-Axis2/CXF 远程调用 demo。
3.（必做）改造自定义 RPC 的程序，提交到 GitHub：
  - 尝试将服务端写死查找接口实现类变成泛型和反射；
  - 尝试将客户端动态代理改成 AOP，添加异常处理；
  > 使用byteBuddy 实现代理. 
  >   原来的想法: 为接口生成代理类, 并且所有方法使用相同的方法体.
  >     byteBuddy 有类似于动态代理的方法. 没有使用
  >     byteBuddy 修改每个方法时, 需要定义为所有方法生生成的代理类指定方法.
  >     方法有返回值时还可以正常使用, 添加注解后不能再用. 需要获取方法名称时需要使用注解. 
  > 在接口, 方法添加异常抛出. 客户端处理接收到的异常
  - 尝试使用 Netty+HTTP 作为 client 端传输方式。
  > 在byteBuddy的方法体中实现netty 访问.

4.（选做☆☆））升级自定义 RPC 的程序：

尝试使用压测并分析优化 RPC 性能；
尝试使用 Netty+TCP 作为两端传输方式；
尝试自定义二进制序列化；
尝试压测改进后的 RPC 并分析优化，有问题欢迎群里讨论；
尝试将 fastjson 改成 xstream；
尝试使用字节码生成方式代替服务端反射。
5.（选做）按课程第二部分练习各个技术点的应用。
6.（选做）按 dubbo-samples 项目的各个 demo 学习具体功能使用。
7.（必做）结合 dubbo+hmily，实现一个 TCC 外汇交易处理，代码提交到 GitHub:

用户 A 的美元账户和人民币账户都在 A 库，使用 1 美元兑换 7 人民币 ;
用户 B 的美元账户和人民币账户都在 B 库，使用 7 人民币兑换 1 美元 ;
设计账户表，冻结资产表，实现上述两个本地事务的分布式事务。
A 人民币->B美元
B 美元->A人民币
A在bank01, B在bank02.
A人民币转换为B美元. 
  A人民币账户减少
  B美元账户增多.
  
8.（挑战☆☆）尝试扩展 Dubbo

基于上次作业的自定义序列化，实现 Dubbo 的序列化扩展 ;
基于上次作业的自定义 RPC，实现 Dubbo 的 RPC 扩展 ;
在 Dubbo 的 filter 机制上，实现 REST 权限控制，可参考 dubbox;
实现一个自定义 Dubbo 的 Cluster/Loadbalance 扩展，如果一分钟内调用某个服务 / 提供者超过 10 次，则拒绝提供服务直到下一分钟 ;
整合 Dubbo+Sentinel，实现限流功能 ;
整合 Dubbo 与 Skywalking，实现全链路性能监控。

## 第十周: 分布式服务

### 1.分布式服务治理/配置/注册/元数据中心 42:09

#### 分布式服务治理

#### 配置/注册/元数据路中心

### 2. 服务的注册与发现/集群与路由/过滤与流控 36:37

#### 服务的注册与发现

#### 服务的集群与路由

#### 服务的过滤和流控

### 3.微服务框架发展历程与应用场景 45:19

#### 微服务架构发展历程

#### 微服务架构应用场景

### 4.SpringCloud 技术体系/微服务相关架构与工具 29:44

#### Spring Cloud技术体系

#### 微服务相关框架与工具

### 5.微服务架构最佳实践 39:37

### 作业
1.（选做）rpcfx1.1: 给自定义 RPC 实现简单的分组 (group) 和版本 (version)。
2.（选做）rpcfx2.0: 给自定义 RPC 实现：

基于 zookeeper 的注册中心，消费者和生产者可以根据注册中心查找可用服务进行调用 (直接选择列表里的最后一个)。
当有生产者启动或者下线时，通过 zookeeper 通知并更新各个消费者，使得各个消费者可以调用新生产者或者不调用下线生产者。
3.（挑战☆）在 2.0 的基础上继续增强 rpcfx 实现：

3.0: 实现基于 zookeeper 的配置中心，消费者和生产者可以根据配置中心配置参数（分组，版本，线程池大小等）。
3.1：实现基于 zookeeper 的元数据中心，将服务描述元数据保存到元数据中心。
3.2：实现基于 etcd/nacos/apollo 等基座的配置 / 注册 / 元数据中心。
4.（挑战☆☆）在 3.2 的基础上继续增强 rpcfx 实现：

4.0：实现基于 tag 的简单路由；
4.1：实现基于 Random/RoundRobbin 的负载均衡 ;
4.2：实现基于 IP 黑名单的简单流控；
4.3：完善 RPC 框架里的超时处理，增加重试参数；
5.（挑战☆☆☆）在 4.3 的基础上继续增强 rpcfx 实现：

5.0：实现利用 HTTP 头跨进程传递 Context 参数（隐式传参）；
5.1：实现消费端 mock 一个指定对象的功能（Mock 功能）；
5.2：实现消费端可以通过一个泛化接口调用不同服务（泛化调用）；
5.3：实现基于 Weight/ConsistentHash 的负载均衡 ;
5.4：实现基于单位时间调用次数的流控，可以基于令牌桶等算法；
6.（挑战☆☆☆☆）：压测，并分析调优 5.4 版本。

7.（选做）进度快的，把前几次课的选做题做做。
8.（选做）进度慢的，把前几次课的必做题做做。
9.（选做）学霸和课代表，，考虑多做做挑战题:

10.（挑战☆☆）对于不断努力前行的少年：

把你对技术架构演进的认识，做一个总结。
把你对微服务的特点，能解决什么问题，适用于什么场景，总结一下。
画一个脑图，总结你能想到的微服务相关技术框架和中间件，想想都有什么作用。
思考（☆☆）：微服务架构是否能应用到你最近接触或负责的业务系统，如何引入和应用，困难点在什么地方。
研究（☆☆☆）：学习和了解 Spring Cloud 体系，特别是 Netflix 和 Alibaba 套件，画出他们的体系结构图。

## 第十一周: 分布式缓存

### 1.本地缓存/远程缓存 42:45

#### 从数据的使用说起

- 静态数据:
- 准静态数据: 变化评率很低.
- 中间状态数据: 计算的可服用的中间数据, 变量副本, 配置中心的本地副本.

适合缓存的特点:
- 热数据: 使用频率高
- 读写比,较大: 读的频率>>写的数据

缓存加载时机
1. 启动全量加载: 全局有效, 使用简单.
2. 懒加载
   1. 同步使用加载
      1. 先看缓存是否有数据, 没有的话从数据库读取
      2. 读取的数据, 先放到内存, 然后返回给调用方
   2. 

如何评价缓存的有效性?
- 读写比: 读数据次数和写数据次数比例. 一般大于10:1 是才用. 
- 命中率: 命中缓存意味着缓存数据被使用, 意为者有价值. 90%+. 所有对数据的访问有90% 使用了缓存.

#### 本地缓存

Spring Cache 是Cache使用的API. 底层仍然使用EHcache等.
[详细介绍Spring Cache](https://developer.ibm.com/zh/articles/os-cn-spring-cache/)

#### 远程缓存

edis 官网：https://redis.io/
Redis 在线测试：http://try.redis.io/
Redis 命令参考：http://doc.redisfans.com/
《Redis 设计与实现》：http://redisbook.com/
Memcached 官网：https://memcached.org/

### 2.缓存策略/缓存常见问题 25:42

#### 缓存策略

- FIFO: 
- LRU(least recently used 最近最少使用): 最主流, 最常见的淘汰算法
- 按固定时间过期
- 按业务时间加权: 例如3+5x. 查询某个时间段的机票信息缓存保存时间. x单位是天. 结果单位是分钟. 今天结果缓存的是3+5\*0分钟. 明天的是3+5\*1分钟. 一次类推. 这么做是因为大部分人查的是今天的票, 更新时间比较快, 所以缓存时间比较短. 后面依次类推. 根据航线的热门程度确认保存时间. 脱离具体场景谈性能都是耍流氓.

#### 缓存常见问题

### 3.Redis基本功能 34:17

Redis 安装:
- 下载安装, 编译源码安装(windows:微软提供3.x/Memurai 提供5.x)
- brew, apt, yum 安装
- Dock而方式启动
  - `docker pull redis` 从镜像仓库获取最新版本的redis
    - `docker pull [选项] [Docker Registry 地址[:端口号]/]仓库名[:标签]`
  - `docker run -itd --name redis-test -p 6379:6379 redis` 创建一个新的容器
    - i 使用交互方式运行
    - t 生成一个客户端
    - d 后台运行容器, 并返回一个容器id
    - --name 为容器指定一个名称
    - p 指定端口映射, 格式为`主机(宿主)端口:容器端口`
  - `docker image inspect redis:latest|grep -i version` 查看docker映像
  - `docker exec -it redis-test /bin/bash` 在运行的容器中执行一个命令
  - `docker run -p 6379:6379 --name redis01 -v /etc/redis/redis.conf:/etc/redis/redis.conf -v /etc/redis/data:/data -d redis redis-server /etc/redis/redis.conf --appendonly yes` 
    - redis-server /etc/redis/redis.conf 已配置文件启动redis, 加载容器的conf文件
    - appendonly yes 开启redis持久化

docker 常用命令:
- `docker images` 查看所有镜像
- `docker ps` 列出所有在运行的容器
  - a 列出所有容器

redis 将保存数据的集合称为key. 数据如果有键值对. 将键称为field, 值为value

Redis 的5中基本数据类型
- 字符串: 三种int, string, byte[]. 它是二进制安全的.可以存储图片等信息. 
  - 操作命令: 
    - set/get/getset/del/exists/append: `set a 1`, `get a`. a 是存放字符串的变量. 1 是存放的值.
    - incr/decr/incrby/decrby: `incr a`
  - 注意事项:
    - 字符串append: 会使用更多内存. append 调用一次会增加一倍的内存
    - 整数共享: 如果能使用整数, 尽量使用整数, redis 会共享整数. 限制了redis淘汰策略+LRU. 
    - 整数精度问题: redis 大概能保存16个精度. 17-18的大整数会丢失精度.
- 散列(hash)
  - 可以看做String key 和String value的map容器.所以该类型非常适合于存储对象的信息. 如果Hash中包含少量字段. 那个该数据字段也只占用很少内存空间
  - 操作命令
    - hset/hget/hmset/hmget/hgetall/hdel/hincrby. `hset h a b` 中h是hash的变量. a 存放的键. b存放的值.
    - hexists/hlen/hkeys
- 列表
  - 类似于java 的linkedList. 内部按照插入顺序排序. 可以再头部或者尾部插入. 当插入时没有list变量, redis 会新建一个. 如果list的变量均被移除. 那么该键也会从数据库中删除.
  - lpush/rpush/lrang/lpop/rpop
- 集合
  - java的set, 不重复的list. 常量时间完成依次操作.
  - 操作命令
    - sadd/srem/smembers/sismember
    - sidff/sinter/sunion
- 有序集合
  - sortedset和set类似. 每个成员都会有一个分数与其关联. redis 通过分数对成员从小到大排序.分数可重复.
  - 操作命令
    - zdd key score member score2 memeber2
    - zscore key member: 返回指定成员的分数
    - zcard key: 获取集合中成员数量
    - zrem key member [memeber]: 移除
    - zrange key start end [withscores]: 获取集合start-end 分数的成员. withscores 参数会返回元素对应的分数
    - zrevrange key start stop[withscores]: 按照分数从大到小的顺序返回start-stop 之间的内容
    - zremrangebyrank key start stop: 按照排名范围删除元素
- Bitmaps
  - 是String类型上的一组面向bit 操作的集合. 由于strings是二进制安全的blob. 最大长度为512M. 所以最大设置2^32不同的bit.
  - 操作命令
    - setbit/getbit/bitop/bitcount/bitpos
- Hyperloglogs
  - 使用标准错误小于1%的估计度量结果. 不需要与需要统计的项相对应的内存. 使用内存的一直恒定不变. 最坏的只有12k. 可以统计接近2^64个不同元素的不相同元素的个数
  - 操作命令
    - pfadd/pfcound/pfmerge
- GEO: 可以将用户给定的地理位置信息存储起来并进行操作
  - geoadd/geohash/geopos/geodist/georadius/georadiusbymemeber
  
Redis到底是单线程还是多线程的:
redis 6之前: 是BIO. 处理io请求时单线程. 处理内存也是单线程. 
redis 6以及之后, 改为NIO. 处理IO请求变为多线程. 处理内存数据的仍然是单线程的.

Redis 架构模型叫做确定性系统. 必须在处理数据模型的时候使用单线程. 这是高性能的核心.

### 4.Redis六大使用场景/Redis的Java客户端 31:42

#### Redis六大使用场景

- 业务数据缓存
  - 通用数据缓存, string, int, list, map等
  - 实时热数据, 最新500条数据
  - 会话缓存, token缓存等
- 业务数据处理
  - 非严格一致性要求的数据: 评论, 点击等
  - 业务数据去重: 订单处理的迷瞪校验等
  - 业务数据排序: 排名, 排行榜等
- 全局一致性
  - 全局流控计数: 多个机器共同使用一个redis技术(下面类似).
  - 秒杀的库存计算
  - 抢红包
  - 全局id生成
- 高效统计计数
  - id去重, 记录访问ip等全局bitmap操作
  - UV, PV等访问量==>非严格一致性要求
- 发布订阅与Stream
  - Pub-Sub模拟队列: redis 一个机器向里面写内容. 一个机器从redis 中读取内容.
    - publish 命令
    - subscribe命令
  - Redis Stream 是Redis5.0 版本新增加的数据结构: Redis Stream 主要用于消息队列(MQ, Message Queue). 可以[参考](https://www.runoob.com/redis/redis-stream.html)
- 分布式锁
  - 获取锁--单个原子性操作: `SET dlock my_random_value NX PX 30000` 
    - `NX` 锁不存在才设置. `PX` 某个线程获取锁后如果不能解锁, 这是设置锁超时时间. 单位毫秒.redis 操作数据的单线程保证的.
  - 释放锁--lua脚本--保证原子性+单线程, 从而具有事务性
```
if redis.call("get", KEYS[1])==ARGV[1]then
  return redis.call("del", KEYS[1])
else
  return 0
end
```

#### Redis的Java客户端

- Redis 官方的Java客户端-Jedis:
  - 类似于JDBC, 可以看做对redis 命令的包装
  - 基于BIO, 线程不安全, 需要配置连接池管理连接.
- Lettuce(推荐)
  - 主流推荐驱动, 基于Netty NIO, API 线程安全.
- Redission
  - 基于Netty NIO, API线程安全
  - 亮点: 大量丰富的分布式功能特性,比如JUC(java.util.currency)的线程安全集合和工具的分布式版本. 分布式基本数据类型和锁.

### 5.Redis与Spring整合/Redis高级功能 47:15

#### Redis 与Spring整合

- Spring Data Redis: 核心是RedisTemplate 可以配置基于Jedis, Letture, Redission. 使用方式类似于JDBC
- Spring Boot与Redis继承
  - 引用spring-boot-starter-data-redis
  - config spring redis
- integrate Spring Cache and Redis
  - 默认使用全局的CacheManager自动集成
  - 默认使用Java的对象序列化, 对象需要实现Serializable. 使用ConcurrentHashMap或Ehcache时, 不需要考虑序列化问题
  - 自定义配置, 可以修改为其它序列化方式.

#### Redis 高级功能

Redis 对事务需求不强. redis 单线程很容易执行事务.

- Redis单机事务. 一般用于单个redis 服务对多个链接时使用.
  - watch: 持续监视一个key是否被修改了, 被修改就抛出错误(不阻挡修改, 执行watch的连接抛出错误)并且事务失败.这是乐观锁.
  - unwatch: 在multi 前对连接进行重置. 不再观察 
  - multi: 开启事务
  - discard: 撤销事务. 在exec 执行前, 对连接进行重置.
  - exec: 提交命令
  - 当需要执行几个操作不被打断时, 使用multi exec 进行事务包装
  - 当需要执行几个操作并且不会被其他链接修改数据时, 需要使用watch, unwatch, discard 进行操作.

加锁的过程就是为某个键设置了value值. 其他进程也可以拿到这个值. 所以其他进程也可以拿到. 
- 支持lua 脚本.

### 作业

1.（选做）按照课程内容，动手验证 Hibernate 和 Mybatis 缓存。

2.（选做）使用 spring 或 guava cache，实现业务数据的查询缓存。

3.（挑战☆）编写代码，模拟缓存穿透，击穿，雪崩。

4.（挑战☆☆）自己动手设计一个简单的 cache，实现过期策略。

5.（选做）命令行下练习操作 Redis 的各种基本数据结构和命令。

6.（选做）分别基于 jedis，RedisTemplate，Lettuce，Redission 实现 redis 基本操作的 demo，可以使用 spring-boot 集成上述工具。

7.（选做）spring 集成练习:

实现 update 方法，配合 @CachePut
实现 delete 方法，配合 @CacheEvict
将示例中的 spring 集成 Lettuce 改成 jedis 或 redisson
8.（必做）基于 Redis 封装分布式数据操作：

在 Java 中实现一个简单的分布式锁；
> 搭建docker, docker 搭建redis.
> 使用Redission 连接. 实现一个锁对数据进行读写

在 Java 中实现一个分布式计数器，模拟减库存。
> 获取还有的库存, 库存减一. 保存剩余库存. 这期间不能被其他线程操作数据.

9.（必做）基于 Redis 的 PubSub 实现订单异步处理

10.（挑战☆）基于其他各类场景，设计并在示例代码中实现简单 demo：

实现分数排名或者排行榜；
实现全局 ID 生成；
基于 Bitmap 实现 id 去重；
基于 HLL 实现点击量计数；
以 redis 作为数据库，模拟使用 lua 脚本实现前面课程的外汇交易事务。
11.（挑战☆☆）升级改造项目：

实现 guava cache 的 spring cache 适配；
替换 jackson 序列化为 fastjson 或者 fst，kryo；
对项目进行分析和性能调优。
12.（挑战☆☆☆）以 redis 作为基础实现上个模块的自定义 rpc 的注册中心。

## 第十二周 分布式缓存

### 1.Redis集群与高并发 01:12:29

我的redis 在wsl 中`/hone/ck/ckbin/redis6.2.4`搭建. 配置文件在`/etc/redis00`, `/etc/redis01`, `/etc/redis-cluster` 中存放.
#### Redis集群与高可用

redis 相关命令
- `slaveof 127.0.0.1 6379`: 将当前服务器配置成6379的从服务器.
- flush 清除redis中数据.
- flushall 清空所有数据
- info 查看redis 信息.

redis.conf 的配置
- `replicaof ::1 6380` 当前redis启动时直接作为6380的从库.
- port 绑定的端口
- pidfile "" pid 文件
- databases 16 数据库个数
- dir "User/kimmking/logs/redis0" 数据文件夹 修改为"/home/ck/logs/redis01"
- io-threads io 线程数
- appendonly no aof的持久化开关
- appendfsync everysec 刷盘的策略.
- dir /home/ck/ckbin/redis-6.2.4/redis6380 创建该文件夹
- logfile /home/ck/ckbin/redis-6.2.4/redis-server6380.log 创建该文件夹. 不设置时在控制台显示日志.
- 注释掉绑定地址 #bind 127.0.0.1
- 去掉requirepass 前面的注释#，在后面添加密码requirepass 111111
- 不以守护进程运行daemonize no

redis server 相关命令:
- `redis-server redis6379.conf` 通过配置文件启动redis
- `redis-cli`  通过默认端口登录redis客户端. 
- `redis-cli -p 6380` 通过 6380 登录redis 客户端.

Redis Sentinel: 
- sentinel 是redis固化的serive 模块. 
- 发现主节点down 时, 就立马拉从节点为主节点. 拉取的算法是raft(阀, 救生艇)
- 启动命令:
  - `redis-sentinel sentinel.conf`: 就是下面命令的别名
  - `redis-server redis.conf --sentinel`
- sentinel 可以通过`redis-cli -p sentinelPort` 进行连接调用.

docker 配置多个redis和Sentinel

sentinel 配置:
- `sentinel monitor mymaster 127.0.0.1 6379 2`: sentinel监视一个名为mymaster ip为127.0.0.1 接口为6379的服务器. 2表示至少两个sentinel统一才会判断主服务器失效. 但只有一个sentinel 获得多个sentinel同意后才会发起自动故障迁移.
- `sentinel down-after-milliseconds mymaster 60000` 主sentinel 在60000毫秒内不能被连接. 会被判定主观下线. 多个sentinel判断主服务器主管下线后才会进行自动故障迁移. 避免网络抖动
- `sentinel failover-timeout mymaster 180000`: 如果18000毫秒内故障迁移失败会寻找新的主服务器. 
- `sentinel parallel-syncs mymaster 1` 故障迁移时, 可以允许从主服务器进行数据复制的从服务器的个数. 如果允许一起复制, 可能会在短时间内造成从服务器集体瘫痪. 如果允许使用过期数据集(slave-server-stale-data), 通过减少并发复制的数量增加从服务器的可用性. 
- sentinel 配置文件中不需要配置从节点和其他sentinel节点信息. 都可以通过`info`从redis 中拉取. 
- sentinel 启动过, 想要重新开始演示. 需要修改sentinel 配置文件. 删除sentinel 生成的组从节点相关信息.
- sentinel myid sdlfjasfldjsaldfhsjdlfshdfljasd sentinel的id, 需要与其他sentinel不同.
- sentinel 本身就是一个redis server 需要自身的端口.

Sentinel相关资料
- redis sentinel 原理介绍：http://www.redis.cn/topics/sentinel.html
- Redis 复制与高可用配置：https://www.cnblogs.com/itzhouq/p/redis5.html

Redis Cluser: 对redis进行分布式操作. 可以扩增redis 的读写量. 通过一致性hash方式, 将数据分散到多个服务器节点: 先设计16384个哈希槽. 分配到多台redis-server. 当需要在redis cluster 存取一个key 时, redis 客户端先对key 使用crc16计算一个数据, 使用数据对16384取模. 存储在模对应的哈希槽中. 哈希槽分布在多态redis-server上.

redis.conf 相关配置
- `cluster-enable yes` 允许redis 使用cluster服务.

cluster 注意事项:
1. 节点使用gossip通信. 节点数最好小于1000. 如果节点太多. 通讯之间的开销(广播的网络通讯效应)比较大.
2. 默认所有槽位都可用才提供服务
3. 一般会配合主从模式使用.

相关资料:
- redis cluster 介绍：http://redisdoc.com/topic/cluster-spec.html
- redis cluster 原理：https://www.cnblogs.com/williamjie/p/11132211.html
- redis cluster 详细配置：https://www.cnblogs.com/renpingsheng/p/9813959.html

#### Redisson介绍

Redisson: 设置在Redis基础上的Java驻内存网格.

在Java端作为Redis的客户端. 有大量有分布式特性的工具和数据结构. 

#### Hazelcast介绍

常见的两种内存网格之一, 另一种是Apache Edgent
vert.x 默认集成了Hazelcast.

hazelcast可以整合server和本地搭建的缓存. 被称为内存网格.

相关问题:
- 每次自动rebalance 几十秒到一分钟的性能抖动.
- 频繁重启, 数据量比较大的情况下导致脑裂.

### 2.从队列到消息服务/消息模式与消息协议(第24节课) 01:02:03

#### 系统间通信方式

- 基于文件
  - 缺点: 明显不方便, 不及时.
- 基于共享内存
- 基于IPC(inter-process Communication 进程间通信)
- 以及Socket
  - 缺点: 使用麻烦, 多数情况下不如RPC
- 基于数据库
  - 缺点: 不及时, 经常有人拿数据库模拟消息队列
- 基于RPC 远程过程调用.
  - 调用关系复杂, 同步处理, 压力大的时候无法缓存.

对系统间通信方式的期望
- 实现异步的消息通信
- 简化参与各方的复杂依赖关系
- 可以再请求量很大的情况下, 缓冲一下
- 保障消息的可靠性, 甚至顺序.

系统间通信方式的别名
- Messge Queue
- Messaging System
- Message Middleware 消息中间件
- Message broken

#### 从队列到消息服务

一个数据是有业务意义, 可以传输就称为消息.

MQ的四大作用
- 异步通信
- 系统解耦
- 削峰平谷:实现一个类似于所谓的被压效果.
  - 被压: 系统防止被压垮, 交接
- 可靠通信:提供多种消息模式, 服务质量, 顺序保障等.

#### 消息模式与消息协议

常见的消息处理模式
- PTP(Point-To-Point). 对应于Queue
- 发布订阅pubSub(public-Subscribe) publisher. 对应于Topic.

QoS(Quality of Service 服务质量 这是消息语义的, 不是业务语义)
- At most one, 至多一次
- At least once, 至少一次. 
- Exactly once, 精确一次, 每条消息肯定被传输一次且仅一次.

消息处理的事务性:
- 通过确认机制实现事务性
- 可以被事务管理器管理, 甚至可以支持XA. 

消息有序性:
- 同一个topic或queue的消息, 保障按顺序投递
- 如果做了消息分区, 或者批量预期之类的操作, 可能就没有顺序了.

继承领域的圣经"企业集成模式".可以认为是SOA/ESB/MQ等理论基础.

消息协议:
- STOMP(Simple(Streaming) text Orientated Messaging Protocol 简单(流)文本定向消息协议)
- JMS(Java Message service J2EE 的一部分, 类似于JDBC)
  - 应用层的API协议.执行了一些接口以及关于消息的实现类.
  - Message结构与Queue概念
    - Body/Header/Property, message Types
    - Queue/Topic
      - 抽象出了父类Destination
      - TemporaryQueue/TemporaryTopic: 为了实现请求/响应, 响应收到一个消息后会返回queue一个消息, 生产者监听这个queue, queue收到消息回复后生产者也能做出反应. 在JMS中这个消息可以封装到requester 对象中. 发起请求后在destination 中获取消息响应.
    - Connection/Session/Producer/Consumer/DurableSubscription. Queue 默认是持久化的. Topic默认消息只在内存中.当一个Topic存在一个DurableSubscribe 时, 它所有消息都会存储起来.
  - Messaging行为
    - PTP&Pub-Sub
    - 持久化
    - 事务机制
    - 确认机制: 自动确认, 手动确认, 批量确认等. 也可以创建一个事务进行确认. 由于消息的有序性, 消息的确认, 消息所在会话的确认是等价的, 确认一个消息时, 会自动确认该消息以前的所在相同会话的消息.
    - 临时队列
- 比较综合集成的协议, 是一个完备的, 多层级的消息协议. 不仅有接口使用的API, 同时规定了跟MQ之间通讯的数据格式. 约束MQ本身的一些行为. 这两个协议的驱动包具有可重用性. 
  - AMQP(Advanced Message Queuing Protocol) 高级消息队列协议. 主要是些大的金融公司发起制定的消息协议. 
  - MQTT(Message Queue Telemetry Transport 消息队列遥感传输) 被IBM发明出来用于物联网的. 交互数据报文特别精简.
- XMPP(Extensible Messaging and Presence Protocol 可扩展通讯和表示协议) 早期互联网基于聊天的即时通讯工具. 基于XML的消息协议 
- Open Messaging. alibaba 提供的相对抽象的一个接口协议. 见的比较少.

消息队列的通用结构: 安全层和管理层可有可无. 
- 安全层
  - 客户端应用层: 发送和接收消息的API接口. 生产者和消费者所使用的JMS
  - 消息模型层: 消息, 连接, 会话, 事务等等. JMS的工作, 只是定义了这些接口. 没有实现, 使用时必须引入实现包.
  - 消息处理层: 消息交互逻辑定义, 持久化. MQ和client 端来回传递的消息格式, 要不要做持久化等. JMS完全没有定义, AMQP, MQTT定义了. 
  - 网络传输层: 序列化协议, 传输协议, 可靠机制. JMS 几乎也没有定义, AMQP, MQTT 也定义了. 
- 管理层

开源消息中间件/消息队列
1. activeMQ(apache)/RabbitMQ(pivotol, Golong开发, 实现了AMQP). AMQP 官方参考实现是Apache Qpid.ActiveMQ 支持很多功能, 支持许多MQ协议.  这代MQ协议:功能比较丰富, 实现的是比较经典的企业集成模式, 堆内存有较高的需求. 不适合大数据量的堆积.
2. Kafka(linkedin 开源归属于apache)/RocketMQ(淘宝最开始把kafka从scala翻译成Java然后自己维护). 基本特点是kafaka原理. 使用磁盘做这种WAL(Write-ahead logging), 不断的叠加顺序写的日志堆积的方式来实现MQ.
3. Apache Pulsar. MQ本身的节点和存储节点的分离.也就是计算存储分离. 

### 3.ActiveMQ消息中间件以及使用示例 22:37

#### ActiveMQ 消息中间件

ActiveMQ 介绍
- ActiveMQ: 高可靠, 事务性的消息队列
- 当前应用最广泛的开源消息中间件
- 项目开始于2005年CodeHaus, 2006年成为Apache项目. 后来与JBoss的HornetQ合并叫做Artemis, 目前是ActiveMQ的子项目.

主要功能
1. 多种语言和协议编写客户端。
  - 语言: Java, C, C++, C#, Ruby, Perl, Python, PHP 等
  - 应用协议: OpenWire,Stomp REST,WS Notification,XMPP,AMQP,MQTT
2. 完全支持JMS1.1和J2EE 1.4规范(持久化，XA 消息，事务)
3. 与Spring 很好地集成，也支持常见J2EE 服务器
4. 支持多种传送协议:in-VM，TCP，SSL，NIO，UDP，Jgroups，JXTA
5. 支持通过JDBC 和journal 提供高速的消息持久化
6. 实现了高性能的集群模式

ActiveMQ
  孵化Scale 语言Apollo
  与JBoss 的HornetQ合并称为Artemis目前是ActiveMQ子项目


#### ActiveMQ 使用示例

使用场景:
1. 所有需要使用消息队列的地方
2. 订单处理, 消息通知, 服务降级等等
3. 特别地, 纯Java实现, 支持嵌入到应用程序. 通过引入ActiveMQServer相关jar包直接在服务器new 出一个ActiveMQServer对象. 应用程序本身变为ActiveMQServer

相关资料
- MQ三个相关以前的PPT会配置在我的教室.
- JMS 介绍：我对JMS的理解和认识: https://kimmking.blog.csdn.n`et/article/details/6577021
- ActiveMQ 官网：https://activemq.apache.org
- ActiveMQ 集群-网络集群模式详解: https://kimmking.blog.csdn.net/article/details/8440150
- ActiveMQ 的集群与高可用: https://kimmking.blog.csdn.net/article/details/13768367

演示相关命令
```
ActiveMQ 配置文件中amqp 的端口可能会和rabbitMQ 端口冲突.

消息消费者的方法中不要讲异常catch掉. 如果消息使用了事务或者需要手动确认, MQ可以通过这个异常知道消息投递失败的, 需要重新投递消息. 
activema start
tail -f data/*.log
localhost:8161
```

### 作业
1.（必做）配置 redis 的主从复制，sentinel 高可用，Cluster 集群。
  - config 配置文件，
  - 启动和操作、验证集群下数据读写的命令步骤。
> 1. redis主从复制
>   1. 配置两个redis 的docker. 每个docker 使用不同的配置文件.
> 2. sentinel 高可用
>   1.运行两个redis 使用sentinel 命令, 并且在配置文件中指定sentinel
> 3. Cluster集群.
>  设置redis 主从复制. 
>     在从redis 中不能添加数据.
>     主redis中添加数据, 从redis 可以读取. 从redis 不能添加数据. 
>     如果刚开始不是主从结构, 主redis 添加数据. 从redis 添加不同值数据. 设置主从后数据发生改变.
> 设置sentinel高可用
>   关闭主redis. 从redis 抛出修改信息. 打开主redis. 从redis 抛出信息
> 配置两个主从redis. 使用官方配置.

2.（选做）练习示例代码里下列类中的作业题:
08cache/redis/src/main/java/io/kimmking/cache/RedisApplication.java

3.（选做☆）练习 redission 的各种功能。

4.（选做☆☆）练习 hazelcast 的各种功能。

5.（选做☆☆☆）搭建 hazelcast 3 节点集群，写入 100 万数据到一个 map，模拟和演 示高可用。

6.（必做）搭建 ActiveMQ 服务，基于 JMS，写代码分别实现对于 queue 和 topic 的消息生产和消费，代码提交到 github。

> 启动activeMQ服务器.
> topic 的发布者, 订阅者
> queue的生产者, 消费者.
> 试着比较queue和topic的不同. 
>   queue 是将消息逐个发送到指定的订阅者. topic 是每次消息都发送给了每个订阅者.


7.（选做）基于数据库的订单表，模拟消息队列处理订单：

一个程序往表里写新订单，标记状态为未处理 (status=0);
另一个程序每隔 100ms 定时从表里读取所有 status=0 的订单，打印一下订单数据，然后改成完成 status=1；
（挑战☆）考虑失败重试策略，考虑多个消费程序如何协作。
8.（选做）将上述订单处理场景，改成使用 ActiveMQ 发送消息处理模式。

9.（选做）使用 java 代码，创建一个 ActiveMQ Broker Server，并测试它。

10.（挑战☆☆）搭建 ActiveMQ 的 network 集群和 master-slave 主从结构。

11.（挑战☆☆☆）基于 ActiveMQ 的 MQTT 实现简单的聊天功能或者 Android 消息推送。

12.（挑战☆）创建一个 RabbitMQ，用 Java 代码实现简单的 AMQP 协议操作。

13.（挑战☆☆）搭建 RabbitMQ 集群，重新实现前面的订单处理。

14.（挑战☆☆☆）使用 Apache Camel 打通上述 ActiveMQ 集群和 RabbitMQ 集群，实现所有写入到 ActiveMQ 上的一个队列 q24 的消息，自动转发到 RabbitMQ。

15.（挑战☆☆☆）压测 ActiveMQ 和 RabbitMQ 的性能。

### 视频教程
#### 120分钟搞懂Redis分布式锁的实现方式
- 分布式锁场景介绍
- 模拟高并发场所: 是有多个客户端请求同一个redis 服务器时, 并发操作.
- 基于redission框架实现分布式锁
- redis主从架构锁失效问题解析
- resission分布式锁实现原理
- redis高并发分布式锁学习指导

## 第十三周 分布式消息

### 1.Kafka的入门和简单使用 48:07


#### Kafka概念和入门

Kafka是一个分布式的, 基于发布/订阅的消息系统.有LinkedIn与2011 年设计研发
1. 以时间复杂度O(1)的方式提供消息持久化能力. 即使大于TB级的数据也可以提供常数级时间复杂度的访问性能
2. 高吞吐率, 即使在非常廉价的商用机上也可以提供单机每秒100K条以上消息的传输
3. 支持Kafka Server间的消息分布, 及分布式消费. 同时保证每个Partition内的消息顺序
4. 同时支持离线数据处理和实时数据处理
5. Scale out: 支持在线水平扩展

Kafka的基本概念
1. Broker：Kafka 集群包含一个或多个服务器，这种服务器被称为broker。
   1. RPC 代理使用Proxy, 云原生, 大数据使用Agent. Kafka使用Broker. 翻译的时候都可以翻译为代理.
   2. broker 是中介, 经纪人. 买股票可能用
   3. proxy 通过桩一样的东西操作另一个东西.
   4. agent 警探. 是一个实体. 一个进程也是一个agent.
2. Topic：每条发布到Kafka 集群的消息都有一个类别，这个类别被称为Topic。（物理上不同 Topic 的消息分开存储，逻辑上一个Topic 的消息虽然保存于一个或多个broker 上，但用户 只需指定消息的Topic 即可生产或消费数据而不必关心数据存于何处）。
3. Partition: Partition 是物理上的概念, 每个Topic可以分成一个或多个Partition.
4. Producer：负责发布消息到Kafka broker。
5. Consumer：消息消费者，向Kafka broker 读取消息的客户端。
6. Consumer Group: 每个Consumer属于一个特定的Consumer Group(可为每个Consumer指定group name, 若不指定group name, 则属于默认group)
7. Replica(副本): 增加kafka容灾能力. 同一个分区不同副本保存的是相同信息. 副本之间是一主多从的关系.
  - leader 负责消息读写. 
  - follower 副本只负责与leader副本同步
  - 分区中所有副本统称为AR(Assigned Replicas)
  - 与leader副本保持一定程度的副本称为ISR(in-sync replica). 一定程度可以配置
  - 与leader副本之后过多的副本称为OSR(Out-of-sync replica)
8. HW(High Watermark 高水位)它标识了一个特定的消息偏移量（offset ），消费者只能拉取到这个offset 之前的消息
9. LEO(Log End  Offset)的缩写，它标识当前日志文件中下一条待写入消息的 offset

- 单机部署结构: 只有一个broker. 消息发送时, 如果consumer 分配在一个组内, 可以指定只需要某个consumer 消费. 如果consumer 分配在多个组. 可以传输给每个组的某个consumer.
- 集群部署结构: 以前的Kafka集群需要zookeeper 管理. 最新版本zookeeper 不在需要, Kafka可以自动管理
- Topic和Partition. **消息在每个partition 中的顺序是怎样的, 可以设定为是否重复么?**
- Partition和Replica. 每个Partition 可以有多个副本, 主副本宕机时, 从副本可以通过选举方式确定主副本. 要求超过半数的副本投票才可以确认. 如果启动时5个副本. 1, 2 认为以1号作为主机.  3,4,5同意3号作为主机. 这就发生了脑裂. **在什么情况下才会发生脑裂的情况, 可能是配置出现错误**.
- Topic 特性
  - 通过partition增加可扩展性
  - 通过顺序写入达到高吞吐
  - 多副本增加容错性.

#### Kafka的简单使用

kafka 内置了一个zookeeper. 演示使用2.7版本. kafka_2.13-2.7.0 中2.13 是kafka使用的scale的版本. win10测试时, 相关命令都在bin\windows 目录下. 且文件名称分隔符是`\`.

启动kafka

```sh
# 启动kafka
# 命令行下进入kafka 目录. 修改配置文件vim config/server.properties. 打开listeners=PLAINTEXT://localhost:9092
# 启动zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties 

# 命令行操作Kafka
# 列出服务器的所有topic
bin/kafka-topics.sh --zookeeper localhost:2181 --list
# 创建副本因子为1, 名称为testk的topic
bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic testk --partitions 3 --replication-factor 1
# 显示名称为testk的topic 描述信息
bin/kafka-topics.sh --zookeeper localhost:2181 --describe --topic testk
  # 执行结果
    # --PartitionCount:3 该topic分为3个Partition. --ReplicationFactor: 副本因子是1, 表示没有副本
    Topic: testk PartitionCount:3 ReplicationFactor:1 Configs:
    # --Partition:0 Partition编号是0, --Leader:0 都在broker 0 上, --Replicas:0 副本数为0, 没有副本. Isr(in-sync replica) 处于同步状态的副本. 和主副本数据一致的从副本的id.
    Topic: testk Partition:0 Leader:0  Replicas:0  Isr:0
# --from-beginning 从开始位置获取消息
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic testk
bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic testk

# 简单性能测试
# 生产消息测试. --throughput 2000 做了限流, 每秒最多2000条消息. --record-size 1000 消息大小为1000字节.
bin/kafka-producer-perf-test.sh --topic testk --num-records 100000 --record-size 1000 --throughput 2000 --producer-props bootstrap.servers=localhost:9092
#测试结果
100000 records sent, 49212.598425 records/sec (46.93 MB/sec), 420.31 ms avg latency, 642.00 ms max latency, 431 ms 50th, 598 ms 95th, 632 ms 99th, 640 ms 99.9th.
# 消费消息测试:--threads 1: 单线程消费
bin/kafka-consumer-perf-test.sh --bootstrap-server localhost:9092 --topic testk --fetch-size 1048576 --messages 100000 --threads 1
# 测试结果
start.time, end.time, data.consumed.in.MB, MB.sec, data.consumed.in.nMsg, nMsg.sec, rebalance.time.ms, fetch.time.ms, fetch.MB.sec, fetch.nMsg.sec
2021-07-19 15:22:20:032, 2021-07-19 15:22:21:011, 95.6164, 97.6674, 100264, 102414.7089, 1626679340444, -1626679339465, -0.0000, -0.0001
```
### 2.Kafka的集群配置 20:16

集群与多副本的说明
1. ISR:In-Sync Replica (in-sync replica) 处于同步状态的副本. 和主副本数据一致的从副本的数.
2. Rebalance: broker和consumer group的rebalance. 在选举新主broker 时,发现从broker 数据不一致, 需要在不同broker之间进行数据迁移确定一致. consumer group 也有这类问题.
3. 热点分区:需要重新平衡. 分区策略有问题, 导致大量数据进入了某个broker, 其他broker 没有数据. 

kafka集群启动:
1. kafka 集群配置文件大部分内容来与config中server.propertis内容相同. **需要修改内容如下**![需要修改内容](images/JAC_kafka_cluster配置文件.png)
2. 清理掉zk 上的所有数据，可以删除zk 的本地文件或者用ZooInspector(删除zooKeeper过去数据的工具) 操作
3. 启动3个kafka：
```sh
# 三个命令行下进入kafka 目录，分别执行
./bin/kafka-server-start.sh kafka9001.properties 
./bin/kafka-server-start.sh kafka9002.properties 
./bin/kafka-server-start.sh kafka9003.properties
```
4. 执行操作测试
```sh
# 创建带有副本的topic：
bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic test32 --partitions 3 --replication-factor 2
bin/kafka-console-producer.sh --bootstrap-server localhost:9003 --topic test32
bin/kafka-console-consumer.sh --bootstrap-server localhost:9001 --topic test32 --from-beginning
# 生产者性能测试：
bin/kafka-producer-perf-test.sh --topic test32 --num-records 100000 --record-size 1000 --throughput 200000 --producer-props bootstrap.servers=localhost:9002
# 测试结果
95441 records sent, 19088.2 records/sec (18.20 MB/sec), 1214.7 ms avg latency, 2095.0 ms max latency.
100000 records sent, 18964.536317 records/sec (18.09 MB/sec), 1232.08 ms avg latency, 2095.00 ms max latency, 1264 ms 50th, 1928 ms 95th, 2064 ms 99th, 2089 ms 99.9th.
# 消费者性能测试
bin/kafka-consumer-perf-test.sh --bootstrap-server localhost:9002 --topic test32 --fetch-size 1048576 --messages 100000 --threads 1
# 测试结果
start.time, end.time, data.consumed.in.MB, MB.sec, data.consumed.in.nMsg, nMsg.sec, rebalance.time.ms, fetch.time.ms, fetch.MB.sec, fetch.nMsg.sec
2021-07-19 21:04:44:316, 2021-07-19 21:04:45:536, 95.4046, 78.2005, 100040, 82000.0000, 1626699885011, -1626699883791, -0.0000, -0.0001
```

### 3.Kafka的高级特性 35:57

- 生产者
  - 执行步骤: 
    - 客户端实现序列化, 分区, 压缩操作. 这也是和第一代MQ的重要区别.另一个区别是第一代不适合大量消息堆积(第二代MQ支持在日志文件中不断堆加数据)
  - 确认模式
    - ack=0:只发送不管有没有写入到broker
    - ack=1: 写入到leader就认为成功
    - ack=-1/all:写入到最小的副本数(大于broker副本数的一半)
  - 特性
    - 同步发送: 客户端发送信息后, 等待broker返回直接执行结果才会继续运行. broker 返回执行结果是指完成了acks的设置, 或者返回一个errors.
      - Future kafkaProducer.send(ProducerRecord). future.get() 等待消息返回后直接执行
      - kafkaProducer.flush(). 强制将消息发送到broker磁盘中
    - 异步发送
      - 设置`pro.put("linger.ms", "1")`等待1ms积攒消息. `pro.put("batch.size", "10240")`批量发送10KB. 这两个条件满足一个就会发送.
        -  linger.ms 默认值为-1表示不等待. 如果调为一个整数, 每次可以发送多条数据,这样可以提高效率. 不过大于1时, 有些消息发送会有一点点迟缓.
      - send 消息默认是异步的. 
      - 也可以使用send 重载方法`send(record, (metadata, exception)->{}})`
    - 顺序保证: kafka 服务器属性`pro.put("max.in.flight.requests.per.connection", "1")` 表明每次连接中发送的消息数量. 设置为1表示每次连接发送一次消息, 直到返回才发送下一次消息. 然后使用同步的`send`和`flush`方法就可以保证消息时顺序的.
    - 消息可靠性传递: 生产者发送broker 的消息默认不会修改. 消费者只读取消息不会删除消息, 如果在消息发送时给加上事务, 可以在事务取消时, 删除刚发送的消息消息(删除是指在broker 的记录中加上标记. 消费者会过滤有标记的消息). 保证多条消息发送同时成功或者同时失败.
    ```java
    pro.put("enable.idempotence", "true"); //此时会把acks设置为all
    pro.put("transaction.id", "tx0001");// 为什么给事务加上一个id
    try{
      kafkaProducer.beginTransaction();
      ProducerRecord record = new ProducerRecord("topic", "key", "value");
      for(int i = 0; i < 100; i++){
        kafkaProducer.send(record, (metadata, exception)->{
          if(exception != null){
            kafkaProducer.abortTransaction();
            throw new KafkaException(exception.getMessage() + ",data:" + record);
          }
        });
      }
      kafkaProducer.commitTransaction();
    }catch(Throwable e){
      kafkaProducer.abortTransaction();
    }
    ```
- 消费者
  - Consumer Group: 将消费者分组, 一个消息只发送给一个组成员. 而且尽可能一个消费者消费一个分区, 或者一个消费者对应多个分区. 当消费者数量大于分区的数量, 某些消费者是空闲的.
    - 消费者组中多个消费者共享一个Partition 时, 原来会在zk 中记录已读消息的偏移量. 新版本中会在一个topic 中记录一个"--commit_offset--"
  - Offset 同步提交: 默认情况下是自动提交. 每拉取一个数据都更新一次offset.
  ```java
  props.put("enable.auto.commit","false");
  while (true) {
    //拉取数据
    ConsumerRecords poll = consumer.poll(Duration.ofMillis(100));
    poll.forEach(o -> {
      ConsumerRecord<String, String> record = (ConsumerRecord) o;
      Order order = JSON.parseObject(record.value(), Order.class);
      System.out.println("order = " + order);
    });
    consumer.commitSync();
  }
  ```
  - Offset 异步提交: 可以拉取多次数据才同步offset
  ```java
  prop.put("enable.auto.commit", "false");
  while (true) {
    //拉取数据
    ConsumerRecords poll = consumer.poll(Duration.ofMillis(100));
    poll.forEach(o -> {
      ConsumerRecord<String, String> record = (ConsumerRecord) o;
      Order order = JSON.parseObject(record.value(), Order.class);
      System.out.println("order = " + order);
    });
    consumer.commitAsync();
  }
  ```
  - Offset 自动提交. 每隔一段时间都同步一次offset
  ```java
  props.put("enable.auto.commit", "true");
  props.put("auto.commit.interval.ms", "5000");
  while (true) {
    //拉取数据
    ConsumerRecords poll = consumer.poll(Duration.ofMillis(100));
    poll.forEach(o -> {
      ConsumerRecord<String, String> record = (ConsumerRecord) o;
      Order order = JSON.parseObject(record.value(), Order.class);
      System.out.println("order = " + order);
    });
  }
  ```
  - Offset Seek: 再均衡, 是指分区从一个消费者转移到另一个消费者的行为. 他为消费者组高可用性和伸缩性提供保障.
  ```java
  props.put("enable.auto.commit", "true");
  //订阅topic
  consumer.subscribe(Arrays.asList("demo-source"), new ConsumerReblanceListener(){
    // 
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions){
      commitOffsetToDB();
    }
    //
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions){
      partitions.forEach(topicPartition -> consumer.seek(topicPartition, getOffsetFromDB(topicPartition)));
    }
  })
  while(true){
    // 拉取数据
    ConsumerRecords poll = consumer.poll(Duration.ofMillis(100));
    poll.forEach(o->{
      processRecord(o);
      saveRecordAndOffsetInDB(o, o.offset());
    });
  }
### 4.RabbitMQ/RocketMQ 56:32

#### RabbitMQ

```
15672
``` 

#### RocketMQ

### 5.Pulsar/EIP/Camel/Spring Integration 49:56

#### Pulsar

基于topic, 支持namespace和多租户. 

#### EIP/Camel/Spring Integration

EIP(Enterprise Integration Parrern)



#### 动手写MQ

第一个版本在09mq.kmq-core 下



### 作业

1.（必做）搭建一个 3 节点 Kafka 集群，测试功能和性能；实现 spring kafka 下对 kafka 集群的操作，将代码提交到 github。
> 部署zookeeper。
> zookeeper 配置文件, kafka集群配置文件. 启动zk和kfa. 测试相关日志. 
> spring kafka 对集群的操作.

2.（选做）安装 kafka-manager 工具，监控 kafka 集群状态。

3.（挑战☆）演练本课提及的各种生产者和消费者特性。
> 生产者, 消费者种类

4.（挑战☆☆☆）Kafka 金融领域实战：在证券或者外汇、数字货币类金融核心交易系统里，对于订单的处理，大概可以分为收单、定序、撮合、清算等步骤。其中我们一般可以用 mq 来实现订单定序，然后将订单发送给撮合模块。
  - 收单：请实现一个订单的 rest 接口，能够接收一个订单 Order 对象；
  - 定序：将 Order 对象写入到 kafka 集群的 order.usd2cny 队列，要求数据有序并且不丢失；
  - 撮合：模拟撮合程序（不需要实现撮合逻辑），从 kafka 获取 order 数据，并打印订单信息，要求可重放, 顺序消费, 消息仅处理一次。
> 非常建议作者到挑战题
5.（选做）自己安装和操作 RabbitMQ，RocketMQ，Pulsar，以及 Camel 和 Spring Integration。

6.（必做）思考和设计自定义 MQ 第二个版本或第三个版本，写代码实现其中至少一个功能点，把设计思路和实现代码，提交到 GitHub。
  - 第一个版本-内存Queue. 基于内存Queue 实现生产和消费API（已经完成）
    - 创建内存BlockingQueue，作为底层消息存储
    - 定义Topic，支持多个Topic
    - 定义Producer，支持Send 消息
    - 定义Consumer，支持Poll消息
  - 第二个版本：自定义Queue. 去掉内存Queue，设计自定义Queue，实现消息确认和消费offset
    - 自定义内存Message 数组模拟Queue。
    - 使用指针记录当前消息写入位置。
    - 对于每个命名消费者，用指针记录消费位置。
  - 第三个版本：基于SpringMVC 实现MQServer. 拆分broker 和client（包括producer 和consumer）
    - 将Queue 保存到web server 端
    - 设计消息读写API 接口，确认接口，提交offset 接口
    - producer 和consumer 通过httpclient 访问Queue
    - 实现消息确认，offset 提交

5）实现consumer 从offset 增量拉取
7.（挑战☆☆☆☆☆）完成所有其他版本的要求。期限一年。
> 这个是期限一年完成所有作业么?

## 第十四周 分布式消息/分布式架构 

### 1.到底什么是架构设计 01:03:59

#### 到底什么是架构设计

#### 系统架构的演化发展

### 2.架构设计形式与方法 42:26

#### 架构设计形式与方法

#### 架构的一些实践经验

### 3.从架构师视角出发 38:35

#### 从架构师视角出发

#### 具体要做哪些事情

### 4.功能性和非功能性 17:27

### 5.如何编写设计文档以及考虑技术选型 32:22

#### 如何编写设计文档

#### 如何考虑技术选型

### 6.其他相关设计要点 23:53

出现问题的原因
1.天灾: 应急预案检测
2.人祸: 总结, 教育.
3.知识盲区

### 本周作业

1.（选做）思考一下自己负责的系统，或者做过的系统，能否描述清楚其架构。

2.（选做）考虑一下，如果让你做一个针对双十一，某东某宝半价抢 100 个 IPhone 的活动系统，你该如何考虑，从什么地方入手。

3.（选做）可以自行学习以下参考书的一两本。
推荐架构书籍：
《软件架构》Mourad Chabane Oussalah
《架构实战 - 软件架构设计的过程》Peter EeLes
《软件系统架构 - 使用视点和视角与利益相关者合作》Nick Rozanski
《企业 IT 架构转型之道》
《大型网站技术架构演进与性能优化》
《银行信息系统架构》
《商业银行分布式架构实践》

4.（选做）针对课上讲解的内容，自己动手设计一个高并发的秒杀系统，讲架构图， 设计文档等，提交到 GitHub。
**
5.（选做）** 针对自己工作的系统，或者自己思考的复杂场景，做系统性的架构设计。

## 第十五周 分布式系统架构

### 如何推动重构A系统 43:05

#### 1.A系统的现状和问题

#### 2.如何推动重构A系统

### 重构的目标和方式 42:06

#### 3.重构的目标和方式

#### 4.重构的过程和结果

#### 5.对于本次重构的复盘

### 程序员升级打怪之路 45:46

#### 1.程序员升级打怪之路

#### 2.夯实自身硬实力技能

### 培养个人全面软实力 52:16

#### 3.培养个人全面软实力

#### 4.努力成为更好的自己

### 作业

1、(必做)分别用 100 个字以上的一段话，加上一幅图 (架构图或脑图)，总结自己
对下列技术的关键点思考和经验认识:
1)JVM
2)NIO
3) 并发编程
4)Spring 和 ORM 等框架
5)MySQL 数据库和 SQL
6) 分库分表
7)RPC和微服务
8) 分布式缓存
9) 分布式消息队列