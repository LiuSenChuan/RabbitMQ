package rabbitmq.rpc;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.CancelCallback;

public class RPCClient {
	public static BlockingQueue<Long> q =new ArrayBlockingQueue<>(10);

	public static void main(String[] args) throws Exception{
		System.out.println("求第几个斐波那契数:");
		int  n = new Scanner(System.in).nextInt();
		long  r = fbnq(n);
		System.out.println(r);
	}
	
	public static long fbnq(int n) throws Exception {
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
				
				//1.发送调用信息
				c.queueDeclare("rpc_queue", false, false, false, null);
				//返回队列名和关联id
				String replyTo = c.queueDeclare().getQueue();
				String correlationId = UUID.randomUUID().toString();
				//将返回队列名和关联id放置属性里面
				BasicProperties props = 
						new BasicProperties()
						.builder()
						.replyTo(replyTo)
						.correlationId(correlationId)
						.build();
				c.basicPublish("", "rpc_queue", props, (""+n).getBytes());
				
				//2.模拟继续执行主线程其他运算
				System.out.println("其他运算.........");
				//3.直到取到结果时,返回结果
				DeliverCallback deliverCallback = new DeliverCallback() {
					@Override
					public void handle(String consumerTag, Delivery message) throws IOException {
					//返回的斐波那契数结果与关联id
						String msg = new String(message.getBody());
						String cid = message.getProperties().getCorrelationId();
						if (cid.equals(correlationId)) {
							//消息处理线程放数据
							q.offer(Long.parseLong(msg));
						}
						try {
							c.close();
						} catch (Exception e) {
							e.printStackTrace();
						} 
						con.close();
					}
				};
				CancelCallback cancelCallback = new CancelCallback() {
					@Override
					public void handle(String consumerTag) throws IOException {
					}
				};
				
				c.basicConsume(replyTo,true, deliverCallback, cancelCallback);
				
				//主线程拿数据
				return q.take();
	}
}
