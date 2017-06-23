package SimpleChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
//处理器
public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String>{
	//取得所有客户端的通道
	public static ChannelGroup channels=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	//当从服务端收到新的客户端连接时调用
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();//取得当前通道
		for(Channel channel:channels){//给每个在线的客户端发送信息
			channel.writeAndFlush("[SERVER]-"+incoming.remoteAddress()+"加入\n");
		}
		channels.add(incoming);//将新加入的客户端Channel加入ChannelGroup
	}
	
	//当从服务端收到客户端断开时调用
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		for(Channel channel:channels){
			channel.writeAndFlush("[SERVER-]"+incoming.remoteAddress()+"离开\n");
		}
		channels.remove(incoming);//客户端的 Channel 移除 ChannelGroup 列表中
	}
	
	//服务端监听到客户端活动时调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"在线");
	}

	//当从服务端读到客户端写入信息时调用
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String str)
			throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		//从服务端读到客户端写入信息时，将信息转发给其他客户端的 Channel。
		for(Channel channel:channels){
			if(channel!=incoming){
				channel.writeAndFlush("["+incoming.remoteAddress()+"]:"+str+"\n");
			}else{
				channel.writeAndFlush("[you]:"+str+"\n");
			}
		}
	}

	//服务端监听到客户端不活动时调用
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"下线");
	}
	
	//当出现 Throwable 对象才会被调用
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"异常");
		cause.printStackTrace();
		ctx.close();
	}
}
