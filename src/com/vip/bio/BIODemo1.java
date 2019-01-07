package cn.tedu.bio;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BIODemo1 {
	public static void main(String[] args) throws Exception {
		ServerSocket ss = new ServerSocket();
		ss.bind(new InetSocketAddress(9999));
		
		Socket s = ss.accept();
		
//		while(true){}
		
		InputStream in = s.getInputStream();
		
		in.read();
	}
}
