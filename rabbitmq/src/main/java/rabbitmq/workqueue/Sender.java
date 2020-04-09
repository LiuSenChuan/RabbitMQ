package rabbitmq.workqueue;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Sender {

	//生产者
	public static void main(String[] args) throws IOException, TimeoutException {
		//链接rabbitmq服务器
		ConnectionFactory f =new ConnectionFactory();
		f.setHost("192.168.64.140");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		//创建连接
		Connection con = f.newConnection();
		//建立通道
		Channel c= con.createChannel();

	    //声明队列
		c.queueDeclare("hellworld", false, false, false, null);
		while (true) {
			System.out.println("输入:");
			String s = new Scanner(System.in).nextLine();
			//发送消息
			c.basicPublish("", "hellworld", MessageProperties.PERSISTENT_TEXT_PLAIN,s.getBytes());
		}
		
		
	}
}
