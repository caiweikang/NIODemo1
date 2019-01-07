package cn.tedu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServerDemo1 {
	public static void main(String[] args) throws Exception {
		// 1.����ѡ������ͨ��ȫ��Ψһ
		Selector selc = Selector.open();
		
		// 2.����ͨ��
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		// 3.ָ�������Ķ˿�
		ssc.socket().bind(new InetSocketAddress(9999));

		// 4.��ͨ��ע�ᵽselc�Ϲ�עACCEPT�¼�
		ssc.register(selc, SelectionKey.OP_ACCEPT);

		//--��ʼѭ������select���������������sk
		while(true){
			// 5.�ж�ע���ͨ���Ƿ���׼������
			int selcCount = selc.select();
			if(selcCount > 0){
				//--ѡ����Ѿ�������sk
				Set<SelectionKey> set = selc.selectedKeys();
				//--��������sk��Ӧ��ͨ�����¼�
				Iterator<SelectionKey> it = set.iterator();
				while(it.hasNext()){
						//--������ÿһ��������sk
						SelectionKey sk = it.next();
						//--����skע��Ĳ�ͬ���ֱ���
						if(sk.isAcceptable()){//�����һ��ACCEPT����
							//--��ȡsk��Ӧ��channel
							ServerSocketChannel sscx = (ServerSocketChannel) sk.channel();
							//--�������ӣ��õ�sc
							SocketChannel sc = sscx.accept();
							//--����sc�ķ�����ģʽ
							sc.configureBlocking(false);
							//--��scע�ᵽselc�ϣ���עREAD����
							sc.register(selc, SelectionKey.OP_READ);
						}else if(sk.isConnectable()){//�����һ��Connect����
							
						}else if(sk.isReadable()){//�����һ��Read����
							//--��ȡsk��Ӧ��ͨ��
							SocketChannel sc = (SocketChannel) sk.channel();

							//--��ȡͷ��Ϣ����֪��ĳ���
							ByteBuffer temp = ByteBuffer.allocate(1);
							String head = "";
							while(!head.endsWith("\r\n")){
								sc.read(temp);
								head += new String(temp.array());
								temp.clear();
							}
							int len = Integer.parseInt(head.substring(0,head.length()-2));
							
							//׼����������������
							ByteBuffer buf = ByteBuffer.allocate(len);
							while(buf.hasRemaining()){
								sc.read(buf);
							}
							
							//��ӡ����
							String msg = new String(buf.array(),"utf-8");
							System.out.println("�������յ��˿ͻ���["+sc.socket().getInetAddress().getHostAddress()+"]���������ݣ�"+msg);
							
						}else if(sk.isWritable()){//�����һ��Write����
							
						}else{//�����ͱ���
							throw new RuntimeException("NIO������ʽ����");
						}
					//--����ѡ�������ɾ���Ѵ������ļ�
					it.remove();
				}
				
			}
		}
	}
}
