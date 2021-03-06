package test2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class TimeServerHandler extends ChannelInboundHandlerAdapter{
  
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		//ByteBuf 作为协议消息的主要数据结构
//		final ByteBuf time=ctx.alloc().buffer(4);//生成4个字节的buf对象
//		time.writeInt((int) (System.currentTimeMillis()/1000L+2208988800L));
		//writeAndFlush会返回一个ChannelFuture对象
		final ChannelFuture f=ctx.writeAndFlush(new Time());
//		f.addListener(new ChannelFutureListener() {
//			
//			@Override
//			public void operationComplete(ChannelFuture future) throws Exception {
//				// TODO Auto-generated method stub
//				assert f==future;//断言
//				ctx.close();
//			}
//		});
		f.addListener(ChannelFutureListener.CLOSE);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		ctx.close();
	}
}
