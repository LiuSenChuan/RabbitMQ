package rabbitmq.rpc;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RPCServer {

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
		//1.接收调用信息
		//2.执行业务运算
		//3.吧结果发回去
		//定义队列
		c.queueDeclare("rpc_queue", false, false, false, null);
		
		DeliverCallback deliverCallback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
               String s = new String(message.getBody());
               int n = Integer.parseInt(s);
               //求第N个斐波那契数
               long r =fbnq(n);
               
               //去出返回队列名和关联id
               //replyTo返回队列名
               String replyTo = message.getProperties().getReplyTo();
               String correlationId  = message.getProperties().getCorrelationId();
               //将correlationId放置BasicProperties属性
               BasicProperties props = new BasicProperties().builder().correlationId(correlationId).build();
               c.basicPublish("", replyTo, props, (""+r).getBytes());
			}
		};
		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		
		c.basicConsume("rpc_queue", true, deliverCallback, cancelCallback);
		
		
	}
	//求第n个斐波那契数
	//1,1,2,3,5,8.....
	static long fbnq(int n) {
		if (n == 1 || n == 2) {
			return 1;
		}
		return fbnq(n-1)+fbnq(n-2);
	}
	
}
