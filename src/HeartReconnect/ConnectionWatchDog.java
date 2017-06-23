package HeartReconnect;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

/**
 * 
 * 重连检测狗，当发现当前的链路不稳定关闭之后，进行12次重连。抽象类
 */
@Sharable//Sharable表示此对象在channel间共享
public abstract class ConnectionWatchDog extends ChannelInboundHandlerAdapter implements TimerTask,ChannelHandlerHolder{
	
	private final Bootstrap bootstrap;
	private final Timer timer;
	private final int port;
	private final String host;
	
	private volatile boolean reconnect=true;
	private int attempts;
	
	public ConnectionWatchDog(Bootstrap bootstrap,Timer timer,int port,String host,boolean reconnect) {
		// TODO Auto-generated constructor stub
		this.bootstrap=bootstrap;
		this.timer=timer;
		this.port=port;
		this.host=host;
		this.reconnect=reconnect;
	}

	/**
     * channel链路每次active的时候，将其连接的次数重新☞ 0
     */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("当前链路已经激活了，重连尝试次数重新置为0");
		attempts=0;
		ctx.fireChannelActive();
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("链接关闭");  
		if(reconnect){
			System.out.println("链接关闭，将进行重连");
			attempts++;
			//重连的间隔时间会越来越长
			int timeout=2<<attempts;//<<位运算符，表示向左移一位
			/*
			 * Timer：netty的定时器管理
			 * newTimeout方法:生成一个定时任务
			 *  参数TimerTask:执行逻辑，卸载run方法中；timeout：时间，表是多久后执行；TimeUnit：时间单位。
			 */                
			timer.newTimeout(this, timeout, TimeUnit.SECONDS);
		}
		ctx.fireChannelInactive();//执行handler连接断开事件
	}

	@Override
	public void run(Timeout timeout) throws Exception {
		// TODO Auto-generated method stub
		ChannelFuture future;
		//bootstrap已经初始化好了，只需要将handler填入就可以了
		synchronized(bootstrap){
			bootstrap.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					// TODO Auto-generated method stub
					ch.pipeline().addLast(handlers());
				}
			});
			future=bootstrap.connect(host, port);
		}	
		future.addListener(new ChannelFutureListener() {
			
			public void operationComplete(ChannelFuture future) throws Exception {
				// TODO Auto-generated method stub
				boolean succed=future.isSuccess();
				//如果重连失败，则调用ChannelInactive方法，再次出发重连事件，一直尝试12次，如果失败则不再重连
				if(!succed){
					System.out.println("重连失败");  
                    future.channel().pipeline().fireChannelInactive();//执行watchdog连接断开事件  
				}else{
					System.out.println("重连成功");  
				}
			}
		});
		
//		future.channel().closeFuture().sync();
	}

}