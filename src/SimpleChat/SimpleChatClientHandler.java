package SimpleChat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SimpleChatClientHandler extends SimpleChannelInboundHandler<String>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String str)//str，读到的服务器传过来的信息
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println(str);//打印接受到server的信息
	}

}
