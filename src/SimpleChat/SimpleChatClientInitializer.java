package SimpleChat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class SimpleChatClientInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline p=ch.pipeline();
		p.addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		p.addLast("decoder",new StringDecoder());
		p.addLast("encoder",new StringEncoder());
		p.addLast("handler",new SimpleChatClientHandler());
			
	}

}
