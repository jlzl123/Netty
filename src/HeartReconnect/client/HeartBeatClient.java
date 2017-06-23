package HeartReconnect.client;

import java.util.concurrent.TimeUnit;

import HeartReconnect.ConnectionIdleStateTrigger;
import HeartReconnect.ConnectionWatchDog;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
/*
 * 心跳检测
 * 1、添加一个IdelStateHandler,每四秒检测是否对服务端有写操作，若没有，则执行idleStateTrigger的userEventTriggered方法，发送一个心跳包给服务端
 * 重连
 * 1、添加一个重连检测狗ConnectionWatchDog，它继承ChannelInboundHandlerAdapter，属于handler，实现TimerTask
 * 2、当连接断开后，执行watchdog的channelInactive方法，通过Timer添加一个定时任务
 * 3、在run方法里重连服务器，并在channelfuture中添加一个ChannelFutureListener，监听重连是否成功 
 * 4、若不成功，执行watchdog连接断开事件  ，添加定时任务，继续重连。
 */
public class HeartBeatClient {
	protected final HashedWheelTimer timer=new HashedWheelTimer();
	private Bootstrap boot;
	private final ConnectionIdleStateTrigger idleStateTrigger=new ConnectionIdleStateTrigger();
	
	public void connect(String host,int port) throws Exception{
		EventLoopGroup group=new NioEventLoopGroup();
		boot=new Bootstrap();
		boot.group(group)
		    .channel(NioSocketChannel.class)
		    .handler(new LoggingHandler(LogLevel.INFO));
		final ConnectionWatchDog watchdog=new ConnectionWatchDog(boot, timer, port, host, true) {
			
			@Override
			public ChannelHandler[] handlers() {
				// TODO Auto-generated method stub
				return new ChannelHandler[]{
						this,//watchdog
						//客户端每4秒检测是否对服务端有写操作，若没有则执行idleStateTrigger的userEventTriggered方法
						new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS),
						idleStateTrigger,
						new StringDecoder(),
						new StringEncoder(),
						new HeartBeatClientHandler()
				};
			}
		};
		
		ChannelFuture future;
		synchronized(boot){
			boot.handler(new ChannelInitializer<Channel>() {
				
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(watchdog.handlers());
				};
			});
			
			future=boot.connect(host,port);
		}
		
		future.sync();
	}
	
	public static void main(String[] args) {
		try {
			new HeartBeatClient().connect("127.0.0.1", 8080);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
