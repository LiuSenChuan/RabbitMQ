package rabbitmq.topic;

import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Receive {

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
				//定义队列
				String queue = c.queueDeclare().getQueue();
				//绑定
				System.out.println("获取绑定建,用空格隔开:");
				String s = new Scanner(System.in).nextLine();
				String[] b = s.split("\\s+");
				for (String key : b) {
					c.queueBind(queue, "topic-logs", key);
				}
				DeliverCallback deliverCallback = new DeliverCallback() {
					@Override
					public void handle(String consumerTag, Delivery message) throws IOException {
						String msg = new String(message.getBody());
						String key = message.getEnvelope().getRoutingKey();
						System.out.println(key + "-" +msg);
					}
				};
				CancelCallback cancelCallback = new CancelCallback() {
					
					@Override
					public void handle(String consumerTag) throws IOException {
						// TODO Auto-generated method stub
						
					}
				};
				c.basicConsume(queue, true, deliverCallback, cancelCallback);
				
	}
}
