# Java8 新特性

http://www.runoob.com/java/java8-new-features.html

# 阻塞队列

*	阻塞队列BlockingQueue是java.util.concurrent包里的接口表示一个线程安全放入和提取实例的队列
*	一个线程将会持续生产新对象并将其插入到队列之中，直到队列达到它所能容纳的临界点。
	也就是说，它是有限的。
	如果该阻塞队列到达了其临界点，负责生产的线程将会在往里边插入新对象时发生阻塞。
	它会一直处于阻塞之中，直到负责消费的线程从队列中拿走一个对象。

*	负责消费的线程将会一直从该阻塞队列中拿出对象。
	如果消费线程尝试去从一个空的队列中提取对象的话，这个消费线程将会处于阻塞之中，直到一个生产线程把一个对象丢进队列。

*	BlockingQueue常用API说明：
	BlockingQueue 具有 4 组不同的方法用于插入、移除以及对队列中的元素进行检查。
	如果请求的操作不能得到立即执行的话，每个方法的表现也不同。这些方法如下：
```
操作	抛异常		特定值		阻塞			超时
插入	add(o)		offer(o)	put(o)		offer(o, timeout, timeunit)
移除	remove(o)	poll(o)		take(o)		poll(timeout, timeunit)
检查	element(o)	peek(o)		不可用		不可用
```
四组不同的行为方式解释：
抛异常：如果试图的操作无法立即执行，抛一个异常。
特定值：如果试图的操作无法立即执行，返回一个特定的值(常常是 true / false)。
阻塞：如果试图的操作无法立即执行，该方法调用将会发生阻塞，直到能够执行。
超时：如果试图的操作无法立即执行，该方法调用将会发生阻塞，直到能够执行，但等待时间不会超过给定值。
	返回一个特定值以告知该操作是否成功(典型的是 true / false)。

*	无法向一个 BlockingQueue 中插入 null。如果你试图插入 null，BlockingQueue 将会抛出一个 NullPointerException。 
*	可以访问到 BlockingQueue 中的所有元素，而不仅仅是开始和结束的元素。
	比如说，你将一个对象放入队列之中以等待处理，但你的应用想要将其取消掉。
*	可以调用诸如 remove(o) 方法来将队列之中的特定对象进行移除。
	但是这么干效率并不高(译者注：基于队列的数据结构，获取除开始或结束位置的其他对象的效率不会太高)，因此你尽量不要用这一类的方法，除非你确实不得不那么做。
	
*	BlockingQueue的实现:
BlockingQueue 是个接口，你需要使用它的实现之一来使用 BlockingQueue。
java.util.concurrent 具有以下 BlockingQueue 接口的实现(Java 6)：
ArrayBlockingQueue
DelayQueue
LinkedBlockingQueue
PriorityBlockingQueue
SynchronousQueue

# 闭锁 CountDownLatch

java.util.concurrent.CountDownLatch 是一个并发构造，它允许一个或多个线程等待一系列指定操作的完成。 
CountDownLatch 以一个给定的数量初始化。countDown() 每被调用一次，这一数量就减一。
通过调用 await() 方法之一，线程可以阻塞等待这一数量到达零。

# 栅栏 CyclicBarrier
java.util.concurrent.CyclicBarrier 类是一种同步机制，它能够对处理一些算法的线程实现同步。
换句话讲，它就是一个所有线程必须等待的一个栅栏，直到所有线程都到达这里，然后所有线程才可以继续做其他事情