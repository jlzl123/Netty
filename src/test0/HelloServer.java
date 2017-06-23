package test0;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloServer {
	//服务端监听的端口地址
	private static final int portNumber = 7878;

	public static void main(String[] args) {
		//EventLoopGroup,管理Channel连接，bossGroup用来接收进来的连接
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		//Worker线程用于管理线程为Boss线程服务。workGroup用来处理已经被接收的连接，
		EventLoopGroup workGroup = new NioEventLoopGroup();

		try {
			//NIO 服务的辅助启动类
			ServerBootstrap sb = new ServerBootstrap();
			sb.group(bossGroup, workGroup);
			sb.channel(NioServerSocketChannel.class);
			sb.childHandler(new HelloServerInitializer());

			// 服务器绑定端口监听
			ChannelFuture cf = sb.bind(portNumber).sync();
			// 监听服务器关闭监听
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
