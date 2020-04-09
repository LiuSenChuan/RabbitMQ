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
		//��������
				ConnectionFactory f = new ConnectionFactory();
				f.setHost("192.168.64.140");
				f.setPort(5672);
				f.setUsername("admin");
				f.setPassword("admin");
				
				//��������
				Connection con = f.newConnection();
				//����ͨ��
				Channel c = con.createChannel();
				//���彻����
				c.exchangeDeclare("topic-logs", BuiltinExchangeType.TOPIC);
				//�������
				String queue = c.queueDeclare().getQueue();
				//��
				System.out.println("��ȡ�󶨽�,�ÿո����:");
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
