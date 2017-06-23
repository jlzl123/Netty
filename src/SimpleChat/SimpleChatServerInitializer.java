package SimpleChat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class SimpleChatServerInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline=ch.pipeline();
		// 以("\n")为结尾分割的 解码器
		pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast("decoder",new StringDecoder());
		pipeline.addLast("encoder",new StringEncoder());
		pipeline.addLast("handler",new SimpleChatServerHandler());
		
		System.out.println("SimpleChatClient:"+ch.remoteAddress()+"连接上");
	}

}
