package test0;

import java.net.InetAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
//handler ���� Netty ������������ I/O �¼��ġ�
public class HelloServerHandler extends SimpleChannelInboundHandler<String>{
//ChannelInboundHandlerAdapter ���ṩ������¼�����ķ���
	
	@Override//�յ��µ�����ʱ,���ø÷���
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		// TODO Auto-generated method stub
		// �յ���Ϣֱ�Ӵ�ӡ���
		System.out.println(ctx.channel().remoteAddress()+" Say:"+msg);
		// ���ؿͻ�����Ϣ - ���Ѿ����յ��������Ϣ
		ctx.writeAndFlush("Received your message !\n");
	}

	//channelActive �����ӱ���������׼������ͨ��ʱ����
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("RemoteAddress:"+ctx.channel().remoteAddress()+" active!");
		//��ͻ��˷��ͻ�ӭ��Ϣ
		ctx.writeAndFlush("Welcome to "+InetAddress.getLocalHost().getHostName()+" service!\n");
		super.channelActive(ctx);
	}
		
}
