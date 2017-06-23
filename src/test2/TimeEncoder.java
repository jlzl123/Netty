package test2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;

public class TimeEncoder extends MessageToByteEncoder<Time>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Time msg, ByteBuf out)
			throws Exception {
		// TODO Auto-generated method stub
		out.writeInt((int) msg.getValue());
	}
	
}
