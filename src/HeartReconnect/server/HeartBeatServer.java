package HeartReconnect.server;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import HeartReconnect.AcceptorIdleStateTrigger;

public class HeartBeatServer {
	private final AcceptorIdleStateTrigger idleStateTrigger=new AcceptorIdleStateTrigger();
	private int port;
	
	public HeartBeatServer(int port) {
		// TODO Auto-generated constructor stub
		this.port=port;
	}
	
	public void start(){
		EventLoopGroup boosGroup=new NioEventLoopGroup();
		EventLoopGroup workGroup=new NioEventLoopGroup();
		ServerBootstrap bootstrap=new ServerBootstrap();
		bootstrap.group(boosGroup, workGroup)
		         .channel(NioServerSocketChannel.class)
		         .handler(new LoggingHandler(LogLevel.INFO))//日志记录的handler
		         .localAddress(new InetSocketAddress(port))
		         .option(ChannelOption.SO_BACKLOG, 128)
		         .childOption(ChannelOption.SO_KEEPALIVE, true)
		         .childHandler(new ChannelInitializer<SocketChannel>() {
		        	 @Override
		        	protected void initChannel(SocketChannel ch)
		        			throws Exception {
		        		// TODO Auto-generated method stub
		        		ChannelPipeline p=ch.pipeline();
		        		p.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
		        		p.addLast(idleStateTrigger);
		        		p.addLast("decode", new StringDecoder());
		        		p.addLast("encode", new StringEncoder());
		        		p.addLast(new HeartBeatServerHandler());
		        	}
				});
		try {
			ChannelFuture future=bootstrap.bind(port).sync();
			System.out.println("Server start listen at " + port); 
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			boosGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		new HeartBeatServer(8080).start();
	}
}
