package rabbitmq.workqueue;

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
		c.queueDeclare("hellworld", true, false, false, null);
		
		//收到消息后用来处理消息的回调
		DeliverCallback deliverCallback =new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String s = new String(message.getBody());
				System.out.println("消息已收到:"+ s);
				for (int i = 0; i < s.length(); i++) {
					//每遇到一个.字符暂停一秒
					if (s.charAt(i)=='.') {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				//手动ack操作,手动向服务器发送回执   false表示是否确认多条消息(否)
				c.basicAck(message.getEnvelope().getDeliveryTag(), false);
				System.out.println("消息处理结束---------------------\n");
			}
		};
		//消费者取消时的回调函数
		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
				
			}
		};
		//设置每次只接受一条数据
		c.basicQos(1);
		//开始消费消息
		//第二个参数true  自动ack  false  手动ack
		c.basicConsume("hellworld",false , deliverCallback,cancelCallback);
	}
}
