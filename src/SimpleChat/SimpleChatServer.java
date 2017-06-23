package SimpleChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SimpleChatServer {
	private int port;
	
	public SimpleChatServer(int port) {
		// TODO Auto-generated constructor stub
		this.port=port;
	}

	public void run() {
		// TODO Auto-generated method stub
		//NioEventLoopGroup是用来处理I/O操作的多线程事件循环器
        EventLoopGroup bossGroup=new NioEventLoopGroup();//用来接收进来的连接
        EventLoopGroup workGroup=new NioEventLoopGroup();//用来处理已经被接收的连接
        
        try {
        	//ServerBootstrap是一个启动 NIO 服务的辅助启动类
			 ServerBootstrap b=new ServerBootstrap();
			 b.group(bossGroup, workGroup);
			 b.channel(NioServerSocketChannel.class);
			 b.childHandler(new SimpleChatServerInitializer());//配置一个新的 Channel
			 //option() 是提供给NioServerSocketChannel用来接收进来的连接
			 b.option(ChannelOption.SO_BACKLOG, 128);
			 //childOption() 是提供给由父管道ServerChannel接收到的连接
			 b.childOption(ChannelOption.SO_KEEPALIVE, true);
			 
			 System.out.println("SimpleChatServer 启动了");
			 
			 // 绑定端口，开始接收进来的连接
			 ChannelFuture future=b.bind(port).sync();
			 //关闭你的服务器
			 future.channel().closeFuture().sync();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("SimpleChatServer 启动异常");
		}finally{
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			
			System.out.println("SimpleChatServer 关闭了");
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        int port;
        if(args.length>0){
        	port=Integer.parseInt(args[0]);
        }else{
        	port=8080;
        }
        new SimpleChatServer(port).run();
	}

}
