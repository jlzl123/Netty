package HeartReconnect;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter{

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		// TODO Auto-generated method stub
		if(evt instanceof IdleStateEvent){
			IdleState state=((IdleStateEvent) evt).state();
			if(state==IdleState.READER_IDLE){
				throw new Exception("idle exception");
			}
		}
		super.userEventTriggered(ctx, evt);
	}
}
