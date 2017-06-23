package test0;

import java.net.InetAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
//handler 是由 Netty 生成用来处理 I/O 事件的。
public class HelloServerHandler extends SimpleChannelInboundHandler<String>{
//ChannelInboundHandlerAdapter 类提供了许多事件处理的方法
	
	@Override//收到新的数据时,调用该方法
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		// TODO Auto-generated method stub
		// 收到消息直接打印输出
		System.out.println(ctx.channel().remoteAddress()+" Say:"+msg);
		// 返回客户端消息 - 我已经接收到了你的消息
		ctx.writeAndFlush("Received your message !\n");
	}

	//channelActive 在连接被建立并且准备进行通信时调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("RemoteAddress:"+ctx.channel().remoteAddress()+" active!");
		//向客户端发送欢迎信息
		ctx.writeAndFlush("Welcome to "+InetAddress.getLocalHost().getHostName()+" service!\n");
		super.channelActive(ctx);
	}
		
}
