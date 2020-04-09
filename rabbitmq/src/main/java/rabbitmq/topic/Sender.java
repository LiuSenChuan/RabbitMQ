package rabbitmq.topic;

import java.util.Scanner;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {
	//主题模式(topic)
	public static void main(String[] args) throws Exception{
		//建立连接
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		//创建链接
		Connection con = f.newConnection();
		//建立通道
		Channel c = con.createChannel();
		//定义交换机
		c.exchangeDeclare("topic-logs", BuiltinExchangeType.TOPIC);
		
		while (true) {
			System.out.println("输入消息:");
			String msg = new Scanner(System.in).next();
			System.out.println("输入key:");
			String key = new Scanner(System.in).next();
			
			c.basicPublish("topic-logs", key, null, msg.getBytes());
		}
		
	}
}
