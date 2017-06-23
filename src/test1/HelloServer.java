package test1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
//quartserver测试服务器
public class HelloServer {

	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootStrap = new ServerBootstrap();
			bootStrap.group(bossGroup, workGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel socketChannel)
								throws Exception {
							// TODO Auto-generated method stub
							socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(
									1024,true,Delimiters.lineDelimiter()));			
							socketChannel.pipeline().addLast(new StringDecoder());
							socketChannel.pipeline().addLast(new StringEncoder());
							socketChannel.pipeline().addLast(new HelloServerHandler());
						}
					}).childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = bootStrap.bind(12138).sync();

			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}

	}

	private static class HelloServerHandler extends
			ChannelInboundHandlerAdapter {

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			// TODO Auto-generated method stub
			System.out.println("Netty服务器收到数据:" + msg.toString());
		}
	}
}
