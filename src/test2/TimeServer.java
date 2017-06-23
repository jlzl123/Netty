package test2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
	private int port;

	public TimeServer(int port) {
		// TODO Auto-generated constructor stub
		this.port = port;
	}

	public void run() {
		// TODO Auto-generated method stub
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup work = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, work)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
                        //匿名内部类，配值channel
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							// TODO Auto-generated method stub
							ch.pipeline().addLast("decoder", new TimeDecoder());
							ch.pipeline().addLast("encoder",new TimeEncoder());
							ch.pipeline().addLast("handler",new TimeServerHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture cf = b.bind(port).sync();
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			work.shutdownGracefully();
			boss.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		int port;
		if(args.length>0){
			port=Integer.parseInt(args[0]);
		}else{
			port=8080;
		}
		new TimeServer(port).run();
	}
}
