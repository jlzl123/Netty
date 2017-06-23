package test1;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpChannel;

public class HelloClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        Bootstrap bootStrap=new Bootstrap();
        bootStrap.channel(NioSctpChannel.class);
        bootStrap.handler(new HelloClientHandler());
        bootStrap.group(new NioEventLoopGroup());
        bootStrap.connect(new InetSocketAddress("127.0.0.1", 8000));
	}

	private static class HelloClientHandler extends
	        ChannelInboundHandlerAdapter{
		
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			// TODO Auto-generated method stub
			System. out .println("Hello world, I'm client.");
		}
	}
}
