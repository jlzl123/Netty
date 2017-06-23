package SimpleChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SimpleChatServer {
	private int port;
	
	public SimpleChatServer(int port) {
		// TODO Auto-generated constructor stub
		this.port=port;
	}

	public void run() {
		// TODO Auto-generated method stub
		//NioEventLoopGroup����������I/O�����Ķ��߳��¼�ѭ����
        EventLoopGroup bossGroup=new NioEventLoopGroup();//�������ս���������
        EventLoopGroup workGroup=new NioEventLoopGroup();//���������Ѿ������յ�����
        
        try {
        	//ServerBootstrap��һ������ NIO ����ĸ���������
			 ServerBootstrap b=new ServerBootstrap();
			 b.group(bossGroup, workGroup);
			 b.channel(NioServerSocketChannel.class);
			 b.childHandler(new SimpleChatServerInitializer());//����һ���µ� Channel
			 //option() ���ṩ��NioServerSocketChannel�������ս���������
			 b.option(ChannelOption.SO_BACKLOG, 128);
			 //childOption() ���ṩ���ɸ��ܵ�ServerChannel���յ�������
			 b.childOption(ChannelOption.SO_KEEPALIVE, true);
			 
			 System.out.println("SimpleChatServer ������");
			 
			 // �󶨶˿ڣ���ʼ���ս���������
			 ChannelFuture future=b.bind(port).sync();
			 //�ر���ķ�����
			 future.channel().closeFuture().sync();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("SimpleChatServer �����쳣");
		}finally{
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			
			System.out.println("SimpleChatServer �ر���");
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        int port;
        if(args.length>0){
        	port=Integer.parseInt(args[0]);
        }else{
        	port=8080;
        }
        new SimpleChatServer(port).run();
	}

}
