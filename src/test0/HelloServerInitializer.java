package test0;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class HelloServerInitializer extends ChannelInitializer<SocketChannel>{

	@Override//配置一个新的 Channel
	protected void initChannel(SocketChannel sc) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline=sc.pipeline();
		// 以("\n")为结尾分割的 解码器
		pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		
		// 字符串解码 和 编码
		pipeline.addLast("decoder",new StringDecoder());
		pipeline.addLast("encoder",new StringEncoder());
		// 自己的逻辑Handler
		pipeline.addLast("handler",new HelloServerHandler());
	}

}
