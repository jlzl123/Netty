package test0;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class HelloServerInitializer extends ChannelInitializer<SocketChannel>{

	@Override//����һ���µ� Channel
	protected void initChannel(SocketChannel sc) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline=sc.pipeline();
		// ��("\n")Ϊ��β�ָ�� ������
		pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		
		// �ַ������� �� ����
		pipeline.addLast("decoder",new StringDecoder());
		pipeline.addLast("encoder",new StringEncoder());
		// �Լ����߼�Handler
		pipeline.addLast("handler",new HelloServerHandler());
	}

}
