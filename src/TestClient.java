import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


public class TestClient {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		EventLoopGroup group=new NioEventLoopGroup();
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				// TODO Auto-generated method stub
				ChannelPipeline pipeline=sc.pipeline();
				pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192,Delimiters.lineDelimiter()));
			    pipeline.addLast("encoder",new StringEncoder());
			    pipeline.addLast("decoder",new StringDecoder());
			    pipeline.addLast("handler", new ClientHandler());
			}
        	
		});
        
        Channel channel=bootstrap.connect("58.249.57.253", 10002).sync().channel();
        channel.writeAndFlush("test");
        channel.closeFuture().sync();
        group.shutdownGracefully();
	}

}

class ClientHandler extends SimpleChannelInboundHandler<String>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		System.out.println("server say:"+msg);		
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
		System.out.println("client Active");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
		System.out.println("client Inactive");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
		System.out.println("¡¨Ω”“Ï≥£");
	}
}
