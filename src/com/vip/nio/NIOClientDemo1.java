package cn.tedu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClientDemo1 {
	public static void main(String[] args) throws Exception {
		// 1.创建选择器，通常全局唯一
		Selector selc = Selector.open();
		
		// 2.创建通道
		SocketChannel sc = SocketChannel.open();
		// 开启非阻塞模式
		sc.configureBlocking(false);
		// 3.将通道注册到selc上关注CONNECT事件
		sc.register(selc, SelectionKey.OP_CONNECT);
		// 4.要发送的ip端口
		sc.connect(new InetSocketAddress("127.0.0.1",9999));
		
		//--开始循环进行select操作
		while(true){
			// 5.判断注册的通道是否已准备就绪
			int selcCount = selc.select();
			if(selcCount > 0){
				//--获取已经就绪的sk
				Set<SelectionKey> set = selc.selectedKeys();
				//--遍历处理sk
				Iterator<SelectionKey> it = set.iterator();
				while(it.hasNext()){
					//--获取遍历到的键
					SelectionKey sk = it.next();
					//--获取键对应的通道
					SocketChannel scx = (SocketChannel) sk.channel();
					if(sk.isConnectable()){//是一个CONNECT操作
						//--完成连接
						scx.finishConnect();
						//--将sc注册到selc上关注WRITE操作
						scx.register(selc, SelectionKey.OP_WRITE);
					}else if(sk.isWritable()){//是一个WRITE操作
						//--待发送的数据
						String str = "愿你一生温暖纯良，不舍爱与自由！";
						//--处理协议
						String sendStr = str.getBytes("utf-8").length +"\r\n" + str;
						//--发送数据
						ByteBuffer buf = ByteBuffer.wrap(sendStr.getBytes("utf-8"));
						while(buf.hasRemaining()){
							scx.write(buf);
						}
						//--取消注册
						sk.cancel();
					}
				}
				//--从已选择键集中删除已经处理完成的键
				it.remove();
			}
		}
	}
}
