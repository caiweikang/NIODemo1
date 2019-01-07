package cn.tedu.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class BIODemo2 {
	public static void main(String[] args) throws IOException {
		Socket s = new Socket();
		s.connect(new InetSocketAddress("127.0.0.1", 9999));
		
		while(true){}
		
//		OutputStream out = s.getOutputStream();
//		
//		for(int i=1;i<=Integer.MAX_VALUE;i++){
//			out.write("a".getBytes());
//			System.out.println("-----"+i);
//		}
		
		
		
	}
}
