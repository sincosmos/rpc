# RPC 学习笔记
* 相关标签 Socket, Java NIO, Netty, Zookeeper, Dubbo
RPC 全称为 Remote Procedure Call，即远程过程调用，它是一个计算机通信协议。它允许像调用本地服务一样调用远程服务，主要应用在分布式系统。RPC 框架
从实现角度看，一个 RPC 框架需要考虑以下几个方面。
1) 通信模型，在 Java 中一般基于 BIO 或 NIO;
2) 过程（服务）定位，即在目标机器（给定 IP 和端口）上找到被调用的方法；
3) 远程代理，在 Java 中使用动态代理实现；
4) 序列化，例如 protobuf, Arvo 等

## Socket
1. TCP/IP 是传输层的一种通信协议；Socket 是传输层通信协议的抽象实现，提供 bind, listen, accept 等标准 API；基于 Sokcet 可以用来实现 TCP/IP 协议，也可以用来实现 UDP 协议等传输层通信协议。
2. 一个服务器的 ServerSocket 监听一个端口的客户端连接请求，可以同时支持多个客户端连接。ServerSocket 的 accept() 方法会返回一个代表了当前客户端连接

## Java NIO
1. NIO 主要使用 channels and buffers 进行读写服务
2. 抽象类 ByteBuffer 的实现类是唯一和 channel 直接进行交互的 buffer 
3. Aside from its content, the essential properties of a buffer are its capacity, limit, and position. 
   A buffer's mark is the index to which its position will be reset when the reset method is invoked.
   capacity: buffer 的容量，最大能够存储的字节数，创建 buffer 时指定，不能改变，例如`ByteBuffer.allocate(10)`创建了容量为 10 个字节的 buffer;
   limit: 可以理解为 buffer 中当前有效字节个数，由于 ByteBuffer 底层时字节数组(可以使用`array()`方法获得底层数组)，limit 也是第一个无效的字节的索引；
   position: 可以理解为下一个要被读/写的字节的索引，会随着`get()`和`put()`方法的调用而增加/减小；
   mark: 一个备忘位置，调用`mark()`方法的话，mark 值将存储当前 position 的值，等下次调用`reset()`方法时，会设定 position 的值为之前的标记值；
   下面是几个常用的 buffer 操作方法
   ``` java
   /**
    * 反转 buffer，通常在要读 buffer 中数据的时候使用，以便从 buffer 头部开始读
    */
   ByteBuffer flip() {
       limit = position;
       position = 0;
       mark = -1;
       return this;
   }
   
   /**
    * 反转 buffer，保持 limit 不变，通常用于要多次重复读 buffer 中的数据时使用
    */
   Buffer rewind() {
       position = 0;
       mark = -1;
       return this;
   }
   
   /**
    * 置空 buffer 供新的读入
    */
   Buffer clear() {
       position = 0;
       limit = capacity;
       mark = -1;
       return this;
   }
   ```
4. ByteBuffer 支持对 primitive data 的读写操作（`getChar()/putChar(), getInt()/putInt()`等）。NIO 还定义了 CharBuffer, 
   IntBuffer, StringCharBuffer 等类型的 buffer，但这些 buffer 是不直接和 channel 进行交互的。可以使用`asCharBuffer()`等方法将
   ByteBuffer 转换为其它类型的 Buffer。 CharBuffer, IntBuffer, StringCharBuffer 等类型的 buffer 的 `get()/put()` 操作返回/传入
   的是相应类型的元素，不必像 ByteBuffer 一样，针对 byte 类型数据进行操作
5. Java IO 中的 FileInputStream, FileOutputStream 和 RandomAccessFile 都提供了 getChannel() 操作，返回一个 FileChannel，支持将
   传统 IO 流转化为 NIO 的 channel 进行操作
6. ByteBuffer 类提供了`put()`系列方法将数据写入buffer（`FileChannel.read(ByteBuffer buffer)`最终也是使用`put()`将数据写入 buffer）。
   此外，还有 ByteBuffer.wrap(byte[] array) 方法将数据写入 buffer，此时，array 本身将作为 ByteBuffer 的底层存储，而不是重新生成一个
   数据将 array 的内容拷贝进去
7. Channel 和流相似，只不过 channel 可以读也可以写，而流一般是单向的（只能读或写）；并且 channel 总是基于 ByteBuffer 进行读写
8. 除了前面的 FileChannel，常见的还有 SocketChannel 和 ServerSocketChannel 用于 TCP 网络连接的套接字
9. Java NIO 的 Pipe 是两个线程间单向传递数据的连接，其应用场景一般都可以被 Selector 取代，并不常用
10. Java NIO Selector 用于检查一个或多个 NIO Channel 的状态是否处于可读、可写。如此可以实现单线程管理多个非阻塞的 Channel 
   （因此 FileChannel 不适用 Selector，因为它不能切换为非阻塞模式。FileChannel 不支持非阻塞的原因是 Unix 操作系统本身不支持文件的非阻塞IO)，
    也就是可以管理多个网络连接 SocketChannel（单线程 IO 多路复用）。
    
## Netty