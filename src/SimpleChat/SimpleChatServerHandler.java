package SimpleChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
//������
public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String>{
	//ȡ�����пͻ��˵�ͨ��
	public static ChannelGroup channels=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	//���ӷ�����յ��µĿͻ�������ʱ����
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();//ȡ�õ�ǰͨ��
		for(Channel channel:channels){//��ÿ�����ߵĿͻ��˷�����Ϣ
			channel.writeAndFlush("[SERVER]-"+incoming.remoteAddress()+"����\n");
		}
		channels.add(incoming);//���¼���Ŀͻ���Channel����ChannelGroup
	}
	
	//���ӷ�����յ��ͻ��˶Ͽ�ʱ����
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		for(Channel channel:channels){
			channel.writeAndFlush("[SERVER-]"+incoming.remoteAddress()+"�뿪\n");
		}
		channels.remove(incoming);//�ͻ��˵� Channel �Ƴ� ChannelGroup �б���
	}
	
	//����˼������ͻ��˻ʱ����
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"����");
	}

	//���ӷ���˶����ͻ���д����Ϣʱ����
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String str)
			throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		//�ӷ���˶����ͻ���д����Ϣʱ������Ϣת���������ͻ��˵� Channel��
		for(Channel channel:channels){
			if(channel!=incoming){
				channel.writeAndFlush("["+incoming.remoteAddress()+"]:"+str+"\n");
			}else{
				channel.writeAndFlush("[you]:"+str+"\n");
			}
		}
	}

	//����˼������ͻ��˲��ʱ����
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"����");
	}
	
	//������ Throwable ����Żᱻ����
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"�쳣");
		cause.printStackTrace();
		ctx.close();
	}
}
