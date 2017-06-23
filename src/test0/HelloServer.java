package test0;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloServer {
	//����˼����Ķ˿ڵ�ַ
	private static final int portNumber = 7878;

	public static void main(String[] args) {
		//EventLoopGroup,����Channel���ӣ�bossGroup�������ս���������
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		//Worker�߳����ڹ����߳�ΪBoss�̷߳���workGroup���������Ѿ������յ����ӣ�
		EventLoopGroup workGroup = new NioEventLoopGroup();

		try {
			//NIO ����ĸ���������
			ServerBootstrap sb = new ServerBootstrap();
			sb.group(bossGroup, workGroup);
			sb.channel(NioServerSocketChannel.class);
			sb.childHandler(new HelloServerInitializer());

			// �������󶨶˿ڼ���
			ChannelFuture cf = sb.bind(portNumber).sync();
			// �����������رռ���
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
