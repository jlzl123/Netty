package HeartReconnect;

import io.netty.channel.ChannelHandler;

public interface ChannelHandlerHolder {

	ChannelHandler[] handlers();
}
