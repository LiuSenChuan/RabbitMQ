package rabbitmq.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Receive {

	//消费者
	public static void main(String[] args) throws IOException, TimeoutException {
		//连接rabbitmq服务器
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		//建立连接
		Connection con = f.newConnection();
		//建立连接通道
		Channel c = con.createChannel();
		 
	   //声明队列定义队列
		c.queueDeclare("hellworld", false, false, false, null);
		
		//收到消息后用来处理消息的回调
		DeliverCallback deliverCallback =new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				byte[] a = message.getBody();
				String s = new String(a);
				System.out.println("消息已收到:"+ s);
			}
		};
		//消费者取消时的回调函数
		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
				
			}
		};
		//开始消费消息
		c.basicConsume("hellworld",true , deliverCallback,cancelCallback);
	}
}
