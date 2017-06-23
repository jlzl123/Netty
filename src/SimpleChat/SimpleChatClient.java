package SimpleChat;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class SimpleChatClient {
	private String host;
	private int port;
	
	public SimpleChatClient(String host,int port) {
		// TODO Auto-generated constructor stub
	    this.host=host;
	    this.port=port;
	}

	public void run() {
		// TODO Auto-generated method stub
        EventLoopGroup group=new NioEventLoopGroup();
        try{
        	Bootstrap bootstrap=new Bootstrap();//配置整个Netty程序，串联起各个组件。
        	bootstrap.group(group);
        	bootstrap.channel(NioSocketChannel.class);
        	bootstrap.handler(new SimpleChatClientInitializer());
        	
        	Channel channel=bootstrap.connect(host, port).sync().channel();
        	
        	BufferedReader buff=new BufferedReader(new InputStreamReader(System.in));
        	while(true){
        		String line=buff.readLine();
        		if(line!=null){
        			channel.writeAndFlush(line+"\r\n");     			
        		}
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	group.shutdownGracefully();
        }
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        new SimpleChatClient("127.0.0.1", 8080).run();
	}

}
