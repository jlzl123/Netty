package SimpleChat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SimpleChatClientHandler extends SimpleChannelInboundHandler<String>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String str)//str�������ķ���������������Ϣ
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println(str);//��ӡ���ܵ�server����Ϣ
	}

}
