package test0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class HelloClient {
	public static String host = "127.0.0.1";
	public static int port = 7878;
	public static Bootstrap bt=null;
	public static ChannelFutureListener listener;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			bt = new Bootstrap();
			bt.group(group);
			bt.channel(NioSocketChannel.class);
			bt.handler(new HelloClientInitilizer());

			//���ӷ�������
			ChannelFuture future=bt.connect(host, port);
			Channel channel= future.sync().channel();
			
//			listener=new ChannelFutureListener() {
//				
//				@Override
//				public void operationComplete(ChannelFuture f) throws Exception {
//					// TODO Auto-generated method stub
//					if(f.isSuccess()){
//						System.out.println("�������ӷ������ɹ�");
//					}else{
//						f.channel().eventLoop().schedule(new Runnable() {
//							
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								ChannelFuture fut=bt.connect(host,port);
//								fut.addListener(listener);
//							}
//						}, 3, TimeUnit.SECONDS);
//					}
//				}
//			};
//			future.addListener(listener);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			for (;;) {
				String line = br.readLine();
				if (line == null || line==" ") {
					continue;//���Ϊ�գ��Ͳ���server������Ϣ
				}
                /*
                 * �����˷����ڿ���̨������ı� ����"\r\n"��β
                 * ֮������\r\n��β ����Ϊ������handler������� DelimiterBasedFrameDecoder ֡���롣
                 * �����������һ������\n����λ�ָ����Ľ�����������ÿ����Ϣ�����������\n�����޷�ʶ��ͽ���
                 */
				channel.writeAndFlush(line + "\r\n");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			group.shutdownGracefully();
		}
	}

}
