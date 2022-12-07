# Java11

## 1、jshell（java9开始支持）

java9开始引入jshell这个交互性工具，让Java也可以像脚本语言一样来运行，可以从控制台启动jshell，在jshell中直接输入表达式并查看其执行结果。当需要测试一个方法的运行效果，或是快速的对表达式进行求值时，jshell都非常实用。

## 2、var 

​	局部变量的类型推断

注意点  1、var a; 这样不行，因为无法推断

​			  2、类型的数据类型不可以使用var

## 3、Stream增强

Stream是java 8中的新特性，Java 9 开始增加以下4个新方法

1、增加单个参数构造方法，可为null

Stream.ofNullable(null).count() ; //0

2、增加 takeWhile和dropWhile

3、iterate重载

这个iterate方法的新重载方法，可以让你提供一个Predicate(判断条件)来指定什么时候结束迭代



## 4、字符串操作

1、去除空格

​		//去重字符串首尾的空白，包括英文和其他所有语言中的空白字符
​		String.strip()

2、重复字符

"Java".repeat(5)

3、统计行数

String.lines().count()



5、垃圾收集器

1、新的Epsilon垃圾收集器

JDK上对这个特性的描述是：开发一个处理内存分配但不实现任何实际内存回收机制的GC，一旦可用堆内存用完，JVM就会退出。

如果有System.gc()调用，实际上什么也不会发生(这种场景下和-XX:+DisableExplicitGC效果是一样的)，因为没有内存回收，这个实现可能会警告用户尝试强制GC是徒劳的。

用法

-XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC



如果使用选项-XX:+UseEpsilonGC，程序很快就因为堆空间不足而退出

使用这个选项的原因：

提供完全被动的GC实现，具有有限的分配限制和尽可能低的延迟开销，但代价是内存占用和内存吞吐量。



主要用途

1、性能测试 可以帮助过滤掉GC引起的性能假象

2、内存压力测试 知道测试用例，应该分配不超过1GB的内存，我们可以使用-Xmx1g  -XX:+UseEpsilonGC，如果程序有问题，则程序就会崩溃

3、非常短的JOB任务 对象这种任务，接受GC清理堆那都是浪费空间

4、VM接口测试

5、Last-drop 延迟&吞吐改进



2、ZGC垃圾收集器

ZGC的设计目标

​	支持TB级内存容量，暂停时间第(<10ms)，对整个程序吞吐量的影响小于15%.

ZGC是一个并发，基于region，压缩型的垃圾收集器，只有root扫描阶段会STW，因此GC停顿时间不会随着堆的增长和存活对象的增长而变长。

用法

​	-XX:+UnlockExperimentalVMOptions -XX:+UseGC，因此ZGC还处于实验阶段，所以需要通过JVM参数来解锁这个特性。

