package test2;

import java.util.Date;
//ʹ�� POJO ���� ByteBuf
public class Time {
	private final long value;

	public Time() {
		// TODO Auto-generated constructor stub
		this(System.currentTimeMillis()/1000L+2208988800L);//����һ�������Ĺ��췽��
	}
	
	public Time(long value){
		this.value=value;
	}
	
	public long getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new Date((getValue() - 2208988800L) * 1000L).toString();
	}
}
