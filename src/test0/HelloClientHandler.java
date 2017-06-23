package test0;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HelloClientHandler extends SimpleChannelInboundHandler<String>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Server say:"+msg);
	}
	
	@Override//和server建立连接是调用
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Client active");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Client close");
		super.channelInactive(ctx);
		
	}
}
