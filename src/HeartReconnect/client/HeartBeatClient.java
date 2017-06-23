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
 * �������
 * 1�����һ��IdelStateHandler,ÿ�������Ƿ�Է������д��������û�У���ִ��idleStateTrigger��userEventTriggered����������һ���������������
 * ����
 * 1�����һ��������⹷ConnectionWatchDog�����̳�ChannelInboundHandlerAdapter������handler��ʵ��TimerTask
 * 2�������ӶϿ���ִ��watchdog��channelInactive������ͨ��Timer���һ����ʱ����
 * 3����run����������������������channelfuture�����һ��ChannelFutureListener�����������Ƿ�ɹ� 
 * 4�������ɹ���ִ��watchdog���ӶϿ��¼�  ����Ӷ�ʱ���񣬼���������
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
						//�ͻ���ÿ4�����Ƿ�Է������д��������û����ִ��idleStateTrigger��userEventTriggered����
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
