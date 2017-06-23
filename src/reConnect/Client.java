package reConnect;


import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Client {
	private static Channel channel;
	private static Bootstrap bootstrap;
	private static ChannelFutureListener channelFutureListener;
	
	public static void main(String[] args) {
		bootstrap=new Bootstrap();
		NioEventLoopGroup group=new NioEventLoopGroup();
		bootstrap.group(group);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				// TODO Auto-generated method stub
				ChannelPipeline p=ch.pipeline();
				p.addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
				p.addLast("decoder",new StringDecoder());
				p.addLast("encoder",new StringEncoder());
				p.addLast("handler",new ClientHandler());
			}
		});
		channelFutureListener=new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture f) throws Exception {
				// TODO Auto-generated method stub
				if(f.isSuccess()){
					System.out.println("连接成功");
				}else{
					System.out.println("连接失败，3秒后重新连接");
//					Thread.sleep(3000);
//					connect();
					f.channel().eventLoop().schedule(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							connect();
						}
					}, 3, TimeUnit.SECONDS);
				}
			}
		};
		connect();
	}
	
	public static void connect() {
		//			channel=bootstrap.connect("127.0.0.1", 8000).sync().channel();
		ChannelFuture channelFuture=bootstrap.connect("127.0.0.1", 8000);
		channelFuture.addListener(channelFutureListener);
	}
}
