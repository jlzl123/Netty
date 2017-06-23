package reConnect;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
		System.out.println("--- Server is active ---");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		 System.out.println("10s 之后尝试重新连接服务器...");
	     Thread.sleep(10 * 1000);
	     Client.connect();
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		// TODO Auto-generated method stub
		sendHeartbeatPacket(ctx);
	}
	
	private void sendHeartbeatPacket(ChannelHandlerContext ctx) {
		// TODO Auto-generated method stub
		
	}
}
