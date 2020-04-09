package rabbitmq.publishsubscribe;

import java.io.IOException;

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
		
		
		//1.定义队列   fanout类型的队列   随机命名,非持久,独占,自动删除
		String queue = c.queueDeclare().getQueue();
		//2.定义交换机
		c.exchangeDeclare("logs", "fanout");
		//3.绑定
		c.queueBind(queue, "logs", "");//发布订阅模式中第三个参数无效
		
		DeliverCallback deliverCallback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
                String msg = new String(message.getBody()); 
                System.out.println("收到:"+msg);
			}
		};
		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		//消费数据
		c.basicConsume(queue, true, deliverCallback, cancelCallback);
	}
}
